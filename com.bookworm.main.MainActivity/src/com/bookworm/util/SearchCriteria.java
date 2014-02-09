package com.bookworm.util;

import java.util.List;

public class SearchCriteria {
	private String bookId;
	private String bookLikerId;
	private List<Long> bookIdList;
	
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getBookLikerId() {
		return bookLikerId;
	}
	public void setBookLikerId(String bookLikerId) {
		this.bookLikerId = bookLikerId;
	}
	public List<Long> getBookIdList() {
		return bookIdList;
	}
	public void setBookIdList(List<Long> bookIdList) {
		this.bookIdList = bookIdList;
	}
	
	
	
	
	
}
