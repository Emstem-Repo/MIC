package com.kp.cms.transactionsimpl.admission;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.CharacterAndConduct;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CurriculumSchemeDuration;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admission.TCDetailsForm;
import com.kp.cms.transactions.admission.ITCDetailsTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;

public class TCDetailsTransactionImpl implements ITCDetailsTransaction {
	/**
	 * Singleton object of TCDetailsTransactionImpl
	 */
	private static volatile TCDetailsTransactionImpl tCDetailsTransactionImpl = null;
	private static final Log log = LogFactory.getLog(TCDetailsTransactionImpl.class);
	private TCDetailsTransactionImpl() {
		
	}
	/**
	 * return singleton object of TCDetailsTransactionImpl.
	 * @return
	 */
	public static TCDetailsTransactionImpl getInstance() {
		if (tCDetailsTransactionImpl == null) {
			tCDetailsTransactionImpl = new TCDetailsTransactionImpl();
		}
		return tCDetailsTransactionImpl;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.ITCDetailsTransaction#getStudentDetails(java.lang.String)
	 */
	@Override
	public List<Student> getStudentDetails(String query) throws Exception {
		Session session = null;
		List<Student> selectedCandidatesList = null;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery(query);
			selectedCandidatesList = selectedCandidatesQuery.list();
			return selectedCandidatesList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.ITCDetailsTransaction#getStudentTCDetails(java.lang.String)
	 */
	@Override
	public Student getStudentTCDetails(String query) throws Exception {
		Session session = null;
		Student student = null;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery(query);
			student = (Student)selectedCandidatesQuery.uniqueResult();
			return student;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} 
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.ITCDetailsTransaction#saveStudentTCDetails(com.kp.cms.bo.admin.StudentTCDetails)
	 */
	@Override
	public boolean saveStudentTCDetails(StudentTCDetails bo, TCNumber tcNumber, int currentAcademicYear, String userId) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			Student student = (Student) session.get(Student.class, bo.getStudent().getId());
			transaction = session.beginTransaction();
			transaction.begin();
			if(student.getTcNo() == null || student.getTcNo().isEmpty()) {
				student.setTcNo(String.valueOf(tcNumber.getCurrentNo()) + "/" + (student.getAdmAppln().getCourseBySelectedCourseId().getIsSelfFinancing() ? "SF/" : "") + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2));
				tcNumber.setCurrentNo(tcNumber.getCurrentNo() + 1);
				tcNumber.setLastModifiedDate(new Date());
				tcNumber.setModifiedBy(userId);
				session.update(tcNumber);
			}
			student.setLastModifiedDate(new Date());
			student.setModifiedBy(userId);
			session.update(student);
			session.saveOrUpdate(bo);
			transaction.commit();
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public List<CharacterAndConduct> getAllCharacterAndConduct()
			throws Exception {
		Session session = null;
		List<CharacterAndConduct> list = null;
		try {
			session = HibernateUtil.getSession();
			Query selectedCandidatesQuery=session.createQuery("from CharacterAndConduct c where c.isActive=1");
			list = selectedCandidatesQuery.list();
			return list;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
	}
	@Override
	public Boolean updateStudentTCDetails(TCDetailsForm tcDetailsForm)
			throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			String query="from Student s where  s.admAppln.isCancelled=0 " +
			" and s.isActive=1 and s.classSchemewise.id="+tcDetailsForm.getClassId();
			if(tcDetailsForm.getRegisterNo()!=null && !tcDetailsForm.getRegisterNo().isEmpty()){
				query=query+" and s.registerNo='"+tcDetailsForm.getRegisterNo()+"'";
			}
			query= query+" order by s.admAppln.personalData.firstName";
			List<Student> studentList= session.createQuery(query).list();
			if(studentList!=null && !studentList.isEmpty()){
				Iterator<Student> itr = studentList.iterator();
				int count=0;
				while(itr.hasNext()){
					Student stu = itr.next();
					StudentTCDetails bo=(StudentTCDetails)session.createQuery("from StudentTCDetails t where t.student.id="+stu.getId()).uniqueResult();
					if(bo==null){
						bo=new StudentTCDetails();
						bo.setCreatedBy(tcDetailsForm.getUserId());
						bo.setCreatedDate(new Date());
					}
					bo.setLastModifiedDate(new Date());
					bo.setModifiedBy(tcDetailsForm.getUserId());
					bo.setPassed(tcDetailsForm.getPassed());
					bo.setFeePaid(tcDetailsForm.getFeePaid());
					bo.setScholarship(tcDetailsForm.getScholarship());
					bo.setReasonOfLeaving(tcDetailsForm.getReasonOfLeaving());
					bo.setIsActive(true);
					bo.setDateOfApplication(CommonUtil.ConvertStringToSQLDate(tcDetailsForm.getDateOfApplication()));
					//bo.setDateOfLeaving(CommonUtil.ConvertStringToSQLDate(tcDetailsForm.getDateOfLeaving()));
					bo.setDateOfLeavingNew(tcDetailsForm.getDateOfLeaving());
					if(tcDetailsForm.getCharacterId()!=null && !tcDetailsForm.getCharacterId().isEmpty()){
						CharacterAndConduct conduct=new CharacterAndConduct();
						conduct.setId(Integer.parseInt(tcDetailsForm.getCharacterId()));
						bo.setCharacterAndConduct(conduct);
					}
					bo.setMonth(tcDetailsForm.getMonth());
					bo.setYear(Integer.parseInt(tcDetailsForm.getYear()));
					bo.setPublicExaminationName(tcDetailsForm.getPublicExamName());
					bo.setShowRegNo(tcDetailsForm.getShowRegisterNo());
					bo.setSubjectsPassedComplimentary(tcDetailsForm.getTcDetailsTO().getSubjectsPassedComplimentary());
					bo.setSubjectsPassedCore(tcDetailsForm.getTcDetailsTO().getSubjectsPassedCore());
					bo.setSubjectsPassedOptional(tcDetailsForm.getTcDetailsTO().getSubjectsPassedOptional());
					bo.setPromotionToNextClass(tcDetailsForm.getTcDetailsTO().getPromotionToNextClass());
					bo.setStudent(stu);
					bo.setIsStudentPunished(tcDetailsForm.getIsStudentPunished());
					session.saveOrUpdate(bo);
					if(++count % 20 == 0){
						session.flush();
						session.clear();
					}
				}
				transaction.commit();
				session.flush();
				session.close();
				return true;
			}
			else
				return false;
		
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
	
	public List<Object[]> getExamNames(TCDetailsForm tcDetailsForm) {
		Session session = null;
		Transaction transaction = null;
		List<Object[]> examdef=null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			String query="select distinct EXAM_definition.id,EXAM_definition.name  from EXAM_definition_program" +
					" inner join EXAM_definition on EXAM_definition_program.exam_defn_id = EXAM_definition.id" +
					" inner join program on EXAM_definition_program.program_id = program.id" +
					" inner join EXAM_exam_course_scheme_details on EXAM_exam_course_scheme_details.exam_id = EXAM_definition.id" +
					" where program.program_type_id=:programType and EXAM_definition.exam_type_id=1" +
					" and EXAM_exam_course_scheme_details.scheme_no = :schemeNo";
			Query selectedCandidatesQuery=session.createSQLQuery(query)
												 .setString("programType", tcDetailsForm.getProgramTypeId())
												 .setString("schemeNo", tcDetailsForm.getSchemeNo());
			examdef = (List<Object[]>) selectedCandidatesQuery.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return examdef;		
	}
	
	public List<Object[]> getSubjectsForAllStudentsByClass(TCDetailsForm tcDetailsForm)throws Exception
	{
		Session session = null;
		Transaction transaction = null;
		List<Object[]> departments = null;
		Classes classes = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			classes=getClasses(tcDetailsForm);	
			Query query=session.createQuery("select c.classes.termNumber from ClassSchemewise c where c.id="+tcDetailsForm.getClassId());
			Integer termNumber = (Integer) query.uniqueResult();
			String sqlQuery="select distinct department.name, subject.subject_type_id from class_schemewise" +
					" inner join classes ON class_schemewise.class_id = classes.id " +
					" inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
					" inner join curriculum_scheme_subject ON curriculum_scheme_subject.curriculum_scheme_duration_id = curriculum_scheme_duration.id" +
					" inner join subject_group ON curriculum_scheme_subject.subject_group_id = subject_group.id" +
					" inner join subject_group_subjects on subject_group_subjects.subject_group_id = subject_group.id" +
					" inner join subject ON subject_group_subjects.subject_id = subject.id" +
					" inner join department on subject.department_id = department.id" +
					" where classes.id=:classId and classes.term_number=:termNumber";
			Query sq=session.createSQLQuery(sqlQuery);
			sq.setString("classId", tcDetailsForm.getClassId());
			sq.setInteger("termNumber", termNumber);
			departments = sq.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return departments;
		
	}

	public Classes getClasses(TCDetailsForm tcDetailsForm)
	{
		Session session = null;
		Transaction transaction = null;
		Classes classes = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			String cls = " from Classes c where c.id="+tcDetailsForm.getClassId();
			Query selectedCandidateClassQuery=session.createQuery(cls);
			classes = (Classes) selectedCandidateClassQuery.uniqueResult();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
		
	}
	@Override
	public ExamDefinitionBO getExamForAllStudentsByClass(
			TCDetailsForm tcDetailsForm) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<CurriculumSchemeDuration> getCurriculumSchemeDuration(String string, int studentId) throws Exception{
		Session session = null;
		Transaction transaction = null;
		List<CurriculumSchemeDuration> list = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			
			String query = "from CurriculumSchemeDuration c " +
					"where c.curriculumScheme.id=(select s.classSchemewise.curriculumSchemeDuration.id " +
					"from Student s where s.id="+studentId+") " +
							"order by c.semesterYearNo desc";
			Query selectedCandidatesQuery=session.createQuery(query);
			list = selectedCandidatesQuery.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Subject> getStudentSubjects(int applnId)throws Exception
	{
		Session session = null;
		Transaction transaction = null;
		List<Subject> selectedCandidatesList = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			String query="select s " +
					"from AdmAppln aa join aa.applicantSubjectGroups asg " +
					"join asg.subjectGroup sg join sg.subjectGroupSubjectses sgs " +
					"join sgs.subject s where aa.id="+applnId+" and sg.isActive=1 and sgs.isActive=1 and s.isActive=1";	
			Query selectedCandidatesQuery=session.createQuery(query);
			selectedCandidatesList = selectedCandidatesQuery.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedCandidatesList;
		
	}
	
	public ExamDefinitionBO getStudentExamName(int studentId,CurriculumSchemeDuration csd) throws Exception{
		Session session = null;
		Transaction transaction = null;
		ExamDefinitionBO selectedCandidatesList = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Integer semno = csd.getSemesterYearNo();
			String classid="select c.classes.id from ClassSchemewise c" +
					" where c.curriculumSchemeDuration.id = " +
					" (select s.classSchemewise.curriculumSchemeDuration.id " +
					" from Student s where s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+semno+" and s.id="+studentId+")	 ";
			Query selectedCandidateClassQuery=session.createQuery(classid);
			Integer clsid = (Integer) selectedCandidateClassQuery.uniqueResult();
			if(clsid==null)
			{
				clsid=0;
			}
			String query="select m.exam from MarksEntry m " +
					"where m.classes.id="+clsid+" and m.student.id="+studentId+" " +
					"and m.exam.examTypeUtilBO.name='regular'";	
			Query selectedCandidatesQuery=session.createQuery(query);
			selectedCandidatesList = (ExamDefinitionBO)selectedCandidatesQuery.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedCandidatesList;
	}
	
public Boolean updateStudentTCNo(Student student)throws Exception {
Session session = null;
Transaction transaction = null;
try {
	SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	session = sessionFactory.openSession();
	transaction = session.beginTransaction();
	transaction.begin();

	if(student!=null){
        session.merge(student);
		transaction.commit();
		session.flush();
		session.close();
		return true;
	}
	else
		return false;

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

	
	
}
