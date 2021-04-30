package com.kp.cms.transactionsimpl.timetable;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.Period;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.admin.TTClasses;
import com.kp.cms.bo.admin.TTClassesHistory;
import com.kp.cms.bo.admin.TTPeriodWeek;
import com.kp.cms.bo.admin.TTPeriodWeekHistory;
import com.kp.cms.bo.admin.TTSubjectBatch;
import com.kp.cms.bo.admin.TTSubjectBatchHistory;
import com.kp.cms.bo.admin.TTUsers;
import com.kp.cms.bo.admin.TTUsersHistory;
import com.kp.cms.bo.admin.TeacherClassSubject;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.timetable.TimeTableForClassForm;
import com.kp.cms.transactions.timetable.ITimeTableForClassTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class TimeTableForClassTransactionImpl implements
		ITimeTableForClassTransaction {
	/**
	 * Singleton object of TimeTableForClassTransactionImpl
	 */
	private static volatile TimeTableForClassTransactionImpl timeTableForClassTransactionImpl = null;
	private static final Log log = LogFactory.getLog(TimeTableForClassTransactionImpl.class);
	private TimeTableForClassTransactionImpl() {
		
	}
	/**
	 * return singleton object of TimeTableForClassTransactionImpl.
	 * @return
	 */
	public static TimeTableForClassTransactionImpl getInstance() {
		if (timeTableForClassTransactionImpl == null) {
			timeTableForClassTransactionImpl = new TimeTableForClassTransactionImpl();
		}
		return timeTableForClassTransactionImpl;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.timetable.ITimeTableForClassTransaction#addTimeTableForaPeriod(java.util.List, com.kp.cms.forms.timetable.TimeTableForClassForm)
	 */
	public boolean addTimeTableForaPeriod(List<TTSubjectBatch> boList, TimeTableForClassForm timeTableForClassForm) throws Exception {
		log.debug("inside addTermsConditionCheckList");
		Session session = null;
		Transaction transaction = null;
		TTSubjectBatch subjectBatch;
		try {
			if(timeTableForClassForm.getTtClassId()>0 && timeTableForClassForm.isChanged() && timeTableForClassForm.getFinalApprove().equalsIgnoreCase("on"))
				saveHistory(timeTableForClassForm.getTtClassId());
			session = InitSessionFactory.getInstance().openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			
			
			TTPeriodWeek periodBo=null;
			TTClasses ttClasses=null;
			if(timeTableForClassForm.getTtClassId()>0){
				ttClasses=(TTClasses)session.get(TTClasses.class,timeTableForClassForm.getTtClassId());
				ttClasses.setLastModifiedDate(new Date());
				ttClasses.setModifiedBy(timeTableForClassForm.getUserId());
				if(timeTableForClassForm.getTtClassId()>0 && timeTableForClassForm.isChanged() && timeTableForClassForm.getFinalApprove().equalsIgnoreCase("on"))
					ttClasses.setIsApproved(false);
				session.update(ttClasses);
			}else{
				ttClasses=new TTClasses();
				ClassSchemewise classSchemewise=new ClassSchemewise();
				classSchemewise.setId(Integer.parseInt(timeTableForClassForm.getClassId()));
				ttClasses.setClassSchemewise(classSchemewise);
				ttClasses.setCreatedBy(timeTableForClassForm.getUserId());
				ttClasses.setCreatedDate(new Date());
				ttClasses.setLastModifiedDate(new Date());
				ttClasses.setModifiedBy(timeTableForClassForm.getUserId());
				ttClasses.setIsApproved(false);
				ttClasses.setIsActive(true);
				session.save(ttClasses);
			}
				
			if(timeTableForClassForm.getTtPeriodWeekId()>0){
				periodBo=(TTPeriodWeek)session.get(TTPeriodWeek.class, timeTableForClassForm.getTtPeriodWeekId());
				periodBo.setLastModifiedDate(new Date());
				periodBo.setModifiedBy(timeTableForClassForm.getUserId());
				session.update(periodBo);
			}else{
				periodBo=new TTPeriodWeek();
				Period period=new Period();
				period.setId(timeTableForClassForm.getPeriodId());
				periodBo.setPeriod(period);
				periodBo.setWeekDay(timeTableForClassForm.getWeek());
				periodBo.setLastModifiedDate(new Date());
				periodBo.setModifiedBy(timeTableForClassForm.getUserId());
				periodBo.setCreatedDate(new Date());
				periodBo.setCreatedBy(timeTableForClassForm.getUserId());
				periodBo.setIsActive(true);
				periodBo.setTtClasses(ttClasses);
				session.save(periodBo);
			}
			
			Iterator<TTSubjectBatch> tcIterator = boList.iterator();
			int count = 0;
			while(tcIterator.hasNext()){
				subjectBatch = tcIterator.next();
				subjectBatch.setTtPeriodWeek(periodBo);
				session.saveOrUpdate(subjectBatch);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			session.flush();
			log.debug("leaving addTermsConditionCheckList");
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			log.error("Error in addTermsConditionCheckList impl...", e);
			throw new BusinessException(e);
		} catch (Exception e) {
			transaction.rollback();
			log.error("Error in addTermsConditionCheckList impl...", e);
			throw new ApplicationException(e);
		}
	}
	/**
	 * @param ttClasses
	 */
	private void saveHistory(int id) throws Exception {
		
		log.debug("inside addTermsConditionCheckList");
		Session session = null;
		Transaction transaction = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			TTClasses ttClasses=(TTClasses)session.get(TTClasses.class,id);
			TTClassesHistory ttClassesHistory=new TTClassesHistory();
			PropertyUtils.copyProperties(ttClassesHistory, ttClasses);

			Set<TTPeriodWeekHistory> periodHistSet=new HashSet<TTPeriodWeekHistory>();
			Iterator<TTPeriodWeek> periodItr=ttClasses.getTtPeriodWeeks().iterator();
			while (periodItr.hasNext()) {
				TTPeriodWeek ttPeriodWeek = periodItr .next();
				TTPeriodWeekHistory ttPeriodWeekHistory=new TTPeriodWeekHistory();
				PropertyUtils.copyProperties(ttPeriodWeekHistory, ttPeriodWeek);
				
				Set<TTSubjectBatch> subjectBatchs=ttPeriodWeek.getTtSubjectBatchs();
				Set<TTSubjectBatchHistory> subjectBatchsHist=new HashSet<TTSubjectBatchHistory>();
				
				Iterator<TTSubjectBatch> subItr=subjectBatchs.iterator();
				while (subItr.hasNext()) {
					TTSubjectBatch ttSubjectBatch = subItr .next();
					TTSubjectBatchHistory ttSubjectBatchHistory=new TTSubjectBatchHistory();
					PropertyUtils.copyProperties(ttSubjectBatchHistory, ttSubjectBatch);
					
					Iterator<TTUsers> uesrItr=ttSubjectBatch.getTtUsers().iterator();
					Set<TTUsersHistory> userHist=new HashSet<TTUsersHistory>();
					while (uesrItr.hasNext()) {
						TTUsers ttUsers = uesrItr.next();
						TTUsersHistory ttUsersHistory=new TTUsersHistory();
						PropertyUtils.copyProperties(ttUsersHistory, ttUsers);
						
						ttUsersHistory.setId(0);
						userHist.add(ttUsersHistory);
					}
					ttSubjectBatchHistory.setTtUsersHistory(userHist);
					ttSubjectBatchHistory.setId(0);
					subjectBatchsHist.add(ttSubjectBatchHistory);
				}
				ttPeriodWeekHistory.setId(0);
				ttPeriodWeekHistory.setTtSubjectBatchsHistory(subjectBatchsHist);
				periodHistSet.add(ttPeriodWeekHistory);
			}
			ttClassesHistory.setTtPeriodWeeksHistory(periodHistSet);
			ttClassesHistory.setId(0);
			ttClassesHistory.setDate(new Date());
			session.save(ttClassesHistory);
			transaction.commit();
		session.flush();
		session.clear();
		session.close();
		log.debug("leaving addTermsConditionCheckList");
	} catch (ConstraintViolationException e) {
		transaction.rollback();
		log.error("Error in addTermsConditionCheckList impl...", e);
		throw new BusinessException(e);
	} catch (Exception e) {
		transaction.rollback();
		log.error("Error in addTermsConditionCheckList impl...", e);
		throw new ApplicationException(e);
	}
		
		
		
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.timetable.ITimeTableForClassTransaction#updateFlagForTimeTable(java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean updateFlagForTimeTable(String userId, int ttClassId,
			String finalApprove) throws Exception {
		log.debug("inside addTermsConditionCheckList");
		Session session = null;
		Transaction transaction = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction = session.beginTransaction();
			transaction.begin();
			TTClasses ttClasses=(TTClasses)session.get(TTClasses.class, ttClassId);
			if(finalApprove.equalsIgnoreCase("on"))
				ttClasses.setIsApproved(true);
			else
				ttClasses.setIsApproved(false);
			ttClasses.setLastModifiedDate(new Date());
			ttClasses.setModifiedBy(userId);
			
			session.update(ttClasses);
			if(finalApprove.equalsIgnoreCase("on")){
				//session.createQuery("delete from TeacherClassSubject ts where ts.classId="+ttClasses.getClassSchemewise().getId()).executeUpdate();   /* by chandra */
				Iterator<TTPeriodWeek> periodItr=ttClasses.getTtPeriodWeeks().iterator();
				while (periodItr.hasNext()) {
					TTPeriodWeek ttPeriodWeek =periodItr.next();
					Iterator<TTSubjectBatch> subItr=ttPeriodWeek.getTtSubjectBatchs().iterator();
					while (subItr.hasNext()) {
						TTSubjectBatch ttSubjectBatch = subItr .next();
						if(ttSubjectBatch.getIsActive() && ttSubjectBatch.getSubject()!=null){
						Iterator<TTUsers> userItr=ttSubjectBatch.getTtUsers().iterator();
						while (userItr.hasNext()) {
							TTUsers ttUsers = userItr.next();
							List list=session.createQuery("from TeacherClassSubject t where t.classId="+ttClasses.getClassSchemewise().getId()+" and t.year="+ttClasses.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()+" and t.teacherId="+ttUsers.getUsers().getId()+" and t.subject.id="+ttSubjectBatch.getSubject().getId()).list();
							/*if(list==null || list.isEmpty()){
								TeacherClassSubject teacherClassSubject=new TeacherClassSubject(0,ttClasses.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear().toString(),ttUsers.getUsers(),ttClasses.getClassSchemewise(),ttSubjectBatch.getSubject(),null,true,userId,new Date(),userId,new Date());
								session.save(teacherClassSubject);
							}*/
							if(list!=null && !list.isEmpty()){
								Iterator<TeacherClassSubject> tcList=list.iterator();
								while (tcList.hasNext()) {
									TeacherClassSubject tc =(TeacherClassSubject)tcList.next();
									tc.setLastModifiedDate(new Date());
									tc.setModifiedBy(userId);
									session.update(tc);
									}
								}else{
										TeacherClassSubject tc =new TeacherClassSubject();
										ClassSchemewise cs=new ClassSchemewise();
										Subject sub=new Subject();
										Users us=new Users();
										us.setId(ttUsers.getUsers().getId());
										sub.setId(ttSubjectBatch.getSubject().getId());
										cs.setId(ttClasses.getClassSchemewise().getId());
										tc.setClassId(cs);
										tc.setSubject(sub);
										tc.setTeacherId(us);
										tc.setYear((ttClasses.getClassSchemewise().getCurriculumSchemeDuration().getAcademicYear()).toString());
										tc.setNumericCode(null);
										tc.setIsActive(true);
										tc.setCreatedBy(userId);
										tc.setCreatedDate(new Date());
										tc.setLastModifiedDate(new Date());
										tc.setModifiedBy(userId);
										session.save(tc);
									
								}
							}
						}
					}
				}
			}
			transaction.commit();
			session.flush();
			log.debug("leaving addTermsConditionCheckList");
			return true;
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			log.error("Error in addTermsConditionCheckList impl...", e);
			throw new BusinessException(e);
		} catch (Exception e) {
			transaction.rollback();
			log.error("Error in addTermsConditionCheckList impl...", e);
			throw new ApplicationException(e);
		}
	}
	/* (non-Javadoc)
	 * checks the TTClassHistory table for data for the input class id and returns true or false
	 * @see com.kp.cms.transactions.timetable.ITimeTableForClassTransaction#checkForTtClassHistory(java.lang.String)
	 */
	@Override
	public boolean checkForTtClassHistory(String classId) throws Exception {
		boolean historyExists=false;
		Session session = null;
		List<TTClassesHistory> ttClassHistoryList = null;
		try {
			session = HibernateUtil.getSession();
			ttClassHistoryList = session.createQuery("from TTClassesHistory tc where tc.classSchemewise.id=:classSchemeId").setInteger("classSchemeId",
					Integer.parseInt(classId)).list();
			if(ttClassHistoryList!=null && !ttClassHistoryList.isEmpty())
					historyExists=true;
			return historyExists;
		} catch (Exception e) {
			log.error("Error while retrieving selected candidates.." +e);
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}
}
