package com.kp.cms.transactionsimpl.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentLogin;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.transactions.admin.IGeneratePasswordTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class GeneratePasswordTransactionImpl implements
		IGeneratePasswordTransaction {
	private static final Log log = LogFactory.getLog(GeneratePasswordTransactionImpl.class);
	/* (non-Javadoc)
	 * gets students not having login credentials
	 * @see com.kp.cms.transactions.admin.IGeneratePasswordTransaction#getSearchedStudents(int, int)
	 */
	@Override
	public List<Student> getSearchedStudents(int programId, int year)
			throws Exception {
		Session session = null;
		 List<Student> stList;
		try{
		StringBuffer quer=new StringBuffer("from Student st where st.id not in(select sl.student.id from StudentLogin sl where sl.isActive =1) and st.isAdmitted=1 and st.admAppln.courseBySelectedCourseId.program.id= :progId and st.admAppln.appliedYear= :applyYear ");
			 
		 session = HibernateUtil.getSession();
		 Query query=session.createQuery(quer.toString());
		 query.setInteger("progId", programId);
		 query.setInteger("applyYear", year);
		 stList=query.list();
		}catch(Exception e){
			 throw  new ApplicationException(e);
		}
		return stList;
	}

	/* (non-Javadoc)
	 * saves student logins
	 * @see com.kp.cms.transactions.admin.IGeneratePasswordTransaction#saveCredentials(java.util.List)
	 */
	@Override
	public boolean saveCredentials(List<StudentLogin> studentlogins)
			throws Exception {
		boolean result= false;
		Session session = null;
		Transaction txn=null;
		try {
			 session = HibernateUtil.getSession();
			 txn= session.beginTransaction();
			 if(studentlogins!=null){
				 Iterator<StudentLogin> stItr=studentlogins.iterator();
				 while (stItr.hasNext()) {
					StudentLogin studentLogin = (StudentLogin) stItr.next();
					session.saveOrUpdate(studentLogin);
				}
			 }
			 txn.commit();
			/// session.flush();
			 //session.close();
			 //sessionFactory.close();
			 result=true;
		 }catch (ConstraintViolationException e) {
			 if(txn!=null){	
			     txn.rollback();
			 }
				log.error("Error during saving student logins...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {
			 if(txn!=null)	{ 
			txn.rollback();
			 }
			log.error("Error during saving student logins...",e);
			 throw new ApplicationException(e);
		 }
		return result;
	}

	/* (non-Javadoc)
	 * resets password, if reseted successfully,returns mail id 
	 * @see com.kp.cms.transactions.admin.IGeneratePasswordTransaction#resetPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public StudentLogin resetPassword(String usernm, String encryptedpwd,String userID,String iserverPassword)
			throws Exception {
		StudentLogin studentlogin=null;
		Session session = null;
		Transaction txn=null;
		try {
			 session = HibernateUtil.getSession();
			 Query query=session.createQuery("from StudentLogin sl where sl.userName= :UNM");
			 query.setString("UNM", usernm);
			 List<StudentLogin> studentlogins=query.list();
			if(studentlogins!=null){
				 txn= session.beginTransaction();
				 Iterator<StudentLogin> stItr=studentlogins.iterator();
				 while (stItr.hasNext()) {
					StudentLogin studentLogin = (StudentLogin) stItr.next();
					studentLogin.setPassword(encryptedpwd);
					studentLogin.setIserverPassword(iserverPassword);
					studentLogin.setModifiedBy(userID);
					studentLogin.setLastModifiedDate(new Date());
					studentLogin.setIsTempPassword(true);

					
					studentlogin=studentLogin;
				}
				 txn.commit();
			}
			
			 session.flush();
			  }catch (ConstraintViolationException e) {
			 if(txn!=null){	
			     txn.rollback();
			 }
				log.error("Error during reset pwd...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {
			 if(txn!=null){	 
			     txn.rollback();
			 }
			
			log.error("Error during reset pwd...",e);
			 throw new ApplicationException(e);
		 }
		
		return studentlogin;
	}

	/* (non-Javadoc)
	 * checks username exists or not
	 * @see com.kp.cms.transactions.admin.IGeneratePasswordTransaction#checkUserExists(java.lang.String)
	 */
	@Override
	public boolean checkUserExists(String userName) throws Exception {
		boolean result=false;
		Session session = null;
		try {
			 
			 session = HibernateUtil.getSession();
			 Query query=session.createQuery("from StudentLogin sl where sl.userName= :UNM");
			 query.setString("UNM",userName);
			 List<StudentLogin> studentlogins=query.list();
		     if(studentlogins!=null && !studentlogins.isEmpty()){
				result=true;
			 }
			 session.flush();
			  }catch (ConstraintViolationException e) {	
				log.error("Error during checking user exists or not...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {

			log.error("Error during checking user exists or not...",e);
			 throw new ApplicationException(e);
		 }
		
		return result;
		
	}
	/* (non-Javadoc)
	 * checks username exists or not
	 * @see com.kp.cms.transactions.admin.IGeneratePasswordTransaction#checkUserExists(java.lang.String)
	 */
	@Override
	public List<String> getAllUserNamesPresent() throws Exception {
		List<String> usernames;
		Session session = null;
		try {
			
			 session = HibernateUtil.getSession();
			 Query query=session.createQuery("select s.userName from StudentLogin s where s.isActive = 1");
			
			 usernames=query.list();

			 session.flush();
			
		 }catch (ConstraintViolationException e) {	
				log.error("Error during getAllUserNamesPresent...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {
			log.error("Error during getAllUserNamesPresent...",e);
			 throw new ApplicationException(e);
		 }
		
		return usernames;
		
	}
	/**
	 * 
	 */
	public List<Student> getStudentsDetails(int year, String regNos, boolean isRollNo ) throws Exception {
		Session session = null;
		 List<Student> stList;
		try{
			StringBuffer quer;
			if(isRollNo){
				quer=new StringBuffer("from Student st where st.rollNo in( '"  + regNos +  "') and st.isAdmitted=1 and st.admAppln.appliedYear= :applyYear ");
			}else
			{
				quer=new StringBuffer("from Student st where st.registerNo in( '"  + regNos +  "') and st.isAdmitted=1 and st.admAppln.appliedYear= :applyYear ");
			}
			 
		 session = HibernateUtil.getSession();
		 Query query=session.createQuery(quer.toString());
		 query.setInteger("applyYear", year);
		 stList=query.list();
		}catch(Exception e){
			 throw  new ApplicationException(e);
		}
		return stList;
	}
	/**
	 * 
	 * @param studentIds
	 * @return
	 * @throws Exception
	 */
	public boolean deleteStudentLogins(String studentIds)throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtil.getSession();
		    tx= session.beginTransaction();
			Query query = session.createQuery("delete from StudentLogin sl where sl.student.id in (" + studentIds + ")");
   		    query.executeUpdate();
			tx.commit();
			session.flush();
			//session.close();
			return true;
		} catch (ConstraintViolationException e) {
			if(tx!=null)
			    tx.rollback();
			log.error("Error in deleteStudentLogins..."	, e);
			throw new BusinessException(e);
		} catch (Exception e) {
			if(tx!=null)
			     tx.rollback();
			log.error("Error in deleteStudentLogins.." + e);
			throw new ApplicationException(e);
		}
	}
	
	public List<Student> getStudents(int year, int programId,Set<Integer> tempcourseset)
	throws Exception {
		Session session = null;
		List<Student> stList;
		try{
			StringBuffer quer=new StringBuffer("from Student st where  st.isAdmitted=1  and st.admAppln.isCancelled=0 and " +
					" st.id not in (select s.student.id from ExamStudentDetentionRejoinDetails s "+
			        " where (s.detain=1 or s.discontinued=1) and (s.rejoin = 0 or s.rejoin is null)) " +
					" and st.admAppln.courseBySelectedCourseId.program.id=" +programId +" and st.admAppln.appliedYear="+year);
	 if(tempcourseset!=null && !tempcourseset.isEmpty()){
		 quer.append(" and st.admAppln.courseBySelectedCourseId.id in (:courselist)");
	 }
	 quer.append(" order by st.admAppln.courseBySelectedCourseId.id");
	 session = HibernateUtil.getSession();
	 Query query=session.createQuery(quer.toString());
	 if(tempcourseset!=null && !tempcourseset.isEmpty()){
		 query.setParameterList("courselist", tempcourseset);
	 }
	 stList=query.list();
		}catch(Exception e){
	 throw  new ApplicationException(e);
		}
		return stList;
	}

	@Override
	public int getRecordId(int id) throws Exception {
		Session session = null;
		int recordId = 0;
		try{
			session = HibernateUtil.getSession();
			String s = "";
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		return 0;
	}

	@Override
	public List<Student> getSearchedStudentsParent(int progid, int year)
			throws Exception {
		Session session = null;
		 List<Student> stList;
		try{
		StringBuffer quer=new StringBuffer("from Student st where st.id  in(select sl.student.id from StudentLogin sl where sl.isActive =1 and sl.isParent = 1) and st.isAdmitted=1 and st.admAppln.courseBySelectedCourseId.program.id= :progId and st.admAppln.appliedYear= :applyYear ");
			 
		 session = HibernateUtil.getSession();
		 Query query=session.createQuery(quer.toString());
		 query.setInteger("progId", progid);
		 query.setInteger("applyYear", year);
		 stList=query.list();
		}catch(Exception e){
			 throw  new ApplicationException(e);
		}
		return stList;
	}

	@Override
	public List<String> getAllUserNamesPresentForParent() throws Exception {
		List<String> usernames;
		Session session = null;
		try {
			
			 session = HibernateUtil.getSession();
			 Query query=session.createQuery("select s.userName from StudentLogin s  where s.isActive = 1 and s.isParentGenerated = 1");
			
			 usernames=query.list();

			 session.flush();
			
		 }catch (ConstraintViolationException e) {	
				log.error("Error during getAllUserNamesPresent...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {
			log.error("Error during getAllUserNamesPresent...",e);
			 throw new ApplicationException(e);
		 }
		
		return usernames;
	}

	@Override
	public StudentLogin getObj(int id) throws Exception {
		Session session = null;
		StudentLogin login = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from StudentLogin s where  s.student.id = :studentId and s.isParent = 1";
			Query query = session.createQuery(s)
							.setInteger("studentId", id);
			login = (StudentLogin)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return login;
	}

	@Override
	public List<String> getAllUserNamesPresentInAppliedYear(int year)
			throws Exception {
		List<String> usernames;
		Session session = null;
		try {
			
			 session = HibernateUtil.getSession();
			 Query query=session.createQuery("select s.userName from StudentLogin s where s.isActive = 1 and s.student.admAppln.appliedYear="+year);
			
			 usernames=query.list();

			 session.flush();
			
		 }catch (ConstraintViolationException e) {	
				log.error("Error during getAllUserNamesPresent...",e);
				 throw new BusinessException(e);
		}catch (Exception e) {
			log.error("Error during getAllUserNamesPresent...",e);
			 throw new ApplicationException(e);
		 }
		
		return usernames;
		
	}
	
	
}
