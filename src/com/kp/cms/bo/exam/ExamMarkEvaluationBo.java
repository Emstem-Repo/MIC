package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.to.admin.ProgramTypeTO;

public class ExamMarkEvaluationBo implements Serializable {
	private int id;
	private Users firstEvaluator;
	private int firstEvaluation;
	private Users secondEvaluator;
	private int secondEvaluation;
	private Users thirdEvaluator;
	private int thirdEvaluation;
	private String falseNo;
	private Users finalEvaluator;
	private int finalEvaluation;
	private String createdBy;;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Users getFirstEvaluator() {
		return firstEvaluator;
	}
	public void setFirstEvaluator(Users firstEvaluator) {
		this.firstEvaluator = firstEvaluator;
	}

	public Users getSecondEvaluator() {
		return secondEvaluator;
	}
	public void setSecondEvaluator(Users secondEvaluator) {
		this.secondEvaluator = secondEvaluator;
	}
	
	public Users getThirdEvaluator() {
		return thirdEvaluator;
	}
	public void setThirdEvaluator(Users thirdEvaluator) {
		this.thirdEvaluator = thirdEvaluator;
	}

	public String getFalseNo() {
		return falseNo;
	}
	public void setFalseNo(String falseNo) {
		this.falseNo = falseNo;
	}
	public Users getFinalEvaluator() {
		return finalEvaluator;
	}
	public void setFinalEvaluator(Users finalEvaluator) {
		this.finalEvaluator = finalEvaluator;
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
	public int getFirstEvaluation() {
		return firstEvaluation;
	}
	public void setFirstEvaluation(int firstEvaluation) {
		this.firstEvaluation = firstEvaluation;
	}
	public int getSecondEvaluation() {
		return secondEvaluation;
	}
	public void setSecondEvaluation(int secondEvaluation) {
		this.secondEvaluation = secondEvaluation;
	}
	public int getThirdEvaluation() {
		return thirdEvaluation;
	}
	public void setThirdEvaluation(int thirdEvaluation) {
		this.thirdEvaluation = thirdEvaluation;
	}
	public int getFinalEvaluation() {
		return finalEvaluation;
	}
	public void setFinalEvaluation(int finalEvaluation) {
		this.finalEvaluation = finalEvaluation;
	}
	
	
	
	
}
