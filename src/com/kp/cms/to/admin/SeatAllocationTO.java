package com.kp.cms.to.admin;

import java.io.Serializable;


public class SeatAllocationTO implements Serializable{
	private int id;
	private int admittedThroughId;
	private int courseId;
	private int noofSeats;
	private int chanceMemoLimit;
	private AdmittedThroughTO admittedThroughTO;
	private String admittedThroughName;
	
	
	public String getAdmittedThroughName() {
		return admittedThroughName;
	}
	public void setAdmittedThroughName(String admittedThroughName) {
		this.admittedThroughName = admittedThroughName;
	}
	public int getAdmittedThroughId() {
		return admittedThroughId;
	}
	public void setAdmittedThroughId(int admittedThroughId) {
		this.admittedThroughId = admittedThroughId;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNoofSeats() {
		return noofSeats;
	}
	public void setNoofSeats(int noofSeats) {
		this.noofSeats = noofSeats;
	}

	public SeatAllocationTO(){};
	
	public SeatAllocationTO(int id, int admittedThroughId,
			int courseId, int noofSeats ,int chanceMemoLimit) {
		super();
		this.id = id;
		this.admittedThroughId = admittedThroughId;
		this.courseId = courseId;
		this.noofSeats = noofSeats;
		this.chanceMemoLimit = chanceMemoLimit;
	}
	public AdmittedThroughTO getAdmittedThroughTO() {
		return admittedThroughTO;
	}
	public void setAdmittedThroughTO(AdmittedThroughTO admittedThroughTO) {
		this.admittedThroughTO = admittedThroughTO;
	}
	public void setChanceMemoLimit(int chanceMemoLimit) {
		this.chanceMemoLimit = chanceMemoLimit;
	}
	public int getChanceMemoLimit() {
		return chanceMemoLimit;
	}

	
	
}
