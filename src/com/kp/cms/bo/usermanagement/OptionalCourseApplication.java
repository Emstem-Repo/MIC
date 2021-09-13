package com.kp.cms.bo.usermanagement;

import java.io.Serializable;
import java.util.Date;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;

public class OptionalCourseApplication implements Serializable{
	int id;
	private Department department;
	private Subject subject;
	private Integer courseOption;
	private String createdBy;;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private Student student;
	private Classes classes;
	
	
	public OptionalCourseApplication() {
		super();
		// TODO Auto-generated constructor stub
	}


	public OptionalCourseApplication(int id, Department department,
			Subject subject, Integer courseOption, String createdBy,
			String modifiedBy, Date createdDate, Date lastModifiedDate,
			Student student, Classes classes) {
		super();
		this.id = id;
		this.department = department;
		this.subject = subject;
		this.courseOption = courseOption;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.student = student;
		this.classes = classes;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
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
	public Integer getCourseOption() {
		return courseOption;
	}
	public void setCourseOption(Integer courseOption) {
		this.courseOption = courseOption;
	}


	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	
	
	
	
	

}
