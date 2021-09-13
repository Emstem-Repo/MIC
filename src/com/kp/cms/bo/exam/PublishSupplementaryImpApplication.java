package com.kp.cms.bo.exam;

import java.util.Date;

import com.kp.cms.bo.admin.Classes;

public class PublishSupplementaryImpApplication implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private ExamDefinition exam;
	private Date startDate;
	private Date endDate;
	private String createdBy;;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Classes classCode;
	private Date extendedDate;
	
	private Date extendedFineDate;
	private Date extendedSuperFineDate;
	private String fineAmount;
	private String superFineAmount;
	private Date extendedFineStartDate;
	private Date extendedSuperFineStartDate;
	
	public PublishSupplementaryImpApplication() {
		super();
	}
	
	public PublishSupplementaryImpApplication(int id, ExamDefinition exam,
			Date startDate, Date endDate, String createdBy, String modifiedBy,
			Date createdDate, Date lastModifiedDate, Boolean isActive,
			Classes classCode, Date extendedDate) {
		super();
		this.id = id;
		this.exam = exam;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.classCode = classCode;
		this.extendedDate= extendedDate;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ExamDefinition getExam() {
		return exam;
	}
	public void setExam(ExamDefinition exam) {
		this.exam = exam;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Classes getClassCode() {
		return classCode;
	}

	public void setClassCode(Classes classCode) {
		this.classCode = classCode;
	}

	public Date getExtendedDate() {
		return extendedDate;
	}

	public void setExtendedDate(Date extendedDate) {
		this.extendedDate = extendedDate;
	}

	/**
	 * @return the extendedFineDate
	 */
	public Date getExtendedFineDate() {
		return extendedFineDate;
	}

	/**
	 * @param extendedFineDate the extendedFineDate to set
	 */
	public void setExtendedFineDate(Date extendedFineDate) {
		this.extendedFineDate = extendedFineDate;
	}

	/**
	 * @return the extendedSuperFineDate
	 */
	public Date getExtendedSuperFineDate() {
		return extendedSuperFineDate;
	}

	/**
	 * @param extendedSuperFineDate the extendedSuperFineDate to set
	 */
	public void setExtendedSuperFineDate(Date extendedSuperFineDate) {
		this.extendedSuperFineDate = extendedSuperFineDate;
	}

	/**
	 * @return the fineAmount
	 */
	public String getFineAmount() {
		return fineAmount;
	}

	/**
	 * @param fineAmount the fineAmount to set
	 */
	public void setFineAmount(String fineAmount) {
		this.fineAmount = fineAmount;
	}

	/**
	 * @return the superFineAmount
	 */
	public String getSuperFineAmount() {
		return superFineAmount;
	}

	/**
	 * @param superFineAmount the superFineAmount to set
	 */
	public void setSuperFineAmount(String superFineAmount) {
		this.superFineAmount = superFineAmount;
	}

	/**
	 * @return the extendedFineStartDate
	 */
	public Date getExtendedFineStartDate() {
		return extendedFineStartDate;
	}

	/**
	 * @param extendedFineStartDate the extendedFineStartDate to set
	 */
	public void setExtendedFineStartDate(Date extendedFineStartDate) {
		this.extendedFineStartDate = extendedFineStartDate;
	}

	/**
	 * @return the extendedSuperFineStartDate
	 */
	public Date getExtendedSuperFineStartDate() {
		return extendedSuperFineStartDate;
	}

	/**
	 * @param extendedSuperFineStartDate the extendedSuperFineStartDate to set
	 */
	public void setExtendedSuperFineStartDate(Date extendedSuperFineStartDate) {
		this.extendedSuperFineStartDate = extendedSuperFineStartDate;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}