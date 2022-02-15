package com.kp.cms.actions.employee;
	import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.apache.struts.upload.FormFile;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.actions.admission.AdmissionFormAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.employee.GuestFacultyInfoForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.employee.AddExaminerHandler;
import com.kp.cms.handlers.employee.GuestFacultyInfoHandler;
import com.kp.cms.to.employee.EligibilityTestTO;
import com.kp.cms.to.employee.EmpQualificationLevelTo;
import com.kp.cms.to.employee.GuestFacultyInfoTo;
import com.kp.cms.to.employee.GuestFacultyTO;
import com.kp.cms.to.employee.GuestPreviousChristWorkDetailsTO;
import com.kp.cms.to.employee.GuestPreviousExperienceTO;
import com.kp.cms.transactionsimpl.employee.GuestFacultyInfoImpl;
import com.kp.cms.utilities.CommonUtil;

	@SuppressWarnings("deprecation")
	public class ExaminerInfoAction   extends BaseDispatchAction {
			private static final Log log = LogFactory.getLog(ExaminerInfoAction.class);
			private static final String MESSAGE_KEY = "messages";
			private static final String PHOTOBYTES = "PhotoBytes";
			private static final String TO_DATEFORMAT = "MM/dd/yyyy";
			private static final String FROM_DATEFORMAT = "dd/MM/yyyy";
			
			
		
	
			
			
			public ActionForward initEmployeeInfoAdd(ActionMapping mapping, ActionForm form,
					HttpServletRequest request, HttpServletResponse response)
					throws Exception {
					
					GuestFacultyInfoForm objform = (GuestFacultyInfoForm) form;
					GuestFacultyInfoHandler.getInstance().getInitialPageData(objform);
						
					return mapping.findForward("addexaminerInit");
					}
			
			public ActionForward addEmployeeInfoAdd(ActionMapping mapping, ActionForm form,
					HttpServletRequest request, HttpServletResponse response)
					throws Exception {
					GuestFacultyInfoForm objform = (GuestFacultyInfoForm) form;
					ActionMessages messages=new ActionMessages();
					ActionErrors errors=objform.validate(mapping, request);
					errors=validatedetails(objform,errors);
					validateData(objform,errors);
					try {
						if (errors.isEmpty()) {
							boolean  result=AddExaminerHandler.getInstance().saveExaminers(objform);
							
							if (result) {
								objform.reset();
								GuestFacultyInfoHandler.getInstance().getInitialPageData(objform);
								ActionMessage message=new ActionMessage(CMSConstants.GUEST_SUBMIT_SUCCESS);
								messages.add(CMSConstants.MESSAGES,message);
								saveMessages(request, messages);
							}
						}else{
							saveErrors(request, errors);
							return mapping.findForward("addexaminerInit");
						}
						
					} catch (Exception e) {
						
					}
					
					
						
					return mapping.findForward("addexaminerInit");
					}

			private ActionErrors validatedetails(GuestFacultyInfoForm objform,ActionErrors errors) {
				if (objform.getName()==null || objform.getName().isEmpty()) {
					errors.add("error", new ActionError("employee.info.emergency.name.required"));
				}
				if (objform.getMobileNo1()==null || objform.getMobileNo1().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.phd.Mobile.Limit"));
				}
				if (objform.getEmail()==null || objform.getEmail().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.supportrequest.mail.notvalid"));
				}
				if (objform.getRetired()==null || objform.getRetired().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.supportrequest.mail.notvalid"));
				}
				
				if (objform.getDepartmentId()==null || objform.getDepartmentId().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.admin.Department.required"));
				}
				if (objform.getCollegeName()==null || objform.getCollegeName().isEmpty()) {
					errors.add("error", new ActionError("admissionForm.transferentry.prevColg.label"));
				}
				if (objform.getExpYears()==null || objform.getExpYears().isEmpty()) {
					errors.add("error", new ActionError("knowledgepro.employee.onlineresume.totalExpYear"));
				}
				
				return errors;
				
			}
			private void validateData(GuestFacultyInfoForm employeeInfoEditForm,ActionErrors errors) {
				try{
				InputStream propStream=AdmissionFormAction.class.getResourceAsStream("/resources/application.properties");
				int maxPhotoSize=0;
				Properties prop=new Properties();
				prop.load(propStream);
				maxPhotoSize=Integer.parseInt(prop.getProperty(CMSConstants.MAX_UPLOAD_PHOTOSIZE_KEY));
				FormFile file=null;
				if(employeeInfoEditForm.getEmpPhoto()!=null)
					file=employeeInfoEditForm.getEmpPhoto();
					if (employeeInfoEditForm.getCurrentAddressLine1() != null && !employeeInfoEditForm.getCurrentAddressLine1().isEmpty() && employeeInfoEditForm.getCurrentAddressLine1().trim().length() > 100)  {
				
						if (errors.get(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE1_ABOVELIMIT) != null
							&& !errors.get(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE1_ABOVELIMIT)
									.hasNext()) {
							ActionMessage error = new ActionMessage(
								CMSConstants.EMPLOYEE_CURRENTADDRESSLINE1_ABOVELIMIT);
							errors.add(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE1_ABOVELIMIT, error);
					}
				}
					if (employeeInfoEditForm.getCurrentAddressLine2() != null && !employeeInfoEditForm.getCurrentAddressLine2().isEmpty() && employeeInfoEditForm.getCurrentAddressLine2().trim().length() > 100)  {
						if (errors.get(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE2_ABOVELIMIT) != null
								&& !errors.get(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE2_ABOVELIMIT)
										.hasNext()) {
							ActionMessage error = new ActionMessage(
									CMSConstants.EMPLOYEE_CURRENTADDRESSLINE2_ABOVELIMIT);
							errors.add(CMSConstants.EMPLOYEE_CURRENTADDRESSLINE2_ABOVELIMIT, error);
						}
					}
					if (employeeInfoEditForm.getAddressLine1() != null && !employeeInfoEditForm.getAddressLine1().isEmpty() && employeeInfoEditForm.getAddressLine1().trim().length() > 100)  {
						if (errors.get(CMSConstants.EMPLOYEE_ADDRESSLINE1_ABOVELIMIT) != null
								&& !errors.get(CMSConstants.EMPLOYEE_ADDRESSLINE1_ABOVELIMIT)
										.hasNext()) {
							ActionMessage error = new ActionMessage(
									CMSConstants.EMPLOYEE_ADDRESSLINE1_ABOVELIMIT);
							errors.add(CMSConstants.EMPLOYEE_ADDRESSLINE1_ABOVELIMIT, error);
						}
					}
					if (employeeInfoEditForm.getAddressLine2() != null && !employeeInfoEditForm.getAddressLine2().isEmpty() && employeeInfoEditForm.getAddressLine2().trim().length() > 100)  {
						if (errors.get(CMSConstants.EMPLOYEE_ADDRESSLINE2_ABOVELIMIT) != null
								&& !errors.get(CMSConstants.EMPLOYEE_ADDRESSLINE2_ABOVELIMIT)
										.hasNext()) {
							ActionMessage error = new ActionMessage(
									CMSConstants.EMPLOYEE_ADDRESSLINE2_ABOVELIMIT);
							errors.add(CMSConstants.EMPLOYEE_ADDRESSLINE2_ABOVELIMIT, error);
						}
					}
				
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			/**
			 * @param mapping
			 * @param form
			 * @param request
			 * @param response
			 * @return
			 * @throws Exception
			 */
			
		}
