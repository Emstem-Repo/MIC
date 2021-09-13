package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamExamCourseSchemeDetailsBO;
import com.kp.cms.bo.exam.ExamFooterAgreementBO;
import com.kp.cms.bo.exam.ExamPublishHallTicketMarksCardBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.SubjectUtilBO;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.exam.ExamStudentTokenRegisterdForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.handlers.attendance.StudentAttendanceSummaryHandler;
import com.kp.cms.helpers.exam.NewSupplementaryImpApplicationHelper;
import com.kp.cms.helpers.exam.StudentTokenRegisterHelper;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationSubjectTO;
import com.kp.cms.to.exam.SupplementaryAppExamTo;
import com.kp.cms.to.reports.StudentWiseSubjectSummaryTO;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction;
import com.kp.cms.transactions.exam.IStudentTokenRegisteredTransaction;
import com.kp.cms.transactionsimpl.admission.DisciplinaryDetailsImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewSupplementaryImpApplicationTransactionImpl;
import com.kp.cms.transactionsimpl.exam.StudentTokenRegisteredTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.PropertyUtil;

public class StudentTokenRegisteredHandler extends ExamGenHandler {
	
	private static volatile StudentTokenRegisteredHandler studentTokenRegisteredHandler = null;
	private static final Log log = LogFactory.getLog(StudentTokenRegisteredHandler.class);
	
	public static StudentTokenRegisteredHandler getInstance(){
		if(studentTokenRegisteredHandler == null){
			studentTokenRegisteredHandler = new StudentTokenRegisteredHandler();
		}
		return studentTokenRegisteredHandler;
	}
	


	public ExamRegularApplication getData(String studentId) throws Exception {
		ExamRegularApplication examRegApp = null;
		IStudentTokenRegisteredTransaction transaction =  StudentTokenRegisteredTransactionImpl.getInstance();
		examRegApp = transaction.getData(studentId);
		return examRegApp;
	}

	public ExamRegularApplication checkValid(ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		int studentId = transaction.getStudentId(examStudentTokenRegisterdForm.getRegistrationNumber());		
		examStudentTokenRegisterdForm.setStudentId(studentId);
		ExamRegularApplication regularApplication =  transaction.hasStudentAppliedForExam(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()), studentId);
		return regularApplication;
	}
	public List<StudentSupplementaryImprovementApplication> checkValidSupp(ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		int studentId = transaction.getStudentId(examStudentTokenRegisterdForm.getRegistrationNumber());
		examStudentTokenRegisterdForm.setStudentId(studentId);
		List<StudentSupplementaryImprovementApplication> supplementryApplications = transaction.hasStudentAppearedForExam(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()), studentId);
		return supplementryApplications;
		
	}



	public boolean saveRegularApplication(ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		ExamRegularApplication examRegularApplication = new ExamRegularApplication();
		examRegularApplication.setIsApplied(true);
		Student student = new Student();
		student.setId(examStudentTokenRegisterdForm.getStudentId());
		examRegularApplication.setStudent(student);
		ExamDefinitionBO examDefinition = new ExamDefinitionBO();
		examDefinition.setId(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()));
		examRegularApplication.setExam(examDefinition);
		int classId = transaction.getClassId(examStudentTokenRegisterdForm.getExamId(), examStudentTokenRegisterdForm.getStudentId());
		Classes classes = new Classes();
		classes.setId(classId);
		examRegularApplication.setClasses(classes);
		examRegularApplication.setCreatedBy(examStudentTokenRegisterdForm.getUserId());
		examRegularApplication.setModifiedBy(examStudentTokenRegisterdForm.getUserId());
		examRegularApplication.setCreatedDate(new Date());
		examRegularApplication.setLastModifiedDate(new Date());
		examRegularApplication.setIsTokenRegisterd(true);
		examRegularApplication.setComments(examStudentTokenRegisterdForm.getComment());
		return transaction.addRegularAppData(examRegularApplication);
	}



	public boolean updateRegularApplication(ExamRegularApplication examRegularApplication) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		examRegularApplication.setIsApplied(true);	
		examRegularApplication.setIsTokenRegisterd(true);
		return transaction.updateRegularAppData(examRegularApplication);		
		
	}

	/*public boolean saveSupplementryApplication(ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		int classId = transaction.getSuppClassId(examStudentTokenRegisterdForm.getExamId(), examStudentTokenRegisterdForm.getStudentId());
		examStudentTokenRegisterdForm.setClassId(String.valueOf(classId));
		List<StudentSupplementaryImprovementApplication> tokenRegistrations = new ArrayList<StudentSupplementaryImprovementApplication>();
		List<Object[]> objects = transaction.getTotalSubjectList(examStudentTokenRegisterdForm.getStudentId(),examStudentTokenRegisterdForm.getClassId());
		Iterator<Object[]> iterator = objects.iterator();
		while(iterator.hasNext()) {
			Object[] subjects = iterator.next();
			StudentSupplementaryImprovementApplication improvementApplication = new StudentSupplementaryImprovementApplication();
			Student student = new Student();
			student.setId(examStudentTokenRegisterdForm.getStudentId());
			improvementApplication.setStudent(student);
			ExamDefinition examDefinition = new ExamDefinition();
			examDefinition.setId(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()));
			improvementApplication.setExamDefinition(examDefinition);
			Classes classes = new Classes();
			classes.setId(classId);
			improvementApplication.setClasses(classes);
			Subject subject = new Subject();
			subject.setId((Integer)subjects[0]);
			improvementApplication.setSubject(subject);
			improvementApplication.setIsSupplementary(true);
			improvementApplication.setIsImprovement(false);
			improvementApplication.setIsFailedTheory((subjects[1].toString().equalsIgnoreCase("T") || subjects[1].toString().equalsIgnoreCase("B")) ? true : false);
			improvementApplication.setIsFailedPractical((subjects[1].toString().equalsIgnoreCase("P") || subjects[1].toString().equalsIgnoreCase("B")) ? true : false);
			improvementApplication.setIsAppearedTheory(false);
			improvementApplication.setIsAppearedPractical(false);
			improvementApplication.setIsOnline(false);
			improvementApplication.setSchemeNo(Integer.parseInt(subjects[2].toString()));
			improvementApplication.setCreatedBy(examStudentTokenRegisterdForm.getUserId());
			improvementApplication.setModifiedBy(examStudentTokenRegisterdForm.getUserId());
			improvementApplication.setCreatedDate(new Date());
			improvementApplication.setLastModifiedDate(new Date());
			improvementApplication.setIsTokenRegisterd(true);	
			tokenRegistrations.add(improvementApplication);
		}
		
		return transaction.saveSupplementaryTokenRegistration(tokenRegistrations);
	}*/
	
	/*public double getRegularFees(ExamStudentTokenRegisterdForm form) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationSubjectTO> subjectList = new LinkedList<ExamSupplementaryImpApplicationSubjectTO>();
		SupplementaryAppExamTo eto=null;
		int count=0;
		
		String query="from Student s where s.registerNo = '"+form.getRegistrationNumber()+"'";
		Student student=(Student)PropertyUtil.getDataForUniqueObject(query);
		form.setStudentObj(student);
		
		String query1="from RegularExamFees r where r.academicYear = "+form.getYear()+"  and r.course.id="+student.getAdmAppln().getCourse().getId();
		RegularExamFees regFees=(RegularExamFees)PropertyUtil.getDataForUniqueObject(query1);
		String query2="from ExamExamCourseSchemeDetailsBO r where r.examId = "+form.getExamId()+"  and r.courseId="+student.getAdmAppln().getCourse().getId()+" and r.isActive=1";
		ExamExamCourseSchemeDetailsBO examCourse=(ExamExamCourseSchemeDetailsBO)PropertyUtil.getDataForUniqueObject(query2);
			List boList = null; 
				boList=transaction1.getSubjectsListForStudentAdmin(student,Integer.parseInt(form.getYear()),examCourse.getSchemeNo());
				if(boList==null || boList.isEmpty())
				boList=transaction1.getSubjectsListForStudentPrevClassAdmin(student,Integer.parseInt(form.getYear()),examCourse.getSchemeNo());

				
			Iterator i=boList.iterator();
			double totalTheoryFees=0;
			double grandTotalFees=0;
			double marksListFees=0;
			double cvCampFees=0;
			double onlineServiceCharge=0;
			double applicationFees=0;
			while(i.hasNext()) {
				ExamSupplementaryImpApplicationSubjectTO sto = new ExamSupplementaryImpApplicationSubjectTO();
				Object obj[]=(Object[])i.next();
				SubjectUtilBO bo=(SubjectUtilBO)obj[0];
				sto.setSubjectCode(bo.getCode());
				sto.setSubjectName(bo.getName());
				if("PROJECT".equalsIgnoreCase(bo.getSubjectType().getName())) {
					sto.setSubjectType("PR");
					totalTheoryFees = totalTheoryFees+regFees.getProjectFees().doubleValue();
				}					
				else if("VIVA".equalsIgnoreCase(bo.getSubjectType().getName())) {
					sto.setSubjectType("V");
					totalTheoryFees = totalTheoryFees+regFees.getVivaFees().doubleValue();
				}
				else {
					sto.setSubjectType(bo.getIsTheoryPractical());
					if(bo.getIsTheoryPractical().equalsIgnoreCase("T")){
						totalTheoryFees = totalTheoryFees+regFees.getTheoryFees().doubleValue();
					}
					else{
						totalTheoryFees = totalTheoryFees+regFees.getPracticalFees().doubleValue();
					}
				}
				if(obj[1]!=null){
					sto.setSectionName(obj[1].toString());
				}else{
					sto.setSectionName("");
				}
				
				subjectList.add(sto);
						
			}

		       	marksListFees = regFees.getMarksListFees().doubleValue();
				cvCampFees = regFees.getCvCampFees().doubleValue();
				onlineServiceCharge = regFees.getOnlineServiceChargeFees().doubleValue();
				applicationFees = regFees.getApplicationFees().doubleValue();

			grandTotalFees = totalTheoryFees+marksListFees+cvCampFees+onlineServiceCharge+applicationFees;
			return grandTotalFees;
			
		
	}
	
	public boolean checkOnlinePaymentReg(ExamStudentTokenRegisterdForm admForm) throws Exception{
		boolean isUpdated=false;
		IStudentTokenRegisteredTransaction transaction=StudentTokenRegisteredTransactionImpl.getInstance();
		boolean dup=false;
		CandidatePGIDetailsExamRegular bo=transaction.checkOnlinePaymentReg(admForm);
		if(bo!=null){
			dup=true;
		}
		return dup;
	}*/
	
	/*public String getParameterForPGIReg(ExamStudentTokenRegisterdForm admForm) throws Exception {

		
		CandidatePGIDetailsExamRegular bo= new CandidatePGIDetailsExamRegular();
		bo.setCandidateName(admForm.getStudentObj().getAdmAppln().getPersonalData().getFirstName());
	
		bo.setTxnStatus("Pending");
			if(admForm.getTotalFees()!=null && !admForm.getTotalFees().isEmpty())
				bo.setTxnAmount(new BigDecimal(admForm.getTotalFees()));
		
		
		//below setting of txn amount has to be commented once the Production phase of PGI has started 
//		bo.setTxnAmount(new BigDecimal("2"));// for testing purpose we are passing 2 rupees
		bo.setMobileNo1(admForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo1());
		bo.setMobileNo2(admForm.getStudentObj().getAdmAppln().getPersonalData().getMobileNo2());
		bo.setEmail(admForm.getStudentObj().getAdmAppln().getPersonalData().getEmail());
		bo.setCreatedBy(admForm.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setModifiedBy(admForm.getUserId());
		
		Student student = new Student();
		student.setId(admForm.getStudentObj().getId());
		bo.setStudent(student);
		ExamDefinitionBO examDefinition = new ExamDefinitionBO();
		examDefinition.setId(Integer.parseInt(admForm.getExamId()));
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
			temp.append(CMSConstants.PGI_MERCHANT_ID_EXAM).append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append(CMSConstants.PGI_SECURITY_ID_EXAM);
		//sha512 ("key|txnid|amount|productinfo|firstname|email|||||||||||","<SALT>");
		//raghu write for pay e
		String hash=NewSupplementaryImpApplicationHandler.getInstance().hashCal("SHA-512",temp.toString());
		admForm.setTest(temp.toString());
		
		//if(checkSum!=null && !checkSum.isEmpty())
		// msg.append(temp).append("|").append(checkSum);
		return hash;
	
	
	
	}*/
	
	/*public boolean updateResponseReg(ExamStudentTokenRegisterdForm admForm) throws Exception{
		boolean isUpdated=false;
		IStudentTokenRegisteredTransaction transaction=StudentTokenRegisteredTransactionImpl.getInstance();
		
		CandidatePGIDetailsExamRegular bo=StudentTokenRegisterHelper.getInstance().convertToBoReg(admForm);
		isUpdated=transaction.updateReceivedStatusReg(bo,admForm);
		return isUpdated;
	}*/
	
	public boolean isRegisterNoValid(ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		IStudentTokenRegisteredTransaction transaction = StudentTokenRegisteredTransactionImpl.getInstance();
		boolean isRegisterNoValid=false;
		//boolean isRegisterNoValid = transaction.isRegisterNoValid(Integer.parseInt(examStudentTokenRegisterdForm.getExamId()), examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getCourse().getId());
		List<Integer> classIds=transaction.getAllClassIdsStudent(examStudentTokenRegisterdForm.getStudentObj().getId());
		int classId=transaction.getClassId(examStudentTokenRegisterdForm.getExamId(), examStudentTokenRegisterdForm.getStudentObj().getId());
		if(classIds.contains(classId)){
			isRegisterNoValid=true;
		}
		else
			isRegisterNoValid=false;
		return isRegisterNoValid;
		
	}
	
	public void getStudentObject(ExamStudentTokenRegisterdForm form) throws Exception {
		String query="from Student s where s.registerNo = '"+form.getRegistrationNumber()+"'";
		Student student=(Student)PropertyUtil.getDataForUniqueObject(query);
		form.setStudentObj(student);
	}
	
	public void getApplicationFormsForRegular(List<Integer> examIds, ExamStudentTokenRegisterdForm examStudentTokenRegisterdForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		INewSupplementaryImpApplicationTransaction transaction1=NewSupplementaryImpApplicationTransactionImpl.getInstance();
		IStudentTokenRegisteredTransaction studentTokenTransactions = StudentTokenRegisteredTransactionImpl.getInstance();
		List<ExamSupplementaryImpApplicationSubjectTO> subjectList = new LinkedList<ExamSupplementaryImpApplicationSubjectTO>();
		SupplementaryAppExamTo eto=null;
		int studentId=examStudentTokenRegisterdForm.getStudentObj().getId();
		int count=0;
			int examId=Integer.parseInt(examStudentTokenRegisterdForm.getExamId());
			String query="";
			query = "from ExamDefinition e where e.id ="+examId;
			List<ExamDefinition> examList=transaction.getDataForQuery(query);
			for (ExamDefinition bo : examList) {
				examStudentTokenRegisterdForm.setAcademicYear(String.valueOf(bo.getAcademicYear()));
				examStudentTokenRegisterdForm.setExamId(""+bo.getId());
				examStudentTokenRegisterdForm.setExamName(bo.getName().toUpperCase());
				examStudentTokenRegisterdForm.setMonth(CommonUtil.getMonthForNumber(Integer.parseInt(bo.getMonth().toUpperCase())));

				examStudentTokenRegisterdForm.setYear(""+bo.getYear());
			}
			
			query = "from CourseToDepartment c where c.course.id ="+examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId().getId();
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
				
				examStudentTokenRegisterdForm.setCourseDep(dep);
				
			}
			
			List boList = null; 
			if(examStudentTokenRegisterdForm.getPreviousExam()){
				boList=transaction1.getSubjectsListForStudentPrevClass(examStudentTokenRegisterdForm.getStudentObj(), Integer.parseInt(examStudentTokenRegisterdForm.getAcademicYear()));
			    if(boList.size()>0){
			    	examStudentTokenRegisterdForm.setRegularAppAvailable(true);
			    	examStudentTokenRegisterdForm.setIsPreviousExam("no");
			    }
			}
			else
				boList=transaction1.getSubjectsListForStudent(examStudentTokenRegisterdForm.getStudentObj(), Integer.parseInt(examStudentTokenRegisterdForm.getAcademicYear()));
			if(examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getTermNumber()==1){
				examStudentTokenRegisterdForm.setIsPreviousExam("no");
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
			RegularExamFees regExamFeesBo = studentTokenTransactions.getRegularExamFees(examStudentTokenRegisterdForm);
			double totalfee = 0;
			if(regExamFeesBo!=null)
				totalfee = Double.parseDouble(regExamFeesBo.getTheoryFees().toString());
			String totalfeeinWords = CommonUtil.numberToWord((int)totalfee);
			if(examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection()!=null)
			{
				String religionSection = examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getPersonalData().getReligionSection().getName();
				
				if(religionSection.equalsIgnoreCase("SC") || 
				   religionSection.equalsIgnoreCase("ST") || 
				   religionSection.equalsIgnoreCase("OEC") ||
				   religionSection.equalsIgnoreCase("FMN") ||
				   religionSection.equalsIgnoreCase("KPCR") ||
				   religionSection.equalsIgnoreCase("SEBC") ||
				   religionSection.equalsIgnoreCase("OBC(Non- Creamy)"))
				{	
					examStudentTokenRegisterdForm.setRegExamFeesExempted(true);
					examStudentTokenRegisterdForm.setApplicationAmount("Fees Exempted");
					examStudentTokenRegisterdForm.setApplicationAmount1("Fees Exempted");
					examStudentTokenRegisterdForm.setApplicationAmountWords("Fees Exempted");
					examStudentTokenRegisterdForm.setSupSubjectList(subjectList);
					// set journalNo for payment through challan
					examStudentTokenRegisterdForm.setJournalNo("Fees Exempted");
				}
				else if(religionSection.equalsIgnoreCase("OBC") || religionSection.equalsIgnoreCase("OTHERS")){
					if(examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId()!=null && examStudentTokenRegisterdForm.getStudentObj().getAdmAppln().getPersonalData().getIncomeByFatherIncomeId().getId()==1)
					{
						examStudentTokenRegisterdForm.setRegExamFeesExempted(true);
						examStudentTokenRegisterdForm.setApplicationAmount("Fees Exempted");
						examStudentTokenRegisterdForm.setApplicationAmount1("Fees Exempted");	
						examStudentTokenRegisterdForm.setApplicationAmountWords("Fees Exempted");
						examStudentTokenRegisterdForm.setSupSubjectList(subjectList);
						// set journalNo for payment through challan
						examStudentTokenRegisterdForm.setJournalNo("Fees Exempted");
					}
					else{
						examStudentTokenRegisterdForm.setRegExamFeesExempted(false);
						// set journalNo for payment through challan
						if(examStudentTokenRegisterdForm.getStudentObj().getRegisterNo()!=null){
							examStudentTokenRegisterdForm.setJournalNo("RG"+examStudentTokenRegisterdForm.getStudentObj().getRegisterNo()+examStudentTokenRegisterdForm.getExamId());
						}
						if(regExamFeesBo!=null){
							examStudentTokenRegisterdForm.setApplicationAmount(totalfee+"");
							examStudentTokenRegisterdForm.setApplicationAmount1(totalfee+"");	
							examStudentTokenRegisterdForm.setApplicationAmountWords(totalfeeinWords);
							examStudentTokenRegisterdForm.setSupSubjectList(subjectList);
						}
					}
				}else{
					examStudentTokenRegisterdForm.setSupSubjectList(subjectList);
				}
			}
						
		examStudentTokenRegisterdForm.setSupSubjectList(subjectList);
		examStudentTokenRegisterdForm.setExamType("Regular".toUpperCase());
		
		//raghu for attenance
		DisciplinaryDetailsImpl impl = new DisciplinaryDetailsImpl();
		int classId = examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getId();
/*		List studateList=impl.getDateList(examStudentTokenRegisterdForm.getStudentObj().getId(), classId);
		List clsdateList=impl.getDateList(0, classId);
		List sessionAttendanceList=impl.getSessionAttendanceList(examStudentTokenRegisterdForm.getStudentObj().getId(), classId);
		float percentage=NewSupplementaryImpApplicationHelper.getInstance().getAttendancePercentage(studateList, sessionAttendanceList, clsdateList);
*/		
		// Vinodha  attendance filter for reg app start date
			if(!transaction.isEligibleWithoutAttendanceCheck(studentId,examId)){
			List<StudentWiseSubjectSummaryTO> summaryList = StudentAttendanceSummaryHandler.getInstance().getAttendancePercentageForRegularApp(examStudentTokenRegisterdForm.getStudentObj());
			
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
			String programType = examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getName(); 
			if(programType.equalsIgnoreCase("ug"))
			{
				float hoursHeld = Float.parseFloat(CMSConstants.ATT_HOURSHELD_PERDAY_UG);
				float days = Float.parseFloat(CMSConstants.ATT_WORKINGDAYS_UG);
				float totalHoursHeld = hoursHeld*days;
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
		
		boolean ava=checkAttendanceAvailability(examStudentTokenRegisterdForm.getStudentObj().getId(),examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getId());
		String reqPercentage=CMSConstants.ATTENDANCE_PERCENTAGE_FOR_APP;
		if(ava){
		String classes = examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getName();
		if(!CMSConstants.ATT_EXCLUDED_EXAMREG.contains(classes)){
		//if(programType.equalsIgnoreCase("ug")){
			if(totalAggrPer<Float.parseFloat(reqPercentage)){
				boolean ava1=checkCondonationAvailability(examStudentTokenRegisterdForm.getStudentObj().getId(),examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getId());
				if(!ava1){
					examStudentTokenRegisterdForm.setIsAttendanceShortage(true);
				}																									
			}
		//}
		}
		}
			//getting instructions
			String programTypeId=	NewSecuredMarksEntryHandler.getInstance().getPropertyValue(examStudentTokenRegisterdForm.getStudentObj().getClassSchemewise().getClasses().getId(),"Classes",true,"course.program.programType.id");
			IDownloadHallTicketTransaction txn= new DownloadHallTicketTransactionImpl();
			List<ExamFooterAgreementBO> footer=txn.getFooterDetails(programTypeId,"E",classId+"");
			if(footer!=null && !footer.isEmpty()){
				ExamFooterAgreementBO obj=footer.get(0);
				if(obj.getDescription()!=null)
					examStudentTokenRegisterdForm.setDescription(obj.getDescription());
			}else{
				examStudentTokenRegisterdForm.setDescription(null);
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

}

