package com.kp.cms.to.studentExtentionActivity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StudentExtentionTO implements Serializable {
	private int id;
	private String activityName;
	private int displayOrder;
	private String groupName;
	private int studentGroupId;
	private Boolean checked;
	private String preference;
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
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getStudentGroupId() {
		return studentGroupId;
	}
	public void setStudentGroupId(int studentGroupId) {
		this.studentGroupId = studentGroupId;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
}
