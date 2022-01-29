package com.kp.cms.actions.exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kp.cms.bo.admin.AcademicYear;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.exam.FalseNumSiNoForm;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.ajax.CommonAjaxExamHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.handlers.exam.FalseNumSiNoHandler;
import com.kp.cms.handlers.exam.NewExamMarksEntryHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.FalseNumSiNoTO;
import com.kp.cms.transactionsimpl.attendance.AcademicyearTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;

public class FalseNumSiNoAction extends BaseDispatchAction{
	private static final Log log = LogFactory.getLog(FalseNumSiNoAction.class);
	
	
	public ActionForward initSiNo(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		try{
			cardSiNoForm.reset();
			setData(cardSiNoForm);
			setUserId(request, cardSiNoForm);
			setRequiredDatatoForm(cardSiNoForm, request);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_SI_NO);
	}
	
	private void setData(FalseNumSiNoForm cardSiNoForm) throws Exception {
		// TODO Auto-generated method stub
		AcademicyearTransactionImpl academicyearTransactionImpl=new AcademicyearTransactionImpl();
		List<FalseNumSiNoTO> toList = FalseNumSiNoHandler.getInstance().getDataConvert();
		cardSiNoForm.setToList(toList);
		List<ProgramTypeTO> programTypeList = ProgramTypeHandler.getInstance().getProgramType();
		cardSiNoForm.setProgramTypeList(programTypeList);
		List<AcademicYear> list=academicyearTransactionImpl.getAllAcademicYear();
		Map<String,String> yearMap=new HashMap<String, String>();
		for (AcademicYear academicYear : list) {
			yearMap.put(String.valueOf(academicYear.getYear()), String.valueOf(academicYear.getYear()) + "-" + String.valueOf(academicYear.getYear()+1));
		}
		cardSiNoForm.setBatchMap(CommonUtil.sortMapByValue(yearMap));
	}

	public ActionForward save(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		errors = cardSiNoForm.validate(mapping, request);
		try{
			if(errors.isEmpty()){
				if(StringUtils.isNumeric(cardSiNoForm.getStartNo())){
					String semString[]=cardSiNoForm.getSemister().split("_");
					cardSiNoForm.setSemister(semString[1]);
					boolean isAvailable = FalseNumSiNoHandler.getInstance().getData(cardSiNoForm);
					if(isAvailable){
						errors.add("errors",new ActionError(CMSConstants.ALREADY_AVAILABLE));
						saveErrors(request, errors);
						List<FalseNumSiNoTO> toList = FalseNumSiNoHandler.getInstance().getDataConvert();
						cardSiNoForm.setToList(toList);
						cardSiNoForm.reset();
						setData(cardSiNoForm);
						return mapping.findForward(CMSConstants.INIT_SI_NO);
					}else{
						boolean isAdded = FalseNumSiNoHandler.getInstance().save(cardSiNoForm);
						if(isAdded){
							ActionMessage message = new ActionMessage("knowledgepro.exam.false.si.no.added");
							messages.add(CMSConstants.MESSAGES, message);
							saveMessages(request, messages);
							cardSiNoForm.reset();
							cardSiNoForm.setIsAdded(true);
							List<FalseNumSiNoTO> toList = FalseNumSiNoHandler.getInstance().getDataConvert();
							cardSiNoForm.setToList(toList);
							setRequiredDatatoForm(cardSiNoForm, request);
							cardSiNoForm.reset();
							setData(cardSiNoForm);
							return mapping.findForward(CMSConstants.INIT_SI_NO);
						}
					}
				}else {
					errors.add("errors", new ActionError(CMSConstants.ENTER_ONLY_NUMERIC));
					saveErrors(request, errors);
					setData(cardSiNoForm);
					return mapping.findForward(CMSConstants.INIT_SI_NO);
					
				}
			  }else{
				saveErrors(request, errors);
				setData(cardSiNoForm);
				return mapping.findForward(CMSConstants.INIT_SI_NO);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		setData(cardSiNoForm);
		return mapping.findForward(CMSConstants.INIT_SI_NO);
	}
	private void setRequiredDatatoForm(FalseNumSiNoForm newExamMarksEntryForm,HttpServletRequest request) throws Exception{
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
		newExamMarksEntryForm.setYear(String.valueOf(year));
		Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByExamTypeAndYear(newExamMarksEntryForm.getExamType(),year); ;// getting exam list based on exam Type and academic year
		examNameMap = (Map<Integer, String>) CommonUtil.sortMapByValue(examNameMap);// sorting the exam names based on name
		newExamMarksEntryForm.setExamNameList(examNameMap);// setting the examNameMap to form
		ExamMarksEntryHandler examhandler = new ExamMarksEntryHandler();
		String currentExam=examhandler.getCurrentExamName(newExamMarksEntryForm.getExamType());
		if((newExamMarksEntryForm.getExamId()==null || newExamMarksEntryForm.getExamId().trim().isEmpty()) && currentExam!=null){
			newExamMarksEntryForm.setExamId(currentExam);
		}
		//ends
		if(newExamMarksEntryForm.getExamId()!=null && !newExamMarksEntryForm.getExamId().isEmpty()){
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
				schemeNo = Integer.parseInt(schemes[1]);
				schemeId = Integer.parseInt(schemes[0]);
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
			if(academicYear ==null){
				academicYear = 0;
			}
			Map<Integer, String> sectionMap = CommonAjaxHandler.getInstance().getSectionByCourseIdSchemeId(courseId.toString(), schemeId.toString(),schemeNo.toString(), academicYear.toString());
			newExamMarksEntryForm.setSectionMap(sectionMap);
		}
		if(courseId!=null && schemeId!=null && schemeNo!=null && examName!=null){
			Map<Integer, String>  subjectCodeNameMap  =CommonAjaxExamHandler.getInstance().getSubjectsCodeNameByCourseSchemeExamId(newExamMarksEntryForm.getDisplaySubType(), courseId, schemeId,schemeNo, examName);
			newExamMarksEntryForm.setSubjectCodeNameMap(subjectCodeNameMap);  
		}
	}
	public ActionForward editDetails(ActionMapping mapping,ActionForm form,
										HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		ActionErrors errors = new ActionErrors();
		try{
			FalseNumSiNoHandler.getInstance().setEditDetails(cardSiNoForm);
			setRequiredDatatoForm(cardSiNoForm, request);
			Object myKey = cardSiNoForm.getSchemeMap().keySet().toArray()[0];
			cardSiNoForm.setSemister(myKey.toString());
			request.setAttribute("editOption", "edit");
		}catch (Exception e) {
			e.printStackTrace();
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.INIT_SI_NO);
		}
		return mapping.findForward(CMSConstants.INIT_SI_NO);
	}
	@SuppressWarnings("deprecation")
	public ActionForward updateDetails(ActionMapping mapping,ActionForm form,
										HttpServletRequest request,HttpServletResponse  response)throws Exception{
		FalseNumSiNoForm numSiNoForm = (FalseNumSiNoForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		boolean isUpdated=false;
		try{
			isUpdated =FalseNumSiNoHandler.getInstance().updateDetails(numSiNoForm);
			if(isUpdated){
				ActionMessage message = new ActionMessage("knowledgepro.admin.OpenCourseAllotedSeats.updatesuccess");
				messages.add(CMSConstants.MESSAGES, message);
				saveMessages(request, messages);
				numSiNoForm.reset();
				numSiNoForm.setIsAdded(true);
				List<FalseNumSiNoTO> toList = FalseNumSiNoHandler.getInstance().getDataConvert();
				numSiNoForm.setToList(toList);
				setRequiredDatatoForm(numSiNoForm, request);
				numSiNoForm.reset();
				setData(numSiNoForm);
				return mapping.findForward(CMSConstants.INIT_SI_NO);
			}else{
				errors.add("error", new ActionError("knowledgepro.admin.OpenCourseAllotedSeats.updatefailure"));
				saveErrors(request, errors);
				addErrors(request, errors);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			errors.add("error", new ActionError("knowledgepro.admin.OpenCourseAllotedSeats.updatefailure"));
			saveErrors(request, errors);
			addErrors(request, errors);
			return mapping.findForward(CMSConstants.INIT_SI_NO);
		}
		return mapping.findForward(CMSConstants.INIT_SI_NO);
	}
	
	public ActionForward initFalseBox(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		try{
			cardSiNoForm.reset();
			setRequiredBoxDatatoForm(cardSiNoForm, request);
			setUserId(request, cardSiNoForm);
			FalseNumSiNoHandler.getInstance().setFalseBoxList(cardSiNoForm);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward("initBOX");
	}

	private void setRequiredBoxDatatoForm(FalseNumSiNoForm newExamMarksEntryForm, HttpServletRequest request) throws Exception {

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
		
				sectionMap = CommonAjaxHandler.getInstance().getSectionByCourseIdSchemeId(courseId.toString(), schemeId.toString(),schemeNo.toString(), academicYear.toString());
			newExamMarksEntryForm.setSectionMap(sectionMap);
		}
		
	
		
		if(courseId!=null && schemeId!=null && schemeNo!=null && examName!=null){
			Map<Integer, String>  subjectCodeNameMap = null;
			
				subjectCodeNameMap  =CommonAjaxExamHandler.getInstance().getSubjectsCodeNameByCourseSchemeExamId(newExamMarksEntryForm.getDisplaySubType(), courseId, schemeId,schemeNo, examName);
			newExamMarksEntryForm.setSubjectCodeNameMap(subjectCodeNameMap);  

		}
		
	
		
	}
	
	public ActionForward initFalseBoxSecondPage(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		if (cardSiNoForm.getYear()==null || cardSiNoForm.getYear().isEmpty()) {
			errors.add("error", new ActionError("knowledgepro.fee.academicyear.required"));
		}
		if (cardSiNoForm.getExamId()==null || cardSiNoForm.getExamId().isEmpty()) {
			errors.add("error", new ActionError("admissionFormForm.education.exam.required"));
		}
		if (cardSiNoForm.getCourseId()==null || cardSiNoForm.getCourseId().isEmpty()) {
			errors.add("error", new ActionError("knowledgepro.employee.course.required"));
		}
		if (cardSiNoForm.getSchemeNo()==null || cardSiNoForm.getSchemeNo().isEmpty()) {
			errors.add("error", new ActionError("knowledgepro.att.condon.semester.required"));
		}
		if (cardSiNoForm.getSubjectId()==null || cardSiNoForm.getSubjectId().isEmpty()) {
			errors.add("error", new ActionError("knowledgepro.exam.uSubjectCode.special.required"));
		}
		if (cardSiNoForm.getBoxNo()==null || cardSiNoForm.getBoxNo().isEmpty()) {
			//errors.add("error", new ActionError("knowledgepro.att.condon.semester.required"));
			//errors.add(messages);
		}
		if (errors.isEmpty()) {
		
		try{
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		}else{
			//errors.add("errors", new ActionError(CMSConstants.ENTER_ONLY_NUMERIC));
			saveErrors(request, errors);
			return mapping.findForward("initBOX");
		}
		return mapping.findForward("initBOXSecond");
	}
	public ActionForward updateBarCodeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		String bcode=null;
		List<String> bcodeList=null;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		try{
			if (cardSiNoForm.getDeleId()!=null ) {
				bcodeList=cardSiNoForm.getBarcodeList();
				bcodeList.remove(cardSiNoForm.getDeleId());
				cardSiNoForm.setBarcodeList(bcodeList);
				cardSiNoForm.setDeleId(null);
				
			}else{
				if (cardSiNoForm.getBarcodeList()!=null) {
					bcodeList=cardSiNoForm.getBarcodeList();
				}else{
					bcodeList=new ArrayList();
				}
			
				bcode=cardSiNoForm.getBarcode();
				if (bcodeList!=null) {
					for (String id : bcodeList) {
						if (bcode==id ||bcode.equalsIgnoreCase(id)) {
							errors.add("error", new ActionError("inventory.stockReceipt.amc.itemNo.duplicate"));
							saveErrors(request, errors);
							return mapping.findForward("initBOXSecond");
						}
					}
				}
				if (bcodeList.size()<30 && !bcode.isEmpty()) {
					bcodeList.add(bcode);
				}else{
					errors.add("error", new ActionError("knowledgepro.exam.false.limit.exeed"));
					saveErrors(request, errors);
				}
				
			}	
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward("initBOXSecond");
	}
	public ActionForward saveBarCodeList(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		FalseNumSiNoForm cardSiNoForm = (FalseNumSiNoForm)form;
		boolean result=false;
		List<String> bcodeList=null;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages =new ActionMessages();
		try{
			
			result=FalseNumSiNoHandler.getInstance().saveFalseBox(cardSiNoForm);
			FalseNumSiNoHandler.getInstance().setFalseBoxList(cardSiNoForm);
			if (result) {
				cardSiNoForm.reset();
				setRequiredBoxDatatoForm(cardSiNoForm, request);
				setUserId(request, cardSiNoForm);
				ActionMessage message = new ActionMessage("knowledgepro.exam.false.si.no.added");
				messages.add(CMSConstants.MESSAGES, message);
				saveMessages(request, messages);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward("initBOX");
	}

}
