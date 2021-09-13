package com.kp.cms.actions.admission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admission.StudentSemesterFeeCorrectionForm;
import com.kp.cms.handlers.admission.AdmissionFormHandler;

public class StudentSemsterFeeCorrectionAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(StudentSemsterFeeCorrectionAction.class);
	private static final String OTHER="other";
	private static final String TO_DATEFORMAT="MM/dd/yyyy";
	private static final String FROM_DATEFORMAT="dd/MM/yyyy";
	private static final String SQL_DATEFORMAT="dd-MMM-yyyy";
	
	
	public ActionForward initStudentSemesterFeeCorrection(ActionMapping mapping, ActionForm form,  HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
		
		StudentSemesterFeeCorrectionForm studentSemesterFeeCorrectionForm = (StudentSemesterFeeCorrectionForm)form;
		studentSemesterFeeCorrectionForm.setTxnRefNo(null);
		studentSemesterFeeCorrectionForm.setTxnRefNo(null);
		studentSemesterFeeCorrectionForm.setTxnAmt(null);
		setUserId(request, studentSemesterFeeCorrectionForm);
		
		return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_CORRECTION);
		
	}
	
	public ActionForward pendingStudentSemesterFee(ActionMapping mapping, ActionForm form,  HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
		StudentSemesterFeeCorrectionForm feeCorrectionForm = (StudentSemesterFeeCorrectionForm)form;
		ActionMessages errors= new ActionMessages();
		try {
			errors=feeCorrectionForm.validate(mapping, request);
			if(feeCorrectionForm.getCorrectionFor().equalsIgnoreCase("Student Semester Fee")) {
				if(feeCorrectionForm.getTxnDate() == null ||feeCorrectionForm.getTxnDate().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.inventory.date.required"));
				}
				if(feeCorrectionForm.getTxnAmt() == null ||feeCorrectionForm.getTxnAmt().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.admin.FineCategoryamount.required"));
				}
			}
			
			if(errors.isEmpty()){
				if(feeCorrectionForm.getCorrectionFor().equalsIgnoreCase("SelfFinancing Semester Fee")){
					boolean isUpdated = AdmissionFormHandler.getInstance().getPendingStudentSemesterFee(feeCorrectionForm);
					if(isUpdated){
						ActionMessages messages = new ActionMessages();
						ActionMessage message = new ActionMessage("knowledgepro.admission.empty.error.message","Payment successfully updated.");
						messages.add("messages", message);
						saveMessages(request, messages);
						
					}else {
						errors.add("knowledgepro.admission.empty.error.message", new ActionError("knowledgepro.admission.empty.error.message","No pending records are found."));
						saveErrors(request, errors);
					}
					
					
				}
				if(feeCorrectionForm.getCorrectionFor().equalsIgnoreCase("Regular Semester Fee")){
					boolean isUpdated = AdmissionFormHandler.getInstance().getPendingStudentSpecialFee(feeCorrectionForm);
					if(isUpdated){
						ActionMessages messages = new ActionMessages();
						ActionMessage message = new ActionMessage("knowledgepro.admission.empty.error.message","Payment successfully updated.");
						messages.add("messages", message);
						saveMessages(request, messages);
						
					}else {
						errors.add("knowledgepro.admission.empty.error.message", new ActionError("knowledgepro.admission.empty.error.message","No pending records are found."));
						saveErrors(request, errors);
					}
					
					
				}
				
			}else {
				saveErrors(request, errors);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("error in init getPendingOnlineApp...",e);
				throw e;
		}
		
		return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_CORRECTION);
	}
}
