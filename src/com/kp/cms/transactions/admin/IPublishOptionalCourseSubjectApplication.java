package com.kp.cms.transactions.admin;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.PublishOptionalCourseSubjectApplication;

public interface IPublishOptionalCourseSubjectApplication
{
	public List<PublishOptionalCourseSubjectApplication> getPublishOptionalCourseSubjects()throws Exception;
	public PublishOptionalCourseSubjectApplication getPublishOptionalCourseSubjectApplication(String id) throws Exception;
	public Map<Integer, String> getClassMap(String[] classId)throws Exception;
	public String isDuplicate(int id,int year, int classId)throws Exception;
	public int insert(Object obj)throws Exception;
	public void delete(int id) throws Exception;
	public String isDuplicateForUpdate(int id, int year, int classId) throws Exception;

public int insertForUpdate(Object obj) throws Exception ;


}
