package com.kp.cms.to.admin;

import org.apache.struts.upload.FormFile;

public class HigherEducationDTO {
	private String higherEduProgram;
	private String college;
	private String yearOfAdmission;
	private FormFile document;
	
	public String getHigherEduProgram() {
		return higherEduProgram;
	}
	public void setHigherEduProgram(String higherEduProgram) {
		this.higherEduProgram = higherEduProgram;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getYearOfAdmission() {
		return yearOfAdmission;
	}
	public void setYearOfAdmission(String yearOfAdmission) {
		this.yearOfAdmission = yearOfAdmission;
	}
	public FormFile getDocument() {
		return document;
	}
	public void setDocument(FormFile document) {
		this.document = document;
	}
}
