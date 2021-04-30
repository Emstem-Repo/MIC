package com.kp.cms.forms.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ConductAndCertificateTO;

public class ConductAndCertificateForm extends BaseActionForm{
	
	private String searchBy;
	private String registerNo;
	private ConductAndCertificateTO certificateTO;
	
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public ConductAndCertificateTO getCertificateTO() {
		return certificateTO;
	}
	public void setCertificateTO(ConductAndCertificateTO certificateTO) {
		this.certificateTO = certificateTO;
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	

}
