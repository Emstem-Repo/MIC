package com.kp.cms.transactions.usermanagement;

import java.util.List;

import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.usermanagement.OptionalCourseApplication;
import com.kp.cms.forms.usermanagement.OptionalCourseApplicationForm;

public interface IOptionalCourseSubjectApplication {

		List<Subject> getOptionalCourseSubjects(OptionalCourseApplicationForm form) throws Exception;
		boolean saveApplication(List<OptionalCourseApplication> boList)throws Exception;
		List<OptionalCourseApplication> getExistingOptionalCourseSubjects(int stuId, int classId,int courseId)throws Exception;
		public Department getDepartment(int courseId)throws Exception;
		public Student getStudentDetails(int stuId)throws Exception;
		boolean checkDuplicates(int stuId,int classId)throws Exception;
}
