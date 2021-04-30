package com.kp.cms.bo.admin;

// Generated Sep 25, 2009 3:08:41 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kp.cms.bo.employee.EmployeeInfoBO;

/**
 * EmpAcheivement generated by hbm2java
 */
public class EmpAcheivement implements java.io.Serializable {

	private int id;
	private Employee employee;
	private EmpOnlineResume empOnlineResume;
	private EmployeeInfoBO employeeInfoBO;
	private String acheivementName;
	private String details;
	private String status;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	
	
	public EmpAcheivement() {
	}

	public EmpAcheivement(int id) {
		this.id = id;
	}

	public EmpAcheivement(int id, String acheivementName, String details,
			String status, String createdBy, Date createdDate,Employee employee,
			String modifiedBy, Date lastModifiedDate, Boolean isActive,EmpOnlineResume empOnlineResume) {
		this.id = id;
		this.employee = employee;
		this.acheivementName = acheivementName;
		this.details = details;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.empOnlineResume=empOnlineResume;
		
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAcheivementName() {
		return this.acheivementName;
	}

	public void setAcheivementName(String acheivementName) {
		this.acheivementName = acheivementName;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setEmpOnlineResume(EmpOnlineResume empOnlineResume) {
		this.empOnlineResume = empOnlineResume;
	}

	public EmpOnlineResume getEmpOnlineResume() {
		return empOnlineResume;
	}

	public EmployeeInfoBO getEmployeeInfoBO() {
		return employeeInfoBO;
	}

	public void setEmployeeInfoBO(EmployeeInfoBO employeeInfoBO) {
		this.employeeInfoBO = employeeInfoBO;
	}

	@Override
	public boolean equals(Object obj) {
		EmpAcheivement acheivement=(EmpAcheivement)obj;
		boolean name=false;
		boolean details=false;
		if(this.acheivementName==null && acheivement.getAcheivementName()==null)
			name=true;
		else if(this.acheivementName!=null && acheivement.getAcheivementName()!=null){
			if(this.acheivementName.equalsIgnoreCase(acheivement.getAcheivementName()))
				name=true;
		}
		if(this.details==null && acheivement.getDetails()==null)
			details=true;
		else if(this.details!=null && acheivement.getDetails()!=null){
			if(this.details.equalsIgnoreCase(acheivement.getDetails()))
				details=true;
		}
		if(name && details && this.employee.getId()==acheivement.getEmployee().getId()){
			return true;
		}else
			return false;
	}	

}
