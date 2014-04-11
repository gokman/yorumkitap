package com.bookworm.model;
// default package
// Generated Jan 13, 2014 10:33:06 PM by Hibernate Tools 3.4.0.CR1


/**
 * ActionTypes generated by hbm2java
 */
public enum ActionType{
	ADD_BOOK(0),ADD_COMMENT(1),FOLLOW(3),UNFOLLOW(4);
	
	private long actionTypeCode;
	 
	private ActionType(int actionTypeCode) {
		this.actionTypeCode = actionTypeCode;
	}
 
	public long asCode() {
		return this.actionTypeCode;
	}
	public static ActionType getAction(int actionTypeCode){
		ActionType type = null;
		for(ActionType actionType : ActionType.values()){
			if(actionType.asCode()== actionTypeCode){
				type=actionType;
				break;
			}
		}
		return type;
	}
	public static ActionType getActionFromString(long actionTypeCode){
		ActionType type = null;
		for(ActionType actionType : ActionType.values()){
			if(actionType.asCode() ==  actionTypeCode){
				type=actionType;
				break;
			}
		}
		return type;
	}	
}