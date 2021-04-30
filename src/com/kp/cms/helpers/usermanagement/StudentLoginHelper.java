package com.kp.cms.helpers.usermanagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePGIDetailsForStuSemesterFees;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.FeePaymentApplicantDetails;
import com.kp.cms.bo.admin.FeePaymentDetail;
import com.kp.cms.bo.admin.FeePaymentDetailFeegroup;
import com.kp.cms.bo.admin.FeePaymentOptionalFeeGroup;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.CandidatePGIForSpecialFees;
import com.kp.cms.bo.exam.OnlinePaymentReciepts;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionActivityDetails;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.handlers.admission.AdmissionFormHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.handlers.usermanagement.StudentLoginHandler;
import com.kp.cms.helpers.exam.NewSupplementaryImpApplicationHelper;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admission.OnlinePaymentRecieptsTo;
import com.kp.cms.to.fee.FeePaymentDetailFeeGroupTO;
import com.kp.cms.to.fee.FeePaymentTO;
import com.kp.cms.to.fee.PrintChalanTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class StudentLoginHelper {
	private static volatile StudentLoginHelper studentLoginHelper = null;
	public static StudentLoginHelper getInstance(){
		if(studentLoginHelper == null){
			studentLoginHelper = new StudentLoginHelper();
			return studentLoginHelper;
		}
		return studentLoginHelper;
	}
	/**
	 * @param mobileNo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PersonalData copyFormToBO(String mobileNo, String userId,PersonalData data) throws Exception{
		
		if(mobileNo!=null && !mobileNo.isEmpty()){
			data.setMobileNo2(mobileNo);
		}
			data.setLastModifiedDate(new Date());
			data.setModifiedBy(userId);
			
		return data;
	}
	/**
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public LoginForm copyBoToForm(PersonalData personalData)throws Exception {
		LoginForm loginForm =new LoginForm();
		if(personalData!=null){
			if(personalData.getMobileNo2()!=null && !personalData.getMobileNo2().isEmpty()){
				loginForm.setMobileNo(personalData.getMobileNo2());
			}
		}
		
		return loginForm;
	}
	/**
	 * converting FeePayment BO to TO to set the list of challan's printed in the jsp
	 * @param feeList
	 * @return
	 * @throws Exception
	 */
	public List<FeePaymentTO> convertFeePaymentBOtoTO(List<FeePayment> feeList) throws Exception {
		List<FeePaymentTO> feeToList= null;
		if(feeList!=null && !feeList.isEmpty()){
			feeToList=new ArrayList<FeePaymentTO>();
		Iterator<FeePayment> itr=feeList.iterator();
		while (itr.hasNext()) {
			FeePayment feePayment = (FeePayment) itr.next();
			FeePaymentTO to=new FeePaymentTO();
			to.setChallenPrintedDate(feePayment.getChallenPrintedDate()!=null?
					CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(feePayment.getChallenPrintedDate()), "dd-MMM-yyyy", "dd/MM/yyyy"):"");
			to.setBillNo(feePayment.getBillNo());
			Set<FeePaymentDetail> feeDetail=feePayment.getFeePaymentDetails();
			Iterator<FeePaymentDetail> itrDetail=feeDetail.iterator();
			while (itrDetail.hasNext()) {
				FeePaymentDetail feePaymentDetail = (FeePaymentDetail) itrDetail.next();
				if(feePaymentDetail.getFeeFinancialYear()!=null)
				{
					//to.setFinancialYear(feePaymentDetail.getFeeFinancialYear().getYear()!=null?String.valueOf(feePaymentDetail.getFeeFinancialYear().getId()):"");
				}
			}
			feeToList.add(to);
		}
		}
		return feeToList;
	}
	/**
	 * converting FeePayment Bo to TO to print the challan
	 * @param feePayment
	 * @param loginForm
	 * @param request
	 * @throws Exception
	 */
	public void copyFeeChallanPrintData(FeePayment feePayment,LoginForm loginForm, HttpServletRequest request) throws Exception{
		if(feePayment!=null){
			
		if(feePayment.getStudent().getAdmAppln() != null){
			loginForm.setApplnNo(String.valueOf(feePayment.getStudent().getAdmAppln().getApplnNo()));
		}
		if(feePayment.getStudent().getRegisterNo()!=null && !feePayment.getStudent().getRegisterNo().isEmpty()){
			loginForm.setApplnNo(String.valueOf(feePayment.getStudent().getRegisterNo()));
		}
		loginForm.setBillNo(feePayment.getBillNo());
		loginForm.setChallanPrintedDate(CommonUtil.getStringDate(feePayment.getChallenPrintedDate()));
		//setting the verified by to form
		//if(feePayment.getCreatedBy()!=null)
		//loginForm.setVerifiedBy(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(feePayment.getCreatedBy()),"Users",true,"userName"));
	
	//	feePaymentForm.setStudentName(feePayment.getStudentName());
		boolean isFirstYear=false;
		if(feePayment.getStudent()!=null){
			Student student = feePayment.getStudent();
			StringBuffer studentName = new StringBuffer(); 
			if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getFirstName() != null) {
				studentName.append(student.getAdmAppln().getPersonalData().getFirstName());
				studentName.append(" ");
			} 
			if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getMiddleName() != null) {
				studentName.append(student.getAdmAppln().getPersonalData().getMiddleName());
				studentName.append(" ");
			}
			if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getLastName() != null) {
				studentName.append(student.getAdmAppln().getPersonalData().getLastName());
				studentName.append(" ");
			}
			loginForm.setStudentName(studentName.toString());
			Set<FeePaymentApplicantDetails>details= feePayment.getFeePaymentApplicantDetailses();
				Iterator<FeePaymentApplicantDetails>iterator=details.iterator();
				while(iterator.hasNext())
				{
					FeePaymentApplicantDetails applicantDetails=iterator.next();
					if(applicantDetails.getSemesterNo()==1)
					{
						isFirstYear=true;
						break;
					}	
				}
		}
		//loginForm.setInstallmentReferenceNo(feePayment.getInstallmentVoucherNo());
		loginForm.setConcessionReferenceNo(feePayment.getConcessionVoucherNo());
		//loginForm.setScholarshipReferenceNo(feePayment.getSchlorshipVoucherNo());
		if(feePayment.getCurrency()!= null && feePayment.getCurrency().getCurrencyCode()!= null){
			loginForm.setCurrencyCode(feePayment.getCurrency().getCurrencyCode());
		}
		
		if(feePayment.getStudent()!= null && feePayment.getStudent().getAdmAppln()!= null && feePayment.getStudent().getAdmAppln().getCourseBySelectedCourseId()!= null &&
				feePayment.getStudent().getAdmAppln().getCourseBySelectedCourseId().getCode()!= null){
			String className="";
			if(isFirstYear){
				className="I ";
			}
			className=className+feePayment.getStudent().getAdmAppln().getCourseBySelectedCourseId().getCode();
			loginForm.setClassName(className);
		}
		if(feePayment.getStudent().getClassSchemewise()!=null && feePayment.getStudent().getClassSchemewise().getClasses()!=null)
		{
			loginForm.setClassName(feePayment.getStudent().getClassSchemewise().getClasses().getName());
			/*if(feePaymentForm.getPucFeePayment())
			{
				String className=getClassName(feePayment);
				feePaymentForm.setClassName(className);
			}*/
		}
		
		if(feePayment.getChallanCreatedTime()!= null){
			loginForm.setChalanCreatedTime(feePayment.getChallanCreatedTime().toString());
		}
		FeePaymentDetailFeeGroupTO feeDetailFeeGroupTO;
		FeePaymentDetailFeegroup feePaymentDetailFeegroup;
		
		HttpSession session = request.getSession(false);
		if(feePayment.getFeePaymentMode()!= null && feePayment.getFeePaymentMode().getName()!= null){
			loginForm.setPaymentMode(feePayment.getFeePaymentMode().getName());
		}
		Set<Integer> sems = new HashSet<Integer>();
		String schemeNo = "";
		StringBuffer schemeBuf = new StringBuffer();
		if(feePayment.getFeePaymentApplicantDetailses().size() != 0) {
			Iterator<FeePaymentApplicantDetails> itr2 = feePayment.getFeePaymentApplicantDetailses().iterator();
			FeePaymentApplicantDetails applicantDetails;
			while(itr2.hasNext()) {
				 applicantDetails = itr2.next();
				 if(!sems.contains(applicantDetails.getSemesterNo())){
					 sems.add(applicantDetails.getSemesterNo());
					 schemeBuf.append(applicantDetails.getSemesterNo()).append('/');
				 }
			}
		}
		/*else if (feePayment.getFeePaymentOptionalFeeGroups().size() != 0){
			Iterator<FeePaymentOptionalFeeGroup> itr2 = feePayment.getFeePaymentOptionalFeeGroups().iterator();
			FeePaymentOptionalFeeGroup applicantDetails;
			while(itr2.hasNext()) {
				 applicantDetails = itr2.next();
				 if(!sems.contains(applicantDetails.getSemesterNo())){
					 sems.add(applicantDetails.getSemesterNo());
					 schemeBuf.append(applicantDetails.getSemesterNo()).append('/');
				 }
			}
		}*/
		
		int length = schemeBuf.length();
		schemeNo = schemeBuf.substring(0, length-1);
		loginForm.setSemister(schemeNo);
		
		Set<FeePaymentDetail> feePaymentList = feePayment.getFeePaymentDetails();
		Iterator<FeePaymentDetail> itr = feePaymentList.iterator();
		Map<Integer,Double> accountWiseNonOptionalAmount = new HashMap<Integer,Double>();
		Map<Integer,Double> accountWiseOptionalAmount = new HashMap<Integer,Double>();
		Map<Integer,Double> accountWiseTotalAmount = new HashMap<Integer,Double>();
		Map<Integer,Double> accountWiseConcessionAmount = new HashMap<Integer,Double>();
		Map<Integer,String> allFeeAccountMap = new HashMap<Integer,String>();
		List<PrintChalanTO> printChalanList = new ArrayList<PrintChalanTO>();
		PrintChalanTO printChalanTO;
		List<String> descList;
		List<String> bankInfoList;
		int totalAmt = 0;
		FeePaymentDetail feePaymentDetail;
		int count = 1;
		StringBuffer accwiseTotalString = new StringBuffer();
		Double grandTotal = 0.00;
		List<Integer> currList = new ArrayList<Integer>();
		while(itr.hasNext()) {
			totalAmt = 0;
			feePaymentDetail = itr.next();
			//if(feePaymentDetail.getIsInstallmentPayment()!= null && feePaymentDetail.getIsInstallmentPayment()){
				//continue;
			//}
			
			allFeeAccountMap.put(feePaymentDetail.getFeeAccount().getId(), (feePaymentDetail.getFeeAccount().getName()+"("+feePaymentDetail.getFeeAccount().getCode()+")"));
			if(feePaymentDetail.getTotalNonAdditionalAmount() != null) {
				accountWiseNonOptionalAmount.put(feePaymentDetail.getFeeAccount().getId(), feePaymentDetail.getTotalNonAdditionalAmount().doubleValue());
			}	
			if(feePaymentDetail.getTotalAdditionalAmount() != null) {
				accountWiseOptionalAmount.put(feePaymentDetail.getFeeAccount().getId(), feePaymentDetail.getTotalAdditionalAmount().doubleValue());
			}	
			accountWiseTotalAmount.put(feePaymentDetail.getFeeAccount().getId(), feePaymentDetail.getTotalAmount().doubleValue());
			accountWiseConcessionAmount.put(feePaymentDetail.getFeeAccount().getId(), feePaymentDetail.getConcessionAmount().doubleValue());

			printChalanTO = new PrintChalanTO();
			printChalanTO.setPrintAccountName(feePaymentDetail.getFeeAccount().getPrintAccountName());
			if(feePaymentDetail.getFeeAccount().getCode().equalsIgnoreCase("993"))
			//printChalanTO.setAccountName("Christ Educational Society");
			//else printChalanTO.setAccountName("Christ University");
			printChalanTO.setAccId(feePaymentDetail.getFeeAccount().getId());
			if(feePaymentDetail.getTotalNonAdditionalAmount() != null) {
				printChalanTO.setNonAdditionalAmount(Integer.toString(feePaymentDetail.getTotalNonAdditionalAmount().intValue()));
				totalAmt = feePaymentDetail.getTotalNonAdditionalAmount().intValue(); 
			}
			if(feePaymentDetail.getTotalAdditionalAmount() != null) {
				printChalanTO.setAdditionalAMount(Integer.toString(feePaymentDetail.getTotalAdditionalAmount().intValue()));
				totalAmt = totalAmt + feePaymentDetail.getTotalAdditionalAmount().intValue(); 
			}
			if(feePaymentDetail.getTotalAmount()!= null) {
				printChalanTO.setTotalAmount(Integer.toString(feePaymentDetail.getTotalAmount().intValue()));
			}
			if(feePaymentDetail.getFeeAccount()!= null && feePaymentDetail.getFeeAccount().getBankInformation()!= null){
				printChalanTO.setBankInfo(feePaymentDetail.getFeeAccount().getBankInformation());
			}
			
			if(feePaymentDetail.getCurrency()!= null){
				//printChalanTO.setCurrencyCode(feePaymentDetail.getCurrency().getCurrencyCode());
			}
			//------
			//Iterator<FeePaymentDetailFeegroup> addItr = feePaymentDetail.getFeePaymentDetailFeegroups().iterator();
			List<FeePaymentDetailFeeGroupTO> additionalList = new ArrayList<FeePaymentDetailFeeGroupTO>();
			/*while(addItr.hasNext()){
				feePaymentDetailFeegroup = addItr.next();
				if(!feePaymentDetailFeegroup.getIsOptional()){
					continue;
				}*/
				feeDetailFeeGroupTO = new FeePaymentDetailFeeGroupTO();
				/*feeDetailFeeGroupTO.setFeeGroupId(feePaymentDetailFeegroup.getFeeGroup().getId());
				feeDetailFeeGroupTO.setFeeGroupName(feePaymentDetailFeegroup.getFeeGroup().getName());
				feeDetailFeeGroupTO.setAmount(Integer.toString(feePaymentDetailFeegroup.getAmount().intValue()));
				*/additionalList.add(feeDetailFeeGroupTO);
			}
			//-----------
			//Collections.sort(additionalList);
			//printChalanTO.setAdditionalList(additionalList);
			
			String verified = "";
			/*if(feePaymentDetail.getFeeAccount()!= null && feePaymentDetail.getFeeAccount().getVerifiedBy()!= null){
				printChalanTO.setVerified(feePaymentDetail.getFeeAccount().getVerifiedBy());
				verified = feePaymentDetail.getFeeAccount().getVerifiedBy();
				StringTokenizer token = new StringTokenizer(verified, " "); 
				List<String> verifiedList = new ArrayList<String>();
				while(token.hasMoreTokens()) {
					String val = token.nextToken(); 
					verifiedList.add(val);
				}
				printChalanTO.setVerifiedList(verifiedList);
			}*/
			
			
			int deducationAmt = 0; 

			/*if(feePaymentDetail.getConcessionAmount()!= null && feePaymentDetail.getConcessionAmount().doubleValue() > 0){
				printChalanTO.setConcessionAmt(Integer.toString(feePaymentDetail.getConcessionAmount().intValue()));
				deducationAmt = feePaymentDetail.getConcessionAmount().intValue(); 
				printChalanTO.setIsConcession(true);
			}*/
			/*if(feePaymentDetail.getInstallmentAmount()!= null && feePaymentDetail.getInstallmentAmount().doubleValue() > 0){
				printChalanTO.setInstallmentAmt(Integer.toString(feePaymentDetail.getInstallmentAmount().intValue()));
				deducationAmt = deducationAmt + feePaymentDetail.getInstallmentAmount().intValue();
				printChalanTO.setIsInstallment(true);
			}
			if(feePaymentDetail.getSchlorshipAmount()!= null && feePaymentDetail.getSchlorshipAmount().doubleValue() > 0){
				printChalanTO.setScholarShipAmt(Integer.toString(feePaymentDetail.getSchlorshipAmount().intValue()));
				deducationAmt = deducationAmt + feePaymentDetail.getSchlorshipAmount().intValue();
				printChalanTO.setIsScholarShipAmt(true);
			}
			*/
			
			
			int netAmt = totalAmt - deducationAmt;
			//printChalanTO.setNetAmount(Integer.toString(netAmt));
			String amtInWord = CommonUtil.numberToWord(netAmt) + " Only ";
			
			/*if(feePaymentDetail.getFeePayment().getCurrency()!= null && feePaymentDetail.getFeePayment().getCurrency().getCurrencyCode()!= null){
					amtInWord = amtInWord + "(IN " + feePaymentDetail.getCurrency().getCurrencyCode() + ")";	
				if(feePaymentDetail.getCurrency()!= null && !currList.contains(feePaymentDetail.getCurrency().getId())){
					currList.add(feePaymentDetail.getCurrency().getId());
				}
			}
			printChalanTO.setAmountInWord(amtInWord);
			printChalanTO.setLogoBytes(feePaymentDetail.getFeeAccount().getLogo());
			printChalanTO.setCount(count);

*/
			if(netAmt!= 0){
				if(!accwiseTotalString.toString().trim().isEmpty()){
					accwiseTotalString.append(" + ");
				}
				accwiseTotalString.append(Integer.toString(netAmt));
			}
			grandTotal = grandTotal + netAmt;
			descList = new ArrayList<String>();
			bankInfoList = new ArrayList<String>();
		    char slashN='\n';
		    char slashR='\r';
		/*	if(feePaymentDetail.getFeeAccount()!= null && feePaymentDetail.getFeeAccount().getDescription1()!= null){
				printChalanTO.setFeeDesc(feePaymentDetail.getFeeAccount().getDescription1());
				String desc = String.valueOf(feePaymentDetail.getFeeAccount().getDescription1());
				char[] descCharArray = desc.toCharArray();
			    	
			    StringBuffer descString = new StringBuffer();  
			    if(descCharArray!= null){
			    	for(int i = 0; i< descCharArray.length;i++){
			    		char descChar = descCharArray[i];
			    		if(descChar == slashR){
			    			descString.append("$");
			    		}
			    		else if(descChar != slashN){
			    			descString.append(descCharArray[i]);
			    		}
			    	}
			    }
			    if(descString!= null){
				 	StringTokenizer st = new StringTokenizer(descString.toString(), "$"); 
					
					while(st.hasMoreTokens()) {
						String val = st.nextToken(); 
						descList.add(val);
					}
			    }
			    //printChalanTO.setDescList(descList);
			}*/
			descList = new ArrayList<String>();
			/*if(feePaymentDetail.getFeeAccount()!= null && feePaymentDetail.getFeeAccount().getDescription2()!= null){
				printChalanTO.setFeeDesc(feePaymentDetail.getFeeAccount().getDescription2());
				String desc = String.valueOf(feePaymentDetail.getFeeAccount().getDescription2());
				char[] descCharArray = desc.toCharArray();
			    	
			    StringBuffer descString = new StringBuffer();  
			    if(descCharArray!= null){
			    	for(int i = 0; i< descCharArray.length;i++){
			    		char descChar = descCharArray[i];
			    		if(descChar == slashR){
			    			descString.append("$");
			    		}
			    		else if(descChar != slashN){
			    			descString.append(descCharArray[i]);
			    		}
			    	}
			    }
			    if(descString!= null){
				 	StringTokenizer st = new StringTokenizer(descString.toString(), "$"); 
					
					while(st.hasMoreTokens()) {
						String val = st.nextToken(); 
						descList.add(val);
					}
			    }
			    //printChalanTO.setDesc2List(descList);
			}*/
			
		/*	if(feePaymentDetail.getFeeAccount()!= null && feePaymentDetail.getFeeAccount().getBankInformation()!= null){
				String bankInfo = String.valueOf(feePaymentDetail.getFeeAccount().getBankInformation());
				char[] bankCharArray = bankInfo.toCharArray();
			    	
			    StringBuffer bankInfoString = new StringBuffer();  
			    if(bankCharArray.length>0){
			    	for(int i = 0; i< bankCharArray.length;i++){
			    		char bankCharChar = bankCharArray[i];
			    		if(bankCharChar == slashR){
			    			bankInfoString.append("$");
			    		}
			    		else if(bankCharChar != slashN){
			    			bankInfoString.append(bankCharArray[i]);
			    		}
			    	}
			    }
			    if(!bankInfoString.toString().isEmpty()){
				 	StringTokenizer st = new StringTokenizer(bankInfoString.toString(), "$"); 
					
					while(st.hasMoreTokens()) {
						String bankVal = st.nextToken(); 
						bankInfoList.add(bankVal);
					}
			    }
			   // printChalanTO.setBankInfoList(bankInfoList);
				
			}*/
			//session.setAttribute("image_" + count, printChalanTO);
			count++;
			
		   // printChalanList.add(printChalanTO);
		}
		/*if(currList.size() <= 1){
			loginForm.setAccwiseTotalPrintString(accwiseTotalString.toString() + " = " + Integer.toString(grandTotal.intValue()));
		}
		loginForm.setAccountWiseNonOptionalAmount(accountWiseNonOptionalAmount);
		loginForm.setAccountWiseOptionalAmount(accountWiseOptionalAmount);
		loginForm.setAllFeeAccountMap(allFeeAccountMap);
		loginForm.setFullAccountWiseTotal(accountWiseTotalAmount);
		loginForm.setPrintChalanList(printChalanList);
		if(printChalanList!= null ){
			if(printChalanList.size() == 1){
				loginForm.setIsSinglePrint(true);
			}
			else
			{
				loginForm.setIsSinglePrint(false);
			}*/
				
		}
		/*Collections.sort(printChalanList);
		if(printChalanList!=null){
			loginForm.setLastNo(printChalanList.size()-1);
		} */
 	
	//}
	//}
	/**
	 * @param studentId
	 * @return
	 */
	public List<OnlinePaymentReciepts> getOnlineReciepts(int studentId) throws Exception{
		String query="from OnlinePaymentReciepts o where o.isPaymentFailed=0 and o.student.id="+studentId;
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		List<OnlinePaymentReciepts> list=txn.getDataForQuery(query);
		return list;
	}
	/**
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<OnlinePaymentRecieptsTo> convertOnlineRecieptToToList( List<OnlinePaymentReciepts> list,HttpServletRequest request) throws Exception {
		List<OnlinePaymentRecieptsTo> toList=new ArrayList<OnlinePaymentRecieptsTo>();
		OnlinePaymentRecieptsTo to=null;
		int count=1;
		List<GroupTemplate> templateList= TemplateHandler.getInstance().getDuplicateCheckList(0,CMSConstants.TEMPLATE_SUPPLEMENTARY_APPLICATION_PRINT);
		for (OnlinePaymentReciepts bo: list) {
			to=new OnlinePaymentRecieptsTo();
			to.setId(bo.getId());
			System.out.println("Id set");
			to.setCount(count);
			System.out.println("Count set");
			to.setRecieptNo(bo.getRecieptNo());
			System.out.println("Recipt set");
			to.setTransactionDate(CommonUtil.formatSqlDate1(bo.getTransactionDate().toString()));
			System.out.println("Date set");
			to.setApplicationFor(bo.getApplicationFor());
			System.out.println("Application type set");
			to.setMsg(NewSupplementaryImpApplicationHelper.getInstance().getPrintData(bo, templateList,request));
			System.out.println("Message set");
			count++;
			System.out.println(toList);
			toList.add(to);
		}
		
		return toList;
	}
	
	public List<StudentGroupTO> convertTo(List<StudentGroup> studentgroup)  throws Exception{
		List<StudentGroupTO> extentionTO = new ArrayList<StudentGroupTO>();
		Iterator<StudentGroup> iterator = studentgroup.iterator();
		while(iterator.hasNext())
		{
			StudentGroupTO obj_TO = new StudentGroupTO();
			StudentGroup group = iterator.next();
			
			if(group.getGroupName() != null && !group.getGroupName().isEmpty()){
			obj_TO.setGroupName(group.getGroupName());
			}
			if(group.getId() != 0){
			obj_TO.setId(group.getId());
			}
			extentionTO.add(obj_TO);
		}
		return extentionTO;
	}
	public List<StudentExtentionTO> convertBOTOTO(List<StudentExtention> studentextention){
		List<StudentExtentionTO> studentTO = new ArrayList<StudentExtentionTO>();
		Iterator<StudentExtention> iterator = studentextention.iterator();
		while(iterator.hasNext()){
			StudentExtentionTO obj = new StudentExtentionTO();
			StudentExtention extention = (StudentExtention)iterator.next();
			obj.setActivityName(extention.getActivityName());
	        obj.setGroupName(extention.getStudentGroup().getGroupName());
			obj.setId(extention.getId());
			studentTO.add(obj);
			
		}
		return studentTO;
	}
	
	public List<StudentExtentionActivityDetails> convertStudentActivitiesTOsToBos(LoginForm loginForm) throws Exception {
		List<StudentExtentionActivityDetails> activityDetails = new ArrayList<StudentExtentionActivityDetails>();
		Iterator<StudentExtentionTO> it = loginForm.getExList().iterator();
		while(it.hasNext()) {
			StudentExtentionTO to = it.next();
			if(to.getChecked() != null && to.getChecked()) {
				StudentExtentionActivityDetails activityDetail = new StudentExtentionActivityDetails();
				
				Student student = new Student();
				student.setId(loginForm.getStudentId());
				activityDetail.setStudent(student);
				
				StudentExtention studentExtention = new StudentExtention();
				studentExtention.setId(to.getId());
				activityDetail.setStudentExtention(studentExtention);
				
				activityDetail.setPreference(Integer.parseInt(to.getPreference()));
				activityDetail.setCreatedBy(loginForm.getUserId());
				activityDetail.setCreatedDate(new Date());
				activityDetail.setModifiedBy(loginForm.getUserId());
				activityDetail.setLastModifiedDate(new Date());
				
				activityDetails.add(activityDetail);
			}
		}
		
		return activityDetails;
	}
	public CandidatePGIDetailsForStuSemesterFees convertToBo(LoginForm loginForm) throws Exception {
		CandidatePGIDetailsForStuSemesterFees bo=new CandidatePGIDetailsForStuSemesterFees();
		StringBuilder temp=new StringBuilder();
		//raghu
		//log.error(admForm.getResponseMsg());
		//log.error(admForm.getHash());
		/*if(admForm.getResponseMsg()!=null){
		//responseArray=admForm.getResponseMsg().split(("\\|"));
		 responseArray=admForm.getHash().split(("\\|"));
		}*/
		
		if(!loginForm.getAdditionalCharges().equalsIgnoreCase("0")){
			//check student curretn mail and payumail same or not 
			if(loginForm.getContactMail()!=null && !loginForm.getContactMail().equalsIgnoreCase("") && loginForm.getContactMail().equalsIgnoreCase(loginForm.getContactMail())){
				temp.append(loginForm.getAdditionalCharges()).append("|").append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
				
			}else if(loginForm.getContactMail()!=null && !loginForm.getContactMail().equalsIgnoreCase("") && !loginForm.getContactMail().equalsIgnoreCase(loginForm.getContactMail())){
				
				temp.append(loginForm.getAdditionalCharges()).append("|").append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
			}else{
				temp.append(loginForm.getAdditionalCharges()).append("|").append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
				
			}
			
		}else{
			if(loginForm.getContactMail()!=null && !loginForm.getContactMail().equalsIgnoreCase("")  && loginForm.getContactMail().equalsIgnoreCase(loginForm.getContactMail())){
			    temp.append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getPaymentStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
			
			}else if(loginForm.getContactMail()!=null && !loginForm.getContactMail().equalsIgnoreCase("") && !loginForm.getContactMail().equalsIgnoreCase(loginForm.getContactMail())){
				
				temp.append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getPaymentStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
				
			}else {
				
				temp.append(CMSConstants.PGI_SECURITY_ID_STU).append("|").append(loginForm.getPaymentStatus()).append("|||||||||||").append(loginForm.getContactMail()).append("|").append(loginForm.getStudentName()).append("|").append(loginForm.getProductinfo()).append("|").append(loginForm.getStudentSemesterFeesAmount()).append("|").append(loginForm.getTxnid()).append("|").append(CMSConstants.PGI_MERCHANT_ID_STU);
				
			}
		}
		
		System.out.println("+++++++++++++++++++++++++++++++++++  this is data before hash alogoritham ++++++++++++++++++++++++++++++"+temp.toString());
		
		//sha512(additionalCharges|<SALT>|status|||||||||||email|firstname|productinfo|amount|txnid|key)
		//raghu write for pay e
		String hash=StudentLoginHandler.getInstance().hashCal("SHA-512",temp.toString());
		
		
		System.out.println("+++++++++++++++++++++++++++++++++++  this is data of after  hash  generation ++++++++++++++++++++++++++++++"+hash);
		
		System.out.println("+++++++++++++++++++++++++++++++++++  this is data of pay u hash ++++++++++++++++++++++++++++++"+loginForm.getHash());
		
		if(loginForm.getHash()!=null && hash!=null && !loginForm.getHash().equals(hash)){
			//log.error("############################ Your Data Tamperd ########################");
			throw  new BusinessException();
		}else{
			bo.setCandidateRefNo(loginForm.getTxnid());
			bo.setTxnRefNo(loginForm.getPayuMoneyId());
			bo.setTxnAmount(new BigDecimal(loginForm.getStudentSemesterFeesAmount()));
			bo.setAdditionalCharges(new BigDecimal(loginForm.getAdditionalCharges()));
			bo.setBankRefNo(loginForm.getBank_ref_num());
			bo.setTxnStatus(loginForm.getPaymentStatus());
			bo.setErrorStatus(loginForm.getError1());
			//raghu setting current date
			bo.setTxnDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy")));
			bo.setMode(loginForm.getMode1());
			bo.setUnmappedStatus(loginForm.getUnmappedstatus());
			bo.setMihpayId(loginForm.getMihpayid());
			bo.setPgType(loginForm.getPG_TYPE());
			//raghu new
			bo.setPaymentEmail(loginForm.getContactMail());
			bo.setTermNo(loginForm.getTermNo());
			
			loginForm.setPgiStatus("Payment Successful");
			loginForm.setTxnAmount(loginForm.getStudentSemesterFeesAmount());
			loginForm.setTxnRefNo(loginForm.getPayuMoneyId());
			loginForm.setTxnDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy"));
			
		}
		
		return bo;
	
	}
	public List<PublishSpecialFeesTO> convertBOtoTOSpecial(List<PublishSpecialFees> list)  throws Exception{
		List<PublishSpecialFeesTO> list2 = new ArrayList<PublishSpecialFeesTO>();
		if(list != null && !list.isEmpty()){
			for (PublishSpecialFees publishSpecialFees : list) {
				PublishSpecialFeesTO to = new PublishSpecialFeesTO();
				to.setClassName(publishSpecialFees.getClasses().getName());
				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				String fromDate = simpleDateFormat1.format(publishSpecialFees.getFromDate());
				to.setFromDate(fromDate);
				String toDate = simpleDateFormat1.format(publishSpecialFees.getToDate());
				to.setToDate(toDate);
				list2.add(to);
			}
		}
		return list2;
	}
	public CandidatePGIForSpecialFees convertBOTO(LoginForm loginForm)  throws Exception{
		CandidatePGIForSpecialFees bo = new CandidatePGIForSpecialFees();
		bo.setCandidateRefNo(loginForm.getTxnid());
		bo.setTxnRefNo(loginForm.getPayuMoneyId());
		bo.setTxnAmount(new BigDecimal(loginForm.getSpecialFeesAmount()));
		if(loginForm.getAdditionalCharges() != null){
		bo.setAdditionalCharges(new BigDecimal(loginForm.getAdditionalCharges()));
		}
		bo.setBankRefNo(loginForm.getBank_ref_num());
		bo.setTxnStatus(loginForm.getPaymentStatus());
		bo.setErrorStatus(loginForm.getError1());
		//raghu setting current date
		bo.setTxnDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy")));
		bo.setMode(loginForm.getMode1());
		bo.setUnmappedStatus(loginForm.getUnmappedstatus());
		bo.setMihpayId(loginForm.getMihpayid());
		bo.setPgType(loginForm.getPG_TYPE());
		//raghu new
		bo.setPaymentEmail(loginForm.getContactMail());
		
		
		loginForm.setPgiStatus("Payment Successful");
		loginForm.setTxnAmount(loginForm.getSpecialFeesAmount());
		loginForm.setTxnRefNo(loginForm.getPayuMoneyId());
		loginForm.setTxnDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy"));
		
	//}
	
	return bo;
	}
}
