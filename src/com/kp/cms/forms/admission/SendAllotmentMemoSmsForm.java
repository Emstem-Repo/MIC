package com.kp.cms.forms.admission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.ExamCourseUtilTO;
import com.kp.cms.transactions.admin.StudentLoginTO;

public class SendAllotmentMemoSmsForm extends BaseActionForm {
	
	private List<ProgramTypeTO> programTypeList;
	private String method;
	private List<Student> rejectedList;
	private List<StudentLoginTO> successList;
	private boolean sameUseridPassword;
	private String sendMail;
	private String userName;
	private String resetPwd;
	private String message;
	private boolean isSureMemo;
	private Integer sureOrChanceMemoNo;
	private String [] courseIds ;
	private List<ExamCourseUtilTO> listCourseName;
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter(CMSConstants.FORMNAME);
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}
	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<Student> getRejectedList() {
		return rejectedList;
	}
	public void setRejectedList(List<Student> rejectedList) {
		this.rejectedList = rejectedList;
	}
	public List<StudentLoginTO> getSuccessList() {
		return successList;
	}
	public void setSuccessList(List<StudentLoginTO> successList) {
		this.successList = successList;
	}
	public boolean isSameUseridPassword() {
		return sameUseridPassword;
	}
	public void setSameUseridPassword(boolean sameUseridPassword) {
		this.sameUseridPassword = sameUseridPassword;
	}
	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getResetPwd() {
		return resetPwd;
	}
	public void setResetPwd(String resetPwd) {
		this.resetPwd = resetPwd;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean getIsSureMemo() {
		return isSureMemo;
	}
	public void setIsSureMemo(boolean isSureMemo) {
		this.isSureMemo = isSureMemo;
	}
	
	public List<ExamCourseUtilTO> getListCourseName() {
		return listCourseName;
	}
	public void setListCourseName(List<ExamCourseUtilTO> listCourseName) {
		this.listCourseName = listCourseName;
	}
	public void setSureMemo(boolean isSureMemo) {
		this.isSureMemo = isSureMemo;
	}
	public String[] getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(String[] courseIds) {
		this.courseIds = courseIds;
	}
	public void setSureOrChanceMemoNo(Integer sureOrChanceMemoNo) {
		this.sureOrChanceMemoNo = sureOrChanceMemoNo;
	}
	public Integer getSureOrChanceMemoNo() {
		return sureOrChanceMemoNo;
	}
	
	

}
