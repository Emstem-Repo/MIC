package com.kp.cms.bo.admin;

import java.util.Date;

@SuppressWarnings("serial")
public class StudentTCDetails implements java.io.Serializable{

	private int id;
	private Student student;
	private CharacterAndConduct characterAndConduct;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date lastModifiedDate;
	private Boolean isActive;
	private String passed;
	private String feePaid;
	private Date dateOfApplication;
	private Date dateOfLeaving;
	private String reasonOfLeaving;
	private String month;
	private Integer year;
	private String scholarship;
	private String eligible;
	private String subjectStudied;
	private String subjectPassed;
	private String publicExaminationName;
	private String showRegNo;
	private Boolean isFeePaidUni;
	//Added by sudhir
	private String classOfLeaving;
	private String promotionToNextClass;
	private String classsubjectOfJoining;

	private String examMonth;
	private Integer examYear;
	
	private String subjectsPassedCore;
	private String subjectsPassedComplimentary;
	private String subjectsPassedOptional;
	private String examRegNo;
	private String leavingAcademicYear;
	private String isStudentPunished;
	private String subjectFailed;
	private String dateOfLeavingNew;
	private Date dateOfAdmission;

	public StudentTCDetails() {
		super();
	}

	public StudentTCDetails(int id, Student student,
			CharacterAndConduct characterAndConduct, String createdBy,
			Date createdDate, String modifiedBy, Date lastModifiedDate,
			Boolean isActive, String passed, String feePaid,
			Date dateOfApplication, Date dateOfLeaving, String reasonOfLeaving,
			String month, Integer year, String scholarship, String eligible,
			String subjectStudied, String subjectPassed,
			String publicExaminationName, String showRegNo,
			Boolean isFeePaidUni, String classOfLeaving,
			String promotionToNextClass, String classsubjectOfJoining, Date dateOfAdmission) {
		super();
		this.id = id;
		this.student = student;
		this.characterAndConduct = characterAndConduct;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isActive = isActive;
		this.passed = passed;
		this.feePaid = feePaid;
		this.dateOfApplication = dateOfApplication;
		this.dateOfLeaving = dateOfLeaving;
		this.reasonOfLeaving = reasonOfLeaving;
		this.month = month;
		this.year = year;
		this.scholarship = scholarship;
		this.eligible = eligible;
		this.subjectStudied = subjectStudied;
		this.subjectPassed = subjectPassed;
		this.publicExaminationName = publicExaminationName;
		this.showRegNo = showRegNo;
		this.isFeePaidUni = isFeePaidUni;
		this.classOfLeaving = classOfLeaving;
		this.promotionToNextClass = promotionToNextClass;
		this.classsubjectOfJoining = classsubjectOfJoining;
		this.dateOfAdmission = dateOfAdmission;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public CharacterAndConduct getCharacterAndConduct() {
		return characterAndConduct;
	}
	public void setCharacterAndConduct(CharacterAndConduct characterAndConduct) {
		this.characterAndConduct = characterAndConduct;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getPassed() {
		return passed;
	}
	public void setPassed(String passed) {
		this.passed = passed;
	}
	public String getFeePaid() {
		return feePaid;
	}
	public void setFeePaid(String feePaid) {
		this.feePaid = feePaid;
	}
	public Date getDateOfApplication() {
		return dateOfApplication;
	}
	public void setDateOfApplication(Date dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}
	public Date getDateOfLeaving() {
		return dateOfLeaving;
	}
	public void setDateOfLeaving(Date dateOfLeaving) {
		this.dateOfLeaving = dateOfLeaving;
	}
	public String getReasonOfLeaving() {
		return reasonOfLeaving;
	}
	public void setReasonOfLeaving(String reasonOfLeaving) {
		this.reasonOfLeaving = reasonOfLeaving;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}


	public String getEligible() {
		return eligible;
	}


	public void setEligible(String eligible) {
		this.eligible = eligible;
	}


	public String getSubjectStudied() {
		return subjectStudied;
	}


	public void setSubjectStudied(String subjectStudied) {
		this.subjectStudied = subjectStudied;
	}


	public String getSubjectPassed() {
		return subjectPassed;
	}


	public void setSubjectPassed(String subjectPassed) {
		this.subjectPassed = subjectPassed;
	}


	public String getPublicExaminationName() {
		return publicExaminationName;
	}


	public void setPublicExaminationName(String publicExaminationName) {
		this.publicExaminationName = publicExaminationName;
	}


	public String getShowRegNo() {
		return showRegNo;
	}


	public void setShowRegNo(String showRegNo) {
		this.showRegNo = showRegNo;
	}


	public Boolean getIsFeePaidUni() {
		return isFeePaidUni;
	}


	public void setIsFeePaidUni(Boolean isFeePaidUni) {
		this.isFeePaidUni = isFeePaidUni;
	}


	public void setClassOfLeaving(String classOfLeaving) {
		this.classOfLeaving = classOfLeaving;
	}


	public String getClassOfLeaving() {
		return classOfLeaving;
	}

	public String getPromotionToNextClass() {
		return promotionToNextClass;
	}

	public void setPromotionToNextClass(String promotionToNextClass) {
		this.promotionToNextClass = promotionToNextClass;
	}

	public String getClasssubjectOfJoining() {
		return classsubjectOfJoining;
	}

	public void setClasssubjectOfJoining(String classsubjectOfJoining) {
		this.classsubjectOfJoining = classsubjectOfJoining;
	}








	public String getExamMonth() {
		return examMonth;
	}








	public void setExamMonth(String examMonth) {
		this.examMonth = examMonth;
	}








	public Integer getExamYear() {
		return examYear;
	}








	public void setExamYear(Integer examYear) {
		this.examYear = examYear;
	}








	public String getSubjectsPassedCore() {
		return subjectsPassedCore;
	}








	public void setSubjectsPassedCore(String subjectsPassedCore) {
		this.subjectsPassedCore = subjectsPassedCore;
	}








	public String getSubjectsPassedComplimentary() {
		return subjectsPassedComplimentary;
	}








	public void setSubjectsPassedComplimentary(String subjectsPassedComplimentary) {
		this.subjectsPassedComplimentary = subjectsPassedComplimentary;
	}








	public String getExamRegNo() {
		return examRegNo;
	}








	public void setExamRegNo(String examRegNo) {
		this.examRegNo = examRegNo;
	}
public String getLeavingAcademicYear() {
		return leavingAcademicYear;
	}
public void setLeavingAcademicYear(String leavingAcademicYear) {
		this.leavingAcademicYear = leavingAcademicYear;
	}

	public String getIsStudentPunished() {
		return isStudentPunished;
	}

	public void setIsStudentPunished(String isStudentPunished) {
		this.isStudentPunished = isStudentPunished;
	}

	public String getSubjectsPassedOptional() {
		return subjectsPassedOptional;
	}

	public void setSubjectsPassedOptional(String subjectsPassedOptional) {
		this.subjectsPassedOptional = subjectsPassedOptional;
	}

	public String getSubjectFailed() {
		return subjectFailed;
	}

	public void setSubjectFailed(String subjectFailed) {
		this.subjectFailed = subjectFailed;
	}

	/**
	 * @return the dateOfLeavingNew
	 */
	public String getDateOfLeavingNew() {
		return dateOfLeavingNew;
	}

	/**
	 * @param dateOfLeavingNew the dateOfLeavingNew to set
	 */
	public void setDateOfLeavingNew(String dateOfLeavingNew) {
		this.dateOfLeavingNew = dateOfLeavingNew;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
}
