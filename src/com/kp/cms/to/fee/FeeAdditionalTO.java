package com.kp.cms.to.fee;


// Generated Feb 23, 2009 6:14:19 PM by Hibernate Tools 3.2.0.b9

/**
 * FeeAdditional generated by hbm2java
 */
public class FeeAdditionalTO implements java.io.Serializable {

	private int id;
	private FeeGroupTO feeGroupTO;
	private String isActive;
	private String createdBy;
	private String modifiedBy;
	private String createdDate;
	private String lastModifiedDate;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the feeGroupTO
	 */
	public FeeGroupTO getFeeGroupTO() {
		return feeGroupTO;
	}
	/**
	 * @param feeGroupTO the feeGroupTO to set
	 */
	public void setFeeGroupTO(FeeGroupTO feeGroupTO) {
		this.feeGroupTO = feeGroupTO;
	}
	public String getIsActive() {
		return isActive;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


}
