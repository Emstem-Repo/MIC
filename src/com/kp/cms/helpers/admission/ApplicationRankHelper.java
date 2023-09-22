package com.kp.cms.helpers.admission;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.AdmSubjectMarkForRank;
import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.CandidateEntranceDetails;
import com.kp.cms.bo.admin.CandidatePreference;
import com.kp.cms.bo.admin.CandidatePreferenceEntranceDetails;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.EdnQualification;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.SeatAllocation;
import com.kp.cms.bo.admin.StudentCommonRank;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import com.kp.cms.bo.admin.StudentCourseAllotmentPrev;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admin.StudentIndexMark;
import com.kp.cms.bo.admin.StudentRank;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.admission.ApplicationEditForm;
import com.kp.cms.to.admin.CandidatePreferenceEntranceDetailsTO;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admin.SeatAllocationTO;
import com.kp.cms.to.admin.StudentCourseAllotmentTo;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.PersonalDataTO;
import com.kp.cms.transactions.admission.IApplicationEditTransaction;
import com.kp.cms.transactionsimpl.admin.CourseTransactionImpl;
import com.kp.cms.transactionsimpl.admission.ApplicationEditTransactionimpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.sun.org.apache.bcel.internal.generic.ALOAD;

/**
 * @author raghu
 *
 */
public class ApplicationRankHelper {
	/**
	 * Singleton object of ApplicationRankHelper
	 */
	private static volatile ApplicationRankHelper applicationRankHelper = null;
	private static final Log log = LogFactory.getLog(ApplicationRankHelper.class);
	private static final String OTHER="Other";
	private static final String PHOTO="Photo";
	private static final String FROM_DATEFORMAT="dd/MM/yyyy";
	private static final String SQL_DATEFORMAT="dd-MMM-yyyy";
	private static final String SQL_DATEFORMAT1="yyyy-MM-dd";
	private ApplicationRankHelper() {
		
	}
	/**
	 * return singleton object of ApplicationRankHelper.
	 * @return
	 */
	public static ApplicationRankHelper getInstance() {
		if (applicationRankHelper == null) {
			applicationRankHelper = new ApplicationRankHelper();
		}
		return applicationRankHelper;
	}
		
	
	
	
				//For rank
	public List<StudentRank> calculateRank(ApplicationEditForm form)throws Exception{
		
		List<StudentRank> rankList= new ArrayList<StudentRank>();
		List<StudentIndexMark> pgMarksCardData = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData1 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData2 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData3 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData4 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData5 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData6 = new ArrayList<StudentIndexMark>();
		List<StudentIndexMark> pgMarksCardData7 = new ArrayList<StudentIndexMark>();
		Session session = null;
	
		try{
			
			session = HibernateUtil.getSession();
			
			// get all student index marks based on courses
			String hqlQuery = " from StudentIndexMark s " +
							  " where s.course.id="+form.getCourseId()+
							  " and s.admAppln.appliedYear="+form.getAcademicYear()+
							  " and s.remark='Eligible' " +
							  " order by s.indexMark desc";
			Query query = session.createQuery(hqlQuery);
			pgMarksCardData = query.list();
			
			// main list if empty or not
			if(pgMarksCardData!=null && !pgMarksCardData.isEmpty()) {
				Iterator<StudentIndexMark> itr=pgMarksCardData.iterator();
			
				Map<Integer,Integer> rankMap=new LinkedHashMap<Integer, Integer>();
				Integer rank=0;

				//main while data start
				while(itr.hasNext()) {
					StudentIndexMark studentmark = (StudentIndexMark) itr.next();
				  
					//check admid and index mark null 
					if(studentmark.getAdmAppln().getId()!=0) {
						         
					   // sample duplicate 1 rank map
					   if(!rankMap.containsKey(studentmark.getAdmAppln().getId())) {
						   String hqlQuery1 = " from StudentIndexMark s" +
						   					  " where s.course.id="+studentmark.getCourse().getId()+
						   					  " and s.indexMark="+studentmark.getIndexMark()+
						   					  " and s.admAppln.appliedYear="+form.getAcademicYear()+
						   					  " and s.remark='Eligible'" +
						   					  " order by s.electivesubmark desc";
						   Query query1 = session.createQuery(hqlQuery1);
						   pgMarksCardData1 = query1.list();
							
						   if(pgMarksCardData1!=null && pgMarksCardData1.size()!=0 && pgMarksCardData1.size()==1) {
							   if(!rankMap.containsKey(studentmark.getAdmAppln().getId())){	
								   rank++;
								   rankMap.put(studentmark.getAdmAppln().getId(),rank);
						             
								   StudentRank studentRank =new StudentRank();
								   AdmAppln appln=new AdmAppln();
								   appln.setId(studentmark.getAdmAppln().getId());
								   studentRank.setAdmAppln(appln);
								   Course course=new Course();
								   course.setId(studentmark.getCourse().getId());
								   studentRank.setCourse(course);
								   studentRank.setIndexMark(studentmark.getIndexMark());
								   studentRank.setRank(rank);
								   studentRank.setPrefNo(studentmark.getPrefNo());
								   studentRank.setTiePreference("Based on index Marks");
								   studentRank.setCreatedDate(new Date());
								   studentRank.setCreatedBy(form.getUserId());
									   
								   rankList.add(studentRank);
									   
								}//dup map close
								
							}//if
							else{
								Iterator<StudentIndexMark> itr1=pgMarksCardData1.iterator();
								while(itr1.hasNext()) {
									StudentIndexMark stumark = (StudentIndexMark) itr1.next();
									// get students  based on courses and index marks
									String hqlQuery2 = " from StudentIndexMark s" +
													   " where s.course.id="+stumark.getCourse().getId()+
													   " and s.indexMark="+stumark.getIndexMark()+
													   " and s.electivesubmark="+stumark.getElectivesubmark()+
													   " and s.admAppln.appliedYear="+form.getAcademicYear()+
													   " and s.remark='Eligible'" +
													   " order by s.language1Marks desc";
									Query query2 = session.createQuery(hqlQuery2);
									pgMarksCardData2 = query2.list();
									
									if(pgMarksCardData2!=null && pgMarksCardData2.size()!=0 && pgMarksCardData2.size()==1){
										
										if(!rankMap.containsKey(stumark.getAdmAppln().getId())){	
							          
										 rank++;
								          
								               rankMap.put(stumark.getAdmAppln().getId(),rank);
								             
											   StudentRank studentRank =new StudentRank();
											   AdmAppln appln=new AdmAppln();
											   appln.setId(stumark.getAdmAppln().getId());
											   studentRank.setAdmAppln(appln);
											   Course course=new Course();
											   course.setId(stumark.getCourse().getId());
											   studentRank.setCourse(course);
											   studentRank.setIndexMark(stumark.getIndexMark());
											   studentRank.setRank(rank);
											   studentRank.setPrefNo(stumark.getPrefNo());
											   studentRank.setTiePreference("Based on index Marks");
											   studentRank.setCreatedDate(new Date());
											   studentRank.setCreatedBy(form.getUserId());
											   
											   rankList.add(studentRank);
											   
										}//dup map close
										
									}//if
									
									else {
										Iterator<StudentIndexMark> itrr2=pgMarksCardData2.iterator();
										
										while(itrr2.hasNext()){
										
											StudentIndexMark stmark = (StudentIndexMark) itrr2.next();
											//if(stmark.getAdmAppln().getId()==991){
											 String hqlQuery7 = "from StudentIndexMark s" +
											 					" where s.course.id="+stmark.getCourse().getId()+
											 					" and s.indexMark="+stmark.getIndexMark()+
											 					" and s.electivesubmark="+stmark.getElectivesubmark()+
											 					" and s.language1Marks="+stmark.getLanguage1Marks()+
											 					" and s.admAppln.appliedYear="+form.getAcademicYear()+
											 					" order by s.admAppln.personalData.dateOfBirth";
											 Query query7 = session.createQuery(hqlQuery7);
											 pgMarksCardData3 = query7.list();
											 if(pgMarksCardData3!=null && pgMarksCardData3.size()!=0 && pgMarksCardData3.size()==1) {
															
														//raghu dupmap check
												 if(!rankMap.containsKey(stmark.getAdmAppln().getId())){	
													 rank++;
											         rankMap.put(stmark.getAdmAppln().getId(),rank);
											            
											         StudentRank studentRank =new StudentRank();
											         AdmAppln appln=new AdmAppln();
											         appln.setId(stmark.getAdmAppln().getId());
											         studentRank.setAdmAppln(appln);
											         Course course=new Course();
											         course.setId(stmark.getCourse().getId());
											         studentRank.setCourse(course);
														   
											         studentRank.setIndexMark(stmark.getIndexMark());
											         studentRank.setRank(rank);
											         studentRank.setPrefNo(stmark.getPrefNo());
											         studentRank.setTiePreference("Based on DOB");
											         studentRank.setCreatedDate(new Date());
											         studentRank.setCreatedBy(form.getUserId());
											         rankList.add(studentRank);
												}// dup map close
											}
											else {
												Iterator<StudentIndexMark> itrr3=pgMarksCardData3.iterator();
												while(itrr3.hasNext()){
													StudentIndexMark sMark = (StudentIndexMark) itrr3.next();
													String hqlQuery8 = "from StudentIndexMark s" +
								 					   " where s.course.id="+sMark.getCourse().getId()+
								 					   " and s.indexMark="+sMark.getIndexMark()+
								 					   " and s.electivesubmark="+sMark.getElectivesubmark()+
								 					   " and s.language1Marks="+sMark.getLanguage1Marks()+
								 					   " and s.admAppln.appliedYear="+form.getAcademicYear()+
								 					   " and s.admAppln.personalData.dateOfBirth=:dob" +
								 					   " order by s.admAppln.personalData.firstName";
													Query query8 = session.createQuery(hqlQuery8);
												
													// String dob=CommonUtil.ConvertStringToSQLDateValue(studentmark.getAdmAppln().getPersonalData().getDateOfBirth().toString()).toString();
													query8.setString("dob", sMark.getAdmAppln().getPersonalData().getDateOfBirth().toString());
													pgMarksCardData7 = query8.list();
													if(pgMarksCardData7!=null && pgMarksCardData7.size()!=0){
														Iterator<StudentIndexMark> itrr7=pgMarksCardData7.iterator();
														
														  while(itrr7.hasNext()){
															  StudentIndexMark obj10 = (StudentIndexMark) itrr7.next();
															  if(!rankMap.containsKey(obj10.getAdmAppln().getId())){		
																  rank++;
																  rankMap.put(obj10.getAdmAppln().getId(),rank);
												            
																  StudentRank studentRank =new StudentRank();
																  AdmAppln appln=new AdmAppln();
																  appln.setId(obj10.getAdmAppln().getId());
																  studentRank.setAdmAppln(appln);
																  Course course=new Course();
																  course.setId(obj10.getCourse().getId());
																  studentRank.setCourse(course);
															   
																  studentRank.setIndexMark(obj10.getIndexMark());
																  studentRank.setRank(rank);
																  studentRank.setPrefNo(obj10.getPrefNo());
																  studentRank.setTiePreference("Based on DOB Marks");
																  studentRank.setCreatedDate(new Date());
																  studentRank.setCreatedBy(form.getUserId());
																  rankList.add(studentRank);
															} //dup map close
														}
													}
												}
											}
										}
									}//fifth else
								}
							}//while
					   	}
					}//if close admid and rank
				}// main while close
			} //main list if close
		}//try close
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return rankList;
	}
		
		
	//For mark
	public List<StudentIndexMark> calculateMark( List<AdmAppln> applDetails,ApplicationEditForm form)throws Exception{

		
		List<StudentIndexMark> indexmarkList=new ArrayList<StudentIndexMark>();
		DecimalFormat twoDForm = new DecimalFormat("#.##");
	
		try{
		Iterator<AdmAppln> iter=applDetails.iterator();
		
			while(iter.hasNext()){
			AdmAppln admAppln=(AdmAppln)iter.next() ;
			
			System.out.println(admAppln.getId()+"########################################################");
			
		//if(admAppln.getId()==23795){
          

            boolean isvhsc=false;

			int penalty=0;
			int bonus=0;
			boolean feepaid=false;
            if(!admAppln.getIsDraftMode() && admAppln.getApplnNo()!=0 && admAppln.getMode()!=null && admAppln.getMode().equalsIgnoreCase("CHALLAN")){
                if(admAppln.getIsChallanRecieved()!=null && admAppln.getIsChallanRecieved()){
                	 feepaid=true;
                }else{
                	 feepaid=false;
                }
                }else if(!admAppln.getIsDraftMode() && admAppln.getApplnNo ()!=0 && admAppln.getMode()!=null && admAppln.getMode().equalsIgnoreCase("NEFT")){
                	  if(admAppln.getIsChallanRecieved()!=null && admAppln.getIsChallanRecieved()){
                    	  feepaid=true;
                      }else{
                    	  feepaid=false;
                      }
                }else if(!admAppln.getIsDraftMode() && admAppln.getApplnNo()!=0 && admAppln.getMode()!=null && admAppln.getMode().equalsIgnoreCase("Online")){
                	 feepaid=true;
                      
                }
           
				Iterator<EdnQualification> itr=admAppln.getPersonalData().getEdnQualifications().iterator();
				
				Iterator<CandidatePreference> ittr=admAppln.getCandidatePreferences().iterator();
			
				Iterator<AdmapplnAdditionalInfo> iterator=admAppln.getAdmapplnAdditionalInfo().iterator();
				
				while(iterator.hasNext()){
				AdmapplnAdditionalInfo admapplnAdditionalInfo=iterator.next();
				
				if(feepaid==true){
					// educationinfo while
				while(itr.hasNext()){
				EdnQualification ednQualificationBO= itr.next() ;
				
				if(ednQualificationBO.getUniversity()!=null && ednQualificationBO.getUniversity().getId()==10){

                    isvhsc=true;

              }


					
	          	if(ednQualificationBO.getDocChecklist()!=null&& ednQualificationBO.getDocChecklist().getIsActive()==true && ednQualificationBO.getDocChecklist().getDocType().getName().equalsIgnoreCase("Class 12") && ednQualificationBO.getDocChecklist().getIsSemesterWise()==false){
					
	          		Set<AdmSubjectMarkForRank> detailMarks=ednQualificationBO.getAdmSubjectMarkForRank();
	          	//penalty based on attempt
					if( ednQualificationBO.getNoOfAttempts()==1){
						penalty=0;//
					}
					else if( ednQualificationBO.getNoOfAttempts()>=2){
						penalty=(ednQualificationBO.getNoOfAttempts()-1)*10;//
					}
					
					
					//Bonus
					
					
					
					//15 mark for ncc & 15 mark for A grade,20 mark for B grade and 25 mark for C grade
					if(admAppln.getPersonalData().getIsNcccertificate() || admAppln.getPersonalData().getIsNsscertificate() || (admAppln.getPersonalData().getIsSpc()!=null && admAppln.getPersonalData().getIsSpc()) || (admAppln.getPersonalData().getIsScouts()!=null && admAppln.getPersonalData().getIsScouts())){
						
							bonus=bonus+15;
							/*if(admAppln.getPersonalData().getNccgrade()!=null && admAppln.getPersonalData().getNccgrade().equalsIgnoreCase("B")){
								bonus=bonus+5;	
							}
							if(admAppln.getPersonalData().getNccgrade()!=null && admAppln.getPersonalData().getNccgrade().equalsIgnoreCase("C")){
								bonus=bonus+10;	
							}*/
					}
					
					
						
					/*else if(admAppln.getPersonalData().getIsNsscertificate()){
						bonus=bonus+15;
					}*/
					
					if(admAppln.getPersonalData().getIsExcervice()){
						bonus=bonus+15;
					}
	          		// detailmarks if check
						if(detailMarks!=null && !detailMarks.isEmpty())
						{
							AdmSubjectMarkForRank detailMarkBO=null;
							while(ittr.hasNext()){
							CandidatePreference candidatePreference= ittr.next(); 
							double totalmarkforpart3=0;
							String remark="Eligible";
							

                		        
 							
						
                           
						//BA English
							//B.A. English & Communicative English
                     	 
                          // else if(candidatePreference.getCourse().getName().equalsIgnoreCase("BA English")){
                        	     if(candidatePreference.getCourse().getId()==2 || candidatePreference.getCourse().getId()==22){
                        	    	 
                        	    	 
                        		   if(isvhsc){

                                	   remark="Eligible";
                                   int i=0;
                                   double maxmark=0;
                                   double language1Mark=0;                          
                                   double language2Mark=0;
                                   double groupMarks=0;
                                   double electiveSubjectMark=0;
                                   double literatureCommunityMark=0;
                                   double marks=0;
                                   double langbonus=0;
                                   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                		while (markItr.hasNext()) {
        									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        									i++;
        									//adding 3 optional subject mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									maxmark=maxmark+200;
        									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									totalmarkforpart3=totalmarkforpart3+mark;
        									}
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									 marks=(Double.parseDouble(detailMarkBO.getConversionmark())/Double.parseDouble(detailMarkBO.getMaxmark()))*400;
        									 langbonus=(Double.parseDouble(detailMarkBO.getConversionmark())/Double.parseDouble(detailMarkBO.getMaxmark()))*200;

        									}
        									
        									
        									//groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        																				
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									}																				
        									//lang2 mark
        									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
        										
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        									
        									if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English Literature") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Communicative English")) {
        										
        										if (literatureCommunityMark!=0 && Double.parseDouble(detailMarkBO.getConversionmark()) < literatureCommunityMark) {
        											literatureCommunityMark=literatureCommunityMark;
												}else {
													literatureCommunityMark=Double.parseDouble(detailMarkBO.getConversionmark());
												}
											}
        									
        									
                                		}
                                		if (marks!=0) {
                                			literatureCommunityMark=(literatureCommunityMark/100)*10;
                                			System.out.println(detailMarkBO.getAdmSubjectForRank().getName());
											
										}else{
											literatureCommunityMark=0;
										}
                                		totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                	
                                	
                                			//penalty &bonus
         	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+langbonus+literatureCommunityMark;
                                   
        								
        									// adding indexmark object
        									StudentIndexMark si=new StudentIndexMark();
        									
        									AdmAppln appln=new AdmAppln();
        									appln.setId(admAppln.getId());
        									si.setAdmAppln(appln);
        									
        									Course course=new Course();
        									course.setId(candidatePreference.getCourse().getId());
        									si.setCourse(course);
        									
        									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        									si.setRemark(remark);
        									si.setCreatedDate(new Date());
        									si.setLastModifiedDate(new Date());
        									si.setCreatedBy(form.getUserId());
        									si.setModifiedBy(form.getUserId());

        									si.setLanguage1Marks(language1Mark);
        									si.setLanguage2Marks(language2Mark);
        									si.setGroupMarks(groupMarks);
        									si.setElectivesubmark(electiveSubjectMark);
        									si.setActive(true);
        									si.setPrefNo(candidatePreference.getPrefNo());
        									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        									
        									indexmarkList.add(si);
                                		     
                        		   }
                        		   else{
                        	   remark="Eligible";
                           int i=0;
                           double maxmark=0;
                           double language1Mark=0;                          
                           double language2Mark=0;
                           double groupMarks=0;
                           double electiveSubjectMark=0;
                           double marks=0;
                           double literatureCommunityMark=0;
                           
                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        		while (markItr.hasNext()) {
									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									i++;
									maxmark=maxmark+200;
									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
									//ra
									//totalmarkforpart3=totalmarkforpart3+totalmarkforpart3;
									totalmarkforpart3=totalmarkforpart3+mark;
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									 marks=Double.parseDouble(detailMarkBO.getConversionmark());
									
									}
									
									
									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									}																				
									//lang2 mark
									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
										
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
									if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English Literature") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Communicative English")) {
										
										if (literatureCommunityMark!=0 && Double.parseDouble(detailMarkBO.getConversionmark()) < literatureCommunityMark) {
											literatureCommunityMark=literatureCommunityMark;
										}else {
											literatureCommunityMark=Double.parseDouble(detailMarkBO.getConversionmark());
										}
									}
									
                        		}
                        		if (marks!=0) {
                        			literatureCommunityMark=(literatureCommunityMark/100)*10;
                        			System.out.println(detailMarkBO.getAdmSubjectForRank().getName());
                        			
									
								}else{
									literatureCommunityMark=0;
								}
                        		totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        	
                        	
                        			//penalty &bonus
 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+literatureCommunityMark;
                           
								
									// adding indexmark object
									StudentIndexMark si=new StudentIndexMark();
									
									AdmAppln appln=new AdmAppln();
									appln.setId(admAppln.getId());
									si.setAdmAppln(appln);
									
									Course course=new Course();
									course.setId(candidatePreference.getCourse().getId());
									si.setCourse(course);
									
									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
									si.setRemark(remark);
									si.setCreatedDate(new Date());
									si.setLastModifiedDate(new Date());
									si.setCreatedBy(form.getUserId());
									si.setModifiedBy(form.getUserId());

									si.setLanguage1Marks(language1Mark);
									si.setLanguage2Marks(language2Mark);
									si.setGroupMarks(groupMarks);
									si.setElectivesubmark(electiveSubjectMark);
									si.setActive(true);
									si.setPrefNo(candidatePreference.getPrefNo());
									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
									
									indexmarkList.add(si);
                        		   }
							}
                           ////BA English over
                        	     //B.A. English & Communicative English over
                      
							
							
							
							//B.Sc Physics
							
                           //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.Sc Physics")){
                        	   else if(candidatePreference.getCourse().getId()==5){
                        		   if(isvhsc){
                                      double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           double electiveSubjectMark=0;
        	                           boolean iselig=false;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                	   while (markItr.hasNext()) {
        										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                								double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										totalmarkforpart3=totalmarkforpart3+mark;	
        										maxmark=maxmark+200;
        									}
        									
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics"))
        									{
        										iselig=true;
        										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
                                	        //groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        																				
        									//lang2 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        									
        									
                                	   }
                                	   
                                  if(iselig){
                                	  remark="Eligible";
                                      totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                      
                                    //penalty &bonus
                                	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                       
                                  }
                                  else{
                                	  remark="rejected";
                                  
                                  }
                                  
                               // adding indexmark object
        							StudentIndexMark si=new StudentIndexMark();
        							
        							AdmAppln appln=new AdmAppln();
        							appln.setId(admAppln.getId());
        							si.setAdmAppln(appln);
        							
        							Course course=new Course();
        							course.setId(candidatePreference.getCourse().getId());
        							si.setCourse(course);
        							
        							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        							si.setRemark(remark);
        							si.setCreatedDate(new Date());
        							si.setLastModifiedDate(new Date());
        							si.setCreatedBy(form.getUserId());
        							si.setModifiedBy(form.getUserId());

        							si.setLanguage1Marks(language1Mark);
        							si.setLanguage2Marks(language2Mark);
        							si.setGroupMarks(groupMarks);
        							si.setElectivesubmark(electiveSubjectMark);
        							si.setActive(true);
        							si.setPrefNo(candidatePreference.getPrefNo());
        							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        							
        							indexmarkList.add(si);
        							
                        		   }
                        		   else{
	                           double maxmark=0;
	                           double language1Mark=0;                          
	                           double language2Mark=0;
	                           double groupMarks=0;
	                           double electiveSubjectMark=0;
	                           boolean iselig=false;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        	   while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									
										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										totalmarkforpart3=totalmarkforpart3+mark;	
										maxmark=maxmark+200;
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics"))
									{
										iselig=true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
                        	        //groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
																				
									//lang2 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
									
									
                        	   }
                        	   
                          if(iselig){
                        	  remark="Eligible";
                              totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                              
                            //penalty &bonus
                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
               
                          }
                          else{
                        	  remark="rejected";
                          
                          }
                          
                       // adding indexmark object
							StudentIndexMark si=new StudentIndexMark();
							
							AdmAppln appln=new AdmAppln();
							appln.setId(admAppln.getId());
							si.setAdmAppln(appln);
							
							Course course=new Course();
							course.setId(candidatePreference.getCourse().getId());
							si.setCourse(course);
							
							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
							si.setRemark(remark);
							si.setCreatedDate(new Date());
							si.setLastModifiedDate(new Date());
							si.setCreatedBy(form.getUserId());
							si.setModifiedBy(form.getUserId());

							si.setLanguage1Marks(language1Mark);
							si.setLanguage2Marks(language2Mark);
							si.setGroupMarks(groupMarks);
							si.setElectivesubmark(electiveSubjectMark);
							si.setActive(true);
							si.setPrefNo(candidatePreference.getPrefNo());
							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
							
							indexmarkList.add(si);
                        		   }
							}
							//B.Sc Physics over
							
							//B.Sc Chemistry
                          // else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.Sc Chemistry")){
                        	   else if(candidatePreference.getCourse().getId()==6){
                        		   if(isvhsc){
        	                           double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           double electiveSubjectMark=0;
        	                           boolean iselig=false;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                	   while (markItr.hasNext()) {
        									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
            									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										totalmarkforpart3=totalmarkforpart3+mark;	
        										maxmark=maxmark+200;
        									}
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Chemistry"))
        									{
        										iselig=true;
        										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
                                	   
        									//groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        																				
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        																				
        									//lang2 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }

        									
                                	   }
                                	   
                                  if(iselig){
                                	  remark="Eligible";
                                      totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                      
                                    //penalty &bonus
                                	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                       
                                
                                  }
                                  else{
                                	  remark="rejected because of no chemistry subject";
                                  }
                                  
                                  
                               // adding indexmark object
        							StudentIndexMark si=new StudentIndexMark();
        							
        							AdmAppln appln=new AdmAppln();
        							appln.setId(admAppln.getId());
        							si.setAdmAppln(appln);
        							
        							Course course=new Course();
        							course.setId(candidatePreference.getCourse().getId());
        							si.setCourse(course);
        							
        							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        							si.setRemark(remark);
        							si.setCreatedDate(new Date());
        							si.setLastModifiedDate(new Date());
        							si.setCreatedBy(form.getUserId());
        							si.setModifiedBy(form.getUserId());
        							si.setLanguage1Marks(language1Mark);
        							si.setLanguage2Marks(language2Mark);
        							si.setGroupMarks(groupMarks);
        							si.setElectivesubmark(electiveSubjectMark);
        							si.setActive(true);
        							si.setPrefNo(candidatePreference.getPrefNo());
        							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        							
        							indexmarkList.add(si);
                                  
                                	     
                        		   }
                        		   else{
	                           double maxmark=0;
	                           double language1Mark=0;                          
	                           double language2Mark=0;
	                           double groupMarks=0;
	                           double electiveSubjectMark=0;
	                           boolean iselig=false;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        	   while (markItr.hasNext()) {
									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									
									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										totalmarkforpart3=totalmarkforpart3+mark;	
										maxmark=maxmark+200;
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Chemistry"))
									{
										iselig=true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
                        	   
									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
																				
									//lang2 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }

									
                        	   }
                        	   
                          if(iselig){
                        	  remark="Eligible";
                              totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                              
                            //penalty &bonus
                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
               
                        
                          }
                          else{
                        	  remark="rejected because of no chemistry subject";
                          }
                          
                          
                       // adding indexmark object
							StudentIndexMark si=new StudentIndexMark();
							
							AdmAppln appln=new AdmAppln();
							appln.setId(admAppln.getId());
							si.setAdmAppln(appln);
							
							Course course=new Course();
							course.setId(candidatePreference.getCourse().getId());
							si.setCourse(course);
							
							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
							si.setRemark(remark);
							si.setCreatedDate(new Date());
							si.setLastModifiedDate(new Date());
							si.setCreatedBy(form.getUserId());
							si.setModifiedBy(form.getUserId());
							si.setLanguage1Marks(language1Mark);
							si.setLanguage2Marks(language2Mark);
							si.setGroupMarks(groupMarks);
							si.setElectivesubmark(electiveSubjectMark);
							si.setActive(true);
							si.setPrefNo(candidatePreference.getPrefNo());
							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
							
							indexmarkList.add(si);
                        		   }
                        	   }
							
							//B.Sc Chemistry over
							
                        	   //Botany & Biotechnology 
                        	   else if(candidatePreference.getCourse().getId()==20){

                            	   if(isvhsc){
                                       double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           boolean iselig=false;
        	                           double electiveSubjectMark=0;
        	                           double electiveSubjectMark1=0;
        	                           double electiveSubjectMark2=0;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                	   while (markItr.hasNext()) {
        										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                								double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										totalmarkforpart3=totalmarkforpart3+mark;	
        										maxmark=maxmark+200;
        										}
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology"))
        									{
        										iselig=true;
        										electiveSubjectMark1=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Bio-Technology"))
        									{
        										electiveSubjectMark2=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
                                	   
        									

        									//groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        																				
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        																				
        									//lang2 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }



        									
                                	   }
                                	  if (electiveSubjectMark1 > electiveSubjectMark2) {
                                		  electiveSubjectMark = electiveSubjectMark1;
									} else{
										electiveSubjectMark = electiveSubjectMark2;
									}
                                	   
                                  if(iselig){
                                	  remark="Eligible";
                                      totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                      
                                    //penalty &bonus
                                	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                       
                                
                                  }
                                  else{
                                	  remark="rejected because biology is not there";
                                  }
        							
                               // adding indexmark object
        							StudentIndexMark si=new StudentIndexMark();
        							
        							AdmAppln appln=new AdmAppln();
        							appln.setId(admAppln.getId());
        							si.setAdmAppln(appln);;
        							
        							Course course=new Course();
        							course.setId(candidatePreference.getCourse().getId());
        							si.setCourse(course);
        							
        							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        							si.setRemark(remark);
        							si.setCreatedDate(new Date());
        							si.setLastModifiedDate(new Date());
        							si.setCreatedBy(form.getUserId());
        							si.setModifiedBy(form.getUserId());
        							si.setLanguage1Marks(language1Mark);
        							si.setLanguage2Marks(language2Mark);
        							si.setGroupMarks(groupMarks);
        							si.setElectivesubmark(electiveSubjectMark);
        							si.setActive(true);
        							si.setPrefNo(candidatePreference.getPrefNo());
        							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        							
        							indexmarkList.add(si);
        							
                            	   }else
                            	   {
    	                           double maxmark=0;
    	                           double maxmarkOrig=0;
    	                           double obtainMark = 0;
    	                           double language1Mark=0;                          
    	                           double language2Mark=0;
    	                           double groupMarks=0;
    	                           boolean iselig=false;
    	                           double electiveSubjectMark=0;
    	                           boolean hasStudiedBotany = false;
    	                           boolean hasStudiedZoology = false;
    	                           double botanyObtMarks = 0.0;
    	                           double botanyMaxMarks = 0.0;
    	                           double zoologyObtMarks = 0.0;
    	                           double zoologyMaxMarks = 0.0;
    	                           double electiveSubjectMark1=0;
    	                           double electiveSubjectMark2=0;
    	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                            	   while (markItr.hasNext()) {
    										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
    										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
    										totalmarkforpart3=totalmarkforpart3+mark;	
    										maxmark=maxmark+200;
    										obtainMark += Double.parseDouble(detailMarkBO.getObtainedmark());
    										maxmarkOrig += Double.parseDouble(detailMarkBO.getMaxmark());
    										System.out.println(detailMarkBO.getAdmSubjectForRank().getName());	
    									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology"))
    									{
    										iselig=true;
    										electiveSubjectMark1=Double.parseDouble(detailMarkBO.getConversionmark());
    									}
    									else if(detailMarkBO.getAdmSubjectForRank().getId() == 196) {	//	Bio-Technology
    										iselig=true;
    										electiveSubjectMark2=Double.parseDouble(detailMarkBO.getConversionmark());
    									}
    									else if(detailMarkBO.getAdmSubjectForRank().getId() == 452) {	//	Botany
    										hasStudiedBotany = true;
    										botanyObtMarks = Double.parseDouble(detailMarkBO.getObtainedmark());
    										botanyMaxMarks = Double.parseDouble(detailMarkBO.getMaxmark());
    									}
    									else if(detailMarkBO.getAdmSubjectForRank().getId() == 453) {	//	Zoology
    										hasStudiedZoology = true;
    										zoologyObtMarks = Double.parseDouble(detailMarkBO.getObtainedmark());
    										zoologyMaxMarks = Double.parseDouble(detailMarkBO.getMaxmark());
    									}
    									
    									//groups mark
    									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
    									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
    									}
    																				
    									//lang1 mark
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    									   }
    																				
    									//lang2 mark
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    									   }



    									
                            	   }
									
                            	   if (electiveSubjectMark1 > electiveSubjectMark2) {
                             		  electiveSubjectMark = electiveSubjectMark1;
									} else{
										electiveSubjectMark = electiveSubjectMark2;
									}
                            	   /**
                            	    * This part is used for students from outside Kerala whose total marks comes out of 1000
                            	    * The only way to identify them is that they will have studied Botany & Zology [for now] 
                            	    */
                            	   if(hasStudiedBotany && hasStudiedZoology) {
										iselig=true;
										electiveSubjectMark = ((botanyObtMarks + zoologyObtMarks) / (botanyMaxMarks + zoologyMaxMarks)) * 200;
                            	   }
                            	   
                              if(iselig){
                            	  remark="Eligible";
                            	  /**
                           	       * This part is used for students from outside Kerala whose total marks comes out of 1000
                           	       * The only way to identify them is that they will have studied Botany & Zology [for now] 
                           	       */
                            	  if(hasStudiedBotany && hasStudiedZoology) {
                            		  totalmarkforpart3=(obtainMark/maxmarkOrig)*1200;
                            	  }
                            	  else {
                            		  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                            	  }
                                  
                                //penalty &bonus
                            	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
                   
                            
                              }
                              else{
                            	  remark="rejected because biology is not there";
                              }
    							
                           // adding indexmark object
    							StudentIndexMark si=new StudentIndexMark();
    							
    							AdmAppln appln=new AdmAppln();
    							appln.setId(admAppln.getId());
    							si.setAdmAppln(appln);;
    							
    							Course course=new Course();
    							course.setId(candidatePreference.getCourse().getId());
    							si.setCourse(course);
    							
    							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
    							si.setRemark(remark);
    							si.setCreatedDate(new Date());
    							si.setLastModifiedDate(new Date());
    							si.setCreatedBy(form.getUserId());
    							si.setModifiedBy(form.getUserId());
    							si.setLanguage1Marks(language1Mark);
    							si.setLanguage2Marks(language2Mark);
    							si.setGroupMarks(groupMarks);
    							si.setElectivesubmark(electiveSubjectMark);
    							si.setActive(true);
    							si.setPrefNo(candidatePreference.getPrefNo());
    							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
    							
    							indexmarkList.add(si);
                            	   }
    							
                        	   }
							//Botany & Biotechnology over
							//B.Sc Botany 
                          // else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.Sc Botany")){
                        	   else if(candidatePreference.getCourse().getId()==7){
                        	   if(isvhsc){
                                   double maxmark=0;
    	                           double language1Mark=0;                          
    	                           double language2Mark=0;
    	                           double groupMarks=0;
    	                           boolean iselig=false;
    	                           double electiveSubjectMark=0;
    	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                            	   while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
	        								double mark=Double.parseDouble(detailMarkBO.getConversionmark());
											totalmarkforpart3=totalmarkforpart3+mark;	
											maxmark=maxmark+200;
										}
    									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology"))
    									{
    										iselig=true;
    										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
    									}
                            	   
    									

    									//groups mark
    									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
    									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
    									}
    																				
    									//lang1 mark
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    									   }
    																				
    									//lang2 mark
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    									   }



    									
                            	   }
                            	   
                              if(iselig){
                            	  remark="Eligible";
                                  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                  
                                //penalty &bonus
                            	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                   
                            
                              }
                              else{
                            	  remark="rejected because biology is not there";
                              }
    							
                           // adding indexmark object
    							StudentIndexMark si=new StudentIndexMark();
    							
    							AdmAppln appln=new AdmAppln();
    							appln.setId(admAppln.getId());
    							si.setAdmAppln(appln);;
    							
    							Course course=new Course();
    							course.setId(candidatePreference.getCourse().getId());
    							si.setCourse(course);
    							
    							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
    							si.setRemark(remark);
    							si.setCreatedDate(new Date());
    							si.setLastModifiedDate(new Date());
    							si.setCreatedBy(form.getUserId());
    							si.setModifiedBy(form.getUserId());
    							si.setLanguage1Marks(language1Mark);
    							si.setLanguage2Marks(language2Mark);
    							si.setGroupMarks(groupMarks);
    							si.setElectivesubmark(electiveSubjectMark);
    							si.setActive(true);
    							si.setPrefNo(candidatePreference.getPrefNo());
    							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
    							
    							indexmarkList.add(si);
    							
                        	   }else
                        	   {
	                           double maxmark=0;
	                           double maxmarkOrig=0;
	                           double obtainMark = 0;
	                           double language1Mark=0;                          
	                           double language2Mark=0;
	                           double groupMarks=0;
	                           boolean iselig=false;
	                           double electiveSubjectMark=0;
	                           boolean hasStudiedBotany = false;
	                           boolean hasStudiedZoology = false;
	                           double botanyObtMarks = 0.0;
	                           double botanyMaxMarks = 0.0;
	                           double zoologyObtMarks = 0.0;
	                           double zoologyMaxMarks = 0.0;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        	   while (markItr.hasNext()) {
									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
									totalmarkforpart3=totalmarkforpart3+mark;	
									maxmark=maxmark+200;
									obtainMark += Double.parseDouble(detailMarkBO.getObtainedmark());
									maxmarkOrig += Double.parseDouble(detailMarkBO.getMaxmark());
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology") && !hasStudiedBotany)
									{
										iselig=true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
									else if(detailMarkBO.getAdmSubjectForRank().getId() == 452) {	//	Botany
										iselig=true;
										hasStudiedBotany = true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
										botanyObtMarks = Double.parseDouble(detailMarkBO.getObtainedmark());
										botanyMaxMarks = Double.parseDouble(detailMarkBO.getMaxmark());
									}
									else if(detailMarkBO.getAdmSubjectForRank().getId() == 453) {	//	Zoology
										hasStudiedZoology = true;
										zoologyObtMarks = Double.parseDouble(detailMarkBO.getObtainedmark());
										zoologyMaxMarks = Double.parseDouble(detailMarkBO.getMaxmark());
									}
									

									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
																				
									//lang2 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }



									
                        	   }
                        	   
                          if(iselig){
                        	  remark="Eligible";
                        	  /**
                       	       * This part is used for students from outside Kerala whose total marks comes out of 1000
                       	       * The only way to identify them is that they will have studied Botany & Zology [for now] 
                       	       */
                        	  if(hasStudiedBotany && hasStudiedZoology) {
                        		  totalmarkforpart3=(obtainMark/maxmarkOrig)*1200;
                        	  }
                        	  else {
                        		  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        	  }
                              
                            //penalty &bonus
                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
               
                        
                          }
                          else{
                        	  remark="rejected because biology is not there";
                          }
							
                       // adding indexmark object
							StudentIndexMark si=new StudentIndexMark();
							
							AdmAppln appln=new AdmAppln();
							appln.setId(admAppln.getId());
							si.setAdmAppln(appln);;
							
							Course course=new Course();
							course.setId(candidatePreference.getCourse().getId());
							si.setCourse(course);
							
							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
							si.setRemark(remark);
							si.setCreatedDate(new Date());
							si.setLastModifiedDate(new Date());
							si.setCreatedBy(form.getUserId());
							si.setModifiedBy(form.getUserId());
							si.setLanguage1Marks(language1Mark);
							si.setLanguage2Marks(language2Mark);
							si.setGroupMarks(groupMarks);
							si.setElectivesubmark(electiveSubjectMark);
							si.setActive(true);
							si.setPrefNo(candidatePreference.getPrefNo());
							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
							
							indexmarkList.add(si);
                        	   }
							}
							
							//B.Sc Botany over
							
							
							//B.Sc Mathematics 
                           //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.Sc Mathematics")){
                        	   else if(candidatePreference.getCourse().getId()==8){
                        		   if(isvhsc){

        	                           double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           double electiveSubjectMark=0;
        	                           boolean iselig=false;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                	   while (markItr.hasNext()) {
        										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                								double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										//total mark in optional subjects
        										totalmarkforpart3=totalmarkforpart3+mark;	
        										maxmark=maxmark+200;
                                	   }
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics"))
        									{
        										iselig=true;
        										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
                                	   
        									
        									

        									//groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        																				
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        																				
        									//lang2 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }

                                	   }
                                	   
                                  if(iselig){
                                	  remark="Eligible";
                                      totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                      
                                    //penalty &bonus
                                	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                       
                                
                                  }
                                  else{
                                	  remark="rejected because there is no mathematics";
                                  }
        							
                               // adding indexmark object
        							StudentIndexMark si=new StudentIndexMark();
        							
        							AdmAppln appln=new AdmAppln();
        							appln.setId(admAppln.getId());
        							si.setAdmAppln(appln);
        							
        							Course course=new Course();
        							course.setId(candidatePreference.getCourse().getId());
        							si.setCourse(course);
        							
        							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        							si.setRemark(remark);
        							si.setCreatedDate(new Date());
        							si.setLastModifiedDate(new Date());
        							si.setCreatedBy(form.getUserId());
        							si.setModifiedBy(form.getUserId());
        							si.setLanguage1Marks(language1Mark);
        							si.setLanguage2Marks(language2Mark);
        							si.setGroupMarks(groupMarks);
        							si.setElectivesubmark(electiveSubjectMark);
        							si.setActive(true);
        							si.setPrefNo(candidatePreference.getPrefNo());
        							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        							
        							indexmarkList.add(si);
                                		   
                        		   }

                        		   else{
	                           double maxmark=0;
	                           double language1Mark=0;                          
	                           double language2Mark=0;
	                           double groupMarks=0;
	                           double electiveSubjectMark=0;
	                           boolean iselig=false;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        	   while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									
										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										totalmarkforpart3=totalmarkforpart3+mark;	
										maxmark=maxmark+200;
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics"))
									{
										iselig=true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
                        	   
									
									

									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
																				
									//lang2 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }

                        	   }
                        	   
                          if(iselig){
                        	  remark="Eligible";
                              totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                              
                            //penalty &bonus
                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
               
                        
                          }
                          else{
                        	  remark="rejected because there is no mathematics";
                          }
							
                       // adding indexmark object
							StudentIndexMark si=new StudentIndexMark();
							
							AdmAppln appln=new AdmAppln();
							appln.setId(admAppln.getId());
							si.setAdmAppln(appln);
							
							Course course=new Course();
							course.setId(candidatePreference.getCourse().getId());
							si.setCourse(course);
							
							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
							si.setRemark(remark);
							si.setCreatedDate(new Date());
							si.setLastModifiedDate(new Date());
							si.setCreatedBy(form.getUserId());
							si.setModifiedBy(form.getUserId());
							si.setLanguage1Marks(language1Mark);
							si.setLanguage2Marks(language2Mark);
							si.setGroupMarks(groupMarks);
							si.setElectivesubmark(electiveSubjectMark);
							si.setActive(true);
							si.setPrefNo(candidatePreference.getPrefNo());
							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
							
							indexmarkList.add(si);
                        		   }
							}
							//B.Sc Mathematics over 
							
							
							//B.Sc Zoology
							
                           //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.Sc Zoology")){
                        	   else if(candidatePreference.getCourse().getId()==9){

                        		   if(isvhsc){
                                       double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           boolean iselig=false;
        	                           double electiveSubjectMark=0;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                                	   while (markItr.hasNext()) {
        										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										totalmarkforpart3=totalmarkforpart3+mark;	
        										maxmark=maxmark+200;
        										}
        									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology"))
        									{
        										iselig=true;
        										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        									
        									

        									//groups mark
        									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
        									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        									}
        																				
        									//lang1 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }
        																				
        									//lang2 mark
        									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        									   }



        									
                                	   }
                                	   
                                  if(iselig){
                                	  remark="Eligible";
                                      totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                                      
                                    //penalty &bonus
                                	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark+(language1Mark*2);
                       
                                
                                  }
                                  else{
                                	  remark="rejected because biology is not there";
                                  }
        							
                               // adding indexmark object
        							StudentIndexMark si=new StudentIndexMark();
        							
        							AdmAppln appln=new AdmAppln();
        							appln.setId(admAppln.getId());
        							si.setAdmAppln(appln);;
        							
        							Course course=new Course();
        							course.setId(candidatePreference.getCourse().getId());
        							si.setCourse(course);
        							
        							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        							si.setRemark(remark);
        							si.setCreatedDate(new Date());
        							si.setLastModifiedDate(new Date());
        							si.setCreatedBy(form.getUserId());
        							si.setModifiedBy(form.getUserId());
        							si.setLanguage1Marks(language1Mark);
        							si.setLanguage2Marks(language2Mark);
        							si.setGroupMarks(groupMarks);
        							si.setElectivesubmark(electiveSubjectMark);
        							si.setActive(true);
        							si.setPrefNo(candidatePreference.getPrefNo());
        							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        							
        							indexmarkList.add(si);
        							
                            	   }else{
                        	   double language1Mark=0;                          
                        	   double language2Mark=0;
                        	   double groupMarks=0;
	                           double maxmark=0;
	                           boolean iselig=false;
	                           double electiveSubjectMark=0;
	                           boolean hasStudiedZoology = false;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        	   while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									
										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										totalmarkforpart3=totalmarkforpart3+mark;	
										maxmark=maxmark+200;
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Biology") && !hasStudiedZoology)
									{
										iselig=true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
									else if(detailMarkBO.getAdmSubjectForRank().getId() == 453) {	//	Zoology
										iselig=true;
										hasStudiedZoology = true;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
									

									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
																				
									//lang2 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
                        	   }
                        	   
                          if(iselig){
                        	  remark="Eligible";
                              totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                              
                            //penalty &bonus
                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+electiveSubjectMark;
               
                          }
                          else{
                        	  remark="rejected because of no biology";
                          }
							
                       // adding indexmark object
							StudentIndexMark si=new StudentIndexMark();
							
							AdmAppln appln=new AdmAppln();
							appln.setId(admAppln.getId());
							si.setAdmAppln(appln);
							
							Course course=new Course();
							course.setId(candidatePreference.getCourse().getId());
							si.setCourse(course);
							
							si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
							si.setRemark(remark);
							si.setCreatedDate(new Date());
							si.setLastModifiedDate(new Date());
							si.setCreatedBy(form.getUserId());
							si.setModifiedBy(form.getUserId());
							si.setLanguage1Marks(language1Mark);
							si.setLanguage2Marks(language2Mark);
							si.setGroupMarks(groupMarks);
							si.setElectivesubmark(electiveSubjectMark);
							si.setActive(true);
							si.setPrefNo(candidatePreference.getPrefNo());
							si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
							
							indexmarkList.add(si);
                        	   }
							}
							//B.Sc Zoology over
						
							//B.com 
							
                           //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.com")){
                        	   else if(candidatePreference.getCourse().getId()==4){
                        		   if(isvhsc){
                        			   int i=0;
                        			   remark="rejected";
                        			   int elig=0;
                        			   boolean isacc=false;
                        			   double maxmark=0;
                        			   double electiveSubjectMark=0;
                        			   double language1Mark=0;                          
                        			   double language2Mark=0;
                        			   double groupMarks=0;
                        			   double percentage=0;
                        			   double businessmark=0;
                        			   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        			   while (markItr.hasNext()) {
                        				   detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
                        				   i++;

                        				   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                        					   double mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   maxmark=maxmark+200;
                        					   totalmarkforpart3=totalmarkforpart3+mark;
                        					   percentage= (totalmarkforpart3/maxmark)*100;
                        				   }
                        				   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
                        					  detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") || 
                        					  detailMarkBO.getAdmSubjectForRank().getId() == 487){
                        					   //isacc=true;
                        					   electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());

                        				   }
                        				   /*if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commerce") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Correspondence") ||
         												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Geography") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics") ||
         												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Life Insurance with Salesmanship") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Banking with Secretarial Practice") ||
         												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics") ||
         												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Applications") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice& Management") ||
         												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Management")){
         											elig++;
         										}*/
                        				   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")){
                        					   businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }

                        				   //groups mark
                        				   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                        					   groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }

                        				   //lang1 mark
                        				   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
                        					   language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }

                        				   //lang2 mark
                        				   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
                        					   language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }


                        			   }
                        			   //	if(isacc==true || elig>=2){//commerce group
                        			   if(admAppln.getPersonalData().getStream().getId()==11){
                        				   remark="Eligible";
                        				   totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                        				   totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;

                        				   //penalty &bonus
                        				   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

                        			   }

                        			   else if(percentage>=45){
                        				   remark="Eligible";
                        				   totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                        				   totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;

                        				   //penalty &bonus
                        				   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

                        			   }

                        			   // adding indexmark object
                        			   StudentIndexMark si=new StudentIndexMark();

                        			   AdmAppln appln=new AdmAppln();
                        			   appln.setId(admAppln.getId());
                        			   si.setAdmAppln(appln);

                        			   Course course=new Course();
                        			   course.setId(candidatePreference.getCourse().getId());
                        			   si.setCourse(course);
                        			   si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
                        			   si.setRemark(remark);
                        			   si.setCreatedDate(new Date());
                        			   si.setLastModifiedDate(new Date());
                        			   si.setCreatedBy(form.getUserId());
                        			   si.setModifiedBy(form.getUserId());
                        			   si.setLanguage1Marks(language1Mark);
                        			   si.setLanguage2Marks(language2Mark);
                        			   si.setGroupMarks(groupMarks);
                        			   si.setElectivesubmark(electiveSubjectMark);
                        			   si.setActive(true);
                        			   si.setPrefNo(candidatePreference.getPrefNo());
                        			   si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));

                        			   indexmarkList.add(si);

                        		   }
                        		   else{
                        			   int i=0;
                        			   remark="rejected";
                        			   int elig=0;
                        			   boolean isacc=false;
                        			   double maxmark=0;
                        			   double electiveSubjectMark=0;
                        			   double language1Mark=0;                          
                        			   double language2Mark=0;
                        			   double groupMarks=0;
                        			   double percentage=0;
                        			   double businessmark=0;
                        			   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        			   while (markItr.hasNext()) {
                        				   detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
                        				   i++;


                        				   double mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   maxmark=maxmark+200;
                        				   totalmarkforpart3=totalmarkforpart3+mark;
                        				   percentage= (totalmarkforpart3/maxmark)*100;

                        				   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
                           					  detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") || 
                           					  detailMarkBO.getAdmSubjectForRank().getId() == 487){
                        					   //isacc=true;
                        					   electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());

                        				   }
                        				   /*	if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commerce") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Correspondence") ||
 												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Geography") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics") ||
 												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Life Insurance with Salesmanship") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Banking with Secretarial Practice") ||
 												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics") ||
 												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Applications") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice& Management") ||
 												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Management")){
 											elig++;
 										}*/
                        				   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")){
                        					   businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }



                        				   //groups mark
                        				   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                        					   groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }

                        				   //lang1 mark
                        				   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
                        					   language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }

                        				   //lang2 mark
                        				   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
                        					   language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }


                        			   }
                        			   if(admAppln.getPersonalData().getStream().getId()==11){
                        				   remark="Eligible";
                        				   totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        				   totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;

                        				   //penalty &bonus
                        				   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

                        			   }

                        			   else if(percentage>=45){
                        				   remark="Eligible";
                        				   totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        				   totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;

                        				   //penalty &bonus
                        				   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

                        			   }

                        			   /*				if(isacc==true || elig>=2){//commerce group

 											 remark="Eligible";
 											totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
 											totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;

 										//penalty &bonus
 		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

 										}
	                        	 //for others

 										else if(isacc==false && elig<=2 && admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("General") 
 												){//other groups
									if(percentage>=45){
										totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising

										 remark="Eligible";
										//penalty &bonus
		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;

									}
 										}

 										else if(isacc==false && elig<=2 && (admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBC") || 
											admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBX") || 
											admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBH") 
											)){
										if(percentage>=42){
											totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising

											 remark="Eligible";
											//penalty &bonus
			 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;


									}
									}
											else if(isacc==false && elig<=2 && !admapplnAdditionalInfo.getBackLogs() && 
													(admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("SC") || admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("ST"))){

													totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
													 remark="Eligible";
											}
                        			    */
                        			   // adding indexmark object
                        			   StudentIndexMark si=new StudentIndexMark();

                        			   AdmAppln appln=new AdmAppln();
                        			   appln.setId(admAppln.getId());
                        			   si.setAdmAppln(appln);

                        			   Course course=new Course();
                        			   course.setId(candidatePreference.getCourse().getId());
                        			   si.setCourse(course);
                        			   si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
                        			   si.setRemark(remark);
                        			   si.setCreatedDate(new Date());
                        			   si.setLastModifiedDate(new Date());
                        			   si.setCreatedBy(form.getUserId());
                        			   si.setModifiedBy(form.getUserId());
                        			   si.setLanguage1Marks(language1Mark);
                        			   si.setLanguage2Marks(language2Mark);
                        			   si.setGroupMarks(groupMarks);
                        			   si.setElectivesubmark(electiveSubjectMark);
                        			   si.setActive(true);
                        			   si.setPrefNo(candidatePreference.getPrefNo());
                        			   si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));

                        			   indexmarkList.add(si);
                        		   }
                        	   }
							//B.com Regular over
						
                        	//B.Com (Accounts & Audit) (Self Financing)
                        	   else if(candidatePreference.getCourse().getId()==26){
                        		/*   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
                        		   CandidatePreferenceEntranceDetails entrance=
                        			   txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
                        		   if(entrance != null){
                        			   double electMaxmark=1200;
                        			   boolean hasStudiedAccountancy = false;
                        			   boolean hasStudiedComputerApplication = false;
                        			   boolean hasStudiedBuisenessStudies = false;
                        			   if(isvhsc){
                        				   int i=0;
                        				   remark="rejected";
                        				   double maxmark=0;
                        				   double electiveSubjectMark=0;
                        				   double language1Mark=0;                          
                        				   double language2Mark=0;
                        				   double groupMarks=0;
                        				   double percentage=0;
                        				   double businessmark=0;
                        				   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        				   while (markItr.hasNext()) {
                        					   detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
                        					   i++;
                        					   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                        						   double mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        						   maxmark=maxmark+200;
                        						   totalmarkforpart3=totalmarkforpart3+mark;
                        						   percentage= (totalmarkforpart3/maxmark)*100;
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy")) {
                        						   hasStudiedAccountancy = true;
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getId() == 487) {
                        						   hasStudiedComputerApplication = true;
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
                        						  detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") ||
                        						  detailMarkBO.getAdmSubjectForRank().getId() == 487)
                        						   electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")) {
                        						   businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
                        						   hasStudiedBuisenessStudies = true;
                        					   }
                        					   //groups mark
                        					   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational"))
                        						   groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
                        					   //lang1 mark
                        					   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))
                        						   language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   //lang2 mark
                        					   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))
                        						   language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }
                        				   //	if(isacc==true || elig>=2){//commerce group
                        				   if(admAppln.getPersonalData().getStream().getId()==11){
                        					   remark="Eligible";
                        					   totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                        					   totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;
                        					   //penalty &bonus
                        					   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                        				   }
                        				   else if(percentage>=45){
                        					   remark="Eligible";
                        					   totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                        					   totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;
                        					   //penalty &bonus
                        					   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                        				   }
                        				   if(hasStudiedBuisenessStudies) {
                        					   electMaxmark += 30;
                        				   }
                        				   if(hasStudiedAccountancy || hasStudiedComputerApplication) {
                        					   electMaxmark += 200;
                        				   }
                        				   // adding indexmark object
                        				   StudentIndexMark si=new StudentIndexMark();
                        				   AdmAppln appln=new AdmAppln();
                        				   appln.setId(admAppln.getId());
                        				   si.setAdmAppln(appln);
                        				   Course course=new Course();
                        				   course.setId(candidatePreference.getCourse().getId());
                        				   si.setCourse(course);
                        				   double indexMark = ((totalmarkforpart3)/(electMaxmark)*50);
                        				   entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
                        				   if(entrance != null && entrance.getMarksObtained() != null)
                        					   indexMark += (entrance.getMarksObtained()).doubleValue();
                        				   si.setIndexMark(indexMark);
                        				   si.setRemark(remark);
                        				   si.setCreatedDate(new Date());
                        				   si.setLastModifiedDate(new Date());
                        				   si.setCreatedBy(form.getUserId());
                        				   si.setModifiedBy(form.getUserId());
                        				   si.setLanguage1Marks(language1Mark);
                        				   si.setLanguage2Marks(language2Mark);
                        				   si.setGroupMarks(groupMarks);
                        				   si.setElectivesubmark(electiveSubjectMark);
                        				   si.setActive(true);
                        				   si.setPrefNo(candidatePreference.getPrefNo());
                        				   si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));

                        				   indexmarkList.add(si);
                        			   }
                        			   else{
                        				   int i=0;
                        				   remark="rejected";
                        				   double maxmark=0;
                        				   double electiveSubjectMark=0;
                        				   double language1Mark=0;                          
                        				   double language2Mark=0;
                        				   double groupMarks=0;
                        				   double percentage=0;
                        				   double businessmark=0;
                        				   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                        				   while (markItr.hasNext()) {
                        					   detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
                        					   i++;
                        					   double mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   maxmark=maxmark+200;
                        					   totalmarkforpart3=totalmarkforpart3+mark;
                        					   percentage= (totalmarkforpart3/maxmark)*100;

                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
                                 				  detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") || 
                                 				  detailMarkBO.getAdmSubjectForRank().getId() == 487){
                        						   electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy")) {
                        						   hasStudiedAccountancy = true;
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getId() == 487) {
                        						   hasStudiedComputerApplication = true;
                        					   }
                        					   if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")) {
                        						   businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
                        						   hasStudiedBuisenessStudies = true;
                        					   }
                        					   //groups mark
                        					   if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational"))
                        						   groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
                        					   //lang1 mark
                        					   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))
                        						   language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        					   //lang2 mark
                        					   if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))
                        						   language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
                        				   }
                        				   if(admAppln.getPersonalData().getStream().getId()==11){
                        					   remark="Eligible";
                        					   totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        					   totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;
                        					   //penalty &bonus
                        					   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                        				   }
                        				   else if(percentage>=45){
                        					   remark="Eligible";
                        					   totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                        					   totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;
                        					   //penalty &bonus
                        					   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                        				   }
                        				   if(hasStudiedBuisenessStudies) {
                        					   electMaxmark += 30;
                        				   }
                        				   if(hasStudiedAccountancy || hasStudiedComputerApplication) {
                        					   electMaxmark += 200;
                        				   }
                        				   // adding indexmark object
                        				   StudentIndexMark si=new StudentIndexMark();

                        				   AdmAppln appln=new AdmAppln();
                        				   appln.setId(admAppln.getId());
                        				   si.setAdmAppln(appln);

                        				   Course course=new Course();
                        				   course.setId(candidatePreference.getCourse().getId());
                        				   si.setCourse(course);
                        				   double indexMark = ((totalmarkforpart3)/(electMaxmark)*50);
                        				   entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
                        				   if(entrance != null && entrance.getMarksObtained() != null)
                        					   indexMark += (entrance.getMarksObtained()).doubleValue();
                        				   si.setIndexMark(indexMark);
                        				   si.setRemark(remark);
                        				   si.setCreatedDate(new Date());
                        				   si.setLastModifiedDate(new Date());
                        				   si.setCreatedBy(form.getUserId());
                        				   si.setModifiedBy(form.getUserId());
                        				   si.setLanguage1Marks(language1Mark);
                        				   si.setLanguage2Marks(language2Mark);
                        				   si.setGroupMarks(groupMarks);
                        				   si.setElectivesubmark(electiveSubjectMark);
                        				   si.setActive(true);
                        				   si.setPrefNo(candidatePreference.getPrefNo());
                        				   si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));

                        				   indexmarkList.add(si);
                        			   }
                        		   }*/
                        		   


   								CandidatePreferenceEntranceDetails entrance=null;
   								Iterator<CandidatePreferenceEntranceDetails> itere =admAppln.getCandidatePreferenceEntranceDetailses().iterator();
  	                        	   while(itere.hasNext()){
  	                        		   CandidatePreferenceEntranceDetails candidateEntranceDetails=itere.next();
  	                        		   if(candidateEntranceDetails.getCourse().getId()==candidatePreference.getCourse().getId()){
  	                        		   double maxmark=0;
  		                        	   double language1Mark=0;                          
  		                        	   double language2Mark=0;
  		                        	   double groupMarks=0;
  		                        	   double electiveSubjectMark=0;
  		                        	   double Jmark=0;
  	                        	   if(candidateEntranceDetails!=null){
  	                        	   
  	                        		 if(isvhsc){
  	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
  	                        	   while (markItr.hasNext()) {
  										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
  										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
  											
  										maxmark=maxmark+200;
  										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
  										totalmarkforpart3=totalmarkforpart3+mark;
  										}
  							     		//groups mark
  										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
  										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
  										}
  																					
  										//lang1 mark
  										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
  										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
  										   }
  																					
  										//lang2 mark
  										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
  										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
  										   }
  										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
      										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
  	                        	   }	
  	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
  	                        	//totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+((Jmark*10)/100)-penalty+bonus;;
  	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)-penalty+bonus;;
  	                        		 }
  	                        		 else{
  	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
      	                        	   while (markItr.hasNext()) {
      										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
      											
      										maxmark=maxmark+200;
      										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										totalmarkforpart3=totalmarkforpart3+mark;
      										
      							     		//groups mark
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
      										}
      																					
      										//lang1 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      																					
      										//lang2 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
          										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
      	                        	   }	
      	                        //	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200+((Jmark*10)/100)-penalty+bonus;;;//normalising
      	                        	 totalmarkforpart3=(totalmarkforpart3/maxmark)*1200-penalty+bonus;;;//normalising
  	                        		 }
  	                        	   remark="Eligible";
  	                        	 
  	                        	   
  	                        	  
  	                        		   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
  	                        		    entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
  	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
  	                        		   totalmarkforpart3=((totalmarkforpart3/1200)*50)+(((Double.parseDouble(String.valueOf(entrance.getMarksObtained())))/(Double.parseDouble(String.valueOf(entrance.getTotalMarks()))))*50);
  	                        		   electiveSubjectMark=Double.parseDouble(String.valueOf(entrance.getMarksObtained()));
  	                        	   }
  	                        	 //penalty &bonus
  	                        	   
                     
  	                        	   }
  	                        
  	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
  	                        		// adding indexmark object
  		                        	   StudentIndexMark si=new StudentIndexMark();
  										
  										AdmAppln appln=new AdmAppln();
  										appln.setId(admAppln.getId());
  										si.setAdmAppln(appln);
  										
  										Course course=new Course();
  										course.setId(candidatePreference.getCourse().getId());
  										si.setCourse(course);
  										
  										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
  										si.setRemark(remark);
  										si.setCreatedDate(new Date());
  										si.setLastModifiedDate(new Date());
  										si.setCreatedBy(form.getUserId());
  										si.setModifiedBy(form.getUserId());		

  										si.setLanguage1Marks(language1Mark);
  										si.setLanguage2Marks(language2Mark);
  										si.setGroupMarks(groupMarks);
  										si.setElectivesubmark(electiveSubjectMark);
  										si.setActive(true);
  										si.setPrefNo(candidatePreference.getPrefNo());
  										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
  										
  										indexmarkList.add(si);
  									
  	                        	   }
  	                        	   
  	                        	   else {
  	                        		  
  		                        		   remark="rejected";
  		                        		// adding indexmark object
  			                        	   StudentIndexMark si=new StudentIndexMark();
  											
  											AdmAppln appln=new AdmAppln();
  											appln.setId(admAppln.getId());
  											si.setAdmAppln(appln);
  											
  											Course course=new Course();
  											course.setId(candidatePreference.getCourse().getId());
  											si.setCourse(course);
  											
  											si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
  											si.setRemark(remark);
  											si.setCreatedDate(new Date());
  											si.setLastModifiedDate(new Date());
  											si.setCreatedBy(form.getUserId());
  											si.setModifiedBy(form.getUserId());		

  											si.setLanguage1Marks(language1Mark);
  											si.setLanguage2Marks(language2Mark);
  											si.setGroupMarks(groupMarks);
  											si.setElectivesubmark(electiveSubjectMark);
  											si.setActive(true);
  											si.setPrefNo(candidatePreference.getPrefNo());
  											si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
  											
  											indexmarkList.add(si);
  										
  	                        	 
  	                        	   }
  	                        	   }
  	                        	
  						}// while close
  	                        	   
  	                        	 
  					
                         	   
                        	   }
                        	   //B.com SF
     							
     							
                                 //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("B.com")){
                              	   else if(candidatePreference.getCourse().getId()==23){
                              		  
                                      if(isvhsc){
                                      	   int i=0;
                                      	   remark="rejected";
                                      	   int elig=0;
                                      	   boolean isacc=false;
                                       	   double maxmark=0;
                                       	   double electiveSubjectMark=0;
                                       	  double language1Mark=0;                          
                                       	  double language2Mark=0;
                                       	  double groupMarks=0;
                                       	  double percentage=0;
                                       	 double businessmark=0;
                                       	 Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
               	                        		while (markItr.hasNext()) {
               										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
               										i++;
               										
               										 if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
               										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
               										maxmark=maxmark+200;
               										totalmarkforpart3=totalmarkforpart3+mark;
               										percentage= (totalmarkforpart3/maxmark)*100;
               										 }
               										if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
               										   detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") ||
               										   detailMarkBO.getAdmSubjectForRank().getId() == 487){
               											//isacc=true;
               											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
               											
               										}
               										/*if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commerce") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Correspondence") ||
               												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Geography") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics") ||
               												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Life Insurance with Salesmanship") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Banking with Secretarial Practice") ||
               												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics") ||
               												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Applications") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice& Management") ||
               												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Management")){
               											elig++;
               										}*/
               										if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")){
               											businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
               										}
               										
               										

               										//groups mark
               										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
               										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
               										}
               																					
               										//lang1 mark
               										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
               										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
               										   }
               																					
               										//lang2 mark
               										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
               										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
               										   }

               										
               	                        		}
               									//	if(isacc==true || elig>=2){//commerce group
               											if(admAppln.getPersonalData().getStream().getId()==11){
               											 remark="Eligible";
               											totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
               											totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;
               											
               										//penalty &bonus
               		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
               	                        
               										}
               										
               											else if(percentage>=45){
                  											 remark="Eligible";
                  											totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                  											totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+electiveSubjectMark+(businessmark*15)/100;
                  											
                  										//penalty &bonus
                  		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                  	                        
                  										}
               										
              	                        
              									
              									
              									
              									// adding indexmark object
              									StudentIndexMark si=new StudentIndexMark();
              									
              									AdmAppln appln=new AdmAppln();
              									appln.setId(admAppln.getId());
              									si.setAdmAppln(appln);
              									
              									Course course=new Course();
              									course.setId(candidatePreference.getCourse().getId());
              									si.setCourse(course);
              									
              									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
              									si.setRemark(remark);
              									si.setCreatedDate(new Date());
              									si.setLastModifiedDate(new Date());
              									si.setCreatedBy(form.getUserId());
              									si.setModifiedBy(form.getUserId());
              									si.setLanguage1Marks(language1Mark);
              									si.setLanguage2Marks(language2Mark);
              									si.setGroupMarks(groupMarks);
              									si.setElectivesubmark(electiveSubjectMark);
              									si.setActive(true);
              									si.setPrefNo(candidatePreference.getPrefNo());
              									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
              									
              									indexmarkList.add(si);
                                      		   
                              		   }
                              		   else{
                              	   int i=0;
                              	   remark="rejected";
                              	   int elig=0;
                              	   boolean isacc=false;
                               	   double maxmark=0;
                               	   double electiveSubjectMark=0;
                               	  double language1Mark=0;                          
                               	  double language2Mark=0;
                               	  double groupMarks=0;
                               	  double percentage=0;
                               	 double businessmark=0;
                               	 Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
       	                        		while (markItr.hasNext()) {
       										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
       										i++;
       										
       										
       										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										maxmark=maxmark+200;
       										totalmarkforpart3=totalmarkforpart3+mark;
       										percentage= (totalmarkforpart3/maxmark)*100;
       									
       									 if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Book-Keeping and Accountancy") || 
                          					detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Accountancy") || 
                           					detailMarkBO.getAdmSubjectForRank().getId() == 487){
       											//isacc=true;
       											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
       											
       										}
       									/*	if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commerce") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Correspondence") ||
       												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Commercial Geography") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics") ||
       												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Life Insurance with Salesmanship") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Banking with Secretarial Practice") ||
       												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics") ||
       												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Applications") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice& Management") ||
       												detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Informatics Practice") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Management")){
       											elig++;
       										}*/
       										if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Business Studies")){
       											businessmark=Double.parseDouble(detailMarkBO.getConversionmark());
       										}
       										
       										

       										//groups mark
       										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")  && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
       										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
       										}
       																					
       										//lang1 mark
       										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
       										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										   }
       																					
       										//lang2 mark
       										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
       										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										   }

       										
       	                        		}
       	                        		if(admAppln.getPersonalData().getStream().getId()==11){
      										 remark="Eligible";
      										totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
      										totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;
      										
      									//penalty &bonus
      	 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
                              
      									}
      									
       	                        		else if(percentage>=45){
      											 remark="Eligible";
      											totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
      											totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;
      											
      										//penalty &bonus
      		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
      	                        
      										}
      									
       						/*				if(isacc==true || elig>=2){//commerce group
       											
       											 remark="Eligible";
       											totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
       											totalmarkforpart3=totalmarkforpart3+electiveSubjectMark+(businessmark*15)/100;
       											
       										//penalty &bonus
       		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
       	                        
       										}
       										
       	                        		
       										
      	                        	 //for others
       	                        		
       										else if(isacc==false && elig<=2 && admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("General") 
       												){//other groups
      									if(percentage>=45){
      										totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
      				                       	 
      										 remark="Eligible";
      										//penalty &bonus
      		 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
      	                        
      									}
       										}
       										
       										
      									
       										else if(isacc==false && elig<=2 && (admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBC") || 
      											admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBX") || 
      											admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("OBH") 
      											)){
      										if(percentage>=42){
      											totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
      					                       	 
      											 remark="Eligible";
      											//penalty &bonus
      			 	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus;
      		                        

      									}
      									}
      									
      									
      									
      								
      											
      											else if(isacc==false && elig<=2 && !admapplnAdditionalInfo.getBackLogs() && 
      													(admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("SC") || admAppln.getPersonalData().getReligionSection().getName().equalsIgnoreCase("ST"))){
      												
      													totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
      													 remark="Eligible";
      							                    			
      															
      											}

      											
      										*/
      									
      									
      									
      									
      									// adding indexmark object
      									StudentIndexMark si=new StudentIndexMark();
      									
      									AdmAppln appln=new AdmAppln();
      									appln.setId(admAppln.getId());
      									si.setAdmAppln(appln);
      									
      									Course course=new Course();
      									course.setId(candidatePreference.getCourse().getId());
      									si.setCourse(course);
      									
      									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      									si.setRemark(remark);
      									si.setCreatedDate(new Date());
      									si.setLastModifiedDate(new Date());
      									si.setCreatedBy(form.getUserId());
      									si.setModifiedBy(form.getUserId());
      									si.setLanguage1Marks(language1Mark);
      									si.setLanguage2Marks(language2Mark);
      									si.setGroupMarks(groupMarks);
      									si.setElectivesubmark(electiveSubjectMark);
      									si.setActive(true);
      									si.setPrefNo(candidatePreference.getPrefNo());
      									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      									
      									indexmarkList.add(si);
                              		   }
       	                        		}
      							
                                 
      							//B.com SF over
      						
      							
                        	     
                        	     
							//BA Economics 
							
                           //else if(candidatePreference.getCourse().getName().equalsIgnoreCase("BA Economics")){
                        	   else if(candidatePreference.getCourse().getId()==1){
                        		   if(isvhsc){

                                	   int i=0;
        	                           double maxmark=0;
        	                           double language1Mark=0;                          
        	                           double language2Mark=0;
        	                           double groupMarks=0;
        	                           double electiveSubjectMark=0;
        	                           double marks=0;
        	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
        	                        		while (markItr.hasNext()) {
        										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
        										i++;
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
                									maxmark=maxmark+200;
        										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										totalmarkforpart3=totalmarkforpart3+mark;
        										}
        										if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics")){
        											 marks=Double.parseDouble(detailMarkBO.getConversionmark());
        											//totalmarkforpart3=totalmarkforpart3+marks;
        											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        										}
        										

        										//groups mark
        										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational	")){
        										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
        										}
        																					
        										//lang1 mark
        										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										   }
        																					
        										//lang2 mark
        										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
        										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
        										   }
        	                        		}
        	                        		
        	                        	 totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
        	                        	
        	                        	
        	                        	//penalty &bonus
        	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+(language1Mark*2);
                          
        	                           
        										remark="Eligible";
        										
        										
        										
        										// adding indexmark object
        										StudentIndexMark si=new StudentIndexMark();
        										
        										AdmAppln appln=new AdmAppln();
        										appln.setId(admAppln.getId());
        										si.setAdmAppln(appln);
        										
        										Course course=new Course();
        										course.setId(candidatePreference.getCourse().getId());
        										si.setCourse(course);
        										
        										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
        										si.setRemark(remark);
        										si.setCreatedDate(new Date());
        										si.setLastModifiedDate(new Date());
        										si.setCreatedBy(form.getUserId());
        										si.setModifiedBy(form.getUserId());
        										si.setLanguage1Marks(language1Mark);
        										si.setLanguage2Marks(language2Mark);
        										si.setGroupMarks(groupMarks);
        										si.setElectivesubmark(electiveSubjectMark);
        										si.setActive(true);
        										si.setPrefNo(candidatePreference.getPrefNo());
        										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
        										
        										indexmarkList.add(si);
                                		    
                        		   }
                        		   else{
                        	   int i=0;
	                           double maxmark=0;
	                           double language1Mark=0;                          
	                           double language2Mark=0;
	                           double groupMarks=0;
	                           double electiveSubjectMark=0;
	                           double marks=0;
	                           Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
	                        		while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
										i++;
										maxmark=maxmark+200;
										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										//ra
										//totalmarkforpart3=totalmarkforpart3+totalmarkforpart3;
										totalmarkforpart3=totalmarkforpart3+mark;
										if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics")){
											 marks=Double.parseDouble(detailMarkBO.getConversionmark());
											//totalmarkforpart3=totalmarkforpart3+marks;
											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
										}
										

										//groups mark
										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational	")){
										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
										}
																					
										//lang1 mark
										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
										   }
																					
										//lang2 mark
										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
										   }
	                        		}
	                        		
	                        	 totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
	                        	
	                        	
	                        	//penalty &bonus
	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks;
                  
	                           
										remark="Eligible";
										
										
										
										// adding indexmark object
										StudentIndexMark si=new StudentIndexMark();
										
										AdmAppln appln=new AdmAppln();
										appln.setId(admAppln.getId());
										si.setAdmAppln(appln);
										
										Course course=new Course();
										course.setId(candidatePreference.getCourse().getId());
										si.setCourse(course);
										
										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
										si.setRemark(remark);
										si.setCreatedDate(new Date());
										si.setLastModifiedDate(new Date());
										si.setCreatedBy(form.getUserId());
										si.setModifiedBy(form.getUserId());
										si.setLanguage1Marks(language1Mark);
										si.setLanguage2Marks(language2Mark);
										si.setGroupMarks(groupMarks);
										si.setElectivesubmark(electiveSubjectMark);
										si.setActive(true);
										si.setPrefNo(candidatePreference.getPrefNo());
										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
										
										indexmarkList.add(si);
                        		   }
							}
							
							
							//BA Economics over
                        	    
                        	 
                        	  //Journalism and Mass Communication   Video Production
                              	   else if(candidatePreference.getCourse().getId()==3){

        								CandidatePreferenceEntranceDetails entrance=null;
        								Iterator<CandidatePreferenceEntranceDetails> itere =admAppln.getCandidatePreferenceEntranceDetailses().iterator();
       	                        	   while(itere.hasNext()){
       	                        		   CandidatePreferenceEntranceDetails candidateEntranceDetails=itere.next();
       	                        		   if(candidateEntranceDetails.getCourse().getId()==candidatePreference.getCourse().getId()){
       	                        		   double maxmark=0;
       		                        	   double language1Mark=0;                          
       		                        	   double language2Mark=0;
       		                        	   double groupMarks=0;
       		                        	   double electiveSubjectMark=0;
       		                        	   double Jmark=0;
       	                        	   if(candidateEntranceDetails!=null){
       	                        	   
       	                        		 if(isvhsc){
       	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
       	                        	   while (markItr.hasNext()) {
       										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
       										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
       											
       										maxmark=maxmark+200;
       										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										totalmarkforpart3=totalmarkforpart3+mark;
       										}
       							     		//groups mark
       										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
       										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
       										}
       																					
       										//lang1 mark
       										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
       										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										   }
       																					
       										//lang2 mark
       										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
       										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
       										   }
       										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
           										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
           										   }
       	                        	   }	
       	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
       	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+((Jmark*10)/100)-penalty+bonus;;
       	                        		 }
       	                        		 else{
       	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
           	                        	   while (markItr.hasNext()) {
           										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
           											
           										maxmark=maxmark+200;
           										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
           										totalmarkforpart3=totalmarkforpart3+mark;
           										
           							     		//groups mark
           										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
           										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
           										}
           																					
           										//lang1 mark
           										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
           										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
           										   }
           																					
           										//lang2 mark
           										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
           										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
           										   }
           										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
               										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
               										   }
           	                        	   }	
           	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200+((Jmark*10)/100)-penalty+bonus;;;//normalising
       	                        		 }
       	                        	   remark="Eligible";
       	                        	 
       	                        	   
       	                        	  
       	                        		   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
       	                        		    entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
       	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
       	                        		   totalmarkforpart3=((totalmarkforpart3/1200)*50)+(((Double.parseDouble(String.valueOf(entrance.getMarksObtained())))/(Double.parseDouble(String.valueOf(entrance.getTotalMarks()))))*50);
       	                        		   electiveSubjectMark=Double.parseDouble(String.valueOf(entrance.getMarksObtained()));
       	                        	   }
       	                        	 //penalty &bonus
       	                        	   
                          
       	                        	   }
       	                        
       	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
       	                        		// adding indexmark object
       		                        	   StudentIndexMark si=new StudentIndexMark();
       										
       										AdmAppln appln=new AdmAppln();
       										appln.setId(admAppln.getId());
       										si.setAdmAppln(appln);
       										
       										Course course=new Course();
       										course.setId(candidatePreference.getCourse().getId());
       										si.setCourse(course);
       										
       										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
       										si.setRemark(remark);
       										si.setCreatedDate(new Date());
       										si.setLastModifiedDate(new Date());
       										si.setCreatedBy(form.getUserId());
       										si.setModifiedBy(form.getUserId());		

       										si.setLanguage1Marks(language1Mark);
       										si.setLanguage2Marks(language2Mark);
       										si.setGroupMarks(groupMarks);
       										si.setElectivesubmark(electiveSubjectMark);
       										si.setActive(true);
       										si.setPrefNo(candidatePreference.getPrefNo());
       										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
       										
       										indexmarkList.add(si);
       									
       	                        	   }
       	                        	   
       	                        	   else {
       	                        		  
       		                        		   remark="rejected";
       		                        		// adding indexmark object
       			                        	   StudentIndexMark si=new StudentIndexMark();
       											
       											AdmAppln appln=new AdmAppln();
       											appln.setId(admAppln.getId());
       											si.setAdmAppln(appln);
       											
       											Course course=new Course();
       											course.setId(candidatePreference.getCourse().getId());
       											si.setCourse(course);
       											
       											si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
       											si.setRemark(remark);
       											si.setCreatedDate(new Date());
       											si.setLastModifiedDate(new Date());
       											si.setCreatedBy(form.getUserId());
       											si.setModifiedBy(form.getUserId());		

       											si.setLanguage1Marks(language1Mark);
       											si.setLanguage2Marks(language2Mark);
       											si.setGroupMarks(groupMarks);
       											si.setElectivesubmark(electiveSubjectMark);
       											si.setActive(true);
       											si.setPrefNo(candidatePreference.getPrefNo());
       											si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
       											
       											indexmarkList.add(si);
       										
       	                        	 
       	                        	   }
       	                        	   }
       	                        	
       						}// while close
       	                        	   
       	                        	 
       					
                              	   }
                        	  //Journalism and Mass Communication and Video Production over
                        	    
                        	    //Software Development
                              	   else if(candidatePreference.getCourse().getId()==19){


       								CandidatePreferenceEntranceDetails entrance=null;
       								Iterator<CandidatePreferenceEntranceDetails> itere =admAppln.getCandidatePreferenceEntranceDetailses().iterator();
      	                        	   while(itere.hasNext()){
      	                        		   CandidatePreferenceEntranceDetails candidateEntranceDetails=itere.next();
      	                        		   if(candidateEntranceDetails.getCourse().getId()==candidatePreference.getCourse().getId()){
      	                        		   double maxmark=0;
      		                        	   double language1Mark=0;                          
      		                        	   double language2Mark=0;
      		                        	   double groupMarks=0;
      		                        	   double electiveSubjectMark=0;
      		                        	   
      	                        	   if(candidateEntranceDetails!=null){
      	                        	   
      	                        		 if(isvhsc){
      	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
      	                        	   while (markItr.hasNext()) {
      										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
      											
      										maxmark=maxmark+200;
      										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										totalmarkforpart3=totalmarkforpart3+mark;
      										}
      							     		//groups mark
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
      										}
      																					
      										//lang1 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      																					
      										//lang2 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      										
      										
      	                        	   }	
      	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
      	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+25-penalty+bonus;;
      	                        		 }
      	                        		 else{
      	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
          	                        	   while (markItr.hasNext()) {
          										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
          											
          										maxmark=maxmark+200;
          										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										totalmarkforpart3=totalmarkforpart3+mark;
          										
          							     		//groups mark
          										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
          										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
          										}
          																					
          										//lang1 mark
          										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
          										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
          																					
          										//lang2 mark
          										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
          										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
          										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
          											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
              										   }
          	                        	   }	
          	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200-penalty+bonus;;//normalising
      	                        		 }
      	                        		if(admAppln.getPersonalData().getStream().getId()==10){
      	                        	   remark="Eligible";
      	                        		}
      	                        		else{
      	                        			remark="rejected";
      	                        		}
      	                        	   
      	                        	  
      	                        		   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
      	                        		    entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
      	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
      	                        		   totalmarkforpart3=((totalmarkforpart3/1200)*50)+(((Double.parseDouble(String.valueOf(entrance.getMarksObtained())))/(Double.parseDouble(String.valueOf(entrance.getTotalMarks()))))*50);
      	                        		   electiveSubjectMark=Double.parseDouble(String.valueOf(entrance.getMarksObtained()));
      	                        	   }
      	                        	 
                         
      	                        	   }
      	                        
      	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
      	                        		// adding indexmark object
      		                        	   StudentIndexMark si=new StudentIndexMark();
      										
      										AdmAppln appln=new AdmAppln();
      										appln.setId(admAppln.getId());
      										si.setAdmAppln(appln);
      										
      										Course course=new Course();
      										course.setId(candidatePreference.getCourse().getId());
      										si.setCourse(course);
      										
      										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      										si.setRemark(remark);
      										si.setCreatedDate(new Date());
      										si.setLastModifiedDate(new Date());
      										si.setCreatedBy(form.getUserId());
      										si.setModifiedBy(form.getUserId());		

      										si.setLanguage1Marks(language1Mark);
      										si.setLanguage2Marks(language2Mark);
      										si.setGroupMarks(groupMarks);
      										si.setElectivesubmark(electiveSubjectMark);
      										si.setActive(true);
      										si.setPrefNo(candidatePreference.getPrefNo());
      										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      										
      										indexmarkList.add(si);
      									
      	                        	   }
      	                        	   
      	                        	   else {
      	                        		  
      		                        		   remark="rejected";
      		                        		// adding indexmark object
      			                        	   StudentIndexMark si=new StudentIndexMark();
      											
      											AdmAppln appln=new AdmAppln();
      											appln.setId(admAppln.getId());
      											si.setAdmAppln(appln);
      											
      											Course course=new Course();
      											course.setId(candidatePreference.getCourse().getId());
      											si.setCourse(course);
      											
      											si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      											si.setRemark(remark);
      											si.setCreatedDate(new Date());
      											si.setLastModifiedDate(new Date());
      											si.setCreatedBy(form.getUserId());
      											si.setModifiedBy(form.getUserId());		

      											si.setLanguage1Marks(language1Mark);
      											si.setLanguage2Marks(language2Mark);
      											si.setGroupMarks(groupMarks);
      											si.setElectivesubmark(electiveSubjectMark);
      											si.setActive(true);
      											si.setPrefNo(candidatePreference.getPrefNo());
      											si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      											
      											indexmarkList.add(si);
      										
      	                        	 
      	                        	   }
      	                        	   }
      	                        	
      						}// while close
      	                        	   
      	                         
                              	   }
                                //Software Development over
                        	    
                        	    
                        	    //Tourism  and  Hospitality Management
                        	    

                              	   else if(candidatePreference.getCourse().getId()==18){

                                 	CandidatePreferenceEntranceDetails entrance=null;
          								Iterator<CandidatePreferenceEntranceDetails> itere =admAppln.getCandidatePreferenceEntranceDetailses().iterator();
         	                        	   while(itere.hasNext()){
         	                        		   CandidatePreferenceEntranceDetails candidateEntranceDetails=itere.next();
         	                        		   if(candidateEntranceDetails.getCourse().getId()==candidatePreference.getCourse().getId() ){
         	                        		   double maxmark=0;
         		                        	   double language1Mark=0;                          
         		                        	   double language2Mark=0;
         		                        	   double groupMarks=0;
         		                        	   double electiveSubjectMark=0;
         		                        	  double percentage=0;
         	                        	   if(candidateEntranceDetails!=null){
         	                        	   
         	                        		 if(isvhsc){
         	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
         	                        	   while (markItr.hasNext()) {
         										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
         										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
         											
         										maxmark=maxmark+200;
         										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
         										totalmarkforpart3=totalmarkforpart3+mark;
         										}
         							     		//groups mark
         										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
         										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
         										}
         																					
         										//lang1 mark
         										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
         										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
         										   }
         																					
         										//lang2 mark
         										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
         										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
         										   }
         	                        	   }	
         	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
         	                        	 percentage= (((totalmarkforpart3/maxmark)*800+(language1Mark*2))/1200)*100;
         	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+25-penalty+bonus;
         	                        		 }
         	                        		 else{
         	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
             	                        	   while (markItr.hasNext()) {
             										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
             											
             										maxmark=maxmark+200;
             										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
             										totalmarkforpart3=totalmarkforpart3+mark;
             										
             							     		//groups mark
             										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
             										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
             										}
             																					
             										//lang1 mark
             										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
             										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
             										   }
             																					
             										//lang2 mark
             										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
             										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
             										   }
             	                        	   }	
             	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200-penalty+bonus;//normalising
             	                        	 percentage=(totalmarkforpart3/maxmark)*100;
         	                        		 }
         	                        		 if(percentage>=45){
         	                        	   remark="Eligible";
         	                        		 }
         	                        		 else{
         	                        			 remark="rejected"; 
         	                        		 }
         	                        	   
         	                        	  
         	                        		   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
         	                        		    entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
         	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
         	                        		   totalmarkforpart3=((totalmarkforpart3/1200)*50)+(((Double.parseDouble(String.valueOf(entrance.getMarksObtained())))/(Double.parseDouble(String.valueOf(entrance.getTotalMarks()))))*50);
         	                        		   electiveSubjectMark=Double.parseDouble(String.valueOf(entrance.getMarksObtained()));
         	                        	   }
         	                        	 
         	                        	   }
         	                        
         	                        	   if(entrance!=null && entrance.getMarksObtained()!=null && percentage>=45){
         	                        		// adding indexmark object
         		                        	   StudentIndexMark si=new StudentIndexMark();
         										
         										AdmAppln appln=new AdmAppln();
         										appln.setId(admAppln.getId());
         										si.setAdmAppln(appln);
         										
         										Course course=new Course();
         										course.setId(candidatePreference.getCourse().getId());
         										si.setCourse(course);
         										
         										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
         										si.setRemark(remark);
         										si.setCreatedDate(new Date());
         										si.setLastModifiedDate(new Date());
         										si.setCreatedBy(form.getUserId());
         										si.setModifiedBy(form.getUserId());		

         										si.setLanguage1Marks(language1Mark);
         										si.setLanguage2Marks(language2Mark);
         										si.setGroupMarks(groupMarks);
         										si.setElectivesubmark(electiveSubjectMark);
         										si.setActive(true);
         										si.setPrefNo(candidatePreference.getPrefNo());
         										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
         										
         										indexmarkList.add(si);
         									
         	                        	   }
         	                        	   
         	                        	   else {
         	                        		  
         		                        		   remark="rejected";
         		                        		// adding indexmark object
         			                        	   StudentIndexMark si=new StudentIndexMark();
         											
         											AdmAppln appln=new AdmAppln();
         											appln.setId(admAppln.getId());
         											si.setAdmAppln(appln);
         											
         											Course course=new Course();
         											course.setId(candidatePreference.getCourse().getId());
         											si.setCourse(course);
         											
         											si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
         											si.setRemark(remark);
         											si.setCreatedDate(new Date());
         											si.setLastModifiedDate(new Date());
         											si.setCreatedBy(form.getUserId());
         											si.setModifiedBy(form.getUserId());		

         											si.setLanguage1Marks(language1Mark);
         											si.setLanguage2Marks(language2Mark);
         											si.setGroupMarks(groupMarks);
         											si.setElectivesubmark(electiveSubjectMark);
         											si.setActive(true);
         											si.setPrefNo(candidatePreference.getPrefNo());
         											si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
         											
         											indexmarkList.add(si);
         										
         	                        	 
         	                        	   }
         	                        	   }
         	                        	
         						}// while close	   
                              	   }
                        	    //Tourism  and  Hospitality Management over
                        	     
                        	     
                        	     //Computer Science
                              	else if(candidatePreference.getCourse().getId()==28){
                              		if (admAppln.getId()==63219) {
										System.out.println("63279");
									}
                      		   if(isvhsc){

                              	   remark="Eligible";
                                 int i=0;
                                 double maxmark=0;
                                 double language1Mark=0;                          
                                 double language2Mark=0;
                                 double groupMarks=0;
                                 double electiveSubjectMark=0;
                                 double marks=0;
                                 boolean isMathematics=false;
                                 boolean isComputerScience=false;
                                 boolean isComputerApplication=false;
                                 boolean isPhysics=false;
                                 String mathematicsMarks=null;
                                 String computerScienceMarks=null;
                                 String physicsMarks=null;
                                 String computerApplication=null;
                                 double highestMark=0;
                                 double marksSub=0;
                                 double mathMarks = 0.0;
									double csMarks = 0.0;
									double caMarks = 0.0;
                                 Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                              		while (markItr.hasNext()) {
      									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
      									i++;
      									//adding 3 optional subject mark
      									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      									maxmark=maxmark+200;
      									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									totalmarkforpart3=totalmarkforpart3+mark;
      									}
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      									 marks=(Double.parseDouble(detailMarkBO.getObtainedmark())/Double.parseDouble(detailMarkBO.getMaxmark()))*400;

      									}
	
      									//groups mark
      									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
      									}
      																				
      									//lang1 mark
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									}																				
      									//lang2 mark
      									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
      										
      									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									   }
      									
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Mathematics")){
    										mathematicsMarks=detailMarkBO.getConversionmark();
    										isMathematics=true;
    										
    									}
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Computer Science")){
    										computerScienceMarks=detailMarkBO.getConversionmark();
    										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
    										isComputerScience=true;
    									}
    									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Physics")){
    										isPhysics=true;
    										physicsMarks=detailMarkBO.getConversionmark();
    									}
    									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Application")){
    										isComputerApplication=true;
    										computerApplication=detailMarkBO.getConversionmark();
    									}
    									


    									if (mathematicsMarks != null && !mathematicsMarks.isEmpty()) {
    									    mathMarks = Double.parseDouble(mathematicsMarks);
    									}

    									if (computerScienceMarks != null && !computerScienceMarks.isEmpty()) {
    									    csMarks = Double.parseDouble(computerScienceMarks);
    									}

    									if (computerApplication != null && !computerApplication.isEmpty()) {
    									    caMarks = Double.parseDouble(computerApplication);
    									}



      									
                              		}
                              		if (mathMarks > csMarks && mathMarks > caMarks) {
								        highestMark = mathMarks;
								    } else if (csMarks > mathMarks && csMarks > caMarks) {
								        highestMark = csMarks;
								    } else if (caMarks > mathMarks && caMarks > csMarks) {
								        highestMark = caMarks;
								    } else {
								        highestMark = mathMarks;
								    }
                              		if (!(isComputerScience || isMathematics || isComputerApplication)) {
                                  		remark="rejected";
            						}
                              		
                              		totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                              		
                              		 if(/*!isMathematics || */admAppln.getPersonalData().getStream().getId()!=10){
                              			totalmarkforpart3=totalmarkforpart3-penalty+bonus+highestMark;
                               	    	
                                 	      remark="rejected because he not done any Mathematics subject";
                                  		// adding indexmark object
           	                        	   StudentIndexMark si=new StudentIndexMark();
           									
           									AdmAppln appln=new AdmAppln();
           									appln.setId(admAppln.getId());
           									si.setAdmAppln(appln);
           									
           									Course course=new Course();
           									course.setId(candidatePreference.getCourse().getId());
           									si.setCourse(course);
           									
           									si.setIndexMark(Double.parseDouble(twoDForm.format(0.00)));
           									si.setRemark(remark);
           									si.setCreatedDate(new Date());
           									si.setLastModifiedDate(new Date());
           									si.setCreatedBy(form.getUserId());
           									si.setModifiedBy(form.getUserId());		

           									si.setLanguage1Marks(language1Mark);
           									si.setLanguage2Marks(language2Mark);
           									si.setGroupMarks(groupMarks);
           									si.setElectivesubmark(electiveSubjectMark);
           									si.setActive(true);
           									si.setPrefNo(candidatePreference.getPrefNo());
           									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
           									
           									indexmarkList.add(si);
           								
                                 	    	
                                 	    }else{
                              	
                              		if(isMathematics && isComputerScience){
                              			marksSub=Double.parseDouble(mathematicsMarks)+Double.parseDouble(computerScienceMarks);
                              		}else if(isMathematics && isPhysics){
                              			marksSub=Double.parseDouble(mathematicsMarks)+Double.parseDouble(physicsMarks);
                              		}else if(isMathematics){
                              			marksSub=Double.parseDouble(mathematicsMarks);
                              		}
                              		
                              			//penalty &bonus
       	                        	  // totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+marksSub; 
       	                        	 totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+highestMark;
                                 
      								
      									// adding indexmark object
      									StudentIndexMark si=new StudentIndexMark();
      									
      									AdmAppln appln=new AdmAppln();
      									appln.setId(admAppln.getId());
      									si.setAdmAppln(appln);
      									
      									Course course=new Course();
      									course.setId(candidatePreference.getCourse().getId());
      									si.setCourse(course);
      									
      									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      									si.setRemark(remark);
      									si.setCreatedDate(new Date());
      									si.setLastModifiedDate(new Date());
      									si.setCreatedBy(form.getUserId());
      									si.setModifiedBy(form.getUserId());

      									si.setLanguage1Marks(language1Mark);
      									si.setLanguage2Marks(language2Mark);
      									si.setGroupMarks(groupMarks);
      									si.setElectivesubmark(electiveSubjectMark);
      									si.setActive(true);
      									si.setPrefNo(candidatePreference.getPrefNo());
      									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      									
      									indexmarkList.add(si);
                              		     
                      		  }
                      		   }
                      		   else{
                      	   remark="Eligible";
                         int i=0; 
                         double maxmark=0;
                         double language1Mark=0;                          
                         double language2Mark=0;
                         double groupMarks=0;
                         double electiveSubjectMark=0;
                         double marks=0;
                         boolean isMathematics=false;
                         boolean isComputerScience=false;
                         boolean isComputerApplication=false;
                         boolean isPhysics=false;
                         String mathematicsMarks=null;
                         String computerScienceMarks=null;
                         String physicsMarks=null;
                         String computerApplication=null;
                         double highestMark=0;
                         double marksSub=0;
                         double mathMarks = 0.0;
							double csMarks = 0.0;
							double caMarks = 0.0;
                         Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                      		while (markItr.hasNext()) {
									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									i++;
									maxmark=maxmark+200;
									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
									
									
									
									
									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									}																				
									//lang2 mark
									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
										
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
									totalmarkforpart3=totalmarkforpart3+mark;
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")){
										mathematicsMarks=detailMarkBO.getConversionmark();
										isMathematics=true;
										
									}
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science")){
										computerScienceMarks=detailMarkBO.getConversionmark();
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
										isComputerScience=true;
									}
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics")){
										isPhysics=true;
										physicsMarks=detailMarkBO.getConversionmark();
									}
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics")){
										isPhysics=true;
										physicsMarks=detailMarkBO.getConversionmark();
									}
									if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Application")){
										isComputerApplication=true;
										computerApplication=detailMarkBO.getConversionmark();
									}


									if (mathematicsMarks != null && !mathematicsMarks.isEmpty()) {
									    mathMarks = Double.parseDouble(mathematicsMarks);
									}

									if (computerScienceMarks != null && !computerScienceMarks.isEmpty()) {
									    csMarks = Double.parseDouble(computerScienceMarks);
									}

									if (computerApplication != null && !computerApplication.isEmpty()) {
									    caMarks = Double.parseDouble(computerApplication);
									}

                      		}
                      	  if (mathMarks > csMarks && mathMarks > caMarks) {
						        highestMark = mathMarks;
						    } else if (csMarks > mathMarks && csMarks > caMarks) {
						        highestMark = csMarks;
						    } else if (caMarks > mathMarks && caMarks > csMarks) {
						        highestMark = caMarks;
						    } else {
						        highestMark = mathMarks;
						    }
                      	  if (!(isComputerScience || isMathematics || isComputerApplication)) {
                      		remark="rejected";
						}
                      		totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                      	
                      	    if(/*!isMathematics  || */admAppln.getPersonalData().getStream().getId()!=10){
                      	    totalmarkforpart3=totalmarkforpart3-penalty+bonus+highestMark;
                      	      remark="rejected because he not done any Mathematics subject";
                       		// adding indexmark object
	                        	   StudentIndexMark si=new StudentIndexMark();
									
									AdmAppln appln=new AdmAppln();
									appln.setId(admAppln.getId());
									si.setAdmAppln(appln);
									
									Course course=new Course();
									course.setId(candidatePreference.getCourse().getId());
									si.setCourse(course);
									
									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
									si.setRemark(remark);
									si.setCreatedDate(new Date());
									si.setLastModifiedDate(new Date());
									si.setCreatedBy(form.getUserId());
									si.setModifiedBy(form.getUserId());		

									si.setLanguage1Marks(language1Mark);
									si.setLanguage2Marks(language2Mark);
									si.setGroupMarks(groupMarks);
									si.setElectivesubmark(electiveSubjectMark);
									si.setActive(true);
									si.setPrefNo(candidatePreference.getPrefNo());
									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
									
									indexmarkList.add(si);
								
                      	    	
                      	    }else{
                      	    	
                      	    
                      		if(isMathematics && isComputerScience){
                      			marksSub=Double.parseDouble(mathematicsMarks)+Double.parseDouble(computerScienceMarks);
                      		}else if(isMathematics && isPhysics){
                      			marksSub=Double.parseDouble(mathematicsMarks)+Double.parseDouble(physicsMarks);
                      		}else if(isMathematics){
                      			marksSub=Double.parseDouble(mathematicsMarks);
                      		}
                      			//penalty &bonus
	                        	   //totalmarkforpart3=totalmarkforpart3-penalty+bonus+marksSub;
                      				totalmarkforpart3=totalmarkforpart3-penalty+bonus+highestMark;
                         
								
									// adding indexmark object
									StudentIndexMark si=new StudentIndexMark();
									
									AdmAppln appln=new AdmAppln();
									appln.setId(admAppln.getId());
									si.setAdmAppln(appln);
									
									Course course=new Course();
									course.setId(candidatePreference.getCourse().getId());
									si.setCourse(course);
									
									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
									si.setRemark(remark);
									si.setCreatedDate(new Date());
									si.setLastModifiedDate(new Date());
									si.setCreatedBy(form.getUserId());
									si.setModifiedBy(form.getUserId());

									si.setLanguage1Marks(language1Mark);
									si.setLanguage2Marks(language2Mark);
									si.setGroupMarks(groupMarks);
									si.setElectivesubmark(electiveSubjectMark);
									si.setActive(true);
									si.setPrefNo(candidatePreference.getPrefNo());
									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
									
									indexmarkList.add(si);
                      	        }
                      		   }
                             }
                        	     //Computer Science over
                        	     
                        	     
                              	else if(candidatePreference.getCourse().getId()==29){
                              		if (admAppln.getId()==56938) {
    									System.out.println();
    								}
                      		   if(isvhsc){

                              	   remark="Eligible";
                                 int i=0;
                                 double maxmark=0;
                                 double language1Mark=0;                          
                                 double language2Mark=0;
                                 double groupMarks=0;
                                 double electiveSubjectMark=0;
                                 double marks=0;
                                 double langbonus=0;
                                 double communicativeEnglishMarks=0;
                                 double literatureCommunityMark=0;
                                 
                                 Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                              		while (markItr.hasNext()) {
      									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
      									i++;
      									//adding 3 optional subject mark
      									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      									maxmark=maxmark+200;
      									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									totalmarkforpart3=totalmarkforpart3+mark;
      									}
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      									 marks=(Double.parseDouble(detailMarkBO.getObtainedmark())/Double.parseDouble(detailMarkBO.getMaxmark()))*400;
      									 langbonus=Double.parseDouble(detailMarkBO.getConversionmark());

      									}
      									
      									
      									//groups mark
      									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
      									}
      																				
      									//lang1 mark
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									}																				
      									//lang2 mark
      									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
      									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
      										
      									   language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      									   }
      									
      									if(candidatePreference.getCourse().getId()==29 && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Communicative English")){
      											communicativeEnglishMarks=Double.parseDouble(detailMarkBO.getConversionmark())*0.5;
      											electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
      									}
      									if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English Literature")) {
    										
    											literatureCommunityMark=Double.parseDouble(detailMarkBO.getConversionmark());
										}
      									
                              		}
                              		if (marks!=0) {
                              			literatureCommunityMark=(literatureCommunityMark/100)*10;
										
									}else{
										literatureCommunityMark=0;
									}
                              		
                              		totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
                              	
                              	
                              			//penalty &bonus
       	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+langbonus+communicativeEnglishMarks+literatureCommunityMark;
                                 
      								
      									// adding indexmark object
      									StudentIndexMark si=new StudentIndexMark();
      									
      									AdmAppln appln=new AdmAppln();
      									appln.setId(admAppln.getId());
      									si.setAdmAppln(appln);
      									
      									Course course=new Course();
      									course.setId(candidatePreference.getCourse().getId());
      									si.setCourse(course);
      									
      									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      									si.setRemark(remark);
      									si.setCreatedDate(new Date());
      									si.setLastModifiedDate(new Date());
      									si.setCreatedBy(form.getUserId());
      									si.setModifiedBy(form.getUserId());

      									si.setLanguage1Marks(language1Mark);
      									si.setLanguage2Marks(language2Mark);
      									si.setGroupMarks(groupMarks);
      									si.setElectivesubmark(electiveSubjectMark);
      									si.setActive(true);
      									si.setPrefNo(candidatePreference.getPrefNo());
      									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      									
      									indexmarkList.add(si);
                              		     
                      		   }
                      		   else{
                      	   remark="Eligible";
                         int i=0;
                         double maxmark=0;
                         double language1Mark=0;                          
                         double language2Mark=0;
                         double groupMarks=0;
                         double electiveSubjectMark=0;
                         double marks=0;
                         double communicativeEnglishMarks=0;
                         double literatureCommunityMark=0;
                         Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
                      		while (markItr.hasNext()) {
									detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
									i++;
									maxmark=maxmark+200;
									double mark=Double.parseDouble(detailMarkBO.getConversionmark());
									//ra
									//totalmarkforpart3=totalmarkforpart3+totalmarkforpart3;
									totalmarkforpart3=totalmarkforpart3+mark;
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									 marks=Double.parseDouble(detailMarkBO.getConversionmark());
									
									}
									
									
									//groups mark
									if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
									groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
									}
																				
									//lang1 mark
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
									language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									}																				
									//lang2 mark
									//if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")  || !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("French"))){
									if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && (!detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English"))){
										
									language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
									   }
									if(candidatePreference.getCourse().getId()==29 && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Communicative English")){
										communicativeEnglishMarks=Double.parseDouble(detailMarkBO.getConversionmark())*0.5;
										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
									
								}
									if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English Literature")) {
											literatureCommunityMark=Double.parseDouble(detailMarkBO.getConversionmark());
									}
									
                      		}
                      		if (marks!=0) {
                      			literatureCommunityMark=(literatureCommunityMark/100)*10;
								
							}else{
								literatureCommunityMark=0;
							}
                      		
                      		totalmarkforpart3=(totalmarkforpart3/maxmark)*1200;//normalising
                      	
                      	
                      			//penalty &bonus
	                        	   totalmarkforpart3=totalmarkforpart3-penalty+bonus+marks+communicativeEnglishMarks+literatureCommunityMark;
                         
								
									// adding indexmark object
									StudentIndexMark si=new StudentIndexMark();
									
									AdmAppln appln=new AdmAppln();
									appln.setId(admAppln.getId());
									si.setAdmAppln(appln);
									
									Course course=new Course();
									course.setId(candidatePreference.getCourse().getId());
									si.setCourse(course);
									
									si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
									si.setRemark(remark);
									si.setCreatedDate(new Date());
									si.setLastModifiedDate(new Date());
									si.setCreatedBy(form.getUserId());
									si.setModifiedBy(form.getUserId());

									si.setLanguage1Marks(language1Mark);
									si.setLanguage2Marks(language2Mark);
									si.setGroupMarks(groupMarks);
									si.setElectivesubmark(electiveSubjectMark);
									si.setActive(true);
									si.setPrefNo(candidatePreference.getPrefNo());
									si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
									
									indexmarkList.add(si);
                      		   }
                              }
                        	     
                        	     
                              	else if(candidatePreference.getCourse().getId()==27){/*

                              		   remark="Eligible";
	                        		   double maxmark=0;
		                        	   double language1Mark=0;                          
		                        	   double language2Mark=0;
		                        	   double groupMarks=0;
		                        	   double electiveSubjectMark=0;
		                        	   double Jmark=0;
	                        	   
	                        		 if(isvhsc){
	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
	                        	   while (markItr.hasNext()) {
										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
											
										maxmark=maxmark+200;
										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
										totalmarkforpart3=totalmarkforpart3+mark;
										}
							     		//groups mark
										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
										}
																					
										//lang1 mark
										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
										   }
																					
										//lang2 mark
										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
										   }
										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
    										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
    										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
    										   }
	                        	   }	
	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+((Jmark*10)/100)-penalty+bonus;;
	                        		 }
	                        		 else{
	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
    	                        	   while (markItr.hasNext()) {
    										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
    											
    										maxmark=maxmark+200;
    										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
    										totalmarkforpart3=totalmarkforpart3+mark;
    										
    							     		//groups mark
    										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
    										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
    										}
    																					
    										//lang1 mark
    										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    										   }
    																					
    										//lang2 mark
    										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
    										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
    										   }
    										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
        										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
        										electiveSubjectMark=Double.parseDouble(detailMarkBO.getConversionmark());
        										   }
    	                        	   }	
    	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200+((Jmark*10)/100)-penalty+bonus;;;//normalising
	                        		 }
	                        
	                        		// adding indexmark object
		                        	   StudentIndexMark si=new StudentIndexMark();
										
										AdmAppln appln=new AdmAppln();
										appln.setId(admAppln.getId());
										si.setAdmAppln(appln);
										
										Course course=new Course();
										course.setId(candidatePreference.getCourse().getId());
										si.setCourse(course);
										
										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
										si.setRemark(remark);
										si.setCreatedDate(new Date());
										si.setLastModifiedDate(new Date());
										si.setCreatedBy(form.getUserId());
										si.setModifiedBy(form.getUserId());		

										si.setLanguage1Marks(language1Mark);
										si.setLanguage2Marks(language2Mark);
										si.setGroupMarks(groupMarks);
										si.setElectivesubmark(electiveSubjectMark);
										si.setActive(true);
										si.setPrefNo(candidatePreference.getPrefNo());
										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
										
										indexmarkList.add(si);
									
                              	*/
                              		CandidatePreferenceEntranceDetails entrance=null;
       								Iterator<CandidatePreferenceEntranceDetails> itere =admAppln.getCandidatePreferenceEntranceDetailses().iterator();
      	                        	   while(itere.hasNext()){
      	                        		   CandidatePreferenceEntranceDetails candidateEntranceDetails=itere.next();
      	                        		   if(candidateEntranceDetails.getCourse().getId()==candidatePreference.getCourse().getId()){
      	                        		   double maxmark=0;
      		                        	   double language1Mark=0;                          
      		                        	   double language2Mark=0;
      		                        	   double groupMarks=0;
      		                        	   double electiveSubjectMark=0;
      		                        	   double Jmark=0;
      	                        	   if(candidateEntranceDetails!=null){
      	                        	   
      	                        		 if(isvhsc){
      	                        	   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
      	                        	   while (markItr.hasNext()) {
      										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language")){
      											
      										maxmark=maxmark+200;
      										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										totalmarkforpart3=totalmarkforpart3+mark;
      										}
      							     		//groups mark
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
      										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
      										}
      																					
      										//lang1 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      																					
      										//lang2 mark
      										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
      										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
      										   }
      										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
          										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
      	                        	   }	
      	                        	  totalmarkforpart3=(totalmarkforpart3/maxmark)*800;//normalising
      	                        	//totalmarkforpart3=totalmarkforpart3+(language1Mark*2)+((Jmark*10)/100)-penalty+bonus;;
      	                        	totalmarkforpart3=totalmarkforpart3+(language1Mark*2)-penalty+bonus;;
      	                        		 }
      	                        		 else{
      	                        		   Iterator<AdmSubjectMarkForRank> markItr=detailMarks.iterator();
          	                        	   while (markItr.hasNext()) {
          										detailMarkBO= (AdmSubjectMarkForRank) markItr.next();
          											
          										maxmark=maxmark+200;
          										double mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										totalmarkforpart3=totalmarkforpart3+mark;
          										
          							     		//groups mark
          										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")){
          										groupMarks=groupMarks+Double.parseDouble(detailMarkBO.getConversionmark());
          										}
          																					
          										//lang1 mark
          										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
          										language1Mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
          																					
          										//lang2 mark
          										if(detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")){
          										language2Mark=Double.parseDouble(detailMarkBO.getConversionmark());
          										   }
          										if(!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Journalism")){
              										Jmark=Double.parseDouble(detailMarkBO.getConversionmark());
              										   }
          	                        	   }	
          	                        //	  totalmarkforpart3=(totalmarkforpart3/maxmark)*1200+((Jmark*10)/100)-penalty+bonus;;;//normalising
          	                        	 totalmarkforpart3=(totalmarkforpart3/maxmark)*1200-penalty+bonus;;;//normalising
      	                        		 }
      	                        	   remark="Eligible";
      	                        	 
      	                        	   
      	                        	  
      	                        		   IApplicationEditTransaction txn= new ApplicationEditTransactionimpl();
      	                        		    entrance=txn.getEntranceDetails(candidatePreference.getCourse().getId(),admAppln.getId());
      	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
      	                        		   totalmarkforpart3=((totalmarkforpart3/1200)*50)+(((Double.parseDouble(String.valueOf(entrance.getMarksObtained())))/(Double.parseDouble(String.valueOf(entrance.getTotalMarks()))))*50);
      	                        		   electiveSubjectMark=Double.parseDouble(String.valueOf(entrance.getMarksObtained()));
      	                        	   }
      	                        	 //penalty &bonus
      	                        	   
                         
      	                        	   }
      	                        
      	                        	   if(entrance!=null && entrance.getMarksObtained()!=null){
      	                        		// adding indexmark object
      		                        	   StudentIndexMark si=new StudentIndexMark();
      										
      										AdmAppln appln=new AdmAppln();
      										appln.setId(admAppln.getId());
      										si.setAdmAppln(appln);
      										
      										Course course=new Course();
      										course.setId(candidatePreference.getCourse().getId());
      										si.setCourse(course);
      										
      										si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      										si.setRemark(remark);
      										si.setCreatedDate(new Date());
      										si.setLastModifiedDate(new Date());
      										si.setCreatedBy(form.getUserId());
      										si.setModifiedBy(form.getUserId());		

      										si.setLanguage1Marks(language1Mark);
      										si.setLanguage2Marks(language2Mark);
      										si.setGroupMarks(groupMarks);
      										si.setElectivesubmark(electiveSubjectMark);
      										si.setActive(true);
      										si.setPrefNo(candidatePreference.getPrefNo());
      										si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      										
      										indexmarkList.add(si);
      									
      	                        	   }
      	                        	   
      	                        	   else {
      	                        		  
      		                        		   remark="rejected";
      		                        		// adding indexmark object
      			                        	   StudentIndexMark si=new StudentIndexMark();
      											
      											AdmAppln appln=new AdmAppln();
      											appln.setId(admAppln.getId());
      											si.setAdmAppln(appln);
      											
      											Course course=new Course();
      											course.setId(candidatePreference.getCourse().getId());
      											si.setCourse(course);
      											
      											si.setIndexMark(Double.parseDouble(twoDForm.format(totalmarkforpart3)));
      											si.setRemark(remark);
      											si.setCreatedDate(new Date());
      											si.setLastModifiedDate(new Date());
      											si.setCreatedBy(form.getUserId());
      											si.setModifiedBy(form.getUserId());		

      											si.setLanguage1Marks(language1Mark);
      											si.setLanguage2Marks(language2Mark);
      											si.setGroupMarks(groupMarks);
      											si.setElectivesubmark(electiveSubjectMark);
      											si.setActive(true);
      											si.setPrefNo(candidatePreference.getPrefNo());
      											si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
      											
      											indexmarkList.add(si);
      										
      	                        	 
      	                        	   }
      	                        	   }
      	                        	
      						}	
                              	
                              	
                              	
                              	} 
                              	 else if (candidatePreference.getCourse().getId() == 30) {
                                     if (isvhsc) {
                                         if (isvhsc) {
                                             int i = 0;
                                             double maxmark = 0.0;
                                             double language1Mark = 0.0;
                                             double language2Mark = 0.0;
                                             double groupMarks = 0.0;
                                             double electiveSubjectMark = 0.0;
                                             double marks = 0.0;
                                             final Iterator<AdmSubjectMarkForRank> markItr2 = detailMarks.iterator();
                                             while (markItr2.hasNext()) {
                                                 detailMarkBO = markItr2.next();
                                                 ++i;
                                                 if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                     maxmark += 200.0;
                                                     final double mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                                     totalmarkforpart3 += mark2;
                                                 }
                                                 if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics")) {
                                                     marks = Double.parseDouble(detailMarkBO.getConversionmark());
                                                     electiveSubjectMark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational\t")) {
                                                     groupMarks += Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                     language1Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                     language2Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                             }
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark * 800.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + marks + language1Mark * 2.0;
                                             remark = "Eligible";
                                             final StudentIndexMark si2 = new StudentIndexMark();
                                             final AdmAppln appln2 = new AdmAppln();
                                             appln2.setId(admAppln.getId());
                                             si2.setAdmAppln(appln2);
                                             final Course course2 = new Course();
                                             course2.setId(candidatePreference.getCourse().getId());
                                             si2.setCourse(course2);
                                             si2.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                             si2.setRemark(remark);
                                             si2.setCreatedDate(new Date());
                                             si2.setLastModifiedDate(new Date());
                                             si2.setCreatedBy(form.getUserId());
                                             si2.setModifiedBy(form.getUserId());
                                             si2.setLanguage1Marks(Double.valueOf(language1Mark));
                                             si2.setLanguage2Marks(Double.valueOf(language2Mark));
                                             si2.setGroupMarks(Double.valueOf(groupMarks));
                                             si2.setElectivesubmark(Double.valueOf(electiveSubjectMark));
                                             si2.setActive(true);
                                             si2.setPrefNo(candidatePreference.getPrefNo());
                                             si2.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                             indexmarkList.add(si2);
                                         }
                                         else {
                                             int i = 0;
                                             double maxmark = 0.0;
                                             double language1Mark = 0.0;
                                             double language2Mark = 0.0;
                                             double groupMarks = 0.0;
                                             double electiveSubjectMark = 0.0;
                                             double marks = 0.0;
                                             final Iterator<AdmSubjectMarkForRank> markItr2 = detailMarks.iterator();
                                             while (markItr2.hasNext()) {
                                                 detailMarkBO = markItr2.next();
                                                 ++i;
                                                 maxmark += 200.0;
                                                 final double mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 totalmarkforpart3 += mark2;
                                                 if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics")) {
                                                     marks = Double.parseDouble(detailMarkBO.getConversionmark());
                                                     electiveSubjectMark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational\t")) {
                                                     groupMarks += Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                     language1Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                                 if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                     language2Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 }
                                             }
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark * 1200.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + marks;
                                             remark = "Eligible";
                                             final StudentIndexMark si2 = new StudentIndexMark();
                                             final AdmAppln appln2 = new AdmAppln();
                                             appln2.setId(admAppln.getId());
                                             si2.setAdmAppln(appln2);
                                             final Course course2 = new Course();
                                             course2.setId(candidatePreference.getCourse().getId());
                                             si2.setCourse(course2);
                                             si2.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                             si2.setRemark(remark);
                                             si2.setCreatedDate(new Date());
                                             si2.setLastModifiedDate(new Date());
                                             si2.setCreatedBy(form.getUserId());
                                             si2.setModifiedBy(form.getUserId());
                                             si2.setLanguage1Marks(Double.valueOf(language1Mark));
                                             si2.setLanguage2Marks(Double.valueOf(language2Mark));
                                             si2.setGroupMarks(Double.valueOf(groupMarks));
                                             si2.setElectivesubmark(Double.valueOf(electiveSubjectMark));
                                             si2.setActive(true);
                                             si2.setPrefNo(candidatePreference.getPrefNo());
                                             si2.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                             indexmarkList.add(si2);
                                         }
                                     }
                                     else {
                                         int i = 0;
                                         double maxmark = 0.0;
                                         double language1Mark = 0.0;
                                         double language2Mark = 0.0;
                                         double groupMarks = 0.0;
                                         double electiveSubjectMark = 0.0;
                                         double marks = 0.0;
                                         final Iterator<AdmSubjectMarkForRank> markItr2 = detailMarks.iterator();
                                         while (markItr2.hasNext()) {
                                             detailMarkBO = markItr2.next();
                                             ++i;
                                             maxmark += 200.0;
                                             final double mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             totalmarkforpart3 += mark2;
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Economics")) {
                                                 marks = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 electiveSubjectMark = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational\t")) {
                                                 groupMarks += Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language1Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language2Mark = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                         }
                                         totalmarkforpart3 = totalmarkforpart3 / maxmark * 1200.0;
                                         totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + marks;
                                         remark = "Eligible";
                                         final StudentIndexMark si2 = new StudentIndexMark();
                                         final AdmAppln appln2 = new AdmAppln();
                                         appln2.setId(admAppln.getId());
                                         si2.setAdmAppln(appln2);
                                         final Course course2 = new Course();
                                         course2.setId(candidatePreference.getCourse().getId());
                                         si2.setCourse(course2);
                                         si2.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                         si2.setRemark(remark);
                                         si2.setCreatedDate(new Date());
                                         si2.setLastModifiedDate(new Date());
                                         si2.setCreatedBy(form.getUserId());
                                         si2.setModifiedBy(form.getUserId());
                                         si2.setLanguage1Marks(Double.valueOf(language1Mark));
                                         si2.setLanguage2Marks(Double.valueOf(language2Mark));
                                         si2.setGroupMarks(Double.valueOf(groupMarks));
                                         si2.setElectivesubmark(Double.valueOf(electiveSubjectMark));
                                         si2.setActive(true);
                                         si2.setPrefNo(candidatePreference.getPrefNo());
                                         si2.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                         indexmarkList.add(si2);
                                     }
                                 }
                                 else if (candidatePreference.getCourse().getId() == 31) {
                                     if (isvhsc) {
                                         double maxmark2 = 0.0;
                                         double language1Mark2 = 0.0;
                                         double language2Mark2 = 0.0;
                                         double groupMarks2 = 0.0;
                                         double electiveSubjectMark2 = 0.0;
                                         boolean iselig = false;
                                         boolean iseli = false;
                                         double maths = 0.0;
                                         final Iterator<AdmSubjectMarkForRank> markItr8 = detailMarks.iterator();
                                         while (markItr8.hasNext()) {
                                             detailMarkBO = markItr8.next();
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 final double mark8 = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 totalmarkforpart3 += mark8;
                                                 maxmark2 += 200.0;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 iselig = true;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Statistics")) {
                                                 iseli = true;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Statistics")) {
                                                 electiveSubjectMark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 groupMarks2 += Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language1Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 maths = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language2Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                         }
                                         if (iselig && iseli) {
                                             remark = "Eligible";
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark2 * 800.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + electiveSubjectMark2 * 0.15 + language1Mark2 * 2.0 + maths;
                                         }
                                         else {
                                             remark = "rejected because there is no mathematics";
                                         }
                                         final StudentIndexMark si7 = new StudentIndexMark();
                                         final AdmAppln appln7 = new AdmAppln();
                                         appln7.setId(admAppln.getId());
                                         si7.setAdmAppln(appln7);
                                         final Course course7 = new Course();
                                         course7.setId(candidatePreference.getCourse().getId());
                                         si7.setCourse(course7);
                                         si7.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                         si7.setRemark(remark);
                                         si7.setCreatedDate(new Date());
                                         si7.setLastModifiedDate(new Date());
                                         si7.setCreatedBy(form.getUserId());
                                         si7.setModifiedBy(form.getUserId());
                                         si7.setLanguage1Marks(Double.valueOf(language1Mark2));
                                         si7.setLanguage2Marks(Double.valueOf(language2Mark2));
                                         si7.setGroupMarks(Double.valueOf(groupMarks2));
                                         si7.setElectivesubmark(Double.valueOf(electiveSubjectMark2));
                                         si7.setActive(true);
                                         si7.setPrefNo(candidatePreference.getPrefNo());
                                         si7.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                         indexmarkList.add(si7);
                                     }
                                     else {
                                         double maxmark2 = 0.0;
                                         double language1Mark2 = 0.0;
                                         double language2Mark2 = 0.0;
                                         double groupMarks2 = 0.0;
                                         double electiveSubjectMark2 = 0.0;
                                         boolean iselig = false;
                                         boolean iseli = false;
                                         double maths = 0.0;
                                         final Iterator<AdmSubjectMarkForRank> markItr8 = detailMarks.iterator();
                                         while (markItr8.hasNext()) {
                                             detailMarkBO = markItr8.next();
                                             final double mark8 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             totalmarkforpart3 += mark8;
                                             maxmark2 += 200.0;
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 iselig = true;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science") || detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Statistics")) {
                                                 iseli = true;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Statistics")) {
                                                 electiveSubjectMark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 groupMarks2 += Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language1Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 maths = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language2Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                         }
                                         if (iselig && iseli) {
                                             remark = "Eligible";
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark2 * 1200.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + electiveSubjectMark2 * 0.15 + maths;
                                         }
                                         else {
                                             remark = "rejected because there is no mathematics";
                                         }
                                         final StudentIndexMark si7 = new StudentIndexMark();
                                         final AdmAppln appln7 = new AdmAppln();
                                         appln7.setId(admAppln.getId());
                                         si7.setAdmAppln(appln7);
                                         final Course course7 = new Course();
                                         course7.setId(candidatePreference.getCourse().getId());
                                         si7.setCourse(course7);
                                         si7.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                         si7.setRemark(remark);
                                         si7.setCreatedDate(new Date());
                                         si7.setLastModifiedDate(new Date());
                                         si7.setCreatedBy(form.getUserId());
                                         si7.setModifiedBy(form.getUserId());
                                         si7.setLanguage1Marks(Double.valueOf(language1Mark2));
                                         si7.setLanguage2Marks(Double.valueOf(language2Mark2));
                                         si7.setGroupMarks(Double.valueOf(groupMarks2));
                                         si7.setElectivesubmark(Double.valueOf(electiveSubjectMark2));
                                         si7.setActive(true);
                                         si7.setPrefNo(candidatePreference.getPrefNo());
                                         si7.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                         indexmarkList.add(si7);
                                     }
                                 }
                                 else {
                                	 //B.Sc. Physics with Machine Learning
                                     if (candidatePreference.getCourse().getId() != 33) {
                                         continue;
                                     }
                                     if (admAppln.getId()==62756) {
										System.out.println("sjsj");
									}
                                     if (isvhsc) {
                                         double maxmark2 = 0.0;
                                         double language1Mark2 = 0.0;
                                         double language2Mark2 = 0.0;
                                         double groupMarks2 = 0.0;
                                         double electiveSubjectMark2 = 0.0;
                                         boolean iselig = false;
                                         boolean isPysicsContain = false;
                                         boolean isChemistryContain = false;
                                         boolean ismathsContain = false;
                                         boolean isComputerContain = false;
                                         final Iterator<AdmSubjectMarkForRank> markItr3 = detailMarks.iterator();
                                         while (markItr3.hasNext()) {
                                             detailMarkBO = markItr3.next();
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 final double mark3 = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 totalmarkforpart3 += mark3;
                                                 maxmark2 += 200.0;
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics")) {
                                                 //iselig = true;
                                            	 isPysicsContain=true;
                                                 electiveSubjectMark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 groupMarks2 += Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language1Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language2Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Chemistry")){
                                            	 isChemistryContain=true;
         										}
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 ismathsContain = true;
                                             }
                                             if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science")){
                                            	 isComputerContain=true;
                                             }
                                         }
                                         if (isComputerContain && ismathsContain && isChemistryContain && isPysicsContain) {
                                        	 iselig=true;
										}
                                         if (iselig) {
                                             remark = "Eligible";
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark2 * 800.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + electiveSubjectMark2 + language1Mark2 * 2.0;
                                         }
                                         else {
                                             remark = "rejected";
                                         }
                                         final StudentIndexMark si3 = new StudentIndexMark();
                                         final AdmAppln appln3 = new AdmAppln();
                                         appln3.setId(admAppln.getId());
                                         si3.setAdmAppln(appln3);
                                         final Course course3 = new Course();
                                         course3.setId(candidatePreference.getCourse().getId());
                                         si3.setCourse(course3);
                                         si3.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                         si3.setRemark(remark);
                                         si3.setCreatedDate(new Date());
                                         si3.setLastModifiedDate(new Date());
                                         si3.setCreatedBy(form.getUserId());
                                         si3.setModifiedBy(form.getUserId());
                                         si3.setLanguage1Marks(Double.valueOf(language1Mark2));
                                         si3.setLanguage2Marks(Double.valueOf(language2Mark2));
                                         si3.setGroupMarks(Double.valueOf(groupMarks2));
                                         si3.setElectivesubmark(Double.valueOf(electiveSubjectMark2));
                                         si3.setActive(true);
                                         si3.setPrefNo(candidatePreference.getPrefNo());
                                         si3.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                         indexmarkList.add(si3);
                                     }
                                     else {
                                         double maxmark2 = 0.0;
                                         double language1Mark2 = 0.0;
                                         double language2Mark2 = 0.0;
                                         double groupMarks2 = 0.0;
                                         double electiveSubjectMark2 = 0.0;
                                         boolean iselig = false;
                                         boolean isPysicsContain = false;
                                         boolean isChemistryContain = false;
                                         boolean ismathsContain = false;
                                         boolean isComputerContain = false;
                                         final Iterator<AdmSubjectMarkForRank> markItr3 = detailMarks.iterator();
                                         while (markItr3.hasNext()) {
                                             detailMarkBO = markItr3.next();
                                             final double mark3 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             totalmarkforpart3 += mark3;
                                             maxmark2 += 200.0;
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Physics")) {
                                                 electiveSubjectMark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                                 isPysicsContain=true;
                                             }
                                             if (!detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Vocational")) {
                                                 groupMarks2 += Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language1Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if (detailMarkBO.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language") && !detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("English")) {
                                                 language2Mark2 = Double.parseDouble(detailMarkBO.getConversionmark());
                                             }
                                             if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Chemistry")){
                                            	 isChemistryContain=true;
         										}
                                             if (detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Mathematics")) {
                                                 ismathsContain = true;
                                             }
                                             if(detailMarkBO.getAdmSubjectForRank().getName().equalsIgnoreCase("Computer Science")){
                                            	 isComputerContain=true;
                                             }
                                         }
                                         if (isComputerContain && ismathsContain && isChemistryContain && isPysicsContain) {
                                        	 iselig=true;
										}
                                         if (iselig) {
                                             remark = "Eligible";
                                             totalmarkforpart3 = totalmarkforpart3 / maxmark2 * 1200.0;
                                             totalmarkforpart3 = totalmarkforpart3 - penalty + bonus + electiveSubjectMark2;
                                         }
                                         else {
                                             remark = "rejected";
                                         }
                                         final StudentIndexMark si3 = new StudentIndexMark();
                                         final AdmAppln appln3 = new AdmAppln();
                                         appln3.setId(admAppln.getId());
                                         si3.setAdmAppln(appln3);
                                         final Course course3 = new Course();
                                         course3.setId(candidatePreference.getCourse().getId());
                                         si3.setCourse(course3);
                                         si3.setIndexMark(Double.valueOf(Double.parseDouble(twoDForm.format(totalmarkforpart3))));
                                         si3.setRemark(remark);
                                         si3.setCreatedDate(new Date());
                                         si3.setLastModifiedDate(new Date());
                                         si3.setCreatedBy(form.getUserId());
                                         si3.setModifiedBy(form.getUserId());
                                         si3.setLanguage1Marks(Double.valueOf(language1Mark2));
                                         si3.setLanguage2Marks(Double.valueOf(language2Mark2));
                                         si3.setGroupMarks(Double.valueOf(groupMarks2));
                                         si3.setElectivesubmark(Double.valueOf(electiveSubjectMark2));
                                         si3.setActive(true);
                                         si3.setPrefNo(candidatePreference.getPrefNo());
                                         si3.setGenerateCourseId(Integer.valueOf(Integer.parseInt(form.getCourseId())));
                                         indexmarkList.add(si3);
                                     }
                                 }
					
                       	   
							
                        	     
                        	   					  }// preference while close	
				    
					}// detailmark if close
						
				 }// doc checklist if close
	          	
				}// ednqal info info while close
				
			
				
		}// aditional info while close
	else{
					
					
					StudentIndexMark si=new StudentIndexMark();
					
					AdmAppln appln=new AdmAppln();
					appln.setId(admAppln.getId());
					si.setAdmAppln(appln);
					
					Course course=new Course();
					course.setId(admAppln.getCourse().getId());
					si.setCourse(course);
					
					si.setIndexMark(0.0);
					si.setRemark("didn't pay appln fee");
					si.setCreatedDate(new Date());
					si.setLastModifiedDate(new Date());
					si.setCreatedBy(form.getUserId());
					si.setModifiedBy(form.getUserId());

					si.setLanguage1Marks(0.0);
					si.setLanguage2Marks(0.0);
					si.setGroupMarks(0.0);
					si.setElectivesubmark(0.0);
					si.setActive(true);
					si.setPrefNo(1);
					si.setGenerateCourseId(Integer.parseInt(form.getCourseId()));
					
					indexmarkList.add(si);
				}
			 
		//}	//student check if close	
		}// main adm while close
		
		//}
			}
	}

		//}//adm appln id checking
		
		catch(Exception e)	{
			System.out.println("helper==================index mark ======================="+e);
			throw e;
		}
			return indexmarkList;
				
		
	}
	
	
	
	
	
		
	
	//allotting courses for all people based on student highest index mark priority
	public boolean generateCourseAllotmentOnIndexMark(ApplicationEditForm form)throws Exception{
		

		
		
		boolean isAdded=false;
		
		/*try{
			
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		List<Course> coursesList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
		int size=0;
		size=coursesList.size();
		
		
		//loop for checking preferences based on course size 
		
		for(int i=1;i<=size;i++){
			
			
		
		// 	getting admapl list
		List admList=txn.getAdmApplonStudentIndexMark(form);
		Iterator admIterator=admList.iterator();
		
		// loop for getting student on admappl
		while(admIterator.hasNext()){	
			AdmAppln a=null;
			Object[] object = (Object[]) admIterator.next();
			
			if(object[0]!=null){
				a=(AdmAppln)object[0];
			}
		
		
		// get student rank based in admid and preno
		StudentRank rank=txn.getRankOnAdmApplPreference(a.getId(), i);
		
		
		//check student have rank or not 
		if(rank!=null){
			
		
		Course course1=rank.getCourse();
		
		form.setCourseId(new Integer(course1.getId()).toString());
		
		int totseats=0;
		int scseat=0;
		int stseat=0;
		int bcseat=0;
		int museat=0;
		int obxseat=0;
		int obhseat=0;
		int lcseat=0;
		int genseat=0;
		int remainseats=0;
		int rankcutoff=0;
		//OBC		SC		ST		MU		OBX		OBH		LC

		int sccount=0;
		int stcount=0;
		int bccount=0;
		int mucount=0;
		int obxcount=0;
		int obhcount=0;
		int lccount=0;
		int gencount=0;
		int totcount=0;
		
		
		//for testing single course wise
		if(course1.getId()==12){
			
		
		
		//getting SeatAllocation for different addmitted through caste
		List<SeatAllocation> seatlist=txn.getSeatAllocation(form);
		
		//assign seats based on caste
		Iterator<SeatAllocation> it=seatlist.iterator();
		while(it.hasNext()){
			SeatAllocation st=it.next();
			
			if("SC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				scseat=st.getNoOfSeats();
			}else if("ST".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				stseat=st.getNoOfSeats();
			}else if("OBC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				bcseat=st.getNoOfSeats();
			}else if("General".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				genseat=st.getNoOfSeats();
			}else if("OBX".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obxseat=st.getNoOfSeats();
			}else if("OBH".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obhseat=st.getNoOfSeats();
			}else if("MU".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				museat=st.getNoOfSeats();
			}else if("LC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				lcseat=st.getNoOfSeats();
			}
			
			
		}// seat while
		
		
		
		// create new 	StudentCourseAllotment list object based on course wise
		List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
		
		//getting allotted students from StudentCourseAllotment for duplication checking
		Map<Integer,Integer> allotedmap=txn.getAllotedStudent(form);
		
		//calculate total seats allotement and rank cutoff and preference based on course
		totseats=course1.getMaxIntake();
		
		int cut=(totseats*20)/100;
		
		if(1<i){
			remainseats=totseats+(cut*i);
			rankcutoff=rankcutoff+remainseats;
		}else{
			rankcutoff=rankcutoff+totseats+remainseats;
		}
		
		
		
		form.setTotalSeats(totseats);
		form.setCutoffRank(rankcutoff);
		form.setPreNo(i);
		
		System.out.println("1st time pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
		
		
		//getting ranks based on above conditions
		List<StudentRank> rankDetails = txn.getStudentRank(form,a.getId());
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		List<StudentCourseAllotmentTo> marktolist=ApplicationRankHelper.getInstance().convertStudentRankBOToTO(rankDetails);
		
		
		// check rank list size is students there are not
			if(marktolist!=null && marktolist.size()!=0){
				
			
			
			
			
		
		
		
		//old fillup seats count based on course wise
		List generalCountList=txn.getSeatCountOnCourse("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=generalCountList.size();
		}
		
		List scCountList=txn.getSeatCountOnCourse("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=scCountList.size();
		}
		
		
		List stCountList=txn.getSeatCountOnCourse("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stCountList.size();
		}
		
		
		List obcCountList=txn.getSeatCountOnCourse("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=obcCountList.size();
		}
		
		
		List obhCountList=txn.getSeatCountOnCourse("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhCountList.size();
		}
		
		
		List obxCountList=txn.getSeatCountOnCourse("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxCountList.size();
		}
		
		
		List muCountList=txn.getSeatCountOnCourse("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=muCountList.size();
		}
		
		
		List lcCountList=txn.getSeatCountOnCourse("","LC", course1.getId());
		if(lcCountList.size()!=0){
			lccount=lcCountList.size();
		}
		
		
		System.out.println("main gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount);
		
		
		//add old count to tot count
		totcount=lccount+mucount+obxcount+obhcount+bccount+stcount+sccount+gencount;
		
		AdmApplnTO at=null;
		PersonalDataTO pt=null;
		CourseTO cto=null;
		String caste="";
	
		
		//check condition between total count and total seats count
		if(totcount<totseats) {
		
		
		//start general
		Iterator<StudentCourseAllotmentTo> i4=marktolist.iterator();
		while(i4.hasNext()){
			StudentCourseAllotmentTo allotmentTo=i4.next();
		
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
			if(gencount<genseat){
				allotmentTo.setAlloted(true);
				
				
				
				StudentCourseAllotment studentRank =new StudentCourseAllotment();
				 
				   AdmAppln appln=new AdmAppln();
				   appln.setId(allotmentTo.getAdmApplnTO().getId());
				   studentRank.setAdmAppln(appln);
				   
				   Course course=new Course();
				   course.setId(allotmentTo.getCourseTO().getId());
				   studentRank.setCourse(course);
				   
				   studentRank.setIndexMark(allotmentTo.getIndexMark());
				   studentRank.setRank(allotmentTo.getRank());
				   studentRank.setPrefNo(allotmentTo.getPrefNo());
				   studentRank.setIsAlloted(true);
				   studentRank.setActive(true);
				   studentRank.setIsAssigned(false);
				   studentRank.setIsCaste(false);
				   studentRank.setIsGeneral(true);
				   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
				   studentRank.setAllotmentNo(1);
				   
				   
				  
				   gencount++;
				   totcount++;
				   rankList.add(studentRank);
				}
				
				
			}
			
			
		}// main if
			
			
		}// general close
	
	


		
		//sc allot
		Iterator< StudentCourseAllotmentTo> iterator=marktolist.iterator();
		while(iterator.hasNext()){
			StudentCourseAllotmentTo allotmentTo=iterator.next();
			
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
				if(caste.equalsIgnoreCase("SC")){
					if(sccount<scseat){
						allotmentTo.setAlloted(true);
						
						
								
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(1);

							   
							
							   sccount++;
							   totcount++;
							   rankList.add(studentRank);
						}
						
						
						
					}
				}
				
				
				
				
			}// main if
				
				
			}//sc close
			
			
	
			
			
			
			//st allot
				Iterator<StudentCourseAllotmentTo> i1=marktolist.iterator();
				while(i1.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i1.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("ST")){
					if(stcount<stseat){
						allotmentTo.setAlloted(true);
						
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
							stcount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						
					
					}
				}
					
						
						
					}// main if
						
						
				
					
					
				}//st close
				
				
				
				
				
				
				
				
				//obc allot
				Iterator<StudentCourseAllotmentTo> i2=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i2.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBC")){
					if(bccount<bcseat){
						allotmentTo.setAlloted(true);
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						   
						   bccount++;
						   totcount++;
						   rankList.add(studentRank);
						}
						
					}
					
					}
					
					
				
					
				}// main if
					
					
					
				}	 //bc close
				
				
				
				
				
				
				//mu allot
				Iterator<StudentCourseAllotmentTo> i3=marktolist.iterator();
				while(i2.hasNext()){
					
					StudentCourseAllotmentTo allotmentTo=i3.next();
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					
					if(caste.equalsIgnoreCase("MU")){
					if(mucount<museat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
						mucount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					
					}
				}// main if
					
					
					
				}	 //mu close
				
				
				
				
				
				//obx allot
				Iterator<StudentCourseAllotmentTo> i5=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i5.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBX")){
					if(obxcount<obxseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						obxcount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}
				}// contain close	
					
					
					
				}	 //bc close
				
				
				
				
				
				
				
				//obh allot
				Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i6.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBH")){
					if(obhcount<obhseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						
						
						obhcount++;
						totcount++;
						 rankList.add(studentRank);
					}
					
					}
					
				}// main if
					
				}// contain close	
					
					
				}	 //bc close
				
				
				
				
				
				
				//lc allot
				Iterator<StudentCourseAllotmentTo> i7=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i7.next();
					
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("LC")){
					if(lccount<lcseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							studentRank.setAllotmentNo(1);
						  
						lccount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}// main if
					
					}// contain close		
					
					
				}	 //lc close
				
				
			}// totseat if close	
				
		
		
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(rankList);
		
		}// rank list size if check close
			else{ 
				
				System.out.println("already alloted pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
				
			}
			
		}// close testing course wise
		
		}// student have rank or not	
		
		}// // loop for getting student on admappl	
		
		
		
		}// preference for loop close	
		
		
		
	}// try close
		catch(Exception e){
			
			isAdded=false;
			Session session = null;
			Transaction transaction = null;
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			session.close();
			System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
			throw e;
		}finally{
			
		}
		
*/		return isAdded;
	
		
	}
	
	
	
	
	
	
	
	
	
	
	// allotting courses for all people based on preferences priority
	
	public boolean generateCourseAllotmentOnPreference(ApplicationEditForm form)throws Exception{
		
		
		boolean isAdded=false;
		/*
		try{
			
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		List<Course> coursesList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
		int size=0;
		size=coursesList.size();
		
		
		//loop for checking preferences based on course size 
		
		for(int i=1;i<=size;i++){
			
			
		
				
		//loop for alloted seats based on courses 	
		Iterator<Course> courseIterator=coursesList.iterator();
		while(courseIterator.hasNext()){
		Course course1=courseIterator.next();
		
		form.setCourseId(new Integer(course1.getId()).toString());
		
		int totseats=0;
		int scseat=0;
		int stseat=0;
		int bcseat=0;
		int museat=0;
		int obxseat=0;
		int obhseat=0;
		int lcseat=0;
		int genseat=0;
		int remainseats=0;
		int rankcutoff=0;
		//OBC		SC		ST		MU		OBX		OBH		LC

		int sccount=0;
		int stcount=0;
		int bccount=0;
		int mucount=0;
		int obxcount=0;
		int obhcount=0;
		int lccount=0;
		int gencount=0;
		int totcount=0;
		
		
		
		
		//getting SeatAllocation for different addmitted through caste
		List<SeatAllocation> seatlist=txn.getSeatAllocation(form);
		
		//assign seats based on caste
		Iterator<SeatAllocation> it=seatlist.iterator();
		while(it.hasNext()){
			SeatAllocation st=it.next();
			
			if("SC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				scseat=st.getNoOfSeats();
			}else if("ST".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				stseat=st.getNoOfSeats();
			}else if("OBC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				bcseat=st.getNoOfSeats();
			}else if("General".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				genseat=st.getNoOfSeats();
			}else if("OBX".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obxseat=st.getNoOfSeats();
			}else if("OBH".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obhseat=st.getNoOfSeats();
			}else if("MU".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				museat=st.getNoOfSeats();
			}else if("LC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				lcseat=st.getNoOfSeats();
			}
			
			
		}// seat while
		
		
		
		// create new 	StudentCourseAllotment list object based on course wise
		List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
		
		//getting allotted students from StudentCourseAllotment for duplication checking
		Map<Integer,Integer> allotedmap=txn.getAllotedStudent(form);
		
		//calculate total seats allotement and rank cutoff and preference based on course
		totseats=course1.getMaxIntake();
		
		int cut=(totseats*20)/100;
		
		if(1<i){
			remainseats=totseats+(cut*i);
			rankcutoff=rankcutoff+remainseats;
		}else{
			rankcutoff=rankcutoff+totseats+remainseats;
		}
		
		
		
		form.setTotalSeats(totseats);
		form.setCutoffRank(rankcutoff);
		form.setPreNo(i);
		
		System.out.println("1st time pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName());
		
		//getting ranks based on above conditions
		List<StudentRank> rankDetails = txn.getStudentRank(form,0);
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		List<StudentCourseAllotmentTo> marktolist=ApplicationRankHelper.getInstance().convertStudentRankBOToTO(rankDetails);
		
		
		// check rank list size is students there are not
			if(marktolist!=null && marktolist.size()!=0){
				
			
			
			
			
		
		
		
		//old fillup seats count based on course wise
		List generalCountList=txn.getSeatCountOnCourse("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=generalCountList.size();
		}
		
		List scCountList=txn.getSeatCountOnCourse("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=scCountList.size();
		}
		
		
		List stCountList=txn.getSeatCountOnCourse("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stCountList.size();
		}
		
		
		List obcCountList=txn.getSeatCountOnCourse("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=obcCountList.size();
		}
		
		
		List obhCountList=txn.getSeatCountOnCourse("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhCountList.size();
		}
		
		
		List obxCountList=txn.getSeatCountOnCourse("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxCountList.size();
		}
		
		
		List muCountList=txn.getSeatCountOnCourse("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=muCountList.size();
		}
		
		
		List lcCountList=txn.getSeatCountOnCourse("","LC", course1.getId());
		if(lcCountList.size()!=0){
			lccount=lcCountList.size();
		}
		
		System.out.println("main gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount);
		
		//add old count to tot count
		totcount=lccount+mucount+obxcount+obhcount+bccount+stcount+sccount+gencount;
		
		AdmApplnTO at=null;
		PersonalDataTO pt=null;
		CourseTO cto=null;
		String caste="";
	
		
		//check condition between total count and total seats count
		if(totcount<totseats) {
		
		
		//start general
		Iterator<StudentCourseAllotmentTo> i4=marktolist.iterator();
		while(i4.hasNext()){
			StudentCourseAllotmentTo allotmentTo=i4.next();
		
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
			if(gencount<genseat){
				allotmentTo.setAlloted(true);
				
				
				
				StudentCourseAllotment studentRank =new StudentCourseAllotment();
				 
				   AdmAppln appln=new AdmAppln();
				   appln.setId(allotmentTo.getAdmApplnTO().getId());
				   studentRank.setAdmAppln(appln);
				   
				   Course course=new Course();
				   course.setId(allotmentTo.getCourseTO().getId());
				   studentRank.setCourse(course);
				   
				   studentRank.setIndexMark(allotmentTo.getIndexMark());
				   studentRank.setRank(allotmentTo.getRank());
				   studentRank.setPrefNo(allotmentTo.getPrefNo());
				   studentRank.setIsAlloted(true);
				   studentRank.setActive(true);
				   studentRank.setIsAssigned(false);
				   studentRank.setIsCaste(false);
				   studentRank.setIsGeneral(true);
				   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
				   studentRank.setAllotmentNo(1);
				   
				   
				  
				   gencount++;
				   totcount++;
				   rankList.add(studentRank);
				}
				
				
			}
			
			
		}// main if
			
			
		}// general close
	
	


		
		
		
		
		//sc allot
		Iterator< StudentCourseAllotmentTo> iterator=marktolist.iterator();
		while(iterator.hasNext()){
			StudentCourseAllotmentTo allotmentTo=iterator.next();
			
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
				if(caste.equalsIgnoreCase("SC")){
					if(sccount<scseat){
						allotmentTo.setAlloted(true);
						
						
								
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(1);

							   
							
							   sccount++;
							   totcount++;
							   rankList.add(studentRank);
						}
						
						
						
					}
				}
				
				
				
				
			}// main if
				
				
			}//sc close
			
			
	
	
	
			
			
			
			
			//st allot
				Iterator<StudentCourseAllotmentTo> i1=marktolist.iterator();
				while(i1.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i1.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("ST")){
					if(stcount<stseat){
						allotmentTo.setAlloted(true);
						
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
							stcount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						
					
					}
				}
					
						
						
					}// main if
					
					
				}//st close
				
				
				
				
				
				
				
				
				//obc allot
				Iterator<StudentCourseAllotmentTo> i2=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i2.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBC")){
					if(bccount<bcseat){
						allotmentTo.setAlloted(true);
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						   
						   bccount++;
						   totcount++;
						   rankList.add(studentRank);
						}
						
					}
					
					}
					
					
				
					
				}// main if
					
					
				}	 //bc close
				
				
				
				
				
				
				//mu allot
				Iterator<StudentCourseAllotmentTo> i3=marktolist.iterator();
				while(i2.hasNext()){
					
					StudentCourseAllotmentTo allotmentTo=i3.next();
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					
					if(caste.equalsIgnoreCase("MU")){
					if(mucount<museat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
						mucount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					
					}
				}// main if
					
					
				}	 //mu close
				
				
				
				
				
				//obx allot
				Iterator<StudentCourseAllotmentTo> i5=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i5.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBX")){
					if(obxcount<obxseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						obxcount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}
				}// contain close	
					
					
					
				}	 //bc close
				
				
				
				
				
				
				
				//obh allot
				Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i6.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBH")){
					if(obhcount<obhseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						
						
						obhcount++;
						totcount++;
						 rankList.add(studentRank);
					}
					
					}
					
				}// main if
					
				}// contain close	
					
					
				}	 //bc close
				
				
				
				
				
				
				//lc allot
				Iterator<StudentCourseAllotmentTo> i7=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i7.next();
					
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("LC")){
					if(lccount<lcseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							studentRank.setAllotmentNo(1);
						  
						lccount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}// main if
					
					}// contain close		
					
					
				}	 //lc close
				
				
			}// totseat if close	
				
		
		
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(rankList);
		
		}// rank list size if check close
		
		}// course alloted  while loop close	
		
		
		
		}// preference for loop close	
		
		
		
	}// try close
		catch(Exception e){
			
			isAdded=false;
			Session session = null;
			Transaction transaction = null;
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			
			System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
			throw e;
		}finally{
			//session.flush();
		}
		
*/		return isAdded;
	}
	
	
	
	
	
	
	// convert bos to tos
	
	public List<StudentCourseAllotmentTo> convertStudentRankBOToTO(List<StudentRank> studentRank) {
		log.info("enter convertIncomeBOToTO" );
		List<StudentCourseAllotmentTo> courseallotlist= new ArrayList<StudentCourseAllotmentTo>();
		if(studentRank!=null){
			Iterator<StudentRank> itr= studentRank.iterator();
			while (itr.hasNext()) {
				
				StudentRank studentRank1 = (StudentRank) itr.next();
				StudentCourseAllotmentTo to= new StudentCourseAllotmentTo();
				to.setId(studentRank1.getId());
				
				AdmAppln appln=studentRank1.getAdmAppln();
				AdmApplnTO applnTO=new AdmApplnTO();
				applnTO.setId(appln.getId());
				applnTO.setApplnNo(appln.getApplnNo());
				applnTO.setAppliedYear(appln.getAppliedYear());
				
				PersonalData data=appln.getPersonalData();
				PersonalDataTO dataTO=new PersonalDataTO();
				dataTO.setId(data.getId());
				dataTO.setFirstName(data.getFirstName());
				dataTO.setGender(data.getGender());
				if(data.getCaste()!=null){
					dataTO.setCasteId(new Integer(data.getCaste().getId()).toString());
					dataTO.setCasteCategory(data.getCaste().getName());
					
				}
				if(data.getReligion()!=null){
					dataTO.setReligionId(new Integer(data.getReligion().getId()).toString());
					dataTO.setReligionName(data.getReligion().getName());
				}
				if(data.getReligionSection()!=null){
					dataTO.setSubregligionName(data.getReligionSection().getName());
					dataTO.setSubReligionId(new Integer(data.getReligionSection().getId()).toString());
				}
				dataTO.setRuralUrban(data.getRuralUrban());
				if(data.getIsSportsPerson()!=null){
				dataTO.setSportsPerson(data.getIsSportsPerson());
				}
				if(data.getIsHandicapped()!=null){
				dataTO.setHandicapped(data.getIsHandicapped());
				}
				if(data.getIsNsscertificate()!=null){
				dataTO.setNcccertificate(data.getIsNcccertificate());
				}
				if(data.getIsNsscertificate()!=null){
				dataTO.setNsscertificate(data.getIsNsscertificate());
				}
				if(data.getIsExcervice()!=null){
					dataTO.setExservice(data.getIsExcervice());
				}
				
				
				applnTO.setPersonalData(dataTO);
				
				Course c=studentRank1.getCourse();
				CourseTO courseTO=new CourseTO();
				courseTO.setId(c.getId());
				courseTO.setMaxInTake(c.getMaxIntake());
				List<SeatAllocationTO> seatlist=new ArrayList<SeatAllocationTO>();
				Set<SeatAllocation> set=c.getSeatAllocations();
				Iterator<SeatAllocation> i=set.iterator();
				
				while(i.hasNext()){
					SeatAllocation s=i.next();
					SeatAllocationTO st=new SeatAllocationTO();
					st.setId(s.getId());
					st.setNoofSeats(s.getNoOfSeats());
					st.setAdmittedThroughId(s.getAdmittedThrough().getId());
					st.setAdmittedThroughName(s.getAdmittedThrough().getName());
					seatlist.add(st);
				}
				
				courseTO.setSeatAllocation(seatlist);
				applnTO.setCourse(courseTO);
				
				to.setAdmApplnTO(applnTO);
				
				to.setAlloted(false);
				to.setRank(studentRank1.getRank());
				to.setPrefNo(studentRank1.getPrefNo());
				to.setIndexMark(studentRank1.getIndexMark());
				to.setCourseTO(courseTO);
				
				courseallotlist.add(to);
				
			}
		}
		log.info("exit convertIncomeBOToTO" );
		return courseallotlist;
	}
	
	
	
	
	
	// get student list for course assignment
	public List<StudentCourseAllotmentTo> copyBoToTO(List<StudentCourseAllotment> studentList, HashMap<Integer, Integer> existMap) {
		
		List<StudentCourseAllotmentTo> studentDetailsToList=new LinkedList<StudentCourseAllotmentTo>();
		
			Iterator<StudentCourseAllotment> it= studentList.iterator();
			while(it.hasNext()){
				
				StudentCourseAllotmentTo studentDetailsTo=new StudentCourseAllotmentTo();
				StudentCourseAllotment rank=it.next();
				
				if(existMap.containsKey(rank.getAdmAppln().getId())){
					studentDetailsTo.setTempChecked("on");
					studentDetailsTo.setChecked("on");
				}
				
				studentDetailsTo.setStudentId(String.valueOf(rank.getAdmAppln().getId()));
				studentDetailsTo.setStudentName(rank.getAdmAppln().getPersonalData().getFirstName());
				studentDetailsTo.setAlloted(rank.getIsAlloted());
				studentDetailsTo.setGroupMarks(rank.getGroupMarks());
				studentDetailsTo.setId(rank.getId());
				studentDetailsTo.setPrefNo(rank.getPrefNo());
				studentDetailsTo.setRank(rank.getRank());
				studentDetailsTo.setIndexMark(rank.getIndexMark());
				studentDetailsTo.setLanguage1Marks(rank.getLanguage1Marks());
				studentDetailsTo.setLanguage2Marks(rank.getLanguage2Marks());
				studentDetailsTo.setActive(rank.getIsActive());	
				studentDetailsTo.setRemark(rank.getRemark());
				studentDetailsTo.setIsSatisfied(rank.getIsSatisfied());
				
				CourseTO courseTO=new CourseTO();
				courseTO.setId(rank.getCourse().getId());
				courseTO.setName(rank.getCourse().getName());
				studentDetailsTo.setCourseTO(courseTO);
				
				AdmApplnTO applnTO=new AdmApplnTO();
				applnTO.setId(rank.getAdmAppln().getId());
				studentDetailsTo.setAdmApplnTO(applnTO);
				
				
				studentDetailsToList.add(studentDetailsTo);
				
			}
	
	
		// TODO Auto-generated method stub
		return studentDetailsToList;
		
	}
	
	
	
	
	//allotting courses multiple times for all people based on student highest index mark priority
	public boolean generateCourseAllotmentOnIndexMarkMultipleTimes(ApplicationEditForm form)throws Exception{
		

		
		
		boolean isAdded=false;
		
		try{
			
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		List<Course> coursesList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
		int size=0;
		size=coursesList.size();
		
		
		//loop for checking preferences based on course size 
		
		for(int i=1;i<=size;i++){
			
			
		
		// 	getting admapl list
		List admList=txn.getAdmApplonStudentIndexMarkMultipleTimes(form);
		Iterator admIterator=admList.iterator();
		
		// loop for getting student on admappl
		while(admIterator.hasNext()){	
			AdmAppln a=null;
			Object[] object = (Object[]) admIterator.next();
			
			if(object[0]!=null){
				a=(AdmAppln)object[0];
			}
		
			//check single student for testing
		//if(a.getId()==1421 || a.getId()==1422 || a.getId()==1427 || a.getId()==1206 || a.getId()==1236){
			
		
		// get student rank based in admid and preno and avoid who got already 1 pre seat
		StudentRank rank=txn.getRankOnAdmApplPreference(a.getId(), i);
		
		
		//check student have rank or not 
		if(rank!=null){
			
		
		Course course1=rank.getCourse();
		
		form.setCourseId(new Integer(course1.getId()).toString());
		
		int totseats=0;
		int scseat=0;
		int stseat=0;
		int bcseat=0;
		int museat=0;
		int obxseat=0;
		int obhseat=0;
		int lcseat=0;
		int genseat=0;
		int remainseats=0;
		int rankcutoff=0;
		//OBC		SC		ST		MU		OBX		OBH		LC

		int sccount=0;
		int stcount=0;
		int bccount=0;
		int mucount=0;
		int obxcount=0;
		int obhcount=0;
		int lccount=0;
		int gencount=0;
		int totcount=0;
		
		
		//for testing single course wise
		if(course1.getId()==12){
		
		
		//getting SeatAllocation for different addmitted through caste
		List<SeatAllocation> seatlist=txn.getSeatAllocation(form);
		
		//assign seats based on caste
		Iterator<SeatAllocation> it=seatlist.iterator();
		while(it.hasNext()){
			SeatAllocation st=it.next();
			
			if("SC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				scseat=st.getNoOfSeats();
			}else if("ST".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				stseat=st.getNoOfSeats();
			}else if("OBC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				bcseat=st.getNoOfSeats();
			}else if("General".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				genseat=st.getNoOfSeats();
			}else if("OBX".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obxseat=st.getNoOfSeats();
			}else if("OBH".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obhseat=st.getNoOfSeats();
			}else if("MU".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				museat=st.getNoOfSeats();
			}else if("LC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				lcseat=st.getNoOfSeats();
			}
			
			
		}// seat while
		
		
		
		// create new 	StudentCourseAllotment list object based on course wise
		List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
		
		//getting allotted students from StudentCourseAllotment for duplication checking
		Map<Integer,Integer> allotedmap=txn.getAllotedStudentMultipleTime(form);
		
		//calculate total seats allotement and rank cutoff and preference based on course
		totseats=course1.getMaxIntake();
		
		int cut=(totseats*20)/100;
		
		if(1<i){
			remainseats=totseats+(cut*i);
			rankcutoff=rankcutoff+remainseats;
		}else{
			rankcutoff=rankcutoff+totseats+remainseats;
		}
		
		
		
		form.setTotalSeats(totseats);
		form.setCutoffRank(rankcutoff);
		form.setPreNo(i);
		
		System.out.println("pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
		
		//getting ranks based on above conditions
		List<StudentRank> rankDetails = txn.getStudentRankMultipleTime(form,a.getId());
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		List<StudentCourseAllotmentTo> marktolist=ApplicationRankHelper.getInstance().convertStudentRankBOToTO(rankDetails);
		
		
		// check rank list size is students there are not
			if(marktolist!=null && marktolist.size()!=0){
				
			
			
			
			
		
		
		
		//old fillup seats count based on course wise multiple times
		List generalCountList=txn.getSeatCountOnCourseMultipleTime("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=generalCountList.size();
		}
		
		List scCountList=txn.getSeatCountOnCourseMultipleTime("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=scCountList.size();
		}
		
		
		List stCountList=txn.getSeatCountOnCourseMultipleTime("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stCountList.size();
		}
		
		
		List obcCountList=txn.getSeatCountOnCourseMultipleTime("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=obcCountList.size();
		}
		
		
		List obhCountList=txn.getSeatCountOnCourseMultipleTime("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhCountList.size();
		}
		
		
		List obxCountList=txn.getSeatCountOnCourseMultipleTime("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxCountList.size();
		}
		
		
		List muCountList=txn.getSeatCountOnCourseMultipleTime("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=muCountList.size();
		}
		
		
		List lcCountList=txn.getSeatCountOnCourseMultipleTime("","LC", course1.getId(),form);
		if(lcCountList.size()!=0){
			lccount=lcCountList.size();
		}
		
		
		System.out.println("pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName());
		System.out.println("1time gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount);
		
		
		//old fillup seats count based on course wise alloted no
		generalCountList=txn.getSeatCountOnCourseAllotedNo("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=gencount+generalCountList.size();
		}
		
		scCountList=txn.getSeatCountOnCourseAllotedNo("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=sccount+scCountList.size();
		}
		
		
		stCountList=txn.getSeatCountOnCourseAllotedNo("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stcount+stCountList.size();
		}
		
		
		obcCountList=txn.getSeatCountOnCourseAllotedNo("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=bccount+obcCountList.size();
		}
		
		
		obhCountList=txn.getSeatCountOnCourseAllotedNo("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhcount+obhCountList.size();
		}
		
		
		obxCountList=txn.getSeatCountOnCourseAllotedNo("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxcount+obxCountList.size();
		}
		
		
		muCountList=txn.getSeatCountOnCourseAllotedNo("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=mucount+muCountList.size();
		}
		
		
		lcCountList=txn.getSeatCountOnCourseAllotedNo("","LC", course1.getId(),form);
		if(lcCountList.size()!=0){
			lccount=lccount+lcCountList.size();
		}
		
	
		
		System.out.println("2time gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount);
		
		//add old count to tot count
		totcount=lccount+mucount+obxcount+obhcount+bccount+stcount+sccount+gencount;
		
		AdmApplnTO at=null;
		PersonalDataTO pt=null;
		CourseTO cto=null;
		String caste="";
	
		
		//check condition between total count and total seats count
		if(totcount<totseats) {
		
		
		//start general
		Iterator<StudentCourseAllotmentTo> i4=marktolist.iterator();
		while(i4.hasNext()){
			StudentCourseAllotmentTo allotmentTo=i4.next();
		
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
			if(gencount<genseat){
				allotmentTo.setAlloted(true);
				
				
				
				StudentCourseAllotment studentRank =new StudentCourseAllotment();
				 
				   AdmAppln appln=new AdmAppln();
				   appln.setId(allotmentTo.getAdmApplnTO().getId());
				   studentRank.setAdmAppln(appln);
				   
				   Course course=new Course();
				   course.setId(allotmentTo.getCourseTO().getId());
				   studentRank.setCourse(course);
				   
				   studentRank.setIndexMark(allotmentTo.getIndexMark());
				   studentRank.setRank(allotmentTo.getRank());
				   studentRank.setPrefNo(allotmentTo.getPrefNo());
				   studentRank.setIsAlloted(true);
				   studentRank.setActive(true);
				   studentRank.setIsAssigned(false);
				   studentRank.setIsCaste(false);
				   studentRank.setIsGeneral(true);
				   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
				   studentRank.setAllotmentNo(form.getAllotedNo()+1);
				   
				   
				  
				   gencount++;
				   totcount++;
				   rankList.add(studentRank);
				}
				
				
			}
			
			
		}// main if
			
			
		}// general close
	
	


		
		//sc allot
		Iterator< StudentCourseAllotmentTo> iterator=marktolist.iterator();
		while(iterator.hasNext()){
			StudentCourseAllotmentTo allotmentTo=iterator.next();
			
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
				if(caste.equalsIgnoreCase("SC")){
					if(sccount<scseat){
						allotmentTo.setAlloted(true);
						
						
								
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(form.getAllotedNo()+1);

							   
							
							   sccount++;
							   totcount++;
							   rankList.add(studentRank);
						}
						
						
						
					}
				}
				
				
				
				
			}// main if
				
				
			}//sc close
			
			
	
			
			
			
			//st allot
				Iterator<StudentCourseAllotmentTo> i1=marktolist.iterator();
				while(i1.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i1.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("ST")){
					if(stcount<stseat){
						allotmentTo.setAlloted(true);
						
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);

						   
						   
						   
							stcount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						
					
					}
				}
					
						
						
					}// main if
						
						
				
					
					
				}//st close
				
				
				
				
				
				
				
				
				//obc allot
				Iterator<StudentCourseAllotmentTo> i2=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i2.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBC")){
					if(bccount<bcseat){
						allotmentTo.setAlloted(true);
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);

						  
						   
						   bccount++;
						   totcount++;
						   rankList.add(studentRank);
						}
						
					}
					
					}
					
					
				
					
				}// main if
					
					
					
				}	 //bc close
				
				
				
				
				
				
				//mu allot
				Iterator<StudentCourseAllotmentTo> i3=marktolist.iterator();
				while(i2.hasNext()){
					
					StudentCourseAllotmentTo allotmentTo=i3.next();
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					
					if(caste.equalsIgnoreCase("MU")){
					if(mucount<museat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);

						   
						   
						   
						mucount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					
					}
				}// main if
					
					
					
				}	 //mu close
				
				
				
				
				
				//obx allot
				Iterator<StudentCourseAllotmentTo> i5=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i5.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBX")){
					if(obxcount<obxseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);

						   
						obxcount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}
				}// contain close	
					
					
					
				}	 //bc close
				
				
				
				
				
				
				
				//obh allot
				Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i6.next();
				
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBH")){
					if(obhcount<obhseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);

						  
						
						
						obhcount++;
						totcount++;
						 rankList.add(studentRank);
					}
					
					}
					
				}// main if
					
				}// contain close	
					
					
				}	 //bc close
				
				
				
				
				
				
				//lc allot
				Iterator<StudentCourseAllotmentTo> i7=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i7.next();
					
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("LC")){
					if(lccount<lcseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						 	studentRank.setAllotmentNo(form.getAllotedNo()+1);
						  
						lccount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}// main if
					
					}// contain close		
					
					
				}	 //lc close
				
				
			}// totseat if close	
				
		
		
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(rankList);
		
		}// rank list size if check close
			else{ 
				
				System.out.println("already alloted pre="+i+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
				
			}
			
		}// course wise testing
		}// student have rank or not	
		
		//}// close if single student checking
		
		}// // loop for getting student on admappl	
		
		
		
		}// preference for loop close	
		
		
		
	}// try close
		catch(Exception e){
			
			isAdded=false;
			Session session = null;
			Transaction transaction = null;
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			session.close();
			System.out.println(form.getAllotedNo()+"helper multiple===============Error occured in while alloting courses to student ================="+e);
			throw e;
		}finally{
			
		}
		
		return isAdded;
	
		
	}
	
	
	
	
	
	
	
	
	//allotting courses for all people based on Rank and Preference
	public boolean generateCourseAllotmentOnRankPreference(ApplicationEditForm form)throws Exception{
		

		
		
		boolean isAdded=false;
		
		try{
			
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		List<Course> coursesList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
		int size=0;
		size=coursesList.size();
		
		
		int mrank=txn.getMaxRank(form);
		// main loop for rank loop
		for(int srank=1;srank<=mrank;srank++){
		
		//loop for checking preferences based on course size 
		
		for(int pre=1;pre<=size;pre++){
			
			
		
		// 	getting rank list
		List<StudentRank> rankList1=txn.getAdmApplonStudentOnRankPreference(form,pre,srank);
		
		// check rank list size is empty
		if(rankList1!=null && rankList1.size()!=0){
	
		// create new 	StudentCourseAllotment list object based on course wise
		List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
			
			
		Iterator<StudentRank> rankIterator=rankList1.iterator();
		
		// loop for getting StudentRank on rank and preference
		while(rankIterator.hasNext()){	
			
			//AdmAppln a=null;
			StudentRank rank = (StudentRank) rankIterator.next();
			
			
		
		
		// get student rank based in admid and preno
		//StudentRank rank=txn.getRankOnAdmApplPreference(a.getId(), pre);
		
		
		//check student have rank or not 
		//if(rank!=null){
			
		
		Course course1=rank.getCourse();
		
		form.setCourseId(new Integer(course1.getId()).toString());
		
		int totseats=0;
		int scseat=0;
		int stseat=0;
		int bcseat=0;
		int museat=0;
		int obxseat=0;
		int obhseat=0;
		int lcseat=0;
		int genseat=0;
		int oecseat=0;
		int remainseats=0;
		int rankcutoff=0;
		//OBC		SC		ST		MU		OBX		OBH		LC

		int sccount=0;
		int stcount=0;
		int bccount=0;
		int mucount=0;
		int obxcount=0;
		int obhcount=0;
		int lccount=0;
		int oeccount=0;
		int gencount=0;
		int totcount=0;
		
		
		//for testing single course wise
		//if(course1.getId()==12){
			
		
		
		//getting SeatAllocation for different addmitted through caste
		List<SeatAllocation> seatlist=txn.getSeatAllocation(form);
		
		//assign seats based on caste
		Iterator<SeatAllocation> it=seatlist.iterator();
		while(it.hasNext()){
			SeatAllocation st=it.next();
			
			if("SC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				scseat=st.getNoOfSeats();
			}else if("ST".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				stseat=st.getNoOfSeats();
			}else if("OBC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				bcseat=st.getNoOfSeats();
			}else if("General".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				genseat=st.getNoOfSeats();
			}else if("OBX".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obxseat=st.getNoOfSeats();
			}else if("OBH".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obhseat=st.getNoOfSeats();
			}else if("MU".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				museat=st.getNoOfSeats();
			}/*else if("LC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				lcseat=st.getNoOfSeats();
			}*/
			else if("OEC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				oecseat=st.getNoOfSeats();
			}
			
		}// seat while
		
		
		
		// create new 	StudentCourseAllotment list object based on course wise
		//List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
		
		
		//calculate total seats allotement and rank cutoff and preference based on course
		totseats=course1.getMaxIntake();
		
		int cut=(totseats*20)/100;
		
		if(1<pre){
			remainseats=totseats+(cut*pre);
			rankcutoff=rankcutoff+remainseats;
		}else{
			rankcutoff=rankcutoff+totseats+remainseats;
		}
		
		
		
		form.setTotalSeats(totseats);
		form.setCutoffRank(rankcutoff);
		form.setPreNo(pre);
		
		System.out.println("1st time pre="+pre+"======course="+course1.getId()+course1.getName()+"======admid="+rank.getAdmAppln().getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
		
		
		//getting ranks based on above conditions
		//List<StudentRank> rankDetails = txn.getStudentRank(form,a.getId());
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		//List<StudentCourseAllotmentTo> marktolist=ApplicationRankHelper.getInstance().convertStudentRankBOToTO1(rankDetails);
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		StudentCourseAllotmentTo allotmentTo=ApplicationRankHelper.getInstance().convertStudentRankBOToTO1(rank);
		
		
		// check rank list size is students there are not
			//if(marktolist!=null && marktolist.size()!=0){
				
			
			
			
			
		
		
		
		//old fillup seats count based on course wise
		List generalCountList=txn.getSeatCountOnCourse("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=generalCountList.size();
		}
		
		List scCountList=txn.getSeatCountOnCourse("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=scCountList.size();
		}
		
		
		List stCountList=txn.getSeatCountOnCourse("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stCountList.size();
		}
		
		
		List obcCountList=txn.getSeatCountOnCourse("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=obcCountList.size();
		}
		
		
		List obhCountList=txn.getSeatCountOnCourse("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhCountList.size();
		}
		
		
		List obxCountList=txn.getSeatCountOnCourse("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxCountList.size();
		}
		
		
		List muCountList=txn.getSeatCountOnCourse("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=muCountList.size();
		}
		
		
		/*List lcCountList=txn.getSeatCountOnCourse("","LC", course1.getId());
		if(lcCountList.size()!=0){
			lccount=lcCountList.size();
		}
		*/
		
		List oecCountList=txn.getSeatCountOnCourse("","OEC", course1.getId(),form);
		if(oecCountList.size()!=0){
			oeccount=oecCountList.size();
		}
		
		
		//add old count to tot count
		totcount=lccount+mucount+obxcount+obhcount+bccount+stcount+sccount+gencount+oeccount;
		
		System.out.println("tot seats="+totseats+"=========filled seats="+totcount+"=========main gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount+"========oec="+oeccount);
		
		AdmApplnTO at=null;
		PersonalDataTO pt=null;
		CourseTO cto=null;
		String caste="";
	
		
		//check condition between total count and total seats count
		if(totcount<totseats) {
		
			
		
		//start general
		//Iterator<StudentCourseAllotmentTo> i4=marktolist.iterator();
		//while(i4.hasNext()){
		//	StudentCourseAllotmentTo allotmentTo=i4.next();
		
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			// testing based on caste
			//if(caste.equalsIgnoreCase("MU")){
				
			 //testing for single student
			//if(allotmentTo.getAdmApplnTO().getId()==1055){
			
			//getting allotted students from StudentCourseAllotment for duplication checking
			Map<Integer,Integer> allotedmap=txn.getAllotedStudent(form);
				
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
				System.out.println("alloted ========"+allotedmap);
			if(!allotmentTo.getIsAlloted()){
			if(gencount<genseat){
				allotmentTo.setAlloted(true);
				
				
				
				StudentCourseAllotment studentRank =new StudentCourseAllotment();
				 
				   AdmAppln appln=new AdmAppln();
				   appln.setId(allotmentTo.getAdmApplnTO().getId());
				   studentRank.setAdmAppln(appln);
				   
				   Course course=new Course();
				   course.setId(allotmentTo.getCourseTO().getId());
				   studentRank.setCourse(course);
				   
				   studentRank.setIndexMark(allotmentTo.getIndexMark());
				   studentRank.setRank(allotmentTo.getRank());
				   studentRank.setPrefNo(allotmentTo.getPrefNo());
				   studentRank.setIsAlloted(true);
				   studentRank.setActive(true);
				   studentRank.setIsAssigned(false);
				   studentRank.setIsCaste(false);
				   studentRank.setIsGeneral(true);
				   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
				   studentRank.setAllotmentNo(1);
				   
				   
				  
				   gencount++;
				   totcount++;
				   rankList.add(studentRank);
				}
				
				
			}
			
			
		//}// main if
			
			
		//}// general close
	
	


		
		//sc allot
		//Iterator< StudentCourseAllotmentTo> iterator=marktolist.iterator();
		//while(iterator.hasNext()){
			//StudentCourseAllotmentTo allotmentTo=iterator.next();
			
			//at=allotmentTo.getAdmApplnTO();
			//pt=at.getPersonalData();
			//cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			//caste=pt.getSubregligionName();
			
			
			//add seat to bo
			//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
				if(caste.equalsIgnoreCase("SC")){
					if(sccount<scseat){
						allotmentTo.setAlloted(true);
						
						
								
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(1);

							   
							
							   sccount++;
							   totcount++;
							   rankList.add(studentRank);
						}
						
						
						
					}
				}
				
				
				
				
		//	}// main if
				
				
			//}//sc close
			
			
	
			
			
			
			//st allot
				//Iterator<StudentCourseAllotmentTo> i1=marktolist.iterator();
				//while(i1.hasNext()){
					//StudentCourseAllotmentTo allotmentTo=i1.next();
				
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("ST")){
					if(stcount<stseat){
						allotmentTo.setAlloted(true);
						
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
							stcount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						
					
					}
				}
					
						
						
				//	}// main if
						
						
				
					
					
				//}//st close
				
				
				
				
				
				
				
				
				//obc allot
				//Iterator<StudentCourseAllotmentTo> i2=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i2.next();
				
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBC")){
					if(bccount<bcseat){
						allotmentTo.setAlloted(true);
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						   
						   bccount++;
						   totcount++;
						   rankList.add(studentRank);
						}
						
					}
					
					}
					
					
				
					
			//	}// main if
					
					
					
				//}	 //bc close
				
				
				
				
				
				
				//mu allot
				//Iterator<StudentCourseAllotmentTo> i3=marktolist.iterator();
				//while(i2.hasNext()){
					
				//	StudentCourseAllotmentTo allotmentTo=i3.next();
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					
					if(caste.equalsIgnoreCase("MU")){
					if(mucount<museat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						   
						   
						mucount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					
					}
			//	}// main if
					
					
					
				//}	 //mu close
				
				
				
				
				
				//obx allot
				//Iterator<StudentCourseAllotmentTo> i5=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i5.next();
				
					//at=allotmentTo.getAdmApplnTO();
					//pt=at.getPersonalData();
					//cto=allotmentTo.getCourseTO();
					
					//totseats=cto.getMaxInTake();
					//caste=pt.getSubregligionName();
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBX")){
					if(obxcount<obxseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						   
						obxcount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}
				//}// contain close	
					
					
					
				//}	 //bc close
				
				
				
				
				
				
				
				//obh allot
				//Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i6.next();
				
					//at=allotmentTo.getAdmApplnTO();
					//pt=at.getPersonalData();
					//cto=allotmentTo.getCourseTO();
					
					//totseats=cto.getMaxInTake();
					//caste=pt.getSubregligionName();
					
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBH")){
					if(obhcount<obhseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(1);

						  
						
						
						obhcount++;
						totcount++;
						 rankList.add(studentRank);
					}
					
					}
					
				//}// main if
					
				}// contain close	
					
					
				//}	 //bc close
				
				
				
				
					
					//oec allot
					//Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
					//while(i2.hasNext()){
					//	StudentCourseAllotmentTo allotmentTo=i6.next();
					
						//at=allotmentTo.getAdmApplnTO();
						//pt=at.getPersonalData();
						//cto=allotmentTo.getCourseTO();
						
						//totseats=cto.getMaxInTake();
						//caste=pt.getSubregligionName();
						
						
						//add seat to bo
					//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
						if(!allotmentTo.getIsAlloted()){
						if(caste.equalsIgnoreCase("OEC")){
						if(oeccount<oecseat){
							allotmentTo.setAlloted(true);
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(1);

							  
							
							
							oeccount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						}
						
					//}// main if
						
					}// contain close	
						
						
					//}	 //bc close
					

					
					
					
				
				
				//lc allot
				/*Iterator<StudentCourseAllotmentTo> i7=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i7.next();
					
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("LC")){
					if(lccount<lcseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							studentRank.setAllotmentNo(1);
						  
						lccount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
				//	}// main if
					
					}// contain close		
					
					
				}	 //lc close
				*/
				
			}//alloted map close	
			
			
		//}// testing student close
					
			}// totseat if close	
				
		
		
		// adding records course wise in database
		//isAdded=txn.generateCourseAllotment(rankList);
		
		//}// rank list size if check close
			//else{ 
				
			//	System.out.println("already alloted pre="+pre+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
				
			//}
			
		//}// close testing course wise
		
		//}// student have rank or not	
		
		//}// testing close caste
		
		}//  loop for getting student on admappl	
		
		
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(rankList);
		
		
		
		}// check rank list size is empty
		
		}// preference for loop close	
		
		}// main loop course
		
	}// try close
		catch(Exception e){
			
			isAdded=false;
			Session session = null;
			Transaction transaction = null;
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			session.close();
			System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
			throw e;
		}finally{
			
		}
		
		return isAdded;
	
		
	}
	
	

	
	
// convert bos to tos
	
	public StudentCourseAllotmentTo convertStudentRankBOToTO1(StudentRank studentRank1) {
		log.info("enter convertIncomeBOToTO" );
		//List<StudentCourseAllotmentTo> courseallotlist= new ArrayList<StudentCourseAllotmentTo>();
		//if(studentRank1!=null){
				StudentCourseAllotmentTo to= new StudentCourseAllotmentTo();
				to.setId(studentRank1.getId());
				
				AdmAppln appln=studentRank1.getAdmAppln();
				AdmApplnTO applnTO=new AdmApplnTO();
				applnTO.setId(appln.getId());
				applnTO.setApplnNo(appln.getApplnNo());
				applnTO.setAppliedYear(appln.getAppliedYear());
				
				PersonalData data=appln.getPersonalData();
				PersonalDataTO dataTO=new PersonalDataTO();
				dataTO.setId(data.getId());
				dataTO.setFirstName(data.getFirstName());
				dataTO.setGender(data.getGender());
				if(data.getCaste()!=null){
					dataTO.setCasteId(new Integer(data.getCaste().getId()).toString());
					dataTO.setCasteCategory(data.getCaste().getName());
					
				}
				if(data.getReligion()!=null){
					dataTO.setReligionId(new Integer(data.getReligion().getId()).toString());
					dataTO.setReligionName(data.getReligion().getName());
				}
				if(data.getReligionSection()!=null){
					dataTO.setSubregligionName(data.getReligionSection().getName());
					dataTO.setSubReligionId(new Integer(data.getReligionSection().getId()).toString());
				}
				dataTO.setRuralUrban(data.getRuralUrban());
				if(data.getIsSportsPerson()!=null){
				dataTO.setSportsPerson(data.getIsSportsPerson());
				}
				if(data.getIsHandicapped()!=null){
				dataTO.setHandicapped(data.getIsHandicapped());
				}
				if(data.getIsNsscertificate()!=null){
				dataTO.setNcccertificate(data.getIsNcccertificate());
				}
				if(data.getIsNsscertificate()!=null){
				dataTO.setNsscertificate(data.getIsNsscertificate());
				}
				if(data.getIsExcervice()!=null){
					dataTO.setExservice(data.getIsExcervice());
				}
				
				
				applnTO.setPersonalData(dataTO);
				
				Course c=studentRank1.getCourse();
				CourseTO courseTO=new CourseTO();
				courseTO.setId(c.getId());
				courseTO.setMaxInTake(c.getMaxIntake());
				List<SeatAllocationTO> seatlist=new ArrayList<SeatAllocationTO>();
				Set<SeatAllocation> set=c.getSeatAllocations();
				Iterator<SeatAllocation> i=set.iterator();
				
				while(i.hasNext()){
					SeatAllocation s=i.next();
					SeatAllocationTO st=new SeatAllocationTO();
					st.setId(s.getId());
					st.setNoofSeats(s.getNoOfSeats());
					st.setAdmittedThroughId(s.getAdmittedThrough().getId());
					st.setAdmittedThroughName(s.getAdmittedThrough().getName());
					seatlist.add(st);
				}
				
				courseTO.setSeatAllocation(seatlist);
				applnTO.setCourse(courseTO);
				
				to.setAdmApplnTO(applnTO);
				
				to.setAlloted(false);
				to.setRank(studentRank1.getRank());
				to.setPrefNo(studentRank1.getPrefNo());
				to.setIndexMark(studentRank1.getIndexMark());
				to.setCourseTO(courseTO);
				
				//courseallotlist.add(to);
				
			
		//}
		log.info("exit convertIncomeBOToTO" );
		return to;
	}
	
	
	
	
	
	//allotting courses for all people based on Rank and Preference
	public boolean generateCourseAllotmentOnRankPreferenceMultipleTime(ApplicationEditForm form)throws Exception{
		
		boolean isAdded=false;
		
		try{
			
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		List<Course> coursesList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
		int size=0;
		size=coursesList.size();
		
		int mrank=txn.getMaxRank(form);
		// main loop for rank loop
		for(int srank=1;srank<=mrank;srank++){
		
		//loop for checking preferences based on course size 
		
		for(int pre=1;pre<=size;pre++){
			
			
		
		// 	getting rank list
		List<StudentRank> rankList1=txn.getAdmApplonStudentOnRankPreferenceMultiple(form,pre,srank);
		
		// check rank list size is empty
		if(rankList1!=null && rankList1.size()!=0){
	
			
		// create new 	StudentCourseAllotment list object based on course wise
		List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
			
			
		Iterator<StudentRank> rankIterator=rankList1.iterator();
		
		// loop for getting StudentRank on rank and preference
		while(rankIterator.hasNext()){	
			
			//AdmAppln a=null;
			StudentRank rank = (StudentRank) rankIterator.next();
			
			
			// check for single student
			if(rank.getAdmAppln().getId()==1589 || rank.getAdmAppln().getId()==1591){
			}
			
		
		// get student rank based in admid and preno
		//StudentRank rank=txn.getRankOnAdmApplPreference(a.getId(), pre);
		
		
		//check student have rank or not 
		//if(rank!=null){
			
		
		Course course1=rank.getCourse();
		
		form.setCourseId(new Integer(course1.getId()).toString());
		
		int totseats=0;
		int scseat=0;
		int stseat=0;
		int bcseat=0;
		int museat=0;
		int obxseat=0;
		int obhseat=0;
		int lcseat=0;
		int genseat=0;
		int oecseat=0;
		int remainseats=0;
		int rankcutoff=0;
		//OBC		SC		ST		MU		OBX		OBH		LC   OEC

		int sccount=0;
		int stcount=0;
		int bccount=0;
		int mucount=0;
		int obxcount=0;
		int obhcount=0;
		int lccount=0;
		int oeccount=0;
		int gencount=0;
		int totcount=0;
		
		
		//for testing single course wise
		//if(course1.getId()==12){
			
		
		
		//getting SeatAllocation for different addmitted through caste
		List<SeatAllocation> seatlist=txn.getSeatAllocation(form);
		
		//assign seats based on caste
		Iterator<SeatAllocation> it=seatlist.iterator();
		while(it.hasNext()){
			SeatAllocation st=it.next();
			
			if("SC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				scseat=st.getNoOfSeats();
			}else if("ST".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				stseat=st.getNoOfSeats();
			}else if("OBC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				bcseat=st.getNoOfSeats();
			}else if("General".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				genseat=st.getNoOfSeats();
			}else if("OBX".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obxseat=st.getNoOfSeats();
			}else if("OBH".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				obhseat=st.getNoOfSeats();
			}else if("MU".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				museat=st.getNoOfSeats();
			}/*else if("LC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				lcseat=st.getNoOfSeats();
			}*/
			else if("OEC".equalsIgnoreCase(st.getAdmittedThrough().getName())){
				oecseat=st.getNoOfSeats();
			}
			
		}// seat while
		
		
		
		// create new 	StudentCourseAllotment list object based on course wise
		//List<StudentCourseAllotment> rankList= new ArrayList<StudentCourseAllotment>();
		
		
		//calculate total seats allotement and rank cutoff and preference based on course
		totseats=course1.getMaxIntake();
		
		/*int cut=(totseats*20)/100;
		
		if(1<pre){
			remainseats=totseats+(cut*pre);
			rankcutoff=rankcutoff+remainseats;
		}else{
			rankcutoff=rankcutoff+totseats+remainseats;
		}*/
		
		
		
		form.setTotalSeats(totseats);
		form.setCutoffRank(rankcutoff);
		form.setPreNo(pre);
		
		System.out.println("1st time pre="+pre+"=========course="+course1.getId()+course1.getName()+"======admid="+rank.getAdmAppln().getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
		
		
		//getting ranks based on above conditions
		//List<StudentRank> rankDetails = txn.getStudentRank(form,a.getId());
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		//List<StudentCourseAllotmentTo> marktolist=ApplicationRankHelper.getInstance().convertStudentRankBOToTO1(rankDetails);
		
		//convert properties StudentRank  to StudentCourseAllotmentTo 
		StudentCourseAllotmentTo allotmentTo=ApplicationRankHelper.getInstance().convertStudentRankBOToTO1(rank);
		
		
		// check rank list size is students there are not
			//if(marktolist!=null && marktolist.size()!=0){
				
			
			
			
			
		
		
		
		//old fillup seats count based on course wise
		List generalCountList=txn.getSeatCountOnCourseMultipleTime("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=generalCountList.size();
		}
		
		List scCountList=txn.getSeatCountOnCourseMultipleTime("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=scCountList.size();
		}
		
		
		List stCountList=txn.getSeatCountOnCourseMultipleTime("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stCountList.size();
		}
		
		
		List obcCountList=txn.getSeatCountOnCourseMultipleTime("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=obcCountList.size();
		}
		
		
		List obhCountList=txn.getSeatCountOnCourseMultipleTime("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhCountList.size();
		}
		
		
		List obxCountList=txn.getSeatCountOnCourseMultipleTime("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxCountList.size();
		}
		
		
		List muCountList=txn.getSeatCountOnCourseMultipleTime("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=muCountList.size();
		}
		
		
		/*List lcCountList=txn.getSeatCountOnCourseMultipleTime("","LC", course1.getId());
		if(lcCountList.size()!=0){
			lccount=lcCountList.size();
		}
		*/
		
		
		List oecCountList=txn.getSeatCountOnCourseMultipleTime("","OEC", course1.getId(),form);
		if(oecCountList.size()!=0){
			oeccount=oecCountList.size();
		}
		
		
		System.out.println("1time gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount+"========oec="+oeccount);
		
		
		//old fillup seats count based on course wise alloted no
		generalCountList=txn.getSeatCountOnCourseAllotedNo("General","", course1.getId(),form);
		if(generalCountList.size()!=0){
			gencount=gencount+generalCountList.size();
		}
		
		scCountList=txn.getSeatCountOnCourseAllotedNo("","SC", course1.getId(),form);
		if(scCountList.size()!=0){
			sccount=sccount+scCountList.size();
		}
		
		
		stCountList=txn.getSeatCountOnCourseAllotedNo("","ST", course1.getId(),form);
		if(stCountList.size()!=0){
			stcount=stcount+stCountList.size();
		}
		
		
		obcCountList=txn.getSeatCountOnCourseAllotedNo("","OBC", course1.getId(),form);
		if(obcCountList.size()!=0){
			bccount=bccount+obcCountList.size();
		}
		
		
		obhCountList=txn.getSeatCountOnCourseAllotedNo("","OBH", course1.getId(),form);
		if(obhCountList.size()!=0){
			obhcount=obhcount+obhCountList.size();
		}
		
		
		obxCountList=txn.getSeatCountOnCourseAllotedNo("","OBX", course1.getId(),form);
		if(obxCountList.size()!=0){
			obxcount=obxcount+obxCountList.size();
		}
		
		
		muCountList=txn.getSeatCountOnCourseAllotedNo("","MU", course1.getId(),form);
		if(muCountList.size()!=0){
			mucount=mucount+muCountList.size();
		}
		
		
		/*lcCountList=txn.getSeatCountOnCourseAllotedNo("","LC", course1.getId());
		if(lcCountList.size()!=0){
			lccount=lccount+lcCountList.size();
		}
		*/
	
		oecCountList=txn.getSeatCountOnCourseAllotedNo("","OEC", course1.getId(),form);
		if(oecCountList.size()!=0){
			oeccount=oeccount+oecCountList.size();
		}
		
		
	
		
		
		//add old count to tot count
		totcount=lccount+mucount+obxcount+obhcount+bccount+stcount+sccount+gencount+oeccount;
		
		System.out.println("2 time tot seats="+totseats+"=========filled seats="+totcount+"=========main gen="+gencount+"======sc="+sccount+"=====st="+stcount+"======obc="+bccount+"======obh="+obhcount+"=======obx="+obxcount+"========mu="+mucount+"========oec="+oeccount);
		
		
		AdmApplnTO at=null;
		PersonalDataTO pt=null;
		CourseTO cto=null;
		String caste="";
	
		
		//check condition between total count and total seats count
		if(totcount<totseats) {
		
			//getting allotted students from StudentCourseAllotment for duplication checking
			Map<Integer,Integer> allotedmap=txn.getAllotedStudentMultipleTimeOnRank(form,pre,srank);
			
			
		//start general
		//Iterator<StudentCourseAllotmentTo> i4=marktolist.iterator();
		//while(i4.hasNext()){
		//	StudentCourseAllotmentTo allotmentTo=i4.next();
		
			at=allotmentTo.getAdmApplnTO();
			pt=at.getPersonalData();
			cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			caste=pt.getSubregligionName();
			
			// testing based on caste
			//if(caste.equalsIgnoreCase("MU")){
				
			
			//add seat to bo
			if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
			if(gencount<genseat){
				allotmentTo.setAlloted(true);
				
				
				
				StudentCourseAllotment studentRank =new StudentCourseAllotment();
				 
				   AdmAppln appln=new AdmAppln();
				   appln.setId(allotmentTo.getAdmApplnTO().getId());
				   studentRank.setAdmAppln(appln);
				   
				   Course course=new Course();
				   course.setId(allotmentTo.getCourseTO().getId());
				   studentRank.setCourse(course);
				   
				   studentRank.setIndexMark(allotmentTo.getIndexMark());
				   studentRank.setRank(allotmentTo.getRank());
				   studentRank.setPrefNo(allotmentTo.getPrefNo());
				   studentRank.setIsAlloted(true);
				   studentRank.setActive(true);
				   studentRank.setIsAssigned(false);
				   studentRank.setIsCaste(false);
				   studentRank.setIsGeneral(true);
				   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
				   studentRank.setAllotmentNo(form.getAllotedNo()+1);
				   
				   
				   
				  
				   gencount++;
				   totcount++;
				   rankList.add(studentRank);
				}
				
				
			}
			
			
		//}// main if
			
			
		//}// general close
	
	


		
		//sc allot
		//Iterator< StudentCourseAllotmentTo> iterator=marktolist.iterator();
		//while(iterator.hasNext()){
			//StudentCourseAllotmentTo allotmentTo=iterator.next();
			
			//at=allotmentTo.getAdmApplnTO();
			//pt=at.getPersonalData();
			//cto=allotmentTo.getCourseTO();
			
			//totseats=cto.getMaxInTake();
			//caste=pt.getSubregligionName();
			
			
			//add seat to bo
			//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
			
			if(!allotmentTo.getIsAlloted()){
				if(caste.equalsIgnoreCase("SC")){
					if(sccount<scseat){
						allotmentTo.setAlloted(true);
						
						
								
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(form.getAllotedNo()+1);
							   

							   
							
							   sccount++;
							   totcount++;
							   rankList.add(studentRank);
						}
						
						
						
					}
				}
				
				
				
				
			//}// main if
				
				
			//}//sc close
			
			
	
			
			
			
			//st allot
				//Iterator<StudentCourseAllotmentTo> i1=marktolist.iterator();
				//while(i1.hasNext()){
					//StudentCourseAllotmentTo allotmentTo=i1.next();
				
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					//add seat to bo
					//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("ST")){
					if(stcount<stseat){
						allotmentTo.setAlloted(true);
						
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);
						   

						   
						   
						   
							stcount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						
					
					}
				}
					
						
						
					//}// main if
						
						
				
					
					
				//}//st close
				
				
				
				
				
				
				
				
				//obc allot
				//Iterator<StudentCourseAllotmentTo> i2=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i2.next();
				
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					
					//add seat to bo
					//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBC")){
					if(bccount<bcseat){
						allotmentTo.setAlloted(true);
						
							
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);
						   

						  
						   
						   bccount++;
						   totcount++;
						   rankList.add(studentRank);
						}
						
					}
					
					}
					
					
				
					
			//	}// main if
					
					
					
				//}	 //bc close
				
				
				
				
				
				
				//mu allot
				//Iterator<StudentCourseAllotmentTo> i3=marktolist.iterator();
				//while(i2.hasNext()){
					
				//	StudentCourseAllotmentTo allotmentTo=i3.next();
				//	at=allotmentTo.getAdmApplnTO();
				//	pt=at.getPersonalData();
				//	cto=allotmentTo.getCourseTO();
					
				//	totseats=cto.getMaxInTake();
				//	caste=pt.getSubregligionName();
					
					//add seat to bo
				//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					
					if(caste.equalsIgnoreCase("MU")){
					if(mucount<museat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);
						   

						   
						   
						   
						mucount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					
					}
				//}// main if
					
					
					
				//}	 //mu close
				
				
				
				
				
				//obx allot
				//Iterator<StudentCourseAllotmentTo> i5=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i5.next();
				
					//at=allotmentTo.getAdmApplnTO();
					//pt=at.getPersonalData();
					//cto=allotmentTo.getCourseTO();
					
					//totseats=cto.getMaxInTake();
					//caste=pt.getSubregligionName();
					
					//add seat to bo
					//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBX")){
					if(obxcount<obxseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);
						   

						   
						obxcount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}
			//	}// contain close	
					
					
					
				//}	 //bc close
				
				
				
				
				
				
				
				//obh allot
				//Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
				//while(i2.hasNext()){
				//	StudentCourseAllotmentTo allotmentTo=i6.next();
				
					//at=allotmentTo.getAdmApplnTO();
					//pt=at.getPersonalData();
					//cto=allotmentTo.getCourseTO();
					
					//totseats=cto.getMaxInTake();
					//caste=pt.getSubregligionName();
					
					
					//add seat to bo
					//if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("OBH")){
					if(obhcount<obhseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
						   studentRank.setAllotmentNo(form.getAllotedNo()+1);
						   

						  
						
						
						obhcount++;
						totcount++;
						 rankList.add(studentRank);
					}
					
					}
					
				//}// main if
					
				}// contain close	
					
					
				//}	 //bc close
				
				
				
					//oec allot
					//Iterator<StudentCourseAllotmentTo> i6=marktolist.iterator();
					//while(i2.hasNext()){
					//	StudentCourseAllotmentTo allotmentTo=i6.next();
					
						//at=allotmentTo.getAdmApplnTO();
						//pt=at.getPersonalData();
						//cto=allotmentTo.getCourseTO();
						
						//totseats=cto.getMaxInTake();
						//caste=pt.getSubregligionName();
						
						
						//add seat to bo
					//	if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
						if(!allotmentTo.getIsAlloted()){
						if(caste.equalsIgnoreCase("OEC")){
						if(oeccount<oecseat){
							allotmentTo.setAlloted(true);
							StudentCourseAllotment studentRank =new StudentCourseAllotment();
							 
							   AdmAppln appln=new AdmAppln();
							   appln.setId(allotmentTo.getAdmApplnTO().getId());
							   studentRank.setAdmAppln(appln);
							   
							   Course course=new Course();
							   course.setId(allotmentTo.getCourseTO().getId());
							   studentRank.setCourse(course);
							   
							   studentRank.setIndexMark(allotmentTo.getIndexMark());
							   studentRank.setRank(allotmentTo.getRank());
							   studentRank.setPrefNo(allotmentTo.getPrefNo());
							   studentRank.setIsAlloted(true);
							   studentRank.setActive(true);
							   studentRank.setIsAssigned(false);
							   studentRank.setIsCaste(true);
							   studentRank.setIsGeneral(false);
							   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							   studentRank.setAllotmentNo(1);

							  
							
							
							oeccount++;
							totcount++;
							rankList.add(studentRank);
						}
						
						}
						
					//}// main if
						
					}// contain close	
						
						
					//}	 //bc close
					


					
					
					
				
				
				//lc allot
				/*Iterator<StudentCourseAllotmentTo> i7=marktolist.iterator();
				while(i2.hasNext()){
					StudentCourseAllotmentTo allotmentTo=i7.next();
					
					at=allotmentTo.getAdmApplnTO();
					pt=at.getPersonalData();
					cto=allotmentTo.getCourseTO();
					
					totseats=cto.getMaxInTake();
					caste=pt.getSubregligionName();
					
					//add seat to bo
					if(!allotedmap.containsKey(allotmentTo.getAdmApplnTO().getId())){
					
					if(!allotmentTo.getIsAlloted()){
					if(caste.equalsIgnoreCase("LC")){
					if(lccount<lcseat){
						allotmentTo.setAlloted(true);
						StudentCourseAllotment studentRank =new StudentCourseAllotment();
						 
						
						   AdmAppln appln=new AdmAppln();
						   appln.setId(allotmentTo.getAdmApplnTO().getId());
						   studentRank.setAdmAppln(appln);
						   
						   Course course=new Course();
						   course.setId(allotmentTo.getCourseTO().getId());
						   studentRank.setCourse(course);
						   
						   studentRank.setIndexMark(allotmentTo.getIndexMark());
						   studentRank.setRank(allotmentTo.getRank());
						   studentRank.setPrefNo(allotmentTo.getPrefNo());
						   studentRank.setIsAlloted(true);
						   studentRank.setActive(true);
						   studentRank.setIsAssigned(false);
						   studentRank.setIsCaste(true);
						   studentRank.setIsGeneral(false);
						   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
							studentRank.setAllotmentNo(form.getAllotedNo()+1);
				   
						  
						lccount++;
						totcount++;
						rankList.add(studentRank);
					}
					
					}
					
					}// main if
					
					}// contain close		
					
					
				}	 //lc close
				*/
				
			}	// allotmap teseting close
				
		} 	// totseat if close
		
		// adding records course wise in database
		//isAdded=txn.generateCourseAllotment(rankList);
		
		//}// rank list size if check close
			//else{ 
				
			//	System.out.println("already alloted pre="+pre+"======remining seats="+remainseats+"=====cut of="+rankcutoff+"======course="+course1.getId()+course1.getName()+"======admid="+a.getId()+"=======rank="+rank.getRank()+"========index="+rank.getIndexMark());
				
			//}
			
		//}// close testing course wise
		
		//}// student have rank or not	
		
		//}// testing based on caste
		
		}//  loop for getting student on admappl	
		
		
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(rankList);
		
		
		
		}// check rank list size is empty
		
		}// preference for loop close	
		
		}// main loop course
		
	}// try close
		catch(Exception e){
			
			isAdded=false;
			Session session = null;
			Transaction transaction = null;
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
			//query.setString("current_date1",date);
			query1.executeUpdate();
			transaction.commit();
			session.close();
			System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
			throw e;
		}finally{
			
		}
		
		return isAdded;
	
		

	}
	
	

	// get student list for course assignment
	public List<StudentCourseAllotmentTo> copyBoToTO1(List<CandidatePreference> studentList, HashMap<Integer, Integer> existMap) {
		
		List<StudentCourseAllotmentTo> studentDetailsToList=new LinkedList<StudentCourseAllotmentTo>();
		
			Iterator<CandidatePreference> it= studentList.iterator();
			while(it.hasNext()){
				
				StudentCourseAllotmentTo studentDetailsTo=new StudentCourseAllotmentTo();
				CandidatePreference rank=it.next();
				
				if(existMap.containsKey(rank.getAdmAppln().getId())){
					studentDetailsTo.setTempChecked("on");
					studentDetailsTo.setChecked("on");
				}
				
				studentDetailsTo.setStudentId(String.valueOf(rank.getAdmAppln().getId()));
				studentDetailsTo.setStudentName(rank.getAdmAppln().getPersonalData().getFirstName());
				studentDetailsTo.setId(rank.getId());
				studentDetailsTo.setPrefNo(rank.getPrefNo());
				
				CourseTO courseTO=new CourseTO();
				courseTO.setId(rank.getCourse().getId());
				courseTO.setName(rank.getCourse().getName());
				studentDetailsTo.setCourseTO(courseTO);
				
				AdmApplnTO applnTO=new AdmApplnTO();
				applnTO.setId(rank.getAdmAppln().getId());
				studentDetailsTo.setAdmApplnTO(applnTO);
				
				studentDetailsTo.setApplicationNumber(String.valueOf(rank.getAdmAppln().getApplnNo()));
				
				
				studentDetailsToList.add(studentDetailsTo);
				
			}
	
	
		// TODO Auto-generated method stub
		return studentDetailsToList;
		
	}
	
	
	
	// get student list for course assignment
	public List<CandidatePreferenceEntranceDetailsTO> copyBoToTO2(List<CandidatePreferenceEntranceDetails> studentList) {
		
		List<CandidatePreferenceEntranceDetailsTO> studentDetailsToList=new LinkedList<CandidatePreferenceEntranceDetailsTO>();
		
			Iterator<CandidatePreferenceEntranceDetails> it= studentList.iterator();
			while(it.hasNext()){
				
				CandidatePreferenceEntranceDetailsTO studentDetailsTo=new CandidatePreferenceEntranceDetailsTO();
				CandidatePreferenceEntranceDetails rank=it.next();
				
				studentDetailsTo.setStudentName(rank.getAdmAppln().getPersonalData().getFirstName());
				studentDetailsTo.setId(rank.getId());
				studentDetailsTo.setPrefNo(rank.getPrefNo());
				if(rank.getMarksObtained()!=null)
				studentDetailsTo.setMarksObtained(rank.getMarksObtained().toString());
				if(rank.getTotalMarks()!=null)
				studentDetailsTo.setTotalMarks(rank.getTotalMarks().toString());
				CourseTO courseTO=new CourseTO();
				courseTO.setId(rank.getCourse().getId());
				courseTO.setName(rank.getCourse().getName());
				studentDetailsTo.setCourseTO(courseTO);
				
				AdmApplnTO applnTO=new AdmApplnTO();
				applnTO.setId(rank.getAdmAppln().getId());
				applnTO.setApplnNo(rank.getAdmAppln().getApplnNo());
				studentDetailsTo.setAdmApplnTO(applnTO);
				
				
				studentDetailsToList.add(studentDetailsTo);
				
			}
	
	
		// TODO Auto-generated method stub
		return studentDetailsToList;
		
	}
	


		
	
	//For mark
	public List<StudentCommonRank> calculateGroupMarks(List<EdnQualification> ednList,ApplicationEditForm form)throws Exception
	{
		
		
		Set<AdmSubjectMarkForRank> SubmarkList=new HashSet<AdmSubjectMarkForRank>();
		Iterator<EdnQualification> iterator = ednList.iterator();
		List<StudentCommonRank> admList=new ArrayList<StudentCommonRank>();
		while (iterator.hasNext()) {
			
			double totalmarks=0;
			double groupmarks=0;
			double totalmaxmarks=200;
			double groupmaxmarks=200;
			double percentage=0;
			double grouppercentage=0;
			int admid=0;
			int courseid=0;
			
			EdnQualification ednQualificationBO = iterator.next();
			
			SubmarkList=ednQualificationBO.getAdmSubjectMarkForRank();
			Set<AdmAppln> aset=ednQualificationBO.getPersonalData().getAdmApplns();
			Iterator<AdmSubjectMarkForRank> itr=SubmarkList.iterator();
			Iterator<AdmAppln> itr1=aset.iterator();
				
			while(itr1.hasNext())
			{
				AdmAppln a=itr1.next();
				admid=a.getId();
			 // courseid=a.getCourse().getId();
			}
		
			while(itr.hasNext())
			 {
				AdmSubjectMarkForRank admSubjectMarkForRank=(AdmSubjectMarkForRank) itr.next();
		
	    	if(admSubjectMarkForRank.getConversionmark()!=null && !admSubjectMarkForRank.getConversionmark().equalsIgnoreCase(""))
	    	{
	    		totalmarks=totalmarks+Double.parseDouble(admSubjectMarkForRank.getConversionmark());
	    		totalmaxmarks=totalmaxmarks+200;
	    	}
	    	if(!admSubjectMarkForRank.getAdmSubjectForRank().getGroupName().equalsIgnoreCase("Language"))
	    	{
	    		groupmarks=groupmarks+Double.parseDouble(admSubjectMarkForRank.getConversionmark());
	    		groupmaxmarks=groupmaxmarks+200;
	    	}
	    	
	    	}
	    
	    percentage=(totalmarks/totalmaxmarks)*100;
	    grouppercentage=(groupmarks/groupmaxmarks)*100;
	    StudentCommonRank studentCommonRank=new StudentCommonRank();
    	studentCommonRank.setGroupMarks(groupmarks);
    	studentCommonRank.setTotalMark(totalmarks);
    	studentCommonRank.setGroupPercentage(grouppercentage);
    	studentCommonRank.setCreatedBy(form.getUserId());
    	studentCommonRank.setCreatedDate(new Date());
    	studentCommonRank.setPercentage(percentage);
    	
    	AdmAppln admAppln=new AdmAppln();
    	admAppln.setId(admid);
    	studentCommonRank.setAdmAppln(admAppln);
    	
    	Course c=new Course();
    	c.setId(Integer.parseInt(form.getCourseId()));
    	studentCommonRank.setCourse(c);
    	
    	admList.add(studentCommonRank);
    	
	    
	}
		return admList;

	}
	
	
	
        //starting
		//allotting courses for all people based on Rank and Course
		/**
		 * @param form
		 * @return
		 * @throws Exception
		 */
		public boolean generateCourseAllotmentOnCourseRank(ApplicationEditForm form)throws Exception{
		
		boolean isAdded=false;
		boolean isStop=true;
			
		try
		{	
			
		
			
		//set allotment no	
		form.setAllotedNo(1);	
		
		// create new 	allotmentMap with student id and StudentCourseAllotment object for storing db
		Map<Integer,StudentCourseAllotment> allotmentMap=new HashMap<Integer,StudentCourseAllotment>();
		Map<Integer,CourseTO> courseMap=new HashMap<Integer,CourseTO>();
		Map<Integer,List<StudentRank>> genRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteSCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteSTRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteBCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteOBXRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteOBHRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteMURankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteLCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteCOMMUNITYRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> sebc=new HashMap<Integer,List<StudentRank>>();
		
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
		//get all courses
		List<Course> courseList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
	
		int courseSize=0;
		courseSize=courseList.size();
	
		//convert course bos to tos list
		List<CourseTO> courseToList=ApplicationRankHelper.getInstance().copyCourseBoTO(courseList);
		//put general details of course into map
		Iterator<CourseTO> courseIterator1=courseToList.iterator();
		while(courseIterator1.hasNext())
		{
		CourseTO courseTO=courseIterator1.next();
		
		if (courseTO.getId()==27) {
			System.out.println("maths");
		}
		//getting rank list on Course and SEBC rank
				List<StudentRank> rankListOnCourseSEBC=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SEBC");
				if(rankListOnCourseSEBC.size()!=0)
				{
					//setting genmap as key course id , value gen rank list
					sebc.put(courseTO.getId(), rankListOnCourseSEBC);
					int genSize=rankListOnCourseSEBC.size();
					StudentRank sr=rankListOnCourseSEBC.get(genSize-1);
					courseTO.setHighsebcrank(sr.getRank());	
				}
				else
				{
					courseTO.setHighsebcrank(0);
				}
		
		//getting rank list on Course and General rank
		List<StudentRank> rankListOnCourseGeneral=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"GENERAL");
		if(rankListOnCourseGeneral.size()!=0)
		{
			//setting genmap as key course id , value gen rank list
			genRankMap.put(courseTO.getId(), rankListOnCourseGeneral);
			int genSize=rankListOnCourseGeneral.size();
			StudentRank sr=rankListOnCourseGeneral.get(genSize-1);
			courseTO.setHighgenrank(sr.getRank());	
		}
		else
		{
			courseTO.setHighgenrank(0);
		}
		
		//getting rank list on Course and SC caste rank
		List<StudentRank> rankListOnCourseCasteSC=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SC");

		if(rankListOnCourseCasteSC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteSCRankMap.put(courseTO.getId(), rankListOnCourseCasteSC);
			 int casteSize=rankListOnCourseCasteSC.size();
			 StudentRank sr=rankListOnCourseCasteSC.get(casteSize-1);
			 courseTO.setHighscrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighscrank(0);
		}
		
		
		//getting rank list on Course and ST caste rank
		List<StudentRank> rankListOnCourseCasteST=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"ST");

		if(rankListOnCourseCasteST.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteSTRankMap.put(courseTO.getId(), rankListOnCourseCasteST);
			 int casteSize=rankListOnCourseCasteST.size();
			 StudentRank sr=rankListOnCourseCasteST.get(casteSize-1);
			 courseTO.setHighstrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighstrank(0);
		}

		
				
		
		//getting rank list on Course and BC caste rank BC mens EZHAVA,THIYYA,BILLAVA
		List<StudentRank> rankListOnCourseCasteBC=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"BC");

		if(rankListOnCourseCasteBC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteBCRankMap.put(courseTO.getId(), rankListOnCourseCasteBC);
			 int casteSize=rankListOnCourseCasteBC.size();
			 StudentRank sr=rankListOnCourseCasteBC.get(casteSize-1);
			 courseTO.setHighbcrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighbcrank(0);
		}

		//getting rank list on Course and MU caste rank
		List<StudentRank> rankListOnCourseCasteMU=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"MU");

		if(rankListOnCourseCasteMU.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteMURankMap.put(courseTO.getId(), rankListOnCourseCasteMU);
			 int casteSize=rankListOnCourseCasteMU.size();
			 StudentRank sr=rankListOnCourseCasteMU.get(casteSize-1);
			 courseTO.setHighmurank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighmurank(0);
		}

				
		//getting rank list on Course and OBH caste rank
		List<StudentRank> rankListOnCourseCasteOBH=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBH");

		if(rankListOnCourseCasteOBH.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteOBHRankMap.put(courseTO.getId(), rankListOnCourseCasteOBH);
			 int casteSize=rankListOnCourseCasteOBH.size();
			 StudentRank sr=rankListOnCourseCasteOBH.get(casteSize-1);
			 courseTO.setHighobhrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighobhrank(0);
		}

		//getting rank list on Course and OBX caste rank
		List<StudentRank> rankListOnCourseCasteOBX=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBX");

		if(rankListOnCourseCasteOBX.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteOBXRankMap.put(courseTO.getId(), rankListOnCourseCasteOBX);
			 int casteSize=rankListOnCourseCasteOBX.size();
			 StudentRank sr=rankListOnCourseCasteOBX.get(casteSize-1);
			 courseTO.setHighobxrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighobxrank(0);
		}

		
		
		//getting rank list on Course and LC  and LCTA caste rank
		List<StudentRank> rankListOnCourseCasteLC=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"LC");

		if(rankListOnCourseCasteLC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteLCRankMap.put(courseTO.getId(), rankListOnCourseCasteLC);
			int casteSize=rankListOnCourseCasteLC.size();
			StudentRank sr=rankListOnCourseCasteLC.get(casteSize-1);
			courseTO.setHighlcrank(sr.getRank());	
		}
		else
		{
			courseTO.setHighlcrank(0);				
		}

				
		//getting rank list on Course amd community rank
		List<StudentRank> rankListOnCourseCasteCOMMUNITY=txn.getAdmApplonStudentOnCourseCategory(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"COMMUNITY");

		if(rankListOnCourseCasteCOMMUNITY.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteCOMMUNITYRankMap.put(courseTO.getId(), rankListOnCourseCasteCOMMUNITY);
			int casteSize=rankListOnCourseCasteCOMMUNITY.size();
			StudentRank sr=rankListOnCourseCasteCOMMUNITY.get(casteSize-1);
			courseTO.setHighcommunityrank(sr.getRank());	
		}
		else
		{
			courseTO.setHighcommunityrank(0);					
		}
		 


		
		
		courseTO.setGencurrank(0);
		courseTO.setSccurrank(0);
		courseTO.setStcurrank(0);
		courseTO.setBccurrank(0);
		courseTO.setObhcurrank(0);
		courseTO.setObxcurrank(0);
		courseTO.setLccurrank(0);
		courseTO.setMucurrank(0);
		courseTO.setLctacurrank(0);
		courseTO.setCommunitycurrank(0);
		courseTO.setSebccurrank(0);
		
		courseMap.put(courseTO.getId(), courseTO);
		}
		//close course iterator map
		
		
		//start with infinite loop upto fill all seats 
		while(isStop)
		{
		
		Iterator<CourseTO> courseIterator=courseToList.iterator();	
		// start first course loop for fill caste seats
		while(courseIterator.hasNext())
		{
			
		CourseTO courseTO=courseIterator.next();
		
		if (courseTO.getId()==30) {
			System.out.println("maths");
		}
		// check Course and caste SC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteSC=casteSCRankMap.get(courseTO.getId());
		 
		// check sc setas zero or not, if zero stop course
		if(courseTO.getScremain()==null || courseTO.getScremain()==0){
			//course over
			courseTO.setScover(true);
			courseTO.setScremain(0);
			courseTO.setScseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
 
		else if(rankListOnCourseCasteSC!=null && rankListOnCourseCasteSC.size()!=0)
		{	 
			// add sc allotment list and map
			addAllotmentlist(rankListOnCourseCasteSC, "SC",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setScover(true);
			courseTO.setScremain(0);
			courseTO.setScseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// SC caste allot over
		
		
		// check Course and caste ST rank list size is empty
		 List<StudentRank> rankListOnCourseCasteST=casteSTRankMap.get(courseTO.getId());
		 
		// check st setas zero or not, if zero stop course
		if(courseTO.getStremain()==null || courseTO.getStremain()==0){
			//course over
			courseTO.setStover(true);
			courseTO.setStremain(0);
			courseTO.setStseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
 
		else if(rankListOnCourseCasteST!=null && rankListOnCourseCasteST.size()!=0)
		{	 
			// add st allotment list and map
			addAllotmentlist(rankListOnCourseCasteST, "ST",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setStover(true);
			courseTO.setStremain(0);
			courseTO.setStseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// ST caste allot over
		
		
		
		
		
		
		// check Course and caste BC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteBC=casteBCRankMap.get(courseTO.getId());
		 
		
		// check bc setas zero or not, if zero stop course
		if(courseTO.getBcremain()==null || courseTO.getBcremain()==0){
			//course over
			courseTO.setBcover(true);
			courseTO.setBcremain(0);
			courseTO.setBcseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteBC!=null && rankListOnCourseCasteBC.size()!=0)
		{	 
			// add bc allotment list and map
			addAllotmentlist(rankListOnCourseCasteBC, "BC",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setBcover(true);
			courseTO.setBcremain(0);
			courseTO.setBcseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// BC caste allot over
		
		
		
		
		// check Course and caste MU rank list size is empty
		 List<StudentRank> rankListOnCourseCasteMU=casteMURankMap.get(courseTO.getId());
		 
		// check mu setas zero or not, if zero stop course
		if(courseTO.getMuremain()==null || courseTO.getMuremain()==0){
			//course over
			courseTO.setMuover(true);
			courseTO.setMuremain(0);
			courseTO.setMuseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteMU!=null && rankListOnCourseCasteMU.size()!=0)
		{	 
			// add mu allotment list and map
			addAllotmentlist(rankListOnCourseCasteMU, "MU",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setMuover(true);
			courseTO.setMuremain(0);
			courseTO.setMuseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// MU caste allot over
		
		
		// check Course and caste OBH rank list size is empty
		 List<StudentRank> rankListOnCourseCasteOBH=casteOBHRankMap.get(courseTO.getId());
		 
		// check obh setas zero or not, if zero stop course
		if(courseTO.getObhremain()==null || courseTO.getObhremain()==0){
			//course over
			courseTO.setObhover(true);
			courseTO.setObhremain(0);
			courseTO.setObhseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteOBH!=null && rankListOnCourseCasteOBH.size()!=0)
		{	 
			// add obh allotment list and map
			addAllotmentlist(rankListOnCourseCasteOBH, "OBH",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setObhover(true);
			courseTO.setObhremain(0);
			courseTO.setObhseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// OBH caste allot over
		
		
		// check Course and caste OBX rank list size is empty
		 List<StudentRank> rankListOnCourseCasteOBX=casteOBXRankMap.get(courseTO.getId());
		// check obx setas zero or not, if zero stop course
		if(courseTO.getObxremain()==null || courseTO.getObxremain()==0){
			//course over
			courseTO.setObxover(true);
			courseTO.setObxremain(0);
			courseTO.setObxseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteOBX!=null && rankListOnCourseCasteOBX.size()!=0)
		{	 
		// add obx allotment list and map
		addAllotmentlist(rankListOnCourseCasteOBX, "OBX",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setObxover(true);
			courseTO.setObxremain(0);
			courseTO.setObxseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// OBX caste allot over
		
		
		// check Course and caste LC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteLC=casteLCRankMap.get(courseTO.getId());
		 
		 // check lc setas zero or not, if zero stop course
		if(courseTO.getLcremain()==null || courseTO.getLcremain()==0){
			//course over
			courseTO.setLcover(true);
			courseTO.setLcremain(0);
			courseTO.setLcseat(0);
			
			
			
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		else if(rankListOnCourseCasteLC!=null && rankListOnCourseCasteLC.size()!=0)
		{	 
			// add lc allotment list and map
			addAllotmentlist(rankListOnCourseCasteLC, "LC",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setLcover(true);
			courseTO.setLcremain(0);
			courseTO.setLcseat(0);
			
		
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// LC caste allot over
		
		
		// check Course and caste LC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteCOMMUNITY=casteCOMMUNITYRankMap.get(courseTO.getId());
		 
		 // check lc setas zero or not, if zero stop course
		if(courseTO.getCommunityremain()==null || courseTO.getCommunityremain()==0){
			//course over
			courseTO.setCommunityover(true);
			courseTO.setCommunityremain(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		else if(rankListOnCourseCasteCOMMUNITY!=null && rankListOnCourseCasteCOMMUNITY.size()!=0)
		{	 
			// add lc allotment list and map
			addAllotmentlist(rankListOnCourseCasteCOMMUNITY, "COMMUNITY",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setCommunityover(true);
			courseTO.setCommunityremain(0);
			
		
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// community caste allot over
		
		// check SEBC
				 List<StudentRank> rankListOnCourseCasteSEBC=sebc.get(courseTO.getId());
				 
				 // check lc setas zero or not, if zero stop course
				if(courseTO.getSebcremain()==null || courseTO.getSebcremain()==0){
					//course over
					courseTO.setSebcover(true);
					courseTO.setSebcremain(0);
					
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
				}
				else if(rankListOnCourseCasteSEBC!=null && rankListOnCourseCasteSEBC.size()!=0)
				{	 
					// add lc allotment list and map
					addAllotmentlist(rankListOnCourseCasteSEBC, "SEBC",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);
				}// caste ranklist size close
				else
				{
					//course over
					courseTO.setSebcover(true);
					courseTO.setSebcremain(0);
					
				
					
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
				}
		 
		
		
		
		}// first caste course loop close
	
	
	
		Iterator<CourseTO> courseIterator2=courseToList.iterator();
		// start second course loop for fill general seats
		while(courseIterator2.hasNext())
		{
		CourseTO courseTO=courseIterator2.next();
		
		//getting rank list on Course and General rank
		List<StudentRank> rankListOnCourseGeneral=genRankMap.get(courseTO.getId());// check Course and caste rank list size is empty
		// check st setas zero or not, if zero stop course
		if(courseTO.getGenremain()==null || courseTO.getGenremain()==0){
			//course over
			courseTO.setGenover(true);
			courseTO.setGenremain(0);
			courseTO.setGenseat(0);
				
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseGeneral!=null && rankListOnCourseGeneral.size()!=0)
		{
			// add general allotment list and map
			addAllotmentlist(rankListOnCourseGeneral, "General",allotmentMap,courseMap,courseTO,form.getAllotedNo(),form);			
		}// general ranklist size close
		else
		{
			//course over
			courseTO.setGenover(true);
			courseTO.setGenremain(0);
			courseTO.setGenseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
	
		
		}
		//second general course loop close
	
		// condition to check all seats fill are not
		int i=0;
		Set<Entry<Integer, CourseTO>> courseSet= courseMap.entrySet();
		Iterator<Entry<Integer, CourseTO>> icourse=courseSet.iterator();
		while(icourse.hasNext())
		{
		Entry<Integer, CourseTO> e=icourse.next();
		CourseTO courseTO=e.getValue();
		
	    if(courseTO.isGenover() && courseTO.isScover() && courseTO.isStover() && courseTO.isBcover() && courseTO.isObhover() && courseTO.isObxover() && courseTO.isMuover() && courseTO.isLcover() && courseTO.isCommunityover() )
		{
			i++;
			System.out.println(courseTO.getName()+"=======  over   ========="+i);
		}
				
		 }
		
		
		
	
	
		// course size course over equals stop infinte loop
		if(courseSize==i)
		{
		isStop=false;
		}
	
		}// infinite loop close
	
	
	
	
		//getting values from allotmap and add values to list   
		List<StudentCourseAllotment> allotmentList=new ArrayList<StudentCourseAllotment>();
		Set<Entry<Integer, StudentCourseAllotment>> studentCourseAllotmentSet= allotmentMap.entrySet();
	
		Iterator<Entry<Integer, StudentCourseAllotment>> i=studentCourseAllotmentSet.iterator();
		while(i.hasNext())
		{
		Entry<Integer, StudentCourseAllotment> e=i.next();
		StudentCourseAllotment allotment=e.getValue();
		allotmentList.add(allotment);
		}
	
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(allotmentList);
		
		// stroring allotment data to another table
		List<StudentCourseAllotmentPrev> prevAllotmentList = new ArrayList<StudentCourseAllotmentPrev>();
		Iterator<StudentCourseAllotment> altr = allotmentList.iterator();
		while(altr.hasNext()){
			StudentCourseAllotment allotmentbo = altr.next();
			StudentCourseAllotmentPrev prevAllotmentbo = new StudentCourseAllotmentPrev();
			
			prevAllotmentbo.setActive(allotmentbo.getIsActive());
			prevAllotmentbo.setAdmAppln(allotmentbo.getAdmAppln());
			prevAllotmentbo.setAllotmentNo(allotmentbo.getAllotmentNo());
			prevAllotmentbo.setCourse(allotmentbo.getCourse());
			prevAllotmentbo.setCreatedBy(allotmentbo.getCreatedBy());
			prevAllotmentbo.setCreatedDate(allotmentbo.getCreatedDate());
			
			prevAllotmentbo.setIndexMark(allotmentbo.getIndexMark());
			prevAllotmentbo.setIsAlloted(allotmentbo.getIsAlloted());
			prevAllotmentbo.setIsAssigned(allotmentbo.getIsAssigned());
			prevAllotmentbo.setIsCancel(allotmentbo.getIsCancel());
			prevAllotmentbo.setIsCaste(allotmentbo.getIsCaste());
			prevAllotmentbo.setIsGeneral(allotmentbo.getIsGeneral());
			prevAllotmentbo.setIsCommunity(allotmentbo.getIsCommunity());
			prevAllotmentbo.setIsSatisfied(allotmentbo.getIsSatisfied());
			
			if(allotmentbo.getLastModifiedDate()!=null){
				prevAllotmentbo.setLastModifiedDate(allotmentbo.getLastModifiedDate());
			}
			if(allotmentbo.getModifiedBy()!=null){
				prevAllotmentbo.setModifiedBy(allotmentbo.getModifiedBy());
			}
			
			prevAllotmentbo.setPrefNo(allotmentbo.getPrefNo());
			prevAllotmentbo.setRank(allotmentbo.getRank());
			
			
			prevAllotmentList.add(prevAllotmentbo);
			
			
		}
		txn.savePrevCourseAllotment(prevAllotmentList);
		
	
		}// try close	
		catch(Exception e)
		{
		isAdded=false;
		/*Session session = null;
		Transaction transaction = null;
		session = HibernateUtil.getSession();
		transaction = session.beginTransaction();
		transaction.begin();
		Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
		//query.setString("current_date1",date);
		query1.executeUpdate();
		transaction.commit();
		session.close();
		System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
		*/
		throw e;
		}
		finally
		{
		
		}
	
		return isAdded;

	
		}

	
	
	
		
	
	
		// add students category wise to allotment list and map
		/**
		 * @param rankList
		 * @param category
		 * @param genrankList
		 * @param allotmentMap
		 */
		public void addAllotmentlist(List<StudentRank> rankList,String category,Map<Integer,StudentCourseAllotment> allotmentMap,Map<Integer,CourseTO> courseMap,CourseTO itrcourseTO,Integer allotmentNo,ApplicationEditForm form ) 
		{
		
		CourseTO courseTO=courseMap.get(itrcourseTO.getId());
		System.out.println();
		System.out.println("==========================  "+ courseTO.getName() + " ============================="+category);
		System.out.println("before gencurrent=="+courseTO.getGencurrank());
		System.out.println("before sccurrent=="+courseTO.getSccurrank());
		System.out.println("before stcurrent=="+courseTO.getStcurrank());
		System.out.println("before bccurrent=="+courseTO.getBccurrank());
		System.out.println("before mucurrent=="+courseTO.getMucurrank());
		System.out.println("before obhcurrent=="+courseTO.getObhcurrank());
		System.out.println("before obxcurrent=="+courseTO.getObxcurrank());
		System.out.println("before communitycurrent=="+courseTO.getCommunitycurrank());
		
		System.out.println("before genremain=="+courseTO.getGenremain());
		System.out.println("before scremain=="+courseTO.getScremain());
		System.out.println("before stremain=="+courseTO.getStremain());
		System.out.println("before bcremain=="+courseTO.getBcremain());
		System.out.println("before muremain=="+courseTO.getMuremain());
		System.out.println("before obhremain=="+courseTO.getObhremain());
		System.out.println("before obxremain=="+courseTO.getObxremain());
		System.out.println("before communityremain=="+courseTO.getCommunityremain());
		
		System.out.println("before higen=="+courseTO.getHighgenrank());
		System.out.println("before higsc=="+courseTO.getHighscrank());
		System.out.println("before hist=="+courseTO.getHighstrank());
		System.out.println("before higbc=="+courseTO.getHighbcrank());
		System.out.println("before himu=="+courseTO.getHighmurank());
		System.out.println("before higobh=="+courseTO.getHighobhrank());
		System.out.println("before higobx=="+courseTO.getHighobxrank());
		System.out.println("before higcommunity=="+courseTO.getHighcommunityrank());


			 System.out.println("before lccurrent=="+courseTO.getLccurrank());
				
			 System.out.println("before lcremain=="+courseTO.getLcremain());
				
			 System.out.println("before higlc=="+courseTO.getHighlcrank());
		
		 
		System.out.println("==========================  "+ category+ " =============================");
		
		
		
		Properties prop = new Properties();
		try 
		{
			InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
			prop.load(in);
		} 
		catch (FileNotFoundException e)
		{
			log.error("Unable to read properties file...", e);

		} 
		catch (IOException e)
		{
			log.error("Unable to read properties file...", e);

		}
		
		//sc
		Integer scId=0;	
		String sc = prop.getProperty ("knowledgepro.admission.religionsection.sc");
		if(sc !=null && ! StringUtils.isEmpty(sc)){ 
			scId=Integer.parseInt(sc);
		}
		
		//ezhava_thiyya_billava
		Integer ezhava_thiyya_billava_Id=0;	
		String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
		if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
			ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
		}
		
		//mu
		Integer muId=0;	
		String mu = prop.getProperty ("knowledgepro.admission.religionsection.mu");
		if(mu !=null && ! StringUtils.isEmpty(mu)){ 
			muId=Integer.parseInt(mu);
		}
		
		//st
		Integer stId=0;	
		String st = prop.getProperty ("knowledgepro.admission.religionsection.st");
		if(st !=null && ! StringUtils.isEmpty(st)){ 
			stId=Integer.parseInt(st);
		}
		
		//lc
		Integer lcId=0;	
		String lc = prop.getProperty ("knowledgepro.admission.religionsection.lc");
		if(lc !=null && ! StringUtils.isEmpty(lc)){ 
			lcId=Integer.parseInt(lc);
		}
		
		//lcta
		Integer lctaId=0;	
		String lcta = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
		if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
			lctaId=Integer.parseInt(lcta);
		}
		
		//obh
		Integer obhId=0;	
		String obh = prop.getProperty ("knowledgepro.admission.religionsection.obh");
		if(obh !=null && ! StringUtils.isEmpty(obh)){ 
			obhId=Integer.parseInt(obh);
		}
		
		//obx
		Integer obxId=0;	
		String obx = prop.getProperty ("knowledgepro.admission.religionsection.obx");
		if(obx !=null && ! StringUtils.isEmpty(obx)){ 
			obxId=Integer.parseInt(obx);
		}
		
		//ezhava
		Integer ezhavaId=0;	
		String ezhava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
		if(ezhava !=null && ! StringUtils.isEmpty(ezhava)){ 
			ezhavaId=Integer.parseInt(ezhava);
		}
		

		//billava
		Integer billavaId=0;	
		String billava = prop.getProperty ("knowledgepro.admission.religionsection.billava");
		if(billava !=null && ! StringUtils.isEmpty(billava)){ 
			billavaId=Integer.parseInt(billava);
		}
		
		//thiyya
		Integer thiyyaId=0;	
		String thiyya = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
		if(thiyya !=null && ! StringUtils.isEmpty(thiyya)){ 
			thiyyaId=Integer.parseInt(thiyya);
		}
		
		//community
		Integer communityId=0;	
		String community = prop.getProperty ("knowledgepro.admission.religionsection.community");
		if(community !=null && ! StringUtils.isEmpty(community)){ 
			communityId=Integer.parseInt(community);
		}

		
		
		
		
		
		
		Iterator<StudentRank> rankIterator=rankList.iterator();
		// loop for getting StudentRank on rank and preference
		while(rankIterator.hasNext()){	
			
			StudentRank currank = (StudentRank) rankIterator.next();
			
			
			
			
			// general check start
			if(category.equalsIgnoreCase("General"))
			{
				// check student rank is more than gencurcommon rank
			   if(currank.getRank() > courseTO.getGencurrank())
			   {
				   // check gencurcommon rank less than highest gen rank and gen seats not equal zero
				   if (courseTO.getGencurrank() < courseTO.getHighgenrank() && courseTO.getGenremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setGencurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() >= currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
									//check map contain student seat is caste or not and add caste seat
									if(allotment.getIsCaste())
									{										
										//update old course of caste seats
										updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);										
									}
									//check map contain student seat is gen or not	and add genremain seat
									else if(allotment.getIsGeneral())
									{
										updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
									}
									else if(allotment.getIsCommunity()) {
										updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									}
								allotment.setIsGeneral(true);
								allotment.setIsCaste(false);
								allotment.setIsCommunity(false);
									
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								 // update--course generaldetails
								courseTO.setGencurrank( currank.getRank());
								
								courseTO.setGenremain(courseTO.getGenremain()-1);
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsGeneral()+" modified data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
								
						}
						//add new student if map does contains
						else
						{
							
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"GENERAL",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							   
							   // update--course general details
							   courseTO.setGencurrank( currank.getRank());
							   courseTO.setGenremain(courseTO.getGenremain()-1);
						   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
						  	 System.out.println("fresh data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
							
						}				  							
					}//main if over
			   }				  
			}// general check over
			
		
			
			
			
			
			// caste sc check start
			
			//SC start
			else if(category.equalsIgnoreCase("SC"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==scId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getSccurrank()){
				 
				// check gencurcommon rank less than highest sc caste rank and sc caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighscrank() && courseTO.getScremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setSccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste scremain seat
								if(allotment.getIsCaste())
								{
									
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
									/*oldCourseTO.setScremain(oldCourseTO.getScremain()+1);
									oldCourseTO.setScover(false);
									System.out.println(oldCourseTO.getName()+"old--------caste in add casterem=="+oldCourseTO.getScremain());
									//update course map
									courseMap.put(oldCourseTO.getId(), oldCourseTO);
									*/
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setScremain(courseTO.getScremain()-1);
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								// update-- course general details
								courseTO.setSccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setScremain(courseTO.getScremain()-1);
							   courseTO.setSccurrank( currank.getRank());
								
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			// SC over
			
			
			
			
			//ST start
			else if(category.equalsIgnoreCase("ST"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==stId )		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getStcurrank()){
				 
				// check gencurcommon rank less than highest st caste rank and st caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighstrank() && courseTO.getStremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setStcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Stremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setStremain(courseTO.getStremain()-1);
								courseTO.setStcurrank( currank.getRank());
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setStremain(courseTO.getStremain()-1);
							   courseTO.setStcurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//ST Over
			
			
			
			
			//BC start
			else if(category.equalsIgnoreCase("BC"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==ezhavaId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==billavaId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==thiyyaId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getBccurrank()){
				 
				// check gencurcommon rank less than highest Bc caste rank and Bc caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighbcrank() && courseTO.getBcremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setBccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Bcremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setBcremain(courseTO.getBcremain()-1);
								courseTO.setBccurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setBcremain(courseTO.getBcremain()-1);
							   courseTO.setBccurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//BC Over
			
			
			//MU srart
			else if(category.equalsIgnoreCase("MU"))
			{	
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==muId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getMucurrank()){
				 
				// check gencurcommon rank less than highest Mu caste rank and Mu caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighmurank() && courseTO.getMuremain() != 0)
					{
					 
					 
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setMucurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Muremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setMuremain(courseTO.getMuremain()-1);
								courseTO.setMucurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setMuremain(courseTO.getMuremain()-1);
							   courseTO.setMucurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//MU Over
			
			
			//OBH start
			else if(category.equalsIgnoreCase("OBH"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==obhId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getObhcurrank()){
				 
				// check gencurcommon rank less than highest Obh caste rank and Obh caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighobhrank() && courseTO.getObhremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setObhcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Obhremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setObhremain(courseTO.getObhremain()-1);
								courseTO.setObhcurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setObhremain(courseTO.getObhremain()-1);
							   courseTO.setObhcurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//OBH Over
			
			
			//OBX
			else if(category.equalsIgnoreCase("OBX"))
			{
				
			//OBX	
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==obxId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getObxcurrank()){
				 
				// check gencurcommon rank less than highest Obx caste rank and Obx caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighobxrank() && courseTO.getObxremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo() < currank.getPrefNo() )
							{
								// update-- course general details
								courseTO.setObxcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo() > currank.getPrefNo() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Obxremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false);
								allotment.setIsCommunity(false);
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setObxremain(courseTO.getObxremain()-1);
								courseTO.setObxcurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
						}
						//add new student if map does contains
						else
						{
							
						   //get allotment object seat to store db
						   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
							
						   //genrankList.add(studentRank);
						   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
						  
						   // update-- course general details
						   courseTO.setObxremain(courseTO.getObxremain()-1);
						   courseTO.setObxcurrank( currank.getRank());
						   
						   //update course map
						   courseMap.put(courseTO.getId(), courseTO);
						   
						   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//OBX Over
			
				 //LC ,LCTAstart
				 if(category.equalsIgnoreCase("LC"))
					{	
					if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==lcId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==lctaId)		
					{
					// check student rank is more than gencurcommon rank
					 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getLccurrank()){
						 
						// check gencurcommon rank less than highest lc caste rank and lc caste seats not equal zero
						 if(courseTO.getGencurrank() < courseTO.getHighlcrank() && courseTO.getLcremain() != 0)
							{
								// check map contains student or not
								if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
								{
									StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
									
									// check map contain student pre less than with student rank pre
									if(allotment.getPrefNo() < currank.getPrefNo() )
									{
										// update-- course general details
										courseTO.setLccurrank( currank.getRank());
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
									}
									
									// check map contain student pre is greater more than equal with student rank pre
									if(allotment.getPrefNo() > currank.getPrefNo() )
									{
										//update old course general details
										CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
										//check map contain student seat is caste or not and add caste Lcremain seat
										if(allotment.getIsCaste())
										{
											//update old course of caste seats
											updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
											
										}
										//check map contain student seat is gen or not and add genremain seat
										else if(allotment.getIsGeneral())
										{
											updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
										}
												
										allotment.setIsCaste(true);
										allotment.setIsGeneral(false);
										allotment.setIsCommunity(false);
										// update--map details
										allotment.setPrefNo(currank.getPrefNo());
										allotment.setRank(currank.getRank());
										allotment.setIndexMark(currank.getIndexMark());
										
										Course course=new Course();
										course.setId(courseTO.getId());
										allotment.setCourse(course);
										
										courseTO.setLcremain(courseTO.getLcremain()-1);
										courseTO.setLccurrank( currank.getRank());
										
										//update allotment map
										allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
										
										System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
										
									}	
								}
								//add new student if map does contains
								else
								{
									  //get allotment object seat to store db
									   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
										
									   //genrankList.add(studentRank);
									   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
									  
									   // update-- course general details
									   courseTO.setLcremain(courseTO.getLcremain()-1);
									   courseTO.setLccurrank( currank.getRank());
									   
									   //update course map
									   courseMap.put(courseTO.getId(), courseTO);
								   
									   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								}	
							}// main if over 
					 	}	  
					}
					
					}
					//LC Over
				 
				 
				 
				 
				 //COMMUNITY start
				 if(category.equalsIgnoreCase("COMMUNITY"))
					{	
					if(currank.getAdmAppln().getPersonalData().getIsCommunity())		
					{
					// check student rank is more than gencurcommon rank
					 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getCommunitycurrank()){
						 
						// check gencurcommon rank less than highest lc caste rank and lc caste seats not equal zero
						 if(courseTO.getGencurrank() < courseTO.getHighcommunityrank() && courseTO.getCommunityremain() != 0)
							{
								// check map contains student or not
								if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
								{
									StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
									
									// check map contain student pre less than with student rank pre
									if(allotment.getPrefNo() < currank.getPrefNo() )
									{
										// update-- course general details
										courseTO.setCommunitycurrank( currank.getRank());
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
									}
									
									// check map contain student pre is greater more than equal with student rank pre
									if(allotment.getPrefNo() > currank.getPrefNo() )
									{
										//update old course general details
										CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
										//check map contain student seat is caste or not and add caste Lcremain seat
										if(allotment.getIsCaste())
										{
											//update old course of caste seats
											updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
											
										}
										//check map contain student seat is gen or not and add genremain seat
										else if(allotment.getIsGeneral())
										{
											updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
										}
										
										allotment.setIsCommunity(true);
										allotment.setIsCaste(false);
										allotment.setIsGeneral(false);
										
										// update--map details
										allotment.setPrefNo(currank.getPrefNo());
										allotment.setRank(currank.getRank());
										allotment.setIndexMark(currank.getIndexMark());
										
										Course course=new Course();
										course.setId(courseTO.getId());
										allotment.setCourse(course);
										
										courseTO.setCommunityremain(courseTO.getCommunityremain()-1);
										courseTO.setCommunitycurrank( currank.getRank());
										
										//update allotment map
										allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
										
										System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
										
									}	
								}
								//add new student if map does contains
								else
								{
									  //get allotment object seat to store db
									   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"COMMUNITY",allotmentNo,form);
										
									   //genrankList.add(studentRank);
									   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
									  
									   // update-- course general details
									   courseTO.setCommunityremain(courseTO.getCommunityremain()-1);
									   courseTO.setCommunitycurrank(currank.getRank());
									   
									   //update course map
									   courseMap.put(courseTO.getId(), courseTO);
								   
									   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								}	
							}// main if over 
					 	}	  
					}
					
					}
					//COMMUNITY Over
				 
				//SEBC start
				 if(category.equalsIgnoreCase("SEBC"))
					{	
					if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==9 || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==9)		
					{
					// check student rank is more than gencurcommon rank
					 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getSebccurrank()){
						 
						// check gencurcommon rank less than highest lc caste rank and lc caste seats not equal zero
						 if(courseTO.getGencurrank() < courseTO.getHighsebcrank() && courseTO.getSebcremain() != 0)
							{
								// check map contains student or not
								if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
								{
									StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
									
									// check map contain student pre less than with student rank pre
									if(allotment.getPrefNo() < currank.getPrefNo() )
									{
										// update-- course general details
										courseTO.setLccurrank( currank.getRank());
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
									}
									
									// check map contain student pre is greater more than equal with student rank pre
									if(allotment.getPrefNo() > currank.getPrefNo() )
									{
										//update old course general details
										CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
										//check map contain student seat is caste or not and add caste Lcremain seat
										if(allotment.getIsCaste())
										{
											//update old course of caste seats
											updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
											
										}
										//check map contain student seat is gen or not and add genremain seat
										else if(allotment.getIsGeneral())
										{
											updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
										}
												
										allotment.setIsCaste(true);
										allotment.setIsGeneral(false);
										allotment.setIsCommunity(false);
										// update--map details
										allotment.setPrefNo(currank.getPrefNo());
										allotment.setRank(currank.getRank());
										allotment.setIndexMark(currank.getIndexMark());
										
										Course course=new Course();
										course.setId(courseTO.getId());
										allotment.setCourse(course);
										
										courseTO.setSebcremain(courseTO.getSebcremain()-1);
										courseTO.setSebccurrank( currank.getRank());
										
										//update allotment map
										allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
										
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
										
										System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
										
									}	
								}
								//add new student if map does contains
								else
								{
									  //get allotment object seat to store db
									   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
										
									   //genrankList.add(studentRank);
									   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
									  
									   // update-- course general details
									   courseTO.setSebcremain(courseTO.getSebcremain()-1);
										courseTO.setSebccurrank( currank.getRank());
									   
									   //update course map
									   courseMap.put(courseTO.getId(), courseTO);
								   
									   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								}	
							}// main if over 
					 	}	  
					}
					
					}
					

			 
			
			
		
			
			// caste check over
					


				
			
		}// student rank loop close
		
		
		
		///after
		System.out.println("after gencurrent=="+courseTO.getGencurrank());
		System.out.println("after sccurrent=="+courseTO.getSccurrank());
		System.out.println("after stcurrent=="+courseTO.getStcurrank());
		System.out.println("after bccurrent=="+courseTO.getBccurrank());
		System.out.println("after lccurrent=="+courseTO.getLccurrank());
		System.out.println("after mucurrent=="+courseTO.getMucurrank());
		System.out.println("after obhcurrent=="+courseTO.getObhcurrank());
		System.out.println("after obxcurrent=="+courseTO.getObxcurrank());
		System.out.println("after communitycurrent=="+courseTO.getCommunitycurrank());
		
		System.out.println("after genremain=="+courseTO.getGenremain());
		System.out.println("after scremain=="+courseTO.getScremain());
		System.out.println("after stremain=="+courseTO.getStremain());
		System.out.println("after bcremain=="+courseTO.getBcremain());
		System.out.println("after muremain=="+courseTO.getMuremain());
		System.out.println("after lcremain=="+courseTO.getLcremain());
		System.out.println("after obhremain=="+courseTO.getObhremain());
		System.out.println("after obxremain=="+courseTO.getObxremain());
		System.out.println("after communityremain=="+courseTO.getCommunityremain());
		
		System.out.println("after higen=="+courseTO.getHighgenrank());
		System.out.println("after higsc=="+courseTO.getHighscrank());
		System.out.println("after hist=="+courseTO.getHighstrank());
		System.out.println("after higbc=="+courseTO.getHighbcrank());
		System.out.println("after himu=="+courseTO.getHighmurank());
		System.out.println("after higlc=="+courseTO.getHighlcrank());
		System.out.println("after higobh=="+courseTO.getHighobhrank());
		System.out.println("after higobx=="+courseTO.getHighobxrank());
		System.out.println("after higcommunity=="+courseTO.getHighcommunityrank());
	    System.out.println("after lccurrent=="+courseTO.getLccurrank());
		System.out.println("after lcremain=="+courseTO.getLcremain());
		System.out.println("after higlc=="+courseTO.getHighlcrank());

		System.out.println("=========================="+ category+ " =============================");
		
		
		//checking all caste and gen over conditions
		
		//general over
		 if (courseTO.getGencurrank() >= courseTO.getHighgenrank() || courseTO.getGenremain() == 0)
		 {
				courseTO.setGenover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //sc over
		 if(courseTO.getGencurrank() >= courseTO.getHighscrank() || courseTO.getScremain() == 0 || courseTO.getSccurrank() >= courseTO.getHighscrank())
		 {
				courseTO.setScover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //st over
		 if(courseTO.getGencurrank() >= courseTO.getHighstrank() || courseTO.getStremain() == 0 || courseTO.getStcurrank() >= courseTO.getHighstrank())
		 {
				courseTO.setStover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		
		 //bc over
		 if(courseTO.getGencurrank() >= courseTO.getHighbcrank() || courseTO.getBcremain() == 0 || courseTO.getBccurrank() >= courseTO.getHighbcrank())
		 {
				courseTO.setBcover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		 //mu over
		 if(courseTO.getGencurrank() >= courseTO.getHighmurank() || courseTO.getMuremain() == 0 || courseTO.getMucurrank() >= courseTO.getHighmurank())
		 {
				courseTO.setMuover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		 //obh over
		 if(courseTO.getGencurrank() >= courseTO.getHighobhrank() || courseTO.getObhremain() == 0 || courseTO.getObhcurrank() >= courseTO.getHighobhrank())
		 {
				courseTO.setObhover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //obx over
		 if(courseTO.getGencurrank() >= courseTO.getHighobxrank() || courseTO.getObxremain() == 0 || courseTO.getObxcurrank() >= courseTO.getHighobxrank() )
		 {
				courseTO.setObxover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		
	 	 //lc over
			 if(courseTO.getGencurrank() >= courseTO.getHighlcrank() || courseTO.getLcremain() == 0 || courseTO.getLccurrank() >= courseTO.getHighlcrank())
			 {
					courseTO.setLcover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 }
			 
			 //lc over
			 if(courseTO.getGencurrank() >= courseTO.getHighcommunityrank() || courseTO.getCommunityremain() == 0 || courseTO.getCommunitycurrank() >= courseTO.getHighcommunityrank())
			 {
					courseTO.setCommunityover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 }
			
			 if(courseTO.getGencurrank() >= courseTO.getHighsebcrank() || courseTO.getSebcremain() == 0 || courseTO.getSebccurrank() >= courseTO.getHighsebcrank())
			 {
					courseTO.setScover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 }
			
			 
		 }
		
		
		//end method
		
	
	
		
		/**
		 * @param allotment
		 * @param courseMap
		 * @param oldCourseTO
		 */
		public void updateCourseMapOnOldCourse(StudentCourseAllotment allotment,Map<Integer,CourseTO> courseMap,CourseTO oldCourseTO) 
		{
			
			
			
			Properties prop = new Properties();
			try 
			{
				InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
				prop.load(in);
			} 
			catch (FileNotFoundException e)
			{
				log.error("Unable to read properties file...", e);

			} 
			catch (IOException e)
			{
				log.error("Unable to read properties file...", e);

			}
			
			//sc
			Integer scId=0;	
			String sc = prop.getProperty ("knowledgepro.admission.religionsection.sc");
			if(sc !=null && ! StringUtils.isEmpty(sc)){ 
				scId=Integer.parseInt(sc);
			}
			
			//ezhava_thiyya_billava
			Integer ezhava_thiyya_billava_Id=0;	
			String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
			if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
				ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
			}
			
			//mu
			Integer muId=0;	
			String mu = prop.getProperty ("knowledgepro.admission.religionsection.mu");
			if(mu !=null && ! StringUtils.isEmpty(mu)){ 
				muId=Integer.parseInt(mu);
			}
			
			//st
			Integer stId=0;	
			String st = prop.getProperty ("knowledgepro.admission.religionsection.st");
			if(st !=null && ! StringUtils.isEmpty(st)){ 
				stId=Integer.parseInt(st);
			}
			
			//lc
			Integer lcId=0;	
			String lc = prop.getProperty ("knowledgepro.admission.religionsection.lc");
			if(lc !=null && ! StringUtils.isEmpty(lc)){ 
				lcId=Integer.parseInt(lc);
			}
			
			//lcta
			Integer lctaId=0;	
			String lcta = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
			if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
				lctaId=Integer.parseInt(lcta);
			}
			
			//obh
			Integer obhId=0;	
			String obh = prop.getProperty ("knowledgepro.admission.religionsection.obh");
			if(obh !=null && ! StringUtils.isEmpty(obh)){ 
				obhId=Integer.parseInt(obh);
			}
			
			//obx
			Integer obxId=0;	
			String obx = prop.getProperty ("knowledgepro.admission.religionsection.obx");
			if(obx !=null && ! StringUtils.isEmpty(obx)){ 
				obxId=Integer.parseInt(obx);
			}
			
			//ezhava
			Integer ezhavaId=0;	
			String ezhava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
			if(ezhava !=null && ! StringUtils.isEmpty(ezhava)){ 
				ezhavaId=Integer.parseInt(ezhava);
			}
			

			//billava
			Integer billavaId=0;	
			String billava = prop.getProperty ("knowledgepro.admission.religionsection.billava");
			if(billava !=null && ! StringUtils.isEmpty(billava)){ 
				billavaId=Integer.parseInt(billava);
			}
			
			//thiyya
			Integer thiyyaId=0;	
			String thiyya = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
			if(thiyya !=null && ! StringUtils.isEmpty(thiyya)){ 
				thiyyaId=Integer.parseInt(thiyya);
			}
			
			
			//community
			Integer communityId=0;	
			String community = prop.getProperty ("knowledgepro.admission.religionsection.community");
			if(community !=null && ! StringUtils.isEmpty(community)){ 
				communityId=Integer.parseInt(community);
			}

			
			
			
			
			// add SC Remain Seat
			if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==scId)
			{
				oldCourseTO.setScremain(oldCourseTO.getScremain()+1);
				oldCourseTO.setScover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			// add ST Remain Seat
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==stId)
			{
				oldCourseTO.setStremain(oldCourseTO.getStremain()+1);
				oldCourseTO.setStover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			
			// add BC Remain Seat BC mens EZHAVA,THIYYA,BILLAVA
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==ezhavaId || allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==billavaId || allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==thiyyaId)
			{
				oldCourseTO.setBcremain(oldCourseTO.getBcremain()+1);
				oldCourseTO.setBcover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			// add MU Remain Seat
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==muId)
			{
				oldCourseTO.setMuremain(oldCourseTO.getMuremain()+1);
				oldCourseTO.setMuover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			// add OBH Remain Seat
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==obhId)
			{
				oldCourseTO.setObhremain(oldCourseTO.getObhremain()+1);
				oldCourseTO.setObhover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			// add OBX Remain Seat
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==obxId)
			{
				oldCourseTO.setObxremain(oldCourseTO.getObxremain()+1);
				oldCourseTO.setObxover(false);
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
				
			}
			
				// add LC ,LCTA Remain Seat
			else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==lcId || allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==lctaId)
			{
				 oldCourseTO.setLcremain(oldCourseTO.getLcremain()+1);
				 oldCourseTO.setLcover(false);
						
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
						
			}
					
			// add community Remain Seat
		if(allotment.getAdmAppln().getPersonalData().getIsCommunity())
			{
				oldCourseTO.setCommunityremain(oldCourseTO.getCommunityremain()+1);
				oldCourseTO.setCommunityover(false);
						
				//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
						
			}
		else if(allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==9 || allotment.getAdmAppln().getPersonalData().getReligionSection().getId()==9)
		{
			 oldCourseTO.setSebcremain(oldCourseTO.getSebcremain()+1);
			 oldCourseTO.setLcover(false);
					
			//System.out.println(oldCourseTO.getName()+"old--------general in add casterem=="+oldCourseTO.getScremain());
					
		}		 
					 
			 
			
			
			
			//update course map
			courseMap.put(oldCourseTO.getId(), oldCourseTO);
			
		
			
			
		}
		
		
		public void updateCourseMapOnOldCourseGeneral(Map<Integer,CourseTO> courseMap,CourseTO oldCourseTO) 
		{
			oldCourseTO.setGenremain(oldCourseTO.getGenremain()+1);
			oldCourseTO.setGenover(false);
			//System.out.println(oldCourseTO.getName()+"old----------caste in add generalrem=="+oldCourseTO.getGenremain());
			//update course map
			courseMap.put(oldCourseTO.getId(), oldCourseTO);
			
		}
		

		//get StudentCourseAllotment object
		/**
		 * @param currank
		 * @param category
		 * @return
		 */
		public StudentCourseAllotment getStudetCourseAllotmentBO(StudentRank currank,String category,Integer allotmentNo,ApplicationEditForm form) 
		{
			
			
		   //create object seat to store db
		   StudentCourseAllotment studentRank =new StudentCourseAllotment();
		 
		   //AdmAppln appln=new AdmAppln();
		   //appln.setId(currank.getAdmAppln().getId());
		   studentRank.setAdmAppln(currank.getAdmAppln());
		   
		   //Course course=new Course();
		  // course.setId(currank.getCourse().getId());
		   studentRank.setCourse(currank.getCourse());
		   
		   studentRank.setIndexMark(currank.getIndexMark());
		   studentRank.setRank(currank.getRank());
		   studentRank.setPrefNo(currank.getPrefNo());
		   studentRank.setIsAlloted(true);
		   studentRank.setActive(true);
		   studentRank.setIsAssigned(false);
		   
		   if(category.equalsIgnoreCase("GENERAL"))
		   {
			   studentRank.setIsGeneral(true);
			   studentRank.setIsCaste(false);			   
			   studentRank.setIsCommunity(false);
		   }
		   
		   else if(category.equalsIgnoreCase("CASTE"))
		   {
			   studentRank.setIsCaste(true);
			   studentRank.setIsGeneral(false);
			   studentRank.setIsCommunity(false);
		   }
		   /**
		    * Added by Arun Sudhakaran
		    * Earlier is_caste was the common column for both caste as well as community, so inorder to avoid ambiguity column for community was added
		    */
		   else if(category.equalsIgnoreCase("COMMUNITY")) {
			   
			   studentRank.setIsCommunity(true);
			   studentRank.setIsCaste(false);
			   studentRank.setIsGeneral(false);			   
		   }
		   
		   studentRank.setCreatedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
		   studentRank.setCreatedBy(form.getUserId());
		   studentRank.setAllotmentNo(allotmentNo);
		
			return studentRank;
		}
	

	
	
		// get general and caste seats based on course and convert course bo to to

		/**
		 * @param courseList
		 * @return
		 */
		public List<CourseTO> copyCourseBoTO(List<Course> courseList) 
		{
			Properties prop = new Properties();
			try 
			{
				InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
				prop.load(in);
			} 
			catch (FileNotFoundException e)
			{
				log.error("Unable to read properties file...", e);

			} 
			catch (IOException e)
			{
				log.error("Unable to read properties file...", e);

			}

		List<CourseTO> courseToList=new LinkedList<CourseTO>();
		Iterator<Course> it= courseList.iterator();
		while(it.hasNext())
		{
			
						
			//sc
			Integer scId=0;	
			String sc = prop.getProperty ("knowledgepro.admission.admittedthrough.sc");
			if(sc !=null && ! StringUtils.isEmpty(sc)){ 
				scId=Integer.parseInt(sc);
			}
			
			//ezhava_thiyya_billava
			Integer ezhava_thiyya_billava_Id=0;	
			String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.ezhava_thiyya_billava");
			if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
				ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
			}
			
			//mu
			Integer muId=0;	
			String mu = prop.getProperty ("knowledgepro.admission.admittedthrough.mu");
			if(mu !=null && ! StringUtils.isEmpty(mu)){ 
				muId=Integer.parseInt(mu);
			}
			
			//st
			Integer stId=0;	
			String st = prop.getProperty ("knowledgepro.admission.admittedthrough.st");
			if(st !=null && ! StringUtils.isEmpty(st)){ 
				stId=Integer.parseInt(st);
			}
			
			
			//obh
			Integer obhId=0;	
			String obh = prop.getProperty ("knowledgepro.admission.admittedthrough.obh");
			if(obh !=null && ! StringUtils.isEmpty(obh)){ 
				obhId=Integer.parseInt(obh);
			}
			
			//obx
			Integer obxId=0;	
			String obx = prop.getProperty ("knowledgepro.admission.admittedthrough.obx");
			if(obx !=null && ! StringUtils.isEmpty(obx)){ 
				obxId=Integer.parseInt(obx);
			}
			
			//lc
			Integer lcId=0;	
			String lc = prop.getProperty ("knowledgepro.admission.admittedthrough.lc");
			if(lc !=null && ! StringUtils.isEmpty(lc)){ 
				lcId=Integer.parseInt(lc);
			}
			
			//lcta
			Integer lctaId=0;	
			String lcta = prop.getProperty ("knowledgepro.admission.admittedthrough.lcta");
			if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
				lctaId=Integer.parseInt(lcta);
			}
			
			//lcta
			Integer communityId=0;	
			String community = prop.getProperty ("knowledgepro.admission.admittedthrough.community");
			if(lcta !=null && ! StringUtils.isEmpty(community)){ 
				communityId=Integer.parseInt(community);
			}

			
			
			//general
			Integer generalId=0;	
			String general = prop.getProperty ("knowledgepro.admission.admittedthrough.general");
			if(general !=null && ! StringUtils.isEmpty(general)){ 
				generalId=Integer.parseInt(general);
			}
			
				CourseTO  courseTo=new CourseTO();
				int totcount=0;
				
				Course course=it.next();
				if (course.getId()==27) {
					System.out.println("maths");
				}
				Set<SeatAllocation> seatAllocation= course.getSeatAllocations();
				Iterator<SeatAllocation> seatIterator=seatAllocation.iterator();
				
				while(seatIterator.hasNext())
				{
					
					SeatAllocation seat=seatIterator.next();
					//GENERAL
					 if(seat.getAdmittedThrough().getId()==generalId)
					{
						courseTo.setGenseat(seat.getNoOfSeats());
						courseTo.setGenremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					
					//SC
					 else if(seat.getAdmittedThrough().getId()==scId)
					{
						courseTo.setScseat(seat.getNoOfSeats());
						courseTo.setScremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					//ST
					else if(seat.getAdmittedThrough().getId()==stId)
					{
						courseTo.setStseat(seat.getNoOfSeats());
						courseTo.setStremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					//OBC
					else if(seat.getAdmittedThrough().getId()==ezhava_thiyya_billava_Id)
					{
						courseTo.setBcseat(seat.getNoOfSeats());
						courseTo.setBcremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					 //OBX
					else if(seat.getAdmittedThrough().getId()==obxId)
					{
						courseTo.setObxseat(seat.getNoOfSeats());
						courseTo.setObxremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					 //OBH
					else if(seat.getAdmittedThrough().getId()==obhId){
						courseTo.setObhseat(seat.getNoOfSeats());
						courseTo.setObhremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}//MU
					else if(seat.getAdmittedThrough().getId()==muId)
					{
						courseTo.setMuseat(seat.getNoOfSeats());
						courseTo.setMuremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
					//community
					else if(seat.getAdmittedThrough().getId()==communityId)
					{
						courseTo.setCommunityseat(seat.getNoOfSeats());
						courseTo.setCommunityremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
					}
	               
					 // lc and lcta
					else if(seat.getAdmittedThrough().getId()==lcId)
					{
						courseTo.setLcseat(seat.getNoOfSeats());
						courseTo.setLcremain(seat.getNoOfSeats());
						totcount=totcount+seat.getNoOfSeats();
						courseTo.setLctaseat(0);
						courseTo.setLctaremain(0);
					}
					 else if(seat.getAdmittedThrough().getId()==20)
						{
							courseTo.setSebcseat(seat.getNoOfSeats());
							courseTo.setSebcremain(seat.getNoOfSeats());
							totcount=totcount+seat.getNoOfSeats();
						}
							
					 
					  
	
					 
					
				}// seat while close
				
				courseTo.setTotcount(totcount);
				courseTo.setTotseats(course.getMaxIntake());
				courseTo.setId(course.getId());
				courseTo.setName(course.getName());
				
				
				courseToList.add(courseTo);
				
			}
	
	
		// TODO Auto-generated method stub
		return courseToList;
		
		}
	

		
		
	
		//allotting courses for all people based on Rank and Course for Multiple allotment
		/**
		 * @param form
		 * @return
		 * @throws Exception
		 */
		public boolean generateCourseAllotmentOnCourseRankForMultipleAllotment(ApplicationEditForm form)throws Exception{
		
		boolean isAdded=false;
		boolean isStop=true;
			
		try
		{	
			
		IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
				
		// create new 	allotmentMap with student id and StudentCourseAllotment object for storing db
		Map<Integer,StudentCourseAllotment> allotmentMap=txn.getallotmentMap(Integer.parseInt(form.getProgramTypeId()),Integer.parseInt(form.getAcademicYear()));
				
		Map<Integer,CourseTO> courseMap=new HashMap<Integer,CourseTO>();
		Map<Integer,List<StudentRank>> genRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteSCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteSTRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteBCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteOBXRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteOBHRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteMURankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteLCRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> casteCOMMUNITYRankMap=new HashMap<Integer,List<StudentRank>>();
		Map<Integer,List<StudentRank>> sebc=new HashMap<Integer,List<StudentRank>>();
		
		//get all courses
		List<Course> courseList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
	
		int courseSize=0;
		courseSize=courseList.size();
	
		//convert course bos to tos list
		List<CourseTO> courseToList=ApplicationRankHelper.getInstance().copyCourseBoTOForMultipleAllotment(courseList, form);
		//put general details of course into map
		Iterator<CourseTO> courseIterator1=courseToList.iterator();
		while(courseIterator1.hasNext())
		{
		CourseTO courseTO=courseIterator1.next();
		
		
		//getting rank list on Course and General rank
		List<StudentRank> rankListOnCourseGeneral=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"GENERAL");
		if(rankListOnCourseGeneral.size()!=0)
		{
			//setting genmap as key course id , value gen rank list
			genRankMap.put(courseTO.getId(), rankListOnCourseGeneral);
			int genSize=rankListOnCourseGeneral.size();
			StudentRank sr=rankListOnCourseGeneral.get(genSize-1);
			courseTO.setHighgenrank(sr.getRank());	
		}
		else
		{
			courseTO.setHighgenrank(0);
		}
		
		//getting rank list on Course and SC caste rank
		List<StudentRank> rankListOnCourseCasteSC=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SC");

		if(rankListOnCourseCasteSC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteSCRankMap.put(courseTO.getId(), rankListOnCourseCasteSC);
			 int casteSize=rankListOnCourseCasteSC.size();
			 StudentRank sr=rankListOnCourseCasteSC.get(casteSize-1);
			 courseTO.setHighscrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighscrank(0);
		}
		
		
		//getting rank list on Course and ST caste rank
		List<StudentRank> rankListOnCourseCasteST=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"ST");

		if(rankListOnCourseCasteST.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteSTRankMap.put(courseTO.getId(), rankListOnCourseCasteST);
			 int casteSize=rankListOnCourseCasteST.size();
			 StudentRank sr=rankListOnCourseCasteST.get(casteSize-1);
			 courseTO.setHighstrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighstrank(0);
		}

		
		 // check for self finance onle
		 if(courseTO.getId()==12 || courseTO.getId()==13 || courseTO.getId()==14 || courseTO.getId()==15 || courseTO.getId()==16 || courseTO.getId()==29 || courseTO.getId()==30 || courseTO.getId()==31)
		 {
		
			//getting rank list on Course and LCTA  caste rank
				List<StudentRank> rankListOnCourseCasteLCTA=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"LCTA");

				if(rankListOnCourseCasteLCTA.size()!=0)
				{
					//setting castemap as key course id , value caste rank list
					casteLCRankMap.put(courseTO.getId(), rankListOnCourseCasteLCTA);
					 int casteSize=rankListOnCourseCasteLCTA.size();
					 StudentRank sr=rankListOnCourseCasteLCTA.get(casteSize-1);
					 courseTO.setHighlctarank(sr.getRank());
					 courseTO.setHighlcrank(0);
				}
				else
				{
					courseTO.setHighlcrank(0);
					courseTO.setHighlctarank(0);
				}


			 
		 }
		 else
		 {
			 
			 
			//getting rank list on Course and LC caste rank
				List<StudentRank> rankListOnCourseCasteLC=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"LC");

				if(rankListOnCourseCasteLC.size()!=0)
				{
					//setting castemap as key course id , value caste rank list
					casteLCRankMap.put(courseTO.getId(), rankListOnCourseCasteLC);
					 int casteSize=rankListOnCourseCasteLC.size();
					 StudentRank sr=rankListOnCourseCasteLC.get(casteSize-1);
					 courseTO.setHighlcrank(sr.getRank());
					 courseTO.setHighlctarank(0);
				}
				else
				{
					courseTO.setHighlcrank(0);
					courseTO.setHighlctarank(0);
				}


			 
		 }
		
				//getting rank list on Course and BC caste rank BC mens EZHAVA,THIYYA,BILLAVA
		List<StudentRank> rankListOnCourseCasteBC=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"BC");

		if(rankListOnCourseCasteBC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteBCRankMap.put(courseTO.getId(), rankListOnCourseCasteBC);
			 int casteSize=rankListOnCourseCasteBC.size();
			 StudentRank sr=rankListOnCourseCasteBC.get(casteSize-1);
			 courseTO.setHighbcrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighbcrank(0);
		}

		//getting rank list on Course and MU caste rank
		List<StudentRank> rankListOnCourseCasteMU=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"MU");

		if(rankListOnCourseCasteMU.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteMURankMap.put(courseTO.getId(), rankListOnCourseCasteMU);
			 int casteSize=rankListOnCourseCasteMU.size();
			 StudentRank sr=rankListOnCourseCasteMU.get(casteSize-1);
			 courseTO.setHighmurank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighmurank(0);
		}

		//getting rank list on Course and community caste rank
		List<StudentRank> rankListOnCourseCasteCOMMUNITY=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"COMMUNITY");

		if(rankListOnCourseCasteCOMMUNITY.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			 casteCOMMUNITYRankMap.put(courseTO.getId(), rankListOnCourseCasteCOMMUNITY);
			 int casteSize=rankListOnCourseCasteCOMMUNITY.size();
			 StudentRank sr=rankListOnCourseCasteCOMMUNITY.get(casteSize-1);
			 courseTO.setHighcommunityrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighcommunityrank(0);
		}
		 
		
		//getting rank list on Course and OBH caste rank
		List<StudentRank> rankListOnCourseCasteOBH=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBH");

		if(rankListOnCourseCasteOBH.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteOBHRankMap.put(courseTO.getId(), rankListOnCourseCasteOBH);
			 int casteSize=rankListOnCourseCasteOBH.size();
			 StudentRank sr=rankListOnCourseCasteOBH.get(casteSize-1);
			 courseTO.setHighobhrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighobhrank(0);
		}

		//getting rank list on Course and OBX caste rank
		List<StudentRank> rankListOnCourseCasteOBX=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBX");

		if(rankListOnCourseCasteOBX.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			casteOBXRankMap.put(courseTO.getId(), rankListOnCourseCasteOBX);
			 int casteSize=rankListOnCourseCasteOBX.size();
			 StudentRank sr=rankListOnCourseCasteOBX.get(casteSize-1);
			 courseTO.setHighobxrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighobxrank(0);
		}
		
		//SEBC
		List<StudentRank> rankListOnCourseCasteSEBC=txn.getAdmApplonStudentOnCourseCategoryForMultipleAllotment(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SEBC");

		if(rankListOnCourseCasteSEBC.size()!=0)
		{
			//setting castemap as key course id , value caste rank list
			sebc.put(courseTO.getId(), rankListOnCourseCasteSEBC);
			 int casteSize=rankListOnCourseCasteSEBC.size();
			 StudentRank sr=rankListOnCourseCasteSEBC.get(casteSize-1);
			 courseTO.setHighsebcrank(sr.getRank());
			 	
		}
		else
		{
			courseTO.setHighsebcrank(0);
		}

		
		courseTO.setGencurrank(0);
		courseTO.setSccurrank(0);
		courseTO.setStcurrank(0);
		courseTO.setBccurrank(0);
		courseTO.setObhcurrank(0);
		courseTO.setObxcurrank(0);
		courseTO.setLccurrank(0);
		courseTO.setMucurrank(0);
		courseTO.setLctacurrank(0);
		courseTO.setCommunitycurrank(0);
		courseTO.setSebccurrank(0);
		
		courseMap.put(courseTO.getId(), courseTO);
		}
		//close course iterator map
		
		
		//start with infinite loop upto fill all seats 
		while(isStop)
		{
		
		Iterator<CourseTO> courseIterator=courseToList.iterator();	
		// start first course loop for fill caste seats
		while(courseIterator.hasNext())
		{
			
		CourseTO courseTO=courseIterator.next();
		
		// check Course and caste SC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteSC=casteSCRankMap.get(courseTO.getId());
		 
		// check sc setas zero or not, if zero stop course
		if(courseTO.getScremain()==null || courseTO.getScremain()==0){
			//course over
			courseTO.setScover(true);
			courseTO.setScremain(0);
			courseTO.setScseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
 
		else if(rankListOnCourseCasteSC!=null && rankListOnCourseCasteSC.size()!=0)
		{	 
			// add sc allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseCasteSC, "SC",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setScover(true);
			courseTO.setScremain(0);
			courseTO.setScseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// SC caste allot over
		
		
		// check Course and caste ST rank list size is empty
		 List<StudentRank> rankListOnCourseCasteST=casteSTRankMap.get(courseTO.getId());
		 
		// check st setas zero or not, if zero stop course
		if(courseTO.getStremain()==null || courseTO.getStremain()==0){
			//course over
			courseTO.setStover(true);
			courseTO.setStremain(0);
			courseTO.setStseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
 
		else if(rankListOnCourseCasteST!=null && rankListOnCourseCasteST.size()!=0)
		{	 
			// add st allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseCasteST, "ST",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setStover(true);
			courseTO.setStremain(0);
			courseTO.setStseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// ST caste allot over
	
			 
			 
			// check Course and caste LC rank list size is empty
			 List<StudentRank> rankListOnCourseCasteLC=casteLCRankMap.get(courseTO.getId());
			 
			 // check lc setas zero or not, if zero stop course
			if(courseTO.getLcremain()==null || courseTO.getLcremain()==0){
				//course over
				courseTO.setLcover(true);
				courseTO.setLcremain(0);
				courseTO.setLcseat(0);
				courseTO.setLctaover(true);
				courseTO.setLctaremain(0);
				courseTO.setLctaseat(0);
				
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
			}
			else if(rankListOnCourseCasteLC!=null && rankListOnCourseCasteLC.size()!=0)
			{	 
				// add lc allotment list and map
				addAllotmentlistForMultiple(rankListOnCourseCasteLC, "LC",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
			}// caste ranklist size close
			else
			{
				//course over
				courseTO.setLcover(true);
				courseTO.setLcremain(0);
				courseTO.setLcseat(0);
				courseTO.setLctaover(true);
				courseTO.setLctaremain(0);
				courseTO.setLctaseat(0);
				
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
			}
			// LC caste allot over
			
			
		 
		
		
		
		
		// check Course and caste BC rank list size is empty
		 List<StudentRank> rankListOnCourseCasteBC=casteBCRankMap.get(courseTO.getId());
		 
		
		// check bc setas zero or not, if zero stop course
		if(courseTO.getBcremain()==null || courseTO.getBcremain()==0){
			//course over
			courseTO.setBcover(true);
			courseTO.setBcremain(0);
			courseTO.setBcseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteBC!=null && rankListOnCourseCasteBC.size()!=0)
		{	 
			// add bc allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseCasteBC, "BC",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setBcover(true);
			courseTO.setBcremain(0);
			courseTO.setBcseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// BC caste allot over
		
		
		
		
		// check Course and caste MU rank list size is empty
		 List<StudentRank> rankListOnCourseCasteMU=casteMURankMap.get(courseTO.getId());
		 
		// check mu setas zero or not, if zero stop course
		if(courseTO.getMuremain()==null || courseTO.getMuremain()==0){
			//course over
			courseTO.setMuover(true);
			courseTO.setMuremain(0);
			courseTO.setMuseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteMU!=null && rankListOnCourseCasteMU.size()!=0)
		{	 
			// add mu allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseCasteMU, "MU",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setMuover(true);
			courseTO.setMuremain(0);
			courseTO.setMuseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// MU caste allot over
		
		
		// check Course and caste OBH rank list size is empty
		 List<StudentRank> rankListOnCourseCasteOBH=casteOBHRankMap.get(courseTO.getId());
		 
		// check obh setas zero or not, if zero stop course
		if(courseTO.getObhremain()==null || courseTO.getObhremain()==0){
			//course over
			courseTO.setObhover(true);
			courseTO.setObhremain(0);
			courseTO.setObhseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteOBH!=null && rankListOnCourseCasteOBH.size()!=0)
		{	 
			// add obh allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseCasteOBH, "OBH",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setObhover(true);
			courseTO.setObhremain(0);
			courseTO.setObhseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// OBH caste allot over
		
		
		// check Course and caste OBX rank list size is empty
		 List<StudentRank> rankListOnCourseCasteOBX=casteOBXRankMap.get(courseTO.getId());
		// check obx setas zero or not, if zero stop course
		if(courseTO.getObxremain()==null || courseTO.getObxremain()==0){
			//course over
			courseTO.setObxover(true);
			courseTO.setObxremain(0);
			courseTO.setObxseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteOBX!=null && rankListOnCourseCasteOBX.size()!=0)
		{	 
		// add obx allotment list and map
		addAllotmentlistForMultiple(rankListOnCourseCasteOBX, "OBX",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setObxover(true);
			courseTO.setObxremain(0);
			courseTO.setObxseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// OBX caste allot over
		
		
		// check Course and caste COMMUNITY rank list size is empty
		 List<StudentRank> rankListOnCourseCasteCOMMUNITY=casteCOMMUNITYRankMap.get(courseTO.getId());
		// check obx setas zero or not, if zero stop course
		if(courseTO.getCommunityremain()==null || courseTO.getCommunityremain()==0){
			//course over
			courseTO.setCommunityover(true);
			courseTO.setCommunityremain(0);
			courseTO.setCommunityseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseCasteCOMMUNITY!=null && casteCOMMUNITYRankMap.size()!=0)
		{	 
		// add obx allotment list and map
		addAllotmentlistForMultiple(rankListOnCourseCasteCOMMUNITY, "COMMUNITY",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);
		}// caste ranklist size close
		else
		{
			//course over
			courseTO.setCommunityover(true);
			courseTO.setCommunityremain(0);
			courseTO.setCommunityseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
		// community caste allot over
		
		
		
		}// first caste course loop close
	
	
	
		Iterator<CourseTO> courseIterator2=courseToList.iterator();
		// start second course loop for fill general seats
		while(courseIterator2.hasNext())
		{
		CourseTO courseTO=courseIterator2.next();
		
		//getting rank list on Course and General rank
		List<StudentRank> rankListOnCourseGeneral=genRankMap.get(courseTO.getId());// check Course and caste rank list size is empty
		// check st setas zero or not, if zero stop course
		if(courseTO.getGenremain()==null || courseTO.getGenremain()==0){
				//course over
				courseTO.setGenover(true);
				courseTO.setGenremain(0);
				courseTO.setGenseat(0);
				
				//update course map
				courseMap.put(courseTO.getId(), courseTO);

		}
		else if(rankListOnCourseGeneral!=null && rankListOnCourseGeneral.size()!=0)
		{
			// add general allotment list and map
			addAllotmentlistForMultiple(rankListOnCourseGeneral, "General",allotmentMap,courseMap,courseTO,form.getAllotedNo()+1,form);			
		}// general ranklist size close
		else
		{
			//course over
			courseTO.setGenover(true);
			courseTO.setGenremain(0);
			courseTO.setGenseat(0);
			
			//update course map
			courseMap.put(courseTO.getId(), courseTO);
		}
	
		
		}
		//second general course loop close
	
		// condition to check all seats fill are not
		int i=0;
		Set<Entry<Integer, CourseTO>> courseSet= courseMap.entrySet();
		Iterator<Entry<Integer, CourseTO>> icourse=courseSet.iterator();
		while(icourse.hasNext())
		{
		Entry<Integer, CourseTO> e=icourse.next();
		CourseTO courseTO=e.getValue();
		
	
			 if(courseTO.isGenover() && courseTO.isScover() && courseTO.isStover() && courseTO.isBcover() && courseTO.isObhover() && courseTO.isObxover() && courseTO.isMuover() && courseTO.isLcover() && courseTO.isCommunityover())
				{
					i++;
					System.out.println(courseTO.getName()+"=======  over   ========="+i);
				}
				
		
			 
		
		}
	
	
	
		// course size course over equals stop infinte loop
		if(courseSize==i)
		{
		isStop=false;
		}
	
		}// infinite loop close
	
	
	
	
		//getting values from allotmap and add values to list   
		List<StudentCourseAllotment> allotmentList=new ArrayList<StudentCourseAllotment>();
		Set<Entry<Integer, StudentCourseAllotment>> studentCourseAllotmentSet= allotmentMap.entrySet();
	
		Iterator<Entry<Integer, StudentCourseAllotment>> i=studentCourseAllotmentSet.iterator();
		while(i.hasNext())
		{
		Entry<Integer, StudentCourseAllotment> e=i.next();
		StudentCourseAllotment allotment=e.getValue();
		allotmentList.add(allotment);
		}
	
		// adding records course wise in database
		isAdded=txn.generateCourseAllotment(allotmentList);
		
		
		// storing allotment data to another table
		List<StudentCourseAllotmentPrev> prevAllotmentList = new ArrayList<StudentCourseAllotmentPrev>();
		Iterator<StudentCourseAllotment> altr = allotmentList.iterator();
		while(altr.hasNext()){
			StudentCourseAllotment allotmentbo = altr.next();
			if(allotmentbo.getAllotmentNo().intValue()==form.getAllotedNo().intValue()+1){
				
				StudentCourseAllotmentPrev prevAllotmentbo = new StudentCourseAllotmentPrev();
				
				prevAllotmentbo.setActive(allotmentbo.getIsActive());
				prevAllotmentbo.setAdmAppln(allotmentbo.getAdmAppln());
				prevAllotmentbo.setAllotmentNo(allotmentbo.getAllotmentNo());
				prevAllotmentbo.setCourse(allotmentbo.getCourse());
				prevAllotmentbo.setCreatedBy(allotmentbo.getCreatedBy());
				prevAllotmentbo.setCreatedDate(allotmentbo.getCreatedDate());
				
				prevAllotmentbo.setIndexMark(allotmentbo.getIndexMark());
				prevAllotmentbo.setIsAlloted(allotmentbo.getIsAlloted());
				prevAllotmentbo.setIsAssigned(allotmentbo.getIsAssigned());
				prevAllotmentbo.setIsCancel(allotmentbo.getIsCancel());
				prevAllotmentbo.setIsCaste(allotmentbo.getIsCaste());
				prevAllotmentbo.setIsGeneral(allotmentbo.getIsGeneral());
				prevAllotmentbo.setIsSatisfied(allotmentbo.getIsSatisfied());
				
				if(allotmentbo.getLastModifiedDate()!=null){
					prevAllotmentbo.setLastModifiedDate(allotmentbo.getLastModifiedDate());
				}
				if(allotmentbo.getModifiedBy()!=null){
					prevAllotmentbo.setModifiedBy(allotmentbo.getModifiedBy());
				}
				
				prevAllotmentbo.setPrefNo(allotmentbo.getPrefNo());
				prevAllotmentbo.setRank(allotmentbo.getRank());
				
				
				prevAllotmentList.add(prevAllotmentbo);
				
			}
			
			
			
		}
		txn.savePrevCourseAllotment(prevAllotmentList);
		
		
		
	
		}// try close
		catch(Exception e)
		{
		isAdded=false;
		Session session = null;
		Transaction transaction = null;
		session = HibernateUtil.getSession();
		transaction = session.beginTransaction();
		transaction.begin();
		Query query1=session.createQuery("delete StudentCourseAllotment s where s.createdDate = current_date");
		//query.setString("current_date1",date);
		query1.executeUpdate();
		transaction.commit();
		session.close();
		System.out.println("helper===============Error occured in while alloting courses to student ================="+e);
		throw e;
		}
		finally
		{
		
		}
	
		return isAdded;

	
		}


		
		
		// get general and caste seats based on course and convert course bo to to for Multiple allotment

		/**
		 * @param courseList
		 * @return
		 */
		public List<CourseTO> copyCourseBoTOForMultipleAllotment(List<Course> courseList,ApplicationEditForm admForm) 
		{
			
		List list=null;
		Session session = null;
		Query hqlQuery=null;
		Properties prop = new Properties();
		try 
		{
			InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
			prop.load(in);
			session = HibernateUtil.getSession();
			
		} 
		catch (FileNotFoundException e)
		{
			log.error("Unable to read properties file...", e);

		} 
		catch (IOException e)
		{
			log.error("Unable to read properties file...", e);

		}
		catch (Exception e)
		{
			log.error("Unable to read properties file...", e);

		}

       
        	
        
		List<CourseTO> courseToList=new LinkedList<CourseTO>();
		 try{
		Iterator<Course> it= courseList.iterator();
		
		while(it.hasNext())
		{
			
						
			
			
			//sc
			Integer scId=0;	
			String sc = prop.getProperty ("knowledgepro.admission.admittedthrough.sc");
			if(sc !=null && ! StringUtils.isEmpty(sc)){ 
				scId=Integer.parseInt(sc);
			}
			
			//ezhava_thiyya_billava
			Integer ezhava_thiyya_billava_Id=0;	
			String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.ezhava_thiyya_billava");
			if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
				ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
			}
			
			//mu
			Integer muId=0;	
			String mu = prop.getProperty ("knowledgepro.admission.admittedthrough.mu");
			if(mu !=null && ! StringUtils.isEmpty(mu)){ 
				muId=Integer.parseInt(mu);
			}
			
			//st
			Integer stId=0;	
			String st = prop.getProperty ("knowledgepro.admission.admittedthrough.st");
			if(st !=null && ! StringUtils.isEmpty(st)){ 
				stId=Integer.parseInt(st);
			}
			
			
			
			//obh
			Integer obhId=0;	
			String obh = prop.getProperty ("knowledgepro.admission.admittedthrough.obh");
			if(obh !=null && ! StringUtils.isEmpty(obh)){ 
				obhId=Integer.parseInt(obh);
			}
			
			//obx
			Integer obxId=0;	
			String obx = prop.getProperty ("knowledgepro.admission.admittedthrough.obx");
			if(obx !=null && ! StringUtils.isEmpty(obx)){ 
				obxId=Integer.parseInt(obx);
			}
			
			//lc
			Integer lcId=0;	
			String lc = prop.getProperty ("knowledgepro.admission.admittedthrough.lc");
			if(lc !=null && ! StringUtils.isEmpty(lc)){ 
				lcId=Integer.parseInt(lc);
			}
			
			//lcta
			Integer lctaId=0;	
			String lcta = prop.getProperty ("knowledgepro.admission.admittedthrough.lcta");
			if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
				lctaId=Integer.parseInt(lcta);
			}
			
			//general
			Integer generalId=0;	
			String general = prop.getProperty ("knowledgepro.admission.admittedthrough.general");
			if(general !=null && ! StringUtils.isEmpty(general)){ 
				generalId=Integer.parseInt(general);
			}
			
			//community
			Integer communityId=0;	
			String community = prop.getProperty ("knowledgepro.admission.admittedthrough.community");
			if(general !=null && ! StringUtils.isEmpty(community)){ 
				communityId=Integer.parseInt(community);
			}
			
			//=========================================================================================
			
			
			//sc
			Integer scIdr=0;	
			String scr = prop.getProperty ("knowledgepro.admission.religionsection.sc");
			if(scr !=null && ! StringUtils.isEmpty(scr)){ 
				scIdr=Integer.parseInt(scr);
			}
			
			//ezhava_thiyya_billava
			Integer ezhava_thiyya_billava_Idr=0;	
			String ezhava_thiyya_billavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
			if(ezhava_thiyya_billavar !=null && ! StringUtils.isEmpty(ezhava_thiyya_billavar)){ 
				ezhava_thiyya_billava_Idr=Integer.parseInt(ezhava_thiyya_billavar);
			}
			
			//mu
			Integer muIdr=0;	
			String mur = prop.getProperty ("knowledgepro.admission.religionsection.mu");
			if(mur !=null && ! StringUtils.isEmpty(mur)){ 
				muIdr=Integer.parseInt(mur);
			}
			
			//st
			Integer stIdr=0;	
			String str = prop.getProperty ("knowledgepro.admission.religionsection.st");
			if(str !=null && ! StringUtils.isEmpty(str)){ 
				stIdr=Integer.parseInt(str);
			}
			
			//lc
			Integer lcIdr=0;	
			String lcr = prop.getProperty ("knowledgepro.admission.religionsection.lc");
			if(lcr !=null && ! StringUtils.isEmpty(lcr)){ 
				lcIdr=Integer.parseInt(lcr);
			}
			
			//lcta
			Integer lctaIdr=0;	
			String lctar = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
			if(lctar !=null && ! StringUtils.isEmpty(lctar)){ 
				lctaIdr=Integer.parseInt(lctar);
			}
			
			//obh
			Integer obhIdr=0;	
			String obhr = prop.getProperty ("knowledgepro.admission.religionsection.obh");
			if(obhr !=null && ! StringUtils.isEmpty(obhr)){ 
				obhIdr=Integer.parseInt(obhr);
			}
			
			//obx
			Integer obxIdr=0;	
			String obxr = prop.getProperty ("knowledgepro.admission.religionsection.obx");
			if(obxr !=null && ! StringUtils.isEmpty(obxr)){ 
				obxIdr=Integer.parseInt(obxr);
			}
			
			//ezhava
			Integer ezhavaIdr=0;	
			String ezhavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
			if(ezhavar !=null && ! StringUtils.isEmpty(ezhavar)){ 
				ezhavaIdr=Integer.parseInt(ezhavar);
			}
			

			//billava
			Integer billavaIdr=0;	
			String billavar = prop.getProperty ("knowledgepro.admission.religionsection.billava");
			if(billavar !=null && ! StringUtils.isEmpty(billavar)){ 
				billavaIdr=Integer.parseInt(billavar);
			}
			
			//thiyya
			Integer thiyyaIdr=0;	
			String thiyyar = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
			if(thiyyar !=null && ! StringUtils.isEmpty(thiyyar)){ 
				thiyyaIdr=Integer.parseInt(thiyyar);
			}
			
			//community
			Integer communityIdr=0;	
			String communityr = prop.getProperty ("knowledgepro.admission.religionsection.community");
			if(communityr !=null && ! StringUtils.isEmpty(communityr)){ 
				communityIdr=Integer.parseInt(communityr);
			}

			

			
			
			
			
				CourseTO  courseTo=new CourseTO();
				int totcount=0;
				
				Course course=it.next();
				if(course.getId()==10){
					System.out.println(course.getName());
				}
				Set<SeatAllocation> seatAllocation= course.getSeatAllocations();
				Iterator<SeatAllocation> seatIterator=seatAllocation.iterator();
				
				
				while(seatIterator.hasNext())
				{
					
					SeatAllocation seat=seatIterator.next();
					//GENERAL
					 if(seat.getAdmittedThrough().getId()==generalId)
					{
						courseTo.setGenseat(seat.getNoOfSeats());
						courseTo.setGenChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where  s.course.id=:courseId and s.isGeneral=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						list=hqlQuery.list();
						courseTo.setGenremain(seat.getNoOfSeats()-list.size());
						totcount=totcount+courseTo.getGenremain();
					}
					
					//SC
					 else if(seat.getAdmittedThrough().getId()==scId)
					{
						courseTo.setScseat(seat.getNoOfSeats());
						courseTo.setScChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",scIdr);
						list=hqlQuery.list();
						courseTo.setScremain(seat.getNoOfSeats()-list.size());
						totcount=totcount+courseTo.getScremain();
					}
					//ST
					else if(seat.getAdmittedThrough().getId()==stId)
					{
						courseTo.setStseat(seat.getNoOfSeats());
						courseTo.setStChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",stIdr);
						list=hqlQuery.list();
						courseTo.setStremain(seat.getNoOfSeats()-list.size());
						totcount=totcount+courseTo.getStremain();
					}
					//OBC
					else if(seat.getAdmittedThrough().getId()==ezhava_thiyya_billava_Id)
					{
						courseTo.setBcseat(seat.getNoOfSeats());
						courseTo.setBcChance(seat.getChanceMemoLimit());
						//getting alloted seats
						String s=""+ezhavaIdr+","+thiyyaIdr+","+billavaIdr;
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id in("+s+") and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						//hqlQuery.setInteger("casteId",ezhava_thiyya_billava_Id);
						list=hqlQuery.list();
						courseTo.setBcremain(seat.getNoOfSeats()-list.size());
						
						totcount=totcount+courseTo.getBcremain();
					}
					 //OBX
					else if(seat.getAdmittedThrough().getId()==obxId)
					{
						courseTo.setObxseat(seat.getNoOfSeats());
						courseTo.setObxChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",obxIdr);
						list=hqlQuery.list();
						courseTo.setObxremain(seat.getNoOfSeats()-list.size());
						
						totcount=totcount+courseTo.getObxremain();
					}
					 //OBH
					else if(seat.getAdmittedThrough().getId()==obhId){
						courseTo.setObhseat(seat.getNoOfSeats());
						courseTo.setObhChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",obhIdr);
						list=hqlQuery.list();
						courseTo.setObhremain(seat.getNoOfSeats()-list.size());
						
						totcount=totcount+courseTo.getObhremain();
					}
					 //MU
					else if(seat.getAdmittedThrough().getId()==muId)
					{
						courseTo.setMuseat(seat.getNoOfSeats());
						courseTo.setMuChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",muIdr);
						list=hqlQuery.list();
						courseTo.setMuremain(seat.getNoOfSeats()-list.size());
						
						totcount=totcount+courseTo.getMuremain();
					}
					 
					 
					else if(seat.getAdmittedThrough().getId()==lcId)
							{
								courseTo.setLcseat(seat.getNoOfSeats());
								courseTo.setLcChance(seat.getChanceMemoLimit());
								//getting alloted seats
								hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
								hqlQuery.setInteger("courseId",course.getId());
								hqlQuery.setInteger("casteId",lcIdr);
							}
					
					else if(seat.getAdmittedThrough().getId()==communityId)
					{
						courseTo.setCommunityseat(seat.getNoOfSeats());
						courseTo.setCommunityChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.isCommunity=1 and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						//hqlQuery.setInteger("casteId",communityIdr);
						list=hqlQuery.list();
						courseTo.setCommunityremain(seat.getNoOfSeats()-list.size());
						
					}	
					//SEBC
					else if(seat.getAdmittedThrough().getId()==20)
					{
						courseTo.setSebcseat(seat.getNoOfSeats());
						courseTo.setSebcChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and (s.isAssigned=1 or s.isSatisfied=1)  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear()));
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",9);
						list=hqlQuery.list();
						courseTo.setSebcremain(seat.getNoOfSeats()-list.size());
						totcount=totcount+courseTo.getStremain();
					}
					 
					  
					 	
				}// seat while close
				
				courseTo.setTotcount(totcount);
				courseTo.setTotseats(course.getMaxIntake());
				courseTo.setId(course.getId());
				courseTo.setName(course.getName());
				
				
				courseToList.add(courseTo);
				
			}
        }catch (Exception e) {
			System.out.println(e);
		}
	
		// TODO Auto-generated method stub
		return courseToList;
		
		}
	

		
				

		
		

		/**
		 * @param rankList
		 * @param category
		 * @param allotmentMap
		 * @param courseMap
		 * @param itrcourseTO
		 * @param allotmentNo
		 */
			public void addAllotmentlistForMultiple(List<StudentRank> rankList,String category,Map<Integer,StudentCourseAllotment> allotmentMap,Map<Integer,CourseTO> courseMap,CourseTO itrcourseTO,Integer allotmentNo,ApplicationEditForm form) 
		{
		
			CourseTO courseTO=courseMap.get(itrcourseTO.getId());
			System.out.println();
			System.out.println("==========================  "+ courseTO.getName() + " ============================="+category);
			System.out.println("before gencurrent=="+courseTO.getGencurrank());
			System.out.println("before sccurrent=="+courseTO.getSccurrank());
			System.out.println("before stcurrent=="+courseTO.getStcurrank());
			System.out.println("before bccurrent=="+courseTO.getBccurrank());
			System.out.println("before mucurrent=="+courseTO.getMucurrank());
			System.out.println("before obhcurrent=="+courseTO.getObhcurrank());
			System.out.println("before obxcurrent=="+courseTO.getObxcurrank());
			System.out.println("before communitycurrent=="+courseTO.getCommunitycurrank());
			
			System.out.println("before genremain=="+courseTO.getGenremain());
			System.out.println("before scremain=="+courseTO.getScremain());
			System.out.println("before stremain=="+courseTO.getStremain());
			System.out.println("before bcremain=="+courseTO.getBcremain());
			System.out.println("before muremain=="+courseTO.getMuremain());
			System.out.println("before obhremain=="+courseTO.getObhremain());
			System.out.println("before obxremain=="+courseTO.getObxremain());
			System.out.println("before communityremain=="+courseTO.getCommunityremain());
			
			System.out.println("before higen=="+courseTO.getHighgenrank());
			System.out.println("before higsc=="+courseTO.getHighscrank());
			System.out.println("before hist=="+courseTO.getHighstrank());
			System.out.println("before higbc=="+courseTO.getHighbcrank());
			System.out.println("before himu=="+courseTO.getHighmurank());
			System.out.println("before higobh=="+courseTO.getHighobhrank());
			System.out.println("before higobx=="+courseTO.getHighobxrank());
			System.out.println("before higcommunity=="+courseTO.getHighcommunityrank());
			
			System.out.println("before lccurrent=="+courseTO.getLccurrank());
			System.out.println("before lcremain=="+courseTO.getLcremain());
		    System.out.println("before higlc=="+courseTO.getHighlcrank());
			 
			 
			System.out.println("==========================  "+ category+ " =============================");
			
			
			
			Properties prop = new Properties();
			try 
			{
				InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
				prop.load(in);
			} 
			catch (FileNotFoundException e)
			{
				log.error("Unable to read properties file...", e);

			} 
			catch (IOException e)
			{
				log.error("Unable to read properties file...", e);

			}
			
			//sc
			Integer scId=0;	
			String sc = prop.getProperty ("knowledgepro.admission.religionsection.sc");
			if(sc !=null && ! StringUtils.isEmpty(sc)){ 
				scId=Integer.parseInt(sc);
			}
			
			//ezhava_thiyya_billava
			Integer ezhava_thiyya_billava_Id=0;	
			String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
			if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
				ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
			}
			
			//mu
			Integer muId=0;	
			String mu = prop.getProperty ("knowledgepro.admission.religionsection.mu");
			if(mu !=null && ! StringUtils.isEmpty(mu)){ 
				muId=Integer.parseInt(mu);
			}
			
			//st
			Integer stId=0;	
			String st = prop.getProperty ("knowledgepro.admission.religionsection.st");
			if(st !=null && ! StringUtils.isEmpty(st)){ 
				stId=Integer.parseInt(st);
			}
			
			//lc
			Integer lcId=0;	
			String lc = prop.getProperty ("knowledgepro.admission.religionsection.lc");
			if(lc !=null && ! StringUtils.isEmpty(lc)){ 
				lcId=Integer.parseInt(lc);
			}
			
			//lcta
			Integer lctaId=0;	
			String lcta = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
			if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
				lctaId=Integer.parseInt(lcta);
			}
			
			//obh
			Integer obhId=0;	
			String obh = prop.getProperty ("knowledgepro.admission.religionsection.obh");
			if(obh !=null && ! StringUtils.isEmpty(obh)){ 
				obhId=Integer.parseInt(obh);
			}
			
			//obx
			Integer obxId=0;	
			String obx = prop.getProperty ("knowledgepro.admission.religionsection.obx");
			if(obx !=null && ! StringUtils.isEmpty(obx)){ 
				obxId=Integer.parseInt(obx);
			}
			
			//ezhava
			Integer ezhavaId=0;	
			String ezhava = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
			if(ezhava !=null && ! StringUtils.isEmpty(ezhava)){ 
				ezhavaId=Integer.parseInt(ezhava);
			}
			

			//billava
			Integer billavaId=0;	
			String billava = prop.getProperty ("knowledgepro.admission.religionsection.billava");
			if(billava !=null && ! StringUtils.isEmpty(billava)){ 
				billavaId=Integer.parseInt(billava);
			}
			
			//thiyya
			Integer thiyyaId=0;	
			String thiyya = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
			if(thiyya !=null && ! StringUtils.isEmpty(thiyya)){ 
				thiyyaId=Integer.parseInt(thiyya);
			}
			
			//community
			Integer communityId=0;	
			String community = prop.getProperty ("knowledgepro.admission.religionsection.community");
			if(community !=null && ! StringUtils.isEmpty(community)){ 
				communityId=Integer.parseInt(community);
			}

			
		
		
		
		Iterator<StudentRank> rankIterator=rankList.iterator();
		// loop for getting StudentRank on rank and preference
		while(rankIterator.hasNext()){	
			
			StudentRank currank = (StudentRank) rankIterator.next();
			
			
			
			
			// general check start
			if(category.equalsIgnoreCase("General"))
			{
				// check student rank is more than gencurcommon rank
			   if(currank.getRank() > courseTO.getGencurrank())
			   {
				   // check gencurcommon rank less than highest gen rank and gen seats not equal zero
				   if (courseTO.getGencurrank() < courseTO.getHighgenrank() && courseTO.getGenremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setGencurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							
							
							// for multiple allotment
							if(allotment.getPrefNo().intValue() == currank.getPrefNo().intValue() )
							{
								

								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
									//check map contain student seat is caste or not and add caste seat
									if(allotment.getIsCaste())
									{
										
										//update old course of caste seats
										updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
										
										
										allotment.setIsCaste(false);
										allotment.setIsCommunity(false);
										allotment.setIsGeneral(true);   
											
										// update--map details
										allotment.setPrefNo(currank.getPrefNo());
										allotment.setRank(currank.getRank());
										allotment.setIndexMark(currank.getIndexMark());
										allotment.setAllotmentNo(allotmentNo);
										allotment.setModifiedBy(form.getUserId());
										allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
										
										
										Course course=new Course();
										course.setId(courseTO.getId());
										allotment.setCourse(course);
										
										//update allotment map
										allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
										
										
										 // update--course generaldetails
										courseTO.setGencurrank( currank.getRank());
										
										courseTO.setGenremain(courseTO.getGenremain()-1);
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
										
										System.out.println(allotment.getIsGeneral()+" modified data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
										
									}
									//check map contain student seat is gen or not	and add genremain seat
									else if(allotment.getIsGeneral())
									{
										//updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
										courseTO.setGencurrank( currank.getRank());
										courseMap.put(courseTO.getId(), courseTO);
									}
									else if(allotment.getIsCommunity()) {
										
										//update old course of caste seats
										updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
										
										
										allotment.setIsCaste(false);
										allotment.setIsCommunity(false);
										allotment.setIsGeneral(true);   
											
										// update--map details
										allotment.setPrefNo(currank.getPrefNo());
										allotment.setRank(currank.getRank());
										allotment.setIndexMark(currank.getIndexMark());
										allotment.setAllotmentNo(allotmentNo);
										allotment.setModifiedBy(form.getUserId());
										allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
										
										
										Course course=new Course();
										course.setId(courseTO.getId());
										allotment.setCourse(course);
										
										//update allotment map
										allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
										
										
										 // update--course generaldetails
										courseTO.setGencurrank( currank.getRank());
										
										courseTO.setGenremain(courseTO.getGenremain()-1);
										//update course map
										courseMap.put(courseTO.getId(), courseTO);
										
										System.out.println(allotment.getIsGeneral()+" modified data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
										
									}
							}
							
							
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() && !allotment.getIsSatisfied())
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
									//check map contain student seat is caste or not and add caste seat
									if(allotment.getIsCaste())
									{
										
										//update old course of caste seats
										updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
										
									}
									//check map contain student seat is gen or not	and add genremain seat
									else if(allotment.getIsGeneral())
									{
										updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
									}
					
								allotment.setIsCaste(false);
								allotment.setIsGeneral(true);   
									
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								 // update--course generaldetails
								courseTO.setGencurrank( currank.getRank());
								
								courseTO.setGenremain(courseTO.getGenremain()-1);
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsGeneral()+" modified data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
							if(allotment.getIsSatisfied())
							
							{
								// update--course generaldetails
								courseTO.setGencurrank( currank.getRank());
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
							}
								
						}
						//add new student if map does contains
						else
						{
							
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"GENERAL",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							   
							   // update--course general details
							   courseTO.setGencurrank( currank.getRank());
							   courseTO.setGenremain(courseTO.getGenremain()-1);
						   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
						  	 System.out.println("fresh data in general id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
							
						}				  							
					}//main if over
			   }				  
			}// general check over
			
		
			
			
			
			
			// caste sc check start
			
			//SC start
			else if(category.equalsIgnoreCase("SC"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==scId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getSccurrank()){
				 
				// check gencurcommon rank less than highest sc caste rank and sc caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighscrank() && courseTO.getScremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setSccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste scremain seat
								if(allotment.getIsCaste())
								{
									
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
									/*oldCourseTO.setScremain(oldCourseTO.getScremain()+1);
									oldCourseTO.setScover(false);
									System.out.println(oldCourseTO.getName()+"old--------caste in add casterem=="+oldCourseTO.getScremain());
									//update course map
									courseMap.put(oldCourseTO.getId(), oldCourseTO);
									*/
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setScremain(courseTO.getScremain()-1);
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								// update-- course general details
								courseTO.setSccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
							
							if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								courseTO.setSccurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
								
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setScremain(courseTO.getScremain()-1);
							   courseTO.setSccurrank( currank.getRank());
								
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			// SC over
			
			
			
			
			//ST start
			else if(category.equalsIgnoreCase("ST"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==stId )		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getStcurrank()){
				 
				// check gencurcommon rank less than highest st caste rank and st caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighstrank() && courseTO.getStremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setStcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Stremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setStremain(courseTO.getStremain()-1);
								courseTO.setStcurrank( currank.getRank());
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
							
							if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
								courseTO.setStcurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setStremain(courseTO.getStremain()-1);
							   courseTO.setStcurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//ST Over
			
			
			
		
			//BC start
			else if(category.equalsIgnoreCase("BC"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==ezhavaId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==billavaId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==thiyyaId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getBccurrank()){
				 
				// check gencurcommon rank less than highest Bc caste rank and Bc caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighbcrank() && courseTO.getBcremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setBccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Bcremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setBcremain(courseTO.getBcremain()-1);
								courseTO.setBccurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
							
	                       if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
	                    	    courseTO.setBccurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setBcremain(courseTO.getBcremain()-1);
							   courseTO.setBccurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//BC Over
			
			
			//MU srart
			else if(category.equalsIgnoreCase("MU"))
			{	
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==muId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getMucurrank()){
				 
				// check gencurcommon rank less than highest Mu caste rank and Mu caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighmurank() && courseTO.getMuremain() != 0)
					{
					 
					 
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setMucurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Muremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setMuremain(courseTO.getMuremain()-1);
								courseTO.setMucurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
							
	                        if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
	                        	courseTO.setMucurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setMuremain(courseTO.getMuremain()-1);
							   courseTO.setMucurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//MU Over
			
			
			//OBH start
			else if(category.equalsIgnoreCase("OBH"))
			{
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==obhId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getObhcurrank()){
				 
				// check gencurcommon rank less than highest Obh caste rank and Obh caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighobhrank() && courseTO.getObhremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setObhcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Obhremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setObhremain(courseTO.getObhremain()-1);
								courseTO.setObhcurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
	                        if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
	                        	courseTO.setObhcurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setObhremain(courseTO.getObhremain()-1);
							   courseTO.setObhcurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//OBH Over
			
			
			//OBX
			else if(category.equalsIgnoreCase("OBX"))
			{
				
			//OBX	
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==obxId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getObxcurrank()){
				 
				// check gencurcommon rank less than highest Obx caste rank and Obx caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighobxrank() && courseTO.getObxremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setObxcurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Obxremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setObxremain(courseTO.getObxremain()-1);
								courseTO.setObxcurrank( currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}	
							
	                        if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
	                        	courseTO.setObxcurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							
						   //get allotment object seat to store db
						   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
							
						   //genrankList.add(studentRank);
						   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
						  
						   // update-- course general details
						   courseTO.setObxremain(courseTO.getObxremain()-1);
						   courseTO.setObxcurrank( currank.getRank());
						   
						   //update course map
						   courseMap.put(courseTO.getId(), courseTO);
						   
						   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//OBX Over
			
			

			//LC ,LCTAstart
			if(category.equalsIgnoreCase("LC"))
			{	
			if(currank.getAdmAppln().getPersonalData().getReligionSection().getId()==lcId || currank.getAdmAppln().getPersonalData().getReligionSection().getId()==lctaId)		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getLccurrank()){
				 
				// check gencurcommon rank less than highest lc caste rank and lc caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighlcrank() && courseTO.getLcremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setLccurrank( currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste Lcremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIsAssigned(false);
								allotment.setIsSatisfied(false);
								
								Course course=new Course();
								course.setId(courseTO.getId());
								allotment.setCourse(course);
								
								courseTO.setLcremain(courseTO.getLcremain()-1);
								courseTO.setLccurrank( currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								allotment.setModifiedBy(form.getUserId());
								allotment.setLastModifiedDate(CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate()));
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
							
							if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
								
								courseTO.setLccurrank( currank.getRank());
								courseMap.put(courseTO.getId(), courseTO);
							}
						}
						//add new student if map does contains
						else
						{
							  //get allotment object seat to store db
							   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"CASTE",allotmentNo,form);
								
							   //genrankList.add(studentRank);
							   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
							  
							   // update-- course general details
							   courseTO.setLcremain(courseTO.getLcremain()-1);
							   courseTO.setLccurrank( currank.getRank());
							   
							   //update course map
							   courseMap.put(courseTO.getId(), courseTO);
						   
							   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//LC Over
			
			 

			
			 
			//communtiy start
			else if(category.equalsIgnoreCase("COMMUNITY"))
			{
				
			//OBX	
			if(currank.getAdmAppln().getPersonalData().getIsCommunity())		
			{
			// check student rank is more than gencurcommon rank
			 if(currank.getRank() > courseTO.getGencurrank() && currank.getRank() > courseTO.getCommunitycurrank()){
				 
				// check gencurcommon rank less than highest community caste rank and comunity caste seats not equal zero
				 if(courseTO.getGencurrank() < courseTO.getHighcommunityrank() && courseTO.getCommunityremain() != 0)
					{
						// check map contains student or not
						if(allotmentMap.containsKey(currank.getAdmAppln().getId()))
						{
							StudentCourseAllotment allotment=allotmentMap.get(currank.getAdmAppln().getId());
							
							// check map contain student pre less than with student rank pre
							if(allotment.getPrefNo().intValue() < currank.getPrefNo().intValue() )
							{
								// update-- course general details
								courseTO.setCommunitycurrank(currank.getRank());
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
							}
							
							// check map contain student pre is greater more than equal with student rank pre
							if(allotment.getPrefNo().intValue() > currank.getPrefNo().intValue() )
							{
								//update old course general details
								CourseTO oldCourseTO=courseMap.get(allotment.getCourse().getId());
								//check map contain student seat is caste or not and add caste communityremain seat
								if(allotment.getIsCaste())
								{
									//update old course of caste seats
									updateCourseMapOnOldCourse(allotment, courseMap, oldCourseTO);
									
								}
								//check map contain student seat is gen or not and add genremain seat
								else if(allotment.getIsGeneral())
								{
									updateCourseMapOnOldCourseGeneral(courseMap, oldCourseTO);
								}
										
								allotment.setIsCaste(true);
								allotment.setIsGeneral(false); 
								// update--map details
								allotment.setPrefNo(currank.getPrefNo());
								allotment.setRank(currank.getRank());
								allotment.setIndexMark(currank.getIndexMark());
								allotment.setAllotmentNo(allotmentNo);
								
								Course course=new Course();
								course.setId(courseTO.getId());
					
								allotment.setCourse(course);
								
								courseTO.setCommunityremain(courseTO.getCommunityremain()-1);
								courseTO.setCommunitycurrank(currank.getRank());
								
								//update allotment map
								allotmentMap.put(allotment.getAdmAppln().getId(), allotment);
								
								//update course map
								courseMap.put(courseTO.getId(), courseTO);
								
								System.out.println(allotment.getIsCaste()+" modified data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
								
							}
							
		                       if(allotment.getPrefNo().intValue()==currank.getPrefNo().intValue()){
									
		                    	   courseTO.setCommunitycurrank(currank.getRank());
								   courseMap.put(courseTO.getId(), courseTO);
								}
						}
						//add new student if map does contains
						else
						{
							
						   //get allotment object seat to store db
						   StudentCourseAllotment allotment =getStudetCourseAllotmentBO(currank,"COMMUNITY",allotmentNo,form);
							
						   //genrankList.add(studentRank);
						   allotmentMap.put(currank.getAdmAppln().getId(), allotment);
						  
						   // update-- course general details
						   courseTO.setCommunityremain(courseTO.getCommunityremain()-1);
						   courseTO.setCommunitycurrank(currank.getRank());
						   
						   //update course map
						   courseMap.put(courseTO.getId(), courseTO);
						   
						   System.out.println("fresh data in caste id=="+currank.getAdmAppln().getId()+" pre=="+currank.getPrefNo()+" rank=="+currank.getRank());
						}	
					}// main if over 
			 	}	  
			}
			
			}
			//community Over
			

			
			// caste check over
					


				
			
		}// student rank loop close
		
		
		
		///after
		System.out.println("after gencurrent=="+courseTO.getGencurrank());
		System.out.println("after sccurrent=="+courseTO.getSccurrank());
		System.out.println("after stcurrent=="+courseTO.getStcurrank());
		System.out.println("after bccurrent=="+courseTO.getBccurrank());
		System.out.println("after lccurrent=="+courseTO.getLccurrank());
		System.out.println("after mucurrent=="+courseTO.getMucurrank());
		System.out.println("after obhcurrent=="+courseTO.getObhcurrank());
		System.out.println("after obxcurrent=="+courseTO.getObxcurrank());
		System.out.println("after communitycurrent=="+courseTO.getCommunitycurrank());
		
		System.out.println("after genremain=="+courseTO.getGenremain());
		System.out.println("after scremain=="+courseTO.getScremain());
		System.out.println("after stremain=="+courseTO.getStremain());
		System.out.println("after bcremain=="+courseTO.getBcremain());
		System.out.println("after muremain=="+courseTO.getMuremain());
		System.out.println("after lcremain=="+courseTO.getLcremain());
		System.out.println("after obhremain=="+courseTO.getObhremain());
		System.out.println("after obxremain=="+courseTO.getObxremain());
		System.out.println("after communityremain=="+courseTO.getCommunityremain());
		
		System.out.println("after higen=="+courseTO.getHighgenrank());
		System.out.println("after higsc=="+courseTO.getHighscrank());
		System.out.println("after hist=="+courseTO.getHighstrank());
		System.out.println("after higbc=="+courseTO.getHighbcrank());
		System.out.println("after himu=="+courseTO.getHighmurank());
		System.out.println("after higlc=="+courseTO.getHighlcrank());
		System.out.println("after higobh=="+courseTO.getHighobhrank());
		System.out.println("after higobx=="+courseTO.getHighobxrank());
		System.out.println("after higcommunity=="+courseTO.getHighcommunityrank());
		
		System.out.println("=========================="+ category+ " =============================");
		
		
			 System.out.println("after lccurrent=="+courseTO.getLccurrank());
				
			 System.out.println("after lcremain=="+courseTO.getLcremain());
				
			 System.out.println("after higlc=="+courseTO.getHighlcrank());
		 
		 
		
		
		//checking all caste and gen over conditions
		
		//general over
		 if (courseTO.getGencurrank() >= courseTO.getHighgenrank() || courseTO.getGenremain() == 0)
		 {
				courseTO.setGenover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //sc over
		 if(courseTO.getGencurrank() >= courseTO.getHighscrank() || courseTO.getScremain() == 0 || courseTO.getSccurrank() >= courseTO.getHighscrank())
		 {
				courseTO.setScover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //st over
		 if(courseTO.getGencurrank() >= courseTO.getHighstrank() || courseTO.getStremain() == 0 || courseTO.getStcurrank() >= courseTO.getHighstrank())
		 {
				courseTO.setStover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		
		 //bc over
		 if(courseTO.getGencurrank() >= courseTO.getHighbcrank() || courseTO.getBcremain() == 0 || courseTO.getBccurrank() >= courseTO.getHighbcrank())
		 {
				courseTO.setBcover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		 //mu over
		 if(courseTO.getGencurrank() >= courseTO.getHighmurank() || courseTO.getMuremain() == 0 || courseTO.getMucurrank() >= courseTO.getHighmurank())
		 {
				courseTO.setMuover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		
		 //obh over
		 if(courseTO.getGencurrank() >= courseTO.getHighobhrank() || courseTO.getObhremain() == 0 || courseTO.getObhcurrank() >= courseTO.getHighobhrank())
		 {
				courseTO.setObhover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
		 //obx over
		 if(courseTO.getGencurrank() >= courseTO.getHighobxrank() || courseTO.getObxremain() == 0 || courseTO.getObxcurrank() >= courseTO.getHighobxrank() )
		 {
				courseTO.setObxover(true);
				//update course map
				courseMap.put(courseTO.getId(), courseTO);
		 }
		 
			 //lc over
			 if(courseTO.getGencurrank() >= courseTO.getHighlcrank() || courseTO.getLcremain() == 0 || courseTO.getLccurrank() >= courseTO.getHighlcrank())
			 {
					courseTO.setLcover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 }
			
			//community over
			 if(courseTO.getGencurrank() >= courseTO.getHighlcrank() || courseTO.getCommunityremain() == 0 || courseTO.getCommunityremain() >= courseTO.getHighcommunityrank())
			 {
					courseTO.setCommunityover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 } 
			 if(courseTO.getGencurrank() >= courseTO.getHighsebcrank() || courseTO.getSebcremain() == 0 || courseTO.getSebccurrank() >= courseTO.getHighsebcrank())
			 {
					courseTO.setScover(true);
					//update course map
					courseMap.put(courseTO.getId(), courseTO);
			 }
		 
		
		
		
		}




		
		
		// add students category wise to allotment list and map
		/**
		 * @param genrankList
		 * @param allotmentMap
		 */
		public void deleteAllotmentlist(List<StudentCourseAllotment> genrankList,Map<Integer,StudentCourseAllotment> allotmentMap,String category ,CourseTO courseTO)
		{
		// delete obx list
		Iterator<StudentCourseAllotment> delobxIterator=genrankList.iterator();
		while(delobxIterator.hasNext())
		{	
			
			//AdmAppln a=null;
			StudentCourseAllotment obxCourseAllotment =  delobxIterator.next();
			
			// check map contains student or not
			if(allotmentMap.containsKey(obxCourseAllotment.getAdmAppln().getId()))
			{
				
				// for general list
				if(category.equalsIgnoreCase("General"))
				{
					
					StudentCourseAllotment  allotment=allotmentMap.get(obxCourseAllotment.getAdmAppln().getId());
					
					// check map contain student allotlist student have different preferenece delete student from list
					if(allotment.getPrefNo()!=obxCourseAllotment.getPrefNo() )
					{
					
						genrankList.remove(obxCourseAllotment);
						//update general details
						courseTO.setGenremain(courseTO.getGenremain()+1);
						courseTO.setGenover(false);
					}
					
				}
				// for caste list
				else
				{
					
					StudentCourseAllotment  allotment=allotmentMap.get(obxCourseAllotment.getAdmAppln().getId());
					
					// check map contain student allotlist student have same preferenece delete student from list
					if(allotment.getPrefNo()==obxCourseAllotment.getPrefNo() )
					{
					
						genrankList.remove(obxCourseAllotment);
						//update general details
						courseTO.setScremain(courseTO.getScremain()+1);
						courseTO.setScover(false);
					}
					
					
				}
				
			}// close allotment contains
		}// delete allotment list while close 
		}
		public boolean getIsAllotmentOver(Integer year, Integer prgmType) throws ApplicationException {
			IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
			Map<Integer,StudentCourseAllotment> allotmentMap=txn.getallotmentMap(prgmType,year);
			if(allotmentMap.size()==0){
				return false;	
			}else{
				return true;
			}
			
		}
		public boolean generateFirstChanceMemo(Integer year, Integer pgmType,ApplicationEditForm form) throws Exception, ApplicationException {
			//form.setChanceNo(1);
			boolean result = false;
			IApplicationEditTransaction txn=ApplicationEditTransactionimpl.getInstance();
			// create new 	allotmentMap with student id and StudentCourseAllotment object for storing db
			Map<Integer,StudentCourseAllotment> allotmentMap=txn.getallotmentMap(Integer.parseInt(form.getProgramTypeId()),Integer.parseInt(form.getAcademicYear()));
			List<StudentCourseChanceMemo> memoList = new LinkedList<StudentCourseChanceMemo>();
//			Map<Integer,CourseTO> courseMap=new HashMap<Integer,CourseTO>();
//			Map<Integer,List<StudentRank>> genRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteSCRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteSTRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteBCRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteOBXRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteOBHRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteMURankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteLCRankMap=new HashMap<Integer,List<StudentRank>>();
//			Map<Integer,List<StudentRank>> casteCOMMUNITYRankMap=new HashMap<Integer,List<StudentRank>>();
			
			//get all courses
			List<Course> courseList=CourseTransactionImpl.getInstance().getCourses(Integer.parseInt(form.getProgramTypeId()));
		
//			int courseSize=0;
//			courseSize=courseList.size();
		
			//convert course bos to tos list
			List<CourseTO> courseToList = new ArrayList<CourseTO>();
			if(form.getChanceNo()==1){
				courseToList=ApplicationRankHelper.getInstance().copyCourseBoTOForChanceMemo(courseList, form);
			}
			else if(form.getChanceNo()>1){
				courseToList=ApplicationRankHelper.getInstance().copyCourseBoTOForMultipleChanceMemo(courseList, form);
			}
			
			Iterator<CourseTO> courseIterator=courseToList.iterator();	
			// start first course loop for fill caste seats
			while(courseIterator.hasNext())
			{				
				CourseTO courseTO=courseIterator.next();
				if(courseTO.getId()==27){
					System.out.println("");
				}

				//getting rank list on Course and General rank
				List<StudentRank> rankListOnCourseGeneral=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"GENERAL");// check Course and caste rank list size is empty
				// check st setas zero or not, if zero stop course
				if(rankListOnCourseGeneral!=null && rankListOnCourseGeneral.size()!=0 && courseTO.getGenChance()!=null)
				{
					// add general allotment list and map
					firstChanceMemo(rankListOnCourseGeneral, "General",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getGenChance(),courseTO.getGencurrank());			
				}// general ranklist size close


				// check Course and caste SC rank list size is empty
				List<StudentRank> rankListOnCourseCasteSC=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SC");

				// check sc setas zero or not, if zero stop course
				if(rankListOnCourseCasteSC!=null && rankListOnCourseCasteSC.size()!=0 && courseTO.getScChance()!=null)
				{	 
					// add sc allotment list and map

					firstChanceMemo(rankListOnCourseCasteSC, "SC",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getScChance(),courseTO.getSccurrank());
				}// caste ranklist size close



				// check Course and caste ST rank list size is empty
				List<StudentRank> rankListOnCourseCasteST=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"ST");

				// check st setas zero or not, if zero stop course
				if(rankListOnCourseCasteST!=null && rankListOnCourseCasteST.size()!=0 && courseTO.getStChance()!=null)
				{	 
					// add st allotment list and map
					firstChanceMemo(rankListOnCourseCasteST, "ST",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getStChance(),courseTO.getStcurrank());
				}// caste ranklist size close
				
				// check Course and caste ST rank list size is empty
				List<StudentRank> rankListOnCourseCasteSEBC=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"SEBC");

				// check st setas zero or not, if zero stop course
				if(rankListOnCourseCasteSEBC!=null && rankListOnCourseCasteSEBC.size()!=0 && courseTO.getStChance()!=null)
				{	 
					// add st allotment list and map
					firstChanceMemo(rankListOnCourseCasteSEBC, "SEBC",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getSebcChance(),courseTO.getSebccurrank());
				}// caste ranklist size close
				
				


				if(courseTO.getId()==12 || courseTO.getId()==13 || courseTO.getId()==14 || courseTO.getId()==15 || courseTO.getId()==16 || courseTO.getId()==29 || courseTO.getId()==30 || courseTO.getId()==31)
				{	 
					// check Course and caste LC rank list size is empty
					List<StudentRank> rankListOnCourseCasteLC=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"LCTA");

					// check lc setas zero or not, if zero stop course
					if(rankListOnCourseCasteLC!=null && rankListOnCourseCasteLC.size()!=0 && courseTO.getLcChance()!=null)
					{	 
						// add lc allotment list and map
						firstChanceMemo(rankListOnCourseCasteLC, "LCTA",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getLcChance(),courseTO.getLctacurrank());
					}// caste ranklist size close

				}else{	 
					// check Course and caste LC rank list size is empty
					List<StudentRank> rankListOnCourseCasteLCTA=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"LC");

					// check lc setas zero or not, if zero stop course
					if(rankListOnCourseCasteLCTA!=null && rankListOnCourseCasteLCTA.size()!=0 && courseTO.getLcChance()!=null)
					{	 
						// add lc allotment list and map
						firstChanceMemo(rankListOnCourseCasteLCTA, "LC",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getLcChance(),courseTO.getLccurrank());
					}// caste ranklist size close

				}


				// check Course and caste BC rank list size is empty
				List<StudentRank> rankListOnCourseCasteBC=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"BC");


				if(rankListOnCourseCasteBC!=null && rankListOnCourseCasteBC.size()!=0 && courseTO.getBcChance()!=null)
				{	 
					// add bc allotment list and map
					firstChanceMemo(rankListOnCourseCasteBC, "BC",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getBcChance(),courseTO.getBccurrank());
				}// caste ranklist size close


				// check Course and caste MU rank list size is empty
				List<StudentRank> rankListOnCourseCasteMU=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"MU");

				if(rankListOnCourseCasteMU!=null && rankListOnCourseCasteMU.size()!=0)
				{	 
					// add mu allotment list and map
					firstChanceMemo(rankListOnCourseCasteMU, "MU",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getMuChance(),courseTO.getMucurrank());
				}// caste ranklist size close


				// check Course and caste OBH rank list size is empty
				List<StudentRank> rankListOnCourseCasteOBH=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBH");

				if(rankListOnCourseCasteOBH!=null && rankListOnCourseCasteOBH.size()!=0 && courseTO.getObhChance()!=null)
				{	 
					// add obh allotment list and map
					firstChanceMemo(rankListOnCourseCasteOBH, "OBH",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getObhChance(),courseTO.getObhcurrank());
				}// caste ranklist size close

				// OBH caste allot over


				// check Course and caste OBX rank list size is empty
				List<StudentRank> rankListOnCourseCasteOBX=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"OBX");
				// check obx setas zero or not, if zero stop course

				if(rankListOnCourseCasteOBX!=null && rankListOnCourseCasteOBX.size()!=0 && courseTO.getObxChance()!=null)
				{	 
					// add obx allotment list and map
					firstChanceMemo(rankListOnCourseCasteOBX, "OBX",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getObxChance(),courseTO.getObxcurrank());
				}// caste ranklist size close



				// check Course and caste COMMUNITY rank list size is empty
				List<StudentRank> rankListOnCourseCasteCOMMUNITY=txn.getAdmApplonStudentOnCourseCategoryForChanceMemo(courseTO.getId(), Integer.parseInt(form.getAcademicYear()),"COMMUNITY");
				// check obx setas zero or not, if zero stop course
				if(rankListOnCourseCasteCOMMUNITY!=null && rankListOnCourseCasteCOMMUNITY.size()!=0 && courseTO.getCommunityChance()!=null)
				{	 
					// add obx allotment list and map
					firstChanceMemo(rankListOnCourseCasteCOMMUNITY, "COMMUNITY",allotmentMap,memoList,courseTO,form.getChanceNo(),form,courseTO.getCommunityChance(),courseTO.getCommunitycurrank());
				}// caste ranklist size close



			}// end of course iteration
			
			if(memoList!=null && memoList.size()>0){
				 result = txn.saveChanceMemo(memoList);
			 }
			
			
			return result;
		}
		
		private void firstChanceMemo(List<StudentRank> rankListOnCourseGeneral,
									 String casteOrGen,
									 Map<Integer, StudentCourseAllotment> allotmentMap,
									 List<StudentCourseChanceMemo> memoList, 
									 CourseTO courseTO,
									 Integer chanceNo, 
									 ApplicationEditForm form, 
									 Integer chance,
									 Integer currentRank) {
			try{
	          Iterator<StudentRank> itr = rankListOnCourseGeneral.iterator();
	          Integer Rank = 1;
	          while (itr.hasNext()) {
				StudentRank currRank = (StudentRank) itr.next();
				if(chance.intValue()>0 && currRank.getRank().intValue()>currentRank.intValue() && Rank<=chance.intValue()){//checking chance limit and rank limit
					
					StudentCourseChanceMemo chanceMemoStudent = null;
					/*if(allotmentMap.containsKey(currRank.getAdmAppln().getId())){
						
						StudentCourseAllotment allotStudent = allotmentMap.get(currRank.getAdmAppln().getId());
						if(allotStudent.getPrefNo().intValue()>currRank.getPrefNo().intValue()){//checking preference priority
							chanceMemoStudent = copyPropertValuesChanceMemo(currRank,Rank,chanceNo,casteOrGen,form);	
						}
					}else{
						chanceMemoStudent = copyPropertValuesChanceMemo(currRank,Rank,chanceNo,casteOrGen,form);
					}*/
					chanceMemoStudent = copyPropertValuesChanceMemo(currRank,Rank,chanceNo,casteOrGen,form);
				if(chanceMemoStudent!=null){
					 memoList.add(chanceMemoStudent);	
					 Rank++;
				}
				  
				}
				
	          }
			}catch (Exception e) {
				System.out.println(e);
			}	
		}
		
		private StudentCourseChanceMemo copyPropertValuesChanceMemo(
				StudentRank currRank, Integer rank, Integer chanceNo, String casteOrGen, ApplicationEditForm form) {
			StudentCourseChanceMemo chanceStudent = new StudentCourseChanceMemo();
			chanceStudent.setActive(true);
			chanceStudent.setAdmAppln(currRank.getAdmAppln());
			chanceStudent.setIsAppeared(false);
			chanceStudent.setChanceNo(chanceNo);
			chanceStudent.setCourse(currRank.getCourse());
			chanceStudent.setCreatedBy(form.getUserId());
			chanceStudent.setCreatedDate(new Date());
			chanceStudent.setLastModifiedDate(new Date());
			chanceStudent.setModifiedBy(form.getUserId());
			chanceStudent.setIndexMark(currRank.getIndexMark());
			chanceStudent.setIsAssigned(false);
			chanceStudent.setChanceRank(rank);
			chanceStudent.setIsCancel(false);
			if(casteOrGen.equalsIgnoreCase("General")){
				chanceStudent.setIsGeneral(true);
			    chanceStudent.setIsCaste(false);
			    chanceStudent.setIsCommunity(false);
			}
			else if(casteOrGen.equalsIgnoreCase("COMMUNITY")){
				chanceStudent.setIsCommunity(true);
				chanceStudent.setIsGeneral(false);
			    chanceStudent.setIsCaste(false);
			}
			else{
				chanceStudent.setIsCaste(true);
				chanceStudent.setIsGeneral(false);
				chanceStudent.setIsCommunity(false);			    
			}
			chanceStudent.setIsSatisfied(false);
			chanceStudent.setPrefNo(currRank.getPrefNo());
			chanceStudent.setRank(currRank.getRank());
			return chanceStudent;
		}
		
	

		private List<CourseTO> copyCourseBoTOForMultipleChanceMemo(
				List<Course> courseList, ApplicationEditForm admForm) {
			
			List list=null;
			Session session = null;
			Query hqlQuery=null;
			Properties prop = new Properties();
			try 
			{
				InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
				prop.load(in);
				session = HibernateUtil.getSession();
				
			} 
			catch (FileNotFoundException e)
			{
				log.error("Unable to read properties file...", e);
			} 
			catch (IOException e)
			{
				log.error("Unable to read properties file...", e);
			}
			catch (Exception e)
			{
				log.error("Unable to read properties file...", e);
			}
       
			List<CourseTO> courseToList=new LinkedList<CourseTO>();
			 try{
			Iterator<Course> it= courseList.iterator();
			
			while(it.hasNext())
			{
				//sc
				Integer scId=0;	
				String sc = prop.getProperty ("knowledgepro.admission.admittedthrough.sc");
				if(sc !=null && ! StringUtils.isEmpty(sc)){ 
					scId=Integer.parseInt(sc);
				}
				
				//ezhava_thiyya_billava
				Integer ezhava_thiyya_billava_Id=0;	
				String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.ezhava_thiyya_billava");
				if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
					ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
				}
				
				//mu
				Integer muId=0;	
				String mu = prop.getProperty ("knowledgepro.admission.admittedthrough.mu");
				if(mu !=null && ! StringUtils.isEmpty(mu)){ 
					muId=Integer.parseInt(mu);
				}
				
				//st
				Integer stId=0;	
				String st = prop.getProperty ("knowledgepro.admission.admittedthrough.st");
				if(st !=null && ! StringUtils.isEmpty(st)){ 
					stId=Integer.parseInt(st);
				}

				//obh
				Integer obhId=0;	
				String obh = prop.getProperty ("knowledgepro.admission.admittedthrough.obh");
				if(obh !=null && ! StringUtils.isEmpty(obh)){ 
					obhId=Integer.parseInt(obh);
				}
				
				//obx
				Integer obxId=0;	
				String obx = prop.getProperty ("knowledgepro.admission.admittedthrough.obx");
				if(obx !=null && ! StringUtils.isEmpty(obx)){ 
					obxId=Integer.parseInt(obx);
				}
				
				//lc
				Integer lcId=0;	
				String lc = prop.getProperty ("knowledgepro.admission.admittedthrough.lc");
				if(lc !=null && ! StringUtils.isEmpty(lc)){ 
					lcId=Integer.parseInt(lc);
				}
				
				//lcta
				Integer lctaId=0;	
				String lcta = prop.getProperty ("knowledgepro.admission.admittedthrough.lcta");
				if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
					lctaId=Integer.parseInt(lcta);
				}
				
				//general
				Integer generalId=0;	
				String general = prop.getProperty ("knowledgepro.admission.admittedthrough.general");
				if(general !=null && ! StringUtils.isEmpty(general)){ 
					generalId=Integer.parseInt(general);
				}
				
				//community
				Integer communityId=0;	
				String community = prop.getProperty ("knowledgepro.admission.admittedthrough.community");
				if(general !=null && ! StringUtils.isEmpty(community)){ 
					communityId=Integer.parseInt(community);
				}
				
				//=========================================================================================
				//sc
				Integer scIdr=0;	
				String scr = prop.getProperty ("knowledgepro.admission.religionsection.sc");
				if(scr !=null && ! StringUtils.isEmpty(scr)){ 
					scIdr=Integer.parseInt(scr);
				}
				
				//ezhava_thiyya_billava
				Integer ezhava_thiyya_billava_Idr=0;	
				String ezhava_thiyya_billavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
				if(ezhava_thiyya_billavar !=null && ! StringUtils.isEmpty(ezhava_thiyya_billavar)){ 
					ezhava_thiyya_billava_Idr=Integer.parseInt(ezhava_thiyya_billavar);
				}
				
				//mu
				Integer muIdr=0;	
				String mur = prop.getProperty ("knowledgepro.admission.religionsection.mu");
				if(mur !=null && ! StringUtils.isEmpty(mur)){ 
					muIdr=Integer.parseInt(mur);
				}
				
				//st
				Integer stIdr=0;	
				String str = prop.getProperty ("knowledgepro.admission.religionsection.st");
				if(str !=null && ! StringUtils.isEmpty(str)){ 
					stIdr=Integer.parseInt(str);
				}
				
				//lc
				Integer lcIdr=0;	
				String lcr = prop.getProperty ("knowledgepro.admission.religionsection.lc");
				if(lcr !=null && ! StringUtils.isEmpty(lcr)){ 
					lcIdr=Integer.parseInt(lcr);
				}
				
				//lcta
				Integer lctaIdr=0;	
				String lctar = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
				if(lctar !=null && ! StringUtils.isEmpty(lctar)){ 
					lctaIdr=Integer.parseInt(lctar);
				}
				
				//obh
				Integer obhIdr=0;	
				String obhr = prop.getProperty ("knowledgepro.admission.religionsection.obh");
				if(obhr !=null && ! StringUtils.isEmpty(obhr)){ 
					obhIdr=Integer.parseInt(obhr);
				}
				
				//obx
				Integer obxIdr=0;	
				String obxr = prop.getProperty ("knowledgepro.admission.religionsection.obx");
				if(obxr !=null && ! StringUtils.isEmpty(obxr)){ 
					obxIdr=Integer.parseInt(obxr);
				}
				
				//ezhava
				Integer ezhavaIdr=0;	
				String ezhavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
				if(ezhavar !=null && ! StringUtils.isEmpty(ezhavar)){ 
					ezhavaIdr=Integer.parseInt(ezhavar);
				}
				

				//billava
				Integer billavaIdr=0;	
				String billavar = prop.getProperty ("knowledgepro.admission.religionsection.billava");
				if(billavar !=null && ! StringUtils.isEmpty(billavar)){ 
					billavaIdr=Integer.parseInt(billavar);
				}
				
				//thiyya
				Integer thiyyaIdr=0;	
				String thiyyar = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
				if(thiyyar !=null && ! StringUtils.isEmpty(thiyyar)){ 
					thiyyaIdr=Integer.parseInt(thiyyar);
				}
				
				//community
				Integer communityIdr=0;	
				String communityr = prop.getProperty ("knowledgepro.admission.religionsection.community");
				if(communityr !=null && ! StringUtils.isEmpty(communityr)){ 
					communityIdr=Integer.parseInt(communityr);
				}
	
				CourseTO  courseTo=new CourseTO();
				int totcount=0;
				
				Course course=it.next();
				Set<SeatAllocation> seatAllocation= course.getSeatAllocations();
				Iterator<SeatAllocation> seatIterator=seatAllocation.iterator();
										
				while(seatIterator.hasNext())
				{					
					SeatAllocation seat=seatIterator.next();
					//GENERAL
					if(seat.getAdmittedThrough().getId()==generalId)
					{
						courseTo.setGenseat(seat.getNoOfSeats());
						courseTo.setGenChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where  s.course.id=:courseId and s.isGeneral=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+" order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setGencurrank(altm.getRank());
						}else{
							courseTo.setGencurrank(0);   	
						}							
					}
						
					//SC
					else if(seat.getAdmittedThrough().getId()==scId)
					{
						courseTo.setScseat(seat.getNoOfSeats());
						courseTo.setScChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",scIdr);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setSccurrank(altm.getRank());
						}else{
							courseTo.setSccurrank(0);   	
						}
					}
					//ST
					else if(seat.getAdmittedThrough().getId()==stId)
					{
						courseTo.setStseat(seat.getNoOfSeats());
						courseTo.setStChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",stIdr);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setStcurrank(altm.getRank());
						}else{
							courseTo.setStcurrank(0);   	
						}
					}
					//OBC
					else if(seat.getAdmittedThrough().getId()==ezhava_thiyya_billava_Id)
					{
						courseTo.setBcseat(seat.getNoOfSeats());
						courseTo.setBcChance(seat.getChanceMemoLimit());
						//getting alloted seats
						String s=""+ezhavaIdr+","+thiyyaIdr+","+billavaIdr;
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id in("+s+") and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						//hqlQuery.setInteger("casteId",ezhava_thiyya_billava_Id);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setBccurrank(altm.getRank());
						}else{
							courseTo.setBccurrank(0);   	
						}
					}
					//OBX
					else if(seat.getAdmittedThrough().getId()==obxId)
					{
						courseTo.setObxseat(seat.getNoOfSeats());
						courseTo.setObxChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",obxIdr);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setObxcurrank(altm.getRank());
						}
						else{
							courseTo.setObxcurrank(0);   	
						}
					}
					//OBH
					else if(seat.getAdmittedThrough().getId()==obhId){
						courseTo.setObhseat(seat.getNoOfSeats());
						courseTo.setObhChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",obhIdr);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setObhcurrank(altm.getRank());
						}else{
							courseTo.setObhcurrank(0);   	
						}
					}
					//MU
					else if(seat.getAdmittedThrough().getId()==muId)
					{
						courseTo.setMuseat(seat.getNoOfSeats());
						courseTo.setMuChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",muIdr);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setMucurrank(altm.getRank());
						}else{
							courseTo.setMucurrank(0);   	
						}
					}
						 						 
					else if(seat.getAdmittedThrough().getId()==lcId)
					{
						courseTo.setLcseat(seat.getNoOfSeats());
						courseTo.setLcChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData. and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",lcIdr);
					}
					
					else if(seat.getAdmittedThrough().getId()==communityId)
					{
						courseTo.setCommunityseat(seat.getNoOfSeats());
						courseTo.setCommunityChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.isCommunity=1 and s.course.id=:courseId and s.isCommunity=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
 						hqlQuery.setInteger("courseId",course.getId());
						//hqlQuery.setInteger("casteId",communityIdr);
 						list=hqlQuery.list();
 						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setCommunitycurrank(altm.getRank());
						}else{
							courseTo.setCommunitycurrank(0);   	
						}	
					}	
					
					//SEBC
					else if(seat.getAdmittedThrough().getId()==20)
					{
						courseTo.setSebcseat(seat.getNoOfSeats());
						courseTo.setSebcChance(seat.getChanceMemoLimit());
						//getting alloted seats
						hqlQuery = session.createQuery("from StudentCourseChanceMemo s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
						hqlQuery.setInteger("courseId",course.getId());
						hqlQuery.setInteger("casteId",9);
						list=hqlQuery.list();
						if(list!=null && list.size()>0){
							StudentCourseChanceMemo altm = (StudentCourseChanceMemo) list.get(list.size()-1);
						    courseTo.setSebccurrank(altm.getRank());
						}else{
							courseTo.setSebccurrank(0);   	
						}
					}
						 
						  
						 	
					}// seat while close
					
					courseTo.setTotcount(totcount);
					courseTo.setTotseats(course.getMaxIntake());
					courseTo.setId(course.getId());
					courseTo.setName(course.getName());
					
					
					courseToList.add(courseTo);
					
				}
	        }catch (Exception e) {
				System.out.println(e);
			}
		
			return courseToList;
			
			}
		
		public List<CourseTO> copyCourseBoTOForChanceMemo(List<Course> courseList,ApplicationEditForm admForm) 
		{

			List list=null;
			Session session = null;
			Query hqlQuery=null;
			Properties prop = new Properties();
			try 
			{
				InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("resources/application.properties");
				prop.load(in);
				session = HibernateUtil.getSession();

			} 
			catch (FileNotFoundException e)
			{
				log.error("Unable to read properties file...", e);

			} 
			catch (IOException e)
			{
				log.error("Unable to read properties file...", e);

			}
			catch (Exception e)
			{
				log.error("Unable to read properties file...", e);

			}




			List<CourseTO> courseToList=new LinkedList<CourseTO>();
			try{
				Iterator<Course> it= courseList.iterator();

				while(it.hasNext())
				{




					//sc
					Integer scId=0;	
					String sc = prop.getProperty ("knowledgepro.admission.admittedthrough.sc");
					if(sc !=null && ! StringUtils.isEmpty(sc)){ 
						scId=Integer.parseInt(sc);
					}

					//ezhava_thiyya_billava
					Integer ezhava_thiyya_billava_Id=0;	
					String ezhava_thiyya_billava = prop.getProperty ("knowledgepro.admission.ezhava_thiyya_billava");
					if(ezhava_thiyya_billava !=null && ! StringUtils.isEmpty(ezhava_thiyya_billava)){ 
						ezhava_thiyya_billava_Id=Integer.parseInt(ezhava_thiyya_billava);
					}

					//mu
					Integer muId=0;	
					String mu = prop.getProperty ("knowledgepro.admission.admittedthrough.mu");
					if(mu !=null && ! StringUtils.isEmpty(mu)){ 
						muId=Integer.parseInt(mu);
					}

					//st
					Integer stId=0;	
					String st = prop.getProperty ("knowledgepro.admission.admittedthrough.st");
					if(st !=null && ! StringUtils.isEmpty(st)){ 
						stId=Integer.parseInt(st);
					}



					//obh
					Integer obhId=0;	
					String obh = prop.getProperty ("knowledgepro.admission.admittedthrough.obh");
					if(obh !=null && ! StringUtils.isEmpty(obh)){ 
						obhId=Integer.parseInt(obh);
					}

					//obx
					Integer obxId=0;	
					String obx = prop.getProperty ("knowledgepro.admission.admittedthrough.obx");
					if(obx !=null && ! StringUtils.isEmpty(obx)){ 
						obxId=Integer.parseInt(obx);
					}

					//lc
					Integer lcId=0;	
					String lc = prop.getProperty ("knowledgepro.admission.admittedthrough.lc");
					if(lc !=null && ! StringUtils.isEmpty(lc)){ 
						lcId=Integer.parseInt(lc);
					}

					//lcta
					Integer lctaId=0;	
					String lcta = prop.getProperty ("knowledgepro.admission.admittedthrough.lcta");
					if(lcta !=null && ! StringUtils.isEmpty(lcta)){ 
						lctaId=Integer.parseInt(lcta);
					}

					//general
					Integer generalId=0;	
					String general = prop.getProperty ("knowledgepro.admission.admittedthrough.general");
					if(general !=null && ! StringUtils.isEmpty(general)){ 
						generalId=Integer.parseInt(general);
					}

					//community
					Integer communityId=0;	
					String community = prop.getProperty ("knowledgepro.admission.admittedthrough.community");
					if(general !=null && ! StringUtils.isEmpty(community)){ 
						communityId=Integer.parseInt(community);
					}

					//=========================================================================================


					//sc
					Integer scIdr=0;	
					String scr = prop.getProperty ("knowledgepro.admission.religionsection.sc");
					if(scr !=null && ! StringUtils.isEmpty(scr)){ 
						scIdr=Integer.parseInt(scr);
					}

					//ezhava_thiyya_billava
					Integer ezhava_thiyya_billava_Idr=0;	
					String ezhava_thiyya_billavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava_thiyya_billava");
					if(ezhava_thiyya_billavar !=null && ! StringUtils.isEmpty(ezhava_thiyya_billavar)){ 
						ezhava_thiyya_billava_Idr=Integer.parseInt(ezhava_thiyya_billavar);
					}

					//mu
					Integer muIdr=0;	
					String mur = prop.getProperty ("knowledgepro.admission.religionsection.mu");
					if(mur !=null && ! StringUtils.isEmpty(mur)){ 
						muIdr=Integer.parseInt(mur);
					}

					//st
					Integer stIdr=0;	
					String str = prop.getProperty ("knowledgepro.admission.religionsection.st");
					if(str !=null && ! StringUtils.isEmpty(str)){ 
						stIdr=Integer.parseInt(str);
					}

					//lc
					Integer lcIdr=0;	
					String lcr = prop.getProperty ("knowledgepro.admission.religionsection.lc");
					if(lcr !=null && ! StringUtils.isEmpty(lcr)){ 
						lcIdr=Integer.parseInt(lcr);
					}

					//lcta
					Integer lctaIdr=0;	
					String lctar = prop.getProperty ("knowledgepro.admission.religionsection.lcta");
					if(lctar !=null && ! StringUtils.isEmpty(lctar)){ 
						lctaIdr=Integer.parseInt(lctar);
					}

					//obh
					Integer obhIdr=0;	
					String obhr = prop.getProperty ("knowledgepro.admission.religionsection.obh");
					if(obhr !=null && ! StringUtils.isEmpty(obhr)){ 
						obhIdr=Integer.parseInt(obhr);
					}

					//obx
					Integer obxIdr=0;	
					String obxr = prop.getProperty ("knowledgepro.admission.religionsection.obx");
					if(obxr !=null && ! StringUtils.isEmpty(obxr)){ 
						obxIdr=Integer.parseInt(obxr);
					}

					//ezhava
					Integer ezhavaIdr=0;	
					String ezhavar = prop.getProperty ("knowledgepro.admission.religionsection.ezhava");
					if(ezhavar !=null && ! StringUtils.isEmpty(ezhavar)){ 
						ezhavaIdr=Integer.parseInt(ezhavar);
					}


					//billava
					Integer billavaIdr=0;	
					String billavar = prop.getProperty ("knowledgepro.admission.religionsection.billava");
					if(billavar !=null && ! StringUtils.isEmpty(billavar)){ 
						billavaIdr=Integer.parseInt(billavar);
					}

					//thiyya
					Integer thiyyaIdr=0;	
					String thiyyar = prop.getProperty ("knowledgepro.admission.religionsection.thiyya");
					if(thiyyar !=null && ! StringUtils.isEmpty(thiyyar)){ 
						thiyyaIdr=Integer.parseInt(thiyyar);
					}

					//community
					Integer communityIdr=0;	
					String communityr = prop.getProperty ("knowledgepro.admission.religionsection.community");
					if(communityr !=null && ! StringUtils.isEmpty(communityr)){ 
						communityIdr=Integer.parseInt(communityr);
					}







					CourseTO  courseTo=new CourseTO();
					int totcount=0;

					Course course=it.next();
					Set<SeatAllocation> seatAllocation= course.getSeatAllocations();
					Iterator<SeatAllocation> seatIterator=seatAllocation.iterator();
					if(course.getId()==5){
						System.out.println("");
					}

					while(seatIterator.hasNext())
					{

						SeatAllocation seat=seatIterator.next();
						//GENERAL
						if(seat.getAdmittedThrough().getId()==generalId)
						{
							courseTo.setGenseat(seat.getNoOfSeats());
							courseTo.setGenChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where  s.course.id=:courseId and s.isGeneral=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+" order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setGencurrank(altm.getRank());
							}else{
								courseTo.setGencurrank(0);   	
							}

						}

						//SC
						else if(seat.getAdmittedThrough().getId()==scId)
						{
							courseTo.setScseat(seat.getNoOfSeats());
							courseTo.setScChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1  and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",scIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setSccurrank(altm.getRank());
							}else{
								courseTo.setSccurrank(0);   	
							}
						}
						//ST
						else if(seat.getAdmittedThrough().getId()==stId)
						{
							courseTo.setStseat(seat.getNoOfSeats());
							courseTo.setStChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",stIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setStcurrank(altm.getRank());
							}else{
								courseTo.setStcurrank(0);   	
							}
						}
						//OBC
						else if(seat.getAdmittedThrough().getId()==ezhava_thiyya_billava_Id)
						{
							courseTo.setBcseat(seat.getNoOfSeats());
							courseTo.setBcChance(seat.getChanceMemoLimit());
							//getting alloted seats
							String s=""+ezhavaIdr+","+thiyyaIdr+","+billavaIdr;
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id in("+s+") and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							//hqlQuery.setInteger("casteId",ezhava_thiyya_billava_Id);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setBccurrank(altm.getRank());
							}else{
								courseTo.setBccurrank(0);   	
							}
						}
						//OBX
						else if(seat.getAdmittedThrough().getId()==obxId)
						{
							courseTo.setObxseat(seat.getNoOfSeats());
							courseTo.setObxChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",obxIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setObxcurrank(altm.getRank());
							}else{
								courseTo.setObxcurrank(0);   	
							}
						}
						//OBH
						else if(seat.getAdmittedThrough().getId()==obhId){
							courseTo.setObhseat(seat.getNoOfSeats());
							courseTo.setObhChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",obhIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setObhcurrank(altm.getRank());
							}else{
								courseTo.setObhcurrank(0);   	
							}
						}
						//MU
						else if(seat.getAdmittedThrough().getId()==muId)
						{
							courseTo.setMuseat(seat.getNoOfSeats());
							courseTo.setMuChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",muIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setMucurrank(altm.getRank());
							}else{
								courseTo.setMucurrank(0);   	
							}
						}


						else if(seat.getAdmittedThrough().getId()==lcId)
						{
							courseTo.setLcseat(seat.getNoOfSeats());
							courseTo.setLcChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",lcIdr);
						}

						else if(seat.getAdmittedThrough().getId()==communityId)
						{
							courseTo.setCommunityseat(seat.getNoOfSeats());
							courseTo.setCommunityChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.isCommunity=1 and s.course.id=:courseId and s.isCommunity=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							//hqlQuery.setInteger("casteId",communityIdr);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setCommunitycurrank(altm.getRank());
							}else{
								courseTo.setCommunitycurrank(0);  	
							}

						}	
						
						//SEBC
						else if(seat.getAdmittedThrough().getId()==20)
						{
							courseTo.setSebcseat(seat.getNoOfSeats());
							courseTo.setSebcChance(seat.getChanceMemoLimit());
							//getting alloted seats
							hqlQuery = session.createQuery("from StudentCourseAllotment s where s.admAppln.personalData.religionSection.id=:casteId and s.course.id=:courseId and s.isCaste=1 and s.admAppln.appliedYear="+Integer.parseInt(admForm.getAcademicYear())+"order by rank asc");
							hqlQuery.setInteger("courseId",course.getId());
							hqlQuery.setInteger("casteId",9);
							list=hqlQuery.list();
							if(list!=null && list.size()>0){
								StudentCourseAllotment altm = (StudentCourseAllotment) list.get(list.size()-1);
								courseTo.setSebccurrank(altm.getRank());
							}else{
								courseTo.setSebccurrank(0);   	
							}
						}



					}// seat while close

					courseTo.setTotcount(totcount);
					courseTo.setTotseats(course.getMaxIntake());
					courseTo.setId(course.getId());
					courseTo.setName(course.getName());


					courseToList.add(courseTo);

				}
			}catch (Exception e) {
				System.out.println(e);
			}

			// TODO Auto-generated method stub
			return courseToList;

		}
		
	
	

}

