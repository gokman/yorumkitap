package com.bookworm.model;



/**
 * Book generated by hbm2java
 */
public class Book implements java.io.Serializable {

	private Long bookId;
	private String name;
	private String description;
	private Long adderId;
	private String writer;
	private String coverPhoto;
	
	public Book() {
	}

	public Book(String name, String description, Long adderId,
			String writer, String coverPhoto) {
		this.name = name;
		this.description = description;
		this.adderId = adderId;
		this.writer = writer;
		this.coverPhoto = coverPhoto;
	}

	public Long getBookId() {
		return this.bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAdderId() {
		return this.adderId;
	}

	public void setAdderId(Long adderId) {
		this.adderId = adderId;
	}

	public String getWriter() {
		return this.writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getCoverPhoto() {
		return this.coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

}
