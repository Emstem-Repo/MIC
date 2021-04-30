package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Income generated by hbm2java
 */
public class Income implements java.io.Serializable {

	private int id;
	private Currency currency;
	private String createdBy;;
	private String modifiedBy;
	private String incomeRange;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Set<PersonalData> personalDatasForMotherIncomeId = new HashSet<PersonalData>(
			0);
	private Set<PersonalData> personalDatasForIncomeId = new HashSet<PersonalData>(
			0);
	private Set<PersonalData> personalDatasForFatherIncomeId = new HashSet<PersonalData>(
			0);

	public Income() {
	}

	public Income(int id) {
		this.id = id;
	}

	public Income(int id, Currency currency, String createdBy,
			String modifiedBy, String incomeRange,
			Date createdDate, Date lastModifiedDate,
			Set<PersonalData> personalDatasForMotherIncomeId, Boolean isActive,
			Set<PersonalData> personalDatasForIncomeId,
			Set<PersonalData> personalDatasForFatherIncomeId) {
		this.id = id;
		this.currency = currency;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.incomeRange = incomeRange;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.personalDatasForMotherIncomeId = personalDatasForMotherIncomeId;
		this.personalDatasForIncomeId = personalDatasForIncomeId;
		this.personalDatasForFatherIncomeId = personalDatasForFatherIncomeId;
		this.isActive = isActive;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)  {
		this.createdBy = createdBy;
	}

	public String getModifiedBy()  {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getIncomeRange() {
		return this.incomeRange;
	}

	public void setIncomeRange(String incomeRange) {
		this.incomeRange = incomeRange;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Set<PersonalData> getPersonalDatasForMotherIncomeId() {
		return this.personalDatasForMotherIncomeId;
	}

	public void setPersonalDatasForMotherIncomeId(
			Set<PersonalData> personalDatasForMotherIncomeId) {
		this.personalDatasForMotherIncomeId = personalDatasForMotherIncomeId;
	}

	public Set<PersonalData> getPersonalDatasForIncomeId() {
		return this.personalDatasForIncomeId;
	}

	public void setPersonalDatasForIncomeId(
			Set<PersonalData> personalDatasForIncomeId) {
		this.personalDatasForIncomeId = personalDatasForIncomeId;
	}

	public Set<PersonalData> getPersonalDatasForFatherIncomeId() {
		return this.personalDatasForFatherIncomeId;
	}

	public void setPersonalDatasForFatherIncomeId(
			Set<PersonalData> personalDatasForFatherIncomeId) {
		this.personalDatasForFatherIncomeId = personalDatasForFatherIncomeId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
