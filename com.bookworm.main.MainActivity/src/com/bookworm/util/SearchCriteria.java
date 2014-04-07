package com.bookworm.util;

import java.util.List;

public class SearchCriteria {
	private String bookId;
	private String bookLikerId;
	private Long userId;
	private String userName;
	private List<Long> bookIdList;
	private List<Long> userIdList;
	private int pageSize;
	private int pageNumber;
	private String orderByCrit;
	private String orderByDrc;
	private Long followerId;
	private Long followingId;
	private Long adderId;
	
	
	
	public List<Long> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}
	public Long getAdderId() {
		return adderId;
	}
	public void setAdderId(Long adderId) {
		this.adderId = adderId;
	}
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
	public Long getFollowerId() {
		return followerId;
	}
	public void setFollowerId(Long followerId) {
		this.followerId = followerId;
	}
	public Long getFollowingId() {
		return followingId;
	}
	public void setFollowingId(Long followingId) {
		this.followingId = followingId;
	}
	
	
	
	
	
}
