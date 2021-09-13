package com.kp.cms.transactionsimpl.exam;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.transactions.exam.ISpecialFeesTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class SpecialFeesTransactionImpl implements ISpecialFeesTransaction {
	private static volatile SpecialFeesTransactionImpl specialFeesTransactionImpl = null;
	private static final Log log = LogFactory.getLog(SpecialFeesTransactionImpl.class);
	private SpecialFeesTransactionImpl() {
		
	}
	/**
	 * return singleton object of NewExamMarksEntryTransactionImpl.
	 * @return
	 */
	public static SpecialFeesTransactionImpl getInstance() {
		if (specialFeesTransactionImpl == null) {
			specialFeesTransactionImpl = new SpecialFeesTransactionImpl();
		}
		return specialFeesTransactionImpl;
	}
	@Override
	public List<SpecialFeesBO> getList(String query) throws Exception {
		Session session = null;
		List list = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();

			Query selectedCandidatesQuery=session.createQuery(query);
			list = selectedCandidatesQuery.list();
			return list;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		}
	}
	@Override
	public Object getMasterEntryDataById(Class class1,
			int id) throws Exception {
		Session session = null;
		Object obj= null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Criteria criteria=session.createCriteria(class1);
			criteria.add(Restrictions.eq("id",id));
			obj= criteria.uniqueResult();
			return obj;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
	}
}
