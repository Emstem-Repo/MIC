package com.kp.cms.transactionsimpl.exam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.FeeBillNumber;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ChallanUploadDataExam;
import com.kp.cms.bo.exam.ExamPublishHallTicketMarksCardBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamRevaluationApp;
import com.kp.cms.bo.exam.ExamRevaluationApplication;
import com.kp.cms.bo.exam.ExamRevaluationApplicationNew;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.OnlineBillNumber;
import com.kp.cms.bo.exam.OnlinePaymentReciepts;
import com.kp.cms.bo.exam.RegularExamApplicationPGIDetails;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.SubjectUtilBO;
import com.kp.cms.bo.exam.SupplementaryExamApplicationPGIDetails;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BillGenerationException;
import com.kp.cms.forms.admission.OnlineApplicationForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

/**
 * @author user
 *
 */
public class NewSupplementaryImpApplicationTransactionImpl implements
		INewSupplementaryImpApplicationTransaction {
	/**
	 * Singleton object of NewSupplementaryImpApplicationTransactionImpl
	 */
	private static volatile NewSupplementaryImpApplicationTransactionImpl newSupplementaryImpApplicationTransactionImpl = null;
	private static final Log log = LogFactory.getLog(NewSupplementaryImpApplicationTransactionImpl.class);
	private NewSupplementaryImpApplicationTransactionImpl() {
		
	}
	/**
	 * return singleton object of NewSupplementaryImpApplicationTransactionImpl.
	 * @return
	 */
	public static NewSupplementaryImpApplicationTransactionImpl getInstance() {
		if (newSupplementaryImpApplicationTransactionImpl == null) {
			newSupplementaryImpApplicationTransactionImpl = new NewSupplementaryImpApplicationTransactionImpl();
		}
		return newSupplementaryImpApplicationTransactionImpl;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction#saveSupplementarys(java.util.List)
	 */
	@Override
	public boolean saveSupplementarys( List<StudentSupplementaryImprovementApplication> boList) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(boList!=null && !boList.isEmpty()){
				Iterator<StudentSupplementaryImprovementApplication> itr=boList.iterator();
				while (itr.hasNext()) {
					StudentSupplementaryImprovementApplication bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;
		}
	}
	@Override
	public boolean deleteSupplementaryImpApp(String query) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.createQuery("delete "+query).executeUpdate();
			transaction.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;
		}
	}
	@Override
	public String getOldRegNo(int id, Integer schemeNo) throws Exception {
		Session session = null;
		String regNo ="";
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("select e1.registerNo from ExamStudentDetentionRejoinDetails e1 where e1.schemeNo= (select min(e.schemeNo) from ExamStudentDetentionRejoinDetails e left join e.rejoinClassSchemewise classSche left join classSche.classes c where e.student.id = e1.student.id " +
					"and ((c.termNumber <> "+schemeNo+") " +
					"or (c.termNumber is null)) and e.schemeNo>="+schemeNo+") and e1.student.id="+id+"  order by e1.student.id ").setMaxResults(1);
			regNo = (String)query.uniqueResult();
			return regNo;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction#saveOnlinePaymentReciept(com.kp.cms.bo.exam.OnlinePaymentReciepts)
	 */
	public void updateAndGenerateRecieptNoOnlinePaymentReciept(
			OnlinePaymentReciepts onlinePaymentReciepts) throws Exception {
		Session session = null;
		Transaction transaction = null;
		Transaction tx = null;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			
			Query query = session.createQuery("from OnlineBillNumber o where o.pcFinancialYear.id = :year and o.isActive = 1").setInteger("year",onlinePaymentReciepts.getPcFinancialYear().getId());
			 if(query.list() == null || query.list().size() == 0) {
				 throw new BillGenerationException();
			 }
			 OnlineBillNumber feeBillNumber = (OnlineBillNumber)query.list().get(0);
			 onlinePaymentReciepts.setRecieptNo(Integer.parseInt(feeBillNumber.getCurrentBillNo()));
			 int feeBillNo = Integer.parseInt(feeBillNumber.getCurrentBillNo());
			 Transaction tx2=session.beginTransaction();
			 feeBillNo=feeBillNo+1;
			 feeBillNumber.setCurrentBillNo(String.valueOf(feeBillNo));
			 session.update(feeBillNumber);
			 tx2.commit();
			 
			 // code written by balaji
			boolean isExist=false;
			do{
				List<FeePayment> bos=session.createQuery("from OnlinePaymentReciepts f where f.recieptNo='"+feeBillNo+"' and f.pcFinancialYear.id="+onlinePaymentReciepts.getPcFinancialYear().getId()).list();
				if(bos!=null && !bos.isEmpty()){
					isExist=true;
					feeBillNo=feeBillNo+1;
					feeBillNumber.setCurrentBillNo(String.valueOf(feeBillNo));
				}else{
					isExist=false;
				}
			}while(isExist);
			tx=session.beginTransaction();
			tx.begin();
			feeBillNumber.setCurrentBillNo(String.valueOf(feeBillNo));
			session.merge(feeBillNumber);
			tx.commit();
			//Old Code
			Transaction tx1 = session.beginTransaction();
			tx1 = session.beginTransaction();
			tx1.begin();
			onlinePaymentReciepts.setRecieptNo(Integer.parseInt(feeBillNumber.getCurrentBillNo()));
			 
			//raghu added from mounts
			onlinePaymentReciepts.setTransactionDate(new Date());
			
			
			session.merge(onlinePaymentReciepts);
			 tx1.commit();
			 transaction = session.beginTransaction();
				transaction.begin();
			session.merge(onlinePaymentReciepts);
			transaction.commit();
			session.flush();
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
		}
	}
	
	
	//raghu write for regular exam
	
	@Override
	public List getSubjectsListForStudent(Student student, int academicYear)
			throws Exception {
		Session session = null;
		List subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			int applnId = student.getAdmAppln().getId();
			String subQuery =   "select distinct esb.subjectUtilBO,esb.examSubjectSectionMasterBO.name from ExamSubDefinitionCourseWiseBO esb where esb.academicYear="+academicYear+" and esb.courseId="+student.getAdmAppln().getCourseBySelectedCourseId().getId()+" and esb.schemeNo= " +student.getClassSchemewise().getClasses().getTermNumber()+
					" and esb.academicYear="+student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()+
					" and esb.subjectUtilBO.id in(select sub.subject.id from SubjectGroupSubjects sub where sub.subjectGroup.id in  " +
					" (select app.subjectGroup.id from ApplicantSubjectGroup app where app.admAppln.id = "+student.getAdmAppln().getId()+") " +
					" and sub.isActive = 1) order by esb.subjectOrder";
			
			Query subjectQuery = session.createQuery(subQuery);
			subject = subjectQuery.list();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return subject;
	}
	
	
		
	@Override
	public boolean checkAttendanceAvailability(int studentid, int classid) throws Exception {
		Session session = null;
		List subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from Attendance a inner join a.attendanceClasses acls inner join a.attendanceStudents astu where acls.classSchemewise.classes.id="+classid+" and astu.student.id="+studentid ; 
			Query subjectQuery = session.createQuery(subQuery);
			subject = subjectQuery.list();
			if(subject.size()!=0){
				dup=true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return dup;
	}
	
	
	@Override
	public boolean checkCondonationAvailability(int studentid, int classid) throws Exception {
		Session session = null;
		List subject = null;
		boolean ava=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from AttendanceCondonation ac where ac.isActive=1 and ac.student.id="+studentid+" and ac.classes.id="+classid ; 
			Query subjectQuery = session.createQuery(subQuery);
			subject = subjectQuery.list();
			if(subject.size()!=0){
				ava=true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return ava;
	}
	
	
	@Override
	public boolean checkDuplication(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		ExamRegularApplication subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from ExamRegularApplication er where er.student.id="+form.getStudentObj().getId()  ; 
			if(!form.getPreviousExam())
			subQuery = subQuery+" and er.classes.id="+form.getStudentObj().getClassSchemewise().getClasses().getId();
			else
				subQuery = subQuery+"and er.classes.id="+form.getPrevClassId();
			Query subjectQuery = session.createQuery(subQuery);
			subject = (ExamRegularApplication) subjectQuery.uniqueResult();
			if(subject!=null){
				String trdate=CommonUtil.ConvertStringToDate1(subject.getCreatedDate().toString());
				form.setApplicationDate(trdate);
				dup=true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return dup;
	}
	
	@Override
	public boolean addAppliedStudent(ExamRegularApplication obj)throws Exception {
		Session session = null;
		List subject = null;
		boolean add=false;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			
			transaction = session.beginTransaction();
			transaction.begin();
			session.save(obj);
			transaction.commit();
			add=true;
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return add;
	}
	@Override
	public RegularExamFees getRegularExamFees(
			NewSupplementaryImpApplicationForm form)
			throws Exception {
		Session session = null;
		RegularExamFees fees = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String queryString ="from RegularExamFees rf where rf.classes.id="+form.getStudentObj().getClassSchemewise().getClasses().getId()+
			" and academicYear="+form.getAcademicYear();
			Query query = session.createQuery(queryString);
			fees = (RegularExamFees) query.uniqueResult();
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return fees;
	}
	
	@Override
	public synchronized String generateCandidateRefNo(RegularExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form) throws Exception {
		log.info("Entered into generateCandidateRefNo-AdmissionFormTransactionImpl");
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="REG"+String.valueOf(savedId);
				bo.setCandidateRefNo(candidateRefNo);
				session.update(bo);
				transaction.commit();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-NewSupplementaryImpApplicationTransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-NewSupplementaryImpApplicationTransactionImpl");
		return candidateRefNo;
	}
	
	@Override
	public boolean updateReceivedStatus(RegularExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form)
			throws Exception {

		log.info("Entered into updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		form.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from RegularExamApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+form.getApplicationAmount()+" and c.txnStatus='Pending'";
				RegularExamApplicationPGIDetails candidatePgiBo=(RegularExamApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setPgType(bo.getPgType());
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());				
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					form.setIsTnxStatusSuccess(true);
					form.setPaymentSuccess(true);
				}
				
				form.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				form.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
	
	
	}
	
	@Override
	public boolean addAppliedStudentToChallan(ChallanUploadDataExam obj)throws Exception {
		Session session = null;
		List subject = null;
		boolean add=false;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			
			transaction = session.beginTransaction();
			transaction.begin();
			session.save(obj);
			transaction.commit();
			add=true;
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return add;
	}
	
	public boolean checkSubmitRegApp(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		List subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="select e.id from exam_regular_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
			" and DATE(e.created_date)<=(select DATE(p.download_end_date) from EXAM_publish_hall_ticket_marks_card p where p.class_id="+form.getStudentObj().getClassSchemewise().getClasses().getId()+" and p.exam_id="+form.getExamId()+")";
			Query subjectQuery = session.createSQLQuery(subQuery);
			subject = subjectQuery.list();
			if(subject.size()!=0){
				dup=true;
			}
			else
			{
				subQuery ="select e.id from exam_regular_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
				" and DATE(e.created_date)<=(select DATE(p.date_fine_extended) from EXAM_publish_hall_ticket_marks_card p where p.class_id="+form.getStudentObj().getClassSchemewise().getClasses().getId()+" and p.exam_id="+form.getExamId()+")";
				subjectQuery = session.createSQLQuery(subQuery);
				subject = subjectQuery.list();
				if(subject.size()!=0){
					form.setIsFine(true);
				}
				else
				{
					subQuery ="select e.id from exam_regular_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
					" and DATE(e.created_date)<=(select DATE(p.date_superfine_extended) from EXAM_publish_hall_ticket_marks_card p where p.class_id="+form.getStudentObj().getClassSchemewise().getClasses().getId()+" and p.exam_id="+form.getExamId()+")";
					subjectQuery = session.createSQLQuery(subQuery);
					subject = subjectQuery.list();
					if(subject.size()!=0){
						form.setIsSuperFine(true);
				}
				}
		} 
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return dup;
	}
	
	public boolean checkSubmitSuppApp(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		List subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="select e.id from exam_supplementary_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
			" and DATE(e.created_date)<=(select DATE(p.end_date) from publish_supplementary_imp_application p where p.class_id="+form.getPrevClassId()+" and p.exam_id="+form.getExamId()+")";
			Query subjectQuery = session.createSQLQuery(subQuery);
			subject = subjectQuery.list();
			if(subject.size()!=0){
				dup=true;
				
			}
			else
			{
				subQuery ="select e.id from exam_supplementary_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
				" and DATE(e.created_date)<=(select DATE(p.date_fine_extended) from publish_supplementary_imp_application p where p.class_id="+form.getPrevClassId()+" and p.exam_id="+form.getExamId()+")";
				subjectQuery = session.createSQLQuery(subQuery);
				subject = subjectQuery.list();
				if(subject.size()!=0){
					form.setIsFine(true);
					form.setIsSuperFine(false);
				}
				else
				{
					subQuery ="select e.id from exam_supplementary_app e where e.exam_id="+form.getExamId()+" and e.student_id="+form.getStudentId()+
					" and DATE(e.created_date)<=(select DATE(p.date_superfine_extended) from publish_supplementary_imp_application p where p.class_id="+form.getPrevClassId()+" and p.exam_id="+form.getExamId()+")";
					subjectQuery = session.createSQLQuery(subQuery);
					subject = subjectQuery.list();
					if(subject.size()!=0){
						form.setIsSuperFine(true);
						form.setIsFine(false);
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return dup;
	}
	
	public boolean checkDuplicationForSuppl(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		ExamSupplementaryApplication subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from ExamSupplementaryApplication er where er.student.id="+form.getStudentObj().getId()+" and er.classes.id="+form.getPrevClassId() ; 
			Query subjectQuery = session.createQuery(subQuery);
			subject = (ExamSupplementaryApplication) subjectQuery.uniqueResult();
			if(subject!=null){
				String trdate=CommonUtil.ConvertStringToDate1(subject.getCreatedDate().toString());
				form.setApplicationDate(trdate);
				dup=true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return dup;
	}
	
	public boolean updateReceivedStatusForSuppl(SupplementaryExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form)
	throws Exception {

		log.info("Entered into updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		form.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from SupplementaryExamApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+form.getApplicationAmount()+" and c.txnStatus='Pending'";
				SupplementaryExamApplicationPGIDetails candidatePgiBo=(SupplementaryExamApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setPgType(bo.getPgType());
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());	
					Classes classes = new Classes();
					classes.setId(Integer.parseInt(form.getPrevClassId()));
					candidatePgiBo.setClasses(classes);
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					form.setIsTnxStatusSuccess(true);
					form.setPaymentSuccess(true);
				}

				form.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				form.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;

			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}

		finally{

			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
}
	
	public boolean addAppliedStudentForSuppl(ExamSupplementaryApplication obj)throws Exception {
		Session session = null;
		List subject = null;
		boolean add=false;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();

			transaction = session.beginTransaction();
			transaction.begin();
			session.save(obj);
			transaction.commit();
			add=true;

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return add;
	}
	
	public synchronized String generateCandidateRefNoForSuppl(SupplementaryExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form) throws Exception {
		log.info("Entered into generateCandidateRefNo-AdmissionFormTransactionImpl");
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="SUP"+String.valueOf(savedId);
				bo.setCandidateRefNo(candidateRefNo);
				session.update(bo);
				transaction.commit();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-NewSupplementaryImpApplicationTransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-NewSupplementaryImpApplicationTransactionImpl");
		return candidateRefNo;
	}
	
	public ExamSupplementaryApplication getApplicationForSuppl(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		ExamSupplementaryApplication appln = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from ExamSupplementaryApplication er where er.student.id="+form.getStudentObj().getId()+" and er.classes.id="+form.getPrevClassId() ; 
			Query subjectQuery = session.createQuery(subQuery);
			appln = (ExamSupplementaryApplication) subjectQuery.uniqueResult();
		
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return appln;
	}
	
	public boolean getPrevClasssIdFromRegularApp(int stuId, int prevClassId)throws Exception {
		Session session = null;
		List<ExamRegularApplication>  regApps = null;
		ExamRegularApplication regApp = null;
		boolean applied=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from ExamRegularApplication er where er.student.id="+stuId+" and er.isApplied=1 and er.classes.id="+prevClassId ; 
			Query subjectQuery = session.createQuery(subQuery);
			regApps = subjectQuery.list();
			if (regApp!=null) {
				regApp=regApps.get(0);
			}
			
			
			
			if(regApp!=null && regApp.getIsApplied())
				applied=true;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return applied;
	}
	
	public List getSubjectsListForStudentPrevClass(Student student, int academicYear) throws Exception {
		Session session = null;
		List subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			int applnId = student.getAdmAppln().getId();
			int termNo = student.getClassSchemewise().getClasses().getTermNumber()-1;
			String subQuery =   "select esb.subjectUtilBO,esb.examSubjectSectionMasterBO.name from ExamSubDefinitionCourseWiseBO esb where  esb.courseId="+student.getAdmAppln().getCourseBySelectedCourseId().getId()+" and esb.schemeNo= " +termNo+
				" and esb.academicYear = " + academicYear + " and esb.subjectUtilBO.id in(select subGrp.subject.id from StudentSubjectGroupHistory s" +
				" join s.subjectGroup.subjectGroupSubjectses subGrp " +
				" where s.schemeNo="+termNo+" and s.student.id="+student.getId()+" and subGrp.isActive=1 and subGrp.subject.isActive=1)";
			Query subjectQuery = session.createQuery(subQuery);
			subject = subjectQuery.list();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return subject;
	}
	
	public boolean addAppliedStudentsForReg(List<ExamRegularApplication> regappList,int  examId,int classId)throws Exception {
		Session session = null;
		List<Integer> stuList = null;
		boolean add=false;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			
			transaction = session.beginTransaction();
			transaction.begin();
			String existingStuQuery= "select er.student_id from exam_regular_app er where er.class_id="+classId+" and er.exam_id="+examId;
			Query stuQuery = session.createSQLQuery(existingStuQuery);
			stuList = stuQuery.list();
			
			for (ExamRegularApplication app : regappList) {
				if(!stuList.contains(app.getStudent().getId()))
					session.save(app);
			}
			
		/*	if(prevExamRegularApplicationList.size()>0){
				for (ExamRegularApplication app : prevExamRegularApplicationList) {
					if(!stuList.contains(app.getStudent().getId()))
						session.save(app);
				}
			}*/
			
		
			transaction.commit();
			add=true;
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return add;
	}
	
// for revaluation by ashwini
	
	public List getSubjectsListForRevaluation(Student student,int examId,int classid)
	throws Exception {
		Session session = null;
		List subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			int applnId = student.getAdmAppln().getId();
			String subQuery = 			
				" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
				" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
				" classes.name,classes.term_number,personal_data.first_name," +
				" EXAM_student_overall_internal_mark_details.student_id as studID,  " +
				" EXAM_student_overall_internal_mark_details.class_id as classID," +
				" EXAM_student_overall_internal_mark_details.subject_id as subID,subject.code as subCode," +
				" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +
				//" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical','Theory')) as subType," +
				" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id, " + 
				" EXAM_student_overall_internal_mark_details.theory_total_mark," +
				" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
				" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
				" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
				" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
				" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
				" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
				" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
				" EXAM_publish_exam_results.publish_date," +
				" EXAM_sub_definition_coursewise.subject_order as subject_order," +
				" classes.term_number, "+
				" EXAM_subject_rule_settings.theory_ese_entered_max_mark "+

				" from EXAM_student_overall_internal_mark_details" +

				" LEFT JOIN student on EXAM_student_overall_internal_mark_details.student_id = student.id" +
				" LEFT JOIN adm_appln on student.adm_appln_id = adm_appln.id" +
				" LEFT JOIN personal_data ON adm_appln.personal_data_id = personal_data.id" +
				" LEFT JOIN subject on EXAM_student_overall_internal_mark_details.subject_id = subject.id" +
				" LEFT JOIN subject_type ON subject.subject_type_id = subject_type.id" +
				" LEFT JOIN EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id" +
				" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id" +
				" LEFT JOIN classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id" +
				" LEFT JOIN class_schemewise ON class_schemewise.class_id = classes.id" +
				" LEFT JOIN curriculum_scheme_duration on class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
				" LEFT JOIN curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id" +
				" inner JOIN EXAM_student_final_mark_details ON EXAM_student_final_mark_details.class_id = classes.id" +
				" and EXAM_student_final_mark_details.exam_id = EXAM_definition.id" +
				" and EXAM_student_final_mark_details.student_id = student.id" +
				" and EXAM_student_final_mark_details.subject_id = subject.id" +
				" LEFT JOIN EXAM_subject_rule_settings ON EXAM_subject_rule_settings.subject_id = subject.id " +
				" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year " +
				" and EXAM_subject_rule_settings.course_id = classes.course_id " +
				" and EXAM_subject_rule_settings.scheme_no = classes.term_number " +
				" LEFT JOIN EXAM_publish_exam_results ON EXAM_publish_exam_results.class_id = classes.id " +
				" and EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
				" LEFT JOIN EXAM_sub_definition_coursewise ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
				" and EXAM_sub_definition_coursewise.course_id = classes.course_id " +
				" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number " +
				" and EXAM_sub_definition_coursewise.academic_year = curriculum_scheme_duration.academic_year " +
				" LEFT JOIN EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id " +
				" LEFT JOIN course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id " +

				" where  EXAM_student_overall_internal_mark_details.exam_id =:examId " +
				" and EXAM_student_overall_internal_mark_details.class_id =:classId and student.id =:studentId" +
				" and (subject.is_theory_practical='P' || subject.is_theory_practical='T')" +

				" group by student.id,EXAM_student_overall_internal_mark_details.exam_id,subject.id " +

				"  UNION ALL" +

				" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
				" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
				" classes.name,classes.term_number,personal_data.first_name," +
				" EXAM_student_overall_internal_mark_details.student_id as studID,  " +
				" EXAM_student_overall_internal_mark_details.class_id as classID," +
				" EXAM_student_overall_internal_mark_details.subject_id as subID,subject.code as subCode," +
				" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +
				//" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical','Theory')) as subType," +
				" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id,  " +
				" EXAM_student_overall_internal_mark_details.theory_total_mark," +
				" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
				" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
				" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
				" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
				" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
				" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
				" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
				" EXAM_publish_exam_results.publish_date," +
				" EXAM_sub_definition_coursewise.subject_order as subject_order," +
				" classes.term_number, "+
				" EXAM_subject_rule_settings.theory_ese_entered_max_mark "+
				" from EXAM_student_overall_internal_mark_details" +

				" LEFT JOIN student on EXAM_student_overall_internal_mark_details.student_id = student.id" +
				" LEFT JOIN adm_appln on student.adm_appln_id = adm_appln.id" +
				" LEFT JOIN personal_data ON adm_appln.personal_data_id = personal_data.id" +
				" LEFT JOIN subject on EXAM_student_overall_internal_mark_details.subject_id = subject.id" +
				" LEFT JOIN subject_type ON subject.subject_type_id = subject_type.id" +
				" LEFT JOIN EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id" +
				" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id" +
				" LEFT JOIN classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id" +
				" LEFT JOIN class_schemewise ON class_schemewise.class_id = classes.id" +
				" LEFT JOIN curriculum_scheme_duration on class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
				" LEFT JOIN curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id" +
				" inner JOIN EXAM_student_final_mark_details ON EXAM_student_final_mark_details.class_id = classes.id" +
				" and EXAM_student_final_mark_details.exam_id = EXAM_definition.id" +
				" and EXAM_student_final_mark_details.student_id = student.id" +
				" and EXAM_student_final_mark_details.subject_id = subject.id" +
				" LEFT JOIN EXAM_subject_rule_settings ON EXAM_subject_rule_settings.subject_id = subject.id " +
				" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year " +
				" and EXAM_subject_rule_settings.course_id = classes.course_id " +
				" and EXAM_subject_rule_settings.scheme_no = classes.term_number " +
				" LEFT JOIN EXAM_publish_exam_results ON EXAM_publish_exam_results.class_id = classes.id " +
				" and EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
				" LEFT JOIN EXAM_sub_definition_coursewise ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
				" and EXAM_sub_definition_coursewise.course_id = classes.course_id " +
				" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number " +
				" and EXAM_sub_definition_coursewise.academic_year = curriculum_scheme_duration.academic_year " +
				" LEFT JOIN EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id " +
				" LEFT JOIN course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id " +

				" where  subject.is_theory_practical='B' and EXAM_student_overall_internal_mark_details.exam_id =:examId" +
				" and EXAM_student_overall_internal_mark_details.class_id =:classId and student.id =:studentId" +


				" group by student.id,EXAM_student_overall_internal_mark_details.exam_id,subject.id  order by subject_order" ;

			Query subjectQuery = session.createSQLQuery(subQuery);
			subjectQuery.setInteger("examId", examId);
			subjectQuery.setInteger("classId", classid);
			subjectQuery.setInteger("studentId", student.getId());

			subject = subjectQuery.list();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return subject;
	}
	
	// for supplementary
	public List getSupplSubjectsListForRevaluation(Student student,int examId,int classid)
	throws Exception {
		Session session = null;
		List subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			int applnId = student.getAdmAppln().getId();
			String subQuery = 			
				" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
				" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
				" classes.name,classes.term_number,personal_data.first_name," +
				" EXAM_student_overall_internal_mark_details.student_id as studID,  " +
				" EXAM_student_overall_internal_mark_details.class_id as classID," +
				" EXAM_student_overall_internal_mark_details.subject_id as subID,subject.code as subCode," +
				" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +

				" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id, " + 
				" EXAM_student_overall_internal_mark_details.theory_total_mark," +
				" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
				" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
				" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
				" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
				" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
				" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
				" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
				" EXAM_publish_exam_results.publish_date," +
				" EXAM_sub_definition_coursewise.subject_order as subject_order," +
				" classes.term_number ,"+
				" EXAM_subject_rule_settings.theory_ese_entered_max_mark " +
				
				"  FROM EXAM_supplementary_improvement_application " +
				"       LEFT JOIN student " +
				"          ON (EXAM_supplementary_improvement_application.student_id = " +
				"                 student.id) " +
				"       LEFT JOIN classes " +
				"          ON (EXAM_supplementary_improvement_application.class_id = " +
				"                 classes.id) " +
				"       LEFT JOIN subject " +
				"          ON (EXAM_supplementary_improvement_application.subject_id = " +
				"                 subject.id) " +
				"       LEFT JOIN EXAM_definition " +
				"          ON (EXAM_supplementary_improvement_application.exam_id = " +
				"                 EXAM_definition.id) " +
				"       LEFT JOIN adm_appln " +
				"          ON student.adm_appln_id = adm_appln.id " +
				"       LEFT JOIN personal_data " +
				"          ON adm_appln.personal_data_id = personal_data.id " +
				"       LEFT JOIN subject_type " +
				"          ON subject.subject_type_id = subject_type.id " +
				"       LEFT JOIN EXAM_student_overall_internal_mark_details " +
				"          ON (EXAM_student_overall_internal_mark_details.student_id = " +
				"                 student.id) " +
				"             AND EXAM_student_overall_internal_mark_details.subject_id = " +
				"                    subject.id " +
				"             AND EXAM_student_overall_internal_mark_details.class_id = " +
				"                    classes.id " +
				"       LEFT JOIN class_schemewise " +
				"          ON class_schemewise.class_id = classes.id " +
				"       LEFT JOIN curriculum_scheme_duration " +
				"          ON class_schemewise.curriculum_scheme_duration_id = " +
				"                curriculum_scheme_duration.id " +
				"       LEFT JOIN curriculum_scheme " +
				"          ON curriculum_scheme_duration.curriculum_scheme_id = " +
				"                curriculum_scheme.id " +
				"       LEFT JOIN course_scheme " +
				"          ON curriculum_scheme.course_scheme_id = course_scheme.id " +
				"       LEFT JOIN EXAM_student_final_mark_details " +
				"          ON (EXAM_student_final_mark_details.class_id = classes.id) " +
				"             AND (EXAM_student_final_mark_details.exam_id = " +
				"                     EXAM_definition.id) " +
				"             AND (EXAM_student_final_mark_details.student_id = student.id) " +
				"             AND (EXAM_student_final_mark_details.subject_id = subject.id) " +
				"             AND (EXAM_student_final_mark_details.exam_id = EXAM_definition.id) "+
				"       LEFT JOIN EXAM_internal_mark_supplementary_details " +
				"          ON (EXAM_internal_mark_supplementary_details.student_id = " +
				"                 student.id) " +
				"             AND (EXAM_internal_mark_supplementary_details.subject_id = " +
				"                     subject.id) " +
				"             AND (EXAM_internal_mark_supplementary_details.class_id = " +
				"                     classes.id) " +

				"       LEFT JOIN EXAM_subject_rule_settings " +
				"          ON EXAM_subject_rule_settings.subject_id = subject.id " +
				"             AND EXAM_subject_rule_settings.academic_year = " +
				"                    curriculum_scheme_duration.academic_year " +
				"             AND EXAM_subject_rule_settings.course_id = classes.course_id " +
				"             AND EXAM_subject_rule_settings.scheme_no = classes.term_number " +
				"       LEFT JOIN EXAM_publish_exam_results " +
				"          ON EXAM_publish_exam_results.class_id = classes.id " +
				"             AND EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
				"       LEFT JOIN EXAM_sub_definition_coursewise " +
				"          ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
				"             AND EXAM_sub_definition_coursewise.course_id = classes.course_id " +
				"             AND EXAM_sub_definition_coursewise.scheme_no = " +
				"                    classes.term_number " +
				"             AND EXAM_sub_definition_coursewise.academic_year = " +
				"                    curriculum_scheme_duration.academic_year " +
				"       LEFT JOIN EXAM_subject_sections " +
				"          ON EXAM_sub_definition_coursewise.subject_section_id = " +
				"                EXAM_subject_sections.id " +
				" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id "+		
				" WHERE ((EXAM_supplementary_improvement_application.is_supplementary=1  AND ( (EXAM_supplementary_improvement_application.is_appeared_practical = 1) " +
				"	       OR (EXAM_supplementary_improvement_application.is_appeared_theory = 1)) " +
				"	      AND ((EXAM_supplementary_improvement_application.is_failed_practical = " +
				"	              1) " +
				"	           OR (EXAM_supplementary_improvement_application.is_failed_theory = " +
				"	                  1))) or (EXAM_supplementary_improvement_application.is_improvement=1" +
				"										  AND ( (EXAM_supplementary_improvement_application.is_appeared_practical = 1) " +
				"	        OR (EXAM_supplementary_improvement_application.is_appeared_theory = 1)))) " +
				
				"	      AND EXAM_supplementary_improvement_application.exam_id =" +examId +
				"       AND EXAM_subject_rule_settings.academic_year = " +
				"              curriculum_scheme_duration.academic_year " +
				"       AND classes.name = (SELECT name " +
				"                             FROM classes " +
				"                            WHERE id ="+classid+") " +
			
				"       AND student.id=" +student.getId()+
				"       AND if(subject.is_theory_practical = 'B', " +
				"              ((EXAM_supplementary_improvement_application.is_appeared_theory =1) " +
				"               OR (EXAM_supplementary_improvement_application.is_failed_theory = 1)), TRUE) " +
				" GROUP BY student.id, EXAM_student_final_mark_details.exam_id, subject.id " +


				"  UNION ALL" +

				" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
				" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
				" classes.name,classes.term_number,personal_data.first_name," +
				" EXAM_student_overall_internal_mark_details.student_id as studID,  " +
				" EXAM_student_overall_internal_mark_details.class_id as classID," +
				" EXAM_student_overall_internal_mark_details.subject_id as subID,subject.code as subCode," +
				" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +

				" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id,  " +
				" EXAM_student_overall_internal_mark_details.theory_total_mark," +
				" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
				" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
				" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
				" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
				" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
				" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
				" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
				" EXAM_publish_exam_results.publish_date," +
				" EXAM_sub_definition_coursewise.subject_order as subject_order," +
				" classes.term_number, "+
				" EXAM_subject_rule_settings.theory_ese_entered_max_mark " +
				"  FROM EXAM_supplementary_improvement_application " +
				"       LEFT JOIN student " +
				"          ON (EXAM_supplementary_improvement_application.student_id = " +
				"                 student.id) " +
				"       LEFT JOIN classes " +
				"          ON (EXAM_supplementary_improvement_application.class_id = " +
				"                 classes.id) " +
				"       LEFT JOIN subject " +
				"          ON (EXAM_supplementary_improvement_application.subject_id = " +
				"                 subject.id) " +
				"       LEFT JOIN EXAM_definition " +
				"          ON (EXAM_supplementary_improvement_application.exam_id = " +
				"                 EXAM_definition.id) " +
				"       LEFT JOIN adm_appln " +
				"          ON student.adm_appln_id = adm_appln.id " +
				"       LEFT JOIN personal_data " +
				"          ON adm_appln.personal_data_id = personal_data.id " +
				"       LEFT JOIN subject_type " +
				"          ON subject.subject_type_id = subject_type.id " +
				"       LEFT JOIN EXAM_student_overall_internal_mark_details " +
				"          ON (EXAM_student_overall_internal_mark_details.student_id = " +
				"                 student.id) " +
				"             AND EXAM_student_overall_internal_mark_details.subject_id = " +
				"                    subject.id " +
				"             AND EXAM_student_overall_internal_mark_details.class_id = " +
				"                    classes.id " +
				"       LEFT JOIN class_schemewise " +
				"          ON class_schemewise.class_id = classes.id " +
				"       LEFT JOIN curriculum_scheme_duration " +
				"          ON class_schemewise.curriculum_scheme_duration_id = " +
				"                curriculum_scheme_duration.id " +
				"       LEFT JOIN curriculum_scheme " +
				"          ON curriculum_scheme_duration.curriculum_scheme_id = " +
				"                curriculum_scheme.id " +
				"       LEFT JOIN course_scheme " +
				"          ON curriculum_scheme.course_scheme_id = course_scheme.id " +
				"       LEFT JOIN EXAM_student_final_mark_details " +
				"          ON (EXAM_student_final_mark_details.class_id = classes.id) " +
				"             AND (EXAM_student_final_mark_details.exam_id = " +
				"                     EXAM_definition.id) " +
				"             AND (EXAM_student_final_mark_details.student_id = student.id) " +
				"             AND (EXAM_student_final_mark_details.subject_id = subject.id) " +
				"       LEFT JOIN EXAM_internal_mark_supplementary_details " +
				"          ON (EXAM_internal_mark_supplementary_details.student_id = " +
				"                 student.id) " +
				"             AND (EXAM_internal_mark_supplementary_details.subject_id = " +
				"                     subject.id) " +
				"             AND (EXAM_internal_mark_supplementary_details.class_id = " +
				"                     classes.id) " +

				"       LEFT JOIN EXAM_subject_rule_settings " +
				"          ON EXAM_subject_rule_settings.subject_id = subject.id " +
				"             AND EXAM_subject_rule_settings.academic_year = " +
				"                    curriculum_scheme_duration.academic_year " +
				"             AND EXAM_subject_rule_settings.course_id = classes.course_id " +
				"             AND EXAM_subject_rule_settings.scheme_no = classes.term_number " +
				"       LEFT JOIN EXAM_publish_exam_results " +
				"          ON EXAM_publish_exam_results.class_id = classes.id " +
				"             AND EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
				"       LEFT JOIN EXAM_sub_definition_coursewise " +
				"          ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
				"             AND EXAM_sub_definition_coursewise.course_id = classes.course_id " +
				"             AND EXAM_sub_definition_coursewise.scheme_no = " +
				"                    classes.term_number " +
				"             AND EXAM_sub_definition_coursewise.academic_year = " +
				"                    curriculum_scheme_duration.academic_year " +
				"       LEFT JOIN EXAM_subject_sections " +
				"          ON EXAM_sub_definition_coursewise.subject_section_id = " +
				"                EXAM_subject_sections.id " +
				" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id "+		
				" WHERE ((EXAM_supplementary_improvement_application.is_supplementary=1  AND ( (EXAM_supplementary_improvement_application.is_appeared_practical = 1) " +
				"	       OR (EXAM_supplementary_improvement_application.is_appeared_theory = 1)) " +
				"	      AND ((EXAM_supplementary_improvement_application.is_failed_practical = " +
				"	              1) " +
				"	           OR (EXAM_supplementary_improvement_application.is_failed_theory = " +
				"	                  1))) or (EXAM_supplementary_improvement_application.is_improvement=1" +
				"										  AND ( (EXAM_supplementary_improvement_application.is_appeared_practical = 1) " +
				"	        OR (EXAM_supplementary_improvement_application.is_appeared_theory = 1)))) " +
				"	      AND EXAM_supplementary_improvement_application.exam_id =" +examId +
				
				"       AND (subject.is_theory_practical = 'T' or subject.is_theory_practical = 'P') " +
				"       AND EXAM_student_final_mark_details.student_practical_marks " +
				"              IS NOT NULL " +
				"       AND EXAM_subject_rule_settings.academic_year = " +
				"              curriculum_scheme_duration.academic_year " +
				"       AND classes.name = (SELECT name " +
				"                             FROM classes " +
				"                            WHERE id = "+classid+") " +
				
				"       AND student.id=" +student.getId()+
				" GROUP BY student.id, EXAM_student_final_mark_details.exam_id, subject.id " ;

			Query subjectQuery = session.createSQLQuery(subQuery);
			subject = subjectQuery.list();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return subject;
	}
	
	public Boolean getExtendedDateForRevaluation(int classId, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception
	{

		Session session = null;
		Boolean extended = false;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery( "from ExamPublishHallTicketMarksCardBO eb where eb.isActive=1 and eb.publishFor='Revaluation/Scrutiny' and eb.examDefinitionBO.examTypeUtilBO.name='Regular' and eb.classes.id=" +classId +
					" and ((eb.downloadEndDate <'" +CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"')" +
					" and (eb.extendedDate >='"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"'))"); 

			List<ExamPublishHallTicketMarksCardBO> list =query.list();
			if (list != null && !list.isEmpty()){
				newSupplementaryImpApplicationForm.setExtended(true);
				extended=true;
				query = session.createQuery( "from ExamPublishHallTicketMarksCardBO eb where eb.isActive=1 and eb.publishFor='Revaluation/Scrutiny' and eb.examDefinitionBO.examTypeUtilBO.name='Regular' and eb.classes.id=" +classId +
						" and ((eb.extendedFineStartDate <='" +CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"')" +
						" and (eb.extendedFineDate >='"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"'))"); 
				list =query.list();
				if (list != null && !list.isEmpty()){
					newSupplementaryImpApplicationForm.setIsFine(true);
				}
				else
				{
					query = session.createQuery( "from ExamPublishHallTicketMarksCardBO eb where eb.isActive=1 and eb.publishFor='Revaluation/Scrutiny' and eb.examDefinitionBO.examTypeUtilBO.name='Regular' and eb.classes.id=" +classId +
							" and ((eb.extendedSuperFineStartDate <='" +CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"')" +
							" and (eb.extendedSuperFineDate >='"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"'))"); 
					list =query.list();
					if (list != null && !list.isEmpty()){
						newSupplementaryImpApplicationForm.setIsSuperFine(true);
					}

				}
			}
			session.flush();
		} catch (Exception e) {
			session.flush();
			log.error("Error during checking duplicate code.." + e);
			throw new ApplicationException(e);
		}
		return extended;
	}
	
	public ExamRevaluationApp checkDuplicationForRevaluation(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		ExamRevaluationApp subject = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery ="from ExamRevaluationApp er where er.student.id="+form.getStudentId()+" and er.classes.id="+form.getRevclassid() ; 
			if(form.getIsRevaluation()!=null && form.getIsRevaluation().equalsIgnoreCase("revaluation")){
				subQuery=subQuery+"  and er.isRevaluation=1" ;
			}
			else if(form.getIsRevaluation()!=null && form.getIsRevaluation().equalsIgnoreCase("scrutiny")){
				subQuery=subQuery+"  and er.isScrutiny=1 " ;
			}
			else{
				subQuery=subQuery+" and er.isChallengeValuation=1 " ;
			}
			Query subjectQuery = session.createQuery(subQuery);
			subject = (ExamRevaluationApp) subjectQuery.uniqueResult();
		
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return subject;
	}
	@Override
	public boolean saveChallengeValuationApps(List<ExamRevaluationApplicationNew> boList) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(boList!=null && !boList.isEmpty()){
				Iterator<ExamRevaluationApplicationNew> itr=boList.iterator();
				while (itr.hasNext()) {
					ExamRevaluationApplicationNew bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;
	}
	
	}
	@Override
	public List getSubjectsListForRevaluationSupplementry(Student student,Integer examId, int revalSupplyClassId) throws Exception {
		Session session = null;
		List subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery = 			
				" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
			" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
			" classes.name,classes.term_number,personal_data.first_name," +
			" EXAM_student_final_mark_details.student_id as studID,  " +
			" EXAM_student_final_mark_details.class_id as classID," +
			" EXAM_student_final_mark_details.subject_id as subID,subject.code as subCode," +
			" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +
			//" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical','Theory')) as subType," +
			" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id, " + 
			" EXAM_student_overall_internal_mark_details.theory_total_mark," +
			" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
			" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
			" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
			" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
			" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
			" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
			" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark >0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
			" EXAM_publish_exam_results.publish_date," +
			" EXAM_sub_definition_coursewise.subject_order as subject_order," +
			" classes.term_number, "+
			" EXAM_subject_rule_settings.theory_ese_entered_max_mark "+
		
			" from EXAM_student_final_mark_details" +
		
			" LEFT JOIN student on EXAM_student_final_mark_details.student_id = student.id" +
			" LEFT JOIN adm_appln on student.adm_appln_id = adm_appln.id" +
			" LEFT JOIN personal_data ON adm_appln.personal_data_id = personal_data.id" +
			" LEFT JOIN subject on EXAM_student_final_mark_details.subject_id = subject.id" +
			" LEFT JOIN subject_type ON subject.subject_type_id = subject_type.id" +
			" LEFT JOIN EXAM_definition ON EXAM_student_final_mark_details.exam_id = EXAM_definition.id" +
			" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id" +
			" LEFT JOIN classes ON EXAM_student_final_mark_details.class_id = classes.id" +
			" LEFT JOIN class_schemewise ON class_schemewise.class_id = classes.id" +
			" LEFT JOIN curriculum_scheme_duration on class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
			" LEFT JOIN curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id" +
			" LEFT JOIN EXAM_student_overall_internal_mark_details ON EXAM_student_overall_internal_mark_details.class_id = classes.id" +
			" and EXAM_student_overall_internal_mark_details.student_id = student.id" +
			" and EXAM_student_overall_internal_mark_details.subject_id = subject.id" +
			" LEFT JOIN EXAM_subject_rule_settings ON EXAM_subject_rule_settings.subject_id = subject.id " +
			" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year " +
			" and EXAM_subject_rule_settings.course_id = classes.course_id " +
			" and EXAM_subject_rule_settings.scheme_no = classes.term_number " +
			" LEFT JOIN EXAM_publish_exam_results ON EXAM_publish_exam_results.class_id = classes.id " +
			" and EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
			" LEFT JOIN EXAM_sub_definition_coursewise ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
			" and EXAM_sub_definition_coursewise.course_id = classes.course_id " +
			" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number " +
			" and EXAM_sub_definition_coursewise.academic_year = curriculum_scheme_duration.academic_year " +
			" LEFT JOIN EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id " +
			" LEFT JOIN course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id " +
		
			" where  EXAM_student_final_mark_details.exam_id =:examId " +
			" and EXAM_student_final_mark_details.class_id =:classId and student.id =:studentId" +
			//" and  EXAM_sub_definition_coursewise.dont_show_att_marks=1"+
		    //" and EXAM_sub_definition_coursewise.show_internal_final_mark_added=1"+
		     " and (subject.is_theory_practical='P' || subject.is_theory_practical='T')"    +                          
			" group by student.id,EXAM_student_overall_internal_mark_details.exam_id,subject.id " +
				
			"  UNION ALL" +
				
			" SELECT EXAM_student_overall_internal_mark_details.exam_id as eid, exam_type.name," +
			" EXAM_definition.name, EXAM_definition.month, EXAM_definition.year," +
			" classes.name,classes.term_number,personal_data.first_name," +
			" EXAM_student_final_mark_details.student_id as studID,  " +
			" EXAM_student_final_mark_details.class_id as classID," +
			" EXAM_student_final_mark_details.subject_id as subID,subject.code as subCode," +
			" subject.name as subName,subject.is_theory_practical as subType, ifnull(EXAM_subject_sections.name,' ') as secName," +
			//" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical','Theory')) as subType," +
			" CAST(adm_appln.selected_course_id AS UNSIGNED INTEGER) AS course_id,  " +
			" EXAM_student_overall_internal_mark_details.theory_total_mark," +
			" EXAM_student_overall_internal_mark_details.practical_total_mark, " +
			" (if(EXAM_student_final_mark_details.student_theory_marks is not null,EXAM_student_final_mark_details.student_theory_marks,0)) as student_theory_marks," +
			" (if(EXAM_student_final_mark_details.student_practical_marks is not null,EXAM_student_final_mark_details.student_practical_marks,0)) as student_practical_marks," +
			" (if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark>0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain,  " +
			" (if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, " +
			" ((if(EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+if(student_theory_marks > 0,student_theory_marks,0))/     (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0))*100) as theoryPer," +
			" ((if(EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark >0,EXAM_student_overall_internal_mark_details.practical_total_sub_internal_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark >0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks >0,student_practical_marks,0))/(if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0))*100) as practicalper," +
			" EXAM_publish_exam_results.publish_date," +
			" EXAM_sub_definition_coursewise.subject_order as subject_order," +
			" classes.term_number, "+
			" EXAM_subject_rule_settings.theory_ese_entered_max_mark "+
		
			" from EXAM_student_final_mark_details" +
		
			" LEFT JOIN student on EXAM_student_final_mark_details.student_id = student.id" +
			" LEFT JOIN adm_appln on student.adm_appln_id = adm_appln.id" +
			" LEFT JOIN personal_data ON adm_appln.personal_data_id = personal_data.id" +
			" LEFT JOIN subject on EXAM_student_final_mark_details.subject_id = subject.id" +
			" LEFT JOIN subject_type ON subject.subject_type_id = subject_type.id" +
			" LEFT JOIN EXAM_definition ON EXAM_student_final_mark_details.exam_id = EXAM_definition.id" +
			" LEFT JOIN exam_type on exam_type.id = EXAM_definition.exam_type_id" +
			" LEFT JOIN classes ON EXAM_student_final_mark_details.class_id = classes.id" +
			" LEFT JOIN class_schemewise ON class_schemewise.class_id = classes.id" +
			" LEFT JOIN curriculum_scheme_duration on class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
			" LEFT JOIN curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id" +
			" LEFT JOIN EXAM_student_overall_internal_mark_details ON EXAM_student_overall_internal_mark_details.class_id = classes.id" +
			" and EXAM_student_overall_internal_mark_details.student_id = student.id" +
			" and EXAM_student_overall_internal_mark_details.subject_id = subject.id" +
			" LEFT JOIN EXAM_subject_rule_settings ON EXAM_subject_rule_settings.subject_id = subject.id " +
			" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year " +
			" and EXAM_subject_rule_settings.course_id = classes.course_id " +
			" and EXAM_subject_rule_settings.scheme_no = classes.term_number " +
			" LEFT JOIN EXAM_publish_exam_results ON EXAM_publish_exam_results.class_id = classes.id " +
			" and EXAM_publish_exam_results.exam_id = EXAM_definition.id " +
			" LEFT JOIN EXAM_sub_definition_coursewise ON EXAM_sub_definition_coursewise.subject_id = subject.id " +
			" and EXAM_sub_definition_coursewise.course_id = classes.course_id " +
			" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number " +
			" and EXAM_sub_definition_coursewise.academic_year = curriculum_scheme_duration.academic_year " +
			" LEFT JOIN EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id " +
			" LEFT JOIN course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id " +
		
			" where  subject.is_theory_practical='B' and EXAM_student_final_mark_details.exam_id =:examId" +
			" and EXAM_student_final_mark_details.class_id =:classId and student.id =:studentId" +
			//" and  EXAM_sub_definition_coursewise.dont_show_att_marks=1"+
		    //" and EXAM_sub_definition_coursewise.show_internal_final_mark_added=1"+
		    
			" group by student.id,EXAM_student_overall_internal_mark_details.exam_id,subject.id  order by subject_order" ;
			
			Query subjectQuery = session.createSQLQuery(subQuery);
			subjectQuery.setInteger("examId", examId);
			subjectQuery.setInteger("classId", revalSupplyClassId);
			subjectQuery.setInteger("studentId", student.getId());
			
			subject = subjectQuery.list();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return subject;
	}
	
	public CandidatePGIDetailsExamRegular checkOnlinePaymentReg(NewSupplementaryImpApplicationForm admForm)throws Exception {
		log.info("Entered into dup generateCandidateRefNo-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			
				String query=" from CandidatePGIDetailsExamRegular c where c.exam.id="+admForm.getExamId()
				+" and c.student.id="+admForm.getStudentId()+" and c.txnStatus='Success'";
				
				
				
				
				CandidatePGIDetailsExamRegular candidatePgiBo=(CandidatePGIDetailsExamRegular)session.createQuery(query).uniqueResult();
				
				log.info("Exit dup generateCandidateRefNo-supp TransactionImpl");
				
				return candidatePgiBo;
		} catch (Exception e) {
			
			log.error("Error in dup generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		
		
	}
	@Override
	public String generateCandidateRefNoReg(CandidatePGIDetailsExamRegular bo)throws Exception {
		log.info("Entered into generateCandidateRefNo-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="EXAMREG"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-supp TransactionImpl");
		return candidateRefNo;
	}
	
	@Override
	public boolean updateReceivedStatusReg(CandidatePGIDetailsExamRegular bo,NewSupplementaryImpApplicationForm admForm)
			throws Exception {
		log.info("Entered into updateReceivedStatus-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from CandidatePGIDetailsExamRegular c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+bo.getTxnAmount()+" and c.txnStatus='Pending'";
				CandidatePGIDetailsExamRegular candidatePgiBo=(CandidatePGIDetailsExamRegular)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
				candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
				//candidatePgiBo.setBankId(bo.getBankId());
				candidatePgiBo.setBankRefNo(bo.getBankRefNo());
				candidatePgiBo.setTxnDate(bo.getTxnDate());
				candidatePgiBo.setTxnStatus(bo.getTxnStatus());
				
				//raghu
				candidatePgiBo.setMode(bo.getMode());
				candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
				candidatePgiBo.setMihpayId(bo.getMihpayId());
				candidatePgiBo.setPgType(bo.getPgType());
				
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					admForm.setIsTnxStatusSuccess(true);
					admForm.setPaymentSuccess(true);
				}
				admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				admForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				}
			}
			transaction.commit();
			session.flush();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit updateReceivedStatus supp TransactionImpl");
		return isUpdated;
	}
	
	@Override
	public boolean checkPaymentDetails(NewSupplementaryImpApplicationForm form)throws Exception {
		Session session = null;
		CandidatePGIDetailsExamRegular paymentCheck = null;
		boolean dup=false;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String subQuery= "from CandidatePGIDetailsExamRegular c where c.exam.id="+form.getExamId() +" and c.student.id="+form.getStudentObj().getId() +" and c.txnStatus='success'";
			
			Query subjectQuery = session.createQuery(subQuery);
			paymentCheck = (CandidatePGIDetailsExamRegular) subjectQuery.uniqueResult();
			if(paymentCheck!=null){
				
				dup=true;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return dup;
	}
	@Override
	public CandidatePGIDetailsExamSupply checkOnlinePaymentSuppl(NewSupplementaryImpApplicationForm admForm) throws Exception {

		log.info("Entered into dup generateCandidateRefNo-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			
				String query=" from CandidatePGIDetailsExamSupply c where c.exam.id="+admForm.getExamId()
				+" and c.student.id="+admForm.getStudentId()+" and c.txnStatus='Success'";
				
				
				
				
				CandidatePGIDetailsExamSupply candidatePgiBo=(CandidatePGIDetailsExamSupply)session.createQuery(query).uniqueResult();
				
				log.info("Exit dup generateCandidateRefNo-supp TransactionImpl");
				
				return candidatePgiBo;
		} catch (Exception e) {
			
			log.error("Error in dup generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		
		
	
	}
	@Override
	public String generateCandidateRefNoSuppl(CandidatePGIDetailsExamSupply bo)throws Exception {
		log.info("Entered into generateCandidateRefNo-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="EXAMSUPPLY"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-supp TransactionImpl");
		return candidateRefNo;
	}
	@Override
	public boolean updateReceivedStatusSupp(CandidatePGIDetailsExamSupply bo,NewSupplementaryImpApplicationForm admForm)
	throws Exception {
		log.info("Entered into updateReceivedStatus-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from CandidatePGIDetailsExamSupply c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+bo.getTxnAmount()+" and c.txnStatus='Pending'";
				CandidatePGIDetailsExamSupply candidatePgiBo=(CandidatePGIDetailsExamSupply)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					//candidatePgiBo.setBankId(bo.getBankId());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());
		
					//raghu
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setPgType(bo.getPgType());
		
					session.update(candidatePgiBo);
					if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
						admForm.setIsTnxStatusSuccess(true);
						admForm.setPaymentSuccess(true);
					}
					admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
					admForm.setTransactionRefNO(bo.getTxnRefNo());
					isUpdated=true;
				}
			}
			transaction.commit();
			session.flush();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit updateReceivedStatus supp TransactionImpl");
		return isUpdated;
	}
	@Override
	public String generateCandidateRefNoRevaluation(RevaluationApplicationPGIDetails bo) throws Exception {
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		List<ExamRevaluationApplicationNew> newList = new ArrayList<ExamRevaluationApplicationNew>();
		int countRev = 0;
		int countScr = 0;
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="EXAMREVP"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				RevaluationApplicationPGIDetails applicationPGIDetails = getStoredData(candidateRefNo);
				if(applicationPGIDetails == null){
				newList = getRevaluationData(bo);
				Iterator<ExamRevaluationApplicationNew> iterator = newList.iterator();
				while(iterator.hasNext()){
					ExamRevaluationApplicationNew applicationNew = iterator.next();
					if(applicationNew.getIsRevaluation()== true){
						countRev++;
					}
					if(applicationNew.getIsScrutiny()== true){
						countScr++;
					}
				}
				if(countRev > 0){
					bo.setIsRevaluation(true);
					bo.setIsScrutiny(false);
				}if(countScr>0){
					bo.setIsRevaluation(false);
					bo.setIsScrutiny(true);
				}
				}
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-supp TransactionImpl");
		return candidateRefNo;
	}
	
	private List<ExamRevaluationApplicationNew> getRevaluationData(RevaluationApplicationPGIDetails bo) throws Exception {
		Session session = null;
		List<ExamRevaluationApplicationNew> arrList = new ArrayList<ExamRevaluationApplicationNew>();
		try{
			session = HibernateUtil.getSession();
			String str = "from ExamRevaluationApplicationNew rev where rev.student.id = :studId and rev.exam.id = :examId";
			Query query = session.createQuery(str).setInteger("studId", bo.getStudent().getId())
			.setInteger("examId", bo.getExam().getId());
			arrList = query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return arrList;
	}
	private RevaluationApplicationPGIDetails getStoredData(String candidateRefNo) throws Exception {
		Session session = null;
		RevaluationApplicationPGIDetails pgiDetails = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails rev where rev.candidateRefNo  = :refNo and rev.txnStatus = 'Pending'";
			Query query = session.createQuery(s).setString("refNo", candidateRefNo);
			pgiDetails = (RevaluationApplicationPGIDetails)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return pgiDetails;
	}
	
	@Override
	public RevaluationApplicationPGIDetails checkOnlinePaymentRev(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
			throws Exception {
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		newSupplementaryImpApplicationForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			
				String query=" from RevaluationApplicationPGIDetails c where c.exam.id="+newSupplementaryImpApplicationForm.getExamId()
				+" and c.student.id="+newSupplementaryImpApplicationForm.getStudentId()+" and c.txnStatus='Success'";
				
				
				
				
                     RevaluationApplicationPGIDetails candidatePgiBo=(RevaluationApplicationPGIDetails)session.createQuery(query).uniqueResult();
				
				log.info("Exit dup generateCandidateRefNo-supp TransactionImpl");
				
				return candidatePgiBo;
		} catch (Exception e) {
			
			log.error("Error in dup generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		
	}
	@Override
	public boolean updateReceivedStatusRev(RevaluationApplicationPGIDetails bo,
			NewSupplementaryImpApplicationForm form) throws Exception {
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		form.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from RevaluationApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+form.getTxnAmt()+" and c.txnStatus='Pending'";
				RevaluationApplicationPGIDetails candidatePgiBo=(RevaluationApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setIsRevaluation(true);
					candidatePgiBo.setIsScrutiny(false);
					
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());				
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					form.setIsTnxStatusSuccess(true);
					form.setPaymentSuccess(true);
				}
				
				form.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				form.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
	
	
	}
	@Override
	public RevaluationApplicationPGIDetails getPgiDetails(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
			throws Exception {
	Session session = null;
	RevaluationApplicationPGIDetails details = null;
	try{
		session = HibernateUtil.getSession();
		String s = "from RevaluationApplicationPGIDetails rev where rev.student.id = :studentId " +
				"and rev.exam.id = :examId " +
				"and rev.txnStatus = 'Success'";
		Query query = session.createQuery(s)
						.setString("studentId", String.valueOf(newSupplementaryImpApplicationForm.getStudentId()))
						.setString("examId", newSupplementaryImpApplicationForm.getExamId());
		details =(RevaluationApplicationPGIDetails) query.uniqueResult();
	}catch (Exception e) {
		e.printStackTrace();
		throw new ApplicationException(e);
	}
		return details;
	}
	@Override
	public List<RevaluationApplicationPGIDetails> getPendingList(NewSupplementaryImpApplicationForm admForm) throws Exception {
		Session session = null;
		List<RevaluationApplicationPGIDetails> list = new ArrayList<RevaluationApplicationPGIDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails rev where rev.candidateRefNo  = :refNo and rev.txnStatus = 'Pending'";
			Query query = session.createQuery(s).setString("refNo", admForm.getTxnid());
			list = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public boolean updateRev(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(list!=null && !list.isEmpty()){
				Iterator<ExamRevaluationApplicationNew> itr=list.iterator();
				while (itr.hasNext()) {
					ExamRevaluationApplicationNew bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;

	}
	}
	@Override
	public RevaluationApplicationPGIDetails getSuccessDetails(
			NewSupplementaryImpApplicationForm admForm) throws Exception {
		Session session = null;
		RevaluationApplicationPGIDetails applicationPGIDetails = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails rev where rev.candidateRefNo  = :refNo and rev.txnStatus = 'success'";
			Query query = session.createQuery(s).setString("refNo", admForm.getCandidateRefNo());
			applicationPGIDetails =(RevaluationApplicationPGIDetails) query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return applicationPGIDetails;

}
	@Override
	public List<ExamRevaluationApplicationNew> getExistingData(NewSupplementaryImpApplicationForm admForm,
			RevaluationApplicationPGIDetails details) throws Exception {
		Session session = null;
		List<ExamRevaluationApplicationNew> revaluationList = new ArrayList<ExamRevaluationApplicationNew>();
		try{
			session = HibernateUtil.getSession();
			String s = "from ExamRevaluationApplicationNew rev where rev.student.id = :studentId and rev.exam.id = :examId and rev.isRevaluation = 1";
			Query query = session.createQuery(s).setInteger("studentId", admForm.getStudentId())
												.setInteger("examId", Integer.parseInt(admForm.getExamId()));
			revaluationList = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return revaluationList;
	}
	@Override
	public boolean updateReceivedStatusScr(RevaluationApplicationPGIDetails bo,
			NewSupplementaryImpApplicationForm admForm) throws Exception {

		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from RevaluationApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+admForm.getTxnAmt()+" and c.txnStatus='Pending'";
				RevaluationApplicationPGIDetails candidatePgiBo=(RevaluationApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setIsRevaluation(false);
					candidatePgiBo.setIsScrutiny(true);
					
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());				
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					admForm.setIsTnxStatusSuccess(true);
					admForm.setPaymentSuccess(true);
				}
				
				admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				admForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
	
	
	
	}
	
	@Override
	public List<ExamRevaluationApplicationNew> getExistingDataScr(NewSupplementaryImpApplicationForm admForm,
			RevaluationApplicationPGIDetails details) throws Exception {
		
		Session session = null;
		List<ExamRevaluationApplicationNew> revaluationList = new ArrayList<ExamRevaluationApplicationNew>();
		try{
			session = HibernateUtil.getSession();
			String s = "from ExamRevaluationApplicationNew rev where rev.student.id = :studentId and rev.exam.id = :examId and rev.isScrutiny = 1";
			Query query = session.createQuery(s).setInteger("studentId", admForm.getStudentId())
												.setInteger("examId", Integer.parseInt(admForm.getExamId()));
			revaluationList = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return revaluationList;
	}
	@Override
	public boolean updateScr(NewSupplementaryImpApplicationForm admForm,
			List<ExamRevaluationApplicationNew> list) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(list!=null && !list.isEmpty()){
				Iterator<ExamRevaluationApplicationNew> itr=list.iterator();
				while (itr.hasNext()) {
					ExamRevaluationApplicationNew bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;
	}
	}
	@Override
	public String generateCandidateRefNoRevaluationSupply(RevaluationApplicationPGIDetails bo) throws Exception {

		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		List<ExamRevaluationApplicationNew> newList = new ArrayList<ExamRevaluationApplicationNew>();
		int countRev = 0;
		int countScr = 0;
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="EXAMREVPSUP"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				RevaluationApplicationPGIDetails applicationPGIDetails = getStoredData(candidateRefNo);
				if(applicationPGIDetails == null){
				newList = getRevaluationData(bo);
				Iterator<ExamRevaluationApplicationNew> iterator = newList.iterator();
				while(iterator.hasNext()){
					ExamRevaluationApplicationNew applicationNew = iterator.next();
					if(applicationNew.getIsRevaluation()== true){
						countRev++;
					}
					if(applicationNew.getIsScrutiny()== true){
						countScr++;
					}
				}
				if(countRev > 0){
					bo.setIsRevaluation(true);
					bo.setIsScrutiny(false);
				}if(countScr>0){
					bo.setIsRevaluation(false);
					bo.setIsScrutiny(true);
				}
				}
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-supp TransactionImpl");
		return candidateRefNo;
	
	}
	@Override
	public List<RevaluationApplicationPGIDetails> getPendingListSupply(NewSupplementaryImpApplicationForm admForm) throws Exception {
		Session session = null;
		List<RevaluationApplicationPGIDetails> list = new ArrayList<RevaluationApplicationPGIDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails rev where rev.candidateRefNo  = :refNo and rev.txnStatus = 'Pending'";
			Query query = session.createQuery(s).setString("refNo", admForm.getTxnid());
			list = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public boolean updateReceivedStatusRevSupply(RevaluationApplicationPGIDetails bo,NewSupplementaryImpApplicationForm admForm) throws Exception {

		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from RevaluationApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+admForm.getTxnAmt()+" and c.txnStatus='Pending'";
				RevaluationApplicationPGIDetails candidatePgiBo=(RevaluationApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setIsRevaluation(true);
					candidatePgiBo.setIsScrutiny(false);
					
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());				
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					admForm.setIsTnxStatusSuccess(true);
					admForm.setPaymentSuccess(true);
				}
				
				admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				admForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
	
	
	
	}
	@Override
	public boolean updateReceivedStatusScrSupply(RevaluationApplicationPGIDetails bo,NewSupplementaryImpApplicationForm admForm) throws Exception {


		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from RevaluationApplicationPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+admForm.getTxnAmt()+" and c.txnStatus='Pending'";
				RevaluationApplicationPGIDetails candidatePgiBo=(RevaluationApplicationPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
					candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
					candidatePgiBo.setBankRefNo(bo.getBankRefNo());
					candidatePgiBo.setTxnDate(bo.getTxnDate());
					candidatePgiBo.setTxnStatus(bo.getTxnStatus());				
					candidatePgiBo.setMode(bo.getMode());
					candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
					candidatePgiBo.setMihpayId(bo.getMihpayId());
					candidatePgiBo.setIsRevaluation(false);
					candidatePgiBo.setIsScrutiny(true);
					
					candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());				
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					admForm.setIsTnxStatusSuccess(true);
					admForm.setPaymentSuccess(true);
				}
				
				admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				admForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl..."+e);
			System.out.println("PGI STATUS");
			e.printStackTrace();
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit updateReceivedStatus-NewSupplementaryImpApplicationTransactionImpl");
		return isUpdated;
	
	
	
	
	}
	@Override
	public boolean updateRevSupply(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list5) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(list5!=null && !list5.isEmpty()){
				Iterator<ExamRevaluationApplicationNew> itr=list5.iterator();
				while (itr.hasNext()) {
					ExamRevaluationApplicationNew bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;

	}
	
	}
	@Override
	public boolean updateScrSupply(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list6) throws Exception {

		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			if(list6!=null && !list6.isEmpty()){
				Iterator<ExamRevaluationApplicationNew> itr=list6.iterator();
				while (itr.hasNext()) {
					ExamRevaluationApplicationNew bo = itr .next();
					if(bo.getId()>0)
						session.update(bo);
					else
						session.save(bo);
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
			return true;
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			return false;
	}
	
	}
	@Override
	public RevaluationApplicationPGIDetails getPgiDetailsSupply(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		Session session = null;
		RevaluationApplicationPGIDetails details = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails rev where rev.student.id = :studentId " +
					"and rev.exam.id = :examId " +
					"and rev.txnStatus = 'Success'";
			Query query = session.createQuery(s)
							.setString("studentId", String.valueOf(newSupplementaryImpApplicationForm.getStudentId()))
							.setString("examId", newSupplementaryImpApplicationForm.getExamId());
			details =(RevaluationApplicationPGIDetails) query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
			return details;
	}
	
}
