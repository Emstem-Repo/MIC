package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ConsolidateMarksCard;
import com.kp.cms.bo.exam.CourseSchemeDetails;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamStudentCCPA;
import com.kp.cms.bo.exam.ExamStudentSgpa;
import com.kp.cms.bo.exam.SemesterWiseExamResults;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.exam.ExamStudentSGPAForm;
import com.kp.cms.helpers.exam.AdminMarksCardHelper;
import com.kp.cms.helpers.exam.ConsolidatedMarksCardHelper;
import com.kp.cms.helpers.exam.DownloadHallTicketHelper;
import com.kp.cms.helpers.exam.ExamUpdateProcessHelper;
import com.kp.cms.to.admin.SubjectTO;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.transactions.exam.IConsolidatedMarksCardTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.IUpdateStudentSGPATxn;
import com.kp.cms.transactionsimpl.exam.ConsolidatedMarksCardTransactionImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.ExamUpdateProcessImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.UpdateExamStudentSGPAImpl;
import com.kp.cms.utilities.CommonUtil;

@SuppressWarnings("unchecked")
public class UpdateStudentSGPAHandler {
	private static final Log log = LogFactory.getLog(UpdateStudentSGPAHandler.class);
	public static volatile UpdateStudentSGPAHandler updateStudentSGPAHandler = null;
	ExamUpdateProcessImpl updateImpl = new ExamUpdateProcessImpl();
	ExamUpdateProcessHandler updateHandler = new ExamUpdateProcessHandler();
	ExamUpdateProcessHelper updateHelper = new ExamUpdateProcessHelper();

	public static UpdateStudentSGPAHandler getInstance() {
		if (updateStudentSGPAHandler == null) {
			updateStudentSGPAHandler = new UpdateStudentSGPAHandler();
			return updateStudentSGPAHandler;
		}
		return updateStudentSGPAHandler;
	}

	/**
	 * 
	 * @param courseId
	 * @param schemeNo
	 * @param academicYear
	 * @return
	 * @throws Exception
	 */
	
	public boolean updateSGPA(int courseId, int schemeNo, Integer academicYear, String programType, String programId, String userId) throws Exception {		log.debug("getClassIdsByCourseAndSchemeNo");
		log.debug("getClassIdsByCourseAndSchemeNo");
		IUpdateStudentSGPATxn iUpdateStudentSGPATxn = UpdateExamStudentSGPAImpl.getInstance();
		Integer classId=0;
		String appliedYear = null;
		boolean isUpdated = false;
		HttpServletRequest request=null;
		ArrayList<Integer> classIdList = iUpdateStudentSGPATxn.getClassIdsByCourseAndScheme(courseId, schemeNo, academicYear);
		if(classIdList.size()>0){
			classId = classIdList.get(0);
		}
		if(classId>0){
			ArrayList<Integer> studentList = new ArrayList<Integer>() ;
			Map<Integer,Map<String, ConsolidateMarksCard>> boMap = null;
			String query ="";
			List<Integer> studentIds=new ArrayList<Integer>();
			ArrayList<ExamStudentSgpa> studentSGPAList = new ArrayList<ExamStudentSgpa>();
			ArrayList<SemesterWiseExamResults> studentSGPAListForSemesterResultslist = new ArrayList<SemesterWiseExamResults>(); 
			IDownloadHallTicketTransaction transaction1= DownloadHallTicketTransactionImpl.getInstance();
			INewExamMarksEntryTransaction txn = NewExamMarksEntryTransactionImpl.getInstance();
			Map<Integer,byte[]> photoMap=txn.getStudentPhtosMap(studentIds,false);
	
			String qry = "from CourseSchemeDetails c where c.program.id = "+programId+" " +
			"and c.schemeNo = "+schemeNo+" and c.course.id= "+courseId+" and c.isActive=1 and c.examDefinition.delIsActive = 1 and c.examDefinition.examType.id = 1 and c.examDefinition.academicYear = "+academicYear+" ";
			List examIDList = txn.getDataForQueryClasses(qry);
			int examId = 0;
			int year =0;
			int month =0;
			Iterator examIdItr = examIDList.iterator();
			while(examIdItr.hasNext()) {
				CourseSchemeDetails examDef = (CourseSchemeDetails) examIdItr.next();
				examId = examDef.getExamDefinition().getId();
				year = examDef.getExamDefinition().getYear();
				month = Integer.parseInt(examDef.getExamDefinition().getMonth());
				MarksCardTO mto = new MarksCardTO();
				Map<Integer, ExamStudentSgpa> sgpaMap =null;
				ExamStudentSgpa examStudentSgpa = null;
				if(examId>0) {
		
					String previousQuery="select s.student.id from StudentPreviousClassHistory s where " +
										 "(s.student.isHide = 0 or s.student.isHide is null) and s.student.id not in (select e.student.id from ExamStudentDetentionRejoinDetails e where e.discontinued=1 ) and s.student.admAppln.isCancelled=0" +
										 " and s.classes.id="+classId+
										 " group by s.student.id ";
					List previousList= txn.getDataForQuery(previousQuery);
					if(previousList!=null && !previousList.isEmpty()){
						studentList.addAll(previousList);
					}
					String currentStudentsQuery="select s.id from Student s where (s.isHide = 0 or s.isHide is null) and s.id not in (select e.student.id from ExamStudentDetentionRejoinDetails e where e.discontinued=1 ) and s.admAppln.isCancelled=0 " +
					"and s.classSchemewise.classes.id="+classId; 
					List list=txn.getDataForQuery(currentStudentsQuery);
					if(list!=null && !list.isEmpty()){
						studentList.addAll(list);
					}
					//studentList.add(443);
					iUpdateStudentSGPATxn.deleleAlreadyExistedRecords(classId, schemeNo);
					if(studentList!= null && studentList.size() > 0){
						for (Integer stuId : studentList) {
							//if(stuId == 16370){
							SemesterWiseExamResults semResults=null;
							mto = new MarksCardTO();
							String appliedYears=AdminMarksCardHelper.getInstance().getCurrentClassStudentsYearQuery(stuId);
							List list1=txn.getDataForQuery(appliedYears);
							appliedYear=list1.get(0).toString();
							if(programType.equalsIgnoreCase("PG")){
								mto = null;
								query=DownloadHallTicketHelper.getInstance().getQueryForPGStudentMarksCard(examId,classId,stuId,schemeNo);
								List<Object[]> pgMarksCardData=transaction1.getStudentHallTicket(query);
								if(pgMarksCardData.size()>0)
									mto= getPGMarksCardResult(pgMarksCardData,stuId,photoMap,request,new HashMap<Integer,String>());
								if(mto!=null){
									semResults = new SemesterWiseExamResults();
									Classes classes = new Classes();
									classes.setId(classId);
									semResults.setClasses(classes);
		
									ExamDefinitionBO examDefinitionBO = new ExamDefinitionBO();
									examDefinitionBO.setId(examId);
									semResults.setExamDef(examDefinitionBO);
		
									Student studentUtilBO = new Student();
									studentUtilBO.setId(stuId);
									semResults.setStudent(studentUtilBO);
									semResults.setAcademicYear(academicYear);
									semResults.setSemNo(String.valueOf(schemeNo));
									if(mto.getResult()!=null){
										semResults.setResult(mto.getResult());
									}
									if(mto.getGradeForSgpa()!=null){
										if(!mto.getGradeForSgpa().trim().equalsIgnoreCase("") )
											semResults.setOverallGrade(mto.getGradeForSgpa());
									}
									if(mto.getTotalGradePointsForSgpa()!=null)
										semResults.setTotalGradePoints(Double.parseDouble(mto.getTotalGradePointsForSgpa()));
									if(mto.getTotalCreditsForSgpa()!=null)
										semResults.setTotalCretits(Double.parseDouble(mto.getTotalCreditsForSgpa()));
									if(mto.getTotalMarksAwarded()!=null)
										semResults.setSgpa(Double.parseDouble(mto.getTotalMarksAwarded()));								
									if(mto.getTotalMaxmarks()!=null)
										semResults.setTotalMaxMarks(Double.parseDouble(mto.getTotalMaxmarks()));
									semResults.setModifiedBy(userId);
									semResults.setLastModifiedDate(new Date());
									semResults.setCreatedBy(userId);
									semResults.setCreatedDate(new Date());
		
									studentSGPAListForSemesterResultslist.add(semResults);
								}
								// for supplimentary marks card
								if(mto!=null){
									String supMarksCardQuery=DownloadHallTicketHelper.getInstance().getSupplMarksCardQueryForSGPA(schemeNo);
									if(boMap == null) {
										IConsolidatedMarksCardTransaction transaction2= ConsolidatedMarksCardTransactionImpl.getInstance();
										List<Object[]> studlist=transaction2.getStudentMarks(supMarksCardQuery,studentList);
										String certificateCourseQuery="select subject_id,is_optional,scheme_no,student_id from student_certificate_course  where is_cancelled=0 and  student_id in (:ids)";
										List certificateList=transaction2.getStudentMarks(certificateCourseQuery,studentList);
										Map<Integer,Map<Integer,Map<Integer,Boolean>>> certificateMap=getCertificateSubjectMap(certificateList);
										List<String> appearedList=transaction2.getSupplimentaryAppeared(studentList);
										boMap=ConsolidatedMarksCardHelper.getInstance().getBoListForInputSupplementary(studlist,userId,certificateMap,appearedList,String.valueOf(courseId));
									}
									if(boMap!=null && boMap.size()>0) {
										if(sgpaMap==null) {
											sgpaMap = calculatesgpaForPGCourses(boMap,schemeNo,examId,classId,courseId);
										}
									}
									examStudentSgpa = null ;
									if(sgpaMap!=null){
										if( sgpaMap.containsKey(stuId))
											examStudentSgpa = sgpaMap.get(stuId);
										if(examStudentSgpa!=null){
											examStudentSgpa.setAppliedYear(Integer.parseInt(appliedYear));
										}
									}
			
									if(examStudentSgpa!=null )
										studentSGPAList.add(examStudentSgpa);
								}
							}
							else{
								mto = null;
								examStudentSgpa = null;
								String marksCardQuery=DownloadHallTicketHelper.getInstance().getQueryForUGStudentMarksCard(examId,classId,schemeNo,stuId);
								List<Object[]> ugMarksCardData=transaction1.getStudentHallTicket(marksCardQuery);
								mto = getUGMarksCardResult(ugMarksCardData,schemeNo,stuId,photoMap,request,new HashMap<Integer, String>(),courseId,appliedYear);
								if(mto!=null) {
									semResults = new SemesterWiseExamResults();
									Classes classes = new Classes();
									classes.setId(classId);
									semResults.setClasses(classes);
		
									ExamDefinitionBO examDefinitionBO = new ExamDefinitionBO();
									examDefinitionBO.setId(examId);
									semResults.setExamDef(examDefinitionBO);
		
									Student studentUtilBO = new Student();
									studentUtilBO.setId(stuId);
									semResults.setStudent(studentUtilBO);
									semResults.setAcademicYear(academicYear);
									semResults.setSemNo(String.valueOf(schemeNo));
									if(!mto.getResultClass().trim().equalsIgnoreCase("") && 
									   !mto.getResultClass().trim().equalsIgnoreCase("-") &&
									   mto.getResultClass()!=null)
										semResults.setResult(mto.getResultClass());
									if(mto.getTotalGrade()!=null && !mto.getTotalGrade().trim().equalsIgnoreCase(""))
										semResults.setOverallGrade(mto.getTotalGrade());
		
									if(mto.getSgpa()!=null && !mto.getSgpa().trim().equalsIgnoreCase(""))
										semResults.setSgpa(Double.parseDouble(mto.getSgpa()));
									if(mto.getTotalCredits()!=null && !mto.getTotalCredits().trim().equalsIgnoreCase(""))
										semResults.setTotalCretits(Double.parseDouble(mto.getTotalCredits()));
		
									if(mto.getTotalGradePoints()!=null && !mto.getTotalGradePoints().trim().equalsIgnoreCase(""))
										semResults.setTotalGradePoints(Double.parseDouble(mto.getTotalGradePoints()));
									if(mto.getTotalMarksAwarded()!=null)
										semResults.setTotalMarksAwarded(Double.parseDouble(mto.getTotalMarksAwarded()));
									if(mto.getTotalMaxmarks()!=null)
										semResults.setTotalMaxMarks(Double.parseDouble(mto.getTotalMaxmarks()));
									semResults.setModifiedBy(userId);
									semResults.setLastModifiedDate(new Date());
									semResults.setCreatedBy(userId);
									semResults.setCreatedDate(new Date());
		
									studentSGPAListForSemesterResultslist.add(semResults);
									mto=null;
		
								}
		
								// for UG supplimentary marks card
								String supMarksCardQuery=DownloadHallTicketHelper.getInstance().getSupplMarksCardQueryForSGPA(schemeNo);
								if(boMap == null ){
									IConsolidatedMarksCardTransaction transaction2= ConsolidatedMarksCardTransactionImpl.getInstance();
									List<Object[]> studlist=transaction2.getStudentMarks(supMarksCardQuery,studentList);
									String certificateCourseQuery="select subject_id,is_optional,scheme_no,student_id from student_certificate_course  where is_cancelled=0 and  student_id in (:ids)";
									List certificateList=transaction2.getStudentMarks(certificateCourseQuery,studentList);
									Map<Integer,Map<Integer,Map<Integer,Boolean>>> certificateMap=getCertificateSubjectMap(certificateList);
									List<String> appearedList=transaction2.getSupplimentaryAppeared(studentList);
									boMap=ConsolidatedMarksCardHelper.getInstance().getBoListForInputSupplementary(studlist,userId,certificateMap,appearedList,String.valueOf(courseId));
								}
								
								if(boMap!=null && boMap.size()>0){
									if(sgpaMap==null){
										sgpaMap = calculateSgpaForUGCourse(boMap,schemeNo,examId,classId,courseId,year,month);
									}
									if(sgpaMap!=null){
										if(sgpaMap.containsKey(stuId))
											examStudentSgpa = sgpaMap.get(stuId);
										if(examStudentSgpa!=null)
											examStudentSgpa.setAppliedYear(Integer.parseInt(appliedYear));
									}
		
								}
								if(examStudentSgpa!=null )
									studentSGPAList.add(examStudentSgpa);
							}
						//}//check for student
						}
					}
		
					isUpdated = false;
					if(studentSGPAListForSemesterResultslist!=null && studentSGPAListForSemesterResultslist.size()>0)
						isUpdated = iUpdateStudentSGPATxn.saveSemesterWiseMarksDetails(studentSGPAListForSemesterResultslist);
					if(studentSGPAList!= null && studentSGPAList.size()>0){
						isUpdated = iUpdateStudentSGPATxn.updateSgpa(studentSGPAList);
					}
				}
			}
		}
		return isUpdated;
	}

	private Map<Integer, Map<Integer, Map<Integer, Boolean>>> getCertificateSubjectMap( List certificateList)  throws Exception{
		Map<Integer,Map<Integer,Map<Integer,Boolean>>> map=new HashMap<Integer,Map<Integer, Map<Integer,Boolean>>>();
		Map<Integer,Map<Integer,Boolean>> outerMap;
		Map<Integer,Boolean> innerMap;
		if(certificateList!=null && !certificateList.isEmpty()){
			Iterator<Object[]> itr=certificateList.iterator();
			while (itr.hasNext()) {
				Object[] objects = (Object[]) itr.next();
				if(objects[0]!=null && objects[1]!=null && objects[2]!=null  && objects[3]!=null){
					if(map.containsKey(Integer.parseInt(objects[3].toString())))
						outerMap=map.get(Integer.parseInt(objects[3].toString()));
					else
						outerMap=new HashMap<Integer, Map<Integer,Boolean>>();


					if(outerMap.containsKey(Integer.parseInt(objects[2].toString()))){
						innerMap=outerMap.remove(Integer.parseInt(objects[2].toString()));
					}else
						innerMap=new HashMap<Integer, Boolean>();

					innerMap.put(Integer.parseInt(objects[0].toString()),(Boolean)objects[1]);
					outerMap.put(Integer.parseInt(objects[2].toString()),innerMap);
					map.put(Integer.parseInt(objects[3].toString()), outerMap);
				}
			}
		}
		return map;
	}	

	private Map<Integer, ExamStudentSgpa> calculatesgpaForPGCourses(Map<Integer,Map<String, ConsolidateMarksCard>> boMap, int schemeNo,int examId, int classId, int courseId) throws Exception{
		int  studentIdFromBoMap ;

		Map<String, ConsolidateMarksCard> conSemMap;
		Map<Integer, ExamStudentSgpa> examStudentSgpaMap = new HashMap<Integer, ExamStudentSgpa>();
		ConsolidateMarksCard consolidateMarksCard;
		String examMonthYear = null;
		String[] examMonthYearArr = new String[2];
		Iterator entries = boMap.entrySet().iterator();
		Map<Integer, String> examIdMap = new HashMap<Integer,String>();
		while (entries.hasNext()) {

			boolean subfailed=false;
			double totalMarksAwarded=0;
			double theoryEseMinMark = 0;
			int totalMaxMarks = 0;
			double practicalESATotalObtainedMarks=0;
			double practicalTotalObtainedMarks=0;
			double practicalMaxESATotalmarks=0;
			double practicalMaxTotalmarks=0;

			Entry mainMap = (Entry) entries.next();
			studentIdFromBoMap = (Integer) mainMap.getKey();
			conSemMap = (Map<String, ConsolidateMarksCard>) mainMap.getValue();
			Iterator conSemMapEntry = conSemMap.entrySet().iterator();
			examIdMap = new HashMap<Integer,String>();
			while(conSemMapEntry.hasNext()){
				double externalmark=0;
				double subFinalMin=0;
				double subTotalMarksAwarded=0;
				examMonthYear = null;
				examMonthYearArr = new String[2];
				Entry semMap = (Entry) conSemMapEntry.next();
				consolidateMarksCard= (ConsolidateMarksCard) semMap.getValue();
				if(consolidateMarksCard.getSection()!=null){
				if(consolidateMarksCard.getIsTheoryPractical().equalsIgnoreCase("t")){
					
					if(!examIdMap.containsKey(consolidateMarksCard.getStudent().getId())){
						examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
						examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
					}
					else{
						examMonthYear = examIdMap.get(consolidateMarksCard.getStudent().getId());
						examMonthYearArr = examMonthYear.split("_");
						int maxExamId = Integer.parseInt(examMonthYearArr[0]);
						if(consolidateMarksCard.getExam().getId()> maxExamId){
							examIdMap.remove(examMonthYear);
							examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
							examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
						}
					}
					
					
					if(consolidateMarksCard.getTheoryMax()!=null)
						totalMaxMarks =  totalMaxMarks + consolidateMarksCard.getTheoryMax().intValue();
					subTotalMarksAwarded = consolidateMarksCard.getTheoryObtain();
					totalMarksAwarded = (totalMarksAwarded + consolidateMarksCard.getTheoryObtain());
					if((consolidateMarksCard.getStudentTheoryMark()!=null && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("C") && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR") &&
							!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase(" ")){
							externalmark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
					}
					else{
						subfailed=true;
					}
					if(consolidateMarksCard.getTheoryeseMinimumMark()!=null)
						theoryEseMinMark = consolidateMarksCard.getTheoryeseMinimumMark().doubleValue();
					if(consolidateMarksCard.getSubjectFinalMin()!=null)
						subFinalMin = consolidateMarksCard.getSubjectFinalMin().doubleValue();

					if(externalmark<theoryEseMinMark || subTotalMarksAwarded<subFinalMin){
						subfailed=true;
					}

				}			

				else if(consolidateMarksCard.getIsTheoryPractical().equalsIgnoreCase("P")){
					
					if(!examIdMap.containsKey(consolidateMarksCard.getStudent().getId())){
						examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
						examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
					}
					else{
						examMonthYear = examIdMap.get(consolidateMarksCard.getStudent().getId());
						examMonthYearArr = examMonthYear.split("_");
						int maxExamId = Integer.parseInt(examMonthYearArr[0]);
						if(consolidateMarksCard.getExam().getId()> maxExamId){
							examIdMap.remove(examMonthYear);
							examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
							examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
						}
					}
					
					if(consolidateMarksCard.getPracticalMax()!=null)
						totalMaxMarks =  totalMaxMarks + consolidateMarksCard.getPracticalMax().intValue();
					subTotalMarksAwarded = consolidateMarksCard.getPracticalObtain();
					if((consolidateMarksCard.getStudentPracticalMark()!=null && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("AB")) && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("C") && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("NR")
							&& !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase(" ")){
							externalmark = Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());

					}
					else{
						subfailed=true;
					}
					totalMarksAwarded = (totalMarksAwarded + consolidateMarksCard.getPracticalObtain());
					practicalTotalObtainedMarks = practicalTotalObtainedMarks + consolidateMarksCard.getPracticalObtain();
					practicalESATotalObtainedMarks = practicalESATotalObtainedMarks +externalmark;
					if(consolidateMarksCard.getPracticaleseMaximumMark()!=null)
						practicalMaxESATotalmarks = practicalMaxESATotalmarks + consolidateMarksCard.getPracticaleseMaximumMark().doubleValue();
					if(consolidateMarksCard.getPracticalMax()!=null)
						practicalMaxTotalmarks = practicalMaxTotalmarks + consolidateMarksCard.getPracticalMax().doubleValue();
					if(consolidateMarksCard.getSubjectFinalMin()!=null)
						subFinalMin = consolidateMarksCard.getSubjectFinalMin().doubleValue();

					if(subTotalMarksAwarded<subFinalMin){	
						subfailed=true;

					}


				}
				// For MTA Course having BOTH subject type..
				else{
					
					if(!examIdMap.containsKey(consolidateMarksCard.getStudent().getId())){
						examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
						examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
					}
					else{
						examMonthYear = examIdMap.get(consolidateMarksCard.getStudent().getId());
						examMonthYearArr = examMonthYear.split("_");
						int maxExamId = Integer.parseInt(examMonthYearArr[0]);
						if(consolidateMarksCard.getExam().getId()> maxExamId){
							examIdMap.remove(examMonthYear);
							examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
							examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
						}
					}
					
					// for theory subject type
					double intMark=0;
					if(consolidateMarksCard.getTheoryMax()!=null)
						totalMaxMarks =  totalMaxMarks + consolidateMarksCard.getTheoryMax().intValue();
					if((consolidateMarksCard.getStudentTheoryMark()!=null && 
						!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) && 
						!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("C") && 
						!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR")
							&& !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase(" ")){
							externalmark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
					}
					else{
						subfailed=true;
					}
					if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null)
						intMark = Double.parseDouble(consolidateMarksCard.getTheoryTotalSubInternalMark());
					subTotalMarksAwarded = externalmark+intMark;
					totalMarksAwarded = totalMarksAwarded + externalmark+intMark;

					if(consolidateMarksCard.getTheoryeseMinimumMark()!=null)
						theoryEseMinMark = consolidateMarksCard.getTheoryeseMinimumMark().doubleValue();
					if(consolidateMarksCard.getSubjectFinalMin()!=null)
						subFinalMin = consolidateMarksCard.getSubjectFinalMin().doubleValue();



					// for practical subject type
					intMark =0;
					if(consolidateMarksCard.getPracticalMax()!=null)
						totalMaxMarks =  totalMaxMarks + consolidateMarksCard.getPracticalMax().intValue();
					if((!String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("AB")) && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("C") && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("NR")
							&& !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase(" ")){

						if(consolidateMarksCard.getStudentPracticalMark()!=null)
							externalmark = Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());
					}
					else{
						subfailed=true;
					}
					if(consolidateMarksCard.getPracticalTotalSubInternalMark()!=null)
						intMark = Double.parseDouble(consolidateMarksCard.getPracticalTotalSubInternalMark());
					practicalTotalObtainedMarks = practicalTotalObtainedMarks +subTotalMarksAwarded;
					practicalESATotalObtainedMarks = practicalESATotalObtainedMarks +externalmark;
					if(consolidateMarksCard.getPracticaleseMaximumMark()!=null)
						practicalMaxESATotalmarks = practicalMaxESATotalmarks + consolidateMarksCard.getPracticaleseMaximumMark().doubleValue();
					if(consolidateMarksCard.getPracticalMax()!=null)
						practicalMaxTotalmarks = practicalMaxTotalmarks + consolidateMarksCard.getPracticalMax().doubleValue();
					if(consolidateMarksCard.getSubjectFinalMin()!=null)
						subFinalMin = consolidateMarksCard.getSubjectFinalMin().doubleValue();

					if(subTotalMarksAwarded<subFinalMin){	
						subfailed=true;
					}
				}
				}
			}

			if(practicalMaxESATotalmarks!=0)
			{
				if(((practicalESATotalObtainedMarks*100)/practicalMaxESATotalmarks) < 30)
					subfailed=true;
			}
			if(practicalMaxTotalmarks!=0)
			{
				if(((practicalTotalObtainedMarks*100)/practicalMaxTotalmarks) < 40)
					subfailed=true;
			}

			ExamStudentSgpa examStudentSgpa = new ExamStudentSgpa();
			Student student = new Student();
			Classes classes = new Classes();
			Course course = new Course();
			course.setId(courseId);
			examStudentSgpa.setCourse(course);
			student.setId(studentIdFromBoMap);
			examStudentSgpa.setStudent(student);
			classes.setId(classId);
			examStudentSgpa.setClasses(classes);
			examStudentSgpa.setSchemeNo(schemeNo);
			/*if(grade!=null)
				examStudentSgpa.setGrade(grade);*/
			
			examMonthYear = examIdMap.get(studentIdFromBoMap);
			examMonthYearArr = examMonthYear.split("_");
			
			String month = examMonthYearArr[2];
			if(month.length()<=1)
				month =	"0"+month;
			
			examStudentSgpa.setYear(Integer.parseInt(examMonthYearArr[1]));
			examStudentSgpa.setMonth(month);
			
			if(subfailed)
				examStudentSgpa.setResult("Failed");
			else
				examStudentSgpa.setResult("Passed");
			examStudentSgpa.setTotalMaxMarks(totalMaxMarks);
			examStudentSgpa.setSgpa(totalMarksAwarded);
			examStudentSgpaMap.put(studentIdFromBoMap, examStudentSgpa);

		}
		return examStudentSgpaMap;
	}

	public MarksCardTO getPGMarksCardResult(List<Object[]> pgMarksCardData,int sid,Map<Integer,byte[]> studentPhotos,HttpServletRequest request,Map<Integer,String> revaluationSubjects) throws Exception {
		MarksCardTO to=null;
		String resultClass="Pass";
		if(pgMarksCardData!=null && !pgMarksCardData.isEmpty()){
			double totalMarksAwarded=0;
			double totalMaxMarks=0;
			double practicalESATotalmarks=0;
			double practicalTotalObtainedMarks=0;
			double practicalMaxESATotalmarks=0;
			double practicalMaxTotalmarks=0;
			// Getting Grade for Fail from database

			Iterator<Object[]> itr=pgMarksCardData.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				to=new MarksCardTO();
				if(obj[20]!=null){
				if(obj[51]!=null){
					if(obj[51].toString().equalsIgnoreCase("t")){

						if(obj[26]!=null){
							totalMaxMarks=totalMaxMarks+Double.parseDouble(obj[26].toString());
						}
						if(obj[28]!=null && CommonUtil.isValidDecimal(obj[28].toString())){
							totalMarksAwarded=totalMarksAwarded+Double.parseDouble(obj[28].toString());

						}

						if(obj[57]!=null){
							double subFinalMin=0;
							double subFinalObt=0;
							if(obj[57]!=null && CommonUtil.isValidDecimal(obj[57].toString()))
								subFinalObt=Double.parseDouble(obj[57].toString());
							if(obj[56]!=null && CommonUtil.isValidDecimal(obj[56].toString()))
								subFinalMin=Double.parseDouble(obj[56].toString());
							if(subFinalMin>subFinalObt)
							{
								resultClass="Fail";
							}

						}

					}
					if(obj[51].toString().equalsIgnoreCase("p")){

						if(obj[27]!=null){
							totalMaxMarks=totalMaxMarks+Double.parseDouble(obj[27].toString());
						}
						if(obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString())){
							totalMarksAwarded=totalMarksAwarded+Double.parseDouble(obj[29].toString());
						}
						if(obj[16]!=null && !obj[16].toString().equalsIgnoreCase("AB") && !obj[16].toString().equalsIgnoreCase("NR") && !obj[16].toString().equalsIgnoreCase("MP") && !obj[16].toString().equalsIgnoreCase("C"))
							practicalESATotalmarks = practicalESATotalmarks + Double.parseDouble(obj[16].toString());
						if(obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString()))
							practicalTotalObtainedMarks = practicalTotalObtainedMarks + Double.parseDouble(obj[29].toString());
						if(obj[14]!=null && CommonUtil.isValidDecimal(obj[14].toString()))
							practicalMaxESATotalmarks = practicalMaxESATotalmarks + Double.parseDouble(obj[14].toString());
						if(obj[27]!=null && CommonUtil.isValidDecimal(obj[27].toString()))
							practicalMaxTotalmarks = practicalMaxTotalmarks + Double.parseDouble(obj[27].toString());


						if(obj[57]!=null){
							double subFinalMin=0;
							double subFinalObt=0;
							if(obj[57]!=null && CommonUtil.isValidDecimal(obj[57].toString()))
								subFinalObt=Double.parseDouble(obj[57].toString());
							if(obj[56]!=null && CommonUtil.isValidDecimal(obj[56].toString()))
								subFinalMin=Double.parseDouble(obj[56].toString());
							if(subFinalMin>subFinalObt)
							{
								resultClass="Fail";
							}

						}

					}
					// both
					if(obj[51].toString().equalsIgnoreCase("b")){

						// subject theory

						if(obj[26]!=null){
							totalMaxMarks=totalMaxMarks+Double.parseDouble(obj[26].toString());
						}
						if(obj[28]!=null && CommonUtil.isValidDecimal(obj[28].toString())){
							totalMarksAwarded=totalMarksAwarded+Double.parseDouble(obj[28].toString());
						}

						// subject practical

						if(obj[27]!=null){
							totalMaxMarks=totalMaxMarks+Double.parseDouble(obj[27].toString());
						}
						if(obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString())){
							totalMarksAwarded=totalMarksAwarded+Double.parseDouble(obj[29].toString());
						}
						if(obj[16]!=null && !obj[16].toString().equalsIgnoreCase("AB")  && !obj[16].toString().equalsIgnoreCase("MP") && !obj[16].toString().equalsIgnoreCase("C") && !obj[16].toString().equalsIgnoreCase("NR") && CommonUtil.isValidDecimal(obj[16].toString()))
							practicalESATotalmarks = practicalESATotalmarks + Double.parseDouble(obj[16].toString());
						if(obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString()))
							practicalTotalObtainedMarks = practicalTotalObtainedMarks + Double.parseDouble(obj[29].toString());
						if(obj[14]!=null)
							practicalMaxESATotalmarks = practicalMaxESATotalmarks + Double.parseDouble(obj[14].toString());
						if(obj[27]!=null && CommonUtil.isValidDecimal(obj[27].toString()))
							practicalMaxTotalmarks = practicalMaxTotalmarks + Double.parseDouble(obj[27].toString());

						if(obj[57]!=null){
							double subFinalMin=0;
							double subFinalObt=0;
							if(obj[57]!=null && CommonUtil.isValidDecimal(obj[57].toString()))
								subFinalObt=Double.parseDouble(obj[57].toString());
							if(obj[56]!=null && CommonUtil.isValidDecimal(obj[56].toString()))
								subFinalMin=Double.parseDouble(obj[56].toString());
							if(subFinalMin>subFinalObt)
							{
								resultClass="Fail";
							}

						}

					}
				}
			}
			}
				
			to.setTotalMarksAwarded(String.valueOf(totalMarksAwarded));
			to.setTotalMaxmarks(String.valueOf(totalMaxMarks));
			if(resultClass.equalsIgnoreCase("Fail")){
				to.setResult("Fail");
			}else{
				to.setResult("Pass");
			}

		}


		return to;
	}	
	public boolean updateCCPA(ExamStudentSGPAForm examStudentSGPAForm ,int courseId, Integer batchYear,String programType, String programId, String userId) throws Exception {
		log.debug("getClassIdsByCourseAndSchemeNo");
		IUpdateStudentSGPATxn iUpdateStudentSGPATxn = UpdateExamStudentSGPAImpl.getInstance();
		boolean isUpdated = false;
		ArrayList<Integer> classIdList = iUpdateStudentSGPATxn.getClassIdsByCourse(courseId, batchYear);
		if(classIdList.size()>0){
			double totalCreditPoints = 0;
			double totalCredits=0;
			double totalMarksAwarded = 0;
			double totalMaxMarks = 0;
			Integer count =0;
	
			ExamStudentSgpa examStudentSgpa = null;
			ExamStudentCCPA examStudentCCPA = new ExamStudentCCPA();
			Integer stuId=0;
			ArrayList<ExamStudentCCPA> examStudentCCPAList = new ArrayList<ExamStudentCCPA>();
			ArrayList<ExamStudentSgpa> resultList = iUpdateStudentSGPATxn.getStudentSemesterCount(courseId,batchYear);
			ArrayList<ExamStudentSgpa> studentDetailsList = new ArrayList<ExamStudentSgpa>();
			Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
			Course course = new Course();
			course.setId(courseId);
			if(resultList!=null && resultList.size()>0) {
				IConsolidatedMarksCardTransaction consolCardTransaction = ConsolidatedMarksCardTransactionImpl.getInstance();
				Map<Integer, ConsolidateMarksCard> notRegisteredMap = consolCardTransaction.getNonRegisteredStudents(batchYear);
				Iterator itr = resultList.iterator();
				while(itr.hasNext()){
					Object[] obj =  (Object[]) itr.next();
					count =  Integer.parseInt(obj[0].toString());
					stuId = (Integer) obj[1];;
					if(!countMap.containsKey(stuId)){
						countMap.put(stuId, count);
					}
				}

				if(programType.equalsIgnoreCase("PG")){
					examStudentSgpa = new ExamStudentSgpa();
					Iterator countEntries = countMap.entrySet().iterator();
					while(countEntries.hasNext()){
						examStudentCCPA = new ExamStudentCCPA();
						examStudentSgpa = new ExamStudentSgpa();
						Entry countEntry = (Entry) countEntries.next();
						stuId = (Integer) countEntry.getKey();
						totalMarksAwarded = 0;
						totalMaxMarks = 0;
						String passMonth = "0";
						int passYear = 0;
						boolean isFailed = false;
						studentDetailsList = iUpdateStudentSGPATxn.getStudentResultDetails(stuId);
						if(studentDetailsList!=null && studentDetailsList.size()>0){
							Iterator listItr = studentDetailsList.iterator();
							while(listItr.hasNext()){
								examStudentSgpa = new ExamStudentSgpa();
								examStudentCCPA = new ExamStudentCCPA();
								examStudentSgpa = (ExamStudentSgpa) listItr.next();
								if(examStudentSgpa.getSgpa()!=null)
									totalMarksAwarded = totalMarksAwarded + examStudentSgpa.getSgpa();
								totalMaxMarks = totalMaxMarks + examStudentSgpa.getTotalMaxMarks();
								if(examStudentSgpa.getResult().equalsIgnoreCase("failed"))
									isFailed = true;
								if(examStudentSgpa.getYear()>0 && examStudentSgpa.getYear()>passYear){
									passYear = examStudentSgpa.getYear();
									passMonth = examStudentSgpa.getMonth();
								}
								else if(examStudentSgpa.getYear()==passYear){
									if(Integer.parseInt(examStudentSgpa.getMonth())>0 && Integer.parseInt(examStudentSgpa.getMonth())>= Integer.parseInt(passMonth)){
										passMonth = examStudentSgpa.getMonth();
									}
								}
							}
							examStudentCCPA.setTotalMaxMarks(totalMaxMarks);
							examStudentCCPA.setCcpa(totalMarksAwarded);
							examStudentCCPA.setCourse(course);
							Student student = new Student();
							student.setId(stuId);
							examStudentCCPA.setStudent(student);
							if(isFailed){
								examStudentCCPA.setResult("failed");
							}
							else{
								examStudentCCPA.setPassOutMonth(passMonth);
								examStudentCCPA.setPassOutYear(passYear);
								examStudentCCPA.setResult("passed");
							}

							examStudentCCPA.setAppliedYear(batchYear);
							examStudentCCPA.setModifiedBy(userId);
							examStudentCCPA.setLastModifiedDate(new Date());
							examStudentCCPA.setCreatedBy(userId);
							examStudentCCPA.setCreatedDate(new Date());
							if(examStudentCCPA!=null)
								examStudentCCPAList.add(examStudentCCPA);
						}
					}
				}
				else{
					examStudentSgpa = new ExamStudentSgpa();
					double ccpa =0;
					Iterator countEntries = countMap.entrySet().iterator();
					while(countEntries.hasNext()){
						examStudentCCPA = new ExamStudentCCPA();
						examStudentSgpa = new ExamStudentSgpa();
						Entry countEntry = (Entry) countEntries.next();
						stuId = (Integer) countEntry.getKey();
						totalCreditPoints = 0;
						totalCredits = 0;
						ccpa =0;
						int passYear=0;
						int passMonth=0;
						boolean isFailed = false;
						int subjectTypeBasedTotalCredits=0;
						double subjectTypeBasedCreditPoints=0;
						double subjectTypeBasedObtainedMarks = 0;
						double subjectTypeBasedAwardedMarks = 0;
						double subjectTypeBasedCCPA=0;
						totalMarksAwarded = 0;
						totalMaxMarks = 0;
						int creditsForDisplay=0;
						//if(stuId==504){
						studentDetailsList = iUpdateStudentSGPATxn.getStudentResultDetails(stuId);
						if(studentDetailsList!=null&& studentDetailsList.size()>0){
							Iterator listItr = studentDetailsList.iterator();
							while(listItr.hasNext()){
								examStudentSgpa = new ExamStudentSgpa();
								examStudentCCPA = new ExamStudentCCPA();
								examStudentSgpa = (ExamStudentSgpa) listItr.next();
								totalMaxMarks = totalMaxMarks + examStudentSgpa.getTotalMaxMarks();
								totalMarksAwarded = totalMarksAwarded+examStudentSgpa.getTotalMarksAwarded();
								if(examStudentSgpa.getCreditGradePoint()!=null)
									totalCreditPoints = totalCreditPoints+examStudentSgpa.getCreditGradePoint();
								if(examStudentSgpa.getCredit()!=null)
									totalCredits = totalCredits+Double.parseDouble(examStudentSgpa.getCredit());
								if(examStudentSgpa.getSubjetcTypeBasedCreditGradePoint()!=null)
									subjectTypeBasedCreditPoints= subjectTypeBasedCreditPoints+Double.parseDouble(examStudentSgpa.getSubjetcTypeBasedCreditGradePoint());
								if(examStudentSgpa.getSubjectTypeBasedObtainedMarks() != null && !examStudentSgpa.getSubjectTypeBasedObtainedMarks().isEmpty())
									subjectTypeBasedObtainedMarks += Double.parseDouble(examStudentSgpa.getSubjectTypeBasedObtainedMarks());
								if(examStudentSgpa.getSubjectTypeBasedAwardedMarks() != null && !examStudentSgpa.getSubjectTypeBasedAwardedMarks().isEmpty())
									subjectTypeBasedAwardedMarks += Double.parseDouble(examStudentSgpa.getSubjectTypeBasedAwardedMarks());
								if(examStudentSgpa.getSubjetcTypeBasedCredit()!=null)
									subjectTypeBasedTotalCredits = subjectTypeBasedTotalCredits+Integer.parseInt(examStudentSgpa.getSubjetcTypeBasedCredit());
								if(examStudentSgpa.getResult().equalsIgnoreCase("failed"))
									isFailed = true;
								if(examStudentSgpa.getYear()>0 && examStudentSgpa.getYear()>passYear){
									passYear = examStudentSgpa.getYear();
									passMonth = Integer.parseInt(examStudentSgpa.getMonth());
								}
								else if(examStudentSgpa.getYear()==passYear){
									if(Integer.parseInt(examStudentSgpa.getMonth())>0 && Integer.parseInt(examStudentSgpa.getMonth())>= passMonth){
										passMonth =Integer.parseInt( examStudentSgpa.getMonth());
									}
								}
								if(examStudentSgpa.getCreditsForDisplay()!=null)
									creditsForDisplay=creditsForDisplay+Integer.parseInt(examStudentSgpa.getCreditsForDisplay());

							}
							if(totalCredits>0)
								ccpa = CommonUtil.Round((totalCreditPoints/totalCredits),2);
							String overallGrade = UpdateExamStudentSGPAImpl.getInstance().getResultGrade(courseId, String.valueOf(ccpa), 0,stuId);
							examStudentCCPA.setCcpa(ccpa);
							examStudentCCPA.setCourse(course);
							Student student = new Student();
							student.setId(stuId);
							examStudentCCPA.setStudent(student);
							examStudentCCPA.setCredit(String.valueOf(totalCredits));
							examStudentCCPA.setCreditGradePoint(String.valueOf(CommonUtil.Round(totalCreditPoints,2)));
							examStudentCCPA.setTotalMarksAwarded(CommonUtil.Round(totalMarksAwarded,2));
							examStudentCCPA.setTotalMaxMarks(totalMaxMarks);

							if(subjectTypeBasedCreditPoints>0 && subjectTypeBasedTotalCredits>0)
								subjectTypeBasedCCPA = CommonUtil.Round(subjectTypeBasedCreditPoints/subjectTypeBasedTotalCredits,2);
							String subjectTypeBasedGrade = UpdateExamStudentSGPAImpl.getInstance().getResultGrade(courseId, String.valueOf(subjectTypeBasedCCPA), 0,stuId);
							if(subjectTypeBasedGrade!=null)
								examStudentCCPA.setSubjetcTypeBasedGrade(subjectTypeBasedGrade);
							examStudentCCPA.setSubjetcTypeBasedCgpa(String.valueOf(subjectTypeBasedCCPA));
							examStudentCCPA.setSubjetcTypeBasedCredit(String.valueOf(subjectTypeBasedTotalCredits));
							examStudentCCPA.setSubjetcTypeBasedCreditGradePoint(String.valueOf(CommonUtil.Round(subjectTypeBasedCreditPoints,2)));
							examStudentCCPA.setSubjectTypeBasedObtainedMarks(String.valueOf(subjectTypeBasedObtainedMarks));
							examStudentCCPA.setSubjectTypeBasedAwardedMarks(String.valueOf(subjectTypeBasedAwardedMarks));

							if(isFailed){
								if(notRegisteredMap.containsKey(stuId)) {
									examStudentCCPA.setResult("NR");
								}
								else {
									examStudentCCPA.setResult("failed");
								}
							}
							else{
								examStudentCCPA.setPassOutMonth(String.valueOf(passMonth));
								examStudentCCPA.setPassOutYear(passYear);
								examStudentCCPA.setResult("passed");
							}
							if(overallGrade!=null){
								examStudentCCPA.setGrade(overallGrade);
							}
							examStudentCCPA.setCreditsForDisplay(String.valueOf(creditsForDisplay));
							//examStudentCCPA.setResult(examStudentSgpa.getResult());
							examStudentCCPA.setAppliedYear(batchYear);
							examStudentCCPA.setModifiedBy(userId);
							examStudentCCPA.setLastModifiedDate(new Date());
							examStudentCCPA.setCreatedBy(userId);
							examStudentCCPA.setCreatedDate(new Date());
							if(examStudentCCPA!=null)
								examStudentCCPAList.add(examStudentCCPA);
						}
						//}
					}
				}
				iUpdateStudentSGPATxn.deleleAlreadyExistingCCPARecords(courseId,batchYear);
				if(examStudentCCPAList!=null && examStudentCCPAList.size()>0)
					isUpdated = iUpdateStudentSGPATxn.updateCCPA(examStudentCCPAList);
			}
		}
		return isUpdated;
	}

	public MarksCardTO getUGMarksCardResult(List<Object[]> ugMarksCardData,int schemeNo,int sid,Map<Integer,byte[]> studentPhotos,HttpServletRequest request,Map<Integer,String> revaluationSubjects, int courseId, String appliedYear) throws Exception {


		MarksCardTO to=null;

		if(ugMarksCardData!=null && !ugMarksCardData.isEmpty()){
			Iterator<Object[]> itr=ugMarksCardData.iterator();
			int cid=0;
			double totalMarksAwarded=0;
			double totalMaxMarks=0;
			int totalCredits=0;
			double tcForCal=0;
			double gpForCal=0;
			double totalgradepoints=0;
			double totcredits=0;
			boolean isFailed=false;

			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				to=new MarksCardTO();
				if(obj[10]!=null)
                cid =  Integer.parseInt(obj[10].toString());
				if(obj[20]!=null){
					SubjectTO subto=new SubjectTO();
					if(obj[40]!=null)
						subto.setGrade(obj[40].toString());
					if(obj[33]!=null){
						if(obj[33].toString().equalsIgnoreCase("Theory")){
							double ciaMarksAwarded=0;
							double externalMark =0;
							if(obj[6]!=null && CommonUtil.isValidDecimal(obj[6].toString())){
								ciaMarksAwarded=ciaMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[6].toString()),2);
							}
						if(obj[11]!=null){
							subto.setEseMinMarks(String.valueOf(CommonUtil.Round(Double.parseDouble(obj[11].toString()),2)));
						}
						if(obj[12]!=null){
							subto.setEseMaxMarks(String.valueOf(CommonUtil.Round(Double.parseDouble(obj[12].toString()),2)));
						}
						if(obj[15]!=null  && !obj[15].toString().equalsIgnoreCase("NR" ) && !obj[15].toString().equalsIgnoreCase("C" ) && !obj[15].toString().equalsIgnoreCase("AB") && !obj[15].toString().equalsIgnoreCase("MP") && CommonUtil.isValidDecimal(obj[15].toString())){
							externalMark =CommonUtil.Round(Double.parseDouble(obj[15].toString()),2);
						}

					if(obj[26]!=null){
						totalMaxMarks=totalMaxMarks+CommonUtil.Round(Double.parseDouble(obj[26].toString()),2);

					}
					if(obj[28]!=null && CommonUtil.isValidDecimal(obj[28].toString())){
						totalMarksAwarded=totalMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[28].toString()),2);

					}
					if(obj[11]!=null && obj[53]!=null){
						BigDecimal externalMinMarks = new BigDecimal(obj[11].toString());
						BigDecimal internalMinMarks = new BigDecimal(obj[53].toString());
					if(externalMark< externalMinMarks.intValue() || ciaMarksAwarded < internalMinMarks.intValue())
						isFailed=true;
					}
					DecimalFormat twoDForm = new DecimalFormat("#.##");
					if(obj[28]!=null){
						BigDecimal pnt=	new BigDecimal(obj[28].toString());
						double point=pnt.doubleValue()/10;
						subto.setTheoryGradePnt(String.valueOf(twoDForm.format(point)));
						BigDecimal ct=new BigDecimal(0);
						if(obj[31]!=null)
						 ct=new BigDecimal(obj[31].toString());
						double credit=ct.intValue();
						double cp=Double.parseDouble(String.valueOf(twoDForm.format(point*credit)));
						totalgradepoints=totalgradepoints+cp;
						totcredits=totcredits+credit;
					}
					else{
						if(obj[31]!=null){
							BigDecimal ct=new BigDecimal(obj[31].toString());
							int credit=ct.intValue();
							totcredits=totcredits+credit;												
						}
						isFailed=true;
					}

					if(obj[31]!=null){
						subto.setCredits(twoDForm.format(Double.parseDouble((obj[31].toString()))));
						if(subto.getGrade()!=null){
							if(obj[11]!=null && obj[15]!=null){
								if(CommonUtil.isValidDecimal(obj[11].toString()) && CommonUtil.isValidDecimal(obj[15].toString())){
									if(Double.parseDouble(obj[11].toString())<=Double.parseDouble(obj[15].toString()) && !subto.getGrade().equalsIgnoreCase("F"))
									{
										if(!subto.getGrade().equalsIgnoreCase("F")){
											totalCredits=totalCredits+Integer.parseInt(obj[31].toString());
										}
									}
								}
							}else{
								if(!subto.getGrade().equalsIgnoreCase("E") && !subto.getGrade().equalsIgnoreCase("H")){
									totalCredits=totalCredits+Integer.parseInt(obj[31].toString());
								}
							}
							tcForCal=tcForCal+Double.parseDouble((obj[31].toString()));
							double d=0;
							if(!CommonUtil.isValidDecimal(obj[15].toString())){
								d=0;
							}else if(subto.getGrade().equalsIgnoreCase("F")){
								d=0;
							}else{
								d=Double.parseDouble(obj[28].toString())/10;
							}
							gpForCal=gpForCal+d;
						}
					}
					}
					}
					if(obj[33].toString().equalsIgnoreCase("Practical")){
						double externalMark =0;
						double ciaMarksAwarded=0;
						if(obj[41]!=null)
							subto.setGrade(obj[41].toString());
						if(obj[8]!=null && CommonUtil.isValidDecimal(obj[8].toString())){
							ciaMarksAwarded=ciaMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[8].toString()),2);
						}

						if(obj[16]!=null && !obj[16].toString().equalsIgnoreCase("AB") && !obj[16].toString().equalsIgnoreCase("MP") && !obj[16].toString().equalsIgnoreCase("C") && !obj[16].toString().equalsIgnoreCase("NR") && CommonUtil.isValidDecimal(obj[16].toString())){
						    externalMark=CommonUtil.Round(Double.parseDouble(obj[16].toString()),2);
						}
						if(obj[27]!=null){
							totalMaxMarks=totalMaxMarks+CommonUtil.Round(Double.parseDouble(obj[27].toString()),2);
						}
						if(obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString())){
							totalMarksAwarded=totalMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[29].toString()),2);

						}
						if(obj[13]!=null && obj[54]!=null){
							BigDecimal externalMinMarks = new BigDecimal(obj[13].toString());
							BigDecimal internalMinMarks = new BigDecimal(obj[54].toString());
							if(externalMark< externalMinMarks.intValue() || ciaMarksAwarded < internalMinMarks.intValue())
								isFailed=true;
						}
						DecimalFormat twoDForm = new DecimalFormat("#.##");
						if(obj[29]!=null){
							if(obj[41]!=null && !obj[41].toString().equalsIgnoreCase("F")){
								//subto.setResultClass("Passed");
							}
							BigDecimal pnt=	new BigDecimal(obj[29].toString());
							double point=pnt.doubleValue()/10;
							BigDecimal ct=new BigDecimal(0);
							if(obj[30]!=null)
							   ct=new BigDecimal(obj[30].toString());
							double credit=ct.intValue();
							double cp=Double.parseDouble(String.valueOf(twoDForm.format(point*credit)));

							totalgradepoints=totalgradepoints+cp;
							totcredits=totcredits+credit;
						}


						else{
							if(obj[30]!=null){
								BigDecimal ct=new BigDecimal(obj[30].toString());
								int credit=ct.intValue();
								totcredits=totcredits+credit;												
							}
							isFailed=true;
						}

						if(obj[30]!=null){
							if(subto.getGrade()!=null){
								if(obj[13]!=null && obj[16]!=null){
									if(CommonUtil.isValidDecimal(obj[13].toString()) && CommonUtil.isValidDecimal(obj[16].toString())){
										if(Double.parseDouble(obj[13].toString())<=Double.parseDouble(obj[16].toString()) && !subto.getGrade().equalsIgnoreCase("F"))
										{
											if(!subto.getGrade().equalsIgnoreCase("F")){
												totalCredits=totalCredits+Integer.parseInt(obj[30].toString());
											}
										}
									}
								}else{
									if(!subto.getGrade().equalsIgnoreCase("E") && !subto.getGrade().equalsIgnoreCase("H")){
										totalCredits=totalCredits+Integer.parseInt(obj[30].toString());
									}
								}
								tcForCal=tcForCal+Double.parseDouble((obj[30].toString()));
								double min=0;
								double stu=0;
								if(obj[13]!=null && CommonUtil.isValidDecimal(obj[13].toString()))
									min=0;
								if(obj[16]!=null && CommonUtil.isValidDecimal(obj[16].toString()))
									stu=0;
								double d=0;
								if(!CommonUtil.isValidDecimal(obj[16].toString())){
									d=0;
								}else if(subto.getGrade().equalsIgnoreCase("F") ||min>stu){
									d=0;
								}else{
									d=Double.parseDouble(obj[29].toString())/10;
								}
								gpForCal=gpForCal+d;
							}
						}							
					}
					if(appliedYear.equalsIgnoreCase("2017") && courseId== 18){
					if(obj[33]!=null){
						if(obj[33].toString().equalsIgnoreCase("Both")){
							double ciaMarksAwarded=0;
							double externalMark =0;
							double externalPracticalMark = 0;
							if(obj[6]!=null && CommonUtil.isValidDecimal(obj[6].toString())){
								ciaMarksAwarded=ciaMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[6].toString()),2);
							}
						if(obj[11]!=null){
							subto.setEseMinMarks(String.valueOf(CommonUtil.Round(Double.parseDouble(obj[11].toString()),2)));
						}
						if(obj[12]!=null){
							subto.setEseMaxMarks(String.valueOf(CommonUtil.Round(Double.parseDouble(obj[12].toString()),2)));
						}
						if(obj[15]!=null  && !obj[15].toString().equalsIgnoreCase("NR" ) && !obj[15].toString().equalsIgnoreCase("C" ) && !obj[15].toString().equalsIgnoreCase("AB") && !obj[15].toString().equalsIgnoreCase("MP") && CommonUtil.isValidDecimal(obj[15].toString())){
							externalMark =CommonUtil.Round(Double.parseDouble(obj[15].toString()),2);
						}
						if(obj[16]!=null  && !obj[16].toString().equalsIgnoreCase("NR" ) && !obj[16].toString().equalsIgnoreCase("C" ) && !obj[16].toString().equalsIgnoreCase("AB") && !obj[16].toString().equalsIgnoreCase("MP") && CommonUtil.isValidDecimal(obj[16].toString())){
							externalPracticalMark = CommonUtil.Round(Double.parseDouble(obj[16].toString()),2);
						}

					if(obj[26]!=null && obj[27] != null){
						totalMaxMarks=totalMaxMarks+CommonUtil.Round(Double.parseDouble(obj[26].toString()),2) + CommonUtil.Round(Double.parseDouble(obj[27].toString()),2) ;

					}
					if(obj[28]!=null && CommonUtil.isValidDecimal(obj[28].toString() )&& obj[29]!=null && CommonUtil.isValidDecimal(obj[29].toString())){
						totalMarksAwarded=totalMarksAwarded+CommonUtil.Round(Double.parseDouble(obj[28].toString()),2) +CommonUtil.Round(Double.parseDouble(obj[29].toString()),2);

					}
					if(obj[11]!=null && obj[53]!=null){
						BigDecimal externalMinMarks = new BigDecimal(obj[11].toString());
						BigDecimal internalMinMarks = new BigDecimal(obj[53].toString());
					if(externalMark< externalMinMarks.intValue() || ciaMarksAwarded < internalMinMarks.intValue())
						isFailed=true;
					}
					DecimalFormat twoDForm = new DecimalFormat("#.##");
					if(obj[28]!=null){
						BigDecimal pnt=	new BigDecimal(obj[28].toString());
						BigDecimal pnt1=	new BigDecimal(obj[29].toString());
						BigDecimal totalpoint = pnt.add(pnt1);
						double point=totalpoint.doubleValue()/10;
						subto.setTheoryGradePnt(String.valueOf(twoDForm.format(point)));
						BigDecimal ct=new BigDecimal(0);
						if(obj[31]!=null)
						 ct=new BigDecimal(obj[31].toString());
						double credit=ct.intValue();
						double cp=Double.parseDouble(String.valueOf(twoDForm.format(point*credit)));
						totalgradepoints=totalgradepoints+cp;
						totcredits=totcredits+credit;
					}
					else{
						if(obj[31]!=null){
							BigDecimal ct=new BigDecimal(obj[31].toString());
							int credit=ct.intValue();
							totcredits=totcredits+credit;												
						}
						isFailed=true;
					}

					if(obj[31]!=null){
						subto.setCredits(twoDForm.format(Double.parseDouble((obj[31].toString()))));
						if(subto.getGrade()!=null){
							if(obj[11]!=null && obj[15]!=null){
								if(CommonUtil.isValidDecimal(obj[11].toString()) && CommonUtil.isValidDecimal(obj[15].toString())){
									if(Double.parseDouble(obj[11].toString())<=Double.parseDouble(obj[15].toString()) && !subto.getGrade().equalsIgnoreCase("F"))
									{
										if(obj[16]!=null  && !obj[16].toString().equalsIgnoreCase("NR" ) && !obj[16].toString().equalsIgnoreCase("C" ) && !obj[16].toString().equalsIgnoreCase("AB") && !obj[16].toString().equalsIgnoreCase("MP") && CommonUtil.isValidDecimal(obj[16].toString())){
										if(Double.parseDouble(obj[13].toString())<=Double.parseDouble(obj[16].toString()) && !subto.getGrade().equalsIgnoreCase("F"))
										{
										if(!subto.getGrade().equalsIgnoreCase("F")){
											totalCredits=totalCredits+Integer.parseInt(obj[31].toString());
										}
									}
									}
									}
								}
							}else{
								if(!subto.getGrade().equalsIgnoreCase("E") && !subto.getGrade().equalsIgnoreCase("H")){
									totalCredits=totalCredits+Integer.parseInt(obj[31].toString());
								}
							}
							tcForCal=tcForCal+Double.parseDouble((obj[31].toString()));
							double d=0;
							double total = 0;
							if(!CommonUtil.isValidDecimal(obj[15].toString())){
								d=0;
							}else if(subto.getGrade().equalsIgnoreCase("F")){
								d=0;
							}else{
								total = Double.parseDouble(obj[28].toString()) + Double.parseDouble(obj[29].toString());
								d=total/10;
							}
							gpForCal=gpForCal+d;
						}
					 }
					}
				  }
				 }
				}


			}	
			to.setTotalMaxmarks(String.valueOf(totalMaxMarks));

				if(totalgradepoints>0 && totcredits>0){
					double sgpa=(totalgradepoints/totcredits);
					to.setSgpa(String.valueOf(CommonUtil.Round(sgpa,2)));
				}
				int tcredits=(int)totcredits;
				to.setTotalCredits(String.valueOf(tcredits));
				//int tcreditPoints=(int)totalgradepoints;
				to.setTotalGradePoints(String.valueOf(totalgradepoints));
				String grade = UpdateExamStudentSGPAImpl.getInstance().getResultGrade(cid, to.getSgpa(), 0,sid);
				if(grade!=null)
				to.setTotalGrade(grade);
				to.setTotalMarksAwarded(String.valueOf(CommonUtil.Round(totalMarksAwarded,2)));
				if(isFailed==true)
				to.setResultClass("Failed");
				else
				to.setResultClass("Passed");
			
			double tgpa=0;
			if(gpForCal>0 && tcForCal>0){
				tgpa=CommonUtil.Round((gpForCal/tcForCal),2);
			}
			if(isFailed==false)
				to.setGradePoints(String.valueOf(CommonUtil.Round(tgpa, 2)));
		}

		return to;

	}
	
	private Map<Integer, ExamStudentSgpa> calculateSgpaForUGCourse(Map<Integer,Map<String, ConsolidateMarksCard>> boMap, int schemeNo,int examId, int classId, int courseId,int year, int month) throws Exception{
		int  studId ;
		Map<String, ConsolidateMarksCard> conSemMap;
		Map<Integer, ExamStudentSgpa> examStudentSgpaMap = new HashMap<Integer, ExamStudentSgpa>();
		ConsolidateMarksCard consolidateMarksCard;
		String examMonthYear = null;
		String[] examMonthYearArr = new String[2];
		Map<Integer, String> examIdMap = new HashMap<Integer,String>();
		Iterator entries = boMap.entrySet().iterator();
		while (entries.hasNext()) {
			boolean isFailed=false;
			double totalcreditpoint=0;
			double gradePoint=0;
			int totcredit=0;
			int totalMaxMarks=0;
			double totalmark=0;
			double totalcreditpointBasedOnSubjectType=0;
			int totcreditBasedOnSubjectType=0;
			double subjectTypeBasedObtainedMarks = 0;
			double subjectTypeBasedAwardedMarks = 0;
			int creditsForDisplay=0;
			Entry mainMap = (Entry) entries.next();
			studId = (Integer) mainMap.getKey();
			//if(studId==16370){
			//System.out.println(studId + "==start==");
			conSemMap = (Map<String, ConsolidateMarksCard>) mainMap.getValue();
			Iterator conSemMapEntry = conSemMap.entrySet().iterator();
			examIdMap = new HashMap<Integer,String>();
			while(conSemMapEntry.hasNext()){
				double internalMark=0;
				double externalMark=0;
				double externalPracticalMark = 0;
				examMonthYear = null;
				examMonthYearArr = new String[2];
				Entry semMap = (Entry) conSemMapEntry.next();
				consolidateMarksCard= (ConsolidateMarksCard) semMap.getValue();

				if(!examIdMap.containsKey(consolidateMarksCard.getStudent().getId())){
					examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
					examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
				}
				else{
					examMonthYear = examIdMap.get(consolidateMarksCard.getStudent().getId());
					examMonthYearArr = examMonthYear.split("_");
					int maxExamId = Integer.parseInt(examMonthYearArr[0]);
					if(consolidateMarksCard.getExam().getId()> maxExamId){
						examIdMap.remove(examMonthYear);
						examMonthYear = consolidateMarksCard.getExam().getId()+"_"+consolidateMarksCard.getYear()+"_"+consolidateMarksCard.getMonth();
						examIdMap.put(consolidateMarksCard.getStudent().getId(), examMonthYear);
					}
				}

				if(consolidateMarksCard.getSubType().equalsIgnoreCase("Theory")){

                  if(!consolidateMarksCard.getShowOnlyCredits()){
					// internal only subject
					if(consolidateMarksCard.getSubject().getSubjectTypeId()==15){
						if(consolidateMarksCard.getTheoryMax()!=null)
							totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue();
				
						if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("AB")&&
								!consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("C")){
							internalMark = Double.valueOf(consolidateMarksCard.getTheoryTotalSubInternalMark());
						}
						else
							isFailed = true;
						totalmark=totalmark+internalMark;

						if(consolidateMarksCard.getTheoryMax()!=null){
							double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
							gradePoint=marksAwarded/10;
						}
						BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
						int credit=pnt.intValue();

						//double cp=CommonUtil.Round(gradePoint*credit,2);
						double cp=CommonUtil.round(gradePoint*credit,2);
						totalcreditpoint=totalcreditpoint+cp;
						totcredit=totcredit+credit;
						
						// calculating credits and creditpoints excluding langauage subjects
						
						if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
								!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
							if(consolidateMarksCard.getTheoryMax()!=null){
								double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
							int credits=pnts.intValue();
							//double creditPoints=gradePoint*credits;
							Double creditPoints = CommonUtil.round(gradePoint * credit,2);
							totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
							totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
							subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
							subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
						}

						double finalTheoryInternalMinimumMark =0;
						double theoryMin=0;
						if(consolidateMarksCard.getFinalTheoryInternalMinimumMark()!=null){
							BigDecimal bd1=consolidateMarksCard.getFinalTheoryInternalMinimumMark(); 
							finalTheoryInternalMinimumMark = bd1.doubleValue();
						}
				
						if(consolidateMarksCard.getTheoryMin()!=null){
							BigDecimal bd3=consolidateMarksCard.getTheoryMin();
							theoryMin = bd3.doubleValue();
						}
						if((internalMark<finalTheoryInternalMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin)){
							isFailed = true;

						}

					}
					// external only subject
					else if(consolidateMarksCard.getSubject().getSubjectTypeId()==13){
						if(consolidateMarksCard.getTheoryMax()!=null)
							totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue();
						if(( consolidateMarksCard.getStudentTheoryMark()!=null && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) &&
								!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).trim().equalsIgnoreCase("") && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR") && !consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("C")){
							externalMark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
						}
						else{
							isFailed = true;
						}

						totalmark=totalmark+externalMark;

						if(consolidateMarksCard.getTheoryMax()!=null){
							double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
							gradePoint=marksAwarded/10;
						}
						BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
						int credit=pnt.intValue();

						//double cp=CommonUtil.Round(gradePoint*credit,2);
						double cp=CommonUtil.round(gradePoint*credit,2);
						totalcreditpoint=totalcreditpoint+cp;
						totcredit=totcredit+credit;
						
						// calculating credits and creditpoints excluding langauage subjects
						if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
								!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
							if(consolidateMarksCard.getTheoryMax()!=null){
								double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
							int credits=pnts.intValue();
							//double creditPoints=gradePoint*credits;
							Double creditPoints = CommonUtil.round(gradePoint * credit,2);
							totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
							totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
							subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
							subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
						}

						double theoryeseMinimumMark =0;
						double theoryMin=0;

						if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
							BigDecimal bd2=consolidateMarksCard.getTheoryeseMinimumMark(); 
							theoryeseMinimumMark = bd2.doubleValue();
						}
						if(consolidateMarksCard.getTheoryMin()!=null){
							BigDecimal bd3=consolidateMarksCard.getTheoryMin();
							theoryMin = bd3.doubleValue();
						}
						if( (externalMark<theoryeseMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin)){
							isFailed = true;

						}

					}
					else{

						if(consolidateMarksCard.getTheoryMax()!=null)
							totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue();
						if((consolidateMarksCard.getStudentTheoryMark()!=null && 
							!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) &&
							!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).trim().equalsIgnoreCase("") && 
							!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR") && 
							!consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("MP") &&
							!consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("C")){
								externalMark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
						}
						else{
							isFailed = true;
						}
						if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("AB")&&
								!consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("C")){
							internalMark = Double.valueOf(consolidateMarksCard.getTheoryTotalSubInternalMark());
						}
						else
							isFailed = true;
						totalmark=totalmark+internalMark+externalMark;

						if(consolidateMarksCard.getTheoryMax()!=null){
							double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
							gradePoint=marksAwarded/10;
						}
						BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
						int credit=pnt.intValue();

						//double cp=CommonUtil.Round(gradePoint*credit,2);
						double cp=CommonUtil.round(gradePoint*credit,2);
						totalcreditpoint=totalcreditpoint+cp;
						totcredit=totcredit+credit;
						
						// calculating credits and creditpoints excluding langauage subjects
						if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
								!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
							if(consolidateMarksCard.getTheoryMax()!=null){
								double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
							int credits=pnts.intValue();
							//double creditPoints=gradePoint*credits;
							Double creditPoints = CommonUtil.round(gradePoint * credit,2);
							totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
							totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
							subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
							subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
						}
						double finalTheoryInternalMinimumMark =0;
						double theoryeseMinimumMark =0;
						double theoryMin=0;
						if(consolidateMarksCard.getFinalTheoryInternalMinimumMark()!=null){
							BigDecimal bd1=consolidateMarksCard.getFinalTheoryInternalMinimumMark(); 
							finalTheoryInternalMinimumMark = bd1.doubleValue();
						}
						if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
							BigDecimal bd2=consolidateMarksCard.getTheoryeseMinimumMark(); 
							theoryeseMinimumMark = bd2.doubleValue();
						}
						if(consolidateMarksCard.getTheoryMin()!=null){
							BigDecimal bd3=consolidateMarksCard.getTheoryMin();
							theoryMin = bd3.doubleValue();
						}
						if((internalMark<finalTheoryInternalMinimumMark) || (externalMark<theoryeseMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin)){
							isFailed = true;

						}

					}
                  
                  }
                  else{
                	  creditsForDisplay=creditsForDisplay+consolidateMarksCard.getTheoryCredit();
                  }
				}			
				if(consolidateMarksCard.getSubType().equalsIgnoreCase("Practical")){
					if(!consolidateMarksCard.getShowOnlyCredits()){
						// internal only subject
						if(consolidateMarksCard.getSubject().getSubjectTypeId()==15){
							if(consolidateMarksCard.getPracticalMax()!=null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getPracticalMax().intValue();

							if(consolidateMarksCard.getPracticalTotalSubInternalMark()!=null && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("AB")
									&& !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("C")){
								internalMark = Double.valueOf(consolidateMarksCard.getPracticalTotalSubInternalMark());
							}
							else{
								isFailed = true;
							}

							totalmark=totalmark+internalMark;

							if(consolidateMarksCard.getPracticalMax()!=null){
								double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}

							BigDecimal pnt=	new BigDecimal(consolidateMarksCard.getPracticalCredit());
							int credit=pnt.intValue();

							double cp=CommonUtil.round(gradePoint*credit,2);
							//float cp = CommonUtil.round((float) (gradePoint * credit),2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;

							// calculating credits and creditpoints excluding langauage subjects
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getPracticalMax()!=null){
									double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getPracticalCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
								subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
							}

							if(consolidateMarksCard.getFinalPracticalInternalMinimumMark()!=null
									&& consolidateMarksCard.getPracticalMin()!=null){
								if((internalMark<Double.parseDouble(String.valueOf(consolidateMarksCard.getFinalPracticalInternalMinimumMark()))) || (consolidateMarksCard.getPracticalObtain()<Double.parseDouble(String.valueOf(consolidateMarksCard.getPracticalMin())))){
									isFailed = true;

								}
							}
							else{
								isFailed = true;
							}


						}

						// external only subjects
						else if(consolidateMarksCard.getSubject().getSubjectTypeId()==13){
							if(consolidateMarksCard.getPracticalMax()!=null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getPracticalMax().intValue();
							if((consolidateMarksCard.getStudentPracticalMark()!=null && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("AB")) && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase(" ")
									&& !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("C")){
								externalMark = Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());
							}
							else{
								isFailed = true;
							}

							totalmark=totalmark+externalMark;
							if(consolidateMarksCard.getPracticalMax()!=null){
								double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnt=	new BigDecimal(consolidateMarksCard.getPracticalCredit());
							int credit=pnt.intValue();

							//double cp=CommonUtil.Round(gradePoint*credit,2);
							double cp=CommonUtil.round(gradePoint*credit,2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;

							// calculating credits and creditpoints excluding langauage subjects
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getPracticalMax()!=null){
									double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getPracticalCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
								subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
							}

							if(consolidateMarksCard.getPracticaleseMinimumMark()!=null
									&& consolidateMarksCard.getPracticalMin()!=null){
								if((externalMark<Double.parseDouble(String.valueOf(consolidateMarksCard.getPracticaleseMinimumMark()))) || (consolidateMarksCard.getPracticalObtain()<Double.parseDouble(String.valueOf(consolidateMarksCard.getPracticalMin())))){
									isFailed = true;

								}
							}
							else{
								isFailed = true;
							}


						}	
						else{

							if(consolidateMarksCard.getPracticalMax()!=null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getPracticalMax().intValue();
							if((consolidateMarksCard.getStudentPracticalMark()!=null && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("AB")) && !String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase(" ")
									&& !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("C")){
								externalMark = Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());
							}
							else{
								isFailed = true;
							}
							if(consolidateMarksCard.getPracticalTotalSubInternalMark()!=null && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("AB")
									&& !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getPracticalTotalSubInternalMark().equalsIgnoreCase("C")){
								internalMark = Double.valueOf(consolidateMarksCard.getPracticalTotalSubInternalMark());
							}
							else{
								isFailed = true;
							}

							totalmark=totalmark+internalMark+externalMark;
							if(consolidateMarksCard.getPracticalMax()!=null){
								double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnt=	new BigDecimal(consolidateMarksCard.getPracticalCredit());
							int credit=pnt.intValue();

							//double cp=CommonUtil.Round(gradePoint*credit,2);
							double cp=CommonUtil.round(gradePoint*credit,2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;

							// calculating credits and creditpoints excluding langauage subjects
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getPracticalMax()!=null){
									double marksAwarded = (consolidateMarksCard.getPracticalObtain()/consolidateMarksCard.getPracticalMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getPracticalCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
								subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
							}

							if(consolidateMarksCard.getFinalPracticalInternalMinimumMark()!=null && consolidateMarksCard.getPracticaleseMinimumMark()!=null
									&& consolidateMarksCard.getPracticalMin()!=null){
								if((internalMark<Double.parseDouble(String.valueOf(consolidateMarksCard.getFinalPracticalInternalMinimumMark()))) || (externalMark<Double.parseDouble(String.valueOf(consolidateMarksCard.getPracticaleseMinimumMark()))) || (consolidateMarksCard.getPracticalObtain()<Double.parseDouble(String.valueOf(consolidateMarksCard.getPracticalMin())))){
									isFailed = true;

								}
							}
							else{
								isFailed = true;
							}

						}
					}
					else{
	                	 creditsForDisplay=creditsForDisplay+consolidateMarksCard.getPracticalCredit();
					}
				}
				if(consolidateMarksCard.getAppliedYear()==2017 && consolidateMarksCard.getCourse().getId() == 18){
				if(consolidateMarksCard.getSubType().equalsIgnoreCase("Both")){

	                  if(!consolidateMarksCard.getShowOnlyCredits()){
						// internal only subject
						if(consolidateMarksCard.getSubject().getSubjectTypeId()==15){
							if(consolidateMarksCard.getTheoryMax()!=null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue();
					
							if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("AB")&&
									!consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("C")){
								internalMark = Double.valueOf(consolidateMarksCard.getTheoryTotalSubInternalMark());
							}
							else
								isFailed = true;
							totalmark=totalmark+internalMark;

							if(consolidateMarksCard.getTheoryMax()!=null){
								double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
							int credit=pnt.intValue();

							//double cp=CommonUtil.Round(gradePoint*credit,2);
							double cp=CommonUtil.round(gradePoint*credit,2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;
							
							// calculating credits and creditpoints excluding langauage subjects
							
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getTheoryMax()!=null){
									double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
								subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
							}

							double finalTheoryInternalMinimumMark =0;
							double theoryMin=0;
							if(consolidateMarksCard.getFinalTheoryInternalMinimumMark()!=null){
								BigDecimal bd1=consolidateMarksCard.getFinalTheoryInternalMinimumMark(); 
								finalTheoryInternalMinimumMark = bd1.doubleValue();
							}
					
							if(consolidateMarksCard.getTheoryMin()!=null){
								BigDecimal bd3=consolidateMarksCard.getTheoryMin();
								theoryMin = bd3.doubleValue();
							}
							if((internalMark<finalTheoryInternalMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin)){
								isFailed = true;

							}

						}
						// external only subject
						else if(consolidateMarksCard.getSubject().getSubjectTypeId()==13){
							if(consolidateMarksCard.getTheoryMax()!=null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue();
							if(( consolidateMarksCard.getStudentTheoryMark()!=null && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) &&
									!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).trim().equalsIgnoreCase("") && !String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR") && !consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("C")){
								externalMark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
							}
							else{
								isFailed = true;
							}

							totalmark=totalmark+externalMark;

							if(consolidateMarksCard.getTheoryMax()!=null){
								double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
							int credit=pnt.intValue();

							//double cp=CommonUtil.Round(gradePoint*credit,2);
							double cp=CommonUtil.round(gradePoint*credit,2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;
							
							// calculating credits and creditpoints excluding langauage subjects
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getTheoryMax()!=null){
									double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								subjectTypeBasedObtainedMarks += consolidateMarksCard.getTheoryObtain();
								subjectTypeBasedAwardedMarks += consolidateMarksCard.getTheoryMax().doubleValue();
							}

							double theoryeseMinimumMark =0;
							double theoryMin=0;

							if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
								BigDecimal bd2=consolidateMarksCard.getTheoryeseMinimumMark(); 
								theoryeseMinimumMark = bd2.doubleValue();
							}
							if(consolidateMarksCard.getTheoryMin()!=null){
								BigDecimal bd3=consolidateMarksCard.getTheoryMin();
								theoryMin = bd3.doubleValue();
							}
							if( (externalMark<theoryeseMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin)){
								isFailed = true;

							}

						}
						else{

							if(consolidateMarksCard.getTheoryMax()!=null && consolidateMarksCard.getPracticalMax() != null)
								totalMaxMarks = totalMaxMarks+ consolidateMarksCard.getTheoryMax().intValue() + consolidateMarksCard.getPracticalMax().intValue();
							if((consolidateMarksCard.getStudentTheoryMark()!=null && 
								!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("AB")) &&
								!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).trim().equalsIgnoreCase("") && 
								!String.valueOf(consolidateMarksCard.getStudentTheoryMark()).equalsIgnoreCase("NR") && 
								!consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("MP") &&
								!consolidateMarksCard.getStudentTheoryMark().equalsIgnoreCase("C")){
									externalMark = Double.parseDouble(consolidateMarksCard.getStudentTheoryMark());
							}
							else{
								isFailed = true;
							}
							if((consolidateMarksCard.getStudentPracticalMark()!=null && 
									!String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("AB")) &&
									!String.valueOf(consolidateMarksCard.getStudentPracticalMark()).trim().equalsIgnoreCase("") && 
									!String.valueOf(consolidateMarksCard.getStudentPracticalMark()).equalsIgnoreCase("NR") && 
									!consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("MP") &&
									!consolidateMarksCard.getStudentPracticalMark().equalsIgnoreCase("C")){
										externalPracticalMark = Double.parseDouble(consolidateMarksCard.getStudentPracticalMark());
								}
								else{
									isFailed = true;
								}
							if(consolidateMarksCard.getTheoryTotalSubInternalMark()!=null && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("AB")&&
									!consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("NR") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("MP") && !consolidateMarksCard.getTheoryTotalSubInternalMark().equalsIgnoreCase("C")){
								internalMark = Double.valueOf(consolidateMarksCard.getTheoryTotalSubInternalMark());
							}
							else
								isFailed = true;
							double totalmarks = 0;
							totalmarks = externalMark +externalPracticalMark;
							totalmark=totalmark+internalMark+totalmarks;

							if(consolidateMarksCard.getTheoryMax()!=null && consolidateMarksCard.getPracticalMax() != null){
								double theoryMax = consolidateMarksCard.getTheoryMax().doubleValue();
								double practicalMax = consolidateMarksCard.getPracticalMax().doubleValue();
								double totalMax = theoryMax + practicalMax;
								double theoryObtainBoth = consolidateMarksCard.getTheoryObtain();
								double practicalObtainBoth = consolidateMarksCard.getPracticalObtain();
								double ObtainMarks = theoryObtainBoth + practicalObtainBoth;
								//double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
								double marksAwarded = (ObtainMarks/totalMax)*100;
								gradePoint=marksAwarded/10;
							}
							BigDecimal pnt=	new BigDecimal(String.valueOf(consolidateMarksCard.getTheoryCredit()));
							int credit=pnt.intValue();

							//double cp=CommonUtil.Round(gradePoint*credit,2);
							double cp=CommonUtil.round(gradePoint*credit,2);
							totalcreditpoint=totalcreditpoint+cp;
							totcredit=totcredit+credit;
							
							// calculating credits and creditpoints excluding langauage subjects
							if( !CMSConstants.SUBJECT_TYPE_ID_LIST.contains(consolidateMarksCard.getSubject().getSubjectTypeId()) &&
									!CMSConstants.SUBJECT_ID_LIST.contains(consolidateMarksCard.getSubject().getId())){
								if(consolidateMarksCard.getTheoryMax()!=null && consolidateMarksCard.getPracticalMax() != null){
									double theoryMax = consolidateMarksCard.getTheoryMax().doubleValue();
									double practicalMax = consolidateMarksCard.getPracticalMax().doubleValue();
									double totalMax = theoryMax + practicalMax;
									double theoryObtainBoth = consolidateMarksCard.getTheoryObtain();
									double practicalObtainBoth = consolidateMarksCard.getPracticalObtain();
									double ObtainMarks = theoryObtainBoth + practicalObtainBoth;
									//double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									double marksAwarded = (ObtainMarks/totalMax)*100;
									//double marksAwarded = (consolidateMarksCard.getTheoryObtain()/consolidateMarksCard.getTheoryMax().doubleValue())*100;
									gradePoint=marksAwarded/10;
								}
								BigDecimal pnts=new BigDecimal(consolidateMarksCard.getTheoryCredit());
								int credits=pnts.intValue();
								//double creditPoints=gradePoint*credits;
								Double creditPoints = CommonUtil.round(gradePoint * credit,2);
								totalcreditpointBasedOnSubjectType=totalcreditpointBasedOnSubjectType+creditPoints;
								totcreditBasedOnSubjectType=totcreditBasedOnSubjectType+credits;
								double theoryObtainBoth = consolidateMarksCard.getTheoryObtain();
								double practicalObtainBoth = consolidateMarksCard.getPracticalObtain();
								double ObtainMarks = theoryObtainBoth + practicalObtainBoth;
								subjectTypeBasedObtainedMarks += ObtainMarks;
								double theoryMax = consolidateMarksCard.getTheoryMax().doubleValue();
								double practicalMax = consolidateMarksCard.getPracticalMax().doubleValue();
								double totalMax = theoryMax + practicalMax;
								subjectTypeBasedAwardedMarks += totalMax;
							}
							double finalTheoryInternalMinimumMark =0;
							double theoryeseMinimumMark =0;
							double theoryMin=0;
							double practicalMin = 0;
							if(consolidateMarksCard.getFinalTheoryInternalMinimumMark()!=null){
								BigDecimal bd1=consolidateMarksCard.getFinalTheoryInternalMinimumMark(); 
								finalTheoryInternalMinimumMark = bd1.doubleValue();
							}
							if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
								BigDecimal bd2=consolidateMarksCard.getTheoryeseMinimumMark(); 
								theoryeseMinimumMark = bd2.doubleValue();
							}
							if(consolidateMarksCard.getTheoryeseMinimumMark()!=null){
								BigDecimal bd3=consolidateMarksCard.getTheoryeseMinimumMark();
								theoryMin = bd3.doubleValue();
							}
							if(consolidateMarksCard.getPracticaleseMinimumMark()!=null){
								BigDecimal bd4=consolidateMarksCard.getPracticaleseMinimumMark();
								practicalMin = bd4.doubleValue();
							}
							if((internalMark<finalTheoryInternalMinimumMark) || (consolidateMarksCard.getTheoryObtain()<theoryMin) || (consolidateMarksCard.getPracticalObtain()<practicalMin)){
								isFailed = true;

							}

						}
	                  
	                  }
	                  else{
	                	  creditsForDisplay=creditsForDisplay+consolidateMarksCard.getTheoryCredit();
	                  }
					}
				}
			}
			//} // checking for individual students
			
			ExamStudentSgpa examStudentSgpa = new ExamStudentSgpa();
			double sgpa=0;
			double subjectTypeBasedsgpa =0;
			String subjectTypeBasedGrade =null;
			if(totalcreditpoint>0 && totcredit>0){
				sgpa = new BigDecimal((Double.valueOf(totalcreditpoint/totcredit)).toString()).setScale(4,RoundingMode.HALF_UP).doubleValue();
				sgpa=(CommonUtil.round(sgpa, 2));
			}
			String overallGrade = UpdateExamStudentSGPAImpl.getInstance().getResultGrade(courseId, String.valueOf(sgpa), 0,studId);

			if(totalcreditpointBasedOnSubjectType>0 && totcreditBasedOnSubjectType>0){
				subjectTypeBasedsgpa=CommonUtil.Round((totalcreditpointBasedOnSubjectType/totcreditBasedOnSubjectType), 2);
				subjectTypeBasedGrade = UpdateExamStudentSGPAImpl.getInstance().getResultGrade(courseId, String.valueOf(subjectTypeBasedsgpa), 0,studId);
				examStudentSgpa.setSubjetcTypeBasedSgpa(String.valueOf(subjectTypeBasedsgpa));
			}
		
			if(subjectTypeBasedGrade!=null && !subjectTypeBasedGrade.equalsIgnoreCase(""))
				examStudentSgpa.setSubjetcTypeBasedGrade(subjectTypeBasedGrade);
			examStudentSgpa.setSubjetcTypeBasedCredit(String.valueOf(totcreditBasedOnSubjectType));
			examStudentSgpa.setSubjetcTypeBasedCreditGradePoint(String.valueOf(CommonUtil.Round(totalcreditpointBasedOnSubjectType, 2)));
			examStudentSgpa.setSubjectTypeBasedObtainedMarks(String.valueOf(subjectTypeBasedObtainedMarks));
			examStudentSgpa.setSubjectTypeBasedAwardedMarks(String.valueOf(subjectTypeBasedAwardedMarks));
			examStudentSgpa.setCreditsForDisplay(String.valueOf(creditsForDisplay));
			
			Student student = new Student();
			Classes classes = new Classes();
			Course course = new Course();
			course.setId(courseId);
			examStudentSgpa.setCourse(course);
			student.setId(studId);
			examStudentSgpa.setStudent(student);
			classes.setId(classId);
			examStudentSgpa.setClasses(classes);
			examStudentSgpa.setSchemeNo(schemeNo);
			examMonthYear = examIdMap.get(studId);
			examMonthYearArr = examMonthYear.split("_");
			examStudentSgpa.setYear(Integer.parseInt(examMonthYearArr[1]));
			examStudentSgpa.setMonth(examMonthYearArr[2]);
			examStudentSgpa.setTotalMaxMarks(totalMaxMarks);
			examStudentSgpa.setTotalMarksAwarded(CommonUtil.Round(totalmark,2));
			if(overallGrade!=null && !overallGrade.equalsIgnoreCase(""))
				examStudentSgpa.setGrade(overallGrade);

			if(isFailed)
				examStudentSgpa.setResult("Failed");
			else
				examStudentSgpa.setResult("Passed");
			examStudentSgpa.setSgpa(sgpa);
			examStudentSgpa.setCredit(String.valueOf(totcredit));
			examStudentSgpa.setCreditGradePoint(totalcreditpoint);
			examStudentSgpaMap.put(studId, examStudentSgpa);
			//System.out.println(studId + "==end==");

		// }// student checking
		}

		return examStudentSgpaMap;
	}


}
