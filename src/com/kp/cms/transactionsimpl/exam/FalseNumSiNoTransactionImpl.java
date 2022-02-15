package com.kp.cms.transactionsimpl.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

	public Map<Integer, String> getTeachers(int dept, int outside)throws Exception {
		Session session =null;
		FalseNumSiNo siNo =null;
		List<Object[]> tList=new ArrayList();
		Map<Integer, String> tmap=new HashMap();
		try{
			session =HibernateUtil.getSession();
			
			String hql=null;
				if (outside!=2 && outside!=3) {
					hql=" select users.id as user_id,  users.user_name  , "+
							" ifnull(employee.id,guest_faculty.id) as employee_id, "+
							" ifnull(employee.first_name,guest_faculty.first_name) as employ_name, department.id as department_id, "+
							" department.name as department FROM  users  "+
							" left join employee ON employee.id = users.employee_id left join guest_faculty on users.guest_id=guest_faculty.id "+
							" inner join department ON ifnull((department.id = employee.department_id),(department.id=guest_faculty.department_id and guest_faculty.outside="+outside +")) "+
							" where department.id= "+dept;
				}else if (outside==2) {
					hql=" select users.id as user_id,  users.user_name  , "+
							" ifnull(employee.id,guest_faculty.id) as employee_id, "+
							" ifnull(employee.first_name,guest_faculty.first_name) as employ_name, department.id as department_id, "+
							" department.name as department FROM  users  "+
							" left join employee ON employee.id = users.employee_id left join guest_faculty on users.guest_id=guest_faculty.id "+
							" inner join department ON ifnull((department.id = employee.department_id),(department.id=guest_faculty.department_id )) "+
							" where department.id= "+dept;
				}else{
					hql=" select users.id as user_id,  users.user_name  , "+
							" ifnull(employee.id,guest_faculty.id) as employee_id, "+
							" ifnull(employee.first_name,guest_faculty.first_name) as employ_name, department.id as department_id, "+
							" department.name as department FROM  users  "+
							" left join employee ON employee.id = users.employee_id left join guest_faculty on users.guest_id=guest_faculty.id "+
							" inner join department ON ifnull((department.id = employee.department_id),(department.id=guest_faculty.department_id )) ";
				}


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
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			//session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			int i=0;
			for (FalseNumberBoxDetails bo : falseNumberBox) {
				if (i==0) {
					session.saveOrUpdate(bo.getFalseNumBox());
				}
				session.saveOrUpdate(bo);
				i++;
			}
			
			tx.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if (session != null){
				session.flush();
				session.close();
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
					" and fb.schemeNum="+ semString[1]+" and fb.courseId="+ cardSiNoForm.getCourseId()+" and fb.boxNum='"+ cardSiNoForm.getBoxNo()+
					"' and fb.examId="+ cardSiNoForm.getExamId()+" and fb.subjectId="+cardSiNoForm.getSubjectId()+" and fb.isActive=true";
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
	public List<FalseNumberBoxDetails> getFlaseNumberDetils(FalseNumSiNoForm cardSiNoForm) {
		Session session =null;
		List<FalseNumberBoxDetails> boxdetailsList =null;
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumberBoxDetails fb where fb.falseNumBox.id="+ cardSiNoForm.getFalseBoxId()+" and fb.isActive=true";
					;
			Query query = session.createQuery(hql);
			boxdetailsList = query.list();
			
				return boxdetailsList;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<FalseNumberBox> getFalseBox(FalseNumSiNoForm cardSiNoForm) {
		Session session =null;
		List<FalseNumberBox> boxList =null;
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumberBox f where f.isActive=true";
			if (cardSiNoForm.getYear()!=null && !cardSiNoForm.getYear().isEmpty()) {
				hql=hql+" and f.academicYear="+cardSiNoForm.getYear();
			}
			if (cardSiNoForm.getExamId()!=null && !cardSiNoForm.getExamId().isEmpty()) {
				hql=hql+" and f.examId.id="+cardSiNoForm.getExamId();
			}
			Query query = session.createQuery(hql);
			boxList = query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return boxList;
	}
	@Override
	public boolean getDuplicateDet(FalseNumSiNoForm cardSiNoForm,String num) {
		Session session =null;
		FalseNumberBoxDetails box =null;
		String semString[]=cardSiNoForm.getSchemeNo().split("_");
		try{
			session =HibernateUtil.getSession();
			String hql = "from FalseNumberBoxDetails fb where fb.falseNumBox.id="+ cardSiNoForm.getFalseBoxId()+
					" and fb.isActive=true"+" and fb.falseNum='"+num+"' and fb.falseNumBox.isActive=true";
			Query query = session.createQuery(hql);
			box = (FalseNumberBoxDetails)query.uniqueResult();
			if (box==null) 
				return false;
			else 
				return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
