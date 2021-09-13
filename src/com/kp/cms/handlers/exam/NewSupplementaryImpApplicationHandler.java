package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.PcFinancialYear;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ChallanUploadDataExam;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamFooterAgreementBO;
import com.kp.cms.bo.exam.ExamPublishHallTicketMarksCardBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamRevaluationApp;
import com.kp.cms.bo.exam.ExamRevaluationApplicationNew;
import com.kp.cms.bo.exam.ExamRevaluationApplictionDetails;
import com.kp.cms.bo.exam.ExamRevaluationFee;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.OnlinePaymentReciepts;
import com.kp.cms.bo.exam.PublishSupplementaryImpApplication;
import com.kp.cms.bo.exam.RegularExamApplicationPGIDetails;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.RevaluationExamFees;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.SubjectUtilBO;
import com.kp.cms.bo.exam.SupplementaryExamApplicationPGIDetails;
import com.kp.cms.bo.exam.SupplementaryFees;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.handlers.attendance.StudentAttendanceSummaryHandler;
import com.kp.cms.helpers.exam.NewSupplementaryImpApplicationHelper;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationSubjectTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationTO;
import com.kp.cms.to.exam.SupplementaryAppExamTo;
import com.kp.cms.to.exam.SupplementaryApplicationClassTo;
import com.kp.cms.to.reports.StudentWiseSubjectSummaryTO;
import com.kp.cms.transactions.admin.ISingleFieldMasterTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewStudentMarksCorrectionTransaction;
import com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction;
import com.kp.cms.transactionsimpl.admin.SingleFieldMasterTransactionImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewStudentMarksCorrectionTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewSupplementaryImpApplicationTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.PropertyUtil;

@SuppressWarnings("unchecked")
public class NewSupplementaryImpApplicationHandler {
	/**
	 * Singleton object of NewSupplementaryImpApplicationHandler
	 */
	private static volatile NewSupplementaryImpApplicationHandler newSupplementaryImpApplicationHandler = null;
	private static final Log log = LogFactory.getLog(NewSupplementaryImpApplicationHandler.class);

	private NewSupplementaryImpApplicationHandler() {

	}

	/**
	 * return singleton object of NewSupplementaryImpApplicationHandler.
	 * 
	 * @return
	 */
	public static NewSupplementaryImpApplicationHandler getInstance() {
		if (newSupplementaryImpApplicationHandler == null) {
			newSupplementaryImpApplicationHandler = new NewSupplementaryImpApplicationHandler();
		}
		return newSupplementaryImpApplicationHandler;
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	public Map<String, ExamSupplementaryImpApplicationTO> getStudentListForInput(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
			throws Exception {
		Integer studentId = getStudentId(newSupplementaryImpApplicationForm);
		String query = NewSupplementaryImpApplicationHelper.getInstance()
				.getStudentListQuery(newSupplementaryImpApplicationForm,
						studentId);
		boolean isRegOrRoll=false;
		boolean checkAll=true;
		if((newSupplementaryImpApplicationForm.getRegisterNo()!=null && !newSupplementaryImpApplicationForm.getRegisterNo().isEmpty()) || (newSupplementaryImpApplicationForm.getRollNo()!=null && !newSupplementaryImpApplicationForm.getRollNo().isEmpty())){
			isRegOrRoll=true;
		}
		if(isRegOrRoll && studentId==null)
			checkAll=false;
		if(checkAll){
			INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl
					.getInstance();
			List boList = transaction.getDataForQuery(query);
			return NewSupplementaryImpApplicationHelper.getInstance()
					.convertBotoToList(boList, newSupplementaryImpApplicationForm);
		}else
			return new HashMap<String, ExamSupplementaryImpApplicationTO>();
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	private Integer getStudentId(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
			throws Exception {
		Integer studentId = null;
		if ((newSupplementaryImpApplicationForm.getRegisterNo() != null && !newSupplementaryImpApplicationForm
				.getRegisterNo().isEmpty())
				|| (newSupplementaryImpApplicationForm.getRollNo() != null && !newSupplementaryImpApplicationForm
						.getRollNo().isEmpty())) {
			if (newSupplementaryImpApplicationForm.getSchemeNo() != null
					&& !newSupplementaryImpApplicationForm.getSchemeNo()
							.isEmpty()) {
				INewStudentMarksCorrectionTransaction transaction1 = NewStudentMarksCorrectionTransactionImpl .getInstance();
				studentId = transaction1.getStudentId( newSupplementaryImpApplicationForm.getRegisterNo(), newSupplementaryImpApplicationForm.getSchemeNo(), newSupplementaryImpApplicationForm.getRollNo());
			}
		}
		return studentId;
	}

	/**
	 * @param to
	 * @throws Exception
	 */
	public void setDatatoTo(ExamSupplementaryImpApplicationTO to)
			throws Exception {
		String query = NewSupplementaryImpApplicationHelper.getInstance().getQueryForEdit(to);
		INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl.getInstance();
		List<StudentSupplementaryImprovementApplication> boList = transaction .getDataForSQLQuery(query);
		NewSupplementaryImpApplicationHelper.getInstance().convertBotoToList(boList, to);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	public boolean saveSupplementaryApplication( NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<StudentSupplementaryImprovementApplication> boList=new ArrayList<StudentSupplementaryImprovementApplication>();
		ExamSupplementaryImpApplicationTO suppTo = newSupplementaryImpApplicationForm .getSuppTo();
		ExamDefinition examDefinition = new ExamDefinition();
		examDefinition.setId(suppTo.getExamId());
		Student student = new Student();
		student.setId(suppTo.getStudentId());
		Classes classes = new Classes();
		classes.setId(suppTo.getClassId());
		List<ExamSupplementaryImpApplicationSubjectTO> listSubject = suppTo .getSubjectList();
		for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
			StudentSupplementaryImprovementApplication objBO = new StudentSupplementaryImprovementApplication();
			if(to.getId()!=null)
				objBO.setId(to.getId());
			objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
			objBO.setCreatedDate(new Date());
			objBO.setLastModifiedDate(new Date());
			objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
			objBO.setStudent(student);
			objBO.setExamDefinition(examDefinition);
			Subject subject = new Subject();
			subject.setId(to.getSubjectId());
			objBO.setSubject(subject);
			if (to.getFailedTheory() != null) {
				objBO.setIsFailedTheory(true);
			} else {
				objBO .setIsFailedTheory((to.getIsFailedTheory()) ? true : false);
			}
			if (to.getFailedPractical() != null) {
				objBO.setIsFailedPractical(true);
			} else {
				objBO.setIsFailedPractical((to.getIsFailedPractical()) ? true : false);
			}
			objBO.setIsAppearedTheory(to.getIsAppearedTheory());
			objBO.setIsAppearedPractical(to.getIsAppearedPractical());
			objBO.setFees(to.getFees());
			objBO.setSchemeNo(suppTo.getSemester_year_no());
			objBO.setChance(to.getChance());
			if(to.getIsOverallTheoryFailed()!=null)
				objBO.setIsTheoryOverallFailed(to.getIsOverallTheoryFailed());
			else
				objBO.setIsTheoryOverallFailed(false);
			if(to.getIsOverallPracticalFailed()!=null)
				objBO.setIsPracticalOverallFailed(to.getIsOverallPracticalFailed());
			else
				objBO.setIsPracticalOverallFailed(false);
			objBO.setClasses(classes);
			if (newSupplementaryImpApplicationForm .getSupplementaryImprovement() != null && newSupplementaryImpApplicationForm .getSupplementaryImprovement().trim().length() > 0) {
				if (newSupplementaryImpApplicationForm .getSupplementaryImprovement().equalsIgnoreCase("Supplementary")) {
					
					//raghu write this one
					//objBO.setIsSupplementary(true);
					//objBO.setIsImprovement(false);
					
					//raghu added newly
					if(to.getIsImprovement()||to.getIsSupplementary()){
						objBO.setIsImprovement(to.getIsImprovement());
						objBO.setIsSupplementary(to.getIsSupplementary());
					}else{
						objBO.setIsSupplementary(true);
						objBO.setIsImprovement(false);
						
					}
					
					
					
				} else if (newSupplementaryImpApplicationForm.getSupplementaryImprovement().equalsIgnoreCase("Improvement")) {
					
					//raghu write this one
					//objBO.setIsImprovement(true);
					//objBO.setIsSupplementary(false);
					
					//raghu added newly
					if(to.getIsImprovement()||to.getIsSupplementary()){
						objBO.setIsImprovement(to.getIsImprovement());
						objBO.setIsSupplementary(to.getIsSupplementary());
					}else{
						objBO.setIsImprovement(true);
						objBO.setIsSupplementary(false);
						
						
					}
				}
			}
			objBO.setIsOnline(false);
			
			
			
			
			//raghu added from mounts
			
			objBO.setIsAppearedTheory(to.getIsAppearedTheory());
			objBO.setIsAppearedPractical(to.getIsAppearedPractical());
			
			
			// new modification
			
			
			if(to.isCiaExam())
			{
				if(to.getIsAppearedTheory())
				{
					objBO.setIsAppearedTheory(false);
					objBO.setIsAppearedPractical(false);
					objBO.setIsFailedPractical(false);
					objBO.setIsFailedTheory(false);
					
					// cia
					objBO.setIsCIAAppearedTheory(to.getIsAppearedTheory());
					objBO.setIsCIAFailedTheory(true);
					objBO.setIsCIAFailedPractical(false);
					objBO.setIsCIAAppearedPractical(false);
					objBO.setCiaExam(true);
				}
				if(to.getIsAppearedPractical())
				{
					objBO.setIsAppearedTheory(false);
					objBO.setIsAppearedPractical(false);
					objBO.setIsFailedPractical(false);
					objBO.setIsFailedTheory(false);
					
					// cia
					objBO.setIsCIAAppearedTheory(false);
					objBO.setIsCIAFailedTheory(true);
					objBO.setIsCIAFailedPractical(true);
					objBO.setIsCIAAppearedPractical(to.getIsAppearedPractical());
					objBO.setCiaExam(true);
				}
			}
			else
			{
				objBO.setIsAppearedTheory(to.getIsAppearedTheory());
				objBO.setIsAppearedPractical(to.getIsAppearedPractical());
				objBO.setIsFailedPractical(to.getIsFailedPractical());
				objBO.setIsFailedTheory(to.getIsFailedTheory());
				
				objBO.setIsCIAAppearedTheory(false);
				objBO.setIsCIAFailedTheory(false);
				objBO.setIsCIAFailedPractical(false);
				objBO.setIsCIAAppearedPractical(false);
				objBO.setCiaExam(false);
			}
			
			
			boList.add(objBO);
		}
		return transaction.saveSupplementarys(boList);
	}

	/**
	 * @param to
	 * @throws Exception
	 */
	public boolean deleteSupplementaryImpApp(ExamSupplementaryImpApplicationTO to) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		String query = NewSupplementaryImpApplicationHelper.getInstance().getQueryForEdit(to);
		return transaction.deleteSupplementaryImpApp(query); 
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	
	public ExamSupplementaryImpApplicationTO getStudentDetails(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<Object[]> studentDetails=new ArrayList<Object[]>();
		List<Subject> subList=new ArrayList<Subject>();
		String previousQuery="select s.student,s.classes from StudentPreviousClassHistory s " +
				" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
				" and s.student.id="+newSupplementaryImpApplicationForm.getStudentId();
		List previousList=transaction.getDataForQuery(previousQuery);
		if(previousList.isEmpty()){
			String currentQuery="select s,s.classSchemewise.classes from Student s " +
								"where s.id=" +newSupplementaryImpApplicationForm.getStudentId()+
								" and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo();
			List currentList=transaction.getDataForQuery(currentQuery);
			studentDetails.addAll(currentList);
		}else{
			studentDetails.addAll(previousList);
		}
		//String subPreQuery="select subGrp.subject from StudentSubjectGroupHistory s join s.subjectGroup.subjectGroupSubjectses subGrp" +
			//	" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
			//	" and s.student.id="+newSupplementaryImpApplicationForm.getStudentId();
		
		String subPreQuery="select  m.subject from MarksEntryDetails m where m.marksEntry.student.id="+newSupplementaryImpApplicationForm.getStudentId()+" and m.marksEntry.exam.examTypeUtilBO.name='Regular' and  m.marksEntry.classes.termNumber=" +newSupplementaryImpApplicationForm.getSchemeNo()+" group by m.subject.id";
		// raghu change query bcz they enterd wrong data in student subject group
		
		
		List<Subject> preList=transaction.getDataForQuery(subPreQuery);
		if(!preList.isEmpty()){
			subList.addAll(preList);
		}
		String subQuery="select subGrp.subject from Student s join s.admAppln.applicantSubjectGroups appSub join appSub.subjectGroup.subjectGroupSubjectses subGrp" +
				" where s.id=" +newSupplementaryImpApplicationForm.getStudentId()+
				" and subGrp.isActive=1 and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo();
		List<Subject> suList=transaction.getDataForQuery(subQuery);
		subList.addAll(suList);
		return NewSupplementaryImpApplicationHelper.getInstance().convertBoListsToTo(subList,studentDetails,newSupplementaryImpApplicationForm);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 */
	public String checkValid(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewStudentMarksCorrectionTransaction transaction1 = NewStudentMarksCorrectionTransactionImpl .getInstance();
		Integer studentId = transaction1.getStudentId( newSupplementaryImpApplicationForm.getRegisterNo(), newSupplementaryImpApplicationForm.getSchemeNo(), newSupplementaryImpApplicationForm.getRollNo());
		String msg="";
		if(studentId==null || studentId==0){
			msg="Invalid Student";
		}else{
			String query="from StudentSupplementaryImprovementApplication s where s.student.id="+studentId+" and s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
			" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId();
			List<StudentSupplementaryImprovementApplication> boList=transaction.getDataForQuery(query);
			if(boList!=null && !boList.isEmpty()){
				msg="Details Already Exists ";
			}else{
				newSupplementaryImpApplicationForm.setStudentId(studentId);
			}
		}
		return msg;
	}
	
	
	public Boolean checkSuppDateExtended(int studentId, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		Boolean isExtended=false;
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		isExtended=transaction.getExtendedDate(studentId, newSupplementaryImpApplicationForm);
		 return isExtended;
		
	}
	/**
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	/*public List<Integer> checkSuppImpAppAvailable(int studentId) throws Exception {
		String query="select p.exam.id from PublishSupplementaryImpApplication p " +
				" where p.isActive=1 and p.exam.delIsActive=1 and p.exam.id in ( select s.examDefinition.id from StudentSupplementaryImprovementApplication s" +
				" where s.student.id= " +studentId+
				" and ((s.isFailedPractical=1 or s.isFailedTheory=1) and (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classCode.id)) " +
		" and ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.endDate or '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.extendedDate) group by p.exam.id "; 
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		return transaction.getDataForQuery(query);
	}*/
	
	public List<Integer> checkSuppImpAppAvailable(int studentId) throws Exception {
		String query="select p.exam.id from PublishSupplementaryImpApplication p " +
				" where p.isActive=1 and p.exam.delIsActive=1 and p.exam.id in ( select s.examDefinition.id from StudentSupplementaryImprovementApplication s" +
				" where s.student.id= " +studentId+
				//raghu added from mounts
				//" and (((s.isFailedPractical=1 or s.isFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) and (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classCode.id)) " +
				" and (((s.isFailedPractical=1 or s.isFailedTheory=1 or s.isCIAFailedPractical=1 or s.isCIAFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) or (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classCode.id)) " +
				
				
				/*"and '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between (p.startDate and p.endDate)  group by p.exam.id ";*/
		" and (('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.endDate) or ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.extendedDate)) group by p.exam.id "; 
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		return transaction.getDataForQuery(query);
	}

	/**
	 * @param examIds
	 * @param newSupplementaryImpApplicationForm
	 * @throws Exception
	 */
	public void getApplicationForms(Boolean extendedTrue, List<Integer> examIds, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<SupplementaryAppExamTo> examList=new ArrayList<SupplementaryAppExamTo>();
		SupplementaryAppExamTo eto=null;
		int count=0;
		boolean buttonDisplay=false;
		boolean challanDisplay=false;
		boolean isFormSubmitted=false;
		List<Integer> classNotRequiredList=new ArrayList<Integer>();

		if(newSupplementaryImpApplicationForm.getExamId()!=null){
			int examId =Integer.parseInt(newSupplementaryImpApplicationForm.getExamId());
			eto=new SupplementaryAppExamTo();
			eto.setExamId(examId);
			eto.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinition",true,"name"));
			String endDateQuery="select concat(s.endDate,'') from PublishSupplementaryImpApplication s where s.exam.id="+examId+
			"and s.classCode.id in (select supp.classes.id from StudentSupplementaryImprovementApplication supp where supp.examDefinition.id="+examId+" and supp.student.id="+newSupplementaryImpApplicationForm.getStudentId()+") and s.isActive=1";
			List dateList = transaction.getDataForQuery(endDateQuery);
			//			String endDate=PropertyUtil.getDataForUnique(endDateQuery); // commented by nagarjuna because this query return multiple records from multiple classes.
			String endDate=""; 
			if(dateList != null && !dateList.isEmpty())
				endDate = (String) dateList.get(0);
			eto.setExamDate(CommonUtil.formatSqlDate1(endDate));
			if(extendedTrue){
				String extendedDateQuery="select concat(s.extendedDate,'') from PublishSupplementaryImpApplication s where s.exam.id="+examId+
				"and s.classCode.id in (select supp.classes.id from StudentSupplementaryImprovementApplication supp where supp.examDefinition.id="+examId+" and supp.student.id="+newSupplementaryImpApplicationForm.getStudentId()+") and s.isActive=1";
				List extendedDateList = transaction.getDataForQuery(extendedDateQuery);
				//				String endDate=PropertyUtil.getDataForUnique(endDateQuery); // commented by nagarjuna because this query return multiple records from multiple classes.
				String extendedEndDate=""; 
				if(extendedDateList != null && !extendedDateList.isEmpty())
					extendedEndDate = (String) extendedDateList.get(0);
				eto.setExtendedDate(CommonUtil.formatSqlDate1(extendedEndDate));
			}


			//rgahu write newly like regular app
			String query1 = "from ExamDefinition e where e.id ="+examId;
			List<ExamDefinition> examList1=transaction.getDataForQuery(query1);
			for (ExamDefinition bo : examList1) {
				newSupplementaryImpApplicationForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
				newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
				newSupplementaryImpApplicationForm.setExamName(bo.getName());
				newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth())));
				newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
			}

			query1 = "from CourseToDepartment c where c.course.id ="+newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
			List<CourseToDepartment> courseToDepartmentList=transaction.getDataForQuery(query1);
			String dep="";

			for (CourseToDepartment bo : courseToDepartmentList) {
				if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()==1){
					dep=dep+bo.getDepartment().getName();
				}else if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()>1){
					if(count==0){
						dep=dep+bo.getDepartment().getName();
					}else{
						dep=dep+","+bo.getDepartment().getName();
					}

					count++;
				}

				newSupplementaryImpApplicationForm.setCourseDep(dep);

			}





			/*String query="from StudentSupplementaryImprovementApplication s" +
				" where s.student.id= " +newSupplementaryImpApplicationForm.getStudentId()+
				//raghu added from mounts
				//" and (((s.isFailedPractical=1 or s.isFailedTheory=1) and s.isSupplementary=1) or s.isImprovement=1) and s.examDefinition.id="+examId;
				" and (((s.isFailedPractical=1 or s.isFailedTheory=1 or s.isCIAFailedPractical=1 or s.isCIAFailedTheory=1) and s.isSupplementary=1) or s.isImprovement=1) and s.examDefinition.id="+examId;

			 */	

			String query="SELECT  *  FROM    EXAM_supplementary_improvement_application EXAM_supplementary_improvement_application" +
			" INNER JOIN EXAM_sub_definition_coursewise EXAM_sub_definition_coursewise " +
			" ON (EXAM_supplementary_improvement_application.subject_id =  EXAM_sub_definition_coursewise.subject_id)" +
			" where EXAM_supplementary_improvement_application.student_id="+newSupplementaryImpApplicationForm.getStudentId()+
			" and EXAM_supplementary_improvement_application.exam_id=" +examId+
			" and (((EXAM_supplementary_improvement_application.is_failed_practical=1 or EXAM_supplementary_improvement_application.is_failed_theory=1 or EXAM_supplementary_improvement_application.cia_is_failed_practical=1 or EXAM_supplementary_improvement_application.cia_is_failed_theory=1)" +
			" and EXAM_supplementary_improvement_application.is_supplementary=1) or EXAM_supplementary_improvement_application.is_improvement=1) "+
			" group by EXAM_supplementary_improvement_application.subject_id order by EXAM_sub_definition_coursewise.subject_order";


			List<StudentSupplementaryImprovementApplication> boList= transaction.getDataForSQLQuery(query);
			List<SupplementaryApplicationClassTo> classList=new ArrayList<SupplementaryApplicationClassTo>();
			SupplementaryApplicationClassTo to=null;
			double applicationFee=0;
			double onlineServiceFees=0;
			double cvCampFee=0;
			double marksListFee=0;
			double totalfee=0;
			query="from SupplementaryFees r where r.academicYear="+newSupplementaryImpApplicationForm.getAcademicYear()+" and r.course.id="+newSupplementaryImpApplicationForm.getCourseId();
			SupplementaryFees supplementaryFees=null;
			List<SupplementaryFees> supplExamFeeList=transaction.getDataForQuery(query);


			if(supplExamFeeList==null || supplExamFeeList.isEmpty() )
				newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
			if(!supplExamFeeList.isEmpty()){
				supplementaryFees=supplExamFeeList.get(0);
				if(supplementaryFees.getApplicationFees()!=null){
					applicationFee=supplementaryFees.getApplicationFees().doubleValue();
					newSupplementaryImpApplicationForm.setApplicationFees(String.valueOf(applicationFee));
				}
				if(supplementaryFees.getMarksListFees()!=null){
					marksListFee=supplementaryFees.getMarksListFees().doubleValue();
					newSupplementaryImpApplicationForm.setMarksListFees(String.valueOf(marksListFee));
				}

				if(supplementaryFees.getCvCampFees()!=null){
				    cvCampFee=supplementaryFees.getCvCampFees().doubleValue();
					newSupplementaryImpApplicationForm.setCvCampFees(String.valueOf(cvCampFee));
				}

				if(supplementaryFees.getOnlineServiceChargeFees()!=null){
					onlineServiceFees=supplementaryFees.getOnlineServiceChargeFees().doubleValue();
					newSupplementaryImpApplicationForm.setOnlineServiceChargeFees(String.valueOf(onlineServiceFees));
				}
			}
			for (StudentSupplementaryImprovementApplication bo : boList) {
				if (!classNotRequiredList.contains(bo.getClasses().getId())) {
					to = new SupplementaryApplicationClassTo();
					to.setClassId(bo.getClasses().getId());
					newSupplementaryImpApplicationForm.setSuppImpClassId(bo.getClasses().getId());
					to.setClassName(bo.getClasses().getName());
					ClassesTO to1 = new ClassesTO();
					to1.setId(bo.getClasses().getId());
					to1.setTermNo(bo.getSchemeNo());
					to1.setYear(bo.getExamDefinition().getAcademicYear());
					to1.setCourseId(Integer.parseInt(newSupplementaryImpApplicationForm.getCourseId()));
					List<ExamSupplementaryImpApplicationSubjectTO> subjectList = null;
					if (classList.contains(to)) {
						to = classList.remove(classList.indexOf(to));
						subjectList = to.getToList();
						if (bo.getIsSupplementary() != null) {
							if (to.isSupplementary() != bo.getIsSupplementary()){
								//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
								//classNotRequiredList.add(bo.getClasses() .getId());
							}
						}
						if (bo.getIsImprovement() != null) {
							if (to.isImprovement() != bo.getIsImprovement()){
								//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
								//classNotRequiredList.add(bo.getClasses() .getId());
							}
						}
					} else {
						subjectList = new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
						to.setCourseName(bo.getClasses().getCourse().getName());
						to.setRegisterNo(bo.getStudent().getRegisterNo());
						to.setRollNo(bo.getStudent().getRollNo());
						to.setSemNo(bo.getClasses().getTermNumber().toString());
						to.setStudentName(bo.getStudent().getAdmAppln() .getPersonalData().getFirstName());
						to.setClassName(bo.getClasses().getName());
						if (bo.getIsImprovement() != null)
							to.setImprovement(bo.getIsImprovement());
						if (bo.getIsSupplementary() != null)
							to.setSupplementary(bo.getIsSupplementary());
						if (count == 0) {
							newSupplementaryImpApplicationForm.setCourseName(bo .getClasses().getCourse().getName());
							newSupplementaryImpApplicationForm.setRegisterNo(bo .getStudent().getRegisterNo());
							newSupplementaryImpApplicationForm .setNameOfStudent(bo.getStudent() .getAdmAppln().getPersonalData() .getFirstName());
							count++;
						}
						newSupplementaryImpApplicationForm.setProgramId(String .valueOf(bo.getStudent().getAdmAppln() .getCourseBySelectedCourseId() .getProgram().getId()));
					}
					if (!classNotRequiredList.contains(bo.getClasses().getId())) {
						ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
						sto.setId(bo.getId());
						sto.setSubjectId(bo.getSubject().getId());
						sto.setSubjectCode(bo.getSubject().getCode());
						sto.setSubjectName(bo.getSubject().getName());
						sto.setSectionName(bo.getSubject().getSubjectType().getName());
						//raghu new					

						sto.setSubjectType(bo.getSubject().getIsTheoryPractical());	
						
						if(bo.getSubject().getSubjectType().getId()==7 || bo.getSubject().getSubjectType().getId()==13){
							sto.setSubjectType("Project");
						}
						//Ashwini did for calculating total supply fees

						if (bo.getIsAppearedPractical() != null && bo.getIsAppearedPractical() || bo.getIsAppearedTheory()!=null && bo.getIsAppearedTheory()){
							if(bo.getIsSupplementary()!=null && bo.getIsSupplementary()==true ){
								if(sto.getSubjectType().equalsIgnoreCase("T")){
									if(supplementaryFees!=null && supplementaryFees.getTheoryFees()!=null)
										totalfee+= supplementaryFees.getTheoryFees().doubleValue();
								}
								else
									totalfee+= supplementaryFees.getPracticalFees().doubleValue();
								isFormSubmitted = true;
							}

							else if(bo.getIsImprovement()!=null && bo.getIsImprovement()==true){
								if(sto.getSubjectType().equalsIgnoreCase("T")){
									if(supplementaryFees!=null && supplementaryFees.getTheoryFees()!=null)
										totalfee+= supplementaryFees.getTheoryFees().doubleValue();
								}
								else
									totalfee+= supplementaryFees.getPracticalFees().doubleValue();
								isFormSubmitted = true;

							}
							challanDisplay=true;
						}

						if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){

							String subQuery =   "select esb.examSubjectSectionMasterBO.name from ExamSubDefinitionCourseWiseBO esb where   esb.subjectUtilBO.id = "+bo.getSubject().getId();
							List sList= transaction.getDataForQuery(subQuery);
							for (Object s : sList) {
								sto.setSectionName(s.toString());
							}

						}

						if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("t") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
							sto.setTheory(true);
						if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("p") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
							sto.setPractical(true);

						if (!bo.getIsFailedTheory() && !bo.getIsFailedPractical()) {
							sto.setIsOverallTheoryFailed(bo .getIsTheoryOverallFailed());
							sto.setIsOverallPracticalFailed(bo .getIsPracticalOverallFailed());
						} else {
							sto.setIsOverallTheoryFailed(false);
							sto.setIsOverallPracticalFailed(false);
						}





						sto.setChance(bo.getChance());
						// sto.setFees(bo.getFees());
						if (bo.getFees() != null && !bo.getFees().isEmpty())
							sto.setPreviousFees(Double .parseDouble(bo.getFees()));
						sto.setIsFailedTheory(bo.getIsFailedTheory());
						sto.setIsFailedPractical(bo.getIsFailedPractical());
						if (bo.getIsAppearedTheory() != null){
							sto.setTempChecked(bo.getIsAppearedTheory());
							sto.setCommonChecked(bo.getIsAppearedTheory());
							if(bo.getIsAppearedTheory())
								challanDisplay = true;
						}

						else
							sto.setTempChecked(false);
						if (bo.getIsAppearedPractical() != null){
							sto.setTempPracticalChecked(bo .getIsAppearedPractical());
							sto.setCommonChecked(bo.getIsAppearedTheory());
							if(bo.getIsAppearedTheory())
								challanDisplay = true;
						}

						else
							sto.setTempPracticalChecked(false);
						boolean controlDisable = false;
						if ((bo.getIsFailedPractical() == true) || (bo.getIsFailedTheory() == true)) {
							controlDisable = true;
						}
						sto.setControlDisable(controlDisable);
						sto.setClassId(bo.getClasses().getId());
						sto.setSchemeNo(bo.getSchemeNo());
						if(bo.getIsOnline()!=null)
							sto.setOnline(bo.getIsOnline());
						else
							sto.setOnline(false);


						//raghu added newly setting supply/improve into sub list
						if(bo.getIsSupplementary()!=null)
							sto.setIsSupplementary(bo.getIsSupplementary());

						//raghu added newly setting supply/improve into sub list
						if(bo.getIsImprovement()!=null)
							sto.setIsImprovement(bo.getIsImprovement());

						//raghu added bcz of athira
						if(bo.getIsSupplementary())
							newSupplementaryImpApplicationForm.setSupplementary(bo.getIsSupplementary());

						newSupplementaryImpApplicationForm.setExamType("Supplementary/Improvement");


						subjectList.add(sto);
						to.setToList(subjectList);
						if(bo.getIsSupplementary()){
							if ((sto.isTempChecked() != sto.getIsFailedTheory()) || (sto.isTempPracticalChecked() != sto .getIsFailedPractical())){
								buttonDisplay = true;
							}
						}
						if(bo.getIsImprovement()){
							if ((sto.isTheory() && !sto.isTempChecked()) || (!sto.isTempPracticalChecked() && sto.isPractical())){
								buttonDisplay = true;
							}
						}

						//raghu added from mounts


						if(bo.getCiaExam()!= null)
						{
							if(bo.getCiaExam())

							{

								sto.setCiaExam(true);
								sto.setIsFailedTheory(bo.getIsCIAFailedTheory());
								sto.setIsFailedPractical(bo.getIsCIAFailedPractical());
								if (bo.getIsCIAAppearedTheory() != null)
								{
									sto.setTempChecked(bo.getIsCIAAppearedTheory());
									if(bo.getIsCIAAppearedTheory())
									{
										sto.setTempCIAExamChecked(true);
									}
									else
									{
										sto.setTempCIAExamChecked(false);
									}
								}
								else
								{
									sto.setTempChecked(false);
								}
								if (bo.getIsCIAAppearedPractical() != null)
								{
									sto.setTempPracticalChecked(bo .getIsCIAAppearedPractical());
									if(bo.getIsCIAAppearedPractical())
									{
										sto.setTempCIAExamChecked(true);
									}
									else
									{
										sto.setTempCIAExamChecked(false);
									}
								}
								else
								{
									sto.setTempPracticalChecked(false);
								}
								if(bo.getIsCIAAppearedPractical() || bo.getIsCIAAppearedTheory() )
								{
									sto.setTempCIAExamChecked(true);
								}
								if(bo.getIsCIAAppearedPractical()!=null)
								{
									sto.setIsCIAAppearedPractical(bo.getIsCIAAppearedPractical());
								}
								else
								{
									sto.setIsCIAAppearedPractical(false);
								}
								if(bo.getIsCIAAppearedTheory()!=null)
								{
									sto.setIsCIAAppearedTheory(bo.getIsCIAAppearedTheory());
								}
								else
								{
									sto.setIsCIAAppearedTheory(false);
								}
								if(bo.getIsCIAFailedPractical()!=null)
								{
									sto.setIsCIAFailedPractical(bo.getIsCIAFailedPractical());
								}
								else
								{
									sto.setIsCIAFailedPractical(false);
								}
								if(bo.getIsCIAFailedTheory()!=null)
								{
									sto.setIsCIAFailedTheory(bo.getIsCIAFailedTheory());
								}
								else
								{
									sto.setIsCIAFailedTheory(false);
								}
							}
							else
							{
								sto.setTempCIAExamChecked(false);
								sto.setIsCIAAppearedPractical(false);
								sto.setIsCIAAppearedTheory(false);
								sto.setIsCIAFailedPractical(false);
								sto.setIsCIAFailedTheory(false);
								sto.setCiaExam(false);
							}
						}
						//
						else
						{
							sto.setTempCIAExamChecked(false);
							sto.setIsCIAAppearedPractical(false);
							sto.setIsCIAAppearedTheory(false);
							sto.setIsCIAFailedPractical(false);
							sto.setIsCIAFailedTheory(false);
							sto.setCiaExam(false);
						}
						if(bo.getCiaExam()!=null)
						{
							if(bo.getCiaExam())
							{
								sto.setIsESE(false);
							}
							else 
							{
								if(bo.getIsAppearedPractical()|| bo.getIsAppearedTheory() )
								{
									sto.setIsESE(true);
								}
								else
								{
									sto.setIsESE(false);
								}
							}

						}
						else 
						{
							if(bo.getIsAppearedPractical()|| bo.getIsAppearedTheory() )
							{
								sto.setIsESE(true);
							}
							else
							{
								sto.setIsESE(false);
							}
						}
						newSupplementaryImpApplicationForm.setPrevClassId(bo.getClasses().getId()+"");
						classList.add(to);
					}
				}
			}

			// Ashwini did for calculating supply fees


			if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection()!=null)
			{
				String religionSection = newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection().getName();

				if(religionSection.equalsIgnoreCase("SC") || religionSection.equalsIgnoreCase("ST") || religionSection.equalsIgnoreCase("OEC"))
				{	
					newSupplementaryImpApplicationForm.setIsFeesExempted(true);
					newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
					// set journalNo for payment through challan
					newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
				}
			/*	else if(religionSection.equalsIgnoreCase("OBC") || religionSection.equalsIgnoreCase("OTHERS"))
					
					if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId().getId()==1)
					{
						newSupplementaryImpApplicationForm.setIsFeesExempted(true);
						newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
						newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");	
						newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
						// set journalNo for payment through challan
						newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
					}*/
					else{
						newSupplementaryImpApplicationForm.setIsFeesExempted(false);
						// set journalNo for payment through challan
						if(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()!=null){
							newSupplementaryImpApplicationForm.setJournalNo("SUP"+newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()+newSupplementaryImpApplicationForm.getExamId());
						}


						query="from PublishSupplementaryImpApplication p"+
						" where p.classCode.id="+newSupplementaryImpApplicationForm.getPrevClassId()+
						" and p.exam.id=" +Integer.parseInt(newSupplementaryImpApplicationForm.getExamId());
						List<PublishSupplementaryImpApplication> fineDetails=transaction.getDataForQuery(query);
						PublishSupplementaryImpApplication supplFees=null;
						if(fineDetails!=null){
							supplFees=fineDetails.get(0);
						}
						totalfee+=cvCampFee+applicationFee+marksListFee+onlineServiceFees;
						if(newSupplementaryImpApplicationForm.isExtended())
							if(transaction1.checkSubmitSuppApp(newSupplementaryImpApplicationForm))
							{
								newSupplementaryImpApplicationForm.setExtended(false);
							}
						if(newSupplementaryImpApplicationForm.isExtended())
						{
							double fine=0.0;
							if(newSupplementaryImpApplicationForm.getIsFine()){
								fine=Double.parseDouble(supplFees.getFineAmount());
								newSupplementaryImpApplicationForm.setFineFees(fine);
							}
							else if(newSupplementaryImpApplicationForm.getIsSuperFine()){
								fine=Double.parseDouble(supplFees.getSuperFineAmount());
								newSupplementaryImpApplicationForm.setFineFees(fine);
							}
							totalfee+=fine;
						}

						else
						{
							newSupplementaryImpApplicationForm.setFineFees(0);
						}

						/*//newSupplementaryImpApplicationForm.setTotalFees(totalfee);
						newSupplementaryImpApplicationForm.setTotalFeesInWords(CommonUtil.numberToWord((int)totalfee));
						newSupplementaryImpApplicationForm.setApplicationAmount(totalfee+"");
						newSupplementaryImpApplicationForm.setApplicationAmount1(totalfee+"");	
						newSupplementaryImpApplicationForm.setApplicationAmountWords(CommonUtil.numberToWord((int)totalfee));*/
					}
			}
		


			eto.setExamList(classList);
			examList.add(eto);
		}
		newSupplementaryImpApplicationForm.setChallanButton(challanDisplay);
		if(challanDisplay){
			newSupplementaryImpApplicationForm.setDisplayButton(false);
			newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		}
		else
		newSupplementaryImpApplicationForm.setDisplayButton(buttonDisplay);
		newSupplementaryImpApplicationForm.setIsAppliedAlready(isFormSubmitted);
		newSupplementaryImpApplicationForm.setMainList(examList);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 */
	public boolean saveSupplementaryApplicationForStudentLogin( NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		OnlinePaymentReciepts onlinePaymentReciepts=new OnlinePaymentReciepts();
		PcFinancialYear pcFinancialYear=new PcFinancialYear();
		pcFinancialYear.setId(newSupplementaryImpApplicationForm.getFinId());
		onlinePaymentReciepts.setPcFinancialYear(pcFinancialYear);
		Student student = new Student();
		student.setId(newSupplementaryImpApplicationForm.getStudentId());
		onlinePaymentReciepts.setStudent(student);
		onlinePaymentReciepts.setTotalAmount(new BigDecimal(newSupplementaryImpApplicationForm.getTotalFees()));
		onlinePaymentReciepts.setIsActive(true);
		onlinePaymentReciepts.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
		onlinePaymentReciepts.setCreatedDate(new Date());
		onlinePaymentReciepts.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
		onlinePaymentReciepts.setLastModifiedDate(new Date());
		onlinePaymentReciepts.setApplicationFor("Supplementary Application");
		//raghu  added from mounts
		onlinePaymentReciepts.setIsPaymentFailed(Boolean.FALSE);
		
		PropertyUtil.getInstance().save(onlinePaymentReciepts);
		
		//raghu added from mounts
		//boolean isPaymentSuccess=false;
		boolean isPaymentSuccess=true;
		
		
		if(onlinePaymentReciepts.getId()>0){
			newSupplementaryImpApplicationForm.setOnlinePaymentId(onlinePaymentReciepts.getId());
			//raghu added from mounts
			//OnlinePaymentUtils.dedactAmountFromAccount(registerNo,onlinePaymentReciepts.getId(), newSupplementaryImpApplicationForm.getTotalFees(), onlinePaymentReciepts);
			
			if(!onlinePaymentReciepts.getIsPaymentFailed()){
				isPaymentSuccess=true;
				transaction.updateAndGenerateRecieptNoOnlinePaymentReciept(onlinePaymentReciepts);
			}else
				newSupplementaryImpApplicationForm.setMsg(onlinePaymentReciepts.getStatus());
		}
		if(isPaymentSuccess){
		List<StudentSupplementaryImprovementApplication> boList=new ArrayList<StudentSupplementaryImprovementApplication>();
		List<SupplementaryAppExamTo> examList=newSupplementaryImpApplicationForm.getMainList();
		for (SupplementaryAppExamTo suppTo : examList) {
			ExamDefinition examDefinition = new ExamDefinition();
			examDefinition.setId(suppTo.getExamId());
			List<SupplementaryApplicationClassTo> classTos=suppTo.getExamList();
			for (SupplementaryApplicationClassTo cto: classTos) {
				Classes classes = new Classes();
				classes.setId(cto.getClassId());
				List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto.getToList();
				for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
					StudentSupplementaryImprovementApplication objBO = new StudentSupplementaryImprovementApplication();
					if(to.getId()!=null)
						objBO.setId(to.getId());
					objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setCreatedDate(new Date());
					objBO.setLastModifiedDate(new Date());
					objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setStudent(student);
					objBO.setExamDefinition(examDefinition);
					Subject subject = new Subject();
					subject.setId(to.getSubjectId());
					objBO.setSubject(subject);
					
					//raghu change code for both imp/sup
					//if(cto.isSupplementary()){
					if(to.getIsSupplementary()){
						
						
						//raghu added from mounts
						// new modification
						// if cia selected then all the property in cse is 0 and property in cia get fille

						if(to.isCiaExam())
						{
							if (to.getFailedTheory() != null) {
								objBO.setIsFailedTheory(false);
							} else {
								objBO .setIsFailedTheory(false);
							}
							if (to.getFailedPractical() != null) {
								objBO.setIsFailedPractical(false);
							} else {
								objBO.setIsFailedPractical(false);
							}
							double fees=0;
							if(to.getPreviousFees()>0)
								fees=fees+to.getPreviousFees();
							boolean isEntered=false;
							if(!to.isTempChecked()){
								objBO.setIsAppearedTheory(false);
								if(to.getIsAppearedTheory()){
									fees=fees+newSupplementaryImpApplicationForm.getTheoryFees();
									objBO.setIsOnline(true);
									isEntered=true;
								}else
									objBO.setIsOnline(to.isOnline());
							}else{
								objBO.setIsAppearedTheory(false);
								objBO.setIsOnline(to.isOnline());
							}
							if(!to.isTempPracticalChecked()){
								objBO.setIsAppearedPractical(false);
								if(to.getIsAppearedPractical()){
									fees=fees+newSupplementaryImpApplicationForm.getPracticalFees();
									if(!isEntered){
										objBO.setIsOnline(true);
										isEntered=true;
									}
								}else{
									if(!isEntered)
										objBO.setIsOnline(to.isOnline());
								}
							}else{
								objBO.setIsAppearedPractical(false);
								if(!isEntered)
									objBO.setIsOnline(to.isOnline());
							}
							objBO.setFees(String.valueOf(fees));
							// new feilds
							if (to.getFailedTheory() != null) {
								objBO.setIsCIAFailedTheory(true);
							} else {
								objBO.setIsCIAFailedTheory((to.getIsCIAFailedTheory()) ? true : false);
							}
							if (to.getFailedPractical() != null) {
								objBO.setIsCIAFailedPractical(true);
							} else {
								objBO.setIsCIAFailedPractical((to.getIsCIAFailedPractical()) ? true : false);
							}
							if(!to.isTempChecked()){
								objBO.setIsCIAAppearedTheory(to.getIsCIAAppearedTheory());
								if(to.getIsCIAAppearedTheory()){
									fees=fees+newSupplementaryImpApplicationForm.getTheoryFees();
									objBO.setIsOnline(true);
									isEntered=true;
								}else
									objBO.setIsOnline(to.isOnline());
							}else{
								objBO.setIsCIAAppearedTheory(true);
								objBO.setIsOnline(to.isOnline());
							}
							if(!to.isTempPracticalChecked()){
								objBO.setIsCIAAppearedPractical(to.getIsCIAAppearedPractical());
								if(to.getIsCIAAppearedPractical()){
									fees=fees+newSupplementaryImpApplicationForm.getPracticalFees();
									if(!isEntered){
										objBO.setIsOnline(true);
										isEntered=true;
									}
								}else{
									if(!isEntered)
										objBO.setIsOnline(to.isOnline());
								}
							}else{
								objBO.setIsCIAAppearedPractical(true);
								if(!isEntered)
									objBO.setIsOnline(to.isOnline());
							}
							if(to.getIsAppearedTheory() || to.isTempChecked())
							{
								objBO.setIsCIAAppearedTheory(true);
								objBO.setIsCIAFailedTheory(true);
								objBO.setIsCIAFailedPractical(false);
								objBO.setIsCIAAppearedPractical(false);
								objBO.setCiaExam(true);

							}
							else if(to.getIsAppearedPractical() || to.isTempPracticalChecked())
							{
								objBO.setIsCIAAppearedTheory(false);
								objBO.setIsCIAFailedTheory(false);
								objBO.setIsCIAFailedPractical(true);
								objBO.setIsCIAAppearedPractical(true);
								objBO.setCiaExam(true);
							}
							else
							{
								objBO.setIsCIAAppearedTheory(false);
								objBO.setIsCIAFailedTheory(false);
								objBO.setIsCIAFailedPractical(false);
								objBO.setIsCIAAppearedPractical(false);
								objBO.setCiaExam(false);

								objBO.setIsFailedPractical(to.getIsFailedPractical());
								objBO.setIsAppearedPractical(false);
								objBO.setIsAppearedTheory(false);
								objBO.setIsFailedTheory(to.getIsFailedTheory());
							}

						}
						else // not cia
						{
					
						
						
						if (to.getFailedTheory() != null) {
							objBO.setIsFailedTheory(true);
						} else {
							objBO .setIsFailedTheory((to.getIsFailedTheory()) ? true : false);
						}
						if (to.getFailedPractical() != null) {
							objBO.setIsFailedPractical(true);
						} else {
							objBO.setIsFailedPractical((to.getIsFailedPractical()) ? true : false);
						}
						double fees=0;
						if(to.getPreviousFees()>0)
							fees=fees+to.getPreviousFees();
						boolean isEntered=false;
						if(!to.isTempChecked()){
							objBO.setIsAppearedTheory(to.getIsAppearedTheory());
							if(to.getIsAppearedTheory()){
								fees=fees+newSupplementaryImpApplicationForm.getTheoryFees();
								objBO.setIsOnline(true);
								isEntered=true;
							}else
								objBO.setIsOnline(to.isOnline());
						}else{
							objBO.setIsAppearedTheory(true);
							objBO.setIsOnline(to.isOnline());
						}
						if(!to.isTempPracticalChecked()){
							objBO.setIsAppearedPractical(to.getIsAppearedPractical());
							if(to.getIsAppearedPractical()){
								fees=fees+newSupplementaryImpApplicationForm.getPracticalFees();
								if(!isEntered){
								objBO.setIsOnline(true);
								isEntered=true;
								}
							}else{
								if(!isEntered)
								objBO.setIsOnline(to.isOnline());
							}
						}else{
							objBO.setIsAppearedPractical(true);
							if(!isEntered)
							objBO.setIsOnline(to.isOnline());
						}
						objBO.setFees(String.valueOf(fees));
						
						
						
						//raghu added from mounts
						
						// nee field
						objBO.setIsCIAAppearedPractical(false);
						objBO.setIsCIAAppearedTheory(false);
						objBO.setIsCIAFailedPractical(false);
						objBO.setIsCIAFailedTheory(false);
						objBO.setCiaExam(false);

					}
						
						
						
					}
						
						
					
					
					
					//raghu change code for both imp/sup
					//if(cto.isImprovement()){
					if(to.getIsImprovement()){
						
						double fees=0;
						if(to.getPreviousFees()>0)
							fees=fees+to.getPreviousFees();
						boolean isEntered=false;
						if(!to.isTempChecked()){/*
							objBO.setIsAppearedTheory(to.getIsAppearedTheory());
							//objBO.setIsFailedTheory(to.getIsAppearedTheory());
							//raghu put for improvement
							objBO.setIsFailedTheory(to.getIsFailedTheory());
							
							if(to.getIsAppearedTheory()){
								fees=fees+newSupplementaryImpApplicationForm.getTheoryFees();
								objBO.setIsOnline(true);
								isEntered=true;
							}else
								objBO.setIsOnline(to.isOnline());
						*/
							
						

							objBO.setIsAppearedTheory(to.getIsAppearedTheory());
							//objBO.setIsFailedTheory(to.getIsAppearedTheory());
							//raghu put for improvement
							//objBO.setIsFailedTheory(to.getIsFailedTheory());
							objBO.setIsFailedTheory(false);
							if(to.getIsAppearedTheory()){
								fees=fees+newSupplementaryImpApplicationForm.getTheoryFees();
								objBO.setIsOnline(true);
								isEntered=true;
							}else
								objBO.setIsOnline(to.isOnline());
						
						}else{/*
							objBO.setIsAppearedTheory(true);
							objBO.setIsFailedTheory(true);
							objBO.setIsOnline(to.isOnline());
							*/
							

							objBO.setIsAppearedTheory(true);
							//objBO.setIsFailedTheory(true);
							objBO.setIsFailedTheory(false);
							objBO.setIsOnline(to.isOnline());
						
						}
						if(!to.isTempPracticalChecked()){/*
							objBO.setIsAppearedPractical(to.getIsAppearedPractical());
							//objBO.setIsFailedPractical(to.getIsAppearedPractical());
							//raghu put for improvement
							objBO.setIsFailedPractical(to.getIsFailedPractical());
							
							if(to.getIsAppearedPractical()){
								fees=fees+newSupplementaryImpApplicationForm.getPracticalFees();
								if(!isEntered){
								objBO.setIsOnline(true);
								isEntered=true;
								}
							}else{
								if(!isEntered)
								objBO.setIsOnline(to.isOnline());
							}
						*/
							

							objBO.setIsAppearedPractical(to.getIsAppearedPractical());
							//objBO.setIsFailedPractical(to.getIsAppearedPractical());
							//raghu put for improvement
							//objBO.setIsFailedPractical(to.getIsFailedPractical());
							objBO.setIsFailedPractical(false);
							if(to.getIsAppearedPractical()){
								fees=fees+newSupplementaryImpApplicationForm.getPracticalFees();
								if(!isEntered){
								objBO.setIsOnline(true);
								isEntered=true;
								}
							}else{
								if(!isEntered)
								objBO.setIsOnline(to.isOnline());
							}
						
						
						}else{/*
							objBO.setIsAppearedPractical(true);
							objBO.setIsFailedPractical(true);
							if(!isEntered)
							objBO.setIsOnline(to.isOnline());
							*/

							objBO.setIsAppearedPractical(true);
							//objBO.setIsFailedPractical(true);
							objBO.setIsFailedPractical(false);
							if(!isEntered)
							objBO.setIsOnline(to.isOnline());
							
						}
						objBO.setFees(String.valueOf(fees));
					}
					objBO.setSchemeNo(to.getSchemeNo());
					objBO.setChance(to.getChance());
					if(to.getIsOverallTheoryFailed()!=null)
						objBO.setIsTheoryOverallFailed(to.getIsOverallTheoryFailed());
					else
						objBO.setIsTheoryOverallFailed(false);
					if(to.getIsOverallPracticalFailed()!=null)
						objBO.setIsPracticalOverallFailed(to.getIsOverallPracticalFailed());
					else
						objBO.setIsPracticalOverallFailed(false);
					objBO.setClasses(classes);
					/*objBO.setIsSupplementary(true);
					objBO.setIsImprovement(false);*/
					objBO.setLastModifiedDate(new Date());
					
					//raghu change code for both imp/sup
					//if (cto.isSupplementary()) {
					if (to.getIsSupplementary()) {
						objBO.setIsSupplementary(true);
						objBO.setIsImprovement(false);
					} 
					
					//raghu change code for both imp/sup
					//else if (cto.isImprovement()) {
					else if (to.getIsImprovement()) {
						objBO.setIsImprovement(true);
						objBO.setIsSupplementary(false);
					}
					boList.add(objBO);
				}
			}
			
		}
		return transaction.saveSupplementarys(boList);
		}else
			return false;
	}

	/**
	 * @param smartCardNo
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public boolean verifySmartCard(String smartCardNo, int studentId) throws Exception {
		String query="select s.smartCardNo from Student s where s.id="+studentId+" and s.smartCardNo like '%"+smartCardNo+"'";
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		List list=txn.getDataForQuery(query);
		if(list!=null && !list.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @param onlinePaymentId
	 * @return
	 * @throws Exception
	 */
	public String getPrintData(int onlinePaymentId,HttpServletRequest request) throws Exception {
		ISingleFieldMasterTransaction txn=SingleFieldMasterTransactionImpl.getInstance();
		OnlinePaymentReciepts bo=(OnlinePaymentReciepts) txn.getMasterEntryDataById(OnlinePaymentReciepts.class,onlinePaymentId);
		List<GroupTemplate> list= TemplateHandler.getInstance().getDuplicateCheckList(0,CMSConstants.TEMPLATE_SUPPLEMENTARY_APPLICATION_PRINT);
		return NewSupplementaryImpApplicationHelper.getInstance().getPrintData(bo,list,request);
	}
	
	
	
	//raghu added from mounts
	
	public void setAppNoAndDate(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		log.debug("call of setAppNoAndDate mehtod in  newSupplementaryImpApplicationHandler.class");
		INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
		newSupplementaryImpApplicationForm.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()), CMSConstants.SOURCE_DATE,CMSConstants.DEST_DATE));
		String appNo = txn.getApplicationNumber(newSupplementaryImpApplicationForm.getStudentId());
		newSupplementaryImpApplicationForm.setApplicationNumber(appNo);
		log.debug("end of setAppNoAndDate mehtod in  newSupplementaryImpApplicationHandler.class");

	}
	
	//raghu added for improvement
	public List<Integer> checkSuppImpAppAvailable1(int studentId) throws Exception {
		String query="select p.exam.id from PublishSupplementaryImpApplication p " +
				" where p.isActive=1 and p.exam.delIsActive=1 and p.exam.id in ( select s.examDefinition.id from StudentSupplementaryImprovementApplication s" +
				" where s.student.id= " +studentId+
				//raghu added from mounts
				//" and (((s.isFailedPractical=1 or s.isFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) and (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classCode.id)) " +
				//" and (((s.isFailedPractical=1 or s.isFailedTheory=1 or s.isCIAFailedPractical=1 or s.isCIAFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) and (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classCode.id)) " +
				
				" and (((s.isFailedPractical=1 or s.isFailedTheory=1 or s.isCIAFailedPractical=1 or s.isCIAFailedTheory=1) and s.isSupplementary=1) or s.isImprovement=1) and s.classes.id in(p.classCode.id)) " +
				
				
				/*"and '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between (p.startDate and p.endDate)  group by p.exam.id ";*/
		" and (('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.endDate) or ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.startDate and p.extendedDate)) group by p.exam.id "; 
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		return transaction.getDataForQuery(query);
	}
	
	
	
	
	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 */
	//raghu check updateprocess has done/ not for supply
	public String checkValidAll(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		String msg="";
		
			String query="from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
			" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId();
			List<StudentSupplementaryImprovementApplication> boList=transaction.getDataForQuery(query);
			if( boList.isEmpty() && boList.size()==0){
				msg="Do UpdateProcess For Supplementary";
			}
		
		return msg;
	}
	
	
	
	
	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 */
	//raghu check updateprocess has done/ not for supply
	public String checkImpValidAll(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		String msg="";
		
			String query="from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
			" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+" and s.isImprovement=1";
			List<StudentSupplementaryImprovementApplication> boList=transaction.getDataForQuery(query);
			if(!boList.isEmpty() && boList.size()!=0){
				msg="Already Improvement Has Done";
			}
		
		return msg;
	}
	
	
	
	
	
	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	//raghu
	public List<ExamSupplementaryImpApplicationTO> getStudentDetailsAll(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationTO> list= new LinkedList<ExamSupplementaryImpApplicationTO>();
		List<Object[]> studentDetails=new ArrayList<Object[]>();
		
		String previousQuery="select s.student,s.classes from StudentPreviousClassHistory s " +
		" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
		//" and s.student.id="+newSupplementaryImpApplicationForm.getStudentId();
		//" and s.classes.id in(select s.classes.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")"+
		" and s.student.isAdmitted=1 and s.student.admAppln.isCancelled=0	and s.academicYear=(select e.academicYear from ExamDefinitionBO e where e.id="+newSupplementaryImpApplicationForm.getExamId()+")"+
		" and s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
		" and s.student.id not in(select s.student.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")";

		
		List previousList=transaction.getDataForQuery(previousQuery);
		if(previousList.isEmpty()){
			String currentQuery="select s,s.classSchemewise.classes from Student s " +
			//"where s.id=" +newSupplementaryImpApplicationForm.getStudentId()+
			" where s.classSchemewise.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
			" and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo()+
			" and s.isAdmitted=1 and s.admAppln.isCancelled=0	 and s.classSchemewise.curriculumSchemeDuration.academicYear=(select e.academicYear from ExamDefinitionBO e where e.id="+newSupplementaryImpApplicationForm.getExamId()+")"+
			" and s.id not in(select s.student.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")";
		//and s.classSchemewise.classes.id in(select s.classes.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")";
		
			List currentList=transaction.getDataForQuery(currentQuery);
			studentDetails.addAll(currentList);
		}else{
			studentDetails.addAll(previousList);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		ExamSupplementaryImpApplicationTO to =null;
		if(!studentDetails.isEmpty()){
			Iterator<Object[]> itr=studentDetails.iterator();
			while (itr.hasNext()) {
				Object[] objects = (Object[]) itr.next();
				Student student=(Student)objects[0];
				Classes classes=(Classes)objects[1];
				List<Subject> subList=new ArrayList<Subject>();
				
				
				
				//String subPreQuery="select subGrp.subject from StudentSubjectGroupHistory s join s.subjectGroup.subjectGroupSubjectses subGrp" +
				//" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
				//" and s.student.id="+student.getId();
				
				String subPreQuery="select  m.subject from MarksEntryDetails m where m.marksEntry.student.id="+student.getId()+" and m.marksEntry.exam.examTypeUtilBO.name='Regular' and  m.marksEntry.classes.termNumber=" +newSupplementaryImpApplicationForm.getSchemeNo()+" and m.marksEntry.exam.academicYear="+newSupplementaryImpApplicationForm.getYear()+" group by m.subject.id";
				// raghu change query bcz they enterd wrong data in student subject group
				
				List<Subject> preList=transaction.getDataForQuery(subPreQuery);
				if(!preList.isEmpty()){
					subList.addAll(preList);
				}
				
				String subQuery="select subGrp.subject from Student s join s.admAppln.applicantSubjectGroups appSub join appSub.subjectGroup.subjectGroupSubjectses subGrp" +
				" where s.id=" +student.getId()+
				" and subGrp.isActive=1 and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo();
				
				List<Subject> suList=transaction.getDataForQuery(subQuery);
				//subList.addAll(suList);
		
		
				if(subList.isEmpty()){
					subList.addAll(suList);
				}
				
		
		
				
				
				
				
				
				to=new ExamSupplementaryImpApplicationTO();
				to.setClassName(classes.getName());
				to.setCourseId(classes.getCourse().getId());
				to.setCourseName(classes.getCourse().getName());
				to.setClassId(classes.getId());
				to.setSection(classes.getSectionName());
				to.setStudentId(student.getId());
				StringBuffer stuName=new StringBuffer();
				if(student.getAdmAppln().getPersonalData().getFirstName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getFirstName());
				if(student.getAdmAppln().getPersonalData().getMiddleName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getMiddleName());
				if(student.getAdmAppln().getPersonalData().getLastName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getLastName());
				
				to.setStudentName(stuName.toString());
				to.setRegNumber(student.getRegisterNo());
				to.setRollNumber(student.getRollNo());
				to.setSemester_year_no(classes.getTermNumber());
				to.setExamId(Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()));
				List<ExamSupplementaryImpApplicationSubjectTO> subjectList=new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
				Iterator<Subject> subitr=subList.iterator();
				while (subitr.hasNext()) {
					Subject bo = subitr .next();
					ExamSupplementaryImpApplicationSubjectTO sto=new ExamSupplementaryImpApplicationSubjectTO();
					sto.setSubjectId(bo.getId());
					sto.setSubjectCode(bo.getCode());
					sto.setSubjectName(bo.getName());
					sto.setIsOverallPracticalFailed(false);
					sto.setIsOverallTheoryFailed(false);
					sto.setChance(0);
					sto.setIsFailedTheory(false);
					sto.setIsFailedPractical(false);
					sto.setTempChecked(false);
					sto.setTempPracticalChecked(false);
					sto.setControlDisable(false);
					sto.setClassId(to.getClassId());
					subjectList.add(sto);
				}// sub while close
				
				to.setSubjectList(subjectList);
				list.add(to);
				
			}// student while close
		}// empty close


		
		
		
	return list;
	}

	
	
	
	
	
	
	
	
	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	//raghu
	public List<ExamSupplementaryImpApplicationTO> getStudentDetailsAllSupply(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationTO> list= new LinkedList<ExamSupplementaryImpApplicationTO>();
		List<Object[]> studentDetails=new ArrayList<Object[]>();
		
		String previousQuery="select s.student,s.classes from StudentPreviousClassHistory s " +
				" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
				//" and s.student.id="+newSupplementaryImpApplicationForm.getStudentId();
				" and s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
				" and s.student.id in(select s.student.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")";
		
		
		List previousList=transaction.getDataForQuery(previousQuery);
		if(previousList.isEmpty()){
			String currentQuery="select s,s.classSchemewise.classes from Student s " +
				//"where s.id=" +newSupplementaryImpApplicationForm.getStudentId()+
				" where s.classSchemewise.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
				" and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo()+
				" and s.id in(select s.student.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+")";
		
			List currentList=transaction.getDataForQuery(currentQuery);
			studentDetails.addAll(currentList);
		}else{
			studentDetails.addAll(previousList);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		ExamSupplementaryImpApplicationTO to =null;
		if(!studentDetails.isEmpty()){
			Iterator<Object[]> itr=studentDetails.iterator();
			while (itr.hasNext()) {
				Object[] objects = (Object[]) itr.next();
				Student student=(Student)objects[0];
				Classes classes=(Classes)objects[1];
				List<Subject> subList=new ArrayList<Subject>();
				
				
				
		
				//String subPreQuery="select subGrp.subject from StudentSubjectGroupHistory s join s.subjectGroup.subjectGroupSubjectses subGrp" +
				//" where s.schemeNo=" +newSupplementaryImpApplicationForm.getSchemeNo()+
				//" and s.student.id="+student.getId()+
				//" and subGrp.subject.id not in(select s.subject.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+" and s.student.id="+student.getId()+")";
		
				
				String subPreQuery="select  m.subject from MarksEntryDetails m where m.marksEntry.student.id="+student.getId()+" and m.marksEntry.exam.examTypeUtilBO.name='Regular' and  m.marksEntry.classes.termNumber=" +newSupplementaryImpApplicationForm.getSchemeNo()+
				"  and m.subject.id not in(select s.subject.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+" and s.student.id="+student.getId()+")  group by m.subject.id";
				
				// raghu change query bcz they enterd wrong data in student subject group
				
				List<Subject> preList=transaction.getDataForQuery(subPreQuery);
				if(!preList.isEmpty()){
					subList.addAll(preList);
				}
				
				String subQuery="select subGrp.subject from Student s join s.admAppln.applicantSubjectGroups appSub join appSub.subjectGroup.subjectGroupSubjectses subGrp" +
				" where s.id=" +student.getId()+
				" and s.classSchemewise.classes.termNumber="+newSupplementaryImpApplicationForm.getSchemeNo()+
				" and subGrp.isActive=1 and subGrp.subject.id not in(select s.subject.id from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+" and s.student.id="+student.getId()+")";
				
				List<Subject> suList=transaction.getDataForQuery(subQuery);
				//subList.addAll(suList);
		
		
				if(subList.isEmpty()){
					subList.addAll(suList);
				}

				
				
				
				
				
				to=new ExamSupplementaryImpApplicationTO();
				to.setClassName(classes.getName());
				to.setCourseId(classes.getCourse().getId());
				to.setCourseName(classes.getCourse().getName());
				to.setClassId(classes.getId());
				to.setSection(classes.getSectionName());
				to.setStudentId(student.getId());
				StringBuffer stuName=new StringBuffer();
				if(student.getAdmAppln().getPersonalData().getFirstName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getFirstName());
				if(student.getAdmAppln().getPersonalData().getMiddleName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getMiddleName());
				if(student.getAdmAppln().getPersonalData().getLastName()!=null)
					stuName.append(student.getAdmAppln().getPersonalData().getLastName());
				
				to.setStudentName(stuName.toString());
				to.setRegNumber(student.getRegisterNo());
				to.setRollNumber(student.getRollNo());
				to.setSemester_year_no(classes.getTermNumber());
				to.setExamId(Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()));
				List<ExamSupplementaryImpApplicationSubjectTO> subjectList=new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
				Iterator<Subject> subitr=subList.iterator();
				while (subitr.hasNext()) {
					Subject bo = subitr .next();
					ExamSupplementaryImpApplicationSubjectTO sto=new ExamSupplementaryImpApplicationSubjectTO();
					sto.setSubjectId(bo.getId());
					sto.setSubjectCode(bo.getCode());
					sto.setSubjectName(bo.getName());
					sto.setIsOverallPracticalFailed(false);
					sto.setIsOverallTheoryFailed(false);
					sto.setChance(0);
					sto.setIsFailedTheory(false);
					sto.setIsFailedPractical(false);
					sto.setTempChecked(false);
					sto.setTempPracticalChecked(false);
					sto.setControlDisable(false);
					sto.setClassId(to.getClassId());
					subjectList.add(sto);
				}// sub while close
				
				to.setSubjectList(subjectList);
				list.add(to);
				
			}// student while close
		}// empty close

		
		
	
	return list;
	}

	

	
	
	
	
	
	/**
	 * @param newSupplementaryImpApplicationForm
	 * @return
	 * @throws Exception
	 */
	public boolean saveSupplementaryApplicationAll( NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		
		
		boolean isAdded=false;
		
		try{
			
		
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<StudentSupplementaryImprovementApplication> boList=new ArrayList<StudentSupplementaryImprovementApplication>();
		List<ExamSupplementaryImpApplicationTO> suppToList = newSupplementaryImpApplicationForm .getSuppToList();
		Iterator<ExamSupplementaryImpApplicationTO> iterator=suppToList.iterator();
		
		while(iterator.hasNext()){
			
		ExamSupplementaryImpApplicationTO suppTo=iterator.next();
		//ExamSupplementaryImpApplicationTO suppTo = newSupplementaryImpApplicationForm .getSuppTo();
			
		ExamDefinition examDefinition = new ExamDefinition();
		examDefinition.setId(suppTo.getExamId());
		Student student = new Student();
		student.setId(suppTo.getStudentId());
		Classes classes = new Classes();
		classes.setId(suppTo.getClassId());
		List<ExamSupplementaryImpApplicationSubjectTO> listSubject = suppTo .getSubjectList();
		for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
			StudentSupplementaryImprovementApplication objBO = new StudentSupplementaryImprovementApplication();
			if(to.getId()!=null)
				objBO.setId(to.getId());
			objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
			objBO.setCreatedDate(new Date());
			objBO.setLastModifiedDate(new Date());
			objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
			objBO.setStudent(student);
			objBO.setExamDefinition(examDefinition);
			Subject subject = new Subject();
			subject.setId(to.getSubjectId());
			objBO.setSubject(subject);
			if (to.getFailedTheory() != null) {
				objBO.setIsFailedTheory(true);
			} else {
				objBO .setIsFailedTheory((to.getIsFailedTheory()) ? true : false);
			}
			if (to.getFailedPractical() != null) {
				objBO.setIsFailedPractical(true);
			} else {
				objBO.setIsFailedPractical((to.getIsFailedPractical()) ? true : false);
			}
			objBO.setIsAppearedTheory(to.getIsAppearedTheory());
			objBO.setIsAppearedPractical(to.getIsAppearedPractical());
			objBO.setFees(to.getFees());
			objBO.setSchemeNo(suppTo.getSemester_year_no());
			objBO.setChance(to.getChance());
			if(to.getIsOverallTheoryFailed()!=null)
				objBO.setIsTheoryOverallFailed(to.getIsOverallTheoryFailed());
			else
				objBO.setIsTheoryOverallFailed(false);
			if(to.getIsOverallPracticalFailed()!=null)
				objBO.setIsPracticalOverallFailed(to.getIsOverallPracticalFailed());
			else
				objBO.setIsPracticalOverallFailed(false);
			objBO.setClasses(classes);
			if (newSupplementaryImpApplicationForm .getSupplementaryImprovement() != null && newSupplementaryImpApplicationForm .getSupplementaryImprovement().trim().length() > 0) {
				if (newSupplementaryImpApplicationForm .getSupplementaryImprovement().equalsIgnoreCase("Supplementary")) {
					objBO.setIsSupplementary(true);
					objBO.setIsImprovement(false);
				} else if (newSupplementaryImpApplicationForm
						.getSupplementaryImprovement().equalsIgnoreCase("Improvement")) {
					objBO.setIsImprovement(true);
					objBO.setIsSupplementary(false);
				}
			}
			objBO.setIsOnline(false);
			
			
			
			
			//raghu added from mounts
			
			objBO.setIsAppearedTheory(to.getIsAppearedTheory());
			objBO.setIsAppearedPractical(to.getIsAppearedPractical());
			
			
			// new modification
			
			
			if(to.isCiaExam())
			{
				if(to.getIsAppearedTheory())
				{
					objBO.setIsAppearedTheory(false);
					objBO.setIsAppearedPractical(false);
					objBO.setIsFailedPractical(false);
					objBO.setIsFailedTheory(false);
					
					// cia
					objBO.setIsCIAAppearedTheory(to.getIsAppearedTheory());
					objBO.setIsCIAFailedTheory(true);
					objBO.setIsCIAFailedPractical(false);
					objBO.setIsCIAAppearedPractical(false);
					objBO.setCiaExam(true);
				}
				if(to.getIsAppearedPractical())
				{
					objBO.setIsAppearedTheory(false);
					objBO.setIsAppearedPractical(false);
					objBO.setIsFailedPractical(false);
					objBO.setIsFailedTheory(false);
					
					// cia
					objBO.setIsCIAAppearedTheory(false);
					objBO.setIsCIAFailedTheory(true);
					objBO.setIsCIAFailedPractical(true);
					objBO.setIsCIAAppearedPractical(to.getIsAppearedPractical());
					objBO.setCiaExam(true);
				}
			}
			else
			{
				objBO.setIsAppearedTheory(to.getIsAppearedTheory());
				objBO.setIsAppearedPractical(to.getIsAppearedPractical());
				objBO.setIsFailedPractical(to.getIsFailedPractical());
				objBO.setIsFailedTheory(to.getIsFailedTheory());
				
				objBO.setIsCIAAppearedTheory(false);
				objBO.setIsCIAFailedTheory(false);
				objBO.setIsCIAFailedPractical(false);
				objBO.setIsCIAAppearedPractical(false);
				objBO.setCiaExam(false);
			}
			
			
			boList.add(objBO);
		}//for loop
		
		isAdded=transaction.saveSupplementarys(boList);
	}//while loop
		
		
		
		}catch (Exception e) {
			
			
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.beginTransaction();
			transaction.begin();
			String query="delete from StudentSupplementaryImprovementApplication s where  s.classes.course.id="+newSupplementaryImpApplicationForm.getCourseId()+
			" and s.schemeNo="+newSupplementaryImpApplicationForm.getSchemeNo()+" and s.examDefinition.id="+newSupplementaryImpApplicationForm.getExamId()+" and s.isImprovement=1";
			
			Query query1=session.createQuery(query);
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			log.error("#############################  error in   newSupplementaryImpApplicationHandler.class while adding all improvement application ################################"+e);
		}
		
		
		return isAdded;	
	}


	//raghu for regular exam application
	
	public List<Integer> checkRegularAppAvailable(int classId) throws Exception {
		String query="select eb.examId from ExamPublishHallTicketMarksCardBO eb where eb.isActive=1 and eb.publishFor='Regular Application' and eb.examDefinitionBO.examTypeUtilBO.name='Regular' and eb.classes.id=" +classId+
				" and ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between eb.downloadStartDate and eb.downloadEndDate)  "; 
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		return transaction.getDataForQuery(query);
	}
	
	
	/**
	 * @param examIds
	 * @param newSupplementaryImpApplicationForm
	 * @throws Exception
	 */
	public void getApplicationFormsForRegular(List<Integer> examIds, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationSubjectTO> subjectList = new LinkedList<ExamSupplementaryImpApplicationSubjectTO>();
		int count=0;
		
		for (Integer examId: examIds) {
			
			String query="";
			query = "from ExamDefinition e where e.id ="+examId;
			List<ExamDefinition> examList=transaction.getDataForQuery(query);
			for (ExamDefinition bo : examList) {
				newSupplementaryImpApplicationForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
				newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
				newSupplementaryImpApplicationForm.setExamName(bo.getName().toUpperCase());
				newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth().toUpperCase())));

				newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
			}
			
			query = "from CourseToDepartment c where c.course.id ="+newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
			List<CourseToDepartment> courseToDepartmentList=transaction.getDataForQuery(query);
			String dep="";
			
			for (CourseToDepartment bo : courseToDepartmentList) {
				if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()==1){
					dep=dep+bo.getDepartment().getName();
				}else if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()>1){
					if(count==0){
						dep=dep+bo.getDepartment().getName();
					}else{
						dep=dep+","+bo.getDepartment().getName();
					}
					
					count++;
				}
				
				newSupplementaryImpApplicationForm.setCourseDep(dep);
				
			}
			
			List boList = null; 
			if(newSupplementaryImpApplicationForm.getPreviousExam()){
				boList=transaction1.getSubjectsListForStudentPrevClass(newSupplementaryImpApplicationForm.getStudentObj(), Integer.parseInt(newSupplementaryImpApplicationForm.getAcademicYear()));
			    if(boList.size()>0){
			    	newSupplementaryImpApplicationForm.setRegularAppAvailable(true);
			    	newSupplementaryImpApplicationForm.setIsPreviousExam("no");
			    }
			}
			else
				boList=transaction1.getSubjectsListForStudent(newSupplementaryImpApplicationForm.getStudentObj(), Integer.parseInt(newSupplementaryImpApplicationForm.getAcademicYear()));
			if(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getTermNumber()==1){
				newSupplementaryImpApplicationForm.setIsPreviousExam("no");
			}
			Iterator i=boList.iterator();
			int theoryCount=0;
			int practicalCount=0;
			while(i.hasNext()) {
				ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
				Object obj[]=(Object[])i.next();
				SubjectUtilBO bo=(SubjectUtilBO)obj[0];
				sto.setSubjectCode(bo.getCode());
				sto.setSubjectName(bo.getName().toUpperCase());
				sto.setSubjectType(bo.getIsTheoryPractical());
				if(bo.getIsTheoryPractical().equalsIgnoreCase("t"))
					theoryCount++;
				if(bo.getIsTheoryPractical().equalsIgnoreCase("p"))
					practicalCount++;
				if(obj[1]!=null){
					sto.setSectionName(obj[1].toString().toUpperCase());
				}else{
					sto.setSectionName("");
				}
				
				subjectList.add(sto);
						
			}
			
			//Set Regular Exam Application Fees
			RegularExamFees regExamFeesBo = transaction1.getRegularExamFees(newSupplementaryImpApplicationForm);
			double totalfee = 0;
			double universityFee=0;
			double lateFinefees = 0;
			if(regExamFeesBo!=null){
				totalfee = Double.parseDouble(regExamFeesBo.getTheoryFees().toString());
				newSupplementaryImpApplicationForm.setTheoryFee(String.valueOf(totalfee));
				//String progtype = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName(); 
				universityFee= Double.parseDouble(regExamFeesBo.getCvCampFees().toString());
				newSupplementaryImpApplicationForm.setUniversityFee(String.valueOf(universityFee));
				
				if(regExamFeesBo.getOnlineServiceChargeFees() != null){
				lateFinefees = Double.parseDouble(regExamFeesBo.getOnlineServiceChargeFees().toString());
				newSupplementaryImpApplicationForm.setOnlineServiceChargeFees(String.valueOf(lateFinefees));
				}else {
					newSupplementaryImpApplicationForm.setOnlineServiceChargeFees("0.0");
				}
			}
			totalfee=totalfee+universityFee +lateFinefees ;
			newSupplementaryImpApplicationForm.setTotalFee(String.valueOf(totalfee));
			String totalfeeinWords = CommonUtil.numberToWord((int)totalfee);
			if(newSupplementaryImpApplicationForm.getStudentObj()!=null)
			{
				
				
				if(newSupplementaryImpApplicationForm.getStudentObj().getIsEgrand() && newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
					newSupplementaryImpApplicationForm.setTheoryFee(String.valueOf(30));
					newSupplementaryImpApplicationForm.setUniversityFee(String.valueOf(15));
					newSupplementaryImpApplicationForm.setOnlineServiceChargeFees(String.valueOf(45));
					newSupplementaryImpApplicationForm.setTotalFee(String.valueOf(45));
					newSupplementaryImpApplicationForm.seteGrandStudent(true);
				}else if(newSupplementaryImpApplicationForm.getStudentObj().getIsEgrand()  && newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==2){
					//course id for diff university fee 10,11,12,16,25
					newSupplementaryImpApplicationForm.seteGrandStudent(true);
					List<Integer> courseId1=new ArrayList<Integer>();
					courseId1.add(10);
					courseId1.add(11);
					courseId1.add(12);
					courseId1.add(16);
					courseId1.add(25);
					List<Integer> courseId2=new ArrayList<Integer>();
					courseId2.add(13);
					courseId2.add(14);
					courseId2.add(15);
					courseId2.add(32);
					
					if(courseId1.contains(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getId())){
						newSupplementaryImpApplicationForm.setTheoryFee(String.valueOf(30));
						newSupplementaryImpApplicationForm.setUniversityFee(String.valueOf(0.0));
						newSupplementaryImpApplicationForm.setTotalFee(String.valueOf(30));
					}else if(courseId2.contains(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getId())){
						newSupplementaryImpApplicationForm.setTheoryFee(String.valueOf(30));
						newSupplementaryImpApplicationForm.setUniversityFee(String.valueOf(0.0));
						newSupplementaryImpApplicationForm.setTotalFee(String.valueOf(30));
					}
					
					
				}
			}
			
			
			/*
			if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection()!=null)
			{
				String religionSection = newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection().getName();
				
				if(religionSection.equalsIgnoreCase("SC") || 
				   religionSection.equalsIgnoreCase("ST") || 
				   religionSection.equalsIgnoreCase("OEC") ||
				   religionSection.equalsIgnoreCase("FMN") ||
				   religionSection.equalsIgnoreCase("KPCR") ||
				   religionSection.equalsIgnoreCase("SEBC") ||
				   religionSection.equalsIgnoreCase("GENERAL") ||
				   religionSection.equalsIgnoreCase("OBC(Non- Creamy)"))
				{	
					newSupplementaryImpApplicationForm.setRegExamFeesExempted(true);
					newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
					newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
					// set journalNo for payment through challan
					newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
				}
				else if(religionSection.equalsIgnoreCase("OBC") || religionSection.equalsIgnoreCase("OTHERS"))
					if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId() != null)
					{
						if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId().getId() == 1){
						newSupplementaryImpApplicationForm.setRegExamFeesExempted(true);
						newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
						newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");	
						newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
						newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
						// set journalNo for payment through challan
						newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
						}else{
							newSupplementaryImpApplicationForm.setRegExamFeesExempted(false);
							// set journalNo for payment through challan
							if(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()!=null){
								newSupplementaryImpApplicationForm.setJournalNo("RG"+newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()+newSupplementaryImpApplicationForm.getExamId());
							}
							//if(regExamFeesBo!=null){	//	commented because client once used this data and now its not being used
								newSupplementaryImpApplicationForm.setApplicationAmount(totalfee+"");
								newSupplementaryImpApplicationForm.setApplicationAmount1(totalfee+"");	
								newSupplementaryImpApplicationForm.setApplicationAmountWords(totalfeeinWords);
								newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
							//}
						}
					}
					
				
			}*/
			if(subjectList != null && !subjectList.isEmpty()){
			newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
			}
		}//main for
		
		newSupplementaryImpApplicationForm.setExamType("Regular".toUpperCase());
		
		//raghu for attenance
		int classId = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId();
/*		List studateList=impl.getDateList(newSupplementaryImpApplicationForm.getStudentObj().getId(), classId);
		List clsdateList=impl.getDateList(0, classId);
		List sessionAttendanceList=impl.getSessionAttendanceList(newSupplementaryImpApplicationForm.getStudentObj().getId(), classId);
		float percentage=NewSupplementaryImpApplicationHelper.getInstance().getAttendancePercentage(studateList, sessionAttendanceList, clsdateList);
*/		
		// Vinodha  attendance filter for reg app start date
			//if(!transaction.isEligibleWithoutAttendanceCheck(studentId,examIds.get(0))){
			List<StudentWiseSubjectSummaryTO> summaryList = StudentAttendanceSummaryHandler.getInstance().getAttendancePercentageForRegularApp(newSupplementaryImpApplicationForm.getStudentObj());
			
			float totalPresent = 0;
			float totalConducted = 0;
			float totalCoCurricularLeave = 0;
			float totalAbsent = 0;
			float totalAggrPer = 0;
			DecimalFormat f = new DecimalFormat("##.00");
			for (StudentWiseSubjectSummaryTO objto : summaryList) {
				totalPresent = totalPresent + objto.getClassesPresent();
				totalConducted = totalConducted + objto.getConductedClasses();
				totalCoCurricularLeave = totalCoCurricularLeave + objto.getCocurricularLeave();
				totalAbsent = totalAbsent + objto.getClassesAbsent();
			}
			totalAggrPer = ((totalPresent + totalCoCurricularLeave) * 100) / totalConducted;
			
			f.format(totalAggrPer);
			String programType = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName(); 
			if(programType.equalsIgnoreCase("ug"))
			{
				//totalAggrPer = Math.round(((float)(totalHoursHeld-(totalAbsent-totalCoCurricularLeave))/totalHoursHeld)*(float)100);
			}
/*			else if(programType.equalsIgnoreCase("pg"))
			{
				float hoursHeld = Float.parseFloat(CMSConstants.ATT_HOURSHELD_PERDAY_PG);
				float days = Float.parseFloat(CMSConstants.ATT_WORKINGDAYS_PG);
				float totalHoursHeld = hoursHeld*days;
				totalAggrPer = ((totalHoursHeld-totalAbsent)/totalHoursHeld)*100;
			}
*/				
		// vinodha activity attendance ends
		
		boolean ava=checkAttendanceAvailability(newSupplementaryImpApplicationForm.getStudentObj().getId(),newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId());
		String reqPercentage=CMSConstants.ATTENDANCE_PERCENTAGE_FOR_APP;
		if(ava){
		int classes = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId();
		if(!CMSConstants.ATT_EXCLUDED_EXAMREG.contains(classes)){
		//if(programType.equalsIgnoreCase("ug")){
			if(totalAggrPer<=Float.parseFloat(reqPercentage)){
				boolean ava1=checkCondonationAvailability(newSupplementaryImpApplicationForm.getStudentObj().getId(),newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId());
				if(!ava1){
					newSupplementaryImpApplicationForm.setIsAttendanceShortage(true);
				}																									
			}
		//}
		}
		//}
			//getting instructions
			String programTypeId=	NewSecuredMarksEntryHandler.getInstance().getPropertyValue(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId(),"Classes",true,"course.program.programType.id");
			IDownloadHallTicketTransaction txn= new DownloadHallTicketTransactionImpl();
			List<ExamFooterAgreementBO> footer=txn.getFooterDetails(programTypeId,"E",classId+"");
			if(footer!=null && !footer.isEmpty()){
				ExamFooterAgreementBO obj=footer.get(0);
				if(obj.getDescription()!=null)
					newSupplementaryImpApplicationForm.setDescription(obj.getDescription());
			}else{
				newSupplementaryImpApplicationForm.setDescription(null);
			}
		
		
		
			}
	}

	public boolean checkAttendanceAvailability(int studentId, int classId) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean dup=false;
		dup=transaction.checkAttendanceAvailability(studentId,classId);
		return dup;	
	}
	public boolean checkCondonationAvailability(int studentId, int classId) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean ava=false;
		ava=transaction.checkCondonationAvailability(studentId,classId);
		return ava;
	}
	
	public boolean checkDuplication(NewSupplementaryImpApplicationForm form) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean dup=false;
		dup=transaction.checkDuplication(form);
		return dup;
	}
	
	public boolean addAppliedStudent(NewSupplementaryImpApplicationForm form) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean add=false;
		if(form.isRegExamFeesExempted()){
			ExamRegularApplication er=new ExamRegularApplication();
			
			Classes c=new Classes();
			c.setId(form.getStudentObj().getClassSchemewise().getClasses().getId());
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanVerified(false);
			er.setChallanNo("FeesExempted");
			er.setMode("FeesExempted");
			er.setIsTokenRegisterd(false);
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudent(er);
		}
		else if(form.getSelectedFeePayment()!=null && !form.getSelectedFeePayment().isEmpty() 
				&& form.getSelectedFeePayment().equalsIgnoreCase("OnlinePayment")){
			ExamRegularApplication er=new ExamRegularApplication();
			
			Classes c=new Classes();
			c.setId(form.getStudentObj().getClassSchemewise().getClasses().getId());
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanNo(form.getTxnRefNo());
			er.setChallanVerified(true);
			er.setMode("OnlinePayment");
			er.setIsTokenRegisterd(false);
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudent(er);
		}
		else if(form.getSelectedFeePayment()!=null && !form.getSelectedFeePayment().isEmpty() 
				&& form.getSelectedFeePayment().equalsIgnoreCase("SBI")){
			ExamRegularApplication er=new ExamRegularApplication();
			
			Classes c=new Classes();
			c.setId(form.getStudentObj().getClassSchemewise().getClasses().getId());
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanNo(form.getJournalNo());
			er.setChallanVerified(false);
			er.setMode("Challan");
			er.setIsTokenRegisterd(false);
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudent(er);
			
			ChallanUploadDataExam challanBo = new ChallanUploadDataExam();
			challanBo.setAmount(Double.parseDouble(form.getApplicationAmount()));
			challanBo.setChallanNo(form.getJournalNo());
			challanBo.setCreatedDate(new Date());
			challanBo.setCreatedBy(form.getUserId());
			challanBo.setChallanDate(CommonUtil.ConvertStringToSQLDate(form.getApplicationDate()));
			add=transaction.addAppliedStudentToChallan(challanBo);
		}else{

			ExamRegularApplication er=new ExamRegularApplication();
			
			Classes c=new Classes();
			if(!form.getPreviousExam())
			c.setId(form.getStudentObj().getClassSchemewise().getClasses().getId());
			else
				c.setId(Integer.parseInt(form.getPrevClassId()));
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			if(!form.getPreviousExam())
			e.setId(Integer.parseInt(form.getExamId()));
			else
				e.setId(Integer.parseInt(form.getPrevClassExamId()));

			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setIsTokenRegisterd(false);
			Date dateTime=er.getCreatedDate();
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudent(er);
		
		}
		return add;
	}

	public AdmapplnAdditionalInfo getTitleForCareTaker(int admApplnId) throws ApplicationException {		
		Session session = null;
		AdmapplnAdditionalInfo bo = null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			String query ="from AdmapplnAdditionalInfo a where a.admAppln.id="+admApplnId;
			Query titleQuery = session.createQuery(query);
			bo = (AdmapplnAdditionalInfo) titleQuery.uniqueResult();			
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return bo;
	}
	
	public String getParameterForPGI(NewSupplementaryImpApplicationForm form) throws Exception {
		
		RegularExamApplicationPGIDetails bo= new RegularExamApplicationPGIDetails();
		bo.setCandidateName(form.getNameOfStudent());
		if(form.getClassId()!=null && !form.getClassId().isEmpty()){
		Classes cls = new Classes();
		cls.setId(Integer.parseInt(form.getClassId()));
		bo.setClasses(cls);
		}
		bo.setTxnStatus("Pending");		
		if(form.getApplicationAmount()!=null && !form.getApplicationAmount1().isEmpty())
			bo.setTxnAmount(new BigDecimal(form.getApplicationAmount()));
		
		//below setting of txn amount has to be commented once the Production phase of PGI has started 
//		bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
		bo.setMobileNo1(form.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
		bo.setMobileNo2(form.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
		bo.setEmail(form.getStudentObj().getAdmAppln().getPersonalData().getEmail());
		bo.setRefundGenerated(false);
		bo.setCreatedBy(form.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(form.getUserId());
		Student student = new Student();
		student.setId(form.getStudentObj().getId());
		bo.setStudent(student);		
		ExamDefinitionBO examBo = new ExamDefinitionBO();
		examBo.setId(Integer.parseInt(form.getExamId()));
		bo.setExam(examBo);
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		String candidateRefNo=transaction.generateCandidateRefNo(bo, form);
		StringBuilder temp=new StringBuilder();		
		
		String productinfo="productinfo";
		form.setRefNo(candidateRefNo);
		form.setProductinfo(productinfo);
		
		//change the url of response in the msg below when it has to be released to the production. Please put the production IP
		if(candidateRefNo!=null && !candidateRefNo.isEmpty())
		 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
			temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID);
		//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
		
		String hash=hashCal("SHA-512",temp.toString());
		form.setTest(temp.toString());		
		return hash;
	}
	
	public String hashCal(String type,String str){
		byte[] hashseq=str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try{
			MessageDigest algorithm = MessageDigest.getInstance(type);
			algorithm.reset();
			algorithm.update(hashseq);
			byte messageDigest[] = algorithm.digest();
			for (int i=0;i<messageDigest.length;i++) {
				String hex=Integer.toHexString(0xFF & messageDigest[i]);
				if(hex.length()==1) hexString.append("0");
					hexString.append(hex);
			}			
		}catch(NoSuchAlgorithmException nsae){ }		
		return hexString.toString();
	}
	
	public boolean updateResponseForSuppl(NewSupplementaryImpApplicationForm form) throws Exception{
		boolean isUpdated=false;
		SupplementaryExamApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoForSuppl(form);
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		isUpdated=transaction.updateReceivedStatusForSuppl(bo,form);
		return isUpdated;
	}
	
	public boolean updateResponse(NewSupplementaryImpApplicationForm form) throws Exception{
		boolean isUpdated=false;
		RegularExamApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBo(form);
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		isUpdated=transaction.updateReceivedStatus(bo,form);
		return isUpdated;
	}
	
	public boolean checkDuplicationForSuppl(NewSupplementaryImpApplicationForm form) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean dup=false;
		dup=transaction.checkDuplicationForSuppl(form);
		return dup;
	}
	
	public boolean addAppliedStudentForSuppl(NewSupplementaryImpApplicationForm form) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean add=false;
		if(form.getIsFeesExempted()){
			ExamSupplementaryApplication er=new ExamSupplementaryApplication();
			
			Classes c=new Classes();
			c.setId(Integer.parseInt(form.getPrevClassId()));
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanVerified(true);
			er.setChallanNo("FeesExempted");
			er.setMode("FeesExempted");
			er.setAmount("FeesExempted");
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudentForSuppl(er);
		}
		else if(form.getSelectedFeePayment()!=null && !form.getSelectedFeePayment().isEmpty() 
				&& form.getSelectedFeePayment().equalsIgnoreCase("OnlinePayment")){
			ExamSupplementaryApplication er=new ExamSupplementaryApplication();
			
			Classes c=new Classes();
			c.setId(Integer.parseInt(form.getPrevClassId()));
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanNo(form.getTxnRefNo());
			er.setChallanVerified(true);
			er.setAmount(form.getApplicationAmount1());
			er.setMode("OnlinePayment");
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudentForSuppl(er);
		}
		else if(form.getSelectedFeePayment()!=null && !form.getSelectedFeePayment().isEmpty() 
				&& form.getSelectedFeePayment().equalsIgnoreCase("SBI")){
			ExamSupplementaryApplication er=new ExamSupplementaryApplication();
			
			Classes c=new Classes();
			c.setId(Integer.parseInt(form.getPrevClassId()));
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setChallanNo(form.getJournalNo());
			er.setChallanVerified(false);
			er.setMode("Challan");
			er.setAmount(form.getApplicationAmount());

			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudentForSuppl(er);
			
			ChallanUploadDataExam challanBo = new ChallanUploadDataExam();
			challanBo.setAmount(Double.parseDouble(form.getApplicationAmount()));
			challanBo.setChallanNo(form.getJournalNo());
			challanBo.setCreatedDate(new Date());
			challanBo.setCreatedBy(form.getUserId());
			challanBo.setChallanDate(CommonUtil.ConvertStringToSQLDate(form.getApplicationDate()));
			add=transaction.addAppliedStudentToChallan(challanBo);
		}else{

			ExamSupplementaryApplication er=new ExamSupplementaryApplication();
			
			Classes c=new Classes();
			c.setId(Integer.parseInt(form.getPrevClassId()));
			er.setClasses(c);
			
			ExamDefinitionBO e=new ExamDefinitionBO();
			e.setId(Integer.parseInt(form.getExamId()));
			er.setExam(e);
			
			Student s=new Student();
			s.setId(form.getStudentObj().getId());
			er.setStudent(s);
			
			er.setIsApplied(true);
			er.setCreatedBy(form.getUserId());
			er.setCreatedDate(new Date());
			er.setModifiedBy(form.getUserId());
			er.setLastModifiedDate(new Date());
			er.setAmount(form.getApplicationAmount());
			Date dateTime=er.getCreatedDate();			;
			form.setApplicationDate(DateFormatUtils.format(dateTime, "dd/MM/yyyy").toString());
			add=transaction.addAppliedStudentForSuppl(er);
		
		}
		return add;
	}
	
public String getParameterForPGIForSuppl(NewSupplementaryImpApplicationForm form) throws Exception {
		
		SupplementaryExamApplicationPGIDetails bo= new SupplementaryExamApplicationPGIDetails();
		bo.setCandidateName(form.getNameOfStudent());
		if(form.getClassId()!=null && !form.getClassId().isEmpty()){
		Classes cls = new Classes();
		cls.setId(Integer.parseInt(form.getClassId()));
		bo.setClasses(cls);
		}
		bo.setTxnStatus("Pending");		
		if(form.getApplicationAmount()!=null && !form.getApplicationAmount1().isEmpty())
			bo.setTxnAmount(new BigDecimal(form.getApplicationAmount()));
		
		//below setting of txn amount has to be commented once the Production phase of PGI has started 
//		bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
		bo.setMobileNo1(form.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
		bo.setMobileNo2(form.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
		bo.setEmail(form.getStudentObj().getAdmAppln().getPersonalData().getEmail());
		bo.setRefundGenerated(false);
		bo.setCreatedBy(form.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(form.getUserId());
		Student student = new Student();
		student.setId(form.getStudentObj().getId());
		bo.setStudent(student);		
		ExamDefinitionBO examBo = new ExamDefinitionBO();
		examBo.setId(Integer.parseInt(form.getExamId()));
		bo.setExam(examBo);
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		String candidateRefNo=transaction.generateCandidateRefNoForSuppl(bo, form);
		StringBuilder temp=new StringBuilder();		
		
		String productinfo="productinfo";
		form.setRefNo(candidateRefNo);
		form.setProductinfo(productinfo);
		
		//change the url of response in the msg below when it has to be released to the production. Please put the production IP
		if(candidateRefNo!=null && !candidateRefNo.isEmpty())
		 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
			temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID);
		//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
		
		String hash=hashCal("SHA-512",temp.toString());
		form.setTest(temp.toString());		
		return hash;
	}

    public ExamSupplementaryApplication applicationForSuppl(NewSupplementaryImpApplicationForm form) throws Exception {
        INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
        ExamSupplementaryApplication appl =transaction.getApplicationForSuppl(form);
	    return appl;
    }
    
    public boolean getPrevClasssIdFromRegularApp(int StuId,int PreClassId) throws Exception {
		INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		boolean applied=false;
		applied=transaction.getPrevClasssIdFromRegularApp(StuId,PreClassId);
		return applied;
	}
    
public List<ExamSupplementaryImpApplicationSubjectTO> getApplicationFormsForRegulartemp(List<Integer> examIds, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationSubjectTO> subjectList = new LinkedList<ExamSupplementaryImpApplicationSubjectTO>();
		int count=0;
		
		for (Integer examId: examIds) {
			
			String query="";
			query = "from ExamDefinition e where e.id ="+examId;
			List<ExamDefinition> examList=transaction.getDataForQuery(query);
			for (ExamDefinition bo : examList) {
				newSupplementaryImpApplicationForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
				newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
				newSupplementaryImpApplicationForm.setExamName(bo.getName().toUpperCase());
				newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth().toUpperCase())));

				newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
			}
			
			query = "from CourseToDepartment c where c.course.id ="+newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
			List<CourseToDepartment> courseToDepartmentList=transaction.getDataForQuery(query);
			String dep="";
			
			for (CourseToDepartment bo : courseToDepartmentList) {
				if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()==1){
					dep=dep+bo.getDepartment().getName();
				}else if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()>1){
					if(count==0){
						dep=dep+bo.getDepartment().getName();
					}else{
						dep=dep+","+bo.getDepartment().getName();
					}
					
					count++;
				}
				
				newSupplementaryImpApplicationForm.setCourseDep(dep);
				
			}
			
			List boList = null; 
			//if(newSupplementaryImpApplicationForm.getPreviousExam()){
				boList=transaction1.getSubjectsListForStudentPrevClass(newSupplementaryImpApplicationForm.getStudentObj(), Integer.parseInt(newSupplementaryImpApplicationForm.getAcademicYear()));
			    if(boList.size()>0){
			    	newSupplementaryImpApplicationForm.setRegularAppAvailable(true);
			    }
			//}
			else
				boList=transaction1.getSubjectsListForStudent(newSupplementaryImpApplicationForm.getStudentObj(), Integer.parseInt(newSupplementaryImpApplicationForm.getAcademicYear()));
			
			Iterator i=boList.iterator();
			int theoryCount=0;
			int practicalCount=0;
			while(i.hasNext()) {
				ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
				Object obj[]=(Object[])i.next();
				SubjectUtilBO bo=(SubjectUtilBO)obj[0];
				//if(bo.getId()==811){
				sto.setSubjectCode(bo.getCode());
				sto.setSubjectName(bo.getName().toUpperCase());
				sto.setSubjectType(bo.getIsTheoryPractical());
				if(bo.getIsTheoryPractical().equalsIgnoreCase("t"))
					theoryCount++;
				if(bo.getIsTheoryPractical().equalsIgnoreCase("p"))
					practicalCount++;
				if(obj[1]!=null){
					sto.setSectionName(obj[1].toString().toUpperCase());
				}else{
					sto.setSectionName("");
				}
				
				
				subjectList.add(sto);
				//}
						
			}
			
			//Set Regular Exam Application Fees
			RegularExamFees regExamFeesBo = transaction1.getRegularExamFees(newSupplementaryImpApplicationForm);
			double totalfee = 0;
			if(regExamFeesBo!=null)
				totalfee = Double.parseDouble(regExamFeesBo.getTheoryFees().toString());
			String totalfeeinWords = CommonUtil.numberToWord((int)totalfee);
			if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection()!=null)
			{
				String religionSection = newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection().getName();
				
				if(religionSection.equalsIgnoreCase("SC") || 
				   religionSection.equalsIgnoreCase("ST") || 
				   religionSection.equalsIgnoreCase("OEC") ||
				   religionSection.equalsIgnoreCase("FMN") ||
				   religionSection.equalsIgnoreCase("KPCR") ||
				   religionSection.equalsIgnoreCase("SEBC") ||
				   religionSection.equalsIgnoreCase("OBC (Non-Creamy)"))
				{	
					newSupplementaryImpApplicationForm.setRegExamFeesExempted(true);
					newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");
					newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
					newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
					// set journalNo for payment through challan
					newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
				}
				else if(religionSection.equalsIgnoreCase("OBC") || religionSection.equalsIgnoreCase("OTHERS"))
					/*if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId().getId()==1)*/
					{
						newSupplementaryImpApplicationForm.setRegExamFeesExempted(true);
						newSupplementaryImpApplicationForm.setApplicationAmount("Fees Exempted");
						newSupplementaryImpApplicationForm.setApplicationAmount1("Fees Exempted");	
						newSupplementaryImpApplicationForm.setApplicationAmountWords("Fees Exempted");
						newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
						// set journalNo for payment through challan
						newSupplementaryImpApplicationForm.setJournalNo("Fees Exempted");
					}
					else{
						newSupplementaryImpApplicationForm.setRegExamFeesExempted(false);
						// set journalNo for payment through challan
						if(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()!=null){
							newSupplementaryImpApplicationForm.setJournalNo("RG"+newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo()+newSupplementaryImpApplicationForm.getExamId());
						}
						if(regExamFeesBo!=null){
							newSupplementaryImpApplicationForm.setApplicationAmount(totalfee+"");
							newSupplementaryImpApplicationForm.setApplicationAmount1(totalfee+"");	
							newSupplementaryImpApplicationForm.setApplicationAmountWords(totalfeeinWords);
							newSupplementaryImpApplicationForm.setSupSubjectList(subjectList);
						}
					}
			}
						
		}//main for
		
		newSupplementaryImpApplicationForm.setExamType("Regular".toUpperCase());
		
		//raghu for attenance
		int classId =0;
		if(!newSupplementaryImpApplicationForm.getPreviousExam())
		   classId = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId();
		else
			classId = Integer.parseInt(newSupplementaryImpApplicationForm.getPrevClassId());
/*		List studateList=impl.getDateList(newSupplementaryImpApplicationForm.getStudentObj().getId(), classId);
		List clsdateList=impl.getDateList(0, classId);
		List sessionAttendanceList=impl.getSessionAttendanceList(newSupplementaryImpApplicationForm.getStudentObj().getId(), classId);
		float percentage=NewSupplementaryImpApplicationHelper.getInstance().getAttendancePercentage(studateList, sessionAttendanceList, clsdateList);
*/		
		// Vinodha  attendance filter for reg app start date
			List<StudentWiseSubjectSummaryTO> summaryList = StudentAttendanceSummaryHandler.getInstance().getAttendancePercentageForRegularApp(newSupplementaryImpApplicationForm.getStudentObj());
			
			float totalPresent = 0;
			float totalConducted = 0;
			float totalCoCurricularLeave = 0;
			float totalAbsent = 0;
			float totalAggrPer = 0;
			newSupplementaryImpApplicationForm.setIsAttendanceShortage(false);
			DecimalFormat f = new DecimalFormat("##.00");
			for (StudentWiseSubjectSummaryTO objto : summaryList) {
				totalPresent = totalPresent + objto.getClassesPresent();
				totalConducted = totalConducted + objto.getConductedClasses();
				totalCoCurricularLeave = totalCoCurricularLeave + objto.getCocurricularLeave();
				totalAbsent = totalAbsent + objto.getClassesAbsent();
			}
			totalAggrPer = ((totalPresent + totalCoCurricularLeave) * 100) / totalConducted;
			f.format(totalAggrPer);
			String programType = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName(); 
			if(programType.equalsIgnoreCase("ug"))
			{
				//totalAggrPer = Math.round(((float)(totalHoursHeld-(totalAbsent-totalCoCurricularLeave))/totalHoursHeld)*(float)100);
			}
/*			else if(programType.equalsIgnoreCase("pg"))
			{
				float hoursHeld = Float.parseFloat(CMSConstants.ATT_HOURSHELD_PERDAY_PG);
				float days = Float.parseFloat(CMSConstants.ATT_WORKINGDAYS_PG);
				float totalHoursHeld = hoursHeld*days;
				totalAggrPer = ((totalHoursHeld-totalAbsent)/totalHoursHeld)*100;
			}
*/				
		// vinodha activity attendance ends
		
		boolean ava=checkAttendanceAvailability(newSupplementaryImpApplicationForm.getStudentObj().getId(),newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId());
		String reqPercentage=CMSConstants.ATTENDANCE_PERCENTAGE_FOR_APP;
		if(ava){
		String classes = newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getName();
		if(!CMSConstants.ATT_EXCLUDED_EXAMREG.contains(classes)){
		//if(programType.equalsIgnoreCase("ug")){
			if(totalAggrPer<Float.parseFloat(reqPercentage)){
				boolean ava1=checkCondonationAvailability(newSupplementaryImpApplicationForm.getStudentObj().getId(),newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId());
				if(!ava1){
					newSupplementaryImpApplicationForm.setIsAttendanceShortage(true);
				}																									
			}
		//}
		}
			//getting instructions
			String programTypeId=	NewSecuredMarksEntryHandler.getInstance().getPropertyValue(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId(),"Classes",true,"course.program.programType.id");
			IDownloadHallTicketTransaction txn= new DownloadHallTicketTransactionImpl();
			List<ExamFooterAgreementBO> footer=txn.getFooterDetails(programTypeId,"E",classId+"");
			if(footer!=null && !footer.isEmpty()){
				ExamFooterAgreementBO obj=footer.get(0);
				if(obj.getDescription()!=null)
					newSupplementaryImpApplicationForm.setDescription(obj.getDescription());
			}else{
				newSupplementaryImpApplicationForm.setDescription(null);
			}
		
		
		
			}
		return subjectList;
	}

// for revaluation
public boolean extendedDateForRevaluation(int classId,NewSupplementaryImpApplicationForm suppForm) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	return transaction.getExtendedDateForRevaluation(classId, suppForm);
}
public List<Integer> checkRevaluationAppAvailable(int classId, boolean isExtended,boolean isSupplementary) throws Exception {
	String query="select eb.examId from ExamPublishHallTicketMarksCardBO eb where eb.isActive=1 and eb.publishFor='Revaluation/Scrutiny' and eb.examDefinitionBO.examTypeUtilBO.name='Regular' and eb.classes.id=" +classId+
	" and ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between eb.downloadStartDate and eb.downloadEndDate)  "; 
INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
return transaction.getDataForQuery(query);
}

// for revaluation added by Ashwini

public void getApplicationFormsForRevaluation(List<Integer> examIds, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {

	INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
	List<SupplementaryAppExamTo> examList=new ArrayList<SupplementaryAppExamTo>();
	SupplementaryAppExamTo eto=null;
	int count=0;
	boolean buttonDisplay=true;
	List<Integer> classNotRequiredList=new ArrayList<Integer>();
	INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	newSupplementaryImpApplicationForm.setChallanButton(false);

	for (Integer examId: examIds) {
		eto=new SupplementaryAppExamTo();
		eto.setExamId(examId);
		eto.setExamName((NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinition",true,"name")).toUpperCase());
		newSupplementaryImpApplicationForm.setExamid(examId);
		//rgahu write newly like regular app
		String query1 = "from ExamDefinition e where e.id ="+examId;
		List<ExamDefinition> examList1=transaction.getDataForQuery(query1);
		for (ExamDefinition bo : examList1) {
			newSupplementaryImpApplicationForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
			newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
			newSupplementaryImpApplicationForm.setExamName(bo.getName().toUpperCase());
			newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth())));
			newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
		}

		query1="from ExamPublishHallTicketMarksCardBO p"+
		" where p.classes.id="+newSupplementaryImpApplicationForm.getRevclassid()+
		" and p.examId=" +Integer.parseInt(newSupplementaryImpApplicationForm.getExamId())+" and p.publishFor='Revaluation/Scrutiny'";
		List<ExamPublishHallTicketMarksCardBO> fineDetails=transaction.getDataForQuery(query1);
		ExamPublishHallTicketMarksCardBO regFineFees=null;
		if(fineDetails!=null && !fineDetails.isEmpty()){
			regFineFees=fineDetails.get(0);
		}

		List boList1 = new ArrayList();
		if(!newSupplementaryImpApplicationForm.isSupplementary())
			boList1=transaction1.getSubjectsListForRevaluation(newSupplementaryImpApplicationForm.getStudentObj(),examId,newSupplementaryImpApplicationForm.getRevclassid());
		else
			boList1 =transaction1.getSupplSubjectsListForRevaluation(newSupplementaryImpApplicationForm.getStudentObj(),examId,newSupplementaryImpApplicationForm.getRevclassid());
		
		List<SupplementaryApplicationClassTo> classList=new ArrayList<SupplementaryApplicationClassTo>();
		SupplementaryApplicationClassTo to=null;
        if(boList1.size()==0)
        	newSupplementaryImpApplicationForm.setRevAppAvailable(false);
		Iterator i=boList1.iterator();
		while(i.hasNext()) {

			Object obj[]=(Object[])i.next();
			to = new SupplementaryApplicationClassTo();
			to.setClassId(newSupplementaryImpApplicationForm.getRevclassid());

			List<ExamSupplementaryImpApplicationSubjectTO> subjectList = null;
			if (classList.contains(to)) {
				to = classList.remove(classList.indexOf(to));
				subjectList = to.getToList();
				//classNotRequiredList.add(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses() .getId());
			}else{
				subjectList = new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
				//to.setClassName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getName());
				to.setCourseName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getName());
				if(obj[26]!=null)
				newSupplementaryImpApplicationForm.setPrevSemNo(Integer.parseInt(obj[26].toString()));
				to.setRegisterNo(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo());
				to.setRollNo(newSupplementaryImpApplicationForm.getStudentObj().getRollNo());
				//to.setSemNo(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getTermNumber().toString());
				to.setStudentName(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln() .getPersonalData().getFirstName());
				if (count == 0) {
					newSupplementaryImpApplicationForm.setCourseName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise() .getClasses().getCourse().getName());
					newSupplementaryImpApplicationForm.setRegisterNo(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo());
					newSupplementaryImpApplicationForm .setNameOfStudent(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData() .getFirstName());
					count++;
				}
				newSupplementaryImpApplicationForm.setProgramId(String .valueOf(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln() .getCourseBySelectedCourseId() .getProgram().getId()));

			}

			if (!classNotRequiredList.contains(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId())) {
				ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
				//sto.setId(bo.getId());
				if(obj[10]!=null)
					sto.setSubjectId(Integer.parseInt(obj[10].toString()));
				if(obj[11]!=null)
					sto.setSubjectCode(obj[11].toString());
				if(obj[12]!=null)
					sto.setSubjectName(obj[12].toString());
				if(obj[13]!=null){
					sto.setSubjectType(obj[13].toString());
					if(obj[13].toString().equalsIgnoreCase("t") || obj[13].toString().equalsIgnoreCase("B"))
						sto.setTheory(true);
					if(obj[13].toString().equalsIgnoreCase("p") || obj[13].toString().equalsIgnoreCase("B"))
						sto.setPractical(true);
				}
				if(obj[27] != null ){
					sto.setMaxMarks(obj[27].toString());
				}
				//if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
				if(obj[14]!=null)
					sto.setSectionName(obj[14].toString());
				sto.setCommonChecked(false);
				sto.setTempChecked(false);
				sto.setControlDisable(true);
				sto.setClassId(newSupplementaryImpApplicationForm.getRevclassid());

				if(obj[18]!=null || obj[19]!=null){
					boolean isAbsent=false;
					float theroy=0;
					float practial=0;
					if(obj[18]!=null && CommonUtil.isValidDecimal(obj[18].toString()))
						theroy=Float.parseFloat(obj[18].toString());
					else{
						isAbsent=true;
						sto.setMarks("AB");
						sto.setDisableCheckBox(true);
					}
					if(obj[19]!=null && CommonUtil.isValidDecimal(obj[19].toString()))	
						practial=Float.parseFloat(obj[19].toString());
					else{
						isAbsent=true;
						sto.setMarks("AB");
						sto.setDisableCheckBox(true);
					}
                    if(!isAbsent){
                    	if(obj[13].toString().equalsIgnoreCase("B")){
                    		sto.setMarks(theroy+"");
                    	}else{
                    		sto.setMarks(theroy+practial+"");
                    	}
                    }
					}
				if(obj[13].toString().equalsIgnoreCase("t")|| obj[13].toString().equalsIgnoreCase("B")){
				subjectList.add(sto);
				}
				to.setToList(subjectList);
				classList.add(to);
			}		

		}//close for loop subject list while

//		if(newSupplementaryImpApplicationForm.getCourseId()!=null){
//
//			String query="from ExamRevaluationFee r where r.academicYear = "+newSupplementaryImpApplicationForm.getAcademicYear()+" and r.course.id="+newSupplementaryImpApplicationForm.getCourseId();
//			ExamRevaluationFee bo=(ExamRevaluationFee)PropertyUtil.getDataForUniqueObject(query);
//			if(bo==null){
//				newSupplementaryImpApplicationForm.setRevAppAvailable(false);
//			}else{
//
//				if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("revaluation")){
//
//					if(bo.getRevaluationFees()!=null){
//						newSupplementaryImpApplicationForm.setRevaluationFees(bo.getRevaluationFees().doubleValue());
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//					
//					if(bo.getApplicationFees()!=null){
//						newSupplementaryImpApplicationForm.setApplicationFees(String.valueOf(bo.getApplicationFees().doubleValue()));
//
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//				}	
//
//				else if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("scrutiny")){
//
//					if(bo.getScrutinyFees()!=null){
//						newSupplementaryImpApplicationForm.setScrutinyFees(bo.getScrutinyFees().doubleValue());
//
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//					
//					if(bo.getApplicationFees()!=null){
//						newSupplementaryImpApplicationForm.setApplicationFees(String.valueOf(bo.getApplicationFees().doubleValue()));
//
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//				}
//				
//				else{
//					if(bo.getChallengeValuationFees()!=null){
//						newSupplementaryImpApplicationForm.setChallengeRevaluationFees(bo.getChallengeValuationFees().doubleValue());
//
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//					
//					if(bo.getApplicationFees()!=null){
//						newSupplementaryImpApplicationForm.setApplicationFees(String.valueOf(bo.getApplicationFees().doubleValue()));
//
//
//					}else{
//						newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
//					}
//				}
//
//				double totalTheoryFees = 0;
//				double applicationFees= newSupplementaryImpApplicationForm.getApplicationFees()==null?0:Double.parseDouble(newSupplementaryImpApplicationForm.getApplicationFees());
//				if(newSupplementaryImpApplicationForm.isExtended()){
//					if(newSupplementaryImpApplicationForm.getIsFine()){
//						newSupplementaryImpApplicationForm.setFineFees(Double.parseDouble(regFineFees.getFineAmount()));
//					}
//					else if(newSupplementaryImpApplicationForm.getIsSuperFine()){
//						newSupplementaryImpApplicationForm.setFineFees(Double.parseDouble(regFineFees.getSuperFineAmount()));
//					}						
//				}
//
//				if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("revaluation")){
//					totalTheoryFees = newSupplementaryImpApplicationForm.getFineFees() +
//					newSupplementaryImpApplicationForm.getRevaluationFees() +applicationFees;
//				}
//				else if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("scrutiny")){
//					totalTheoryFees = newSupplementaryImpApplicationForm.getFineFees() +
//					newSupplementaryImpApplicationForm.getScrutinyFees() +
//					newSupplementaryImpApplicationForm.getApplicationFees()==null?0:Integer.parseInt(newSupplementaryImpApplicationForm.getApplicationFees());
//
//					
//				}
//				else{
//					totalTheoryFees = newSupplementaryImpApplicationForm.getFineFees() +
//					newSupplementaryImpApplicationForm.getChallengeRevaluationFees() +applicationFees;
//
//				}
//				newSupplementaryImpApplicationForm.setApplicationAmount(String.valueOf(totalTheoryFees));
//				newSupplementaryImpApplicationForm.setTotalFees(totalTheoryFees-applicationFees);
//				
//
//			}
//		
//		}

		eto.setExamList(classList);
		examList.add(eto);
	}//close main for loop


	//ExamRevaluationApplicationNew applicationNew = transaction1.getRevaulationDetails(newSupplementaryImpApplicationForm);
	newSupplementaryImpApplicationForm.setDisplayButton(buttonDisplay);
	newSupplementaryImpApplicationForm.setMainList(examList);
}




/**
 * @param examIds
 * @param newSupplementaryImpApplicationForm
 * @throws Exception
 */
public void getApplicationFormsForRevaluationChooseType(List<Integer> examIds, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {

	INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
	List<SupplementaryAppExamTo> examList=new ArrayList<SupplementaryAppExamTo>();
	SupplementaryAppExamTo eto=null;
	int count=0;
	boolean displayButton=false;
	boolean buttonDisplay=false;
	Double revaluationFee=0.0;
	int revaluationcount=0;
	List<Integer> classNotRequiredList=new ArrayList<Integer>();
	newSupplementaryImpApplicationForm.setChallanButton(false);

	for (Integer examId: examIds) {
		eto=new SupplementaryAppExamTo();
		eto.setExamId(examId);
		eto.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinition",true,"name"));
		newSupplementaryImpApplicationForm.setExamid(examId);
		//rgahu write newly like regular app
		String query1 = "from ExamDefinition e where e.id ="+examId;
		List<ExamDefinition> examList1=transaction.getDataForQuery(query1);
		for (ExamDefinition bo : examList1) {
			newSupplementaryImpApplicationForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
			newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
			newSupplementaryImpApplicationForm.setExamName(bo.getName());
			newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth())));
			newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
		}

		int courseId = Integer.parseInt(newSupplementaryImpApplicationForm.getCourseId());
		if(courseId != 0){
		query1 = "from ExamRevaluationFee e  where e.course.id = " + courseId ;
		List<ExamRevaluationFee> revaluationExamFeesDetails = transaction.getDataForQuery(query1);
		for(int i=0 ; i<revaluationExamFeesDetails.size() ; i++){
			ExamRevaluationFee bo = revaluationExamFeesDetails.get(i);
			if(bo.getRevaluationFees() != null){
				revaluationFee = Double.parseDouble(String.valueOf(bo.getRevaluationFees()));
			}
			//basicFee = Double.parseDouble(String.valueOf(bo.getApplicationFees()));
		}
		}
		// checking records exist or what
		
		String query="from ExamRevaluationApplicationNew er where er.classes.id="+newSupplementaryImpApplicationForm.getRevclassid()+
		" and er.exam.id="+examId+" and er.student.id= "+newSupplementaryImpApplicationForm.getStudentId();

		if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")){
			query=query+"  and er.isRevaluation=1" ;
		}
		if(newSupplementaryImpApplicationForm.getIsScrutiny()!=null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")){
			query=query+"  and er.isScrutiny=1 " ;
		}


		List<ExamRevaluationApplicationNew> boList= transaction.getDataForQuery(query);
		Iterator<ExamRevaluationApplicationNew> iterator = boList.iterator();
		int countApp = 0;
		while(iterator.hasNext()){
			ExamRevaluationApplicationNew new1 = iterator.next();
			if(new1.getRevaluationPgiDetails().getId() != 0){
				countApp++;
			}
		}
		if(countApp++ >0){
			newSupplementaryImpApplicationForm.setPaymentDone(true);
		}else {
			newSupplementaryImpApplicationForm.setPaymentDone(false);
		}
		List<SupplementaryApplicationClassTo> classList=new ArrayList<SupplementaryApplicationClassTo>();
		SupplementaryApplicationClassTo to=null;

		
		
		
		for (ExamRevaluationApplicationNew bo : boList) {
			if (!classNotRequiredList.contains(bo.getClasses().getId())) {
				to = new SupplementaryApplicationClassTo();
				to.setClassId(bo.getClasses().getId());
				to.setClassName(bo.getClasses().getName());
				List<ExamSupplementaryImpApplicationSubjectTO> subjectList = null;
				if (classList.contains(to)) {
					to = classList.remove(classList.indexOf(to));
					subjectList = to.getToList();
					
						if (to.isRevaluation() != bo.getIsRevaluation()){
							//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
							//classNotRequiredList.add(bo.getClasses() .getId());
						}else if (to.isScrutiny() != bo.getIsScrutiny()){
							//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
							//classNotRequiredList.add(bo.getClasses() .getId());
						}
					
				} else {
					subjectList = new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
					to.setCourseName(bo.getClasses().getCourse().getName());
					to.setRegisterNo(bo.getStudent().getRegisterNo());
					to.setRollNo(bo.getStudent().getRollNo());
					to.setSemNo(bo.getClasses().getTermNumber().toString());
					to.setStudentName(bo.getStudent().getAdmAppln() .getPersonalData().getFirstName());
					to.setClassName(bo.getClasses().getName());
					
					if (bo.getIsRevaluation())
						to.setRevaluation(true);
					if (bo.getIsScrutiny())
						to.setScrutiny(true);
					
					
					if (count == 0) {
						newSupplementaryImpApplicationForm.setCourseName(bo .getClasses().getCourse().getName());
						newSupplementaryImpApplicationForm.setRegisterNo(bo .getStudent().getRegisterNo());
						newSupplementaryImpApplicationForm .setNameOfStudent(bo.getStudent() .getAdmAppln().getPersonalData() .getFirstName());
						count++;
					}
					newSupplementaryImpApplicationForm.setProgramId(String .valueOf(bo.getStudent().getAdmAppln() .getCourseBySelectedCourseId() .getProgram().getId()));
				}
				if (!classNotRequiredList.contains(bo.getClasses().getId())) {
					ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
					sto.setId(bo.getId());
					sto.setSubjectId(bo.getSubject().getId());
					sto.setSubjectCode(bo.getSubject().getCode());
					sto.setSubjectName(bo.getSubject().getName());
					
					//raghu new 
					sto.setSubjectType(bo.getSubject().getIsTheoryPractical());
					
					if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
						
					String subQuery =   "select esb.examSubjectSectionMasterBO.name from ExamSubDefinitionCourseWiseBO esb where   esb.subjectUtilBO.id = "+bo.getSubject().getId();
					List sList= transaction.getDataForQuery(subQuery);
					for (Object s : sList) {
						sto.setSectionName(s.toString());
					}
					
					}
					
					if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("t") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
						sto.setTheory(true);
					if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("p") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
						sto.setPractical(true);
					
					if (bo.getIsRevaluation()){
						sto.setRevaluation(true);
					}else if (bo.getIsScrutiny()){
						sto.setScrutiny(true);
					}
					
						
					if (bo.getIsApplied()){
						sto.setIsApplied(true);
						sto.setCommonChecked(true);
						sto.setTempChecked(true);
					}
					
					boolean controlDisable = false;
					
					if ((bo.getIsRevaluation() == true) || (bo.getIsScrutiny() == true )) {
						controlDisable = true;
					}
					
					sto.setControlDisable(controlDisable);
					sto.setClassId(bo.getClasses().getId());
					sto.setSchemeNo(bo.getClasses().getTermNumber());
					sto.setMarks(bo.getMarks());
					sto.setMaxMarks(bo.getMaxMarks());
					
					subjectList.add(sto);
					
					to.setToList(subjectList);
					
					if(to.isRevaluation()){
						if (bo.getIsApplied() == bo.getIsRevaluation()){
							buttonDisplay = true;
							revaluationcount++;
						}
					}
					if(to.isScrutiny()){
						if (bo.getIsApplied() == bo.getIsScrutiny()){
							buttonDisplay = true;
							
						}
					}
					
					
					classList.add(to);
				}
			}
		}//close for loop subject list
		
		
		
		eto.setExamList(classList);
		examList.add(eto);
	}//close main for loop
	newSupplementaryImpApplicationForm.setRevaluationFees(Math.round(revaluationcount*revaluationFee));
	newSupplementaryImpApplicationForm.setTotalFeesInWords(CommonUtil.numberToWord1(Integer.parseInt(String.valueOf((Math.round(revaluationcount*revaluationFee))))));
	newSupplementaryImpApplicationForm.setApplicationAmount(Math.round(Double.parseDouble(String.valueOf(Math.round(Double.parseDouble(String.valueOf(revaluationcount*revaluationFee))))))+"."+0+0);
	newSupplementaryImpApplicationForm.setDisplayButton(buttonDisplay);
	newSupplementaryImpApplicationForm.setMainList(examList);
}

public ExamRevaluationApp checkDuplicationForRevaluation(NewSupplementaryImpApplicationForm form) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	ExamRevaluationApp bo =transaction.checkDuplicationForRevaluation(form);
	return bo;
}

public boolean saveRevaluationApplicationForStudentLogin( NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {

	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	Student student = new Student();
	student.setId(newSupplementaryImpApplicationForm.getStudentId());
	RevaluationApplicationPGIDetails details = transaction.getPgiDetails(newSupplementaryImpApplicationForm);
	List<ExamRevaluationApplicationNew> boList=new ArrayList<ExamRevaluationApplicationNew>();
	List<SupplementaryAppExamTo> examList=newSupplementaryImpApplicationForm.getMainList();
	for (SupplementaryAppExamTo suppTo : examList) {
		ExamDefinitionBO examDefinition = new ExamDefinitionBO();
		examDefinition.setId(suppTo.getExamId());
		List<SupplementaryApplicationClassTo> classTos=suppTo.getExamList();
		for (SupplementaryApplicationClassTo cto: classTos) {
			Classes classes = new Classes();
			classes.setId(cto.getClassId());
			List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto.getToList();
			for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
				
				
				ExamRevaluationApplicationNew objBO = new ExamRevaluationApplicationNew();
				if(to.getId()!=null)
					objBO.setId(to.getId());
					objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setCreatedDate(new Date());
					objBO.setLastModifiedDate(new Date());
					objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setStudent(student);
					objBO.setExam(examDefinition);
					Subject subject = new Subject();
					subject.setId(to.getSubjectId());
					objBO.setSubject(subject);
					objBO.setClasses(classes);
					if(details != null){
						RevaluationApplicationPGIDetails applicationPGIDetails = new RevaluationApplicationPGIDetails();
						applicationPGIDetails.setId(details.getId());
						objBO.setRevaluationPgiDetails(applicationPGIDetails);
					}else {
						objBO.setRevaluationPgiDetails(null);
					}
					if(to.getMarks()!=null && !CommonUtil.isAlphaSpaceDot(to.getMarks())){
					objBO.setMarks(String.valueOf(Math.round(Double.parseDouble(to.getMarks()))));
					}else{
						objBO.setMarks(to.getMarks());
					}
					objBO.setMaxMarks(to.getMaxMarks());
					
					//objBO.setChallanVerified(false);
				
				//set new one
				if(!to.isTempChecked()){
				if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")){
					objBO.setIsRevaluation(true);
					objBO.setIsScrutiny(false);
					objBO.setIsAnswerScrptCopy(false);
					objBO.setIsApplied(to.getIsApplied());
					}
					
				else if(newSupplementaryImpApplicationForm.getIsScrutiny()!=null &&  newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")){
					objBO.setIsRevaluation(false);
					objBO.setIsScrutiny(true);
					objBO.setIsAnswerScrptCopy(false);
					objBO.setIsApplied(to.getIsApplied());
				}
				
				
				}//close checked or not
				//set old one
				else{
					objBO.setIsRevaluation(to.getRevaluation());
					objBO.setIsScrutiny(to.getScrutiny());
					objBO.setIsAnswerScrptCopy(false);
					objBO.setIsApplied(to.getIsApplied());
					
					
				}
				
				boList.add(objBO);
				
			
			}//close sub loop	
			
		}//close class loop
		
	}//close exam loop
	
	return transaction.saveChallengeValuationApps(boList);
	

}

public boolean saveRevaluationAppWithChallanNos( NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {

	Student student = new Student();
	student.setId(newSupplementaryImpApplicationForm.getStudentId());
	Classes classes = new Classes();
	classes.setId(newSupplementaryImpApplicationForm.getRevclassid());

	ExamRevaluationApp objBO = new ExamRevaluationApp();
	ExamDefinitionBO def = new ExamDefinitionBO();
	def.setId(Integer.parseInt(newSupplementaryImpApplicationForm.getRevExamId()));
	objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
	objBO.setCreatedDate(new Date());
	objBO.setLastModifiedDate(new Date());
	objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
	objBO.setStudent(student);
	objBO.setExam(def);
	objBO.setChallanNo(newSupplementaryImpApplicationForm.getChallanNo());
	objBO.setAmount(Double.parseDouble(newSupplementaryImpApplicationForm.getApplicationAmount()));
	objBO.setIsChallanVerified(false);
	objBO.setClasses(classes);
	
	if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("revaluation")){
		objBO.setIsRevaluation(true);
		objBO.setIsScrutiny(false);

	}

	else if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null &&  newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("scrutiny")){
		objBO.setIsRevaluation(false);
		objBO.setIsScrutiny(true);

	}
	else{ 
		objBO.setIsRevaluation(false);
		objBO.setIsScrutiny(false);
	}
	
	//return transaction.saveRevaluationAppWithChallanNos(objBO);
	return false;

}

public List<Integer> checkRevaluationAvailableForSupplementry(int studentId) throws Exception {
	String query="select p.examDefinitionBO.id from ExamPublishHallTicketMarksCardBO p " +
	" where p.isActive=1 and p.examDefinitionBO.delIsActive=1 and p.examDefinitionBO.id in ( select s.examDefinition.id from StudentSupplementaryImprovementApplication s" +
	" where s.student.id= " +studentId+
	//raghu added from mounts
	//" and (((s.isFailedPractical=1 or s.isFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) and (s.isSupplementary=1 or s.isImprovement=1))and s.classes.id in(p.classes.id)) " +
	" and ((((s.isFailedPractical=1 or s.isFailedTheory=1 or s.isCIAFailedPractical=1 or s.isCIAFailedTheory=1) or (s.isTheoryOverallFailed=1 or s.isPracticalOverallFailed=1)) or (s.isSupplementary=1 or s.isImprovement=1))) and s.classes.id in(p.classes.id)) " +
	
	
	/*"and '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between (p.startDate and p.endDate)  group by p.exam.id ";*/
" and (('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between p.downloadStartDate and p.downloadEndDate)) and p.publishFor='Revaluation/Scrutiny' group by p.examDefinitionBO.id "; 
INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
return transaction.getDataForQuery(query);
}

public void getApplicationFormsForSupplementaryRevaluation(int examId, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
	
	INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
	List<SupplementaryAppExamTo> examList=new ArrayList<SupplementaryAppExamTo>();
	SupplementaryAppExamTo eto=null;
	int count=0;
	boolean buttonDisplay=true;
	List<Integer> classNotRequiredList=new ArrayList<Integer>();
	INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	
	if(examId!=0) {
		eto=new SupplementaryAppExamTo();
		eto.setExamId(examId);
		eto.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinition",true,"name"));
		
		//rgahu write newly like regular app
		String query1 = "from ExamDefinition e where e.id ="+examId;
		List<ExamDefinition> examList1=transaction.getDataForQuery(query1);
		for (ExamDefinition bo : examList1) {
			newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
			newSupplementaryImpApplicationForm.setExamName(bo.getName());
			newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth())));
			newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
		}
		
		//query1 = "from CourseToDepartment c where c.course.id ="+newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
		//List<CourseToDepartment> courseToDepartmentList=transaction.getDataForQuery(query1);
		//String dep="";
		
		/*for (CourseToDepartment bo : courseToDepartmentList) {
			if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()==1){
				dep=dep+bo.getDepartment().getName();
			}else if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()>1){
				if(count==0){
					dep=dep+bo.getDepartment().getName();
				}else{
					dep=dep+","+bo.getDepartment().getName();
				}
				
				count++;
			}
			
			newSupplementaryImpApplicationForm.setCourseDep(dep);
			
		}*/
		
		
		List boList1=transaction1.getSubjectsListForRevaluationSupplementry(newSupplementaryImpApplicationForm.getStudentObj(),examId,newSupplementaryImpApplicationForm.getRevalSupplyClassId());
		
		List<SupplementaryApplicationClassTo> classList=new ArrayList<SupplementaryApplicationClassTo>();
		SupplementaryApplicationClassTo to=null;
		
		Iterator i=boList1.iterator();
		while(i.hasNext()) {
		
				Object obj[]=(Object[])i.next();
				to = new SupplementaryApplicationClassTo();
				to.setClassId(newSupplementaryImpApplicationForm.getRevalSupplyClassId());
				
				List<ExamSupplementaryImpApplicationSubjectTO> subjectList = null;
				if (classList.contains(to)) {
					to = classList.remove(classList.indexOf(to));
					subjectList = to.getToList();
					//classNotRequiredList.add(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses() .getId());
				}else{
					subjectList = new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
					//to.setClassName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getName());
					to.setCourseName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getName());
					
					to.setRegisterNo(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo());
					to.setRollNo(newSupplementaryImpApplicationForm.getStudentObj().getRollNo());
					//to.setSemNo(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getTermNumber().toString());
					to.setStudentName(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln() .getPersonalData().getFirstName());
					if (count == 0) {
						newSupplementaryImpApplicationForm.setCourseName(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise() .getClasses().getCourse().getName());
						newSupplementaryImpApplicationForm.setRegisterNo(newSupplementaryImpApplicationForm.getStudentObj().getRegisterNo());
						newSupplementaryImpApplicationForm .setNameOfStudent(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData() .getFirstName());
						count++;
					}
					newSupplementaryImpApplicationForm.setProgramId(String .valueOf(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln() .getCourseBySelectedCourseId() .getProgram().getId()));
				
				}
					
				/*if (!classNotRequiredList.contains(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getId())) {*/
				if (!classNotRequiredList.contains(newSupplementaryImpApplicationForm.getRevalSupplyClassId())) {
					ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
					//sto.setId(bo.getId());
					if(obj[10]!=null)
					sto.setSubjectId(Integer.parseInt(obj[10].toString()));
					if(obj[11]!=null)
					sto.setSubjectCode(obj[11].toString());
					if(obj[12]!=null)
					sto.setSubjectName(obj[12].toString());
					if(obj[13]!=null){
					sto.setSubjectType(obj[13].toString());
					if(obj[13].toString().equalsIgnoreCase("t") || obj[13].toString().equalsIgnoreCase("B"))
						sto.setTheory(true);
					if(obj[13].toString().equalsIgnoreCase("p") || obj[13].toString().equalsIgnoreCase("B"))
						sto.setPractical(true);
					}
					if(obj[27] != null ){
						sto.setMaxMarks(obj[27].toString());
					}
					//if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
					if(obj[14]!=null)
					sto.setSectionName(obj[14].toString());
					sto.setCommonChecked(false);
					sto.setTempChecked(false);
					sto.setControlDisable(true);
					sto.setClassId(newSupplementaryImpApplicationForm.getRevclassid());
					//sto.setSchemeNo(newSupplementaryImpApplicationForm.getStudentObj().getClassSchemewise().getClasses().getTermNumber());
					if(obj[18]!=null || obj[19]!=null){
						
						float theroy=0;
						float practial=0;
						if(obj[18]!=null && !obj[18].toString().equalsIgnoreCase("Ab"))
							theroy=Float.parseFloat(obj[18].toString());
						if(obj[19]!=null && !obj[19].toString().equalsIgnoreCase("Ab"))	
							practial=Float.parseFloat(obj[19].toString());
						
						if(obj[13].toString().equalsIgnoreCase("B")){
							sto.setMarks(theroy+"");
						}else {
							sto.setMarks(theroy+practial+"");
						}
					}
					if(obj[13].toString().equalsIgnoreCase("t") || obj[13].toString().equalsIgnoreCase("B")){
					subjectList.add(sto);
					}
					to.setToList(subjectList);
					classList.add(to);
				}		
			
		}//close for loop subject list while
		
		
		
		eto.setExamList(classList);
		examList.add(eto);
	}//close main for loop
	
	newSupplementaryImpApplicationForm.setDisplayButton(buttonDisplay);
	newSupplementaryImpApplicationForm.setMainList(examList);
}

public boolean saveRevaluationApplicationForStudentLoginSupply(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception{
INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	Student student = new Student();
	student.setId(newSupplementaryImpApplicationForm.getStudentId());
	RevaluationApplicationPGIDetails details = transaction.getPgiDetailsSupply(newSupplementaryImpApplicationForm);
	List<ExamRevaluationApplicationNew> boList=new ArrayList<ExamRevaluationApplicationNew>();
	List<SupplementaryAppExamTo> examList=newSupplementaryImpApplicationForm.getMainList();
	for (SupplementaryAppExamTo suppTo : examList) {
		ExamDefinitionBO examDefinition = new ExamDefinitionBO();
		examDefinition.setId(suppTo.getExamId());
		List<SupplementaryApplicationClassTo> classTos=suppTo.getExamList();
		for (SupplementaryApplicationClassTo cto: classTos) {
			Classes classes = new Classes();
			classes.setId(cto.getClassId());
			List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto.getToList();
			for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
				
				
				ExamRevaluationApplicationNew objBO = new ExamRevaluationApplicationNew();
				if(to.getId()!=null)
					objBO.setId(to.getId());
					objBO.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setCreatedDate(new Date());
					objBO.setLastModifiedDate(new Date());
					objBO.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
					objBO.setStudent(student);
					objBO.setExam(examDefinition);
					Subject subject = new Subject();
					subject.setId(to.getSubjectId());
					objBO.setSubject(subject);
					objBO.setClasses(classes);
					if(details != null){
						RevaluationApplicationPGIDetails applicationPGIDetails = new RevaluationApplicationPGIDetails();
						applicationPGIDetails.setId(details.getId());
						objBO.setRevaluationPgiDetails(applicationPGIDetails);
					}else {
						objBO.setRevaluationPgiDetails(null);
					}
					objBO.setMarks(to.getMarks());
					objBO.setMaxMarks(to.getMaxMarks());
				//	objBO.setChallanVerified(false);
				
				//set new one
				if(!to.isTempChecked()){
				if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")){
					objBO.setIsRevaluation(true);
					objBO.setIsScrutiny(false);
					objBO.setIsApplied(to.getIsApplied());
					}
					
				else if(newSupplementaryImpApplicationForm.getIsScrutiny()!=null &&  newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")){
					objBO.setIsRevaluation(false);
					objBO.setIsScrutiny(true);
					objBO.setIsApplied(to.getIsApplied());
				}
				
				
				}//close checked or not
				//set old one
				else{
					objBO.setIsRevaluation(to.getRevaluation());
					objBO.setIsScrutiny(to.getScrutiny());
					objBO.setIsApplied(to.getIsApplied());
					
					
				}
				
				boList.add(objBO);
				
			
			}//close sub loop	
			
		}//close class loop
		
	}//close exam loop
	
	return transaction.saveChallengeValuationApps(boList);
}

public void getApplicationFormsForSupplyRevaluationChooseType(int examId,NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
	INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
	List<SupplementaryAppExamTo> examList=new ArrayList<SupplementaryAppExamTo>();
	SupplementaryAppExamTo eto=null;
	int count=0;
	boolean buttonDisplay=false;
	List<Integer> classNotRequiredList=new ArrayList<Integer>();
	
	
		if(examId!=0) {
			eto=new SupplementaryAppExamTo();
			eto.setExamId(examId);
			eto.setExamName(NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examId,"ExamDefinition",true,"name"));
		
		//rgahu write newly like regular app
		String query1 = "from ExamDefinition e where e.id ="+examId;
		List<ExamDefinition> examList1=transaction.getDataForQuery(query1);
		for (ExamDefinition bo : examList1) {
			newSupplementaryImpApplicationForm.setExamId(""+bo.getId());
			newSupplementaryImpApplicationForm.setExamName(bo.getName());
			newSupplementaryImpApplicationForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth())));
			newSupplementaryImpApplicationForm.setYear(""+bo.getYear());
		}
		
		/*query1 = "from CourseToDepartment c where c.course.id ="+newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
		List<CourseToDepartment> courseToDepartmentList=transaction.getDataForQuery(query1);
		String dep="";
		
		for (CourseToDepartment bo : courseToDepartmentList) {
			if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()==1){
				dep=dep+bo.getDepartment().getName();
			}else if(courseToDepartmentList.size()!=0 && courseToDepartmentList.size()>1){
				if(count==0){
					dep=dep+bo.getDepartment().getName();
				}else{
					dep=dep+","+bo.getDepartment().getName();
				}
				
				count++;
			}
			
			newSupplementaryImpApplicationForm.setCourseDep(dep);
			
		}
		*/
		
		
		
		String query="from ExamRevaluationApplicationNew er where er.classes.id="+newSupplementaryImpApplicationForm.getRevalSupplyClassId()+
				" and er.exam.id="+examId+" and er.student.id= "+newSupplementaryImpApplicationForm.getStudentId();
				
		if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")){
					query=query+"  and er.isRevaluation=1" ;
				}
		if(newSupplementaryImpApplicationForm.getIsScrutiny()!=null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")){
					query=query+"  and er.isScrutiny=1 " ;
				}
		
				
		List<ExamRevaluationApplicationNew> boList= transaction.getDataForQuery(query);
		
		Iterator<ExamRevaluationApplicationNew> iterator = boList.iterator();
		int countApp = 0;
		while(iterator.hasNext()){
			ExamRevaluationApplicationNew new1 = iterator.next();
			if(new1.getRevaluationPgiDetails() != null){
			if(new1.getRevaluationPgiDetails().getId() != 0){
				countApp++;
			}
			}
		}
		if(countApp++ >0){
			newSupplementaryImpApplicationForm.setPaymentDone(true);
		}else {
			newSupplementaryImpApplicationForm.setPaymentDone(false);
		}
		List<SupplementaryApplicationClassTo> classList=new ArrayList<SupplementaryApplicationClassTo>();
		SupplementaryApplicationClassTo to=null;
		
		for (ExamRevaluationApplicationNew bo : boList) {
			if (!classNotRequiredList.contains(bo.getClasses().getId())) {
				to = new SupplementaryApplicationClassTo();
				to.setClassId(bo.getClasses().getId());
				to.setClassName(bo.getClasses().getName());
				List<ExamSupplementaryImpApplicationSubjectTO> subjectList = null;
				if (classList.contains(to)) {
					to = classList.remove(classList.indexOf(to));
					subjectList = to.getToList();
					
						if (to.isRevaluation() != bo.getIsRevaluation()){
							//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
							//classNotRequiredList.add(bo.getClasses() .getId());
						}else if (to.isScrutiny() != bo.getIsScrutiny()){
							//seniors wrote this condition to check supply student have improvement, if he has he will not get receipt, but raghu put cooment
							//classNotRequiredList.add(bo.getClasses() .getId());
						}
					
				} else {
					subjectList = new ArrayList<ExamSupplementaryImpApplicationSubjectTO>();
					to.setCourseName(bo.getClasses().getCourse().getName());
					to.setRegisterNo(bo.getStudent().getRegisterNo());
					to.setRollNo(bo.getStudent().getRollNo());
					to.setSemNo(bo.getClasses().getTermNumber().toString());
					to.setStudentName(bo.getStudent().getAdmAppln() .getPersonalData().getFirstName());
					to.setClassName(bo.getClasses().getName());
					
					if (bo.getIsRevaluation())
						to.setRevaluation(true);
					if (bo.getIsScrutiny())
						to.setScrutiny(true);
					
					
					if (count == 0) {
						newSupplementaryImpApplicationForm.setCourseName(bo .getClasses().getCourse().getName());
						newSupplementaryImpApplicationForm.setRegisterNo(bo .getStudent().getRegisterNo());
						newSupplementaryImpApplicationForm .setNameOfStudent(bo.getStudent() .getAdmAppln().getPersonalData() .getFirstName());
						count++;
					}
					newSupplementaryImpApplicationForm.setProgramId(String .valueOf(bo.getStudent().getAdmAppln() .getCourseBySelectedCourseId() .getProgram().getId()));
				}
				if (!classNotRequiredList.contains(bo.getClasses().getId())) {
					ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
					sto.setId(bo.getId());
					sto.setSubjectId(bo.getSubject().getId());
					sto.setSubjectCode(bo.getSubject().getCode());
					sto.setSubjectName(bo.getSubject().getName());
					//raghu new 
					sto.setSubjectType(bo.getSubject().getIsTheoryPractical());
					
					if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getName().equalsIgnoreCase("UG")){
						
					String subQuery =   "select esb.examSubjectSectionMasterBO.name from ExamSubDefinitionCourseWiseBO esb where   esb.subjectUtilBO.id = "+bo.getSubject().getId();
					List sList= transaction.getDataForQuery(subQuery);
					for (Object s : sList) {
						sto.setSectionName(s.toString());
					}
					
					}
					
					if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("t") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
						sto.setTheory(true);
					if(bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("p") || bo.getSubject().getIsTheoryPractical().equalsIgnoreCase("B"))
						sto.setPractical(true);
					
					if (bo.getIsRevaluation()){
						sto.setRevaluation(true);
					}else if (bo.getIsScrutiny()){
						sto.setScrutiny(true);
					}
					
						
					if (bo.getIsApplied()){
						sto.setIsApplied(true);
						sto.setCommonChecked(true);
						sto.setTempChecked(true);
					}
					
					boolean controlDisable = false;
					
					if ((bo.getIsRevaluation() == true) || (bo.getIsScrutiny() == true || bo.getIsAnswerScrptCopy() == true )) {
						controlDisable = true;
					}
					
					sto.setControlDisable(controlDisable);
					sto.setClassId(bo.getClasses().getId());
					sto.setSchemeNo(bo.getClasses().getTermNumber());
					sto.setMarks(bo.getMarks());
					sto.setMaxMarks(bo.getMaxMarks());
					
					subjectList.add(sto);
					to.setToList(subjectList);
					
					if(to.isRevaluation())
						if (bo.getIsApplied() != bo.getIsRevaluation()){
							buttonDisplay = true;
						}
					if(to.isScrutiny())
						if (bo.getIsApplied() != bo.getIsScrutiny()){
							buttonDisplay = true;
						}
					
					
					
					
					classList.add(to);
				}
			}
		}//close for loop subject list
		
		
		
		eto.setExamList(classList);
		examList.add(eto);
	}//close main for loop
	
	newSupplementaryImpApplicationForm.setDisplayButton(buttonDisplay);
	newSupplementaryImpApplicationForm.setMainList(examList);
	
}
public boolean checkOnlinePaymentReg(NewSupplementaryImpApplicationForm admForm) throws Exception{
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	boolean dup=false;
	CandidatePGIDetailsExamRegular bo=transaction.checkOnlinePaymentReg(admForm);
	if(bo!=null){
		dup=true;
	}
	return dup;
}

public String getParameterForPGIReg(NewSupplementaryImpApplicationForm admForm) throws Exception{

	
	CandidatePGIDetailsExamRegular bo= new CandidatePGIDetailsExamRegular();
	bo.setCandidateName(admForm.getNameOfStudent());

	bo.setTxnStatus("Pending");
		if(admForm.getTotalFee()!=null && !admForm.getTotalFee().isEmpty())
			bo.setTxnAmount(new BigDecimal(admForm.getTotalFee()));
	
	
	//below setting of txn amount has to be commented once the Production phase of PGI has started 
//	bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
	bo.setMobileNo1(admForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
	bo.setMobileNo2(admForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
	bo.setEmail(admForm.getEmail());
	bo.setCreatedBy(admForm.getUserId());
	bo.setCreatedDate(new Date());
	bo.setLastModifiedDate(new Date());
	bo.setModifiedBy(admForm.getUserId());
	
	Student student = new Student();
	student.setId(admForm.getStudentId());
	bo.setStudent(student);
	ExamDefinitionBO examDefinition = new ExamDefinitionBO();
	examDefinition.setId(Integer.valueOf(admForm.getExamId()));
	bo.setExam(examDefinition);
	
			
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	String candidateRefNo=transaction.generateCandidateRefNoReg(bo);
	StringBuilder temp=new StringBuilder();
			
	String productinfo="productinfo";
	admForm.setRefNo(candidateRefNo);
	admForm.setProductinfo(productinfo);
	
	//change the url of response in the msg below when it has to be released to the production. Please put the production IP
	if(candidateRefNo!=null && !candidateRefNo.isEmpty())
	 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
		temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
	//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
	//raghu write for pay e
	String hash=hashCal("SHA-512",temp.toString());
	admForm.setTest(temp.toString());
	
	//if(checkSum!=null && !checkSum.isEmpty())
	// msg.append(temp).append("|").append(checkSum);
	return hash;



}

public boolean updateResponseReg(NewSupplementaryImpApplicationForm admForm) throws Exception{
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	CandidatePGIDetailsExamRegular bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoReg(admForm);
	isUpdated=transaction.updateReceivedStatusReg(bo,admForm);
	return isUpdated;
}

public boolean checkPaymentDetails(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception{
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	boolean dup=false;
	dup=transaction.checkPaymentDetails(newSupplementaryImpApplicationForm);
	return dup;
	
}
public boolean checkOnlinePaymentSuppl(NewSupplementaryImpApplicationForm admForm) throws Exception{
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	boolean dup=false;
	CandidatePGIDetailsExamSupply bo=transaction.checkOnlinePaymentSuppl(admForm);
	if(bo!=null){
		dup=true;
	}
	return dup;
}

public String getParameterForPGISuppl(NewSupplementaryImpApplicationForm admForm) throws Exception{

	
	CandidatePGIDetailsExamSupply bo= new CandidatePGIDetailsExamSupply();
	bo.setCandidateName(admForm.getNameOfStudent());

	bo.setTxnStatus("Pending");
		if( String.valueOf(admForm.getTotalFees())!=null && !String.valueOf(admForm.getTotalFees()).isEmpty())
			bo.setTxnAmount(new BigDecimal(String.valueOf(admForm.getTotalFees())));
		
	
	//below setting of txn amount has to be commented once the Production phase of PGI has started 
//	bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
	bo.setMobileNo1(admForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
	bo.setMobileNo2(admForm.getMobileNo());
	bo.setEmail(admForm.getEmail());
	bo.setCreatedBy(admForm.getUserId());
	bo.setCreatedDate(new Date());
	bo.setLastModifiedDate(new Date());
	bo.setModifiedBy(admForm.getUserId());
	
	Student student = new Student();
	student.setId(admForm.getStudentId());
	bo.setStudent(student);
	ExamDefinitionBO examDefinition = new ExamDefinitionBO();
	examDefinition.setId(Integer.valueOf(admForm.getExamId()));
	bo.setExam(examDefinition);
	Classes classes=new Classes();
	classes.setId(admForm.getSuppImpClassId());
	bo.setClasses(classes);
			
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	String candidateRefNo=transaction.generateCandidateRefNoSuppl(bo);
	StringBuilder temp=new StringBuilder();
			
	String productinfo="productinfo";
	admForm.setRefNo(candidateRefNo);
	admForm.setProductinfo(productinfo);
	
	//change the url of response in the msg below when it has to be released to the production. Please put the production IP
	if(candidateRefNo!=null && !candidateRefNo.isEmpty())
	 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
	temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
	//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
	//raghu write for pay e
	String hash=hashCal("SHA-512",temp.toString());
	admForm.setTest(temp.toString());
	
	//if(checkSum!=null && !checkSum.isEmpty())
	// msg.append(temp).append("|").append(checkSum);
	return hash;



}

public boolean updateResponseSupp(NewSupplementaryImpApplicationForm admForm) throws Exception{
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	CandidatePGIDetailsExamSupply bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoSupp(admForm);
	isUpdated=transaction.updateReceivedStatusSupp(bo,admForm);
	return isUpdated;
}

public String getParameterForRevaluation(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
	RevaluationApplicationPGIDetails bo= new RevaluationApplicationPGIDetails();
	bo.setCandidateName(newSupplementaryImpApplicationForm.getNameOfStudent());

	bo.setTxnStatus("Pending");
	if(newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees()!=null && !newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees().isEmpty())
			bo.setTxnAmount(new BigDecimal(newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees()));
	
	
	//below setting of txn amount has to be commented once the Production phase of PGI has started 
//	bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
	bo.setMobileNo1(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
	bo.setMobileNo2(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
	bo.setEmail(newSupplementaryImpApplicationForm.getEmail());
	bo.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
	bo.setCreatedDate(new Date());
	bo.setLastModifiedDate(new Date());
	bo.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
	
	Student student = new Student();
	student.setId(newSupplementaryImpApplicationForm.getStudentId());
	bo.setStudent(student);
	ExamDefinitionBO examDefinition = new ExamDefinitionBO();
	examDefinition.setId(Integer.valueOf(newSupplementaryImpApplicationForm.getExamId()));
	bo.setExam(examDefinition);
	
			
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	String candidateRefNo=transaction.generateCandidateRefNoRevaluation(bo);
	
	StringBuilder temp=new StringBuilder();
			
	String productinfo="productinfo";
	newSupplementaryImpApplicationForm.setRefNo(candidateRefNo);
	newSupplementaryImpApplicationForm.setProductinfo(productinfo);
	
	//change the url of response in the msg below when it has to be released to the production. Please put the production IP
	if(candidateRefNo!=null && !candidateRefNo.isEmpty())
	 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
		temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
	//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
	//raghu write for pay e
	String hash=hashCal("SHA-512",temp.toString());
	newSupplementaryImpApplicationForm.setTest(temp.toString());
	
	//if(checkSum!=null && !checkSum.isEmpty())
	// msg.append(temp).append("|").append(checkSum);
	return hash;

}

public boolean checkDuplicateRecord(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	boolean dup=false;
	RevaluationApplicationPGIDetails bo=transaction.checkOnlinePaymentRev(newSupplementaryImpApplicationForm);
	if(bo!=null){
		dup=true;
	}
	return dup;
}

public boolean updateResponseRev(NewSupplementaryImpApplicationForm admForm) throws Exception {
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	RevaluationApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoRev(admForm);
	isUpdated=transaction.updateReceivedStatusRev(bo,admForm);
	return isUpdated;
}

public List<RevaluationApplicationPGIDetails> getPendingDetails(NewSupplementaryImpApplicationForm admForm)  throws Exception{
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<RevaluationApplicationPGIDetails> list = transaction.getPendingList(admForm);
	return list;
}

public boolean updateData(NewSupplementaryImpApplicationForm admForm, RevaluationApplicationPGIDetails details) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<ExamRevaluationApplicationNew> list = transaction.getExistingData(admForm,details);
	Iterator<ExamRevaluationApplicationNew> iterator = list.iterator();
	List<ExamRevaluationApplicationNew> list2 = new ArrayList<ExamRevaluationApplicationNew>();
	while(iterator.hasNext()){
		ExamRevaluationApplicationNew applicationNew = iterator.next();
		RevaluationApplicationPGIDetails details2 = new RevaluationApplicationPGIDetails();
		details2.setId(details.getId());
		applicationNew.setRevaluationPgiDetails(details2);
		list2.add(applicationNew);
	}
	
	boolean isUpdated = transaction.updateRev(admForm,list2);
	return isUpdated;
}

public RevaluationApplicationPGIDetails getSuccessDetails(NewSupplementaryImpApplicationForm admForm) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	RevaluationApplicationPGIDetails details = transaction.getSuccessDetails(admForm);
	return details;
}

public boolean updateResponseScr(NewSupplementaryImpApplicationForm admForm) throws Exception {
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	RevaluationApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoScr(admForm);
	isUpdated=transaction.updateReceivedStatusScr(bo,admForm);
	return isUpdated;
}

public boolean updateDataScr(NewSupplementaryImpApplicationForm admForm,
		RevaluationApplicationPGIDetails details) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<ExamRevaluationApplicationNew> list = transaction.getExistingDataScr(admForm,details);
	Iterator<ExamRevaluationApplicationNew> iterator = list.iterator();
	List<ExamRevaluationApplicationNew> list3 = new ArrayList<ExamRevaluationApplicationNew>();
	while(iterator.hasNext()){
		ExamRevaluationApplicationNew applicationNew = iterator.next();
		RevaluationApplicationPGIDetails details2 = new RevaluationApplicationPGIDetails();
		details2.setId(details.getId());
		applicationNew.setRevaluationPgiDetails(details2);
		list3.add(applicationNew);
	}
	
	boolean isUpdated = transaction.updateScr(admForm,list3);
	return isUpdated;
}

public String getParameterForRevaluationSupply(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception {
	RevaluationApplicationPGIDetails bo= new RevaluationApplicationPGIDetails();
	bo.setCandidateName(newSupplementaryImpApplicationForm.getNameOfStudent());

	bo.setTxnStatus("Pending");
	if(newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees()!=null && !newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees().isEmpty())
			bo.setTxnAmount(new BigDecimal(newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees()));
	
	
	//below setting of txn amount has to be commented once the Production phase of PGI has started 
//	bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
	bo.setMobileNo1(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
	bo.setMobileNo2(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
	bo.setEmail(newSupplementaryImpApplicationForm.getEmail());
	bo.setCreatedBy(newSupplementaryImpApplicationForm.getUserId());
	bo.setCreatedDate(new Date());
	bo.setLastModifiedDate(new Date());
	bo.setModifiedBy(newSupplementaryImpApplicationForm.getUserId());
	
	Student student = new Student();
	student.setId(newSupplementaryImpApplicationForm.getStudentId());
	bo.setStudent(student);
	ExamDefinitionBO examDefinition = new ExamDefinitionBO();
	examDefinition.setId(Integer.valueOf(newSupplementaryImpApplicationForm.getExamId()));
	bo.setExam(examDefinition);
	
			
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	String candidateRefNo=transaction.generateCandidateRefNoRevaluationSupply(bo);
	
	StringBuilder temp=new StringBuilder();
			
	String productinfo="productinfo";
	newSupplementaryImpApplicationForm.setRefNo(candidateRefNo);
	newSupplementaryImpApplicationForm.setProductinfo(productinfo);
	
	//change the url of response in the msg below when it has to be released to the production. Please put the production IP
	if(candidateRefNo!=null && !candidateRefNo.isEmpty())
	 //temp.append(CMSConstants.PGI_MERCHANT_ID).append("|").append(candidateRefNo).append("|NA|").append(bo.getTxnAmount()).append("|NA|NA|NA|INR|NA|R|").append(CMSConstants.PGI_SECURITY_ID).append("|NA|NA|F|applicationFee|NA|NA|NA|NA|NA|NA|").append(CMSConstants.pgiLink);
		temp.append(CMSConstants.PGI_MERCHANT_ID_REV).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_REV);
	//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
	//raghu write for pay e
	String hash=hashCal("SHA-512",temp.toString());
	newSupplementaryImpApplicationForm.setTest(temp.toString());
	
	//if(checkSum!=null && !checkSum.isEmpty())
	// msg.append(temp).append("|").append(checkSum);
	return hash;
}

public List<RevaluationApplicationPGIDetails> getPendingDetailsForSupply(NewSupplementaryImpApplicationForm admForm) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<RevaluationApplicationPGIDetails> list = transaction.getPendingListSupply(admForm);
	return list;
}

public boolean updateResponseRevSupply(NewSupplementaryImpApplicationForm admForm) throws Exception {
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	RevaluationApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoRevSupply(admForm);
	isUpdated=transaction.updateReceivedStatusRevSupply(bo,admForm);
	return isUpdated;
}

public boolean updateResponseScrSupply(NewSupplementaryImpApplicationForm admForm) throws Exception {
	boolean isUpdated=false;
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	
	RevaluationApplicationPGIDetails bo=NewSupplementaryImpApplicationHelper.getInstance().convertToBoScrSupply(admForm);
	isUpdated=transaction.updateReceivedStatusScrSupply(bo,admForm);
	return isUpdated;
}

public boolean updateDataSupply(NewSupplementaryImpApplicationForm admForm,RevaluationApplicationPGIDetails details) throws Exception{
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<ExamRevaluationApplicationNew> list = transaction.getExistingData(admForm,details);
	Iterator<ExamRevaluationApplicationNew> iterator = list.iterator();
	List<ExamRevaluationApplicationNew> list5 = new ArrayList<ExamRevaluationApplicationNew>();
	while(iterator.hasNext()){
		ExamRevaluationApplicationNew applicationNew = iterator.next();
		RevaluationApplicationPGIDetails details2 = new RevaluationApplicationPGIDetails();
		details2.setId(details.getId());
		applicationNew.setRevaluationPgiDetails(details2);
		list5.add(applicationNew);
	}
	
	boolean isUpdated = transaction.updateRevSupply(admForm,list5);
	return isUpdated;
}

public boolean updateDataScrSupply(NewSupplementaryImpApplicationForm admForm,RevaluationApplicationPGIDetails details) throws Exception {
	INewSupplementaryImpApplicationTransaction transaction=NewSupplementaryImpApplicationTransactionImpl.getInstance();
	List<ExamRevaluationApplicationNew> list = transaction.getExistingDataScr(admForm,details);
	Iterator<ExamRevaluationApplicationNew> iterator = list.iterator();
	List<ExamRevaluationApplicationNew> list6 = new ArrayList<ExamRevaluationApplicationNew>();
	while(iterator.hasNext()){
		ExamRevaluationApplicationNew applicationNew = iterator.next();
		RevaluationApplicationPGIDetails details2 = new RevaluationApplicationPGIDetails();
		details2.setId(details.getId());
		applicationNew.setRevaluationPgiDetails(details2);
		list6.add(applicationNew);
	}
	
	boolean isUpdated = transaction.updateScrSupply(admForm,list6);
	return isUpdated;
}

}
