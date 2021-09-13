package com.kp.cms.helpers.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.forms.admin.PublishStudentSemesterFeesForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.PublishStudentSemesterFeesTO;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.utilities.CurrentAcademicYear;

public class PublishStudentSemesterFeesHelper {
	private static final Log log = LogFactory.getLog(PublishStudentSemesterFeesHelper.class);
	private static PublishStudentSemesterFeesHelper publishStudentSemesterFeesHelper= null;
	public static PublishStudentSemesterFeesHelper getInstance() {
	      if(publishStudentSemesterFeesHelper == null) {
	    	  publishStudentSemesterFeesHelper = new PublishStudentSemesterFeesHelper();
	    	  return publishStudentSemesterFeesHelper;
	      }
	      return publishStudentSemesterFeesHelper;
	}
	public List<PublishStudentSemesterFeesTO> convertBOToTO(List<PublishStudentSemesterFees> studentSemesterPublishList) throws Exception {
			List<PublishStudentSemesterFeesTO> attList = new ArrayList<PublishStudentSemesterFeesTO>();
		
		if (studentSemesterPublishList != null) {

			Iterator<PublishStudentSemesterFees> iterator = studentSemesterPublishList.iterator();
			ClassesTO classesTo;
			while (iterator.hasNext()) {
				PublishStudentSemesterFees pocsbo = (PublishStudentSemesterFees) iterator.next();
				PublishStudentSemesterFeesTO pocsTO = new PublishStudentSemesterFeesTO();
				
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
	public void setPublishOptionalCourseSubjectApplication(PublishStudentSemesterFeesForm publishStudentSemesterFeesForm,
			PublishStudentSemesterFees publishStudentSemesterFees)  throws Exception{

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = df.format(publishStudentSemesterFees.getFromDate());
		String toDate = df.format(publishStudentSemesterFees.getToDate());
		String[] classArray = new String[10];
		if (publishStudentSemesterFeesForm != null) {
			publishStudentSemesterFeesForm.setFromDate(fromDate);
			publishStudentSemesterFeesForm.setToDate(toDate);
			publishStudentSemesterFeesForm.setYear(publishStudentSemesterFees.getAcademicYear());
			//new
			publishStudentSemesterFeesForm.setAcademicYear(publishStudentSemesterFeesForm.getAcademicYear());
			
			publishStudentSemesterFeesForm.setClassId(String.valueOf(publishStudentSemesterFees.getClasses().getId()));
			Map<Integer,String> classMap = new HashMap<Integer, String>();

				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int year=CurrentAcademicYear.getInstance().getAcademicyear();
				if(year!=0){
					currentYear=year;
				}
				if(publishStudentSemesterFeesForm.getYear()!=null && !publishStudentSemesterFeesForm.getYear().isEmpty()){
					currentYear=Integer.parseInt(publishStudentSemesterFeesForm.getYear());
				}
				classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
				publishStudentSemesterFeesForm.setClassMap(classMap);
				
				Map<Integer, String> mapClas = publishStudentSemesterFeesForm.getClassMap();
				Set<Integer> set = new HashSet<Integer>();
				set.add(publishStudentSemesterFees.getClasses().getId());
				mapClas.keySet().retainAll(set);
				publishStudentSemesterFeesForm.setClassMap(mapClas);
				Map<Integer, String> mapSelectedClass = new HashMap<Integer, String>();
				
				mapSelectedClass.putAll(mapClas);
				publishStudentSemesterFeesForm.setMapSelectedClass(mapSelectedClass);
			classArray[0]=String.valueOf(publishStudentSemesterFees.getClasses().getId());
			publishStudentSemesterFeesForm.setClassCodeIdsFrom(classArray);
			String[] classids = { Integer.toString(publishStudentSemesterFees.getClasses().getId()) };
			publishStudentSemesterFeesForm.setClassCodeIdsTo(classids);
			publishStudentSemesterFeesForm.setYear(publishStudentSemesterFees.getAcademicYear());
		}
		log.error("ending of setDepartmentDetails method in DepartmentEntryHelper");
	
		
	}
	
	
	
}
