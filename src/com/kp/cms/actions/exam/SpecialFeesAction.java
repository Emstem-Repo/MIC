package com.kp.cms.actions.exam;

import java.util.HashMap;
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
import com.kp.cms.forms.exam.SpecialFeesForm;
import com.kp.cms.forms.exam.SupplementaryFeesForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.SpecialFeesHandler;
import com.kp.cms.handlers.exam.SupplementaryFeesHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.SpecialFeesTO;

public class SpecialFeesAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(SpecialFeesAction.class);
	
	
	public ActionForward initSpecialFees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		SpecialFeesForm specialFeesForm = (SpecialFeesForm)form;
		specialFeesForm.resetFields();
		setRequiredDataForForm(specialFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);
	}


	private void setRequiredDataForForm(SpecialFeesForm specialFeesForm,HttpServletRequest request) throws Exception {
		List<ProgramTypeTO> programTypeList=ProgramTypeHandler.getInstance().getProgramType();
		if(programTypeList != null){
			specialFeesForm.setProgramTypeList(programTypeList);
		}
		Map<Integer,String> classMap = new HashMap<Integer,String>();
		if(specialFeesForm.getProgramTypeId()!=null && !specialFeesForm.getProgramTypeId().isEmpty()){
			classMap = CommonAjaxHandler.getInstance().getClassesByProgramTypeAndAcademicYear(Integer.parseInt(specialFeesForm.getProgramTypeId()),specialFeesForm.getDeanaryName());
			request.setAttribute("coursesMap",classMap);
			specialFeesForm.setClassMap(classMap);
		}
		List<SpecialFeesTO> toList = SpecialFeesHandler.getInstance().getActiveList();
		specialFeesForm.setRegularExamToList(toList);
	}
	
	public ActionForward addOrUpdatePublishSpecial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SpecialFeesForm specialFeesForm = (SpecialFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = specialFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, specialFeesForm);
			isPublishSupAppAdded =SpecialFeesHandler.getInstance().addOrUpdateRegularFee(specialFeesForm);
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDataForForm(specialFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDataForForm(specialFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				specialFeesForm.setErrorMessage(msg);
				specialFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				if(specialFeesForm.getMode().equalsIgnoreCase("add")){
					ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess","Special Fees");
					messages.add("messages", message);
				}
				else{
					ActionMessage message = new ActionMessage("knowledgepro.admin.updatesuccess","Special Fees");
					messages.add("messages", message);
				}
				saveMessages(request, messages);
				specialFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Special Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDataForForm(specialFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);
	}
	
	public ActionForward deleteOrReactivatePublishRegular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		SpecialFeesForm specialFeesForm = (SpecialFeesForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = specialFeesForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, specialFeesForm);
			isPublishSupAppAdded =SpecialFeesHandler.getInstance().deleteOrReactivateRegular(specialFeesForm.getId(),specialFeesForm.getMode(),specialFeesForm.getUserId());
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDataForForm(specialFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.regular.fees.exists"));
					saveErrors(request, errors);
					setRequiredDataForForm(specialFeesForm,request);
					return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				specialFeesForm.setErrorMessage(msg);
				specialFeesForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				ActionMessage message = new ActionMessage("knowledgepro.admin.deletesuccess","Special Fees");
				messages.add("messages", message);
				saveMessages(request, messages);
				specialFeesForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Special Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDataForForm(specialFeesForm,request);
		return mapping.findForward(CMSConstants.INIT_SPECIAL_FEES);
	}
}
