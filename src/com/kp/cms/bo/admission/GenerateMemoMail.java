package com.kp.cms.bo.admission;

public class GenerateMemoMail implements java.io.Serializable {
	private int id;
	private String fromName;
	private String fromAddress;
	private String toAddress;
	private String subject;
	private String message;
	private String attachment;
	private String filePath;
	private Boolean isSureMemo;
	private Integer allotmentNo;
    private Integer appliedYear;
	private Integer courseId;	
	private Integer studentId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Boolean getIsSureMemo() {
		return isSureMemo;
	}
	public void setIsSureMemo(Boolean isSureMemo) {
		this.isSureMemo = isSureMemo;
	}
	public Integer getAllotmentNo() {
		return allotmentNo;
	}
	public void setAllotmentNo(Integer allotmentNo) {
		this.allotmentNo = allotmentNo;
	}
	public Integer getAppliedYear() {
		return appliedYear;
	}
	public void setAppliedYear(Integer appliedYear) {
		this.appliedYear = appliedYear;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

}
