package com.kp.cms.handlers.fee;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentSemesterFeeDetails;
import com.kp.cms.bo.exam.StudentUtilBO;
import com.kp.cms.forms.fee.StudentSemesterFeeDetailsForm;
import com.kp.cms.helpers.fee.StudentSemesterFeeDetailsHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;
import com.kp.cms.transactions.fee.IStudentSemesterFeeDetailsTransaction;
import com.kp.cms.transactionsimpl.fee.StudentSemesterFeeDetailsTransactionImpl;

public class StudentSemesterFeeDetailsHandler {
	
	private static StudentSemesterFeeDetailsHandler studentSemesterFeeDetailsHandler = null;
	private static final Log log = LogFactory.getLog(StudentSemesterFeeDetailsHandler.class);
	
	
	public static StudentSemesterFeeDetailsHandler getInstance() {
		if(studentSemesterFeeDetailsHandler == null) {
			studentSemesterFeeDetailsHandler = new StudentSemesterFeeDetailsHandler();
			return studentSemesterFeeDetailsHandler;
		}
		return studentSemesterFeeDetailsHandler;
	}


	public List<StudentSemesterFeeDetailsTo> getStudentList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List  studentList = transaction.getStudent(studentSemesterFeeDetailsForm);
		return StudentSemesterFeeDetailsHelper.getInstance().convertBOToTOList(studentList,studentSemesterFeeDetailsForm);
	}

	public String getClassName(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		String className = transaction.getClassName(studentSemesterFeeDetailsForm);
		return className;
	}

	public boolean save(List<StudentSemesterFeeDetailsTo> toList, StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List<StudentSemesterFeeDetails> bo = StudentSemesterFeeDetailsHelper.getInstance().convertTOtoBo(toList,studentSemesterFeeDetailsForm);
		return transaction.save(bo) ;
	}

	public List<StudentSemesterFeeDetailsTo> getList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List<StudentSemesterFeeDetails> list = transaction.getstudList(studentSemesterFeeDetailsForm);
		
		return StudentSemesterFeeDetailsHelper.getInstance().convertBOtoTO1(list,studentSemesterFeeDetailsForm);
		
	}

	public List<StudentSemesterFeeDetails> totList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List<StudentSemesterFeeDetails> list = transaction.totalList(studentSemesterFeeDetailsForm);
		return list;
	}

	public List  approvedList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List  approvedList = transaction.getApprovedStudent(studentSemesterFeeDetailsForm);
		return approvedList;
	}


	public int getcId(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		int courseid = transaction.getcId(studentSemesterFeeDetailsForm);
		return courseid;
	}


	public List<StudentSemesterFeeDetailsTo> getPreviousStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		IStudentSemesterFeeDetailsTransaction transaction = StudentSemesterFeeDetailsTransactionImpl.getInstance();
		List previousStudentList = transaction.getPrevStudent(studentSemesterFeeDetailsForm);
		return StudentSemesterFeeDetailsHelper.getInstance().convertBotoTo2(previousStudentList,studentSemesterFeeDetailsForm);
	}


	


//	
}
