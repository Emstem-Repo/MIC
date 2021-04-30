package com.kp.cms.forms.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.EmployeeTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamCourseUtilTO;
import com.kp.cms.transactions.admin.StudentLoginTO;

public class SendBulkSmsToStudentParentsNewForm extends BaseActionForm{
	private List<ProgramTypeTO> programTypeList;
	private String method;
	private List<Student> rejectedList;
	private List<StudentLoginTO> successList;
	private boolean sameUseridPassword;
	private String sendMail;
	private String userName;
	private String resetPwd;
	private String message;
	private boolean isStudent;
	private String[] courseIds;
	private List<ExamCourseUtilTO> listCourseName;
	private String studentWise;
	private String classWise;
	private String teachingWise;
	private String nonteachingWise;
	private String[] departmentIds;
	private Map<Integer,String> departmentMap;
	private List<EmployeeTO> employeeList;
	private String sendTo;
	private String[] deptIds;
	private String classes;
	private List<StudentTO> studentList;
	private String parent;
	
	@Override
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
	public boolean getIsStudent() {
		return isStudent;
	}
	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	
	public List<ExamCourseUtilTO> getListCourseName() {
		return listCourseName;
	}
	public void setListCourseName(List<ExamCourseUtilTO> listCourseName) {
		this.listCourseName = listCourseName;
	}
	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	public String[] getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(String[] courseIds) {
		this.courseIds = courseIds;
	}
	public String getStudentWise() {
		return studentWise;
	}
	public void setStudentWise(String studentWise) {
		this.studentWise = studentWise;
	}
	public String getClassWise() {
		return classWise;
	}
	public void setClassWise(String classWise) {
		this.classWise = classWise;
	}
	public String getTeachingWise() {
		return teachingWise;
	}
	public void setTeachingWise(String teachingWise) {
		this.teachingWise = teachingWise;
	}
	public String getNonteachingWise() {
		return nonteachingWise;
	}
	public void setNonteachingWise(String nonteachingWise) {
		this.nonteachingWise = nonteachingWise;
	}
	public String[] getDepartmentIds() {
		return departmentIds;
	}
	public void setDepartmentIds(String[] departmentIds) {
		this.departmentIds = departmentIds;
	}
	public Map<Integer, String> getDepartmentMap() {
		return departmentMap;
	}
	public void setDepartmentMap(Map<Integer, String> departmentMap) {
		this.departmentMap = departmentMap;
	}
	public List<EmployeeTO> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List<EmployeeTO> employeeList) {
		this.employeeList = employeeList;
	}
	public String getSendTo() {
		return sendTo;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public String[] getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String[] deptIds) {
		this.deptIds = deptIds;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public List<StudentTO> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<StudentTO> studentList) {
		this.studentList = studentList;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
	
	
	
	

}
