package com.kp.cms.helpers.exam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.ConsolidateMarksCard;
import com.kp.cms.bo.exam.ConsolidatedMarksCardProgrammePart;
import com.kp.cms.bo.exam.ExamStudentCCPA;
import com.kp.cms.bo.exam.ExamStudentSgpa;
import com.kp.cms.forms.exam.CertificateMarksCardForm;
import com.kp.cms.to.admin.SubjectTO;
import com.kp.cms.to.exam.ConsolidateMarksCardProgrammePartTO;
import com.kp.cms.to.exam.ConsolidateMarksCardTO;
import com.kp.cms.to.exam.ExamStudentSGPATO;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.transactions.exam.IConsolidatedMarksCardTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.IUpdateStudentSGPATxn;
import com.kp.cms.transactionsimpl.exam.ConsolidatedMarksCardTransactionImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.UpdateExamStudentSGPAImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.MarkComparator;
import com.kp.cms.utilities.PropertyUtil;
import com.kp.cms.utilities.SubjectGroupDetailsComparator;

public class CertificateMarksCardHelper {
	
	private static Map<Integer, String> semMap = null;
	static IDownloadHallTicketTransaction transaction=DownloadHallTicketTransactionImpl.getInstance();
	static INewExamMarksEntryTransaction txn=NewExamMarksEntryTransactionImpl.getInstance();
	private static Map<Integer, String> monthMap = null;
	
	static {
		semMap = new HashMap<Integer, String>();
		semMap.put(1, "I");
		semMap.put(2, "II");
		semMap.put(3, "III");
		semMap.put(4, "IV");
		semMap.put(5, "V");
		semMap.put(6, "VI");
		semMap.put(7, "VII");
		semMap.put(8, "VIII");
		semMap.put(9, "IX");
		semMap.put(10, "X");
		
		monthMap = new HashMap<Integer, String>();
		monthMap.put(0, "January");
		monthMap.put(1, "February");
		monthMap.put(2, "March");
		monthMap.put(3, "April");
		monthMap.put(4, "May");
		monthMap.put(5, "June");
		monthMap.put(6, "July");
		monthMap.put(7, "August");
		monthMap.put(8, "September");
		monthMap.put(9, "October");
		monthMap.put(10, "November");
		monthMap.put(11, "December");
	}
	
	/**
	 * Singleton object of CertificateMarksCardHelper
	 */
	private static volatile CertificateMarksCardHelper certificateMarksCardHelper = null;
	private static final Log log = LogFactory.getLog(CertificateMarksCardHelper.class);
	private CertificateMarksCardHelper() {
		
	}
	/**
	 * return singleton object of CertificateMarksCardHelper.
	 * @return
	 */
	public static CertificateMarksCardHelper getInstance() {
		if (certificateMarksCardHelper == null) {
			certificateMarksCardHelper = new CertificateMarksCardHelper();
		}
		return certificateMarksCardHelper;
	}
	/**
	 * @param studentId
	 * @return
	 */
	public String getStudentCertificateMarksCardQuery(int studentId) {
		String query="from ConsolidateMarksCard c where c.student.id="+studentId;
		return query;
	}
	/**
	 * @param boList
	 * @return
	 */
	public ConsolidateMarksCardTO convertBotoTo(List<ConsolidateMarksCard> boList,int sid, boolean checkAggregate) throws Exception {
		
		ConsolidateMarksCardTO to = null;
				
		if(boList != null && !boList.isEmpty()) {
			
			to = new ConsolidateMarksCardTO();
			
			IUpdateStudentSGPATxn iUpdateStudentSGPATxn = UpdateExamStudentSGPAImpl.getInstance();
			List<ExamStudentSgpa> sgpaBOs = iUpdateStudentSGPATxn.getStudentResultDetails(sid);
			List<ExamStudentSGPATO> sgpaTOs = convertSGPABOToTO(sgpaBOs);
			to.setSgpaTO(sgpaTOs);
			
			String monthAndYear = iUpdateStudentSGPATxn.getExamMonthAndYearFromCCPA(sid);
			if(monthAndYear != null) {
				
				String[] splitData = monthAndYear.split(",");
				if(splitData[0] != null && !splitData[0].isEmpty()) {
					to.setExamMonth(monthMap.get(Integer.parseInt(splitData[0])));
				}
				else {
					to.setExamMonth("0");	/** month not present in table */
				}
				if(Integer.parseInt(splitData[1]) != 0)
					to.setExamYear(Integer.parseInt(splitData[1]));
			}			
			
			IConsolidatedMarksCardTransaction iConsolidatedMarksCardTransaction = ConsolidatedMarksCardTransactionImpl.getInstance();
			List<ConsolidatedMarksCardProgrammePart> programmePartBOs = iConsolidatedMarksCardTransaction.getProgrammePartData(sid);
			List<ConsolidateMarksCardProgrammePartTO> programmePartTOs = convertProgrammePartBOToTO(programmePartBOs);
			Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> programmePartMap = getMapForDisplayPurpose(programmePartTOs);
			to.setProgrammePartMap(programmePartMap);
			
			ExamStudentCCPA studentCCPA = iConsolidatedMarksCardTransaction.getStudentCCPA(sid);
			if(studentCCPA != null) {
				
				to.setCcpa(studentCCPA.getCcpa());
				int creditsForDisplay = studentCCPA.getCreditsForDisplay()==null?0:Integer.parseInt(studentCCPA.getCreditsForDisplay()); 
				if(studentCCPA.getCredit() != null && !studentCCPA.getCredit().isEmpty())
					to.setCcpaCredit(Integer.valueOf(studentCCPA.getCredit())+creditsForDisplay);
				to.setCcpaGrade(studentCCPA.getGrade());
				if(studentCCPA.getSubjetcTypeBasedCgpa() != null && !studentCCPA.getSubjetcTypeBasedCgpa().isEmpty())
					to.setCcpas(Double.parseDouble(studentCCPA.getSubjetcTypeBasedCgpa()));
				if(studentCCPA.getSubjetcTypeBasedCredit() != null && !studentCCPA.getSubjetcTypeBasedCredit().isEmpty())
					to.setCcpasCredit(Integer.parseInt(studentCCPA.getSubjetcTypeBasedCredit()));
				to.setCcpasGrade(studentCCPA.getSubjetcTypeBasedGrade());
				if(studentCCPA.getResult()!=null)
					to.setResult(studentCCPA.getResult());
			}
			
			if(to.getStudentName() == null || to.getStudentName().isEmpty()) {
				to.setStudentName(boList.get(0).getStudent().getAdmAppln().getPersonalData().getFirstName());
			}
			if(to.getCourseName() == null || to.getCourseName().isEmpty()) {
				to.setCourseName(boList.get(0).getCourse().getName());
			}
			if(to.getRegisterNo() == null || to.getRegisterNo().isEmpty()) {
				to.setRegisterNo(boList.get(0).getStudent().getRegisterNo());
			}
			if(to.getCourseId() == 0) {
				to.setCourseId(boList.get(0).getCourse().getId());
			}
		}
		
		return to;
	}
	
	/**
	 * @param certificateList
	 * @return
	 */
	private Map<Integer, Map<Integer, Boolean>> getCertificateSubjectMap( List<Object[]> certificateList) {
		Map<Integer,Map<Integer,Boolean>> map=new HashMap<Integer, Map<Integer,Boolean>>();
		Map<Integer,Boolean> innerMap;
		if(certificateList!=null && !certificateList.isEmpty()){
			Iterator<Object[]> itr=certificateList.iterator();
			while (itr.hasNext()) {
				Object[] objects = (Object[]) itr.next();
				if(objects[0]!=null && objects[1]!=null && objects[2]!=null){
					if(map.containsKey(Integer.parseInt(objects[2].toString()))){
						innerMap=map.remove(Integer.parseInt(objects[2].toString()));
					}else
						innerMap=new HashMap<Integer, Boolean>();
					
					innerMap.put(Integer.parseInt(objects[0].toString()),(Boolean)objects[1]);
					map.put(Integer.parseInt(objects[2].toString()),innerMap);
				}
			}
		}
		return map;
	}
	/**
	 * @param certificateMarksCardForm
	 * @return
	 */
	public String getStudentIdsQueryForInput( CertificateMarksCardForm certificateMarksCardForm) throws Exception  {
		String query="select s.id from Student s where s.admAppln.isCancelled=0 and s.isAdmitted=1 and (s.isHide is null or s.isHide=0) and s.admAppln.courseBySelectedCourseId.id="+certificateMarksCardForm.getCourseId()+" and s.admAppln.appliedYear="+certificateMarksCardForm.getYear();
		if(certificateMarksCardForm.getRegFrom()!=null && !certificateMarksCardForm.getRegFrom().isEmpty()){
			query=query+" and s.registerNo>='"+certificateMarksCardForm.getRegFrom()+"' ";
		}
		if(certificateMarksCardForm.getRegTo()!=null && !certificateMarksCardForm.getRegTo().isEmpty()){
			query=query+" and s.registerNo<='"+certificateMarksCardForm.getRegTo()+"' ";
		}
		return query+" order by s.registerNo";
	}
	
	public List<ConsolidateMarksCardProgrammePartTO> convertProgrammePartBOToTO(List<ConsolidatedMarksCardProgrammePart> listBO) throws Exception {
		
		List<ConsolidateMarksCardProgrammePartTO> listTO = new ArrayList<ConsolidateMarksCardProgrammePartTO>();
		
		if(listBO != null && !listBO.isEmpty()) {
			
			Iterator<ConsolidatedMarksCardProgrammePart> it = listBO.iterator();
			while(it.hasNext()) {
				
				ConsolidatedMarksCardProgrammePart bo = it.next();
				ConsolidateMarksCardProgrammePartTO to = new ConsolidateMarksCardProgrammePartTO();
				
				if(bo.getStudent() != null) {					
					to.setStudentId(bo.getStudent().getId());
				}
				if(bo.getCourse() != null) {
					to.setCourseId(bo.getCourse().getId());
				}
				if(bo.getConsolidatedSubjectSection() != null) {
				
					to.setConsolidatedSubjectSectionId(bo.getConsolidatedSubjectSection().getId());
					to.setConsolidatedSubjectSectionName(bo.getConsolidatedSubjectSection().getSectionName());
				}
				if(bo.getSubjectStream() != null) {
					
					to.setConsolidatedSubjectStreamId(bo.getSubjectStream().getId());
					to.setConsolidatedSubjectStreamName(bo.getSubjectStream().getStreamName());
				}
				if(bo.getCcpa() != null) {
					
					to.setCcpa(Double.parseDouble(bo.getCcpa()));
				}
				int creditsForDisplay=  bo.getCreditsForDisplay()==null?0: Integer.parseInt(bo.getCreditsForDisplay());
				if(bo.getShowOnlyCredits()){
					to.setCredit(bo.getCredit());
					to.setCreditsForDisplay(creditsForDisplay);
				}
				else
					to.setCredit(bo.getCredit());
				to.setCreditPoint(Double.parseDouble(bo.getCreditPoints()));
				to.setShowStreamForSection(bo.getConsolidatedSubjectSection().getShowRespectiveStreams());
				
				listTO.add(to);
			}
		}
		return listTO;
	}
	
	public List<ExamStudentSGPATO> convertSGPABOToTO(List<ExamStudentSgpa> listBO) throws Exception {
		
		List<ExamStudentSGPATO> listTO = new ArrayList<ExamStudentSGPATO>();
		
		if(listBO != null && !listBO.isEmpty()) {
			
			Iterator<ExamStudentSgpa> it = listBO.iterator();
			while(it.hasNext()) {
				
				ExamStudentSgpa bo = it.next();
				ExamStudentSGPATO to = new ExamStudentSGPATO();
				
				to.setSchemeNo(bo.getSchemeNo());
				to.setSgpa(bo.getSgpa());
				int creditsForDisplay=bo.getCreditsForDisplay()==null?0: Integer.parseInt(bo.getCreditsForDisplay());
				if(bo.getCredit() != null && !bo.getCredit().isEmpty())
					to.setCredit(String.valueOf((Integer.parseInt(bo.getCredit())+creditsForDisplay)));
				
				listTO.add(to);
			}
		}
		Collections.sort(listTO);
		return listTO;
	}
	
	private Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> getMapForDisplayPurpose(List<ConsolidateMarksCardProgrammePartTO> list) throws Exception {
		
		Map<String, Map<String, ConsolidateMarksCardProgrammePartTO>> displayMap = new LinkedHashMap<String, Map<String,ConsolidateMarksCardProgrammePartTO>>();
		boolean isBcom = false;
		
		if(list != null && !list.isEmpty()) {
		
			ListIterator<ConsolidateMarksCardProgrammePartTO> it = list.listIterator();
			while(it.hasNext()) {
				
				ConsolidateMarksCardProgrammePartTO programmePartTO = it.next();
				
				if(displayMap.containsKey(String.valueOf(programmePartTO.getConsolidatedSubjectSectionName()))) {
					//if(programmePartTO.getConsolidatedSubjectSectionName().equalsIgnoreCase("Foundation Course")){//For ConsolidateSubjectSectionName 
					Map<String, ConsolidateMarksCardProgrammePartTO> innerMap = displayMap.get(programmePartTO.getConsolidatedSubjectSectionName());
					Set<String> streamIds = innerMap.keySet();
					List<String> streamList = new ArrayList<String>(streamIds);
					ListIterator<String> streamIdsIt = streamList.listIterator();
					while(streamIdsIt.hasNext()) {
						
						String key = streamIdsIt.next();
						ConsolidateMarksCardProgrammePartTO existingProgrammePartTO = innerMap.get(key);
						if(!existingProgrammePartTO.isShowStreamForSection()) {
							
							int totalCredit = existingProgrammePartTO.getCredit() + programmePartTO.getCredit();
							double totalCreditPoint = existingProgrammePartTO.getCreditPoint() + programmePartTO.getCreditPoint();
							double totalCCPA = totalCreditPoint/totalCredit;
							existingProgrammePartTO.setCredit(totalCredit+existingProgrammePartTO.getCreditsForDisplay()+programmePartTO.getCreditsForDisplay());
							existingProgrammePartTO.setCcpa(CommonUtil.Round(totalCCPA,2));
							innerMap.put(key, existingProgrammePartTO);
							displayMap.put(existingProgrammePartTO.getConsolidatedSubjectSectionName(), innerMap);
						}
						else if(existingProgrammePartTO.getCourseId() == 4 && existingProgrammePartTO.getConsolidatedSubjectSectionId() == 3) {

							isBcom = true;
							int totalCredit = existingProgrammePartTO.getCredit() + programmePartTO.getCredit();
							double totalCreditPoint = existingProgrammePartTO.getCreditPoint() + programmePartTO.getCreditPoint();
							double totalCCPA = totalCreditPoint/totalCredit;
							existingProgrammePartTO.setCredit(totalCredit);
							existingProgrammePartTO.setCcpa(CommonUtil.Round(totalCCPA,2));
							existingProgrammePartTO.setConsolidatedSubjectStreamName(ConsolidatedMarksCardTransactionImpl.getInstance().getElectiveStreamOfBcomStudentForCore(programmePartTO.getStudentId()));
							innerMap.put(key, existingProgrammePartTO);							
							displayMap.put("Core Course with Elective", innerMap);
						}
						else if((existingProgrammePartTO.getCourseId() == 3 || existingProgrammePartTO.getCourseId() == 20) &&
								(existingProgrammePartTO.getConsolidatedSubjectSectionId() == 3 || existingProgrammePartTO.getConsolidatedSubjectSectionId() == 4)) {
							
							int totalCredit = existingProgrammePartTO.getCredit() + programmePartTO.getCredit();
							double totalCreditPoint = existingProgrammePartTO.getCreditPoint() + programmePartTO.getCreditPoint();
							double totalCCPA = totalCreditPoint/totalCredit;
							existingProgrammePartTO.setCredit(totalCredit);
							existingProgrammePartTO.setCcpa(CommonUtil.Round(totalCCPA,2));
							existingProgrammePartTO.setShowStreamForSection(false);
							innerMap.put(key, existingProgrammePartTO);
							displayMap.put(existingProgrammePartTO.getConsolidatedSubjectSectionName(), innerMap);
						}
						else {
							
							innerMap.put(String.valueOf(programmePartTO.getConsolidatedSubjectStreamId()), programmePartTO);
							displayMap.put(programmePartTO.getConsolidatedSubjectSectionName(), innerMap);
						}
					}
					//}//Commented for Foundation
				}
				else {
					
					Map<String, ConsolidateMarksCardProgrammePartTO> innerMap = new LinkedHashMap<String, ConsolidateMarksCardProgrammePartTO>();
					if(((programmePartTO.getCourseId() == 3 || programmePartTO.getCourseId() == 20) &&
					    (programmePartTO.getConsolidatedSubjectSectionId() == 3 || programmePartTO.getConsolidatedSubjectSectionId() == 4))
					   ||
					   (programmePartTO.getCourseId() == 4 && programmePartTO.getConsolidatedSubjectSectionId() == 4)) {
						
						int totalCredit = programmePartTO.getCredit();
						double totalCreditPoint = programmePartTO.getCreditPoint();
						double totalCCPA = totalCreditPoint/totalCredit;
						programmePartTO.setCredit(totalCredit);
						programmePartTO.setCcpa(CommonUtil.Round(totalCCPA,2));
						programmePartTO.setShowStreamForSection(false);
						innerMap.put(programmePartTO.getConsolidatedSubjectStreamName(), programmePartTO);
						displayMap.put(programmePartTO.getConsolidatedSubjectSectionName(), innerMap);
					}
					else {
											
						innerMap.put(String.valueOf(programmePartTO.getConsolidatedSubjectStreamId()), programmePartTO);
						displayMap.put(programmePartTO.getConsolidatedSubjectSectionName(), innerMap);
					}					
				}
			
			}
		}
		
		if(isBcom){
			displayMap.remove("Core Course");
		}
		
		return displayMap;
	}
	
}