package com.kp.cms.forms.exam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.exam.ConsolidatedSubjectSectionTO;

@SuppressWarnings("serial")
public class ConsolidatedSubjectSectionForm  extends BaseActionForm
{
	private int id;
	private String sectionName;
	private String origSectionName;
	private String sectionOrder;
	private int dupId;
	private boolean showRespectiveStreams;
	private List<ConsolidatedSubjectSectionTO> subjectSections;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		String formName = request.getParameter(CMSConstants.FORMNAME);
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}
	
	public void reset()
	{
		this.sectionName = null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getOrigSectionName() {
		return origSectionName;
	}
	public void setOrigSectionName(String origSectionName) {
		this.origSectionName = origSectionName;
	}
	public String getSectionOrder() {
		return sectionOrder;
	}
	public void setSectionOrder(String sectionOrder) {
		this.sectionOrder = sectionOrder;
	}
	public int getDupId() {
		return dupId;
	}
	public void setDupId(int dupId) {
		this.dupId = dupId;
	}
	public List<ConsolidatedSubjectSectionTO> getSubjectSections() {
		return subjectSections;
	}
	public void setSubjectSections(
			List<ConsolidatedSubjectSectionTO> subjectSections) {
		this.subjectSections = subjectSections;
	}
	public boolean isShowRespectiveStreams() {
		return showRespectiveStreams;
	}
	public void setShowRespectiveStreams(boolean showRespectiveStreams) {
		this.showRespectiveStreams = showRespectiveStreams;
	}
}
