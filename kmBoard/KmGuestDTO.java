package com.kmBoard;

public class KmGuestDTO {
	
	/*
	CREATE TABLE kmGuest 
	(guNum number primary key,
	userId varchar2(20) not null,
	guContent varchar2(2000) not null,
	guCreated date default sysdate,
	gdsNum number not null);
 
	 */
	
	private int guNum;
	private String userId;
	private String guContent;
	private String guCreated;
	private int gdsNum;
	
	
	
	public int getGdsNum() {
		return gdsNum;
	}
	public void setGdsNum(int gdsNum) {
		this.gdsNum = gdsNum;
	}
	public int getGuNum() {
		return guNum;
	}
	public void setGuNum(int guNum) {
		this.guNum = guNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGuContent() {
		return guContent;
	}
	public void setGuContent(String guContent) {
		this.guContent = guContent;
	}
	public String getGuCreated() {
		return guCreated;
	}
	public void setGuCreated(String guCreated) {
		this.guCreated = guCreated;
	}
	
	

}
