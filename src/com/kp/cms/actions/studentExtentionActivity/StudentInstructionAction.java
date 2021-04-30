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
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.studentExtentionActivity.StudentInstructionForm;
import com.kp.cms.handlers.studentExtentionActivity.StudentInstructionHandler;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;

public class StudentInstructionAction extends BaseDispatchAction {
	public ActionForward initStudentInstruction(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		StudentInstructionForm stuFeedbackInstructionsForm = (StudentInstructionForm)form;
		stuFeedbackInstructionsForm.reset(mapping, request);
		setStuFeedbackInstructions(stuFeedbackInstructionsForm);
   		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_INSTRUCTION);
	}
	
	
	private void setStuFeedbackInstructions( StudentInstructionForm stuFeedbackInstructionsForm) throws Exception{
		List<StudentInstructionTO> instructionsList = StudentInstructionHandler.getInstance().getInstructions();
			stuFeedbackInstructionsForm.setStuFeedbackInsToList(instructionsList);
	}

	
	public ActionForward addStuFeedbackInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		StudentInstructionForm stuFeedbackInstructionsForm = (StudentInstructionForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = stuFeedbackInstructionsForm.validate(mapping, request);
		setUserId(request, stuFeedbackInstructionsForm);
		validateDescription(stuFeedbackInstructionsForm,errors);
			try{
			if(errors!=null && errors.isEmpty()){
				boolean isExist = StudentInstructionHandler.getInstance().checkDuplicateInstructions(stuFeedbackInstructionsForm);
				if(!isExist){
					boolean isAdded= StudentInstructionHandler.getInstance().addStuFeedbackInstructions(stuFeedbackInstructionsForm,"add");
					if(isAdded){
						ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.AddedSuccess");
						messages.add("messages", message);
						saveMessages(request, messages);
						stuFeedbackInstructionsForm.reset(mapping, request);	
					}else{
						
						ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.AddedFailure");
						messages.add("messages", message);
						saveMessages(request, messages);
						stuFeedbackInstructionsForm.reset(mapping, request);	
					}
				}else{
					errors.add("CMSConstants.ERROR", new ActionError("knowledgepro.admin.stufeedback.instructions.Exist"));
					saveErrors(request, errors);
				}
			}else{
				errors.add(messages);
				saveErrors(request, errors);
			}
			}catch (BusinessException businessException) {
				String msgKey = super.handleBusinessException(businessException);
				ActionMessage message = new ActionMessage(msgKey);
				messages.add(CMSConstants.MESSAGES, message);
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			} catch (Exception exception) {	
				String msg = super.handleApplicationException(exception);
				stuFeedbackInstructionsForm.setErrorMessage(msg);
				stuFeedbackInstructionsForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
				setStuFeedbackInstructions(stuFeedbackInstructionsForm);
				return mapping.findForward(CMSConstants.STUDENT_EXTENTION_INSTRUCTION);
			}	
	
	
  public ActionForward editStuFeedbackInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	  StudentInstructionForm stuFeedbackInstructionsForm = (StudentInstructionForm)form;
		ActionMessages messages = new ActionMessages();
		setUserId(request, stuFeedbackInstructionsForm);
	try{
		StudentInstructionHandler.getInstance().editFeedbackInstructions(stuFeedbackInstructionsForm);
		request.setAttribute("instructions", "edit");
	}catch (BusinessException businessException) {
		String msgKey = super.handleBusinessException(businessException);
		ActionMessage message = new ActionMessage(msgKey);
		messages.add(CMSConstants.MESSAGES, message);
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	} catch (Exception exception) {	
		String msg = super.handleApplicationException(exception);
		stuFeedbackInstructionsForm.setErrorMessage(msg);
		stuFeedbackInstructionsForm.setErrorStack(exception.getMessage());
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	}
		setStuFeedbackInstructions(stuFeedbackInstructionsForm);
		return mapping.findForward(CMSConstants.STUDENT_EXTENTION_INSTRUCTION);
	}
  public ActionForward deleteStuFeedbackInstructions(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	  StudentInstructionForm stuFeedbackInstructionsForm = (StudentInstructionForm)form;
	ActionMessages messages = new ActionMessages();
	setUserId(request, stuFeedbackInstructionsForm);
	ActionErrors errors = stuFeedbackInstructionsForm.validate(mapping, request);
	try{
		if(errors.isEmpty()){
			boolean isDeleted = StudentInstructionHandler.getInstance().deleteInstructions(stuFeedbackInstructionsForm);
			if(isDeleted){
				ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.DeleteSuccess");
				messages.add("messages", message);
				saveMessages(request, messages);
				stuFeedbackInstructionsForm.reset(mapping, request);	
			}else{
				ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.DeleteFailure");
				messages.add("messages", message);
				saveMessages(request, messages);
				stuFeedbackInstructionsForm.reset(mapping, request);	
			}
		}else{
			errors.add(messages);
			saveErrors(request, errors);
		}
	}catch (BusinessException businessException) {
		String msgKey = super.handleBusinessException(businessException);
		ActionMessage message = new ActionMessage(msgKey);
		messages.add(CMSConstants.MESSAGES, message);
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	} catch (Exception exception) {	
		String msg = super.handleApplicationException(exception);
		stuFeedbackInstructionsForm.setErrorMessage(msg);
		stuFeedbackInstructionsForm.setErrorStack(exception.getMessage());
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	}
	setStuFeedbackInstructions(stuFeedbackInstructionsForm);
	return mapping.findForward(CMSConstants.STUDENT_EXTENTION_INSTRUCTION);
	}
		
  public ActionForward updateStuFeedbackInstructions(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	  StudentInstructionForm stuFeedbackInstructionsForm = (StudentInstructionForm)form;
	ActionMessages messages = new ActionMessages();
	ActionErrors errors = stuFeedbackInstructionsForm.validate(mapping, request);
	setUserId(request, stuFeedbackInstructionsForm);
	validateDescription(stuFeedbackInstructionsForm,errors);
	try{
		if(errors.isEmpty()){
			boolean isUpdated = StudentInstructionHandler.getInstance().addStuFeedbackInstructions(stuFeedbackInstructionsForm,"edit");
			if(isUpdated){
				ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.UpdatedSuccess");
				messages.add("messages", message);
				saveMessages(request, messages);
				stuFeedbackInstructionsForm.reset(mapping, request);	
			}else{
				ActionMessage message = new ActionMessage("knowledgepro.admin.stufeedback.instructions.UpdatedFailure");
				messages.add("messages", message);
				saveMessages(request, messages);
				stuFeedbackInstructionsForm.reset(mapping, request);	
			}
		}else{
			errors.add(messages);
			request.setAttribute("instructions", "edit");
			saveErrors(request, errors);
		}
	}catch (BusinessException businessException) {
		String msgKey = super.handleBusinessException(businessException);
		ActionMessage message = new ActionMessage(msgKey);
		messages.add(CMSConstants.MESSAGES, message);
		request.setAttribute("instructions", "edit");
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	} catch (Exception exception) {	
		String msg = super.handleApplicationException(exception);
		stuFeedbackInstructionsForm.setErrorMessage(msg);
		stuFeedbackInstructionsForm.setErrorStack(exception.getMessage());
		request.setAttribute("instructions", "edit");
		return mapping.findForward(CMSConstants.ERROR_PAGE);
	}
	setStuFeedbackInstructions(stuFeedbackInstructionsForm);
	return mapping.findForward(CMSConstants.STUDENT_EXTENTION_INSTRUCTION);
	} 
		private void validateDescription( StudentInstructionForm stuFeedbackInstructionsForm, ActionErrors errors)throws Exception {
			if(stuFeedbackInstructionsForm.getDescription()!=null && !stuFeedbackInstructionsForm.getDescription().isEmpty() && stuFeedbackInstructionsForm.getDescription().length()>2000){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.onlineleaveins.description.length"));
			}
			
		}
	
}
