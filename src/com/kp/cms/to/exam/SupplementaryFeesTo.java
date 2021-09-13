package com.kp.cms.to.exam;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author user
 *
 */
public class SupplementaryFeesTo implements Serializable,Comparable<SupplementaryFeesTo> {
	
	private int id;
	private String courseName;
	private double theoryFees;
	private double practicalFees;
	private double improvementTheoryFees;
	private double improvementPracticalFees;
	private double applicationFees;
	private double cvCampFees;
	private double marksListFees;
	private double onlineServiceChargeFees;
	private String academicYear;
	private double egrandFees;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getTheoryFees() {
		return theoryFees;
	}
	public void setTheoryFees(double theoryFees) {
		this.theoryFees = theoryFees;
	}
	public double getPracticalFees() {
		return practicalFees;
	}
	public void setPracticalFees(double practicalFees) {
		this.practicalFees = practicalFees;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SupplementaryFeesTo arg0) {
		if(arg0 instanceof SupplementaryFeesTo && arg0.getCourseName()!=null ){
			return this.courseName.compareTo(arg0.getCourseName());
		}else
			return 0;
	}
	public double getImprovementTheoryFees() {
		return improvementTheoryFees;
	}
	public void setImprovementTheoryFees(double improvementTheoryFees) {
		this.improvementTheoryFees = improvementTheoryFees;
	}
	public double getImprovementPracticalFees() {
		return improvementPracticalFees;
	}
	public void setImprovementPracticalFees(double improvementPracticalFees) {
		this.improvementPracticalFees = improvementPracticalFees;
	}
	public double getApplicationFees() {
		return applicationFees;
	}
	public void setApplicationFees(double applicationFees) {
		this.applicationFees = applicationFees;
	}
	public double getCvCampFees() {
		return cvCampFees;
	}
	public void setCvCampFees(double cvCampFees) {
		this.cvCampFees = cvCampFees;
	}
	public double getMarksListFees() {
		return marksListFees;
	}
	public void setMarksListFees(double marksListFees) {
		this.marksListFees = marksListFees;
	}
	public double getOnlineServiceChargeFees() {
		return onlineServiceChargeFees;
	}
	public void setOnlineServiceChargeFees(double onlineServiceChargeFees) {
		this.onlineServiceChargeFees = onlineServiceChargeFees;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	public double getEgrandFees() {
		return egrandFees;
	}
	public void setEgrandFees(double egrandFees) {
		this.egrandFees = egrandFees;
	}
	
}
