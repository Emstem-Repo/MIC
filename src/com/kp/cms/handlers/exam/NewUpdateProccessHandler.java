package com.kp.cms.handlers.exam;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.forms.exam.NewUpdateProccessForm;
import com.kp.cms.helpers.exam.NewUpdateProccessHelper;
import com.kp.cms.to.attendance.ClassesTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewUpdateProccessTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewUpdateProccessTransactionImpl;

public class NewUpdateProccessHandler {
	
	private final int PROCESS_CALC_REGULAR_MARKS = 1;
	private final int PROCESS_CALC_INTERNAL_MARKS = 2;
	private final int PROCESS_SUPPLEMENTARY_DATA_CREATION = 3;
	private final int PROCESS_UPDATE_PASS_FAIL = 4;
	private final int PROCESS_REVALUATION_MODERATION = 5;
	private final int PROCESS_CALC_INTERNAL_RETEST_MARKS = 6;
	
	/**
	 * Singleton object of NewUpdateProccessHandler
	 */
	private static volatile NewUpdateProccessHandler newUpdateProccessHandler = null;
	private static final Log log = LogFactory.getLog(NewUpdateProccessHandler.class);
	private NewUpdateProccessHandler() {
		
	}
	/**
	 * return singleton object of NewUpdateProccessHandler.
	 * @return
	 */
	public static NewUpdateProccessHandler getInstance() {
		if (newUpdateProccessHandler == null) {
			newUpdateProccessHandler = new NewUpdateProccessHandler();
		}
		return newUpdateProccessHandler;
	}
	
	public Map<Integer, String> getBatchYear(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		String query=NewUpdateProccessHelper.getInstance().getQueryForBatchYear();
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		List<Integer> list=transaction.getDataByQuery(query);
		return NewUpdateProccessHelper.getInstance().convertBatchBoListToTOList(list);
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public List<ClassesTO> getClassesByExamAndExamType(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		String query=NewUpdateProccessHelper.getInstance().getqueryForSuppliementaryDataCreation(newUpdateProccessForm);
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		List list=transaction.getDataByQuery(query);
		return NewUpdateProccessHelper.getInstance().convertBoListToTOList(list);
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public boolean updateProcess(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		boolean isUpdated=false;
		switch (Integer.parseInt(newUpdateProccessForm.getProcess())) {
		case PROCESS_CALC_REGULAR_MARKS:
			isUpdated= calculateRegularOverAllMarks(newUpdateProccessForm);
			if(newUpdateProccessForm.getErrorList()==null || newUpdateProccessForm.getErrorList().isEmpty()){
				if(newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty())
					passOrFail(newUpdateProccessForm);
			}
				
			break;

		case PROCESS_CALC_INTERNAL_MARKS:
			isUpdated = calculateInternalOverAllMarks(newUpdateProccessForm);
			if(newUpdateProccessForm.getErrorList()==null || newUpdateProccessForm.getErrorList().isEmpty())
				passOrFail(newUpdateProccessForm);
			break;
			
			
		case PROCESS_SUPPLEMENTARY_DATA_CREATION:
			isUpdated = supplementaryDataCreation(newUpdateProccessForm);
			break;
		case PROCESS_UPDATE_PASS_FAIL: 
			 passOrFail(newUpdateProccessForm);
			 isUpdated=true;
			 break;
				// vinodha	 
		case PROCESS_REVALUATION_MODERATION:
			isUpdated= calculateRevaluationModeration(newUpdateProccessForm);
			 isUpdated=true;
			 if(newUpdateProccessForm.getErrorList()==null || newUpdateProccessForm.getErrorList().isEmpty()){
					if(newUpdateProccessForm.getErrorMessage() == null || newUpdateProccessForm.getErrorMessage().isEmpty())
						passOrFail(newUpdateProccessForm);
				}
			 break;	
		case PROCESS_CALC_INTERNAL_RETEST_MARKS:
			isUpdated = calculateInternalRetestOverAllMarks(newUpdateProccessForm);
			if(newUpdateProccessForm.getErrorList()==null || newUpdateProccessForm.getErrorList().isEmpty())
				passOrFail(newUpdateProccessForm);
			break;
		}
		return isUpdated;
	}
	
	
	/**
	 * @param newUpdateProccessForm
	 * @throws Exception
	 */
	private void passOrFail(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		NewUpdateProccessHelper.getInstance().updatePassOrFail(newUpdateProccessForm);
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	private boolean calculateRegularOverAllMarks(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		return NewUpdateProccessHelper.getInstance().calculateRegularOverAllAndSaveData(newUpdateProccessForm);
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	private boolean calculateInternalOverAllMarks(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		return NewUpdateProccessHelper.getInstance().calculateInternalOverAllAndSaveData(newUpdateProccessForm);
	}
	/**
	 * @param newUpdateProccessForm
	 * @return
	 * @throws Exception
	 */
	public boolean supplementaryDataCreation(NewUpdateProccessForm newUpdateProccessForm) throws Exception{
		INewUpdateProccessTransaction transaction=NewUpdateProccessTransactionImpl.getInstance();
		List<StudentSupplementaryImprovementApplication> boList=NewUpdateProccessHelper.getInstance().getBoListFromToList(newUpdateProccessForm);
		return transaction.saveUpdateProcess(boList);
	}
	/**
	 * @param classIds
	 * @param newUpdateProccessForm
	 * @return
	 */
	public List<String> getAlreadyModifiedStudents(String classIds, NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		String query="select s.student.registerNo from StudentSupplementaryImprovementApplication s" +
				" where s.examDefinition.id= " +newUpdateProccessForm.getExamId()+
				" and s.classes.id in ("+classIds+") and (s.isAppearedPractical=1 or s.isAppearedTheory=1) group by s.student.registerNo";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		return transaction.getDataForQuery(query);
	}
	
	private boolean calculateRevaluationModeration(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		return NewUpdateProccessHelper.getInstance().calculateRevaluationModerationAndSaveData(newUpdateProccessForm);
		
	}
	private boolean calculateInternalRetestOverAllMarks(NewUpdateProccessForm newUpdateProccessForm) throws Exception {
		return NewUpdateProccessHelper.getInstance().calculateInternalRetestOverAllAndSaveData(newUpdateProccessForm);
	}
}
