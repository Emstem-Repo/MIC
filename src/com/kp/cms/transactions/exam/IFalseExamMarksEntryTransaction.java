package com.kp.cms.transactions.exam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.ProgramType;
import com.kp.cms.bo.exam.ExamBlockUnblockHallTicketBO;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.ExamMarkEvaluationBo;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.SupplementaryFees;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.forms.exam.NewSecuredMarksEntryForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;

public interface IFalseExamMarksEntryTransaction {

	List getDataForQuery(String query) throws Exception;

	boolean saveMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;

	List<String> getExamAbscentCode()throws Exception;

	Double getMaxMarkOfSubject(NewSecuredMarksEntryForm newSecuredMarksEntryForm)throws Exception;

	BigDecimal getExamDiffPercentage() throws Exception;

	Double getMaxMarkOfSubject(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;

	Map<String, String> getOldRegMap(List<Integer> schemeList) throws Exception;

	Map<String, byte[]> getStudentPhtos(List<Integer> studentIds,boolean isReg) throws Exception;

	ExamBlockUnblockHallTicketBO getExamBlockUnblockHallTicket( Integer studentId, int examId, int classId,String hallOrMark) throws Exception;
	
	Map<Integer, byte[]> getStudentPhtosMap(List<Integer> studentIds,boolean isReg) throws Exception;
	
	List<Integer> getDataForQueryClasses(String query) throws Exception;

	boolean checkAggregateResultClassWise(int sid) throws Exception;

	Map<Integer, String> getSubjects(String examId, String subCode,
			String examType, String year) throws Exception;
	
	Boolean getExtendedDate(int studentId, NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm) throws Exception;
	
	List<Integer> getExemptionStudentsListBySubjectId(String subjectId,String examId) throws Exception;
	//raghu added from mounts
	String getApplicationNumber(int studentId) throws Exception;

	List<StudentSupplementaryImprovementApplication> getDataForSQLQuery(String query) throws Exception;
	
	public ProgramType getProgramTypeByClassId(int classId) throws Exception;

	//raghu for all internals
	public Map<Integer,Double> getMaxMarkOfSubjectForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;
	
	boolean saveMarksForAllInternals(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;

	List getQueryForAlreadyEnteredMarksForAllInternal(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;
	
	public List getFalseNoForQuery(String falsenoquery) throws Exception;

	boolean isCurrentDateValidForAllInternalMarks(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception;
	
	public boolean isEligibleWithoutAttendanceCheck(int studentId,int examId) throws Exception;

	public List<Object[]> getDataByStudentAndClassId(int studentId, int classId, int subjectId,
			Integer year)throws Exception;

	public int checkSubjectInRetestForm(NewExamMarksEntryForm newExamMarksEntryForm, int id);

	boolean saveBo(SupplementaryFees bo) throws Exception;

	boolean saveRegularBo(RegularExamFees bo) throws Exception;
	public int getCurrentNO(NewExamMarksEntryForm marksCardForm)throws Exception;

	public ExamFalseNumberGen getDetailsByFalsenum(String falseNo);

	public boolean checkFallseBox(NewExamMarksEntryForm marksCardForm);

	public Object getUniqeDataForQuery(String query) throws Exception;

	boolean saveEvalationMarks(List<ExamMarkEvaluationBo> boList) throws BusinessException, ApplicationException;

	int getDuplication(String falseNo);

}
