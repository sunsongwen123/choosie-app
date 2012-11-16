package com.choosie.app;

import java.util.List;

import com.choosie.app.ChoosieClient.ChoosiePostData;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class FeedScreenController extends ScreenController {

	public FeedScreenController(View layout, Activity activity) {
		super(layout, activity);
		
	}


	@Override
	protected void onCreate() {
		Log.i(ChoosieConstants.LOG_TAG, "Feed.onShow()");
		refreshFeed();
	}
	
	@Override
	protected void onShow() {
		Log.i(ChoosieConstants.LOG_TAG, "Feed.onShow()");
		refreshFeed();
	}

	@Override
	protected void onHide() {
		// TODO Auto-generated method stub

	}

	private void refreshFeed() {
		this.client.getFeedFromServer(new Callback<List<ChoosiePostData>>() {

			@Override
			void handleOperationFinished(List<ChoosiePostData> param) {
				loadPosts(param);
			}
		});

	}

	protected void loadPosts(List<ChoosiePostData> param) {
		Log.i(ChoosieConstants.LOG_TAG, "Feed before getPictureFromServer");
		if (param.size() == 0) {
			Log.i(ChoosieConstants.LOG_TAG, "No images in feed.");
		}
		loadPost(param.get(0));
	}

	private void loadPost(ChoosiePostData post) {
		loadImageToView(post.photo1URL, (ImageView)view.findViewById(R.id.feedimage1));
		loadImageToView(post.photo2URL, (ImageView)view.findViewById(R.id.feedimage2));
		((TextView)view.findViewById(R.id.feedtext)).setText(post.question);
	}

	private void loadImageToView(String urlToLoad, final ImageView imageView) {
		this.client.getPictureFromServer(urlToLoad, new Callback<Bitmap>() {

			@Override
			void handleOperationFinished(Bitmap param) {
				Log.i(ChoosieConstants.LOG_TAG,
						"Feed after getPictureFromServer");
				imageView.setImageBitmap(param);
			}
		});
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	

}
