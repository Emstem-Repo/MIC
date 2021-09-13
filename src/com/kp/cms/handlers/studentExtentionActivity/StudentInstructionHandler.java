package com.kp.cms.handlers.studentExtentionActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.forms.studentExtentionActivity.StudentInstructionForm;
import com.kp.cms.to.studentExtentionActivity.StudentInstructionTO;
import com.kp.cms.transactions.studentExtentionActivity.IStudentInstructionTransaction;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentInstructionTransactionImpl;

public class StudentInstructionHandler {

	private static final Logger log = Logger.getLogger(StudentInstructionHandler.class);
	public static volatile StudentInstructionHandler feedbackInstructionsHandler = null;
	public static StudentInstructionHandler getInstance(){
		if(feedbackInstructionsHandler ==null){
			feedbackInstructionsHandler = new StudentInstructionHandler();
			return feedbackInstructionsHandler;
		}
		return feedbackInstructionsHandler;
	}
	IStudentInstructionTransaction tx = StudentInstructionTransactionImpl.getInstance();
	
	public List<StudentInstructionTO> getInstructions() throws Exception{
		List<StudentInstructionTO> instructionsTOList = new ArrayList<StudentInstructionTO>();
		List<StudentInstructions> instructions = tx.getInstructions();
		StudentInstructionTO to = null;
		if(instructions!=null){
			Iterator<StudentInstructions> iterator = instructions.iterator();
			while (iterator.hasNext()) {
				StudentInstructions studentFeedbackInstructions = (StudentInstructions) iterator .next();
				to = new StudentInstructionTO();
				to.setId(studentFeedbackInstructions.getId());
				to.setDescription(studentFeedbackInstructions.getDescription());
				instructionsTOList.add(to);
			}
		}
		return instructionsTOList;
	}
	
	public boolean checkDuplicateInstructions( StudentInstructionForm stuFeedbackInstructionsForm)throws Exception {
		boolean isExist = tx.checkDuplicateInstructions(stuFeedbackInstructionsForm);
		return isExist;
	}
	
	public boolean deleteInstructions( StudentInstructionForm stuFeedbackInstructionsForm) throws Exception{
		boolean isDeleted = tx.deleteInstructions(stuFeedbackInstructionsForm);
		return isDeleted;
	}
	public boolean addStuFeedbackInstructions( StudentInstructionForm stuFeedbackInstructionsForm ,String mode)throws Exception {
		StudentInstructions feedbackInstructions = null;
		if(stuFeedbackInstructionsForm.getDescription()!=null && !stuFeedbackInstructionsForm.getDescription().isEmpty()){
			if(mode.equalsIgnoreCase("add")){
				feedbackInstructions = new StudentInstructions();
				feedbackInstructions.setDescription(stuFeedbackInstructionsForm.getDescription());
				feedbackInstructions.setCreatedBy(stuFeedbackInstructionsForm.getUserId());
				feedbackInstructions.setCreatedDate(new Date());
			}else if(mode.equalsIgnoreCase("edit")){
				feedbackInstructions = new StudentInstructions();
				feedbackInstructions.setId(stuFeedbackInstructionsForm.getId());
				feedbackInstructions.setDescription(stuFeedbackInstructionsForm.getDescription());
				feedbackInstructions.setModifiedBy(stuFeedbackInstructionsForm.getUserId());
				feedbackInstructions.setLastModifiedDate(new Date());
			}
		}
		boolean isAdded = tx.addStuFeedbackInstructions(feedbackInstructions,mode);
		return isAdded;
	}
	
	public void editFeedbackInstructions( StudentInstructionForm stuFeedbackInstructionsForm) throws Exception{
		StudentInstructions feedbackInstructions = tx.editFeedbackInstructions(stuFeedbackInstructionsForm);
		if(feedbackInstructions!=null && !feedbackInstructions.toString().isEmpty()){
			stuFeedbackInstructionsForm.setId(feedbackInstructions.getId());
			stuFeedbackInstructionsForm.setDescription(feedbackInstructions.getDescription());
		}
	}
}
