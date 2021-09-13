package com.kp.cms.to.exam;

public class ConsolidatedSubjectSectionTO 
{
	private int id;
	private String sectionName;
	private String sectionOrder;
	private boolean showRespectiveStreams;
	
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
	public String getSectionOrder() {
		return sectionOrder;
	}
	public void setSectionOrder(String sectionOrder) {
		this.sectionOrder = sectionOrder;
	}
	public boolean isShowRespectiveStreams() {
		return showRespectiveStreams;
	}
	public void setShowRespectiveStreams(boolean showRespectiveStreams) {
		this.showRespectiveStreams = showRespectiveStreams;
	}
}
