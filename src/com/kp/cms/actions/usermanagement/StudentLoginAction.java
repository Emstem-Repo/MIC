package com.kp.cms.actions.usermanagement;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.actions.admission.AdmissionFormAction;
import com.kp.cms.actions.admission.NewStudentCertificateCourseAction;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.StuCocurrLeaveDetails;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.bo.exam.ExamBlockUnblockHallTicketBO;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamFooterAgreementBO;
import com.kp.cms.bo.exam.ExamMidsemRepeatSetting;
import com.kp.cms.bo.exam.ExamPublishExamResultsBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.bo.sap.UploadSAPMarksBo;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.DuplicatePreferenceException;
import com.kp.cms.exceptions.ExcessPreferenceException;
import com.kp.cms.exceptions.MinimumPreferenceViolationException;
import com.kp.cms.exceptions.MissingPreferenceException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.fee.FeePrintReciept;
import com.kp.cms.forms.reports.StudentWiseAttendanceSummaryForm;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.handlers.admin.AttendanceGraphHandler;
import com.kp.cms.handlers.admin.EvaStudentFeedbackOpenConnectionHandler;
import com.kp.cms.handlers.admin.EvaluationStudentFeedbackHandler;
import com.kp.cms.handlers.admin.MaintenanceAlertHandler;
import com.kp.cms.handlers.admin.NewsEventsHandler;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admission.OnlineApplicationHandler;
import com.kp.cms.handlers.admission.StudentCertificateCourseHandler;
import com.kp.cms.handlers.admission.UploadInterviewSelectionHandler;
import com.kp.cms.handlers.attendance.StudentAttendanceSummaryHandler;
import com.kp.cms.handlers.exam.CertificateMarksCardHandler;
import com.kp.cms.handlers.exam.DownloadHallTicketHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.handlers.exam.ExamMidsemRepeatHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.handlers.exam.NewSupplementaryImpApplicationHandler;
import com.kp.cms.handlers.hostel.HostelLeaveHandler;
import com.kp.cms.handlers.reports.CiaOverallReportHandler;
import com.kp.cms.handlers.usermanagement.LoginHandler;
import com.kp.cms.handlers.usermanagement.StudentLoginHandler;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;
import com.kp.cms.to.admin.NewsEventsTO;
import com.kp.cms.to.admin.OrganizationTO;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.admin.SubjectTO;
import com.kp.cms.to.admission.OnlinePaymentRecieptsTo;
import com.kp.cms.to.exam.CandidatePaymentDetailsTO;
import com.kp.cms.to.exam.ClearanceCertificateTO;
import com.kp.cms.to.exam.ConsolidateMarksCardTO;
import com.kp.cms.to.exam.ExamInternalRetestApplicationSubjectsTO;
import com.kp.cms.to.exam.ExamMarksEntryDetailsTO;
import com.kp.cms.to.exam.HallTicketTo;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.to.exam.ShowMarksCardTO;
import com.kp.cms.to.exam.StudentMarkDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.pettycash.ICashCollectionTransaction;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.FalseExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.pettycash.CashCollectionTransactionImpl;
import com.kp.cms.transactionsimpl.usermanagement.StudentLoginTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.EncryptUtil;
import com.kp.cms.utilities.MarkComparator;
import com.kp.cms.utilities.PropertyUtil;


/**
 * @author christ
 *
 */
@SuppressWarnings("deprecation")
public class StudentLoginAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(StudentLoginAction.class);
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward studentLoginActionNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionMessage message = null;
		 ActionErrors errors = loginForm.validate(mapping, request);
		HttpSession session = request.getSession(true);
		loginForm.clearMideSemExam();
		System.out.println("Emstem");
		session.setAttribute("USERNAME", loginForm.getUserName());
		if (errors.isEmpty()) {
			try {
				loginForm.setEncryptedPassword(EncryptUtil.getInstance().encryptDES(loginForm.getPassword()));
				StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
				
				if (studentLogin == null) {
					message = new ActionMessage("knowledgepro.admin.validusername");
					messages.add("messages", message);
					addErrors(request, messages);
					loginForm.resetFields();
					if(CMSConstants.LINK_FOR_CJC)
					{ 
						return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
					}else
					{
					return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
					}
				} else {
//					setNewsData(loginForm); data added at server startup in StudentLoginLogo servlet
					loginForm.setDescription(CMSConstants.NEWS_DESCRIPTION);
					setDataToForm(loginForm,request);
					
					StudentLoginHandler.getInstance().setStudentDetailsToForm(studentLogin,session,loginForm);
					
					// this method used to display the student links based on some conditions
					
					displayStudentLoginLinks(studentLogin,session,loginForm);
					/* code added by sudhir */
					if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln()!=null){
						session.setAttribute("admApplnId", studentLogin.getStudent().getAdmAppln());
					}else{
						session.setAttribute("admApplnId", null);
					}
					/*added code ends here */
					
					
					//----------added by jismy for Hall Ticket
					String studentid = (String) session.getAttribute("studentid");
					int classId = 0;
					if(studentLogin.getStudent() != null && studentLogin.getStudent().getClassSchemewise() != null && 
							studentLogin.getStudent().getClassSchemewise().getClasses() != null && studentLogin.getStudent().getClassSchemewise().getClasses().getId() != 0){
						classId=studentLogin.getStudent().getClassSchemewise().getClasses().getId();
					}
					
					session.setAttribute("stuRegNo", loginForm.getRegNo());
					
					int examId = 0;
					if(classId > 0){
						examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm, "Hall Ticket");
					}
					session.setAttribute("examID", examId);
					loginForm.setExamId(examId);
					ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = null;
					if(examId>0){
						examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), classId, examId, "H");  
					}
					boolean isBlockedStudent = false;
					if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
						isBlockedStudent = true;
					}
					boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(classId, examId, "Hall Ticket", false);
					if(classId > 0 && examId > 0 && isDateValid){
						session.setAttribute("showHallTicket", true);
					}
					else{
						session.setAttribute("showHallTicket", false);
					}
					if(isBlockedStudent){
						session.setAttribute("isHallTicketBlockedStudent", true);
						session.setAttribute("hallTicketBlockReason", examBlockUnblockHallTicketBO.getBlockReason());
						session.setAttribute("blockHallTicketId", examBlockUnblockHallTicketBO.getId());
						boolean isLessPercentage=LoginHandler.getInstance().checkStudentAttendancePercentage(studentid,classId);
						session.setAttribute("isLessPercentage", isLessPercentage);
					}else{
						session.setAttribute("isHallTicketBlockedStudent", false);
					}
					//CandidatePGIDetailsExamRegular regbo= (CandidatePGIDetailsExamRegular) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("");
					//CandidatePGIDetailsExamSupply supboo= (CandidatePGIDetailsExamSupply) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("");
					session.removeAttribute("agreement");
					//Change By manu,1st check condition
					ExamFooterAgreementBO agreementBO=null;
					if(loginForm.getAgreementId()>0){
						agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
					}
					if(agreementBO!= null && agreementBO.getDescription()!= null){
						session.setAttribute("agreement", agreementBO.getDescription());
					}
					else{
						session.setAttribute("agreement", null);
					}
					//Added By Manu For Repeat MidSem Exam Apply Link
					if(loginForm.getTermNo()>1){
						session.setAttribute("previousAttendance", true);
					}else{
						session.setAttribute("previousAttendance", false);
					}
					loginForm.clearMideSemExam();
					session.setAttribute("linkForRepeatExamsApplication", false);
					session.setAttribute("linkForRepeatExamsFeePayment",false);
					session.setAttribute("linkFordownloadHallTicket", false);
					
   				ExamMidsemRepeatSetting checkForApplicationDate=ExamMidsemRepeatHandler.getInstance().checkForApplicationDate(loginForm);
   				String linkForRepeatExam=null;		
   				if(checkForApplicationDate!=null) {
					   linkForRepeatExam=ExamMidsemRepeatHandler.getInstance().checkForRepeatApplicationExam(studentLogin.getStudent().getId(),checkForApplicationDate.getMidSemExamId().getId(),loginForm);
					  if(linkForRepeatExam.equalsIgnoreCase("validData"))
					  session.setAttribute("linkForRepeatExamsApplication", true);
					  else if(linkForRepeatExam.equalsIgnoreCase("isApproved"))
						session.setAttribute("linkForRepeatExamsFeePayment",true);
					  }
					
					if(linkForRepeatExam!=null && linkForRepeatExam.equalsIgnoreCase("notValidData")){
						
						ExamMidsemRepeatSetting checkForFeesDate=ExamMidsemRepeatHandler.getInstance().checkForFeePaymentDate(loginForm);
						if(checkForFeesDate!=null) {
						  linkForRepeatExam=ExamMidsemRepeatHandler.getInstance().checkForRepeatFeesPaymentExam(studentLogin.getStudent().getId(),checkForFeesDate.getMidSemExamId().getId(),loginForm);
						 if(linkForRepeatExam.equalsIgnoreCase("hallTicket"))
						 session.setAttribute("linkFordownloadHallTicket", true);
						 else if(linkForRepeatExam.equalsIgnoreCase("isApproved"))
							session.setAttribute("linkForRepeatExamsFeePayment",true);
						  }
					}
					/*ExamMidsemRepeatSetting checkForFeesDate=ExamMidsemRepeatHandler.getInstance().checkForFeePaymentDate(loginForm);
						if(checkForFeesDate!=null) {
						  linkForRepeatExam=ExamMidsemRepeatHandler.getInstance().checkForRepeatFeesPaymentExam(studentLogin.getStudent().getId(),checkForFeesDate.getMidSemExamId().getId(),loginForm);
						 if(linkForRepeatExam.equalsIgnoreCase("hallTicket"))
						 session.setAttribute("linkFordownloadHallTicket", true);
						}*/
					  //endManu
					//----------------------
					showSupplementaryHallTcket( session,  classId,  loginForm, studentid);
					//------------
					loginForm.setRevaluationRegClassId(null);
					loginForm.setRevaluationSupClassId(null);
					
					//------show CIA Overall Report
					
					ExamPublishExamResultsBO examPublishExamResultsBO =  CiaOverallReportHandler.getInstance().getPublishedClassId(Integer.parseInt(studentid), classId);
			        if(examPublishExamResultsBO!= null && examPublishExamResultsBO.getClassId() > 0){
						session.setAttribute("showOverallReport", true);
					}
					else{
						session.setAttribute("showOverallReport", false);	
					}
			        if(loginForm.getProgramTypeId() != null &&  CMSConstants.UG_ID.contains(Integer.parseInt(loginForm.getProgramTypeId()))){
						session.setAttribute("birtUg", true);
					}else if(loginForm.getProgramTypeId() != null && CMSConstants.PG_ID.contains(Integer.parseInt(loginForm.getProgramTypeId()))){
						session.setAttribute("birtUg", false);
					}

					if(studentLogin.getStudent().getAdmAppln() != null && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!= null 
							&& session.getAttribute("birtUg") != null ){
						boolean isUg=(Boolean)session.getAttribute("birtUg");
						if(isUg && Integer.parseInt(loginForm.getYear())>=2011 && loginForm.getTermNo()<=4){
							/* code added by sudhir */
							boolean isMandatoryCourse = StudentLoginHandler.getInstance().checkCourseIsMandatory(studentLogin.getStudent().getAdmAppln());
							if(isMandatoryCourse){
								/* code added by sudhir */
								session.setAttribute("showCertCourse", true);
								session.setAttribute("courseId", studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
								session.setAttribute("studentId",studentLogin.getStudent().getId());
								/*int completedSubCount = StudentCertificateCourseHandler.getInstance().getCompletedCourseCount(Integer.parseInt(studentid),studentLogin.getStudent().getAdmAppln());
								int pendingCount = 0;
								if(2-completedSubCount > 0){
									pendingCount = 2-completedSubCount;
								}
								session.setAttribute("pendingCount", pendingCount);
								session.setAttribute("completedSubCount", completedSubCount);*/}else{
								session.setAttribute("showCertCourse", false);
							}
						}else{
							session.setAttribute("showCertCourse", false);
						}
					}else{
						session.setAttribute("showCertCourse", false);
					}
					
					if(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!= null){
						session.setAttribute("stuCourseId", String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId()));
					}
					if(studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName() != null){
						session.setAttribute("STUNAME",studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName());
					}
					// added for print challan link
					
					boolean isSmartCardMode=StudentLoginHandler.getInstance().getStudentPaymentMode(studentLogin.getStudent().getId(),loginForm);
					if(CMSConstants.LINK_FOR_CHALLAN_PRINT && isSmartCardMode){
						session.setAttribute("linkForPrintChallan", true);
					}else{
						session.setAttribute("linkForPrintChallan", false);
					}
					session.setAttribute("studentId",studentLogin.getStudent().getId());
					
						int groupId=StudentLoginHandler.getInstance().getStudentGroupIdForCourse(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
						if(groupId>0){
							String detainQuery="select s.student.id from ExamStudentDetentionRejoinDetails s " +
							"where (s.detain=1 or s.discontinued=1) and (s.rejoin = 0 or s.rejoin is null) and s.student.id="+studentLogin.getStudent().getId();
							List list=PropertyUtil.getInstance().getListOfData(detainQuery);
							if(list==null || list.isEmpty()){
								session.setAttribute("linkForCertificateCourse", true);
								session.setAttribute("studentRegNo",studentLogin.getStudent().getRegisterNo());
								if(studentLogin.getStudent().getClassSchemewise()!=null)
									session.setAttribute("studentsemNo",studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber());
							}
						}else
							session.setAttribute("linkForCertificateCourse", false);
					
					// code added by sudhir
					// checking in database whether the classid is their for today date to appear the faculty Feedback link 
						if(studentLogin.getStudent().getClassSchemewise()!=null){
							session.setAttribute("classSchemeWiseId", String.valueOf(studentLogin.getStudent().getClassSchemewise().getId()));
						}else{
							session.setAttribute("classSchemeWiseId",null);
						}
					session.setAttribute("ClassId", String.valueOf(studentLogin.getStudent().getClassSchemewise().getClasses().getId()));
					String classid = session.getAttribute("ClassId").toString();
					/*newly added */
					Map<Integer,String> specializationIds = StudentLoginHandler.getInstance().getSpecializationIds(loginForm);
					
					/*-----------*/
					boolean isFacultyFeedbackAvailable = StudentLoginHandler.getInstance().isFacultyFeedbackAvailable(Integer.parseInt(classid),session,specializationIds);
						if(isFacultyFeedbackAvailable){
							session.setAttribute("studentFacultyFeedback", true);
						}else{
							session.setAttribute("studentFacultyFeedback", false);
						}
						/*--------------- SAP EXAM REGISTRATION LINK DISPLAY--------------------------------- */
//						if the student is passed do not display the link . if failed display link as SAP Supplementry Exam.
						UploadSAPMarksBo bo=StudentLoginHandler.getInstance().getSAPExamResuls(studentid);
						if(bo!=null){
							if(bo.getStatus().equalsIgnoreCase("PASS") || bo.getStatus().equalsIgnoreCase("PASSED")){
								loginForm.setStatus("PASS");
							}else if(bo.getStatus().equalsIgnoreCase("Fail")|| bo.getStatus().equalsIgnoreCase("Failed")){
								loginForm.setStatus("FAIL");
							}
						}else{
							loginForm.setStatus("ALLOW");
						}
						/*---------------SAP EXAM REGISTRATION LINK DISPLAY Code ends Here  */
					if(studentLogin.getIsTempPassword()){
						session.setAttribute("tempPasswordLogin", true);
						return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS_PASSCHANGE);
					}else {	
						session.setAttribute("tempPasswordLogin", false);
						
							
							if(CMSConstants.LINK_FOR_CJC)
							{ 
								return mapping.findForward(CMSConstants.STUDENT_LOGIN_SUCCESS_CJC);
							}else
							{
							return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS);
							}
					}	
				}
			}catch(Exception e) {
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		}else {
			addErrors(request, errors);				
			if(CMSConstants.LINK_FOR_CJC)
			{ 
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
			}else
			{
			return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
			}
		}
	
	}
	
	/**
	 * @param studentLogin
	 * @param session
	 * @param loginForm 
	 * @throws Exception
	 */
	private void displayStudentLoginLinks(StudentLogin studentLogin, HttpSession session, LoginForm loginForm) throws Exception{
		
		
		// set data to session
		
		session.setAttribute("ChangePassword",CMSConstants.STUDENT_CHANGE_PASSWORD);
		session.setAttribute("LogoBytes", CMSConstants.LOGIN_LOGO);
		
		session.setAttribute("uid", String.valueOf(studentLogin.getId()));
		session.setAttribute("studentid", String.valueOf(studentLogin.getStudent().getId()));
		session.setAttribute("username", studentLogin.getUserName());
		session.setMaxInactiveInterval(CMSConstants.MAX_SESSION_INACTIVE_TIME);
		
		if(studentLogin.getRemarks() != null && !studentLogin.getRemarks().trim().isEmpty())
			session.setAttribute("studentAchievement", studentLogin.getRemarks());
		else
			session.setAttribute("studentAchievement", null);
		
		session.setAttribute("showAttendanceRep", true);
		// Code Added by Mary Job----- For Holistic Supplementary Exam------Link
		if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!=null){
			int programId=studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId();
			//if(programId!=4 && programId!=94 && programId!=81 && programId!=82 && programId!=74)	
			if(programId==3)
					session.setAttribute("linkForHolisticExam", true);
			else 
					session.setAttribute("linkForHolisticExam", false);
					
		}
	//	session.setAttribute("linkForHolisticExam", true);
		
		if(CMSConstants.SHOW_LINK.equalsIgnoreCase("true"))
			session.setAttribute("showLinks", true);
		else
			session.setAttribute("showLinks",false);
		
		if(CMSConstants.LINK_FOR_CJC){
			session.setAttribute("linkForCjc",true);
		}else{
			session.setAttribute("linkForCjc", false);
		}
		
		//Hostel Leave application 
		boolean hostelLinks = HostelLeaveHandler.getInstance().checkStudentDetails(studentLogin.getStudent().getId());
		if(hostelLinks){
			session.setAttribute("hostelLinks", true);
		}else{
			session.setAttribute("hostelLinks", false);
		}
		
		boolean isPrintAvailable=StudentLoginHandler.getInstance().checkOnlineReciepts(studentLogin.getStudent().getId());
		if(isPrintAvailable)
			session.setAttribute("linkForOnlineReciepts", true);
		else
			session.setAttribute("linkForOnlineReciepts", false);
		
		
		boolean finalStudent=false;
		if(studentLogin.getStudent() != null && studentLogin.getStudent().getId() !=0){
			if(CMSConstants.OPEN_HONOURSCOURS_LINK){
				if(studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber() == 4){
					boolean display = StudentLoginHandler.getInstance().checkHonoursCourse(studentLogin.getStudent().getId(),studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
					session.setAttribute("honoursLink", display);
				}else{
					session.setAttribute("honoursLink", false);
				}
			}else{
				session.setAttribute("honoursLink", false);
			}
			if(CMSConstants.CONVOCATION_REGISTRATION){
				finalStudent=CMSConstants.CONVOCATION_REGISTRATION;
			}
		}
	    //added by mahi
		session.setAttribute("linkForConsolidateMarksCard", false);
		if(finalStudent){
			boolean studentIsFinalYearOrNot=LoginHandler.getInstance().checkStudentIsFinalYearOrNot(loginForm,session);
			if(studentIsFinalYearOrNot){
				session.setAttribute("finalYearStudent", true);	
				if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD){
					session.setAttribute("linkForConsolidateMarksCard", true);
				}
			}else{
				session.setAttribute("finalYearStudent", false);	
			}
		}else{
			boolean studentIsFinalYearOrNot=LoginHandler.getInstance().checkStudentIsFinalYearOrNot(loginForm,session);
			if(studentIsFinalYearOrNot){
				if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD){
					session.setAttribute("linkForConsolidateMarksCard", true);
				}
			}
		}
	}

	/**isDateValid
	 * 
	 * @param mappingisDateValid
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward studentLoginAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the studentLoginAction");
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionMessage message = null;
		 ActionErrors errors = loginForm.validate(mapping, request);
	//if(!loginForm.getUserName().equalsIgnoreCase("User Name") && !loginForm.getPassword().equalsIgnoreCase("Password")){
		HttpSession session = request.getSession(true);
		loginForm.clearMideSemExam();
		session.setAttribute("USERNAME", loginForm.getUserName());
		if (errors.isEmpty()) {
			try {
				//loginForm.setEncryptedPassword(EncryptUtil.getInstance().encrypt(loginForm.getPassword()));
				loginForm.setEncryptedPassword(EncryptUtil.getInstance().encryptDES(loginForm.getPassword()));
				StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
				
				if (studentLogin == null) {
					message = new ActionMessage("knowledgepro.admin.validusername");
					messages.add("messages", message);
					addErrors(request, messages);
					loginForm.resetFields();
					if(CMSConstants.LINK_FOR_CJC)
					{ 
						return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
					}else
					{
						return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
					}
					
				} else {
//					List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
					
					setNewsData(loginForm);
					 setDataToForm(loginForm,request);
					AdmAppln admAppln = studentLogin.getStudent().getAdmAppln();
					/* code added by sudhir */
						if(admAppln!=null && !admAppln.toString().isEmpty()){
							session.setAttribute("admApplnId",admAppln.getId());
						}else{
							session.setAttribute("admApplnId", null);
						}
					/*added code ends here */
					// display achievements in notification panel
					if(studentLogin.getRemarks() != null && !studentLogin.getRemarks().trim().isEmpty())
						session.setAttribute("studentAchievement", studentLogin.getRemarks());
					else
						session.setAttribute("studentAchievement", null);
				//	if(CMSConstants.ATTENDANCE_DISP_COURSE_LIST.contains(admAppln.getCourseBySelectedCourseId().getId())){
						session.setAttribute("showAttendanceRep", true);
				//	}
				//	else{
				//		session.setAttribute("showAttendanceRep", false);
				//	}
					loginForm.setDisplaySem1and2("false");
					
					// set photo to session
					// all the photos moved to one folder.
					
					String STUDENT_IMAGE = "images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg?"+studentLogin.getStudent().getAdmAppln().getLastModifiedDate();
					session.setAttribute("STUDENT_IMAGE", STUDENT_IMAGE);
					
					/*if(admAppln.getApplnDocs()!=null){
						Iterator<ApplnDoc> docItr=admAppln.getApplnDocs().iterator();
						CMSConstants.STUDENT_IMAGE="";
						while (docItr.hasNext()) {
							ApplnDoc doc = docItr.next();
							if(doc.getName()!=null && doc.getIsPhoto() && session != null){
									session.setAttribute("PhotoBytes", doc.getDocument());
								 FileOutputStream fos = new FileOutputStream(CMSConstants.STUDENT_PHOTO_PATH+"images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg");
								 fos.write(doc.getDocument());
								 fos.close();
								 CMSConstants.STUDENT_IMAGE = "images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg";
							}
						}
					}*/
					if(CMSConstants.LINK_FOR_CJC){
						if(admAppln.getApplnDocs()!=null){
							Iterator<ApplnDoc> docItr=admAppln.getApplnDocs().iterator();
							while (docItr.hasNext()) {
								ApplnDoc doc = docItr.next();
								if(doc.getName()!=null && doc.getIsPhoto() && session != null && doc.getDocument() != null){
//										session.setAttribute("PhotoBytes", doc.getDocument());
									 FileOutputStream fos = new FileOutputStream(request.getRealPath("")+"/images/StudentPhotos/"+studentLogin.getStudent().getId()+".jpg");
									 fos.write(doc.getDocument());
									 fos.close();
								}
							}
						}
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
					if(studentLogin.getStudent().getAdmAppln().getPersonalData().getUniversityEmail()!=null){
						loginForm.setUnivEmailId(studentLogin.getStudent().getAdmAppln().getPersonalData().getUniversityEmail());
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
						session.setAttribute("studentName", studentName);
						loginForm.setStudentId(studentLogin.getStudent().getId());
						loginForm.setCourseId(String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId()));
						loginForm.setYear(Integer.toString(studentLogin.getStudent().getAdmAppln().getAppliedYear()));
						loginForm.setEnteredDob(null);
						session.setAttribute("courseId", String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId()));
					}
					if(studentLogin.getStudent()!=null && studentLogin.getStudent().getClassSchemewise()!=null 
					&& studentLogin.getStudent().getClassSchemewise().getClasses()!=null && studentLogin.getStudent().getClassSchemewise().getClasses().getName()!=null){
						loginForm.setClassName(studentLogin.getStudent().getClassSchemewise().getClasses().getName());
						loginForm.setClassId(String.valueOf(studentLogin.getStudent().getClassSchemewise().getClasses().getId()));
						loginForm.setTermNo(studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber());
						session.setAttribute("termNo", studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber());
					}
					// Code Added by Mary Job----- For Holistic Supplementary Exam------Link
					
					if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!=null){
						int programId=studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId();
						//if(programId!=3 && programId!=4 && programId!=94 && programId!=81 && programId!=82 && programId!=74)	
						if(programId==3)
								session.setAttribute("linkForHolisticExam", true);
						else 
								session.setAttribute("linkForHolisticExam", false);
								
					}
					if(studentLogin.getStudent().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType()!=null) {
						loginForm.setProgramTypeId(String.valueOf(studentLogin.getStudent().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()));
					}
					if(loginForm.getProgramTypeId().equalsIgnoreCase("1"))
						session.setAttribute("programType","UG");
					else
						session.setAttribute("programType","PG");
					
					if(studentLogin.getStudent()!=null && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!=null){
						int programTypeId=studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId();
						// commented by Nagarjuna.
				        // Consolidate Marks Card link should be displayed for all the courses 
						
						/*if(CMSConstants.UG_ID.contains(programTypeId)){
							session.setAttribute("birtUg", true);
							if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId() != 18){
								if(studentLogin.getStudent().getAdmAppln().getAppliedYear()==2009 && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=4
										&& studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=4
										&& studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=17
										&& studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=34)
									session.setAttribute("linkForConsolidateMarksCard", true);
								else if(studentLogin.getStudent().getAdmAppln().getAppliedYear()==2008 && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()==10)
									session.setAttribute("linkForConsolidateMarksCard", true);
								else
									session.setAttribute("linkForConsolidateMarksCard", false);
							}else{
								session.setAttribute("linkForConsolidateMarksCard", false);
							}
						}else if(CMSConstants.PG_ID.contains(programTypeId)){
							session.setAttribute("birtUg", false);
							if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD && ((studentLogin.getStudent().getAdmAppln().getAppliedYear()==2009 && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()==56) ||
									(studentLogin.getStudent().getAdmAppln().getAppliedYear()==2010 && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=56 && studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=3))){
								session.setAttribute("linkForConsolidateMarksCard", true);
							}else{
								session.setAttribute("linkForConsolidateMarksCard", false);
							}
						}*/
						loginForm.setProgramTypeId(String.valueOf(programTypeId));
					}
					
					if(CMSConstants.SHOW_LINK.equalsIgnoreCase("true"))
						session.setAttribute("showLinks", true);
					else
						session.setAttribute("showLinks",false);
					
					List<OrganizationTO> organisations=OrganizationHandler.getInstance().getOrganizationDetails();
					if(organisations!=null){
						Iterator<OrganizationTO> orgItr=organisations.iterator();
						while (orgItr.hasNext()) {
							OrganizationTO orgTO = (OrganizationTO) orgItr.next();
							session.setAttribute("ChangePassword",orgTO.getChangePassword());
							session.setAttribute("LogoBytes", orgTO.getLogo());
						}
					}
					
					session.setAttribute("uid", String.valueOf(studentLogin.getId()));
					session.setAttribute("studentid", String.valueOf(studentLogin.getStudent().getId()));
					session.setAttribute("username", studentLogin.getUserName());
					session.setMaxInactiveInterval(CMSConstants.MAX_SESSION_INACTIVE_TIME);
					
					
					//----------added by jismy for Hall Ticket
					String studentid = (String) session.getAttribute("studentid");
					int classId = DownloadHallTicketHandler.getInstance().getClassId(Integer.parseInt(studentid), loginForm);
					
					session.setAttribute("stuRegNo", loginForm.getRegNo());
					
					int examId = 0;
					if(classId > 0){
						examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm, "Hall Ticket");
					}
					session.setAttribute("examID", examId);
					loginForm.setExamId(examId);
					ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), classId, examId, "H");  
					boolean isBlockedStudent = false;
					if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
						isBlockedStudent = true;
					}
					int previousClassId=DownloadHallTicketHandler.getInstance().getPreClassId(Integer.parseInt(studentid), loginForm);
					if(previousClassId == 550 ||
					   previousClassId == 559 ||
					   previousClassId == 558 ||
					   previousClassId == 548 ||
					   previousClassId == 561 ||
					   previousClassId == 557 ||
					   previousClassId == 560 ||
					   previousClassId == 556 ||
					   previousClassId == 553 ||
					   previousClassId == 552 ||
					   previousClassId == 554 ||
					   previousClassId == 562 ||
					   previousClassId == 563 ||
					   previousClassId == 564 ||
					   previousClassId == 565 ||
					   previousClassId == 566 ||
					   previousClassId == 567 ||
					   previousClassId == 568 ||
					   previousClassId == 569 ||
					   previousClassId == 570 ||
					   previousClassId == 571 ||
					   previousClassId == 572 ||
					   previousClassId == 573 ||
					   previousClassId == 574 ||
					   previousClassId == 575 ||
					   previousClassId == 584 ||
					   previousClassId == 555 ||
					   //new
					   previousClassId==682 ||
					   previousClassId==681 ||
					   previousClassId==680 ||
					   previousClassId==679 ||
					   previousClassId==678 ||
					   previousClassId==677 ||
					   previousClassId==676 ||
					   previousClassId==675 ||
					   previousClassId==674 ||
					   previousClassId==673 ||
					   previousClassId==672 ||
					   previousClassId==671 ||
					   previousClassId==670 ||
					   previousClassId==669 ||
					   previousClassId==668 ||
					   previousClassId==667 ||
					   previousClassId==666 ||
					   previousClassId==665 ||
					   previousClassId==664 ||
					   previousClassId==663 ||
					   previousClassId==662 ||
					   previousClassId==661 ||
					   previousClassId == 699 ||
					   previousClassId == 700 ||
					   previousClassId == 701 ||
					   previousClassId == 702 ||
					   previousClassId == 703 ||
					   previousClassId == 704 ||
					   previousClassId == 705 ||
					   previousClassId == 706 ||
					   previousClassId == 707 ||
					   previousClassId == 711 ||
					   previousClassId == 716){
						
						//classId = previousClassId;
						//examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm, "Hall Ticket");
						session.setAttribute("examID", examId);
						session.setAttribute("stuRegNo", loginForm.getRegNo());
						loginForm.setExamId(examId);
						 examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), classId, examId, "H");  
						 isBlockedStudent = false;
						if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
							isBlockedStudent = true;
						}
					}
				
					boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(classId, examId, "Hall Ticket", false);
					if(classId > 0 && examId > 0 && isDateValid){
						session.setAttribute("showHallTicket", true);
					}
					else{
						session.setAttribute("showHallTicket", false);
					}
					
					
					if(isBlockedStudent){
						session.setAttribute("isHallTicketBlockedStudent", true);
						session.setAttribute("hallTicketBlockReason", examBlockUnblockHallTicketBO.getBlockReason());
						session.setAttribute("blockHallTicketId", examBlockUnblockHallTicketBO.getId());
						boolean isLessPercentage=LoginHandler.getInstance().checkStudentAttendancePercentage(studentid,classId);
						session.setAttribute("isLessPercentage", isLessPercentage);
					}
					else{
						session.setAttribute("isHallTicketBlockedStudent", false);
					}
					session.removeAttribute("agreement");
					//Change By manu,1st check condition
					ExamFooterAgreementBO agreementBO=null;
					if(loginForm.getAgreementId()>0){
					agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
					}
					if(agreementBO!= null && agreementBO.getDescription()!= null){
						session.setAttribute("agreement", agreementBO.getDescription());
					}
					else{
						session.setAttribute("agreement", null);
					}
					
					//----------------------
					
					showSupplementaryHallTcket( session,  classId,  loginForm, studentid);
					//------------
					loginForm.setRevaluationRegClassId(null);
					loginForm.setRevaluationSupClassId(null);
					
					// for supplementary revaluation application 
					
					showSupplementaryRevaluationApplication( session,  classId,  loginForm, studentid);

					
					int ciaCurClassId = CiaOverallReportHandler.getInstance().getClassId(Integer.parseInt(studentid));
					ExamPublishExamResultsBO examPublishExamResultsBO =  CiaOverallReportHandler.getInstance().getPublishedClassId(Integer.parseInt(studentid), ciaCurClassId);
			          if(examPublishExamResultsBO!= null && examPublishExamResultsBO.getClassId() > 0){
						session.setAttribute("showOverallReport", true);
					}
					else{
						session.setAttribute("showOverallReport", false);	
					}
			          if(loginForm.getProgramTypeId() != null &&  CMSConstants.UG_ID.contains(Integer.parseInt(loginForm.getProgramTypeId()))){
							session.setAttribute("birtUg", true);
						}else if(loginForm.getProgramTypeId() != null && CMSConstants.PG_ID.contains(Integer.parseInt(loginForm.getProgramTypeId()))){
							session.setAttribute("birtUg", false);
						}

			          session.setAttribute("programTypeId",loginForm.getProgramTypeId());
			          
					if(admAppln!= null && admAppln.getCourseBySelectedCourseId()!= null && session.getAttribute("birtUg") != null /*&&
							admAppln.getCourseBySelectedCourseId().getProgram().getStream()!= null && !admAppln.getCourseBySelectedCourseId().getProgram().getStream().trim().isEmpty()*/){
						boolean isUg=(Boolean)session.getAttribute("birtUg");
						if(isUg && Integer.parseInt(loginForm.getYear())>=2011 && loginForm.getTermNo()<=4){
							/* code added by sudhir*/
						boolean isMandatoryCourse = StudentLoginHandler.getInstance().checkCourseIsMandatory(admAppln);
							if(isMandatoryCourse){
								/* code added by sudhir */
								session.setAttribute("showCertCourse", true);
								session.setAttribute("courseId", studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
								session.setAttribute("studentId",studentLogin.getStudent().getId());
								
								/*int completedSubCount = StudentCertificateCourseHandler.getInstance().getCompletedCourseCount(Integer.parseInt(studentid),studentLogin.getStudent().getAdmAppln());
								int pendingCount = 0;
								if(2-completedSubCount > 0){
									pendingCount = 2-completedSubCount;
								}
								session.setAttribute("pendingCount", pendingCount);
								session.setAttribute("completedSubCount", completedSubCount);*/}else{
								session.setAttribute("showCertCourse", false);
								session.setAttribute("courseId", studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());

							}
						}else{
							session.setAttribute("showCertCourse", false);
						}
					}
					else{
						session.setAttribute("showCertCourse", false);
					}
					//added by priyatham start
					if(CMSConstants.LINK_FOR_CJC){
						session.setAttribute("linkForCjc",true);
					}else{
						session.setAttribute("linkForCjc", false);
					}
//					priyatham end
					//code added by sudhir
					if(studentLogin.getStudent().getAdmAppln().getPersonalData().getMobileNo2()!=null){
						loginForm.setMobileNo(studentLogin.getStudent().getAdmAppln().getPersonalData().getMobileNo2());
					}
					if(studentLogin.getStudent().getAdmAppln().getPersonalData().getId()!=0){
						loginForm.setPersonalDateId(studentLogin.getStudent().getAdmAppln().getPersonalData().getId());
					}
					//
					if(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId()!= null){
						session.setAttribute("stuCourseId", String.valueOf(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId()));
					}
					if(studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName() != null){
						session.setAttribute("STUNAME",studentLogin.getStudent().getAdmAppln().getPersonalData().getFirstName());
					}
					// added for print challan link
					
					boolean isSmartCardMode=StudentLoginHandler.getInstance().getStudentPaymentMode(studentLogin.getStudent().getId(),loginForm);
					if(CMSConstants.LINK_FOR_CHALLAN_PRINT && isSmartCardMode){
						session.setAttribute("linkForPrintChallan", true);
					}else{
						session.setAttribute("linkForPrintChallan", false);
					}
					session.setAttribute("studentId",studentLogin.getStudent().getId());
					
//					if(studentLogin.getStudent().getAdmAppln().getAppliedYear()!=null && studentLogin.getStudent().getAdmAppln().getAppliedYear()>=2011){
						int groupId=StudentLoginHandler.getInstance().getStudentGroupIdForCourse(studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
						if(groupId>0){
							String detainQuery="select s.student.id from ExamStudentDetentionRejoinDetails s " +
							"where (s.detain=1 or s.discontinued=1) and (s.rejoin = 0 or s.rejoin is null) and s.student.id="+studentLogin.getStudent().getId();
							List list=PropertyUtil.getInstance().getListOfData(detainQuery);
							if(list==null || list.isEmpty()){
							session.setAttribute("linkForCertificateCourse", true);
							session.setAttribute("studentRegNo",studentLogin.getStudent().getRegisterNo());
							if(studentLogin.getStudent().getClassSchemewise()!=null)
							session.setAttribute("studentsemNo",studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber());
							}
						}else
							session.setAttribute("linkForCertificateCourse", false);
					/*}else{
						session.setAttribute("linkForCertificateCourse", false);
					}*/
					boolean isPrintAvailable=StudentLoginHandler.getInstance().checkOnlineReciepts(studentLogin.getStudent().getId());
					if(isPrintAvailable)
						session.setAttribute("linkForOnlineReciepts", true);
					else
						session.setAttribute("linkForOnlineReciepts", false);
					// code added by sudhir
					// checking in database whether the classid is their for today date to appear the faculty Feedback link 
					if(studentLogin.getStudent().getClassSchemewise()!=null){
						session.setAttribute("classSchemeWiseId", String.valueOf(studentLogin.getStudent().getClassSchemewise().getId()));
					}else{
						session.setAttribute("classSchemeWiseId",null);
					}
					session.setAttribute("ClassId", String.valueOf(studentLogin.getStudent().getClassSchemewise().getClasses().getId()));
					//String classid = session.getAttribute("ClassId").toString();
					/*newly added */
					//Map<Integer,String> specializationIds = StudentLoginHandler.getInstance().getSpecializationIds(loginForm);
					
					/*-----------*/
					/*boolean isFacultyFeedbackAvailable = StudentLoginHandler.getInstance().isFacultyFeedbackAvailable(Integer.parseInt(classid),session,specializationIds);
						if(isFacultyFeedbackAvailable){
							session.setAttribute("studentFacultyFeedback", true);
						}else{
							session.setAttribute("studentFacultyFeedback", false);
					}*/
						//pavani 
						boolean isFacultyFeedbackAvailable=false;
						List<Integer> feedbackGivenClassIds;
						List<EvaStudentFeedbackOpenConnectionTo> finalClassIdList = new ArrayList<EvaStudentFeedbackOpenConnectionTo>();
						List<EvaStudentFeedbackOpenConnectionTo> toList = EvaStudentFeedbackOpenConnectionHandler.getInstance().getDetailsBySessionIdNew(request);
						feedbackGivenClassIds = EvaluationStudentFeedbackHandler.getInstance().getClassesNew(toList,Integer.parseInt(studentid));
						
						List<Integer> allClassIdsOfStud = EvaluationStudentFeedbackHandler.getInstance().getAllClassesOfStudent(Integer.parseInt(studentid));
						Iterator<EvaStudentFeedbackOpenConnectionTo> itr2 = toList.iterator();
						List<EvaStudentFeedbackOpenConnectionTo> finToList = new ArrayList<EvaStudentFeedbackOpenConnectionTo>();
						while(itr2.hasNext())
						{
							EvaStudentFeedbackOpenConnectionTo to = itr2.next();
							if(allClassIdsOfStud.contains(to.getClassesid())){
								finToList.add(to);
							}					
						}
						
						List<Integer> attCondonedClassIds = StudentLoginHandler.getInstance().getAttendanceCondened(studentid);
						if(feedbackGivenClassIds.size()==0){
							if(!attCondonedClassIds.isEmpty()){								
								Iterator<EvaStudentFeedbackOpenConnectionTo> itr = finToList.iterator();
								while(itr.hasNext()){
									EvaStudentFeedbackOpenConnectionTo to = itr.next();
									if(attCondonedClassIds.contains(to.getClassesid())){
									}else{
										finalClassIdList.add(to);
									}
								}		
							}else{
								finalClassIdList.addAll(finToList);
							}
							if(finalClassIdList.size()==0){
								isFacultyFeedbackAvailable=true;
							}else{
								isFacultyFeedbackAvailable=false;
							}
						}else{
							Iterator<EvaStudentFeedbackOpenConnectionTo> itr = finToList.iterator();
							while(itr.hasNext()){
								EvaStudentFeedbackOpenConnectionTo to = itr.next();
								if(feedbackGivenClassIds.contains(to.getClassesid())){
								}else{
									finalClassIdList.add(to);
								}
								if(!attCondonedClassIds.isEmpty()){								
									Iterator<EvaStudentFeedbackOpenConnectionTo> itr1 = finToList.iterator();
									while(itr1.hasNext()){
										EvaStudentFeedbackOpenConnectionTo to1 = itr1.next();
										if(attCondonedClassIds.contains(to1.getClassesid())){
										}else{
											if(feedbackGivenClassIds.contains(to.getClassesid())){
											}else{
												finalClassIdList.add(to);
											}
										}
									}		
								}
							}
							if(finalClassIdList.size()==0){
							 isFacultyFeedbackAvailable=true;
							}else{
								isFacultyFeedbackAvailable=false;
							}
						}
						
					
						//EvaluationStudentFeedbackHandler.getInstance().getStuFeedbackInstructionsNew(loginForm);
						
					boolean finalStudent=false;
					boolean isStudentPhotoUpload=false;
					if(studentLogin.getStudent() != null && studentLogin.getStudent().getId() !=0){
						Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
						if(organisation!=null){
							if(organisation.getOpenHonoursCourseLink()){
								if(studentLogin.getStudent().getClassSchemewise().getClasses().getTermNumber() == 4){
									boolean display = StudentLoginHandler.getInstance().checkHonoursCourse(studentLogin.getStudent().getId(),studentLogin.getStudent().getAdmAppln().getCourseBySelectedCourseId().getId());
									session.setAttribute("honoursLink", display);
								}else{
									session.setAttribute("honoursLink", false);
								}
							}else{
								session.setAttribute("honoursLink", false);
							}
							if(organisation.getConvocationRegistration()!=null){
								finalStudent=organisation.getConvocationRegistration();
							}
							if(organisation.getStudentPhotoUpload()!=null){
								isStudentPhotoUpload=organisation.getStudentPhotoUpload();
							}
						}else{
							session.setAttribute("honoursLink", false);
						}
					}
					//Added By Manu For Repeat MidSem Exam Apply Link
					if(loginForm.getTermNo()>1){
						session.setAttribute("previousAttendance", true);
					}else{
						session.setAttribute("previousAttendance", false);
					}
					loginForm.clearMideSemExam();
					session.setAttribute("linkForRepeatExamsApplication", false);
					session.setAttribute("linkForRepeatExamsFeePayment",false);
					session.setAttribute("linkFordownloadHallTicket", false);
					
					
				ExamMidsemRepeatSetting checkForApplicationDate=ExamMidsemRepeatHandler.getInstance().checkForApplicationDate(loginForm);
				ExamMidsemRepeatSetting checkForFeesDate=ExamMidsemRepeatHandler.getInstance().checkForFeePaymentDate(loginForm);
				String linkForRepeatExam=null;
				String FeePaid=null;
			//	String hallTicket=null;
				if(checkForFeesDate!=null)
				{
					FeePaid=ExamMidsemRepeatHandler.getInstance().checkForRepeatFeesPaymentExam(studentLogin.getStudent().getId(),checkForFeesDate.getMidSemExamId().getId(),loginForm);
				}//hallTicket= ExamMidsemRepeatHandler.getInstance().checkForRepeatHallticket(studentLogin.getStudent().getId(),loginForm);
				Date todayDate = new Date();
				Date feeEndDate=null;
				Date FeeEnddate=null;
				Date TodayDate=null;
				if(loginForm.getFeeEndDate()!=null && !loginForm.getFeeEndDate().isEmpty()){
					feeEndDate=CommonUtil.ConvertStringToDate(loginForm.getFeeEndDate());
				 FeeEnddate = CommonUtil.ConvertStringToDate(CommonUtil.formatDates(feeEndDate));
			     TodayDate = CommonUtil.ConvertStringToDate(CommonUtil.formatDates(todayDate));
				}
				if(checkForApplicationDate!=null) {
					   linkForRepeatExam=ExamMidsemRepeatHandler.getInstance().checkForRepeatApplicationExam(studentLogin.getStudent().getId(),checkForApplicationDate.getMidSemExamId().getId(),loginForm);
					  if(linkForRepeatExam.equalsIgnoreCase("validData"))
						  session.setAttribute("linkForRepeatExamsApplication", true);
					  else if(linkForRepeatExam.equalsIgnoreCase("isApproved") && ((FeeEnddate!=null && TodayDate!=null) &&  FeeEnddate.after(TodayDate)))
						session.setAttribute("linkForRepeatExamsFeePayment",true);
					  else if(linkForRepeatExam.equalsIgnoreCase("isApproved") && ((FeeEnddate!=null && TodayDate!=null) && FeeEnddate.equals(TodayDate)))
						session.setAttribute("linkForRepeatExamsFeePayment",true);
					  if(FeePaid!=null && !FeePaid.isEmpty()){
						  if(FeePaid.equalsIgnoreCase("hallTicket"))
						  session.setAttribute("linkFordownloadHallTicket", true);
					  }
				}
				else
				{	
					if(checkForFeesDate!=null){
						  if(FeePaid!=null && FeePaid.equalsIgnoreCase("hallTicket"))
							  session.setAttribute("linkFordownloadHallTicket", true); 
						  else if(FeePaid!=null && FeePaid.equalsIgnoreCase("isApproved"))
							session.setAttribute("linkForRepeatExamsFeePayment",true);
					 }
				}
					  //endManu
				    //added by mahi
					session.setAttribute("linkForConsolidateMarksCard", false);
					if(finalStudent){
						boolean studentIsFinalYearOrNot=LoginHandler.getInstance().checkStudentIsFinalYearOrNot(loginForm,session);
						if(studentIsFinalYearOrNot){
							session.setAttribute("finalYearStudent", true);	
							if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD){
								session.setAttribute("linkForConsolidateMarksCard", true);
							}
						}else{
							session.setAttribute("finalYearStudent", false);	
						}
					}else{
						boolean studentIsFinalYearOrNot=LoginHandler.getInstance().checkStudentIsFinalYearOrNot(loginForm,session);
						if(studentIsFinalYearOrNot){
							if(CMSConstants.LINK_FOR_CONSOLIDATE_MARKS_CARD){
								session.setAttribute("linkForConsolidateMarksCard", true);
							}
						}
					}
					
					//code added by chandra for checking SAP Resuls Link
					if(studentLogin.getStudent() != null && studentLogin.getStudent().getId() !=0){
						boolean isApplied=StudentLoginHandler.getInstance().isAppliedForSAPExam(studentLogin.getStudent().getId());
						if(isApplied){
							session.setAttribute("sapResultLinks", true);
						}else{
							session.setAttribute("sapResultLinks", false);
						}
						
					}
					//Hostel Leave application 
					boolean hostelLinks = HostelLeaveHandler.getInstance().checkStudentDetails(studentLogin.getStudent().getId());
					if(hostelLinks){
						session.setAttribute("hostelLinks", true);
					}else{
						session.setAttribute("hostelLinks", false);
					}
					/*--------------- SAP EXAM REGISTRATION LINK DISPLAY--------------------------------- */
//					if the student is passed do not display the link . if failed display link as SAP Supplementry Exam.
					UploadSAPMarksBo bo=StudentLoginHandler.getInstance().getSAPExamResuls(studentid);
					if(bo!=null){
						if(bo.getStatus().equalsIgnoreCase("PASS") || bo.getStatus().equalsIgnoreCase("PASSED")){
							loginForm.setStatus("PASS");
						}else if(bo.getStatus().equalsIgnoreCase("Fail") || bo.getStatus().equalsIgnoreCase("Failed")){
							loginForm.setStatus("FAIL");
						}
					}else{
						loginForm.setStatus("ALLOW");
					}
					/*---------------SAP EXAM REGISTRATION LINK DISPLAY Code ends Here  */
					
					//added by mahi start
					 loginForm.setServerDownMessage(null);
					 String maintenanceMessage =  MaintenanceAlertHandler.getInstance().getMaintenanceDetailsByDate();
					 if(maintenanceMessage!=null){
						 loginForm.setServerDownMessage(maintenanceMessage);
						 session.setAttribute("serverDownMessage", maintenanceMessage);
					 }
					 //end
					 if(isStudentPhotoUpload){
						    boolean finalYearStudent=LoginHandler.getInstance().checkStudentIsFinalYearOrNot(loginForm,session);
					     if(finalYearStudent){
								  loginForm.setStudentPhotoUpload(true);
						 }else{
								  loginForm.setStudentPhotoUpload(false);
	   						  }
					 }else{
					 	      loginForm.setStudentPhotoUpload(false);
					 }
					 classId = DownloadHallTicketHandler.getInstance().getClassId(Integer.parseInt(studentid), loginForm);
					 isDateValid = DownloadHallTicketHandler.isDateValidForOptionCourseApplication(classId);
						if (classId > 0 && isDateValid) {
							session.setAttribute("showCourseApp", true);
						} else {
							session.setAttribute("showCourseApp", false);
						}
					 
					 
					 List<Integer> examIds=NewSupplementaryImpApplicationHandler.getInstance().checkSuppImpAppAvailable(loginForm.getStudentId());
				
						if(examIds.isEmpty() || examIds==null){
							examIds=NewSupplementaryImpApplicationHandler.getInstance().checkSuppImpAppAvailable1(loginForm.getStudentId());
							if(examIds.isEmpty() || examIds==null){
								session.setAttribute("isSupplyImpAppDisplay", false);
								session.setAttribute("examIdList", null);
							}
							else{
								session.setAttribute("examIdList", examIds);
								session.setAttribute("isSupplyImpAppDisplay", true);
							}
						}
						else
						{
							session.setAttribute("examIdList", examIds);
							session.setAttribute("isSupplyImpAppDisplay", true);
						}
					 
					//added by vishnu/raghu
						String sectionForAtt = "";
						
						sectionForAtt =  request.getParameter("attendanceSession");
						if(sectionForAtt==null)
						{
							sectionForAtt = "am";
						}
						String jsonChart = AttendanceGraphHandler.getInstance().getAttendanceGraph(sectionForAtt,loginForm);
						session.setAttribute("jsonChart", jsonChart);
						session.setAttribute("sectionForAtt", sectionForAtt);						
						 
						 
						// for student login consolidate marks card
						int consolidateExamId=0;
						if (classId > 0) {
							consolidateExamId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm,"Consolidate Marks Card");
						}
						if(consolidateExamId>0)
							session.setAttribute("showConsolidateMarksCard",true);
						
						//raghu write new regular app
						if (classId > 0) {
							examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm,"Regular Application");
						}
							// if students did nt apply regular appln for prev exam do not allow for next exam
							int prevClassId=DownloadHallTicketHandler.getInstance().getPreClassId(Integer.parseInt(studentid), loginForm);
							boolean	appliedForPrevExam=false;
							int prevClassExamId=0;
							if(prevClassId>0){
								appliedForPrevExam= NewSupplementaryImpApplicationHandler.getInstance().getPrevClasssIdFromRegularApp(Integer.parseInt(studentid), prevClassId);
							}
							if(!appliedForPrevExam || appliedForPrevExam){
								prevClassExamId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(prevClassId, loginForm,"Regular Application");
					
							}
							
							session.setAttribute("prevClassId", prevClassId);	
							session.setAttribute("prevClassExamId", prevClassExamId);
							session.setAttribute("regAppExamId", examId);
							loginForm.setRegAppExamId(""+examId);
							examBlockUnblockHallTicketBO = null;
							if (prevClassExamId > 0) {
								if(!appliedForPrevExam)
								examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), prevClassId,prevClassExamId, "A");

							}
							examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), classId,examId, "A");

							boolean isBlockedStudentRegularApp = false;
							if (examBlockUnblockHallTicketBO != null && examBlockUnblockHallTicketBO.getId() > 0) {
								isBlockedStudentRegularApp = true;
							}
							if(!appliedForPrevExam){
								session.setAttribute("isPrevApplicationAvailable","yes");
								isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(prevClassId, examId,"Regular Application", false);
								if(isDateValid){
									appliedForPrevExam=true;
									
								}
							}
							isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(classId, examId,"Regular Application", false);
							if(appliedForPrevExam)
							session.setAttribute("isPrevApplicationAvailable","no");
							List<Integer> regClassList=new ArrayList(); 
							regClassList.add(699);regClassList.add(700);regClassList.add(701);regClassList.add(702);regClassList.add(703);regClassList.add(704);regClassList.add(705);
							regClassList.add(706);regClassList.add(707);regClassList.add(711);regClassList.add(716);
							
							if (classId > 0 && examId > 0 && isDateValid) {
								session.setAttribute("showRegApp", true);
							}else if(regClassList.contains(prevClassId)){
								session.setAttribute("showRegApp", true);
							}
							else {
								session.setAttribute("showRegApp", false);
							}
							if (isBlockedStudentRegularApp) {
								session.setAttribute("isBlockedStudentRegularApp",true);
								
								
							} else {
								session.setAttribute("isBlockedStudentRegularApp",false);
							}
							
							session.setAttribute("isStudent", "1");
							
							// Ashwini for revaluation

							int revExamId=0;
							int revClassId =0;
							int schemeNo =0;

							revClassId = DownloadHallTicketHandler.getInstance().getClassIdForRevaluation(Integer.parseInt(studentid), loginForm,false,"Revaluation/Scrutiny");
							revExamId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(revClassId, loginForm,"Revaluation/Scrutiny");
							schemeNo = loginForm.getTermNo();
							loginForm.setRevClassId(revClassId);
							// bring old class id
							if(revExamId==0){
								List<Integer> prevClassIds = new ArrayList<Integer>();
								int prevClassIdForRev = DownloadHallTicketHandler.getInstance().getPreClassId(Integer.parseInt(studentid), loginForm);
								prevClassIds.add(prevClassIdForRev);

								IDownloadHallTicketTransaction iDownloadHallTicketTransaction = DownloadHallTicketTransactionImpl.getInstance();
								List<Object> revalexamIds=iDownloadHallTicketTransaction.checkRevaluationAppAvailable(prevClassIds);


								if(revalexamIds.size()>0){
									Iterator<Object> itr = revalexamIds.iterator();
									while(itr.hasNext()){
										Object[] obj = (Object[]) itr.next();
										revExamId = Integer.parseInt(obj[0].toString());
										revClassId = Integer.parseInt(obj[1].toString());
										schemeNo = Integer.parseInt(obj[2].toString());

									}
								}
							}
					

							session.setAttribute("revclassId",revClassId);
							session.setAttribute("revAppExamId", revExamId);
							session.setAttribute("prevSemNo", schemeNo);
							loginForm.setRevClassId(revClassId);
							examBlockUnblockHallTicketBO = null;
							if (revExamId > 0) {
								examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), revClassId,revExamId, "A");
							}
						
							isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(revClassId, revExamId,"Revaluation/Scrutiny", false);
							if (revClassId > 0 && revExamId > 0 && isDateValid) {
								session.setAttribute("showRevApp", true);
							} else {
								session.setAttribute("showRevApp", false);
							}
							
							List<Integer> examIdss = NewSupplementaryImpApplicationHandler.getInstance().checkRevaluationAvailableForSupplementry(loginForm.getStudentId());
							if(examIdss.isEmpty() || examIdss==null){
								examIds=NewSupplementaryImpApplicationHandler.getInstance().checkRevaluationAvailableForSupplementry(loginForm.getStudentId());
								if(examIds.isEmpty() || examIds==null){
									session.setAttribute("isSupplyImpRevAppDisplay", false);
									session.setAttribute("supplyExamIdList", null);
								}
								else{
									session.setAttribute("supplyExamIdList", examIdss);
									session.setAttribute("isSupplyImpRevAppDisplay", true);
								}
							}
							else
							{
								session.setAttribute("supplyExamIdList", examIdss);
								session.setAttribute("isSupplyImpRevAppDisplay", true);
							}
							
							
							
							boolean isDateValidForExtentionActivity = StudentLoginHandler.getInstance().isDateValidForStudentExtention(Integer.parseInt(loginForm.getClassId()));
							if(isDateValidForExtentionActivity) {
								session.setAttribute("showExtentionLink", true);
							}
							else {
								session.setAttribute("showExtentionLink", false);
							}
							String previousClassIds=DownloadHallTicketHandler.getInstance().getPreviousClassIds(Integer.parseInt(studentid),classId);
							
							if(previousClassIds!=null &&  !previousClassIds.isEmpty()){
								boolean isDateValidForInternalMarksCard = DownloadHallTicketHandler.getInstance().checkDateIsValid(previousClassIds,"Internal Marks Card");
								if(isDateValidForInternalMarksCard) {
									session.setAttribute("showInternalMarksCard", true);
								}
								else {
									session.setAttribute("showInternalMarksCard", false);
								}
							}
							
						//code for student semester fees
							boolean isDateValidForStudentSemesterFees = DownloadHallTicketHandler.getInstance().checkDateValidation(loginForm.getClassId());
							boolean applicableCourse = false;
							if(loginForm.getCourseId().equalsIgnoreCase("22")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("10000");
							}else if(loginForm.getCourseId().equalsIgnoreCase("23")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("10000");
							}else if(loginForm.getCourseId().equalsIgnoreCase("26")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("12250");
							}else if(loginForm.getCourseId().equalsIgnoreCase("28")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("19750");
							}else if(loginForm.getCourseId().equalsIgnoreCase("29")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("12250");
							}

							else if(loginForm.getCourseId().equalsIgnoreCase("30")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("10000");
							}
							else if(loginForm.getCourseId().equalsIgnoreCase("31")){
								applicableCourse = true;
								loginForm.setStudentSemesterFeesAmount("12250");
							}if(isDateValidForStudentSemesterFees && applicableCourse){
								session.setAttribute("studentSemesterFees", true);
							}else {
								session.setAttribute("studentSemesterFees", false);
							}
							
							boolean isDateValidForSpecialFeeLink = DownloadHallTicketHandler.getInstance().checkDate(loginForm.getClassId());
							if(isDateValidForSpecialFeeLink){
								session.setAttribute("specialFeeLink", true);
							}else {
								session.setAttribute("specialFeeLink", false);
							}
							
					 //end by mahi
					if(studentLogin.getIsTempPassword()){
						session.setAttribute("tempPasswordLogin", true);
						return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS_PASSCHANGE);
					}else {	
						session.setAttribute("tempPasswordLogin", false);
						
						if(!isFacultyFeedbackAvailable){
							request.setAttribute("disableChangePasswordLink", true);
							return mapping.findForward(CMSConstants.EVAL_STU_FEEDBACK_FIRST_PAGE);	 
						}
						else{
							
							request.setAttribute("disableChangePasswordLink", false);
							if(CMSConstants.LINK_FOR_CJC)
							{ 
								return mapping.findForward(CMSConstants.STUDENT_LOGIN_SUCCESS_CJC);
							}
							else
							{
							return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS);
							}
						}
					}
					
					
					
					
					
					
				}
			} catch (ApplicationException e) {
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				//return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
				return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
			} catch(Exception e) {
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				//return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
				return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
				//throw e;
			}
		} else {
			log.debug("leaving the studentLoginAction with errors");
			addErrors(request, errors);		
			if(CMSConstants.LINK_FOR_CJC)
			{ 
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
			}else
			{
				return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
			}
		}
	}
	
	/**
	 * @param session
	 * @param classId
	 * @param loginForm
	 * @param studentid
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private void showRegularMarksCard(HttpSession session, int classId,
			LoginForm loginForm, String studentId) throws NumberFormatException, Exception {
		List<ShowMarksCardTO> regMarksCard=new ArrayList<ShowMarksCardTO>();
		int examId = 0;
		int count=1;
		List<Integer> classList = DownloadHallTicketHandler.getInstance().getRegularClassIds(Integer.parseInt(studentId), classId, true, "Marks Card");
		if(classList!=null && !classList.isEmpty()){
			for (Iterator iterator = classList.iterator(); iterator.hasNext();) {
				ShowMarksCardTO to=new ShowMarksCardTO();
				to.setCount(count);
				count+=1;
				Integer marksCardClassId = (Integer) iterator.next();
				if(marksCardClassId > 0){
					examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(marksCardClassId, loginForm, "Marks Card");
				}
				Integer semesterYearNo = DownloadHallTicketHandler.getInstance().getTermNumber(marksCardClassId);
				// Code commented by Balaji
//				session.setAttribute("supMCClassId", marksCardClassId);
//				session.setAttribute("supMCsemesterYearNo", semesterYearNo);
//				session.setAttribute("supMCexamID", examId);
				
				to.setRegMCClassId(marksCardClassId);
				to.setRegMCsemesterYearNo(semesterYearNo);
				to.setRegMCexamID(examId);
				
				session.setAttribute("examIDForMCard", examId);
				loginForm.setMarksCardClassId(marksCardClassId);
				loginForm.setSemesterYearNo(semesterYearNo);
				
				
				ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentId), marksCardClassId, examId, "M");  
				boolean isBlockedStudent = false;
				if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
					isBlockedStudent = true;
				}
				boolean isExcluded = DownloadHallTicketHandler.getInstance().getIsExcluded(Integer.parseInt(studentId), examId);
				boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(marksCardClassId, examId, "Marks Card", false);
				//boolean isAppeared = 
						DownloadHallTicketHandler.getInstance().isAppeared(Integer.parseInt(studentId), examId,marksCardClassId,"null");
				if(marksCardClassId > 0 && examId > 0 && isDateValid && !isExcluded){
//					session.setAttribute("showSupMC", true);
					to.setShowRegMC(true);
				}
				else{
//					session.setAttribute("showSupMC", false);
					to.setShowRegMC(false);
				}
				if(isBlockedStudent){
//					session.setAttribute("isSupMCBlockedStudent", true);
//					session.setAttribute("supMCBlockReason", examBlockUnblockHallTicketBO.getBlockReason());
					to.setRegMCBlockedStudent(true);
					to.setRegMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
				}
				else{
//					session.setAttribute("isSupMCBlockedStudent", false);
					to.setRegMCBlockedStudent(false);
				}	
				if(to.isShowRegMC()){
					regMarksCard.add(to);
				}
				//Change By manu,1st check condition
				ExamFooterAgreementBO agreementBO=null;
				if(loginForm.getAgreementId()>0){
				agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
				}
				if(agreementBO!= null && agreementBO.getDescription()!= null){
					session.setAttribute("marksCardAgreement", agreementBO.getDescription());
				}
				else{
					session.setAttribute("marksCardAgreement", null);
				}
			}
			session.setAttribute("regMarksCard", regMarksCard);
		}
	}

	public ActionForward studentLoginLoadData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(CMSConstants.LINK_FOR_CJC)
		{ 
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_SUCCESS_CJC);
		}else
		{
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS);
		}
	}
	public ActionForward studentLoginHallTicketAgreement(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CMSConstants.STUDENTLOGIN_AGREEMENT);
	}
	
	/**
	 * performs logout action.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward studentLogoutAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm = (LoginForm) form;
		
		HttpSession session= request.getSession(false);
		try {
			setNewsData(loginForm);
			boolean studnetLoginNew=false;
			if(session.getAttribute("studnetLoginNew")!=null && !session.getAttribute("studnetLoginNew").toString().isEmpty()){
				studnetLoginNew=(Boolean)session.getAttribute("studnetLoginNew");	
			}
			if (session != null) {
				session.invalidate();
			}
			Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
			if(organisation!=null){
				session= request.getSession(true);
				if(session.getAttribute("username")!=null){
					session.removeAttribute("username");
				}
				// set photo to session
				if(!studnetLoginNew){
					session.setAttribute("studnetLoginNew",false);
				if(organisation.getLogoContentType()!=null){
					if(session!=null){
						session.setAttribute(CMSConstants.KNOWLEDGEPRO_LOGO, organisation.getLogo());
						session.setAttribute(CMSConstants.KNOWLEDGEPRO_TOPBAR, organisation.getTopbar());
					}
				}
				}else{
					session.setAttribute("studnetLoginNew",true);
					if(organisation.getLogoContentType1()!=null){
						if(session!=null){
							session.setAttribute(CMSConstants.KNOWLEDGEPRO_LOGO, organisation.getLogo1());
							session.setAttribute(CMSConstants.KNOWLEDGEPRO_TOPBAR, organisation.getTopbar1());
						}
					}
				}
			}	
			loginForm.setServerDownMessage(null);
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		if(CMSConstants.LINK_FOR_CJC)
		{ 
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_LOGOUT_SUCCESS_CJC);
		}else
		{
			return mapping.findForward(CMSConstants.STUDENTLOGOUT_SUCCESS);
		}
	}
	
	public void setNewsData(LoginForm loginForm) throws Exception {
		StringBuffer description = new StringBuffer();
		NewsEventsTO eventsTO = null;
		try {
			List<NewsEventsTO> newsEventsList = NewsEventsHandler.getInstance()
					.getNewsEvents(CMSConstants.LOGIN_STUDENT);
			Iterator<NewsEventsTO> itr = newsEventsList.iterator();
			while (itr.hasNext()) {
				eventsTO = (NewsEventsTO) itr.next();
				description.append(eventsTO.getName()).append("<br/><br/>");
			}
			loginForm.setDescription(description.toString());
		
	   }catch (Exception e) {
		 log.debug(e.getMessage());
	  }
	}
	public ActionForward studentLoginMarksCardAgreement(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_AGREEMENT);
	}
	@SuppressWarnings("unchecked")
	public ActionForward studentLoginHallTicketBlock(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm=(LoginForm)form;
		if(loginForm.isSupHallTicket() && loginForm.getCount()>0){
			HttpSession session=request.getSession(false);
			List<ShowMarksCardTO> list=(ArrayList<ShowMarksCardTO>) session.getAttribute("supHallTicketList");
			for (ShowMarksCardTO to : list) {
				if(to.getCount()==loginForm.getCount()){
					session.setAttribute("hallTicketBlockReason",to.getSupMCBlockReason());
				}
			}
		}
		return mapping.findForward(CMSConstants.STUDENTLOGIN_HALL_TICKET_BLOCK_MES);
	}
	public ActionForward studentLoginMarksCardBlock(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_BLOCK_MES);
	}
	/**
	 * 
	 * @param session
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void showSupplementaryHallTcket(HttpSession session, int classId, LoginForm loginForm, String studentId) throws NumberFormatException, Exception{

		Map<Integer,List<Integer>> examClassMap=new HashMap<Integer, List<Integer>>();
		List<Integer> suppHallTcktClassIds=DownloadHallTicketHandler.getInstance().getSupplementaryClassIds(Integer.parseInt(studentId), classId, true, "Hall Ticket");
		List<Integer> classIds=null;
		String exams = "";
		for (Integer hallTcktClassId : suppHallTcktClassIds) {
			exams = "";
			if(hallTcktClassId > 0){
				exams = DownloadHallTicketHandler.getInstance().getExamIdByClassIdForSupHallTicket(hallTcktClassId, loginForm, "Hall Ticket");
			}
			if(!exams.isEmpty()){
				String[] examsId=exams.split(",");
				for (String examId : examsId) {
					if(examClassMap.containsKey(Integer.parseInt(examId))){
						classIds=examClassMap.remove(Integer.parseInt(examId));
					}else
						classIds=new ArrayList<Integer>();
					classIds.add(hallTcktClassId);
					examClassMap.put(Integer.parseInt(examId),classIds);
				}
			}
		}
		
		List<ShowMarksCardTO> supHallTicketList=new ArrayList<ShowMarksCardTO>();
		// map key is examId and value is list of classIds
		int count=1;
		int examId=0;
		for (Map.Entry<Integer, List<Integer>> entry : examClassMap.entrySet()) {
			examId=entry.getKey();
			ShowMarksCardTO to=new ShowMarksCardTO();
			to.setSupMCexamID(examId);
			to.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinitionBO",true,"name"));
			to.setCnt(count);
			count+=1;
			List<Integer> classesId=new ArrayList<Integer>();
			for (Integer hallTcktClassId : entry.getValue()) {
				ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentId), hallTcktClassId, examId, "H");  
				boolean isBlockedStudent = false;
				if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
					isBlockedStudent = true;
				}
				boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(hallTcktClassId, examId, "Hall Ticket", true);
				boolean isAppeared = DownloadHallTicketHandler.getInstance().isAppeared(Integer.parseInt(studentId), examId,hallTcktClassId,"hallTicket");
				if(hallTcktClassId > 0 && examId > 0 && isDateValid && isAppeared){
					classesId.add(hallTcktClassId);
					to.setShowSupMC(true);
				}
				if(isBlockedStudent){
					to.setSupMCBlockedStudent(true);
					to.setSupMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
				}
				//Change By manu,1st check condition
				ExamFooterAgreementBO agreementBO=null;
				if(loginForm.getAgreementId()>0){
				agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
				}
				if(agreementBO!= null && agreementBO.getDescription()!= null){
					to.setSupHallTicketagreement(agreementBO.getDescription());
				}
			}
			to.setClassIds(classesId);
			if(to.isShowSupMC()){
				supHallTicketList.add(to);
			}
		}
		session.setAttribute("supHallTicketList", supHallTicketList);
		
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward studentLoginHallTicketSuplementaryAgreement(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm=(LoginForm)form;
		if(loginForm.getCount()>0){
			HttpSession session=request.getSession(false);
			List<ShowMarksCardTO> list=(ArrayList<ShowMarksCardTO>) session.getAttribute("supHallTicketList");
			for (ShowMarksCardTO to : list) {
				if(to.getCnt()==loginForm.getCount()){
					session.setAttribute("agreement",to.getSupHallTicketagreement());
				}
			}
		}
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_HALL_AGREEMENT);
	}
	
	
	/**
	 * 
	 * @param session
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	public void showSupplementaryMarksCard(HttpSession session, int classId, LoginForm loginForm, String studentId) throws NumberFormatException, Exception{
		List<ShowMarksCardTO> supMarksCard=new ArrayList<ShowMarksCardTO>();
		int examId = 0;
		int count=1;
		List<Integer> classList = DownloadHallTicketHandler.getInstance().getSupplementaryClassIds(Integer.parseInt(studentId), classId, true, "Marks Card");
		if(classList!=null && !classList.isEmpty()){
			for (Iterator iterator = classList.iterator(); iterator.hasNext();) {
				ShowMarksCardTO to=new ShowMarksCardTO();
				to.setCnt(count);
				count+=1;
				Integer marksCardClassId = (Integer) iterator.next();
				if(marksCardClassId > 0){
					examId = DownloadHallTicketHandler.getInstance().getExamIdByClassIdForSupMarksCard(marksCardClassId, loginForm, "Marks Card");
				}
				Integer semesterYearNo = DownloadHallTicketHandler.getInstance().getTermNumber(marksCardClassId);
				// Code commented by Balaji
//				session.setAttribute("supMCClassId", marksCardClassId);
//				session.setAttribute("supMCsemesterYearNo", semesterYearNo);
//				session.setAttribute("supMCexamID", examId);
				
				to.setSupMCClassId(marksCardClassId);
				to.setSupMCsemesterYearNo(semesterYearNo);
				to.setSupMCexamID(examId);
				
				loginForm.setSupMCClassId(marksCardClassId);
				loginForm.setSupMCexamID(examId);
				loginForm.setSupMCsemesterYearNo(semesterYearNo);
				
				ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentId), marksCardClassId, examId, "M");  
				boolean isBlockedStudent = false;
				if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
					isBlockedStudent = true;
				}
				boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(marksCardClassId, examId, "Marks Card", true);
				boolean isAppeared = DownloadHallTicketHandler.getInstance().isAppeared(Integer.parseInt(studentId), examId,marksCardClassId,"marksCard");
				if(marksCardClassId > 0 && examId > 0 && isDateValid && isAppeared){
//					session.setAttribute("showSupMC", true);
					to.setShowSupMC(true);
				}
				else{
//					session.setAttribute("showSupMC", false);
					to.setShowSupMC(false);
				}
				if(isBlockedStudent){
//					session.setAttribute("isSupMCBlockedStudent", true);
//					session.setAttribute("supMCBlockReason", examBlockUnblockHallTicketBO.getBlockReason());
					to.setSupMCBlockedStudent(true);
					to.setSupMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
				}
				else{
//					session.setAttribute("isSupMCBlockedStudent", false);
					to.setSupMCBlockedStudent(false);
				}	
				if(to.isShowSupMC()){
					supMarksCard.add(to);
				}
			}
			session.setAttribute("supMarksCard", supMarksCard);
		}
		
	}
	
	public ActionForward studentLoginSupMarksCardBlock(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int count=Integer.parseInt(request.getParameter("count"));
		request.setAttribute("count",count);
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUPPLY_MARKS_CARD_BLOCK_MES);
	}
	
	public ActionForward getHallTicket(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		DownloadHallTicketHandler.getInstance().setProgramType(loginForm);
		HallTicketTo hallTickets=DownloadHallTicketHandler.getInstance().getHallticketForStudent(loginForm);
		loginForm.setHallTicket(hallTickets);
		loginForm.setHallticketBlock(null);
		CandidatePGIDetailsExamRegular regbo= (CandidatePGIDetailsExamRegular) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("from CandidatePGIDetailsExamRegular c where c.exam.id="+loginForm.getExamId()+" and c.student.id="+loginForm.getStudentId()+" and c.txnStatus='success'");
		CandidatePGIDetailsExamSupply supbo= (CandidatePGIDetailsExamSupply) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("from CandidatePGIDetailsExamSupply c where c.exam.id="+loginForm.getExamId()+" and c.student.id="+loginForm.getStudentId()+" and c.txnStatus='success'");
		if (regbo==null && supbo==null) {
			loginForm.setHallticketBlock("Exam fees Not payed");
		}
		return mapping.findForward(CMSConstants.STUDENTLOGIN_HALL_TICKET);
	}

	public ActionForward printHallTicket(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		//LoginForm loginForm = (LoginForm) form;
		return mapping.findForward(CMSConstants.STUDENTLOGIN_HALL_TICKET_PRINT);
	}
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getStudentMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm = (LoginForm) form;
		MarksCardTO marksCardTo=null;
		boolean isUg=false;
		if(request.getSession().getAttribute("birtUg")!=null)
		 isUg=(Boolean)request.getSession().getAttribute("birtUg");
		
		if(isUg){
			marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForUG(loginForm,request);
		}else{
			marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
		}
		if(loginForm.getBatch()!=null && loginForm.getProgramTypeId()!=null){
			if(loginForm.getBatch().equalsIgnoreCase("2012") && loginForm.getProgramTypeId().equalsIgnoreCase("3"))
				loginForm.setDisplaySem1and2("true");
		}
		loginForm.setMarksCardTo(marksCardTo);
		if(loginForm.getCourseId()!=null && !loginForm.getCourseId().equalsIgnoreCase("18")){
		IDownloadHallTicketTransaction transaction= new DownloadHallTicketTransactionImpl();
		List<ExamFooterAgreementBO> footer=transaction.getFooterDetails(loginForm.getProgramTypeId(),"M",loginForm.getClassId());
		if(footer!=null && !footer.isEmpty()){
			ExamFooterAgreementBO obj=footer.get(0);
			if(obj.getDescription()!=null)
				loginForm.setDescription1(obj.getDescription());
		}else{
			loginForm.setDescription1(null);
		}
		}else{
			loginForm.setDescription1(CMSConstants.MARKS_CARD_DESCRIPTION);
		}
		if(isUg){
			return mapping.findForward(CMSConstants.STUDENTLOGIN_UG_MARKS_CARD);
		}else{
			return mapping.findForward(CMSConstants.STUDENTLOGIN_PG_MARKS_CARD);
		}
	}
	
	
	public ActionForward printMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		boolean isUg=false;
		if(request.getSession().getAttribute("birtUg")!=null)
		 isUg=(Boolean)request.getSession().getAttribute("birtUg");
		
		if(isUg){
			return mapping.findForward(CMSConstants.PRINT_UG_MARKS_CARD);
		}else{
			if(CMSConstants.COURSEID_MTA.contains(Integer.parseInt(loginForm.getCourseId())))
				return mapping.findForward(CMSConstants.PRINT_PG_MARKS_CARD_MTA);
			else
				return mapping.findForward(CMSConstants.PRINT_PG_MARKS_CARD);
		}
	}
	
	
	public ActionForward getSuppHallTicket(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		ActionErrors errors = new ActionErrors();
		LoginForm loginForm = (LoginForm) form;
		DownloadHallTicketHandler.getInstance().setProgramType(loginForm);
		
		
		
		
		HallTicketTo hallTickets=DownloadHallTicketHandler.getInstance().getSupHallticketForStudent(loginForm,request);
		loginForm.setHallTicket(hallTickets);		
		IDownloadHallTicketTransaction transaction= new DownloadHallTicketTransactionImpl();
		List<ExamFooterAgreementBO> footer=transaction.getFooterDetails(loginForm.getProgramTypeId(),"H",loginForm.getClassId());
		if(footer!=null && !footer.isEmpty()){
			ExamFooterAgreementBO obj=footer.get(0);
			if(obj.getDescription()!=null)
				loginForm.setDescription1(obj.getDescription());
		}else{
			loginForm.setDescription1(null);
		}
		loginForm.setHallticketBlock(null);
		CandidatePGIDetailsExamRegular regbo= (CandidatePGIDetailsExamRegular) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("from CandidatePGIDetailsExamRegular c where c.exam.id="+hallTickets.getExamId()+" and c.student.id="+loginForm.getStudentId()+" and c.txnStatus='success'");
		CandidatePGIDetailsExamSupply supbo= (CandidatePGIDetailsExamSupply) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("from CandidatePGIDetailsExamSupply c where c.exam.id="+hallTickets.getExamId()+" and c.student.id="+loginForm.getStudentId()+" and c.txnStatus='success'");
		if (regbo==null && supbo==null) {
			loginForm.setHallticketBlock("Exam fees Not payed");
		}
		if(hallTickets==null)
		{
			/*ActionMessage message = new ActionMessage("knowledgepro.Exam.downloadHallTicket.examTimeTableError");
			messages.add("messages", message);
			saveMessages(request, messages);
			*/
			errors.add("error", new ActionError("knowledgepro.Exam.downloadHallTicket.examTimeTableError"));
			saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_HALL_TICKET);
	}

	public ActionForward printSuppHallTicket(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		//LoginForm loginForm = (LoginForm) form;
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_HALL_TICKET_PRINT);
	}
	
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getStudentSupMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm = (LoginForm) form;
		MarksCardTO marksCardTo=null;
		boolean isUg=false;
		if(request.getSession().getAttribute("birtUg")!=null)
		 isUg=(Boolean)request.getSession().getAttribute("birtUg");
		
		if(isUg){
			marksCardTo=DownloadHallTicketHandler.getInstance().getSupMarksCardForUG(loginForm,request);
		}else{
			marksCardTo=DownloadHallTicketHandler.getInstance().getSupMarksCardForPG(loginForm,request);
		}
		loginForm.setMarksCardTo(marksCardTo);
		IDownloadHallTicketTransaction transaction= new DownloadHallTicketTransactionImpl();
		List<ExamFooterAgreementBO> footer=transaction.getFooterDetails(loginForm.getProgramTypeId(),"M", loginForm.getClassId());
		if(footer!=null && !footer.isEmpty()){
			ExamFooterAgreementBO obj=footer.get(0);
			if(obj.getDescription()!=null)
				loginForm.setDescription1(obj.getDescription());
		}else{
			loginForm.setDescription1(null);
		}
		if(isUg){
			return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_UG_MARKS_CARD);
		}else{
			return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_PG_MARKS_CARD);
		}
	}
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printSupMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		boolean isUg=false;
		if(request.getSession().getAttribute("birtUg")!=null)
		 isUg=(Boolean)request.getSession().getAttribute("birtUg");
		
		if(isUg){
			return mapping.findForward(CMSConstants.PRINT_SUP_UG_MARKS_CARD);
		}else{
			if(CMSConstants.COURSEID_MTA.contains(Integer.parseInt(loginForm.getCourseId())))
				return mapping.findForward(CMSConstants.PRINT_SUP_PG_MARKS_CARD_MTA);
			else
				return mapping.findForward(CMSConstants.PRINT_SUP_PG_MARKS_CARD);
		}
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward returnHomePage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		if(CMSConstants.LINK_FOR_CJC)
		{ 
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_SUCCESS_CJC);
		}else
		{
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS);
		}
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward hallTicketClearanceCertificate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		ClearanceCertificateTO cto=DownloadHallTicketHandler.getInstance().getCleareanceCertificateForStudent(loginForm.getBlockId());
		loginForm.setCto(cto);
		return mapping.findForward(CMSConstants.HALLTICKET_CLEARANCE_CERTIFICATE);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printHallTicketClearanceCertificate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the getHallTicket");
		return mapping.findForward(CMSConstants.HALLTICKET_CLEARANCE_CERTIFICATE_PRINT);
	}
	
	public ActionForward marksCardClearanceCertificate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		ClearanceCertificateTO cto=DownloadHallTicketHandler.getInstance().getCleareanceCertificateForStudent(loginForm.getBlockId());
		loginForm.setCto(cto);
		return mapping.findForward(CMSConstants.MARKS_CARD_CLEARANCE_CERTIFICATE);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printMarksCardClearanceCertificate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the getHallTicket");
		return mapping.findForward(CMSConstants.MARKS_CARD_CLEARANCE_CERTIFICATE_PRINT);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitMobileNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the submitMobileNo");
		LoginForm loginForm = (LoginForm) form;
		setUserId(request, loginForm);
		//boolean isUpdate=false;
		try{
			String mobileNo=loginForm.getMobileNo();
			String userId=loginForm.getUserId();
			int personalId=loginForm.getPersonalDateId();
			//isUpdate=
					StudentLoginHandler.getInstance().saveMobileNo(mobileNo,userId,personalId);
			loginForm=StudentLoginHandler.getInstance().getMobileNo(loginForm);
			
		}catch (ApplicationException e) {
			log.debug("leaving the studentLoginAction with exception");
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		} catch(Exception e) {
			log.debug("leaving the studentLoginAction with exception");
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			//throw e;
		}
		
		if(CMSConstants.LINK_FOR_CJC)
		{ 
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_SUCCESS_CJC);
		}else
		{
		return mapping.findForward(CMSConstants.STUDENTLOGIN_SUCCESS);
		}
		
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward help(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(CMSConstants.STUDENTLOGIN_HELP);
	}
	/**
	 * Dispaly's the list of challan's generated through smart card mode for the previous month for the student
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initFeeChallanPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		LoginForm loginForm=(LoginForm)form;
		ActionErrors errors = new ActionErrors();
		StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
		boolean smartCardMode=StudentLoginHandler.getInstance().getStudentPaymentMode(studentLogin.getStudent().getId(),loginForm);
		if(!smartCardMode){
			errors.add("error",new ActionError(CMSConstants.KNOWLEDGEPRO_NORECORDS));
			saveErrors(request, errors);
		}
	 return mapping.findForward(CMSConstants.STUDENTLOGIN_PRINTCHALLAN_LIST);
 }
	
	/**
	 * print's challan for student
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printFeeChallan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
	 	log.debug("Entering printFeeChallan-studentLogin ");
	 	LoginForm loginForm = (LoginForm)form;
	 	try {
 			StudentLoginHandler.getInstance().getChallanData(loginForm, request);
 			
//	 		FeePaymentHandler.getInstance().copyPrintChallenData(feePaymentForm);
	 	} catch(Exception e) {
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
	 	}
	 	
		if(loginForm.getIsSinglePrint()){
	 		return mapping.findForward(CMSConstants.STUDENT_LOGIN_SINGLE_PRINTCHALLAN);
	 	}
	 	else
	 	{
	 		return mapping.findForward(CMSConstants.STUDENT_LOGIN_PRINTCHALLAN);
	 	}
	 	
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward certificateMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		ConsolidateMarksCardTO to=CertificateMarksCardHandler.getInstance().getStudentCertificateMarksCard(loginForm.getStudentId());
		if(to==null){
			return mapping.findForward(CMSConstants.CERTIFICATE_MARKS_CARD_NOT_AVALIABLE);
		}
		loginForm.setConsolidateMarksCardTO(to);
		return mapping.findForward(CMSConstants.CERTIFICATE_MARKS_CARD);
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printCertificateMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		return mapping.findForward(CMSConstants.STUDENTLOGIN_CERTIFICATE_MARKS_CARD_PRINT);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Entered ScoreSheetAction - getCandidates");
		ActionErrors errors = new ActionErrors();
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		errors = loginForm.validate(mapping, request);
		validateDDdetails(loginForm, errors);
		String forward=getMarksCardForwardName(loginForm.getMarksCardType());
		if (errors.isEmpty()) {
			try {
				//SmartCard Payment Code.....
				
				loginForm.setPrintCertificateReq(false);
				String msg="";
				boolean isSaved=StudentLoginHandler.getInstance().saveSupplementaryApplicationForStudentLogin(loginForm);
				msg=loginForm.getMsg();
				if(isSaved){
					//handler.sendSMSToStudent(loginForm.getStudentId(),CMSConstants.CERTIFICATE_APPLICATION_SMS_TEMPLATE);
					DownloadHallTicketHandler.getInstance().sendMailToStudent(loginForm);
					
					String mobileNo="91";
					if(loginForm.getMobileNo()!=null)
						mobileNo=mobileNo+loginForm.getMobileNo();
					
					if(mobileNo.length()==12){
						UploadInterviewSelectionHandler.getInstance().sendSMSToStudent(mobileNo,CMSConstants.SMS_REVALUATION_TEMPLATE,null);
					}
					ActionMessage message = new ActionMessage("KnowledgePro.Revaluation.added.successfully");
					messages.add("messages", message);
					saveMessages(request, messages);
					loginForm.clearRevaluation();
					MarksCardTO marksCardTo=null;
					if(loginForm.getBatch()!=null && loginForm.getProgramTypeId()!=null){
						if(loginForm.getBatch().equalsIgnoreCase("2012") && loginForm.getProgramTypeId().equalsIgnoreCase("3"))
							loginForm.setDisplaySem1and2("true");
					}
					if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_UG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForUG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_PG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_SUP_UG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_SUP_PG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}
					loginForm.setMarksCardTo(marksCardTo);
					//messages.add(CMSConstants.MESSAGES,new ActionMessage("knowledgepro.certificate.request.success.online"));
					//saveMessages(request, messages);
					//below is code for  Receipt print.......
					String printData=NewSupplementaryImpApplicationHandler.getInstance().getPrintData(loginForm.getOnlinePaymentId(),request);
					loginForm.resetFields();
					loginForm.setPrintData(printData);
					loginForm.setPrintCertificateReq(true);
					setDataToForm(loginForm,request);
				}else{
					if(msg==null || msg.isEmpty()){
						msg="Payment Failed";
						}
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message","Revaluation/Retotalling Application was not successfull, Reason:"+msg));
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message","Kindly rectify the errors mentioned and re-submit the application" ));
					saveErrors(request, errors);
				}
				
				//
				
				/*boolean isUpdate=DownloadHallTicketHandler.getInstance().saveRevaluationData(loginForm);
				if(isUpdate){
					DownloadHallTicketHandler.getInstance().sendMailToStudent(loginForm);
					
					String mobileNo="91";
					if(loginForm.getMobileNo()!=null)
						mobileNo=mobileNo+loginForm.getMobileNo();
					
					if(mobileNo.length()==12){
						UploadInterviewSelectionHandler.getInstance().sendSMSToStudent(mobileNo,CMSConstants.SMS_REVALUATION_TEMPLATE,null);
					}
					
					ActionMessage message = new ActionMessage("KnowledgePro.Revaluation.added.successfully");
					messages.add("messages", message);
					saveMessages(request, messages);
					loginForm.clearRevaluation();
					MarksCardTO marksCardTo=null;
					if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_UG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForUG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_PG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_SUP_UG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}else if(forward.equalsIgnoreCase(CMSConstants.STUDENTLOGIN_SUP_PG_MARKS_CARD)){
						marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
					}
					loginForm.setMarksCardTo(marksCardTo);
				}*/
			}catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(exception.getMessage());
			}
		} else {
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.STUDENTLOGIN_REVALUATION_PAGE);
		}
		log.info("Entered ScoreSheetAction - getCandidates");
		
		return mapping.findForward(forward);
	}

	/**
	 * @param marksCardType
	 * @return
	 */
	private String getMarksCardForwardName(String marksCardType) throws Exception {
		String forwardName="";
		if(marksCardType.equalsIgnoreCase("regUg")){
			forwardName=CMSConstants.STUDENTLOGIN_UG_MARKS_CARD;
		}else if(marksCardType.equalsIgnoreCase("regPg")){
			forwardName=CMSConstants.STUDENTLOGIN_PG_MARKS_CARD;
		}else if(marksCardType.equalsIgnoreCase("supUg")){
			forwardName=CMSConstants.STUDENTLOGIN_SUP_UG_MARKS_CARD;
		}else if(marksCardType.equalsIgnoreCase("supPg")){
			forwardName=CMSConstants.STUDENTLOGIN_SUP_PG_MARKS_CARD;
		}
		return forwardName;
	}

	/**
	 * @param loginForm
	 * @param errors
	 */
	private void validateDDdetails(LoginForm loginForm, ActionErrors errors) throws Exception{
		if (loginForm.getDdDate()!=null && !StringUtils.isEmpty(loginForm.getDdDate())) {
			if(CommonUtil.isValidDate(loginForm.getDdDate())){
			boolean	isValid = AdmissionFormAction.validatefutureDate(loginForm.getDdDate());
			if(!isValid){
				if (errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE)!=null && !errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE).hasNext()) {
					errors.add(CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE, new ActionError(CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE));
				}
			}
			}else{
				if (errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID)!=null && !errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID).hasNext()) {
					errors.add(CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID, new ActionError(CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID));
				}
			}
		}
			if(errors.isEmpty()){
				boolean isValid=DownloadHallTicketHandler.getInstance().checkValidDDdetails(loginForm);
				if(!isValid)
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message","DD Details Already Exists"));
			}
		
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitRevaluation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("Entered ScoreSheetAction - getCandidates");
		
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors = new ActionErrors();
		if (errors.isEmpty()) {
			try {
				List<SubjectTO> mainList=new ArrayList<SubjectTO>();
				MarksCardTO to=loginForm.getMarksCardTo();
				Map<String,List<SubjectTO>> subMap=to.getSubMap();
				if(subMap!=null && !subMap.isEmpty()){
					for (Map.Entry<String,List<SubjectTO>> entry : subMap.entrySet()) {
						List<SubjectTO> subList=entry.getValue();
						mainList.addAll(subList);
					}
				}
				
				Map<String,List<SubjectTO>> addOnCoursesubMap=to.getAddOnCoursesubMap();
				if(addOnCoursesubMap!=null && !addOnCoursesubMap.isEmpty()){
					for (Map.Entry<String,List<SubjectTO>> entry : addOnCoursesubMap.entrySet()) {
						List<SubjectTO> subList=entry.getValue();
						mainList.addAll(subList);
					}
				}
				Map<String,List<SubjectTO>> nonElectivesubMap=to.getNonElectivesubMap();
				if(nonElectivesubMap!=null && !nonElectivesubMap.isEmpty()){
					for (Map.Entry<String,List<SubjectTO>> entry : nonElectivesubMap.entrySet()) {
						List<SubjectTO> subList=entry.getValue();
						mainList.addAll(subList);
					}
				}
				to.setMainList(mainList);
				loginForm.setMarksCardTo(to);
			}catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(exception.getMessage());
			}
		} else {
			addErrors(request, errors);
		}
		log.info("Entered ScoreSheetAction - getCandidates");
		return mapping.findForward(CMSConstants.STUDENTLOGIN_REVALUATION_PAGE);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward backToMarksCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		String forward=getMarksCardForwardName(loginForm.getMarksCardType());
		return mapping.findForward(forward);
	}	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initOnlineRecieptsForStudentLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the initOnlineRecieptsForStudentLogin");
		LoginForm loginForm= (LoginForm) form;
		List<OnlinePaymentRecieptsTo> paymentList=StudentLoginHandler.getInstance().getOnlinePaymentReciepts(loginForm.getStudentId(),request);
		Collections.sort(paymentList);
		loginForm.setPaymentList(paymentList);
		log.debug("Exit  from initOnlineRecieptsForStudentLogin");
		return mapping.findForward(CMSConstants.INIT_ONLINE_RECIEPTS);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printOnlineRecieptsForStudentLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the initOnlineRecieptsForStudentLogin");
		LoginForm loginForm= (LoginForm) form;
		List<OnlinePaymentRecieptsTo> paymentList=loginForm.getPaymentList();
		for (OnlinePaymentRecieptsTo to : paymentList) {
			if(to.getCount()==loginForm.getCount()){
				request.setAttribute("MSG",to.getMsg());
			}
		}
		log.debug("Exit  from initOnlineRecieptsForStudentLogin");
		return mapping.findForward(CMSConstants.PRINT_ONLINE_RECIEPTS);
	}
	
	public ActionForward initMarksCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the initOnlineRecieptsForStudentLogin");
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionMessage message = null;
		 ActionErrors errors = loginForm.validate(mapping, request);
		cleanUpPageData(loginForm);
		if(request.getSession().getAttribute("marksCardBlockReason")!=null){
			request.getSession().removeAttribute("marksCardBlockReason");
		}
		//session.setAttribute("regMarksCard", regMarksCard);
		if(request.getSession().getAttribute("regMarksCard")!=null){
			request.getSession().removeAttribute("regMarksCard");
		}
		if(request.getSession().getAttribute("supMarksCard")!=null){
			request.getSession().removeAttribute("supMarksCard");
		}
		
		if(request.getSession().getAttribute("marksCardAgreement")!=null){
			request.getSession().removeAttribute("marksCardAgreement");
		}
		if (errors.isEmpty()) {
			try {
		StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
		
		if (studentLogin == null) {
			message = new ActionMessage("knowledgepro.admin.validusername");
			messages.add("messages", message);
			addErrors(request, messages);
			loginForm.resetFields();
			if(CMSConstants.LINK_FOR_CJC)
			{ 
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
			}else
			{
				return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
			}
		} else {		
			@SuppressWarnings("unused")
			List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
		// no need to excute this code if student is detained
		//if(!listOfDetainedStudents.contains(loginForm.getStudentId())){
			loginForm.setExamType("Regular");
			 List<MarksCardTO> SemExamListTo=(DownloadHallTicketHandler.getInstance().getExamSemList(loginForm));
			if(SemExamListTo!=null)	
			{
			Collections.sort(SemExamListTo,new MarkComparator());
			loginForm.setRegularExamList(SemExamListTo);
			}
		//}
		//Sup Marks Card
             List<MarksCardTO> SupSemList=(DownloadHallTicketHandler.getInstance().getSupExamSemList(loginForm));
			if(SupSemList!= null)
			Collections.sort(SupSemList,new MarkComparator());
			loginForm.setSuppExamList(SupSemList);
		
		log.debug("Exit  from initOnlineRecieptsForStudentLogin");
		return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
	}	
	} catch (ApplicationException e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
	} catch(Exception e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		//throw e;
	}
} else {
	log.debug("leaving the studentLoginAction with errors");
	addErrors(request, errors);	
	if(CMSConstants.LINK_FOR_CJC)
	{ 
		return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
	}else
	{
		return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
	}
}
}

	public ActionForward MarksCardDisplay(ActionMapping mapping, 
										  ActionForm form, 
										  HttpServletRequest request, 
										  HttpServletResponse response) throws Exception {
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors = loginForm.validate(mapping, request);
		HttpSession session = request.getSession(true);
		session.setAttribute("USERNAME", loginForm.getUserName());
		if (errors.isEmpty()) {
			try {
				LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
				String studentid = String.valueOf(loginForm.getStudentId());
				loginForm.setRevaluationRegClassId(null);
				int examId = 0;
				List<ShowMarksCardTO> regMarksCard=new ArrayList<ShowMarksCardTO>();
				List<MarksCardTO> regularList = loginForm.getRegularExamList();
				examId = Integer.parseInt(loginForm.getRegularExamId().substring(0, loginForm.getRegularExamId().indexOf('_')));
				if (regularList != null) {
					Iterator<MarksCardTO> iterator = regularList.iterator();
					while (iterator.hasNext()) {
						MarksCardTO mkTo = iterator.next();
						ShowMarksCardTO to=new ShowMarksCardTO();
						if (mkTo != null) {
							if(mkTo.getNewExamId().equalsIgnoreCase(loginForm.getRegularExamId())) {
						
								Integer semesterYearNo = Integer.parseInt(mkTo.getSemNo());
								Integer classId = Integer.parseInt(mkTo.getClassId());
								to.setRegMCsemesterYearNo(semesterYearNo);
								to.setRegMCexamID(examId);
								to.setRegMCClassId(classId);
								loginForm.setExamIDForMCard(examId);
								loginForm.setMarksCardClassId(classId);
						
								loginForm.setSemesterYearNo(semesterYearNo);
								boolean isBlockedStudent = false;
								ExamFooterAgreementBO agreementBO = null;
								if(classId > 0){
									examId = DownloadHallTicketHandler.getInstance().getExamIdByClassId(classId, loginForm, "Marks Card");
								}
								ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO= null;
								examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentid), classId, examId, "M");  
								isBlockedStudent = false;
								if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0) {
									isBlockedStudent = true;
									to.setRegMCBlockedStudent(true);
									to.setRegMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
									session.setAttribute("marksCardBlockReason", examBlockUnblockHallTicketBO.getBlockReason());
									session.setAttribute("marksCardBlockId", examBlockUnblockHallTicketBO.getId());
									return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_BLOCK_MES);
								}
								else {  //Change By manu,1st check condition
									if(loginForm.getAgreementId()>0){
										agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
									}
									if(agreementBO!= null && agreementBO.getDescription()!= null){
										session.setAttribute("marksCardAgreement", agreementBO.getDescription());
										
										if(!loginForm.isAgreementAccepted())
										{
										loginForm.setAgreementAccepted(true);
										return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_AGREEMENT_NEW);
										}
									}
									else{
										session.setAttribute("marksCardAgreement", null);
									}
									if((agreementBO!=null && loginForm.isAgreementAccepted())|| (agreementBO==null && !loginForm.isAgreementAccepted())) {
										to.setRegMCBlockedStudent(false);
										boolean isExcluded = DownloadHallTicketHandler.getInstance().getIsExcluded(Integer.parseInt(studentid), examId);
										if(classId > 0 && examId > 0 && !isExcluded){
											MarksCardTO marksCardTo=null;
											boolean isUg=false;
											if(request.getSession().getAttribute("birtUg")!=null)
												isUg=(Boolean)request.getSession().getAttribute("birtUg");
											if(isUg){
												marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForUG(loginForm,request);
											}else{
												marksCardTo=DownloadHallTicketHandler.getInstance().getMarksCardForPG(loginForm,request);
											}
											to.setShowRegMC(true);
											loginForm.setMarksCardTo(marksCardTo);
											loginForm.setDontShowPracticals(marksCardTo.isDontShowPracticals());
											IDownloadHallTicketTransaction transaction= new DownloadHallTicketTransactionImpl();
											List<ExamFooterAgreementBO> footer=transaction.getFooterDetails(loginForm.getProgramTypeId(),"M", loginForm.getClassId());
											if(footer!=null && !footer.isEmpty()){
												ExamFooterAgreementBO obj=footer.get(0);
												if(obj.getDescription()!=null)
													loginForm.setDescription1(obj.getDescription());
											}else{
												loginForm.setDescription1(null);
											}
											if(isUg){
												return mapping.findForward(CMSConstants.STUDENTLOGIN_UG_MARKS_CARD);
											}else{
												if(CMSConstants.COURSEID_MTA.contains(Integer.parseInt(loginForm.getCourseId())))
													return mapping.findForward(CMSConstants.STUDENTLOGIN_PG_MARKS_CARD_MTA);
												else
													return mapping.findForward(CMSConstants.STUDENTLOGIN_PG_MARKS_CARD);
											}
										} else{
											to.setShowRegMC(false);
											errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.KNOWLEDGEPRO_NORECORDS));
											saveErrors(request, errors);
											return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
										}
									}
								}
								if(to.isShowRegMC()){
									regMarksCard.add(to);
								}else if(!to.isShowRegMC() && isBlockedStudent){
									regMarksCard.add(to);
								}
							}
							session.setAttribute("regMarksCard", regMarksCard);
						}
					}
				}
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);	
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			} catch(Exception e) {
				e.printStackTrace();
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
				//throw e;
			}
		} else {
			log.debug("leaving the studentLoginAction with errors");
			cleanUpPageData(loginForm);
			//addErrors(request, errors);
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
		}
	}
	
	public ActionForward SupplementaryMarksCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
		LoginForm loginForm = (LoginForm) form;
		 ActionErrors errors = loginForm.validate(mapping, request);
		HttpSession session = request.getSession(true);
		session.setAttribute("USERNAME", loginForm.getUserName());
	if (errors.isEmpty()) {
		try {
		
		//StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
		String studentId = String.valueOf(loginForm.getStudentId());
		loginForm.setRevaluationSupClassId(null);
		
		List<ShowMarksCardTO> supMarksCard=new ArrayList<ShowMarksCardTO>();
		int examId = 0;
		List<MarksCardTO> supList = loginForm.getSuppExamList();
		String suppExamId =  loginForm.getSuppExamId();
		
		examId = Integer.parseInt(loginForm.getSuppExamId().substring(0, suppExamId.indexOf("-")));
		if (supList != null) {
			Iterator<MarksCardTO> iterator = supList.iterator();
			while (iterator.hasNext()) {
				MarksCardTO mkTo = iterator.next();
				ShowMarksCardTO to=new ShowMarksCardTO();
				if (mkTo != null) {
				if(Integer.parseInt(mkTo.getExamId())==examId){
					if(mkTo.getNewExamId().equalsIgnoreCase(loginForm.getSuppExamId())){
						Integer semesterYearNo = Integer.parseInt(mkTo.getSemNo());
						Integer classId = Integer.parseInt(mkTo.getClassId());
						to.setSupMCsemesterYearNo(semesterYearNo);
						to.setSupMCexamID(examId);
						to.setSupMCClassId(classId);
						loginForm.setSupMCexamID(examId);
						loginForm.setSupMCClassId(classId);
						loginForm.setSupMCsemesterYearNo(semesterYearNo);
						boolean isBlockedStudent = false;
						if(classId > 0){
							examId = DownloadHallTicketHandler.getInstance().getExamIdByClassIdForSupMarksCard(classId, loginForm, "Marks Card");
						}
						ExamFooterAgreementBO agreementBO = null;
						ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO= null;
						 examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentId), classId, examId, "M");  
						 isBlockedStudent = false;
						if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
							isBlockedStudent = true;
							to.setSupMCBlockedStudent(true);
							to.setSupMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
							return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_BLOCK_MES);
						}
						else 
						{   

							//Change By manu,1st check condition
							if(loginForm.getAgreementId()>0){
							agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
							}
							if(agreementBO!= null && agreementBO.getDescription()!= null){
							session.setAttribute("marksCardAgreement", agreementBO.getDescription());
							if(!loginForm.isAgreementAccepted())
							{
							loginForm.setAgreementAccepted(true);
							return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_AGREEMENT_SUPP);
							}
							}
						else{
							session.setAttribute("marksCardAgreement", null);
							}
						if((agreementBO!=null && loginForm.isAgreementAccepted())|| (agreementBO==null && !loginForm.isAgreementAccepted()))
						{
						//isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(classId, examId, "Marks Card", true);
						boolean isAppeared = DownloadHallTicketHandler.getInstance().isAppeared(Integer.parseInt(studentId), examId,classId,"marksCard");
						//if(classId > 0 && examId > 0 && isDateValid && isAppeared){
						if(classId > 0 && examId > 0 && isAppeared){
							to.setShowSupMC(true);
							MarksCardTO marksCardTo=null;
							boolean isUg=false;
							if(request.getSession().getAttribute("birtUg")!=null)
							 isUg=(Boolean)request.getSession().getAttribute("birtUg");
							
							if(isUg){
								marksCardTo=DownloadHallTicketHandler.getInstance().getSupMarksCardForUG(loginForm,request);
							}else{
								marksCardTo=DownloadHallTicketHandler.getInstance().getSupMarksCardForPG(loginForm,request);
							}
							loginForm.setMarksCardTo(marksCardTo);
							IDownloadHallTicketTransaction transaction= new DownloadHallTicketTransactionImpl();
							ExamDefinitionBO examDefinitionBO = transaction.getObj(loginForm);
							if(examDefinitionBO != null && !examDefinitionBO.toString().isEmpty()){
								if(examDefinitionBO.getIsImprovement()== true){
									loginForm.setIsImpReap(true);
								}else if(examDefinitionBO.getIsReappearance() == true){
									loginForm.setIsImpReap(false);
								}
							}
							List<ExamFooterAgreementBO> footer=transaction.getFooterDetails(loginForm.getProgramTypeId(),"M", loginForm.getClassId());
							if(footer!=null && !footer.isEmpty()){
								ExamFooterAgreementBO obj=footer.get(0);
								if(obj.getDescription()!=null)
									loginForm.setDescription1(obj.getDescription());
							}else{
								loginForm.setDescription1(null);
							}
							if(isUg){
								return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_UG_MARKS_CARD);
							}else{

								if(CMSConstants.COURSEID_MTA.contains(Integer.parseInt(loginForm.getCourseId())))
									return mapping.findForward(CMSConstants.STUDENTLOGIN_SUPP_PG_MARKS_CARD_MTA);
								else
									return mapping.findForward(CMSConstants.STUDENTLOGIN_SUP_PG_MARKS_CARD);

							
							}
						}else{
							to.setShowSupMC(false);
							errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.KNOWLEDGEPRO_NORECORDS));
							saveErrors(request, errors);
							return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
							}
						}
						}
						if(to.isShowSupMC()){
							supMarksCard.add(to);
						}
						else if(!to.isShowSupMC() && isBlockedStudent){
							supMarksCard.add(to);
						}
					}
			}
			session.setAttribute("supMarksCard", supMarksCard);
		}
		
			}
		}
			cleanUpPageData(loginForm);
			log.debug("Exit  from initOnlineRecieptsForStudentLogin");
	return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
	
		}catch (ApplicationException e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
	} catch(Exception e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		//throw e;
	}
}else {
log.debug("leaving the studentLoginAction with errors");
cleanUpPageData(loginForm);
return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
}
}
	public ActionForward studentLoginMarksCardAgreementSupp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_AGREEMENT_SUPP);
	}
	
	public ActionForward studentLoginMarksCardAgreementNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CMSConstants.STUDENTLOGIN_MARKS_CARD_AGREEMENT_NEW);
	}
	
	private void cleanUpPageData(LoginForm loginform) {
		loginform.setRegularExamId(null);
		loginform.setSuppExamId(null);
		loginform.setExamIDForMCard(0);
		loginform.setSupMCexamID(0);
		loginform.setSupMCClassId(0);
		loginform.setExamType("Regular");
		loginform.setAgreementAccepted(false);
		loginform.setAgreementId(0);
		loginform.setRevaluationRegClassId(null);
		loginform.setRevaluationSupClassId(null);
		loginform.setSmartCardNo(null);
		loginform.setAmount(null);
		loginform.setEnteredDob(null);
		//loginform.setDateOfBirth(null);
	}
	
	 @SuppressWarnings("unchecked")
	public  void setDataToForm(LoginForm loginform, HttpServletRequest request)throws Exception {
 		ICashCollectionTransaction cashCollectionTransaction = CashCollectionTransactionImpl.getInstance();
		int finId = cashCollectionTransaction.getCurrentFinancialYear();
		if(finId<=0){
				loginform.setFeesNotConfigured(true);
		}else{
			String query="from OnlineBillNumber o where o.pcFinancialYear.id = "+finId+" and o.isActive = 1";
			INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
			List list1=txn.getDataForQuery(query);
			if(list1==null || list1.isEmpty())
				loginform.setFeesNotConfigured(true);
				loginform.setFinId(finId);
			
		}
     }
     
	
	public ActionForward calculateAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered calculateAmount ------------------");
		LoginForm loginform = (LoginForm)form;
		 ActionErrors errors = loginform.validate(mapping, request);
		setUserId(request,loginform);
		if (errors.isEmpty()) {
			double amount=0;
			try {	
				if(loginform.getAmount()!=null && !loginform.getAmount().isEmpty()){
				amount = Double.parseDouble(loginform.getAmount());
				}
				if(amount==0){
			
					if (errors.get(CMSConstants.KNOWLEDGEPRO_REVALUATION_NORECORD_SELECTED) != null
							&& !errors.get(CMSConstants.KNOWLEDGEPRO_REVALUATION_NORECORD_SELECTED)
									.hasNext()) {
							ActionMessage error = new ActionMessage(
									CMSConstants.KNOWLEDGEPRO_REVALUATION_NORECORD_SELECTED);
							errors.add(CMSConstants.KNOWLEDGEPRO_REVALUATION_NORECORD_SELECTED, error);
					}
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.STUDENTLOGIN_REVALUATION_PAGE);
				}
				loginform.setTotalFees(amount);	
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				loginform.setErrorMessage(msg);
				loginform.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log.info("Exit calculateAmount--------------------------------------- ");
			return mapping.findForward(CMSConstants.STUDENTLOGIN_REVALUATION_PAGE);
		}
		log.info("Entered calculateAmount---------------------------------------");
		return mapping.findForward(CMSConstants.REVALUATION_STUDENT_VEIRFY_SMART);
	}
	
    
    /**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verifyStudentSmartCardForStudentLogin(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LoginForm loginform = (LoginForm)form;// Type casting the Action form to Required Form
		ActionErrors errors = new ActionErrors();
		setUserId(request,loginform);//setting the userId to Form
		try {
			boolean isValidSmartCard=StudentLoginHandler.getInstance().verifySmartCard(loginform.getSmartCardNo(),loginform.getStudentId());
			if(!isValidSmartCard){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message","Please Enter the valid last 5 digits of your smart card number"));
			}
			if(loginform.getEnteredDob()!=null){
				if(!loginform.getEnteredDob().equalsIgnoreCase(loginform.getDob()))
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message","Please Enter Valid Date Of Birth"));
			}
			
			if(!errors.isEmpty()){
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.REVALUATION_STUDENT_VEIRFY_SMART);
			}
			
		}catch (ReActivateException e) {
			errors.add("error", new ActionError("knowledgepro.admission.certificate.course.available"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.REVALUATION_STUDENT_VEIRFY_SMART);
		} catch (Exception e) {
			log.error("Error in getCertificateCourses"+e.getMessage());
			String msg = super.handleApplicationException(e);
			loginform.setErrorMessage(msg);
			loginform.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		
		return mapping.findForward(CMSConstants.REVALUATION_STUDENT_VEIRFY_SMART_1);
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initConvocationRegistration(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		LoginForm loginForm=(LoginForm) form;
		loginForm.reset1();
		loadConvocationRegistration(loginForm,request);
		if(loginForm.getConvocationId()!=0)
		{
			if(loginForm.getGuestPassRequired()!=null && loginForm.getGuestPassRequired()==true)
			{
			loginForm.setConvocationRelation(true);
			}
		}
		else{
			loginForm.setConvocationRelation(false);
		}
		return mapping.findForward(CMSConstants.INIT_CONVOCATION_REGISTRATION);
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveConvocationRegistration(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		LoginForm loginForm=(LoginForm) form;
		ActionMessages messages=new ActionMessages();
		HttpSession session=request.getSession();
		setUserId(request, loginForm);
		ActionErrors errors=loginForm.validate(mapping, request);
		boolean isAdded=false;
		if(errors.isEmpty())
		{
			if(loginForm.getGuestPassRequired()==true)
			{
				if(loginForm.getRelationshipWithGuest().isEmpty())
				{
					errors.add("error", new ActionError("knowledgepro.convocation.guestpass.relationship.required"));
					saveErrors(request, errors);
					if(loginForm.getGuestPassRequired()!=null && loginForm.getGuestPassRequired()==true)
					{
						loginForm.setConvocationRelation(true);
					}
					else
					{
						loginForm.setConvocationRelation(false);
					}
					return mapping.findForward(CMSConstants.INIT_CONVOCATION_REGISTRATION);
				}
			}
		try {
			isAdded=LoginHandler.getInstance().saveConvocationRegistration(loginForm,session);
			if(isAdded)
			{
				ActionMessage message = new ActionMessage("knowledgepro.convocation.registration.success.message");// Adding success message.
				messages.add("messages", message);
				saveMessages(request, messages);
				if(loginForm.getGuestPassRequired()!=null && loginForm.getGuestPassRequired()==true)
				{
					loginForm.setConvocationRelation(true);
				}
				else
				{
					loginForm.setConvocationRelation(false);
				}
				return mapping.findForward(CMSConstants.SUCCESS_CONVOCATION_REGISTRATION);
			}
			else{
				ActionMessage message = new ActionMessage("knowledgepro.convocation.registration.failure.message");// Adding failure message.
				messages.add("messages", message);
				saveMessages(request, messages);
				return mapping.findForward(CMSConstants.INIT_CONVOCATION_REGISTRATION);
			}
		} catch (Exception e) {
			log.error("Error in getCertificateCourses"+e.getMessage());
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		}
		else
		{
			addErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.INIT_CONVOCATION_REGISTRATION);
	}
	
	/**
	 * @param loginForm
	 * @param request
	 * @throws Exception
	 */
	public void loadConvocationRegistration(LoginForm loginForm,HttpServletRequest request) throws Exception
	{
		HttpSession session=request.getSession();
		LoginHandler.getInstance().loadConvocationRegistration(loginForm,session);
	}
	
	public ActionForward initSapRegistration(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		LoginForm loginForm=(LoginForm) form;
		boolean exist=false;
		setUserId(request, loginForm);
		loginForm.setSapRegExist(false);
		try {
			exist=LoginHandler.getInstance().SapRegistrationLoad(loginForm);
			if(exist)
			{ 
				loginForm.setSapRegExist(true);
				return mapping.findForward(CMSConstants.INIT_SAP_REGISTRATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_SAP_REGISTRATION);
		
	}
	public ActionForward saveSapRegistration(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		LoginForm loginForm=(LoginForm) form;
		ActionMessages messages=new ActionMessages();
		HttpSession session=request.getSession();
		setUserId(request, loginForm);
		ActionErrors errors=loginForm.validate(mapping, request);
		loginForm.setSapRegExist(false);
		boolean isAdded=false;
		boolean exist=false;
		try
		{
			if(errors.isEmpty())
			{
				exist=LoginHandler.getInstance().SapRegistrationLoad(loginForm);
				if(exist)
				{ 
					loginForm.setSapRegExist(true);
					return mapping.findForward(CMSConstants.INIT_SAP_REGISTRATION);
				}else
				{
						isAdded=LoginHandler.getInstance().saveSapRegistration(loginForm,session);
						if(isAdded)
						{
							LoginHandler.getInstance().sendMailToAdmin(loginForm);
							LoginHandler.getInstance().sendSMSToStudent(loginForm);
							ActionMessage message = new ActionMessage("knowledgepro.sap.registration.success.message");// Adding success message.
							messages.add("messages", message);
							saveMessages(request, messages);
							return mapping.findForward(CMSConstants.INIT_SAP_REGISTRATION);
						}else{
							ActionMessage message = new ActionMessage("knowledgepro.sap.registration.failure.message");// Adding failure message.
							messages.add("messages", message);
							saveMessages(request, messages);
							return mapping.findForward(CMSConstants.INIT_SAP_REGISTRATION);
						}
					}
			}
			else
			{
				addErrors(request, errors);
			}
		} catch (Exception e) {
			log.error("Error in getCertificateCourses"+e.getMessage());
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		
		return mapping.findForward(CMSConstants.INIT_CONVOCATION_REGISTRATION);
	}
	public ActionForward StudentDetails(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		/*@SuppressWarnings("unused")
		LoginForm loginForm=(LoginForm) form;
*/		
		return mapping.findForward(CMSConstants.STUDENT_DETAILS_DISPLAY);
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward displaySAPpResuls(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		LoginForm loginForm=(LoginForm) form;
		loginForm.setStatus(null);
		HttpSession session = request.getSession(true);
		loginForm.setSapRegExist(false);
		loginForm.setStatusIsPass(false);
		loginForm.setStatusIsFail(false);
		loginForm.setStatusIsIsOther(false);
		try {
			String studentid = (String) session.getAttribute("studentid");
			UploadSAPMarksBo bo=StudentLoginHandler.getInstance().getSAPExamResuls(studentid);
			if(bo.getStatus()!=null && !bo.getStatus().isEmpty()){
				if(bo.getStatus().equalsIgnoreCase("PASS")){
					loginForm.setStatusIsPass(true);
				}else if(bo.getStatus().equalsIgnoreCase("Fail")){
					loginForm.setStatusIsFail(true);
				}else{
					loginForm.setStatusIsIsOther(true);
				}
				loginForm.setStatus((bo.getStatus()).toUpperCase());
				loginForm.setSapExamDate(CommonUtil.formatSqlDate(bo.getDate().toString()));
				request.setAttribute("status", bo.getStatus());
			}
		} catch (Exception e) {
			log.error("Error in getCertificateCourses"+e.getMessage());
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.STUDENT_SAP_EXAM_RESULS);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward certificateCourseStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Entering the getHallTicket");
		LoginForm loginForm = (LoginForm) form;
		try {
			List<StudentMarkDetailsTO> list=StudentCertificateCourseHandler.getInstance().getCompletedCourseCount111(loginForm.getStudentId(),Integer.parseInt(loginForm.getCourseId()),request,loginForm.getTermNo());
			loginForm.setStudentMarkDetailsTOList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.CERTIFICATE_COURSE_STATUS);
	}
	
	public int showSupplementaryRevaluationApplication(HttpSession session, int classId, LoginForm loginForm, String studentId) throws NumberFormatException, Exception{

		Map<Integer,List<Integer>> examClassMap=new HashMap<Integer, List<Integer>>();
		List<Integer> suppHallTcktClassIds=DownloadHallTicketHandler.getInstance().getSupplementaryClassIds(Integer.parseInt(studentId), classId, true, "Revaluation/Scrutiny");
		List<Integer> classIds=null;
		String exams = "";
		int suppExamId=0;
		for (Integer hallTcktClassId : suppHallTcktClassIds) {
			exams = "";
			if(hallTcktClassId > 0){
				exams = DownloadHallTicketHandler.getInstance().getExamIdByClassIdForSupHallTicket(hallTcktClassId, loginForm, "Revaluation/Scrutiny");
			}
			if(!exams.isEmpty()){
				String[] examsId=exams.split(",");
				for (String examId : examsId) {
					suppExamId = Integer.parseInt(examId);
					if(examClassMap.containsKey(Integer.parseInt(examId))){
						classIds=examClassMap.remove(Integer.parseInt(examId));
					}else
						classIds=new ArrayList<Integer>();
					classIds.add(hallTcktClassId);
					examClassMap.put(Integer.parseInt(examId),classIds);
				}
			}
		}
		
		List<ShowMarksCardTO> supList=new ArrayList<ShowMarksCardTO>();
		// map key is examId and value is list of classIds
		int count=1;
		int examId=0;
		for (Map.Entry<Integer, List<Integer>> entry : examClassMap.entrySet()) {
			examId=entry.getKey();
			List<Integer> classIdList = entry.getValue();
			ShowMarksCardTO to=new ShowMarksCardTO();
			to.setSupMCexamID(examId);
			to.setSupMCClassId(classIdList.get(0));
			to.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinitionBO",true,"name"));
			to.setCnt(count);
			count+=1;
			List<Integer> classesId=new ArrayList<Integer>();
			for (Integer hallTcktClassId : entry.getValue()) {
				ExamBlockUnblockHallTicketBO examBlockUnblockHallTicketBO = DownloadHallTicketHandler.getInstance().isBlockedStudent(Integer.parseInt(studentId), hallTcktClassId, examId, "H");  
				boolean isBlockedStudent = false;
				if(examBlockUnblockHallTicketBO!= null && examBlockUnblockHallTicketBO.getId() > 0){
					isBlockedStudent = true;
				}
				boolean isDateValid = DownloadHallTicketHandler.getInstance().isDateValid(hallTcktClassId, examId, "Revaluation/Scrutiny", true);
				boolean isAppeared = DownloadHallTicketHandler.getInstance().isAppeared(Integer.parseInt(studentId), examId,hallTcktClassId,"Revaluation/Scrutiny");
				if(hallTcktClassId > 0 && examId > 0 && isDateValid && isAppeared){
					classesId.add(hallTcktClassId);
					to.setShowSupRevalAppln(true);
				}
				if(isBlockedStudent){
					to.setSupMCBlockedStudent(true);
					to.setSupMCBlockReason(examBlockUnblockHallTicketBO.getBlockReason());
				}
				//Change By manu,1st check condition
				ExamFooterAgreementBO agreementBO=null;
				if(loginForm.getAgreementId()>0){
				agreementBO = DownloadHallTicketHandler.getInstance().getAgreement(loginForm.getAgreementId());
				}
				if(agreementBO!= null && agreementBO.getDescription()!= null){
					to.setSupHallTicketagreement(agreementBO.getDescription());
				}
			}
			to.setClassIds(classesId);
			if(to.getShowSupRevalAppln()){
				supList.add(to);
			}
		}
		session.setAttribute("supRevalApplnList", supList);
		return suppExamId;
		
	}
	
	public ActionForward initStudentInstruction(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		LoginForm loginForm = (LoginForm)form;
		loginForm.reset(mapping, request);
		if(StudentLoginHandler.getInstance().checkExistingExtentionActivity(loginForm.getStudentId())) {
			loginForm.setHasAlreadySubmittedExtensionActivity(true);
		}
		else {
			setStuFeedbackInstructions(loginForm);
		}
   		return mapping.findForward(CMSConstants.STUDENT_LOGIN_INSTRUCTION);
	}
	private void setStuFeedbackInstructions( LoginForm loginForm) throws Exception{
		List<StudentInstructionTO> instructionsList = StudentLoginHandler.getInstance().getInstructions(loginForm);
		loginForm.setStuFeedbackInsToList(instructionsList);
	}
		
	public ActionForward initPublish(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response ) throws Exception{
		LoginForm loginForm = (LoginForm)form;
		HttpSession session = request.getSession(true);
		loginForm.reset(mapping, request);
		loginForm.setClassId((String)session.getAttribute("ClassId"));
		loginForm.setList(StudentLoginHandler.getInstance().getStudentGroup(loginForm));
		loginForm.setStudentGrpId(String.valueOf(loginForm.getList().get(0).getId()));
		loginForm.setExList(StudentLoginHandler.getInstance().getStudentExtention(loginForm));
		return mapping.findForward(CMSConstants.STUDENT_LOGIN_PUBLISH);
	}
	
	public ActionForward saveStudentExtensionActivities(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response ) throws Exception{
		LoginForm loginForm = (LoginForm)form;
		ActionErrors errors = new ActionErrors();
		try {
			errors = validateExtensions(loginForm);
			if(!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_PUBLISH);
			}
			else {
				setUserId(request, loginForm);
				boolean isSaved = StudentLoginHandler.getInstance().saveStudentExtensions(loginForm);
				if(!isSaved) {
					errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.studentLogin.addFailure", " is required!"));
					saveErrors(request, errors);
				}
				else {
					loginForm.reset(mapping, request);
					request.setAttribute("extensionActivityResult", true);
					return initStudentInstruction(mapping, loginForm, request, response);
				}
			}
		}
		catch(Exception ex) {
			if(ex instanceof MissingPreferenceException) {
				errors.add("error", new ActionError("knowledgepro.admission.online.apply.pref.required", "is"));
			}
			else if(ex instanceof ExcessPreferenceException) {
				errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.studentLogin.moreThanRequiredSelected"));
			}
			else if(ex instanceof DuplicatePreferenceException) {
				errors.add("error", new ActionError("admissionFormForm.coursepref.dup.invalid"));
			}
			else if(ex instanceof MinimumPreferenceViolationException) {
				errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.studentLogin.lessPreferenceSelected"));
			}
			if(!errors.isEmpty()) {
				Iterator<StudentExtentionTO> it = loginForm.getExList().iterator();
				while(it.hasNext()) {
					StudentExtentionTO to = it.next();
					to.setChecked(null);
					to.setPreference(null);
				}
				saveErrors(request, errors);
			}
			loginForm.reset(mapping, request);
			ex.printStackTrace();
		}
		return mapping.findForward(CMSConstants.STUDENT_LOGIN_PUBLISH);
	}
	
	private ActionErrors validateExtensions(LoginForm loginForm) throws Exception {
		ActionErrors errors = new ActionErrors();
		Iterator<StudentExtentionTO> it = loginForm.getExList().iterator();
		int count = 0;
		int preferenceTotal = 0;
		while(it.hasNext()) {
			StudentExtentionTO to = it.next();
			if(to.getChecked() != null && to.getChecked()) {
				count++;
				if(to.getPreference() != null && !to.getPreference().isEmpty() ){
					preferenceTotal += Integer.parseInt(to.getPreference());
				}
				if(to.getPreference() == null || to.getPreference().isEmpty()) {
					throw new MissingPreferenceException();
				}
			}
			if(count > 3) {
				throw new ExcessPreferenceException();
			}
		}
		if(preferenceTotal != 6) {
			throw new DuplicatePreferenceException();
		}
		if(count < 3) {
			throw new MinimumPreferenceViolationException();
		}
		return errors;
	}
	public ActionForward getInternalMarks(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors = loginForm.validate(mapping, request);
		try {
			
			if(loginForm.getStudentId()>0){
				String prevClassIds=DownloadHallTicketHandler.getInstance().getPreviousClassIds(loginForm.getStudentId(), Integer.valueOf(loginForm.getClassId()));
				Map<Integer,String> classesMap=DownloadHallTicketHandler.getInstance().getInternalMarksCardClasses(prevClassIds,"Internal Marks Card");
				loginForm.setInternalClassesMap(classesMap);
			}
			
			
		} catch (Exception ex) {
			errors.add(CMSConstants.ERROR, new ActionError(
					"knowledgepro.errorMessage"));
			saveErrors(request, errors);
			ex.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_INTERNAL_MARKS_CARD);
	}
	
	public ActionForward getInternalMarkDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors=loginForm.validate(mapping, request);
		try{
			if(!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.INIT_INTERNAL_MARKS_CARD);
			}else{
				IDownloadHallTicketTransaction iDownloadHallTicketTransaction = DownloadHallTicketTransactionImpl.getInstance(); 
				Map<Integer, String> examNameMap =DownloadHallTicketHandler.getInstance().getInternalExamNameByClass(loginForm.getInternalClassId());
				examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);// sorting the exam names based on name
				loginForm.setExamNameList(examNameMap);
				
				StudentTO interalMarks=DownloadHallTicketHandler.getInstance().getInternalExamsByClass(loginForm);
				if(interalMarks==null){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
								
					return mapping.findForward(CMSConstants.INIT_INTERNAL_MARKS_CARD);
				}
				loginForm.setInternalMarks(interalMarks);
				
				
				List<StudentMarksTO> examList=new ArrayList<StudentMarksTO>();
				Iterator<Integer> examsIte=loginForm.getExamNameList().keySet().iterator();
				while(examsIte.hasNext()){
					StudentMarksTO examto=new StudentMarksTO();
					Integer examId=examsIte.next();
					examto.setExamId(examId+"");
					examto.setExamName(loginForm.getExamNameList().get(examId));
					examList.add(examto);
				}
				loginForm.setExamList(examList);
				
			}
			
			
			
		}catch (Exception e) {
			log.error("Error while getting the absence information summary"+e.getMessage());
			String msg = super.handleApplicationException(e);
			loginForm.setErrorMessage(msg);
			loginForm.setErrorStack(e.getMessage());
			ActionMessage message = new ActionMessage("knowledgepro.studentlogin.markscard.publish");
			messages.add("messages", message);
			saveMessages(request, messages);
			return mapping.findForward(CMSConstants.INIT_INTERNAL_MARKS_CARD);
		}
		
		return mapping.findForward(CMSConstants.INTERNAL_MARKS_CARD_STUDENT);
		
	}
	
	public ActionForward initStudentSemesterFeesPayment(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		LoginForm loginForm = (LoginForm) form;
		if(loginForm.getCourseId().equalsIgnoreCase("22")){
			loginForm.setStudentSemesterFeesAmount("10000");
		}else if(loginForm.getCourseId().equalsIgnoreCase("23")){
			loginForm.setStudentSemesterFeesAmount("10000");
		}else if(loginForm.getCourseId().equalsIgnoreCase("26")){
			loginForm.setStudentSemesterFeesAmount("12250");
		}else if(loginForm.getCourseId().equalsIgnoreCase("28")){
			loginForm.setStudentSemesterFeesAmount("19750");
		}else if(loginForm.getCourseId().equalsIgnoreCase("29")){
			loginForm.setStudentSemesterFeesAmount("12250");
		}
		else if(loginForm.getCourseId().equalsIgnoreCase("30")){
			loginForm.setStudentSemesterFeesAmount("10000");
		}
		else if(loginForm.getCourseId().equalsIgnoreCase("31")){
			loginForm.setStudentSemesterFeesAmount("12250");
		}
		boolean paymentDone = StudentLoginHandler.getInstance().paymentDone(loginForm);
		if(paymentDone){
			loginForm.setPaymentDone(true);
		}else {
			loginForm.setPaymentDone(false);
		}
		return mapping.findForward(CMSConstants.PAY_STUDENT_SEMESTER_FEES_ONLINE_LINK);
		
	}
	
	public ActionForward redirectingToPGI(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors=new ActionErrors();
		validatePGI(loginForm,errors);
		setUserId(request,loginForm);
		try{
			String hash = StudentLoginHandler.getInstance().getParameterForPGI(loginForm);
			request.setAttribute("hash", hash);
			request.setAttribute("txnid", loginForm.getRefNo());
			request.setAttribute("productinfo", loginForm.getProductinfo());
			request.setAttribute("amount", loginForm.getStudentSemesterFeesAmount());
			request.setAttribute("email", loginForm.getContactMail());
			request.setAttribute("firstname", loginForm.getStudentName());
			request.setAttribute("phone",loginForm.getMobileNo());
			request.setAttribute("test",loginForm.getTest());
			request.setAttribute("surl",CMSConstants.PAYUMONEY_STUDENT_SEMESTER_SUCCESSURL);
			request.setAttribute("furl",CMSConstants.PAYUMONEY_STUDENT_SEMESTER_FAILUREURL);
		}catch (Exception e) {
			log.error("error in redirectToPGI...",e);
			//throw e;
			System.out.println("************************ error details in online admission in redirectToPGI*************************"+e);
			
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.REDIRECT_PGI_STUDENT_SEMESTER);
	}

	private void validatePGI(LoginForm loginForm, ActionErrors errors) throws Exception {
		if(CMSConstants.PGI_MERCHANT_ID_STU == null || CMSConstants.PGI_MERCHANT_ID_STU.isEmpty()
				|| CMSConstants.PGI_MERCHANT_ID_STU==""){
			if (errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_MERCHANT_ID_REQUIRED, new ActionError(CMSConstants.PGI_MERCHANT_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_SECURITY_ID_STU == null || CMSConstants.PGI_SECURITY_ID_STU.isEmpty()
				|| CMSConstants.PGI_SECURITY_ID_STU==""){
			if (errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_SECURITY_ID_REQUIRED, new ActionError(CMSConstants.PGI_SECURITY_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_CHECKSUM_KEY_STU == null || CMSConstants.PGI_CHECKSUM_KEY_STU.isEmpty()
				|| CMSConstants.PGI_CHECKSUM_KEY_STU==""){
			if (errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED)!=null && !errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED, new ActionError(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED));
			}
		}
		
	}
	
	public ActionForward updatePGIResponse(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors=new ActionErrors();
		ActionMessages messages=new ActionMessages();
		
		try{
			boolean isUpdated= StudentLoginHandler.getInstance().updateResponse(loginForm);
			if(isUpdated && loginForm.getIsTnxStatusSuccess()){
				//boolean	result=handler.saveApplicationDetailsToSession(loginForm);
				
				messages.add(CMSConstants.MESSAGES,new ActionMessage("knowledgepro.admission.empty.err.message",loginForm.getPgiStatus()));
				saveMessages(request, messages);
			}
			else{
				errors.add("error", new ActionError("knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
			
			boolean paymentDone = StudentLoginHandler.getInstance().paymentDone(loginForm);
			if(paymentDone){
				loginForm.setPaymentDone(true);
			}else {
				loginForm.setPaymentDone(false);
			}
			
		}catch (BusinessException e) {
			errors.add("error", new ActionError("knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		}
		catch (Exception e) {
			log.error("error in updatePGIResponse-AdmissionFormAction...",e);
			//throw e;
			System.out.println("************************ error details in online admission in updatePGIResponse*************************"+e);
			
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		
		}
		return mapping.findForward(CMSConstants.PAY_STUDENT_SEMESTER_FEES_ONLINE_LINK);
		
	}
	
	public ActionForward getAllPaymentDetails(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors=loginForm.validate(mapping, request);
		String studentId = String.valueOf(loginForm.getStudentId());
		Student student = StudentLoginHandler.getInstance().getStudentObj(studentId);
		loginForm.setFeePrintReciept(new FeePrintReciept());
		loginForm.setRegNo(student.getRegisterNo());
		loginForm.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
		loginForm.setCourseName(student.getAdmAppln().getCourse().getName());
		//Date date = new Date();
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//String daString = simpleDateFormat.format(date);
		//loginForm.setDate(daString);
	/*	if(loginForm.getCourseId() != null){
			String department = StudentLoginHandler.getInstance().getDepartment(loginForm.getCourseId());
			loginForm.setDeptName(department);
		}*/
		int count = 0;
		List<CandidatePaymentDetailsTO> paymentdetailsList1 = new ArrayList<CandidatePaymentDetailsTO>();
		List<CandidatePaymentDetailsTO> paymentdetailsList2 = new ArrayList<CandidatePaymentDetailsTO>();
		List<CandidatePaymentDetailsTO> paymentdetailsList3 = new ArrayList<CandidatePaymentDetailsTO>();
		List<CandidatePaymentDetailsTO> paymentdetailsList4 = new ArrayList<CandidatePaymentDetailsTO>();
		List<CandidatePaymentDetailsTO> paymentdetailsList5 = new ArrayList<CandidatePaymentDetailsTO>();
		List<CandidatePaymentDetailsTO> mainPaymentdetailsList = new ArrayList<CandidatePaymentDetailsTO>();
		List<Integer> examIdList1 = new ArrayList<Integer>();
		List<String> statusList1 = new ArrayList<String>();
		List<Integer> examIdList2 = new ArrayList<Integer>();
		List<String> statusList2 = new ArrayList<String>();
		List<Integer> examIdList3 = new ArrayList<Integer>();
		List<String> statusList3 = new ArrayList<String>();
		
		List<CandidatePGIDetailsExamRegular> listDetailsExamRegulars = StudentLoginHandler.getInstance().getRegularList(studentId);
		if(listDetailsExamRegulars != null && !listDetailsExamRegulars.isEmpty()){
		for (CandidatePGIDetailsExamRegular candidatePGIDetailsExamRegular : listDetailsExamRegulars) {
			CandidatePaymentDetailsTO candidatePaymentDetailsTO = new CandidatePaymentDetailsTO();
			/*
			if(candidatePGIDetailsExamRegular.getTxnStatus().equalsIgnoreCase("success")){
				candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
				candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				if(!candidatePGIDetailsExamRegular.getTxnStatus().equalsIgnoreCase("success")){
				paymentdetailsList1.clear();
				}
				paymentdetailsList1.add(candidatePaymentDetailsTO);
				//break;
			}else {
				count++;
				if(count == 1){
					candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
					candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(candidatePGIDetailsExamRegular.getTxnDate() != null){
					String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList1.add(candidatePaymentDetailsTO);
				}
			}
			
			*/
			
			if(paymentdetailsList1.isEmpty()){
				
				candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
				candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
				candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamRegular.getTxnRefNo());
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				if(candidatePGIDetailsExamRegular.getTxnDate() != null){
				String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				}
				int examId = candidatePGIDetailsExamRegular.getExam().getId();
				String status = candidatePGIDetailsExamRegular.getTxnStatus();
				examIdList1.add(examId);
				statusList1.add(status);
				paymentdetailsList1.add(candidatePaymentDetailsTO);
			}else {
				int examId = candidatePGIDetailsExamRegular.getExam().getId();
				String status1 = candidatePGIDetailsExamRegular.getTxnStatus();
				examIdList1.add(examId);
				statusList1.add(status1);
				boolean present = examIdList1.contains(examId);
				boolean status = statusList1.contains(status1);
				if(present == true ){
					if(status == false){
						paymentdetailsList1.clear();
					candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
					candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
					candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamRegular.getTxnRefNo());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(candidatePGIDetailsExamRegular.getTxnDate() != null){
					String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList1.add(candidatePaymentDetailsTO);
					}else {
						if(status1.equalsIgnoreCase("success")){
							candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
							candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
							candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
							candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamRegular.getTxnRefNo());
							SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
							if(candidatePGIDetailsExamRegular.getTxnDate() != null){
							String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
							candidatePaymentDetailsTO.setTxnDate(dateString);
							}
							paymentdetailsList1.add(candidatePaymentDetailsTO);
						}
					}
				}else {
					if(status == false){
						candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamRegular.getTxnStatus());
						candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamRegular.getExam().getName());
						candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamRegular.getTxnAmount().toString());
						candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamRegular.getTxnRefNo());
						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
						if(candidatePGIDetailsExamRegular.getTxnDate() != null){
						String dateString = simpleDateFormat1.format(candidatePGIDetailsExamRegular.getTxnDate());
						candidatePaymentDetailsTO.setTxnDate(dateString);
						}
						paymentdetailsList1.add(candidatePaymentDetailsTO);
					}
				}
			}
		}
		mainPaymentdetailsList.addAll(paymentdetailsList1);
		}
		count = 0;
		List<CandidatePGIDetailsExamSupply> supplyList = StudentLoginHandler.getInstance().getSupplyList(studentId);
		if(supplyList != null && !supplyList.isEmpty()){
		for (CandidatePGIDetailsExamSupply candidatePGIDetailsExamSupply : supplyList) {
			CandidatePaymentDetailsTO candidatePaymentDetailsTO = new CandidatePaymentDetailsTO();
			/*
			if(candidatePGIDetailsExamSupply.getTxnStatus().equalsIgnoreCase("success")){
				candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
				candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				if(!candidatePGIDetailsExamSupply.getTxnStatus().equalsIgnoreCase("success")){
				paymentdetailsList2.clear();
				}
				paymentdetailsList2.add(candidatePaymentDetailsTO);
				
				//break;
			}else {
				count++;
				if(count == 1){
					candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
					candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(candidatePGIDetailsExamSupply.getTxnDate() != null){
					String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList2.add(candidatePaymentDetailsTO);
				}
			}*/
			
			if(paymentdetailsList2.isEmpty()){
				candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
				candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
				candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamSupply.getTxnRefNo());
				if(candidatePGIDetailsExamSupply.getTxnDate() != null){
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				}
				int examId = candidatePGIDetailsExamSupply.getExam().getId();
				String status = candidatePGIDetailsExamSupply.getTxnStatus();
				examIdList2.add(examId);
				statusList2.add(status);
				paymentdetailsList2.add(candidatePaymentDetailsTO);
			}else {
				int examId = candidatePGIDetailsExamSupply.getExam().getId();
				String status1 = candidatePGIDetailsExamSupply.getTxnStatus();
				examIdList2.add(examId);
				statusList2.add(status1);
				boolean present = examIdList2.contains(examId);
				boolean status = statusList2.contains(status1);
				if(present == true ){
					if(status == false){
						paymentdetailsList2.clear();
					candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
					candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
					candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamSupply.getTxnRefNo());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(candidatePGIDetailsExamSupply.getTxnDate()!= null){
					String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList2.add(candidatePaymentDetailsTO);
					}else {
						if(status1.equalsIgnoreCase("success")){
							candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
							candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
							candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
							candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamSupply.getTxnRefNo());
							SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
							if(candidatePGIDetailsExamSupply.getTxnDate()!= null){
							String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
							candidatePaymentDetailsTO.setTxnDate(dateString);
							}
							paymentdetailsList2.add(candidatePaymentDetailsTO);
						}
					}
				}else {
					if(status == false){
						candidatePaymentDetailsTO.setTxnStatus(candidatePGIDetailsExamSupply.getTxnStatus());
						candidatePaymentDetailsTO.setExamName(candidatePGIDetailsExamSupply.getExam().getName());
						candidatePaymentDetailsTO.setAmount(candidatePGIDetailsExamSupply.getTxnAmount().toString());
						candidatePaymentDetailsTO.setTxnrefNo(candidatePGIDetailsExamSupply.getTxnRefNo());
						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
						if(candidatePGIDetailsExamSupply.getTxnDate() != null){
						String dateString = simpleDateFormat1.format(candidatePGIDetailsExamSupply.getTxnDate());
						candidatePaymentDetailsTO.setTxnDate(dateString);
						}
						paymentdetailsList2.add(candidatePaymentDetailsTO);
					}
				}
			}
			
		}
		mainPaymentdetailsList.addAll(paymentdetailsList2);
		}
		count = 0;
		List<RevaluationApplicationPGIDetails> revaluationList = StudentLoginHandler.getInstance().getRevaluationList(studentId);
		if(revaluationList != null && !revaluationList.isEmpty()){
		for (RevaluationApplicationPGIDetails revaluationApplicationPGIDetails : revaluationList) {
			CandidatePaymentDetailsTO candidatePaymentDetailsTO = new CandidatePaymentDetailsTO();
			/*
			if(revaluationApplicationPGIDetails.getTxnStatus().equalsIgnoreCase("success")){
				candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
				candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				if(!revaluationApplicationPGIDetails.getTxnStatus().equalsIgnoreCase("success")){
				paymentdetailsList3.clear();
				}
				paymentdetailsList3.add(candidatePaymentDetailsTO);
				
				//break;
			}else {
				count++;
				if(count == 1){
					candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
					candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(revaluationApplicationPGIDetails.getTxnDate() != null){
					String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList3.add(candidatePaymentDetailsTO);
				}
			}*/
			
			if(paymentdetailsList3.isEmpty()){
				candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
				candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
				candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
				candidatePaymentDetailsTO.setTxnrefNo(revaluationApplicationPGIDetails.getTxnRefNo());
				if(revaluationApplicationPGIDetails.getTxnDate() != null){
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
				candidatePaymentDetailsTO.setTxnDate(dateString);
				}
				int examId = revaluationApplicationPGIDetails.getExam().getId();
				String status = revaluationApplicationPGIDetails.getTxnStatus();
				examIdList3.add(examId);
				statusList3.add(status);
				paymentdetailsList3.add(candidatePaymentDetailsTO);
			}else {
				int examId = revaluationApplicationPGIDetails.getExam().getId();
				String status1 = revaluationApplicationPGIDetails.getTxnStatus();
				examIdList3.add(examId);
				statusList3.add(status1);
				boolean present = examIdList3.contains(examId);
				boolean status = statusList3.contains(status1);
				if(present == true ){
					if(status == false){
						paymentdetailsList3.clear();
					candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
					candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
					candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
					candidatePaymentDetailsTO.setTxnrefNo(revaluationApplicationPGIDetails.getTxnRefNo());
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					if(revaluationApplicationPGIDetails.getTxnDate() != null){
					String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
					candidatePaymentDetailsTO.setTxnDate(dateString);
					}
					paymentdetailsList3.add(candidatePaymentDetailsTO);
					}else {
						if(status1.equalsIgnoreCase("success")){
						candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
						candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
						candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
						candidatePaymentDetailsTO.setTxnrefNo(revaluationApplicationPGIDetails.getTxnRefNo());
						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
						if(revaluationApplicationPGIDetails.getTxnDate() != null){
						String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
						candidatePaymentDetailsTO.setTxnDate(dateString);
						}
						paymentdetailsList3.add(candidatePaymentDetailsTO);
						}
					}
				}else {
					if(status == false){
						candidatePaymentDetailsTO.setTxnStatus(revaluationApplicationPGIDetails.getTxnStatus());
						candidatePaymentDetailsTO.setExamName(revaluationApplicationPGIDetails.getExam().getName());
						candidatePaymentDetailsTO.setAmount(revaluationApplicationPGIDetails.getTxnAmount().toString());
						candidatePaymentDetailsTO.setTxnrefNo(revaluationApplicationPGIDetails.getTxnRefNo());
						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
						if(revaluationApplicationPGIDetails.getTxnDate() != null){
						String dateString = simpleDateFormat1.format(revaluationApplicationPGIDetails.getTxnDate());
						candidatePaymentDetailsTO.setTxnDate(dateString);
						}
						paymentdetailsList3.add(candidatePaymentDetailsTO);
					}
				}
			}
		}
		mainPaymentdetailsList.addAll(paymentdetailsList3);
		}
		count = 0;
		List<Integer> examId4 = new ArrayList<Integer>();
		List<ExamRegularApplication> regularList = StudentLoginHandler.getInstance().getRegPaymentList(studentId);
		if(regularList != null && !regularList.isEmpty()){
			CandidatePaymentDetailsTO candidatePaymentDetailsTO = new CandidatePaymentDetailsTO();
			for (ExamRegularApplication examRegularApplication : regularList) {
				int examId = examRegularApplication.getExam().getId();
				if(!examId4.contains(examId)){
				candidatePaymentDetailsTO.setTxnStatus("success");
				candidatePaymentDetailsTO.setExamName(examRegularApplication.getExam().getName());
				if(examRegularApplication.getAmount() != null){
				candidatePaymentDetailsTO.setAmount(examRegularApplication.getAmount());
				}
				candidatePaymentDetailsTO.setTxnDate("-");
				examId4.add(examId);
				paymentdetailsList4.add(candidatePaymentDetailsTO);
				}
			}
			mainPaymentdetailsList.addAll(paymentdetailsList4);
		}
		count = 0;
		List<Integer> examId5 = new ArrayList<Integer>();
		List<ExamSupplementaryImprovementApplicationBO> suppList = StudentLoginHandler.getInstance().getSuppPaymentList(studentId);
		if(suppList != null && !suppList.isEmpty()){
			CandidatePaymentDetailsTO candidatePaymentDetailsTO = new CandidatePaymentDetailsTO();
			for (ExamSupplementaryImprovementApplicationBO examSupplementaryImprovementApplicationBO : suppList) {
				int examId = examSupplementaryImprovementApplicationBO.getExamDefinitionBO().getId();
				if(!examId5.contains(examId)){
				candidatePaymentDetailsTO.setTxnStatus("success");
				candidatePaymentDetailsTO.setExamName(examSupplementaryImprovementApplicationBO.getExamDefinitionBO().getName());
				if(examSupplementaryImprovementApplicationBO.getAmount() != null){
				candidatePaymentDetailsTO.setAmount(examSupplementaryImprovementApplicationBO.getAmount());
				}
				candidatePaymentDetailsTO.setTxnDate("-");
				examId5.add(examId);
				paymentdetailsList5.add(candidatePaymentDetailsTO);
				}
			}
			mainPaymentdetailsList.addAll(paymentdetailsList5);
		}
		loginForm.setPaymentDetailsList(mainPaymentdetailsList);
		return mapping.findForward(CMSConstants.PAYMENT_DETAILS_INFO);
	}
	
	public ActionForward initSpecialFeesPaymentLink(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		LoginForm loginForm = (LoginForm) form;
		String studentId = String.valueOf(loginForm.getStudentId());
		Student student = StudentLoginHandler.getInstance().getStudentObj(studentId);
		loginForm.setRegNo(student.getRegisterNo());
		loginForm.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
		List<PublishSpecialFeesTO> puList = StudentLoginHandler.getInstance().getPublishList(loginForm.getClassId());
		loginForm.setPublishList(puList);
		if(student.getIsEgrand() == true){
			loginForm.setSpecialFeesAmount("0");
		}else {
		SpecialFeesBO bo = StudentLoginHandler.getInstance().getTotalAmount(loginForm.getClassId());
		double tutionFees = 0.0;
		double specialFees = 0.0;
		double applicationFees = 0.0;
		double lateFineFees = 0.0;
		double totalAmount = 0.0;
		if(bo.getTutionFees()!= null){
			tutionFees = bo.getTutionFees().doubleValue();
		}
		if(bo.getSpecialFees() != null){
			specialFees = bo.getSpecialFees().doubleValue();
		}
		if(bo.getApplicationFees() != null){
			applicationFees = bo.getApplicationFees().doubleValue();
		}
		if(bo.getLateFineFees() != null){
			lateFineFees = bo.getLateFineFees().doubleValue();
			loginForm.setLateFineFees(bo.getLateFineFees().toString());
		}
		totalAmount = tutionFees + specialFees + applicationFees + lateFineFees;
		String amount = String.valueOf(totalAmount);
		
		loginForm.setSpecialFeesAmount(amount);
		}
		boolean paymentDone = StudentLoginHandler.getInstance().paymentDoneForSpecial(loginForm);
		if(paymentDone){
			loginForm.setPaymentDone(true);
		}else {
			loginForm.setPaymentDone(false);
		}
		return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES_LIST);
	}
	
	public ActionForward redirectingToPGISpecial(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors=new ActionErrors();
		validatePGISpecial(loginForm,errors);
		setUserId(request,loginForm);
		
		try{
			String hash = StudentLoginHandler.getInstance().getParameterForPGIForSpecial(loginForm);
			request.setAttribute("hash", hash);
			request.setAttribute("txnid", loginForm.getRefNo());
			request.setAttribute("productinfo", loginForm.getProductinfo());
			request.setAttribute("amount", loginForm.getSpecialFeesAmount());
			request.setAttribute("email", loginForm.getContactMail());
			request.setAttribute("firstname", loginForm.getStudentName());
			request.setAttribute("phone",loginForm.getMobileNo());
			request.setAttribute("test",loginForm.getTest());
			request.setAttribute("surl",CMSConstants.PAYUMONEY_SPECIAL_FEE_SUCCESSURL);
			request.setAttribute("furl",CMSConstants.PAYUMONEY_SPECIAL_FEE_FAILUREURL);
			
		}catch (Exception e) {
			log.error("error in redirectToPGI...",e);
			//throw e;
			System.out.println("************************ error details in online admission in redirectToPGI*************************"+e);
			
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		}
		
		return mapping.findForward(CMSConstants.REDIRECT_PGI_SPECIAL_FEE);
	}

	private void validatePGISpecial(LoginForm loginForm, ActionErrors errors) throws Exception {
		if(CMSConstants.PGI_MERCHANT_ID_REV == null || CMSConstants.PGI_MERCHANT_ID_REV.isEmpty()
				|| CMSConstants.PGI_MERCHANT_ID_REV==""){
			if (errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_MERCHANT_ID_REQUIRED, new ActionError(CMSConstants.PGI_MERCHANT_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_SECURITY_ID_REV == null || CMSConstants.PGI_SECURITY_ID_REV.isEmpty()
				|| CMSConstants.PGI_SECURITY_ID_REV==""){
			if (errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_SECURITY_ID_REQUIRED, new ActionError(CMSConstants.PGI_SECURITY_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_CHECKSUM_KEY_REV == null || CMSConstants.PGI_CHECKSUM_KEY_REV.isEmpty()
				|| CMSConstants.PGI_CHECKSUM_KEY_REV==""){
			if (errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED)!=null && !errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED, new ActionError(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED));
			}
		}
		
	}
	
	public ActionForward updatePGIResponseSpecialFees(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors=new ActionErrors();
		ActionMessages messages=new ActionMessages();
		
		try{
			boolean isUpdated= StudentLoginHandler.getInstance().updateResponseForSpecial(loginForm);
			if(isUpdated && loginForm.getIsTnxStatusSuccess()){
				//boolean	result=handler.saveApplicationDetailsToSession(loginForm);
				loginForm.setPaymentSuccess(true);
				messages.add(CMSConstants.MESSAGES,new ActionMessage("knowledgepro.admission.empty.err.message",loginForm.getPgiStatus()));
				saveMessages(request, messages);
			}
			else{
				errors.add("error", new ActionError("knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
			
			
			
		}catch (BusinessException e) {
			errors.add("error", new ActionError("knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		}
		catch (Exception e) {
			log.error("error in updatePGIResponse-AdmissionFormAction...",e);
			//throw e;
			System.out.println("************************ error details in online admission in updatePGIResponse*************************"+e);
			
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		
		}
		
		return mapping.findForward("paymentSuccessfullDetailsForStudent");
		
	}
	
	public ActionForward initInternalRetst(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ActionErrors errors=new ActionErrors();
		ActionMessages messages=new ActionMessages();
		try {
			
		
		LoginForm loginForm = (LoginForm) form;
		List<ExamInternalRetestApplicationSubjectsTO> toList=LoginHandler.getInstance().getInternalFailedSubjects(loginForm);
		if (!toList.isEmpty()) {
			loginForm.setExamInternalRetestApplicationSubjectsTO(toList);
		}
		else{
			loginForm.setExamInternalRetestApplicationSubjectsTO(new ArrayList<ExamInternalRetestApplicationSubjectsTO>());
		}
		} catch (Exception e) {
			ActionMessage message = new ActionMessage("knowledgepro.studentlogin.markscard.publish");
			messages.add("messages", message);
			saveMessages(request, messages);
		}
		return mapping.findForward("init_internal_retst");
		
	}
	public ActionForward saveSubjectListForInternalRetst(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ActionErrors errors=new ActionErrors();
		ActionMessages messages=new ActionMessages();
		LoginForm loginForm = (LoginForm) form;
		int resullt=0;
		try {
			resullt=LoginHandler.getInstance().updateInternalRetestSubjects(loginForm);
			if (resullt==1) {
				ActionMessage message = new ActionMessage("knowledgepro.sap.registration.success.message");// Adding success message.
				messages.add("messages", message);
				saveMessages(request, messages);
			}
			else{
				ActionMessage message = new ActionMessage("knowledgepro.convocation.registration.failure.message");// Adding failure message.
				messages.add("messages", message);
				saveMessages(request, messages);
			}
		} catch (Exception e) {
			ActionMessage message = new ActionMessage("knowledgepro.studentlogin.markscard.publish");
			messages.add("messages", message);
			saveMessages(request, messages);
		}
		return mapping.findForward("init_internal_retst");
		
	}
	public ActionForward initRevaluationMarksCardMemo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Entering the initOnlineRecieptsForStudentLogin");
		LoginForm loginForm = (LoginForm) form;
		ActionMessages messages = new ActionMessages();
		ActionMessage message = null;
		 ActionErrors errors = loginForm.validate(mapping, request);
		cleanUpPageData(loginForm);
		
		if (errors.isEmpty()) {
			try {
		StudentLogin studentLogin = LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
		
		if (studentLogin == null) {
			message = new ActionMessage("knowledgepro.admin.validusername");
			messages.add("messages", message);
			addErrors(request, messages);
			loginForm.resetFields();
			if(CMSConstants.LINK_FOR_CJC)
			{ 
				return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
			}else
			{
				return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
			}
		} else {		
			@SuppressWarnings("unused")
			List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
		// no need to excute this code if student is detained
		//if(!listOfDetainedStudents.contains(loginForm.getStudentId())){
			loginForm.setExamType("Regular");
			 List<MarksCardTO> SemExamListTo=(DownloadHallTicketHandler.getInstance().getExamSemList(loginForm));
			 List<Integer> numList=StudentLoginTransactionImpl.getInstance().getexamidInrevaluationApp(loginForm);
			 if(SemExamListTo!=null)	
			{
			Collections.sort(SemExamListTo,new MarkComparator());
			
			List<MarksCardTO> SemExamListToFinal=new ArrayList<MarksCardTO>();
			for (MarksCardTO marksCardTO : SemExamListTo) {
				if(numList.contains(Integer.parseInt(marksCardTO.getNewExamId().substring(0, marksCardTO.getNewExamId().indexOf('_'))))){
					SemExamListToFinal.add(marksCardTO);
				}
			}
			loginForm.setRegularExamList(SemExamListToFinal);
			}
		//}
		//Sup Marks Card
             List<MarksCardTO> SupSemList=(DownloadHallTicketHandler.getInstance().getSupExamSemList(loginForm));
			if(SupSemList!= null)
			Collections.sort(SupSemList,new MarkComparator());
			List<MarksCardTO> SupSemListFinal=new ArrayList<MarksCardTO>();
			for (MarksCardTO marksCardTO : SupSemList) {
				if(numList.contains(Integer.parseInt(marksCardTO.getNewExamId().substring(0, marksCardTO.getNewExamId().indexOf('-'))))){
					SupSemListFinal.add(marksCardTO);
				}
			}
			loginForm.setSuppExamList(SupSemListFinal);
		
		log.debug("Exit  from initOnlineRecieptsForStudentLogin");
		return mapping.findForward("initRevaluationMarksCard");
	}	
	} catch (ApplicationException e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
	} catch(Exception e) {
		log.debug("leaving the studentLoginAction with exception");
		String msg = super.handleApplicationException(e);
		loginForm.setErrorMessage(msg);
		loginForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		//throw e;
	}
} else {
	log.debug("leaving the studentLoginAction with errors");
	addErrors(request, errors);	
	if(CMSConstants.LINK_FOR_CJC)
	{ 
		return mapping.findForward(CMSConstants.STUDENT_LOGIN_FAILURE_CJC);
	}else
	{
		return mapping.findForward(CMSConstants.STUDENTLOGIN_FAILURE);
	}
}
}
	public ActionForward revaluationMemoDisplay(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		LoginForm loginForm = (LoginForm) form;
		ActionErrors errors = loginForm.validate(mapping, request);
		HttpSession session = request.getSession(true);
		session.setAttribute("USERNAME", loginForm.getUserName());
		if (errors.isEmpty()) {
			try {
				LoginHandler.getInstance().isValiedStudentUser(loginForm) ;
				String studentid = String.valueOf(loginForm.getStudentId());
				loginForm.setRevaluationRegClassId(null);
				int examId = 0;
				List<ShowMarksCardTO> regMarksCard=new ArrayList<ShowMarksCardTO>();
				List<MarksCardTO> regularList = loginForm.getRegularExamList();
				String examNam=loginForm.getRegularExamId();
				if (examNam.contains("_")) {
					examId = Integer.parseInt(examNam.substring(0, examNam.indexOf('_')));
				}else if (examNam.contains("-")) {
					examId = Integer.parseInt(examNam.substring(0, examNam.indexOf('-')));
					
				}
				
				loginForm.setExamid(examId);
				StudentLoginHandler.getInstance().getrevaluationMarkCardMemo(loginForm);
				return mapping.findForward("revaluationMemo");	
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			} catch(Exception e) {
				e.printStackTrace();
				log.debug("leaving the studentLoginAction with exception");
				String msg = super.handleApplicationException(e);
				loginForm.setErrorMessage(msg);
				loginForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
				//throw e;
			}
		} else {
			log.debug("leaving the studentLoginAction with errors");
			cleanUpPageData(loginForm);
			//addErrors(request, errors);
			return mapping.findForward(CMSConstants.STUDENT_LOGIN_MARKSCARD);
		}
	}
	
	public ActionForward paymmentReciept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginForm loginForm = (LoginForm) form;
		FeePrintReciept recipt=loginForm.getFeePrintReciept();
		recipt.setAmountWords(CommonUtil.numberToWord(new Double(recipt.getAmount()).intValue()));
		return mapping.findForward("feeReciept");
	}
}
