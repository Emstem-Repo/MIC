package com.kp.cms.transactions.admission;

import java.util.List;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;

public interface IConductCertificateTransaction {
	public List<StudentTCDetails> getStudentList(String classId,String fromReg, String toReg,String studentId)throws Exception;
	public List<StudentTCDetails> getStudentsByName(String name) throws Exception;
	public int getClassTermNumber(int classId) throws Exception;
}
