package com.kp.cms.to.exam;

public class ConsolidateMarksCardProgrammePartTO{

	private int id;
	private int studentId;
	private int courseId;
	private int consolidatedSubjectSectionId;
	private String consolidatedSubjectSectionName;
	private int consolidatedSubjectStreamId;
	private String consolidatedSubjectStreamName;
	private double obtainedstudentMark;
	private double maxMarkAwarded;
	private double creditPoint;
	private int credit;
	private double ccpa;
	private String grade;
	private int appliedYear;
	private int consolidatedSubjectSectionOrder;
	private boolean showStreamForSection;
	private int creditsForDisplay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getConsolidatedSubjectSectionId() {
		return consolidatedSubjectSectionId;
	}
	public void setConsolidatedSubjectSectionId(int consolidatedSubjectSectionId) {
		this.consolidatedSubjectSectionId = consolidatedSubjectSectionId;
	}
	public String getConsolidatedSubjectSectionName() {
		return consolidatedSubjectSectionName;
	}
	public void setConsolidatedSubjectSectionName(
			String consolidatedSubjectSectionName) {
		this.consolidatedSubjectSectionName = consolidatedSubjectSectionName;
	}
	public int getConsolidatedSubjectStreamId() {
		return consolidatedSubjectStreamId;
	}
	public void setConsolidatedSubjectStreamId(int consolidatedSubjectStreamId) {
		this.consolidatedSubjectStreamId = consolidatedSubjectStreamId;
	}
	public String getConsolidatedSubjectStreamName() {
		return consolidatedSubjectStreamName;
	}
	public void setConsolidatedSubjectStreamName(
			String consolidatedSubjectStreamName) {
		this.consolidatedSubjectStreamName = consolidatedSubjectStreamName;
	}
	public double getObtainedstudentMark() {
		return obtainedstudentMark;
	}
	public void setObtainedstudentMark(double obtainedstudentMark) {
		this.obtainedstudentMark = obtainedstudentMark;
	}
	public double getMaxMarkAwarded() {
		return maxMarkAwarded;
	}
	public void setMaxMarkAwarded(double maxMarkAwarded) {
		this.maxMarkAwarded = maxMarkAwarded;
	}
	public double getCreditPoint() {
		return creditPoint;
	}
	public void setCreditPoint(double creditPoint) {
		this.creditPoint = creditPoint;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public double getCcpa() {
		return ccpa;
	}
	public void setCcpa(double ccpa) {
		this.ccpa = ccpa;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getAppliedYear() {
		return appliedYear;
	}
	public void setAppliedYear(int appliedYear) {
		this.appliedYear = appliedYear;
	}
	public int getConsolidatedSubjectSectionOrder() {
		return consolidatedSubjectSectionOrder;
	}
	public void setConsolidatedSubjectSectionOrder(
			int consolidatedSubjectSectionOrder) {
		this.consolidatedSubjectSectionOrder = consolidatedSubjectSectionOrder;
	}
	public boolean isShowStreamForSection() {
		return showStreamForSection;
	}
	public void setShowStreamForSection(boolean showStreamForSection) {
		this.showStreamForSection = showStreamForSection;
	}
	public int getCreditsForDisplay() {
		return creditsForDisplay;
	}
	public void setCreditsForDisplay(int creditsForDisplay) {
		this.creditsForDisplay = creditsForDisplay;
	}
}
