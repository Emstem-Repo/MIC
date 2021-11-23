package com.kp.cms.handlers.admission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.SubjectGroup;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.attendance.SubjectGroupDetailsForm;
import com.kp.cms.helpers.admission.AddOnCourseDetailsHelper;
import com.kp.cms.to.admission.CertificateCourseTO;
import com.kp.cms.to.admission.StudentCertificateCourseTO;
import com.kp.cms.to.attendance.SubjectGroupDetailsTo;
import com.kp.cms.to.exam.ExamSpecializationTO;
import com.kp.cms.transactions.admission.IAddOnCourseDetailsTransaction;
import com.kp.cms.transactionsimpl.admission.AddOnCourseDetailsTransactionImpl;

public class AddOnCourseDetailsHandler {
  public static volatile AddOnCourseDetailsHandler subjectGroupDetailshandler=null;
  private static final Log log = LogFactory.getLog(AddOnCourseDetailsHandler.class);
  static{
	  subjectGroupDetailshandler=new AddOnCourseDetailsHandler();
	  
  }
 public static AddOnCourseDetailsHandler getInstance(){
	 if(subjectGroupDetailshandler == null) {
		 subjectGroupDetailshandler = new AddOnCourseDetailsHandler();
   	  return subjectGroupDetailshandler;
     }
	 return subjectGroupDetailshandler;
 }
 /**This method is used to get the Student Details from the database.
 * @param subjectGroupDetailsForm
 * @return
 * @throws Exception
 */
public List<StudentCertificateCourseTO> getStudentDetails(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception{
	IAddOnCourseDetailsTransaction subjectTxImpl=new AddOnCourseDetailsTransactionImpl();
	 AddOnCourseDetailsHelper subjectDetailsHelper=AddOnCourseDetailsHelper.getInstance();
	 List<Student> studentList=subjectTxImpl.getStudentDetails(subjectGroupDetailsForm);
	 Map<Integer,String> studentWiseSpecializationMap = new HashMap<Integer, String>();
	 List<StudentCertificateCourseTO> studentGroupList= subjectDetailsHelper.convertBoToTO(studentList,new HashMap<Integer, Integer>(),studentWiseSpecializationMap);
	 int halfLength = 0;
		int totLength = studentGroupList.size();
		if (totLength % 2 == 0) {
			halfLength = totLength / 2;
		} else {
			halfLength = (totLength / 2) + 1;
		}
		subjectGroupDetailsForm.setHalfLength(halfLength);
		
	 return studentGroupList;
	 
 }
 
 /**
  * This method is used to add Subject Group details to database.
  * @param SubjectGroupDetailsForm
  * @throws DuplicateException
  * @throws ReActivateException
  * @throws Exception
  */
 
 public boolean addSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm)
	throws DuplicateException, ReActivateException, Exception {
	   log.info("call of addSubjectGroups method in SubjectGroupDetailsHandler class.");
	   boolean isAdded=false;
	   IAddOnCourseDetailsTransaction subjectTxImpl =new AddOnCourseDetailsTransactionImpl();
		isAdded=subjectTxImpl.addSubjectGroups(subjectGroupDetailsForm);
	 log.info("end of addSubjectGroups method in SubjectGroupDetailsHandler class.");
	return isAdded;
  }
 
	/**This method is used to get the SubjectGroups from the database.
	 * @param subjectGroupDetailsForm
	 * @return
	 * @throws Exception
	 */
	public List<SubjectGroupDetailsTo> getSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception {
		log.info("call of getSubjectGroups method in SubjectGroupDetailsHandler class.");
		IAddOnCourseDetailsTransaction subjectGroupTransaction = new AddOnCourseDetailsTransactionImpl();
		List<SubjectGroupDetailsTo> subjectGroupList = AddOnCourseDetailsHelper
				.convertBoToTo(subjectGroupTransaction.getSubjectGroups(subjectGroupDetailsForm));
		log.info("end of getSubjectGroups method in SubjectGroupDetailsHandler class.");
		return subjectGroupList;
	}
	/**This method is used to Edit the Subject Group Details 
	 * @param subjectGroupDetailsForm
	 * @return
	 * @throws Exception
	 */
	public List<StudentCertificateCourseTO> getEditSubjectGroups (SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception{
		log.info("call of getSubjectGroups method in SubjectGroupDetailsHandler class.");
		IAddOnCourseDetailsTransaction subjectGroupTransaction = new AddOnCourseDetailsTransactionImpl();
		 List<Student> studentList=subjectGroupTransaction.getStudentDetails(subjectGroupDetailsForm);
		 Map<Integer,Integer> map=subjectGroupTransaction.getEditSubjectGroup(subjectGroupDetailsForm);
		 Map<Integer,String> studentWiseSpecializationMap = subjectGroupTransaction.getstudentWiseSpecialization(studentList);
		List<StudentCertificateCourseTO> studentDetails=AddOnCourseDetailsHelper.getInstance().convertBoToTO(studentList,map,studentWiseSpecializationMap);
		//List<SubjectGroupDetailsTo> subjectGroupList=subjectGroupTransaction.getEditSubjectGroup();
		return studentDetails;
		
	}
	/**This method is used to get the SubjectGroup Names from the database.
	 * @param subjectGroupDetailsForm
	 * @return
	 * @throws Exception
	 */
	public List<CertificateCourseTO> getSubjectGroupNames(
			SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception{
		IAddOnCourseDetailsTransaction subjectGroupTransaction = new AddOnCourseDetailsTransactionImpl();
		List<CertificateCourseTO> subjectGroupNames=AddOnCourseDetailsHelper.convertBoToTo1(subjectGroupTransaction.getSubjectGroupNames(subjectGroupDetailsForm));
		return subjectGroupNames;
	}
	/**This method is used to update the Subject Groups.
	 * @param subjectGroupDetailsForm
	 * @return
	 * @throws Exception
	 */
	 public boolean updateSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm)
		throws DuplicateException, ReActivateException, Exception {
		   log.info("call of updateSubjectGroups method in SubjectGroupDetailsHandler class.");
		   boolean isUpdate=false;
		   IAddOnCourseDetailsTransaction subjectTxImpl =new AddOnCourseDetailsTransactionImpl();
		   isUpdate=subjectTxImpl.updateSubjectGroups(subjectGroupDetailsForm);
		 log.info("end of addSubjectGroups method in SubjectGroupDetailsHandler class.");
		return isUpdate;
	  }
	/**
	 * @param subjectGroupDetailsForm
	 * @return
	 * @throws Exception
	 */
	public Map<Integer,String> getSubjectGroupsId(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception {
 		IAddOnCourseDetailsTransaction subjectGroupTransaction = new AddOnCourseDetailsTransactionImpl();
		 Map<Integer,String> group=subjectGroupTransaction.getsubjectGroupsId(subjectGroupDetailsForm);
		// List<SubjectGroupDetailsTo> subjectGroupMap=AddOnCourseDetailsHelper.convertBOToTO(group,subjectGroupDetailsForm);
	
		return group;
	}
	/**
	 * @param subjectGroupDetailsForm 
	 * @return
	 * @throws Exception
	 */
	public List<ExamSpecializationTO> getSpecializationData(SubjectGroupDetailsForm subjectGroupDetailsForm)throws Exception {
		IAddOnCourseDetailsTransaction subjectTxImpl =new AddOnCourseDetailsTransactionImpl();
		int classSchemeWiseId = Integer.parseInt(subjectGroupDetailsForm.getClassSchemewiseId());
		List<ExamSpecializationBO> specializationBOs = subjectTxImpl.getSpecializationData(classSchemeWiseId);
		List<ExamSpecializationTO> tos = AddOnCourseDetailsHelper.convertExamSpecializationBOToTO(specializationBOs);
		return tos;
	}
	
}
