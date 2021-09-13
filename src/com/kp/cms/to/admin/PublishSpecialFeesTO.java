package com.kp.cms.to.admin;

import java.io.Serializable;

public class PublishSpecialFeesTO implements Serializable {
	private int id;
	private String toDate;
	private String fromDate;
	private String className;
	private String attendanceId;
	private String classID;
	private String acadamicYear;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getAttendanceId() {
		return attendanceId;
	}
	public void setAttendanceId(String attendanceId) {
		this.attendanceId = attendanceId;
	}
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getAcadamicYear() {
		return acadamicYear;
	}
	public void setAcadamicYear(String acadamicYear) {
		this.acadamicYear = acadamicYear;
	}
	
	
}
