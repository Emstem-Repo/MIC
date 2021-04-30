package com.kp.cms.forms.attendance;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.attendance.ApproveLeaveTO;
import com.kp.cms.to.attendance.CocurricularAttendnaceEntryTo;


public class ExtraCocurricularLeaveEntryForm extends BaseActionForm{
	
	private String classes;
	private Map<Integer, String> cocurriculartActivityMap;
	Map<Date, CocurricularAttendnaceEntryTo> cocurricularAttendanceEntryToList;
	private int stuId;
	private List<ApproveLeaveTO> approveLeaveTOs;
	List<CocurricularAttendnaceEntryTo> list;
	List<CocurricularAttendnaceEntryTo>leaveList;
	private String studentName;
	private String registerNo;
	private String className;
	private String termNo;
	private String workingHours;
	private String presentHours;
	private String percentage;
	private String requiredHrs;
	private String shortageOfAttendance;
	private String subTotalHrs;
	//newly added
	private boolean dislaySubmitButton;
	private boolean isPublished;
	private boolean isPeriodChecked;
	private String approvedLeaveHrs;
	private Map<String,String> periodsMap;
	
	
	

	public void clearAll(){
		this.classes = null;
		this.cocurriculartActivityMap = null;
		this.approveLeaveTOs=null;
		this.cocurricularAttendanceEntryToList = null;
		this.approveLeaveTOs = null;
		this.list = null;
	}
	//newly added
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public Map<Integer, String> getCocurriculartActivityMap() {
		return cocurriculartActivityMap;
	}

	public void setCocurriculartActivityMap(
			Map<Integer, String> cocurriculartActivityMap) {
		this.cocurriculartActivityMap = cocurriculartActivityMap;
	}

	public Map<Date, CocurricularAttendnaceEntryTo> getCocurricularAttendanceEntryToList() {
		return cocurricularAttendanceEntryToList;
	}

	public void setCocurricularAttendanceEntryToList(
			Map<Date, CocurricularAttendnaceEntryTo> cocurricularAttendanceEntryToList) {
		this.cocurricularAttendanceEntryToList = cocurricularAttendanceEntryToList;
	}

	public int getStuId() {
		return stuId;
	}

	public void setStuId(int stuId) {
		this.stuId = stuId;
	}

	public List<ApproveLeaveTO> getApproveLeaveTOs() {
		return approveLeaveTOs;
	}

	public void setApproveLeaveTOs(List<ApproveLeaveTO> approveLeaveTOs) {
		this.approveLeaveTOs = approveLeaveTOs;
	}

	public List<CocurricularAttendnaceEntryTo> getList() {
		return list;
	}

	public void setList(List<CocurricularAttendnaceEntryTo> list) {
		this.list = list;
	}
	public boolean isDislaySubmitButton() {
		return dislaySubmitButton;
	}
	public void setDislaySubmitButton(boolean dislaySubmitButton) {
		this.dislaySubmitButton = dislaySubmitButton;
	}
	public boolean getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}
	public List<CocurricularAttendnaceEntryTo> getLeaveList() {
		return leaveList;
	}
	public void setLeaveList(List<CocurricularAttendnaceEntryTo> leaveList) {
		this.leaveList = leaveList;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTermNo() {
		return termNo;
	}
	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}
	public String getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	public String getPresentHours() {
		return presentHours;
	}
	public void setPresentHours(String presentHours) {
		this.presentHours = presentHours;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getRequiredHrs() {
		return requiredHrs;
	}
	public void setRequiredHrs(String requiredHrs) {
		this.requiredHrs = requiredHrs;
	}
	public String getShortageOfAttendance() {
		return shortageOfAttendance;
	}
	public void setShortageOfAttendance(String shortageOfAttendance) {
		this.shortageOfAttendance = shortageOfAttendance;
	}
	public String getSubTotalHrs() {
		return subTotalHrs;
	}
	public void setSubTotalHrs(String subTotalHrs) {
		this.subTotalHrs = subTotalHrs;
	}
	public boolean getIsPeriodChecked() {
		return isPeriodChecked;
	}
	public void setIsPeriodChecked(boolean isPeriodChecked) {
		this.isPeriodChecked = isPeriodChecked;
	}
	public String getApprovedLeaveHrs() {
		return approvedLeaveHrs;
	}
	public void setApprovedLeaveHrs(String approvedLeaveHrs) {
		this.approvedLeaveHrs = approvedLeaveHrs;
	}
	public Map<String, String> getPeriodsMap() {
		return periodsMap;
	}
	public void setPeriodsMap(Map<String, String> periodsMap) {
		this.periodsMap = periodsMap;
	}
}
