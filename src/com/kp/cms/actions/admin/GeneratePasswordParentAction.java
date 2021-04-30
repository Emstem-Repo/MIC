package com.kp.cms.actions.admin;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admin.GeneratePasswordParentForm;
import com.kp.cms.handlers.admin.GeneratePasswordForParentHandler;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.to.admin.OrganizationTO;
import com.kp.cms.to.admin.ProgramTypeTO;

public class GeneratePasswordParentAction  extends BaseDispatchAction{
	private static final Log log = LogFactory.getLog(GeneratePasswordParentAction.class);
	
	public ActionForward initGeneratePasswordParent(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response) throws Exception{
		GeneratePasswordParentForm generatePasswordParentForm = (GeneratePasswordParentForm)form;
		//IPADDRESS CHECK
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		   if (ipAddress == null) {  
			   ipAddress = request.getRemoteAddr();  
		   }
		if(!CMSConstants.IPADDRESSLIST.contains(ipAddress)){
			
			return mapping.findForward("logout");	
		}
		
		try {
			// initialize program type
			setUserId(request, generatePasswordParentForm);
			generatePasswordParentForm.setProgramTypeId(null);
			generatePasswordParentForm.setProgramId(null);
			generatePasswordParentForm.setStudentResgisterNo(false);
			generatePasswordParentForm.setStudentRollNo(false);
			
			List<ProgramTypeTO> programTypeList = ProgramTypeHandler.getInstance().getProgramType();
			generatePasswordParentForm.setProgramTypeList(programTypeList);
			
		
		}catch (ApplicationException e) {
			log.error("error in init application detail page...",e);
			
				String msg = super.handleApplicationException(e);
				generatePasswordParentForm.setErrorMessage(msg);
				generatePasswordParentForm.setErrorStack(e.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		catch (Exception e) {
			log.error("error in init application detail page...",e);
				throw e;
		}
		return mapping.findForward(CMSConstants.GENERATE_PASSWORD_PARENT);
	}
	
	public ActionForward generatePassword(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception {
		GeneratePasswordParentForm gnform = (GeneratePasswordParentForm)form;
		ActionMessages errors = gnform.validate(mapping, request);
		try{
			validateUsernameConfugurations(gnform,errors);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.GENERATE_PASSWORD_PARENT);
			}
			//check parent and student having same userid and pwd
			List<OrganizationTO> organisations=OrganizationHandler.getInstance().getOrganizationDetails();
			if(organisations!=null){
				Iterator<OrganizationTO> orgItr=organisations.iterator();
				while (orgItr.hasNext()) {
					OrganizationTO orgTO = (OrganizationTO) orgItr.next();
					gnform.setSameUseridPassword(orgTO.getSameUseridPassword());
				}
			}
			GeneratePasswordForParentHandler handler = GeneratePasswordForParentHandler .getInstance();
			handler.updateOnlyPassword(gnform);
			if(gnform.getParentSuccessList() != null && !gnform.getParentSuccessList().isEmpty()){
				handler.sendSMS(gnform);
			}
			gnform.setMessage(CMSConstants.GENERATE_PASSWORD_PARENT);
			return mapping.findForward(CMSConstants.GENERATE_PASSWORD_PARENT_CONFIRM);
			
		}catch (ApplicationException e) {
			log.error("error in init application detail page...",e);
			
			String msg = super.handleApplicationException(e);
			gnform.setErrorMessage(msg);
			gnform.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		} 
		catch (Exception e) {
			log.error("error in init application detail page...",e);
				throw e;
		}	 
	}

	private void validateUsernameConfugurations(GeneratePasswordParentForm gnform, ActionMessages errors) throws Exception {

		if(errors==null){
			errors= new ActionMessages();
		}
		if(  !gnform.getStudentResgisterNo() && !gnform.getStudentRollNo()){
			
			if (errors.get(CMSConstants.GENERATEPASSWORD_STUDENTUSERNAME_SELECT)!=null && !errors.get(CMSConstants.GENERATEPASSWORD_STUDENTUSERNAME_SELECT).hasNext()) {
				ActionMessage error = new ActionMessage(
						CMSConstants.GENERATEPASSWORD_STUDENTUSERNAME_SELECT);
				errors.add(CMSConstants.GENERATEPASSWORD_STUDENTUSERNAME_SELECT, error);
			}
		}
		
		
	
		
	}
}
