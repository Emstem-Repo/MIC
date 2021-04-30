package com.kp.cms.to.exam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ConsolidateMarksCardTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String registerNo;
	private List<String> regNos;
	private String courseName;
	private String yearOfStudy;
	private String result;
	private String totalMaxMarks;
	private String totalMarksAwarded;
	private String totalCredits;
	List<MarksCardTO> toList;
	private String gradePointAvg;
	private String studentName;
	private String programmeName;
	private String examMonth;
	private int examYear;
	private int semester;
	private Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> programmePartMap;
	private List<ExamStudentSGPATO> sgpaTO;
	private double ccpas;
	private int ccpasCredit;
	private String ccpasGrade;
	private double ccpa;
	private int ccpaCredit;
	private String ccpaGrade;
	private int courseId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getYearOfStudy() {
		return yearOfStudy;
	}
	public void setYearOfStudy(String yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTotalMaxMarks() {
		return totalMaxMarks;
	}
	public void setTotalMaxMarks(String totalMaxMarks) {
		this.totalMaxMarks = totalMaxMarks;
	}
	public String getTotalMarksAwarded() {
		return totalMarksAwarded;
	}
	public void setTotalMarksAwarded(String totalMarksAwarded) {
		this.totalMarksAwarded = totalMarksAwarded;
	}
	public String getTotalCredits() {
		return totalCredits;
	}
	public void setTotalCredits(String totalCredits) {
		this.totalCredits = totalCredits;
	}
	public List<MarksCardTO> getToList() {
		return toList;
	}
	public void setToList(List<MarksCardTO> toList) {
		this.toList = toList;
	}
	public String getGradePointAvg() {
		return gradePointAvg;
	}
	public void setGradePointAvg(String gradePointAvg) {
		this.gradePointAvg = gradePointAvg;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public List<String> getRegNos() {
		return regNos;
	}
	public void setRegNos(List<String> regNos) {
		this.regNos = regNos;
	}
	public String getProgrammeName() {
		return programmeName;
	}
	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}
	public String getExamMonth() {
		return examMonth;
	}
	public void setExamMonth(String examMonth) {
		this.examMonth = examMonth;
	}
	public int getExamYear() {
		return examYear;
	}
	public void setExamYear(int examYear) {
		this.examYear = examYear;
	}
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	public Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> getProgrammePartMap() {
		return programmePartMap;
	}
	public void setProgrammePartMap(
			Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> programmePartMap) {
		this.programmePartMap = programmePartMap;
	}
	public List<ExamStudentSGPATO> getSgpaTO() {
		return sgpaTO;
	}
	public void setSgpaTO(List<ExamStudentSGPATO> sgpaTO) {
		this.sgpaTO = sgpaTO;
	}
	public double getCcpas() {
		return ccpas;
	}
	public void setCcpas(double ccpas) {
		this.ccpas = ccpas;
	}
	public int getCcpasCredit() {
		return ccpasCredit;
	}
	public void setCcpasCredit(int ccpasCredit) {
		this.ccpasCredit = ccpasCredit;
	}
	public String getCcpasGrade() {
		return ccpasGrade;
	}
	public void setCcpasGrade(String ccpasGrade) {
		this.ccpasGrade = ccpasGrade;
	}
	public double getCcpa() {
		return ccpa;
	}
	public void setCcpa(double ccpa) {
		this.ccpa = ccpa;
	}
	public int getCcpaCredit() {
		return ccpaCredit;
	}
	public void setCcpaCredit(int ccpaCredit) {
		this.ccpaCredit = ccpaCredit;
	}
	public String getCcpaGrade() {
		return ccpaGrade;
	}
	public void setCcpaGrade(String ccpaGrade) {
		this.ccpaGrade = ccpaGrade;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
}