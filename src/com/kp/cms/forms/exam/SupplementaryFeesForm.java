package com.kp.cms.forms.exam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SupplementaryFeesTo;

public class SupplementaryFeesForm extends BaseActionForm {
	
	private String[] programIds;
	private String theoryFees;
	private String practicalFees;
	private List<ProgramTO> programList;
	private int id;
	private String mode;
	private List<SupplementaryFeesTo> toList;
	List<ProgramTypeTO> programTypeList;
	private List<RegularExamFeesTo> regularExamToList;
	private String[] selectedCourse;
	private String applicationFees;
	private String cvCampFees;
	private String marksListFees;
	private String onlineServiceChargeFees;
	private String[] selectedClasses;

	public String[] getProgramIds() {
		return programIds;
	}
	public void setProgramIds(String[] programIds) {
		this.programIds = programIds;
	}
	public String getTheoryFees() {
		return theoryFees;
	}
	public void setTheoryFees(String theoryFees) {
		this.theoryFees = theoryFees;
	}
	public String getPracticalFees() {
		return practicalFees;
	}
	public void setPracticalFees(String practicalFees) {
		this.practicalFees = practicalFees;
	}
	
	public List<ProgramTO> getProgramList() {
		return programList;
	}
	public void setProgramList(List<ProgramTO> programList) {
		this.programList = programList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public List<SupplementaryFeesTo> getToList() {
		return toList;
	}
	public void setToList(List<SupplementaryFeesTo> toList) {
		this.toList = toList;
	}
	/**
	 * 
	 */
	public void resetFields(){
		this.programIds=null;
		this.theoryFees=null;
		this.practicalFees=null;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}
	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}
	public List<RegularExamFeesTo> getRegularExamToList() {
		return regularExamToList;
	}
	public void setRegularExamToList(List<RegularExamFeesTo> regularExamToList) {
		this.regularExamToList = regularExamToList;
	}
	public String[] getSelectedCourse() {
		return selectedCourse;
	}
	public void setSelectedCourse(String[] selectedCourse) {
		this.selectedCourse = selectedCourse;
	}
	public String getApplicationFees() {
		return applicationFees;
	}
	public void setApplicationFees(String applicationFees) {
		this.applicationFees = applicationFees;
	}
	public String getCvCampFees() {
		return cvCampFees;
	}
	public void setCvCampFees(String cvCampFees) {
		this.cvCampFees = cvCampFees;
	}
	public String getMarksListFees() {
		return marksListFees;
	}
	public void setMarksListFees(String marksListFees) {
		this.marksListFees = marksListFees;
	}
	public String getOnlineServiceChargeFees() {
		return onlineServiceChargeFees;
	}
	public void setOnlineServiceChargeFees(String onlineServiceChargeFees) {
		this.onlineServiceChargeFees = onlineServiceChargeFees;
	}
	public String[] getSelectedClasses() {
		return selectedClasses;
	}
	public void setSelectedClasses(String[] selectedClasses) {
		this.selectedClasses = selectedClasses;
	}

}
