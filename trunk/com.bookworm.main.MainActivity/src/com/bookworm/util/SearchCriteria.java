package com.bookworm.util;

import java.util.List;

public class SearchCriteria {
	private String bookId;
	private String bookLikerId;
	private Long userId;
	private String userName;
	private List<Long> bookIdList;
	private int pageSize;
	private int pageNumber;
	private String orderByCrit;
	private String orderByDrc;
	
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
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getOrderByCrit() {
		return orderByCrit;
	}
	public void setOrderByCrit(String orderByCrit) {
		this.orderByCrit = orderByCrit;
	}
	public String getOrderByDrc() {
		return orderByDrc;
	}
	public void setOrderByDrc(String orderByDrc) {
		this.orderByDrc = orderByDrc;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
	
}
