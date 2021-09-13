package com.kp.cms.transactionsimpl.exam;



import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.actions.exam.NewExamStudentTokenRegisteredEntryAction;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamExamCourseSchemeDetailsBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamStudentPreviousClassDetailsBO;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.exam.ExamStudentTokenRegisterdForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactions.exam.IStudentTokenRegisteredTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class StudentTokenRegisteredTransactionImpl implements IStudentTokenRegisteredTransaction{
	
	private static volatile StudentTokenRegisteredTransactionImpl studentTokenRegisteredTransactionImpl = null;
	private static final Log log = LogFactory.getLog(StudentTokenRegisteredTransactionImpl.class);
    private StudentTokenRegisteredTransactionImpl() {
		
	}
    public static StudentTokenRegisteredTransactionImpl getInstance() {
		if (studentTokenRegisteredTransactionImpl == null) {
			studentTokenRegisteredTransactionImpl = new StudentTokenRegisteredTransactionImpl();
		}
		return studentTokenRegisteredTransactionImpl;
	}
    


	@Override
	public ExamRegularApplication getData(String studentId) throws Exception {
		Session session = null;
		ExamRegularApplication examRegApp = null;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from ExamRegularApplication e where e.student.id=:student.id" ).setString("student.id", studentId);
		}
		catch(Exception e){
    		e.printStackTrace();
    	
    	}
		
		finally{
    		if (session != null) {
				session.flush();
				//session.close();
			}
    	}
		return examRegApp;
	}
	@Override
	public Integer getStudentId(String registrationNumber) throws Exception {
		Session session = null; 
		Integer id = null;
		try{
			session = HibernateUtil.getSession();
			if(registrationNumber != null && !registrationNumber.isEmpty()){
				Query query = session.createQuery("select s.id from Student s where s.admAppln.isCancelled = 0 and s.registerNo='"+registrationNumber+"'");
				id = (Integer) query.uniqueResult();
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return id;
	}
	@Override
	public ExamRegularApplication hasStudentAppliedForExam(int examId, int studentId) throws Exception {
		Session session = null;
		try{
			session = HibernateUtil.getSession();
			String query = "from ExamRegularApplication e where e.student.id=:studentId and e.exam.id=:examId";
			Query query1 = session.createQuery(query)
								  .setInteger("examId", examId)
								  .setInteger("studentId", studentId);
			return (ExamRegularApplication) query1.uniqueResult();			
		}
		catch(Exception e){
			e.printStackTrace();
			if(session != null) {
				session.flush();
				session.close();
			}
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
	}
	
	@Override
	public List<StudentSupplementaryImprovementApplication> hasStudentAppearedForExam(int examId, int studentId) throws Exception {
		Session session = null;
		try{
			session = HibernateUtil.getSession();
			String hql = "from StudentSupplementaryImprovementApplication e where e.student.id=:studentId and e.examDefinition.id=:examId";
			Query query = session.createQuery(hql)
								  .setInteger("examId", examId)
								  .setInteger("studentId", studentId);
			return query.list();			
		}
		catch(Exception e){
			e.printStackTrace();
			if(session != null) {
				session.flush();
				session.close();
			}
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
	}
		
	
	@Override
	public int getClassId(String examId,int studentId) throws Exception {
		Session session = null;
		Integer classId = null;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from ExamRegularApplication reg where reg.exam.id =:examId and reg.classes.course.id = (select student.classSchemewise.classes.course.id from Student student where student.id = :studentId) group by reg.classes.id")
								 .setInteger("examId", Integer.parseInt(examId))
								 .setInteger("studentId", studentId);
			ExamRegularApplication application = (ExamRegularApplication) query.uniqueResult();
			classId = application.getClasses().getId();
			return classId;
		}
		catch(Exception e){
			e.printStackTrace();
			if(session != null) {
				session.flush();
				//session.close();
			}
			if(classId == null) {
				throw new DataNotFoundException();
			}
			throw new ApplicationException();
			
		}
		finally{
			if(session != null){
				session.flush();
			}
			
		}
		
	}
	@Override
	public boolean addRegularAppData(
			ExamRegularApplication examRegularApplication) throws Exception {
		Session session = null;
		Transaction transaction = null;
		
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.save(examRegularApplication);
			transaction.commit();
			session.flush();
			//session.close();
			return true;
			
		}
		 catch (Exception e){
			 if ( transaction != null){
				 transaction.rollback();
			 }
			 if( session != null ){
				 session.flush();
				 //session.close();
			 }
			 throw e;
		 }
	}
	@Override
	public boolean updateRegularAppData(ExamRegularApplication examRegularApplication) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			session.save(examRegularApplication);
			transaction.commit();
			session.flush();
			//session.close();
			return true;
			
		}
		catch(Exception e){
			 if ( transaction != null){
				 transaction.rollback();
			 }
			 if( session != null ){
				 session.flush();
				 //session.close();
			 }
			 throw e;
			
		}
		
		
	}
	
	/*public boolean saveSupplementaryTokenRegistration(List<StudentSupplementaryImprovementApplication> tokenRegistrations) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();			
			transaction = session.beginTransaction();
			Iterator<StudentSupplementaryImprovementApplication> it = tokenRegistrations.iterator();
			while(it.hasNext()) {
				StudentSupplementaryImprovementApplication tokenRegistration = it.next();
				session.save(tokenRegistration);
			}
			transaction.commit();
			session.flush();
			session.close();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}*/

	@Override
	public int getSuppClassId(String examId, int studentId) throws Exception {
		Session session = null;
		Integer classId = null;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from StudentSupplementaryImprovementApplication supp where supp.examDefinition.id =:examId and supp.classes.course.id = (select student.classSchemewise.classes.course.id from Student student where student.id = :studentId) group by supp.classes.id")
								 .setInteger("examId", Integer.parseInt(examId))
								 .setInteger("studentId", studentId);
			StudentSupplementaryImprovementApplication application = (StudentSupplementaryImprovementApplication) query.uniqueResult();
			classId = application.getClasses().getId();
			
		}
		catch(Exception e){
			e.printStackTrace();
			if(session != null) {
				session.flush();
				//session.close();
			}
			if(classId == null) {
				throw new DataNotFoundException();
			}
			throw new ApplicationException();
				}
		finally{
			if(session != null){
				session.flush();
			}
			
		}
		
		return classId;
	}
	@Override
	public List<Object[]> getTotalSubjectList(int studentId, String classId) throws Exception {
		Session session = null;
		List<Object[]> objects=null;
		try{
			session = HibernateUtil.getSession();
			String str =  " select subject.id as sub_id, subject.is_theory_practical,classes.term_number from student  "  +
							 "  inner join adm_appln ON student.adm_appln_id = adm_appln.id  "  +
							 "  inner join class_schemewise ON student.class_schemewise_id = class_schemewise.id  "  +
							 " inner join classes ON class_schemewise.class_id = classes.id " +
							 " inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id " +
							 " inner join applicant_subject_group on applicant_subject_group.adm_appln_id = adm_appln.id " +
							 " inner join subject_group ON applicant_subject_group.subject_group_id = subject_group.id and subject_group.is_active=1 " +
							 " inner join subject_group_subjects on subject_group_subjects.subject_group_id = subject_group.id and subject_group_subjects.is_active=1 " +
							 " inner join subject ON subject_group_subjects.subject_id = subject.id and subject.is_active=1 " +
							 " where student.id=:studId " +
							 " and classes.id=:classId " +
							
							 " group by student.id,subject.id,classes.id " +
							
							
							 " union all " +
							
							 " select subject.id as sub_id, subject.is_theory_practical,classes.term_number from student " +
							 " inner join EXAM_student_previous_class_details on EXAM_student_previous_class_details.student_id = student.id " +
							 " inner join classes ON EXAM_student_previous_class_details.class_id = classes.id " +
							 " inner join class_schemewise ON student.class_schemewise_id = class_schemewise.id " +
							 " inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id " +
							 " inner join EXAM_student_sub_grp_history on EXAM_student_sub_grp_history.student_id = student.id  " +
							 " and EXAM_student_sub_grp_history.scheme_no = classes.term_number " +
							 " inner join subject_group ON EXAM_student_sub_grp_history.subject_group_id = subject_group.id and subject_group.is_active=1 " +
							 " inner join subject_group_subjects on subject_group_subjects.subject_group_id = subject_group.id and subject_group_subjects.is_active=1 " +
							 " inner join subject ON subject_group_subjects.subject_id = subject.id and subject.is_active=1 " +
							 " where student.id= :studId " +		
							 " and classes.id= :classId " +
							 " group by student.id,subject.id,classes.id " ;
			Query query = session.createSQLQuery(str)
									.setInteger("studId", studentId)
									.setString("classId", classId);
									
			objects = query.list();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objects;
	}
	
	/*public CandidatePGIDetailsExamRegular checkOnlinePaymentReg(ExamStudentTokenRegisterdForm admForm)throws Exception {
		log.info("Entered into dup generateCandidateRefNo-supp TransactionImpl");
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			
				String query=" from CandidatePGIDetailsExamRegular c where c.exam.id="+Integer.parseInt(admForm.getExamId())
				+" and c.student.id="+admForm.getStudentObj().getId()+" and c.txnStatus='Success'";
				
				
				
				
				CandidatePGIDetailsExamRegular candidatePgiBo=(CandidatePGIDetailsExamRegular)session.createQuery(query).uniqueResult();
				
				log.info("Exit dup generateCandidateRefNo-supp TransactionImpl");
				
				return candidatePgiBo;
		} catch (Exception e) {
			
			log.error("Error in dup generateCandidateRefNo-supp TransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		
		
	}*/
	
	/*public boolean updateReceivedStatusReg(CandidatePGIDetailsExamRegular bo,ExamStudentTokenRegisterdForm admForm)
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
	}*/
	
	@Override
	public boolean isRegisterNoValid(int examId, int courseId) throws Exception {
		Session session = null;
		ExamExamCourseSchemeDetailsBO examBo=null;
		try{
			session = HibernateUtil.getSession();
			
			String hql = "from ExamExamCourseSchemeDetailsBO e where e.courseId=:courseId and e.examId=:examId and e.isActive=1";
			Query query = session.createQuery(hql)
								  .setInteger("examId", examId)
								  .setInteger("courseId", courseId);
			examBo=(ExamExamCourseSchemeDetailsBO)query.uniqueResult();
			if(examBo!=null){
				return true;
			}
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			if(session != null) {
				session.flush();
				session.close();
			}
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
	}
	
	public List<Integer> getAllClassIdsStudent(int studentId) throws Exception{
		Session session=null;
		List<Integer> classesList=null;
		try{
			session=HibernateUtil.getSession();
			Query query=session.createQuery("select spc.classId from ExamStudentPreviousClassDetailsBO spc where spc.studentId=:studentId");
			query.setParameter("studentId", studentId);
			classesList=query.list();
			Query query1=session.createQuery("select s.classSchemewise.classes.id from Student s where s.id=:studentId ");
			query1.setParameter("studentId", studentId);
			int classId=(Integer)query1.uniqueResult();
			classesList.add(classId);
			return classesList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(session!=null){
				session.flush();
			}
		}
		return classesList;
	}
	
	public RegularExamFees getRegularExamFees(
			ExamStudentTokenRegisterdForm form)
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
	
}
