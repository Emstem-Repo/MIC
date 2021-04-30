package com.kp.cms.transactionsimpl.admission;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePreferenceEntranceDetails;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admission.ApplicationEditForm;
import com.kp.cms.forms.admission.DDStatusForm;
import com.kp.cms.to.admin.CandidatePreferenceEntranceDetailsTO;
import com.kp.cms.to.admission.DDStatusTO;
import com.kp.cms.transactions.admission.IDDStatusTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class DDStatusTransactionImpl implements IDDStatusTransaction {
	/**
	 * Singleton object of DDStatusTransactionImpl
	 */
	private static DDStatusTransactionImpl dDStatusTransactionImpl = null;
	private static final Log log = LogFactory.getLog(DDStatusTransactionImpl.class);
	private DDStatusTransactionImpl() {
		
	}
	/**
	 * return singleton object of DDStatusTransactionImpl.
	 * @return
	 */
	public static DDStatusTransactionImpl getInstance() {
		if (dDStatusTransactionImpl == null) {
			dDStatusTransactionImpl = new DDStatusTransactionImpl();
		}
		return dDStatusTransactionImpl;
	}
	@Override
	public boolean getAlreadyEntered(String query) throws Exception {
		Session session = null;
		AdmAppln bo=null;
		boolean isAlready=false;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery(query);
			bo =(AdmAppln) selectedCandidatesQuery.uniqueResult();
			if(bo!=null){
				if(bo.getRecievedDDNo()!=null && !bo.getRecievedDDNo().isEmpty()){
					isAlready=true;
				}
			}
			return isAlready;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	@Override
	public boolean checkStudent(String query) throws Exception {
		Session session = null;
		AdmAppln bo=null;
		boolean isAlready=false;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery(query);
			bo =(AdmAppln) selectedCandidatesQuery.uniqueResult();
			if(bo!=null){
					isAlready=true;
			}
			return isAlready;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	@Override
	public AdmAppln updateStatus(String query,DDStatusForm dDStatusForm) throws Exception {
		Session session = null;
		AdmAppln bo=null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Transaction tx=session.beginTransaction();
			Query selectedCandidatesQuery=session.createQuery(query);
			bo =(AdmAppln) selectedCandidatesQuery.uniqueResult();
			if(bo!=null){
				bo.setRecievedDate(CommonUtil.ConvertStringToSQLDate(dDStatusForm.getRecievedDDDate()));
				bo.setRecievedDDNo(dDStatusForm.getRecievedDDNo());
				bo.setModifiedBy(dDStatusForm.getUserId());
				bo.setLastModifiedDate(new Date());
				bo.setIsDDRecieved(true);
				session.update(bo);
			}
			tx.commit();
			return bo;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	
	
	
	//raghu
	
	@Override
	public boolean getAlreadyEntered1(String query) throws Exception {
		Session session = null;
		AdmAppln bo=null;
		boolean isAlready=false;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery(query);
			bo =(AdmAppln) selectedCandidatesQuery.uniqueResult();
			if(bo!=null){
				if(bo.getRecievedChallanNo()!=null && !bo.getRecievedChallanNo().isEmpty()){
					isAlready=true;
				}
			}
			return isAlready;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	
	
	
	
	@Override
	public AdmAppln updateStatus1(String query,DDStatusForm dDStatusForm) throws Exception {
		Session session = null;
		AdmAppln bo=null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Transaction tx=session.beginTransaction();
			Query selectedCandidatesQuery=session.createQuery(query);
			bo =(AdmAppln) selectedCandidatesQuery.uniqueResult();
			if(bo!=null){
				bo.setRecievedChallanDate(CommonUtil.ConvertStringToSQLDate(dDStatusForm.getRecievedChallanDate()));
				bo.setRecievedChallanNo(dDStatusForm.getRecievedChallanNo());
				bo.setModifiedBy(dDStatusForm.getUserId());
				bo.setLastModifiedDate(new Date());
				bo.setIsChallanRecieved(true);
				session.update(bo);
			}
			tx.commit();
			return bo;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	
	
	
	@Override
	public List<AdmAppln> getStudentsChallanStatusOnCourse(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<AdmAppln> list=null;
		Map<Integer,String> m=new HashMap<Integer, String>();
		try{
			session = HibernateUtil.getSession();
			//Query hqlQuery = session.createQuery(" from AdmAppln a where  a.course.program.programType.id="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and a.mode='CHALLAN' and (a.isChallanRecieved=0 or a.isChallanRecieved is null)");
			Query hqlQuery = session.createQuery(" from AdmAppln a where  a.studentOnlineApplication.programTypeId="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and (a.mode='Challan' or a.mode='NEFT') and (a.isChallanRecieved=0 or a.isChallanRecieved is null)");
			//neft check
			
			hqlQuery.setString("cdate",CommonUtil.ConvertStringToSQLDate(ddForm.getRecievedChallanDate()).toString());
			list=hqlQuery.list();
			
			Iterator<AdmAppln> i=list.iterator();
			//while(i.hasNext()){
			//	AdmAppln a=i.next();
			//	m.put(a.getId(), a.getPersonalData().getFirstName());
				
			//}
			 
			ddForm.setStudentMap(m);
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	

	
	
	@Override
	public boolean updateChallanStatusOnCourse(DDStatusForm ddForm)	throws Exception {
		// TODO Auto-generated method stub
		
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			// SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			AdmAppln bo=null;
			
			if (list != null && !list.isEmpty()) {
				
					Iterator<DDStatusTO> itr = list.iterator();
				
					int count = 0;
					
					while (itr.hasNext()) {
						
						DDStatusTO  ddTo = (DDStatusTO) itr.next();
					
						
						if (ddTo.getChecked()!=null && ddTo.getChecked().equalsIgnoreCase("on")){
							
						
						bo =(AdmAppln) session.get(AdmAppln.class,ddTo.getAdmId() );
							
							bo.setRecievedChallanDate(new Date());
							bo.setRecievedChallanNo(ddTo.getEnterdChallanNo());
							bo.setModifiedBy(ddForm.getUserId());
							bo.setLastModifiedDate(new Date());
							bo.setIsChallanRecieved(true);
							
						
						transaction.begin();
						session.update(bo);	
						transaction.commit();
						
						//if (++count % 20 == 0) {
						//session.flush();
						//session.clear();
						//}
						isAdded = true;
						
						}
						
					}
					
				}
					
			
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
		
	}
	

	
	
	
	@Override
	public List<AdmAppln> getStudentsDDStatusOnCourse(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<AdmAppln> list=null;
		Map<Integer,String> m=new HashMap<Integer, String>();
		try{
			session = HibernateUtil.getSession();
			Query hqlQuery = session.createQuery(" from AdmAppln a where  a.course.program.programType.id="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and a.mode='DD' and (a.isDDRecieved=0 or a.isDDRecieved is null)");
			hqlQuery.setString("cdate",CommonUtil.ConvertStringToSQLDate(ddForm.getRecievedChallanDate()).toString());
			list=hqlQuery.list();
			
			Iterator<AdmAppln> i=list.iterator();
			//while(i.hasNext()){
			//	AdmAppln a=i.next();
			//	m.put(a.getId(), a.getPersonalData().getFirstName());
				
			//}
			 
			ddForm.setStudentMap(m);
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	

	
	
	@Override
	public boolean updateDDStatusOnCourse(DDStatusForm ddForm)	throws Exception {
		// TODO Auto-generated method stub
		
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			// SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			AdmAppln bo=null;
			
			if (list != null && !list.isEmpty()) {
				
					Iterator<DDStatusTO> itr = list.iterator();
				
					int count = 0;
					
					while (itr.hasNext()) {
						
						DDStatusTO  ddTo = (DDStatusTO) itr.next();
					
						
						if (ddTo.getChecked()!=null && ddTo.getChecked().equalsIgnoreCase("on")){
							
						
						bo =(AdmAppln) session.get(AdmAppln.class,ddTo.getAdmId() );
							
							bo.setRecievedDate(new Date());
							bo.setRecievedDDNo(ddTo.getEnterdChallanNo());
							bo.setModifiedBy(ddForm.getUserId());
							bo.setLastModifiedDate(new Date());
							bo.setIsDDRecieved(true);
							
						
						transaction.begin();
						session.update(bo);	
						transaction.commit();
						
						//if (++count % 20 == 0) {
						//session.flush();
						//session.clear();
						//}
						isAdded = true;
						
						}
						
					}
					
				}
					
			
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
		
	}
	
	
	
	public List<AdmAppln> getStudentsChallanDtailsOnDate(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<AdmAppln> list=null;
		
		try{
			session = HibernateUtil.getSession();
			Query hqlQuery = session.createQuery("	from AdmAppln a where   a.appliedYear=:cyear  and (a.mode='Challan' or a.mode='NEFT') and (a.isChallanRecieved=0 or a.isChallanRecieved is null) "+
			" and a.journalNo in ( select cd.challanNo from ChallanUploadData cd where cd.year=:cyear and cd.challanDate=:cdate)");
			//neft check
			
			hqlQuery.setString("cyear",ddForm.getAcademicYear());		
			hqlQuery.setString("cdate",CommonUtil.ConvertStringToSQLDate(ddForm.getRecievedChallanDate()).toString());
			list=hqlQuery.list();
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	
	
	@Override
	public boolean updateChallanUploadProcess(DDStatusForm ddForm)	throws Exception {
		// TODO Auto-generated method stub
		
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			// SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			AdmAppln bo=null;
			
			if (list != null && !list.isEmpty()) {
				
					Iterator<DDStatusTO> itr = list.iterator();
				
					int count = 0;
					
					while (itr.hasNext()) {
						
						DDStatusTO  ddTo = (DDStatusTO) itr.next();
					
						bo =(AdmAppln) session.get(AdmAppln.class,ddTo.getAdmId() );
							
							bo.setRecievedChallanDate(new Date());
							bo.setRecievedChallanNo(ddTo.getEnterdChallanNo());
							bo.setModifiedBy(ddForm.getUserId());
							bo.setLastModifiedDate(new Date());
							bo.setIsChallanRecieved(true);
							
						
						transaction.begin();
						session.update(bo);	
						
						
						if (++count % 20 == 0) {
						session.flush();
						session.clear();
						}
						
						
						
						
					}
					
					transaction.commit();
					isAdded = true;
				}
					
			
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
		
	}
	

	@Override
	public List<ExamRegularApplication> getStudentsChallanStatusOnCourseForExam(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<ExamRegularApplication> list=null;
		Map<Integer,String> m=new HashMap<Integer, String>();
		try{
			session = HibernateUtil.getSession();
			//Query hqlQuery = session.createQuery(" from AdmAppln a where  a.course.program.programType.id="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and a.mode='CHALLAN' and (a.isChallanRecieved=0 or a.isChallanRecieved is null)");
			Query hqlQuery = session.createQuery("from ExamRegularApplication e where  e.classes.id="+Integer.parseInt(ddForm.getClasses())+" and e.exam.id="+ddForm.getExamid()+" and e.challanVerified=0");
			//neft check
			list=hqlQuery.list();
			
			Iterator<ExamRegularApplication> i=list.iterator();
			//while(i.hasNext()){
			//	AdmAppln a=i.next();
			//	m.put(a.getId(), a.getPersonalData().getFirstName());
				
			//}
			 
			ddForm.setStudentMap(m);
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	
	@Override
	public boolean updateChallanStatusOnCourseForExam(DDStatusForm ddForm)	throws Exception {
		// TODO Auto-generated method stub
		
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			// SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			ExamRegularApplication bo=null;
			
			if (list != null && !list.isEmpty()) {
				
					Iterator<DDStatusTO> itr = list.iterator();
				
					int count = 0;
					
					while (itr.hasNext()) {
						
						DDStatusTO  ddTo = (DDStatusTO) itr.next();
					
						
						if (ddTo.getChecked()!=null && ddTo.getChecked().equalsIgnoreCase("on")){
							
						
						bo =(ExamRegularApplication) session.get(ExamRegularApplication.class,ddTo.getAdmId() );
						
							bo.setModifiedBy(ddForm.getUserId());
							bo.setLastModifiedDate(new Date());
							bo.setChallanVerified(true);
							bo.setRemarks(ddTo.getRemarks());
							bo.setAmount(ddTo.getFees());
						
						transaction.begin();
						session.update(bo);	
						transaction.commit();
						
						//if (++count % 20 == 0) {
						//session.flush();
						//session.clear();
						//}
						isAdded = true;
						
						}
						
					}
					
				}
					
			
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
		
	}
	
	public List<ExamRegularApplication> getStudentsChallanDtailsOnDateForExam(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<ExamRegularApplication> list=null;
		
		try{
			session = HibernateUtil.getSession();
			Query hqlQuery = session.createQuery("	from ExamRegularApplication e where e.challanVerified=0 "+
			" and e.challanNo in ( select cd.challanNo from ChallanUploadDataExam cd where cd.challanDate=:cdate)");
			//neft check		
			hqlQuery.setString("cdate",CommonUtil.ConvertStringToSQLDate(ddForm.getRecievedChallanDate()).toString());
			list=hqlQuery.list();
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	
	public boolean updateChallanUploadProcessForExam(DDStatusForm ddForm)	throws Exception {
		// TODO Auto-generated method stub
		
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			// SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			ExamRegularApplication bo=null;
			
			if (list != null && !list.isEmpty()) {
				
					Iterator<DDStatusTO> itr = list.iterator();
				
					int count = 0;
					
					while (itr.hasNext()) {
						
						DDStatusTO  ddTo = (DDStatusTO) itr.next();
					
						bo =(ExamRegularApplication) session.get(ExamRegularApplication.class,ddTo.getStuRegAppId() );
							
							bo.setModifiedBy(ddForm.getUserId());
							bo.setLastModifiedDate(new Date());
							bo.setChallanVerified(true);
							
						
						transaction.begin();
						session.update(bo);	
						
						
						if (++count % 20 == 0) {
						session.flush();
						session.clear();
						}
						
						
						
						
					}
					
					transaction.commit();
					isAdded = true;
				}
					
			
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
		
	}
	
	public Integer ChallanVerifiedCount(DDStatusForm ddform) throws Exception
	{
		Session session=null;
		Integer count;
		try
		{
			session=HibernateUtil.getSession();
			String s1="select count(a.id) from AdmAppln a where a.date=:challandate and a.isChallanRecieved=1";
			Query query=session.createQuery(s1);
			query.setString("challandate", CommonUtil.ConvertStringToSQLDate(ddform.getRecievedChallanDate()).toString());
			Long count1=(Long)query.uniqueResult();
			count=(int)(long)count1;
		}
		catch(Exception e)
		{
			throw new ApplicationException(e);
		}
		finally
		{
			if(session!=null)
			{
				session.flush();
			}
		}
		return count;
	}
	
	public Integer ChallanNotVerifiedCount(DDStatusForm ddform) throws Exception
	{
		Session session=null;
		Integer count;
		try
		{
			session=HibernateUtil.getSession();
			String s1="select count(a.id) from AdmAppln a where a.date=:challandate and a.isChallanRecieved=0";
			Query query=session.createQuery(s1);
			query.setString("challandate", CommonUtil.ConvertStringToSQLDate(ddform.getRecievedChallanDate()).toString());
			Long count1=(Long)query.uniqueResult();
			count=(int)(long)count1;
			
		}
		catch(Exception e)
		{
			throw new ApplicationException(e);
		}
		finally
		{
			if(session!=null)
			{
				session.flush();
			}
		}
		return count;
	}
	
	public List<ExamSupplementaryImprovementApplicationBO> getStudentsChallanStatusOnCourseForSupplExam(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<ExamSupplementaryImprovementApplicationBO> list=null;
		Map<Integer,String> m=new HashMap<Integer, String>();
		try{
			session = HibernateUtil.getSession();
			//Query hqlQuery = session.createQuery(" from AdmAppln a where  a.course.program.programType.id="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and a.mode='CHALLAN' and (a.isChallanRecieved=0 or a.isChallanRecieved is null)");
			Query hqlQuery = session.createQuery("from ExamSupplementaryImprovementApplicationBO e where  e.classes.id="+Integer.parseInt(ddForm.getClasses())+" and e.examDefinitionBO.id="+ddForm.getExamid()+" and e.challanVerified=0 and (e.isAppearedTheory=1 or e.isAppearedPractical=1) group by e.studentId ");
			//neft check
			list=hqlQuery.list();
			Iterator<ExamSupplementaryImprovementApplicationBO> i=list.iterator();
			 
			ddForm.setStudentMap(m);
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	
	public boolean updateChallanStatusOnCourseForSupplExam(DDStatusForm ddForm)	throws Exception {

		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria=null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			List<ExamSupplementaryImprovementApplicationBO> supplList=null; 
			if (list != null && !list.isEmpty()) {
				Iterator<DDStatusTO> itr = list.iterator();
				int count = 0;
				while (itr.hasNext()) {
					DDStatusTO  ddTo = (DDStatusTO) itr.next();
					if (ddTo.getChecked()!=null && ddTo.getChecked().equalsIgnoreCase("on")){
						criteria =session.createCriteria(ExamSupplementaryImprovementApplicationBO.class);
						Criterion c1= Restrictions.eq("examId", ddForm.getExamid());
						Criterion c2=Restrictions.eq("studentId", Integer.parseInt(ddTo.getStudentId()));
						Criterion c3=Restrictions.eq("isAppearedTheory", 1);
						Criterion c4=Restrictions.eq("isAppearedPractical", 1);
						Criterion c5=Restrictions.and(c1, c2);
						Criterion c6=Restrictions.or(c3, c4);
						Criterion c7=Restrictions.and(c5, c6);
						criteria.add(c7);
						supplList=criteria.list();
						if(supplList!=null && !supplList.isEmpty()){
							for(ExamSupplementaryImprovementApplicationBO bo:supplList){
									bo.setModifiedBy(ddForm.getUserId());
									bo.setLastModifiedDate(new Date());
									bo.setChallanVerified(true);
									bo.setRemarks(ddTo.getRemarks());
									bo.setAmount(ddTo.getFees());
									transaction.begin();
									session.update(bo);	
							}
						}
						transaction.commit();
						isAdded = true;
					}
				}
			}
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;

	}
	
	public List<ExamSupplementaryApplication> getStudentsChallanDtailsOnDateForSupplExam(DDStatusForm ddForm) throws Exception {
		Session session = null;
		List<ExamSupplementaryApplication> list=null;
		
		try{
			session = HibernateUtil.getSession();
			Query hqlQuery = session.createQuery("	from ExamSupplementaryApplication e where e.challanVerified=0 "+
			" and e.challanNo in ( select cd.challanNo from ChallanUploadDataExam cd where cd.challanDate=:cdate)");
			//neft check		
			hqlQuery.setString("cdate",CommonUtil.ConvertStringToSQLDate(ddForm.getRecievedChallanDate()).toString());
			list=hqlQuery.list();
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return list;

		
		
	}
	
	public boolean updateChallanUploadProcessForSupplExam(DDStatusForm ddForm)	throws Exception {

		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<DDStatusTO> list = ddForm.getDdStatusList();
			ExamSupplementaryApplication bo=null;
			if (list != null && !list.isEmpty()) {
				Iterator<DDStatusTO> itr = list.iterator();
				int count = 0;
				while (itr.hasNext()) {

					DDStatusTO  ddTo = (DDStatusTO) itr.next();
					bo =(ExamSupplementaryApplication) session.get(ExamSupplementaryApplication.class,ddTo.getStuRegAppId() );
					bo.setModifiedBy(ddForm.getUserId());
					bo.setLastModifiedDate(new Date());
					bo.setChallanVerified(true);

					transaction.begin();
					session.update(bo);	
					if (++count % 20 == 0) {
						session.flush();
						session.clear();
					}
				}
				transaction.commit();
				isAdded = true;
			}

		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
		log.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;

	}
	
	public Student getStudent(int studId) throws Exception {
		Session session = null;
		Student student=null;
		try{
			session = HibernateUtil.getSession();
			//Query hqlQuery = session.createQuery(" from AdmAppln a where  a.course.program.programType.id="+ddForm.getProgramTypeId()+" and a.appliedYear="+Integer.parseInt(ddForm.getAcademicYear())+" and a.date=:cdate and a.mode='CHALLAN' and (a.isChallanRecieved=0 or a.isChallanRecieved is null)");
			Query hqlQuery = session.createQuery("from Student s where s.id="+studId);
			//neft check
			student=(Student)hqlQuery.uniqueResult();
			
		}catch (Exception e) {
			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
		return student;

		
		
	}

}
