package com.kp.cms.transactionsimpl.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.EligibilityCriteria;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.FalseNumSiNo;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.exam.ConsolidatedMarksCardForm;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
//import com.kp.cms.transactions.exam.IMarksCardTransaction;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class ExamFalseNumberTransactionImpl implements IExamFalseNumberTransaction{
	
	private static volatile ExamFalseNumberTransactionImpl obj;
	
	public static ExamFalseNumberTransactionImpl getInstance() {
		if(obj == null) {
			obj = new ExamFalseNumberTransactionImpl();
		}
		return obj;
	}
	@Override
	public List<Student> getQueryForCurrentClass(NewExamMarksEntryForm marksCardForm)
			throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query=("select s from Student s inner join s.classSchemewise csc join csc.curriculumSchemeDuration csd join csc.classes cls "+
                      " where s.admAppln.isCancelled=0  and (s.isHide=0 or s.isHide is null) "+
                      " and s.classSchemewise.curriculumSchemeDuration.academicYear=(select e.academicYear from ExamDefinitionBO e where e.id="+marksCardForm.getExamName()+")"+ 
                      " and s.admAppln.courseBySelectedCourseId.id=" +marksCardForm.getCourseId()+" and s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+marksCardForm.getSchemeNo()+ 
                      " and s.classSchemewise.classes.id="+marksCardForm.getClassId()+" and s.id not in(select b.student.id from ExamStudentDetentionRejoinDetails b where b.discontinued=1 or b.detain=1)") ;
				
			Query query1=session.createQuery(query);
		List<Student> list=query1.list();
		return list;
	}



	@Override
	public void getcourseansScheme(NewExamMarksEntryForm marksCardForm)
			throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query="select c.course.id,c.termNumber from Classes c where c.id="+marksCardForm.getClassId()+" and c.isActive=1";
		Query query1=session.createQuery(query);
		List list=query1.list();
		Iterator<Object> itr=list.iterator();
		while(itr.hasNext()){
			Object[] obj=(Object[]) itr.next();
			if(obj[0]!=null && obj[1]!=null){
				marksCardForm.setCourseId(obj[0].toString());
				marksCardForm.setSchemeNo(obj[1].toString());
			}
		}
	}

	@Override
	public boolean savedata(List<ExamFalseNumberGen> bo) throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		try{
		
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		txn.begin();
		for (ExamFalseNumberGen examFalseNumberGen : bo) {
			session.save(examFalseNumberGen);	
		}
		
		txn.commit();
		session.flush();
		return true;
		}
		catch (Exception exception) {
			if (txn != null) {
				txn.rollback();
				
			}
			return false;

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
	}

	@Override
	public List<ExamFalseNumberGen> getfalsenos(
			NewExamMarksEntryForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		List<ExamFalseNumberGen> list=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query="from ExamFalseNumberGen e where e.classId.termNumber="+marksCardForm.getSchemeNo()+" and e.course.id="+marksCardForm.getCourseId()+
				" and e.examId.id="+marksCardForm.getExamId()/*+" and e.subject.id="+marksCardForm.getSubjectId()*/;
		Query query1=session.createQuery(query);
		list=query1.list();
		
		return list;
	}
	public boolean updateFalseSiNo(NewExamMarksEntryForm form) throws Exception{

		Session session = null;
		try {
			 SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session =sessionFactory.openSession();
			 Transaction tran = session.beginTransaction();
			 String query="";
				 query="select bo from FalseNumSiNo bo inner join bo.courseId.classes cls inner join cls.classSchemewises sch  where bo.isActive=1 and bo.courseId.id ="+form.getCourseId()+" and sch.curriculumSchemeDuration.academicYear = bo.academicYear and cls.termNumber = bo.semister and bo.examId.id="+form.getExamId();
			 Query qr = session.createQuery(query);
			 
			 FalseNumSiNo obj=(FalseNumSiNo)qr.uniqueResult();
			 
			 if(obj!=null){
				 obj.setCurrentNo(form.getTotalCount()+"");
				 session.merge(obj);
				 tran.commit();
				 return true;
			 }
		 }catch (Exception e) {
			 throw e; 
		 }finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return false;
	
	}
	public String getMaxFalseNo(NewExamMarksEntryForm marksCardForm) throws Exception{

		String currentNo = null;
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			currentNo = (String)session.createQuery("select bo.currentNo from FalseNumSiNo bo where bo.isActive=1 and bo.academicYear="+Integer.parseInt(marksCardForm.getYear())+" and bo.courseId.id="+Integer.parseInt(marksCardForm.getCourseId())+" and bo.semister="+Integer.parseInt(marksCardForm.getSchemeNo())+" and bo.examId.id="+Integer.parseInt(marksCardForm.getExamId())).uniqueResult();
			
//			session.flush();
//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return currentNo;
	
	}
	
	public boolean DuplicateFalseNo(NewExamMarksEntryForm marksCardForm,String randomNo) throws Exception{

		String currentNo = null;
		Session session = null;
		boolean flag=false;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			currentNo = (String)session.createQuery("select bo.currentNo from FalseNumSiNo bo where bo.isActive=1 and bo.currentNo="+Integer.parseInt(randomNo)).uniqueResult();
			if(currentNo!=null){
				flag=true;
			}
//			session.flush();
//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return flag;
	
	}
	@Override
	public Object getData(NewExamMarksEntryForm marksCardForm, String quer)
			throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		session=(Session)HibernateUtil.getSession();
		Query query1=session.createQuery(quer);
		Object obj=query1.uniqueResult();
		return obj;
	}

}
