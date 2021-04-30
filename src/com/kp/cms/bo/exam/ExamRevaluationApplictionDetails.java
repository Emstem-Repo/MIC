package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;

import com.kp.cms.bo.admin.Subject;

public class ExamRevaluationApplictionDetails implements Serializable {
private int id;
private ExamRevaluationApp examRevApp;
private Subject subject;
private boolean isApplied;
private Date createdDate;
private String marks;

;
public ExamRevaluationApplictionDetails() {
	super();
}

public ExamRevaluationApplictionDetails(int id,
		ExamRevaluationApp examRevApp, Subject subject,
		String status, String type, Boolean isActive, Date createdDate,
		Date lastModifiedDate, String createdBy, String modifiedBy) {
	super();
	this.id = id;
	this.examRevApp = examRevApp;
	this.subject = subject;
	this.createdDate = createdDate;

}



public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public ExamRevaluationApp getExamRevApp() {
	return examRevApp;
}
public void setExamRevApp(ExamRevaluationApp examRevApp) {
	this.examRevApp = examRevApp;
}
public Subject getSubject() {
	return subject;
}
public void setSubject(Subject subject) {
	this.subject = subject;
}

public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public boolean getIsApplied() {
	return isApplied;
}

public void setIsApplied(boolean isApplied) {
	this.isApplied = isApplied;
}

public String getMarks() {
	return marks;
}

public void setMarks(String marks) {
	this.marks = marks;
}

}