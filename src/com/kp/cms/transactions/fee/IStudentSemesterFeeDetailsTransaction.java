package com.kp.cms.transactions.fee;

import java.util.List;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentSemesterFeeDetails;
import com.kp.cms.forms.fee.StudentSemesterFeeDetailsForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;

public interface IStudentSemesterFeeDetailsTransaction {

	List getStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	String getClassName(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	boolean save(List<StudentSemesterFeeDetails> bo) throws Exception;

	List<StudentSemesterFeeDetails> getstudList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	List  getApprovedStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	List<StudentSemesterFeeDetails> totalList(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	int getcId(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

	List getPrevStudent(StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception;

}
