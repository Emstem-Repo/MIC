package com.kp.cms.transactions.exam;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.forms.exam.ConsolidatedMarksCardForm;
import com.kp.cms.forms.exam.ExamFalseNumberForm;
import com.kp.cms.to.admin.StudentTO;


public interface IExamFalseNumberTransaction {
	
	List<Student> getQueryForCurrentClass(ExamFalseNumberForm marksCardForm)throws Exception;
	//List<StudentTO> getQueryForPreviousClass(MarksCardForm marksCardForm)throws Exception;
	void getcourseansScheme(ExamFalseNumberForm marksCardForm)throws Exception;
	boolean savedata(ExamFalseNumberGen bo)throws Exception;
	//public Boolean isCurrentClassDuplicated(int classId, int studentId,int examId)  throws Exception;
	List<ExamFalseNumberGen> getfalsenos(ExamFalseNumberForm marksCardForm) throws Exception;

}
