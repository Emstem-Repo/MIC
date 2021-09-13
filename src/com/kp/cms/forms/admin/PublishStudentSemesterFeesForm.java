package com.kp.cms.forms.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.PublishOptionalCourseSubjectApplicationTO;
import com.kp.cms.to.admin.PublishStudentSemesterFeesTO;

public class PublishStudentSemesterFeesForm  extends BaseActionForm{
	private String fromDate;
	private String toDate;
	private String academicYear;
	private String id;
	private String DupId;
	private String orgId;
	private String[] stayClass;
	
	private Map<Integer, String> mapClass;
	private Map<Integer, String> mapSelectedClass;
	private String[] classCodeIdsFrom;
	private String[] classCodeIdsTo;
	private Map<Integer,String> classMap = new HashMap<Integer, String>();
	private List<PublishStudentSemesterFeesTO> attendanceList;
	private String classId;
	private String classNames;
	private String tempYear1;
	
	
	
	
	
	 public String getTempYear1() {
		return tempYear1;
	}
	public void setTempYear1(String tempYear1) {
		this.tempYear1 = tempYear1;
	}
	public String[] getStayClass() {
			return stayClass;
		}
		public void setStayClass(String[] stayClass) {
			this.stayClass = stayClass;
		}
	
	public Map<Integer, String> getMapClass() {
		return mapClass;
	}
	public void setMapClass(Map<Integer, String> mapClass) {
		this.mapClass = mapClass;
	}
	public Map<Integer, String> getMapSelectedClass() {
		return mapSelectedClass;
	}
	public void setMapSelectedClass(Map<Integer, String> mapSelectedClass) {
		this.mapSelectedClass = mapSelectedClass;
	}
	public String[] getClassCodeIdsFrom() {
		return classCodeIdsFrom;
	}
	public void setClassCodeIdsFrom(String[] classCodeIdsFrom) {
		this.classCodeIdsFrom = classCodeIdsFrom;
	}
	public String[] getClassCodeIdsTo() {
		return classCodeIdsTo;
	}
	public void setClassCodeIdsTo(String[] classCodeIdsTo) {
		this.classCodeIdsTo = classCodeIdsTo;
	}
	
	public List<PublishStudentSemesterFeesTO> getAttendanceList() {
		return attendanceList;
	}
	public void setAttendanceList(
			List<PublishStudentSemesterFeesTO> attendanceList) {
		this.attendanceList = attendanceList;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDupId() {
		return DupId;
	}
	public void setDupId(String dupId) {
		DupId = dupId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Map<Integer, String> getClassMap() {
		return classMap;
	}
	public void setClassMap(Map<Integer, String> classMap) {
		this.classMap = classMap;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassNames() {
		return classNames;
	}
	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.classCodeIdsTo = null;
		this.toDate = null;
		this.fromDate = null;
		this.academicYear = null;
		this.attendanceList=null;
		this.classMap=null;
		this.classNames=null;
		this.classId=null;
	}
	
	
	
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter(CMSConstants.FORMNAME);
		ActionErrors actionErrors = super.validate(mapping, request, formName);

		return actionErrors;
	}



}
