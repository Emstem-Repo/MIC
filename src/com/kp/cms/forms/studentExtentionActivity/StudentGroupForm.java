package com.kp.cms.forms.studentExtentionActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;

public class StudentGroupForm extends BaseActionForm{
    private int id;
	private String groupName;
	private String orgGroup;
	private int dupId;
   List<StudentGroupTO> studentGroup;
   
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


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(String orgGroup) {
		this.orgGroup = orgGroup;
	}

	public int getDupId() {
		return dupId;
	}

	public void setDupId(int dupId) {
		this.dupId = dupId;
	}

	public List<StudentGroupTO> getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(List<StudentGroupTO> studentGroup) {
		this.studentGroup = studentGroup;
	}
	
}
