package com.bookworm.so;

import com.bookworm.model.Comment;

public class CommentSCR {
	
	public CommentSCR(Comment comm){
		this.comment = comm;
	}
	private Comment comment;
	private String genericProperty = "profilePhoto";
	
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public String getGenericProperty() {
		return genericProperty;
	}
	
}
