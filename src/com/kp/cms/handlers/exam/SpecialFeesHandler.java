package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.exam.SpecialFeesForm;
import com.kp.cms.helpers.exam.SpecialFeesHelper;
import com.kp.cms.to.exam.SpecialFeesTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.ISpecialFeesTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.SpecialFeesTransactionImpl;
import com.kp.cms.utilities.PropertyUtil;

public class SpecialFeesHandler {
	private static volatile SpecialFeesHandler specialFeesHandler= null;
	private static final Log log = LogFactory.getLog(SpecialFeesHandler.class);
	private SpecialFeesHandler() {
		
	}
	
	public static SpecialFeesHandler getInstance() {
		if (specialFeesHandler == null) {
			specialFeesHandler = new SpecialFeesHandler();
		}
		return specialFeesHandler;
	}

	public List<SpecialFeesTO> getActiveList()  throws Exception{
		String query = "from SpecialFeesBO s where s.isActive = 1";
		ISpecialFeesTransaction txn = SpecialFeesTransactionImpl.getInstance();
		List<SpecialFeesBO> boList = txn.getList(query);
		return SpecialFeesHelper.getInstance().convertBOToTO(boList);
	}

	public boolean addOrUpdateRegularFee(SpecialFeesForm specialFeesForm) throws Exception {
		log.info("Entered into addOrUpdate");
		String query="from SpecialFeesBO r where r.isActive=1 and r.academicYear="+specialFeesForm.getAcademicYear()+" and r.classes.id in(";
		StringBuilder intType =new StringBuilder();
		String[] tempArray=specialFeesForm.getSelectedClasses();
		for(int i=0;i<tempArray.length;i++){
			intType.append(tempArray[i]);
			 if(i<(tempArray.length-1)){
				 intType.append(",");
			 }
		}
		query=query+intType+")";
		ISpecialFeesTransaction txn = SpecialFeesTransactionImpl.getInstance();
		List<SpecialFeesBO> fees=txn.getList(query);
		if(fees!=null && !fees.isEmpty()){
			String classNames="";
			for (SpecialFeesBO bo: fees) {
				if(!classNames.isEmpty())
					classNames=classNames+","+bo.getClasses().getName();
				else
					classNames=bo.getClasses().getName();
			}
			throw new DuplicateException(classNames);
		}
		List<SpecialFeesBO> boList=new ArrayList<SpecialFeesBO>();
		for (int i = 0; i < tempArray.length; i++) {
			SpecialFeesBO bo=new SpecialFeesBO();
			Classes classes=new Classes();
			classes.setId(Integer.parseInt(tempArray[i]));
			bo.setClasses(classes);
			bo.setIsActive(true);
			bo.setCreatedBy(specialFeesForm.getUserId());
			bo.setModifiedBy(specialFeesForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setTutionFees(new BigDecimal(specialFeesForm.getTuitionFees()));
			if(!specialFeesForm.getSpecialFees().isEmpty())
				bo.setSpecialFees(new BigDecimal(specialFeesForm.getSpecialFees()));
			if(!specialFeesForm.getApplicationFees().isEmpty())
				bo.setApplicationFees(new BigDecimal(specialFeesForm.getApplicationFees()));
			if(!specialFeesForm.getLateFineFees().isEmpty())
				bo.setLateFineFees(new BigDecimal(specialFeesForm.getLateFineFees()));
			
			bo.setAcademicYear(specialFeesForm.getAcademicYear());
			boList.add(bo);
		}
		log.info("Exit from addOrUpdate");
		return PropertyUtil.getInstance().saveList(boList);
	}

	public boolean deleteOrReactivateRegular(int id, String mode, String userId) throws Exception {
		ISpecialFeesTransaction txn = SpecialFeesTransactionImpl.getInstance();
		SpecialFeesBO bo=(SpecialFeesBO) txn.getMasterEntryDataById(SpecialFeesBO.class, id);
		return PropertyUtil.getInstance().delete(bo);
	}
}
