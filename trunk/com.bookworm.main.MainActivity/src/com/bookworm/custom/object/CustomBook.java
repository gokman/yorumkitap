package com.bookworm.custom.object;

public class CustomBook {
	
	private Long bookId;
	private String name;
	private String description;
	private Long adderId;
	private String writer;
	private String coverPhoto;
	private String adderName;
	
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getAdderId() {
		return adderId;
	}
	public void setAdderId(Long adderId) {
		this.adderId = adderId;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getCoverPhoto() {
		return coverPhoto;
	}
	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}
	public String getAdderName() {
		return adderName;
	}
	public void setAdderName(String adderName) {
		this.adderName = adderName;
	}
	
}