package com.kp.cms.actions.studentExtentionActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.studentExtentionActivity.StudentExtentionFeedbackHandler;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;

public class StudentExtentionFeedbackAction extends BaseDispatchAction{
	public ActionForward initStudentFeedback(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm)form;
	   StudentExtentionFeedbackHandler.getInstance().getStudentGroup();
		
		feedbackform.setSubGrouplist(StudentExtentionFeedbackHandler.getInstance().getStudentGroup());
		feedbackform.setExList(StudentExtentionFeedbackHandler.getInstance().getStudentExtention());
		try{
			feedbackform.clear();
			setUserId(request, feedbackform);
 			setClassesToRequest(feedbackform, request); 
			setStudentfeedbackList(feedbackform);
 			setSessionList(feedbackform,request);
			
		}catch(Exception exception){
			exception.printStackTrace();
			
		}
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
	}
	
      public ActionForward submitFeedbackDetails(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
    	  StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm)form;
    	  ActionMessages messages = new ActionMessages();
  		   ActionErrors errors = feedbackform.validate(mapping, request);
  		 validateStartDate(feedbackform, errors);
 		try {
 			if (errors.isEmpty()) {
 				
 				setUserId(request, feedbackform);
 				boolean duplicatecheck = StudentExtentionFeedbackHandler
 						.getInstance().duplicateCheck(feedbackform);
 				if (!duplicatecheck) {
 					boolean isAdded = StudentExtentionFeedbackHandler
 							.getInstance().submitOpenConnectionDetails(
 									feedbackform);
 					if (isAdded) {
 						ActionMessage message = new ActionError( "knowledgepro.studentpublish.addSuccess");
 						messages.add("messages", message);
 						saveMessages(request, messages);
 						feedbackform.clear();
 					} else {
 						feedbackform.clear();
 						errors .add( "error", new ActionError( "knowledgepro.studentpublish.addFailure"));
 						saveErrors(request, errors);
 					}
 				} else {
 					errors .add( "error", new ActionError( "knowledgepro.studentpublish.exists"));
 					setSpecializationListByClass(feedbackform, request);
 					saveErrors(request, errors);
 				}
 			} else {
 				saveErrors(request, errors);
 			}

 		} catch (BusinessException businessException) {
 			String msgKey = super.handleBusinessException(businessException);
 			ActionMessage message = new ActionMessage(msgKey);
 			messages.add(CMSConstants.MESSAGES, message);
 			return mapping.findForward(CMSConstants.ERROR_PAGE);
 		} catch (Exception exception) {
 			String msg = super.handleApplicationException(exception);
 			feedbackform.setErrorMessage(msg);
 			feedbackform.setErrorStack(exception.getMessage());
 			return mapping.findForward(CMSConstants.ERROR_PAGE);
 		}
 		feedbackform.setFlag(false);
 		setClassesToRequest(feedbackform, request); 
		setStudentfeedbackList(feedbackform);
		setSessionList(feedbackform,request);
 		
 		return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
 	}
      
      public ActionForward editFeedbackDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
    	  StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm)form;
    	  ActionMessages messages = new ActionMessages();
  		   ActionErrors errors = feedbackform.validate(mapping, request);
  		   boolean flag = false;
  		   try{
  			 setUserId(request, feedbackform);
  			StudentExtentionFeedbackHandler.getInstance()
 					.getEditOpenConnectionDetails(feedbackform);
 			feedbackform.setFlag(flag);
  			   
  		   }catch (Exception exception) {
  				String msg = super.handleApplicationException(exception);
  				feedbackform.setErrorMessage(msg);
  				feedbackform.setErrorStack(exception.getMessage());
  				return mapping.findForward(CMSConstants.ERROR_PAGE);
  			}
  			request.setAttribute("openConnection", "edit");
  			setClassesToRequest(feedbackform, request); 
			setStudentfeedbackList(feedbackform);
 			setSessionList(feedbackform,request);
 			setSpecializationListByClass(feedbackform,request);
 			return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
      }
      
      public ActionForward deleteFeedbackDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
    	  
    	  StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm)form;
    	  ActionMessages messages = new ActionMessages();
  		   ActionErrors errors = feedbackform.validate(mapping, request);
  		 try {
 			if (errors.isEmpty()) {
 				boolean isDeleted = StudentExtentionFeedbackHandler
 						.getInstance().deleteOpenConnection(feedbackform);
 				if (isDeleted) {
 					ActionMessage message = new ActionError( "knowledgepro.studentpublish.deleteSuccess");
 					messages.add("messages", message);
 					saveMessages(request, messages);
 					feedbackform.clear();
 				} else {
 					errors .add( "error", new ActionError( "knowledgepro.studentpublish.deleteFailure"));
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
 			feedbackform.setErrorMessage(msg);
 			feedbackform.setErrorStack(exception.getMessage());
 			return mapping.findForward(CMSConstants.ERROR_PAGE);
 		}
 		setClassesToRequest(feedbackform, request); 
		setStudentfeedbackList(feedbackform);
		setSessionList(feedbackform,request);
 		return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
      }
      
      public ActionForward updateFeedbackDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
    	  StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm)form;
    	    setUserId(request, feedbackform);
	  		ActionMessages messages = new ActionMessages();
	  		ActionErrors errors = feedbackform.validate(mapping, request);
	  		validateStartDate(feedbackform, errors);
	  		try {
				if (errors.isEmpty()) {
					boolean duplicatecheck = StudentExtentionFeedbackHandler
							.getInstance().duplicateCheck(feedbackform);
					if (duplicatecheck) {
						boolean isUpdated = StudentExtentionFeedbackHandler
								.getInstance().updateOpenConnectionDetails(
										feedbackform);
						if (isUpdated) {
							ActionMessage message = new ActionError(
									"knowledgepro.studentpublish.updateSuccess");
							messages.add("messages", message);
							saveMessages(request, messages);
							feedbackform.clear();
						} else {
							
							errors .add( "error", new ActionError( "knowledgepro.studentpublish.updateFailure"));
							saveErrors(request, errors);
							request.setAttribute("openConnection", "edit");
							setClassesToRequest(feedbackform, request); 
							setStudentfeedbackList(feedbackform);
							setSessionList(feedbackform,request);
							return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
						}
					} else {
						errors .add( "error", new ActionError( "knowledgepro.studentpublish.exists"));
						saveErrors(request, errors);
						request.setAttribute("openConnection", "edit");
						setClassesToRequest(feedbackform, request); 
						setStudentfeedbackList(feedbackform);
			 			setSessionList(feedbackform,request);
			 			setSpecializationListByClass(feedbackform,request);
						return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
					}
				} else {
					saveErrors(request, errors);
					request.setAttribute("openConnection", "edit");
					setClassesToRequest(feedbackform, request); 
					setStudentfeedbackList(feedbackform);
					setSessionList(feedbackform,request);
					return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
				}
			} catch (BusinessException businessException) {
				String msgKey = super.handleBusinessException(businessException);
				ActionMessage message = new ActionMessage(msgKey);
				messages.add(CMSConstants.MESSAGES, message);
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				feedbackform.setErrorMessage(msg);
				feedbackform.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
			feedbackform.setFlag(false);
			request.setAttribute("openConnection", "add");
			setClassesToRequest(feedbackform, request); 
			setStudentfeedbackList(feedbackform);
			setSessionList(feedbackform,request);
			return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
		}
  		   
      
      
	private void setSessionList(StudentExtentionFeedbackForm feedbackform,
			HttpServletRequest request) throws Exception{
		
		Map<Integer,String> feedbackSessionMap = StudentExtentionFeedbackHandler.getInstance().getFeedBackSessionList(feedbackform);
		request.setAttribute("SessionMap", feedbackSessionMap);
		
	}

	private void setStudentfeedbackList(StudentExtentionFeedbackForm feedbackform) throws Exception{
		int year=0;
        if(feedbackform.getAcademicYear()!=null && !feedbackform.getAcademicYear().isEmpty()){
        year=Integer.parseInt(feedbackform.getAcademicYear());
        }
        if(year==0){
		year = CurrentAcademicYear.getInstance().getAcademicyear();
		}
		List<StudentExtentionFeedbackTO> toList = StudentExtentionFeedbackHandler.getInstance().getStudentDetails(year);
		feedbackform.setFeedbackConnectionTo(toList);
		
	}

	private void setClassesToRequest(StudentExtentionFeedbackForm feedbackform,
			HttpServletRequest request) throws Exception{
		Map<Integer, String> classMap;
		try {
			if (feedbackform.getAcademicYear() == null) {
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
								Integer.parseInt(feedbackform .getAcademicYear()));
			}

			request.setAttribute("classMap", classMap);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
	}
	public ActionForward getClassAndSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentExtentionFeedbackForm feedbackform = (StudentExtentionFeedbackForm) form;
		feedbackform.setSubGrouplist(StudentExtentionFeedbackHandler.getInstance().getStudentGroup());
		setUserId(request, feedbackform);
		try{
			setClassesToRequest(feedbackform, request); 
			setStudentfeedbackList(feedbackform);
 			setSessionList(feedbackform,request);
 			setSpecializationListByClass(feedbackform,request);
 			
		}catch (BusinessException businessException) {
			String msgKey = super.handleBusinessException(businessException);
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			feedbackform.setErrorMessage(msg);
			feedbackform.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping .findForward(CMSConstants.STUDENT_EXTENTION_FEEDBACK);
	}
	private void setSpecializationListByClass( StudentExtentionFeedbackForm feedbackform, HttpServletRequest request)throws Exception {
		if(feedbackform.getClassesId()!=null && !feedbackform.getClassesId().toString().isEmpty()){
			String selectedClasses[] = feedbackform.getClassesId() ;
			Set<Integer> classesIdsSet = new HashSet<Integer>();
			for (int i = 0; i < selectedClasses.length; i++) {
				classesIdsSet.add(Integer.parseInt(selectedClasses[i]));
			}
			Map<Integer, String> specializationMap = CommonAjaxHandler.getInstance().getSpecializationByClassId(classesIdsSet);
		    request.setAttribute("specializationMap", specializationMap);
	}
	}
	
	private void validateStartDate(
			StudentExtentionFeedbackForm feedbackform,
			ActionErrors errors) {
		if (feedbackform.getStartDate() != null
				&& !StringUtils.isEmpty(feedbackform.getStartDate())
				&& !CommonUtil.isValidDate(feedbackform.getStartDate())) {
			if (errors.get(CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID) != null
					&& !errors.get(
							CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID)
							.hasNext()) {
				errors
						.add(
								CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID,
								new ActionError(
										CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID));
			}
		}
		if (feedbackform.getEndDate() != null
				&& !StringUtils.isEmpty(feedbackform.getEndDate())
				&& !CommonUtil.isValidDate(feedbackform.getEndDate())) {
			if (errors.get(CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID) != null
					&& !errors.get(
							CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID)
							.hasNext()) {
				errors
						.add(
								CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID,
								new ActionError(
										CMSConstants.ATTENDANCE_ATTENDANCE_DATEINVALID));
			}
		}
		if (feedbackform.getEndDate() != null
				&& !StringUtils.isEmpty(feedbackform.getEndDate())
				&& CommonUtil.isValidDate(feedbackform.getEndDate())) {

			if (CommonUtil.checkForEmpty(feedbackform.getStartDate())
					&& CommonUtil.checkForEmpty(feedbackform.getEndDate())) {
				Date startDate = CommonUtil.ConvertStringToDate(feedbackform
						.getStartDate());
				Date endDate = CommonUtil.ConvertStringToDate(feedbackform
						.getEndDate());

				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(startDate);
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(endDate);
				long daysBetween = CommonUtil.getDaysBetweenDates(cal1, cal2);
				if (daysBetween <= 0) {
					errors.add("error", new ActionError(
							CMSConstants.KNOWLEDGEPRO_STARTDATE_CONNOTBELESS));

				}
			}
		}
	}
}
