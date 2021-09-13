package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;

public class ExamFalseNumberGen implements Serializable {
	private int id;
	private Classes classId;
	private Student studentId;
	private ExamDefinition examId;
	private String marksCardNo;
	private String registerNo;
    private String marksCardNo1;
	private Date createdDate;
	private String createdBy;
	private String falseNo;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Subject subject;
	private Course course;
	
	public ExamFalseNumberGen(){}
	
	
	public String getRegisterNo() {
		return registerNo;
	}


	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}


	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Classes getClassId() {
		return classId;
	}
	public void setClassId(Classes classId) {
		this.classId = classId;
	}
	public Student getStudentId() {
		return studentId;
	}
	public void setStudentId(Student studentId) {
		this.studentId = studentId;
	}
	public ExamDefinition getExamId() {
		return examId;
	}
	public void setExamId(ExamDefinition examId) {
		this.examId = examId;
	}
	public String getMarksCardNo() {
		return marksCardNo;
	}
	public void setMarksCardNo(String marksCardNo) {
		this.marksCardNo = marksCardNo;
	}

	public String getMarksCardNo1() {
		return marksCardNo1;
	}
	
	public void setMarksCardNo1(String marksCardNo1) {
		this.marksCardNo1 = marksCardNo1;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFalseNo() {
		return falseNo;
	}

	public void setFalseNo(String falseNo) {
		this.falseNo = falseNo;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public Subject getSubject() {
		return subject;
	}


	public void setSubject(Subject subject) {
		this.subject = subject;
	}


	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}



}
