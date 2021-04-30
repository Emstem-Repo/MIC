package com.kp.cms.transactionsimpl.exam;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.exam.ConsolidatedSubjectSection;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.exam.ConsolidatedSubjectSectionForm;
import com.kp.cms.transactions.exam.IConsolidatedSubjectSectionTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class ConsolidatedSubjectSectionTransactionImpl implements IConsolidatedSubjectSectionTransaction 
{
	private static volatile ConsolidatedSubjectSectionTransactionImpl obj;
	
	public static ConsolidatedSubjectSectionTransactionImpl getInstance()
	{
		if(obj == null)
		{
			obj = new ConsolidatedSubjectSectionTransactionImpl();
		}
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsolidatedSubjectSection> getConsolidatedSubjectSections() throws Exception
	{
		Session session = null;

		try
		{
			session = HibernateUtil.getSession();
			Query query = session.createQuery(" from ConsolidatedSubjectSection section where isActive = 1 order by section.sectionOrder");
			List<ConsolidatedSubjectSection> consolidatedSubjectSections = query.list();
			session.flush();
			return consolidatedSubjectSections;
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
	
	public boolean addConsolidatedSubjectSection(ConsolidatedSubjectSection consolidatedSubjectSection, String mode) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			if(mode.equalsIgnoreCase("Add"))
			{
				session.save(consolidatedSubjectSection);
			}
			else if(mode.equalsIgnoreCase("Edit"))
			{
				session.update(consolidatedSubjectSection);
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
	
	public boolean deleteConsolidatedSubjectSection(int dupId, boolean activate, ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		
		try
		{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			ConsolidatedSubjectSection consolidatedSubjectSection = (ConsolidatedSubjectSection) session.get(ConsolidatedSubjectSection.class, dupId);
			if(activate)
			{
				consolidatedSubjectSection.setIsActive(true);
			}
			else
			{
				consolidatedSubjectSection.setIsActive(false);
			}
			consolidatedSubjectSection.setModifiedBy(consolidatedSubjectSectionForm.getUserId());
			consolidatedSubjectSection.setLastModifiedDate(new Date());
			session.update(consolidatedSubjectSection);
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
	
	public ConsolidatedSubjectSection isDuplicate(ConsolidatedSubjectSection oldConsolidatedSubjectSection) throws Exception
	{
		Session session = null;
		
		try
		{
			session = HibernateUtil.getSession();
			Query query = session.createQuery(" from ConsolidatedSubjectSection where sectionName = :sectionName");
			query.setString("sectionName", oldConsolidatedSubjectSection.getSectionName());
			ConsolidatedSubjectSection consolidatedSubjectSection = (ConsolidatedSubjectSection)query.uniqueResult();
			session.flush();
			return consolidatedSubjectSection;
		}
		catch (Exception e) 
		{
			throw new ApplicationException(e);
		}
	}
}
