package com.kp.cms.transactions.studentExtentionActivity;

import java.util.List;

import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;
import com.kp.cms.forms.studentExtentionActivity.StudentGroupForm;

public interface IStudentGroupTransaction {

	 public List<StudentGroup> getStudentGroupDetails() throws Exception;
	 public boolean addStudenGroup(StudentGroup studentGroup, String mode) throws Exception;
	 public StudentGroup isDuplicate(StudentGroup oldStudentGroup)throws Exception;
	 public boolean deleteStudentGroup(int dupId, boolean activate, StudentGroupForm studentGroupForm) throws Exception;
}
