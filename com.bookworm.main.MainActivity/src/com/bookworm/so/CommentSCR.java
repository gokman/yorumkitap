package com.bookworm.so;

import com.bookworm.custom.object.CustomComment;

public class CommentSCR {
	
	public CommentSCR(CustomComment comm){
		this.comment = comm;
	}
	private CustomComment comment;
	private String genericProperty = "profilePhoto";
	
	public CustomComment getComment() {
		return comment;
	}
	public void setComment(CustomComment comment) {
		this.comment = comment;
	}
	public String getGenericProperty() {
		return genericProperty;
	}
	
}
