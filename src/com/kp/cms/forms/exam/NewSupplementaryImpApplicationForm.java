package com.kp.cms.forms.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationSubjectTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationTO;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.SupplementaryAppExamTo;

public class NewSupplementaryImpApplicationForm extends BaseActionForm {
	
	private String supplementaryImprovement;
	private String examId;
	private String registerNo;
	private String rollNo;
	private String schemeNo;
	private List<KeyValueTO> examList;
	private Map<String,ExamSupplementaryImpApplicationTO> toMap;
	private String student;
	private ExamSupplementaryImpApplicationTO suppTo;
	private Map<Integer, String> courseList;
	private Map<Integer, String> schemeList;
	private String examName;
	private String schemeName;
	private String addOrEdit;
	private int studentId;
	//added by Smitha 
	private HashMap<Integer, String> examTypeList;
	private Map<Integer,String> examNameMap;
	private boolean suppImpAppAvailable;
	private List<SupplementaryAppExamTo> mainList;
	private double theoryFees;
	private double practicalFees;
	private boolean feesNotConfigured;
	private double totalFees;
	private String msg;
	private int finId;
	private int onlinePaymentId;
	private boolean printSupplementary;
	private String printData;
	private boolean displayButton;
	private boolean isExtended;
	private double fineFees;
	private String eGrandFees;
	
	//raghu added from mounts 
	private String dateOfApplication;
	private String applicationNumber;
	private boolean regularAppAvailable;
	
	private List<ExamSupplementaryImpApplicationTO> suppToList;
	private List<ExamSupplementaryImpApplicationSubjectTO> supSubjectList;
	private String address;
	private Student studentObj;
	private String mobileNo;
	private String email;
	private String courseDep;
	private boolean isAttendanceShortage;
	private boolean add;
	private boolean continue1;
	private String description;
	private boolean supplementary;
	private String examType;
	private String feeConcessionCategory;
	private String feeConcession;
	
	//vinodha
	private String gender; 
	private String communicationAddress;
	private String communicationMobileNo;
	private String communicationEmail;
	private String religion;
	private String careTaker;
	private String permanentAddressZipCode;
	private String communicationAddressZipCode;
	private boolean challanButton;
	
	// vinodha for Online Regular Exam Application fee
	private String selectedFeePayment;
	private boolean onlineApply;
	private String journalNo;
	private String applicationDate;
	private String applicationAmount;
	private String bankBranch;
	private String ddNo;
	private String ddDrawnOn;
	private String ddDate;
	private String ddAmount;
	private Boolean paymentSuccess;
	private String applicationAmount1;
	private String refNo;
	private String productinfo;
	private String applicantName;
	private String mobileNo1;
	private String mobileNo2;
	private String test;
	private String mihpayid;
	private String mode1;
	private String status;
	private String key;
	private String txnid;
	private String amount;
	private String hash;
	private String error1;
	private String PG_TYPE;
	private String bank_ref_num;
	private String unmappedstatus;
	private String payuMoneyId;
	private String additionalCharges;
	private String paymentMail;
	private String pgiStatus;
	private String txnAmt;
	private String txnDate;
	private String txnRefNo;
	private Boolean isTnxStatusSuccess;
	private String candidateRefNo;
	private String transactionRefNO;
	private boolean regExamFeesExempted;
	private String applicationAmountWords;
	private boolean isFine;
	private boolean isSuperFine;
	
	private String applicationFees;
	private String cvCampFees;
	private String marksListFees;
	private String onlineServiceChargeFees;
	private String grandTotal;
	private String prevClassId;
	private String totalFeesInWords;
	private boolean isFeesExempted;
	private boolean isAppliedAlready;
	private boolean previousExam;
	private String prevClassExamId;
	private Map<Integer,String> examNameList;
	private String classCodeIdsFrom;
	private Map<Integer, String> mapClass;
	
	// for revaluation
	private boolean revAppAvailable;
	private String isRevaluation;
	private String isScrutiny;
	private int revclassid;
	
	private double revaluationFees;
	private double scrutinyFees;
	private double onlineSevriceFees;
	private String challanNo;
	private String revExamId;
	private String isChallengeRevaluation;
	private double challengeRevaluationFees;
	private int prevSemNo;
	private boolean appliedAlready;
	private boolean isAppliedThroughAdmin;
	private String suppExamId;
	private String suppClassId;
	List<StudentTO> stuList;
	private Boolean print;
	private boolean printApplication;
	private int revalSupplyClassId;
	
	private String theoryFee;
	
	private String universityFee;
	private String totalFee;
	private int suppImpClassId;
	private String totalRevaluationPaymentFees;
	private String method;
	private boolean paymentDone;
	private boolean eGrandStudent;
	private boolean scrutiny;
	
	

	

	public boolean iseGrandStudent() {
		return eGrandStudent;
	}
	public void seteGrandStudent(boolean eGrandStudent) {
		this.eGrandStudent = eGrandStudent;
	}
	public boolean isPaymentDone() {
		return paymentDone;
	}
	public void setPaymentDone(boolean paymentDone) {
		this.paymentDone = paymentDone;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getRevalSupplyClassId() {
		return revalSupplyClassId;
	}
	public void setRevalSupplyClassId(int revalSupplyClassId) {
		this.revalSupplyClassId = revalSupplyClassId;
	}
	public boolean getPrintApplication() {
		return printApplication;
	}
	public void setPrintApplication(boolean printApplication) {
		this.printApplication = printApplication;
	}
	public List<ExamSupplementaryImpApplicationTO> getSuppToList() {
		return suppToList;
	}
	public void setSuppToList(List<ExamSupplementaryImpApplicationTO> suppToList) {
		this.suppToList = suppToList;
	}
	
	public String getSupplementaryImprovement() {
		return supplementaryImprovement;
	}
	public void setSupplementaryImprovement(String supplementaryImprovement) {
		this.supplementaryImprovement = supplementaryImprovement;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getSchemeNo() {
		return schemeNo;
	}
	public void setSchemeNo(String schemeNo) {
		this.schemeNo = schemeNo;
	}
	
	public List<KeyValueTO> getExamList() {
		return examList;
	}
	public void setExamList(List<KeyValueTO> examList) {
		this.examList = examList;
	}
	public Map<String, ExamSupplementaryImpApplicationTO> getToMap() {
		return toMap;
	}
	public void setToMap(Map<String, ExamSupplementaryImpApplicationTO> toMap) {
		this.toMap = toMap;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public ExamSupplementaryImpApplicationTO getSuppTo() {
		return suppTo;
	}
	public void setSuppTo(ExamSupplementaryImpApplicationTO suppTo) {
		this.suppTo = suppTo;
	}
	public Map<Integer, String> getCourseList() {
		return courseList;
	}
	public void setCourseList(Map<Integer, String> courseList) {
		this.courseList = courseList;
	}
	public Map<Integer, String> getSchemeList() {
		return schemeList;
	}
	public void setSchemeList(Map<Integer, String> schemeList) {
		this.schemeList = schemeList;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getAddOrEdit() {
		return addOrEdit;
	}
	public void setAddOrEdit(String addOrEdit) {
		this.addOrEdit = addOrEdit;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public HashMap<Integer, String> getExamTypeList() {
		return examTypeList;
	}
	public void setExamTypeList(HashMap<Integer, String> examTypeList) {
		this.examTypeList = examTypeList;
	}
	public Map<Integer, String> getExamNameMap() {
		return examNameMap;
	}
	public void setExamNameMap(Map<Integer, String> examNameMap) {
		this.examNameMap = examNameMap;
	}
 	public boolean isSuppImpAppAvailable() {
		return suppImpAppAvailable;
	}
	public void setSuppImpAppAvailable(boolean suppImpAppAvailable) {
		this.suppImpAppAvailable = suppImpAppAvailable;
	}
	public List<SupplementaryAppExamTo> getMainList() {
		return mainList;
	}
	public void setMainList(List<SupplementaryAppExamTo> mainList) {
		this.mainList = mainList;
	}
	public double getTheoryFees() {
		return theoryFees;
	}
	public void setTheoryFees(double theoryFees) {
		this.theoryFees = theoryFees;
	}
	public double getPracticalFees() {
		return practicalFees;
	}
	public void setPracticalFees(double practicalFees) {
		this.practicalFees = practicalFees;
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
	public boolean isPrintSupplementary() {
		return printSupplementary;
	}
	public void setPrintSupplementary(boolean printSupplementary) {
		this.printSupplementary = printSupplementary;
	}
	public void setOnlinePaymentId(int onlinePaymentId) {
		this.onlinePaymentId = onlinePaymentId;
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
	
	public boolean isDisplayButton() {
		return displayButton;
	}
	public void setDisplayButton(boolean displayButton) {
		this.displayButton = displayButton;
	}
	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}
	
	/**
	 * 
	 */
	public void resetFields(){
//		this.supplementaryImprovement=null;
		//this.examId=null;
		this.registerNo=null;
		this.rollNo=null;
		//super.setCourseId(null);
		this.schemeNo=null;
		this.student=null;
		this.suppTo=null;
		this.studentId=0;
		this.suppImpAppAvailable=false;
		this.mainList=null;
		this.feesNotConfigured=false;
		this.theoryFees=0;
		this.practicalFees=0;
		this.totalFees=0;
		this.msg=null;
		this.finId=0;
		this.onlinePaymentId=0;
		this.printSupplementary=false;
		this.printData=null;
		this.displayButton=false;
		this.isExtended=false;
		super.setSmartCardNo(null);
		super.setValidThruMonth(null);
		super.setValidThruYear(null);
		this.dateOfApplication = null;
		this.applicationNumber = null;
		this.regularAppAvailable=false;
		this.supSubjectList=new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
		this.address=null;
		this.studentObj=null;
		this.courseDep=null;
		this.email="";
		this.mobileNo="";
		this.isAttendanceShortage=false;
		super.setNameOfStudent(null);
		super.setMonth(null);
		super.setYear(null);
		super.setOriginalDob(null);
		super.setCourseName(null);
		this.add=true;
		this.continue1=false;
		this.description=null;
		this.examType=null;
		this.supplementary=false;
		this.regExamFeesExempted=false;
		this.paymentSuccess=false;
		this.examType="Regular";
	}
	public boolean isExtended() {
		return isExtended;
	}
	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}
	public double getFineFees() {
		return fineFees;
	}
	public void setFineFees(double fineFees) {
		this.fineFees = fineFees;
	}
	public String getDateOfApplication() {
		return dateOfApplication;
	}
	public void setDateOfApplication(String dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public boolean isRegularAppAvailable() {
		return regularAppAvailable;
	}
	public void setRegularAppAvailable(boolean regularAppAvailable) {
		this.regularAppAvailable = regularAppAvailable;
	}

	
	public List<ExamSupplementaryImpApplicationSubjectTO> getSupSubjectList() {
		return supSubjectList;
	}
	public void setSupSubjectList(
			List<ExamSupplementaryImpApplicationSubjectTO> supSubjectList) {
		this.supSubjectList = supSubjectList;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Student getStudentObj() {
		return studentObj;
	}
	public void setStudentObj(Student studentObj) {
		this.studentObj = studentObj;
	}
	public String getCourseDep() {
		return courseDep;
	}
	public void setCourseDep(String courseDep) {
		this.courseDep = courseDep;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean getIsAttendanceShortage() {
		return isAttendanceShortage;
	}
	public void setIsAttendanceShortage(boolean isAttendanceShortage) {
		this.isAttendanceShortage = isAttendanceShortage;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	public boolean isContinue1() {
		return continue1;
	}
	public void setContinue1(boolean continue1) {
		this.continue1 = continue1;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isSupplementary() {
		return supplementary;
	}
	public void setSupplementary(boolean supplementary) {
		this.supplementary = supplementary;
	}
	
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getFeeConcessionCategory() {
		return feeConcessionCategory;
	}
	public void setFeeConcessionCategory(String feeConcessionCategory) {
		this.feeConcessionCategory = feeConcessionCategory;
	}
	public String getFeeConcession() {
		return feeConcession;
	}
	public void setFeeConcession(String feeConcession) {
		this.feeConcession = feeConcession;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCommunicationAddress() {
		return communicationAddress;
	}
	public void setCommunicationAddress(String communicationAddress) {
		this.communicationAddress = communicationAddress;
	}
	public String getCommunicationMobileNo() {
		return communicationMobileNo;
	}
	public void setCommunicationMobileNo(String communicationMobileNo) {
		this.communicationMobileNo = communicationMobileNo;
	}
	public String getCommunicationEmail() {
		return communicationEmail;
	}
	public void setCommunicationEmail(String communicationEmail) {
		this.communicationEmail = communicationEmail;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getCareTaker() {
		return careTaker;
	}
	public void setCareTaker(String careTaker) {
		this.careTaker = careTaker;
	}
	public String getPermanentAddressZipCode() {
		return permanentAddressZipCode;
	}
	public void setPermanentAddressZipCode(String permanentAddressZipCode) {
		this.permanentAddressZipCode = permanentAddressZipCode;
	}
	public String getCommunicationAddressZipCode() {
		return communicationAddressZipCode;
	}
	public void setCommunicationAddressZipCode(String communicationAddressZipCode) {
		this.communicationAddressZipCode = communicationAddressZipCode;
	}
	public boolean getChallanButton() {
		return challanButton;
	}
	public void setChallanButton(boolean challanButton) {
		this.challanButton = challanButton;
	}
	public String getSelectedFeePayment() {
		return selectedFeePayment;
	}
	public void setSelectedFeePayment(String selectedFeePayment) {
		this.selectedFeePayment = selectedFeePayment;
	}
	public boolean isOnlineApply() {
		return onlineApply;
	}
	public void setOnlineApply(boolean onlineApply) {
		this.onlineApply = onlineApply;
	}
	public String getJournalNo() {
		return journalNo;
	}
	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}
	public String getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getApplicationAmount() {
		return applicationAmount;
	}
	public void setApplicationAmount(String applicationAmount) {
		this.applicationAmount = applicationAmount;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getDdNo() {
		return ddNo;
	}
	public void setDdNo(String ddNo) {
		this.ddNo = ddNo;
	}
	public String getDdDrawnOn() {
		return ddDrawnOn;
	}
	public void setDdDrawnOn(String ddDrawnOn) {
		this.ddDrawnOn = ddDrawnOn;
	}
	public String getDdDate() {
		return ddDate;
	}
	public void setDdDate(String ddDate) {
		this.ddDate = ddDate;
	}
	public String getDdAmount() {
		return ddAmount;
	}
	public void setDdAmount(String ddAmount) {
		this.ddAmount = ddAmount;
	}	
	public Boolean getPaymentSuccess() {
		return paymentSuccess;
	}
	public void setPaymentSuccess(Boolean paymentSuccess) {
		this.paymentSuccess = paymentSuccess;
	}
	public String getApplicationAmount1() {
		return applicationAmount1;
	}
	public void setApplicationAmount1(String applicationAmount1) {
		this.applicationAmount1 = applicationAmount1;
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
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getMobileNo1() {
		return mobileNo1;
	}
	public void setMobileNo1(String mobileNo1) {
		this.mobileNo1 = mobileNo1;
	}
	public String getMobileNo2() {
		return mobileNo2;
	}
	public void setMobileNo2(String mobileNo2) {
		this.mobileNo2 = mobileNo2;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getAdditionalCharges() {
		return additionalCharges;
	}
	public void setAdditionalCharges(String additionalCharges) {
		this.additionalCharges = additionalCharges;
	}	
	public String getPaymentMail() {
		return paymentMail;
	}
	public void setPaymentMail(String paymentMail) {
		this.paymentMail = paymentMail;
	}
	public String getPgiStatus() {
		return pgiStatus;
	}
	public void setPgiStatus(String pgiStatus) {
		this.pgiStatus = pgiStatus;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getTxnRefNo() {
		return txnRefNo;
	}
	public void setTxnRefNo(String txnRefNo) {
		this.txnRefNo = txnRefNo;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public Boolean getIsTnxStatusSuccess() {
		return isTnxStatusSuccess;
	}
	public void setIsTnxStatusSuccess(Boolean isTnxStatusSuccess) {
		this.isTnxStatusSuccess = isTnxStatusSuccess;
	}
	public String getCandidateRefNo() {
		return candidateRefNo;
	}
	public void setCandidateRefNo(String candidateRefNo) {
		this.candidateRefNo = candidateRefNo;
	}
	public String getTransactionRefNO() {
		return transactionRefNO;
	}
	public void setTransactionRefNO(String transactionRefNO) {
		this.transactionRefNO = transactionRefNO;
	}
	public boolean isRegExamFeesExempted() {
		return regExamFeesExempted;
	}
	public void setRegExamFeesExempted(boolean regExamFeesExempted) {
		this.regExamFeesExempted = regExamFeesExempted;
	}
	public String getApplicationAmountWords() {
		return applicationAmountWords;
	}
	public void setApplicationAmountWords(String applicationAmountWords) {
		this.applicationAmountWords = applicationAmountWords;
	}
	
	public boolean getIsFine() {
		return isFine;
	}

	public void setIsFine(boolean isFine) {
		this.isFine = isFine;
	}
	
	public boolean getIsSuperFine() {
		return isSuperFine;
	}

	public void setIsSuperFine(boolean isSuperFine) {
		this.isSuperFine = isSuperFine;
	}
	/**
	 * @return the applicationFees
	 */
	public String getApplicationFees() {
		return applicationFees;
	}
	/**
	 * @param applicationFees the applicationFees to set
	 */
	public void setApplicationFees(String applicationFees) {
		this.applicationFees = applicationFees;
	}
	/**
	 * @return the cvCampFees
	 */
	public String getCvCampFees() {
		return cvCampFees;
	}
	/**
	 * @param cvCampFees the cvCampFees to set
	 */
	public void setCvCampFees(String cvCampFees) {
		this.cvCampFees = cvCampFees;
	}
	/**
	 * @return the marksListFees
	 */
	public String getMarksListFees() {
		return marksListFees;
	}
	/**
	 * @param marksListFees the marksListFees to set
	 */
	public void setMarksListFees(String marksListFees) {
		this.marksListFees = marksListFees;
	}
	/**
	 * @return the onlineServiceChargeFees
	 */
	public String getOnlineServiceChargeFees() {
		return onlineServiceChargeFees;
	}
	/**
	 * @param onlineServiceChargeFees the onlineServiceChargeFees to set
	 */
	public void setOnlineServiceChargeFees(String onlineServiceChargeFees) {
		this.onlineServiceChargeFees = onlineServiceChargeFees;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}
	
	public String getPrevClassId() {
		return prevClassId;
	}
	
	public void setPrevClassId(String prevClassId) {
		this.prevClassId = prevClassId;
	}
	
	public String getTotalFeesInWords() {
		return totalFeesInWords;
	}

	public void setTotalFeesInWords(String totalFeesInWords) {
		this.totalFeesInWords = totalFeesInWords;
	}

	public boolean getIsFeesExempted() {
		return isFeesExempted;
	}
	
	public void setIsFeesExempted(boolean isFeesExempted) {
		this.isFeesExempted = isFeesExempted;
	}
	/**
	 * @return the isAppliedAlready
	 */
	public boolean getIsAppliedAlready() {
		return isAppliedAlready;
	}
	/**
	 * @param isAppliedAlready the isAppliedAlready to set
	 */
	public void setIsAppliedAlready(boolean isAppliedAlready) {
		this.isAppliedAlready = isAppliedAlready;
	}
	/**
	 * @return the previousExam
	 */
	public boolean getPreviousExam() {
		return previousExam;
	}
	/**
	 * @param previousExam the previousExam to set
	 */
	public void setPreviousExam(boolean previousExam) {
		this.previousExam = previousExam;
	}
	/**
	 * @return the prevClassExamId
	 */
	public String getPrevClassExamId() {
		return prevClassExamId;
	}
	/**
	 * @param prevClassExamId the prevClassExamId to set
	 */
	public void setPrevClassExamId(String prevClassExamId) {
		this.prevClassExamId = prevClassExamId;
	}
	/**
	 * @return the examNameList
	 */
	public Map<Integer, String> getExamNameList() {
		return examNameList;
	}
	/**
	 * @param examNameList the examNameList to set
	 */
	public void setExamNameList(Map<Integer, String> examNameList) {
		this.examNameList = examNameList;
	}
	/**
	 * @return the classCodeIdsFrom
	 */
	public String getClassCodeIdsFrom() {
		return classCodeIdsFrom;
	}
	/**
	 * @param classCodeIdsFrom the classCodeIdsFrom to set
	 */
	public void setClassCodeIdsFrom(String classCodeIdsFrom) {
		this.classCodeIdsFrom = classCodeIdsFrom;
	}
	/**
	 * @return the mapClass
	 */
	public Map<Integer, String> getMapClass() {
		return mapClass;
	}
	/**
	 * @param mapClass the mapClass to set
	 */
	public void setMapClass(Map<Integer, String> mapClass) {
		this.mapClass = mapClass;
	}
	public boolean getRevAppAvailable() {
		return revAppAvailable;
	}
	public void setRevAppAvailable(boolean revAppAvailable) {
		this.revAppAvailable = revAppAvailable;
	}
	public String getIsRevaluation() {
		return isRevaluation;
	}
	public void setIsRevaluation(String isRevaluation) {
		this.isRevaluation = isRevaluation;
	}
	public String getIsScrutiny() {
		return isScrutiny;
	}
	public void setIsScrutiny(String isScrutiny) {
		this.isScrutiny = isScrutiny;
	}
	public int getRevclassid() {
		return revclassid;
	}
	public void setRevclassid(int revclassid) {
		this.revclassid = revclassid;
	}
	public double getRevaluationFees() {
		return revaluationFees;
	}
	public void setRevaluationFees(double revaluationFees) {
		this.revaluationFees = revaluationFees;
	}
	public double getScrutinyFees() {
		return scrutinyFees;
	}
	public void setScrutinyFees(double scrutinyFees) {
		this.scrutinyFees = scrutinyFees;
	}
	public double getOnlineSevriceFees() {
		return onlineSevriceFees;
	}
	public void setOnlineSevriceFees(double onlineSevriceFees) {
		this.onlineSevriceFees = onlineSevriceFees;
	}
	public String getChallanNo() {
		return challanNo;
	}
	public void setChallanNo(String challanNo) {
		this.challanNo = challanNo;
	}
	public String getRevExamId() {
		return revExamId;
	}
	public void setRevExamId(String revExamId) {
		this.revExamId = revExamId;
	}
	public String getIsChallengeRevaluation() {
		return isChallengeRevaluation;
	}
	public void setIsChallengeRevaluation(String isChallengeRevaluation) {
		this.isChallengeRevaluation = isChallengeRevaluation;
	}
	public double getChallengeRevaluationFees() {
		return challengeRevaluationFees;
	}
	public void setChallengeRevaluationFees(double challengeRevaluationFees) {
		this.challengeRevaluationFees = challengeRevaluationFees;
	}
	public int getPrevSemNo() {
		return prevSemNo;
	}
	public void setPrevSemNo(int prevSemNo) {
		this.prevSemNo = prevSemNo;
	}
	public boolean getAppliedAlready() {
		return appliedAlready;
	}
	public void setAppliedAlready(boolean appliedAlready) {
		this.appliedAlready = appliedAlready;
	}
	public boolean getAppliedThroughAdmin() {
		return isAppliedThroughAdmin;
	}
	public void setAppliedThroughAdmin(boolean isAppliedThroughAdmin) {
		this.isAppliedThroughAdmin = isAppliedThroughAdmin;
	}
	public String getSuppExamId() {
		return suppExamId;
	}
	public void setSuppExamId(String suppExamId) {
		this.suppExamId = suppExamId;
	}
	public String getSuppClassId() {
		return suppClassId;
	}
	public void setSuppClassId(String suppClassId) {
		this.suppClassId = suppClassId;
	}
	public void setAttendanceShortage(boolean isAttendanceShortage) {
		this.isAttendanceShortage = isAttendanceShortage;
	}
	public void setSuperFine(boolean isSuperFine) {
		this.isSuperFine = isSuperFine;
	}
	public List<StudentTO> getStuList() {
		return stuList;
	}
	public void setStuList(List<StudentTO> stuList) {
		this.stuList = stuList;
	}
	public Boolean getPrint() {
		return print;
	}
	public void setPrint(Boolean print) {
		this.print = print;
	}
	public String getTheoryFee() {
		return theoryFee;
	}
	public void setTheoryFee(String theoryFee) {
		this.theoryFee = theoryFee;
	}
	public String getUniversityFee() {
		return universityFee;
	}
	public void setUniversityFee(String universityFee) {
		this.universityFee = universityFee;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	public int getSuppImpClassId() {
		return suppImpClassId;
	}
	public void setSuppImpClassId(int suppImpClassId) {
		this.suppImpClassId = suppImpClassId;
	}
	public String getTotalRevaluationPaymentFees() {
		return totalRevaluationPaymentFees;
	}
	public void setTotalRevaluationPaymentFees(String totalRevaluationPaymentFees) {
		this.totalRevaluationPaymentFees = totalRevaluationPaymentFees;
	}
	public String geteGrandFees() {
		return eGrandFees;
	}
	public void seteGrandFees(String eGrandFees) {
		this.eGrandFees = eGrandFees;
	}
	public boolean isScrutiny() {
		return scrutiny;
	}
	public void setScrutiny(boolean scrutiny) {
		this.scrutiny = scrutiny;
	}
	public boolean isAppliedThroughAdmin() {
		return isAppliedThroughAdmin;
	}
	public void setFine(boolean isFine) {
		this.isFine = isFine;
	}
	public void setFeesExempted(boolean isFeesExempted) {
		this.isFeesExempted = isFeesExempted;
	}
}
