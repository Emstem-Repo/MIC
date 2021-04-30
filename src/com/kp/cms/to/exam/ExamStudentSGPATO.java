package com.kp.cms.to.exam;

public class ExamStudentSGPATO implements Comparable<ExamStudentSGPATO>{

	private Integer id;
	private int studentId;
	private int courseId;
	private Integer schemeNo;
	private int classId;
	private Double sgpa;
	private String credit;
	private Double creditGradePoint;
	private String result;
	private String grade;
	private int appliedYear;
	private int year;
	private String month;
	private double totalMarksAwarded;
	private double totalMaxMarks;
	private String subjetcTypeBasedSgpa;
	private String subjetcTypeBasedCredit;
	private String subjetcTypeBasedCreditGradePoint;
	private String subjetcTypeBasedGrade;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public Integer getSchemeNo() {
		return schemeNo;
	}
	public void setSchemeNo(Integer schemeNo) {
		this.schemeNo = schemeNo;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public Double getSgpa() {
		return sgpa;
	}
	public void setSgpa(Double sgpa) {
		this.sgpa = sgpa;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public Double getCreditGradePoint() {
		return creditGradePoint;
	}
	public void setCreditGradePoint(Double creditGradePoint) {
		this.creditGradePoint = creditGradePoint;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getTotalMarksAwarded() {
		return totalMarksAwarded;
	}
	public void setTotalMarksAwarded(double totalMarksAwarded) {
		this.totalMarksAwarded = totalMarksAwarded;
	}
	public double getTotalMaxMarks() {
		return totalMaxMarks;
	}
	public void setTotalMaxMarks(double totalMaxMarks) {
		this.totalMaxMarks = totalMaxMarks;
	}
	public String getSubjetcTypeBasedSgpa() {
		return subjetcTypeBasedSgpa;
	}
	public void setSubjetcTypeBasedSgpa(String subjetcTypeBasedSgpa) {
		this.subjetcTypeBasedSgpa = subjetcTypeBasedSgpa;
	}
	public String getSubjetcTypeBasedCredit() {
		return subjetcTypeBasedCredit;
	}
	public void setSubjetcTypeBasedCredit(String subjetcTypeBasedCredit) {
		this.subjetcTypeBasedCredit = subjetcTypeBasedCredit;
	}
	public String getSubjetcTypeBasedCreditGradePoint() {
		return subjetcTypeBasedCreditGradePoint;
	}
	public void setSubjetcTypeBasedCreditGradePoint(
			String subjetcTypeBasedCreditGradePoint) {
		this.subjetcTypeBasedCreditGradePoint = subjetcTypeBasedCreditGradePoint;
	}
	public String getSubjetcTypeBasedGrade() {
		return subjetcTypeBasedGrade;
	}
	public void setSubjetcTypeBasedGrade(String subjetcTypeBasedGrade) {
		this.subjetcTypeBasedGrade = subjetcTypeBasedGrade;
	}
	public int compareTo(ExamStudentSGPATO other) {
		return this.schemeNo - other.schemeNo;
	}
}
