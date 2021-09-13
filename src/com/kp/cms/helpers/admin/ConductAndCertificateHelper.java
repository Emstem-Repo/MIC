package com.kp.cms.helpers.admin;


import java.util.List;

import sun.security.x509.CertException;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.forms.admin.ConductAndCertificateForm;
import com.kp.cms.to.admin.ConductAndCertificateTO;
import com.kp.cms.transactions.admin.IConductAndCertificateTransaction;
import com.kp.cms.transactionsimpl.admin.ConductAndCertificateTransactionImpl;

public class ConductAndCertificateHelper {
	
	private static volatile ConductAndCertificateHelper obj=null;
	
	public static ConductAndCertificateHelper getInstance(){
		if(obj==null){
			obj = new ConductAndCertificateHelper();
		}
		return obj; 
	}

	public String getStudentDetailQuery(ConductAndCertificateForm certificateForm)throws Exception{
		String query=" from Student s where (s.admAppln.isSelected=1 and s.admAppln.isApproved=1 OR (s.admAppln.isCancelled = 1)) and" + ("R".equals(certificateForm.getSearchBy()) ? " s.registerNo like '" + certificateForm.getRegisterNo() + "'" : " s.admAppln.admissionNumber like '" + certificateForm.getRegisterNo() + "'");
		return query;
	}

	public void convertBotoTo(Student student,ConductAndCertificateForm certificateForm)throws Exception{
		ConductAndCertificateTO certificateTO = new ConductAndCertificateTO();
		IConductAndCertificateTransaction transaction = ConductAndCertificateTransactionImpl.getInstance();
		if(student!=null){
			certificateTO.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
			certificateTO.setRegisterNo(student.getRegisterNo());
			certificateTO.setAdmNo(student.getAdmAppln().getAdmissionNumber());
			certificateTO.setCourseName(student.getClassSchemewise().getClasses().getCourse().getName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			certificateTO.setDob(dateFormat.format(student.getAdmAppln().getPersonalData().getDateOfBirth()));
			List<String> subsidiaries=transaction.getSubsidiaries(student.getId());
			String subsi="";
			for(String sub:subsidiaries){
				if(subsi.isEmpty()){
					subsi=subsi.concat(sub);
				}else{
					subsi=subsi.concat(",").concat(sub);
				}
			}
			certificateTO.setSubsidiaries(subsi);
			int programTypeId=student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId();
			if(programTypeId==1){
				certificateTO.setProgramme("Degree");
			}else{
				certificateTO.setProgramme("Post Graduate");
			}
		}
		certificateForm.setCertificateTO(certificateTO);
		
	}

}
