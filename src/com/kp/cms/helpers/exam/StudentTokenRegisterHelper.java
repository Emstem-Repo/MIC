package com.kp.cms.helpers.exam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StudentTokenRegisterHelper {
	/**
	 * Singleton object of NewSupplementaryImpApplicationHelper
	 */
	private static volatile StudentTokenRegisterHelper studentTokenRegisterHelper = null;
	private static final Log log = LogFactory.getLog(StudentTokenRegisterHelper.class);
	private StudentTokenRegisterHelper() {
		
	}
	/**
	 * return singleton object of NewSupplementaryImpApplicationHelper.
	 * @return
	 */
	public static StudentTokenRegisterHelper getInstance() {
		if (studentTokenRegisterHelper == null) {
			studentTokenRegisterHelper = new StudentTokenRegisterHelper();
		}
		return studentTokenRegisterHelper;
	}
	/*public CandidatePGIDetailsExamRegular convertToBoReg(ExamStudentTokenRegisterdForm admForm) throws Exception {
		
		CandidatePGIDetailsExamRegular bo=new CandidatePGIDetailsExamRegular();
		StringBuilder temp=new StringBuilder();
		//raghu
		
		log.error(admForm.getHash());
			if(admForm.getPaymentMail()!=null && !admForm.getPaymentMail().equalsIgnoreCase("")  && admForm.getPaymentMail().equalsIgnoreCase(admForm.getStudentObj().getAdmAppln().getPersonalData().getEmail())){
			    temp.append(CMSConstants.PGI_SECURITY_ID_EXAM).append("|").append(admForm.getStatus()).append("|||||||||||").append(admForm.getStudentObj().getAdmAppln().getPersonalData().getEmail()).append("|").append(admForm.getStudentObj().getAdmAppln().getPersonalData().getFirstName()).append("|").append(admForm.getProductinfo()).append("|").append(admForm.getTotalFees()).append("|").append(admForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_EXAM);
			
			}else if(admForm.getPaymentMail()!=null && !admForm.getPaymentMail().equalsIgnoreCase("") && !admForm.getPaymentMail().equalsIgnoreCase(admForm.getStudentObj().getAdmAppln().getPersonalData().getEmail())){
				
				temp.append(CMSConstants.PGI_SECURITY_ID_EXAM).append("|").append(admForm.getStatus()).append("|||||||||||").append(admForm.getPaymentMail()).append("|").append(admForm.getStudentObj().getAdmAppln().getPersonalData().getFirstName()).append("|").append(admForm.getProductinfo()).append("|").append(admForm.getAmount()).append("|").append(admForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_EXAM);
				
			}else {
				
				temp.append(CMSConstants.PGI_SECURITY_ID_EXAM).append("|").append(admForm.getStatus()).append("|||||||||||").append(admForm.getStudentObj().getAdmAppln().getPersonalData().getEmail()).append("|").append(admForm.getStudentObj().getAdmAppln().getPersonalData().getFirstName()).append("|").append(admForm.getProductinfo()).append("|").append(admForm.getAmount()).append("|").append(admForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_EXAM);
				
			}
		
		//raghu write for pay e
		String hash=AdmissionFormHandler.getInstance().hashCal("SHA-512",temp.toString());
		
		if(admForm.getHash()!=null && hash!=null && !admForm.getHash().equals(hash)){
			throw  new BusinessException();
		}else{
			bo.setCandidateRefNo(admForm.getTxnid());
			bo.setTxnRefNo(admForm.getPayuMoneyId());
			bo.setTxnAmount(new BigDecimal(admForm.getAmount()));
			bo.setBankRefNo(admForm.getBank_ref_num());
			bo.setTxnStatus(admForm.getStatus());
			bo.setErrorStatus(admForm.getError1());
			//raghu setting current date
			bo.setTxnDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy")));
			bo.setMode(admForm.getMode1());
			bo.setUnmappedStatus(admForm.getUnmappedstatus());
			bo.setMihpayId(admForm.getMihpayid());
			bo.setPgType(admForm.getPG_TYPE());
			//raghu new
			bo.setPaymentEmail(admForm.getPaymentMail());
			
			
			admForm.setPgiStatus("Payment Successful");
			admForm.setTxnAmt(admForm.getAmount());
			admForm.setTxnRefNo(admForm.getPayuMoneyId());
			admForm.setTxnDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy"));
			
		}
		
		return bo;
	
	}*/
	
}
