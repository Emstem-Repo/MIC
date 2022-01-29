package com.kp.cms.actions.admission;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

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

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePreference;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.bo.admin.InterviewCardHistory;
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.forms.admission.OnlineApplicationForm;
import com.kp.cms.forms.admission.UniqueIdRegistrationForm;
import com.kp.cms.handlers.admin.AdmittedThroughHandler;
import com.kp.cms.handlers.admin.MaintenanceAlertHandler;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.handlers.admission.AdmissionFormHandler;
import com.kp.cms.handlers.admission.AdmissionStatusHandler;
import com.kp.cms.handlers.admission.CertificateCourseEntryHandler;
import com.kp.cms.handlers.admission.OnlineApplicationHandler;
import com.kp.cms.helpers.admission.AdmissionStatusHelper;
import com.kp.cms.to.admin.AdmittedThroughTO;
import com.kp.cms.to.admin.ApplnDocTO;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admin.OrganizationTO;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.AdmissionStatusTO;
import com.kp.cms.to.admission.CertificateCourseTO;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;
import com.kp.cms.transactionsimpl.admission.AdmissionStatusTransactionImpl;
import com.kp.cms.utilities.CommonUtil;



@SuppressWarnings("deprecation")
public class AdmissionStatusAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(AdmissionStatusAction.class);
	private static final String DATE = "MM/dd/yyyy"; 
	private static final String FROM_DATEFORMAT="dd/MM/yyyy";
	private static final String SQL_DATEFORMAT="dd-MMM-yyyy";
	private static final String PHOTOBYTES="PhotoBytes";
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param Initializes
	 *            Admission status
	 * @returns Admission Status with Application No & Date of birth as two
	 *          fields
	 * @throws Exception
	 */

	public ActionForward initAdmissionStatus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Entered into initAdmissionStatus of AdmissionStatusAction");
		AdmissionStatusForm admissionStatusForm=(AdmissionStatusForm)form;		
		try {
			admissionStatusForm.clear();
			admissionStatusForm.clearadmissionStatusTO();
			admissionStatusForm.clearstatusTO();	
		} catch (Exception e) {
			log.error("Error occured at initAdmissionStatus of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into initAdmissionStatus of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.ADMISSION_STATUS);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @returns the Admit Card for an application
	 * @throws Exception
	 */
	public ActionForward downloadInterviewCard(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Entered into downloadInterviewCard of AdmissionStatusAction");
		ActionErrors errors = new ActionErrors();
		AdmissionStatusForm admissionStatusForm = (AdmissionStatusForm) form;
		try {
			if (admissionStatusForm.getApplicationNo() != null && admissionStatusForm.getCourseId()!=null && !admissionStatusForm.getApplicationNo().trim().isEmpty() && !admissionStatusForm.getCourseId().trim().isEmpty() && admissionStatusForm.getInterviewTypeId()!=null && !admissionStatusForm.getInterviewTypeId().trim().isEmpty()) {
				
				List<GroupTemplate> groupTemplateList = TemplateHandler.getInstance().getDuplicateCheckList(Integer.parseInt(admissionStatusForm.getCourseId()), CMSConstants.E_ADMITCARD_TEMPLATE);
				String templateDescription = "";
				
				Iterator<GroupTemplate> iterator = groupTemplateList.iterator();
				while (iterator.hasNext()) {
					GroupTemplate groupTemplate = (GroupTemplate) iterator.next();
					templateDescription = groupTemplate.getTemplateDescription();
				}
				//------------bar code creation added by Manu
				Barcode code = null;
				File barCodeImgFile = null;
				if(!admissionStatusForm.getApplicationNo().trim().isEmpty()){
					String imgPath=request.getRealPath("");
					imgPath = imgPath + "//BarCode//"+ admissionStatusForm.getApplicationNo() + ".jpeg";
					 barCodeImgFile =  new File(imgPath);
					if(barCodeImgFile.exists()){
						barCodeImgFile.delete();
					}
					code = BarcodeFactory.createCode128A(admissionStatusForm.getApplicationNo());
					code.setBarWidth(1);
					code.setBarHeight(40);
					code.setDrawingText(false);
					BarcodeImageHandler.saveJPEG(code, barCodeImgFile);
				}
				
				List<InterviewCard> admitCardList = AdmissionStatusHandler.getInstance().getStudentAdmitCardDetails(admissionStatusForm.getApplicationNo(), admissionStatusForm.getInterviewTypeId());
				/*code added by sudhir */
				List<InterviewCardHistory> admitCardHistoryList = AdmissionStatusHandler.getInstance().getStudentAdmitCardHistoryDetails(admissionStatusForm.getApplicationNo());
				/*---------------------*/
				//pass barCodeImgFile by manu
				  if(admitCardList !=null && !admitCardList.isEmpty()){
					String finalTemplate = AdmissionStatusHelper.generateAdmitCard(templateDescription, admitCardList, request,admitCardHistoryList,barCodeImgFile);
					admissionStatusForm.setTemplateDescription(finalTemplate);
				}
				
				if(groupTemplateList != null && groupTemplateList.isEmpty() && admitCardList != null && admitCardList.isEmpty()) {
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_ICARD_NOTEXISTS));
					saveErrors(request, errors);
					admissionStatusForm.clear();
					return mapping.findForward(CMSConstants.ADMISSION_STATUS);
				}
			}
		} catch (Exception e) {
			log.error("Error occured at downloadInterviewCard of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into downloadInterviewCard of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.ADMIT_CARD_PRINT);
	}
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @returns the Admission card for an application
	 * @throws Exception
	 */
	public ActionForward downloadAdmissionCard(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered into downloadAdmissionCard of AdmissionStatusAction");
		ActionErrors errors = new ActionErrors();
		AdmissionStatusForm admissionStatusForm = (AdmissionStatusForm) form;
		try {
			if (admissionStatusForm.getApplicationNo() != null && admissionStatusForm.getCourseId()!=null && !admissionStatusForm.getApplicationNo().trim().isEmpty() && !admissionStatusForm.getCourseId().trim().isEmpty() && admissionStatusForm.getInterviewTypeId()!=null && !admissionStatusForm.getInterviewTypeId().trim().isEmpty()) {
				
				List<GroupTemplate> groupTemplateList = TemplateHandler.getInstance().getDuplicateCheckList(Integer.parseInt(admissionStatusForm.getCourseId()), CMSConstants.E_ADMISSIONCARD_TEMPLATE);
				String templateDescription = "";
				
				Iterator<GroupTemplate> iterator = groupTemplateList.iterator();
				while (iterator.hasNext()) {
					GroupTemplate groupTemplate = (GroupTemplate) iterator.next();
					templateDescription = groupTemplate.getTemplateDescription();
				}
				AdmAppln admAppln  = AdmissionStatusHandler.getInstance().getApplicantDetails(admissionStatusForm.getApplicationNo());
				/*List<InterviewCard> admitCardList = AdmissionStatusHandler.getInstance().getStudentAdmitCardDetails(admAppln);
				if(admitCardList !=null && !admitCardList.isEmpty()){
					String finalTemplate = AdmissionStatusHelper.generateAdmitCard(templateDescription, admitCardList, request);
					admissionStatusForm.setTemplateDescription(finalTemplate);
				}*/
				//------------bar code creation added by Manu
				Barcode code = null;
				File barCodeImgFile = null;
				if(!admissionStatusForm.getApplicationNo().trim().isEmpty()){
					String imgPath=request.getRealPath("");
					imgPath = imgPath + "//BarCode//"+ admissionStatusForm.getApplicationNo() + ".jpeg";
					 barCodeImgFile =  new File(imgPath);
					if(barCodeImgFile.exists()){
						barCodeImgFile.delete();
					}
					code = BarcodeFactory.createCode128A(admissionStatusForm.getApplicationNo());
					code.setBarWidth(1);
					code.setBarHeight(40);
					code.setDrawingText(false);
					BarcodeImageHandler.saveJPEG(code, barCodeImgFile);
				}
				
				//pass barCodeImgFile by manu
				if(admAppln !=null && admAppln.getApplnNo() > 0){
					String finalTemplate = AdmissionStatusHelper.generateAdmissionCard(templateDescription, admAppln, request,barCodeImgFile);
					admissionStatusForm.setTemplateDescription(finalTemplate);
				}
							
				if(groupTemplateList != null && groupTemplateList.isEmpty() && admAppln != null) {
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_ICARD_NOTEXISTS));
					saveErrors(request, errors);
					admissionStatusForm.clear();
					return mapping.findForward(CMSConstants.ADMISSION_STATUS);
				}
			}
		} catch (Exception e) {
			log.error("Error occured at downloadAdmissionCard of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into downloadAdmissionCard of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.ADMIT_CARD_PRINT);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param Takes
	 *            input as the Application No & Date of birth
	 * @param response
	 *            gives the status Selected/Not selected
	 * @returns the last round result if that candidate is not selected for admission
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward getAdmissionStatus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
        AdmissionStatusAction.log.info((Object)"Inside of getOnlineApplicationStatus of AdmissionStatusAction");
        final AdmissionStatusForm admissionStatusForm = (AdmissionStatusForm)form;
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        admissionStatusForm.clearadmissionStatusTO();
        admissionStatusForm.clearstatusTO();
        admissionStatusForm.clearpref();
        final ActionErrors errors = admissionStatusForm.validate(mapping, request);
        admissionStatusForm.setOnlineAcknowledgement(false);
        if (admissionStatusForm.getDateOfBirth() != null && !StringUtils.isEmpty(admissionStatusForm.getDateOfBirth()) && CommonUtil.isValidDate(admissionStatusForm.getDateOfBirth())) {
            final boolean isValid = this.validatefutureDate(admissionStatusForm.getDateOfBirth());
            if (!isValid) {
                if (errors.get("admissionFormForm.dob.large") != null && !errors.get("admissionFormForm.dob.large").hasNext()) {
                    errors.add("admissionFormForm.dob.large", new ActionError("admissionFormForm.dob.large"));
                }
                admissionStatusForm.clearadmissionStatusTO();
                admissionStatusForm.clearstatusTO();
            }
        }
        try {
            if (errors.isEmpty() && errors != null) {
                final String applicationNo = admissionStatusForm.getApplicationNo().trim();
                final List<AdmissionStatusTO> AdmAppln = (List<AdmissionStatusTO>)AdmissionStatusHandler.getInstance().getDetailsAdmAppln(applicationNo, admissionStatusForm);
                if (AdmAppln.isEmpty()) {
                    final boolean availableInApplnAcknowledgement = AdmissionStatusHandler.getInstance().checkApplnAvailableInAck(applicationNo, admissionStatusForm.getDateOfBirth());
                    if (availableInApplnAcknowledgement) {
                        final AdmissionStatusTO admissionStatusTO = new AdmissionStatusTO();
                        admissionStatusForm.setAdmStatus(CMSConstants.ADM_STATUS_OFFLINE_APPLN);
                        admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
                        admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), "dd/MM/yyyy", "dd-MMM-yyyy"));
                        admissionStatusTO.setEmail("");
                        admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                        admissionStatusForm.clear();
                        admissionStatusForm.clearstatusTO();
                        return mapping.findForward("getAdmissionStatus");
                    }
                    errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.invalidappno"));
                    this.saveErrors(request, errors);
                    admissionStatusForm.clearadmissionStatusTO();
                    admissionStatusForm.clearstatusTO();
                    return mapping.findForward("initapplicationstatus");
                }
                else {
                    if (AdmAppln.size() > 1) {
                        errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.invaliddata"));
                        this.saveErrors(request, errors);
                        admissionStatusForm.clear();
                        admissionStatusForm.clearadmissionStatusTO();
                        admissionStatusForm.clearstatusTO();
                        return mapping.findForward("getAdmissionStatus");
                    }
                    final Iterator<AdmissionStatusTO> it = AdmAppln.iterator();
                    this.setDiplomaCourses(admissionStatusForm);
                    while (it.hasNext()) {
                        final AdmissionStatusTO admissionStatusTO = it.next();
                        if (admissionStatusTO.isCancelled()) {
                            errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.is.cancelled"));
                            this.saveErrors(request, errors);
                            admissionStatusForm.clear();
                            admissionStatusForm.clearadmissionStatusTO();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("initapplicationstatus");
                        }
                        /*if (admissionStatusTO.isAdmitted()) {
                            errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.is.admitted"));
                            this.saveErrors(request, errors);
                            admissionStatusForm.clear();
                            admissionStatusForm.clearadmissionStatusTO();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("getAdmissionStatus");
                        }*/
                        admissionStatusForm.setAdmStatus(null);
                        if (admissionStatusTO.getAdmStatus() != null && !admissionStatusTO.getAdmStatus().trim().isEmpty()) {
                            admissionStatusForm.setAdmStatus(admissionStatusTO.getAdmStatus());
                        }
                        if (admissionStatusTO.getIsSelected() == null || admissionStatusTO.getIsSelected().isEmpty()) {
                            admissionStatusTO.setIsSelected("Application Submitted Online - Send the Application and Supporting Documents to Office of Admissions");
                            admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
                            admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), "dd/MM/yyyy", "dd-MMM-yyyy"));
                            admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                            admissionStatusForm.clear();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (admissionStatusTO.getDateOfBirth() == null || admissionStatusTO.getPersonalDataId() == 0) {
                            errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.nullfields"));
                            this.saveErrors(request, errors);
                            admissionStatusForm.clear();
                            admissionStatusForm.clearadmissionStatusTO();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (admissionStatusTO.getApplicationNo() == 0 || admissionStatusTO.getIsSelected() == null || admissionStatusTO.getDateOfBirth() == null) {
                            continue;
                        }
                        final String uiDob = admissionStatusForm.getDateOfBirth();
                        final String toDateofBirth = CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), "dd-MMM-yyyy", "dd/MM/yyyy");
                        if (!uiDob.equals(toDateofBirth)) {
                            errors.add("error", new ActionError("knowledgepro.admission.admissionstatus.invaliddob"));
                            this.saveErrors(request, errors);
                            admissionStatusForm.clearadmissionStatusTO();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (!admissionStatusTO.getIsSelected().equalsIgnoreCase("You are Selected for Admission") && admissionStatusTO.isCancelled()) {
                            admissionStatusTO.setIsSelected("Application Cancelled");
                            admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
                            admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), "dd/MM/yyyy", "dd-MMM-yyyy"));
                            admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                            admissionStatusForm.clear();
                            admissionStatusForm.clearstatusTO();
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (!admissionStatusTO.getIsSelected().equalsIgnoreCase("You are Selected for Admission") && admissionStatusTO.getInterviewSelectionSchedule() != null) {
                            final AdmissionStatusTO admTO = AdmissionStatusHandler.getInstance().getInterviewResult(applicationNo, admissionStatusTO.getAppliedYear());
                            admissionStatusForm.setStatusTO(admTO);
                            admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (!admissionStatusTO.getIsSelected().equalsIgnoreCase("You are Selected for Admissions")) {
                            final AdmissionStatusTO admTO = AdmissionStatusHandler.getInstance().getInterviewResult(applicationNo, admissionStatusTO.getAppliedYear());
                            admissionStatusForm.setStatusTO(admTO);
                            admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                            admissionStatusForm.setIsChecked(false);
                            if (admissionStatusForm.getSelectedCourseId() > 0 && admissionStatusForm.getSelectedValue() != null && !admissionStatusForm.getSelectedValue().isEmpty()) {
                                AdmissionStatusHandler.getInstance().updateCourseAllotment(applicationNo, admissionStatusForm);
                            }
                            if (admissionStatusForm.getUploadDetail() != null && !admissionStatusForm.getUploadDetail().isEmpty()) {
                                AdmissionStatusHandler.getInstance().uploadDetail(applicationNo, admissionStatusForm);
                            }
                            final List<AdmissionStatusTO> statusTOs = (List<AdmissionStatusTO>)AdmissionStatusHandler.getInstance().getToListForStatus(applicationNo, admissionStatusForm);
                            admissionStatusForm.setStatusTOs(statusTOs);
                            return mapping.findForward("getAdmissionStatus");
                        }
                        if (admissionStatusForm.getApplicationNo() != null) {
                            final List interviewCardTOList = AdmissionStatusHandler.getInstance().getStudentsList(admissionStatusForm.getApplicationNo());
                            if (interviewCardTOList != null && !interviewCardTOList.isEmpty()) {
                                if (admissionStatusTO.getIsSelected() != null && admissionStatusTO.getIsSelected().equalsIgnoreCase("You are Selected for Admission")) {
                                    admissionStatusTO.setIsInterviewSelected("admission");
                                }
                            }
                            else if (admissionStatusTO.getIsSelected().equalsIgnoreCase("You are Selected for Admission")) {
                                admissionStatusTO.setIsInterviewSelected("admission");
                            }
                            else {
                                admissionStatusTO.setIsInterviewSelected("false");
                            }
                        }
                        admissionStatusForm.setAppliedYear(admissionStatusTO.getAppliedYear());
                        admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                        if (admissionStatusTO.isByPassed()) {
                            admissionStatusTO.setIsInterviewSelected("admission");
                        }
                        admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
                        admissionStatusForm.clear();
                        admissionStatusForm.clearstatusTO();
                        return mapping.findForward("getAdmissionStatus");
                    }
                }
            }
        }
        catch (Exception e) {
            AdmissionStatusAction.log.error((Object)"Error occured at getOnlineApplicationStatus of Admission StatusAction", (Throwable)e);
            final String msg = super.handleApplicationException(e);
            admissionStatusForm.setErrorMessage(msg);
            admissionStatusForm.setErrorStack(e.getMessage());
            return mapping.findForward("newerrorpage");
        }
        this.saveErrors(request, errors);
        AdmissionStatusAction.log.info((Object)"Leaving from getOnlineApplicationStatus of AdmissionStatusAction");
        return mapping.findForward("getAdmissionStatus");
        
	}
	private void setDiplomaCourses(AdmissionStatusForm admissionStatusForm) throws Exception {
		Map<Integer, String>  certificateMap = AdmissionStatusHandler.getInstance().getActiveCourses1(admissionStatusForm.getAppliedYear());
		admissionStatusForm.setCerticateCourses(certificateMap);
	}

	/**
	 * Method to check the entered date is not a future date
	 * @param dateString
	 * @return boolean value
	 */
	
	private boolean validatefutureDate(String dateString) throws Exception{
		log.info("Entering into validatefutureDate of AdmissionStatusAction");
		String formattedString=CommonUtil.ConvertStringToDateFormat(dateString, CMSConstants.DEST_DATE,DATE);
		Date date = new Date(formattedString);
		Date currentDate = new Date();
		if (date.compareTo(currentDate) == 1)
			return false;
		else
			log.info("Leaving into validatefutureDate of AdmissionStatusAction");
			return true;
	}	
	/**
	 * Used to view Application and print the application form
	 */
	public ActionForward downloadApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entering into downloadApplication of AdmissionStatusAction");
		AdmissionStatusForm admForm =(AdmissionStatusForm)form;
		admForm.setCasteDisplay(CMSConstants.CASTE_ENABLED);
		try{
			 ActionErrors errors = admForm.validate(mapping, request);
			
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				
				return mapping.findForward(CMSConstants.ADMISSIONFORM_INITAPPROVAL);
			}
				String applicationNumber = admForm.getApplicationNo().trim();
				int applicationYear = admForm.getAppliedYear();
				AdmApplnTO applicantDetails = AdmissionFormHandler.getInstance().getApplicationDetails(applicationNumber, applicationYear);
				
				if( applicantDetails == null){					
					ActionMessages messages = new ActionMessages();
					ActionMessage message = null;
						message = new ActionMessage(CMSConstants.ADMISSIONFORM_NOAPPROVAL_PENDING);
						messages.add("messages", message);
						saveMessages(request, messages);						
						return mapping.findForward(CMSConstants.VIEW_APPLICATION);
					
				} else {
					
					if(applicantDetails.getId()>0)
					{
						String txnRefNo = AdmissionFormHandler.getInstance().getCandidatePGIDetails(applicantDetails.getId());
						if(txnRefNo!=null && !txnRefNo.isEmpty())
						admForm.setTxnRefNo(txnRefNo);
					}
					//------------bar code creation
					if(!applicationNumber.trim().isEmpty()){
						String imgPath=request.getRealPath("");
						imgPath = imgPath + "//BarCode//"+ applicationNumber + ".jpeg";
						File barCodeImgFile =  new File(imgPath);
						if(barCodeImgFile.exists()){
							barCodeImgFile.delete();
						}
						Barcode code = BarcodeFactory.createCode128A(applicationNumber);
						code.setBarWidth(1);
						code.setBarHeight(40);
						code.setDrawingText(false);
						BarcodeImageHandler.saveJPEG(code, barCodeImgFile);
					}								
					
					if(applicantDetails.getPersonalData()!=null && applicantDetails.getPersonalData().getDob()!=null )
						applicantDetails.getPersonalData().setDob(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getDob(), AdmissionStatusAction.SQL_DATEFORMAT,AdmissionStatusAction.FROM_DATEFORMAT));
					if(applicantDetails.getChallanDate()!=null )
						applicantDetails.setChallanDate(CommonUtil.ConvertStringToDateFormat(applicantDetails.getChallanDate(), AdmissionStatusAction.SQL_DATEFORMAT,AdmissionStatusAction.FROM_DATEFORMAT));
					//set applicant details
					admForm.setApplicantDetails(applicantDetails);
					String workExpNeeded=admForm.getApplicantDetails().getCourse().getIsWorkExperienceRequired();
					if(admForm.getApplicantDetails().getCourse()!=null && "Yes".equalsIgnoreCase(workExpNeeded))
					{
						admForm.setWorkExpNeeded(true);
					}else{
						admForm.setWorkExpNeeded(false);
					}
						admForm.setApplicantDetails(applicantDetails);
					ProgramTypeHandler programtypehandler = ProgramTypeHandler.getInstance();
						List<ProgramTypeTO> programtypes = programtypehandler
									.getProgramType();
						// set flags
						if (programtypes!=null) {
							admForm.setEditProgramtypes(programtypes);
							Iterator<ProgramTypeTO> typeitr= programtypes.iterator();
							while (typeitr.hasNext()) {
								ProgramTypeTO typeTO = (ProgramTypeTO) typeitr.next();
								if(typeTO.getProgramTypeId()==admForm.getApplicantDetails().getCourse().getProgramTypeId()){
									if(typeTO.getPrograms()!=null){
										admForm.setEditprograms(typeTO.getPrograms());
										Iterator<ProgramTO> programitr= typeTO.getPrograms().iterator();
											while (programitr.hasNext()) {
												ProgramTO programTO = (ProgramTO) programitr
														.next();
												if(programTO.getId()== admForm.getApplicantDetails().getCourse().getProgramId()){
													admForm.setEditcourses(programTO.getCourseList());
													if(programTO!=null){ 
														if(programTO.getIsMotherTongue()==true)
														admForm.setDisplayMotherTongue(true);
														if(programTO.getIsDisplayLanguageKnown()==true)
															admForm.setDisplayLanguageKnown(true);
														if(programTO.getIsHeightWeight()==true)
															admForm.setDisplayHeightWeight(true);
														if(programTO.getIsDisplayTrainingCourse()==true)
															admForm.setDisplayTrainingDetails(true);
														if(programTO.getIsAdditionalInfo()==true)
															admForm.setDisplayAdditionalInfo(true);
														if(programTO.getIsExtraDetails()==true)
															admForm.setDisplayExtracurricular(true);
														if(programTO.getIsSecondLanguage()==true)
															admForm.setDisplaySecondLanguage(true);
														if(programTO.getIsFamilyBackground()==true)
															admForm.setDisplayFamilyBackground(true);
														if(programTO.getIsTCDetails()==true)
															admForm.setDisplayTCDetails(true);
														if(programTO.getIsEntranceDetails()==true)
															admForm.setDisplayEntranceDetails(true);
														if(programTO.getIsLateralDetails()==true)
															admForm.setDisplayLateralDetails(true);
														if(programTO.getIsTransferCourse()==true)
															admForm.setDisplayTransferCourse(true);
													}
												}
											}
									}	
								}
							}
						}
					
						List<AdmittedThroughTO> admittedList=AdmittedThroughHandler.getInstance().getAdmittedThrough();
						admForm.setAdmittedThroughList(admittedList);
						OrganizationHandler orgHandler= OrganizationHandler.getInstance();
						
							List<OrganizationTO> tos=orgHandler.getOrganizationDetails();
							if(tos!=null && !tos.isEmpty())
							{
								OrganizationTO orgTO=tos.get(0);
								admForm.setOrganizationName(orgTO.getOrganizationName());
								admForm.setNeedApproval(orgTO.isNeedApproval());
							}
							
							//Sets Logo
							Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
							if(organisation!=null){
								StringBuffer orgAdd=new StringBuffer();
								orgAdd.append(organisation.getAddressLine1());
								if(organisation.getAddressLine2()!=null && !StringUtils.isEmpty(organisation.getAddressLine2())){
								orgAdd.append(",");
								orgAdd.append(organisation.getAddressLine2());
								}
								if(organisation.getAddressLine3()!=null && !StringUtils.isEmpty(organisation.getAddressLine3())){
								orgAdd.append(",");
								orgAdd.append(organisation.getAddressLine3());
								}
								
								admForm.setOrgAddress(orgAdd.toString());
								// set photo to session
								if(organisation.getLogoContentType()!=null){
									HttpSession session= request.getSession(false);
									if(session!=null){
										session.setAttribute(CMSConstants.KNOWLEDGEPRO_LOGO, organisation.getLogo());
										
									}
								}
							}		
							
							
					//get template and replace the data
					String template=AdmissionFormHandler.getInstance().getDeclarationTemplate(applicantDetails,request);
					admForm.setInstrTemplate(template);
							
				
					if(applicantDetails.getPersonalData()!=null && applicantDetails.getPersonalData().getPassportValidity()!=null && !StringUtils.isEmpty(applicantDetails.getPersonalData().getPassportValidity()) )
						applicantDetails.getPersonalData().setPassportValidity(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getPassportValidity(), AdmissionStatusAction.SQL_DATEFORMAT,AdmissionStatusAction.FROM_DATEFORMAT));
					
					// set photo to session
					
					// set photo to session
					if(applicantDetails.getEditDocuments()!=null){
						Iterator<ApplnDocTO> docItr=applicantDetails.getEditDocuments().iterator();
						while (docItr.hasNext()) {
							ApplnDocTO docTO = (ApplnDocTO) docItr.next();
							if(docTO.getSubmitDate()!=null && !StringUtils.isEmpty(docTO.getSubmitDate()))
							{
								admForm.setSubmitDate(docTO.getSubmitDate());
							}
							if(docTO.getDocName()!=null && docTO.getDocName().equalsIgnoreCase("Photo") && docTO.isDefaultPhoto())
							{
								HttpSession session= request.getSession(false);
								if(session!=null){
									byte[] fileData= null;
									// set default photo image
									try {
										InputStream photoin=AdmissionFormAction.class.getClassLoader().getResourceAsStream(CMSConstants.PRINT_PHOTO_PATH);
										
										if(photoin!=null){
											fileData= new byte[photoin.available()];
											photoin.read(fileData, 0, photoin.available());	
										}
									} catch (Exception e) {
										log.error(e);
									}
									
									if(fileData!=null)
									session.setAttribute(AdmissionStatusAction.PHOTOBYTES,fileData );
								}
								request.setAttribute("STUDENT_IMAGE", "images/photoblank.gif");
							}else if(docTO.getDocName()!=null && docTO.getDocName().equalsIgnoreCase("Photo")){
								
								//raghu
								if(request.getSession()!=null){
									request.getSession().setAttribute("PhotoBytes", docTO.getPhotoBytes());
								}
								//request.setAttribute("STUDENT_IMAGE", "images/StudentPhotos/"+applicantDetails.getStudentId()+".jpg?"+applicantDetails.getLastModifiedDate());
							}
						}
					}
					String detailMarkPrint=admForm.getApplicantDetails().getCourse().getIsDetailMarkPrint();
					if(detailMarkPrint!=null && "Yes".equalsIgnoreCase(detailMarkPrint))
					{
						admForm.setDisplaySemister(true);
					}else{
						admForm.setDisplaySemister(false);
					}
				}
				if(admForm.isWorkExpNeeded()){
					String totalExp=AdmissionFormAction.getTotalYearsOfExperience(applicantDetails.getWorkExpList());
					admForm.setTotalYearOfExp(totalExp);
				}
			}catch (Exception e){
				e.printStackTrace();
				String msg = super.handleApplicationException(e);
				admForm.setErrorMessage(msg);
				admForm.setErrorStack(e.getMessage());
				//return mapping.findForward(CMSConstants.ERROR_PAGE);
				return mapping.findForward("newerrorpage");
			}
			
		if(admForm.getChanceMemo()!=null && admForm.getChanceMemo()){
			final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
	        final List<AdmAppln> newList = (List<AdmAppln>)admissionStatusTransaction.getDetailsAdmAppln(admForm.getApplicationNo().trim());
	        final List<AdmissionStatusTO> list = (List<AdmissionStatusTO>)AdmissionStatusHandler.getInstance().getchanceList((List)newList, admForm);
	        admForm.getAdmissionStatusTO().setChanceList((List)list);
	        if (admForm.getChanceMemo() != null && admForm.getChanceMemo()) {
	            final AdmissionStatusTO to2 = null;
	            final AdmissionStatusTO to3 = admForm.getAdmissionStatusTO();
	            for (final AdmissionStatusTO name : admForm.getAdmissionStatusTO().getChanceList()) {
	                System.out.println(name.getChanceCurrentcourse());
	            }
	            request.setAttribute("chanceCourseId", (Object)admForm.getChanceCourseId());
	            return mapping.findForward("adminChanceMemo");
	        }	
	        }
	        if (admForm.isMemo()) {
	            return mapping.findForward("viewallotmentmemo");
	        }
		
		return mapping.findForward("viewapplication");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param Initializes
	 *            Admission status
	 * @returns Admission Status with Application No & Date of birth as two
	 *          fields
	 * @throws Exception
	 */

	public ActionForward initOutsideAccessAdmissionStatusOfStudent(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Entered into initOutsideAccessStatus of AdmissionStatusAction");
		AdmissionStatusForm admissionStatusForm=(AdmissionStatusForm)form;	
		HttpSession session= request.getSession(false);
		
		try {
			admissionStatusForm.clear();
			admissionStatusForm.clearadmissionStatusTO();
			admissionStatusForm.clearstatusTO();
			admissionStatusForm.setIsOnceAccept(false);
			admissionStatusForm.setStatusTOs(null);
			Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
			if(organisation!=null){
				// set photo to session
				if(organisation.getLogoContentType()!=null){
					if(session!=null){
						session.setAttribute(CMSConstants.KNOWLEDGEPRO_LOGO, organisation.getLogo());
						session.setAttribute(CMSConstants.KNOWLEDGEPRO_TOPBAR, organisation.getTopbar());
					}
				}
			}
			session.setAttribute("isOnline", "1");
			//added by mahi start
			admissionStatusForm.setServerDownMessage(null);
			 String maintenanceMessage =  MaintenanceAlertHandler.getInstance().getMaintenanceDetailsByDate();
			 if(maintenanceMessage!=null){
				 admissionStatusForm.setServerDownMessage(maintenanceMessage);
				 session.setAttribute("serverDownMessage", maintenanceMessage);
			 }
			 //end
		} catch (Exception e) {
			log.error("Error occured at initOutsideAccessStatus of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			//return mapping.findForward(CMSConstants.ERROR_PAGE);
			return mapping.findForward("newerrorpage");
		}
		log.info("Leaving into initAdmissionStatus of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
	}
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param Takes
	 *            input as the Application No & Date of birth
	 * @param response
	 *            gives the status Selected/Not selected
	 * @returns the last round result if that candidate is not selected for admission
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward getOnlineApplicationStatus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Inside of getOnlineApplicationStatus of AdmissionStatusAction");
		AdmissionStatusForm admissionStatusForm = (AdmissionStatusForm) form;
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		admissionStatusForm.clearadmissionStatusTO();
		admissionStatusForm.clearstatusTO();
		admissionStatusForm.clearpref();
		 admissionStatusForm.setCourseId(null);
		 ActionErrors errors = admissionStatusForm.validate(mapping, request);
		admissionStatusForm.setOnlineAcknowledgement(false);
		if (admissionStatusForm.getDateOfBirth() != null && !StringUtils.isEmpty(admissionStatusForm.getDateOfBirth())) {
			if (CommonUtil.isValidDate(admissionStatusForm.getDateOfBirth())) {
				boolean isValid = validatefutureDate(admissionStatusForm.getDateOfBirth());
				if (!isValid) {
					if (errors.get(CMSConstants.ADMISSIONFORM_DOB_LARGE) != null && !errors.get(CMSConstants.ADMISSIONFORM_DOB_LARGE).hasNext()) {
						errors.add(CMSConstants.ADMISSIONFORM_DOB_LARGE, new ActionError(CMSConstants.ADMISSIONFORM_DOB_LARGE));
					}
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
				}
			}	
		}
		try {
			if (errors.isEmpty() && errors != null) {
				String applicationNo = admissionStatusForm.getApplicationNo().trim();
				/**
				 * Calling the getDateOfBirth() by passing the applicationNo
				 * taken from UI and getting the record (date of birth along with all the fiels of AdmAppln)
				 */
				List<AdmissionStatusTO> AdmAppln = AdmissionStatusHandler.getInstance().getDetailsAdmAppln(applicationNo,admissionStatusForm);
				
				/**
				 * Checks If user enters wrong application no. then add appropriate error message 
				 */
				if (AdmAppln.isEmpty()) {
					/* Check if application no is available in Appln Acknowledgement table if available then appropriate msg is displayed  */
					boolean availableInApplnAcknowledgement=AdmissionStatusHandler.getInstance().checkApplnAvailableInAck(applicationNo,admissionStatusForm.getDateOfBirth());
					if(availableInApplnAcknowledgement){
						AdmissionStatusTO admissionStatusTO=new AdmissionStatusTO();
						admissionStatusForm.setAdmStatus(CMSConstants.ADM_STATUS_OFFLINE_APPLN);
						admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
						admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), CMSConstants.DEST_DATE,CMSConstants.SOURCE_DATE));						
						admissionStatusTO.setEmail("");
						admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
						admissionStatusForm.clear();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
					}
					else{
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_APPNO_INVALID));
					saveErrors(request, errors);
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
					}
				}
				/**
				 * Checks if multiple records exists in DB based on the entered application no. from UI then add the error message
				 */
				
				if(AdmAppln.size()>1){
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_INVALID_DATA));
					saveErrors(request, errors);
					admissionStatusForm.clear();
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);			
				}
				
					
				/**
				 * Iterating the list of data getting based on the application no. entered from UI 
				 */
				
				Iterator<AdmissionStatusTO> it=AdmAppln.iterator();
				setDiplomaCourses(admissionStatusForm);
				
				while (it.hasNext()) {
					
					AdmissionStatusTO admissionStatusTO = (AdmissionStatusTO) it.next();
					if(admissionStatusTO.isCancelled()){
						errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_IS_CANCELLED));
						saveErrors(request, errors);
						admissionStatusForm.clear();
						admissionStatusForm.clearadmissionStatusTO();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);	
					}
					if(admissionStatusTO.isAdmitted()){
						errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_ADMITTED));
						saveErrors(request, errors);
						admissionStatusForm.clear();
						admissionStatusForm.clearadmissionStatusTO();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);	
					}				

				/**
				 * Checks if any of the column (Isselected/PersonalDataId/DateofBirth) is null in DB then add the appropriate error message & show the status
				 */
				admissionStatusForm.setAdmStatus(null);
				if(admissionStatusTO.getAdmStatus()!= null && !admissionStatusTO.getAdmStatus().trim().isEmpty()){
					admissionStatusForm.setAdmStatus(admissionStatusTO.getAdmStatus());
				}				
				if (admissionStatusTO.getIsSelected() == null || admissionStatusTO.getIsSelected().isEmpty())
				{
					admissionStatusTO.setIsSelected(CMSConstants.ADMISSION_ADMISSIONSTATUS_UNDER_PROCESS);
					admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
					admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), CMSConstants.DEST_DATE,CMSConstants.SOURCE_DATE));						
					admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
					admissionStatusForm.clear();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
				}
				if (admissionStatusTO.getDateOfBirth()==null || admissionStatusTO.getPersonalDataId()==0) {
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_NULL));
					saveErrors(request, errors);
					admissionStatusForm.clear();
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
				}
				/**
				 * Checks whether the date of birth retrieved from DB (if
				 * correct application no is entered) is same with the date of
				 * birth entered by user or not. If same then adding to formbean and displays the admission status in UI
				 */
				if(admissionStatusTO.getApplicationNo()!=0 && admissionStatusTO.getIsSelected()!=null && admissionStatusTO.getDateOfBirth() !=null)
				{     
			        String uiDob=admissionStatusForm.getDateOfBirth();
			        String toDateofBirth=CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), CMSConstants.SOURCE_DATE,CMSConstants.DEST_DATE);
				if (uiDob.equals(toDateofBirth)) {
					
					//Check for the cacelled admission status
					if(!admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION) && admissionStatusTO.isCancelled())
					{
						admissionStatusTO.setIsSelected(CMSConstants.ADMISSION_ADMISSIONSTATUS_APPLICATION_CANCELLED);
						admissionStatusTO.setApplicationNo(Integer.parseInt(admissionStatusForm.getApplicationNo()));
						admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusForm.getDateOfBirth(), CMSConstants.DEST_DATE,CMSConstants.SOURCE_DATE));	
						admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
						admissionStatusForm.clear();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
					}
					
					/**
					 * If the candidate is not selected for admission then check for the interview status and display the last interview round status in UI
				
					 */
				if(!admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION) && admissionStatusTO.getInterviewSelectionSchedule()!=null )
					{
						AdmissionStatusTO admTO = AdmissionStatusHandler.getInstance().getInterviewResult(applicationNo, admissionStatusTO.getAppliedYear());
						admissionStatusForm.setStatusTO(admTO);
						//vibin
						admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
						
						
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);	
					}
				else if(!admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION))
				{
					//Used to get the interview status of the application					
					AdmissionStatusTO admTO = AdmissionStatusHandler.getInstance().getInterviewResult(applicationNo, admissionStatusTO.getAppliedYear());
					admissionStatusForm.setStatusTO(admTO);
					//vibin
					admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
					
					
					admissionStatusForm.setIsChecked(false);
					if(admissionStatusForm.getSelectedCourseId() > 0 && admissionStatusForm.getSelectedValue()!=null 
							&& !admissionStatusForm.getSelectedValue().isEmpty()){
						boolean isUpdate=AdmissionStatusHandler.getInstance().updateCourseAllotment(applicationNo,admissionStatusForm);
					}
					if(admissionStatusForm.getUploadDetail()!=null && !admissionStatusForm.getUploadDetail().isEmpty()){
						boolean isUpload=AdmissionStatusHandler.getInstance().uploadDetail(applicationNo,admissionStatusForm);
					}
					List<AdmissionStatusTO> statusTOs = AdmissionStatusHandler.getInstance().getToListForStatus(applicationNo,admissionStatusForm);
					admissionStatusForm.setStatusTOs(statusTOs);
					List<CertificateCourseTO> toList=null;
					if (admissionStatusForm.getAdmApplnId()!=null) {
						toList=admissionStatusTransaction.getCertificateCoursesprint(Integer.parseInt(admissionStatusForm.getAdmApplnId()));
					}
					
					if (toList!=null && !toList.isEmpty()) {
						admissionStatusForm.setCertificationCourseDone(true);
						admissionStatusForm.setPrefList(toList);
					}
					return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);					
				}
				
					if(admissionStatusForm.getApplicationNo()!=null){
						List interviewCardTOList = AdmissionStatusHandler.getInstance().getStudentsList(admissionStatusForm.getApplicationNo());
						if(interviewCardTOList!=null && !interviewCardTOList.isEmpty()){
							if(admissionStatusTO.getIsSelected()!= null){		
									if(admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION)){
										admissionStatusTO.setIsInterviewSelected(CMSConstants.ADMISSION);
									}
							}				
						}
						else{
							if(admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION))
								admissionStatusTO.setIsInterviewSelected(CMSConstants.ADMISSION);
							else admissionStatusTO.setIsInterviewSelected("false");						
						}
					}					
						admissionStatusForm.setAppliedYear(admissionStatusTO.getAppliedYear());
						admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), CMSConstants.SOURCE_DATE,CMSConstants.SOURCE_DATE));
						//--------added for bypass
						if(admissionStatusTO.isByPassed()){
							admissionStatusTO.setIsInterviewSelected(CMSConstants.ADMISSION);
						}
						//-------
						
						admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
						admissionStatusForm.clear();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
				}
				/**
				 * Else Add appropriate error message if date of birth entered is
				 * wrong
				 */
				else {					
						errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_INVALID_DOB));
						saveErrors(request, errors);
						admissionStatusForm.clearadmissionStatusTO();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
				}
				}
				}
			}
		} catch (Exception e) {
			log.error("Error occured at getOnlineApplicationStatus of Admission StatusAction",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			//return mapping.findForward(CMSConstants.ERROR_PAGE);
			return mapping.findForward("newerrorpage");
		}
		saveErrors(request, errors);
		log.info("Leaving from getOnlineApplicationStatus of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward downloadAcknowledgement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Entered into downloadAcknowledgement of AdmissionStatusAction");
		AdmissionStatusForm admissionStatusForm=(AdmissionStatusForm)form;	
		admissionStatusForm.setOnlineAcknowledgement(false);
		try {
			IAdmissionStatusTransaction txn=new AdmissionStatusTransactionImpl();
			List<String> messageList = new ArrayList<String>();
			CandidatePGIDetails details = txn.getCandidateDetails(admissionStatusForm);
			HttpSession session = request.getSession(false);
			if(details != null){
				TemplateHandler temphandle=TemplateHandler.getInstance();
				List<GroupTemplate> list= temphandle.getDuplicateCheckList("Online payment Acknowledgement Slip");
				
				if(list != null && !list.isEmpty()) {
					String desc ="";
					if(list.get(0)!=null && list.get(0).getTemplateDescription()!=null){
						desc = list.get(0).getTemplateDescription();
					}
					desc = desc.replace("[Applicant_Name]", details.getCandidateName());
					desc = desc.replace("[APPLICATIONNO]", admissionStatusForm.getApplicationNo());
					desc = desc.replace("[COURSE]", details.getCourse().getName());
					if(details.getTxnRefNo() != null)
						desc = desc.replace("[Transaction_Ref_number]", details.getTxnRefNo());
					else
						desc = desc.replace("[Transaction_Ref_number]", "--");
					if(details.getBankRefNo() != null)
						desc = desc.replace("[Bank_Ref_number]", details.getBankRefNo());
					else
						desc = desc.replace("[Bank_Ref_number]", "--");
					if(details.getBankId() != null)
						desc = desc.replace("[Bank_ID]", details.getBankId());
					else
						desc = desc.replace("[Bank_ID]", "--");
					if(details.getCandidateRefNo() != null)
						desc = desc.replace("[Candidate_Ref_number]", details.getCandidateRefNo());
					else
						desc = desc.replace("[Candidate_Ref_number]", "--");
					desc = desc.replace("[EMAIL]", details.getEmail());
					if(details.getTxnDate() != null)
						desc = desc.replace("[Transaction_Date]",  CommonUtil.getStringDate(details.getTxnDate()));
					else
						desc = desc.replace("[Transaction_Date]",  "--");
					if(details.getTxnAmount() != null)
						desc = desc.replace("[Transaction_Amount]",  details.getTxnAmount().toString());
					else
						desc = desc.replace("[Transaction_Amount]",  "0.00");
					
				//------------bar code creation added by Manu
				if(!admissionStatusForm.getApplicationNo().trim().isEmpty()){
					String imgPath=request.getRealPath("");
					imgPath = imgPath + "//BarCode//"+ admissionStatusForm.getApplicationNo() + ".jpeg";
					File barCodeImgFile =  new File(imgPath);
					if(barCodeImgFile.exists()){
						barCodeImgFile.delete();
					}
					Barcode code = BarcodeFactory.createCode128A(admissionStatusForm.getApplicationNo());
					code.setBarWidth(1);
					code.setBarHeight(40);
					code.setDrawingText(false);
					BarcodeImageHandler.saveJPEG(code, barCodeImgFile);
					
					
					byte[] barCodeByte = new byte[(int)barCodeImgFile.length ()];
					FileInputStream fis=new FileInputStream(barCodeImgFile);
					// convert image into byte array
					fis.read(barCodeByte);
					session.setAttribute("barCodeBytes", barCodeByte);
					String barCode = request.getContextPath();
					barCode = "<img src="+ barCode+ "/BarCodeServlet alt='Barcode  not available' width='120' height='20' >";
					if(barCode != null)
						desc = desc.replace("[BARCODE]",barCode);
					else
						desc = desc.replace("[BARCODE]",  "");
					
					messageList.add(desc);
				   }
				}
				admissionStatusForm.setMessageList(messageList);
			}
		} catch (Exception e) {
			log.error("Error occured at downloadAcknowledgement of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into downloadAcknowledgement of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.VIEW_ACKNOWLEDGEMENT);
	}
	
	public ActionForward forwardDashBordPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("forwardDashBordPage");	

	}
	
	public ActionForward redirectToPayment(ActionMapping mapping,ActionForm form,
				HttpServletRequest request,HttpServletResponse response)throws Exception{
	
		AdmissionStatusForm admForm=(AdmissionStatusForm)form;
		ActionErrors errors=new ActionErrors();
		
		//validatePgi(admForm,errors);
		try{
			if(errors!=null && !errors.isEmpty()){
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
			}
			String hash= AdmissionStatusHandler.getInstance().getParameterForPGI(admForm);
			request.setAttribute("hash", hash);
			request.setAttribute("txnid", admForm.getRefNo());
			request.setAttribute("productinfo", admForm.getProductinfo());
			request.setAttribute("amount", admForm.getApplicationAmount());
			request.setAttribute("email", admForm.getEmail());
			request.setAttribute("firstname", admForm.getApplicantName());
			request.setAttribute("phone",admForm.getMobile2());
			request.setAttribute("test",admForm.getTest());
			request.setAttribute("surl",CMSConstants.PAYUMONEY_SUCCESSURL_ALLOTMENT);
			request.setAttribute("furl",CMSConstants.PAYUMONEY_FAILUREURL_ALLOTMENT);
			
		}
		catch (Exception e) {
			System.out.println("************************ error details in online admission in redirectToPGI*************************"+e);
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		
		}
		return mapping.findForward(CMSConstants.REDIRECT_TO_PGI_PAGE);
	
	
	}

	private void validatePgi(AdmissionStatusForm admForm, ActionErrors errors) {
		if((admForm.getApplicationAmount()== null || admForm.getApplicationAmount().isEmpty())
				&& (admForm.getEquivalentCalcApplnFeeINR()== null || admForm.getEquivalentCalcApplnFeeINR().isEmpty())){
			if (errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED)!=null && !errors.get(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED).hasNext()) {
				errors.add(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED, new ActionError(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED));
			}
		}
		if(CMSConstants.PGI_MERCHANT_ID== null || CMSConstants.PGI_MERCHANT_ID.isEmpty()
				|| CMSConstants.PGI_MERCHANT_ID==""){
			if (errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_MERCHANT_ID_REQUIRED, new ActionError(CMSConstants.PGI_MERCHANT_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_SECURITY_ID == null || CMSConstants.PGI_SECURITY_ID.isEmpty()
				|| CMSConstants.PGI_SECURITY_ID==""){
			if (errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED)!=null && !errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_SECURITY_ID_REQUIRED, new ActionError(CMSConstants.PGI_SECURITY_ID_REQUIRED));
			}
		}
		if(CMSConstants.PGI_CHECKSUM_KEY == null || CMSConstants.PGI_CHECKSUM_KEY.isEmpty()
				|| CMSConstants.PGI_CHECKSUM_KEY==""){
			if (errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED)!=null && !errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED, new ActionError(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED));
			}
		}
	}
	
	public ActionForward updatePGIResponse(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		AdmissionStatusForm admForm=(AdmissionStatusForm)form;
		ActionErrors errors=new ActionErrors();
		ActionMessages messages=new ActionMessages();
		
		try{
			boolean isUpdated= AdmissionStatusHandler.getInstance().updateResponse(admForm);
			if(isUpdated && admForm.getIsTnxStatusSuccess()){
				admForm.setIsPaid(true);
				messages.add(CMSConstants.MESSAGES,new ActionMessage("knowledgepro.admission.empty.err.message",admForm.getPgiStatus()));
				saveMessages(request, messages);
			}
			else{
				errors.add("error", new ActionError("knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
		}
		catch (BusinessException e) {
			errors.add("error", new ActionError("knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		}
		catch (Exception e) {
			
		     errors.add("knowledgepro.admission.boardDetails.duplicateEntry", new ActionError("knowledgepro.admission.boardDetails.duplicateEntry","Error was occured, please login and enter details again"));
		     saveErrors(request, errors);
		
		}
		return mapping.findForward(CMSConstants.PAYMENT_SUCCESS_FOR_CHANCE);
	
	
	
	}
	
	public ActionForward printAdmissionFormOnline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		String applicationNumber = admForm.getApplicationNo().trim();
		List<CertificateCourseTO> toList=new ArrayList();
		IAdmissionStatusTransaction admissionStatusTransaction=new AdmissionStatusTransactionImpl();
		int applicationYear = admForm.getAdmissionStatusTO().getAppliedYear();
		AdmApplnTO applicantDetails = AdmissionFormHandler.getInstance().getApplicationDetails(applicationNumber, applicationYear);
		//AdmApplnTO applicantDetails = AdmissionFormHandler.getInstance().getApplicantDetails(applicationNumber, applicationYear,false);
		admForm.setApplicantDetails(applicantDetails);
		toList=admissionStatusTransaction.getCertificateCoursesprint(Integer.parseInt(admForm.getAdmApplnId()));
		admForm.setCerticateCoursesPrint(toList);
		if (admForm.getPageNum()=="1" || admForm.getPageNum().equalsIgnoreCase("1")) {
			return mapping.findForward("onlineAnnexure");
		}
		else if (admForm.getPageNum()=="2" || admForm.getPageNum().equalsIgnoreCase("2")) {
			return mapping.findForward("onlineMembership");
		}
		else if (admForm.getPageNum()=="3" || admForm.getPageNum().equalsIgnoreCase("3")) {
			return mapping.findForward("onlineBioDataForm");
		}
		else if (admForm.getPageNum()=="4" || admForm.getPageNum().equalsIgnoreCase("4")) {
			return mapping.findForward("onlineAPPAdmission");
		}
		return mapping.findForward("onlineAPPAdmission");
	}
	public ActionForward submitAddMorePreferences(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		List<CertificateCourseTO> prefList=admForm.getPrefList();
		int count=1;
		
		List<CertificateCourseTO> current=admForm.getPrefList();
		int dup=0;
		for (CertificateCourseTO certificateCourseTO : current) {
			count++;
			if (certificateCourseTO.getId()==admForm.getCerticateId()) {
				dup=1;
			}
		}
		if (dup==0) {
		for (Map.Entry<Integer,String> entry : admForm.getCerticateCourses().entrySet()){
			CertificateCourseTO to=new CertificateCourseTO();
			if (entry.getKey()==admForm.getCerticateId()) {
				to.setId(admForm.getCerticateId());
				to.setCourseName(entry.getValue());
				prefList.add(to);
			}
           /* System.out.println("Key = " + entry.getKey() +
                             ", Value = " + entry.getValue());*/
		}
		}
		admForm.setPrefList(prefList);
		admForm.setPreCount(count);
		System.out.println(count);
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
		
	}
	public ActionForward removePreferences(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		List<CertificateCourseTO> prefList=admForm.getPrefList();
		if (!prefList.isEmpty()) {
			 int indexOfLastElement = prefList.size() - 1;
			 prefList.remove(indexOfLastElement);
			admForm.setPrefList(prefList);
			admForm.setPreCount(admForm.getPreCount()-1);
		}
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
		
	}
	
	public ActionForward submitcourse(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		List<CertificateCourseTO> prefList=admForm.getPrefList();
		boolean result=AdmissionStatusHandler.getInstance().saveCertificateCourses(admForm);
		admForm.setCertificationCourseDone(true);
		
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
		
	}
}
