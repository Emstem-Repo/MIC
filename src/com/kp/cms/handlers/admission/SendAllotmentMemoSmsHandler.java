package com.kp.cms.handlers.admission;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.forms.admission.SendAllotmentMemoSmsForm;
import com.kp.cms.handlers.admission.SendAllotmentMemoSmsHandler;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admission.SendAllotmentMemoSmsBo;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.bo.admission.GenerateMemoMail;
import com.kp.cms.helpers.attendance.AttendanceEntryHelper;
import com.kp.cms.transactions.admission.ISendAllotmentSmsTransaction;
import com.kp.cms.transactionsimpl.admission.SendAllotmentSmsTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.ConverationUtil;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.SMSUtil;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.MailTO;
import com.kp.cms.utilities.jms.SMS_Message;

public class SendAllotmentMemoSmsHandler {
	private static final Log log = LogFactory.getLog(SendAllotmentMemoSmsHandler.class);
	private static volatile SendAllotmentMemoSmsHandler sendAllotmentMemoSmsHandler = null;
    private SendAllotmentMemoSmsHandler(){
    	
    }
	public static SendAllotmentMemoSmsHandler getInstance() {
		if (sendAllotmentMemoSmsHandler == null) {
			sendAllotmentMemoSmsHandler = new SendAllotmentMemoSmsHandler();
		}
		return sendAllotmentMemoSmsHandler;
	}
	
	
	public boolean sendSMS(SendAllotmentMemoSmsForm sbForm,
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
		List<SendAllotmentMemoSmsBo> list=new ArrayList<SendAllotmentMemoSmsBo>();

		while(itr.hasNext())
		{
			
			student=itr.next();
			//testing students
			//if(student.getId()==3532){
				
			//System.out.println("---------------------------s id"+student.getId());
			if(true){
				
				SendAllotmentMemoSmsBo bo=new SendAllotmentMemoSmsBo();
				
			    //bo.setCourseId(Integer.parseInt(sbForm.getCourseId()));
			 
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
				temp=temp+URLEncoder.encode("Dear "+student.getAdmAppln().getPersonalData().getFirstName()+ " ","UTF-8");
				temp=temp+URLEncoder.encode(sbForm.getMessage()+ " ","UTF-8");
				if(footer!=null){
				   temp=temp+URLEncoder.encode("Principal "+footer+"", "UTF-8");
				}else{
					temp=temp+URLEncoder.encode("Principal Mar Ivanios College College, Trivandrum", "UTF-8");
					
				}
					bo.setMessagePriority(3);
					bo.setAppliedYear(Integer.parseInt(sbForm.getYear()));
				    bo.setSenderName(senderName);
				    bo.setSenderNumber(senderNumber);
				    bo.setMessageEnqueueDate(new Date());
				    bo.setStudentId(student.getId());
				    bo.setIsMessageSent(false);
				    bo.setMessageBody(temp);
				    bo.setAllotmentNo(sbForm.getSureOrChanceMemoNo());
				    bo.setIsSureMemo(sbForm.getIsSureMemo());
				    String countrycode="91";
				    if(student.getAdmAppln().getPersonalData().getMobileNo1()!=null && !student.getAdmAppln().getPersonalData().getMobileNo1().isEmpty())
				    	countrycode=student.getAdmAppln().getPersonalData().getMobileNo1();
				    if(student.getAdmAppln().getPersonalData().getMobileNo2()!=null && !student.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()){
				    	bo.setDestinationNumber(countrycode+student.getAdmAppln().getPersonalData().getMobileNo2());
				    }
				  bo.setCreatedDate(new Date());
				  
				   list.add(bo);
				   
			}//class null check over
			
		}
				 
		//}//testing single student  
		
	    PropertyUtil.getInstance().saveAllotmentMemoSms(list);
	    
	    log.info(" ********************* adding Student list to SendBulkSmsToStudentParentsBo in log file after size ******************** :"+list.size());
		log.debug(" ******************* add list to SendBulkSmsToStudentParentsBo list***********************");
		System.out.println("++++++++++++++++++ adding Student list to SendBulkSmsToStudentParentsBo in sysout after size ++++++++++++++++++: "+list.size());
		SMSUtil s =new SMSUtil();
    	SMSUtils smsUtils=new SMSUtils();
        ConverationUtil converationUtil=new ConverationUtil();
    	List<SMS_Message> listSms=converationUtil.converttoToAllotmentMemoSms(s.getAllotmentMemoSMSList());
    	
    	log.info(" ********************* after sending sms in log file after size ******************** :"+list.size());
		log.debug(" ******************* after sending sms ***********************");
		System.out.println("++++++++++++++++++ after sending sms in sysout after size ++++++++++++++++++: "+list.size());
		//raghu write new method for bulk sms storing
    	List<SMS_Message> mobList=smsUtils.sendAllotmentMemoSMS(listSms);
    	sent=s.updateSMSList(converationUtil.converttoBOAllotmentMemoSms(mobList));
    	//this is for checking dummy data
    	//sent=s.updateSMSList(converationUtil.converttoBO(listSms));
		
    	log.info(" ********************* final size ******************** :"+mobList.size());
		log.debug(" ******************* final ***********************");
		System.out.println("++++++++++++++++++ final size ++++++++++++++++++: "+mobList.size());
		
		}// student size check over
		
		
		log.info("Handler : Leaving getAttendanceObj");
		return sent;
	
	}
	public List<Student> getStudents(SendAllotmentMemoSmsForm sbForm, Set<Integer> tempcourseset) throws ApplicationException {
		ISendAllotmentSmsTransaction txn = new SendAllotmentSmsTransactionImpl();
		    List<Student> studentList = new ArrayList<Student>(); 
			List<Integer> sentMemoList = txn.getSentMemoList(sbForm);
			  if(sbForm.getIsSureMemo()) {
				  studentList = txn.getSureStudentList(sbForm,sentMemoList,tempcourseset);
			  }else{
				  studentList = txn.getChanceStudentList(sbForm,sentMemoList,tempcourseset);
			  }
					
		
		return studentList;
	}
	public boolean sendMail(SendAllotmentMemoSmsForm sbForm,List<Student> studentList) throws Exception {
		log.info(" ********************* Student list size in log file  ******************** :"+studentList.size());
		log.debug(" ******************* Student start in log file ***********************");
		System.out.println("++++++++++++++++++ Student list size in sysout ++++++++++++++++++: "+studentList.size());
		boolean sent=false;
		if(studentList.size()!=0){
			
			Iterator<Student> itr=studentList.iterator();
			Student student=null;
			List<GenerateMemoMail> list=new ArrayList<GenerateMemoMail>();

            while (itr.hasNext()) {
				 student = (Student) itr.next();
				 GenerateMemoMail mailBo = new GenerateMemoMail();
				 try
					{
				 	boolean sent1=false;
					Properties prop = new Properties();
					String toAddress="";
					if(student.getAdmAppln().getPersonalData().getEmail()!=null && !student.getAdmAppln().getPersonalData().getEmail().isEmpty()){
						toAddress = toAddress +student.getAdmAppln().getPersonalData().getEmail();
			 		}
					String collegeName = CMSConstants.COLLEGE_NAME;
					String subject="Admission Status for the candidate[ "+collegeName+"]";
					String msg= "Dear "+student.getAdmAppln().getPersonalData().getFirstName()+",<br/>"+sbForm.getMessage();
					final String adminmail=CMSConstants.MAIL_USERID;
					final String password = CMSConstants.MAIL_PASSWORD;
					final String port = CMSConstants.MAIL_PORT;
					final String host = CMSConstants.MAIL_HOST;
					Properties props = new Properties();
					props.put("mail.smtp.host", host);
					props.put("mail.smtp.socketFactory.port", port);
					props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.port", port);
			 
					Session session = Session.getInstance(props,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(adminmail,password);
							}
						});				
					mailBo.setAllotmentNo(sbForm.getSureOrChanceMemoNo());
					mailBo.setAppliedYear(Integer.parseInt(sbForm.getYear()));
					mailBo.setFromAddress(adminmail);
					mailBo.setFromName(prop.getProperty("knowledgepro.admission.studentmail.fromName"));
					mailBo.setIsSureMemo(sbForm.getIsSureMemo());
					mailBo.setMessage(msg);
					mailBo.setStudentId(student.getId());
					mailBo.setSubject(subject);
					mailBo.setToAddress(toAddress);
	
					
						 MimeMessage message1 = new MimeMessage(session);
							
							// Set from & to addresses
							InternetAddress from = new InternetAddress(adminmail,"Admission Status for the candidate[ "+collegeName+"]");
							
							InternetAddress toAssociate = new InternetAddress(toAddress);
							message1.setSubject(subject);
							message1.setFrom(from);
							message1.addRecipient(Message.RecipientType.TO, toAssociate);
						    MimeBodyPart mailBody = new MimeBodyPart();
						    mailBody.setText(msg, "US-ASCII", "html");
						    MimeMultipart mimeMultipart = new MimeMultipart();
						    
						    mimeMultipart.addBodyPart(mailBody);
						    message1.setContent(mimeMultipart);
						
						    Properties config = new Properties() {
								{
									put("mail.smtps.auth", "true");
									put("mail.smtp.host", host);
									put("mail.smtp.port", port);
									put("mail.smtp.starttls.enable", "true");
									put("mail.transport.protocol", "smtps");
								}
							}; 
							
							
							
						Session carrierSession = Session.getInstance(config, new Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(adminmail,password);
							}
						});
						
						
						
						Transport transport = carrierSession.getTransport("smtps");
						transport.connect(host,adminmail,password);
						transport.sendMessage(message1,message1.getRecipients(Message.RecipientType.TO));  //set
				        transport.close();
			 
						System.out.println("==========Done========");
					}
					
					catch (Exception e) {
						System.out.println(e.getMessage());
						
					}
				list.add(mailBo); 
				
			}// end of student iteration
            ISendAllotmentSmsTransaction txn = new SendAllotmentSmsTransactionImpl();
        if(list!=null && list.size()>0){
        	sent  = txn.saveMailList(list);
        	
        }
		}
		
		// TODO Auto-generated method stub
		return sent;
	}
	public List<Student> getStudentsForMail(SendAllotmentMemoSmsForm sbForm,Set<Integer> tempcourseset) throws ApplicationException {
		ISendAllotmentSmsTransaction txn = new SendAllotmentSmsTransactionImpl();
	    List<Student> studentList = new ArrayList<Student>(); 
		List<Integer> sentMemoList = txn.getSentMemoMailList(sbForm);
		  if(sbForm.getIsSureMemo()) {
			  studentList = txn.getSureStudentList(sbForm,sentMemoList,tempcourseset);
		  }else{
			  studentList = txn.getChanceStudentList(sbForm,sentMemoList,tempcourseset);
		  }
				
	
	return studentList;
}



}
