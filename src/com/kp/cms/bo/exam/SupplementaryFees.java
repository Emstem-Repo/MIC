package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Program;

public class SupplementaryFees implements Serializable {
	
	private int id;
	private Course course;
	private BigDecimal theoryFees;
	private BigDecimal practicalFees;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isActive;
	private BigDecimal improvementTheoryFees;
	private BigDecimal improvementPracticalFees;
	private BigDecimal applicationFees;
	private BigDecimal cvCampFees;
	private BigDecimal marksListFees;
	private BigDecimal onlineServiceChargeFees;
	private String academicYear;
	private BigDecimal egrandFees;
	
	public SupplementaryFees() {
		super();
	}
	
	public SupplementaryFees(int id, Program program, BigDecimal theoryFees,
			BigDecimal practicalFees, String createdBy, String modifiedBy,
			Date createdDate, Date lastModifiedDate, Boolean isActive) {
		this.id = id;
		this.course = course;
		this.theoryFees = theoryFees;
		this.practicalFees = practicalFees;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public BigDecimal getTheoryFees() {
		return theoryFees;
	}
	public void setTheoryFees(BigDecimal theoryFees) {
		this.theoryFees = theoryFees;
	}
	public BigDecimal getPracticalFees() {
		return practicalFees;
	}
	public void setPracticalFees(BigDecimal practicalFees) {
		this.practicalFees = practicalFees;
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
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getImprovementTheoryFees() {
		return improvementTheoryFees;
	}

	public void setImprovementTheoryFees(BigDecimal improvementTheoryFees) {
		this.improvementTheoryFees = improvementTheoryFees;
	}

	public BigDecimal getImprovementPracticalFees() {
		return improvementPracticalFees;
	}

	public void setImprovementPracticalFees(BigDecimal improvementPracticalFees) {
		this.improvementPracticalFees = improvementPracticalFees;
	}

	public BigDecimal getApplicationFees() {
		return applicationFees;
	}

	public void setApplicationFees(BigDecimal applicationFees) {
		this.applicationFees = applicationFees;
	}

	public BigDecimal getCvCampFees() {
		return cvCampFees;
	}

	public void setCvCampFees(BigDecimal cvCampFees) {
		this.cvCampFees = cvCampFees;
	}

	public BigDecimal getMarksListFees() {
		return marksListFees;
	}

	public void setMarksListFees(BigDecimal marksListFees) {
		this.marksListFees = marksListFees;
	}

	public BigDecimal getOnlineServiceChargeFees() {
		return onlineServiceChargeFees;
	}

	public void setOnlineServiceChargeFees(BigDecimal onlineServiceChargeFees) {
		this.onlineServiceChargeFees = onlineServiceChargeFees;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public BigDecimal getEgrandFees() {
		return egrandFees;
	}

	public void setEgrandFees(BigDecimal egrandFees) {
		this.egrandFees = egrandFees;
	}

	
}