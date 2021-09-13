package com.kp.cms.bo.admin;

// Generated Aug 27, 2009 2:58:28 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * InvItemCategory generated by hbm2java
 */
public class InvItemCategory implements java.io.Serializable {

	private int id;
	private String name;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Set<InvAmc> invAmcs = new HashSet<InvAmc>(0);
	private Set<InvItem> invItems = new HashSet<InvItem>(0);
	private Set<InvVendorItemCategory> invVendorItemCategories = new HashSet<InvVendorItemCategory>(
			0);

	public InvItemCategory() {
	}

	public InvItemCategory(int id) {
		this.id = id;
	}

	public InvItemCategory(int id, String name, String createdBy,
			Date createdDate, String modifiedBy, Date lastModifiedDate,
			Boolean isActive, Set<InvAmc> invAmcs, Set<InvItem> invItems,
			Set<InvVendorItemCategory> invVendorItemCategories) {
		this.id = id;
		this.name = name;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.invAmcs = invAmcs;
		this.invItems = invItems;
		this.invVendorItemCategories = invVendorItemCategories;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Set<InvAmc> getInvAmcs() {
		return this.invAmcs;
	}

	public void setInvAmcs(Set<InvAmc> invAmcs) {
		this.invAmcs = invAmcs;
	}

	public Set<InvItem> getInvItems() {
		return this.invItems;
	}

	public void setInvItems(Set<InvItem> invItems) {
		this.invItems = invItems;
	}

	public Set<InvVendorItemCategory> getInvVendorItemCategories() {
		return this.invVendorItemCategories;
	}

	public void setInvVendorItemCategories(
			Set<InvVendorItemCategory> invVendorItemCategories) {
		this.invVendorItemCategories = invVendorItemCategories;
	}

}
