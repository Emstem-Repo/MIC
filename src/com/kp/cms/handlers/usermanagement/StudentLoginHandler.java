package com.kp.cms.handlers.usermanagement;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JTextArea;

import com.kp.cms.actions.admission.NewStudentCertificateCourseAction;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.CandidatePGIDetailsForStuSemesterFees;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.EvaStudentFeedbackOpenConnection;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.PcFinancialYear;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.bo.exam.CandidatePGIForSpecialFees;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.OnlinePaymentReciepts;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.bo.sap.UploadSAPMarksBo;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionActivityDetails;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admin.EvaluationStudentFeedbackForm;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.handlers.exam.DownloadHallTicketHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.helpers.admission.OnlineApplicationHelper;
import com.kp.cms.helpers.studentExtentionActivity.StudentExtentionFeedbackHelper;
import com.kp.cms.helpers.usermanagement.StudentLoginHelper;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admission.OnlinePaymentRecieptsTo;
import com.kp.cms.to.fee.FeePaymentTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;
import com.kp.cms.transactions.admin.ICertificateRequestOnlineTransaction;
import com.kp.cms.transactions.admission.IOnlineApplicationTxn;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionFeedbackTransaction;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionTransaction;
import com.kp.cms.transactions.usermanagement.IStudentLoginTransaction;
import com.kp.cms.transactionsimpl.admin.CertificateRequestOnlineImpl;
import com.kp.cms.transactionsimpl.admission.OnlineApplicationImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentExtentionFeedbackTransactionImpl;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentExtentionTransactionImpl;
import com.kp.cms.transactionsimpl.usermanagement.StudentLoginTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.OnlinePaymentUtils;
import com.kp.cms.utilities.PropertyUtil;

public class StudentLoginHandler {
	
	private static volatile StudentLoginHandler studentLoginHandler=null;
	public static StudentLoginHandler getInstance(){
		if(studentLoginHandler == null){
			studentLoginHandler = new StudentLoginHandler();
			return studentLoginHandler;
		}
		return studentLoginHandler;
	}
	 ICertificateRequestOnlineTransaction txn= new CertificateRequestOnlineImpl();
	   
	/**
	 * @param mobileNo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean saveMobileNo(String mobileNo,String userId,int personalId) throws Exception {
		boolean isAdded=false;
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		PersonalData studentData = iStudentLoginTransaction.getStudentPersonalData(personalId);
		PersonalData personalData=StudentLoginHelper.getInstance().copyFormToBO(mobileNo,userId,studentData);
		isAdded=iStudentLoginTransaction.saveMobileNo(personalData);
		return isAdded;
	}
	/**
	 * @param loginForm
	 * @return
	 * @throws Exception
	 */
	public LoginForm getMobileNo(LoginForm loginForm) throws Exception{
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		int personalId=loginForm.getPersonalDateId();
		PersonalData studentData = iStudentLoginTransaction.getStudentPersonalData(personalId);
		loginForm=StudentLoginHelper.getInstance().copyBoToForm(studentData);
		return loginForm;
	}
	/**
	 * Checks whether the student has paid through smart card payment mode
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean getStudentPaymentMode(int id,LoginForm loginForm) throws Exception {
		boolean isSmartCardPayment=false;
		IStudentLoginTransaction transaction=StudentLoginTransactionImpl.getInstance();
		List<FeePayment> feeList=transaction.getStudentPaymentMode(id);
		if(feeList!=null && !feeList.isEmpty()){
			isSmartCardPayment=true;
		}
		List<FeePaymentTO> feePaymentToList=StudentLoginHelper.getInstance().convertFeePaymentBOtoTO(feeList);
		loginForm.setFeeToList(feePaymentToList);
		return isSmartCardPayment;
	}
	/**
	 * getting the challan data to print
	 * @param loginForm
	 * @param request
	 * @throws Exception
	 */
	public void getChallanData(LoginForm loginForm, HttpServletRequest request) throws Exception {
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		int financialYear = 0;
		if (loginForm.getFinancialYear() != null) {
			financialYear = Integer.parseInt(loginForm.getFinancialYear());
		}
		FeePayment feePayment = transaction.getFeePaymentDetailsForEdit(Integer.parseInt(loginForm.getBillNo()), financialYear);
		StudentLoginHelper.getInstance().copyFeeChallanPrintData(feePayment,loginForm, request);
	}
	/**
	 * @param id
	 * @return
	 */
	public int getStudentGroupIdForCourse(int id) throws Exception {
		String groupId=PropertyUtil.getDataForUnique("select concat(c.ccGroup.id,'') from CCGroupCourse c where c.isActive=1 and c.ccGroup.isActive=1 and c.course.id="+id+" and '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between c.ccGroup.startDate and c.ccGroup.endDate");
		if(groupId!=null && !groupId.isEmpty())
			return Integer.parseInt(groupId);
		else
			return 0;
	}
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean checkOnlineReciepts(int id) throws Exception {
		List<OnlinePaymentReciepts> list=StudentLoginHelper.getInstance().getOnlineReciepts(id);
		if(list.isEmpty())
			return false;
		else
			return true;
			
	}
	/**
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public List<OnlinePaymentRecieptsTo> getOnlinePaymentReciepts(int studentId,HttpServletRequest request) throws Exception {
		List<OnlinePaymentReciepts> list=StudentLoginHelper.getInstance().getOnlineReciepts(studentId);
		return StudentLoginHelper.getInstance().convertOnlineRecieptToToList(list,request);
	}
	/**
	 * @param id
	 * @param session 
	 * @param specializationIds 
	 * @return
	 */
	public boolean isFacultyFeedbackAvailable(int id, HttpSession session, Map<Integer,String> specializationIds)throws Exception {
		boolean isAvailable = false;
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		EvaStudentFeedbackOpenConnection connections = transaction.isFacultyFeedbackAvailable(id,specializationIds);
		session.setAttribute("SESSIONID", null);
		if(connections!=null && !connections.toString().isEmpty()){
			session.setAttribute("SESSIONID", connections.getFeedbackSession().getId());
			isAvailable = true;
		}
		return isAvailable;
	}
	public boolean verifySmartCard(String smartCardNo, int studentId) throws Exception {
		String query="select s.smartCardNo from Student s where s.id="+studentId+" and s.smartCardNo like '%"+smartCardNo+"'";
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		List list=txn.getDataForQuery(query);
		if(list!=null && !list.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean saveSupplementaryApplicationForStudentLogin(LoginForm loginForm) throws Exception {
		OnlinePaymentReciepts onlinePaymentReciepts=new OnlinePaymentReciepts();
		PcFinancialYear pcFinancialYear=new PcFinancialYear();
		pcFinancialYear.setId(loginForm.getFinId());
		onlinePaymentReciepts.setPcFinancialYear(pcFinancialYear);
		Student student = new Student();
		student.setId(loginForm.getStudentId());
		onlinePaymentReciepts.setStudent(student);
		onlinePaymentReciepts.setTotalAmount(new BigDecimal(loginForm.getTotalFees()));
		onlinePaymentReciepts.setApplicationFor("Revaluation/Retotalling Application");
		onlinePaymentReciepts.setCreatedBy(loginForm.getUserId());
		onlinePaymentReciepts.setCreatedDate(new Date());
		onlinePaymentReciepts.setLastModifiedDate(new Date());
		onlinePaymentReciepts.setModifiedBy(loginForm.getUserId());
		onlinePaymentReciepts.setTransactionDate(new Date());
		onlinePaymentReciepts.setIsActive(true);
		PropertyUtil.getInstance().save(onlinePaymentReciepts);
		boolean isPaymentSuccess=false;
		if(onlinePaymentReciepts.getId()>0){
			loginForm.setOnlinePaymentId(onlinePaymentReciepts.getId());
			String registerNo=NewSecuredMarksEntryHandler.getInstance().getPropertyValue(loginForm.getStudentId(),"Student",true,"registerNo");
			OnlinePaymentUtils.dedactAmountFromAccount(registerNo,onlinePaymentReciepts.getId(), loginForm.getTotalFees(), onlinePaymentReciepts);
			if(!onlinePaymentReciepts.getIsPaymentFailed()/*true*/){
				isPaymentSuccess=true;
				txn.updateAndGenerateRecieptNoOnlinePaymentReciept(onlinePaymentReciepts);
			}else
				loginForm.setMsg(onlinePaymentReciepts.getStatus());
		}
		if(isPaymentSuccess){
			boolean isUpdate=DownloadHallTicketHandler.getInstance().saveRevaluationData(loginForm);
		return isUpdate;
		}else
			return false;
	}
	/**
	 * @param loginForm
	 * @return
	 * @throws Exception
	 */
	public Map<Integer,String> getSpecializationIds(LoginForm loginForm) throws Exception{
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		Map<Integer,String> map = new HashMap<Integer, String>();
		if(loginForm.getStudentId()!=0){
		int studentId = loginForm.getStudentId();
		map = transaction.getSpecializationIds(studentId);
		}
		return map;
	}
	public Map<Integer,String> getSpecializationIdsByStudentId(EvaluationStudentFeedbackForm evaStudentFeedbackForm) throws Exception{
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		Map<Integer,String> map = new HashMap<Integer, String>();
		if(evaStudentFeedbackForm.getStudentId()!=0){
		int studentId = evaStudentFeedbackForm.getStudentId();
		map = transaction.getSpecializationIds(studentId);
		}
		return map;
	}
	/**
	 * @param studentId
	 * @param courseId 
	 * @return
	 * @throws Exception
	 */
	public boolean checkHonoursCourse(int studentId, int courseId) throws Exception{
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		return transaction.checkHonoursCourse(studentId,courseId);
	}
	/**
	 * @param studentLogin
	 * @param session
	 * @param loginForm 
	 * @throws Exception
	 */
	public void setStudentDetailsToForm(StudentLogin studentLogin, HttpSession session, LoginForm loginForm) throws Exception{
		// set photo to session
		if(studentLogin.getStudent() != null && studentLogin.getStudent().getAdmAppln() != null && 
				studentLogin.getStudent().getAdmAppln().getApplnDocs()!=null){
			Iterator<ApplnDoc> docItr=studentLogin.getStudent().getAdmAppln().getApplnDocs().iterator();
			while (docItr.hasNext()) {
				ApplnDoc doc = docItr.next();
				if(doc.getName()!=null && doc.getIsPhoto() && session != null){
						//session.setAttribute("PhotoBytes", doc.getDocument());
					 FileOutputStream fos = new FileOutputStream(CMSConstants.STUDENT_PHOTO_PATH+"images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg");
					 fos.write(doc.getDocument());
					 fos.close();
					 CMSConstants.STUDENT_IMAGE = "images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg";
				}
			}
		}
		loginForm.setDisplaySem1and2("false");
		
		// set programID to form
		if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!=null &&
				 studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram() != null && 
				 studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType() != null){
			loginForm.setProgramTypeId(String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()));
		}
		
		if(studentLogin.getStudent().getAdmAppln().getAppliedYear() != null && studentLogin.getStudent().getAdmAppln().getAppliedYear()>0) {
			loginForm.setBatch(String.valueOf(studentLogin.getStudent().getAdmAppln().getAppliedYear()));
		}
		//List<LoginTransactionTo> loginTransactionList = LoginHandler.getInstance().getAccessableModules(loginForm);
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getDateOfBirth() != null) {
			loginForm.setDateOfBirth(CommonUtil.getStringDate(studentLogin.getStudent().getAdmAppln().getPersonalData().getDateOfBirth()));
		}
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getDateOfBirth() != null) {
			loginForm.setDob(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(studentLogin.getStudent().getAdmAppln().getPersonalData().getDateOfBirth()), NewStudentCertificateCourseAction.SQL_DATEFORMAT,NewStudentCertificateCourseAction.FROM_DATEFORMAT));
		}
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getEmail() != null){
			loginForm.setContactMail(studentLogin.getStudent().getAdmAppln().getPersonalData().getEmail());
		}
		if(studentLogin.getStudent().getBankAccNo()!=null){
			loginForm.setBankAccNo(studentLogin.getStudent().getBankAccNo());
		}
		if(studentLogin.getStudent().getRegisterNo()!=null){
			loginForm.setRegNo(studentLogin.getStudent().getRegisterNo());
		}
		//Get the student Name, Father Name, Mother Name and Class Name. Set all to form
		if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln()!=null
		&& studentLogin.getStudent().getAdmAppln().getPersonalData()!=null){
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getFatherName()!=null){
				loginForm.setFatherName(studentLogin.getStudent().getAdmAppln().getPersonalData().getFatherName());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getMotherName()!=null){
				loginForm.setMotherName(studentLogin.getStudent().getAdmAppln().getPersonalData().getMotherName());
			}
			String studentName = "";
			String phoneNo = "";
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName()!=null){
				studentName = studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName();
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getMiddleName()!=null){
				studentName = studentName + " " + studentLogin.getStudent().getAdmAppln().getPersonalData().getMiddleName();
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getLastName()!=null){
				studentName = studentName + " " + studentLogin.getStudent().getAdmAppln().getPersonalData().getLastName();
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressLine1()!=null){
				loginForm.setCurrentAddress1(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressLine1());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressLine2()!=null){
				loginForm.setCurrentAddress2(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressLine2());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getCityByCurrentAddressCityId()!=null){
				loginForm.setCurrentCity(studentLogin.getStudent().getAdmAppln().getPersonalData().getCityByCurrentAddressCityId());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByCurrentAddressStateId()!=null && studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByCurrentAddressStateId().getName()!=null){
				loginForm.setCurrentState(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByCurrentAddressStateId().getName());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByCurrentAddressStateId()==null && studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressStateOthers()!=null && !studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressStateOthers().isEmpty()){
				loginForm.setCurrentState(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressStateOthers());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressZipCode()!=null){
				loginForm.setCurrentPincode(studentLogin.getStudent().getAdmAppln().getPersonalData().getCurrentAddressZipCode());
			}
			//------------------------permanent Address--------------------------------------------------------------
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressLine1()!=null){
				loginForm.setPermanentAddress1(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressLine1());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressLine2()!=null){
				loginForm.setPermanentAddress2(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressLine2());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getCityByPermanentAddressCityId()!=null){
				loginForm.setPermanentCity(studentLogin.getStudent().getAdmAppln().getPersonalData().getCityByPermanentAddressCityId());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByPermanentAddressStateId()!=null && studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByPermanentAddressStateId().getName()!=null){
				loginForm.setPermanentState(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByPermanentAddressStateId().getName());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getStateByPermanentAddressStateId()==null && studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressStateOthers()!=null && !studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressStateOthers().isEmpty()){
				loginForm.setPermanentState(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressStateOthers());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressZipCode()!=null){
				loginForm.setPermanentPincode(studentLogin.getStudent().getAdmAppln().getPersonalData().getPermanentAddressZipCode());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getNationality()!=null && studentLogin.getStudent().getAdmAppln().getPersonalData().getNationality().getName()!=null){
				loginForm.setNationality(studentLogin.getStudent().getAdmAppln().getPersonalData().getNationality().getName());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getBloodGroup()!=null){
				loginForm.setBloodGroup(studentLogin.getStudent().getAdmAppln().getPersonalData().getBloodGroup());
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo1()!=null){
				phoneNo = studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo1();
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo2()!=null){
				phoneNo = phoneNo + "-" + studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo2();
			}
			if(studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo3()!=null){
				phoneNo = phoneNo + "-" + studentLogin.getStudent().getAdmAppln().getPersonalData().getPhNo3();
			}
			//-----------------------------------------------------------------------------------------------------------
			loginForm.setPhNo1(phoneNo);
			loginForm.setStudentName(studentName);
			loginForm.setStudentId(studentLogin.getStudent().getId());
			loginForm.setCourseId(String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId()));
			loginForm.setYear(Integer.toString(studentLogin.getStudent().getAdmAppln().getAppliedYear()));
			loginForm.setEnteredDob(null);
		}
		if(studentLogin.getStudent()!=null && studentLogin.getStudent().getClassSchemewise()!=null 
		&& studentLogin.getStudent().getClassSchemewise().getClasses()!=null && studentLogin.getStudent().getClassSchemewise().getClasses().getName()!=null){
			loginForm.setClassName(studentLogin.getStudent().getClassSchemewise().getClasses().getName());
			loginForm.setClassId(String.valueOf(studentLogin.getStudent().getClassSchemewise().getClasses().getId()));
			loginForm.setTermNo(studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber());
		}
		
		//code added by sudhir
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getMobileNo2()!=null){
			loginForm.setMobileNo(studentLogin.getStudent().getAdmAppln().getPersonalData().getMobileNo2());
		}
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getId()!=0){
			loginForm.setPersonalDateId(studentLogin.getStudent().getAdmAppln().getPersonalData().getId());
		}
		if(studentLogin.getStudent().getAdmAppln().getPersonalData().getUniversityEmail()!=null){
			loginForm.setUnivEmailId(studentLogin.getStudent().getAdmAppln().getPersonalData().getUniversityEmail());
		}
		//
		
	}
	/**
	 * @param admAppln
	 * @return
	 * @throws Exception 
	 */
	public boolean checkCourseIsMandatory(AdmAppln admAppln) throws Exception {
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		boolean isCourseMandatory = transaction.checkMandatoryCertificateCourse(admAppln);
		return isCourseMandatory;
	}

	public boolean isAppliedForSAPExam(int studentId)throws Exception{
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		boolean isApplied=iStudentLoginTransaction.checkingStudentIsAppliedForSAPExam(studentId);
	return isApplied;	
	}

	public UploadSAPMarksBo getSAPExamResuls(String studentId)throws Exception{
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		UploadSAPMarksBo bo=iStudentLoginTransaction.getSAPExamResults(studentId);
		return bo;
	}
	
	public List<Integer>  getAttendanceCondened(String studentId)throws Exception{
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		List<Integer> condonationClassIds = iStudentLoginTransaction.isAttendanceCondened(studentId);
		return condonationClassIds;
	}
	
	public boolean isDateValidForStudentExtention(int classId) throws Exception {
		IStudentExtentionFeedbackTransaction tx = StudentExtentionFeedbackTransactionImpl.getInstance();
		List<StudentExtentionFeedback> extentionFeedbacks = tx.getConnectionListBySessionId();
		Iterator<StudentExtentionFeedback> it = extentionFeedbacks.iterator();
		while(it.hasNext()) {
			StudentExtentionFeedback feedback = it.next();
			if(feedback.getClassId().getId() == classId) {
				return true;
			}
		}
		return false;
	}
	
	public List<StudentInstructionTO> getInstructions(LoginForm loginForm) throws Exception{
		List<StudentInstructionTO> instructionsTOList = new ArrayList<StudentInstructionTO>();
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		List<StudentInstructions> instructions = iStudentLoginTransaction.getInstructions();
		StudentInstructionTO to = null;
		if(instructions!=null){
			Iterator<StudentInstructions> iterator = instructions.iterator();
			while (iterator.hasNext()) {
				StudentInstructions studentFeedbackInstructions = (StudentInstructions) iterator .next();
				to = new StudentInstructionTO();
				to.setId(studentFeedbackInstructions.getId());
				to.setDescription(studentFeedbackInstructions.getDescription());
				loginForm.setInstruction(studentFeedbackInstructions.getDescription());
				instructionsTOList.add(to);
			}
		}
		return instructionsTOList;
	}
	
	public List<StudentGroupTO> getStudentGroup(LoginForm loginForm)throws Exception {
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		List<StudentGroup> studentgroup = iStudentLoginTransaction.getStudentGroupDetails(loginForm);
		List<StudentGroupTO> studentGroupTO =StudentLoginHelper.getInstance().convertTo(studentgroup);
		return studentGroupTO;
	}
	
	public List<StudentExtentionTO> getStudentExtention(LoginForm loginForm) throws Exception{
		IStudentLoginTransaction iStudentLoginTransaction = StudentLoginTransactionImpl.getInstance();
		List<StudentExtention> studentextention = iStudentLoginTransaction.getStudentExtentionDetails(loginForm);
		List<StudentExtentionTO> studentExtentionTO = StudentLoginHelper.getInstance().convertBOTOTO(studentextention);
	     return studentExtentionTO;
	}	
	
	public boolean checkExistingExtentionActivity(int studentId) throws Exception {
		IStudentExtentionTransaction tx = StudentExtentionTransactionImpl.getInstance();
		return tx.hasStudentAlreadySubmitedActivities(studentId);
	}
	
	public boolean saveStudentExtensions(LoginForm loginForm) throws Exception {
		List<StudentExtentionActivityDetails> activityDetails = StudentLoginHelper.getInstance().convertStudentActivitiesTOsToBos(loginForm);
		IStudentExtentionTransaction tx = StudentExtentionTransactionImpl.getInstance();
		return tx.saveStudentExtensionActivityDetails(activityDetails);
	}
	public String getParameterForPGI(LoginForm loginForm) throws Exception {
		CandidatePGIDetailsForStuSemesterFees bo = new CandidatePGIDetailsForStuSemesterFees();
		bo.setCandidateName(loginForm.getStudentName());
		
		
		if(loginForm.getCourseId()!=null && !loginForm.getCourseId().isEmpty()){
		Course course=new Course();
		course.setId(Integer.parseInt(loginForm.getCourseId()));
		bo.setCourse(course);
		}
		if(loginForm.getStudentId() != 0){
			Student student = new Student();
			student.setId(loginForm.getStudentId());
			bo.setStudent(student);
		}
		bo.setTxnStatus("Pending");
		bo.setTxnAmount(new BigDecimal(loginForm.getStudentSemesterFeesAmount()));
		bo.setMobileNo1(loginForm.getMobileNo());
		bo.setMobileNo2(loginForm.getMobileNo());
		bo.setEmail(loginForm.getContactMail());
		bo.setRefundGenerated(false);
		bo.setCreatedBy(loginForm.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(loginForm.getUserId());
		bo.setTermNo(loginForm.getTermNo());
		IStudentLoginTransaction transaction = new StudentLoginTransactionImpl();
		String candidateRefNo=transaction.generateCandidateRefNo(bo);
		StringBuilder temp=new StringBuilder();
		String productinfo="productinfo";
		loginForm.setRefNo(candidateRefNo);
		loginForm.setProductinfo(productinfo);
		//change the url of response in the msg below when it has to be released to the production. Please put the production IP
		if(candidateRefNo!=null && !candidateRefNo.isEmpty())
		 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
			temp.append(CMSConstants.PGI_MERCHANT_ID_STU).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_STU);
		//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
		//raghu write for pay e
		String hash=hashCal("SHA-512",temp.toString());
		loginForm.setTest(temp.toString());
		
		//if(checkSum!=null && !checkSum.isEmpty())
		// msg.append(temp).append("|").append(checkSum);
		return hash;
	}
	public String hashCal(String type,String str){
		byte[] hashseq=str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try{
		MessageDigest algorithm = MessageDigest.getInstance(type);
		algorithm.reset();
		algorithm.update(hashseq);
		byte messageDigest[] = algorithm.digest();
            
		

		for (int i=0;i<messageDigest.length;i++) {
			String hex=Integer.toHexString(0xFF & messageDigest[i]);
			if(hex.length()==1) hexString.append("0");
			hexString.append(hex);
		}
			
		}catch(NoSuchAlgorithmException nsae){ }
		
		return hexString.toString();


	}
	public boolean updateResponse(LoginForm loginForm)  throws Exception{
		boolean isUpdated=false;
		CandidatePGIDetailsForStuSemesterFees bo=StudentLoginHelper.getInstance().convertToBo(loginForm);
		IStudentLoginTransaction transaction=StudentLoginTransactionImpl.getInstance();
		isUpdated=transaction.updateReceivedStatus(bo,loginForm);
		return isUpdated;
	}
	public boolean paymentDone(LoginForm loginForm) throws Exception {
		boolean paymentDone = false;
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		paymentDone = txn.paymentDone(loginForm);
		return paymentDone;
	}
	public Student getStudentObj(String studentId) throws Exception {
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		Student student = txn.getStudentObj(studentId);
		return student;
	}
	public List<CandidatePGIDetailsExamRegular> getRegularList(String studentId) throws Exception {
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<CandidatePGIDetailsExamRegular> candidatePGIDetailsExamRegular = txn.getRegList(studentId);
		return candidatePGIDetailsExamRegular;
	}
	public List<CandidatePGIDetailsExamSupply> getSupplyList(String studentId) throws Exception {
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<CandidatePGIDetailsExamSupply> candidatePGIDetailsExamSupplies = txn.getSuppList(studentId);
		return candidatePGIDetailsExamSupplies;
	}
	public List<RevaluationApplicationPGIDetails> getRevaluationList(String studentId) throws Exception{
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<RevaluationApplicationPGIDetails> revaluationApplicationPGIDetails = txn.getRevList(studentId);
		return revaluationApplicationPGIDetails;
	}
	public String getDepartment(String courseId)  throws Exception{
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		String dept = null;
		CourseToDepartment courseToDepartment = txn.getDept(courseId);
		if(courseToDepartment != null){
			dept = courseToDepartment.getDepartment().getName();
		}else {
			dept = "-";
		}
		
		return dept;
	}
	public List<ExamRegularApplication> getRegPaymentList(String studentId)  throws Exception{
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<ExamRegularApplication> regList = txn.getRegPaymentList(studentId);
		return regList;
	}
	public List<ExamSupplementaryImprovementApplicationBO> getSuppPaymentList(String studentId)  throws Exception{
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<ExamSupplementaryImprovementApplicationBO> supList = txn.getSupPaymentList(studentId);
		return supList;
	}
	public List<PublishSpecialFeesTO> getPublishList(String classId) throws Exception {
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		List<PublishSpecialFees> list = txn.getPublishClassList(classId);
		List<PublishSpecialFeesTO> toList = StudentLoginHelper.getInstance().convertBOtoTOSpecial(list);
		return toList;
	}
	public SpecialFeesBO getTotalAmount(String classId) throws Exception {
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		SpecialFeesBO bo = txn.getData(classId);
		return bo;
	}
	public String getParameterForPGIForSpecial(LoginForm loginForm) throws Exception{
		CandidatePGIForSpecialFees bo = new CandidatePGIForSpecialFees();
		bo.setCandidateName(loginForm.getStudentName());
		
		
		if(loginForm.getClassId()!=null && !loginForm.getClassId().isEmpty()){
		Classes classes = new Classes();
		classes.setId(Integer.parseInt(loginForm.getClassId()));
		bo.setClasses(classes);
		}
		if(loginForm.getStudentId() != 0){
			Student student = new Student();
			student.setId(loginForm.getStudentId());
			bo.setStudent(student);
		}
		bo.setTxnStatus("Pending");
		bo.setTxnAmount(new BigDecimal(loginForm.getSpecialFeesAmount()));
		if(loginForm.getLateFineFees() != null){
			bo.setLateFineFees(new BigDecimal(loginForm.getLateFineFees()));
		}
		bo.setMobileNo1(loginForm.getMobileNo());
		bo.setMobileNo2(loginForm.getMobileNo());
		bo.setEmail(loginForm.getContactMail());
		bo.setRefundGenerated(false);
		bo.setCreatedBy(loginForm.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(loginForm.getUserId());
		IStudentLoginTransaction transaction = new StudentLoginTransactionImpl();
		String candidateRefNo=transaction.generateCandidateRefNoForSpecial(bo);
		StringBuilder temp=new StringBuilder();
		String productinfo="productinfo";
		loginForm.setRefNo(candidateRefNo);
		loginForm.setProductinfo(productinfo);
		//change the url of response in the msg below when it has to be released to the production. Please put the production IP
		if(candidateRefNo!=null && !candidateRefNo.isEmpty())
		 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
			temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
		//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
		//raghu write for pay e
		String hash=hashCal("SHA-512",temp.toString());
		loginForm.setTest(temp.toString());
		
		//if(checkSum!=null && !checkSum.isEmpty())
		// msg.append(temp).append("|").append(checkSum);
		return hash;
	}
	public boolean updateResponseForSpecial(LoginForm loginForm) throws Exception {
		boolean isUpdated=false;
		IStudentLoginTransaction transaction = StudentLoginTransactionImpl.getInstance();
		CandidatePGIForSpecialFees bo = StudentLoginHelper.getInstance().convertBOTO(loginForm);
		isUpdated = transaction.updateStatus(bo,loginForm);
		return isUpdated;
	}
	public boolean paymentDoneForSpecial(LoginForm loginForm) throws Exception {
		boolean paymentDone = false;
		IStudentLoginTransaction txn = new StudentLoginTransactionImpl();
		paymentDone = txn.paymentDoneForSpecial(loginForm);
		return paymentDone;
	}
}

