package com.kp.cms.actions.admission;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.Validate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admission.PublishForAllotmentForm;
import com.kp.cms.handlers.admin.ConvocationSessionHandler;
import com.kp.cms.handlers.admission.PublishForAllotmentHandler;
import com.kp.cms.to.admission.PublishForAllotmentTO;
import com.kp.cms.utilities.CommonUtil;

public class PublishForAllotmentAction extends BaseDispatchAction{
	
	
	public ActionForward initpublishAllotment(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		PublishForAllotmentForm publishForAllotmentForm = (PublishForAllotmentForm)form;
		try{
			setCourseMapToForm(publishForAllotmentForm);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.PUBLISH_ALLOTMENT_ENTRY);
	}

	private void setCourseMapToForm(PublishForAllotmentForm publishForAllotmentForm) throws Exception{
		Map<Integer, String> courseMap=ConvocationSessionHandler.getInstance().getCourseMap();
		publishForAllotmentForm.setCourseMap(courseMap);
		List<PublishForAllotmentTO> allotmentTOs = PublishForAllotmentHandler.getInstance().getToList();
		publishForAllotmentForm.setPublishForAllotmentTOs(allotmentTOs);
	}
	
	@SuppressWarnings("deprecation")
	public ActionForward addAllotmentDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		PublishForAllotmentForm publishForAllotmentForm=(PublishForAllotmentForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors= validateData(publishForAllotmentForm);
		setUserId(request, publishForAllotmentForm);
		try{
			validateForm(errors,publishForAllotmentForm);
			if(errors!=null && errors.isEmpty()){
				boolean isDuplicate=PublishForAllotmentHandler.getInstance().checkDuplicate(publishForAllotmentForm);
					if(!isDuplicate){
						boolean isAdded=PublishForAllotmentHandler.getInstance().addAllotmentDetails(publishForAllotmentForm);
						if(isAdded){
							ActionMessage message = new ActionError( "knowledgepro.admission.publish.allotment.success");
							messages.add("messages", message);
							saveMessages(request, messages);
							publishForAllotmentForm.reset(mapping, request);
							setCourseMapToForm(publishForAllotmentForm);
						}else{
							publishForAllotmentForm.reset(mapping, request);
							errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.failure"));
							saveErrors(request, errors);
						}
					}else{
						if(publishForAllotmentForm.getErrorMessage() != null && !publishForAllotmentForm.getErrorMessage().isEmpty()){
							errors .add( "error", new ActionError("knowledgepro.admission.publish.allotment.exists",publishForAllotmentForm.getErrorMessage()));
							saveErrors(request, errors);
						}else{
							errors .add( "error", new ActionError("knowledgepro.admin.convocation.session.already.exist"));
							saveErrors(request, errors);
						}
					}
				
			}else{
				saveErrors(request, errors);
			}
			
		}catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			publishForAllotmentForm.setErrorMessage(msg);
			publishForAllotmentForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.PUBLISH_ALLOTMENT_ENTRY);
	}

	private ActionErrors validateData(PublishForAllotmentForm publishForAllotmentForm) {
		Date startDate=null;
		Date endDate=null;
		ActionErrors errors = new ActionErrors();
		if ((publishForAllotmentForm.getFromDate() != null && publishForAllotmentForm
				.getFromDate().trim().length() > 0)) {
			startDate = new Date(publishForAllotmentForm.getFromDate());

		}
		if ((publishForAllotmentForm.getEndDate() != null && publishForAllotmentForm
				.getEndDate().trim().length() > 0)) {
			endDate = new Date(publishForAllotmentForm.getEndDate());
		}

		startDate = CommonUtil.ConvertStringToSQLDate(publishForAllotmentForm
				.getFromDate());
		endDate = CommonUtil.ConvertStringToSQLDate(publishForAllotmentForm
				.getEndDate());
		int flag  = CommonUtil.getDaysDiff(startDate, endDate);
		if (flag < 0) {
			errors.add("error", new ActionError( "knowledgepro.exam.publishHM.MarksCard.validDate"));

		}

		return errors;
	}

	@SuppressWarnings("deprecation")
	private void validateForm(ActionErrors errors, PublishForAllotmentForm publishForAllotmentForm) {
		if(publishForAllotmentForm.getFromDate() == null || publishForAllotmentForm.getFromDate().isEmpty()){
			errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.fromDate"));
		}
		if(publishForAllotmentForm.getEndDate() == null || publishForAllotmentForm.getEndDate().isEmpty()){
			errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.endDate"));
		}
		if(publishForAllotmentForm.getCourseIds() == null || publishForAllotmentForm.getCourseIds().length==0){
			errors .add( "error", new ActionError( "knowledgepro.admin.convocation.session.courses"));
		}
		if(publishForAllotmentForm.getYear()==null || publishForAllotmentForm.getYear().isEmpty()){
			errors.add("error", new ActionError("knowledgepro.admission.publish.allotment.year"));
		}
		if(publishForAllotmentForm.getAllotmentNo()==null || publishForAllotmentForm.getAllotmentNo().isEmpty()){
			errors.add("error", new ActionError("knowledgepro.admission.publish.allotment.allotmentNo"));
		}
		if(publishForAllotmentForm.getChanceNo()==null || publishForAllotmentForm.getChanceNo().isEmpty()){
			errors.add("error", new ActionError("knowledgepro.admission.publish.allotment.chanceNo"));
		} 
	}
	
	@SuppressWarnings("deprecation")
	public ActionForward updateAllotmentDetails(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		PublishForAllotmentForm allotmentForm =(PublishForAllotmentForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = validateData(allotmentForm);
		try{
			validateUpdateForm(errors,allotmentForm);
			if(errors.isEmpty()){
				boolean isUpdated=PublishForAllotmentHandler.getInstance().updateAllotmentDetails(allotmentForm);
				if(isUpdated){
					ActionMessage message = new ActionError( "knowledgepro.admission.publish.allotment.updated");
					messages.add("messages", message);
					saveMessages(request, messages);
					allotmentForm.reset(mapping, request);
					setCourseMapToForm(allotmentForm);
				}else{
					allotmentForm.reset(mapping, request);
					errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.updated.failure"));
					saveErrors(request, errors);
				}
			}else{
				saveErrors(request, errors);
			}
		}catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			allotmentForm.setErrorMessage(msg);
			allotmentForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.PUBLISH_ALLOTMENT_ENTRY);
	}

	@SuppressWarnings("deprecation")
	private void validateUpdateForm(ActionErrors errors, PublishForAllotmentForm publishForAllotmentForm) {
		if(publishForAllotmentForm.getFromDate() == null || publishForAllotmentForm.getFromDate().isEmpty()){
			errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.fromDate"));
		}
		if(publishForAllotmentForm.getEndDate() == null || publishForAllotmentForm.getEndDate().isEmpty()){
			errors .add( "error", new ActionError( "knowledgepro.admission.publish.allotment.endDate"));
		}
		
	}

}
