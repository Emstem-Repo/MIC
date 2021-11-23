package com.kp.cms.helpers.admission;

import com.kp.cms.bo.admin.StudentCertificateCourse;
import com.kp.cms.to.admission.CertificateCourseTO;
import com.kp.cms.bo.admin.CertificateCourse;
import java.math.BigDecimal;
import java.util.Date;
import com.kp.cms.bo.admission.StudentAllotmentPGIDetails;
import com.kp.cms.transactions.admission.IAdmissionStatusTransaction;
import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.transactionsimpl.admission.AdmissionStatusTransactionImpl;
import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.StudentSpecializationPrefered;
import java.util.Collection;
import com.kp.cms.bo.admin.InterviewSelected;
import com.kp.cms.bo.admin.Organisation;
import javax.servlet.http.HttpSession;
import com.kp.cms.constants.CMSConstants;
import java.io.FileInputStream;
import com.kp.cms.handlers.admin.OrganizationHandler;
import java.io.File;
import com.kp.cms.bo.admin.InterviewCardHistory;
import javax.servlet.http.HttpServletRequest;
import com.kp.cms.to.admission.InterviewScheduleTO;
import com.kp.cms.to.admin.CourseTO;
import com.kp.cms.to.admission.InterviewCardTO;
import com.kp.cms.bo.admin.InterviewCard;
import com.kp.cms.to.admission.PersonalDataTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.CandidatePGIDetails;
import com.kp.cms.transactions.admission.IApplicationEditTransaction;
import com.kp.cms.bo.admin.StudentCourseAllotment;
import com.kp.cms.transactions.admission.IAdmSubjectForRank;
import java.util.Iterator;
import com.kp.cms.transactions.admission.IAdmissionFormTransaction;
import com.kp.cms.handlers.admin.TemplateHandler;
import java.util.Collections;
import java.util.Map;
import com.kp.cms.bo.admin.StudentCourseChanceMemo;
import com.kp.cms.bo.admin.CandidatePreference;
import java.util.HashMap;
import com.kp.cms.transactionsimpl.admission.ApplicationEditTransactionimpl;
import com.kp.cms.bo.admin.StudentRank;
import com.kp.cms.transactionsimpl.admission.AdmSubjectForRankTransactionImpl;
import com.kp.cms.bo.admin.Student;
import java.util.Set;
import com.kp.cms.to.admission.InterviewProgramCourseTO;
import com.kp.cms.bo.admin.InterviewResult;
import com.kp.cms.to.admission.InterviewResultTO;
import java.util.HashSet;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.transactionsimpl.admission.AdmissionFormTransactionImpl;
import java.util.ArrayList;
import com.kp.cms.to.admission.AdmissionStatusTO;
import com.kp.cms.forms.admission.AdmissionStatusForm;
import com.kp.cms.bo.admin.AdmAppln;
import java.util.List;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class AdmissionStatusHelper
{
    private static final Log log;
    public static volatile AdmissionStatusHelper admissionStatusHelper;
    public static final String PHOTOBYTES = "PhotoBytes";
    private static final String SQL_DATEFORMAT = "dd-MMM-yyyy";
    
    static {
        log = LogFactory.getLog((Class)AdmissionStatusHelper.class);
        AdmissionStatusHelper.admissionStatusHelper = null;
    }
    
    public static AdmissionStatusHelper getInstance() {
        if (AdmissionStatusHelper.admissionStatusHelper == null) {
            return AdmissionStatusHelper.admissionStatusHelper = new AdmissionStatusHelper();
        }
        return AdmissionStatusHelper.admissionStatusHelper;
    }
    
    public List<AdmissionStatusTO> populateAdmApplnBOtoTO(final List<AdmAppln> admApplnBO, final AdmissionStatusForm admissionStatusForm) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Inside of populateAdmApplnBOtoTO of AdmissionStatusHelper");
        AdmissionStatusTO admissionStatusTO = null;
        InterviewResultTO interviewResultTO = null;
        final List<AdmissionStatusTO> admAppln = new ArrayList<AdmissionStatusTO>();
        if (admApplnBO != null) {
            final IAdmissionFormTransaction txn = (IAdmissionFormTransaction)new AdmissionFormTransactionImpl();
            for (final AdmAppln appln : admApplnBO) {
                admissionStatusTO = new AdmissionStatusTO();
                admissionStatusTO.setId(appln.getId());
                if (appln.getIsBypassed() != null && appln.getIsBypassed()) {
                    admissionStatusTO.setByPassed((boolean)appln.getIsBypassed());
                }
                else if (appln.getIsSelected() != null && appln.getPersonalData() != null && appln.getPersonalData().getDateOfBirth() != null && appln.getPersonalData().getId() != 0 && appln.getIsCancelled() != null) {
                    admissionStatusTO.setApplicationNo(appln.getApplnNo());
                    admissionStatusTO.setAppliedYear((int)appln.getAppliedYear());
                    admissionStatusTO.setDateOfBirth(CommonUtil.getStringDate(appln.getPersonalData().getDateOfBirth()));
                    admissionStatusTO.setPersonalDataId(appln.getPersonalData().getId());
                    boolean isFinalMeritApproved = false;
                    if (appln.getIsFinalMeritApproved() != null) {
                        isFinalMeritApproved = appln.getIsFinalMeritApproved();
                    }
                    admissionStatusTO.setIsSelected(convertBoolValueIsSelected(appln.getIsSelected(), isFinalMeritApproved));
                }
                if (appln.getPersonalData() != null && appln.getPersonalData().getEmail() != null) {
                    admissionStatusTO.setEmail(appln.getPersonalData().getEmail());
                }
                admissionStatusTO.setCancelled((boolean)appln.getIsCancelled());
                if (appln.getAppliedYear() != 0) {
                    admissionStatusTO.setAppliedYear((int)appln.getAppliedYear());
                }
                if (appln.getCourseBySelectedCourseId() != null) {
                    admissionStatusTO.setCourseId(appln.getCourseBySelectedCourseId().getId());
                }
                final Set<InterviewResult> intResultSet = (Set<InterviewResult>)appln.getInterviewResults();
                final Set<InterviewResultTO> interviewResultTOSet = new HashSet<InterviewResultTO>();
                for (final InterviewResult interviewResult : intResultSet) {
                    if (interviewResult != null) {
                        interviewResultTO = new InterviewResultTO();
                        final InterviewProgramCourseTO interviewProgramCourseTO = new InterviewProgramCourseTO();
                        if (interviewResult.getInterviewStatus() == null || interviewResult.getInterviewProgramCourse() == null) {
                            continue;
                        }
                        if (interviewResult.getInterviewStatus().getName() != null && !interviewResult.getInterviewStatus().getName().isEmpty()) {
                            interviewResultTO.setInterviewStatus(interviewResult.getInterviewStatus().getName());
                        }
                        if (interviewResult.getId() != 0) {
                            interviewResultTO.setId((int)interviewResult.getId());
                        }
                        if (interviewResult.getInterviewProgramCourse().getName() != null && !interviewResult.getInterviewProgramCourse().getName().isEmpty()) {
                            interviewProgramCourseTO.setName(interviewResult.getInterviewProgramCourse().getName());
                        }
                        if (interviewResult.getInterviewProgramCourse().getId() != 0) {
                            interviewProgramCourseTO.setId((int)interviewResult.getInterviewProgramCourse().getId());
                        }
                        interviewResultTO.setInterviewProgramCourseTO(interviewProgramCourseTO);
                        interviewResultTOSet.add(interviewResultTO);
                    }
                }
                admissionStatusTO.setAdmStatus(appln.getAdmStatus());
                admissionStatusTO.setInterviewResultTO((Set)interviewResultTOSet);
                final Set<Student> studentSet = (Set<Student>)appln.getStudents();
                final Iterator<Student> stItr = studentSet.iterator();
                boolean isAdmitted = false;
                while (stItr.hasNext()) {
                    final Student student = stItr.next();
                    if (student.getIsAdmitted() != null) {
                        isAdmitted = student.getIsAdmitted();
                    }
                }
                admissionStatusTO.setAdmitted(isAdmitted);
                if (appln.getMode() != null && appln.getMode().equalsIgnoreCase("CHALLAN")) {
                    if (appln.getIsChallanRecieved() != null && appln.getIsChallanRecieved()) {
                        admissionStatusForm.setIsChallanRecieved(1);
                    }
                    else {
                        admissionStatusForm.setIsChallanRecieved(0);
                    }
                }
                else if (appln.getMode() != null && appln.getMode().equalsIgnoreCase("NEFT")) {
                    if (appln.getIsChallanRecieved() != null && appln.getIsChallanRecieved()) {
                        admissionStatusForm.setIsChallanRecieved(1);
                    }
                    else {
                        admissionStatusForm.setIsChallanRecieved(0);
                    }
                }
                else if (appln.getApplnNo() != 0 && appln.getMode() != null && appln.getMode().equalsIgnoreCase("Online")) {
                    admissionStatusForm.setIsChallanRecieved(1);
                }
                final IAdmSubjectForRank txn2 = (IAdmSubjectForRank)new AdmSubjectForRankTransactionImpl();
                final List<StudentRank> Ranklist = (List<StudentRank>)txn2.getRankDetails((int)appln.getAppliedYear(), appln.getCourse().getId());
                final Iterator<StudentRank> itrt1 = Ranklist.iterator();
                int k = 0;
                int rank = 0;
                if (Ranklist != null) {
                    while (itrt1.hasNext()) {
                        final StudentRank studentRank = itrt1.next();
                        ++k;
                        if (appln.getId() == studentRank.getAdmAppln().getId()) {
                            rank = k;
                        }
                    }
                }
                final String limit = "";
                final Integer maxallotment = txn.getmaxallotment(appln.getCourse().getProgram().getProgramType().getId(), (int)appln.getAppliedYear());
                final Integer maxChance = txn.getMaxChance(appln.getCourse().getProgram().getProgramType().getId(), appln.getAppliedYear(), admissionStatusForm.getApplicationNo());
                int mxal = 0;
                if (maxChance != null) {
                    mxal = maxChance;
                }
                if (appln.getCourse().getProgram().getProgramType().getId() == 2) {
                    if (maxChance != null) {
                        mxal = maxChance;
                    }
                    else if (maxallotment != null) {
                        mxal = maxallotment;
                    }
                }
                else if (maxChance != null) {
                    mxal = maxChance;
                }
                else if (maxallotment != null) {
                    mxal = maxallotment;
                }
                admissionStatusForm.setMaxallotment(mxal);
                final StudentCourseAllotment allotmentdata = txn.getallotmentdetails(appln.getId(), mxal);
                if (allotmentdata != null) {
                    admissionStatusTO.setIndexmark(allotmentdata.getIndexMark().toString());
                    admissionStatusTO.setCurrentcourse(allotmentdata.getCourse().getName());
                    if (allotmentdata.getCourse().getGeneralFee() != null) {
                        admissionStatusTO.setGeneralFee(allotmentdata.getCourse().getGeneralFee());
                    }
                    if (allotmentdata.getCourse().getCasteFee() != null) {
                        admissionStatusTO.setCasteFee(allotmentdata.getCourse().getCasteFee());
                    }
                    admissionStatusTO.setCurrentcourseid(allotmentdata.getCourse().getId());
                    admissionStatusTO.setPref((int)allotmentdata.getPrefNo());
                    admissionStatusTO.setAllotmentno((int)allotmentdata.getAllotmentNo());
                    admissionStatusTO.setAssigned(allotmentdata.getIsAssigned());
                    admissionStatusTO.setCancel(allotmentdata.getIsCancel());
                    admissionStatusTO.setAlmntcaste(allotmentdata.getIsCaste());
                    admissionStatusTO.setAlmntgeneral(allotmentdata.getIsGeneral());
                    admissionStatusTO.setAlmntCommunity(allotmentdata.getIsCommunity());
                    if (allotmentdata.getCourse().getDateTime() != null) {
                        admissionStatusTO.setDateTime(allotmentdata.getCourse().getDateTime());
                    }
                    if (admissionStatusTO.isAlmntgeneral()) {
                        admissionStatusTO.setAltmntcategory("GENERAL");
                        final int memorank = txn.getmemorank(allotmentdata.getCourse().getId(), appln.getAppliedYear(), appln.getPersonalData().getReligionSection().getId(), appln.getId(), 1);
                        admissionStatusTO.setRank(memorank);
                    }
                    else if (admissionStatusTO.isAlmntcaste()) {
                        final int memorank = txn.getmemorank(allotmentdata.getCourse().getId(), appln.getAppliedYear(), appln.getPersonalData().getReligionSection().getId(), appln.getId(), 3);
                        admissionStatusTO.setRank(memorank);
                        admissionStatusTO.setAltmntcategory(allotmentdata.getAdmAppln().getPersonalData().getReligionSection().getName());
                        if ((allotmentdata.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || allotmentdata.getAdmAppln().getPersonalData().getReligionSection().getId() == 3) && allotmentdata.getCourse().getCasteDateTime() != null) {
                            admissionStatusTO.setDateTime(allotmentdata.getCourse().getCasteDateTime());
                        }
                    }
                    else if (admissionStatusTO.getAlmntCommunity()) {
                        admissionStatusTO.setAltmntcategory("COMMUNITY");
                        final int memorank = txn.getmemorank(allotmentdata.getCourse().getId(), appln.getAppliedYear(), appln.getPersonalData().getReligionSection().getId(), appln.getId(), 2);
                        admissionStatusTO.setRank(memorank);
                        if (allotmentdata.getCourse().getCommunityDateTime() != null) {
                            admissionStatusTO.setDateTime(allotmentdata.getCourse().getCommunityDateTime());
                        }
                        admissionStatusTO.setRank(memorank);
                    }
                    admissionStatusTO.setAllotmentflag(true);
                }
                final IApplicationEditTransaction txn3 = (IApplicationEditTransaction)ApplicationEditTransactionimpl.getInstance();
                final Map<Integer, AdmissionStatusTO> chanceMap = new HashMap<Integer, AdmissionStatusTO>();
                final List<AdmissionStatusTO> chanceListFormap = new ArrayList<AdmissionStatusTO>();
                final Set<CandidatePreference> prefSet = (Set<CandidatePreference>)appln.getCandidatePreferences();
                for (final CandidatePreference candidatePreference : prefSet) {
                    Boolean isCaste = false;
                    Boolean isCommunity = false;
                    Integer maxChanceNo = txn3.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    int mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    List<StudentCourseChanceMemo> chanceList = (List<StudentCourseChanceMemo>)txn.GetChanceListForStudent(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        admissionStatusTO.setChanceflag(true);
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
                    maxChanceNo = txn3.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    chanceList = (List<StudentCourseChanceMemo>)txn.GetChanceListForStudent(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        admissionStatusTO.setChanceflag(true);
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
                    maxChanceNo = txn3.getMaxChanceNo(appln.getAppliedYear(), Integer.valueOf(appln.getCourse().getProgram().getProgramType().getId()), Integer.valueOf(candidatePreference.getCourse().getId()), isCaste, isCommunity);
                    mxChance = 0;
                    if (maxChanceNo != null) {
                        mxChance = maxChanceNo;
                    }
                    chanceList = (List<StudentCourseChanceMemo>)txn.GetChanceListForStudent(appln.getId(), Integer.valueOf(mxChance), candidatePreference.getCourse().getId(), isCaste, isCommunity);
                    if (chanceList != null && chanceList.size() > 0) {
                        admissionStatusTO.setChanceflag(true);
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
                admissionStatusTO.setChanceMemoMap((Map)chanceMap);
                Collections.sort(chanceListFormap);
                admissionStatusTO.setChanceList((List)chanceListFormap);
                admAppln.add(admissionStatusTO);
                final CandidatePGIDetails details = txn.getCandidateDetails(appln.getId());
                final TemplateHandler temphandle = TemplateHandler.getInstance();
                final List<GroupTemplate> list = (List<GroupTemplate>)temphandle.getDuplicateCheckList("Online payment Acknowledgement Slip");
                if (list != null && !list.isEmpty() && details != null) {
                    admissionStatusForm.setOnlineAcknowledgement(true);
                }
            }
        }
        AdmissionStatusHelper.log.info((Object)"End of populateAdmApplnBOtoTO of AdmissionStatusHelper");
        return admAppln;
    }
    
    public InterviewResultTO populategetInterviewResultBOtoTO(final InterviewResult interviewResult) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Inside of populategetInterviewResultBOtoTO of AdmissionStatusHelper");
        AdmApplnTO admApplnTO = null;
        PersonalDataTO personalDataTO = null;
        if (interviewResult != null) {
            final InterviewResultTO interviewResultTO = new InterviewResultTO();
            if (interviewResult.getId() != 0) {
                interviewResultTO.setId((int)interviewResult.getId());
            }
            if (interviewResult.getAdmAppln() != null && interviewResult.getAdmAppln().getPersonalData() != null && interviewResult.getAdmAppln().getPersonalData().getEmail() != null) {
                admApplnTO = new AdmApplnTO();
                personalDataTO = new PersonalDataTO();
                admApplnTO.setId(interviewResult.getAdmAppln().getId());
                personalDataTO.setEmail(interviewResult.getAdmAppln().getPersonalData().getEmail());
                admApplnTO.setPersonalData(personalDataTO);
                interviewResultTO.setAdmApplnTO(admApplnTO);
            }
            if (interviewResult.getAdmAppln() != null && interviewResult.getAdmAppln().getCourseBySelectedCourseId() != null) {
                interviewResultTO.setCourseId(String.valueOf(interviewResult.getAdmAppln().getCourseBySelectedCourseId().getId()));
            }
            if (interviewResult.getInterviewProgramCourse() != null && interviewResult.getInterviewProgramCourse().getName() != null && !interviewResult.getInterviewProgramCourse().getName().startsWith("You are invited for ")) {
                interviewResultTO.setInterviewStatus("Results under process");
            }
            final InterviewProgramCourseTO interviewProgramCourseTO = new InterviewProgramCourseTO();
            if (interviewResult.getInterviewProgramCourse() != null) {
                if (interviewResult.getInterviewProgramCourse().getName() != null && !interviewResult.getInterviewProgramCourse().getName().isEmpty()) {
                    interviewProgramCourseTO.setName(interviewResult.getInterviewProgramCourse().getName());
                }
                if (interviewResult.getInterviewProgramCourse().getId() != 0) {
                    interviewProgramCourseTO.setId((int)interviewResult.getInterviewProgramCourse().getId());
                }
                interviewResultTO.setInterviewProgramCourseTO(interviewProgramCourseTO);
            }
            return interviewResultTO;
        }
        AdmissionStatusHelper.log.info((Object)"End of populategetInterviewResultBOtoTO of AdmissionStatusHelper");
        return null;
    }
    
    private static String convertBoolValueIsSelected(final Boolean value, final boolean isFinalMeritApproved) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Inside of convertBoolValueIsSelected of AdmissionStatusHelper");
        if (value && isFinalMeritApproved) {
            return "You are Selected for Admission";
        }
        AdmissionStatusHelper.log.info((Object)"End of convertBoolValueIsSelected of AdmissionStatusHelper");
        return "Not selected for admission";
    }
    
    public static List<InterviewCardTO> getInterviewCardTO(final List<InterviewCard> interviewCard) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Start of getInterviewCardTO of AdmissionStatusHelper");
        InterviewCardTO interviewCardTO = null;
        final List<InterviewCardTO> interviewCardTOList = new ArrayList<InterviewCardTO>();
        InterviewCard iCard = null;
        if (interviewCard != null) {
            final Iterator<InterviewCard> itr = interviewCard.iterator();
            while (itr.hasNext()) {
                final AdmApplnTO admApplnTO = new AdmApplnTO();
                final PersonalDataTO personalDataTO = new PersonalDataTO();
                final CourseTO courseTO = new CourseTO();
                final InterviewScheduleTO interviewScheduleTO = new InterviewScheduleTO();
                iCard = itr.next();
                interviewCardTO = new InterviewCardTO();
                if (iCard != null) {
                    if (iCard.getAdmAppln() != null && Integer.valueOf(iCard.getAdmAppln().getApplnNo()) != null) {
                        admApplnTO.setApplnNo(iCard.getAdmAppln().getApplnNo());
                    }
                    if (iCard.getAdmAppln() != null && iCard.getAdmAppln().getPersonalData() != null && iCard.getAdmAppln().getPersonalData().getFirstName() != null) {
                        personalDataTO.setFirstName(iCard.getAdmAppln().getPersonalData().getFirstName());
                        admApplnTO.setPersonalData(personalDataTO);
                    }
                    if (iCard.getAdmAppln() != null && iCard.getAdmAppln().getCourseBySelectedCourseId() != null && iCard.getAdmAppln().getCourseBySelectedCourseId().getName() != null) {
                        courseTO.setCode(iCard.getAdmAppln().getCourseBySelectedCourseId().getName());
                        admApplnTO.setCourse(courseTO);
                    }
                    interviewCardTO.setAdmApplnTO(admApplnTO);
                    if (iCard.getInterview() != null && iCard.getInterview().getDate() != null) {
                        interviewScheduleTO.setDate(CommonUtil.getStringDate(iCard.getInterview().getDate()));
                    }
                    interviewCardTO.setInterview(interviewScheduleTO);
                    interviewCardTO.setTime(iCard.getTime());
                    if (iCard.getInterview() != null && iCard.getInterview().getInterview() != null && iCard.getInterview().getInterview().getInterviewProgramCourse() != null && iCard.getInterview().getInterview().getInterviewProgramCourse().getName() != null) {
                        interviewCardTO.setInterviewType(String.valueOf(iCard.getInterview().getInterview().getInterviewProgramCourse().getName()));
                    }
                }
                interviewCardTOList.add(interviewCardTO);
            }
        }
        AdmissionStatusHelper.log.info((Object)"End of getInterviewCardTO of AdmissionStatusHelper");
        return interviewCardTOList;
    }
    
    public static String generateAdmitCard(final String templateDescription, final List<InterviewCard> admitCardList, final HttpServletRequest request, final List<InterviewCardHistory> admitCardHistoryList, final File barCodeImgFile) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Inside of generateAdmitCard of AdmissionStatusHelper");
        String program = "";
        String course = "";
        String selectedCourse = "";
        String applicationNo = "";
        String interviewVenue = "";
        String academicYear = "";
        String applicantName = "";
        String dateOfBirth = "";
        String placeOfBirth = "";
        String nationality = "";
        String religion = "";
        String subreligion = "";
        String residentCategory = "";
        String category = "";
        String gender = "";
        String email = "";
        String finalTemplate = "";
        String interviewType = "";
        String interviewDate = "";
        String interviewTime = "";
        String contextPath = "";
        String logoPath = "";
        String seatNo = "";
        String examCenter = "";
        final StringBuffer currentAddress = new StringBuffer();
        final StringBuffer permanentAddress = new StringBuffer();
        String examCenterAddress1 = "";
        String examCenterAddress2 = "";
        String examCenterAddress3 = "";
        String examCenterAddress4 = "";
        String seatNoPrefix = "";
        byte[] logo = null;
        String mode = "";
        String barCode = "";
        byte[] barCodeByte = null;
        final HttpSession session = request.getSession(false);
        if (templateDescription != null && !templateDescription.trim().isEmpty() && admitCardList != null && !admitCardList.isEmpty()) {
            final InterviewCard applicantDetails = admitCardList.get(0);
            if (applicantDetails != null) {
                if (applicantDetails.getAdmAppln() != null && applicantDetails.getAdmAppln().getPersonalData() != null) {
                    if (applicantDetails.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                        dateOfBirth = CommonUtil.getStringDate(applicantDetails.getAdmAppln().getPersonalData().getDateOfBirth());
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getBirthPlace() != null) {
                        placeOfBirth = applicantDetails.getAdmAppln().getPersonalData().getBirthPlace();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getFirstName() != null && !applicantDetails.getAdmAppln().getPersonalData().getFirstName().trim().isEmpty()) {
                        applicantName = applicantDetails.getAdmAppln().getPersonalData().getFirstName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getMiddleName() != null && !applicantDetails.getAdmAppln().getPersonalData().getMiddleName().trim().isEmpty()) {
                        applicantName = String.valueOf(applicantName) + " " + applicantDetails.getAdmAppln().getPersonalData().getMiddleName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getLastName() != null && !applicantDetails.getAdmAppln().getPersonalData().getLastName().trim().isEmpty()) {
                        applicantName = String.valueOf(applicantName) + " " + applicantDetails.getAdmAppln().getPersonalData().getLastName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getNationalityOthers() != null) {
                        nationality = applicantDetails.getAdmAppln().getPersonalData().getNationalityOthers();
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getNationality() != null) {
                        nationality = applicantDetails.getAdmAppln().getPersonalData().getNationality().getName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getReligionOthers() != null) {
                        religion = applicantDetails.getAdmAppln().getPersonalData().getReligionOthers();
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getReligion() != null) {
                        religion = applicantDetails.getAdmAppln().getPersonalData().getReligion().getName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getReligionSectionOthers() != null) {
                        subreligion = applicantDetails.getAdmAppln().getPersonalData().getReligionSectionOthers();
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getReligionSection() != null) {
                        subreligion = applicantDetails.getAdmAppln().getPersonalData().getReligionSection().getName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getResidentCategory() != null && applicantDetails.getAdmAppln().getPersonalData().getResidentCategory().getName() != null) {
                        residentCategory = applicantDetails.getAdmAppln().getPersonalData().getResidentCategory().getName();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getGender() != null) {
                        gender = applicantDetails.getAdmAppln().getPersonalData().getGender();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getEmail() != null) {
                        email = applicantDetails.getAdmAppln().getPersonalData().getEmail();
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCasteOthers() != null) {
                        category = applicantDetails.getAdmAppln().getPersonalData().getCasteOthers();
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getCaste() != null) {
                        category = applicantDetails.getAdmAppln().getPersonalData().getCaste().getName();
                    }
                    if (applicantDetails.getAdmAppln().getCourseBySelectedCourseId() != null) {
                        course = applicantDetails.getAdmAppln().getCourseBySelectedCourseId().getName();
                    }
                    if (applicantDetails.getAdmAppln().getCourseBySelectedCourseId() != null) {
                        selectedCourse = applicantDetails.getAdmAppln().getCourseBySelectedCourseId().getName();
                    }
                    if (applicantDetails.getAdmAppln().getCourseBySelectedCourseId() != null && applicantDetails.getAdmAppln().getCourseBySelectedCourseId().getProgram() != null) {
                        program = applicantDetails.getAdmAppln().getCourseBySelectedCourseId().getProgram().getName();
                    }
                    applicationNo = String.valueOf(applicantDetails.getAdmAppln().getApplnNo());
                    academicYear = String.valueOf(applicantDetails.getAdmAppln().getAppliedYear());
                    if (applicantDetails.getAdmAppln() != null && applicantDetails.getAdmAppln().getStudents() != null) {
                        for (final Student student : applicantDetails.getAdmAppln().getStudents()) {
                            contextPath = "<img src='images/StudentPhotos/" + student.getId() + ".jpg?" + applicantDetails.getAdmAppln().getLastModifiedDate() + "' alt='Photo not available' width='150' height='150' >";
                        }
                    }
                    else {
                        contextPath = "<img src='images/photoblank.gif' width='150' height='150' >";
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressLine1() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressLine1());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressLine2() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressLine2());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCityByCurrentAddressCityId() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCityByCurrentAddressCityId());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getStateByCurrentAddressStateId() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getStateByCurrentAddressStateId().getName());
                        currentAddress.append(' ');
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressStateOthers() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressStateOthers());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCountryByCurrentAddressCountryId() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCountryByCurrentAddressCountryId().getName());
                        currentAddress.append(' ');
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressCountryOthers() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressCountryOthers());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressZipCode() != null) {
                        currentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCurrentAddressZipCode());
                        currentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getParentAddressLine1() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getParentAddressLine1());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getParentAddressLine2() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getParentAddressLine2());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCityByPermanentAddressCityId() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCityByPermanentAddressCityId());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getStateByParentAddressStateId() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getStateByParentAddressStateId().getName());
                        permanentAddress.append(' ');
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getParentAddressStateOthers() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getParentAddressStateOthers());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getCountryByPermanentAddressCountryId() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getCountryByPermanentAddressCountryId().getName());
                        permanentAddress.append(' ');
                    }
                    else if (applicantDetails.getAdmAppln().getPersonalData().getPermanentAddressCountryOthers() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getPermanentAddressCountryOthers());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getPersonalData().getPermanentAddressZipCode() != null) {
                        permanentAddress.append(applicantDetails.getAdmAppln().getPersonalData().getPermanentAddressZipCode());
                        permanentAddress.append(' ');
                    }
                    if (applicantDetails.getAdmAppln().getSeatNo() != null) {
                        seatNo = applicantDetails.getAdmAppln().getSeatNo();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getCenter() != null) {
                        examCenter = applicantDetails.getAdmAppln().getExamCenter().getCenter();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getAddress1() != null) {
                        examCenterAddress1 = applicantDetails.getAdmAppln().getExamCenter().getAddress1();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getAddress2() != null) {
                        examCenterAddress2 = applicantDetails.getAdmAppln().getExamCenter().getAddress2();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getAddress3() != null) {
                        examCenterAddress3 = applicantDetails.getAdmAppln().getExamCenter().getAddress3();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getAddress4() != null) {
                        examCenterAddress4 = applicantDetails.getAdmAppln().getExamCenter().getAddress4();
                    }
                    if (applicantDetails.getAdmAppln().getExamCenter() != null && applicantDetails.getAdmAppln().getExamCenter().getSeatNoPrefix() != null) {
                        seatNoPrefix = applicantDetails.getAdmAppln().getExamCenter().getSeatNoPrefix();
                    }
                    if (applicantDetails.getAdmAppln().getMode() != null) {
                        mode = applicantDetails.getAdmAppln().getMode();
                    }
                }
                final Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
                if (organisation != null) {
                    logo = organisation.getLogo();
                }
                if (session != null) {
                    session.setAttribute("LogoBytes", (Object)logo);
                }
                logoPath = request.getContextPath();
                logoPath = "<img src=" + logoPath + "/LogoServlet alt='Logo not available' width='210' height='100' >";
                if (applicantDetails.getInterview() != null) {
                    interviewTime = applicantDetails.getTime();
                    if (applicantDetails.getInterview().getDate() != null) {
                        interviewDate = CommonUtil.getStringDate(applicantDetails.getInterview().getDate());
                        interviewVenue = applicantDetails.getInterview().getVenue();
                    }
                    if (applicantDetails.getInterview().getInterview() != null && applicantDetails.getInterview().getInterview().getInterviewProgramCourse() != null) {
                        interviewType = applicantDetails.getInterview().getInterview().getInterviewProgramCourse().getName();
                    }
                }
            }
            finalTemplate = templateDescription;
            barCodeByte = new byte[(int)barCodeImgFile.length()];
            final FileInputStream fis = new FileInputStream(barCodeImgFile);
            fis.read(barCodeByte);
            session.setAttribute("barCodeBytes", (Object)barCodeByte);
            barCode = request.getContextPath();
            barCode = "<img src=" + barCode + "/BarCodeServlet alt='Barcode  not available' width='137' height='37' >";
            if ("[BARCODE]" != null && !"[BARCODE]".isEmpty()) {
                finalTemplate = finalTemplate.replace("[BARCODE]", barCode);
            }
            finalTemplate = finalTemplate.replace("[APPLICANTNAME]", applicantName);
            finalTemplate = finalTemplate.replace("[DOB]", dateOfBirth);
            finalTemplate = finalTemplate.replace("[POB]", placeOfBirth);
            finalTemplate = finalTemplate.replace("[NATIONALITY]", nationality);
            finalTemplate = finalTemplate.replace("[SUB-RELIGION]", subreligion);
            finalTemplate = finalTemplate.replace("[RESIDENTCATEGORY]", residentCategory);
            finalTemplate = finalTemplate.replace("[RELIGION]", religion);
            finalTemplate = finalTemplate.replace("[GENDER]", gender);
            finalTemplate = finalTemplate.replace("[EMAIL]", email);
            finalTemplate = finalTemplate.replace("[CASTE]", category);
            finalTemplate = finalTemplate.replace("[INTERVIEWTYPE]", interviewType);
            finalTemplate = finalTemplate.replace("[INTERVIEWDATE]", interviewDate);
            finalTemplate = finalTemplate.replace("[INTERVIEWTIME]", interviewTime);
            finalTemplate = finalTemplate.replace("[PHOTO]", contextPath);
            finalTemplate = finalTemplate.replace("[LOGO]", logoPath);
            finalTemplate = finalTemplate.replace("[PROGRAM]", program);
            finalTemplate = finalTemplate.replace("[COURSE]", course);
            finalTemplate = finalTemplate.replace("[SELECTEDCOURSE]", selectedCourse);
            finalTemplate = finalTemplate.replace("[APPLICATIONNO]", applicationNo);
            finalTemplate = finalTemplate.replace("[ACADEMICYEAR]", academicYear);
            finalTemplate = finalTemplate.replace("[VENUE]", interviewVenue);
            finalTemplate = finalTemplate.replace("[STUDENT_CURRENT_ADDRESS]", currentAddress);
            finalTemplate = finalTemplate.replace("[STUDENT_PERMANENT_ADDRESS]", permanentAddress);
            if (seatNo != null && !seatNo.trim().isEmpty()) {
                finalTemplate = finalTemplate.replace("[EXAM_CENTER_SEAT_NO]", String.format("%04d", Integer.parseInt(seatNo)));
            }
            finalTemplate = finalTemplate.replace("[EXAM_CENTER_NAME]", examCenter);
            finalTemplate = finalTemplate.replace("[ADDRESS]", String.valueOf(examCenterAddress1) + "<br/>" + examCenterAddress2 + "<br/>" + examCenterAddress3 + "<br/>" + examCenterAddress4);
            finalTemplate = finalTemplate.replace("[SEAT_NO_PREFIX]", seatNoPrefix);
            finalTemplate = finalTemplate.replace(CMSConstants.TEMPLATE_MODE, mode);
            finalTemplate = finalTemplate.replace("[INTERVIEW_CARD_CREATED_DATE]", CommonUtil.formatDates(applicantDetails.getLastModifiedDate()));
            finalTemplate = finalTemplate.replace("[INTERVIEW_CARD_CREATED_TIME]", CommonUtil.getTimeByDate(applicantDetails.getLastModifiedDate()));
            int count = 0;
            if (admitCardHistoryList != null && !admitCardHistoryList.isEmpty()) {
                String s = "<div align='left'><b>Previous E-Admit Card Details</b></div><div>&nbsp;&nbsp;&nbsp; Call History &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Admit Card generated Date & Time &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Previous Selection Process Date & Time</div><br> ";
                final Iterator<InterviewCardHistory> iterator = admitCardHistoryList.iterator();
                int callHistory = admitCardHistoryList.size();
                String admitCardGeneratedOn = "";
                String previousSelection = "";
                String admitCardGenerationOnTime = "";
                String previousSelectionTime = "";
                while (iterator.hasNext()) {
                    final InterviewCardHistory interviewCardHistory = iterator.next();
                    admitCardGeneratedOn = CommonUtil.formatDates(interviewCardHistory.getLastModifiedDate());
                    previousSelection = CommonUtil.formatDates(interviewCardHistory.getInterview().getDate());
                    admitCardGenerationOnTime = CommonUtil.getTimeByDate(interviewCardHistory.getLastModifiedDate());
                    previousSelectionTime = interviewCardHistory.getTime();
                    s = String.valueOf(s) + "<div>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + callHistory + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + admitCardGeneratedOn + "&nbsp;&nbsp;-&nbsp;&nbsp;" + admitCardGenerationOnTime + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + previousSelection + "&nbsp;&nbsp;-&nbsp;&nbsp;" + previousSelectionTime + "</div><br>";
                    ++count;
                    --callHistory;
                }
                finalTemplate = finalTemplate.replace("[INTERVIEW_SCHEDULED_HISTORY]", s);
            }
            else {
                finalTemplate = finalTemplate.replace("[INTERVIEW_SCHEDULED_HISTORY]", "");
            }
            ++count;
            finalTemplate = finalTemplate.replace("[INTERVIEW_SCHEDULED_COUNT]", String.valueOf(count));
        }
        AdmissionStatusHelper.log.info((Object)"End of generateAdmitCard of AdmissionStatusHelper");
        return finalTemplate;
    }
    
    public AdmissionStatusTO getInterviewStatus(final AdmAppln admAppln) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Begin of getInterviewStatus of AdmissionStatusHelper");
        final AdmissionStatusTO statusTO = new AdmissionStatusTO();
        final Set<InterviewSelected> interviewSelectedSet = (Set<InterviewSelected>)admAppln.getInterviewSelecteds();
        final Set<InterviewResult> interviewresultSet = (Set<InterviewResult>)admAppln.getInterviewResults();
        boolean subroundexist = false;
        boolean cardgenerated = false;
        InterviewSelected oldInterviewSelected = null;
        String status = "";
        if (interviewSelectedSet != null && !interviewSelectedSet.isEmpty() && interviewSelectedSet.size() > 1) {
            final List<InterviewSelected> interviewSelectedList = new ArrayList<InterviewSelected>();
            interviewSelectedList.addAll(interviewSelectedSet);
            Collections.sort(interviewSelectedList);
            final InterviewSelected lastMainRoundRecord = interviewSelectedList.get(interviewSelectedSet.size() - 1);
            int lastMainRoundBOId = 0;
            int lastMainRoundId = 0;
            if (lastMainRoundRecord != null) {
                lastMainRoundBOId = lastMainRoundRecord.getId();
                if (lastMainRoundRecord.getInterviewProgramCourse() != null) {
                    lastMainRoundId = lastMainRoundRecord.getInterviewProgramCourse().getId();
                }
            }
            for (final InterviewSelected interviewSelected : interviewSelectedList) {
                if (interviewSelected.getInterviewProgramCourse() != null) {
                    final int currentMainRoundId = interviewSelected.getId();
                    if (currentMainRoundId == lastMainRoundBOId) {
                        for (final InterviewResult interviewResult : interviewresultSet) {
                            if (interviewResult.getInterviewProgramCourse() != null && interviewResult.getInterviewProgramCourse().getId() == lastMainRoundId) {
                                subroundexist = true;
                                status = "Results under process";
                                if (interviewResult.getAdmAppln() != null) {
                                    if (interviewResult.getAdmAppln().getApplnNo() != 0) {
                                        statusTO.setApplicationNo(interviewResult.getAdmAppln().getApplnNo());
                                    }
                                    if (interviewResult.getAdmAppln().getAppliedYear() != null && interviewResult.getAdmAppln().getAppliedYear() != 0) {
                                        statusTO.setAppliedYear((int)interviewResult.getAdmAppln().getAppliedYear());
                                    }
                                    if (interviewResult.getAdmAppln().getPersonalData() != null) {
                                        if (interviewResult.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                                            statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(interviewResult.getAdmAppln().getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                        }
                                        if (interviewResult.getAdmAppln().getPersonalData().getEmail() != null) {
                                            statusTO.setEmail(interviewResult.getAdmAppln().getPersonalData().getEmail());
                                        }
                                    }
                                    if (interviewResult.getAdmAppln().getCourseBySelectedCourseId() != null) {
                                        statusTO.setCourseId(interviewResult.getAdmAppln().getCourseBySelectedCourseId().getId());
                                    }
                                }
                                if (interviewResult.getInterviewProgramCourse() != null) {
                                    statusTO.setInterviewProgramCourseId((int)interviewResult.getInterviewProgramCourse().getId());
                                    if (interviewResult.getInterviewProgramCourse().getName() != null) {
                                        statusTO.setInterviewStatus(String.valueOf(interviewResult.getInterviewProgramCourse().getName()) + " " + status);
                                    }
                                }
                                return statusTO;
                            }
                        }
                        if (!subroundexist) {
                            cardgenerated = interviewSelected.getIsCardGenerated();
                            if (cardgenerated) {
                                status = "You are invited for ";
                                if (interviewSelected.getAdmAppln() != null) {
                                    if (interviewSelected.getAdmAppln().getApplnNo() != 0) {
                                        statusTO.setApplicationNo(interviewSelected.getAdmAppln().getApplnNo());
                                    }
                                    if (interviewSelected.getAdmAppln().getAppliedYear() != null && interviewSelected.getAdmAppln().getAppliedYear() != 0) {
                                        statusTO.setAppliedYear((int)interviewSelected.getAdmAppln().getAppliedYear());
                                    }
                                    if (interviewSelected.getAdmAppln().getPersonalData() != null) {
                                        if (interviewSelected.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                                            statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(interviewSelected.getAdmAppln().getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                        }
                                        if (interviewSelected.getAdmAppln().getPersonalData().getEmail() != null) {
                                            statusTO.setEmail(interviewSelected.getAdmAppln().getPersonalData().getEmail());
                                        }
                                    }
                                    if (interviewSelected.getAdmAppln().getCourseBySelectedCourseId() != null) {
                                        statusTO.setCourseId(interviewSelected.getAdmAppln().getCourseBySelectedCourseId().getId());
                                    }
                                }
                                if (interviewSelected.getInterviewProgramCourse() != null) {
                                    statusTO.setInterviewProgramCourseId((int)interviewSelected.getInterviewProgramCourse().getId());
                                    if (interviewSelected.getInterviewProgramCourse().getName() != null) {
                                        statusTO.setInterviewStatus(String.valueOf(status) + interviewSelected.getInterviewProgramCourse().getName());
                                    }
                                }
                                statusTO.setIsInterviewSelected("interview");
                                return statusTO;
                            }
                            if (!cardgenerated) {
                                status = "Results under process";
                                if (oldInterviewSelected.getAdmAppln() != null) {
                                    if (oldInterviewSelected.getAdmAppln().getApplnNo() != 0) {
                                        statusTO.setApplicationNo(oldInterviewSelected.getAdmAppln().getApplnNo());
                                    }
                                    if (oldInterviewSelected.getAdmAppln().getAppliedYear() != null && oldInterviewSelected.getAdmAppln().getAppliedYear() != 0) {
                                        statusTO.setAppliedYear((int)oldInterviewSelected.getAdmAppln().getAppliedYear());
                                    }
                                    if (oldInterviewSelected.getAdmAppln().getPersonalData() != null) {
                                        if (oldInterviewSelected.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                                            statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(oldInterviewSelected.getAdmAppln().getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                        }
                                        if (oldInterviewSelected.getAdmAppln().getPersonalData().getEmail() != null) {
                                            statusTO.setEmail(oldInterviewSelected.getAdmAppln().getPersonalData().getEmail());
                                        }
                                    }
                                    if (oldInterviewSelected.getAdmAppln().getCourseBySelectedCourseId() != null) {
                                        statusTO.setCourseId(oldInterviewSelected.getAdmAppln().getCourseBySelectedCourseId().getId());
                                    }
                                }
                                if (oldInterviewSelected.getInterviewProgramCourse() != null) {
                                    statusTO.setInterviewProgramCourseId((int)oldInterviewSelected.getInterviewProgramCourse().getId());
                                    if (oldInterviewSelected.getInterviewProgramCourse().getName() != null) {
                                        statusTO.setInterviewStatus(String.valueOf(oldInterviewSelected.getInterviewProgramCourse().getName()) + " " + status);
                                    }
                                }
                                return statusTO;
                            }
                        }
                    }
                }
                oldInterviewSelected = interviewSelected;
            }
        }
        else {
            if (interviewSelectedSet == null || interviewSelectedSet.isEmpty()) {
                status = "Application Submitted Online - Send the Application and Supporting Documents to Office of Admissions";
                if (admAppln.getApplnNo() != 0) {
                    statusTO.setApplicationNo(admAppln.getApplnNo());
                }
                if (admAppln.getAppliedYear() != null) {
                    statusTO.setAppliedYear((int)admAppln.getAppliedYear());
                }
                if (admAppln.getPersonalData() != null) {
                    if (admAppln.getPersonalData().getDateOfBirth() != null) {
                        statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(admAppln.getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                    }
                    if (admAppln.getPersonalData().getEmail() != null) {
                        statusTO.setEmail(admAppln.getPersonalData().getEmail());
                    }
                }
                statusTO.setInterviewStatus(status);
                statusTO.setIsInterviewSelected("viewapplication");
                return statusTO;
            }
            if (interviewSelectedSet != null && !interviewSelectedSet.isEmpty() && interviewSelectedSet.size() == 1) {
                for (final InterviewSelected interviewSelected2 : interviewSelectedSet) {
                    if (interviewSelected2.getInterviewProgramCourse() != null) {
                        final int selectProgcrsID = interviewSelected2.getInterviewProgramCourse().getId();
                        final String selectProgcrsSequence = interviewSelected2.getInterviewProgramCourse().getSequence();
                        for (final InterviewResult interviewResult2 : interviewresultSet) {
                            if (interviewResult2.getInterviewProgramCourse() != null && (interviewResult2.getInterviewProgramCourse().getId() == selectProgcrsID || interviewResult2.getInterviewProgramCourse().getSequence().equalsIgnoreCase(selectProgcrsSequence))) {
                                subroundexist = true;
                                status = "Results under process";
                                if (interviewResult2.getAdmAppln() != null) {
                                    if (interviewResult2.getAdmAppln().getApplnNo() != 0) {
                                        statusTO.setApplicationNo(interviewResult2.getAdmAppln().getApplnNo());
                                    }
                                    if (interviewResult2.getAdmAppln().getAppliedYear() != null && interviewResult2.getAdmAppln().getAppliedYear() != 0) {
                                        statusTO.setAppliedYear((int)interviewResult2.getAdmAppln().getAppliedYear());
                                    }
                                    if (interviewResult2.getAdmAppln().getPersonalData() != null) {
                                        if (interviewResult2.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                                            statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(interviewResult2.getAdmAppln().getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                        }
                                        if (interviewResult2.getAdmAppln().getPersonalData().getEmail() != null) {
                                            statusTO.setEmail(interviewResult2.getAdmAppln().getPersonalData().getEmail());
                                        }
                                    }
                                    if (interviewResult2.getAdmAppln().getCourseBySelectedCourseId() != null) {
                                        statusTO.setCourseId(interviewResult2.getAdmAppln().getCourseBySelectedCourseId().getId());
                                    }
                                }
                                if (interviewResult2.getInterviewProgramCourse() != null) {
                                    statusTO.setInterviewProgramCourseId((int)interviewResult2.getInterviewProgramCourse().getId());
                                    if (interviewResult2.getInterviewProgramCourse().getName() != null) {
                                        statusTO.setInterviewStatus(String.valueOf(interviewResult2.getInterviewProgramCourse().getName()) + " " + status);
                                    }
                                }
                                return statusTO;
                            }
                        }
                        if (subroundexist) {
                            continue;
                        }
                        cardgenerated = interviewSelected2.getIsCardGenerated();
                        if (cardgenerated) {
                            status = "You are invited for ";
                            if (interviewSelected2.getAdmAppln() != null) {
                                if (interviewSelected2.getAdmAppln().getApplnNo() != 0) {
                                    statusTO.setApplicationNo(interviewSelected2.getAdmAppln().getApplnNo());
                                }
                                if (interviewSelected2.getAdmAppln().getAppliedYear() != null && interviewSelected2.getAdmAppln().getAppliedYear() != 0) {
                                    statusTO.setAppliedYear((int)interviewSelected2.getAdmAppln().getAppliedYear());
                                }
                                if (interviewSelected2.getAdmAppln().getPersonalData() != null) {
                                    if (interviewSelected2.getAdmAppln().getPersonalData().getDateOfBirth() != null) {
                                        statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(interviewSelected2.getAdmAppln().getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                    }
                                    if (interviewSelected2.getAdmAppln().getPersonalData().getEmail() != null) {
                                        statusTO.setEmail(interviewSelected2.getAdmAppln().getPersonalData().getEmail());
                                    }
                                }
                                if (interviewSelected2.getAdmAppln().getCourseBySelectedCourseId() != null) {
                                    statusTO.setCourseId(interviewSelected2.getAdmAppln().getCourseBySelectedCourseId().getId());
                                }
                            }
                            if (interviewSelected2.getInterviewProgramCourse() != null) {
                                statusTO.setInterviewProgramCourseId((int)interviewSelected2.getInterviewProgramCourse().getId());
                                if (interviewSelected2.getInterviewProgramCourse().getName() != null) {
                                    if (interviewSelected2.getAdmAppln().getSeatNo() != null && !interviewSelected2.getAdmAppln().getSeatNo().isEmpty() && interviewSelected2.getAdmAppln().getExamCenter() != null && interviewSelected2.getAdmAppln().getExamCenter().getSeatNoPrefix() != null) {
                                        statusTO.setSeatNo(String.valueOf(interviewSelected2.getAdmAppln().getExamCenter().getSeatNoPrefix()) + String.format("%04d", Integer.parseInt(interviewSelected2.getAdmAppln().getSeatNo())));
                                    }
                                    statusTO.setInterviewStatus(String.valueOf(status) + interviewSelected2.getInterviewProgramCourse().getName());
                                }
                            }
                            statusTO.setIsInterviewSelected("interview");
                            return statusTO;
                        }
                        if (!cardgenerated) {
                            status = "Application Submitted Online - Send the Application and Supporting Documents to Office of Admissions";
                            if (admAppln.getApplnNo() != 0) {
                                statusTO.setApplicationNo(admAppln.getApplnNo());
                            }
                            if (admAppln.getAppliedYear() != null) {
                                statusTO.setAppliedYear((int)admAppln.getAppliedYear());
                            }
                            if (admAppln.getPersonalData() != null) {
                                if (admAppln.getPersonalData().getDateOfBirth() != null) {
                                    statusTO.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(CommonUtil.getStringDate(admAppln.getPersonalData().getDateOfBirth()), "dd-MMM-yyyy", "dd-MMM-yyyy"));
                                }
                                if (admAppln.getPersonalData().getEmail() != null) {
                                    statusTO.setEmail(admAppln.getPersonalData().getEmail());
                                }
                            }
                            statusTO.setInterviewStatus(status);
                            statusTO.setIsInterviewSelected("viewapplication");
                            return statusTO;
                        }
                        continue;
                    }
                }
            }
        }
        AdmissionStatusHelper.log.info((Object)"End of getInterviewStatus of AdmissionStatusHelper");
        return statusTO;
    }
    
    public static String generateAdmissionCard(final String templateDescription, final AdmAppln applicantDetails, final HttpServletRequest request, final File barCodeImgFile) throws Exception {
        AdmissionStatusHelper.log.info((Object)"Inside of generateAdmitCard of AdmissionStatusHelper");
        String program = "";
        String course = "";
        String selectedCourse = "";
        String applicationNo = "";
        final String interviewVenue = "";
        String academicYear = "";
        String applicantName = "";
        String dateOfBirth = "";
        String placeOfBirth = "";
        String nationality = "";
        String religion = "";
        String subreligion = "";
        String residentCategory = "";
        String category = "";
        String gender = "";
        String email = "";
        String finalTemplate = "";
        String contextPath = "";
        String logoPath = "";
        String seatNo = "";
        String examCenter = "";
        final StringBuffer currentAddress = new StringBuffer();
        final StringBuffer permanentAddress = new StringBuffer();
        String examCenterAddress1 = "";
        String examCenterAddress2 = "";
        String examCenterAddress3 = "";
        String examCenterAddress4 = "";
        String seatNoPrefix = "";
        byte[] logo = null;
        String approvedate = "";
        String mode = "";
        String admissionScheduledDate = "";
        String admissionScheduledTime = "";
        String specializationPrefered = "";
        String commencementDate = "";
        String barCode = "";
        byte[] barCodeByte = null;
        final HttpSession session = request.getSession(false);
        if (templateDescription != null && !templateDescription.trim().isEmpty() && applicantDetails != null) {
            if (applicantDetails.getPersonalData() != null) {
                if (applicantDetails.getFinalMeritListApproveDate() != null) {
                    approvedate = CommonUtil.getStringDate(applicantDetails.getFinalMeritListApproveDate());
                }
                if (applicantDetails.getPersonalData().getDateOfBirth() != null) {
                    dateOfBirth = CommonUtil.getStringDate(applicantDetails.getPersonalData().getDateOfBirth());
                }
                if (applicantDetails.getPersonalData().getBirthPlace() != null) {
                    placeOfBirth = applicantDetails.getPersonalData().getBirthPlace();
                }
                if (applicantDetails.getPersonalData().getFirstName() != null && !applicantDetails.getPersonalData().getFirstName().trim().isEmpty()) {
                    applicantName = applicantDetails.getPersonalData().getFirstName();
                }
                if (applicantDetails.getPersonalData().getMiddleName() != null && !applicantDetails.getPersonalData().getMiddleName().trim().isEmpty()) {
                    applicantName = String.valueOf(applicantName) + " " + applicantDetails.getPersonalData().getMiddleName();
                }
                if (applicantDetails.getPersonalData().getLastName() != null && !applicantDetails.getPersonalData().getLastName().trim().isEmpty()) {
                    applicantName = String.valueOf(applicantName) + " " + applicantDetails.getPersonalData().getLastName();
                }
                if (applicantDetails.getPersonalData().getNationalityOthers() != null) {
                    nationality = applicantDetails.getPersonalData().getNationalityOthers();
                }
                else if (applicantDetails.getPersonalData().getNationality() != null) {
                    nationality = applicantDetails.getPersonalData().getNationality().getName();
                }
                if (applicantDetails.getPersonalData().getReligionOthers() != null) {
                    religion = applicantDetails.getPersonalData().getReligionOthers();
                }
                else if (applicantDetails.getPersonalData().getReligion() != null) {
                    religion = applicantDetails.getPersonalData().getReligion().getName();
                }
                if (applicantDetails.getPersonalData().getReligionSectionOthers() != null) {
                    subreligion = applicantDetails.getPersonalData().getReligionSectionOthers();
                }
                else if (applicantDetails.getPersonalData().getReligionSection() != null) {
                    subreligion = applicantDetails.getPersonalData().getReligionSection().getName();
                }
                if (applicantDetails.getPersonalData().getResidentCategory() != null && applicantDetails.getPersonalData().getResidentCategory().getName() != null) {
                    residentCategory = applicantDetails.getPersonalData().getResidentCategory().getName();
                }
                if (applicantDetails.getPersonalData().getGender() != null) {
                    gender = applicantDetails.getPersonalData().getGender();
                }
                if (applicantDetails.getPersonalData().getEmail() != null) {
                    email = applicantDetails.getPersonalData().getEmail();
                }
                if (applicantDetails.getPersonalData().getCasteOthers() != null) {
                    category = applicantDetails.getPersonalData().getCasteOthers();
                }
                else if (applicantDetails.getPersonalData().getCaste() != null) {
                    category = applicantDetails.getPersonalData().getCaste().getName();
                }
                if (applicantDetails.getCourseBySelectedCourseId() != null) {
                    course = applicantDetails.getCourseBySelectedCourseId().getName();
                }
                if (applicantDetails.getCourseBySelectedCourseId() != null) {
                    selectedCourse = applicantDetails.getCourseBySelectedCourseId().getName();
                }
                if (applicantDetails.getCourseBySelectedCourseId() != null && applicantDetails.getCourseBySelectedCourseId().getProgram() != null) {
                    program = applicantDetails.getCourseBySelectedCourseId().getProgram().getName();
                }
                applicationNo = String.valueOf(applicantDetails.getApplnNo());
                academicYear = String.valueOf(applicantDetails.getAppliedYear());
                if (applicantDetails.getStudents() != null) {
                    for (final Student student : applicantDetails.getStudents()) {
                        contextPath = "<img src='images/StudentPhotos/" + student.getId() + ".jpg?" + applicantDetails.getLastModifiedDate() + "' alt='Photo not available' width='150' height='150' >";
                    }
                }
                else {
                    contextPath = "<img src='images/photoblank.gif' width='150' height='150' >";
                }
                if (applicantDetails.getPersonalData().getCurrentAddressLine1() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCurrentAddressLine1());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCurrentAddressLine2() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCurrentAddressLine2());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCityByCurrentAddressCityId() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCityByCurrentAddressCityId());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getStateByCurrentAddressStateId() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getStateByCurrentAddressStateId().getName());
                    currentAddress.append(' ');
                }
                else if (applicantDetails.getPersonalData().getCurrentAddressStateOthers() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCurrentAddressStateOthers());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCountryByCurrentAddressCountryId() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCountryByCurrentAddressCountryId().getName());
                    currentAddress.append(' ');
                }
                else if (applicantDetails.getPersonalData().getCurrentAddressCountryOthers() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCurrentAddressCountryOthers());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCurrentAddressZipCode() != null) {
                    currentAddress.append(applicantDetails.getPersonalData().getCurrentAddressZipCode());
                    currentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getParentAddressLine1() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getParentAddressLine1());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getParentAddressLine2() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getParentAddressLine2());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCityByPermanentAddressCityId() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getCityByPermanentAddressCityId());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getStateByParentAddressStateId() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getStateByParentAddressStateId().getName());
                    permanentAddress.append(' ');
                }
                else if (applicantDetails.getPersonalData().getParentAddressStateOthers() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getParentAddressStateOthers());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getCountryByPermanentAddressCountryId() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getCountryByPermanentAddressCountryId().getName());
                    permanentAddress.append(' ');
                }
                else if (applicantDetails.getPersonalData().getPermanentAddressCountryOthers() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getPermanentAddressCountryOthers());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getPersonalData().getPermanentAddressZipCode() != null) {
                    permanentAddress.append(applicantDetails.getPersonalData().getPermanentAddressZipCode());
                    permanentAddress.append(' ');
                }
                if (applicantDetails.getSeatNo() != null) {
                    seatNo = applicantDetails.getSeatNo();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getCenter() != null) {
                    examCenter = applicantDetails.getExamCenter().getCenter();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getAddress1() != null) {
                    examCenterAddress1 = applicantDetails.getExamCenter().getAddress1();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getAddress2() != null) {
                    examCenterAddress2 = applicantDetails.getExamCenter().getAddress2();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getAddress3() != null) {
                    examCenterAddress3 = applicantDetails.getExamCenter().getAddress3();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getAddress4() != null) {
                    examCenterAddress4 = applicantDetails.getExamCenter().getAddress4();
                }
                if (applicantDetails.getExamCenter() != null && applicantDetails.getExamCenter().getSeatNoPrefix() != null) {
                    seatNoPrefix = applicantDetails.getExamCenter().getSeatNoPrefix();
                }
                if (applicantDetails.getMode() != null) {
                    mode = applicantDetails.getMode();
                }
                if (applicantDetails.getStudentSpecializationPrefered() != null && !applicantDetails.getStudentSpecializationPrefered().isEmpty()) {
                    final Set<StudentSpecializationPrefered> stuSpecialization = (Set<StudentSpecializationPrefered>)applicantDetails.getStudentSpecializationPrefered();
                    for (final StudentSpecializationPrefered studentSpecializationPrefered : stuSpecialization) {
                        if (studentSpecializationPrefered.getSpecializationPrefered() != null) {
                            specializationPrefered = studentSpecializationPrefered.getSpecializationPrefered();
                        }
                    }
                }
                if (applicantDetails.getCourseBySelectedCourseId() != null && applicantDetails.getCourseBySelectedCourseId().getCommencementDate() != null) {
                    commencementDate = CommonUtil.getStringDate(applicantDetails.getCourseBySelectedCourseId().getCommencementDate());
                }
            }
            final Organisation organisation = OrganizationHandler.getInstance().getRequiredFile();
            if (organisation != null) {
                logo = organisation.getLogo();
            }
            if (session != null) {
                session.setAttribute("LogoBytes", (Object)logo);
            }
            logoPath = request.getContextPath();
            logoPath = "<img src=" + logoPath + "/LogoServlet alt='Logo not available' width='210' height='100' >";
            if (applicantDetails.getAdmapplnAdditionalInfo() != null && !applicantDetails.getAdmapplnAdditionalInfo().isEmpty()) {
                for (final AdmapplnAdditionalInfo admapplnAdditionalInfo : applicantDetails.getAdmapplnAdditionalInfo()) {
                    if (admapplnAdditionalInfo.getAdmissionScheduledDate() != null) {
                        admissionScheduledDate = CommonUtil.getStringDate(admapplnAdditionalInfo.getAdmissionScheduledDate());
                    }
                    if (admapplnAdditionalInfo.getAdmissionScheduledTime() != null) {
                        admissionScheduledTime = admapplnAdditionalInfo.getAdmissionScheduledTime();
                    }
                }
            }
            finalTemplate = templateDescription;
            barCodeByte = new byte[(int)barCodeImgFile.length()];
            final FileInputStream fis = new FileInputStream(barCodeImgFile);
            fis.read(barCodeByte);
            session.setAttribute("barCodeBytes", (Object)barCodeByte);
            barCode = request.getContextPath();
            barCode = "<img src=" + barCode + "/BarCodeServlet alt='Barcode  not available' width='137' height='37' >";
            finalTemplate = finalTemplate.replace("[BARCODE]", barCode);
            finalTemplate = finalTemplate.replace("[APPLICANTNAME]", applicantName);
            finalTemplate = finalTemplate.replace("[DOB]", dateOfBirth);
            finalTemplate = finalTemplate.replace("[POB]", placeOfBirth);
            finalTemplate = finalTemplate.replace("[NATIONALITY]", nationality);
            finalTemplate = finalTemplate.replace("[SUB-RELIGION]", subreligion);
            finalTemplate = finalTemplate.replace("[RESIDENTCATEGORY]", residentCategory);
            finalTemplate = finalTemplate.replace("[RELIGION]", religion);
            finalTemplate = finalTemplate.replace("[GENDER]", gender);
            finalTemplate = finalTemplate.replace("[EMAIL]", email);
            finalTemplate = finalTemplate.replace("[CASTE]", category);
            finalTemplate = finalTemplate.replace("[PHOTO]", contextPath);
            finalTemplate = finalTemplate.replace("[LOGO]", logoPath);
            finalTemplate = finalTemplate.replace("[PROGRAM]", program);
            finalTemplate = finalTemplate.replace("[COURSE]", course);
            finalTemplate = finalTemplate.replace("[SELECTEDCOURSE]", selectedCourse);
            finalTemplate = finalTemplate.replace("[APPLICATIONNO]", applicationNo);
            finalTemplate = finalTemplate.replace("[ACADEMICYEAR]", academicYear);
            finalTemplate = finalTemplate.replace("[VENUE]", interviewVenue);
            finalTemplate = finalTemplate.replace("[STUDENT_CURRENT_ADDRESS]", currentAddress);
            finalTemplate = finalTemplate.replace("[STUDENT_PERMANENT_ADDRESS]", permanentAddress);
            if (seatNo != null && !seatNo.trim().isEmpty()) {
                finalTemplate = finalTemplate.replace("[EXAM_CENTER_SEAT_NO]", String.format("%04d", Integer.parseInt(seatNo)));
            }
            finalTemplate = finalTemplate.replace("[EXAM_CENTER_NAME]", examCenter);
            finalTemplate = finalTemplate.replace("[ADDRESS]", String.valueOf(examCenterAddress1) + "<br/>" + examCenterAddress2 + "<br/>" + examCenterAddress3 + "<br/>" + examCenterAddress4);
            finalTemplate = finalTemplate.replace("[SEAT_NO_PREFIX]", seatNoPrefix);
            finalTemplate = finalTemplate.replace("[approved_date]", approvedate);
            finalTemplate = finalTemplate.replace(CMSConstants.TEMPLATE_MODE, mode);
            finalTemplate = finalTemplate.replace("[ADMISSION_SCHEDULED_DATE]", admissionScheduledDate);
            finalTemplate = finalTemplate.replace("[ADMISSION_SCHEDULED_TIME]", admissionScheduledTime);
            finalTemplate = finalTemplate.replace("[STUDENT_SPECIALIZATION_PREFERED]", specializationPrefered);
            finalTemplate = finalTemplate.replace("[COURSE_COMMENCEMENT_DATE]", commencementDate);
        }
        AdmissionStatusHelper.log.info((Object)"End of generateAdmitCard of AdmissionStatusHelper");
        return finalTemplate;
    }
    
    public List<AdmissionStatusTO> setToList(final List<StudentCourseChanceMemo> allotments, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final List<AdmissionStatusTO> statusTOs = new ArrayList<AdmissionStatusTO>();
        final IAdmissionStatusTransaction trx = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<PublishForAllotment> publishForAllotments = (List<PublishForAllotment>)trx.getPublishAllotment();
        final Map<Integer, Integer> publishMap = new HashMap<Integer, Integer>();
        if (publishForAllotments != null) {
            for (final PublishForAllotment bo : publishForAllotments) {
                publishMap.put(bo.getCourse().getId(), bo.getChanceNo());
            }
        }
        admissionStatusForm.setIsOnceAccept(false);
        admissionStatusForm.setIsUploadDocument(false);
        admissionStatusForm.setPayonline(false);
        int selectedCourseId = 0;
        try {
            for (final StudentCourseChanceMemo allotment : allotments) {
                final AdmissionStatusTO to = new AdmissionStatusTO();
                to.setCourseName(allotment.getCourse().getName());
                to.setAllotmentno((int)allotment.getChanceNo());
                if (allotment.getIsGeneral()) {
                    to.setCasteName("You have been provisionally Alloted- General Quota-");
                }
                else if (allotment.getIsCaste()) {
                    to.setCasteName("You have been provisionally Alloted-SC/ST Quota-");
                }
                else {
                    to.setCasteName("You have been provisionally Alloted-Malankara Syrian catholic");
                }
                admissionStatusForm.setAdmApplnId(String.valueOf(allotment.getAdmAppln().getId()));
                to.setCourseId(allotment.getCourse().getId());
                if (allotment.getIsAccept() && allotment.getIsGeneral()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under Genearal Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                else if (allotment.getIsAccept() && allotment.getIsCaste()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under SC/ST Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                else if (allotment.getIsAccept() && allotment.getIsCommunity()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under Malankara Syrian Catholic Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                if (allotment.getIsDecline() && allotment.getIsGeneral()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under Genearal Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment.getIsDecline() && allotment.getIsCaste()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under SC/ST Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment.getIsDecline() && allotment.getIsCommunity()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under Malankara Syrian Catholic is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                if (allotment.getIsAccept()) {
                    if (allotment.getPayonline()) {
                        admissionStatusForm.setPayonline(true);
                    }
                    final IAdmissionFormTransaction tx = AdmissionFormTransactionImpl.getInstance();
                    final StudentAllotmentPGIDetails details = tx.getBoObj(allotment.getAdmAppln().getStudentOnlineApplication().getId());
                    if (details != null) {
                        admissionStatusForm.setIsPaid(true);
                    }
                }
                if (allotment.getIsUploaded()) {
                    admissionStatusForm.setIsUploadDocument(true);
                    if (allotment.getPayonline()) {
                        admissionStatusForm.setPayonline(true);
                    }
                    if (allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 3 || allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 4) {
                        if (allotment.getCourse().getIsSelfFinancing()) {
                            if (allotment.getIsGeneral()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                            }
                            else if (allotment.getIsCaste()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getCasteFee());
                            }
                            else if (allotment.getIsCommunity()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                            }
                        }
                        else {
                            admissionStatusForm.setApplicationAmount(allotment.getCourse().getCasteFee());
                        }
                    }
                    else {
                        admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getFirstName() != null && !allotment.getAdmAppln().getPersonalData().getFirstName().isEmpty()) {
                        admissionStatusForm.setApplicantName(allotment.getAdmAppln().getPersonalData().getFirstName());
                    }
                    if (allotment.getAdmAppln().getCourse() != null) {
                        admissionStatusForm.setCourseId(String.valueOf(allotment.getCourse().getId()));
                    }
                    if (allotment.getAdmAppln() != null) {
                        admissionStatusForm.setUniqueId(String.valueOf(allotment.getAdmAppln().getStudentOnlineApplication().getId()));
                    }
                    if (allotment.getAdmAppln().getPersonalData().getMobileNo1() != null && !allotment.getAdmAppln().getPersonalData().getMobileNo1().isEmpty() && allotment.getAdmAppln().getPersonalData().getMobileNo2() != null && !allotment.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()) {
                        admissionStatusForm.setMobile1(allotment.getAdmAppln().getPersonalData().getMobileNo1());
                        admissionStatusForm.setMobile2(allotment.getAdmAppln().getPersonalData().getMobileNo2());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getEmail() != null && !allotment.getAdmAppln().getPersonalData().getEmail().isEmpty()) {
                        admissionStatusForm.setEmail(allotment.getAdmAppln().getPersonalData().getEmail());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getResidentCategory() != null) {
                        admissionStatusForm.setResidentCategoryForOnlineAppln(String.valueOf(allotment.getAdmAppln().getPersonalData().getResidentCategory().getId()));
                    }
                }
                if (!publishMap.isEmpty() && publishMap.containsKey(to.getCourseId()) && publishMap.containsValue(to.getAllotmentno())) {
                    statusTOs.add(to);
                }
            }
            if (admissionStatusForm.getIsOnceAccept()) {
                if (selectedCourseId == 1) {
                    admissionStatusForm.setFormlink("https://forms.gle/aqj96G5zE57AewGX7");
                }
                else if (selectedCourseId == 2) {
                    admissionStatusForm.setFormlink("https://forms.gle/7AvRSvvEQkT8WPfv7");
                }
                else if (selectedCourseId == 27) {
                    admissionStatusForm.setFormlink("https://forms.gle/QqcsCnBXKMgAKty1A");
                }
                else if (selectedCourseId == 4) {
                    admissionStatusForm.setFormlink("https://forms.gle/evCC73va9mw4fLBy6");
                }
                else if (selectedCourseId == 8) {
                    admissionStatusForm.setFormlink("https://forms.gle/8qwvGmP7RarpQYs18");
                }
                else if (selectedCourseId == 5) {
                    admissionStatusForm.setFormlink("https://forms.gle/xjKYam1eMkLccjcc9");
                }
                else if (selectedCourseId == 6) {
                    admissionStatusForm.setFormlink("https://forms.gle/TAhB1Ux6CMmB2taaA");
                }
                else if (selectedCourseId == 7) {
                    admissionStatusForm.setFormlink("https://forms.gle/ktfGitTYZrXtRcgn8");
                }
                else if (selectedCourseId == 20) {
                    admissionStatusForm.setFormlink("https://forms.gle/YAcqWQ53v6L5uVgz6");
                }
                else if (selectedCourseId == 9) {
                    admissionStatusForm.setFormlink("https://forms.gle/bWF5mJGdnP3QQAQ5A");
                }
                else if (selectedCourseId == 22) {
                    admissionStatusForm.setFormlink("https://forms.gle/sr767p5DreTEgNUi9");
                }
                else if (selectedCourseId == 29) {
                    admissionStatusForm.setFormlink("https://forms.gle/688atwmugdvGSxD28");
                }
                else if (selectedCourseId == 23) {
                    admissionStatusForm.setFormlink("https://forms.gle/H31pgNwK9AaLeni76");
                }
                else if (selectedCourseId == 26) {
                    admissionStatusForm.setFormlink("https://forms.gle/ftoEqouxTbLC9yJCA");
                }
                else if (selectedCourseId == 28) {
                    admissionStatusForm.setFormlink("https://forms.gle/tsYjtF1wyUBJxAUZ7");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return statusTOs;
    }
    
    public List<StudentCourseChanceMemo> getUpdatedBo(final List<StudentCourseChanceMemo> allotments, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final List<StudentCourseChanceMemo> studentCourseAllotments = new ArrayList<StudentCourseChanceMemo>();
        final List<StudentCourseChanceMemo> courseAllotments = new ArrayList<StudentCourseChanceMemo>();
        final List<StudentCourseChanceMemo> totalAllotments = new ArrayList<StudentCourseChanceMemo>();
        final Map<Integer, Integer> publishMap = new HashMap<Integer, Integer>();
        boolean isOnceAccept = false;
        try {
            for (final StudentCourseChanceMemo allotment : allotments) {
                if (allotment.getCourse().getId() == admissionStatusForm.getSelectedCourseId()) {
                    if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                        allotment.setIsAccept(Boolean.valueOf(true));
                        isOnceAccept = true;
                    }
                    else if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("decline")) {
                        allotment.setIsDecline(Boolean.valueOf(true));
                    }
                    allotment.setModifiedBy(admissionStatusForm.getUserId());
                    allotment.setLastModifiedDate(new Date());
                }
                studentCourseAllotments.add(allotment);
            }
            if (isOnceAccept) {
                for (final StudentCourseChanceMemo allotment : studentCourseAllotments) {
                    if (allotment.getCourse().getId() != admissionStatusForm.getSelectedCourseId()) {
                        publishMap.put(allotment.getCourse().getId(), allotment.getAdmAppln().getId());
                        courseAllotments.add(allotment);
                    }
                }
                if (courseAllotments.isEmpty()) {
                    return studentCourseAllotments;
                }
                for (final StudentCourseChanceMemo s : studentCourseAllotments) {
                    if (publishMap.containsKey(s.getCourse().getId())) {
                        s.setIsDecline(Boolean.valueOf(true));
                        s.setModifiedBy(admissionStatusForm.getUserId());
                        s.setLastModifiedDate(new Date());
                        totalAllotments.add(s);
                    }
                    else {
                        totalAllotments.add(s);
                    }
                }
                return totalAllotments;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return studentCourseAllotments;
    }
    
    public StudentAllotmentPGIDetails convertToBo(final AdmissionStatusForm admForm) throws Exception {
        final StudentAllotmentPGIDetails bo = new StudentAllotmentPGIDetails();
        final StringBuilder temp = new StringBuilder();
        System.out.println("+++++++++++++++++++++++++++++++++++  this is data before hash alogoritham ++++++++++++++++++++++++++++++" + temp.toString());
        bo.setCandidateRefNo(admForm.getTxnid());
        bo.setTxnRefNo(admForm.getPayuMoneyId());
        bo.setTxnAmount(new BigDecimal(admForm.getAmount()));
        bo.setAdditionalCharges(new BigDecimal(admForm.getAdditionalCharges()));
        bo.setBankRefNo(admForm.getBank_ref_num());
        bo.setTxnStatus(admForm.getStatus());
        bo.setErrorStatus(admForm.getError1());
        bo.setTxnDate((Date)CommonUtil.ConvertStringToSQLDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy")));
        bo.setMode(admForm.getMode1());
        bo.setUnmappedStatus(admForm.getUnmappedstatus());
        bo.setMihpayId(admForm.getMihpayid());
        bo.setPgType(admForm.getPG_TYPE());
        bo.setPaymentEmail(admForm.getPaymentMail());
        admForm.setPgiStatus("Payment Successful");
        admForm.setTxnAmt(admForm.getAmount());
        admForm.setTxnRefNo(admForm.getPayuMoneyId());
        admForm.setTxnDate(CommonUtil.ConvertStringToDateFormat(CommonUtil.getDateTime(), "MM/dd/yyyy hh:mm:ss", "dd/MM/yyyy"));
        return bo;
    }
    
    public List<AdmissionStatusTO> setTOPGList(final List<StudentCourseChanceMemo> allotments, final AdmissionStatusForm admissionStatusForm, final List<StudentCourseAllotment> courseAllotments) throws Exception {
        final List<AdmissionStatusTO> statusTOs = new ArrayList<AdmissionStatusTO>();
        final List<AdmissionStatusTO> chanceTOs = new ArrayList<AdmissionStatusTO>();
        final List<AdmissionStatusTO> allotmentTOs = new ArrayList<AdmissionStatusTO>();
        final IAdmissionStatusTransaction trx = (IAdmissionStatusTransaction)new AdmissionStatusTransactionImpl();
        final List<PublishForAllotment> publishForAllotments = (List<PublishForAllotment>)trx.getPublishAllotment();
        final Map<Integer, Integer> publishMap = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> publishMapForChance = new HashMap<Integer, Integer>();
        if (publishForAllotments != null) {
            for (final PublishForAllotment bo : publishForAllotments) {
                publishMap.put(bo.getCourse().getId(), bo.getAllotmentNo());
                publishMapForChance.put(bo.getCourse().getId(), bo.getChanceNo());
            }
        }
        admissionStatusForm.setIsOnceAccept(false);
        admissionStatusForm.setIsUploadDocument(false);
        admissionStatusForm.setPayonline(false);
        int selectedCourseId = 0;
        try {
            for (final StudentCourseAllotment allotment : courseAllotments) {
                final AdmissionStatusTO to = new AdmissionStatusTO();
                to.setCourseName(allotment.getCourse().getName());
                to.setAllotmentno((int)allotment.getAllotmentNo());
                to.setChanceAllotment("Allotment");
                if (allotment.getIsGeneral()) {
                    to.setCasteName("You have been provisionally Alloted- General Quota-");
                }
                else if (allotment.getIsCaste()) {
                    to.setCasteName("You have been provisionally Alloted-SC/ST Quota-");
                }
                else {
                    to.setCasteName("You have been provisionally Alloted-Malankara Syrian catholic");
                }
                admissionStatusForm.setAdmApplnId(String.valueOf(allotment.getAdmAppln().getId()));
                to.setCourseId(allotment.getCourse().getId());
                if (allotment.getIsAccept() && allotment.getIsGeneral() && allotment.getPayonline()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under Genearal Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                else if (allotment.getIsAccept() && allotment.getIsCaste()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under SC/ST Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                else if (allotment.getIsAccept() && allotment.getIsCommunity()) {
                    to.setMsgValue(" the allotment for " + allotment.getCourse().getName() + " under Malankara Syrian Catholic Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment.getCourse().getId();
                }
                if (allotment.getIsDecline() && allotment.getIsGeneral()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under Genearal Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment.getIsDecline() && allotment.getIsCaste()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under SC/ST Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment.getIsDecline() && allotment.getIsCommunity()) {
                    to.setMsgValue("Your admission for " + allotment.getCourse().getName() + " under Malankara Syrian Catholic is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                if (allotment.getIsAccept()) {
                    final IAdmissionFormTransaction tx = AdmissionFormTransactionImpl.getInstance();
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    final StudentAllotmentPGIDetails details = tx.getBoObj(allotment.getAdmAppln().getStudentOnlineApplication().getId());
                    if (details != null) {
                        admissionStatusForm.setIsPaid(true);
                    }
                }
                if (allotment.getIsAccept() && allotment.getPayonline()) {
                    if (allotment.getPayonline()) {
                        admissionStatusForm.setPayonline(true);
                    }
                    if (allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 3 || allotment.getAdmAppln().getPersonalData().getReligionSection().getId() == 4) {
                        if (allotment.getCourse().getIsSelfFinancing()) {
                            if (allotment.getIsGeneral()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                            }
                            else if (allotment.getIsCaste()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getCasteFee());
                            }
                            else if (allotment.getIsCommunity()) {
                                admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                            }
                        }
                        else {
                            admissionStatusForm.setApplicationAmount(allotment.getCourse().getCasteFee());
                        }
                    }
                    else {
                        admissionStatusForm.setApplicationAmount(allotment.getCourse().getGeneralFee());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getFirstName() != null && !allotment.getAdmAppln().getPersonalData().getFirstName().isEmpty()) {
                        admissionStatusForm.setApplicantName(allotment.getAdmAppln().getPersonalData().getFirstName());
                    }
                    if (allotment.getAdmAppln().getCourse() != null && allotment.getPayonline()) {
                        admissionStatusForm.setCourseId(String.valueOf(allotment.getCourse().getId()));
                        admissionStatusForm.setCourseName(allotment.getCourse().getName());
                    }
                    if (allotment.getAdmAppln() != null) {
                        admissionStatusForm.setUniqueId(String.valueOf(allotment.getAdmAppln().getStudentOnlineApplication().getId()));
                    }
                    if (allotment.getAdmAppln().getPersonalData().getMobileNo1() != null && !allotment.getAdmAppln().getPersonalData().getMobileNo1().isEmpty() && allotment.getAdmAppln().getPersonalData().getMobileNo2() != null && !allotment.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()) {
                        admissionStatusForm.setMobile1(allotment.getAdmAppln().getPersonalData().getMobileNo1());
                        admissionStatusForm.setMobile2(allotment.getAdmAppln().getPersonalData().getMobileNo2());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getEmail() != null && !allotment.getAdmAppln().getPersonalData().getEmail().isEmpty()) {
                        admissionStatusForm.setEmail(allotment.getAdmAppln().getPersonalData().getEmail());
                    }
                    if (allotment.getAdmAppln().getPersonalData().getResidentCategory() != null) {
                        admissionStatusForm.setResidentCategoryForOnlineAppln(String.valueOf(allotment.getAdmAppln().getPersonalData().getResidentCategory().getId()));
                    }
                }
                if (!publishMap.isEmpty() && publishMap.containsKey(to.getCourseId()) && publishMap.containsValue(to.getAllotmentno())) {
                    allotmentTOs.add(to);
                }
            }
            for (final StudentCourseChanceMemo allotment2 : allotments) {
                final AdmissionStatusTO to = new AdmissionStatusTO();
                to.setCourseName(allotment2.getCourse().getName());
                to.setAllotmentno((int)allotment2.getChanceNo());
                to.setChanceAllotment("Chance");
                if (allotment2.getIsGeneral()) {
                    to.setCasteName("You have been provisionally Alloted- General Quota-");
                }
                else if (allotment2.getIsCaste()) {
                    to.setCasteName("You have been provisionally Alloted-SC/ST Quota-");
                }
                else {
                    to.setCasteName("You have been provisionally Alloted-Malankara Syrian catholic");
                }
                admissionStatusForm.setAdmApplnId(String.valueOf(allotment2.getAdmAppln().getId()));
                to.setCourseId(allotment2.getCourse().getId());
                if (allotment2.getIsAccept() && allotment2.getIsGeneral()) {
                    to.setMsgValue(" the allotment for " + allotment2.getCourse().getName() + " under Genearal Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment2.getCourse().getId();
                }
                else if (allotment2.getIsAccept() && allotment2.getIsCaste()) {
                    to.setMsgValue(" the allotment for " + allotment2.getCourse().getName() + " under SC/ST Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment2.getCourse().getId();
                }
                else if (allotment2.getIsAccept() && allotment2.getIsCommunity()) {
                    to.setMsgValue(" the allotment for " + allotment2.getCourse().getName() + " under Malankara Syrian Catholic Quota");
                    to.setIsAccept(Boolean.valueOf(true));
                    admissionStatusForm.setIsOnceAccept(true);
                    selectedCourseId = allotment2.getCourse().getId();
                }
                if (allotment2.getIsDecline() && allotment2.getIsGeneral()) {
                    to.setMsgValue("Your admission for " + allotment2.getCourse().getName() + " under Genearal Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment2.getIsDecline() && allotment2.getIsCaste()) {
                    to.setMsgValue("Your admission for " + allotment2.getCourse().getName() + " under SC/ST Quota is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                else if (allotment2.getIsDecline() && allotment2.getIsCommunity()) {
                    to.setMsgValue("Your admission for " + allotment2.getCourse().getName() + " under Malankara Syrian Catholic is ");
                    to.setIsDecline(Boolean.valueOf(true));
                }
                if (allotment2.getIsAccept()) {
                    final IAdmissionFormTransaction tx = AdmissionFormTransactionImpl.getInstance();
                    final StudentAllotmentPGIDetails details = tx.getBoObj(allotment2.getAdmAppln().getStudentOnlineApplication().getId());
                    if (details != null) {
                        admissionStatusForm.setIsPaid(true);
                    }
                }
                if (allotment2.getIsAccept() && allotment2.getPayonline()) {
                    if (allotment2.getPayonline()) {
                        admissionStatusForm.setPayonline(true);
                    }
                    if (allotment2.getAdmAppln().getPersonalData().getReligionSection().getId() == 2 || allotment2.getAdmAppln().getPersonalData().getReligionSection().getId() == 3 || allotment2.getAdmAppln().getPersonalData().getReligionSection().getId() == 4) {
                        if (allotment2.getCourse().getIsSelfFinancing()) {
                            if (allotment2.getIsGeneral()) {
                                admissionStatusForm.setApplicationAmount(allotment2.getCourse().getGeneralFee());
                            }
                            else if (allotment2.getIsCaste()) {
                                admissionStatusForm.setApplicationAmount(allotment2.getCourse().getCasteFee());
                            }
                            else if (allotment2.getIsCommunity()) {
                                admissionStatusForm.setApplicationAmount(allotment2.getCourse().getGeneralFee());
                            }
                        }
                        else {
                            admissionStatusForm.setApplicationAmount(allotment2.getCourse().getCasteFee());
                        }
                    }
                    else {
                        admissionStatusForm.setApplicationAmount(allotment2.getCourse().getGeneralFee());
                    }
                    if (allotment2.getAdmAppln().getPersonalData().getFirstName() != null && !allotment2.getAdmAppln().getPersonalData().getFirstName().isEmpty()) {
                        admissionStatusForm.setApplicantName(allotment2.getAdmAppln().getPersonalData().getFirstName());
                    }
                    if (allotment2.getAdmAppln().getCourse() != null && allotment2.getPayonline()) {
                        admissionStatusForm.setCourseId(String.valueOf(allotment2.getCourse().getId()));
                        admissionStatusForm.setCourseName(allotment2.getCourse().getName());
                    }
                    if (allotment2.getAdmAppln() != null) {
                        admissionStatusForm.setUniqueId(String.valueOf(allotment2.getAdmAppln().getStudentOnlineApplication().getId()));
                    }
                    if (allotment2.getAdmAppln().getPersonalData().getMobileNo1() != null && !allotment2.getAdmAppln().getPersonalData().getMobileNo1().isEmpty() && allotment2.getAdmAppln().getPersonalData().getMobileNo2() != null && !allotment2.getAdmAppln().getPersonalData().getMobileNo2().isEmpty()) {
                        admissionStatusForm.setMobile1(allotment2.getAdmAppln().getPersonalData().getMobileNo1());
                        admissionStatusForm.setMobile2(allotment2.getAdmAppln().getPersonalData().getMobileNo2());
                    }
                    if (allotment2.getAdmAppln().getPersonalData().getEmail() != null && !allotment2.getAdmAppln().getPersonalData().getEmail().isEmpty()) {
                        admissionStatusForm.setEmail(allotment2.getAdmAppln().getPersonalData().getEmail());
                    }
                    if (allotment2.getAdmAppln().getPersonalData().getResidentCategory() != null) {
                        admissionStatusForm.setResidentCategoryForOnlineAppln(String.valueOf(allotment2.getAdmAppln().getPersonalData().getResidentCategory().getId()));
                    }
                }
                if (!publishMapForChance.isEmpty() && publishMapForChance.containsKey(to.getCourseId()) && publishMapForChance.containsValue(to.getAllotmentno())) {
                    chanceTOs.add(to);
                }
            }
            if (!allotmentTOs.isEmpty()) {
                statusTOs.addAll(allotmentTOs);
            }
            if (!chanceTOs.isEmpty()) {
                statusTOs.addAll(chanceTOs);
            }
            if (admissionStatusForm.getIsOnceAccept()) {
                if (selectedCourseId == 1) {
                    admissionStatusForm.setFormlink("https://forms.gle/aqj96G5zE57AewGX7");
                }
                else if (selectedCourseId == 2) {
                    admissionStatusForm.setFormlink("https://forms.gle/7AvRSvvEQkT8WPfv7");
                }
                else if (selectedCourseId == 27) {
                    admissionStatusForm.setFormlink("https://forms.gle/QqcsCnBXKMgAKty1A");
                }
                else if (selectedCourseId == 4) {
                    admissionStatusForm.setFormlink("https://forms.gle/evCC73va9mw4fLBy6");
                }
                else if (selectedCourseId == 8) {
                    admissionStatusForm.setFormlink("https://forms.gle/8qwvGmP7RarpQYs18");
                }
                else if (selectedCourseId == 5) {
                    admissionStatusForm.setFormlink("https://forms.gle/xjKYam1eMkLccjcc9");
                }
                else if (selectedCourseId == 6) {
                    admissionStatusForm.setFormlink("https://forms.gle/TAhB1Ux6CMmB2taaA");
                }
                else if (selectedCourseId == 7) {
                    admissionStatusForm.setFormlink("https://forms.gle/ktfGitTYZrXtRcgn8");
                }
                else if (selectedCourseId == 20) {
                    admissionStatusForm.setFormlink("https://forms.gle/YAcqWQ53v6L5uVgz6");
                }
                else if (selectedCourseId == 9) {
                    admissionStatusForm.setFormlink("https://forms.gle/bWF5mJGdnP3QQAQ5A");
                }
                else if (selectedCourseId == 22) {
                    admissionStatusForm.setFormlink("https://forms.gle/sr767p5DreTEgNUi9");
                }
                else if (selectedCourseId == 29) {
                    admissionStatusForm.setFormlink("https://forms.gle/688atwmugdvGSxD28");
                }
                else if (selectedCourseId == 23) {
                    admissionStatusForm.setFormlink("https://forms.gle/H31pgNwK9AaLeni76");
                }
                else if (selectedCourseId == 26) {
                    admissionStatusForm.setFormlink("https://forms.gle/ftoEqouxTbLC9yJCA");
                }
                else if (selectedCourseId == 28) {
                    admissionStatusForm.setFormlink("https://forms.gle/tsYjtF1wyUBJxAUZ7");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return statusTOs;
    }
    
    public List<StudentCourseAllotment> getUpdatedBoPG(final List<StudentCourseAllotment> allotments, final AdmissionStatusForm admissionStatusForm) throws Exception {
        final List<StudentCourseAllotment> studentCourseAllotments = new ArrayList<StudentCourseAllotment>();
        final List<StudentCourseAllotment> courseAllotments = new ArrayList<StudentCourseAllotment>();
        final List<StudentCourseAllotment> totalAllotments = new ArrayList<StudentCourseAllotment>();
        final Map<Integer, Integer> publishMap = new HashMap<Integer, Integer>();
        boolean isOnceAccept = false;
        try {
            for (final StudentCourseAllotment allotment : allotments) {
                if (allotment.getCourse().getId() == admissionStatusForm.getSelectedCourseId()) {
                    if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("accept")) {
                        allotment.setIsAccept(Boolean.valueOf(true));
                        isOnceAccept = true;
                    }
                    else if (admissionStatusForm.getSelectedValue().equalsIgnoreCase("decline")) {
                        allotment.setIsDecline(Boolean.valueOf(true));
                    }
                    allotment.setModifiedBy(admissionStatusForm.getUserId());
                    allotment.setLastModifiedDate(new Date());
                }
                studentCourseAllotments.add(allotment);
            }
            if (isOnceAccept) {
                for (final StudentCourseAllotment allotment : studentCourseAllotments) {
                    if (allotment.getCourse().getId() != admissionStatusForm.getSelectedCourseId()) {
                        publishMap.put(allotment.getCourse().getId(), allotment.getAdmAppln().getId());
                        courseAllotments.add(allotment);
                    }
                }
                if (courseAllotments.isEmpty()) {
                    return studentCourseAllotments;
                }
                for (final StudentCourseAllotment s : studentCourseAllotments) {
                    if (publishMap.containsKey(s.getCourse().getId())) {
                        s.setModifiedBy(admissionStatusForm.getUserId());
                        s.setLastModifiedDate(new Date());
                        totalAllotments.add(s);
                    }
                    else {
                        totalAllotments.add(s);
                    }
                }
                return totalAllotments;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return studentCourseAllotments;
    }
    
    public Map<Integer, String> populateCourseBOtoTO(final List<CertificateCourse> courseBoList) {
        AdmissionStatusHelper.log.info((Object)"Start of populateCourseBOtoTO of CertificateCourseHelper");
        final List<CertificateCourseTO> courseToList = new ArrayList<CertificateCourseTO>();
        final Iterator<CertificateCourse> iterator = courseBoList.iterator();
        final Map<Integer, String> certificateCourses = new HashMap<Integer, String>();
        while (iterator.hasNext()) {
            final CertificateCourse certificateCourse = iterator.next();
            certificateCourses.put(certificateCourse.getId(), certificateCourse.getCertificateCourseName());
        }
        AdmissionStatusHelper.log.info((Object)"End of populateCourseBOtoTO of CertificateCourseHelper");
        return certificateCourses;
    }
    
    public List<StudentCertificateCourse> copyBoTo(final List<CertificateCourseTO> prefList, final AdmissionStatusForm admForm) {
        final List<StudentCertificateCourse> studcertCoursBoList = new ArrayList<StudentCertificateCourse>();
        for (final CertificateCourseTO to : prefList) {
            final StudentCertificateCourse bo = new StudentCertificateCourse();
            final CertificateCourse course = new CertificateCourse();
            course.setId(to.getId());
            bo.setCertificateCourse(course);
            final AdmAppln adm = new AdmAppln();
            adm.setId(Integer.parseInt(admForm.getAdmApplnId()));
            bo.setAdmAppln(adm);
            studcertCoursBoList.add(bo);
        }
        return studcertCoursBoList;
    }
}