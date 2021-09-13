package com.kp.cms.bo.exam;

/**
 * Created by 9Elelments Team
 */

import java.io.Serializable;
import java.util.Date;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Program;

@SuppressWarnings("serial")
public class ExamPublishHallTicketMarksCardBO extends ExamGenBO implements Serializable{

	private int id;
	private int examId;
	private int classId;
	private int programTypeId;
	private Integer agreementId;
	private Integer footerId;
	private String publishFor;
	private Date downloadStartDate;
	private Date downloadEndDate;

	private ExamDefinitionBO examDefinitionBO;
	private ClassUtilBO classUtilBO;
	private ExamProgramTypeUtilBO examProgramTypeUtilBO;
	private ExamFooterAgreementBO examAgreementBO;
	private ExamFooterAgreementBO examFooterBO;
	private String examCenterCode;
	private String examCenter;
	private Date revaluationEndDate;
	private Program programId;
	private Classes classes;
	
	private Date extendedFineDate;
	private Date extendedSuperFineDate;
	private String fineAmount;
	private String superFineAmount;
	private Date extendedFineStartDate;
	private Date extendedSuperFineStartDate;
	private Date extendedDate;

	public ExamPublishHallTicketMarksCardBO() {
		super();
	}

	public ExamPublishHallTicketMarksCardBO(int examId, int classId,
			int programTypeId, Integer agreementId, Integer footerId,
			String publishFor, Date downloadStartDate, Date downloadEndDate,
			String userId, String examCenterCode, String examCenter,Date revaluationEndDate) {
		super();

		this.examId = examId;
		this.classId = classId;
		this.setProgramTypeId(programTypeId);
		this.agreementId = agreementId;
		this.footerId = footerId;
		this.publishFor = publishFor;
		this.downloadStartDate = downloadStartDate;
		this.downloadEndDate = downloadEndDate;
		this.createdDate = new Date();
		this.lastModifiedDate = new Date();
		this.modifiedBy = userId;
		this.createdBy = userId;
		this.isActive = true;
		this.examCenterCode = examCenterCode;
		this.examCenter = examCenter;
		this.revaluationEndDate=revaluationEndDate;
	}

	
	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Program getProgramId() {
		return programId;
	}

	public void setProgramId(Program programId) {
		this.programId = programId;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public Integer getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	public Integer getFooterId() {
		return footerId;
	}

	public void setFooterId(Integer footerId) {
		this.footerId = footerId;
	}

	public String getPublishFor() {
		return publishFor;
	}

	public void setPublishFor(String publishFor) {
		this.publishFor = publishFor;
	}

	public Date getDownloadStartDate() {
		return downloadStartDate;
	}

	public void setDownloadStartDate(Date downloadStartDate) {
		this.downloadStartDate = downloadStartDate;
	}

	public Date getDownloadEndDate() {
		return downloadEndDate;
	}

	public void setDownloadEndDate(Date downloadEndDate) {
		this.downloadEndDate = downloadEndDate;
	}

	public ExamDefinitionBO getExamDefinitionBO() {
		return examDefinitionBO;
	}

	public void setExamDefinitionBO(ExamDefinitionBO examDefinitionBO) {
		this.examDefinitionBO = examDefinitionBO;
	}

	public ClassUtilBO getClassUtilBO() {
		return classUtilBO;
	}

	public void setClassUtilBO(ClassUtilBO classUtilBO) {
		this.classUtilBO = classUtilBO;
	}

	public void setExamAgreementBO(ExamFooterAgreementBO examAgreementBO) {
		this.examAgreementBO = examAgreementBO;
	}

	public ExamFooterAgreementBO getExamAgreementBO() {
		return examAgreementBO;
	}

	public void setExamFooterBO(ExamFooterAgreementBO examFooterBO) {
		this.examFooterBO = examFooterBO;
	}

	public ExamFooterAgreementBO getExamFooterBO() {
		return examFooterBO;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setProgramTypeId(int programTypeId) {
		this.programTypeId = programTypeId;
	}

	public int getProgramTypeId() {
		return programTypeId;
	}

	public void setExamProgramTypeUtilBO(
			ExamProgramTypeUtilBO examProgramTypeUtilBO) {
		this.examProgramTypeUtilBO = examProgramTypeUtilBO;
	}

	public ExamProgramTypeUtilBO getExamProgramTypeUtilBO() {
		return examProgramTypeUtilBO;
	}

	public String getExamCenterCode() {
		return examCenterCode;
	}

	public void setExamCenterCode(String examCenterCode) {
		this.examCenterCode = examCenterCode;
	}

	public String getExamCenter() {
		return examCenter;
	}

	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}

	public Date getRevaluationEndDate() {
		return revaluationEndDate;
	}

	public void setRevaluationEndDate(Date revaluationEndDate) {
		this.revaluationEndDate = revaluationEndDate;
	}

	/**
	 * @return the extendedFineDate
	 */
	public Date getExtendedFineDate() {
		return extendedFineDate;
	}

	/**
	 * @param extendedFineDate the extendedFineDate to set
	 */
	public void setExtendedFineDate(Date extendedFineDate) {
		this.extendedFineDate = extendedFineDate;
	}

	/**
	 * @return the extendedSuperFineDate
	 */
	public Date getExtendedSuperFineDate() {
		return extendedSuperFineDate;
	}

	/**
	 * @param extendedSuperFineDate the extendedSuperFineDate to set
	 */
	public void setExtendedSuperFineDate(Date extendedSuperFineDate) {
		this.extendedSuperFineDate = extendedSuperFineDate;
	}

	/**
	 * @return the fineAmount
	 */
	public String getFineAmount() {
		return fineAmount;
	}

	/**
	 * @param fineAmount the fineAmount to set
	 */
	public void setFineAmount(String fineAmount) {
		this.fineAmount = fineAmount;
	}

	/**
	 * @return the superFineAmount
	 */
	public String getSuperFineAmount() {
		return superFineAmount;
	}

	/**
	 * @param superFineAmount the superFineAmount to set
	 */
	public void setSuperFineAmount(String superFineAmount) {
		this.superFineAmount = superFineAmount;
	}

	/**
	 * @return the extendedFineStartDate
	 */
	public Date getExtendedFineStartDate() {
		return extendedFineStartDate;
	}

	/**
	 * @param extendedFineStartDate the extendedFineStartDate to set
	 */
	public void setExtendedFineStartDate(Date extendedFineStartDate) {
		this.extendedFineStartDate = extendedFineStartDate;
	}

	/**
	 * @return the extendedSuperFineStartDate
	 */
	public Date getExtendedSuperFineStartDate() {
		return extendedSuperFineStartDate;
	}

	/**
	 * @param extendedSuperFineStartDate the extendedSuperFineStartDate to set
	 */
	public void setExtendedSuperFineStartDate(Date extendedSuperFineStartDate) {
		this.extendedSuperFineStartDate = extendedSuperFineStartDate;
	}

	/**
	 * @return the extendedDate
	 */
	public Date getExtendedDate() {
		return extendedDate;
	}

	/**
	 * @param extendedDate the extendedDate to set
	 */
	public void setExtendedDate(Date extendedDate) {
		this.extendedDate = extendedDate;
	}
	

}