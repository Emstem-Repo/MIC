package com.kp.cms.bo.exam;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;

public class ExamStudentSgpa implements java.io.Serializable {
	private Integer id;
	private Student student;
	private Course course;
	private Integer schemeNo;
	private Classes classes;
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
	private String subjectTypeBasedObtainedMarks;
	private String subjectTypeBasedAwardedMarks;
	private String creditsForDisplay;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	private int getAppliedYear() {
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

	public ExamStudentSgpa(){
		
	}
	
	public ExamStudentSgpa(Integer id, Student student, Integer schemeNo, 
			Classes classes, Double sgpa, int credit, Double creditGradePoint) {
		super();
		this.id = id;
		this.student = student;
		this.schemeNo = schemeNo;
		this.classes = classes;
		this.sgpa = sgpa;
		this.creditGradePoint = creditGradePoint;
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
	public Integer getSchemeNo() {
		return schemeNo;
	}
	public void setSchemeNo(Integer schemeNo) {
		this.schemeNo = schemeNo;
	}


	public Classes getClasses() {
		return classes;
	}


	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Double getSgpa() {
		return sgpa;
	}

	public void setSgpa(Double sgpa) {
		this.sgpa = sgpa;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the subjetcTypeBasedSgpa
	 */
	public String getSubjetcTypeBasedSgpa() {
		return subjetcTypeBasedSgpa;
	}

	/**
	 * @param subjetcTypeBasedSgpa the subjetcTypeBasedSgpa to set
	 */
	public void setSubjetcTypeBasedSgpa(String subjetcTypeBasedSgpa) {
		this.subjetcTypeBasedSgpa = subjetcTypeBasedSgpa;
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

	
	
	