package com.kp.cms.bo.admin;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class TCNumber implements Serializable {

	private int id;
	private String tcFor;
	private String type;
	private Integer startNo;
	private String prefix;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Integer year;
	private Integer currentNo;
	private Integer slNo;
	private boolean isSelfFinancing;
	public TCNumber() {
		super();
	}
	public TCNumber(int id, String tcFor, String type, Integer startNo,
			String prefix, String createdBy, Date createdDate,
			String modifiedBy, Date lastModifiedDate, Boolean isActive,
			Integer year, Integer currentNo, Integer slNo,
			Boolean isSelfFinancing) {
		super();
		this.id = id;
		this.tcFor = tcFor;
		this.type = type;
		this.startNo = startNo;
		this.prefix = prefix;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.year = year;
		this.currentNo = currentNo;
		this.slNo = slNo;
		this.isSelfFinancing = isSelfFinancing;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTcFor() {
		return tcFor;
	}
	public void setTcFor(String tcFor) {
		this.tcFor = tcFor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStartNo() {
		return startNo;
	}
	public void setStartNo(Integer startNo) {
		this.startNo = startNo;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getCurrentNo() {
		return currentNo;
	}
	public void setCurrentNo(Integer currentNo) {
		this.currentNo = currentNo;
	}
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	public boolean getIsSelfFinancing() {
		return isSelfFinancing;
	}
	public void setIsSelfFinancing(boolean isSelfFinancing) {
		this.isSelfFinancing = isSelfFinancing;
	}
}
