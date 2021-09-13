package com.kp.cms.actions.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.admin.PublishSpecialFeesForm;
import com.kp.cms.handlers.admin.PublishSpecialFeesHandler;
import com.kp.cms.handlers.admin.PublishStudentSemesterFeesHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admin.PublishStudentSemesterFeesTO;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;


public class PublishSpecialFeesAction extends BaseDispatchAction {
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	private static final Log log = LogFactory.getLog(PublishSpecialFeesAction.class);
	PublishSpecialFeesHandler handler = new PublishSpecialFeesHandler();
	
	public ActionForward initPublishSpecialFees(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		PublishSpecialFeesForm publishSpecialFeesForm = (PublishSpecialFeesForm)form;
		HttpSession session=request.getSession();
		publishSpecialFeesForm.reset(mapping, request);
		session.setAttribute("field","Caste"); 
		try{
			setUserId(request, publishSpecialFeesForm);
			initsetDataToForm(publishSpecialFeesForm,request);
			List<PublishSpecialFeesTO> pubList = PublishSpecialFeesHandler.getInstance().getPublishList(publishSpecialFeesForm);
			publishSpecialFeesForm.setAttendanceList(pubList);
		}catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			publishSpecialFeesForm.setErrorMessage(msg);
			publishSpecialFeesForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
	}

	private void initsetDataToForm(PublishSpecialFeesForm publishSpecialFeesForm,
			HttpServletRequest request)  throws Exception{
		Map<Integer,String> classMap = setupClassMapToRequest(publishSpecialFeesForm,request);
		publishSpecialFeesForm.setClassMap(classMap);
		
	}

	private Map<Integer, String> setupClassMapToRequest(PublishSpecialFeesForm publishSpecialFeesForm,
			HttpServletRequest request) throws Exception {
		Map<Integer,String> classMap = new HashMap<Integer, String>();
		try {
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int year=CurrentAcademicYear.getInstance().getAcademicyear();
			if(year!=0){
				currentYear=year;
			}
			if(publishSpecialFeesForm.getYear()!=null && !publishSpecialFeesForm.getYear().isEmpty()){
				currentYear=Integer.parseInt(publishSpecialFeesForm.getYear());
			}
			classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
			request.setAttribute("classMap", classMap);
			return classMap;
		} catch (Exception e) {
			log.debug(e.getMessage());
			log.error("Error occured in setupClassMapToRequest of CreatePracticalBatchAction");
		}
		log.info("Leaving into setupClassMapToRequest of CreatePracticalBatchAction");
		return classMap;
	}
	
	public ActionForward addSpecialFees(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		PublishSpecialFeesForm objform = (PublishSpecialFeesForm)form;
		errors.clear();
		messages.clear();
		setUserId(request, objform);
		errors = objform.validate(mapping, request);
		saveErrors(request, errors);
        String[] stayClass = objform.getStayClass();
        validateDates(objform,errors);
        
        if (errors.isEmpty()) {
    		
			try {
				if(stayClass!=null && !stayClass[0].isEmpty())
				{
					
				handler.addList(
						 objform.getStayClass(),objform.getToDate(),objform.getFromDate(),objform.getUserId() ,objform );

				ActionMessage message = new ActionMessage(
						"knowledgepro.admin.publishstudentsemesterfees.addsuccess", objform.getClassNames());
				messages.add("messages", message);
				saveMessages(request, messages);
				objform.reset(mapping, request);
				
				}
				else
				{
					errors.add("error", new ActionError(
							"knowledgepro.exam.publish.select.classes"));
					saveErrors(request, errors);
					
				}
			} catch (DuplicateException e1) {
				errors.add("error", new ActionError(
						"knowledgepro.admin.publishstudentsemesterfees.exists",objform.getClassNames()));
				saveErrors(request, errors);
			}
			 catch (ApplicationException e2) {
					errors.add("error", new ActionError(
							"knowledgepro.exam.publish.marks.card.error",e2.getMessage()));
					saveErrors(request, errors);
				}
		}else {
			saveErrors(request, errors);
		}
        
        objform=retainAllValues(objform);
		objform.setMapSelectedClass(null);
		initsetDataToForm(objform,request);
		List<PublishSpecialFeesTO> pubList = PublishSpecialFeesHandler.getInstance().getPublishList(objform);
		objform.setAttendanceList(pubList);
		request.setAttribute("editPublishHallTicket", "add");
		
		return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
	}

	private PublishSpecialFeesForm retainAllValues(PublishSpecialFeesForm objform) throws Exception {
		objform.setId(objform.getId());
		objform.setToDate(objform.getToDate());
		objform.setFromDate(objform.getFromDate());
		objform.setAcademicYear(objform.getYear());
		objform.setStayClass(objform.getStayClass());
		
		
		
		objform.setClassCodeIdsTo(objform.getStayClass());
		
		return objform;
	}

	private void validateDates(PublishSpecialFeesForm objform,ActionErrors errors2) throws Exception{
		if(CommonUtil.checkForEmpty(objform.getFromDate()) && CommonUtil.checkForEmpty(objform.getToDate())){
			Date startDate = CommonUtil.ConvertStringToDate(objform.getFromDate());
			Date endDate = CommonUtil.ConvertStringToDate(objform.getToDate());

			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(startDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(endDate);
			long daysBetween = CommonUtil.getDaysBetweenDates(cal1, cal2);
			if(daysBetween <= 0) {
				errors.add("error", new ActionError(CMSConstants.KNOWLEDGEPRO_FROMDATE_CONNOTBELESS));
			}
		}
		
	}
	
	public ActionForward deleteSpecialFees(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		PublishSpecialFeesForm objform = (PublishSpecialFeesForm)form;
		errors.clear();
		messages.clear();
		setUserId(request, objform);
		String id = request.getParameter("id");
		handler.delete(Integer.parseInt(id));
		ActionMessage message = new ActionMessage(
				"knowledgepro.admin.publishstudentsemesterfees.deletesuccess", "");
		messages.add("messages", message);
		saveMessages(request, messages);
		objform.reset(mapping, request);
		List<PublishSpecialFeesTO> pubList = PublishSpecialFeesHandler.getInstance().getPublishList(objform);
		objform.setAttendanceList(pubList);
		return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
	}
	
	public ActionForward updateSpecialFees(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		PublishSpecialFeesForm objform = (PublishSpecialFeesForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = objform.validate(mapping, request);
		validateDates(objform,errors);
			try{
				if (errors.isEmpty()) {

					
					setUserId(request, objform);
					handler.update(Integer.parseInt(objform.getId()), 
							 objform.getClassCodeIdsTo(),objform.getToDate(),objform.getFromDate(),objform.getUserId() ,objform
							);
					ActionMessage message = new ActionMessage("knowledgepro.admin.publishstudentsemesterfees.updatesuccess", objform.getClassName());
					messages.add("messages", message);
					saveMessages(request, messages);
					objform.reset(mapping, request);
					initsetDataToForm(objform,request);
					List<PublishSpecialFeesTO> publishList = PublishSpecialFeesHandler.getInstance().getPublishList(objform); 
					objform.setAttendanceList(publishList);
					 
					}else{
						initsetDataToForm(objform,request);
						List<PublishSpecialFeesTO> publishList = PublishSpecialFeesHandler.getInstance().getPublishList(objform); 
						objform.setAttendanceList(publishList);
						request.setAttribute("department", "edit");
						saveErrors(request, errors);
						
					}
				}			
			catch (DuplicateException e1) {
				errors.add("error", new ActionError("knowledgepro.admin.publishstudentsemesterfees.exists"));
				saveErrors(request, errors);
				
				request.setAttribute("department", "edit");
				return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
			} catch (ReActivateException e1) {
				errors.add("error", new ActionError("knowledgepro.attendance.attendanceentryday.addfailure.alreadyexist.reactivate"));
				saveErrors(request, errors);
				
				request.setAttribute("department", "edit");
				return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
			} catch (Exception e) {
				log.error("error in update admitted through page...", e);
				if (e instanceof BusinessException) {
					String msgKey = super.handleBusinessException(e);
					ActionMessage message = new ActionMessage(msgKey);
					messages.add("messages", message);
					request.setAttribute("department", "edit");
					return mapping.findForward(CMSConstants.ERROR_PAGE);
				} else if (e instanceof ApplicationException) {
					String msg = super.handleApplicationException(e);
					objform.setErrorMessage(msg);
					objform.setErrorStack(e.getMessage());
					request.setAttribute("department", "edit");
					return mapping.findForward(CMSConstants.ERROR_PAGE);
				} else {
					String msg = super.handleApplicationException(e);
					objform.setErrorMessage(msg);
					objform.setErrorStack(e.getMessage());
					request.setAttribute("department", "edit");
					return mapping.findForward(CMSConstants.ERROR_PAGE);
				}
			}
			return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
	
		
	}
	
	public ActionForward editSpecialFees(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		PublishSpecialFeesForm publishSpecialFeesForm = (PublishSpecialFeesForm)form;
		setUserId(request, publishSpecialFeesForm);
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		
		try {
			PublishSpecialFeesHandler.getInstance().editPublishSpecialFees(publishSpecialFeesForm);
			request.setAttribute("department", "edit");

		} catch (BusinessException businessException) {
			log.info("Exception editMenus");
			String msgKey = super.handleBusinessException(businessException);
			ActionMessage message = new ActionMessage(msgKey);
			messages.add(CMSConstants.MESSAGES, message);
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			publishSpecialFeesForm.setErrorMessage(msg);
			publishSpecialFeesForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		initsetDataToForm(publishSpecialFeesForm,request);
		List<PublishSpecialFeesTO> publishList = PublishSpecialFeesHandler.getInstance().getPublishList(publishSpecialFeesForm);
		publishSpecialFeesForm.setAttendanceList(publishList);
		return mapping.findForward(CMSConstants.PUBLISH_SPECIAL_FEES);
	}
	
}
