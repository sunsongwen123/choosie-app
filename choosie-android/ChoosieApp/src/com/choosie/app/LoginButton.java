/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.choosie.app;

import com.choosie.app.SessionEvents.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LoginButton extends ImageButton {

	private Facebook mFacebook;
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;
	private int mActivityCode;

	public LoginButton(Context context) {
		super(context);
	}

	public LoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(final Activity activity, final int activityCode,
			final Facebook fb) {
		init(activity, activityCode, fb, new String[] {});
	}

	public void init(final Activity activity, final int activityCode,
			final Facebook fb, final String[] permissions) {
		mActivity = activity;
		mActivityCode = activityCode;
		mFacebook = fb;
		mPermissions = permissions;
		mHandler = new Handler();

		setBackgroundColor(Color.TRANSPARENT);
		setImageResource(fb.isSessionValid() ? R.drawable.logout_button
				: R.drawable.login_button);
		drawableStateChanged();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		setOnClickListener(new ButtonOnClickListener());
	}

	@SuppressWarnings("deprecation")
	private final class ButtonOnClickListener implements OnClickListener {
		/*
		 * Source Tag: login_tag
		 */

		public void onClick(View arg0) {
			if (mFacebook.isSessionValid()) {
				SessionEvents.onLogoutBegin();
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
						mFacebook);
				asyncRunner.logout(getContext(), new LogoutRequestListener());
			} else {
				mFacebook.authorize(mActivity, mPermissions, mActivityCode,
						new LoginDialogListener());
				Log.i(ChoosieConstants.LOG_TAG, "After authorize");
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			Log.i(ChoosieConstants.LOG_TAG, "onComplete");
			SessionEvents.onLoginSuccess();
		}

		public void onFacebookError(FacebookError error) {
			Log.i(ChoosieConstants.LOG_TAG, "onFacebookError");
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			Log.i(ChoosieConstants.LOG_TAG, "onError");
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			Log.i(ChoosieConstants.LOG_TAG, "onCancel");
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			setImageResource(R.drawable.logout_button);
			SessionStore.save(mFacebook, getContext());
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(getContext());
			setImageResource(R.drawable.login_button);
		}
	}

}
