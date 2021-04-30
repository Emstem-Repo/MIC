package com.kp.cms.to.exam;

import java.io.Serializable;

import com.kp.cms.bo.exam.ExamInternalRetestApplicationBO;

@SuppressWarnings("serial")
public class ExamInternalRetestApplicationSubjectsTO implements Serializable,Comparable<ExamInternalRetestApplicationSubjectsTO>{
	
	private String subjectId;
	private String subjectCode;
	private String subjectName;
	private String fees;
	private String theoryAppeared;
	private String practicalAppeared;
	private boolean isCheckedDummy;
	private boolean isCheckedDummyPractical;
	private boolean isApplied;
	private int id;	
	private int examInternalRetestApplicationId;
	private String className;
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}
	public String getTheoryAppeared() {
		return theoryAppeared;
	}
	public void setTheoryAppeared(String theoryAppeared) {
		this.theoryAppeared = theoryAppeared;
	}
	public String getPracticalAppeared() {
		return practicalAppeared;
	}
	public void setPracticalAppeared(String practicalAppeared) {
		this.practicalAppeared = practicalAppeared;
	}
	public void setIsCheckedDummy(boolean b) {
		this.isCheckedDummy = b;
	}
	public boolean getIsCheckedDummy() {
		return isCheckedDummy;
	}
	public void setIsCheckedDummyPractical(boolean isCheckedDummyPractical) {
		this.isCheckedDummyPractical = isCheckedDummyPractical;
	}
	public boolean getIsCheckedDummyPractical() {
		return isCheckedDummyPractical;
	}
	@Override
	public int compareTo(ExamInternalRetestApplicationSubjectsTO arg0) {
		if(arg0!=null && this!=null && arg0.getSubjectName()!=null 
				 && this.getSubjectName()!=null){
			return this.getSubjectName().compareTo(arg0.getSubjectName());
		}else
			return 0;
	}
	public boolean getIsApplied() {
		return isApplied;
	}
	public void setIsApplied(boolean isApplied) {
		this.isApplied = isApplied;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExamInternalRetestApplicationId() {
		return examInternalRetestApplicationId;
	}
	public void setExamInternalRetestApplicationId(
			int examInternalRetestApplicationId) {
		this.examInternalRetestApplicationId = examInternalRetestApplicationId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
