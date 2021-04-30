package com.kp.cms.handlers.admission;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admission.TransferCertificateForm;
import com.kp.cms.helpers.admission.TransferCertificateHelper;
import com.kp.cms.to.admission.PrintTcDetailsTo;
import com.kp.cms.transactions.admin.ITCMasterTransaction;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactionsimpl.admin.TCMasterTransactionImpl;
import com.kp.cms.transactionsimpl.admission.TransferCertificateTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.AcademicyearTransactionImpl;

public class TransferCertificateHandler 
{
	public static volatile TransferCertificateHandler certificateHandler=null;
	
	public static TransferCertificateHandler getInstance()
	{
		if(certificateHandler==null)
			certificateHandler=new TransferCertificateHandler();
		return certificateHandler;
	}
	ITransferCertificateTransaction certificateTransaction=TransferCertificateTransactionImpl.getInstance();
	ITCMasterTransaction  masterTransactionImpl=TCMasterTransactionImpl.getInstance();
	
	public List<PrintTcDetailsTo> getStudentsByClass(HttpServletRequest request,TransferCertificateForm form) throws Exception {
		List<PrintTcDetailsTo> studentList = new ArrayList<PrintTcDetailsTo>();
		List<Student>  studentBoList = certificateTransaction.getStudentList(form);
		if(studentBoList.isEmpty()) {
			throw new DataNotFoundException();
		}
		if(form.getTcFor()==null)
			form.setTcFor("MIC");
		int academicYear = AcademicyearTransactionImpl.getInstance().getCurrentAcademicYearforTeacher();
		form.setAcademicYear(String.valueOf(academicYear));
		List<TCNumber> number=certificateTransaction.getTCNumber(academicYear,
																 "TC",
																 String.valueOf(studentBoList.get(0).getAdmAppln().getCourseBySelectedCourseId().getId()));
		List<Integer> discontinuedStudentId=certificateTransaction.getDiscontinuedStudentId();
		if(number!=null && number.size()>0)
		{	
			List<Student> studentsTakenTcList = new ArrayList<Student>();
			studentList=TransferCertificateHelper.getInstance().convertStudentBoToTo(studentBoList,studentsTakenTcList,request,form,discontinuedStudentId);
			if(studentsTakenTcList.size()!=0)
			{
				//	commented by Arun Sudhakaran because TC number for a student is set while entering his TC details
				/*certificateTransaction.updateStudentsTcNo(studentsTakenTcList);
				for(TCNumber bo:number)
					masterTransactionImpl.addTCMaster(bo,"update");*/
			}
		}	
		return studentList;
	}

	public String getOldClassNameByStudentId(int studentId)throws Exception 
	{
		String oldClassName=certificateTransaction.getOldClassNameByStudentId(studentId);
		return oldClassName;
	}

	public List<PrintTcDetailsTo> getStudentsByRegNo(String reg,HttpServletRequest request, TransferCertificateForm form) throws Exception
	{
		List<Student> studentBo=certificateTransaction.getStudentsByRegNo(reg,form.getToUsn());
		List<Integer> discontinuedStudentsList=certificateTransaction.getDiscontinuedStudentId();
		List<PrintTcDetailsTo>studentList=TransferCertificateHelper.getInstance().convertStudentBoToToForReprint(studentBo,request, discontinuedStudentsList,form);
		Collections.sort(studentList);
		return studentList;
	}
	
	public List<PrintTcDetailsTo> getStudentsByClassForReprint(HttpServletRequest request,TransferCertificateForm form) throws Exception
	{
		List<Integer> discontinuedStudentsList=certificateTransaction.getDiscontinuedStudentId();
		List<Student> studentBo=certificateTransaction.getStudentListForReprint(form.getClasses(),form.getFromUsn(),form.getToUsn(),form.getIncludeFail(),form.getTcType());
		List<PrintTcDetailsTo>studentList=TransferCertificateHelper.getInstance().convertStudentBoToToForReprint(studentBo,request, discontinuedStudentsList,form);
		Collections.sort(studentList);
		return studentList;
	}

	public List<PrintTcDetailsTo> getStudentsByClassForCjc(TransferCertificateForm form,HttpServletRequest request) throws Exception
	{
		List<PrintTcDetailsTo>studentList=new ArrayList<PrintTcDetailsTo>();
		List<Student>studentBoList=certificateTransaction.getStudentListForCjc(form.getClasses(),form.getFromUsn(),form.getToUsn(),form.getTcType(),form.getIncludeFail());
		if(form.getTcType().equalsIgnoreCase("Duplicate") || form.getTcType().equalsIgnoreCase("Duplicate(Discontinued)"))
			form.setDuplicate("yes");
		else
			form.setDuplicate("no");
		List<TCNumber> number=certificateTransaction.getTCNumber(Integer.parseInt(form.getYear()),form.getTcFor(),form.getClasses());
		List<Integer> discontinuedStudentId=certificateTransaction.getDiscontinuedStudentId();
		if(number!=null && number.size()>0)
		{	
			List<Student> studentsTakenTcList=new ArrayList<Student>();
			studentList=TransferCertificateHelper.getInstance().convertStudentBoToTo(studentBoList,studentsTakenTcList,request,form,discontinuedStudentId);
			/*if(studentsTakenTcList.size()!=0)
			{
				certificateTransaction.updateStudentsTcNo(studentsTakenTcList);
				for(TCNumber bo:number)
					masterTransactionImpl.addTCMaster(bo,"update");
			}*/
		}	
		Collections.sort(studentList);
		return studentList;
	}

	/**
	 * @param request
	 * @param certificateForm
	 * @return
	 * @throws Exception
	 */
	public List<PrintTcDetailsTo> getTCForStudentsByClass(HttpServletRequest request, TransferCertificateForm certificateForm,TCNumber number) throws Exception{
		String query=TransferCertificateHelper.getInstance().getQueryForInputSearch(certificateForm);
		@SuppressWarnings("unused")
		List<Student> studentBoList=certificateTransaction.getStudentListByInputQuery(query);
		return null;
		//return TransferCertificateHelper.getInstance().getPrintTCDetails(studentBoList,number);
	}
	public Integer getAdmittedSemester(int studentId)throws Exception 
	{
		int oldClassId =certificateTransaction.getAdmittedSemester(studentId);
		return oldClassId;
	}
	
	public String getOldClassNameByStudentIdNEW(int studentId, int Sem)throws Exception 
	{
		String oldClassName =certificateTransaction.getOldClassNameByStudentIdNew(studentId, Sem);
		return oldClassName;
	}
	public String getCourseScheme(int courseId, Integer appliedYear)throws Exception{
		return certificateTransaction.getCourseSchemeId(courseId, appliedYear);
	}

	public List<PrintTcDetailsTo> getStudentsByClassOnlyTC(HttpServletRequest request,TransferCertificateForm form)throws Exception 
	{
		List<PrintTcDetailsTo> studentList=new ArrayList<PrintTcDetailsTo>();
		List<Student>  studentBoList=certificateTransaction.getStudentList(form);
		if(form.getTcFor()==null)
			form.setTcFor("MIC");
		List<TCNumber> number=certificateTransaction.getTCNumber(Integer.parseInt(form.getYear()),form.getTcFor(),form.getClasses());
		List<Integer> discontinuedStudentId=certificateTransaction.getDiscontinuedStudentId();
		if(number!=null && number.size()>0)
		{	
			List<Student>studentsTakenTcList=new ArrayList<Student>();
			studentList=TransferCertificateHelper.getInstance().convertStudentBoToToOnlyTC(studentBoList,number,studentsTakenTcList,request,form,discontinuedStudentId);
			if(studentsTakenTcList.size()!=0 && !form.getDuplicate().equalsIgnoreCase("yes"))
			{
				/*certificateTransaction.updateStudentsTcNo(studentsTakenTcList);
				for(TCNumber bo:number)
					masterTransactionImpl.addTCMaster(bo,"update");*/
			}
		}	
		Collections.sort(studentList);
		return studentList;
	}
	public BigInteger getRejoinYear(int studentId)throws Exception {
		return certificateTransaction.getRejoinYear(studentId);
	}
		
	public List<PrintTcDetailsTo> getStudentsByRegNoForReprintOnlyTc(String reg,HttpServletRequest request, TransferCertificateForm certificateForm) throws Exception
	{
		List<Student> studentBo=certificateTransaction.getStudentsByRegNo(reg,certificateForm.getToUsn());
		List<PrintTcDetailsTo>studentList=TransferCertificateHelper.getInstance().convertStudentBoToToForReprintOnlyTc(studentBo,request,certificateForm);
		Collections.sort(studentList);
		return studentList;
	}
}
