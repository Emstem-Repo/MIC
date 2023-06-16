package com.kp.cms.to.exam;

public class CandidatePaymentDetailsTO {
	private int id;
	private String examName;
	private String txnDate;
	private String txnStatus;
	private String amount;
	private String txnrefNo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTxnrefNo() {
		return txnrefNo;
	}
	public void setTxnrefNo(String txnrefNo) {
		this.txnrefNo = txnrefNo;
	}
	
	
}
