package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Program;

public class SpecialFeesBO implements Serializable{
	private int id;
	private Classes classes;
	private BigDecimal tutionFees;
	private BigDecimal specialFees;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isActive;
	private BigDecimal applicationFees;
	private BigDecimal lateFineFees;
	private String academicYear;
	
	public SpecialFeesBO() {
		super();
	}
	
	public SpecialFeesBO(int id, Program program, BigDecimal tutionFees,Classes classes,
			BigDecimal specialFees,BigDecimal applicationFees,BigDecimal lateFineFees, String createdBy, String modifiedBy,
			Date createdDate, Date lastModifiedDate, Boolean isActive) {
		this.id = id;
		this.classes = classes;
		this.tutionFees = tutionFees;
		this.specialFees = specialFees;
		this.applicationFees = applicationFees;
		this.lateFineFees = lateFineFees;
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

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public BigDecimal getTutionFees() {
		return tutionFees;
	}

	public void setTutionFees(BigDecimal tutionFees) {
		this.tutionFees = tutionFees;
	}

	public BigDecimal getSpecialFees() {
		return specialFees;
	}

	public void setSpecialFees(BigDecimal specialFees) {
		this.specialFees = specialFees;
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

	public BigDecimal getApplicationFees() {
		return applicationFees;
	}

	public void setApplicationFees(BigDecimal applicationFees) {
		this.applicationFees = applicationFees;
	}

	public BigDecimal getLateFineFees() {
		return lateFineFees;
	}

	public void setLateFineFees(BigDecimal lateFineFees) {
		this.lateFineFees = lateFineFees;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	
	
}
