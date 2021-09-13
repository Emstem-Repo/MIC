package com.kp.cms.actions.exam;

import java.util.HashMap;
import java.util.Iterator;
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
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.employee.PayScaleDetailsForm;
import com.kp.cms.forms.exam.ExamRevaluationFeeForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.employee.PayScaleDetailsHandler;
import com.kp.cms.handlers.exam.ExamRevaluationFeeHandler;
import com.kp.cms.handlers.exam.SupplementaryFeesHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.ExamRevaluationFeeTO;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.RevaluationExamFeesTo;

public class ExamRevaluationFeeAction extends BaseDispatchAction{
private static final Log log = LogFactory.getLog(ExamRevaluationFeeAction.class);
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initExamRevaluationFee(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExamRevaluationFeeForm examRevaluationFeeForm=(ExamRevaluationFeeForm)form;
		examRevaluationFeeForm.resetFields();
		setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
		return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
	}
	
	
	private void setRequiredDatatoFormRevaluation( ExamRevaluationFeeForm examRevaluationFeeForm,HttpServletRequest request) throws Exception {

		List<ProgramTypeTO> programTypeList=ProgramTypeHandler.getInstance().getProgramType();
		if(programTypeList != null){
			examRevaluationFeeForm.setProgramTypeList(programTypeList);
		}
		Map<Integer,String> courseMap = new HashMap<Integer,String>();
		if(examRevaluationFeeForm.getProgramTypeId()!=null && !examRevaluationFeeForm.getProgramTypeId().isEmpty()){
			List<KeyValueTO> list = CommonAjaxHandler.getInstance().getCoursesByProgramTypes1(examRevaluationFeeForm.getProgramTypeId());
			if(list!=null && !list.isEmpty()){
				Iterator<KeyValueTO> itr=list.iterator();
				while (itr.hasNext()) {
					KeyValueTO keyValueTO = (KeyValueTO) itr.next();
					courseMap.put(keyValueTO.getId(),keyValueTO.getDisplay());
				}
			}
			request.setAttribute("coursesMap",courseMap);
			examRevaluationFeeForm.setCourseMap(courseMap);
		}
		List<ExamRevaluationFeeTO> toList=ExamRevaluationFeeHandler.getInstance().getActiveListRevaluation();
		examRevaluationFeeForm.setRevaluationExamToList(toList);
	}
	
	public ActionForward addOrUpdatePublishRevaluation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		ExamRevaluationFeeForm examRevaluationFeeForm = (ExamRevaluationFeeForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = examRevaluationFeeForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, examRevaluationFeeForm);
			isPublishSupAppAdded =ExamRevaluationFeeHandler.getInstance().addOrUpdateRevaluationFee(examRevaluationFeeForm);
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.revaluation.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.revaluation.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				examRevaluationFeeForm.setErrorMessage(msg);
				examRevaluationFeeForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				if(examRevaluationFeeForm.getMode().equalsIgnoreCase("add")){
					ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess","Revaluation Fees");
					messages.add("messages", message);
				}
				else{
					ActionMessage message = new ActionMessage("knowledgepro.admin.updatesuccess","Revaluation Fees");
					messages.add("messages", message);
				}
				saveMessages(request, messages);
				examRevaluationFeeForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Revaluation Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
		return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
	}
	public ActionForward deleteOrReactivatePublishRevaluation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Entered into initPublishSupplementary");
		ExamRevaluationFeeForm examRevaluationFeeForm = (ExamRevaluationFeeForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = examRevaluationFeeForm.validate(mapping, request);

		boolean isPublishSupAppAdded = false;
		if (errors.isEmpty()) {
			try{
			setUserId(request, examRevaluationFeeForm);
			isPublishSupAppAdded =ExamRevaluationFeeHandler.getInstance().deleteOrReactivateRevaluation(examRevaluationFeeForm.getId(),examRevaluationFeeForm.getMode(),examRevaluationFeeForm.getUserId());
			}catch (Exception e) {
				if(e instanceof DuplicateException){
					errors.add("error", new ActionError("knowledgepro.admin.revaluation.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);	
				}
				if(e instanceof ReActivateException){
					errors.add("error", new ActionError("knowledgepro.admin.revaluation.fees.exists"));
					saveErrors(request, errors);
					setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);	
				}
				log.error("Error occured in caste Entry Action", e);
				String msg = super.handleApplicationException(e);
				examRevaluationFeeForm.setErrorMessage(msg);
				examRevaluationFeeForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
				
			}
			if (isPublishSupAppAdded) {
				ActionMessage message = new ActionMessage("knowledgepro.admin.deletesuccess","Revaluation Fees");
				messages.add("messages", message);
				saveMessages(request, messages);
				examRevaluationFeeForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("kknowledgepro.admin.addfailure","Revaluation Fees"));
				saveErrors(request, errors);
			}
		} else {
			saveErrors(request, errors);
		}
		setRequiredDatatoFormRevaluation(examRevaluationFeeForm,request);
		return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward editRevaluationFee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ExamRevaluationFeeForm examRevaluationFeeForm=(ExamRevaluationFeeForm)form;
		log.debug("Entering editRevaluationFee ");
		try {
			ExamRevaluationFeeHandler.getInstance().editRevaluationFee(examRevaluationFeeForm);
			request.setAttribute("operation", "edit");
			//request.setAttribute("Update", "Update");// setting update attribute
			log.debug("Leaving editRevaluationFee ");
		} catch (Exception e) {
			log.error("error in editing RevaluationFee...", e);
			String msg = super.handleApplicationException(e);
			examRevaluationFeeForm.setErrorMessage(msg);
			examRevaluationFeeForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateRevaluationFee(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("Enter: updateRevaluationFee ");
		ExamRevaluationFeeForm examRevaluationFeeForm=(ExamRevaluationFeeForm)form;
		HttpSession session=request.getSession();
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = examRevaluationFeeForm.validate(mapping, request);
		boolean isUpdated = false;
		String mode="update";
        if(errors.isEmpty()){
		try {
			// This condition works when reset button will click in update mode
			if (isCancelled(request)) {
				examRevaluationFeeForm.reset(mapping, request);
				String formName = mapping.getName();
				request.getSession().setAttribute(CMSConstants.FORMNAME,
						formName);
				ExamRevaluationFeeHandler.getInstance().editRevaluationFee(examRevaluationFeeForm);
				request.setAttribute("operation", "edit");
				return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
			}
			setUserId(request, examRevaluationFeeForm); 
			boolean isDuplicate=ExamRevaluationFeeHandler.getInstance().duplicateCheck(examRevaluationFeeForm,errors,session);
			if(!isDuplicate){
			isUpdated = ExamRevaluationFeeHandler.getInstance().updateRevaluationFee(examRevaluationFeeForm, mode);
			if (isUpdated) {
				ActionMessage message = new ActionMessage(
						"knowledgepro.exam.revaluationfee.update.success");
				messages.add("messages", message);
				saveMessages(request, messages);
				//employeeResumeForm.reset(mapping, request);
				examRevaluationFeeForm.resetFields();
			} else {
				errors.add("error", new ActionError(
						"knowledgepro.exam.revaluationfee.update.failed"));
				//saveErrors(request, errors);
				addErrors(request, errors);
				examRevaluationFeeForm.resetFields();
			}}else{
				addErrors(request, errors);
			}
		} catch (Exception e) {
			log.error("Error occured in edit RevaluationFee", e);
			String msg = super.handleApplicationException(e);
			examRevaluationFeeForm.setErrorMessage(msg);
			examRevaluationFeeForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}}else{
			saveErrors(request, errors);
			//setDataToForm(examRevaluationFeeForm);
			request.setAttribute("operation", "edit");
			return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);
		}
       // setDataToForm(examRevaluationFeeForm);
		log.debug("Exit: action class updateRevaluationFee");
		return mapping.findForward(CMSConstants.INIT_REVALUATION_FEE);

	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
}
