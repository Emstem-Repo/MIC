package com.kp.cms.bo.admin;

import java.util.Date;

public class ExtraCoCurricularLeavePublishBO implements java.io.Serializable {
	private int id;
	private Classes classes;
	private String publishFor;
	private Date publishStartDate;
	private Date publishEndDate;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifyDate;
	private boolean isActive;
	
	
	
	public ExtraCoCurricularLeavePublishBO(int id, Classes classes,
			String publishFor, Date publishStartDate, Date publishEndDate,
			String createdBy, Date createdDate, String modifiedBy,
			Date modifyDate) {
		super();
		this.id = id;
		this.classes = classes;
		this.publishFor = publishFor;
		this.publishStartDate = publishStartDate;
		this.publishEndDate = publishEndDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifyDate = modifyDate;
		this.isActive = isActive;
	}
	public ExtraCoCurricularLeavePublishBO() {
		//super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	public String getPublishFor() {
		return publishFor;
	}
	public void setPublishFor(String publishFor) {
		this.publishFor = publishFor;
	}
	public Date getPublishStartDate() {
		return publishStartDate;
	}
	public void setPublishStartDate(Date publishStartDate) {
		this.publishStartDate = publishStartDate;
	}
	public Date getPublishEndDate() {
		return publishEndDate;
	}
	public void setPublishEndDate(Date publishEndDate) {
		this.publishEndDate = publishEndDate;
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
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
