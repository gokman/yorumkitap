package com.bookworm.model;
// default package
// Generated Jan 13, 2014 10:33:06 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * FollowShipId generated by hbm2java
 */
public class FollowShipId implements java.io.Serializable {

	private int followerUserId;
	private Integer followedUserId;
	private Date creationDate;

	public FollowShipId() {
	}

	public FollowShipId(int followerUserId) {
		this.followerUserId = followerUserId;
	}

	public FollowShipId(int followerUserId, Integer followedUserId,
			Date creationDate) {
		this.followerUserId = followerUserId;
		this.followedUserId = followedUserId;
		this.creationDate = creationDate;
	}

	public int getFollowerUserId() {
		return this.followerUserId;
	}

	public void setFollowerUserId(int followerUserId) {
		this.followerUserId = followerUserId;
	}

	public Integer getFollowedUserId() {
		return this.followedUserId;
	}

	public void setFollowedUserId(Integer followedUserId) {
		this.followedUserId = followedUserId;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FollowShipId))
			return false;
		FollowShipId castOther = (FollowShipId) other;

		return (this.getFollowerUserId() == castOther.getFollowerUserId())
				&& ((this.getFollowedUserId() == castOther.getFollowedUserId()) || (this
						.getFollowedUserId() != null
						&& castOther.getFollowedUserId() != null && this
						.getFollowedUserId().equals(
								castOther.getFollowedUserId())))
				&& ((this.getCreationDate() == castOther.getCreationDate()) || (this
						.getCreationDate() != null
						&& castOther.getCreationDate() != null && this
						.getCreationDate().equals(castOther.getCreationDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getFollowerUserId();
		result = 37
				* result
				+ (getFollowedUserId() == null ? 0 : this.getFollowedUserId()
						.hashCode());
		result = 37
				* result
				+ (getCreationDate() == null ? 0 : this.getCreationDate()
						.hashCode());
		return result;
	}

}
