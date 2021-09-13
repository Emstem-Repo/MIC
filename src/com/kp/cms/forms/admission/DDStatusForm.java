package com.kp.cms.forms.admission;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admission.DDStatusTO;

public class DDStatusForm extends BaseActionForm {
	
	private String applnNo;
	private String recievedDDNo;
	private String recievedDDDate;
	
	//raghu
	private String recievedChallanNo;
	private String recievedChallanDate;
	private List<DDStatusTO> ddStatusList;
	private List<ProgramTypeTO> programTypeList;
	private Map<Integer,String> studentMap;
	private Map<Integer, String> examMap;
	private String classes;
	private String chalanVerifiedCount;
	private String chalanNotVerified;

	public List<DDStatusTO> getDdStatusList() {
		return ddStatusList;
	}
	public void setDdStatusList(List<DDStatusTO> ddStatusList) {
		this.ddStatusList = ddStatusList;
	}
	public String getApplnNo() {
		return applnNo;
	}
	public void setApplnNo(String applnNo) {
		this.applnNo = applnNo;
	}
	public String getRecievedDDNo() {
		return recievedDDNo;
	}
	public void setRecievedDDNo(String recievedDDNo) {
		this.recievedDDNo = recievedDDNo;
	}
	public String getRecievedDDDate() {
		return recievedDDDate;
	}
	public void setRecievedDDDate(String recievedDDDate) {
		this.recievedDDDate = recievedDDDate;
	}
	
	public String getRecievedChallanNo() {
		return recievedChallanNo;
	}
	public void setRecievedChallanNo(String recievedChallanNo) {
		this.recievedChallanNo = recievedChallanNo;
	}
	
	public String getRecievedChallanDate() {
		return recievedChallanDate;
	}
	public void setRecievedChallanDate(String recievedChallanDate) {
		this.recievedChallanDate = recievedChallanDate;
	}
	
	public void resetFields() {
		this.applnNo=null;
		this.recievedDDDate=null;
		this.recievedDDNo=null;
		super.setYear(null);
		this.recievedChallanDate=null;
		this.recievedChallanNo=null;
		this.ddStatusList=null;
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	
	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		
		this.programTypeList=programTypeList;
	}
	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}
	public Map<Integer, String> getStudentMap() {
		return studentMap;
	}
	public void setStudentMap(Map<Integer, String> studentMap) {
		this.studentMap = studentMap;
	}
	public Map<Integer, String> getExamMap() {
		return examMap;
	}
	public void setExamMap(Map<Integer, String> examMap) {
		this.examMap = examMap;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getChalanVerifiedCount() {
		return chalanVerifiedCount;
	}
	public void setChalanVerifiedCount(String chalanVerifiedCount) {
		this.chalanVerifiedCount = chalanVerifiedCount;
	}
	public String getChalanNotVerified() {
		return chalanNotVerified;
	}
	public void setChalanNotVerified(String chalanNotVerified) {
		this.chalanNotVerified = chalanNotVerified;
	}

	
}
