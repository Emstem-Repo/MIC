package com.kp.cms.actions.exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.actions.admission.NewStudentCertificateCourseAction;
import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamBlockUnblockHallTicketBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.exam.ExamStudentTokenRegisterdForm;
import com.kp.cms.forms.exam.ExamSupplementaryImpAppForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.handlers.exam.NewSupplementaryImpApplicationHandler;
import com.kp.cms.handlers.exam.StudentTokenRegisteredHandler;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;
import com.kp.cms.utilities.PropertyUtil;

public class NewExamStudentTokenRegisteredEntryAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(NewExamStudentTokenRegisteredEntryAction.class);
	
	public ActionForward initStudentTokenRegisteredEntry(ActionMapping mapping,ActionForm form,HttpServletRequest request,
				HttpServletResponse response) throws Exception {
		
		log.info("Entered initStudentRegisteredEntry input");
		ExamStudentTokenRegisterdForm examStudentTokenRegisteredForm = (ExamStudentTokenRegisterdForm)form;
		examStudentTokenRegisteredForm.resetFields();// resetting the fields for jsp.
		setRequiredDatatoForm(examStudentTokenRegisteredForm, request);
		log.info("Exit initStudentRegisteredEntry input");
		
		return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
	}

	private void setRequiredDatatoForm(ExamStudentTokenRegisterdForm examStudentTokenRegisteredForm,HttpServletRequest request) throws Exception {
		int year = CurrentAcademicYear.getInstance().getAcademicyear();
		HttpSession session = request.getSession();
		if(examStudentTokenRegisteredForm.getAcademicYear()!= null && !examStudentTokenRegisteredForm.getAcademicYear().isEmpty()){
			year = Integer.parseInt(examStudentTokenRegisteredForm.getAcademicYear());
			
		}
		if(year == 0){
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
		}
		Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByExamTypeAndYear(examStudentTokenRegisteredForm.getExamType(),year);
		examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);
		examStudentTokenRegisteredForm.setExamNameMap(examNameMap);
		String currentExam = ExamMarksEntryHandler.getInstance().getCurrentExamName(examStudentTokenRegisteredForm.getExamType());
		if(examStudentTokenRegisteredForm.getExamId() == null || examStudentTokenRegisteredForm.getExamId().isEmpty() && currentExam != null || !currentExam.isEmpty()){
			examStudentTokenRegisteredForm.setExamId(currentExam);
		}
		String registrationNumber = examStudentTokenRegisteredForm.getRegistrationNumber();
		examStudentTokenRegisteredForm.setRegistrationNumber(registrationNumber);
		examStudentTokenRegisteredForm.setAcademicYear(String.valueOf(year));
	}
	
@SuppressWarnings("deprecation")
	/*public ActionForward submitStudentSuppApplication(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		ExamStudentTokenRegisterdForm  examStudentTokenRegisterdForm = (ExamStudentTokenRegisterdForm)form;
		ActionErrors errors = examStudentTokenRegisterdForm.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		if(errors.isEmpty()){
			
			try{
				List<StudentSupplementaryImprovementApplication> examSuppImpApp = StudentTokenRegisteredHandler.getInstance().checkValidSupp(examStudentTokenRegisterdForm);
				setUserId(request, examStudentTokenRegisterdForm);
				if(examSuppImpApp != null && !examSuppImpApp.isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.hostel.application.form.already.exists"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}
				else {
					// new ExamRegularApplication
					boolean isSaved = StudentTokenRegisteredHandler.getInstance().saveSupplementryApplication(examStudentTokenRegisterdForm);
					if(isSaved) {
						ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess", "Supplymentary exam Token registraion");
						messages.add("messages", message);
						saveMessages(request, messages);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					}
					else {
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Supplymentary exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);			    		
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				if(e instanceof DataNotFoundException) {
					errors.add("error", new ActionError("knowledgepro.exam.tokenRegistration.noMatchingExam"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}				
				String msg = super.handleApplicationException(e);
            	examStudentTokenRegisterdForm.setErrorMessage(msg);
            	examStudentTokenRegisterdForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
			
		}
		return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
		
	}*/
	
	/*public ActionForward submitStudentRegApplication(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		ExamStudentTokenRegisterdForm  examStudentTokenRegisterdForm = (ExamStudentTokenRegisterdForm)form;
		ActionErrors errors = examStudentTokenRegisterdForm.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		if(errors.isEmpty()){
			
			try{
				ExamRegularApplication regularApplication = StudentTokenRegisteredHandler.getInstance().checkValid(examStudentTokenRegisterdForm);
				setUserId(request, examStudentTokenRegisterdForm);
				if(regularApplication != null && regularApplication.getIsApplied()) {
					errors.add("error", new ActionError("knowledgepro.hostel.application.form.already.exists"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}
				else if(regularApplication != null && !regularApplication.getIsApplied()) {
					//
					boolean isUpdatedPayment= StudentTokenRegisteredHandler.getInstance().updateResponseReg(examStudentTokenRegisterdForm);
					
					if(examStudentTokenRegisterdForm.getIsTnxStatusSuccess()) {
						//boolean isUpdated = StudentTokenRegisteredHandler.getInstance().updateRegularApplication(regularApplication);
						//if(isUpdated){
						ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess", "Regular exam Token registraion");
						messages.add("messages", message);
						saveMessages(request, messages);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					//	}
						else
						{
							errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
							saveErrors(request,errors);
							return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
						}
					}
					else {
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);			    		
					}
				}				
				else {
					// new ExamRegularApplication
					boolean isUpdatedPayment= StudentTokenRegisteredHandler.getInstance().updateResponseReg(examStudentTokenRegisterdForm);
					if(examStudentTokenRegisterdForm.getIsTnxStatusSuccess()){
					//boolean isSaved = StudentTokenRegisteredHandler.getInstance().saveRegularApplication(examStudentTokenRegisterdForm);
					//if(isSaved) {
						
						ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess", "Regular exam Token registraion");
						messages.add("messages", message);
						saveMessages(request, messages);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					//}
					else {
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);			    		
					}
					}
					else{
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					}
						
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				if(e instanceof DataNotFoundException) {
					errors.add("error", new ActionError("knowledgepro.exam.tokenRegistration.noMatchingExam"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}				
				String msg = super.handleApplicationException(e);
            	examStudentTokenRegisterdForm.setErrorMessage(msg);
            	examStudentTokenRegisterdForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
			
		}
		return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
		
	}*/
		

	public ActionForward submitStudentRegApplicationManual(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		ExamStudentTokenRegisterdForm  examStudentTokenRegisterdForm = (ExamStudentTokenRegisterdForm)form;
		ActionErrors errors = examStudentTokenRegisterdForm.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		if(errors.isEmpty()){
		
			try{
				examStudentTokenRegisterdForm.setChallanButton(false);
				StudentTokenRegisteredHandler.getInstance().getStudentObject(examStudentTokenRegisterdForm);
				boolean isRegisterNoValid=StudentTokenRegisteredHandler.getInstance().isRegisterNoValid(examStudentTokenRegisterdForm);
				if(!isRegisterNoValid){
					errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Register No is not valid."));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}
				ExamRegularApplication regularApplication = StudentTokenRegisteredHandler.getInstance().checkValid(examStudentTokenRegisterdForm);
				setUserId(request, examStudentTokenRegisterdForm);
				if(regularApplication != null && regularApplication.getIsApplied()) {
					errors.add("error", new ActionError("knowledgepro.hostel.application.form.already.exists"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}
				else if(regularApplication != null && !regularApplication.getIsApplied()) {
				
				
					boolean isUpdated = StudentTokenRegisteredHandler.getInstance().updateRegularApplication(regularApplication);
					if(isUpdated){
						ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess", "Regular exam Token registraion");
						messages.add("messages", message);
						saveMessages(request, messages);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					}
	
					else {
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);			    		
					}
				}				
				else {
					// new ExamRegularApplication
					boolean isSaved = StudentTokenRegisteredHandler.getInstance().saveRegularApplication(examStudentTokenRegisterdForm);
					if(isSaved) {
					
					ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess", "Regular exam Token registraion");
					messages.add("messages", message);
					saveMessages(request, messages);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					}
				
					else{
						errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Regular exam Token registraion"));
						saveErrors(request,errors);
						return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
					}
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				if(e instanceof DataNotFoundException) {
					errors.add("error", new ActionError("knowledgepro.exam.tokenRegistration.noMatchingExam"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
				}				
				String msg = super.handleApplicationException(e);
				examStudentTokenRegisterdForm.setErrorMessage(msg);
				examStudentTokenRegisterdForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		
		}
		return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
	
	}

	public ActionForward printRegularApplication(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ExamStudentTokenRegisterdForm  examStudentTokenRegisterdForm = (ExamStudentTokenRegisterdForm)form;
		ActionErrors errors=new ActionErrors();
		ExamRegularApplication regularApplication = StudentTokenRegisteredHandler.getInstance().checkValid(examStudentTokenRegisterdForm);
		if((regularApplication != null && !regularApplication.getIsApplied()) || (regularApplication == null)) {
			examStudentTokenRegisterdForm.setChallanButton(false);
			errors.add("error", new ActionError("knowledgepro.norecords"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
		}
		else{
			examStudentTokenRegisterdForm.setChallanButton(true);
			StudentTokenRegisteredHandler.getInstance().getStudentObject(examStudentTokenRegisterdForm);
			setDataToFormForRegular(examStudentTokenRegisterdForm, request);
			return mapping.findForward(CMSConstants.EXAM_STUDENT_TOKEN_REGISTERED_ENTRY);
		}
	}
	

	public ActionForward printRegularApplication1(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ExamStudentTokenRegisterdForm  examStudentTokenRegisterdForm = (ExamStudentTokenRegisterdForm)form;
		ActionErrors errors=new ActionErrors();
		if(examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()==1){
			return mapping.findForward("printDetailsforRegularUGAdmin");
		}else{
			return mapping.findForward("printDetailsforRegularPGAdmin");
		}
		
	}
	
	private void setDataToFormForRegular( ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm , HttpServletRequest request) throws Exception {
		HttpSession session=request.getSession();
		Student student = examStudentTokenRegisterdForm.getStudentObj();
		List<Integer> examIds = new ArrayList<Integer>();
		examStudentTokenRegisterdForm.setClassId(String.valueOf(student.getClassSchemewise().getClasses().getId()));
		//examIds=NewSupplementaryImpApplicationHandler.getInstance().checkRegularAppAvailable(student.getClassSchemewise().getClasses().getId());
		IDownloadHallTicketTransaction txn1=DownloadHallTicketTransactionImpl.getInstance();
		if(examStudentTokenRegisterdForm.getExamId()!=null){
		/*if(examIds.isEmpty() || !examIds.contains(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()))){
			examStudentTokenRegisterdForm.setRegularAppAvailable(false);
		}*/
		
		//else{
			/*ExamBlockUnblockHallTicketBO h=null;
			for (Integer examId: examIds) {
				h=txn1.isHallTicketBlockedStudent(student.getId(), student.getClassSchemewise().getClasses().getId(), examId,"A");
			}
			if(h!=null){
				examStudentTokenRegisterdForm.setRegularAppAvailable(false);
			}else{*/
				
				
				
				examStudentTokenRegisterdForm.setRegularAppAvailable(true);
				StudentTokenRegisteredHandler.getInstance().getApplicationFormsForRegular(examIds,examStudentTokenRegisterdForm);
				
				
				if(student!=null){
					String STUDENT_IMAGE = "images/StudentPhotos/"+student.getId()+".jpg?"+student.getAdmAppln().getLastModifiedDate();
					session.setAttribute("STUDENT_IMAGE", STUDENT_IMAGE);
					
					examStudentTokenRegisterdForm.setNameOfStudent(student.getAdmAppln().getPersonalData().getFirstName()+(student.getAdmAppln().getPersonalData().getMiddleName()!=null?student.getAdmAppln().getPersonalData().getMiddleName():"")+(student.getAdmAppln().getPersonalData().getLastName()!=null?student.getAdmAppln().getPersonalData().getLastName():""));
					examStudentTokenRegisterdForm.setClassName(student.getClassSchemewise()!=null?student.getClassSchemewise().getClasses().getName():"");
					examStudentTokenRegisterdForm.setRegNo(student.getRegisterNo());
					examStudentTokenRegisterdForm.setRollNo(student.getRollNo());
					examStudentTokenRegisterdForm.setDob(null);
					examStudentTokenRegisterdForm.setOriginalDob(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()), NewStudentCertificateCourseAction.SQL_DATEFORMAT,NewStudentCertificateCourseAction.FROM_DATEFORMAT));
					examStudentTokenRegisterdForm.setAddress("");
					examStudentTokenRegisterdForm.setCourseName(student.getAdmAppln().getCourseBySelectedCourseId().getName());
					examStudentTokenRegisterdForm.setCourseDep(student.getAdmAppln().getCourseBySelectedCourseId().getCode());
					examStudentTokenRegisterdForm.setSchemeNo(""+student.getClassSchemewise().getClasses().getTermNumber());
					examStudentTokenRegisterdForm.setEmail(student.getAdmAppln().getPersonalData().getEmail());
					examStudentTokenRegisterdForm.setMobileNo(student.getAdmAppln().getPersonalData().getMobileNo2());
					examStudentTokenRegisterdForm.setCourseId(""+student.getClassSchemewise().getClasses().getCourse().getId());
									
					examStudentTokenRegisterdForm.setDate(CommonUtil.getTodayDate());
					examStudentTokenRegisterdForm.setGender(student.getAdmAppln().getPersonalData().getGender());
					String address="";
					if(student.getAdmAppln().getPersonalData().getPermanentAddressLine1()!=null && !student.getAdmAppln().getPersonalData().getPermanentAddressLine1().equalsIgnoreCase("")){
						address=address+""+student.getAdmAppln().getPersonalData().getPermanentAddressLine1()+",";
					}
					if(student.getAdmAppln().getPersonalData().getPermanentAddressLine2()!=null && !student.getAdmAppln().getPersonalData().getPermanentAddressLine2().equalsIgnoreCase("")){
						address=address+""+student.getAdmAppln().getPersonalData().getPermanentAddressLine2()+",";
					}
					if(student.getAdmAppln().getPersonalData().getStateByParentAddressDistrictId()!=null ){
						address=address+""+student.getAdmAppln().getPersonalData().getStateByParentAddressDistrictId().getName()+",";
					}else if(student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers()!=null && !student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers().equalsIgnoreCase("")){
						address=address+""+student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers()+",";
						
					}
					if(student.getAdmAppln().getPersonalData().getStateByPermanentAddressStateId()!=null ){
						address=address+""+student.getAdmAppln().getPersonalData().getStateByPermanentAddressStateId().getName();
					}else if(student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers()!=null && !student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers().equalsIgnoreCase("")){
						address=address+""+student.getAdmAppln().getPersonalData().getPermanentAddressStateOthers();
						
					}
					if(student.getAdmAppln().getPersonalData().getPermanentAddressZipCode()!=null)
						examStudentTokenRegisterdForm.setPermanentAddressZipCode(student.getAdmAppln().getPersonalData().getPermanentAddressZipCode());
						
					examStudentTokenRegisterdForm.setAddress(address.toUpperCase());
					
					// vinodha
					examStudentTokenRegisterdForm.setCommunicationEmail(student.getAdmAppln().getPersonalData().getEmail());
					examStudentTokenRegisterdForm.setCommunicationMobileNo(student.getAdmAppln().getPersonalData().getMobileNo2());
					
					String communicationAddress="";
					if(student.getAdmAppln().getPersonalData().getCurrentAddressLine1()!=null && !student.getAdmAppln().getPersonalData().getCurrentAddressLine1().equalsIgnoreCase("")){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getCurrentAddressLine1()+",";
					}
					if(student.getAdmAppln().getPersonalData().getCurrentAddressLine2()!=null && !student.getAdmAppln().getPersonalData().getCurrentAddressLine2().equalsIgnoreCase("")){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getCurrentAddressLine2()+",";
					}
					if(student.getAdmAppln().getPersonalData().getStateByCurrentAddressDistrictId()!=null ){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getStateByCurrentAddressDistrictId().getName()+",";
					}else if(student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers()!=null && !student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers().equalsIgnoreCase("")){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers()+",";
						
					}
					if(student.getAdmAppln().getPersonalData().getStateByCurrentAddressStateId()!=null ){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getStateByCurrentAddressStateId().getName();
					}else if(student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers()!=null && !student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers().equalsIgnoreCase("")){
						communicationAddress=communicationAddress+""+student.getAdmAppln().getPersonalData().getCurrentAddressStateOthers();
					}
					if(student.getAdmAppln().getPersonalData().getCurrentAddressZipCode()!=null)
						examStudentTokenRegisterdForm.setCommunicationAddressZipCode(student.getAdmAppln().getPersonalData().getCurrentAddressZipCode());
					
					examStudentTokenRegisterdForm.setCommunicationAddress(communicationAddress.toUpperCase());
					
					if(student.getAdmAppln().getPersonalData().getReligionSection()!=null)
						examStudentTokenRegisterdForm.setFeeConcessionCategory(student.getAdmAppln().getPersonalData().getReligionSection().getName());
					
					if(student.getAdmAppln().getPersonalData().getReligionSection()!=null)
						if(student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("sc") || 
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("st") ||
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("OEC") ||
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("FMN") ||
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("KPCR") ||
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("SEBC") ||
						   student.getAdmAppln().getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBC (Non-Creamy)"))
							examStudentTokenRegisterdForm.setFeeConcession("YES");
						else
							examStudentTokenRegisterdForm.setFeeConcession("NO");
					
					String religion="";
					if(student.getAdmAppln().getPersonalData().getReligion()!=null)
						religion = religion+""+student.getAdmAppln().getPersonalData().getReligion().getName();
					else if(student.getAdmAppln().getPersonalData().getReligionOthers()!=null)
						religion = religion+","+student.getAdmAppln().getPersonalData().getReligionOthers();
					if(student.getAdmAppln().getPersonalData().getCaste()!=null)
						religion = religion+","+student.getAdmAppln().getPersonalData().getCaste().getName();
					else if(student.getAdmAppln().getPersonalData().getCasteOthers()!=null)
						religion = religion+","+student.getAdmAppln().getPersonalData().getCasteOthers();
					
					examStudentTokenRegisterdForm.setReligion(religion);
					
					String careTaker="";
					String fatherTitle="";
					String motherTitle="";
					AdmapplnAdditionalInfo addInfoAdmAppln=NewSupplementaryImpApplicationHandler.getInstance().getTitleForCareTaker(student.getAdmAppln().getId());
					if(addInfoAdmAppln!=null)
					{
						if(addInfoAdmAppln.getTitleFather()!=null && !addInfoAdmAppln.getTitleFather().isEmpty())
							fatherTitle="Father";
						if(addInfoAdmAppln.getTitleMother()!=null && !addInfoAdmAppln.getTitleMother().isEmpty())
							if(addInfoAdmAppln.getTitleMother().equalsIgnoreCase("sr"))
								motherTitle="Sister";
							else
								motherTitle="Mother";
					}
					if(student.getAdmAppln().getPersonalData().getFatherName()!=null && !student.getAdmAppln().getPersonalData().getFatherName().isEmpty())				
						careTaker = student.getAdmAppln().getPersonalData().getFatherName()+","+fatherTitle;	
					else if(student.getAdmAppln().getPersonalData().getMotherName()!=null && !student.getAdmAppln().getPersonalData().getMotherName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData().getMotherName()+","+motherTitle;
					else if(student.getAdmAppln().getPersonalData().getGuardianName()!=null && !student.getAdmAppln().getPersonalData().getGuardianName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData().getGuardianName()+",Guardian";
					
					examStudentTokenRegisterdForm.setCareTaker(careTaker.toUpperCase());
				}
				
			//}
			
		}
	}
	
}
