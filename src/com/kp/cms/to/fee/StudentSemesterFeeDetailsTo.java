package com.kp.cms.to.fee;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kp.cms.bo.admin.Student;

public class StudentSemesterFeeDetailsTo implements Serializable {
	
	private int id;
	private String studentName;
	private String classId;
	private String className;
	private String registerNo;
	private String semester;
	
	private double universityFee;
	private double specialFee;
	private double otherFee;
	private double CATrainingFee;
	private double semesterFee;
	
	private boolean feeApprove;
	private String remarks;
	private String date;
	private String studentId;
	private Student student;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
	public double getUniversityFee() {
		return universityFee;
	}
	public void setUniversityFee(double universityFee) {
		this.universityFee = universityFee;
	}
	public double getSpecialFee() {
		return specialFee;
	}
	public void setSpecialFee(double specialFee) {
		this.specialFee = specialFee;
	}
	public double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(double otherFee) {
		this.otherFee = otherFee;
	}
	public double getCATrainingFee() {
		return CATrainingFee;
	}
	public void setCATrainingFee(double cATrainingFee) {
		CATrainingFee = cATrainingFee;
	}
	public Boolean getFeeApprove() {
		return feeApprove;
	}
	public void setFeeApprove(Boolean feeApprove) {
		this.feeApprove = feeApprove;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public void setFeeApprove(boolean feeApprove) {
		this.feeApprove = feeApprove;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public double getSemesterFee() {
		return semesterFee;
	}
	public void setSemesterFee(double semesterFee) {
		this.semesterFee = semesterFee;
	}

}
