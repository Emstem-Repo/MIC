package com.kp.cms.forms.admin;

import java.util.List;		

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTypeTO;

public class GeneratePasswordParentForm  extends BaseActionForm{
private String parentUserName;
private String parentPassword;
private List<ProgramTypeTO> programTypeList;
private List<Student> rejectList;
private List<StudentLogin> parentSuccessList;
private boolean studentRollNo;
private String sendSMS;
private boolean studentResgisterNo;
private String method;
private boolean sameUseridPassword;
private String message;


public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getParentUserName() {
	return parentUserName;
}
public void setParentUserName(String parentUserName) {
	this.parentUserName = parentUserName;
}
public String getParentPassword() {
	return parentPassword;
}
public void setParentPassword(String parentPassword) {
	this.parentPassword = parentPassword;
}
public List<ProgramTypeTO> getProgramTypeList() {
	return programTypeList;
}
public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
	this.programTypeList = programTypeList;
}
public List<Student> getRejectList() {
	return rejectList;
}
public void setRejectList(List<Student> rejectList) {
	this.rejectList = rejectList;
}
public List<StudentLogin> getParentSuccessList() {
	return parentSuccessList;
}
public void setParentSuccessList(List<StudentLogin> parentSuccessList) {
	this.parentSuccessList = parentSuccessList;
}
public boolean getStudentRollNo() {
	return studentRollNo;
}
public void setStudentRollNo(boolean studentRollNo) {
	this.studentRollNo = studentRollNo;
}
public boolean getStudentResgisterNo() {
	return studentResgisterNo;
}
public void setStudentResgisterNo(boolean studentResgisterNo) {
	this.studentResgisterNo = studentResgisterNo;
}
public String getMethod() {
	return method;
}
public void setMethod(String method) {
	this.method = method;
}
public String getSendSMS() {
	return sendSMS;
}
public void setSendSMS(String sendSMS) {
	this.sendSMS = sendSMS;
}

public boolean getSameUseridPassword() {
	return sameUseridPassword;
}
public void setSameUseridPassword(boolean sameUseridPassword) {
	this.sameUseridPassword = sameUseridPassword;
}

}
