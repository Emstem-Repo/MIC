package com.kp.cms.handlers.usermanagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.usermanagement.OptionalCourseApplication;
import com.kp.cms.forms.usermanagement.OptionalCourseApplicationForm;
import com.kp.cms.helpers.usermanagement.OptionalCourseApplicationHelper;
import com.kp.cms.to.usermanagement.OptionalCourseApplicationTO;
import com.kp.cms.transactions.usermanagement.IOptionalCourseSubjectApplication;
import com.kp.cms.transactionsimpl.usermanagement.OptionalCourseApplicationTransactionImpl;


public class OptionalCourseApplicationHandler {
	private static volatile OptionalCourseApplicationHandler optionalCourseApplicationHandler = null;
    private OptionalCourseApplicationHandler(){
    	
    }
	public static OptionalCourseApplicationHandler getInstance() {
		if (optionalCourseApplicationHandler == null) {
			optionalCourseApplicationHandler = new OptionalCourseApplicationHandler();
		}
		return optionalCourseApplicationHandler;
	}
	
	public List<OptionalCourseApplicationTO> getOptionalCourseSubjects(OptionalCourseApplicationForm form) throws Exception{
		boolean exists = false;
		IOptionalCourseSubjectApplication txn = OptionalCourseApplicationTransactionImpl.getInstance();
		List<Subject> boList = txn.getOptionalCourseSubjects(form);
		List<OptionalCourseApplicationTO> toList = OptionalCourseApplicationHelper.getInstance().convertBoToTo(boList, form);
		exists = txn.checkDuplicates(Integer.parseInt(form.getStudentId()), Integer.parseInt(form.getClassId()));
		if(exists)
			form.setIsSubmitted(true);
		
		
		
		
		return toList;
	}

	public boolean saveApplication(OptionalCourseApplicationForm applicationForm) throws Exception{
		IOptionalCourseSubjectApplication txn = OptionalCourseApplicationTransactionImpl.getInstance();
		List<OptionalCourseApplication> boList = OptionalCourseApplicationHelper.getInstance().convertFormToBo(applicationForm);
		boolean isAdded = txn.saveApplication(boList);
		return true;
	}
}
