package com.kp.cms.bo.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.to.admin.ProgramTypeTO;

public class FalseNumberBoxDetails implements Serializable {
	private int id;
	private String falseNum;
	private FalseNumberBox falseNumBox;
	private Boolean isActive;
	
	public FalseNumberBoxDetails(){
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public FalseNumberBox getFalseNumBox() {
		return falseNumBox;
	}
	public void setFalseNumBox(FalseNumberBox falseNumBox) {
		this.falseNumBox = falseNumBox;
	}
	public String getFalseNum() {
		return falseNum;
	}
	public void setFalseNum(String falseNum) {
		this.falseNum = falseNum;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
}
