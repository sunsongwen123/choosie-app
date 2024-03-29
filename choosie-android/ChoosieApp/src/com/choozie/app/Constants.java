package com.choozie.app;

import java.io.File;
import android.os.Environment;

public class Constants {
	public static final String LOG_TAG = "Choosie";

	public static class URIs {


		public static final String ROOT_URL = "http://choosieapp.appspot.com";
//		 public static final String ROOT_URL =
//		 "http://choosie-dev.appspot.com";

		public static final String REGISTER = ROOT_URL + "/register";
		public static final String CRASH_REPORT = ROOT_URL + "/collectcrash";
		public static final String FEED_URI = ROOT_URL + "/feed";
		public static final String NEW_VOTE_URI = ROOT_URL + "/votes/new";
		public static final String NEW_POSTS_URI = ROOT_URL + "/posts/new";
		public static final String NEW_COMMENT_URI = ROOT_URL + "/comments/new";
		public static final String POSTS_URI = ROOT_URL + "/posts";
		public static final String USER = ROOT_URL + "/user";
		public static final String EDIT_USER_DETAILS_URI = ROOT_URL + "/user/edit";
		public static final String mainDirectoryPath = Environment
				.getExternalStorageDirectory()
				+ File.separator
				+ "choosie"
				+ File.separator;
		// public static final String mainDirectoryPath = (Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)).getAbsolutePath();
		public static final String APPLICATION_NAME = "Choozie";

		

		public static final String FACEBOOK_PROFILE_PIC(String fb_uid) {
			return "http://graph.facebook.com/" + fb_uid + "/picture";
		}
	}

	public static class RequestCodes {
		public static final int TAKE_FIRST_PICTURE_FROM_CAMERA = 1;
		public static final int TAKE_SECOND_PICTURE_FROM_CAMERA = 2;
		public static final int TAKE_FIRST_PICTURE_FROM_GALLERY = 3;
		public static final int TAKE_SECOND_PICTURE_FROM_GALLERY = 4;
		public static final int CROP_FIRST = 5;
		public static final int CROP_SECOND = 6;
		public static final int COMMENT = 7;
		public static final int GALLERY = 8;
		public static final int VOTES = 9;
		public static final int FB_REQUEST_PUBLISH_PERMISSION = 64206;
		public static final int FB_PERMISSIONS_GRANTED = -1;
		public static final int FB_PERMISSIONS_DENIED = 0;
		public static final int EnalargeImage = 10;
		public static final int START_ACTIVITY = 11;
		public static final int CAMERA_PICURE_FIRST = 12;
		public static final int CAMERA_PICURE_SECOND = 13;
		public static final int NEW_POST = 14;
		public static final int CAMERA_PICTURE_CONFIRM = 15;
		public static final int CAMERA_CONFIRM_FIRST = 16;
		public static final int CAMERA_CONFIRM_SECOND = 17;
		public static final int CAMERA_RETAKE_PICTURE = 18;
		public static final int CAMERA_PICURE_GALLERY = 19;
		public static final int CAMERA_GALLERY_CROP = 20;
		public static final int PROFILE_SCREEN = 0;
		public static final int EDIT_PROFILE_SCREEN = 21;
		public static final int PICK_CONTACT = 22;
	}

	public static class IntentsCodes {
		public static final String path = "path";
		public static final String photo2Path = "photo2Path";
		public static final String photo1Path = "photo1Path";
		public static final String photoNumber = "photoNumber";
		public static final String userPhotoPath = "userPhotoPath";
		public static final String nameList = "nameList";
		public static final String commentList = "commentList";
		public static final String commentierPhotoUrlList = "commentierPhotoUrlList";
		public static final String text = "text";
		public static final String post_key = "post_key";
		public static final String createdAtList = "createdAtList";
		public static final String question = "question";
		public static final String votersPhotoUrlList = "votersPhotoUrlList";
		public static final String voteForList = "voteForList";
		public static final String startingImageToEnlarge = "startingImageToEnlarge";
		public static final String votes1 = "votes1";
		public static final String votes2 = "votes2";
		public static final String userName = "userName";
		public static final String isAlreadyVoted = "isAlreadyVoted";
		public static final String intentData = "intentData";
		public static final String channelingJob = "channelingJob";
		public static final String isChannelingCose = "isChannelingCose";
		public static final String isPostByMe = "isPostByMe";
		public static final String openVotesWindow = "openVotesWindow";
		public static final String cameraTopWrapperHeight = "cameraTopWrapperHeight";
		public static final String cameraTopHideHeight = "cameraTopHideHeight";
		public static final String cameraBottomWrapperHeight = "cameraBottomWrapperHeight";
		public static final String cameraBottomHideHeight = "cameraBottomHideHeight";
		public static final String error = "error";
		public static final String stayOnScreen = "stayOnScreen";
		public static final String user = "user";
		public static final String fbUid = "fbUid";
		public static final String userDetails = "userDetails";
		public static final String frameId = "frameId";
		public static final String userList = "userList";
	}

	public static class Notifications {

		public static final String SENDER_ID = "101212394485";
		public static final int NOTIFICATION_ID = 1611;

		public static final String NEW_POST_CONTENT_TITLE = "New Choozie";
		public static final String NEW_COMMENT_CONTENT_TITLE = "New Comment";
		public static final String NEW_VOTE_CONTENT_TITLE = "New Vote";
		public static final String DEFAULT_CONTENT_TITLE = "Choozie";

		public static final int NEW_POST_NOTIFICATION_TYPE = 1;
		public static final int NEW_COMMENT_NOTIFICATION_TYPE = 2;
		public static final int NEW_VOTE_NOTIFICATION_TYPE = 3;
		public static final int REGISTER_NOTIFICATION_TYPE = 4;

	}

	public static class SP {
		// public static final String PUSH_NOTIFICATIONS = "push_notifications";
		// public static final String CAMERA_TYPE = "camera_type";
		public static final String PREF_NOTIFICATION = "pref_notification";
		public static final String PREF_CAMERA = "pref_camera";
	}

	public static class DialogId {

		public static final int EXIT_ALERT_DIALOG = 0;
		public static final int WAIT_LOADING = 1;
		public static final int ERROR = 2;
		public static final int WAIT_SAVING = 3;

	}
	
	public static class Gender {
		public static final String MALE = "Male";
		public static final String FEMALE = "Female";
	}
	
	public static int USER_INFO_MAX_LEN = 80;

}
