package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<String> barcodeList=cardSiNoForm.getBarcodeList();
		FalseNumberBox falsebox=new FalseNumberBox();
		List<FalseNumberBoxDetails> boxDetailsList=new ArrayList();
		
		falsebox.setAcademicYear(Integer.parseInt(cardSiNoForm.getYear()));
		falsebox.setBoxNum(Integer.parseInt(cardSiNoForm.getBoxNo()));
		String semString[]=cardSiNoForm.getSchemeNo().split("_");
		falsebox.setSchemeNum(Integer.parseInt(semString[1]));
		Course couse=new Course();
		ExamDefinition edef=new ExamDefinition();
		edef.setId(Integer.parseInt(cardSiNoForm.getExamId()));
		Subject sub=new Subject();
		sub.setId(Integer.parseInt(cardSiNoForm.getSubjectId()));
		couse.setId(Integer.parseInt(cardSiNoForm.getCourseId()));
		Users uid=new Users();
		uid.setId(Integer.parseInt(cardSiNoForm.getTeachers()));
		falsebox.setCourseId(couse);
		falsebox.setSubjectId(sub);
		falsebox.setExamId(edef);
		falsebox.setExaminerId(uid);
		if (cardSiNoForm.getChiefExaminer()!=null && !cardSiNoForm.getChiefExaminer().isEmpty()) {
			falsebox.setChiefExaminer(cardSiNoForm.getChiefExaminer());
		}

		if (cardSiNoForm.getAdditionalExaminer()!=null && !cardSiNoForm.getAdditionalExaminer().isEmpty()) {
			falsebox.setAdditionalExaminer(cardSiNoForm.getAdditionalExaminer());
		}
		for (String bcode : barcodeList) {
			FalseNumberBoxDetails boxDetails=new FalseNumberBoxDetails();
			boxDetails.setFalseNumBox(falsebox);
			boxDetails.setFalseNum(bcode);
			boxDetailsList.add(boxDetails);
		}
		
		return boxDetailsList;
	}
	public List<FalseBoxDetTo> setFalseNumberBoxBoTo(List<FalseNumberBox> boxList) {
		List<FalseBoxDetTo> toList=new ArrayList();
		if (boxList!=null) {
		for (FalseNumberBox bo : boxList) {
			FalseBoxDetTo to=new FalseBoxDetTo();
			to.setAcademicYear(bo.getAcademicYear());
			to.setBoxNum(bo.getBoxNum());
			to.setCourseId(bo.getCourseId().getId());
			to.setCourseName(bo.getCourseId().getName());
			to.setSchemeNum(bo.getSchemeNum());
			to.setSubjectId(bo.getSubjectId().getId());
			to.setSubjectName(bo.getSubjectId().getName());
			to.setExamId(bo.getExaminerId().getId());
			to.setExaminerName(bo.getExaminerId().getEmployee().getFirstName());
			toList.add(to);
			
		}
		return toList;
		}
		return null;
	}
}
