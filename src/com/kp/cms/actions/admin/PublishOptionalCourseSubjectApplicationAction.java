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
import com.kp.cms.forms.admin.PublishOptionalCourseSubjectApplicationForm;
import com.kp.cms.handlers.admin.PublishOptionalCourseSubjectApplicationHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.PublishOptionalCourseSubjectApplicationTO;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;

public class PublishOptionalCourseSubjectApplicationAction extends BaseDispatchAction 
{
	ActionErrors errors = new ActionErrors();
	ActionMessages messages = new ActionMessages();
	PublishOptionalCourseSubjectApplicationHandler handler = new PublishOptionalCourseSubjectApplicationHandler();
	private static final Log log = LogFactory.getLog(PublishOptionalCourseSubjectApplicationAction.class);
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request 
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 * This method will be invoke when the particular link clicked.
	 * 
	 */
	public ActionForward initPublishOptionalCourseSubjectApplication(ActionMapping mapping,
													ActionForm form, HttpServletRequest request,
													HttpServletResponse response) throws Exception {
		
		log.info("Inside of initAttendanceSlipDetails");
		PublishOptionalCourseSubjectApplicationForm attendancePercentageDayRangeForm = (PublishOptionalCourseSubjectApplicationForm) form;	
		HttpSession session=request.getSession();
		attendancePercentageDayRangeForm.reset(mapping, request);
		session.setAttribute("field","Caste"); 
		try {
			
			
			setUserId(request, attendancePercentageDayRangeForm);
			
			initsetDataToForm(attendancePercentageDayRangeForm,request);
			List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(attendancePercentageDayRangeForm);
			attendancePercentageDayRangeForm.setAttendanceList(attendancePercentageList);
		}  catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			attendancePercentageDayRangeForm.setErrorMessage(msg);
			attendancePercentageDayRangeForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Leaving into initAttendanceEntry");
		return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
	}
	/**
	 * @param attendanceEntryForm
	 */
	private void initsetDataToForm(PublishOptionalCourseSubjectApplicationForm attendanceEntryForm,HttpServletRequest request) throws Exception{
		Map<Integer,String> classMap = setupClassMapToRequest(attendanceEntryForm,request);
		attendanceEntryForm.setClassMap(classMap);
	}
	public Map<Integer,String> setupClassMapToRequest(PublishOptionalCourseSubjectApplicationForm attendanceEntryForm,HttpServletRequest request) throws Exception{
		log.info("Entering into setpClassMapToRequest of CreatePracticalBatchAction");
		Map<Integer,String> classMap = new HashMap<Integer, String>();
		try {
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int year=CurrentAcademicYear.getInstance().getAcademicyear();
			if(year!=0){
				currentYear=year;
			}
			if(attendanceEntryForm.getYear()!=null && !attendanceEntryForm.getYear().isEmpty()){
				currentYear=Integer.parseInt(attendanceEntryForm.getYear());
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
	
	
	
	
	
	// adding
	public ActionForward addPublishOptionalCourseSubjectApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublishOptionalCourseSubjectApplicationForm objform = (PublishOptionalCourseSubjectApplicationForm) form;
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
							"knowledgepro.admin.publishoptionalcoursesubject.addsuccess", objform.getClassNames());
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
							"knowledgepro.admin.publishoptionalcoursesubject.exists","Attendance Percentage DayRange",objform.getClassNames()));
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
		//}

		
		objform=retainAllValues(objform);
		objform.setMapSelectedClass(null);
		
		initsetDataToForm(objform,request);
		List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(objform);
		objform.setAttendanceList(attendancePercentageList);
		request.setAttribute("editPublishHallTicket", "add");
		
		return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);

	}

	
	private void validateDates( PublishOptionalCourseSubjectApplicationForm attendancePercentageDayRangeForm, ActionErrors errors) throws Exception {
		if(CommonUtil.checkForEmpty(attendancePercentageDayRangeForm.getFromDate()) && CommonUtil.checkForEmpty(attendancePercentageDayRangeForm.getToDate())){
			Date startDate = CommonUtil.ConvertStringToDate(attendancePercentageDayRangeForm.getFromDate());
			Date endDate = CommonUtil.ConvertStringToDate(attendancePercentageDayRangeForm.getToDate());

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
	
	
	
//On - EDIT (To retain all values)
private PublishOptionalCourseSubjectApplicationForm retainAllValues(PublishOptionalCourseSubjectApplicationForm objform) throws Exception {
	
	
	objform.setId(objform.getId());
	objform.setToDate(objform.getToDate());
	objform.setFromDate(objform.getFromDate());
	objform.setAcademicYear(objform.getYear());
	objform.setStayClass(objform.getStayClass());
	
	
	
	objform.setClassCodeIdsTo(objform.getStayClass());
	
	return objform;
}
	

//On - DELETE
public ActionForward deletePublishOptionalCourseSubjectApplication(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
	PublishOptionalCourseSubjectApplicationForm objform = (PublishOptionalCourseSubjectApplicationForm) form;
	errors.clear();
	messages.clear();
	setUserId(request, objform);
	String id = request.getParameter("id");
	handler.delete(Integer.parseInt(id));
	ActionMessage message = new ActionMessage(
			"knowledgepro.admin.publishoptionalcoursesubject.deletesuccess", "");
	messages.add("messages", message);
	saveMessages(request, messages);
	objform.reset(mapping, request);
	List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(objform);
	objform.setAttendanceList(attendancePercentageList);
	return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
}




	
	public ActionForward updatePublishOptionalCourseSubjectApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublishOptionalCourseSubjectApplicationForm objform = (PublishOptionalCourseSubjectApplicationForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = objform.validate(mapping, request);
		validateDates(objform,errors);
			try{
				if (errors.isEmpty()) {

					
					setUserId(request, objform);
					handler.update(Integer.parseInt(objform.getId()), 
							 objform.getClassCodeIdsTo(),objform.getToDate(),objform.getFromDate(),objform.getUserId() ,objform
							);
					ActionMessage message = new ActionMessage("knowledgepro.admin.publishoptionalcoursesubject.updatesuccess", objform.getClassName());
					messages.add("messages", message);
					saveMessages(request, messages);
					objform.reset(mapping, request);
					initsetDataToForm(objform,request);
					List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(objform );
					objform.setAttendanceList(attendancePercentageList);
					 
					}else{
						initsetDataToForm(objform,request);
						List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(objform );
						objform.setAttendanceList(attendancePercentageList);
						request.setAttribute("department", "edit");
						saveErrors(request, errors);
						
					}
				}			
			catch (DuplicateException e1) {
				errors.add("error", new ActionError("knowledgepro.admin.publishoptionalcoursesubject.exists"));
				saveErrors(request, errors);
				
				request.setAttribute("department", "edit");
				return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
			} catch (ReActivateException e1) {
				errors.add("error", new ActionError("knowledgepro.attendance.attendanceentryday.addfailure.alreadyexist.reactivate"));
				saveErrors(request, errors);
				
				request.setAttribute("department", "edit");
				return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
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
			return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
	}
	
	

	public ActionForward editPublishOptionalCourseSubjectApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		PublishOptionalCourseSubjectApplicationForm publishOptionalCourseSubjectApplicationForm = (PublishOptionalCourseSubjectApplicationForm)form;
		setUserId(request, publishOptionalCourseSubjectApplicationForm);
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		
		try {
			PublishOptionalCourseSubjectApplicationHandler.getInstance().editPublishOptionalCourseSubjectApplication(publishOptionalCourseSubjectApplicationForm);
			request.setAttribute("department", "edit");

		} catch (BusinessException businessException) {
			log.info("Exception editMenus");
			String msgKey = super.handleBusinessException(businessException);
			ActionMessage message = new ActionMessage(msgKey);
			messages.add(CMSConstants.MESSAGES, message);
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			publishOptionalCourseSubjectApplicationForm.setErrorMessage(msg);
			publishOptionalCourseSubjectApplicationForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		initsetDataToForm(publishOptionalCourseSubjectApplicationForm,request);
		List<PublishOptionalCourseSubjectApplicationTO> attendancePercentageList = PublishOptionalCourseSubjectApplicationHandler.getInstance().getPublishOptionalCourseSubjectApplication(publishOptionalCourseSubjectApplicationForm);
		publishOptionalCourseSubjectApplicationForm.setAttendanceList(attendancePercentageList);
		return mapping.findForward(CMSConstants.PUBLISH_OPTIONAL_COURSE_SUBJECT_APPLICATION);
	}


}
