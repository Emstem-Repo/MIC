package com.kp.cms.helpers.exam;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.SubjectGroupSubjects;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamStudentPassFail;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.bo.exam.StudentOverallInternalMarkDetails;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.exam.NewUpdateProccessForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.admin.SubjectTO;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.to.exam.StudentMarkDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.SubjectMarksTO;
import com.kp.cms.to.exam.SubjectRuleSettingsTO;
import com.kp.cms.to.usermanagement.StudentAttendanceTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewUpdateProccessTransaction;
import com.kp.cms.transactions.exam.IUpdateStudentSGPATxn;
import com.kp.cms.transactionsimpl.exam.ExamUpdateProcessImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewUpdateProccessTransactionImpl;
import com.kp.cms.transactionsimpl.exam.UpdateExamStudentSGPAImpl;
import com.kp.cms.utilities.CommonUtil;

@SuppressWarnings("unchecked")
public class NewUpdateProccessHelper {
	static DecimalFormat df = new DecimalFormat("#.##");
	public static List<Integer> avoidExamIds=new ArrayList<Integer>();
	IUpdateStudentSGPATxn iUpdateStudentSGPATxn = UpdateExamStudentSGPAImpl.getInstance();
	/**
	 * Singleton object of NewUpdateProccessHelper
	 */
	private static volatile NewUpdateProccessHelper newUpdateProccessHelper = null;
	private NewUpdateProccessHelper() {
		
	}
	/**
	 * return singleton object of NewUpdateProccessHelper.
	 * @return
	 */
	public static NewUpdateProccessHelper getInstance() {
		if (newUpdateProccessHelper == null) {
			newUpdateProccessHelper = new NewUpdateProccessHelper();
		}
		return newUpdateProccessHelper;
	}
	ExamUpdateProcessImpl impl = new ExamUpdateProcessImpl();
	

	public String getQueryForBatchYear() throws Exception{
		String query="select c.year" +
					" from ExamDefinition e" +
					" join e.courseSchemeDetails courseDetails" +
					" join courseDetails.course.classes classes" +
					" join classes.classSchemewises classwise" +
					" join classwise.curriculumSchemeDuration.curriculumScheme c" +
					" where e.delIsActive=1 group by c.year order by c.year";
		return query;
	}
	
	
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public String getqueryForSuppliementaryDataCreation(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		String query="select classwise.curriculumSchemeDuration.academicYear," +
				" c.year,classes" +
				" from ExamDefinition e" +
				" join e.courseSchemeDetails courseDetails" +
				" join courseDetails.course.classes classes" +
				" join classes.classSchemewises classwise" +
				" join classwise.curriculumSchemeDuration.curriculumScheme c" +
				" where  e.delIsActive=1 and e.id="+newUpdateProccessForm.getExamId();
		if(newUpdateProccessForm.getBatchYear()!=null && !newUpdateProccessForm.getBatchYear().isEmpty()){
			query=query+" and c.year="+newUpdateProccessForm.getBatchYear();
		}
		if(newUpdateProccessForm.getProcess().equalsIgnoreCase("3")){
			query=query+" and classwise.curriculumSchemeDuration.academicYear>=e.examForJoiningBatch" ;
		}else if(newUpdateProccessForm.getProcess().equalsIgnoreCase("2")){
			query=query+" and classwise.curriculumSchemeDuration.academicYear=e.academicYear" ;
		}
		query=query+" and classes.termNumber=courseDetails.schemeNo" +
				" and classes.isActive=1 order by classes.name";
		return query;
	}
			
	public Map<Integer, String> convertBatchBoListToTOList(List list) throws Exception {
		
		Map<Integer, String> batchmap=new HashMap<Integer, String>();
		int batch=0;
		if(list!=null && !list.isEmpty()){
			Iterator itr=list.iterator();
			while (itr.hasNext()) {
				Object object = (Object) itr.next();
				if(object != null)
					batch=(Integer.parseInt(object.toString()));
					
				batchmap.put(batch, String.valueOf(batch));
			}
		}
		batchmap = (Map<Integer, String>) CommonUtil.sortMapByValue(batchmap);
		return batchmap;
	}
	/**
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<ClassesTO> convertBoListToTOList(List list) throws Exception {
		List<ClassesTO> mainList=new ArrayList<ClassesTO>();
		if(list!=null && !list.isEmpty()){
			Iterator itr=list.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				ClassesTO to=new ClassesTO();
				if(object[0]!=null)
					to.setYear(Integer.parseInt(object[0].toString()));
				if(object[1]!=null)
					to.setBatchYear(Integer.parseInt(object[1].toString()));
				if(object[2]!=null){
					Classes c=(Classes)object[2];
					to.setId(c.getId());
					to.setClassName(c.getName());
					to.setTermNo(c.getTermNumber());
					to.setCourseId(c.getCourse().getId());
					//raghu adding ug/pg
					to.setSectionName(c.getCourse().getProgram().getProgramType().getName());
				}
				mainList.add(to);	
			}
		}
		return mainList;
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public List<StudentSupplementaryImprovementApplication> getBoListFromToList(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
		List<StudentSupplementaryImprovementApplication> boList=new ArrayList<StudentSupplementaryImprovementApplication>();
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		boolean isImprovement=false;
		if(classList!=null && !classList.isEmpty()){
			Iterator<ClassesTO> itr=classList.iterator();
			while (itr.hasNext()) {
				ClassesTO to = (ClassesTO) itr.next();
				if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
					Map<String,SubjectMarksTO> minMarks=transaction.getMinMarksMap(to);
					List<Integer> excludedList =  impl.getExcludedFromResultSubjects(to.getCourseId(), to.getTermNo(),to.getYear());
					List<Integer> failureExcludeList = impl.getExcludedFromTotResultSubjects(to.getCourseId(), to.getTermNo(),to.getYear());
					boolean isDelted=transaction.deleteOldRecords(to.getId(),newUpdateProccessForm.getExamId());
					Map<String, Boolean> stuImpMap = transaction.getSuppImpExamDetails(to.getId());
					if(isDelted){
						boolean isMaxRecord=false;
						if(to.getCourseId()!=18){
							isMaxRecord=true;
						}
						List<StudentTO> studentList=getStudentListForClass(to.getId());
						if(studentList!=null && !studentList.isEmpty()){
							// The Real Code Here Only Starts
							Iterator<StudentTO> stuItr=studentList.iterator();
							while (stuItr.hasNext()) {
								StudentTO studentTO = (StudentTO) stuItr.next();
								//if(studentTO.getId()==2534){//remove this
								Map<Integer,StudentMarkDetailsTO> totSubMap=new HashMap<Integer, StudentMarkDetailsTO>();//subject and marks details to verify max or latest and keep in the map at last
								BigDecimal requiredAggrePercentage = impl.getAggregatePassPercentage(to.getCourseId());
								boolean checkTotal=true;
								double stuTotalMarks=0;
								double subTotalMarks=0;
								List<Object[]> list=transaction.getDataByStudentAndClassId(studentTO.getId(),to.getId(),studentTO.getSubjectIdList(),studentTO.getAppliedYear());
								if(list!=null && !list.isEmpty()){
									Iterator<Object[]> marksItr=list.iterator();
									
									while (marksItr.hasNext()) {
										Object[] objects = (Object[]) marksItr.next();
										if(objects[9]!=null && minMarks.containsKey(objects[9].toString())){
											boolean isAddTotal=true;
											if(excludedList.contains(Integer.parseInt(objects[9].toString()))){
												isAddTotal=false;
											}
											SubjectMarksTO subTo=minMarks.get(objects[9].toString());
											StudentMarkDetailsTO markDetailsTO= new StudentMarkDetailsTO();
											if(objects[4]!=null)
												markDetailsTO.setStudentId(Integer.parseInt(objects[4].toString()));
											if(objects[9]!=null)
												markDetailsTO.setSubjectId(Integer.parseInt(objects[9].toString()));
											double theoryRegMark=0;
											boolean isTheoryAlpha=false;
											if (objects[15] != null || objects[16] != null) {
												if(objects[15]!=null){
													if(!StringUtils.isAlpha(objects[15].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[15].toString());
													else
														isTheoryAlpha=true;
												}
												if(objects[16]!=null){
													if(!StringUtils.isAlpha(objects[16].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[16].toString());
													else
														isTheoryAlpha=true;
												}
											}else{
												if(objects[11]!=null){
													if(!StringUtils.isAlpha(objects[11].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[11].toString());
													else
														isTheoryAlpha=true;
												}
												if(objects[12]!=null){
													if(!StringUtils.isAlpha(objects[12].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[12].toString());
													else
														isTheoryAlpha=true;
												}
											}
											if(!isTheoryAlpha)
												markDetailsTO.setStuTheoryIntMark(String.valueOf(theoryRegMark));
											else
												markDetailsTO.setStuTheoryIntMark("Ab");
											double practicalRegMark=0;
											boolean isPracticalAlpha=false;
											if (objects[17] != null || objects[18] != null) {
												if(objects[17]!=null){
													if(!StringUtils.isAlpha(objects[17].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[17].toString());
													else
														isPracticalAlpha=true;
												}if(objects[18]!=null){
													if(!StringUtils.isAlpha(objects[18].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[18].toString());
													else
														isPracticalAlpha=true;
												}
											}else{
												if(objects[13]!=null){
													if(!StringUtils.isAlpha(objects[13].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[13].toString());
													else
														isPracticalAlpha=true;
												}
												if(objects[14]!=null){
													if(!StringUtils.isAlpha(objects[14].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[14].toString());
													else
														isPracticalAlpha=true;
												}
											}
											if(!isPracticalAlpha)
												markDetailsTO.setStuPracIntMark(String.valueOf(practicalRegMark));
											else
												markDetailsTO.setStuPracIntMark("Ab");
											
											if (objects[19] != null) {
												markDetailsTO.setStuTheoryRegMark(objects[19].toString());
											}
											if (objects[20] != null) {
												markDetailsTO.setStuPracRegMark(objects[20].toString());
											}
											if (objects[22] != null) {
												markDetailsTO.setIs_theory_practical(objects[22].toString());
											}
											isImprovement=false;
											if (objects[23] != null) {
												isImprovement=Boolean.parseBoolean(objects[23].toString());
												if(isImprovement){
													markDetailsTO.setPassRegular("yes");
												}
											}
											if(isMaxRecord){
												if(totSubMap.containsKey(markDetailsTO.getSubjectId())){
													StudentMarkDetailsTO markDetailsTO2=totSubMap.remove(markDetailsTO.getSubjectId());
													StudentMarkDetailsTO maxMarks=checkMaxBetweenTOs(markDetailsTO,markDetailsTO2,subTo,subTotalMarks,stuTotalMarks,isAddTotal,isImprovement);
													totSubMap.put(markDetailsTO.getSubjectId(),maxMarks);
												}else{
													totSubMap.put(markDetailsTO.getSubjectId(),markDetailsTO);
												}
											}else{
												if(!totSubMap.containsKey(markDetailsTO.getSubjectId())){
													totSubMap.put(markDetailsTO.getSubjectId(),markDetailsTO);
												}
											}
										}
									}
									
									// The Real Logic comes now ( New Logic has Implemented)
									List<StudentMarkDetailsTO> totalList = new ArrayList<StudentMarkDetailsTO>(totSubMap.values());

									if(!totalList.isEmpty()){
										Iterator<StudentMarkDetailsTO> totalitr=totalList.iterator();
										while (totalitr.hasNext()) {
											StudentMarkDetailsTO markDetailsTO = totalitr .next();
											if(minMarks.containsKey(String.valueOf(markDetailsTO.getSubjectId()))){
												boolean isAddTotal=true;
												if(excludedList.contains(markDetailsTO.getSubjectId())){
													isAddTotal=false;
												}
												SubjectMarksTO subTo=minMarks.get(String.valueOf(markDetailsTO.getSubjectId()));
												// The Real Logic has to implement Here
												if(subTo.getTheoryIntMin()!=null && !subTo.getTheoryIntMin().isEmpty()){
													if (markDetailsTO.getStuTheoryIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
															checkTotal=false;
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double
																.parseDouble(subTo.getTheoryIntMin())) {
															checkTotal=false;
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
													else{
														checkTotal=false;
														markDetailsTO.setIsTheoryFailed(true);
													}
												}
												if(subTo.getTheoryRegMin()!=null && !subTo.getTheoryRegMin().isEmpty()){
													if (markDetailsTO.getStuTheoryRegMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark()) < Double
																.parseDouble(subTo.getTheoryRegMin())) {
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
													else{
														markDetailsTO.setIsTheoryFailed(true);
													}
												}
												if(subTo.getPracticalIntMin()!=null && !subTo.getPracticalIntMin().isEmpty()){
													if (markDetailsTO.getStuPracIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
																.parseDouble(subTo.getPracticalIntMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
													else{
														markDetailsTO.setIsPracticalFailed(true);
													}
												}
												if(subTo.getPracticalRegMin()!=null && !subTo.getPracticalRegMin().isEmpty()){
													if (markDetailsTO.getStuPracRegMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
															markDetailsTO.setIsPracticalFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracRegMark()) < Double
																.parseDouble(subTo.getPracticalRegMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
													else{
														markDetailsTO.setIsPracticalFailed(true);
													}
												}

												if (subTo.getFinalTheoryMin() != null) {
													if(isAddTotal){
														if(subTo.getFinalTheoryMarks()!=null)
															subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalTheoryMarks());
														if(markDetailsTO.getStuTheoryRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryRegMark());
														}
														if(markDetailsTO.getStuTheoryIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryIntMark());
														}
													}
													if (markDetailsTO.getStuTheoryRegMark() != null
															&& markDetailsTO.getStuTheoryIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim()) ){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark())+ 
																Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double.parseDouble(subTo.getFinalTheoryMin())) {
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
												}
												if (subTo.getFinalPracticalMin() != null) {
													if(isAddTotal){
														if(subTo.getFinalPracticalMarks()!=null)
															subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalPracticalMarks());
														if(markDetailsTO.getStuPracRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracRegMark());
														}
														if(markDetailsTO.getStuPracIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracIntMark());
														}
													}
													if (markDetailsTO.getStuPracRegMark() != null && markDetailsTO.getStuPracIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
															markDetailsTO.setIsPracticalFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracRegMark())
																+ Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
																.parseDouble(subTo.getFinalPracticalMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
												}
												if (subTo.getSubjectFinalMinimum() != null) {
													if(isAddTotal){
														if(subTo.getFinalTheoryMarks()!=null)
															subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalTheoryMarks());
														if(markDetailsTO.getStuTheoryRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryRegMark());
														}
														if(markDetailsTO.getStuTheoryIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
															stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryIntMark());
														}
													}
													if (markDetailsTO.getStuTheoryRegMark() != null
															&& markDetailsTO.getStuTheoryIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim()) ){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark())+ 
																Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double.parseDouble(subTo.getSubjectFinalMinimum())) {
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
												 }
												if(((markDetailsTO.getIsTheoryFailed()!=null && markDetailsTO.getIsTheoryFailed()) || (markDetailsTO.getIsPracticalFailed()!=null && markDetailsTO.getIsPracticalFailed())) && !failureExcludeList.contains(markDetailsTO.getSubjectId())){
													checkTotal=false;
												}
											}
										}
									}
									
									if(!totSubMap.isEmpty()){
										if(checkTotal){
											// percentage Calculation should be done here
											boolean isAverage=false;
											Double studentPercentage =Double.valueOf(0);
											if(subTotalMarks>0)
											studentPercentage= Double.valueOf((stuTotalMarks * 100) / subTotalMarks);
											if (requiredAggrePercentage != null	&& studentPercentage != null) {
												if ((new BigDecimal(studentPercentage.toString()).intValue()) < (new BigDecimal(requiredAggrePercentage.toString()).intValue())) {
													isAverage = true;	
												}
											}
											if(isAverage){
												Iterator it = totSubMap.entrySet().iterator();
											    while (it.hasNext()) {}
											}
										}else{
											Iterator it = totSubMap.entrySet().iterator();
											StudentMarkDetailsTO sto=null; 
										    while (it.hasNext()) {
										        Map.Entry pairs = (Map.Entry)it.next();
										        sto=(StudentMarkDetailsTO)pairs.getValue();
										        if( stuImpMap.size()==0 || (stuImpMap.containsKey(studentTO.getId()+"_"+sto.getSubjectId())&& !stuImpMap.get(studentTO.getId()+"_"+sto.getSubjectId()))|| !stuImpMap.containsKey(studentTO.getId()+"_"+sto.getSubjectId())){

										        if(((sto.getIsTheoryFailed()!=null && sto.getIsTheoryFailed()) || (sto.getIsPracticalFailed()!=null && sto.getIsPracticalFailed())) && !failureExcludeList.contains(sto.getSubjectId())){
										        	StudentSupplementaryImprovementApplication bo=new StudentSupplementaryImprovementApplication();
										        	Student student=new Student();
										        	student.setId(studentTO.getId());
										        	bo.setStudent(student);
										        	if(sto.getIsTheoryFailed()!=null && sto.getIsTheoryFailed()){
										        		bo.setIsFailedTheory(true);
										        	}else{
										        		bo.setIsFailedTheory(false);
										        	}
										        	if(sto.getIsPracticalFailed()!=null && sto.getIsPracticalFailed()){
										        		bo.setIsFailedPractical(true);
										        	}else{
										        		bo.setIsFailedPractical(false);
										        	}
										        	bo.setIsSupplementary(true);
										        	bo.setIsAppearedTheory(false);
										        	bo.setIsAppearedPractical(false);
										        	bo.setIsImprovement(false);
										        	ExamDefinition exam=new ExamDefinition();
										        	exam.setId(Integer.parseInt(newUpdateProccessForm.getExamId()));
										        	bo.setExamDefinition(exam);
										        	Subject subject=new Subject();
										        	subject.setId(sto.getSubjectId());
										        	bo.setSubject(subject);
										        	Classes classes=new Classes();
										        	classes.setId(to.getId());
										        	bo.setClasses(classes);
										        	bo.setSchemeNo(to.getTermNo());
										        	bo.setCreatedBy(newUpdateProccessForm.getUserId());
										        	bo.setCreatedDate(new Date());
										        	bo.setModifiedBy(newUpdateProccessForm.getUserId());
										        	bo.setLastModifiedDate(new Date());
										        	bo.setIsOnline(false);
										        	boList.add(bo);
										        }
										        }
										    }
										}
									}
								}
							//}//remove this
						}
						}
					}
				}
			}
		}
		return boList;
	}

	/**
	 * @param markDetailsTO1
	 * @param markDetailsTO2
	 * @throws Exception
	 */
	public StudentMarkDetailsTO checkMaxBetweenTOs(StudentMarkDetailsTO to1,StudentMarkDetailsTO to2,SubjectMarksTO subTo,double subTotalMarks,double stuTotalMarks,boolean isAddTotal,boolean isImprovement) throws Exception {
		
		StudentMarkDetailsTO  markDetailsTO =new StudentMarkDetailsTO();
		if(to2.getPassRegular()!=null && to2.getPassRegular().equalsIgnoreCase("yes")){
			isImprovement=true;
		}
		if(to1.getPassRegular()!=null && to1.getPassRegular().equalsIgnoreCase("yes")){
			isImprovement=true;
		}

		if(isAddTotal){
			//everything two times we have to deticate in subTotalMarks  because we are adding two times for two TO'S
			if(subTo.getFinalPracticalMarks()!=null && !subTo.getFinalPracticalMarks().isEmpty()){
				subTotalMarks=subTotalMarks-Double.parseDouble(subTo.getFinalPracticalMarks());// For To1 subraction
				subTotalMarks=subTotalMarks-Double.parseDouble(subTo.getFinalPracticalMarks());// For To2 subraction
				if (to1.getStuPracIntMark() != null && !StringUtils.isAlpha(to1.getStuPracIntMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to1.getStuPracIntMark());
				}
				if (to2.getStuPracIntMark() != null && !StringUtils.isAlpha(to2.getStuPracIntMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to2.getStuPracIntMark());
				}
				if(to1.getStuPracRegMark() != null && !StringUtils.isAlpha(to1.getStuPracRegMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to1.getStuPracRegMark());
				}
				if(to2.getStuPracRegMark() != null && !StringUtils.isAlpha(to2.getStuPracRegMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to2.getStuPracRegMark());
				}
			}
			if(subTo.getFinalTheoryMarks()!=null && !subTo.getFinalTheoryMarks().isEmpty()){
				subTotalMarks=subTotalMarks-Double.parseDouble(subTo.getFinalTheoryMarks());// For To1 subraction
				subTotalMarks=subTotalMarks-Double.parseDouble(subTo.getFinalTheoryMarks());// For To2 subraction
				if (to1.getStuTheoryIntMark() != null && !StringUtils.isAlpha(to1.getStuTheoryIntMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to1.getStuTheoryIntMark());
				}
				if (to2.getStuTheoryIntMark() != null && !StringUtils.isAlpha(to2.getStuTheoryIntMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to2.getStuTheoryIntMark());
				}
				if(to1.getStuTheoryRegMark() != null && !StringUtils.isAlpha(to1.getStuTheoryRegMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to1.getStuTheoryRegMark());
				}
				if(to2.getStuTheoryRegMark() != null && !StringUtils.isAlpha(to2.getStuTheoryRegMark().trim())){
					stuTotalMarks=stuTotalMarks-Double.parseDouble(to2.getStuTheoryRegMark());
				}
			}
		}
		// The Real Logic has to implement Here
		if(to1!=null && to2!=null){
			markDetailsTO.setSubjectId(to1.getSubjectId());
			markDetailsTO.setIs_theory_practical(to1.getIs_theory_practical());
			if(isImprovement){
				// If it is improvement then we have to get max between the two records
				// Theory Int Marks
				if(to1.getStuTheoryIntMark()!=null && !to1.getStuTheoryIntMark().isEmpty()){
					if(StringUtils.isAlpha(to1.getStuTheoryIntMark().trim())){
						if(to2.getStuTheoryIntMark()!=null && !to2.getStuTheoryIntMark().isEmpty() && !StringUtils.isAlpha(to2.getStuTheoryIntMark().trim())){
							markDetailsTO.setStuTheoryIntMark(to2.getStuTheoryIntMark());
						}else{
							markDetailsTO.setStuTheoryIntMark(to1.getStuTheoryIntMark());
						}
					}else{
						if(to2.getStuTheoryIntMark()!=null && !to2.getStuTheoryIntMark().isEmpty() && !StringUtils.isAlpha(to2.getStuTheoryIntMark().trim())){
							if(Double.parseDouble(to1.getStuTheoryIntMark()) < Double.parseDouble(to2.getStuTheoryIntMark()))
								markDetailsTO.setStuTheoryIntMark(to2.getStuTheoryIntMark());
							else
								markDetailsTO.setStuTheoryIntMark(to1.getStuTheoryIntMark());
						}else{
							markDetailsTO.setStuTheoryIntMark(to1.getStuTheoryIntMark());
						}
					}
				}else{
					if(to2.getStuTheoryIntMark()!=null && !to2.getStuTheoryIntMark().isEmpty()){
						markDetailsTO.setStuTheoryIntMark(to2.getStuTheoryIntMark());
					}
				}
				
				// Practical Int Mark
				if(to1.getStuPracIntMark()!=null && !to1.getStuPracIntMark().isEmpty()){
					if(StringUtils.isAlpha(to1.getStuPracIntMark().trim())){
						if(to2.getStuPracIntMark()!=null && !to2.getStuPracIntMark().isEmpty() && !StringUtils.isAlpha(to2.getStuPracIntMark().trim())){
							markDetailsTO.setStuPracIntMark(to2.getStuPracIntMark());
						}else{
							markDetailsTO.setStuPracIntMark(to1.getStuPracIntMark());
						}
					}else{
						if(to2.getStuPracIntMark()!=null && !to2.getStuPracIntMark().isEmpty() && !StringUtils.isAlpha(to2.getStuPracIntMark().trim())){
							if(Double.parseDouble(to1.getStuPracIntMark()) < Double.parseDouble(to2.getStuPracIntMark()))
								markDetailsTO.setStuPracIntMark(to2.getStuPracIntMark());
							else
								markDetailsTO.setStuPracIntMark(to1.getStuPracIntMark());
						}else{
							stuTotalMarks=stuTotalMarks-Double.parseDouble(to1.getStuPracIntMark());
							markDetailsTO.setStuPracIntMark(to1.getStuPracIntMark());
						}
					}
				}else{
					if(to2.getStuPracIntMark()!=null && !to2.getStuPracIntMark().isEmpty()){
						markDetailsTO.setStuPracIntMark(to2.getStuPracIntMark());
					}
				}
				
				// Theory Reg Mark
				if(to1.getStuTheoryRegMark()!=null && !to1.getStuTheoryRegMark().isEmpty()){
					if(StringUtils.isAlpha(to1.getStuTheoryRegMark().trim())){
						if(to2.getStuTheoryRegMark()!=null && !to2.getStuTheoryRegMark().isEmpty() && !StringUtils.isAlpha(to2.getStuTheoryRegMark().trim())){
							markDetailsTO.setStuTheoryRegMark(to2.getStuTheoryRegMark());
						}else{
							markDetailsTO.setStuTheoryRegMark(to1.getStuTheoryRegMark());
						}
					}else{
						if(to2.getStuTheoryRegMark()!=null && !to2.getStuTheoryRegMark().isEmpty() && !StringUtils.isAlpha(to2.getStuTheoryRegMark().trim())){
							if(Double.parseDouble(to1.getStuTheoryRegMark()) < Double.parseDouble(to2.getStuTheoryRegMark()))
								markDetailsTO.setStuTheoryRegMark(to2.getStuTheoryRegMark());
							else
								markDetailsTO.setStuTheoryRegMark(to1.getStuTheoryRegMark());
						}else{
							markDetailsTO.setStuTheoryRegMark(to1.getStuTheoryRegMark());
						}
					}
				}else{
					if(to2.getStuTheoryRegMark()!=null && !to2.getStuTheoryRegMark().isEmpty()){
						markDetailsTO.setStuTheoryRegMark(to2.getStuTheoryRegMark());
					}
				}
				
				
				// practical Reg Mark
				if(to1.getStuPracRegMark()!=null && !to1.getStuPracRegMark().isEmpty()){
					if(StringUtils.isAlpha(to1.getStuPracRegMark().trim())){
						if(to2.getStuPracRegMark()!=null && !to2.getStuPracRegMark().isEmpty() && !StringUtils.isAlpha(to2.getStuPracRegMark().trim())){
							markDetailsTO.setStuPracRegMark(to2.getStuPracRegMark());
						}else{
							markDetailsTO.setStuPracRegMark(to1.getStuPracRegMark());
						}
					}else{
						if(to2.getStuPracRegMark()!=null && !to2.getStuPracRegMark().isEmpty() && !StringUtils.isAlpha(to2.getStuPracRegMark().trim())){
							if(Double.parseDouble(to1.getStuPracRegMark()) < Double.parseDouble(to2.getStuPracRegMark()))
								markDetailsTO.setStuPracRegMark(to2.getStuPracRegMark());
							else
								markDetailsTO.setStuPracRegMark(to1.getStuPracRegMark());
						}else{
							markDetailsTO.setStuPracRegMark(to1.getStuPracRegMark());
						}
					}
				}else{
					if(to2.getStuPracRegMark()!=null && !to2.getStuPracRegMark().isEmpty()){
						markDetailsTO.setStuPracRegMark(to2.getStuPracRegMark());
					}
				}
			}else{
				// If it is supplementary then we have to get latest availablity data
				
				// Theory Int Marks
				if(to2.getStuTheoryIntMark()==null || to2.getStuTheoryIntMark().isEmpty() || StringUtils.isAlpha(to2.getStuTheoryIntMark().trim())){
					markDetailsTO.setStuTheoryIntMark(to1.getStuTheoryIntMark());
				}else{
					markDetailsTO.setStuTheoryIntMark(to2.getStuTheoryIntMark());
				}
				
				// Practical Int Mark
				if(to2.getStuPracIntMark()==null || to2.getStuPracIntMark().isEmpty() || StringUtils.isAlpha(to2.getStuPracIntMark().trim())){
					markDetailsTO.setStuPracIntMark(to1.getStuPracIntMark());
				}else{
					markDetailsTO.setStuPracIntMark(to2.getStuPracIntMark());
				}
				
				// Theory Reg Mark
				if(to2.getStuTheoryRegMark()==null || to2.getStuTheoryRegMark().isEmpty() || StringUtils.isAlpha(to2.getStuTheoryRegMark().trim())){
					markDetailsTO.setStuTheoryRegMark(to1.getStuTheoryRegMark());
				}else{
					markDetailsTO.setStuTheoryRegMark(to2.getStuTheoryRegMark());
				}
				
				
				// practical Reg Mark
				if(to2.getStuPracRegMark()==null || to2.getStuPracRegMark().isEmpty() || StringUtils.isAlpha(to2.getStuPracRegMark().trim())){
					markDetailsTO.setStuPracRegMark(to1.getStuPracRegMark());
				}else{
					markDetailsTO.setStuPracRegMark(to2.getStuPracRegMark());
				}
				
			}
			if(subTo.getTheoryIntMin()!=null && !subTo.getTheoryIntMin().isEmpty()){
				if (markDetailsTO.getStuTheoryIntMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
						markDetailsTO.setIsTheoryFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double
							.parseDouble(subTo.getTheoryIntMin())) {
						markDetailsTO.setIsTheoryFailed(true);
					}
				}
				else{
					markDetailsTO.setIsTheoryFailed(true);
				}
			}
			if(subTo.getTheoryRegMin()!=null && !subTo.getTheoryRegMin().isEmpty()){
				if (markDetailsTO.getStuTheoryRegMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
						markDetailsTO.setIsTheoryFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark()) < Double
							.parseDouble(subTo.getTheoryRegMin())) {
						markDetailsTO.setIsTheoryFailed(true);
					}
				}
				else{
					markDetailsTO.setIsTheoryFailed(true);
				}
			}
			if(subTo.getPracticalIntMin()!=null && !subTo.getPracticalIntMin().isEmpty()){
				if (markDetailsTO.getStuPracIntMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
						markDetailsTO.setIsPracticalFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
							.parseDouble(subTo.getPracticalIntMin())) {
						markDetailsTO.setIsPracticalFailed(true);
					}
				}
				else{
					markDetailsTO.setIsPracticalFailed(true);
				}
			}
			if(subTo.getPracticalRegMin()!=null && !subTo.getPracticalRegMin().isEmpty()){
				if (markDetailsTO.getStuPracRegMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
						markDetailsTO.setIsPracticalFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuPracRegMark()) < Double
							.parseDouble(subTo.getPracticalRegMin())) {
						markDetailsTO.setIsPracticalFailed(true);
					}
				}
				else{
					markDetailsTO.setIsPracticalFailed(true);
				}
			}
			
			if (subTo.getFinalTheoryMin() != null) {
				if(isAddTotal){
					if(subTo.getFinalTheoryMarks()!=null)
						subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalTheoryMarks());
					if(markDetailsTO.getStuTheoryRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
						stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryRegMark());
					}
					if(markDetailsTO.getStuTheoryIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
						stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryIntMark());
					}
				}
				if (markDetailsTO.getStuTheoryRegMark() != null
						&& markDetailsTO.getStuTheoryIntMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim()) ){
						markDetailsTO.setIsTheoryFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark())+ 
							Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double.parseDouble(subTo.getFinalTheoryMin())) {
						markDetailsTO.setIsTheoryFailed(true);
					}
				}
			 }
			if (subTo.getFinalPracticalMin() != null) {
				if(isAddTotal){
					if(subTo.getFinalPracticalMarks()!=null)
						subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalPracticalMarks());
					if(markDetailsTO.getStuPracRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
						stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracRegMark());
					}
					if(markDetailsTO.getStuPracIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
						stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracIntMark());
					}
				}
				if (markDetailsTO.getStuPracRegMark() != null && markDetailsTO.getStuPracIntMark() != null){
					if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
						markDetailsTO.setIsPracticalFailed(true);
					}
					else if (Double.parseDouble(markDetailsTO.getStuPracRegMark())
							+ Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
							.parseDouble(subTo.getFinalPracticalMin())) {
						markDetailsTO.setIsPracticalFailed(true);
					}
				}
			}
		}
		markDetailsTO.setTempStuTotal(stuTotalMarks);
		markDetailsTO.setTempSubTotal(subTotalMarks);
		return markDetailsTO;
	}
	/**
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	
	private List<StudentTO> getStudentListForClass(int classId) throws Exception {
		List<StudentTO> studentList=new ArrayList<StudentTO>();
		String query=getCurrentClassQuery(classId);// Getting Current Class Students Query
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<Student> currentStudentList=transaction.getDataForQuery(query);
		getFinalStudentsForCurrentClass(currentStudentList,studentList);//Adding current Class Students for StudentList
		String preQuery=getPreviousClassQuery(classId);
		List<Object[]> previousStudentList=transaction.getDataForQuery(preQuery);
		getFinalStudentsForPreviousClass(previousStudentList,studentList);
		return studentList;
	}
	@SuppressWarnings("unused")
	private List<StudentTO> getStudentListForClassExamId(int classId) throws Exception {
		List<StudentTO> studentList=new ArrayList<StudentTO>();
		String query=getCurrentClassQuery(classId);// Getting Current Class Students Query
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<Student> currentStudentList=transaction.getDataForQuery(query);
		getFinalStudentsForCurrentClass(currentStudentList,studentList);//Adding current Class Students for StudentList
		String preQuery=getPreviousClassQuery(classId);
		List<Object[]> previousStudentList=transaction.getDataForQuery(preQuery);
		getFinalStudentsForPreviousClass(previousStudentList,studentList);
		return studentList;
	}
	/**
	 * @param previousStudentList
	 * @param studentList
	 * @throws Exception
	 */
	private void getFinalStudentsForPreviousClass(List<Object[]> previousStudentList, List<StudentTO> studentList) throws Exception{
		Map<Integer,StudentTO> studentMap=new HashMap<Integer, StudentTO>();
		if(previousStudentList!=null && !previousStudentList.isEmpty()){
			Iterator<Object[]> preItr=previousStudentList.iterator();
			while (preItr.hasNext()) {
				Object[] obj = (Object[]) preItr.next();
				if(obj[0]!=null && obj[1]!=null){
					if(studentMap.containsKey(Integer.parseInt(obj[0].toString()))){
						StudentTO to=studentMap.remove(Integer.parseInt(obj[0].toString()));
						to.setId(Integer.parseInt(obj[0].toString()));
						if(obj[3]!=null)
						to.setAppliedYear(Integer.parseInt(obj[3].toString()));
						if(obj[2]!=null)
						to.setRegisterNo(obj[2].toString());
						List<SubjectTO> subList=to.getSubjectList();
						List<Integer> subIdList=to.getSubjectIdList();
						Subject subject=(Subject)obj[1];
						SubjectTO subTo=new SubjectTO();
						subTo.setId(subject.getId());
						subTo.setName(subject.getName());
						subList.add(subTo);
						subIdList.add(subject.getId());
						to.setSubjectList(subList);
						to.setSubjectIdList(subIdList);
						studentMap.put(to.getId(),to);
					}else{
						StudentTO to=new StudentTO();
						to.setId(Integer.parseInt(obj[0].toString()));
						if(obj[2]!=null)
							to.setRegisterNo(obj[2].toString());
						if(obj[3]!=null)
							to.setAppliedYear(Integer.parseInt(obj[3].toString()));
						List<SubjectTO> subList=new ArrayList<SubjectTO>();
						List<Integer> subIdList=new ArrayList<Integer>();
						Subject subject=(Subject)obj[1];
						SubjectTO subTo=new SubjectTO();
						subTo.setId(subject.getId());
						subTo.setName(subject.getName());
						subList.add(subTo);
						subIdList.add(subject.getId());
						to.setSubjectIdList(subIdList);
						to.setSubjectList(subList);
						studentMap.put(to.getId(),to);
					}
				}
			}
			studentList.addAll(studentMap.values());
		}
	}
	/**
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	private String getPreviousClassQuery(int classId)  throws Exception{
		String query="select s.id,subSet.subject,s.registerNo,s.admAppln.appliedYear from Student s" +
				" join s.studentPreviousClassesHistory classHis" +
				" join classHis.classes.classSchemewises classSchemewise" +
				" join classSchemewise.curriculumSchemeDuration cd" +
				" join s.studentSubjectGroupHistory subjHist " +
				" join subjHist.subjectGroup.subjectGroupSubjectses subSet" +
				" where s.admAppln.isCancelled=0 and s.isAdmitted=1 and subSet.isActive=1 and classHis.classes.id=" +classId+
				" and s.classSchemewise.classes.id <> "+classId+
				" and classHis.schemeNo=subjHist.schemeNo" ;
		return query;
	}
	/**
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	private String getCurrentClassQuery(int classId) throws Exception{
		String query="from Student s" +
				" where s.admAppln.isCancelled=0 and s.isAdmitted=1 " +
				" and s.classSchemewise.classes.id="+classId;
		return query;
	}
	/**
	 * @param currentStudentList
	 * @param studentList
	 * @throws Exception
	 */
	private void getFinalStudentsForCurrentClass(List<Student> currentStudentList, List<StudentTO> studentList) throws Exception{
		if(currentStudentList!=null && !currentStudentList.isEmpty()){
			Iterator<Student> itr=currentStudentList.iterator();
			while (itr.hasNext()) {
				Student bo = (Student) itr.next();
				StudentTO to=new StudentTO();
				if (bo.getId()==49560) {
					System.out.println("");
				}
				to.setId(bo.getId());
				to.setAppliedYear(bo.getAdmAppln().getAppliedYear());
				to.setRegisterNo(bo.getRegisterNo());
				Set<ApplicantSubjectGroup> subSet=bo.getAdmAppln().getApplicantSubjectGroups();
				List<SubjectTO> subList=new ArrayList<SubjectTO>();
				List<Integer> subIdList=new ArrayList<Integer>();
				if(subSet!=null && !subSet.isEmpty()){
					Iterator<ApplicantSubjectGroup> subItr=subSet.iterator();
					while (subItr.hasNext()) {
						ApplicantSubjectGroup subGrp = (ApplicantSubjectGroup) subItr.next();
						Set<SubjectGroupSubjects> sub=subGrp.getSubjectGroup().getSubjectGroupSubjectses();
						if (sub!=null && !sub.isEmpty()) {
							Iterator<SubjectGroupSubjects> subGrpSubItr=sub.iterator();
							while (subGrpSubItr.hasNext()) {
								SubjectGroupSubjects subjectGroupSubjects = (SubjectGroupSubjects) subGrpSubItr.next();
								if(subjectGroupSubjects.getIsActive()){
									SubjectTO subTo=new SubjectTO();
									subTo.setId(subjectGroupSubjects.getSubject().getId());
									subTo.setName(subjectGroupSubjects.getSubject().getName());
									subList.add(subTo);
									subIdList.add(subjectGroupSubjects.getSubject().getId());
								}
							}
						}
						
					}
				}
				to.setSubjectList(subList);
				to.setSubjectIdList(subIdList);
				studentList.add(to);
			}
		}
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 */
	public boolean calculateInternalOverAllAndSaveData(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		ExamDefinition exam=new ExamDefinition();
		exam.setId(Integer.parseInt(newUpdateProccessForm.getExamId()));
		List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
		List<StudentOverallInternalMarkDetails> boList=new ArrayList<StudentOverallInternalMarkDetails>();
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		List<Integer> intExamId=transaction.getInternalExamId(Integer.parseInt(newUpdateProccessForm.getExamId()));
		if(classList!=null && !classList.isEmpty()){
			Iterator<ClassesTO> itr=classList.iterator();
			while (itr.hasNext()) {
				ClassesTO to = (ClassesTO) itr.next();
				if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
					Map<Integer,SubjectRuleSettingsTO> subRuleMap=transaction.getSubjectRuleSettings(to.getCourseId(),to.getYear(),to.getTermNo());
					List<StudentTO> studentList=getStudentListForClass(to.getId());
					if(studentList!=null && !studentList.isEmpty()){
						Iterator<StudentTO> sitr=studentList.iterator();
						while (sitr.hasNext()) {
							StudentTO sto = (StudentTO) sitr.next();
						//if(sto.getId()==9032){//remove this
							Map<String,StudentAttendanceTO> stuAttMap=transaction.getAttendanceForStudent(to.getId(),sto.getId(),sto.getSubjectIdList());
							Map<Integer,StudentAttendanceTO> stuAssMap=transaction.getAssignmentMarksForStudent(to.getCourseId(),sto.getId(),sto.getSubjectIdList());
							if(sto.getSubjectIdList()!=null && !sto.getSubjectIdList().isEmpty()){
								Iterator<Integer> subItr=sto.getSubjectIdList().iterator();
								while (subItr.hasNext()) {
									Integer subId = (Integer) subItr.next();
									//if(subId==197){//remove this
									StudentOverallInternalMarkDetails bo=new StudentOverallInternalMarkDetails();
									Student student=new Student();
									student.setId(sto.getId());
									bo.setStudent(student);
									Classes classes=new Classes();
									classes.setId(to.getId());
									bo.setClasses(classes);
									bo.setExam(exam);
									Subject subject=new Subject();
									subject.setId(subId);
									bo.setSubject(subject);
									double theoryAttendance=0;
									double theoryAssignMarks=0;
									double theoryMarks=0;
									double practicalAttendance=0;
									double practicalAssignMarks=0;
									double practicalMarks=0;
									if(subRuleMap.containsKey(subId)){
										SubjectRuleSettingsTO subTo=subRuleMap.get(subId);
										if(subTo.getIsTheoryPractical().equalsIgnoreCase("T")){
											if(subTo.isTheoryAttendanceCheck()){
												theoryAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getTheoryAttTypeId(),subTo.isTheoryCoLeaveCheck(),subTo.isTheoryLeaveCheck(),to,transaction);
												bo.setTheoryTotalAttendenceMarks(String.valueOf(theoryAttendance));
											}
											if(subTo.isTheoryAssignmentCheck()){
												theoryAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"t");
												bo.setTheoryTotalAssignmentMarks(String.valueOf(theoryAssignMarks));
											}
											if(subTo.isTheoryInteralCheck()){
												theoryMarks=getStudentMarksForSubject(subId,sto.getId(),intExamId,to,"t",transaction,subTo.getTheoryBest(),subTo.isTheoryIndCheck(),exam.getId(),newUpdateProccessForm);
											}
											bo.setTheoryTotalSubInternalMarks(String.valueOf(theoryMarks));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}
											}
											
											bo.setIsGracing(newUpdateProccessForm.getIsGracing());
										}else if(subTo.getIsTheoryPractical().equalsIgnoreCase("P")){
											if(subTo.isPracticalAttendanceCheck()){
												practicalAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getPracticalAttTypeId(),subTo.isPracticalCoLeaveCheck(),subTo.isPracticalLeaveCheck(),to,transaction);
												bo.setPracticalTotalAttendenceMarks(String.valueOf(practicalAttendance));
											}
											if(subTo.isPracticalAssigntmentCheck()){
												practicalAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"p");
												bo.setPracticalTotalAssignmentMarks(String.valueOf(practicalAssignMarks));
											}
											if(subTo.isPracticalInternalCheck()){
												practicalMarks=getStudentMarksForSubject(subId,sto.getId(),intExamId,to,"p",transaction,subTo.getPracticalBest(),subTo.isPracticalIndCheck(),exam.getId(),newUpdateProccessForm);
											}
											bo.setPracticalTotalSubInternalMarks(String.valueOf(practicalMarks));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}
												
												
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}
											}
											
											
											bo.setIsGracing(newUpdateProccessForm.getIsGracing());
										}else if(subTo.getIsTheoryPractical().equalsIgnoreCase("B")){
											if(subTo.isTheoryAttendanceCheck()){
												theoryAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getTheoryAttTypeId(),subTo.isTheoryCoLeaveCheck(),subTo.isTheoryLeaveCheck(),to,transaction);
												bo.setTheoryTotalAttendenceMarks(String.valueOf(theoryAttendance));
											}
											if(subTo.isTheoryAssignmentCheck()){
												theoryAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"t");
												bo.setTheoryTotalAssignmentMarks(String.valueOf(theoryAssignMarks));
											}
											if(subTo.isTheoryInteralCheck()){
												theoryMarks=getStudentMarksForSubject(subId,sto.getId(),intExamId,to,"t",transaction,subTo.getTheoryBest(),subTo.isTheoryIndCheck(),exam.getId(),newUpdateProccessForm);
												bo.setIsGracing(newUpdateProccessForm.getIsGracing());
											}
											bo.setTheoryTotalSubInternalMarks(String.valueOf(theoryMarks));
											if(subTo.isPracticalAttendanceCheck()){
												practicalAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getPracticalAttTypeId(),subTo.isPracticalCoLeaveCheck(),subTo.isPracticalLeaveCheck(),to,transaction);
												bo.setPracticalTotalAttendenceMarks(String.valueOf(practicalAttendance));
											}
											if(subTo.isPracticalAssigntmentCheck()){
												practicalAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"p");
												bo.setPracticalTotalAssignmentMarks(String.valueOf(practicalAssignMarks));
											}
											if(subTo.isPracticalInternalCheck()){
												practicalMarks=getStudentMarksForSubject(subId,sto.getId(),intExamId,to,"p",transaction,subTo.getPracticalBest(),subTo.isPracticalIndCheck(),exam.getId(),newUpdateProccessForm);
												if(bo.getIsGracing()!=null && bo.getIsGracing()){
													bo.setIsGracing(true);
												}else{
													bo.setIsGracing(newUpdateProccessForm.getIsGracing());
												}
											}
											bo.setPracticalTotalSubInternalMarks(String.valueOf(practicalMarks));
											
											bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}
											}
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}
											}
											
										}
										boList.add(bo);
									}
								}
							//}//remove this
							}
						//}// remove this
						}
					}
				}
			}
		}
		
		return transaction.saveInternalOverAll(boList);
	}
	/**
	 * @param subId
	 * @param studentId
	 * @param intExamId
	 * @param to
	 * @param subType
	 * @param transaction
	 * @return
	 * @throws Exception
	 */
	private double getStudentMarksForSubject(Integer subId, int studentId, List<Integer> intExamId, ClassesTO to, String subType,INewUpdateProccessTransaction transaction,int limit,boolean isInd,int examId,NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		
		return transaction.getStudentMarksForSubject(subId,studentId,intExamId,to,subType,limit,isInd,examId,newUpdateProccessForm);
	}
	/**
	 * @param subId
	 * @param stuAssMap
	 * @param subType
	 * @return
	 * @throws Exception
	 */
	private double getAssignMarksForSubject(Integer subId,Map<Integer, StudentAttendanceTO> stuAssMap, String subType) throws Exception {
		double assMarks=0;
		if(stuAssMap.containsKey(subId)){
			StudentAttendanceTO to=stuAssMap.get(subId);
			if(subType.equalsIgnoreCase("t")){
				assMarks=to.getTheoryAssMarks();
			}
			if(subType.equalsIgnoreCase("p")){
				assMarks=to.getPracticalAssMarks();
			}
		}
		return assMarks;
	}
	/**
	 * @param subId
	 * @param stuAttMap
	 * @param theoryAttTypeId
	 * @param theoryCoLeaveCheck
	 * @param theoryLeaveCheck
	 * @param to
	 * @return
	 * @throws Exception
	 */
	private double getAttendanceMarksForSubject(Integer subId, Map<String, StudentAttendanceTO> stuAttMap, 
			int attTypeId, boolean coLeaveCheck, boolean leaveCheck, ClassesTO to,INewUpdateProccessTransaction transaction) throws Exception {
		double points=0;
		if(stuAttMap.containsKey(subId+"_"+attTypeId)){
			StudentAttendanceTO sto=stuAttMap.get(subId+"_"+attTypeId);
			float hoursHeld=sto.getSubjectHoursHeld();
			float hoursAtt=sto.getPresentHoursAtt();
			if(coLeaveCheck)
				hoursAtt=hoursAtt+sto.getCoLeaveHoursAtt();
			if(leaveCheck)
				hoursAtt=hoursAtt+sto.getLeaveHoursAtt();
			float percentage=(hoursAtt/hoursHeld)*100;
			percentage=CommonUtil.roundToDecimal(percentage, 2);
			points=transaction.getAttendanceMarksForPercentage(to.getCourseId(),subId,percentage);
		}
		return points;
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public boolean calculateRegularOverAllAndSaveData(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		try {
			ExamDefinition exam=new ExamDefinition();
			int examId=Integer.parseInt(newUpdateProccessForm.getExamId());
			exam.setId(examId);
			List<String> errorList=new ArrayList<String>();
			List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
			List<StudentFinalMarkDetails> boList=new ArrayList<StudentFinalMarkDetails>();
			INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
			if(classList!=null && !classList.isEmpty()){
				Iterator<ClassesTO> itr=classList.iterator();
				while (itr.hasNext()) {
					ClassesTO to = (ClassesTO) itr.next();
					Classes classes=new Classes();
					classes.setId(to.getId());
					//int SemNo=to.getTermNo();
					if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
//					if(to.getId()==620){// remove this
						
						Map<Integer,SubjectRuleSettingsTO> subRuleMap=transaction.getSubjectRuleSettingsForRegularOverAll(to.getCourseId(),to.getYear(),to.getTermNo());
						List<StudentTO> studentList=getStudentListForClass(to.getId());
//						System.out.println("class Id:"+to.getId());
						if(studentList!=null && !studentList.isEmpty()){
							Iterator<StudentTO> sitr=studentList.iterator();
							while (sitr.hasNext()) {
								StudentTO sto=sitr.next();
//								if(sto.getId()==49288){//remove this
//								System.out.println("student Id"+sto.getId());
								
								if(sto.getSubjectIdList()!=null && !sto.getSubjectIdList().isEmpty()){
									Map<Integer,List<StudentMarksTO>> stuMarksMap=getStudentRegularMarks(sto.getId(),sto.getSubjectIdList(),examId,transaction, to.getId(), newUpdateProccessForm);
									if(newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty()){
										Iterator<Integer> subItr=sto.getSubjectIdList().iterator();
										while (subItr.hasNext()) {
											Integer subId = (Integer) subItr.next();
											if(subRuleMap.containsKey(subId) && stuMarksMap.containsKey(subId)){
												SubjectRuleSettingsTO subSetTo=subRuleMap.get(subId);
												List<StudentMarksTO> marksList=stuMarksMap.get(subId);
												if(subSetTo.isTheoryRegularCheck() || subSetTo.isPracticalRegularCheck()){
													StudentFinalMarkDetails bo=getRegularFinalMarkDetailsBo(subSetTo,marksList,to,sto.getId(),exam,subId,classes,newUpdateProccessForm.getUserId());
													bo.setExam(exam);
													boList.add(bo);
												}else if((subSetTo.isTheoryAnsCheck() && subSetTo.isTheoryEvalCheck()) || (subSetTo.isPracticalAnsCheck() && subSetTo.isPracticalEvalCheck())){
													int noOfAns=0;
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfAns=noOfAns+subSetTo.getTheoryNoOfAns();
													}
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfAns=noOfAns+subSetTo.getPracticalNoOfEval();
													}
													int noOfEvaluation=0;
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfEvaluation=noOfEvaluation+subSetTo.getTheoryNoOfEval();
													}
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfEvaluation=noOfEvaluation+subSetTo.getPracticalNoOfEval();
													}
													if(marksList.size()==(noOfAns*noOfEvaluation)){
														StudentFinalMarkDetails bo=getRegularEvalAndAnsFinalMarkDetailsBo(subSetTo,marksList,to,sto.getId(),exam,subId,classes,newUpdateProccessForm.getUserId());
														bo.setExam(exam);
														boList.add(bo);
													}else{
														errorList.add(sto.getRegisterNo()+"-"+subSetTo.getSubjectName());
													}
												}else if(subSetTo.isTheoryAnsCheck() || subSetTo.isPracticalAnsCheck()){
													int noOfAns=0;
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfAns=noOfAns+subSetTo.getTheoryNoOfAns();
													}
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfAns=noOfAns+subSetTo.getPracticalNoOfEval();
													}
													if(marksList.size()==noOfAns){
														StudentFinalMarkDetails bo=getRegularAnsFinalMarkDetailsBo(subSetTo,marksList,to,sto.getId(),exam,subId,classes,newUpdateProccessForm.getUserId());
														bo.setExam(exam);
														boList.add(bo);
													}else{
														errorList.add(sto.getRegisterNo()+"-"+subSetTo.getSubjectName());
													}
												}else if(subSetTo.isTheoryEvalCheck() || subSetTo.isPracticalEvalCheck()){
													int noOfEvaluation=0;
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfEvaluation=noOfEvaluation+subSetTo.getTheoryNoOfEval();
													}
													if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
														noOfEvaluation=noOfEvaluation+subSetTo.getPracticalNoOfEval();
													}
													if(marksList.size()==noOfEvaluation){
														StudentFinalMarkDetails bo=getRegularEvalFinalMarkDetailsBo(subSetTo,marksList,to,sto.getId(),exam,subId,classes,newUpdateProccessForm.getUserId());
														bo.setExam(exam);
														boList.add(bo);
													}else{
														errorList.add(sto.getRegisterNo()+"-"+subSetTo.getSubjectName());
													}
												}
											}
										}
									}
								}
							}//remove this
							}
						}
//					}//remove THis
				//	}
					
				}
			}
			if(errorList.isEmpty() && (newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty())){
				return transaction.saveRegularOverAll(boList);
			}else{
				newUpdateProccessForm.setErrorList(errorList);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException();
		}
	}
	/**
	 * @param subSetTo
	 * @param marksList
	 * @param to
	 * @param studentId
	 * @param exam
	 * @param subId
	 * @param classes
	 * @param userId
	 * @return
	 */
	private StudentFinalMarkDetails getRegularEvalAndAnsFinalMarkDetailsBo(
			SubjectRuleSettingsTO subSetTo, List<StudentMarksTO> marksList,
			ClassesTO to, int studentId, ExamDefinition exam, Integer subId,
			Classes classes, String userId) {
		StudentFinalMarkDetails bo=new StudentFinalMarkDetails();
		StudentMarksTO mto=new StudentMarksTO();
		Map<String,Double> marksMap=new HashMap<String, Double>();
		boolean isPass=true;
		String theoryAlphaMarks="";
		String practicalAlphaMarks="";
		
		boolean isGracingForEvl1=false;
		boolean isGracingForEvl2=false;
		
		if(marksList!=null && !marksList.isEmpty()){
			Iterator<StudentMarksTO> itr=marksList.iterator();
			while (itr.hasNext()) {
				StudentMarksTO sto = (StudentMarksTO) itr.next();
				if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
					double theoryMarks=0;
					if(marksMap.containsKey(sto.getEvaId()+"_t")){
						theoryMarks=marksMap.remove(sto.getEvaId()+"_t");
					}
					if(sto.getTheoryMarks()!=null && !sto.getTheoryMarks().isEmpty()){
						if(!CommonUtil.isValidDecimal(sto.getTheoryMarks())){
							isPass=false;
							if(theoryAlphaMarks.isEmpty()){
								theoryAlphaMarks=sto.getTheoryMarks();
							}
						}else{
							theoryMarks= theoryMarks+Double.parseDouble(sto.getTheoryMarks());
						}
					}
					marksMap.put(sto.getEvaId()+"_t",theoryMarks);
				}
				if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
					double practicalMarks=0;
					if(marksMap.containsKey(sto.getEvaId()+"_p")){
						practicalMarks=marksMap.remove(sto.getEvaId()+"_p");
					}
					if(sto.getPracticalMarks()!=null && !sto.getPracticalMarks().isEmpty()){
						if(!CommonUtil.isValidDecimal(sto.getPracticalMarks())){
							isPass=false;
							if(practicalAlphaMarks.isEmpty()){
								practicalAlphaMarks=sto.getPracticalMarks();
							}
						}else{
							practicalMarks= practicalMarks+Double.parseDouble(sto.getPracticalMarks());
						}
					}
					marksMap.put(sto.getEvaId()+"_p",practicalMarks);
				}
				// /* code added by chandra
				if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("1")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl1=true;
				}else if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("2")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl2=true;
				}
				// */
			}
		}
		List<Double> theoryList=new ArrayList<Double>();
		List<Double> practicalList=new ArrayList<Double>();
		for (Map.Entry<String, Double> entry : marksMap.entrySet()) {
			if(entry.getKey().contains("_t")){
				theoryList.add(entry.getValue());
			}
			if(entry.getKey().contains("_p")){
				practicalList.add(entry.getValue());
			}
		}
		
		double theoryMarks=0;
		double practicalMarks=0;
		if(isPass){
			if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
				if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Highest")){
					theoryMarks=Collections.max(theoryList);
				}else if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Lowest")){
					theoryMarks=Collections.min(theoryList);
				}else if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Average")){
					int count=0;
					double tMarks=0;
					Iterator<Double> thItr=theoryList.iterator();
					while (thItr.hasNext()) {
						tMarks+= (Double) thItr.next();
						count+=1;
					}
					theoryMarks=tMarks/count;
				}
			}
			if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
				if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Highest")){
					practicalMarks=Collections.max(practicalList);
				}else if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Lowest")){
					practicalMarks=Collections.min(practicalList);
				}else if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Average")){
					int count=0;
					double pMarks=0;
					Iterator<Double> thItr=practicalList.iterator();
					while (thItr.hasNext()) {
						pMarks+= (Double) thItr.next();
						count+=1;
					}
					practicalMarks=pMarks/count;
				}
			}
		}
		mto.setTheoryMarks(String.valueOf(theoryMarks));
		mto.setPracticalMarks(String.valueOf(practicalMarks));
		Student student=new Student();
		student.setId(studentId);
		bo.setStudent(student);
		bo.setClasses(classes);
		Subject subject=new Subject();
		subject.setId(subId);
		bo.setSubject(subject);
		bo.setCreatedBy(userId);
		bo.setCreatedDate(new Date());
		bo.setModifiedBy(userId);
		bo.setLastModifiedDate(new Date());
		
		if(subSetTo.getTheoryEseMinMarks()>0)
			bo.setSubjectTheoryMark(String.valueOf(subSetTo.getTheoryEseMinMarks()));
		if(subSetTo.getPracticalEseMinMarks()>0)
			bo.setSubjectPracticalMark(String.valueOf(subSetTo.getPracticalEseMinMarks()));
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentTheoryMarks(theoryAlphaMarks);
			}else{
				double tMarks= (Double.parseDouble(mto.getTheoryMarks())/subSetTo.getTheoryEseEnteredMaxMarks())*subSetTo.getTheoryEseMaxMarks();
				if(tMarks<subSetTo.getTheoryEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
				if(!avoidExamIds.contains(exam.getId())){
					//bo.setStudentTheoryMarks(String.valueOf(Math.round(Double.parseDouble(df.format(tMarks)))));
					bo.setStudentTheoryMarks(String.valueOf(Double.parseDouble(df.format(tMarks))));
				}else{
					bo.setStudentTheoryMarks(String.valueOf(tMarks));
					//bo.setStudentTheoryMarks(String.valueOf(Math.round(tMarks)));
				}
			}
		}
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentPracticalMarks(practicalAlphaMarks);
			}else{
				double pMarks= (Double.parseDouble(mto.getPracticalMarks())/subSetTo.getPracticalEseEnteredMaxMarks())*subSetTo.getPracticalEseMaxMarks();
				if(pMarks<subSetTo.getPracticalEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
//				
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentPracticalMarks(String.valueOf(Double.parseDouble(df.format(pMarks))));
				else
					bo.setStudentPracticalMarks(String.valueOf(pMarks));
			}
		}
		// /* code added by chandra
		if(isGracingForEvl1==true || isGracingForEvl2==true){
			bo.setIsGracing(true);
		}else{
			bo.setIsGracing(false);
		}
		// */
		return bo;
	}
	/**
	 * @param subSetTo
	 * @param marksList
	 * @param to
	 * @param id
	 * @param exam
	 * @param subId
	 * @param classes
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private StudentFinalMarkDetails getRegularAnsFinalMarkDetailsBo(SubjectRuleSettingsTO subSetTo, List<StudentMarksTO> marksList, ClassesTO to, int studentId, ExamDefinition exam, 
			Integer subId, Classes classes, String userId) throws Exception {
		StudentFinalMarkDetails bo=new StudentFinalMarkDetails();
		StudentMarksTO mto=new StudentMarksTO();
		double theoryMarks=0;
		double practicalMarks=0;
		boolean isPass=true;
		String theoryAlphaMarks="";
		String practicalAlphaMarks="";
		
		boolean isGracingForEvl1=false;
		boolean isGracingForEvl2=false;
		
		if(marksList!=null && !marksList.isEmpty()){
			Iterator<StudentMarksTO> itr=marksList.iterator();
			while (itr.hasNext()) {
				StudentMarksTO sto = (StudentMarksTO) itr.next();
				if(sto.getTheoryMarks()!=null && !sto.getTheoryMarks().isEmpty()){
					if(!CommonUtil.isValidDecimal(sto.getTheoryMarks())){
						isPass=false;
						if(theoryAlphaMarks.isEmpty())
							theoryAlphaMarks=sto.getTheoryMarks();
					}else{
						theoryMarks=theoryMarks+Double.parseDouble(sto.getTheoryMarks());
					}
				}
				if(sto.getPracticalMarks()!=null && !sto.getPracticalMarks().isEmpty()){
					if(!CommonUtil.isValidDecimal(sto.getPracticalMarks())){
						isPass=false;
						if(practicalAlphaMarks.isEmpty())
							practicalAlphaMarks=sto.getPracticalMarks();
					}else{
						practicalMarks=practicalMarks+Double.parseDouble(sto.getPracticalMarks());
					}
				}
				// /* code added by chandra
				if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("1")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl1=true;
				}else if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("2")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl2=true;
				}
				// */
			}
		}
		mto.setTheoryMarks(String.valueOf(theoryMarks));
		mto.setPracticalMarks(String.valueOf(practicalMarks));
		Student student=new Student();
		student.setId(studentId);
		bo.setStudent(student);
		bo.setClasses(classes);
		Subject subject=new Subject();
		subject.setId(subId);
		bo.setSubject(subject);
		bo.setCreatedBy(userId);
		bo.setCreatedDate(new Date());
		bo.setModifiedBy(userId);
		bo.setLastModifiedDate(new Date());
		
		if(subSetTo.getTheoryEseMinMarks()>0)
			bo.setSubjectTheoryMark(String.valueOf(subSetTo.getTheoryEseMinMarks()));
		if(subSetTo.getPracticalEseMinMarks()>0)
			bo.setSubjectPracticalMark(String.valueOf(subSetTo.getPracticalEseMinMarks()));
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentTheoryMarks(theoryAlphaMarks);
			}else{
				double tMarks= (Double.parseDouble(mto.getTheoryMarks())/subSetTo.getTheoryEseEnteredMaxMarks())*subSetTo.getTheoryEseMaxMarks();
				if(tMarks<subSetTo.getTheoryEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
//				bo.setStudentTheoryMarks(String.valueOf(Math.round(tMarks)));
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentTheoryMarks(String.valueOf(Double.parseDouble(df.format(tMarks))));
				else
					bo.setStudentTheoryMarks(String.valueOf(tMarks));
			}
		}
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentPracticalMarks(practicalAlphaMarks);
			}else{
				double pMarks= (Double.parseDouble(mto.getPracticalMarks())/subSetTo.getPracticalEseEnteredMaxMarks())*subSetTo.getPracticalEseMaxMarks();
				if(pMarks<subSetTo.getPracticalEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
//				bo.setStudentPracticalMarks(String.valueOf(Math.round(pMarks)));
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentPracticalMarks(String.valueOf(Double.parseDouble(df.format(pMarks))));
				else
					bo.setStudentPracticalMarks(String.valueOf(pMarks));
					
			}
		}
		// /* code added by chandra
		if(isGracingForEvl1==true || isGracingForEvl2==true){
			bo.setIsGracing(true);
		}else{
			bo.setIsGracing(false);
		}
		
		// */
		return bo;
	}
	/**
	 * @param subSetTo
	 * @param marksList
	 * @param to
	 * @param studentId
	 * @param exam
	 * @param subId
	 * @param classes
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private StudentFinalMarkDetails getRegularEvalFinalMarkDetailsBo(SubjectRuleSettingsTO subSetTo, List<StudentMarksTO> marksList, ClassesTO to, int studentId, ExamDefinition exam, Integer subId, Classes classes, String userId) throws Exception {
		StudentFinalMarkDetails bo=new StudentFinalMarkDetails();
		StudentMarksTO mto=new StudentMarksTO();
		double theoryMarks=0;
		double practicalMarks=0;
		boolean isPass=true;
		List<Double> theoryList=new ArrayList<Double>();
		List<Double> practicalList=new ArrayList<Double>();
		int count=0;
		String theoryAlphaMarks="";
		String practicalAlphaMarks="";
		
		boolean isGracingForEvl1=false;
		boolean isGracingForEvl2=false;
		
		if(marksList!=null && !marksList.isEmpty()){
			Iterator<StudentMarksTO> itr=marksList.iterator();
			while (itr.hasNext()) {
				StudentMarksTO sto = (StudentMarksTO) itr.next();
				count+=1;
				if(sto.getTheoryMarks()!=null && !sto.getTheoryMarks().isEmpty()){
					if(!CommonUtil.isValidDecimal(sto.getTheoryMarks())){
						isPass=false;
						if(theoryAlphaMarks.isEmpty()){
							theoryAlphaMarks=sto.getTheoryMarks();
						}
					}else{
						theoryMarks=theoryMarks+Double.parseDouble(sto.getTheoryMarks());
						theoryList.add(Double.parseDouble(sto.getTheoryMarks()));
					}
				}
				if(sto.getPracticalMarks()!=null && !sto.getPracticalMarks().isEmpty()){
					if(!CommonUtil.isValidDecimal(sto.getPracticalMarks())){
						isPass=false;
						if(practicalAlphaMarks.isEmpty()){
							practicalAlphaMarks=sto.getPracticalMarks();
						}
					}else{
						practicalMarks=practicalMarks+Double.parseDouble(sto.getPracticalMarks());
						practicalList.add(Double.parseDouble(sto.getPracticalMarks()));
					}
				}
				// /* code added by chandra
				if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("1")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl1=true;
				}else if(sto.getEvaId()!=null && !sto.getEvaId().isEmpty() && sto.getEvaId().equalsIgnoreCase("2")){
					if(sto.getIsGracing() != null && sto.getIsGracing())
						isGracingForEvl2=true;
				}
				// */
			}
		}
		if(isPass){
			if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
				if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Highest")){
					theoryMarks=Collections.max(theoryList);
				}else if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Lowest")){
					theoryMarks=Collections.min(theoryList);
				}else if(subSetTo.getTheoryTypeOfEval().equalsIgnoreCase("Average")){
					theoryMarks=theoryMarks/count;
				}
			}
			if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
				if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Highest")){
					practicalMarks=Collections.max(practicalList);
				}else if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Lowest")){
					practicalMarks=Collections.min(practicalList);
				}else if(subSetTo.getPracticalTypeOfEval().equalsIgnoreCase("Average")){
					practicalMarks=practicalMarks/count;
				}
			}
		}
		mto.setTheoryMarks(String.valueOf(theoryMarks));
		mto.setPracticalMarks(String.valueOf(practicalMarks));
		Student student=new Student();
		student.setId(studentId);
		bo.setStudent(student);
		bo.setClasses(classes);
		Subject subject=new Subject();
		subject.setId(subId);
		bo.setSubject(subject);
		bo.setCreatedBy(userId);
		bo.setCreatedDate(new Date());
		bo.setModifiedBy(userId);
		bo.setLastModifiedDate(new Date());
		
		if(subSetTo.getTheoryEseMinMarks()>0)
			bo.setSubjectTheoryMark(String.valueOf(subSetTo.getTheoryEseMinMarks()));
		if(subSetTo.getPracticalEseMinMarks()>0)
			bo.setSubjectPracticalMark(String.valueOf(subSetTo.getPracticalEseMinMarks()));
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentTheoryMarks(theoryAlphaMarks);
			}else{
				double tMarks= (Double.parseDouble(mto.getTheoryMarks())/subSetTo.getTheoryEseEnteredMaxMarks())*subSetTo.getTheoryEseMaxMarks();
				if(tMarks<subSetTo.getTheoryEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
//				bo.setStudentTheoryMarks(String.valueOf(Math.round(tMarks)));
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentTheoryMarks(String.valueOf(Double.parseDouble(df.format(tMarks))));
				else
					bo.setStudentTheoryMarks(String.valueOf(tMarks));
					
			}
		}
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(!isPass){
				bo.setPassOrFail("fail");
				bo.setStudentPracticalMarks(practicalAlphaMarks);
			}else{
				double pMarks= (Double.parseDouble(mto.getPracticalMarks())/subSetTo.getPracticalEseEnteredMaxMarks())*subSetTo.getPracticalEseMaxMarks();
				if(pMarks<subSetTo.getPracticalEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
//				bo.setStudentPracticalMarks(String.valueOf(Math.round(pMarks)));
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentPracticalMarks(String.valueOf(Double.parseDouble(df.format(pMarks))));
				else
					bo.setStudentPracticalMarks(String.valueOf(pMarks));
					
			}
		}
		// /* code added by chandra
		if(isGracingForEvl1==true || isGracingForEvl2==true){
			bo.setIsGracing(true);
		}else{
			bo.setIsGracing(false);
		}
		bo.setIsRevaluationModeration(false);
		// */
		return bo;
	}
	/**
	 * @param subSetTo
	 * @param marksList
	 * @param to
	 * @param id
	 * @param exam
	 * @param subId
	 * @return
	 * @throws Exception
	 */
	private StudentFinalMarkDetails getRegularFinalMarkDetailsBo( SubjectRuleSettingsTO subSetTo, List<StudentMarksTO> marksList, ClassesTO to, int studentId, ExamDefinition exam, Integer subId,Classes classes,String userId) throws Exception{
		StudentFinalMarkDetails bo=new StudentFinalMarkDetails();
		StudentMarksTO mto=null;
		mto=marksList.get(0);
		Student student=new Student();
		student.setId(studentId);
		bo.setStudent(student);
		bo.setClasses(classes);
		Subject subject=new Subject();
		subject.setId(subId);
		bo.setSubject(subject);
		bo.setCreatedBy(userId);
		bo.setCreatedDate(new Date());
		bo.setModifiedBy(userId);
		bo.setLastModifiedDate(new Date());
		
		if(subSetTo.getTheoryEseMinMarks()>0)
			bo.setSubjectTheoryMark(String.valueOf(subSetTo.getTheoryEseMinMarks()));
		if(subSetTo.getPracticalEseMinMarks()>0)
			bo.setSubjectPracticalMark(String.valueOf(subSetTo.getPracticalEseMinMarks()));
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("t") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(StringUtils.isAlpha(mto.getTheoryMarks())){
				bo.setPassOrFail("fail");
				bo.setStudentTheoryMarks(mto.getTheoryMarks());
			}else{
				double theoryMarks=0;
				if(mto.getTheoryMarks()!=null){
				 theoryMarks= (Double.parseDouble(mto.getTheoryMarks().trim())/subSetTo.getTheoryEseEnteredMaxMarks())*subSetTo.getTheoryEseMaxMarks();
//				 bo.setStudentTheoryMarks(String.valueOf(Math.round(theoryMarks)));
				 if(!avoidExamIds.contains(exam.getId()))
					 bo.setStudentTheoryMarks(String.valueOf(Double.parseDouble(df.format(theoryMarks))));
				 else
					 bo.setStudentTheoryMarks(String.valueOf(theoryMarks));
				 if(theoryMarks<subSetTo.getTheoryEseMinMarks())
					 bo.setPassOrFail("fail");
				 else
					 bo.setPassOrFail("pass");
				}else{
					bo.setPassOrFail("pass");
				}
			}
			
		}
		if(subSetTo.getIsTheoryPractical().equalsIgnoreCase("p") || subSetTo.getIsTheoryPractical().equalsIgnoreCase("b")){
			if(StringUtils.isAlpha(mto.getPracticalMarks())){
				bo.setPassOrFail("fail");
				bo.setStudentPracticalMarks(mto.getPracticalMarks());
			}else{
				double PracticalMarks=0;
				if(mto.getPracticalMarks()!=null){
				 PracticalMarks= (Double.parseDouble(mto.getPracticalMarks())/subSetTo.getPracticalEseEnteredMaxMarks())*subSetTo.getPracticalEseMaxMarks();
				if(PracticalMarks<subSetTo.getPracticalEseMinMarks())
					bo.setPassOrFail("fail");
				else
					bo.setPassOrFail("pass");
				if(!avoidExamIds.contains(exam.getId()))
					bo.setStudentPracticalMarks(String.valueOf(Double.parseDouble(df.format(PracticalMarks))));
				else
					bo.setStudentPracticalMarks(String.valueOf(PracticalMarks));
					
				}else{
					bo.setPassOrFail("pass");
				}
			}
		}
		// /* code added by chandra for checking the gracing
		if(mto.getIsGracing() != null && mto.getIsGracing())
			bo.setIsGracing(true);
		else
			bo.setIsGracing(false);
		bo.setIsRevaluationModeration(false);
		// */
		return bo;
	}
	/**
	 * @param studentId
	 * @param subjectIdList
	 * @param examId
	 * @param transaction
	 * @param newUpdateProccessForm 
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, List<StudentMarksTO>> getStudentRegularMarks(int studentId, List<Integer> subjectIdList, 
			int examId, INewUpdateProccessTransaction transaction, int classId, NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		return transaction.getStudentRegularMarks(studentId,subjectIdList,examId, classId, newUpdateProccessForm);
	}
	
	
	
	/**
	 * new changes are made accourding to requirement by balaji (The Complete code has changed)
	 * @param newUpdateProccessForm
	 * @throws Exception
	 */
	public void updatePassOrFail(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		try {
			List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
			List<ExamStudentPassFail> boList=new ArrayList<ExamStudentPassFail>();
			INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
			boolean isImprovement=false;
			if(classList!=null && !classList.isEmpty()){
				Iterator<ClassesTO> itr=classList.iterator();
				while (itr.hasNext()) {
					ClassesTO to = (ClassesTO) itr.next();
					if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
						Classes classes=new Classes();
						classes.setId(to.getId());
						Map<String,SubjectMarksTO> minMarks=transaction.getMinMarksMap(to);
						List<Integer> excludedList =  impl.getExcludedFromResultSubjects(to.getCourseId(), to.getTermNo(),to.getYear());
						List<Integer> failureExcludeList = impl.getExcludedFromTotResultSubjects(to.getCourseId(), to.getTermNo(),to.getYear());
						
						boolean isMaxRecord=false;
						if(to.getCourseId()!=18){
							isMaxRecord=true;
						}
						List<StudentTO> studentList=getStudentListForClass(to.getId());
						if(studentList!=null && !studentList.isEmpty()){
							// The Real Code Here Only Starts
							Iterator<StudentTO> stuItr=studentList.iterator();
							while (stuItr.hasNext()) {
								StudentTO studentTO = (StudentTO) stuItr.next();
//								System.out.println("pass or fail:"+studentTO.getId());
//					if(studentTO.getId()==3282){// remove this
								Map<Integer,StudentMarkDetailsTO> totSubMap=new HashMap<Integer, StudentMarkDetailsTO>();//subject and marks details to verify max or latest and keep in the map at last
								BigDecimal requiredAggrePercentage = impl.getAggregatePassPercentage(to.getCourseId());
								boolean checkTotal=true;
								double stuTotalMarks=0;
								double subTotalMarks=0;
								List<Object[]> list=transaction.getDataByStudentAndClassId(studentTO.getId(),to.getId(),studentTO.getSubjectIdList(),studentTO.getAppliedYear());
								if(list!=null && !list.isEmpty()){
									Iterator<Object[]> marksItr=list.iterator();
									while (marksItr.hasNext()) {
										Object[] objects = (Object[]) marksItr.next();
										if(objects[9]!=null && minMarks.containsKey(objects[9].toString())){
											boolean isAddTotal=true;
											if(excludedList.contains(Integer.parseInt(objects[9].toString()))){
												isAddTotal=false;
											}
											SubjectMarksTO subTo=minMarks.get(objects[9].toString());
											StudentMarkDetailsTO markDetailsTO= new StudentMarkDetailsTO();
											markDetailsTO.setAddTotal(isAddTotal);
											if(objects[4]!=null)
												markDetailsTO.setStudentId(Integer.parseInt(objects[4].toString()));
											if(objects[9]!=null)
												markDetailsTO.setSubjectId(Integer.parseInt(objects[9].toString()));
											double theoryRegMark=0;
											boolean isTheoryAlpha=false;
											if (objects[15] != null || objects[16] != null) {
												if(objects[15]!=null){
													if(!StringUtils.isAlpha(objects[15].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[15].toString());
													else
														isTheoryAlpha=true;
												}
												if(objects[16]!=null){
													if(!StringUtils.isAlpha(objects[16].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[16].toString());
													else
														isTheoryAlpha=true;
												}
											}else{
												if(objects[11]!=null){
													if(!StringUtils.isAlpha(objects[11].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[11].toString());
													else
														isTheoryAlpha=true;
												}
												if(objects[12]!=null){
													if(!StringUtils.isAlpha(objects[12].toString()))
														theoryRegMark=theoryRegMark+Double.parseDouble(objects[12].toString());
													else
														isTheoryAlpha=true;
												}
											}
											if(!isTheoryAlpha)
												markDetailsTO.setStuTheoryIntMark(String.valueOf(theoryRegMark));
											else
												markDetailsTO.setStuTheoryIntMark("Ab");
											double practicalRegMark=0;
											boolean isPracticalAlpha=false;
											if (objects[17] != null || objects[18] != null) {
												if(objects[17]!=null){
													if(!StringUtils.isAlpha(objects[17].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[17].toString());
													else
														isPracticalAlpha=true;
												}if(objects[18]!=null){
													if(!StringUtils.isAlpha(objects[18].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[18].toString());
													else
														isPracticalAlpha=true;
												}
											}else{
												if(objects[13]!=null){
													if(!StringUtils.isAlpha(objects[13].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[13].toString());
													else
														isPracticalAlpha=true;
												}
												if(objects[14]!=null){
													if(!StringUtils.isAlpha(objects[14].toString()))
														practicalRegMark=practicalRegMark+Double.parseDouble(objects[14].toString());
													else
														isPracticalAlpha=true;
												}
											}
											if(!isPracticalAlpha)
												markDetailsTO.setStuPracIntMark(String.valueOf(practicalRegMark));
											else
												markDetailsTO.setStuPracIntMark("Ab");
											
											if (objects[19] != null) {
												markDetailsTO.setStuTheoryRegMark(objects[19].toString());
											}
											if (objects[20] != null) {
												markDetailsTO.setStuPracRegMark(objects[20].toString());
											}
											if (objects[22] != null) {
												markDetailsTO.setIs_theory_practical(objects[22].toString());
											}
											
											//if the student has written improvement for the subject then we should check max
											isImprovement=false;
											if (objects[23] != null) {
												isImprovement=Boolean.parseBoolean(objects[23].toString());
											}
											if(isMaxRecord){
												if(totSubMap.containsKey(markDetailsTO.getSubjectId())){
													StudentMarkDetailsTO markDetailsTO2=totSubMap.remove(markDetailsTO.getSubjectId());
													StudentMarkDetailsTO maxMarks=checkMaxBetweenTOs(markDetailsTO,markDetailsTO2,subTo,subTotalMarks,stuTotalMarks,isAddTotal,isImprovement);
													totSubMap.put(markDetailsTO.getSubjectId(),maxMarks);
												}else{
													totSubMap.put(markDetailsTO.getSubjectId(),markDetailsTO);
												}
											}else{
												if(!totSubMap.containsKey(markDetailsTO.getSubjectId())){
													totSubMap.put(markDetailsTO.getSubjectId(),markDetailsTO);
												}
											}
										}
									}
									
									
									// The Real Logic comes now ( New Logic has Implemented)
									List<StudentMarkDetailsTO> totalList = new ArrayList<StudentMarkDetailsTO>(totSubMap.values());

									if(!totalList.isEmpty()){
										Iterator<StudentMarkDetailsTO> totalitr=totalList.iterator();
										while (totalitr.hasNext()) {
											StudentMarkDetailsTO markDetailsTO = totalitr .next();
											if(minMarks.containsKey(String.valueOf(markDetailsTO.getSubjectId()))){
												boolean isAddTotal=true;
												if(excludedList.contains(markDetailsTO.getSubjectId())){
													isAddTotal=false;
												}
												SubjectMarksTO subTo=minMarks.get(String.valueOf(markDetailsTO.getSubjectId()));
												// The Real Logic has to implement Here
												if(subTo.getTheoryIntMin()!=null && !subTo.getTheoryIntMin().isEmpty()){
													if (markDetailsTO.getStuTheoryIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
															checkTotal=false;
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double
																.parseDouble(subTo.getTheoryIntMin())) {
															checkTotal=false;
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
													else{
														checkTotal=false;
														markDetailsTO.setIsTheoryFailed(true);
													}
												}
												if(subTo.getTheoryRegMin()!=null && !subTo.getTheoryRegMin().isEmpty()){
													if (markDetailsTO.getStuTheoryRegMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark()) < Double
																.parseDouble(subTo.getTheoryRegMin())) {
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
													else{
														markDetailsTO.setIsTheoryFailed(true);
													}
												}
												if(subTo.getPracticalIntMin()!=null && !subTo.getPracticalIntMin().isEmpty()){
													if (markDetailsTO.getStuPracIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
																.parseDouble(subTo.getPracticalIntMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
													else{
														markDetailsTO.setIsPracticalFailed(true);
													}
												}
												if(subTo.getPracticalRegMin()!=null && !subTo.getPracticalRegMin().isEmpty()){
													if (markDetailsTO.getStuPracRegMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
															markDetailsTO.setIsPracticalFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracRegMark()) < Double
																.parseDouble(subTo.getPracticalRegMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
													else{
														markDetailsTO.setIsPracticalFailed(true);
													}
												}
												
												if (subTo.getFinalTheoryMin() != null) {
													if(isAddTotal){
//														if(!isMaxRecord){
															if(!totSubMap.containsKey(markDetailsTO.getSubjectId())){
																if(subTo.getFinalTheoryMarks()!=null)
																	subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalTheoryMarks());
																if(markDetailsTO.getStuTheoryRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
																	stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryRegMark());
																}
																if(markDetailsTO.getStuTheoryIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
																	stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryIntMark());
																}
//															}
														}else{
															if(subTo.getFinalTheoryMarks()!=null)
																subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalTheoryMarks());
															if(markDetailsTO.getStuTheoryRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim())){
																stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryRegMark());
															}
															if(markDetailsTO.getStuTheoryIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim())){
																stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuTheoryIntMark());
															}
														}
													}
													if (markDetailsTO.getStuTheoryRegMark() != null
															&& markDetailsTO.getStuTheoryIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuTheoryRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuTheoryIntMark().trim()) ){
															markDetailsTO.setIsTheoryFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuTheoryRegMark())+ 
																Double.parseDouble(markDetailsTO.getStuTheoryIntMark()) < Double.parseDouble(subTo.getFinalTheoryMin())) {
															markDetailsTO.setIsTheoryFailed(true);
														}
													}
												}
												if (subTo.getFinalPracticalMin() != null) {
													if(isAddTotal){
//														if(!isMaxRecord){
															if(!totSubMap.containsKey(markDetailsTO.getSubjectId())){
																if(subTo.getFinalPracticalMarks()!=null)
																	subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalPracticalMarks());
																if(markDetailsTO.getStuPracRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
																	stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracRegMark());
																}
																if(markDetailsTO.getStuPracIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
																	stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracIntMark());
																}
//															}
														}else{
															if(subTo.getFinalPracticalMarks()!=null)
																subTotalMarks=subTotalMarks+Double.parseDouble(subTo.getFinalPracticalMarks());
															if(markDetailsTO.getStuPracRegMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim())){
																stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracRegMark());
															}
															if(markDetailsTO.getStuPracIntMark()!=null && !StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
																stuTotalMarks=stuTotalMarks+Double.parseDouble(markDetailsTO.getStuPracIntMark());
															}
														}
													}
													if (markDetailsTO.getStuPracRegMark() != null && markDetailsTO.getStuPracIntMark() != null){
														if(StringUtils.isAlpha(markDetailsTO.getStuPracRegMark().trim()) || StringUtils.isAlpha(markDetailsTO.getStuPracIntMark().trim())){
															markDetailsTO.setIsPracticalFailed(true);
														}
														else if (Double.parseDouble(markDetailsTO.getStuPracRegMark())
																+ Double.parseDouble(markDetailsTO.getStuPracIntMark()) < Double
																.parseDouble(subTo.getFinalPracticalMin())) {
															markDetailsTO.setIsPracticalFailed(true);
														}
													}
												}
												if(((markDetailsTO.getIsTheoryFailed()!=null && markDetailsTO.getIsTheoryFailed()) || (markDetailsTO.getIsPracticalFailed()!=null && markDetailsTO.getIsPracticalFailed())) && !failureExcludeList.contains(markDetailsTO.getSubjectId())){
													checkTotal=false;
												}
											}
										}
									}
									
									
									
									if(!totSubMap.isEmpty()){
										Double studentPercentage =Double.valueOf(0);
										if(subTotalMarks>0 && stuTotalMarks>0)
										 studentPercentage = Double.valueOf((stuTotalMarks * 100) / subTotalMarks);
										if (requiredAggrePercentage != null	&& studentPercentage != null) {
											if ((new BigDecimal(studentPercentage.toString()).intValue()) < (new BigDecimal(requiredAggrePercentage.toString()).intValue())) {
												checkTotal =false;	
											}
										}
										ExamStudentPassFail examStudentPassFail=new ExamStudentPassFail();
										Student student=new Student();
										student.setId(studentTO.getId());
										examStudentPassFail.setStudent(student);
										examStudentPassFail.setSchemeNo(to.getTermNo());
										examStudentPassFail.setClasses(classes);
										if(checkTotal)
											examStudentPassFail.setPassFail('P');
										else
											examStudentPassFail.setPassFail('F'); // modified by Nagarjun
										examStudentPassFail.setPercentage(new BigDecimal(studentPercentage));
										if(examStudentPassFail.getPassFail().equals('F')){
											examStudentPassFail.setResult("Fail");
										}else{
											String result = iUpdateStudentSGPATxn.getResultClass(to.getCourseId(), Double.valueOf(studentPercentage),studentTO.getAppliedYear(),studentTO.getId());
											examStudentPassFail.setResult(result);
										}
										boList.add(examStudentPassFail);
									}
								}
//					}//remove this
							}
						}
					}
				}
			}
			impl.updatePassFail(boList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException();
		}
	}
	
	public boolean calculateRevaluationModerationAndSaveData(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
	
		ExamDefinition exam=new ExamDefinition();
		int examId=Integer.parseInt(newUpdateProccessForm.getExamId());
		exam.setId(examId);
		List<String> errorList=new ArrayList<String>();
		List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
		List<StudentFinalMarkDetails> boList=new ArrayList<StudentFinalMarkDetails>();
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		if(classList!=null && !classList.isEmpty()){
			Iterator<ClassesTO> itr=classList.iterator();
			while (itr.hasNext()) {
				ClassesTO to = (ClassesTO) itr.next();
				Classes classes=new Classes();
				classes.setId(to.getId());
				//int SemNo=to.getTermNo();
				if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
//				if(to.getId()==620){// remove this
					
					//Map<Integer,SubjectRuleSettingsTO> subRuleMap=transaction.getSubjectRuleSettingsForRegularOverAll(to.getCourseId(),to.getYear(),to.getTermNo());
					//raghu
					//boolean isPGStudent = transaction.isPgStudent(to.getCourseId());
					List<Integer> students = getStudentIdsForModeration(to.getId(),examId);
					//List<StudentTO> studentList=getStudentListForClass(to.getId());
//					System.out.println("class Id:"+to.getId());
					if(students!=null && !students.isEmpty()){
						Iterator<Integer> sitr=students.iterator();
						while (sitr.hasNext()) {
							Integer stdnt=sitr.next();
//							if(sto.getId()==2052){//remove this
							System.out.println("student Id"+stdnt);
							//}
							List<Integer> subjects = getStubjectIdsForModeration(to.getId(),examId,stdnt);
							if(subjects!=null && !subjects.isEmpty()){
								Map<Integer,StudentFinalMarkDetails> finalMarkMap = getFinalMarkMap(to.getId(),examId,stdnt);
								//Map<Integer,List<StudentMarksTO>> stuMarksMap=getStudentRegularMarks(sto.getId(),sto.getSubjectIdList(),examId,transaction, to.getId(), newUpdateProccessForm);
								if(newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty()){
									Iterator<Integer> subItr=subjects.iterator();
									while (subItr.hasNext()) { 
										boolean change = false;
										Integer subId = (Integer) subItr.next();
										if(finalMarkMap.containsKey(subId)){
											
											StudentFinalMarkDetails sfm = finalMarkMap.get(subId);
											List obj = getMaxModeration(to.getId(),examId,stdnt,subId);
											if(obj!=null && obj.size()!=0){
												Iterator<Object[]> itr2 = obj.iterator();
												while (itr2.hasNext()) {
													Object[] objct = itr2.next();
													if(objct[0]!=null && !StringUtils.isEmpty(objct[0].toString())){
														if(Double.parseDouble(objct[0].toString())>Double.parseDouble(sfm.getStudentTheoryMarks())){
															sfm.setStudentTheoryMarks(objct[0].toString());
															change=true;
														}
													}
													if(objct[1]!=null && !StringUtils.isEmpty(objct[1].toString())){
														if(Double.parseDouble(objct[1].toString())>Double.parseDouble(sfm.getStudentPracticalMarks())){
															sfm.setStudentPracticalMarks(objct[1].toString());
															change=true;
														}
													}
													
													
												}
											}
										 if(change){
											 sfm.setModifiedBy(newUpdateProccessForm.getUserId());
											 sfm.setLastModifiedDate(new Date());
											 sfm.setIsRevaluationModeration(true);
											 boList.add(sfm);
										 }
										}
										
									}
									}
								}
							}
//						}//remove this stu id
						}//remove this
						}
					}
//				}//remove THis
			//	}
				
			}
		
		if(errorList.isEmpty() && (newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty())){
			if(boList == null || boList.isEmpty()) {
				System.gc();
				throw new DataNotFoundException();
			}
			return transaction.saveRegularOverAllAfterRevaluation(boList);
		}else{
			newUpdateProccessForm.setErrorList(errorList);
			return false;
		}
	}
	private List getMaxModeration(int id, int examId, Integer stdnt,Integer subId) throws ApplicationException {
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		return transaction.getMaxModeration(id,examId,stdnt,subId);
	}
	private Map<Integer, StudentFinalMarkDetails> getFinalMarkMap(int id,int examId, Integer stdnt) throws ApplicationException {
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		return transaction.getgetFinalMarkMap(id,examId,stdnt);
	}
	private List<Integer> getStubjectIdsForModeration(int id, int examId,Integer stdnt) throws ApplicationException {
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		return transaction.getSubjectIds(id,examId,stdnt);
	}
	private List<Integer> getStudentIdsForModeration(int id, int examId) throws ApplicationException {
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		return transaction.getStudentIds(id,examId);	
	}
	public boolean calculateInternalRetestOverAllAndSaveData(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		ExamDefinition exam=new ExamDefinition();
		exam.setId(Integer.parseInt(newUpdateProccessForm.getExamId()));
		List<ClassesTO> classList=newUpdateProccessForm.getClassesList();
		List<StudentOverallInternalMarkDetails> boList=new ArrayList<StudentOverallInternalMarkDetails>();
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		//List<Integer> intExamId=transaction.getInternalExamId(Integer.parseInt(newUpdateProccessForm.getExamId()));
		List<Integer> intExamId=transaction.getInternalExamIdForRetest(Integer.parseInt(newUpdateProccessForm.getExamId()));
		if(classList!=null && !classList.isEmpty()){
			Iterator<ClassesTO> itr=classList.iterator();
			while (itr.hasNext()) {
				ClassesTO to = (ClassesTO) itr.next();
				if(to.getChecked()!=null && !to.getChecked().isEmpty() && to.getChecked().equalsIgnoreCase("on")){
					Map<Integer,SubjectRuleSettingsTO> subRuleMap=transaction.getSubjectRuleSettings(to.getCourseId(),to.getYear(),to.getTermNo());
					List<StudentTO> studentList=getStudentListForClassForRetest(to.getId());
					if(studentList!=null && !studentList.isEmpty()){
						Iterator<StudentTO> sitr=studentList.iterator();
						while (sitr.hasNext()) {
							StudentTO sto = (StudentTO) sitr.next();
						//if(sto.getId()==9032){//remove this
							Map<String,StudentAttendanceTO> stuAttMap=transaction.getAttendanceForStudent(to.getId(),sto.getId(),sto.getSubjectIdList());
							Map<Integer,StudentAttendanceTO> stuAssMap=transaction.getAssignmentMarksForStudent(to.getCourseId(),sto.getId(),sto.getSubjectIdList());
							if(sto.getSubjectIdList()!=null && !sto.getSubjectIdList().isEmpty()){
								Iterator<Integer> subItr=sto.getSubjectIdList().iterator();
								while (subItr.hasNext()) {
									Integer subId = (Integer) subItr.next();
									//if(subId==197){//remove this
									StudentOverallInternalMarkDetails bo=new StudentOverallInternalMarkDetails();
									Student student=new Student();
									student.setId(sto.getId());
									bo.setStudent(student);
									Classes classes=new Classes();
									classes.setId(to.getId());
									bo.setClasses(classes);
									bo.setExam(exam);
									Subject subject=new Subject();
									subject.setId(subId);
									bo.setSubject(subject);
									double theoryAttendance=0;
									double theoryAssignMarks=0;
									double theoryMarks=0;
									double practicalAttendance=0;
									double practicalAssignMarks=0;
									double practicalMarks=0;
									if(subRuleMap.containsKey(subId)){
										SubjectRuleSettingsTO subTo=subRuleMap.get(subId);
										if(subTo.getIsTheoryPractical().equalsIgnoreCase("T")){
											if(subTo.isTheoryAttendanceCheck()){
												theoryAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getTheoryAttTypeId(),subTo.isTheoryCoLeaveCheck(),subTo.isTheoryLeaveCheck(),to,transaction);
												bo.setTheoryTotalAttendenceMarks(String.valueOf(theoryAttendance));
											}
											if(subTo.isTheoryAssignmentCheck()){
												theoryAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"t");
												bo.setTheoryTotalAssignmentMarks(String.valueOf(theoryAssignMarks));
											}
											if(subTo.isTheoryInteralCheck()){
												theoryMarks=getStudentRetestMarksForSubject(subId,sto.getId(),intExamId,to,"t",transaction,subTo.getTheoryBest(),subTo.isTheoryIndCheck(),exam.getId(),newUpdateProccessForm);
											}
											bo.setTheoryTotalSubInternalMarks(String.valueOf(theoryMarks));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}
											}
											
											bo.setIsGracing(newUpdateProccessForm.getIsGracing());
										}else if(subTo.getIsTheoryPractical().equalsIgnoreCase("P")){
											if(subTo.isPracticalAttendanceCheck()){
												practicalAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getPracticalAttTypeId(),subTo.isPracticalCoLeaveCheck(),subTo.isPracticalLeaveCheck(),to,transaction);
												bo.setPracticalTotalAttendenceMarks(String.valueOf(practicalAttendance));
											}
											if(subTo.isPracticalAssigntmentCheck()){
												practicalAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"p");
												bo.setPracticalTotalAssignmentMarks(String.valueOf(practicalAssignMarks));
											}
											if(subTo.isPracticalInternalCheck()){
												practicalMarks=getStudentRetestMarksForSubject(subId,sto.getId(),intExamId,to,"p",transaction,subTo.getPracticalBest(),subTo.isPracticalIndCheck(),exam.getId(),newUpdateProccessForm);
											}
											bo.setPracticalTotalSubInternalMarks(String.valueOf(practicalMarks));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}
												
												
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}
											}
											
											
											bo.setIsGracing(newUpdateProccessForm.getIsGracing());
										}else if(subTo.getIsTheoryPractical().equalsIgnoreCase("B")){
											if(subTo.isTheoryAttendanceCheck()){
												theoryAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getTheoryAttTypeId(),subTo.isTheoryCoLeaveCheck(),subTo.isTheoryLeaveCheck(),to,transaction);
												bo.setTheoryTotalAttendenceMarks(String.valueOf(theoryAttendance));
											}
											if(subTo.isTheoryAssignmentCheck()){
												theoryAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"t");
												bo.setTheoryTotalAssignmentMarks(String.valueOf(theoryAssignMarks));
											}
											if(subTo.isTheoryInteralCheck()){
												theoryMarks=getStudentRetestMarksForSubject(subId,sto.getId(),intExamId,to,"t",transaction,subTo.getTheoryBest(),subTo.isTheoryIndCheck(),exam.getId(),newUpdateProccessForm);
												bo.setIsGracing(newUpdateProccessForm.getIsGracing());
											}
											bo.setTheoryTotalSubInternalMarks(String.valueOf(theoryMarks));
											if(subTo.isPracticalAttendanceCheck()){
												practicalAttendance=getAttendanceMarksForSubject(subId,stuAttMap,subTo.getPracticalAttTypeId(),subTo.isPracticalCoLeaveCheck(),subTo.isPracticalLeaveCheck(),to,transaction);
												bo.setPracticalTotalAttendenceMarks(String.valueOf(practicalAttendance));
											}
											if(subTo.isPracticalAssigntmentCheck()){
												practicalAssignMarks=getAssignMarksForSubject(subId,stuAssMap,"p");
												bo.setPracticalTotalAssignmentMarks(String.valueOf(practicalAssignMarks));
											}
											if(subTo.isPracticalInternalCheck()){
												practicalMarks=getStudentRetestMarksForSubject(subId,sto.getId(),intExamId,to,"p",transaction,subTo.getPracticalBest(),subTo.isPracticalIndCheck(),exam.getId(),newUpdateProccessForm);
												if(bo.getIsGracing()!=null && bo.getIsGracing()){
													bo.setIsGracing(true);
												}else{
													bo.setIsGracing(newUpdateProccessForm.getIsGracing());
												}
											}
											bo.setPracticalTotalSubInternalMarks(String.valueOf(practicalMarks));
											
											bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setTheoryTotalMarks(String.valueOf(theoryAttendance+theoryAssignMarks+theoryMarks));
												}else{
													bo.setTheoryTotalMarks(String.valueOf(Math.ceil(theoryAttendance+theoryAssignMarks+theoryMarks)));
												}
											}
											
											if(to.getSectionName().equalsIgnoreCase("UG")){
												if(CMSConstants.UG_INTERNALCALC_REGULAREXAM2015_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}
											}else{
												if(CMSConstants.PG_INTERNALCALC_REGULAREXAM2104_IDSLIST.contains(Integer.parseInt(newUpdateProccessForm.getExamId()))){
													bo.setPracticalTotalMarks(String.valueOf(practicalAttendance+practicalAssignMarks+practicalMarks));
												}else{
													bo.setPracticalTotalMarks(String.valueOf(Math.ceil(practicalAttendance+practicalAssignMarks+practicalMarks)));
												}
											}
											
										}
										boList.add(bo);
									}
								}
							//}//remove this
							}
						//}// remove this
						}
					}
				}
			}
		}
		
		return transaction.saveInternalOverAll(boList);
	}
	private List<StudentTO> getStudentListForClassForRetest(int classId) throws Exception {
		List<StudentTO> studentList=new ArrayList<StudentTO>();
		String query=getCurrentClassQueryForRetest(classId);// Getting Current Class Students Query
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<Student> currentStudentList=transaction.getDataForQuery(query);
		getFinalStudentsForCurrentClass(currentStudentList,studentList);//Adding current Class Students for StudentList
		String preQuery=getPreviousClassQueryForRetest(classId);
		List<Object[]> previousStudentList=transaction.getDataForQuery(preQuery);
		getFinalStudentsForPreviousClass(previousStudentList,studentList);
		return studentList;
	}
	private String getCurrentClassQueryForRetest(int classId) throws Exception{
		String query="from Student s" +
				" where s.admAppln.isCancelled=0 and s.isAdmitted=1 " +
				" and s.classSchemewise.classes.id="+classId+" and s.id in(select rt.studentId from ExamInternalRetestApplicationBO rt where rt.classId="+classId+")";
		return query;
	}
	private String getPreviousClassQueryForRetest(int classId)  throws Exception{
		String query="select s.id,subSet.subject,s.registerNo,s.admAppln.appliedYear from Student s" +
				" join s.studentPreviousClassesHistory classHis" +
				" join classHis.classes.classSchemewises classSchemewise" +
				" join classSchemewise.curriculumSchemeDuration cd" +
				" join s.studentSubjectGroupHistory subjHist " +
				" join subjHist.subjectGroup.subjectGroupSubjectses subSet" +
				" where s.admAppln.isCancelled=0 and s.isAdmitted=1 and subSet.isActive=1 and classHis.classes.id=" +classId+
				" and s.classSchemewise.classes.id <> "+classId+
				" and classHis.schemeNo=subjHist.schemeNo and s.id in(select rt.studentId from ExamInternalRetestApplicationBO rt where rt.classId="+classId+")" ;
		return query;
	}
private double getStudentRetestMarksForSubject(Integer subId, int studentId, List<Integer> intExamId, ClassesTO to, String subType,INewUpdateProccessTransaction transaction,int limit,boolean isInd,int examId,NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		
		return transaction.getStudentRetestMarksForSubject(subId,studentId,intExamId,to,subType,limit,isInd,examId,newUpdateProccessForm);
	}

}
