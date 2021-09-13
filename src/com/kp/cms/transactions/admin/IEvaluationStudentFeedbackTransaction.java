package com.kp.cms.transactions.admin;

import java.util.List;

import com.kp.cms.bo.admin.EvaluationStudentFeedback;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackFaculty;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentFeedbackInstructions;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.bo.admin.TeacherClassSubject;
import com.kp.cms.bo.studentfeedback.EvaStudentFeedbackQuestion;
import com.kp.cms.forms.admin.EvaluationStudentFeedbackForm;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;
import com.kp.cms.to.admin.EvaluationStudentFeedbackTO;
import com.kp.cms.to.studentfeedback.EvaStudentFeedBackGroupTo;

public interface IEvaluationStudentFeedbackTransaction {


	public StudentLogin getStudentLoginDetails(String userId)throws Exception;

	public List<Integer> getSubjectIds(int admApplnId)throws Exception;

	public List<TeacherClassSubject> getTeacherClassSubjects(List<Integer> subjectIds, String classSchemeWiseId)throws Exception;

	public List<EvaStudentFeedbackQuestion> getfacultyEvalQuestionList()throws Exception;

	public boolean saveFacultyEvaluationFeedback( EvaluationStudentFeedback facultyEvaluationFeedback)throws Exception;

	public boolean checkStuIsAlreadyExist( EvaluationStudentFeedbackForm evaStudentFeedbackForm)throws Exception;

	public List<EvaStudentFeedBackGroupTo> getGroupDetailsList( EvaluationStudentFeedbackForm evaStudentFeedbackForm)throws Exception;

	public String getBatchId(int studentId, int classId, int subjectId)throws Exception;

	public String getAttendancePercentage(String attendanceQuery)throws Exception;
	
	public List<Integer> getClasses(List<EvaStudentFeedbackOpenConnectionTo> toList,EvaluationStudentFeedbackForm evaStudentFeedbackForm) throws Exception;
	
	public List<Integer> getSubjectIdsByClassId(int admApplnId,EvaluationStudentFeedbackForm evaStudentFeedbackForm) throws Exception;
	
	public List<Integer> getClassesNew(List<EvaStudentFeedbackOpenConnectionTo> toList,int id) throws Exception;

	public List<Integer> allClassIdsOfStud(int studentId) throws Exception;

}
