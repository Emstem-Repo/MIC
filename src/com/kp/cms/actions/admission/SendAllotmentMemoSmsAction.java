package com.kp.cms.actions.admission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admission.SendAllotmentMemoSmsForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admission.SendAllotmentMemoSmsHandler;
import com.kp.cms.handlers.exam.GradeDefinitionBatchWiseHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.transactions.admin.IGeneratePasswordTransaction;
import com.kp.cms.transactionsimpl.admin.GeneratePasswordTransactionImpl;

public class SendAllotmentMemoSmsAction extends BaseDispatchAction {
	final Log log = LogFactory.getLog(SendAllotmentMemoSmsAction.class);
	public ActionForward initSendingMemoSms(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		
		GradeDefinitionBatchWiseHandler handler = new GradeDefinitionBatchWiseHandler();
		SendAllotmentMemoSmsForm sbForm = (SendAllotmentMemoSmsForm)form;
		try {
			// initialize program type
			setUserId(request, sbForm);
			sbForm.setProgramTypeId(null);
			sbForm.setProgramId(null);
			sbForm.setSendMail(null);
			sbForm.setIsSureMemo(true);
			List<ProgramTypeTO> programTypeList = ProgramTypeHandler.getInstance().getProgramType();
			sbForm.setProgramTypeList(programTypeList);
			sbForm.setListCourseName(handler.getListExamCourseUtil());
		}catch (ApplicationException e) {
			log.error("error in init application detail page...",e);
			
				String msg = super.handleApplicationException(e);
				sbForm.setErrorMessage(msg);
				sbForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		catch (Exception e) {
			log.error("error in init application detail page...",e);
				throw e;
		}
		return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_SMS);
	}

	public ActionForward SendingAllotmentMemoSms(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ActionMessage message=null;
		ActionMessages messages = new ActionMessages();
		SendAllotmentMemoSmsForm sbForm = (SendAllotmentMemoSmsForm)form;
		int year=0;
		int progid=0;
		try{
			ArrayList<Integer> listCourses = new ArrayList<Integer>();
			String[] tempCommaSepVal =sbForm.getCourseIds();
			Set<Integer> tempcourseset=null;
			if(tempCommaSepVal!=null && !tempCommaSepVal[0].equalsIgnoreCase("")){
				tempcourseset=new HashSet<Integer>();
				for(String str:tempCommaSepVal){
					if(str!=null)
					tempcourseset.add(Integer.parseInt(str));
				}
				
				
			}
			ActionErrors errors = sbForm.validate(mapping, request);
			
			if(tempCommaSepVal==null ){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}else if(tempCommaSepVal!=null && tempCommaSepVal.length==0){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}
			
			if(tempcourseset==null){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}
			if(sbForm.getSureOrChanceMemoNo()==null){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Chance or Sure No. required"));
				saveErrors(request, errors);
			}
			
			if (errors.isEmpty()) {		
			if (sbForm.getYear() != null
					&& !StringUtils.isEmpty(sbForm.getYear().trim())
					&& StringUtils.isNumeric(sbForm.getYear())) {
				year = Integer.parseInt(sbForm.getYear());
			}
			if (sbForm.getProgramId() != null
					&& !StringUtils.isEmpty(sbForm.getProgramId().trim())
					&& StringUtils.isNumeric(sbForm.getProgramId())) {
				progid = Integer.parseInt(sbForm.getProgramId());
			}
			List<Student> studentList = new ArrayList<Student>();
			studentList = SendAllotmentMemoSmsHandler.getInstance().getStudents(sbForm,tempcourseset);
				
				
			
			
				boolean isMsgSent=false;
			if(studentList!=null && studentList.size()!=0){
				
			
					isMsgSent=SendAllotmentMemoSmsHandler.getInstance().sendSMS(sbForm,studentList);
					if(isMsgSent)
					{
						message = new ActionMessage(
								CMSConstants.NEW_SMS_SEND_SUCCESS);
						messages.add(CMSConstants.MESSAGES, message);
						addMessages(request, messages);
						sbForm.setCourseIds(null);
					}
					else
					{
						sbForm.setCourseIds(null);
						//message = new ActionMessage(CMSConstants.NEW_SMS_SEND_FAILED);
						//messages.add(CMSConstants.MESSAGES, message);
						//addMessages(request, messages);
						errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.NEW_SMS_SEND_FAILED));
						saveErrors(request, errors);
						sbForm.setCourseIds(null);
						return	mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_SMS);
					}
			}	else{
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.norecords"));
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_SMS);
				
			}
			}else{
				saveErrors(request, errors);
				//bcz of this code u wil get java.util.ConcurrentModificationException
				//addErrors(request, errors);
				
				return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_SMS);
			}
		} catch (Exception e) {
			log.debug("*********** MESSAGE IN LOG DEBUG TIME ********************"+e.getMessage());
			throw e;
			
		}
	return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_SMS);

		
	}

	public ActionForward initSendingMemoMail(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		
		GradeDefinitionBatchWiseHandler handler = new GradeDefinitionBatchWiseHandler();
		SendAllotmentMemoSmsForm sbForm = (SendAllotmentMemoSmsForm)form;
		try {
			// initialize program type
			setUserId(request, sbForm);
			sbForm.setProgramTypeId(null);
			sbForm.setProgramId(null);
			sbForm.setSendMail(null);
			sbForm.setIsSureMemo(true);
			List<ProgramTypeTO> programTypeList = ProgramTypeHandler.getInstance().getProgramType();
			sbForm.setProgramTypeList(programTypeList);
			sbForm.setListCourseName(handler.getListExamCourseUtil());
		}catch (ApplicationException e) {
			log.error("error in init application detail page...",e);
			
				String msg = super.handleApplicationException(e);
				sbForm.setErrorMessage(msg);
				sbForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		catch (Exception e) {
			log.error("error in init application detail page...",e);
				throw e;
		}
		return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_MAIL);
	}

	
	
	public ActionForward SendingAllotmentMemoMail(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ActionMessage message=null;
		ActionMessages messages = new ActionMessages();
		SendAllotmentMemoSmsForm sbForm = (SendAllotmentMemoSmsForm)form;
		int year=0;
		int progid=0;
		try{
			ArrayList<Integer> listCourses = new ArrayList<Integer>();
			String[] tempCommaSepVal =sbForm.getCourseIds();
			Set<Integer> tempcourseset=null;
			if(tempCommaSepVal!=null && !tempCommaSepVal[0].equalsIgnoreCase("")){
				tempcourseset=new HashSet<Integer>();
				for(String str:tempCommaSepVal){
					if(str!=null)
					tempcourseset.add(Integer.parseInt(str));
				}
				
				
			}
			ActionErrors errors = sbForm.validate(mapping, request);
			
			if(tempCommaSepVal==null ){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}else if(tempCommaSepVal!=null && tempCommaSepVal.length==0){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}
			
			if(tempcourseset==null){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Atleast one Course"));
				saveErrors(request, errors);
			}
			
			if (errors.isEmpty()) {		
			if (sbForm.getYear() != null
					&& !StringUtils.isEmpty(sbForm.getYear().trim())
					&& StringUtils.isNumeric(sbForm.getYear())) {
				year = Integer.parseInt(sbForm.getYear());
			}
			if (sbForm.getProgramId() != null
					&& !StringUtils.isEmpty(sbForm.getProgramId().trim())
					&& StringUtils.isNumeric(sbForm.getProgramId())) {
				progid = Integer.parseInt(sbForm.getProgramId());
			}
			List<Student> studentList = new ArrayList<Student>();
			studentList = SendAllotmentMemoSmsHandler.getInstance().getStudentsForMail(sbForm,tempcourseset);
				
				
			
			
				boolean isMailSent=false;
			if(studentList!=null && studentList.size()!=0){
				
			
				isMailSent=SendAllotmentMemoSmsHandler.getInstance().sendMail(sbForm,studentList);
					if(isMailSent)
					{
						message = new ActionMessage(
								CMSConstants.NEW_SMS_SEND_SUCCESS);
						messages.add(CMSConstants.MESSAGES, message);
						addMessages(request, messages);
						sbForm.setCourseIds(null);
					}
					else
					{
						sbForm.setCourseIds(null);
						//message = new ActionMessage(CMSConstants.NEW_SMS_SEND_FAILED);
						//messages.add(CMSConstants.MESSAGES, message);
						//addMessages(request, messages);
						errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.NEW_SMS_SEND_FAILED));
						saveErrors(request, errors);
						sbForm.setCourseIds(null);
						return	mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_MAIL);
					}
			}	else{
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.norecords"));
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_MAIL);
				
			}
			}else{
				saveErrors(request, errors);
				//bcz of this code u wil get java.util.ConcurrentModificationException
				//addErrors(request, errors);
				
				return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_MAIL);
			}
		} catch (Exception e) {
			log.debug("*********** MAIL IN LOG DEBUG TIME ********************"+e.getMessage());
			throw e;
			
		}
	return mapping.findForward(CMSConstants.SEND_ALLOTMENT_MEMO_MAIL);

		
	}


}
