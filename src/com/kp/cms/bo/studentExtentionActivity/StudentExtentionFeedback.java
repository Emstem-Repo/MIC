package com.kp.cms.bo.studentExtentionActivity;

import java.util.Date;

import com.kp.cms.bo.admin.Classes;

public class StudentExtentionFeedback implements java.io.Serializable {

	private int id;
	private Classes classId;
	private StudentGroup groupId;
	private Date startDate;
	private Date endDate;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private boolean isActive;
	
	public StudentExtentionFeedback(){
		super();
	}
	public StudentExtentionFeedback(int id,
			                      Classes classId,
			                      StudentGroup groupId,
			                      Date startDate,
			                      Date 	endDate,
			                      String createdBy,
			                      Date createdDate,
			                      String modifiedBy,
			                      Date lastModifiedDate,
			                      boolean isActive
	                                ){
	this.id = id;
	this.classId = classId;
	this.groupId = groupId;
	this.startDate = startDate;
	this.endDate = endDate;
	this.createdBy = createdBy;
	this.createdDate = createdDate;
	this.modifiedBy = modifiedBy;
	this.lastModifiedDate = lastModifiedDate;
	this.isActive = isActive;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Classes getClassId() {
		return classId;
	}
	public void setClassId(Classes classId) {
		this.classId = classId;
	}
	public StudentGroup getGroupId() {
		return groupId;
	}
	public void setGroupId(StudentGroup groupId) {
		this.groupId = groupId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
