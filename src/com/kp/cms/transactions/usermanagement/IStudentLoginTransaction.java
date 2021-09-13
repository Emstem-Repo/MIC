package com.kp.cms.transactions.usermanagement;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.AttendanceCondonation;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.CandidatePGIDetailsForStuSemesterFees;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.EvaStudentFeedbackOpenConnection;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.CandidatePGIForSpecialFees;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.bo.sap.UploadSAPMarksBo;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;

public interface IStudentLoginTransaction {

	public boolean saveMobileNo(PersonalData personalData)throws Exception;

	public PersonalData getStudentPersonalData(int personalId)throws Exception;

	public List<FeePayment> getStudentPaymentMode(int id) throws Exception;

	public FeePayment getFeePaymentDetailsForEdit(int parseInt,int financialYear) throws Exception;

	public EvaStudentFeedbackOpenConnection isFacultyFeedbackAvailable(int id, Map<Integer,String> specializationIds)throws Exception;

	public Map<Integer,String> getSpecializationIds(int studentId)throws Exception;

	public boolean checkHonoursCourse(int studentId, int courseId) throws Exception;

	public boolean checkMandatoryCertificateCourse(AdmAppln admAppln)throws Exception;
	
	public boolean checkingStudentIsAppliedForSAPExam(int studentId)throws Exception;
	
	public UploadSAPMarksBo getSAPExamResults(String studentId)throws Exception;

	public List<Integer> isAttendanceCondened(String studentId) throws Exception;
	
	
	public List<StudentInstructions> getInstructions()throws Exception;
	public List<StudentGroup> getStudentGroupDetails(LoginForm loginForm) throws Exception;
	public List<StudentExtention> getStudentExtentionDetails(LoginForm form) throws Exception;

	public String generateCandidateRefNo(CandidatePGIDetailsForStuSemesterFees bo) throws Exception;

	public boolean updateReceivedStatus(CandidatePGIDetailsForStuSemesterFees bo, LoginForm loginForm) throws Exception;

	public boolean paymentDone(LoginForm loginForm) throws Exception;

	public Student getStudentObj(String studentId) throws Exception;

	public List<CandidatePGIDetailsExamRegular> getRegList(String studentId) throws Exception;

	public List<CandidatePGIDetailsExamSupply> getSuppList(String studentId) throws Exception;

	public List<RevaluationApplicationPGIDetails> getRevList(String studentId) throws Exception;

	public CourseToDepartment getDept(String courseId) throws Exception;

	public List<ExamRegularApplication> getRegPaymentList(String studentId) throws Exception;

	public List<ExamSupplementaryImprovementApplicationBO> getSupPaymentList(String studentId) throws Exception;

	public List<PublishSpecialFees> getPublishClassList(String classId) throws Exception;

	public SpecialFeesBO getData(String classId) throws Exception;

	public String generateCandidateRefNoForSpecial(CandidatePGIForSpecialFees bo) throws Exception;

	public boolean updateStatus(CandidatePGIForSpecialFees bo,LoginForm loginForm) throws Exception;

	public boolean paymentDoneForSpecial(LoginForm loginForm) throws Exception;
}
