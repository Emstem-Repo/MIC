package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;

/**
 * ClassStudent generated by hbm2java
 */
public class ClassStudent implements java.io.Serializable {

	private int id;
	private Classes classes;
	private String createdBy;;
	private String modifiedBy;
	private SubjectGroup subjectGroup;
	private Integer subClassId;
	private String registerNumber;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isExamEligible;

	public ClassStudent() {
	}

	public ClassStudent(int id) {
		this.id = id;
	}

	public ClassStudent(int id, Classes classes, String createdBy,
			String modifiedBy, SubjectGroup subjectGroup,
			Integer subClassId, String registerNumber, Date createdDate,
			Date lastModifiedDate, Boolean isExamEligible) {
		this.id = id;
		this.classes = classes;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.subjectGroup = subjectGroup;
		this.subClassId = subClassId;
		this.registerNumber = registerNumber;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.isExamEligible = isExamEligible;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Classes getClasses() {
		return this.classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)  {
		this.createdBy = createdBy;
	}

	public String getModifiedBy()  {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public SubjectGroup getSubjectGroup() {
		return this.subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public Integer getSubClassId() {
		return this.subClassId;
	}

	public void setSubClassId(Integer subClassId) {
		this.subClassId = subClassId;
	}

	public String getRegisterNumber() {
		return this.registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsExamEligible() {
		return this.isExamEligible;
	}

	public void setIsExamEligible(Boolean isExamEligible) {
		this.isExamEligible = isExamEligible;
	}

}
