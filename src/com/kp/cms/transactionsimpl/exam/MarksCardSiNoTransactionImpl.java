package com.kp.cms.transactionsimpl.exam;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.forms.exam.MarksCardSiNoForm;
import com.kp.cms.transactions.exam.IMarksCardSiNoTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class MarksCardSiNoTransactionImpl implements IMarksCardSiNoTransaction {
	
	
	public boolean save(MarksCardSiNo bo)throws Exception{
		Session session = null;
		Transaction tx=null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(bo);
			tx.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
			return false;
		}
	}
	
	public List<MarksCardSiNo> getData()throws Exception{
		MarksCardSiNo bo = null;
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			List<MarksCardSiNo> l =session.createQuery("from MarksCardSiNo bo where bo.isActive=1").list();
			
				return l;
			
//			session.flush();
//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return null;
	}
	
	public boolean getDataAvailable(MarksCardSiNoForm cardSiNoForm)throws Exception{
		MarksCardSiNo bo = null;
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			bo = (MarksCardSiNo)session.createQuery("from MarksCardSiNo bo where bo.isActive=1 and bo.academicYear="+cardSiNoForm.getAcademicYear()+" and bo.courseId.id="+cardSiNoForm.getCourseId()+" and bo.semister="+cardSiNoForm.getSemister()).uniqueResult();
			if(bo!=null){
				return true;
			}
//			session.flush();
//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return false;
	}
	
}
