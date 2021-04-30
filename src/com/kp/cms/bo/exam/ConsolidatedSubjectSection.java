package com.kp.cms.bo.exam;

import java.util.Date;

public class ConsolidatedSubjectSection 
{
	private int id;
	private String sectionName;
	private int sectionOrder;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String modifiedBy;
	private boolean isActive;
	private Boolean showRespectiveStreams;
	public ConsolidatedSubjectSection() {
		super();
	}
	public ConsolidatedSubjectSection(int id, 
									  String sectionName,
									  Date createdDate, 
									  String createdBy, 
									  Date lastModifiedDate,
									  String modifiedBy, 
									  boolean isActive,
									  int sectionOrder,
									  boolean showRespectiveStreams) {
		super();
		this.id = id;
		this.sectionName = sectionName;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastModifiedDate = lastModifiedDate;
		this.modifiedBy = modifiedBy;
		this.isActive = isActive;
		this.sectionOrder = sectionOrder;
		this.showRespectiveStreams = showRespectiveStreams;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getSectionOrder() {
		return sectionOrder;
	}
	public void setSectionOrder(int sectionOrder) {
		this.sectionOrder = sectionOrder;
	}
	public boolean getShowRespectiveStreams() {
		return showRespectiveStreams;
	}
	public void setShowRespectiveStreams(boolean showRespectiveStreams) {
		this.showRespectiveStreams = showRespectiveStreams;
	}
}
