package com.kp.cms.bo.studentExtentionActivity;

import java.util.Date;

import com.kp.cms.bo.admin.Student;

public class StudentExtentionActivityDetails {

	private int id;
	private Student student;
	private StudentExtention studentExtention;
	private int preference;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String modifiedBy;
	public StudentExtentionActivityDetails() {
		super();
	}
	public StudentExtentionActivityDetails(int id, Student student,
			StudentExtention studentExtention, int preference,
			Date createdDate, String createdBy, Date lastModifiedDate,
			String modifiedBy) {
		super();
		this.id = id;
		this.student = student;
		this.studentExtention = studentExtention;
		this.preference = preference;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastModifiedDate = lastModifiedDate;
		this.modifiedBy = modifiedBy;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public StudentExtention getStudentExtention() {
		return studentExtention;
	}
	public void setStudentExtention(StudentExtention studentExtention) {
		this.studentExtention = studentExtention;
	}
	public int getPreference() {
		return preference;
	}
	public void setPreference(int preference) {
		this.preference = preference;
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
}
