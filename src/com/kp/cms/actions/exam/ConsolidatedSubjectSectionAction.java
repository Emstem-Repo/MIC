package com.kp.cms.actions.exam;

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
import com.kp.cms.forms.exam.ConsolidatedSubjectSectionForm;
import com.kp.cms.handlers.exam.ConsolidatedSubjectSectionHandler;

@SuppressWarnings("deprecation")
public class ConsolidatedSubjectSectionAction extends BaseDispatchAction
{
	public ActionForward initConsolidatedSubjectSection(ActionMapping mapping,
			   										    ActionForm form,
			   										    HttpServletRequest request,
			   										    HttpServletResponse response) throws Exception
	{
		ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm = (ConsolidatedSubjectSectionForm) form;
		try
		{
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
			return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
		}
		catch(Exception ex)
		{
			String msg = super.handleApplicationException(ex);
			consolidatedSubjectSectionForm.setErrorMessage(msg);
			consolidatedSubjectSectionForm.setErrorStack(ex.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}		
	}
	public ActionForward submitConsolidatedSubjectSection(ActionMapping mapping,
		  		 										  ActionForm form,
		  		 										  HttpServletRequest request,
		  		 										  HttpServletResponse response) throws Exception
	{
		ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm = (ConsolidatedSubjectSectionForm) form;
		ActionErrors errors = consolidatedSubjectSectionForm.validate(mapping, request);
		ActionMessages messages = new ActionMessages();
		boolean isAdded = false;

		try
		{
			if(!errors.isEmpty())
			{
				saveErrors(request, errors);				
				consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
				if(consolidatedSubjectSectionForm.getSectionName().trim().equals("")) {
					consolidatedSubjectSectionForm.setSectionName(null);
				}
				return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
			}
						
			setUserId(request, consolidatedSubjectSectionForm);
			isAdded = ConsolidatedSubjectSectionHandler.getInstance().addSubjectSection(consolidatedSubjectSectionForm, "Add");
			
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
		}
		catch(DuplicateException ex)
		{
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.exists"));
			saveErrors(request, errors);			
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
			return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
		}
		catch (ReActivateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.reactivate"));
			saveErrors(request, errors);			
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
			return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
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
				consolidatedSubjectSectionForm.setErrorMessage(msg);
				consolidatedSubjectSectionForm.setErrorStack(ex.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} 
			else 
			{
				throw ex;
			}
		}
		
		if (isAdded) {
			ActionMessage message = new ActionMessage("knowledgepro.consolidatedSubjectSection.addSucess",consolidatedSubjectSectionForm.getSectionName());
			messages.add("messages", message);
			saveMessages(request, messages);
			consolidatedSubjectSectionForm.reset(mapping, request);
		}
		else
		{
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.addFailure",consolidatedSubjectSectionForm.getSectionName()));
			saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
	}
	
	public ActionForward updateConsolidatedSubjectSection(ActionMapping mapping,
	  		 											  ActionForm form,
	  		 											  HttpServletRequest request,
	  		 											  HttpServletResponse response) throws Exception
	{
		ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm = (ConsolidatedSubjectSectionForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = consolidatedSubjectSectionForm.validate(mapping, request);
		boolean isUpdated = false;
		try 
		{
			if (!errors.isEmpty()) 
			{
				saveErrors(request, errors);				
				consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
				if((consolidatedSubjectSectionForm.getSectionName().trim()).isEmpty()){       
				consolidatedSubjectSectionForm.setSectionName(null);
				}
				request.setAttribute("admOperation", "edit");
				return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
			}
			
			setUserId(request, consolidatedSubjectSectionForm);
			isUpdated = ConsolidatedSubjectSectionHandler.getInstance().addSubjectSection(consolidatedSubjectSectionForm, "Edit");
		} 
		catch (DuplicateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.reactivate"));
			saveErrors(request, errors);
			
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
		}
		catch (ReActivateException e1) 
		{
			errors.add("error", new ActionError("knowledgepro.collegeMasterEntry.reactivate"));
			saveErrors(request, errors);			
			consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
			request.setAttribute("admOperation", "edit");
			return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
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
				consolidatedSubjectSectionForm.setErrorMessage(msg);
				consolidatedSubjectSectionForm.setErrorStack(e.getMessage());
				request.setAttribute("admOperation", "edit");
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} 
			else
			{
				throw e;
			}
		}
		
		consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
		if (isUpdated) {
			ActionMessage message = new ActionMessage("knowledgepro.consolidatedSubjectSection.updateSucess", consolidatedSubjectSectionForm.getSectionName());
			messages.add("messages", message);
			saveMessages(request, messages);
			consolidatedSubjectSectionForm.reset(mapping, request);
		} 
		else {
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.updateFailure", consolidatedSubjectSectionForm.getSectionName()));
			saveErrors(request, errors);
		}
		request.setAttribute("admOperation", "add");
		return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
	}
	
	public ActionForward deleteConsolidatedSubjectStection(ActionMapping mapping,
				 										   ActionForm form,
				 										   HttpServletRequest request,
				 										   HttpServletResponse response) throws Exception
	{
		ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm = (ConsolidatedSubjectSectionForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		boolean isDeleted = false;
		try 
		{
			if (consolidatedSubjectSectionForm.getId() != 0) 
			{
				int dupId = consolidatedSubjectSectionForm.getId();
				setUserId(request, consolidatedSubjectSectionForm);
				isDeleted = ConsolidatedSubjectSectionHandler.getInstance().deleteSubjectSection(dupId, false, consolidatedSubjectSectionForm);
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
				consolidatedSubjectSectionForm.setErrorMessage(msg);
				consolidatedSubjectSectionForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} 
			else 
			{
				throw e;
			}
		}
		consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
		if (isDeleted) {
			ActionMessage message = new ActionMessage("knowledgepro.consolidatedSubjectSection.deleteSucess", consolidatedSubjectSectionForm.getSectionName());
			messages.add("messages", message);
			saveMessages(request, messages);
			consolidatedSubjectSectionForm.reset(mapping, request);
		}
		else {
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.deleteFailure", consolidatedSubjectSectionForm.getSectionName()));
			saveErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);
	}
	
	public ActionForward reActivateConsolidatedSubjectSection(ActionMapping mapping,
		  		 											  ActionForm form,
		  		 											  HttpServletRequest request,
		  		 											  HttpServletResponse response) throws Exception
	{
		ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm = (ConsolidatedSubjectSectionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		boolean isActivated = false;
		try 
		{
			if (consolidatedSubjectSectionForm.getDupId() != 0) 
			{
				int dupId = consolidatedSubjectSectionForm.getDupId();
				setUserId(request, consolidatedSubjectSectionForm);
				isActivated = ConsolidatedSubjectSectionHandler.getInstance().deleteSubjectSection(dupId, true, consolidatedSubjectSectionForm);
			}
		}
		catch (Exception e)
		{
			errors.add("error", new ActionError("knowledgepro.consolidatedSubjectSection.reactivationFailure"));
			saveErrors(request, errors);
		}
		
		consolidatedSubjectSectionForm.setSubjectSections(ConsolidatedSubjectSectionHandler.getInstance().getSubjectSections());
		if (isActivated) {
			ActionMessage message = new ActionMessage("knowledgepro.consolidatedSubjectSection.reactivationSucess");
			messages.add("messages", message);
			saveMessages(request, messages);
		}
		return mapping.findForward(CMSConstants.CONSOLIDATED_SUBJECT_SECTION_MASTER);		
	}
}
