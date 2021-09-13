package com.kp.cms.transactionsimpl.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.PublishOptionalCourseSubjectApplication;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.transactions.admin.IPublishOptionalCourseSubjectApplication;
import com.kp.cms.utilities.HibernateUtil;

public class PublishOptionalCourseSubjectApplicationTransactionImpl implements IPublishOptionalCourseSubjectApplication
{
private static final Log log = LogFactory.getLog(PublishOptionalCourseSubjectApplicationTransactionImpl.class);
	
public List<PublishOptionalCourseSubjectApplication> getPublishOptionalCourseSubjects()
throws Exception
{
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			List<PublishOptionalCourseSubjectApplication> pocsList = session.createQuery("from PublishOptionalCourseSubjectApplication p where p.isActive=1").list();
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
	
	public Map<Integer, String> getClassMap(String[] classId) {
		Session session = null;
		Map<Integer,String> classMap= new HashMap<Integer, String>();
		try {
			 session = HibernateUtil.getSession();
			 
			 String id = "";
			 for(String cId: classId){
				 if(cId != null){
					 if(id.isEmpty()){
						 id = cId;
					 }else{
						 id = id+","+cId;
					 }
				 }
					 
			 }
			 Query query = session.createQuery(" from Classes c where id in ("+id+")" ); 		
			 List<Classes> list = query.list();
			 if(list!=null && !list.isEmpty()){
				 Iterator<Classes> itr = list.iterator();
				 while (itr.hasNext()) {
					 Classes classes = (Classes) itr.next();
					 classMap.put(classes.getId(), classes.getName());
				}
			 }
		}catch (Exception e) {
			session.flush();
			session.close();
		}
		return classMap;
	}
	
	
	public String isDuplicate(int id,int year, int classId) throws Exception {
		Session session = null;
		String className="";
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		try {
			String query="from PublishOptionalCourseSubjectApplication p where p.isActive=1 and p.classes.id="+classId;
			
			if(id!=0){
				query=query+" and p.id!="+id;
			}
			
			Query query1= session.createQuery(query);
			PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication=(PublishOptionalCourseSubjectApplication)query1.uniqueResult();
					if (publishOptionalCourseSubjectApplication!=null && id != publishOptionalCourseSubjectApplication.getId() ) {
						if(publishOptionalCourseSubjectApplication.getIsActive()==true){
					   className=publishOptionalCourseSubjectApplication.getClasses().getName();
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
	
	
	
	
	public String isDuplicateForUpdate(int id, int year, int classId) throws Exception {
		Session session = null;
		String className="";
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		try {
			String query="from PublishOptionalCourseSubjectApplication p where p.isActive=1 and p.classes.id="+classId;
			
			if(id!=0){
				query=query+" and p.id!="+id;
			}
			
			Query query1= session.createQuery(query);
			PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication=(PublishOptionalCourseSubjectApplication)query1.uniqueResult();
					if (publishOptionalCourseSubjectApplication!=null && id != publishOptionalCourseSubjectApplication.getId() ) {
						if(publishOptionalCourseSubjectApplication.getIsActive()==true){
					   className=publishOptionalCourseSubjectApplication.getClasses().getName();
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
	

	
	

	
	
	public int insert(Object obj) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int flag = 1;
		try {
			session.saveOrUpdate(obj);
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
	
	
	
	public int insertForUpdate(Object obj) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int flag = 1;
		try {
			session.saveOrUpdate(obj);
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
	
	
	// On - DELETE
	public void delete(int id) throws Exception {
		Session session = null;
		try{
		String HQL_QUERY = "delete from PublishOptionalCourseSubjectApplication p"
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
	
	
	

	
	public PublishOptionalCourseSubjectApplication getPublishOptionalCourseSubjectApplication(String id) throws Exception {
		Session session=null;
		PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication=null;
		try{
			session=HibernateUtil.getSession();
			String str="from PublishOptionalCourseSubjectApplication p where p.id="+id;
			Query query=session.createQuery(str);
			publishOptionalCourseSubjectApplication=(PublishOptionalCourseSubjectApplication)query.uniqueResult();
			
		}catch(Exception e){
			log.error("Error during getting TeacherDepartment by id..." , e);
			//session.flush();
			//session.close();
			
		}
		return publishOptionalCourseSubjectApplication;
		
	
	}


}
