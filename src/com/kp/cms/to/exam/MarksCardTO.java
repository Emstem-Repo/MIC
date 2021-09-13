package com.kp.cms.to.exam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.kp.cms.to.admin.SubjectTO;

public class MarksCardTO implements Serializable {
	private String courseName;
	private String date;
	private String semType;
	private String semNo;
	private String monthYear;
	private String studentName;
	private String regNo;
	private String totalMarksInWord;
	private String totalMaxmarks;
	private String totalMarksAwarded;
	private String result;
	private String totalCredits;
	private String gradePoints;
	private String examName;
	private String examName2;
	private String className;
	private String classId;
	private int count;
	private int regularMarkFlag;
	private int supMarkFlag;
	private Map<String,List<SubjectTO>> subMap;
	private Map<String,List<SubjectTO>> addOnCoursesubMap;
	private Map<String,List<SubjectTO>> nonElectivesubMap;
	private String photo;
	private int termNo;
	private String examNameSup;
	private String examId;
	private List<SubjectTO> mainList;
	private int studentId;
	private int exam;
	private int classesId;
	private String regularExamId;
	private String data;
	private String newExamId;
	private String dispalySem1and2;
	private int month;
	private int year;
	private int examDefId;
	private int monthCheck;
	private int yearCheck;
	private String totalGradePoints;
	private String resultClass;
	private String sgpa;
	private String totalGrade;
	private String semString;
	private String ugorpg;
	
	// vinodha for max marks displaying in common for all subjects
	private String maxCIAMarks;
	private String maxESEMarks;
	private String maxTotalMarks;
	private String maxMarksPractical;	
	private String practicalCIATotalMarks;
	private String practicalESATotalMarks;
	private String practicalTotalMarks;
	private String grandTotalCIAMarks;
	private String grandTotalESAMarks;
	private boolean hasPracticals;
	// Ashwini for SGPA storing
	private String gradeForSgpa;
	private String semesterSgpa;
	private String creditPointsForSgpa;
	private String totalGradePointsForSgpa;
	private String totalCreditsForSgpa;
	private String resultForSgpa;
	private String totalMarksAwardedForSgpa;
	private String totalMaxMarksForSgpa;
	private boolean dontShowPracticals;
	private String grandTotalMarks;
	private Map<String,List<SubjectTO>> subMapForSuppl;
	
	public int getExamDefId() {
		return examDefId;
	}
	public void setExamDefId(int examDefId) {
		this.examDefId = examDefId;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Map<String, List<SubjectTO>> getAddOnCoursesubMap() {
		return addOnCoursesubMap;
	}
	public void setAddOnCoursesubMap(Map<String, List<SubjectTO>> addOnCoursesubMap) {
		this.addOnCoursesubMap = addOnCoursesubMap;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSemType() {
		return semType;
	}
	public void setSemType(String semType) {
		this.semType = semType;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getTotalMarksInWord() {
		return totalMarksInWord;
	}
	public void setTotalMarksInWord(String totalMarksInWord) {
		this.totalMarksInWord = totalMarksInWord;
	}
	public String getTotalMaxmarks() {
		return totalMaxmarks;
	}
	public void setTotalMaxmarks(String totalMaxmarks) {
		this.totalMaxmarks = totalMaxmarks;
	}
	public String getTotalMarksAwarded() {
		return totalMarksAwarded;
	}
	public void setTotalMarksAwarded(String totalMarksAwarded) {
		this.totalMarksAwarded = totalMarksAwarded;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTotalCredits() {
		return totalCredits;
	}
	public void setTotalCredits(String totalCredits) {
		this.totalCredits = totalCredits;
	}
	public String getGradePoints() {
		return gradePoints;
	}
	public void setGradePoints(String gradePoints) {
		this.gradePoints = gradePoints;
	}
	public String getSemNo() {
		return semNo;
	}
	public void setSemNo(String semNo) {
		this.semNo = semNo;
	}
	public Map<String, List<SubjectTO>> getSubMap() {
		return subMap;
	}
	public void setSubMap(Map<String, List<SubjectTO>> subMap) {
		this.subMap = subMap;
	}
	public String getExamName() {
		return examName;
	}
	public String getClassName() {
		return className;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Map<String, List<SubjectTO>> getNonElectivesubMap() {
		return nonElectivesubMap;
	}
	public void setNonElectivesubMap(Map<String, List<SubjectTO>> nonElectivesubMap) {
		this.nonElectivesubMap = nonElectivesubMap;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getTermNo() {
		return termNo;
	}
	public void setTermNo(int termNo) {
		this.termNo = termNo;
	}
	public String getExamNameSup() {
		return examNameSup;
	}
	public void setExamNameSup(String examNameSup) {
		this.examNameSup = examNameSup;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public List<SubjectTO> getMainList() {
		return mainList;
	}
	public void setMainList(List<SubjectTO> mainList) {
		this.mainList = mainList;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getExam() {
		return exam;
	}
	public void setExam(int exam) {
		this.exam = exam;
	}
	public int getClassesId() {
		return classesId;
	}
	public void setClassesId(int classesId) {
		this.classesId = classesId;
	}
	public String getRegularExamId() {
		return regularExamId;
	}
	public void setRegularExamId(String regularExamId) {
		this.regularExamId = regularExamId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getNewExamId() {
		return newExamId;
	}
	public void setNewExamId(String newExamId) {
		this.newExamId = newExamId;
	}
	public String getDispalySem1and2() {
		return dispalySem1and2;
	}
	public void setDispalySem1and2(String dispalySem1and2) {
		this.dispalySem1and2 = dispalySem1and2;
	}
	public int getMonthCheck() {
		return monthCheck;
	}
	public void setMonthCheck(int monthCheck) {
		this.monthCheck = monthCheck;
	}
	public int getYearCheck() {
		return yearCheck;
	}
	public void setYearCheck(int yearCheck) {
		this.yearCheck = yearCheck;
	}
	public String getExamName2() {
		return examName2;
	}
	public void setExamName2(String examName2) {
		this.examName2 = examName2;
	}
	public int getRegularMarkFlag() {
		return regularMarkFlag;
	}
	public void setRegularMarkFlag(int regularMarkFlag) {
		this.regularMarkFlag = regularMarkFlag;
	}
	public int getSupMarkFlag() {
		return supMarkFlag;
	}
	public void setSupMarkFlag(int supMarkFlag) {
		this.supMarkFlag = supMarkFlag;
	}
	public String getTotalGradePoints() {
		return totalGradePoints;
	}
	public void setTotalGradePoints(String totalGradePoints) {
		this.totalGradePoints = totalGradePoints;
	}
	public String getResultClass() {
		return resultClass;
	}
	public void setResultClass(String resultClass) {
		this.resultClass = resultClass;
	}
	public String getSgpa() {
		return sgpa;
	}
	public void setSgpa(String sgpa) {
		this.sgpa = sgpa;
	}
	public String getTotalGrade() {
		return totalGrade;
	}
	public void setTotalGrade(String totalGrade) {
		this.totalGrade = totalGrade;
	}
	public String getSemString() {
		return semString;
	}
	public void setSemString(String semString) {
		this.semString = semString;
	}
	public String getUgorpg() {
		return ugorpg;
	}
	public void setUgorpg(String ugorpg) {
		this.ugorpg = ugorpg;
	}
	public String getMaxCIAMarks() {
		return maxCIAMarks;
	}
	public void setMaxCIAMarks(String maxCIAMarks) {
		this.maxCIAMarks = maxCIAMarks;
	}
	public String getMaxESEMarks() {
		return maxESEMarks;
	}
	public void setMaxESEMarks(String maxESEMarks) {
		this.maxESEMarks = maxESEMarks;
	}
	public String getMaxTotalMarks() {
		return maxTotalMarks;
	}
	public void setMaxTotalMarks(String maxTotalMarks) {
		this.maxTotalMarks = maxTotalMarks;
	}
	public String getPracticalCIATotalMarks() {
		return practicalCIATotalMarks;
	}
	public void setPracticalCIATotalMarks(String practicalCIATotalMarks) {
		this.practicalCIATotalMarks = practicalCIATotalMarks;
	}
	public String getPracticalESATotalMarks() {
		return practicalESATotalMarks;
	}
	public void setPracticalESATotalMarks(String practicalESATotalMarks) {
		this.practicalESATotalMarks = practicalESATotalMarks;
	}
	public String getPracticalTotalMarks() {
		return practicalTotalMarks;
	}
	public void setPracticalTotalMarks(String practicalTotalMarks) {
		this.practicalTotalMarks = practicalTotalMarks;
	}
	public String getGrandTotalCIAMarks() {
		return grandTotalCIAMarks;
	}
	public void setGrandTotalCIAMarks(String grandTotalCIAMarks) {
		this.grandTotalCIAMarks = grandTotalCIAMarks;
	}
	public String getGrandTotalESAMarks() {
		return grandTotalESAMarks;
	}
	public void setGrandTotalESAMarks(String grandTotalESAMarks) {
		this.grandTotalESAMarks = grandTotalESAMarks;
	}
	public String getMaxMarksPractical() {
		return maxMarksPractical;
	}
	public void setMaxMarksPractical(String maxMarksPractical) {
		this.maxMarksPractical = maxMarksPractical;
	}
	public boolean isHasPracticals() {
		return hasPracticals;
	}
	public void setHasPracticals(boolean hasPracticals) {
		this.hasPracticals = hasPracticals;
	}
	public String getGradeForSgpa() {
		return gradeForSgpa;
	}
	public void setGradeForSgpa(String gradeForSgpa) {
		this.gradeForSgpa = gradeForSgpa;
	}
	public String getSemesterSgpa() {
		return semesterSgpa;
	}
	public void setSemesterSgpa(String semesterSgpa) {
		this.semesterSgpa = semesterSgpa;
	}
	public String getCreditPointsForSgpa() {
		return creditPointsForSgpa;
	}
	public void setCreditPointsForSgpa(String creditPointsForSgpa) {
		this.creditPointsForSgpa = creditPointsForSgpa;
	}
	public String getTotalGradePointsForSgpa() {
		return totalGradePointsForSgpa;
	}
	public void setTotalGradePointsForSgpa(String totalGradePointsForSgpa) {
		this.totalGradePointsForSgpa = totalGradePointsForSgpa;
	}
	public String getTotalCreditsForSgpa() {
		return totalCreditsForSgpa;
	}
	public void setTotalCreditsForSgpa(String totalCreditsForSgpa) {
		this.totalCreditsForSgpa = totalCreditsForSgpa;
	}
	public String getResultForSgpa() {
		return resultForSgpa;
	}
	public void setResultForSgpa(String resultForSgpa) {
		this.resultForSgpa = resultForSgpa;
	}
	public String getTotalMarksAwardedForSgpa() {
		return totalMarksAwardedForSgpa;
	}
	public void setTotalMarksAwardedForSgpa(String totalMarksAwardedForSgpa) {
		this.totalMarksAwardedForSgpa = totalMarksAwardedForSgpa;
	}
	public String getTotalMaxMarksForSgpa() {
		return totalMaxMarksForSgpa;
	}
	public void setTotalMaxMarksForSgpa(String totalMaxMarksForSgpa) {
		this.totalMaxMarksForSgpa = totalMaxMarksForSgpa;
	}
	public boolean isDontShowPracticals() {
		return dontShowPracticals;
	}
	public void setDontShowPracticals(boolean dontShowPracticals) {
		this.dontShowPracticals = dontShowPracticals;
	}
	/**
	 * @return the grandTotalMarks
	 */
	public String getGrandTotalMarks() {
		return grandTotalMarks;
	}
	/**
	 * @param grandTotalMarks the grandTotalMarks to set
	 */
	public void setGrandTotalMarks(String grandTotalMarks) {
		this.grandTotalMarks = grandTotalMarks;
	}
	/**
	 * @return the subMapForSuppl
	 */
	public Map<String, List<SubjectTO>> getSubMapForSuppl() {
		return subMapForSuppl;
	}
	/**
	 * @param subMapForSuppl the subMapForSuppl to set
	 */
	public void setSubMapForSuppl(Map<String, List<SubjectTO>> subMapForSuppl) {
		this.subMapForSuppl = subMapForSuppl;
	}	
	
}