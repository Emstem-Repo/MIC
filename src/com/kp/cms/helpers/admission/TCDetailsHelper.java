package com.kp.cms.helpers.admission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.CharacterAndConduct;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CurriculumSchemeDuration;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.forms.admission.TCDetailsForm;
import com.kp.cms.handlers.admission.TCDetailsHandler;
import com.kp.cms.helpers.exam.DownloadHallTicketHelper;
import com.kp.cms.to.admission.BoardDetailsTO;
import com.kp.cms.to.admission.CharacterAndConductTO;
import com.kp.cms.to.admission.TCDetailsTO;
import com.kp.cms.to.exam.ExamDefinitionTO;
import com.kp.cms.transactions.admission.ITCDetailsTransaction;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactionsimpl.admission.TCDetailsTransactionImpl;
import com.kp.cms.transactionsimpl.admission.TransferCertificateTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class TCDetailsHelper {
	private static final String FROM_DATEFORMAT = "dd/MM/yyyy";
	private static final String SQL_DATEFORMAT = "dd-MMM-yyyy";
	/**
	 * Singleton object of TCDetailsHelper
	 */
	private static volatile TCDetailsHelper tCDetailsHelper = null;
	private static final Log log = LogFactory.getLog(TCDetailsHelper.class);
	private TCDetailsHelper() {
		
	}
	/**
	 * return singleton object of TCDetailsHelper.
	 * @return
	 */
	public static TCDetailsHelper getInstance() {
		if (tCDetailsHelper == null) {
			tCDetailsHelper = new TCDetailsHelper();
		}
		return tCDetailsHelper;
	}
	
	/**
	 * @param boardDetailsForm
	 * @return
	 * @throws Exception
	 */
	public String getSearchQuery(TCDetailsForm tcDetailsForm) throws Exception {
		String query="from Student s where  s.admAppln.isCancelled=0 " +
				" and s.isActive=1 and s.classSchemewise.id="+tcDetailsForm.getClassId();
		if(tcDetailsForm.getRegisterNo()!=null && !tcDetailsForm.getRegisterNo().isEmpty()){
			query=query+" and s.registerNo='"+tcDetailsForm.getRegisterNo()+"'";
		}
		return query+" order by s.admAppln.personalData.firstName";
	}
	
	/**
	 * @param studentList
	 * @return
	 */
	public List<BoardDetailsTO> convertBotoToList(List<Student> studentList) {
		List<BoardDetailsTO> boardDetailsTOs=new ArrayList<BoardDetailsTO>();
		BoardDetailsTO boardDetailsTO;
		if(studentList!=null && !studentList.isEmpty()){
			Iterator<Student> iterator=studentList.iterator();
			while (iterator.hasNext()) {
				Student student = (Student) iterator.next();
				boardDetailsTO=new BoardDetailsTO();
				boardDetailsTO.setRegisterNo(student.getRegisterNo());
				boardDetailsTO.setStudentId(student.getId());
				boardDetailsTO.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
				boardDetailsTOs.add(boardDetailsTO);
			}
		}
		Collections.sort(boardDetailsTOs);
		return boardDetailsTOs;
	}
	/**
	 * @param tcDetailsForm
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForTCDetails(TCDetailsForm tcDetailsForm) throws Exception {
		String query=" from Student s where (s.admAppln.isSelected=1 and s.admAppln.isApproved=1 OR (s.admAppln.isCancelled = 1)) and" + ("R".equals(tcDetailsForm.getSearchBy()) ? " s.registerNo like '" + tcDetailsForm.getRegisterNo() + "'" : " s.admAppln.admissionNumber like '" + tcDetailsForm.getRegisterNo() + "'");
		return query;
	}
	/**
	 * @param student
	 * @param tcDetailsForm
	 * @throws Exception
	 */
	public void convertBotoForm(Student student, TCDetailsForm tcDetailsForm) throws Exception {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                 		
		TCDetailsTO to=new TCDetailsTO();
		
		to.setRegisterNo(student.getRegisterNo());
		to.setAdmissionNo(student.getAdmAppln().getAdmissionNumber());
		to.setStudentName(student.getAdmAppln().getPersonalData().getFirstName());
		to.setStudentId(student.getId());
		to.setDateOfBirth(CommonUtil.formatDate(student.getAdmAppln().getPersonalData().getDateOfBirth(), "dd/MM/yyyy"));
		tcDetailsForm.setCourseId(String.valueOf(student.getAdmAppln().getCourseBySelectedCourseId().getId()));
		
		int termNumber = student.getClassSchemewise().getClasses().getTermNumber();
		String semNo=DownloadHallTicketHelper.semMap.get(String.valueOf(termNumber));
		
		String className= student.getAdmAppln().getCourseBySelectedCourseId().getName();
		to.setClassOfLeaving(className+" "+semNo+" Semester");
		
		List<Subject> subjects = TCDetailsTransactionImpl.getInstance().getStudentSubjects(student.getAdmAppln().getId());
		List<CurriculumSchemeDuration> curriculumSchemeDuration = TCDetailsTransactionImpl.getInstance().getCurriculumSchemeDuration(tcDetailsForm.getClassId(),student.getId());
		
		if(subjects!=null)
		{
			Iterator<Subject> itr = subjects.iterator();
			StringBuffer coreSubjects = new StringBuffer();
			StringBuffer complementarySubjects = new StringBuffer();
			StringBuffer optionalSubjects = new StringBuffer();
			while(itr.hasNext())
			{
				Subject bo = itr.next();
				if(bo.getSubjectType().getId() == 1)
				{
					if(coreSubjects.length()<0 || coreSubjects.length()==0)
					coreSubjects.append(bo.getConsolidatedSubjectStream().getStreamName());
					
				}
				else if(bo.getSubjectType().getId() == 2){
					complementarySubjects.append(bo.getDepartment().getName());
					complementarySubjects.append(",");
				}
				else if(bo.getIsOptionalSubject()){
					optionalSubjects.append(bo.getDepartment().getName());
					optionalSubjects.append(",");
				}
			}
			
			if(coreSubjects.length()!=0) {
				to.setSubjectsPassedCore(coreSubjects.toString());	
			}
			if(complementarySubjects.length()!=0) {
				complementarySubjects.deleteCharAt(complementarySubjects.length()-1);	
				String s2 = complementarySubjects.toString();			
		        String result2 = "";
		        char s2FirstChar = s2.charAt(0);
		        result2 = result2 + Character.toUpperCase(s2FirstChar);
		        for (int i = 1; i < s2.length(); i++) {
		            char currentChar = s2.charAt(i);
		            char previousChar = s2.charAt(i - 1);
		            if (previousChar==','||previousChar=='-') {
		                result2 = result2 + Character.toUpperCase(currentChar);
		            } else {
		            	result2 = result2 + Character.toLowerCase(currentChar);
		            }
		        }
		        if(result2!=null && !result2.isEmpty())
				{
					Set<String> subjects1=TransferCertificateHelper.uniques(result2);
					Iterator subItr = subjects1.iterator(); 
					StringBuffer s = new StringBuffer();
					while(subItr.hasNext()){								
						s.append(subItr.next());	
						s.append(",");
					}
					s.deleteCharAt(s.length()-1);
				}				
			}
			
			if(optionalSubjects.length()!=0)
			{
				optionalSubjects.deleteCharAt(optionalSubjects.length()-1);	
				String s2 = optionalSubjects.toString();			
		        String result3 = "";
		        char s2FirstChar = s2.charAt(0);
		        result3 = result3 + Character.toUpperCase(s2FirstChar);
		        for (int i = 1; i < s2.length(); i++) {
		            char currentChar = s2.charAt(i);
		            char previousChar = s2.charAt(i - 1);
		            if (previousChar==','||previousChar=='-') {
		            	result3 = result3 + Character.toUpperCase(currentChar);
		            } else {
		            	result3 = result3 + Character.toLowerCase(currentChar);
		            }
		        }
		        if(result3!=null && !result3.isEmpty())
				{
					Set<String> subjects1=TransferCertificateHelper.uniques(result3);
					Iterator subItr = subjects1.iterator(); 
					StringBuffer s = new StringBuffer();
					while(subItr.hasNext()){								
						s.append(subItr.next());	
						s.append(",");
					}
					s.deleteCharAt(s.length()-1);
				}				
			}
		}
		
		if(curriculumSchemeDuration!=null)
		{
			Iterator<CurriculumSchemeDuration> itr = curriculumSchemeDuration.iterator();
			while(itr.hasNext())
			{
				CurriculumSchemeDuration csd = new CurriculumSchemeDuration();
				csd = itr.next();
				ExamDefinitionBO examName = TCDetailsTransactionImpl.getInstance().getStudentExamName(student.getId(),csd);
				if(examName!=null)
				{
					to.setPublicExamName(examName.getName());
					String month = monthInWords(examName.getMonth());
					to.setExamMonth(month);
					to.setExamYear(examName.getYear());
					break;
				}
				else
				{
					to.setPublicExamName("");
				}
			}
		}	
				
		if(student.getStudentTCDetails()!=null && !student.getStudentTCDetails().isEmpty()){
			Iterator<StudentTCDetails> itr=student.getStudentTCDetails().iterator();
			if (itr.hasNext()) {
				StudentTCDetails bo=(StudentTCDetails)itr.next();
				to.setId(bo.getId());
				to.setFeePaid(bo.getFeePaid());
				to.setScholarship(bo.getScholarship());
				to.setPassed(bo.getPassed());
				to.setDateOfApplication(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(bo.getDateOfApplication()),TCDetailsHelper.SQL_DATEFORMAT,TCDetailsHelper.FROM_DATEFORMAT));
				//to.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(bo.getDateOfLeaving()),TCDetailsHelper.SQL_DATEFORMAT,TCDetailsHelper.FROM_DATEFORMAT));
				to.setDateOfLeaving(bo.getDateOfLeavingNew());
				to.setReasonOfLeaving(bo.getReasonOfLeaving());
				to.setLeavingAcademicYear(bo.getLeavingAcademicYear());
				if(bo.getCharacterAndConduct()!=null){
					to.setCharacterId(String.valueOf(bo.getCharacterAndConduct().getId()));
				}
				to.setMonth(bo.getMonth());
				to.setYear(String.valueOf(bo.getYear()));
				if(bo.getSubjectPassed()!=null)
				to.setSubjectPassed(bo.getSubjectPassed());
				if(bo.getSubjectFailed()!=null)
				to.setSubjectFailed(bo.getSubjectFailed());
				to.setPublicExamName(bo.getPublicExaminationName());
				tcDetailsForm.setPublicExamName(bo.getPublicExaminationName());
				to.setShowRegisterNo(bo.getShowRegNo());
				if(bo.getSubjectsPassedCore()!=null)
				to.setSubjectsPassedCore(bo.getSubjectsPassedCore());
				if(bo.getSubjectsPassedComplimentary()!=null)
				to.setSubjectsPassedComplimentary(bo.getSubjectsPassedComplimentary());
				if(bo.getSubjectsPassedOptional()!=null)
					to.setSubjectsPassedOptional(bo.getSubjectsPassedOptional());

				
				//raghu
				
				if(bo.getClasssubjectOfJoining()!=null)
				to.setClasssubjectOfJoining(bo.getClasssubjectOfJoining());
				if(bo.getPromotionToNextClass()!=null)
				to.setPromotionToNextClass(bo.getPromotionToNextClass());
				if(bo.getIsStudentPunished()!=null)
					to.setIsStudentPunished(bo.getIsStudentPunished());
				
				if(bo.getExamMonth()!=null)
				to.setExamMonth(bo.getExamMonth());
				if(bo.getExamYear()!=null)
				to.setExamYear(bo.getExamYear());
				to.setClassId(bo.getStudent().getClassSchemewise().getClasses().getId());
				
				if(bo.getDateOfAdmission() != null) {
					to.setDateOfAdmission(CommonUtil.formatDate(bo.getDateOfAdmission(), "dd/MM/yyyy"));
				}
				else {
					if(student.getAdmAppln().getAdmissionDate() != null) {
						to.setDateOfAdmission(CommonUtil.formatDate(student.getAdmAppln().getAdmissionDate(), "dd/MM/yyyy"));
					}
				}
			}
		}else{
			to.setFeePaid("Yes");
			to.setPassed("Yes");
			to.setScholarship("No");
			to.setShowRegisterNo("Yes");
			to.setYear(tcDetailsForm.getYear());
			to.setMonth(tcDetailsForm.getMonth());
		}
		
		tcDetailsForm.setProgramTypeId(String.valueOf(student.getAdmAppln().getCourseBySelectedCourseId().getProgram().getProgramType().getId()));
		tcDetailsForm.setSchemeNo(String.valueOf(student.getClassSchemewise().getClasses().getTermNumber()));
		List<Object[]> examNames = TCDetailsHandler.getInstance().getExamNames(tcDetailsForm);
		List<ExamDefinitionTO> examList = TCDetailsHandler.getInstance().convertExamNamesToForm(examNames);
		tcDetailsForm.setExamNames(examList);
		
		tcDetailsForm.setTcDetailsTO(to);
	}
	/**
	 * @param tcDetailsForm
	 * @return
	 * @throws Exception
	 */
	public StudentTCDetails convertTotoBo(TCDetailsForm tcDetailsForm) throws Exception{
		TCDetailsTO to=tcDetailsForm.getTcDetailsTO();
		StudentTCDetails bo=new StudentTCDetails();
		if(to!=null){
			if(to.getId()>0){
				bo.setId(to.getId());
			}else{
				bo.setCreatedBy(tcDetailsForm.getUserId());
				bo.setCreatedDate(new Date());
			}
			bo.setModifiedBy(tcDetailsForm.getUserId());
			bo.setLastModifiedDate(new Date());
			bo.setPassed(to.getPassed());
			bo.setFeePaid(to.getFeePaid());
			bo.setScholarship(to.getScholarship());
			bo.setReasonOfLeaving(to.getReasonOfLeaving());
			
			bo.setIsActive(true);
			//bo.setDateOfApplication(CommonUtil.ConvertStringToSQLDate(to.getDateOfApplication()));
			//bo.setDateOfLeaving(CommonUtil.ConvertStringToSQLDate(to.getDateOfLeaving()));
			bo.setDateOfLeavingNew(to.getDateOfLeaving());
			Student student=new Student();
			student.setId(to.getStudentId());
			bo.setStudent(student);
			if(to.getCharacterId()!=null && !to.getCharacterId().isEmpty()){
				CharacterAndConduct conduct=new CharacterAndConduct();
				conduct.setId(Integer.parseInt(to.getCharacterId()));
				bo.setCharacterAndConduct(conduct);
			}
			bo.setMonth(to.getMonth());
			bo.setYear(Integer.parseInt(to.getYear()));
			bo.setSubjectPassed(to.getSubjectPassed());
			bo.setSubjectFailed(to.getSubjectFailed());
			bo.setPublicExaminationName(tcDetailsForm.getPublicExamName());
			bo.setShowRegNo(to.getShowRegisterNo());
			//core & complementary
			if(to.getSubjectsPassedCore()!=null)
			bo.setSubjectsPassedCore(to.getSubjectsPassedCore());
			if(to.getSubjectsPassedComplimentary()!=null)
			bo.setSubjectsPassedComplimentary(to.getSubjectsPassedComplimentary());
			if(to.getSubjectsPassedOptional()!=null)
				bo.setSubjectsPassedOptional(to.getSubjectsPassedOptional());
			if(tcDetailsForm.getIsStudentPunished()!=null)
				bo.setIsStudentPunished(tcDetailsForm.getIsStudentPunished());
			if(tcDetailsForm.getClassName()!=null)
				bo.setClassOfLeaving(to.getClassOfLeaving());
			bo.setLeavingAcademicYear(tcDetailsForm.getLeavingAcademicYear());
			//Added by sudhir
			
			//raghu
			if(to.getClasssubjectOfJoining()!=null)
			bo.setClasssubjectOfJoining(to.getClasssubjectOfJoining());
			if(to.getPromotionToNextClass()!=null)
			bo.setPromotionToNextClass(to.getPromotionToNextClass());
			
			if(to.getExamMonth()!=null)
			{
				//String month = monthInWords(to.getExamMonth());
				String month = to.getExamMonth();
				bo.setExamMonth(month);
			}
			if(to.getExamYear()!=null)
			bo.setExamYear(to.getExamYear());
			bo.setDateOfAdmission(CommonUtil.ConvertStringToSQLDate(to.getDateOfAdmission()));
		}
		return bo;
	}
	/**
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<CharacterAndConductTO> convertBoListToTOList(List<CharacterAndConduct> list)  throws Exception{
		List<CharacterAndConductTO> finalList=new ArrayList<CharacterAndConductTO>();
		if(list!=null && !list.isEmpty()){
			Iterator<CharacterAndConduct> itr=list.iterator();
			while (itr.hasNext()) {
				CharacterAndConduct bo = (CharacterAndConduct) itr.next();
				CharacterAndConductTO to=new CharacterAndConductTO();
				to.setId(bo.getId());
				to.setName(bo.getName());
				finalList.add(to);
			}
		}
		return finalList;
	}
	
	public void convertBotoFormAllStudentsByClass(List<Object[]> subjects,
			ExamDefinitionBO bo, TCDetailsForm tcDetailsForm) throws NumberFormatException, Exception {		
		
		TCDetailsTO to=new TCDetailsTO();
		if(bo!=null)
		{
			String month = monthInWords(bo.getMonth());
			to.setExamMonth(month);
			to.setExamYear(bo.getYear());
			String examName = bo.getName();	
			StringBuilder sb = new StringBuilder();
		    boolean capNext = true;
		    final String ACTIONABLE_DELIMITERS = " '-/"; 
			for(char c : examName.toCharArray())
			{
				c = (capNext)
                ? Character.toUpperCase(c)
                : Character.toLowerCase(c);
                sb.append(c);
                capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); 
			}
			tcDetailsForm.setPublicExamName(sb.toString());
		}		
		to.setClassOfLeaving(tcDetailsForm.getClassName());				
	/*	if(subjects!=null)
		{
			Iterator<Object[]> itr= subjects.iterator();
			StringBuffer sb = new StringBuffer();
			while(itr.hasNext())
			{
				Object data = (Object)itr.next();
				if(data.toString()!=null)
				{					
					sb.append(data.toString());	
					sb.append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);	
			String s = sb.toString();
			String result = "";
	        char firstChar = s.charAt(0);
	        result = result + Character.toUpperCase(firstChar);
	        for (int i = 1; i < s.length(); i++) {
	            char currentChar = s.charAt(i);
	            char previousChar = s.charAt(i - 1);
	            if (previousChar==',') {
	                result = result + Character.toUpperCase(currentChar);
	            } else {
	                result = result + Character.toLowerCase(currentChar);
	            }
	        }
			to.setSubjectPassed(result);
			
		}*/
		if(subjects!=null)
		{
			Iterator<Object[]> itr= subjects.iterator();
			StringBuffer coreSubjects = new StringBuffer();
			StringBuffer complimentarySubjects = new StringBuffer();

			while(itr.hasNext())
			{
				Object[] data = (Object[])itr.next();
				if(data[1]!=null && data[1].toString().equalsIgnoreCase("9"))
				{					
					coreSubjects.append(data[0]);	
					coreSubjects.append(",");
				}
				else if(data[1]!=null && data[1].toString().equalsIgnoreCase("11"))
				{
					complimentarySubjects.append(data[0]);
					complimentarySubjects.append(",");
				}
			}
			if(coreSubjects.length()!=0)
			{
				coreSubjects.deleteCharAt(coreSubjects.length()-1);	
				String s1 = coreSubjects.toString();
				String result1 = "";
		        char s1FirstChar = s1.charAt(0);
		        result1 = result1 + Character.toUpperCase(s1FirstChar);
		        for (int i = 1; i < s1.length(); i++) {
		            char currentChar = s1.charAt(i);
		            char previousChar = s1.charAt(i - 1);
		            if (previousChar==','||previousChar=='-') {
		                result1 = result1 + Character.toUpperCase(currentChar);
		            } else {
		            	result1 = result1 + Character.toLowerCase(currentChar);
		            }
		        }
		        if(result1!=null && !result1.isEmpty())
				{
					Set<String> subjects1=TransferCertificateHelper.uniques(result1);
					Iterator subItr = subjects1.iterator(); 
					StringBuffer s = new StringBuffer();
					while(subItr.hasNext()){								
						s.append(subItr.next());	
						s.append(",");
					}
					s.deleteCharAt(s.length()-1);
					to.setSubjectsPassedCore(s.toString());
				}
				
		        
			}
			if(complimentarySubjects.length()!=0)
			{
				complimentarySubjects.deleteCharAt(complimentarySubjects.length()-1);	
				String s2 = complimentarySubjects.toString();			
		        String result2 = "";
		        char s2FirstChar = s2.charAt(0);
		        result2 = result2 + Character.toUpperCase(s2FirstChar);
		        for (int i = 1; i < s2.length(); i++) {
		            char currentChar = s2.charAt(i);
		            char previousChar = s2.charAt(i - 1);
		            if (previousChar==','||previousChar=='-') {
		                result2 = result2 + Character.toUpperCase(currentChar);
		            } else {
		            	result2 = result2 + Character.toLowerCase(currentChar);
		            }
		        }
		        if(result2!=null && !result2.isEmpty())
				{
					Set<String> subjects1=TransferCertificateHelper.uniques(result2);
					Iterator subItr = subjects1.iterator(); 
					StringBuffer s = new StringBuffer();
					while(subItr.hasNext()){								
						s.append(subItr.next());	
						s.append(",");
					}
					s.deleteCharAt(s.length()-1);
					to.setSubjectsPassedComplimentary(s.toString());	
				}
			}
		}
		ITransferCertificateTransaction transferCertificate= TransferCertificateTransactionImpl.getInstance();
		int termNumber = transferCertificate.getClassTermNumber(Integer.parseInt(tcDetailsForm.getClassId()));
		//String semNo=DownloadHallTicketHelper.semMap.get(String.valueOf(termNumber));
		String semNo=String.valueOf(termNumber);

		ITCDetailsTransaction transaction=TCDetailsTransactionImpl.getInstance();
		Classes classes = transaction.getClasses(tcDetailsForm);
		//to.setClassOfLeaving(classes.getCourse().getName()+" "+semNo+" Semester");
		to.setClassOfLeaving(semNo+" Sem"+" "+classes.getCourse().getProgram().getProgramType().getName()+" Programme");
		tcDetailsForm.setTcDetailsTO(to);				
	}
	
	public String monthInWords(String month1)
	{
		String month = Integer.parseInt(month1)+"";
		
		if(month.equalsIgnoreCase("1"))
		return "January";
		if(month.equalsIgnoreCase("2"))
			return "February";
		if(month.equalsIgnoreCase("3"))
			return "March";
		if(month.equalsIgnoreCase("4"))
			return "April";
		if(month.equalsIgnoreCase("5"))
			return "May";
		if(month.equalsIgnoreCase("6"))
			return "June";
		if(month.equalsIgnoreCase("7"))
			return "July";
		if(month.equalsIgnoreCase("8"))
			return "August";
		if(month.equalsIgnoreCase("9"))
			return "September";
		if(month.equalsIgnoreCase("10"))
			return "October";
		if(month.equalsIgnoreCase("11"))
			return "November";
		if(month.equalsIgnoreCase("12"))
			return "December";
		return month;
	}

}
