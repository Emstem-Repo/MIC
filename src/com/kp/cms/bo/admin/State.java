package com.kp.cms.bo.admin;

// Generated Aug 27, 2009 2:58:28 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * State generated by hbm2java
 */
public class State implements java.io.Serializable {

	private int id;
	private Country country;
	private String name;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private Set<Recommendor> recommendors = new HashSet<Recommendor>(0);
	private Set<City> cities = new HashSet<City>(0);
	private Set<PersonalData> personalDatasForGuardianAddressStateId = new HashSet<PersonalData>(
			0);
	private Set<EdnQualification> ednQualifications = new HashSet<EdnQualification>(
			0);
	private Set<Address> addresses = new HashSet<Address>(0);
	private Set<HlHostel> hlHostels = new HashSet<HlHostel>(0);
	private Set<PersonalData> personalDatasForParentAddressStateId = new HashSet<PersonalData>(
			0);
	private Set<InvVendor> invVendors = new HashSet<InvVendor>(0);
	private Set<Employee> employeesForPermanentAddressStateId = new HashSet<Employee>(
			0);
	private Set<PersonalData> personalDatasForPermanentAddressStateId = new HashSet<PersonalData>(
			0);
	private Set<Employee> employeesForCommunicationAddressStateId = new HashSet<Employee>(
			0);
	private Set<PersonalData> personalDatasForStateId = new HashSet<PersonalData>(
			0);
	private Set<PersonalData> personalDatasForCurrentAddressStateId = new HashSet<PersonalData>(
			0);
	private Set<EmpOnlineResume> empOnlineResumes = new HashSet<EmpOnlineResume>(
			0);
	private String bankStateId;
	private Set<District> district = new HashSet<District>(0);
	public State() {
	}

	public State(int id) {
		this.id = id;
	}

	public State(int id, Country country, String name, String createdBy,
			Date createdDate, String modifiedBy, Date lastModifiedDate,
			Boolean isActive, Set<Recommendor> recommendors,Set<City> cities,
			Set<PersonalData> personalDatasForGuardianAddressStateId,
			Set<EdnQualification> ednQualifications, Set<Address> addresses,
			Set<HlHostel> hlHostels,
			Set<PersonalData> personalDatasForParentAddressStateId,
			Set<InvVendor> invVendors,
			Set<Employee> employeesForPermanentAddressStateId,
			Set<PersonalData> personalDatasForPermanentAddressStateId,
			Set<Employee> employeesForCommunicationAddressStateId,
			Set<PersonalData> personalDatasForStateId,
			Set<PersonalData> personalDatasForCurrentAddressStateId, Set<EmpOnlineResume> empOnlineResumes) {
		this.id = id;
		this.country = country;
		this.cities = cities;
		this.name = name;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.recommendors = recommendors;
		this.personalDatasForGuardianAddressStateId = personalDatasForGuardianAddressStateId;
		this.ednQualifications = ednQualifications;
		this.addresses = addresses;
		this.hlHostels = hlHostels;
		this.personalDatasForParentAddressStateId = personalDatasForParentAddressStateId;
		this.invVendors = invVendors;
		this.employeesForPermanentAddressStateId = employeesForPermanentAddressStateId;
		this.personalDatasForPermanentAddressStateId = personalDatasForPermanentAddressStateId;
		this.employeesForCommunicationAddressStateId = employeesForCommunicationAddressStateId;
		this.personalDatasForStateId = personalDatasForStateId;
		this.personalDatasForCurrentAddressStateId = personalDatasForCurrentAddressStateId;
		this.empOnlineResumes = empOnlineResumes;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
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

	public Set<Recommendor> getRecommendors() {
		return this.recommendors;
	}

	public void setRecommendors(Set<Recommendor> recommendors) {
		this.recommendors = recommendors;
	}

	public Set<PersonalData> getPersonalDatasForGuardianAddressStateId() {
		return this.personalDatasForGuardianAddressStateId;
	}

	public void setPersonalDatasForGuardianAddressStateId(
			Set<PersonalData> personalDatasForGuardianAddressStateId) {
		this.personalDatasForGuardianAddressStateId = personalDatasForGuardianAddressStateId;
	}

	public Set<EdnQualification> getEdnQualifications() {
		return this.ednQualifications;
	}

	public void setEdnQualifications(Set<EdnQualification> ednQualifications) {
		this.ednQualifications = ednQualifications;
	}

	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<HlHostel> getHlHostels() {
		return this.hlHostels;
	}

	public void setHlHostels(Set<HlHostel> hlHostels) {
		this.hlHostels = hlHostels;
	}

	public Set<PersonalData> getPersonalDatasForParentAddressStateId() {
		return this.personalDatasForParentAddressStateId;
	}

	public void setPersonalDatasForParentAddressStateId(
			Set<PersonalData> personalDatasForParentAddressStateId) {
		this.personalDatasForParentAddressStateId = personalDatasForParentAddressStateId;
	}

	public Set<InvVendor> getInvVendors() {
		return this.invVendors;
	}

	public void setInvVendors(Set<InvVendor> invVendors) {
		this.invVendors = invVendors;
	}

	public Set<Employee> getEmployeesForPermanentAddressStateId() {
		return this.employeesForPermanentAddressStateId;
	}

	public void setEmployeesForPermanentAddressStateId(
			Set<Employee> employeesForPermanentAddressStateId) {
		this.employeesForPermanentAddressStateId = employeesForPermanentAddressStateId;
	}

	public Set<PersonalData> getPersonalDatasForPermanentAddressStateId() {
		return this.personalDatasForPermanentAddressStateId;
	}

	public void setPersonalDatasForPermanentAddressStateId(
			Set<PersonalData> personalDatasForPermanentAddressStateId) {
		this.personalDatasForPermanentAddressStateId = personalDatasForPermanentAddressStateId;
	}

	public Set<Employee> getEmployeesForCommunicationAddressStateId() {
		return this.employeesForCommunicationAddressStateId;
	}

	public void setEmployeesForCommunicationAddressStateId(
			Set<Employee> employeesForCommunicationAddressStateId) {
		this.employeesForCommunicationAddressStateId = employeesForCommunicationAddressStateId;
	}

	public Set<PersonalData> getPersonalDatasForStateId() {
		return this.personalDatasForStateId;
	}

	public void setPersonalDatasForStateId(
			Set<PersonalData> personalDatasForStateId) {
		this.personalDatasForStateId = personalDatasForStateId;
	}

	public Set<PersonalData> getPersonalDatasForCurrentAddressStateId() {
		return this.personalDatasForCurrentAddressStateId;
	}

	public void setPersonalDatasForCurrentAddressStateId(
			Set<PersonalData> personalDatasForCurrentAddressStateId) {
		this.personalDatasForCurrentAddressStateId = personalDatasForCurrentAddressStateId;
	}

	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}

	public Set<EmpOnlineResume> getEmpOnlineResumes() {
		return empOnlineResumes;
	}

	public void setEmpOnlineResumes(Set<EmpOnlineResume> empOnlineResumes) {
		this.empOnlineResumes = empOnlineResumes;
	}

	public String getBankStateId() {
		return bankStateId;
	}

	public void setBankStateId(String bankStateId) {
		this.bankStateId = bankStateId;
	}

	public Set<District> getDistrict() {
		return district;
	}

	public void setDistrict(Set<District> district) {
		this.district = district;
	}

}