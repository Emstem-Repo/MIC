package com.kp.cms.transactions.attandance;

import java.util.List;

import com.kp.cms.bo.admin.ExtraCoCurricularLeavePublishBO;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;

public interface IExtraCoCurricularLeavePublishTransaction {

	boolean save(List<ExtraCoCurricularLeavePublishBO> exBoList) throws Exception;

	ExtraCoCurricularLeavePublishBO getRecord(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception;

	boolean update(ExtraCoCurricularLeavePublishBO extraCoCurricularLeavePublishBO) throws Exception;

	boolean isDuplicate(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception;

	ExtraCoCurricularLeavePublishBO getForEditDetails(int id) throws Exception;

	boolean deleteOpenConnection(ExtraCoCurricularLeavePublishForm publishForm1) throws Exception;

	List<ExtraCoCurricularLeavePublishBO> getList(int year) throws Exception;

	int getRecordId(String classId) throws Exception;

	boolean updateOpenConnection(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception;

}
