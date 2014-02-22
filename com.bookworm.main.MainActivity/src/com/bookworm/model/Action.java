package com.bookworm.model;

import java.util.Date;

// default package
// Generated Jan 13, 2014 10:33:06 PM by Hibernate Tools 3.4.0.CR1


/**
 * Action generated by hbm2java
 */
public class Action implements java.io.Serializable {

	private ActionType actionType;
	private Long  userId;
	private Long  actionId;
	private Date actionDate;
	private Long actionDetailId;
	public Action() {
	}

	public Action(ActionType actionType, Long  userId,Long actionDetailId) {
		this.actionType = actionType;
		this.userId = userId;
		this.actionDetailId = actionDetailId;
	}


	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public Long  getUserId() {
		return this.userId;
	}

	public void setUserId(Long  userId) {
		this.userId = userId;
	}

	public Long  getActionId() {
		return this.actionId;
	}

	public void setActionId(Long  actionId) {
		this.actionId = actionId;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Long getActionDetailId() {
		return actionDetailId;
	}

	public void setActionDetailId(Long actionDetailId) {
		this.actionDetailId = actionDetailId;
	}

}
