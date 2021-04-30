package com.kp.cms.actions.fee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentSemesterFeeDetails;
import com.kp.cms.bo.exam.StudentUtilBO;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.fee.StudentSemesterFeeDetailsForm;
import com.kp.cms.handlers.fee.StudentSemesterFeeDetailsHandler;
import com.kp.cms.helpers.fee.StudentSemesterFeeDetailsHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;
import com.kp.cms.utilities.CurrentAcademicYear;



public class StudentSemesterFeeDetailsAction  extends BaseDispatchAction{
	 private static Log log = LogFactory.getLog(StudentSemesterFeeDetailsAction.class);
	 
	 public ActionForward initStudentSemesterFeeDetails(ActionMapping mapping,ActionForm form
			 ,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		 log.info("Entered initStudentSemesterFeeDetails input");
			StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm = (StudentSemesterFeeDetailsForm) form;
			studentSemesterFeeDetailsForm.resetFields();
			setRequiredDataToForm(studentSemesterFeeDetailsForm,request);
			log.info("Exit initStudentSemesterFeeDetails input");
			
			return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
	 }
	 
	 public void setRequiredDataToForm(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm,HttpServletRequest request)
		throws Exception {
			int academicYear = CurrentAcademicYear.getInstance().getAcademicyear();
			
		}
	 
	 public ActionForward getStudent(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response
			 ) throws Exception {
		 
		 log.info("studentSemesterFeeDetails--getCandidates");
		 StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm = (StudentSemesterFeeDetailsForm) form;
		 ActionErrors errors = studentSemesterFeeDetailsForm.validate(mapping, request);
		 List<StudentSemesterFeeDetailsTo> studentList = null;
		 List<StudentSemesterFeeDetailsTo> stuList = null;
		 List<StudentUtilBO> studentList1 = null;
		 String className = null;
		 try{
			 		int courseId = StudentSemesterFeeDetailsHandler.getInstance().getcId(studentSemesterFeeDetailsForm);
			 		if(courseId == 26){
			 			studentSemesterFeeDetailsForm.setIsCourse(true);
			 		}else {
			 			studentSemesterFeeDetailsForm.setIsCourse(false);
			 		}
			 		className = StudentSemesterFeeDetailsHandler.getInstance().getClassName(studentSemesterFeeDetailsForm);
			 		studentSemesterFeeDetailsForm.setClassName(className);
			 		List<StudentSemesterFeeDetails> totalList = StudentSemesterFeeDetailsHandler.getInstance().totList(studentSemesterFeeDetailsForm);
			 		List approvedList = StudentSemesterFeeDetailsHandler.getInstance().approvedList(studentSemesterFeeDetailsForm);
			 		if((totalList != null && !totalList.isEmpty())&& (approvedList != null && !approvedList.isEmpty())){
			 		List  finalList = StudentSemesterFeeDetailsHelper.getInstance().finalList(totalList,approvedList);
			 		//boolean approved = StudentSemesterFeeDetailsHandler.getInstance().getApproved(studentSemesterFeeDetailsForm);
			 		if(finalList == null || finalList.isEmpty()){
			 			errors.add("error", new ActionError("knowledgepro.fees.noRecords"));
						saveErrors(request, errors);
						//setRequiredDatatoForm(studentSemesterFeeDetailsForm, request);
						log.info("Exit Interview Batch Result - getSelectedCandidates size 0");
						return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
			 		}else {
			 			stuList = StudentSemesterFeeDetailsHandler.getInstance().getList(studentSemesterFeeDetailsForm);
			 		}
			 	}
			 		if(stuList != null && !stuList.isEmpty()){
			 			studentSemesterFeeDetailsForm.setStudentList(stuList);
			 		}else  {
			 			 studentList = StudentSemesterFeeDetailsHandler.getInstance().getStudentList(studentSemesterFeeDetailsForm);
			 			 if(studentList == null || studentList.isEmpty()){
			 				studentList = StudentSemesterFeeDetailsHandler.getInstance().getPreviousStudent(studentSemesterFeeDetailsForm);
			 			 }
				 		 studentSemesterFeeDetailsForm.setStudentList(studentList);
			 			//studentSemesterFeeDetailsForm.setStudentList(null);
				 		if (studentList==null || studentList.isEmpty()) {
							errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
							saveErrors(request, errors);
							//setRequiredDatatoForm(studentSemesterFeeDetailsForm, request);
							log.info("Exit Interview Batch Result - getSelectedCandidates size 0");
							return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
					} 
			 		}			 		   
			 		
			 }catch (Exception exception) {
					String msg = super.handleApplicationException(exception);
					studentSemesterFeeDetailsForm.setErrorMessage(msg);
					studentSemesterFeeDetailsForm.setErrorStack(exception.getMessage());
					return mapping.findForward(CMSConstants.ERROR_PAGE);				
				}
			 //studentSemesterFeeDetailsForm.setStudentList(stuList);
			 
			 studentSemesterFeeDetailsForm.setClassName(className);
			
			 log.info("Entered StudentSemesterFeeDetailsAction - getCandidates");
				return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS_RESULTS);
		 
	 }
	 
	 public ActionForward save(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm = (StudentSemesterFeeDetailsForm) form;
		 ActionErrors errors = studentSemesterFeeDetailsForm.validate(mapping, request);
		 ActionMessages messages = new ActionMessages();
		 try{
			 setUserId(request, studentSemesterFeeDetailsForm);
			 List<StudentSemesterFeeDetailsTo> toList = studentSemesterFeeDetailsForm.getStudentList();
			 boolean save = StudentSemesterFeeDetailsHandler.getInstance().save(toList,studentSemesterFeeDetailsForm);
			 if(save){
				 ActionMessage message = new ActionMessage("knowledgepro.admin.addsuccess"," Student Semester Fees");
					messages.add("messages", message);
					saveMessages(request, messages);
					return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
			 }else {
				 errors.add("error", new ActionError("kknowledgepro.admin.addfailure", "Student Semester Fees"));
					saveErrors(request,errors);
					return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
			 }
		 }catch (Exception exception) {
			 exception.printStackTrace();
				if(exception instanceof DataNotFoundException) {
					errors.add("error", new ActionError("knowledgepro.exam.tokenRegistration.noMatchingExam"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.STUDENT_SEMESTER_FEE_DETAILS);
				}	
				String msg = super.handleApplicationException(exception);
				studentSemesterFeeDetailsForm.setErrorMessage(msg);
				studentSemesterFeeDetailsForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);				
			}
		
		 
	 }
	 

}
