package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;

import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.PayScaleBO;
import com.kp.cms.bo.exam.ExamRevaluationFee;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.employee.PayScaleDetailsForm;
import com.kp.cms.forms.exam.ExamRevaluationFeeForm;
import com.kp.cms.helpers.employee.PayScaleDetailsHelper;
import com.kp.cms.helpers.exam.ExamRevaluationFeeHelper;
import com.kp.cms.helpers.exam.SupplementaryFeesHelper;
import com.kp.cms.to.employee.PayScaleTO;
import com.kp.cms.to.exam.ExamRevaluationFeeTO;
import com.kp.cms.transactions.admin.ISingleFieldMasterTransaction;
import com.kp.cms.transactions.employee.IPayScaleTransactions;
import com.kp.cms.transactions.exam.IExamRevaluationFeeTxn;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.admin.SingleFieldMasterTransactionImpl;
import com.kp.cms.transactionsimpl.employee.PayScaleTransactionImpl;
import com.kp.cms.transactionsimpl.exam.ExamRevaluationFeeTxnImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.PropertyUtil;

public class ExamRevaluationFeeHandler {
	private static final Log log = LogFactory.getLog(ExamRevaluationFeeHandler.class);
	private static volatile ExamRevaluationFeeHandler instance=null;
	IExamRevaluationFeeTxn transaction=ExamRevaluationFeeTxnImpl.getInstance().getInstance();
	
	public static ExamRevaluationFeeHandler getInstance(){
		log.info("Start getInstance of ExamRevaluationFeeHandler");
		if(instance==null){
			instance=new ExamRevaluationFeeHandler();
		}
		log.info("End getInstance of ExamRevaluationFeeHandler");
		return instance;
	}

	
	public boolean duplicateCheck(ExamRevaluationFeeForm examRevaluationFeeForm,ActionErrors errors,HttpSession session){
		boolean duplicate=transaction.duplicateCheck(session,errors,examRevaluationFeeForm);
		return duplicate;
	}

	public void editRevaluationFee(ExamRevaluationFeeForm examRevaluationFeeForm)throws Exception{
		ExamRevaluationFee revaluationFee=transaction.getRevaluationFeeById(examRevaluationFeeForm.getId());
		//ExamRevaluationFeeHelper.getInstance().setBotoForm(examRevaluationFeeForm, revaluationFee);
	}

	public boolean updateRevaluationFee(ExamRevaluationFeeForm examRevaluationFeeForm,String mode){
		//ExamRevaluationFee revaluation=ExamRevaluationFeeHelper.getInstance().convertFormTOBO(examRevaluationFeeForm, mode);
	//	boolean isUpdated=transaction.updateRevaluationFee(revaluation);
		return false;
	}
	
	//revaluation
	public List<ExamRevaluationFeeTO> getActiveListRevaluation() throws Exception {
		String query="from ExamRevaluationFee r where r.isActive=1";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<ExamRevaluationFee> boList=transaction.getDataForQuery(query);
		return ExamRevaluationFeeHelper.getInstance().convertBotoToListRevaluation(boList);
	}
	public boolean addOrUpdateRevaluationFee(ExamRevaluationFeeForm examRevaluationFeeForm) throws Exception {
		log.info("Entered into addOrUpdate");
		String query="from ExamRevaluationFee r where r.isActive=1 and r.academicYear="+examRevaluationFeeForm.getAcademicYear()+" and r.course.id in(";
		StringBuilder intType =new StringBuilder();
		String[] tempArray=examRevaluationFeeForm.getSelectedCourse();
		for(int i=0;i<tempArray.length;i++){
			intType.append(tempArray[i]);
			 if(i<(tempArray.length-1)){
				 intType.append(",");
			 }
		}
		query=query+intType+")";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<ExamRevaluationFee> fees=transaction.getDataForQuery(query);
		if(fees!=null && !fees.isEmpty()){
			String courseNames="";
			for (ExamRevaluationFee bo: fees) {
				if(!courseNames.isEmpty())
					courseNames=courseNames+","+bo.getCourse().getName();
				else
					courseNames=bo.getCourse().getName();
			}
			throw new DuplicateException(courseNames);
		}
		List<ExamRevaluationFee> boList=new ArrayList<ExamRevaluationFee>();
		for (int i = 0; i < tempArray.length; i++) {
			ExamRevaluationFee bo=new ExamRevaluationFee();
			Course course=new Course();
			course.setId(Integer.parseInt(tempArray[i]));
			bo.setCourse(course);
			bo.setIsActive(true);
			bo.setCreatedBy(examRevaluationFeeForm.getUserId());
			bo.setModifiedBy(examRevaluationFeeForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			
			if(examRevaluationFeeForm.getRevaluationFees()!=null && !examRevaluationFeeForm.getRevaluationFees().isEmpty())
				bo.setRevaluationFees(new BigDecimal(examRevaluationFeeForm.getRevaluationFees()));
			if(examRevaluationFeeForm.getScrutinyFees()!=null && !examRevaluationFeeForm.getScrutinyFees().isEmpty())
				bo.setScrutinyFees(new BigDecimal(examRevaluationFeeForm.getScrutinyFees()));
			if(examRevaluationFeeForm.getApplicationFees()!=null && !examRevaluationFeeForm.getApplicationFees().isEmpty())
				bo.setApplicationFees(new BigDecimal(examRevaluationFeeForm.getApplicationFees()));

	
			
			bo.setAcademicYear(examRevaluationFeeForm.getAcademicYear());
			boList.add(bo);
		}
		log.info("Exit from addOrUpdate");
		return PropertyUtil.getInstance().saveList(boList);
	}
	
	public boolean deleteOrReactivateRevaluation(int id, String mode, String userId) throws Exception{
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		ExamRevaluationFee bo=(ExamRevaluationFee) transaction.getMasterEntryDataById(ExamRevaluationFee.class, id);

		return PropertyUtil.getInstance().delete(bo);
	}
}

