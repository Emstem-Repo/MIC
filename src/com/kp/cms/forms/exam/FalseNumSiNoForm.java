package com.kp.cms.forms.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.bo.admin.Users;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.exam.FalseBoxDetTo;
import com.kp.cms.to.exam.FalseNumSiNoTO;
import com.kp.cms.to.exam.KeyValueTO;

public class FalseNumSiNoForm extends BaseActionForm {
				
	private String startNo;
	private List<FalseNumSiNoTO> toList; 
	private boolean isAdded;
	//private String academicYear;
	private String semister;
	private List<ProgramTypeTO> programTypeList;
	private String prefix;
	private boolean regular;
	private boolean internal;
	private List<KeyValueTO> subjectList;
	private int totalCount;
	private Map<Integer, String> examNameList;
	private Map<Integer, String> courseMap;
	private Map<String, String> schemeMap;
	private String examId;
	private Map<Integer, String> subjectMap;
	private Map<Integer, String> sectionMap;
	private Map<Integer, String> subjectCodeNameMap;
	private String displaySubType;
	private String examType;
	private int falseNoId;
	private Map<String, String> batchMap;
	private String currentFalseNo;
	private String teachers;
	private Map<Integer, String> subjectTypeMap;
	private Map<Integer, String> teachersMap;
	private String boxNo;
	private String barcode;
	private List<FalseBoxDetTo> barcodeList;
	private List<FalseBoxDetTo> ceckBarcodeList;
	private String deleId;
	private String outside;
	
	private boolean edit;

	private String chiefExaminer;
	private String additionalExaminer;
	private List<FalseBoxDetTo> falseBoxToList;
	private int falseBoxId;
	private Map<Integer, String> departmentMap;
	private String correctionValidator;
	
	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}
	
	public void reset(){
		this.startNo=null;
		this.toList=null;
		this.prefix=null;
		this.currentFalseNo = null;
		this.subjectTypeMap=new HashMap();
		this.teachersMap=new HashMap();
		this.departmentMap=new HashMap();
		this.barcodeList=new ArrayList();
		this.additionalExaminer=null;
		this.chiefExaminer=null;
		this.teachers=null;
		this.examId=null;
		this.subjectId=null;
		this.boxNo=null;
		
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors = super.validate(mapping, request, formName);
		return actionErrors;
	}

	

	public List<FalseNumSiNoTO> getToList() {
		return toList;
	}

	public void setToList(List<FalseNumSiNoTO> toList) {
		this.toList = toList;
	}

	public boolean getIsAdded() {
		return isAdded;
	}

	public void setIsAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}

	/*public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
*/
	public String getSemister() {
		return semister;
	}

	public void setSemister(String semister) {
		this.semister = semister;
	}

	public List<ProgramTypeTO> getProgramTypeList() {
		return programTypeList;
	}

	public void setProgramTypeList(List<ProgramTypeTO> programTypeList) {
		this.programTypeList = programTypeList;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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

	public List<KeyValueTO> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<KeyValueTO> subjectList) {
		this.subjectList = subjectList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
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
	public int getFalseNoId() {
		return falseNoId;
	}

	public void setFalseNoId(int falseNoId) {
		this.falseNoId = falseNoId;
	}
	
	public Map<String, String> getBatchMap() {
		return batchMap;
	}

	public void setBatchMap(Map<String, String> batchMap) {
		this.batchMap = batchMap;
	}
	
	

	public String getCurrentFalseNo() {
		return currentFalseNo;
	}

	public void setCurrentFalseNo(String currentFalseNo) {
		this.currentFalseNo = currentFalseNo;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		//this.academicYear=null;
		this.startNo=null;
		this.examId=null;
		this.subjectId=null;
		this.semister=null;
		this.examType="Regular";
		this.currentFalseNo = null;
	}

	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	public Map<Integer, String> getSubjectTypeMap() {
		return subjectTypeMap;
	}

	public void setSubjectTypeMap(Map<Integer, String> subjectTypeMap) {
		this.subjectTypeMap = subjectTypeMap;
	}

	public Map<Integer, String> getTeachersMap() {
		return teachersMap;
	}

	public void setTeachersMap(Map<Integer, String> teachersMap) {
		this.teachersMap = teachersMap;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}



	public String getDeleId() {
		return deleId;
	}

	public void setDeleId(String deleId) {
		this.deleId = deleId;
	}

	public String getChiefExaminer() {
		return chiefExaminer;
	}

	public void setChiefExaminer(String chiefExaminer) {
		this.chiefExaminer = chiefExaminer;
	}

	public String getAdditionalExaminer() {
		return additionalExaminer;
	}

	public void setAdditionalExaminer(String additionalExaminer) {
		this.additionalExaminer = additionalExaminer;
	}

	public List<FalseBoxDetTo> getFalseBoxToList() {
		return falseBoxToList;
	}

	public void setFalseBoxToList(List<FalseBoxDetTo> falseBoxToList) {
		this.falseBoxToList = falseBoxToList;
	}

	public int getFalseBoxId() {
		return falseBoxId;
	}

	public void setFalseBoxId(int falseBoxId) {
		this.falseBoxId = falseBoxId;
	}

	public List<FalseBoxDetTo> getBarcodeList() {
		return barcodeList;
	}

	public void setBarcodeList(List<FalseBoxDetTo> barcodeList) {
		this.barcodeList = barcodeList;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<FalseBoxDetTo> getCeckBarcodeList() {
		return ceckBarcodeList;
	}

	public void setCeckBarcodeList(List<FalseBoxDetTo> ceckBarcodeList) {
		this.ceckBarcodeList = ceckBarcodeList;
	}

	public Map<Integer, String> getDepartmentMap() {
		return departmentMap;
	}

	public void setDepartmentMap(Map<Integer, String> departmentMap) {
		this.departmentMap = departmentMap;
	}

	public String getOutside() {
		return outside;
	}

	public void setOutside(String outside) {
		this.outside = outside;
	}

	public String getCorrectionValidator() {
		return correctionValidator;
	}

	public void setCorrectionValidator(String correctionValidator) {
		this.correctionValidator = correctionValidator;
	}

	
}
