package com.kp.cms.bo.admin;

// Generated Nov 10, 2009 11:28:29 AM by Hibernate Tools 3.2.0.b9

import java.util.Date;

/**
 * FeeVoucher generated by hbm2java
 */
public class FeeVoucher implements java.io.Serializable {

	private int id;
	private String type;
	private FeeFinancialYear feeFinancialYear;
	private String startingNumber;
	private String endingNumber;
	private String slipBookNumber;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private String alpha;

	public FeeVoucher() {
	}

	public FeeVoucher(int id) {
		this.id = id;
	}

	public FeeVoucher(int id, String type, FeeFinancialYear feeFinancialYear,
			String startingNumber, String endingNumber, String slipBookNumber,
			String createdBy, Date createdDate, String modifiedBy,
			Date lastModifiedDate, Boolean isActive, String alpha) {
		this.id = id;
		this.type = type;
		this.feeFinancialYear = feeFinancialYear;
		this.startingNumber = startingNumber;
		this.endingNumber = endingNumber;
		this.slipBookNumber = slipBookNumber;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.alpha =alpha;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FeeFinancialYear getFeeFinancialYear() {
		return feeFinancialYear;
	}

	public void setFeeFinancialYear(FeeFinancialYear feeFinancialYear) {
		this.feeFinancialYear = feeFinancialYear;
	}

	public String getStartingNumber() {
		return this.startingNumber;
	}

	public void setStartingNumber(String startingNumber) {
		this.startingNumber = startingNumber;
	}

	public String getEndingNumber() {
		return this.endingNumber;
	}

	public void setEndingNumber(String endingNumber) {
		this.endingNumber = endingNumber;
	}

	public String getSlipBookNumber() {
		return this.slipBookNumber;
	}

	public void setSlipBookNumber(String slipBookNumber) {
		this.slipBookNumber = slipBookNumber;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

}
