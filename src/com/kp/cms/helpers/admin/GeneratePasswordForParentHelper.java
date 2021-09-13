package com.kp.cms.helpers.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Roles;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admin.GeneratePasswordParentForm;
import com.kp.cms.transactions.admin.StudentLoginTO;
import com.kp.cms.utilities.EncryptUtil;
import com.kp.cms.utilities.PasswordGenerator;

public class GeneratePasswordForParentHelper {
private static final Log log = LogFactory.getLog(GeneratePasswordForParentHelper.class);
	
	public static volatile GeneratePasswordForParentHelper self=null;
	public static  GeneratePasswordForParentHelper getInstance(){
		
			if(self==null)
					self= new GeneratePasswordForParentHelper();
			return self;
		}
		
	private GeneratePasswordForParentHelper(){
		
	}

	public List<StudentLogin> prepareStudentLogins(List<Student> studentList,
			GeneratePasswordParentForm gnform, List<String> usernames) throws Exception {

		List<StudentLogin> studentlogins= new ArrayList<StudentLogin>();
		if(usernames==null)
			usernames=new ArrayList<String>();
		List<StudentLoginTO> studentloginTos= new ArrayList<StudentLoginTO>();
		List<Student> rejectList= new ArrayList<Student>();
		if(studentList!=null){
			Iterator<Student> stItr=studentList.iterator();
			while (stItr.hasNext()) {
				Student student = (Student) stItr.next();
				//if it is mailid to be username
				 if(gnform.getStudentRollNo()  && student.getRollNo()!=null && !StringUtils.isEmpty(student.getRollNo().trim())){
					StudentLogin login= new StudentLogin();
					StudentLoginTO loginTo= new StudentLoginTO();
					login.setStudent(student);
					login.setUserName(student.getRollNo());
					
					//student role
					Roles roles= new Roles();
					roles.setId(CMSConstants.STUDENT_ROLE_ID);
					login.setRoles(roles);
					login.setIsActive(true);
					login.setIsStudent(true);
					login.setIsParent(true);
					login.setCreatedBy(gnform.getUserId());
					login.setCreatedDate(new Date());
					if(usernames != null && ! usernames.isEmpty()){
					if(!usernames.contains(login.getUserName())){
					studentlogins.add(login);
					}else {
						rejectList.add(student);
					}
				}else {
					studentlogins.add(login);
				}
					
					//if reg.no
				}else if(gnform.getStudentResgisterNo() && student.getRegisterNo()!=null && !StringUtils.isEmpty(student.getRegisterNo().trim())){
					StudentLogin login= new StudentLogin();
					StudentLoginTO loginTo= new StudentLoginTO();
					login.setStudent(student);
					login.setUserName(student.getRegisterNo());
					
					//student role
					Roles roles= new Roles();
					roles.setId(CMSConstants.STUDENT_ROLE_ID);
					login.setRoles(roles);
					login.setIsActive(true);
					login.setIsStudent(true);
					login.setIsParent(true);
					login.setCreatedBy(gnform.getUserId());
					login.setCreatedDate(new Date());
					if(usernames != null && ! usernames.isEmpty()){
						if(!usernames.contains(login.getUserName())){
						studentlogins.add(login);
						}else {
							rejectList.add(student);
						}
					}else {
						studentlogins.add(login);
					}
				
			}
		}
		}
		
		gnform.setRejectList(rejectList);
		log.info("Exit prepareStudentLogins");
		return studentlogins;
		}
		
	
	private void setParentLoginDetails(Student student,List<StudentLogin> studentlogins, StudentLoginTO loginTo,StudentLogin login,
			GeneratePasswordParentForm gnform,
			List<StudentLoginTO> studentloginTos, List<String> usernames,
			List<Student> rejectList) throws Exception {
		StudentLogin parentlogin=null;
		String parentPass="";
		if(student.getAdmAppln().getPersonalData().getFatherEmail()!=null && !StringUtils.isEmpty(student.getAdmAppln().getPersonalData().getFatherEmail().trim())){
		// if parent userid,password diff
			if(!gnform.getSameUseridPassword()){
			
				parentlogin=new StudentLogin();
				parentlogin.setStudent(student);
				parentlogin.setUserName(student.getAdmAppln().getPersonalData().getFatherEmail());
				//student role
				Roles parentRole= new Roles();
				parentRole.setId(CMSConstants.STUDENT_ROLE_ID);
				parentlogin.setRoles(parentRole);
				parentlogin.setIsActive(true);
				parentlogin.setIsStudent(false);
				parentlogin.setCreatedBy(gnform.getUserId());
				parentlogin.setCreatedDate(new Date());
				if(!usernames.contains(parentlogin.getUserName())){
					usernames.add(parentlogin.getUserName());
					studentlogins.add(parentlogin);
				}else{
					rejectList.add(student);
				}
				
			}
		}
		
	}

	public List<StudentLogin> prepareParentCreditials(
			List<StudentLogin> studentlogins,
			GeneratePasswordParentForm gnform, List<String> usernames) throws Exception {

		List<StudentLogin> parentLogins = new ArrayList<StudentLogin>();
		if(usernames == null){
			usernames = new ArrayList<String>();
		}
		List<StudentLogin> parentRejectList = new ArrayList<StudentLogin>();
		if(studentlogins != null){
			Iterator<StudentLogin> itr = studentlogins.iterator();
			while(itr.hasNext()){
				StudentLogin student = (StudentLogin)itr.next();
				if(gnform.getStudentRollNo() && student.getStudent().getRollNo()!=null && !StringUtils.isEmpty(student.getStudent().getRollNo().trim())){
					StudentLogin parentlogin = new StudentLogin();
					parentlogin.setStudent(student.getStudent());
					parentlogin.setParentUsername(student.getStudent().getRollNo());
					parentlogin.setParentPassword(student.getStudent().getRollNo());
					parentlogin.setIsParent(true);
					parentLogins.add(parentlogin);
					
					
				}else if(gnform.getStudentResgisterNo() && student.getStudent().getRegisterNo()!=null && !StringUtils.isEmpty(student.getStudent().getRegisterNo().trim())){
					StudentLogin parentlogin = new StudentLogin();
					parentlogin.setStudent(student.getStudent());
					parentlogin.setParentUsername(student.getStudent().getRegisterNo());
					parentlogin.setParentPassword(student.getStudent().getRegisterNo());
					parentlogin.setIsParent(true);
					parentLogins.add(parentlogin);
				}else {
					parentRejectList.add(student);
				}
			}
		}
		
		return parentLogins;
	
		
	}
	
	
	}
