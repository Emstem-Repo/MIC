package com.kp.cms.bo.admin;

// Generated Aug 27, 2009 2:58:28 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * InvIssue generated by hbm2java
 */
public class InvIssue implements java.io.Serializable {

	private int id;
	private InvRequest invRequest;
	private InvLocation invLocation;
	private Date issueDate;
	private String issueTo;
	private String remarks;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Set<InvIssueItem> invIssueItems = new HashSet<InvIssueItem>(0);

	public InvIssue() {
	}

	public InvIssue(int id) {
		this.id = id;
	}

	public InvIssue(int id, InvRequest invRequest, InvLocation invLocation,
			Date issueDate, String issueTo, String remarks, String createdBy,
			Date createdDate, String modifiedBy, Date lastModifiedDate,
			Boolean isActive, Set<InvIssueItem> invIssueItems) {
		this.id = id;
		this.invRequest = invRequest;
		this.invLocation = invLocation;
		this.issueDate = issueDate;
		this.issueTo = issueTo;
		this.remarks = remarks;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.invIssueItems = invIssueItems;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InvRequest getInvRequest() {
		return this.invRequest;
	}

	public void setInvRequest(InvRequest invRequest) {
		this.invRequest = invRequest;
	}

	public InvLocation getInvLocation() {
		return this.invLocation;
	}

	public void setInvLocation(InvLocation invLocation) {
		this.invLocation = invLocation;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueTo() {
		return this.issueTo;
	}

	public void setIssueTo(String issueTo) {
		this.issueTo = issueTo;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Set<InvIssueItem> getInvIssueItems() {
		return this.invIssueItems;
	}

	public void setInvIssueItems(Set<InvIssueItem> invIssueItems) {
		this.invIssueItems = invIssueItems;
	}

}
