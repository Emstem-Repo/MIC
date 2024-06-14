package com.kp.cms.to.admin;

import org.apache.struts.upload.FormFile;

public class PlacementDTO {
	private String placementFirm;
	private String annualSalary;
	private String yearOfPlacemment;
	private FormFile document;
	
	public String getPlacementFirm() {
		return placementFirm;
	}
	public void setPlacementFirm(String placementFirm) {
		this.placementFirm = placementFirm;
	}
	public String getAnnualSalary() {
		return annualSalary;
	}
	public void setAnnualSalary(String annualSalary) {
		this.annualSalary = annualSalary;
	}
	public String getYearOfPlacemment() {
		return yearOfPlacemment;
	}
	public void setYearOfPlacemment(String yearOfPlacemment) {
		this.yearOfPlacemment = yearOfPlacemment;
	}
	public FormFile getDocument() {
		return document;
	}
	public void setDocument(FormFile document) {
		this.document = document;
	}
	
}
