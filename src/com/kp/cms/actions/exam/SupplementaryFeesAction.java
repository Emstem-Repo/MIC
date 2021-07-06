package com.kp.cms.actions.exam;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.admin.CourseForm;
import com.kp.cms.forms.exam.SupplementaryFeesForm;
import com.kp.cms.handlers.admin.ProgramHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.SupplementaryFeesHandler;
import com.kp.cms.to.admin.ProgramTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SupplementaryFeesTo;

public class SupplementaryFeesAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(SupplementaryFeesAction.class);
	
	/**
	 * Method to set the required data to the form to display it in ScoreSheet.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSupplementaryFees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered into initSupplementaryFees");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		supplementaryFeesForm.resetFields();
		setRequiredDatatoForm(supplementaryFeesForm,request);
		log.info("Exit from initSupplementaryFees");
		
		return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);
	}

	/**
	 * @param supplementaryFeesForm
	 * @throws Exception
	 */
	private void setRequiredDatatoForm( SupplementaryFeesForm supplementaryFeesForm,HttpServletRequest request) throws Exception {

		List<ProgramTypeTO> programTypeList=ProgramTypeHandler.getInstance().getProgramType();
		if(programTypeList != null){
			supplementaryFeesForm.setProgramTypeList(programTypeList);
		}
		Map<Integer,String> courseMap = new HashMap<Integer,String>();
		if(supplementaryFeesForm.getProgramTypeId()!=null && !supplementaryFeesForm.getProgramTypeId().isEmpty()){
			List<KeyValueTO> list = CommonAjaxHandler.getInstance().getCoursesByProgramTypes1(supplementaryFeesForm.getProgramTypeId());
			if(list!=null && !list.isEmpty()){
				Iterator<KeyValueTO> itr=list.iterator();
				while (itr.hasNext()) {
					KeyValueTO keyValueTO = (KeyValueTO) itr.next();
					courseMap.put(keyValueTO.getId(),keyValueTO.getDisplay());
				}
			}
			request.setAttribute("coursesMap",courseMap);
			supplementaryFeesForm.setCourseMap(courseMap);
		}
	
		
		//List<ProgramTO> programList=ProgramHandler.getInstance().getProgram();
		//supplementaryFeesForm.setProgramList(programList);
		List<SupplementaryFeesTo> toList=SupplementaryFeesHandler.getInstance().getActiveList();
		supplementaryFeesForm.setToList(toList);
	}
	
	
	/**
	 * Performs the add Caste action.
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return The forward to which control should be transferred.
     * @throws Exception if an exception occurs
	 */
	public ActionForward addOrUpdatePublish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = supplementaryFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, supplementaryFeesForm);
			if (supplementaryFeesForm.getMode()=="add" || supplementaryFeesForm.getMode().equalsIgnoreCase("add")) {
				isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().addOrUpdate(supplementaryFeesForm);
			}else if(supplementaryFeesForm.getMode()=="update" || supplementaryFeesForm.getMode().equalsIgnoreCase("update")){
				isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().UpdateFees(supplementaryFeesForm);
			}
			
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.supplementaryFees.program.exists"));
					saveErrors(request, errors);
					setRequiredDatatoForm(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.supplementaryFees.program.exists"));
					saveErrors(request, errors);
					setRequiredDatatoForm(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				supplementaryFeesForm.setErrorMessage(msg);
				supplementaryFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				if(supplementaryFeesForm.getMode().equalsIgnoreCase("add")){
					ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess","Supplementary Fees");
					messages.add("messages", message);
				}
				else{
					ActionMessage message = new ActionMessage("knowledgepro.admin.updatesuccess","Supplementary Fees");
					messages.add("messages", message);
				}
				saveMessages(request, messages);
				supplementaryFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Supplementary Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoForm(supplementaryFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);
	}
	
	/**
	 * Performs the add Caste action.
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return The forward to which control should be transferred.
     * @throws Exception if an exception occurs
	 */
	public ActionForward deleteOrReactivatePublish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = supplementaryFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, supplementaryFeesForm);
			isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().deleteOrReactivate(supplementaryFeesForm.getId(),supplementaryFeesForm.getMode(),supplementaryFeesForm.getUserId());
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.supplementaryFees.program.exists"));
					saveErrors(request, errors);
					setRequiredDatatoForm(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.supplementaryFees.program.exists"));
					saveErrors(request, errors);
					setRequiredDatatoForm(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				supplementaryFeesForm.setErrorMessage(msg);
				supplementaryFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				ActionMessage message = new ActionMessage("knowledgepro.admin.deletesuccess","Supplementary Fees");
				messages.add("messages", message);
				saveMessages(request, messages);
				supplementaryFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Supplementary Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoForm(supplementaryFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);
	}

	// Exam Regular Application Fees 
	public ActionForward initRegularFees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered into initSupplementaryFees");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		supplementaryFeesForm.resetFields();
		setRequiredDatatoFormRegular(supplementaryFeesForm,request);
		log.info("Exit from initSupplementaryFees");
		
		return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);
	}
	private void setRequiredDatatoFormRegular( SupplementaryFeesForm supplementaryFeesForm,HttpServletRequest request) throws Exception {

		List<ProgramTypeTO> programTypeList=ProgramTypeHandler.getInstance().getProgramType();
		if(programTypeList != null){
			supplementaryFeesForm.setProgramTypeList(programTypeList);
		}
		Map<Integer,String> classMap = new HashMap<Integer,String>();
		if(supplementaryFeesForm.getProgramTypeId()!=null && !supplementaryFeesForm.getProgramTypeId().isEmpty()){
			classMap = CommonAjaxHandler.getInstance().getClassesByProgramTypeAndAcademicYear(Integer.parseInt(supplementaryFeesForm.getProgramTypeId()),supplementaryFeesForm.getDeanaryName());
			request.setAttribute("coursesMap",classMap);
			supplementaryFeesForm.setClassMap(classMap);
		}
		List<RegularExamFeesTo> toList=SupplementaryFeesHandler.getInstance().getActiveListRegular();
		supplementaryFeesForm.setRegularExamToList(toList);
	}
	
	public ActionForward addOrUpdatePublishRegular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = supplementaryFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, supplementaryFeesForm);
			if (supplementaryFeesForm.getMode()=="add" || supplementaryFeesForm.getMode().equalsIgnoreCase("add")) {
				isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().addOrUpdateRegularFee(supplementaryFeesForm);
			}else if(supplementaryFeesForm.getMode()=="update" || supplementaryFeesForm.getMode().equalsIgnoreCase("update")){
				isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().UpdateRegularFee(supplementaryFeesForm);
			}
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRegular(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRegular(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				supplementaryFeesForm.setErrorMessage(msg);
				supplementaryFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				if(supplementaryFeesForm.getMode().equalsIgnoreCase("add")){
					ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess","Regular Fees");
					messages.add("messages", message);
				}
				else{
					ActionMessage message = new ActionMessage("knowledgepro.admin.updatesuccess","Regular Fees");
					messages.add("messages", message);
				}
				saveMessages(request, messages);
				supplementaryFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Regular Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoFormRegular(supplementaryFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);
	}
	public ActionForward deleteOrReactivatePublishRegular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = supplementaryFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, supplementaryFeesForm);
			isPublishSupAppAdded =SupplementaryFeesHandler.getInstance().deleteOrReactivateRegular(supplementaryFeesForm.getId(),supplementaryFeesForm.getMode(),supplementaryFeesForm.getUserId());
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRegular(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRegular(supplementaryFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				supplementaryFeesForm.setErrorMessage(msg);
				supplementaryFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				ActionMessage message = new ActionMessage("knowledgepro.admin.deletesuccess","Regular Fees");
				messages.add("messages", message);
				saveMessages(request, messages);
				supplementaryFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Regular Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoFormRegular(supplementaryFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);
	}
	
	public ActionForward editOrUpdatePublish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		assignDataToForm(request, supplementaryFeesForm); 
		return mapping.findForward(CMSConstants.INIT_SUPPLEMENTARY_FEES);
	}
	

	private void assignDataToForm(HttpServletRequest request, SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		
		request.setAttribute("operation", "edit");
		SupplementaryFeesHandler.getInstance().setEditData(supplementaryFeesForm.getId(),supplementaryFeesForm);
		Map<Integer,String> courseMap = new HashMap<Integer,String>();
		if(supplementaryFeesForm.getProgramTypeId()!=null && !supplementaryFeesForm.getProgramTypeId().isEmpty()){
			List<KeyValueTO> list = CommonAjaxHandler.getInstance().getCoursesByProgramTypes1(supplementaryFeesForm.getProgramTypeId());
			if(list!=null && !list.isEmpty()){
				Iterator<KeyValueTO> itr=list.iterator();
				while (itr.hasNext()) {
					KeyValueTO keyValueTO = (KeyValueTO) itr.next();
					courseMap.put(keyValueTO.getId(),keyValueTO.getDisplay());
				}
			}
			request.setAttribute("coursesMap",courseMap);
			supplementaryFeesForm.setCourseMap(courseMap);
		}
		
	}
	
	public ActionForward editOrUpdateRegularPublish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SupplementaryFeesForm supplementaryFeesForm = (SupplementaryFeesForm) form;
		assigRegularDataToForm(request, supplementaryFeesForm); 
		return mapping.findForward(CMSConstants.INIT_REGULAR_FEES);
	}

	private void assigRegularDataToForm(HttpServletRequest request, SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		request.setAttribute("operation", "edit");
		Map<Integer,String> classMap = new HashMap<Integer,String>();
		SupplementaryFeesHandler.getInstance().setEditForRegularData(supplementaryFeesForm.getId(),supplementaryFeesForm);
		if(supplementaryFeesForm.getProgramTypeId()!=null && !supplementaryFeesForm.getProgramTypeId().isEmpty()){
			classMap = CommonAjaxHandler.getInstance().getClassesByProgramTypeId(Integer.parseInt(supplementaryFeesForm.getProgramTypeId()));
			request.setAttribute("coursesMap",classMap);
			supplementaryFeesForm.setClassMap(classMap);
		}
		
		
	}

}
