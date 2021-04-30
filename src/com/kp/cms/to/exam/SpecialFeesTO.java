package com.kp.cms.to.exam;

import java.io.Serializable;

public class SpecialFeesTO implements Serializable{
	
	private int id;
	private String className;
	private double tutionFees;
	private double specialFees;
	private double applicationFees;
	private double lateFineFees;
	private String academicYear;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public double getTutionFees() {
		return tutionFees;
	}
	public void setTutionFees(double tutionFees) {
		this.tutionFees = tutionFees;
	}
	public double getSpecialFees() {
		return specialFees;
	}
	public void setSpecialFees(double specialFees) {
		this.specialFees = specialFees;
	}
	public double getApplicationFees() {
		return applicationFees;
	}
	public void setApplicationFees(double applicationFees) {
		this.applicationFees = applicationFees;
	}
	public double getLateFineFees() {
		return lateFineFees;
	}
	public void setLateFineFees(double lateFineFees) {
		this.lateFineFees = lateFineFees;
	}
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	
	
	

}
