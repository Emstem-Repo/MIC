package com.kp.cms.forms.usermanagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.usermanagement.OptionalCourseApplicationTO;

public class OptionalCourseApplicationForm extends BaseActionForm{
	List<OptionalCourseApplicationTO> optionalCourseApplicationTO;
	String stuName;
	String departmentName;
	String courseName;
	String registerNo;
	String rollNo;
	String dob;
	String courseId;
	String studentId;
	String classId;
	String deptId;
	List<OptionalCourseApplicationTO> optionCourseList;
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	boolean isSubmitted;
	

	public boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	Map<Integer,Integer> optionMap = new HashMap<Integer,Integer>();
	

	public Map<Integer, Integer> getOptionMap() {
		return optionMap;
	}

	public void setOptionMap(Map<Integer, Integer> optionMap) {
		this.optionMap = optionMap;
	}
	public List<OptionalCourseApplicationTO> getOptionalCourseApplicationTO() {
		return optionalCourseApplicationTO;
	}

	public void setOptionalCourseApplicationTO(
			List<OptionalCourseApplicationTO> optionalCourseApplicationTO) {
		this.optionalCourseApplicationTO = optionalCourseApplicationTO;
	}
	public List<OptionalCourseApplicationTO> getOptionCourseList() {
		return optionCourseList;
	}

	public void setOptionCourseList(
			List<OptionalCourseApplicationTO> optionCourseList) {
		this.optionCourseList = optionCourseList;
	}

}
