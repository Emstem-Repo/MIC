package com.kp.cms.forms.studentExtentionActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;

@SuppressWarnings("serial")
public class StudentExtentionForm extends BaseActionForm {
    private int id;
	private String activityName;
	
	private String origActivityName;
	private int dupId;
	
	private List<StudentExtentionTO> subjectActivity;
	
	private int displayOrder;
	private int studentGroupId;
	
	private List<StudentGroupTO> list; 
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		String formName = request.getParameter(CMSConstants.FORMNAME);
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	public String getOrigActivityName() {
		return origActivityName;
	}

	public void setOrigActivityName(String origActivityName) {
		this.origActivityName = origActivityName;
	}

	public int getDupId() {
		return dupId;
	}

	public void setDupId(int dupId) {
		this.dupId = dupId;
	}

	public List<StudentExtentionTO> getSubjectActivity() {
		return subjectActivity;
	}
	public void setSubjectActivity(List<StudentExtentionTO> subjectActivity) {
		this.subjectActivity = subjectActivity;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public List<StudentGroupTO> getList() {
		return list;
	}

	public void setList(List<StudentGroupTO> list) {
		this.list = list;
	}

	public int getStudentGroupId() {
		return studentGroupId;
	}

	public void setStudentGroupId(int studentGroupId) {
		this.studentGroupId = studentGroupId;
	}


	

	
}
