package com.kp.cms.forms.admission;

import com.kp.cms.forms.BaseActionForm;

public class StudentSemesterFeeCorrectionForm extends BaseActionForm {
	private String candidateRefNo;
	private String txnDate;
	private String txnRefNo;
	private String txnAmt;
	private String correctionFor;
	public String getCandidateRefNo() {
		return candidateRefNo;
	}
	public void setCandidateRefNo(String candidateRefNo) {
		this.candidateRefNo = candidateRefNo;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnRefNo() {
		return txnRefNo;
	}
	public void setTxnRefNo(String txnRefNo) {
		this.txnRefNo = txnRefNo;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getCorrectionFor() {
		return correctionFor;
	}
	public void setCorrectionFor(String correctionFor) {
		this.correctionFor = correctionFor;
	}
	
	

}
