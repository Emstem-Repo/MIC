package com.kp.cms.forms.admission;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;

public class ExportPhotosForm extends BaseActionForm{
	private String admittedYear;
	//private String folderName;
	private Map<Integer, String> listProgram;
	private String admitOrAll;
	
	
	
	public String getAdmitOrAll() {
		return admitOrAll;
	}
	public void setAdmitOrAll(String admitOrAll) {
		this.admitOrAll = admitOrAll;
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}
	public void setListProgram(Map<Integer, String> listProgram) {
		this.listProgram = listProgram;
	}

	public Map<Integer, String> getListProgram() {
		return listProgram;
	}

	public void setAdmittedYear(String admittedYear) {
		this.admittedYear = admittedYear;
	}

	public String getAdmittedYear() {
		return admittedYear;
	}

	

}
