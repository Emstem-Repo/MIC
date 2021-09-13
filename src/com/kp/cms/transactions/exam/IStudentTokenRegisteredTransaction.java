package com.kp.cms.transactions.exam;

import java.util.List;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.forms.exam.ExamStudentTokenRegisterdForm;

public interface IStudentTokenRegisteredTransaction {
	ExamRegularApplication getData(String studentId) throws Exception;
	Integer getStudentId(String registrationNumber) throws Exception;
	public ExamRegularApplication hasStudentAppliedForExam(int examId, int studentId) throws Exception;
	int getClassId(String string,int studentId) throws Exception;
	boolean addRegularAppData(ExamRegularApplication examRegularApplication) throws Exception;
	boolean updateRegularAppData(ExamRegularApplication examRegularApplication) throws Exception;
	public List<StudentSupplementaryImprovementApplication> hasStudentAppearedForExam(int examId,int studentId) throws Exception ;
	//public boolean saveSupplementaryTokenRegistration(List<StudentSupplementaryImprovementApplication> tokenRegistrations) throws Exception;
	public List<Object[]> getTotalSubjectList(int studentId,String string)throws Exception;
	int getSuppClassId(String examId, int studentId) throws Exception;
	/*public CandidatePGIDetailsExamRegular checkOnlinePaymentReg(ExamStudentTokenRegisterdForm admForm)throws Exception;
	public boolean updateReceivedStatusReg(CandidatePGIDetailsExamRegular bo,ExamStudentTokenRegisterdForm admForm) throws Exception;*/
	public boolean isRegisterNoValid(int examId, int courseId) throws Exception;
	public List<Integer> getAllClassIdsStudent(int studentId) throws Exception;
	public RegularExamFees getRegularExamFees(ExamStudentTokenRegisterdForm form)throws Exception;
}

