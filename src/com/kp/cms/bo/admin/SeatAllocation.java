package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

/**
 * SeatAllocation generated by hbm2java
 */
public class SeatAllocation implements java.io.Serializable {

	private int id;
	private AdmittedThrough admittedThrough;
	private Course course;
	private Integer noOfSeats;
	private Integer chanceMemoLimit;

	public SeatAllocation() {
	}

	public SeatAllocation(int id) {
		this.id = id;
	}

	public SeatAllocation(int id, AdmittedThrough admittedThrough,
			Course course, Integer noOfSeats ,Integer chanceMemoLimit) {
		this.id = id;
		this.admittedThrough = admittedThrough;
		this.course = course;
		this.noOfSeats = noOfSeats;
		this.chanceMemoLimit = chanceMemoLimit;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AdmittedThrough getAdmittedThrough() {
		return this.admittedThrough;
	}

	public void setAdmittedThrough(AdmittedThrough admittedThrough) {
		this.admittedThrough = admittedThrough;
	}

	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getNoOfSeats() {
		return this.noOfSeats;
	}

	public void setNoOfSeats(Integer noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public void setChanceMemoLimit(Integer chanceMemoLimit) {
		this.chanceMemoLimit = chanceMemoLimit;
	}

	public Integer getChanceMemoLimit() {
		return chanceMemoLimit;
	}

}
