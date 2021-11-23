package com.kp.cms.transactionsimpl.admission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.ApplicantSubjectGroup;
import com.kp.cms.bo.admin.CertificateCourse;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.bo.admin.SubjectGroup;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.attendance.SubjectGroupDetailsForm;
import com.kp.cms.to.admission.StudentCertificateCourseTO;
import com.kp.cms.to.attendance.SubjectGroupDetailsTo;
import com.kp.cms.transactions.admission.IAddOnCourseDetailsTransaction;
import com.kp.cms.utilities.HibernateUtil;

public class AddOnCourseDetailsTransactionImpl implements
IAddOnCourseDetailsTransaction {
	private static final Log log = LogFactory
			.getLog(StudentEditTransactionImpl.class);

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getStudentDetails(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Student> getStudentDetails(
			SubjectGroupDetailsForm subjectGroupDetailsForm) {
		Session session = null;
		List<Student> studentDetailsList = null;
		try {
			session = HibernateUtil.getSession();
			String str = "from Student s where s.classSchemewise.curriculumSchemeDuration.academicYear="
					+ subjectGroupDetailsForm.getYear()
					+ " and s.classSchemewise.id= "
					+ subjectGroupDetailsForm.getClassSchemewiseId() + "and s.admAppln.isCancelled=0";
			Query query = session.createQuery(str);
			studentDetailsList = query.list();
		} catch (Exception exception) {
			log.error("Error while getting subjectgroup details.....", exception);
		}
		return studentDetailsList;
	}
	
	
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getSubjectGroups(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	public List<ApplicantSubjectGroup> getSubjectGroups(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception{
		log.info("call of getSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		Session session = null;
		try {
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from ApplicantSubjectGroup appSubjectGroup where appSubjectGroup.subjectGroup.id=" +subjectGroupDetailsForm.getSubjectGroupId()+" group by appSubjectGroup.subjectGroup.id");
			 List<ApplicantSubjectGroup> list = query.list();
			 session.flush();
			 log.info("end of getSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
			 return list;
		 } catch (Exception e) {
			 if( session != null){
				 session.flush();
			 }
			 throw new ApplicationException(e);
		 }
	 }

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#addSubjectGroups(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	@Override
	public boolean addSubjectGroups(
			SubjectGroupDetailsForm subjectGroupDetailsForm) {
		log .info("call of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		boolean isAdded = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<StudentCertificateCourseTO> list = subjectGroupDetailsForm
					.getAddOnCourseList();
			if (list != null && !list.isEmpty()) {
				Iterator<StudentCertificateCourseTO> itr = list.iterator();
				int count = 0;
				List<String> subList= new ArrayList<String>();
				while (itr.hasNext()) {
					StudentCertificateCourseTO subjectGroupDetailsTo = (StudentCertificateCourseTO) itr
							.next();
					if (subjectGroupDetailsTo.getChecked()!=null && subjectGroupDetailsTo.getChecked().equalsIgnoreCase("on")) {
						// setting student specialization to Exam_student_biodata table
						//
						CertificateCourse subjectGroup = new CertificateCourse();
						AdmAppln admAppin = new AdmAppln();
						admAppin.setId(Integer.parseInt(subjectGroupDetailsTo.getAdmApplnId()));
						subjectGroup.setId(Integer.parseInt(subjectGroupDetailsForm.getSubjectGroupId()));
							/*if (!isDuplicateEntry(admAppin, subjectGroup)) {*/
								StudentCertificateCourse appsubjectGroup = new StudentCertificateCourse();
							appsubjectGroup.setAdmAppln(admAppin);
							appsubjectGroup.setCertificateCourse(subjectGroup);
							appsubjectGroup.setCreatedBy(subjectGroupDetailsForm.getUserId());
							appsubjectGroup.setCreatedDate(new Date());
							appsubjectGroup.setModifiedBy(subjectGroupDetailsForm.getUserId());
							appsubjectGroup.setLastModifiedDate(new Date());
							appsubjectGroup.setIsOnline(true);
							session.save(appsubjectGroup);
							
							if (++count % 20 == 0) {
								session.flush();
								session.clear();
							}
							isAdded = true;
							/*}*/
					}
				}
				transaction.commit();
				
			}
			/*List<SubjectGroupDetailsTo> tos =new ArrayList<SubjectGroupDetailsTo>();
			Iterator<SubjectGroupDetailsTo> iterator= list.iterator();
			while (iterator.hasNext()) {
				SubjectGroupDetailsTo subjectGroupDetailsTo = (SubjectGroupDetailsTo) iterator .next();
				if(subjectGroupDetailsTo.getChecked()!=null && subjectGroupDetailsTo.getChecked().equalsIgnoreCase("on")){
					subjectGroupDetailsTo.setChecked(null);
				}
				tos.add(subjectGroupDetailsTo);
			}
			subjectGroupDetailsForm.setSubjectList(tos);*/
		} catch (Exception exception) {
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
			}
		}
		log
				.info("end of addSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isAdded;
	}

	/**
	 * @param admAppIn
	 * @param subjectgroup
	 * @return
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	public boolean isDuplicateEntry(AdmAppln admAppIn,CertificateCourse subjectgroup) throws ApplicationException{
		Session session=null;
		boolean isDuplicateEntry = false;
		List<ApplicantSubjectGroup> appsubjectGroup;
		try{
			session=HibernateUtil.getSession();
			String str="from StudentCertificateCourse app where app.admAppln.id =" +admAppIn.getId()+ " and app.CertificateCourse.id =" +subjectgroup.getId();
			Query query=session.createQuery(str);
			appsubjectGroup=(List<ApplicantSubjectGroup>) query.list();
			if(appsubjectGroup!=null && !appsubjectGroup.isEmpty()){
				isDuplicateEntry = true;
			}
		} finally {
			if (session != null) {
				session.flush();
			}
		}log.info("end of isDuplicateEntry method in SubjectGroupDetailsTransactionImpl class.");
		return isDuplicateEntry;
		
	}
    
	/**
	 * @param subjectGroupDetailsTo
	 * @param subjectGroupDetailsForm
	 * @param subList 
	 * @return
	 * @throws ApplicationException 
	 */



	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getSubjectGroupNames(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	@SuppressWarnings("unchecked")
	public List<CertificateCourse> getSubjectGroupNames(SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception{
		log.info("call of getSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		Session session = null;
		try {
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select sbg.certificateCourse from Student s join  s.admAppln.studentCertificateCourse sbg where s.classSchemewise.id=" +subjectGroupDetailsForm.getClassSchemewiseId()+ " and s.admAppln.isCancelled=0" + " group by sbg.certificateCourse.id");
			 List<CertificateCourse> list = query.list();
			 session.flush();
			 log.info("end of getSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
			 return list;
		 } catch (Exception exception) {
			 if( session != null){
				 session.flush();
			 }
			 throw new ApplicationException(exception);
		 }
	 }
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getEditSubjectGroup(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer,Integer> getEditSubjectGroup(
			SubjectGroupDetailsForm subjectGroupDetailsForm) throws Exception {
		log.info("call of getEditSubjectGroup method in SubjectGroupDetailsTransactionImpl class.");
		Map<Integer,Integer> subMap=new HashMap<Integer, Integer>();
		Session session = null;
		try{
			session = HibernateUtil.getSession();
			String str="select s.id,addCourse.id from Student s join s.admAppln.studentCertificateCourse addCourse where addCourse.certificateCourse.id=" +subjectGroupDetailsForm.getSubjectGroupId()+ " and s.classSchemewise.id=" +subjectGroupDetailsForm.getClassSchemewiseId()+ " and addCourse.isOnline=1 group by s.id";
			Query query=session.createQuery(str);
			List<Object[]> list=query.list();
			Iterator<Object[]> it=list.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				subMap.put(Integer.parseInt(objects[0].toString()),Integer.parseInt(objects[1].toString()));
			}
			session.flush();
			 log.info("end of getEditSubjectGroup method in SubjectGroupDetailsTransactionImpl class.");
			 return subMap;
			 
		}catch (Exception exception) {
			
				 if( session != null){
					 session.flush();
				 }
				 throw new ApplicationException(exception);
			}
		
		}


	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#updateSubjectGroups(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	public boolean updateSubjectGroups(
			SubjectGroupDetailsForm subjectGroupDetailsForm)throws Exception{
		log.info("call of updateSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		boolean isUpdate = false;
		Session session = null;
		Transaction transaction = null;
		try {
	
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			List<StudentCertificateCourseTO> list = subjectGroupDetailsForm
			.getAddOnCourseList();
			if (list != null && !list.isEmpty()) {
				Iterator<StudentCertificateCourseTO> itr = list.iterator();
				int count = 0;
				while (itr.hasNext()) {
					StudentCertificateCourseTO subjectGroupDetailsTo = (StudentCertificateCourseTO) itr.next();
					 
					if (subjectGroupDetailsTo.getChecked()!=null && subjectGroupDetailsTo.getChecked().equalsIgnoreCase("on")) {
						// setting student specialization to Exam_student_biodata table
						CertificateCourse subjectGroup = new CertificateCourse();
						AdmAppln admAppin = new AdmAppln();
						admAppin.setId(Integer.parseInt(subjectGroupDetailsTo.getAdmApplnId()));
						subjectGroup.setId(Integer.parseInt(subjectGroupDetailsForm.getSubjectGroupId()));
								StudentCertificateCourse appsubjectGroup = new StudentCertificateCourse();
								appsubjectGroup.setAdmAppln(admAppin);
								appsubjectGroup.setId(subjectGroupDetailsTo.getAddOnSelectedCourseId());
								appsubjectGroup.setCertificateCourse(subjectGroup);
								appsubjectGroup.setIsOnline(true);
								appsubjectGroup.setCreatedBy(subjectGroupDetailsForm.getUserId());
								appsubjectGroup.setCreatedDate(new Date());
								appsubjectGroup.setModifiedBy(subjectGroupDetailsForm.getUserId());
								appsubjectGroup.setLastModifiedDate(new Date());
								session.saveOrUpdate(appsubjectGroup);
					
								if (++count % 20 == 0) {
									session.flush();
									session.clear();
								}
								isUpdate = true;
					} else if (subjectGroupDetailsTo.getTempChecked() != null && subjectGroupDetailsTo.getTempChecked().equalsIgnoreCase("on") && subjectGroupDetailsTo.getChecked() == null) {

						StudentCertificateCourse app = new StudentCertificateCourse();
						app.setId(subjectGroupDetailsTo.getAddOnSelectedCourseId());
						session.delete(app);
						if (++count % 20 == 0) {
							session.flush();
							session.clear();
						}
						isUpdate = true;
					}
				}
				transaction.commit();

			}
			List<StudentCertificateCourseTO> tos =new ArrayList<StudentCertificateCourseTO>();
			Iterator<StudentCertificateCourseTO> iterator= list.iterator();
			while (iterator.hasNext()) {
				StudentCertificateCourseTO subjectGroupDetailsTo = (StudentCertificateCourseTO) iterator .next();
				if(subjectGroupDetailsTo.getChecked()!=null && subjectGroupDetailsTo.getChecked().equalsIgnoreCase("on")){
					subjectGroupDetailsTo.setChecked(null);
				}
				tos.add(subjectGroupDetailsTo);
			}
			subjectGroupDetailsForm.setAddOnCourseList(tos);
		} catch (Exception exception) {
			
			if (transaction != null) {
				transaction.rollback();
			}

		} finally {
			if (session != null) {
				session.flush();
			}
		}
		log.info("end of updateSubjectGroups method in SubjectGroupDetailsTransactionImpl class.");
		return isUpdate;
	}


	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getsubjectGroupsId(com.kp.cms.forms.attendance.SubjectGroupDetailsForm)
	 */
	@Override
	public Map<Integer,String> getsubjectGroupsId(
			SubjectGroupDetailsForm subjectGroupDetailsForm) {
		log.info("call of getsubjectGroupsId method in SubjectGroupDetailsTransactionImpl class.");
		Map<Integer,String> subjectGroup=new HashMap<Integer, String>();
		Session session=null;
		Transaction transaction=null;
		try{
			session=HibernateUtil.getSession();
			String str="select c.id, c.certificateCourseName from CertificateCourse c where c.year=" +subjectGroupDetailsForm.getYear();
			Query query=session.createQuery(str);
			List<Object[]> list=query.list();
			Iterator<Object[]> it=list.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				subjectGroup.put(Integer.parseInt(objects[0].toString()),objects[1].toString());
			}
			session.flush();
			 log.info("end of getsubjectGroupsId method in SubjectGroupDetailsTransactionImpl class.");

			
		}catch (Exception exception) {
			
			 if( session != null){
				 session.flush();
			 }
			
	}
		 return subjectGroup; 
		
	}
	
	/**
	 * @param subjectGroupDetailsTo
	 * @param subjectGroupDetailsForm
	 * @param subList
	 * @return
	 */
	private boolean isValidateSecondLanguage( SubjectGroupDetailsTo subjectGroupDetailsTo, SubjectGroupDetailsForm subjectGroupDetailsForm,
			List<String> subList) throws Exception{
		Session session = null;
		Transaction tx = null;
		String secondLanguage = "";
		boolean isvalid = false;
		try{
			session = HibernateUtil.getSession();
			if(subjectGroupDetailsTo.getSecondlanguage()!=null && !subjectGroupDetailsTo.getSecondlanguage().isEmpty()){
				String str= "from SubjectGroup subGrp where subGrp.id=" +subjectGroupDetailsForm.getSubjectGroupId();
				Query query = session.createQuery(str);
				SubjectGroup subjectGroup = (SubjectGroup) query.uniqueResult();
				if(subjectGroup!=null && !subjectGroup.toString().isEmpty()){
					if(!subjectGroup.getIsCommonSubGrp()){
						if(subjectGroup.getSecondLanguageId()!=null && subjectGroup.getSecondLanguageId()!=0){
							if(subjectGroup.getExamSecondLanguageMasterBO()!=null && subjectGroup.getExamSecondLanguageMasterBO().getName()!=null){
								secondLanguage = subjectGroup.getExamSecondLanguageMasterBO().getName();
								if(secondLanguage!=null && !secondLanguage.isEmpty() && !secondLanguage.equalsIgnoreCase(subjectGroupDetailsTo.getSecondlanguage())){
									subList.add(subjectGroupDetailsTo.getRegisterNo());
									isvalid = true;
								}
								subjectGroupDetailsForm.setList(subList);
							}
						}
					}
				}
			}
		}catch (Exception exception) {
			
			if (tx != null) {
				tx.rollback();
			}
		}
		finally {
			if (session != null) {
				session.flush();
			}
		}
		return isvalid;
	}


	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getSpecializationData()
	 */
	@Override
	public List<ExamSpecializationBO> getSpecializationData(int classSchemeWiseId) throws Exception {
		Session session = null;
		List<ExamSpecializationBO>  bos = null;
		try{
			session = HibernateUtil.getSession();
			String str = "from ExamSpecializationBO exam where exam.courseUtilBO.courseID in (select cls.classes.course.id from ClassSchemewise cls where cls.id = "+classSchemeWiseId+") and exam.isActive=1";
			Query query =  session.createQuery(str);
			bos = query.list();
		}catch(Exception e){
			log.error("Error while getting ExamSpecializationBO.." +e);
			throw new ApplicationException();
		}
		finally{
			if(session!=null){
				session.flush();
			}
		}
		return bos;
	}


	


	@Override
	public Map<Integer, String> getstudentWiseSpecialization(List<Student> studentList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param subjectGroupDetailsTo
	 * @param subjectGroupDetailsForm
	 * @param specializationList 
	 * @return
	 * @throws Exception
	 */
	

	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.attandance.ISubjectGroupDetailsTransaction#getstudentWiseSpecialization(java.util.List)
	 */
	/*@Override
	public Map<Integer, String> getstudentWiseSpecialization(
			List<Student> studentList) throws Exception {
		Session session =null;
		Map<Integer, String> specializationMap = new HashMap<Integer, String>();
		try{
			session = HibernateUtil.getSession();
			Map<Integer,String> specializationBoMap = getSpecialization();
			Iterator<Student> iterator = studentList.iterator();
			while (iterator.hasNext()) {
				Student student = (Student) iterator.next();
				if(student!=null && !student.toString().isEmpty()){
					String str = "from ExamStudentBioDataBO specialization where specialization.studentId ="+student.getId();
					Query query = session.createQuery(str);
					List<ExamStudentBioDataBO> bioDataBOLIst = query.list();
					if(bioDataBOLIst!=null && !bioDataBOLIst.isEmpty()){
						Iterator<ExamStudentBioDataBO> iterator2 = bioDataBOLIst.iterator();
						while (iterator2.hasNext()) {
							ExamStudentBioDataBO examStudentBioDataBO = (ExamStudentBioDataBO) iterator2 .next();
							
							if(specializationMap.containsKey(examStudentBioDataBO.getStudentId())){
								if(specializationBoMap.get(examStudentBioDataBO.getSpecializationId())!=null && !specializationBoMap.get(examStudentBioDataBO.getSpecializationId()).isEmpty()){
								String specializationName = specializationMap.get(examStudentBioDataBO.getStudentId());
								specializationName = specializationName + "," + specializationBoMap.get(examStudentBioDataBO.getSpecializationId());
								specializationMap.put(examStudentBioDataBO.getStudentId(), specializationName);
								}
								}else{
								if(specializationBoMap.get(examStudentBioDataBO.getSpecializationId())!=null && !specializationBoMap.get(examStudentBioDataBO.getSpecializationId()).isEmpty()){
									specializationMap.put(examStudentBioDataBO.getStudentId(), specializationBoMap.get(examStudentBioDataBO.getSpecializationId()));
								}
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			log.error("Error while getting ExamStudentBioDataBO.." +e);
			throw new ApplicationException();
		}
		return specializationMap;
	}
*/

	/**
	 * @return
	 * @throws Exception
	 */

}
