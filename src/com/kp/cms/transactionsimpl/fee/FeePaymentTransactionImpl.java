package com.kp.cms.transactionsimpl.fee;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.AdmittedThrough;
import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.Caste;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.EdnQualification;
import com.kp.cms.bo.admin.FeeAdditionalAccountAssignment;
import com.kp.cms.bo.admin.FeeBillNumber;
import com.kp.cms.bo.admin.FeeCriteria;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.FeePaymentDetailAmount;
import com.kp.cms.bo.admin.FeePaymentMode;
import com.kp.cms.bo.admin.FeeVoucher;
import com.kp.cms.bo.admin.Nationality;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.ResidentCategory;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BillGenerationException;
import com.kp.cms.forms.fee.FeePaymentForm;
import com.kp.cms.to.fee.FeePaymentDetailEditTO;
import com.kp.cms.transactions.fee.IFeePaymentTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;
/**
 * @author microhard
 * This is a TransactionImpl classes for Fee Payment.
 */
public class FeePaymentTransactionImpl implements IFeePaymentTransaction{
	public static final Log log = LogFactory.getLog(FeePaymentTransactionImpl.class);
	private static FeePaymentTransactionImpl feePaymentTransactionImpl = null;
		
	/*
	 * return the singleton class.
	 */
	public static FeePaymentTransactionImpl getInstance() {
		if (feePaymentTransactionImpl == null) {
			feePaymentTransactionImpl = new FeePaymentTransactionImpl();
		}
		return feePaymentTransactionImpl;
	}
	
	/**
	 * adds a new payment.
	 */
	public int addNewPayment(FeePayment feePayment, int financialYear, boolean isBillNoSave) throws Exception,BillGenerationException {
		 Session session = null; 
		 Transaction tx = null;
		 try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
		     tx = session.beginTransaction();
			 tx.begin();
			 
			 Query query = session.createQuery("from FeeBillNumber feebill where feebill.feeFinancialYear.id = :year and feebill.isActive = 1").setInteger("year",financialYear);
			 if(query.list() == null || query.list().size() == 0) {
				 throw new BillGenerationException();
			 }
			 FeeBillNumber feeBillNumber = (FeeBillNumber)query.list().get(0);
			 if(isBillNoSave){
				 feePayment.setBillNo(feeBillNumber.getCurrentBillNo());
			 }
			 session.save(feePayment);
			 int currBillNo = 0;
			 if(isBillNoSave){
				 currBillNo = Integer.parseInt(feeBillNumber.getCurrentBillNo());
				 currBillNo++;
				 feeBillNumber.setCurrentBillNo(String.valueOf(currBillNo--));
			 }
			 tx.commit();
			 return currBillNo;
		 } catch (BillGenerationException e) {
			 tx.rollback();
			 throw e;				 
		 } 
		   catch (Exception e) {
			 tx.rollback();
			 throw e;				 
		 } finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
		
		/**
		 * This method will return the AdmAppln object for particular application and year.
		 */
		public Student getApplicantDetails(String applicationNumber,String year, String regNo, String rollNo) throws Exception{

			Session session = null;
			try {
				session = HibernateUtil.getSession();
				StringBuffer feeApplicantDetailsQuery = new StringBuffer("");
				feeApplicantDetailsQuery.append("Select student.admAppln.id," +
						" student.admAppln.applnNo," +
						" student.admAppln.appliedYear," +
						" student.admAppln.courseBySelectedCourseId.id," +
						" student.admAppln.courseBySelectedCourseId.name," +
						" student.admAppln.admittedThrough.id," +
						" student.admAppln.admittedThrough.name," +
						" student.admAppln.personalData.firstName," +
						" student.admAppln.personalData.middleName," +
						" student.admAppln.personalData.lastName," +
						" student.admAppln.isFreeShip," +
						" student.registerNo," +
						" student.rollNo," +
						" student.id," +
						" student.admAppln.isLig," +
						" caste.isFeeExcemption," +
						" student.admAppln.personalData.residentCategory.id," +
						" student.admAppln.personalData.nationality.id," +
						" student.admAppln.personalData.id, " +
						" student.admAppln.personalData.secondLanguage," +
						" student.admAppln.isAided, " +
						" caste.id " +
						" from Student student" +
						" left join student.admAppln.personalData.caste caste " +    
						" where student.admAppln.isSelected = :isSelected" +
						" and student.admAppln.isApproved = 1" +
					    " and student.admAppln.appliedYear = :year" );
 
				if(applicationNumber!= null && !applicationNumber.trim().isEmpty()){
					feeApplicantDetailsQuery.append(" and student.admAppln.applnNo = '"+applicationNumber+"'");
				}
				else if(regNo!= null && !regNo.trim().isEmpty()){
					feeApplicantDetailsQuery.append(" and student.registerNo = '"+regNo.trim()+"'");	
				}
				else if (rollNo!= null && !rollNo.trim().isEmpty()){
					feeApplicantDetailsQuery.append(" and student.rollNo = '"+rollNo.trim()+"'");	
				}
				Query query = session.createQuery(feeApplicantDetailsQuery.toString());
				query.setInteger("year", new Integer(year));
				query.setBoolean("isSelected", true);
				Student studentFeeDetails = new Student();
				AdmAppln applicantDetails = new AdmAppln();
				AdmittedThrough admittedThrough = new AdmittedThrough();
				PersonalData personalData = new PersonalData();
				Course course = new Course();
				List<Object[]> tempList = query.list();
				Iterator<Object[]> itr = tempList.iterator();
				if(tempList != null ) {
					while (itr.hasNext()) {
						Object[] row = itr.next();
						applicantDetails.setId((Integer) row[0]);
						applicantDetails.setApplnNo((Integer)row[1]);
						applicantDetails.setAppliedYear((Integer)row[2]);
						course.setId((Integer)row[3]);
						course.setName((String)row[4]);
						if(row[15]!= null){
							Caste caste = new Caste();
							caste.setIsFeeExcemption((Boolean)row[15]);
							personalData.setCaste(caste);
						}
						applicantDetails.setCourse(course);
						admittedThrough.setId((Integer)row[5]);
						admittedThrough.setName((String)row[6]);
						applicantDetails.setAdmittedThrough(admittedThrough);
						personalData.setFirstName((String)row[7]);
						personalData.setMiddleName((String)row[8]);
						personalData.setLastName((String)row[9]);
						if(row[16]!= null){
							ResidentCategory residentCategory = new ResidentCategory();
							residentCategory.setId((Integer)row[16]);
							personalData.setResidentCategory(residentCategory);
						}
						if(row[17]!= null){
							Nationality nationality = new Nationality();
							nationality.setId((Integer)row[17]);
							personalData.setNationality(nationality);
						}
						if(row[18]!= null){
							personalData.setId((Integer)row[18]);
						}
							
						if(row[19]!= null){
							personalData.setSecondLanguage(row[19].toString());
						}
						if(row[21]!= null){
							Caste caste = new Caste();
							caste.setId((Integer) row[21]);
							personalData.setCaste(caste);
						}
						applicantDetails.setPersonalData(personalData);
						applicantDetails.setIsFreeShip((Boolean)row[10]);
						applicantDetails.setIsLig((Boolean)row[14]);
						if(row[20]!= null){
							applicantDetails.setAided((Boolean) row[20]);
						}
						else{
							applicantDetails.setAided(false);
						}

						studentFeeDetails.setRegisterNo((String)row[11]);
						studentFeeDetails.setRollNo((String)row[12]);
						studentFeeDetails.setId((Integer)row[13]);
						studentFeeDetails.setAdmAppln(applicantDetails);
						break;
					}
				} 
				return studentFeeDetails;
			} catch (Exception e) {
				throw e;
			} finally {
				if (session != null) {
					session.flush();
				}
			}
		}	
	
	/**
	 * This method will return the List of ApplicantSubjectGroup. 
	 */
	public List<ApplicantSubjectGroup> getApplicantSubjectGroup(int id) throws Exception {
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from ApplicantSubjectGroup where admAppln.id = :id and subjectGroup.isActive = 1");
			 query.setInteger("id",id);
			 List<ApplicantSubjectGroup> list = query.list();
			 return list;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}
	/**
	 *  This method will return the list of fee payment modes.
	 */
	public List<FeePaymentMode> getAllPaymentMode() throws Exception {
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from FeePaymentMode where isActive = :isActive");
			 query.setBoolean("isActive",true);
			 List<FeePaymentMode> list = query.list();
			 return list;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}
	
	/**
	 *  This method will returns the fee id's 
	 *  those matches the semester, feegroupIds and application number.
	 */
	public List<Object[]> getChallanIdsForApplicant(Set<Integer> semSet,String applicationNo,String registerNo, String rollNo, int year) throws Exception {
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = null;
			 if(applicationNo !=null & !applicationNo.trim().isEmpty()){
				 query = session.createQuery("select fee.id "+
							" from FeePayment fee "+
							" join fee.feePaymentApplicantDetailses feePaymentApplicantDetailses " +
								 "	with feePaymentApplicantDetailses.semesterNo in (:semSet) " +
							" where fee.applicationNo = :applicationNo "+
							" and fee.academicYear = :academicYear " + 
							" and fee.isCancelChallan = 0 ");
				query.setParameterList("semSet", semSet);
				query.setInteger("applicationNo",Integer.valueOf(applicationNo));
				query.setInteger("academicYear",Integer.valueOf(year));
			 }else if(registerNo!=null && !registerNo.trim().isEmpty()){
				 query = session.createQuery("select fee.id "+
							" from FeePayment fee "+
							" join fee.feePaymentApplicantDetailses feePaymentApplicantDetailses " +
								 "	with feePaymentApplicantDetailses.semesterNo in (:semSet) " +
							" where fee.registrationNo = :registerNo "+
							" and fee.academicYear = :academicYear " + 
							" and fee.isCancelChallan = 0 ");
				query.setParameterList("semSet", semSet);
				query.setInteger("registerNo",Integer.valueOf(registerNo));
				query.setInteger("academicYear",Integer.valueOf(year));
			 }else if(rollNo!=null && !rollNo.trim().isEmpty()){
				 query = session.createQuery("select fee.id "+
							" from FeePayment fee "+
							" join fee.feePaymentApplicantDetailses feePaymentApplicantDetailses " +
								 "	with feePaymentApplicantDetailses.semesterNo in (:semSet) " +
							" where fee.rollNo = :rollNo "+
							" and fee.academicYear = :academicYear " + 
							" and fee.isCancelChallan = 0 ");
				query.setParameterList("semSet", semSet);
				query.setInteger("rollNo",Integer.valueOf(rollNo));
				query.setInteger("academicYear",Integer.valueOf(year));
			 }
			 
			 List<Object[]> list = query.list();
							 
			 return list;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	 }
	
	
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getChallanDetailsById(java.lang.String)
	 */
	public FeePayment getChallanDetailsById(String billNo) throws Exception {
		Session session = null;
		FeePayment feePayment;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 feePayment =(FeePayment)session.createQuery("from FeePayment where billNo = :billNo").setString("billNo",billNo).uniqueResult();
			 return feePayment;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getFeePaymentsBetweenDates(java.util.Date, java.util.Date)
	 * This method will return the list of fee payments between 2 dates.
	 */
	public List<FeePayment> getFeePaymentsBetweenDates(Date startDate,Date endDate, String divId) throws Exception {
		Session session = null;
		List<FeePayment> feePaymentList;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from FeePayment where isFeePaid = 0 and isCancelChallan = 0 and challenPrintedDate between :startDate and :endDate" +
			 		" and feeDivision.id = :divId");
			 query.setDate("startDate", startDate);
			 query.setDate("endDate",endDate);
			 if(divId!= null){
				 query.setInteger("divId", Integer.parseInt(divId));
			 }
			 else
			 {
				 query.setInteger("divId", 0);
			 }
			 feePaymentList = query.list();
			 return feePaymentList;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getFeePaymentsBetweenDatesForCancel(java.util.Date, java.util.Date)
	 * This method will returs the list of fee payment those not canceled.
	 */
	public List<FeePayment> getFeePaymentsBetweenDatesForCancel(Date startDate,Date endDate) throws Exception {
		Session session = null;
		List<FeePayment> feePaymentList;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from FeePayment where isCancelChallan = 0 and challenPrintedDate between :startDate and :endDate");
			 query.setDate("startDate", startDate);
			 query.setDate("endDate",endDate);
			 feePaymentList = query.list();
			 return feePaymentList;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getOptionalFeeCodes(java.lang.String)
	 */
	public List<FeeAdditionalAccountAssignment> getOptionalFeeCodes(String addIds) throws Exception {
		Session session = null;
		List<FeeAdditionalAccountAssignment> additionalList;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from FeeAdditionalAccountAssignment where feeAdditional.id in (" + addIds + ")");
			 additionalList = query.list();
			 return additionalList;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getFinancialYear()
	 */
	@Override
	public int getFinancialYear() throws Exception {
		Session session = null;
		int financialYear = 0;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select financialYear.id from FeeFinancialYear financialYear" +
			 		" where financialYear.isCurrent = 1" +
			 		" and financialYear.isActive = 1");
			 	
			 financialYear = (Integer) query.uniqueResult();
			 return financialYear;
		 } catch (Exception e) {
			 throw e;
		 } finally {
				if (session != null) {
					session.flush();
					//session.close();
				}
		}
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeePaymentTransaction#getClassNameByStudentId(int)
	 */
	@Override
	public String getClassNameByStudentId(int studentId) throws Exception {
		Session session = null;
		String className = "";
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select student.classSchemewise.classes.name" +
			 		" from Student student " +
			 		" where student.id = :studentId" +
			 		" and student.isAdmitted = 1" +
			 		" and student.isActive = 1");

			 query.setInteger("studentId", studentId);
			 className = (String) query.uniqueResult();
			 return className;
		 } catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}


	}	
//start by murthy
	public List<FeeVoucher> getFeevoucherList(Integer financialYearId) throws Exception
	{
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery(" From FeeVoucher fv where fv.feeFinancialYear.id = :FinYearId " +
			 		"and fv.isActive=1");

			 query.setInteger("FinYearId", financialYearId);
			List<FeeVoucher> list=query.list();
			return list;
		 } catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}

	@Override
	public FeePayment getFeePaymentDetailsForEdit(int billNo,
			int financialYearId) throws Exception {
		Session session = null;
		FeePayment feePayment = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select feePayment" +
			 		" from FeePayment feePayment" +
			 		" join feePayment.feePaymentDetails paymentDetails" +
			 		" where feePayment.billNo = :billNo" +
			 		" and feePayment.amountFinancialYear.id = :FinYearId and feePayment.isCancelChallan = 0" +
			 		" group by feePayment.id ");
			query.setInteger("billNo", billNo); 
			query.setInteger("FinYearId", financialYearId);
			
			if(query.list() != null && !query.list().isEmpty()){
				feePayment = (FeePayment) query.list().get(0);
			}
			return feePayment;
		 } catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}

	@Override
	public boolean feePaymentDetailsUpdate(FeePaymentForm feePaymentForm)
			throws Exception {
		Session session = null;
		Transaction txn=null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			txn= session.beginTransaction();
			
			if (feePaymentForm != null && feePaymentForm.getFeePaymentEditTO()!=null) {
				double deducationAmt;
				double totalAmount = new Double(feePaymentForm.getFeePaymentEditTO().getTotalAmount());
				deducationAmt = new Double(feePaymentForm.getFeePaymentEditTO().getTotalConcessionAmount()) +  new Double(feePaymentForm.getFeePaymentEditTO().getTotalInstallmentAmount()) +
								new Double(feePaymentForm.getFeePaymentEditTO().getTotalScholarshipAmount());
				double paid = totalAmount - deducationAmt;
				boolean isCompletelyPaid = false;
				if(paid == totalAmount){
					isCompletelyPaid = true;
				}
				
				if(feePaymentForm.isFeePaid()){
					String feePaymentQuery = "update FeePayment fp set fp.isFeePaid = :feePaid"
						+ ", fp.feePaidDate = :paidDate"
						+ ", fp.challenPrintedDate = :challanDate"
						+ ", fp.feePaymentMode.id = :modeId, totalConcessionAmount = :totalConc, totalFeePaid = " + paid
						+ ", isCompletlyPaid = " + isCompletelyPaid + ", concessionVoucherNo = :concVouch "
						+ " where fp.id = :paymentId";
				Query query = session.createQuery(feePaymentQuery);
				query.setBoolean("feePaid", true);
				query.setDate("paidDate", CommonUtil.ConvertStringToSQLDate(feePaymentForm.getFeePaymentEditTO().getDateTime()));
				query.setDate("challanDate", CommonUtil.ConvertStringToSQLDate(feePaymentForm.getFeePaymentEditTO().getChalanDate()));
				query.setInteger("modeId", feePaymentForm.getFeePaymentEditTO().getFeePaymentModeId());
				query.setInteger("paymentId", feePaymentForm.getFeePaymentEditTO().getFeePaymentId());
				query.setString("totalConc", feePaymentForm.getFeePaymentEditTO().getTotalConcessionAmount());
				query.setString("concVouch", feePaymentForm.getFeePaymentEditTO().getConcessionVoucherNo());
				
				query.executeUpdate();
				}else{
					String feePaymentQuery = "update FeePayment fp set fp.isFeePaid = :feePaid"
						+ ", fp.feePaidDate = :paidDate"
						+ ", fp.challenPrintedDate = :challanDate"
						+ ", fp.feePaymentMode.id = :modeId, totalConcessionAmount = :totalConc, totalFeePaid = " + paid
						+ ", isCompletlyPaid = " + isCompletelyPaid + ", concessionVoucherNo = :concVouch "
						+ " where fp.id = :paymentId";
				Query query = session.createQuery(feePaymentQuery);
				query.setBoolean("feePaid", false);
				query.setDate("paidDate", CommonUtil.ConvertStringToSQLDate(feePaymentForm.getFeePaymentEditTO().getDateTime()));
				query.setDate("challanDate", CommonUtil.ConvertStringToSQLDate(feePaymentForm.getFeePaymentEditTO().getChalanDate()));
				query.setInteger("modeId", feePaymentForm.getFeePaymentEditTO().getFeePaymentModeId());
				query.setInteger("paymentId", feePaymentForm.getFeePaymentEditTO().getFeePaymentId());
				query.setString("totalConc", feePaymentForm.getFeePaymentEditTO().getTotalConcessionAmount());
				query.setString("concVouch", feePaymentForm.getFeePaymentEditTO().getConcessionVoucherNo());
				query.executeUpdate();
				}
			}
			
			if (feePaymentForm != null && feePaymentForm.getFeePaymentEditTO()!=null && feePaymentForm.getFeePaymentEditTO().getFeePaymentDetailEditList()!=null) {
				List<FeePaymentDetailEditTO> feePaymentDetailList = feePaymentForm.getFeePaymentEditTO().getFeePaymentDetailEditList();
				if(feePaymentDetailList!=null && !feePaymentDetailList.isEmpty()){
					
					Iterator<FeePaymentDetailEditTO> feePaymentDetailsItr = feePaymentDetailList.iterator();
					
					while (feePaymentDetailsItr.hasNext()) {
						FeePaymentDetailEditTO feePaymentDetailEditTO = (FeePaymentDetailEditTO) feePaymentDetailsItr
								.next();
						String feePaymentDetailQuery = "update FeePaymentDetail fpd set fpd.excessShortAmount = :amount , fpd.feePaymentMode.id= :modeId," +
								" concessionAmount = :concessionAmt"
							+ " where fpd.id = :paymentDetailId";
					Query query = session.createQuery(feePaymentDetailQuery);
					query.setString("amount", feePaymentDetailEditTO.getExcessShortAmount());
					query.setInteger("paymentDetailId", feePaymentDetailEditTO.getFeePaymentDetailId());
					query.setInteger("modeId", feePaymentForm.getFeePaymentEditTO().getFeePaymentModeId());
					query.setString("concessionAmt", feePaymentDetailEditTO.getConcessionAmount());
					query.executeUpdate();
					}
				}
			}
			txn.commit();
			return true;
		} catch (ConstraintViolationException e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}catch (Exception e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}finally{
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	@Override
	public long getNoOfChallansForcourse(String query) throws Exception {
		Session session = null;
		long count = 0;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			Query selectedQuery=session.createQuery(query);
			count = (Long)selectedQuery.uniqueResult();
			return count;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	/***
	 * 
	 * @param instId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria  getAdditionalFeeForInst(int instId) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from FeeCriteria f where f.university.id is null and college.id = " + instId + " and nationality.id is null and residentCategory.id is null");
			
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughIdForInst...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}

	/**
	 * 
	 * @param natId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForNationality(int natId) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from FeeCriteria f where f.nationality.id="+natId + " and f.college.id is null and f.university.id is null and f.residentCategory.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughIdForNationality...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}	

	/**
	 * 
	 * @param natId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForUniveristy(int uniId) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from FeeCriteria f where f.university.id="+uniId + " and college.id is null and nationality.id is null and residentCategory.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughIdForUniveristy...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * 
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForResidentCategory(int resId) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where f.residentCategory.id="+resId + " and f.college.id is null and f.nationality.id is null and f.university.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughResidentCategory...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * 
	 * @param instId
	 * @param natId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForInstNationality(int instId, int natId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id = " + natId + "and" +
					" f.university.id is null and " +
					" f.residentCategory.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdditionalFeeForInstNationality...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}		

	/**
	 * 
	 */
	public FeeCriteria getAdditionalFeeForInstUni(int instId, int uniId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id is null and " +
					" f.university.id =" + uniId + " and " + 
					" f.residentCategory.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdditionalFeeForInstUni...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}			

	/**
	 * 
	 * @param instId
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForInstRes(int instId, int resId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id is null and " +
					" f.university.id  is null and " + 
					" f.residentCategory.id =" + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdditionalFeeForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	/***
	 * 
	 */
	public FeeCriteria getAdditionalFeeForNatUni(int natId, int uniId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id is null and " +
					" f.nationality.id = " + natId + " and " +
					" f.university.id  = " + uniId + " and " +
					" f.residentCategory.id is null " );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	/**
	 * 
	 * @param natId
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForNatRes(int natId, int resId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id is null and " +
					" f.nationality.id = " + natId + " and " +
					" f.university.id  is null and " +
					" f.residentCategory.id = " + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * 
	 * @param resId
	 * @param uniId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForResUni(int resId, int uniId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id is null and " +
					" f.nationality.id is null and " +
					" f.university.id  = " + uniId + " and " +
					" f.residentCategory.id = " + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}	

	/**
	 * 
	 * @param instId
	 * @param natId
	 * @param uniId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForInstNationalityUni(int instId, int natId, int uniId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id =" + natId + " and " +
					" f.university.id  =" + uniId + " and " + 
					" f.residentCategory.id is null");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughResidentCategory...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * 
	 * @param instId
	 * @param natId
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForInstNatRes(int instId, int natId, int resId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id = " + natId + " and " +
					" f.university.id  is null and " +
					" f.residentCategory.id = " + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	/**
	 * 
	 * @param natId
	 * @param uniId
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForNatUniRes(int natId, int uniId, int resId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id is null and " +
					" f.nationality.id = " + natId + " and " +
					" f.university.id  = " + uniId + " and " + 
					" f.residentCategory.id = " + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}	

	/**
	 * 
	 * @param uniId
	 * @param resId
	 * @param instId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForUniResInst(int uniId, int resId, int instId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id is null and " +
					" f.university.id  = " + uniId + " and " + 
					" f.residentCategory.id = " + resId );
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughForInstRes...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}	

	/**
	 * 
	 * @param instId
	 * @param natId
	 * @param uniId
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForInstNationalityUniRes(int instId, int natId, int uniId, int resId ) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" f.college.id = " + instId + " and " +
					" f.nationality.id = " + natId + " and " +
					" f.university.id  = " + uniId + " and " + 
					" f.residentCategory.id =" + resId);
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdmittedThroughResidentCategory...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}
	/**
	 *   
	 * @param personalDataId
	 * @return
	 * @throws Exception
	 */
	 
	public List<EdnQualification> getEdnQualificationByPersonalData(int personalDataId ) throws Exception {
		Session session = null;
		List<EdnQualification> ednQualificationList = new ArrayList<EdnQualification>();
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from EdnQualification e where e.personalData.id = " + personalDataId);
					
			ednQualificationList =(List<EdnQualification>) query.list();
			
			session.flush();
			return ednQualificationList;
		} catch (Exception e) {
			log.error("Error in getEdnQualificationByPersonalData...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}		
	/**
	 * 
	 * @param language
	 * @return
	 * @throws Exception
	 */
	public FeeCriteria getAdditionalFeeForSecondLanguage(String language) throws Exception {
		Session session = null;
		FeeCriteria feeCriteria;
		try {
			SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Query query = session.createQuery("from FeeCriteria f where " +
					" trim(f.secLanguage) = '" + language.trim() + "'");
			feeCriteria =(FeeCriteria) query.uniqueResult();
			
			session.flush();
			return feeCriteria;
		} catch (Exception e) {
			log.error("Error in getAdditionalFeeForSecondLanguage...", e);

			if (session != null) {
				session.flush();
			}
			throw new ApplicationException(e);
		}
	}

	/**
	 * 
	 * @param applNo
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public List<FeePayment> getOldFeePaymentDetails(String applNo, String year) throws Exception {
		Session session = null;
		List<FeePayment> feePaymentList;
		try {
			 session = HibernateUtil.getSession();
			 String year1 = Integer.toString(Integer.parseInt(year)+1);
//			 String quer = " from FeePayment fp "
//						+  " where fp.id in (select fpd.feePayment.id from FeePaymentDetail fpd where fpd.feeFinancialYear.id=:yearId) "
//						+  " and fp.applicationNo=:applNo";
//			 String sqlQuery = " from FeePayment fp "
//							  +" where fp.applicationNo=:applNo "
//							  +" and (fp.academicYear=:yearId or fp.academicYear=:yearIds) "
//			 				  +" and fp.isCancelChallan=0";
//			 Query query = session.createQuery(sqlQuery);
//			 query.setString("applNo", applNo);
//			 query.setString("yearId", year);
//			 query.setString("yearIds", year1);
			 String qr = "select fp from FeePayment fp " +
			 		" join fp.feePaymentDetails fpd" +
			 		" where fpd.feeFinancialYear.id=" +year+
			 		" and fp.applicationNo=" +applNo+
			 		" and fp.isCancelChallan=0 " +
			 		" group by fp.id "; 
			 Query query = session.createQuery(qr);
			 feePaymentList = query.list();
			 
			 return feePaymentList;
		 } catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
			}
		}
	}	
	
	/**
	 * 
	 * @param amountList
	 * @return
	 * @throws Exception
	 */
	public boolean addFeePaymentDetailAmounts(List<FeePaymentDetailAmount> amountList) throws Exception {
		log.info("Inside updateFeePaymentDetailAmounts TransactionImpl");
		FeePaymentDetailAmount feePaymentDetailAmount;
		Transaction tx=null;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Iterator<FeePaymentDetailAmount> itr = amountList.iterator();
			int count = 0;
			tx = session.beginTransaction();
			tx.begin();
			while (itr.hasNext()) {
				feePaymentDetailAmount = itr.next();
				session.save(feePaymentDetailAmount);
				if (++count % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			if(tx !=null){
				tx.rollback();
			}
			log.error("Error occured in updateFeePaymentDetailAmounts");
			throw new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
			}
			log.info("End of updateFeePaymentDetailAmounts in TransactionImpl");
		}
	}

	public boolean UpdateAdmittedThrough(int ApplnId, int admThroughId)
			throws Exception {
		Session session = null;
		Transaction txn=null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			txn= session.beginTransaction();
			
			String feePaymentQuery = "update AdmAppln set admittedThrough.id = :admThroughId "
				+ " where id = :ApplnId";
			Query query = session.createQuery(feePaymentQuery);
			query.setInteger("ApplnId", ApplnId);
			query.setInteger("admThroughId", admThroughId);
						
			query.executeUpdate();
			
			txn.commit();
			return true;
		} catch (ConstraintViolationException e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}catch (Exception e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}finally{
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	/**
	 * This method will return the AdmAppln object for particular application and year.
	 */
	public List<Student> getApplicantDetailsByName(String studentName, String year) throws Exception{

		Session session = null;
		try {
			session = HibernateUtil.getSession();
			StringBuffer feeApplicantDetailsQuery = new StringBuffer("");
			feeApplicantDetailsQuery.append("from Student student" +
					" where student.admAppln.isSelected = :isSelected" +
					" and student.admAppln.isApproved = 1" +
				    " and student.admAppln.appliedYear = :year and student.admAppln.personalData.firstName like '%" + studentName + "%'" );

			Query query = session.createQuery(feeApplicantDetailsQuery.toString());
			query.setInteger("year", new Integer(year));
			query.setBoolean("isSelected", true);
			List<Student> tempList = query.list();
			return tempList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (session != null) {
				session.flush();
			}
		}
	}
	public boolean isCasteExemption(int casteId) throws Exception{

		Session session = null;
		try {
			session = HibernateUtil.getSession();
			StringBuffer queryString = new StringBuffer("");
			queryString.append("from Caste c " +
				    " where c.id = :casteId" );

			Query query = session.createQuery(queryString.toString());
			query.setInteger("casteId", casteId);
			List<Caste> tempList = query.list();
			if(tempList!= null && tempList.size() > 0){
			 	Caste caste = new Caste();
			 	caste = tempList.get(0);
			 	if(caste.getIsFeeExcemption()){
			 		return true;
			 	}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (session != null) {
				session.flush();
			}
		}
		return false;
	}
	/**
	 * 
	 * @param id
	 * @param casteId
	 * @return
	 * @throws Exception
	 */
	public boolean UpdatePersonalData(int id, int casteId, String secLanguage) throws Exception {
		Session session = null;
		Transaction txn=null;
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
			txn= session.beginTransaction();
			
			String feePaymentQuery = "update PersonalData set caste.id = :casteId, secondLanguage= :secLan "
				+ " where id = :id";
			Query query = session.createQuery(feePaymentQuery);
			query.setInteger("id", id);
			query.setInteger("casteId", casteId);
			query.setString("secLan", secLanguage);
						
			query.executeUpdate();
			
			txn.commit();
			return true;
		} catch (ConstraintViolationException e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}catch (Exception e) {
			if(session.isOpen()){	
				txn.rollback();
			}
			return false;
		}finally{
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param regNo
	 * @param financialYearId
	 * @return
	 * @throws Exception
	 */
	public String getConcessionAlpha(String financialYearId) throws Exception {
		Session session = null;
		String alpha = "";
		try {
			session = HibernateUtil.getSession();
			
			 Query query = session.createQuery(" select alpha From FeeVoucher fv where fv.feeFinancialYear.id = :FinYearId " +
		 		"and fv.isActive=1");
			
			 query.setString("FinYearId", financialYearId);
			 List<String> list1 = query.list();
			 if(list1!= null && list1.size() > 0 && list1.get(0)!= null){
				 alpha = list1.get(0);
			 }

		} catch (Exception e) {
			log.debug("Exception" + e.getMessage());
		}
		return alpha;

	}
	
	
	public String getFinancialYear(String financialYearId) throws Exception{
		Session session = null;
		String finYear = "";
		try {
			session = HibernateUtil.getSession();
			
			 Query query = session.createQuery(" select fv.year From FeeFinancialYear fv where fv.id = :FinYearId and fv.isActive=1");
			
			 query.setString("FinYearId", financialYearId);
			 List<String> list1 = query.list();
			 if(list1!= null && list1.size() > 0 && list1.get(0)!= null){
				 finYear = list1.get(0).substring(0, 4);
			 }

		} catch (Exception e) {
			log.debug("Exception" + e.getMessage());
		}
		return finYear;
	}

	@Override
	public int getSemAcademicYear(Set<Integer> semSet,
			FeePaymentForm feePaymentForm) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Integer year = null;
		Integer tempYear =0;
		Iterator<Integer> itr = semSet.iterator();
		while (itr.hasNext()) {
			tempYear = itr.next();
		}
		tempYear = (tempYear * 2) - 1;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery("select csd.academicYear from CurriculumSchemeDuration csd " +
							"	where csd.semesterYearNo = :tempYear " +
			                "   and csd.curriculumScheme.year=:appliedYear "+
			                "   group by csd.academicYear ");
			query.setParameter("tempYear", tempYear);
			query.setInteger("appliedYear",Integer.valueOf(feePaymentForm.getSemAcademicYear()));
			year=(Integer) query.uniqueResult();
		}
			catch (Exception e) {
				log.debug("Exception" + e.getMessage());
		
	}
			return year;
}
}