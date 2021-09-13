package com.kp.cms.transactions.admission;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.forms.admission.TransferCertificateForm;

public interface ITransferCertificateTransaction 
{

	public List<Student> getStudentList(TransferCertificateForm form) throws Exception;

	public List<TCNumber> getTCNumber(Integer year, String tcFor, String courseId)throws Exception;

	public void updateStudentsTcNo(List<Student> studentsTakenTcList)throws Exception;

	public String getOldClassNameByStudentId(int studentId)throws Exception;
	
	public String getOldClassNameByStudentIdNew(int studentId, int Sem)throws Exception;

	public List<Student> getStudentsByRegNo(String reg, String toReg)throws Exception;
	
	public List<Student> getStudentListForReprint(String classId,String fromReg, String toReg,String includeFail, String string)throws Exception;

	public List<Student> getStudentListForCjc(String classes, String fromUsn,String toUsn, String tcType, String includeFail)throws Exception;

	public TCNumber getOnlyTCNumber(int parseInt, String tcFor,boolean isTC);

	public List<Student> getStudentListByInputQuery(String query) throws Exception;
	public Integer getAdmittedSemester(int studentId)throws Exception;
	public String getCourseSchemeId(int courseId, Integer appliedYear)throws Exception;
	public BigInteger getRejoinYear(int studentId)throws Exception;
	public List<Integer> getDiscontinuedStudentId()throws Exception;
	public String getStudentSubject(Set<ApplicantSubjectGroup> subjectGroup,int courseId,int academicYear)throws Exception;
	public int getClassTermNumber(int classId)throws Exception;

	public Classes getAdmittedClassesDetails(int id) throws Exception;

	public List getStudentMarks(String marksEntryQuery,List<Integer> studentIds) throws Exception ;
}
