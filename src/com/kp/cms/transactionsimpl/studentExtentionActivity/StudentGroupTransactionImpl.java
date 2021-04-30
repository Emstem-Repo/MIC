package com.kp.cms.transactionsimpl.studentExtentionActivity;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.studentExtentionActivity.StudentGroupForm;
import com.kp.cms.transactions.studentExtentionActivity.IStudentGroupTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class StudentGroupTransactionImpl implements IStudentGroupTransaction {

	 private static volatile StudentGroupTransactionImpl obj;
	 public static StudentGroupTransactionImpl getInstance(){
		 if(obj == null){
			 obj = new StudentGroupTransactionImpl();
		 }
		 return obj;
	 }
	 
	 public List<StudentGroup> getStudentGroupDetails() throws Exception
		{
			Session session = null;
			
			try
			{
				session = HibernateUtil.getSession();
				Query query = session.createQuery(" from StudentGroup se where isActive = 1 order by se.groupName");
				List<StudentGroup> studentGroup = query.list();
				return studentGroup;
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
		public StudentGroup isDuplicate(StudentGroup oldStudentGroup)
				throws Exception {
	        Session session = null;
			
			try
			{
				session = HibernateUtil.getSession();
				Query query = session.createQuery(" from StudentGroup where groupName = :groupName");
				query.setString("groupName", oldStudentGroup.getGroupName());
				StudentGroup studentGroup = (StudentGroup)query.uniqueResult();
				return studentGroup;
			}
			catch (Exception e) 
			{
				throw new ApplicationException(e);
			}
	}

	@Override
	public boolean addStudenGroup(StudentGroup studentGroup, String mode) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			if(mode.equalsIgnoreCase("Add"))
			{
				session.save(studentGroup);
			}
			else if(mode.equalsIgnoreCase("Edit"))
			{
				session.update(studentGroup);
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
			throw new ApplicationException();
		}
	}

	@Override
	public boolean deleteStudentGroup(int dupId, boolean activate,
			StudentGroupForm studentGroupForm) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			StudentGroup studentform = (StudentGroup) session.get(StudentGroup.class, dupId);
			if(activate)
			{
				studentform.setIsActive(true);
			}
			else
			{
				studentform.setIsActive(false);
			}
			studentform.setModifiedBy(studentGroupForm.getUserId());
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

	
}
