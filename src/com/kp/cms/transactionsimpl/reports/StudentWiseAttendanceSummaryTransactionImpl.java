package com.kp.cms.transactionsimpl.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.AttendanceStudent;
import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.PrincipalRemarks;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentRemarks;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.SubjectGroupSubjects;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamMarksEntryDetailsBO;
import com.kp.cms.bo.exam.ExamPublishExamResultsBO;
import com.kp.cms.bo.exam.ExamSubDefinitionCourseWiseBO;
import com.kp.cms.bo.exam.ExamSubjectRuleSettingsSubInternalBO;
import com.kp.cms.bo.exam.MarksEntryDetails;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.reports.StudentWiseAttendanceSummaryForm;
import com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;

public class StudentWiseAttendanceSummaryTransactionImpl implements IStudentWiseAttendanceSummaryTransaction {
	private static final Log log = LogFactory.getLog(StudentWiseAttendanceSummaryTransactionImpl.class);
	/** 
	 * @see com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction#getStudentWiseAttendanceSummaryInformation(java.lang.String)
	 */
	@Override
	public List<Object[]> getStudentWiseAttendanceSummaryInformation(
			String absenceInformationQuery) throws ApplicationException {
		Session session = null;
		List<Object[]> studentSearchResult = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			Query studentQuery = session
					.createQuery(absenceInformationQuery);
			studentSearchResult = studentQuery.list();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				////session.close();
			}
		}
		return studentSearchResult;
	}

	/**
	 * Used to get Student by Regd/Roll No.
	 */
	public Student getStudentByRegdRollNo(int registerNo) throws Exception {
		log.info("Entering into StudentWiseAttendanceSummaryTransactionImpl of getStudentByRegdRollNo");
		//SessionFactory sessionFactory = InitSessionFactory.getInstance();		
		Session session = null;
		Student student = null;
	
		try { 
		 //session =sessionFactory.openSession();
		 session = HibernateUtil.getSession();
		 Query query1 = session.createQuery("from Student s where s.isActive = 1 and s.id = :registerNo");				                          
		 query1.setInteger("registerNo", registerNo);
		 student = (Student) query1.uniqueResult();		 
		 return student;
		}
		catch (Exception e) {	
			log.error("Exception occured while getDetailByRoomTypeId in RoomTypeTransactionImpl :"+e);
			throw  new ApplicationException(e);
		} finally {
		if (session != null) {
			//sessionFactory.close();
			session.flush();
			////session.close();
		}
		log.info("Leaving into StudentWiseAttendanceSummaryTransactionImpl of getStudentByRegdRollNo");
		}
	}

	/**
	 * Used to get Students 
	 */
	public List<Object[]> getStudentBySearch(String dyanmicQuery) throws Exception {
		log.info("Entering into StudentWiseAttendanceSummaryTransactionImpl of getStudentBySearch");
		//SessionFactory sessionFactory = InitSessionFactory.getInstance();		
		Session session = null;
		try { 
		 //session =sessionFactory.openSession();
		 session = HibernateUtil.getSession();
		 Query query = session.createQuery(dyanmicQuery);				                          
		 List<Object[]> studentList = query.list();
			 
		 return studentList;
		}
		catch (Exception e) {	
			log.error("Exception occured while getStudentBySearch in StudentWiseAttendanceSummaryTransactionImpl :"+e);
			throw  new ApplicationException(e);
		} finally {
		if (session != null) {
			//sessionFactory.close();
			session.flush();
			////session.close();
		}
		log.info("Leaving into StudentWiseAttendanceSummaryTransactionImpl of getStudentBySearch");
		}
	}

	/**
	 * Used to get absence period informations
	 */
	public List<AttendanceStudent> getAbsencePeriodDetails(
			String absenceSearchCriteria)throws Exception {
		log.info("Entering into StudentWiseAttendanceSummaryTransactionImpl of getAbsencePeriodDetails");
		//SessionFactory sessionFactory = InitSessionFactory.getInstance();		
		Session session = null;
		try { 
		 //session = sessionFactory.openSession();
		 session = HibernateUtil.getSession();
		 Query query = session.createQuery(absenceSearchCriteria);				                          
		 List<AttendanceStudent> absencePeriodList = query.list();			 
		 return absencePeriodList;
		}
		catch (Exception e) {	
			log.error("Exception occured while getAbsencePeriodDetails in StudentWiseAttendanceSummaryTransactionImpl :"+e);
			throw  new ApplicationException(e);
		} finally {
		if (session != null) {
			//sessionFactory.close();
			session.flush();
			////session.close();
		}
		log.info("Leaving into StudentWiseAttendanceSummaryTransactionImpl of getAbsencePeriodDetails");
		}
	}
	/**
	 * 
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public List<StudentRemarks> getStaffRemarks(int studentId) throws Exception {
		Session session =null;
		
		List<StudentRemarks> list = null;
		try {
			/* SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session =sessionFactory.openSession();*/
			session = HibernateUtil.getSession();
			 Query query = session.createQuery(" from StudentRemarks s where s.student.id =" + studentId);
			
			 list=query.list();

			 session.flush();
			//session.close();
		 } catch (Exception e) {

			 session.flush();
			 ////session.close();
			 log.error("Error while getting getStaffRemarks.."+e);
			 throw  new ApplicationException(e); 
		 }
		return list;
	}
	/**
	 * 
	 * @param principalRemarks
	 * @return
	 * @throws Exception
	 */
	public boolean addPrincipalRemarks(PrincipalRemarks principalRemarks) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			//SessionFactory sessionFactory = InitSessionFactory.getInstance();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.saveOrUpdate(principalRemarks);
			transaction.commit();
			session.flush();
			//session.close();
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			log.error("Error during saving addPrincipalRemarks data..." ,e);
			throw new BusinessException(e);
		} catch (Exception e) {
			transaction.rollback();
			log.error("Error during saving addPrincipalRemarks data..." ,e);
			throw new ApplicationException(e);
		}
	}

	/**
	 * 
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public PrincipalRemarks getPricipalRemarks(int studentId) throws Exception {
		Session session =null;
		
		List<PrincipalRemarks> list = null;
		PrincipalRemarks principalRemarks = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 //session =sessionFactory.openSession();
			 session = HibernateUtil.getSession();

			 Query query = session.createQuery(" from PrincipalRemarks s where s.student.id =" + studentId);
			
			 list=query.list();
			 if(list!= null && list.size() > 0){
				 principalRemarks =  list.get(0);
			 }

			 session.flush();
			//session.close();
		 } catch (Exception e) {

			 session.flush();
			 //session.close();
			 log.error("Error while getting getStaffRemarks.."+e);
			 throw  new ApplicationException(e); 
		 }
		return principalRemarks;
	}

	@Override
	public List<Integer> getPeriodList(
			StudentWiseAttendanceSummaryForm attendanceSummaryForm)
			throws Exception {
		Session session =null;
		List<Integer> list = null;
		try {
			 /*SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session =sessionFactory.openSession();*/
			session = HibernateUtil.getSession();
			 Query query = session.createSQLQuery("SELECT period.id  FROM (period period " +
			 		"INNER JOIN class_schemewise class_schemewise " +
			 		"ON (period.class_schemewise_id = class_schemewise.id))" +
			 		" INNER JOIN student student ON (student.class_schemewise_id = class_schemewise.id) WHERE student.id =" + Integer.parseInt(attendanceSummaryForm.getStudentID()));
			 list=query.list();
			 session.flush();
			//session.close();
		 } catch (Exception e) {
			 session.flush();
			 //session.close();
			 log.error("Error while getting periodlist for student.."+e);
			 throw  new ApplicationException(e); 
		 }
		 return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MarksEntryDetails> getStudentWiseExamMarkDetailsView(int studentId, int classId) throws ApplicationException {
		Session session = null;
		List<MarksEntryDetails> examMarksEntryDetailsBOList = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("from MarksEntryDetails md where md.marksEntry.exam.examTypeID in (4,5) and md.marksEntry.student.id = "+ studentId +" and md.marksEntry.classes.id= " +classId);
			 examMarksEntryDetailsBOList = studentQuery.list();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				////session.close();
			}
		}
		return examMarksEntryDetailsBOList;
	}
	
	public List<ExamMarksEntryDetailsBO> getStudentWiseExamMarkDetails(int studentId) throws ApplicationException {
		Session session = null;
		List<ExamMarksEntryDetailsBO> examMarksEntryDetailsBOList = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			String query = "";
			boolean isCjc = CMSConstants.LINK_FOR_CJC;
			if(isCjc){
				query ="from ExamMarksEntryDetailsBO det where det.examMarksEntryBO.examDefinitionBO.examTypeID in(4,5) and det.examMarksEntryBO.studentId = " + studentId+" and det.subjectUtilBO.code !='VED'";
			}else{
				query = "from ExamMarksEntryDetailsBO det where det.examMarksEntryBO.examDefinitionBO.examTypeID in(4,5) and det.examMarksEntryBO.studentId = " + studentId;
			}
			Query studentQuery = session.createQuery(query);
			examMarksEntryDetailsBOList = studentQuery.list();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				////session.close();
			}
		}
		return examMarksEntryDetailsBOList;
	}	
	/**
	 * 
	 * @param groupIds
	 * @return
	 * @throws ApplicationException
	 */
	public HashMap<Integer, String> getSubjectsBySubjectGroupId(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 ");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){  
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getName());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}
	
	public HashMap<Integer, String> getSubjectsBySubjectGroupIdCJC(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and (grp.subject.isAdditionalSubject=0 or grp.subject.isAdditionalSubject is null)");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){  
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getName());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}	
	
	public HashMap<Integer, String> getSubjectsBySubjectGroupIdLogin(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and grp.subject.isAdditionalSubject=0 or grp.subject.isAdditionalSubject is null)");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getName());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}
	
	public HashMap<Integer, String> getSubjectsBySubjectGroupIdAdditional(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and grp.subject.isAdditionalSubject=1");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getName());
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}
	/**
	 * 
	 * @param classId
	 * @return
	 * @throws ApplicationException
	 */
	public List<Integer> getExamPublishedIds(int classId) throws ApplicationException {
		Session session = null;
		List<ExamPublishExamResultsBO> examPublishExamResultsBOList = null;
		List<Integer> publishedExamIds = new ArrayList<Integer>();
		try {
			session = HibernateUtil.getSession();
			Query examQuery = session.createQuery("from ExamPublishExamResultsBO res where res.classId = " + classId);
			examPublishExamResultsBOList = examQuery.list();
			
			if(examPublishExamResultsBOList!= null && examPublishExamResultsBOList.size() > 0){
				Iterator<ExamPublishExamResultsBO> itr = examPublishExamResultsBOList.iterator();
				while (itr.hasNext()) {
					ExamPublishExamResultsBO examPublishExamResultsBO = (ExamPublishExamResultsBO) itr
							.next();
					publishedExamIds.add(examPublishExamResultsBO.getExamId());
				}
			}
			
			

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return publishedExamIds;
	}
	
	/**
	 * 
	 * @param studentId
	 * @return
	 * @throws ApplicationException
	 */
	public Integer getClassId(int studentId) throws ApplicationException {
		Session session = null;
		
		Integer classId = -1;
		try {
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("select classSchemewise.classes.id from Student st where st.id = " + studentId);

			List<Object[]> obj = studentQuery.list();
			if(obj!= null && obj.size() > 0){
				Iterator<Object[]> itr = obj.iterator();
				while (itr.hasNext()) {
					Object id = itr.next();
					if(id!=null)
					classId = Integer.parseInt(id.toString());
				}
			}

			return classId;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}	
	
	public Integer getClassIdPrevious(int studentId, int schemeNo) throws ApplicationException {
		Session session = null;
		
		Integer classId = -1;
		try {
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("select classId from ExamStudentPreviousClassDetailsBO where studentId = '"+studentId+"' and schemeNo='"+schemeNo+"'");

			List<Object[]> obj = studentQuery.list();
			if(obj!= null && obj.size() > 0){
				Iterator<Object[]> itr = obj.iterator();
				while (itr.hasNext()) {
					Object id = itr.next();
					if(id!=null)
					classId = Integer.parseInt(id.toString());
				}
			}

			return classId;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public Map<Integer, Integer> getSubjectOrder(int courseId,int semNo,int semesterAcademicYear) throws ApplicationException {
		Session session = null;
		
		Map<Integer, Integer> orderMap = new HashMap<Integer, Integer>();
		try {
			session = HibernateUtil.getSession();
			String q="from ExamSubDefinitionCourseWiseBO e where e.courseId = "+ courseId;
			if(semNo>0)
				q= q+" and e.schemeNo=" +semNo;
			if(semesterAcademicYear>0)
				q= q+" and e.academicYear=" +semesterAcademicYear;
			Query query = session.createQuery(q);

			List<ExamSubDefinitionCourseWiseBO> examSubDefinitionCourseWiseBOList = query.list();
			if(examSubDefinitionCourseWiseBOList!= null && examSubDefinitionCourseWiseBOList.size() > 0){
				Iterator<ExamSubDefinitionCourseWiseBO> itr = examSubDefinitionCourseWiseBOList.iterator();
				while (itr.hasNext()) {
					ExamSubDefinitionCourseWiseBO examSubDefinitionCourseWiseBO = itr.next();
					orderMap.put(examSubDefinitionCourseWiseBO.getSubjectId(), examSubDefinitionCourseWiseBO.getSubjectOrder());
				}
			}
			return orderMap;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}
	/**
	 * 
	 * @param groupIds
	 * @return
	 * @throws ApplicationException
	 */
	public HashMap<Integer, String> getSubjectCodesBySubjectGroupId(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getCode());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}
	public HashMap<Integer, String> getSubjectCodesBySubjectGroupIdCJC(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and (grp.subject.isAdditionalSubject=0 or grp.subject.isAdditionalSubject is null)");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getCode());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction#getMaxMarksFromExamSubjectRuleSettingsSubInternal(int, int)
	 */
	@Override
	public List<ExamSubjectRuleSettingsSubInternalBO> getMaxMarksFromExamSubjectRuleSettingsSubInternal(
			int internalExamTypeId, int subjectId, String courseId, int studentId)
			throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query stuQuery = session.createQuery("from Student s where s.id = '"+studentId+"'");
			List<Student> studentList =  stuQuery.list();
			Iterator<Student> stuIterator = studentList.iterator();
			String academicYear = "";
			String semNo = "";
			while (stuIterator.hasNext()) {
				Student student = (Student) stuIterator.next();
				academicYear = String.valueOf(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear());
				semNo = String.valueOf(student.getClassSchemewise().getCurriculumSchemeDuration().getSemesterYearNo());
			}
			Query examQuery = session.createQuery("select e.id from ExamSubjectRuleSettingsBO e where e.academicYear = '"+academicYear+"'"+
												  " and e.schemeNo = '"+semNo+"' and e.courseId = '"+courseId+"' and e.subjectId = '"+subjectId+"'");
			List<Integer> examSubjectRuleSettingsId = examQuery.list();
			if(examSubjectRuleSettingsId!=null && !examSubjectRuleSettingsId.isEmpty()){
			Query query = session.createQuery("from ExamSubjectRuleSettingsSubInternalBO e where e.examSubjectRuleSettingsBO.id in (:examSubjectRuleIdList) and e.internalExamTypeId = '"+internalExamTypeId+"'");
			query.setParameterList("examSubjectRuleIdList", examSubjectRuleSettingsId);
			List<ExamSubjectRuleSettingsSubInternalBO> examSubjectRuleSettingsSubInternalBOs = query.list();
			return examSubjectRuleSettingsSubInternalBOs;
			}else
				return new ArrayList<ExamSubjectRuleSettingsSubInternalBO>();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction#getExamDefinationList(java.util.List)
	 */
	@Override
	public List<ExamDefinitionBO> getExamDefinationList(List<String> examCodeList)
			throws Exception {
		Session session = null;
		List<ExamDefinitionBO> examDefinationBOList = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			Query examQuery = session.createQuery("select e from ExamDefinitionBO e where e.examCode in (:ExamCode) order by e.year,month  ");
			examQuery.setParameterList("ExamCode", examCodeList);
			examDefinationBOList = examQuery.list();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		
		return examDefinationBOList;
		
	}

	@Override
	public List<Subject> getSubjectsListForStudent(int studentId)
			throws Exception {
		Session session = null;
		List<Subject> subject = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			Query examQuery = session.createQuery("from Student s where s.id ='"+studentId+"'");
			Student student= (Student)examQuery.uniqueResult();
			int applnId = student.getAdmAppln().getId();
			String subQuery =   "select sub.subject from SubjectGroupSubjects sub"+ 
								" where sub.subjectGroup.id in "+
								" (select app.subjectGroup.id from ApplicantSubjectGroup app where app.admAppln.id = '"+applnId+"')"+
								" and sub.isActive = 1";
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
	
	public HashMap<Integer, String> getSubjectCodesBySubjectGroupIdCJCAdditional(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and grp.subject.isAdditionalSubject=1");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getCode());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}
	public HashMap<Integer, String> getSubjectsBySubjectGroupIdCJCAdditional(String groupIds) throws ApplicationException {
		Session session = null;
		List<SubjectGroupSubjects> subjectGroupSubjectsList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createQuery("from SubjectGroupSubjects grp where grp.subjectGroup.id in (" + groupIds + ") and grp.isActive=1 and grp.subject.isAdditionalSubject=1 ");
			subjectGroupSubjectsList = subjectQuery.list();
			if(subjectGroupSubjectsList!= null && subjectGroupSubjectsList.size() > 0){  
				Iterator<SubjectGroupSubjects>  subItr = subjectGroupSubjectsList.iterator();
				while (subItr.hasNext()) {
					SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subItr
							.next();
					if(subjectGroupSubjects.getSubject()!=null && subjectGroupSubjects.getSubject().getIsActive())
					subjectMap.put(subjectGroupSubjects.getSubject().getId(), subjectGroupSubjects.getSubject().getName());
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}	
	
	@SuppressWarnings("unchecked")
	public List<MarksEntryDetails> getStudentWiseExamMarkDetailsViewAdditional(int studentId, int classId) throws ApplicationException {
		Session session = null;
		List<MarksEntryDetails> examMarksEntryDetailsBOList = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("from MarksEntryDetails md where md.marksEntry.exam.examTypeID in (4,5) and md.marksEntry.student.id = "+ studentId +" and md.marksEntry.classes.id= " +classId);
			 examMarksEntryDetailsBOList = studentQuery.list();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				////session.close();
			}
		}
		return examMarksEntryDetailsBOList;
	}

	/* (non-Javadoc)
	 * retuns student semester and semester's academic year
	 * @see com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction#getSudentSemAcademicYear(int)
	 */
	@Override
	public Student getSudentSemAcademicYear(int studentId) throws Exception {
		Session session = null;
		Student stu = null;
		try {
			//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			//session = sessionFactory.openSession();
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("from Student s where s.id="+studentId);
			stu = (Student)studentQuery.uniqueResult();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				////session.close();
			}
		}
		return stu;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.reports.IStudentWiseAttendanceSummaryTransaction#getClassSchemeSemAcademicYear(int)
	 */
	@Override
	public ClassSchemewise getClassSchemeSemAcademicYear(int classId) throws Exception {
		Session session = null;
		ClassSchemewise clas = null;
		try {
			session = HibernateUtil.getSession();
			Query studentQuery = session.createQuery("from ClassSchemewise s where s.classes.id="+classId);
			clas = (ClassSchemewise)studentQuery.uniqueResult();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return clas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, String> setPreviousClassId(String studentid) throws Exception {
		Session session = null;
		List<Object[]> classesList = null;
		Map<Integer, String> classMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(" select classes.id,classes.name from student inner join EXAM_student_previous_class_details on EXAM_student_previous_class_details.student_id = student.id" +
					                                 " inner join classes ON EXAM_student_previous_class_details.class_id = classes.id " +
					                                 " where student.id="+studentid+" and classes.is_active=1");
			classesList = query.list();
			if(classesList!= null && classesList.size() > 0){  
				Iterator<Object[]>  claItr = classesList.iterator();
				while (claItr.hasNext()) {
					Object[] classAll = (Object[]) claItr.next();
					if(classAll!=null)
						classMap.put(Integer.parseInt(classAll[0].toString()),classAll[1].toString());
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		classMap=CommonUtil.sortMapByValueDesc(classMap);
		return (HashMap<Integer, String>) classMap;
	}

	@Override
	public HashMap<Integer, String> getSubjectsByClassId(String classesId)throws Exception {
		Session session = null;
		List<Object[]> objList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createSQLQuery(" select subject.id,subject.name from attendance inner join attendance_class on attendance_class.attendance_id = attendance.id" +
					                                    " inner join class_schemewise ON attendance_class.class_schemewise_id = class_schemewise.id inner join classes on class_schemewise.class_id = classes.id " +
					                                    " inner join subject ON attendance.subject_id = subject.id inner join subject_group_subjects on subject_group_subjects.subject_id = subject.id " +
					                                    " and subject_group_subjects.is_active=1 where attendance.is_canceled=0 and (subject.is_additional_subject=0 or subject.is_additional_subject=null)" +
					                                    " and classes.id="+classesId+" group by subject.id ");
			objList = subjectQuery.list();
			if(objList!= null && objList.size() > 0){
				Iterator<Object[]>  subItr = objList.iterator();
				while (subItr.hasNext()) {
					Object[] obj = (Object[]) subItr.next();
					if(obj!=null)
					subjectMap.put(Integer.parseInt(obj[0].toString()),obj[1].toString());
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}

	@Override
	public HashMap<Integer, String> getSubjectsByClassIdAdditional(	String classesId) throws Exception {
		Session session = null;
		List<Object[]> objList = null;
		Map<Integer, String> subjectMap = new HashMap<Integer, String>();
		try {
			session = HibernateUtil.getSession();
			Query subjectQuery = session.createSQLQuery(" select subject.id,subject.name from attendance inner join attendance_class on attendance_class.attendance_id = attendance.id" +
					                                    " inner join class_schemewise ON attendance_class.class_schemewise_id = class_schemewise.id inner join classes on class_schemewise.class_id = classes.id " +
					                                    " inner join subject ON attendance.subject_id = subject.id inner join subject_group_subjects on subject_group_subjects.subject_id = subject.id " +
					                                    " and subject_group_subjects.is_active=1 where attendance.is_canceled=0 and subject.is_additional_subject=1 " +
					                                    " and classes.id="+classesId+" group by subject.id ");
			objList = subjectQuery.list();
			if(objList!= null && objList.size() > 0){
				Iterator<Object[]>  subItr = objList.iterator();
				while (subItr.hasNext()) {
					Object[] obj = (Object[]) subItr.next();
					if(obj!=null)
					subjectMap.put(Integer.parseInt(obj[0].toString()),obj[1].toString());
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return (HashMap<Integer, String>) subjectMap;
	}

	@Override
	public List<String> getPreviousPeriodList(	StudentWiseAttendanceSummaryForm attendanceSummaryForm)	throws Exception {
		Session session =null;
		List<String> list = null;
		try {
			 /*SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session =sessionFactory.openSession();*/
			session = HibernateUtil.getSession();
			 Query query = session.createSQLQuery(" SELECT period.period_name from student inner join attendance_student on attendance_student.student_id = student.id inner join attendance ON attendance_student.attendance_id = attendance.id " +
			 		" inner join attendance_class on attendance_class.attendance_id = attendance.id inner join class_schemewise ON attendance_class.class_schemewise_id = class_schemewise.id inner join classes on class_schemewise.class_id = classes.id " +
			 		" inner join subject ON attendance.subject_id = subject.id " +
			 		" inner join attendance_period on attendance_period.attendance_id = attendance.id inner join period ON attendance_period.period_id = period.id /* and period.class_schemewise_id = class_schemewise.id */" +
			 		" where attendance_student.is_present=0 and attendance.is_canceled=0" +
			 		" and classes.id="+attendanceSummaryForm.getClassesId()+
			 		" and student.id ="+Integer.parseInt(attendanceSummaryForm.getStudentID())+
			 		" and subject.id ="+Integer.parseInt(attendanceSummaryForm.getSubjectId())+
			 		" group by period.period_name " );
			 list=query.list();
			 session.flush();
			//session.close();
		 } catch (Exception e) {
			 session.flush();
			 //session.close();
			 log.error("Error while getting periodlist for student.."+e);
			 throw  new ApplicationException(e); 
		 }
		 return list;
	}

	@Override
	public String getClassesName (String classesId) throws Exception {
		Session session =null;
		String str ="";
		try {
			session = HibernateUtil.getSession();
			 Query query = session.createSQLQuery("select classes.name from classes where classes.id="+classesId+" and classes.is_active=1");
			 str=(String) query.uniqueResult();
			 session.flush();
		 } catch (Exception e) {
			 session.flush();
			 throw  new ApplicationException(e); 
		 }
		 return str;
	}
}
