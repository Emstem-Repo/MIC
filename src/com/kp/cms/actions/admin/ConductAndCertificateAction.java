package com.kp.cms.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.Validate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admin.ConductAndCertificateForm;
import com.kp.cms.handlers.admin.ConductAndCertificateHandler;

public class ConductAndCertificateAction extends BaseDispatchAction{
	
	public ActionForward initConductDetails(ActionMapping mapping,ActionForm form,
											HttpServletRequest request,HttpServletResponse response)
											throws Exception{
		ConductAndCertificateForm certificateForm = (ConductAndCertificateForm) form;
		try{
			certificateForm.setRegisterNo(null);
			certificateForm.setSearchBy(null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_CONDUCT_DETAILS);
	}
	
	@SuppressWarnings("deprecation")
	public ActionForward getStudentConductDetails(ActionMapping mapping,ActionForm form,
												  HttpServletRequest request,HttpServletResponse response)
												  throws Exception{
		ConductAndCertificateForm certificateForm =(ConductAndCertificateForm)form;
		ActionErrors errors =certificateForm.validate(mapping, request);
		if(errors.isEmpty()){
			try{
				ConductAndCertificateHandler.getInstance().getStudentDetails(certificateForm);
			}catch (DataNotFoundException ex) {
				ex.printStackTrace();
				errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.student.norecord"));
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.INIT_CONDUCT_DETAILS);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.INIT_CONDUCT_DETAILS);
		}
		return mapping.findForward(CMSConstants.STUDENT_CONDUCT_DETAIL);
	}
	
	public ActionForward printConductCertificate(ActionMapping mapping,ActionForm form,
												 HttpServletRequest request,HttpServletResponse response)
												 throws Exception{
		ConductAndCertificateForm certificateForm = (ConductAndCertificateForm) form;
		return mapping.findForward(CMSConstants.PRINT_CONDUCT_AND_CERTIFICATE);
	}
}
