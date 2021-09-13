package com.kp.cms.transactionsimpl.fee;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.kp.cms.bo.admin.StudentSemesterFeeDetails;
import com.kp.cms.bo.exam.StudentUtilBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.fee.StudentSemesterFeeDetailsForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;
import com.kp.cms.transactions.fee.IStudentSemesterFeeDetailsTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class StudentSemesterFeeDetailsTransactionImpl implements IStudentSemesterFeeDetailsTransaction {
	
	private static volatile StudentSemesterFeeDetailsTransactionImpl studentSemesterFeeDetailsTransactionImpl = null;
	public static StudentSemesterFeeDetailsTransactionImpl getInstance(){
		if(studentSemesterFeeDetailsTransactionImpl == null){
			studentSemesterFeeDetailsTransactionImpl = new StudentSemesterFeeDetailsTransactionImpl();
			return studentSemesterFeeDetailsTransactionImpl;
		}
		return studentSemesterFeeDetailsTransactionImpl;
	}
	@Override
	public List<StudentSemesterFeeDetailsTo> getStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm)throws Exception {
		Session session = null;
		List<StudentSemesterFeeDetailsTo> list = new ArrayList<StudentSemesterFeeDetailsTo>();
		try{
			session = HibernateUtil.getSession();
			String s = "from Student s where s.classSchemewise.classes.id= :classId " +
					"and s.classSchemewise.curriculumSchemeDuration.semesterYearNo= :sem  order by s.registerNo";
			Query query = session.createQuery(s)
						.setString("classId", studentSemesterFeeDetailsForm.getClassId())
						.setString("sem", studentSemesterFeeDetailsForm.getSemester());
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public String getClassName(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		Session session = null;
		String name = null;
		try{
			session = HibernateUtil.getSession();
			String s = " select c.name from Classes c where c.id= :classId";
			Query query = session.createQuery(s)
							.setString("classId", studentSemesterFeeDetailsForm.getClassId());
			name = (String)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return name;
	}
	@Override
	public boolean save(List<StudentSemesterFeeDetails> bo) throws Exception {
		Session session = null;
		Transaction transaction = null;
		
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			java.util.Iterator<StudentSemesterFeeDetails> itr = bo.iterator();
			 while (itr.hasNext()) {
				 StudentSemesterFeeDetails studentSemesterFeeDetails = (StudentSemesterFeeDetails) itr.next();
				session.saveOrUpdate(studentSemesterFeeDetails);
		
			 }
			
			transaction.commit();
			session.flush();
			//session.close();
			return true;
			
		}
		 catch (Exception e){
			 if ( transaction != null){
				 transaction.rollback();
			 }
			 if( session != null ){
				 session.flush();
				 //session.close();
			 }
			 throw e;
		 }
		
	}
	@Override
	public List<StudentSemesterFeeDetails> getstudList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		Session session = null;
		List<StudentSemesterFeeDetails> list = new ArrayList<StudentSemesterFeeDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "from StudentSemesterFeeDetails s where s.classes.id= :classId " +
					" and s.semister= :sem  " +
					" and s.feeApprove = 0";
			Query query = session.createQuery(s)
							.setString("classId", studentSemesterFeeDetailsForm.getClassId())
							.setString("sem", studentSemesterFeeDetailsForm.getSemester());
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public List getApprovedStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		Session session = null;
		boolean approved = false;
		List<StudentSemesterFeeDetails> list = new ArrayList<StudentSemesterFeeDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "select s.student.id  from StudentSemesterFeeDetails s where s.classes.id= :classId " +
					" and s.semister= :sem  " +
					" and s.feeApprove = 1";
			Query query = session.createQuery(s)
							.setString("classId", studentSemesterFeeDetailsForm.getClassId())
							.setString("sem", studentSemesterFeeDetailsForm.getSemester());
			list = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public List<StudentSemesterFeeDetails> totalList(
			StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm)
			throws Exception {
		Session session = null;
		List<StudentSemesterFeeDetails> list = new ArrayList<StudentSemesterFeeDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "from StudentSemesterFeeDetails s where s.classes.id= :classId " +
					" and s.semister= :sem  " ;
					
			Query query = session.createQuery(s)
							.setString("classId",studentSemesterFeeDetailsForm.getClassId())
							.setString("sem", studentSemesterFeeDetailsForm.getSemester());
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public int getcId(
			StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm)
			throws Exception {
		Session session = null;
		int cId = 0;
		try{
			session = HibernateUtil.getSession();
			String s = "select c.course.id from Classes c where c.id= :classId";
			Query query = session.createQuery(s)
							.setString("classId", studentSemesterFeeDetailsForm.getClassId());
			cId = (Integer)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return cId;
	}
	@Override
	public List<StudentUtilBO> getPrevStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm)
			throws Exception {
		Session session = null;
		try{
			session = HibernateUtil.getSession();
			StringBuilder sb = new StringBuilder("select b.studentUtilBO from ExamStudentPreviousClassDetailsBO b where b.classId= :classId  order by b.studentUtilBO.registerNo "); 
			Query query = session.createQuery(sb.toString())
			             .setString("classId", studentSemesterFeeDetailsForm.getClassId());
			return query.list();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}
	
	
	

}
