package com.kp.cms.transactionsimpl.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.Program;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.transactions.admin.ISendBulkSmsToStudentParents;
import com.kp.cms.transactions.admin.ISendBulkSmsToStudentParentsNew;
import com.kp.cms.transactionsimpl.ajax.CommonAjaxImpl;
import com.kp.cms.utilities.HibernateUtil;


public class SendBulkSmsToStudentParentsNewImpl implements ISendBulkSmsToStudentParentsNew{
	private static final Log log = LogFactory.getLog(CommonAjaxImpl.class);
	@Override
	public void sendBulkSms() {
		
		
	}
	public List<Student> getStudentByClass(int classIds) throws Exception {
		log.debug("Txn Impl : Entering getStudentByClass ");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			Query query = session.createQuery("from Student s where s.classSchemewise.classes.id=(:classesIds) and isAdmitted = 1 and s.isActive = 1  and s.admAppln.isCancelled=0 and (s.isHide =0 or s.isHide is null)");	
			query.setInteger("classesIds", classIds);
			
			 List<Student> list = query.list();
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getStudentByClass with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getStudentByClass with Exception");
			 throw e;
		 }
		}
	
	public List<Integer> getDetainedOrDiscontinuedStudents() throws Exception
	{
		List<Integer> studentList=null;
		Session session=null;
		try
		{
			session=HibernateUtil.getSession();
			/*	String query="select s.student.id from ExamStudentDetentionRejoinDetails s " +
							"where s.id=(select max(id) from ExamStudentDetentionRejoinDetails b " +
							"where b.student.id=s.student.id) and (s.detain=1 or s.discontinued=1)";
			*/
			String query="select s.student.id from ExamStudentDetentionRejoinDetails s " +
			"where (s.detain=1 or s.discontinued=1) and (s.rejoin = 0 or s.rejoin is null)";
			studentList=session.createQuery(query).list();
		}
		catch (Exception e) 
		{
			throw  new ApplicationException(e);
		}
		
		return studentList;
	}
	
	public List<Employee> getTeachersByDepartment(Set<Integer> deptIds) throws Exception {
		log.debug("Txn Impl : Entering getStudentByClass ");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			Query query = session.createQuery("from Employee e where e.department.id in (:deptIds) and e.isActive = 1 and e.active = 1  and e.teachingStaff=1");	
			query.setParameterList("deptIds", deptIds);
			
			 List<Employee> list = query.list();
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getTeachersByDepartment with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getTeachersByDepartment with Exception");
			 throw e;
		 }
		}
	
	public List<Employee> getNonTeachersByDepartment() throws Exception {
		log.debug("Txn Impl : Entering getStudentByClass ");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			Query query = session.createQuery("from Employee e where e.isActive = 1 and e.active = 1  and (e.teachingStaff=0 or e.teachingStaff=null)");	
			
			 List<Employee> list = query.list();
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getNonTeachersByDepartment with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getNonTeachersByDepartment with Exception");
			 throw e;
		 }
		}
	
	public List<Integer> getStudentIds() throws Exception 
	{
		Session session;
        Transaction transaction;
        List<Integer> studentIdList=new ArrayList<Integer>();
        session = null;
        transaction = null;
        try
        {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session = HibernateUtil.getSession();
            String hqlQuery="select studentId from MobileMessaging";
            Query query = session.createQuery(hqlQuery);
            studentIdList = query.list();
            transaction.commit();
        }
        catch(Exception e)
        {
            if(transaction != null)
            {
                transaction.rollback();
            }
           throw new ApplicationException(e);
        }
        finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
        log.info("End of of getAcademicYear of AcademicYearTransactionImpl");
        return studentIdList;
	}

}
