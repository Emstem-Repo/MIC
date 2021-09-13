package com.kp.cms.helpers.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.PayScaleBO;
import com.kp.cms.bo.admin.ProgramType;
import com.kp.cms.bo.exam.ExamRevaluationFee;
import com.kp.cms.forms.exam.ExamRevaluationFeeForm;
import com.kp.cms.to.employee.PayScaleTO;
import com.kp.cms.to.exam.ExamRevaluationFeeTO;

public class ExamRevaluationFeeHelper {
	private static final Log log = LogFactory.getLog(ExamRevaluationFeeHelper.class);
	public static volatile ExamRevaluationFeeHelper examRevaluation = null;

	public static ExamRevaluationFeeHelper getInstance() {
		if (examRevaluation == null) {
			examRevaluation = new ExamRevaluationFeeHelper();
			return examRevaluation;
		}
		return examRevaluation;
	}

/*	public void setBotoForm(ExamRevaluationFeeForm examRevaluationFeeForm,ExamRevaluationFee revaluation){
	    	if(revaluation!=null){
	    		examRevaluationFeeForm.setProgramTypeId(String.valueOf(revaluation.getProgramType().getId()));
	    		examRevaluationFeeForm.setType(revaluation.getType());
	    		examRevaluationFeeForm.setAmount(revaluation.getAmount().toString());
	    	}
	    }*/
	 /**
	 * @param revaluationFee
	 * @return
	 */
	public List<ExamRevaluationFeeTO> convertBotoToListRevaluation( List<ExamRevaluationFee> boList) throws Exception {
		List<ExamRevaluationFeeTO> toList=new ArrayList<ExamRevaluationFeeTO>();
		ExamRevaluationFeeTO to=null;
		for (ExamRevaluationFee bo : boList) {
			to=new ExamRevaluationFeeTO();
			to.setId(bo.getId());
			to.setCourseName(bo.getCourse().getName());
			if(bo.getRevaluationFees()!=null)
			to.setRevaluationFees(bo.getRevaluationFees().doubleValue());
			if(bo.getScrutinyFees()!=null)
			to.setScrutinyFees(bo.getScrutinyFees().doubleValue());
	
			if(bo.getApplicationFees()!=null)
			to.setApplicationFees(bo.getApplicationFees().doubleValue());
			
			if(bo.getChallengeValuationFees()!=null)
				to.setChallengeValuationFees(bo.getChallengeValuationFees().doubleValue());
	
		
			to.setAcademicYear(bo.getAcademicYear());
			toList.add(to);
		}
		return toList;
	}
}
