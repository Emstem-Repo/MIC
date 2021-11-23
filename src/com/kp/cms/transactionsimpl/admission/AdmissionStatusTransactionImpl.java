package com.kp.cms.transactionsimpl.admission;

import com.kp.cms.to.admission.CertificateCourseTO;
import org.hibernate.exception.ConstraintViolationException;
import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.utilities.InitSessionFactory;
import com.kp.cms.bo.admin.CertificateCourse;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import java.util.Date;
import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.bo.admission.StudentAllotmentPGIDetails;
import java.util.Iterator;
import java.util.ArrayList;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admin.InterviewCardHistory;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.bo.admission.ApplnAcknowledgement;
import com.kp.cms.utilities.CommonUtil;
import org.hibernate.Transaction;
import com.kp.cms.bo.admin.InterviewCard;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.bo.admin.AdmAppln;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;

public class AdmissionStatusTransactionImpl implements IAdmissionStatusTransaction
{
    private static final Log log;
    
    static {
        log = LogFactory.getLog((Class)AdmissionStatusTransactionImpl.class);
    }
    
    public AdmAppln getInterviewResult(final String applicationNo, final int year) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        AdmAppln admAppln = null;
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from AdmAppln adm where adm.applnNo=? and adm.appliedYear =  (select max(appliedYear) from AdmAppln sub where sub.applnNo = " + applicationNo + ")");
            query.setString(0, applicationNo);
            admAppln = (AdmAppln)query.uniqueResult();
            AdmissionStatusTransactionImpl.log.info((Object)"End of getInterviewResult of AdmissionStatusTransactionImpl");
            return admAppln;
        }
        catch (Exception e) {
            AdmissionStatusTransactionImpl.log.error((Object)("Exception ocured in getInterviewResult of AdmissionStatusTransactionImpl :" + e));
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public List<AdmAppln> getDetailsAdmAppln(final String applicationNo) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getDetailsAdmAppln of AdmissionStatusTransactionImpl");
        Session session = null;
        List<AdmAppln> admAppln;
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from AdmAppln adm where adm.applnNo= " + applicationNo + " and appliedYear = (select max(appliedYear) from AdmAppln sub where sub.applnNo = " + applicationNo + ")");
            admAppln = (List<AdmAppln>)query.list();
            if (!admAppln.isEmpty()) {
                return admAppln;
            }
        }
        catch (Exception e) {
            AdmissionStatusTransactionImpl.log.error((Object)("Exception ocured in getDetailsAdmAppln of AdmissionStatusTransactionImpl :" + e));
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
        if (session != null) {
            session.flush();
        }
        AdmissionStatusTransactionImpl.log.info((Object)"End of getDetailsAdmAppln of AdmissionStatusTransactionImpl");
        return admAppln;
    }
    
    public List<InterviewCard> getStudentsList(final int applicationNo) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"entered getStudentsList.of AdmissionStatusTransactionImpl");
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            transaction.begin();
            final Query query = session.createQuery("from InterviewCard i where i.admAppln.applnNo = :applicationNo");
            query.setInteger("applicationNo", applicationNo);
            final List<InterviewCard> interviewTypes = (List<InterviewCard>)query.list();
            transaction.commit();
            session.flush();
            AdmissionStatusTransactionImpl.log.info((Object)"End getStudentsList.of AdmissionStatusTransactionImpl");
            return interviewTypes;
        }
        catch (Exception e) {
            if (session != null) {
                transaction.rollback();
            }
            session.flush();
            AdmissionStatusTransactionImpl.log.error((Object)("Error while getting Student Details in AdmissionStatusTransactionImpl" + e));
            throw new ApplicationException(e);
        }
    }
    
    public List<InterviewCard> getStudentAdmitCardDetails(final int applicationNo, final int interviewTypeId) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"entered getStudentAdmitCardDetails of AdmissionStatusTransactionImpl");
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            List<InterviewCard> studentAdmitCardList = null;
            if (interviewTypeId != 0) {
                final Query query = session.createQuery("from InterviewCard interviewCard  where interviewCard.admAppln.applnNo = :applicationNo  and interviewCard.interview.interview.interviewProgramCourse.id = :interviewTypeId");
                query.setInteger("applicationNo", applicationNo);
                query.setInteger("interviewTypeId", interviewTypeId);
                studentAdmitCardList = (List<InterviewCard>)query.list();
            }
            else {
                final Query query = session.createQuery("from InterviewCard interviewCard  where interviewCard.admAppln.applnNo = :applicationNo");
                query.setInteger("applicationNo", applicationNo);
                studentAdmitCardList = (List<InterviewCard>)query.list();
            }
            session.flush();
            AdmissionStatusTransactionImpl.log.info((Object)"End getStudentAdmitCardDetails of AdmissionStatusTransactionImpl");
            return studentAdmitCardList;
        }
        catch (Exception e) {
            if (session != null) {
                session.flush();
            }
            AdmissionStatusTransactionImpl.log.error((Object)("Error while getting getStudentAdmitCardDetails in AdmissionStatusTransactionImpl" + e));
            throw new ApplicationException(e);
        }
    }
    
    public boolean getApplnAcknowledgement(final String applicationNo, final String dateOfBirth) throws Exception {
        Session session = null;
        boolean exists = false;
        try {
            session = HibernateUtil.getSession();
            final String quer = "from ApplnAcknowledgement apln where apln.applnNo=" + applicationNo + " and apln.dateOfBirth='" + CommonUtil.ConvertStringToSQLDate(dateOfBirth) + "'";
            final Query query = session.createQuery(quer);
            final ApplnAcknowledgement appln = (ApplnAcknowledgement)query.uniqueResult();
            if (appln != null) {
                exists = true;
            }
        }
        catch (Exception exp) {
            throw new BusinessException(exp);
        }
        return exists;
    }
    
    public CandidatePGIDetails getCandidateDetails(final AdmissionStatusForm admissionStatusForm) throws Exception {
        Session session = null;
        CandidatePGIDetails details;
        try {
            session = HibernateUtil.getSession();
            final String query = "from CandidatePGIDetails c  where c.txnRefNo is not null and c.admAppln.applnNo=" + admissionStatusForm.getApplicationNo();
            details = (CandidatePGIDetails)session.createQuery(query).uniqueResult();
        }
        catch (Exception e) {
            AdmissionStatusTransactionImpl.log.error((Object)("Error while getting applicant details..." + e));
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
        if (session != null) {
            session.flush();
        }
        return details;
    }
    
    public List<InterviewCardHistory> getStudentAdmitCardHistoryDetails(final int applicationNo) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            List<InterviewCardHistory> studentAdmitCardList = null;
            final Query query = session.createQuery("from InterviewCardHistory interviewCard  where interviewCard.admAppln.applnNo = :applicationNo order by interviewCard.id desc");
            query.setInteger("applicationNo", applicationNo);
            studentAdmitCardList = (List<InterviewCardHistory>)query.list();
            session.flush();
            return studentAdmitCardList;
        }
        catch (Exception e) {
            if (session != null) {
                session.flush();
            }
            throw new ApplicationException(e);
        }
    }
    
    public List<StudentCourseChanceMemo> getBolist(final String applicationNo, final String dob) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        List<StudentCourseChanceMemo> courseAllotment = new ArrayList<StudentCourseChanceMemo>();
        final String d = CommonUtil.formatDate(dob);
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=4 and s.admAppln.applnNo = " + applicationNo + " and s.admAppln.personalData.dateOfBirth='" + d + "'" + ")");
            courseAllotment = (List<StudentCourseChanceMemo>)query.list();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public boolean isUpdated(final List<StudentCourseChanceMemo> allotments) throws Exception {
        Session session = null;
        Transaction tx = null;
        boolean isUpdate = false;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            tx.begin();
            for (final StudentCourseChanceMemo allotment : allotments) {
                session.update((Object)allotment);
            }
            tx.commit();
            isUpdate = true;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }
       /* if (session != null) {
            session.flush();
            session.close();
        }*/
        return isUpdate;
    }
    
    public StudentCourseChanceMemo getBoClass(final String admApplnId) throws Exception {
        Session session = null;
        StudentCourseChanceMemo courseAllotment = new StudentCourseChanceMemo();
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseChanceMemo s where s.isAccept=s and s.admAppln.id = " + admApplnId + ")");
            courseAllotment = (StudentCourseChanceMemo)query.uniqueResult();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public boolean isUpdateUpload(final StudentCourseChanceMemo allotment) throws Exception {
        Session session = null;
        Transaction tx = null;
        boolean isUpdate = false;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            tx.begin();
            session.update((Object)allotment);
            tx.commit();
            isUpdate = true;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        if (session != null) {
            session.flush();
            session.close();
        }
        return isUpdate;
    }
    
    public boolean updateReceivedStatus(final StudentAllotmentPGIDetails bo, final AdmissionStatusForm admForm) throws Exception {
        Session session = null;
        Transaction transaction = null;
        boolean isUpdated = false;
        admForm.setIsTnxStatusSuccess(Boolean.valueOf(false));
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            if (bo != null) {
                final String query = " from StudentAllotmentPGIDetails c where c.candidateRefNo='" + bo.getCandidateRefNo() + "' and c.txnAmount=" + admForm.getAmount() + " and c.txnStatus='Pending'";
                final StudentAllotmentPGIDetails candidatePgiBo = (StudentAllotmentPGIDetails)session.createQuery(query).uniqueResult();
                if (candidatePgiBo != null) {
                    candidatePgiBo.setTxnRefNo(bo.getTxnRefNo());
                    candidatePgiBo.setBankRefNo(bo.getBankRefNo());
                    candidatePgiBo.setTxnDate(bo.getTxnDate());
                    candidatePgiBo.setTxnStatus(bo.getTxnStatus());
                    candidatePgiBo.setMode(bo.getMode());
                    candidatePgiBo.setUnmappedStatus(bo.getUnmappedStatus());
                    candidatePgiBo.setMihpayId(bo.getMihpayId());
                    candidatePgiBo.setPgType(bo.getPgType());
                    candidatePgiBo.setAdditionalCharges(bo.getAdditionalCharges());
                    if (admForm.getAdmApplnId() != null && !admForm.getAdmApplnId().equalsIgnoreCase("") && bo.getTxnStatus() != null && bo.getTxnStatus().equalsIgnoreCase("Success")) {
                        final AdmAppln adm = new AdmAppln();
                        adm.setId(Integer.parseInt(admForm.getAdmApplnId()));
                        candidatePgiBo.setAdmAppln(adm);
                    }
                    session.update((Object)candidatePgiBo);
                    if (bo.getTxnStatus() != null && bo.getTxnStatus().equalsIgnoreCase("Success")) {
                        admForm.setIsTnxStatusSuccess(Boolean.valueOf(true));
                        admForm.setPaymentSuccess(Boolean.valueOf(true));
                    }
                    admForm.setCandidateRefNo(candidatePgiBo.getCandidateRefNo());
                    admForm.setTransactionRefNO(bo.getTxnRefNo());
                    isUpdated = true;
                }
            }
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (session != null) {
                session.close();
            }
            System.out.println("Error during .................................updateReceivedStatus.........." + e.getCause().toString());
            throw new ApplicationException(e);
        }
        finally {
            session.close();
        }
        session.close();
        return isUpdated;
    }
    
    public List<PublishForAllotment> getPublishAllotment() throws Exception {
        Session session = null;
        List<PublishForAllotment> allotments = new ArrayList<PublishForAllotment>();
        final Date date = new Date();
        final java.sql.Date dat = new java.sql.Date(date.getTime());
        try {
            session = HibernateUtil.getSession();
            final String sql = "from PublishForAllotment p where '" + dat + "'" + " between p.fromDate and p.endDate";
            final Query query = session.createQuery(sql);
            allotments = (List<PublishForAllotment>)query.list();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allotments;
    }
    
    public List<StudentCourseAllotment> getBolistForAllotment(final String applicationNo, final String dateOfBirth) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        List<StudentCourseAllotment> courseAllotment = new ArrayList<StudentCourseAllotment>();
        final String d = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseAllotment s where s.allotmentNo=1 and s.admAppln.applnNo = " + applicationNo + " and s.admAppln.personalData.dateOfBirth='" + d + "'");
            courseAllotment = (List<StudentCourseAllotment>)query.list();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public boolean isUpdatedForPg(final List<StudentCourseAllotment> courseAllotments) throws Exception {
        Session session = null;
        Transaction tx = null;
        boolean isUpdate = false;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            tx.begin();
            for (final StudentCourseAllotment allotment : courseAllotments) {
                session.update((Object)allotment);
            }
            tx.commit();
            isUpdate = true;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }
       /* if (session != null) {
            session.flush();
            session.close();
        }*/
        return isUpdate;
    }
    
    public int getProgramTypeId(final String applicationNo, final String dateOfBirth) throws Exception {
        Session session = null;
        Integer programType = 0;
        final String dateOf = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final String hql = " select a.course.program.programType.id  from AdmAppln a  where a.applnNo=" + Integer.parseInt(applicationNo) + " and a.personalData.dateOfBirth='" + dateOf + "'";
            final Query query = session.createQuery(hql);
            programType = (Integer)query.uniqueResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return programType;
    }
    
    public boolean isUpdateForBoth(final List<StudentCourseChanceMemo> allotments, final List<StudentCourseAllotment> courseAllotments, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final List<StudentCourseChanceMemo> totalChances = new ArrayList<StudentCourseChanceMemo>();
        final List<StudentCourseAllotment> totalAllotment = new ArrayList<StudentCourseAllotment>();
        boolean isupdate = false;
        try {
            final String type = admissionStatusForm.getChancOrAllotment();
            if (type.equalsIgnoreCase("Allotment")) {
                final StudentCourseAllotment all = courseAllotments.get(0);
                final StudentCourseChanceMemo chance = allotments.get(0);
                if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                    all.setIsAccept(Boolean.valueOf(true));
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    chance.setIsDecline(Boolean.valueOf(true));
                    chance.setModifiedBy(admissionStatusForm.getUserId());
                    chance.setLastModifiedDate(new Date());
                    totalAllotment.add(all);
                    totalChances.add(chance);
                    isupdate = this.isUpdateFo(totalChances, totalAllotment);
                }
                else {
                    all.setIsDecline(Boolean.valueOf(true));
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    totalAllotment.add(all);
                    isupdate = this.isUpdatedForPg(totalAllotment);
                }
            }
            else {
                final StudentCourseAllotment all = courseAllotments.get(0);
                final StudentCourseChanceMemo chance = allotments.get(0);
                if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                    chance.setIsAccept(Boolean.valueOf(true));
                    chance.setModifiedBy(admissionStatusForm.getUserId());
                    chance.setLastModifiedDate(new Date());
                    all.setIsDecline(Boolean.valueOf(true));
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    totalChances.add(chance);
                    totalAllotment.add(all);
                    isupdate = this.isUpdateFo(totalChances, totalAllotment);
                }
                else {
                    chance.setIsDecline(Boolean.valueOf(true));
                    chance.setModifiedBy(admissionStatusForm.getUserId());
                    chance.setLastModifiedDate(new Date());
                    totalChances.add(chance);
                    isupdate = this.isUpdated(totalChances);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isupdate;
    }
    
    private boolean isUpdateFo(final List<StudentCourseChanceMemo> totalChances, final List<StudentCourseAllotment> totalAllotment) throws Exception {
        Session session = null;
        Transaction tx = null;
        boolean isUpdate = false;
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            tx.begin();
            for (final StudentCourseAllotment allotment : totalAllotment) {
                session.update((Object)allotment);
            }
            for (final StudentCourseChanceMemo allotment2 : totalChances) {
                session.update((Object)allotment2);
            }
            tx.commit();
            isUpdate = true;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        if (session != null) {
            session.flush();
            session.close();
        }
        return isUpdate;
    }
    
    public List<StudentCourseChanceMemo> getBolistForPG(final String applicationNo, final String dateOfBirth) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        List<StudentCourseChanceMemo> courseAllotment = new ArrayList<StudentCourseChanceMemo>();
        final String d = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=2 and s.admAppln.applnNo = " + applicationNo + " and s.admAppln.personalData.dateOfBirth='" + d + "'");
            courseAllotment = (List<StudentCourseChanceMemo>)query.list();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public int getSelectedCourseId(final String applicationNo, final String dateOfBirth) throws Exception {
        Session session = null;
        Integer cousrseId = 0;
        final String dateOf = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final String hql = " select a.course.id  from AdmAppln a  where a.applnNo=" + Integer.parseInt(applicationNo) + " and a.personalData.dateOfBirth='" + dateOf + "'";
            final Query query = session.createQuery(hql);
            cousrseId = (Integer)query.uniqueResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cousrseId;
    }
    
    public List<StudentCourseChanceMemo> getBolistForUg(final String applicationNo, final String dateOfBirth) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        List<StudentCourseChanceMemo> courseAllotment = new ArrayList<StudentCourseChanceMemo>();
        final String d = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseChanceMemo s where s.chanceNo=4 and s.admAppln.applnNo = " + applicationNo + " and s.admAppln.personalData.dateOfBirth='" + d + "'");
            courseAllotment = (List<StudentCourseChanceMemo>)query.list();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public List<StudentCourseAllotment> getBolistForAllotmentUg(final String applicationNo, final String dateOfBirth) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        List<StudentCourseAllotment> courseAllotment = new ArrayList<StudentCourseAllotment>();
        final String d = CommonUtil.formatDate(dateOfBirth);
        try {
            session = HibernateUtil.getSession();
            final Query query = session.createQuery("from StudentCourseAllotment s where s.allotmentNo=2 and s.admAppln.applnNo = " + applicationNo + " and s.admAppln.personalData.dateOfBirth='" + d + "'");
            courseAllotment = (List<StudentCourseAllotment>)query.list();
            return courseAllotment;
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
    }
    
    public boolean isUpdateForBothForUg(final List<StudentCourseChanceMemo> allotments, final List<StudentCourseAllotment> courseAllotments, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final List<StudentCourseChanceMemo> totalChances = new ArrayList<StudentCourseChanceMemo>();
        final List<StudentCourseAllotment> totalAllotment = new ArrayList<StudentCourseAllotment>();
        boolean isupdate = false;
        try {
            final String type = admissionStatusForm.getChancOrAllotment();
            if (type.equalsIgnoreCase("Allotment")) {
                final StudentCourseAllotment all = courseAllotments.get(0);
                if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                    all.setIsAccept(Boolean.valueOf(true));
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    for (final StudentCourseChanceMemo memo : allotments) {
                        memo.setModifiedBy(admissionStatusForm.getUserId());
                        memo.setLastModifiedDate(new Date());
                        totalChances.add(memo);
                    }
                    totalAllotment.add(all);
                    isupdate = this.isUpdateFo(totalChances, totalAllotment);
                }
                else if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("decline")) {
                    all.setIsDecline(Boolean.valueOf(true));
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    for (final StudentCourseChanceMemo memo : allotments) {
                        memo.setModifiedBy(admissionStatusForm.getUserId());
                        memo.setLastModifiedDate(new Date());
                        totalChances.add(memo);
                    }
                    totalAllotment.add(all);
                    isupdate = this.isUpdatedForPg(totalAllotment);
                }
                else {
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    totalAllotment.add(all);
                    isupdate = this.isUpdatedForPg(totalAllotment);
                }
            }
            else {
                final StudentCourseAllotment all = courseAllotments.get(0);
                if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                    for (final StudentCourseChanceMemo memo : allotments) {
                        if (memo.getCourse().getId() == admissionStatusForm.getSelectedCourseId()) {
                            memo.setIsAccept(Boolean.valueOf(true));
                            memo.setModifiedBy(admissionStatusForm.getUserId());
                            memo.setLastModifiedDate(new Date());
                        }
                        else {
                            memo.setIsDecline(Boolean.valueOf(true));
                            memo.setModifiedBy(admissionStatusForm.getUserId());
                            memo.setLastModifiedDate(new Date());
                        }
                        totalChances.add(memo);
                    }
                    all.setModifiedBy(admissionStatusForm.getUserId());
                    all.setLastModifiedDate(new Date());
                    totalAllotment.add(all);
                    isupdate = this.isUpdateFo(totalChances, totalAllotment);
                }
                else {
                    for (final StudentCourseChanceMemo memo : allotments) {
                        if (memo.getCourse().getId() == admissionStatusForm.getSelectedCourseId()) {
                            memo.setIsDecline(Boolean.valueOf(true));
                            memo.setModifiedBy(admissionStatusForm.getUserId());
                            memo.setLastModifiedDate(new Date());
                        }
                        totalChances.add(memo);
                    }
                    isupdate = this.isUpdated(totalChances);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isupdate;
    }
    
    public List<CertificateCourse> getActiveCertificateCourses(final int year) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Start of getActiveCourses of CourseTransactionImpl");
        Session session = null;
        List<CertificateCourse> courseBoList;
        try {
            session = (Session)InitSessionFactory.getInstance().openSession();
            courseBoList = (List<CertificateCourse>)session.createQuery("from CertificateCourse c where c.isActive = 1").list();
        }
        catch (Exception e) {
            AdmissionStatusTransactionImpl.log.error((Object)"Error in getActiveCertificateCourses of Course Impl", (Throwable)e);
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
        if (session != null) {
            session.flush();
        }
        AdmissionStatusTransactionImpl.log.info((Object)"End of getActiveCertificateCourses of impl");
        return courseBoList;
    }
    
    public boolean saveStudentCertificateCourse(final List<StudentCertificateCourse> studCertCourse) throws Exception {
        AdmissionStatusTransactionImpl.log.debug((Object)"inside addCertificateCourse");
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            transaction.begin();
            for (final StudentCertificateCourse bo : studCertCourse) {
                session.save((Object)bo);
            }
            transaction.commit();
            if (session != null) {
                session.flush();
                session.close();
            }
            AdmissionStatusTransactionImpl.log.debug((Object)"leaving addCertificateCourse");
            return true;
        }
        catch (ConstraintViolationException e) {
            transaction.rollback();
            AdmissionStatusTransactionImpl.log.error((Object)"Error during saving addCertificateCourse...", (Throwable)e);
            throw new BusinessException((Exception)e);
        }
        catch (Exception e2) {
            transaction.rollback();
            AdmissionStatusTransactionImpl.log.error((Object)"Error during saving addCertificateCourse data...", (Throwable)e2);
            throw new ApplicationException(e2);
        }
    }
    
    public List<CertificateCourseTO> getCertificateCoursesprint(final int id) throws Exception {
        AdmissionStatusTransactionImpl.log.info((Object)"Inside of getInterviewResult of AdmissionStatusTransactionImpl");
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            List<StudentCertificateCourse> list = new ArrayList<StudentCertificateCourse>();
            final List<CertificateCourseTO> toList = new ArrayList<CertificateCourseTO>();
            final Query query = session.createQuery("from StudentCertificateCourse s where s.admAppln.id=" + id);
            list = (List<StudentCertificateCourse>)query.list();
            if (list != null) {
                for (final StudentCertificateCourse bo : list) {
                    final CertificateCourseTO to = new CertificateCourseTO();
                    to.setId(bo.getId());
                    to.setCourseName(bo.getCertificateCourse().getCertificateCourseName());
                    toList.add(to);
                }
                return toList;
            }
        }
        catch (Exception e) {
            throw new ApplicationException(e);
        }
        finally {
            if (session != null) {
                session.flush();
            }
        }
        if (session != null) {
            session.flush();
        }
        return null;
    }
    
    public List<StudentCourseChanceMemo> getChanceListForStudentMemo(int id, int max, int courseId, boolean isCaste,  boolean isCommunity) throws Exception {
        Session session = null;
        List<StudentCourseChanceMemo> allotment = null;
        try {
            session = HibernateUtil.getSession();
            String hqlQuery = "from StudentCourseChanceMemo s where s.admAppln.id=:id and s.course.id=:courseId";
            if (isCaste) {
                hqlQuery = String.valueOf(hqlQuery) + " and s.isCaste = true";
            }
            else if (isCommunity) {
                hqlQuery = String.valueOf(hqlQuery) + " and s.isCommunity = true";
            }
            else {
                hqlQuery = String.valueOf(hqlQuery) + " and s.isGeneral = true";
            }
            final Query query = session.createQuery(hqlQuery);
            query.setInteger("id", id);
            query.setInteger("courseId", courseId);
            allotment = (List<StudentCourseChanceMemo>)query.list();
        }
        catch (Exception e) {
            AdmissionStatusTransactionImpl.log.error((Object)"Error during getting allotment deatails", (Throwable)e);
            if (session != null) {
                session.flush();
            }
            throw new ApplicationException(e);
        }
        return allotment;
    }
}