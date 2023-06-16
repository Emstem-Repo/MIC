package com.kp.cms.to.exam;

import java.math.BigDecimal;
import java.util.Date;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamDefinition;

public class ExamMarkEvaluationPrintTo {
	
	private int id;
	private int firstEvaluator;
	private String firstEvaluation;
	private int secondEvaluator;
	private String secondEvaluation;
	private int thirdEvaluator;
	private String thirdEvaluation;
	private String falseNo;
	private int finalEvaluator;
	private String finalEvaluation;
	private String examName;
	private String programmeName;
	private String courseName;
	private String cousrseCode;
	private String mark;
	private String markInWords;
	private String boxNo;
	private String empName;
	private String dept;
	private String profession;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstEvaluation() {
		return firstEvaluation;
	}
	public void setFirstEvaluation(String firstEvaluation) {
		this.firstEvaluation = firstEvaluation;
	}
	
	public String getSecondEvaluation() {
		return secondEvaluation;
	}
	public void setSecondEvaluation(String secondEvaluation) {
		this.secondEvaluation = secondEvaluation;
	}
	
	public String getThirdEvaluation() {
		return thirdEvaluation;
	}
	public void setThirdEvaluation(String thirdEvaluation) {
		this.thirdEvaluation = thirdEvaluation;
	}
	public String getFalseNo() {
		return falseNo;
	}
	public void setFalseNo(String falseNo) {
		this.falseNo = falseNo;
	}

	public String getFinalEvaluation() {
		return finalEvaluation;
	}
	public void setFinalEvaluation(String finalEvaluation) {
		this.finalEvaluation = finalEvaluation;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getProgrammeName() {
		return programmeName;
	}
	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCousrseCode() {
		return cousrseCode;
	}
	public void setCousrseCode(String cousrseCode) {
		this.cousrseCode = cousrseCode;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getMarkInWords() {
		return markInWords;
	}
	public void setMarkInWords(String markInWords) {
		this.markInWords = markInWords;
	}
	public int getFirstEvaluator() {
		return firstEvaluator;
	}
	public void setFirstEvaluator(int firstEvaluator) {
		this.firstEvaluator = firstEvaluator;
	}
	public int getSecondEvaluator() {
		return secondEvaluator;
	}
	public void setSecondEvaluator(int secondEvaluator) {
		this.secondEvaluator = secondEvaluator;
	}
	public int getThirdEvaluator() {
		return thirdEvaluator;
	}
	public void setThirdEvaluator(int thirdEvaluator) {
		this.thirdEvaluator = thirdEvaluator;
	}
	public int getFinalEvaluator() {
		return finalEvaluator;
	}
	public void setFinalEvaluator(int finalEvaluator) {
		this.finalEvaluator = finalEvaluator;
	}
	public String getBoxNo() {
		return boxNo;
	}
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	
}
