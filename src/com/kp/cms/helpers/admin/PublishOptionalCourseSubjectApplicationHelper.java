package com.kp.cms.helpers.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.PublishOptionalCourseSubjectApplication;
import com.kp.cms.forms.admin.PublishOptionalCourseSubjectApplicationForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.PublishOptionalCourseSubjectApplicationTO;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;

public class PublishOptionalCourseSubjectApplicationHelper
{
	private static final Log log = LogFactory.getLog(PublishOptionalCourseSubjectApplicationHelper.class);
	private static PublishOptionalCourseSubjectApplicationHelper publishOptionalCourseSubjectApplicationHelper= null;
	public static PublishOptionalCourseSubjectApplicationHelper getInstance() {
		      if(publishOptionalCourseSubjectApplicationHelper == null) {
		    	  publishOptionalCourseSubjectApplicationHelper = new PublishOptionalCourseSubjectApplicationHelper();
		    	  return publishOptionalCourseSubjectApplicationHelper;
		      }
	return publishOptionalCourseSubjectApplicationHelper;
	}
	
	public static List<PublishOptionalCourseSubjectApplicationTO> convertBOsToTos(List<PublishOptionalCourseSubjectApplication> publishOptionalCourseSubjectApplicationList) {
		List<PublishOptionalCourseSubjectApplicationTO> attList = new ArrayList<PublishOptionalCourseSubjectApplicationTO>();
		
		if (publishOptionalCourseSubjectApplicationList != null) {

			Iterator<PublishOptionalCourseSubjectApplication> iterator = publishOptionalCourseSubjectApplicationList.iterator();
			ClassesTO classesTo;
			while (iterator.hasNext()) {
				PublishOptionalCourseSubjectApplication pocsbo = (PublishOptionalCourseSubjectApplication) iterator.next();
				PublishOptionalCourseSubjectApplicationTO pocsTO = new PublishOptionalCourseSubjectApplicationTO();
				
				pocsTO.setId(pocsbo.getId());
				pocsTO.setAcadamicYear(pocsbo.getAcademicYear());
				pocsTO.setClassID(pocsbo.getClasses().getId()+"");
				pocsTO.setClassName(pocsbo.getClasses().getName());
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String fromtDate = df.format(pocsbo.getFromDate());
				String toDate = df.format(pocsbo.getToDate());;
				pocsTO.setFromDate(fromtDate);
				pocsTO.setToDate(toDate);
				attList.add(pocsTO);
			}
		}
		return attList;
	}
	
	
	
	public static PublishOptionalCourseSubjectApplication convertTOtoBO(PublishOptionalCourseSubjectApplicationForm publishOptionalCourseSubjectApplicationForm) throws Exception{
		PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication=new PublishOptionalCourseSubjectApplication();
		Classes classes =  new Classes();
		if(publishOptionalCourseSubjectApplicationForm.getClassId()!=null)
		classes.setId(Integer.parseInt(publishOptionalCourseSubjectApplicationForm.getClassId()));
		publishOptionalCourseSubjectApplication.setId(Integer.parseInt(publishOptionalCourseSubjectApplicationForm.getId()));
		publishOptionalCourseSubjectApplication.setCreatedBy(publishOptionalCourseSubjectApplicationForm.getUserId());
		publishOptionalCourseSubjectApplication.setCreatedDate(new Date());
		publishOptionalCourseSubjectApplication.setAcademicYear(publishOptionalCourseSubjectApplicationForm.getAcademicYear());
		publishOptionalCourseSubjectApplication.setFromDate(CommonUtil.ConvertStringToDate(publishOptionalCourseSubjectApplicationForm.getFromDate()));
		publishOptionalCourseSubjectApplication.setToDate(CommonUtil.ConvertStringToDate(publishOptionalCourseSubjectApplicationForm.getToDate()));
		publishOptionalCourseSubjectApplication.setIsActive(true);
		publishOptionalCourseSubjectApplication.setClasses(classes);


		return publishOptionalCourseSubjectApplication;
	}
	
	

	public void setPublishOptionalCourseSubjectApplication(PublishOptionalCourseSubjectApplicationForm publishOptionalCourseSubjectApplicationForm,
			PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = df.format(publishOptionalCourseSubjectApplication.getFromDate());
		String toDate = df.format(publishOptionalCourseSubjectApplication.getToDate());
		String[] classArray = new String[10];
		if (publishOptionalCourseSubjectApplicationForm != null) {
			publishOptionalCourseSubjectApplicationForm.setFromDate(fromDate);
			publishOptionalCourseSubjectApplicationForm.setToDate(toDate);
			publishOptionalCourseSubjectApplicationForm.setYear(publishOptionalCourseSubjectApplication.getAcademicYear());
			//new
			publishOptionalCourseSubjectApplicationForm.setAcademicYear(publishOptionalCourseSubjectApplication.getAcademicYear());
			
			publishOptionalCourseSubjectApplicationForm.setClassId(String.valueOf(publishOptionalCourseSubjectApplication.getClasses().getId()));
			Map<Integer,String> classMap = new HashMap<Integer, String>();

				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int year=CurrentAcademicYear.getInstance().getAcademicyear();
				if(year!=0){
					currentYear=year;
				}
				if(publishOptionalCourseSubjectApplicationForm.getYear()!=null && !publishOptionalCourseSubjectApplicationForm.getYear().isEmpty()){
					currentYear=Integer.parseInt(publishOptionalCourseSubjectApplicationForm.getYear());
				}
				classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
				publishOptionalCourseSubjectApplicationForm.setClassMap(classMap);
				
				Map<Integer, String> mapClas = publishOptionalCourseSubjectApplicationForm.getClassMap();
				Set<Integer> set = new HashSet<Integer>();
				set.add(publishOptionalCourseSubjectApplication.getClasses().getId());
				mapClas.keySet().retainAll(set);
				publishOptionalCourseSubjectApplicationForm.setClassMap(mapClas);
				Map<Integer, String> mapSelectedClass = new HashMap<Integer, String>();
				
				mapSelectedClass.putAll(mapClas);
				publishOptionalCourseSubjectApplicationForm.setMapSelectedClass(mapSelectedClass);
			classArray[0]=String.valueOf(publishOptionalCourseSubjectApplication.getClasses().getId());
			publishOptionalCourseSubjectApplicationForm.setClassCodeIdsFrom(classArray);
			String[] classids = { Integer.toString(publishOptionalCourseSubjectApplication.getClasses().getId()) };
			publishOptionalCourseSubjectApplicationForm.setClassCodeIdsTo(classids);
			publishOptionalCourseSubjectApplicationForm.setYear(publishOptionalCourseSubjectApplication.getAcademicYear());
		}
		log.error("ending of setDepartmentDetails method in DepartmentEntryHelper");
	}


}
