package com.choozie.app.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.choozie.app.AppSettings;
import com.choozie.app.Callback;
import com.choozie.app.L;
import com.choozie.app.NewChoosiePostData;
import com.choozie.app.controllers.FeedCacheKey;
import com.choozie.app.models.ChoosiePostData;
import com.choozie.app.models.FacebookDetails;
import com.choozie.app.models.User;
import com.choozie.app.models.UserDetails;

public abstract class Client {

	private UserDetails userDetails;

	private Context context;

	private static Client instance = new RealClient();

	protected Client() {
	}

	public static Client getInstance() {
		return instance;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public abstract void sendCommentToServer(String post_key, String text,
			final Callback<Void, Void, Boolean> callback);

	public abstract void sendVoteToServer(String postKey, int whichPhoto,
			final Callback<Void, Void, Boolean> callback);

	public abstract void login(final Callback<Void, Void, Void> onLoginComplete);

	public Bitmap getPictureFromServerSync(final String pictureUrl,
			Callback<Void, Integer, Void> progressCallback) {

		String urlToLoad = pictureUrl;
		L.d("getPictureFromServer: Loading URL: " + urlToLoad);
		URL url;
		try {
			url = new URL(urlToLoad);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		progressCallback.onProgress(Integer.valueOf(1));
		try {
			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			byte[] buffer = downloadFile(progressCallback, connection);
			progressCallback.onProgress(Integer.valueOf(99));
			Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0,
					buffer.length);
			L.d("getPictureFromServer: returning bitmap = " + bitmap);
			return bitmap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			L.d("getPictureFromServer: returning null");
			e.printStackTrace();
			return null;
		}
	}

	public abstract FeedResponse getFeedByCursor(FeedCacheKey feedRequest,
			Callback<Void, Integer, Void> progressCallback);

	public abstract void sendChoosiePostToServer(NewChoosiePostData data,
			Callback<Void, Integer, Void> progressCallback);

	public abstract ChoosiePostData getPostByKey(String param,
			Callback<Void, Integer, Void> progressCallback);

	public FacebookDetails getFacebookDetailsOfLoogedInUser() {
		return AppSettings.getFacebookDetailsOfLoggedInUser(context);
	}

	public void setFacebookDetails(FacebookDetails fbDetails) {
		AppSettings.saveFacebookDetailsOfLoggedInUser(context, fbDetails);
	}

	private byte[] downloadFile(Callback<Void, Integer, Void> progressCallback,
			HttpURLConnection connection) throws IOException {
		int imageSize = connection.getContentLength();
		// Sometimes the size isn't known: just read the stream.
		if (imageSize <= 0) {
			ByteArrayBuffer buf = new ByteArrayBuffer(1024);
			int b;
			while ((b = connection.getInputStream().read()) > -1) {
				buf.append(b);
			}
			return buf.toByteArray();
		}
		// When it is known, read the stream and public progress.
		progressCallback.onProgress(Integer.valueOf(2));
		InputStream input = connection.getInputStream();
		final int kBufferSize = 1024;
		byte[] buffer = new byte[imageSize];
		int downloaded = 0;
		int bytesRead = 0;
		while (bytesRead > -1) {
			int remaining = imageSize - downloaded;
			int toReadThisIteration = Math.min(remaining, kBufferSize);
			bytesRead = input.read(buffer, downloaded, toReadThisIteration);
			if (bytesRead >= 0) {
				downloaded += bytesRead;
				progressCallback.onProgress(100 * downloaded / imageSize);
			}
		}
		return buffer;
	}

	public abstract void registerGCM(String deviceId);

	public abstract void unregisterGCM(String registrationId);

	public abstract User getActiveUser();

	public abstract void getUserDetailsFromServer(User user,
			Callback<Void, Void, UserDetails> callback);

	public abstract void updateUserDetailsInfo(UserDetails userDetails,
			Callback<Void, Void, Void> callback);

	public UserDetails getActiveUserDetails() {
		if (userDetails != null)
			L.i("getActiveUserDetails() - " + userDetails.toString());
		else {
			L.i("getActiveUserDetails() - activeUserDetails is null");
		}
		return userDetails;
	}

	public void setActiveUserDetails(UserDetails userDetails) {
		L.i("setActiveUserDetails() - " + userDetails.toString());
		this.userDetails = userDetails;
	}

}