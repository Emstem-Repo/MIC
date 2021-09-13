package com.kp.cms.handlers.studentExtentionActivity;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectStream;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.exam.ConsolidatedSubjectStreamForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;
import com.kp.cms.helpers.exam.ConsolidatedSubjectStreamHelper;
import com.kp.cms.helpers.studentExtentionActivity.StudentExtentionHelper;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.transactions.exam.IConsolidatedSubjectStreamTransaction;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionTransaction;
import com.kp.cms.transactionsimpl.exam.ConsolidatedSubjectStreamTransactionImpl;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentExtentionTransactionImpl;

public class StudentExtentionHandler {

private static volatile StudentExtentionHandler obj;
	
	public static StudentExtentionHandler getInstance()
	{
		if(obj == null)
		{
			obj = new StudentExtentionHandler();
		}
		return obj;
	}
	
	public List<StudentExtentionTO> getSubjectActivity() throws Exception{
		IStudentExtentionTransaction tx = StudentExtentionTransactionImpl.getInstance();
		List<StudentExtention> studentExtention = tx.getStudentExtentionDetails();
		List<StudentGroup> studentgroup = tx.getStudentGroupDetails();
		List<StudentExtentionTO> studentExtentionTO = StudentExtentionHelper.getInstance().convertBOToTO(studentExtention);
		return studentExtentionTO;
	}
	
	
	public boolean addStudentExtentionDetails(StudentExtentionForm masterForm, String mode) throws Exception{
		
		IStudentExtentionTransaction tx = StudentExtentionTransactionImpl.getInstance();
		boolean isAdded = false;
		boolean originalNotChanged = false;
		String activityName = "";
		String origActivityName = "";
		
		if(masterForm.getActivityName() != null && !masterForm.getActivityName().equals("")) {
			activityName = masterForm.getActivityName();
		}
		if(masterForm.getOrigActivityName() != null && !masterForm.getOrigActivityName().equals(""))	{
			origActivityName = masterForm.getOrigActivityName();
		}
		
		if(origActivityName.equalsIgnoreCase(activityName)) {
			originalNotChanged = true;
		}
		if(mode.equalsIgnoreCase("Add")) {
			originalNotChanged = false;
		}
		
		if(!originalNotChanged){
			StudentExtention dupStudentExtention=StudentExtentionHelper.getInstance().convertFormToBO(masterForm);
			dupStudentExtention = tx.isDuplicate(dupStudentExtention);
			
			if(dupStudentExtention != null && dupStudentExtention.getIsActive()) {
				throw new DuplicateException();
			}
			else if(dupStudentExtention != null && !dupStudentExtention.getIsActive()) {
				masterForm.setDupId(dupStudentExtention.getId());
				throw new ReActivateException();
			}
		}
		
		StudentExtention masterForms = StudentExtentionHelper.getInstance().convertFormToBO(masterForm);
		if(mode.equalsIgnoreCase("Add")) {
			masterForms.setCreatedBy(masterForm.getUserId());
			masterForms.setCreatedDate(new Date());
			masterForms.setModifiedBy(masterForm.getUserId());
			masterForms.setLastModifiedDate(new Date());
		}
		else {
			masterForms.setModifiedBy(masterForm.getUserId());
			masterForms.setLastModifiedDate(new Date());
		}
		
		isAdded = tx.addStudentExtention(masterForms, mode);
		return isAdded;
		
	}
	
	public boolean deleteSubjectActivity(int dupId, boolean activate, StudentExtentionForm studentExtentionForm) throws Exception {
		IStudentExtentionTransaction tx =  StudentExtentionTransactionImpl.getInstance();
		boolean deleted = tx.deleteStudentExtention(dupId, activate, studentExtentionForm);
		return deleted;
	}
	public List<StudentGroupTO> getStudentGroup()throws Exception {
		IStudentExtentionTransaction tx = StudentExtentionTransactionImpl.getInstance();
		List<StudentGroup> studentgroup = tx.getStudentGroupDetails();
		List<StudentGroupTO> studentGroupTO = StudentExtentionHelper.getInstance().convertTo(studentgroup);
		return studentGroupTO;
	}
}
