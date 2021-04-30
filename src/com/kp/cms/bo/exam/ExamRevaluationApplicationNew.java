package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;

public class ExamRevaluationApplicationNew implements Serializable {
	
	private int id;
	private ExamDefinitionBO exam;
	private Student student;
	private Subject subject;
	private Classes classes;
	private boolean isRevaluation;
	private boolean isAnswerScrptCopy;
	private boolean isScrutiny;
	private boolean isApplied;
	private String marks;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private String maxMarks;
	private RevaluationApplicationPGIDetails revaluationPgiDetails;
	
	
	



	public ExamRevaluationApplicationNew() {
		super();
	}
	
	

	public ExamRevaluationApplicationNew(int id, ExamDefinitionBO exam,
			Student student, Subject subject, Classes classes,
			boolean isRevaluation, boolean isAnswerScrptCopy,
			boolean isScrutiny, boolean isApplied, String marks,
			String createdBy, String modifiedBy, Date createdDate,
			Date lastModifiedDate,String maxMarks,RevaluationApplicationPGIDetails revaluationPgiDetails) {
		super();
		this.id = id;
		this.exam = exam;
		this.student = student;
		this.subject = subject;
		this.classes = classes;
		this.isRevaluation = isRevaluation;
		this.isAnswerScrptCopy = isAnswerScrptCopy;
		this.isScrutiny = isScrutiny;
		this.isApplied = isApplied;
		this.marks = marks;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.maxMarks = maxMarks;
		this.revaluationPgiDetails = revaluationPgiDetails;
		
		
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
	public boolean getIsApplied() {
		return isApplied;
	}
	public void setIsApplied(boolean isApplied) {
		this.isApplied = isApplied;
	}

	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public boolean getIsRevaluation() {
		return isRevaluation;
	}
	public void setIsRevaluation(boolean isRevaluation) {
		this.isRevaluation = isRevaluation;
	}
	public boolean getIsAnswerScrptCopy() {
		return isAnswerScrptCopy;
	}
	public void setIsAnswerScrptCopy(boolean isAnswerScrptCopy) {
		this.isAnswerScrptCopy = isAnswerScrptCopy;
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



	public String getMaxMarks() {
		return maxMarks;
	}



	public void setMaxMarks(String maxMarks) {
		this.maxMarks = maxMarks;
	}

	public RevaluationApplicationPGIDetails getRevaluationPgiDetails() {
		return revaluationPgiDetails;
	}



	public void setRevaluationPgiDetails(
			RevaluationApplicationPGIDetails revaluationPgiDetails) {
		this.revaluationPgiDetails = revaluationPgiDetails;
	}

	
	

}
