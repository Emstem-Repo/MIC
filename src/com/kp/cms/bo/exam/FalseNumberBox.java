package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.to.admin.ProgramTypeTO;

public class FalseNumberBox implements Serializable {
	private int id;
	private String chiefExaminer;
	
	private String additionalExaminer;
	private Integer academicYear;
	private Integer schemeNum;
	private Course courseId;
	private Users examinerId;
	private Integer boxNum;
	private ExamDefinition examId;
	private Subject subjectId;
	public FalseNumberBox(){
		
	}
	public int getId() {
		return id;
	}
	public FalseNumberBox(Course courseId, Users examinerId, ExamDefinition examId) {
		super();
		this.courseId = courseId;
		this.examinerId = examinerId;
		this.examId = examId;
	}
	public Subject getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Subject subjectId) {
		this.subjectId = subjectId;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Integer getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}
	public Integer getSchemeNum() {
		return schemeNum;
	}
	public void setSchemeNum(Integer schemeNum) {
		this.schemeNum = schemeNum;
	}
	public Course getCourseId() {
		return courseId;
	}
	public void setCourseId(Course courseId) {
		this.courseId = courseId;
	}
	public Users getExaminerId() {
		return examinerId;
	}
	public void setExaminerId(Users examinerId) {
		this.examinerId = examinerId;
	}
	public Integer getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}
	public ExamDefinition getExamId() {
		return examId;
	}
	public void setExamId(ExamDefinition examId) {
		this.examId = examId;
	}
	public String getChiefExaminer() {
		return chiefExaminer;
	}
	public void setChiefExaminer(String chiefExaminer) {
		this.chiefExaminer = chiefExaminer;
	}
	public String getAdditionalExaminer() {
		return additionalExaminer;
	}
	public void setAdditionalExaminer(String additionalExaminer) {
		this.additionalExaminer = additionalExaminer;
	}
	
	
	
}
