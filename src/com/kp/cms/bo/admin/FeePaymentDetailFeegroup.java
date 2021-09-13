package com.kp.cms.bo.admin;

// Generated Nov 25, 2009 5:46:43 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

/**
 * FeePaymentDetailFeegroup generated by hbm2java
 */
public class FeePaymentDetailFeegroup implements java.io.Serializable {

	private int id;
	private FeeGroup feeGroup;
	private FeePaymentDetail feePaymentDetail;
	private BigDecimal amount;
	private Boolean isActive;
	private Boolean isOptional;

	public FeePaymentDetailFeegroup() {
	}

	public FeePaymentDetailFeegroup(int id) {
		this.id = id;
	}

	public FeePaymentDetailFeegroup(int id, FeeGroup feeGroup,
			FeePaymentDetail feePaymentDetail, BigDecimal amount,
			Boolean isActive, Boolean isOptional) {
		this.id = id;
		this.feeGroup = feeGroup;
		this.feePaymentDetail = feePaymentDetail;
		this.amount = amount;
		this.isActive = isActive;
		this.isOptional = isOptional;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FeeGroup getFeeGroup() {
		return this.feeGroup;
	}

	public void setFeeGroup(FeeGroup feeGroup) {
		this.feeGroup = feeGroup;
	}

	public FeePaymentDetail getFeePaymentDetail() {
		return this.feePaymentDetail;
	}

	public void setFeePaymentDetail(FeePaymentDetail feePaymentDetail) {
		this.feePaymentDetail = feePaymentDetail;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsOptional() {
		return this.isOptional;
	}

	public void setIsOptional(Boolean isOptional) {
		this.isOptional = isOptional;
	}

}
