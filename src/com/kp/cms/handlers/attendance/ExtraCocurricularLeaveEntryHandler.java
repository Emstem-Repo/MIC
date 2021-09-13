package com.kp.cms.handlers.attendance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.actions.admin.StudentUploadPhotoAction;
import com.kp.cms.bo.admin.Activity;
import com.kp.cms.bo.admin.AttendanceType;
import com.kp.cms.bo.admin.Period;
import com.kp.cms.bo.admin.StuCocurrLeave;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.attendance.ExtraCocurricularLeaveEntryForm;
import com.kp.cms.forms.reports.AttendenceFinalSummaryForm;
import com.kp.cms.helpers.attendance.CocurricularAttendnaceEntryHelper;
import com.kp.cms.helpers.attendance.ExtraCocurricularLeaveEntryHelper;
import com.kp.cms.helpers.attendance.PeriodHelper;
import com.kp.cms.helpers.attendance.StudentAttendanceSummaryHelper;
import com.kp.cms.to.admin.PeriodTONew;
import com.kp.cms.to.attendance.ApproveLeaveTO;
import com.kp.cms.to.attendance.CocurricularAttendnaceEntryTo;
import com.kp.cms.to.attendance.PeriodTO;
import com.kp.cms.transactions.attandance.IExtraCocurricularLeaveEntryTransaction;
import com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction;
import com.kp.cms.transactionsimpl.attendance.ExtraCocurricularLeaveEntryTransactionImpl;
import com.kp.cms.transactionsimpl.reports.StudentWiseAttendanceSummaryTransactionImpl;
import com.kp.cms.utilities.CurrentAcademicYear;

public class ExtraCocurricularLeaveEntryHandler {
	private static volatile ExtraCocurricularLeaveEntryHandler extraCocurricularLeaveEntryHandler= null;
	private static final Log log = LogFactory.getLog(ExtraCocurricularLeaveEntryHandler.class);
	private static Map<String, String> periodsMap = null;
	static{
		periodsMap=new HashMap<String, String>();
		periodsMap.put("Period1", "P1");
		periodsMap.put("Period2", "P2");
		periodsMap.put("Period3", "P3");
		periodsMap.put("Period4", "P4");
		periodsMap.put("Period5", "P5");
		periodsMap.put("Period6", "P6");
		periodsMap.put("Period7", "P7");
	}
	public static ExtraCocurricularLeaveEntryHandler getInstance()
	{
		if(extraCocurricularLeaveEntryHandler==null)
		{
			extraCocurricularLeaveEntryHandler = new ExtraCocurricularLeaveEntryHandler();
		}
		return extraCocurricularLeaveEntryHandler;
	}
	
	public Map<Date, CocurricularAttendnaceEntryTo> getCocurricularAttendanceMap(ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm, String studentId, String courseId) throws Exception {
	    log.debug("call of getCocurricularAttendanceMap method in ExtraCocurricularLeaveEntryHandler.class");
	    IExtraCocurricularLeaveEntryTransaction transaction = new   ExtraCocurricularLeaveEntryTransactionImpl().getInstance();
	    Map<Date, CocurricularAttendnaceEntryTo> attendacePeriodMap = new HashMap<Date, CocurricularAttendnaceEntryTo>();
	    Student student = transaction.getStudentByStudentId(studentId,courseId); // get student with student Id
	    List<Period> periodList =  transaction.getPeriodListByClass(student.getClassSchemewise().getId()); 
	    List<PeriodTO> periodTOList = PeriodHelper.getInstance().copyPeriodBOToTO(periodList);
	    List<Object[]> attendanceObjectsList = new ArrayList<Object[]>();
		List<Object[]> duplicateList = new ArrayList<Object[]>();
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		String applicationLimit= "";
		try
		{
			Properties prop = new Properties();
			InputStream in = StudentUploadPhotoAction.class.getClassLoader().getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
	        prop.load(in);
	        applicationLimit=prop.getProperty("knowledgepro.extracurricular.attendace.application.attendace.date.limit");
		}
		catch (Exception e) {
			throw new ApplicationException(e);
			// TODO: handle exception
		}
		subjectMap  = transaction.getSubjectMap();
	    if(student!=null)
	    {
	    	Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int year=CurrentAcademicYear.getInstance().getAcademicyear();
			 if(year!=0){
				currentYear=year;
			 }
	    	String attendanceDuplicateQuery = ExtraCocurricularLeaveEntryHelper.getInstance().getAttendanceDuplicateQuery(extraCocurricularLeaveEntryForm,student.getId(),student.getClassSchemewise().getId(),applicationLimit);
			duplicateList = transaction.getDuplicateRecords(attendanceDuplicateQuery);
			String attendanceQuery =  ExtraCocurricularLeaveEntryHelper.getInstance().getAttendanceQuery(extraCocurricularLeaveEntryForm,student.getId(),student.getClassSchemewise().getId(),currentYear,applicationLimit);
			attendanceObjectsList = transaction.getAttendnaceListByDates(attendanceQuery);
			//list = CocurricularAttendnaceEntryHelper.getInstance().convertBolistTOtoList(attendanceObjectsList,cocurricularAttendnaceEntryForm,periodTOList,duplicateList);
			attendacePeriodMap= ExtraCocurricularLeaveEntryHelper.getInstance().getAttendancePeriodMap(attendanceObjectsList,extraCocurricularLeaveEntryForm,periodTOList,duplicateList,subjectMap);
	    }
	    log.debug("end of getCocurricularAttendanceMap method in ExtraCocurricularLeaveEntryHandler.class");
		return attendacePeriodMap;
	}
	public boolean saveCocurricularDetails(ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm) throws Exception{
		log.debug("call of saveCocurricularDetails method in ExtraCocurricularLeaveEntryHandler.class");
		boolean isAdded = false;
		IExtraCocurricularLeaveEntryTransaction transaction = new   ExtraCocurricularLeaveEntryTransactionImpl().getInstance();
		List<StuCocurrLeave> cocurricularList = ExtraCocurricularLeaveEntryHelper.getInstance().createBoObject(extraCocurricularLeaveEntryForm);
		List<StuCocurrLeave> cocurricularListNew = new ArrayList<StuCocurrLeave>();
		if(extraCocurricularLeaveEntryForm.getApproveLeaveTOs()!=null)
		{
			List<ApproveLeaveTO> approveLeaveTOs = extraCocurricularLeaveEntryForm.getApproveLeaveTOs();
			Iterator<StuCocurrLeave> iterator = cocurricularList.iterator();
			List<Integer> dupCheckLis = new ArrayList<Integer>();
			/*
			 * checking duplicate id in StuCocurrLeave object and removing the duplicate objects
			 * */
			while(iterator.hasNext())
			{
				StuCocurrLeave cocurrLeave = iterator.next();
				if(!dupCheckLis.contains(cocurrLeave.getId()))
				{
					if(cocurrLeave.getId()!=0) // for new record id will be 0 no need of duplicate check
					{
						dupCheckLis.add(cocurrLeave.getId());
						
					}
					cocurricularListNew.add(cocurrLeave); // adding to new list without having any duplicate object
				}
			}
			
			isAdded = transaction.saveCocurricularLeaveDetails(cocurricularListNew,approveLeaveTOs,extraCocurricularLeaveEntryForm);
		}
		log.debug("end of saveCocurricularDetails method in ExtraCocurricularLeaveEntryHandler.class");
		return isAdded;
	}
	
	public boolean ispublishDateValid(String studentid) throws Exception {
		IExtraCocurricularLeaveEntryTransaction transaction = new   ExtraCocurricularLeaveEntryTransactionImpl().getInstance();
		int classid = transaction.getClassIdByStudent(studentid);
		boolean validDate = transaction.isValidApplyDate(classid,"Apply for extra co curricular leave");
		return validDate;
	}
	public List<CocurricularAttendnaceEntryTo> getList(Map<Date, CocurricularAttendnaceEntryTo> map,ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm) throws Exception {
		IExtraCocurricularLeaveEntryTransaction transaction = new   ExtraCocurricularLeaveEntryTransactionImpl().getInstance();
		List<CocurricularAttendnaceEntryTo> toList = new ArrayList<CocurricularAttendnaceEntryTo>();
		Iterator<Date> itr1 = map.keySet().iterator();
		
		while(itr1.hasNext()){
			Integer difference1 = 0;
			List<PeriodTONew> plist = new ArrayList<PeriodTONew>();
			CocurricularAttendnaceEntryTo tEntryTo = new CocurricularAttendnaceEntryTo();
			Date attDate = itr1.next();
			CocurricularAttendnaceEntryTo to = map.get(attDate);
			List<PeriodTONew> periodlist = to.getPeriodToList();
			Iterator<PeriodTONew> itr2 = periodlist.iterator();
			boolean isCheck = false;
			while(itr2.hasNext()){
				PeriodTONew new1 = itr2.next();
				PeriodTONew toNew = new PeriodTONew();
				if(new1.isChecked() || new1.isTempChecked()){
					isCheck = true;
				Activity activity = transaction.getActivity(new1.getActivity());
				toNew.setActivityName(activity.getName());
				String starttime = new1.getStartTime();
				String endtime = new1.getEndTime();
				toNew.setStartTime(new1.getStartTime());
				toNew.setEndTime(new1.getEndTime());
				toNew.setPeriodName(new1.getPeriodName());

				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				Date date1 = format.parse(starttime);
				Date date2 = format.parse(endtime);
				long difference = date2.getTime() - date1.getTime();
				int hours   = (int) ((difference / (1000*60*60)) % 24);
				 difference1 = difference1 + hours;
				 toNew.setTotalHrs(String.valueOf(difference1));
				 com.ibm.icu.text.DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
				String date = df1.format(attDate);
				 toNew.setDate(date);
				 toNew.setAppStatus(new1.getAppStatus());
				 plist.add(toNew);
				}
			}
			if(plist != null && !plist.isEmpty() && isCheck){
			tEntryTo.setPeriodToList(plist);
			toList.add(tEntryTo);
			}
			
			
		}
		
		List<String> activityList = new ArrayList<String>();
		activityList = ExtraCocurricularLeaveEntryHelper.getInstance().getactivityList(toList);
		List<CocurricularAttendnaceEntryTo> finalObjectList = new ArrayList<CocurricularAttendnaceEntryTo>();
		Iterator<String> iterator = activityList.iterator();
		Map<String,String> priods=new LinkedHashMap<String, String>();
		while(iterator.hasNext()){
			String  totalDate = "";
			String totalDate1 = "";
			String totalRevisedDate = "";
			Set<String> dateSet = new HashSet<String>();
			Integer difference1 = 0;
			Integer ApprovedLeaveHrsDiff = 0;
			String periodCountActivityWise="";
			CocurricularAttendnaceEntryTo attendnaceEntryTo = new CocurricularAttendnaceEntryTo();
			String reason = iterator.next();
			List<CocurricularAttendnaceEntryTo> objectList = toList;
			Iterator<CocurricularAttendnaceEntryTo> iterator2 = objectList.iterator();
			while(iterator2.hasNext()){
				String leaveMonth = null;
				String leaveDay = null;
				String dateValue = null;
				String totalLeaveHrs = null;
				String seteMonth = null;
				String availedLeave = null;
				String approvedLeave = null;
				List<PeriodTONew>finalList = new ArrayList<PeriodTONew>();
				String periodCountActivity="";
				CocurricularAttendnaceEntryTo to = iterator2.next();
			    List<PeriodTONew> perList = to.getPeriodToList();
			    Iterator<PeriodTONew> iterator3 = perList.iterator();
			    int count=0;
			    while(iterator3.hasNext()){
			    	count++;
				PeriodTONew toNew = iterator3.next();
				PeriodTONew periodTONew = new PeriodTONew();
				if(reason.equals(toNew.getActivityName())){
					String date = toNew.getDate();
					String[] month = date.split("/");
					 leaveMonth = month[1];
					 String leavemonthWords = ExtraCocurricularLeaveEntryHelper.getInstance().monthInWords(leaveMonth);
					 leaveDay = month[0];
					 //totalDate = totalDate +""+leavemonthWords.concat(leaveDay+"("+toNew.getPeriodName()+")"+",");
					 if(!toNew.getAppStatus().equalsIgnoreCase("approved")){
					 totalDate = totalDate +""+leavemonthWords.concat(leaveDay+",");
					 totalDate1 = totalDate;
					  periodTONew.setDate(totalDate1);
					  //
					  if(periodCountActivity!=null && !periodCountActivity.isEmpty()){
						  periodCountActivity=periodCountActivity.substring( 0, periodCountActivity.length() - 2)+","+periodsMap.get(toNew.getPeriodName())+") ";
					  }else{
						  periodCountActivity=periodCountActivity+leavemonthWords.concat(leaveDay)+"("+periodsMap.get(toNew.getPeriodName())+") ";
						  
						 
						  
					  }
					 
					  
					  /*if(toNew.getPeriodName()!=null)
							periodCountActivity=periodCountActivity+periodsMap.get(toNew.getPeriodName())+",";*/
					  if(priods.containsKey(reason)){
						  priods.remove(priods.containsKey(leavemonthWords.concat(leaveDay)));
					  }
					 
					  
					  
					 }
					  periodTONew.setLeaveMonth(leavemonthWords);
					  periodTONew.setActivityName(toNew.getActivityName());
					  periodTONew.setAppStatus(toNew.getAppStatus());
					  String starttime = toNew.getStartTime();
						String endtime = toNew.getEndTime();
						SimpleDateFormat format = new SimpleDateFormat("HH:mm");
						Date date1 = format.parse(starttime);
						Date date2 = format.parse(endtime);
						long difference = date2.getTime() - date1.getTime();
						int hours   = (int) ((difference / (1000*60*60)) % 24);
						if(!toNew.getAppStatus().equalsIgnoreCase("approved")){
						 difference1 = difference1 + hours;
						 periodTONew.setTotalHrs(String.valueOf(difference1));
						}else {
							ApprovedLeaveHrsDiff = ApprovedLeaveHrsDiff + hours;
							periodTONew.setApprovedLeaveHrs(String.valueOf(ApprovedLeaveHrsDiff));
						}
						// periodTONew.setTotalHrs(String.valueOf(difference1));
						 //periodTONew.setApprovedLeaveHrs(String.valueOf(ApprovedLeaveHrsDiff));
					  finalList.add(periodTONew);
				}
				
				 if(perList.size()==count){
					 periodCountActivityWise=periodCountActivityWise+periodCountActivity;
					  priods.put(reason,periodCountActivityWise );
				 }
			}
			if(finalList != null && !finalList.isEmpty()){
			attendnaceEntryTo.setPeriodToList(finalList);
			dateValue = getDate(finalList);
			if(dateValue != null && !dateValue.isEmpty()){
			 totalRevisedDate = dateValue.substring(0, dateValue.length()-1);
			}
			if(totalRevisedDate != null && !totalRevisedDate.isEmpty()){
			String stringDates = getSet(totalRevisedDate);
			 attendnaceEntryTo.setDate(stringDates);
			}
			 totalLeaveHrs = getHrs(finalList);
			 if(totalLeaveHrs != null && !totalLeaveHrs.isEmpty()){
			 attendnaceEntryTo.setTotalHrs(totalLeaveHrs);
			 }
			 approvedLeave = getFigure(finalList);
			 if(approvedLeave != null && !approvedLeave.isEmpty()){
				 attendnaceEntryTo.setApprovedLeaveHrs(approvedLeave);
			 }
			}
			attendnaceEntryTo.setActivityName(reason);
			}
			finalObjectList.add(attendnaceEntryTo);
		}
		//showing periods in print application by bhargav
		extraCocurricularLeaveEntryForm.setPeriodsMap(priods);
		return finalObjectList;
	}

	

	

	

	
	public static Student getStudent(String studentid) throws Exception {
		IExtraCocurricularLeaveEntryTransaction transaction = new   ExtraCocurricularLeaveEntryTransactionImpl().getInstance();
		Student student = transaction.getStud(studentid);
		return student;
	}

	public List<Object[]> getclassesconducted(String studentid, int classId) throws Exception {
		IStudentWiseAttendanceSummaryTransaction studentWiseAttendanceSummaryTransaction = new StudentWiseAttendanceSummaryTransactionImpl();
		StudentAttendanceSummaryHelper helper = StudentAttendanceSummaryHelper.getInstance();
		List<Object[]> classesconducted = studentWiseAttendanceSummaryTransaction
		.getStudentWiseAttendanceSummaryInformation(StudentAttendanceSummaryHelper
				.getInstance()
				.getStudentAbsenceInformationSummaryQueryForConducted(Integer.parseInt(studentid), classId));
		return classesconducted;
	}

	public List<Object[]> getclassespresent(String studentid, int classId) throws Exception {
		IStudentWiseAttendanceSummaryTransaction studentWiseAttendanceSummaryTransaction = new StudentWiseAttendanceSummaryTransactionImpl();
		StudentAttendanceSummaryHelper helper = StudentAttendanceSummaryHelper.getInstance();
		List<Object[]> classesPresentList = studentWiseAttendanceSummaryTransaction
		.getStudentWiseAttendanceSummaryInformation(StudentAttendanceSummaryHelper
				.getInstance()
				.getStudentAbsenceInformationSummaryQueryForPresent1(Integer.parseInt(studentid), classId));
		return classesPresentList;
	}

	public String getTotalHrs(List<CocurricularAttendnaceEntryTo> leaveList) throws Exception {
		int totalHrs = 0;
		Iterator<CocurricularAttendnaceEntryTo> iterator = leaveList.iterator();
		while(iterator.hasNext()){
			CocurricularAttendnaceEntryTo to = iterator.next();
			String hrs = to.getTotalHrs();
			if(hrs != null && !hrs.isEmpty()){
			totalHrs = totalHrs + Integer.parseInt(hrs);
			}
		}
		return String.valueOf(totalHrs);
	}

	public boolean periodChecked(Map<Date, CocurricularAttendnaceEntryTo> map) throws Exception {
		boolean isChecked = false;
		List<CocurricularAttendnaceEntryTo> toList = new ArrayList<CocurricularAttendnaceEntryTo>();
		if(map != null && !map.isEmpty()){
		Iterator<Date> itr1 = map.keySet().iterator();
		while(itr1.hasNext()){
			Date attDate = itr1.next();
			CocurricularAttendnaceEntryTo to = map.get(attDate);
			List<PeriodTONew> periodlist = to.getPeriodToList();
			Iterator<PeriodTONew> itr2 = periodlist.iterator();
			while(itr2.hasNext()){
				PeriodTONew new1 = itr2.next();
				if(new1.isChecked() || new1.isTempChecked()){
					isChecked = true;
					break;
				}
			  }
			}
		}
		return isChecked;
	}
	private String getDate(List<PeriodTONew> finalList) throws Exception {
		String date = null;
		Iterator<PeriodTONew> iterator = finalList.iterator();
		while(iterator.hasNext()){
			PeriodTONew periodTONew = iterator.next();
			if(periodTONew.getDate() != null && !periodTONew.getDate().isEmpty()){
			date = periodTONew.getDate();
			}
		}
		return date;
	}
	private String getHrs(List<PeriodTONew> finalList) {
		String hrs = null;
		Iterator<PeriodTONew> iterator = finalList.iterator();
		while(iterator.hasNext()){
			PeriodTONew periodTONew = iterator.next();
			if(periodTONew.getTotalHrs() != null && !periodTONew.getTotalHrs().isEmpty()){
			hrs = periodTONew.getTotalHrs();
			}
		}
		return hrs;
	}
	private String getSet(String totalRevisedDate) {
		Set<String> set=new HashSet<String>();
		String convert = null;
		String[] split = totalRevisedDate.split(",");
		for (int i = 0; i < split.length; i++) {
			set.add(split[i]);
		}
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String str : set)  {
		    arrayList.add(str);
		}
		 convert =  arrayList.toString().replace("[", "").replace("]", "");
		return convert;
	}
	private String getFigure(List<PeriodTONew> finalList) {
		String leavehrs = null;
		Iterator<PeriodTONew> iterator = finalList.iterator();
		while(iterator.hasNext()){
			PeriodTONew periodTONew = iterator.next();
			if(periodTONew.getApprovedLeaveHrs() != null && !periodTONew.getApprovedLeaveHrs().isEmpty()){
			leavehrs = periodTONew.getApprovedLeaveHrs();
			}
		}
		return leavehrs;
	}

	public String approvedLeaveHrs(List<CocurricularAttendnaceEntryTo> leaveList) {
		int totalHrs = 0;
		Iterator<CocurricularAttendnaceEntryTo> iterator = leaveList.iterator();
		while(iterator.hasNext()){
			CocurricularAttendnaceEntryTo to = iterator.next();
			String hrs = to.getApprovedLeaveHrs();
			if(hrs != null && !hrs.isEmpty()){
			totalHrs = totalHrs + Integer.parseInt(hrs);
			}
		}
		return String.valueOf(totalHrs);
	}

	public List<CocurricularAttendnaceEntryTo> finalLeaveList(List<CocurricularAttendnaceEntryTo> leaveList) throws Exception {
		List<CocurricularAttendnaceEntryTo> finalList = new ArrayList<CocurricularAttendnaceEntryTo>();
		List<CocurricularAttendnaceEntryTo> list = leaveList;
		Iterator<CocurricularAttendnaceEntryTo> iterator = list.iterator();
		while(iterator.hasNext()){
			boolean approved = false;
			CocurricularAttendnaceEntryTo to = iterator.next();
			List<PeriodTONew> pList = to.getPeriodToList();
			Iterator<PeriodTONew> iterator2 = pList.iterator();
			while (iterator2.hasNext()){
				PeriodTONew new1 = iterator2.next();
				if(new1.getAppStatus().equalsIgnoreCase("approved")){
					approved = true;
				}
			}
			if(!approved){
			finalList.add(to);
			}
		}
		
		return finalList;
	}

}
