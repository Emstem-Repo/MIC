package com.kp.cms.transactions.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.Student;

public interface ISendBulkSmsToStudentParentsNew {
	public void sendBulkSms();
	public List<Student> getStudentByClass(int classIds) throws Exception;
	public List<Integer> getDetainedOrDiscontinuedStudents() throws Exception;
	public List<Employee> getTeachersByDepartment(Set<Integer> deptIds) throws Exception;
	public List<Employee> getNonTeachersByDepartment() throws Exception;
	public List<Integer> getStudentIds() throws Exception;

}
