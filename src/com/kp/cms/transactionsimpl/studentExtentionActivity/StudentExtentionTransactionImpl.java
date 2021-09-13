package com.kp.cms.transactionsimpl.studentExtentionActivity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionActivityDetails;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class StudentExtentionTransactionImpl implements IStudentExtentionTransaction
{
	private static volatile StudentExtentionTransactionImpl obj;
	public static StudentExtentionTransactionImpl getInstance()
	{
		if(obj == null)
		{
			obj = new StudentExtentionTransactionImpl();
		}
		return obj;
	}
	
	public List<StudentExtention> getStudentExtentionDetails() throws Exception
	{
		Session session = null;
		
		try
		{
			session = HibernateUtil.getSession();
			Query query = session.createQuery(" from StudentExtention se where isActive = 1 order by se.activityName");
			List<StudentExtention> studentExtention = query.list();
			return studentExtention;
		}
		catch(Exception ex)
		{
			if(session != null)
			{
				session.flush();
			}
			throw new ApplicationException();
		}
	}
	
	@Override
	public StudentExtention isDuplicate(StudentExtention oldStudentExtention)
			throws Exception {
        Session session = null;
		
		try
		{
			session = HibernateUtil.getSession();
			Query query = session.createQuery(" from StudentExtention where activityName = :activityName or displayOrder = :displayOrder");
			query.setString("activityName", oldStudentExtention.getActivityName());
			query.setInteger("displayOrder", oldStudentExtention.getDisplayOrder());
			StudentExtention studentExtention = (StudentExtention)query.uniqueResult();
			return studentExtention;
		}
		catch (Exception e) 
		{
			throw new ApplicationException(e);
		}
}

	@Override
	public boolean addStudentExtention(StudentExtention studentExtention,
			String mode) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			if(mode.equalsIgnoreCase("Add"))
			{
				session.save(studentExtention);
			}
			else if(mode.equalsIgnoreCase("Edit"))
			{
				session.update(studentExtention);
			}
			tx.commit();
			session.flush();
			return true;
		}
		catch(ConstraintViolationException e) 
		{
			if(tx!=null)
				tx.rollback();
			throw new BusinessException(e);
		}
		catch(Exception e) 
		{
			if(tx!=null)
			     tx.rollback();
			throw new ApplicationException(e);
		}
	}

	@Override
	public boolean deleteStudentExtention(int dupId, boolean activate,
			StudentExtentionForm studentExtentionForm) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			StudentExtention studentform = (StudentExtention) session.get(StudentExtention.class, dupId);
			if(activate)
			{
				studentform.setIsActive(true);
			}
			else
			{
				studentform.setIsActive(false);
			}
			studentform.setModifiedBy(studentExtentionForm.getUserId());
			studentform.setLastModifiedDate(new Date());
			session.update(studentform);
			tx.commit();
			session.flush();
			return true;
		}
		catch(ConstraintViolationException e) 
		{
			if(tx!=null)
				tx.rollback();
			throw new BusinessException(e);
		}
		catch(Exception e) 
		{
			if(tx!=null)
			     tx.rollback();
			throw new ApplicationException(e);
		}
	}

	@Override
	public List<StudentGroup> getStudentGroupDetails() throws Exception {
		Session session = null;
		try{
			session = HibernateUtil .getSession();
			Query query =session.createQuery("from StudentGroup sg where isActive=1 order by sg.groupName ");
			List<StudentGroup> studentGroup = query.list();
			return studentGroup;
		}catch(Exception ex){
			if(session != null)
			{
			  session.flush();	
			}
		}
		throw new ApplicationException();
	}
	
	public boolean hasStudentAlreadySubmitedActivities(int studentId) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			String hql = " from StudentExtentionActivityDetails s where s.student.id = :studentId";
			Query query = session.createQuery(hql).setInteger("studentId", studentId);
			return !query.list().isEmpty();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException();
		}
	}

	public boolean saveStudentExtensionActivityDetails(List<StudentExtentionActivityDetails> activityDetails) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			int count = 0;
			Iterator<StudentExtentionActivityDetails> it = activityDetails.iterator();
			while(it.hasNext()) {
				StudentExtentionActivityDetails activityDetail = it.next();
				session.save(activityDetail);
				if(count++ % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			session.flush();
			session.close();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			if(transaction != null) {
				transaction.rollback();
			}
			if(session != null) {
				session.flush();
				session.close();
			}
			throw new ApplicationException();
		}
	}
	
}
