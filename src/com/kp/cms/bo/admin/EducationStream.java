package com.kp.cms.bo.admin;

import java.util.Date;

public class EducationStream 
{
	private int id;
	private String name;
	private boolean isActive;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	
	
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIsActive(boolean isActive)
	{
		this.isActive=isActive;
	}
	public boolean getIsActive()
	{
		return isActive;
	}

}
