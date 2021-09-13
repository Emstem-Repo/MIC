package com.kp.cms.transactionsimpl.usermanagement;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.Caste;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.District;
//import com.kp.cms.bo.admin.OptionCourse;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.usermanagement.OptionalCourseApplication;
import com.kp.cms.forms.usermanagement.OptionalCourseApplicationForm;
import com.kp.cms.handlers.usermanagement.OptionalCourseApplicationHandler;
import com.kp.cms.to.usermanagement.OptionalCourseApplicationTO;
import com.kp.cms.transactions.usermanagement.IOptionalCourseSubjectApplication;
import com.kp.cms.utilities.HibernateUtil;

public class OptionalCourseApplicationTransactionImpl implements IOptionalCourseSubjectApplication{
	private static volatile OptionalCourseApplicationTransactionImpl impl = null;
   
	public static OptionalCourseApplicationTransactionImpl getInstance() {
		if (impl == null) {
			impl = new OptionalCourseApplicationTransactionImpl();
		}
		return impl;
	}
	

	public boolean saveApplication(List<OptionalCourseApplication> boList)throws Exception{Session session = null;
	Transaction transaction = null;
	session = HibernateUtil.getSession();
	transaction = session.beginTransaction();
	transaction.begin();
	
	try {
		
	Iterator itr = boList.iterator();
	while(itr.hasNext()){
		OptionalCourseApplication bo = (OptionalCourseApplication) itr.next();
		
		session.save(bo);

	}
	
	transaction.commit();
	
	return true;
	
	} catch (Exception e) {
		if ( transaction != null){
			transaction.rollback();
		}
		if (session != null){
			
			session.close();
		}
		return false;
	}finally{
		
		session.close();
	}
	
	}
	
	public Department getDepartment(int courseId)throws Exception {
    Session session = null;
    Department department=null;
    List<Department> list=null;
    
    try {
    	
	   session = HibernateUtil.getSession();
	   list =session.createQuery("select c.department from CourseToDepartment c where c.course.id="+courseId+" ").list();
	   department = list.get(0);
	   session.flush();
	   return department;
    } catch (Exception e) {
	if (session != null){
		session.flush();
		//session.close();
	}
	return department;
}

}
	
	public Student getStudentDetails(int stuId)throws Exception {
	    Session session = null;
	    Student student=null;
	    try {
	    	
		   session = HibernateUtil.getSession();
		   student = (Student) session.createQuery("from Student s where s.id="+stuId+" ").uniqueResult();
		   session.flush();
		   return student;
	    } catch (Exception e) {
		if (session != null){
			session.flush();
			//session.close();
		}
		return student;
	}
	
}

public boolean checkDuplicates(int stuId, int classId)throws Exception{
		
		 Session session = null;
		 boolean isExists= false;
		 List<OptionalCourseApplication> boList;
		    try {
		    	
			   session = HibernateUtil.getSession();
			   boList =  session.createQuery("from OptionalCourseApplication o where o.student.id="+stuId+" and o.classes.id="+classId).list();
			   session.flush();
			   if(boList.size()>0 && boList!=null)
				   isExists=true;
			   return isExists;
		    } catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
			return isExists;
		}
		
	}
@Override
public List<OptionalCourseApplication> getExistingOptionalCourseSubjects(
		int stuId, int classId,int courseId) throws Exception {



	Session session = null;
	List<OptionalCourseApplication> boList = null;
	try {
	session = HibernateUtil.getSession();
	boList = session.createQuery("from OptionalCourseApplication o where o.student.id="+stuId+" and o.classes.id="+classId+"  order by o.courseOption)" ).list();
	session.flush();
	return boList;
	} catch (Exception e) {
	if (session != null){
		session.flush();
		//session.close();
	}
	return null;
	}

}
@Override
public List<Subject> getOptionalCourseSubjects(
		OptionalCourseApplicationForm form) throws Exception {

	Session session = null;
	List<Subject> boList = null;
	try {
		session = HibernateUtil.getSession();
	  String s= "select s.department.name, s.name,s.department.id,s.id from Subject s where s.isCourseOptionalSubject=1 and s.department.id !="+Integer.parseInt(form.getDepartmentId());
	  //Basim HardCoded for BA English Department not to show BA VocEnglish Department optional subject and vice versa.
	  if(form.getDepartmentId().equalsIgnoreCase("1")){
			s+=" and s.department.id!=31";
		} 	
	  /*if(form.getDepartmentId().equalsIgnoreCase("31")){
			s+=" and s.department.id!=1";
		} */
	    boList = session.createQuery(s).list();
		session.flush();
		return boList;
	} catch (Exception e) {
		if (session != null){
			session.flush();
			//session.close();
		}
		return null;
	}

}
	
}