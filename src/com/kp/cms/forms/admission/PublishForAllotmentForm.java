package com.kp.cms.forms.admission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admission.PublishForAllotmentTO;

public class PublishForAllotmentForm extends BaseActionForm{
	
	private int id;
	private String[] courseIds;
	private String fromDate;
	private String endDate;
	private String allotmentNo;
	private String chanceNo;
	private List<PublishForAllotmentTO> publishForAllotmentTOs;
	
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.id= 0;
		this.courseIds=null;
		this.fromDate=null;
		this.endDate=null;
		this.allotmentNo="";
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String[] getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(String[] courseIds) {
		this.courseIds = courseIds;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<PublishForAllotmentTO> getPublishForAllotmentTOs() {
		return publishForAllotmentTOs;
	}
	public void setPublishForAllotmentTOs(List<PublishForAllotmentTO> publishForAllotmentTOs) {
		this.publishForAllotmentTOs = publishForAllotmentTOs;
	}
	public String getAllotmentNo() {
		return allotmentNo;
	}
	public void setAllotmentNo(String allotmentNo) {
		this.allotmentNo = allotmentNo;
	}


	public String getChanceNo() {
		return chanceNo;
	}


	public void setChanceNo(String chanceNo) {
		this.chanceNo = chanceNo;
	}
	
	
	

}
