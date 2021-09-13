package com.kp.cms.handlers.exam;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admin.EligibilityCriteria;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.exam.ExamFalseNumberForm;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.attendance.ClassEntryHandler;
import com.kp.cms.helpers.admin.EligibilityCriteriaHelper;
import com.kp.cms.helpers.exam.ExamFalseNumberHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamDefinitionTO;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;

public class ExamFalseNumberHandler {
	private static ExamFalseNumberHandler marksCardHandler=null;
	public static ExamFalseNumberHandler getInstance(){
		if(marksCardHandler==null){
			marksCardHandler = new ExamFalseNumberHandler();
		}
		return marksCardHandler;
	}
	
	IExamFalseNumberTransaction transaction = new ExamFalseNumberTransactionImpl();
	
	/*
	
	public void getcourseansScheme(ExamFalseNumberForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		transaction.getcourseansScheme(marksCardForm);
		
	}*/
	public boolean duplicatecheck(ExamFalseNumberForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		boolean isduplicated=false;
		List<ExamFalseNumberGen> list=transaction.getfalsenos(marksCardForm);
		if(list!=null && !list.isEmpty()){
			isduplicated=true;
		}
		return isduplicated;
	}

	public boolean saveFalseNumbers(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		// TODO Auto-generated method stub
		boolean isadded = ExamFalseNumberHelper.getInstance().convertTotoBo(newExamMarksEntryForm);
		return isadded;
	}
	


	
}
