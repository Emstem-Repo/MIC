package com.kp.cms.transactions.admin;

import java.util.List;

import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.forms.admin.PublishStudentSemesterFeesForm;

public interface IPublishStudentSemesterFeesTransaction {

	List<PublishStudentSemesterFees> getList(PublishStudentSemesterFeesForm studentSemesterFeesForm) throws Exception;

	String isDuplicate(int id, int year, int classID) throws Exception;

	int insert(PublishStudentSemesterFees publishStudentSemesterFees) throws Exception;

	void delete(int id) throws Exception;

	String isDuplicateForUpdate(int id, int year, int classID) throws Exception;

	int insertForUpdate(PublishStudentSemesterFees publishStudentSemesterFees) throws Exception;

	PublishStudentSemesterFees getPublishOptionalCourseSubjectApplication(String id) throws Exception;

}
