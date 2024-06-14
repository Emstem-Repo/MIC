package com.kp.cms.actions.exam;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.forms.exam.CareerProgressionForm;
import com.kp.cms.to.admin.HigherEducationDTO;
import com.kp.cms.to.admin.PlacementDTO;
import com.kp.cms.to.admission.CertificateCourseTO;

/**
 * @author Administrator
 *
 */
public class CareerProgressionMapEntryAction extends BaseDispatchAction {
	
	private static final Log log = LogFactory.getLog(CareerProgressionMapEntryAction.class);
	
	public ActionForward initCareerProgression(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		CareerProgressionForm careerProgressionForm = (CareerProgressionForm) form;// Type casting the Action form to Required Form
		HttpSession session = request.getSession();
		//careerProgressionForm.setRoleId(session.getAttribute("rid").toString());
		
		List<HigherEducationDTO> higherEducationList=new ArrayList<HigherEducationDTO>();
		List<PlacementDTO> placementList=new ArrayList<PlacementDTO>();
		setListobj(higherEducationList, placementList);
		careerProgressionForm.setHigherEducationList(higherEducationList);
		careerProgressionForm.setPlacementList(placementList);
		log.info("Exit initExamMarksEntry input");
		setUserId(request, careerProgressionForm);
		return mapping.findForward("initCareerMap");
	}

	private void setListobj(List<HigherEducationDTO> higherEducationList, List<PlacementDTO> placementList) {
		// TODO Auto-generated method stub
		
	}

	public void setUserId(HttpServletRequest request, BaseActionForm form){
		HttpSession session = request.getSession(false);
		if(session.getAttribute("uid")!=null){
			form.setUserId(session.getAttribute("uid").toString());
		}
		request.getSession().removeAttribute("baseActionForm");
	}
	public ActionForward submitAddMorePlacement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		List<CertificateCourseTO> prefList=admForm.getPrefList();
		int count=1;
		
		List<CertificateCourseTO> current=admForm.getPrefList();
		int dup=0;
		for (CertificateCourseTO certificateCourseTO : current) {
			count++;
			if (certificateCourseTO.getId()==admForm.getCerticateId()) {
				dup=1;
			}
		}
		if (dup==0) {
		for (Map.Entry<Integer,String> entry : admForm.getCerticateCourses().entrySet()){
			CertificateCourseTO to=new CertificateCourseTO();
			if (entry.getKey()==admForm.getCerticateId()) {
				to.setId(admForm.getCerticateId());
				to.setCourseName(entry.getValue());
				prefList.add(to);
			}
           /* System.out.println("Key = " + entry.getKey() +
                             ", Value = " + entry.getValue());*/
		}
		}
		admForm.setPrefList(prefList);
		admForm.setPreCount(count);
		System.out.println(count);
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
		
	}
	
	public ActionForward submitAddMoreHigherEducation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("entered initAdmissionApproval..");
		AdmissionStatusForm admForm = (AdmissionStatusForm) form;
		List<CertificateCourseTO> prefList=admForm.getPrefList();
		int count=1;
		
		List<CertificateCourseTO> current=admForm.getPrefList();
		int dup=0;
		for (CertificateCourseTO certificateCourseTO : current) {
			count++;
			if (certificateCourseTO.getId()==admForm.getCerticateId()) {
				dup=1;
			}
		}
		if (dup==0) {
		for (Map.Entry<Integer,String> entry : admForm.getCerticateCourses().entrySet()){
			CertificateCourseTO to=new CertificateCourseTO();
			if (entry.getKey()==admForm.getCerticateId()) {
				to.setId(admForm.getCerticateId());
				to.setCourseName(entry.getValue());
				prefList.add(to);
			}
           /* System.out.println("Key = " + entry.getKey() +
                             ", Value = " + entry.getValue());*/
		}
		}
		admForm.setPrefList(prefList);
		admForm.setPreCount(count);
		System.out.println(count);
		return mapping.findForward(CMSConstants.ADMISSION_INIT_APPLICATIONSTATUS);
		
	}
	
}
