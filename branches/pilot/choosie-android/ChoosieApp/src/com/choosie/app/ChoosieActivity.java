package com.choosie.app;

import com.choosie.app.controllers.SuperController;
import com.choosie.app.Models.FacebookDetails;
import com.facebook.Session;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class ChoosieActivity extends Activity {

	SuperController superController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosie);

		Utils.makeMainDirectory();

		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Inflate FEED xml and add it to layout_feed
		RelativeLayout layoutFeed = (RelativeLayout) findViewById(R.id.layout_feed);
		layoutFeed.addView(layoutInflater.inflate(R.layout.screen_feed, null));

		// Inflate FEED xml and add it to layout_post
		RelativeLayout layoutPost = (RelativeLayout) findViewById(R.id.layout_post);
		layoutPost.addView(layoutInflater.inflate(R.layout.screen_post, null));

		// Inflate FEED xml and add it to layout_post
		RelativeLayout layoutMe = (RelativeLayout) findViewById(R.id.layout_me);
		layoutMe.addView(layoutInflater.inflate(R.layout.screen_me, null));

		FacebookDetails fbDetails = (FacebookDetails) getIntent()
				.getSerializableExtra("fb_details");

		superController = new SuperController(this, fbDetails);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_choosie, menu);
		return true;
	}

	public void onBottomNavBarButtonClick(View view) {
		switch (view.getId()) {
		case R.id.layout_button_feed:
		case R.id.layout_button_image_feed:
			superController.switchToScreen(Screen.FEED);
			break;
		case R.id.layout_button_post:
		case R.id.layout_button_image_post:
			superController.switchToScreen(Screen.POST);
			break;

		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		Log.i(Constants.LOG_TAG, "after activity "
				+ Session.getActiveSession().getPermissions().toString());

		if ((requestCode == Constants.RequestCodes.TAKE_FIRST_PICTURE_FROM_CAMERA)
				|| requestCode == Constants.RequestCodes.TAKE_SECOND_PICTURE_FROM_CAMERA
				|| requestCode == Constants.RequestCodes.TAKE_FIRST_PICTURE_FROM_GALLERY
				|| requestCode == Constants.RequestCodes.TAKE_SECOND_PICTURE_FROM_GALLERY
				|| requestCode == Constants.RequestCodes.CROP_FIRST
				|| requestCode == Constants.RequestCodes.CROP_SECOND) {
			superController.getControllerForScreen(Screen.POST)
					.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == Constants.RequestCodes.COMMENT) {
			superController.onActivityResult(resultCode, data);
		}
		if (requestCode == Constants.RequestCodes.FB_REQUEST_PUBLISH_PERMISSION) {
			Log.i(Constants.LOG_TAG, "after activity fb");
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (superController.getCurrentScreen()) {
		case POST:
			superController.getControllerForScreen(Screen.POST).onResume();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (superController.getCurrentScreen()) {
		case POST:
			superController.getControllerForScreen(Screen.POST).onKeyDown(
					keyCode, event);
			break;
		case FEED:
			finish();
			break;
		}

		return true;
	}

}
