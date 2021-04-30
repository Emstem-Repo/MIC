package com.kp.cms.transactionsimpl.studentExtentionActivity;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.studentExtentionActivity.StudentInstructionForm;
import com.kp.cms.transactions.studentExtentionActivity.IStudentInstructionTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class StudentInstructionTransactionImpl implements IStudentInstructionTransaction{
	
	public static volatile StudentInstructionTransactionImpl feedbackInstructionsTrasImpl = null;
	public static StudentInstructionTransactionImpl getInstance(){
		if(feedbackInstructionsTrasImpl == null){
			feedbackInstructionsTrasImpl = new StudentInstructionTransactionImpl();
			return feedbackInstructionsTrasImpl;
		}
		return feedbackInstructionsTrasImpl;
	}
	@Override
	public List<StudentInstructions> getInstructions() throws Exception {
		Session session = null;
		List<StudentInstructions> feedbackInstructions;
		try{
			session = HibernateUtil.getSession();
			feedbackInstructions = session.createQuery("from StudentInstructions instructions").list();
		}catch (Exception e) {
			throw new ApplicationException(e);
			}
		finally {
			if (session != null) {
				session.flush();
			}
		}
		return feedbackInstructions;
	}
	@Override
	public boolean checkDuplicateInstructions(
			StudentInstructionForm stuFeedbackInstructionsForm)
			throws Exception {
		Session session = null;
		boolean isExist = false;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("select instructions.description from StudentInstructions instructions" );
			List<StudentInstructions> list = query.list();
			if(list!=null && !list.isEmpty() && list.contains(stuFeedbackInstructionsForm.getDescription())){
				isExist = true;
			}
		}
		catch (Exception e) {
			isExist = false;
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return isExist;
	}
	@Override
	public boolean addStuFeedbackInstructions(
			StudentInstructions feedbackInstructions, String mode)
			throws Exception {
		Session session = null;
		boolean isAdded = false;
		Transaction txTransaction = null;
		try{
			session = HibernateUtil.getSession();
			txTransaction = session.beginTransaction();
			txTransaction.begin();
			if(mode.equalsIgnoreCase("add")){
				session.save(feedbackInstructions);
			}else if(mode.equalsIgnoreCase("edit")){
				session.update(feedbackInstructions);
			}
			txTransaction.commit();
			isAdded = true;
		}catch (Exception e) {
			isAdded = false;
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return isAdded;
	}
	@Override
	public StudentInstructions editFeedbackInstructions(
			StudentInstructionForm stuFeedbackInstructionsForm)
			throws Exception {
		Session session =null;
		StudentInstructions instructions;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from StudentInstructions inst where inst.id="+stuFeedbackInstructionsForm.getId());
			instructions = (StudentInstructions) query.uniqueResult();
		}catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return instructions;
	}
	@Override
	public boolean deleteInstructions(
			StudentInstructionForm stuFeedbackInstructionsForm)
			throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isDeleted = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			StudentInstructions instructions = (StudentInstructions)session.get(StudentInstructions.class, stuFeedbackInstructionsForm.getId());
			session.delete(instructions);
			tx.commit();
			isDeleted = true;
		}catch (Exception e) {
			isDeleted = false;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return isDeleted;
	}

}
