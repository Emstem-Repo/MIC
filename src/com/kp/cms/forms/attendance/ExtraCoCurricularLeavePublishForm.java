package com.kp.cms.forms.attendance;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.attendance.ExtraCoCurricularLeavePublishTO;

public class ExtraCoCurricularLeavePublishForm extends BaseActionForm {
 private int id;
 private Classes classes;
 private String publishFor;
 private String publishStartDate;
 private String publishEndDate;
 private Map<Integer, String> classMap;
 private List<ExtraCoCurricularLeavePublishTO> toList;
 
 
 



public ActionErrors validate(ActionMapping mapping,HttpServletRequest request){
	 String formName = request.getParameter("formName");
	 ActionErrors actionErrors = super.validate(mapping, request,formName);
	 return actionErrors;
 }
 
 public void reset(){
	 this.classes = null;
	 this.publishEndDate = null;
	 this.publishFor = null;
	 this.publishStartDate = null;
 }
 
 
 public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
 
public Classes getClasses() {
	return classes;
}
public void setClasses(Classes classes) {
	this.classes = classes;
}
public String getPublishFor() {
	return publishFor;
}
public void setPublishFor(String publishFor) {
	this.publishFor = publishFor;
}
public String getPublishStartDate() {
	return publishStartDate;
}
public void setPublishStartDate(String publishStartDate) {
	this.publishStartDate = publishStartDate;
}
public String getPublishEndDate() {
	return publishEndDate;
}
public void setPublishEndDate(String publishEndDate) {
	this.publishEndDate = publishEndDate;
}

public Map<Integer, String> getClassMap() {
	return classMap;
}

public void setClassMap(Map<Integer, String> classMap) {
	this.classMap = classMap;
}
public List<ExtraCoCurricularLeavePublishTO> getToList() {
	return toList;
}

public void setToList(List<ExtraCoCurricularLeavePublishTO> toList) {
	this.toList = toList;
}
}
