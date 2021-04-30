package com.kp.cms.actions.admission;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.handlers.admin.AdmittedThroughHandler;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admission.AdmissionFormHandler;
import com.kp.cms.handlers.admission.AdmissionStatusHandler;
import com.kp.cms.to.admin.AdmittedThroughTO;
import com.kp.cms.to.admin.ApplnDocTO;
import com.kp.cms.to.admin.OrganizationTO;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.AdmissionStatusTO;
import com.kp.cms.transactions.ajax.ICommonAjax;
import com.kp.cms.transactionsimpl.ajax.CommonAjaxImpl;
import com.kp.cms.utilities.CommonUtil;

public class AllotmentMemoAction extends BaseDispatchAction{
	private static final Log log = LogFactory.getLog(AdmissionStatusAction.class);
	private static final String DATE = "MM/dd/yyyy"; 
	private static final String FROM_DATEFORMAT="dd/MM/yyyy";
	private static final String SQL_DATEFORMAT="dd-MMM-yyyy";
	
	
	public ActionForward initViewMemo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ){
		log.info("Entered into initAdmissionStatus of AdmissionStatusAction");
		ICommonAjax txn=new CommonAjaxImpl();
		AdmissionStatusForm admissionStatusForm=(AdmissionStatusForm)form;		
		try {
			admissionStatusForm.clear();
			admissionStatusForm.clearadmissionStatusTO();
			admissionStatusForm.clearstatusTO();	
			Set<Integer> set=new HashSet<Integer>();
			set.add(0);
			admissionStatusForm.setDeptMap(txn.getCourseByProgramId(set));
			admissionStatusForm.setCategoryMap(txn.getSubReligion());
		} catch (Exception e) {
			log.error("Error occured at initAdmissionStatus of Admission Status Action",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into initAdmissionStatus of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
	}
	@SuppressWarnings("unchecked")
	public ActionForward getAdmissionStatus(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("Inside of getAdmissionStatus of AdmissionStatusAction");
		AdmissionStatusForm admissionStatusForm = (AdmissionStatusForm) form;
		// ActionErrors errors = admissionStatusForm.validate(mapping, request);
		 ActionErrors errors=new ActionErrors();
		 ICommonAjax txn=new CommonAjaxImpl();
		
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
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_APPNO_INVALID));
					saveErrors(request, errors);
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
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
					return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);			
				}
					
				/**
				 * Iterating the list of data getting based on the application no. entered from UI 
				 */
				
				Iterator<AdmissionStatusTO> it=AdmAppln.iterator();
				while (it.hasNext()) {
				AdmissionStatusTO admissionStatusTO = (AdmissionStatusTO) it.next();

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
					return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
				}
				if (admissionStatusTO.getDateOfBirth()==null || admissionStatusTO.getPersonalDataId()==0) {
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.ADMISSION_ADMISSIONSTATUS_NULL));
					saveErrors(request, errors);
					admissionStatusForm.clear();
					admissionStatusForm.clearadmissionStatusTO();
					admissionStatusForm.clearstatusTO();
					return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
				}
			
				/**
				 * Checks whether the date of birth retrieved from DB (if
				 * correct application no is entered) is same with the date of
				 * birth entered by user or not. If same then adding to formbean and displays the admission status in UI
				 */
				if(admissionStatusTO.getApplicationNo()!=0 && admissionStatusTO.getIsSelected()!=null && admissionStatusTO.getDateOfBirth() !=null)
				{     
			        //String uiDob=admissionStatusForm.getDateOfBirth();
			        String uiDob=CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), CMSConstants.SOURCE_DATE,CMSConstants.DEST_DATE);
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
						return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
					}
					
					//vibin for status change
					if(admissionStatusTO.isAllotmentflag()){
						
					}
					
					//
					
					/**
					 * If the candidate is not selected for admission then check for the interview status and display the last interview round status in UI
					 */
					if(!admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION))
					{
					//Used to get the interview status of the application					
					AdmissionStatusTO admTO = AdmissionStatusHandler.getInstance().getInterviewResult(applicationNo, admissionStatusTO.getAppliedYear());
					admissionStatusForm.setStatusTO(admTO);
					//vibin
					admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
					
					admissionStatusForm.clear();
					Set<Integer> set=new HashSet<Integer>();
					set.add(0);
					admissionStatusForm.setDeptMap(txn.getCourseByProgramId(set));
					admissionStatusForm.setCategoryMap(txn.getSubReligion());
					return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);					
				}				
					if(admissionStatusForm.getApplicationNo()!=null){
						List interviewCardTOList = AdmissionStatusHandler.getInstance().getStudentsList(admissionStatusForm.getApplicationNo());
						if(!interviewCardTOList.isEmpty()){
							if(admissionStatusTO.getIsSelected()!= null){		
									if(admissionStatusTO.getIsSelected().equalsIgnoreCase(CMSConstants.SELECTED_FOR_ADMISSION)){
										admissionStatusTO.setIsInterviewSelected(CMSConstants.ADMISSION);
									}
							}				
						}
						else{
							admissionStatusTO.setIsInterviewSelected("false");						
						}
					}					
						admissionStatusForm.setAppliedYear(admissionStatusTO.getAppliedYear());
						admissionStatusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(admissionStatusTO.getDateOfBirth(), CMSConstants.SOURCE_DATE,CMSConstants.SOURCE_DATE));
						admissionStatusForm.setAdmissionStatusTO(admissionStatusTO);
						admissionStatusForm.clear();
						admissionStatusForm.clearstatusTO();
						return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
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
						return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
				}
				}
				}
			}
		} catch (Exception e) {
			log.error("Error occured at getAdmissionStatus of Admission StatusAction",e);
			String msg = super.handleApplicationException(e);
			admissionStatusForm.setErrorMessage(msg);
			admissionStatusForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		saveErrors(request, errors);
		log.info("Leaving from getAdmissionStatus of AdmissionStatusAction");
		return mapping.findForward(CMSConstants.INIT_MEMO_PAGE);
	}
	
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
						applicantDetails.getPersonalData().setDob(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getDob(), AllotmentMemoAction.SQL_DATEFORMAT,AllotmentMemoAction.FROM_DATEFORMAT));
					if(applicantDetails.getChallanDate()!=null )
						applicantDetails.setChallanDate(CommonUtil.ConvertStringToDateFormat(applicantDetails.getChallanDate(), AllotmentMemoAction.SQL_DATEFORMAT,AllotmentMemoAction.FROM_DATEFORMAT));
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
						applicantDetails.getPersonalData().setPassportValidity(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getPassportValidity(), AllotmentMemoAction.SQL_DATEFORMAT,AllotmentMemoAction.FROM_DATEFORMAT));
					
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
								/*if(session!=null){
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
								}*/
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
			AdmissionStatusTO to = admForm.getAdmissionStatusTO();
			AdmissionStatusTO to2 = admForm.getAdmissionStatusTO().getChanceMemoMap().get(admForm.getChanceCourseId());
     	       to.setChanceIndexmark(to2.getChanceIndexmark());
		       to.setChanceCurrentcourse(to2.getChanceCurrentcourse());
		       to.setChanceCurrentcourseid(to2.getChanceCurrentcourseid());
		      /* if(to2.getDateTime()!=null)
		    	   to.setDateTime(to2.getDateTime());*/
		       if(to2.getChanceGeneralFee()!=null)
		    	   to.setChanceGeneralFee(to2.getChanceGeneralFee());
		       if(to2.getChanceCasteFee()!=null)
		    	   to.setChanceCasteFee(to2.getChanceCasteFee());
               if(to2.getCommunityDateTime()!=null)
            	   to.setCommunityDateTime(to2.getCommunityDateTime());
		       if(to2.getCasteRank()!=null)
		    	   to.setCasteRank(to2.getCasteRank());
		       if(to2.getGenRank()!=null)
		    	   to.setGenRank(to2.getGenRank());
		       to.setChancePref(to2.getChancePref());
		       to.setChanceAllotmentno(to2.getChanceAllotmentno());
		       if(to2.getChanceCategory()!=null)
		    	   to.setChanceCategory(to2.getChanceCategory());
		       to.setChanceflag(true);
		       to.setChanceAlmntcaste(to2.isChanceAlmntcaste());
		       to.setChanceAlmntgeneral(to2.isChanceAlmntgeneral());
		       to.setChanceAlmntCommunity(to2.isChanceAlmntCommunity());
		       admForm.setAdmissionStatusTO(to);
		       request.setAttribute("chanceCourseId", admForm.getChanceCourseId());
			return mapping.findForward(CMSConstants.VIEW_CHANCE_MEMO);
		}
		if(admForm.isMemo()){
			return mapping.findForward("viewallotmentmemo");
		}
		return mapping.findForward(CMSConstants.VIEW_APPLICATION);
	}
}
