package com.kp.cms.transactionsimpl.studentExtentionActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackSession;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionFeedbackTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;

public class StudentExtentionFeedbackTransactionImpl implements IStudentExtentionFeedbackTransaction{
	
	private static volatile StudentExtentionFeedbackTransactionImpl obj;
	public static StudentExtentionFeedbackTransactionImpl getInstance()
	{
		if(obj == null)
		{
			obj = new StudentExtentionFeedbackTransactionImpl();
		}
		return obj;
	}
	@Override
	public List<StudentExtentionFeedback> getFeedbackList(int year
			) throws Exception {
		Session session = null;
		List<StudentExtentionFeedback> feedbackConnection = null;
		try{
			session = HibernateUtil.getSession();
			String str ="select conn from StudentExtentionFeedback conn join conn.classId cl join cl.classSchemewises cls " +
					" join cls.curriculumSchemeDuration cud where conn.isActive=1 and cl.isActive=1 and cud.academicYear="+year;
			Query query = session.createQuery(str);
			feedbackConnection = query.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
		return feedbackConnection;
	}
	@Override
	public List<EvaluationStudentFeedbackSession> getSessionDetails(Integer year)
			throws Exception {
		Session session = null;
		List<EvaluationStudentFeedbackSession> sessionsList = null;
		try{
			session = HibernateUtil.getSession();
			String str = "from EvaluationStudentFeedbackSession session where session.isactive =1  and session.academicYear="+year;
			Query query  = session.createQuery(str);
			sessionsList =query.list();
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
				}
			}
		return sessionsList;
	}
	@Override
	public List<StudentExtentionFeedback> getConnectionListBySessionId() throws Exception {
		Session session = null;
		List<StudentExtentionFeedback> feedbackConnection = null;
		try{
			session = HibernateUtil.getSession();
			String str = "select conn from StudentExtentionFeedback conn " +
			" where conn.isActive=1 and conn.classId.isActive=1 and  (current_date between conn.startDate and conn.endDate)";
			Query query = session.createQuery(str);
			feedbackConnection = query.list();
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ApplicationException();
		}
		finally{
			if(session != null){
				session.flush();
			}
		}
		return feedbackConnection;
	}
	@Override
	public List<StudentExtentionFeedback> getConnectionListBySessionIdNew()
			throws Exception {
		Session session = null;
		List<StudentExtentionFeedback> connection;
		try{
			session = HibernateUtil.getSession();
			String str = "select conn from StudentExtentionFeedback conn join conn.classesId cl join cl.classSchemewises cls " +
					" join cls.curriculumSchemeDuration cud where conn.isActive=1 and cl.isActive=1 and  (sysdate() between conn.startDate and conn.endDate)";
			
			Query query = session.createQuery(str);
			connection = query.list();
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
		}
	}
		return connection;
	}
	
	@Override
	public boolean checkDuplicate(StudentExtentionFeedbackForm feedbackform)
			throws Exception {
		Session session = null;
		boolean isDuplicate = false;
		try{
			String[] clsId = feedbackform.getClassesId();
			session = HibernateUtil.getSession();
			int i =0;
			for(i=0;i<clsId.length;i++){
				int classId = Integer.parseInt(clsId[i]);
				String str = "from StudentExtentionFeedback connection where connection.isActive=1 " +
							" and connection.classId.id=" +classId;
				Query query =session.createQuery(str);
				StudentExtentionFeedback connection = (StudentExtentionFeedback) query.uniqueResult();
				if(connection!=null && !connection.toString().isEmpty()){
					if(connection.getId() == feedbackform.getId()){
						isDuplicate = false;
					}else{
						isDuplicate = true;
						
					}
				}
			}
		}catch (Exception e) {
			isDuplicate = false;
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
			}
		}
		return isDuplicate;
	}
	
	@Override
	public Map<Integer, Integer> getSpecializationIds(String[] classesIds,
			String specializationName) throws Exception {
		Session session = null;
		Map<Integer,Integer> specializationIds = new HashMap<Integer, Integer>();
		try{
			session = HibernateUtil.getSession();
			if(classesIds!=null && classesIds.length !=0 ){
				String[] classesId = classesIds;
				int i =0;
				for(i=0;i<classesId.length;i++){
					Integer classId = Integer.parseInt(classesId[i]);
					String str = "from ExamSpecializationBO exam where exam.courseUtilBO.courseID in (select cls.course.id from Classes cls where cls.id = "+classId+") and exam.isActive=1 and exam.name='"+specializationName+"'";
					Query query = session.createQuery(str);
					ExamSpecializationBO bo = (ExamSpecializationBO) query.uniqueResult();
					if(bo!=null && !bo.toString().isEmpty()){
						specializationIds.put(classId, bo.getId());
					}
				}
			}
			
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
				}
			}
		return specializationIds;
	}
	
	@Override
	public String getSpecializationName(String specializationId)
			throws Exception {
		Session session =null;
		String specializationName = null;
			try{
				session = HibernateUtil.getSession();
				String str = "select spec.name from studentSpecialization spec where spec.isActive =1 and spec.id="+Integer.parseInt(specializationId);
				Query query = session.createQuery(str);
				specializationName = (String) query.uniqueResult();
			}catch (Exception e) {
				throw new ApplicationException(e);
			}
			finally {
			if (session != null) {
				session.flush();
				session.close();
					}
				}
			return specializationName;
	}
	@Override
	public boolean submitOpenConnectionDetails(
			List<StudentExtentionFeedback> boList) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isAdded = false;
		try{
			session  = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			if(boList!=null && !boList.isEmpty()){
				Iterator<StudentExtentionFeedback> iterator = boList.iterator();
				while (iterator.hasNext()) {
					StudentExtentionFeedback StudentFeedbackConnection = (StudentExtentionFeedback) iterator
							.next();
					session.save(StudentFeedbackConnection);
				}
			}
			tx.commit();
		//	session.flush();
			isAdded = true;
			}catch (Exception e) {
				isAdded =false;
				throw new ApplicationException(e);
			}
			finally {
				if (session != null) {
					session.flush();
					session.close();
				}
			}
		return isAdded;
	}
	@Override
	public StudentExtentionFeedback getFeedbackDetails(int id) throws Exception {
		Session session = null;
		StudentExtentionFeedback connection;
		try{
			session = HibernateUtil.getSession();
			String str = "from StudentExtentionFeedback conn where conn.id="+id+" and conn.isActive = 1";
			Query query = session.createQuery(str);
			connection = (StudentExtentionFeedback) query.uniqueResult();
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
		}
	}
		return connection;
	}
	@Override
	public boolean deleteOpenConnection(
			StudentExtentionFeedbackForm feedbackform) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean isDeleted = false;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			StudentExtentionFeedback connection = (StudentExtentionFeedback)session.get(StudentExtentionFeedback.class, feedbackform.getId());
			connection.setLastModifiedDate(new Date());
			connection.setModifiedBy(feedbackform.getUserId());
			connection.setIsActive(false);
			session.update(connection);
			tx.commit();
			isDeleted = true;
		}catch (Exception e) {
			isDeleted = false;
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
				}
			}
		return isDeleted;
	}
	
	@Override
	public boolean updateOpenConnection(
			StudentExtentionFeedbackForm feedbackform) throws Exception {
		Session session = null;
		StudentExtentionFeedback connection = null;
		Transaction tx = null;
		boolean isUpdated = false;
		try{
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			tx.begin();
			connection = (StudentExtentionFeedback)session.get(StudentExtentionFeedback.class, feedbackform.getId());
			Classes classes = new Classes();
			String[] clsId = feedbackform.getClassesId();
			classes.setId(Integer.parseInt(clsId[0]));
			connection.setClassId(classes);
			connection.setStartDate(CommonUtil.ConvertStringToDate(feedbackform.getStartDate()));
			connection.setEndDate(CommonUtil.ConvertStringToDate(feedbackform.getEndDate()));
			connection.setLastModifiedDate(new Date());
			connection.setModifiedBy(feedbackform.getUserId());
			connection.setIsActive(true);
			session.update(connection);
			tx.commit();
			isUpdated = true;
			
			
		}catch (Exception e) {
			throw new ApplicationException(e);
		}
		finally {
		if (session != null) {
			session.flush();
			session.close();
				}
			}
		return isUpdated;
	}
	
	@Override
	public List<StudentGroup> getStudentGroupDetails() throws Exception {
		Session session = null;
		try{
			session = HibernateUtil .getSession();
			Query query =session.createQuery("from StudentGroup sg where isActive=1 order by sg.groupName ");
			List<StudentGroup> studentGroup = query.list();
			return studentGroup;
		}catch(Exception ex){
			if(session != null)
			{
			  session.flush();	
			}
		}
		throw new ApplicationException();
		}
	@Override
	public List<StudentExtention> getStudentExtentionDetails() throws Exception {
		Session session = null;
		try{
			session = HibernateUtil .getSession();
			Query query =session.createQuery("from StudentExtention sg where isActive=1 order by sg.activityName");
			List<StudentExtention> studentextention = query.list();
			return studentextention;
		}catch(Exception ex){
			if(session != null)
			{
			  session.flush();	
			}
		}
		throw new ApplicationException();
	}
	@Override
	public int getRecordId(String classId) throws Exception {
		Session session = null;
		int recordId = 0;
		try{
			session = HibernateUtil.getSession();
			String s = "select sef.id from StudentExtentionFeedback sef where sef.classId= :classId";
			Query query = session.createQuery(s)
						.setString("classId", classId);
			recordId = (Integer)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return recordId;
	}
	
	}
	
	


