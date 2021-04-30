package com.kp.cms.transactions.studentExtentionActivity;

import java.util.List;

import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.forms.admin.StudentFeedbackInstructionsForm;
import com.kp.cms.forms.studentExtentionActivity.StudentInstructionForm;

public interface IStudentInstructionTransaction {
	public List<StudentInstructions> getInstructions()throws Exception;
	public boolean deleteInstructions( StudentInstructionForm stuFeedbackInstructionsForm)throws Exception;
	public boolean checkDuplicateInstructions( StudentInstructionForm stuFeedbackInstructionsForm)throws Exception;
	public StudentInstructions editFeedbackInstructions( StudentInstructionForm stuFeedbackInstructionsForm)throws Exception;
	public boolean addStuFeedbackInstructions( StudentInstructions feedbackInstructions,String mode)throws Exception;
}
