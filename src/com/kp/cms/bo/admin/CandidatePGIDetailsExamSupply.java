package com.kp.cms.bo.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kp.cms.bo.exam.ExamDefinitionBO;

public class CandidatePGIDetailsExamSupply implements Serializable{


	private int id;
	private String candidateRefNo;
	private String candidateName;
	private String txnStatus;
	private BigDecimal txnAmount;
	private Date txnDate;
	private String txnRefNo;
	private String bankId;
	private String bankRefNo;
	private String errorStatus;
	private String errorDesc;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private String mobileNo1;
	private String mobileNo2;
	private String email;
	
	private String mihpayId;
	private String pgType;
	private String unmappedStatus;
	private String mode;
	private BigDecimal additionalCharges;
	
	//new for Admission
	private String paymentEmail;
	private ExamDefinitionBO exam;
	private Student student;
	private Classes classes;

	



	public CandidatePGIDetailsExamSupply() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	
	public CandidatePGIDetailsExamSupply(int id, String candidateRefNo,
			String candidateName, String txnStatus, BigDecimal txnAmount,
			Date txnDate, String txnRefNo, String bankId, String bankRefNo,
			String errorStatus, String errorDesc, String createdBy,
			String modifiedBy, Date createdDate, Date lastModifiedDate,
			String mobileNo1, String mobileNo2, String email, String mihpayId,
			String pgType, String unmappedStatus, String mode,
			BigDecimal additionalCharges, String paymentEmail,
			ExamDefinitionBO exam, Student student,Classes classes) {
		super();
		this.id = id;
		this.candidateRefNo = candidateRefNo;
		this.candidateName = candidateName;
		this.txnStatus = txnStatus;
		this.txnAmount = txnAmount;
		this.txnDate = txnDate;
		this.txnRefNo = txnRefNo;
		this.bankId = bankId;
		this.bankRefNo = bankRefNo;
		this.errorStatus = errorStatus;
		this.errorDesc = errorDesc;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.mobileNo1 = mobileNo1;
		this.mobileNo2 = mobileNo2;
		this.email = email;
		this.mihpayId = mihpayId;
		this.pgType = pgType;
		this.unmappedStatus = unmappedStatus;
		this.mode = mode;
		this.additionalCharges = additionalCharges;
		this.paymentEmail = paymentEmail;
		this.exam = exam;
		this.student = student;
		this.classes=classes;
	}





	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCandidateRefNo() {
		return candidateRefNo;
	}
	public void setCandidateRefNo(String candidateRefNo) {
		this.candidateRefNo = candidateRefNo;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public BigDecimal getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnRefNo() {
		return txnRefNo;
	}
	public void setTxnRefNo(String txnRefNo) {
		this.txnRefNo = txnRefNo;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankRefNo() {
		return bankRefNo;
	}
	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}
	public String getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
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
	
	public String getMobileNo1() {
		return mobileNo1;
	}
	public void setMobileNo1(String mobileNo1) {
		this.mobileNo1 = mobileNo1;
	}
	public String getMobileNo2() {
		return mobileNo2;
	}
	public void setMobileNo2(String mobileNo2) {
		this.mobileNo2 = mobileNo2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMihpayId() {
		return mihpayId;
	}
	public void setMihpayId(String mihpayId) {
		this.mihpayId = mihpayId;
	}
	public String getPgType() {
		return pgType;
	}
	public void setPgType(String pgType) {
		this.pgType = pgType;
	}
	public String getUnmappedStatus() {
		return unmappedStatus;
	}
	public void setUnmappedStatus(String unmappedStatus) {
		this.unmappedStatus = unmappedStatus;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public BigDecimal getAdditionalCharges() {
		return additionalCharges;
	}
	public void setAdditionalCharges(BigDecimal additionalCharges) {
		this.additionalCharges = additionalCharges;
	}
	
	public String getPaymentEmail() {
		return paymentEmail;
	}
	public void setPaymentEmail(String paymentEmail) {
		this.paymentEmail = paymentEmail;
	}
	
	public ExamDefinitionBO getExam() {
		return exam;
	}
	public void setExam(ExamDefinitionBO exam) {
		this.exam = exam;
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
	

}
