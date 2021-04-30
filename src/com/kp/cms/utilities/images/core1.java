package com.kp.cms.utilities.images;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.kp.cms.utilities.HibernateUtil;

public class core1 {

	
	private int courseid;
	private int rank;
	private int marks;
	private boolean allotment;
	private String caste;
	private int admid;
	
	
	public int getAdmid() {
		return admid;
	}
	public void setAdmid(int admid) {
		this.admid = admid;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public int getCourseid() {
		return courseid;
	}
	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public boolean isAllotment() {
		return allotment;
	}
	public void setAllotment(boolean allotment) {
		this.allotment = allotment;
	}
	
	
	

	

}
