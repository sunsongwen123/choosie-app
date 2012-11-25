package com.choosie.app.controllers;

import com.choosie.app.Constants;
import com.choosie.app.R;
import com.choosie.app.R.id;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FeedScreenController extends ScreenController {

	private FeedListAdapter choosiePostsItemAdapter;

	public FeedScreenController(View layout, Activity activity,
			SuperController superController) {
		super(layout, activity, superController);
	}

	@Override
	protected void onCreate() {
		Log.i(Constants.LOG_TAG, "Feed.onShow()");
		// Create a progress bar to display while the list loads
		TextView textView = new TextView(this.getActivity());
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		textView.setText("Nothing to display. Please check back later.");

		ListView listView = (ListView) view.findViewById(R.id.feedListView);
		listView.setEmptyView(textView);

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) view.findViewById(R.id.layout_feed);
		root.addView(textView);
		choosiePostsItemAdapter = new FeedListAdapter(getActivity(), R.id.layout_me,
				this.superController);
		listView.setAdapter(choosiePostsItemAdapter);

		listView.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				/* maybe add a padding */
				boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
				if (loadMore) {
					choosiePostsItemAdapter.appendToList();
				}
			}

		});
	}

	@Override
	protected void onShow() {
		refresh();
	}

	@Override
	protected void onHide() {

	}

	@Override
	public void refresh() {
		// TODO Refresh if needed only.
		choosiePostsItemAdapter.refreshFeed();
	}

}
