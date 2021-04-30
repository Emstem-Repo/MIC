package com.kp.cms.transactions.admission;

import java.util.List;

import com.kp.cms.bo.admin.CharacterAndConduct;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CurriculumSchemeDuration;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.forms.admission.TCDetailsForm;

public interface ITCDetailsTransaction {

	List<Student> getStudentDetails(String query) throws Exception;

	Student getStudentTCDetails(String query) throws Exception;

	public boolean saveStudentTCDetails(StudentTCDetails bo, TCNumber tcNumber, int currentAcademicYear, String userId) throws Exception;

	List<CharacterAndConduct> getAllCharacterAndConduct() throws Exception;

	Boolean updateStudentTCDetails(TCDetailsForm tcDetailsForm) throws Exception;
	
	public List<Object[]> getExamNames(TCDetailsForm tcDetailsForm);

	ExamDefinitionBO getExamForAllStudentsByClass(TCDetailsForm tcDetailsForm);

	Classes getClasses(TCDetailsForm tcDetailsForm);
	
	public List<Object[]> getSubjectsForAllStudentsByClass(TCDetailsForm tcDetailsForm)throws Exception;

	List<CurriculumSchemeDuration> getCurriculumSchemeDuration(String string,
			int studentId) throws Exception;

	List<Subject> getStudentSubjects(int applnId) throws Exception;
	
	ExamDefinitionBO getStudentExamName(int studentId, CurriculumSchemeDuration csd)
	throws Exception;
	
	public Boolean updateStudentTCNo(Student student)throws Exception ;
}
