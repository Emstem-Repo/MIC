package com.kp.cms.to.exam;

import java.io.Serializable;

public class ExamSubCoursewiseGradeDefnTO implements Serializable,Comparable<ExamSubCoursewiseGradeDefnTO> {
	
	private int id;
	private int subjectId;
	private String startPrcntgGrade;
	private String endPrcntgGrade;
	private String grade;
	private String gradeInterpretation;
	private String resultClass;
	private String gradePoint;
	private String subjectCode;
	private String subjectName;
	private String universitySubjectCode;
	public ExamSubCoursewiseGradeDefnTO() 
	{
		super();
	}
	public ExamSubCoursewiseGradeDefnTO(String startPrcntgGrade,
			String endPrcntgGrade, String grade,
			String gradeInterpretation, String resultClass,
			String gradePoint, int subjectId,String subjectName,int id) {
		super();
		this.startPrcntgGrade = startPrcntgGrade;
		this.endPrcntgGrade = endPrcntgGrade;
		this.grade = grade;
		this.gradeInterpretation = gradeInterpretation;
		this.resultClass = resultClass;
		this.gradePoint = gradePoint;
		this.subjectId = subjectId;
		this.setSubjectName(subjectName);
		this.id=id;
	}
	
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getStartPrcntgGrade() {
		return startPrcntgGrade;
	}
	public void setStartPrcntgGrade(String startPrcntgGrade) {
		this.startPrcntgGrade = startPrcntgGrade;
	}
	public String getEndPrcntgGrade() {
		return endPrcntgGrade;
	}
	public void setEndPrcntgGrade(String endPrcntgGrade) {
		this.endPrcntgGrade = endPrcntgGrade;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGradeInterpretation() {
		return gradeInterpretation;
	}
	public void setGradeInterpretation(String gradeInterpretation) {
		this.gradeInterpretation = gradeInterpretation;
	}
	public String getResultClass() {
		return resultClass;
	}
	public void setResultClass(String resultClass) {
		this.resultClass = resultClass;
	}
	public String getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(String gradePoint) {
		this.gradePoint = gradePoint;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getUniversitySubjectCode() {
		return universitySubjectCode;
	}
	public void setUniversitySubjectCode(String universitySubjectCode) {
		this.universitySubjectCode = universitySubjectCode;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	@Override
	public int compareTo(ExamSubCoursewiseGradeDefnTO arg0) {
		if(arg0!=null && this!=null && arg0.getGrade()!=null 
				 && this.getGrade()!=null){
			return this.getGrade().compareTo(arg0.getGrade());
		}else
			return 0;
	}
	

}
