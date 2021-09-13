package com.kp.cms.transactions.exam;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ConsolidateMarksCardNoGen;
import com.kp.cms.bo.exam.MarksCardNoGen;
import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.bo.exam.MarksCardSiNoGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.forms.exam.ConsolidateMarksCardForm;
import com.kp.cms.forms.exam.MarksCardForm;

public interface IMarksCardTransaction {

	int getStudentCount(MarksCardForm marksCardForm) throws Exception;

	int getCurrentNO(MarksCardForm marksCardForm)throws Exception;

	boolean getUpdateSI(String totalCount,MarksCardForm marksCardForm) throws Exception;

	int getAcademicId(String year)throws Exception;

	List<Integer> getStudentId(MarksCardForm marksCardForm)throws Exception;

	boolean updateStudent(List<MarksCardNoGen> boList)throws Exception;

	Map<Integer, String> getStudentList(MarksCardForm marksCardForm)throws Exception;

	boolean checkRegNo(MarksCardForm marksCardForm)throws Exception;

	void updateSingleStudent(MarksCardNoGen bo)throws Exception;

	Student getStudentBoDetails(MarksCardForm marksCardForm)throws Exception;

	MarksCardNoGen getMarksCardNoGen(MarksCardForm marksCardForm,List<Integer> studentId)throws Exception;
	
	public int getConsolidateCurrentNO()throws Exception;
	public List<Integer> getStudentDetails(ConsolidateMarksCardForm consolidateMarksCardForm)throws Exception;
	public Map<Integer, String> getStudentDetailList(ConsolidateMarksCardForm consolidateMarksCardForm)throws Exception;
	public boolean updateConsolidate(List<ConsolidateMarksCardNoGen> boList)throws Exception;
	public boolean getUpdate(String totalCount) throws Exception;
	
	boolean updateStudent1(List<MarksCardSiNoGen> boList)throws Exception;
	List<StudentFinalMarkDetails> getStudentIdForExamType(MarksCardForm marksCardForm)throws Exception;
	public boolean validateGenerateNoForExamType(MarksCardForm marksCardForm)throws Exception;
	Map<Integer, MarksCardSiNoGen> getStudentList1(MarksCardForm marksCardForm)throws Exception;
	boolean updateStudentForExamType(MarksCardForm marksCardForm)throws Exception;
	public List<MarksCardSiNoGen> getDataAvailable(MarksCardForm marksCardForm)throws Exception;
	public boolean updateSingleStudentDuplicate(MarksCardSiNoGen bo)throws Exception;
}
