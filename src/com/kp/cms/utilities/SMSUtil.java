package com.kp.cms.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admission.SendAllotmentMemoSmsBo;
import com.kp.cms.handlers.admission.SendAllotmentMemoSmsHandler;
import com.kp.cms.bo.admin.MobileMessaging;
import com.kp.cms.bo.admin.MobileMessagingSchedule;
import com.kp.cms.bo.admin.PasswordMobileMessaging;
import com.kp.cms.bo.admin.SendBulkSmsToStudentParentsBo;
import com.kp.cms.bo.admin.SendBulkSmsToStudentParentsNewBo;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.utilities.jms.SMS_Message;

public class SMSUtil {
	
	/**
	 * getting the list of SMS
	 * @return
	 * @throws Exception
	 */
	private static final Log log = LogFactory.getLog(SendAllotmentMemoSmsHandler.class);
	
	public List<MobileMessaging> getListOfSMS() throws Exception{
		Session session = null;
		List<MobileMessaging> messageList = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			//Query selectedCandidatesQuery=session.createQuery("from MobileMessaging m where m.isMessageSent=0 and m.messageEnqueueDate=current_date");
			Query selectedCandidatesQuery=session.createQuery("from MobileMessaging m where m.isMessageSent=0 and m.attendanceDate=current_date");
			//selectedCandidatesQuery.setMaxResults(50);
			messageList = selectedCandidatesQuery.list();
			return messageList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	/**
	 * making the mobile messaging property is messageSent to true
	 * @return
	 * @throws Exception
	 */
	public void updateSMS(List<MobileMessaging> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<MobileMessaging> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				MobileMessaging mobileMessaging = (MobileMessaging) iterator.next();
				mobileMessaging.setIsMessageSent(true);
				session.merge(mobileMessaging);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	/**
	 * sample method
	 */
	public void sentSMS(){
		List<MobileMessaging> updateList=new ArrayList<MobileMessaging>();
		try{
			boolean isFlag=false;
			
			List<MobileMessaging> list=getListOfSMS();
			if(list!=null && !list.isEmpty()){
				Iterator<MobileMessaging> itr=list.iterator();
				while (itr.hasNext()) {
					MobileMessaging mobileMessaging = (MobileMessaging) itr.next();
					SMS_Message smsTo=new SMS_Message();
					isFlag=CommonUtil.sendSMS(smsTo);
					if(isFlag){
						updateList.add(mobileMessaging);
					}
				}
			}
		}catch(Exception e){
			
		}finally{
			try {
				updateSMS(updateList);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	

	public List<MobileMessagingSchedule> getMessageList()throws Exception{
		List<MobileMessagingSchedule> list = null;
		Session session =null;
		try{
			session=HibernateUtil.getSession();
			Query query=session.createQuery("from MobileMessagingSchedule m where m.isMessageSent=0");
			list=query.list();
			return list;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			session.flush();
//			session.close();
		}
		return null;
	}
	
	
	public void updateSMSForSchedule(List<MobileMessagingSchedule> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<MobileMessagingSchedule> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				MobileMessagingSchedule mobileMessagingSchedule = (MobileMessagingSchedule) iterator.next();
				mobileMessagingSchedule.setIsMessageSent(true);
				session.update(mobileMessagingSchedule);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	
	public List<PasswordMobileMessaging> getListOfSMSPassword() throws Exception{
		Session session = null;
		List<PasswordMobileMessaging> messageList = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Query selectedCandidatesQuery=session.createQuery("from PasswordMobileMessaging m where m.isMessageSent=0");
			selectedCandidatesQuery.setMaxResults(10);
			messageList = selectedCandidatesQuery.list();
			if(messageList!=null && !messageList.isEmpty()){
				Iterator<PasswordMobileMessaging> itr=messageList.iterator();
				Transaction transaction=session.beginTransaction();
				while (itr.hasNext()) {
					PasswordMobileMessaging mobileMessaging =itr .next();
					mobileMessaging.setIsMessageSent(true);
					session.update(mobileMessaging);
				}
				transaction.commit();
			}
			return messageList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	
	/**
	 * making the mobile messaging property is messageSent to true
	 * @return
	 * @throws Exception
	 */
	public void updateSMSPassword(List<PasswordMobileMessaging> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<PasswordMobileMessaging> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				PasswordMobileMessaging mobileMessaging = (PasswordMobileMessaging) iterator.next();
				mobileMessaging.setIsMessageSent(true);
				session.merge(mobileMessaging);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}

	public List<SendAllotmentMemoSmsBo> getAllotmentMemoSMSList() throws Exception{
		Session session = null;
		List<SendAllotmentMemoSmsBo> messageList = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Query selectedCandidatesQuery=session.createQuery("from SendAllotmentMemoSmsBo s where s.isMessageSent=0 and s.createdDate=current_date");
			//selectedCandidatesQuery.setMaxResults(50);
			messageList = selectedCandidatesQuery.list();
			return messageList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	public boolean updateSMSList(List<SendAllotmentMemoSmsBo> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		boolean sent=false;
		int i=0;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<SendAllotmentMemoSmsBo> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				SendAllotmentMemoSmsBo mobileMessaging = (SendAllotmentMemoSmsBo) iterator.next();
				mobileMessaging.setIsMessageSent(true);
				System.out.println(mobileMessaging.getId()+"final update time of  SendBulkSmsToStudentParentsBo id++++++++++++++++++ final update time of student id ++++++++++++++++++: "+mobileMessaging.getStudentId());
				
				session.update(mobileMessaging);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
					
				}
				i++;
			}
			transaction.commit();
			System.out.println(" SendBulkSmsToStudentParentsBo final list size of update time++++++++++++++++++ +++++++++++++++: "+i);
			
			sent=true;
		} catch (Exception e) {
			log.debug(" ******************* sent sms updated time in log debug ***********************"+e.getMessage());
			System.out.println("++++++++++++++++++ sent sms updated time in sysout ++++++++++++++++++: "+e.getStackTrace());
			
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return sent;
	}
	public boolean updateSMS1List(List<SendBulkSmsToStudentParentsNewBo> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		boolean sent=false;
		int i=0;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<SendBulkSmsToStudentParentsNewBo> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				SendBulkSmsToStudentParentsNewBo mobileMessaging = (SendBulkSmsToStudentParentsNewBo) iterator.next();
				mobileMessaging.setIsMessageSent(true);
				System.out.println(mobileMessaging.getId()+"final update time of  SendBulkSmsToStudentParentsBo id++++++++++++++++++ final update time of student id ++++++++++++++++++: "+mobileMessaging.getStudentId());
				
				session.update(mobileMessaging);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
					
				}
				i++;
			}
			transaction.commit();
			System.out.println(" SendBulkSmsToStudentParentsBo final list size of update time++++++++++++++++++ +++++++++++++++: "+i);
			
			sent=true;
		} catch (Exception e) {
			log.debug(" ******************* sent sms updated time in log debug ***********************"+e.getMessage());
			System.out.println("++++++++++++++++++ sent sms updated time in sysout ++++++++++++++++++: "+e.getStackTrace());
			
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return sent;
	}
	
	
	public List<SendBulkSmsToStudentParentsBo> getSMSList() throws Exception{
		Session session = null;
		List<SendBulkSmsToStudentParentsBo> messageList = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Query selectedCandidatesQuery=session.createQuery("from SendBulkSmsToStudentParentsBo s where s.isMessageSent=0 and s.createdDate=current_date");
			//selectedCandidatesQuery.setMaxResults(50);
			messageList = selectedCandidatesQuery.list();
			return messageList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	
	public boolean updateSMSLists(List<SendBulkSmsToStudentParentsBo> smsList) throws Exception{
		Session session = null;
		Transaction transaction=null;
		boolean sent=false;
		int i=0;
		try {
			session = InitSessionFactory.getInstance().openSession();
			transaction=session.beginTransaction();
			Iterator<SendBulkSmsToStudentParentsBo> iterator=smsList.iterator();
			int count=0;
			while (iterator.hasNext()) {
				SendBulkSmsToStudentParentsBo mobileMessaging = (SendBulkSmsToStudentParentsBo) iterator.next();
				mobileMessaging.setIsMessageSent(true);
				System.out.println(mobileMessaging.getId()+"final update time of  SendBulkSmsToStudentParentsBo id++++++++++++++++++ final update time of student id ++++++++++++++++++: "+mobileMessaging.getStudentId());
				
				session.update(mobileMessaging);
				if(++count % 20 == 0){
					session.flush();
					session.clear();
					
				}
				i++;
			}
			transaction.commit();
			System.out.println(" SendBulkSmsToStudentParentsBo final list size of update time++++++++++++++++++ +++++++++++++++: "+i);
			
			sent=true;
		} catch (Exception e) {
			log.debug(" ******************* sent sms updated time in log debug ***********************"+e.getMessage());
			System.out.println("++++++++++++++++++ sent sms updated time in sysout ++++++++++++++++++: "+e.getStackTrace());
			
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return sent;
	}
	
	public List<SendBulkSmsToStudentParentsNewBo> getSMSList1() throws Exception{
		Session session = null;
		List<SendBulkSmsToStudentParentsNewBo> messageList = null;
		try {
			session = InitSessionFactory.getInstance().openSession();
			Query selectedCandidatesQuery=session.createQuery("from SendBulkSmsToStudentParentsNewBo s where s.isMessageSent=0 and s.createdDate=current_date");
			//selectedCandidatesQuery.setMaxResults(50);
			messageList = selectedCandidatesQuery.list();
			return messageList;
		} catch (Exception e) {
			throw  new ApplicationException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
	}
	


}
