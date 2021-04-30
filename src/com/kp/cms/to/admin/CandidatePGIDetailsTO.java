package com.kp.cms.to.admin;

public class CandidatePGIDetailsTO {
	private int id;
	private String refundGenerated;
	private String courseName;
	private String admApplnId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRefundGenerated() {
		return refundGenerated;
	}
	public void setRefundGenerated(String refundGenerated) {
		this.refundGenerated = refundGenerated;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getAdmApplnId() {
		return admApplnId;
	}
	public void setAdmApplnId(String admApplnId) {
		this.admApplnId = admApplnId;
	}
}
