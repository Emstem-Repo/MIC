package com.kp.cms.helpers.exam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.parser.node.MathUtils;

import com.kp.cms.actions.employee.ExceptionDetailsAction;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ConsolidateMarksCard;
import com.kp.cms.bo.exam.ConsolidatedMarksCardProgrammePart;
import com.kp.cms.bo.exam.ConsolidatedSubjectSection;
import com.kp.cms.bo.exam.ConsolidatedSubjectStream;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamGradeDefinitionBO;
import com.kp.cms.bo.exam.ExamSubjectSectionMasterBO;
import com.kp.cms.bo.exam.InternalMarkSupplementaryDetails;
import com.kp.cms.bo.exam.UGConsolidateMarksCardBO;
import com.kp.cms.forms.exam.ConsolidatedMarksCardForm;
import com.kp.cms.to.exam.ConsolidateMaxTo;
import com.kp.cms.to.exam.NewConsolidatedMarksCardTo;
import com.kp.cms.transactions.exam.IConsolidatedMarksCardTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.ConsolidatedMarksCardTransactionImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.EncryptUtil;

public class ConsolidatedMarksCardHelper {
	/**
	 * Singleton object of ConsolidatedMarksCardHelper
	 */
	private static volatile ConsolidatedMarksCardHelper consolidatedMarksCardHelper = null;
	private static final Log log = LogFactory.getLog(ConsolidatedMarksCardHelper.class);
	private ConsolidatedMarksCardHelper() {

	}
	/**
	 * return singleton object of ConsolidatedMarksCardHelper.
	 * @return
	 */
	public static ConsolidatedMarksCardHelper getInstance() {
		if (consolidatedMarksCardHelper == null) {
			consolidatedMarksCardHelper = new ConsolidatedMarksCardHelper();
		}
		return consolidatedMarksCardHelper;
	}
	/**
	 * @param consolidatedMarksCardForm
	 * @return
	 */
	public String getStudentsQuery( String courseId,String year) throws Exception {
		log.info("entered into getStudentsQuery");
		String query="select s.id from Student s where (s.isHide=0 or s.isHide is null) and s.admAppln.isCancelled=0 and s.isAdmitted=true " +
		" and s.admAppln.courseBySelectedCourseId.id=" +courseId+
		" and s.admAppln.appliedYear="+year;
		log.info("exit from getStudentsQuery");
		return query;
	}
	/**
	 * @param detailMap
	 * @return
	 */
	public List<UGConsolidateMarksCardBO> getConsolidateMarksBO( Map<Integer, Map<Integer, NewConsolidatedMarksCardTo>> detailMap) throws Exception {
		IConsolidatedMarksCardTransaction transaction=ConsolidatedMarksCardTransactionImpl.getInstance();
		Iterator it = detailMap.entrySet().iterator();
		NewConsolidatedMarksCardTo to;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			int studentId=Integer.parseInt(pairs.getKey().toString());
			Map<Integer,NewConsolidatedMarksCardTo> schemeMap=(Map<Integer,NewConsolidatedMarksCardTo>)pairs.getValue();
			if(schemeMap!=null && !schemeMap.isEmpty()){
				Iterator itr=schemeMap.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry schemePairs = (Map.Entry)itr.next();
					if(schemePairs.getValue()!=null){
						to=(NewConsolidatedMarksCardTo)schemePairs.getValue();
						List<Object[]> list=transaction.getMarksDetails(studentId,to);
					}
				}
			}
		}
		return null;
	}
	/**
	 * @param consolidatedMarksCardForm
	 * @return
	 * @throws Exception
	 */
	public String getConsolidateQuery( ConsolidatedMarksCardForm consolidatedMarksCardForm) throws Exception {
		String query=" select EXAM_definition.id, EXAM_definition.month as exam_month, EXAM_definition.year as exam_year, "+
		" student.id as student_id, classes.id as classId, subject.id as subject_id, "+
		" subject.code as subCode, subject.name as subName, "+
		" ifnull(EXAM_internal_mark_supplementary_details.theory_total_mark, EXAM_student_overall_internal_mark_details.theory_total_mark) "+
		" as theory_total_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.theory_total_attendance_mark, EXAM_student_overall_internal_mark_details.theory_total_attendance_mark) "+
		" as theory_total_attendance_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.practical_total_mark, EXAM_student_overall_internal_mark_details.practical_total_mark) "+
		" as practical_total_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.practical_total_attendance_mark, EXAM_student_overall_internal_mark_details.practical_total_attendance_mark) "+
		" as practical_total_attendance_mark, "+
		" classes.course_id as course_id, "+
		" EXAM_subject_rule_settings.theory_ese_minimum_mark,     "+
		" EXAM_subject_rule_settings.theory_ese_maximum_mark,     "+
		" EXAM_subject_rule_settings.practical_ese_minimum_mark,     "+
		" EXAM_subject_rule_settings.practical_ese_maximum_mark, "+
		" EXAM_student_final_mark_details.student_theory_marks, "+
		" EXAM_student_final_mark_details.student_practical_marks, "+
		" personal_data.first_name,     "+
		" personal_data.middle_name,     "+
		" personal_data.last_name,  "+
		" EXAM_subject_sections.name as section,  "+
		" EXAM_subject_sections.is_initialise,     "+
		" EXAM_subject_sections.id as section_id,     "+
		" EXAM_sub_definition_coursewise.subject_order,     "+
		" EXAM_subject_rule_settings.final_practical_internal_maximum_mark,     "+
		" EXAM_subject_rule_settings.final_theory_internal_maximum_mark,  "+
		" EXAM_sub_definition_coursewise.practical_credit, "+
		" EXAM_sub_definition_coursewise.theory_credit, "+
		" student.register_no, "+
		" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical',if(subject.is_theory_practical='B','Both','Theory'))) as subType, "+
		" classes.term_number as term_number, "+
		" (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0)) as theoryMax,     "+
		" (if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0)) as practicalMax, "+
		" EXAM_sub_definition_coursewise.dont_show_max_marks,     "+
		" EXAM_sub_definition_coursewise.dont_show_min_marks,  "+
		" EXAM_sub_definition_coursewise.show_only_grade,  "+
		" course_scheme.name as schemeName,     "+
		" EXAM_sub_definition_coursewise.dont_show_sub_type,      "+
		" adm_appln.applied_year,  "+
		" EXAM_definition.academic_year, "+
		" adm_appln.selected_course_id,EXAM_subject_rule_settings.final_theory_internal_minimum_mark, " +
		" EXAM_subject_rule_settings.final_practical_internal_minimum_mark ,EXAM_subject_rule_settings.theory_ese_theory_final_minimum_mark,EXAM_subject_rule_settings.practical_ese_theory_final_minimum_mark"+
		" ,EXAM_sub_definition_coursewise.dont_consider_failure_total_result,EXAM_supplementary_improvement_application.is_appeared_theory, EXAM_supplementary_improvement_application.is_appeared_practical,EXAM_sub_definition_coursewise.dont_add_tot_mark_cls_decln,subject.is_certificate_course,if(theory_ese_maximum_mark is null and theory_ese_entered_max_mark is null and practical_ese_maximum_mark is null and practical_ese_entered_max_mark is null, 1, 0) as only_internal,EXAM_supplementary_improvement_application.is_improvement, " +
		
		"(if(EXAM_student_overall_internal_mark_details.theory_total_mark>0,EXAM_student_overall_internal_mark_details.theory_total_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain, "+ 
		"   (if(EXAM_student_overall_internal_mark_details.practical_total_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, "+
		" EXAM_subject_rule_settings.subject_final_minimum as subFinalMin, "+
		" subject.consolidated_subject_stream_id as consolidated_subject_stream_id, "+
		" EXAM_subject_sections.consolidated_subject_section_id as consolidated_subject_section_id, "+
		" program.program_type_id ,"+
		" EXAM_sub_definition_coursewise.show_only_credits, "+
		" subject.is_theory_practical as is_theory_practical,"+
		" subject.subject_type_id as is_subject_type_id"+
		" from student "+
		" inner join adm_appln ON student.adm_appln_id = adm_appln.id "+
		" inner join personal_data ON adm_appln.personal_data_id = personal_data.id "+
		" inner join EXAM_student_overall_internal_mark_details on EXAM_student_overall_internal_mark_details.student_id = student.id "+
		" inner join classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id "+
		" inner join class_schemewise on class_schemewise.class_id = classes.id "+
		" inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id "+
		" inner join curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id "+
		" inner join course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id "+
		" inner join course ON classes.course_id = course.id "+
		" inner join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id "+
		" inner join program on course.program_id = program.id "+
		
		" left join EXAM_student_final_mark_details on EXAM_student_final_mark_details.class_id = classes.id "+
		" and EXAM_student_final_mark_details.student_id = student.id "+
		" and EXAM_student_final_mark_details.subject_id = subject.id and (EXAM_student_final_mark_details.student_theory_marks is not null or "+
		" EXAM_student_final_mark_details.student_practical_marks is not null ) "+
		" inner join EXAM_subject_rule_settings on EXAM_subject_rule_settings.course_id = course.id "+
		" and EXAM_subject_rule_settings.subject_id = subject.id "+
		" and EXAM_subject_rule_settings.scheme_no=classes.term_number "+
		" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year "+
		" left join EXAM_sub_definition_coursewise on EXAM_sub_definition_coursewise.course_id = course.id "+
		" and EXAM_sub_definition_coursewise.subject_id = subject.id "+
		" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
		" and EXAM_sub_definition_coursewise.academic_year=curriculum_scheme_duration.academic_year "+
		" left join EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id "+
		" inner join EXAM_definition ON ifnull(EXAM_student_final_mark_details.exam_id,EXAM_student_overall_internal_mark_details.exam_id) = EXAM_definition.id "+
		" left join EXAM_supplementary_improvement_application on EXAM_supplementary_improvement_application.scheme_no = classes.term_number "+
		" and EXAM_supplementary_improvement_application.student_id = student.id "+
		" and EXAM_supplementary_improvement_application.subject_id = subject.id "+
//		" and EXAM_supplementary_improvement_application.exam_id = EXAM_definition.id "+
		" left join EXAM_internal_mark_supplementary_details on EXAM_internal_mark_supplementary_details.class_id = classes.id "+
//		" and EXAM_internal_mark_supplementary_details.exam_id = EXAM_definition.id "+
		" and EXAM_internal_mark_supplementary_details.student_id = student.id "+
		" and EXAM_internal_mark_supplementary_details.subject_id = subject.id "+
		" where student.id in (:ids)" +
		" group by student_id,EXAM_definition.id,subject.id "+
		" order by term_number, student_id, subject_id,exam_year asc,exam_month asc ";
		
		return query;
	}
	/**
	 * @param list
	 * @param consolidatedMarksCardForm
	 * @return
	 */
	public Map<Integer,Map<String, ConsolidateMarksCard>> getBoListForInput(List<Object[]> list, ConsolidatedMarksCardForm consolidatedMarksCardForm,Map<Integer,Map<Integer,Map<Integer,Boolean>>> certificateMap,List<String> appearedList,String courseId, boolean forConsolidatedProcess) throws Exception {
		Map<Integer,Map<String, ConsolidateMarksCard>> mainMap=new HashMap<Integer, Map<String,ConsolidateMarksCard>>();
		boolean isMaxRecord=true;
		
		if(list!=null && !list.isEmpty()){
			Map<String, ConsolidateMarksCard> semMap;
			ConsolidateMarksCard consolidateMarksCard;
			ExamDefinition exam;
			Student student;
			Classes classes;
			Subject subject;
			Course course;
			int programTypeId=0;
			Iterator<Object[]> itr=list.iterator();
			while (itr.hasNext()) {
				boolean isCertificateCourse=false;
				boolean isOptional=false;

				Object[] obj=itr.next();
				//if(Integer.parseInt(obj[3].toString()) == 16370 && obj[5].toString().equalsIgnoreCase("897")){
				boolean doNotAddMarks=false;
				boolean showOnlyCredits=false;
				if(obj[50]!=null && obj[50].toString().equalsIgnoreCase("true"))
					doNotAddMarks=true;
				if(obj[60]!=null && obj[60].toString().equalsIgnoreCase("true")){
					showOnlyCredits=true;
					doNotAddMarks=false;
				}
				if(!doNotAddMarks){
				if(obj[22]!=null){
					//if(obj[3]!=null && obj[32]!=null && obj[5]!=null /*&& Integer.parseInt(obj[3].toString())==4226 */ && (Integer.parseInt(obj[5].toString())==468)|| Integer.parseInt(obj[5].toString())==768){
						if(obj[3]!=null && obj[32]!=null && obj[5]!=null /*&& Integer.parseInt(obj[3].toString())==4226 && Integer.parseInt(obj[5].toString())==468*/){
											System.out.println(obj[3].toString()+"_"+obj[5].toString());
						//					System.out.println(obj[3].toString());
						if(mainMap.containsKey(Integer.parseInt(obj[3].toString()))){
							semMap=mainMap.get(Integer.parseInt(obj[3].toString()));
							if(semMap.containsKey(Integer.parseInt(obj[32].toString())+"_"+Integer.parseInt(obj[5].toString())+"_"+obj[31].toString())){
								if(!isMaxRecord)
									continue;
							}
							semMap=mainMap.remove(Integer.parseInt(obj[3].toString()));
						}else
							semMap=new HashMap<String, ConsolidateMarksCard>();

						consolidateMarksCard =new ConsolidateMarksCard();
						if(obj[58]!=null)
							programTypeId = Integer.parseInt(obj[58].toString());
						if(obj[56]!=null){
							BigDecimal decVal = new BigDecimal(obj[56].toString());
							consolidateMarksCard.setSubjectFinalMin(decVal);
						}
						// only for display purpose
						if(showOnlyCredits)
							consolidateMarksCard.setShowOnlyCredits(true);
						else
							consolidateMarksCard.setShowOnlyCredits(false);
						if(obj[1]!=null)
							consolidateMarksCard.setMonth(obj[1].toString());
						if(obj[2]!=null)
							consolidateMarksCard.setYear(obj[2].toString());
						if(obj[0]!=null){
							exam=new ExamDefinition();
							exam.setId(Integer.parseInt(obj[0].toString()));
							consolidateMarksCard.setExam(exam);
						}
						if(obj[3]!=null && !obj[3].toString().isEmpty() && StringUtils.isNumeric(obj[3].toString())){
							student=new Student();
							student.setId(Integer.parseInt(obj[3].toString()));
							consolidateMarksCard.setStudent(student);
						}
						if(obj[4]!=null && !obj[4].toString().isEmpty() && StringUtils.isNumeric(obj[4].toString())){
							classes=new Classes();
							classes.setId(Integer.parseInt(obj[4].toString()));
							consolidateMarksCard.setClasses(classes);
						}
						if(obj[5]!=null && !obj[5].toString().isEmpty() && StringUtils.isNumeric(obj[5].toString())){
							subject=new Subject();
							subject.setId(Integer.parseInt(obj[5].toString()));
							if(obj[61]!=null)
								subject.setIsTheoryPractical(obj[61].toString());
							if(obj[62]!=null)
								subject.setSubjectTypeId(obj[62].toString());
							consolidateMarksCard.setSubject(subject);
						}
						if(obj[12]!=null && !obj[12].toString().isEmpty() && StringUtils.isNumeric(obj[12].toString())){
							course=new Course();
							course.setId(Integer.parseInt(obj[12].toString()));
							consolidateMarksCard.setCourse(course);
						}
						if(obj[6]!=null)
							consolidateMarksCard.setSubjectCode(obj[6].toString());
						if(obj[7]!=null)
							consolidateMarksCard.setSubjectName(obj[7].toString());
						if(obj[8]!=null)
							consolidateMarksCard.setTheoryTotalSubInternalMark(obj[8].toString());
						if(obj[9]!=null)
							consolidateMarksCard.setTheoryTotalAttendanceMark(obj[9].toString());
						if(obj[10]!=null)
							consolidateMarksCard.setPracticalTotalSubInternalMark(obj[10].toString());
						if(obj[11]!=null)
							consolidateMarksCard.setPracticalTotalAttendanceMark(obj[11].toString());
						if(obj[13]!=null && !obj[13].toString().isEmpty() && CommonUtil.isValidDecimal(obj[13].toString()))
							consolidateMarksCard.setTheoryeseMinimumMark(new BigDecimal(obj[13].toString()));
						if(obj[14]!=null && !obj[14].toString().isEmpty() && CommonUtil.isValidDecimal(obj[14].toString()))
							consolidateMarksCard.setTheoryeseMaximumMark(new BigDecimal(obj[14].toString()));
						if(obj[15]!=null && !obj[15].toString().isEmpty() && CommonUtil.isValidDecimal(obj[15].toString()))
							consolidateMarksCard.setPracticaleseMinimumMark(new BigDecimal(obj[15].toString()));
						if(obj[16]!=null && !obj[16].toString().isEmpty() && CommonUtil.isValidDecimal(obj[16].toString()))
							consolidateMarksCard.setPracticaleseMaximumMark(new BigDecimal(obj[16].toString()));
						if(obj[17]!=null)
							consolidateMarksCard.setStudentTheoryMark(obj[17].toString());
						if(obj[18]!=null)
							consolidateMarksCard.setStudentPracticalMark(obj[18].toString());
						if(obj[19]!=null)
							consolidateMarksCard.setFirstName(obj[19].toString());
						if(obj[20]!=null)
							consolidateMarksCard.setMiddleName(obj[20].toString());
						if(obj[21]!=null)
							consolidateMarksCard.setLastName(obj[21].toString());
						if(obj[22]!=null)
							consolidateMarksCard.setSection(obj[22].toString());

						if(obj[23]!=null && (obj[23].toString().equalsIgnoreCase("1") || obj[23].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setIsInitialise(true);
						else
							consolidateMarksCard.setIsInitialise(false);

						if(obj[25]!=null && !obj[25].toString().isEmpty() && StringUtils.isNumeric(obj[25].toString()))
							consolidateMarksCard.setSubjectOrder(Integer.parseInt(obj[25].toString()));
						if(obj[26]!=null && !obj[26].toString().isEmpty() && CommonUtil.isValidDecimal(obj[26].toString()))
							consolidateMarksCard.setFinalPracticalInternalMaximumMark(new BigDecimal(obj[26].toString()));
						if(obj[27]!=null && !obj[27].toString().isEmpty() && CommonUtil.isValidDecimal(obj[27].toString()))
							consolidateMarksCard.setFinalTheoryInternalMaximumMark(new BigDecimal(obj[27].toString()));
						if(obj[28]!=null && !obj[28].toString().isEmpty() && StringUtils.isNumeric(obj[28].toString()))
							consolidateMarksCard.setPracticalCredit(Integer.parseInt(obj[28].toString()));
						if(obj[29]!=null && !obj[29].toString().isEmpty() && StringUtils.isNumeric(obj[29].toString()))
							consolidateMarksCard.setTheoryCredit(Integer.parseInt(obj[29].toString()));
						if(obj[32]!=null && !obj[32].toString().isEmpty() && StringUtils.isNumeric(obj[32].toString()))
							consolidateMarksCard.setTermNumber(Integer.parseInt(obj[32].toString()));
						if(obj[30]!=null)
							consolidateMarksCard.setRegNo(obj[30].toString());
						if(obj[31]!=null)
							consolidateMarksCard.setSubType(obj[31].toString());
						if(obj[33]!=null && !obj[33].toString().isEmpty() && CommonUtil.isValidDecimal(obj[33].toString()))
							consolidateMarksCard.setTheoryMax(new BigDecimal(obj[33].toString()));
						if(obj[34]!=null && !obj[34].toString().isEmpty() && CommonUtil.isValidDecimal(obj[34].toString()))
							consolidateMarksCard.setPracticalMax(new BigDecimal(obj[34].toString()));

						if(obj[35]!=null && (obj[35].toString().equalsIgnoreCase("1") || obj[35].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowMaxMarks(true);
						else
							consolidateMarksCard.setDontShowMaxMarks(false);

						if(obj[36]!=null && (obj[36].toString().equalsIgnoreCase("1") || obj[36].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowMinMarks(true);
						else
							consolidateMarksCard.setDontShowMinMarks(false);

						if(obj[37]!=null && (obj[37].toString().equalsIgnoreCase("1") || obj[37].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setShowOnlyGrade(true);
						else
							consolidateMarksCard.setShowOnlyGrade(false);

						if(obj[39]!=null && (obj[39].toString().equalsIgnoreCase("1") || obj[39].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowSubType(true);
						else
							consolidateMarksCard.setDontShowSubType(false);

						if(obj[38]!=null)
							consolidateMarksCard.setName(obj[38].toString());
						if(obj[40]!=null && !obj[40].toString().isEmpty() && StringUtils.isNumeric(obj[40].toString()))
							consolidateMarksCard.setAppliedYear(Integer.parseInt(obj[40].toString()));
						if(obj[24]!=null && !obj[24].toString().isEmpty() && StringUtils.isNumeric(obj[24].toString()))
							consolidateMarksCard.setSectionId(Integer.parseInt(obj[24].toString()));
						if(obj[41]!=null && !obj[41].toString().isEmpty() && StringUtils.isNumeric(obj[41].toString()))
							consolidateMarksCard.setAcademicYear(Integer.parseInt(obj[41].toString()));
						if(obj[42]!=null && !obj[42].toString().isEmpty() && StringUtils.isNumeric(obj[42].toString()))
							consolidateMarksCard.setSelectedCourseId(Integer.parseInt(obj[42].toString()));

						if(obj[43]!=null && !obj[43].toString().isEmpty() && CommonUtil.isValidDecimal(obj[43].toString()))
							consolidateMarksCard.setFinalTheoryInternalMinimumMark(new BigDecimal(obj[43].toString()));
						if(obj[44]!=null && !obj[44].toString().isEmpty() && CommonUtil.isValidDecimal(obj[44].toString()))
							consolidateMarksCard.setFinalPracticalInternalMinimumMark(new BigDecimal(obj[44].toString()));

						if(obj[45]!=null && !obj[45].toString().isEmpty() && CommonUtil.isValidDecimal(obj[45].toString()))
							consolidateMarksCard.setTheoryMin(new BigDecimal(obj[45].toString()));
						if(obj[46]!=null && !obj[46].toString().isEmpty() && CommonUtil.isValidDecimal(obj[46].toString()))
							consolidateMarksCard.setPracticalMin(new BigDecimal(obj[46].toString()));	

						if(obj[47]!=null && (obj[47].toString().equalsIgnoreCase("1") || obj[47].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontConsiderFailureTotalResult(true);
						else
							consolidateMarksCard.setDontConsiderFailureTotalResult(false);
						if(obj[62]!=null){
							consolidateMarksCard.setSubjectType(String.valueOf(obj[62]));
						}

						//					if(obj[48]!=null && (obj[48].toString().equalsIgnoreCase("1") || obj[48].toString().equalsIgnoreCase("true")))
						//						consolidateMarksCard.setIsTheoryAppeared(true);
						//					else
						//						consolidateMarksCard.setIsTheoryAppeared(false);
						//
						//					if(obj[49]!=null && (obj[49].toString().equalsIgnoreCase("1") || obj[49].toString().equalsIgnoreCase("true")))
						//						consolidateMarksCard.setIsPracticalAppeared(true);
						//					else
						//						consolidateMarksCard.setIsPracticalAppeared(false);

						if(appearedList.contains(consolidateMarksCard.getStudent().getId()+"_"+consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_T")){
							consolidateMarksCard.setIsTheoryAppeared(true);
						}else{
							consolidateMarksCard.setIsTheoryAppeared(false);
						}
						if(appearedList.contains(consolidateMarksCard.getStudent().getId()+"_"+consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_P")){
							consolidateMarksCard.setIsPracticalAppeared(true);
						}else{
							consolidateMarksCard.setIsPracticalAppeared(false);
						}
						if(obj[50]!=null && (obj[50].toString().equalsIgnoreCase("1") || obj[50].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontAddInTotal(true);
						else
							consolidateMarksCard.setDontAddInTotal(false);

						if(certificateMap.containsKey(consolidateMarksCard.getStudent().getId())){
							Map<Integer,Map<Integer,Boolean>> termMap=certificateMap.get(consolidateMarksCard.getStudent().getId());
							if(termMap.containsKey(consolidateMarksCard.getTermNumber())){
								Map<Integer,Boolean> subMap=termMap.get(consolidateMarksCard.getTermNumber());
								if(subMap.containsKey(consolidateMarksCard.getSubject().getId())){
									isCertificateCourse=true;
									isOptional=subMap.get(consolidateMarksCard.getSubject().getId());
								}else if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
									isCertificateCourse=true;
									isOptional=true;
								}
							}else if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
								isCertificateCourse=true;
								isOptional=true;
							}
						}else{
							if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
								isCertificateCourse=true;
								isOptional=true;
							}
						}

						consolidateMarksCard.setIsCertificateCourse(isCertificateCourse);
						consolidateMarksCard.setCreatedBy(consolidatedMarksCardForm.getUserId());
						consolidateMarksCard.setCreatedDate(new Date());
						// if it is only internal subject we should explicitily hit the data base
						if(obj[52]!=null && (obj[52].toString().equalsIgnoreCase("1") || obj[52].toString().equalsIgnoreCase("true"))){
							checkInternalMarksForSubject(consolidateMarksCard,isMaxRecord);
						}
						boolean isImprovement=false;
						if((consolidateMarksCard.getIsTheoryAppeared()!=null && consolidateMarksCard.getIsTheoryAppeared()) ||  (consolidateMarksCard.getIsPracticalAppeared()!=null && consolidateMarksCard.getIsPracticalAppeared()))
							if(obj[53]!=null && (obj[53].toString().equalsIgnoreCase("1") || obj[53].toString().equalsIgnoreCase("true"))){
								isImprovement=true;
							}
						
						/***
						 * The below if block is added by Arun Sudhakaran
						 * The argument 'forConsolidatedProcess' is passed for clarification
						 * The same method is accessed from two different classes for totally three times
						 * So for each call list that is passed as first argument will be different because queries used are different
						 * Which will lead to array index out of bound exception, because the list will have Object[] with different number of indexes
						 * So when this method is called from ConsolidatedMarksCardHandler keep the last boolean argument as true and false the other time
						 */
						
						if(forConsolidatedProcess) {
						
							if(obj[57] != null && !obj[57].toString().isEmpty()) {
								consolidateMarksCard.setSubjectStreamId(Integer.parseInt(obj[57].toString()));
							}
							
							if(obj[58] != null && !obj[58].toString().isEmpty()) {
								consolidateMarksCard.setConsolidatedSubjectSectionId(Integer.parseInt(obj[58].toString()));
							}
						}						
						
						ExamDefinition examRef = new ExamDefinition();
						if(isMaxRecord){
							if(semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								ConsolidateMarksCard bo=semMap.get(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType());
								ConsolidateMaxTo to=CheckMaxMarks(consolidateMarksCard,bo,isImprovement);
								if(consolidateMarksCard.getSubType().equalsIgnoreCase("practical")){
									consolidateMarksCard.setPracticalTotalSubInternalMark(to.getPracticalCia());
									consolidateMarksCard.setPracticalTotalAttendanceMark(to.getPracticalAtt());
									consolidateMarksCard.setStudentPracticalMark(to.getPracticalEse());
									consolidateMarksCard.setIsPracticalAppeared(to.getIsAppearedPractical());
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
								}else if(consolidateMarksCard.getSubType().equalsIgnoreCase("Both")){
									consolidateMarksCard.setStudentTheoryMark(to.getTheoryEse());
									consolidateMarksCard.setStudentPracticalMark(to.getPracticalEse());
									consolidateMarksCard.setPracticalTotalSubInternalMark(to.getPracticalCia());
									consolidateMarksCard.setTheoryTotalSubInternalMark(to.getTheoryCia());
									consolidateMarksCard.setPracticalTotalAttendanceMark(to.getPracticalAtt());
									consolidateMarksCard.setTheoryTotalAttendanceMark(to.getTheoryAtt());
									consolidateMarksCard.setIsPracticalAppeared(to.getIsAppearedPractical());
									consolidateMarksCard.setIsTheoryAppeared(to.getIsAppearedTheory());
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
								}
								else{
									consolidateMarksCard.setTheoryTotalSubInternalMark(to.getTheoryCia());
									consolidateMarksCard.setTheoryTotalAttendanceMark(to.getTheoryAtt());
									consolidateMarksCard.setStudentTheoryMark(to.getTheoryEse());
									consolidateMarksCard.setIsTheoryAppeared(to.getIsAppearedTheory());
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
								}
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
							}else{
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
							}

							if(semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								semMap.remove(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType());
							}

							semMap.put(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType(),consolidateMarksCard);
							mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);

						}else{
							if(!semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
								semMap.put(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType(),consolidateMarksCard);
								mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);
							}else{
								mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);
							}
						}
					}
				}
			}
		}
			//}//for checking student
		}

		return mainMap;
	}
	/**
	 * @param consolidateMarksCard
	 * @param isMaxRecord
	 */
	private void checkInternalMarksForSubject( ConsolidateMarksCard consolidateMarksCard, boolean isMaxRecord) throws Exception {
		String query="from InternalMarkSupplementaryDetails i where i.student.id="+consolidateMarksCard.getStudent().getId()+" and i.subject.id="+consolidateMarksCard.getSubject().getId()+" order by i.exam.year desc,i.exam.month desc ";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<InternalMarkSupplementaryDetails> list=transaction.getDataForQuery(query);
		ConsolidateMarksCard card=null;
		ConsolidateMarksCard latest=null;
		if(list!=null && !list.isEmpty()){
			Iterator<InternalMarkSupplementaryDetails> itr=list.iterator();
			while (itr.hasNext()) {
				InternalMarkSupplementaryDetails bo = itr .next();
				card=new ConsolidateMarksCard();
				if(consolidateMarksCard.getSubType().equalsIgnoreCase("practical")){
					if(bo.getPracticalTotalSubInternalMarks()!=null)
						card.setPracticalTotalSubInternalMark(bo.getPracticalTotalSubInternalMarks());
					if(bo.getPracticalTotalAttendenceMarks()!=null)
						card.setPracticalTotalAttendanceMark(bo.getPracticalTotalAttendenceMarks());
				}else{
					if(bo.getTheoryTotalSubInternalMarks()!=null)
						card.setTheoryTotalSubInternalMark(bo.getTheoryTotalSubInternalMarks());
					if(bo.getTheoryTotalAttendenceMarks()!=null)
						card.setTheoryTotalAttendanceMark(bo.getTheoryTotalAttendenceMarks());
				}

				if(isMaxRecord){
					if(latest==null){
						latest=card;
					}else{
						ConsolidateMaxTo to=checkMaxForInternal(latest,card);
						if(consolidateMarksCard.getSubType().equalsIgnoreCase("practical")){
							if(to.getPracticalCia()!=null)
								latest.setPracticalTotalSubInternalMark(to.getPracticalCia());
							if(to.getPracticalAtt()!=null)
								latest.setPracticalTotalAttendanceMark(to.getPracticalAtt());
						}else{
							if(to.getTheoryCia()!=null)
								latest.setTheoryTotalSubInternalMark(to.getTheoryCia());
							if(to.getTheoryAtt()!=null)
								latest.setTheoryTotalAttendanceMark(to.getTheoryAtt());
						}
					}

				}else{
					if(latest==null)
						latest=card;
				}

			}
			if(latest!=null){
				if(consolidateMarksCard.getSubType().equalsIgnoreCase("practical")){
					if(latest.getPracticalTotalSubInternalMark()!=null)
						consolidateMarksCard.setPracticalTotalSubInternalMark(latest.getPracticalTotalSubInternalMark());
					if(latest.getPracticalTotalAttendanceMark()!=null)
						consolidateMarksCard.setPracticalTotalAttendanceMark(latest.getPracticalTotalAttendanceMark());
				}else{
					if(latest.getTheoryTotalSubInternalMark()!=null)
						consolidateMarksCard.setTheoryTotalSubInternalMark(latest.getTheoryTotalSubInternalMark());
					if(latest.getTheoryTotalAttendanceMark()!=null)
						consolidateMarksCard.setTheoryTotalAttendanceMark(latest.getTheoryTotalAttendanceMark());
				}
			}
		}

	}
	/**
	 * @param latest
	 * @param card
	 * @return
	 */
	private ConsolidateMaxTo checkMaxForInternal( ConsolidateMarksCard bo1, ConsolidateMarksCard bo2) throws Exception {
		ConsolidateMaxTo to=new ConsolidateMaxTo();
		if(bo1!=null && bo2!=null){
			if(bo1.getTheoryTotalSubInternalMark()!=null && !bo1.getTheoryTotalSubInternalMark().isEmpty()){
				if(StringUtils.isAlpha(bo1.getTheoryTotalSubInternalMark().trim())){
					if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalSubInternalMark().trim())){
						to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
					}else{
						to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
					}
				}else{
					if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalSubInternalMark().trim())){
						if(Double.parseDouble(bo1.getTheoryTotalSubInternalMark()) < Double.parseDouble(bo2.getTheoryTotalSubInternalMark()))
							to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
						else
							to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
					}else{
						to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
					}
				}
			}else{
				if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty()){
					to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
				}
			}

			// Practical Int Mark
			if(bo1.getPracticalTotalSubInternalMark()!=null && !bo1.getPracticalTotalSubInternalMark().isEmpty()){
				if(StringUtils.isAlpha(bo1.getPracticalTotalSubInternalMark().trim())){
					if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalSubInternalMark().trim())){
						to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
					}else{
						to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
					}
				}else{
					if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalSubInternalMark().trim())){
						if(Double.parseDouble(bo1.getPracticalTotalSubInternalMark()) < Double.parseDouble(bo2.getPracticalTotalSubInternalMark()))
							to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
						else
							to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
					}else{
						to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
					}
				}
			}else{
				if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty()){
					to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
				}
			}

			if(bo1.getTheoryTotalAttendanceMark()!=null && !bo1.getTheoryTotalAttendanceMark().isEmpty()){
				if(StringUtils.isAlpha(bo1.getTheoryTotalAttendanceMark().trim())){
					if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalAttendanceMark().trim())){
						to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
					}else{
						to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
					}
				}else{
					if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalAttendanceMark().trim())){
						if(Double.parseDouble(bo1.getTheoryTotalAttendanceMark()) < Double.parseDouble(bo2.getTheoryTotalAttendanceMark()))
							to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
						else
							to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
					}else{
						to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
					}
				}
			}else{
				if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty()){
					to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
				}
			}
			// Practical Att Mark
			if(bo1.getPracticalTotalAttendanceMark()!=null && !bo1.getPracticalTotalAttendanceMark().isEmpty()){
				if(StringUtils.isAlpha(bo1.getPracticalTotalAttendanceMark().trim())){
					if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalAttendanceMark().trim())){
						to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
					}else{
						to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
					}
				}else{
					if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalAttendanceMark().trim())){
						if(Double.parseDouble(bo1.getPracticalTotalAttendanceMark()) < Double.parseDouble(bo2.getPracticalTotalAttendanceMark()))
							to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
						else
							to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
					}else{
						to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
					}
				}
			}else{
				if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty()){
					to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
				}
			}
		}
		return to;
	}
	/**
	 * @param consolidateMarksCard
	 * @param bo
	 * @return
	 */
	private ConsolidateMaxTo CheckMaxMarks( ConsolidateMarksCard bo1, ConsolidateMarksCard bo2,boolean isImprovement) throws Exception {
		ConsolidateMaxTo to=new ConsolidateMaxTo();
		if(bo1!=null && bo2!=null){
			//Is Appeared Theory
			if((bo1.getIsTheoryAppeared()!=null && bo1.getIsTheoryAppeared()) || (bo2.getIsTheoryAppeared()!=null && bo2.getIsTheoryAppeared())){
				to.setIsAppearedTheory(true);
			}else{
				to.setIsAppearedTheory(false);
			}
			//Is Appeared Practical
			if((bo1.getIsPracticalAppeared()!=null && bo1.getIsPracticalAppeared()) || (bo2.getIsPracticalAppeared()!=null && bo2.getIsPracticalAppeared())){
				to.setIsAppearedPractical(true);
			}else{
				to.setIsAppearedPractical(false);
			}

			//if(isImprovement){// If it is improvement then we have to get max between the two records
				// Theory Int Marks
				if(bo1.getTheoryTotalSubInternalMark()!=null && !bo1.getTheoryTotalSubInternalMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getTheoryTotalSubInternalMark().trim())){
						if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalSubInternalMark().trim())){
							to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
						}else{
							to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
						}
					}else{
						if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalSubInternalMark().trim())){
							if(Double.parseDouble(bo1.getTheoryTotalSubInternalMark()) < Double.parseDouble(bo2.getTheoryTotalSubInternalMark()))
								to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
							else
								to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
						}else{
							to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
						}
					}
				}else{
					if(bo2.getTheoryTotalSubInternalMark()!=null && !bo2.getTheoryTotalSubInternalMark().isEmpty()){
						to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
					}
				}

				// Practical Int Mark
				if(bo1.getPracticalTotalSubInternalMark()!=null && !bo1.getPracticalTotalSubInternalMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getPracticalTotalSubInternalMark().trim())){
						if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalSubInternalMark().trim())){
							to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
						}else{
							to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
						}
					}else{
						if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalSubInternalMark().trim())){
							if(Double.parseDouble(bo1.getPracticalTotalSubInternalMark()) < Double.parseDouble(bo2.getPracticalTotalSubInternalMark()))
								to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
							else
								to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
						}else{
							to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
						}
					}
				}else{
					if(bo2.getPracticalTotalSubInternalMark()!=null && !bo2.getPracticalTotalSubInternalMark().isEmpty()){
						to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
					}
				}

				// Theory Reg Mark
				if(bo1.getStudentTheoryMark()!=null && !bo1.getStudentTheoryMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getStudentTheoryMark().trim())){
						if(bo2.getStudentTheoryMark()!=null && !bo2.getStudentTheoryMark().isEmpty() && !StringUtils.isAlpha(bo2.getStudentTheoryMark().trim())){
							to.setExamId(String.valueOf(bo2.getExam().getId()));
							to.setTheoryEse(bo2.getStudentTheoryMark());
							to.setPassingMonth(bo2.getMonth());
							to.setPassingYear(bo2.getYear());
						}else{
							to.setExamId(String.valueOf(bo1.getExam().getId()));
							to.setTheoryEse(bo1.getStudentTheoryMark());
							to.setPassingMonth(bo1.getMonth());
							to.setPassingYear(bo1.getYear());
						}
					}else{
						if(bo2.getStudentTheoryMark()!=null && !bo2.getStudentTheoryMark().isEmpty() && !StringUtils.isAlpha(bo2.getStudentTheoryMark().trim())){
							if(Double.parseDouble(bo1.getStudentTheoryMark()) < Double.parseDouble(bo2.getStudentTheoryMark())){
								to.setExamId(String.valueOf(bo2.getExam().getId()));
								to.setTheoryEse(bo2.getStudentTheoryMark());
								to.setPassingMonth(bo2.getMonth());
								to.setPassingYear(bo2.getYear());
							}	
							else{
								to.setExamId(String.valueOf(bo1.getExam().getId()));
								to.setTheoryEse(bo1.getStudentTheoryMark());
								to.setPassingMonth(bo1.getMonth());
								to.setPassingYear(bo1.getYear());
							}
						}else{
							to.setExamId(String.valueOf(bo1.getExam().getId()));
							to.setTheoryEse(bo1.getStudentTheoryMark());
							to.setPassingMonth(bo1.getMonth());
							to.setPassingYear(bo1.getYear());
						}
					}
				}else{
					if(bo2.getStudentTheoryMark()!=null && !bo2.getStudentTheoryMark().isEmpty()){
						to.setExamId(String.valueOf(bo2.getExam().getId()));
						to.setTheoryEse(bo2.getStudentTheoryMark());
						to.setPassingMonth(bo2.getMonth());
						to.setPassingYear(bo2.getYear());
					}
				}


				// practical Reg Mark
				if(bo1.getStudentPracticalMark()!=null && !bo1.getStudentPracticalMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getStudentPracticalMark().trim())){
						if(bo2.getStudentPracticalMark()!=null && !bo2.getStudentPracticalMark().isEmpty() && !StringUtils.isAlpha(bo2.getStudentPracticalMark().trim())){
							to.setExamId(String.valueOf(bo2.getExam().getId()));
							to.setPracticalEse(bo2.getStudentPracticalMark());
							to.setPassingMonth(bo2.getMonth());
							to.setPassingYear(bo2.getYear());
						}else{
							to.setExamId(String.valueOf(bo1.getExam().getId()));
							to.setPracticalEse(bo1.getStudentPracticalMark());
							to.setPassingMonth(bo1.getMonth());
							to.setPassingYear(bo1.getYear());
						}
					}else{
						if(bo2.getStudentPracticalMark()!=null && !bo2.getStudentPracticalMark().isEmpty() && !StringUtils.isAlpha(bo2.getStudentPracticalMark().trim())){
							if(Double.parseDouble(bo1.getStudentPracticalMark()) < Double.parseDouble(bo2.getStudentPracticalMark())){
								to.setExamId(String.valueOf(bo2.getExam().getId()));
								to.setPracticalEse(bo2.getStudentPracticalMark());
								to.setPassingMonth(bo2.getMonth());
								to.setPassingYear(bo2.getYear());
							}else{
								to.setExamId(String.valueOf(bo1.getExam().getId()));
								to.setPracticalEse(bo1.getStudentPracticalMark());
								to.setPassingMonth(bo1.getMonth());
								to.setPassingYear(bo1.getYear());
							}
						}else{
							to.setExamId(String.valueOf(bo1.getExam().getId()));
							to.setPracticalEse(bo1.getStudentPracticalMark());
							to.setPassingMonth(bo1.getMonth());
							to.setPassingYear(bo1.getYear());
						}
					}
				}else{
					if(bo2.getStudentPracticalMark()!=null && !bo2.getStudentPracticalMark().isEmpty()){
						to.setExamId(String.valueOf(bo2.getExam().getId()));
						to.setPracticalEse(bo2.getStudentPracticalMark());
						to.setPassingMonth(bo2.getMonth());
						to.setPassingYear(bo2.getYear());
					}
				}

				// Theory Att Mark
				if(bo1.getTheoryTotalAttendanceMark()!=null && !bo1.getTheoryTotalAttendanceMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getTheoryTotalAttendanceMark().trim())){
						if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalAttendanceMark().trim())){
							to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
						}else{
							to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
						}
					}else{
						if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getTheoryTotalAttendanceMark().trim())){
							if(Double.parseDouble(bo1.getTheoryTotalAttendanceMark()) < Double.parseDouble(bo2.getTheoryTotalAttendanceMark()))
								to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
							else
								to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
						}else{
							to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
						}
					}
				}else{
					if(bo2.getTheoryTotalAttendanceMark()!=null && !bo2.getTheoryTotalAttendanceMark().isEmpty()){
						to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
					}
				}
				// Practical Att Mark
				if(bo1.getPracticalTotalAttendanceMark()!=null && !bo1.getPracticalTotalAttendanceMark().isEmpty()){
					if(StringUtils.isAlpha(bo1.getPracticalTotalAttendanceMark().trim())){
						if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalAttendanceMark().trim())){
							to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
						}else{
							to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
						}
					}else{
						if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty() && !StringUtils.isAlpha(bo2.getPracticalTotalAttendanceMark().trim())){
							if(Double.parseDouble(bo1.getPracticalTotalAttendanceMark()) < Double.parseDouble(bo2.getPracticalTotalAttendanceMark()))
								to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
							else
								to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
						}else{
							to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
						}
					}
				}else{
					if(bo2.getPracticalTotalAttendanceMark()!=null && !bo2.getPracticalTotalAttendanceMark().isEmpty()){
						to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
					}
				}
			//}
		/*else{
				
				// If it is supplementary then we have to get latest availablity data
				// Theory Int Marks
				if(bo2.getTheoryTotalSubInternalMark()==null || bo2.getTheoryTotalSubInternalMark().isEmpty()){
					to.setTheoryCia(bo1.getTheoryTotalSubInternalMark());
				}else
					to.setTheoryCia(bo2.getTheoryTotalSubInternalMark());
				
				
				// Practical Int Mark
				if(bo2.getPracticalTotalSubInternalMark()==null || bo2.getPracticalTotalSubInternalMark().isEmpty()){
					to.setPracticalCia(bo1.getPracticalTotalSubInternalMark());
				}else{
					to.setPracticalCia(bo2.getPracticalTotalSubInternalMark());
				}
				
				// Theory Reg Mark
			
				if(bo1.getStudentTheoryMark()==null || bo1.getStudentTheoryMark().isEmpty()){
					to.setTheoryEse(bo2.getStudentTheoryMark());
				}else{
					to.setExamId(String.valueOf(bo1.getExam().getId()));
					to.setTheoryEse(bo1.getStudentTheoryMark());
				}
				
				// practical Reg Mark
				if(bo1.getStudentPracticalMark()==null || bo1.getStudentPracticalMark().isEmpty()){
					to.setPracticalEse(bo2.getStudentPracticalMark());
				}else{
					to.setExamId(String.valueOf(bo1.getExam().getId()));
					to.setPracticalEse(bo1.getStudentPracticalMark());
				}
				
				// Theory Att Mark
				if(bo2.getTheoryTotalAttendanceMark()==null || bo2.getTheoryTotalAttendanceMark().isEmpty()){
					to.setTheoryAtt(bo1.getTheoryTotalAttendanceMark());
				}else{
					to.setTheoryAtt(bo2.getTheoryTotalAttendanceMark());
				}
				// Practical Att Mark
				if(bo2.getPracticalTotalAttendanceMark()==null || bo2.getPracticalTotalAttendanceMark().isEmpty()){
					to.setPracticalAtt(bo1.getPracticalTotalAttendanceMark());
				}else{
					to.setPracticalAtt(bo2.getPracticalTotalAttendanceMark());
				}
			}*/
		}
		return to;
	}
	/**
	 * @param consolidateMarksCard
	 */
	private void calculateMarksPercentage(ConsolidateMarksCard consolidateMarksCard,boolean isCertificateCourse,boolean isOptional,int programTypeId) throws Exception {
		IDownloadHallTicketTransaction transaction= DownloadHallTicketTransactionImpl.getInstance();

		double theoryCia=0;
		double theoryAtt=0;
		double theoryEse=0;
		double theoryTotal=0;
		double practicalCia=0;
		double practicalAtt=0;
		double practicalEse=0;
		double practicalTotal=0;
		double subFinalTotalMarks=0;
		boolean fail=false;
		if(consolidateMarksCard.getSubType().equalsIgnoreCase("Both")){
			// practical subject
			if(consolidateMarksCard.getPracticalTotalAttendanceMark()!=null && !consolidateMarksCard.getPracticalTotalAttendanceMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getPracticalTotalAttendanceMark()))
					practicalAtt=Double.parseDouble(consolidateMarksCard.getPracticalTotalAttendanceMark());
				else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			if(consolidateMarksCard.getPracticalTotalSubInternalMark()!=null && !consolidateMarksCard.getPracticalTotalSubInternalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getPracticalTotalSubInternalMark())){
					practicalCia=Double.parseDouble(consolidateMarksCard.getPracticalTotalSubInternalMark());
					practicalTotal = practicalTotal+practicalCia;

				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}

			if(consolidateMarksCard.getStudentPracticalMark()!=null && !consolidateMarksCard.getStudentPracticalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getStudentPracticalMark())){
					practicalEse=Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());

				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			practicalTotal=practicalAtt+practicalCia+practicalEse;

			// theory part
			if(consolidateMarksCard.getTheoryTotalAttendanceMark()!=null && !consolidateMarksCard.getTheoryTotalAttendanceMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getTheoryTotalAttendanceMark())){
					theoryAtt=Double.parseDouble(consolidateMarksCard.getTheoryTotalAttendanceMark());
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getTheoryTotalSubInternalMark())){
					theoryCia=Double.parseDouble(consolidateMarksCard.getTheoryTotalSubInternalMark());

				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}

			if(consolidateMarksCard.getStudentTheoryMark()!=null && !consolidateMarksCard.getStudentTheoryMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getStudentTheoryMark())){
					theoryEse=Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());

				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			theoryTotal=theoryAtt+theoryCia+theoryEse;
			double maxMark =0;
			double totalMax=0;
			if(consolidateMarksCard.getTheoryMax()!=null){
				maxMark=consolidateMarksCard.getTheoryMax().doubleValue();
				totalMax = maxMark;
				if(maxMark>0){
					double percentage=(theoryTotal/maxMark)*100;
					consolidateMarksCard.setTheoryPercentage(CommonUtil.Round(percentage,2));
				}
			}

			double minMark=0;
			if(consolidateMarksCard.getSubjectFinalMin()!=null){
				minMark=consolidateMarksCard.getSubjectFinalMin().doubleValue();
			}
			subFinalTotalMarks = theoryTotal +practicalTotal;	
			if(subFinalTotalMarks<minMark)
				fail=true;
			maxMark =0;
			if(consolidateMarksCard.getPracticalMax()!=null){
				maxMark=consolidateMarksCard.getPracticalMax().doubleValue();
				totalMax = totalMax + maxMark;
				if(maxMark>0){
					double percentage=(practicalTotal/maxMark)*100;
					consolidateMarksCard.setPracticalPercentage(CommonUtil.Round(percentage,2));
					percentage = (subFinalTotalMarks/totalMax)*100;
					String query="select ifnull((SELECT EXAM_sub_coursewise_grade_defn.grade " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+
					percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+")," +
					" (ifnull( (SELECT EXAM_grade_class_definition_frombatch.grade " +
					" FROM EXAM_grade_class_definition_frombatch " +
					" EXAM_grade_class_definition_frombatch where "+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1)," +
					" (SELECT  EXAM_grade_class_definition.grade " +
					" FROM EXAM_grade_class_definition EXAM_grade_class_definition where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as pragra," +
					"  ifnull( (SELECT EXAM_sub_coursewise_grade_defn.grade_point " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where " +
					+percentage+" between start_prcntg_grade and end_prcntg_grade and " +
					" EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+"), " +
					"  (ifnull( (SELECT     EXAM_grade_class_definition_frombatch.grade_point " +
					"  FROM EXAM_grade_class_definition_frombatch EXAM_grade_class_definition_frombatch where " +
					+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and " +
					" EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1), " +
					"(SELECT EXAM_grade_class_definition.grade_point " +
					"  FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
					" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and " +
					" EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as pragrap";



					/*String query="select if((SELECT EXAM_sub_coursewise_grade_defn.grade " +
				" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn " +
				" where "+percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") is not null, " +
				" (SELECT EXAM_sub_coursewise_grade_defn.grade FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+percentage+
				" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") ," +
				" (SELECT EXAM_grade_class_definition.grade FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
				" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id ="+consolidateMarksCard.getCourse().getId()+"  limit 1))," +
						"if((SELECT EXAM_sub_coursewise_grade_defn.grade_point " +
				" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn " +
				" where "+percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") is not null, " +
				" (SELECT EXAM_sub_coursewise_grade_defn.grade_point FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+percentage+
				" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") ," +
				" (SELECT EXAM_grade_class_definition.grade_point FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
				" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id ="+consolidateMarksCard.getCourse().getId()+"  limit 1))";
					 */

					List<Object[]> rList=transaction.getStudentHallTicket(query);
					if(rList!=null && !rList.isEmpty()){
						Iterator<Object[]> rItr=rList.iterator();
						while (rItr.hasNext()) {
							Object[] robj = rItr.next();
							if(robj[0]!=null){
								consolidateMarksCard.setGrade(robj[0].toString());
							}
							if(robj[1]!=null){
								consolidateMarksCard.setGradePoint(new BigDecimal(robj[1].toString()));
							}

						}
					}
				}
			}
			consolidateMarksCard.setTheoryObtain(theoryTotal);
			consolidateMarksCard.setPracticalObtain(practicalTotal);
			BigDecimal gradepint =new BigDecimal(0);
		
			if(fail){
				consolidateMarksCard.setPassOrFail("Fail");
				if(consolidateMarksCard.getSection()!=null && !consolidateMarksCard.getSection().equalsIgnoreCase("Add On Course"))
					consolidateMarksCard.setGrade("F");
			}else{
				consolidateMarksCard.setPassOrFail("Pass");
			}
		}
		else if(consolidateMarksCard.getSubType().equalsIgnoreCase("Practical")){
			if(consolidateMarksCard.getPracticalTotalAttendanceMark()!=null && !consolidateMarksCard.getPracticalTotalAttendanceMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getPracticalTotalAttendanceMark()))
					practicalAtt=Double.parseDouble(consolidateMarksCard.getPracticalTotalAttendanceMark());
				else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			if(consolidateMarksCard.getPracticalTotalSubInternalMark()!=null && !consolidateMarksCard.getPracticalTotalSubInternalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getPracticalTotalSubInternalMark())){
					practicalCia=Double.parseDouble(consolidateMarksCard.getPracticalTotalSubInternalMark());
					if(consolidateMarksCard.getFinalPracticalInternalMinimumMark()!=null){
						double minMark=consolidateMarksCard.getFinalPracticalInternalMinimumMark().doubleValue();
						if((practicalAtt+practicalCia)<minMark)
							if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
								fail=true;
					}
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}

			if(consolidateMarksCard.getStudentPracticalMark()!=null && !consolidateMarksCard.getStudentPracticalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getStudentPracticalMark())){
					practicalEse=Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());
					if(consolidateMarksCard.getPracticaleseMinimumMark()!=null){
						double minMark=consolidateMarksCard.getPracticaleseMinimumMark().doubleValue();
						if(practicalEse<minMark)
							if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
								fail=true;
					}
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			practicalTotal=practicalAtt+practicalCia+practicalEse;
			double minMark=0;
		/*	if(consolidateMarksCard.getPracticalMin()!=null){
				minMark=consolidateMarksCard.getPracticalMin().doubleValue();
			}*/

			if(consolidateMarksCard.getSubjectFinalMin()!=null){
				minMark=consolidateMarksCard.getSubjectFinalMin().doubleValue();
			}
			if(consolidateMarksCard.getPracticalMax()!=null)
				if(isCertificateCourse)
					if(isOptional){
						if(consolidateMarksCard.getPracticalMax().doubleValue()==100)
							minMark=60;
						else if(consolidateMarksCard.getPracticalMax().doubleValue()==50)
							minMark=30;
					}
			if(practicalTotal<minMark)
				fail=true;
			if(consolidateMarksCard.getPracticalMax()!=null){
				double maxMark=consolidateMarksCard.getPracticalMax().doubleValue();
				if(maxMark>0){
					double percentage=(practicalTotal/maxMark)*100;
					consolidateMarksCard.setPracticalPercentage(CommonUtil.Round(percentage,2));

					String query="select ifnull((SELECT EXAM_sub_coursewise_grade_defn.grade " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+
					percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+")," +
					" (ifnull( (SELECT EXAM_grade_class_definition_frombatch.grade " +
					" FROM EXAM_grade_class_definition_frombatch " +
					" EXAM_grade_class_definition_frombatch where "+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1)," +
					" (SELECT  EXAM_grade_class_definition.grade " +
					" FROM EXAM_grade_class_definition EXAM_grade_class_definition where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as pragra," +
					"  ifnull( (SELECT EXAM_sub_coursewise_grade_defn.grade_point " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where " +
					+percentage+" between start_prcntg_grade and end_prcntg_grade and " +
					" EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+"), " +
					"  (ifnull( (SELECT     EXAM_grade_class_definition_frombatch.grade_point " +
					"  FROM EXAM_grade_class_definition_frombatch EXAM_grade_class_definition_frombatch where " +
					+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and " +
					" EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1), " +
					"(SELECT EXAM_grade_class_definition.grade_point " +
					"  FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
					" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and " +
					" EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as pragrap";



					/*String query="select if((SELECT EXAM_sub_coursewise_grade_defn.grade " +
				" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn " +
				" where "+percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") is not null, " +
				" (SELECT EXAM_sub_coursewise_grade_defn.grade FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+percentage+
				" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") ," +
				" (SELECT EXAM_grade_class_definition.grade FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
				" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id ="+consolidateMarksCard.getCourse().getId()+"  limit 1))," +
						"if((SELECT EXAM_sub_coursewise_grade_defn.grade_point " +
				" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn " +
				" where "+percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") is not null, " +
				" (SELECT EXAM_sub_coursewise_grade_defn.grade_point FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+percentage+
				" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id ="+consolidateMarksCard.getCourse().getId()+"  and subject_id="+consolidateMarksCard.getSubject().getId()+") ," +
				" (SELECT EXAM_grade_class_definition.grade_point FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
				" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id ="+consolidateMarksCard.getCourse().getId()+"  limit 1))";
					 */

					List<Object[]> rList=transaction.getStudentHallTicket(query);
					if(rList!=null && !rList.isEmpty()){
						Iterator<Object[]> rItr=rList.iterator();
						while (rItr.hasNext()) {
							Object[] robj = rItr.next();
							if(robj[0]!=null){
								consolidateMarksCard.setGrade(robj[0].toString());
							}
							if(robj[1]!=null){
								consolidateMarksCard.setGradePoint(new BigDecimal(robj[1].toString()));
							}

						}
					}
				}
			}
			consolidateMarksCard.setPracticalObtain(practicalTotal);
			BigDecimal gradepint =new BigDecimal(0);
			if(programTypeId==1){
				gradepint=new BigDecimal(practicalTotal/10);
			   consolidateMarksCard.setGradePoint(gradepint);
			}
			if(fail){
				consolidateMarksCard.setPassOrFail("Fail");
				if(consolidateMarksCard.getSection()!=null && !consolidateMarksCard.getSection().equalsIgnoreCase("Add On Course"))
					consolidateMarksCard.setGrade("F");
			}else{
				consolidateMarksCard.setPassOrFail("Pass");
			}
		}else{
			if(consolidateMarksCard.getTheoryTotalAttendanceMark()!=null && !consolidateMarksCard.getTheoryTotalAttendanceMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getTheoryTotalAttendanceMark())){
					theoryAtt=Double.parseDouble(consolidateMarksCard.getTheoryTotalAttendanceMark());
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getTheoryTotalSubInternalMark())){
					theoryCia=Double.parseDouble(consolidateMarksCard.getTheoryTotalSubInternalMark());
					if(consolidateMarksCard.getFinalTheoryInternalMinimumMark()!=null){
						double minMark=consolidateMarksCard.getFinalTheoryInternalMinimumMark().doubleValue();
						if((theoryAtt+theoryCia)<minMark)
							if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
								fail=true;
					}
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}

			if(consolidateMarksCard.getStudentTheoryMark()!=null && !consolidateMarksCard.getStudentTheoryMark().isEmpty()){
				if(CommonUtil.isValidDecimal(consolidateMarksCard.getStudentTheoryMark())){
					theoryEse=Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
					if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
						double minMark=consolidateMarksCard.getTheoryeseMinimumMark().doubleValue();
						if(theoryEse<minMark)
							if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
								fail=true;
					}
				}else{
					if(!isCertificateCourse && !consolidateMarksCard.getDontConsiderFailureTotalResult())
						fail=true;
				}
			}
			theoryTotal=theoryAtt+theoryCia+theoryEse;
			consolidateMarksCard.setTheoryObtain(theoryTotal);
			
			double minMark=0;
		/*	if(consolidateMarksCard.getTheoryMin()!=null){
				minMark=consolidateMarksCard.getTheoryMin().doubleValue();
			}*/
			if(consolidateMarksCard.getSubjectFinalMin()!=null){
				minMark=consolidateMarksCard.getSubjectFinalMin().doubleValue();
			}
			if(consolidateMarksCard.getTheoryMax()!=null)
				if(isCertificateCourse)
					if(isOptional){
						if(consolidateMarksCard.getTheoryMax().doubleValue()==100)
							minMark=60;
						else if(consolidateMarksCard.getTheoryMax().doubleValue()==50)
							minMark=30;
					}
			if(theoryTotal<minMark)
				fail=true;

			if(consolidateMarksCard.getTheoryMax()!=null){
				double maxMark=consolidateMarksCard.getTheoryMax().doubleValue();
				if(maxMark>0){
					double percentage=(theoryTotal/maxMark)*100;
					consolidateMarksCard.setTheoryPercentage(CommonUtil.Round(percentage,2));

					String query="select ifnull((SELECT EXAM_sub_coursewise_grade_defn.grade " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where "+
					percentage+" between start_prcntg_grade and end_prcntg_grade and EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+")," +
					" (ifnull( (SELECT EXAM_grade_class_definition_frombatch.grade " +
					" FROM EXAM_grade_class_definition_frombatch " +
					" EXAM_grade_class_definition_frombatch where "+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1)," +
					" (SELECT  EXAM_grade_class_definition.grade " +
					" FROM EXAM_grade_class_definition EXAM_grade_class_definition where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as thegra," +
					"  ifnull( (SELECT EXAM_sub_coursewise_grade_defn.grade_point " +
					" FROM EXAM_sub_coursewise_grade_defn EXAM_sub_coursewise_grade_defn where " +
					+percentage+" between start_prcntg_grade and end_prcntg_grade and " +
					" EXAM_sub_coursewise_grade_defn.course_id = "+consolidateMarksCard.getCourse().getId()+" and subject_id="+consolidateMarksCard.getSubject().getId()+"), " +
					"  (ifnull( (SELECT     EXAM_grade_class_definition_frombatch.grade_point " +
					"  FROM EXAM_grade_class_definition_frombatch EXAM_grade_class_definition_frombatch where " +
					+percentage+" between EXAM_grade_class_definition_frombatch.start_percentage and " +
					" EXAM_grade_class_definition_frombatch.end_percentage and " +
					" EXAM_grade_class_definition_frombatch.course_id = "+consolidateMarksCard.getCourse().getId()+" and from_batch <= "+consolidateMarksCard.getAppliedYear()+" limit 1), " +
					"(SELECT EXAM_grade_class_definition.grade_point " +
					"  FROM EXAM_grade_class_definition EXAM_grade_class_definition " +
					" where "+percentage+" between start_percentage and EXAM_grade_class_definition.end_percentage and " +
					" EXAM_grade_class_definition.course_id = "+consolidateMarksCard.getCourse().getId()+" limit 1)))) as thegrap";


					List<Object[]> rList=transaction.getStudentHallTicket(query);
					if(rList!=null && !rList.isEmpty()){
						Iterator<Object[]> rItr=rList.iterator();
						while (rItr.hasNext()) {
							Object[] robj = rItr.next();
							if(robj[0]!=null){
								consolidateMarksCard.setGrade(robj[0].toString());
							}
							if(robj[1]!=null){
								consolidateMarksCard.setGradePoint(new BigDecimal(robj[1].toString()));
							}

						}
					}
				}
			}
			BigDecimal gradepint =new BigDecimal(0);
			if(programTypeId==1){
				gradepint=new BigDecimal(theoryTotal/10);
			   consolidateMarksCard.setGradePoint(gradepint);
			}
			if(fail){
				consolidateMarksCard.setPassOrFail("Fail");
				if(consolidateMarksCard.getSection()!=null && !consolidateMarksCard.getSection().equalsIgnoreCase("Add On Course"))
					consolidateMarksCard.setGrade("F");
			}else{
				consolidateMarksCard.setPassOrFail("Pass");
			}
		}




	}

	public Map<Integer,Map<String, ConsolidateMarksCard>> getBoListForInputSupplementary(List<Object[]> list, String userId,Map<Integer,Map<Integer,Map<Integer,Boolean>>> certificateMap,List<String> appearedList,String courseId) throws Exception {
		Map<Integer,Map<String, ConsolidateMarksCard>> mainMap=new HashMap<Integer, Map<String,ConsolidateMarksCard>>();
		boolean isMaxRecord=true;
		
		if(list!=null && !list.isEmpty()){
			Map<String, ConsolidateMarksCard> semMap;
			ConsolidateMarksCard consolidateMarksCard;
			ExamDefinition exam;
			Student student;
			Classes classes;
			Subject subject;
			Course course;
			int programTypeId=0;
			Iterator<Object[]> itr=list.iterator();
			while (itr.hasNext()) {
				boolean isCertificateCourse=false;
				boolean isOptional=false;

				Object[] obj=itr.next();
				boolean doNotAddMarks=false;
				boolean showOnlyCredits=false;
				if(obj[50] != null && obj[50].toString().equalsIgnoreCase("true"))
					doNotAddMarks=true;
				if(obj[60]!=null && obj[60].toString().equalsIgnoreCase("true")){
					showOnlyCredits=true;
					doNotAddMarks=false;
				}
				if(!doNotAddMarks){
				if(obj[22]!=null){
					//if(Integer.parseInt(obj[3].toString())==16370){
					if(obj[3]!=null && obj[32]!=null && obj[5]!=null /*&& Integer.parseInt(obj[3].toString())==4226  && Integer.parseInt(obj[5].toString())==702*/){
						//					System.out.println(obj[3].toString()+"_"+obj[5].toString());
						//					System.out.println(obj[3].toString());
						if(mainMap.containsKey(Integer.parseInt(obj[3].toString()))){
							semMap=mainMap.get(Integer.parseInt(obj[3].toString()));
							if(semMap.containsKey(Integer.parseInt(obj[32].toString())+"_"+Integer.parseInt(obj[5].toString())+"_"+obj[31].toString())){
								if(!isMaxRecord)
									continue;
							}
							semMap=mainMap.remove(Integer.parseInt(obj[3].toString()));
						}else
							semMap=new HashMap<String, ConsolidateMarksCard>();

						consolidateMarksCard =new ConsolidateMarksCard();
						if(obj[59]!=null)
							programTypeId = Integer.parseInt(obj[59].toString());
						if(obj[1]!=null)
							consolidateMarksCard.setMonth(obj[1].toString());
						if(obj[2]!=null)
							consolidateMarksCard.setYear(obj[2].toString());
						// only for display purpose
						if(showOnlyCredits)
							consolidateMarksCard.setShowOnlyCredits(true);
						else
							consolidateMarksCard.setShowOnlyCredits(false);
						if(obj[22]!=null)
							consolidateMarksCard.setSection(obj[22].toString());
						if(obj[55]!=null)
							consolidateMarksCard.setIsTheoryPractical(obj[55].toString());
						if(obj[56]!=null)
							consolidateMarksCard.setTheoryObtain(Double.parseDouble(obj[56].toString()));
						if(obj[57]!=null)
							consolidateMarksCard.setPracticalObtain(Double.parseDouble(obj[56].toString()));
						if(obj[54]!=null)
							consolidateMarksCard.setSubjectFinalMin(new BigDecimal(obj[54].toString()));
						if(obj[0]!=null){
							exam=new ExamDefinition();
							exam.setId(Integer.parseInt(obj[0].toString()));
							consolidateMarksCard.setExam(exam);
						}
						if(obj[3]!=null && !obj[3].toString().isEmpty() && StringUtils.isNumeric(obj[3].toString())){
							student=new Student();
							student.setId(Integer.parseInt(obj[3].toString()));
							consolidateMarksCard.setStudent(student);
						}
						if(obj[4]!=null && !obj[4].toString().isEmpty() && StringUtils.isNumeric(obj[4].toString())){
							classes=new Classes();
							classes.setId(Integer.parseInt(obj[4].toString()));
							consolidateMarksCard.setClasses(classes);
						}
						if(obj[5]!=null && !obj[5].toString().isEmpty() && StringUtils.isNumeric(obj[5].toString())){
							subject=new Subject();
							subject.setId(Integer.parseInt(obj[5].toString()));
							if(obj[58]!=null)
								subject.setSubjectTypeId(obj[58].toString());
								
							consolidateMarksCard.setSubject(subject);
						}
						if(obj[12]!=null && !obj[12].toString().isEmpty() && StringUtils.isNumeric(obj[12].toString())){
							course=new Course();
							course.setId(Integer.parseInt(obj[12].toString()));
							consolidateMarksCard.setCourse(course);
						}
						if(obj[6]!=null)
							consolidateMarksCard.setSubjectCode(obj[6].toString());
						if(obj[7]!=null)
							consolidateMarksCard.setSubjectName(obj[7].toString());
						if(obj[8]!=null)
							consolidateMarksCard.setTheoryTotalSubInternalMark(obj[8].toString());
						if(obj[9]!=null)
							consolidateMarksCard.setTheoryTotalAttendanceMark(obj[9].toString());
						if(obj[10]!=null)
							consolidateMarksCard.setPracticalTotalSubInternalMark(obj[10].toString());
						if(obj[11]!=null)
							consolidateMarksCard.setPracticalTotalAttendanceMark(obj[11].toString());
						if(obj[13]!=null && !obj[13].toString().isEmpty() && CommonUtil.isValidDecimal(obj[13].toString()))
							consolidateMarksCard.setTheoryeseMinimumMark(new BigDecimal(obj[13].toString()));
						if(obj[14]!=null && !obj[14].toString().isEmpty() && CommonUtil.isValidDecimal(obj[14].toString()))
							consolidateMarksCard.setTheoryeseMaximumMark(new BigDecimal(obj[14].toString()));
						if(obj[15]!=null && !obj[15].toString().isEmpty() && CommonUtil.isValidDecimal(obj[15].toString()))
							consolidateMarksCard.setPracticaleseMinimumMark(new BigDecimal(obj[15].toString()));
						if(obj[16]!=null && !obj[16].toString().isEmpty() && CommonUtil.isValidDecimal(obj[16].toString()))
							consolidateMarksCard.setPracticaleseMaximumMark(new BigDecimal(obj[16].toString()));
						if(obj[17]!=null)
							consolidateMarksCard.setStudentTheoryMark(obj[17].toString());
						if(obj[18]!=null)
							consolidateMarksCard.setStudentPracticalMark(obj[18].toString());
						if(obj[19]!=null)
							consolidateMarksCard.setFirstName(obj[19].toString());
						if(obj[20]!=null)
							consolidateMarksCard.setMiddleName(obj[20].toString());
						if(obj[21]!=null)
							consolidateMarksCard.setLastName(obj[21].toString());
						if(obj[22]!=null)
							consolidateMarksCard.setSection(obj[22].toString());

						if(obj[23]!=null && (obj[23].toString().equalsIgnoreCase("1") || obj[23].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setIsInitialise(true);
						else
							consolidateMarksCard.setIsInitialise(false);

						if(obj[25]!=null && !obj[25].toString().isEmpty() && StringUtils.isNumeric(obj[25].toString()))
							consolidateMarksCard.setSubjectOrder(Integer.parseInt(obj[25].toString()));
						if(obj[26]!=null && !obj[26].toString().isEmpty() && CommonUtil.isValidDecimal(obj[26].toString()))
							consolidateMarksCard.setFinalPracticalInternalMaximumMark(new BigDecimal(obj[26].toString()));
						if(obj[27]!=null && !obj[27].toString().isEmpty() && CommonUtil.isValidDecimal(obj[27].toString()))
							consolidateMarksCard.setFinalTheoryInternalMaximumMark(new BigDecimal(obj[27].toString()));
						if(obj[28]!=null && !obj[28].toString().isEmpty() && StringUtils.isNumeric(obj[28].toString()))
							consolidateMarksCard.setPracticalCredit(Integer.parseInt(obj[28].toString()));
						if(obj[29]!=null && !obj[29].toString().isEmpty() && StringUtils.isNumeric(obj[29].toString()))
							consolidateMarksCard.setTheoryCredit(Integer.parseInt(obj[29].toString()));
						if(obj[32]!=null && !obj[32].toString().isEmpty() && StringUtils.isNumeric(obj[32].toString()))
							consolidateMarksCard.setTermNumber(Integer.parseInt(obj[32].toString()));
						if(obj[30]!=null)
							consolidateMarksCard.setRegNo(obj[30].toString());
						if(obj[31]!=null)
							consolidateMarksCard.setSubType(obj[31].toString());
						if(obj[33]!=null && !obj[33].toString().isEmpty() && CommonUtil.isValidDecimal(obj[33].toString()))
							consolidateMarksCard.setTheoryMax(new BigDecimal(obj[33].toString()));
						if(obj[34]!=null && !obj[34].toString().isEmpty() && CommonUtil.isValidDecimal(obj[34].toString()))
							consolidateMarksCard.setPracticalMax(new BigDecimal(obj[34].toString()));

						if(obj[35]!=null && (obj[35].toString().equalsIgnoreCase("1") || obj[35].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowMaxMarks(true);
						else
							consolidateMarksCard.setDontShowMaxMarks(false);

						if(obj[36]!=null && (obj[36].toString().equalsIgnoreCase("1") || obj[36].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowMinMarks(true);
						else
							consolidateMarksCard.setDontShowMinMarks(false);

						if(obj[37]!=null && (obj[37].toString().equalsIgnoreCase("1") || obj[37].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setShowOnlyGrade(true);
						else
							consolidateMarksCard.setShowOnlyGrade(false);

						if(obj[39]!=null && (obj[39].toString().equalsIgnoreCase("1") || obj[39].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontShowSubType(true);
						else
							consolidateMarksCard.setDontShowSubType(false);

						if(obj[38]!=null)
							consolidateMarksCard.setName(obj[38].toString());
						if(obj[40]!=null && !obj[40].toString().isEmpty() && StringUtils.isNumeric(obj[40].toString()))
							consolidateMarksCard.setAppliedYear(Integer.parseInt(obj[40].toString()));
						if(obj[24]!=null && !obj[24].toString().isEmpty() && StringUtils.isNumeric(obj[24].toString()))
							consolidateMarksCard.setSectionId(Integer.parseInt(obj[24].toString()));
						if(obj[41]!=null && !obj[41].toString().isEmpty() && StringUtils.isNumeric(obj[41].toString()))
							consolidateMarksCard.setAcademicYear(Integer.parseInt(obj[41].toString()));
						if(obj[42]!=null && !obj[42].toString().isEmpty() && StringUtils.isNumeric(obj[42].toString()))
							consolidateMarksCard.setSelectedCourseId(Integer.parseInt(obj[42].toString()));

						if(obj[43]!=null && !obj[43].toString().isEmpty() && CommonUtil.isValidDecimal(obj[43].toString()))
							consolidateMarksCard.setFinalTheoryInternalMinimumMark(new BigDecimal(obj[43].toString()));
						if(obj[44]!=null && !obj[44].toString().isEmpty() && CommonUtil.isValidDecimal(obj[44].toString()))
							consolidateMarksCard.setFinalPracticalInternalMinimumMark(new BigDecimal(obj[44].toString()));

						if(obj[45]!=null && !obj[45].toString().isEmpty() && CommonUtil.isValidDecimal(obj[45].toString()))
							consolidateMarksCard.setTheoryMin(new BigDecimal(obj[45].toString()));
						if(obj[46]!=null && !obj[46].toString().isEmpty() && CommonUtil.isValidDecimal(obj[46].toString()))
							consolidateMarksCard.setPracticalMin(new BigDecimal(obj[46].toString()));	

						if(obj[47]!=null && (obj[47].toString().equalsIgnoreCase("1") || obj[47].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontConsiderFailureTotalResult(true);
						else
							consolidateMarksCard.setDontConsiderFailureTotalResult(false);

						//					if(obj[48]!=null && (obj[48].toString().equalsIgnoreCase("1") || obj[48].toString().equalsIgnoreCase("true")))
						//						consolidateMarksCard.setIsTheoryAppeared(true);
						//					else
						//						consolidateMarksCard.setIsTheoryAppeared(false);
						//
						//					if(obj[49]!=null && (obj[49].toString().equalsIgnoreCase("1") || obj[49].toString().equalsIgnoreCase("true")))
						//						consolidateMarksCard.setIsPracticalAppeared(true);
						//					else
						//						consolidateMarksCard.setIsPracticalAppeared(false);

						if(appearedList.contains(consolidateMarksCard.getStudent().getId()+"_"+consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_T")){
							consolidateMarksCard.setIsTheoryAppeared(true);
						}else{
							consolidateMarksCard.setIsTheoryAppeared(false);
						}
						if(appearedList.contains(consolidateMarksCard.getStudent().getId()+"_"+consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_P")){
							consolidateMarksCard.setIsPracticalAppeared(true);
						}else{
							consolidateMarksCard.setIsPracticalAppeared(false);
						}
						if(obj[50]!=null && (obj[50].toString().equalsIgnoreCase("1") || obj[50].toString().equalsIgnoreCase("true")))
							consolidateMarksCard.setDontAddInTotal(true);
						else
							consolidateMarksCard.setDontAddInTotal(false);

						if(certificateMap.containsKey(consolidateMarksCard.getStudent().getId())){
							Map<Integer,Map<Integer,Boolean>> termMap=certificateMap.get(consolidateMarksCard.getStudent().getId());
							if(termMap.containsKey(consolidateMarksCard.getTermNumber())){
								Map<Integer,Boolean> subMap=termMap.get(consolidateMarksCard.getTermNumber());
								if(subMap.containsKey(consolidateMarksCard.getSubject().getId())){
									isCertificateCourse=true;
									isOptional=subMap.get(consolidateMarksCard.getSubject().getId());
								}else if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
									isCertificateCourse=true;
									isOptional=true;
								}
							}else if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
								isCertificateCourse=true;
								isOptional=true;
							}
						}else{
							if(obj[51]!=null && (obj[51].toString().equalsIgnoreCase("1") || obj[51].toString().equalsIgnoreCase("true"))){
								isCertificateCourse=true;
								isOptional=true;
							}
						}

						consolidateMarksCard.setIsCertificateCourse(isCertificateCourse);
						consolidateMarksCard.setCreatedBy(userId);
						consolidateMarksCard.setCreatedDate(new Date());
						// if it is only internal subject we should explicitily hit the data base
						if(obj[52]!=null && (obj[52].toString().equalsIgnoreCase("1") || obj[52].toString().equalsIgnoreCase("true"))){
							checkInternalMarksForSubject(consolidateMarksCard,isMaxRecord);
						}
						boolean isImprovement=false;
						if((consolidateMarksCard.getIsTheoryAppeared()!=null && consolidateMarksCard.getIsTheoryAppeared()) ||  (consolidateMarksCard.getIsPracticalAppeared()!=null && consolidateMarksCard.getIsPracticalAppeared()))
							if(obj[53]!=null && (obj[53].toString().equalsIgnoreCase("1") || obj[53].toString().equalsIgnoreCase("true"))){
								isImprovement=true;
							}
						ExamDefinition examRef = new ExamDefinition();

						if(isMaxRecord){
							if(semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								ConsolidateMarksCard bo=semMap.get(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType());
								ConsolidateMaxTo to=CheckMaxMarks(consolidateMarksCard,bo,isImprovement);
								if(consolidateMarksCard.getSubType().equalsIgnoreCase("practical")){
									consolidateMarksCard.setPracticalTotalSubInternalMark(to.getPracticalCia());
									consolidateMarksCard.setPracticalTotalAttendanceMark(to.getPracticalAtt());
									consolidateMarksCard.setStudentPracticalMark(to.getPracticalEse());
									consolidateMarksCard.setIsPracticalAppeared(to.getIsAppearedPractical());
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
									if(to.getPassingMonth()!=null)
										consolidateMarksCard.setMonth(to.getPassingMonth());
									if(to.getPassingYear()!=null)
										consolidateMarksCard.setYear(to.getPassingYear());
									
								}else if(consolidateMarksCard.getSubType().equalsIgnoreCase("Both")){
									consolidateMarksCard.setPracticalTotalSubInternalMark(to.getPracticalCia());
									consolidateMarksCard.setPracticalTotalAttendanceMark(to.getPracticalAtt());
									consolidateMarksCard.setStudentPracticalMark(to.getPracticalEse());
									consolidateMarksCard.setIsPracticalAppeared(to.getIsAppearedPractical());
									consolidateMarksCard.setStudentTheoryMark(to.getTheoryEse());
									consolidateMarksCard.setIsTheoryAppeared(to.getIsAppearedTheory());
									consolidateMarksCard.setTheoryTotalSubInternalMark(to.getTheoryCia());
									consolidateMarksCard.setTheoryTotalAttendanceMark(to.getTheoryAtt());
									
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
									if(to.getPassingMonth()!=null)
										consolidateMarksCard.setMonth(to.getPassingMonth());
									if(to.getPassingYear()!=null)
										consolidateMarksCard.setYear(to.getPassingYear());
								}
								else{
									consolidateMarksCard.setTheoryTotalSubInternalMark(to.getTheoryCia());
									consolidateMarksCard.setTheoryTotalAttendanceMark(to.getTheoryAtt());
									consolidateMarksCard.setStudentTheoryMark(to.getTheoryEse());
									consolidateMarksCard.setIsTheoryAppeared(to.getIsAppearedTheory());
									if(to.getExamId()!=null){
										examRef.setId(Integer.parseInt(to.getExamId()));
										consolidateMarksCard.setExam(examRef);
									}
									if(to.getPassingMonth()!=null)
										consolidateMarksCard.setMonth(to.getPassingMonth());
									if(to.getPassingYear()!=null)
										consolidateMarksCard.setYear(to.getPassingYear());
								}
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
							}else{
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
							}

							if(semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								semMap.remove(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType());
							}
							semMap.put(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType(),consolidateMarksCard);
							mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);
						}else{
							if(!semMap.containsKey(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType())){
								calculateMarksPercentage(consolidateMarksCard,isCertificateCourse,isOptional,programTypeId);
								semMap.put(consolidateMarksCard.getTermNumber()+"_"+consolidateMarksCard.getSubject().getId()+"_"+consolidateMarksCard.getSubType(),consolidateMarksCard);
								mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);
							}else{
								mainMap.put(consolidateMarksCard.getStudent().getId(),semMap);
							}
						}
					}
					//}// student checking
				}
			}
			}
		}


		return mainMap;
	}

	public String getConsolidateQueryForMarksCard(int schemeNo, int examId) throws Exception {
		String query=" select EXAM_definition.id,CAST(EXAM_definition.month AS UNSIGNED INTEGER) AS exam_month, EXAM_definition.year as exam_year, "+
		" student.id as student_id, classes.id as classId, subject.id as subject_id, "+
		" subject.code as subCode, subject.name as subName, "+
		" ifnull(EXAM_internal_mark_supplementary_details.theory_total_mark, EXAM_student_overall_internal_mark_details.theory_total_mark) "+
		" as theory_total_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.theory_total_attendance_mark, EXAM_student_overall_internal_mark_details.theory_total_attendance_mark) "+
		" as theory_total_attendance_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.practical_total_mark, EXAM_student_overall_internal_mark_details.practical_total_mark) "+
		" as practical_total_mark, "+
		" ifnull(EXAM_internal_mark_supplementary_details.practical_total_attendance_mark, EXAM_student_overall_internal_mark_details.practical_total_attendance_mark) "+
		" as practical_total_attendance_mark, "+
		" classes.course_id as course_id, "+
		" EXAM_subject_rule_settings.theory_ese_minimum_mark,     "+
		" EXAM_subject_rule_settings.theory_ese_maximum_mark,     "+
		" EXAM_subject_rule_settings.practical_ese_minimum_mark,     "+
		" EXAM_subject_rule_settings.practical_ese_maximum_mark, "+
		" EXAM_student_final_mark_details.student_theory_marks, "+
		" EXAM_student_final_mark_details.student_practical_marks, "+
		" personal_data.first_name,     "+
		" personal_data.middle_name,     "+
		" personal_data.last_name,  "+
		" EXAM_subject_sections.name as section,  "+
		" EXAM_subject_sections.is_initialise,     "+
		" EXAM_subject_sections.id as section_id,     "+
		" EXAM_sub_definition_coursewise.subject_order,     "+
		" EXAM_subject_rule_settings.final_practical_internal_maximum_mark,     "+
		" EXAM_subject_rule_settings.final_theory_internal_maximum_mark,  "+
		" EXAM_sub_definition_coursewise.practical_credit, "+
		" EXAM_sub_definition_coursewise.theory_credit, "+
		" student.register_no, "+
		" if(subject.is_theory_practical='T','Theory',if(subject.is_theory_practical='P','Practical',if(subject.is_theory_practical='B','Both','Theory'))) as subType, "+
		" classes.term_number as term_number, "+
		" (if(EXAM_subject_rule_settings.final_theory_internal_maximum_mark > 0,EXAM_subject_rule_settings.final_theory_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.theory_ese_maximum_mark >0,EXAM_subject_rule_settings.theory_ese_maximum_mark,0)) as theoryMax,     "+
		" (if(EXAM_subject_rule_settings.final_practical_internal_maximum_mark >0,EXAM_subject_rule_settings.final_practical_internal_maximum_mark,0)+if(EXAM_subject_rule_settings.practical_ese_maximum_mark >0,EXAM_subject_rule_settings.practical_ese_maximum_mark,0)) as practicalMax, "+
		" EXAM_sub_definition_coursewise.dont_show_max_marks,     "+
		" EXAM_sub_definition_coursewise.dont_show_min_marks,  "+
		" EXAM_sub_definition_coursewise.show_only_grade,  "+
		" course_scheme.name as schemeName,     "+
		" EXAM_sub_definition_coursewise.dont_show_sub_type,      "+
		" adm_appln.applied_year,  "+
		" EXAM_definition.academic_year, "+
		" adm_appln.selected_course_id,EXAM_subject_rule_settings.final_theory_internal_minimum_mark, " +
		" EXAM_subject_rule_settings.final_practical_internal_minimum_mark ,EXAM_subject_rule_settings.theory_ese_theory_final_minimum_mark,EXAM_subject_rule_settings.practical_ese_theory_final_minimum_mark"+
		" ,EXAM_sub_definition_coursewise.dont_consider_failure_total_result,EXAM_supplementary_improvement_application.is_appeared_theory, EXAM_supplementary_improvement_application.is_appeared_practical,EXAM_sub_definition_coursewise.dont_add_tot_mark_cls_decln,subject.is_certificate_course,if(theory_ese_maximum_mark is null and theory_ese_entered_max_mark is null and practical_ese_maximum_mark is null and practical_ese_entered_max_mark is null, 1, 0) as only_internal,EXAM_supplementary_improvement_application.is_improvement, " +

		"(if(EXAM_student_overall_internal_mark_details.theory_total_mark>0,EXAM_student_overall_internal_mark_details.theory_total_mark,0)+if(EXAM_student_overall_internal_mark_details.theory_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.theory_total_attendance_mark,0)+(if(student_theory_marks is null,0,student_theory_marks))) as theoryObtain, "+ 
		"   (if(EXAM_student_overall_internal_mark_details.practical_total_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_mark,0)+if(EXAM_student_overall_internal_mark_details.practical_total_attendance_mark > 0,EXAM_student_overall_internal_mark_details.practical_total_attendance_mark,0)+if(student_practical_marks is null,0,student_practical_marks )) as practicalObtain, "+
		" EXAM_subject_rule_settings.subject_final_minimum as subFinalMin, "+
		" subject.consolidated_subject_stream_id as consolidated_subject_stream_id, "+
		" EXAM_subject_sections.consolidated_subject_section_id as consolidated_subject_section_id, "+
		" program.program_type_id ,"+
		" EXAM_sub_definition_coursewise.show_only_credits, "+
		" subject.is_theory_practical as is_theory_practical, "+
		" subject.subject_type_id as is_subject_type_id"+

		" from student "+
		" inner join adm_appln ON student.adm_appln_id = adm_appln.id "+
		" inner join personal_data ON adm_appln.personal_data_id = personal_data.id "+
		" inner join EXAM_student_overall_internal_mark_details on EXAM_student_overall_internal_mark_details.student_id = student.id "+
		" inner join classes ON EXAM_student_overall_internal_mark_details.class_id = classes.id "+
		" inner join class_schemewise on class_schemewise.class_id = classes.id "+
		" inner join curriculum_scheme_duration ON class_schemewise.curriculum_scheme_duration_id = curriculum_scheme_duration.id "+
		" inner join curriculum_scheme ON curriculum_scheme_duration.curriculum_scheme_id = curriculum_scheme.id "+
		" inner join course_scheme ON curriculum_scheme.course_scheme_id = course_scheme.id "+
		" inner join course ON classes.course_id = course.id "+
		" inner join subject ON EXAM_student_overall_internal_mark_details.subject_id = subject.id "+
		" inner join program on course.program_id = program.id "+
		" left join EXAM_student_final_mark_details on EXAM_student_final_mark_details.class_id = classes.id "+
		" and EXAM_student_final_mark_details.student_id = student.id "+
		" and EXAM_student_final_mark_details.subject_id = subject.id and (EXAM_student_final_mark_details.student_theory_marks is not null or "+
		" EXAM_student_final_mark_details.student_practical_marks is not null ) "+
		" inner join EXAM_subject_rule_settings on EXAM_subject_rule_settings.course_id = course.id "+
		" and EXAM_subject_rule_settings.subject_id = subject.id "+
		" and EXAM_subject_rule_settings.scheme_no=classes.term_number "+
		" and EXAM_subject_rule_settings.academic_year = curriculum_scheme_duration.academic_year "+
		" left join EXAM_sub_definition_coursewise on EXAM_sub_definition_coursewise.course_id = course.id "+
		" and EXAM_sub_definition_coursewise.subject_id = subject.id "+
		" and EXAM_sub_definition_coursewise.scheme_no = classes.term_number "+
		" and EXAM_sub_definition_coursewise.academic_year=curriculum_scheme_duration.academic_year "+
		" left join EXAM_subject_sections on EXAM_sub_definition_coursewise.subject_section_id = EXAM_subject_sections.id "+
		" inner join EXAM_definition ON ifnull(EXAM_student_final_mark_details.exam_id,EXAM_student_overall_internal_mark_details.exam_id) = EXAM_definition.id "+
		" left join EXAM_supplementary_improvement_application on EXAM_supplementary_improvement_application.scheme_no = classes.term_number "+
		" and EXAM_supplementary_improvement_application.student_id = student.id "+
		" and EXAM_supplementary_improvement_application.subject_id = subject.id "+
		" left join EXAM_internal_mark_supplementary_details on EXAM_internal_mark_supplementary_details.class_id = classes.id "+
		" and EXAM_internal_mark_supplementary_details.student_id = student.id "+
		" and EXAM_internal_mark_supplementary_details.subject_id = subject.id "+
		" left join subject_type ON subject_type.id = subject.subject_type_id "+
		" where student.id in (:ids)" +
		" and classes.term_number ="+schemeNo+
		" and EXAM_definition.id<= "+examId+
		" group by student_id,EXAM_definition.id,subject.id "+
		" order by term_number, student_id, subject_id,exam_year asc,exam_month asc ";
		return query;
	}
	
	public Map<String, ConsolidatedMarksCardProgrammePart> getMapForProgramPartSectionInMarksCard(Map<Integer,Map<String, ConsolidateMarksCard>> consolidatedMarksCardMap, ConsolidatedMarksCardForm consolidatedMarksCardForm, List<ExamGradeDefinitionBO> gradeList) throws ParseException
	{
		Map<String, ConsolidatedMarksCardProgrammePart> programPartMap = new HashMap<String, ConsolidatedMarksCardProgrammePart>();		
		
		Set<Integer> keyOuter = consolidatedMarksCardMap.keySet();
		for(int key1 : keyOuter) {
			Map<String, ConsolidateMarksCard> innerMap = consolidatedMarksCardMap.get(key1);
			Set<String> keyInner = innerMap.keySet();
			
			for(String key2 : keyInner) {
				ConsolidateMarksCard consolidateMarksCard = innerMap.get(key2);
				int studentId = consolidateMarksCard.getStudent().getId();
				int sectionId = 0;	
				int streamId = 0;
				double gradePoint=0;
				if(consolidateMarksCard.getSubjectStreamId() != null)
					streamId = consolidateMarksCard.getSubjectStreamId();
				if(consolidateMarksCard.getConsolidatedSubjectSectionId() != null)
					sectionId = consolidateMarksCard.getConsolidatedSubjectSectionId();
				String key = studentId + "_" + sectionId + "_"  + streamId;						
				
				//if(studentId == 6295/*&& consolidateMarksCard.getConsolidatedSubjectSectionId() == 5*/){
				if(programPartMap.containsKey(key)) {	// if section and stream are same add marks for that section and stream into one BO 
					ConsolidatedMarksCardProgrammePart programmePart = programPartMap.get(key);
					
					if(!consolidateMarksCard.getShowOnlyCredits()){
						double maxMarksObtained = consolidateMarksCard.getTheoryObtain() + consolidateMarksCard.getPracticalObtain();
						programmePart.setMaxMarksObtained(maxMarksObtained + programmePart.getMaxMarksObtained());

						int credit = 0;
						if(consolidateMarksCard.getSubType() != null) {
							if("Theory".equals(consolidateMarksCard.getSubType()) && consolidateMarksCard.getTheoryMax() != null) {
								BigDecimal maxAwardedMarks = consolidateMarksCard.getTheoryMax();
								if( programmePart.getMaxMarksAwarded()!=null)
								programmePart.setMaxMarksAwarded(maxAwardedMarks.add( programmePart.getMaxMarksAwarded() ));
								credit = consolidateMarksCard.getTheoryCredit();
								if(consolidateMarksCard.getTheoryMax()!=null){
									double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
							}
							else if("Practical".equals(consolidateMarksCard.getSubType()) && consolidateMarksCard.getPracticalMax()!= null) {
								BigDecimal maxAwardedMarks = consolidateMarksCard.getPracticalMax();
								if( programmePart.getMaxMarksAwarded()!=null)
								programmePart.setMaxMarksAwarded(maxAwardedMarks.add( programmePart.getMaxMarksAwarded() ));
								credit = consolidateMarksCard.getPracticalCredit();
								if(consolidateMarksCard.getPracticalMax()!=null){
									double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
							}
							// for future enhancement if subject is both Theory and Practical
							else
							{
								if(consolidateMarksCard.getTheoryMax() != null && consolidateMarksCard.getPracticalMax() == null)
								{
									BigDecimal maxAwardedMarks = consolidateMarksCard.getTheoryMax();
									programmePart.setMaxMarksAwarded(maxAwardedMarks.add( programmePart.getMaxMarksAwarded() ));
									credit = consolidateMarksCard.getTheoryCredit();
								}
								else if(consolidateMarksCard.getPracticalMax() != null && consolidateMarksCard.getTheoryMax() == null)
								{
									BigDecimal maxAwardedMarks = consolidateMarksCard.getPracticalMax();
									programmePart.setMaxMarksAwarded(maxAwardedMarks.add( programmePart.getMaxMarksAwarded() ));
									credit = consolidateMarksCard.getPracticalCredit();
								}
								else if(consolidateMarksCard.getTheoryMax() != null && consolidateMarksCard.getPracticalMax()!= null)
								{
									BigDecimal maxAwardedTheoryMarks = consolidateMarksCard.getTheoryMax();
									BigDecimal maxAwardedPracticalMarks = consolidateMarksCard.getPracticalMax();
									BigDecimal totalMaxAwarded = maxAwardedPracticalMarks.add(maxAwardedTheoryMarks);
									programmePart.setMaxMarksAwarded(totalMaxAwarded.add( programmePart.getMaxMarksAwarded() ));
									credit = (consolidateMarksCard.getTheoryCredit() + consolidateMarksCard.getPracticalCredit());
									gradePoint = (((consolidateMarksCard.getTheoryObtain()+consolidateMarksCard.getPracticalObtain())/(maxAwardedTheoryMarks.doubleValue()+maxAwardedPracticalMarks.doubleValue()))*100)/10;
								}


							}
						}					

						programmePart.setCredit(credit + programmePart.getCredit());	// credit set

						//Double creditPoint =CommonUtil.round(gradePoint * credit,2);
						Double creditPoint =gradePoint * credit;
						if(programmePart.getCreditPoints() != null) {
							programmePart.setCreditPoints(String.valueOf(creditPoint + Double.parseDouble(programmePart.getCreditPoints())));
						}						
						else {
							programmePart.setCreditPoints(String.valueOf(creditPoint));		// credit point set
						}
						double ccpa = CommonUtil.round(Double.parseDouble(programmePart.getCreditPoints())/programmePart.getCredit(), 2);	// rounded to two decimal places
						//double ccpa = CommonUtil.round(creditPoint/credit, 2);
						programmePart.setCcpa(String.valueOf(CommonUtil.Round(ccpa,2)));	// ccpa set

						Iterator<ExamGradeDefinitionBO> gradeItr = gradeList.iterator();	// getting course wise grade definition
						Double programmePartCCPA = Double.parseDouble(programmePart.getCcpa());
						while(gradeItr.hasNext()) {
							ExamGradeDefinitionBO gradeDef = gradeItr.next();							
							if(Double.compare(programmePartCCPA, gradeDef.getStartPercentage().doubleValue()) >= 0 &&
									Double.compare(programmePartCCPA, gradeDef.getEndPercentage().doubleValue()) <= 0) {
								programmePart.setGrade(gradeDef.getGrade());	// grade set
								break;
							}
						}	
					}
					else{
						if("Theory".equals(consolidateMarksCard.getSubType())){
							if(consolidateMarksCard.getTheoryCredit()>0){
								programmePart.setShowOnlyCredits(true);
								int creditsForDisplay =0;
								if(programmePart.getCreditsForDisplay()!=null)
									 creditsForDisplay= Integer.parseInt(programmePart.getCreditsForDisplay());
									programmePart.setCreditsForDisplay(String.valueOf(consolidateMarksCard.getTheoryCredit()+creditsForDisplay));
								
							}
						}
						else{

							if(consolidateMarksCard.getPracticalCredit()>0){
								programmePart.setShowOnlyCredits(true);
								int creditsForDisplay =0;
								if(programmePart.getCreditsForDisplay()!=null)
									creditsForDisplay= Integer.parseInt(programmePart.getCreditsForDisplay());
									programmePart.setCreditsForDisplay(String.valueOf(consolidateMarksCard.getPracticalCredit()+creditsForDisplay));
								
							}
						}
					}
					
				}
				else {
					ConsolidatedMarksCardProgrammePart programmePart = new ConsolidatedMarksCardProgrammePart();
					
					if(!consolidateMarksCard.getShowOnlyCredits()){
						Student student = new Student();
						student.setId(studentId);
						programmePart.setStudent(student);	// student set
						
						if(sectionId == 0) {
							programmePart.setConsolidatedSubjectSection(null);
						}
						else {
							ConsolidatedSubjectSection consolidatedSubjectSection = new ConsolidatedSubjectSection();
							consolidatedSubjectSection.setId(sectionId);
							programmePart.setConsolidatedSubjectSection(consolidatedSubjectSection);	// subject section set
						}						
						
						if(streamId == 0) {	// if subject is not defined with a stream id [Foreign key]
							programmePart.setSubjectStream(null);
						}
						else {
							ConsolidatedSubjectStream subjectStream = new ConsolidatedSubjectStream();
							subjectStream.setId(streamId);
							programmePart.setSubjectStream(subjectStream);	// subject stream set
						}
											
						programmePart.setMaxMarksObtained(consolidateMarksCard.getTheoryObtain() + consolidateMarksCard.getPracticalObtain());
						
						int credit = 0;
						if(consolidateMarksCard.getSubType() != null) {
							if("Theory".equals(consolidateMarksCard.getSubType()) && consolidateMarksCard.getTheoryMax() != null) {
								programmePart.setMaxMarksAwarded(consolidateMarksCard.getTheoryMax());
								credit = consolidateMarksCard.getTheoryCredit();				
								if(consolidateMarksCard.getTheoryMax()!=null){
									double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
							}
							else if("Practical".equals(consolidateMarksCard.getSubType()) && consolidateMarksCard.getPracticalMax()!= null) {
								programmePart.setMaxMarksAwarded(consolidateMarksCard.getPracticalMax());
								credit = consolidateMarksCard.getPracticalCredit();
								if(consolidateMarksCard.getPracticalMax()!=null){
									double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
							}
							// for future enhancement if subject is both Theory and Practical
							else
							{
								if(consolidateMarksCard.getTheoryMax() != null && consolidateMarksCard.getPracticalMax() == null)
								{
									programmePart.setMaxMarksAwarded(consolidateMarksCard.getTheoryMax());
									credit = consolidateMarksCard.getTheoryCredit();
								}
								else if(consolidateMarksCard.getPracticalMax() != null && consolidateMarksCard.getTheoryMax() == null)
								{
									programmePart.setMaxMarksAwarded(consolidateMarksCard.getPracticaleseMaximumMark());
									credit = consolidateMarksCard.getPracticalCredit();
								}
								else if(consolidateMarksCard.getTheoryMax() != null && consolidateMarksCard.getPracticalMax()!= null)
								{
									programmePart.setMaxMarksAwarded(consolidateMarksCard.getTheoryMax().add( consolidateMarksCard.getPracticalMax() ));
									credit = (consolidateMarksCard.getTheoryCredit() + consolidateMarksCard.getPracticalCredit());
									gradePoint = (((consolidateMarksCard.getTheoryObtain()+consolidateMarksCard.getPracticalObtain())/(consolidateMarksCard.getTheoryMax().doubleValue()+consolidateMarksCard.getPracticalMax().doubleValue()))*100)/10;
								}
								
							}
						}					
						
						programmePart.setCredit(credit);	//	credit set
						
						//if(consolidateMarksCard.getGradePoint() != null) {	// if grade is defined for that subject
						
						    Double creditPoint =CommonUtil.round(gradePoint * credit,2);
						    Double creditPoint1 = gradePoint * credit;
							programmePart.setCreditPoints(String.valueOf(creditPoint));		// credit point set
							
							Double ccpa = creditPoint1/credit;// rounded to two decimal places
							ccpa = CommonUtil.round(ccpa, 2);
							programmePart.setCcpa(String.valueOf(ccpa));
							
							Iterator<ExamGradeDefinitionBO> gradeItr = gradeList.iterator();	// getting course wise grade definition
							Double programmePartCCPA = Double.parseDouble(programmePart.getCcpa());
							while(gradeItr.hasNext()) {
								ExamGradeDefinitionBO gradeDef = gradeItr.next();							
								if(Double.compare(programmePartCCPA, gradeDef.getStartPercentage().doubleValue()) >= 0 &&
								   Double.compare(programmePartCCPA, gradeDef.getEndPercentage().doubleValue()) <= 0) {
									programmePart.setGrade(gradeDef.getGrade());	// grade set
									break;
								}
							}
					}
					else{
						if("Theory".equals(consolidateMarksCard.getSubType())){
							if(consolidateMarksCard.getTheoryCredit()>0){
								programmePart.setShowOnlyCredits(true);
								//int CreditsForDisplay= (Integer) (programmePart.getCreditsForDisplay()==null?0:programmePart.getCreditsForDisplay());
								programmePart.setCreditsForDisplay(String.valueOf(consolidateMarksCard.getTheoryCredit()));
							}
						}
						else{

							if(consolidateMarksCard.getPracticalCredit()>0){
								programmePart.setShowOnlyCredits(true);
								//int CreditsForDisplay= (Integer) (programmePart.getCreditsForDisplay()==null?0:programmePart.getCreditsForDisplay());
								programmePart.setCreditsForDisplay(String.valueOf(consolidateMarksCard.getPracticalCredit()));
							}
						}
					}
																
					
					programmePart.setCreatedBy(consolidatedMarksCardForm.getUserId());
					programmePart.setCreatedDate(new Date());
					
					Course course = new Course();
					course.setId(consolidateMarksCard.getCourse().getId());
					programmePart.setCourse(course);					
					programmePart.setAppliedYear(consolidateMarksCard.getAppliedYear());
					
					programPartMap.put(key, programmePart);
				}
				//}// end braces for checking
				
			}
		}
		
		return programPartMap;
	}

}
