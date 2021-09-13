package com.kp.cms.forms.admission;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.ProgramType;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admission.BoardDetailsTO;
import com.kp.cms.to.admission.CharacterAndConductTO;
import com.kp.cms.to.admission.TCDetailsTO;
import com.kp.cms.to.exam.ExamDefinitionTO;

public class TCDetailsForm extends BaseActionForm {
	
	private static final long serialVersionUID = 1L;
	private String className;
	List<BoardDetailsTO> boardList;
	private String registerNo;
	private String studentId;
	private TCDetailsTO tcDetailsTO;
	List<CharacterAndConductTO> list;
	private String month;
	private String dateOfApplication;
	private String feePaid;
	private String dateOfLeaving;
	private String reasonOfLeaving;
	private String characterId;
	private String scholarship;
	private String publicExamName;
	private String showRegisterNo;
	private String passed;
	private String isStudentPunished;
	private List<ExamDefinitionTO> examNames;
	private String leavingAcademicYear;
	List<ProgramTypeTO> programTypeList;
	List<CourseTO> courseList;
	private String searchBy;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public List<BoardDetailsTO> getBoardList() {
		return boardList;
	}
	public void setBoardList(List<BoardDetailsTO> boardList) {
		this.boardList = boardList;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public TCDetailsTO getTcDetailsTO() {
		return tcDetailsTO;
	}
	public void setTcDetailsTO(TCDetailsTO tcDetailsTO) {
		this.tcDetailsTO = tcDetailsTO;
	}
	public List<CharacterAndConductTO> getList() {
		return list;
	}
	public void setList(List<CharacterAndConductTO> list) {
		this.list = list;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDateOfApplication() {
		return dateOfApplication;
	}
	public void setDateOfApplication(String dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}
	public String getFeePaid() {
		return feePaid;
	}
	public void setFeePaid(String feePaid) {
		this.feePaid = feePaid;
	}
	public String getDateOfLeaving() {
		return dateOfLeaving;
	}
	public void setDateOfLeaving(String dateOfLeaving) {
		this.dateOfLeaving = dateOfLeaving;
	}
	public String getReasonOfLeaving() {
		return reasonOfLeaving;
	}
	public void setReasonOfLeaving(String reasonOfLeaving) {
		this.reasonOfLeaving = reasonOfLeaving;
	}
	public String getCharacterId() {
		return characterId;
	}
	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}
	public String getPublicExamName() {
		return publicExamName;
	}
	public void setPublicExamName(String publicExamName) {
		this.publicExamName = publicExamName;
	}
	public String getShowRegisterNo() {
		return showRegisterNo;
	}
	public void setShowRegisterNo(String showRegisterNo) {
		this.showRegisterNo = showRegisterNo;
	}
	public String getPassed() {
		return passed;
	}
	public void setPassed(String passed) {
		this.passed = passed;
	}
	public String getIsStudentPunished() {
		return isStudentPunished;
	}
	public void setIsStudentPunished(String isStudentPunished) {
		this.isStudentPunished = isStudentPunished;
	}
	public List<ExamDefinitionTO> getExamNames() {
		return examNames;
	}
	public void setExamNames(List<ExamDefinitionTO> examNames) {
		this.examNames = examNames;
	}
	public String getLeavingAcademicYear() {
		return leavingAcademicYear;
	}
	public void setLeavingAcademicYear(String leavingAcademicYear) {
		this.leavingAcademicYear = leavingAcademicYear;
	}
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}
	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}
	/**
	 *  To Reset the fields value in the form
	 */
	public void resetFields(){
		super.setClassId(null);
		super.setYear(null);
		super.setAcademicYear(null);
		super.setProgramTypeId(null);
		this.registerNo=null;
		this.studentId=null;
		this.tcDetailsTO=null;
		this.month=null;
		this.dateOfApplication=null;
		this.dateOfLeaving=null;
		this.feePaid="Yes";
		this.reasonOfLeaving=null;
		this.characterId=null;
		this.passed="Yes";
		this.scholarship="No";
		this.publicExamName=null;
		this.showRegisterNo="Yes";
		this.isStudentPunished="No";
		this.tcDetailsTO=new TCDetailsTO();
		this.searchBy = null;
	}
	public void reset()
	{
		this.publicExamName=null;
		this.tcDetailsTO=new TCDetailsTO();
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	public List<CourseTO> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<CourseTO> courseList) {
		this.courseList = courseList;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
}
