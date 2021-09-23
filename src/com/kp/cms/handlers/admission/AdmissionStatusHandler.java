package com.kp.cms.handlers.admission;

import java.util.Set;
import com.kp.cms.transactions.admission.IApplicationEditTransaction;
import java.util.Iterator;
import com.kp.cms.to.admission.InterviewResultTO;
import java.util.Collections;
import com.kp.cms.bo.admin.CandidatePreference;
import java.util.HashMap;
import com.kp.cms.transactionsimpl.admission.ApplicationEditTransactionimpl;
import java.util.ArrayList;
import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.bo.admin.CertificateCourse;
import java.util.Map;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import com.kp.cms.transactions.admission.IAdmissionFormTransaction;
import com.kp.cms.transactionsimpl.admission.AdmissionFormTransactionImpl;
import com.kp.cms.bo.admin.StudentOnlineApplication;
import com.kp.cms.bo.admin.ResidentCategory;
import java.math.BigDecimal;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.bo.admission.StudentAllotmentPGIDetails;
import java.util.Date;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admin.InterviewCardHistory;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.to.admission.InterviewCardTO;
import java.util.List;
import com.kp.cms.bo.admin.AdmAppln;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;
import com.kp.cms.helpers.admission.AdmissionStatusHelper;
import com.kp.cms.transactionsimpl.admission.AdmissionStatusTransactionImpl;
import com.kp.cms.to.admission.AdmissionStatusTO;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class AdmissionStatusHandler
{
    private static final Log log;
    public static volatile AdmissionStatusHandler admissionStatusHandler;
    
    static {
        log = LogFactory.getLog((Class)AdmissionStatusHandler.class);
        AdmissionStatusHandler.admissionStatusHandler = null;
    }
    
    private AdmissionStatusHandler() {
    }
    
    public static AdmissionStatusHandler getInstance() {
        if (AdmissionStatusHandler.admissionStatusHandler == null) {
            AdmissionStatusHandler.admissionStatusHandler = new AdmissionStatusHandler();
        }
        return AdmissionStatusHandler.admissionStatusHandler;
    }
    
    public AdmissionStatusTO getInterviewResult(final String applicationNo, final int appliedYear) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getInterviewResult of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final AdmAppln admAppln = admissionStatusTransaction.getInterviewResult(applicationNo, appliedYear);
        final AdmissionStatusTO admissionStatusTO = AdmissionStatusHelper.getInstance().getInterviewStatus(admAppln);
        AdmissionStatusHandler.log.info((Object)"Leaving from getInterviewResult of AdmissionStatusHandler");
        return admissionStatusTO;
    }
    
    public List<InterviewCardTO> getStudentsList(final String applicationNo) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getStudentsList of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<InterviewCard> iCard = (List<InterviewCard>)admissionStatusTransaction.getStudentsList(Integer.parseInt(applicationNo));
        AdmissionStatusHandler.log.info((Object)"Leaving into getStudentsList of AdmissionStatusHandler");
        AdmissionStatusHelper.getInstance();
        return (List<InterviewCardTO>)AdmissionStatusHelper.getInterviewCardTO((List)iCard);
    }
    
    public List<InterviewCard> getStudentAdmitCardDetails(final String applicationNo, final String interviewTypeId) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getStudentsList of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        AdmissionStatusHandler.log.info((Object)"Leaving into getStudentsList of AdmissionStatusHandler");
        return (List<InterviewCard>)admissionStatusTransaction.getStudentAdmitCardDetails(Integer.parseInt(applicationNo), Integer.parseInt(interviewTypeId));
    }
    
    public List<AdmissionStatusTO> getDetailsAdmAppln(final String applicationNo, final AdmissionStatusForm admissionStatusForm) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getDetailsAdmAppln of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<AdmAppln> newList = (List<AdmAppln>)admissionStatusTransaction.getDetailsAdmAppln(applicationNo);
        AdmissionStatusHandler.log.info((Object)"Leaving into getDetailsAdmAppln of AdmissionStatusHandler");
        return (List<AdmissionStatusTO>)AdmissionStatusHelper.getInstance().populateAdmApplnBOtoTO((List)newList, admissionStatusForm);
    }
    
    public AdmAppln getApplicantDetails(final String applicationNo) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getApplicantDetails of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<AdmAppln> newList = (List<AdmAppln>)admissionStatusTransaction.getDetailsAdmAppln(applicationNo);
        AdmissionStatusHandler.log.info((Object)"Leaving into getApplicantDetails of AdmissionStatusHandler");
        if (newList != null && newList.size() > 0) {
            return newList.get(0);
        }
        return new AdmAppln();
    }
    
    public boolean checkApplnAvailableInAck(final String applicationNo, final String dateOfBirth) throws Exception {
        final IAdmissionStatusTransaction txn = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        boolean exists = false;
        exists = txn.getApplnAcknowledgement(applicationNo, dateOfBirth);
        return exists;
    }
    
    public List<InterviewCardHistory> getStudentAdmitCardHistoryDetails(final String applicationNo) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Entering into getStudentAdmitCardHistoryDetails of AdmissionStatusHandler");
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        AdmissionStatusHandler.log.info((Object)"Leaving into getStudentAdmitCardHistoryDetails of AdmissionStatusHandler");
        return (List<InterviewCardHistory>)admissionStatusTransaction.getStudentAdmitCardHistoryDetails(Integer.parseInt(applicationNo));
    }
    
    public List<AdmissionStatusTO> getToListForStatus(final String applicationNo, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<StudentCourseChanceMemo> allotments = (List<StudentCourseChanceMemo>)admissionStatusTransaction.getBolist(applicationNo, admissionStatusForm.getDateOfBirth());
        final List<StudentCourseAllotment> courseAllotments = (List<StudentCourseAllotment>)admissionStatusTransaction.getBolistForAllotment(applicationNo, admissionStatusForm.getDateOfBirth());
        final List<StudentCourseChanceMemo> chanceMemos = (List<StudentCourseChanceMemo>)admissionStatusTransaction.getBolistForPG(applicationNo, admissionStatusForm.getDateOfBirth());
        final List<StudentCourseChanceMemo> chanceMemosForUg = (List<StudentCourseChanceMemo>)admissionStatusTransaction.getBolistForUg(applicationNo, admissionStatusForm.getDateOfBirth());
        final List<StudentCourseAllotment> courseAllotmentsForUg = (List<StudentCourseAllotment>)admissionStatusTransaction.getBolistForAllotmentUg(applicationNo, admissionStatusForm.getDateOfBirth());
        final int programTypeId = admissionStatusTransaction.getProgramTypeId(applicationNo, admissionStatusForm.getDateOfBirth());
        final int selectedCourseId = admissionStatusTransaction.getSelectedCourseId(applicationNo, admissionStatusForm.getDateOfBirth());
        admissionStatusForm.setProgramTypeId(String.valueOf(programTypeId));
        admissionStatusForm.setSpecialCourse(false);
        List<AdmissionStatusTO> statusTOs = null;
        if (programTypeId == 2) {
            statusTOs = (List<AdmissionStatusTO>)AdmissionStatusHelper.getInstance().setTOPGList((List)chanceMemos, admissionStatusForm, (List)courseAllotments);
            if (selectedCourseId == 32) {
                admissionStatusForm.setSpecialCourse(true);
            }
        }
        else {
            statusTOs = (List<AdmissionStatusTO>)AdmissionStatusHelper.getInstance().setTOPGList((List)chanceMemos, admissionStatusForm, (List)courseAllotments);
        }
        return statusTOs;
    }
    
    public boolean updateCourseAllotment(final String applicationNo, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final IAdmissionStatusTransaction tx = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<StudentCourseChanceMemo> allotments = (List<StudentCourseChanceMemo>)tx.getBolist(applicationNo, admissionStatusForm.getDateOfBirth());
        List<StudentCourseAllotment> courseAllotments = (List<StudentCourseAllotment>)tx.getBolistForAllotment(applicationNo, admissionStatusForm.getDateOfBirth());
        List<StudentCourseChanceMemo> chanceMemos = (List<StudentCourseChanceMemo>)tx.getBolistForPG(applicationNo, admissionStatusForm.getDateOfBirth());
        final int programTypeId = tx.getProgramTypeId(applicationNo, admissionStatusForm.getDateOfBirth());
        final int selectedCourseId = tx.getSelectedCourseId(applicationNo, admissionStatusForm.getDateOfBirth());
        List<StudentCourseChanceMemo> chanceMemosForUg = (List<StudentCourseChanceMemo>)tx.getBolistForUg(applicationNo, admissionStatusForm.getDateOfBirth());
        List<StudentCourseAllotment> courseAllotmentsForUg = (List<StudentCourseAllotment>)tx.getBolistForAllotmentUg(applicationNo, admissionStatusForm.getDateOfBirth());
        admissionStatusForm.setProgramTypeId(String.valueOf(programTypeId));
        boolean isUpdated = false;
        admissionStatusForm.setSpecialCourse(false);
        if (programTypeId == 1) {
            if (chanceMemosForUg.isEmpty()) {
                courseAllotmentsForUg = (List<StudentCourseAllotment>)AdmissionStatusHelper.getInstance().getUpdatedBoPG((List)courseAllotmentsForUg, admissionStatusForm);
                isUpdated = tx.isUpdatedForPg((List)courseAllotmentsForUg);
            }
            else if (courseAllotmentsForUg.isEmpty()) {
                chanceMemosForUg = (List<StudentCourseChanceMemo>)AdmissionStatusHelper.getInstance().getUpdatedBo((List)chanceMemosForUg, admissionStatusForm);
                isUpdated = tx.isUpdated((List)chanceMemosForUg);
            }
            else if (!chanceMemosForUg.isEmpty() && !courseAllotmentsForUg.isEmpty()) {
                isUpdated = tx.isUpdateForBothForUg((List)chanceMemosForUg, (List)courseAllotmentsForUg, admissionStatusForm);
            }
        }
        else {
            if (selectedCourseId == 32) {
                admissionStatusForm.setSpecialCourse(true);
            }
            if (chanceMemos.isEmpty()) {
                courseAllotments = (List<StudentCourseAllotment>)AdmissionStatusHelper.getInstance().getUpdatedBoPG((List)courseAllotments, admissionStatusForm);
                isUpdated = tx.isUpdatedForPg((List)courseAllotments);
            }
            else if (courseAllotments.isEmpty()) {
                chanceMemos = (List<StudentCourseChanceMemo>)AdmissionStatusHelper.getInstance().getUpdatedBo((List)chanceMemos, admissionStatusForm);
                isUpdated = tx.isUpdated((List)chanceMemos);
            }
            else if (!chanceMemos.isEmpty() && !courseAllotments.isEmpty()) {
                isUpdated = tx.isUpdateForBoth((List)chanceMemos, (List)courseAllotments, admissionStatusForm);
            }
        }
        return isUpdated;
    }
    
    public boolean uploadDetail(final String applicationNo, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final IAdmissionStatusTransaction tx = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final StudentCourseChanceMemo allotment = tx.getBoClass(admissionStatusForm.getAdmApplnId());
        if (allotment != null) {
            allotment.setIsUploaded(Boolean.valueOf(true));
            allotment.setLastModifiedDate(new Date());
            allotment.setModifiedBy(admissionStatusForm.getUserId());
        }
        return tx.isUpdateUpload(allotment);
    }
    
    public boolean updateResponse(final AdmissionStatusForm admForm) throws Exception {
        boolean isUpdated = false;
        final StudentAllotmentPGIDetails bo = AdmissionStatusHelper.getInstance().convertToBo(admForm);
        final IAdmissionStatusTransaction tx = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        isUpdated = tx.updateReceivedStatus(bo, admForm);
        return isUpdated;
    }
    
    public String getParameterForPGI(final AdmissionStatusForm admForm) throws Exception {
        final StudentAllotmentPGIDetails bo = new StudentAllotmentPGIDetails();
        bo.setCandidateName(admForm.getApplicantName());
        bo.setCandidateDob((Date)CommonUtil.ConvertStringToSQLDate(admForm.getDateOfBirth()));
        if (admForm.getCourseId() != null && !admForm.getCourseId().isEmpty()) {
            final Course course = new Course();
            course.setId(Integer.parseInt(admForm.getCourseId()));
            bo.setCourse(course);
        }
        bo.setTxnStatus("Pending");
        if (admForm.getApplicationAmount() != null && !admForm.getApplicationAmount().isEmpty()) {
            bo.setTxnAmount(new BigDecimal(admForm.getApplicationAmount()));
        }
        bo.setMobileNo1(admForm.getMobile1());
        bo.setMobileNo2(admForm.getMobile2());
        bo.setEmail(admForm.getEmail());
        final ResidentCategory rc = new ResidentCategory();
        rc.setId(Integer.parseInt(admForm.getResidentCategoryForOnlineAppln()));
        bo.setRefundGenerated(Boolean.valueOf(false));
        bo.setResidentCategory(rc);
        bo.setCreatedBy(admForm.getUserId());
        bo.setCreatedDate(new Date());
        bo.setLastModifiedDate(new Date());
        bo.setModifiedBy(admForm.getUserId());
        if (admForm.getUniqueId() != null && !admForm.getUniqueId().isEmpty()) {
            final StudentOnlineApplication uniqueIdBO = new StudentOnlineApplication();
            uniqueIdBO.setId(Integer.parseInt(admForm.getUniqueId()));
            bo.setUniqueId(uniqueIdBO);
        }
        final IAdmissionFormTransaction transaction = AdmissionFormTransactionImpl.getInstance();
        final String candidateRefNo = transaction.generateCandidateRefNo(bo);
        final StringBuilder temp = new StringBuilder();
        final String productinfo = "productinfo";
        admForm.setRefNo(candidateRefNo);
        admForm.setProductinfo(productinfo);
        if (candidateRefNo != null && !candidateRefNo.isEmpty()) {
            temp.append("F0meUOlq").append("|").append(candidateRefNo).append("|").append(bo.getTxnAmount()).append("|").append(productinfo).append("|").append(bo.getCandidateName()).append("|").append(bo.getEmail()).append("|||||||||||").append("fQmaRSFBHb");
        }
        final String hash = this.hashCal("SHA-512", temp.toString());
        admForm.setTest(temp.toString());
        return hash;
    }
    
    private String hashCal(final String type, final String str) {
        final byte[] hashseq = str.getBytes();
        final StringBuffer hexString = new StringBuffer();
        try {
            final MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            final byte[] messageDigest = algorithm.digest();
            for (int i = 0; i < messageDigest.length; ++i) {
                final String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        }
        catch (NoSuchAlgorithmException ex) {}
        return hexString.toString();
    }
    
    public Map<Integer, String> getActiveCourses1(final int appliedYear) throws Exception {
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<CertificateCourse> courseBoList = (List<CertificateCourse>)admissionStatusTransaction.getActiveCertificateCourses(appliedYear);
        if (courseBoList != null && !courseBoList.isEmpty()) {
            return (Map<Integer, String>)AdmissionStatusHelper.getInstance().populateCourseBOtoTO((List)courseBoList);
        }
        return null;
    }
    
    public boolean saveCertificateCourses(final AdmissionStatusForm admForm) throws Exception {
        final List<StudentCertificateCourse> studCertCourse = (List<StudentCertificateCourse>)AdmissionStatusHelper.getInstance().copyBoTo((List)admForm.getPrefList(), admForm);
        final IAdmissionStatusTransaction admissionStatusTransaction = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final boolean result = admissionStatusTransaction.saveStudentCertificateCourse((List)studCertCourse);
        return result;
    }
    
    public List<AdmissionStatusTO> getchanceList(final List<AdmAppln> admApplnBO, final AdmissionStatusForm admissionStatusForm) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Inside of populateAdmApplnBOtoTO of AdmissionStatusHelper");
        final InterviewResultTO interviewResultTO = null;
        final List<AdmissionStatusTO> admAppln = new ArrayList<AdmissionStatusTO>();
        if (admApplnBO != null) {
            final IAdmissionStatusTransaction txn = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
            final Iterator<AdmAppln> iterator = admApplnBO.iterator();
            if (iterator.hasNext()) {
                final AdmAppln appln = iterator.next();
                final IApplicationEditTransaction txn2 = (IApplicationEditTransaction)ApplicationEditTransactionimpl.getInstance();
                final Map<Integer, AdmissionStatusTO> chanceMap = new HashMap<Integer, AdmissionStatusTO>();
                final List<AdmissionStatusTO> chanceListFormap = new ArrayList<AdmissionStatusTO>();
                final Set<CandidatePreference> prefSet = (Set<CandidatePreference>)appln.getCandidatePreferences();
                for (final CandidatePreference candidatePreference : prefSet) {
                    Boolean isCaste = false;
                    Boolean isCommunity = false;
                    Integer maxChanceNo = txn2.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    int mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    List<StudentCourseChanceMemo> chanceList = (List<StudentCourseChanceMemo>)txn.getChanceListForStudentMemo(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        final Iterator<StudentCourseChanceMemo> chanceItr = chanceList.iterator();
                        while (chanceItr.hasNext()) {
                            final AdmissionStatusTO to = new AdmissionStatusTO();
                            final StudentCourseChanceMemo memo = chanceItr.next();
                            if (memo.getCourse().getDateTime() != null) {
                                to.setDateTime(memo.getCourse().getChanceGenDateTime());
                                to.setCommunityDateTime(memo.getCourse().getChanceGenDateTime());
                            }
                            to.setChanceGeneralFee(memo.getCourse().getGeneralFee());
                            to.setChanceCasteFee(memo.getCourse().getCasteFee());
                            if (memo.getCourse().getGeneralFee() != null) {
                                to.setGeneralFee(memo.getCourse().getGeneralFee());
                            }
                            if (memo.getCourse().getCasteFee() != null) {
                                to.setCasteFee(memo.getCourse().getCasteFee());
                            }
                            to.setChanceCurrentcourse(memo.getCourse().getName());
                            to.setChanceCurrentcourseid(memo.getCourse().getId());
                            to.setChancePref((int)memo.getPrefNo());
                            to.setChanceIndexmark(memo.getIndexMark().toString());
                            to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                            if (memo.getIsCaste()) {
                                to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                                if ((memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 3) && memo.getCourse().getChanceCasteDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCasteDateTime());
                                }
                                to.setChanceAlmntcaste(true);
                                to.setCasteRank(memo.getChanceRank());
                            }
                            else if (memo.getIsGeneral()) {
                                to.setChanceCategory("GENERAL");
                                to.setChanceAlmntgeneral(true);
                                to.setGenRank(memo.getChanceRank());
                            }
                            else if (memo.getIsCommunity()) {
                                if (memo.getCourse().getChanceCommDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCommDateTime());
                                }
                                to.setChanceCategory("COMMUNITY");
                                to.setCasteRank(memo.getChanceRank());
                                to.setChanceAlmntCommunity(true);
                            }
                            chanceMap.put(memo.getCourse().getId(), to);
                            chanceListFormap.add(to);
                        }
                    }
                    isCaste = true;
                    maxChanceNo = txn2.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    chanceList = (List<StudentCourseChanceMemo>)txn.getChanceListForStudentMemo(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        final Iterator<StudentCourseChanceMemo> chanceItr = chanceList.iterator();
                        while (chanceItr.hasNext()) {
                            final AdmissionStatusTO to = new AdmissionStatusTO();
                            final StudentCourseChanceMemo memo = chanceItr.next();
                            if (memo.getCourse().getDateTime() != null) {
                                to.setDateTime(memo.getCourse().getChanceGenDateTime());
                                to.setCommunityDateTime(memo.getCourse().getChanceGenDateTime());
                            }
                            to.setChanceGeneralFee(memo.getCourse().getGeneralFee());
                            to.setChanceCasteFee(memo.getCourse().getCasteFee());
                            if (memo.getCourse().getGeneralFee() != null) {
                                to.setGeneralFee(memo.getCourse().getGeneralFee());
                            }
                            if (memo.getCourse().getCasteFee() != null) {
                                to.setCasteFee(memo.getCourse().getCasteFee());
                            }
                            to.setChanceCurrentcourse(memo.getCourse().getName());
                            to.setChanceCurrentcourseid(memo.getCourse().getId());
                            to.setChancePref((int)memo.getPrefNo());
                            to.setChanceIndexmark(memo.getIndexMark().toString());
                            to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                            if (memo.getIsCaste()) {
                                to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                                if ((memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 3) && memo.getCourse().getChanceCasteDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCasteDateTime());
                                }
                                to.setChanceAlmntcaste(true);
                                to.setCasteRank(memo.getChanceRank());
                            }
                            else if (memo.getIsGeneral()) {
                                to.setChanceCategory("GENERAL");
                                to.setChanceAlmntgeneral(true);
                                to.setGenRank(memo.getChanceRank());
                            }
                            else if (memo.getIsCommunity()) {
                                if (memo.getCourse().getChanceCommDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCommDateTime());
                                }
                                to.setChanceCategory("COMMUNITY");
                                to.setCasteRank(memo.getChanceRank());
                                to.setChanceAlmntCommunity(true);
                            }
                            chanceMap.put(memo.getCourse().getId(), to);
                            chanceListFormap.add(to);
                        }
                    }
                    isCaste = false;
                    isCommunity = true;
                    maxChanceNo = txn2.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    chanceList = (List<StudentCourseChanceMemo>)txn.getChanceListForStudentMemo(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        final Iterator<StudentCourseChanceMemo> chanceItr = chanceList.iterator();
                        while (chanceItr.hasNext()) {
                            final AdmissionStatusTO to = new AdmissionStatusTO();
                            final StudentCourseChanceMemo memo = chanceItr.next();
                            if (memo.getCourse().getDateTime() != null) {
                                to.setDateTime(memo.getCourse().getChanceGenDateTime());
                                to.setCommunityDateTime(memo.getCourse().getChanceGenDateTime());
                            }
                            to.setChanceGeneralFee(memo.getCourse().getGeneralFee());
                            to.setChanceCasteFee(memo.getCourse().getCasteFee());
                            if (memo.getCourse().getGeneralFee() != null) {
                                to.setGeneralFee(memo.getCourse().getGeneralFee());
                            }
                            if (memo.getCourse().getCasteFee() != null) {
                                to.setCasteFee(memo.getCourse().getCasteFee());
                            }
                            to.setChanceCurrentcourse(memo.getCourse().getName());
                            to.setChanceCurrentcourseid(memo.getCourse().getId());
                            to.setChancePref((int)memo.getPrefNo());
                            to.setChanceIndexmark(memo.getIndexMark().toString());
                            to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                            if (memo.getIsCaste()) {
                                to.setChanceCategory(memo.getAdmAppln().getPersonalData().getReligionSection().getName());
                                if ((memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || memo.getAdmAppln().getPersonalData().getReligionSection().getId() == 3) && memo.getCourse().getChanceCasteDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCasteDateTime());
                                }
                                to.setChanceAlmntcaste(true);
                                to.setCasteRank(memo.getChanceRank());
                            }
                            else if (memo.getIsGeneral()) {
                                to.setChanceCategory("GENERAL");
                                to.setChanceAlmntgeneral(true);
                                to.setGenRank(memo.getChanceRank());
                            }
                            else if (memo.getIsCommunity()) {
                                if (memo.getCourse().getChanceCommDateTime() != null) {
                                    to.setCommunityDateTime(memo.getCourse().getChanceCommDateTime());
                                }
                                to.setChanceCategory("COMMUNITY");
                                to.setCasteRank(memo.getChanceRank());
                                to.setChanceAlmntCommunity(true);
                            }
                            chanceMap.put(memo.getCourse().getId(), to);
                            chanceListFormap.add(to);
                        }
                    }
                }
                Collections.sort(chanceListFormap);
                return chanceListFormap;
            }
        }
        AdmissionStatusHandler.log.info((Object)"End of populateAdmApplnBOtoTO of AdmissionStatusHelper");
        return null;
    }
    
    private static String convertBoolValueIsSelected(final Boolean value, final boolean isFinalMeritApproved) throws Exception {
        AdmissionStatusHandler.log.info((Object)"Inside of convertBoolValueIsSelected of AdmissionStatusHelper");
        if (value && isFinalMeritApproved) {
            return "You are Selected for Admission";
        }
        AdmissionStatusHandler.log.info((Object)"End of convertBoolValueIsSelected of AdmissionStatusHelper");
        return "Not selected for admission";
    }
}