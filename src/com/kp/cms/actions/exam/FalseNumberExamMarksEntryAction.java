package com.kp.cms.actions.exam;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.ajax.CommonAjaxExamHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.ExamFalseNumberHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.handlers.exam.FalseNumExamMarksEntryHandler;
import com.kp.cms.handlers.exam.NewExamMarksEntryHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamMarkEvaluationTo;
import com.kp.cms.to.exam.FalseNoDisplayTo;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.StudentMarksTO1;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;
import com.kp.cms.utilities.ExamComparator;
import com.kp.cms.utilities.StudentRollNoComparator;

public class FalseNumberExamMarksEntryAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(FalseNumberExamMarksEntryAction.class);
	
	/**
	 * Method to set the required data to the form to display it in ExamMarksEntry.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initExamMarksEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		HttpSession session = request.getSession();
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		//setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry input");
		newExamMarksEntryForm.setDisplatoList(new FalseNoDisplayTo());
		List<ExamMarkEvaluationTo> evalList=new ArrayList<ExamMarkEvaluationTo>();
		newExamMarksEntryForm.setExamEvalToList(evalList);
		setUserId(request, newExamMarksEntryForm);
		return mapping.findForward("initFalseMarksEntry");
	}
	
	public ActionForward setvaluesBybarcode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		if (newExamMarksEntryForm.getFalseNo()==null || newExamMarksEntryForm.getFalseNo().isEmpty()) {
			errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admin.updateFailure"));
		}if (!NewExamMarksEntryTransactionImpl.getInstance().checkFallseBox(newExamMarksEntryForm)) {
			errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admin.updateFailure"));
		}
		if (errors.isEmpty()) {
			newExamMarksEntryForm.setDisplatoList(new FalseNoDisplayTo());
			NewExamMarksEntryHandler.getInstance().setValuesFalseNumberBased(newExamMarksEntryForm);
			setStudentToList(mapping,request,newExamMarksEntryForm,response);
		}else{
			addErrors(request, errors);
		}
		
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward("initFalseMarksEntry");
	}

	private void setStudentToList(ActionMapping mapping, HttpServletRequest request,
			NewExamMarksEntryForm newExamMarksEntryForm, HttpServletResponse response) throws Exception {
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		validateExamMarksEntry(newExamMarksEntryForm,errors);
		
		if (errors.isEmpty()) {
			try {
				
				String schemeNo=newExamMarksEntryForm.getSchemeNo();// If no record comes to put the value to form
				String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
				newExamMarksEntryForm.setSchemeNo(schemes[1]);
				Set<StudentMarksTO> studentList=FalseNumExamMarksEntryHandler.getInstance().getStudentForInput(newExamMarksEntryForm);// getting the student list for input search
				if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
				}
				List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);
				List<StudentMarksTO> finalList = new ArrayList();
				
				
				Collections.sort(list);
					for (StudentMarksTO studentMarksTO : list) {
						if (studentMarksTO.getFalseNo()==newExamMarksEntryForm.getFalseNo() || studentMarksTO.getFalseNo().equalsIgnoreCase(newExamMarksEntryForm.getFalseNo())) {
							finalList.add(studentMarksTO);
							newExamMarksEntryForm.setStudentList(finalList);
							newExamMarksEntryForm.setStudentMarksTo(studentMarksTO);
						}
					}
				
				
				setNamesToForm(newExamMarksEntryForm);
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
			}
		}else {
			addErrors(request, errors);
			//setRequiredDatatoForm(newExamMarksEntryForm, request);			
			log.info("Exit NewExamMarksEntryAction - getSelectedCandidates errors not empty ");
		}
		
	}
	

	public ActionForward AddEvaluatorsMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		setUserId(request,newExamMarksEntryForm);
		if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==1 
				&& (newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getFirstEvaluation()==null || newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getFirstEvaluation().isEmpty())) {
			errors.add(CMSConstants.ERROR,new ActionError("admissionFormForm.education.totalMark.notNumeric"));
		}else if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==2 
				&& (newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getSecondEvaluation()==null || newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getSecondEvaluation().isEmpty())){
			errors.add(CMSConstants.ERROR,new ActionError("admissionFormForm.education.totalMark.notNumeric"));
		}
		else if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==3
				&& (newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getThirdEvaluation()==null || newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo().getThirdEvaluation().isEmpty())){
			errors.add(CMSConstants.ERROR,new ActionError("admissionFormForm.education.totalMark.notNumeric"));
		}
		//validateMaxMarks(newExamMarksEntryForm,errors);
		if (errors.isEmpty()) {
			try {
				boolean isUpdated =FalseNumExamMarksEntryHandler.getInstance().addEvalationMarks(newExamMarksEntryForm);
				//newExamMarksEntryForm.setDisplatoList(new FalseNoDisplayTo());
				newExamMarksEntryForm.setFalseNo(null);
				newExamMarksEntryForm.getStudentMarksTo().setExamEvalTo(new ExamMarkEvaluationTo());
				//setRequiredDatatoForm(newExamMarksEntryForm, request);
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
//			setRequiredDatatoForm(newExamMarksEntryForm, request);
			log.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping.findForward("initFalseMarksEntry");
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
			return mapping.findForward("initFalseMarksEntry");
		
	}

	
	public ActionForward saveEvaluatorsMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		setUserId(request,newExamMarksEntryForm);
		//validateMaxMarks(newExamMarksEntryForm,errors);
		if (errors.isEmpty()) {
			try {
				boolean isUpdated =FalseNumExamMarksEntryHandler.getInstance().saveEvalationMarks(newExamMarksEntryForm);
				if (isUpdated) {
					newExamMarksEntryForm.setDisplatoList(new FalseNoDisplayTo());
					List<ExamMarkEvaluationTo> evalList=new ArrayList<ExamMarkEvaluationTo>();
					newExamMarksEntryForm.setExamEvalToList(evalList);
					newExamMarksEntryForm.setEvalNo("0");
					messages.add(CMSConstants.MESSAGES, new ActionError("knowledgepro.admin.addsuccess","Marks"));
					saveMessages(request, messages);
					
				}else{
					errors.add(CMSConstants.ERROR,new ActionError("kknowledgepro.admin.addfailure","Marks"));
					addErrors(request, errors);
				}
				//setRequiredDatatoForm(newExamMarksEntryForm, request);
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
//			setRequiredDatatoForm(newExamMarksEntryForm, request);
			log.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping.findForward("initFalseMarksEntry");
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
			return mapping.findForward("initFalseMarksEntry");
		
	}
	
	public ActionForward initFinalExamMarksEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		HttpSession session = request.getSession();
		newExamMarksEntryForm.setFinalValidation(false);
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry input");
		newExamMarksEntryForm.setDisplatoList(new FalseNoDisplayTo());
		setUserId(request, newExamMarksEntryForm);
		return mapping.findForward("initFinalFalseMarksEntry");
	}
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - getCandidates");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		 ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		validateExamMarksEntry(newExamMarksEntryForm,errors);
		boolean result=FalseNumExamMarksEntryHandler.getInstance().checkFinalSubmitAccess(newExamMarksEntryForm);
		
		if (!result) {
			errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
		}
		if (errors.isEmpty()) {
			try {
				// Commented because change in requirement
				/*boolean checkSecuredMarksEntry=NewExamMarksEntryHandler.getInstance().checkMarksEnteredThroughSecured(newExamMarksEntryForm);//checking whether marks entered through secured marks entry
				if(checkSecuredMarksEntry){// if the marks are entered through secured marks entry display message in first screen
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.marksentry.marks.through.secured"));
					addErrors(request, errors);
					setRequiredDatatoForm(newExamMarksEntryForm, request);			
					return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
				}*/
				String schemeNo=newExamMarksEntryForm.getSchemeNo();// If no record comes to put the value to form
				String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
				newExamMarksEntryForm.setSchemeNo(schemes[1]);
				Set<StudentMarksTO> studentList=FalseNumExamMarksEntryHandler.getInstance().getStudentForFinalPublishInput(newExamMarksEntryForm);// getting the student list for input search
				if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
					setRequiredDatatoForm(newExamMarksEntryForm, request);			
					return mapping.findForward("initFinalFalseMarksEntry");
				}
				List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);
				
				
				Collections.sort(list);
				newExamMarksEntryForm.setStudentList(list);
				setNamesToForm(newExamMarksEntryForm);
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newExamMarksEntryForm, request);			
			log.info("Exit NewExamMarksEntryAction - getSelectedCandidates errors not empty ");
			return mapping.findForward("initFinalFalseMarksEntry");
		}
		log.info("Entered NewExamMarksEntryAction - getCandidates");
			return mapping.findForward("finalFalseMarksEntryresult");
	}

	/**
	 * @param newExamMarksEntryForm
	 * @param errors
	 * @throws Exception
	 */
	private void validateMaxMarks(NewExamMarksEntryForm newExamMarksEntryForm,ActionErrors errors) throws Exception{
		Double maxMark=NewExamMarksEntryHandler.getInstance().getMaxMarkOfSubject(newExamMarksEntryForm);
		if(!newExamMarksEntryForm.getExamType().equals("Supplementary")){
			if(maxMark==null){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.notDefined"));
			}else{
				List<StudentMarksTO> list=newExamMarksEntryForm.getStudentList();
				Iterator<StudentMarksTO> itr=list.iterator();
				String reg="";
				String regValid="";
				List<String> examAbscentCode = NewSecuredMarksEntryHandler.getInstance().getExamAbscentCode();
				while (itr.hasNext()) {
					StudentMarksTO to = (StudentMarksTO) itr.next();
					if((to.getIsTheory() && to.getTheoryMarks()!=null && !to.getTheoryMarks().isEmpty())){
						if(!(to.getTheoryMarks().matches("\\d{0,3}(\\.\\d{1,2})?") )){
							if(examAbscentCode!=null && !examAbscentCode.contains(to.getTheoryMarks())){
								if(regValid.isEmpty()){
									regValid=to.getRegisterNo();
								}else{
									regValid=regValid+","+to.getRegisterNo();
								}
							}
						}else{
							
							double marks=Double.parseDouble(to.getTheoryMarks());
							if(marks>maxMark){
								if(reg.isEmpty()){
									reg=to.getRegisterNo();
								}else{
									reg=reg+","+to.getRegisterNo();
								}
							}
						}
					}
					
				else if(to.getIsPractical() && to.getPracticalMarks()!=null && !to.getPracticalMarks().isEmpty()){
					if(!(to.getPracticalMarks().matches("\\d{0,3}(\\.\\d{1,2})?") )){
							if(examAbscentCode!=null && !examAbscentCode.contains(to.getPracticalMarks())){
								if(regValid.isEmpty()){
									regValid=to.getRegisterNo();
								}else{
									regValid=regValid+","+to.getRegisterNo();
								}
							}
						
							}else{
							double marks=Double.parseDouble(to.getPracticalMarks());
							if(marks>maxMark){
								if(reg.isEmpty()){
									reg=to.getRegisterNo();
								}else{
									reg=reg+","+to.getRegisterNo();
								}
							}
						}
					}
				}
				if(!reg.isEmpty()){
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo",reg));
				}
				if(!regValid.isEmpty()){
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo.validMarks",regValid));
				}
			}
		}else{
			Map<Integer, Integer> studentsYearMap = newExamMarksEntryForm.getStudentsYearMap();
			Map<Integer, Double> maxMarksMap = newExamMarksEntryForm.getMaxMarksMap();
			List<StudentMarksTO> list=newExamMarksEntryForm.getStudentList();
			Iterator<StudentMarksTO> itr=list.iterator();
			String reg="";
			String regValid="";
			List<String> examAbscentCode = CMSConstants.EXAM_ABSCENT_CODE;
			while (itr.hasNext()) {
				StudentMarksTO to = (StudentMarksTO) itr.next();
				if(studentsYearMap != null && maxMarksMap != null){
					int year  = studentsYearMap.get(to.getStudentId());
					maxMark = maxMarksMap.get(year);
				}
				if((to.getIsTheory() && to.getTheoryMarks()!=null && !to.getTheoryMarks().isEmpty())){
					if(!(to.getTheoryMarks().matches("\\d{0,3}(\\.\\d{1,2})?"))){
						if(examAbscentCode!=null && !examAbscentCode.contains(to.getTheoryMarks())){
							if(regValid.isEmpty()){
								regValid=to.getRegisterNo();
							}else{
								regValid=regValid+","+to.getRegisterNo();
							}
						}
					}else{
						double marks=Double.parseDouble(to.getTheoryMarks());
						if(marks>maxMark){
							if(reg.isEmpty()){
								reg=to.getRegisterNo();
							}else{
								reg=reg+","+to.getRegisterNo();
							}
						}
					}
					
				}else if(to.getIsPractical() && to.getPracticalMarks()!=null && !to.getPracticalMarks().isEmpty()){
					if(!(to.getPracticalMarks().matches("\\d{0,3}(\\.\\d{1,2})?"))){
						if(examAbscentCode!=null && !examAbscentCode.contains(to.getPracticalMarks())){
							if(regValid.isEmpty()){
								regValid=to.getRegisterNo();
							}else{
								regValid=regValid+","+to.getRegisterNo();
							}
						}
					}else{
						double marks=Double.parseDouble(to.getPracticalMarks());
						if(marks>maxMark){
							if(reg.isEmpty()){
								reg=to.getRegisterNo();
							}else{
								reg=reg+","+to.getRegisterNo();
							}
						}
					}
				}
			}
			if(!reg.isEmpty()){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo",reg));
			}
			if(!regValid.isEmpty()){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo.validMarks",regValid));
			}
		
		}
	}

	/**
	 * @param newExamMarksEntryForm
	 */
	private void setNamesToForm(NewExamMarksEntryForm newExamMarksEntryForm)throws Exception {
		
		newExamMarksEntryForm.setSubjectCode(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getSubjectId()),"Subject",true,"code"));
		newExamMarksEntryForm.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getExamId()),"ExamDefinitionBO",true,"name"));
		newExamMarksEntryForm.setSubjectName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getSubjectId()),"Subject",true,"name"));
		newExamMarksEntryForm.setCourseName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getCourseId()),"Course",true,"name"));
		
	}

	/**
	 * @param newExamMarksEntryForm
	 * @param errors
	 * @throws Exception
	 */
	private void validateExamMarksEntry(NewExamMarksEntryForm newExamMarksEntryForm, ActionErrors errors) throws Exception {
		if(newExamMarksEntryForm.getValidationAST()!=null && newExamMarksEntryForm.getValidationAST().equals("yes")){
			if(!CommonUtil.checkForEmpty(newExamMarksEntryForm.getAnswerScriptType())){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required", "AnswerScript"));
			}
		}
		if(newExamMarksEntryForm.getValidationET()!=null && newExamMarksEntryForm.getValidationET().equals("yes")){
			if(!CommonUtil.checkForEmpty(newExamMarksEntryForm.getEvaluatorType())){
				errors.add(CMSConstants.ERROR,new ActionError("errors.required","Evaluator"));
			}
		}
	}
	
	/**
	 * Method to select the candidates for score sheet result entry based on the input selected
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		setUserId(request,newExamMarksEntryForm);
		validateMaxMarks(newExamMarksEntryForm,errors);
		if (errors.isEmpty()) {
			try {
				boolean isUpdated =NewExamMarksEntryHandler.getInstance().saveMarks(newExamMarksEntryForm);
				if (isUpdated) {
					messages.add(CMSConstants.MESSAGES, new ActionError("knowledgepro.admin.addsuccess","Marks"));
					saveMessages(request, messages);
					boolean isRegular=newExamMarksEntryForm.isRegular();
					boolean isInternal=newExamMarksEntryForm.isInternal();
					//newExamMarksEntryForm.resetFields();
					if(isRegular){
						newExamMarksEntryForm.setRegular(true);
						newExamMarksEntryForm.setInternal(false);
						newExamMarksEntryForm.setExamType("Regular");
					}
					if(isInternal){
						newExamMarksEntryForm.setRegular(false);
						newExamMarksEntryForm.setInternal(true);
						newExamMarksEntryForm.setExamType("Internal");
					}
				}else{
					errors.add(CMSConstants.ERROR,new ActionError("kknowledgepro.admin.addfailure","Marks"));
					addErrors(request, errors);
				}
				//setRequiredDatatoForm(newExamMarksEntryForm, request);
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
//			setRequiredDatatoForm(newExamMarksEntryForm, request);
			log.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping.findForward("initFinalFalseMarksEntry");
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
			return mapping.findForward("initFinalFalseMarksEntry");
		
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initExamMarksEntryForRegular(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		newExamMarksEntryForm.setRegular(true);
		newExamMarksEntryForm.setInternal(false);
		newExamMarksEntryForm.setExamType("Regular");
		HttpSession session = request.getSession();
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		//setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
	}
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	
	//raghu newly implemented for entering all internal marks
	
	

	
	
	
	
	
	private void setRequiredDatatoForm(NewExamMarksEntryForm newExamMarksEntryForm,HttpServletRequest request) throws Exception{
	ExamMarksEntryHandler securedhandler = ExamMarksEntryHandler.getInstance();
	//added by Smitha 
	int year=CurrentAcademicYear.getInstance().getAcademicyear();
	if(newExamMarksEntryForm.getYear()!=null && !newExamMarksEntryForm.getYear().isEmpty()){
		year=Integer.parseInt(newExamMarksEntryForm.getYear());
	}
	if(year==0){
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
	}
	Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByExamTypeAndYear(newExamMarksEntryForm.getExamType(),year); ;// getting exam list based on exam Type and academic year
	examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);// sorting the exam names based on name
	newExamMarksEntryForm.setExamNameList(examNameMap);// setting the examNameMap to form
	ExamMarksEntryHandler examhandler = new ExamMarksEntryHandler();
	String currentExam=examhandler.getCurrentExamName(newExamMarksEntryForm.getExamType());
	if((newExamMarksEntryForm.getExamId()==null || newExamMarksEntryForm.getExamId().trim().isEmpty()) && currentExam!=null){
		newExamMarksEntryForm.setExamId(currentExam);
	}
	//ends
	
	setUserId(request, newExamMarksEntryForm);
	if(newExamMarksEntryForm.getExamId()!=null && !newExamMarksEntryForm.getExamId().isEmpty()){
		if(CMSConstants.ROLE_ID_TEACHER.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId().trim())) && newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
		{
			Map<Integer, String> courseMap = CommonAjaxExamHandler.getInstance().getCourseByExamNameByTeacher(newExamMarksEntryForm.getExamId(),newExamMarksEntryForm.getUserId(),year+"");
			courseMap=CommonUtil.sortMapByValue(courseMap);
			newExamMarksEntryForm.setCourseMap(courseMap);
			if(newExamMarksEntryForm.getCourseId()!=null && !newExamMarksEntryForm.getCourseId().isEmpty()){
				Map<String, String> schemeMap = CommonAjaxExamHandler.getInstance().getSchemeNoByExamIdCourseIdByTeacher(Integer.parseInt(newExamMarksEntryForm.getExamId()), Integer.parseInt(newExamMarksEntryForm.getCourseId()),newExamMarksEntryForm.getUserId(),year);
				schemeMap=CommonUtil.sortMapByValue(schemeMap);
				newExamMarksEntryForm.setSchemeMap(schemeMap);
			}else{
				newExamMarksEntryForm.setSchemeMap(null);
			}
		}else{
			Map<Integer, String> courseMap = CommonAjaxHandler.getInstance().getCourseByExamName(newExamMarksEntryForm.getExamId());
			courseMap=CommonUtil.sortMapByValue(courseMap);
			newExamMarksEntryForm.setCourseMap(courseMap);
			if(newExamMarksEntryForm.getCourseId()!=null && !newExamMarksEntryForm.getCourseId().isEmpty()){
				Map<String, String> schemeMap = CommonAjaxHandler.getInstance().getSchemeNoByExamIdCourseId(Integer.parseInt(newExamMarksEntryForm.getExamId()), Integer.parseInt(newExamMarksEntryForm.getCourseId()));
				schemeMap=CommonUtil.sortMapByValue(schemeMap);
				newExamMarksEntryForm.setSchemeMap(schemeMap);
			}else{
				newExamMarksEntryForm.setSchemeMap(null);
			}
		}
		
	}else{
		newExamMarksEntryForm.setCourseMap(null);
		newExamMarksEntryForm.setSchemeMap(null);
	}
	Integer courseId = null, schemeNo = null, subjectId = null, subjectTypeId = null, examName = null,schemeId=null;
	if (CommonUtil.checkForEmpty(newExamMarksEntryForm.getSubjectId())) {
		subjectId = Integer.parseInt(newExamMarksEntryForm.getSubjectId());
	}
	if (newExamMarksEntryForm.getSubjectType() != null
			&& newExamMarksEntryForm.getSubjectType().trim().length() > 0) {
		subjectTypeId = Integer.parseInt(newExamMarksEntryForm
				.getSubjectType());
	}
	if (CommonUtil.checkForEmpty(newExamMarksEntryForm.getCourseId())) {
		courseId = Integer.parseInt(newExamMarksEntryForm.getCourseId());
	}
	if (CommonUtil.checkForEmpty(newExamMarksEntryForm.getSchemeNo())) {
		String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
		if(schemes.length>1){
			schemeNo = Integer.parseInt(schemes[1]);
			schemeId = Integer.parseInt(schemes[0]);
		}else{
			schemeNo = Integer.parseInt(schemes[0]);
		}
	}
	if (CommonUtil.checkForEmpty(newExamMarksEntryForm.getExamId())) {
		examName = Integer.parseInt(newExamMarksEntryForm.getExamId());
	}
	if(courseId!=null && schemeId!=null && schemeNo!=null && examName!=null){
		Map<Integer, String> subjectMap = CommonAjaxHandler.getInstance().getSubjectsByCourseSchemeExamId(courseId, schemeId,schemeNo, examName);
		newExamMarksEntryForm.setSubjectMap(subjectMap);
	}else{
		newExamMarksEntryForm.setSubjectMap(null);
	}
	if(examName!=null && courseId!=null && schemeId!=null && schemeNo!=null){
		Integer academicYear = CommonAjaxHandler.getInstance().getAcademicYearByExam(examName);
		Map<Integer, String> sectionMap = null;
		if(academicYear ==null){
			academicYear = 0;
		}
		if(CMSConstants.ROLE_ID_TEACHER.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId().trim())) && newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
		{
			sectionMap = CommonAjaxHandler.getInstance().getSectionByCourseIdSchemeIdByTeacher(courseId.toString(), schemeId.toString(),schemeNo.toString(), academicYear.toString(),newExamMarksEntryForm.getUserId());
		}
		else
		{
			sectionMap = CommonAjaxHandler.getInstance().getSectionByCourseIdSchemeId(courseId.toString(), schemeId.toString(),schemeNo.toString(), academicYear.toString());
		}
		newExamMarksEntryForm.setSectionMap(sectionMap);
	}
	
	if((courseId!=null && schemeNo!=null) || (subjectId!=null && subjectTypeId!=null && examName!=null && courseId!=null && schemeNo!=null)){
		Map<Integer, String> evaluatorList = securedhandler.get_evaluatorList_ruleSettings(courseId, schemeNo, subjectId, subjectTypeId, examName);
		newExamMarksEntryForm.setEvaluatorList(evaluatorList);
		if(evaluatorList!=null && !evaluatorList.isEmpty()){
			newExamMarksEntryForm.setValidationET("yes");
			newExamMarksEntryForm.setDisplayET("yes");
		}else{
			newExamMarksEntryForm.setValidationET("no");
			newExamMarksEntryForm.setDisplayET("no");
		}
		Map<Integer, String> answerScriptType = securedhandler.getListanswerScriptType(courseId,schemeNo,subjectId,subjectTypeId,examName);
		newExamMarksEntryForm.setAnswerScriptTypeList(answerScriptType);
		if(!answerScriptType.isEmpty()){
			newExamMarksEntryForm.setValidationAST("yes");
			newExamMarksEntryForm.setDisplayAST("yes");
		}else{
			newExamMarksEntryForm.setValidationAST("no");
			newExamMarksEntryForm.setDisplayAST("no");
		}
	}else{
		newExamMarksEntryForm.setValidationET("no");
		newExamMarksEntryForm.setDisplayET("yes");
		newExamMarksEntryForm.setValidationAST("no");
		newExamMarksEntryForm.setDisplayAST("yes");
		newExamMarksEntryForm.setEvaluatorList(null);
		newExamMarksEntryForm.setAnswerScriptTypeList(null);
	}
	
	if(newExamMarksEntryForm.getExamType()!=null && !newExamMarksEntryForm.getExamType().isEmpty()){
		if(newExamMarksEntryForm.getExamType().equalsIgnoreCase("Supplementary")){
			if (CommonUtil.checkForEmpty(newExamMarksEntryForm.getSubjectId())) 
				newExamMarksEntryForm.setSubjectTypeMap(NewExamMarksEntryHandler.subjectTypeMapForSupplementary);
			else
				newExamMarksEntryForm.setSubjectTypeMap(null);
		}else{
				newExamMarksEntryForm.setSubjectTypeMap(NewExamMarksEntryHandler.subjectTypeMap);
		}
		if(newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal")){
			// if exam Type is supplementary dont display evaluator and answerscript both
			newExamMarksEntryForm.setValidationET("no");
			newExamMarksEntryForm.setDisplayET("no");
			newExamMarksEntryForm.setValidationAST("no");
			newExamMarksEntryForm.setDisplayAST("no");
			newExamMarksEntryForm.setEvaluatorList(null);
			newExamMarksEntryForm.setAnswerScriptTypeList(null);
		}
	}else{
		newExamMarksEntryForm.setSubjectTypeMap(null);
	}
	
	if(courseId!=null && schemeId!=null && schemeNo!=null && examName!=null){
		Map<Integer, String>  subjectCodeNameMap = null;
		if(CMSConstants.ROLE_ID_TEACHER.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId().trim())) && newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
		{
			subjectCodeNameMap  =CommonAjaxExamHandler.getInstance().getSubjectsCodeNameByCourseSchemeExamIdByTeacher(newExamMarksEntryForm.getDisplaySubType(), courseId, schemeId,schemeNo, examName,newExamMarksEntryForm.getUserId());
		}
		else
		{
			subjectCodeNameMap  =CommonAjaxExamHandler.getInstance().getSubjectsCodeNameByCourseSchemeExamId(newExamMarksEntryForm.getDisplaySubType(), courseId, schemeId,schemeNo, examName);
		}
		newExamMarksEntryForm.setSubjectCodeNameMap(subjectCodeNameMap);  

	}
}

	public ActionForward printList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		FalseNumExamMarksEntryHandler.getInstance().setprintData();
		return mapping.findForward("printFalseMarks");
	}
	
	
}