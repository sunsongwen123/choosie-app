package com.choosie.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.choosie.app.Models.ChoosiePostData;
import com.choosie.app.Models.Vote;
import com.choosie.app.Models.VoteData;
import com.choosie.app.controllers.SuperController;

/*
 * class VotePopupWindowUtils: basic utils for poping a window for votes
 * for activate - create new instance and activate popUpVotesWindow
 */
public class VotePopupWindowUtils {

	Activity activity;

	public VotePopupWindowUtils(Activity activity) {
		this.activity = activity;
	}

	public void popUpVotesWindow(ChoosiePostData choosiePost) {
		PopupWindow pw;

		// We need to get the instance of the LayoutInflater, use the
		// context of this activity
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.popup_layout,
				(ViewGroup) activity.findViewById(R.id.popup_element));

		// set the votes numbers
		final TextView textViewVotes1 = (TextView) layout
				.findViewById(R.id.votesPopupWindow_votes1);
		final TextView textViewVotes2 = (TextView) layout
				.findViewById(R.id.votesPopupWindow_votes2);

		textViewVotes1.setText(choosiePost.getVotes1() + " votes");
		textViewVotes2.setText(choosiePost.getVotes2() + " votes");

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

		// create the adapter
		ArrayAdapter<VoteData> voteScreenAdapter = makeVotesScreenAdapter(
				nameList, votersPhotoUrlList, voteForList);

		// attache the listView, where the votes will be displayed
		ListView listView = (ListView) layout
				.findViewById(R.id.votesPopupWindow_listView);
		listView.setAdapter(voteScreenAdapter);

		// set view and size
		pw = new PopupWindow(layout, Utils.getScreenWidth() - 30,
				Utils.getScreenHeight() / 2, true);
		pw.setAnimationStyle(R.style.PopupWindowAnimation);

		// for closing when touching outside - set the background not null
		pw.setBackgroundDrawable(new BitmapDrawable());

		// show it!
		pw.showAtLocation(layout, Gravity.BOTTOM, 0, 10);
	}

	private ArrayAdapter<VoteData> makeVotesScreenAdapter(
			ArrayList<String> nameList, ArrayList<String> votersPhotoUrlList,
			ArrayList<Integer> voteForList) {

		ArrayAdapter<VoteData> adi = new ArrayAdapter<VoteData>(activity,
				R.layout.view_votes) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				VoteData item = getItem(position);

				return createViewVotes(item, convertView, parent); 
			}
		};

		ArrayList<MiniVoteData> votersToPhoto1 = new ArrayList<MiniVoteData>();
		ArrayList<MiniVoteData> votersToPhoto2 = new ArrayList<MiniVoteData>();
		devideVoters(votersToPhoto1, votersToPhoto2, nameList,
				votersPhotoUrlList, voteForList);

		for (int i = 0; i < Math.max(votersToPhoto1.size(),
				votersToPhoto2.size()); i++) {

			String voterName1 = null;
			String photoUrl1 = null;
			String voterName2 = null;
			String photoUrl2 = null;

			if (i < votersToPhoto1.size()) {
				voterName1 = votersToPhoto1.get(i).getName();
				photoUrl1 = votersToPhoto1.get(i).getphotoUrl();
			}
			if (i < votersToPhoto2.size()) {
				voterName2 = votersToPhoto2.get(i).getName();
				photoUrl2 = votersToPhoto2.get(i).getphotoUrl();
			}
			VoteData newVoteData = new VoteData(voterName1, photoUrl1,
					voterName2, photoUrl2);
			adi.add(newVoteData);
		}
		return adi;
	}

	private class MiniVoteData {
		private final String name;
		private final String photoUrl;

		public MiniVoteData(String name, String photoUrl) {
			this.name = name;
			this.photoUrl = photoUrl;
		}

		public String getName() {
			return this.name;
		}

		public String getphotoUrl() {
			return this.photoUrl;
		}
	}

	private void devideVoters(ArrayList<MiniVoteData> votersToPhoto1,
			ArrayList<MiniVoteData> votersToPhoto2, ArrayList<String> nameList,
			ArrayList<String> votersPhotoUrlList, ArrayList<Integer> voteForList) {

		for (int i = 0; i < nameList.size(); i++) {
			if (voteForList.get(i) == 1) {
				votersToPhoto1.add(new MiniVoteData(nameList.get(i),
						votersPhotoUrlList.get(i)));
			} else {
				votersToPhoto2.add(new MiniVoteData(nameList.get(i),
						votersPhotoUrlList.get(i)));
			}
		}
	}

	private View createViewVotes(VoteData item, View convertView,
			View parentView) {

		LinearLayout itemView = null;
		VoteViewHolder voteViewHolder = null;

		if (convertView == null) {
			// we have to inflate new view, and create new holder

			itemView = new LinearLayout(parentView.getContext());

			LayoutInflater inflater = (LayoutInflater) parentView.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.view_votes, itemView);

			voteViewHolder = new VoteViewHolder();
			voteViewHolder.tv1 = (TextView) itemView
					.findViewById(R.id.voteView_name1);
			voteViewHolder.tv2 = (TextView) itemView
					.findViewById(R.id.voteView_name2);
			voteViewHolder.voterPhotoImageView1 = (ImageView) itemView
					.findViewById(R.id.voteView_voterPhoto1);
			voteViewHolder.voterPhotoImageView2 = (ImageView) itemView
					.findViewById(R.id.voteView_voterPhoto2);
			itemView.setTag(voteViewHolder);
		} else {
			// we can reuse the convertView and get the holder
			itemView = (LinearLayout) convertView;
			voteViewHolder = (VoteViewHolder) itemView.getTag();
		}

		// first, clean the image and text in any case
		voteViewHolder.tv1.setText("");
		voteViewHolder.tv2.setText("");
		voteViewHolder.voterPhotoImageView1.setImageBitmap(null);
		voteViewHolder.voterPhotoImageView2.setImageBitmap(null);

		if (item.getName1() != null) {
			// set the voter1 names
			setTextOntv(item.getName1(), voteViewHolder.tv1);

			// set the voter1 photo
			voteViewHolder.voterPhotoImageView1.setImageBitmap(BitmapFactory
					.decodeFile(item.getVoterPhotoPath1()));
		}

		if (item.getName2() != null) {
			// set the voter2 names
			setTextOntv(item.getName2(), voteViewHolder.tv2);

			// set the voter2 photo
			voteViewHolder.voterPhotoImageView2.setImageBitmap(BitmapFactory
					.decodeFile(item.getVoterPhotoPath2()));
		}

		return itemView;
	}

	private class VoteViewHolder {
		TextView tv1;
		ImageView voterPhotoImageView1;
		TextView tv2;
		ImageView voterPhotoImageView2;
	}

	private void setTextOntv(String name, TextView tv) {
		final SpannableStringBuilder sb = new SpannableStringBuilder(name);

		// Same as the User textColor in the XML.
		// TODO: Make it a resource that both use
		final ForegroundColorSpan blueLinkColor = new ForegroundColorSpan(
				Color.rgb(42, 30, 176));

		// Span to make text bold
		int charsToBoldify = name.length();
		sb.setSpan(blueLinkColor, 0, charsToBoldify,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		// Span to set text color to some RGB value
		final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

		// Set the text color for first charsToBoldify characters
		sb.setSpan(bss, 0, charsToBoldify, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		tv.setText(sb);
	}

}