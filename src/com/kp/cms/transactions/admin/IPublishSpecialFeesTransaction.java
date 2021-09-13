package com.kp.cms.transactions.admin;

import java.util.List;

import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.forms.admin.PublishSpecialFeesForm;

public interface IPublishSpecialFeesTransaction {

	List<PublishSpecialFees> getList(PublishSpecialFeesForm publishSpecialFeesForm) throws Exception;

	String isDuplicate(int id, int year, int classID) throws Exception;

	int insert(PublishSpecialFees publishSpecialFees) throws Exception;

	void delete(int id) throws Exception;

	String isDuplicateForUpdate(int id, int year, int classID) throws Exception;

	int insertForUpdate(PublishSpecialFees publishStudentSemesterFees) throws Exception;

	PublishSpecialFees getPublishSpecialFees(String id) throws Exception;

}
