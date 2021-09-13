package com.kp.cms.transactions.exam;

import java.util.List;

import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ChallanUploadDataExam;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamRevaluationApp;
import com.kp.cms.bo.exam.ExamRevaluationApplication;
import com.kp.cms.bo.exam.ExamRevaluationApplicationNew;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.OnlinePaymentReciepts;
import com.kp.cms.bo.exam.RegularExamApplicationPGIDetails;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.SubjectUtilBO;
import com.kp.cms.bo.exam.SupplementaryExamApplicationPGIDetails;
import com.kp.cms.forms.admission.OnlineApplicationForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;

public interface INewSupplementaryImpApplicationTransaction {

	boolean saveSupplementarys( List<StudentSupplementaryImprovementApplication> boList) throws Exception;

	boolean deleteSupplementaryImpApp(String query) throws Exception;

	String getOldRegNo(int id, Integer termNumber) throws Exception;

	void updateAndGenerateRecieptNoOnlinePaymentReciept(OnlinePaymentReciepts onlinePaymentReciepts) throws Exception;

	List getSubjectsListForStudent(Student student,int acadenicYear) throws Exception;

	public boolean checkAttendanceAvailability(int studentId, int classId) throws Exception;
	
	public boolean checkCondonationAvailability(int studentId, int classId) throws Exception;
			
	public boolean checkDuplication(NewSupplementaryImpApplicationForm form) throws Exception;
				
	public boolean addAppliedStudent(ExamRegularApplication obj) throws Exception;

	RegularExamFees getRegularExamFees(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	String generateCandidateRefNo(RegularExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form) throws Exception;

	boolean updateReceivedStatus(RegularExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form) throws Exception;

	public boolean addAppliedStudentToChallan(ChallanUploadDataExam obj) throws Exception;
	
	public boolean checkSubmitSuppApp(NewSupplementaryImpApplicationForm form)throws Exception;

	public boolean checkDuplicationForSuppl(NewSupplementaryImpApplicationForm form) throws Exception;
	
	public boolean updateReceivedStatusForSuppl(SupplementaryExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form)
	throws Exception ;
	
	public boolean addAppliedStudentForSuppl(ExamSupplementaryApplication obj)throws Exception;
	
	String generateCandidateRefNoForSuppl(SupplementaryExamApplicationPGIDetails bo, NewSupplementaryImpApplicationForm form) throws Exception;

	public ExamSupplementaryApplication getApplicationForSuppl(NewSupplementaryImpApplicationForm form)throws Exception;

	public boolean getPrevClasssIdFromRegularApp(int stuId, int prevClassId)throws Exception;
	
	public List getSubjectsListForStudentPrevClass(Student student, int academicYear) throws Exception;
	
	public boolean addAppliedStudentsForReg(List<ExamRegularApplication> regappList,int  examId,int classId)throws Exception;

	public List getSupplSubjectsListForRevaluation(Student student,int examId,int classid)
	throws Exception;
	
	List getSubjectsListForRevaluation(Student student,int examId,int classid) throws Exception;
	
	public Boolean getExtendedDateForRevaluation(int classId, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	public ExamRevaluationApp checkDuplicationForRevaluation(NewSupplementaryImpApplicationForm form) throws Exception;

	boolean saveChallengeValuationApps(List<ExamRevaluationApplicationNew> boList) throws Exception;

	List getSubjectsListForRevaluationSupplementry(Student student,Integer examId, int revalSupplyClassId) throws Exception;

	CandidatePGIDetailsExamRegular checkOnlinePaymentReg(NewSupplementaryImpApplicationForm admForm)throws Exception;

	String generateCandidateRefNoReg(CandidatePGIDetailsExamRegular bo)throws Exception;

	boolean updateReceivedStatusReg(CandidatePGIDetailsExamRegular bo,NewSupplementaryImpApplicationForm admForm)throws Exception;

	boolean checkPaymentDetails(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	CandidatePGIDetailsExamSupply checkOnlinePaymentSuppl(NewSupplementaryImpApplicationForm admForm) throws Exception;

	String generateCandidateRefNoSuppl(CandidatePGIDetailsExamSupply bo)throws Exception;

	boolean updateReceivedStatusSupp(CandidatePGIDetailsExamSupply bo,NewSupplementaryImpApplicationForm admForm)throws Exception;

	String generateCandidateRefNoRevaluation(RevaluationApplicationPGIDetails bo) throws Exception;

	RevaluationApplicationPGIDetails checkOnlinePaymentRev(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	boolean updateReceivedStatusRev(RevaluationApplicationPGIDetails bo,NewSupplementaryImpApplicationForm admForm) throws Exception;

	public RevaluationApplicationPGIDetails getPgiDetails(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	List<RevaluationApplicationPGIDetails> getPendingList(NewSupplementaryImpApplicationForm admForm) throws Exception;

	boolean updateRev(NewSupplementaryImpApplicationForm admForm, List<ExamRevaluationApplicationNew> list) throws Exception;

	RevaluationApplicationPGIDetails getSuccessDetails(NewSupplementaryImpApplicationForm admForm) throws Exception;

	List<ExamRevaluationApplicationNew> getExistingData(NewSupplementaryImpApplicationForm admForm,
			RevaluationApplicationPGIDetails details) throws Exception;

	boolean updateReceivedStatusScr(RevaluationApplicationPGIDetails bo,
			NewSupplementaryImpApplicationForm admForm) throws Exception;

	List<ExamRevaluationApplicationNew> getExistingDataScr(
			NewSupplementaryImpApplicationForm admForm,
			RevaluationApplicationPGIDetails details) throws Exception;

	boolean updateScr(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list) throws Exception;

	String generateCandidateRefNoRevaluationSupply(RevaluationApplicationPGIDetails bo) throws Exception;

	List<RevaluationApplicationPGIDetails> getPendingListSupply(NewSupplementaryImpApplicationForm admForm) throws Exception;

	boolean updateReceivedStatusRevSupply(RevaluationApplicationPGIDetails bo,NewSupplementaryImpApplicationForm admForm) throws Exception;

	boolean updateReceivedStatusScrSupply(RevaluationApplicationPGIDetails bo,NewSupplementaryImpApplicationForm admForm) throws Exception;

	boolean updateRevSupply(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list5) throws Exception;

	boolean updateScrSupply(NewSupplementaryImpApplicationForm admForm,List<ExamRevaluationApplicationNew> list6) throws Exception;

	RevaluationApplicationPGIDetails getPgiDetailsSupply(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;

	
}
