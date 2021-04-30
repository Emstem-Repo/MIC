package com.kp.cms.transactions.admission;

import java.util.List;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePreferenceEntranceDetails;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.forms.admission.ApplicationEditForm;
import com.kp.cms.forms.admission.DDStatusForm;

public interface IDDStatusTransaction {

	boolean getAlreadyEntered(String query) throws Exception;

	boolean checkStudent(String query) throws Exception;

	AdmAppln updateStatus(String alreadyEnteredQuery,DDStatusForm dDStatusForm) throws Exception;

	boolean getAlreadyEntered1(String query) throws Exception;

	AdmAppln updateStatus1(String query, DDStatusForm dDStatusForm)	throws Exception;

	boolean updateChallanStatusOnCourse(DDStatusForm dDStatusForm)	throws Exception;
	
	List<AdmAppln> getStudentsChallanStatusOnCourse(DDStatusForm dDStatusForm) throws Exception;
	
	boolean updateDDStatusOnCourse(DDStatusForm dDStatusForm)	throws Exception;
	
	List<AdmAppln> getStudentsDDStatusOnCourse(DDStatusForm dDStatusForm) throws Exception;
	
	List<AdmAppln> getStudentsChallanDtailsOnDate(DDStatusForm dDStatusForm) throws Exception;
	
	boolean updateChallanUploadProcess(DDStatusForm dDStatusForm)	throws Exception;

	List<ExamRegularApplication> getStudentsChallanStatusOnCourseForExam(DDStatusForm ddForm) throws Exception;
	
	boolean updateChallanStatusOnCourseForExam(DDStatusForm ddForm)	throws Exception;
	
	public List<ExamRegularApplication> getStudentsChallanDtailsOnDateForExam(DDStatusForm ddForm) throws Exception;
	
	public boolean updateChallanUploadProcessForExam(DDStatusForm ddForm)	throws Exception;

	Integer ChallanNotVerifiedCount(DDStatusForm ddform) throws Exception;
	
	Integer ChallanVerifiedCount(DDStatusForm ddform) throws Exception;
	
	public List<ExamSupplementaryImprovementApplicationBO> getStudentsChallanStatusOnCourseForSupplExam(DDStatusForm ddForm) throws Exception ;
	
	public boolean updateChallanStatusOnCourseForSupplExam(DDStatusForm ddForm)	throws Exception;
	
	public List<ExamSupplementaryApplication> getStudentsChallanDtailsOnDateForSupplExam(DDStatusForm ddForm) throws Exception;

	public boolean updateChallanUploadProcessForSupplExam(DDStatusForm ddForm)	throws Exception;
	
	public Student getStudent(int studId) throws Exception;
}
