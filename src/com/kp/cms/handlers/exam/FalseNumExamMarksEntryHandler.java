package com.kp.cms.handlers.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.ExamMarkEvaluationBo;
import com.kp.cms.bo.exam.FalseNumberBox;
import com.kp.cms.bo.exam.MarksEntryDetails;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.helpers.exam.FalseExamMarksEntryHelper;
import com.kp.cms.helpers.exam.NewExamMarksEntryHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamMarkEvaluationPrintTo;
import com.kp.cms.to.exam.ExamMarkEvaluationTo;
import com.kp.cms.to.exam.FalseNoDisplayTo;
import com.kp.cms.to.exam.MarksDetailsTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.transactions.exam.IFalseExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.FalseExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;

public class FalseNumExamMarksEntryHandler {
	/**
	 * Singleton object of newExamMarksEntryHandler
	 */
	private static volatile FalseNumExamMarksEntryHandler newExamMarksEntryHandler = null;
	private static final Log log = LogFactory.getLog(FalseNumExamMarksEntryHandler.class);
	private FalseNumExamMarksEntryHandler() {
		
	}
	/**
	 * return singleton object of newExamMarksEntryHandler.
	 * @return
	 */
	public static FalseNumExamMarksEntryHandler getInstance() {
		if (newExamMarksEntryHandler == null) {
			newExamMarksEntryHandler = new FalseNumExamMarksEntryHandler();
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
		String marksQuery=FalseExamMarksEntryHelper.getInstance().getQueryForAlreadyEnteredMarks(newExamMarksEntryForm);
		IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		String evalmarksQuery=FalseExamMarksEntryHelper.getInstance().getQueryForAlreadyEnteredEvaluationMarks(newExamMarksEntryForm);
		MarksEntryDetails markentrydetBo=(MarksEntryDetails) transaction.getUniqeDataForQuery(marksQuery);// calling the method for checking data is there for the marksQuery
		ExamMarkEvaluationBo evalBo=(ExamMarkEvaluationBo) transaction.getUniqeDataForQuery(evalmarksQuery);
		
		
		MarksDetailsTO existsMarks=null;
		if(markentrydetBo!=null){
			existsMarks=FalseExamMarksEntryHelper.getInstance().convertBoDataToMarksMap(markentrydetBo);// converting the database data to Required Map
		}
		ExamMarkEvaluationTo existsEvalMarks=null;
		if(evalBo!=null){
			existsEvalMarks=FalseExamMarksEntryHelper.getInstance().convertEvalBoDataToMarksMap(evalBo);// converting the database data to Required Map
		}
		
		Set<StudentMarksTO> studentList=new HashSet<StudentMarksTO>();
		if(!newExamMarksEntryForm.getExamType().equalsIgnoreCase("Supplementary")){
			List<Integer> listOfDetainedStudents =ExamMarksEntryHandler.getInstance().getDetainedOrDiscontinuedStudents();
			
			String currentStudentQuery=FalseExamMarksEntryHelper.getInstance().getQueryForCurrentClassStudents(newExamMarksEntryForm);
			
			List currentStudentList=transaction.getDataForQuery(currentStudentQuery);// calling the method for getting current class students
			if(currentStudentList!=null && !currentStudentList.isEmpty()){
				studentList=FalseExamMarksEntryHelper.getInstance().convertBotoTOListForCurrentStudents(studentList,currentStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm,existsEvalMarks);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForPreviousClassStudents(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentList=FalseExamMarksEntryHelper.getInstance().convertBotoTOListForPreviousClassStudents(studentList,previousStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm,existsEvalMarks);
			}
		}/*else{// For Supplementary
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
		}*/
		return studentList;
	}
	
	public boolean saveEvalationMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();
		List<ExamMarkEvaluationPrintTo> prnto=new ArrayList();
		List<ExamMarkEvaluationBo> boList = FalseExamMarksEntryHelper.getInstance().convertEvationTOBO(newExamMarksEntryForm);
		setprintdata(boList,prnto, newExamMarksEntryForm);
		if (!newExamMarksEntryForm.isSaved()) {
			return transaction.saveEvalationMarks(boList);
		}
		else{
			return false;
		}
	}
	
	public void setprintdata(List<ExamMarkEvaluationBo> boList, List<ExamMarkEvaluationPrintTo> prnto, NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		List<ExamMarkEvaluationPrintTo> toList=new ArrayList();
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		ExamFalseNumberGen falbo=null;
		ExamMarkEvaluationPrintTo printTo=new ExamMarkEvaluationPrintTo();
		Users user=(Users) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("from Users u where u.id="+newExamMarksEntryForm.getUserId());
		
		for (ExamMarkEvaluationBo bo : boList) {
			ExamMarkEvaluationPrintTo to=new ExamMarkEvaluationPrintTo();
			falbo= transaction.getDetailsByFalsenum(bo.getFalseNo());
			String boxNo=(String) FalseExamMarksEntryTransactionImpl.getInstance().getUniqeDataForQuery("select bo.falseNumBox.boxNum from FalseNumberBoxDetails bo where bo.falseNum='"+bo.getFalseNo()+"'");
			if (bo.getFirstEvaluation()!=0 && (newExamMarksEntryForm.getEvalNo() == "1" || newExamMarksEntryForm.getEvalNo().equalsIgnoreCase("1"))) {
				to.setFirstEvaluation(String.valueOf(bo.getFirstEvaluation()));
				to.setFirstEvaluator(bo.getFirstEvaluator().getId());
				to.setMark(String.valueOf(bo.getFirstEvaluation()));
				to.setMarkInWords(convertToWords(String.valueOf(bo.getFirstEvaluation()).toCharArray()));
			
			}
			
			if (bo.getSecondEvaluation()!=0 && (newExamMarksEntryForm.getEvalNo() == "2" || newExamMarksEntryForm.getEvalNo().equalsIgnoreCase("2"))) {
				to.setSecondEvaluation(String.valueOf(bo.getSecondEvaluation()));
				to.setSecondEvaluator(bo.getSecondEvaluator().getId());
				to.setMark(String.valueOf(bo.getSecondEvaluation()));
				to.setMarkInWords(convertToWords(String.valueOf(bo.getSecondEvaluation()).toCharArray()));
				
			}
			if (bo.getThirdEvaluation()!=0 && (newExamMarksEntryForm.getEvalNo() == "3" || newExamMarksEntryForm.getEvalNo().equalsIgnoreCase("3"))) {
				to.setThirdEvaluation(String.valueOf(bo.getThirdEvaluation()));
				to.setThirdEvaluator(bo.getThirdEvaluator().getId());
				to.setMark(String.valueOf(bo.getThirdEvaluation()));
				to.setMarkInWords(convertToWords(String.valueOf(bo.getThirdEvaluation()).toCharArray()));
				
			}
			
			if (bo.getFinalEvaluation()!=0 && (newExamMarksEntryForm.getEvalNo() == "4" || newExamMarksEntryForm.getEvalNo().equalsIgnoreCase("4"))) {
				to.setFinalEvaluation(String.valueOf(bo.getFinalEvaluation()));
				to.setFinalEvaluator(bo.getFinalEvaluator().getId());
				to.setMark(String.valueOf(bo.getFinalEvaluation()));
				to.setMarkInWords(convertToWords(String.valueOf(bo.getFinalEvaluation()).toCharArray()));
				
			}
			if (printTo.getEmpName()==null) {
				if (user.getEmployee()!=null && user.getEmployee().getDepartment()!=null) {
					printTo.setEmpName(user.getEmployee().getFirstName());
					printTo.setProfession(user.getEmployee().getDesignationName());
					printTo.setDept(user.getEmployee().getDepartment().getName());
				}else if (user.getGuest()!=null) {
					printTo.setEmpName(user.getGuest().getFirstName());
					printTo.setProfession(user.getGuest().getDesignationName());
					printTo.setDept(user.getGuest().getDepartment().getName());
				}
			}
			
			to.setFalseNo(bo.getFalseNo());
			to.setBoxNo(boxNo);
			newExamMarksEntryForm.setExamMarkPrintTo(printTo);
			newExamMarksEntryForm.setExamName(falbo.getExamId().getName());
			newExamMarksEntryForm.setCourseName(falbo.getSubject().getName());
			newExamMarksEntryForm.setProgramName(falbo.getCourse().getName());
			newExamMarksEntryForm.setCourseCode(falbo.getSubject().getCode());
			//newExamMarksEntryForm.setQpCode(qpCode);
			toList.add(to);
		}
		
		
		newExamMarksEntryForm.setExamMarkEvaluationPrintToList(toList);
	}
	public Set<StudentMarksTO> getStudentForFinalPublishInput(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		String marksQuery=NewExamMarksEntryHelper.getInstance().getQueryForAlreadyEnteredMarks(newExamMarksEntryForm);
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		List marksList=transaction.getDataForQuery(marksQuery);// calling the method for checking data is there for the marksQuery
		if (marksList!=null && !marksList.isEmpty()) {
			newExamMarksEntryForm.setFinalValidation(true);
		}else{
			newExamMarksEntryForm.setFinalValidation(false);
		}
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
				studentList=FalseExamMarksEntryHelper.getInstance().convertFinnalMarkBotoTOListForCurrentStudents(studentList,currentStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
			
			String previousStudentQuery=NewExamMarksEntryHelper.getInstance().getQueryForPreviousClassStudents(newExamMarksEntryForm);
			List previousStudentList=transaction.getDataForQuery(previousStudentQuery);// calling the method for getting Previous class students
			if(previousStudentList!=null && !previousStudentList.isEmpty()){
				studentList=FalseExamMarksEntryHelper.getInstance().convertFinalMarkBotoTOListForPreviousClassStudents(studentList,previousStudentList,existsMarks,listOfDetainedStudents,newExamMarksEntryForm);
			}
		}/*else{// For Supplementary
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
		}*/
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
		IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();// creating object for Transaction Impl class
		return transaction.getMaxMarkOfSubject(newExamMarksEntryForm);
	}
	public boolean checkFinalSubmitAccess(NewExamMarksEntryForm newExamMarksEntryForm) {
		FalseNumberBox bo=FalseExamMarksEntryTransactionImpl.getInstance().getDetailsOfFalseBox(newExamMarksEntryForm);
		if (bo!=null) {
			return true;
		}
		return false;
	}
	public boolean addEvalationMarks(NewExamMarksEntryForm newExamMarksEntryForm) {IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();
		ExamMarkEvaluationTo to=newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo();
		List<ExamMarkEvaluationTo> toList=newExamMarksEntryForm.getExamEvalToList();
		int dup=0;
		for (ExamMarkEvaluationTo tos : toList) {
			if (tos.getFalseNo()==newExamMarksEntryForm.getFalseNo()
					|| tos.getFalseNo().equalsIgnoreCase(newExamMarksEntryForm.getFalseNo())) {
				dup++;
				
			}
		}
		if (dup==0) {
			to.setFalseNo(newExamMarksEntryForm.getFalseNo());
			toList.add(to);
			newExamMarksEntryForm.setExamEvalToList(toList);
		}
		
	return true;
	}
	
	
	static String convertToWords(char[] num)
    {
        // Get number of digits
        // in given number
        int len = num.length;
  
       
        String[] single_digits = new String[] {
            "zero", "one", "two",   "three", "four",
            "five", "six", "seven", "eight", "nine"
        };
  
      
        String[] two_digits = new String[] {
            "",          "ten",      "eleven",  "twelve",
            "thirteen",  "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen"
        };
  
        
        String[] tens_multiple = new String[] {
            "",      "",      "twenty",  "thirty", "forty",
            "fifty", "sixty", "seventy", "eighty", "ninety"
        };
  
       
  
       
  
        /* For single digit number */
        if (len == 1) {
            System.out.println(single_digits[num[0] - '0']);
            return single_digits[num[0] - '0'].toString();
        }
  
        /* Iterate while num
            is not '\0' */
        int x = 0;
        StringBuilder sb=new StringBuilder();
        while (x < num.length) {
  
           
                if (num[x] - '0' == 1) {
                    int sum
                        = num[x] - '0' + num[x + 1] - '0';
                    System.out.println(two_digits[sum]);
                    return two_digits[sum].toString();
                }
  
                /* Need to explicitly handle 20 */
                else if (num[x] - '0' == 2
                         && num[x + 1] - '0' == 0) {
                    System.out.println("twenty");
                    return "twenty";
                }
  
                /* Rest of the two digit
                numbers i.e., 21 to 99 */
                else {
                	
                    int i = (num[x] - '0');
                    if (i > 0){
                        //System.out.print(tens_multiple[i]+ " ");
                        sb.append(tens_multiple[i]+ " ");
                    }
                    else{
                        System.out.print("");
                        sb.append("");
                    }
                    ++x;
                    if (num[x] - '0' != 0){
                        //System.out.println(single_digits[num[x] - '0']);
                        sb.append(single_digits[num[x] - '0']);
                    }
                    System.out.println(sb.toString()); 
                }
            ++x;
        }
		return sb.toString();
    }
	
	public boolean addEvalationMarksAdmin(NewExamMarksEntryForm newExamMarksEntryForm) {IFalseExamMarksEntryTransaction transaction=FalseExamMarksEntryTransactionImpl.getInstance();
	ExamMarkEvaluationTo to=newExamMarksEntryForm.getStudentMarksTo().getExamEvalTo();
	List<ExamMarkEvaluationTo> toList=newExamMarksEntryForm.getExamEvalToList();
	int dup=0;
	for (ExamMarkEvaluationTo tos : toList) {
		if (tos.getFalseNo()==newExamMarksEntryForm.getFalseNo()
				|| tos.getFalseNo().equalsIgnoreCase(newExamMarksEntryForm.getFalseNo())) {
			dup++;
			
		}
	}
	if (dup==0) {
		to.setFalseNo(newExamMarksEntryForm.getFalseNo());
		if (to.getFirstEvaluation() != null && !to.getFirstEvaluation().isEmpty()) {
			to.setFirstEvaluator(newExamMarksEntryForm.getUserId());
		}
		if (to.getSecondEvaluation() != null && !to.getSecondEvaluation().isEmpty()) {
			to.setSecondEvaluator(newExamMarksEntryForm.getUserId());
		}
		if (to.getThirdEvaluation() != null && !to.getThirdEvaluation().isEmpty()) {
			to.setThirdEvaluator(newExamMarksEntryForm.getUserId());
		}

		toList.add(to);
		newExamMarksEntryForm.setExamEvalToList(toList);
	}
	
return true;
}
	
}
