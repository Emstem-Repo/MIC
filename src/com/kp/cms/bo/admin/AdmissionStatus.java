package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

/**
 * AdmissionStatus generated by hbm2java
 */
public class AdmissionStatus implements java.io.Serializable {

	private int id;
	private String name;
	private boolean isActive;
	public AdmissionStatus() {
	}

	public AdmissionStatus(int id) {
		this.id = id;
	}

	public AdmissionStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}
