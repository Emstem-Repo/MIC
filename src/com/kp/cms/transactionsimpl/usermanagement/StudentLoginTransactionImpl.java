package com.kp.cms.transactionsimpl.usermanagement;



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
import org.hibernate.exception.ConstraintViolationException;

import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.bo.admin.AttendanceCondonation;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamRegular;
import com.kp.cms.bo.admin.CandidatePGIDetailsExamSupply;
import com.kp.cms.bo.admin.CandidatePGIDetailsForStuSemesterFees;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.CourseToDepartment;
import com.kp.cms.bo.admin.EvaStudentFeedbackOpenConnection;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackSession;
import com.kp.cms.bo.admin.FeePayment;
import com.kp.cms.bo.admin.PersonalData;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.CandidatePGIForSpecialFees;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.bo.exam.ExamStudentBioDataBO;
import com.kp.cms.bo.exam.ExamSupplementaryImprovementApplicationBO;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.bo.sap.UploadSAPMarksBo;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.bo.studentExtentionActivity.StudentInstructions;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.to.admin.EvaStudentFeedbackOpenConnectionTo;
import com.kp.cms.transactions.usermanagement.IStudentLoginTransaction;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.HibernateUtil;
public class StudentLoginTransactionImpl implements IStudentLoginTransaction{
	private static final Log log = LogFactory.getLog(StudentLoginTransactionImpl.class);
	private static volatile StudentLoginTransactionImpl studentLoginTransactionImpl = null;
	public static StudentLoginTransactionImpl getInstance(){
		if(studentLoginTransactionImpl == null){
			studentLoginTransactionImpl = new StudentLoginTransactionImpl();
			return studentLoginTransactionImpl;
		}
		return studentLoginTransactionImpl;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#saveMobileNo(com.kp.cms.bo.admin.PersonalData)
	 */
	@Override
	public boolean saveMobileNo(PersonalData personalData) throws Exception {
		Session session=null;
		Transaction tx =null;
		boolean isAdded=false;
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			tx.begin();
			session.saveOrUpdate(personalData);
			tx.commit();
			session.flush();
			session.close();
			isAdded = true;
		}catch (ConstraintViolationException e) {
			tx.rollback();
			log.error("Error during saving Interview Comments data..." + e);
			throw new BusinessException(e);
		} catch (Exception e) {
			tx.rollback();
			log.error("Error during saving Interview Comments data..." + e);
			throw new ApplicationException(e);
		}
		return isAdded;
	}
	
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#getStudentPersonalData(int)
	 */
	@Override
	public PersonalData getStudentPersonalData(int personalId) throws Exception {
		Session session=null;
		PersonalData data=null;
		try{
			session= HibernateUtil.getSession();
			String str="from PersonalData p where p.id =" +personalId;
			Query query = session.createQuery(str);
			data= (PersonalData) query.uniqueResult();
			session.flush();
		}catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}	
			throw  new ApplicationException(e);
		}
		return data;
	}
	/* (non-Javadoc)
	 * Querying the database to find whether student has any fee payment through smart card mode in the last one month
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#getStudentPaymentMode(int)
	 */
	@Override
	public List<FeePayment> getStudentPaymentMode(int id) throws Exception {
		Session session=null;
		List<FeePayment> feeList=null;
		try{
			session= HibernateUtil.getSession();
			String currentDate=CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(new Date()), "dd-MMM-yyyy","yyyy-MM-dd");
			String previousMonthDate=CommonUtil.ConvertStringToDateFormat(CommonUtil.getPreviousMonthDate(), "dd-MM-yyyy","yyyy-MM-dd");
			String str="from FeePayment f where f.student.id=" +id+
					" and f.feePaymentMode.id=" +CMSConstants.smartCardPaymentMode+
					" and f.isCancelChallan=0 and f.challenPrintedDate between '" +previousMonthDate+
					"' and '"+currentDate+"'" ;
			Query query = session.createQuery(str);
			 feeList=  query.list();
			
			session.flush();
		}catch (Exception e) {
			if (session != null){
				session.flush();
				//session.close();
			}	
			throw  new ApplicationException(e);
		}
		return feeList;
	}
	/* (non-Javadoc)
	 * getting the FeePayment BO to print challan
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#getFeePaymentDetailsForEdit(int, int)
	 */
	@Override
	public FeePayment getFeePaymentDetailsForEdit(int parseInt,
			int financialYear) throws Exception {
		Session session = null;
		FeePayment feePayment = null;
		try {
			 //SessionFactory sessionFactory = InitSessionFactory.getInstance();
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select feePayment" +
			 		" from FeePayment feePayment" +
			 		" join feePayment.feePaymentDetails paymentDetails" +
			 		" where feePayment.billNo = :billNo" +
			 		" and paymentDetails.feeFinancialYear.id = :FinYearId and feePayment.isCancelChallan = 0" +
			 		" group by feePayment.id");
			query.setInteger("billNo", parseInt); 
			query.setInteger("FinYearId", financialYear);
			
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
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#isFacultyFeedbackAvailable(int)
	 */
	@Override
	public EvaStudentFeedbackOpenConnection isFacultyFeedbackAvailable(int id,Map<Integer,String> specializationIds) throws Exception {
		Session session =null;
		Object object = null;
		EvaStudentFeedbackOpenConnection connection = null;
		try{
			
			session = HibernateUtil.getSession();
			Query query  = session.createQuery("from EvaStudentFeedbackOpenConnection conn where conn.isActive=1 and conn.classesId.id="+id+" and '"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between conn.startDate and conn.endDate");
			//connection = (EvaStudentFeedbackOpenConnection) query.uniqueResult();
			//newly modified
			List<EvaStudentFeedbackOpenConnection> list = query.list();
			if(list!=null && !list.isEmpty()){
				Iterator<EvaStudentFeedbackOpenConnection> iterator = list.iterator();
				while (iterator.hasNext()) {
					EvaStudentFeedbackOpenConnection connectionBo = (EvaStudentFeedbackOpenConnection) iterator .next();
					if(connectionBo.getExamSpecializationBO()!=null && !connectionBo.getExamSpecializationBO().toString().isEmpty()){
						if(specializationIds.containsKey(connectionBo.getExamSpecializationBO().getId())){
							connection = new EvaStudentFeedbackOpenConnection();
							EvaluationStudentFeedbackSession feedbackSession = new EvaluationStudentFeedbackSession();
							Classes classes = new Classes();
							ExamSpecializationBO specializationBo = new ExamSpecializationBO();
							feedbackSession.setId(connectionBo.getFeedbackSession().getId());
							classes.setId(connectionBo.getClassesId().getId());
							specializationBo.setId(connectionBo.getExamSpecializationBO().getId());
							connection.setFeedbackSession(feedbackSession);
							connection.setClassesId(classes);
							connection.setExamSpecializationBO(specializationBo);
						}
					}else{
						connection = new EvaStudentFeedbackOpenConnection();
						EvaluationStudentFeedbackSession feedbackSession = new EvaluationStudentFeedbackSession();
						Classes classes = new Classes();
						feedbackSession.setId(connectionBo.getFeedbackSession().getId());
						classes.setId(connectionBo.getClassesId().getId());
						connection.setFeedbackSession(feedbackSession);
						connection.setClassesId(classes);
						
					}
				}
			}
			//
		}catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return connection;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#getSpecializationIds(int)
	 */
	@Override
	public Map<Integer,String> getSpecializationIds(int studentId) throws Exception {
		Session session = null;
		Map<Integer,String> map = new HashMap<Integer,String>();
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from ExamStudentBioDataBO examStudentBioData where examStudentBioData.studentId="+studentId);
			List<ExamStudentBioDataBO> listBos = query.list();
			if(listBos!=null && !listBos.isEmpty()){
				Iterator<ExamStudentBioDataBO> iterator = listBos.iterator();
				while (iterator.hasNext()) {
					ExamStudentBioDataBO examStudentBioDataBO = (ExamStudentBioDataBO) iterator .next();
					if(examStudentBioDataBO.getSpecializationId()!=0){
						map.put(examStudentBioDataBO.getSpecializationId(), examStudentBioDataBO.getExamSpecializationBO().getName());
					}
				}
			}
		}catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return map;
	}
	/* (non-Javadoc)
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#checkHonoursCourse(int)
	 */
	@Override
	public boolean checkHonoursCourse(int studentId,int courseId) throws Exception {
		Session session = null;
		boolean displayLink = false;
		try {
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from HonoursCourse h where h.isActive=1 and h.eligibleCourse.id="+courseId);
			if(query.list() != null && !query.list().isEmpty()){
				displayLink=true;
			}
			return displayLink;
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
	 * @see com.kp.cms.transactions.usermanagement.IStudentLoginTransaction#checkMandatoryCertificateCourse(com.kp.cms.bo.admin.AdmAppln)
	 */
	@Override
	public boolean checkMandatoryCertificateCourse(AdmAppln admAppln) throws Exception {
		boolean isCheckMandatory = false;
		Session session = null;
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from AssignCertificateCourse certificateCourse where certificateCourse.isActive =1 " +
					      " and certificateCourse.course.id="+admAppln.getCourseBySelectedCourseId().getId()+
					      " and certificateCourse.academicYear>="+admAppln.getAppliedYear());
			if(query.list()!=null && !query.list().isEmpty()){
				isCheckMandatory = true;
			}
		}catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
		return isCheckMandatory;
	}
	public boolean checkingStudentIsAppliedForSAPExam(int studentId) throws Exception {
		Session session = null;
		boolean displayLink = false;
		try {
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("from UploadSAPMarksBo bo where bo.isActive=1 " +
			 										" and bo.studentId.id="+studentId);
			if(query.list() != null && !query.list().isEmpty()){
				displayLink=true;
			}
			return displayLink;
		 } catch (Exception e) {
			 throw e;
		 } finally {
			if (session != null) {
				session.flush();
				//session.close();
			}
		}
	}
	
	public UploadSAPMarksBo getSAPExamResults(String studentId) throws Exception {
		Session session = null;
		try {
			 session = HibernateUtil.getSession();
			 Query query = session.createQuery("select bo from UploadSAPMarksBo bo"+
													" where bo.isActive=1  and bo.studentId.id="+studentId+
													" order by date desc ");
			 
			 query.setMaxResults(1);
			 UploadSAPMarksBo bo=(UploadSAPMarksBo)query.uniqueResult();
			
			return bo;
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
	public List<Integer> isAttendanceCondened(String studentId) throws Exception {
		Session session = null;
		try {
			 session = HibernateUtil.getSession();
			 List<Integer> condonationClassIds = new ArrayList<Integer>();
			 Query query = session.createQuery("select ac.classes.id from AttendanceCondonation ac"+
													" where ac.isActive=1  and ac.student.id="+studentId);
													
			 condonationClassIds =(List<Integer>)query.list();
			 return condonationClassIds;
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
	public List<StudentInstructions> getInstructions() throws Exception {
		Session session = null;
		List<StudentInstructions> feedbackInstructions;
		try{
			session = HibernateUtil.getSession();
			feedbackInstructions = session.createQuery("from StudentInstructions instructions").list();
		}catch (Exception e) {
			throw new ApplicationException(e);
			}
		finally {
			if (session != null) {
				session.flush();
			}
		}
		return feedbackInstructions;
	}
	@Override
	public List<StudentGroup> getStudentGroupDetails(LoginForm loginForm) throws Exception {
		Session session = null;
		try{
			session = HibernateUtil .getSession();
			Query query =session.createQuery("select sg from StudentExtentionFeedback st join st.groupId sg where st.classId.id=:cid and sg.isActive=1" +
					" and ('"+CommonUtil.ConvertStringToSQLDate(CommonUtil.getTodayDate())+"' between st.startDate and st.endDate)")				
											.setInteger("cid", Integer.parseInt(loginForm.getClassId()));											
			
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
	public List<StudentExtention> getStudentExtentionDetails(LoginForm form) throws Exception {
		Session session = null;
		try{
			session = HibernateUtil .getSession();
			Query query =session.createQuery("select ex from StudentExtention ex join ex.studentGroup sg where sg.id=:grpId")
													.setInteger("grpId", Integer.parseInt(form.getStudentGrpId()));
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
	public String generateCandidateRefNo(CandidatePGIDetailsForStuSemesterFees bo) throws Exception {
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="MICSTUSEM"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-AdmissionFormTransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-AdmissionFormTransactionImpl");
		return candidateRefNo;
	}
	@Override
	public boolean updateReceivedStatus(CandidatePGIDetailsForStuSemesterFees bo, LoginForm loginForm)throws Exception {
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		loginForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from CandidatePGIDetailsForStuSemesterFees c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+loginForm.getStudentSemesterFeesAmount()+" and c.txnStatus='Pending'";
				CandidatePGIDetailsForStuSemesterFees candidatePgiBo=(CandidatePGIDetailsForStuSemesterFees)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
				candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
				//candidatePgiBo.setBankId(bo.getBankId());
				candidatePgiBo.setBankRefNo(bo.getBankRefNo());
				candidatePgiBo.setTxnDate(bo.getTxnDate());
				candidatePgiBo.setTxnStatus(bo.getTxnStatus());
				
				//raghu
				candidatePgiBo.setMode(bo.getMode());
				candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
				candidatePgiBo.setMihpayId(bo.getMihpayId());
				candidatePgiBo.setPgType(bo.getPgType());
				candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());
				if(loginForm.getStudentId()!= 0){
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					Student student = new Student();
					student.setId(loginForm.getStudentId());
					candidatePgiBo.setStudent(student);
				}
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					loginForm.setIsTnxStatusSuccess(true);
					//admForm.setPaymentSuccess(true);
				}
				
				//loginForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				//loginForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-AdmissionFormTransactionImpl..."+e);
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit generateCandidateRefNo-AdmissionFormTransactionImpl");
		return isUpdated;
	
	
	}
	@Override
	public boolean paymentDone(LoginForm loginForm) throws Exception {
		Session session = null;
		boolean paymentDone = false;
		CandidatePGIDetailsForStuSemesterFees bo = new CandidatePGIDetailsForStuSemesterFees();
		try{
			session = HibernateUtil.getSession();
			String str = "from CandidatePGIDetailsForStuSemesterFees b where b.student.id = :studentId and b.txnStatus = 'success' and b.termNo="+loginForm.getTermNo();
			Query query = session.createQuery(str).setInteger("studentId", loginForm.getStudentId());
			bo = (CandidatePGIDetailsForStuSemesterFees)query.uniqueResult();
			if(bo != null){
				paymentDone  = true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return paymentDone;
	}
	@Override
	public Student getStudentObj(String studentId) throws Exception {
		Session session = null;
		Student student = null;
		try{
			session = HibernateUtil.getSession();
			student = (Student) session.get(Student.class, Integer.parseInt(studentId));
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return student;
	}
	@Override
	public List<CandidatePGIDetailsExamRegular> getRegList(String studentId)
			throws Exception {
		Session session = null;
		List<CandidatePGIDetailsExamRegular> list = new ArrayList<CandidatePGIDetailsExamRegular>();
		try{
			session = HibernateUtil.getSession();
			String s = "from CandidatePGIDetailsExamRegular cd where cd.student.id = :studentId";
			Query query = session.createQuery(s).setInteger("studentId", Integer.parseInt(studentId));
			list = query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public List<CandidatePGIDetailsExamSupply> getSuppList(String studentId)
			throws Exception {
		Session session = null;
		List<CandidatePGIDetailsExamSupply> list1 = new ArrayList<CandidatePGIDetailsExamSupply>();
		try{
			session = HibernateUtil.getSession();
			String s = "from CandidatePGIDetailsExamSupply cd where cd.student.id = :studentId";
			Query query = session.createQuery(s).setInteger("studentId", Integer.parseInt(studentId));
			list1 = query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list1;
	}
	@Override
	public List<RevaluationApplicationPGIDetails> getRevList(String studentId)
			throws Exception {
		Session session = null;
		List<RevaluationApplicationPGIDetails> list2 = new ArrayList<RevaluationApplicationPGIDetails>();
		try{
			session = HibernateUtil.getSession();
			String s = "from RevaluationApplicationPGIDetails cd where cd.student.id = :studentId";
			Query query = session.createQuery(s).setInteger("studentId", Integer.parseInt(studentId));
			list2 = query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list2;
	}
	@Override
	public CourseToDepartment getDept(String courseId) throws Exception {
		Session session = null;
		CourseToDepartment courseToDepartment = null;
		try{
			session = HibernateUtil.getSession();
			String s  = "from CourseToDepartment c where c.course.id = :courseId";
			Query query = session.createQuery(s).setInteger("courseId", Integer.parseInt(courseId));
			courseToDepartment = (CourseToDepartment)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return courseToDepartment;
	}
	@Override
	public List<ExamRegularApplication> getRegPaymentList(String studentId)
			throws Exception {
		Session session = null;
		List<ExamRegularApplication> list = new ArrayList<ExamRegularApplication>();
		try{
			session = HibernateUtil.getSession();
			String s = "from ExamRegularApplication e where e.student.id = :studentId  and e.challanVerified = 1  and e.isTokenRegisterd = 1";
			Query query = session.createQuery(s).setInteger("studentId", Integer.parseInt(studentId));
			list = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return list;
	}
	@Override
	public List<ExamSupplementaryImprovementApplicationBO> getSupPaymentList(String studentId) throws Exception {
		Session session = null;
		List<ExamSupplementaryImprovementApplicationBO>list1 = new ArrayList<ExamSupplementaryImprovementApplicationBO>();
		try{
			session = HibernateUtil.getSession();
			String s = "from ExamSupplementaryImprovementApplicationBO e where e.studentUtilBO.id = :studentId  and e.challanVerified = 1";
			Query query = session.createQuery(s).setInteger("studentId", Integer.parseInt(studentId));
			list1 = query.list();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		
		return list1;
	}
	@Override
	public List<PublishSpecialFees> getPublishClassList(String classId)throws Exception {
		Session session = null;
		List<PublishSpecialFees> publishSpecialFees = new ArrayList<PublishSpecialFees>();
		try{
			session = HibernateUtil.getSession();
			String s = "from PublishSpecialFees p where p.classes.id = :classId  and p.isActive = 1";
			Query query = session.createQuery(s).setInteger("classId", Integer.parseInt(classId));
			publishSpecialFees = query.list();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return publishSpecialFees;
	}
	@Override
	public SpecialFeesBO getData(String classId) throws Exception {
		Session session = null;
		SpecialFeesBO bo = null;
		try{
			session = HibernateUtil.getSession();
			String s = "from SpecialFeesBO s where s.classes.id =:classId  and s.isActive = 1";
			Query query = session.createQuery(s).setInteger("classId", Integer.parseInt(classId));
			bo = (SpecialFeesBO)query.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return bo;
	}
	@Override
	public String generateCandidateRefNoForSpecial(CandidatePGIForSpecialFees bo)
			throws Exception {
		Session session=null;
		Transaction transaction=null;
		String candidateRefNo="";
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				session.save(bo);
			}
			transaction.commit();
//			session.flush();
			int savedId=bo.getId();
			if(savedId>0){
				transaction=session.beginTransaction();
				candidateRefNo="MICSPEFEE"+String.valueOf(savedId);
//				CandidatePGIDetails savedBo=(CandidatePGIDetails)session.get(CandidatePGIDetails.class, savedId);
				bo.setCandidateRefNo(candidateRefNo);
//				session.save(savedBo);
				session.update(bo);
				transaction.commit();
//				session.flush();
			}
			session.close();
		} catch (Exception e) {
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-AdmissionFormTransactionImpl..."+e);
			throw  new ApplicationException(e);
		}
		log.info("Exit generateCandidateRefNo-AdmissionFormTransactionImpl");
		return candidateRefNo;
	}
	@Override
	public boolean updateStatus(CandidatePGIForSpecialFees bo,LoginForm loginForm) throws Exception {
		Session session=null;
		Transaction transaction=null;
		boolean isUpdated=false;
		loginForm.setIsTnxStatusSuccess(false);
		try {
			session=HibernateUtil.getSession();
			transaction=session.beginTransaction();
			if(bo!=null){
				String query=" from CandidatePGIForSpecialFees c where c.candidateRefNo='"+bo.getCandidateRefNo()
				+"' and c.txnAmount="+loginForm.getSpecialFeesAmount()+" and c.txnStatus='Pending'";
				CandidatePGIForSpecialFees candidatePgiBo=(CandidatePGIForSpecialFees)session.createQuery(query).uniqueResult();
				if(candidatePgiBo!=null){
				candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
				//candidatePgiBo.setBankId(bo.getBankId());
				candidatePgiBo.setBankRefNo(bo.getBankRefNo());
				candidatePgiBo.setTxnDate(bo.getTxnDate());
				candidatePgiBo.setTxnStatus(bo.getTxnStatus());
				
				//raghu
				candidatePgiBo.setMode(bo.getMode());
				candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
				candidatePgiBo.setMihpayId(bo.getMihpayId());
				candidatePgiBo.setPgType(bo.getPgType());
				candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());
				loginForm.setStudentId(candidatePgiBo.getStudent().getId());
				if(loginForm.getStudentId()!= 0){
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					Student student = new Student();
					student.setId(loginForm.getStudentId());
					candidatePgiBo.setStudent(student);
				}
				}
				session.update(candidatePgiBo);
				if(bo.getTxnStatus()!=null && bo.getTxnStatus().equalsIgnoreCase("Success")){
					loginForm.setIsTnxStatusSuccess(true);
					//admForm.setPaymentSuccess(true);
				}
				
				//loginForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
				//loginForm.setTransactionRefNO(bo.getTxnRefNo());
				isUpdated=true;
				}
			}
			transaction.commit();
			//session.flush();
			//session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ( transaction != null){
				transaction.rollback();
			}
			if (session != null){
				//session.flush();
				session.close();
			}
			log.error("Error in generateCandidateRefNo-AdmissionFormTransactionImpl..."+e);
			System.out.println("Error during .................................updateReceivedStatus.........."+ e.getCause().toString());
			throw  new ApplicationException(e);
		}
		
        finally{
			
			//session.flush();
			session.close();
		}
		log.info("Exit generateCandidateRefNo-AdmissionFormTransactionImpl");
		return isUpdated;
	
	}
	@Override
	public boolean paymentDoneForSpecial(LoginForm loginForm) throws Exception {
		Session session = null;
		boolean paymentDone = false;
		CandidatePGIForSpecialFees bo = new CandidatePGIForSpecialFees();
		try{
			session = HibernateUtil.getSession();
			String str = "from CandidatePGIForSpecialFees b where b.student.id = :studentId and b.txnStatus = 'success'";
			Query query = session.createQuery(str).setInteger("studentId", loginForm.getStudentId());
			bo = (CandidatePGIForSpecialFees)query.uniqueResult();
			if(bo != null){
				paymentDone  = true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
		return paymentDone;
	}
}
