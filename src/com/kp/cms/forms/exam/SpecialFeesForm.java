package com.kp.cms.forms.exam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SpecialFeesTO;
import com.kp.cms.to.exam.SupplementaryFeesTo;

public class SpecialFeesForm extends BaseActionForm {
	private String[] programIds;
	private List<ProgramTO> programList;
	private int id;
	private String mode;
	private String method;
	private List<SupplementaryFeesTo> toList;
	List<ProgramTypeTO> programTypeList;
	private List<SpecialFeesTO> regularExamToList;
	private String[] selectedCourse;
	private String applicationFees;
	private String specialFees;
	private String tuitionFees;
	private String lateFineFees;
	private String[] selectedClasses;
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
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}
	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}
	public List<SpecialFeesTO> getRegularExamToList() {
		return regularExamToList;
	}
	public void setRegularExamToList(List<SpecialFeesTO> regularExamToList) {
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
	public String getSpecialFees() {
		return specialFees;
	}
	public void setSpecialFees(String specialFees) {
		this.specialFees = specialFees;
	}
	public String getTuitionFees() {
		return tuitionFees;
	}
	public void setTuitionFees(String tuitionFees) {
		this.tuitionFees = tuitionFees;
	}
	public String getLateFineFees() {
		return lateFineFees;
	}
	public void setLateFineFees(String lateFineFees) {
		this.lateFineFees = lateFineFees;
	}
	public String[] getSelectedClasses() {
		return selectedClasses;
	}
	public void setSelectedClasses(String[] selectedClasses) {
		this.selectedClasses = selectedClasses;
	}
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public void resetFields(){
		this.programIds=null;
		this.applicationFees=null;
		this.tuitionFees=null;
		this.specialFees=null;
		this.lateFineFees=null;
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
}
