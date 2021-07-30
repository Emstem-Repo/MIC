package com.kp.cms.actions.admission;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

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
import org.apache.struts.actions.DownloadAction.StreamInfo;
import org.apache.struts.upload.FormFile;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.ApplicantLateralDetails;
import com.kp.cms.bo.admin.ApplicantTransferDetails;
import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.CandidatePreference;
import com.kp.cms.bo.admin.DocType;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.bo.admin.Program;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.SelectionDateNotAvailableException;
import com.kp.cms.forms.admission.AdmissionFormForm;
import com.kp.cms.handlers.admin.AdmittedThroughHandler;
import com.kp.cms.handlers.admin.CandidateMarkTO;
import com.kp.cms.handlers.admin.CasteHandler;
import com.kp.cms.handlers.admin.CountryHandler;
import com.kp.cms.handlers.admin.CourseHandler;
import com.kp.cms.handlers.admin.EntranceDetailsHandler;
import com.kp.cms.handlers.admin.GuidelinesEntryHandler;
import com.kp.cms.handlers.admin.LanguageHandler;
import com.kp.cms.handlers.admin.MaintenanceAlertHandler;
import com.kp.cms.handlers.admin.OccupationTransactionHandler;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admin.ReligionHandler;
import com.kp.cms.handlers.admin.StateHandler;
import com.kp.cms.handlers.admin.SubjectGroupHandler;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.handlers.admin.UniversityHandler;
import com.kp.cms.handlers.admission.AdmissionFormHandler;
import com.kp.cms.handlers.admission.OfflineDetailsHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.ExamGenHandler;
import com.kp.cms.helpers.admission.AdmissionFormHelper;
import com.kp.cms.to.admin.AdmSubjectMarkForRankTO;
import com.kp.cms.to.admin.AdmittedThroughTO;
import com.kp.cms.to.admin.ApplicantLateralDetailsTO;
import com.kp.cms.to.admin.ApplicantMarkDetailsTO;
import com.kp.cms.to.admin.ApplicantTransferDetailsTO;
import com.kp.cms.to.admin.ApplicantWorkExperienceTO;
import com.kp.cms.to.admin.ApplnDocTO;
import com.kp.cms.to.admin.CandidatePreferenceTO;
import com.kp.cms.to.admin.CasteTO;
import com.kp.cms.to.admin.CollegeTO;
import com.kp.cms.to.admin.CountryTO;
import com.kp.cms.to.admin.CoursePrerequisiteTO;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admin.CurrencyTO;
import com.kp.cms.to.admin.DioceseTo;
import com.kp.cms.to.admin.EdnQualificationTO;
import com.kp.cms.to.admin.EntrancedetailsTO;
import com.kp.cms.to.admin.ExamCenterTO;
import com.kp.cms.to.admin.GuideLinesCheckListTO;
import com.kp.cms.to.admin.IncomeTO;
import com.kp.cms.to.admin.OrganizationTO;
import com.kp.cms.to.admin.ParishTo;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.ReligionSectionTO;
import com.kp.cms.to.admin.ReligionTO;
import com.kp.cms.to.admin.SingleFieldMasterTO;
import com.kp.cms.to.admin.StateTO;
import com.kp.cms.to.admin.SubjectGroupTO;
import com.kp.cms.to.admin.TermsConditionChecklistTO;
import com.kp.cms.to.admin.UGCoursesTO;
import com.kp.cms.to.admin.UniversityTO;
import com.kp.cms.to.admission.AddressTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.PersonalDataTO;
import com.kp.cms.to.admission.PreferenceTO;
import com.kp.cms.transactions.admin.IProgramTransaction;
import com.kp.cms.transactions.admin.ISubReligionTransaction;
import com.kp.cms.transactions.admin.ITemplatePassword;
import com.kp.cms.transactions.admission.IAdmissionFormTransaction;
import com.kp.cms.transactionsimpl.admin.DioceseTransactionImpl;
import com.kp.cms.transactionsimpl.admin.ParishTransactionImpl;
import com.kp.cms.transactionsimpl.admin.ProgramTransactionImpl;
import com.kp.cms.transactionsimpl.admin.SubReligionTransactionImpl;
import com.kp.cms.transactionsimpl.admin.TemplateImpl;
import com.kp.cms.transactionsimpl.admission.AdmissionFormTransactionImpl;
import com.kp.cms.utilities.CSVUpdater;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.SubjectGroupDetailsComparator;
import com.lowagie.tools.plugins.Encrypt;
/**
 * 
 * 
 * Class implemented to handle admission and application(both off line and online) related activities.
 * 
 */

@SuppressWarnings("deprecation")
public class AdmissionFormOnlineAction extends BaseDispatchAction {
	
	private static final Log log = LogFactory.getLog(AdmissionFormOnlineAction.class);
	private static final String OTHER="other";
	private static final String TO_DATEFORMAT="MM/dd/yyyy";
	private static final String FROM_DATEFORMAT="dd/MM/yyyy";
	private static final String SQL_DATEFORMAT="dd-MMM-yyyy";
	private static final String COUNTID="countID";
	private static final String PHOTOBYTES="PhotoBytes";
	private static Map<Integer,String> prefNameMap=null;
	private static SubjectGroupDetailsComparator comparator=new SubjectGroupDetailsComparator();
	
	/**
	 * This method is to proceed to view details for admission approval
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward initAdmissionFormOnline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionFormForm admForm = (AdmissionFormForm) form;
		//clean up
		admForm.setApplicationNumber(null);
		admForm.setApplicationYear(null);
		admForm.setApplicantDetails(null);
		return mapping.findForward(CMSConstants.INIT_FORMS_ONLINE);
	}
	
	
	/**
	 * This method is to proceed to view details for admission approval
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward getStudentData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered submitAdmissionApprovalInit..");
		AdmissionFormForm admForm = (AdmissionFormForm) form;
		try{
			 ActionErrors errors = admForm.validate(mapping, request);
			
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				
				return mapping.findForward(CMSConstants.INIT_FORMS_ONLINE);
			}
				String applicationNumber = admForm.getApplicationNumber().trim();
				int applicationYear = Integer.parseInt(admForm.getApplicationYear());
				AdmApplnTO applicantDetails = AdmissionFormHandler.getInstance().getApplicantDetails(applicationNumber, applicationYear,false);
				
				if( applicantDetails == null){
					
					ActionMessages messages = new ActionMessages();
					ActionMessage message = null;
						message = new ActionMessage(CMSConstants.INIT_FORMS_ONLINE);
						messages.add("messages", message);
						saveMessages(request, messages);
						
						return mapping.findForward(CMSConstants.INIT_FORMS_ONLINE);
					
				} else {
					if(applicantDetails.getPersonalData()!=null && applicantDetails.getPersonalData().getDob()!=null )
						applicantDetails.getPersonalData().setDob(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getDob(), AdmissionFormOnlineAction.SQL_DATEFORMAT,AdmissionFormOnlineAction.FROM_DATEFORMAT));
					if(applicantDetails.getChallanDate()!=null )
						applicantDetails.setChallanDate(CommonUtil.ConvertStringToDateFormat(applicantDetails.getChallanDate(), AdmissionFormOnlineAction.SQL_DATEFORMAT,AdmissionFormOnlineAction.FROM_DATEFORMAT));
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
					/*ProgramTypeHandler programtypehandler = ProgramTypeHandler.getInstance();
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
						*/
						
						//checkExtradetailsDisplay(admForm);
						//checkLateralTransferDisplay(admForm);
						// Admitted Through
//						List<AdmittedThroughTO> admittedList=AdmittedThroughHandler.getInstance().getAdmittedThrough();
//						admForm.setAdmittedThroughList(admittedList);
//						OrganizationHandler orgHandler= OrganizationHandler.getInstance();
////						if(admForm.isDisplayAdditionalInfo())
////						{
//							
//							List<OrganizationTO> tos=orgHandler.getOrganizationDetails();
//							if(tos!=null && !tos.isEmpty())
//							{
//								OrganizationTO orgTO=tos.get(0);
//								admForm.setOrganizationName(orgTO.getOrganizationName());
//								admForm.setNeedApproval(orgTO.isNeedApproval());
//							}
////						}
//						
//							Properties prop = new Properties();
//							try {
//								InputStream inputStream = CommonUtil.class.getClassLoader()
//										.getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
//								prop.load(inputStream);
//							} 
//							catch (IOException e) {
//								log.error("Error occured at convertToExcel of studentDetailsReportHelper ",e);
//								throw new IOException(e);
//							}
//							String fileName=prop.getProperty(CMSConstants.PRG_TYPE);
//							if(admForm.getApplicantDetails().getSelectedCourse().getProgramTypeId()==Integer.parseInt(fileName)){
//								admForm.setViewextradetails(true);
//							}
//					
//					if(applicantDetails.getPersonalData()!=null && applicantDetails.getPersonalData().getPassportValidity()!=null && !StringUtils.isEmpty(applicantDetails.getPersonalData().getPassportValidity()) )
//						applicantDetails.getPersonalData().setPassportValidity(CommonUtil.ConvertStringToDateFormat(applicantDetails.getPersonalData().getPassportValidity(), AdmissionFormOnlineAction.SQL_DATEFORMAT,AdmissionFormOnlineAction.FROM_DATEFORMAT));
//					
//					// set photo to session
//					if(applicantDetails.getEditDocuments()!=null){
//						Iterator<ApplnDocTO> docItr=applicantDetails.getEditDocuments().iterator();
//						while (docItr.hasNext()) {
//							ApplnDocTO docTO = (ApplnDocTO) docItr.next();
//							if(docTO.getSubmitDate()!=null && !StringUtils.isEmpty(docTO.getSubmitDate()))
//							{
//								admForm.setSubmitDate(docTO.getSubmitDate());
//							}
//							if(docTO.getDocName()!=null && docTO.getDocName().equalsIgnoreCase("Photo")){
//								HttpSession session= request.getSession(false);
//								if(session!=null){
//									session.setAttribute(AdmissionFormOnlineAction.PHOTOBYTES, docTO.getPhotoBytes());
//								}
//							}
//						}
//					}
				}
			
			}catch (Exception e){
				log.error("Error in  getApplicantDetails application form approval page...",e);
				if (e instanceof ApplicationException) {
					String msg = super.handleApplicationException(e);
					admForm.setErrorMessage(msg);
					admForm.setErrorStack(e.getMessage());
					return mapping.findForward(CMSConstants.ERROR_PAGE);
				}else {
					throw e;
				}
			}
		log.info("exit detailAdmissionApproval..");
		return mapping.findForward(CMSConstants.INIT_FORMS_ONLINE);
	}
	public ActionForward printAdmissionFormOnline(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionFormForm admForm = (AdmissionFormForm) form;
		String applicationNumber = admForm.getApplicationNumber().trim();
		int applicationYear = Integer.parseInt(admForm.getApplicationYear());
		AdmApplnTO applicantDetails = AdmissionFormHandler.getInstance().getApplicantDetails(applicationNumber, applicationYear,false);
		admForm.setApplicantDetails(applicantDetails);
		return mapping.findForward("onlineBioDataForm");
	}
	
	/**
	 * This method is to proceed to view details for admission approval
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
// ===================================== class close




	
	
	

}
