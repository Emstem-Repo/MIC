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
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.forms.admin.PublishSpecialFeesForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.to.admin.PublishStudentSemesterFeesTO;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.utilities.CurrentAcademicYear;

public class PublishSpecialFeesHelper {
	private static final Log log = LogFactory.getLog(PublishSpecialFeesHelper.class);
	private static PublishSpecialFeesHelper publishSpecialFeesHelper= null;
	public static PublishSpecialFeesHelper getInstance() {
	      if(publishSpecialFeesHelper == null) {
	    	  publishSpecialFeesHelper = new PublishSpecialFeesHelper();
	    	  return publishSpecialFeesHelper;
	      }
	      return publishSpecialFeesHelper;
	}
	public List<PublishSpecialFeesTO> convertBOToTO(List<PublishSpecialFees> publishSpecialFees) throws Exception {
List<PublishSpecialFeesTO> attList = new ArrayList<PublishSpecialFeesTO>();
		
		if (publishSpecialFees != null) {

			Iterator<PublishSpecialFees> iterator = publishSpecialFees.iterator();
			ClassesTO classesTo;
			while (iterator.hasNext()) {
				PublishSpecialFees pocsbo = (PublishSpecialFees) iterator.next();
				PublishSpecialFeesTO pocsTO = new PublishSpecialFeesTO();
				
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
	public void setPublishSpecialFees(PublishSpecialFeesForm publishSpecialFeesForm,PublishSpecialFees publishSpecialFees)  throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = df.format(publishSpecialFees.getFromDate());
		String toDate = df.format(publishSpecialFees.getToDate());
		String[] classArray = new String[10];
		if (publishSpecialFeesForm != null) {
			publishSpecialFeesForm.setFromDate(fromDate);
			publishSpecialFeesForm.setToDate(toDate);
			publishSpecialFeesForm.setYear(publishSpecialFees.getAcademicYear());
			//new
			publishSpecialFeesForm.setAcademicYear(publishSpecialFeesForm.getAcademicYear());
			
			publishSpecialFeesForm.setClassId(String.valueOf(publishSpecialFees.getClasses().getId()));
			Map<Integer,String> classMap = new HashMap<Integer, String>();

				Calendar calendar = Calendar.getInstance();
				int currentYear = calendar.get(Calendar.YEAR);
				int year=CurrentAcademicYear.getInstance().getAcademicyear();
				if(year!=0){
					currentYear=year;
				}
				if(publishSpecialFeesForm.getYear()!=null && !publishSpecialFeesForm.getYear().isEmpty()){
					currentYear=Integer.parseInt(publishSpecialFeesForm.getYear());
				}
				classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
				publishSpecialFeesForm.setClassMap(classMap);
				
				Map<Integer, String> mapClas = publishSpecialFeesForm.getClassMap();
				Set<Integer> set = new HashSet<Integer>();
				set.add(publishSpecialFees.getClasses().getId());
				mapClas.keySet().retainAll(set);
				publishSpecialFeesForm.setClassMap(mapClas);
				Map<Integer, String> mapSelectedClass = new HashMap<Integer, String>();
				
				mapSelectedClass.putAll(mapClas);
				publishSpecialFeesForm.setMapSelectedClass(mapSelectedClass);
			classArray[0]=String.valueOf(publishSpecialFees.getClasses().getId());
			publishSpecialFeesForm.setClassCodeIdsFrom(classArray);
			String[] classids = { Integer.toString(publishSpecialFees.getClasses().getId()) };
			publishSpecialFeesForm.setClassCodeIdsTo(classids);
			publishSpecialFeesForm.setYear(publishSpecialFees.getAcademicYear());
		}
		log.error("ending of setDepartmentDetails method in DepartmentEntryHelper");
	
		
	}
}
