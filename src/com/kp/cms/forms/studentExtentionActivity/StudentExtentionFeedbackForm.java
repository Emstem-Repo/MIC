package com.kp.cms.forms.studentExtentionActivity;

import java.util.List;
import java.util.Map;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;

public class StudentExtentionFeedbackForm extends BaseActionForm{
     private int id;
	  private String academicYear;
	  private boolean flag;
	  private String startDate;
	  private String endDate;
     List<StudentExtentionFeedbackTO> feedbackConnectionTo;
     Map<Integer, String> feedbackToList;
     private String[] classesId;
     private List<StudentGroupTO> subGrouplist;
     private String studentGroupId;
     
     
     public String getStudentGroupId() {
		return studentGroupId;
	}

	public void setStudentGroupId(String studentGroupId) {
		this.studentGroupId = studentGroupId;
	}

	private List<StudentExtentionTO> exList;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<StudentExtentionFeedbackTO> getFeedbackConnectionTo() {
		return feedbackConnectionTo;
	}

	public void setFeedbackConnectionTo(
			List<StudentExtentionFeedbackTO> feedbackConnectionTo) {
		this.feedbackConnectionTo = feedbackConnectionTo;
	}

	public Map<Integer, String> getFeedbackToList() {
		return feedbackToList;
	}

	public void setFeedbackToList(Map<Integer, String> feedbackToList) {
		this.feedbackToList = feedbackToList;
	}

	public String[] getClassesId() {
		return classesId;
	}

	public void setClassesId(String[] classesId) {
		this.classesId = classesId;
	}

	
	public List<StudentGroupTO> getSubGrouplist() {
		return subGrouplist;
	}

	public void setSubGrouplist(List<StudentGroupTO> subGrouplist) {
		this.subGrouplist = subGrouplist;
	}

	public List<StudentExtentionTO> getExList() {
		return exList;
	}

	public void setExList(List<StudentExtentionTO> exList) {
		this.exList = exList;
	}
   
	
   
}
