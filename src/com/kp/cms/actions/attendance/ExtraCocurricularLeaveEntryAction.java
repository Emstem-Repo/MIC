package com.kp.cms.actions.attendance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.attendance.ExtraCocurricularLeaveEntryForm;
import com.kp.cms.handlers.attendance.CocurricularAttendnaceEntryHandler;
import com.kp.cms.to.attendance.CocurricularAttendnaceEntryTo;
import com.kp.cms.to.usermanagement.StudentAttendanceTO;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.handlers.attendance.ExtraCocurricularLeaveEntryHandler;
import com.kp.cms.handlers.reports.StudentWiseAttendanceSummaryHandler;
import com.kp.cms.helpers.attendance.ExtraCocurricularLeaveEntryHelper;
public class ExtraCocurricularLeaveEntryAction extends BaseDispatchAction{
	
	private static final Log log = LogFactory.getLog(ExtraCocurricularLeaveEntryAction.class);
	/**
	 * 
	 * Performs the Extra co curricular Attendance entry
	 *  .
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param form
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return The forward to which control should be transferred,
	 * @throws Exception
	 *             if an exception occurs
	 */
	
	public ActionForward initExtraCocurricularLeaveEntry(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		log.debug("call of initExtraCocurricularLeaveEntry method in ExtraCocurricularLeaveEntryAction.class");
		ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm = (ExtraCocurricularLeaveEntryForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		extraCocurricularLeaveEntryForm.clearAll();
		HttpSession session = request.getSession();
		String studentid = (String) session.getAttribute("studentid");
		String courseId = (String) session.getAttribute("stuCourseId");
		if(studentid!=null && !studentid.isEmpty() && courseId!=null && !courseId.isEmpty())
		{
			setRequiredDataTOForm(extraCocurricularLeaveEntryForm, request, response);
			Map<Date, CocurricularAttendnaceEntryTo> cocurricularAttendnaceEntryTos = ExtraCocurricularLeaveEntryHandler.getInstance().getCocurricularAttendanceMap(extraCocurricularLeaveEntryForm,studentid,courseId);
			if(cocurricularAttendnaceEntryTos.isEmpty()){
				errors.add(CMSConstants.ERROR, new ActionError(CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
				saveErrors(request, errors);
				extraCocurricularLeaveEntryForm.clearAll();
				setRequiredDataTOForm(extraCocurricularLeaveEntryForm, request, response);
				log.info("Exit initAttendanceReEntrySecondPage - getSelectedCandidates size 0");
			}else{
				
				
				Collection<CocurricularAttendnaceEntryTo> clist=cocurricularAttendnaceEntryTos.values();
				List<CocurricularAttendnaceEntryTo> list=new ArrayList<CocurricularAttendnaceEntryTo>(clist);
				Collections.sort(list);
				extraCocurricularLeaveEntryForm.setList(list);
				extraCocurricularLeaveEntryForm.setCocurricularAttendanceEntryToList(cocurricularAttendnaceEntryTos);
				//added code for applying co curricular leave
				
				boolean validateDate = ExtraCocurricularLeaveEntryHandler.getInstance().ispublishDateValid(studentid);
				if(validateDate){
					extraCocurricularLeaveEntryForm.setIsPublished(true);
					extraCocurricularLeaveEntryForm.setDislaySubmitButton(true);
				}else {
					extraCocurricularLeaveEntryForm.setIsPublished(false);
					extraCocurricularLeaveEntryForm.setDislaySubmitButton(false);
				}
			}
		}
		else
		{
			ActionMessage message = new ActionMessage("knowledgepro.show.attendance.message");
			messages.add("messages", message);
			messages.add("messages",new ActionMessage("knowledgepro.show.attendance.totalmessage"));
			saveMessages(request, messages);
		}
		session.setAttribute("map", extraCocurricularLeaveEntryForm.getCocurricularAttendanceEntryToList());
		Map<Date, CocurricularAttendnaceEntryTo> map = (Map<Date, CocurricularAttendnaceEntryTo>)session.getAttribute("map");
		boolean isChecked = ExtraCocurricularLeaveEntryHandler.getInstance().periodChecked(map);
		if(isChecked){
			extraCocurricularLeaveEntryForm.setIsPeriodChecked(true);
		}else {
			extraCocurricularLeaveEntryForm.setIsPeriodChecked(false);
		}
		
		log.debug("end of initExtraCocurricularLeaveEntry method in ExtraCocurricularLeaveEntryAction.class");
		return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_ATTENDANCE_ENTRY);
	}
	private void setRequiredDataTOForm(ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm,HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("call of setRequiredDataTOForm method in ExtraCocurricularLeaveEntryAction.class");
		Map<Integer, String> cocurricularActivity= new HashMap<Integer, String>();
		cocurricularActivity =  CocurricularAttendnaceEntryHandler.getInstance().getActivityMap();
		extraCocurricularLeaveEntryForm.setCocurriculartActivityMap(cocurricularActivity);
		
		
		log.debug("end of setRequiredDataTOForm method in ExtraCocurricularLeaveEntryAction.class");
		
	}
	public ActionForward  saveCocurricularDetails(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		log.debug("call of saveCocurricularDetails method in ExtraCocurricularLeaveEntryAction.class");
		ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm = (ExtraCocurricularLeaveEntryForm)form;
		HttpSession session = request.getSession();
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		boolean isAdded = false;
		try
		{
			 
			isAdded = ExtraCocurricularLeaveEntryHandler.getInstance().saveCocurricularDetails(extraCocurricularLeaveEntryForm);
			if(isAdded)
			{
				ActionMessage message = new ActionMessage("knowledgepro.attendance.studentLogin.extra.curricular.leave.entry.subit.success" );
				messages.add("messages", message);
				saveMessages(request, messages);
				extraCocurricularLeaveEntryForm.clearAll();
				setRequiredDataTOForm(extraCocurricularLeaveEntryForm, request,response);
			}
			else
			{
				ActionMessage message = new ActionMessage("knowledgepro.attendance.studentLogin.extra.curricular.leave.entry.subit.failed" );
				messages.add("messages", message);
				saveMessages(request, messages);
				setRequiredDataTOForm(extraCocurricularLeaveEntryForm, request,response);
			}
			
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			setRequiredDataTOForm(extraCocurricularLeaveEntryForm,request,response);
			errors.add("error", new ActionError(e.getMessage()));
	 		saveErrors(request,errors);
	 		return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_ATTENDANCE_ENTRY);
			// TODO: handle exception
		}
		log.debug("end of saveCocurricularDetails method in ExtraCocurricularLeaveEntryAction.class");
		//return mapping.findForward(CMSConstants.EXTRA_COCURRICULAR_ATTENDANCE_ENTRY_SUCCES);
		return initExtraCocurricularLeaveEntry(mapping, extraCocurricularLeaveEntryForm, request, response);

	}
	public ActionForward printCoCurricularLeaveApplication(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm = (ExtraCocurricularLeaveEntryForm)form;
		HttpSession session = request.getSession();
		String studentid = (String) session.getAttribute("studentid");
		String courseId = (String) session.getAttribute("stuCourseId");
		Map<Date, CocurricularAttendnaceEntryTo> map = (Map<Date, CocurricularAttendnaceEntryTo>)session.getAttribute("map");
		String date = CommonUtil.getTodayDate();
		com.ibm.icu.text.DateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date d = df.parse(date);
		com.ibm.icu.text.DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
		String date1 = df1.format(d);
		extraCocurricularLeaveEntryForm.setDate(date1);
		Student student = ExtraCocurricularLeaveEntryHandler.getStudent(studentid);
		extraCocurricularLeaveEntryForm.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
		extraCocurricularLeaveEntryForm.setRegisterNo(student.getRegisterNo());
		extraCocurricularLeaveEntryForm.setClassName(student.getClassSchemewise().getClasses().getName());
		extraCocurricularLeaveEntryForm.setTermNo(String.valueOf(student.getClassSchemewise().getCurriculumSchemeDuration().getSemesterYearNo()));
		List<CocurricularAttendnaceEntryTo> leaveList = ExtraCocurricularLeaveEntryHandler.getInstance().getList(map,extraCocurricularLeaveEntryForm);
		String totalLeaveHrs = ExtraCocurricularLeaveEntryHandler.getInstance().getTotalHrs(leaveList);
		String approvedLeaveHrs = ExtraCocurricularLeaveEntryHandler.getInstance().approvedLeaveHrs(leaveList);
		List<CocurricularAttendnaceEntryTo> finalList = ExtraCocurricularLeaveEntryHandler.getInstance().finalLeaveList(leaveList);
		int classId = student.getClassSchemewise().getClasses().getId();
		float totalPercentages = 0f;
		float totalclassesConducted1 = 0f;
		float totalclassesPresent1 = 0f;
		float classesforelegibility = 0f;
		float shortageOfAttendance = 0f;
		List<Object[]> classesConductedList = ExtraCocurricularLeaveEntryHandler.getInstance().getclassesconducted(studentid,classId);
		long totalclassesConducted = ExtraCocurricularLeaveEntryHelper.getInstance().getTotalClasses(classesConductedList);
		 totalclassesConducted1 = Float.parseFloat(String.valueOf(totalclassesConducted));
		List<Object[]> classesPresentList = ExtraCocurricularLeaveEntryHandler.getInstance().getclassespresent(studentid,classId);
		long totalclassesPresent = ExtraCocurricularLeaveEntryHelper.getInstance().getTotalPresent(classesPresentList);
		totalclassesPresent1 = Float.parseFloat(String.valueOf(totalclassesPresent));
		totalPercentages=CommonUtil.roundToDecimal(((totalclassesPresent1/totalclassesConducted1)*100), 2);
		double round = CommonUtil.Round((0.75*(totalclassesConducted1)), 0);
		classesforelegibility = Float.parseFloat(String.valueOf(round));
		 
		if(round > totalclassesPresent1){
			 shortageOfAttendance = classesforelegibility - totalclassesPresent1;
		}else {
			shortageOfAttendance = 0;
		}
		extraCocurricularLeaveEntryForm.setWorkingHours(String.valueOf(totalclassesConducted));
		extraCocurricularLeaveEntryForm.setPresentHours(String.valueOf(totalclassesPresent));
		extraCocurricularLeaveEntryForm.setPercentage(String.valueOf(totalPercentages));
		extraCocurricularLeaveEntryForm.setLeaveList(finalList);
		extraCocurricularLeaveEntryForm.setRequiredHrs(String.valueOf(classesforelegibility));
		extraCocurricularLeaveEntryForm.setShortageOfAttendance(String.valueOf(shortageOfAttendance));
		extraCocurricularLeaveEntryForm.setSubTotalHrs(totalLeaveHrs);
		//extraCocurricularLeaveEntryForm.setApprovedLeaveHrs(approvedLeaveHrs);
		int approvedLeave =StudentWiseAttendanceSummaryHandler.getInstance().getAttendanceDataByStudentForCoCurricular(studentid,extraCocurricularLeaveEntryForm);
		extraCocurricularLeaveEntryForm.setApprovedLeaveHrs(String.valueOf(approvedLeave));
		return mapping.findForward(CMSConstants.PRINT_EXTRA_COCURRICULAR_LEAVE_APPLICATIION);
	}
	
	
}
