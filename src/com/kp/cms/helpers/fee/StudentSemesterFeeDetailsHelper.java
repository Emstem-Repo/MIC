package com.kp.cms.helpers.fee;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jxl.write.DateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentSemesterFeeDetails;
import com.kp.cms.bo.exam.StudentUtilBO;
import com.kp.cms.forms.fee.StudentSemesterFeeDetailsForm;
import com.kp.cms.helpers.exam.NewUpdateProccessHelper;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.fee.StudentSemesterFeeDetailsTo;

public class StudentSemesterFeeDetailsHelper {
	
	private static volatile StudentSemesterFeeDetailsHelper studentSemesterFeeDetailsHelper = null;
	private static final Log log = LogFactory.getLog(StudentSemesterFeeDetailsHelper.class);
	private StudentSemesterFeeDetailsHelper() {
		
	}
	/**
	 * return singleton object of NewUpdateProccessHelper.
	 * @return
	 */
	public static StudentSemesterFeeDetailsHelper getInstance() {
		if (studentSemesterFeeDetailsHelper == null) {
			studentSemesterFeeDetailsHelper = new StudentSemesterFeeDetailsHelper();
		}
		return studentSemesterFeeDetailsHelper;
	}
	public List<StudentSemesterFeeDetailsTo> convertBOToTOList(List studentList, StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm2) {
		List<StudentSemesterFeeDetailsTo> list = new ArrayList<StudentSemesterFeeDetailsTo>();
		if(studentList != null && !studentList.isEmpty()){
			Iterator<Student> iterator = studentList.iterator();
			while (iterator.hasNext()) {
				Student bo = iterator.next();
				StudentSemesterFeeDetailsTo to = new StudentSemesterFeeDetailsTo();
				
				to.setRegisterNo(bo.getRegisterNo());
				to.setStudentName(bo.getAdmAppln().getPersonalData().getFirstName());
				to.setClassName(bo.getClassSchemewise().getClasses().getName());
				StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm = new StudentSemesterFeeDetailsForm();
				studentSemesterFeeDetailsForm.setClassName(to.getClassName());
				to.setClassId(String.valueOf(bo.getClassSchemewise().getClasses().getId()));
				to.setStudentId(String.valueOf(bo.getId()));
				to.setSemester(studentSemesterFeeDetailsForm.getSemister());
				
				
				
				
				list.add(to);
			}
		}
		return list;
	}
	public List<StudentSemesterFeeDetails> convertTOtoBo(List<StudentSemesterFeeDetailsTo> toList, StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		List<StudentSemesterFeeDetails> bo = new ArrayList<StudentSemesterFeeDetails>();
		if(toList != null && !toList.isEmpty()){
			Iterator<StudentSemesterFeeDetailsTo> iterator = toList.iterator();
			while(iterator.hasNext()){
				StudentSemesterFeeDetailsTo to = iterator.next();
				StudentSemesterFeeDetails bo1 = new StudentSemesterFeeDetails();
				bo1.setId(to.getId());
				bo1.setUniversityFee(new BigDecimal(to.getUniversityFee()));
				bo1.setSpecialFee(new BigDecimal(to.getSpecialFee()));
				bo1.setOtherFee(new BigDecimal(to.getOtherFee()));
				bo1.setCATrainingFee(new BigDecimal(to.getCATrainingFee()));
				bo1.setSemesterFee(new BigDecimal(to.getSemesterFee()));
				bo1.setRemarks(to.getRemarks());
				bo1.setFeeApprove(to.getFeeApprove());
				Classes classes = new Classes();
				classes.setId(Integer.parseInt(to.getClassId()));
				bo1.setClasses(classes);
				Student student = new Student();
				student.setId(Integer.parseInt(String.valueOf(to.getStudentId())));
				bo1.setStudent(student);
				bo1.setRegisterNo(to.getRegisterNo());
				bo1.setCreatedDate(new Date());
				bo1.setLastModifiedDate(new Date());
				bo1.setSemister(studentSemesterFeeDetailsForm.getSemester());
				bo1.setCreatedBy(studentSemesterFeeDetailsForm.getUserId());
				bo1.setModifiedBy(studentSemesterFeeDetailsForm.getUserId());
				if(to.getDate() != null && !to.getDate().isEmpty()){
				String date = to.getDate();
				java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = df.parse(date);
				bo1.setDate(date1);
				}
				
				
				bo.add(bo1);
			}
		}
		return bo;
	}
	public List<StudentSemesterFeeDetailsTo> convertBOtoTO1(List<StudentSemesterFeeDetails> list,
			StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm) throws Exception {
		List<StudentSemesterFeeDetailsTo> list1 = new ArrayList<StudentSemesterFeeDetailsTo>();
		if(list != null && !list.isEmpty()){
			Iterator<StudentSemesterFeeDetails> iterator = list.iterator();
			while (iterator.hasNext()) {
				StudentSemesterFeeDetails bo = iterator.next();
				StudentSemesterFeeDetailsTo to = new StudentSemesterFeeDetailsTo();
				to.setId(bo.getId());
				to.setRegisterNo(bo.getStudent().getRegisterNo());
				to.setStudentName(bo.getStudent().getAdmAppln().getPersonalData().getFirstName());
				//to.setClassName(bo.getClasses().getName());
				StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm1 = new StudentSemesterFeeDetailsForm();
				studentSemesterFeeDetailsForm.setClassName(to.getClassName());
				to.setClassId(String.valueOf(bo.getClasses().getId()));
				to.setStudentId(String.valueOf(bo.getStudent().getId()));
				to.setSemester(studentSemesterFeeDetailsForm.getSemister());
				
				
				
				
				list1.add(to);
			}
		}
		return list1;
	}
	public List  finalList(List<StudentSemesterFeeDetails> totalList,List approvedList) 
	throws Exception{
		
		Set<Integer> studentList = new HashSet<Integer>(approvedList);
		Iterator<StudentSemesterFeeDetails> itr = totalList.iterator();
		while(itr.hasNext()){
			if(studentList.contains(itr.next().getStudent().getId())){
				itr.remove();
			}
		}
		return totalList;
		
	}
	public List<StudentSemesterFeeDetailsTo> convertBotoTo2(List previousStudentList,StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm)
	throws Exception{
		List<StudentSemesterFeeDetailsTo> list = new ArrayList<StudentSemesterFeeDetailsTo>();
		if(previousStudentList != null && !previousStudentList.isEmpty()){
			Iterator<StudentUtilBO> iterator = previousStudentList.iterator();
			while (iterator.hasNext()) {
				StudentUtilBO bo = iterator.next();
				StudentSemesterFeeDetailsTo to = new StudentSemesterFeeDetailsTo();
				
				to.setRegisterNo(bo.getRegisterNo());
				to.setStudentName(bo.getAdmApplnUtilBO().getPersonalDataUtilBO().getFirstName());
				to.setClassName(studentSemesterFeeDetailsForm.getClassName());
				StudentSemesterFeeDetailsForm studentSemesterFeeDetailsForm2 = new StudentSemesterFeeDetailsForm();
				studentSemesterFeeDetailsForm.setClassName(to.getClassName());
				to.setClassId(studentSemesterFeeDetailsForm.getClassId());
				to.setStudentId(String.valueOf(bo.getId()));
				to.setSemester(studentSemesterFeeDetailsForm.getSemister());
				
				
				
				
				list.add(to);
			}
		}
		return list;
	}
	
}
