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
import com.kp.cms.handlers.exam.NewExamMarksEntryHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.StudentMarksTO1;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;
import com.kp.cms.utilities.ExamComparator;
import com.kp.cms.utilities.StudentRollNoComparator;

public class NewExamMarksEntryAction extends BaseDispatchAction {
	private static final Log log = LogFactory.getLog(NewExamMarksEntryAction.class);
	
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
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
	}

	/**
	 * setting the Requested data to Form
	 * @param newExamMarksEntryForm
	 * @param request
	 * @throws Exception
	 */
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
				Set<StudentMarksTO> studentList=NewExamMarksEntryHandler.getInstance().getStudentForInput(newExamMarksEntryForm);// getting the student list for input search
				if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
					setRequiredDatatoForm(newExamMarksEntryForm, request);			
					return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
				}
				List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);
				
				if(newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
				{
					Iterator<StudentMarksTO> itr=list.iterator();
					while(itr.hasNext()){
						StudentMarksTO to = itr.next();
						if(to.getIsTheorySecured())
						{
							newExamMarksEntryForm.setMarksEntryExist(true);
							
						}else{
							newExamMarksEntryForm.setMarksEntryExist(false);
						}
						if(newExamMarksEntryForm.isMarksEntryExist())
							break;						
					}

					if(CMSConstants.TEACHER_ID.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId())))
						newExamMarksEntryForm.setTeacher(true);
					else
						newExamMarksEntryForm.setTeacher(false);
					if(CMSConstants.CONTROLLER_ID.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId())))
						newExamMarksEntryForm.setController(true);
					else
						newExamMarksEntryForm.setController(false);
				}
				
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
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
		}
		log.info("Entered NewExamMarksEntryAction - getCandidates");
		if(newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT_INTERNAL);
		else
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT);
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
				setRequiredDatatoForm(newExamMarksEntryForm, request);
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
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		if(newExamMarksEntryForm.isInternal())
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL);
		else
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
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
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
	public ActionForward initExamMarksEntryForInternal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		HttpSession session = request.getSession();
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		newExamMarksEntryForm.setRegular(false);
		newExamMarksEntryForm.setInternal(true);
		newExamMarksEntryForm.setExamType("Internal");
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		Properties prop = new Properties();
		InputStream inputStream = CommonUtil.class.getClassLoader().getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
		prop.load(inputStream);
		StringBuffer buf = new StringBuffer();		
		Iterator<Integer> itr = CMSConstants.ROLE_ID_TEACHER.iterator();
		while(itr.hasNext())
		{
			Integer roles = itr.next();
			buf.append(roles);
			buf.append(",");
		}
		buf.deleteCharAt(buf.length()-1);
		newExamMarksEntryForm.setRoleIdForTeacher(buf.toString());		
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL);
	}
	
	// False No Entry
	
	public ActionForward initFalseNoEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		boolean isFalsenoscreen=true;
		HttpSession session = request.getSession();
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		//newExamMarksEntryForm.setFalsenoscreen(isFalsenoscreen);
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
	}
	
	public ActionForward initFalseNoEntryForEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry input");
		boolean isFalsenoscreen=true;
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		if(newExamMarksEntryForm.getYear()==null)
		newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		setRequiredDatatoForm(newExamMarksEntryForm, request);// setting the requested data to form
		//newExamMarksEntryForm.setFalsenoscreen(isFalsenoscreen);
		log.info("Exit initExamMarksEntry input");
		
		return mapping.findForward("initFalseEntryForEdit");
	}

	public ActionForward saveFalseNumber(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		boolean isadded=false;
		boolean isduplicated=false;
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		ActionMessages messages=new ActionMessages();
		validateExamMarksEntry(newExamMarksEntryForm,errors);
		String schemeNo = null;
		if (errors.isEmpty()) {
			try {
				String[] semArr=newExamMarksEntryForm.getSchemeNo().split("_");
				newExamMarksEntryForm.setSemister(semArr[1]);
				int currentNO = NewExamMarksEntryHandler.getInstance().getCurrentNO(newExamMarksEntryForm);
				
				if(currentNO != -1){
					schemeNo=newExamMarksEntryForm.getSchemeNo();// If no record comes to put the value to form
					String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
					newExamMarksEntryForm.setSchemeNo(schemes[1]);
					newExamMarksEntryForm.setSubjectId(null);
					//Set<StudentMarksTO> studentList=NewExamMarksEntryHandler.getInstance().getStudentForInput(newExamMarksEntryForm);// getting the student list for input search
					/*if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
						errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
						saveErrors(request, errors);
						newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
						setRequiredDatatoForm(newExamMarksEntryForm, request);			
						return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
					}else{*/
						//duplicate check
						ExamFalseNumberHandler.getInstance().getcourseansScheme(newExamMarksEntryForm);
						isduplicated=ExamFalseNumberHandler.getInstance().duplicatecheck(newExamMarksEntryForm);
						if(isduplicated){
							errors.add("error", new ActionError("knowledgepro.exam.FalseNo.exists"));
							saveErrors(request, errors);
							newExamMarksEntryForm.setSchemeNo(schemeNo);
							newExamMarksEntryForm.setSubjectId(null);
							setRequiredDatatoForm(newExamMarksEntryForm, request);	
							return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
						}else{
							/*List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);*/
							/*Collections.sort(list);
							newExamMarksEntryForm.setStudentList(list);*/
							isadded=ExamFalseNumberHandler.getInstance().saveFalseNumbers(newExamMarksEntryForm);
							
							if(isadded){
								boolean isUpdate=ExamFalseNumberHandler.getInstance().updateFalseSiNo(newExamMarksEntryForm);
								messages.add(CMSConstants.MESSAGES, new ActionError("knowledgepro.admin.FalseNo.addsuccess"));
								saveMessages(request, messages);
							}
						}
					//}
					newExamMarksEntryForm.setSchemeNo(schemeNo);
					setNamesToForm(newExamMarksEntryForm);
				}
				else {
					//errors.add("errors", new ActionError(CMSConstants.FALSE_SI_NOT_AVAILABLE));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);
					newExamMarksEntryForm.setSubjectId(null);
					setRequiredDatatoForm(newExamMarksEntryForm, request);
					return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
				}
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				exception.printStackTrace();
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			newExamMarksEntryForm.setSchemeNo(schemeNo);
			newExamMarksEntryForm.setSubjectId(null);
			setRequiredDatatoForm(newExamMarksEntryForm, request);			
			return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
		}
		newExamMarksEntryForm.setSubjectId(null);
		setRequiredDatatoForm(newExamMarksEntryForm, request);
		newExamMarksEntryForm.setSchemeNo(schemeNo);
		return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_INPUT);
	}
	
	
	public ActionForward getCandidatesForFalseNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - getCandidates");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		if (errors.isEmpty()) {
			try {
				request.getSession().setAttribute("schemeNo", newExamMarksEntryForm.getSchemeNo());
				String schemeNo=newExamMarksEntryForm.getSchemeNo();// If no record comes to put the value to form
				String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
				newExamMarksEntryForm.setSchemeNo(schemes[1]);
				
				Set<StudentMarksTO> studentList=NewExamMarksEntryHandler.getInstance().getStudentForInput(newExamMarksEntryForm);// getting the student list for input search
				if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
					setRequiredDatatoForm(newExamMarksEntryForm, request);			
					return mapping.findForward("initFalseEntryForEdit");
				}
				List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);
				List<StudentMarksTO1> list1=new ArrayList<StudentMarksTO1> ();
				Map<String, ExamFalseNumberGen> falseNumberMap = ExamFalseNumberHandler.getInstance().getFlaseNumberMap(newExamMarksEntryForm);
				for(StudentMarksTO student : list) {
					StudentMarksTO1 bo=new StudentMarksTO1();
				    bo.setClassId(student.getClassId());
					bo.setStudentId(student.getStudentId());
					bo.setName(student.getName());
					if(student.getFalseNoId()!=null)
						bo.setFalseNoId(student.getFalseNoId());
					bo.setRegisterNo(student.getRegisterNo());
					if(student.getFalseNo()!=null)
						bo.setFalseNo(student.getFalseNo());
					
					String key = newExamMarksEntryForm.getExamId() + "_" + newExamMarksEntryForm.getCourseId() + "_" + student.getStudentId() + "_" + newExamMarksEntryForm.getSubjectId();
					ExamFalseNumberGen falseNum = falseNumberMap.get(key);
					if(falseNum != null) {
						bo.setFalseNumberGenId(falseNum.getId());
						bo.setFalseNo(falseNum.getFalseNo());
					}
					
					list1.add(bo);
				}
				
				Collections.sort(list1);
				newExamMarksEntryForm.setStudentlist1(list1);
				setNamesToForm(newExamMarksEntryForm);
			}  catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newExamMarksEntryForm, request);			
			log.info("Exit NewExamMarksEntryAction - getSelectedCandidates errors not empty ");
			return mapping.findForward("initFalseEntryForEdit");
		}
		log.info("Entered NewExamMarksEntryAction - getCandidates");
		return mapping.findForward(CMSConstants.EXAM_FALSE_ENTRY_RESULT);
	}
	

	
	public ActionForward saveFalseNumberForEdit(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception {

		log.debug("Enter into MArks Card Generate Screen");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		errors = newExamMarksEntryForm.validate(mapping, request);
		IExamFalseNumberTransaction transaction = new ExamFalseNumberTransactionImpl();
		INewExamMarksEntryTransaction txn=null;
		boolean isadded=false;
		boolean isduplicated=false;
		
		try{
			if(errors.isEmpty())
			{
			//duplicate check
			//ExamFalseNumberHandler.getInstance().getcourseansScheme(marksCardForm);
			//isduplicated=ExamFalseNumberHandler.getInstance().duplicatecheck(marksCardForm);
			//if(isduplicated){
			//	errors.add("error", new ActionError("knowledgepro.exam.FalseNo.exists"));
			//	saveErrors(request, errors);
			//	
			//return mapping.findForward(CMSConstants.ExamFalseNumber_GENERATE);
			//}
		
				setUserId(request, newExamMarksEntryForm);
				isadded=ExamFalseNumberHandler.getInstance().saveFalseNumbersForEdit(newExamMarksEntryForm);
			
			if(isadded){
				ActionMessage message = new ActionError("knowledgepro.admin.FalseNo.addsuccess");
				messages.add("messages", message);
				saveMessages(request, messages);

			}
			}else{
				saveErrors(request,errors);
			}
			setRequiredDatatoForm(newExamMarksEntryForm, request);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return mapping.findForward("initFalseEntryForEdit");
	
	}
	
	
	//raghu newly implemented for entering all internal marks
	public ActionForward initExamMarksEntryForAllInternals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered initExamMarksEntry all interanl input");
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		HttpSession session = request.getSession();
		//newExamMarksEntryForm.resetFields();//Reseting the fields for input jsp
		newExamMarksEntryForm.setStudentMarksList(null);
		newExamMarksEntryForm.setStudentList(null);
		newExamMarksEntryForm.setSubjectId(null);
		newExamMarksEntryForm.setRegular(false);
		newExamMarksEntryForm.setInternal(true);
		newExamMarksEntryForm.setOldMarksCheck(false);
		newExamMarksEntryForm.setExamType("Internal");
		newExamMarksEntryForm.setInternalexamSize(0);
		newExamMarksEntryForm.setRoleId(session.getAttribute("rid").toString());
		
		if(newExamMarksEntryForm.getRoleId()!=null && CMSConstants.INTERNAL_MARKSCARD_MARKSENTER_LIST.contains(Integer.valueOf(newExamMarksEntryForm.getRoleId()))){
			newExamMarksEntryForm.setIsRoleIdForDeveloper(true);
		}
		Properties prop = new Properties();
		InputStream inputStream = CommonUtil.class.getClassLoader().getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
		prop.load(inputStream);
		StringBuffer buf = new StringBuffer();		
		Iterator<Integer> itr = CMSConstants.ROLE_ID_TEACHER.iterator();
		while(itr.hasNext())
		{
			Integer roles = itr.next();
			buf.append(roles);
			buf.append(",");
		}
		buf.deleteCharAt(buf.length()-1);
		newExamMarksEntryForm.setRoleIdForTeacher(buf.toString());		
		setRequiredDatatoFormAllInternals(newExamMarksEntryForm, request);// setting the requested data to form
		log.info("Exit initExamMarksEntry all interanl input");
		
		return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL_ALL);
	}
	
	
	/**
	 * setting the Requested data to Form
	 * @param newExamMarksEntryForm
	 * @param request
	 * @throws Exception
	 */
	private void setRequiredDatatoFormAllInternals(NewExamMarksEntryForm newExamMarksEntryForm,HttpServletRequest request) throws Exception{
		ExamMarksEntryHandler securedhandler = ExamMarksEntryHandler.getInstance();
		//added by Smitha 
		int year=CurrentAcademicYear.getInstance().getAcademicyear();
		int curyear=year;
		if(newExamMarksEntryForm.getYear()!=null && !newExamMarksEntryForm.getYear().isEmpty()){
			year=Integer.parseInt(newExamMarksEntryForm.getYear());
		}
		if(year==0){
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
		}
		
		//old mark check
		if(year<curyear){
			newExamMarksEntryForm.setOldMarksCheck(true);
		}else{
			newExamMarksEntryForm.setOldMarksCheck(false);
		}
		
		//Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByExamTypeAndYear(newExamMarksEntryForm.getExamType(),year); ;// getting exam list based on exam Type and academic year
		//examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);// sorting the exam names based on name
		//newExamMarksEntryForm.setExamNameList(examNameMap);// setting the examNameMap to form
		ExamMarksEntryHandler examhandler = new ExamMarksEntryHandler();
		String currentExam=examhandler.getCurrentExamName(newExamMarksEntryForm.getExamType());
		if((newExamMarksEntryForm.getExamId()==null || newExamMarksEntryForm.getExamId().trim().isEmpty()) && currentExam!=null){
			newExamMarksEntryForm.setExamId(currentExam);
		}
		//ends
		
		setUserId(request, newExamMarksEntryForm);
		
			//if(CMSConstants.ROLE_ID_TEACHER.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId().trim())) && newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
			//{
				Map<Integer, String> courseMap = CommonAjaxExamHandler.getInstance().getCourseByTeacher(newExamMarksEntryForm.getUserId(),year+"");
				courseMap=CommonUtil.sortMapByValue(courseMap);
				newExamMarksEntryForm.setCourseMap(courseMap);
				if(newExamMarksEntryForm.getCourseId()!=null && !newExamMarksEntryForm.getCourseId().isEmpty()){
					Map<String, String> schemeMap = CommonAjaxExamHandler.getInstance().getSchemeNoByCourseIdByTeacher( Integer.parseInt(newExamMarksEntryForm.getCourseId()),newExamMarksEntryForm.getUserId(),year);
					schemeMap=CommonUtil.sortMapByValue(schemeMap);
					newExamMarksEntryForm.setSchemeMap(schemeMap);
				}else{
					newExamMarksEntryForm.setSchemeMap(null);
				}
			//}
			
		
		
		
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
				String schemes1[] = newExamMarksEntryForm.getOldschemeNo().split("_");
				if(schemes1.length>1){
					schemeNo = Integer.parseInt(schemes1[1]);
					schemeId = Integer.parseInt(schemes1[0]);
					newExamMarksEntryForm.setSchemeNo(newExamMarksEntryForm.getOldschemeNo());
				}else{
					schemeNo = Integer.parseInt(schemes[0]);
				}
				
			}
		}
	
	
		
		
		
		
		
		
		/*if((courseId!=null && schemeNo!=null) || (subjectId!=null && subjectTypeId!=null && examName!=null && courseId!=null && schemeNo!=null)){
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
		*/
		
		
		if(courseId!=null && schemeId!=null && schemeNo!=null){
			Map<Integer, String>  subjectCodeNameMap = null;
			//if(CMSConstants.ROLE_ID_TEACHER.contains(Integer.parseInt(newExamMarksEntryForm.getRoleId().trim())) && newExamMarksEntryForm.getExamType().equalsIgnoreCase("Internal"))
			//{
				subjectCodeNameMap  =CommonAjaxExamHandler.getInstance().getSubjectsCodeNameByCourseSchemeIdByTeacher(newExamMarksEntryForm.getDisplaySubType(), courseId, schemeId,schemeNo,newExamMarksEntryForm.getUserId());
			//}
			
			newExamMarksEntryForm.setSubjectCodeNameMap(subjectCodeNameMap);  

		}
		
		
	}
	
	public ActionForward getCandidatesForAllInternals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - all interanl getCandidates");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		 ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		//validateExamMarksEntry(newExamMarksEntryForm,errors);
		if (errors.isEmpty()) {
			try {
				// Commented because change in requirement
				/*boolean checkSecuredMarksEntry=NewExamMarksEntryHandler.getInstance().checkMarksEnteredThroughSecured(newExamMarksEntryForm);//checking whether marks entered through secured marks entry
				if(checkSecuredMarksEntry){// if the marks are entered through secured marks entry display message in first screen
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.marksentry.marks.through.secured"));
					addErrors(request,  errors);
					setRequiredDatatoForm(newExamMarksEntryForm, request);			
					return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT);
				}*/
				
				//check old marks
				int curyear=CurrentAcademicYear.getInstance().getAcademicyear();
				if(!newExamMarksEntryForm.getIsRoleIdForDeveloper()){
				if(Integer.parseInt(newExamMarksEntryForm.getYear())<curyear && Integer.parseInt(newExamMarksEntryForm.getYear())!=curyear-1){
					newExamMarksEntryForm.setOldMarksCheck(true);
				}else{
					newExamMarksEntryForm.setOldMarksCheck(false);
				}
				}else{
					newExamMarksEntryForm.setOldMarksCheck(false);
				}
				//set old scheme no
				newExamMarksEntryForm.setOldschemeNo(newExamMarksEntryForm.getSchemeNo());
				String schemeNo=newExamMarksEntryForm.getSchemeNo();// If no record comes to put the value to form
				String schemes[] = newExamMarksEntryForm.getSchemeNo().split("_");
				newExamMarksEntryForm.setSchemeNo(schemes[1]);
				int year=Integer.parseInt(newExamMarksEntryForm.getYear());
				Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByYearAndCourseAndSem(year, Integer.parseInt(newExamMarksEntryForm.getCourseId()), Integer.parseInt(schemes[1]));
				examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);// sorting the exam names based on name
				newExamMarksEntryForm.setExamNameList(examNameMap);// setting the examNameMap to form
				if(examNameMap.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
					setRequiredDatatoFormAllInternals(newExamMarksEntryForm, request);			
					return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL_ALL);
				}
				List<StudentTO> studentList=NewExamMarksEntryHandler.getInstance().getStudentForInputForAllInternals(newExamMarksEntryForm);// getting the student list for input search
				if(studentList==null || studentList.isEmpty()){// if student list is empty display no record found in the input screen
					errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					newExamMarksEntryForm.setSchemeNo(schemeNo);//setting back the previous value
					setRequiredDatatoFormAllInternals(newExamMarksEntryForm, request);			
					return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL_ALL);
				}
				
							
				//sorting and set studentlist
				StudentRollNoComparator s=new StudentRollNoComparator();
				s.setRegNoCheck(true);
				Collections.sort(studentList, s);
				newExamMarksEntryForm.setStudentMarksList(studentList);
				
				//set exam list
				List<StudentMarksTO> examList=new ArrayList<StudentMarksTO>();
				Iterator<Integer> examsIte=newExamMarksEntryForm.getExamNameList().keySet().iterator();
				while(examsIte.hasNext()){
					StudentMarksTO examto=new StudentMarksTO();
					Integer examId=examsIte.next();
					examto.setExamId(examId+"");
					examto.setExamName(newExamMarksEntryForm.getExamNameList().get(examId));
					examList.add(examto);
				}
				
				ExamComparator e=new ExamComparator();
				e.setCompare(0);
				Collections.sort(examList, e);
				newExamMarksEntryForm.setExamList(examList);
				
				newExamMarksEntryForm.setSubjectCode(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getSubjectId()),"Subject",true,"code"));
				newExamMarksEntryForm.setSubjectName(newExamMarksEntryForm.getSubjectCode()+" - "+NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getSubjectId()),"Subject",true,"name"));
				newExamMarksEntryForm.setCourseName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(Integer.parseInt(newExamMarksEntryForm.getCourseId()),"Course",true,"name"));
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				newExamMarksEntryForm.setDate(sdf.format(new Date()));
			
				//set exam size
				newExamMarksEntryForm.setInternalexamSize(newExamMarksEntryForm.getExamNameList().keySet().size());
				//setNamesToForm(newExamMarksEntryForm);
				
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			//setRequiredDatatoForm(newExamMarksEntryForm, request);			
			log.info("Exit NewExamMarksEntryAction - getSelectedCandidates errors not empty ");
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_INPUT_INTERNAL_ALL);
		}
		log.info("Entered NewExamMarksEntryAction - getCandidates");
		
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT_INTERNAL_ALL);
	}

	
	
	public ActionForward saveMarksForAllInternal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered NewExamMarksEntryAction - all interanl saveMarks");
		
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;// Type casting the Action form to Required Form
		ActionMessages messages=new ActionMessages();
		ActionErrors errors = newExamMarksEntryForm.validate(mapping, request);
		setUserId(request,newExamMarksEntryForm);
		validateMaxMarksForAllInternal(newExamMarksEntryForm,errors);
		if (errors.isEmpty()) {
			try {
				boolean isUpdated =NewExamMarksEntryHandler.getInstance().saveMarksForAllInternals(newExamMarksEntryForm);
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
					setRequiredDatatoFormAllInternals(newExamMarksEntryForm, request);
					getCandidatesForAllInternals(mapping, newExamMarksEntryForm, request, response);
				}else{
					errors.add(CMSConstants.ERROR,new ActionError("kknowledgepro.admin.addfailure","Marks"));
					addErrors(request, errors);
				}
				
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newExamMarksEntryForm.setErrorMessage(msg);
				newExamMarksEntryForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
//			setRequiredDatatoForm(newExamMarksEntryForm, request);
			log.info("Exit NewExamMarksEntryAction - all interanl saveMarks errors not empty ");
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT_INTERNAL_ALL);
		}
		log.info("Entered NewExamMarksEntryAction - all interanl saveMarks");
		
			return mapping.findForward(CMSConstants.EXAM_MARKS_ENTRY_RESULT_INTERNAL_ALL);
		
	}
	
	
	
	private void validateMaxMarksForAllInternal(NewExamMarksEntryForm newExamMarksEntryForm,ActionErrors errors) throws Exception{
		Double maxMark=0.0;
		Map<Integer,Double> maxMarkMap=NewExamMarksEntryHandler.getInstance().getMaxMarkOfSubjectForAllInternals(newExamMarksEntryForm);
		
		if(!newExamMarksEntryForm.getExamType().equals("Supplementary")){
			if(maxMarkMap==null || maxMarkMap.size()==0){
				errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.notDefined"));
			}else{
				boolean breakExam=false;
				List<StudentTO> studentlist=newExamMarksEntryForm.getStudentMarksList();
				Iterator<StudentTO> stuitr=studentlist.iterator();
				while (stuitr.hasNext()) {
					
				if(!breakExam){
				StudentTO sto = (StudentTO) stuitr.next();
				List<StudentMarksTO> list=sto.getStudentMarksList();
				Iterator<StudentMarksTO> itr=list.iterator();
				String reg="";
				String regValid="";
				List<String> examAbscentCode = NewSecuredMarksEntryHandler.getInstance().getExamAbscentCode();
				while (itr.hasNext()) {
					StudentMarksTO to = (StudentMarksTO) itr.next();
					
					
					if((to.getIsTheory() && to.getTheoryMarks()!=null && !to.getTheoryMarks().isEmpty())){
						if(maxMarkMap.containsKey(Integer.parseInt(to.getExamId()))){
							maxMark=maxMarkMap.get(Integer.parseInt(to.getExamId()));
						if(!(to.getTheoryMarks().matches("\\d{0,2}(\\.\\d{1,2})?") )){
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
						
						}else{
							errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.notDefined"));
							breakExam=true;
							break;
						}//contains over
					}
					
				else if(to.getIsPractical() && to.getPracticalMarks()!=null && !to.getPracticalMarks().isEmpty()){
					if(maxMarkMap.containsKey(Integer.parseInt(to.getExamId()))){
						maxMark=maxMarkMap.get(Integer.parseInt(to.getExamId()));
					if(!(to.getPracticalMarks().matches("\\d{0,2}(\\.\\d{1,2})?") )){
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
					}else{
						errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.notDefined"));
						breakExam=true;
						break;
					}
					}
					
				
				
				
				if(!reg.isEmpty()){
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo",reg));
					break;
				}
				if(!regValid.isEmpty()){
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.exam.max.marks.registerNo.validMarks",regValid));
					break;
				}
					
				}//inner list close
				
				}//if break over
				//break main iteration
				else{
					break;
				}
				}//main list close
			}
		}else{/*
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
					if(!(to.getTheoryMarks().matches("\\d{0,2}(\\.\\d{1,2})?"))){
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
					if(!(to.getPracticalMarks().matches("\\d{0,2}(\\.\\d{1,2})?"))){
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
		
		*/}
	}
	
	
	public ActionForward getPrintMarksForAllInternals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;
		setRequiredDatatoFormAllInternals(newExamMarksEntryForm, request);
		getCandidatesForAllInternals(mapping, newExamMarksEntryForm, request, response);	
		return mapping.findForward("examMarksEntryResultInternalAllPrintMarks");
	}
	public ActionForward BarcodeGenerator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		NewExamMarksEntryForm newExamMarksEntryForm = (NewExamMarksEntryForm) form;
		ExamFalseNumberHandler.getInstance().BarcodeGeneration(newExamMarksEntryForm);
		return mapping.findForward("examMarksEntryResultInternalAllPrintMarks");
	}

	
	
}