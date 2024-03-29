package com.choosie.app;

import android.graphics.Bitmap;

public class NewChoosiePostData {

	private Bitmap image1;
	private Bitmap image2;
	private String question;
	private boolean shareOnFacebook;

	public NewChoosiePostData(Bitmap image1, Bitmap image2, String question) {
		this(image1, image2, question, false);
	}
	
	public NewChoosiePostData(Bitmap image1, Bitmap image2, String question, boolean shareOnFacebook) {
		this.setImage1(image1);
		this.setImage2(image2);
		this.setQuestion(question);
		this.setShareOnFacebook(shareOnFacebook);
	}


	public Bitmap getImage1() {
		return image1;
	}

	public void setImage1(Bitmap image1) {
		this.image1 = image1;
	}

	public Bitmap getImage2() {
		return image2;
	}

	public void setImage2(Bitmap image2) {
		this.image2 = image2;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isShareOnFacebook() {
		return shareOnFacebook;
	}

	public void setShareOnFacebook(boolean shareOnFacebook) {
		this.shareOnFacebook = shareOnFacebook;
	}
}
