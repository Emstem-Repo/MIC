package com.kp.cms.transactions.attandance;

import java.util.List;
import java.util.Map;

import com.kp.cms.bo.admin.Activity;
import com.kp.cms.bo.admin.AttendanceType;
import com.kp.cms.bo.admin.Period;
import com.kp.cms.bo.admin.StuCocurrLeave;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.forms.attendance.ExtraCocurricularLeaveEntryForm;
import com.kp.cms.to.attendance.ApproveLeaveTO;

public interface IExtraCocurricularLeaveEntryTransaction {

	public Student getStudentByStudentId(String studentId, String courseId) throws Exception;
	public List<Period> getPeriodListByClass(int id)throws Exception;
	public Map<Integer, String> getSubjectMap()throws Exception;
	public List<Object[]> getDuplicateRecords(String attendanceDuplicateQuery) throws Exception;
	public List<Object[]> getAttendnaceListByDates(String attendanceQuery) throws Exception;
	public boolean saveCocurricularLeaveDetails(List<StuCocurrLeave> cocurricularListNew,List<ApproveLeaveTO> approveLeaveTOs,ExtraCocurricularLeaveEntryForm extraCocurricularLeaveEntryForm) throws Exception;
	public boolean isValidApplyDate(int classid, String reason) throws Exception;
	public int getClassIdByStudent(String studentid) throws Exception;
	public Activity getActivity(String activity) throws Exception;
	public Student getStud(String studentid) throws Exception;
	

}
