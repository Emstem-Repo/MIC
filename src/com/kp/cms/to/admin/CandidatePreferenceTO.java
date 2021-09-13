package com.kp.cms.to.admin;

import java.io.Serializable;
import java.util.List;



public class CandidatePreferenceTO implements Serializable,Comparable<CandidatePreferenceTO> {
	private int id;
	private String coursId;
	private String coursName;
	private String progId;
	private String programtypeId;
	private int admApplnid;
	private int prefNo;
	private List<CourseTO> prefcourses;
	private List<ProgramTypeTO> prefProgramtypes;
	private List<ProgramTO> prefprograms;
	private Boolean isMandatory;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

	public String getCoursId() {
		return coursId;
	}
	public void setCoursId(String coursId) {
		this.coursId = coursId;
	}
	public String getProgId() {
		return progId;
	}
	public void setProgId(String progId) {
		this.progId = progId;
	}
	public String getProgramtypeId() {
		return programtypeId;
	}
	public void setProgramtypeId(String programtypeId) {
		this.programtypeId = programtypeId;
	}
	public int getPrefNo() {
		return prefNo;
	}
	public void setPrefNo(int prefNo) {
		this.prefNo = prefNo;
	}
	public List<CourseTO> getPrefcourses() {
		return prefcourses;
	}
	public void setPrefcourses(List<CourseTO> prefcourses) {
		this.prefcourses = prefcourses;
	}
	public List<ProgramTypeTO> getPrefProgramtypes() {
		return prefProgramtypes;
	}
	public void setPrefProgramtypes(List<ProgramTypeTO> prefProgramtypes) {
		this.prefProgramtypes = prefProgramtypes;
	}
	public List<ProgramTO> getPrefprograms() {
		return prefprograms;
	}
	public void setPrefprograms(List<ProgramTO> prefprograms) {
		this.prefprograms = prefprograms;
	}
	public int getAdmApplnid() {
		return admApplnid;
	}
	public void setAdmApplnid(int admApplnid) {
		this.admApplnid = admApplnid;
	}
	public String getCoursName() {
		return coursName;
	}
	public void setCoursName(String coursName) {
		this.coursName = coursName;
	}
	@Override
	public int compareTo(CandidatePreferenceTO arg0) {
		if(arg0!=null && this!=null){
			if(this.getPrefNo() > arg0.getPrefNo())
				return 1;
			else if(this.getPrefNo() < arg0.getPrefNo())
				return -1;
			else
				return 0;
		}
		return 0;
	}
	public Boolean getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
		
}
