package com.kp.cms.to.exam;

import java.io.Serializable;

public class MarksDetailsTO implements Serializable {
	private int id;
	private String theoryMarks;
	private String practicalMarks;
	private int classId;
	private int marksId;
	private boolean theorySecured;
	private boolean practicalSecured;
	private String subjectId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTheoryMarks() {
		return theoryMarks;
	}
	public void setTheoryMarks(String theoryMarks) {
		this.theoryMarks = theoryMarks;
	}
	public String getPracticalMarks() {
		return practicalMarks;
	}
	public void setPracticalMarks(String practicalMarks) {
		this.practicalMarks = practicalMarks;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getMarksId() {
		return marksId;
	}
	public void setMarksId(int marksId) {
		this.marksId = marksId;
	}
	public boolean isTheorySecured() {
		return theorySecured;
	}
	public void setTheorySecured(boolean theorySecured) {
		this.theorySecured = theorySecured;
	}
	public boolean isPracticalSecured() {
		return practicalSecured;
	}
	public void setPracticalSecured(boolean practicalSecured) {
		this.practicalSecured = practicalSecured;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	
}
