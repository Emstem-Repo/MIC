package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * City generated by hbm2java
 */
public class City implements java.io.Serializable {

	private int id;
	private String createdBy;;
	private State state;
	private String modifiedBy;
	private String name;
	private Date createdDate;
	private Date lastModifiedDate;
	private Set<PersonalData> personalDatasForCurrentAddressCityId = new HashSet<PersonalData>(
			0);
	private Set<PersonalData> personalDatasForParentAddressCityId = new HashSet<PersonalData>(
			0);
	private Set<Recommendor> recommendors = new HashSet<Recommendor>(0);
	private Set<Address> addresses = new HashSet<Address>(0);
	private Set<PersonalData> personalDatasForPermanentAddressCityId = new HashSet<PersonalData>(
			0);

	public City() {
	}

	public City(int id) {
		this.id = id;
	}

	public City(int id, String createdBy, State state,
			String modifiedBy, String name, Date createdDate,
			Date lastModifiedDate,
			Set<PersonalData> personalDatasForCurrentAddressCityId,
			Set<PersonalData> personalDatasForParentAddressCityId,
			Set<Recommendor> recommendors, Set<Address> addresses,
			Set<PersonalData> personalDatasForPermanentAddressCityId) {
		this.id = id;
		this.createdBy = createdBy;
		this.state = state;
		this.modifiedBy = modifiedBy;
		this.name = name;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.personalDatasForCurrentAddressCityId = personalDatasForCurrentAddressCityId;
		this.personalDatasForParentAddressCityId = personalDatasForParentAddressCityId;
		this.recommendors = recommendors;
		this.addresses = addresses;
		this.personalDatasForPermanentAddressCityId = personalDatasForPermanentAddressCityId;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)  {
		this.createdBy = createdBy;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getModifiedBy()  {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<PersonalData> getPersonalDatasForCurrentAddressCityId() {
		return this.personalDatasForCurrentAddressCityId;
	}

	public void setPersonalDatasForCurrentAddressCityId(
			Set<PersonalData> personalDatasForCurrentAddressCityId) {
		this.personalDatasForCurrentAddressCityId = personalDatasForCurrentAddressCityId;
	}

	public Set<PersonalData> getPersonalDatasForParentAddressCityId() {
		return this.personalDatasForParentAddressCityId;
	}

	public void setPersonalDatasForParentAddressCityId(
			Set<PersonalData> personalDatasForParentAddressCityId) {
		this.personalDatasForParentAddressCityId = personalDatasForParentAddressCityId;
	}

	public Set<Recommendor> getRecommendors() {
		return this.recommendors;
	}

	public void setRecommendors(Set<Recommendor> recommendors) {
		this.recommendors = recommendors;
	}

	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<PersonalData> getPersonalDatasForPermanentAddressCityId() {
		return this.personalDatasForPermanentAddressCityId;
	}

	public void setPersonalDatasForPermanentAddressCityId(
			Set<PersonalData> personalDatasForPermanentAddressCityId) {
		this.personalDatasForPermanentAddressCityId = personalDatasForPermanentAddressCityId;
	}

}
