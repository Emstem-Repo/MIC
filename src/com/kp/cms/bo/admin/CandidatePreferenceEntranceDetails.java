package com.kp.cms.bo.admin;

// Generated Apr 14, 2009 10:52:57 AM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

/**
 * CandidateEntranceDetails generated by hbm2java
 */
public class CandidatePreferenceEntranceDetails implements java.io.Serializable {

	private int id;
	private Entrance entrance;
	private AdmAppln admAppln;
	private String entranceRollNo;
	private Integer yearPassing;
	private Integer monthPassing;
	private BigDecimal marksObtained;
	private BigDecimal totalMarks;
	private Course course;
	private Integer prefNo;

	public CandidatePreferenceEntranceDetails() {
	}

	public CandidatePreferenceEntranceDetails(int id) {
		this.id = id;
	}

	public CandidatePreferenceEntranceDetails(int id, 
			AdmAppln admAppln, Entrance entrance,
			String entranceRollNo,Integer yearPassing,Integer monthPassing,
			BigDecimal marksObtained,BigDecimal totalMarks,Course course,Integer prefNo) {
		this.id = id;
		
		this.admAppln = admAppln;
		this.entrance = entrance;
		this.entranceRollNo = entranceRollNo;
		this.yearPassing=yearPassing;
		this.monthPassing=monthPassing;
		this.marksObtained=marksObtained;
		this.totalMarks=totalMarks;
		this.course=course;
		this.prefNo=prefNo;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public AdmAppln getAdmAppln() {
		return this.admAppln;
	}

	public void setAdmAppln(AdmAppln admAppln) {
		this.admAppln = admAppln;
	}

	
	public String getEntranceRollNo() {
		return this.entranceRollNo;
	}

	public void setEntranceRollNo(String entranceRollNo) {
		this.entranceRollNo = entranceRollNo;
	}

	
	public Entrance getEntrance() {
		return this.entrance;
	}

	public void setEntrance(Entrance entrance) {
		this.entrance = entrance;
	}

	public Integer getYearPassing() {
		return yearPassing;
	}

	public void setYearPassing(Integer yearPassing) {
		this.yearPassing = yearPassing;
	}

	public Integer getMonthPassing() {
		return monthPassing;
	}

	public void setMonthPassing(Integer monthPassing) {
		this.monthPassing = monthPassing;
	}

	public BigDecimal getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(BigDecimal marksObtained) {
		this.marksObtained = marksObtained;
	}

	public BigDecimal getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(BigDecimal totalMarks) {
		this.totalMarks = totalMarks;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getPrefNo() {
		return prefNo;
	}

	public void setPrefNo(Integer prefNo) {
		this.prefNo = prefNo;
	}
	

}
