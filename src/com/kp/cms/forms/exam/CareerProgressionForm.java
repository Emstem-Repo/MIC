package com.kp.cms.forms.exam;

import java.util.List;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.HigherEducationDTO;
import com.kp.cms.to.admin.PlacementDTO;

public class CareerProgressionForm extends BaseActionForm {
	
	private String programType;
	private String batch;
	private String course;
	private List<HigherEducationDTO> higherEducationList;
	private List<PlacementDTO> placementList;
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public List<HigherEducationDTO> getHigherEducationList() {
		return higherEducationList;
	}
	public void setHigherEducationList(List<HigherEducationDTO> higherEducationList) {
		this.higherEducationList = higherEducationList;
	}
	public List<PlacementDTO> getPlacementList() {
		return placementList;
	}
	public void setPlacementList(List<PlacementDTO> placementList) {
		this.placementList = placementList;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
}
