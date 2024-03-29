package com.choosie.app.Models;

public class CommentData {

	private final String name;
	private final String comment;
	private final String commentierPhotoPath;
	private final CharSequence createdAt;
	private final boolean isDummyComment;

	public CommentData(String name, String comment, String commentierPhotoUrlList, CharSequence createdAt) {
		this.name = name;
		this.comment = comment;
		this.commentierPhotoPath = commentierPhotoUrlList;
		this.createdAt = createdAt;
		this.isDummyComment = false;
	}
	
	public CommentData(boolean isDummyComment){
		this.name = null;
		this.comment = null;
		this.commentierPhotoPath = null;
		this.createdAt = null;
		this.isDummyComment = true;
	}

	public String getName() {
		return this.name;
	}

	public String getComment() {
		return this.comment;
	}
	
	public String getcommentierPhotoPath() {
		return this.commentierPhotoPath;
	}
	
	public CharSequence getCreatedAt() {
		return this.createdAt;
	}
	
	public boolean checkIfDummyComment(){
		return isDummyComment;
	}
}
