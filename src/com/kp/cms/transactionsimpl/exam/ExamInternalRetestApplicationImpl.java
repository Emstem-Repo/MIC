package com.kp.cms.transactionsimpl.exam;

/**
 * Mar 2, 2010 Created By 9Elements Team
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.exam.ExamInternalRetestApplicationBO;
import com.kp.cms.bo.exam.ExamInternalRetestApplicationSubjectsBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.to.exam.ExamInternalRetestApplicationSubjectsTO;
import com.kp.cms.to.exam.ExamInternalRetestApplicationTO;
import com.kp.cms.utilities.HibernateUtil;

@SuppressWarnings("unchecked")
public class ExamInternalRetestApplicationImpl extends ExamGenImpl {

	// On SEARCH to get the student details

	public List<Object[]> select_student_details(Integer examId,
			Integer classId, String rollNo, String regNo,
			boolean rollNoPresent, boolean regNoPresent) throws BusinessException {
		Session session=null;

		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();

			String HQL_QUERY = "select e.studentUtilBO.classSchemewiseUtilBO.classUtilBO.name, e.studentUtilBO.registerNo,"
				+ " e.studentUtilBO.rollNo, e.studentUtilBO.admApplnUtilBO.personalDataUtilBO.firstName, e.id"
				+ " from ExamInternalRetestApplicationBO e"
				+ " where e.examId = :examId";

			if (classId != null && classId.intValue() != 0) {
				HQL_QUERY = HQL_QUERY + " and e.classId = :classId";

			}
			if (rollNoPresent) {
				HQL_QUERY = HQL_QUERY + " and e.studentUtilBO.rollNo = :rollNo";

			} else if (regNoPresent) {

				HQL_QUERY = HQL_QUERY + " and e.studentUtilBO.registerNo = :regNo";

			}

			Query query = session.createQuery(HQL_QUERY);

			query.setParameter("examId", examId);
			if (classId != null && classId.intValue() != 0) {
				query.setParameter("classId", classId);
			}
			if (rollNoPresent) {
				query.setParameter("rollNo", rollNo);
			} else if (regNoPresent) {
				query.setParameter("regNo", regNo);
			}
			return query.list();

		}catch (Exception e) {
			throw  new BusinessException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	// ADD - to get the exam details for a particular student
	public List<Object[]> get_examDetails(Integer examId, Integer classId,
			String rollNo, String regNo) throws BusinessException {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			boolean rollNoPresent = false;
			boolean regNOPresent = false;
			List<Object[]> list = null;

			if (rollNo != null && rollNo.length() > 0) {
				rollNoPresent = true;
			}
			if (regNo != null && regNo.length() > 0) {
				regNOPresent = true;
			}

			String SQL1 = "";
			if (classId != null && classId.intValue() != 0) {
				SQL1 = " and ira.class_id = :classId";

			}
			String SQL_QUERY = "SELECT a.chance, classes.name, personal_data.first_name, stu.register_no, stu.roll_no, stu.id as stuid, a.id, classes.id as clsId"
				+ "		  FROM    student stu "
				+ "		       LEFT JOIN "
				+ "		          (SELECT ira.student_id, ira.id, ira.chance, ira.class_id "
				+ "		             FROM EXAM_internal_retest_application ira "
				+ "		            WHERE ira.exam_id = :examId  "
				+ SQL1
				+ "								) a "
				+ "		       ON a.student_id = stu.id "
				+ "					 left join class_schemewise ON stu.class_schemewise_id = class_schemewise.id "
				+ "					 left join classes ON class_schemewise.class_id = classes.id "
				+ "					 left join adm_appln ON stu.adm_appln_id = adm_appln.id "
				+ "					 left join personal_data ON adm_appln.personal_data_id = personal_data.id ";

			if (rollNoPresent) {
				SQL_QUERY = SQL_QUERY + " Where stu.roll_no = :rollNo";

			} else if (regNOPresent) {

				SQL_QUERY = SQL_QUERY + " Where stu.register_no = :regNo";

			}

			Query query = session.createSQLQuery(SQL_QUERY);
			if (classId != null && classId.intValue() != 0) {
				query.setParameter("classId", classId);
			}
			if (rollNoPresent) {
				query.setParameter("rollNo", rollNo);
			} else if (regNOPresent) {
				query.setParameter("regNo", regNo);
			}
			query.setParameter("examId", examId);
			list = query.list();
			return list;
		}catch (Exception e) {
			throw  new BusinessException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	// ADD - to get the subjects for a particular student
	public List<Object[]> get_subjectsList(Integer examId, String rollNo,
			String regNo) throws BusinessException {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();


			String SQL_QUERY = " select adm_appln.applied_year, EXAM_definition.academic_year, EXAM_definition.id as exam_id, "+
			" EXAM_definition.name as exam, EXAM_definition.internal_exam_type_id,student.register_no, "+
			" EXAM_internal_exam_type.name as internal_exam_type,student.id as student_id, "+
			" classes.id as class_id, classes.name as class,subject.id as subject_id, "+
			" (select ifnull(e_s.theory_total_attendance_mark,e_s.practical_total_attendance_mark)as att "+
			" from exam_student_overall_internal_mark_details e_s "+
			" where e_s.student_id=student.id and e_s.subject_id=subject.id and e_s.class_id=classes.id ) as att , "+
			" (select exam_subject_rule_settings_sub_internal.maximum_mark from exam_subject_rule_settings_sub_internal "+
			" 		where internal_exam_type_id=4 and exam_subject_rule_settings_sub_internal.subject_rule_settings_id=EXAM_subject_rule_settings.id) as att_max "+		 
			" 		from EXAM_marks_entry  "+
			" 		inner join EXAM_definition ON EXAM_definition.id = EXAM_marks_entry.exam_id "+
			" 		inner join EXAM_marks_entry_details ON EXAM_marks_entry.id = EXAM_marks_entry_details.marks_entry_id "+
			" 		inner join classes ON classes.id = EXAM_marks_entry.class_id "+
			" 		inner join EXAM_internal_exam_type ON EXAM_internal_exam_type.id = EXAM_definition.internal_exam_type_id "+
			" 		inner join course ON course.id = classes.course_id "+
			" 		inner join student ON student.id = EXAM_marks_entry.student_id "+
			" 		inner join subject ON subject.id = EXAM_marks_entry_details.subject_id "+
			" 		inner join adm_appln ON student.adm_appln_id = adm_appln.id "+
			" 		inner join EXAM_sub_definition_coursewise on EXAM_definition.academic_year = EXAM_sub_definition_coursewise.academic_year "+
			" 		and EXAM_sub_definition_coursewise.course_id = course.id "+
			" 		and EXAM_sub_definition_coursewise.subject_id = subject.id "+
			" 		and EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
			" 		inner join EXAM_subject_rule_settings on EXAM_definition.academic_year = EXAM_subject_rule_settings.academic_year "+
			" 		and EXAM_subject_rule_settings.course_id = course.id "+
			" 		and EXAM_subject_rule_settings.subject_id = subject.id "+
			" 		and EXAM_subject_rule_settings.scheme_no = classes.term_number "+
			" 		left join EXAM_subject_rule_settings_sub_internal on EXAM_subject_rule_settings_sub_internal.subject_rule_settings_id = EXAM_subject_rule_settings.id "+
			" 		and EXAM_subject_rule_settings_sub_internal.internal_exam_type_id = EXAM_internal_exam_type.id "+
			" 		inner join program on course.program_id = program.id "+
			" 		where student.id="+rollNo  +
			"				and subject.id in( "+
			" 				select subject.id "+
			" 				from EXAM_student_overall_internal_mark_details "+
			" 				inner join classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id "+
			" 				inner join EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id "+
			" 				inner join student stud ON EXAM_student_overall_internal_mark_details.student_id = stud.id "+
			" 				inner join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id "+
			" 				inner join exam_type ON EXAM_definition.exam_type_id = exam_type.id "+
			" 				inner join adm_appln ON stud.adm_appln_id = adm_appln.id "+
			" 				inner join class_schemewise ON class_schemewise.class_id = classes.id "+
			" 				inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id "+
			" 				inner join course ON adm_appln.selected_course_id = course.id "+
			" 				left join EXAM_sub_definition_coursewise  "+
			" 				ON EXAM_sub_definition_coursewise.subject_id = subject.id "+
			" 				AND EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
			" 				and EXAM_sub_definition_coursewise.course_id = course.id "+
			" 					and EXAM_sub_definition_coursewise.academic_year=curriculum_scheme_duration.academic_year "+
			" 				left join EXAM_subject_rule_settings on  EXAM_subject_rule_settings.course_id = course.id  "+
			" 				and EXAM_subject_rule_settings.subject_id = subject.id "+
			" 				and EXAM_subject_rule_settings.academic_year=curriculum_scheme_duration.academic_year "+ 
			" 				and EXAM_subject_rule_settings.scheme_no = classes.term_number "+
			" 				where  stud.id=student.id "+
			" and if(subject.is_theory_practical='B', "+
			" 		(if(EXAM_student_overall_internal_mark_details.practical_total_mark='AB'|| EXAM_student_overall_internal_mark_details.practical_total_mark='Ab' || "+ 
			" 			EXAM_student_overall_internal_mark_details.practical_total_mark='ab' || EXAM_student_overall_internal_mark_details.practical_total_mark=null || "+
			" 			EXAM_student_overall_internal_mark_details.practical_total_mark is null,0,EXAM_student_overall_internal_mark_details.practical_total_mark)+  "+
			" 		if(EXAM_student_overall_internal_mark_details.theory_total_mark='AB'|| EXAM_student_overall_internal_mark_details.theory_total_mark='Ab' ||  "+
			" 			EXAM_student_overall_internal_mark_details.theory_total_mark='ab' || EXAM_student_overall_internal_mark_details.theory_total_mark=null || "+
			" 			EXAM_student_overall_internal_mark_details.theory_total_mark is null,0,EXAM_student_overall_internal_mark_details.theory_total_mark)), "+
			" 			ifnull(EXAM_student_overall_internal_mark_details.practical_total_mark,EXAM_student_overall_internal_mark_details.theory_total_mark))<ifnull( EXAM_subject_rule_settings.final_theory_internal_minimum_mark,EXAM_subject_rule_settings.final_practical_internal_minimum_mark)";

			Query query = session.createSQLQuery(SQL_QUERY);

			return query.list();
		}catch (Exception e) {
			throw  new BusinessException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	// On EDIT - to get student details
	public List<Object[]> get_subjectsList(int id) throws Exception {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();

			String HQL_QUERY = "select e.subjectUtilBO.code, e.subjectUtilBO.name, "
				+ " e.fees, e.isTheory, e.isPractical, e.subjectUtilBO.id"
				+ " from ExamInternalRetestApplicationSubjectsBO e where e.examInternalRetestApplicationId = :id";

			Query query = session.createQuery(HQL_QUERY);
			query.setParameter("id", id);

			return query.list();

		}catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}

	// On EDIT - to get exam details
	public List<Object[]> get_examDetails(int id) throws Exception {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();

			String HQL_QUERY = "select e.chance, e.studentUtilBO.classSchemewiseUtilBO.classUtilBO.name,"
				+ " e.studentUtilBO.admApplnUtilBO.personalDataUtilBO.firstName, "
				+ " e.studentUtilBO.registerNo, e.studentUtilBO.rollNo,"
				+ " e.studentUtilBO.id, e.id, e.studentUtilBO.classSchemewiseUtilBO.classUtilBO.id from ExamInternalRetestApplicationBO e where e.id = :id";

			Query query = session.createQuery(HQL_QUERY);
			query.setParameter("id", id);

			return query.list();
		}catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}

	// To update
	public void updateSubjectDetails(int id, int subjectId, String fees,
			int theory, int practical, Integer chance) throws Exception {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();

			String HQL_QUERY = "update ExamInternalRetestApplicationSubjectsBO e set e.fees = :fees,"
				+ " e.isTheory = :theory, e.isPractical = :practical"
				+ " where e.examInternalRetestApplicationId = :id and e.subjectId = :subjectId";

			Query query = session.createQuery(HQL_QUERY);
			query.setParameter("id", id);
			query.setParameter("subjectId", subjectId);
			query.setParameter("fees", fees);
			query.setParameter("theory", theory);
			query.setParameter("practical", practical);
			query.executeUpdate();

			String HQL_QUERY1 = "update ExamInternalRetestApplicationBO e set e.chance = :chance"
				+ " where e.id = :id";

			Query query1 = session.createQuery(HQL_QUERY1);
			query1.setParameter("id", id);
			query1.setParameter("chance", chance);
			query1.executeUpdate();
			tx.commit();
		}catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	// To DELETE
	public void deleteIntAppl(int id) throws Exception {

		Session session = null;
		try{
			String HQL_QUERY = "delete from ExamInternalRetestApplicationSubjectsBO e"
				+ " where e.examInternalRetestApplicationId = :id ";

			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery(HQL_QUERY);
			query.setParameter("id", id);

			query.executeUpdate();

			String HQL_QUERY1 = "delete from ExamInternalRetestApplicationBO e"
				+ " where e.id = :id ";

			Query query1 = session.createQuery(HQL_QUERY1);
			query1.setParameter("id", id);
			query1.executeUpdate();
			tx.commit();
		}catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	// To get the id of first table(EXAM_internal_retest_application)
	public int insert_returnId(ExamInternalRetestApplicationBO objBO) throws Exception {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int id = 0;
		try {

			session.save(objBO);

			id = objBO.getId();

			tx.commit();
			session.flush();
			session.close();
			return id;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}

		}
		return id;
	}

	// To get class name based on examId & roll/register no
	public List getClassByExamNameRegNoOnly(int examId, String rollNo,
			String regNo, boolean rollNoPresent, boolean regNOPresent) {
		Session session = null;
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();

		String SQL_QUERY = "select distinct classes.id, classes.name from classes join class_schemewise on"
			+ " class_schemewise.class_id = classes.id"
			+ " join curriculum_scheme_duration on"
			+ " class_schemewise.curriculum_scheme_duration_id ="
			+ " curriculum_scheme_duration.id"
			+ " join curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id"
			+ " = curriculum_scheme.id"
			+ " join student on student.class_schemewise_id = class_schemewise.id join"
			+ " EXAM_exam_course_scheme_details on"
			+ " EXAM_exam_course_scheme_details.course_id = classes.course_id"
			+ " and EXAM_exam_course_scheme_details.exam_id = :examId";

		if (rollNoPresent) {
			SQL_QUERY = SQL_QUERY + " where (student.roll_no = '" + rollNo
			+ "')";

		} else if (regNOPresent) {
			SQL_QUERY = SQL_QUERY + " where (student.register_no = '" + regNo
			+ "' )";

		}

		Query query = session.createSQLQuery(SQL_QUERY);
		query.setParameter("examId", examId);
		return query.list();
	}
	public List<Object[]> get_examDetail(Integer examId, Integer classId) throws BusinessException {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			boolean rollNoPresent = false;
			boolean regNOPresent = false;
			List<Object[]> list = null;



			String SQL_QUERY = " SELECT  classes.name, personal_data.first_name, stu.register_no, stu.roll_no, stu.id as stuid, classes.id as clsId	"+	  
				" FROM    student stu 		       	"+
				" left join class_schemewise ON stu.class_schemewise_id = class_schemewise.id	"+ 					 
				" left join classes ON class_schemewise.class_id = classes.id 					 	"+
				" left join adm_appln ON stu.adm_appln_id = adm_appln.id 					 	"+
				" left join personal_data ON adm_appln.personal_data_id = personal_data.id 	"+
				" left join EXAM_student_overall_internal_mark_details ON exam_student_overall_internal_mark_details.class_id = classes.id	"+
				" and exam_student_overall_internal_mark_details.student_id = stu.id	"+
				" left join EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id	"+
				" left join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id	"+
				" left join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id	"+
				" left join course ON adm_appln.selected_course_id = course.id	"+
				" left join EXAM_subject_rule_settings on  EXAM_subject_rule_settings.course_id = course.id	"+ 
				" and EXAM_subject_rule_settings.subject_id = subject.id	"+
				" and EXAM_subject_rule_settings.academic_year=curriculum_scheme_duration.academic_year	"+ 
				" and EXAM_subject_rule_settings.scheme_no = classes.term_number	"+
				" left join subject_type ON subject_type.id = subject.subject_type_id  "+

				" WHERE classes.id = "+classId+ 
				" and subject_type.id !=13 "+
				" and if(subject.is_theory_practical='B', "+
				" 		(if(EXAM_student_overall_internal_mark_details.practical_total_mark='AB'|| EXAM_student_overall_internal_mark_details.practical_total_mark='Ab' || "+
				" 			EXAM_student_overall_internal_mark_details.practical_total_mark='ab' || EXAM_student_overall_internal_mark_details.practical_total_mark=null || "+
				" 			EXAM_student_overall_internal_mark_details.practical_total_mark is null,0,EXAM_student_overall_internal_mark_details.practical_total_mark)+ "+
				" 		if(EXAM_student_overall_internal_mark_details.theory_total_mark='AB'|| EXAM_student_overall_internal_mark_details.theory_total_mark='Ab' || "+
				" 			EXAM_student_overall_internal_mark_details.theory_total_mark='ab' || EXAM_student_overall_internal_mark_details.theory_total_mark=null || "+
				" 			EXAM_student_overall_internal_mark_details.theory_total_mark is null,0,EXAM_student_overall_internal_mark_details.theory_total_mark)), "+
				" 			ifnull(EXAM_student_overall_internal_mark_details.practical_total_mark,EXAM_student_overall_internal_mark_details.theory_total_mark))<ifnull( EXAM_subject_rule_settings.final_theory_internal_minimum_mark,EXAM_subject_rule_settings.final_practical_internal_minimum_mark) "+
				" 						and EXAM_definition.id=	"+examId+
				" 							group by stu.id	"+
				" 							union all	"+
				" 							SELECT classes.name, personal_data.first_name, stu.register_no, stu.roll_no, stu.id as stuid, classes.id as clsId	"+		  
				" 							FROM    student stu 		       	"+
				" 									left join exam_student_previous_class_details ON exam_student_previous_class_details.student_id = stu.id	"+
				" 									left join classes ON exam_student_previous_class_details.class_id = classes.id 					 	"+
				" 									left join adm_appln ON stu.adm_appln_id = adm_appln.id 					 	"+
				" 									left join personal_data ON adm_appln.personal_data_id = personal_data.id	"+ 
				" 									left join EXAM_student_overall_internal_mark_details ON exam_student_overall_internal_mark_details.class_id = classes.id	"+
				" 										and exam_student_overall_internal_mark_details.student_id = stu.id	"+
				" 									left join EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id	"+
				" 									left join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id	"+
				" 									left join course ON adm_appln.selected_course_id = course.id	"+
				" 									left join EXAM_subject_rule_settings on  EXAM_subject_rule_settings.course_id = course.id	"+ 
				" 									and EXAM_subject_rule_settings.subject_id = subject.id	"+
				" 									and EXAM_subject_rule_settings.academic_year=exam_student_previous_class_details.academic_year	"+ 
				" 									and EXAM_subject_rule_settings.scheme_no = classes.term_number	"+
				" 									left join subject_type ON subject_type.id = subject.subject_type_id  "+
				" 														WHERE classes.id =	"+classId+
				"														and subject_type.id !=13 "+
				" 														and if(subject.is_theory_practical='B', "+
				" 		(if(EXAM_student_overall_internal_mark_details.practical_total_mark='AB'|| EXAM_student_overall_internal_mark_details.practical_total_mark='Ab' ||  "+
				" 			EXAM_student_overall_internal_mark_details.practical_total_mark='ab' || EXAM_student_overall_internal_mark_details.practical_total_mark=null || "+
				" 			EXAM_student_overall_internal_mark_details.practical_total_mark is null,0,EXAM_student_overall_internal_mark_details.practical_total_mark)+ "+
				" 		if(EXAM_student_overall_internal_mark_details.theory_total_mark='AB'|| EXAM_student_overall_internal_mark_details.theory_total_mark='Ab' ||  "+
				" 			EXAM_student_overall_internal_mark_details.theory_total_mark='ab' || EXAM_student_overall_internal_mark_details.theory_total_mark=null || "+
				" 			EXAM_student_overall_internal_mark_details.theory_total_mark is null,0,EXAM_student_overall_internal_mark_details.theory_total_mark)), "+
				" 			ifnull(EXAM_student_overall_internal_mark_details.practical_total_mark,EXAM_student_overall_internal_mark_details.theory_total_mark))<ifnull( EXAM_subject_rule_settings.final_theory_internal_minimum_mark,EXAM_subject_rule_settings.final_practical_internal_minimum_mark) "+
				" 															and EXAM_definition.id=	"+examId+
				" 																group by stu.id	";



				Query query = session.createSQLQuery(SQL_QUERY);
			
			list = query.list();
			return list;
		}catch (Exception e) {
			throw  new BusinessException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}
	public List<Object[]> get_subjectsListFor(Integer examId, int stuId,
			String regNo) throws BusinessException {
		Session session = null;
		try{
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();


			String SQL_QUERY = "select subject.id as subject_id,subject.name,subject.code,  EXAM_definition.id as exam_id,  EXAM_definition.name as exam, EXAM_definition.internal_exam_type_id,EXAM_internal_exam_type.name as internal_exam_type,student.id as student_id,subject.is_theory_practical, "+
			" classes.id as class_id, classes.name as class,subject.id as subject_id, "+
			" (select ifnull(e_s.theory_total_attendance_mark,e_s.practical_total_attendance_mark)as att "+
			" from exam_student_overall_internal_mark_details e_s "+
			" where e_s.student_id=student.id and e_s.subject_id=subject.id and e_s.class_id=classes.id ) as att , "+
			" (select exam_subject_rule_settings_sub_internal.maximum_mark from exam_subject_rule_settings_sub_internal "+
			" 		where internal_exam_type_id=4 and exam_subject_rule_settings_sub_internal.subject_rule_settings_id=EXAM_subject_rule_settings.id) as att_max "+		 
			" 		from EXAM_marks_entry  "+
			" 		inner join EXAM_definition ON EXAM_definition.id = EXAM_marks_entry.exam_id "+
			" 		inner join EXAM_marks_entry_details ON EXAM_marks_entry.id = EXAM_marks_entry_details.marks_entry_id "+
			" 		inner join classes ON classes.id = EXAM_marks_entry.class_id "+
			" 		inner join EXAM_internal_exam_type ON EXAM_internal_exam_type.id = EXAM_definition.internal_exam_type_id "+
			" 		inner join course ON course.id = classes.course_id "+
			" 		inner join student ON student.id = EXAM_marks_entry.student_id "+
			" 		inner join subject ON subject.id = EXAM_marks_entry_details.subject_id "+
			" 		inner join adm_appln ON student.adm_appln_id = adm_appln.id "+
			" 		inner join EXAM_sub_definition_coursewise on EXAM_definition.academic_year = EXAM_sub_definition_coursewise.academic_year "+
			" 		and EXAM_sub_definition_coursewise.course_id = course.id "+
			" 		and EXAM_sub_definition_coursewise.subject_id = subject.id "+
			" 		and EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
			" 		inner join EXAM_subject_rule_settings on EXAM_definition.academic_year = EXAM_subject_rule_settings.academic_year "+
			" 		and EXAM_subject_rule_settings.course_id = course.id "+
			" 		and EXAM_subject_rule_settings.subject_id = subject.id "+
			" 		and EXAM_subject_rule_settings.scheme_no = classes.term_number "+
			" 		left join EXAM_subject_rule_settings_sub_internal on EXAM_subject_rule_settings_sub_internal.subject_rule_settings_id = EXAM_subject_rule_settings.id "+
			" 		and EXAM_subject_rule_settings_sub_internal.internal_exam_type_id = EXAM_internal_exam_type.id "+
			" 		inner join program on course.program_id = program.id "+
			" 		where student.id="+stuId  +
			"				and subject.id in( "+
			" 				select subject.id "+
			" 				from EXAM_student_overall_internal_mark_details "+
			" 				inner join classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id "+
			" 				inner join EXAM_definition ON EXAM_student_overall_internal_mark_details.exam_id = EXAM_definition.id "+
			" 				inner join student stud ON EXAM_student_overall_internal_mark_details.student_id = stud.id "+
			" 				inner join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id "+
			" 				inner join exam_type ON EXAM_definition.exam_type_id = exam_type.id "+
			" 				inner join adm_appln ON stud.adm_appln_id = adm_appln.id "+
			" 				inner join class_schemewise ON class_schemewise.class_id = classes.id "+
			" 				inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id "+
			" 				inner join course ON adm_appln.selected_course_id = course.id "+
			" 				left join EXAM_sub_definition_coursewise  "+
			" 				ON EXAM_sub_definition_coursewise.subject_id = subject.id "+
			" 				AND EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
			" 				and EXAM_sub_definition_coursewise.course_id = course.id "+
			" 					and EXAM_sub_definition_coursewise.academic_year=curriculum_scheme_duration.academic_year "+
			" 				left join EXAM_subject_rule_settings on  EXAM_subject_rule_settings.course_id = course.id  "+
			" 				and EXAM_subject_rule_settings.subject_id = subject.id "+
			" 				and EXAM_subject_rule_settings.academic_year=curriculum_scheme_duration.academic_year "+ 
			" 				and EXAM_subject_rule_settings.scheme_no = classes.term_number "+
			" 				where  stud.id=student.id "+
			" and if(subject.is_theory_practical='B', "+
			" 		(if(EXAM_student_overall_internal_mark_details.practical_total_mark='AB'|| EXAM_student_overall_internal_mark_details.practical_total_mark='Ab' || "+ 
			" 			EXAM_student_overall_internal_mark_details.practical_total_mark='ab' || EXAM_student_overall_internal_mark_details.practical_total_mark=null || "+
			" 			EXAM_student_overall_internal_mark_details.practical_total_mark is null,0,EXAM_student_overall_internal_mark_details.practical_total_mark)+  "+
			" 		if(EXAM_student_overall_internal_mark_details.theory_total_mark='AB'|| EXAM_student_overall_internal_mark_details.theory_total_mark='Ab' ||  "+
			" 			EXAM_student_overall_internal_mark_details.theory_total_mark='ab' || EXAM_student_overall_internal_mark_details.theory_total_mark=null || "+
			" 			EXAM_student_overall_internal_mark_details.theory_total_mark is null,0,EXAM_student_overall_internal_mark_details.theory_total_mark)), "+
			" 			ifnull(EXAM_student_overall_internal_mark_details.practical_total_mark,EXAM_student_overall_internal_mark_details.theory_total_mark))<ifnull( EXAM_subject_rule_settings.final_theory_internal_minimum_mark,EXAM_subject_rule_settings.final_practical_internal_minimum_mark)) group by subject.id";

			Query query = session.createSQLQuery(SQL_QUERY);

			return query.list();
		}catch (Exception e) {
			throw  new BusinessException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}

	}

	public int addStudentsForRetest(List<ExamInternalRetestApplicationTO> stListTo,int clsId) throws ApplicationException {

		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("select rt.studentId from ExamInternalRetestApplicationBO rt where rt.classId="+clsId);
			List stIdList= query.list();
			List<ExamInternalRetestApplicationBO> boList=new ArrayList();
		
		for (ExamInternalRetestApplicationTO to : stListTo) {
			if (stIdList.contains(to.getStudentId())) {
				continue;
			}
			ExamInternalRetestApplicationBO bo=new ExamInternalRetestApplicationBO();
			bo.setAcademicYear(to.getAcademicYear());
			bo.setClassId(to.getClassId());
			bo.setIsActive(true);
			bo.setStudentId(to.getStudentId());
			bo.setCreatedDate(new Date());
			//Set<ExamInternalRetestApplicationSubjectsBO> subBoSet=new HashSet();
			List<ExamInternalRetestApplicationSubjectsBO> subBoSet=new ArrayList();
			for (ExamInternalRetestApplicationSubjectsTO subto : to.getSubjectList()) {
				ExamInternalRetestApplicationSubjectsBO subBo=new ExamInternalRetestApplicationSubjectsBO();
				subBo.setSubjectId(Integer.parseInt(subto.getSubjectId()));
				if (subto.getIsCheckedDummy()) {
					subBo.setIsTheory(1);
				}
				if (subto.getIsCheckedDummyPractical()) {
					subBo.setIsPractical(1);
				}
				System.out.println(bo.getId());
				subBoSet.add(subBo);
			}
			//bo.setSubList(subBoSet);
			session.saveOrUpdate(bo);
			int id=bo.getId();
			for (ExamInternalRetestApplicationSubjectsBO subo : subBoSet) {
				subo.setExamInternalRetestApplicationId(id);
				session.saveOrUpdate(subo);
			}
		}
		tx.commit();
		session.flush();
		session.close();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}

		}
		return 1;
	}

	public int getFromRetestAppliication(Integer stId) {

		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = null;
		int result=0;
		try {
			session=HibernateUtil.getSession();
			Query query = session.createQuery("select rt.studentId from ExamInternalRetestApplicationBO rt where rt.studentId="+stId);
				result=(Integer) query.uniqueResult();
				return result;
		} catch (Exception e) {

		}
		return result;
	}
}
