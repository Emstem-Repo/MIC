package com.kp.cms.handlers.exam;

import java.util.List;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ConsolidateMarksCardNoGen;
import com.kp.cms.bo.exam.MarksCardNoGen;
import com.kp.cms.bo.exam.MarksCardSiNoGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.exam.ConsolidateMarksCardForm;
import com.kp.cms.forms.exam.MarksCardForm;
import com.kp.cms.helpers.exam.MarksCardHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.transactions.exam.IMarksCardTransaction;
import com.kp.cms.transactionsimpl.exam.MarksCardTransactionImpl;

public class MarksCardHandler {
	private static MarksCardHandler marksCardHandler=null;
	public static MarksCardHandler getInstance(){
		if(marksCardHandler==null){
			marksCardHandler = new MarksCardHandler();
		}
		return marksCardHandler;
	}
	IMarksCardTransaction transaction = new MarksCardTransactionImpl();
	
	/**
	 * @param marksCardForm
	 * @return
	 * @throws Exception
	 */
	public int getStudentCount(MarksCardForm marksCardForm) throws Exception{
		// TODO Auto-generated method stub
		
		return transaction.getStudentCount(marksCardForm);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public int getCurrentNO(MarksCardForm marksCardForm) throws Exception{
		// TODO Auto-generated method stub
		return transaction.getCurrentNO(marksCardForm);
	}

	/**
	 * @param totalCount
	 * @return
	 * @throws Exception
	 */
	public boolean update(String totalCount,MarksCardForm marksCardForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.getUpdateSI(totalCount,marksCardForm);
	}

	/**
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public int getAcademicId(String year)throws Exception {
		// TODO Auto-generated method stub
		return transaction.getAcademicId(year);
	}

	/**
	 * @param marksCardForm
	 * @param currentNO
	 * @return
	 * @throws Exception
	 */
	public boolean updateStudent(MarksCardForm marksCardForm,int currentNO) throws Exception{
		// TODO Auto-generated method stub
		List<Integer> studentId = transaction.getStudentId(marksCardForm);
		if(studentId.isEmpty())
		{
			throw new DataNotFoundException();
		}
		//List<MarksCardNoGen> boList = MarksCardHelper.getInstance().convertData(studentId,marksCardForm,currentNO);
		List<MarksCardSiNoGen> boList = MarksCardHelper.getInstance().convertData1(studentId,marksCardForm,currentNO);
		return transaction.updateStudent1(boList);
	}

	/**
	 * @param marksCardForm
	 * @return
	 * @throws Exception
	 */
	public boolean checkRegNo(MarksCardForm marksCardForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.checkRegNo(marksCardForm);
	}

	/**
	 * @param marksCardForm
	 * @param currentNO
	 * @throws Exception
	 */
	public void updateSingleStudent(MarksCardForm marksCardForm, int currentNO) throws Exception{
		// TODO Auto-generated method stub
		
		
		MarksCardNoGen bo = MarksCardHelper.getInstance().convertSingleStudentFormtoTo(marksCardForm,currentNO);
		transaction.updateSingleStudent(bo);
	}

	public boolean updateBalanceStudent(List<Integer> newStudentId,
			MarksCardForm marksCardForm, int currentNO)throws Exception {
		// TODO Auto-generated method stub
		List<MarksCardNoGen> boList = MarksCardHelper.getInstance().convertData(newStudentId,marksCardForm,currentNO);
		return transaction.updateStudent(boList);
	}

	public boolean checkDuplicate(MarksCardForm marksCardForm,
			List<Integer> studentId) throws Exception {
		MarksCardNoGen  bo=transaction.getMarksCardNoGen(marksCardForm,studentId);
		if(bo==null){
			return true;
		}else{
			return false;
		}
	}
	
	public int getConsolidateCurrentNO() throws Exception{
		// TODO Auto-generated method stub
		return transaction.getConsolidateCurrentNO();
	}
	
	
	public boolean consolidateupdateStudent(ConsolidateMarksCardForm consolidateMarksCardForm,int currentNO) throws Exception{
		// TODO Auto-generated method stub
		List<Integer> studentId = transaction.getStudentDetails(consolidateMarksCardForm);
		if(studentId.isEmpty())
		{
			throw new DataNotFoundException();
		}
		List<ConsolidateMarksCardNoGen> boList = MarksCardHelper.getInstance().convertDataDetails(studentId,consolidateMarksCardForm,currentNO);
		return transaction.updateConsolidate(boList);
	}
	
	
	public boolean updatecount(String totalCount)throws Exception {
		// TODO Auto-generated method stub
		return transaction.getUpdate(totalCount);
	}

	/**
	 * @param marksCardForm
	 * @return
	 * @throws Exception
	 */
	public boolean validateGenerateNoForExamType(MarksCardForm marksCardForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.validateGenerateNoForExamType(marksCardForm);
	}
	
	
	public List<StudentTO> getStudentIdForExamType(MarksCardForm marksCardForm)throws Exception {
		List<StudentFinalMarkDetails> studentList=transaction.getStudentIdForExamType(marksCardForm);
		List<StudentTO> listTo=MarksCardHelper.getInstance().convertStudentBo(studentList);
		return listTo;
	}

	public boolean updateSingleStudentDuplicate(MarksCardForm marksCardForm,List<MarksCardSiNoGen> origbo,List<MarksCardSiNoGen> dupbo ) throws Exception{
		// TODO Auto-generated method stub
		
		
		MarksCardSiNoGen bo = MarksCardHelper.getInstance().convertData2(marksCardForm, origbo, dupbo);
		boolean update=transaction.updateSingleStudentDuplicate(bo);
		return update;
	}
	

//	public boolean checkStudentClass(MarksCardForm marksCardForm,
		//	List<Integer> studentId) {
		// TODO Auto-generated method stub
	//	return false;
	//}

}
