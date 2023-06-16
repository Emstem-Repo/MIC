package com.kp.cms.helpers.exam;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.ExamMarkEvaluationBo;
import com.kp.cms.bo.exam.MarksEntryDetails;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.exam.NewExamMarksEntryHandler;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamMarkEvaluationTo;
import com.kp.cms.to.exam.MarksDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.transactions.exam.IFalseExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.FalseExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.ExamComparator;

public class FalseExamMarksEntryHelper {
	/**
	 * Singleton object of NewExamMarksEntryHelper
	 */
	private static volatile FalseExamMarksEntryHelper newExamMarksEntryHelper = null;
	private static final Log log = LogFactory.getLog(FalseExamMarksEntryHelper.class);
	static DecimalFormat df = new DecimalFormat("#.##");
	
	private FalseExamMarksEntryHelper() {
		
	}
	/**
	 * return singleton object of newExamMarksEntryHelper.
	 * @return
	 */
	public static FalseExamMarksEntryHelper getInstance() {
		if (newExamMarksEntryHelper == null) {
			newExamMarksEntryHelper = new FalseExamMarksEntryHelper();
		}
		return newExamMarksEntryHelper;
	}
	/**
	 * creating query for Checking Marks Entered Through Secured 
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public String getQueryForCheckMarksEnteredThroughSecured(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		String query="from MarksEntryDetails m" +
				" where m.subject.id=" +newExamMarksEntryForm.getSubjectId()+
				" and m.marksEntry.classes.course.id= " +newExamMarksEntryForm.getCourseId()+
				" and m.marksEntry.exam.id="+newExamMarksEntryForm.getExamId();
		if(newExamMarksEntryForm.getEvaluatorType()!=null && !newExamMarksEntryForm.getEvaluatorType().isEmpty()){
			query=query+" and m.marksEntry.evaluatorType="+newExamMarksEntryForm.getEvaluatorType();
		}
		if(newExamMarksEntryForm.getAnswerScriptType()!=null && !newExamMarksEntryForm.getAnswerScriptType().isEmpty()){
			query=query+" and m.marksEntry.answerScript="+newExamMarksEntryForm.getAnswerScriptType();
		}
		if(newExamMarksEntryForm.getSubjectType()!=null && !newExamMarksEntryForm.getSubjectType().isEmpty()){
			if(newExamMarksEntryForm.getSubjectType().equals("1")){
				query=query+" and m.theoryMarks is not null and m.isTheorySecured=1";
			}else if(newExamMarksEntryForm.getSubjectType().equals("0")){
				query=query+" and m.practicalMarks is not null and m.isPracticalSecured=1";
			}else{
				query=query+" and m.theoryMarks is not null and m.practicalMarks is not null and m.isTheorySecured=1 and m.isPracticalSecured=1";
			}
		}
		return query;
	}
	/**
	 * creating query for Already Entered Marks Entered
	 * @param newExamMarksEntryForm
	 * @return
	 */
	public String getQueryForAlreadyEnteredMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String query="from MarksEntryDetails m" +
			" where m.subject.id=" +newExamMarksEntryForm.getSubjectId()+
			" and m.marksEntry.exam.id="+newExamMarksEntryForm.getExamId()+
			" and m.marksEntry.student.id="+newExamMarksEntryForm.getDisplatoList().getStudentId();
		if(newExamMarksEntryForm.getEvaluatorType()!=null && !newExamMarksEntryForm.getEvaluatorType().isEmpty()){
			query=query+" and m.marksEntry.evaluatorType="+newExamMarksEntryForm.getEvaluatorType();
		}
		if(newExamMarksEntryForm.getAnswerScriptType()!=null && !newExamMarksEntryForm.getAnswerScriptType().isEmpty()){
			query=query+" and m.marksEntry.answerScript="+newExamMarksEntryForm.getAnswerScriptType();
		}
		if(newExamMarksEntryForm.getSubjectType()!=null && !newExamMarksEntryForm.getSubjectType().isEmpty()){
			if(newExamMarksEntryForm.getSubjectType().equals("1")){
				query=query+" and m.theoryMarks is not null ";
			}else if(newExamMarksEntryForm.getSubjectType().equals("0")){
				query=query+" and m.practicalMarks is not null ";
			}else{
				query=query+" and m.theoryMarks is not null and m.practicalMarks is not null ";
			}
		}
		return query;
	}
	public String getQueryForAlreadyEnteredEvaluationMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String query="from ExamMarkEvaluationBo bo where bo.falseNo='"+newExamMarksEntryForm.getFalseNo()+"'";
		
		return query;
	}
	/**
	 * creating the Bo List to Required Map 
	 * @param marksList
	 * @return
	 * @throws Exception
	 */
	public MarksDetailsTO convertBoDataToMarksMap(MarksEntryDetails bo) throws Exception {
			MarksEntryDetails marksEntryDetails = bo;
			MarksDetailsTO to=new MarksDetailsTO();
			to.setId(marksEntryDetails.getId());
			to.setTheoryMarks(marksEntryDetails.getTheoryMarks());
			to.setPracticalMarks(marksEntryDetails.getPracticalMarks());
			if(marksEntryDetails.getMarksEntry().getClasses()!=null)
				to.setClassId(marksEntryDetails.getMarksEntry().getClasses().getId());
			to.setMarksId(marksEntryDetails.getMarksEntry().getId());
			if(marksEntryDetails.getIsTheorySecured()!=null && marksEntryDetails.getIsTheorySecured()){
				to.setTheorySecured(true);
			}else{
				to.setTheorySecured(false);
			}
			if(marksEntryDetails.getIsPracticalSecured()!=null && marksEntryDetails.getIsPracticalSecured()){
				to.setPracticalSecured(true);
			}else{
				to.setPracticalSecured(false);
			}
		return to;
	}
	/**
	 * creating query for getting student for Current Class
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public String getQueryForCurrentClassStudents(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String query="select s,s.classSchemewise.classes.id from Student s" +
				" join s.admAppln.applicantSubjectGroups appSub" +
				" join appSub.subjectGroup.subjectGroupSubjectses subGroup" +
				" where s.admAppln.isCancelled=0 and subGroup.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and s.classSchemewise.curriculumSchemeDuration.academicYear=(select e.academicYear" +
				" from ExamDefinitionBO e where e.id="+newExamMarksEntryForm.getExamId()+") " +
				" and subGroup.subject.id=" +newExamMarksEntryForm.getSubjectId()+
				" and s.admAppln.courseBySelectedCourseId.id=" +newExamMarksEntryForm.getCourseId()+
				" and s.id="+newExamMarksEntryForm.getDisplatoList().getStudentId()+
				" and s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+newExamMarksEntryForm.getSchemeNo();
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and s.classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
			query=query+" and s.id in(select rt.studentId from ExamInternalRetestApplicationBO rt where rt.classId="+newExamMarksEntryForm.getSection()+")";
		}
		return query;
	}
	/**
	 * @param currentStudentList
	 * @param existsMarks
	 * @return
	 */
	public Set<StudentMarksTO> convertBotoTOListForCurrentStudents(Set<StudentMarksTO> studentList,List currentStudentList,MarksDetailsTO existsMarks,List<Integer> listOfDetainedStudents,NewExamMarksEntryForm newExamMarksEntryForm, ExamMarkEvaluationTo existEvalMarks) throws Exception {
		Iterator<Object[]> itr=currentStudentList.iterator();
		boolean disable=true;
		boolean isfalsegenerated=false;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if(!listOfDetainedStudents.contains(student.getId())){
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
						to.setRetests(true);
					}else{
						to.setRetests(false);
					}
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						
						String falsenoquery="select f.falseNo,f.id from ExamFalseNumberGen f where " +
						" f.classId.id="+objects[1].toString()+" and f.course.id="+newExamMarksEntryForm.getCourseId()+" and f.examId.id= "+newExamMarksEntryForm.getExamId() +
						" and f.studentId.id="+student.getId()+" and f.subject.id="+newExamMarksEntryForm.getSubjectId();
						
						
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		List falsenoList=txn.getFalseNoForQuery(falsenoquery);
		if(falsenoList!=null && !falsenoList.isEmpty()){
			isfalsegenerated=true;
			Iterator<Object[]> itr2=falsenoList.iterator();
			while(itr2.hasNext()){
				Object[] obj = (Object[]) itr2.next();
			if(obj[0]!=null)
			to.setFalseNo(obj[0].toString());
			if(obj[1]!=null)
			to.setFalseNoId(obj[1].toString());
			}
		}
				/*	if(existsMarks!=null){
						MarksDetailsTO detailTo=existsMarks;
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
					}else{
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
					}*/
					if (existEvalMarks!=null) {
						if(existEvalMarks.getFalseNo()==to.getFalseNo() || existEvalMarks.getFalseNo().equalsIgnoreCase(to.getFalseNo())){
							to.setExamEvalTo(existEvalMarks);
						}
					}else{
						to.setExamEvalTo(new ExamMarkEvaluationTo());
					}
					if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					studentList.add(to);
				}
			}
		}
		newExamMarksEntryForm.setIsfalsegenerated(isfalsegenerated);
		return studentList;
	}
	/**
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public String getQueryForPreviousClassStudents(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		String query="select s,classHis.classes.id from Student s" +
				" join s.studentPreviousClassesHistory classHis" +
				" join classHis.classes.classSchemewises classSchemewise" +
				" join s.studentSubjectGroupHistory subjHist" +
				" join subjHist.subjectGroup.subjectGroupSubjectses subSet" +
				" where s.admAppln.isCancelled=0  and subSet.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and classSchemewise.curriculumSchemeDuration.academicYear=(select e.academicYear" +
				" from ExamDefinitionBO e where e.id="+newExamMarksEntryForm.getExamId()+")" +
				" and classSchemewise.curriculumSchemeDuration.semesterYearNo=" +newExamMarksEntryForm.getSchemeNo()+
				" and classSchemewise.classes.course.id="+newExamMarksEntryForm.getCourseId()+
				" and subSet.subject.id="+newExamMarksEntryForm.getSubjectId();
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
			query=query+" and s.id in(select rt.studentId from ExamInternalRetestApplicationBO rt where rt.classId=521)";
		}
		query=query+" and subjHist.schemeNo="+newExamMarksEntryForm.getSchemeNo();
		return query;
	}
	/**
	 * @param studentList
	 * @param previousStudentList
	 * @param existsMarks
	 * @return
	 */
	public Set<StudentMarksTO> convertBotoTOListForPreviousClassStudents(Set<StudentMarksTO> studentList, List previousStudentList,MarksDetailsTO existsMarks,List<Integer> listOfDetainedStudents,NewExamMarksEntryForm newExamMarksEntryForm,ExamMarkEvaluationTo existEvalMarks) throws Exception {
		boolean disable=true;
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		boolean isfalsegenerated=false;
		Iterator<Object[]> itr=previousStudentList.iterator();
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
					int result=txn.checkSubjectInRetestForm(newExamMarksEntryForm,student.getId());
					if (result==0) {
						continue;
					}
				}
				
				if(!listOfDetainedStudents.contains(student.getId())){
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
						to.setRetests(true);
					}else{
						to.setRetests(false);
					}
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						
						
						String falsenoquery="select f.falseNo,f.id from ExamFalseNumberGen f where " +
						" f.classId.id="+objects[1].toString()+" and f.course.id="+newExamMarksEntryForm.getCourseId()+" and f.examId.id= "+newExamMarksEntryForm.getExamId() +
						" and f.studentId.id="+student.getId()+" and f.subject.id="+newExamMarksEntryForm.getSubjectId();
						
						/*String falsenoquery1="select marksCardSiNo.prefix,marksCardSiNo.id from MarksCardSiNo marksCardSiNo where " +
						"  marksCardSiNo.courseId="+newExamMarksEntryForm.getCourseId()+"and marksCardSiNo.semister="+newExamMarksEntryForm.getSchemeNo()+"and marksCardSiNo.academicYear="+newExamMarksEntryForm.getYear();*/
						
						
		
		List falsenoList=txn.getFalseNoForQuery(falsenoquery);
		if(falsenoList!=null && !falsenoList.isEmpty()){
			isfalsegenerated=true;
			Iterator<Object[]> itr2=falsenoList.iterator();
			while(itr2.hasNext()){
				Object[] obj = (Object[]) itr2.next();
			if(obj[0]!=null)
			to.setFalseNo(obj[0].toString());
			if(obj[1]!=null)
			to.setFalseNoId(obj[1].toString());
			}
		}
					/*if(existsMarks!=null){
						MarksDetailsTO detailTo=existsMarks;
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
					}else{
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
					}*/
					if (existEvalMarks!=null) {
						if(existEvalMarks.getFalseNo()==to.getFalseNo() || existEvalMarks.getFalseNo().equalsIgnoreCase(to.getFalseNo())){
							to.setExamEvalTo(existEvalMarks);
						}
					}else{
						to.setExamEvalTo(new ExamMarkEvaluationTo());
					}
					if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					studentList.add(to);
				}
			}
		}
		newExamMarksEntryForm.setIsfalsegenerated(isfalsegenerated);
		return studentList;
	}
	/**
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public String getQueryForSupplementaryCurrentClassStudents(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String query="select s,c.id,cd.academicYear  from Student s" +
				" join s.classSchemewise.classes c" +
				" join s.classSchemewise.curriculumSchemeDuration cd" +
				" join s.admAppln.applicantSubjectGroups appSub" +
				" join appSub.subjectGroup.subjectGroupSubjectses subGroup" +
				" join s.studentSupplementaryImprovements supImp with supImp.subject.id="+newExamMarksEntryForm.getSubjectId()+
				" and supImp.examDefinition.id="+newExamMarksEntryForm.getExamId()+
				" where s.admAppln.isCancelled=0 and subGroup.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and s.classSchemewise.curriculumSchemeDuration.academicYear>=(select e.examForJoiningBatch" +
				" from ExamDefinitionBO e where e.id="+newExamMarksEntryForm.getExamId()+")" +
				" and s.admAppln.courseBySelectedCourseId.id="+newExamMarksEntryForm.getCourseId()+
				" and supImp.classes.course.id="+newExamMarksEntryForm.getCourseId()+
				" and subGroup.subject.id="+newExamMarksEntryForm.getSubjectId()+
				" and s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+newExamMarksEntryForm.getSchemeNo()+" and subGroup.isActive=1 ";
		if(newExamMarksEntryForm.getSubjectType()!=null && !newExamMarksEntryForm.getSubjectType().isEmpty()){
			if(newExamMarksEntryForm.getSubjectType().equals("1")){
				query=query+" and supImp.isAppearedTheory=1";
			}else if(newExamMarksEntryForm.getSubjectType().equals("0")){
				query=query+" and supImp.isAppearedPractical=1";
			}else{
				query=query+" and supImp.isAppearedTheory=1 and supImp.isAppearedPractical=1";
			}
		}
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and s.classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		return query;
	}
	/**
	 * @param studentList
	 * @param currentStudentList
	 * @param existsMarks
	 * @return
	 */
	public Set<StudentMarksTO> convertBotoTOListForSupplementaryCurrentStudents(Set<StudentMarksTO> studentList, List currentStudentList,Map<Integer, MarksDetailsTO> existsMarks,NewExamMarksEntryForm newExamMarksEntryForm,Map<Integer,String> oldRegNo) throws Exception {
		boolean disable=true;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		Iterator<Object[]> itr=currentStudentList.iterator();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				StudentMarksTO to=new StudentMarksTO();
				to.setStudentId(student.getId());
				to.setName(student.getAdmAppln().getPersonalData().getFirstName());
				if(oldRegNo.containsKey(student.getId())){
					to.setRegisterNo(oldRegNo.get(student.getId()));
				}else{
					to.setRegisterNo(student.getRegisterNo());
				}
				if(existsMarks.containsKey(student.getId())){
					MarksDetailsTO detailTo=existsMarks.get(student.getId());
					to.setId(detailTo.getId());
					to.setPracticalMarks(detailTo.getPracticalMarks());
					to.setTheoryMarks(detailTo.getTheoryMarks());
					to.setOldPracticalMarks(detailTo.getPracticalMarks());
					to.setOldTheoryMarks(detailTo.getTheoryMarks());
					to.setClassId(detailTo.getClassId());
					to.setMarksId(detailTo.getMarksId());
					if(detailTo.isTheorySecured() || disable)
						to.setIsTheorySecured(true);
					else
						to.setIsTheorySecured(false);
					if(detailTo.isPracticalSecured() || disable)
						to.setIsPracticalSecured(true);
					else
						to.setIsPracticalSecured(false);
				}else{
					if(objects[1]!=null){
						to.setClassId(Integer.parseInt(objects[1].toString()));
					}
					to.setIsTheorySecured(false);
					to.setIsPracticalSecured(false);
				}
				if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
					to.setIsTheory(true);
					to.setIsPractical(false);
				}else if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
					to.setIsTheory(false);
					to.setIsPractical(true);
				}else{
					to.setIsTheory(true);
					to.setIsPractical(true);
				}
				if(objects[2] != null && objects[2].toString() != null){
					map.put(student.getId(), Integer.parseInt(objects[2].toString()));
				}
				studentList.add(to);
			}
		}
		newExamMarksEntryForm.setStudentsYearMap(map);
		return studentList;
	}
	/**
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public String getQueryForSupplementaryPreviousClassStudents(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		String query="select s,classHis.classes.id,cd.academicYear from Student s" +
				" join s.studentPreviousClassesHistory classHis" +
				" join classHis.classes.classSchemewises classSchemewise join classSchemewise.curriculumSchemeDuration cd " +
				" join s.studentSubjectGroupHistory subjHist" +
				" join subjHist.subjectGroup.subjectGroupSubjectses subSet" +
				" join s.studentSupplementaryImprovements supImp with supImp.subject.id=" +newExamMarksEntryForm.getSubjectId()+
				" and supImp.examDefinition.id=" +newExamMarksEntryForm.getExamId()+
				" where s.admAppln.isCancelled=0  and subSet.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and classSchemewise.curriculumSchemeDuration.academicYear>=(select e.examForJoiningBatch" +
				" from ExamDefinitionBO e" +
				" where e.id="+newExamMarksEntryForm.getExamId()+") and classSchemewise.curriculumSchemeDuration.semesterYearNo="+newExamMarksEntryForm.getSchemeNo()+
				" and classSchemewise.classes.course.id="+newExamMarksEntryForm.getCourseId()+
				" and subSet.subject.id="+newExamMarksEntryForm.getSubjectId()+
				" and supImp.classes.course.id=" +newExamMarksEntryForm.getCourseId();
		if(newExamMarksEntryForm.getSubjectType()!=null && !newExamMarksEntryForm.getSubjectType().isEmpty()){
			if(newExamMarksEntryForm.getSubjectType().equals("1")){
				query=query+" and supImp.isAppearedTheory=1";
			}else if(newExamMarksEntryForm.getSubjectType().equals("0")){
				query=query+" and supImp.isAppearedPractical=1";
			}else{
				query=query+" and supImp.isAppearedTheory=1 and supImp.isAppearedPractical=1";
			}
		}
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		return query;
	}
	/**
	 * @param studentList
	 * @param previousStudentList
	 * @param existsMarks
	 * @return
	 */
	public Set<StudentMarksTO> convertBotoTOListForSupplementaryPreviousClassStudents(Set<StudentMarksTO> studentList, List previousStudentList,Map<Integer, MarksDetailsTO> existsMarks,NewExamMarksEntryForm newExamMarksEntryForm,Map<Integer,String> oldRegNo) throws Exception{
		boolean disable=true;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		Iterator<Object[]> itr=previousStudentList.iterator();
		Map<Integer, Integer> map = newExamMarksEntryForm.getStudentsYearMap();
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				StudentMarksTO to=new StudentMarksTO();
				to.setStudentId(student.getId());
				to.setName(student.getAdmAppln().getPersonalData().getFirstName());
				if(oldRegNo.containsKey(student.getId())){
					to.setRegisterNo(oldRegNo.get(student.getId()));
				}else{
					to.setRegisterNo(student.getRegisterNo());
				}
				if(existsMarks.containsKey(student.getId())){
					MarksDetailsTO detailTo=existsMarks.get(student.getId());
					to.setId(detailTo.getId());
					to.setPracticalMarks(detailTo.getPracticalMarks());
					to.setTheoryMarks(detailTo.getTheoryMarks());
					to.setOldPracticalMarks(detailTo.getPracticalMarks());
					to.setOldTheoryMarks(detailTo.getTheoryMarks());
					to.setClassId(detailTo.getClassId());
					to.setMarksId(detailTo.getMarksId());
					if(detailTo.isTheorySecured() || disable)
						to.setIsTheorySecured(true);
					else
						to.setIsTheorySecured(false);
					if(detailTo.isPracticalSecured() || disable)
						to.setIsPracticalSecured(true);
					else
						to.setIsPracticalSecured(false);
				}else{
					if(objects[1]!=null){
						to.setClassId(Integer.parseInt(objects[1].toString()));
					}
					to.setIsTheorySecured(false);
					to.setIsPracticalSecured(false);
				}
				if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
					to.setIsTheory(true);
					to.setIsPractical(false);
				}else if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
					to.setIsTheory(false);
					to.setIsPractical(true);
				}else{
					to.setIsTheory(true);
					to.setIsPractical(true);
				}
				if(map != null){
					if(objects[2] != null && objects[2].toString() != null){
						map.put(student.getId(), Integer.parseInt(objects[2].toString()));
					}
				}
				studentList.add(to);
			}
		}
		newExamMarksEntryForm.setStudentsYearMap(map);
		return studentList;
	}
	/**
	 * @param schemeNo
	 * @return
	 * @throws Exception
	 */
	public String getQueryForOldRegisterNos(String schemeNo) throws Exception{
//		String query="select e.student.id, min(e.registerNo) from ExamStudentDetentionRejoinDetails e left join e.rejoinClassSchemewise classSche left join classSche.classes c where ((e.schemeNo < c.termNumber) or (c.termNumber is null))" +
//				" and "+schemeNo+"<= e.schemeNo group by e.student.id ";
		String query="select stu.student.id,stu.registerNo " +
				" from StudentOldRegisterNumber stu " +
				" where stu.isActive=1 and stu.schemeNo="+schemeNo+" order by stu.student.id ";
		return query;
	}
	/**
	 * @param oldRegList
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, String> getOldRegMap(List oldRegList) throws Exception {
		Map<Integer,String> map=new HashMap<Integer, String>();
		if(oldRegList!=null && !oldRegList.isEmpty()){
			Iterator<Object[]> itr=oldRegList.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				if(obj[0]!=null && obj[1]!=null)
					map.put(Integer.parseInt(obj[0].toString()),obj[1].toString());
			}
		}
		return map;
	}
	
	//raghu for all internals
	public Map<String, MarksDetailsTO> convertBoDataToMarksMapForAll(List marksList) throws Exception {
		Map<String, MarksDetailsTO> marksMap=new HashMap<String, MarksDetailsTO>();
		Iterator<MarksEntryDetails> itr=marksList.iterator();
		while (itr.hasNext()) {
			MarksEntryDetails marksEntryDetails = (MarksEntryDetails) itr.next();
			MarksDetailsTO to=new MarksDetailsTO();
			to.setId(marksEntryDetails.getId());
			to.setTheoryMarks(marksEntryDetails.getTheoryMarks());
			to.setPracticalMarks(marksEntryDetails.getPracticalMarks());
			if(marksEntryDetails.getMarksEntry().getClasses()!=null)
				to.setClassId(marksEntryDetails.getMarksEntry().getClasses().getId());
			to.setMarksId(marksEntryDetails.getMarksEntry().getId());
			if(marksEntryDetails.getIsTheorySecured()!=null && marksEntryDetails.getIsTheorySecured()){
				to.setTheorySecured(true);
			}else{
				to.setTheorySecured(false);
			}
			if(marksEntryDetails.getIsPracticalSecured()!=null && marksEntryDetails.getIsPracticalSecured()){
				to.setPracticalSecured(true);
			}else{
				to.setPracticalSecured(false);
			}
			marksMap.put(marksEntryDetails.getMarksEntry().getStudent().getId()+"_"+marksEntryDetails.getMarksEntry().getExam().getId(),to);
		}
		return marksMap;
	}
	
	public List<StudentTO> convertBotoTOListForCurrentStudentsForAll(List<StudentTO> allstudentsList,List currentStudentList, Map<String, MarksDetailsTO> existsMarks,List<Integer> listOfDetainedStudents,NewExamMarksEntryForm newExamMarksEntryForm/*,Map<Integer,String> oldRegNo*/) throws Exception {
		
		
		Set<Integer> examSet=newExamMarksEntryForm.getExamNameList().keySet();
		Map<Integer,Boolean> examExceeded=new HashMap<Integer, Boolean>();
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		Map<Integer,Double> maxMarkMap=NewExamMarksEntryHandler.getInstance().getMaxMarkOfSubjectForAllInternals(newExamMarksEntryForm);
		Iterator<Integer> examitrtr=examSet.iterator();
		while (examitrtr.hasNext()) {	
		int examId=examitrtr.next();
		newExamMarksEntryForm.setExamid(examId);
		boolean exceeded=txn.isCurrentDateValidForAllInternalMarks(newExamMarksEntryForm);
		newExamMarksEntryForm.setIsExamMaxEntry(false);
		if(CMSConstants.INTERNAL_EXAM_IDS.contains(examId))
			newExamMarksEntryForm.setIsExamMaxEntry(true);
		examExceeded.put(examId, exceeded);
		}
		Iterator<Object[]> itr=currentStudentList.iterator();
		boolean disable=true;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		while (itr.hasNext()) {
			
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if(!listOfDetainedStudents.contains(student.getId())){
					float totalMarksT=0;
					float totalMarksP=0;
					boolean zeroMarks=false;
					String totalMarksTS=null;
					String totalMarksPS=null;
					StudentTO studentto=new StudentTO();
					studentto.setId(student.getId());
					studentto.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
					studentto.setRegisterNo(student.getRegisterNo());
					studentto.setRollNo(student.getRollNo());
					List<StudentMarksTO> studentMarksList=new ArrayList<StudentMarksTO>();
					Iterator<Integer> examitr=examSet.iterator();
					while (examitr.hasNext()) {
						
					int examId=examitr.next();
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					//if(to.getStudentId()== 12749){
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						if(newExamMarksEntryForm.getIsRoleIdForDeveloper()){
							to.setAllInternalDateExceeded(true);
						}else{
							to.setAllInternalDateExceeded(examExceeded.get(examId));
						}
					
					//exist marks to
					to.setIsConversion(false);
					if(existsMarks.containsKey(student.getId()+"_"+examId)){
						zeroMarks=true;
						MarksDetailsTO detailTo=existsMarks.get(student.getId()+"_"+examId);
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						to.setExamId(""+examId);
						double actualMarks = 0.0;
						if(CMSConstants.INTERNAL_EXAM_IDS.contains(examId)){
							if(detailTo.getTheoryMarks()!=null && !detailTo.getTheoryMarks().isEmpty() && CommonUtil.isValidDecimal(detailTo.getTheoryMarks())){
							double convMark=Double.parseDouble(detailTo.getTheoryMarks());
//							Iterator<StudentTO> iterator =  (Iterator<StudentTO>) newExamMarksEntryForm.getStudentMarksList();
//							while(iterator.hasNext()){
//								StudentTO stuTo = iterator.next();
//								Iterator<StudentMarksTO> iterator2 = (Iterator<StudentMarksTO>) stuTo.getStudentMarksList();
//								while(iterator2.hasNext()){
//									StudentMarksTO marksTO = iterator2.next();
//									if(to.getStudentId()== marksTO.getStudentId()){
//										actualMarks =Double.parseDouble(marksTO.getTheoryMarks());
//									}
//									
//								}
//							}
							convMark=CommonUtil.round((convMark*80)/10, 0);
							//studentto.setMaxMarksInternal(String.valueOf((int)convMark));
							
							//code for any type marks entry rounding
							if(convMark != 0.0){
								
								String s = String.valueOf(convMark);
								char[] ch = s.toCharArray();
								if(ch.length == 2){
								if(Integer.parseInt(String.valueOf(ch[2])) == 0){
									convMark = Math.round(convMark);
								}
								}else if(ch.length == 3){
									if(Integer.parseInt(String.valueOf(ch[2])) == 0){
										convMark = Math.round(convMark);
									}
								}else if(ch.length > 3){
									if(Integer.parseInt(String.valueOf(ch[3])) == 0){
										convMark = Math.round(convMark);
									}
								}
							}
						/*	String change = String.valueOf(convMark).substring(3,4);
						     if(change.equalsIgnoreCase("0")){
								convMark = Math.round(convMark);
							}
								}*/
								
							
							studentto.setMaxMarksInternal(String.valueOf(convMark));
							//to.setTheoryMarks(String.valueOf((int)convMark));
							to.setTheoryMarks(String.valueOf(convMark));
							studentto.setMaxMarksInternal(detailTo.getTheoryMarks());
							to.setIsConversion(true);
							}else if(detailTo.getTheoryMarks()!=null){
								to.setIsConversion(true);
								studentto.setMaxMarksInternal(String.valueOf(detailTo.getTheoryMarks()));
								to.setTheoryMarks(String.valueOf(detailTo.getTheoryMarks()));
									
							}

							if(detailTo.getPracticalMarks()!=null && CommonUtil.isValidDecimal(detailTo.getPracticalMarks())){
							double convMark=Double.parseDouble(detailTo.getPracticalMarks());
							convMark=CommonUtil.round((convMark*80)/10, 0);
							
							//code for any type marks entry rounding
							if(convMark != 0.0){
								
								String s = String.valueOf(convMark);
								char[] ch = s.toCharArray();
								if(ch.length == 2){
								if(Integer.parseInt(String.valueOf(ch[2])) == 0){
									convMark = Math.round(convMark);
								}
								}else if(ch.length == 3){
									if(Integer.parseInt(String.valueOf(ch[2])) == 0){
										convMark = Math.round(convMark);
									}
								}else if(ch.length > 3){
									if(Integer.parseInt(String.valueOf(ch[3])) == 0){
										convMark = Math.round(convMark);
									}
								}
							}
							/*String change = String.valueOf(convMark).substring(3,4);
						     if(change.equalsIgnoreCase("0")){
								convMark = Math.round(convMark);
							}
								}*/
							//studentto.setMaxMarksInternal(String.valueOf((int)convMark));
							studentto.setMaxMarksInternal(String.valueOf(convMark));
							//to.setPracticalMarks(String.valueOf((int)convMark));
							to.setPracticalMarks(String.valueOf(convMark));
							studentto.setMaxMarksInternal(detailTo.getPracticalMarks());
							to.setIsConversion(true);
							}else if(detailTo.getPracticalMarks()!=null){
								to.setIsConversion(true);
								studentto.setMaxMarksInternal(String.valueOf(detailTo.getPracticalMarks()));
								to.setPracticalMarks(String.valueOf(detailTo.getPracticalMarks()));
									
							}
							
						}
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
						
						if(detailTo.getTheoryMarks()!=null && !StringUtils.isAlpha(detailTo.getTheoryMarks())){
						totalMarksT=totalMarksT+Float.parseFloat(detailTo.getTheoryMarks());
						}else{
							totalMarksTS=detailTo.getTheoryMarks();
						}
						if(detailTo.getPracticalMarks()!=null && !StringUtils.isAlpha(detailTo.getPracticalMarks())){
						totalMarksP=totalMarksP+Float.parseFloat(detailTo.getPracticalMarks());
						}else{
							totalMarksPS=detailTo.getPracticalMarks();
						}
						
					}
					//new to
					else{
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
						to.setExamId(""+examId);
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
					}
					
					
					if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					if(maxMarkMap.containsKey(examId))
						to.setMaxMarks(maxMarkMap.get(examId));
					studentMarksList.add(to);
					//} particular student checking
					}// exam set	
					
					if(totalMarksT!=0){
						if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
							if(newExamMarksEntryForm.getYear().equalsIgnoreCase("2015")){
								studentto.setTotalInternalMarksT((Math.ceil(totalMarksT))+"");
							}else{
								studentto.setTotalInternalMarksT(df.format(totalMarksT)+"");
							}
							
							
						}else{
							studentto.setTotalInternalMarksT((Math.ceil(totalMarksT))+"");
						}
					}else{
						if(totalMarksTS!=null){
						studentto.setTotalInternalMarksT(totalMarksTS);
						}else if(zeroMarks){
							studentto.setTotalInternalMarksT(totalMarksT+"");	
						}
					}
					
					if(totalMarksP!=0){
						if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
							if(newExamMarksEntryForm.getYear().equalsIgnoreCase("2015")){
								studentto.setTotalInternalMarksP((Math.ceil(totalMarksP))+"");
							}else{
								studentto.setTotalInternalMarksP(df.format(totalMarksP)+"");
							}
							
							
						}else{
							studentto.setTotalInternalMarksP((Math.ceil(totalMarksP))+"");
						}
					}else{
						if(totalMarksPS!=null){
							studentto.setTotalInternalMarksP(totalMarksPS);
						}else if(zeroMarks){
							studentto.setTotalInternalMarksP(totalMarksP+"");	
						}
					}
					
						
					ExamComparator e=new ExamComparator();
					e.setCompare(0);
					Collections.sort(studentMarksList, e);
					studentto.setStudentMarksList(studentMarksList);
					allstudentsList.add(studentto);
					
					newExamMarksEntryForm.setProgramTypeId(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()+"");
						
					
				}//detained check
			}//empty check
			
			
		}//main students over
		
		return allstudentsList;
	}
	
	public List<StudentTO> convertBotoTOListForPreviousClassStudentsForAll(List<StudentTO> allstudentsList, List previousStudentList,Map<String, MarksDetailsTO> existsMarks,List<Integer> listOfDetainedStudents,NewExamMarksEntryForm newExamMarksEntryForm/*,Map<Integer,String> oldRegNo*/) throws Exception {
		
		
		Set<Integer> examSet=newExamMarksEntryForm.getExamNameList().keySet();
		Map<Integer,Boolean> examExceeded=new HashMap<Integer, Boolean>();
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		Map<Integer,Double> maxMarkMap=NewExamMarksEntryHandler.getInstance().getMaxMarkOfSubjectForAllInternals(newExamMarksEntryForm);
		Iterator<Integer> examitrtr=examSet.iterator();
		while (examitrtr.hasNext()) {	
		int examId=examitrtr.next();
		newExamMarksEntryForm.setExamid(examId);
		boolean exceeded=txn.isCurrentDateValidForAllInternalMarks(newExamMarksEntryForm);
		newExamMarksEntryForm.setIsExamMaxEntry(false);
		if(CMSConstants.INTERNAL_EXAM_IDS.contains(examId))
			newExamMarksEntryForm.setIsExamMaxEntry(true);
		examExceeded.put(examId, exceeded);
		}
		
		boolean disable=true;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		Iterator<Object[]> itr=previousStudentList.iterator();
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if(!listOfDetainedStudents.contains(student.getId())){
					float totalMarksT=0;
					float totalMarksP=0;
					boolean zeroMarks=false;
					String totalMarksTS=null;
					String totalMarksPS=null;
					StudentTO studentto=new StudentTO();
					studentto.setId(student.getId());
					studentto.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
					studentto.setRegisterNo(student.getRegisterNo());
					studentto.setRollNo(student.getRollNo());
					List<StudentMarksTO> studentMarksList=new ArrayList<StudentMarksTO>();
					Iterator<Integer> examitr=examSet.iterator();
					while (examitr.hasNext()) {
						
					int examId=examitr.next();
					
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						if(newExamMarksEntryForm.getIsRoleIdForDeveloper()){
							to.setAllInternalDateExceeded(true);
						}else{
							to.setAllInternalDateExceeded(examExceeded.get(examId));
						}
						
					//exist marks to
					to.setIsConversion(false);	
					if(existsMarks.containsKey(student.getId()+"_"+examId)){
						zeroMarks=true;
						MarksDetailsTO detailTo=existsMarks.get(student.getId()+"_"+examId);
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						to.setExamId(""+examId);
						if(CMSConstants.INTERNAL_EXAM_IDS.contains(examId)){
							if(detailTo.getTheoryMarks()!=null && !detailTo.getTheoryMarks().isEmpty() && CommonUtil.isValidDecimal(detailTo.getTheoryMarks())){
								double convMark=Double.parseDouble(detailTo.getTheoryMarks());
								boolean lessThan10 = false;
								convMark=CommonUtil.round((convMark*80)/10, 0);
								//code for any type marks entry rounding
								if(convMark != 0.0){
								/*	if(String.valueOf(convMark).indexOf(4)== -1){
										lessThan10 = true;
										String lesschange = String.valueOf(convMark).substring(3,4);
										if(Integer.parseInt(lesschange) > 0){
											convMark = Math.round(convMark);
										}
									}*/
									String s = String.valueOf(convMark);
									char[] ch = s.toCharArray();
									if(ch.length == 2){
									if(Integer.parseInt(String.valueOf(ch[2])) == 0){
										convMark = Math.round(convMark);
									}
									}else if(ch.length == 3){
										if(Integer.parseInt(String.valueOf(ch[2])) == 0){
											convMark = Math.round(convMark);
										}
									}else if(ch.length > 3){
										if(Integer.parseInt(String.valueOf(ch[3])) == 0){
											convMark = Math.round(convMark);
										}
									}
									
								/*String change = String.valueOf(convMark).substring(3,4);
							     if(change.equalsIgnoreCase("0")){
									convMark = Math.round(convMark);
								}*/
									
								}
								//studentto.setMaxMarksInternal(String.valueOf((int)convMark));
								studentto.setMaxMarksInternal(String.valueOf(convMark));
								//to.setTheoryMarks(String.valueOf((int)convMark));
								to.setTheoryMarks(String.valueOf(convMark));
								studentto.setMaxMarksInternal(detailTo.getTheoryMarks());
								to.setIsConversion(true);
								}else if(detailTo.getTheoryMarks()!=null){
									to.setIsConversion(true);
									studentto.setMaxMarksInternal(String.valueOf(detailTo.getTheoryMarks()));
									to.setTheoryMarks(String.valueOf(detailTo.getTheoryMarks()));
										
								}

								if(detailTo.getPracticalMarks()!=null && CommonUtil.isValidDecimal(detailTo.getPracticalMarks())){
								double convMark=Double.parseDouble(detailTo.getPracticalMarks());
								convMark=CommonUtil.round((convMark*80)/10, 0);
								
								//code for any type marks entry rounding
								if(convMark != 0.0){
									
									String s = String.valueOf(convMark);
									char[] ch = s.toCharArray();
									if(ch.length == 2){
									if(Integer.parseInt(String.valueOf(ch[2])) == 0){
										convMark = Math.round(convMark);
									}
									}else if(ch.length == 3){
										if(Integer.parseInt(String.valueOf(ch[2])) == 0){
											convMark = Math.round(convMark);
										}
									}else if(ch.length > 3){
										if(Integer.parseInt(String.valueOf(ch[3])) == 0){
											convMark = Math.round(convMark);
										}
									}
								}
							/*	String change = String.valueOf(convMark).substring(3,4);
							     if(change.equalsIgnoreCase("0")){
									convMark = Math.round(convMark);
								}
									}*/
								//studentto.setMaxMarksInternal(String.valueOf((int)convMark));
								studentto.setMaxMarksInternal(String.valueOf(convMark));
								//to.setPracticalMarks(String.valueOf((int)convMark));
								to.setPracticalMarks(String.valueOf(convMark));
								studentto.setMaxMarksInternal(detailTo.getPracticalMarks());
								to.setIsConversion(true);
								}else if(detailTo.getPracticalMarks()!=null){
									to.setIsConversion(true);
									studentto.setMaxMarksInternal(String.valueOf(detailTo.getPracticalMarks()));
									to.setPracticalMarks(String.valueOf(detailTo.getPracticalMarks()));
										
								}
								
							}
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
						
						if(detailTo.getTheoryMarks()!=null && !StringUtils.isAlpha(detailTo.getTheoryMarks())){
							totalMarksT=totalMarksT+Float.parseFloat(detailTo.getTheoryMarks());
							}else{
								totalMarksTS=detailTo.getTheoryMarks();
							}
						if(detailTo.getPracticalMarks()!=null && !StringUtils.isAlpha(detailTo.getPracticalMarks())){
							totalMarksP=totalMarksP+Float.parseFloat(detailTo.getPracticalMarks());
							}else{
								totalMarksPS=detailTo.getPracticalMarks();
							}
					}
					//new marks to
					else{
						to.setExamId(""+examId);
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
					}
					
					if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					if(maxMarkMap.containsKey(examId))
						to.setMaxMarks(maxMarkMap.get(examId));
					to.compareTo(to);;
					studentMarksList.add(to);
				}//exam set
					
					if(totalMarksT!=0){
						
						if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
							if(newExamMarksEntryForm.getYear().equalsIgnoreCase("2015")){
								studentto.setTotalInternalMarksT((Math.ceil(totalMarksT))+"");
							}else{
								studentto.setTotalInternalMarksT(df.format(totalMarksT)+"");
							}
							
							
						}else{
							studentto.setTotalInternalMarksT((Math.ceil(totalMarksT))+"");
						}
						
						
					}else{
						if(totalMarksTS!=null){
						studentto.setTotalInternalMarksT(totalMarksTS);
						}else if(zeroMarks){
							studentto.setTotalInternalMarksT(totalMarksT+"");	
						}
					}
					
					if(totalMarksP!=0){
						if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
							if(newExamMarksEntryForm.getYear().equalsIgnoreCase("2015")){
								studentto.setTotalInternalMarksP((Math.ceil(totalMarksP))+"");
							}else{
								studentto.setTotalInternalMarksP(df.format(totalMarksP)+"");
							}
							
							
						}else{
							studentto.setTotalInternalMarksP((Math.ceil(totalMarksP))+"");
						}
						
					}else{
						if(totalMarksPS!=null){
							studentto.setTotalInternalMarksP(totalMarksPS);
						}else if(zeroMarks){
							studentto.setTotalInternalMarksP(totalMarksP+"");	
						}
					}
					
					ExamComparator e=new ExamComparator();
					e.setCompare(0);
					Collections.sort(studentMarksList, e);
					studentto.setStudentMarksList(studentMarksList);
					allstudentsList.add(studentto);
					
					newExamMarksEntryForm.setProgramTypeId(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()+"");
					
				}//detained list
			}//empty check
			
			
		}//main student over
		
		return allstudentsList;
	}
	
	
	
	public String getQueryForCurrentClassStudentsForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String query="select s,s.classSchemewise.classes.id from Student s" +
				" join s.admAppln.applicantSubjectGroups appSub" +
				" join appSub.subjectGroup.subjectGroupSubjectses subGroup" +
				" where s.admAppln.isCancelled=0 and subGroup.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and s.classSchemewise.curriculumSchemeDuration.academicYear="+newExamMarksEntryForm.getYear() +
				" and subGroup.subject.id=" +newExamMarksEntryForm.getSubjectId()+
				" and s.admAppln.courseBySelectedCourseId.id=" +newExamMarksEntryForm.getCourseId()+
				" and s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+newExamMarksEntryForm.getSchemeNo();
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and s.classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		return query;
	}
	
	public String getQueryForPreviousClassStudentsForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		String query="select s,classHis.classes.id from Student s" +
				" join s.studentPreviousClassesHistory classHis" +
				" join classHis.classes.classSchemewises classSchemewise" +
				" join s.studentSubjectGroupHistory subjHist" +
				" join subjHist.subjectGroup.subjectGroupSubjectses subSet" +
				" where s.admAppln.isCancelled=0  and subSet.isActive=1 and (s.isHide=0 or s.isHide is null)" +
				" and classSchemewise.curriculumSchemeDuration.academicYear="+newExamMarksEntryForm.getYear()+
				" and classSchemewise.curriculumSchemeDuration.semesterYearNo=" +newExamMarksEntryForm.getSchemeNo()+
				" and classSchemewise.classes.course.id="+newExamMarksEntryForm.getCourseId()+
				" and subSet.subject.id="+newExamMarksEntryForm.getSubjectId();
		if(newExamMarksEntryForm.getSection()!=null && !newExamMarksEntryForm.getSection().isEmpty() && !newExamMarksEntryForm.getSection().equalsIgnoreCase("0")){
			query=query+" and classSchemewise.classes.id="+newExamMarksEntryForm.getSection();
		}
		query=query+" and subjHist.schemeNo="+newExamMarksEntryForm.getSchemeNo();
		return query;
	}
	public ExamMarkEvaluationTo convertEvalBoDataToMarksMap(ExamMarkEvaluationBo bo) {
		ExamMarkEvaluationTo to=new ExamMarkEvaluationTo();
		to.setId(bo.getId());
		to.setFalseNo(bo.getFalseNo());
		if (bo.getFirstEvaluator()!=null) {
			to.setFirstEvaluation(String.valueOf(bo.getFirstEvaluation()));
			to.setFirstEvaluator(String.valueOf(bo.getFirstEvaluator().getId()));
		}
		if (bo.getSecondEvaluator()!=null) {
			to.setSecondEvaluation(String.valueOf(bo.getSecondEvaluation()));
			to.setSecondEvaluator(String.valueOf(bo.getSecondEvaluator().getId()));
		}
		if (bo.getThirdEvaluator()!=null) {
			to.setThirdEvaluation(String.valueOf(bo.getThirdEvaluation()));
			to.setThirdEvaluator(String.valueOf(bo.getThirdEvaluator().getId()));
		}	
		if (bo.getFinalEvaluator()!=null) {
			to.setFinalEvaluation(String.valueOf(bo.getFinalEvaluation()));
			to.setFirstEvaluator(String.valueOf(bo.getFinalEvaluator().getId()));
		}	
		
		return to;
	}
	public List<ExamMarkEvaluationBo> convertEvationTOBO(NewExamMarksEntryForm newExamMarksEntryForm) {
		IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();
		
		List<ExamMarkEvaluationTo> toList=newExamMarksEntryForm.getExamEvalToList();
		List<ExamMarkEvaluationBo> boList =new ArrayList<ExamMarkEvaluationBo>();
		
		for (ExamMarkEvaluationTo to : toList) {
			ExamMarkEvaluationBo bo =new ExamMarkEvaluationBo();
			int id=transaction.getDuplication(newExamMarksEntryForm.getFalseNo());
			if (id!=0) {
				bo.setId(id);
			}
			if (to.getId()!=0) {
				bo.setId(to.getId());
			}
			bo.setFalseNo(to.getFalseNo());
			newExamMarksEntryForm.setLastEnteredFalsenum(newExamMarksEntryForm.getFalseNo());
			if (to.getFirstEvaluation()!=null && !to.getFirstEvaluation().isEmpty()) {
				bo.setFirstEvaluation(Integer.parseInt(to.getFirstEvaluation()));
				
			}
			if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==1 ){
				Users user=new Users();
				user.setId(Integer.parseInt(newExamMarksEntryForm.getUserId()));
				bo.setFirstEvaluator(user);
			}else if(to.getFirstEvaluator()!=null){
				Users user=new Users();
				user.setId(Integer.parseInt(to.getFirstEvaluator()));
				bo.setFirstEvaluator(user);
			}
			if (to.getSecondEvaluation()!=null && !to.getSecondEvaluation().isEmpty()) {
				bo.setSecondEvaluation(Integer.parseInt(to.getSecondEvaluation()));
				
			}
			if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==2 ){
				Users user=new Users();
				user.setId(Integer.parseInt(newExamMarksEntryForm.getUserId()));
				bo.setSecondEvaluator(user);
			}else if(to.getSecondEvaluator()!=null){
				Users user=new Users();
				user.setId(Integer.parseInt(to.getSecondEvaluator()));
				bo.setSecondEvaluator(user);
			}
			if (to.getThirdEvaluation()!=null && !to.getThirdEvaluation().isEmpty()) {
				bo.setThirdEvaluation(Integer.parseInt(to.getThirdEvaluation()));
				
			}
			if (Integer.parseInt(newExamMarksEntryForm.getEvalNo())==3 ){
				Users user=new Users();
				user.setId(Integer.parseInt(newExamMarksEntryForm.getUserId()));
				bo.setThirdEvaluator(user);
			}else if(to.getThirdEvaluator()!=null){
				Users user=new Users();
				user.setId(Integer.parseInt(to.getThirdEvaluator()));
				bo.setThirdEvaluator(user);
			}
			boList.add(bo);
		}
		
		
		newExamMarksEntryForm.getStudentMarksTo().setExamEvalTo(new ExamMarkEvaluationTo());
		newExamMarksEntryForm.setFalseNo(null);
		return boList;
	}
	public Set<StudentMarksTO> convertFinnalMarkBotoTOListForCurrentStudents(Set<StudentMarksTO> studentList,
			List currentStudentList, Map<Integer, MarksDetailsTO> existsMarks, List<Integer> listOfDetainedStudents,
			NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		Iterator<Object[]> itr=currentStudentList.iterator();
		boolean disable=true;
		boolean isfalsegenerated=false;
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if(!listOfDetainedStudents.contains(student.getId())){
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
						to.setRetests(true);
					}else{
						to.setRetests(false);
					}
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						
						String falsenoquery="select f.falseNo,f.id from ExamFalseNumberGen f where " +
						" f.classId.id="+objects[1].toString()+" and f.course.id="+newExamMarksEntryForm.getCourseId()+" and f.examId.id= "+newExamMarksEntryForm.getExamId() +
						" and f.studentId.id="+student.getId()+" and f.subject.id="+newExamMarksEntryForm.getSubjectId();
						
						
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		List falsenoList=txn.getFalseNoForQuery(falsenoquery);
		if(falsenoList!=null && !falsenoList.isEmpty()){
			isfalsegenerated=true;
			Iterator<Object[]> itr2=falsenoList.iterator();
			while(itr2.hasNext()){
				Object[] obj = (Object[]) itr2.next();
			if(obj[0]!=null)
			to.setFalseNo(obj[0].toString());
			if(obj[1]!=null)
			to.setFalseNoId(obj[1].toString());
			}
		}
		if (to.getFalseNo().equals("2002204P0110210")) {
			System.out.println();
		}
		ExamMarkEvaluationTo evalTO=null;
		ExamMarkEvaluationBo evalbo=FalseExamMarksEntryTransactionImpl.getInstance().getEvalBo(to.getFalseNo());
		if (evalbo!=null) {
			evalTO=FalseExamMarksEntryHelper.getInstance().convertEvalBoDataToMarksMap(evalbo);
		}else{
			evalTO=new ExamMarkEvaluationTo();
			
		}
					to.setExamEvalTo(evalTO);
					if(existsMarks.containsKey(student.getId())){
						MarksDetailsTO detailTo=existsMarks.get(student.getId());
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
					}else{
						if (evalTO!=null && evalTO.getThirdEvaluation()==null 
								&& evalTO.getFirstEvaluation()!=null && evalTO.getSecondEvaluation()!=null) {
							//enter mark
							ExamFalseNumberGen falsBo=FalseExamMarksEntryTransactionImpl.getInstance().getDetailsByFalsenum(to.getFalseNo());
							if (falsBo.getSubject().getIsTheoryPractical()=="T" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("T")) {
								to.setTheoryMarks(String.valueOf(Math.round((new Double(evalTO.getFirstEvaluation())+new Double(evalTO.getSecondEvaluation()))/2)));
								
							}else if (falsBo.getSubject().getIsTheoryPractical()=="P" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("P")) {
								to.setPracticalMarks(String.valueOf(Math.round((new Double(evalTO.getFirstEvaluation())+new Double(evalTO.getSecondEvaluation()))/2)));
								
							}
							
						}else if (evalTO!=null && evalTO.getThirdEvaluation()!=null 
								&& evalTO.getFirstEvaluation()!=null && evalTO.getSecondEvaluation()!=null) {
							//enter mark
							ExamFalseNumberGen falsBo=FalseExamMarksEntryTransactionImpl.getInstance().getDetailsByFalsenum(to.getFalseNo());
							if (falsBo.getSubject().getIsTheoryPractical()=="T" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("T")) {
								int firstDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getFirstEvaluation());
								if (firstDiff<0) {
									firstDiff=firstDiff*(-1);
								}
								
								int secondDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getSecondEvaluation());
								if (secondDiff<0) {
									secondDiff=secondDiff*(-1);
								}
								if (firstDiff == secondDiff) {
									String finalMark=findTwoMaxNumbers(Integer.parseInt(evalTO.getFirstEvaluation()), Integer.parseInt(evalTO.getSecondEvaluation()), Integer.parseInt(evalTO.getThirdEvaluation()));
									to.setTheoryMarks(finalMark);
								}
								else if (firstDiff<secondDiff) {
									Double result=new Double(0);
									result=((new Double(evalTO.getFirstEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}else{
									Double result=new Double(0);
									result=((new Double(evalTO.getSecondEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}
								
								
								
							}else if (falsBo.getSubject().getIsTheoryPractical()=="P" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("P")) {
								int firstDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getFirstEvaluation());
								if (firstDiff<0) {
									firstDiff=firstDiff*(-1);
								}
								
								int secondDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getSecondEvaluation());
								if (secondDiff<0) {
									secondDiff=secondDiff*(-1);
								}
								if(firstDiff == secondDiff){
									String finalMark=findTwoMaxNumbers(Integer.parseInt(evalTO.getFirstEvaluation()), Integer.parseInt(evalTO.getSecondEvaluation()), Integer.parseInt(evalTO.getThirdEvaluation()));
									to.setPracticalMarks(finalMark);
								}
								else if (firstDiff<secondDiff) {
									Double result=new Double(0);
									result=((new Double(evalTO.getFirstEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}else{
									Double result=new Double(0);
									result=((new Double(evalTO.getSecondEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}
								
								
								
							}
							
						}
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
					}
					if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					studentList.add(to);
				}
			}
		}
		newExamMarksEntryForm.setIsfalsegenerated(isfalsegenerated);
		return studentList;
	}
	public Set<StudentMarksTO> convertFinalMarkBotoTOListForPreviousClassStudents(Set<StudentMarksTO> studentList,
			List previousStudentList, Map<Integer, MarksDetailsTO> existsMarks, List<Integer> listOfDetainedStudents,
			NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		boolean disable=true;
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		if(!newExamMarksEntryForm.isInternal() && !newExamMarksEntryForm.isRegular())
			disable=false;
		boolean isfalsegenerated=false;
		Iterator<Object[]> itr=previousStudentList.iterator();
		while (itr.hasNext()) {
			Object[] objects = (Object[]) itr.next();
			if(objects[0]!=null){
				Student student=(Student)objects[0];
				if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
					int result=txn.checkSubjectInRetestForm(newExamMarksEntryForm,student.getId());
					if (result==0) {
						continue;
					}
				}
				
				if(!listOfDetainedStudents.contains(student.getId())){
					StudentMarksTO to=new StudentMarksTO();
					to.setStudentId(student.getId());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					to.setName(student.getAdmAppln().getPersonalData().getFirstName());
					if (newExamMarksEntryForm.getRetest()!=null && newExamMarksEntryForm.getRetest().equalsIgnoreCase("yes")) {
						to.setRetests(true);
					}else{
						to.setRetests(false);
					}
					/*if(oldRegNo.containsKey(student.getId())){
						to.setRegisterNo(oldRegNo.get(student.getId()));
					}else{*/
						to.setRegisterNo(student.getRegisterNo());
//					}
						
						
						String falsenoquery="select f.falseNo,f.id from ExamFalseNumberGen f where " +
						" f.classId.id="+objects[1].toString()+" and f.course.id="+newExamMarksEntryForm.getCourseId()+" and f.examId.id= "+newExamMarksEntryForm.getExamId() +
						" and f.studentId.id="+student.getId()+" and f.subject.id="+newExamMarksEntryForm.getSubjectId();
						
						/*String falsenoquery1="select marksCardSiNo.prefix,marksCardSiNo.id from MarksCardSiNo marksCardSiNo where " +
						"  marksCardSiNo.courseId="+newExamMarksEntryForm.getCourseId()+"and marksCardSiNo.semister="+newExamMarksEntryForm.getSchemeNo()+"and marksCardSiNo.academicYear="+newExamMarksEntryForm.getYear();*/
						
						
		
		List falsenoList=txn.getFalseNoForQuery(falsenoquery);
		if(falsenoList!=null && !falsenoList.isEmpty()){
			isfalsegenerated=true;
			Iterator<Object[]> itr2=falsenoList.iterator();
			while(itr2.hasNext()){
				Object[] obj = (Object[]) itr2.next();
			if(obj[0]!=null)
			to.setFalseNo(obj[0].toString());
			if(obj[1]!=null)
			to.setFalseNoId(obj[1].toString());
			}
		}
		ExamMarkEvaluationTo evalTO=null;
		ExamMarkEvaluationBo evalbo=FalseExamMarksEntryTransactionImpl.getInstance().getEvalBo(to.getFalseNo());
		if (evalbo!=null) {
			evalTO=FalseExamMarksEntryHelper.getInstance().convertEvalBoDataToMarksMap(evalbo);
		}else{
			evalTO=new ExamMarkEvaluationTo();
			
		}
					to.setExamEvalTo(evalTO);
		
					if(existsMarks.containsKey(student.getId())){
						MarksDetailsTO detailTo=existsMarks.get(student.getId());
						to.setId(detailTo.getId());
						to.setPracticalMarks(detailTo.getPracticalMarks());
						to.setTheoryMarks(detailTo.getTheoryMarks());
						to.setOldPracticalMarks(detailTo.getPracticalMarks());
						to.setOldTheoryMarks(detailTo.getTheoryMarks());
						to.setClassId(detailTo.getClassId());
						to.setMarksId(detailTo.getMarksId());
						if(detailTo.isTheorySecured() || disable)
							to.setIsTheorySecured(true);
						else
							to.setIsTheorySecured(false);
						if(detailTo.isPracticalSecured() || disable)
							to.setIsPracticalSecured(true);
						else
							to.setIsPracticalSecured(false);
					}else{
						if (evalTO!=null && evalTO.getThirdEvaluation()==null 
								&& evalTO.getFirstEvaluation()!=null && evalTO.getSecondEvaluation()!=null) {
							//enter mark
							ExamFalseNumberGen falsBo=FalseExamMarksEntryTransactionImpl.getInstance().getDetailsByFalsenum(to.getFalseNo());
							if (falsBo.getSubject().getIsTheoryPractical()=="T" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("T")) {
								to.setTheoryMarks(String.valueOf(Math.round((new Double(evalTO.getFirstEvaluation())+new Double(evalTO.getSecondEvaluation()))/2)));
								
							}else if (falsBo.getSubject().getIsTheoryPractical()=="P" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("P")) {
								to.setPracticalMarks(String.valueOf(Math.round((new Double(evalTO.getFirstEvaluation())+new Double(evalTO.getSecondEvaluation()))/2)));
								
							}
							
						}else if (evalTO!=null && evalTO.getThirdEvaluation()!=null 
								&& evalTO.getFirstEvaluation()!=null && evalTO.getSecondEvaluation()!=null) {
							//enter mark
							ExamFalseNumberGen falsBo=FalseExamMarksEntryTransactionImpl.getInstance().getDetailsByFalsenum(to.getFalseNo());
							if (falsBo.getSubject().getIsTheoryPractical()=="T" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("T")) {
								int firstDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getFirstEvaluation());
								if (firstDiff<0) {
									firstDiff=firstDiff*(-1);
								}
								
								int secondDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getSecondEvaluation());
								if (secondDiff<0) {
									secondDiff=secondDiff*(-1);
								}
								if (firstDiff == secondDiff) {
									String finalMark=findTwoMaxNumbers(Integer.parseInt(evalTO.getFirstEvaluation()), Integer.parseInt(evalTO.getSecondEvaluation()), Integer.parseInt(evalTO.getThirdEvaluation()));
									to.setTheoryMarks(finalMark);
								}
								else if (firstDiff < secondDiff) {
									Double result=new Double(0);
									result=((new Double(evalTO.getFirstEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}else{
									Double result=new Double(0);
									result=((new Double(evalTO.getSecondEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}
								
								
							}else if (falsBo.getSubject().getIsTheoryPractical()=="P" 
									|| falsBo.getSubject().getIsTheoryPractical().equalsIgnoreCase("P")) {
								int firstDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getFirstEvaluation());
								if (firstDiff<0) {
									firstDiff=firstDiff*(-1);
								}
								
								int secondDiff=Integer.parseInt(evalTO.getThirdEvaluation())-Integer.parseInt(evalTO.getSecondEvaluation());
								if (secondDiff<0) {
									secondDiff=secondDiff*(-1);
								}
								if (firstDiff == secondDiff) {
									String finalMark=findTwoMaxNumbers(Integer.parseInt(evalTO.getFirstEvaluation()), Integer.parseInt(evalTO.getSecondEvaluation()), Integer.parseInt(evalTO.getThirdEvaluation()));
									to.setPracticalMarks(finalMark);
								}
								else if (firstDiff<secondDiff) {
									Double result=new Double(0);
									result=((new Double(evalTO.getFirstEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}else{
									Double result=new Double(0);
									result=((new Double(evalTO.getSecondEvaluation()))+(new Double(evalTO.getThirdEvaluation())))/2;
									String finalMark=String.valueOf(Math.round(result));
									to.setTheoryMarks(finalMark);
								}
								
								
								
							}
							
						}
						to.setIsTheorySecured(false);
						to.setIsPracticalSecured(false);
						if(objects[1]!=null){
							to.setClassId(Integer.parseInt(objects[1].toString()));
						}
					}
					if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("1")){
						to.setIsTheory(true);
						to.setIsPractical(false);
					}else if(newExamMarksEntryForm.getSubjectType()!=null && newExamMarksEntryForm.getSubjectType().equalsIgnoreCase("0")){
						to.setIsTheory(false);
						to.setIsPractical(true);
					}else{
						to.setIsTheory(true);
						to.setIsPractical(true);
					}
					studentList.add(to);
				}
			}
		}
		newExamMarksEntryForm.setIsfalsegenerated(isfalsegenerated);
		return studentList;
	}
	 public String findTwoMaxNumbers(int first , int second, int third){
		 int array[] = {first,second,third};
         int maxOne = 0;
         String finalMark=null;
         Double result=new Double(0);
         int maxTwo = 0;
         for(int num : array){
             if(maxOne < num){
                 maxTwo = maxOne;
                 maxOne =num;
             } else if(maxTwo < num){
                 maxTwo = num;
             }
         }
         result=((new Double(maxOne))+(new Double(maxTwo)))/2;
         finalMark=String.valueOf(Math.round(result));
		return finalMark;
     }
	
}