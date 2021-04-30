package com.kp.cms.actions.attendance;

import java.util.Calendar;
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
import com.kp.cms.bo.admin.ExtraCoCurricularLeavePublishBO;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.attendance.ExtraCoCurricularLeavePublishHandler;
import com.kp.cms.handlers.studentExtentionActivity.StudentExtentionFeedbackHandler;
import com.kp.cms.to.attendance.ExtraCoCurricularLeavePublishTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.utilities.CurrentAcademicYear;

public class ExtraCoCurricularLeavePublishAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(ExtraCoCurricularLeavePublishAction.class);
	
	
	
	public ActionForward initExtraCoCurricularLeavePublish(ActionMapping mapping,ActionForm form,HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		
		ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm  = (ExtraCoCurricularLeavePublishForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		extraCoCurricularLeavePublishForm.reset();
		setClassesToRequest(extraCoCurricularLeavePublishForm, request);
		setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
		return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
	}
	
	public ActionForward saveData(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		 
		ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm = (ExtraCoCurricularLeavePublishForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		ExtraCoCurricularLeavePublishBO exPublishBO = null;
		
		try{
			setUserId(request, extraCoCurricularLeavePublishForm);
			if(extraCoCurricularLeavePublishForm.getPublishFor() == null || extraCoCurricularLeavePublishForm.getPublishFor().isEmpty()){
				errors.add("error", new ActionError("knowledgepro.admin.addfailurePublishFor", "publish for"));
				saveErrors(request,errors);
				return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
			}
			//exPublishBO = ExtraCoCurricularLeavePublishHandler.getInstance().getRecord(extraCoCurricularLeavePublishForm);
			boolean isDuplicate = ExtraCoCurricularLeavePublishHandler.getInstance().isDuplicate(extraCoCurricularLeavePublishForm);
			if(!isDuplicate){
			boolean save = ExtraCoCurricularLeavePublishHandler.getInstance().saveData(extraCoCurricularLeavePublishForm);
			if(save){
				ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccessPublish"," Extra CoCurricular Leave ");
				messages.add("messages", message);
				saveMessages(request, messages);
				setClassesToRequest(extraCoCurricularLeavePublishForm, request);
				setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
				return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
			}else {
				errors.add("error", new ActionError("knowledgepro.admin.addfailurePublish", "Extra CoCurricular Leave"));
				saveErrors(request,errors);
				return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
			}
		}else {
			errors .add( "error", new ActionError( "knowledgepro.extracocurricularpublish.exists"));
				setClassesToRequest(extraCoCurricularLeavePublishForm, request);
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
		}
			
		}catch (Exception exception) {
			String  msg = super.handleApplicationException(exception);
			extraCoCurricularLeavePublishForm.setErrorMessage(msg);
			extraCoCurricularLeavePublishForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
			
		}
	}
	
	private void setClassesToRequest(ExtraCoCurricularLeavePublishForm curricularLeavePublishForm,
			HttpServletRequest request) throws Exception{
		Map<Integer, String> classMap;
		try {
			if (curricularLeavePublishForm.getAcademicYear() == null) {
				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int year = CurrentAcademicYear.getInstance().getAcademicyear();
				if (year != 0) {
					currentYear = year;
				}
				classMap = CommonAjaxHandler.getInstance()
						.getClassesByYearForMuliSelect(currentYear);
			} else {
				classMap = CommonAjaxHandler.getInstance()
						.getClassesByYearForMuliSelect(
								Integer.parseInt(curricularLeavePublishForm .getAcademicYear()));
			}
			
			request.setAttribute("classMap", classMap);
			curricularLeavePublishForm.setClassMap(classMap);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
	}
	
	public ActionForward editCoCurricularLeaveDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		ExtraCoCurricularLeavePublishForm publishForm = (ExtraCoCurricularLeavePublishForm)form;
  	  ActionMessages messages = new ActionMessages();
		   ActionErrors errors = publishForm.validate(mapping, request);
		   boolean flag = false;
		   try{
			 setUserId(request, publishForm);
			ExtraCoCurricularLeavePublishHandler.getInstance().getEditExtraCoCurricularFormPublish(publishForm);
		   }catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				publishForm.setErrorMessage(msg);
				publishForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
			request.setAttribute("openConnection", "edit");
			setClassesToRequest(publishForm, request); 
			return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
    }
	
	 public ActionForward deleteCoCurricularLeaveDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
   	  
		 ExtraCoCurricularLeavePublishForm publishForm1 = (ExtraCoCurricularLeavePublishForm)form;
   	  ActionMessages messages = new ActionMessages();
 		   ActionErrors errors = publishForm1.validate(mapping, request);
 		 try {
			if (errors.isEmpty()) {
				boolean isDeleted = ExtraCoCurricularLeavePublishHandler
						.getInstance().deleteExtraCoCurricularFormPublish(publishForm1);
				if (isDeleted) {
					ActionMessage message = new ActionError( "knowledgepro.cocurricularpublish.deleteSuccess");
					messages.add("messages", message);
					saveMessages(request, messages);
					publishForm1.clear();
				} else {
					errors .add( "error", new ActionError( "knowledgepro.cocurricularpublish.deleteSuccess"));
					saveErrors(request, errors);
				}
			}
		} catch (BusinessException businessException) {
			String msgKey = super.handleBusinessException(businessException);
			ActionMessage message = new ActionMessage(msgKey);
			messages.add(CMSConstants.MESSAGES, message);
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			publishForm1.setErrorMessage(msg);
			publishForm1.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		setClassesToRequest(publishForm1, request); 
		setExtraCoCurricularLeaveList(publishForm1);
		return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
     }
	 
	 private void setExtraCoCurricularLeaveList(ExtraCoCurricularLeavePublishForm leavePublishForm) throws Exception{
			int year=0;
	        if(leavePublishForm.getAcademicYear()!=null && !leavePublishForm.getAcademicYear().isEmpty()){
	        year=Integer.parseInt(leavePublishForm.getAcademicYear());
	        }
	        if(year==0){
			year = CurrentAcademicYear.getInstance().getAcademicyear();
			}
			List<ExtraCoCurricularLeavePublishTO> toList = ExtraCoCurricularLeavePublishHandler.getInstance().getStudentDetails(year);
			leavePublishForm.setToList(toList);
			
		}
	 public ActionForward updateExtraCoCurricularPublishDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		 ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm = (ExtraCoCurricularLeavePublishForm)form;
   	    setUserId(request, extraCoCurricularLeavePublishForm);
	  		ActionMessages messages = new ActionMessages();
	  		ActionErrors errors = extraCoCurricularLeavePublishForm.validate(mapping, request);
	  		try {
				if (errors.isEmpty()) {
					boolean duplicatecheck = ExtraCoCurricularLeavePublishHandler
							.getInstance().isDuplicate(extraCoCurricularLeavePublishForm);
					if (!duplicatecheck) {
						boolean isUpdated = ExtraCoCurricularLeavePublishHandler
								.getInstance().updateExtraCocurricularLeaveDetails(
										extraCoCurricularLeavePublishForm);
						if (isUpdated) {
							ActionMessage message = new ActionError(
									"knowledgepro.cocurricularpublish.updateSuccess");
							messages.add("messages", message);
							saveMessages(request, messages);
							extraCoCurricularLeavePublishForm.clear();
						} else {
							
							errors .add( "error", new ActionError( "nowledgepro.cocurricularpublish.updateFailur"));
							saveErrors(request, errors);
							request.setAttribute("openConnection", "edit");
							setClassesToRequest(extraCoCurricularLeavePublishForm, request);
							setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
							return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
						}
					} else {
						errors .add( "error", new ActionError( "knowledgepro.studentpublish.exists"));
						saveErrors(request, errors);
						request.setAttribute("openConnection", "edit");
						setClassesToRequest(extraCoCurricularLeavePublishForm, request);
						setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
						return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
					}
				} else {
					saveErrors(request, errors);
					request.setAttribute("openConnection", "edit");
					setClassesToRequest(extraCoCurricularLeavePublishForm, request);
					setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
					return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
				}
			} catch (BusinessException businessException) {
				String msgKey = super.handleBusinessException(businessException);
				ActionMessage message = new ActionMessage(msgKey);
				messages.add(CMSConstants.MESSAGES, message);
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				extraCoCurricularLeavePublishForm.setErrorMessage(msg);
				extraCoCurricularLeavePublishForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
			
			request.setAttribute("openConnection", "add");
			setClassesToRequest(extraCoCurricularLeavePublishForm, request);
			setExtraCoCurricularLeaveList(extraCoCurricularLeavePublishForm);
			return mapping .findForward(CMSConstants.EXTRA_COCURRICULAR_LEAVE_PUBLISH);
		}
}
