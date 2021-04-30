package com.kp.cms.actions.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
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
import com.kp.cms.forms.admin.SendBulkSmsToStudentParentsNewForm;
import com.kp.cms.forms.attendance.NewAttendanceSmsForm;
import com.kp.cms.handlers.admin.DepartmentEntryHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admin.SendBulkSmsToStudentParentsNewHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.attendance.NewAttendanceSmsHandler;
import com.kp.cms.handlers.exam.GradeDefinitionBatchWiseHandler;
import com.kp.cms.to.admin.EmployeeTO;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.admin.IGeneratePasswordTransaction;
import com.kp.cms.transactionsimpl.admin.GeneratePasswordTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.AcademicyearTransactionImpl;

public class SendBulkSmsToStudentParentsNew extends BaseDispatchAction {
	final Log log = LogFactory.getLog(SendBulkSmsToStudentParentsNew.class);
	
	
	public ActionForward initSendingBulkSms(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GradeDefinitionBatchWiseHandler handler = new GradeDefinitionBatchWiseHandler();
		SendBulkSmsToStudentParentsNewForm sbForm = (SendBulkSmsToStudentParentsNewForm)form;
		try {
			// initialize program type
			setUserId(request, sbForm);
			sbForm.setProgramTypeId(null);
			sbForm.setProgramId(null);
			sbForm.setDepartmentIds(null);
			sbForm.setSendMail(null);
			sbForm.setIsStudent(true);
			sbForm.setSendTo(null);
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
		return mapping.findForward(CMSConstants.SEND_BULK_SMS);
	}
	
	
	public ActionForward SendingBulkSms(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionMessage message=null;
		ActionMessages messages = new ActionMessages();
		SendBulkSmsToStudentParentsNewForm sbForm = (SendBulkSmsToStudentParentsNewForm)form;
		IGeneratePasswordTransaction txn = new GeneratePasswordTransactionImpl();
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
		List<Student> studentList = txn.getStudents(year,progid,tempcourseset);
		
			boolean isMsgSent=false;
		if(studentList!=null && studentList.size()!=0){
			
		
				isMsgSent=SendBulkSmsToStudentParentsNewHandler.getInstance().sendSMS(sbForm,studentList);
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
					return	mapping.findForward(CMSConstants.SEND_BULK_SMS);
				}
		}	else{
			errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.norecords"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.SEND_BULK_SMS);
			
		}
		}else{
			saveErrors(request, errors);
			//bcz of this code u wil get java.util.ConcurrentModificationException
			//addErrors(request, errors);
			
			return mapping.findForward(CMSConstants.SEND_BULK_SMS);
		}
			
			} catch (Exception e) {
				log.debug("*********** MESSAGE IN LOG DEBUG TIME ********************"+e.getMessage());
				throw e;
				
			}
		return mapping.findForward(CMSConstants.SEND_BULK_SMS);
	}
	
	
	public ActionForward initSendingBulkSmsForEmployeeAndStudent(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GradeDefinitionBatchWiseHandler handler = new GradeDefinitionBatchWiseHandler();
		SendBulkSmsToStudentParentsNewForm sbForm = (SendBulkSmsToStudentParentsNewForm)form;
		try {
			
			// initialize program type
			setUserId(request, sbForm);
			sbForm.setProgramTypeId(null);
			sbForm.setProgramId(null);
			sbForm.setParent("false");
			
			sbForm.setYear(AcademicyearTransactionImpl.getInstance().getCurrentAcademicYearforTeacher()+"");
			sbForm.setSendTo(null);
			sbForm.setDepartmentIds(null);
			Map<Integer, String> deptMap = DepartmentEntryHandler.getInstance().getDepartment();
			sbForm.setDepartmentMap(deptMap);
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
		return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
	}
	
	public ActionForward getStudentsOrTeachers(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		SendBulkSmsToStudentParentsNewForm sendstuteaform=(SendBulkSmsToStudentParentsNewForm)form;
		ActionMessage message;
		ActionMessages messages=new ActionMessages();
		ActionErrors errors=new ActionErrors();
		ActionError error;
		sendstuteaform.setMessage(null);
		
		errors=sendstuteaform.validate(mapping, request);
		try
		{
			if(sendstuteaform.getSendTo().equalsIgnoreCase("studentWise")){
				if(sendstuteaform.getYear()==null||sendstuteaform.getYear()==" "){
				error= new ActionError("accademic year required");
				errors.add("errors", new ActionError("knowledgepro.accadyear.required"));
				}
				if(sendstuteaform.getClassId()==null||sendstuteaform.getClassId().isEmpty()){
					error= new ActionError("accademic year required");
					errors.add("errors", new ActionError("knowledgepro.class.required"));
					}
			}

			if(sendstuteaform.getSendTo().equalsIgnoreCase("teachingWise")){
				if(sendstuteaform.getDepartmentIds()==null){
				error= new ActionError("accademic year required");
				errors.add("errors", new ActionError("knowledgepro.department.required"));
				}
			}
			if(sendstuteaform.getSendTo().equals("studentWise"))
			{
				if(errors.isEmpty())
				{
					SendBulkSmsToStudentParentsNewHandler.getInstance().getStudents(sendstuteaform);
					boolean isMsgSent=false;
					if (sendstuteaform.getStudentList() == null || sendstuteaform.getStudentList().size()<=0) 
					{
						message = new ActionMessage(
								CMSConstants.ATTENDANCE_ENTRY_NORECORD);
						messages.add(CMSConstants.MESSAGES, message);
						addMessages(request, messages);
						return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
					}
				}
				else
				{
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
				
				}
			}
			if(sendstuteaform.getSendTo().equals("teachingWise")||sendstuteaform.getSendTo().equals("nonteachingWise"))
			{
				if(errors.isEmpty())
				{
					if(sendstuteaform.getSendTo().equals("teachingWise"))
					{
						SendBulkSmsToStudentParentsNewHandler.getInstance().getTeacher(sendstuteaform);
					}
					else if(sendstuteaform.getSendTo().equals("nonteachingWise"))
					{
						SendBulkSmsToStudentParentsNewHandler.getInstance().getNonTeacher(sendstuteaform);
					}
					boolean isMsgSent=false;
					if (sendstuteaform.getEmployeeList() == null || sendstuteaform.getEmployeeList().size()<=0) 
					{
						message = new ActionMessage(
								CMSConstants.ATTENDANCE_ENTRY_NORECORD);
						messages.add(CMSConstants.MESSAGES, message);
						addMessages(request, messages);
						return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
					}
				}
				else
				{
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
				
				}
			}
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
		return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT1);
	}
	
	public ActionForward sendSms(ActionMapping mapping, ActionForm form,	HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
		SendBulkSmsToStudentParentsNewForm sendBulkSms=(SendBulkSmsToStudentParentsNewForm)form;
		ActionMessages messages=new ActionMessages();
		ActionMessage message=null;
		ActionErrors errors=new ActionErrors();
		try
		{
			
			boolean isMessageSent=false;
			if(sendBulkSms.getStudentList()!=null&&!sendBulkSms.getStudentList().isEmpty()&&sendBulkSms.getSendTo().equalsIgnoreCase("studentWise"))
			{
				if(sendBulkSms.getMessage()==null||sendBulkSms.getMessage().isEmpty()){
					SendBulkSmsToStudentParentsNewHandler.getInstance().getStudents(sendBulkSms);
					errors.add("errors", new ActionError("knowledgepro.message.required"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT1);
					}
				Iterator<StudentTO> it=sendBulkSms.getStudentList().iterator();
				StudentTO to;
				boolean flag=true;
				while(it.hasNext())
				{
					to=it.next();
					if(to.isChecked())
					{
						flag=false;
						break;
					}
				}
				if(flag)
				{
					SendBulkSmsToStudentParentsNewHandler.getInstance().getStudents(sendBulkSms);
					errors.add("errors", new ActionError("knowledgepro.message.required"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT1);
				}
				isMessageSent=SendBulkSmsToStudentParentsNewHandler.getInstance().sendSMSToStudents(sendBulkSms, sendBulkSms.getStudentList());
			}
			if(sendBulkSms.getEmployeeList()!=null&&!sendBulkSms.getEmployeeList().isEmpty()&&sendBulkSms.getSendTo().equalsIgnoreCase("teachingWise")||sendBulkSms.getSendTo().equalsIgnoreCase("nonteachingWise"))
			{
				if(sendBulkSms.getMessage()==null||sendBulkSms.getMessage().isEmpty()){
					if(sendBulkSms.getSendTo()=="teachingWise")
					{
					SendBulkSmsToStudentParentsNewHandler.getInstance().getTeacher(sendBulkSms);
					}
					if(sendBulkSms.getSendTo()=="nonteachingWise")
					{
						SendBulkSmsToStudentParentsNewHandler.getInstance().getNonTeacher(sendBulkSms);
					}
					errors.add("errors", new ActionError("knowledgepro.message.required"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT1);
					}
				Iterator<EmployeeTO> it=sendBulkSms.getEmployeeList().iterator();
				EmployeeTO to;
				boolean flag=true;
				while(it.hasNext())
				{
					to=it.next();
					if(to.isChecked())
					{
						flag=false;
						break;
					}
				}
				if(flag)
				{
					if(sendBulkSms.getSendTo()=="teachingWise")
					{
					SendBulkSmsToStudentParentsNewHandler.getInstance().getTeacher(sendBulkSms);
					}
					if(sendBulkSms.getSendTo()=="nonteachingWise")
					{
						SendBulkSmsToStudentParentsNewHandler.getInstance().getNonTeacher(sendBulkSms);
					}
					errors.add("errors", new ActionError("knowledgepro.checkbox.employee.required"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT1);
				}
				isMessageSent=SendBulkSmsToStudentParentsNewHandler.getInstance().sendSMSToTeacherOrNonTeacher(sendBulkSms, sendBulkSms.getEmployeeList());
			}
			if(isMessageSent)
			{
				message = new ActionMessage(
						CMSConstants.NEW_SMS_SEND_SUCCESS);
				messages.add(CMSConstants.MESSAGES, message);
				addMessages(request, messages);
			}
			else
			{
				message = new ActionMessage(
						CMSConstants.NEW_SMS_SEND_FAILED);
				messages.add(CMSConstants.MESSAGES, message);
				addMessages(request, messages);
				setRequiredDataToForm(sendBulkSms,request);
				return	mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
			}
			
		}				
		 catch (Exception e) {
			log.debug(e.getMessage());
			throw e;
			
		}
		setRequiredDataToForm(sendBulkSms,request);

		return mapping.findForward(CMSConstants.SEND_BULK_SMS_EMPLOYEE_STUDENT);
}
	private void setRequiredDataToForm(SendBulkSmsToStudentParentsNewForm sendBulkSms,HttpServletRequest request) throws Exception 
	{
		GradeDefinitionBatchWiseHandler handler = new GradeDefinitionBatchWiseHandler();
		Map<Integer, String> deptMap = DepartmentEntryHandler.getInstance().getDepartment();
		sendBulkSms.setDepartmentMap(deptMap);
		sendBulkSms.setSendTo(null);
		sendBulkSms.setDepartmentIds(null);
		sendBulkSms.setParent("false");
		sendBulkSms.setYear(new AcademicyearTransactionImpl().getCurrentAcademicYearforTeacher()+"");
		sendBulkSms.setListCourseName(handler.getListExamCourseUtil());
	}	

}
