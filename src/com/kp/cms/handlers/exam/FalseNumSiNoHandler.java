package com.kp.cms.handlers.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.FalseNumSiNo;
import com.kp.cms.bo.exam.FalseNumberBox;
import com.kp.cms.bo.exam.FalseNumberBoxDetails;
import com.kp.cms.forms.exam.FalseNumSiNoForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.helpers.exam.FalseNumSiNoHelper;
import com.kp.cms.to.exam.FalseBoxDetTo;
import com.kp.cms.to.exam.FalseNumSiNoTO;
import com.kp.cms.transactions.ajax.ICommonExamAjax;
import com.kp.cms.transactions.exam.IFalseNumSiNoTransaction;
import com.kp.cms.transactionsimpl.ajax.CommonAjaxExamImpl;
import com.kp.cms.transactionsimpl.exam.FalseNumSiNoTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class FalseNumSiNoHandler {
	private static FalseNumSiNoHandler marksCardSiNoHandler=null;
	public static FalseNumSiNoHandler getInstance(){
		if(marksCardSiNoHandler==null){
			marksCardSiNoHandler = new FalseNumSiNoHandler();
		}
		return marksCardSiNoHandler;
	}
	
	IFalseNumSiNoTransaction transaction = new FalseNumSiNoTransactionImpl();
	
	
	public boolean save(FalseNumSiNoForm cardSiNoForm) throws Exception{
		// TODO Auto-generated method stub
		FalseNumSiNo bo = FalseNumSiNoHelper.getInstance().convertFormToBo(cardSiNoForm);
		return transaction.save(bo);
	}


	public boolean getData(FalseNumSiNoForm cardSiNoForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.getDataAvailable(cardSiNoForm);
	}


	public List<FalseNumSiNoTO> getDataConvert()throws Exception {
		// TODO Auto-generated method stub
		List<FalseNumSiNo> bo = transaction.getData();
		return FalseNumSiNoHelper.getInstance().convertBotoTo(bo);
	}


	public void setEditDetails(FalseNumSiNoForm cardSiNoForm)throws Exception {
		FalseNumSiNo numSiNo = transaction.getFalseNoBoObject(cardSiNoForm);
		cardSiNoForm.setExamType(numSiNo.getExamId().getExamType().getName());
		cardSiNoForm.setExamId(String.valueOf(numSiNo.getExamId().getId()));
		cardSiNoForm.setCourseId(String.valueOf(numSiNo.getCourseId().getId()));
		cardSiNoForm.setSemister(String.valueOf(numSiNo.getSemister()));
		cardSiNoForm.setStartNo(numSiNo.getStartNo());
		cardSiNoForm.setPrefix(numSiNo.getPrefix());
		cardSiNoForm.setCurrentFalseNo(numSiNo.getCurrentNo());
		cardSiNoForm.setYear(String.valueOf(numSiNo.getAcademicYear()));
	}


	public boolean updateDetails(FalseNumSiNoForm numSiNoForm)throws Exception {
		FalseNumSiNo numSiNo =FalseNumSiNoHelper.getInstance().updateFalseNo(numSiNoForm);
		boolean isUpdated =transaction.updateFalseNo(numSiNo);
		return isUpdated;
	}


	public boolean saveFalseBox(FalseNumSiNoForm cardSiNoForm)throws Exception {
		List<FalseNumberBoxDetails> falseNumberBox =FalseNumSiNoHelper.getInstance().convertToFalseNumberBoxDetails(cardSiNoForm);
		
		boolean isDuplicate=transaction.getDuplicate(cardSiNoForm);
		if (!isDuplicate || cardSiNoForm.isEdit()) {
			boolean isUpdated =transaction.updateFalseNoBox(falseNumberBox);
			return isUpdated;
		}
		
		return false;
	}
	public boolean setFalseBoxList(FalseNumSiNoForm cardSiNoForm)throws Exception {
		List<FalseNumberBox> boxList=transaction.getFalseBox(cardSiNoForm);
		List<FalseBoxDetTo> falseNumberBoxTo =FalseNumSiNoHelper.getInstance().setFalseNumberBoxBoTo(boxList);
		if (falseNumberBoxTo!=null && !falseNumberBoxTo.isEmpty()) {
			cardSiNoForm.setFalseBoxToList(falseNumberBoxTo);
			return true;
		}else{
			cardSiNoForm.setFalseBoxToList(new ArrayList<FalseBoxDetTo>());
		}
		
		return false;
	}


	public void editFalseBox(FalseNumSiNoForm cardSiNoForm) throws NumberFormatException, Exception {
		ICommonExamAjax iCommonExamAjax = CommonAjaxExamImpl.getInstance();
		int schemeNo=0;
		FalseNumSiNoTransactionImpl transaction = new FalseNumSiNoTransactionImpl();
		if (CommonUtil.checkForEmpty(cardSiNoForm.getSchemeNo())) {
			String schemeSplit[] = cardSiNoForm.getSchemeNo().split("_");
			schemeNo = Integer.parseInt(schemeSplit[1]);
		}else{
			
		}
		
		
		Map<Integer, String> courseList=CommonAjaxHandler.getInstance().getCourseByExamName(String.valueOf(Integer.parseInt(cardSiNoForm.getExamId())));
		Map<String, String> schemeList=CommonAjaxHandler.getInstance().getSchemeNoByExamIdCourseId(Integer.parseInt(cardSiNoForm.getExamId()), Integer.parseInt(cardSiNoForm.getCourseId()));
		Map<Integer, String> subjectMap = iCommonExamAjax.getSubjectsCodeNameByCourseSchemeExamId(
				"sCodeName",Integer.parseInt(cardSiNoForm.getCourseId()), 7, schemeNo,Integer.parseInt(cardSiNoForm.getExamId()));
		Map<Integer, String> teacherMap =transaction.getTeachers(Integer.parseInt(cardSiNoForm.getSubjectId()), 3);
		Map<Integer, String> examNameMap = CommonAjaxHandler.getInstance().getExamNameByExamTypeAndYear(cardSiNoForm.getExamType(),Integer.parseInt(cardSiNoForm.getYear()));
		cardSiNoForm.setExamNameList(examNameMap);
		cardSiNoForm.setCourseMap(courseList);
		cardSiNoForm.setSchemeMap(schemeList);
		cardSiNoForm.setSubjectCodeNameMap(subjectMap);
		cardSiNoForm.setTeachersMap(teacherMap);
		
		
	}
	public List<FalseBoxDetTo> getFlaseNumberDetils(FalseNumSiNoForm cardSiNoForm){
		FalseNumSiNoTransactionImpl transaction = new FalseNumSiNoTransactionImpl();
		 List<FalseNumberBoxDetails> falseDetList = transaction.getFlaseNumberDetils(cardSiNoForm);
		List <FalseBoxDetTo> toList=new ArrayList<FalseBoxDetTo>();
		 
		 for (FalseNumberBoxDetails bo : falseDetList) {
			 FalseBoxDetTo to=new FalseBoxDetTo();
			 to.setBoxDetId(bo.getId());
			 to.setBoxId(bo.getFalseNumBox().getId());
			 to.setBoxDetIsActive(bo.getIsActive());
			 to.setBoxIsActive(bo.getFalseNumBox().getIsActive());
			 to.setFalseNum(bo.getFalseNum());
			 toList.add(to);
			
		}
		
		return toList;
		
	}


	public boolean setcheckIsAvalible(FalseNumSiNoForm cardSiNoForm, FalseBoxDetTo to) throws Exception {
		ExamFalseNumberGen bo= transaction.getcheckfalseNoAvailable(cardSiNoForm,to);
		if (bo!=null) {
			return true;
		}
		return false;
		
	}
}
