package com.kp.cms.to.admission;

public class BoardDetailsTO implements Comparable<BoardDetailsTO>{
	private int studentId;
	private String registerNo;
	private String studentName;
	private String examRegNo;
	private String studentNo;
	private String fatherName;
	private String motherName;
	 
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getExamRegNo() {
		return examRegNo;
	}
	public void setExamRegNo(String examRegNo) {
		this.examRegNo = examRegNo;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	@Override
	public int compareTo(BoardDetailsTO arg0) {

		if(arg0!=null && this!=null && arg0.getRegisterNo()!=null
				 && this.getRegisterNo()!=null){
				return this.getRegisterNo().compareTo(arg0.getRegisterNo());
		}
		else
			return 0;
		
	
	}
	
}
