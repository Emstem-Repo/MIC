package com.kp.cms.handlers.admission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.CharacterAndConduct;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admission.TCDetailsForm;
import com.kp.cms.helpers.admission.TCDetailsHelper;
import com.kp.cms.to.admission.BoardDetailsTO;
import com.kp.cms.to.admission.CharacterAndConductTO;
import com.kp.cms.to.exam.ExamDefinitionTO;
import com.kp.cms.transactions.admission.ITCDetailsTransaction;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactionsimpl.admission.TCDetailsTransactionImpl;
import com.kp.cms.transactionsimpl.admission.TransferCertificateTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.AcademicyearTransactionImpl;
import com.kp.cms.utilities.PropertyUtil;

public class TCDetailsHandler {
	/**
	 * Singleton object of TCDetailsHandler
	 */
	private static volatile TCDetailsHandler tCDetailsHandler = null;
	private static final Log log = LogFactory.getLog(TCDetailsHandler.class);
	private TCDetailsHandler() {
		
	}
	/**
	 * return singleton object of TCDetailsHandler.
	 * @return
	 */
	public static TCDetailsHandler getInstance() {
		if (tCDetailsHandler == null) {
			tCDetailsHandler = new TCDetailsHandler();
		}
		return tCDetailsHandler;
	}
	/**
	 * @param tcDetailsForm
	 * @return
	 * @throws Exception
	 */
	public List<BoardDetailsTO> getListOfCandidates(TCDetailsForm tcDetailsForm) throws Exception {
		log.info("Entered into getListOfCandidates- TCDetailsHandler");
		
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		String query=TCDetailsHelper.getInstance().getSearchQuery(tcDetailsForm);
		List<Student> studentList=transaction.getStudentDetails(query);
		
		log.info("Exit from getListOfCandidates- TCDetailsHandler");
		return TCDetailsHelper.getInstance().convertBotoToList(studentList);
	}
	/**
	 * @param tcDetailsForm
	 * @throws Exception
	 */
	public void getStudentTCDetails(TCDetailsForm tcDetailsForm) throws Exception {
		log.info("Entered into getStudentTCDetails- BoardDetailsHandler");
		
		String query=TCDetailsHelper.getInstance().getSearchQueryForTCDetails(tcDetailsForm);
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		Student student=transaction.getStudentTCDetails(query);
		if(student == null) {
			throw new DataNotFoundException();
		}
		TCDetailsHelper.getInstance().convertBotoForm(student,tcDetailsForm);
		
		log.info("Exit from getStudentTCDetails- TCDetailsHandler");
	}
	/**
	 * @param tcDetailsForm
	 * @return
	 * @throws Exception
	 */
	public boolean updateStudentTCDetails(TCDetailsForm tcDetailsForm) throws Exception {
		StudentTCDetails bo=TCDetailsHelper.getInstance().convertTotoBo(tcDetailsForm);
		AcademicyearTransactionImpl acadTxn = new AcademicyearTransactionImpl();
		tcDetailsForm.setAcademicYear(String.valueOf(acadTxn.getCurrentAcademicYear()));
		ITransferCertificateTransaction txn = TransferCertificateTransactionImpl.getInstance();
		TCNumber tcNumber = txn.getTCNumber(Integer.parseInt(tcDetailsForm.getAcademicYear()), "TC", tcDetailsForm.getCourseId()).get(0);
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		return transaction.saveStudentTCDetails(bo, tcNumber, Integer.parseInt(tcDetailsForm.getAcademicYear()), tcDetailsForm.getUserId());
	}
	/**
	 * @return
	 * @throws Exception
	 */
	public List<CharacterAndConductTO> getCharacterAndConductList() throws Exception {
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		List<CharacterAndConduct> list=transaction.getAllCharacterAndConduct();
		return TCDetailsHelper.getInstance().convertBoListToTOList(list);
	}
	public Boolean getUpdateStatus(TCDetailsForm tcDetailsForm) throws Exception {
	ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
	return transaction.updateStudentTCDetails(tcDetailsForm);
	}
	
	public List<Object[]> getExamNames(TCDetailsForm tcDetailsForm) {
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		List<Object[]> examNames = transaction.getExamNames(tcDetailsForm);
		return examNames;
	}
	@SuppressWarnings("unchecked")
	public List<ExamDefinitionTO> convertExamNamesToForm(List<Object[]> examNames) {
		List<ExamDefinitionTO> examList = new ArrayList<ExamDefinitionTO>();
		if(examNames!=null)
		{
			Iterator itr = examNames.iterator();
			while(itr.hasNext()){
				ExamDefinitionTO to = new ExamDefinitionTO();
				Object[] data = (Object[]) itr.next();
				if(data[0]!=null)				
					to.setId(Integer.parseInt(data[0].toString()));
				if(data[1]!=null)				
					to.setExamName(data[1].toString());
				examList.add(to);
			}
		}
		return examList;
	}
	public void getAllStudentTCDetailsByClass(TCDetailsForm tcDetailsForm) throws Exception {
		log.info("Entered into getStudentTCDetails- BoardDetailsHandler");
		
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		List<Object[]> subjects = transaction.getSubjectsForAllStudentsByClass(tcDetailsForm);
		ExamDefinitionBO bo = transaction.getExamForAllStudentsByClass(tcDetailsForm); 		
		TCDetailsHelper.getInstance().convertBotoFormAllStudentsByClass(subjects,bo,tcDetailsForm);		
		
		log.info("Exit from getStudentTCDetails- TCDetailsHandler");
	}
	
	public boolean cancelTCPrint(TCDetailsForm tcDetailsForm) throws Exception {
		boolean updated= false;
		String query="from Student s where s.registerNo='"+tcDetailsForm.getRegisterNo()+"' and s.tcNo is not null" ;
		Student student = (Student) PropertyUtil.getDataForUniqueObject(query);
		if(student!=null && student.getTcNo()!=null){
			student.setTcNo(null);
			student.setTcDate(null);
			student.setSlNo(null);
			ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
			updated = transaction.updateStudentTCNo(student);
		}
		return updated;
	}
}
