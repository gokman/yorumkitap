package com.bookworm.custom.object;
// default package
// Generated Jan 13, 2014 10:33:06 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.bookworm.model.Comment;

public class CustomComment {

	private Long commentId;
	private Long commenterId;
	private String commentText;
	private Long commentedBookId;
	private Long commentedBookAdderId;
	private String creationDate;
	private String commenterName;

	public CustomComment() {
	}

	public CustomComment(Long commenterId, String commentText,
			Long commentedBookId, Long commentedBookAdderId,
			String creationDate) {
		this.commenterId = commenterId;
		this.commentText = commentText;
		this.commentedBookId = commentedBookId;
		this.commentedBookAdderId = commentedBookAdderId;
		this.creationDate = creationDate;
	}
	
	public CustomComment(Comment comment,String commenterName){
		this.commentedBookAdderId=comment.getCommentedBookAdderId();
		this.commentedBookId=comment.getCommentedBookId();
		this.commenterId=comment.getCommenterId();
		this.commentId=comment.getCommentId();
		this.commentText=comment.getCommentText();
		this.creationDate=comment.getCreationDate();
		this.commenterName=commenterName;
	}

	public Long getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	
	public Long getCommenterId() {
		return this.commenterId;
	}

	public void setCommenterId(Long commenterId) {
		this.commenterId = commenterId;
	}

	public String getCommentText() {
		return this.commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Long getCommentedBookId() {
		return this.commentedBookId;
	}

	public String getCommenterName() {
		return commenterName;
	}

	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}

	public void setCommentedBookId(Long commentedBookId) {
		this.commentedBookId = commentedBookId;
	}

	public Long getCommentedBookAdderId() {
		return this.commentedBookAdderId;
	}

	public void setCommentedBookAdderId(Long commentedBookAdderId) {
		this.commentedBookAdderId = commentedBookAdderId;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

}
