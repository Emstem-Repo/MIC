	
	package com.kp.cms.handlers.admission;

	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessages;

import com.billdesk.pgidsk.PGIUtil;
import com.kp.cms.actions.admission.AdmissionFormAction;
import com.kp.cms.actions.exam.NewStudentMarksCorrectionAction;
import com.kp.cms.bo.admin.Address;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.AdmittedThrough;
import com.kp.cms.bo.admin.ApplicantFeedback;
import com.kp.cms.bo.admin.ApplicantLateralDetails;
import com.kp.cms.bo.admin.ApplicantTransferDetails;
import com.kp.cms.bo.admin.ApplicantWorkExperience;
import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.CandidateEntranceDetails;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePreference;
import com.kp.cms.bo.admin.CandidatePrerequisiteMarks;
import com.kp.cms.bo.admin.Country;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.CoursePrerequisite;
import com.kp.cms.bo.admin.Currency;
import com.kp.cms.bo.admin.DetailedSubjects;
import com.kp.cms.bo.admin.DocChecklist;
import com.kp.cms.bo.admin.DocTypeExams;
import com.kp.cms.bo.admin.EdnQualification;
import com.kp.cms.bo.admin.EligibilityCriteria;
import com.kp.cms.bo.admin.EligibleSubjects;
import com.kp.cms.bo.admin.ExamCenter;
import com.kp.cms.bo.admin.GenerateMail;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.Income;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.bo.admin.InterviewSchedule;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.Nationality;
import com.kp.cms.bo.admin.Occupation;
import com.kp.cms.bo.admin.PasswordMobileMessaging;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.Prerequisite;
import com.kp.cms.bo.admin.Program;
import com.kp.cms.bo.admin.ProgramType;
import com.kp.cms.bo.admin.ReligionSection;
import com.kp.cms.bo.admin.ResidentCategory;
import com.kp.cms.bo.admin.SMSTemplate;
import com.kp.cms.bo.admin.State;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentOnlineApplication;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.constants.KPPropertiesConfiguration;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.admission.AdmissionFormForm;
import com.kp.cms.forms.admission.ApplicationEditForm;
import com.kp.cms.forms.admission.OnlineApplicationForm;
import com.kp.cms.handlers.admin.CandidateMarkTO;
import com.kp.cms.handlers.admin.CourseHandler;
import com.kp.cms.handlers.admin.DetailedSubjectsHandler;
import com.kp.cms.handlers.admin.SMSTemplateHandler;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.helpers.admission.OnlineApplicationHelper;
import com.kp.cms.to.admin.ApplicantMarkDetailsTO;
import com.kp.cms.to.admin.ApplnDocTO;
import com.kp.cms.to.admin.CandidatePreferenceTO;
import com.kp.cms.to.admin.CoursePrerequisiteTO;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admin.CurrencyTO;
import com.kp.cms.to.admin.DetailedSubjectsTO;
import com.kp.cms.to.admin.DocTypeExamsTO;
import com.kp.cms.to.admin.EdnQualificationTO;
import com.kp.cms.to.admin.ExamCenterTO;
import com.kp.cms.to.admin.IncomeTO;
import com.kp.cms.to.admin.NationalityTO;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.ResidentCategoryTO;
import com.kp.cms.to.admin.SingleFieldMasterTO;
import com.kp.cms.to.admin.SportsTO;
import com.kp.cms.to.admission.AddressTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.EligibilityCriteriaCheckTO;
import com.kp.cms.to.admission.InterviewCardTO;
import com.kp.cms.to.admission.PersonalDataTO;
import com.kp.cms.to.admission.PreferenceTO;
import com.kp.cms.transactions.admin.ICourseTransaction;
import com.kp.cms.transactions.admin.ITemplatePassword;
import com.kp.cms.transactions.admission.IAdmissionFormTransaction;
import com.kp.cms.transactions.admission.IOnlineApplicationTxn;
import com.kp.cms.transactions.admission.IUploadInteviewSelectionTxn;
import com.kp.cms.transactionsimpl.admin.CourseTransactionImpl;
import com.kp.cms.transactionsimpl.admin.TemplateImpl;
import com.kp.cms.transactionsimpl.admission.AdmissionFormTransactionImpl;
import com.kp.cms.transactionsimpl.admission.OnlineApplicationImpl;
import com.kp.cms.transactionsimpl.admission.UploadInterviewSelectionTxnImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.ConverationUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.SMSUtil;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.MailTO;
import com.kp.cms.utilities.jms.SMS_Message;
import com.kp.cms.utilities.print.HtmlPrinter;

	@SuppressWarnings({"deprecation","resource","rawtypes"})
	public class OnlineApplicationHandler {
	private static final Log log = LogFactory.getLog(OnlineApplicationHandler.class);
	
	public static volatile OnlineApplicationHandler self=null;
	public static OnlineApplicationHandler getInstance(){
		if(self==null){
			self= new OnlineApplicationHandler();
		}
		return self;
	}
	private OnlineApplicationHandler(){
		
	}
	IOnlineApplicationTxn txn= new OnlineApplicationImpl();
	OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
	
	
		public boolean saveBasicPage(OnlineApplicationForm admForm) throws Exception {
			log.info("enter createApplicant");
			
			PersonalData persData=new PersonalData();
			ResidentCategory rs=new ResidentCategory();
			rs.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
			persData.setResidentCategory(rs);
			persData.setFirstName(admForm.getApplicantName());
			persData.setDateOfBirth(CommonUtil.ConvertStringToSQLDate(admForm.getApplicantDob()));
			persData.setEmail(admForm.getEmail());
			if(admForm.getMobileNo1()!=null && !admForm.getMobileNo1().isEmpty())
			persData.setMobileNo1(admForm.getMobileNo1().trim());
			if(admForm.getMobileNo2()!=null && !admForm.getMobileNo2().isEmpty())
			persData.setMobileNo2(admForm.getMobileNo2().trim());
			persData.setGender(admForm.getGender());
			persData.setCreatedBy(admForm.getUserId());
			persData.setCreatedDate(new Date());
			persData.setModifiedBy(admForm.getUserId());
			persData.setLastModifiedDate(new Date());
			persData.setIsmgquota(admForm.isIsmgquota());
			persData.setIsCommunity(admForm.isMalankara());
			
			ReligionSection religionSection=new ReligionSection();
			religionSection.setId(Integer.parseInt(admForm.getSubReligion()));
			persData.setReligionSection(religionSection);
			
			AdmAppln appBO=helper.getBasicPageApplicantBO(admForm);
			Student std= new Student();
			std.setIsAdmitted(false);
			std.setCreatedBy(appBO.getCreatedBy());
			std.setCreatedDate(appBO.getCreatedDate());
			std.setAdmAppln(appBO);
			std.setIsActive(true);
			if(appBO!=null){
				
				appBO.setPersonalData(persData);
			}	
				
			boolean updated=txn.submitBasicPage(std,admForm);
			
			
			log.info("exit createApplicant");
			admForm.setDisplayPage("guidelines");
			return updated;
		}
	
		
		public List<AdmAppln> getSavedApplicantDetails(OnlineApplicationForm admForm) throws Exception {
			List<AdmAppln> admApp=new ArrayList<AdmAppln>();
			admApp=txn.getApplicantSavedDetails(admForm);
			return admApp;
		}
		
		public AdmAppln getAppliedApplicationDetails(OnlineApplicationForm admForm) throws Exception {
			AdmAppln admApp=new AdmAppln();
			admApp=txn.getAppliedApplnDetails(admForm);
			return admApp;
		}
		
		public List<CourseTO> getCourse(int id) throws Exception {
			ICourseTransaction iCourseTransaction = CourseTransactionImpl.getInstance();
			List<Course> courseList = iCourseTransaction.getCourse(id);
			List<CourseTO> courseToList = helper.copyCourseBosToTos(courseList, id);
			return courseToList;
		}
		
		public boolean createApplicant(AdmApplnTO applicantDetail,
				OnlineApplicationForm admForm,boolean isPresidance,String saveMode) throws Exception {
			log.info("enter createApplicant");
			AdmAppln appBO=null;
			Student admBO= null;
			appBO=txn.getAppliedApplnDetails(admForm);
			admBO=txn.getStudentDetailsFromAdmAppln(appBO.getId());
			if(admBO==null){
				admBO=new Student();
			}
			if(appBO!=null){
		 	appBO=helper.getApplicantBO(appBO,applicantDetail,admForm,saveMode);
		 	
			admBO.setIsAdmitted(false);
			if(appBO!=null){
				
				appBO.setId(Integer.parseInt(admForm.getAdmApplnId()));
				
				/*
				//raghu put comment
				//appBO.setIsCancelled(false);
				//appBO.setIsSelected(false);
				//appBO.setIsApproved(false);
				//appBO.setIsFinalMeritApproved(false);
				//appBO.setIsLig(false);
				//appBO.setIsBypassed(false);
				//appBO.setIsChallanVerified(false);
				
				
				admBO.setCreatedBy(appBO.getCreatedBy());
				admBO.setCreatedDate(appBO.getCreatedDate());
				// setting the candidate Pre requisite details from detailApplicantCreate jsp.
				if(appBO.getCandidatePrerequisiteMarks()!=null && !appBO.getCandidatePrerequisiteMarks().isEmpty()){
					Iterator<CandidatePrerequisiteMarks> candidatePreRequisiteMarksItr=appBO.getCandidatePrerequisiteMarks().iterator();
					while (candidatePreRequisiteMarksItr.hasNext()) {
						CandidatePrerequisiteMarks preRequisiteMarks = (CandidatePrerequisiteMarks) candidatePreRequisiteMarksItr.next();
						preRequisiteMarks.setPrerequisiteMarksObtained(new BigDecimal(applicantDetail.getPreRequisiteObtMarks()));
						preRequisiteMarks.setRollNo(applicantDetail.getPreRequisiteRollNo());
						preRequisiteMarks.setExamMonth(Integer.parseInt(applicantDetail.getPreRequisiteExamMonth()));
						preRequisiteMarks.setExamYear(Integer.parseInt(applicantDetail.getPreRequisiteExamYear()));
					}
				}
				Set<AdmapplnAdditionalInfo> admAddnSet=new HashSet<AdmapplnAdditionalInfo>();
				AdmapplnAdditionalInfo additionalInfo=new AdmapplnAdditionalInfo();
				if(applicantDetail.getTitleOfFather()!=null && !applicantDetail.getTitleOfFather().isEmpty())
					additionalInfo.setTitleFather(applicantDetail.getTitleOfFather());
				if(applicantDetail.getTitleOfMother()!=null && !applicantDetail.getTitleOfMother().isEmpty())
					additionalInfo.setTitleMother(applicantDetail.getTitleOfMother());
				if(applicantDetail.getApplicantFeedbackId()!=null && !applicantDetail.getApplicantFeedbackId().isEmpty()){
					ApplicantFeedback feedback=new ApplicantFeedback();
					feedback.setId(Integer.parseInt(applicantDetail.getApplicantFeedbackId()));
					additionalInfo.setApplicantFeedback(feedback);
				}
				if(applicantDetail.getInternationalCurrencyId()!=null && !applicantDetail.getInternationalCurrencyId().isEmpty()){
					Currency curr=new Currency();
					curr.setId(Integer.parseInt(applicantDetail.getInternationalCurrencyId()));
					additionalInfo.setInternationalApplnFeeCurrency(curr);
				}
				if(applicantDetail.getIsComeDk()!=null ){
					if(applicantDetail.getIsComeDk())
						additionalInfo.setIsComedk(applicantDetail.getIsComeDk());
					else
						additionalInfo.setIsComedk(false);
				}else{
					additionalInfo.setIsComedk(false);
				}
				
					
				//raghu
				additionalInfo.setIsSpotAdmission(false);
				
				additionalInfo.setCreatedBy(appBO.getCreatedBy());
				additionalInfo.setCreatedDate(new Date());
				additionalInfo.setModifiedBy(appBO.getCreatedBy());
				additionalInfo.setLastModifiedDate(new Date());
				additionalInfo.setBackLogs(admForm.isBackLogs());
				//raghu
				additionalInfo.setIsSaypass(admForm.getIsSaypass());
				
				//raghu
				if(applicantDetail.getAdmapplnAdditionalInfos()!=null && applicantDetail.getAdmapplnAdditionalInfos().size()!=0){
					List<AdmapplnAdditionalInfo> addInfo=new ArrayList<AdmapplnAdditionalInfo>(applicantDetail.getAdmapplnAdditionalInfos());
					additionalInfo.setId(addInfo.get(0).getId());
				}
				
				admAddnSet.add(additionalInfo);
				appBO.setAdmapplnAdditionalInfo(admAddnSet);
				*/}
			}
			admBO.setAdmAppln(appBO);
			boolean updated=txn.createNewApplicant(admBO, admForm,saveMode);
			
			
			log.info("exit createApplicant");
			
			return updated;
		}
		/**
		 * add ApplnDoc BOs to session
		 * @param admForm
		 * @param session
		 * @throws Exception
		 */
		public void persistAdmissionFormAttachments(OnlineApplicationForm admForm,HttpSession session) throws Exception {
			log.info("Enter persistAdmissionFormAttachments ...");
			
			
			List<ApplnDoc> uploadList=helper.getDocUploadBO(admForm.getUploadDocs(),admForm.getUserId());
			session.setAttribute(CMSConstants.STUDENT_DOCUMENT_DETAILS, uploadList);
			log.info("Exit persistAdmissionFormAttachments ...");
		}
		
		/**
		 * get resident category list
		 * @return
		 */
		public List<ResidentCategoryTO> getResidentTypes()throws Exception{
			log.info("Enter getResidentTypes ...");
			
			List<ResidentCategory> residentbos=txn.getResidentTypes();
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<ResidentCategoryTO> residents=helper.convertResidentBOToTO(residentbos);
			log.info("Exit getResidentTypes ...");
			return residents;
		}
		/**
		 * save student data tom session
		 * @param studentpersonaldata
		 * @param admForm
		 * @return
		 */
		public boolean saveStudentPersonaldataToSession(PersonalDataTO studentpersonaldata, OnlineApplicationForm admForm,HttpSession session) throws Exception {
			log.info("Enter saveStudentPersonaldataToSession ...");
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			PersonalData studentdata=helper.convertPersonalDataTOtoBO(studentpersonaldata);
			studentdata.setCreatedBy(admForm.getUserId());
			studentdata.setCreatedDate(new Date());
			// changed to current address mandatory.
			AddressTO permAddTo=admForm.getTempAddr();
			Address permAddBo=helper.convertPermanentAddressTOToBO(permAddTo);
			Address temAddrBO=null;
			if(admForm.isSameTempAddr()){
				temAddrBO=permAddBo;
			}else
			{
				temAddrBO=helper.convertPermanentAddressTOToBO(admForm.getPermAddr());
			}
			


			/*PreferenceTO firstpref= admForm.getFirstPref();
			PreferenceTO secpref= admForm.getSecondPref();
			PreferenceTO thirdpref= admForm.getThirdPref();*/
			
			//List<CandidatePreference> preferenceBos= helper.convertPreferenceTOToBO(firstpref,secpref,thirdpref,session);
			//work experience set
			/*Set<ApplicantWorkExperience> workExperiences = new HashSet<ApplicantWorkExperience>();
			if(!CMSConstants.LINK_FOR_CJC)
				workExperiences=helper.convertExperienceTostoBOs(admForm);*/
			// maintain session data
			if(session.getAttribute(CMSConstants.APPLICATION_DATA)!=null)
			{
				log.info("application no set session application data...");
				AdmAppln applicationdata=(AdmAppln)session.getAttribute(CMSConstants.APPLICATION_DATA);
				//applicationdata.setApplicantWorkExperiences(workExperiences);
//				// assigned first preference course as selected course by default
//				if(firstpref!=null && firstpref.getCourseId()!=null && !StringUtils.isEmpty(firstpref.getCourseId()) && StringUtils.isNumeric(firstpref.getCourseId()) )
//				{
//					Course crs= new Course();
//					crs.setId(Integer.parseInt(firstpref.getCourseId()));
//					applicationdata.setCourseBySelectedCourseId(crs);
//				}
				
				if (!admForm.isOnlineApply()) {
					applicationdata.setApplnNo(Integer.parseInt(admForm
							.getApplicationNumber()));
					if (!checkApplicationNoUniqueForYear(applicationdata)) {
						return false;
					}
				}
				session.setAttribute(CMSConstants.APPLICATION_DATA, applicationdata);
			}
			
			session.setAttribute(CMSConstants.STUDENT_PERSONAL_DATA, studentdata);
			log.info("student data set to session ...");
			session.setAttribute(CMSConstants.STUDENT_PERMANENT_ADDRESS, temAddrBO);
			log.info("perm address set to session ...");
			session.setAttribute(CMSConstants.STUDENT_COMM_ADDRESS,permAddBo );
			log.info("comm addr set to session ...");
			/*if(preferenceBos!=null && !preferenceBos.isEmpty()){
			session.setAttribute(CMSConstants.STUDENT_PREFERENCES, preferenceBos);
			}*/
			log.info("preferences set to session ...");
			return true;
		}
		
		
		/**
		 * application details add to session
		 * @param admForm
		 * @param session
		 * @return
		 */
		public boolean saveApplicationDetailsToSession(OnlineApplicationForm admForm)throws Exception {
			log.info("Enter saveApplicationDetailsToSession ...");
			boolean result=false;
			AdmAppln appln=new AdmAppln();
			appln=txn.getAppliedApplnDetails(admForm);
			if(admForm.getMihpayid() != null){
				admForm.setSelectedFeePayment("OnlinePayment");
			}
			//save challan
			if(admForm.getSelectedFeePayment()!=null && !admForm.getSelectedFeePayment().isEmpty() && admForm.getSelectedFeePayment().equalsIgnoreCase("SBI")){
				if(admForm.getApplicationAmount()!=null && !StringUtils.isEmpty(admForm.getApplicationAmount())&& CommonUtil.isValidDecimal(admForm.getApplicationAmount())){
					appln.setAmount(new BigDecimal(admForm.getApplicationAmount()));
				}else{
						appln.setAmount(new BigDecimal("0.0"));
				}
				appln.setChallanRefNo(admForm.getChallanNo());
				appln.setJournalNo(admForm.getJournalNo());
				appln.setBankBranch(admForm.getBankBranch());
				appln.setDate(CommonUtil.ConvertStringToSQLDate(admForm.getApplicationDate()));
				//raghu
				appln.setMode("Challan");
				appln.setIsChallanRecieved(false);
				appln.setIsChallanVerified(false);
				admForm.setMode("Challan");
				
			} 
			//save neft detail
			else if(admForm.getSelectedFeePayment()!=null && !admForm.getSelectedFeePayment().isEmpty() && admForm.getSelectedFeePayment().equalsIgnoreCase("NEFT")){
					if(admForm.getApplicationAmount()!=null && !StringUtils.isEmpty(admForm.getApplicationAmount())&& CommonUtil.isValidDecimal(admForm.getApplicationAmount())){
						appln.setAmount(new BigDecimal(admForm.getApplicationAmount()));
					}else{
							appln.setAmount(new BigDecimal("0.0"));
					}
					appln.setChallanRefNo(admForm.getChallanNo());
					appln.setJournalNo(admForm.getJournalNo());
					appln.setBankBranch(admForm.getBankBranch());
					appln.setDate(CommonUtil.ConvertStringToSQLDate(admForm.getApplicationDate()));
					//raghu
					appln.setMode("NEFT");
					appln.setIsChallanRecieved(false);
					appln.setIsChallanVerified(false);
					admForm.setMode("NEFT");
					
				}
				
			//online payment store
			
			else if(admForm.getSelectedFeePayment()!=null && !admForm.getSelectedFeePayment().isEmpty() 
					&& admForm.getSelectedFeePayment().equalsIgnoreCase("OnlinePayment")){
				if(admForm.getTxnAmt()!=null && !StringUtils.isEmpty(admForm.getTxnAmt())&& CommonUtil.isValidDecimal(admForm.getTxnAmt())){
					appln.setAmount(new BigDecimal(admForm.getTxnAmt()));
				}else{
						appln.setAmount(new BigDecimal("0.0"));
				}
				appln.setJournalNo(admForm.getTxnRefNo());
				appln.setDate(CommonUtil.ConvertStringToSQLDate(admForm.getTxnDate()));
				if(admForm.getApplicationNumber()!=null && !admForm.getApplicationNumber().isEmpty())
				appln.setApplnNo(Integer.parseInt(admForm.getApplicationNumber()));
				appln.setMode("Online");
				appln.setIsChallanRecieved(false);
				appln.setIsChallanVerified(false);
				admForm.setMode("Online");
			}
			
			result=txn.saveChallanDetail(appln, admForm);
			if(result){
				admForm.setCurrentPageNo("payment");
				admForm.setDisplayPage("paymentsuccess");
				admForm.setOnlineApply(true);
				admForm.setDataSaved(true);
				
			}else{
				admForm.setCurrentPageNo("basic");
				admForm.setDisplayPage("payment");
				
			}
			//result=true;
			log.info("application details set to session ...");
			return result;
		}
		
		
		
		/**
		 * application details add to session
		 * @param admForm
		 * @param session
		 * @return
		 */
		public boolean saveFeeSuccessPage(OnlineApplicationForm admForm,HttpSession session, ActionMessages err)throws Exception {
			log.info("Enter saveApplicationDetailsToSession ...");
			boolean result=false;
			AdmAppln appln=new AdmAppln();
			appln=txn.getAppliedApplnDetails(admForm);
			result=txn.savePaymentSuccessPage(appln, admForm);
			if(result){
				admForm.setCurrentPageNo("paymentsuccess");
				admForm.setDisplayPage("preferences");
				
				admForm.setDataSaved(true);
				
			}else{
				admForm.setCurrentPageNo("payment");
				admForm.setDisplayPage("paymentsuccess");
				
			}
			//result=true;
			log.info("application details set to session ...");
			return result;
		}
		
		
		//raghu
		
		/**
		 * application details add to session
		 * @param admForm
		 * @param session
		 * @return
		 */
		public boolean  saveCompleteApplicationGenerateNo(OnlineApplicationForm admForm,ActionMessages errors)throws Exception {
			log.info("Enter saveApplicationDetailsToSession ...");
			String appno="";
			boolean result=false;
			AdmAppln appln=new AdmAppln();
			appln=txn.getAppliedApplnDetails(admForm);
			//if new app create appno
			if(appln.getApplnNo()==0){
				appno=txn.saveCompleteApplicationGenerateNo(Integer.parseInt(admForm.getCourseId()), Integer.parseInt(admForm.getApplicationYear()), true, appln, admForm, errors);
				if(appno!=null){
					result=true;
				}
			}
			//if already app no is there no need create
			else{
				result=true;
				//close session
				HibernateUtil.closeSession();
			}
			
			log.info("application details set to session ...");
			return result;
		}
		
		
		
		/**
		 * application details add to session
		 * @param admForm
		 * @param session
		 * @return
		 */
		public boolean saveCompleteApplicationGenerateNoWithNoMoreEdit(AdmApplnTO applicantDetail,OnlineApplicationForm admForm,ActionMessages errors,String saveMode)throws Exception {
			log.info("Enter saveApplicationDetailsToSession ...");
			String appno="";
			boolean result=false;
			AdmAppln appBO=null;
			Student admBO= null;
			appBO=txn.getAppliedApplnDetails(admForm);
			
			//this one newly we have to generate app no so we have to merge 
			if(appBO.getApplnNo()==0){
				
				appno=txn.saveCompleteApplicationGenerateNoWithNoMoreEdit(Integer.parseInt(admForm.getCourseId()), Integer.parseInt(admForm.getApplicationYear()), true, appBO, admForm, errors);
				if(appno!=null){
					result=true;
					
				}
				
			}
			
			//this one already generated appno so we have to update
			else {
			
			admBO=txn.getStudentDetailsFromAdmAppln(appBO.getId());
				
			if(admBO==null){
				admBO=new Student();
			}
			if(appBO!=null){
		 	appBO=helper.getApplicantBO(appBO,applicantDetail,admForm,saveMode);
		 	
			admBO.setIsAdmitted(false);
			if(appBO!=null){
				
				appBO.setId(Integer.parseInt(admForm.getAdmApplnId()));
				
				/*
				appBO.setId(Integer.parseInt(admForm.getAdmApplnId()));
				//raghu put comment
				//appBO.setIsCancelled(false);
				//appBO.setIsSelected(false);
				//appBO.setIsApproved(false);
				//appBO.setIsFinalMeritApproved(false);
				//appBO.setIsLig(false);
				//appBO.setIsBypassed(false);
				//appBO.setIsChallanVerified(false);
				
				
				admBO.setCreatedBy(appBO.getCreatedBy());
				admBO.setCreatedDate(appBO.getCreatedDate());
				// setting the candidate Pre requisite details from detailApplicantCreate jsp.
				if(appBO.getCandidatePrerequisiteMarks()!=null && !appBO.getCandidatePrerequisiteMarks().isEmpty()){
					Iterator<CandidatePrerequisiteMarks> candidatePreRequisiteMarksItr=appBO.getCandidatePrerequisiteMarks().iterator();
					while (candidatePreRequisiteMarksItr.hasNext()) {
						CandidatePrerequisiteMarks preRequisiteMarks = (CandidatePrerequisiteMarks) candidatePreRequisiteMarksItr.next();
						preRequisiteMarks.setPrerequisiteMarksObtained(new BigDecimal(applicantDetail.getPreRequisiteObtMarks()));
						preRequisiteMarks.setRollNo(applicantDetail.getPreRequisiteRollNo());
						preRequisiteMarks.setExamMonth(Integer.parseInt(applicantDetail.getPreRequisiteExamMonth()));
						preRequisiteMarks.setExamYear(Integer.parseInt(applicantDetail.getPreRequisiteExamYear()));
					}
				}
				Set<AdmapplnAdditionalInfo> admAddnSet=new HashSet<AdmapplnAdditionalInfo>();
				AdmapplnAdditionalInfo additionalInfo=new AdmapplnAdditionalInfo();
				if(applicantDetail.getTitleOfFather()!=null && !applicantDetail.getTitleOfFather().isEmpty())
					additionalInfo.setTitleFather(applicantDetail.getTitleOfFather());
				if(applicantDetail.getTitleOfMother()!=null && !applicantDetail.getTitleOfMother().isEmpty())
					additionalInfo.setTitleMother(applicantDetail.getTitleOfMother());
				if(applicantDetail.getApplicantFeedbackId()!=null && !applicantDetail.getApplicantFeedbackId().isEmpty()){
					ApplicantFeedback feedback=new ApplicantFeedback();
					feedback.setId(Integer.parseInt(applicantDetail.getApplicantFeedbackId()));
					additionalInfo.setApplicantFeedback(feedback);
				}
				if(applicantDetail.getInternationalCurrencyId()!=null && !applicantDetail.getInternationalCurrencyId().isEmpty()){
					Currency curr=new Currency();
					curr.setId(Integer.parseInt(applicantDetail.getInternationalCurrencyId()));
					additionalInfo.setInternationalApplnFeeCurrency(curr);
				}
				if(applicantDetail.getIsComeDk()!=null ){
					if(applicantDetail.getIsComeDk())
						additionalInfo.setIsComedk(applicantDetail.getIsComeDk());
					else
						additionalInfo.setIsComedk(false);
				}else{
					additionalInfo.setIsComedk(false);
				}
				
					
				//raghu
				additionalInfo.setIsSpotAdmission(false);
				
				additionalInfo.setCreatedBy(appBO.getCreatedBy());
				additionalInfo.setCreatedDate(new Date());
				additionalInfo.setModifiedBy(appBO.getCreatedBy());
				additionalInfo.setLastModifiedDate(new Date());
				additionalInfo.setBackLogs(admForm.isBackLogs());
				//raghu
				additionalInfo.setIsSaypass(admForm.getIsSaypass());
				
				//raghu
				if(applicantDetail.getAdmapplnAdditionalInfos()!=null && applicantDetail.getAdmapplnAdditionalInfos().size()!=0){
					List<AdmapplnAdditionalInfo> addInfo=new ArrayList<AdmapplnAdditionalInfo>(applicantDetail.getAdmapplnAdditionalInfos());
					additionalInfo.setId(addInfo.get(0).getId());
				}
				
				admAddnSet.add(additionalInfo);
				appBO.setAdmapplnAdditionalInfo(admAddnSet);
				*/}
			}
			
			admBO.setAdmAppln(appBO);
			
		
			result=txn.createNewApplicant(admBO, admForm,saveMode);
			admForm.getApplicantDetails().setApplnNo(appBO.getApplnNo());
			
			}
			if(result) {
				send_email_new(admForm);
				send_sms_new(admForm);
			}
			
			log.info("application details set to session ...");
			return result;
		}
		
	
		/**
		 * prepare EdnQualificationTos for application form
		 * @param admForm
		 * @return
		 * @throws Exception
		 */
		public List<EdnQualificationTO> getEdnQualifications(OnlineApplicationForm admForm) throws Exception {
			log.info("Enter getEdnQualifications ...");
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<DocChecklist> exambos= txn.getExamtypes(Integer.parseInt(admForm.getCourseId()),Integer.parseInt(admForm.getApplicationYear()));
			
			
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			log.info("Exit getEdnQualifications ...");
			return helper.prepareQualificationsFromExamBos(exambos);
		}
		

		/**
		 * @param admForm
		 * @param session
		 * @throws Exception 
		 */
		@SuppressWarnings("unchecked")
		public void saveEducationDetailsToSession(OnlineApplicationForm admForm,
				HttpSession session,boolean isPresidance) throws Exception {
			log.info("Enter saveEducationDetailsToSession ...");
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<EdnQualification> educationDetails=helper.getEducationDetailsBO(admForm,isPresidance);
			Set<CandidateEntranceDetails> candidateentrances=new HashSet<CandidateEntranceDetails>();
			if(admForm.isDisplayEntranceDetails()){
				candidateentrances=helper.getCandidateEntranceDetails(admForm,candidateentrances);
			}
			session.setAttribute(CMSConstants.STUDENT_EDUCATION_DETAILS, educationDetails);
			
			AdmAppln applicationData=null;
			if(session.getAttribute(CMSConstants.APPLICATION_DATA)!=null){
				applicationData=(AdmAppln) session.getAttribute(CMSConstants.APPLICATION_DATA);
			}
			Set<ApplicantLateralDetails> lateraldata=null;
			if(session.getAttribute(CMSConstants.STUDENT_LATERALDETAILS)!=null){
				lateraldata=(HashSet<ApplicantLateralDetails>) session.getAttribute(CMSConstants.STUDENT_LATERALDETAILS);
			}
			Set<ApplicantTransferDetails> transferdata=null;
			if(session.getAttribute(CMSConstants.STUDENT_TRANSFERDETAILS)!=null){
				transferdata=(HashSet<ApplicantTransferDetails>) session.getAttribute(CMSConstants.STUDENT_TRANSFERDETAILS);
			}
				if(applicationData!=null){
					if(admForm.isDisplayTCDetails()){
						if(admForm.getTcDate()!=null && !StringUtils.isEmpty(admForm.getTcDate()) && CommonUtil.isValidDate(admForm.getTcDate())){
						applicationData.setTcDate(CommonUtil.ConvertStringToSQLDate(admForm.getTcDate()));
						}
						applicationData.setTcNo(admForm.getTcNo());
						if(admForm.getMarkcardDate()!=null && !StringUtils.isEmpty(admForm.getMarkcardDate()) && CommonUtil.isValidDate(admForm.getMarkcardDate())){
						applicationData.setMarkscardDate(CommonUtil.ConvertStringToSQLDate(admForm.getMarkcardDate()));
						}
						applicationData.setMarkscardNo(admForm.getMarkcardNo());
					}
					if(candidateentrances!=null && !candidateentrances.isEmpty()){
						applicationData.setCandidateEntranceDetailses(candidateentrances);
					}
					if(lateraldata!=null && !lateraldata.isEmpty()){
						applicationData.setApplicantLateralDetailses(lateraldata);
					}
					if(transferdata!=null && !transferdata.isEmpty()){
						applicationData.setApplicantTransferDetailses(transferdata);
					}
					session.setAttribute(CMSConstants.APPLICATION_DATA,applicationData);
				}
				
			
			
			log.info("Education details set to session ...");
		}
		
		/**
		 * create list of required doc.s to be uploaded in application form
		 * @param courseID
		 * @param appliedyear
		 * @return
		 * @throws Exception
		 */
		public List<ApplnDocTO> getRequiredDocList(String courseID, int appliedyear)throws Exception{
			log.info("Enter getRequiredDocList ...");
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<DocChecklist> checklistDocs=txn.getNeededDocumentList(courseID);
			log.info("Exit getRequiredDocList ...");
			return helper.createDocUploadListForCourse(checklistDocs,appliedyear);
		}
		/**
		 * get currency list
		 * @return
		 */
		public List<CurrencyTO> getCurrencies()throws Exception{
			log.info("Enter getCurrencies ...");
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<Currency> currancybos=txn.getCurrencies();
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<CurrencyTO> currencies=helper.convertCurrencyBOToTO(currancybos);
			log.info("Exit getCurrencies ...");
			return currencies;
		}
		/**
		 * get income list
		 * @return
		 */
		public List<IncomeTO> getIncomes()throws Exception{
			log.info("Enter getIncomes ...");
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<Object[]> incomebos=txn.getIncomes();
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<IncomeTO> currencies=helper.convertIncomeBOToTO(incomebos);
			log.info("Exit getIncomes ...");
			return currencies;
		}
		/**
		 * retrives everything from session and saves it...
		 * @param session
		 * @param admForm
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public boolean saveCompleteApplication(HttpSession session,
				OnlineApplicationForm admForm) throws Exception {
			log.info("Entered Save complete application in handler ...");
			PersonalData personaldata=updateParentdata(session,admForm);
			AdmAppln applicationData=null;
			Address permAddr=null;
			Address commAddr=null;
			List<CandidatePreference> preference=null;
			List<EdnQualification> qualifications=null;
			List<ApplnDoc> uploads=null;
			
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			if(session.getAttribute(CMSConstants.APPLICATION_DATA)!=null)
				applicationData=(AdmAppln)session.getAttribute(CMSConstants.APPLICATION_DATA);
			if(session.getAttribute(CMSConstants.STUDENT_PERMANENT_ADDRESS)!=null)
				permAddr=(Address)session.getAttribute(CMSConstants.STUDENT_PERMANENT_ADDRESS);
			if(session.getAttribute(CMSConstants.STUDENT_COMM_ADDRESS)!=null)
				commAddr=(Address)session.getAttribute(CMSConstants.STUDENT_COMM_ADDRESS);
				updateAddressToPersonalData(permAddr,commAddr,personaldata);
			if(session.getAttribute(CMSConstants.STUDENT_PREFERENCES)!=null)
				preference=(List<CandidatePreference>)session.getAttribute(CMSConstants.STUDENT_PREFERENCES);
			if(session.getAttribute(CMSConstants.STUDENT_EDUCATION_DETAILS)!=null)
				qualifications=(List<EdnQualification>)session.getAttribute(CMSConstants.STUDENT_EDUCATION_DETAILS);
			if(session.getAttribute(CMSConstants.STUDENT_DOCUMENT_DETAILS)!=null)
				uploads=(List<ApplnDoc>)session.getAttribute(CMSConstants.STUDENT_DOCUMENT_DETAILS);
			
			if(applicationData!=null)
			{

				
				if(!checkApplicationNoUniqueForYear(applicationData))
				{
					return false;
				}
			
				session.setAttribute(CMSConstants.APPLICATION_DATA, applicationData);
			}
			
			
			Student newStudent=createStudentBO(applicationData, personaldata,preference, qualifications, uploads);
			boolean result=txn.persistCompleteApplicationData(newStudent, admForm);
			log.info("Exit Save complete application in handler ..."+result);
			return result;
		}
		
		
		/**
		 * retrives everything from session and makes a admappln to object..
		 * @param session
		 * @param admForm
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public AdmApplnTO getCompleteApplication(HttpSession session,
				OnlineApplicationForm admForm) throws Exception {
			log.info("Entered Save complete application in handler ...");
			PersonalData personaldata=updateParentdata(session,admForm);
			AdmAppln applicationData=null;
			Address permAddr=null;
			Address commAddr=null;
			List<CandidatePreference> preference=null;
			List<EdnQualification> qualifications=null;
			List<ApplnDoc> uploads=null;
			
			//IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			if(session.getAttribute(CMSConstants.APPLICATION_DATA)!=null)
				applicationData=(AdmAppln)session.getAttribute(CMSConstants.APPLICATION_DATA);
			if(session.getAttribute(CMSConstants.STUDENT_PERMANENT_ADDRESS)!=null)
				permAddr=(Address)session.getAttribute(CMSConstants.STUDENT_PERMANENT_ADDRESS);
			if(session.getAttribute(CMSConstants.STUDENT_COMM_ADDRESS)!=null)
				commAddr=(Address)session.getAttribute(CMSConstants.STUDENT_COMM_ADDRESS);
				updateAddressToPersonalData(permAddr,commAddr,personaldata);
			if(session.getAttribute(CMSConstants.STUDENT_PREFERENCES)!=null)
				preference=(List<CandidatePreference>)session.getAttribute(CMSConstants.STUDENT_PREFERENCES);
			if(session.getAttribute(CMSConstants.STUDENT_EDUCATION_DETAILS)!=null)
				qualifications=(List<EdnQualification>)session.getAttribute(CMSConstants.STUDENT_EDUCATION_DETAILS);
			if(session.getAttribute(CMSConstants.STUDENT_DOCUMENT_DETAILS)!=null)
				uploads=(List<ApplnDoc>)session.getAttribute(CMSConstants.STUDENT_DOCUMENT_DETAILS);
			
//			if(applicationData!=null)
//			{
	//
//				
//				if(!checkApplicationNoUniqueForYear(applicationData))
//				{
//					return false;
//				}
//			
//				session.setAttribute(CMSConstants.APPLICATION_DATA, applicationData);
//			}
			
			
			Student newStudent=createStudentBO(applicationData, personaldata,preference, qualifications, uploads);
			AdmApplnTO applicantdetails=OnlineApplicationHelper.getInstance().copyPropertiesValue(newStudent.getAdmAppln(),session);
			applicantdetails.setOriginalPreferences(newStudent.getAdmAppln().getCandidatePreferences());
			log.info("Exit get complete applicant details in handler ...");
			return applicantdetails;
		}
		
		/**
		 * checks application number already exists for year and course or not
		 * @param applicationData
		 * @param courseId
		 * @return
		 * @throws Exception
		 */
		public boolean checkApplicationNoUniqueForYear(AdmAppln applicationData)throws Exception {
			log.info("Enter checkApplicationNoUniqueForYear ...");
			int applnNo=applicationData.getApplnNo();
			boolean unique=false;
			int year=0;
			if(applicationData.getAppliedYear()!=null)
				year=applicationData.getAppliedYear().intValue();
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			unique=txn.checkApplicationNoUniqueForYear(applnNo,year);
			log.info("Exit checkApplicationNoUniqueForYear ...");
			return unique;
		}
		
		
		/**
		 * adds address details to personal data
		 * @param permAddr
		 * @param commAddr
		 * @param personaldata
		 */
		private void updateAddressToPersonalData(Address permAddr,
				Address commAddr, PersonalData personaldata) {
			log.info("Enter updateAddressToPersonalData ...");
			if(permAddr!=null){
				personaldata.setPermanentAddressLine1(permAddr.getAddrLine1());
				personaldata.setPermanentAddressLine2(permAddr.getAddrLine2());
				personaldata.setPermanentAddressZipCode(permAddr.getPinCode());
				personaldata.setCityByPermanentAddressCityId(permAddr.getCity());
				if(permAddr.getState()!=null){
					personaldata.setStateByPermanentAddressStateId(permAddr.getState());
				}else{
					personaldata.setStateByPermanentAddressStateId(null);
					personaldata.setPermanentAddressStateOthers(permAddr.getStateOthers());
				}
				personaldata.setCountryByPermanentAddressCountryId(permAddr.getCountry());
			}
			if(commAddr!=null){
				personaldata.setCurrentAddressLine1(commAddr.getAddrLine1());
				personaldata.setCurrentAddressLine2(commAddr.getAddrLine2());
				personaldata.setCurrentAddressZipCode(commAddr.getPinCode());
				personaldata.setCityByCurrentAddressCityId(commAddr.getCity());
				if(commAddr.getState()!=null){
					personaldata.setStateByCurrentAddressStateId(commAddr.getState());
				}else{
					personaldata.setCurrentAddressStateOthers(commAddr.getStateOthers());
				}
				personaldata.setCountryByCurrentAddressCountryId(commAddr.getCountry());
			}
			log.info("Exit updateAddressToPersonalData ...");
		}
		
		
		/**
		 * creates student BO to persist
		 * @param applicationData
		 * @param personaldata
		 * @param preference
		 * @param qualifications
		 * @param uploads
		 * @return
		 */
		private Student createStudentBO(AdmAppln applicationData,PersonalData personaldata,List<CandidatePreference> preference,List<EdnQualification> qualifications,List<ApplnDoc> uploads) {
			log.info("Entered complete object graph creation ...");
			Student newStudent= new Student();
			newStudent.setCreatedBy(applicationData.getCreatedBy());
			newStudent.setCreatedDate(new Date());
			if (preference!=null && !preference.isEmpty()) {
				Set<CandidatePreference> preferenceset = new HashSet<CandidatePreference>();
				preferenceset.addAll(preference);
				applicationData.setCandidatePreferences(preferenceset);
			}
			if (uploads!=null && !uploads.isEmpty()) {
				Set<ApplnDoc> uploadSet = new HashSet<ApplnDoc>();
				uploadSet.addAll(uploads);
				applicationData.setApplnDocs(uploadSet);
			}
			if (qualifications!=null && !qualifications.isEmpty()) {
				Set<EdnQualification> qualfSet = new HashSet<EdnQualification>();
				qualfSet.addAll(qualifications);
				personaldata.setEdnQualifications(qualfSet);
			}
			applicationData.setPersonalData(personaldata);
			newStudent.setAdmAppln(applicationData);
			log.info("Exit complete object graph creation ...");
			return newStudent;
		}
		
		
		/**
		 * adds parent data to personal data
		 * @param session
		 * @return
		 */
		private PersonalData updateParentdata(HttpSession session,OnlineApplicationForm admForm) {
			log.info("Enter updateParentdata ...");
			PersonalData studentdata=null;
			if(session.getAttribute(CMSConstants.STUDENT_PERSONAL_DATA)!=null)
				studentdata=(PersonalData)session.getAttribute(CMSConstants.STUDENT_PERSONAL_DATA);
			if(studentdata!=null){
				studentdata.setCreatedBy(admForm.getUserId());
				studentdata.setCreatedDate(new Date());
				studentdata.setFatherEducation(admForm.getFatherEducation());
				studentdata.setMotherEducation(admForm.getMotherEducation());
				
				studentdata.setFatherName(admForm.getFatherName());
				studentdata.setMotherName(admForm.getMotherName());
				
				studentdata.setFatherEmail(admForm.getFatherEmail());
				studentdata.setMotherEmail(admForm.getMotherEmail());
				
				
				if (admForm.getFatherIncome()!=null && !StringUtils.isEmpty(admForm.getFatherIncome()) && StringUtils.isNumeric(admForm.getFatherIncome())) {
					Income fatherIncome = new Income();
					if (admForm.getFatherCurrency() != null
							&& !StringUtils.isEmpty(admForm.getFatherCurrency())
							&& StringUtils.isNumeric(admForm.getFatherCurrency())) {
						Currency fatherCurrency = new Currency();
						fatherCurrency.setId(Integer.parseInt(admForm
								.getFatherCurrency()));
						fatherIncome.setCurrency(fatherCurrency);
						studentdata
								.setCurrencyByFatherIncomeCurrencyId(fatherCurrency);
					} else {
						fatherIncome.setCurrency(null);
						studentdata.setCurrencyByFatherIncomeCurrencyId(null);
					}
					
					fatherIncome.setId(Integer.parseInt(admForm.getFatherIncome()));
					studentdata.setIncomeByFatherIncomeId(fatherIncome);
				}else{
					studentdata.setIncomeByFatherIncomeId(null);
					if (admForm.getFatherCurrency() != null
							&& !StringUtils.isEmpty(admForm.getFatherCurrency())
							&& StringUtils.isNumeric(admForm.getFatherCurrency())) {
						Currency fatherCurrency = new Currency();
						fatherCurrency.setId(Integer.parseInt(admForm
								.getFatherCurrency()));
						studentdata
								.setCurrencyByFatherIncomeCurrencyId(fatherCurrency);
					} else {
						studentdata.setCurrencyByFatherIncomeCurrencyId(null);
					}
				}
				if (admForm.getMotherIncome()!=null && !StringUtils.isEmpty(admForm.getMotherIncome()) && StringUtils.isNumeric(admForm.getMotherIncome())) {
					Income motherIncome = new Income();
					if (admForm.getMotherCurrency() != null
							&& !StringUtils.isEmpty(admForm.getMotherCurrency())
							&& StringUtils.isNumeric(admForm.getMotherCurrency())) {
						Currency motherCurrency = new Currency();
						motherCurrency.setId(Integer.parseInt(admForm
								.getMotherCurrency()));
						motherIncome.setCurrency(motherCurrency);
						studentdata
								.setCurrencyByMotherIncomeCurrencyId(motherCurrency);
					} else {
						motherIncome.setCurrency(null);
						studentdata.setCurrencyByMotherIncomeCurrencyId(null);
					}
					
					
					motherIncome.setId(Integer.parseInt(admForm.getMotherIncome()));
					studentdata.setIncomeByMotherIncomeId(motherIncome);
				}else{
					studentdata.setIncomeByMotherIncomeId(null);
					if (admForm.getMotherCurrency() != null
							&& !StringUtils.isEmpty(admForm.getMotherCurrency())
							&& StringUtils.isNumeric(admForm.getMotherCurrency())) {
						Currency motherCurrency = new Currency();
						motherCurrency.setId(Integer.parseInt(admForm
								.getMotherCurrency()));
						studentdata
								.setCurrencyByMotherIncomeCurrencyId(motherCurrency);
					} else {
						studentdata.setCurrencyByMotherIncomeCurrencyId(null);
					}
				}
				
				if(admForm.getFatherOccupation()!=null && !StringUtils.isEmpty(admForm.getFatherOccupation()) && StringUtils.isNumeric(admForm.getFatherOccupation())){
				Occupation fatherOccupation= new Occupation();
				fatherOccupation.setId(Integer.parseInt(admForm.getFatherOccupation()));
				studentdata.setOccupationByFatherOccupationId(fatherOccupation);
				}else{
					studentdata.setOccupationByFatherOccupationId(null);
				}
				if(admForm.getMotherOccupation()!=null && !StringUtils.isEmpty(admForm.getMotherOccupation()) && StringUtils.isNumeric(admForm.getMotherOccupation())){
				Occupation motherOccupation= new Occupation();
				motherOccupation.setId(Integer.parseInt(admForm.getMotherOccupation()));
				studentdata.setOccupationByMotherOccupationId(motherOccupation);
				}else{
					studentdata.setOccupationByMotherOccupationId(null);
				}
				
				AddressTO parentAddress=admForm.getParentAddress();
				AddressTO guardianAddress=admForm.getGuardianAddress();
				studentdata.setParentAddressLine1(parentAddress.getAddrLine1());
				studentdata.setGuardianAddressLine1(guardianAddress.getAddrLine1());
				studentdata.setParentAddressLine2(parentAddress.getAddrLine2());
				studentdata.setGuardianAddressLine2(guardianAddress.getAddrLine2());
				studentdata.setParentAddressLine3(parentAddress.getAddrLine3());
				studentdata.setGuardianAddressLine3(guardianAddress.getAddrLine3());
				studentdata.setParentAddressZipCode(parentAddress.getPinCode());
				studentdata.setGuardianAddressZipCode(guardianAddress.getPinCode());
				if(parentAddress.getCountryId()!=null && !StringUtils.isEmpty(parentAddress.getCountryId()) && StringUtils.isNumeric(parentAddress.getCountryId())){
				Country parentcountry= new Country();
				parentcountry.setId(Integer.parseInt(parentAddress.getCountryId()));
				studentdata.setCountryByParentAddressCountryId(parentcountry);
				}else{
					studentdata.setCountryByParentAddressCountryId(null);
				}
				if(guardianAddress.getCountryId()!=null && !StringUtils.isEmpty(guardianAddress.getCountryId()) && StringUtils.isNumeric(guardianAddress.getCountryId())){
					Country parentcountry= new Country();
					parentcountry.setId(Integer.parseInt(guardianAddress.getCountryId()));
					studentdata.setCountryByGuardianAddressCountryId(parentcountry);
					}else{
						studentdata.setCountryByGuardianAddressCountryId(null);
					}
				if (parentAddress.getStateId()!=null && !StringUtils.isEmpty(parentAddress.getStateId() ) && StringUtils.isNumeric(parentAddress.getStateId())) {
					State parentState = new State();
					parentState.setId(Integer.parseInt(parentAddress.getStateId()));
					studentdata.setStateByParentAddressStateId(parentState);
				}else {
					studentdata.setStateByParentAddressStateId(null);
					studentdata.setParentAddressStateOthers(parentAddress.getOtherState());
				}
				if (guardianAddress.getStateId()!=null && !StringUtils.isEmpty(guardianAddress.getStateId() ) && StringUtils.isNumeric(guardianAddress.getStateId())) {
					State parentState = new State();
					parentState.setId(Integer.parseInt(guardianAddress.getStateId()));
					studentdata.setStateByGuardianAddressStateId(parentState);
				}else {
					studentdata.setStateByGuardianAddressStateId(null);
					studentdata.setGuardianAddressStateOthers(guardianAddress.getOtherState());
				}
				studentdata.setCityByParentAddressCityId(parentAddress.getCity());
				studentdata.setCityByGuardianAddressCityId(guardianAddress.getCity());
				studentdata.setParentPh1(admForm.getParentPhone1());
				studentdata.setParentPh2(admForm.getParentPhone2());
				studentdata.setParentPh3(admForm.getParentPhone3());
				
				studentdata.setGuardianPh1(admForm.getGuardianPhone1());
				studentdata.setGuardianPh2(admForm.getGuardianPhone2());
				studentdata.setGuardianPh3(admForm.getGuardianPhone3());
				
				studentdata.setParentMob1(admForm.getParentMobile1());
				studentdata.setParentMob2(admForm.getParentMobile2());
				studentdata.setParentMob3(admForm.getParentMobile3());
				
				studentdata.setGuardianMob1(admForm.getGuardianMobile1());
				studentdata.setGuardianMob2(admForm.getGuardianMobile2());
				studentdata.setGuardianMob3(admForm.getGuardianMobile3());
				
				
				studentdata.setBrotherName(admForm.getBrotherName());
				studentdata.setBrotherEducation(admForm.getBrotherEducation());
				studentdata.setBrotherOccupation(admForm.getBrotherOccupation());
				studentdata.setBrotherIncome(admForm.getBrotherIncome());
				
				studentdata.setBrotherAge(admForm.getBrotherAge());
				studentdata.setGuardianName(admForm.getGuardianName());
				studentdata.setSisterName(admForm.getSisterName());
				studentdata.setSisterEducation(admForm.getSisterEducation());
				studentdata.setSisterOccupation(admForm.getSisterOccupation());
				studentdata.setSisterIncome(admForm.getSisterIncome());
				studentdata.setSisterAge(admForm.getSisterAge());
				
			}
			log.info("Exit updateParentdata ...");
			return studentdata;
		}
		/**
		 * creates list of preferences for application
		 * @param admForm
		 * @throws Exception
		 */
		/*public void populatePreferenceList(OnlineApplicationForm admForm) throws Exception {
			log.info("Enter populatePreferenceList ...");
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<Course> prefCourses=txn.getCourseForPreference(admForm.getCourseId());
			List<ProgramTO> prefPrograms= new ArrayList<ProgramTO>();
			List<ProgramTypeTO> prefProgramtypes= new ArrayList<ProgramTypeTO>();
			List<CourseTO> prefCoursetos= new ArrayList<CourseTO>();
			if(!prefCourses.isEmpty()){
				Iterator<Course> prefitr=prefCourses.iterator();
				while (prefitr.hasNext()) {
					Course prefCrs = (Course) prefitr.next();
					if (prefCrs.getIsActive()) {
						
						Program prefProg = prefCrs.getProgram();
						ProgramType prefProgtype = prefCrs.getProgram()
								.getProgramType();
						CourseTO toCrs = new CourseTO();
						toCrs.setId(prefCrs.getId());
						toCrs.setName(prefCrs.getName());
						prefCoursetos.add(toCrs);
						ProgramTO toProg = new ProgramTO();
						toProg.setId(prefProg.getId());
						toProg.setName(prefProg.getName());
						prefPrograms.add(toProg);
						ProgramTypeTO toProgtype = new ProgramTypeTO();
						toProgtype.setProgramTypeId(prefProgtype.getId());
						toProgtype.setProgramTypeName(prefProgtype.getName());
						prefProgramtypes.add(toProgtype);
					}
				}
				
			} 
//			else {
//				CourseTO toCrs = new CourseTO();
//				toCrs.setId(Integer.parseInt(admForm.getCourseId()));
//				toCrs.setName(admForm.getCourseName());
//				prefCoursetos.add(toCrs);
//			}
			
			List<CourseTO> uniqueCourse=removeDuplicateCourses(prefCoursetos);
			List<ProgramTO> uniqueprograms=removeDuplicatePrograms(prefPrograms);
			List<ProgramTypeTO> uniqueprogramTypes=removeDuplicateProgramTypes(prefProgramtypes);
			admForm.setPrefcourses(uniqueCourse);
			admForm.setPrefprograms(uniqueprograms);
			admForm.setPrefProgramtypes(uniqueprogramTypes);
			log.info("Exit populatePreferenceList ...");
		}*/
		
		/**
		 * SETS DEFUALT PREFERENCE AS COURSE SELECTED
		 * @param admForm
		 */
		/*public void setDefaultPreferences(OnlineApplicationForm admForm) {
			PreferenceTO preferenceTO = new PreferenceTO();
			preferenceTO.setCourseId(admForm.getCourseId());
			preferenceTO.setFirstprefCourseName(admForm.getCourseName());
			admForm.setFirstPref(preferenceTO);
		}*/
		
		/**
		 * POPULATES PREFERENCE TOS FOR SELECTION
		 * @param prefTO
		 * @param courseID
		 * @throws Exception
		 */
		/*public void populatePreferenceTO(CandidatePreferenceTO prefTO,String courseId) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<Course> prefCourses=txn.getCourseForPreference(courseId);
			List<ProgramTO> prefPrograms= new ArrayList<ProgramTO>();
			List<ProgramTypeTO> prefProgramtypes= new ArrayList<ProgramTypeTO>();
			List<CourseTO> prefCoursetos= new ArrayList<CourseTO>();
			if(!prefCourses.isEmpty()){
				Iterator<Course> prefitr=prefCourses.iterator();
				while (prefitr.hasNext()) {
					Course prefCrs = (Course) prefitr.next();
					if (prefCrs.getIsActive()) {
						
						Program prefProg = prefCrs.getProgram();
						ProgramType prefProgtype = prefCrs.getProgram()
								.getProgramType();
						CourseTO toCrs = new CourseTO();
						toCrs.setId(prefCrs.getId());
						toCrs.setName(prefCrs.getName());
						prefCoursetos.add(toCrs);
						ProgramTO toProg = new ProgramTO();
						toProg.setId(prefProg.getId());
						toProg.setName(prefProg.getName());
						prefPrograms.add(toProg);
						ProgramTypeTO toProgtype = new ProgramTypeTO();
						toProgtype.setProgramTypeId(prefProgtype.getId());
						toProgtype.setProgramTypeName(prefProgtype.getName());
						prefProgramtypes.add(toProgtype);
					}
				}
				
			} 
//			else {
//				CourseTO toCrs = new CourseTO();
//				toCrs.setId(courseTO.getId());
//				toCrs.setName(courseTO.getName());
//				prefCoursetos.add(toCrs);
//			}
			List<CourseTO> uniqueCourse=removeDuplicateCourses(prefCoursetos);
			List<ProgramTO> uniqueprograms=removeDuplicatePrograms(prefPrograms);
			List<ProgramTypeTO> uniqueprogramTypes=removeDuplicateProgramTypes(prefProgramtypes);
			prefTO.setPrefcourses(uniqueCourse);
			prefTO.setPrefprograms(uniqueprograms);
			prefTO.setPrefProgramtypes(uniqueprogramTypes);
			
		}*/
		
		/**
		 * @param prefProgramtypes
		 * @return
		 */
		/*private List<ProgramTypeTO> removeDuplicateProgramTypes(
				List<ProgramTypeTO> prefProgramtypes) {
			Map<Integer, ProgramTypeTO> programtypeMap = new HashMap<Integer, ProgramTypeTO>();
			Iterator<ProgramTypeTO> itr = prefProgramtypes.iterator();
			ProgramTypeTO progTo;
			while(itr.hasNext()) {
				progTo = (ProgramTypeTO)itr.next();
				if(!programtypeMap.containsKey(Integer.valueOf((progTo.getProgramTypeId())))) {
					programtypeMap.put(Integer.valueOf((progTo.getProgramTypeId())), progTo);
				}
			}
			
			return new ArrayList<ProgramTypeTO>(programtypeMap.values());
		}*/
		/**
		 * @param prefPrograms
		 * @return
		 */
		/*private List<ProgramTO> removeDuplicatePrograms(List<ProgramTO> prefPrograms) {
			Map<Integer, ProgramTO> programToMap = new HashMap<Integer, ProgramTO>();
			Iterator<ProgramTO> itr = prefPrograms.iterator();
			ProgramTO progTo;
			while(itr.hasNext()) {
				progTo = (ProgramTO)itr.next();
				if(!programToMap.containsKey(Integer.valueOf((progTo.getId())))) {
					programToMap.put(Integer.valueOf((progTo.getId())), progTo);
				}
			}
			
			return new ArrayList<ProgramTO>(programToMap.values());
		}*/
		/**
		 * @param prefCoursetos
		 * @return
		 */
		/*private List<CourseTO> removeDuplicateCourses(List<CourseTO> prefCoursetos) {
			Map<Integer, CourseTO> courseToMap = new HashMap<Integer, CourseTO>();
			Iterator<CourseTO> itr = prefCoursetos.iterator();
			CourseTO courseTo;
			while(itr.hasNext()) {
				courseTo = (CourseTO)itr.next();
				if(!courseToMap.containsKey(Integer.valueOf((courseTo.getId())))) {
					courseToMap.put(Integer.valueOf((courseTo.getId())), courseTo);
				}
			}
			
			return new ArrayList<CourseTO>(courseToMap.values());
		}*/
		
		
		
		/**
		 * FETCHES ALL NATIONALITIES
		 * @return
		 * @throws Exception
		 */
		public List<NationalityTO> getNationalities()throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<Nationality> nationBOs=txn.getNationalities();
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<NationalityTO> nationTOs=helper.convertNationalityBOtoTO(nationBOs);
			return nationTOs;
		}
		/**
		 * generates challan NO.
		 * @return
		 * @throws Exception
		 */
		/*public String getNewChallanNo() throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			String curRefNo=txn.getCurrentChallanNo();
			String latestRefNo="";
			if(curRefNo!=null && StringUtils.isNumeric(curRefNo))
			{
				int curNO=Integer.parseInt(curRefNo);
				curNO++;
				latestRefNo=String.valueOf(curNO);
			}
			boolean updatedLatest=txn.updateLatestChallanNo(latestRefNo);
			if(updatedLatest){
			return latestRefNo;
			}else{
				return null;
			}
		}*/
		/**
		 * prerequisite preparation
		 * @param courseID
		 * @return
		 * @throws Exception
		 */
		/*public List<CoursePrerequisiteTO> getCoursePrerequisites(int courseID) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<CoursePrerequisite> requisiteBOs=txn.getCoursePrerequisites(courseID);
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			List<CoursePrerequisiteTO> requisiteTOs=helper.convertRequisiteBOstoTOS(requisiteBOs);
			return requisiteTOs;
		}*/
		/**
		 * checks validity of application number for the course
		 * @param applicationNumber
		 * @param onlineApply
		 * @param appliedyear
		 * @param courseId
		 * @return
		 * @throws Exception
		 */
		public boolean checkApplicationNoInRange(String applicationNumber,
				boolean onlineApply, int appliedyear, String courseId) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			int applnNo=0;
			if(StringUtils.isNumeric(applicationNumber)){
				applnNo=Integer.parseInt(applicationNumber);
				return txn.checkApplicationNoInRange(applnNo,onlineApply,appliedyear,courseId);
			}else{
				return false;
			}
			
		}
		/**
		 * checks validity of application number for the course
		 * @param appliedyear
		 * @param courseId
		 * @return
		 * @throws Exception
		 */
		public boolean checkApplicationNoConfigured(int appliedyear, String courseId) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			
			return txn.checkApplicationNoConfigured(appliedyear,courseId);
		
		}
		/**
		 * generate application  number for online
		 * @param courseId
		 * @param year
		 * @param isOnline
		 * @return
		 * @throws Exception
		 */
		public String getGeneratedOnlineAppNo(String courseId, int year,boolean isOnline) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			return txn.getGeneratedOnlineAppNo(Integer.parseInt(courseId),year,isOnline);
			
		}
		/**
		 * checks work exp needed for course or not
		 * @param admForm
		 * @throws Exception
		 */
		/*public void setWorkExpNeeded(OnlineApplicationForm admForm) throws Exception{
			String courseID=admForm.getCourseId();
			CourseHandler handler= CourseHandler.getInstance();
			List<CourseTO> coursetos=handler.getCourse(Integer.parseInt(courseID));
			if(coursetos!=null && !coursetos.isEmpty()){
				CourseTO crsto=coursetos.get(0);
				if("Yes".equalsIgnoreCase(crsto.getIsWorkExperienceRequired()))
				{
					admForm.setWorkExpNeeded(true);
				}else{
					admForm.setWorkExpNeeded(false);
				}
			}
		}*/
		
		//edit section
		
		
		
		/*public boolean checkAdmitted(AdmApplnTO applicantDetail) throws Exception
		{
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			boolean admitted=txn.checkAdmittedOrNot(applicantDetail.getApplnNo(),applicantDetail.getSelectedCourse().getId(),applicantDetail.getAppliedYear());
			return admitted;
		}*/
		
		
		
		
		
		/**
		 * FETCHES APPLICANT DETAILS
		 * @param applicationNumber
		 * @param applicationYear
		 * @return appDetails
		 */
		public AdmApplnTO getApplicantDetails(String applicationNumber,
				int applicationYear,boolean admissionForm,OnlineApplicationForm admForm,HttpServletRequest request) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			OnlineApplicationHelper helper = OnlineApplicationHelper.getInstance();

			AdmAppln applicantDetails = txn.getApplicantDetails(applicationNumber, applicationYear,admissionForm);
			int admittedThroughId = 0;
			if(applicantDetails!= null){
				Set<EdnQualification> ednQualificationSet = applicantDetails.getPersonalData().getEdnQualifications();
				Iterator<EdnQualification> eduItr = ednQualificationSet.iterator();
				int uniId = 0;
				int instId = 0;
				while (eduItr.hasNext()) {
					EdnQualification ednQualification = (EdnQualification) eduItr
							.next();
					if(ednQualification.getDocChecklist()!= null && ednQualification.getDocChecklist().getIsPreviousExam()){
						if(ednQualification.getUniversity()!= null){
							uniId = ednQualification.getUniversity().getId();
						}
						if(ednQualification.getCollege()!= null){
							instId = ednQualification.getCollege().getId();
						}
					}
					
				
				}
				int natId = 0;
				int resCategory = 0;
				if(applicantDetails.getPersonalData()!= null && applicantDetails.getPersonalData().getResidentCategory()!= null){
					resCategory = applicantDetails.getPersonalData().getResidentCategory().getId();
				}
				if(applicantDetails.getPersonalData().getNationality()!= null){
					natId = applicantDetails.getPersonalData().getNationality().getId();
				}
				
				//-----------intstitute, nationality, university
				if(admittedThroughId == 0 && instId > 0){
					admittedThroughId = txn.getAdmittedThroughIdForInst(instId);
				}
				if(admittedThroughId == 0 && natId > 0){
					admittedThroughId = txn.getAdmittedThroughIdForNationality(natId);
				}
				if(admittedThroughId == 0 && uniId > 0){
					admittedThroughId = txn.getAdmittedThroughIdForUniveristy(uniId);
				}
				if(admittedThroughId == 0 && resCategory > 0){
					admittedThroughId = txn.getAdmittedThroughResidentCategory(resCategory);
				}
				//------------------
				
				
				if(admittedThroughId == 0 && instId > 0 && natId > 0){
					admittedThroughId = txn.getAdmittedThroughForInstNationality(instId, natId);
				}
				
				if(admittedThroughId == 0 && instId > 0 && uniId > 0){
					admittedThroughId =  txn.getAdmittedThroughForInstUni(instId, uniId);
				}
				if(admittedThroughId == 0 &&  instId > 0 &&  resCategory > 0 ){
					admittedThroughId =  txn.getAdmittedThroughForInstRes(instId, resCategory);
				}

				if(admittedThroughId == 0 && natId > 0 && uniId > 0){
					admittedThroughId =  txn.getAdmittedThroughForNatUni(natId, uniId);
				}
				if(admittedThroughId == 0 &&  natId > 0 &&  resCategory > 0 ){
					admittedThroughId =  txn.getAdmittedThroughForNatRes(natId, resCategory );
				}
				
				if(admittedThroughId == 0 &&  resCategory > 0 &&  uniId > 0 ){
					admittedThroughId = txn.getAdmittedThroughForResUni(resCategory, uniId);
				}
				
				
				if(admittedThroughId == 0 && instId > 0 && natId > 0 && uniId > 0 ){
					admittedThroughId = txn.getAdmittedThroughForInstNationalityUni(instId, natId, uniId);
				}
				
				
				if(admittedThroughId == 0 && instId > 0 && natId > 0 && resCategory > 0 ){
					admittedThroughId = txn.getAdmittedThroughForInstNatRes(instId, natId, resCategory);
				}
				if(admittedThroughId == 0 &&  natId > 0 && uniId > 0 && resCategory > 0 ){
					admittedThroughId = txn.getAdmittedThroughForNatUniRes(natId, uniId, resCategory);
				}

				if(admittedThroughId == 0 &&  uniId > 0 && resCategory > 0 && instId > 0){
					admittedThroughId = txn.getAdmittedThroughForUniResInst(uniId, resCategory, instId);
				}
				
				
				if(admittedThroughId == 0 && instId > 0 && natId > 0 && uniId > 0 && resCategory > 0){
					admittedThroughId = txn.getAdmittedThroughForInstNationalityUniRes(instId, natId, uniId, resCategory);
				}
//				set uniqueId to the form.
				if(applicantDetails.getStudentOnlineApplication()!=null ){
					 admForm.setUniqueId(String.valueOf(applicantDetails.getStudentOnlineApplication().getId()));
				}else{
					admForm.setUniqueId(null);
				}
			}
			
			if(admittedThroughId > 0){
				AdmittedThrough admittedThrough = new AdmittedThrough();
				admittedThrough.setId(admittedThroughId);
				admittedThrough.setIsActive(true);
				applicantDetails.setAdmittedThrough(admittedThrough);
			}
			
			if(applicantDetails!=null){
				Set<Student> students=applicantDetails.getStudents();
				if(students!=null && !students.isEmpty()){
					for(Student student:students){
						request.setAttribute("STUDENT_IMAGE", "images/StudentPhotos/"+student.getId()+".jpg?"+applicantDetails.getLastModifiedDate());
					}
					
				}
			}
			
			
			//to copy the BO properties to TO
			AdmApplnTO appDetails = helper.copyPropertiesValue(applicantDetails,request.getSession());
			return appDetails;
		}
		
		
		

		/**
		 * @param attendanceSummaryReportForm
		 * @return
		 * This method will build dynamic query
		 */
		/*private static String commonSearch(OnlineApplicationForm admForm) {
			log.info("entered commonSearch..");
			String searchCriteria = "";

			if (admForm.getApplicationYear().trim().length() > 0) {
				String appliedYear = " st.admAppln.appliedYear = "
					+ admForm.getApplicationYear();
				searchCriteria = searchCriteria + appliedYear;
			}
			if (admForm.getRegNumber()!=null && admForm.getRegNumber().trim().length() > 0) {
				String regNo = " and st.registerNo = "+ "'"+admForm.getRegNumber().trim()+"'";
				searchCriteria = searchCriteria + regNo;
			}
			if (admForm.getApplicationNumber()!=null && StringUtils.isNumeric(admForm.getApplicationNumber().trim()) && admForm.getApplicationNumber().trim().length() > 0) {
				String appNo = " and st.admAppln.applnNo = "+ Integer.parseInt(admForm.getApplicationNumber().trim());
				searchCriteria = searchCriteria + appNo;
			}
			if(admForm.getRollNo()!=null && admForm.getRollNo().trim().length() > 0){
				String rollNo = " and st.rollNo = "+ "'"+admForm.getRollNo().trim()+"'";
				searchCriteria = searchCriteria + rollNo;
			}
			if(admForm.getCourseId()!= null && admForm.getCourseId().trim().length() > 0){
				String courseId = " and st.admAppln.courseBySelectedCourseId.id = "+ admForm.getCourseId();
				searchCriteria = searchCriteria + courseId;
			}
			if(admForm.getProgramId()!= null && admForm.getProgramId().trim().length() > 0){
				String programId = " and st.admAppln.courseBySelectedCourseId.program.id = "+ admForm.getProgramId();
				searchCriteria = searchCriteria + programId;
			}
			if(admForm.getProgramTypeId()!= null && admForm.getProgramTypeId().trim().length() > 0){
				String ptypeId = " and st.admAppln.courseBySelectedCourseId.program.programType.id = "+ admForm.getProgramTypeId();
				searchCriteria = searchCriteria + ptypeId;
			}
			searchCriteria = searchCriteria + " and st.admAppln.isSelected = 1";
			log.info("exit commonSearch..");
			return searchCriteria;
		}*/

		/**
		 * @param studentSearchForm
		 * @return
		 * This method will give final query
		 */
		/*public static String getSelectionSearchCriteria(
				OnlineApplicationForm admForm) {
			log.info("entered getSelectionSearchCriteria..");
			String statusCriteria = commonSearch(admForm);

			String searchCriteria= "select st.admAppln.applnNo from Student st"
				+" where"+statusCriteria ;					
			log.info("exit getSelectionSearchCriteria..");
			return searchCriteria;

		}*/
		
		
		
	
		
		/**
		 * FETCHES LIST OF DETAIL SUBJECTS
		 * @param courseId
		 * @return
		 * @throws Exception
		 */
		public Map<Integer,String> getDetailedSubjectsByCourse(String courseId) throws Exception {
			Map<Integer,String> detailedSubMap = new HashMap<Integer,String>();
			List<DetailedSubjectsTO> detailedSubjectsList = DetailedSubjectsHandler.getInstance().getDetailedsubjectsByCourse(courseId);
			Iterator<DetailedSubjectsTO> itr = detailedSubjectsList.iterator();
			DetailedSubjectsTO detailedSubjectsTo;
			while(itr.hasNext()) {
				detailedSubjectsTo = itr.next();
				detailedSubMap.put(detailedSubjectsTo.getId(),detailedSubjectsTo.getSubjectName());
			}
		return detailedSubMap;	
		}
		/**
		 * SAVES PREREQUISITES TO SESSION
		 * @param session
		 * @param prerequisites
		 * @param userID
		 * @throws Exception 
		 */
		/*public boolean savePrerequisitesToSession(HttpSession session,OnlineApplicationForm admForm) throws Exception
		{
			boolean result=false;
			if(admForm.getCoursePrerequisites()!=null){
				Set<CandidatePrerequisiteMarks> prerequisiteSet= new HashSet<CandidatePrerequisiteMarks>();
				List<CandidatePrerequisiteMarks> prerequisitesList= new ArrayList<CandidatePrerequisiteMarks>();
				Iterator<CoursePrerequisiteTO> toItr=admForm.getCoursePrerequisites().iterator();
				while (toItr.hasNext()) {
					CoursePrerequisiteTO to = (CoursePrerequisiteTO) toItr.next();
					CandidatePrerequisiteMarks bo= new CandidatePrerequisiteMarks();
					if(to.getUserMark()!=0.0){
						if(to.getPrerequisiteTO()!=null){
						
						Prerequisite prereq=new Prerequisite();
						prereq.setId(to.getPrerequisiteTO().getId());
						bo.setPrerequisite(prereq);
						}
						else
						{
							bo.setPrerequisite(null);
						}
						
						AdmAppln adm=new AdmAppln();
						adm.setId(Integer.parseInt(admForm.getAdmApplnId()));
						bo.setAdmAppln(adm);
						bo.setPrerequisiteMarksObtained(new BigDecimal(to.getUserMark()));
						bo.setPrerequisiteTotalMarks(new BigDecimal(to.getTotalMark()));
						bo.setRollNo(to.getRollNo());
						bo.setExamMonth(to.getExamMonth());
						bo.setExamYear(Integer.parseInt(to.getExamYear()));
						bo.setIsActive(true);
						bo.setCreatedBy(admForm.getUserId());
						bo.setCreatedDate(new Date());
						bo.setLastModifiedDate(new Date());
						bo.setModifiedBy(admForm.getUserId());
						prerequisiteSet.add(bo);
						prerequisitesList.add(bo);
						}
				}
				
				session.setAttribute(CMSConstants.STUDENT_PREREQUISITES, prerequisiteSet);
				result=txn.savePrerequisitesPage(admForm,prerequisitesList);
			}
			return result;
		}*/
	
		/**
		 *  CHECKS DUPLICATE PREREQUISITE INFORMATION OR NOT
		 * @param examYear
		 * @param rollNo
		 * @return
		 * @throws Exception
		 */
		/*public boolean checkDuplicatePrerequisite(String examYear, String rollNo) throws Exception {
			int year=0;
			if(examYear!=null && !StringUtils.isEmpty(examYear) && StringUtils.isNumeric(examYear))
				year=Integer.parseInt(examYear);
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			boolean duplicate = txn.checkDuplicatePrerequisite(year,rollNo);
			return duplicate;
		}*/
		/**
		 * CHECKS DUPLICATE PAYMENT INFORMATION OR NOT
		 * @param challanNo
		 * @param journalNo
		 * @param applicationDate
		 * @param year
		 * @param firstName 
		 * @return
		 * @throws Exception
		 */
		public boolean checkPaymentDetails(String challanNo, String journalNo,
				String applicationDate, int year) throws Exception{
			java.sql.Date applnDate=CommonUtil.ConvertStringToSQLDate(applicationDate);
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			boolean duplicate = txn.checkPaymentDetailsDuplicate(challanNo,journalNo,applnDate,year);
			return duplicate;
		}
		
		
		
		/**
		 * FETCHES Application Details
		 * @param applicationNumber
		 * @param applicationYear
		 * @param regNO 
		 * @return appDetails
		 */
		public AdmApplnTO getApplicationDetails(String applicationNumber,
				int applicationYear, String regNO,HttpServletRequest request) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			OnlineApplicationHelper helper = OnlineApplicationHelper.getInstance();

			AdmAppln applicantDetails = txn.getApplicationDetails(applicationNumber, applicationYear,regNO);

			//to copy the BO properties to TO
			AdmApplnTO appDetails = helper.copyPropertiesValue(applicantDetails,request.getSession());

			return appDetails;
		}
		
			
		
		
		/*
		 * Single application start
		 */
		
		/**
		 * returns a blank student object
		 * @param stForm
		 * @return
		 */
		/*public AdmApplnTO getNewStudent(HttpSession session,OnlineApplicationForm stForm)throws Exception {
			log.info("Enter getNewStudent ...");
			OnlineApplicationHelper helper= OnlineApplicationHelper.getInstance();
			log.info("Exit getNewStudent ...");
			return helper.getNewStudent(session,stForm.getCourseId(),stForm);
		}*/
		
	
		/**
		 * @param id
		 * @return
		 * @throws Exception
		 */
		public List<DocTypeExamsTO> getDocExamsByID(int id) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			List<DocTypeExams> examBos=txn.getDocExamsByID(id);
			return OnlineApplicationHelper.getInstance().convertDocExamBosToTOS(examBos);
		}
		
		

		/*
		 * Single application end
		 */
		public int getApplicationYear(int courseId) throws Exception{
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			int year = txn.getAppliedYearForCourse(courseId);
			return year;
		}

		
		/**
		 * getting the course Map which contain Course Code and Id from the database.
		 * @param parseInt
		 * @param i
		 * @return
		 * @throws Exception
		 */
		public Map<String, Integer> getCoursesById(int id, int mode) throws Exception{
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			String searchQuery=OnlineApplicationHelper.getInstance().getSearchQuery(id,mode);
			return txn.getCourseMapByInputId(searchQuery);
		}
		
		
		
		
		/**
		 * @param studentSearchForm
		 * @return
		 * This method will give final query
		 */
		/*public static String getSelectionSearchCriteriaForApplicant(
				OnlineApplicationForm admForm) {
			log.info("entered getSelectionSearchCriteria..");
			String statusCriteria = commonSearch(admForm);

			String searchCriteria= "select st.admAppln from Student st"
				+" where"+statusCriteria ;					
			log.info("exit getSelectionSearchCriteria..");
			return searchCriteria;

		}*/
		
		
			/*public AdmApplnTO getApplicantStatusDetails(String applicationNumber,
				int applicationYear, int courseId,HttpServletRequest request) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();


			List applicantDetails = txn.getApplicantStatusDetails(applicationNumber, applicationYear, courseId);
				AdmApplnTO adminAppTO = null;
			if (applicantDetails != null) {
				adminAppTO = new AdmApplnTO();

				adminAppTO.setStatus(applicantDetails.getAdmStatus());
			}

			AdmApplnTO	adminAppTO = new AdmApplnTO();
			if (applicantDetails != null && !applicantDetails.isEmpty()) {
				String Status="";
				String ShortStatus="";
				Iterator itr = applicantDetails.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					if (obj[0] != null) {
						Status = (obj[0].toString());
						adminAppTO.setStatus(Status);
					}
					if (obj[1] != null) {
						ShortStatus = (obj[1].toString());
						adminAppTO.setShortStatus(ShortStatus);
					}
			      		
					
				}	
			}
			return adminAppTO;
		}*/
		
		/*public AdmApplnTO getApplicantStatusDetails(String applicationNumber,
				int applicationYear, int courseId) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();


			List applicantDetails = txn.getApplicantStatusDetails(applicationNumber, applicationYear, courseId);
				AdmApplnTO adminAppTO = null;
			if (applicantDetails != null) {
				adminAppTO = new AdmApplnTO();

				adminAppTO.setStatus(applicantDetails.getAdmStatus());
			}

			AdmApplnTO	adminAppTO = new AdmApplnTO();
			if (applicantDetails != null && !applicantDetails.isEmpty()) {
				String Status="";
				String ShortStatus="";
				Iterator itr = applicantDetails.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					if (obj[0] != null) {
						Status = (obj[0].toString());
						adminAppTO.setStatus(Status);
					}
					if (obj[1] != null) {
						ShortStatus = (obj[1].toString());
						adminAppTO.setShortStatus(ShortStatus);
					}
			      		
					
				}	
			}
			return adminAppTO;
		}*/
		
		
			
		public Map<String, String> getYear() throws Exception {
			return OnlineApplicationImpl.getInstance().getYear();
		}
		
		
		public Map<String, String> getYearByMonth(String Month) throws Exception {
			return OnlineApplicationImpl.getInstance().getYearByMonth(Month);
		}
		
		
		
		/**
		 * @return
		 * @throws Exception
		 */
		public String getParameterForPGI(OnlineApplicationForm admForm) throws Exception {/*
			
			CandidatePGIDetails bo= new CandidatePGIDetails();
			bo.setCandidateName(admForm.getApplicantName().toUpperCase());
			bo.setCandidateDob(CommonUtil.ConvertStringToSQLDate(admForm.getApplicantDob()));
			if(admForm.getCourseId()!=null && !admForm.getCourseId().isEmpty()){
			Course course=new Course();
			course.setId(Integer.parseInt(admForm.getCourseId()));
			bo.setCourse(course);
			}
			bo.setTxnStatus("Pending");
			if(admForm.getIndianCandidate()){
				if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
			}else{
				if(admForm.getEquivalentCalcApplnFeeINR()!=null && !admForm.getEquivalentCalcApplnFeeINR().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getEquivalentCalcApplnFeeINR()));
			}
			//below setting of txn amount has to be commented once the Production phase of PGI has started 
//			bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
			bo.setMobileNo1(admForm.getMobileNo1());
			bo.setMobileNo2(admForm.getMobileNo2());
			bo.setEmail(admForm.getEmail());
			ResidentCategory rc=new ResidentCategory();
			rc.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
			bo.setRefundGenerated(false);
			bo.setResidentCategory(rc);
			bo.setCreatedBy(admForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setModifiedBy(admForm.getUserId());
//			code added by sudhir
			if(admForm.getUniqueId()!=null && !admForm.getUniqueId().isEmpty()){
				StudentOnlineApplication uniqueIdBO = new StudentOnlineApplication();
				uniqueIdBO.setId(Integer.parseInt(admForm.getUniqueId()));
				bo.setUniqueId(uniqueIdBO);
			}
			IOnlineApplicationTxn transaction=OnlineApplicationImpl.getInstance();
			String candidateRefNo=transaction.generateCandidateRefNo(bo);
			StringBuilder temp=new StringBuilder();
			StringBuilder msg=new StringBuilder();
			//change the url of response in the msg below when it has to be released to the production. Please put the production IP
			if(candidateRefNo!=null && !candidateRefNo.isEmpty())
				 temp.append(KPPropertiesConfiguration.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(KPPropertiesConfiguration.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(KPPropertiesConfiguration.PGI_RESPONSE_LINK_NEW);
				String checkSum=null;
				if (admForm.getInstitute().equals("LV") || admForm.getInstitute().equals("GH")) {
					checkSum=HmacSHA256(temp.toString(),KPPropertiesConfiguration.PGI_CHECKSUM_KEY);
				} else {
					checkSum=PGIUtil.doDigest(temp.toString(),KPPropertiesConfiguration.PGI_CHECKSUM_KEY);
				}
			if(checkSum!=null && !checkSum.isEmpty())
			 msg.append(temp).append("|").append(checkSum);
			return msg.toString();
		*/
			
		

			
			CandidatePGIDetails bo= new CandidatePGIDetails();
			bo.setCandidateName(admForm.getApplicantName());
			bo.setCandidateDob(CommonUtil.ConvertStringToSQLDate(admForm.getApplicantDob()));
			if(admForm.getCourseId()!=null && !admForm.getCourseId().isEmpty()){
			Course course=new Course();
			course.setId(Integer.parseInt(admForm.getCourseId()));
			bo.setCourse(course);
			}
			bo.setTxnStatus("Pending");
			if(admForm.getIndianCandidate()){
				if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
			}else{
				//if(admForm.getEquivalentCalcApplnFeeINR()!=null && !admForm.getEquivalentCalcApplnFeeINR().isEmpty())
					//bo.setTxnAmount(new BigDecimal(admForm.getEquivalentCalcApplnFeeINR()));
				if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
			
			}
			//below setting of txn amount has to be commented once the Production phase of PGI has started 
//			bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
			bo.setMobileNo1(admForm.getMobileNo1());
			bo.setMobileNo2(admForm.getMobileNo2());
			bo.setEmail(admForm.getEmail());
			ResidentCategory rc=new ResidentCategory();
			rc.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
			bo.setRefundGenerated(false);
			bo.setResidentCategory(rc);
			bo.setCreatedBy(admForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setModifiedBy(admForm.getUserId());
			//raghu new
			if(admForm.getUniqueId()!=null && !admForm.getUniqueId().isEmpty()){
				StudentOnlineApplication uniqueIdBO = new StudentOnlineApplication();
				uniqueIdBO.setId(Integer.parseInt(admForm.getUniqueId()));
				bo.setUniqueId(uniqueIdBO);
			}
			IAdmissionFormTransaction transaction=AdmissionFormTransactionImpl.getInstance();
			String candidateRefNo=transaction.generateCandidateRefNo(bo);
			StringBuilder temp=new StringBuilder();
			//StringBuilder msg=new StringBuilder();
			
			
			/*JSONObject productinfo1 = new JSONObject();
			productinfo1.put("name","abc");
			productinfo1.put("description","abcd");
			productinfo1.put("value",bo.getTxnAmount());
			productinfo1.put("isRequired","true");
			productinfo1.put("settlementEvent",bo.getEmail());*/
			
			String productinfo="productinfo";
			admForm.setRefNo(candidateRefNo);
			//admForm.setProductinfo1(productinfo1);
			admForm.setProductinfo(productinfo);
			
			//change the url of response in the msg below when it has to be released to the production. Please put the production IP
			if(candidateRefNo!=null && !candidateRefNo.isEmpty())
			 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
				temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID);
			//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
			//raghu write for pay e
			String hash=hashCal("SHA-512",temp.toString());
			admForm.setTest(temp.toString());
			
			//if(checkSum!=null && !checkSum.isEmpty())
			// msg.append(temp).append("|").append(checkSum);
			return hash;
		
		
		
		}
		
		
		
		/**
		 * updating the response received from third party Payment Gateway
		 * @param admForm
		 * @return
		 * @throws Exception
		 */
		public boolean updateResponse(OnlineApplicationForm admForm) throws Exception{
			boolean isUpdated=false;
			CandidatePGIDetails bo=OnlineApplicationHelper.getInstance().convertToBo(admForm);
			IOnlineApplicationTxn transaction=OnlineApplicationImpl.getInstance();
			
			
			isUpdated=transaction.updateReceivedStatus(bo,admForm);
			AdmAppln appln = transaction.getAdmApplnDetails(admForm);
			admForm.setAdmApplnId(String.valueOf(appln.getId()));
			return isUpdated;
		}
		
		
		/**
		 * Checks if Application Fee is paid through inline and Not filled the complete appln
		 * @param admForm
		 * @return
		 * @throws Exception
		 */
		public boolean checkApplnFeePaidThroughOnline(OnlineApplicationForm admForm) throws Exception{
			boolean applnFeePaid=false;
			String appName=admForm.getApplicantName();
			if(admForm.getApplicantName().contains("'")){
				appName = admForm.getApplicantName().replaceAll("'", "''");
			}
			String query="from CandidatePGIDetails c where c.candidateName='" +appName+
					"' and c.candidateDob='" +CommonUtil.ConvertStringToSQLDate(admForm.getApplicantDob())+
					//"' and c.course.id="+admForm.getCourseId()+
					"' and c.uniqueId.id="+admForm.getUniqueId()+
					
					"  and c.mobileNo1='" +admForm.getMobileNo1()+
					"' and c.mobileNo2='" +admForm.getMobileNo2()+
					"' and c.email='" +admForm.getEmail()+
					"' and c.residentCategory.id=" +admForm.getResidentCategoryForOnlineAppln()+ 
					" and c.admAppln.id is null and c.txnStatus='Success' and c.refundGenerated=0 " +
					" order by c.id desc";
			IOnlineApplicationTxn txn=new OnlineApplicationImpl();
			/* code modified by sudhir 
			 * getting the list of bos and
			 * taking the latest record from the list
			 * */
			List<CandidatePGIDetails> candidatePGIDetailsList=txn.getPaidCandidateDetails(query);
			if(candidatePGIDetailsList!=null && !candidatePGIDetailsList.isEmpty()){
				CandidatePGIDetails candidatePGIDetails = candidatePGIDetailsList.get(0);
			/* code modified by sudhir ended here	*/
				applnFeePaid=true;
				admForm.setTxnAmt(candidatePGIDetails.getTxnAmount().toPlainString());
				admForm.setTxnRefNo(candidatePGIDetails.getTxnRefNo());
				admForm.setTxnDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(candidatePGIDetails.getTxnDate()), "dd-MMM-yyyy", "dd/MM/yyyy"));
				admForm.setCandidateRefNo(candidatePGIDetails.getCandidateRefNo());
				admForm.setPaymentSuccess(true);
			}
			return applnFeePaid;
		}
		
	
		/**
		 * @param stForm
		 * @throws Exception 
		 */
	/*	public void checkWorkExperianceMandatory(OnlineApplicationForm stForm) throws Exception {
			IOnlineApplicationTxn txn=new OnlineApplicationImpl();
			boolean mandatory = txn.checkWorkExperianceMandatory(stForm.getCourseId());
			stForm.setShowWorkExp(mandatory);
		}*/
		
		
		/**
		 * @param admApplnId
		 * @return
		 * @throws Exception
		 */
		public String getCandidatePGIDetails(int admApplnId ) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();

			String txnRefNo = txn.getCandidatePGIDetails(admApplnId);

			return txnRefNo;
		}
		
		
		
		
		
		
		
		/**
		 * to get checkSum
		 * @param message
		 * @param secret
		 * @return
		 */
		public static String HmacSHA256(String message,String secret)  {
			//MessageDigest md = null;
				try {
					Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
					 SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
					 sha256_HMAC.init(secret_key);
					byte raw[] = sha256_HMAC.doFinal(message.getBytes());
					StringBuffer ls_sb=new StringBuffer();
					for(int i=0;i<raw.length;i++)
						ls_sb.append(char2hex(raw[i]));
						return ls_sb.toString(); //step 6
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}

		public static String char2hex(byte x){
			    char arr[]={
			                  '0','1','2','3',
			                  '4','5','6','7',
			                  '8','9','A','B',
			                  'C','D','E','F'
			                };

			    char c[] = {arr[(x & 0xF0)>>4],arr[x & 0x0F]};
			    return (new String(c));
			  }
		
		
		
		
		/**
		 * @param admForm
		 * @param candidatePGIId
		 */
		public void setCandidatePGIDetailsToForm(OnlineApplicationForm admForm, int candidatePGIId) throws Exception{
			IOnlineApplicationTxn transaction = OnlineApplicationImpl.getInstance();
			CandidatePGIDetails candidatePGIDetails = transaction.getCandiadatePGIDetailsById(candidatePGIId);
			if(candidatePGIDetails!=null){
				if(candidatePGIDetails.getCandidateName()!=null && !candidatePGIDetails.getCandidateName().isEmpty()){
					admForm.setApplicantName(candidatePGIDetails.getCandidateName());
				}
				if(candidatePGIDetails.getCandidateDob()!=null){
					admForm.setApplicantDob(CommonUtil.formatDates(candidatePGIDetails.getCandidateDob()));
				}
				if(candidatePGIDetails.getCourse()!=null){
						admForm.setCourseId(String.valueOf(candidatePGIDetails.getCourse().getId()));
					if(candidatePGIDetails.getCourse().getProgram()!=null){
						admForm.setProgramId(String.valueOf(candidatePGIDetails.getCourse().getProgram().getId()));
						if(candidatePGIDetails.getCourse().getProgram().getProgramType()!=null){
							admForm.setProgramTypeId(String.valueOf(candidatePGIDetails.getCourse().getProgram().getProgramType().getId()));
						}
					}
				}
				if(candidatePGIDetails.getEmail()!=null && !candidatePGIDetails.getEmail().isEmpty()){
					admForm.setEmail(candidatePGIDetails.getEmail());
					admForm.setConfirmEmail(candidatePGIDetails.getEmail());
				}
				if(candidatePGIDetails.getMobileNo1()!=null && !candidatePGIDetails.getMobileNo1().isEmpty()){
					admForm.setMobileNo1(candidatePGIDetails.getMobileNo1());
				}
				if(candidatePGIDetails.getMobileNo2()!=null && !candidatePGIDetails.getMobileNo2().isEmpty()){
					admForm.setMobileNo2(candidatePGIDetails.getMobileNo2());
				}
				if(candidatePGIDetails.getResidentCategory()!=null){
					admForm.setResidentCategoryForOnlineAppln(String.valueOf(candidatePGIDetails.getResidentCategory().getId()));
				}
				// get Program Map details.
				Map<Integer,String> programMap = CommonAjaxHandler.getInstance() .getApplnProgramsByProgramType(Integer.parseInt(admForm.getProgramTypeId()));
			 if(programMap!=null && !programMap.isEmpty()){
				 admForm.setProgramMap(programMap);
			 }else{
				 admForm.setProgramMap(null);
			 }
			 // get Course Map based on programId.
			 Map<Integer,String>  courseMap = CommonAjaxHandler.getInstance().getCourseByProgramForOnline( Integer.parseInt(admForm.getProgramId()));
			 if(courseMap!=null && !courseMap.isEmpty()){
				 admForm.setCourseMap(courseMap);
			 }else{
				 admForm.setCourseMap(null);
				 
			 }
			}
			
		}
		
		public void getUniqueId(OnlineApplicationForm admForm) throws Exception {
			log.info("Enter getEdnQualifications ...");
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			/*OnlineApplicationForm adm= */txn.getUniqueId(admForm);
		}
		
		
		
		public boolean checkCourseInDraftMode(String uniqueId, int courseId) throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			return txn.checkCourseInDraftMode(uniqueId,courseId);
		}
		
		
		
		
		/**
		 * @return
		 * @throws Exception
		 */
		public String getParameterForPGI(AdmissionFormForm admForm) throws Exception {
			
			CandidatePGIDetails bo= new CandidatePGIDetails();
			bo.setCandidateName(admForm.getApplicantName());
			bo.setCandidateDob(CommonUtil.ConvertStringToSQLDate(admForm.getApplicantDob()));
			if(admForm.getCourseId()!=null && !admForm.getCourseId().isEmpty()){
			Course course=new Course();
			course.setId(Integer.parseInt(admForm.getCourseId()));
			bo.setCourse(course);
			}
			bo.setTxnStatus("Pending");
			if(admForm.getIndianCandidate()){
				if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
			}else{
				//if(admForm.getEquivalentCalcApplnFeeINR()!=null && !admForm.getEquivalentCalcApplnFeeINR().isEmpty())
					//bo.setTxnAmount(new BigDecimal(admForm.getEquivalentCalcApplnFeeINR()));
				if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
					bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
			
			}
			//below setting of txn amount has to be commented once the Production phase of PGI has started 
//			bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
			bo.setMobileNo1(admForm.getMobileNo1());
			bo.setMobileNo2(admForm.getMobileNo2());
			bo.setEmail(admForm.getEmail());
			ResidentCategory rc=new ResidentCategory();
			rc.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
			bo.setRefundGenerated(false);
			bo.setResidentCategory(rc);
			bo.setCreatedBy(admForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setModifiedBy(admForm.getUserId());
			
			IAdmissionFormTransaction transaction=AdmissionFormTransactionImpl.getInstance();
			String candidateRefNo=transaction.generateCandidateRefNo(bo);
			StringBuilder temp=new StringBuilder();
			//StringBuilder msg=new StringBuilder();
			
			
			/*JSONObject productinfo1 = new JSONObject();
			productinfo1.put("name","abc");
			productinfo1.put("description","abcd");
			productinfo1.put("value",bo.getTxnAmount());
			productinfo1.put("isRequired","true");
			productinfo1.put("settlementEvent",bo.getEmail());*/
			
			String productinfo="productinfo";
			admForm.setRefNo(candidateRefNo);
			//admForm.setProductinfo1(productinfo1);
			admForm.setProductinfo(productinfo);
			
			//change the url of response in the msg below when it has to be released to the production. Please put the production IP
			if(candidateRefNo!=null && !candidateRefNo.isEmpty())
			 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
				temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID);
			//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
			//raghu write for pay e
			String hash=hashCal("SHA-512",temp.toString());
			admForm.setTest(temp.toString());
			
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
		public List<SportsTO> getSportsList() throws Exception {
			IOnlineApplicationTxn txn= new OnlineApplicationImpl();
			return txn.getSportsList();
		}
		
		private void send_email_new(OnlineApplicationForm admForm) throws Exception{
		 	
		 	boolean sent=false;
			Properties prop = new Properties();
			String toAddress="";
			if(admForm.getApplicantDetails().getPersonalData().getEmail()!=null && !admForm.getApplicantDetails().getPersonalData().getEmail().isEmpty()){
				toAddress = toAddress + admForm.getApplicantDetails().getPersonalData().getEmail();
	 		}
			String collegeName = CMSConstants.COLLEGE_NAME;
			String subject="Application submitted successfully "+collegeName;
			String msg= "Your application " + admForm.getApplicantDetails().getApplnNo() + " has been submitted successfully. Please save this message for all future reference.";
			
		 	
			final String adminmail=CMSConstants.MAIL_USERID;
			final String password = CMSConstants.MAIL_PASSWORD;
			final String port = CMSConstants.MAIL_PORT;
			final String host = CMSConstants.MAIL_HOST;
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", port);
	 
			Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(adminmail,password);
					}
				});
	 
			MailTO mailto=new MailTO();
			mailto.setFromName(CMSConstants.COLLEGE_NAME);
			mailto.setFromAddress(adminmail);
			mailto.setToAddress(toAddress);
			mailto.setSubject(subject);
			mailto.setMessage(msg);
			mailto.setFromName(prop.getProperty("knowledgepro.admission.studentmail.fromName"));
				
			try
			{
				 MimeMessage message1 = new MimeMessage(session);
					
					// Set from & to addresses
					InternetAddress from = new InternetAddress(adminmail, collegeName + " Online Admission");
					
					InternetAddress toAssociate = new InternetAddress(toAddress);
					message1.setSubject(subject);
					message1.setFrom(from);
					message1.addRecipient(Message.RecipientType.TO, toAssociate);
				    MimeBodyPart mailBody = new MimeBodyPart();
				    mailBody.setText(msg, "US-ASCII", "html");
				    MimeMultipart mimeMultipart = new MimeMultipart();
				    
				    mimeMultipart.addBodyPart(mailBody);
				    message1.setContent(mimeMultipart);
				
				    Properties config = new Properties() {
						{
							put("mail.smtps.auth", "true");
							put("mail.smtp.host", host);
							put("mail.smtp.port", port);
							put("mail.smtp.starttls.enable", "true");
							put("mail.transport.protocol", "smtps");
						}
					}; 
					
					
					
					Session carrierSession = Session.getInstance(config, new Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(adminmail,password);
						}
					});
				
				
				
				Transport transport = carrierSession.getTransport("smtps");
				transport.connect(host,adminmail,password);
				transport.sendMessage(message1,message1.getRecipients(Message.RecipientType.TO));  //set
		        transport.close();
	 
				System.out.println("==========Done========");
			}
			
			catch (Exception e) {
				System.out.println(e.getMessage());
				
			}
			//uses JMS 
			sent=CommonUtil.sendMail(mailto);
		}
		
		private void send_sms_new(OnlineApplicationForm objForm) throws Exception{

			Properties prop = new Properties();
			try {
				InputStream in = UniqueIdRegistrationHandler.class.getClassLoader()
				.getResourceAsStream(CMSConstants.SMS_FILE_CFG);
				prop.load(in);
			} catch (FileNotFoundException e) {	
			log.error("Unable to read properties file...", e);
				
			} catch (IOException e) {
				log.error("Unable to read properties file...", e);
				
			}
			
			String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
			String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
			
			String temp = "";
			temp=temp+URLEncoder.encode("Your application " + objForm.getApplicantDetails().getApplnNo() + " has been submitted successfully. Please save this message for all future reference."
										,"UTF-8");		
			
			String candidateMobileNumber = objForm.getApplicantDetails().getPersonalData().getMobileNo();
			String[] array = candidateMobileNumber.split(" ");
			candidateMobileNumber = array[0] + array[1];
			
			PasswordMobileMessaging mob=new PasswordMobileMessaging();						
			mob.setDestinationNumber(candidateMobileNumber);
			mob.setMessagePriority(3);
			mob.setSenderName(senderName);
			mob.setSenderNumber(senderNumber);
			mob.setMessageEnqueueDate(new Date());
			mob.setIsMessageSent(false);									
			mob.setMessageBody(temp);
			
			PropertyUtil.getInstance().save(mob);
			SMSUtil s=new SMSUtil();
			//SMSUtils smsUtils=new SMSUtils();

			ConverationUtil converationUtil=new ConverationUtil();
			List<SMS_Message> listSms=converationUtil.convertBotoTOPassword(s.getListOfSMSPassword());
			List<SMS_Message> mobList=SMSUtils.sendSMS(listSms);
			s.updateSMSPassword(converationUtil.convertTotoBOPassword(mobList));
		}
		public StudentOnlineApplication getUniqueObj(OnlineApplicationForm admForm)  throws Exception{
			IOnlineApplicationTxn txn = OnlineApplicationImpl.getInstance();
			StudentOnlineApplication onlineApplication = txn.getObj(admForm);
			return onlineApplication;
		}
		public AdmAppln getAdmApplnObject(OnlineApplicationForm admForm) throws Exception {
			IOnlineApplicationTxn txn = OnlineApplicationImpl.getInstance();
			AdmAppln admAppln = txn.getAdmApplnDetails(admForm);
			return admAppln;
		}
		
	}
