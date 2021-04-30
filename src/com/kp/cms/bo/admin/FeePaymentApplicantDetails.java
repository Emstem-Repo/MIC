package com.kp.cms.bo.admin;

// Generated Feb 20, 2009 2:31:55 PM by Hibernate Tools 3.2.0.b9

/**
 * FeePaymentApplicantDetails generated by hbm2java
 */
public class FeePaymentApplicantDetails implements java.io.Serializable {

	private int id;
	private FeeGroup feeGroup;
	private FeePayment feePayment;
	private Integer semesterNo;

	public FeePaymentApplicantDetails() {
	}

	public FeePaymentApplicantDetails(int id) {
		this.id = id;
	}
	

	public FeePaymentApplicantDetails(int id, FeeGroup feeGroup,
			FeePayment feePayment, Integer semesterNo) {
		this.id = id;
		this.feeGroup = feeGroup;
		this.feePayment = feePayment;
		this.semesterNo = semesterNo;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the feeGroup
	 */
	public FeeGroup getFeeGroup() {
		return feeGroup;
	}

	/**
	 * @param feeGroup the feeGroup to set
	 */
	public void setFeeGroup(FeeGroup feeGroup) {
		this.feeGroup = feeGroup;
	}	

	public FeePayment getFeePayment() {
		return this.feePayment;
	}

	public void setFeePayment(FeePayment feePayment) {
		this.feePayment = feePayment;
	}

	public Integer getSemesterNo() {
		return this.semesterNo;
	}

	public void setSemesterNo(Integer semesterNo) {
		this.semesterNo = semesterNo;
	}

}
