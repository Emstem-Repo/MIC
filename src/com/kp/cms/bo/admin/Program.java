package com.kp.cms.bo.admin;

// Generated Jan 22, 2009 3:35:47 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Program generated by hbm2java
 */
public class Program implements java.io.Serializable {

	private int id;
	private ProgramType programType;
	private String createdBy;;
	private String modifiedBy;
	private String code;
	private String name;
	private Boolean isActive;
	private Date createdDate;
	private Date lastModifiedDate;
	private Boolean isMotherTongue;
	private Boolean isSecondLanguage;
	private Boolean isDisplayLanguageKnown;
	private Boolean isFamilyBackground;
	private Boolean isHeightWeight;
	private Boolean isEntranceDetails;
	private Boolean isLateralDetails;
	private Boolean isDisplayTrainingCourse;
	private Boolean isTransferCourse;
	private Boolean isAdditionalInfo;
	private Boolean isExtraDetails;
	private Boolean isTCDetails;
	private Boolean isRegistrationNo;
	private Boolean isOpen;
	private Integer academicYear;
	private Boolean isExamCenterRequired;
	private String stream;
	
	private Set<DocChecklist> docChecklists = new HashSet<DocChecklist>(0);
	private Set<ApplnType> applnTypes = new HashSet<ApplnType>(0);
	private Set<Course> courses = new HashSet<Course>(0);
	private Set<Entrance> entrances = new HashSet<Entrance>(0);
	private Set<SelectionProcess> selectionProcesses = new HashSet<SelectionProcess>(
			0);
	private Set<Weightage> weightages = new HashSet<Weightage>(0);
	private Set<RegistrationNumberFormat> registrationNumberFormats = new HashSet<RegistrationNumberFormat>(
			0);
	
	private Set<Fee> fees = new HashSet<Fee>(0);
	private Set<ProgramCourseIntake> programCourseIntakes = new HashSet<ProgramCourseIntake>(
			0);
	private Set<InterviewProgramCourse> interviewProgramCourses = new HashSet<InterviewProgramCourse>(
			0);
	private Set<ProgCourseDoc> progCourseDocs = new HashSet<ProgCourseDoc>(0);
	private Set<AttendanceMarks> attendanceMarkses = new HashSet<AttendanceMarks>(
			0);
	private Set<FeeConcession> feeConcessions = new HashSet<FeeConcession>(0);
	private Set<GroupTemplate> groupTemplates = new HashSet<GroupTemplate>(0);
	private Set<ExamCenter> examCenters = new HashSet<ExamCenter>(0);
	private String programNameCertificate;
	public Program() {
	}

	public Program(int id, ProgramType programType) {
		this.id = id;
		this.programType = programType;
	}

	public Program(int id, ProgramType programType,
			String createdBy, String modifiedBy,
			String code, String name, Boolean isActive, Date createdDate,
			Boolean isMotherTongue,
			Boolean isSecondLanguage,
			Boolean isDisplayLanguageKnown,
			Boolean isFamilyBackground,
			Boolean isHeightWeight,
			Boolean isEntranceDetails,
			Boolean isLateralDetails,
			Boolean isDisplayTrainingCourse,
			Boolean isTransferCourse,
			Boolean isAdditionalInfo,
			Boolean isExtraDetails,
			Boolean isTCDetails,Boolean isOpen,
			Boolean isRegistrationNo,
			Date lastModifiedDate, Set<DocChecklist> docChecklists,
			Set<ApplnType> applnTypes, Set<Course> courses,
			Set<Entrance> entrances,
			Set<SelectionProcess> selectionProcesses,
			Set<Weightage> weightages,
			Set<RegistrationNumberFormat> registrationNumberFormats,
			Set<Fee> fees,
			Set<ProgramCourseIntake> programCourseIntakes,
			Set<InterviewProgramCourse> interviewProgramCourses,
			Set<ProgCourseDoc> progCourseDocs,
			Set<AttendanceMarks> attendanceMarkses,
			Set<FeeConcession> feeConcessions,Set<GroupTemplate> groupTemplates, Integer academicYear,
			Set<ExamCenter> examCenters, String stream,String programNameCertificate) {
		this.id = id;
		this.programType = programType;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.code = code;
		this.name = name;
		this.isActive = isActive;
		this.createdDate = (Date)createdDate.clone();
		this.lastModifiedDate = (Date)lastModifiedDate.clone();
		this.docChecklists = docChecklists;
		this.applnTypes = applnTypes;
		this.courses = courses;
		this.entrances = entrances;
		this.selectionProcesses = selectionProcesses;
		this.weightages = weightages;
		this.registrationNumberFormats = registrationNumberFormats;
		this.isOpen = isOpen;
		this.fees = fees;
		this.programCourseIntakes = programCourseIntakes;
		this.interviewProgramCourses = interviewProgramCourses;
		this.progCourseDocs = progCourseDocs;
		this.attendanceMarkses = attendanceMarkses;
		this.feeConcessions = feeConcessions;
		this.isMotherTongue = isMotherTongue;
		this.isSecondLanguage = isSecondLanguage;
		this.isDisplayLanguageKnown = isDisplayLanguageKnown;
		this.isFamilyBackground = isFamilyBackground;
		this.isHeightWeight = isHeightWeight;
		this.isEntranceDetails = isEntranceDetails;
		this.isLateralDetails = isLateralDetails;
		this.isDisplayTrainingCourse = isDisplayTrainingCourse;
		this.isTransferCourse = isTransferCourse;
		this.isAdditionalInfo = isAdditionalInfo;
		this.isExtraDetails = isExtraDetails;
		this.isTCDetails=isTCDetails;
		this.isRegistrationNo=isRegistrationNo;
		this.groupTemplates = groupTemplates;
		this.academicYear = academicYear;
		this.examCenters = examCenters;
		this.stream = stream;
		this.setProgramNameCertificate(programNameCertificate);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProgramType getProgramType() {
		return this.programType;
	}

	public void setProgramType(ProgramType programType) {
		this.programType = programType;
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	/**
	 * @return the isMotherTongue
	 */
	public Boolean getIsMotherTongue() {
		return isMotherTongue;
	}

	/**
	 * @param isMotherTongue the isMotherTongue to set
	 */
	public void setIsMotherTongue(Boolean isMotherTongue) {
		this.isMotherTongue = isMotherTongue;
	}

	/**
	 * @return the isSecondLanguage
	 */
	public Boolean getIsSecondLanguage() {
		return isSecondLanguage;
	}

	/**
	 * @param isSecondLanguage the isSecondLanguage to set
	 */
	public void setIsSecondLanguage(Boolean isSecondLanguage) {
		this.isSecondLanguage = isSecondLanguage;
	}

	/**
	 * @return the isDisplayLanguageKnown
	 */
	public Boolean getIsDisplayLanguageKnown() {
		return isDisplayLanguageKnown;
	}

	/**
	 * @param isDisplayLanguageKnown the isDisplayLanguageKnown to set
	 */
	public void setIsDisplayLanguageKnown(Boolean isDisplayLanguageKnown) {
		this.isDisplayLanguageKnown = isDisplayLanguageKnown;
	}

	/**
	 * @return the isFamilyBackground
	 */
	public Boolean getIsFamilyBackground() {
		return isFamilyBackground;
	}

	/**
	 * @param isFamilyBackground the isFamilyBackground to set
	 */
	public void setIsFamilyBackground(Boolean isFamilyBackground) {
		this.isFamilyBackground = isFamilyBackground;
	}

	/**
	 * @return the isHeightWeight
	 */
	public Boolean getIsHeightWeight() {
		return isHeightWeight;
	}

	/**
	 * @param isHeightWeight the isHeightWeight to set
	 */
	public void setIsHeightWeight(Boolean isHeightWeight) {
		this.isHeightWeight = isHeightWeight;
	}

	/**
	 * @return the isEntranceDetails
	 */
	public Boolean getIsEntranceDetails() {
		return isEntranceDetails;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * @param isEntranceDetails the isEntranceDetails to set
	 */
	public void setIsEntranceDetails(Boolean isEntranceDetails) {
		this.isEntranceDetails = isEntranceDetails;
	}

	/**
	 * @return the isLateralDetails
	 */
	public Boolean getIsLateralDetails() {
		return isLateralDetails;
	}

	/**
	 * @param isLateralDetails the isLateralDetails to set
	 */
	public void setIsLateralDetails(Boolean isLateralDetails) {
		this.isLateralDetails = isLateralDetails;
	}

	/**
	 * @return the isDisplayTrainingCourse
	 */
	public Boolean getIsDisplayTrainingCourse() {
		return isDisplayTrainingCourse;
	}

	/**
	 * @param isDisplayTrainingCourse the isDisplayTrainingCourse to set
	 */
	public void setIsDisplayTrainingCourse(Boolean isDisplayTrainingCourse) {
		this.isDisplayTrainingCourse = isDisplayTrainingCourse;
	}

	/**
	 * @return the isTransferCourse
	 */
	public Boolean getIsTransferCourse() {
		return isTransferCourse;
	}

	/**
	 * @param isTransferCourse the isTransferCourse to set
	 */
	public void setIsTransferCourse(Boolean isTransferCourse) {
		this.isTransferCourse = isTransferCourse;
	}

	/**
	 * @return the isAdditionalInfo
	 */
	public Boolean getIsAdditionalInfo() {
		return isAdditionalInfo;
	}

	/**
	 * @param isAdditionalInfo the isAdditionalInfo to set
	 */
	public void setIsAdditionalInfo(Boolean isAdditionalInfo) {
		this.isAdditionalInfo = isAdditionalInfo;
	}

	/**
	 * @return the isExtraDetails
	 */
	public Boolean getIsExtraDetails() {
		return isExtraDetails;
	}

	/**
	 * @param isExtraDetails the isExtraDetails to set
	 */
	public void setIsExtraDetails(Boolean isExtraDetails) {
		this.isExtraDetails = isExtraDetails;
	}

	public Set<DocChecklist> getDocChecklists() {
		return this.docChecklists;
	}

	public void setDocChecklists(Set<DocChecklist> docChecklists) {
		this.docChecklists = docChecklists;
	}

	public Set<ApplnType> getApplnTypes() {
		return this.applnTypes;
	}

	public void setApplnTypes(Set<ApplnType> applnTypes) {
		this.applnTypes = applnTypes;
	}

	public Set<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	

	public Set<SelectionProcess> getSelectionProcesses() {
		return this.selectionProcesses;
	}

	public void setSelectionProcesses(Set<SelectionProcess> selectionProcesses) {
		this.selectionProcesses = selectionProcesses;
	}

	public Set<Weightage> getWeightages() {
		return this.weightages;
	}

	public void setWeightages(Set<Weightage> weightages) {
		this.weightages = weightages;
	}

	public Set<RegistrationNumberFormat> getRegistrationNumberFormats() {
		return this.registrationNumberFormats;
	}

	public void setRegistrationNumberFormats(
			Set<RegistrationNumberFormat> registrationNumberFormats) {
		this.registrationNumberFormats = registrationNumberFormats;
	}

	

	public Set<Fee> getFees() {
		return this.fees;
	}

	public void setFees(Set<Fee> fees) {
		this.fees = fees;
	}

	public Set<ProgramCourseIntake> getProgramCourseIntakes() {
		return this.programCourseIntakes;
	}

	public void setProgramCourseIntakes(
			Set<ProgramCourseIntake> programCourseIntakes) {
		this.programCourseIntakes = programCourseIntakes;
	}

	public Set<InterviewProgramCourse> getInterviewProgramCourses() {
		return this.interviewProgramCourses;
	}

	public void setInterviewProgramCourses(
			Set<InterviewProgramCourse> interviewProgramCourses) {
		this.interviewProgramCourses = interviewProgramCourses;
	}

	public Set<ProgCourseDoc> getProgCourseDocs() {
		return this.progCourseDocs;
	}

	public void setProgCourseDocs(Set<ProgCourseDoc> progCourseDocs) {
		this.progCourseDocs = progCourseDocs;
	}

	public Set<AttendanceMarks> getAttendanceMarkses() {
		return this.attendanceMarkses;
	}

	public void setAttendanceMarkses(Set<AttendanceMarks> attendanceMarkses) {
		this.attendanceMarkses = attendanceMarkses;
	}

	public Set<FeeConcession> getFeeConcessions() {
		return this.feeConcessions;
	}

	public void setFeeConcessions(Set<FeeConcession> feeConcessions) {
		this.feeConcessions = feeConcessions;
	}

	public Boolean getIsTCDetails() {
		return isTCDetails;
	}

	public void setIsTCDetails(Boolean isTCDetails) {
		this.isTCDetails = isTCDetails;
	}
	public Boolean getIsRegistrationNo() {
		return isRegistrationNo;
	}

	public void setIsRegistrationNo(Boolean isRegistrationNo) {
		this.isRegistrationNo = isRegistrationNo;
	}

	public Set<GroupTemplate> getGroupTemplates() {
		return groupTemplates;
	}

	public void setGroupTemplates(Set<GroupTemplate> groupTemplates) {
		this.groupTemplates = groupTemplates;
	}

	public Set<Entrance> getEntrances() {
		return entrances;
	}

	public void setEntrances(Set<Entrance> entrances) {
		this.entrances = entrances;
	}
	public Integer getAcademicYear() {
		return this.academicYear;
	}

	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}

	public Set<ExamCenter> getExamCenters() {
		return examCenters;
	}

	public void setExamCenters(Set<ExamCenter> examCenters) {
		this.examCenters = examCenters;
	}

	public Boolean getIsExamCenterRequired() {
		return isExamCenterRequired;
	}

	public void setIsExamCenterRequired(Boolean isExamCenterRequired) {
		this.isExamCenterRequired = isExamCenterRequired;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public void setProgramNameCertificate(String programNameCertificate) {
		this.programNameCertificate = programNameCertificate;
	}

	public String getProgramNameCertificate() {
		return programNameCertificate;
	}
	
}
