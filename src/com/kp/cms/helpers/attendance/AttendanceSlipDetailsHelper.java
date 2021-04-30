package com.kp.cms.helpers.attendance;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.StringTokenizer;
import com.kp.cms.actions.attendance.AttendanceEntryAction;
import com.kp.cms.bo.admin.Attendance;
import com.kp.cms.bo.admin.AttendanceClass;
import com.kp.cms.bo.admin.AttendanceInstructor;
import com.kp.cms.bo.admin.AttendancePeriod;
import com.kp.cms.bo.admin.Period;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.attendance.AttendanceEntryForm;
import com.kp.cms.to.attendance.AttendanceTO;
import com.kp.cms.utilities.CommonUtil;
//AttendanceSlipDetailsHelper
public class AttendanceSlipDetailsHelper {
	private static AttendanceSlipDetailsHelper attendanceSlipDetailsHelper=new AttendanceSlipDetailsHelper();
	private static final Log log = LogFactory.getLog(AttendanceEntryAction.class);
	public static AttendanceSlipDetailsHelper getInstance() {
		// TODO Auto-generated method stub
		return attendanceSlipDetailsHelper;
	}
	public String getSlipDetailsSearchQuery(AttendanceEntryForm attendanceEntryForm) {
		// TODO Auto-generated method stub
		log.info("Start getSlipDetailsSearchQuery of AttendanceSlipDetailsHelper");
		
		 //String periods=getPeriods(attendanceEntryForm);
		
		String 	query="select ac.attendance from AttendanceClass ac" +
				" where ac.attendance.attendanceDate between '"+CommonUtil.ConvertStringToSQLDate(attendanceEntryForm.getFromDate())+ "' and '"+CommonUtil.ConvertStringToSQLDate(attendanceEntryForm.getToDate())+"'";
						
		StringBuilder intType =new StringBuilder();
		if (attendanceEntryForm.getClasses().length > 0) {
			String [] tempArray = attendanceEntryForm.getClasses();
			
			for(int i=0;i<tempArray.length;i++){
				intType.append(tempArray[i]);
				 if(i<(tempArray.length-1)){
					 intType.append(",");
				 }
			}
		}
		query = query+ " and ac.classSchemewise.id in ("+ intType+") and ac.attendance.isCanceled=0 group by ac.attendance.id order by ac.attendance.attendanceDate";
		log.info("End getSlipDetailsSearchQuery of AttendanceSlipDetailsHelper");
		return query;
	}
	
	public String getPeriods(AttendanceEntryForm attendanceEntryForm) {
		// TODO Auto-generated method stub
		log.info("Start getPeriods of AttendanceSlipDetailsHelper");
		String periods="select p.period.periodName from AttendanceClass ac join ac.attendance.attendancePeriods p "+
		"where ac.classSchemewise.id in (";
		StringBuilder intType =new StringBuilder();
		if (attendanceEntryForm.getClasses().length > 0) {
			String [] tempArray = attendanceEntryForm.getClasses();
			
			for(int i=0;i<tempArray.length;i++){
				intType.append(tempArray[i]);
				 if(i<(tempArray.length-1)){
					 intType.append(",");
				 }
			}
		}
		periods=periods+intType+") group by p.period.periodName order by p.period.periodName ";
		log.info("End getPeriods of AttendanceSlipDetailsHelper");
		return periods;
	}
	
	public static String getClassNames(AttendanceEntryForm attendanceEntryForm){
		log.info("Start getClassNames of AttendanceSlipDetailsHelper");
		String classNames="select c.classes.name from ClassSchemewise c where c.id in (";
		StringBuilder intType =new StringBuilder();
		if (attendanceEntryForm.getClasses().length > 0) {
			String [] tempArray = attendanceEntryForm.getClasses();
			
			for(int i=0;i<tempArray.length;i++){
				intType.append(tempArray[i]);
				 if(i<(tempArray.length-1)){
					 intType.append(",");
				 }
			}
		}
		classNames=classNames+intType+")";
		log.info("End getClassNames of AttendanceSlipDetailsHelper");
		return classNames;
	}
	
	public static String getClasses(List<String> classesList){
		StringBuilder className =new StringBuilder();
		if(classesList.size()>0){
			for(int i=0;i<classesList.size();i++){
				className.append(classesList.get(i));
				if(i<classesList.size()-1){
					className.append(",");
				}
			}
		}
		return className.toString();
	}
	
	public Map<String, Map<String,List<AttendanceTO>>> convertBotoTo(List<Attendance> attendaceList, List<String> periodList,AttendanceEntryForm attendanceEntryForm, HttpServletRequest request) {
		Map<String,String> subjectMap=new TreeMap<String, String>();
		Map<String, Map<String,List<AttendanceTO>>> attendanceMap = new TreeMap<String, Map<String,List<AttendanceTO>>>(AttendanceSlipDetailsHelper.sortDate);
		String[] classes=attendanceEntryForm.getClasses();
		List<String> classlist=Arrays.asList(classes);
		List<Integer> classlistval=new ArrayList<Integer>();
		Iterator<String> classlistIterator=classlist.iterator();
//		Map<String,String> dayMap=new HashMap<String, String>();
		while(classlistIterator.hasNext()){
			String classId=classlistIterator.next();
			if(classId!=null && !classId.isEmpty())
			classlistval.add(Integer.parseInt(classId));
		}
		Iterator<Attendance> attendanceListIterator=attendaceList.iterator();
		String pervPeriodName=null;
		while(attendanceListIterator.hasNext()){
			Attendance attendanceBO=attendanceListIterator.next();
			String attendanceDate=CommonUtil.ConvertStringToDateFormat(attendanceBO.getAttendanceDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy");
			if(!attendanceMap.containsKey(attendanceDate)){
				Map<String,List<AttendanceTO>> periodMap=new TreeMap<String,List<AttendanceTO>>();
				Iterator<String> periodListIterator=periodList.iterator();
				while(periodListIterator.hasNext()){
					periodMap.put(periodListIterator.next(),new ArrayList<AttendanceTO>());
				}
				Set<AttendancePeriod> attendancePeriods=attendanceBO.getAttendancePeriods();
				Iterator<AttendancePeriod> attendancePeriodsiIterator=attendancePeriods.iterator();
				while(attendancePeriodsiIterator.hasNext()){
					AttendancePeriod attendancePeriod=attendancePeriodsiIterator.next();
					if(classlistval.contains(attendancePeriod.getPeriod().getClassSchemewise().getId())){
					pervPeriodName=attendancePeriod.getPeriod().getPeriodName();
					if(periodList.contains(pervPeriodName)){
						List<AttendanceTO> periodMapList=periodMap.get(pervPeriodName);
						periodMapList.add(getAttendanceTO(attendanceBO,attendanceEntryForm,subjectMap));
						periodMap.put(pervPeriodName,periodMapList);
					}
					}
				}
				attendanceMap.put(attendanceDate,periodMap);
			}else{
				Map<String,List<AttendanceTO>> periodMap=attendanceMap.get(attendanceDate);
				Set<AttendancePeriod> attendancePeriods=attendanceBO.getAttendancePeriods();
				Iterator<AttendancePeriod> attendancePeriodsiIterator=attendancePeriods.iterator();
				while(attendancePeriodsiIterator.hasNext()){
						AttendancePeriod attendancePeriod=attendancePeriodsiIterator.next();
						if(classlistval.contains(attendancePeriod.getPeriod().getClassSchemewise().getId())){			
							String periodName=attendancePeriod.getPeriod().getPeriodName();
							if(periodList.contains(periodName)){
							List<AttendanceTO> periodMapList=periodMap.get(periodName);
							periodMapList.add(getAttendanceTO(attendanceBO,attendanceEntryForm,subjectMap));
							periodMap.put(periodName,periodMapList);
							}
						}
				}
				attendanceMap.put(attendanceDate, periodMap);
			}
		}
//		HttpSession session=request.getSession();
//		session.setAttribute("dayMap",dayMap);
		return attendanceMap;
	}
	
	public AttendanceTO getAttendanceTO(Attendance attendance,AttendanceEntryForm attendanceEntryForm,Map<String,String> subjectMap){
				AttendanceTO attendanceTO=new AttendanceTO();
				if(attendance != null){
					attendanceTO.setSubject(attendance.getSubject().getCode());
					attendanceTO.setSubjectName(attendance.getSubject().getName());
						if(!subjectMap.containsKey(attendance.getSubject().getCode())){
							subjectMap.put(attendance.getSubject().getCode(),attendance.getSubject().getName());
						}
						attendanceEntryForm.setSubMap(subjectMap);
					
					attendanceTO.setDates(CommonUtil.ConvertStringToDateFormat(attendance.getAttendanceDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
					Set<AttendanceInstructor> instructorSet=attendance.getAttendanceInstructors();
					if(instructorSet!=null && !instructorSet.isEmpty()){
						String instructorName="";
						Iterator<AttendanceInstructor> instItr=instructorSet.iterator();
						while (instItr.hasNext()) {
							AttendanceInstructor attendanceInstructor = (AttendanceInstructor) instItr.next();
							if(instructorName.isEmpty()){
								instructorName=attendanceInstructor.getUsers().getUserName();
							}else{
								instructorName=instructorName+","+attendanceInstructor.getUsers().getUserName();
							}
						}
						   attendanceTO.setTeachers(instructorName);
					}
				}
				return attendanceTO;
	}
	
	public static Comparator<String> sortDate = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			try {
				java.util.Date o11 =  new SimpleDateFormat("dd/MM/yyyy").parse(o1);
				java.util.Date o12=  new SimpleDateFormat("dd/MM/yyyy").parse(o2);
				return o12.compareTo(o11);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	};
	
public Map<String, Map<String,List<AttendanceTO>>> convertBotoTo1(List periodsDetailsbyClass,List<String> periodList,Map<String, Map<String,List<AttendanceTO>>> listOfSlipDetails,AttendanceEntryForm attendanceEntryForm) throws ParseException {
		
		Map<String,String> subjectMap=new TreeMap<String, String>();
		Map<String, Map<String,List<AttendanceTO>>> periodsDetailsbyClassMap = new TreeMap<String, Map<String,List<AttendanceTO>>>();
		
		
		Set<String> daysSet=new HashSet<String>();
		Iterator it=periodsDetailsbyClass.iterator();

		while(it.hasNext()){
			Object obj[]=(Object[])it.next();
			daysSet.add(obj[0].toString());
			
		}

			
			Iterator attendancePeriodsiIterator=periodsDetailsbyClass.iterator();
			while(attendancePeriodsiIterator.hasNext()){
				Object obj[]=(Object[])attendancePeriodsiIterator.next();
			
				
			Iterator<String> it1=daysSet.iterator();

			while(it1.hasNext()){
				String day=it1.next();
					
				
					
			if(obj[0].toString().equalsIgnoreCase((day))){
				String pervPeriodName=null;
			
				
			if(!periodsDetailsbyClassMap.containsKey(day)){
				
				Map<String,List<AttendanceTO>> periodMap=new TreeMap<String,List<AttendanceTO>>();
				Iterator<String> periodListIterator=periodList.iterator();
				while(periodListIterator.hasNext()){
					periodMap.put(periodListIterator.next(),new ArrayList<AttendanceTO>());
				}
				
				
					
					pervPeriodName=obj[1].toString();
					if(periodList.contains(pervPeriodName)){
						List<AttendanceTO> periodMapList=periodMap.get(pervPeriodName);
						periodMapList.add(getAttendanceTO1(obj,attendanceEntryForm,subjectMap));
						periodMap.put(pervPeriodName,periodMapList);
					}
					
				
				
				
					periodsDetailsbyClassMap.put(day,periodMap);
				
			}else{
				
				

				Map<String,List<AttendanceTO>> periodMap=periodsDetailsbyClassMap.get(day);
			
					
					String periodName=obj[1].toString();
						if(periodList.contains(periodName)){
							List<AttendanceTO> periodMapList=periodMap.get(periodName);
							periodMapList.add(getAttendanceTO1(obj,attendanceEntryForm,subjectMap));
							periodMap.put(periodName,periodMapList);
							}
					
				
						periodsDetailsbyClassMap.put(day, periodMap);
				
				
				}
			
			
			}
				
			}
				
			}
			
			
		
		
		Map<String, Map<String,List<AttendanceTO>>> periodsDetailsbyClassMap1 = new LinkedHashMap<String, Map<String,List<AttendanceTO>>>();
		
		
		
		Iterator<Map.Entry<String, Map<String,List<AttendanceTO>>>> oldMapiterator=listOfSlipDetails.entrySet().iterator();
		java.util.Calendar c=java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
		
		while(oldMapiterator.hasNext()){
			
			
			Map<String,List<AttendanceTO>> periodsDetailsMap1 = new TreeMap<String,List<AttendanceTO>>();
			Map.Entry<String, Map<String,List<AttendanceTO>>> oldMapEntrySet=oldMapiterator.next();
			
	        Date d = formatter.parse(oldMapEntrySet.getKey());
			c.setTime(d);
			
			int day = c.get(java.util.Calendar.DAY_OF_WEEK);
			String dayOfMonthStr = symbols.getWeekdays()[day];
			
			
			if(periodsDetailsbyClassMap.containsKey(dayOfMonthStr)){
				
				Map<String,List<AttendanceTO>> periodsDetailsbyClassMapValue=periodsDetailsbyClassMap.get(dayOfMonthStr);
				Map<String,List<AttendanceTO>> oldMapEntrySetValues=oldMapEntrySet.getValue();
				
				Iterator<Map.Entry<String,List<AttendanceTO>>> oldMapEntrySetValuesIterator=oldMapEntrySetValues.entrySet().iterator();
				while(oldMapEntrySetValuesIterator.hasNext()){
					
					Map.Entry<String,List<AttendanceTO>> oldMapEntrySetValuesMap=oldMapEntrySetValuesIterator.next();
					if(periodsDetailsbyClassMapValue.containsKey(oldMapEntrySetValuesMap.getKey())){
						
						List<AttendanceTO> list=periodsDetailsbyClassMapValue.get(oldMapEntrySetValuesMap.getKey());
						if(list.size()!=oldMapEntrySetValuesMap.getValue().size()){
							List<AttendanceTO> oldlist=oldMapEntrySetValuesMap.getValue();
							if(oldlist.size()!=0){
								List<AttendanceTO> newlist=new ArrayList<AttendanceTO>();
								Iterator<AttendanceTO> attIterator=list.iterator();
								
								while(attIterator.hasNext()){
									AttendanceTO to=attIterator.next();
									AttendanceTO attendanceTO=new AttendanceTO();
									attendanceTO.setSubject(to.getSubject());
									attendanceTO.setSubjectName(to.getSubjectName());
									attendanceTO.setTeachers(to.getTeachers());
									attendanceTO.setChecked(true);
									attendanceTO.setDay(to.getDay());
									attendanceTO.setPeriodName(to.getPeriodName());
									Iterator<AttendanceTO> oldIterator=oldlist.iterator();
									boolean isPresent=false;
									while(oldIterator.hasNext()){
										
										
										AttendanceTO to1=oldIterator.next();
										if(attendanceTO.getSubject().equalsIgnoreCase(to1.getSubject()) && attendanceTO.getTeachers().equalsIgnoreCase(to1.getTeachers())){
											
											isPresent=true;
											break;
											
											//System.out.println(to.getSubject().equalsIgnoreCase(to1.getSubject())+"size  match"+oldMapEntrySetValuesMap.getKey());
										}
									}
									
									if(isPresent)
										attendanceTO.setChecked(false);
									else
										attendanceTO.setChecked(true);
									newlist.add(attendanceTO);
									
								}
								
								periodsDetailsMap1.put(oldMapEntrySetValuesMap.getKey(),newlist );
							}else{
								//System.out.println("size not match"+oldMapEntrySetValuesMap.getKey());
								periodsDetailsMap1.put(oldMapEntrySetValuesMap.getKey(),list );
							}
							
						}else{
							periodsDetailsMap1.put(oldMapEntrySetValuesMap.getKey(), oldMapEntrySetValuesMap.getValue());
						}
					}else{
						
						//System.out.println("period not there"+oldMapEntrySetValuesMap.getKey());
						
					}
					
				}
				
				
				
			}else{
				//this one for sunday and sat check
				periodsDetailsbyClassMap1.put(oldMapEntrySet.getKey(), oldMapEntrySet.getValue());
			}
			
			//this one for sunday and sat check
			if(!periodsDetailsbyClassMap1.containsKey(oldMapEntrySet.getKey()))
			periodsDetailsbyClassMap1.put(oldMapEntrySet.getKey(), periodsDetailsMap1);
			
		}
		
		
		periodsDetailsbyClassMap=null;
		
		return periodsDetailsbyClassMap1;
			
		}

public AttendanceTO getAttendanceTO1(Object[] obj,AttendanceEntryForm attendanceEntryForm,Map<String,String> subjectMap){
	AttendanceTO attendanceTO=new AttendanceTO();
	
		attendanceTO.setSubject(obj[2].toString());
		attendanceTO.setSubjectName(obj[3].toString());
			if(!subjectMap.containsKey(obj[2].toString())){
				subjectMap.put(obj[2].toString(),obj[3].toString());
			}
			attendanceEntryForm.setSubMap(subjectMap);
		
		attendanceTO.setTeachers(obj[4].toString());
		attendanceTO.setChecked(true);
		attendanceTO.setDay(obj[0].toString());
		attendanceTO.setPeriodName(obj[1].toString());
		
	return attendanceTO;
 }
	
}
