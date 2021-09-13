package com.kp.cms.transactionsimpl.fee;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.Fee;
import com.kp.cms.bo.admin.FeeAccountAssignment;
import com.kp.cms.bo.admin.FeeGroup;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.transactions.fee.IFeeTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

/**
 * 
 * @author microhard
 * @version 1.0
 * Date 09/jan/2009
 * This is Transaction Implementation Class for Fee Assignment.  
 * This class used to interact with fee,fee_assignment_account related tables.
 */
public class FeeTransactionTmpl implements IFeeTransaction{
        
	private static FeeTransactionTmpl feeAssignmentTransactionTmpl = null;
	private static final Log log = LogFactory.getLog(FeeTransactionTmpl.class);
	public static FeeTransactionTmpl getInstance() {
	    if(feeAssignmentTransactionTmpl == null ){
		    feeAssignmentTransactionTmpl = new FeeTransactionTmpl();
	       return feeAssignmentTransactionTmpl;
	    }
	    return feeAssignmentTransactionTmpl;
	}
	
	/**
	 * This method add the Fee and FeeAccountAssignment.
	 */
	public boolean addFeeAssignment(Fee fee) throws ConstraintViolationException,Exception{
		log.debug("Txn Impl : Entering addFeeAssignment ");
		Session session = null; 
		Transaction tx = null;
		try {
			 SessionFactory sessionFactory = InitSessionFactory.getInstance();
//			 session = HibernateUtil.getSession();
			 session=sessionFactory.openSession();
			 tx = session.beginTransaction();
			 session.saveOrUpdate(fee);
			 tx.commit();
			 session.close();
			 log.debug("Txn Impl : Leaving addFeeAssignment with success");
	    	 return true;
		 } catch (ConstraintViolationException e) {
			 tx.rollback();
			 session.close();
			 log.debug("Txn Impl : Leaving addFeeAssignment with Exception"+e.getMessage());
			 throw e;				 
		 } catch (Exception e) {
			 tx.rollback();
			 session.close();
			 log.debug("Txn Impl : Leaving addFeeAssignment with Exception"+e.getMessage());
			 throw e;				 
		 } 
			 
	}

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.fee.IFeeAssignmentTransaction#getAllFeeAssignments()
	 */
	
	public List<Fee> getAllFees() throws Exception {
		log.debug("Txn Impl : Entering getAllFees ");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from Fee where " +
					 						   " programType.isActive = 1"+
					 						   " and program.isActive = 1"+
					 						   " and course.isActive = 1"+
			 								   " and isActive = :isActive");
			 query.setBoolean("isActive",true);
			 List<Fee> list = query.list();
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getAllFees with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getAllFees with Exception"+e.getMessage());
			 throw e;
		 }
	}
	
	/**
	 * This method delete the FeeAssignment from fee & fee_account_assignment tables.
	 *  @return true / flase based on result.
	 */
	public boolean deleteFeeAssignment(Fee fee) throws Exception{
		log.debug("Txn Impl : Entering deleteFeeAssignment "); 
		Session session = null;
		Transaction tx = null;
		Fee persistantFee;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
		     tx = session.beginTransaction();
			 tx.begin();

			 persistantFee = (Fee)session.get(Fee.class, fee.getId());
			 Iterator<FeeAccountAssignment> itr = persistantFee.getFeeAccountAssignments().iterator();
			 while(itr.hasNext()){
				 itr.next().setIsActive(false);
			 }
			 persistantFee.setIsActive(false);
			 persistantFee.setModifiedBy(fee.getModifiedBy());
			 persistantFee.setLastModifiedDate(fee.getLastModifiedDate());
			 tx.commit();
			 //session.close();
			 log.debug("Txn Impl : Leaving deleteFeeAssignment with success");
	    	 return true;
		} catch (Exception e){
			 log.debug("Txn Impl : Leaving deleteFeeAssignment with Exception");
			 tx.rollback();
			 //session.close();
			 throw e;
		}
	 }
	
	/**
	 * This method delete the FeeAssignment from fee & fee_account_assignment tables.
	 *  @return true / flase based on result.
	 */
	public boolean activateFeeAssignment(Fee fee) throws Exception{
		log.debug("Txn Impl : Entering activateFeeAssignment ");
		Session session = null;
		Transaction tx = null;
		Fee persistantFee;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
		     tx = session.beginTransaction();
			 tx.begin();

			 persistantFee = (Fee)session.get(Fee.class, fee.getId());
			 Iterator<FeeAccountAssignment> itr = persistantFee.getFeeAccountAssignments().iterator();
			 while(itr.hasNext()){
				 itr.next().setIsActive(true);
			 }
			 persistantFee.setIsActive(true);
			 persistantFee.setModifiedBy(fee.getModifiedBy());
			 persistantFee.setLastModifiedDate(fee.getLastModifiedDate());
			 
			 tx.commit();
			 //session.close();
			 log.debug("Txn Impl : Leaving activateFeeAssignment with success");
	    	 return true;
		} catch (Exception e){
			 log.debug("Txn Impl : Leaving activateFeeAssignment with Exception");
			 tx.rollback();
			 //session.close();
			 throw e;
		}
	 }
	
	/**
	 * This method add the Fee and FeeAccountAssignment.
	 */
	public boolean updateFeeAssignment(Fee fee) throws Exception{
		log.debug("Txn Impl : Entering updateFeeAssignment ");
		Session session = null; 
		Transaction tx = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
		     tx = session.beginTransaction();
			 tx.begin();
			 session.saveOrUpdate(fee);
			 tx.commit();
			 session.close();
			 log.debug("Txn Impl : Leaving updateFeeAssignment with success");
	    	 return true;
		 } catch (Exception e) {
			 tx.rollback();
			 session.close();
			 log.debug("Txn Impl : Leaving updateFeeAssignment with Exception");
			 throw e;				 
		 }
	}
	/**
	 * This method load the FeeAssignment from fee & fee_account_assignment tables.
	 *  @return Fee object.
	 */
	public Fee getFeeAssignmentById(int feeId) throws Exception{
		log.debug("Txn Impl : Entering getFeeAssignmentById "); 
		Session session = null;
		Fee fee;
		try {
			//SessionFactory sessionFactory = InitSessionFactory.getInstance();
			session = HibernateUtil.getSession();
		    Transaction tx = session.beginTransaction();
			tx.begin();
			fee = (Fee)session.get(Fee.class, feeId);
			tx.commit();
			//session.close();
			log.debug("Txn Impl : Leaving getFeeAssignmentById with success");
	    	return fee;
		} catch (Exception e){
			log.debug("Txn Impl : Leaving getFeeAssignmentById with Exception");
			//session.close();
			throw e;
		}
	 
	 }
	 
	 /**
	  * 
	  */
	 public List<FeeAccountAssignment> getFeesAssignAccounts(int feeId,Set<Integer> accountSet,Set<Integer> applicableSet) throws Exception {
		log.debug("Txn Impl : Entering getFeesAssignAccounts ");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from FeeAccountAssignment where fee.id = :feeId"
					                           +" and feeAccount.id in (:accountSet)"
			 								   +" and applicableFees.id in (:applicableSet)");
			 query.setInteger("feeId", feeId);
			 query.setParameterList("accountSet", accountSet);
			 query.setParameterList("applicableSet", applicableSet);
			
			 List<FeeAccountAssignment> list = query.list();
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getFeesAssignAccounts with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getFeesAssignAccounts with Exception");
			 throw e;
		 }
	}
	 
	 public List<Fee> getFeesPaymentDetailsForApplicationNo(Set<Integer> courseSet, String year, Set<Integer> semSet, boolean isAided) throws Exception {
		log.debug("Txn Impl : Entering getFeesPaymentDetailsForApplicationNo ");
		Session session = null;
		try {
			 session = HibernateUtil.getSession();
			 
			 String sqlQuery = "";
				 
			 if(isAided){
				 sqlQuery = "from Fee where course.id in (:courseSet)"
					   + " and semesterNo in (:semsSet) "
						   + " and academicYear = :academicYear"
						   + " and isActive =:isActive and"
						   + " aidedUnaided = 'Aided'";
			 }else{
				 sqlQuery = "from Fee where course.id in (:courseSet)"
					   + " and semesterNo in (:semsSet) "
						   + " and academicYear = :academicYear"
						   + " and isActive =:isActive and"
						   + " aidedUnaided = 'Unaided'";
			 }
			 Query query = session.createQuery(sqlQuery);
			 query.setParameterList("courseSet", courseSet);
			 query.setParameterList("semsSet", semSet);
			 query.setInteger("academicYear", new Integer(year));
			 query.setBoolean("isActive", true);
			 
			 List<Fee> list = query.list();
			 log.debug("Txn Impl : Leaving getFeesPaymentDetailsForApplicationNo with success");
			 return list;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getFeesPaymentDetailsForApplicationNo with Exception");
			 throw e;
		 }
	 }
	 
	/**
	* This method load the FeeAssignment from fee & fee_account_assignment tables.
	*  @return Fee object.
	*/
	public Fee getFeeByCompositeKeys(Fee feeOld) throws Exception{
		log.debug("Txn Impl : Entering getFeeByCompositeKeys "); 
		Session session = null;
		Fee fee=null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
		     Transaction tx = session.beginTransaction();
			 tx.begin();
			 Query query = session.createQuery("from Fee where programType.id = :programType"
					 							+" and program.id = :program "
					 							+" and course.id = :course "
					 							+" and academicYear = :academicYear"
					 							+" and semesterNo = :semesterNo"
					 							+" and aidedUnaided=:aidedUnaided"
					 							);
			 query.setInteger("programType", feeOld.getProgramType().getId());
			 query.setInteger("program", feeOld.getProgram().getId());
			 query.setInteger("course", feeOld.getCourse().getId());
			 query.setInteger("academicYear", feeOld.getAcademicYear());
			 query.setInteger("semesterNo", feeOld.getSemesterNo());
			 query.setString("aidedUnaided",feeOld.getAidedUnaided());
			 fee =(Fee)query.uniqueResult();
			 tx.commit();
			 //session.close();
			 log.debug("Txn Impl : Leaving getFeeByCompositeKeys with success");
	    	 return fee;
		 } catch (Exception e){
			 log.debug("Txn Impl : Leaving getFeeByCompositeKeys with Exception");
			 //session.close();
			 throw e;
		 }
		 
	 }
	
	/**
	 * 
	 */
	public Map<Integer,String> getFeesGroupDetailsForCourse(Set<Integer>courseSet,String year) throws Exception {
		log.debug("Txn Impl : Entering getFeesGroupDetailsForCourse ");
		Map<Integer,String> feeGroupMap = new HashMap<Integer,String>();
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select distinct fee.feeGroup.id, fee.feeGroup.name "
					 +" from Fee fee"  
					 +" inner join fee.feeGroup.feeHeadings headings" 
					 +" where headings.isActive = 1"
					 +" and fee.feeGroup.isActive = 1"
					 +" and fee.isActive = 1 "
					 +" and fee.course.id = :courseSet"
					 +" and fee.academicYear = :academicYear");
			 query.setParameterList("courseSet", courseSet);
			 query.setInteger("academicYear",Integer.valueOf(year));
			 
			 List<Object[]> list = query.list();
			 Iterator<Object[]> itr = list.iterator();
			 while(itr.hasNext()) {
				 Object[] row = itr.next();
				 feeGroupMap.put((Integer)row[0], row[1].toString());
			 }
			 //session.close();
			 //sessionFactory.close();
			 log.debug("Txn Impl : Leaving getFeesGroupDetailsForCourse with success");
			 return feeGroupMap;
		 } catch (Exception e) {
			 log.debug("Txn Impl : Leaving getFeesGroupDetailsForCourse with Exception");
			 throw e;
		 }
	 }

	
	public List<FeeGroup> getFeeGroup() throws Exception {
		log.info("call of getFeeGroup method in FeeGroupTransactionImpl class.");
		Session session = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select g from FeeGroup g " +
			 		"inner join g.feeHeadings h " +
			 		"where g.isActive=1 " +
			 		"and g.isOptional=0 " +
			 		"group by g.id");
			 List<FeeGroup> feeGroup = query.list();
			 session.flush();
			 //session.close();
			 //sessionFactory.close();
			 log.info("end of getFeeGroup method in FeeGroupTransactionImpl class.");
			 return feeGroup;
		 } catch (Exception e) {
			 if( session != null){
				 session.flush();
				 //session.close();
			 }
			 throw new ApplicationException(e);
		 }
	 }

	
}
