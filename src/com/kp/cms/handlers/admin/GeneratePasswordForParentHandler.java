package com.kp.cms.handlers.admin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admin.GeneratePasswordParentForm;
import com.kp.cms.helpers.admin.GeneratePasswordForParentHelper;
import com.kp.cms.helpers.admin.GeneratePasswordHelper;
import com.kp.cms.transactions.admin.IGeneratePasswordTransaction;
import com.kp.cms.transactionsimpl.admin.GeneratePasswordTransactionImpl;
import com.kp.cms.utilities.SMSUtils;
import com.kp.cms.utilities.jms.SMS_Message;

public class GeneratePasswordForParentHandler {
	private static final Log log = LogFactory
	.getLog(GeneratePasswordForParentHandler.class);

public static volatile GeneratePasswordForParentHandler self = null;

public static GeneratePasswordForParentHandler getInstance() {
if (self == null) {
	self = new GeneratePasswordForParentHandler();
}
return self;
}

private GeneratePasswordForParentHandler() {

}

public boolean updateOnlyPassword(GeneratePasswordParentForm gnform) throws Exception {
	GeneratePasswordForParentHelper helper = GeneratePasswordForParentHelper.getInstance();
	boolean result = false;
	IGeneratePasswordTransaction txn = new GeneratePasswordTransactionImpl();
	int year = 0;
	int progid = 0;
	if (gnform.getYear() != null
			&& !StringUtils.isEmpty(gnform.getYear().trim())
			&& StringUtils.isNumeric(gnform.getYear())) {
		year = Integer.parseInt(gnform.getYear());
	}
	if (gnform.getProgramId() != null
			&& !StringUtils.isEmpty(gnform.getProgramId().trim())
			&& StringUtils.isNumeric(gnform.getProgramId())) {
		progid = Integer.parseInt(gnform.getProgramId());
	}
	List<Student> studentList = txn.getSearchedStudentsParent(progid, year);
	List<String> usernames = txn.getAllUserNamesPresentForParent();
	// get list and generate user name and password for each
	List<StudentLogin> studentlogins = helper.prepareStudentLogins(
			studentList, gnform, usernames);
	List<StudentLogin> parentLogins = helper.prepareParentCreditials(studentlogins,gnform,usernames);
	
	List<StudentLogin> parentListUpdation = new ArrayList<StudentLogin>();
	Iterator<StudentLogin> itr = studentlogins.iterator();
	while(itr.hasNext()){
		StudentLogin login = itr.next();
		if(parentLogins != null && !parentLogins.isEmpty()){
			Iterator<StudentLogin> itr1 = parentLogins.iterator();
			while (itr1.hasNext()) {
				StudentLogin studentLogin = new StudentLogin();
				studentLogin = itr1.next();
				//int recordId = txn.getRecordId(login.getStudent().getId());
				StudentLogin obj = txn.getObj(login.getStudent().getId());
				if((String.valueOf(login.getStudent().getId()).equalsIgnoreCase(String.valueOf(studentLogin.getStudent().getId())))){
				if(studentLogin.getIsParent() == true){
					obj.setIsParentGenerated(true);
					parentListUpdation.add(obj);
				}
			  }
				
			}
		}
		//studentlogins.add(login);
	}
	result = txn.saveCredentials(parentListUpdation);
	gnform .setParentSuccessList(parentLogins);
	return result;
}

public void sendSMS(GeneratePasswordParentForm gnform) throws Exception {
	IGeneratePasswordTransaction txn = new GeneratePasswordTransactionImpl();
	int year = 0;
	int progid = 0;
	if (gnform.getYear() != null
			&& !StringUtils.isEmpty(gnform.getYear().trim())
			&& StringUtils.isNumeric(gnform.getYear())) {
		year = Integer.parseInt(gnform.getYear());
	}
	if (gnform.getProgramId() != null
			&& !StringUtils.isEmpty(gnform.getProgramId().trim())
			&& StringUtils.isNumeric(gnform.getProgramId())) {
		progid = Integer.parseInt(gnform.getProgramId());
	}
	//List<StudentLogin> list = txn.getStudentsForSMS(progid, year);
	List<StudentLogin> list = gnform.getParentSuccessList();// this list is basically for that parents whose password are generated succesfully.
	initPasswordSMS(list);
	
  }

private void initPasswordSMS(List<StudentLogin> list)  throws Exception{
	Iterator<StudentLogin> stItr=list.iterator();
	while(stItr.hasNext()){	
		StudentLogin stu = stItr.next();
		String username = stu.getParentUsername();
		//String password = EncryptUtil.getInstance().decryptDES(stu.getPassword());
		String password = stu.getParentPassword();
		Properties prop=new Properties();
		String collegeName = CMSConstants.COLLEGE_NAME;
		String senderNumber=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NUMBER);
		String senderName=prop.getProperty(CMSConstants.KNOWLEDGEPRO_SENDER_NAME);
	
		if(stu.getStudent().getAdmAppln().getPersonalData()!=null){			
	
			String temp="";
			temp=temp+URLEncoder.encode("Please note your USERNAME & PWD for accessing Parent Portal \n","UTF-8");
			temp=temp+URLEncoder.encode("USERNAME: "+username+"\nPWD: "+password+"","UTF-8");		
	
			SMS_Message sms=new SMS_Message();
			if(stu.getStudent().getAdmAppln().getPersonalData().getParentMob2() != null && !stu.getStudent().getAdmAppln().getPersonalData().getParentMob2().isEmpty()){
			sms.setDestination_number(stu.getStudent().getAdmAppln().getPersonalData().getParentMob2());
			}
			//sms.setDestination_number("8249577986");
			sms.setMessage_body(temp);
			sms.setMessage_priority(String.valueOf(3));
			sms.setSender_name(senderName);
			sms.setSender_number(senderNumber);
			List<SMS_Message> smsList=new ArrayList<SMS_Message>();
			smsList.add(sms);
			SMSUtils smsUtils=new SMSUtils();
			List<SMS_Message> mobList=smsUtils.sendSMS(smsList); 				   
		}//sms check over	
		//break;
	}
	
	
}


}
