package com.kp.cms.forms.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamMarkEvaluationTo;
import com.kp.cms.to.exam.FalseNoDisplayTo;
import com.kp.cms.to.exam.KeyValueTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.StudentMarksTO1;

public class NewExamMarksEntryForm extends BaseActionForm {
	
	private String examType;
	private String marksEntryFor;
	private String schemeNo;
	private String subjectId;
	private String subjectType;
	private String evaluatorType;
	private String answerScriptType;
	private String orderBy;
	private String section;
	private String examId;
	private Map<Integer, String> examNameList;
	private Map<Integer, String> courseMap;
	private Map<String, String> schemeMap;
	private String validationAST;
	private String validationET;
	private String displayAST;
	private String displayET;
	private Map<Integer, String> evaluatorList;
	private Map<Integer, String> answerScriptTypeList;
	private Map<Integer, String> subjectTypeMap;
	private Map<Integer, String> subjectMap;
	private Map<Integer, String> sectionMap;
	private List<StudentMarksTO> studentList;
	private String examName;
	private String subjectName;
	private String subjectCode;
	private String courseName;
	private boolean regular;
	private boolean internal;
	private Map<Integer, Integer> studentsYearMap;
	private Map<Integer, Double> maxMarksMap;
	private String displaySubType;
	private Map<Integer, String> subjectCodeNameMap;
	private List<KeyValueTO> subjectList;
	private String roleId;
	private String roleIdForTeacher;
	private boolean teacher;
	private boolean controller;
	private boolean marksEntryExist;
	private boolean isFalsenoscreen;
	private List<StudentMarksTO1> studentlist1;
	//raghu all internal mark 
	private List<StudentTO> studentMarksList;
	private List<StudentMarksTO> examList;
	private String oldschemeNo;
	private boolean isfalsegenerated;
	private boolean oldMarksCheck;
	private int internalexamSize;
	
	private boolean isRoleIdForDeveloper;
	private boolean isExamMaxEntry;
	private String retest;
	private int totalCount;
	private boolean generateRandomly;
	private String falseNo;
	private String name;
	private boolean falseBased;
	private FalseNoDisplayTo displatoList;
	private String evalNo;
	private StudentMarksTO studentMarksTo;
	private List<ExamMarkEvaluationTo> examEvalToList;
	private String lastEnteredFalsenum;
	private String falseNumber;
	private boolean finalValidation;


	public boolean isIsfalsegenerated() {
		return isfalsegenerated;
	}
	public void setIsfalsegenerated(boolean isfalsegenerated) {
		this.isfalsegenerated = isfalsegenerated;
	}
	public Map<Integer, String> getSubjectCodeNameMap() {
		return subjectCodeNameMap;
	}
	public void setSubjectCodeNameMap(Map<Integer, String> subjectCodeNameMap) {
		this.subjectCodeNameMap = subjectCodeNameMap;
	}
	public String getDisplaySubType() {
		return displaySubType;
	}
	public void setDisplaySubType(String displaySubType) {
		this.displaySubType = displaySubType;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getMarksEntryFor() {
		return marksEntryFor;
	}
	public void setMarksEntryFor(String marksEntryFor) {
		this.marksEntryFor = marksEntryFor;
	}
	public String getSchemeNo() {
		return schemeNo;
	}
	public void setSchemeNo(String schemeNo) {
		this.schemeNo = schemeNo;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getEvaluatorType() {
		return evaluatorType;
	}

	public void setEvaluatorType(String evaluatorType) {
		this.evaluatorType = evaluatorType;
	}

	public String getAnswerScriptType() {
		return answerScriptType;
	}

	public void setAnswerScriptType(String answerScriptType) {
		this.answerScriptType = answerScriptType;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public Map<Integer, String> getExamNameList() {
		return examNameList;
	}
	public void setExamNameList(Map<Integer, String> examNameList) {
		this.examNameList = examNameList;
	}
	public Map<Integer, String> getCourseMap() {
		return courseMap;
	}
	public void setCourseMap(Map<Integer, String> courseMap) {
		this.courseMap = courseMap;
	}
	public Map<String, String> getSchemeMap() {
		return schemeMap;
	}
	public void setSchemeMap(Map<String, String> schemeMap) {
		this.schemeMap = schemeMap;
	}
	public String getValidationAST() {
		return validationAST;
	}
	public Map<Integer, String> getSubjectTypeMap() {
		return subjectTypeMap;
	}
	public void setSubjectTypeMap(Map<Integer, String> subjectTypeMap) {
		this.subjectTypeMap = subjectTypeMap;
	}
	public void setValidationAST(String validationAST) {
		this.validationAST = validationAST;
	}
	public String getValidationET() {
		return validationET;
	}
	public void setValidationET(String validationET) {
		this.validationET = validationET;
	}
	public String getDisplayAST() {
		return displayAST;
	}
	public void setDisplayAST(String displayAST) {
		this.displayAST = displayAST;
	}
	public String getDisplayET() {
		return displayET;
	}
	public void setDisplayET(String displayET) {
		this.displayET = displayET;
	}
	public Map<Integer, String> getEvaluatorList() {
		return evaluatorList;
	}
	public void setEvaluatorList(Map<Integer, String> evaluatorList) {
		this.evaluatorList = evaluatorList;
	}
	public Map<Integer, String> getAnswerScriptTypeList() {
		return answerScriptTypeList;
	}
	public void setAnswerScriptTypeList(Map<Integer, String> answerScriptTypeList) {
		this.answerScriptTypeList = answerScriptTypeList;
	}
	public Map<Integer, String> getSubjectMap() {
		return subjectMap;
	}
	public void setSubjectMap(Map<Integer, String> subjectMap) {
		this.subjectMap = subjectMap;
	}
	public Map<Integer, String> getSectionMap() {
		return sectionMap;
	}
	public void setSectionMap(Map<Integer, String> sectionMap) {
		this.sectionMap = sectionMap;
	}
	public List<StudentMarksTO> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<StudentMarksTO> studentList) {
		this.studentList = studentList;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public boolean isRegular() {
		return regular;
	}
	public void setRegular(boolean regular) {
		this.regular = regular;
	}
	public boolean isInternal() {
		return internal;
	}
	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	
	/**
	 * reseting the fields in the form
	 */
	public void resetFields() {
		this.examType="Regular";
		this.marksEntryFor=null;
		this.schemeNo=null;
		this.subjectId=null;
		this.subjectType=null;
		this.evaluatorType=null;
		this.answerScriptType=null;
		this.orderBy="register";
		this.section=null;
		this.examId=null;
		super.setCourseId(null);
		this.examName=null;
		this.subjectCode=null;
		this.subjectName=null;
		this.courseName=null;
		this.regular=false;
		this.internal=false;
		this.displaySubType=null;
		this.falseBased=false;
		this.name=null;
		this.falseNo=null;
		this.evalNo=null;
		this.displatoList=new FalseNoDisplayTo();
	}
	public Map<Integer, Integer> getStudentsYearMap() {
		return studentsYearMap;
	}
	public void setStudentsYearMap(Map<Integer, Integer> studentsYearMap) {
		this.studentsYearMap = studentsYearMap;
	}
	public Map<Integer, Double> getMaxMarksMap() {
		return maxMarksMap;
	}
	public void setMaxMarksMap(Map<Integer, Double> maxMarksMap) {
		this.maxMarksMap = maxMarksMap;
	}
	public List<KeyValueTO> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<KeyValueTO> subjectList) {
		this.subjectList = subjectList;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleIdForTeacher() {
		return roleIdForTeacher;
	}
	public void setRoleIdForTeacher(String roleIdForTeacher) {
		this.roleIdForTeacher = roleIdForTeacher;
	}
	public boolean isTeacher() {
		return teacher;
	}
	public void setTeacher(boolean teacher) {
		this.teacher = teacher;
	}
	public boolean isController() {
		return controller;
	}
	public void setController(boolean controller) {
		this.controller = controller;
	}
	public boolean isMarksEntryExist() {
		return marksEntryExist;
	}
	public void setMarksEntryExist(boolean marksEntryExist) {
		this.marksEntryExist = marksEntryExist;
	}
	public boolean isFalsenoscreen() {
		return isFalsenoscreen;
	}
	public void setFalsenoscreen(boolean isFalsenoscreen) {
		this.isFalsenoscreen = isFalsenoscreen;
	}
	public List<StudentMarksTO1> getStudentlist1() {
		return studentlist1;
	}
	public void setStudentlist1(List<StudentMarksTO1> studentlist1) {
		this.studentlist1 = studentlist1;
	}	
	public List<StudentTO> getStudentMarksList() {
		return studentMarksList;
	}
	public void setStudentMarksList(List<StudentTO> studentMarksList) {
		this.studentMarksList = studentMarksList;
	}
	public List<StudentMarksTO> getExamList() {
		return examList;
	}
	public void setExamList(List<StudentMarksTO> examList) {
		this.examList = examList;
	}
	public String getOldschemeNo() {
		return oldschemeNo;
	}
	public void setOldschemeNo(String oldschemeNo) {
		this.oldschemeNo = oldschemeNo;
	}
	public boolean getOldMarksCheck() {
		return oldMarksCheck;
	}
	public void setOldMarksCheck(boolean oldMarksCheck) {
		this.oldMarksCheck = oldMarksCheck;
	}
	public int getInternalexamSize() {
		return internalexamSize;
	}
	public void setInternalexamSize(int internalexamSize) {
		this.internalexamSize = internalexamSize;
	}
	public boolean getIsRoleIdForDeveloper() {
		return isRoleIdForDeveloper;
	}
	public void setIsRoleIdForDeveloper(boolean isRoleIdForDeveloper) {
		this.isRoleIdForDeveloper = isRoleIdForDeveloper;
	}
	public Boolean getIsExamMaxEntry() {
		return isExamMaxEntry;
	}
	public void setIsExamMaxEntry(Boolean isExamMaxEntry) {
		this.isExamMaxEntry = isExamMaxEntry;
	}
	public String getRetest() {
		return retest;
	}
	public void setRetest(String retest) {
		this.retest = retest;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean getGenerateRandomly() {
		return generateRandomly;
	}
	public void setGenerateRandomly(boolean generateRandomly) {
		this.generateRandomly = generateRandomly;
	}
	public String getFalseNo() {
		return falseNo;
	}
	public void setFalseNo(String falseNo) {
		this.falseNo = falseNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFalseBased() {
		return falseBased;
	}
	public void setFalseBased(boolean falseBased) {
		this.falseBased = falseBased;
	}
	public FalseNoDisplayTo getDisplatoList() {
		return displatoList;
	}
	public void setDisplatoList(FalseNoDisplayTo displatoList) {
		this.displatoList = displatoList;
	}
	public String getEvalNo() {
		return evalNo;
	}
	public void setEvalNo(String evalNo) {
		this.evalNo = evalNo;
	}
	public StudentMarksTO getStudentMarksTo() {
		return studentMarksTo;
	}
	public void setStudentMarksTo(StudentMarksTO studentMarksTo) {
		this.studentMarksTo = studentMarksTo;
	}
	public String getLastEnteredFalsenum() {
		return lastEnteredFalsenum;
	}
	public void setLastEnteredFalsenum(String lastEnteredFalsenum) {
		this.lastEnteredFalsenum = lastEnteredFalsenum;
	}
	
	public boolean isFinalValidation() {
		return finalValidation;
	}
	public void setFinalValidation(boolean finalValidation) {
		this.finalValidation = finalValidation;
	}
	public List<ExamMarkEvaluationTo> getExamEvalToList() {
		return examEvalToList;
	}
	public void setExamEvalToList(List<ExamMarkEvaluationTo> examEvalToList) {
		this.examEvalToList = examEvalToList;
	}
	public String getFalseNumber() {
		return falseNumber;
	}
	public void setFalseNumber(String falseNumber) {
		this.falseNumber = falseNumber;
	}
	
	
}
