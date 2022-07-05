package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.record.formula.functions.Count;

import java.util.Iterator;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.FalseNumSiNo;
import com.kp.cms.bo.exam.FalseNumberBox;
import com.kp.cms.bo.exam.FalseNumberBoxDetails;
import com.kp.cms.forms.exam.FalseNumSiNoForm;
import com.kp.cms.to.exam.FalseBoxDetTo;
import com.kp.cms.to.exam.FalseNumSiNoTO;
import com.kp.cms.transactions.exam.IFalseNumSiNoTransaction;
import com.kp.cms.transactionsimpl.exam.FalseNumSiNoTransactionImpl;

public class FalseNumSiNoHelper {
	private static FalseNumSiNoHelper marksCardSiNoHelper = null;
	public static FalseNumSiNoHelper getInstance(){
		if(marksCardSiNoHelper==null){
			marksCardSiNoHelper = new FalseNumSiNoHelper();
		}
		return marksCardSiNoHelper;
	}
	public FalseNumSiNo convertFormToBo(FalseNumSiNoForm cardSiNoForm)throws Exception {
		
		// TODO Auto-generated method stub
		FalseNumSiNo bo = new FalseNumSiNo();
		if(cardSiNoForm.getStartNo().equalsIgnoreCase(""))
			bo.setStartNo("0");
		else
			bo.setStartNo(cardSiNoForm.getStartNo());
		bo.setIsActive(true);
		if(cardSiNoForm.getStartNo().equalsIgnoreCase(""))
			bo.setCurrentNo("0");
		else
			bo.setCurrentNo(cardSiNoForm.getStartNo());
		bo.setCreatedDate(new Date());
		bo.setCreatedBy(cardSiNoForm.getUserId());
		bo.setAcademicYear(Integer.parseInt(cardSiNoForm.getYear()));
		bo.setPrefix(cardSiNoForm.getPrefix());
		bo.setSemister(Integer.parseInt(cardSiNoForm.getSemister()));
		Course course=new Course();
		course.setId(Integer.parseInt(cardSiNoForm.getCourseId()));
		ExamDefinition examDefinition=new ExamDefinition();
		examDefinition.setId(Integer.parseInt(cardSiNoForm.getExamId()));
		bo.setCourseId(course);
		bo.setExamId(examDefinition);
		return bo;
	}
	
	
	public List<FalseNumSiNoTO> convertBotoTo(List<FalseNumSiNo> list)throws Exception {
		// TODO Auto-generated method stub
		List<FalseNumSiNoTO> toList = new ArrayList<FalseNumSiNoTO>();
		
		try{
			if(list!=null && list.size()!=0){
				Iterator<FalseNumSiNo> i=list.iterator();
				while(i.hasNext()){
				FalseNumSiNoTO to = new FalseNumSiNoTO();	
				FalseNumSiNo bo=i.next();	
				to = new FalseNumSiNoTO();
				to.setId(bo.getId());
				to.setStartNo(bo.getStartNo());
				to.setCurrentNo(bo.getCurrentNo());
				if(bo.getCreatedDate()!=null){
					to.setCreatedDate(bo.getCreatedDate());
				}
				to.setAcademicYear(bo.getAcademicYear());
				to.setCourseId(String.valueOf(bo.getCourseId().getId()));
				to.setCourseName(bo.getCourseId().getName());
				to.setSemister(String.valueOf(bo.getSemister()));
				to.setPrefix(bo.getPrefix());
				to.setExamName(bo.getExamId().getName());
				to.setExamId(String.valueOf(bo.getExamId().getId()));
				toList.add(to);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return toList;
	}
	public FalseNumSiNo updateFalseNo(FalseNumSiNoForm cardSiNoForm)throws Exception {
		IFalseNumSiNoTransaction transaction = new FalseNumSiNoTransactionImpl();
		FalseNumSiNo bo = transaction.getFalseNoBoObject(cardSiNoForm);
		if(cardSiNoForm.getStartNo().equalsIgnoreCase(""))
			bo.setStartNo("0");
		else
			bo.setStartNo(cardSiNoForm.getStartNo());
		bo.setAcademicYear(Integer.parseInt(cardSiNoForm.getYear()));
		bo.setPrefix(cardSiNoForm.getPrefix());
		String sem= cardSiNoForm.getSemister();
		String[] split=sem.split("_");
		bo.setSemister(Integer.parseInt(split[1]));
		Course course=new Course();
		course.setId(Integer.parseInt(cardSiNoForm.getCourseId()));
		ExamDefinition examDefinition=new ExamDefinition();
		examDefinition.setId(Integer.parseInt(cardSiNoForm.getExamId()));
		bo.setCourseId(course);
		bo.setExamId(examDefinition);
		if(cardSiNoForm.getCurrentFalseNo() != null && !cardSiNoForm.getCurrentFalseNo().isEmpty()){
		bo.setCurrentNo(cardSiNoForm.getCurrentFalseNo());
		}
		return bo;
	}
	public List<FalseNumberBoxDetails> convertToFalseNumberBoxDetails(FalseNumSiNoForm cardSiNoForm) {
		List<FalseBoxDetTo> barcodeList=cardSiNoForm.getBarcodeList();
		FalseNumberBox falsebox=new FalseNumberBox();
		List<FalseNumberBoxDetails> boxDetailsList=new ArrayList();
		if (cardSiNoForm.getFalseBoxId()!=0) {
			falsebox.setId(cardSiNoForm.getFalseBoxId());
		}
		falsebox.setAcademicYear(Integer.parseInt(cardSiNoForm.getYear()));
		falsebox.setBoxNum(cardSiNoForm.getBoxNo());
		String semString[]=cardSiNoForm.getSchemeNo().split("_");
		falsebox.setSchemeNum(Integer.parseInt(semString[1]));
		Course couse=new Course();
		ExamDefinition edef=new ExamDefinition();
		edef.setId(Integer.parseInt(cardSiNoForm.getExamId()));
		Subject sub=new Subject();
		sub.setId(Integer.parseInt(cardSiNoForm.getSubjectId()));
		couse.setId(Integer.parseInt(cardSiNoForm.getCourseId()));
		
		falsebox.setCourseId(couse);
		falsebox.setSubjectId(sub);
		falsebox.setExamId(edef);
		if (cardSiNoForm.getTeachers()!=null  && !cardSiNoForm.getTeachers().isEmpty()) {
			Users uid=new Users();
			uid.setId(Integer.parseInt(cardSiNoForm.getTeachers()));
			falsebox.setExaminerId(uid);
		}
		falsebox.setIsActive(true);
		if (cardSiNoForm.getChiefExaminer()!=null && !cardSiNoForm.getChiefExaminer().isEmpty()) {
			Users cid=new Users();
			cid.setId(Integer.parseInt(cardSiNoForm.getChiefExaminer()));
			falsebox.setChiefExaminer(cid);
		}

		if (cardSiNoForm.getAdditionalExaminer()!=null && !cardSiNoForm.getAdditionalExaminer().isEmpty()) {
			Users aid=new Users();
			aid.setId(Integer.parseInt(cardSiNoForm.getAdditionalExaminer()));
			falsebox.setAdditionalExaminer(aid);
		}
		if (cardSiNoForm.getCorrectionValidator()!=null &&! cardSiNoForm.getCorrectionValidator().isEmpty()) {
			Users corid=new Users();
			corid.setId(Integer.parseInt(cardSiNoForm.getCorrectionValidator()));
			falsebox.setCorrectionValidator(corid);
		}
		for (FalseBoxDetTo bcode : barcodeList) {
			FalseNumberBoxDetails boxDetails=new FalseNumberBoxDetails();
			if (bcode.getBoxDetId()!=0) {
				boxDetails.setId(bcode.getBoxDetId());
			}
			boxDetails.setFalseNumBox(falsebox);
			boxDetails.setFalseNum(bcode.getFalseNum());
			boxDetails.setIsActive(bcode.isBoxDetIsActive());
			boxDetailsList.add(boxDetails);
		}
		
		return boxDetailsList;
	}
	public List<FalseBoxDetTo> setFalseNumberBoxBoTo(List<FalseNumberBox> boxList) {
		List<FalseBoxDetTo> toList=new ArrayList();
		IFalseNumSiNoTransaction transaction = new FalseNumSiNoTransactionImpl();
		if (boxList!=null) {
		for (FalseNumberBox bo : boxList) {
			FalseBoxDetTo to=new FalseBoxDetTo();
			to.setBoxId(bo.getId());
			to.setAcademicYear(bo.getAcademicYear());
			to.setBoxNum(bo.getBoxNum());
			to.setCourseId(bo.getCourseId().getId());
			to.setCourseName(bo.getCourseId().getName());
			to.setSchemeNum(bo.getSchemeNum());
			to.setSubjectId(bo.getSubjectId().getId());
			to.setSubjectName(bo.getSubjectId().getName());
			to.setExamId(bo.getExamId().getId());
			to.setExamName(bo.getExamId().getName());
			to.setCount(transaction.getCount(bo.getId()));
			if (bo.getExaminerId()!=null) {
				to.setExaminerId(bo.getExaminerId().getId());
				if (bo.getExaminerId().getEmployee()!=null) 
					to.setExaminerName(bo.getExaminerId().getEmployee().getFirstName());
				else if (bo.getExaminerId().getGuest()!=null) 
					to.setExaminerName(bo.getExaminerId().getGuest().getFirstName());
			}
			to.setExamType(String.valueOf(bo.getExamId().getExamType().getId()));
			
			if (bo.getAdditionalExaminer()!=null) {
				to.setAdditionalExaminerId(bo.getAdditionalExaminer().getId());
				if (bo.getAdditionalExaminer().getEmployee()!=null) {
					to.setAdditionalExaminer(bo.getAdditionalExaminer().getEmployee().getFirstName());
				}else if (bo.getAdditionalExaminer().getGuest()!=null) {
					to.setAdditionalExaminer(bo.getAdditionalExaminer().getGuest().getFirstName());
				}
			}
			
			if (bo.getChiefExaminer()!=null) {
				to.setChiefExaminerId(bo.getChiefExaminer().getId());
				if (bo.getChiefExaminer().getEmployee()!=null) {
					to.setChiefExaminer(bo.getChiefExaminer().getEmployee().getFirstName());
				}else if (bo.getChiefExaminer().getGuest()!=null) {
					to.setChiefExaminer(bo.getChiefExaminer().getGuest().getFirstName());
				}
			}
			
			if (bo.getCorrectionValidator()!=null) {
				to.setCorrectionValidatorId(bo.getCorrectionValidator().getId());
				if (bo.getCorrectionValidator().getEmployee()!=null) {
					to.setCorrectionValidatorName(bo.getCorrectionValidator().getEmployee().getFirstName());
				}else if (bo.getCorrectionValidator().getGuest()!=null) {
					to.setCorrectionValidatorName(bo.getCorrectionValidator().getGuest().getFirstName());
				}
			}
			toList.add(to);
			
		}
		return toList;
		}
		return null;
	}
}
