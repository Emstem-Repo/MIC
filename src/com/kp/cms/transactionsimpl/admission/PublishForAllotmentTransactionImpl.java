package com.kp.cms.transactionsimpl.admission;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.ConvocationSession;
import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admission.PublishForAllotmentForm;
import com.kp.cms.transactions.admission.IPublishForAllotmentTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;

public class PublishForAllotmentTransactionImpl implements IPublishForAllotmentTransaction{
	
	public static volatile PublishForAllotmentTransactionImpl impl = null;
	
	public static PublishForAllotmentTransactionImpl getInstance(){
		if(impl==null){
			impl=new PublishForAllotmentTransactionImpl();
			return impl;
		}
		return impl;
	}

	@Override
	public boolean checkDuplicate(PublishForAllotmentForm allotmentForm) throws Exception {
		Session session = null;
		boolean duplicate = false;
		try{
			session  = HibernateUtil.getSession();
			String hqlquery = "select c.course.id from PublishForAllotment c where c.isActive = 1"
					+ " and c.allotmentNo= '"+Integer.parseInt(allotmentForm.getAllotmentNo())+"'" + 
					" and  c.appliedYear='"+allotmentForm.getYear()+"'" +
					" and c.chanceNo= '"+Integer.parseInt(allotmentForm.getChanceNo())+"'";
			Query newQuery =  session.createQuery(hqlquery);
			List<Integer> courseIds = newQuery.list();
			if(courseIds != null && !courseIds.isEmpty()){
				String courseNames ="";
				for (int i = 0; i < allotmentForm.getCourseIds().length; i++) {
					if(allotmentForm.getCourseIds()[i] != null){
						if(courseIds.contains(Integer.parseInt(allotmentForm.getCourseIds()[i]))){
							duplicate = true;
							if(courseNames.isEmpty()){
								courseNames = allotmentForm.getCourseMap().get(Integer.parseInt(allotmentForm.getCourseIds()[i]));
							}else{
								courseNames = courseNames +" ;    "+allotmentForm.getCourseMap().get(Integer.parseInt(allotmentForm.getCourseIds()[i]));
							}
						}
					}
				}
				allotmentForm.setErrorMessage(courseNames);
			}
			
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			//session.close();
		}
	}
		return duplicate;
	}

	@Override
	public boolean addDetails(List<PublishForAllotment> allotments) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isAdded = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			for(PublishForAllotment allotment:allotments){
				session.save(allotment);
			}
			tx.commit();
			isAdded = true;
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
		}
		
	}
		return isAdded;
	}

	@Override
	public List<PublishForAllotment> getBolist() throws Exception {
		Session session=null;
		List<PublishForAllotment> allotments = new ArrayList<PublishForAllotment>();
		try{
			session=HibernateUtil.getSession();
			String hql="from PublishForAllotment p where p.isActive=1";
			Query query= session.createQuery(hql);
			allotments=query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return allotments;
	}

	@Override
	public PublishForAllotment getBo(PublishForAllotmentForm allotmentForm) throws Exception {
		Session session=null;
		PublishForAllotment allotment=null;
		try{
			session=HibernateUtil.getSession();
			String hql="from PublishForAllotment p where p.isActive=1 and p.id="+allotmentForm.getId();
			Query query = session.createQuery(hql);
			allotment=(PublishForAllotment)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return allotment;
	}

	@Override
	public boolean updateBo(PublishForAllotment allotment) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isUpdated = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			session.update(allotment);
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
