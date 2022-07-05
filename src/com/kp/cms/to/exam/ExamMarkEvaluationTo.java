package com.kp.cms.to.exam;

import java.math.BigDecimal;
import java.util.Date;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamDefinition;

public class ExamMarkEvaluationTo {
	
	private int id;
	private String firstEvaluator;
	private String firstEvaluation;
	private String secondEvaluator;
	private String secondEvaluation;
	private String thirdEvaluator;
	private String thirdEvaluation;
	private String falseNo;
	private String finalEvaluator;
	private String finalEvaluation;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstEvaluator() {
		return firstEvaluator;
	}
	public void setFirstEvaluator(String firstEvaluator) {
		this.firstEvaluator = firstEvaluator;
	}
	public String getFirstEvaluation() {
		return firstEvaluation;
	}
	public void setFirstEvaluation(String firstEvaluation) {
		this.firstEvaluation = firstEvaluation;
	}
	public String getSecondEvaluator() {
		return secondEvaluator;
	}
	public void setSecondEvaluator(String secondEvaluator) {
		this.secondEvaluator = secondEvaluator;
	}
	public String getSecondEvaluation() {
		return secondEvaluation;
	}
	public void setSecondEvaluation(String secondEvaluation) {
		this.secondEvaluation = secondEvaluation;
	}
	public String getThirdEvaluator() {
		return thirdEvaluator;
	}
	public void setThirdEvaluator(String thirdEvaluator) {
		this.thirdEvaluator = thirdEvaluator;
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
	public String getFinalEvaluator() {
		return finalEvaluator;
	}
	public void setFinalEvaluator(String finalEvaluator) {
		this.finalEvaluator = finalEvaluator;
	}
	public String getFinalEvaluation() {
		return finalEvaluation;
	}
	public void setFinalEvaluation(String finalEvaluation) {
		this.finalEvaluation = finalEvaluation;
	}
	
}
