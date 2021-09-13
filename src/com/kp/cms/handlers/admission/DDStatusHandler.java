package com.kp.cms.handlers.admission;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePreferenceEntranceDetails;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.forms.admission.ApplicationEditForm;
import com.kp.cms.forms.admission.DDStatusForm;
import com.kp.cms.helpers.admission.ApplicationEditHelper;
import com.kp.cms.helpers.admission.DDStatusHelper;
import com.kp.cms.to.admin.CandidatePreferenceEntranceDetailsTO;
import com.kp.cms.to.admission.DDStatusTO;
import com.kp.cms.transactions.admission.IApplicationEditTransaction;
import com.kp.cms.transactions.admission.IDDStatusTransaction;
import com.kp.cms.transactionsimpl.admission.ApplicationEditTransactionimpl;
import com.kp.cms.transactionsimpl.admission.DDStatusTransactionImpl;

public class DDStatusHandler {
	
	IDDStatusTransaction transaction=DDStatusTransactionImpl.getInstance();
	/**
	 * Singleton object of DDStatusHandler
	 */
	private static DDStatusHandler dDStatusHandler = null;
	private static final Log log = LogFactory.getLog(DDStatusHandler.class);
	private DDStatusHandler() {
		
	}
	/**
	 * return singleton object of DDStatusHandler.
	 * @return
	 */
	public static DDStatusHandler getInstance() {
		if (dDStatusHandler == null) {
			dDStatusHandler = new DDStatusHandler();
		}
		return dDStatusHandler;
	}
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean updateDDStatus(DDStatusForm dDStatusForm) throws Exception {
		AdmAppln bo=transaction.updateStatus(DDStatusHelper.getInstance().getAlreadyEnteredQuery(dDStatusForm),dDStatusForm);
		return DDStatusHelper.getInstance().sendMailToStudent(bo);
	}
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean getAlreadyEntered(DDStatusForm dDStatusForm) throws Exception{
		String query=DDStatusHelper.getInstance().getAlreadyEnteredQuery(dDStatusForm);
		return transaction.getAlreadyEntered(query);
	}
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean checkStudent(DDStatusForm dDStatusForm) throws Exception {
		String query=DDStatusHelper.getInstance().getAlreadyEnteredQuery(dDStatusForm);
		return transaction.checkStudent(query);
	}
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean checkDDExists(DDStatusForm dDStatusForm) throws Exception {
		String query=DDStatusHelper.getInstance().checkDDExistsQuery(dDStatusForm);
		return transaction.checkStudent(query);
	}
	
	
	
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean updateChallanStatus(DDStatusForm dDStatusForm) throws Exception {
		AdmAppln bo=transaction.updateStatus1(DDStatusHelper.getInstance().getAlreadyEnteredQuery(dDStatusForm),dDStatusForm);
		return DDStatusHelper.getInstance().sendMailToStudent1(bo);
	}
	
	
	/**
	 * @param dDStatusForm
	 * @return
	 * @throws Exception
	 */
	public boolean checkChallanExists(DDStatusForm dDStatusForm) throws Exception {
		String query=DDStatusHelper.getInstance().checkChallanExistsQuery(dDStatusForm);
		return transaction.checkStudent(query);
	}
	
	
	
	
	// get students for challan
	public List<DDStatusTO> getStudentsChallanStatusOnCourse(DDStatusForm ddForm) throws Exception {
		// TODO Auto-generated method stub
	
		
		List<AdmAppln> studentList =transaction.getStudentsChallanStatusOnCourse(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTO2(studentList);
		
		
		
		return list;
		
	}
	
	
	public boolean updateChallanStatusOnCourse(DDStatusForm ddForm) throws Exception {
		 boolean isAdded=false;
		   
			   isAdded=transaction.updateChallanStatusOnCourse(ddForm);
		   
			
			return isAdded;
	}
	
	// get students for dd
	public List<DDStatusTO> getStudentsDDStatusOnCourse(DDStatusForm ddForm) throws Exception {
		// TODO Auto-generated method stub
	
		
		List<AdmAppln> studentList =transaction.getStudentsDDStatusOnCourse(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTO3(studentList);
		
		
		
		return list;
		
	}
	
	
	public boolean updateDDStatusOnCourse(DDStatusForm ddForm) throws Exception {
		 boolean isAdded=false;
		   
			   isAdded=transaction.updateDDStatusOnCourse(ddForm);
		   
			
			return isAdded;
	}
	
	
	// get students for dd
	public List<DDStatusTO> getStudentsChallanDtailsOnDate(DDStatusForm ddForm) throws Exception {
		// TODO Auto-generated method stub
	
		
		List<AdmAppln> studentList =transaction.getStudentsChallanDtailsOnDate(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTO4(studentList);
		
		
		
		return list;
		
	}
	
	
	public boolean updateChallanUploadProcess(DDStatusForm ddForm) throws Exception {
		 boolean isAdded=false;
		   
			   isAdded=transaction.updateChallanUploadProcess(ddForm);
		   
			return isAdded;
	}
	
	public List<DDStatusTO> getStudentsChallanStatusOnCourseForExam(DDStatusForm ddForm) throws Exception {
		// TODO Auto-generated method stub
	
		
		List<ExamRegularApplication> studentList =transaction.getStudentsChallanStatusOnCourseForExam(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTO5(studentList);
		
		
		
		return list;
		
	}
	
	public boolean updateChallanStatusOnCourseForExam(DDStatusForm ddForm) throws Exception {
		 boolean isAdded=false;
		   
			   isAdded=transaction.updateChallanStatusOnCourseForExam(ddForm);
		   
			
			return isAdded;
	}
	
	// get students for dd
	public List<DDStatusTO> getStudentsChallanDtailsOnDateForExam(DDStatusForm ddForm) throws Exception {
		// TODO Auto-generated method stub
	
		
		List<ExamRegularApplication> studentList =transaction.getStudentsChallanDtailsOnDateForExam(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTO6(studentList);
		
		
		
		return list;
		
	}
	public boolean updateChallanUploadProcessForExam(DDStatusForm ddForm) throws Exception {
		 boolean isAdded=false;
		   
			   isAdded=transaction.updateChallanUploadProcessForExam(ddForm);
		   
			return isAdded;
	}
	
	public Integer ChallanVerifiedCount(DDStatusForm ddForm) throws Exception
	{
		return transaction.ChallanVerifiedCount(ddForm);
	}
	public Integer ChallanNotVerifiedCount(DDStatusForm ddform) throws Exception
	{
		return transaction.ChallanNotVerifiedCount(ddform);
	}
	public List<DDStatusTO> getStudentsChallanStatusOnCourseForSupplExam(DDStatusForm ddForm) throws Exception {
		List<ExamSupplementaryImprovementApplicationBO> studentList =transaction.getStudentsChallanStatusOnCourseForSupplExam(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTOForSuppl(studentList);
		
		return list;
		
	}
	public boolean updateChallanStatusOnCourseForSupplExam(DDStatusForm ddForm) throws Exception {
		boolean isAdded=false;
		isAdded=transaction.updateChallanStatusOnCourseForSupplExam(ddForm);
		return isAdded;
	}
	
	// get students for dd
	public List<DDStatusTO> getStudentsChallanDtailsOnDateForSupplExam(DDStatusForm ddForm) throws Exception {
		List<ExamSupplementaryApplication> studentList =transaction.getStudentsChallanDtailsOnDateForSupplExam(ddForm);
		List<DDStatusTO> list = DDStatusHelper.getInstance().copyBoToTOForSupplyChallanUploadProcess(studentList);
		return list;
	}
	
	public boolean updateChallanUploadProcessForSupplExam(DDStatusForm ddForm) throws Exception {
		boolean isAdded=false;
		isAdded=transaction.updateChallanUploadProcessForSupplExam(ddForm);
		return isAdded;
	}
}
