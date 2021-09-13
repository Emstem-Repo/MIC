package com.kp.cms.bo.admin;

// Generated Mar 26, 2009 2:37:52 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * InterviewSubRounds generated by hbm2java
 */
public class InterviewSubRounds implements java.io.Serializable {

	private int id;
	private InterviewProgramCourse interviewProgramCourse;
	private String name;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Integer noOfInterviewsPerPanel;
	private Weightage weightage;
	private BigDecimal weightagePercentage;
	private Set<InterviewResultDetail> interviewResultDetails = new HashSet<InterviewResultDetail>(
			0);
	private Set<InterviewResult> interviewResults = new HashSet<InterviewResult>(
			0);

	public InterviewSubRounds() {
	}

	public InterviewSubRounds(int id) {
		this.id = id;
	}

	public InterviewSubRounds(int id,
			InterviewProgramCourse interviewProgramCourse, String name,
			String createdBy, Date createdDate, String modifiedBy,
			Date lastModifiedDate, Boolean isActive,Integer noOfInterviewsPerPanel,
			Set<InterviewResultDetail> interviewResultDetails,
			Set<InterviewResult> interviewResults, Weightage weightage, BigDecimal weightagePercentage) {
		this.id = id;
		this.interviewProgramCourse = interviewProgramCourse;
		this.name = name;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.interviewResultDetails = interviewResultDetails;
		this.interviewResults = interviewResults;
		this.weightage = weightage;
		this.weightagePercentage = weightagePercentage;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InterviewProgramCourse getInterviewProgramCourse() {
		return this.interviewProgramCourse;
	}

	public void setInterviewProgramCourse(
			InterviewProgramCourse interviewProgramCourse) {
		this.interviewProgramCourse = interviewProgramCourse;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<InterviewResultDetail> getInterviewResultDetails() {
		return this.interviewResultDetails;
	}

	public void setInterviewResultDetails(
			Set<InterviewResultDetail> interviewResultDetails) {
		this.interviewResultDetails = interviewResultDetails;
	}

	public Set<InterviewResult> getInterviewResults() {
		return this.interviewResults;
	}

	public void setInterviewResults(Set<InterviewResult> interviewResults) {
		this.interviewResults = interviewResults;
	}

	public Integer getNoOfInterviewsPerPanel() {
		return noOfInterviewsPerPanel;
	}

	public void setNoOfInterviewsPerPanel(Integer noOfInterviewsPerPanel) {
		this.noOfInterviewsPerPanel = noOfInterviewsPerPanel;
	}

	public Weightage getWeightage() {
		return weightage;
	}

	public void setWeightage(Weightage weightage) {
		this.weightage = weightage;
	}

	public BigDecimal getWeightagePercentage() {
		return weightagePercentage;
	}

	public void setWeightagePercentage(BigDecimal weightagePercentage) {
		this.weightagePercentage = weightagePercentage;
	}

}
