package com.kp.cms.handlers.attendance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.ExtraCoCurricularLeavePublishBO;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;
import com.kp.cms.helpers.attendance.ExtraCoCurricularLeavePublishHelper;
import com.kp.cms.to.attendance.ExtraCoCurricularLeavePublishTO;
import com.kp.cms.to.employee.ExceptionDetailsDatesTO;
import com.kp.cms.transactions.attandance.IExtraCoCurricularLeavePublishTransaction;
import com.kp.cms.transactionsimpl.attendance.ExtraCoCurricularLeavePublishTransactionImpl;

public class ExtraCoCurricularLeavePublishHandler {
	private static volatile ExtraCoCurricularLeavePublishHandler  extraCoCurricularLeavePublishHandler= null;
	private static final Log log = LogFactory.getLog(ExtraCoCurricularLeavePublishHandler.class);
	public static ExtraCoCurricularLeavePublishHandler getInstance()
	{
		if(extraCoCurricularLeavePublishHandler==null)
		{
			extraCoCurricularLeavePublishHandler = new ExtraCoCurricularLeavePublishHandler();
		}
		return extraCoCurricularLeavePublishHandler;
	}
	
	public String formatDate(Date date){
		DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
		String newDate=formatter.format(date);
		return newDate;
	}
	public boolean saveData(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		List<ExtraCoCurricularLeavePublishBO> exBoList = ExtraCoCurricularLeavePublishHelper.getInstance().convertToBO(extraCoCurricularLeavePublishForm);
		return txn.save(exBoList);
	}
	public ExtraCoCurricularLeavePublishBO getRecord(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		ExtraCoCurricularLeavePublishBO exLeavePublishBO = txn.getRecord(extraCoCurricularLeavePublishForm);
		return exLeavePublishBO;
	}
	public boolean updateData(ExtraCoCurricularLeavePublishBO exPublishBO, ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		ExtraCoCurricularLeavePublishBO extraCoCurricularLeavePublishBO = ExtraCoCurricularLeavePublishHelper.getInstance().convertToUpdateBo(exPublishBO,extraCoCurricularLeavePublishForm);
		return txn.update(extraCoCurricularLeavePublishBO);
	}
	public boolean isDuplicate(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		boolean isDuplicate = txn.isDuplicate(extraCoCurricularLeavePublishForm) ;
		return isDuplicate;
	}
	public void getEditExtraCoCurricularFormPublish(ExtraCoCurricularLeavePublishForm publishForm) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		ExtraCoCurricularLeavePublishBO bo = txn.getForEditDetails(publishForm.getId());
		if(bo!=null && !bo.toString().isEmpty()){
			if(bo.getId()!=0 ){
				publishForm.setId(bo.getId());
			}
			if(bo.getClasses()!=null && !bo.getClasses().toString().isEmpty()){
				String classesId[] =  new String[1];
				 classesId[0] = Integer.toString(bo.getClasses().getId());
				 publishForm.setClassListId(classesId);
				 
			}
			if(bo.getPublishStartDate()!=null && !bo.getPublishStartDate().toString().isEmpty()){
				publishForm.setPublishStartDate(formatDate(bo.getPublishStartDate()));
			}
			if(bo.getPublishEndDate()!=null && !bo.getPublishEndDate().toString().isEmpty()){
				publishForm.setPublishEndDate(formatDate(bo.getPublishEndDate()));
			}
			if(bo.getClasses()!=null && bo.getClasses().getClassSchemewises()!=null){
				Set<ClassSchemewise> set = bo.getClasses().getClassSchemewises();
				Iterator<ClassSchemewise> iterator = set.iterator();
				while (iterator.hasNext()) {
					ClassSchemewise classSchemewise = (ClassSchemewise) iterator .next();
					if(classSchemewise!=null && classSchemewise.getCurriculumSchemeDuration()!= null){
						if(classSchemewise.getCurriculumSchemeDuration().getAcademicYear()!=null){
							publishForm.setAcademicYear(classSchemewise.getCurriculumSchemeDuration().getAcademicYear().toString());
						}
					}
				}
			}
		}
		
	}

	public boolean deleteExtraCoCurricularFormPublish(ExtraCoCurricularLeavePublishForm publishForm1) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		boolean isDeleted = txn.deleteOpenConnection(publishForm1);
		return isDeleted;
	}

	public List<ExtraCoCurricularLeavePublishTO> getStudentDetails(int year) throws Exception {
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		List<ExtraCoCurricularLeavePublishBO> list = txn.getList(year);
		List<ExtraCoCurricularLeavePublishTO> toList = ExtraCoCurricularLeavePublishHelper.getInstance().convertBOToTO(list);
		return toList;
	}

	public boolean updateExtraCocurricularLeaveDetails(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception{
		IExtraCoCurricularLeavePublishTransaction txn = new ExtraCoCurricularLeavePublishTransactionImpl();
		String[] clsId = extraCoCurricularLeavePublishForm.getClassListId();
		  String classId = clsId[0];
		  int recordId = txn.getRecordId(classId);
		  if(recordId != 0){
			  extraCoCurricularLeavePublishForm.setId(recordId);
		  }
			boolean isUpdated = txn.updateOpenConnection(extraCoCurricularLeavePublishForm);
			return isUpdated;
		}
	}
	
	
	

