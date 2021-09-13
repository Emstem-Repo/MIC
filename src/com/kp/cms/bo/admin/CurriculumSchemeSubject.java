package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;

/**
 * CurriculumSchemeSubject generated by hbm2java
 */
public class CurriculumSchemeSubject implements java.io.Serializable {

	private int id;
	private String createdBy;;
	private CurriculumSchemeDuration curriculumSchemeDuration;
	private String modifiedBy;
	private SubjectGroup subjectGroup;
	private Date createdDate;
	private Date lastModifiedDate;

	public CurriculumSchemeSubject() {
	}

	public CurriculumSchemeSubject(int id) {
		this.id = id;
	}

	public CurriculumSchemeSubject(int id, String createdBy,
			CurriculumSchemeDuration curriculumSchemeDuration,
			String modifiedBy, SubjectGroup subjectGroup,
			Date createdDate, Date lastModifiedDate) {
		this.id = id;
		this.createdBy = createdBy;
		this.curriculumSchemeDuration = curriculumSchemeDuration;
		this.modifiedBy = modifiedBy;
		this.subjectGroup = subjectGroup;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)  {
		this.createdBy = createdBy;
	}

	public CurriculumSchemeDuration getCurriculumSchemeDuration() {
		return this.curriculumSchemeDuration;
	}

	public void setCurriculumSchemeDuration(
			CurriculumSchemeDuration curriculumSchemeDuration) {
		this.curriculumSchemeDuration = curriculumSchemeDuration;
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

}
