package com.kp.cms.transactions.timetable;

import java.util.List;

import com.kp.cms.bo.admin.TTSubjectBatch;
import com.kp.cms.forms.timetable.TimeTableForClassForm;

public interface ITimeTableForClassTransaction {

	boolean addTimeTableForaPeriod(List<TTSubjectBatch> boList, TimeTableForClassForm timeTableForClassForm) throws Exception;

	boolean updateFlagForTimeTable(String userId, int ttClassId, String finalApprove) throws Exception;

	boolean checkForTtClassHistory(String classId) throws Exception;

}
