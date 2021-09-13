package com.kp.cms.actions.studentExtentionActivity;

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
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.studentExtentionActivity.StudentGroupForm;
import com.kp.cms.handlers.studentExtentionActivity.StudentGroupHandler;

@SuppressWarnings("deprecation")
public class StudentGroupAction extends BaseDispatchAction  {
	
	public ActionForward initStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		StudentGroupForm formgroup = (StudentGroupForm)form;
		try{
			formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
			return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
		}catch(Exception exception)
		{
			String msg = super.handleApplicationException(exception);
			formgroup.setErrorMessage(msg);
			formgroup.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		
	}
	public ActionForward submitStudentGroupDetails(ActionMapping mapping,
	  		 ActionForm form,
	  		 HttpServletRequest request,
	  		 HttpServletResponse response) throws Exception{
		StudentGroupForm formgroup = (StudentGroupForm)form;
		ActionErrors errors = formgroup.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		boolean isAdded = false;
	try
	{
		if(!errors.isEmpty())
		{
			saveErrors(request, errors);				
			formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
			if(formgroup.getGroupName().trim().equals("")) {
				formgroup.setGroupName(null);
			}
			return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
		}
		setUserId(request, formgroup);
		isAdded = StudentGroupHandler.getInstance().addStudentGroupDetails(formgroup, "Add");
		
		formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
	}
	catch(DuplicateException ex)
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.exists"));
		saveErrors(request, errors);			
		formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
	}
	catch (ReActivateException e1) 
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.reactivate"));
		saveErrors(request, errors);			
		formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
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
			formgroup.setErrorMessage(msg);
			formgroup.setErrorStack(ex.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		else 
		{
			throw ex;
		}
	}
	
	if (isAdded) {
		ActionMessage message = new ActionMessage("knowledgepro.studentGroup.addSucess",formgroup.getGroupName());
		messages.add("messages", message);
		saveMessages(request, messages);
		formgroup.reset(mapping, request);
	}
	else
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.addFailure",formgroup.getGroupName()));
		saveErrors(request, errors);
	}
	return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP); 
}
	
 public ActionForward updateStudentGroupDetails(ActionMapping mapping,
		  		 ActionForm form,
		  		 HttpServletRequest request,
		  		 HttpServletResponse response) throws Exception
	{
	StudentGroupForm formgroup = (StudentGroupForm)form;
	ActionMessages messages = new ActionMessages();
	ActionErrors errors = formgroup.validate(mapping, request);
	boolean isUpdated = false;
	try 
	{
	
		if(!errors.isEmpty())
		{
			saveErrors(request, errors);				
			formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
			if(formgroup.getGroupName().trim().equals("")) {
				formgroup.setGroupName(null);
			}
			return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
		}
		setUserId(request, formgroup);
		isUpdated = StudentGroupHandler.getInstance().addStudentGroupDetails(formgroup, "Edit");
	} 
	catch (DuplicateException e1) 
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.exists"));
		saveErrors(request, errors);
		formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
		request.setAttribute("admOperation", "edit");
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
	}
	catch (ReActivateException e1) 
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.reactivate"));
		saveErrors(request, errors);			
		formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
		request.setAttribute("admOperation", "edit");
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
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
		formgroup.setErrorMessage(msg);
		formgroup.setErrorStack(e.getMessage());
		request.setAttribute("admOperation", "edit");
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	} 
	else
	{
	throw e;
	}
	}
	
	formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
	if (isUpdated) {
		ActionMessage message = new ActionMessage("knowledgepro.studentGroup.updateSucess", formgroup.getGroupName());
		messages.add("messages", message);
		saveMessages(request, messages);
		formgroup.reset(mapping, request);
	} 
	else {
	errors.add("error", new ActionError("knowledgepro.studentGroup.updateFailure",formgroup.getGroupName()));
	saveErrors(request, errors);
	}
		request.setAttribute("admOperation", "add");
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
	}
 
	 public ActionForward deleteStudentGroupDetails(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response) throws Exception
	{
		StudentGroupForm formgroup = (StudentGroupForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
	boolean isDeleted = false;
	try 
	{
	if (formgroup.getId() != 0) 
	{
		int dupId = formgroup.getId();
		setUserId(request, formgroup);
		isDeleted = StudentGroupHandler.getInstance().deleteStudentGroup(dupId, false, formgroup);
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
		formgroup.setErrorMessage(msg);
		formgroup.setErrorStack(e.getMessage());
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	} 
	else 
	{
	throw e;
	}
	}
	formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
	if (isDeleted) {
		ActionMessage message = new ActionMessage("knowledgepro.studentGroup.deleteSucess", formgroup.getGroupName());
		messages.add("messages", message);
		saveMessages(request, messages);
		formgroup.reset(mapping, request);
	} else {
		errors.add("error", new ActionError("knowledgepro.studentGroup.deleteFailure",formgroup.getGroupName()));
		saveErrors(request, errors);
	}
	return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);
	}
   
	 public ActionForward reActivateStudentGroupDetails(ActionMapping mapping,
		  		 ActionForm form,
		  		 HttpServletRequest request,
		  		 HttpServletResponse response) throws Exception
	{
		 StudentGroupForm formgroup = (StudentGroupForm)form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		boolean isActivated = false;
	try 
	{
	if (formgroup.getDupId() != 0) 
	{
		int dupId = formgroup.getDupId();
		setUserId(request, formgroup);
		isActivated = StudentGroupHandler.getInstance().deleteStudentGroup(dupId, true, formgroup);
	}
	}
	catch (Exception e)
	{
		errors.add("error", new ActionError("knowledgepro.studentGroup.reactivationFailure"));
		saveErrors(request, errors);
	}
	formgroup.setStudentGroup(StudentGroupHandler.getInstance().getstudentGroup());
	if (isActivated) {
		ActionMessage message = new ActionMessage("knowledgepro.studentGroup.reactivationSucess");
		messages.add("messages", message);
		saveMessages(request, messages);
	}
	return mapping.findForward(CMSConstants.STUDENT_EXTENTION_GROUP);		
	}
}
