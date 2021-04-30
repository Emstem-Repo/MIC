package com.kp.cms.transactionsimpl.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.kp.cms.transactions.admin.IConductAndCertificateTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class ConductAndCertificateTransactionImpl implements IConductAndCertificateTransaction{
	
private static volatile ConductAndCertificateTransactionImpl obj=null;
	
	public static ConductAndCertificateTransactionImpl getInstance(){
		if(obj==null){
			obj = new ConductAndCertificateTransactionImpl();
		}
		return obj; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSubsidiaries(int stuId) throws Exception {
		Session session =null;
		List<String> subs= new ArrayList<String>();
		try{
			session=HibernateUtil.getSession();
			String hql="select c.subjectStream.streamName from ConsolidatedMarksCardProgrammePart c where c.student.id=:sid and c.consolidatedSubjectSection.id=4";
			Query query =session.createQuery(hql)
			  					.setInteger("sid", stuId);
			subs=query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return subs;
	}

}
