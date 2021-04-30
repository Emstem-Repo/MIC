package com.kp.cms.helpers.admission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ibm.icu.text.SimpleDateFormat;
import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.SubjectGroupSubjects;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.forms.admission.ConductCertificateForm;
import com.kp.cms.forms.admission.TransferCertificateForm;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admission.TransferCertificateHandler;
import com.kp.cms.helpers.exam.DownloadHallTicketHelper;
import com.kp.cms.to.admission.PrintTcDetailsTo;
import com.kp.cms.to.admission.TCDetailsTO;
import com.kp.cms.transactions.admission.IConductCertificateTransaction;
import com.kp.cms.transactionsimpl.admission.ConductCertificateTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class ConductCertificateHelper {
public static volatile ConductCertificateHelper certificateHelper=null;
	
	public static ConductCertificateHelper getInstance()
	{
		if(certificateHelper==null)
			certificateHelper=new ConductCertificateHelper();
		return certificateHelper;
	}
	
private static Map<String, String> monthMap = null;
	
	static {
		monthMap = new HashMap<String, String>();
		monthMap.put("JAN", "JANUARY");
		monthMap.put("FEB", "FEBRUARY");
		monthMap.put("MAR", "MARCH");
		monthMap.put("APR", "APRIL");
		monthMap.put("MAY", "MAY");
		monthMap.put("JUN", "JUNE");
		monthMap.put("JUL", "JULY");
		monthMap.put("AUG", "AUGUST");
		monthMap.put("SEPT", "SEPTEMBER");
		monthMap.put("OCT", "OCTOBER");
		monthMap.put("NOV", "NOVEMBER");
		monthMap.put("DEC", "DECEMBER");
		
	}
	
	IConductCertificateTransaction transferCertificate= new ConductCertificateTransactionImpl();

	public List<PrintTcDetailsTo> convertStudentBoToTo(
			List<StudentTCDetails> studentBoList,HttpServletRequest request, ConductCertificateForm form) throws NumberFormatException, Exception {
		List<PrintTcDetailsTo>studentToList=new ArrayList<PrintTcDetailsTo>();
		if(studentBoList!=null && studentBoList.size()!=0)
		{
			/*Student st = new Student();
			PrintTcDetailsTo to1 =new PrintTcDetailsTo();
			 Iterator<StudentTCDetails> itr=st.getStudentTCDetails().iterator();
				while(itr.hasNext())
				{
					StudentTCDetails tcDetails=itr.next();
					//if(st.getClassSchemewise().getClasses().getCourse().getProgram() != null)
					if(tcDetails.getDuration()!=null)
					{
						int edYr=0;
						int stYr = st.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1;
						if(st.getClassSchemewise().getClasses().getCourse().getProgram().getId()==1)
							edYr = stYr + 3;
						else if(st.getClassSchemewise().getClasses().getCourse().getProgram().getId()==2)
							edYr = stYr + 2;
						to1.setDuration(stYr+"-"+edYr);
					}*/
					
					/*StudentTCDetails tcDetails=itr.next();
					if(tcDetails.getSubjectPassed()!=null)
						to1.setSubjectsStudied(tcDetails.getSubjectPassed());
					to1.setStudentNo(st.getStudentNo());
					to1.setSlNo(st.getSlNo());
					if(st.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
					{
						Integer year = st.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
						String yr = String.valueOf(year);
						to1.setAcademicYear(yr);
					}*/

				//}   
			for(StudentTCDetails student:studentBoList)
			{
				PrintTcDetailsTo to =new PrintTcDetailsTo();
				to.setStudentName(student.getStudent().getAdmAppln().getPersonalData().getFirstName());

				if(student.getDateOfLeaving()!=null){
					String year = CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getDateOfLeaving()),"dd-MMM-yyyy","dd MMMMM yyyy");
					String[] yr = year.split(" ");
					year = yr[2]+"-"+(Integer.parseInt(yr[2])+1);
					to.setLeavingAcademicYear(year);
				}

				to.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getDateOfLeaving()),"dd-MMM-yyyy","dd MMMMM yyyy"));
				//to.setCurDate(String.valueOf(student.getStudent().getAdmAppln().getDate()));
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				 //String DateToStr = format.format(student.getStudent().getAdmAppln().getDate());
				 String DateToStr = format.format(new java.util.Date());
				 to.setCurDate(DateToStr);
				 to.setAcademicYear(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getStudent().getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd-MM-yyyy"));
				//to.setCurDate(DateToStr);
				 to.setSlNo(student.getStudent().getSlNo());
				 to.setRegNo(student.getStudent().getRegisterNo());
				
				if(student.getCharacterAndConduct().getName()!=null)
				to.setConduct(student.getCharacterAndConduct().getName());
				

				
				
				if(student.getSubjectsPassedCore()!=null)
				to.setSubjectPassedCore(student.getSubjectsPassedCore());
				if(student.getSubjectsPassedComplimentary()!=null)
				to.setSubjectsPassedComplimentary(student.getSubjectsPassedComplimentary());
				/*int termNumber = transferCertificate.getClassTermNumber(Integer.parseInt(String.valueOf(st.getClassSchemewise().getId())));
				String semNo=DownloadHallTicketHelper.semMap.get(String.valueOf(termNumber));
				// for temparary purpose for CJC
					String className= student.getStudent().getAdmAppln().getCourseBySelectedCourseId().getName();
					String Newclassname=student.getClassSchemewise().getClasses().getName();
					to.setClassName(semNo+" "+className);
					to.setAdmissionClass(semNo+" "+className);*/
				
				if(student.getSubjectPassed()!=null)
					to.setSubjectsStudied(student.getSubjectPassed());
				
				if(student.getMonth()!=null)
					to.setExammonth(monthMap.get(student.getMonth()));
				
				if(student.getStudent().getAdmAppln().getAdmissionDate()!=null)
				to.setDateOfAdmission(student.getStudent().getAdmAppln().getAdmissionDate().toString());
				
				to.setCourse(student.getStudent().getClassSchemewise().getClasses().getCourse().getName());
				if(student.getStudent().getClassSchemewise().getClasses().getCourse().getProgram() != null)
				{
					int stYr=student.getStudent().getAdmAppln().getAppliedYear();
					Date edDate = student.getStudent().getClassSchemewise().getCurriculumSchemeDuration().getEndDate();
					SimpleDateFormat sdfr = new SimpleDateFormat("yyyy");
					String edYr = sdfr.format(edDate);						
					/*if(student.getStudent().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1)
						edYr = stYr + 3;
					else if(student.getStudent().getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==2)
						edYr = stYr + 2;*/
					
					// according to client requirement add one year if his applied anf leaving year is same.
						if(stYr==Integer.parseInt(edYr))
							edYr = Integer.parseInt(edYr)+1+"";
						to.setDuration(stYr+"-"+edYr);
				}
				if(student.getLeavingAcademicYear()!=null){
					to.setLeavingAcademicYear(student.getLeavingAcademicYear()+"-"+(Integer.parseInt(student.getLeavingAcademicYear())+1));
				}
				if(student.getSubjectsPassedOptional() != null)
				{
					to.setSubjectPassedOptional(student.getSubjectsPassedOptional());
				}
				studentToList.add(to);
			}
			
		}
		
		return studentToList;
	}
	
	public List<TCDetailsTO> convertStudentTcDetailsBoToToForReprintOnlyTc(
			List<StudentTCDetails> studentBo, HttpServletRequest request,
			ConductCertificateForm certificateForm) {
		List<TCDetailsTO> to = new ArrayList<TCDetailsTO>();
		if(studentBo!=null)
		{
			Iterator<StudentTCDetails> itr=studentBo.iterator();
			
			while(itr.hasNext())
			{
				StudentTCDetails bo = itr.next();
				TCDetailsTO detailsTo =new TCDetailsTO();
				detailsTo.setStudentName(bo.getStudent().getAdmAppln().getPersonalData().getFirstName());
				detailsTo.setCourse(bo.getStudent().getClassSchemewise().getClasses().getCourse().getName());
				detailsTo.setRegisterNo(bo.getStudent().getRegisterNo());
				detailsTo.setStudentId(bo.getStudent().getId());
				detailsTo.setSubjectsPassedComplimentary(bo.getStudent().getSubjectsPassedComplimentary());
				detailsTo.setSubjectsPassedCore(bo.getStudent().getSubjectPassedCore());
				detailsTo.setExamRegNo(bo.getStudent().getExamRegNo());
				
				to.add(detailsTo);
			}	
		}
		return to;
	}
}
