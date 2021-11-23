package com.kp.cms.transactions.admission;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.CertificateCourse;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.SubjectGroup;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.forms.attendance.SubjectGroupDetailsForm;

public interface IAddOnCourseDetailsTransaction {
public List<Student> getStudentDetails(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception;
public List<ApplicantSubjectGroup> getSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception;
public boolean addSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm)throws Exception;
public Map<Integer, Integer> getEditSubjectGroup(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception;
public List<CertificateCourse> getSubjectGroupNames(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception;
public boolean updateSubjectGroups(
		SubjectGroupDetailsForm subjectGroupDetailsForm)throws Exception;
public  Map<Integer, String> getsubjectGroupsId(SubjectGroupDetailsForm subjectGroupDetailsForm)throws Exception;
public List<ExamSpecializationBO> getSpecializationData(int classSchemeWiseId)throws Exception;
public Map<Integer, String> getstudentWiseSpecialization( List<Student> studentList)throws Exception;
}
