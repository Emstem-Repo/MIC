package com.kp.cms.helpers.admission;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.Organisation;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.SubjectGroupSubjects;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.bo.exam.ExamStudentDetentionRejoinDetails;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.admission.TransferCertificateForm;
import com.kp.cms.handlers.admin.OrganizationHandler;
import com.kp.cms.handlers.admission.TransferCertificateHandler;
import com.kp.cms.helpers.exam.DownloadHallTicketHelper;
import com.kp.cms.to.admission.PrintTcDetailsTo;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactionsimpl.admission.TransferCertificateTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.NumberToWordConvertor;

@SuppressWarnings("unchecked")
public class TransferCertificateHelper 
{
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
	
	public static volatile TransferCertificateHelper certificateHelper=null;
	
	public static TransferCertificateHelper getInstance()
	{
		if(certificateHelper==null)
			certificateHelper=new TransferCertificateHelper();
		return certificateHelper;
	}
	ITransferCertificateTransaction transferCertificate= TransferCertificateTransactionImpl.getInstance();
	/**
	 * @param studentBoList
	 * @param number
	 * @param studentsGotTcList
	 * @param request
	 * @param form
	 * @param discontinuedStudentId
	 * @return
	 * @throws Exception
	 */
	public List<PrintTcDetailsTo> convertStudentBoToTo(List<Student> studentBoList,
													   List<Student> studentsGotTcList,
													   HttpServletRequest request,
													   TransferCertificateForm form,
													   List<Integer> discontinuedStudentId) throws Exception {
		form.setTcDate(CommonUtil.formatDate(new Date(), "dd/MM/yyyy"));
		form.setToCollege("MIC");
		int classId = studentBoList.get(0).getClassSchemewise().getClasses().getId();
		int termNumber = transferCertificate.getClassTermNumber(classId);
		String semNo=DownloadHallTicketHelper.semMap.get(String.valueOf(termNumber));
		List<PrintTcDetailsTo>studentToList=new ArrayList<PrintTcDetailsTo>();
		ArrayList<Integer> studentList = new ArrayList<Integer>() ;
		ArrayList<Integer> studentListWithMarksEntryList = new ArrayList<Integer>() ;
		int courseId= 0;
		List studentMarksList = null;
		if(studentBoList.size()>0){
			Iterator<Student> itr = studentBoList.iterator();
			while(itr.hasNext()){
				Student stu = itr.next();
				studentList.add(stu.getId());
			}
			
		String marksEntryQuery= "select e.id,e.student_id from EXAM_marks_entry e "+
        " where e.class_id =" + classId +
        " and e.student_id in(:ids) ";
		
		studentMarksList = transferCertificate.getStudentMarks(marksEntryQuery, studentList);
		if(studentMarksList.size()>0 && !studentMarksList.isEmpty()){
			Iterator<Object[]> itr1 =studentMarksList.iterator();
			while(itr.hasNext()){
				Object[] obj=itr1.next();
				if(obj[1]!=null)
				studentListWithMarksEntryList.add(Integer.parseInt(obj[1].toString()));
			    
			}
		}
		
		}
		if(form.getToCollege().equalsIgnoreCase("MIC"))
		{		
			byte[] logo = null;
			HttpSession session = request.getSession(false);
			Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
			if (organisation != null) {
				logo = organisation.getTopbar();
			}
			if (session != null) {
				session.setAttribute("LogoBytes", logo);
			}
		}	
		if(studentBoList!=null && studentBoList.size()!=0)
		{
			for(Student student:studentBoList)
			{
				//if(!discontinuedStudentId.contains(student.getId())){
				
				if(form.getToCollege().equalsIgnoreCase("Cjc"))
				{
					if(form.getTcType().equalsIgnoreCase("normal") && discontinuedStudentId.contains(student.getId())){
						continue;
					}
					if(form.getTcType().equalsIgnoreCase("Discontinued") && !discontinuedStudentId.contains(student.getId())){
						continue;
					}
				}
					
				  if(form.getDuplicate().equalsIgnoreCase("yes") && student.getTcNo()!=null)
				  {	
					PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
					if(student.getStudentTCDetails().size()!=0)
					{	
						String oldClassName=TransferCertificateHandler.getInstance().getOldClassNameByStudentId(student.getId());
						String name=student.getAdmAppln().getPersonalData().getFirstName();
						if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
								student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getMiddleName();
						if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
								student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getLastName();
						detailsTo.setStudentName(name);
						
						detailsTo.setStudentNo(student.getStudentNo());
						
						detailsTo.setRegNo(student.getRegisterNo());
						detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
						detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
						courseId = student.getAdmAppln().getCourseBySelectedCourseId().getId();
						if(form.getToCollege().equalsIgnoreCase("Cjc")){
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						else{
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						String dobInWords= "";
						if(form.getToCollege().equalsIgnoreCase("Cjc")){
							dobInWords=convertIntegerToWordForCJC(detailsTo.getDobFigures());
						}
						else{
							dobInWords=convertIntegerToWord(detailsTo.getDobFigures());	
						}
						dobInWords = dobInWords.replace(',', ' ');
						detailsTo.setDobWords(dobInWords);
						
						detailsTo.setSex(student.getAdmAppln().getPersonalData().getGender());
						if(student.getAdmAppln().getPersonalData().getReligion()!=null)
							detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligion().getName().toUpperCase());
						else if(student.getAdmAppln().getPersonalData().getReligionOthers()!=null)
							detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligionSectionOthers().toUpperCase());
		
						detailsTo.setNationality(student.getAdmAppln().getPersonalData().getNationality().getName().toUpperCase());
						
						detailsTo.setFatherName(student.getAdmAppln().getPersonalData().getFatherName());
		
						detailsTo.setMotherName(student.getAdmAppln().getPersonalData().getMotherName());
		
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
							detailsTo.setClassName(semNo+" "+className);
						}
						
						
						if(oldClassName!=null && !oldClassName.trim().isEmpty())
							detailsTo.setAdmissionClass(oldClassName);
						else
							detailsTo.setAdmissionClass(detailsTo.getClassName());
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
							detailsTo.setAdmissionClass(semNo+" "+className);
						}
						String caste="";
						String subCaste="";
						if(student.getAdmAppln().getPersonalData().getReligionSectionOthers()!=null && !student.getAdmAppln().getPersonalData().getReligionSectionOthers().isEmpty()){
							subCaste=student.getAdmAppln().getPersonalData().getReligionSectionOthers();
						}else if(student.getAdmAppln().getPersonalData().getReligionSection()!=null){
							subCaste=student.getAdmAppln().getPersonalData().getReligionSection().getName();
						}
						if(student.getAdmAppln().getPersonalData().getCaste()!=null)
						{	
							caste=student.getAdmAppln().getPersonalData().getCaste().getName();
							
							if(caste.equalsIgnoreCase("sc") || caste.equalsIgnoreCase("st") ||caste.equalsIgnoreCase("Nomadic Tribe") 
									||caste.equalsIgnoreCase("Semi-Nomadic Tribe")  ||caste.equalsIgnoreCase("CAT I") ||caste.equalsIgnoreCase("CAT IIIB")
									||caste.equalsIgnoreCase("CAT IIIA") ||caste.equalsIgnoreCase("CAT IIA") ||caste.equalsIgnoreCase("CAT IIB")){
								if(!subCaste.isEmpty()){
									detailsTo.setCaste("YES, "+" "+caste.toUpperCase()+" ("+subCaste.toUpperCase()+")");
								}else{
									detailsTo.setCaste("YES, "+" "+caste.toUpperCase());
								}
							}else{
								detailsTo.setCaste("NO");
							}
						}
						if(!subCaste.isEmpty())
							detailsTo.setReligion(detailsTo.getReligion()+" - "+subCaste.toUpperCase());
						
						if(form.getToCollege().equalsIgnoreCase("Cjc"))	{
							if(student.getAdmAppln().getAdmissionDate()!=null)
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						else{
							if(student.getAdmAppln().getAdmissionDate()!=null)
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						if(form.getTcDate()!=null && !form.getTcDate().isEmpty()){
						student.setTcDate(CommonUtil.ConvertStringToDate(form.getTcDate()));
						}else{
							student.setTcDate(new Date());
						}
						detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
						while(itr.hasNext())
						{
							StudentTCDetails tcDetails=itr.next();
							
							//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
							detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
							detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
							
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no")){
								detailsTo.setPassed( tcDetails.getPassed().toUpperCase()+"; "+tcDetails.getSubjectPassed().toUpperCase());
							}else{	
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
								detailsTo.setPassed("YES");
							}
							
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
								detailsTo.setFeePaid("Yes");
							else
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
								detailsTo.setFeePaid("No");
							if(tcDetails.getCharacterAndConduct()!=null)
							detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
							
							detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							if(form.getTcType() != null && form.getTcType().equalsIgnoreCase("Discontinued")){
								detailsTo.setRegMonthYear("---------");
							}else{
								detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());

							}
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
								detailsTo.setScholarship("YES");
							else
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
								detailsTo.setScholarship("NO");
							
							//detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
							
							detailsTo.setReason(tcDetails.getReasonOfLeaving());
							if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
							{
								Integer year = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
								String yr = String.valueOf(year);
								detailsTo.setAcademicYear(yr);
							}
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
								detailsTo.setEligible("YES");
							else
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
								detailsTo.setEligible("NO");
							
							if(discontinuedStudentId.contains(student.getId())){
								detailsTo.setPublicExamName("DISCONTINUED");
							}
							else{
								if(tcDetails.getPublicExaminationName()!=null)
								detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());
							}
							//Added by sudhir
							if(tcDetails.getClassOfLeaving()!=null && !tcDetails.getClassOfLeaving().isEmpty()){
								detailsTo.setClassOfLeaving(tcDetails.getClassOfLeaving());
							}
							// added by ashwini
							if(tcDetails.getPromotionToNextClass()!=null)
								detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
							
							if(tcDetails.getYear()!=null) 
								detailsTo.setExamyear(tcDetails.getYear());									
							
							if(tcDetails.getExamMonth()!=null)
							{
								detailsTo.setExammonth(tcDetails.getExamMonth());
							}
							if(tcDetails.getExamYear()!=null)
							{
								detailsTo.setExamyear(tcDetails.getExamYear());
							}
							if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
							{
								detailsTo.setPassed("Yes");
							}
							else
								detailsTo.setPassed("No");
							
							String subjectStudied="";
							if(tcDetails.getSubjectsPassedCore()!=null){
								detailsTo.setSubjectPassedCore(tcDetails.getSubjectsPassedCore());
								subjectStudied = subjectStudied+tcDetails.getSubjectsPassedCore();
							}
							if(tcDetails.getSubjectsPassedComplimentary()!=null){
								detailsTo.setSubjectsPassedComplimentary(tcDetails.getSubjectsPassedComplimentary());
								subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedComplimentary();
							}
							if(tcDetails.getSubjectsPassedOptional()!=null){
								detailsTo.setSubjectPassedOptional(tcDetails.getSubjectsPassedOptional());
								subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedOptional();
							}
							detailsTo.setSubjectsStudied(subjectStudied);
							if(tcDetails.getSubjectPassed()!=null)
								detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
							if(tcDetails.getSubjectFailed()!=null)
								detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
							
							detailsTo.setIsAppeared(tcDetails.getPassed());
						}
						String part1="";
						String part2="";
						String secondLanguage="";
						if(student.getAdmAppln().getApplicantSubjectGroups()!=null){
						Iterator<ApplicantSubjectGroup> iterator=student.getAdmAppln().getApplicantSubjectGroups().iterator();
						while(iterator.hasNext())
						{
							ApplicantSubjectGroup group=iterator.next();
							Iterator<SubjectGroupSubjects>iterator2=group.getSubjectGroup().getSubjectGroupSubjectses().iterator();
							
							while(iterator2.hasNext())
							{
								SubjectGroupSubjects groupSubjects=iterator2.next();
								if(!groupSubjects.getIsActive()){
									continue;
								}
								if(!groupSubjects.getSubject().getCode().equals("VED") && !groupSubjects.getSubject().getCode().equals("CA") && (groupSubjects.getSubject().getName().toLowerCase().indexOf("remidial")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("aeee")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("cet")==-1) )
								if(groupSubjects.getSubject().getIsSecondLanguage() || (groupSubjects.getSubject().getName().toLowerCase().indexOf("english")!=-1))
								{
									if(!groupSubjects.getSubject().getIsSecondLanguage()){
										if(!part1.isEmpty()){
											part1+=", "+groupSubjects.getSubject().getName().toUpperCase();
										}else{
											part1+=groupSubjects.getSubject().getName().toUpperCase();
										}
									}else{
										if(!secondLanguage.isEmpty()){
											secondLanguage+=", "+groupSubjects.getSubject().getName().toUpperCase();
										}else{
											secondLanguage+=groupSubjects.getSubject().getName().toUpperCase();
										}
									}
								}
								else
								{
									if(!part2.isEmpty())
										part2+=", "+groupSubjects.getSubject().getName().toUpperCase();
									else
										part2+=groupSubjects.getSubject().getName().toUpperCase();
								}
							}
						}
						}
						if(secondLanguage.isEmpty())
							detailsTo.setSubjectsPart1(part1);
						else
							detailsTo.setSubjectsPart1(part1+", "+secondLanguage);
						detailsTo.setSubjectsPart2(part2);
						if(form.getToCollege().equalsIgnoreCase("Cjc"))	{
							detailsTo.setSubjectsPart2(transferCertificate.getStudentSubject(student.getAdmAppln().getApplicantSubjectGroups(), student.getAdmAppln().getCourseBySelectedCourseId().getId(), Integer.parseInt(form.getYear())));
						}
						
						detailsTo.setTcNo(student.getTcNo());
						detailsTo.setTcType(student.getTcType());
						studentToList.add(detailsTo);
					}	
				}
				else
				if(form.getDuplicate().equalsIgnoreCase("no"))
				{
					
					PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
					if(student.getStudentTCDetails().size()!=0)
					{	
						String oldClassName=TransferCertificateHandler.getInstance().getOldClassNameByStudentId(student.getId());
						String name=student.getAdmAppln().getPersonalData().getFirstName();
						if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
								student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getMiddleName();
						if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
								student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getLastName();
						detailsTo.setStudentName(name);
						
						detailsTo.setStudentNo(student.getStudentNo());
						
						detailsTo.setRegNo(student.getRegisterNo());
						detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
						detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
						courseId = student.getAdmAppln().getCourseBySelectedCourseId().getId();

						if(form.getToCollege().equalsIgnoreCase("Cjc")){
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						else{
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						}
						
						//detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						String dobInWords= "";
						
						if(form.getToCollege().equalsIgnoreCase("Cjc")){
							dobInWords=convertIntegerToWordForCJC(detailsTo.getDobFigures());
						}
						else{
							dobInWords=convertBirthDateToWord(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy")).toUpperCase();
						}
						dobInWords = dobInWords.replace(',', ' ');
						detailsTo.setDobWords(dobInWords);
						
						detailsTo.setSex(student.getAdmAppln().getPersonalData().getGender());
						
						if(student.getAdmAppln().getPersonalData().getReligion()!=null)
							detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligion().getName().toUpperCase());
						else if(student.getAdmAppln().getPersonalData().getReligionOthers()!=null)
							detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligionSectionOthers().toUpperCase());
		
						detailsTo.setNationality(student.getAdmAppln().getPersonalData().getNationality().getName().toUpperCase());
						
						detailsTo.setFatherName(student.getAdmAppln().getPersonalData().getFatherName());
		
						detailsTo.setMotherName(student.getAdmAppln().getPersonalData().getMotherName());
		
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
							detailsTo.setClassName(semNo+" "+className);
						}
						
						
						if(oldClassName!=null && !oldClassName.trim().isEmpty())
							detailsTo.setAdmissionClass(oldClassName);
						else
							detailsTo.setAdmissionClass(detailsTo.getClassName());
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
							detailsTo.setAdmissionClass(semNo+" "+className);
						}
						String caste="";
						String subCaste="";
						if(student.getAdmAppln().getPersonalData().getReligionSectionOthers()!=null && !student.getAdmAppln().getPersonalData().getReligionSectionOthers().isEmpty()){
							subCaste=student.getAdmAppln().getPersonalData().getReligionSectionOthers();
						}else if(student.getAdmAppln().getPersonalData().getReligionSection()!=null){
							subCaste=student.getAdmAppln().getPersonalData().getReligionSection().getName();
						}
						if(student.getAdmAppln().getPersonalData().getCaste()!=null)
						{	
							caste=student.getAdmAppln().getPersonalData().getCaste().getName();
							
							if(caste.equalsIgnoreCase("sc") || caste.equalsIgnoreCase("st") ||caste.equalsIgnoreCase("Nomadic Tribe") 
									||caste.equalsIgnoreCase("Semi-Nomadic Tribe")  ||caste.equalsIgnoreCase("CAT I") ||caste.equalsIgnoreCase("CAT IIIB")
									||caste.equalsIgnoreCase("CAT IIIA") ||caste.equalsIgnoreCase("CAT IIA") ||caste.equalsIgnoreCase("CAT IIB")){
								if(!subCaste.isEmpty())
									detailsTo.setCaste("YES, "+" "+caste.toUpperCase()+" ("+subCaste.toUpperCase()+")");
								else
									detailsTo.setCaste("YES, "+" "+caste.toUpperCase());
							}else{
								detailsTo.setCaste("NO");
							}
						}
						if(!subCaste.isEmpty())
							detailsTo.setReligion(detailsTo.getReligion()+" - "+subCaste.toUpperCase());
						
						if(form.getTcDate()!=null && !form.getTcDate().isEmpty()){
							student.setTcDate(CommonUtil.ConvertStringToDate(form.getTcDate()));
							}else{
								student.setTcDate(new Date());
							}
						detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
						while(itr.hasNext())
						{
							StudentTCDetails tcDetails=itr.next();
							
							//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
							detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
							detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());
							detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
							detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no")){
								detailsTo.setPassed( tcDetails.getPassed().toUpperCase()+"; "+tcDetails.getSubjectPassed().toUpperCase());
							}else{	
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
								detailsTo.setPassed("YES");
							}
							
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
								detailsTo.setFeePaid("Yes");
							else
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
								detailsTo.setFeePaid("No");
							if(tcDetails.getCharacterAndConduct()!=null)
							detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
							
							detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							if(form.getTcType() != null && form.getTcType().equalsIgnoreCase("Discontinued")){
								detailsTo.setRegMonthYear("---------");
							}else{
								detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());
							}
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
								detailsTo.setScholarship("YES");
							else
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
								detailsTo.setScholarship("NO");
							
							detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
							
							detailsTo.setReason(tcDetails.getReasonOfLeaving());
							if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
							{
								Integer year = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
								String yr = String.valueOf(year);
								detailsTo.setAcademicYear(yr);
							}
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
								detailsTo.setEligible("YES");
							else
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
								detailsTo.setEligible("NO");
							
							if(discontinuedStudentId.contains(student.getId())){
								detailsTo.setPublicExamName("DISCONTINUED");
							}else{
								if(tcDetails.getPublicExaminationName()!=null)
									detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());
							}
							//Added by sudhir
							if(tcDetails.getClassOfLeaving()!=null && !tcDetails.getClassOfLeaving().isEmpty()){
								detailsTo.setClassOfLeaving(tcDetails.getClassOfLeaving());
							}
							//Ashwini
							if(tcDetails.getPromotionToNextClass()!=null)
								detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
							
							if(tcDetails.getYear()!=null) 
								detailsTo.setExamyear(tcDetails.getYear());									
							
							if(tcDetails.getExamMonth()!=null)
							{
								detailsTo.setExammonth(tcDetails.getExamMonth());
							}
							if(tcDetails.getExamYear()!=null)
							{
								detailsTo.setExamyear(tcDetails.getExamYear());
							}	
							if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
							{
								detailsTo.setPassed("Yes");
							}
							else
								detailsTo.setPassed("No");
							
							String subjectStudied="";
							if(tcDetails.getSubjectsPassedCore()!=null){
								subjectStudied = subjectStudied+tcDetails.getSubjectsPassedCore();
								detailsTo.setSubjectPassedCore(tcDetails.getSubjectsPassedCore());
							}
							if(tcDetails.getSubjectsPassedComplimentary()!=null){
								subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedComplimentary();
								detailsTo.setSubjectsPassedComplimentary(tcDetails.getSubjectsPassedComplimentary());
							}
							if(tcDetails.getSubjectsPassedOptional()!=null){
								subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedOptional();
								detailsTo.setSubjectPassedOptional(tcDetails.getSubjectsPassedOptional());
							}
							detailsTo.setSubjectsStudied(subjectStudied);
							if(tcDetails.getSubjectPassed()!=null)
								detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
							if(tcDetails.getSubjectFailed()!=null)
								detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
							detailsTo.setIsAppeared(tcDetails.getPassed());
							
							if(tcDetails.getDateOfAdmission() != null) {
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfAdmission()),"dd-MMM-yyyy","dd/MM/yyyy"));
							}
							else {
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
							}								
						}
						String part1="";
						String part2="";
						String secondLanguage="";
						if(student.getAdmAppln().getApplicantSubjectGroups()!=null){
						Iterator<ApplicantSubjectGroup> iterator=student.getAdmAppln().getApplicantSubjectGroups().iterator();
						while(iterator.hasNext())
						{
							ApplicantSubjectGroup group=iterator.next();
							Iterator<SubjectGroupSubjects>iterator2=group.getSubjectGroup().getSubjectGroupSubjectses().iterator();
							
							while(iterator2.hasNext())
							{
								SubjectGroupSubjects groupSubjects=iterator2.next();
								if(!groupSubjects.getIsActive()){
									continue;
								}
								if(!groupSubjects.getSubject().getCode().equals("VED") && !groupSubjects.getSubject().getCode().equals("CA") && (groupSubjects.getSubject().getName().toLowerCase().indexOf("remidial")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("aeee")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("cet")==-1) )
								if(groupSubjects.getSubject().getIsSecondLanguage() || (groupSubjects.getSubject().getName().toLowerCase().indexOf("english")!=-1))
								{
									if(!groupSubjects.getSubject().getIsSecondLanguage()){
										if(!part1.isEmpty()){
											part1+=", "+groupSubjects.getSubject().getName().toUpperCase();
										}else{
											part1+=groupSubjects.getSubject().getName().toUpperCase();
										}
									}else{
										if(!secondLanguage.isEmpty()){
											secondLanguage+=", "+groupSubjects.getSubject().getName().toUpperCase();
										}else{
											secondLanguage+=groupSubjects.getSubject().getName().toUpperCase();
										}
									}
								}
								else
								{
									if(!part2.isEmpty())
										part2+=", "+groupSubjects.getSubject().getName().toUpperCase();
									else
										part2+=groupSubjects.getSubject().getName().toUpperCase();
								}
							}
						}
						}
						if(secondLanguage.isEmpty())
							detailsTo.setSubjectsPart1(part1);
						else
							detailsTo.setSubjectsPart1(part1+", "+secondLanguage);
						detailsTo.setSubjectsPart2(part2);
						if(form.getToCollege().equalsIgnoreCase("Cjc"))	{
							detailsTo.setSubjectsPart2(transferCertificate.getStudentSubject(student.getAdmAppln().getApplicantSubjectGroups(), student.getAdmAppln().getCourseBySelectedCourseId().getId(), Integer.parseInt(form.getYear())));
						}
					
						
						if(form.getToCollege().equalsIgnoreCase("MIC"))
						{
							if(!CMSConstants.COURSE_ID_LIST.contains(courseId)){
								detailsTo.setTcNo(student.getTcNo());
								student.setTcNo(detailsTo.getTcNo());
							}
							else{
								detailsTo.setTcNo(student.getTcNo());
							}
							
							detailsTo.setTcType(form.getTcType());

						}
						else
						{
							student.setTcNo(detailsTo.getTcNo());
							detailsTo.setTcNo(student.getRegisterNo());
						}
						
						//student.setTcNo(String.valueOf(currentTcNo));
						
						student.setSlNo(detailsTo.getSlNo());
						student.setTcType(form.getTcType());
						
						studentsGotTcList.add(student);
						studentToList.add(detailsTo);
					}
				  }	
				//}
			}	
		}	
		
		return studentToList;
	}

	/**
	 * @param slNo
	 * @param prefix
	 * @param year
	 * @return
	 */
	private String getSlno(int slNo,String year, int courseId) 
	{
		String strSlNo=""+slNo;
		String result="";
		if(strSlNo.length()==1)
			strSlNo="000"+strSlNo;
		else
		if(strSlNo.length()==2)
			strSlNo="00"+strSlNo;
		else
		if(strSlNo.length()==3)
			strSlNo="0"+strSlNo;
		
		String nextYear=""+String.valueOf(Integer.parseInt(year)+1);
		result=strSlNo+"/"+year+"-"+nextYear.substring(2);			
		return result;
	}

	/**
	 * @param dobFigures
	 * @return
	 */
	private String convertIntegerToWord(String dobFigures) 
	{
		String inWords="";
		StringTokenizer str=new StringTokenizer(dobFigures,"/");
		int date=Integer.parseInt(str.nextToken());
		int month=Integer.parseInt(str.nextToken());
		String year=str.nextToken();
		String year1=year.substring(0, 2);
		String year2=year.substring(2);
		inWords=NumberToWordConvertor.getDate(date)+","+CommonUtil.getMonthForNumber(month)+","+NumberToWordConvertor.convertNumber(year1).toUpperCase()+" "+NumberToWordConvertor.convertNumber(year2).toUpperCase();
		return inWords;
	}

	/**
	 * @param studentBoList
	 * @param request
	 * @param discontinuedStudentsList
	 * @param form 
	 * @return
	 * @throws Exception
	 */
	public List<PrintTcDetailsTo> convertStudentBoToToForReprint(List<Student> studentBoList, HttpServletRequest request, List<Integer>  discontinuedStudentsList, TransferCertificateForm form)throws Exception  
	{
		
		List<PrintTcDetailsTo>studentToList=new ArrayList<PrintTcDetailsTo>();
		ArrayList<Integer> studentList = new ArrayList<Integer>() ;
		ArrayList<Integer> studentListWithMarksEntryList = new ArrayList<Integer>() ;
		int courseId= 0;
		List studentMarksList = null;
		if(studentBoList.size()>0){
			Iterator<Student> itr = studentBoList.iterator();
			while(itr.hasNext()){
				Student stu = itr.next();
				studentList.add(stu.getId());
			}
			
		String marksEntryQuery= "select e.id,e.student_id from EXAM_marks_entry e "+
        " where e.class_id =" + form.getClasses()+
        " and e.student_id in(:ids) ";
		
		studentMarksList = transferCertificate.getStudentMarks(marksEntryQuery, studentList);
		if(studentMarksList.size()>0 && !studentMarksList.isEmpty()){
			Iterator<Object[]> itr1 =studentMarksList.iterator();
			while(itr.hasNext()){
				Object[] obj=itr1.next();
				if(obj[1]!=null)
				studentListWithMarksEntryList.add(Integer.parseInt(obj[1].toString()));
			    
			}
		}
		
		}
		byte[] logo = null;
		HttpSession session = request.getSession(false);
		Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
		if (organisation != null) {
			logo = organisation.getTopbar();
		}
		if (session != null) {
			session.setAttribute("LogoBytes", logo);
		}
		if(studentBoList!=null)
		{
			for(Student student:studentBoList)
			{	
				PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
				if(student.getStudentTCDetails().size()!=0)
				{	
					String oldClassName=TransferCertificateHandler.getInstance().getOldClassNameByStudentId(student.getId());
					String name=student.getAdmAppln().getPersonalData().getFirstName();
					if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
						student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
					name+=student.getAdmAppln().getPersonalData().getMiddleName();
					if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
						student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
					name+=student.getAdmAppln().getPersonalData().getLastName();
					detailsTo.setStudentName(name);
						
					detailsTo.setStudentNo(student.getStudentNo());
					
					detailsTo.setRegNo(student.getRegisterNo());
					
					detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
					courseId = student.getAdmAppln().getCourseBySelectedCourseId().getId();
					detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
					detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
					String dobInWords=convertIntegerToWordForCJC(detailsTo.getDobFigures());
					detailsTo.setDobWords(dobInWords);
					
					detailsTo.setSex(student.getAdmAppln().getPersonalData().getGender());
						
					if(student.getAdmAppln().getPersonalData().getReligion()!=null)
						detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligion().getName().toUpperCase());
					else if(student.getAdmAppln().getPersonalData().getReligionOthers()!=null)
						detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligionSectionOthers().toUpperCase());
		
					detailsTo.setNationality(student.getAdmAppln().getPersonalData().getNationality().getName().toUpperCase());
					
					detailsTo.setFatherName(student.getAdmAppln().getPersonalData().getFatherName());
		
					detailsTo.setMotherName(student.getAdmAppln().getPersonalData().getMotherName());
					int termNumber = transferCertificate.getClassTermNumber(Integer.parseInt(String.valueOf(student.getClassSchemewise().getId())));
					String semNo=DownloadHallTicketHelper.semMap.get(String.valueOf(termNumber));
					
					int year=student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
					
					if(oldClassName!=null && !oldClassName.trim().isEmpty())
						detailsTo.setAdmissionClass(oldClassName);
					else
						detailsTo.setAdmissionClass(detailsTo.getClassName());
					
					// for temparary purpose for CJC
						String className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
						//String Newclassname=student.getClassSchemewise().getClasses().getName();
						detailsTo.setClassName(semNo+" "+className);
						detailsTo.setAdmissionClass(semNo+" "+className);
		
					String caste="";
					String subCaste="";
					if(student.getAdmAppln().getPersonalData().getReligionSectionOthers()!=null && !student.getAdmAppln().getPersonalData().getReligionSectionOthers().isEmpty()){
						subCaste=student.getAdmAppln().getPersonalData().getReligionSectionOthers();
					}else if(student.getAdmAppln().getPersonalData().getReligionSection()!=null){
						subCaste=student.getAdmAppln().getPersonalData().getReligionSection().getName();
					}
					if(student.getAdmAppln().getPersonalData().getCaste()!=null)
					{	
						caste=student.getAdmAppln().getPersonalData().getCaste().getName();
						
						if(caste.equalsIgnoreCase("sc") || caste.equalsIgnoreCase("st") ||caste.equalsIgnoreCase("Nomadic Tribe") 
								||caste.equalsIgnoreCase("Semi-Nomadic Tribe")  ||caste.equalsIgnoreCase("CAT I") ||caste.equalsIgnoreCase("CAT IIIB")
								||caste.equalsIgnoreCase("CAT IIIA") ||caste.equalsIgnoreCase("CAT IIA") ||caste.equalsIgnoreCase("CAT IIB")){
							if(!subCaste.isEmpty())
								detailsTo.setCaste("YES, "+" "+caste.toUpperCase()+" ("+subCaste.toUpperCase()+")");
							else
								detailsTo.setCaste("YES, "+" "+caste.toUpperCase());
						}else{
							detailsTo.setCaste("NO");
						}
					}
					if(!subCaste.isEmpty())
						detailsTo.setReligion(detailsTo.getReligion()+" - "+subCaste.toUpperCase());
					
					if(student.getAdmAppln().getAdmissionDate()!=null)
						detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
					if(form.getTcReDate()!=null && !form.getTcReDate().isEmpty())
					{
						detailsTo.setTcDate(form.getTcReDate());
					}else{
					detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
					}
					detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
					
					Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
					while(itr.hasNext())
					{
						StudentTCDetails tcDetails=itr.next();
							
						//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
						detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
						detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
						detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());
						if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no"))
							detailsTo.setPassed( tcDetails.getPassed().toUpperCase()+"; "+tcDetails.getSubjectPassed().toUpperCase());
						else	
						if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
							detailsTo.setPassed("YES");
						
						if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
							detailsTo.setFeePaid("Yes");
						else
						if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
							detailsTo.setFeePaid("No");
						if(tcDetails.getCharacterAndConduct()!=null)	
						detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
						
						detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
						if(student.getTcType() != null && student.getTcType().equalsIgnoreCase("Discontinued")){
							detailsTo.setRegMonthYear("--------");
						}else{
							detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());
						}
						if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
							detailsTo.setScholarship("YES");
						else
						if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
							detailsTo.setScholarship("NO");
							
						detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
							
						detailsTo.setReason(tcDetails.getReasonOfLeaving());
							
						if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
						{
							Integer academicYear = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
							String yr = String.valueOf(academicYear);
							detailsTo.setAcademicYear(yr);
						}
						if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
							detailsTo.setEligible("YES");
						else
						if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
							detailsTo.setEligible("NO");
						
						if(discontinuedStudentsList.contains(student.getId())){
							if(tcDetails.getPublicExaminationName()!=null)
								detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());
								//form.setTcType("Discontinued");
								detailsTo.setTcType("Discontinued");
						}
						else{
							if(tcDetails.getPublicExaminationName()!=null)
								detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());	
						}
						
						if(tcDetails.getIsFeePaidUni()!= null && tcDetails.getIsFeePaidUni()){
							detailsTo.setFeeDueToUni("YES");
						}
						else{
							detailsTo.setFeeDueToUni("NO");
						}
						//Added by sudhir
						if(tcDetails.getClassOfLeaving()!=null && !tcDetails.getClassOfLeaving().isEmpty()){
							detailsTo.setClassOfLeaving(tcDetails.getClassOfLeaving());
						}
						// added by ashwini
						if(tcDetails.getPromotionToNextClass()!=null)
							detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
						
						if(tcDetails.getYear()!=null) 
							detailsTo.setExamyear(tcDetails.getYear());									
						
						if(tcDetails.getExamMonth()!=null)
						{
							detailsTo.setExammonth(tcDetails.getExamMonth());
						}
						if(tcDetails.getExamYear()!=null)
						{
							detailsTo.setExamyear(tcDetails.getExamYear());
						}
						if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
						{
							detailsTo.setPassed("Yes");
						}
						else
							detailsTo.setPassed("No");	
						String subjectStudied="";
						if(tcDetails.getSubjectsPassedCore()!=null){
							detailsTo.setSubjectPassedCore(tcDetails.getSubjectsPassedCore());
							subjectStudied = subjectStudied+tcDetails.getSubjectsPassedCore();
						}
						if(tcDetails.getSubjectsPassedComplimentary()!=null){
							detailsTo.setSubjectsPassedComplimentary(tcDetails.getSubjectsPassedComplimentary());
							subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedComplimentary();
						}
						if(tcDetails.getSubjectsPassedOptional()!=null){
							detailsTo.setSubjectPassedOptional(tcDetails.getSubjectsPassedOptional());
							subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedOptional();
						}
						detailsTo.setSubjectsStudied(subjectStudied);
						if(tcDetails.getSubjectPassed()!=null)
							detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
						if(tcDetails.getSubjectFailed()!=null)
							detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
						detailsTo.setIsAppeared(tcDetails.getPassed());
					}	
					String part1="";
					String part2="";
					String secondLanguage="";
					if(student.getAdmAppln().getApplicantSubjectGroups()!=null){
					Iterator<ApplicantSubjectGroup> iterator=student.getAdmAppln().getApplicantSubjectGroups().iterator();
					while(iterator.hasNext())
					{
						ApplicantSubjectGroup group=iterator.next();
						Iterator<SubjectGroupSubjects>iterator2=group.getSubjectGroup().getSubjectGroupSubjectses().iterator();
						
						while(iterator2.hasNext())
						{
							SubjectGroupSubjects groupSubjects=iterator2.next();
							if(!groupSubjects.getIsActive()){
								continue;
							}
							if(!groupSubjects.getSubject().getCode().equals("VED") && !groupSubjects.getSubject().getCode().equals("CA") && (groupSubjects.getSubject().getName().toLowerCase().indexOf("remidial")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("aeee")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("cet")==-1) )
							if(groupSubjects.getSubject().getIsSecondLanguage() || (groupSubjects.getSubject().getName().toLowerCase().indexOf("english")!=-1))
							{
								if(!groupSubjects.getSubject().getIsSecondLanguage()){
									if(!part1.isEmpty()){
										part1+=", "+groupSubjects.getSubject().getName().toUpperCase();
									}else{
										part1+=groupSubjects.getSubject().getName().toUpperCase();
									}
								}else{
									if(!secondLanguage.isEmpty()){
										secondLanguage+=", "+groupSubjects.getSubject().getName().toUpperCase();
									}else{
										secondLanguage+=groupSubjects.getSubject().getName().toUpperCase();
									}
								}
							}
							else
							{
								if(!part2.isEmpty())
									part2+=", "+groupSubjects.getSubject().getName().toUpperCase();
								else
									part2+=groupSubjects.getSubject().getName().toUpperCase();
							}
						}
					}
					}
					if(secondLanguage.isEmpty())
						detailsTo.setSubjectsPart1(part1);
					else
						detailsTo.setSubjectsPart1(part1+", "+secondLanguage);
					detailsTo.setSubjectsPart2(part2);
					// for CJC Purpose
					detailsTo.setSubjectsPart2(transferCertificate.getStudentSubject(student.getAdmAppln().getApplicantSubjectGroups(), student.getAdmAppln().getCourseBySelectedCourseId().getId(),year));
					if(courseId!=23)
						detailsTo.setTcNo(student.getTcNo());
					else
						detailsTo.setTcNo(student.getTcNo());
					
					detailsTo.setSlNo(student.getSlNo());
					detailsTo.setMcNo(student.getMcNo());
					studentToList.add(detailsTo);
				}
			}	
		}
		return studentToList;
	}

	public String getQueryForInputSearch(TransferCertificateForm certificateForm) throws Exception {
		
		StringBuffer query = new StringBuffer("select s.student from StudentTCDetails s where s.student.admAppln.isSelected=1 " +
				" and s.student.admAppln.isApproved=1 ");
		if(certificateForm.getClasses()!=null && !certificateForm.getClasses().trim().isEmpty())
			query.append(" and s.student.classSchemewise.id="+certificateForm.getClasses());
		if(certificateForm.getFromUsn()!=null && !certificateForm.getFromUsn().trim().isEmpty())
			query.append(" and s.student.registerNo>='"+certificateForm.getFromUsn()+"'");
		if(certificateForm.getToUsn()!=null && !certificateForm.getToUsn().trim().isEmpty())
			query.append(" and s.student.registerNo<='"+certificateForm.getToUsn()+"'");
		if(certificateForm.getDuplicate().equalsIgnoreCase("no"))
			query.append(" and s.student.tcNo is null");
		
		
		return query.toString();
	}
	/*
	public List<PrintTcDetailsTo> getPrintTCDetails(List<Student> studentBoList, TCNumber number)  throws Exception{
		boolean isDuplicate=false;
		List<PrintTcDetailsTo> printList=new ArrayList<PrintTcDetailsTo>();
		if(studentBoList!=null && !studentBoList.isEmpty()){
			Iterator<Student> itr=studentBoList.iterator();
			while (itr.hasNext()) {
				Student student = (Student) itr.next();

				if(isDuplicate && student.getTcNo()!=null)
				{	
					PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
					if(student.getStudentTCDetails().size()!=0)
					{	
						
						String oldClassName=TransferCertificateHandler.getInstance().getOldClassNameByStudentId(student.getId());
						String name=student.getAdmAppln().getPersonalData().getFirstName();
						if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
								student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getMiddleName();
						if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
								student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getLastName();
						detailsTo.setStudentName(name);
						
						detailsTo.setStudentNo(student.getStudentNo());
						
						detailsTo.setRegNo(student.getRegisterNo());
						
						detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
						
						detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						String dobInWords=convertIntegerToWord(detailsTo.getDobFigures());
						detailsTo.setDobWords(dobInWords);
						
						detailsTo.setSex(student.getAdmAppln().getPersonalData().getGender());
						
						detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligion().getName());
		
						detailsTo.setNationality(student.getAdmAppln().getPersonalData().getNationality().getName());
						
						detailsTo.setFatherName(student.getAdmAppln().getPersonalData().getFatherName());
		
						detailsTo.setMotherName(student.getAdmAppln().getPersonalData().getMotherName());
		
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							StringTokenizer str=new StringTokenizer(student.getClassSchemewise().getClasses().getName()," ");
							className=str.nextToken()+" "+str.nextToken();
							detailsTo.setClassName(className);
						}
						
						
						if(oldClassName!=null && !oldClassName.trim().isEmpty())
							detailsTo.setAdmissionClass(oldClassName);
						else
							detailsTo.setAdmissionClass(detailsTo.getClassName());
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							StringTokenizer str=new StringTokenizer(detailsTo.getAdmissionClass()," ");
							className=str.nextToken()+" "+str.nextToken();
							detailsTo.setAdmissionClass(className);
						}
						String caste="";
						if(student.getAdmAppln().getPersonalData().getCaste()!=null)
						{	
							caste=student.getAdmAppln().getPersonalData().getCaste().getName();
							
							if(caste.equalsIgnoreCase("sc") || caste.equalsIgnoreCase("st") ||caste.equalsIgnoreCase("Nomadic Tribe") ||caste.equalsIgnoreCase("Semi-Nomadic Tribe"))
								detailsTo.setCaste("Yes "+caste);
							else
								detailsTo.setCaste("No");
							
							detailsTo.setReligion(detailsTo.getReligion()+"-"+caste);
						}
						if(student.getAdmAppln().getAdmissionDate()!=null)
							detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						student.setTcDate(new Date());
						detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd-MM-yyyy"));
						
						Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
						while(itr.hasNext())
						{
							StudentTCDetails tcDetails=itr.next();
							
							detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							detailsTo.setLeavingYear(detailsTo.getDateOfLeaving().substring(6));
							
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no"))
								detailsTo.setPassed(tcDetails.getSubjectPassed());
							else	
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
								detailsTo.setPassed("Yes");
							
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
								detailsTo.setFeePaid("Yes");
							else
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
								detailsTo.setFeePaid("No");
							if(tcDetails.getCharacterAndConduct()!=null)
							detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName());
							
							detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							String examRegNo="";
							if(student.getExamRegisterNo()!=null)
								examRegNo=student.getExamRegisterNo();
							else
							{	
								if(form.getToCollege().equalsIgnoreCase("MIC"))	
									examRegNo=student.getRegisterNo();
							}	
							detailsTo.setRegMonthYear(examRegNo+" "+tcDetails.getMonth()+" "+tcDetails.getYear());
							
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
								detailsTo.setScholarship("Yes");
							else
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
								detailsTo.setScholarship("No");
							
							detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
							
							detailsTo.setReason(tcDetails.getReasonOfLeaving());
							
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
								detailsTo.setEligible("Yes");
							else
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
								detailsTo.setEligible("No");
							
							detailsTo.setPublicExamName(tcDetails.getPublicExaminationName());
						}
						String part1="";
						String part2="";
						if(student.getAdmAppln().getApplicantSubjectGroups()!=null){
						Iterator<ApplicantSubjectGroup> iterator=student.getAdmAppln().getApplicantSubjectGroups().iterator();
						while(iterator.hasNext())
						{
							ApplicantSubjectGroup group=iterator.next();
							Iterator<SubjectGroupSubjects>iterator2=group.getSubjectGroup().getSubjectGroupSubjectses().iterator();
							
							while(iterator2.hasNext())
							{
								SubjectGroupSubjects groupSubjects=iterator2.next();
								if(!groupSubjects.getSubject().getCode().equals("VED") && !groupSubjects.getSubject().getCode().equals("CA") && (groupSubjects.getSubject().getName().toLowerCase().indexOf("remidial")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("aeee")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("cet")==-1) )
								if(groupSubjects.getSubject().getIsSecondLanguage() || (groupSubjects.getSubject().getName().toLowerCase().indexOf("english")!=-1))
								{
									part1+=groupSubjects.getSubject().getName()+",";
								}
								else
								{
									part2+=groupSubjects.getSubject().getName()+",";
								}
							}
						}
						part1=StringUtils.chop(part1);
						part2=StringUtils.chop(part2);
						}
						
						detailsTo.setSubjectsPart1(part1);
						detailsTo.setSubjectsPart2(part2);
											
						TCNumber tcNumber=null;
						TCNumber mcNumber=null;
						for(TCNumber tc:number)
						{
							if(tc.getType().equalsIgnoreCase("TC"))
								tcNumber=tc;
							else
							if(tc.getType().equalsIgnoreCase("MC"))
								mcNumber=tc;
						}
						
						int currentTcNo=tcNumber.getCurrentNo();
						int slNo=tcNumber.getSlNo();
						if(form.getToCollege().equalsIgnoreCase("MIC"))
						{
							detailsTo.setTcNo(student.getTcNo());detailsTo.setSlNo(""+slNo);
							detailsTo.setTcType(form.getTcType());
							tcNumber.setCurrentNo(++currentTcNo);
						}
						else
						{
							detailsTo.setTcNo(student.getRegisterNo());
							detailsTo.setSlNo(getSlno(slNo,tcNumber.getPrefix()));
						}
						student.setTcNo(detailsTo.getTcNo());
						student.setSlNo(detailsTo.getSlNo());
						student.setTcType(detailsTo.getTcType());
						tcNumber.setSlNo(++slNo);
						detailsTo.setMcNo(student.getMcNo());
						studentsGotTcList.add(student);
						studentToList.add(detailsTo);
					}	
				}
				else
				if(form.getDuplicate().equalsIgnoreCase("no"))
				{
					
					PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
					if(student.getStudentTCDetails().size()!=0)
					{	
						
						String oldClassName=TransferCertificateHandler.getInstance().getOldClassNameByStudentId(student.getId());
						String name=student.getAdmAppln().getPersonalData().getFirstName();
						if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
								student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getMiddleName();
						if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
								student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
							name+=student.getAdmAppln().getPersonalData().getLastName();
						detailsTo.setStudentName(name);
						
						detailsTo.setStudentNo(student.getStudentNo());
						
						detailsTo.setRegNo(student.getRegisterNo());
						
						detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
						
						detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						String dobInWords=convertIntegerToWord(detailsTo.getDobFigures());
						detailsTo.setDobWords(dobInWords);
						
						detailsTo.setSex(student.getAdmAppln().getPersonalData().getGender());
						
						detailsTo.setReligion(student.getAdmAppln().getPersonalData().getReligion().getName());
		
						detailsTo.setNationality(student.getAdmAppln().getPersonalData().getNationality().getName());
						
						detailsTo.setFatherName(student.getAdmAppln().getPersonalData().getFatherName());
		
						detailsTo.setMotherName(student.getAdmAppln().getPersonalData().getMotherName());
		
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							StringTokenizer str=new StringTokenizer(student.getClassSchemewise().getClasses().getName()," ");
							className=str.nextToken()+" "+str.nextToken();
							detailsTo.setClassName(className);
						}
						
						
						if(oldClassName!=null && !oldClassName.trim().isEmpty())
							detailsTo.setAdmissionClass(oldClassName);
						else
							detailsTo.setAdmissionClass(detailsTo.getClassName());
						if(form.getToCollege().equalsIgnoreCase("Cjc"))
						{
							String className="";
							StringTokenizer str=new StringTokenizer(detailsTo.getAdmissionClass()," ");
							className=str.nextToken()+" "+str.nextToken();
							detailsTo.setAdmissionClass(className);
						}
						String caste="";
						if(student.getAdmAppln().getPersonalData().getCaste()!=null)
						{	
							caste=student.getAdmAppln().getPersonalData().getCaste().getName();
							
							if(caste.equalsIgnoreCase("sc") || caste.equalsIgnoreCase("st") ||caste.equalsIgnoreCase("Nomadic Tribe") ||caste.equalsIgnoreCase("Semi-Nomadic Tribe"))
								detailsTo.setCaste("Yes "+caste);
							else
								detailsTo.setCaste("No");
							
							detailsTo.setReligion(detailsTo.getReligion()+"-"+caste);
						}
						if(student.getAdmAppln().getAdmissionDate()!=null)
							detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						student.setTcDate(new Date());
						detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd-MM-yyyy"));
						
						Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
						while(itr.hasNext())
						{
							StudentTCDetails tcDetails=itr.next();
							
							detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							detailsTo.setLeavingYear(detailsTo.getDateOfLeaving().substring(6));
							
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no"))
								detailsTo.setPassed(tcDetails.getSubjectPassed());
							else	
							if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
								detailsTo.setPassed("Yes");
							
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
								detailsTo.setFeePaid("Yes");
							else
							if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
								detailsTo.setFeePaid("No");
							if(tcDetails.getCharacterAndConduct()!=null)
							detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName());
							
							detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd-MM-yyyy"));
							
							String examRegNo="";
							if(student.getExamRegisterNo()!=null)
								examRegNo=student.getExamRegisterNo();
							else
							{	
								if(form.getToCollege().equalsIgnoreCase("MIC"))	
									examRegNo=student.getRegisterNo();
							}	
							detailsTo.setRegMonthYear(examRegNo+" "+tcDetails.getMonth()+" "+tcDetails.getYear());
							
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
								detailsTo.setScholarship("Yes");
							else
							if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
								detailsTo.setScholarship("No");
							
							detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
							
							detailsTo.setReason(tcDetails.getReasonOfLeaving());
							
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
								detailsTo.setEligible("Yes");
							else
							if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
								detailsTo.setEligible("No");
							
							detailsTo.setPublicExamName(tcDetails.getPublicExaminationName());
						}
						String part1="";
						String part2="";
						if(student.getAdmAppln().getApplicantSubjectGroups()!=null){
						Iterator<ApplicantSubjectGroup> iterator=student.getAdmAppln().getApplicantSubjectGroups().iterator();
						while(iterator.hasNext())
						{
							ApplicantSubjectGroup group=iterator.next();
							Iterator<SubjectGroupSubjects>iterator2=group.getSubjectGroup().getSubjectGroupSubjectses().iterator();
							
							while(iterator2.hasNext())
							{
								SubjectGroupSubjects groupSubjects=iterator2.next();
								if(!groupSubjects.getSubject().getCode().equals("VED") && !groupSubjects.getSubject().getCode().equals("CA") && (groupSubjects.getSubject().getName().toLowerCase().indexOf("remidial")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("aeee")==-1) &&(groupSubjects.getSubject().getName().toLowerCase().indexOf("cet")==-1) )
								if(groupSubjects.getSubject().getIsSecondLanguage() || (groupSubjects.getSubject().getName().toLowerCase().indexOf("english")!=-1))
								{
									part1+=groupSubjects.getSubject().getName()+",";
								}
								else
								{
									part2+=groupSubjects.getSubject().getName()+",";
								}
							}
						}
						part1=StringUtils.chop(part1);
						part2=StringUtils.chop(part2);
						}
						detailsTo.setSubjectsPart1(part1);
						detailsTo.setSubjectsPart2(part2);
											
						TCNumber tcNumber=null;
						TCNumber mcNumber=null;
						for(TCNumber tc:number)
						{
							if(tc.getType().equalsIgnoreCase("TC"))
								tcNumber=tc;
							else
							if(tc.getType().equalsIgnoreCase("MC"))
								mcNumber=tc;
						}
						
						int currentTcNo=tcNumber.getCurrentNo();
						int slNo=tcNumber.getSlNo();
						if(form.getToCollege().equalsIgnoreCase("MIC"))
						{
								detailsTo.setTcNo(tcNumber.getPrefix()+currentTcNo);
								detailsTo.setSlNo(""+slNo);
								detailsTo.setTcType(form.getTcType());
								tcNumber.setCurrentNo(++currentTcNo);
							
						}
						else
						{
							detailsTo.setTcNo(student.getRegisterNo());
							detailsTo.setSlNo(getSlno(slNo,tcNumber.getPrefix()));
						}
						student.setTcNo(detailsTo.getTcNo());
						student.setSlNo(detailsTo.getSlNo());
						student.setTcType(form.getTcType());
						tcNumber.setSlNo(++slNo);
						
						if(mcNumber!=null)
						{	
							int currentMcNo=mcNumber.getCurrentNo();
							detailsTo.setMcNo(mcNumber.getPrefix()+currentMcNo);
							student.setMcNo(detailsTo.getMcNo());
							mcNumber.setCurrentNo(++currentMcNo);
						}	
						number.clear();
						number.add(tcNumber);
						if(mcNumber!=null)
						{
							number.add(mcNumber);
						}	
						studentsGotTcList.add(student);
						studentToList.add(detailsTo);
					}	
				}
			
			}
		}
		return printList;
	}*/
	
	public String getSchemeinWords(String courseScheme, Integer schemeNo) throws Exception {
		String year = "";
		if(courseScheme.equalsIgnoreCase("Semester")){
			if(schemeNo == 1 || schemeNo == 2 ){
				year = "First";
			}
			else if( schemeNo == 3 || schemeNo == 4){
				year = "Second";	
			}
			else if( schemeNo == 5 || schemeNo == 6){
				year = "Third";	
			}
			else if( schemeNo == 7 || schemeNo == 8){
				year = "Fourth";	
			}
			else if( schemeNo == 9 || schemeNo == 10){
				year = "Fifth";	
			}
		}
		else if(courseScheme.equalsIgnoreCase("Trimester")){
			if(schemeNo == 1 || schemeNo == 2 || schemeNo == 3){
				year = "First";
			}
			else if( schemeNo == 4 || schemeNo == 5 || schemeNo == 6){
				year = "Second";	
			}
			else if( schemeNo == 7 || schemeNo == 8 || schemeNo == 9){
				year = "Third";	
			}
			else if( schemeNo == 10 || schemeNo == 11 || schemeNo == 12){
				year = "Fourth";	
			}
		}
		else if(courseScheme.equalsIgnoreCase("Year")){
			if(schemeNo == 1){
				year = "First";
			}
			else if( schemeNo == 2){
				year = "Second";	
			}
			else if( schemeNo == 3){
				year = "Third";	
			}
			else if( schemeNo == 4){
				year = "Fourth";	
			}
			else if( schemeNo == 5){
				year = "Fifth";	
			}
		}
		return year;
		
	}
	//tc print for MOUNT CARMEL 
	public List<PrintTcDetailsTo> convertStudentBoToToOnlyTC(List<Student> studentBoList,
															 List<TCNumber> number,
															 List<Student> studentsGotTcList,
															 HttpServletRequest request,
															 TransferCertificateForm form,
															 List<Integer> discontinuedStudentId)throws Exception 
	{
		List<PrintTcDetailsTo>studentToList=new ArrayList<PrintTcDetailsTo>();
		ArrayList<Integer> studentList = new ArrayList<Integer>() ;
		if(studentBoList.size()>0){
			Iterator<Student> itr = studentBoList.iterator();
			while(itr.hasNext()){
				Student stu = itr.next();
				studentList.add(stu.getId());
			}
			
		
		}
		if(number!=null && !number.isEmpty())
		{	
			
			if(studentBoList!=null && studentBoList.size()!=0)
			{
				for(Student student:studentBoList)
				{
					Integer admittedSem = TransferCertificateHandler.getInstance().getAdmittedSemester(student.getId());
					BigInteger rejoinYear = TransferCertificateHandler.getInstance().getRejoinYear(student.getId());
					if(form.getDuplicate().equalsIgnoreCase("yes") && student.getTcNo()!=null)
					{	
						PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
						detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
						if(student.getStudentTCDetails().size()!=0)
						{	
							String name=student.getAdmAppln().getPersonalData().getFirstName();
							if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
									student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
								name+= " " + student.getAdmAppln().getPersonalData().getMiddleName();
							if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
									student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
								name+= " " + student.getAdmAppln().getPersonalData().getLastName();
							detailsTo.setStudentName(name);
							
							detailsTo.setStudentNo(student.getStudentNo());
							
							detailsTo.setRegNo(student.getRegisterNo());
							
							detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
							
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
							
							String dobInWords=convertBirthDateToWord(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy")).toUpperCase();
							dobInWords = dobInWords.replace(',', ' ');
							detailsTo.setDobWords(dobInWords);
							
						
		
							
							String schemeInWords = getSchemeinWords(student.getClassSchemewise().getCurriculumSchemeDuration().getCurriculumScheme().getCourseScheme().getName(), student.getClassSchemewise().getClasses().getTermNumber());
							String className="";
							if(schemeInWords!=null){
								className=schemeInWords.toUpperCase();
							}
							/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
							}else if(CMSConstants.PG_ID.contains(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId())
									||student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
							}else if(student.getClassSchemewise().getClasses().getCourse().getProgram().getId()!=9){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getName().toUpperCase();
							}else
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();*/
							if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
								className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
							}
							
							detailsTo.setClassName(className);
							
							String courseScheme = TransferCertificateHandler.getInstance().getCourseScheme(student.getAdmAppln().getCourseBySelectedCourseId().getId(), student.getAdmAppln().getAppliedYear());
							schemeInWords = getSchemeinWords(courseScheme, admittedSem);
							className="";
							
							if(schemeInWords!=null)
								className=schemeInWords.toUpperCase();
							/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
							}else if(CMSConstants.PG_ID.contains(student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId())
									|| student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()==3){
								className=className+" "+admittedDetails.getCourse().getName().toUpperCase();
							}else if(student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=9){
								className=className+" "+admittedDetails.getCourse().getProgram().getName().toUpperCase();
							}else{
								className=className+" "+admittedDetails.getCourse().getName().toUpperCase();
							}*/
							
							
							if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
								// /*code added by chandra
								if(form.getToCollege().equalsIgnoreCase("MIC")){
									if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3 || student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==9){
										className = student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
										form.setFlag(true);
									}else{
										className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
									}
								}else if(form.getToCollege().equalsIgnoreCase("Cjc")){
									className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
								}
								// */
							}
							detailsTo.setAdmissionClass(className);
							
							if(student.getAdmAppln().getAdmissionDate()!=null){
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
							}
							
							if(form.getTcDate()!=null && !form.getTcDate().isEmpty()){
								student.setTcDate(CommonUtil.ConvertStringToDate(form.getTcDate()));
								}else{
									student.setTcDate(new Date());
								}
							detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							if(rejoinYear!= null){
								detailsTo.setAdmittedYear(student.getRegisterNo() + "/" + rejoinYear+ "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
							}
							else{
								detailsTo.setAdmittedYear(student.getRegisterNo() + "/" +student.getAdmAppln().getAppliedYear().toString() + "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
							}

							
							
							Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
							while(itr.hasNext())
							{
								StudentTCDetails tcDetails=itr.next();
								

								//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
								detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
								detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());
								detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
								
								if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no"))
									detailsTo.setPassed(tcDetails.getSubjectPassed());
								else	
								if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
									detailsTo.setPassed("YES");
								
								if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
									detailsTo.setFeePaid("Yes");
								else
								if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
									detailsTo.setFeePaid("No");
								if(tcDetails.getCharacterAndConduct()!=null)
								detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
								
								detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd-MM-yyyy"));
								
								detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd-MM-yyyy"));
								
								
								if(form.getTcType() != null && form.getTcType().equalsIgnoreCase("Discontinued")){
									detailsTo.setRegMonthYear("---------");
								}else{
									detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());
								}
								if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
									detailsTo.setScholarship("YES");
								else
								if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
									detailsTo.setScholarship("NO");
								
								detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied().toUpperCase());
								if(tcDetails.getPromotionToNextClass()!=null)
									detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
								
								detailsTo.setReason(tcDetails.getReasonOfLeaving());
								if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
								{
									Integer year = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
									String yr = String.valueOf(year);
									detailsTo.setAcademicYear(yr);
								}	
								if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
									detailsTo.setEligible("YES");
								else
								if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
									detailsTo.setEligible("NO");
								
								if(tcDetails.getIsFeePaidUni()!= null && tcDetails.getIsFeePaidUni()){
									detailsTo.setFeeDueToUni("YES");
								}
								else{
									detailsTo.setFeeDueToUni("NO");
								}
								
								if(tcDetails.getPublicExaminationName()!=null)
									detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());
								//Ashwini
								if(tcDetails.getPromotionToNextClass()!=null)
									detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
								
								if(tcDetails.getYear()!=null) 
									detailsTo.setExamyear(tcDetails.getYear());									
								
								if(tcDetails.getExamMonth()!=null)
								{
									detailsTo.setExammonth(tcDetails.getExamMonth());
								}
								if(tcDetails.getExamYear()!=null)
								{
									detailsTo.setExamyear(tcDetails.getExamYear());
								}	
								if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
								{
									detailsTo.setPassed("Yes");
								}
								else
									detailsTo.setPassed("No");
								if(tcDetails.getSubjectPassed()!=null)
									detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
								if(tcDetails.getSubjectFailed()!=null)
									detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
								detailsTo.setIsAppeared(tcDetails.getPassed());
							}
							
												
							//TCNumber tcNumber=null;
							/*for(TCNumber tc:number)
							{
								if(tc.getType().equalsIgnoreCase("TC Convocation")){
									tcNumber=tc;
								}
							}*/
							
							//int currentTcNo=tcNumber.getCurrentNo();
							
							
							detailsTo.setTcNo(student.getTcNo());
							detailsTo.setTcType(student.getTcType());
							
					// Adding all the regNos avaialbe in detention rejoin details for the student-Smitha
							StringBuffer detainedRegNos=new StringBuffer();
							Set<String> detainedRegNoSet=new TreeSet<String>(Collections.reverseOrder());
							Set<ExamStudentDetentionRejoinDetails> detentionRejoinSet=student.getExamStudentDetentionRejoinDetails();
							Iterator<ExamStudentDetentionRejoinDetails> detainItr=detentionRejoinSet.iterator();
							while (detainItr.hasNext()) {
								ExamStudentDetentionRejoinDetails examStudentDetentionRejoinDetails = (ExamStudentDetentionRejoinDetails) detainItr.next();
								if(examStudentDetentionRejoinDetails.getRejoin()!=null && examStudentDetentionRejoinDetails.getRejoin())
								detainedRegNoSet.add(examStudentDetentionRejoinDetails.getRegisterNo());
							}
							if(!detainedRegNoSet.isEmpty()){
								Iterator<String> setItr=detainedRegNoSet.iterator();
								while (setItr.hasNext()) {
									String string = (String) setItr.next();
									if(detainedRegNos.length()>0){
										detainedRegNos.append(", "+string);
									}else detainedRegNos.append(string);
								}
							}
							if(detainedRegNos.length()>0)
								detailsTo.setDetainedRegNos(detainedRegNos.toString());
							
							//tcNumber.setCurrentNo(++currentTcNo);
							
							student.setTcNo(detailsTo.getTcNo());
							student.setTcType(detailsTo.getTcType());
							studentsGotTcList.add(student);
							studentToList.add(detailsTo);
						}	
					}
					else
					if(form.getDuplicate().equalsIgnoreCase("no"))
					{
		
						PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
						detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
						if(student.getStudentTCDetails().size()!=0)
						{	
							String name=student.getAdmAppln().getPersonalData().getFirstName();
							int courseId=student.getAdmAppln().getCourseBySelectedCourseId().getId();
							if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
									student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
								name+= " " + student.getAdmAppln().getPersonalData().getMiddleName();
							if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
									student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
								name+= " " + student.getAdmAppln().getPersonalData().getLastName();
							detailsTo.setStudentName(name);
							
							detailsTo.setStudentNo(student.getStudentNo());
							
							detailsTo.setRegNo(student.getRegisterNo());
							
							detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
							
							detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							
							String dobInWords=convertBirthDateToWord(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy")).toUpperCase();
							dobInWords = dobInWords.replace(',', ' ');
							detailsTo.setDobWords(dobInWords);
							
							
							String schemeInWords = getSchemeinWords(student.getClassSchemewise().getCurriculumSchemeDuration().getCurriculumScheme().getCourseScheme().getName(), student.getClassSchemewise().getClasses().getTermNumber());
							String className="";
							if(schemeInWords!=null){
								className=schemeInWords.toUpperCase();
							}
							/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
							}else if(CMSConstants.PG_ID.contains(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()) || 
								student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
							}else if(student.getClassSchemewise().getClasses().getCourse().getProgram().getId()!=9){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getName().toUpperCase();
							}else
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
							*/
							if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
								className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
							}
							detailsTo.setClassName(className);
							
							String courseScheme = TransferCertificateHandler.getInstance().getCourseScheme(student.getAdmAppln().getCourseBySelectedCourseId().getId(), student.getAdmAppln().getAppliedYear());
							schemeInWords = getSchemeinWords(courseScheme, admittedSem);
							className="";
							
							if(schemeInWords!=null)
								className=schemeInWords.toUpperCase();
							
							/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
								className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
							}else if(CMSConstants.PG_ID.contains(student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()) ||
								student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()==3){
								className=className+" "+admittedDetails.getCourse().getName().toUpperCase();
							}else if(student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getId()!=9){
								className=className+" "+admittedDetails.getCourse().getProgram().getName().toUpperCase();
							}else{
								className=className+" "+admittedDetails.getCourse().getName().toUpperCase();
							}*/
							if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
								// /*code added by chandra
								if(form.getToCollege().equalsIgnoreCase("MIC")){
									if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3 || student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==9){
										className = student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
										form.setFlag(true);
									}else{
										className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
									}
								} else if(form.getToCollege().equalsIgnoreCase("Cjc")){
									className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
								}
								// */
							}
							detailsTo.setAdmissionClass(className);
							
							if(student.getAdmAppln().getAdmissionDate()!=null){
								detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
							}
							if(form.getTcDate()!=null && !form.getTcDate().isEmpty()){
								student.setTcDate(CommonUtil.ConvertStringToDate(form.getTcDate()));
								}else{
									student.setTcDate(new Date());
								}
							detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
							
							if(rejoinYear!= null){
								detailsTo.setAdmittedYear(student.getRegisterNo() + "/" + rejoinYear+ "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
							}
							else{
								detailsTo.setAdmittedYear(student.getRegisterNo() + "/" +student.getAdmAppln().getAppliedYear().toString() + "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
							}


							Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
							while(itr.hasNext())
							{
								StudentTCDetails tcDetails=itr.next();
								
								//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
								detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
								detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
								detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());

								if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no")){
									detailsTo.setPassed( tcDetails.getPassed().toUpperCase()+"; "+tcDetails.getSubjectPassed().toUpperCase());
								}else{	
								if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
									detailsTo.setPassed("YES");
								}
								
								if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
									detailsTo.setFeePaid("Yes");
								else
								if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
									detailsTo.setFeePaid("No");
								if(tcDetails.getCharacterAndConduct()!=null)
								detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
								
								detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd/MM/yyyy"));
								
								detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd/MM/yyyy"));
								
								if(form.getTcType() != null && form.getTcType().equalsIgnoreCase("Discontinued")){
									detailsTo.setRegMonthYear("---------");
								}else{
									detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());

								}
								if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
									detailsTo.setScholarship("YES");
								else
								if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
									detailsTo.setScholarship("NO");
								
								//detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
								
								detailsTo.setReason(tcDetails.getReasonOfLeaving());
								if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
								{
									Integer year = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
									String yr = String.valueOf(year);
									detailsTo.setAcademicYear(yr);
								}
								if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
									detailsTo.setEligible("YES");
								else
								if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
									detailsTo.setEligible("NO");
								
								if(discontinuedStudentId.contains(student.getId())){
									detailsTo.setPublicExamName("DISCONTINUED");
								}
								else{
									if(tcDetails.getPublicExaminationName()!=null)
									detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());
								}
								//Added by sudhir
								if(tcDetails.getClassOfLeaving()!=null && !tcDetails.getClassOfLeaving().isEmpty()){
									detailsTo.setClassOfLeaving(tcDetails.getClassOfLeaving());
								}
								// added by ashwini
								if(tcDetails.getPromotionToNextClass()!=null)
									detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
								
								if(tcDetails.getYear()!=null) 
									detailsTo.setExamyear(tcDetails.getYear());									
								
								if(tcDetails.getExamMonth()!=null)
								{
									detailsTo.setExammonth(tcDetails.getExamMonth());
								}
								if(tcDetails.getExamYear()!=null)
								{
									detailsTo.setExamyear(tcDetails.getExamYear());
								}
								if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
								{
									detailsTo.setPassed("Yes");
								}
								else
									detailsTo.setPassed("No");
								
								String subjectStudied="";
								if(tcDetails.getSubjectsPassedCore()!=null){
									detailsTo.setSubjectPassedCore(tcDetails.getSubjectsPassedCore());
									subjectStudied = subjectStudied+tcDetails.getSubjectsPassedCore();
								}
								if(tcDetails.getSubjectsPassedComplimentary()!=null){
									detailsTo.setSubjectsPassedComplimentary(tcDetails.getSubjectsPassedComplimentary());
									subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedComplimentary();
								}
								if(tcDetails.getSubjectsPassedOptional()!=null){
									detailsTo.setSubjectPassedOptional(tcDetails.getSubjectsPassedOptional());
									subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedOptional();
								}
								detailsTo.setSubjectsStudied(subjectStudied);
								if(tcDetails.getSubjectPassed()!=null)
									detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
								if(tcDetails.getSubjectFailed()!=null)
									detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
								detailsTo.setIsAppeared(tcDetails.getPassed());
							}
													
							TCNumber tcNumber=null;
							for(TCNumber tc:number){
								if(tc.getType().equalsIgnoreCase("TC"))
									tcNumber=tc;
							}
							
							int currentTcNo=tcNumber.getCurrentNo();
							int slNo=tcNumber.getSlNo();
							if(form.getToCollege().equalsIgnoreCase("MIC"))
							{
								detailsTo.setTcNo(student.getTcNo());
								detailsTo.setSlNo(""+slNo);
								detailsTo.setTcType(form.getTcType());
								tcNumber.setCurrentNo(++currentTcNo);
							}
							else
							{
								student.setTcNo(detailsTo.getTcNo());
								detailsTo.setTcNo(student.getRegisterNo());
								detailsTo.setSlNo(getSlno(slNo,form.getYear(),courseId));
							}
							
							detailsTo.setTcNo(student.getTcNo());
							detailsTo.setTcType(form.getTcType());
							tcNumber.setCurrentNo(++currentTcNo);
							
							// Adding all the regNos avaialbe in detention rejoin details for the student-Smitha
							StringBuffer detainedRegNos=new StringBuffer();
							Set<String> detainedRegNoSet=new TreeSet<String>(Collections.reverseOrder());
							Set<ExamStudentDetentionRejoinDetails> detentionRejoinSet=student.getExamStudentDetentionRejoinDetails();
							Iterator<ExamStudentDetentionRejoinDetails> detainItr=detentionRejoinSet.iterator();
							while (detainItr.hasNext()) {
								ExamStudentDetentionRejoinDetails examStudentDetentionRejoinDetails = (ExamStudentDetentionRejoinDetails) detainItr.next();
								if(examStudentDetentionRejoinDetails.getRejoin()!=null && examStudentDetentionRejoinDetails.getRejoin())
								detainedRegNoSet.add(examStudentDetentionRejoinDetails.getRegisterNo());
							}
							if(!detainedRegNoSet.isEmpty()){
								Iterator<String> setItr=detainedRegNoSet.iterator();
								while (setItr.hasNext()) {
									String string = (String) setItr.next();
									if(detainedRegNos.length()>0){
										detainedRegNos.append(", "+string);
									}else detainedRegNos.append(string);
								}
							}
							if(detainedRegNos.length()>0)
								detailsTo.setDetainedRegNos(detainedRegNos.toString());
							
							student.setTcNo(detailsTo.getTcNo());
							student.setTcType(tcNumber.getType());
							
							number.clear();
							number.add(tcNumber);
							
							studentsGotTcList.add(student);
							studentToList.add(detailsTo);
						}	
					}
				}	
			}
		}	
		return studentToList;
	}

	private String convertBirthDateToWord(String dobFigures) 
	{
		String inWords="";
		StringTokenizer str=new StringTokenizer(dobFigures,"/");
		int date=Integer.parseInt(str.nextToken());
		int month=Integer.parseInt(str.nextToken());
		String year=str.nextToken();
		if(Integer.parseInt(year) >= 2000) {
			inWords=NumberToWordConvertor.getDate(date)+" "+CommonUtil.getMonthForNumber(month)+" "+NumberToWordConvertor.convertNumber(year).toUpperCase();
		} else {
			String year1=year.substring(0, 2);
			String year2=year.substring(2);
			inWords=NumberToWordConvertor.getDate(date)+" "+CommonUtil.getMonthForNumber(month)+" "+NumberToWordConvertor.convertNumber(year1).toUpperCase()+" "+NumberToWordConvertor.convertNumber(year2).toUpperCase();
		}
		
		return inWords;
	}
	/**
	 * 
	 * @param studentBoList
	 * @param request
	 * @param certificateForm 
	 * @return
	 * @throws Exception
	 */
	public List<PrintTcDetailsTo> convertStudentBoToToForReprintOnlyTc(List<Student> studentBoList, HttpServletRequest request, TransferCertificateForm certificateForm)throws Exception  
	{
		List<PrintTcDetailsTo>studentToList=new ArrayList<PrintTcDetailsTo>();
		ArrayList<Integer> studentList = new ArrayList<Integer>() ;
		ArrayList<Integer> studentListWithMarksEntryList = new ArrayList<Integer>() ;
		List studentMarksList = null;
		if(studentBoList.size()>0){
			Iterator<Student> itr = studentBoList.iterator();
			while(itr.hasNext()){
				Student stu = itr.next();
				studentList.add(stu.getId());
			}
			
		String marksEntryQuery= "select e.id,e.student_id from EXAM_marks_entry e "+
        " where e.class_id =" + certificateForm.getClasses()+
        " and e.student_id in(:ids) ";
		
		studentMarksList = transferCertificate.getStudentMarks(marksEntryQuery, studentList);
		if(studentMarksList.size()>0 && !studentMarksList.isEmpty()){
			Iterator<Object[]> itr1 =studentMarksList.iterator();
			while(itr.hasNext()){
				Object[] obj=itr1.next();
				if(obj[1]!=null)
				studentListWithMarksEntryList.add(Integer.parseInt(obj[1].toString()));
			    
			}
		}
		
		}		
		if(studentBoList!=null)
		{
			for(Student student:studentBoList)
			{	
				PrintTcDetailsTo detailsTo=new PrintTcDetailsTo();
				if(student.getStudentTCDetails().size()!=0)
				{		
					Integer admittedSem = TransferCertificateHandler.getInstance().getAdmittedSemester(student.getId());
					//String oldClassName= TransferCertificateHandler.getInstance().getOldClassNameByStudentIdNEW(student.getId(),admittedSem );
					BigInteger rejoinYear = TransferCertificateHandler.getInstance().getRejoinYear(student.getId());
					
					String name=student.getAdmAppln().getPersonalData().getFirstName();
					if(student.getAdmAppln().getPersonalData().getMiddleName()!=null && 
							student.getAdmAppln().getPersonalData().getMiddleName().trim().length()!=0)
						name+= " " + student.getAdmAppln().getPersonalData().getMiddleName();
					if(student.getAdmAppln().getPersonalData().getLastName()!=null && 
							student.getAdmAppln().getPersonalData().getLastName().trim().length()!=0)
						name+= " " + student.getAdmAppln().getPersonalData().getLastName();
					detailsTo.setStudentName(name);
					
					detailsTo.setStudentNo(student.getStudentNo());
					
					detailsTo.setRegNo(student.getRegisterNo());
					
					detailsTo.setCourse(student.getAdmAppln().getCourseBySelectedCourseId().getName());
					
					detailsTo.setDobFigures(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy"));
				
					detailsTo.setAdmissionnumber(student.getAdmAppln().getAdmissionNumber());
					String dobInWords=convertBirthDateToWord(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getPersonalData().getDateOfBirth()),"dd-MMM-yyyy","dd/MM/yyyy")).toUpperCase();
					dobInWords = dobInWords.replace(',', ' ');
					detailsTo.setDobWords(dobInWords);
					
				

					
					String schemeInWords = getSchemeinWords(student.getClassSchemewise().getCurriculumSchemeDuration().getCurriculumScheme().getCourseScheme().getName(), student.getClassSchemewise().getClasses().getTermNumber());
					String className="";
					if(schemeInWords!=null){
						className=schemeInWords.toUpperCase();
					}
					/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
					}else if(CMSConstants.PG_ID.contains(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId())
							||student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
					}else if(student.getClassSchemewise().getClasses().getCourse().getProgram().getId()!=9){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getName().toUpperCase();
					}else
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
				*/
					if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
						className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
					}
					detailsTo.setClassName(className);
					
					String courseScheme = TransferCertificateHandler.getInstance().getCourseScheme(student.getAdmAppln().getCourseBySelectedCourseId().getId(), student.getAdmAppln().getAppliedYear());
					schemeInWords = getSchemeinWords(courseScheme, admittedSem);
					className="";
					
					if(schemeInWords!=null)
						className=schemeInWords.toUpperCase();
					/*if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==1){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getCode();
					}else if(CMSConstants.PG_ID.contains(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId())
							||student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();
					}else if(student.getClassSchemewise().getClasses().getCourse().getProgram().getId()!=9){
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getProgram().getName().toUpperCase();
					}else
						className=className+" "+student.getClassSchemewise().getClasses().getCourse().getName().toUpperCase();*/
					if(student.getClassSchemewise().getClasses().getCourse() != null && student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard() != null){
						// /* code added by chandra 
						if(certificateForm.getToCollege().equalsIgnoreCase("MIC")){
							if(student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==3 || student.getClassSchemewise().getClasses().getCourse().getProgram().getProgramType().getId()==9){
								className = student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
								certificateForm.setFlag(true);
							}else{
								className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
							}
						}else if(certificateForm.getToCollege().equalsIgnoreCase("Cjc")){
							className = className +" "+ student.getClassSchemewise().getClasses().getCourse().getCourseMarksCard().toUpperCase();
						}
						// */
					}
					detailsTo.setAdmissionClass(className);
		
					
					
					if(student.getAdmAppln().getAdmissionDate()!=null){
						detailsTo.setDateOfAdmission(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getAdmAppln().getAdmissionDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
					}
					if(certificateForm.getTcReDate()!=null && !certificateForm.getTcReDate().isEmpty())
					{
						detailsTo.setTcDate(certificateForm.getTcReDate());
					}else{
					detailsTo.setTcDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(student.getTcDate()),"dd-MMM-yyyy","dd/MM/yyyy"));
					}
					if(rejoinYear!= null){
						detailsTo.setAdmittedYear(student.getRegisterNo() + "/" + rejoinYear+ "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
					}
					else{
						detailsTo.setAdmittedYear(student.getRegisterNo() + "/" +student.getAdmAppln().getAppliedYear().toString() + "-"  + (student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear() + 1) );
					}

					
					
					Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
					while(itr.hasNext())
					{
						StudentTCDetails tcDetails=itr.next();
						
						//detailsTo.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfLeaving()),"dd-MMM-yyyy","dd/MM/yyyy"));
						detailsTo.setDateOfLeaving(tcDetails.getDateOfLeavingNew());
						detailsTo.setLeavingYear(detailsTo.getDateOfLeaving());
						detailsTo.setIsStudentPunished(tcDetails.getIsStudentPunished());

						if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("no")){
							detailsTo.setPassed( tcDetails.getPassed().toUpperCase()+"; "+tcDetails.getSubjectPassed().toUpperCase());
						}else{	
						if(tcDetails.getPassed()!=null && tcDetails.getPassed().equalsIgnoreCase("yes"))	
							detailsTo.setPassed("YES");
						}
						
						if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("yes"))
							detailsTo.setFeePaid("Yes");
						else
						if(tcDetails.getFeePaid()!=null && tcDetails.getFeePaid().equalsIgnoreCase("no"))	
							detailsTo.setFeePaid("No");
						if(tcDetails.getCharacterAndConduct()!=null)
						detailsTo.setConduct(tcDetails.getCharacterAndConduct().getName().toUpperCase());
						
						detailsTo.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(tcDetails.getDateOfApplication()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						detailsTo.setDateOfIssue(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()),"dd-MMM-yyyy","dd/MM/yyyy"));
						
						if(certificateForm.getTcType() != null && certificateForm.getTcType().equalsIgnoreCase("Discontinued")){
							detailsTo.setRegMonthYear("---------");
						}else{
							detailsTo.setRegMonthYear(student.getRegisterNo()+", "+monthMap.get(tcDetails.getMonth())+" "+tcDetails.getYear());

						}
						if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("yes"))
							detailsTo.setScholarship("YES");
						else
						if(tcDetails.getScholarship()!=null && tcDetails.getScholarship().equals("no"))	
							detailsTo.setScholarship("NO");
						
						//detailsTo.setSubjectsStudied(tcDetails.getSubjectStudied());
						
						detailsTo.setReason(tcDetails.getReasonOfLeaving());
						if(student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()!=null)
						{
							Integer year = student.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear();
							String yr = String.valueOf(year);
							detailsTo.setAcademicYear(yr);
						}
						if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("Yes"))
							detailsTo.setEligible("YES");
						else
						if(tcDetails.getEligible()!=null && tcDetails.getEligible().equalsIgnoreCase("No"))
							detailsTo.setEligible("NO");

						if(tcDetails.getPublicExaminationName()!=null)
							detailsTo.setPublicExamName(tcDetails.getPublicExaminationName().toUpperCase());

						//Added by sudhir
						if(tcDetails.getClassOfLeaving()!=null && !tcDetails.getClassOfLeaving().isEmpty()){
							detailsTo.setClassOfLeaving(tcDetails.getClassOfLeaving());
						}
						// added by ashwini
						if(tcDetails.getPromotionToNextClass()!=null)
							detailsTo.setPromotionToNextClass(tcDetails.getPromotionToNextClass());
						
						if(tcDetails.getYear()!=null) 
							detailsTo.setExamyear(tcDetails.getYear());									
						
						if(tcDetails.getExamMonth()!=null)
						{
							detailsTo.setExammonth(tcDetails.getExamMonth());
						}
						if(tcDetails.getExamYear()!=null)
						{
							detailsTo.setExamyear(tcDetails.getExamYear());
						}
						if(tcDetails.getPassed()!=null &&  tcDetails.getPassed().equalsIgnoreCase("yes"))
						{
							detailsTo.setPassed("Yes");
						}
						else
							detailsTo.setPassed("No");
						
						String subjectStudied="";
						if(tcDetails.getSubjectsPassedCore()!=null){
							detailsTo.setSubjectPassedCore(tcDetails.getSubjectsPassedCore());
							subjectStudied = subjectStudied+tcDetails.getSubjectsPassedCore();
						}
						if(tcDetails.getSubjectsPassedComplimentary()!=null){
							detailsTo.setSubjectsPassedComplimentary(tcDetails.getSubjectsPassedComplimentary());
							subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedComplimentary();
						}
						if(tcDetails.getSubjectsPassedOptional()!=null){
							detailsTo.setSubjectPassedOptional(tcDetails.getSubjectsPassedOptional());
							subjectStudied = subjectStudied+","+tcDetails.getSubjectsPassedOptional();
						}
						detailsTo.setSubjectsStudied(subjectStudied);
						if(tcDetails.getSubjectPassed()!=null)
							detailsTo.setSubjectPassed(tcDetails.getSubjectPassed());
						if(tcDetails.getSubjectFailed()!=null)
							detailsTo.setSubjectFailed(tcDetails.getSubjectFailed());
						detailsTo.setIsAppeared(tcDetails.getPassed());
					}
					
										
					//TCNumber tcNumber=null;
					/*for(TCNumber tc:number)
					{
						if(tc.getType().equalsIgnoreCase("TC Convocation")){
							tcNumber=tc;
						}
					}*/
					
					//int currentTcNo=tcNumber.getCurrentNo();
					
					
					
					detailsTo.setTcNo(student.getTcNo());
					detailsTo.setTcType(student.getTcType());
					
					// Adding all the regNos avaialbe in detention rejoin details for the student-Smitha
					StringBuffer detainedRegNos=new StringBuffer();
					Set<String> detainedRegNoSet=new TreeSet<String>(Collections.reverseOrder());
					Set<ExamStudentDetentionRejoinDetails> detentionRejoinSet=student.getExamStudentDetentionRejoinDetails();
					Iterator<ExamStudentDetentionRejoinDetails> detainItr=detentionRejoinSet.iterator();
					while (detainItr.hasNext()) {
						ExamStudentDetentionRejoinDetails examStudentDetentionRejoinDetails = (ExamStudentDetentionRejoinDetails) detainItr.next();
						if(examStudentDetentionRejoinDetails.getRejoin()!=null && examStudentDetentionRejoinDetails.getRejoin())
						detainedRegNoSet.add(examStudentDetentionRejoinDetails.getRegisterNo());
					}
					if(!detainedRegNoSet.isEmpty()){
						Iterator<String> setItr=detainedRegNoSet.iterator();
						while (setItr.hasNext()) {
							String string = (String) setItr.next();
							if(detainedRegNos.length()>0){
								detainedRegNos.append(", "+string);
							}else detainedRegNos.append(string);
						}
					}
					if(detainedRegNos.length()>0)
						detailsTo.setDetainedRegNos(detainedRegNos.toString());
					
					//tcNumber.setCurrentNo(++currentTcNo);
					
					student.setTcNo(detailsTo.getTcNo());
					student.setTcType(detailsTo.getTcType());
					studentToList.add(detailsTo);
				}
			}	
		}
		return studentToList;
	}

	private String convertIntegerToWordForCJC(String dobFigures) 
	{
		String inWords="";
		StringTokenizer str=new StringTokenizer(dobFigures,"/");
		int date=Integer.parseInt(str.nextToken());
		int month=Integer.parseInt(str.nextToken());
		String year=str.nextToken();
		String year1=year.substring(0, 2);
		String year2=year.substring(2);
		inWords=NumberToWordConvertor.getDate(date)+", "+CommonUtil.getMonthForNumber(month)+", "+NumberToWordConvertor.convertNumber(year1).toUpperCase()+" HUNDRED "+NumberToWordConvertor.convertNumber(year2).toUpperCase();
		return inWords;
	}
	public static Set<String> uniques(String input){

		if(input == null || input.isEmpty()){
			return Collections.emptySet();
		}
		Set<String> uniques = new HashSet<String>();

		String[] words = input.split(",");

		for(String word : words){            
			uniques.add(word);            
		}
		return uniques;
	}
	
}
