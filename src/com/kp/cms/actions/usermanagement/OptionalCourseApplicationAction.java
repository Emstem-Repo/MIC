package com.kp.cms.actions.usermanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.usermanagement.OptionalCourseApplication;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.usermanagement.OptionalCourseApplicationForm;
import com.kp.cms.handlers.admin.CasteHandler;
import com.kp.cms.handlers.usermanagement.OptionalCourseApplicationHandler;
import com.kp.cms.helpers.usermanagement.OptionalCourseApplicationHelper;
import com.kp.cms.to.admin.CasteTO;
import com.kp.cms.to.usermanagement.OptionalCourseApplicationTO;
import com.kp.cms.transactions.admin.ISingleFieldMasterTransaction;
import com.kp.cms.transactions.usermanagement.IOptionalCourseSubjectApplication;
import com.kp.cms.transactionsimpl.admin.SingleFieldMasterTransactionImpl;
import com.kp.cms.transactionsimpl.usermanagement.OptionalCourseApplicationTransactionImpl;

public class OptionalCourseApplicationAction extends BaseDispatchAction{

	private static final Log log = LogFactory.getLog(OptionalCourseApplicationAction.class);
	
	public ActionForward initOptionalCourseApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OptionalCourseApplicationForm applicationForm = (OptionalCourseApplicationForm)form;
		HttpSession session=request.getSession(true);
		int stuId = Integer.parseInt(session.getAttribute("studentId").toString());
		
		ISingleFieldMasterTransaction txn=SingleFieldMasterTransactionImpl.getInstance();
		Student student=(Student) txn.getMasterEntryDataById(Student.class,stuId);
		int courseId = student.getAdmAppln().getCourseBySelectedCourseId().getId();
		applicationForm.setStudentId(String.valueOf(student.getId()));
		applicationForm.setClassId(String.valueOf(student.getClassSchemewise().getClasses().getId()));
		applicationForm.setCourseId(String.valueOf(student.getAdmAppln().getCourseBySelectedCourseId().getId()));
		IOptionalCourseSubjectApplication transaction = OptionalCourseApplicationTransactionImpl.getInstance();
		Department department = transaction.getDepartment(courseId);
		if(department!=null){
			applicationForm.setDepartmentName(department.getName());
			applicationForm.setDepartmentId(String.valueOf(department.getId()));
		}
		Student st= transaction.getStudentDetails(stuId);
		if(student!=null){
			applicationForm.setStuName(student.getAdmAppln().getPersonalData().getFirstName());
			applicationForm.setRollNo(student.getRollNo());
			applicationForm.setRegisterNo(student.getRegisterNo());
			applicationForm.setCourseName(student.getAdmAppln().getCourse().getName());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dobString = sdf.format( student.getAdmAppln().getPersonalData().getDateOfBirth() );
			applicationForm.setDob(dobString);
			
		
		}
		
		
		setRequiredDataToForm(applicationForm);
		
		return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);
	}
	private void setRequiredDataToForm(OptionalCourseApplicationForm form) throws Exception{
		OptionalCourseApplicationHandler handler = OptionalCourseApplicationHandler.getInstance();
		List<OptionalCourseApplicationTO> list = handler.getOptionalCourseSubjects(form);
		form.setOptionalCourseApplicationTO(list);
		
		IOptionalCourseSubjectApplication tx = OptionalCourseApplicationTransactionImpl.getInstance();
		List<OptionalCourseApplication> existingOptionCourseList = tx.getExistingOptionalCourseSubjects(Integer.parseInt(form.getStudentId()),Integer.parseInt(form.getClassId()),Integer.parseInt(form.getCourseId()));
		form.setOptionCourseList(OptionalCourseApplicationHelper.getInstance().convertBoToTo1(existingOptionCourseList, form));
		
	}
	
	public ActionForward saveApplication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		OptionalCourseApplicationForm applicationForm = (OptionalCourseApplicationForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = applicationForm.validate(mapping, request);
		try{
			if(!applicationForm.getIsSubmitted()){
			List<OptionalCourseApplicationTO> toList = applicationForm.getOptionalCourseApplicationTO();
			List<OptionalCourseApplication> boList = new ArrayList<OptionalCourseApplication>();
			Iterator itr = toList.iterator();
			int count=0;
			ArrayList<String> a=new ArrayList<String>();
			HashSet<String> h=new HashSet<String>();
			
			while(itr.hasNext()){
				OptionalCourseApplicationTO to= (OptionalCourseApplicationTO)itr.next();
				if(to.getOption()!=null && !to.getOption().equalsIgnoreCase("")){
				a.add(to.getOption());
				h.add(to.getOption());
				count++;
				
				}
			}
			
			
		if(count==0){
			errors.add("error", new ActionError("knowledgepro.studentlogin.courseoption.select"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);
		}
		
		if(count!=3){
			errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.admission.empty.err.message","Please select all optional course subjects"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);
		}	
		
		if(a.size()!=h.size()){
			errors.add("error", new ActionError("knowledgepro.studentlogin.courseoption.duplicate"));
			saveErrors(request, errors);
			return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);
		}	
			if(errors.isEmpty()){
		      setUserId(request, applicationForm);
		      boolean isAdded = OptionalCourseApplicationHandler.getInstance().saveApplication(applicationForm);
		   if(isAdded){
			    applicationForm.setIsSubmitted(true);
			    ActionMessage message = new ActionMessage("knowledgepro.studentlogin.courseoption.addsuccess");
				messages.add("messages", message);
				saveMessages(request, messages);
				
				setRequiredDataToForm(applicationForm);
		   }
		   else
			  applicationForm.setIsSubmitted(false);
		     }
		}	
		}
		catch (Exception e) {
			if(e instanceof DuplicateException){
				/*errors.add("error", new ActionError("knowledgepro.admin.Caste.name.exists",casteName));
				saveErrors(request, errors);*/
				
				return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);	
			}
			log.error("Error occured in caste Entry Action", e);
			String msg = super.handleApplicationException(e);
			applicationForm.setErrorMessage(msg);
			applicationForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
			
		}
		setRequiredDataToForm(applicationForm);
		return mapping.findForward(CMSConstants.OPTIONAL_COURSE_ENTRY);
		
	}
	
//	public ActionForward printApplication(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		OptionalCourseApplicationForm applicationForm = (OptionalCourseApplicationForm)form;
//
//		IOptionalCourseSubjectApplication txn = OptionalCourseApplicationTransactionImpl.getInstance();
//		List<OptionalCourseApplication> existingOptionCourseList = txn.getExistingOptionalCourseSubjects(Integer.parseInt(applicationForm.getStudentId()),Integer.parseInt(applicationForm.getClassId()),Integer.parseInt(applicationForm.getCourseId()));
//		applicationForm.setOptionalCourseApplicationTO(OptionalCourseApplicationHelper.getInstance().convertBoToTo1(existingOptionCourseList, applicationForm));
//		
//		return mapping.findForward(CMSConstants.PRINT_OPTIONAL_COURSE_ENTRY);
//	}
	
}
