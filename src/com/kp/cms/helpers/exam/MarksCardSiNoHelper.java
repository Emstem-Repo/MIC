package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Iterator;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.forms.exam.MarksCardSiNoForm;
import com.kp.cms.to.exam.MarksCardSiNoTO;

public class MarksCardSiNoHelper {
	private static MarksCardSiNoHelper marksCardSiNoHelper = null;
	public static MarksCardSiNoHelper getInstance(){
		if(marksCardSiNoHelper==null){
			marksCardSiNoHelper = new MarksCardSiNoHelper();
		}
		return marksCardSiNoHelper;
	}
	public MarksCardSiNo convertFormToBo(MarksCardSiNoForm cardSiNoForm)throws Exception {
		// TODO Auto-generated method stub
		MarksCardSiNo bo = new MarksCardSiNo();
		bo.setStartNo(cardSiNoForm.getStartNo());
		bo.setIsActive(true);
		bo.setCurrentNo(cardSiNoForm.getStartNo());
		bo.setCreatedDate(new Date());
		bo.setCreatedBy(cardSiNoForm.getUserId());
		bo.setAcademicYear(Integer.parseInt(cardSiNoForm.getAcademicYear()));
		bo.setPrefix(cardSiNoForm.getPrefix());
		bo.setSemister(Integer.parseInt(cardSiNoForm.getSemister()));
		Course course=new Course();
		course.setId(Integer.parseInt(cardSiNoForm.getCourseId()));
		bo.setCourseId(course);
		return bo;
	}
	
	
	public List<MarksCardSiNoTO> convertBotoTo(List<MarksCardSiNo> list)throws Exception {
		// TODO Auto-generated method stub
		List<MarksCardSiNoTO> toList = new ArrayList<MarksCardSiNoTO>();
		
		try{
			if(list.size()!=0){
				Iterator<MarksCardSiNo> i=list.iterator();
				while(i.hasNext()){
				MarksCardSiNoTO to = new MarksCardSiNoTO();	
				MarksCardSiNo bo=i.next();	
				to = new MarksCardSiNoTO();
				to.setId(bo.getId());
				to.setStartNo(bo.getStartNo());
				to.setCurrentNo(bo.getCurrentNo());
				if(bo.getCreatedDate()!=null){
					to.setCreatedDate(bo.getCreatedDate());
				}
				to.setAcademicYear(bo.getAcademicYear());
				to.setCourseId(bo.getCourseId().getId());
				to.setCourseName(bo.getCourseId().getName());
				to.setSemister(bo.getSemister());
				to.setPrefix(bo.getPrefix());
				
				toList.add(to);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return toList;
	}
}
