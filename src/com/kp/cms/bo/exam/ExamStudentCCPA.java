package com.kp.cms.bo.exam;

import java.util.Date;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;

public class ExamStudentCCPA implements java.io.Serializable {
	private Integer id;
	private Student student;
	private Course course;
	private Double ccpa;
	private String credit;
	private String creditGradePoint;
	private String result;
	private String grade;
	private int appliedYear;
	private String createdBy;;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private String passOutMonth;
	private int passOutYear;
	private double totalMarksAwarded;
	private double totalMaxMarks;
	private String subjetcTypeBasedCgpa;
	private String subjetcTypeBasedCredit;
	private String subjetcTypeBasedCreditGradePoint;
	private String subjetcTypeBasedGrade;
	private String subjectTypeBasedObtainedMarks;
	private String subjectTypeBasedAwardedMarks;
	private String creditsForDisplay;
	
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

	public Double getCcpa() {
		return ccpa;
	}

	public void setCcpa(Double ccpa) {
		this.ccpa = ccpa;
	}
	
	public int getAppliedYear() {
		return appliedYear;
	}

	public void setAppliedYear(int appliedYear) {
		this.appliedYear = appliedYear;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public ExamStudentCCPA(){
		
	}
	
	public ExamStudentCCPA(Integer id, Student student, Integer schemeNo, 
			Classes classes, Double sgpa, int credit, Double creditGradePoint) {
		super();
		this.id = id;
	
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
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

	
	public int getPassOutYear() {
		return passOutYear;
	}

	public void setPassOutYear(int passOutYear) {
		this.passOutYear = passOutYear;
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

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCreditGradePoint() {
		return creditGradePoint;
	}

	public void setCreditGradePoint(String creditGradePoint) {
		this.creditGradePoint = creditGradePoint;
	}

	public String getPassOutMonth() {
		return passOutMonth;
	}

	public void setPassOutMonth(String passOutMonth) {
		this.passOutMonth = passOutMonth;
	}

	/**
	 * @return the subjetcTypeBasedCgpa
	 */
	public String getSubjetcTypeBasedCgpa() {
		return subjetcTypeBasedCgpa;
	}

	/**
	 * @param subjetcTypeBasedCgpa the subjetcTypeBasedCgpa to set
	 */
	public void setSubjetcTypeBasedCgpa(String subjetcTypeBasedCgpa) {
		this.subjetcTypeBasedCgpa = subjetcTypeBasedCgpa;
	}

	/**
	 * @return the subjetcTypeBasedCredit
	 */
	public String getSubjetcTypeBasedCredit() {
		return subjetcTypeBasedCredit;
	}

	/**
	 * @param subjetcTypeBasedCredit the subjetcTypeBasedCredit to set
	 */
	public void setSubjetcTypeBasedCredit(String subjetcTypeBasedCredit) {
		this.subjetcTypeBasedCredit = subjetcTypeBasedCredit;
	}

	/**
	 * @return the subjetcTypeBasedCreditGradePoint
	 */
	public String getSubjetcTypeBasedCreditGradePoint() {
		return subjetcTypeBasedCreditGradePoint;
	}

	/**
	 * @param subjetcTypeBasedCreditGradePoint the subjetcTypeBasedCreditGradePoint to set
	 */
	public void setSubjetcTypeBasedCreditGradePoint(
			String subjetcTypeBasedCreditGradePoint) {
		this.subjetcTypeBasedCreditGradePoint = subjetcTypeBasedCreditGradePoint;
	}

	/**
	 * @return the subjetcTypeBasedGrade
	 */
	public String getSubjetcTypeBasedGrade() {
		return subjetcTypeBasedGrade;
	}

	/**
	 * @param subjetcTypeBasedGrade the subjetcTypeBasedGrade to set
	 */
	public void setSubjetcTypeBasedGrade(String subjetcTypeBasedGrade) {
		this.subjetcTypeBasedGrade = subjetcTypeBasedGrade;
	}

	public String getSubjectTypeBasedObtainedMarks() {
		return subjectTypeBasedObtainedMarks;
	}

	public void setSubjectTypeBasedObtainedMarks(
			String subjectTypeBasedObtainedMarks) {
		this.subjectTypeBasedObtainedMarks = subjectTypeBasedObtainedMarks;
	}

	public String getSubjectTypeBasedAwardedMarks() {
		return subjectTypeBasedAwardedMarks;
	}

	public void setSubjectTypeBasedAwardedMarks(String subjectTypeBasedAwardedMarks) {
		this.subjectTypeBasedAwardedMarks = subjectTypeBasedAwardedMarks;
	}

	public String getCreditsForDisplay() {
		return creditsForDisplay;
	}

	public void setCreditsForDisplay(String creditsForDisplay) {
		this.creditsForDisplay = creditsForDisplay;
	}

	
}
