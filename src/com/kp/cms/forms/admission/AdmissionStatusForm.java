package com.kp.cms.forms.admission;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.to.admission.AdmissionStatusTO;



public class AdmissionStatusForm extends AdmissionFormForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String method;
	private String applicationNo;
	private String dateOfBirth;
	private int appliedYear;
	private AdmissionStatusTO admissionStatusTO;
	private AdmissionStatusTO statusTO;
	private String templateDescription;
	private boolean displaySemister;
	private String admStatus;
	private boolean onlineAcknowledgement;
	private String serverDownMessage;
	//vibin for status change
	private boolean memo;
	private int maxallotment;
	private int isChallanRecieved;
	private List<AdmissionStatusTO> statusTOs;
	private int selectedCourseId;
	private String selectedValue;
	private String formlink;
	private boolean isOnceAccept;
	private boolean isChecked;
	private String totalAmt;
	private String uploadId;
	private String uploadDetail;
	private Boolean isUploadDocument;
	private String paymentMail;
	private String uniqueId;
	private boolean isPaid;
	private boolean payonline;
	private Map<Integer, String> deptMap;
	private Map<Integer, String> categoryMap;
	private String deptId;
	private String categoryId;

	public String getServerDownMessage() {
		return serverDownMessage;
	}

	public void setServerDownMessage(String serverDownMessage) {
		this.serverDownMessage = serverDownMessage;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public AdmissionStatusTO getStatusTO() {
		return statusTO;
	}

	public void setStatusTO(AdmissionStatusTO statusTO) {
		this.statusTO = statusTO;
	}

	public AdmissionStatusTO getAdmissionStatusTO() {
		return admissionStatusTO;
	}

	public void setAdmissionStatusTO(AdmissionStatusTO admissionStatusTO) {
		this.admissionStatusTO = admissionStatusTO;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getAppliedYear() {
		return appliedYear;
	}

	public void setAppliedYear(int appliedYear) {
		this.appliedYear = appliedYear;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter(CMSConstants.FORMNAME);
		ActionErrors actionErrors = super.validate(mapping, request, formName);

		return actionErrors;
	}
	public void clear()
	{
		this.applicationNo=null;
		this.dateOfBirth=null;
		this.templateDescription = "";
		this.displaySemister=false;
		this.serverDownMessage=null;
	}
	public boolean getDisplaySemister() {
		return displaySemister;
	}

	public void setDisplaySemister(boolean displaySemister) {
		this.displaySemister = displaySemister;
	}

	public void clearadmissionStatusTO(){
		this.admissionStatusTO=null;
	}
	public void clearstatusTO(){
		this.statusTO=null;
	}

	public String getAdmStatus() {
		return admStatus;
	}

	public void setAdmStatus(String admStatus) {
		this.admStatus = admStatus;
	}

	public boolean isOnlineAcknowledgement() {
		return onlineAcknowledgement;
	}

	public void setOnlineAcknowledgement(boolean onlineAcknowledgement) {
		this.onlineAcknowledgement = onlineAcknowledgement;
	}

	public void setMemo(boolean memo) {
		this.memo = memo;
	}

	public boolean isMemo() {
		return memo;
	}

	public void setMaxallotment(int maxallotment) {
		this.maxallotment = maxallotment;
	}

	public int getMaxallotment() {
		return maxallotment;
	}

	public void setIsChallanRecieved(int isChallanRecieved) {
		this.isChallanRecieved = isChallanRecieved;
	}

	public int getIsChallanRecieved() {
		return isChallanRecieved;
	}

	public List<AdmissionStatusTO> getStatusTOs() {
		return statusTOs;
	}

	public void setStatusTOs(List<AdmissionStatusTO> statusTOs) {
		this.statusTOs = statusTOs;
	}

	public int getSelectedCourseId() {
		return selectedCourseId;
	}

	public void setSelectedCourseId(int selectedCourseId) {
		this.selectedCourseId = selectedCourseId;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getFormlink() {
		return formlink;
	}

	public void setFormlink(String formlink) {
		this.formlink = formlink;
	}

	public Boolean getIsOnceAccept() {
		return isOnceAccept;
	}

	public void setIsOnceAccept(Boolean isOnceAccept) {
		this.isOnceAccept = isOnceAccept;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getUploadDetail() {
		return uploadDetail;
	}

	public void setUploadDetail(String uploadDetail) {
		this.uploadDetail = uploadDetail;
	}

	public Boolean getIsUploadDocument() {
		return isUploadDocument;
	}

	public void setIsUploadDocument(Boolean isUploadDocument) {
		this.isUploadDocument = isUploadDocument;
	}

	public String getPaymentMail() {
		return paymentMail;
	}

	public void setPaymentMail(String paymentMail) {
		this.paymentMail = paymentMail;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Boolean getPayonline() {
		return payonline;
	}

	public void setPayonline(Boolean payonline) {
		this.payonline = payonline;
	}

	public Map<Integer, String> getDeptMap() {
		return deptMap;
	}

	public void setDeptMap(Map<Integer, String> deptMap) {
		this.deptMap = deptMap;
	}

	public Map<Integer, String> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(Map<Integer, String> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

		
	
	
	
}
