package com.kp.cms.forms.usermanagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.NewsEventsTO;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admin.StudentFeedbackInstructionsTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.admission.OnlinePaymentRecieptsTo;
import com.kp.cms.to.exam.CandidatePaymentDetailsTO;
import com.kp.cms.to.exam.ClearanceCertificateTO;
import com.kp.cms.to.exam.ConsolidateMarksCardTO;
import com.kp.cms.to.exam.ExamInternalRetestApplicationSubjectsTO;
import com.kp.cms.to.exam.ExamMidsemRepeatTO;
import com.kp.cms.to.exam.HallTicketTo;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.to.exam.RevaluationMarksUpdateTo;
import com.kp.cms.to.exam.StudentMarkDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.fee.FeePaymentTO;
import com.kp.cms.to.fee.PrintChalanTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;
import com.kp.cms.to.usermanagement.LoginTransactionTo;

@SuppressWarnings("serial")
public class LoginForm extends BaseActionForm {

	private String userName;

	private String password;

	private String description;

	private List<LoginTransactionTo> moduleMenusList;

	private String encryptedPassword;

	private List<NewsEventsTO> newsEventsList;

	private String dateOfBirth;
	private String contactMail;
	private String studentName;
	private String fatherName;
	private String motherName;
	private String className;
	// new addition
	private String currentAddress1;
	private String currentAddress2;
	private String currentCity;
	private String currentState;
	private String currentCountry;
	private String currentPincode;
	private String permanentAddress1;
	private String permanentAddress2;
	private String permanentCity;
	private String permanentState;
	private String permanentCountry;
	private String permanentPincode;
	private String nationality;
	private String bloodGroup;
	private String phNo1;
	private String phNo2;
	// ....ends
	private String photoPath;
	private int agreementId;
	private String agreement;
	private int examId;
	private HallTicketTo hallTicket;
	private int studentId;
	private int termNo;
	private int examIDForMCard;
	private int marksCardClassId;
	private int semesterYearNo;
	private MarksCardTO marksCardTo;
	private int supHallTicketexamID;
	private int supMCexamID;
	private int supMCsemesterYearNo;
	private int supMCClassId;
	private String search;
	private Integer menuCount;
	private String blockId;
	private ClearanceCertificateTO cto;
	private String description1;
	private String mobileNo;
	private int personalDateId;
	// added for challan print
	private List<FeePaymentTO> feeToList;
	private String financialYear;
	private String billNo;
	private String applnNo;
	private String challanPrintedDate;
	private String installmentReferenceNo;
	private String concessionReferenceNo;
	private String scholarshipReferenceNo;
	private String currencyCode;
	private String chalanCreatedTime;
	private String paymentMode;
	private String accwiseTotalPrintString;
	private Map<Integer, Double> accountWiseNonOptionalAmount;
	private Map<Integer, Double> accountWiseOptionalAmount;
	private Map<Integer, String> allFeeAccountMap;
	private Map<Integer, Double> fullAccountWiseTotal;
	private List<PrintChalanTO> printChalanList;
	private Boolean isSinglePrint;
	private int lastNo;
	private String verifiedBy;
	ConsolidateMarksCardTO consolidateMarksCardTO;
	private String loginType;
	private List<Integer> revaluationRegClassId;
	private List<Integer> revaluationSupClassId;
	private Map<String, Double> revalationFeeMap;
	private Map<Integer, String> revaluationSubjects;
	private boolean checkRevaluation;
	private boolean checkDD;
	private String ddNo;
	private String ddDate;
	private String amount;
	private String marksCardType;
	private String bankName;
	private String branchName;
	Map<Integer, String> revDateMap;
	Map<Integer, String> suprevDateMap;
	private String revDate;
	private boolean attendanceLogin;
	private String bankAccNo;
	private Integer notifications;
	private boolean ciaEntrys;
	private boolean supHallTicket;
	private int count;
	private List<OnlinePaymentRecieptsTo> paymentList;
	List<MarksCardTO> regularExamList;
	List<MarksCardTO> suppExamList;
	private String regularExamId;
	private String suppExamId;
	private boolean agreementAccepted;
	private String enteredDob;
	private String regNo;
	private String smartCardNo;
	private String validThruMonth;
	private String validThruYear;
	private boolean feesNotConfigured;
	private double totalFees;
	private int finId;
	private int onlinePaymentId;
	private boolean printCertificateReq;
	private String printData;
	private String msg;
	private boolean peerEvaluationLinkPresent;
	private boolean researchLinkPresent;
	private String displaySem1and2;
	private String batch;
	private Boolean participatingConvocation;
	private Boolean guestPassRequired;
	private String relationshipWithGuest;
	private int convocationId;
	private boolean convocationRelation;
	private String homePage;
	private int sapId;
	private Boolean sapRegExist;
	private String univEmailId;
	private List<LoginTransactionTo> invigilationDutyAllotmentDetails;
	private Boolean isAllotmentDetails;
	private String status;
	private String sapExamDate;
	private Boolean statusIsPass;
	private Boolean statusIsFail;
	private Boolean statusIsIsOther;
	private Boolean linkForCJC;
	private String displayLinkExamName;
	private List<StudentMarkDetailsTO> studentMarkDetailsTOList;
	private String serverDownMessage;
	private String midSemStudentId;
	private String midSemRepeatId;
	private String midSemRepeatExamId;
	private String midSemExamId;
	private String repeatExamName;
	private String midSemStudentName;
	private String midSemRepeatRegNo;
	private String midSemClassName;
	private int midSemClassId;
	private BigDecimal midSemAmount;
	private List<ExamMidsemRepeatTO> midSemRepeatList;
	private boolean midSemPrint;
	private String isFeesPaid;
	private Boolean isDownloaded;
	private BigDecimal midSemTotalAmount;
	private String dob;
	private List<Integer> subjectIdList;
	private String attemtsCompleted;
	private String attemptsCount;
	private String midSemRepeatProgram;
	private String midSemRepeatReason;
	private String midSemFatherName;
	private String midSemGender;
	private String midSemAttemptsLeft;
	private String midSemCountWords;
	private String midSemAggreagatePrint;
	private String syllabusEntryBatch;
	private String feesExemption;
	private String feeEndDate;
	private Boolean studentPhotoUpload;
	private boolean isImpReap;
	Map<Integer, String> examNameMap;
	public List<StudentFeedbackInstructionsTO> instructionsList;
	private int revClassId;

	private List<StudentInstructionTO> stuFeedbackInsToList;
	private String instruction;
	private int studentGroupId;
	private int studentExId;
	private List<StudentGroupTO> list;
	private List<StudentExtentionTO> exList;
	private String studentGrpId;
	private boolean hasAlreadySubmittedExtensionActivity;
	private String parentUserName;
	private String parentPassword;
	private String date;

	private Map<Integer, String> internalClassesMap;
	private String internalClassId;
	private Map<Integer, String> examNameList;
	private StudentTO internalMarks;
	private List<StudentMarksTO> examList;
	private String studentSemesterFeesAmount;
	private String method;

	// pgi properties
	private String refNo;
	private String productinfo;
	private String test;
	private String mihpayid;
	private String mode1;
	private String paymentStatus;
	private String key;
	private String txnid;
	private String hash;
	private String error1;
	private String PG_TYPE;
	private String bank_ref_num;
	private String unmappedstatus;
	private String payuMoneyId;
	private Boolean isTnxStatusSuccess;
	private String additionalCharges;
	private String pgiStatus;
	private String txnAmount;
	private String txnRefNo;
	private String txnDate;
	private boolean paymentDone;
	private List<CandidatePaymentDetailsTO> paymentDetailsList;
	private String deptName;
	private List<PublishSpecialFeesTO> publishList;
	private String specialFeesAmount;
	private boolean paymentSuccess;
	private String lateFineFees;
	private List<ExamInternalRetestApplicationSubjectsTO> examInternalRetestApplicationSubjectsTO;
	private List<RevaluationMarksUpdateTo> revaluationMemoList;
	
	

	public String getLateFineFees() {
		return lateFineFees;
	}

	public void setLateFineFees(String lateFineFees) {
		this.lateFineFees = lateFineFees;
	}

	public boolean isPaymentSuccess() {
		return paymentSuccess;
	}

	public void setPaymentSuccess(boolean paymentSuccess) {
		this.paymentSuccess = paymentSuccess;
	}

	public List<StudentExtentionTO> getExList() {
		return exList;
	}

	public void setExList(List<StudentExtentionTO> exList) {
		this.exList = exList;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public List<StudentInstructionTO> getStuFeedbackInsToList() {
		return stuFeedbackInsToList;
	}

	public void setStuFeedbackInsToList(
			List<StudentInstructionTO> stuFeedbackInsToList) {
		this.stuFeedbackInsToList = stuFeedbackInsToList;
	}

	public List<StudentFeedbackInstructionsTO> getInstructionsList() {
		return instructionsList;
	}

	public void setInstructionsList(
			List<StudentFeedbackInstructionsTO> instructionsList) {
		this.instructionsList = instructionsList;
	}

	// reg raghu
	private String regAppExamId;
	private String userOTP;
	private boolean dontShowPracticals;

	public Map<Integer, String> getExamNameMap() {
		return examNameMap;
	}

	public void setExamNameMap(Map<Integer, String> examNameMap) {
		this.examNameMap = examNameMap;
	}

	public Boolean getStudentPhotoUpload() {
		return studentPhotoUpload;
	}

	public void setStudentPhotoUpload(Boolean studentPhotoUpload) {
		this.studentPhotoUpload = studentPhotoUpload;
	}

	public String getSyllabusEntryBatch() {
		return syllabusEntryBatch;
	}

	public void setSyllabusEntryBatch(String syllabusEntryBatch) {
		this.syllabusEntryBatch = syllabusEntryBatch;
	}

	public String getServerDownMessage() {
		return serverDownMessage;
	}

	public void setServerDownMessage(String serverDownMessage) {
		this.serverDownMessage = serverDownMessage;
	}

	public List<StudentMarkDetailsTO> getStudentMarkDetailsTOList() {
		return studentMarkDetailsTOList;
	}

	public void setStudentMarkDetailsTOList(
			List<StudentMarkDetailsTO> studentMarkDetailsTOList) {
		this.studentMarkDetailsTOList = studentMarkDetailsTOList;
	}

	public boolean isConvocationRelation() {
		return convocationRelation;
	}

	public void setConvocationRelation(boolean convocationRelation) {
		this.convocationRelation = convocationRelation;
	}

	public int getConvocationId() {
		return convocationId;
	}

	public void setConvocationId(int convocationId) {
		this.convocationId = convocationId;
	}

	public Integer getMenuCount() {
		return menuCount;
	}

	public void setMenuCount(Integer menuCount) {
		this.menuCount = menuCount;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public LoginForm() {
		this.moduleMenusList = new ArrayList<LoginTransactionTo>();
	}

	public List<NewsEventsTO> getNewsEventsList() {
		return newsEventsList;
	}

	public void setNewsEventsList(List<NewsEventsTO> newsEventsList) {
		this.newsEventsList = newsEventsList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<LoginTransactionTo> getModuleMenusList() {
		return moduleMenusList;
	}

	public void setModuleMenusList(List<LoginTransactionTo> moduleMenusList) {
		this.moduleMenusList = moduleMenusList;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);

		return actionErrors;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the contactMail
	 */
	public String getContactMail() {
		return contactMail;
	}

	/**
	 * @param contactMail
	 *            the contactMail to set
	 */
	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public String getClassName() {
		return className;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void resetFields() {
		this.userName = null;
		this.password = null;
		this.serverDownMessage = null;
	}

	public void reset1() {
		this.participatingConvocation = null;
		this.guestPassRequired = null;
		this.relationshipWithGuest = null;
		this.serverDownMessage = null;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(int agreementId) {
		this.agreementId = agreementId;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public HallTicketTo getHallTicket() {
		return hallTicket;
	}

	public void setHallTicket(HallTicketTo hallTicket) {
		this.hallTicket = hallTicket;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getTermNo() {
		return termNo;
	}

	public void setTermNo(int termNo) {
		this.termNo = termNo;
	}

	public int getExamIDForMCard() {
		return examIDForMCard;
	}

	public void setExamIDForMCard(int examIDForMCard) {
		this.examIDForMCard = examIDForMCard;
	}

	public int getMarksCardClassId() {
		return marksCardClassId;
	}

	public void setMarksCardClassId(int marksCardClassId) {
		this.marksCardClassId = marksCardClassId;
	}

	public int getSemesterYearNo() {
		return semesterYearNo;
	}

	public void setSemesterYearNo(int semesterYearNo) {
		this.semesterYearNo = semesterYearNo;
	}

	public MarksCardTO getMarksCardTo() {
		return marksCardTo;
	}

	public void setMarksCardTo(MarksCardTO marksCardTo) {
		this.marksCardTo = marksCardTo;
	}

	public int getSupHallTicketexamID() {
		return supHallTicketexamID;
	}

	public void setSupHallTicketexamID(int supHallTicketexamID) {
		this.supHallTicketexamID = supHallTicketexamID;
	}

	public int getSupMCexamID() {
		return supMCexamID;
	}

	public void setSupMCexamID(int supMCexamID) {
		this.supMCexamID = supMCexamID;
	}

	public int getSupMCsemesterYearNo() {
		return supMCsemesterYearNo;
	}

	public void setSupMCsemesterYearNo(int supMCsemesterYearNo) {
		this.supMCsemesterYearNo = supMCsemesterYearNo;
	}

	public int getSupMCClassId() {
		return supMCClassId;
	}

	public void setSupMCClassId(int supMCClassId) {
		this.supMCClassId = supMCClassId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public ClearanceCertificateTO getCto() {
		return cto;
	}

	public void setCto(ClearanceCertificateTO cto) {
		this.cto = cto;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setPersonalDateId(int personalDateId) {
		this.personalDateId = personalDateId;
	}

	public int getPersonalDateId() {
		return personalDateId;
	}

	public List<FeePaymentTO> getFeeToList() {
		return feeToList;
	}

	public void setFeeToList(List<FeePaymentTO> feeToList) {
		this.feeToList = feeToList;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getApplnNo() {
		return applnNo;
	}

	public void setApplnNo(String applnNo) {
		this.applnNo = applnNo;
	}

	public String getChallanPrintedDate() {
		return challanPrintedDate;
	}

	public void setChallanPrintedDate(String challanPrintedDate) {
		this.challanPrintedDate = challanPrintedDate;
	}

	public String getInstallmentReferenceNo() {
		return installmentReferenceNo;
	}

	public void setInstallmentReferenceNo(String installmentReferenceNo) {
		this.installmentReferenceNo = installmentReferenceNo;
	}

	public String getConcessionReferenceNo() {
		return concessionReferenceNo;
	}

	public void setConcessionReferenceNo(String concessionReferenceNo) {
		this.concessionReferenceNo = concessionReferenceNo;
	}

	public String getScholarshipReferenceNo() {
		return scholarshipReferenceNo;
	}

	public void setScholarshipReferenceNo(String scholarshipReferenceNo) {
		this.scholarshipReferenceNo = scholarshipReferenceNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getChalanCreatedTime() {
		return chalanCreatedTime;
	}

	public void setChalanCreatedTime(String chalanCreatedTime) {
		this.chalanCreatedTime = chalanCreatedTime;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getAccwiseTotalPrintString() {
		return accwiseTotalPrintString;
	}

	public void setAccwiseTotalPrintString(String accwiseTotalPrintString) {
		this.accwiseTotalPrintString = accwiseTotalPrintString;
	}

	public Map<Integer, Double> getAccountWiseNonOptionalAmount() {
		return accountWiseNonOptionalAmount;
	}

	public void setAccountWiseNonOptionalAmount(
			Map<Integer, Double> accountWiseNonOptionalAmount) {
		this.accountWiseNonOptionalAmount = accountWiseNonOptionalAmount;
	}

	public Map<Integer, Double> getAccountWiseOptionalAmount() {
		return accountWiseOptionalAmount;
	}

	public void setAccountWiseOptionalAmount(
			Map<Integer, Double> accountWiseOptionalAmount) {
		this.accountWiseOptionalAmount = accountWiseOptionalAmount;
	}

	public Map<Integer, String> getAllFeeAccountMap() {
		return allFeeAccountMap;
	}

	public void setAllFeeAccountMap(Map<Integer, String> allFeeAccountMap) {
		this.allFeeAccountMap = allFeeAccountMap;
	}

	public Map<Integer, Double> getFullAccountWiseTotal() {
		return fullAccountWiseTotal;
	}

	public void setFullAccountWiseTotal(
			Map<Integer, Double> fullAccountWiseTotal) {
		this.fullAccountWiseTotal = fullAccountWiseTotal;
	}

	public List<PrintChalanTO> getPrintChalanList() {
		return printChalanList;
	}

	public void setPrintChalanList(List<PrintChalanTO> printChalanList) {
		this.printChalanList = printChalanList;
	}

	public Boolean getIsSinglePrint() {
		return isSinglePrint;
	}

	public void setIsSinglePrint(Boolean isSinglePrint) {
		this.isSinglePrint = isSinglePrint;
	}

	public int getLastNo() {
		return lastNo;
	}

	public void setLastNo(int lastNo) {
		this.lastNo = lastNo;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public ConsolidateMarksCardTO getConsolidateMarksCardTO() {
		return consolidateMarksCardTO;
	}

	public void setConsolidateMarksCardTO(
			ConsolidateMarksCardTO consolidateMarksCardTO) {
		this.consolidateMarksCardTO = consolidateMarksCardTO;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public List<Integer> getRevaluationRegClassId() {
		return revaluationRegClassId;
	}

	public void setRevaluationRegClassId(List<Integer> revaluationRegClassId) {
		this.revaluationRegClassId = revaluationRegClassId;
	}

	public List<Integer> getRevaluationSupClassId() {
		return revaluationSupClassId;
	}

	public void setRevaluationSupClassId(List<Integer> revaluationSupClassId) {
		this.revaluationSupClassId = revaluationSupClassId;
	}

	public Map<String, Double> getRevalationFeeMap() {
		return revalationFeeMap;
	}

	public void setRevalationFeeMap(Map<String, Double> revalationFeeMap) {
		this.revalationFeeMap = revalationFeeMap;
	}

	public Map<Integer, String> getRevaluationSubjects() {
		return revaluationSubjects;
	}

	public void setRevaluationSubjects(Map<Integer, String> revaluationSubjects) {
		this.revaluationSubjects = revaluationSubjects;
	}

	public boolean isCheckRevaluation() {
		return checkRevaluation;
	}

	public void setCheckRevaluation(boolean checkRevaluation) {
		this.checkRevaluation = checkRevaluation;
	}

	public boolean isCheckDD() {
		return checkDD;
	}

	public void setCheckDD(boolean checkDD) {
		this.checkDD = checkDD;
	}

	public String getDdNo() {
		return ddNo;
	}

	public void setDdNo(String ddNo) {
		this.ddNo = ddNo;
	}

	public String getDdDate() {
		return ddDate;
	}

	public void setDdDate(String ddDate) {
		this.ddDate = ddDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMarksCardType() {
		return marksCardType;
	}

	public void setMarksCardType(String marksCardType) {
		this.marksCardType = marksCardType;
	}

	public void clearRevaluation() {
		this.amount = null;
		this.ddDate = null;
		this.ddNo = null;
		this.bankName = null;
		this.branchName = null;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Map<Integer, String> getRevDateMap() {
		return revDateMap;
	}

	public void setRevDateMap(Map<Integer, String> revDateMap) {
		this.revDateMap = revDateMap;
	}

	public Map<Integer, String> getSuprevDateMap() {
		return suprevDateMap;
	}

	public void setSuprevDateMap(Map<Integer, String> suprevDateMap) {
		this.suprevDateMap = suprevDateMap;
	}

	public String getRevDate() {
		return revDate;
	}

	public void setRevDate(String revDate) {
		this.revDate = revDate;
	}

	public boolean isAttendanceLogin() {
		return attendanceLogin;
	}

	public void setAttendanceLogin(boolean attendanceLogin) {
		this.attendanceLogin = attendanceLogin;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public Integer getNotifications() {
		return notifications;
	}

	public void setNotifications(Integer notifications) {
		this.notifications = notifications;
	}

	public boolean isCiaEntrys() {
		return ciaEntrys;
	}

	public void setCiaEntrys(boolean ciaEntrys) {
		this.ciaEntrys = ciaEntrys;
	}

	public boolean isSupHallTicket() {
		return supHallTicket;
	}

	public void setSupHallTicket(boolean supHallTicket) {
		this.supHallTicket = supHallTicket;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OnlinePaymentRecieptsTo> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<OnlinePaymentRecieptsTo> paymentList) {
		this.paymentList = paymentList;
	}

	public String getRegularExamId() {
		return regularExamId;
	}

	public void setRegularExamId(String regularExamId) {
		this.regularExamId = regularExamId;
	}

	public String getSuppExamId() {
		return suppExamId;
	}

	public void setSuppExamId(String suppExamId) {
		this.suppExamId = suppExamId;
	}

	public List<MarksCardTO> getRegularExamList() {
		return regularExamList;
	}

	public void setRegularExamList(List<MarksCardTO> regularExamList) {
		this.regularExamList = regularExamList;
	}

	public List<MarksCardTO> getSuppExamList() {
		return suppExamList;
	}

	public void setSuppExamList(List<MarksCardTO> suppExamList) {
		this.suppExamList = suppExamList;
	}

	public boolean isAgreementAccepted() {
		return agreementAccepted;
	}

	public void setAgreementAccepted(boolean agreementAccepted) {
		this.agreementAccepted = agreementAccepted;
	}

	public String getEnteredDob() {
		return enteredDob;
	}

	public void setEnteredDob(String enteredDob) {
		this.enteredDob = enteredDob;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getSmartCardNo() {
		return smartCardNo;
	}

	public void setSmartCardNo(String smartCardNo) {
		this.smartCardNo = smartCardNo;
	}

	public String getValidThruMonth() {
		return validThruMonth;
	}

	public void setValidThruMonth(String validThruMonth) {
		this.validThruMonth = validThruMonth;
	}

	public String getValidThruYear() {
		return validThruYear;
	}

	public void setValidThruYear(String validThruYear) {
		this.validThruYear = validThruYear;
	}

	public boolean isFeesNotConfigured() {
		return feesNotConfigured;
	}

	public void setFeesNotConfigured(boolean feesNotConfigured) {
		this.feesNotConfigured = feesNotConfigured;
	}

	public double getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(double totalFees) {
		this.totalFees = totalFees;
	}

	public int getFinId() {
		return finId;
	}

	public void setFinId(int finId) {
		this.finId = finId;
	}

	public int getOnlinePaymentId() {
		return onlinePaymentId;
	}

	public void setOnlinePaymentId(int onlinePaymentId) {
		this.onlinePaymentId = onlinePaymentId;
	}

	public boolean isPrintCertificateReq() {
		return printCertificateReq;
	}

	public void setPrintCertificateReq(boolean printCertificateReq) {
		this.printCertificateReq = printCertificateReq;
	}

	public String getPrintData() {
		return printData;
	}

	public void setPrintData(String printData) {
		this.printData = printData;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCurrentAddress1() {
		return currentAddress1;
	}

	public void setCurrentAddress1(String currentAddress1) {
		this.currentAddress1 = currentAddress1;
	}

	public String getCurrentAddress2() {
		return currentAddress2;
	}

	public void setCurrentAddress2(String currentAddress2) {
		this.currentAddress2 = currentAddress2;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getCurrentCountry() {
		return currentCountry;
	}

	public void setCurrentCountry(String currentCountry) {
		this.currentCountry = currentCountry;
	}

	public String getPermanentAddress1() {
		return permanentAddress1;
	}

	public void setPermanentAddress1(String permanentAddress1) {
		this.permanentAddress1 = permanentAddress1;
	}

	public String getPermanentAddress2() {
		return permanentAddress2;
	}

	public void setPermanentAddress2(String permanentAddress2) {
		this.permanentAddress2 = permanentAddress2;
	}

	public String getPermanentCity() {
		return permanentCity;
	}

	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}

	public String getPermanentState() {
		return permanentState;
	}

	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}

	public String getPermanentCountry() {
		return permanentCountry;
	}

	public void setPermanentCountry(String permanentCountry) {
		this.permanentCountry = permanentCountry;
	}

	public String getCurrentPincode() {
		return currentPincode;
	}

	public void setCurrentPincode(String currentPincode) {
		this.currentPincode = currentPincode;
	}

	public String getPermanentPincode() {
		return permanentPincode;
	}

	public void setPermanentPincode(String permanentPincode) {
		this.permanentPincode = permanentPincode;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getPhNo1() {
		return phNo1;
	}

	public void setPhNo1(String phNo1) {
		this.phNo1 = phNo1;
	}

	public String getPhNo2() {
		return phNo2;
	}

	public void setPhNo2(String phNo2) {
		this.phNo2 = phNo2;
	}

	public boolean isPeerEvaluationLinkPresent() {
		return peerEvaluationLinkPresent;
	}

	public void setPeerEvaluationLinkPresent(boolean peerEvaluationLinkPresent) {
		this.peerEvaluationLinkPresent = peerEvaluationLinkPresent;
	}

	public boolean isResearchLinkPresent() {
		return researchLinkPresent;
	}

	public void setResearchLinkPresent(boolean researchLinkPresent) {
		this.researchLinkPresent = researchLinkPresent;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getDisplaySem1and2() {
		return displaySem1and2;
	}

	public void setDisplaySem1and2(String displaySem1and2) {
		this.displaySem1and2 = displaySem1and2;
	}

	public Boolean getParticipatingConvocation() {
		return participatingConvocation;
	}

	public void setParticipatingConvocation(Boolean participatingConvocation) {
		this.participatingConvocation = participatingConvocation;
	}

	public Boolean getGuestPassRequired() {
		return guestPassRequired;
	}

	public void setGuestPassRequired(Boolean guestPassRequired) {
		this.guestPassRequired = guestPassRequired;
	}

	public String getRelationshipWithGuest() {
		return relationshipWithGuest;
	}

	public void setRelationshipWithGuest(String relationshipWithGuest) {
		this.relationshipWithGuest = relationshipWithGuest;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public int getSapId() {
		return sapId;
	}

	public void setSapId(int sapId) {
		this.sapId = sapId;
	}

	public Boolean getSapRegExist() {
		return sapRegExist;
	}

	public void setSapRegExist(Boolean sapRegExist) {
		this.sapRegExist = sapRegExist;
	}

	public String getUnivEmailId() {
		return univEmailId;
	}

	public void setUnivEmailId(String univEmailId) {
		this.univEmailId = univEmailId;
	}

	public List<LoginTransactionTo> getInvigilationDutyAllotmentDetails() {
		return invigilationDutyAllotmentDetails;
	}

	public void setInvigilationDutyAllotmentDetails(
			List<LoginTransactionTo> invigilationDutyAllotmentDetails) {
		this.invigilationDutyAllotmentDetails = invigilationDutyAllotmentDetails;
	}

	public Boolean getIsAllotmentDetails() {
		return isAllotmentDetails;
	}

	public void setIsAllotmentDetails(Boolean isAllotmentDetails) {
		this.isAllotmentDetails = isAllotmentDetails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSapExamDate() {
		return sapExamDate;
	}

	public void setSapExamDate(String sapExamDate) {
		this.sapExamDate = sapExamDate;
	}

	public Boolean getStatusIsPass() {
		return statusIsPass;
	}

	public void setStatusIsPass(Boolean statusIsPass) {
		this.statusIsPass = statusIsPass;
	}

	public Boolean getStatusIsFail() {
		return statusIsFail;
	}

	public void setStatusIsFail(Boolean statusIsFail) {
		this.statusIsFail = statusIsFail;
	}

	public Boolean getStatusIsIsOther() {
		return statusIsIsOther;
	}

	public void setStatusIsIsOther(Boolean statusIsIsOther) {
		this.statusIsIsOther = statusIsIsOther;
	}

	public Boolean getLinkForCJC() {
		return linkForCJC;
	}

	public void setLinkForCJC(Boolean linkForCJC) {
		this.linkForCJC = linkForCJC;
	}

	public String getDisplayLinkExamName() {
		return displayLinkExamName;
	}

	public void setDisplayLinkExamName(String displayLinkExamName) {
		this.displayLinkExamName = displayLinkExamName;
	}

	public void clearMideSemExam() {
		this.midSemStudentId = null;
		this.midSemRepeatId = null;
		this.midSemExamId = null;
		this.midSemRepeatList = null;
		this.repeatExamName = null;
		this.midSemStudentName = null;
		this.midSemRepeatRegNo = null;
		this.midSemClassName = null;
		this.midSemPrint = false;
		this.midSemAmount = null;
		this.midSemAmount = null;
		this.isFeesPaid = null;
		this.midSemRepeatExamId = null;
		this.isDownloaded = false;
		this.midSemTotalAmount = null;
		this.midSemClassId = 0;
		this.subjectIdList = null;
		this.attemtsCompleted = "false";
		this.attemptsCount = null;
		this.midSemRepeatProgram = null;
		this.midSemRepeatReason = null;
		this.midSemFatherName = null;
		this.midSemGender = null;
		this.midSemAttemptsLeft = "0";
		this.midSemCountWords = null;
		this.midSemAggreagatePrint = "0";
	}

	public String getMidSemStudentId() {
		return midSemStudentId;
	}

	public void setMidSemStudentId(String midSemStudentId) {
		this.midSemStudentId = midSemStudentId;
	}

	public String getMidSemRepeatId() {
		return midSemRepeatId;
	}

	public void setMidSemRepeatId(String midSemRepeatId) {
		this.midSemRepeatId = midSemRepeatId;
	}

	public String getMidSemRepeatExamId() {
		return midSemRepeatExamId;
	}

	public void setMidSemRepeatExamId(String midSemRepeatExamId) {
		this.midSemRepeatExamId = midSemRepeatExamId;
	}

	public String getMidSemExamId() {
		return midSemExamId;
	}

	public void setMidSemExamId(String midSemExamId) {
		this.midSemExamId = midSemExamId;
	}

	public String getRepeatExamName() {
		return repeatExamName;
	}

	public void setRepeatExamName(String repeatExamName) {
		this.repeatExamName = repeatExamName;
	}

	public String getMidSemStudentName() {
		return midSemStudentName;
	}

	public void setMidSemStudentName(String midSemStudentName) {
		this.midSemStudentName = midSemStudentName;
	}

	public String getMidSemRepeatRegNo() {
		return midSemRepeatRegNo;
	}

	public void setMidSemRepeatRegNo(String midSemRepeatRegNo) {
		this.midSemRepeatRegNo = midSemRepeatRegNo;
	}

	public String getMidSemClassName() {
		return midSemClassName;
	}

	public void setMidSemClassName(String midSemClassName) {
		this.midSemClassName = midSemClassName;
	}

	public int getMidSemClassId() {
		return midSemClassId;
	}

	public void setMidSemClassId(int midSemClassId) {
		this.midSemClassId = midSemClassId;
	}

	public BigDecimal getMidSemAmount() {
		return midSemAmount;
	}

	public void setMidSemAmount(BigDecimal midSemAmount) {
		this.midSemAmount = midSemAmount;
	}

	public List<ExamMidsemRepeatTO> getMidSemRepeatList() {
		return midSemRepeatList;
	}

	public void setMidSemRepeatList(List<ExamMidsemRepeatTO> midSemRepeatList) {
		this.midSemRepeatList = midSemRepeatList;
	}

	public boolean isMidSemPrint() {
		return midSemPrint;
	}

	public void setMidSemPrint(boolean midSemPrint) {
		this.midSemPrint = midSemPrint;
	}

	public String getIsFeesPaid() {
		return isFeesPaid;
	}

	public void setIsFeesPaid(String isFeesPaid) {
		this.isFeesPaid = isFeesPaid;
	}

	public Boolean getIsDownloaded() {
		return isDownloaded;
	}

	public void setIsDownloaded(Boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

	public BigDecimal getMidSemTotalAmount() {
		return midSemTotalAmount;
	}

	public void setMidSemTotalAmount(BigDecimal midSemTotalAmount) {
		this.midSemTotalAmount = midSemTotalAmount;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public List<Integer> getSubjectIdList() {
		return subjectIdList;
	}

	public void setSubjectIdList(List<Integer> subjectIdList) {
		this.subjectIdList = subjectIdList;
	}

	public String getAttemtsCompleted() {
		return attemtsCompleted;
	}

	public void setAttemtsCompleted(String attemtsCompleted) {
		this.attemtsCompleted = attemtsCompleted;
	}

	public String getAttemptsCount() {
		return attemptsCount;
	}

	public void setAttemptsCount(String attemptsCount) {
		this.attemptsCount = attemptsCount;
	}

	public String getMidSemRepeatProgram() {
		return midSemRepeatProgram;
	}

	public void setMidSemRepeatProgram(String midSemRepeatProgram) {
		this.midSemRepeatProgram = midSemRepeatProgram;
	}

	public String getMidSemRepeatReason() {
		return midSemRepeatReason;
	}

	public void setMidSemRepeatReason(String midSemRepeatReason) {
		this.midSemRepeatReason = midSemRepeatReason;
	}

	public String getMidSemFatherName() {
		return midSemFatherName;
	}

	public void setMidSemFatherName(String midSemFatherName) {
		this.midSemFatherName = midSemFatherName;
	}

	public String getMidSemGender() {
		return midSemGender;
	}

	public void setMidSemGender(String midSemGender) {
		this.midSemGender = midSemGender;
	}

	public String getMidSemAttemptsLeft() {
		return midSemAttemptsLeft;
	}

	public void setMidSemAttemptsLeft(String midSemAttemptsLeft) {
		this.midSemAttemptsLeft = midSemAttemptsLeft;
	}

	public String getMidSemCountWords() {
		return midSemCountWords;
	}

	public void setMidSemCountWords(String midSemCountWords) {
		this.midSemCountWords = midSemCountWords;
	}

	public String getMidSemAggreagatePrint() {
		return midSemAggreagatePrint;
	}

	public void setMidSemAggreagatePrint(String midSemAggreagatePrint) {
		this.midSemAggreagatePrint = midSemAggreagatePrint;
	}

	public String getFeesExemption() {
		return feesExemption;
	}

	public void setFeesExemption(String feesExemption) {
		this.feesExemption = feesExemption;
	}

	public String getFeeEndDate() {
		return feeEndDate;
	}

	public void setFeeEndDate(String feeEndDate) {
		this.feeEndDate = feeEndDate;
	}

	public String getRegAppExamId() {
		return regAppExamId;
	}

	public void setRegAppExamId(String regAppExamId) {
		this.regAppExamId = regAppExamId;
	}

	public String getUserOTP() {
		return userOTP;
	}

	public void setUserOTP(String userOTP) {
		this.userOTP = userOTP;
	}

	public boolean isDontShowPracticals() {
		return dontShowPracticals;
	}

	public void setDontShowPracticals(boolean dontShowPracticals) {
		this.dontShowPracticals = dontShowPracticals;
	}

	/**
	 * @return the revClassId
	 */
	public int getRevClassId() {
		return revClassId;
	}

	/**
	 * @param revClassId
	 *            the revClassId to set
	 */
	public void setRevClassId(int revClassId) {
		this.revClassId = revClassId;
	}

	public Boolean getIsImpReap() {
		return isImpReap;
	}

	public void setIsImpReap(Boolean isImpReap) {
		this.isImpReap = isImpReap;
	}

	public List<StudentGroupTO> getList() {
		return list;
	}

	public void setList(List<StudentGroupTO> list) {
		this.list = list;
	}

	public int getStudentGroupId() {
		return studentGroupId;
	}

	public void setStudentGroupId(int studentGroupId) {
		this.studentGroupId = studentGroupId;
	}

	public int getStudentExId() {
		return studentExId;
	}

	public void setStudentExId(int studentExId) {
		this.studentExId = studentExId;
	}

	public String getStudentGrpId() {
		return studentGrpId;
	}

	public void setStudentGrpId(String studentGrpId) {
		this.studentGrpId = studentGrpId;
	}

	public boolean isHasAlreadySubmittedExtensionActivity() {
		return hasAlreadySubmittedExtensionActivity;
	}

	public void setHasAlreadySubmittedExtensionActivity(
			boolean hasAlreadySubmittedExtensionActivity) {
		this.hasAlreadySubmittedExtensionActivity = hasAlreadySubmittedExtensionActivity;
	}

	public String getParentUserName() {
		return parentUserName;
	}

	public void setParentUserName(String parentUserName) {
		this.parentUserName = parentUserName;
	}

	public String getParentPassword() {
		return parentPassword;
	}

	public void setParentPassword(String parentPassword) {
		this.parentPassword = parentPassword;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<Integer, String> getInternalClassesMap() {
		return internalClassesMap;
	}

	public void setInternalClassesMap(Map<Integer, String> internalClassesMap) {
		this.internalClassesMap = internalClassesMap;
	}

	public String getInternalClassId() {
		return internalClassId;
	}

	public void setInternalClassId(String internalClassId) {
		this.internalClassId = internalClassId;
	}

	public Map<Integer, String> getExamNameList() {
		return examNameList;
	}

	public void setExamNameList(Map<Integer, String> examNameList) {
		this.examNameList = examNameList;
	}

	public StudentTO getInternalMarks() {
		return internalMarks;
	}

	public void setInternalMarks(StudentTO internalMarks) {
		this.internalMarks = internalMarks;
	}

	public List<StudentMarksTO> getExamList() {
		return examList;
	}

	public void setExamList(List<StudentMarksTO> examList) {
		this.examList = examList;
	}

	public String getStudentSemesterFeesAmount() {
		return studentSemesterFeesAmount;
	}

	public void setStudentSemesterFeesAmount(String studentSemesterFeesAmount) {
		this.studentSemesterFeesAmount = studentSemesterFeesAmount;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getProductinfo() {
		return productinfo;
	}

	public void setProductinfo(String productinfo) {
		this.productinfo = productinfo;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getMihpayid() {
		return mihpayid;
	}

	public void setMihpayid(String mihpayid) {
		this.mihpayid = mihpayid;
	}

	public String getMode1() {
		return mode1;
	}

	public void setMode1(String mode1) {
		this.mode1 = mode1;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getError1() {
		return error1;
	}

	public void setError1(String error1) {
		this.error1 = error1;
	}

	public String getPG_TYPE() {
		return PG_TYPE;
	}

	public void setPG_TYPE(String pGTYPE) {
		PG_TYPE = pGTYPE;
	}

	public String getBank_ref_num() {
		return bank_ref_num;
	}

	public void setBank_ref_num(String bankRefNum) {
		bank_ref_num = bankRefNum;
	}

	public String getUnmappedstatus() {
		return unmappedstatus;
	}

	public void setUnmappedstatus(String unmappedstatus) {
		this.unmappedstatus = unmappedstatus;
	}

	public String getPayuMoneyId() {
		return payuMoneyId;
	}

	public void setPayuMoneyId(String payuMoneyId) {
		this.payuMoneyId = payuMoneyId;
	}

	

	public Boolean getIsTnxStatusSuccess() {
		return isTnxStatusSuccess;
	}

	public void setIsTnxStatusSuccess(Boolean isTnxStatusSuccess) {
		this.isTnxStatusSuccess = isTnxStatusSuccess;
	}

	public String getAdditionalCharges() {
		return additionalCharges;
	}

	public void setAdditionalCharges(String additionalCharges) {
		this.additionalCharges = additionalCharges;
	}

	public String getPgiStatus() {
		return pgiStatus;
	}

	public void setPgiStatus(String pgiStatus) {
		this.pgiStatus = pgiStatus;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnRefNo() {
		return txnRefNo;
	}

	public void setTxnRefNo(String txnRefNo) {
		this.txnRefNo = txnRefNo;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public boolean isPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(boolean paymentDone) {
		this.paymentDone = paymentDone;
	}

	public List<CandidatePaymentDetailsTO> getPaymentDetailsList() {
		return paymentDetailsList;
	}

	public void setPaymentDetailsList(
			List<CandidatePaymentDetailsTO> paymentDetailsList) {
		this.paymentDetailsList = paymentDetailsList;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<PublishSpecialFeesTO> getPublishList() {
		return publishList;
	}

	public void setPublishList(List<PublishSpecialFeesTO> publishList) {
		this.publishList = publishList;
	}

	public String getSpecialFeesAmount() {
		return specialFeesAmount;
	}

	public void setSpecialFeesAmount(String specialFeesAmount) {
		this.specialFeesAmount = specialFeesAmount;
	}

	public List<ExamInternalRetestApplicationSubjectsTO> getExamInternalRetestApplicationSubjectsTO() {
		return examInternalRetestApplicationSubjectsTO;
	}

	public void setExamInternalRetestApplicationSubjectsTO(
			List<ExamInternalRetestApplicationSubjectsTO> examInternalRetestApplicationSubjectsTO) {
		this.examInternalRetestApplicationSubjectsTO = examInternalRetestApplicationSubjectsTO;
	}

	public List<RevaluationMarksUpdateTo> getRevaluationMemoList() {
		return revaluationMemoList;
	}

	public void setRevaluationMemoList(List<RevaluationMarksUpdateTo> revaluationMemoList) {
		this.revaluationMemoList = revaluationMemoList;
	}
	
	
}