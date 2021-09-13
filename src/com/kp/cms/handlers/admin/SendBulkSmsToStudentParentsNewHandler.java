package com.kp.cms.handlers.admin;

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
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.SMSTemplate;
import com.kp.cms.bo.admin.SendBulkSmsToStudentParentsNewBo;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.constants.CMSConstants;

import com.kp.cms.forms.admin.SendBulkSmsToStudentParentsNewForm;
import com.kp.cms.forms.attendance.NewAttendanceSmsForm;
import com.kp.cms.handlers.attendance.NewAttendanceSmsHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.helpers.admin.SendBulkSmsToStudentParentsNewHelper;
import com.kp.cms.helpers.attendance.AttendanceEntryHelper;
import com.kp.cms.helpers.attendance.NewAttendanceSmsHelper;
import com.kp.cms.to.admin.EmployeeTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.admin.ISendBulkSmsToStudentParentsNew;
import com.kp.cms.transactions.attandance.IAttendanceEntryTransaction;
import com.kp.cms.transactions.attandance.INewAttendanceSmsTransaction;
import com.kp.cms.transactionsimpl.admin.SendBulkSmsToStudentParentsNewImpl;
import com.kp.cms.transactionsimpl.attendance.AttendanceEntryTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.NewAttendanceSmsTransImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.ConverationUtil;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.SMSUtil;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.SMS_Message;

public class SendBulkSmsToStudentParentsNewHandler {
	private static final Log log = LogFactory.getLog(SendBulkSmsToStudentParentsNewHandler.class);
	private static volatile SendBulkSmsToStudentParentsNewHandler sendBulkSmsToStudentParentsNewHandler = null;
    private SendBulkSmsToStudentParentsNewHandler(){
    	
    }
	public static SendBulkSmsToStudentParentsNewHandler getInstance() {
		if (sendBulkSmsToStudentParentsNewHandler == null) {
			sendBulkSmsToStudentParentsNewHandler = new SendBulkSmsToStudentParentsNewHandler();
		}
		return sendBulkSmsToStudentParentsNewHandler;
	}
	
	
	public boolean sendSMS(SendBulkSmsToStudentParentsNewForm sbForm,
			List<Student> studentList) throws Exception{
		// TODO Auto-generated method stub

		log.info(" ********************* Student list size in log file  ******************** :"+studentList.size());
		log.debug(" ******************* Student start in log file ***********************");
		System.out.println("++++++++++++++++++ Student list size in sysout ++++++++++++++++++: "+studentList.size());
		boolean sent=false;
		
		//check student size
		if(studentList.size()!=0){
			
		
		Iterator<Student> itr=studentList.iterator();
		Student student=null;
		List<SendBulkSmsToStudentParentsNewBo> list=new ArrayList<SendBulkSmsToStudentParentsNewBo>();

		while(itr.hasNext())
		{
			
			student=itr.next();
			//testing students
			//if(student.getId()==3532){
				
			//System.out.println("---------------------------s id"+student.getId());
			if(student.getClassSchemewise()!=null){
				
			SendBulkSmsToStudentParentsNewBo bo=new SendBulkSmsToStudentParentsNewBo();
				
			 bo.setClassId(student.getClassSchemewise().getClasses().getId());
			 
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
				temp=temp+URLEncoder.encode(sbForm.getMessage()+ " ","UTF-8");
				if(footer!=null){
				   temp=temp+URLEncoder.encode("Principal "+footer+"", "UTF-8");
				}else{
					temp=temp+URLEncoder.encode("Principal MIC College", "UTF-8");
					
				}
					bo.setMessagePriority(3);
				    bo.setSenderName(senderName);
				    bo.setSenderNumber(senderNumber);
				    bo.setMessageEnqueueDate(new Date());
				    bo.setStudentId(student.getId());
				    bo.setIsMessageSent(false);
				    bo.setMessageBody(temp);
				   if(sbForm.getIsStudent()){
				    bo.setIsStudent(true);
				    String countrycode="91";
				    if(student.getAdmAppln().getPersonalData().getMobileNo1()!=null && !student.getAdmAppln().getPersonalData().getMobileNo1().isEmpty())
				    	countrycode=student.getAdmAppln().getPersonalData().getMobileNo1();
				    if(student.getAdmAppln().getPersonalData().getMobileNo2()!=null && !student.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()){
				    	bo.setDestinationNumber(countrycode+student.getAdmAppln().getPersonalData().getMobileNo2());
				    }
				    
				   
				   }
				   else
				   {
				    	bo.setIsStudent(false);	

					    String countrycode="91";
					    if(student.getAdmAppln().getPersonalData().getParentMob1()!=null && !student.getAdmAppln().getPersonalData().getParentMob1().isEmpty())
					    	countrycode=student.getAdmAppln().getPersonalData().getParentMob1();
					    if(student.getAdmAppln().getPersonalData().getParentMob2()!=null && !student.getAdmAppln().getPersonalData().getParentMob2().isEmpty()){
					    	bo.setDestinationNumber(countrycode+student.getAdmAppln().getPersonalData().getParentMob2());
					    }
					    
					    else if(student.getAdmAppln().getPersonalData().getGuardianMob2()!=null && !student.getAdmAppln().getPersonalData().getGuardianMob2().isEmpty()){
					    	if(student.getAdmAppln().getPersonalData().getGuardianMob1()!=null && !student.getAdmAppln().getPersonalData().getGuardianMob1().isEmpty()){
					    		countrycode=student.getAdmAppln().getPersonalData().getGuardianMob1();
					    	}
					    	if(student.getAdmAppln().getPersonalData().getGuardianMob2()!=null && !student.getAdmAppln().getPersonalData().getGuardianMob2().isEmpty()){
					    		bo.setDestinationNumber(countrycode+student.getAdmAppln().getPersonalData().getMobileNo2());
					    	}
					    }
					   
				   }
				   
				  bo.setCreatedDate(new Date());
				  
				   list.add(bo);
				   
			}//class null check over
			
		}
				 
		//}//testing single student  
		
	    PropertyUtil.getInstance().saveSMS1(list);
	    
	    log.info(" ********************* adding Student list to SendBulkSmsToStudentParentsBo in log file after size ******************** :"+list.size());
		log.debug(" ******************* add list to SendBulkSmsToStudentParentsBo list***********************");
		System.out.println("++++++++++++++++++ adding Student list to SendBulkSmsToStudentParentsBo in sysout after size ++++++++++++++++++: "+list.size());
		
		SMSUtil s =new SMSUtil();
    	SMSUtils smsUtils=new SMSUtils();
        ConverationUtil converationUtil=new ConverationUtil();
    	List<SMS_Message> listSms=converationUtil.converttoTo1(s.getSMSList1());
    	
    	log.info(" ********************* after sending sms in log file after size ******************** :"+list.size());
		log.debug(" ******************* after sending sms ***********************");
		System.out.println("++++++++++++++++++ after sending sms in sysout after size ++++++++++++++++++: "+list.size());
		//raghu write new method for bulk sms storing
    	List<SMS_Message> mobList=smsUtils.sendSMSBulk(listSms);
    	sent=s.updateSMS1List(converationUtil.converttoBO1(mobList));
    	//this is for checking dummy data
    	//sent=s.updateSMSList(converationUtil.converttoBO(listSms));
		
    	log.info(" ********************* final size ******************** :"+mobList.size());
		log.debug(" ******************* final ***********************");
		System.out.println("++++++++++++++++++ final size ++++++++++++++++++: "+mobList.size());
		
		}// student size check over
		
		
		log.info("Handler : Leaving getAttendanceObj");
		return sent;
	
	}
	public void getStudents(SendBulkSmsToStudentParentsNewForm sendbulksms) throws Exception 
	{
		log.info("Handler : Inside getStudents");
		ISendBulkSmsToStudentParentsNew sendbulksmstrans = new SendBulkSmsToStudentParentsNewImpl();
		List<StudentTO> studentList = new ArrayList<StudentTO>();
		List<Student> students;
		List<Integer> listOfDetainedStudents =SendBulkSmsToStudentParentsNewHandler.getInstance().getDetainedOrDiscontinuedStudents();
		
		
		students = sendbulksmstrans.getStudentByClass(Integer.parseInt(sendbulksms.getClassId()));
						
			studentList = SendBulkSmsToStudentParentsNewHelper.getInstance()
					.copyStudentBoToTO(students, listOfDetainedStudents);
			sendbulksms.setStudentList(studentList);
		String date = new Date().toLocaleString();
		sendbulksms.setStudentList(studentList);
		log.info("Handler : Leaving getStudents");
}
	public List<Integer> getDetainedOrDiscontinuedStudents()throws Exception
	{
		ISendBulkSmsToStudentParentsNew impl=new SendBulkSmsToStudentParentsNewImpl();
		return impl.getDetainedOrDiscontinuedStudents();
	}
	public void getTeacher(SendBulkSmsToStudentParentsNewForm sendbulksms) throws Exception 
	{
		log.info("Handler : Inside getStudents");
		ISendBulkSmsToStudentParentsNew sendbulksmstrans = new SendBulkSmsToStudentParentsNewImpl();
		List<EmployeeTO> employeeList = new ArrayList<EmployeeTO>();
		List<Employee> employee;
		Set<Integer> deptSet = new HashSet<Integer>();
		for (String str : sendbulksms.getDepartmentIds()) {
			if(str != null){
				deptSet.add(Integer.parseInt(str));
			}
		}
		employee = sendbulksmstrans.getTeachersByDepartment(deptSet);
						
		employeeList = SendBulkSmsToStudentParentsNewHelper.getInstance()
					.copyTeacherBoToTO(employee);
			sendbulksms.setEmployeeList(employeeList);
		String date = new Date().toLocaleString();
		log.info("Handler : Leaving getTeacher");
}
	
	public void getNonTeacher(SendBulkSmsToStudentParentsNewForm sendbulksms) throws Exception 
	{
		log.info("Handler : Inside getStudents");
		ISendBulkSmsToStudentParentsNew sendbulksmstrans = new SendBulkSmsToStudentParentsNewImpl();
		List<EmployeeTO> employeeList = new ArrayList<EmployeeTO>();
		List<Employee> employee;
		employee = sendbulksmstrans.getNonTeachersByDepartment();
						
		employeeList = SendBulkSmsToStudentParentsNewHelper.getInstance()
					.copyTeacherBoToTO(employee);
			sendbulksms.setEmployeeList(employeeList);
		String date = new Date().toLocaleString();
		log.info("Handler : Leaving getTeacher");
}
	public boolean sendSMSToStudents(SendBulkSmsToStudentParentsNewForm sbForm,
			List<StudentTO> studentList) throws Exception
	{
		ISendBulkSmsToStudentParentsNew sendbulksmstransaction=new SendBulkSmsToStudentParentsNewImpl();
		SendBulkSmsToStudentParentsNewBo bo;
		List<SendBulkSmsToStudentParentsNewBo> list=new ArrayList<SendBulkSmsToStudentParentsNewBo>();
		
		boolean isMessageSent=false;
		if(studentList.size()!=0)
		{
			Iterator<StudentTO> itr=studentList.iterator();
			StudentTO studentTO;
			List<MobileMessaging> smsList=new ArrayList<MobileMessaging>();
			while(itr.hasNext())
			{
				bo=new SendBulkSmsToStudentParentsNewBo();
				studentTO=itr.next();
				if(studentTO.isChecked())
				{
					
					SMSTemplateHandler temphandle=SMSTemplateHandler.getInstance();
					
					
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
					temp=temp+URLEncoder.encode(sbForm.getMessage()+ " ","UTF-8");
					
					MobileMessaging mob=new MobileMessaging();	
					if(sbForm.getParent().equalsIgnoreCase("false"))
					{
						bo.setDestinationNumber(studentTO.getMobileNo1()+studentTO.getMobileNo2());
						bo.setIsStudent(true);
					}
					else if(sbForm.getParent().equalsIgnoreCase("true"))
					{
						bo.setDestinationNumber(studentTO.getParentMobileNo1()+studentTO.getParentMobileNo2());
						bo.setIsStudent(false);
					}
					if(temp.length()>140)
					{
						//mob.setMessageBody(URLEncoder.encode(temp.substring(0,132)+"from Principal NHPUC"));
						if(footer!=null){
							temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
						}else{
							temp=temp+URLEncoder.encode(" Principal MIC College", "UTF-8");
						}
						
						
						mob.setMessageBody(temp);
					}
					else
					{  

						if(footer!=null){
							temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
						}else{
							temp=temp+URLEncoder.encode(" Principal MIC College", "UTF-8");
						}
						bo.setMessageBody(temp);
					}				
					bo.setMessagePriority(3);
					bo.setSenderName(senderName);
					bo.setSenderNumber(senderNumber);
					bo.setMessageEnqueueDate(new Date());
					bo.setStudentId(studentTO.getId());
					bo.setIsMessageSent(false);
					bo.setCreatedDate(new Date());
					

					
						
					list.add(bo);
				}
					
			}
			
			 PropertyUtil.getInstance().saveSMS1(list);
			    
			    log.info(" ********************* adding Student list to SendBulkSmsToStudentParentsBo in log file after size ******************** :"+list.size());
				log.debug(" ******************* add list to SendBulkSmsToStudentParentsBo list***********************");
				System.out.println("++++++++++++++++++ adding Student list to SendBulkSmsToStudentParentsBo in sysout after size ++++++++++++++++++: "+list.size());
				
				SMSUtil s =new SMSUtil();
		    	SMSUtils smsUtils=new SMSUtils();
		        ConverationUtil converationUtil=new ConverationUtil();
		    	//List<SMS_Message> listSms=converationUtil.converttoTo(s.getSMSList());
		    	
		        List<SMS_Message> listSms=converationUtil.converttoTo1(s.getSMSList1());
		    	log.info(" ********************* after sending sms in log file after size ******************** :"+list.size());
				log.debug(" ******************* after sending sms ***********************");
				System.out.println("++++++++++++++++++ after sending sms in sysout after size ++++++++++++++++++: "+list.size());
				//raghu write new method for bulk sms storing
		    	List<SMS_Message> mobList=smsUtils.sendSMSBulk(listSms);
		    	isMessageSent=s.updateSMS1List(converationUtil.converttoBO1(mobList));
		    	//this is for checking dummy data
		    	//sent=s.updateSMSList(converationUtil.converttoBO(listSms));
				
		    	log.info(" ********************* final size ******************** :"+mobList.size());
				log.debug(" ******************* final ***********************");
				System.out.println("++++++++++++++++++ final size ++++++++++++++++++: "+mobList.size());
				
	    	
			}
			log.info("Handler : Leaving sendSmsToStudents");
			return isMessageSent;
	}
	public boolean sendSMSToTeacherOrNonTeacher(SendBulkSmsToStudentParentsNewForm sbForm,
			List<EmployeeTO> employeeList) throws Exception
	{
		ISendBulkSmsToStudentParentsNew sendbulksmstransaction=new SendBulkSmsToStudentParentsNewImpl();
		SendBulkSmsToStudentParentsNewBo bo;
		List<SendBulkSmsToStudentParentsNewBo> list=new ArrayList<SendBulkSmsToStudentParentsNewBo>();
		boolean isMessageSent=false;
		if(employeeList.size()!=0)
		{
			Iterator<EmployeeTO> itr=employeeList.iterator();
			EmployeeTO employeeTO;
			List<MobileMessaging> smsList=new ArrayList<MobileMessaging>();
			while(itr.hasNext())
			{
				bo=new SendBulkSmsToStudentParentsNewBo();
				employeeTO=itr.next();
				if(employeeTO.isChecked())
				{
					
					SMSTemplateHandler temphandle=SMSTemplateHandler.getInstance();
					
					
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
					temp=temp+URLEncoder.encode(sbForm.getMessage()+ " ","UTF-8");
					
					MobileMessaging mob=new MobileMessaging();	
					bo.setDestinationNumber("91"+employeeTO.getCurrentAddressMobile1());
					if(temp.length()>140)
					{
						//mob.setMessageBody(URLEncoder.encode(temp.substring(0,132)+"from Principal NHPUC"));
						if(footer!=null){
							temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
						}else{
							temp=temp+URLEncoder.encode(" Principal MIC College", "UTF-8");
						}
						
						
						bo.setMessageBody(temp);
					}
					else
					{  

						if(footer!=null){
							temp=temp+URLEncoder.encode(" Principal "+footer+"", "UTF-8");
						}else{
							temp=temp+URLEncoder.encode(" Principal MIC College", "UTF-8");
						}
						bo.setMessageBody(temp);
					}				
					bo.setMessagePriority(3);
					bo.setSenderName(senderName);
					bo.setSenderNumber(senderNumber);
					bo.setMessageEnqueueDate(new Date());
					bo.setIsMessageSent(false);
					bo.setCreatedDate(new Date());
					bo.setEmployeeId(employeeTO.getId());
					bo.setIsStudent(false);
					
						list.add(bo);	
				}
			}
			PropertyUtil.getInstance().saveSMS1(list);
		    
		    log.info(" ********************* adding Employee list to SendBulkSmsToStudentParentsNewBo in log file after size ******************** :"+list.size());
			log.debug(" ******************* add list to SendBulkSmsToStudentParentsNewBo list***********************");
			System.out.println("++++++++++++++++++ adding Employee list to SendBulkSmsToStudentParentsNewBo in sysout after size ++++++++++++++++++: "+list.size());
			
			SMSUtil s =new SMSUtil();
	    	SMSUtils smsUtils=new SMSUtils();
	        ConverationUtil converationUtil=new ConverationUtil();
	    	List<SMS_Message> listSms=converationUtil.converttoTo1(s.getSMSList1());
	    	
	    	log.info(" ********************* after sending sms in log file after size ******************** :"+list.size());
			log.debug(" ******************* after sending sms ***********************");
			System.out.println("++++++++++++++++++ after sending sms in sysout after size ++++++++++++++++++: "+list.size());
			//raghu write new method for bulk sms storing
	    	List<SMS_Message> mobList=smsUtils.sendSMSBulk(listSms);
	    	isMessageSent=s.updateSMS1List(converationUtil.converttoBO1(mobList));
	    	//this is for checking dummy data
	    	//sent=s.updateSMSList(converationUtil.converttoBO(listSms));
			
	    	log.info(" ********************* final size ******************** :"+mobList.size());
			log.debug(" ******************* final ***********************");
			System.out.println("++++++++++++++++++ final size ++++++++++++++++++: "+mobList.size());
	    	
	    	
		}
			log.info("Handler : Leaving getAttendanceObj");
			return isMessageSent;
	}

}
