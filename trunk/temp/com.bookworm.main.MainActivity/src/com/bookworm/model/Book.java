package com.bookworm.model;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


	  public class Book implements KvmSerializable{
	  	
	  	private Long id;
	  	private String name;
	  	private String writer;
	  	private String type;
	  	private String description;
	  	private String publishingDate;
	  	private String adderId;
	  	private String addingDate;
	  	private String publisher;
	    private String image;

	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getWriter() {
			return writer;
		}

		public void setWriter(String writer) {
			this.writer = writer;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPublishingDate() {
			return publishingDate;
		}

		public void setPublishingDate(String publishingDate) {
			this.publishingDate = publishingDate;
		}

		public String getAdderId() {
			return adderId;
		}

		public void setAdderId(String adderId) {
			this.adderId = adderId;
		}

		public String getAddingDate() {
			return addingDate;
		}

		public void setAddingDate(String addingDate) {
			this.addingDate = addingDate;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public Object getProperty(int paramInt) {
			return null;
		}

		public int getPropertyCount() {
			return 0;
		}

		public void setProperty(int paramInt, Object paramObject) {
			
		}

		public void getPropertyInfo(int paramInt, Hashtable paramHashtable,
				PropertyInfo paramPropertyInfo) {
			
		}		
	  	
	  	
	
}
