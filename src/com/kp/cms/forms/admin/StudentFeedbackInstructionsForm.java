package com.kp.cms.forms.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.StudentFeedbackInstructionsTO;

public class StudentFeedbackInstructionsForm extends BaseActionForm {
	 private int id;
	 private String description; 
	 List<StudentFeedbackInstructionsTO> stuFeedbackInsToList;
	 private int stuFeedbackInsId;
	 public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<StudentFeedbackInstructionsTO> getStuFeedbackInsToList() {
		return stuFeedbackInsToList;
	}

	public void setStuFeedbackInsToList(
			List<StudentFeedbackInstructionsTO> stuFeedbackInsToList) {
		this.stuFeedbackInsToList = stuFeedbackInsToList;
	}

	public int getStuFeedbackInsId() {
		return stuFeedbackInsId;
	}

	public void setStuFeedbackInsId(int stuFeedbackInsId) {
		this.stuFeedbackInsId = stuFeedbackInsId;
	}

	public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) {
			String formName = request.getParameter(CMSConstants.FORMNAME);
			ActionErrors actionErrors = super.validate(mapping, request, formName);

			return actionErrors;
		}
		
		@Override
		public void reset(ActionMapping mapping, HttpServletRequest request) {
			super.reset(mapping, request);
			this.description = null;
		}
}
