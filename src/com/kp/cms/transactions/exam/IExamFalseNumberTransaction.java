package com.kp.cms.transactions.exam;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.forms.exam.ConsolidatedMarksCardForm;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.to.admin.StudentTO;


public interface IExamFalseNumberTransaction {
	
	List<Student> getQueryForCurrentClass(NewExamMarksEntryForm marksCardForm)throws Exception;
	//List<StudentTO> getQueryForPreviousClass(MarksCardForm marksCardForm)throws Exception;
	void getcourseansScheme(NewExamMarksEntryForm marksCardForm)throws Exception;
	boolean savedata(List<ExamFalseNumberGen> bo)throws Exception;
	//public Boolean isCurrentClassDuplicated(int classId, int studentId,int examId)  throws Exception;
	List<ExamFalseNumberGen> getfalsenos(NewExamMarksEntryForm marksCardForm) throws Exception;
	public String getMaxFalseNo(NewExamMarksEntryForm marksCardForm) throws Exception;
	public boolean updateFalseSiNo(NewExamMarksEntryForm form) throws Exception;
	public boolean DuplicateFalseNo(NewExamMarksEntryForm marksCardForm,String randomNo) throws Exception;
	Object getData(NewExamMarksEntryForm marksCardForm,String quer) throws Exception;
}
