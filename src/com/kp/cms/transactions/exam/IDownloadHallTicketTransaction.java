package com.kp.cms.transactions.exam;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.ProgramType;
import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.bo.exam.ExamBlockUnblockHallTicketBO;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamFooterAgreementBO;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.forms.admission.DisciplinaryDetailsForm;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.to.examallotment.ExamRoomAllotmentDetailsTO;

public interface IDownloadHallTicketTransaction {
	public int getClassId(int studentId, LoginForm loginForm) throws Exception;
	public int getExamIdByClassId(int classId, LoginForm loginForm, String hallTicketOrMarksCard) throws Exception;
	public ExamBlockUnblockHallTicketBO isHallTicketBlockedStudent(int studentId , int classId, int examId, String hallTicketMarksCard) throws Exception;
	public boolean isCurrentDateValidForDownLoadHallTicket(int classId, int examId, String hallTicketMarksCard, boolean isSup) throws Exception;
	public ExamFooterAgreementBO getExamFooterAgreement(int agrementId) throws Exception;
	public int getClassIds(int studentId, int curClassId, boolean isSup, String publishedFor)throws Exception;
	public boolean getIsExcluded(int studentId, int examId) throws Exception;
	public Integer getTermNumber(int classId) throws Exception;
	public String getExamIdByClassIdForSupHallTicket(int classId, LoginForm loginForm, String hallTicketOrMarksCard) throws Exception;
	public boolean getIsAppliedForSupp(int studentId, int examId,int classId, String mode);
	public List getStudentHallTicket(String hallTicketQuery) throws Exception;
	public List<ExamFooterAgreementBO> getFooterDetails(String programTypeId,String type, String classId) throws Exception;
	public List<Object[]> getDataByHql(String query) throws Exception;
	public List<Integer> getSupplementaryClassIds(int studentId, int curClassId,boolean isSup, String publishedFor) throws Exception;
	public int getTermNo(int studentId, DisciplinaryDetailsForm objForm) throws Exception;
	
	public int getClassId(int studentId, DisciplinaryDetailsForm objForm) throws Exception;
	public int getExamIdByClassId(int classId, DisciplinaryDetailsForm objForm, String PublishedFor) throws Exception;
	//public int getExamIdByClassIdFromPreviousStudentDetailsView(String classIds, boolean isSup, String publishedFor) throws Exception;
	public List<Integer> getClassIdsView(int studentId, int curClassId, boolean isSup, String publishedFor)throws Exception;

	public List<Integer> getRegularClassIds(int studentId, int curClassId,boolean isSup, String publishedFor) throws Exception;
	public int getExamIdByClassIdForSupHallTicket(int classId, DisciplinaryDetailsForm objForm, String hallTicketOrMarksCard) throws Exception;
	public ExamBlockUnblockHallTicketBO getExamBlockUnblock(String blockId) throws Exception;
	public List<Object[]> getStudentSupMarksCard(String hallTicketQuery)	throws Exception;
	public List<ExamSupplementaryImprovementApplicationBO> getStudentSupMarksCardList(String hallTicketQuery)throws Exception;
	public int getExamIdByClassIdForSupMarksCard(int classId, LoginForm loginForm, String hallTicketOrMarksCard) throws Exception;
	public List getStudentHallTicketNew(String hallTicketQuery,List<Integer> classList) throws Exception;
	
	public int getExamIdByClassIdMarksCard(int classId, LoginForm loginForm, String hallTicketOrMarksCard) throws Exception;
	public List<Integer> getRegularClassIdsForMarksCard(int studentId, int curClassId,boolean isSup, String publishedFor) throws Exception;
	public Object[] getRoomNoAndFloorNoAndBlockNo(int studentId, int examId, int classId, Map<String, ExamRoomAllotmentDetailsTO> examSessionMap)throws Exception;
	public String getStudentBatch(int stdId)throws Exception;
	public StudentCertificateCourse getStudentCertificateCourseOnGoing(
			int studentId, int termNo)throws Exception;
	
	public Map<Integer, String> getClasesByExamName(String examId, String year) throws Exception;
	
	public Map<Integer, Map<String, ExamRoomAllotmentDetailsTO>> getRoomNoDetailsForStudents(
			List<Integer> studentIds, String examId, String classId) throws Exception;
	public String getProgramTypeByMarksCardClassId(int classId) throws Exception;
	public ProgramType getProgramTypeByClassId(int classId) throws Exception;
	public int getPreClassId(int studentId, LoginForm loginForm) throws Exception;
	public List getSubjectsListForMarksCard(int stuId, int academicYear,int courseId,int termNo) throws Exception;
	public int getClassIdForRevaluation(int studentId, LoginForm loginForm) throws Exception;
	public List<Object> checkRevaluationAppAvailable(List<Integer> classIds) throws Exception ;
	public ExamDefinitionBO getObj(LoginForm loginForm) throws Exception;
	public boolean isCurrentDateValidForCourseOption(int classId) throws Exception;
	public String getPreviousClassIds(int studentId,int curClassId)throws Exception;
	public Map<Integer, String> getInternalMarksCardClasses(String prevClassIds, String publishFor)throws Exception;
	public Map<Integer, String> getInternalExamNameByClass(String internalClassId)throws Exception;
	public int getAcadamicYearByClass(String internalClassId)throws Exception;
	public List<Object[]> getInternalDataByStudent(int internalClassId,int studentId, int acadamicYear)throws Exception;
	public boolean checkDateIsValid(String previousClassIds, String publishedFor)throws Exception;
	public boolean isDateValid(String classId) throws Exception;
	public int getPrevClass(String studentid) throws Exception;
	public boolean isDateValidForLink(String classId) throws Exception;
}
