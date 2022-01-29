package com.kp.cms.to.exam;

import java.io.Serializable;
import java.util.Date;

import com.kp.cms.bo.admin.Course;

public class FalseNumSiNoTO implements Serializable {
	
	private int id;
	private String startNo;
	private String currentNo;
	private Boolean isActive;
	private Date createdDate;
	private Integer academicYear;
	private String semister;
	private String prefix;
	private String courseId;
	private String courseName;
	private String examName;
	private String examId;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartNo() {
		return startNo;
	}
	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}
	public String getCurrentNo() {
		return currentNo;
	}
	public void setCurrentNo(String currentNo) {
		this.currentNo = currentNo;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}
	
	public String getSemister() {
		return semister;
	}
	public void setSemister(String semister) {
		this.semister = semister;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	
	
	
}
