package com.kp.cms.transactions.studentExtentionActivity;

import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectStream;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionActivityDetails;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.exam.ConsolidatedSubjectStreamForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;

public interface IStudentExtentionTransaction {
   
	public List<StudentExtention> getStudentExtentionDetails() throws Exception;
	public StudentExtention isDuplicate(StudentExtention oldStudentExtention) throws Exception;
	public boolean addStudentExtention(StudentExtention studentExtention, String mode) throws Exception;
	public boolean deleteStudentExtention(int dupId, boolean activate, StudentExtentionForm studentExtentionForm) throws Exception;
	public List<StudentGroup> getStudentGroupDetails() throws Exception;
	public boolean hasStudentAlreadySubmitedActivities(int studentId) throws Exception;
	public boolean saveStudentExtensionActivityDetails(List<StudentExtentionActivityDetails> activityDetails) throws Exception;
}
