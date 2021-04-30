package com.kp.cms.handlers.admin;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admin.ConductAndCertificateForm;
import com.kp.cms.helpers.admin.ConductAndCertificateHelper;
import com.kp.cms.transactions.admission.ITCDetailsTransaction;
import com.kp.cms.transactionsimpl.admission.TCDetailsTransactionImpl;

public class ConductAndCertificateHandler {
	
	private static volatile ConductAndCertificateHandler obj=null;
	
	public static ConductAndCertificateHandler getInstance(){
		if(obj==null){
			obj= new ConductAndCertificateHandler();
		}
		return obj;
	}

	public void getStudentDetails(ConductAndCertificateForm certificateForm)throws Exception {
		
		String query=ConductAndCertificateHelper.getInstance().getStudentDetailQuery(certificateForm);
		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		Student student=transaction.getStudentTCDetails(query);
		if(student==null){
			throw new DataNotFoundException();
		}
		ConductAndCertificateHelper.getInstance().convertBotoTo(student,certificateForm);
	}

}
