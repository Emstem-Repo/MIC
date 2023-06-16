package com.kp.cms.handlers.attendance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.AttendanceStudent;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.SMSTemplate;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.attendance.NewAttendanceSmsForm;
import com.kp.cms.handlers.admin.SMSTemplateHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.helpers.attendance.AttendanceEntryHelper;
import com.kp.cms.helpers.attendance.NewAttendanceSmsHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.attandance.IAttendanceEntryTransaction;
import com.kp.cms.transactions.attandance.INewAttendanceSmsTransaction;
import com.kp.cms.transactionsimpl.attendance.AttendanceEntryTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.NewAttendanceSmsTransImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.ConverationUtil;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.SMSUtil;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.SMS_Message;

public class NewAttendanceSmsHandler 
{
	private static volatile NewAttendanceSmsHandler newAttendanceSmsHandler = null;
	private static final Log log = LogFactory.getLog(NewAttendanceSmsHandler.class);
	
	public static NewAttendanceSmsHandler getInstance() 
	{
		if (newAttendanceSmsHandler == null) 
		{
			newAttendanceSmsHandler = new NewAttendanceSmsHandler();
			return newAttendanceSmsHandler;
		}
		return newAttendanceSmsHandler;
	}

	@SuppressWarnings("deprecation")
	public void getAbsentees(NewAttendanceSmsForm newAttendanceSmsForm) 
	{
		log.info("Handler : Inside getStudents");
		INewAttendanceSmsTransaction newAttendanceSmsTransaction = NewAttendanceSmsTransImpl.getInstance();
		
		int year=0;
		String[] classes=null;
		java.sql.Date date=CommonUtil.ConvertStringToSQLDate(newAttendanceSmsForm.getAttendancedate());
		if(newAttendanceSmsForm.getYear()!=null && !newAttendanceSmsForm.getYear().isEmpty())
		{
			year=Integer.parseInt(newAttendanceSmsForm.getYear());
		}
		if(newAttendanceSmsForm.getClasses()!=null && newAttendanceSmsForm.getClasses().length>0 && date!=null && year>0)
		{
			classes=newAttendanceSmsForm.getClasses();
			List<Object[]> absenteeList=newAttendanceSmsTransaction.getAbsentees(year,classes,date);
			List<StudentTO> absenteesToList=NewAttendanceSmsHelper.getInstance().convertBoToTO(absenteeList);
			newAttendanceSmsForm.setAbsenteesList(absenteesToList);
		}
		
		
	}

	@SuppressWarnings("static-access")
	public boolean sendSMS(NewAttendanceSmsForm newAttendanceSmsForm) throws Exception 
	{
		log.info("Handler : Inside getAttendanceObj");
		INewAttendanceSmsTransaction newAttendanceSmsTransaction = NewAttendanceSmsTransImpl.getInstance();
		IAttendanceEntryTransaction attendanceEntryTransaction = AttendanceEntryTransactionImpl.getInstance();
		boolean sent=false;
		String date =new Date().toString();
		
		if(newAttendanceSmsForm.getStudentList().size()!=0){
			
		Iterator<StudentTO> itr=newAttendanceSmsForm.getStudentList().iterator();
		StudentTO studentTO=null;
		List<MobileMessaging> smsList=new ArrayList<MobileMessaging>();
		java.sql.Date attDate=CommonUtil.ConvertStringToSQLDate(newAttendanceSmsForm.getAttendancedate());
		
		while(itr.hasNext())
		{
			studentTO=itr.next();
			String periodNames="";
			String subjectNames="";
			if(studentTO.getSubList()!=null && !studentTO.isChecked() )
			{
				Object[] tempArray1 = studentTO.getSubList().toArray();
				String intType1 ="";
				for(int i=0;i<tempArray1.length;i++){
					 intType1 = tempArray1[i].toString();
					 String subquery1="select s.consldtdMarkCardSubName from Subject s where s.id ="+intType1;
						//periodNames=transaction.getPeriodNamesById(query);
					 subjectNames=subjectNames+NewAttendanceSmsTransImpl.getInstance().getSubjectNamesByIdNew(subquery1)+ ",";
					 
					 if(i<(tempArray1.length-1)){
						 intType1 = intType1+",";
					 }
				}
		
			
			if(studentTO.getPeriodList() != null && !studentTO.isChecked()) 
			{	
				Object[] tempArray = studentTO.getPeriodList().toArray();
				String intType ="";
				for(int i=0;i<tempArray.length;i++){
					 intType = intType+tempArray[i].toString();
					 if(i<(tempArray.length-1)){
						 intType = intType+",";
					 }
				}
				
				
				String query="select distinct p.periodName from Period p where p.id in ("+intType+")";
				periodNames=attendanceEntryTransaction.getPeriodNamesById(query);
				String[] perNames=periodNames.split(",");
				String periods="";
				for(String p:perNames)
				{
					periods=periods+p.charAt(p.length()-1)+"hr,";
				}				
				String desc="";
				
				
				SMSTemplateHandler temphandle=SMSTemplateHandler.getInstance();
				List<SMSTemplate> list= temphandle.getDuplicateCheckList(0,CMSConstants.TEMPLATE_ATTENDANCE_ENTRY);
				if(list != null && !list.isEmpty()) {
					desc = list.get(0).getTemplateDescription();
					desc=desc.replace(CMSConstants.TEMPLATE_SMS_DATE, date);
					desc=desc.replace(CMSConstants.TEMPLATE_SMS_PERIOD, periods);
				}
				Properties prop = new Properties();
				try {
					InputStream in = AttendanceEntryHelper.class.getClassLoader()
					.getResourceAsStream(CMSConstants.SMS_FILE_CFG);
					prop.load(in);
				} catch (FileNotFoundException e) {	
				log.error("Unable to read properties file...", e);
					return false;
				} catch (IOException e) {
					log.error("Unable to read properties file...", e);
					return false;
				}
				String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
				String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
				String footer=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_FOOTER);
				
				String temp = "";
				temp=temp+URLEncoder.encode("Dear" + " ", "UTF-8");
				
				temp=temp+URLEncoder.encode("Parent, ","UTF-8"); 
				temp=temp+URLEncoder.encode("Your ward ","UTF-8");
				//temp = temp+URLEncoder.encode(studentTO.getStudentName().substring(0, 40) + " ","UTF-8" );	
				if(studentTO.getStudentName()!=null && !studentTO.getStudentName().isEmpty())
				{
					if(studentTO.getStudentName().length()>40)
					{
						temp = temp+URLEncoder.encode(studentTO.getStudentName().substring(0, 40) + " ","UTF-8" );		
					}
					else
					{
						temp = temp+URLEncoder.encode(studentTO.getStudentName() + " ","UTF-8" );	
					}
				}
				
				//temp=temp+ URLEncoder.encode("was absent for the" + " ", "UTF-8"); 
				//temp=temp+ URLEncoder.encode("was absent today("+CommonUtil.getTodayDate()+") during" + " ", "UTF-8");
				temp=temp+ URLEncoder.encode("was absent today"+CommonUtil.getTodayDate()+" ", "UTF-8");
				//temp=temp+ URLEncoder.encode(""+subjectNames+"", "UTF-8");
				//temp=temp+ URLEncoder.encode(""+periodNames+"", "UTF-8");
				
				
				//temp=temp+ URLEncoder.encode("on "+newAttendanceSmsForm.getAttendancedate(), "UTF-8");
				
				MobileMessaging mob=new MobileMessaging();						
				mob.setDestinationNumber(studentTO.getMobileNo1()+studentTO.getMobileNo2());
				if(temp.length()>140)
				{
					//mob.setMessageBody(URLEncoder.encode(temp.substring(0,132)+"from Principal NHPUC"));
					if(footer!=null){
						temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
					}else{
						temp=temp+URLEncoder.encode(" Principal Mar Ivanios College", "UTF-8");
					}
					
					
					mob.setMessageBody(temp);
				}
				else
				{  

					if(footer!=null){
						temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
					}else{
						temp=temp+URLEncoder.encode(" Principal Mar Ivanios College", "UTF-8");
					}
					mob.setMessageBody(temp);
				}				
				mob.setMessagePriority(3);
				mob.setSenderName(senderName);
				mob.setSenderNumber(senderNumber);
				mob.setMessageEnqueueDate(new Date());
				mob.setAttendanceDate(CommonUtil.ConvertStringToSQLDate(newAttendanceSmsForm.getAttendancedate()));
				mob.setStudentId(studentTO.getId());
				mob.setIsMessageSent(false);
				mob.setCreatedDate(new Date());
				
				List<Integer> studId=newAttendanceSmsTransaction.getStudentIds(attDate);
				if(!studId.contains(studentTO.getId()))
				{
					smsList.add(mob);	
				}									

				if(studentTO.getAttendanceStudentId()!=null && !studentTO.getAttendanceStudentId().isEmpty())
		    	{
					List<AttendanceStudent> atteStudList=newAttendanceSmsTransaction.getAttendanceStudent(studentTO.getAttendanceStudentId());				    			
	    			sent=newAttendanceSmsTransaction.updateAttendanceStudent(atteStudList);
		    	}				
			}
			}
		}
		PropertyUtil.getInstance().saveSMSList(smsList);
		SMSUtil s =new SMSUtil();
    	SMSUtils smsUtils=new SMSUtils();

		ConverationUtil converationUtil=new ConverationUtil();
    	List<SMS_Message> listSms=converationUtil.convertBotoTO(s.getListOfSMS());
    	List<SMS_Message> mobList=smsUtils.sendSMS(listSms);
    	s.updateSMS(converationUtil.convertTotoBO(mobList));
    	
	}
		log.info("Handler : Leaving getAttendanceObj");
		return sent;
	
	}

	public void getStudents(NewAttendanceSmsForm newAttendanceSmsForm) throws Exception 
	{
		log.info("Handler : Inside getStudents");
		IAttendanceEntryTransaction attendanceEntryTransaction = AttendanceEntryTransactionImpl.getInstance();
		List<StudentTO> studentList = new ArrayList<StudentTO>();
		List<Student> students;
		Set<Integer> classSet = new HashSet<Integer>();
		List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
		for (String str : newAttendanceSmsForm.getClasses()) {
			if(str != null){
				classSet.add(Integer.parseInt(str));
			}
		}
		students = attendanceEntryTransaction.getStudentByClass(classSet);
						
			studentList = NewAttendanceSmsHelper.getInstance()
					.copyStudentBoToTO(students, listOfDetainedStudents,newAttendanceSmsForm.getAbsenteesList());
			newAttendanceSmsForm.setStudentList(studentList);
		String date = new Date().toLocaleString();
		newAttendanceSmsForm.setStudentList(studentList);
		int halfLength = 0;
		int totLength = studentList.size();
		if (totLength % 2 == 0) {
			halfLength = totLength / 2;
		} else {
			halfLength = (totLength / 2) + 1;
		}
		newAttendanceSmsForm.setHalfLength(halfLength);
		log.info("Handler : Leaving getStudents");
}
		
	}
	
