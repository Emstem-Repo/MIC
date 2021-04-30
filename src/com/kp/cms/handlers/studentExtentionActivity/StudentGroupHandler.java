package com.kp.cms.handlers.studentExtentionActivity;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.studentExtentionActivity.StudentGroupForm;
import com.kp.cms.helpers.studentExtentionActivity.StudentGroupHelper;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.transactions.studentExtentionActivity.IStudentGroupTransaction;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentGroupTransactionImpl;

public class StudentGroupHandler {

	 private static volatile StudentGroupHandler obj;
	 public static StudentGroupHandler getInstance(){
		  if(obj == null){
			  obj = new StudentGroupHandler();
		  }
		  return obj;
	 }
	 
	    public List<StudentGroupTO> getstudentGroup() throws Exception{
		    IStudentGroupTransaction tx = StudentGroupTransactionImpl.getInstance();
			List<StudentGroup> studentGroup = tx.getStudentGroupDetails();
			List<StudentGroupTO> studentGroupTO = StudentGroupHelper.getInstance().convertBOToTO(studentGroup);
			return studentGroupTO;
	    }
	    public boolean addStudentGroupDetails(StudentGroupForm formgroup, String mode) throws Exception{
			
	        IStudentGroupTransaction tx = StudentGroupTransactionImpl.getInstance();
			boolean isAdded = false;
			boolean originalNotChanged = false;
			String group = "";
			String orgGroup = "";
			
			if(formgroup.getGroupName() != null && !formgroup.getGroupName().equals("")) {
				group = formgroup.getGroupName();
			}
			if(formgroup.getOrgGroup() != null && !formgroup.getOrgGroup().equals(""))	{
				orgGroup = formgroup.getOrgGroup();
			}
			
			if(orgGroup.equalsIgnoreCase(group)) {
				originalNotChanged = true;
			}
			if(mode.equalsIgnoreCase("Add")) {
				originalNotChanged = false;
			}
			
			if(!originalNotChanged){
				StudentGroup dupStudentGroup=StudentGroupHelper.getInstance().convertFormToBO(formgroup);
				dupStudentGroup = tx.isDuplicate(dupStudentGroup);
				
				if(dupStudentGroup != null && dupStudentGroup.getIsActive()) {
					throw new DuplicateException();
				}
				else if(dupStudentGroup != null && !dupStudentGroup.getIsActive()) {
					formgroup.setDupId(dupStudentGroup.getId());
					throw new ReActivateException();
				}
			}
			
			StudentGroup masterForms = StudentGroupHelper.getInstance().convertFormToBO(formgroup);
			if(mode.equalsIgnoreCase("Add")) {
				masterForms.setCreatedBy(formgroup.getUserId());
				masterForms.setCreatedDate(new Date());
				masterForms.setModifiedBy(formgroup.getUserId());
				masterForms.setLastModifiedDate(new Date());
			}
			else {
				masterForms.setModifiedBy(formgroup.getUserId());
				masterForms.setLastModifiedDate(new Date());
			}
			
			isAdded = tx.addStudenGroup(masterForms, mode);
			return isAdded;
			
		}
	    
	    public boolean deleteStudentGroup(int dupId, boolean activate, StudentGroupForm studentGroupForm) throws Exception {
	    	 IStudentGroupTransaction tx = StudentGroupTransactionImpl.getInstance();
			boolean deleted = tx.deleteStudentGroup(dupId, activate, studentGroupForm);
			return deleted;
		}
}
