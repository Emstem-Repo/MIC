package com.kp.cms.transactionsimpl.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.exam.FalseNumSiNo;
import com.kp.cms.bo.exam.FalseNumberBox;
import com.kp.cms.bo.exam.FalseNumberBoxDetails;
import com.kp.cms.forms.exam.FalseNumSiNoForm;
import com.kp.cms.transactions.exam.IFalseNumSiNoTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class FalseNumSiNoTransactionImpl implements IFalseNumSiNoTransaction {


	public boolean save(FalseNumSiNo bo)throws Exception{
		Session session = null;
		Transaction tx=null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			session.save(bo);
			tx.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
			return false;
		}
	}

	public List<FalseNumSiNo> getData()throws Exception{
		FalseNumSiNo bo = null;
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			List<FalseNumSiNo> l =session.createQuery("from FalseNumSiNo bo where bo.isActive=1").list();

			return l;

			//			session.flush();
			//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return null;
	}

	public boolean getDataAvailable(FalseNumSiNoForm cardSiNoForm)throws Exception{
		FalseNumSiNo bo = null;
		Session session = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			bo = (FalseNumSiNo)session.createQuery("from FalseNumSiNo bo where bo.isActive=1 and bo.academicYear="+Integer.parseInt(cardSiNoForm.getAcademicYear())+" and bo.courseId.id="+Integer.parseInt(cardSiNoForm.getCourseId())+" and bo.semister="+Integer.parseInt(cardSiNoForm.getSemister())+" and bo.examId.id="+Integer.parseInt(cardSiNoForm.getExamId())).uniqueResult();
			if(bo!=null){
				return true;
			}
			//			session.flush();
			//			session.close();
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
		}
		return false;
	}

	@Override
	public FalseNumSiNo getFalseNoBoObject(FalseNumSiNoForm cardSiNoForm)throws Exception {
		Session session =null;
		FalseNumSiNo siNo =null;
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumSiNo f where f.id = :fid and f.isActive=1";
			Query query = session.createQuery(hql)
					.setInteger("fid", cardSiNoForm.getFalseNoId());
			siNo = (FalseNumSiNo)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return siNo;
	}

	@Override
	public boolean updateFalseNo(FalseNumSiNo numSiNo) throws Exception {
		Session session =null;
		Transaction transaction =null;
		try{
			session=HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.update(numSiNo);
			transaction.commit();
			session.flush();
			session.close();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Map<Integer, String> getTeachers(int subId, int year)throws Exception {
		Session session =null;
		FalseNumSiNo siNo =null;
		List<Object[]> tList=new ArrayList();
		Map<Integer, String> tmap=new HashMap();
		try{
			session =HibernateUtil.getSession();
			
			String hql = "select "+
					//" curriculum_scheme_duration.academic_year, "+
					" users.id as user_id, "+
					" users.user_name "+
					/*" subject.id as subject_id, "+
					" subject.name as subject, "+
					" classes.id as class_id, "+
					" classes.name as class, "+
					" classes.term_number, "+
					" course.id as course_id, "+
					" course.name as course "+*/
					" FROM teacher_class_subject "+
					" inner join class_schemewise ON class_schemewise.id = teacher_class_subject.class_schemewise_id "+
					" inner join classes ON classes.id = class_schemewise.class_id "+
					" inner join curriculum_scheme_duration ON curriculum_scheme_duration.id = class_schemewise.curriculum_scheme_duration_id "+
					" inner join subject ON subject.id = teacher_class_subject.subject_id "+
					" inner join users ON users.id = teacher_class_subject.teacher_id "+
					" inner join course ON course.id = classes.course_id "+
					" where curriculum_scheme_duration.academic_year="+year+
					" and subject.id="+subId;
					Query query = session.createSQLQuery(hql);
					tList= query.list();
					for (Object obj[] : tList) {
						tmap.put(Integer.parseInt(obj[0].toString()), (String)obj[1]);
					}
					
		}catch (Exception e) {
			e.printStackTrace();
		}
		return tmap;
	}

	@Override
	public boolean updateFalseNoBox(List<FalseNumberBoxDetails> falseNumberBox) {
		Session session = null;
		Transaction tx=null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			int i=0;
			for (FalseNumberBoxDetails bo : falseNumberBox) {
				if (i==0) {
					session.save(bo.getFalseNumBox());
				}
				session.save(bo);
				i++;
			}
			
			tx.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}
			return false;
		}
	}

	@Override
	public boolean getDuplicate(FalseNumSiNoForm cardSiNoForm) {
		Session session =null;
		FalseNumberBox box =null;
		String semString[]=cardSiNoForm.getSchemeNo().split("_");
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumberBox fb where fb.academicYear="+ cardSiNoForm.getYear()+
					" and fb.schemeNum="+ semString[1]+" and fb.courseId="+ cardSiNoForm.getCourseId()+" and fb.boxNum="+ cardSiNoForm.getBoxNo()+
					" and fb.examId="+ cardSiNoForm.getExamId()+" and fb.subjectId="+cardSiNoForm.getSubjectId();
			Query query = session.createQuery(hql);
			box = (FalseNumberBox)query.uniqueResult();
			if (box==null) 
				return false;
			else 
				return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<FalseNumberBox> getFalseBox(FalseNumSiNoForm cardSiNoForm) {
		Session session =null;
		List<FalseNumberBox> boxList =null;
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumberBox f";
			Query query = session.createQuery(hql);
			boxList = query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return boxList;
	}
}
