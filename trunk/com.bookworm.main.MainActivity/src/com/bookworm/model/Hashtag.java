package com.bookworm.model;
// default package
// Generated Jan 13, 2014 10:33:06 PM by Hibernate Tools 3.4.0.CR1

/**
 * Hashtag generated by hbm2java
 */
public class Hashtag implements java.io.Serializable {

	private Long hashTagId;
	private Long bookId;
	private String tag;

	public Hashtag() {
	}

	public Hashtag(Long bookId) {
		this.bookId = bookId;
	}

	public Hashtag(Long bookId, String tag) {
		this.bookId = bookId;
		this.tag = tag;
	}

	public Long getHashTagId() {
		return this.hashTagId;
	}

	public void setHashTagId(Long hashTagId) {
		this.hashTagId = hashTagId;
	}

	public Long  getBookId() {
		return this.bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
