package com.kp.cms.helpers.admission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admin.ConvocationCourse;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.forms.admission.PublishForAllotmentForm;
import com.kp.cms.to.admission.PublishForAllotmentTO;
import com.kp.cms.utilities.CommonUtil;

public class PublishForAllotmentHelper {
	
	private static volatile PublishForAllotmentHelper helper =null;
	
	public static PublishForAllotmentHelper getInstance(){

		if(helper==null){
			helper = new PublishForAllotmentHelper();
			return helper;
		}
		return helper;
	}

	public List<PublishForAllotment> setBoList(PublishForAllotmentForm publishForAllotmentForm) throws Exception{
		List<PublishForAllotment> forAllotments = new ArrayList<PublishForAllotment>();
		for (int i = 0; i < publishForAllotmentForm.getCourseIds().length; i++) {
			if(publishForAllotmentForm.getCourseIds()[i] != null && !publishForAllotmentForm.getCourseIds()[i].trim().isEmpty()){
				Course course = new Course();
				course.setId(Integer.parseInt(publishForAllotmentForm.getCourseIds()[i]));
				PublishForAllotment allotment = new PublishForAllotment();
				allotment.setCourse(course);
				allotment.setFromDate(CommonUtil.ConvertStringToSQLDate(publishForAllotmentForm.getFromDate()));
				allotment.setEndDate(CommonUtil.ConvertStringToSQLDate(publishForAllotmentForm.getEndDate()));
				allotment.setCreatedBy(publishForAllotmentForm.getUserId());
				allotment.setModifiedBy(publishForAllotmentForm.getUserId());
				allotment.setCreatedDate(new Date());
				allotment.setLastModifiedDate(new Date());
				allotment.setIsActive(true);
				allotment.setAppliedYear(Integer.parseInt(publishForAllotmentForm.getYear()));
				allotment.setAllotmentNo(Integer.parseInt(publishForAllotmentForm.getAllotmentNo()));
				allotment.setChanceNo(Integer.parseInt(publishForAllotmentForm.getChanceNo()));
				forAllotments.add(allotment);
			}
		}
		return forAllotments;
	}

	public List<PublishForAllotmentTO> getTolist(List<PublishForAllotment> allotments) {
		List<PublishForAllotmentTO> allotmentTOs = new ArrayList<PublishForAllotmentTO>();
		try{
			for(PublishForAllotment allotment:allotments){
				PublishForAllotmentTO allotmentTO = new PublishForAllotmentTO();
				allotmentTO.setAppliedYear(String.valueOf(allotment.getAppliedYear()));
				allotmentTO.setCourseName(allotment.getCourse().getName());
				allotmentTO.setFromDate(CommonUtil.formatSqlDate(allotment.getFromDate().toString()));
				allotmentTO.setToDate(CommonUtil.formatSqlDate(allotment.getEndDate().toString()));
				allotmentTO.setId(allotment.getId());
				allotmentTO.setCourseId(String.valueOf(allotment.getCourse().getId()));
				allotmentTO.setAllotmentNo(String.valueOf(allotment.getAllotmentNo()));
				if(String.valueOf(allotment.getChanceNo())!=null){
					allotmentTO.setChanceNo(String.valueOf(allotment.getChanceNo()));
				}
				allotmentTOs.add(allotmentTO);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return allotmentTOs;
	}

}
