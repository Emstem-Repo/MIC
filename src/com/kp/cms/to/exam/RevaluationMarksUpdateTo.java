package com.kp.cms.to.exam;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.to.admin.EmployeeTO;

public class RevaluationMarksUpdateTo implements Comparable<RevaluationMarksUpdateTo>{
	private String registerNo;
	private String className;
	private String newMarks;
	private String newMark1;
	private String newMark2;
	private String oldMarks;
	private String oldMark1;
	private String oldMark2;
	private String subjectCode;
	private String studentName;
	private String diffOfMarks;
	private String diffOfMarks1;
	private String diffOfMarks2;
	private String comment;
	private String avgMarks;
	private int studentId;
	private int subjectId;
	private int classId;
	private int examRevaluationAppId;
	private int examMarksEntryId;
	private int examMarksEntryDetailsId;
	private int examMarksEntryIdForSecondEvl2;
	private int examMarksEntryDetailsIdForSecondEvl2;
	private int examMarksEntryIdForNoEvl;
	private int examMarksEntryDetailsIdForNoEvl;
	private int courseId;
	private int schemeNumber;
	private int examRevaluationAppIdForEvL1;
	private int examRevaluationAppIdForEvL2;
	private String oldAvgMarks;
	private String marks;
	private String thirdEvlMarks;
	private Boolean gracedMark;
	private Boolean gracedMark1;
	private Boolean gracedMark2;
	private Boolean isUpdated;
	
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getNewMarks() {
		return newMarks;
	}
	public void setNewMarks(String newMarks) {
		this.newMarks = newMarks;
	}
	public String getNewMark1() {
		return newMark1;
	}
	public void setNewMark1(String newMark1) {
		this.newMark1 = newMark1;
	}
	public String getNewMark2() {
		return newMark2;
	}
	public void setNewMark2(String newMark2) {
		this.newMark2 = newMark2;
	}
	public String getOldMarks() {
		return oldMarks;
	}
	public void setOldMarks(String oldMarks) {
		this.oldMarks = oldMarks;
	}
	public String getOldMark1() {
		return oldMark1;
	}
	public void setOldMark1(String oldMark1) {
		this.oldMark1 = oldMark1;
	}
	public String getOldMark2() {
		return oldMark2;
	}
	public void setOldMark2(String oldMark2) {
		this.oldMark2 = oldMark2;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getDiffOfMarks() {
		return diffOfMarks;
	}
	public void setDiffOfMarks(String diffOfMarks) {
		this.diffOfMarks = diffOfMarks;
	}
	public String getDiffOfMarks1() {
		return diffOfMarks1;
	}
	public void setDiffOfMarks1(String diffOfMarks1) {
		this.diffOfMarks1 = diffOfMarks1;
	}
	public String getDiffOfMarks2() {
		return diffOfMarks2;
	}
	public void setDiffOfMarks2(String diffOfMarks2) {
		this.diffOfMarks2 = diffOfMarks2;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAvgMarks() {
		return avgMarks;
	}
	public void setAvgMarks(String avgMarks) {
		this.avgMarks = avgMarks;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getExamRevaluationAppId() {
		return examRevaluationAppId;
	}
	public void setExamRevaluationAppId(int examRevaluationAppId) {
		this.examRevaluationAppId = examRevaluationAppId;
	}
	public int getExamMarksEntryId() {
		return examMarksEntryId;
	}
	public void setExamMarksEntryId(int examMarksEntryId) {
		this.examMarksEntryId = examMarksEntryId;
	}
	public int getExamMarksEntryDetailsId() {
		return examMarksEntryDetailsId;
	}
	public void setExamMarksEntryDetailsId(int examMarksEntryDetailsId) {
		this.examMarksEntryDetailsId = examMarksEntryDetailsId;
	}
	public int getExamMarksEntryIdForSecondEvl2() {
		return examMarksEntryIdForSecondEvl2;
	}
	public void setExamMarksEntryIdForSecondEvl2(int examMarksEntryIdForSecondEvl2) {
		this.examMarksEntryIdForSecondEvl2 = examMarksEntryIdForSecondEvl2;
	}
	public int getExamMarksEntryDetailsIdForSecondEvl2() {
		return examMarksEntryDetailsIdForSecondEvl2;
	}
	public void setExamMarksEntryDetailsIdForSecondEvl2(
			int examMarksEntryDetailsIdForSecondEvl2) {
		this.examMarksEntryDetailsIdForSecondEvl2 = examMarksEntryDetailsIdForSecondEvl2;
	}
	public int getExamMarksEntryIdForNoEvl() {
		return examMarksEntryIdForNoEvl;
	}
	public void setExamMarksEntryIdForNoEvl(int examMarksEntryIdForNoEvl) {
		this.examMarksEntryIdForNoEvl = examMarksEntryIdForNoEvl;
	}
	public int getExamMarksEntryDetailsIdForNoEvl() {
		return examMarksEntryDetailsIdForNoEvl;
	}
	public void setExamMarksEntryDetailsIdForNoEvl(
			int examMarksEntryDetailsIdForNoEvl) {
		this.examMarksEntryDetailsIdForNoEvl = examMarksEntryDetailsIdForNoEvl;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getSchemeNumber() {
		return schemeNumber;
	}
	public void setSchemeNumber(int schemeNumber) {
		this.schemeNumber = schemeNumber;
	}
	public int getExamRevaluationAppIdForEvL1() {
		return examRevaluationAppIdForEvL1;
	}
	public void setExamRevaluationAppIdForEvL1(int examRevaluationAppIdForEvL1) {
		this.examRevaluationAppIdForEvL1 = examRevaluationAppIdForEvL1;
	}
	public int getExamRevaluationAppIdForEvL2() {
		return examRevaluationAppIdForEvL2;
	}
	public void setExamRevaluationAppIdForEvL2(int examRevaluationAppIdForEvL2) {
		this.examRevaluationAppIdForEvL2 = examRevaluationAppIdForEvL2;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	
	
	public int compareTo(RevaluationMarksUpdateTo arg0) {
		if(arg0 instanceof RevaluationMarksUpdateTo && this.getSubjectCode()!=null &&  arg0.getSubjectCode()!=null ){
			return this.getSubjectCode().compareTo(arg0.subjectCode);
	}else
		return 0;
}
	public String getOldAvgMarks() {
		return oldAvgMarks;
	}
	public void setOldAvgMarks(String oldAvgMarks) {
		this.oldAvgMarks = oldAvgMarks;
	}
	public String getThirdEvlMarks() {
		return thirdEvlMarks;
	}
	public void setThirdEvlMarks(String thirdEvlMarks) {
		this.thirdEvlMarks = thirdEvlMarks;
	}
	public Boolean getGracedMark() {
		return gracedMark;
	}
	public void setGracedMark(Boolean gracedMark) {
		this.gracedMark = gracedMark;
	}
	public Boolean getGracedMark1() {
		return gracedMark1;
	}
	public void setGracedMark1(Boolean gracedMark1) {
		this.gracedMark1 = gracedMark1;
	}
	public Boolean getGracedMark2() {
		return gracedMark2;
	}
	public void setGracedMark2(Boolean gracedMark2) {
		this.gracedMark2 = gracedMark2;
	}
	public Boolean getIsUpdated() {
		return isUpdated;
	}
	public void setIsUpdated(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	
}
