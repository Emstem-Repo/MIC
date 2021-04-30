package com.kp.cms.handlers.admission;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.bo.admin.InterviewCardHistory;
import com.kp.cms.bo.admin.ResidentCategory;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admin.StudentOnlineApplication;
import com.kp.cms.bo.admission.StudentAllotmentPGIDetails;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.helpers.admission.AdmissionStatusHelper;
import com.kp.cms.helpers.admission.OnlineApplicationHelper;
import com.kp.cms.to.admission.AdmissionStatusTO;
import com.kp.cms.to.admission.InterviewCardTO;
import com.kp.cms.transactions.admission.IAdmissionFormTransaction;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;
import com.kp.cms.transactions.admission.IOnlineApplicationTxn;
import com.kp.cms.transactionsimpl.admission.AdmissionFormTransactionImpl;
import com.kp.cms.transactionsimpl.admission.AdmissionStatusTransactionImpl;
import com.kp.cms.transactionsimpl.admission.OnlineApplicationImpl;
import com.kp.cms.utilities.CommonUtil;


public class AdmissionStatusHandler {

	private static final Log log = LogFactory.getLog(AdmissionStatusHandler.class);
	public static volatile AdmissionStatusHandler admissionStatusHandler = null;

	private AdmissionStatusHandler(){
	
	}
	
	/**
	 * 
	 * @returns a single instance (Singleton)every time. 
	 */
	public static AdmissionStatusHandler getInstance() {
		if (admissionStatusHandler == null) {
			admissionStatusHandler = new AdmissionStatusHandler();
		}
		return admissionStatusHandler;
	}
	
	/**
	 * 
	 * @param Passing application no. and gets the exactly matching records from AdmAppln table 
	 * @return
	 */
	public AdmissionStatusTO getInterviewResult(String applicationNo, int appliedYear) throws Exception
	{
		log.info("Entering into getInterviewResult of AdmissionStatusHandler");
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
//		Calendar cal= Calendar.getInstance();
		AdmAppln admAppln=admissionStatusTransaction.getInterviewResult(applicationNo,appliedYear/*cal.get(Calendar.YEAR)*/);
		
		AdmissionStatusTO admissionStatusTO = AdmissionStatusHelper.getInstance().getInterviewStatus(admAppln);
		log.info("Leaving from getInterviewResult of AdmissionStatusHandler");
		return admissionStatusTO;
	}


	public List<InterviewCardTO> getStudentsList(String applicationNo ) throws Exception{
		log.info("Entering into getStudentsList of AdmissionStatusHandler");
		
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		List<InterviewCard> iCard = admissionStatusTransaction.getStudentsList(Integer.parseInt(applicationNo));
		log.info("Leaving into getStudentsList of AdmissionStatusHandler");
		
		return AdmissionStatusHelper.getInstance().getInterviewCardTO(iCard);
	}

	public List<InterviewCard> getStudentAdmitCardDetails(String applicationNo, String interviewTypeId) throws Exception{
		log.info("Entering into getStudentsList of AdmissionStatusHandler");
		
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		log.info("Leaving into getStudentsList of AdmissionStatusHandler");
		
		return admissionStatusTransaction.getStudentAdmitCardDetails(Integer.parseInt(applicationNo), Integer.parseInt(interviewTypeId));
	}

	public List<AdmissionStatusTO> getDetailsAdmAppln(String applicationNo, AdmissionStatusForm admissionStatusForm)throws Exception
	{
		log.info("Entering into getDetailsAdmAppln of AdmissionStatusHandler");
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		List<AdmAppln> newList=admissionStatusTransaction.getDetailsAdmAppln(applicationNo,admissionStatusForm.getYear());
		log.info("Leaving into getDetailsAdmAppln of AdmissionStatusHandler");
		return AdmissionStatusHelper.getInstance().populateAdmApplnBOtoTO(newList,admissionStatusForm);
	}
	
	public AdmAppln getApplicantDetails(String applicationNo)throws Exception
	{
		log.info("Entering into getApplicantDetails of AdmissionStatusHandler");
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		List<AdmAppln> newList=admissionStatusTransaction.getDetailsAdmAppln(applicationNo);
		log.info("Leaving into getApplicantDetails of AdmissionStatusHandler");
		if(newList!= null && newList.size() > 0){
			return newList.get(0); 
		}
		else{
			return new AdmAppln();
		}
	}

	/**
	 * Checks if appln n dob exists in Appln Acknowledgement table
	 * @param applicationNo
	 * @param dateOfBirth
	 * @return
	 * @throws Exception
	 */
	public boolean checkApplnAvailableInAck(String applicationNo, String dateOfBirth) throws Exception {
		IAdmissionStatusTransaction txn=new AdmissionStatusTransactionImpl();
		boolean exists=false;
		exists=txn.getApplnAcknowledgement(applicationNo,dateOfBirth);
		return exists;
	}

	/**
	 * @param applicationNo
	 * @return
	 * @throws Exception
	 */
	public List<InterviewCardHistory> getStudentAdmitCardHistoryDetails( String applicationNo) throws Exception{
		log.info("Entering into getStudentAdmitCardHistoryDetails of AdmissionStatusHandler");
		
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		log.info("Leaving into getStudentAdmitCardHistoryDetails of AdmissionStatusHandler");
		
		return admissionStatusTransaction.getStudentAdmitCardHistoryDetails(Integer.parseInt(applicationNo));
	}

	public List<AdmissionStatusTO> getToListForStatus(String applicationNo,AdmissionStatusForm admissionStatusForm)throws Exception {
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		List<StudentCourseChanceMemo> allotments = admissionStatusTransaction.getBolist(applicationNo,admissionStatusForm.getDateOfBirth());
		List<AdmissionStatusTO> statusTOs = AdmissionStatusHelper.getInstance().setToList(allotments,admissionStatusForm);
		return statusTOs;
	}

	public boolean updateCourseAllotment(String applicationNo, AdmissionStatusForm admissionStatusForm)throws Exception {
		IAdmissionStatusTransaction tx=new AdmissionStatusTransactionImpl();
		List<StudentCourseChanceMemo> allotments = tx.getBolist(applicationNo,admissionStatusForm.getDateOfBirth());
		allotments=AdmissionStatusHelper.getInstance().getUpdatedBo(allotments,admissionStatusForm);
		boolean isUpdated=tx.isUpdated(allotments);
		return isUpdated;
	}

	public boolean uploadDetail(String applicationNo, AdmissionStatusForm admissionStatusForm)throws Exception {
		IAdmissionStatusTransaction tx=new AdmissionStatusTransactionImpl();
		StudentCourseChanceMemo allotment =tx.getBoClass(admissionStatusForm.getAdmApplnId());
		if(allotment!=null){
			allotment.setIsUploaded(true);
			allotment.setLastModifiedDate(new Date());
			allotment.setModifiedBy(admissionStatusForm.getUserId());
		}
		return tx.isUpdateUpload(allotment);
	}

	public boolean updateResponse(AdmissionStatusForm admForm) throws Exception{
		boolean isUpdated=false;
		StudentAllotmentPGIDetails bo=AdmissionStatusHelper.getInstance().convertToBo(admForm);
		IAdmissionStatusTransaction tx=new AdmissionStatusTransactionImpl();
		isUpdated=tx.updateReceivedStatus(bo,admForm);
		return isUpdated;
	}

	public String getParameterForPGI(AdmissionStatusForm admForm) throws Exception{		
		StudentAllotmentPGIDetails bo= new StudentAllotmentPGIDetails();
		bo.setCandidateName(admForm.getApplicantName());
		bo.setCandidateDob(CommonUtil.ConvertStringToSQLDate(admForm.getDateOfBirth()));
		if(admForm.getCourseId()!=null && !admForm.getCourseId().isEmpty()){
		Course course=new Course();
		course.setId(Integer.parseInt(admForm.getCourseId()));
		bo.setCourse(course);
		}
		bo.setTxnStatus("Pending");
		if(admForm.getApplicationAmount()!=null && !admForm.getApplicationAmount().isEmpty())
				bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
		bo.setMobileNo1(admForm.getMobile1());
		bo.setMobileNo2(admForm.getMobile2());
		bo.setEmail(admForm.getEmail());
		ResidentCategory rc=new ResidentCategory();
		rc.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
		bo.setRefundGenerated(false);
		bo.setResidentCategory(rc);
		bo.setCreatedBy(admForm.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(admForm.getUserId());
		if(admForm.getUniqueId()!=null && !admForm.getUniqueId().isEmpty()){
			StudentOnlineApplication uniqueIdBO = new StudentOnlineApplication();
			uniqueIdBO.setId(Integer.parseInt(admForm.getUniqueId()));
			bo.setUniqueId(uniqueIdBO);
		}
		IAdmissionFormTransaction transaction=AdmissionFormTransactionImpl.getInstance();
		String candidateRefNo=transaction.generateCandidateRefNo(bo);
		StringBuilder temp=new StringBuilder();
		
		
		
		String productinfo="productinfo";
		admForm.setRefNo(candidateRefNo);
		admForm.setProductinfo(productinfo);
		
		if(candidateRefNo!=null && !candidateRefNo.isEmpty())
			temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
		String hash=hashCal("SHA-512",temp.toString());
		admForm.setTest(temp.toString());
		
		return hash;
	
	
	
	}

	private String hashCal(String type, String str) {
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
}