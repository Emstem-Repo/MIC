package com.kp.cms.handlers.usermanagement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

import com.kp.cms.actions.exam.NewStudentMarksCorrectionAction;
import com.kp.cms.bo.admin.ConvocationRegistration;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.PeersEvaluationOpenSession;
import com.kp.cms.bo.admin.SMSTemplate;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamInternalRetestApplicationSubjectsBO;
import com.kp.cms.bo.sap.SapRegistration;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admin.SyllabusEntryForm;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.handlers.admin.SMSTemplateHandler;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.helpers.usermanagement.LoginTransactionHelper;
import com.kp.cms.to.exam.ExamInternalRetestApplicationSubjectsTO;
import com.kp.cms.to.usermanagement.LoginTransactionTo;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.usermanagement.ILoginTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.usermanagement.LoginTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.jms.MailTO;
import com.kp.cms.utilities.print.HtmlPrinter;

public class LoginHandler {
	
	/**
	 * Represents singleton object of LoginHandler
	 */
	private static volatile LoginHandler loginhandler = null;
	
	
	private LoginHandler() {
		
	}
	
	public static LoginHandler getInstance() {
		if(loginhandler == null) {
			loginhandler = new LoginHandler();
		}
		return loginhandler;
	}
	
	/**
	 * Check the user agenest username and password.
	 * @param loginForm  - 
	 *                      Represents Loginform object.
	 * @return           -  
	 *                      User object if user name and password presents , null otherwise.
	 * @throws ApplicationException
	 */
	public Users isValiedUser(LoginForm loginForm) throws ApplicationException {
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		return loginTransaction.verifyUser(loginForm);		
	}
	

	/**
	 * Returns the list of accessable modules for username and password
	 * @param loginForm -  
	 *                     Represents Loginform object.
	 * @return          - 
	 *                     List<LoginTransactionTo> , List of Transfor object.
	 * @throws ApplicationException
	 */
	public List<LoginTransactionTo> getAccessableModules(LoginForm loginForm)
			throws ApplicationException {
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		List modulesList = loginTransaction.getAccessableModules(loginForm);

		List<LoginTransactionTo> accessableModulesList = LoginTransactionHelper
				.getInstance().getModulesFromList(modulesList,loginForm);

		return accessableModulesList;

	}
	
	/**
	 * Check the user agenest username and password.
	 * @param loginForm  - 
	 *                      Represents Loginform object.
	 * @return           -  
	 *                      User object if user name and password presents , null otherwise.
	 * @throws ApplicationException
	 */
	public StudentLogin isValiedStudentUser(LoginForm loginForm) throws ApplicationException {
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		return loginTransaction.verifyStudentUser(loginForm);		
	}
	/**
	 * method for updating user table for last logged in
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean updateLastLoggedIn(int userId) throws Exception {
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		Boolean isUpdated = iLoginTransaction.updateLastLoggedIn(userId);
		return isUpdated;
	}

	/**
	 * @param studentid
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public boolean checkStudentAttendancePercentage(String studentid, int classId) throws Exception{
		boolean isCheck=false;
		String query="select ac.classSchemewise.classes.name, attStu.student, " +
		" sum(a.hoursHeld), sum(case when (attStu.isPresent=1 ) then a.hoursHeld else 0 end), sum(case when (attStu.isCoCurricularLeave=1) then a.hoursHeld else 0 end), " +
		" sum(case when (attStu.isOnLeave=1) then a.hoursHeld else 0 end)" +
		" from Attendance a join a.attendanceStudents attStu" +
		" join a.attendanceClasses ac where ac.classSchemewise.id= attStu.student.classSchemewise.id" +
		" and attStu.student.admAppln.isCancelled=0 and a.isCanceled=0 and attStu.student.isHide=0 " +
		" and ac.classSchemewise.classes.id in ("+classId+") and attStu.student.id=" +studentid+
		" group by attStu.student.id ";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List list=transaction.getDataForQuery(query);
		Iterator<Object[]> itr=list.iterator();
		if(itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			double classHeld=0;
			double classAtt=0;
			if(obj[2]!=null)
				classHeld=Integer.parseInt(obj[2].toString());
			if(obj[3]!=null)
				classAtt=Integer.parseInt(obj[3].toString());
			if(obj[4]!=null)
				classAtt=classAtt+Integer.parseInt(obj[4].toString());
			double percentage=0;
			if(classHeld>0 && classAtt>0){
				percentage=Math.round((classAtt/classHeld)*100);
				
					if(percentage>=75){
						isCheck=true;
					}
			}
			
		}
		if(list==null || list.isEmpty()){
			if(classId==1870 || classId==1897 || classId==1875 || classId==2021 || classId==2031){
				isCheck=true;
			}
		}
		return isCheck;
	}

	/**
	 * @param mobileNo
	 * @param userId
	 * @param employeeId
	 * @return
	 * @throws Exception 
	 */
	public boolean saveMobileNo(String mobileNo, String userId,String employeeId) throws Exception {
		boolean isAdded=false;
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		Employee employee=iLoginTransaction.getEmployeeDetails(employeeId);
		Employee emp =LoginTransactionHelper.getInstance().copyFormToBO(mobileNo,userId,employee);
		isAdded=iLoginTransaction.getsaveMobileNo(emp);
		return isAdded;
	}

	/**
	 * @param loginForm
	 * @param employeeId
	 * @return
	 * @throws Exception
	 */
	public LoginForm getMobileNo(LoginForm loginForm, String employeeId)throws Exception {
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		Employee employee=iLoginTransaction.getEmployeeDetails(employeeId);
		loginForm=LoginTransactionHelper.getInstance().copyBOToForm(employee);
		return loginForm;
	}
	public Integer getNotifications(String userId) throws Exception {
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		Integer count = iLoginTransaction.getNotificationCount(userId);
		return count;
	}

	public String getMarksEnteryLinks(String userId, HttpSession session) throws Exception{
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
//		List<Object> couseIds = iLoginTransaction.getMarksEnteryLinks(userId,session);
		String displayLinkExamName = iLoginTransaction.getMarksEnteryLinks(userId,session);
		return displayLinkExamName;
	}

	/**
	 * @param loginForm
	 * @param session
	 * @return
	 */
	public boolean getIsPresentPeersEvaluationLink(LoginForm loginForm, HttpSession session)throws Exception {
		boolean isPresent = false;
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		if(session.getAttribute("DepartmentId") != null){
			String departmentId = session.getAttribute("DepartmentId").toString();
			if(departmentId !=null && !departmentId.isEmpty()){
				PeersEvaluationOpenSession openSession = iLoginTransaction.getOpenSession(departmentId);
				session.setAttribute("SessionId", null);
				if(openSession!=null && !openSession.toString().isEmpty()){
					if(openSession.getPeerFeedbackSession()!=null && !openSession.getPeerFeedbackSession().toString().isEmpty()){
						session.setAttribute("SessionId", openSession.getPeerFeedbackSession().getId());
						isPresent = true;
					}
				}
			}
		}
		return isPresent;
	}
	
	
	public List<Object> getIsResearchLink(String userId, HttpSession session) throws Exception{
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		List<Object> couseIds = iLoginTransaction.getResearchLinks(userId,session);
		return couseIds;
	}

	/**
	 * @param loginForm
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public boolean checkStudentIsFinalYearOrNot(LoginForm loginForm,HttpSession session) throws Exception
	{
		ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
		int studentId=iLoginTransaction.getStudentIdByUserName(loginForm);
		session.setAttribute("studentIdforConvocation", studentId);
		return iLoginTransaction.checkStudentIsFinalYearOrNot(studentId);
		
	}
	
   /**
 * @param loginForm
 * @param session
 * @return
 * @throws Exception
 */
public boolean saveConvocationRegistration(LoginForm loginForm,HttpSession session) throws Exception
   {
	   ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
	   int academicYear=iLoginTransaction.getAcademicYearByStudentRegNo(session);
	   ConvocationRegistration registration=LoginTransactionHelper.getInstance().convertFormToBO(academicYear,loginForm,session);
	   return iLoginTransaction.saveConvocationRegistration(registration);
   }
  /**
 * @param loginForm
 * @param session
 * @throws Exception
 */
public void loadConvocationRegistration(LoginForm loginForm,HttpSession session) throws Exception
  {
	  ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
	 ConvocationRegistration convocationRegistration= iLoginTransaction.loadConvocationRegistration(session);
	 if(convocationRegistration!=null)
	 {
		 LoginTransactionHelper.getInstance().convertBoToConvocationForm(convocationRegistration,loginForm);
	 }
  }
public boolean SapRegistrationLoad(LoginForm loginForm) throws Exception
{
	boolean exist=false;
	  ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
	  SapRegistration sapRegistration= iLoginTransaction.LoadSapRegistration(loginForm);
	 if(sapRegistration!=null)
	 {
		 exist=true;
	 }
	 return exist;
}
public boolean saveSapRegistration(LoginForm loginForm,HttpSession session) throws Exception
{
	   ILoginTransaction iLoginTransaction = new LoginTransactionImpl();
	 //  int academicYear=iLoginTransaction.getAcademicYearByStudentRegNo(session);
	   SapRegistration registration=LoginTransactionHelper.getInstance().convertFormToBOSap(loginForm);
	   return iLoginTransaction.saveSapRegistration(registration);
}

public boolean sendMailToAdmin(LoginForm loginForm) throws Exception {
	boolean sent=false;
	Properties prop = new Properties();
	try {
		InputStream inStr = CommonUtil.class.getClassLoader()
				.getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
		prop.load(inStr);
	} catch (FileNotFoundException e) {
		return false;
	} catch (IOException e) {
		return false;
	}
		List<GroupTemplate> list=null;
		TemplateHandler temphandle=TemplateHandler.getInstance();
		 list= temphandle.getDuplicateCheckList(CMSConstants.SAP_ADMIN_NOTIFICATION_MAIL);
		 String date = CommonUtil.getStringDate(new Date());
		if(list != null && !list.isEmpty()) {

			String desc = list.get(0).getTemplateDescription();
			String fromName = prop.getProperty(CMSConstants.SAP_ADMIN_MAIL_FROMNAME);
			String fromAddress=CMSConstants.MAIL_USERID;
			//String toName=prop.getProperty(CMSConstants.SAP_ADMIN_NOTIFICATION_MAIL_NAME);
			String toAddress=prop.getProperty(CMSConstants.SAP_ADMIN_NOTIFICATION_MAIL_ID);
			String subject= "SAP-e Course Registered by student";
			String message =desc;
			message = message.replace(CMSConstants.TEMPLATE_DATE,date);
			message = message.replace(CMSConstants.TEMPLATE_SMS_REGISTERNO,loginForm.getRegNo());
			message = message.replace(CMSConstants.TEMPLATE_CLASS,loginForm.getClassName());
			message = message.replace(CMSConstants.TEMPLATE_CANDIDATE_NAME,loginForm.getStudentName());
			message = message.replace(CMSConstants.TEMPLATE_STUDENTEMAIL_SAP,loginForm.getUnivEmailId());
			sent=sendMail(toAddress, subject, message, fromName, fromAddress);
			HtmlPrinter.printHtml(message);
			}
					 
		return sent;
}
/**
 * Common Send mail
 * @param admForm
 * @return
 */
public boolean sendMail(String mailID, String sub,String message, String fromName, String fromAddress) {
		boolean sent=false;
			String toAddress=mailID;
			// MAIL TO CONSTRUCTION
			String subject=sub;
			String msg=message;
		
			MailTO mailto=new MailTO();
			mailto.setFromAddress(fromAddress);
			mailto.setToAddress(toAddress);
			mailto.setSubject(subject);
			mailto.setMessage(msg);
			mailto.setFromName(fromName);
			
			sent=CommonUtil.sendMail(mailto);
		return sent;
}

public void sendSMSToStudent(LoginForm crForm) throws Exception {
	Properties prop = new Properties();
    InputStream in1 = NewStudentMarksCorrectionAction.class.getClassLoader().getResourceAsStream(CMSConstants.SMS_FILE_CFG);
    prop.load(in1);
	String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
	String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
	String desc="";
	SMSTemplateHandler temphandle=SMSTemplateHandler.getInstance();
	List<SMSTemplate> list= temphandle.getDuplicateCheckList(0,CMSConstants.SAP_STUDENT_SMS);
	if(list != null && !list.isEmpty()) {
		desc = list.get(0).getTemplateDescription();
	}
	String mobileNo="";
	/*if(crForm.getMobileNo()!=null && !crForm.getMobileNo().isEmpty()){
		mobileNo=crForm.getMobileNo();
	}else{
		mobileNo="91";
	}*/
	if(crForm.getMobileNo()!=null && !crForm.getMobileNo().isEmpty()){
		mobileNo="91"+crForm.getMobileNo();
	}
	if(StringUtils.isNumeric(mobileNo) && (mobileNo.length()==12 && desc.length()<=160)){
		MobileMessaging mob=new MobileMessaging();
		mob.setDestinationNumber(mobileNo);
		mob.setMessageBody(desc);
		mob.setMessagePriority(3);
		mob.setSenderName(senderName);
		mob.setSenderNumber(senderNumber);
		mob.setMessageEnqueueDate(new Date());
		mob.setIsMessageSent(false);
		PropertyUtil.getInstance().save(mob);
	}
}

/*public void sendSMSToStudent(LoginForm crForm,String templateName) throws Exception {
	
	ISingleFieldMasterTransaction txn=SingleFieldMasterTransactionImpl.getInstance();
	List<CertificateOnlineStudentRequest> boList=new ArrayList<CertificateOnlineStudentRequest>();
	if(crForm.getStudentToList()!=null && !crForm.getStudentToList().isEmpty()){	
	Iterator<CertificateRequestOnlineTO> itr=crForm.getStudentToList().iterator();
	Map<Integer,Integer> stuMap = new  HashMap<Integer, Integer>();
	while (itr.hasNext()) {
		String mobileNo="";
		String certificateName="";
		CertificateRequestOnlineTO to = (CertificateRequestOnlineTO) itr.next();
		if(to.getIsCompleted()!=null && to.getIsCompleted())
		{
		int studentId= Integer.parseInt(to.getStudentId());
		Student bo=(Student)txn.getMasterEntryDataById(Student.class,studentId);
		if(crForm.getStudentToList()!=null && !crForm.getStudentToList().isEmpty()){
			
			Iterator<CertificateRequestOnlineTO> itr1=crForm.getStudentToList().iterator();
			while (itr1.hasNext()) {
				CertificateRequestOnlineTO to1 = (CertificateRequestOnlineTO) itr1.next();
				if(Integer.parseInt(to1.getStudentId())==studentId){
				if(to1.getIsCompleted()!=null && to1.getIsCompleted()){
				int certId=Integer.parseInt(to1.getCertificateId());
				CertificateDetails cert= (CertificateDetails)txn.getMasterEntryDataById(CertificateDetails.class,certId);
				if(certificateName == null || certificateName.isEmpty())
				{
					certificateName=cert.getCertificateName();
				}
				else if(certificateName!=null && !certificateName.isEmpty())
				{
					certificateName=certificateName+", "+cert.getCertificateName();
				}
			}}
		}
		
	if(bo!=null){
		
	//	if(bo.getAdmAppln().getCourseBySelectedCourseId().getIsApplicationProcessSms()){
			
			if(bo.getAdmAppln().getPersonalData().getMobileNo1()!=null && !bo.getAdmAppln().getPersonalData().getMobileNo1().isEmpty()){
				if(bo.getAdmAppln().getPersonalData().getMobileNo1().trim().equals("0091") || bo.getAdmAppln().getPersonalData().getMobileNo1().trim().equals("+91")
						|| bo.getAdmAppln().getPersonalData().getMobileNo1().trim().equals("091") || bo.getAdmAppln().getPersonalData().getMobileNo1().trim().equals("0"))
					mobileNo = "91";
				else
					mobileNo=bo.getAdmAppln().getPersonalData().getMobileNo1();
			}else{
				mobileNo="91";
			}
			if(bo.getAdmAppln().getPersonalData().getMobileNo2()!=null && !bo.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()){
				mobileNo=mobileNo+bo.getAdmAppln().getPersonalData().getMobileNo2();
				//mobileNo="918095960252";
			}
			
		}
	}
		if(!stuMap.containsKey(studentId)){
			if(mobileNo.length()==12){
				sendSMSToStudent(mobileNo,templateName,certificateName);
				}
			
		}
		stuMap.put(studentId, studentId);
}else if(crForm.getIsReject().equalsIgnoreCase("true"))
{
	if(crForm.getRejectId().equals(to.getId().toString())){
	int studentId= Integer.parseInt(to.getStudentId());
	Student bo=(Student)txn.getMasterEntryDataById(Student.class,studentId);
	if(crForm.getStudentToList()!=null && !crForm.getStudentToList().isEmpty()){
		
		Iterator<CertificateRequestOnlineTO> itr1=crForm.getStudentToList().iterator();
		while (itr1.hasNext()) {
			CertificateRequestOnlineTO to1 = (CertificateRequestOnlineTO) itr1.next();
			if(Integer.parseInt(to1.getStudentId())==studentId){
			if(to1.isRejected()){
			int certId=Integer.parseInt(to1.getCertificateId());
			CertificateDetails cert= (CertificateDetails)txn.getMasterEntryDataById(CertificateDetails.class,certId);
			if(certificateName == null || certificateName.isEmpty())
			{
				certificateName=cert.getCertificateName();
			}
			else if(certificateName!=null && !certificateName.isEmpty())
			{
				certificateName=certificateName+", "+cert.getCertificateName();
			}
		
			
				}
			}
	}
	
if(bo!=null){
	
//	if(bo.getAdmAppln().getCourseBySelectedCourseId().getIsApplicationProcessSms()){
		
		if(bo.getAdmAppln().getPersonalData().getMobileNo1()!=null && !bo.getAdmAppln().getPersonalData().getMobileNo1().isEmpty()){
			mobileNo=bo.getAdmAppln().getPersonalData().getMobileNo1();
		}else{
			mobileNo="91";
		}
		if(bo.getAdmAppln().getPersonalData().getMobileNo2()!=null && !bo.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()){
			mobileNo=mobileNo+bo.getAdmAppln().getPersonalData().getMobileNo2();
			//mobileNo="918095960252";
		}
		if(mobileNo.length()==12){
		 //   mobileNo="0123456789";
		}
	}
}
	if(!stuMap.containsKey(studentId)){
		sendSMSToStudent(mobileNo,templateName,certificateName);
	}
	stuMap.put(studentId, studentId);

}
}}}
}

public void sendSMSToStudent(String mobileNo, String templateName,String certificateName) throws Exception{
	Properties prop = new Properties();
    InputStream in1 = ApplicationStatusUpdateHandler.class.getClassLoader().getResourceAsStream(CMSConstants.SMS_FILE_CFG);
    prop.load(in1);
	String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
	String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
	String desc="";
	SMSTemplateHandler temphandle=SMSTemplateHandler.getInstance();
	List<SMSTemplate> list= temphandle.getDuplicateCheckList(0,templateName);
	if(list != null && !list.isEmpty()) {
		desc = list.get(0).getTemplateDescription();
	}
	desc=desc.replace(CMSConstants.TEMPLATE_CERTIFICATENAME,certificateName);
	
	if(StringUtils.isNumeric(mobileNo) && (mobileNo.length()==12 && desc.length()<=160)){
		MobileMessaging mob=new MobileMessaging();
		mob.setDestinationNumber(mobileNo);
		mob.setMessageBody(desc);
		mob.setMessagePriority(3);
		mob.setSenderName(senderName);
		mob.setSenderNumber(senderNumber);
		mob.setMessageEnqueueDate(new Date());
		mob.setIsMessageSent(false);
		PropertyUtil.getInstance().save(mob);
	}
	
}*/

	public List<LoginTransactionTo> invigilationDutyAllotment(int userId)throws Exception{
	 
	ILoginTransaction loginTransaction = new LoginTransactionImpl();
	List<Object[]> dutyAllotmentDetails = loginTransaction.invigilationDutyAllotmentDetails(userId);
	
	List<LoginTransactionTo> allotmentDetails = LoginTransactionHelper
		.getInstance().converBoToTo(dutyAllotmentDetails);
	
	return allotmentDetails;
	
	}
	public Integer checkingDutyAllotmentDetails(int userId)throws Exception{
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		Integer dutyAllotmentDetailsSize=loginTransaction.getDutyAllotmentDetailsSize(userId);
		return dutyAllotmentDetailsSize;
	}

	public boolean checkForSyllabusEntryLink(SyllabusEntryForm syllabusEntryForm) throws Exception{
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		 	Date currentDate=new Date();
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(currentDate);
		    boolean flag=loginTransaction.checkForSyllabusEntryOpen(syllabusEntryForm,CommonUtil.ConvertStringToSQLDate(CommonUtil.formatDates(currentDate)));
		return flag;
	}

	public StudentLogin isValiedParentUser(LoginForm loginForm) throws Exception {
		ILoginTransaction loginTransaction = new LoginTransactionImpl();
		return loginTransaction.verifyParentUser(loginForm);
	}

	public List<ExamInternalRetestApplicationSubjectsTO> getInternalFailedSubjects(
			LoginForm form) throws ApplicationException {
		ILoginTransaction txn = new LoginTransactionImpl();
		List<ExamInternalRetestApplicationSubjectsBO> boList=txn.getgetInternalFailedSubjectsBo(form);
		List<ExamInternalRetestApplicationSubjectsTO> toList=LoginTransactionHelper.getInstance().convertBOTO(boList);
		return toList;
	}

	public int updateInternalRetestSubjects(LoginForm loginForm) throws ApplicationException {
		ILoginTransaction txn = new LoginTransactionImpl();
		//List<ExamInternalRetestApplicationSubjectsBO> boList=LoginTransactionHelper.getInstance().convertTOBO(loginForm.getExamInternalRetestApplicationSubjectsTO());
		return txn.saveExamInternalRetestApplicationSubjectsBO(loginForm.getExamInternalRetestApplicationSubjectsTO());
	}
   
}

