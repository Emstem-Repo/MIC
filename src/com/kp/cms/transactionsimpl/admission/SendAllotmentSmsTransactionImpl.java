package com.kp.cms.transactionsimpl.admission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.forms.admission.SendAllotmentMemoSmsForm;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.bo.admission.GenerateMemoMail;
import com.kp.cms.transactions.admission.ISendAllotmentSmsTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class SendAllotmentSmsTransactionImpl implements ISendAllotmentSmsTransaction {

	@Override
	public List<Integer> getSentMemoList(
			SendAllotmentMemoSmsForm sbForm) throws ApplicationException {
		 Session session = null;
		 List<Integer> stList = new ArrayList<Integer>();
		try{
			StringBuffer quer=new StringBuffer("select bo.studentId from SendAllotmentMemoSmsBo bo where  bo.isSureMemo=:isSure and bo.allotmentNo=:allNo and bo.appliedYear=:year");
	 session = HibernateUtil.getSession();
	 Query query=session.createQuery(quer.toString());
	
		 query.setBoolean("isSure",sbForm.getIsSureMemo());
		 query.setInteger("allNo",sbForm.getSureOrChanceMemoNo());
		 query.setInteger("year",Integer.parseInt(sbForm.getYear()));
	
	 stList=query.list();
	 stList.add(0);
	 
		}catch(Exception e){
	 throw  new ApplicationException(e);
		}
		return stList;
	}

	@Override
	public List<Student> getSureStudentList(SendAllotmentMemoSmsForm sbForm, List<Integer> sentMemoList,Set<Integer> tempcourseset) throws ApplicationException {
		Session session = null;
		List<Student> stList;
		try{
			
			StringBuffer quer=new StringBuffer("select st from StudentCourseAllotment bo join bo.admAppln adm join adm.students st  where st.id not in(:studList)  and bo.admAppln.appliedYear=:year and bo.allotmentNo=:allNo and bo.course.id in(:course) group by st.id ");
	 session = HibernateUtil.getSession();
	 Query query=session.createQuery(quer.toString());
	     query.setParameterList("course",tempcourseset);
		 query.setParameterList("studList",sentMemoList);
		 query.setInteger("allNo",sbForm.getSureOrChanceMemoNo());
		 query.setParameter("year",Integer.parseInt(sbForm.getYear()));
	
	 stList=query.list();
	 
		}catch(Exception e){
	 throw  new ApplicationException(e);
		}
		return stList;
	}

	@Override
	public List<Student> getChanceStudentList(SendAllotmentMemoSmsForm sbForm,
			List<Integer> sentMemoList,Set<Integer> tempcourseset) throws ApplicationException {
		Session session = null;
		List<Student> stList;
		try{
			
			StringBuffer quer=new StringBuffer("select st from StudentCourseChanceMemo bo join bo.admAppln adm join adm.students st  where st.id not in(:studList)  and bo.admAppln.appliedYear=:year and bo.chanceNo=:allNo and bo.course.id in(:course) group by st.id ");
	 session = HibernateUtil.getSession();
	 Query query=session.createQuery(quer.toString());
	     query.setParameterList("course",tempcourseset);
		 query.setParameterList("studList",sentMemoList);
		 query.setInteger("allNo",sbForm.getSureOrChanceMemoNo());
		 query.setParameter("year",Integer.parseInt(sbForm.getYear()));
	
	 stList=query.list();
	 
		}catch(Exception e){
	 throw  new ApplicationException(e);
		}
		return stList;
	}

	@Override
	public boolean saveMailList(List<GenerateMemoMail> list) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		GenerateMemoMail tcLChecklist;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Iterator<GenerateMemoMail> tcIterator = list.iterator();
			int count = 0;
			while(tcIterator.hasNext()){
				tcLChecklist = tcIterator.next();
				session.save(tcLChecklist);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
				}
			}
			
			transaction.commit();
			session.flush();
			//session.close();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new BusinessException(e);
		} catch (Exception e) {
			transaction.rollback();
			throw new ApplicationException(e);
		}
	}

	@Override
	public List<Integer> getSentMemoMailList(SendAllotmentMemoSmsForm sbForm) throws ApplicationException {
		 Session session = null;
		 List<Integer> stList = new ArrayList<Integer>();
		try{
			StringBuffer quer=new StringBuffer("select bo.studentId from GenerateMemoMail bo where  bo.isSureMemo=:isSure and bo.allotmentNo=:allNo and bo.appliedYear=:year");
	 session = HibernateUtil.getSession();
	 Query query=session.createQuery(quer.toString());
	
		 query.setBoolean("isSure",sbForm.getIsSureMemo());
		 query.setInteger("allNo",sbForm.getSureOrChanceMemoNo());
		 query.setInteger("year",Integer.parseInt(sbForm.getYear()));
	
	 stList=query.list();
	 stList.add(0);
	 
		}catch(Exception e){
	 throw  new ApplicationException(e);
		}
		return stList;
	}

}
