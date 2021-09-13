package com.kp.cms.actions.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.admin.EvaluationStudentFeedback;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.admin.EvaluationStudentFeedbackForm;
import com.kp.cms.handlers.admin.EvaStudentFeedbackOpenConnectionHandler;
import com.kp.cms.handlers.admin.EvaluationStudentFeedbackHandler;
import com.kp.cms.handlers.usermanagement.StudentLoginHandler;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;
import com.kp.cms.to.admin.EvaluationStudentFeedbackTO;
import com.kp.cms.to.admin.StudentFeedbackInstructionsTO;
import com.kp.cms.to.admin.TeacherClassSubjectTO;
import com.kp.cms.to.studentfeedback.EvaStudentFeedBackQuestionTo;

public class EvaluationStudentFeedbackAction extends BaseDispatchAction{
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initEvaluationStudentFeedback(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EvaluationStudentFeedbackForm evaStudentFeedbackForm = (EvaluationStudentFeedbackForm)form;
		setUserId(request, evaStudentFeedbackForm);
		HttpSession session = request.getSession();
		String studentid = (String) session.getAttribute("studentid");
		String courseId = (String) session.getAttribute("stuCourseId");
		String classId = (String) session.getAttribute("ClassId");
		String programTypeId= (String) session.getAttribute("programTypeId");
		/* newly added code */
		int admApplnId = (Integer) session.getAttribute("admApplnId");
		String classSchemeWiseId = (String) session.getAttribute("classSchemeWiseId");
		/*----------------*/
		evaStudentFeedbackForm.setTeacherClassSubjectToList(null);
		evaStudentFeedbackForm.setTeacherId(0);
		evaStudentFeedbackForm.setTeacherName(null);
		evaStudentFeedbackForm.setSubjectId(null);
		evaStudentFeedbackForm.setSubjectName(null);
		evaStudentFeedbackForm.setSubjectNo(0);
		evaStudentFeedbackForm.setStudentId(0);
		evaStudentFeedbackForm.setFacultyEvaluationTo(null);
		evaStudentFeedbackForm.setInstructionsList(null);
		evaStudentFeedbackForm.setEvaluationFeedbackId(0);
		evaStudentFeedbackForm.setTotalSubjects(0);
		evaStudentFeedbackForm.setSessionId(0);
		evaStudentFeedbackForm.setAdmApplnId(0);
		evaStudentFeedbackForm.setClassSchemewiseId(null);
		try{
			//int sessionId = (Integer) session.getAttribute("SESSIONID");
			//evaStudentFeedbackForm.setSessionId(sessionId);
			evaStudentFeedbackForm.setStudentId(Integer.parseInt(studentid));
			evaStudentFeedbackForm.setCourseId(courseId);
			evaStudentFeedbackForm.setClassId(classId);
			evaStudentFeedbackForm.setAdmApplnId(admApplnId);
			evaStudentFeedbackForm.setClassSchemewiseId(classSchemeWiseId);
			//raghu for ug check only
		if(programTypeId.equalsIgnoreCase("1")){
		
			EvaluationStudentFeedbackHandler.getInstance().getStudentTotalAttendancePercentage(Integer.valueOf(studentid.trim()),Integer.valueOf(classId.trim()),evaStudentFeedbackForm);
			//EvaluationStudentFeedbackHandler.getInstance().calculateStudentAttendance(Integer.valueOf(studentid.trim()),evaStudentFeedbackForm,courseId);
			/*InputStream inStr = CommonUtil.class.getClassLoader()
								.getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES)
			
			prop.load(inStr);;*/
			String attendancePercentage=CMSConstants.ATTENDANCE_PERCENTAGE;
			Float maxPercentage = Float.valueOf(attendancePercentage);
			Float studentPercentage = Float.valueOf(evaStudentFeedbackForm.getTotalPercentage());
				if(studentPercentage < maxPercentage){
				return mapping.findForward(CMSConstants.INIT_EVAL_STU_FEEDBACK_ATTENDANCE_SHORTAGE);
			}
			}
		
			//Pavani 
			/*Map<Integer,String> specializationIds = StudentLoginHandler.getInstance().getSpecializationIdsByStudentId(evaStudentFeedbackForm);
			
			-----------
			boolean isFacultyFeedbackAvailable = StudentLoginHandler.getInstance().isFacultyFeedbackAvailable(Integer.parseInt(evaStudentFeedbackForm.getClassId()),session,specializationIds);
				if(isFacultyFeedbackAvailable){
					session.setAttribute("studentFacultyFeedback", true);
				}else{
					session.setAttribute("studentFacultyFeedback", false);
			}*/
			boolean isExist = EvaluationStudentFeedbackHandler.getInstance(). checkStudentIsAlreadyExist(evaStudentFeedbackForm);
			if(isExist){
				return mapping.findForward(CMSConstants.INIT_EVAL_STU_FEEDBACK_ALREADY_EXIST);
			}
			EvaluationStudentFeedbackHandler.getInstance().getStuFeedbackInstructions(evaStudentFeedbackForm);
		}catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			evaStudentFeedbackForm.setErrorMessage(msg);
			evaStudentFeedbackForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.INIT_EVAL_STU_FEEDBACK);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public ActionForward getClasses(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EvaluationStudentFeedbackForm evaStudentFeedbackForm = (EvaluationStudentFeedbackForm)form;
		setUserId(request, evaStudentFeedbackForm);
		List<Integer> feedbackGivenClassIds;
		List<Integer> allClassIdsOfStud = EvaluationStudentFeedbackHandler.getInstance().getAllClassesOfStudent(evaStudentFeedbackForm.getStudentId());
		try{
			List<EvaStudentFeedbackOpenConnectionTo> toList = EvaStudentFeedbackOpenConnectionHandler.getInstance().getDetailsBySessionId(evaStudentFeedbackForm.getSessionId()+"");
			Iterator<EvaStudentFeedbackOpenConnectionTo> itr1 = toList.iterator();
			List<EvaStudentFeedbackOpenConnectionTo> finToList = new ArrayList<EvaStudentFeedbackOpenConnectionTo>();
			while(itr1.hasNext())
			{
				EvaStudentFeedbackOpenConnectionTo to = itr1.next();
				if(allClassIdsOfStud.contains(to.getClassesid())){
					finToList.add(to);
				}					
			}
			
			feedbackGivenClassIds = EvaluationStudentFeedbackHandler.getInstance().getClasses(finToList,evaStudentFeedbackForm);
			if(feedbackGivenClassIds.size()==0){
				evaStudentFeedbackForm.setEvaStudentFeedbackOpenConnectionToList(finToList);				
			}else{
				List<EvaStudentFeedbackOpenConnectionTo> finalClassIdList = new ArrayList<EvaStudentFeedbackOpenConnectionTo>();
				Iterator<EvaStudentFeedbackOpenConnectionTo> itr = finToList.iterator();
				while(itr.hasNext()){
					EvaStudentFeedbackOpenConnectionTo to = itr.next();
					if(feedbackGivenClassIds.contains(to.getClassesid())){
					}else{
						finalClassIdList.add(to);
					}
				}
				evaStudentFeedbackForm.setEvaStudentFeedbackOpenConnectionToList(finalClassIdList);
			}
			
		} catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			evaStudentFeedbackForm.setErrorMessage(msg);
			evaStudentFeedbackForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.EVAL_STU_FEEDBACK_CLASS_DETAILS);
	}
	
	
	public ActionForward getTeachersAndSubjectDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EvaluationStudentFeedbackForm evaStudentFeedbackForm = (EvaluationStudentFeedbackForm)form;
		setUserId(request, evaStudentFeedbackForm);
		try{
			//pavani
			List<TeacherClassSubjectTO> facultyEvaluationList1 = EvaluationStudentFeedbackHandler.getInstance().getDetailsByClassId(evaStudentFeedbackForm);
			//List<TeacherClassSubjectTO> facultyEvaluationList = EvaluationStudentFeedbackHandler.getInstance().getDetails(evaStudentFeedbackForm);
			evaStudentFeedbackForm.setTeacherClassSubjectToList(facultyEvaluationList1);
		} catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			evaStudentFeedbackForm.setErrorMessage(msg);
			evaStudentFeedbackForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.EVAL_STU_FEEDBACK_DETAILS);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getAnsweringCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EvaluationStudentFeedbackForm evaStudentFeedbackForm = (EvaluationStudentFeedbackForm)form;
		setUserId(request, evaStudentFeedbackForm);
		try{
			EvaluationStudentFeedbackHandler.getInstance().getTeacherNameAndSubjectName(evaStudentFeedbackForm);
			List<EvaStudentFeedBackQuestionTo> questionListTo = EvaluationStudentFeedbackHandler.getInstance().getQuestionList(evaStudentFeedbackForm);
			evaStudentFeedbackForm.setQuestionListTo(questionListTo);
		}catch (BusinessException businessException) {
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		} catch (Exception exception) {	
			String msg = super.handleApplicationException(exception);
			evaStudentFeedbackForm.setErrorMessage(msg);
			evaStudentFeedbackForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.FACULTY_EVAL_ANSWERING_CHECK);
	}
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward submitFacultyEvaluationDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		EvaluationStudentFeedbackForm evaStudentFeedbackForm = (EvaluationStudentFeedbackForm)form;
		setUserId(request, evaStudentFeedbackForm);
		HttpSession session = request.getSession(true);
		try{
			evaStudentFeedbackForm = EvaluationStudentFeedbackHandler.getInstance().saveEvaluationFacultyDetailsToList(evaStudentFeedbackForm);
			Integer subjectNo = evaStudentFeedbackForm.getSubjectNo();
			Integer totalSubjects = evaStudentFeedbackForm.getTotalSubjects();
			//for displaying next teacher and subject details
			EvaluationStudentFeedbackHandler.getInstance().getTeacherNameAndSubjectName(evaStudentFeedbackForm);
			List<EvaStudentFeedBackQuestionTo> questionListTo = EvaluationStudentFeedbackHandler.getInstance().getQuestionList(evaStudentFeedbackForm);
			evaStudentFeedbackForm.setQuestionListTo(questionListTo);
			//
			if(totalSubjects.equals(subjectNo)){
				if(session.getAttribute("SESSIONID")!=null){
					int sessionId = (Integer) session.getAttribute("SESSIONID");
					evaStudentFeedbackForm.setSessionId(sessionId);				
				}
				boolean isAdded = EvaluationStudentFeedbackHandler.getInstance().submitEvaluationFacultyList(evaStudentFeedbackForm);
				if(isAdded){
					evaStudentFeedbackForm.setFacultyEvaluationTo(null);
					request.setAttribute("disableChangePasswordLink", false);	//	for enabling change password link once faculty feed back is finished
					return mapping.findForward(CMSConstants.EVAL_STU_FEEDBACK_SUCCESSFUL);
				}else{
					return mapping.findForward(CMSConstants.INIT_EVAL_STU_FEEDBACK_ALREADY_EXIST);
				}
			}
			evaStudentFeedbackForm.setRemarks(null);
			evaStudentFeedbackForm.setAdditionalInfo(null);
		} catch (BusinessException businessException) {
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		} catch (Exception exception) {
			String msg = super.handleApplicationException(exception);
			evaStudentFeedbackForm.setErrorMessage(msg);
			evaStudentFeedbackForm.setErrorStack(exception.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}
			return mapping.findForward(CMSConstants.FACULTY_EVAL_ANSWERING_CHECK);
		}
}
