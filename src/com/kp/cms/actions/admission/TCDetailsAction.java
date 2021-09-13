package com.kp.cms.actions.admission;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admission.TCDetailsForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.admission.TCDetailsHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admission.BoardDetailsTO;
import com.kp.cms.to.admission.CharacterAndConductTO;
import com.kp.cms.to.admission.TCDetailsTO;
import com.kp.cms.to.exam.ExamDefinitionTO;
import com.kp.cms.utilities.CurrentAcademicYear;

@SuppressWarnings("deprecation")
public class TCDetailsAction extends BaseDispatchAction {

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return Initializes createPracticalBatch Initializes classMap and
	 *         subjectMap
	 * @throws Exception
	 */
	public ActionForward initTCDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		try {
			tcDetailsForm.resetFields();
			// Setting the classMap to request scope
			setClassMapToRequest(request, tcDetailsForm);
			setProgramTypeListToRequest(request, tcDetailsForm);
			setDataToForm(tcDetailsForm);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = super.handleApplicationException(e);
			tcDetailsForm.setErrorMessage(msg);
			tcDetailsForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
	}

	/**
	 * 
	 * @param request
	 * @param boardDetailsForm
	 *            Sets all the classes for the current year in request scope
	 * @throws Exception
	 */
	public void setClassMapToRequest(HttpServletRequest request,TCDetailsForm tcDetailsForm) throws Exception {
		try {
			if (tcDetailsForm.getAcademicYear() == null
					|| tcDetailsForm.getAcademicYear().isEmpty()) {
				// Below statements is used to get the current year and for the
				// year get the class Map.
				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int year = CurrentAcademicYear.getInstance().getAcademicyear();
				if (year != 0) {
					currentYear = year;
				}
				Map<Integer, String> classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
				request.setAttribute("classMap", classMap);
			}
			// Otherwise get the classMap for the selected year
			else {
				int year = Integer.parseInt(tcDetailsForm.getAcademicYear().trim());
				Map<Integer, String> classMap = CommonAjaxHandler.getInstance()
						.getClassesByYear(year);
				request.setAttribute("classMap", classMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}
	/**
	 * Method to select the candidates for score sheet result entry based on the
	 * input selected
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * -------------------------
	 * modified by Arun Sudhakaran
	 * This method is omitted because the first page only asks register number
	 * So no list is required.
	 */
	public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		 ActionErrors errors = tcDetailsForm.validate(mapping, request);
		if (errors.isEmpty()) {
			try {
				List<BoardDetailsTO> boardList = TCDetailsHandler.getInstance().getListOfCandidates(tcDetailsForm);
				if (boardList.isEmpty()) {
					errors.add(CMSConstants.ERROR,new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					setClassMapToRequest(request, tcDetailsForm);
					setProgramTypeListToRequest(request, tcDetailsForm);
					setDataToForm(tcDetailsForm);
					return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
				}
				tcDetailsForm.setBoardList(boardList);
			} catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				tcDetailsForm.setErrorMessage(msg);
				tcDetailsForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setClassMapToRequest(request, tcDetailsForm);
			setDataToForm(tcDetailsForm);
			return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
		}
		return mapping.findForward(CMSConstants.TC_DETAILS_RESULT);
	}
	
	/**
	 * Method to select the candidates for score sheet result entry based on the
	 * input selected
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getStudentDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		 ActionErrors errors = tcDetailsForm.validate(mapping, request);
		if (errors.isEmpty()) {
			try {
				TCDetailsHandler.getInstance().getStudentTCDetails(tcDetailsForm);
				setDataToForm(tcDetailsForm);
			} catch (DataNotFoundException exception) {
				exception.printStackTrace();
				errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.student.norecord"));
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
			} catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				tcDetailsForm.setErrorMessage(msg);
				tcDetailsForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.TC_DETAILS_RESULT);
		}
		return mapping.findForward(CMSConstants.TC_DETAILS);
	}
	
	/**
	 * @param tcDetailsForm
	 * @throws Exception
	 */
	private void setDataToForm(TCDetailsForm tcDetailsForm) throws Exception {
		List<CharacterAndConductTO> list=TCDetailsHandler.getInstance().getCharacterAndConductList();
		tcDetailsForm.setList(list);
		
	}

	/**
	 * Method to select the candidates for score sheet result entry based on the
	 * input selected
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateStudentTCDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = tcDetailsForm.validate(mapping, request);
		validateTCDetails(tcDetailsForm,errors);
		if (errors.isEmpty()) {
			try {
				setUserId(request, tcDetailsForm);
				boolean isUpdated=TCDetailsHandler.getInstance().updateStudentTCDetails(tcDetailsForm);
				if(isUpdated){
					ActionMessage message = new ActionMessage("knowledgepro.admission.tc.details.added.successfully");
					messages.add("messages", message);
					saveMessages(request, messages);
					tcDetailsForm.setRegisterNo(null);
				}else{
					// failed
					errors.add("error", new ActionError("knowledgepro.admission.tc.details.added.failure"));
					saveErrors(request, errors);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				tcDetailsForm.setErrorMessage(msg);
				tcDetailsForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.TC_DETAILS);
		}
		return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
	}

	private void validateTCDetails(TCDetailsForm tcDetailsForm,
			ActionErrors errors) throws Exception {
		TCDetailsTO to=tcDetailsForm.getTcDetailsTO();
		if(to.getPassed()!=null && !to.getPassed().isEmpty() && to.getPassed().equalsIgnoreCase("no")){
			if(to.getSubjectPassed()==null || to.getSubjectPassed().isEmpty()){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.student.tcDetails.subjectPassed.required"));
			}
		}
		
	}
	
	public ActionForward updateCandidateDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = tcDetailsForm.validate(mapping, request);
		Boolean saved = false;
		setUserId(request, tcDetailsForm);
		if (errors.isEmpty()) {
			saved=TCDetailsHandler.getInstance().getUpdateStatus(tcDetailsForm);
			if(saved){
				ActionMessage message = new ActionMessage("knowledgepro.admission.tc.details.added.successfully");
				messages.add("messages", message);
				saveMessages(request, messages);
				tcDetailsForm.resetFields();
			}else{
				// failed
				errors.add("error", new ActionError("knowledgepro.admission.tc.details.added.failure"));
				saveErrors(request, errors);
			}
		} else {
			addErrors(request, errors);
			setClassMapToRequest(request, tcDetailsForm);
			setProgramTypeListToRequest(request, tcDetailsForm);
			setDataToForm(tcDetailsForm);
			return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
		}
		
		setClassMapToRequest(request, tcDetailsForm);
		setProgramTypeListToRequest(request, tcDetailsForm);
		setDataToForm(tcDetailsForm);
		return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
	}
	public void setProgramTypeListToRequest(HttpServletRequest request, TCDetailsForm tCDetailsForm)	throws Exception {
		List<ProgramTypeTO> programTypeList = ProgramTypeHandler.getInstance().getProgramType();
		tCDetailsForm.setProgramTypeList(programTypeList);
		request.setAttribute("programTypeList", programTypeList);
	}
	
	public ActionForward getExamNames(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		List<Object[]> examNames = TCDetailsHandler.getInstance().getExamNames(tcDetailsForm);
		List<ExamDefinitionTO> examList = TCDetailsHandler.getInstance().convertExamNamesToForm(examNames);
		tcDetailsForm.setExamNames(examList);
		setClassMapToRequest(request, tcDetailsForm);
		setProgramTypeListToRequest(request,tcDetailsForm);  //setting programType List to request to populate in combo
		return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
	}
	
	public ActionForward getDetailsForAllStudentsByClass(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;		
		tcDetailsForm.reset();
		TCDetailsHandler.getInstance().getAllStudentTCDetailsByClass(tcDetailsForm);
		setClassMapToRequest(request, tcDetailsForm);
		setDataToForm(tcDetailsForm);
		setProgramTypeListToRequest(request,tcDetailsForm);
		return mapping.findForward(CMSConstants.INIT_TC_DETAILS);
	}
	
	public ActionForward initTcCancelDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		ActionErrors errors = tcDetailsForm.validate(mapping, request);
		if(errors.isEmpty()){
		try {
			tcDetailsForm.resetFields();
			
		} catch (Exception e) {
			e.printStackTrace();
			String msg = super.handleApplicationException(e);
			tcDetailsForm.setErrorMessage(msg);
			tcDetailsForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		}
		else{
			addErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.INIT_TC_CANCEL_DETAILS);
	}
	public ActionForward cancelTCDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,HttpServletResponse response){
		TCDetailsForm tcDetailsForm = (TCDetailsForm) form;
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = tcDetailsForm.validate(mapping, request);
		if(errors.isEmpty()){
		try {
			boolean isCancelled= TCDetailsHandler.getInstance().cancelTCPrint(tcDetailsForm);
			if(isCancelled){
				ActionMessage message = new ActionMessage("knowledgepro.tc.print.cancelled.successfully");
				messages.add("messages", message);
				saveMessages(request, messages);
				tcDetailsForm.resetFields();
			
			}
			else{
				ActionMessage message = new ActionMessage("knowledgepro.norecords");
				messages.add("messages", message);
				saveMessages(request, messages);
				tcDetailsForm.resetFields();		
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
			String msg = super.handleApplicationException(e);
			tcDetailsForm.setErrorMessage(msg);
			tcDetailsForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		}
		else{
			addErrors(request, errors);
		}
		return mapping.findForward(CMSConstants.INIT_TC_CANCEL_DETAILS);
	}
	
}
