package com.kp.cms.forms.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.StudentTO;
public class ExamFalseNumberForm extends BaseActionForm {
	private String examNameId;
	private String classNameId;
	private String studentId;
	private String examType;
	private String regNo;
	private String isDuplicate;
	private String totalCount;
	private Map<Integer, String> listClassName;
	private Map<Integer, String> listExamName;
	private String radioId;
	private  String regForConsolidate;
	private String year;
	private String courseId;
	private Map<Integer,String> classMap = new HashMap<Integer, String>();
	private String isRegular;
	private String isSupplementary;
	private String isImprovement;
	private String isRevaluation;
	private String semister;
	private String examName;
	private String className;
	private int halfLength;
	private List<ProgramTypeTO> programTypeList;
	private List<StudentTO> studentList;
	private Map<Integer, ExamFalseNumberGen> studentMap;
	
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);

		return actionErrors;
	}
	
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}

	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}

	public List<StudentTO> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<StudentTO> studentList) {
		this.studentList = studentList;
	}

	public Map<Integer, ExamFalseNumberGen> getStudentMap() {
		return studentMap;
	}

	public void setStudentMap(Map<Integer, ExamFalseNumberGen> studentMap) {
		this.studentMap = studentMap;
	}

	public void reset(){
		this.classNameId=null;
		this.isDuplicate=null;
		this.regNo=null;
		this.isDuplicate="no";
		this.examType="";
		this.listClassName=null;
		this.listExamName=null;
		this.isRegular=null;
		this.isSupplementary=null;
		this.isImprovement=null;
		this.isRevaluation=null;
		this.studentList=null;
		this.studentMap=null;
	}

	public Map<Integer, String> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<Integer, String> classMap) {
		this.classMap = classMap;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getExamNameId() {
		return examNameId;
	}

	public void setExamNameId(String examNameId) {
		this.examNameId = examNameId;
	}

	public String getClassNameId() {
		return classNameId;
	}

	public void setClassNameId(String classNameId) {
		this.classNameId = classNameId;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(String isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public Map<Integer, String> getListClassName() {
		return listClassName;
	}

	public void setListClassName(Map<Integer, String> listClassName) {
		this.listClassName = listClassName;
	}

	public Map<Integer, String> getListExamName() {
		return listExamName;
	}

	public void setListExamName(Map<Integer, String> listExamName) {
		this.listExamName = listExamName;
	}

	public String getRadioId() {
		return radioId;
	}

	public void setRadioId(String radioId) {
		this.radioId = radioId;
	}

	public String getRegForConsolidate() {
		return regForConsolidate;
	}

	public void setRegForConsolidate(String regForConsolidate) {
		this.regForConsolidate = regForConsolidate;
	}

	public String getIsRegular() {
		return isRegular;
	}

	public void setIsRegular(String isRegular) {
		this.isRegular = isRegular;
	}

	public String getIsSupplementary() {
		return isSupplementary;
	}

	public void setIsSupplementary(String isSupplementary) {
		this.isSupplementary = isSupplementary;
	}

	public String getIsImprovement() {
		return isImprovement;
	}

	public void setIsImprovement(String isImprovement) {
		this.isImprovement = isImprovement;
	}

	public String getIsRevaluation() {
		return isRevaluation;
	}

	public void setIsRevaluation(String isRevaluation) {
		this.isRevaluation = isRevaluation;
	}

	public String getSemister() {
		return semister;
	}

	public void setSemister(String semister) {
		this.semister = semister;
	}

	
	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getHalfLength() {
		return halfLength;
	}

	public void setHalfLength(int halfLength) {
		this.halfLength = halfLength;
	}


}
