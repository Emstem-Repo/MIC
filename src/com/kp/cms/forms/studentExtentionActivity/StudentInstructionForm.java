package com.kp.cms.forms.studentExtentionActivity;

import java.util.List;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;

public class StudentInstructionForm extends BaseActionForm{

	private int id;
	private String description;
	private List<StudentInstructionTO>  stuFeedbackInsToList;
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
	public List<StudentInstructionTO> getStuFeedbackInsToList() {
		return stuFeedbackInsToList;
	}
	public void setStuFeedbackInsToList(
			List<StudentInstructionTO> stuFeedbackInsToList) {
		this.stuFeedbackInsToList = stuFeedbackInsToList;
	}
	
	
}
