package com.kp.cms.forms.exam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.ExamRevaluationFeeTO;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.RevaluationExamFeesTo;
import com.kp.cms.to.exam.SupplementaryFeesTo;

public class ExamRevaluationFeeForm extends BaseActionForm{
	
	private String[] programIds;
	private List<ProgramTO> programList;
	private int id;
	private String mode;
	private List<SupplementaryFeesTo> toList;
	List<ProgramTypeTO> programTypeList;
	private String[] selectedCourse;

	private String[] selectedClasses;
	
	private List<ExamRevaluationFeeTO> revaluationExamToList;
	private String marksCopyFees;
	private String revaluationFees;
	private String scrutinyFees;
	private String onlineSevriceFees;
	private String challengeValuationFees;
	private String applicationFees;

	public String[] getProgramIds() {
		return programIds;
	}
	public void setProgramIds(String[] programIds) {
		this.programIds = programIds;
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

	public String[] getSelectedCourse() {
		return selectedCourse;
	}
	public void setSelectedCourse(String[] selectedCourse) {
		this.selectedCourse = selectedCourse;
	}
	
	public String[] getSelectedClasses() {
		return selectedClasses;
	}
	public void setSelectedClasses(String[] selectedClasses) {
		this.selectedClasses = selectedClasses;
	}
	public List<ExamRevaluationFeeTO> getRevaluationExamToList() {
		return revaluationExamToList;
	}
	public void setRevaluationExamToList(
			List<ExamRevaluationFeeTO> revaluationExamToList) {
		this.revaluationExamToList = revaluationExamToList;
	}
	public String getMarksCopyFees() {
		return marksCopyFees;
	}
	public void setMarksCopyFees(String marksCopyFees) {
		this.marksCopyFees = marksCopyFees;
	}
	public String getRevaluationFees() {
		return revaluationFees;
	}
	public void setRevaluationFees(String revaluationFees) {
		this.revaluationFees = revaluationFees;
	}
	public String getScrutinyFees() {
		return scrutinyFees;
	}
	public void setScrutinyFees(String scrutinyFees) {
		this.scrutinyFees = scrutinyFees;
	}
	public String getOnlineSevriceFees() {
		return onlineSevriceFees;
	}
	public void setOnlineSevriceFees(String onlineSevriceFees) {
		this.onlineSevriceFees = onlineSevriceFees;
	}
	public String getChallengeValuationFees() {
		return challengeValuationFees;
	}
	public void setChallengeValuationFees(String challengeValuationFees) {
		this.challengeValuationFees = challengeValuationFees;
	}
	public String getApplicationFees() {
		return applicationFees;
	}
	public void setApplicationFees(String applicationFees) {
		this.applicationFees = applicationFees;
	}

}
