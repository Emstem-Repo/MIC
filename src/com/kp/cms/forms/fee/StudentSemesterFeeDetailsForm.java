package com.kp.cms.forms.fee;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;

public class StudentSemesterFeeDetailsForm  extends BaseActionForm{
	private String academicYear;
	private Classes classes;
	private String semester;
	private Student student;
	private String studentName;
	private String regNo;
	private double universityFee;
	private double specialFee;
	private double otherFee;
	private double CATrainingFee;
	private Boolean isApprove;
	private String remarks;
	private String classId;
	private Map<Integer, String> classMap;
	private List<StudentSemesterFeeDetailsTo> studentList;
	private String className;
	private String date;
	private List<StudentSemesterFeeDetailsTo> stuFeeDetailsTos;
	private boolean isCourse;
	
	public List<StudentSemesterFeeDetailsTo> getStuFeeDetailsTos() {
		return stuFeeDetailsTos;
	}
	public void setStuFeeDetailsTos(
			List<StudentSemesterFeeDetailsTo> stuFeeDetailsTos) {
		this.stuFeeDetailsTos = stuFeeDetailsTos;
	}
	
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
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
	public Boolean getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public void resetFields(){
		this.classes = null;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Map<Integer, String> getClassMap() {
		return classMap;
	}
	public void setClassMap(Map<Integer, String> classMap) {
		this.classMap = classMap;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<StudentSemesterFeeDetailsTo> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<StudentSemesterFeeDetailsTo> studentList) {
		this.studentList = studentList;
	}
	public Boolean getIsCourse() {
		return isCourse;
	}
	public void setIsCourse(Boolean isCourse) {
		this.isCourse = isCourse;
	}
	

}
