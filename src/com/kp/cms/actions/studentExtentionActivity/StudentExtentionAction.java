package com.kp.cms.actions.studentExtentionActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;
import com.kp.cms.handlers.studentExtentionActivity.StudentExtentionHandler;


public class StudentExtentionAction extends BaseDispatchAction{
	
	public ActionForward initStudentExtension(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
                                              throws Exception{

		StudentExtentionForm masterForm = (StudentExtentionForm)form;
      try{
    	masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
      	masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
    	return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
    	
		}catch(Exception exception)
		{
		String msg = super.handleApplicationException(exception);
		masterForm.setErrorMessage(msg);
		masterForm.setErrorStack(exception.getMessage());
		return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		}
	
	public ActionForward submitStudentExtentionDetails(ActionMapping mapping,
		  		 ActionForm form,
		  		 HttpServletRequest request,
		  		 HttpServletResponse response) throws Exception{
		StudentExtentionForm masterForm = (StudentExtentionForm)form;
		ActionErrors errors = masterForm.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		boolean isAdded = false;
		try
		{
			if(!errors.isEmpty())
			{
				saveErrors(request, errors);				
				masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
				masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
				if(masterForm.getActivityName().trim().equals("")) {
					masterForm.setActivityName(null);
				}
				return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
			}
			setUserId(request, masterForm);
			isAdded = StudentExtentionHandler.getInstance().addStudentExtentionDetails(masterForm, "Add");
			
			masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
			masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
		}
		catch(DuplicateException ex)
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.exists"));
			saveErrors(request, errors);			
			masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
			return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
		catch (ReActivateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.reactivate"));
			saveErrors(request, errors);			
			masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
			masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
			return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
		catch(Exception ex)
		{
			if (ex instanceof BusinessException) 
			{
				String msgKey = super.handleBusinessException(ex);
				ActionMessage message = new ActionMessage(msgKey);
				messages.add("messages", message);
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} 
			else if (ex instanceof ApplicationException)
			{
				String msg = super.handleApplicationException(ex);
				masterForm.setErrorMessage(msg);
				masterForm.setErrorStack(ex.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} 
			else 
			{
				throw ex;
			}
		}
		
		if (isAdded) {
			ActionMessage message = new ActionMessage("knowledgepro.studentExtentionActivity.addSucess",masterForm.getActivityName());
			messages.add("messages", message);
			saveMessages(request, messages);
			masterForm.reset(mapping, request);
		}
		else
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.addFailure",masterForm.getActivityName()));
			saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY); 
	}
        
	public ActionForward updateStudentExtentionDetails(ActionMapping mapping,
			  		 ActionForm form,
			  		 HttpServletRequest request,
			  		 HttpServletResponse response) throws Exception
		{
		StudentExtentionForm masterForm = (StudentExtentionForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = masterForm.validate(mapping, request);
		boolean isUpdated = false;
		try 
		{

			if(!errors.isEmpty())
			{
				saveErrors(request, errors);				
				masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
				masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
				if(masterForm.getActivityName().trim().equals("")) {
					masterForm.setActivityName(null);
				}
				return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
			}
			setUserId(request, masterForm);
			isUpdated = StudentExtentionHandler.getInstance().addStudentExtentionDetails(masterForm, "Edit");
		} 
		catch (DuplicateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.exists"));
			saveErrors(request, errors);
			masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
		catch (ReActivateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.reactivate"));
			saveErrors(request, errors);			
			masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
		catch (Exception e) 
		{
		if (e instanceof BusinessException)
		{
			String msgKey = super.handleBusinessException(e);
			ActionMessage message = new ActionMessage(msgKey);
			messages.add("messages", message);
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		else if (e instanceof ApplicationException) 
		{
			String msg = super.handleApplicationException(e);
			masterForm.setErrorMessage(msg);
			masterForm.setErrorStack(e.getMessage());
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		else
		{
		throw e;
		}
		}
		
		masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
		masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
		if (isUpdated) {
			ActionMessage message = new ActionMessage("knowledgepro.studentExtentionActivity.updateSucess", masterForm.getActivityName());
			messages.add("messages", message);
			saveMessages(request, messages);
			masterForm.reset(mapping, request);
		} 
		else {
		errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.updateFailure",masterForm.getActivityName()));
		saveErrors(request, errors);
		}
			request.setAttribute("admOperation", "add");
			return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
		
	public ActionForward deleteStudentExtentionDetails(ActionMapping mapping,
						 ActionForm form,
						 HttpServletRequest request,
						 HttpServletResponse response) throws Exception
		{
		StudentExtentionForm masterForm = (StudentExtentionForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		boolean isDeleted = false;
		try 
		{
		if (masterForm.getId() != 0) 
		{
			int dupId = masterForm.getId();
		setUserId(request, masterForm);
		isDeleted = StudentExtentionHandler.getInstance().deleteSubjectActivity(dupId, false, masterForm);
		}
		} 
		catch (Exception e) 
		{
		if (e instanceof BusinessException) 
		{
		String msgKey = super.handleBusinessException(e);
		ActionMessage message = new ActionMessage(msgKey);
		messages.add("messages", message);
		return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		else if (e instanceof ApplicationException) 
		{
		String msg = super.handleApplicationException(e);
		masterForm.setErrorMessage(msg);
		masterForm.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		else 
		{
		throw e;
		}
		}
		masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
		masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
		if (isDeleted) {
		ActionMessage message = new ActionMessage("knowledgepro.studentExtentionActivity.deleteSucess", masterForm.getActivityName());
		messages.add("messages", message);
		saveMessages(request, messages);
		masterForm.reset(mapping, request);
		} else {
		errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.deleteFailure",masterForm.getActivityName()));
		saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);
		}
	
			public ActionForward reActivateStudentExtentionDetails(ActionMapping mapping,
				  		 ActionForm form,
				  		 HttpServletRequest request,
				  		 HttpServletResponse response) throws Exception
		{
			StudentExtentionForm masterForm = (StudentExtentionForm)form;
			ActionErrors errors = new ActionErrors();
			ActionMessages messages = new ActionMessages();
			boolean isActivated = false;
		try 
		{
		if (masterForm.getDupId() != 0) 
		{
			int dupId = masterForm.getDupId();
			setUserId(request, masterForm);
			isActivated = StudentExtentionHandler.getInstance().deleteSubjectActivity(dupId, true, masterForm);
		}
		}
		catch (Exception e)
		{
			errors.add("error", new ActionError("knowledgepro.studentExtentionActivity.reactivationFailure"));
			saveErrors(request, errors);
		}
		  masterForm.setSubjectActivity(StudentExtentionHandler.getInstance().getSubjectActivity());
		  masterForm.setList(StudentExtentionHandler.getInstance().getStudentGroup());
			if (isActivated) {
			ActionMessage message = new ActionMessage("knowledgepro.studentExtentionActivity.reactivationSucess");
			messages.add("messages", message);
			saveMessages(request, messages);
		}
		  return mapping.findForward(CMSConstants.STUDENT_EXTENSION_ENTRY);		
		}		

				
}
