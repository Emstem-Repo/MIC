package com.kp.cms.bo.admin;

// Generated Dec 18, 2009 3:48:30 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * HlGroupStudent generated by hbm2java
 */
public class HlGroupStudent implements java.io.Serializable {

	private int id;
	private HlApplicationForm hlApplicationForm;
	private HlGroup hlGroup;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Set<HlAttendance> hlAttendances = new HashSet<HlAttendance>(0);

	public HlGroupStudent() {
	}

	public HlGroupStudent(int id) {
		this.id = id;
	}

	public HlGroupStudent(int id, HlApplicationForm hlApplicationForm,
			HlGroup hlGroup, String createdBy, Date createdDate,
			String modifiedBy, Date lastModifiedDate, Boolean isActive,
			Set<HlAttendance> hlAttendances) {
		this.id = id;
		this.hlApplicationForm = hlApplicationForm;
		this.hlGroup = hlGroup;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.hlAttendances = hlAttendances;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HlApplicationForm getHlApplicationForm() {
		return this.hlApplicationForm;
	}

	public void setHlApplicationForm(HlApplicationForm hlApplicationForm) {
		this.hlApplicationForm = hlApplicationForm;
	}

	public HlGroup getHlGroup() {
		return this.hlGroup;
	}

	public void setHlGroup(HlGroup hlGroup) {
		this.hlGroup = hlGroup;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<HlAttendance> getHlAttendances() {
		return this.hlAttendances;
	}

	public void setHlAttendances(Set<HlAttendance> hlAttendances) {
		this.hlAttendances = hlAttendances;
	}

}
