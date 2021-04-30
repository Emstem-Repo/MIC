package com.kp.cms.handlers.admin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admin.ReGeneratePasswordForm;
import com.kp.cms.helpers.admin.ReGeneratePasswordHelper;
import com.kp.cms.transactions.admin.IReGeneratePasswordTransaction;
import com.kp.cms.transactionsimpl.admin.ReGeneratePasswordTransactionImpl;
import com.kp.cms.utilities.EncryptUtil;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.SMS_Message;

public class ReGeneratePasswordHandler {
	/**
	 * Singleton object of ReGeneratePasswordHandler
	 */
	private static volatile ReGeneratePasswordHandler reGeneratePasswordHandler = null;
	private static final Log log = LogFactory.getLog(ReGeneratePasswordHandler.class);
	private ReGeneratePasswordHandler() {
		
	}
	/**
	 * return singleton object of ReGeneratePasswordHandler.
	 * @return
	 */
	public static ReGeneratePasswordHandler getInstance() {
		if (reGeneratePasswordHandler == null) {
			reGeneratePasswordHandler = new ReGeneratePasswordHandler();
		}
		return reGeneratePasswordHandler;
	}
	/**
	 * @param registerNoList
	 * @return
	 * @throws Exception
	 */
	public List<StudentLogin> getStudentLoginsByRegisterNo(
			ArrayList<String> registerNoList)  throws Exception{
		IReGeneratePasswordTransaction transaction=new ReGeneratePasswordTransactionImpl();
		return transaction.getStudentLogins(registerNoList);
	}
	/**
	 * @param registerNoList
	 * @return
	 * @throws Exception
	 */
	public List<String> getStudentsByRegisterNo(ArrayList<String> registerNoList) throws Exception {
		IReGeneratePasswordTransaction transaction=new ReGeneratePasswordTransactionImpl();
		return transaction.getStudentsByRegisterNo(registerNoList);
	}
	/**
	 * @param studentLogins
	 * @param reGeneratePasswordForm
	 * @throws Exception
	 */
	public boolean updatePassword(List<StudentLogin> studentLogins,ReGeneratePasswordForm reGeneratePasswordForm) throws Exception {
		List<StudentLogin> finalStudentLogins=ReGeneratePasswordHelper.getInstance().getFinalStudentLogins(studentLogins,reGeneratePasswordForm);
		IReGeneratePasswordTransaction transaction=new ReGeneratePasswordTransactionImpl();
		return transaction.updateStudentLogin(finalStudentLogins);
	}
	public void sendSMS(List<StudentLogin> studentLogins,ReGeneratePasswordForm reGeneratePasswordForm)throws Exception {
		List<StudentLogin> finalStudentLogins=ReGeneratePasswordHelper.getInstance().getFinalStudentLogins(studentLogins,reGeneratePasswordForm);
		sendPassword(finalStudentLogins,reGeneratePasswordForm.getSendSMS());
	}
	private void sendPassword(List<StudentLogin> finalStudentLogins,String sendSMS)throws Exception{
		Iterator<StudentLogin> stItr=finalStudentLogins.iterator();
		while(stItr.hasNext()){	
			StudentLogin stu = stItr.next();
			String username = stu.getUserName();
			String password = EncryptUtil.getInstance().decryptDES(stu.getPassword());
			Properties prop=new Properties();
			String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
			String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
		
			if(stu.getStudent().getAdmAppln().getPersonalData()!=null){			
		
				String temp="";
				temp=temp+URLEncoder.encode("Please note your USERNAME & PWD for accessing Student Portal \n","UTF-8");
				temp=temp+URLEncoder.encode("USERNAME: "+username+"\nPWD: "+password+"","UTF-8");
				temp=temp+URLEncoder.encode("from Mar Ivanios College","UTF-8");
		
				SMS_Message sms=new SMS_Message();
				if(sendSMS.equalsIgnoreCase("student")){
					if(stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2()!=null && 
							!stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2().isEmpty()){
						sms.setDestination_number(stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2());
					}
				}else if (sendSMS.equalsIgnoreCase("parent")) {
					if(stu.getStudent().getAdmAppln().getPersonalData().getParentMob2() != null &&
							!stu.getStudent().getAdmAppln().getPersonalData().getParentMob2().isEmpty()){
						sms.setDestination_number(stu.getStudent().getAdmAppln().getPersonalData().getParentMob2());
					}
				}else if(sendSMS.equalsIgnoreCase("both")){
					if(stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2()!=null && 
							!stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2().isEmpty() && 
							stu.getStudent().getAdmAppln().getPersonalData().getParentMob2()!=null &&
							!stu.getStudent().getAdmAppln().getPersonalData().getParentMob2().isEmpty()){
						sms.setDestination_number(stu.getStudent().getAdmAppln().getPersonalData().getMobileNo2()+","+
								stu.getStudent().getAdmAppln().getPersonalData().getParentMob2());
					}
				}
				sms.setMessage_body(temp);
				sms.setMessage_priority(String.valueOf(3));
				sms.setSender_name(senderName);
				sms.setSender_number(senderNumber);
				List<SMS_Message> smsList=new ArrayList<SMS_Message>();
				smsList.add(sms);
				SMSUtils smsUtils=new SMSUtils();
				List<SMS_Message> mobList=smsUtils.sendSMS(smsList); 				   
			}	
		}
		
	}
}
