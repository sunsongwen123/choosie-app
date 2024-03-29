package com.choosie.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.choosie.app.Caches;
import com.choosie.app.Callback;
import com.choosie.app.ChoosieActivity;
import com.choosie.app.CommentScreen;
import com.choosie.app.Constants;
import com.choosie.app.IntentData;

import com.choosie.app.EnlargePhotoActivity;
import com.choosie.app.R;
import com.choosie.app.Screen;
import com.choosie.app.Utils;
import com.choosie.app.VotesScreenActivity;
import com.choosie.app.client.RealClient;
import com.choosie.app.client.ClientBase;
import com.choosie.app.Models.ChoosiePostData;
import com.choosie.app.Models.Comment;
import com.choosie.app.Models.FacebookDetails;
import com.choosie.app.Models.Vote;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

public class SuperController {
	private Screen currentScreen;
	private Activity activity;
	private ClientBase client;
	private final Caches caches = new Caches(this);
	Map<Screen, ScreenController> screenToController;

	public SuperController(Activity activity, FacebookDetails fbDetails) {
		this.activity = activity;
		client = new RealClient(fbDetails);

		List<Pair<Screen, ScreenController>> screenControllerPairs = new ArrayList<Pair<Screen, ScreenController>>();

		screenControllerPairs.add(new Pair<Screen, ScreenController>(
				Screen.FEED, new FeedScreenController(activity
						.findViewById(R.id.layout_feed), this)));
		screenControllerPairs.add(new Pair<Screen, ScreenController>(
				Screen.POST, new PostScreenController(activity
						.findViewById(R.id.layout_post), this)));
		screenControllerPairs.add(new Pair<Screen, ScreenController>(Screen.ME,
				new MeScreenController(activity.findViewById(R.id.layout_me),
						this)));

		screenToController = new HashMap<Screen, ScreenController>();

		for (Pair<Screen, ScreenController> pair : screenControllerPairs) {
			screenToController.put(pair.first, pair.second);
		}

		for (ScreenController screen : screenToController.values()) {
			screen.onCreate();
		}

		client.login(new Callback<Void, Void, Void>() {
			@Override
			public void onFinish(Void param) {
			}
		});

		setCurrentScreen(Screen.FEED);
	}

	public void switchToScreen(Screen screenToShow) {
		// Hide all screens except 'screen'
		for (Screen screen : screenToController.keySet()) {
			if (screen == screenToShow) {
				screenToController.get(screen).showScreen();
			} else {
				screenToController.get(screen).hideScreen();
			}
		}
	}

	public void voteFor(final ChoosiePostData post, int whichPhoto) {
		Log.i(Constants.LOG_TAG, "Issuing vote for: " + post.getPostKey());
		this.client.sendVoteToServer(post, whichPhoto,
				new Callback<Void, Void, Boolean>() {

					@Override
					public void onFinish(Boolean param) {
						if (param) {
							refreshPost(post.getPostKey());
						}
					}

				});
	}

	private void refreshPost(String postKey) {
		this.caches.getPostsCache().invalidateKey(postKey);
		this.caches.getPostsCache().getValue(postKey,
				new Callback<Void, Object, ChoosiePostData>() {
					@Override
					public void onFinish(ChoosiePostData param) {
						if (param == null) {
							// TODO: Handle error
							Toast.makeText(getActivity(),
									"Failed to update post.",
									Toast.LENGTH_SHORT).show();
							return;
						}
						((FeedScreenController) screenToController
								.get(Screen.FEED)).refreshPost(param);
					}
				});

	}

	public void CommentFor(final String post_key, String text) {
		Log.i(Constants.LOG_TAG, "commenting vote for: " + post_key);
		this.client.sendCommentToServer(post_key, text,
				new Callback<Void, Void, Boolean>() {

					@Override
					public void onFinish(Boolean param) {
						if (param) {
							refreshPost(post_key);
						}
					}
				});
	}

	public ClientBase getClient() {
		return client;
	}

	public Caches getCaches() {
		return caches;
	}

	public void switchToCommentScreen(ChoosiePostData choosiePost) {
		Intent intent = new Intent(screenToController.get(Screen.FEED)
				.getActivity().getApplicationContext(), CommentScreen.class);

		intent.putExtra("post_key", choosiePost.getPostKey());
		intent.putExtra("question", choosiePost.getQuestion());

		String photo1Path = Utils.getFileNameForURL(choosiePost.getPhoto1URL());
		String photo2Path = Utils.getFileNameForURL(choosiePost.getPhoto2URL());
		String userPhotoPath = Utils.getFileNameForURL(choosiePost.getAuthor()
				.getPhotoURL());

		intent.putExtra(Constants.IntentsCodes.photo1Path, photo1Path);
		intent.putExtra(Constants.IntentsCodes.photo2Path, photo2Path);
		intent.putExtra(Constants.IntentsCodes.userPhotoPath, userPhotoPath);
		intent.putExtra(Constants.IntentsCodes.userName, choosiePost
				.getAuthor().getUserName());

		// create the comments list
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> commentList = new ArrayList<String>();
		ArrayList<String> commentierPhotoUrlList = new ArrayList<String>();
		ArrayList<CharSequence> createdAtList = new ArrayList<CharSequence>();

		for (Comment comment : choosiePost.getComments()) {
			nameList.add(comment.getUser().getUserName());
			commentList.add(comment.getText());
			commentierPhotoUrlList.add(Utils.getFileNameForURL(comment
					.getUser().getPhotoURL()));
			createdAtList.add(Utils.getTimeDifferenceTextFromNow(comment
					.getCreatedAt()));
		}

		intent.putStringArrayListExtra(Constants.IntentsCodes.nameList,
				nameList);
		intent.putStringArrayListExtra(Constants.IntentsCodes.commentList,
				commentList);
		intent.putStringArrayListExtra(
				Constants.IntentsCodes.commentierPhotoUrlList,
				commentierPhotoUrlList);
		intent.putCharSequenceArrayListExtra(
				Constants.IntentsCodes.createdAtList, createdAtList);

		screenToController.get(Screen.FEED).getActivity()
				.startActivityForResult(intent, Constants.RequestCodes.COMMENT);
	}

	public void switchToVotesScreen(ChoosiePostData choosiePost) {
		Intent intent = new Intent(screenToController.get(Screen.FEED)
				.getActivity().getApplicationContext(),
				VotesScreenActivity.class);

		// intent.putExtra("post_key", choosiePost.getPostKey());
		intent.putExtra(Constants.IntentsCodes.question,
				choosiePost.getQuestion());

		String photo1Path = Utils.getFileNameForURL(choosiePost.getPhoto1URL());
		String photo2Path = Utils.getFileNameForURL(choosiePost.getPhoto2URL());
		String userPhotoPath = Utils.getFileNameForURL(choosiePost.getAuthor()
				.getPhotoURL());

		intent.putExtra(Constants.IntentsCodes.photo1Path, photo1Path);
		intent.putExtra(Constants.IntentsCodes.photo2Path, photo2Path);
		intent.putExtra(Constants.IntentsCodes.userPhotoPath, userPhotoPath);

		// create the votes list
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> votersPhotoUrlList = new ArrayList<String>();
		ArrayList<Integer> voteForList = new ArrayList<Integer>();

		for (Vote vote : choosiePost.getVotes()) {
			nameList.add(vote.getUsers().getUserName());
			votersPhotoUrlList.add(Utils.getFileNameForURL(vote.getUsers()
					.getPhotoURL()));
			voteForList.add(vote.getVote_for());
		}

		intent.putStringArrayListExtra(Constants.IntentsCodes.nameList,
				nameList);
		intent.putStringArrayListExtra(
				Constants.IntentsCodes.votersPhotoUrlList, votersPhotoUrlList);
		intent.putIntegerArrayListExtra(Constants.IntentsCodes.voteForList,
				voteForList);

		screenToController.get(Screen.FEED).getActivity().startActivity(intent);// Constants.RequestCodes.VOTES);
	}

	public void onActivityResult(int resultCode, Intent data) {
		if (resultCode == ChoosieActivity.RESULT_OK) {
			String text = data.getStringExtra(Constants.IntentsCodes.text);
			String post_key = data
					.getStringExtra(Constants.IntentsCodes.post_key);
			CommentFor(post_key, text);
		}
	}

	public ScreenController getControllerForScreen(Screen screen) {
		return screenToController.get(screen);
	}

	public Activity getActivity() {
		return this.activity;
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}

	public void switchToEnlargeImage(View v, ChoosiePostData choosiePost) {
		Intent intent = new Intent(screenToController.get(Screen.FEED)
				.getActivity().getApplicationContext(),
				EnlargePhotoActivity.class);

		int startingImage = 2;
		switch (v.getId()) {
		case (R.id.feedimage1):
			startingImage = 0;
			break;
		case (R.id.feedimage2):
			startingImage = 1;
			break;
		}

		int votes1 = choosiePost.getVotes1();
		int votes2 = choosiePost.getVotes2();
		String photo1Path = Utils.getFileNameForURL(choosiePost.getPhoto1URL());
		String photo2Path = Utils.getFileNameForURL(choosiePost.getPhoto2URL());
		String userPhotoPath = Utils.getFileNameForURL(choosiePost.getAuthor()
				.getPhotoURL());
		String userName = choosiePost.getAuthor().getUserName();
		String question = choosiePost.getQuestion();
		boolean isVotedAlready = choosiePost.isVotedAlready();

		IntentData intentData = new IntentData(startingImage, votes1, votes2,
				photo1Path, photo2Path, userPhotoPath, userName, question,
				isVotedAlready);
		
		intent.putExtra(Constants.IntentsCodes.intentData, intentData);

		getActivity().startActivityForResult(intent,
				Constants.RequestCodes.EnalargeImage);
	}
}
