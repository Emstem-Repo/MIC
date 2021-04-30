package com.kp.cms.handlers.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.helpers.exam.NewExamMarksEntryHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.MarksDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;

public class NewExamMarksEntryHandler {
	/**
	 * Singleton object of newExamMarksEntryHandler
	 */
	private static volatile NewExamMarksEntryHandler newExamMarksEntryHandler = null;
	private static final Log log = LogFactory.getLog(NewExamMarksEntryHandler.class);
	private NewExamMarksEntryHandler() {
		
	}
	/**
	 * return singleton object of newExamMarksEntryHandler.
	 * @return
	 */
	public static NewExamMarksEntryHandler getInstance() {
		if (newExamMarksEntryHandler == null) {
			newExamMarksEntryHandler = new NewExamMarksEntryHandler();
		}
		return newExamMarksEntryHandler;
	}
	
	public static Map<Integer, String> subjectTypeMap = new HashMap<Integer, String>();//creating a static map for Regular or Internal ExamType
	public static Map<Integer, String> subjectTypeMapForSupplementary = new HashMap<Integer, String>();//creating a static map for Supplementary ExamType
	// setting the default values for maps
	static {
		subjectTypeMap.put(1, "Theory");
		subjectTypeMap.put(0, "Practical");
//		subjectTypeMap.put(11, "Theory and Practical");
		subjectTypeMapForSupplementary.put(1, "Theory");
		subjectTypeMapForSupplementary.put(0, "Practical");
	}
	/**
	 * checking whether marks Entered through secured for that exam and subject
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public boolean checkMarksEnteredThroughSecured(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		String query=NewExamMarksEntryHelper.getInstance().getQueryForCheckMarksEnteredThroughSecured(newExamMarksEntryForm);// getting the query from Helper class
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		boolean isEntered=false;// By default false value 
		List existList=transaction.getDataForQuery(query); // calling the method for checking data is there for the query
		if(existList!=null && !existList.isEmpty()){
			isEntered=true;// if existList is not empty that means marks entered through secured marks Entry screen
		}
		return isEntered;
	}
	/**
	 * getting students for input search
	 * @param newExamMarksEntryForm
	 * @return
	 */
	public Set<StudentMarksTO> getStudentForInput(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String marksQuery=NewExamMarksEntryHelper.getInstance().getQueryForAlreadyEnteredMarks(newExamMarksEntryForm);
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		List marksList=transaction.getDataForQuery(marksQuery);// calling the method for checking data is there for the marksQuery
		Map<Integer,MarksDetailsTO> existsMarks=new HashMap<Integer, MarksDetailsTO>();
		if(marksList!=null && !marksList.isEmpty()){
			existsMarks=NewExamMarksEntryHelper.getInstance().convertBoDataToMarksMap(marksList);// converting the database data to Required Map
		}
		
		Set<StudentMarksTO> studentList=new HashSet<StudentMarksTO>();
		if(!newExamMarksEntryForm.getExamType().equalsIgnoreCase("Supplementary")){
			List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
			String currentStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForCurrentClassStudents(newExamMarksEntryForm);
			List currentStudentList=transaction.getDataForQuery(currentStudentQuery);// calling the method for getting current class students
			if(currentStudentList!=null && !currentStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForCurrentStudents(studentList,currentStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForPreviousClassStudents(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForPreviousClassStudents(studentList,previousStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
		}else{// For Supplementary
			Map<Integer, String> oldRegMap=getOldRegisterNo(newExamMarksEntryForm.getSchemeNo());
			String currentStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForSupplementaryCurrentClassStudents(newExamMarksEntryForm);
			List currentStudentList=transaction.getDataForQuery(currentStudentQuery);// calling the method for getting Supplementary current class students
			if(currentStudentList!=null && !currentStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForSupplementaryCurrentStudents(studentList,currentStudentList,existsMarks,newExamMarksEntryForm,oldRegMap);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForSupplementaryPreviousClassStudents(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Supplementary Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForSupplementaryPreviousClassStudents(studentList,previousStudentList,existsMarks,newExamMarksEntryForm,oldRegMap);
			}
		}
		return studentList;
	}
	/**
	 * @param schemeNo
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, String> getOldRegisterNo(String schemeNo) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		String oldRegQuery=NewExamMarksEntryHelper.getInstance().getQueryForOldRegisterNos(schemeNo);
		List oldRegList=transaction.getDataForQuery(oldRegQuery);
		return NewExamMarksEntryHelper.getInstance().getOldRegMap(oldRegList);
	}
	/**
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public boolean saveMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		return transaction.saveMarks(newExamMarksEntryForm);
	}
	/**
	 * @param newExamMarksEntryForm
	 * @return
	 * @throws Exception
	 */
	public Double getMaxMarkOfSubject(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		return transaction.getMaxMarkOfSubject(newExamMarksEntryForm);
	}
	
	//raghu for all internals
	public Map<Integer,Double> getMaxMarkOfSubjectForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		return transaction.getMaxMarkOfSubjectForAllInternals(newExamMarksEntryForm);
	}
	
	public List<StudentTO> getStudentForInputForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		List marksList=transaction.getQueryForAlreadyEnteredMarksForAllInternal(newExamMarksEntryForm);// calling the method for checking data is there for the marksQuery
		Map<String,MarksDetailsTO> existsMarks=new HashMap<String, MarksDetailsTO>();
		if(marksList!=null && !marksList.isEmpty()){
			existsMarks=NewExamMarksEntryHelper.getInstance().convertBoDataToMarksMapForAll(marksList);// converting the database data to Required Map
		}
		List<StudentTO> studentListAll=new ArrayList<StudentTO>();
		Set<StudentMarksTO> studentList=new HashSet<StudentMarksTO>();
		if(!newExamMarksEntryForm.getExamType().equalsIgnoreCase("Supplementary")){
			List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
			String currentStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForCurrentClassStudentsForAllInternals(newExamMarksEntryForm);
			List currentStudentList=transaction.getDataForQuery(currentStudentQuery);// calling the method for getting current class students
			if(currentStudentList!=null && !currentStudentList.isEmpty()){
				studentListAll=NewExamMarksEntryHelper.getInstance().convertBotoTOListForCurrentStudentsForAll(studentListAll,currentStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForPreviousClassStudentsForAllInternals(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentListAll=NewExamMarksEntryHelper.getInstance().convertBotoTOListForPreviousClassStudentsForAll(studentListAll,previousStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
		}else{/*// For Supplementary
			Map<Integer, String> oldRegMap=getOldRegisterNo(newExamMarksEntryForm.getSchemeNo());
			String currentStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForSupplementaryCurrentClassStudents(newExamMarksEntryForm);
			List currentStudentList=transaction.getDataForQuery(currentStudentQuery);// calling the method for getting Supplementary current class students
			if(currentStudentList!=null && !currentStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForSupplementaryCurrentStudents(studentList,currentStudentList,existsMarks,newExamMarksEntryForm,oldRegMap);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForSupplementaryPreviousClassStudents(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Supplementary Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentList=NewExamMarksEntryHelper.getInstance().convertBotoTOListForSupplementaryPreviousClassStudents(studentList,previousStudentList,existsMarks,newExamMarksEntryForm,oldRegMap);
			}
		*/}
		return studentListAll;
	}
	
	public boolean saveMarksForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		return transaction.saveMarksForAllInternals(newExamMarksEntryForm);
	}
	
}
