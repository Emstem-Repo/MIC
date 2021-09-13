package com.kp.cms.transactions.studentExtentionActivity;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.EvaStudentFeedbackOpenConnection;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackSession;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.admin.EvaStudentFeedbackOpenConnectionForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;

public interface IStudentExtentionFeedbackTransaction {
	public List<StudentExtentionFeedback> getFeedbackList(int year)throws Exception;
	public List<EvaluationStudentFeedbackSession> getSessionDetails(Integer year) throws Exception;
	
	public List<StudentExtentionFeedback> getConnectionListBySessionId() throws Exception ;
	public List<StudentExtentionFeedback> getConnectionListBySessionIdNew()throws Exception;
	
	public boolean checkDuplicate(StudentExtentionFeedbackForm feedbackform)throws Exception;
	public String getSpecializationName(String specializationId)throws Exception;
	public Map<Integer, Integer> getSpecializationIds(String[] classesId, String specializationName)throws Exception;
	public boolean submitOpenConnectionDetails(List<StudentExtentionFeedback> boList)throws Exception;
	
	public StudentExtentionFeedback getFeedbackDetails(int id)throws Exception;
	public boolean deleteOpenConnection( StudentExtentionFeedbackForm feedbackform)throws Exception;
	public boolean updateOpenConnection( StudentExtentionFeedbackForm feedbackform)throws Exception;
	
	
	public List<StudentGroup> getStudentGroupDetails() throws Exception;
	public List<StudentExtention> getStudentExtentionDetails() throws Exception;
	public int getRecordId(String classId) throws Exception;
}
