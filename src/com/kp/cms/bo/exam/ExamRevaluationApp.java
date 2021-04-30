package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;

public class ExamRevaluationApp implements Serializable {private int id;
private ExamDefinitionBO exam;
private Student student;
private Classes classes;
private boolean isRevaluation;
private boolean isScrutiny;
private String marks;
private String createdBy;
private String modifiedBy;
private Date createdDate;
private Date applicationDate;
private Date lastModifiedDate;
private Set<ExamRevaluationApplictionDetails> examRevaluationAppDetails;
private String challanNo;
private boolean isChallanVerified;
private double amount;

public ExamRevaluationApp() {
	super();
}

public ExamRevaluationApp(int id, ExamDefinitionBO exam,
		Student student, Subject subject, Classes classes,
		boolean isRevaluation, boolean isChallengeValuation,
		boolean isScrutiny, boolean isApplied, String marks,
		String createdBy, String modifiedBy, Date createdDate,
		Date lastModifiedDate) {
	super();
	this.id = id;
	this.exam = exam;
	this.student = student;
	this.classes = classes;
	this.isRevaluation = isRevaluation;
	this.isScrutiny = isScrutiny;
	this.marks = marks;
	this.createdBy = createdBy;
	this.modifiedBy = modifiedBy;
	this.createdDate = createdDate;
	this.lastModifiedDate = lastModifiedDate;
}



public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public ExamDefinitionBO getExam() {
	return exam;
}
public void setExam(ExamDefinitionBO exam) {
	this.exam = exam;
}
public Student getStudent() {
	return student;
}
public void setStudent(Student student) {
	this.student = student;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}
public String getModifiedBy() {
	return modifiedBy;
}
public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public Date getLastModifiedDate() {
	return lastModifiedDate;
}
public void setLastModifiedDate(Date lastModifiedDate) {
	this.lastModifiedDate = lastModifiedDate;
}
public Classes getClasses() {
	return classes;
}
public void setClasses(Classes classes) {
	this.classes = classes;
}

public boolean getIsRevaluation() {
	return isRevaluation;
}
public void setIsRevaluation(boolean isRevaluation) {
	this.isRevaluation = isRevaluation;
}

public boolean getIsScrutiny() {
	return isScrutiny;
}
public void setIsScrutiny(boolean isScrutiny) {
	this.isScrutiny = isScrutiny;
}
public String getMarks() {
	return marks;
}
public void setMarks(String marks) {
	this.marks = marks;
}

public Set<ExamRevaluationApplictionDetails> getExamRevaluationAppDetails() {
	return examRevaluationAppDetails;
}

public void setExamRevaluationAppDetails(
		Set<ExamRevaluationApplictionDetails> examRevaluationAppDetails) {
	this.examRevaluationAppDetails = examRevaluationAppDetails;
}

public String getChallanNo() {
	return challanNo;
}

public void setChallanNo(String challanNo) {
	this.challanNo = challanNo;
}

public boolean getIsChallanVerified() {
	return isChallanVerified;
}

public void setIsChallanVerified(boolean isChallanVerified) {
	this.isChallanVerified = isChallanVerified;
}

public double getAmount() {
	return amount;
}

public void setAmount(double amount) {
	this.amount = amount;
}

public Date getApplicationDate() {
	return applicationDate;
}

public void setApplicationDate(Date applicationDate) {
	this.applicationDate = applicationDate;
}}