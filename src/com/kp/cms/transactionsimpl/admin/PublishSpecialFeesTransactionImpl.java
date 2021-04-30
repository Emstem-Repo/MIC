package com.kp.cms.transactionsimpl.admin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admin.PublishSpecialFeesForm;
import com.kp.cms.transactions.admin.IPublishSpecialFeesTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class PublishSpecialFeesTransactionImpl implements IPublishSpecialFeesTransaction {
	private static final Log log = LogFactory.getLog(PublishStudentSemesterFeesTransactionImpl.class);

	@Override
	public List<PublishSpecialFees> getList(PublishSpecialFeesForm publishSpecialFeesForm) throws Exception {
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			List<PublishSpecialFees> pocsList = session.createQuery("from PublishSpecialFees p where p.isActive=1").list();
			//session.flush();
			//session.close();
			return pocsList;
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
			return null;
		}
	}

	@Override
	public String isDuplicate(int id, int year, int classID) throws Exception {
		Session session = null;
		String className="";
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		try {
			String query="from PublishSpecialFees p where p.isActive=1 and p.classes.id="+classID;
			
			if(id!=0){
				query=query+" and p.id!="+id;
			}
			
			Query query1= session.createQuery(query);
			PublishSpecialFees publishSpecialFees=(PublishSpecialFees)query1.uniqueResult();
					if (publishSpecialFees!=null && id != publishSpecialFees.getId() ) {
						if(publishSpecialFees.getIsActive()==true){
					   className=publishSpecialFees.getClasses().getName();
						}
					   }
			
			return className;
			
			
			
		}
		catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}

	@Override
	public int insert(PublishSpecialFees publishSpecialFees) throws Exception {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int flag = 1;
		try {
			session.saveOrUpdate(publishSpecialFees);
			tx.commit();
			// session.flush();
			session.close();
		} catch (Exception e) {
			flag = 0;
			log.error(e.getMessage());
			if (tx != null) {
				tx.rollback();
			}
			if (session != null) {
				// session.flush();
				session.close();
			}
		}
		return flag;
	}

	@Override
	public void delete(int id) throws Exception {
		Session session = null;
		try{
		String HQL_QUERY = "delete from PublishSpecialFees p"
				+ " where p.id = :id ";

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery(HQL_QUERY);
		query.setParameter("id", id);
		query.executeUpdate();
		tx.commit();
		}catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		
	}

	@Override
	public String isDuplicateForUpdate(int id, int year, int classID)
			throws Exception {
		Session session = null;
		String className="";
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		try {
			String query="from PublishSpecialFees p where p.isActive=1 and p.classes.id="+classID;
			
			if(id!=0){
				query=query+" and p.id!="+id;
			}
			
			Query query1= session.createQuery(query);
			PublishSpecialFees publishStudentSemesterFees=(PublishSpecialFees)query1.uniqueResult();
					if (publishStudentSemesterFees!=null && id != publishStudentSemesterFees.getId() ) {
						if(publishStudentSemesterFees.getIsActive()==true){
					   className=publishStudentSemesterFees.getClasses().getName();
						}
					   }
			
			return className;
			
			
			
		}
		catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		
	}

	@Override
	public int insertForUpdate(PublishSpecialFees publishStudentSemesterFees)
			throws Exception {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int flag = 1;
		try {
			session.saveOrUpdate(publishStudentSemesterFees);
			tx.commit();
		//session.flush();
		//session.close();
		} catch (Exception e) {
			flag = 0;
			log.error(e.getMessage());
			if (tx != null) {
				tx.rollback();
			}
			
		}
		 finally {
				if (session != null) {
					session.flush();
					session.close();
				}}
		return flag;
		
	}

	@Override
	public PublishSpecialFees getPublishSpecialFees(String id) throws Exception {
		Session session=null;
		PublishSpecialFees publishSpecialFees=null;
		try{
			session=HibernateUtil.getSession();
			String str="from PublishSpecialFees p where p.id="+id;
			Query query=session.createQuery(str);
			publishSpecialFees=(PublishSpecialFees)query.uniqueResult();
			
		}catch(Exception e){
			log.error("Error during getting TeacherDepartment by id..." , e);
			//session.flush();
			//session.close();
			
		}
		return publishSpecialFees;
	}
}
