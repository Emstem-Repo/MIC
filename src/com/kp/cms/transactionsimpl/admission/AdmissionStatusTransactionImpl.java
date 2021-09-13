package com.kp.cms.transactionsimpl.admission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CertificateCourse;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.bo.admin.InterviewCardHistory;
import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admission.ApplnAcknowledgement;
import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.bo.admission.StudentAllotmentPGIDetails;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.to.admission.CertificateCourseTO;
import com.kp.cms.to.admission.StudentCertificateCourseTO;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class AdmissionStatusTransactionImpl implements IAdmissionStatusTransaction {
	
	private static final Log log = LogFactory.getLog(AdmissionStatusTransactionImpl.class);
	
	/**
	 * Passing application no. and gets the matching records from AdmAppln table which shows the admission status and also checks for the interview status
	 */
	
	public AdmAppln getInterviewResult(String applicationNo,int year) throws Exception{
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		AdmAppln admAppln=null;		
		try {
			//session =InitSessionFactory.getInstance().openSession();
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from AdmAppln adm where adm.applnNo=? and adm.appliedYear = " +
			" (select max(appliedYear) from AdmAppln sub where sub.applnNo = " + applicationNo + ")");
			query.setString(0,applicationNo);
			//query.setInteger(1, year);
			admAppln = (AdmAppln)query.uniqueResult();		
			log.info("End of getInterviewResult of AdmissionStatusTransactionImpl");
			return admAppln;	
		} catch (Exception e) {
			log.error("Exception ocured in getInterviewResult of AdmissionStatusTransactionImpl :" + e);
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}
	
	/**
	 * Passing application no. and gets the admission status from AdmAppln table
	 */
	
	public List<AdmAppln> getDetailsAdmAppln(String applicationNo)throws Exception {
		log.info("Inside of getDetailsAdmAppln of AdmissionStatusTransactionImpl");
		Session session = null;
		List<AdmAppln> admAppln;
		try {
			//session =InitSessionFactory.getInstance().openSession();
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from AdmAppln adm where adm.applnNo= " + applicationNo +  
					" and appliedYear = (select max(appliedYear) from AdmAppln sub where sub.applnNo = " + applicationNo + ")");
			admAppln = query.list();
			if(!admAppln.isEmpty())
			{
				return admAppln;
			}			
		} catch (Exception e) {
		log.error("Exception ocured in getDetailsAdmAppln of AdmissionStatusTransactionImpl :"+e);
			throw  new ApplicationException(e);
		} finally {
		if (session != null) {
			session.flush();
			//session.close();
		}
	}
		log.info("End of getDetailsAdmAppln of AdmissionStatusTransactionImpl");
		return admAppln;
	}
	public List<InterviewCard> getStudentsList(int applicationNo) throws Exception {
		log.info("entered getStudentsList.of AdmissionStatusTransactionImpl");
		Session session = null;
		Transaction transaction = null;
		try {
				//SessionFactory sessionFactory = InitSessionFactory.getInstance();
				//session = sessionFactory.openSession()
				session = HibernateUtil.getSession();
				transaction = session.beginTransaction();
				transaction.begin();
				 Query query = session.createQuery("from InterviewCard i where i.admAppln.applnNo = :applicationNo");
				 query.setInteger("applicationNo", applicationNo);
				 List<InterviewCard> interviewTypes=  query.list();
				 transaction.commit();
				session.flush();
//				//session.close();
				log.info("End getStudentsList.of AdmissionStatusTransactionImpl");
				return interviewTypes;
		} catch (Exception e) {
			if (session != null)
				transaction.rollback();
				session.flush();
				//session.close();
			log.error("Error while getting Student Details in AdmissionStatusTransactionImpl"+e);
			throw  new ApplicationException(e); 
		}
	}
	
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.IAdmissionStatusTransaction#getStudentAdmitCardDetails(int, int)
	 */
	public List<InterviewCard> getStudentAdmitCardDetails(int applicationNo, int interviewTypeId) throws Exception {
		log.info("entered getStudentAdmitCardDetails of AdmissionStatusTransactionImpl");
		Session session = null;
		try {
				//SessionFactory sessionFactory = InitSessionFactory.getInstance();
				//session = sessionFactory.openSession()
				session = HibernateUtil.getSession();
				List<InterviewCard> studentAdmitCardList = null;
				
				if(interviewTypeId != 0){
					Query query = session.createQuery("from InterviewCard interviewCard " +
							" where interviewCard.admAppln.applnNo = :applicationNo " +
							" and interviewCard.interview.interview.interviewProgramCourse.id = :interviewTypeId");
					query.setInteger("applicationNo", applicationNo);
					query.setInteger("interviewTypeId", interviewTypeId);
					studentAdmitCardList = query.list();
				}else{
					Query query = session.createQuery("from InterviewCard interviewCard " +
							" where interviewCard.admAppln.applnNo = :applicationNo");
					query.setInteger("applicationNo", applicationNo);
					studentAdmitCardList = query.list();
				}
				session.flush();
//				//session.close();
				log.info("End getStudentAdmitCardDetails of AdmissionStatusTransactionImpl");
				return studentAdmitCardList;
		} catch (Exception e) {
			if (session != null)
				session.flush();
				//session.close();
			log.error("Error while getting getStudentAdmitCardDetails in AdmissionStatusTransactionImpl"+e);
			throw  new ApplicationException(e); 
		}
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.IAdmissionStatusTransaction#getApplnAcknowledgement(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean getApplnAcknowledgement(String applicationNo, String dateOfBirth) throws Exception {
		Session session=null;
		boolean exists = false;
		try{
			session = HibernateUtil.getSession();
			String quer= "from ApplnAcknowledgement apln where apln.applnNo="+applicationNo+" and apln.dateOfBirth='"+CommonUtil.ConvertStringToSQLDate(dateOfBirth)+"'";
			Query query = session.createQuery(quer);
			ApplnAcknowledgement appln = (ApplnAcknowledgement)query.uniqueResult();
			if(appln!=null)
				exists=true;
		}catch(Exception exp){
			throw new BusinessException(exp);
		}
		return exists;
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.admission.IAdmissionStatusTransaction#getCandidateDetails(com.kp.cms.forms.admission.AdmissionStatusForm)
	 */
	@Override
	public CandidatePGIDetails getCandidateDetails(
			AdmissionStatusForm admissionStatusForm) throws Exception {
		Session session = null;
		CandidatePGIDetails details;
		try{
			session = HibernateUtil.getSession();
			String query = "from CandidatePGIDetails c " +
					" where c.txnRefNo is not null and c.admAppln.applnNo=" +admissionStatusForm.getApplicationNo();
			details = (CandidatePGIDetails)session.createQuery(query).uniqueResult();
		}catch (Exception e) {
			log.error("Error while getting applicant details..." + e);
			throw new ApplicationException(e);
		}finally {
			if (session != null) {
				session.flush();
//				session.close();
			}
		}
		return details;
	}

	@Override
	public List<InterviewCardHistory> getStudentAdmitCardHistoryDetails( int applicationNo) throws Exception {
		Session session = null;
		try {
				session = HibernateUtil.getSession();
				List<InterviewCardHistory> studentAdmitCardList = null;
					Query query = session.createQuery("from InterviewCardHistory interviewCard " +
							" where interviewCard.admAppln.applnNo = :applicationNo order by interviewCard.id desc");
					query.setInteger("applicationNo", applicationNo);
					studentAdmitCardList = query.list();
				session.flush();
//				//session.close();
				return studentAdmitCardList;
		} catch (Exception e) {
			if (session != null)
				session.flush();
				//session.close();
			throw  new ApplicationException(e); 
		}
	}

	@Override
	public List<StudentCourseChanceMemo> getBolist(String applicationNo,String dob) throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		List<StudentCourseChanceMemo> courseAllotment= new ArrayList<StudentCourseChanceMemo>();
		String d=CommonUtil.formatDate(dob);
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=1 and s.admAppln.applnNo = " + applicationNo + 
					" and s.admAppln.personalData.dateOfBirth='" + d + "'" + ")");
			courseAllotment = query.list();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public boolean isUpdated(List<StudentCourseChanceMemo> allotments) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isUpdate = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			for(StudentCourseChanceMemo allotment:allotments){
				session.update(allotment);
			}
			tx.commit();
			isUpdate = true;
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
		}
		
	}
		return isUpdate;
	}

	@Override
	public StudentCourseChanceMemo getBoClass(String admApplnId) throws Exception {
		Session session = null;
		StudentCourseChanceMemo courseAllotment= new StudentCourseChanceMemo();	
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseChanceMemo s where s.isAccept=s and s.admAppln.id = " + admApplnId + ")");
			courseAllotment =(StudentCourseChanceMemo) query.uniqueResult();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public boolean isUpdateUpload(StudentCourseChanceMemo allotment) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isUpdate = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			session.update(allotment);
			tx.commit();
			isUpdate = true;
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
		}
		
	}
		return isUpdate;
	}

	@Override
	public boolean updateReceivedStatus(StudentAllotmentPGIDetails bo, AdmissionStatusForm admForm) throws Exception {	
	
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		admForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from StudentAllotmentPGIDetails c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+admForm.getAmount()+" and c.txnStatus='Pending'";
				StudentAllotmentPGIDetails candidatePgiBo=(StudentAllotmentPGIDetails)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
				candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
				candidatePgiBo.setBankRefNo(bo.getBankRefNo());
				candidatePgiBo.setTxnDate(bo.getTxnDate());
				candidatePgiBo.setTxnStatus(bo.getTxnStatus());
				
				candidatePgiBo.setMode(bo.getMode());
				candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
				candidatePgiBo.setMihpayId(bo.getMihpayId());
				candidatePgiBo.setPgType(bo.getPgType());
				candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());
				if(admForm.getAdmApplnId()!=null && !admForm.getAdmApplnId().equalsIgnoreCase("")){
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					AdmAppln adm=new AdmAppln();
					adm.setId(Integer.parseInt(admForm.getAdmApplnId()));
					candidatePgiBo.setAdmAppln(adm);
				}
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					admForm.setIsTnxStatusSuccess(true);
					admForm.setPaymentSuccess(true);
				}
				
				admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				admForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				}
			}
			transaction.commit();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			session.close();
		}
		return isUpdated;
	
	
	}

	@Override
	public List<PublishForAllotment> getPublishAllotment() throws Exception {
		Session session=null;
		List<PublishForAllotment> allotments = new ArrayList<PublishForAllotment>();
		Date date =new Date();
		java.sql.Date dat = new java.sql.Date(date.getTime());
		try{
			session=HibernateUtil.getSession();
			String sql="from PublishForAllotment p where '"+ dat + "'" +" between p.fromDate and p.endDate";
			Query query =session.createQuery(sql);
			allotments=query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return allotments;
	}

	@Override
	public List<StudentCourseAllotment> getBolistForAllotment(String applicationNo, String dateOfBirth)
			throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		List<StudentCourseAllotment> courseAllotment= new ArrayList<StudentCourseAllotment>();
		String d=CommonUtil.formatDate(dateOfBirth);
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseAllotment s where s.allotmentNo=2 and s.admAppln.applnNo = " + applicationNo + 
					" and s.admAppln.personalData.dateOfBirth='" + d + "'" );
			courseAllotment = query.list();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public boolean isUpdatedForPg(List<StudentCourseAllotment> courseAllotments) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isUpdate = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			for(StudentCourseAllotment allotment:courseAllotments){
				session.update(allotment);
			}
			tx.commit();
			isUpdate = true;
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
		}
		
	}
		return isUpdate;
	}

	@Override
	public int getProgramTypeId(String applicationNo, String dateOfBirth) throws Exception {
		Session session=null;
		Integer programType=0;
		String dateOf=CommonUtil.formatDate(dateOfBirth);
		try{
			session=HibernateUtil.getSession();
			String hql=" select a.course.program.programType.id " +
					   " from AdmAppln a " +
					   " where a.applnNo="+Integer.parseInt(applicationNo) +
					   " and a.personalData.dateOfBirth='" + dateOf + "'" ;
			Query query = session.createQuery(hql);
			programType=(Integer)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return programType;
	}

	@Override
	public boolean isUpdateForBoth(List<StudentCourseChanceMemo> allotments,
			List<StudentCourseAllotment> courseAllotments, AdmissionStatusForm admissionStatusForm) throws Exception {
		
		
		List<StudentCourseChanceMemo> totalChances = new ArrayList<StudentCourseChanceMemo>();
		
		List<StudentCourseAllotment> totalAllotment = new ArrayList<StudentCourseAllotment>();
		boolean isupdate=false;
		
		try{
			String type=admissionStatusForm.getChancOrAllotment();
			if(type.equalsIgnoreCase("Allotment")){
				
				StudentCourseAllotment all=courseAllotments.get(0);
				StudentCourseChanceMemo chance=allotments.get(0);
				if(admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")){
					all.setIsAccept(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					chance.setIsDecline(true);
					chance.setModifiedBy(admissionStatusForm.getUserId());
					chance.setLastModifiedDate(new Date());
					totalAllotment.add(all);
					totalChances.add(chance);
					isupdate=isUpdateFo(totalChances,totalAllotment);
				}else{
					all.setIsDecline(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					totalAllotment.add(all);
					isupdate=isUpdatedForPg(totalAllotment);
				}
				
			}else{
				
				StudentCourseAllotment all=courseAllotments.get(0);
				StudentCourseChanceMemo chance=allotments.get(0);
				if(admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")){
					chance.setIsAccept(true);
					chance.setModifiedBy(admissionStatusForm.getUserId());
					chance.setLastModifiedDate(new Date());
					all.setIsDecline(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					totalChances.add(chance);
					totalAllotment.add(all);
					isupdate=isUpdateFo(totalChances,totalAllotment);
				}else{
					chance.setIsDecline(true);
					chance.setModifiedBy(admissionStatusForm.getUserId());
					chance.setLastModifiedDate(new Date());
					totalChances.add(chance);
					isupdate=isUpdated(totalChances);
				}
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isupdate;
	}

	private boolean isUpdateFo(List<StudentCourseChanceMemo> totalChances,
			List<StudentCourseAllotment> totalAllotment)throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isUpdate = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			for(StudentCourseAllotment allotment:totalAllotment){
				session.update(allotment);
			}
			for(StudentCourseChanceMemo allotment:totalChances){
				session.update(allotment);
			}
			tx.commit();
			isUpdate = true;
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
		}
		
	}
		return isUpdate;
	}

	@Override
	public List<StudentCourseChanceMemo> getBolistForPG(String applicationNo, String dateOfBirth) throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		List<StudentCourseChanceMemo> courseAllotment= new ArrayList<StudentCourseChanceMemo>();
		String d=CommonUtil.formatDate(dateOfBirth);
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=2 and s.admAppln.applnNo = " + applicationNo + 
					" and s.admAppln.personalData.dateOfBirth='" + d + "'");
			courseAllotment = query.list();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public int getSelectedCourseId(String applicationNo, String dateOfBirth) throws Exception {
		Session session=null;
		Integer cousrseId=0;
		String dateOf=CommonUtil.formatDate(dateOfBirth);
		try{
			session=HibernateUtil.getSession();
			String hql=" select a.course.id " +
					   " from AdmAppln a " +
					   " where a.applnNo="+Integer.parseInt(applicationNo) +
					   " and a.personalData.dateOfBirth='" + dateOf + "'" ;
			Query query = session.createQuery(hql);
			cousrseId=(Integer)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cousrseId;
	}

	@Override
	public List<StudentCourseChanceMemo> getBolistForUg(String applicationNo, String dateOfBirth) throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		List<StudentCourseChanceMemo> courseAllotment= new ArrayList<StudentCourseChanceMemo>();
		String d=CommonUtil.formatDate(dateOfBirth);
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=1 and s.admAppln.applnNo = " + applicationNo + 
					" and s.admAppln.personalData.dateOfBirth='" + d + "'" );
			courseAllotment = query.list();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public List<StudentCourseAllotment> getBolistForAllotmentUg(String applicationNo, String dateOfBirth)
			throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		List<StudentCourseAllotment> courseAllotment= new ArrayList<StudentCourseAllotment>();
		String d=CommonUtil.formatDate(dateOfBirth);
		try {
			session = HibernateUtil.getSession();
			Query query=session.createQuery("from StudentCourseAllotment s where s.allotmentNo=2 and s.admAppln.applnNo = " + applicationNo + 
					" and s.admAppln.personalData.dateOfBirth='" + d + "'" );
			courseAllotment = query.list();		
			return courseAllotment;	
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
	}

	@Override
	public boolean isUpdateForBothForUg(List<StudentCourseChanceMemo> allotments,
			List<StudentCourseAllotment> courseAllotments, AdmissionStatusForm admissionStatusForm)
			throws Exception {
		
		
		List<StudentCourseChanceMemo> totalChances = new ArrayList<StudentCourseChanceMemo>();
		
		List<StudentCourseAllotment> totalAllotment = new ArrayList<StudentCourseAllotment>();
		boolean isupdate=false;
		
		try{
			String type=admissionStatusForm.getChancOrAllotment();
			if(type.equalsIgnoreCase("Allotment")){
				
				StudentCourseAllotment all=courseAllotments.get(0);
				if(admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")){
					all.setIsAccept(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					for(StudentCourseChanceMemo memo:allotments){
						//memo.setIsDecline(true);
						memo.setModifiedBy(admissionStatusForm.getUserId());
						memo.setLastModifiedDate(new Date());
						totalChances.add(memo);
					}
					totalAllotment.add(all);
					isupdate=isUpdateFo(totalChances,totalAllotment);
				}else if(admissionStatusForm.getSelectedValue().equalsIgnoreCase("decline")){
					all.setIsDecline(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
						for(StudentCourseChanceMemo memo:allotments){
							//memo.setIsDecline(true);
							memo.setModifiedBy(admissionStatusForm.getUserId());
							memo.setLastModifiedDate(new Date());
							totalChances.add(memo);
						}
					totalAllotment.add(all);
					isupdate=isUpdatedForPg(totalAllotment);
				}else{
					//all.setIsDecline(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					totalAllotment.add(all);
					isupdate=isUpdatedForPg(totalAllotment);
				}
				
			}else{
				
				StudentCourseAllotment all=courseAllotments.get(0);
				if(admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")){
					for(StudentCourseChanceMemo memo:allotments){
						if(memo.getCourse().getId()==admissionStatusForm.getSelectedCourseId()){
							memo.setIsAccept(true);
							memo.setModifiedBy(admissionStatusForm.getUserId());
							memo.setLastModifiedDate(new Date());
						}else{
							memo.setIsDecline(true);
							memo.setModifiedBy(admissionStatusForm.getUserId());
							memo.setLastModifiedDate(new Date());
						}
						totalChances.add(memo);
					}
					//all.setIsDecline(true);
					all.setModifiedBy(admissionStatusForm.getUserId());
					all.setLastModifiedDate(new Date());
					totalAllotment.add(all);
					isupdate=isUpdateFo(totalChances,totalAllotment);
				}else{
					for(StudentCourseChanceMemo memo:allotments){
						if(memo.getCourse().getId()==admissionStatusForm.getSelectedCourseId()){
							memo.setIsDecline(true);
							memo.setModifiedBy(admissionStatusForm.getUserId());
							memo.setLastModifiedDate(new Date());
						}
						totalChances.add(memo);
					}
					isupdate=isUpdated(totalChances);
				}
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isupdate;
	}
	
	public List<CertificateCourse> getActiveCertificateCourses(int year)throws Exception
	{
		log.info("Start of getActiveCourses of CourseTransactionImpl");
		Session session = null;
		List<CertificateCourse> courseBoList;
		try {
			session = InitSessionFactory.getInstance().openSession();
			courseBoList = session.createQuery("from CertificateCourse c where c.isActive = 1").list();
			} catch (Exception e) {
			log.error("Error in getActiveCertificateCourses of Course Impl",e);
			throw new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
				}
			}
			log.info("End of getActiveCertificateCourses of impl");
			return courseBoList;
	}

	@Override
	public boolean saveStudentCertificateCourse(List<StudentCertificateCourse> studCertCourse) throws Exception {
		log.debug("inside addCertificateCourse");
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			transaction.begin();
			for (StudentCertificateCourse bo : studCertCourse) {
				session.save(bo);
			}
			
			transaction.commit();
			if (session != null) {
				session.flush();
				session.close();
			}
			log.debug("leaving addCertificateCourse");
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			log.error("Error during saving addCertificateCourse..." , e);
			throw new BusinessException(e);
		} catch (Exception e) {
			transaction.rollback();
			log.error("Error during saving addCertificateCourse data..." , e);
			throw new ApplicationException(e);
		}

	}
	@Override
	public List<CertificateCourseTO> getCertificateCoursesprint(int id) throws Exception {
		log.info("Inside of getInterviewResult of AdmissionStatusTransactionImpl");
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			List<StudentCertificateCourse> list=new ArrayList<StudentCertificateCourse>();
			List<CertificateCourseTO> toList=new ArrayList<CertificateCourseTO>();
			Query query=session.createQuery("from StudentCertificateCourse s where s.admAppln.id="+id );
			 list = query.list();	
			if (list!=null) {
				for (StudentCertificateCourse bo : list) {
					CertificateCourseTO to=new CertificateCourseTO();
					to.setId(bo.getId());
					to.setCourseName(bo.getCertificateCourse().getCertificateCourseName());
					toList.add(to);
				}
				return toList;
			}
		} catch (Exception e) {
			throw  new ApplicationException(e);
			} finally {
			if (session != null) {
				session.flush();
			}
		}
		return null;
	}
}