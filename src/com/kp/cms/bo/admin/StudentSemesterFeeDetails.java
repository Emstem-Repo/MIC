package com.kp.cms.bo.admin;

import java.math.BigDecimal;
import java.util.Date;

public class StudentSemesterFeeDetails implements java.io.Serializable {
	private int id;
	private Student student;
	private Classes classes;
	private String registerNo;
	private BigDecimal universityFee;
	private BigDecimal specialFee;
	private BigDecimal otherFee;
	private BigDecimal CATrainingFee;
	private BigDecimal semesterFee;
	
	private String semister;
	private boolean feeApprove;
	private String remarks;
	private String createdBy;;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private Date date;
	
	
	
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
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	
	public String getSemister() {
		return semister;
	}
	public void setSemister(String semister) {
		this.semister = semister;
	}
	public Boolean getFeeApprove() {
		return feeApprove;
	}
	public void setFeeApprove(Boolean feeApprove) {
		this.feeApprove = feeApprove;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public BigDecimal getUniversityFee() {
		return universityFee;
	}
	public void setUniversityFee(BigDecimal universityFee) {
		this.universityFee = universityFee;
	}
	public BigDecimal getSpecialFee() {
		return specialFee;
	}
	public void setSpecialFee(BigDecimal specialFee) {
		this.specialFee = specialFee;
	}
	public BigDecimal getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}
	public BigDecimal getCATrainingFee() {
		return CATrainingFee;
	}
	public void setCATrainingFee(BigDecimal cATrainingFee) {
		CATrainingFee = cATrainingFee;
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
	
	public void setFeeApprove(boolean feeApprove) {
		this.feeApprove = feeApprove;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public BigDecimal getSemesterFee() {
		return semesterFee;
	}
	public void setSemesterFee(BigDecimal semesterFee) {
		this.semesterFee = semesterFee;
	}

}
