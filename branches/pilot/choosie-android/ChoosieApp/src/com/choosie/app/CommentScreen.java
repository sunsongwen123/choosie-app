package com.choosie.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.choosie.app.R;
import com.choosie.app.Models.Comment;
import com.choosie.app.Models.CommentData;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class CommentScreen extends Activity {
	Intent intent;
	// key listener - for: when the user pressing the enter key - sends the
	// comment
	OnKeyListener sendKeyListener = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					activateSendButton(intent);
					return true;
				default:
					break;
				}
			}
			return false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(Constants.LOG_TAG, "in comment screen");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_screen);

		// setting onclikelistener
		EditText questionEditText = (EditText) findViewById(R.id.editText_comment);
		questionEditText.setOnKeyListener(sendKeyListener);

		intent = getIntent();
		fillCommentView(intent);

		ArrayAdapter<CommentData> commentScreenAdapter = makeCommentScreenAdapter(intent);

		ListView listView = (ListView) findViewById(R.id.commentsListView);
		listView.setAdapter(commentScreenAdapter);

		Button buttonSendComment = (Button) findViewById(R.id.button_send_comment);
		buttonSendComment.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				activateSendButton(intent);
			}
		});

	}

	private ArrayAdapter<CommentData> makeCommentScreenAdapter(
			final Intent intent) {
		ArrayAdapter<CommentData> adi = new ArrayAdapter<CommentData>(this,
				R.layout.view_comment) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				CommentData item = getItem(position);

				return createViewComment(item, intent, position);
			}
		};

		// fill the adapter:

		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> commentList = new ArrayList<String>();
		ArrayList<String> commentierPhotoUrlList = new ArrayList<String>();
		ArrayList<CharSequence> createdAtList = new ArrayList<CharSequence>();

		nameList = intent
				.getStringArrayListExtra(Constants.IntentsCodes.nameList);
		commentList = intent
				.getStringArrayListExtra(Constants.IntentsCodes.commentList);
		commentierPhotoUrlList = intent
				.getStringArrayListExtra(Constants.IntentsCodes.commentierPhotoUrlList);
		createdAtList = intent
				.getCharSequenceArrayListExtra(Constants.IntentsCodes.createdAtList);

		// if there are no comments - put a dummy item for showing the pictures
		if (nameList.size() == 0) {
			adi.add(new CommentData(true));
		} else {
			for (int i = 0; i < nameList.size(); i++) {
				CommentData newCommentData = new CommentData(nameList.get(i),
						commentList.get(i), commentierPhotoUrlList.get(i),
						createdAtList.get(i));
				adi.add(newCommentData);
			}
		}

		return adi;
	}

	private void fillCommentView(Intent intent) {
		final ImageView imageViewUserPhoto = (ImageView) findViewById(R.id.userPhoto_commetns);

		String userPhotoPath = intent
				.getStringExtra(Constants.IntentsCodes.userPhotoPath);

		setImageFromPath(userPhotoPath, imageViewUserPhoto);
		
		//set the user name
		TextView userNameTextView = (TextView) findViewById(R.id.comment_activity_user_name);
		userNameTextView.setText(intent.getStringExtra(Constants.IntentsCodes.userName));

		// set the question
		((TextView) findViewById(R.id.textImage_comment_question))
				.setText(intent.getStringExtra("question"));
	}

	private View createViewComment(CommentData item, Intent intent, int position) {
		LinearLayout itemView = new LinearLayout(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_comment, itemView);

		if ((position == 0) || (item.checkIfDummyComment() == true)) {
			final ImageView imageViewPhoto1 = (ImageView) itemView
					.findViewById(R.id.photo1_comment_screen);
			final ImageView imageViewPhoto2 = (ImageView) itemView
					.findViewById(R.id.photo2_comment_screen);

			String photo1Path = intent
					.getStringExtra(Constants.IntentsCodes.photo1Path);
			String photo2Path = intent
					.getStringExtra(Constants.IntentsCodes.photo2Path);

			setImageFromPath(photo1Path, imageViewPhoto1);
			setImageFromPath(photo2Path, imageViewPhoto2);
		} else {
			LinearLayout imagesLayout = (LinearLayout) itemView
					.findViewById(R.id.layout_images_comment);
			imagesLayout.setVisibility(View.GONE);
		}

		// comtinue only if it is a real comment
		if (item.checkIfDummyComment() == false) {

			// set the comment text
			TextView tv = (TextView) itemView
					.findViewById(R.id.view_comment_comment);
			setTextOntv(item, tv);

			// set the commentier photo
			ImageView commentierPhotoImageView = (ImageView) itemView
					.findViewById(R.id.commentScreen_commentierPhoto);
			commentierPhotoImageView.setImageBitmap(BitmapFactory
					.decodeFile(item.getcommentierPhotoPath()));

			// set the comment time
			TextView commentTime = (TextView) itemView
					.findViewById(R.id.commentScreen_commentTime);
			commentTime.setText(item.getCreatedAt() + " ago");
		} else {
			((ImageView) itemView.findViewById(R.id.view_comment_clockImage))
					.setVisibility(View.GONE);
		}
		return itemView;
	}

	private void activateSendButton(final Intent intent) {
		String text = ((EditText) findViewById(R.id.editText_comment))
				.getText().toString();

		// get the intent and add the comment
		intent.putExtra(Constants.IntentsCodes.text, text);

		// activate the 'onActivityResult'
		setResult(RESULT_OK, intent);
		// resetCommetnScreen();
		finish();
	}

	private void setTextOntv(CommentData item, TextView tv) {
		final SpannableStringBuilder sb = new SpannableStringBuilder(
				item.getName() + " " + item.getComment());

		// Same as the User textColor in the XML.
		// TODO: Make it a resource that both use
		final ForegroundColorSpan blueLinkColor = new ForegroundColorSpan(
				Color.rgb(42, 30, 176));

		// Span to make text bold
		int charsToBoldify = item.getName().length();
		sb.setSpan(blueLinkColor, 0, charsToBoldify,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		// Span to set text color to some RGB value
		final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

		// Set the text color for first charsToBoldify characters
		sb.setSpan(bss, 0, charsToBoldify, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		tv.setText(sb);
	}

	private void setImageFromPath(String photoPath, ImageView imageViewPhoto) {
		if (photoPath != null) {
			imageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath));
		}
	}

	// TODO: check if can delete this function
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED, null);
		finish();
		return;
	}

	// setting the backKey to cancel the commentizatzia
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(RESULT_CANCELED, null);
			resetCommentScreen();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void resetCommentScreen() {
		final ImageView imageViewUserPhoto = (ImageView) findViewById(R.id.userPhoto_commetns);
		Bitmap userPhotoBitmap = ((BitmapDrawable) imageViewUserPhoto
				.getDrawable()).getBitmap();
		if (userPhotoBitmap != null) {
			userPhotoBitmap.recycle();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_comment_screen, menu);
		return true;
	}
}
