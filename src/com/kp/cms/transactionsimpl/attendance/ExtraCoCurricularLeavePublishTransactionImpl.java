package com.kp.cms.transactionsimpl.attendance;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.ExtraCoCurricularLeavePublishBO;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;
import com.kp.cms.transactions.attandance.IExtraCoCurricularLeavePublishTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;

public class ExtraCoCurricularLeavePublishTransactionImpl implements IExtraCoCurricularLeavePublishTransaction {
	private static volatile ExtraCoCurricularLeavePublishTransactionImpl extraCoCurricularLeavePublishTransactionImpl = null;
	private static final Log log = LogFactory.getLog(ExtraCoCurricularLeavePublishTransactionImpl.class);
	
	public static ExtraCoCurricularLeavePublishTransactionImpl getInstance()
	{
		if(extraCoCurricularLeavePublishTransactionImpl==null)
		{
			extraCoCurricularLeavePublishTransactionImpl = new ExtraCoCurricularLeavePublishTransactionImpl();
		}
		return extraCoCurricularLeavePublishTransactionImpl;
	}

	@Override
	public boolean save(List<ExtraCoCurricularLeavePublishBO> exBoList) throws Exception {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction txn = session.beginTransaction();
		boolean flag = false;
		try{
			if(exBoList != null && !exBoList.isEmpty()){
				Iterator<ExtraCoCurricularLeavePublishBO> itr = exBoList.iterator();
				while(itr.hasNext()){
					ExtraCoCurricularLeavePublishBO exBo = (ExtraCoCurricularLeavePublishBO)itr.next();
					session.save(exBo);
				}
			}
			txn.commit();
			session.flush();
			flag = true;
		}catch (Exception e) {
			if ( txn != null){
				txn.rollback();
			 }
			 if( session != null ){
				 session.flush();
				 //session.close();
			 }
			 throw e;
		}
		return flag;
	}

	@Override
	public ExtraCoCurricularLeavePublishBO getRecord(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm)
			throws Exception {
		Session session = null;
		ExtraCoCurricularLeavePublishBO bo = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from ExtraCoCurricularLeavePublishBO bo where bo.classes.id= :classId ";
			Query query = session.createQuery(s)
						.setString("classId", extraCoCurricularLeavePublishForm.getClassId());
			bo = (ExtraCoCurricularLeavePublishBO) query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);	
		}
		return bo;
	}

	@Override
	public boolean update(ExtraCoCurricularLeavePublishBO extraCoCurricularLeavePublishBO) throws Exception {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction txn = session.beginTransaction();
		boolean flag = false;
		try{
			session.update(extraCoCurricularLeavePublishBO);
			txn.commit();
			session.flush();
			flag = true;
		}catch (Exception e) {
			if ( txn != null){
				txn.rollback();
			 }
			 if( session != null ){
				 session.flush();
				 //session.close();
			 }
			 throw e;
		}
		return flag;
	}

	@Override
	public boolean isDuplicate(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {

		Session session = null;
		boolean isDuplicate = false;
		try{
			String[] clsId = extraCoCurricularLeavePublishForm.getClassListId();
			session = HibernateUtil.getSession();
			int i =0;
			for(i=0;i<clsId.length;i++){
				int classId = Integer.parseInt(clsId[i]);
				String str = "from ExtraCoCurricularLeavePublishBO bo where bo.classes.id= :classId  and bo.isActive = 1 ";
				Query query =session.createQuery(str)
							.setInteger("classId", classId);
				ExtraCoCurricularLeavePublishBO connection = (ExtraCoCurricularLeavePublishBO) query.uniqueResult();
				if(connection!=null && !connection.toString().isEmpty()){
					if(connection.getId() == extraCoCurricularLeavePublishForm.getId()){
						isDuplicate = false;
					}else{
						isDuplicate = true;
						
					}
				}
			}
		}catch (Exception e) {
			isDuplicate = false;
			throw new ApplicationException(e);
		}
		
		return isDuplicate;
	
		
	}

	@Override
	public ExtraCoCurricularLeavePublishBO getForEditDetails(int id)throws Exception {
		Session session = null;
		ExtraCoCurricularLeavePublishBO connection;
		try{
			session = HibernateUtil.getSession();
			String str = "from ExtraCoCurricularLeavePublishBO conn where conn.id="+id+" and conn.isActive = 1";
			Query query = session.createQuery(str);
			connection = (ExtraCoCurricularLeavePublishBO) query.uniqueResult();
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
		}
	}
		return connection;
		
	}

	@Override
	public boolean deleteOpenConnection(ExtraCoCurricularLeavePublishForm publishForm1) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isDeleted = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			ExtraCoCurricularLeavePublishBO bo = (ExtraCoCurricularLeavePublishBO)session.get(ExtraCoCurricularLeavePublishBO.class, publishForm1.getId());
			bo.setModifyDate(new Date());
			bo.setModifiedBy(publishForm1.getUserId());
			bo.setIsActive(false);
			session.update(bo);
			tx.commit();
			isDeleted = true;
		}catch (Exception e) {
			isDeleted = false;
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
				}
			}
		return isDeleted;
	}

	@Override
	public List<ExtraCoCurricularLeavePublishBO> getList(int year) throws Exception {
		Session session = null;
		List<ExtraCoCurricularLeavePublishBO> publishBOs = null;
		try{
			session = HibernateUtil.getSession();
			String str ="select conn  from ExtraCoCurricularLeavePublishBO conn join conn.classes cl   join cl.classSchemewises cls " +
					" join cls.curriculumSchemeDuration cud where conn.isActive=1 and cl.isActive=1 and cud.academicYear="+year;
			Query query = session.createQuery(str);
			publishBOs = query.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
		return publishBOs;
		
	}

	@Override
	public int getRecordId(String classId) throws Exception {
		Session session = null;
		int recordId = 0;
		try{
			session = HibernateUtil.getSession();
			String s = "select sef.id from ExtraCoCurricularLeavePublishBO sef where sef.classes.id= :classId";
			Query query = session.createQuery(s)
						.setString("classId", classId);
			recordId = (Integer)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return recordId;
	}

	@Override
	public boolean updateOpenConnection(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm)
			throws Exception {
		Session session = null;
		ExtraCoCurricularLeavePublishBO connection = null;
		Transaction tx = null;
		boolean isUpdated = false;
		try{
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			connection = (ExtraCoCurricularLeavePublishBO)session.get(ExtraCoCurricularLeavePublishBO.class, extraCoCurricularLeavePublishForm.getId());
			Classes classes = new Classes();
			String[] clsId = extraCoCurricularLeavePublishForm.getClassListId();
			classes.setId(Integer.parseInt(clsId[0]));
			connection.setClasses(classes);
			connection.setPublishStartDate(CommonUtil.ConvertStringToDate(extraCoCurricularLeavePublishForm.getPublishStartDate()));
			connection.setPublishEndDate(CommonUtil.ConvertStringToDate(extraCoCurricularLeavePublishForm.getPublishEndDate()));
			connection.setModifyDate(new Date());
			connection.setModifiedBy(extraCoCurricularLeavePublishForm.getUserId());
			connection.setIsActive(true);
			session.update(connection);
			tx.commit();
			isUpdated = true;
			
			
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
				}
			}
		return isUpdated;
	}
	
	
}
