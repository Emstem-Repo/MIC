package com.kp.cms.actions.exam;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.actions.admission.NewStudentCertificateCourseAction;
import com.kp.cms.bo.admin.AdmapplnAdditionalInfo;
import com.kp.cms.bo.admin.ApplnDoc;
import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ChallanUploadDataExam;
import com.kp.cms.bo.exam.ExamBlockUnblockHallTicketBO;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamFooterAgreementBO;
import com.kp.cms.bo.exam.ExamRegularApplication;
import com.kp.cms.bo.exam.ExamRevaluationApp;
import com.kp.cms.bo.exam.ExamRevaluationApplicationNew;
import com.kp.cms.bo.exam.ExamSupplementaryApplication;
import com.kp.cms.bo.exam.RevaluationApplicationPGIDetails;
import com.kp.cms.bo.exam.SupplementaryFees;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.forms.exam.NewSupplementaryImpApplicationForm;
import com.kp.cms.forms.exam.StudentsImprovementExamDetailsForm;
import com.kp.cms.forms.usermanagement.LoginForm;
import com.kp.cms.handlers.admission.NewStudentCertificateCourseHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.DownloadHallTicketHandler;
import com.kp.cms.handlers.exam.ExamMarksEntryHandler;
import com.kp.cms.handlers.exam.ExamSupplementaryImpAppHandler;
import com.kp.cms.handlers.exam.NewSecuredMarksEntryHandler;
import com.kp.cms.handlers.exam.NewSupplementaryImpApplicationHandler;
import com.kp.cms.handlers.exam.StudentsImprovementExamDetailsHandler;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationSubjectTO;
import com.kp.cms.to.exam.ExamSupplementaryImpApplicationTO;
import com.kp.cms.to.exam.SupplementaryAppExamTo;
import com.kp.cms.to.exam.SupplementaryApplicationClassTo;
import com.kp.cms.transactions.admin.ISingleFieldMasterTransaction;
import com.kp.cms.transactions.exam.IDownloadHallTicketTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactions.exam.INewSupplementaryImpApplicationTransaction;
import com.kp.cms.transactions.pettycash.ICashCollectionTransaction;
import com.kp.cms.transactionsimpl.admin.SingleFieldMasterTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.ClassEntryTransImpl;
import com.kp.cms.transactionsimpl.exam.DownloadHallTicketTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewSupplementaryImpApplicationTransactionImpl;
import com.kp.cms.transactionsimpl.pettycash.CashCollectionTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.CurrentAcademicYear;
import com.kp.cms.utilities.PropertyUtil;

public class NewSupplementaryImpApplicationAction extends BaseDispatchAction {

	private static final Log log = LogFactory
	.getLog(NewSupplementaryImpApplicationAction.class);
	ExamSupplementaryImpAppHandler handler = new ExamSupplementaryImpAppHandler();
	private static final String TO_DATEFORMAT = "MM/dd/yyyy";
	private static final String FROM_DATEFORMAT = "dd/MM/yyyy";

	/**
	 * Method to set the required data to the form to display it in
	 * ScoreSheet.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSupplementaryImpApplication(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered initSupplementaryImpApplication");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		newSupplementaryImpApplicationForm.resetFields();
		setRequiredDatatoForm(newSupplementaryImpApplicationForm);
		log.info("Exit initSupplementaryImpApplication");

		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @throws Exception
	 */
	private void setRequiredDatatoForm(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
	throws Exception {
		/*
		 * List<KeyValueTO> examList=handler.getExamNameList_SI();
		 * newSupplementaryImpApplicationForm.setExamList(examList);
		 */
		// added by Smitha for academic year and exam type parameters
		int year = 0;
		year = CurrentAcademicYear.getInstance().getAcademicyear();
		if (newSupplementaryImpApplicationForm.getYear() != null
				&& !newSupplementaryImpApplicationForm.getYear().isEmpty()) {
			year = Integer.parseInt(newSupplementaryImpApplicationForm
					.getYear());
		}
		if (year == 0) {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
		}
		if (newSupplementaryImpApplicationForm.getExamType() == null
				|| newSupplementaryImpApplicationForm.getExamType().trim()
				.isEmpty())
			newSupplementaryImpApplicationForm.setExamType("Suppl");

		Map<Integer, String> examMap = CommonAjaxHandler.getInstance()
		.getExamNameByExamTypeAndYear(
				newSupplementaryImpApplicationForm.getExamType(), year);
		newSupplementaryImpApplicationForm.setExamNameMap(examMap);

		ExamMarksEntryHandler examhandler = new ExamMarksEntryHandler();
		String currentExam = examhandler
		.getCurrentExamName(newSupplementaryImpApplicationForm
				.getExamType());
		if ((newSupplementaryImpApplicationForm.getExamId() == null || newSupplementaryImpApplicationForm
				.getExamId().trim().isEmpty())
				&& currentExam != null) {
			newSupplementaryImpApplicationForm.setExamId(currentExam);
		}
		// ends
		if (newSupplementaryImpApplicationForm.getExamId() != null
				&& newSupplementaryImpApplicationForm.getExamId().length() > 0) {
			newSupplementaryImpApplicationForm.setCourseList(handler
					.getCourseByExamNameRegNoRollNo(Integer
							.parseInt(newSupplementaryImpApplicationForm
									.getExamId()),
									newSupplementaryImpApplicationForm.getRegisterNo(),
									newSupplementaryImpApplicationForm.getRollNo()));
		} else {
			newSupplementaryImpApplicationForm.setCourseList(null);
		}
		if (newSupplementaryImpApplicationForm.getCourseId() != null
				&& newSupplementaryImpApplicationForm.getCourseId().length() > 0) {
			newSupplementaryImpApplicationForm.setSchemeList(handler
					.getSchemeNoByCourse(newSupplementaryImpApplicationForm
							.getCourseId()));
		} else {
			newSupplementaryImpApplicationForm.setSchemeList(null);
		}
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		validateCandidate(newSupplementaryImpApplicationForm, errors);
		if (errors.isEmpty()) {
			try {
				Map<String, ExamSupplementaryImpApplicationTO> toMap = NewSupplementaryImpApplicationHandler
				.getInstance().getStudentListForInput(
						newSupplementaryImpApplicationForm);
				if (toMap.isEmpty()) {
					errors
					.add(
							CMSConstants.ERROR,
							new ActionError(
									CMSConstants.KNOWLEDGEPRO_ADMISSION_NORECORDSFOUND));
					saveErrors(request, errors);
					setRequiredDatatoForm(newSupplementaryImpApplicationForm);
					return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
				}
				newSupplementaryImpApplicationForm.setToMap(toMap);
				setNameToForm(newSupplementaryImpApplicationForm);
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
		}
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @param errors
	 * @throws Exception
	 */
	private void validateCandidate(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			ActionErrors errors) throws Exception {
		if ((newSupplementaryImpApplicationForm.getRegisterNo() != null && !newSupplementaryImpApplicationForm
				.getRegisterNo().isEmpty())
				|| (newSupplementaryImpApplicationForm.getRollNo() != null && !newSupplementaryImpApplicationForm
						.getRollNo().isEmpty())) {
			if (newSupplementaryImpApplicationForm.getSchemeNo() == null
					|| newSupplementaryImpApplicationForm.getSchemeNo().trim()
					.isEmpty()) {
				errors.add("error", new ActionError(
				"knowledgepro.fee.semister.required"));
			}
		}
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @throws Exception
	 */
	private void setNameToForm(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm)
	throws Exception {
		newSupplementaryImpApplicationForm
		.setExamName(NewSecuredMarksEntryHandler
				.getInstance()
				.getPropertyValue(
						Integer
						.parseInt(newSupplementaryImpApplicationForm
								.getExamId()),
								"ExamDefinitionBO", true, "name"));
		if (newSupplementaryImpApplicationForm.getCourseId() != null
				&& !newSupplementaryImpApplicationForm.getCourseId().isEmpty())
			newSupplementaryImpApplicationForm
			.setCourseName(NewSecuredMarksEntryHandler
					.getInstance()
					.getPropertyValue(
							Integer
							.parseInt(newSupplementaryImpApplicationForm
									.getCourseId()), "Course",
									true, "name"));
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editStudent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		if (errors.isEmpty()) {
			try {
				Map<String, ExamSupplementaryImpApplicationTO> stuMap = newSupplementaryImpApplicationForm
				.getToMap();
				ExamSupplementaryImpApplicationTO to = stuMap
				.get(newSupplementaryImpApplicationForm.getStudent());
				NewSupplementaryImpApplicationHandler.getInstance()
				.setDatatoTo(to);
				newSupplementaryImpApplicationForm.setSuppTo(to);
				newSupplementaryImpApplicationForm.setAddOrEdit("edit");
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
		}
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT);
	}

	/**
	 * Method to select the candidates for score sheet result entry based on the
	 * input selected
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addSupplementaryApplication(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewExamMarksEntryAction - saveMarks");

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {
				boolean isSaved = NewSupplementaryImpApplicationHandler
				.getInstance().saveSupplementaryApplication(
						newSupplementaryImpApplicationForm);
				if (isSaved) {
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admin.updatesuccess",
					"Student Supplementary Application"));
					saveMessages(request, messages);
					newSupplementaryImpApplicationForm.resetFields();
				} else {
					errors.add(CMSConstants.ERROR, new ActionError(
							"kknowledgepro.admin.addfailure",
					"Student Supplementary Application"));
					saveErrors(request, errors);
				}
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		if (newSupplementaryImpApplicationForm.getAddOrEdit().equals("add")) {
			newSupplementaryImpApplicationForm.setAddOrEdit(null);
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
		} else {
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
		}
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteSupplementaryImpApp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);

		if (errors.isEmpty()) {
			try {
				Map<String, ExamSupplementaryImpApplicationTO> stuMap = newSupplementaryImpApplicationForm
				.getToMap();
				ExamSupplementaryImpApplicationTO to = stuMap
				.get(newSupplementaryImpApplicationForm.getStudent());
				boolean isDelete = NewSupplementaryImpApplicationHandler
				.getInstance().deleteSupplementaryImpApp(to);
				if (isDelete) {
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admin.deletesuccess",
					"Student Supplementary Application"));
					saveMessages(request, messages);
				} else {
					errors.add(CMSConstants.ERROR, new ActionError(
							"kknowledgepro.admin.addfailure",
					"Student Supplementary Application"));
					saveErrors(request, errors);
				}
				Map<String, ExamSupplementaryImpApplicationTO> toMap = NewSupplementaryImpApplicationHandler
				.getInstance().getStudentListForInput(
						newSupplementaryImpApplicationForm);
				newSupplementaryImpApplicationForm.setToMap(toMap);
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
		}
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getStudent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		validateStudentInformation(newSupplementaryImpApplicationForm, errors);
		if (errors.isEmpty()) {
			try {
				String validMsg = NewSupplementaryImpApplicationHandler
				.getInstance().checkValid(
						newSupplementaryImpApplicationForm);
				if (validMsg.isEmpty()) {
					ExamSupplementaryImpApplicationTO to = NewSupplementaryImpApplicationHandler
					.getInstance().getStudentDetails(
							newSupplementaryImpApplicationForm);
					newSupplementaryImpApplicationForm.setSuppTo(to);
				} else {
					errors.add(CMSConstants.ERROR,
							new ActionError(
									"knowledgepro.exam.supplementary.message",
									validMsg));
					addErrors(request, errors);
					setRequiredDatatoForm(newSupplementaryImpApplicationForm);
					log
					.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
					return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
				}
				newSupplementaryImpApplicationForm.setAddOrEdit("add");
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP);
		}
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT);
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @param errors
	 * @throws Exception
	 */
	private void validateStudentInformation(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			ActionErrors errors) throws Exception {
		String regNo = newSupplementaryImpApplicationForm.getRegisterNo();
		String rollNo = newSupplementaryImpApplicationForm.getRollNo();
		if ((regNo == null || regNo.trim().equals(""))
				&& (rollNo == null || rollNo.trim().equals(""))) {
			errors.add("error", new ActionError(
			"knowledgepro.exam.ExamInternalMark.regNoOrrollNumber"));
		}
		if (newSupplementaryImpApplicationForm.getSchemeNo() == null
				|| newSupplementaryImpApplicationForm.getSchemeNo().trim()
				.isEmpty()) {
			errors.add("error", new ActionError(
			"knowledgepro.fee.semister.required"));
		}
	}

	/**
	 * Method to set the required data to the form to display it in
	 * ScoreSheet.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cancelSupplementaryImpApplication(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_RESULT);
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkSupplementaryImpApplicationForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		HttpSession session = request.getSession(true);
		String courseId = (String) session.getAttribute("courseId").toString();
		newSupplementaryImpApplicationForm.resetFields();
		newSupplementaryImpApplicationForm.setCourseId(courseId);
		Properties prop = new Properties();
		InputStream inStr = CommonUtil.class.getClassLoader()
		.getResourceAsStream(CMSConstants.APPLICATION_PROPERTIES);
		prop.load(inStr);
		newSupplementaryImpApplicationForm.setFineFees(Double.parseDouble(prop
				.getProperty(CMSConstants.LINK_FOR_FEE_FINE)));
		setDataToForm(newSupplementaryImpApplicationForm, request);
		boolean dup = NewSupplementaryImpApplicationHandler.getInstance()
		.checkOnlinePaymentSuppl(newSupplementaryImpApplicationForm);

		if (dup) {
			newSupplementaryImpApplicationForm.setChallanButton(false);
			newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		} else {
			newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		}

		double totalAmount = 0;
		int theoryCount = 0;
		int practicalCount = 0;
		int projectCount = 0;
		double lateFine = 0;

		List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
		.getMainList();
		for (SupplementaryAppExamTo suppTo : examList) {
			List<SupplementaryApplicationClassTo> classTos = suppTo
			.getExamList();
			for (SupplementaryApplicationClassTo cto : classTos) {
				List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
				.getToList();
				for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
					if (to.getSubjectType().equalsIgnoreCase("T")) {
						if (to.isTempChecked()) {
							if (theoryCount == 0) {
								theoryCount++;
								totalAmount = totalAmount + newSupplementaryImpApplicationForm.getTheoryFees();
								
								
							} else if (theoryCount >= 1) {
								totalAmount = totalAmount + Double.valueOf(newSupplementaryImpApplicationForm.getApplicationFees());
								
							}
						}
					} else if (to.getSubjectType().equalsIgnoreCase("P")) {
						if (to.isTempPracticalChecked()) {
							if (practicalCount == 0 && theoryCount == 0) {
								practicalCount++;
								totalAmount = totalAmount + newSupplementaryImpApplicationForm.getPracticalFees()+ Double.valueOf(newSupplementaryImpApplicationForm.getCvCampFees());
							} else if (practicalCount >= 1 || theoryCount >= 1) {
								totalAmount = totalAmount
								+ Double
								.valueOf(newSupplementaryImpApplicationForm
										.getCvCampFees());
								
							}
						}
					} else if (to.getSubjectType().equalsIgnoreCase("Project")) {
						totalAmount = totalAmount
						+ Double
						.valueOf(newSupplementaryImpApplicationForm
								.getMarksListFees())
								+ Double
								.valueOf(newSupplementaryImpApplicationForm
										.getOnlineServiceChargeFees());

					}

					/*
					 * if(!to.isTempChecked()) if(to.getIsAppearedTheory())
					 * amount
					 * =amount+newSupplementaryImpApplicationForm.getTheoryFees
					 * (); if(!to.isTempPracticalChecked())
					 * if(to.getIsAppearedPractical())
					 * amount=amount+newSupplementaryImpApplicationForm
					 * .getPracticalFees();
					 */}
			}
		}
		
		if(practicalCount != 0 || theoryCount != 0){
			if(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees() != null){
			lateFine = Double.parseDouble(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees());
			}
			newSupplementaryImpApplicationForm.setTotalFees(totalAmount + lateFine);
		}else{
		   newSupplementaryImpApplicationForm.setTotalFees(totalAmount);
		}

		if (newSupplementaryImpApplicationForm.getStudentObj().getIsEgrand()) {
			newSupplementaryImpApplicationForm.setTotalFees(1030);
		}
		// online payment code temporarily commented do not delete
		/*
		 * booleandup=NewSupplementaryImpApplicationHandler.getInstance().
		 * checkDuplicationForSuppl(newSupplementaryImpApplicationForm);
		 * if(!dup) {
		 * if((session.getAttribute("programTypeId").toString().equalsIgnoreCase
		 * ("1") &&
		 * !newSupplementaryImpApplicationForm.getSchemeNo().equalsIgnoreCase
		 * ("6")) ||
		 * (session.getAttribute("programTypeId").toString().equalsIgnoreCase
		 * ("2") &&
		 * !newSupplementaryImpApplicationForm.getSchemeNo().equalsIgnoreCase
		 * ("4"))){ if(newSupplementaryImpApplicationForm.getIsFeesExempted()) {
		 * boolean result=NewSupplementaryImpApplicationHandler.getInstance().
		 * addAppliedStudentForSuppl(newSupplementaryImpApplicationForm);
		 * if(result) { return
		 * mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		 * 
		 * }else { return
		 * mapping.findForward("printDetailsforSupplementaryFail"); } }else{
		 * newSupplementaryImpApplicationForm.setChallanButton(false);
		 * newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		 * return
		 * mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		 * //return
		 * mapping.findForward(CMSConstants.SUPPLEMENTARY_APP_STUDENT_PAYMENT);
		 * } } else {
		 * if((session.getAttribute("programTypeId").toString().equalsIgnoreCase
		 * ("1") &&
		 * !newSupplementaryImpApplicationForm.getSchemeNo().equalsIgnoreCase
		 * ("6")) ||
		 * (session.getAttribute("programTypeId").toString().equalsIgnoreCase
		 * ("2") &&
		 * !newSupplementaryImpApplicationForm.getSchemeNo().equalsIgnoreCase
		 * ("4"))){
		 * 
		 * return
		 * mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		 * } else{ return
		 * mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		 * } }
		 * 
		 * }
		 * 
		 * else{ ExamSupplementaryApplication appl =
		 * NewSupplementaryImpApplicationHandler
		 * .getInstance().applicationForSuppl
		 * (newSupplementaryImpApplicationForm); if(appl!=null &&
		 * appl.isChallanVerified()){
		 * newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		 * newSupplementaryImpApplicationForm.setDisplayButton(false);
		 * newSupplementaryImpApplicationForm.setChallanButton(true); } else{
		 * ActionMessage message = new
		 * ActionMessage("knowledgepro.admin.challan.verification.process");
		 * messages.add("messages", message); saveMessages(request, messages);
		 * newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		 * newSupplementaryImpApplicationForm.setDisplayButton(false);
		 * newSupplementaryImpApplicationForm.setChallanButton(false); } return
		 * mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		 * 
		 * }
		 */

		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);

	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @param request
	 * @throws Exception
	 */
	private void setDataToForm(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setStudentId(Integer
				.parseInt(session.getAttribute("studentId").toString()));

		// rgahu write newly like regular app
		ISingleFieldMasterTransaction txn1 = SingleFieldMasterTransactionImpl
		.getInstance();
		Student student = (Student) txn1.getMasterEntryDataById(Student.class,
				newSupplementaryImpApplicationForm.getStudentId());
		newSupplementaryImpApplicationForm.setStudentObj(student);

		Boolean extendedTrue = NewSupplementaryImpApplicationHandler
		.getInstance().checkSuppDateExtended(
				newSupplementaryImpApplicationForm.getStudentId(),
				newSupplementaryImpApplicationForm);
		List<Integer> examIds = NewSupplementaryImpApplicationHandler
		.getInstance().checkSuppImpAppAvailable(
				newSupplementaryImpApplicationForm.getStudentId());

		// raghu added for improvement

		if (examIds.isEmpty()) {
			examIds = NewSupplementaryImpApplicationHandler.getInstance()
			.checkSuppImpAppAvailable1(
					newSupplementaryImpApplicationForm.getStudentId());

		}

		// raghu added from mounts

		// set app no and date
		NewSupplementaryImpApplicationHandler.getInstance().setAppNoAndDate(
				newSupplementaryImpApplicationForm);

		if (examIds.isEmpty()) {
			newSupplementaryImpApplicationForm.setSuppImpAppAvailable(false);
		} else {
			newSupplementaryImpApplicationForm.setSuppImpAppAvailable(true);
			NewSupplementaryImpApplicationHandler.getInstance()
			.getApplicationForms(extendedTrue, examIds,
					newSupplementaryImpApplicationForm);
			if (newSupplementaryImpApplicationForm.getProgramId() != null) {
				String query = "from SupplementaryFees s where s.course.id="
					+ newSupplementaryImpApplicationForm.getCourseId()
					+ " and s.academicYear ="
					+ newSupplementaryImpApplicationForm.getAcademicYear();
				SupplementaryFees bo = (SupplementaryFees) PropertyUtil
				.getDataForUniqueObject(query);
				if (bo != null) {
					if (bo.getTheoryFees() != null) {
						newSupplementaryImpApplicationForm.setTheoryFees(bo
								.getTheoryFees().doubleValue());
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}
					if (bo.getPracticalFees() != null) {
						newSupplementaryImpApplicationForm.setPracticalFees(bo
								.getPracticalFees().doubleValue());
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}
					if (bo.getApplicationFees() != null) {
						newSupplementaryImpApplicationForm
						.setApplicationFees(String.valueOf(bo
								.getApplicationFees().doubleValue()));
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}
					if (bo.getCvCampFees() != null) {
						newSupplementaryImpApplicationForm.setCvCampFees(String
								.valueOf(bo.getCvCampFees().doubleValue()));
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}
					if (bo.getMarksListFees() != null) {
						newSupplementaryImpApplicationForm
						.setMarksListFees(String.valueOf(bo
								.getMarksListFees().doubleValue()));
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}
					if (bo.getOnlineServiceChargeFees() != null) {
						newSupplementaryImpApplicationForm
						.setOnlineServiceChargeFees(String.valueOf(bo
								.getOnlineServiceChargeFees()
								.doubleValue()));
					} else {
						newSupplementaryImpApplicationForm
						.setFeesNotConfigured(true);
					}

				} else {
					newSupplementaryImpApplicationForm
					.setPrintSupplementary(false);
					newSupplementaryImpApplicationForm
					.setFeesNotConfigured(true);
				}
			}
			// temparery purpose
			newSupplementaryImpApplicationForm.setFeesNotConfigured(false);
			ICashCollectionTransaction cashCollectionTransaction = CashCollectionTransactionImpl
			.getInstance();
			int finId = cashCollectionTransaction.getCurrentFinancialYear();
			if (finId <= 0) {
				// newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
			} else {
				String query = "from OnlineBillNumber o where o.pcFinancialYear.id = "
					+ finId + " and o.isActive = 1";
				INewExamMarksEntryTransaction txn = NewExamMarksEntryTransactionImpl
				.getInstance();
				List list = txn.getDataForQuery(query);
				if (list == null || list.isEmpty())
					// newSupplementaryImpApplicationForm.setFeesNotConfigured(true);
					newSupplementaryImpApplicationForm.setFinId(finId);
			}
			// ISingleFieldMasterTransaction
			// txn=SingleFieldMasterTransactionImpl.getInstance();
			// Student student=(Student)
			// txn.getMasterEntryDataById(Student.class,newSupplementaryImpApplicationForm.getStudentId());
			if (student != null) {

				// raghu write newly like regular app
				newSupplementaryImpApplicationForm.setNameOfStudent(student
						.getAdmAppln().getPersonalData().getFirstName()
						+ (student.getAdmAppln().getPersonalData()
								.getMiddleName() != null ? student
										.getAdmAppln().getPersonalData()
										.getMiddleName() : "")
										+ (student.getAdmAppln().getPersonalData()
												.getLastName() != null ? student.getAdmAppln()
														.getPersonalData().getLastName() : ""));
				newSupplementaryImpApplicationForm.setClassName(student
						.getClassSchemewise() != null ? student
								.getClassSchemewise().getClasses().getName() : "");
				newSupplementaryImpApplicationForm.setRegNo(student
						.getRegisterNo());
				newSupplementaryImpApplicationForm.setDob(null);
				newSupplementaryImpApplicationForm
				.setOriginalDob(CommonUtil
						.ConvertStringToDateFormat(
								CommonUtil.getStringDate(student
										.getAdmAppln()
										.getPersonalData()
										.getDateOfBirth()),
										NewStudentCertificateCourseAction.SQL_DATEFORMAT,
										NewStudentCertificateCourseAction.FROM_DATEFORMAT));
				newSupplementaryImpApplicationForm.setAddress("");
				newSupplementaryImpApplicationForm.setCourseName(student
						.getAdmAppln().getCourseBySelectedCourseId().getName());
				newSupplementaryImpApplicationForm.setSchemeNo(""
						+ student.getClassSchemewise().getClasses()
						.getTermNumber());
				newSupplementaryImpApplicationForm.setEmail(student
						.getAdmAppln().getPersonalData().getEmail());
				newSupplementaryImpApplicationForm.setMobileNo(student
						.getAdmAppln().getPersonalData().getMobileNo2());
				newSupplementaryImpApplicationForm.setCourseId(""
						+ student.getClassSchemewise().getClasses().getCourse()
						.getId());
				newSupplementaryImpApplicationForm.setGender(student
						.getAdmAppln().getPersonalData().getGender());
				String address = "";
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine1() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine2() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressDistrictId()
					.getName();
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressStateId()
					.getName() + ",";
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}

				newSupplementaryImpApplicationForm.setAddress(address);

				// vinodha
				newSupplementaryImpApplicationForm
				.setCommunicationEmail(student.getAdmAppln()
						.getPersonalData().getEmail());
				newSupplementaryImpApplicationForm
				.setCommunicationMobileNo(student.getAdmAppln()
						.getPersonalData().getMobileNo2());

				String communicationAddress = "";
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1().equalsIgnoreCase("")) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine1() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2().equalsIgnoreCase("")) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine2() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId() != null) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressDistrictId()
					.getName() + ",";
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId() != null) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressStateId()
					.getName();
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					communicationAddress = communicationAddress
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers();
				}
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressZipCode() != null)
					newSupplementaryImpApplicationForm
					.setCommunicationAddressZipCode(student
							.getAdmAppln().getPersonalData()
							.getCurrentAddressZipCode());

				newSupplementaryImpApplicationForm
				.setCommunicationAddress(communicationAddress
						.toUpperCase());

				if (student.getAdmAppln().getPersonalData()
						.getReligionSection() != null)
					newSupplementaryImpApplicationForm
					.setFeeConcessionCategory(student.getAdmAppln()
							.getPersonalData().getReligionSection()
							.getName());

				if (student.getAdmAppln().getPersonalData()
						.getReligionSection() != null)
					if (student.getAdmAppln().getPersonalData()
							.getReligionSection().getName().equalsIgnoreCase(
							"sc")
							|| student.getAdmAppln().getPersonalData()
							.getReligionSection().getName()
							.equalsIgnoreCase("st")
							|| student.getAdmAppln().getPersonalData()
							.getReligionSection().getName()
							.equalsIgnoreCase("FMN")
							|| student.getAdmAppln().getPersonalData()
							.getReligionSection().getName()
							.equalsIgnoreCase("KPCR")
							|| student.getAdmAppln().getPersonalData()
							.getReligionSection().getName()
							.equalsIgnoreCase("SEBC")
							|| student.getAdmAppln().getPersonalData()
							.getReligionSection().getName()
							.equalsIgnoreCase("OBC (Non-Creamy)"))
						newSupplementaryImpApplicationForm
						.setFeeConcession("YES");
					else
						newSupplementaryImpApplicationForm
						.setFeeConcession("NO");

				String religion = "";
				if (student.getAdmAppln().getPersonalData().getReligion() != null)
					religion = religion
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getReligion().getName();
				else if (student.getAdmAppln().getPersonalData()
						.getReligionOthers() != null)
					religion = religion
					+ ","
					+ student.getAdmAppln().getPersonalData()
					.getReligionOthers();
				if (student.getAdmAppln().getPersonalData().getCaste() != null)
					religion = religion
					+ ","
					+ student.getAdmAppln().getPersonalData()
					.getCaste().getName();
				else if (student.getAdmAppln().getPersonalData()
						.getCasteOthers() != null)
					religion = religion
					+ ","
					+ student.getAdmAppln().getPersonalData()
					.getCasteOthers();

				newSupplementaryImpApplicationForm.setReligion(religion);

				String careTaker = "";
				if (student.getAdmAppln().getPersonalData().getFatherName() != null)
					careTaker = student.getAdmAppln().getPersonalData()
					.getFatherName()
					+ ",Father";
				else if (student.getAdmAppln().getPersonalData()
						.getMotherName() != null)
					careTaker = student.getAdmAppln().getPersonalData()
					.getMotherName()
					+ ",Mother";
				else if (student.getAdmAppln().getPersonalData()
						.getGuardianName() != null)
					careTaker = student.getAdmAppln().getPersonalData()
					.getGuardianName()
					+ ",Guardian";

				newSupplementaryImpApplicationForm.setCareTaker(careTaker
						.toUpperCase());

				// getting instructions
				String programTypeId = NewSecuredMarksEntryHandler
				.getInstance().getPropertyValue(
						student.getClassSchemewise().getClasses()
						.getId(), "Classes", true,
				"course.program.programType.id");
				IDownloadHallTicketTransaction txn = new DownloadHallTicketTransactionImpl();
				List<ExamFooterAgreementBO> footer = txn.getFooterDetails(
						programTypeId, "E", newSupplementaryImpApplicationForm
						.getClassId());
				if (footer != null && !footer.isEmpty()) {
					ExamFooterAgreementBO obj = footer.get(0);
					if (obj.getDescription() != null)
						newSupplementaryImpApplicationForm.setDescription(obj
								.getDescription());
				} else {
					newSupplementaryImpApplicationForm.setDescription(null);
				}

			}
			if (extendedTrue) {
				newSupplementaryImpApplicationForm.setExtended(true);
			} else {
				newSupplementaryImpApplicationForm.setExtended(false);
			}
		}
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward calculateAmount(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewExamMarksEntryAction - saveMarks");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			double amount = 0;
			try {
				List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
				.getMainList();
				for (SupplementaryAppExamTo suppTo : examList) {
					List<SupplementaryApplicationClassTo> classTos = suppTo
					.getExamList();
					for (SupplementaryApplicationClassTo cto : classTos) {
						List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
						.getToList();
						for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
							if (!to.isTempChecked())
								if (to.getIsAppearedTheory())
									amount = amount
									+ newSupplementaryImpApplicationForm
									.getTheoryFees();
							if (!to.isTempPracticalChecked())
								if (to.getIsAppearedPractical())
									amount = amount
									+ newSupplementaryImpApplicationForm
									.getPracticalFees();
						}
					}
				}

				if (amount == 0) {
					newSupplementaryImpApplicationForm
					.setPrintSupplementary(false);
					errors.add(CMSConstants.ERROR, new ActionError(
							"knowledgepro.exam.supplementary.message",
					"Please Select atleast one subject"));
					saveErrors(request, errors);
					return mapping
					.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
				} else {
					newSupplementaryImpApplicationForm
					.setPrintSupplementary(true);
					if (newSupplementaryImpApplicationForm.isExtended()) {
						amount = amount
						+ newSupplementaryImpApplicationForm
						.getFineFees();
					}
				}
				newSupplementaryImpApplicationForm.setTotalFees(amount);

				// for supplementary online payment temp commented as mic not
				// using online payment

				/*
				 * if(amount==0){
				 * if(newSupplementaryImpApplicationForm.getIsAppliedAlready()){
				 * newSupplementaryImpApplicationForm.setChallanButton(false);
				 * newSupplementaryImpApplicationForm
				 * .setPrintSupplementary(false);
				 * newSupplementaryImpApplicationForm.setDisplayButton(true);
				 * returnmapping.findForward(CMSConstants.
				 * SUPPLEMENTARY_APP_STUDENT_PAYMENT); } else{
				 * newSupplementaryImpApplicationForm
				 * .setPrintSupplementary(false);
				 * errors.add(CMSConstants.ERROR,new
				 * ActionError("knowledgepro.exam.supplementary.message"
				 * ,"Please Select atleast one subject")); saveErrors(request,
				 * errors); returnmapping.findForward(CMSConstants.
				 * INIT_SUPPL_IMP_APP_STUDENT_RESULT); } }
				 * 
				 * else {
				 * if(!newSupplementaryImpApplicationForm.getIsFeesExempted()){
				 * newSupplementaryImpApplicationForm.setTotalFees(amount);
				 * double applicationFees =0; double markslistFees =0; double
				 * cvCampFees =0; double onlineServiceCharge =0; double
				 * grandTotalFees =0;
				 * if(newSupplementaryImpApplicationForm.getApplicationFees
				 * ()!=null) applicationFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getApplicationFees());
				 * if(newSupplementaryImpApplicationForm.getCvCampFees()!=null)
				 * cvCampFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getCvCampFees());
				 * if(newSupplementaryImpApplicationForm.getMarksListFees
				 * ()!=null) markslistFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getMarksListFees());
				 * if(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees
				 * ()!=null) onlineServiceCharge =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getOnlineServiceChargeFees()); amount =
				 * amount+applicationFees
				 * +cvCampFees+markslistFees+onlineServiceCharge;
				 * 
				 * 
				 * 
				 * 
				 * //newSupplementaryImpApplicationForm.setPrintSupplementary(true
				 * ); if(newSupplementaryImpApplicationForm.isExtended()) {
				 * amount
				 * =amount+newSupplementaryImpApplicationForm.getFineFees(); }
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmount(String.valueOf(amount));
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmount1(String.valueOf(amount));
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmountWords(CommonUtil.numberToWord1((int)
				 * amount)); } else{
				 * newSupplementaryImpApplicationForm.setApplicationAmount
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setApplicationAmount1
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setApplicationAmountWords
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setPrintSupplementary
				 * (true);
				 * newSupplementaryImpApplicationForm.setChallanButton(true);
				 * returnmapping.findForward(CMSConstants.
				 * INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART_2); } }
				 */
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		// raghu added from mounts
		// return
		// mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART);

		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART_2);

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verifyStudentSmartCardForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = new ActionErrors();
		setUserId(request, newSupplementaryImpApplicationForm);// setting the
		// userId to
		// Form
		try {
			boolean isValidSmartCard = NewSupplementaryImpApplicationHandler
			.getInstance()
			.verifySmartCard(
					newSupplementaryImpApplicationForm.getSmartCardNo(),
					newSupplementaryImpApplicationForm.getStudentId());
			if (!isValidSmartCard) {
				errors
				.add(
						CMSConstants.ERROR,
						new ActionError(
								"knowledgepro.admission.empty.err.message",
						"Please Enter the valid last 5 digits of your smart card number"));
			}
			if (newSupplementaryImpApplicationForm.getDob() != null) {
				if (!newSupplementaryImpApplicationForm.getDob()
						.equalsIgnoreCase(
								newSupplementaryImpApplicationForm
								.getOriginalDob()))
					errors.add(CMSConstants.ERROR, new ActionError(
							"knowledgepro.admission.empty.err.message",
					"Please Enter Valid Date Of Birth"));
			}

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping
				.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART);
			}

		} catch (ReActivateException e) {
			errors.add("error", new ActionError(
			"knowledgepro.admission.certificate.course.available"));
			saveErrors(request, errors);
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART);
		} catch (Exception e) {
			log.error("Error in getCertificateCourses" + e.getMessage());
			String msg = super.handleApplicationException(e);
			newSupplementaryImpApplicationForm.setErrorMessage(msg);
			newSupplementaryImpApplicationForm.setErrorStack(e.getMessage());
			return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
		}

		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART_1);
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addSupplementaryApplicationForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewExamMarksEntryAction - saveMarks");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {
				String msg = "";
				boolean isSaved = NewSupplementaryImpApplicationHandler
				.getInstance()
				.saveSupplementaryApplicationForStudentLogin(
						newSupplementaryImpApplicationForm);
				msg = newSupplementaryImpApplicationForm.getMsg();
				if (isSaved) {
					// raghu
					// NewStudentCertificateCourseHandler.getInstance().sendSMSToStudent(newSupplementaryImpApplicationForm.getStudentId(),CMSConstants.SUPPLEMENTARY_APPLICATION_SMS_TEMPLATE);
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
					"knowledgepro.certificate.added.success.online"));
					saveMessages(request, messages);

					// raghu added from mounts
					// set supply details to tos for printing application
					boolean extendedTrue = NewSupplementaryImpApplicationHandler
					.getInstance().checkSuppDateExtended(
							newSupplementaryImpApplicationForm
							.getStudentId(),
							newSupplementaryImpApplicationForm);
					List<Integer> examIds = NewSupplementaryImpApplicationHandler
					.getInstance().checkSuppImpAppAvailable(
							newSupplementaryImpApplicationForm
							.getStudentId());
					NewSupplementaryImpApplicationHandler.getInstance()
					.getApplicationForms(extendedTrue, examIds,
							newSupplementaryImpApplicationForm);

					double totalAmount = 0;
					int theoryCount = 0;
					int practicalCount = 0;
					int projectCount = 0;
					double lateFine = 0;

					List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
					.getMainList();
					for (SupplementaryAppExamTo suppTo : examList) {
						List<SupplementaryApplicationClassTo> classTos = suppTo
						.getExamList();
						for (SupplementaryApplicationClassTo cto : classTos) {
							List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
							.getToList();
							for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
								if (to.getSubjectType().equalsIgnoreCase("T")) {
									if (to.isTempChecked()) {
										if (theoryCount == 0) {
											theoryCount++;
											totalAmount = totalAmount
											+ newSupplementaryImpApplicationForm
											.getTheoryFees();
											
										} else if (theoryCount >= 1) {
											totalAmount = totalAmount
											+ Double
											.valueOf(newSupplementaryImpApplicationForm
													.getApplicationFees());
											
											
										}
										
									}
								} else if (to.getSubjectType()
										.equalsIgnoreCase("P")) {
									if (to.isTempPracticalChecked()) {
										if (practicalCount == 0
												&& theoryCount == 0) {
											practicalCount++;
											totalAmount = totalAmount
											+ newSupplementaryImpApplicationForm
											.getPracticalFees()
											+ Double
											.valueOf(newSupplementaryImpApplicationForm
													.getCvCampFees());
											
										} else if (practicalCount >= 1
												|| theoryCount >= 1) {
											totalAmount = totalAmount
											+ Double
											.valueOf(newSupplementaryImpApplicationForm
													.getCvCampFees());
											
										}
									}
								} else if (to.getSubjectType()
										.equalsIgnoreCase("Project")) {
									totalAmount = totalAmount
									+ Double
									.valueOf(newSupplementaryImpApplicationForm
											.getMarksListFees())
											+ Double
											.valueOf(newSupplementaryImpApplicationForm
													.getOnlineServiceChargeFees());

								}

								/*
								 * if(!to.isTempChecked())
								 * if(to.getIsAppearedTheory())
								 * amount=amount+newSupplementaryImpApplicationForm
								 * .getTheoryFees();
								 * if(!to.isTempPracticalChecked())
								 * if(to.getIsAppearedPractical())
								 * amount=amount+
								 * newSupplementaryImpApplicationForm
								 * .getPracticalFees();
								 */}
						}
					}
					if(practicalCount != 0 || theoryCount != 0){
						lateFine = Double.parseDouble(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees());
						newSupplementaryImpApplicationForm.setTotalFees(totalAmount + lateFine);
					}else{
					   newSupplementaryImpApplicationForm.setTotalFees(totalAmount);
					}
					//newSupplementaryImpApplicationForm.setTotalFees(totalAmount);
					ISingleFieldMasterTransaction txn1 = SingleFieldMasterTransactionImpl
					.getInstance();
					Student student = (Student) txn1.getMasterEntryDataById(
							Student.class, newSupplementaryImpApplicationForm
							.getStudentId());
					newSupplementaryImpApplicationForm.setStudentObj(student);
					if (newSupplementaryImpApplicationForm.getStudentObj()
							.getIsEgrand()) {
						newSupplementaryImpApplicationForm.setTotalFees(1030);
					}
					String printData = NewSupplementaryImpApplicationHandler
					.getInstance().getPrintData(
							newSupplementaryImpApplicationForm
							.getOnlinePaymentId(), request);
					// resetting the fields
					// newSupplementaryImpApplicationForm.resetFields();
					// setting data for print
					newSupplementaryImpApplicationForm.setPrintData(printData);
					setDataToForm(newSupplementaryImpApplicationForm, request);
					newSupplementaryImpApplicationForm
					.setPrintSupplementary(false);
				} else {
					if (msg == null || msg.isEmpty()) {
						msg = "Payment Failed";
					}
					errors.add(CMSConstants.ERROR, new ActionError(
							"knowledgepro.admission.empty.err.message",
							"Supplementary/Improvement submission was not successfull, Reason:"
							+ msg));
					errors
					.add(
							CMSConstants.ERROR,
							new ActionError(
									"knowledgepro.admission.empty.err.message",
							"Kindly rectify the errors mentioned and re-submit the application"));
					saveErrors(request, errors);
					return mapping
					.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
				}
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		// return
		// mapping.findForward(CMSConstants.SUPPLEMENTARY_APP_STUDENT_PAYMENT);
		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showPrintDetails(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// raghu added from mounts
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		if (newSupplementaryImpApplicationForm.isPrintSupplementary()) {
			if (newSupplementaryImpApplicationForm.getStudentObj()
					.getAdmAppln().getCourseBySelectedCourseId().getProgram()
					.getProgramType().getId() == 1) {
				return mapping.findForward("printDetailsUG");
			} else {
				return mapping.findForward("printDetailsPG");
			}
		} else {
			errors.add(CMSConstants.ERROR, new ActionError(
					"knowledgepro.exam.supplementary.message",
			"Please Select atleast one subject"));
			saveErrors(request, errors);
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}

	}

	// raghu added from mounts

	public ActionForward showPrintDetailsSubjectList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();

		return mapping.findForward("printDetails");
	}

	/**
	 * Method to set the required data to the form to display it in
	 * ScoreSheet.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSupplementaryImpApplicationForAll(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered initSupplementaryImpApplication");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		newSupplementaryImpApplicationForm.resetFields();
		setRequiredDatatoForm(newSupplementaryImpApplicationForm);
		log.info("Exit initSupplementaryImpApplication");

		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);
	}

	// raghu write for getting all improvement student
	public ActionForward checkUpdateProcessForSupp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log
		.info("Entered NewSupplementaryImpApplicationAction - checkUpdateProcessForSupp");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);

		INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl
		.getInstance();
		String query = "from StudentSupplementaryImprovementApplication s where  s.classes.course.id="
			+ newSupplementaryImpApplicationForm.getCourseId()
			+ " and s.schemeNo="
			+ newSupplementaryImpApplicationForm.getSchemeNo()
			+ " and s.examDefinition.id="
			+ newSupplementaryImpApplicationForm.getExamId();

		List previousList = transaction.getDataForQuery(query);
		if (previousList.size() != 0) {

			getAllStudents(mapping, newSupplementaryImpApplicationForm,
					request, response);
			newSupplementaryImpApplicationForm.setAdd(true);
			newSupplementaryImpApplicationForm.setContinue1(false);

			log
			.info("Entered NewSupplementaryImpApplicationAction - checkUpdateProcessForSupp");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);

		} else {
			newSupplementaryImpApplicationForm.setAdd(false);
			newSupplementaryImpApplicationForm.setContinue1(true);
			errors.add(CMSConstants.ERROR, new ActionError(
			"knowledgepro.exam.supp.updateprocess.check"));
			saveErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Entered NewSupplementaryImpApplicationAction - checkUpdateProcessForSupp");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);
		}

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// raghu write for getting all improvement student
	public ActionForward getAllStudents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);

		// for check updateprocess
		newSupplementaryImpApplicationForm.setAdd(true);
		newSupplementaryImpApplicationForm.setContinue1(false);

		if (errors.isEmpty()) {
			try {
				String validMsg = "";
				// raghu
				// String
				// validMsg=NewSupplementaryImpApplicationHandler.getInstance().checkValidAll(newSupplementaryImpApplicationForm);
				String validMsg1 = NewSupplementaryImpApplicationHandler
				.getInstance().checkImpValidAll(
						newSupplementaryImpApplicationForm);

				if (validMsg.isEmpty() && validMsg1.isEmpty()) {

					List<ExamSupplementaryImpApplicationTO> suppToList = NewSupplementaryImpApplicationHandler
					.getInstance().getStudentDetailsAllSupply(
							newSupplementaryImpApplicationForm);
					newSupplementaryImpApplicationForm
					.setSuppToList(suppToList);

					boolean isSaved = NewSupplementaryImpApplicationHandler
					.getInstance().saveSupplementaryApplicationAll(
							newSupplementaryImpApplicationForm);

					List<ExamSupplementaryImpApplicationTO> impToList = NewSupplementaryImpApplicationHandler
					.getInstance().getStudentDetailsAll(
							newSupplementaryImpApplicationForm);
					newSupplementaryImpApplicationForm.setSuppToList(impToList);

					isSaved = NewSupplementaryImpApplicationHandler
					.getInstance().saveSupplementaryApplicationAll(
							newSupplementaryImpApplicationForm);

					if (isSaved) {
						messages.add(CMSConstants.MESSAGES, new ActionMessage(
								"knowledgepro.admin.updatesuccess",
						"Student Improvement Application"));
						saveMessages(request, messages);
						newSupplementaryImpApplicationForm.resetFields();
					} else {
						errors.add(CMSConstants.ERROR, new ActionError(
								"kknowledgepro.admin.addfailure",
						"Student Improvement Application"));
						saveErrors(request, errors);
					}

				} else {

					if (!validMsg.isEmpty()) {
						errors.add(CMSConstants.ERROR, new ActionError(
								"knowledgepro.exam.supplementary.message",
								validMsg));
					} else {
						errors.add(CMSConstants.ERROR, new ActionError(
								"knowledgepro.exam.supplementary.message",
								validMsg1));

					}
					addErrors(request, errors);
					setRequiredDatatoForm(newSupplementaryImpApplicationForm);
					log
					.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
					return mapping
					.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);
				}
				newSupplementaryImpApplicationForm.setAddOrEdit("add");
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			setRequiredDatatoForm(newSupplementaryImpApplicationForm);
			log
			.info("Exit NewSupplementaryImpApplicationAction - getCandidates errors not empty ");
			return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);
		}
		log
		.info("Entered NewSupplementaryImpApplicationAction - getCandidates");
		return mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_ALL);
	}

	// raghu for regular exam application

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkRegularApplicationForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		setUserId(request, newSupplementaryImpApplicationForm);
		newSupplementaryImpApplicationForm.setPaymentSuccess(false);
		newSupplementaryImpApplicationForm.resetFields();
		setDataToFormForRegular(newSupplementaryImpApplicationForm, request);
		boolean dup = NewSupplementaryImpApplicationHandler.getInstance()
		.checkPaymentDetails(newSupplementaryImpApplicationForm);

		if (dup) {
			newSupplementaryImpApplicationForm.setChallanButton(false);
			newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		}else {
			newSupplementaryImpApplicationForm.setChallanButton(true);
			newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		}

		/*
		 * boolean
		 * dup=NewSupplementaryImpApplicationHandler.getInstance().checkDuplication
		 * (newSupplementaryImpApplicationForm); if(!dup) {
		 * if((session.getAttribute
		 * ("programTypeId").toString().equalsIgnoreCase("1") &&
		 * !newSupplementaryImpApplicationForm
		 * .getSchemeNo().equalsIgnoreCase("6")) ||
		 * (session.getAttribute("programTypeId"
		 * ).toString().equalsIgnoreCase("2") &&
		 * !newSupplementaryImpApplicationForm
		 * .getSchemeNo().equalsIgnoreCase("4"))){
		 * if(newSupplementaryImpApplicationForm.isRegExamFeesExempted()) {
		 * boolean result=
		 * NewSupplementaryImpApplicationHandler.getInstance().addAppliedStudent
		 * (newSupplementaryImpApplicationForm); if(result) {
		 * if(newSupplementaryImpApplicationForm
		 * .getStudentObj().getAdmAppln().getCourseBySelectedCourseId
		 * ().getProgram().getProgramType().getId()==1){ return
		 * mapping.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		 * }else{ return
		 * mapping.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		 * } }else { return mapping.findForward("printDetailsforRegularFail"); }
		 * }else{ return
		 * mapping.findForward(CMSConstants.REGULAR_APP_STUDENT_PAYMENT); } }
		 * else { boolean result=
		 * NewSupplementaryImpApplicationHandler.getInstance
		 * ().addAppliedStudent(newSupplementaryImpApplicationForm); if(result)
		 * {
		 * if(newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln().
		 * getCourseBySelectedCourseId
		 * ().getProgram().getProgramType().getId()==1){ return
		 * mapping.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		 * }else{ return
		 * mapping.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		 * } }else { return mapping.findForward("printDetailsforRegularFail"); }
		 * }
		 * 
		 * }
		 */
		if (newSupplementaryImpApplicationForm.getIsPreviousExam()
				.equalsIgnoreCase("yes")) {
			newSupplementaryImpApplicationForm.setRegularAppAvailable(false);
		}

		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		}
	}

	/**
	 * @param newSupplementaryImpApplicationForm
	 * @param request
	 * @throws Exception
	 */
	private void setDataToFormForRegular(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setIsPreviousExam(session
				.getAttribute("isPrevApplicationAvailable").toString());
		Integer prevClassId = (Integer) session.getAttribute("prevClassId");
		newSupplementaryImpApplicationForm.setPrevClassId(String
				.valueOf(prevClassId));
		newSupplementaryImpApplicationForm.setStudentId(Integer
				.parseInt(session.getAttribute("studentId").toString()));
		ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl
		.getInstance();
		Student student = (Student) txn.getMasterEntryDataById(Student.class,
				newSupplementaryImpApplicationForm.getStudentId());
		newSupplementaryImpApplicationForm.setStudentObj(student);
		List<Integer> examIds = new ArrayList<Integer>();
		if (newSupplementaryImpApplicationForm.getPreviousExam()) {
			examIds = NewSupplementaryImpApplicationHandler.getInstance()
			.checkRegularAppAvailable(prevClassId);
			if (examIds.size() > 0)
				newSupplementaryImpApplicationForm.setPrevClassExamId(String
						.valueOf(examIds.get(0)));
		} else {
			newSupplementaryImpApplicationForm
			.setClassId(String.valueOf(student.getClassSchemewise()
					.getClasses().getId()));
			examIds = NewSupplementaryImpApplicationHandler.getInstance()
			.checkRegularAppAvailable(
					student.getClassSchemewise().getClasses().getId());
		}
		IDownloadHallTicketTransaction txn1 = DownloadHallTicketTransactionImpl
		.getInstance();
		if (examIds.isEmpty()) {
			newSupplementaryImpApplicationForm.setRegularAppAvailable(false);
		}

		else {
			ExamBlockUnblockHallTicketBO h = null;
			for (Integer examId : examIds) {
				h = txn1
				.isHallTicketBlockedStudent(student.getId(), student
						.getClassSchemewise().getClasses().getId(),
						examId, "A");
			}
			if (h != null) {
				newSupplementaryImpApplicationForm
				.setRegularAppAvailable(false);
			} else {

				newSupplementaryImpApplicationForm.setRegularAppAvailable(true);
				NewSupplementaryImpApplicationHandler.getInstance()
				.getApplicationFormsForRegular(examIds,
						newSupplementaryImpApplicationForm);

				boolean dup = NewSupplementaryImpApplicationHandler
				.getInstance().checkDuplication(
						newSupplementaryImpApplicationForm);
				if (!dup) {
					newSupplementaryImpApplicationForm.setDisplayButton(true);
					newSupplementaryImpApplicationForm.setChallanButton(false);
				} else {
					newSupplementaryImpApplicationForm.setChallanButton(true);
					newSupplementaryImpApplicationForm.setDisplayButton(false);
				}

				if (student != null) {
					String STUDENT_IMAGE = "images/StudentPhotos/"
						+ student.getId() + ".jpg?"
						+ student.getAdmAppln().getLastModifiedDate();
					session.setAttribute("STUDENT_IMAGE", STUDENT_IMAGE);

					newSupplementaryImpApplicationForm.setNameOfStudent(student
							.getAdmAppln().getPersonalData().getFirstName()
							+ (student.getAdmAppln().getPersonalData()
									.getMiddleName() != null ? student
											.getAdmAppln().getPersonalData()
											.getMiddleName() : "")
											+ (student.getAdmAppln().getPersonalData()
													.getLastName() != null ? student
															.getAdmAppln().getPersonalData()
															.getLastName() : ""));
					newSupplementaryImpApplicationForm.setClassName(student
							.getClassSchemewise() != null ? student
									.getClassSchemewise().getClasses().getName() : "");
					newSupplementaryImpApplicationForm.setRegNo(student
							.getRegisterNo());
					newSupplementaryImpApplicationForm.setRollNo(student
							.getRollNo());
					newSupplementaryImpApplicationForm.setDob(null);
					newSupplementaryImpApplicationForm
					.setOriginalDob(CommonUtil
							.ConvertStringToDateFormat(
									CommonUtil.getStringDate(student
											.getAdmAppln()
											.getPersonalData()
											.getDateOfBirth()),
											NewStudentCertificateCourseAction.SQL_DATEFORMAT,
											NewStudentCertificateCourseAction.FROM_DATEFORMAT));
					newSupplementaryImpApplicationForm.setAddress("");
					newSupplementaryImpApplicationForm.setCourseName(student
							.getAdmAppln().getCourseBySelectedCourseId()
							.getName());
					newSupplementaryImpApplicationForm.setCourseDep(student
							.getAdmAppln().getCourseBySelectedCourseId()
							.getCode());
					newSupplementaryImpApplicationForm.setSchemeNo(""
							+ student.getClassSchemewise().getClasses()
							.getTermNumber());
					newSupplementaryImpApplicationForm.setEmail(student
							.getAdmAppln().getPersonalData().getEmail());
					newSupplementaryImpApplicationForm.setMobileNo(student
							.getAdmAppln().getPersonalData().getMobileNo2());
					newSupplementaryImpApplicationForm.setCourseId(""
							+ student.getClassSchemewise().getClasses()
							.getCourse().getId());

					newSupplementaryImpApplicationForm.setDate(CommonUtil
							.getTodayDate());
					newSupplementaryImpApplicationForm.setGender(student
							.getAdmAppln().getPersonalData().getGender());
					String address = "";
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine1() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine1()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressLine1() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine2() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine2()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressLine2() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByParentAddressDistrictId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByParentAddressDistrictId()
						.getName() + ",";
					} else if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressStateOthers() + ",";

					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByPermanentAddressStateId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByPermanentAddressStateId()
						.getName();
					} else if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressStateOthers();

					}
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressZipCode() != null)
						newSupplementaryImpApplicationForm
						.setPermanentAddressZipCode(student
								.getAdmAppln().getPersonalData()
								.getPermanentAddressZipCode());

					newSupplementaryImpApplicationForm.setAddress(address
							.toUpperCase());

					// vinodha
					newSupplementaryImpApplicationForm
					.setCommunicationEmail(student.getAdmAppln()
							.getPersonalData().getEmail());
					newSupplementaryImpApplicationForm
					.setCommunicationMobileNo(student.getAdmAppln()
							.getPersonalData().getMobileNo2());

					String communicationAddress = "";
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1().equalsIgnoreCase(
							"")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2().equalsIgnoreCase(
							"")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressDistrictId() != null) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId()
						.getName() + ",";
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() + ",";

					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressStateId() != null) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId()
						.getName();
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers();
					}
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressZipCode() != null)
						newSupplementaryImpApplicationForm
						.setCommunicationAddressZipCode(student
								.getAdmAppln().getPersonalData()
								.getCurrentAddressZipCode());

					newSupplementaryImpApplicationForm
					.setCommunicationAddress(communicationAddress
							.toUpperCase());

					if (student.getAdmAppln().getPersonalData()
							.getReligionSection() != null)
						newSupplementaryImpApplicationForm
						.setFeeConcessionCategory(student.getAdmAppln()
								.getPersonalData().getReligionSection()
								.getName());

					if (student.getAdmAppln().getPersonalData()
							.getReligionSection() != null)
						if (student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("sc")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("st")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("OEC")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("FMN")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("KPCR")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("SEBC")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("GENERAL")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("OBC (Non-Creamy)"))
							newSupplementaryImpApplicationForm
							.setFeeConcession("YES");
						else
							newSupplementaryImpApplicationForm
							.setFeeConcession("NO");

					String religion = "";
					if (student.getAdmAppln().getPersonalData().getReligion() != null)
						religion = religion
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getReligion().getName();
					else if (student.getAdmAppln().getPersonalData()
							.getReligionOthers() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getReligionOthers();
					if (student.getAdmAppln().getPersonalData().getCaste() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getCaste().getName();
					else if (student.getAdmAppln().getPersonalData()
							.getCasteOthers() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getCasteOthers();

					newSupplementaryImpApplicationForm.setReligion(religion);

					String careTaker = "";
					String fatherTitle = "";
					String motherTitle = "";
					AdmapplnAdditionalInfo addInfoAdmAppln = NewSupplementaryImpApplicationHandler
					.getInstance().getTitleForCareTaker(
							student.getAdmAppln().getId());
					if (addInfoAdmAppln != null) {
						if (addInfoAdmAppln.getTitleFather() != null
								&& !addInfoAdmAppln.getTitleFather().isEmpty())
							fatherTitle = "Father";
						if (addInfoAdmAppln.getTitleMother() != null
								&& !addInfoAdmAppln.getTitleMother().isEmpty())
							if (addInfoAdmAppln.getTitleMother()
									.equalsIgnoreCase("sr"))
								motherTitle = "Sister";
							else
								motherTitle = "Mother";
					}
					if (student.getAdmAppln().getPersonalData().getFatherName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getFatherName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getFatherName()
						+ "," + fatherTitle;
					else if (student.getAdmAppln().getPersonalData()
							.getMotherName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getMotherName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getMotherName()
						+ "," + motherTitle;
					else if (student.getAdmAppln().getPersonalData()
							.getGuardianName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getGuardianName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getGuardianName()
						+ ",Guardian";

					newSupplementaryImpApplicationForm.setCareTaker(careTaker
							.toUpperCase());
				}

			}

		}
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showPrintDetailsForRegular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean dup1 = false;
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;

		boolean dup = NewSupplementaryImpApplicationHandler.getInstance()
		.checkDuplication(newSupplementaryImpApplicationForm);
		if (!dup) {
			dup1 = NewSupplementaryImpApplicationHandler.getInstance()
			.addAppliedStudent(newSupplementaryImpApplicationForm);
		}
		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping.findForward("printDetailsforRegularUG");
		} else {
			return mapping.findForward("printDetailsforRegularPG");
		}

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showPrintChallanForRegular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;

		return mapping.findForward("printDetailsChallanRegular");

	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showPrintChallanForSupply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;

		return mapping.findForward("printDetailsChallanSupply");
	}

	private void validatePgi(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			ActionErrors errors) {
		if (newSupplementaryImpApplicationForm.getSelectedFeePayment() != null
				&& !newSupplementaryImpApplicationForm.getSelectedFeePayment()
				.isEmpty()) {
			if (newSupplementaryImpApplicationForm.getSelectedFeePayment()
					.equalsIgnoreCase("online")) {
				if ((newSupplementaryImpApplicationForm.getApplicationAmount1() == null || newSupplementaryImpApplicationForm
						.getApplicationAmount1().isEmpty())) {
					if (errors
							.get(CMSConstants.REGULAR_EXAM_APPLICATIONAMNT_REQUIRED) != null
							&& !errors
							.get(
									CMSConstants.REGULAR_EXAM_APPLICATIONAMNT_REQUIRED)
									.hasNext()) {
						errors
						.add(
								CMSConstants.REGULAR_EXAM_APPLICATIONAMNT_REQUIRED,
								new ActionError(
										CMSConstants.REGULAR_EXAM_APPLICATIONAMNT_REQUIRED));
					}
				}
				// added by giri
				if (CMSConstants.PGI_MERCHANT_ID == null
						|| CMSConstants.PGI_MERCHANT_ID.isEmpty()
						|| CMSConstants.PGI_MERCHANT_ID == "") {
					if (errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED) != null
							&& !errors.get(
									CMSConstants.PGI_MERCHANT_ID_REQUIRED)
									.hasNext()) {
						errors.add(CMSConstants.PGI_MERCHANT_ID_REQUIRED,
								new ActionError(
										CMSConstants.PGI_MERCHANT_ID_REQUIRED));
					}
				}
				if (CMSConstants.PGI_SECURITY_ID == null
						|| CMSConstants.PGI_SECURITY_ID.isEmpty()
						|| CMSConstants.PGI_SECURITY_ID == "") {
					if (errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED) != null
							&& !errors.get(
									CMSConstants.PGI_SECURITY_ID_REQUIRED)
									.hasNext()) {
						errors.add(CMSConstants.PGI_SECURITY_ID_REQUIRED,
								new ActionError(
										CMSConstants.PGI_SECURITY_ID_REQUIRED));
					}
				}
				if (CMSConstants.PGI_CHECKSUM_KEY == null
						|| CMSConstants.PGI_CHECKSUM_KEY.isEmpty()
						|| CMSConstants.PGI_CHECKSUM_KEY == "") {
					if (errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED) != null
							&& !errors.get(
									CMSConstants.PGI_CHECKSUM_KEY_REQUIRED)
									.hasNext()) {
						errors
						.add(
								CMSConstants.PGI_CHECKSUM_KEY_REQUIRED,
								new ActionError(
										CMSConstants.PGI_CHECKSUM_KEY_REQUIRED));
					}
				}
			}
			if (newSupplementaryImpApplicationForm.getSelectedFeePayment()
					.equalsIgnoreCase("SBI")) {

				if (newSupplementaryImpApplicationForm.getJournalNo() == null
						|| StringUtils
						.isEmpty(newSupplementaryImpApplicationForm
								.getJournalNo())) {

					if (errors
							.get(CMSConstants.ADMISSIONFORM_CONFIRM_JOURNALNO_REQUIRED) != null
							&& !errors
							.get(
									CMSConstants.ADMISSIONFORM_CONFIRM_JOURNALNO_REQUIRED)
									.hasNext()) {
						errors
						.add(
								CMSConstants.ADMISSIONFORM_CONFIRM_JOURNALNO_REQUIRED,
								new ActionError(
										CMSConstants.ADMISSIONFORM_CONFIRM_JOURNALNO_REQUIRED));
					}

				}
				if (newSupplementaryImpApplicationForm.getApplicationDate() == null
						|| StringUtils
						.isEmpty(newSupplementaryImpApplicationForm
								.getApplicationDate())) {

					if (errors
							.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_REQUIRED) != null
							&& !errors
							.get(
									CMSConstants.ADMISSIONFORM_APPLICATIONDT_REQUIRED)
									.hasNext()) {
						errors
						.add(
								CMSConstants.ADMISSIONFORM_APPLICATIONDT_REQUIRED,
								new ActionError(
										CMSConstants.ADMISSIONFORM_APPLICATIONDT_REQUIRED));
					}

				}
				if (newSupplementaryImpApplicationForm.getApplicationAmount() == null
						|| StringUtils
						.isEmpty(newSupplementaryImpApplicationForm
								.getApplicationAmount())) {

					if (errors
							.get(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED) != null
							&& !errors
							.get(
									CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED)
									.hasNext()) {
						errors
						.add(
								CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED,
								new ActionError(
										CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_REQUIRED));
					}

				}
				if (newSupplementaryImpApplicationForm.getApplicationDate() != null
						&& !StringUtils
						.isEmpty(newSupplementaryImpApplicationForm
								.getApplicationDate())) {
					if (CommonUtil
							.isValidDate(newSupplementaryImpApplicationForm
									.getApplicationDate())) {
						boolean isValid = validatefutureDate(newSupplementaryImpApplicationForm
								.getApplicationDate());
						if (!isValid) {
							if (errors
									.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE) != null
									&& !errors
									.get(
											CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE)
											.hasNext()) {
								errors
								.add(
										CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE,
										new ActionError(
												CMSConstants.ADMISSIONFORM_APPLICATIONDT_FUTURE));
							}
						}
					} else {
						if (errors
								.get(CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID) != null
								&& !errors
								.get(
										CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID)
										.hasNext()) {
							errors
							.add(
									CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID,
									new ActionError(
											CMSConstants.ADMISSIONFORM_APPLICATIONDT_INVALID));
						}
					}
				}
				if (newSupplementaryImpApplicationForm.getApplicationAmount() != null
						&& !StringUtils
						.isEmpty(newSupplementaryImpApplicationForm
								.getApplicationAmount().trim())) {
					if (!CommonUtil
							.isValidDecimal(newSupplementaryImpApplicationForm
									.getApplicationAmount().trim())) {
						if (errors
								.get(CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_INVALID) != null
								&& !errors
								.get(
										CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_INVALID)
										.hasNext()) {
							errors
							.add(
									CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_INVALID,
									new ActionError(
											CMSConstants.ADMISSIONFORM_APPLICATIONAMNT_INVALID));
						}
					}
				}
			}

		}
		// end by giri
	}

	public static boolean validatefutureDate(String dateString) {
		log.info("enter validatefutureDate..");
		String formattedString = CommonUtil.ConvertStringToDateFormat(
				dateString,
				NewSupplementaryImpApplicationAction.FROM_DATEFORMAT,
				NewSupplementaryImpApplicationAction.TO_DATEFORMAT);
		Date date = new Date(formattedString);
		Date curdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(curdate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date origdate = cal.getTime();
		log.info("exit validatefutureDate..");
		return !(date.compareTo(origdate) == 1);
	}

	public ActionForward redirectToRegularExamApplicationPGI(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("enter redirectToRegularExamApplicationPGI...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		boolean dup = NewSupplementaryImpApplicationHandler.getInstance()
		.checkDuplication(newSupplementaryImpApplicationForm);
		if (!dup) {
			if (newSupplementaryImpApplicationForm.getApplicationAmount1() != null)
				newSupplementaryImpApplicationForm
				.setApplicationAmount(newSupplementaryImpApplicationForm
						.getApplicationAmount1());
			validatePgi(newSupplementaryImpApplicationForm, errors);
			try {
				if (errors != null && !errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping
					.findForward(CMSConstants.REGULAR_APP_STUDENT_PAYMENT);
				}
				String hash = NewSupplementaryImpApplicationHandler
				.getInstance().getParameterForPGI(
						newSupplementaryImpApplicationForm);
				// request.setAttribute("pgiMsg", msg);
				request.setAttribute("hash", hash);
				request.setAttribute("txnid",
						newSupplementaryImpApplicationForm.getRefNo());
				request.setAttribute("productinfo",
						newSupplementaryImpApplicationForm.getProductinfo());
				request.setAttribute("amount",
						newSupplementaryImpApplicationForm
						.getApplicationAmount());
				request.setAttribute("email",
						newSupplementaryImpApplicationForm.getEmail());
				request.setAttribute("firstname",
						newSupplementaryImpApplicationForm.getNameOfStudent());
				request.setAttribute("phone",
						newSupplementaryImpApplicationForm.getStudentObj()
						.getAdmAppln().getPersonalData().getMobileNo1()
						+ ""
						+ newSupplementaryImpApplicationForm
						.getStudentObj().getAdmAppln()
						.getPersonalData().getMobileNo2());
				request.setAttribute("test", newSupplementaryImpApplicationForm
						.getTest());
				request.setAttribute("surl",
						CMSConstants.REG_EXAM_APP_PAYUMONEY_SUCCESSURL);
				request.setAttribute("furl",
						CMSConstants.REG_EXAM_APP_PAYUMONEY_FAILUREURL);

			} catch (Exception e) {
				log.error("error in redirectToRegularExamApplicationPGI...", e);
				// throw e;
				System.out
				.println("************************ error details in redirectToRegularExamApplicationPGI*************************"
						+ e);

				errors
				.add(
						"knowledgepro.admission.boardDetails.duplicateEntry",
						new ActionError(
								"knowledgepro.admission.boardDetails.duplicateEntry",
						"Error was occured, please login and enter details again"));
				saveErrors(request, errors);
				System.out.println("PGI ERROR" + e);
				return mapping.findForward("printDetailsforRegularFail");
			}
			return mapping.findForward(CMSConstants.REDIRECT_TO_PGI_PAGE);
		} else {
			if (newSupplementaryImpApplicationForm.getStudentObj()
					.getAdmAppln().getCourseBySelectedCourseId().getProgram()
					.getProgramType().getId() == 1) {
				return mapping.findForward("printDetailsforRegularUG");
			} else {
				return mapping.findForward("printDetailsforRegularPG");
			}
		}

	}

	public ActionForward updateRegExamAppPGIResponse(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out
		.println("In NewSupplementaryImpApplicationAction - updateRegExamAppPGIResponse method");
		log
		.info("enter updateRegExamAppPGIResponse-NewSupplementaryImpApplicationAction...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			boolean isUpdated = NewSupplementaryImpApplicationHandler
			.getInstance().updateResponse(
					newSupplementaryImpApplicationForm);
			if (newSupplementaryImpApplicationForm.getIsTnxStatusSuccess()) {
				// AdmissionFormHandler.getInstance().sendMailForOnlinePaymentConformation(admForm);
			}
			if (isUpdated
					&& newSupplementaryImpApplicationForm
					.getIsTnxStatusSuccess()) {
				boolean result = NewSupplementaryImpApplicationHandler
				.getInstance().addAppliedStudent(
						newSupplementaryImpApplicationForm);
				if (result) {
					if (newSupplementaryImpApplicationForm.getStudentObj()
							.getAdmAppln().getCourseBySelectedCourseId()
							.getProgram().getProgramType().getId() == 1) {
						return mapping
						.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
					} else {
						return mapping
						.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
					}
				} else {
					return mapping.findForward("printDetailsforRegularFail");
				}
			} else {
				errors.add("error", new ActionError(
				"knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
				return mapping.findForward("printDetailsforRegularFail");
			}
		} catch (BusinessException e) {
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
			return mapping.findForward("printDetailsforRegularFail");
		} catch (Exception e) {
			log
			.error(
					"error in updateRegExamAppPGIResponse-NewSupplementaryImpApplicationAction...",
					e);
			// throw e;
			System.out
			.println("************************ error details in online regularApplication in updateRegExamAppPGIResponse*************************"
					+ e);

			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			System.out.println("PGI ERROR" + e);
			return mapping.findForward("printDetailsforRegularFail");

		}

	}

	public ActionForward submitRegularExamApplicationChallan(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out
		.println("In NewSupplementaryImpApplicationAction - updateRegExamAppPGIResponse method");
		log
		.info("enter updateRegExamAppPGIResponse-NewSupplementaryImpApplicationAction...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		validatePgi(newSupplementaryImpApplicationForm, errors);
		try {
			boolean result = NewSupplementaryImpApplicationHandler
			.getInstance().addAppliedStudent(
					newSupplementaryImpApplicationForm);
			if (result) {
				if (newSupplementaryImpApplicationForm.getStudentObj()
						.getAdmAppln().getCourseBySelectedCourseId()
						.getProgram().getProgramType().getId() == 1) {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
				} else {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
				}
			} else {
				return mapping.findForward("printDetailsforRegularFail");
			}

		} catch (BusinessException e) {
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			log
			.error(
					"error in updateRegExamAppPGIResponse-NewSupplementaryImpApplicationAction...",
					e);
			// throw e;
			System.out
			.println("************************ error details in online regularApplication in updateRegExamAppPGIResponse*************************"
					+ e);

			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			System.out.println("PGI ERROR" + e);
			return mapping.findForward("printDetailsforRegularFail");
		}
		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		}
	}

	public ActionForward redirectToSupplementaryExamApplicationPGI(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("enter redirectToSupplementaryExamApplicationPGI...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		boolean dup = NewSupplementaryImpApplicationHandler.getInstance()
		.checkDuplicationForSuppl(newSupplementaryImpApplicationForm);
		if (!dup) {
			if (newSupplementaryImpApplicationForm.getApplicationAmount1() != null)
				newSupplementaryImpApplicationForm
				.setApplicationAmount(newSupplementaryImpApplicationForm
						.getApplicationAmount1());
			validatePgi(newSupplementaryImpApplicationForm, errors);
			try {
				if (errors != null && !errors.isEmpty()) {
					saveErrors(request, errors);
					return mapping
					.findForward(CMSConstants.SUPPLEMENTARY_APP_STUDENT_PAYMENT);
				}
				String hash = NewSupplementaryImpApplicationHandler
				.getInstance().getParameterForPGIForSuppl(
						newSupplementaryImpApplicationForm);
				// request.setAttribute("pgiMsg", msg);
				request.setAttribute("hash", hash);
				request.setAttribute("txnid",
						newSupplementaryImpApplicationForm.getRefNo());
				request.setAttribute("productinfo",
						newSupplementaryImpApplicationForm.getProductinfo());
				request.setAttribute("amount",
						newSupplementaryImpApplicationForm
						.getApplicationAmount());
				request.setAttribute("email",
						newSupplementaryImpApplicationForm.getEmail());
				request.setAttribute("firstname",
						newSupplementaryImpApplicationForm.getNameOfStudent());
				request.setAttribute("phone",
						newSupplementaryImpApplicationForm.getStudentObj()
						.getAdmAppln().getPersonalData().getMobileNo1()
						+ ""
						+ newSupplementaryImpApplicationForm
						.getStudentObj().getAdmAppln()
						.getPersonalData().getMobileNo2());
				request.setAttribute("test", newSupplementaryImpApplicationForm
						.getTest());
				request.setAttribute("surl",
						CMSConstants.SUP_EXAM_APP_PAYUMONEY_SUCCESSURL);
				request.setAttribute("furl",
						CMSConstants.SUP_EXAM_APP_PAYUMONEY_FAILUREURL);

			} catch (Exception e) {
				log.error("error in redirectToRegularExamApplicationPGI...", e);
				// throw e;
				System.out
				.println("************************ error details in redirectToRegularExamApplicationPGI*************************"
						+ e);

				errors
				.add(
						"knowledgepro.admission.boardDetails.duplicateEntry",
						new ActionError(
								"knowledgepro.admission.boardDetails.duplicateEntry",
						"Error was occured, please login and enter details again"));
				saveErrors(request, errors);
				System.out.println("PGI ERROR" + e);
				return mapping.findForward("printDetailsforRegularFail");
			}
			return mapping.findForward(CMSConstants.REDIRECT_TO_PGI_PAGE);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}

	}

	public ActionForward updateSupExamAppPGIResponse(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log
		.info("enter updateSupExamAppPGIResponse-NewSupplementaryImpApplicationAction...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			boolean isUpdated = NewSupplementaryImpApplicationHandler
			.getInstance().updateResponseForSuppl(
					newSupplementaryImpApplicationForm);

			if (isUpdated
					&& newSupplementaryImpApplicationForm
					.getIsTnxStatusSuccess()) {
				boolean result = NewSupplementaryImpApplicationHandler
				.getInstance().addAppliedStudentForSuppl(
						newSupplementaryImpApplicationForm);
				if (result) {
					newSupplementaryImpApplicationForm.setChallanButton(true);
					newSupplementaryImpApplicationForm.setDisplayButton(false);
					return mapping
					.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
				} else {
					return mapping
					.findForward("printDetailsforSupplementaryFail");
				}
			} else {
				errors.add("error", new ActionError(
				"knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
				return mapping.findForward("printDetailsforSupplementaryFail");
			}
		} catch (BusinessException e) {
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
			return mapping.findForward("printDetailsforSupplementaryFail");
		} catch (Exception e) {
			log
			.error(
					"error in updateRegExamAppPGIResponse-NewSupplementaryImpApplicationAction...",
					e);
			// throw e;
			System.out
			.println("************************ error details in online regularApplication in updateRegExamAppPGIResponse*************************"
					+ e);

			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			System.out.println("PGI ERROR" + e);
			return mapping.findForward("printDetailsforSupplementaryFail");

		}

	}

	public ActionForward submitSupplementaryExamApplicationChallan(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out
		.println("In NewSupplementaryImpApplicationAction - submitSupplementaryExamApplicationChallan method");
		log
		.info("enter submitSupplementaryExamApplicationChallan-NewSupplementaryImpApplicationAction...");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		validatePgi(newSupplementaryImpApplicationForm, errors);
		try {
			boolean result = NewSupplementaryImpApplicationHandler
			.getInstance().addAppliedStudentForSuppl(
					newSupplementaryImpApplicationForm);
			if (result) {
				ActionMessage message = new ActionMessage(
				"knowledgepro.admin.challan.submit.application");
				messages.add("messages", message);
				saveMessages(request, messages);
				newSupplementaryImpApplicationForm.setChallanButton(false);
				newSupplementaryImpApplicationForm.setDisplayButton(false);
				return mapping
				.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
			} else {
				return mapping.findForward("printDetailsforSupplementaryFail");
			}

		} catch (BusinessException e) {
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			log
			.error(
					"error in submitSupplementaryExamApplicationChallan-NewSupplementaryImpApplicationAction...",
					e);
			// throw e;
			System.out
			.println("************************ error details in online regularApplication in submitSupplementaryExamApplicationChallan*************************"
					+ e);

			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			System.out.println("PGI ERROR" + e);
			return mapping.findForward("printDetailsforSupplementaryFail");
		}
		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);

	}

	public ActionForward checkRegularApplicationForPreviousExam(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setIsPreviousExam(session
				.getAttribute("isPrevApplicationAvailable").toString());
		newSupplementaryImpApplicationForm.setPrevClassExamId(session
				.getAttribute("prevClassExamId").toString());

		if (newSupplementaryImpApplicationForm.getIsPreviousExam()
				.equalsIgnoreCase("yes"))
			newSupplementaryImpApplicationForm.setPreviousExam(true);
		setUserId(request, newSupplementaryImpApplicationForm);
		newSupplementaryImpApplicationForm.setPaymentSuccess(false);
		newSupplementaryImpApplicationForm.resetFields();
		setDataToFormForRegular(newSupplementaryImpApplicationForm, request);

		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		}
	}

	// temp purpose added by Ashwini
	public ActionForward checkRegularApplicationForStudentLoginTemp(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		HttpSession session = request.getSession(true);
		setUserId(request, newSupplementaryImpApplicationForm);
		newSupplementaryImpApplicationForm.setPaymentSuccess(false);
		newSupplementaryImpApplicationForm.setPrint(false);
		// newSupplementaryImpApplicationForm.resetFields();
		boolean add = setDataToFormForRegularTemp(
				newSupplementaryImpApplicationForm, request);
		if (add) {

			ActionMessages messages = new ActionMessages();
			ActionMessage message = new ActionMessage(
			"knowledgepro.admin.challan.submit.application");
			messages.add("messages", message);
			saveMessages(request, messages);

		}

		return mapping
		.findForward(CMSConstants.REGULAR_APPLICATION_RESULT_EDIT);

	}

	public ActionForward printRegularApplication(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		return mapping
		.findForward(CMSConstants.REGULAR_APPLICATION_RESULT_EDIT);

	}

	public ActionForward editRegularApplication(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		return mapping
		.findForward(CMSConstants.REGULAR_APPLICATION_RESULT_EDIT);

	}

	public ActionForward printRegularApplicationApp(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		newSupplementaryImpApplicationForm.setPrintSupplementary(true);
		return mapping.findForward(CMSConstants.REGULAR_APPLICATION_RESULT);

	}

	private boolean setDataToFormForRegularTemp(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		boolean add = false;

		List<Student> previousList = new ArrayList<Student>();
		String stuQuery = "from Student s where s.classSchemewise.classes.id="
			+ newSupplementaryImpApplicationForm.getClassCodeIdsFrom();
		List<Student> StuList = PropertyUtil.getInstance()
		.getListOfStudentData(stuQuery);

		String previousQuery = "select s.student from StudentPreviousClassHistory s where "
			+ "(s.student.isHide = 0 or s.student.isHide is null) and s.student.id not in (select e.student.id from ExamStudentDetentionRejoinDetails e where e.discontinued=1 ) and s.student.admAppln.isCancelled=0"
			+ " and s.classes.id="
			+ newSupplementaryImpApplicationForm.getClassCodeIdsFrom()
			+ " group by s.student.id ";
		previousList = PropertyUtil.getInstance().getListOfStudentData(
				previousQuery);

		List<StudentTO> studentList = new ArrayList<StudentTO>();
		List<ExamRegularApplication> examRegularApplicationList = new ArrayList<ExamRegularApplication>();
		List<ExamRegularApplication> prevExamRegularApplicationList = new ArrayList<ExamRegularApplication>();
		StuList.addAll((Collection<? extends Student>) previousList);
		List<Integer> stuIds = new ArrayList<Integer>();
		Iterator studentListItr = StuList.iterator();
		while (studentListItr.hasNext()) {
			Student stu = (Student) studentListItr.next();
			// stuIds.add(stu.getId());
		}
		// stuIds.add(379);
		INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl
		.getInstance();
		// Map<Integer,byte[]>
		// photoMap=transaction.getStudentPhtosMap(stuIds,false);
		if (StuList.size() > 0) {
			for (Student student : StuList) {
				// if(student.getId()==274 || student.getId()==275){
				System.out.println(student.getId());
				LoginForm loginForm = new LoginForm();

				newSupplementaryImpApplicationForm
				.setStudentId(student.getId());
				ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl
				.getInstance();
				newSupplementaryImpApplicationForm.setStudentObj(student);
				List<Integer> examIds = new ArrayList<Integer>();
				int examId11 = Integer
				.parseInt(newSupplementaryImpApplicationForm
						.getExamId());

				examIds.add(examId11);
				newSupplementaryImpApplicationForm.setClassId(String
						.valueOf(newSupplementaryImpApplicationForm
								.getClassCodeIdsFrom()));
				IDownloadHallTicketTransaction txn1 = DownloadHallTicketTransactionImpl
				.getInstance();
				if (examIds.size() == 0 || examIds.get(0) == 0) {
					newSupplementaryImpApplicationForm
					.setRegularAppAvailable(false);
				}

				else {
					ExamBlockUnblockHallTicketBO h = null;
					if (examId11 > 0) {
						h = txn1.isHallTicketBlockedStudent(student.getId(),
								student.getClassSchemewise().getClasses()
								.getId(), examId11, "A");
					}
					if (h == null) {
						/*
						 * newSupplementaryImpApplicationForm.setRegularAppAvailable
						 * (false); }else{
						 */

						StudentTO studentTo = new StudentTO();
						newSupplementaryImpApplicationForm
						.setRegularAppAvailable(true);
						List<ExamSupplementaryImpApplicationSubjectTO> subjectList = NewSupplementaryImpApplicationHandler
						.getInstance()
						.getApplicationFormsForRegulartemp(examIds,
								newSupplementaryImpApplicationForm);
						if (subjectList.size() > 0)
							newSupplementaryImpApplicationForm.setPrint(true);
						if (student != null) {
							String STUDENT_IMAGE = "images/StudentPhotos/"
								+ student.getId()
								+ ".jpg?"
								+ student.getAdmAppln()
								.getLastModifiedDate();
							session
							.setAttribute("STUDENT_IMAGE",
									STUDENT_IMAGE);
							student.setStudentPhoto(STUDENT_IMAGE);
							studentTo.setId(student.getId());
							// studentTo.setStudentPhoto(photoMap.get(student.getId()));
							newSupplementaryImpApplicationForm
							.setCourseName(student.getAdmAppln()
									.getCourseBySelectedCourseId()
									.getName());
							// studentTo.setStudentPhoto(student.getAdmAppln().getApplnDocs().);
							studentTo.setStudentName(student.getAdmAppln()
									.getPersonalData().getFirstName()
									+ (student.getAdmAppln().getPersonalData()
											.getMiddleName() != null ? student
													.getAdmAppln().getPersonalData()
													.getMiddleName() : "")
													+ (student.getAdmAppln().getPersonalData()
															.getLastName() != null ? student
																	.getAdmAppln().getPersonalData()
																	.getLastName() : ""));

							studentTo
							.setDob(CommonUtil
									.ConvertStringToDateFormat(
											CommonUtil
											.getStringDate(student
													.getAdmAppln()
													.getPersonalData()
													.getDateOfBirth()),
													NewStudentCertificateCourseAction.SQL_DATEFORMAT,
													NewStudentCertificateCourseAction.FROM_DATEFORMAT));

							String religion = "";
							if (student.getAdmAppln().getPersonalData()
									.getReligion() != null)
								religion = religion
								+ ""
								+ student.getAdmAppln()
								.getPersonalData()
								.getReligion().getName();
							else if (student.getAdmAppln().getPersonalData()
									.getReligionOthers() != null)
								religion = religion
								+ ","
								+ student.getAdmAppln()
								.getPersonalData()
								.getReligionOthers();
							if (student.getAdmAppln().getPersonalData()
									.getCaste() != null)
								religion = religion
								+ ","
								+ student.getAdmAppln()
								.getPersonalData().getCaste()
								.getName();
							else if (student.getAdmAppln().getPersonalData()
									.getCasteOthers() != null)
								religion = religion
								+ ","
								+ student.getAdmAppln()
								.getPersonalData()
								.getCasteOthers();

							studentTo.setReligion(religion);

							String careTaker = "";
							String fatherTitle = "";
							String motherTitle = "";
							AdmapplnAdditionalInfo addInfoAdmAppln = NewSupplementaryImpApplicationHandler
							.getInstance().getTitleForCareTaker(
									student.getAdmAppln().getId());
							if (addInfoAdmAppln != null) {
								if (addInfoAdmAppln.getTitleFather() != null
										&& !addInfoAdmAppln.getTitleFather()
										.isEmpty())
									fatherTitle = "Father";
								if (addInfoAdmAppln.getTitleMother() != null
										&& !addInfoAdmAppln.getTitleMother()
										.isEmpty())
									if (addInfoAdmAppln.getTitleMother()
											.equalsIgnoreCase("sr"))
										motherTitle = "Sister";
									else
										motherTitle = "Mother";
							}

							if (student.getAdmAppln().getPersonalData()
									.getFatherName() != null
									&& !student.getAdmAppln().getPersonalData()
									.getFatherName().isEmpty())
								careTaker = student.getAdmAppln()
								.getPersonalData().getFatherName()
								+ "," + fatherTitle;
							else if (student.getAdmAppln().getPersonalData()
									.getMotherName() != null
									&& !student.getAdmAppln().getPersonalData()
									.getMotherName().isEmpty())
								careTaker = student.getAdmAppln()
								.getPersonalData().getMotherName()
								+ "," + motherTitle;
							else if (student.getAdmAppln().getPersonalData()
									.getGuardianName() != null
									&& !student.getAdmAppln().getPersonalData()
									.getGuardianName().isEmpty())
								careTaker = student.getAdmAppln()
								.getPersonalData().getGuardianName()
								+ ",Guardian";

							studentTo.setCareTaker(careTaker);

							String communicationAddress = "";
							if (student.getAdmAppln().getPersonalData()
									.getCurrentAddressLine1() != null
									&& !student.getAdmAppln().getPersonalData()
									.getCurrentAddressLine1()
									.equalsIgnoreCase("")) {
								communicationAddress = communicationAddress
								+ ""
								+ student.getAdmAppln()
								.getPersonalData()
								.getCurrentAddressLine1() + ",";
							}
							if (student.getAdmAppln().getPersonalData()
									.getCurrentAddressLine2() != null
									&& !student.getAdmAppln().getPersonalData()
									.getCurrentAddressLine2()
									.equalsIgnoreCase("")) {
								communicationAddress = communicationAddress
								+ ""
								+ student.getAdmAppln()
								.getPersonalData()
								.getCurrentAddressLine2() + ",";
							}
							if (student.getAdmAppln().getPersonalData()
									.getStateByCurrentAddressDistrictId() != null) {
								communicationAddress = communicationAddress
								+ ""
								+ student
								.getAdmAppln()
								.getPersonalData()
								.getStateByCurrentAddressDistrictId()
								.getName() + ",";
							} else if (student.getAdmAppln().getPersonalData()
									.getCurrentAddressStateOthers() != null
									&& !student.getAdmAppln().getPersonalData()
									.getCurrentAddressStateOthers()
									.equalsIgnoreCase("")) {
								communicationAddress = communicationAddress
								+ ""
								+ student.getAdmAppln()
								.getPersonalData()
								.getCurrentAddressStateOthers()
								+ ",";

							}
							if (student.getAdmAppln().getPersonalData()
									.getStateByCurrentAddressStateId() != null) {
								communicationAddress = communicationAddress
								+ ""
								+ student
								.getAdmAppln()
								.getPersonalData()
								.getStateByCurrentAddressStateId()
								.getName();
							} else if (student.getAdmAppln().getPersonalData()
									.getCurrentAddressStateOthers() != null
									&& !student.getAdmAppln().getPersonalData()
									.getCurrentAddressStateOthers()
									.equalsIgnoreCase("")) {
								communicationAddress = communicationAddress
								+ ""
								+ student.getAdmAppln()
								.getPersonalData()
								.getCurrentAddressStateOthers();
							}

							studentTo
							.setCommunicationAddress(communicationAddress
									.toUpperCase());

							if (student.getAdmAppln().getPersonalData()
									.getCurrentAddressZipCode() != null)
								studentTo
								.setCommunicationAddressZipCode(student
										.getAdmAppln()
										.getPersonalData()
										.getCurrentAddressZipCode());

							studentTo.setCommunicationMobileNo(student
									.getAdmAppln().getPersonalData()
									.getMobileNo2());

							studentTo.setRegisterNo(student.getRegisterNo());

							if (student.getAdmAppln().getPersonalData()
									.getReligionSection() != null)
								newSupplementaryImpApplicationForm
								.setFeeConcessionCategory(student
										.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName());

							if (student.getAdmAppln().getPersonalData()
									.getReligionSection() != null)
								if (student.getAdmAppln().getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("sc")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("st")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("OEC")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("FMN")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("KPCR")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase("SEBC")
										|| student.getAdmAppln()
										.getPersonalData()
										.getReligionSection().getName()
										.equalsIgnoreCase(
										"OBC (Non-Creamy)"))
									newSupplementaryImpApplicationForm
									.setFeeConcession("YES");
								else
									newSupplementaryImpApplicationForm
									.setFeeConcession("NO");
							studentTo.setGender(student.getAdmAppln()
									.getPersonalData().getGender());
							// studentTo.setIsAttendanceShortage(newSupplementaryImpApplicationForm.getIsAttendanceShortage());
							if (!newSupplementaryImpApplicationForm
									.getIsAttendanceShortage())
								studentList.add(studentTo);

							// regualr app bo

							ExamRegularApplication er = new ExamRegularApplication();
							// if(!stuList.contains(student.getId())){
							if (!newSupplementaryImpApplicationForm
									.getIsAttendanceShortage()) {
								if (newSupplementaryImpApplicationForm
										.isRegExamFeesExempted()) {
									er = new ExamRegularApplication();
									Classes c = new Classes();
									c
									.setId(Integer
											.parseInt(newSupplementaryImpApplicationForm
													.getClassId()));
									er.setClasses(c);

									ExamDefinitionBO e = new ExamDefinitionBO();
									e
									.setId(Integer
											.parseInt(newSupplementaryImpApplicationForm
													.getExamId()));
									er.setExam(e);

									Student s = new Student();
									s.setId(newSupplementaryImpApplicationForm
											.getStudentObj().getId());
									er.setStudent(s);

									er.setIsApplied(true);
									er
									.setCreatedBy(newSupplementaryImpApplicationForm
											.getUserId());
									er.setCreatedDate(new Date());
									er
									.setModifiedBy(newSupplementaryImpApplicationForm
											.getUserId());
									er.setLastModifiedDate(new Date());
									er.setChallanVerified(false);
									er.setChallanNo("FeesExempted");
									er.setMode("FeesExempted");
									Date dateTime = er.getCreatedDate();
									er.setIsTokenRegisterd(false);
									newSupplementaryImpApplicationForm
									.setApplicationDate(DateFormatUtils
											.format(dateTime,
											"dd/MM/yyyy")
											.toString());
									// add=transaction.addAppliedStudent(er);
								} else {

									Classes c = new Classes();
									// if(!newSupplementaryImpApplicationForm.getPreviousExam())
									c
									.setId(Integer
											.parseInt(newSupplementaryImpApplicationForm
													.getClassId()));

									er.setClasses(c);

									ExamDefinitionBO e = new ExamDefinitionBO();
									// if(!newSupplementaryImpApplicationForm.getPreviousExam())
									e
									.setId(Integer
											.parseInt(newSupplementaryImpApplicationForm
													.getExamId()));

									er.setExam(e);

									Student s = new Student();
									s.setId(newSupplementaryImpApplicationForm
											.getStudentObj().getId());
									er.setStudent(s);

									er.setIsApplied(true);
									er
									.setCreatedBy(newSupplementaryImpApplicationForm
											.getUserId());
									er.setCreatedDate(new Date());
									er
									.setModifiedBy(newSupplementaryImpApplicationForm
											.getUserId());
									er.setLastModifiedDate(new Date());
									Date dateTime = er.getCreatedDate();
									er.setIsTokenRegisterd(false);
									// newSupplementaryImpApplicationForm.setApplicationDate(DatenewSupplementaryImpApplicationFormatUtils.newSupplementaryImpApplicationFormat(dateTime,
									// "dd/MM/yyyy").toString());
									// add=transaction.addAppliedStudent(er);

								}
								if (er != null)
									examRegularApplicationList.add(er);
							}
						}

					}

					// }

				}
			}

		}
		if (examRegularApplicationList.size() > 0) {
			INewSupplementaryImpApplicationTransaction tx = NewSupplementaryImpApplicationTransactionImpl
			.getInstance();
			tx.addAppliedStudentsForReg(examRegularApplicationList, Integer
					.parseInt(newSupplementaryImpApplicationForm.getExamId()),
					Integer.parseInt(newSupplementaryImpApplicationForm
							.getClassId()));
		}
		Collections.sort(studentList);
		newSupplementaryImpApplicationForm.setStuList(studentList);
		return add;
	}

	public ActionForward initRegApplication(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		newSupplementaryImpApplicationForm.resetFields();
		setRequiredDatatoForm(newSupplementaryImpApplicationForm);
		try {

		} catch (Exception e) {
		}
		return mapping.findForward(CMSConstants.TEMP_REGULAR);
	}

	public ActionForward getClassNameByExamName(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		Map<Integer, String> examMap = new HashMap<Integer, String>();
		Map<Integer, String> classMap = new HashMap<Integer, String>();
		newSupplementaryImpApplicationForm.setExamNameMap(CommonAjaxHandler
				.getInstance().getExamNameByExamTypeAndYear(
						newSupplementaryImpApplicationForm.getExamType(),
						Integer.parseInt(newSupplementaryImpApplicationForm
								.getYear())));
		if (newSupplementaryImpApplicationForm.getExamId().length() != 0) {
			int examId = Integer.parseInt(newSupplementaryImpApplicationForm
					.getExamId());
			try {

				classMap = CommonAjaxHandler.getInstance()
				.getClassNameByExamName(examId);
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		newSupplementaryImpApplicationForm.setMapClass(classMap);

		return mapping.findForward(CMSConstants.TEMP_REGULAR);
	}

	public ActionForward checkRevaluationApplicationForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		newSupplementaryImpApplicationForm.resetFields();
		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setProgramTypeName((String) session
				.getAttribute("programType").toString());
		newSupplementaryImpApplicationForm.setRevclassid(Integer
				.parseInt((String) session.getAttribute("revclassId")
						.toString()));
		newSupplementaryImpApplicationForm.setRevExamId((String) session
				.getAttribute("revAppExamId").toString());
		newSupplementaryImpApplicationForm
		.setPrevSemNo(Integer.parseInt((String) session.getAttribute(
		"prevSemNo").toString()));
		newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		session.setAttribute("isSupplementaryApplication", false);
		Integer revClassId = (Integer) session.getAttribute("revclassId");
		newSupplementaryImpApplicationForm.setRevclassid(revClassId);
		newSupplementaryImpApplicationForm.setSupplementary(false);
		setDataToFormForRevaluation(newSupplementaryImpApplicationForm, request);

		SimpleDateFormat df = new SimpleDateFormat();
		newSupplementaryImpApplicationForm.setDateOfApplication(df
				.format(new Date()));
		newSupplementaryImpApplicationForm.setIsRevaluation("");
		newSupplementaryImpApplicationForm.setIsScrutiny("");
		return mapping
		.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
	}

	private void setDataToFormForRevaluation(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			HttpServletRequest request) throws Exception {

		try {
			HttpSession session = request.getSession(false);
			newSupplementaryImpApplicationForm.setStudentId(Integer
					.parseInt(session.getAttribute("studentId").toString()));
			// newSupplementaryImpApplicationForm.setRevclassid(Integer.parseInt(session.getAttribute("revclassId").toString()));
			ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl
			.getInstance();
			Student student = (Student) txn.getMasterEntryDataById(
					Student.class, newSupplementaryImpApplicationForm
					.getStudentId());
			newSupplementaryImpApplicationForm.setStudentObj(student);

			// boolean
			// extendedTrue=NewSupplementaryImpApplicationHandler.getInstance().extendedDateForRevaluation(student.getClassSchemewise().getClasses().getId(),
			// newSupplementaryImpApplicationForm);
			boolean extendedTrue = NewSupplementaryImpApplicationHandler
			.getInstance().extendedDateForRevaluation(
					newSupplementaryImpApplicationForm.getRevclassid(),
					newSupplementaryImpApplicationForm);

			if (extendedTrue) {
				newSupplementaryImpApplicationForm.setExtended(true);
			}

			List<Integer> examIds = NewSupplementaryImpApplicationHandler
			.getInstance().checkRevaluationAppAvailable(
					newSupplementaryImpApplicationForm.getRevclassid(),
					newSupplementaryImpApplicationForm.isExtended(),
					newSupplementaryImpApplicationForm
					.isSupplementary());
			newSupplementaryImpApplicationForm.setCourseId(student
					.getAdmAppln().getCourseBySelectedCourseId().getId()
					+ "");
			if (student.getClassSchemewise() != null) {
				if (student.getClassSchemewise().getClasses() != null) {
					if (student.getClassSchemewise().getClasses().getCourse() != null) {
						String courseId = String.valueOf(student
								.getClassSchemewise().getClasses().getCourse()
								.getId());
						int programTypeId = ClassEntryTransImpl.getInstance()
						.getProgramTypeId(courseId);
						newSupplementaryImpApplicationForm
						.setProgramTypeId(String.valueOf(programTypeId));
					}
				}
			}
			if (examIds.isEmpty()) {
				newSupplementaryImpApplicationForm.setRevAppAvailable(false);
			}

			else {

				newSupplementaryImpApplicationForm.setRevAppAvailable(true);
				// newSupplementaryImpApplicationForm.setIsRevaluation("revaluation");

				newSupplementaryImpApplicationForm.setPrintApplication(false);
				NewSupplementaryImpApplicationHandler.getInstance()
				.getApplicationFormsForRevaluation(examIds,
						newSupplementaryImpApplicationForm);

				// NewSupplementaryImpApplicationHandler.getInstance().getApplicationFormsForRevaluation(examIds,newSupplementaryImpApplicationForm);
				if (student != null) {

					// get class based on exam
					ClassSchemewise classes = ClassEntryTransImpl.getInstance()
					.getClassById(
							newSupplementaryImpApplicationForm
							.getRevclassid());

					String STUDENT_IMAGE = "images/StudentPhotos/"
						+ student.getId() + ".jpg?"
						+ student.getAdmAppln().getLastModifiedDate();
					session.setAttribute("STUDENT_IMAGE", STUDENT_IMAGE);

					newSupplementaryImpApplicationForm.setNameOfStudent(student
							.getAdmAppln().getPersonalData().getFirstName()
							+ (student.getAdmAppln().getPersonalData()
									.getMiddleName() != null ? student
											.getAdmAppln().getPersonalData()
											.getMiddleName() : "")
											+ (student.getAdmAppln().getPersonalData()
													.getLastName() != null ? student
															.getAdmAppln().getPersonalData()
															.getLastName() : ""));
					newSupplementaryImpApplicationForm.setClassName(student
							.getClassSchemewise() != null ? student
									.getClassSchemewise().getClasses().getName() : "");
					newSupplementaryImpApplicationForm.setRegNo(student
							.getRegisterNo());
					newSupplementaryImpApplicationForm.setRollNo(student
							.getRollNo());
					newSupplementaryImpApplicationForm.setDob(null);
					newSupplementaryImpApplicationForm
					.setOriginalDob(CommonUtil
							.ConvertStringToDateFormat(
									CommonUtil.getStringDate(student
											.getAdmAppln()
											.getPersonalData()
											.getDateOfBirth()),
											NewStudentCertificateCourseAction.SQL_DATEFORMAT,
											NewStudentCertificateCourseAction.FROM_DATEFORMAT));
					newSupplementaryImpApplicationForm.setAddress("");
					newSupplementaryImpApplicationForm.setCourseName(student
							.getAdmAppln().getCourseBySelectedCourseId()
							.getName());
					newSupplementaryImpApplicationForm.setCourseDep(student
							.getAdmAppln().getCourseBySelectedCourseId()
							.getCode());
					newSupplementaryImpApplicationForm.setSchemeNo(""
							+ student.getClassSchemewise().getClasses()
							.getTermNumber());
					newSupplementaryImpApplicationForm.setEmail(student
							.getAdmAppln().getPersonalData().getEmail());
					newSupplementaryImpApplicationForm.setMobileNo(student
							.getAdmAppln().getPersonalData().getMobileNo2());
					newSupplementaryImpApplicationForm.setCourseId(""
							+ student.getClassSchemewise().getClasses()
							.getCourse().getId());

					newSupplementaryImpApplicationForm.setDate(CommonUtil
							.getTodayDate());
					newSupplementaryImpApplicationForm.setGender(student
							.getAdmAppln().getPersonalData().getGender());
					String address = "";
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine1() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine1()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressLine1() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine2() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressLine2()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressLine2() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByParentAddressDistrictId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByParentAddressDistrictId()
						.getName() + ",";
					} else if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressStateOthers() + ",";

					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByPermanentAddressStateId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByPermanentAddressStateId()
						.getName();
					} else if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getPermanentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getPermanentAddressStateOthers();

					}
					if (student.getAdmAppln().getPersonalData()
							.getPermanentAddressZipCode() != null)
						newSupplementaryImpApplicationForm
						.setPermanentAddressZipCode(student
								.getAdmAppln().getPersonalData()
								.getPermanentAddressZipCode());

					newSupplementaryImpApplicationForm.setAddress(address
							.toUpperCase());

					// vinodha
					newSupplementaryImpApplicationForm
					.setCommunicationEmail(student.getAdmAppln()
							.getPersonalData().getEmail());
					newSupplementaryImpApplicationForm
					.setCommunicationMobileNo(student.getAdmAppln()
							.getPersonalData().getMobileNo2());

					String communicationAddress = "";
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1().equalsIgnoreCase(
							"")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2().equalsIgnoreCase(
							"")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressDistrictId() != null) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId()
						.getName() + ",";
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() + ",";

					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressStateId() != null) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId()
						.getName();
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						communicationAddress = communicationAddress
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers();
					}
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressZipCode() != null)
						newSupplementaryImpApplicationForm
						.setCommunicationAddressZipCode(student
								.getAdmAppln().getPersonalData()
								.getCurrentAddressZipCode());

					newSupplementaryImpApplicationForm
					.setCommunicationAddress(communicationAddress
							.toUpperCase());

					if (student.getAdmAppln().getPersonalData()
							.getReligionSection() != null)
						newSupplementaryImpApplicationForm
						.setFeeConcessionCategory(student.getAdmAppln()
								.getPersonalData().getReligionSection()
								.getName());

					if (student.getAdmAppln().getPersonalData()
							.getReligionSection() != null)
						if (student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("sc")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("st")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("OEC")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("FMN")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("KPCR")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("SEBC")
								|| student.getAdmAppln().getPersonalData()
								.getReligionSection().getName()
								.equalsIgnoreCase("OBC (Non-Creamy)"))
							newSupplementaryImpApplicationForm
							.setFeeConcession("YES");
						else
							newSupplementaryImpApplicationForm
							.setFeeConcession("NO");

					String religion = "";
					if (student.getAdmAppln().getPersonalData().getReligion() != null)
						religion = religion
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getReligion().getName();
					else if (student.getAdmAppln().getPersonalData()
							.getReligionOthers() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getReligionOthers();
					if (student.getAdmAppln().getPersonalData().getCaste() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getCaste().getName();
					else if (student.getAdmAppln().getPersonalData()
							.getCasteOthers() != null)
						religion = religion
						+ ","
						+ student.getAdmAppln().getPersonalData()
						.getCasteOthers();

					newSupplementaryImpApplicationForm.setReligion(religion);

					String careTaker = "";
					String fatherTitle = "";
					String motherTitle = "";
					AdmapplnAdditionalInfo addInfoAdmAppln = NewSupplementaryImpApplicationHandler
					.getInstance().getTitleForCareTaker(
							student.getAdmAppln().getId());
					if (addInfoAdmAppln != null) {
						if (addInfoAdmAppln.getTitleFather() != null
								&& !addInfoAdmAppln.getTitleFather().isEmpty())
							fatherTitle = "Father";
						if (addInfoAdmAppln.getTitleMother() != null
								&& !addInfoAdmAppln.getTitleMother().isEmpty())
							if (addInfoAdmAppln.getTitleMother()
									.equalsIgnoreCase("sr"))
								motherTitle = "Sister";
							else
								motherTitle = "Mother";
					}
					if (student.getAdmAppln().getPersonalData().getFatherName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getFatherName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getFatherName()
						+ "," + fatherTitle;
					else if (student.getAdmAppln().getPersonalData()
							.getMotherName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getMotherName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getMotherName()
						+ "," + motherTitle;
					else if (student.getAdmAppln().getPersonalData()
							.getGuardianName() != null
							&& !student.getAdmAppln().getPersonalData()
							.getGuardianName().isEmpty())
						careTaker = student.getAdmAppln().getPersonalData()
						.getGuardianName()
						+ ",Guardian";

					newSupplementaryImpApplicationForm.setCareTaker(careTaker
							.toUpperCase());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ActionForward getDataToFormForRevaluationChoose(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form

		try {
			HttpSession session = request.getSession(false);
			newSupplementaryImpApplicationForm.setStudentId(Integer.parseInt(session.getAttribute("studentId").toString()));
			newSupplementaryImpApplicationForm.setRevclassid(Integer.parseInt(session.getAttribute("revclassId").toString()));

			ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl.getInstance();
			Student student = (Student) txn.getMasterEntryDataById(Student.class, newSupplementaryImpApplicationForm.getStudentId());
			newSupplementaryImpApplicationForm.setStudentObj(student);

			boolean extendedTrue = NewSupplementaryImpApplicationHandler.getInstance().extendedDateForRevaluation(student.getClassSchemewise().getClasses().getId(),newSupplementaryImpApplicationForm);
			if (extendedTrue) {
				newSupplementaryImpApplicationForm.setExtended(true);
			}

			List<Integer> examIds = NewSupplementaryImpApplicationHandler.getInstance().checkRevaluationAppAvailable(newSupplementaryImpApplicationForm.getRevclassid(),newSupplementaryImpApplicationForm.isExtended(),
					newSupplementaryImpApplicationForm.isSupplementary());
			newSupplementaryImpApplicationForm.setCourseId(student.getAdmAppln().getCourseBySelectedCourseId().getId()+ "");

			if (examIds.isEmpty()) {
				newSupplementaryImpApplicationForm.setRevAppAvailable(false);
			}

			else {

				newSupplementaryImpApplicationForm.setRevAppAvailable(true);

				INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl.getInstance();
				String query = "from ExamRevaluationApplicationNew er where er.classes.id="+ newSupplementaryImpApplicationForm.getRevclassid()+ " and er.exam.id="+ examIds.get(0)
					+ " and er.student.id= "+ newSupplementaryImpApplicationForm.getStudentId();

				if (newSupplementaryImpApplicationForm.getIsRevaluation() != null	&& newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")) {
					query = query + "  and er.isRevaluation=1";
				}
				if (newSupplementaryImpApplicationForm.getIsScrutiny() != null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")) {
					query = query + "  and er.isScrutiny=1 ";
				}

				List<ExamRevaluationApp> boList = transaction.getDataForQuery(query);

				if (boList.size() != 0) {
					newSupplementaryImpApplicationForm.setPrintApplication(true);
					NewSupplementaryImpApplicationHandler.getInstance().getApplicationFormsForRevaluationChooseType(examIds, newSupplementaryImpApplicationForm);
				} else {
					newSupplementaryImpApplicationForm.setPrintApplication(false);
					newSupplementaryImpApplicationForm.setPaymentDone(false);
					NewSupplementaryImpApplicationHandler.getInstance().getApplicationFormsForRevaluation(examIds,newSupplementaryImpApplicationForm);

				}

				if (student != null) {
					// get class based on exam
					ClassSchemewise classes = ClassEntryTransImpl.getInstance().getClassById(newSupplementaryImpApplicationForm.getRevclassid());

					// setting data for print

					/*
					 * if(NewSupplementaryImpApplicationHandler.getInstance().checkOnlinePayment
					 * (newSupplementaryImpApplicationForm)){
					 * newSupplementaryImpApplicationForm
					 * .setPrintSupplementary(true);
					 * 
					 * }else{
					 * newSupplementaryImpApplicationForm.setPrintSupplementary
					 * (false);
					 * 
					 * }
					 */

					// raghu write newly like regular app
					newSupplementaryImpApplicationForm.setNameOfStudent(student.getAdmAppln().getPersonalData().getFirstName()
							+ (student.getAdmAppln().getPersonalData()
									.getMiddleName() != null ? student
											.getAdmAppln().getPersonalData()
											.getMiddleName() : "")
											+ (student.getAdmAppln().getPersonalData()
													.getLastName() != null ? student
															.getAdmAppln().getPersonalData()
															.getLastName() : ""));
					newSupplementaryImpApplicationForm.setClassName(classes
							.getClasses().getName());
					newSupplementaryImpApplicationForm.setRegisterNo(student
							.getRegisterNo());
					newSupplementaryImpApplicationForm.setRollNo(student
							.getRollNo());
					newSupplementaryImpApplicationForm.setDob(null);
					newSupplementaryImpApplicationForm
					.setOriginalDob(CommonUtil
							.ConvertStringToDateFormat(
									CommonUtil.getStringDate(student
											.getAdmAppln()
											.getPersonalData()
											.getDateOfBirth()),
											NewStudentCertificateCourseAction.SQL_DATEFORMAT,
											NewStudentCertificateCourseAction.FROM_DATEFORMAT));
					newSupplementaryImpApplicationForm.setAddress("");
					newSupplementaryImpApplicationForm.setCourseName(student
							.getAdmAppln().getCourseBySelectedCourseId()
							.getName());
					newSupplementaryImpApplicationForm.setSchemeNo(""
							+ student.getClassSchemewise().getClasses()
							.getTermNumber());
					newSupplementaryImpApplicationForm.setEmail(student
							.getAdmAppln().getPersonalData().getEmail());
					newSupplementaryImpApplicationForm.setMobileNo(student
							.getAdmAppln().getPersonalData().getMobileNo2());
					newSupplementaryImpApplicationForm.setCourseId(""
							+ student.getClassSchemewise().getClasses()
							.getCourse().getId());

					String address = "";
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine1().equalsIgnoreCase(
							"")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressLine2().equalsIgnoreCase(
							"")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() + ",";
					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressDistrictId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId()
						.getName();
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() + ",";

					}
					if (student.getAdmAppln().getPersonalData()
							.getStateByCurrentAddressStateId() != null) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId()
						.getName() + ",";
					} else if (student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers() != null
							&& !student.getAdmAppln().getPersonalData()
							.getCurrentAddressStateOthers()
							.equalsIgnoreCase("")) {
						address = address
						+ ""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() + ",";

					}

					newSupplementaryImpApplicationForm.setAddress(address);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping
		.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addRevaluationApplicationForStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewSupplementaryImpApplicationAction - saveMarks");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {

				// checl atleast one subject select or not
				int select = 0;
				List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
				.getMainList();
				for (SupplementaryAppExamTo suppTo : examList) {
					List<SupplementaryApplicationClassTo> classTos = suppTo
					.getExamList();
					for (SupplementaryApplicationClassTo cto : classTos) {
						List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
						.getToList();
						for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
							if (!to.isTempChecked())
								if (to.getIsApplied())
									select++;

						}
					}
				}

				if (select == 0) {
					errors.add(CMSConstants.ERROR, new ActionError(	"knowledgepro.exam.supplementary.message","Please Select atleast one subject"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
				}

				// checl atleast one revaluation select or not
				select = 0;
				if (newSupplementaryImpApplicationForm.getIsRevaluation() != null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")) {
					select++;
				}
				if (newSupplementaryImpApplicationForm.getIsScrutiny() != null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")) {
					select++;
				}
				// if(newSupplementaryImpApplicationForm.getIsRevaluation()!=null
				// &&
				// newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("scrutiny")){
				// select++;
				// }
				// if(newSupplementaryImpApplicationForm.getProgramTypeName().equalsIgnoreCase("PG")){
				// select++;
				// }

				newSupplementaryImpApplicationForm.setPrintSupplementary(false);
				String msg = "";
				boolean isSaved = NewSupplementaryImpApplicationHandler.getInstance().saveRevaluationApplicationForStudentLogin(newSupplementaryImpApplicationForm);

				msg = newSupplementaryImpApplicationForm.getMsg();
				if (isSaved) {

				getDataToFormForRevaluationChoose(mapping,newSupplementaryImpApplicationForm, request,response);

					newSupplementaryImpApplicationForm.setDisplayButton(true);

					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.revaluation.added.success"));

					saveMessages(request, messages);

					// redirecting pgi
				redirectPGIForRevaluationScrutiny(mapping,newSupplementaryImpApplicationForm, request,response);

				} else {
					errors.add(CMSConstants.ERROR,new ActionError("knowledgepro.admission.empty.err.message"," Revaluation application submission was not successful."));
					saveErrors(request, errors);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log.info("Exit NewSupplementaryImpApplicationAction - saveMarks errors not empty ");
			return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
		}
		log.info("Entered NewSupplementaryImpApplicationAction - saveMarks");
		return mapping.findForward(CMSConstants.REDIRECT_PGI_REVALUATION);
	}

	public ActionForward showPrintDetailsForRevalution(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		newSupplementaryImpApplicationForm.setPrintSupplementary(false);

		return mapping.findForward("printDetailsforRevaluation");

	}

	public ActionForward checkRevaluationApplicationForSuppl(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		newSupplementaryImpApplicationForm.resetFields();
		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setProgramTypeName((String) session
				.getAttribute("programType").toString());
		newSupplementaryImpApplicationForm.setRevclassid(Integer
				.parseInt(newSupplementaryImpApplicationForm.getSuppClassId()));
		newSupplementaryImpApplicationForm
		.setRevExamId(newSupplementaryImpApplicationForm
				.getSuppExamId());
		newSupplementaryImpApplicationForm.setPrintSupplementary(false);
		newSupplementaryImpApplicationForm.setSupplementary(true);

		session.setAttribute("revclassId", newSupplementaryImpApplicationForm
				.getSuppClassId());
		newSupplementaryImpApplicationForm
		.setExamId(newSupplementaryImpApplicationForm.getSuppExamId());
		newSupplementaryImpApplicationForm.setRevclassid(Integer
				.parseInt(newSupplementaryImpApplicationForm.getSuppClassId()));
		setDataToFormForRevaluation(newSupplementaryImpApplicationForm, request);

		SimpleDateFormat df = new SimpleDateFormat();
		newSupplementaryImpApplicationForm.setDateOfApplication(df
				.format(new Date()));

		return mapping
		.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_FIRST_PAGE);
	}

	public ActionForward showPrintChallanForRevaluation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;

		return mapping.findForward("printDetailsChallanRevaluation");

	}

	public ActionForward checkRevaluationApplicationForSupplementaryStudentLogin(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		String examId = newSupplementaryImpApplicationForm.getExamId();
		System.out.println(examId);
		newSupplementaryImpApplicationForm.resetFields();
		newSupplementaryImpApplicationForm.setIsRevaluation("");
		newSupplementaryImpApplicationForm.setIsScrutiny("");
		newSupplementaryImpApplicationForm.setRevclassid(0);
		newSupplementaryImpApplicationForm.setExamId(examId);
		newSupplementaryImpApplicationForm.setPrintApplication(false);

		setDataToFormForSupplementaryRevaluation(
				newSupplementaryImpApplicationForm, request);

		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
		}

	}

	private void setDataToFormForSupplementaryRevaluation(
			NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,
			HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession(true);
		newSupplementaryImpApplicationForm.setStudentId(Integer
				.parseInt(session.getAttribute("studentId").toString()));
		newSupplementaryImpApplicationForm.setRevclassid(Integer
				.parseInt(session.getAttribute("revclassId").toString()));
		ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl
		.getInstance();
		Student student = (Student) txn.getMasterEntryDataById(Student.class,
				newSupplementaryImpApplicationForm.getStudentId());
		newSupplementaryImpApplicationForm.setStudentObj(student);
		List<Integer> examIds = NewSupplementaryImpApplicationHandler
		.getInstance().checkRevaluationAvailableForSupplementry(
				newSupplementaryImpApplicationForm.getStudentId());
		int classId = ClassEntryTransImpl.getInstance().getPrevClassId(
				newSupplementaryImpApplicationForm.getStudentId(),
				Integer
				.parseInt(newSupplementaryImpApplicationForm
						.getExamId()));
		newSupplementaryImpApplicationForm.setRevalSupplyClassId(classId);
		if (student.getClassSchemewise() != null) {
			if (student.getClassSchemewise().getClasses() != null) {
				if (student.getClassSchemewise().getClasses().getCourse() != null) {
					String courseId = String.valueOf(student
							.getClassSchemewise().getClasses().getCourse()
							.getId());
					int programTypeId = ClassEntryTransImpl.getInstance()
					.getProgramTypeId(courseId);
					newSupplementaryImpApplicationForm.setProgramTypeId(String
							.valueOf(programTypeId));
				}
			}
		}
		// NewSupplementaryImpApplicationHandler.getInstance().getStartDateEndDateRevaluationForSupp(classId,
		// newSupplementaryImpApplicationForm);
		if (examIds.isEmpty()) {
			newSupplementaryImpApplicationForm.setRevAppAvailable(false);
		}

		else if (examIds.contains(Integer
				.parseInt(newSupplementaryImpApplicationForm.getExamId()))) {

			newSupplementaryImpApplicationForm.setRevAppAvailable(true);
			NewSupplementaryImpApplicationHandler.getInstance()
			.getApplicationFormsForSupplementaryRevaluation(
					Integer.parseInt(newSupplementaryImpApplicationForm
							.getExamId()),
							newSupplementaryImpApplicationForm);
			if (student != null) {

				// get class based on exam
				// ClassSchemewise
				// classes=ClassEntryTransImpl.getInstance().getClassById(newSupplementaryImpApplicationForm.getRevclassid());
				ClassSchemewise classes = ClassEntryTransImpl.getInstance()
				.getClassById(classId);
				// raghu write newly like regular app
				newSupplementaryImpApplicationForm.setNameOfStudent(student
						.getAdmAppln().getPersonalData().getFirstName()
						+ (student.getAdmAppln().getPersonalData()
								.getMiddleName() != null ? student
										.getAdmAppln().getPersonalData()
										.getMiddleName() : "")
										+ (student.getAdmAppln().getPersonalData()
												.getLastName() != null ? student.getAdmAppln()
														.getPersonalData().getLastName() : ""));
				newSupplementaryImpApplicationForm.setClassName(classes
						.getClasses().getName());
				newSupplementaryImpApplicationForm.setRegisterNo(student
						.getRegisterNo());
				newSupplementaryImpApplicationForm.setRollNo(student
						.getRollNo());
				newSupplementaryImpApplicationForm.setDob(null);
				newSupplementaryImpApplicationForm
				.setOriginalDob(CommonUtil
						.ConvertStringToDateFormat(
								CommonUtil.getStringDate(student
										.getAdmAppln()
										.getPersonalData()
										.getDateOfBirth()),
										NewStudentCertificateCourseAction.SQL_DATEFORMAT,
										NewStudentCertificateCourseAction.FROM_DATEFORMAT));
				newSupplementaryImpApplicationForm.setAddress("");
				newSupplementaryImpApplicationForm.setCourseName(student
						.getAdmAppln().getCourseBySelectedCourseId().getName());
				newSupplementaryImpApplicationForm.setSchemeNo(""
						+ student.getClassSchemewise().getClasses()
						.getTermNumber());
				newSupplementaryImpApplicationForm.setEmail(student
						.getAdmAppln().getPersonalData().getEmail());
				newSupplementaryImpApplicationForm.setMobileNo(student
						.getAdmAppln().getPersonalData().getMobileNo2());
				newSupplementaryImpApplicationForm.setCourseId(""
						+ student.getClassSchemewise().getClasses().getCourse()
						.getId());

				String address = "";
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine1() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine2() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressDistrictId()
					.getName();
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressStateId()
					.getName() + ",";
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}

				newSupplementaryImpApplicationForm.setAddress(address);
			}

		} else {
			newSupplementaryImpApplicationForm.setRevAppAvailable(false);
		}

	}

	public ActionForward addRevaluationApplicationForStudentLoginSupply(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewExamMarksEntryAction - saveMarks");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {

				// checl atleast one subject select or not
				int select = 0;
				List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
				.getMainList();
				for (SupplementaryAppExamTo suppTo : examList) {
					List<SupplementaryApplicationClassTo> classTos = suppTo
					.getExamList();
					for (SupplementaryApplicationClassTo cto : classTos) {
						List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
						.getToList();
						for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
							if (!to.isTempChecked())
								if (to.getIsApplied())
									select++;

						}
					}
				}

				if (select == 0) {
					errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.exam.supplementary.message","Please Select atleast one subject"));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
				}

				// checl atleast one revaluation select or not
				select = 0;
				if (newSupplementaryImpApplicationForm.getIsRevaluation() != null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")) {
					select++;
				}
				if (newSupplementaryImpApplicationForm.getIsScrutiny() != null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")) {
					select++;
				}

				if (select == 0) {
					errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.exam.supplementary.message","Please Select atleast one apply exam type "));
					saveErrors(request, errors);
					return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
				}

				newSupplementaryImpApplicationForm.setPrintSupplementary(false);
				String msg = "";
				boolean isSaved = NewSupplementaryImpApplicationHandler.getInstance().saveRevaluationApplicationForStudentLoginSupply(newSupplementaryImpApplicationForm);
				msg = newSupplementaryImpApplicationForm.getMsg();
				if (isSaved) {
					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.revaluation.added.success.online"));
					saveMessages(request, messages);
					String examId = newSupplementaryImpApplicationForm.getExamId();
					newSupplementaryImpApplicationForm.resetFields();
					newSupplementaryImpApplicationForm.setExamId(examId);
					// setting data for print

			newSupplementaryImpApplicationForm.setPrintApplication(true);
			getDataToFormForRevaluationChooseSupply(mapping,newSupplementaryImpApplicationForm, request,response);
			
			redirectPGIForRevaluationScrutinySupply(mapping, newSupplementaryImpApplicationForm, request, response);
				}
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping
			.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		return mapping
		.findForward(CMSConstants.REDIRECT_PGI_REVALUATION);
	}

	public ActionForward getDataToFormForRevaluationChooseSupply(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		HttpSession session = request.getSession(true);

		newSupplementaryImpApplicationForm.setStudentId(Integer.parseInt(session.getAttribute("studentId").toString()));
		newSupplementaryImpApplicationForm.setRevclassid(Integer.parseInt(session.getAttribute("revclassId").toString()));

		ISingleFieldMasterTransaction txn = SingleFieldMasterTransactionImpl.getInstance();
		Student student = (Student) txn.getMasterEntryDataById(Student.class,newSupplementaryImpApplicationForm.getStudentId());
		newSupplementaryImpApplicationForm.setStudentObj(student);
		List<Integer> examIds = NewSupplementaryImpApplicationHandler.getInstance().checkRevaluationAvailableForSupplementry(newSupplementaryImpApplicationForm.getStudentId());

		if (examIds.isEmpty()) {
			newSupplementaryImpApplicationForm.setRevAppAvailable(false);
		}

		else if (examIds.contains(Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()))) {
			newSupplementaryImpApplicationForm.setRevAppAvailable(true);
			// int
			// classId=ClassEntryTransImpl.getInstance().getPrevClassId(newSupplementaryImpApplicationForm.getStudentId(),
			// Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()));
			INewExamMarksEntryTransaction transaction = NewExamMarksEntryTransactionImpl.getInstance();
			/*
			 * String
			 * query="from ExamRevaluationApplicationNew er where er.classes.id="
			 * +newSupplementaryImpApplicationForm.getRevclassid()+
			 * " and er.exam.id="+examIds.get(0)+" and er.student.id= "+
			 * newSupplementaryImpApplicationForm.getStudentId();
			 */
			String query = "from ExamRevaluationApplicationNew er where er.classes.id="+ newSupplementaryImpApplicationForm.getRevalSupplyClassId() + " and er.exam.id=" + newSupplementaryImpApplicationForm.getExamId()
				+ " and er.student.id= " + newSupplementaryImpApplicationForm.getStudentId();

			if (newSupplementaryImpApplicationForm.getIsRevaluation() != null && newSupplementaryImpApplicationForm.getIsRevaluation().equalsIgnoreCase("on")) {
				query = query + "  and er.isRevaluation=1";
			}
			if (newSupplementaryImpApplicationForm.getIsScrutiny() != null && newSupplementaryImpApplicationForm.getIsScrutiny().equalsIgnoreCase("on")) {
				query = query + "  and er.isScrutiny=1 ";
			}

			List<ExamRevaluationApplicationNew> boList = transaction.getDataForQuery(query);

			if (boList.size() != 0) {
				newSupplementaryImpApplicationForm.setPrintApplication(true);
				NewSupplementaryImpApplicationHandler.getInstance().getApplicationFormsForSupplyRevaluationChooseType(Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()),newSupplementaryImpApplicationForm);
			} else {
				newSupplementaryImpApplicationForm.setPrintApplication(false);
				newSupplementaryImpApplicationForm.setPaymentDone(false);
				NewSupplementaryImpApplicationHandler.getInstance().getApplicationFormsForSupplementaryRevaluation(Integer.parseInt(newSupplementaryImpApplicationForm.getExamId()),newSupplementaryImpApplicationForm);

			}

			if (student != null) {
				// get class based on exam
				/*
				 * ClassSchemewise
				 * classes=ClassEntryTransImpl.getInstance().getClassById
				 * (newSupplementaryImpApplicationForm.getRevclassid());
				 */
				ClassSchemewise classes = ClassEntryTransImpl.getInstance()
				.getClassById(
						newSupplementaryImpApplicationForm
						.getRevalSupplyClassId());

				// raghu write newly like regular app
				newSupplementaryImpApplicationForm.setNameOfStudent(student
						.getAdmAppln().getPersonalData().getFirstName()
						+ (student.getAdmAppln().getPersonalData()
								.getMiddleName() != null ? student
										.getAdmAppln().getPersonalData()
										.getMiddleName() : "")
										+ (student.getAdmAppln().getPersonalData()
												.getLastName() != null ? student.getAdmAppln()
														.getPersonalData().getLastName() : ""));
				newSupplementaryImpApplicationForm.setClassName(classes
						.getClasses().getName());
				newSupplementaryImpApplicationForm.setRegisterNo(student
						.getRegisterNo());
				newSupplementaryImpApplicationForm.setRollNo(student
						.getRollNo());
				newSupplementaryImpApplicationForm.setDob(null);
				newSupplementaryImpApplicationForm
				.setOriginalDob(CommonUtil
						.ConvertStringToDateFormat(
								CommonUtil.getStringDate(student
										.getAdmAppln()
										.getPersonalData()
										.getDateOfBirth()),
										NewStudentCertificateCourseAction.SQL_DATEFORMAT,
										NewStudentCertificateCourseAction.FROM_DATEFORMAT));
				newSupplementaryImpApplicationForm.setAddress("");
				newSupplementaryImpApplicationForm.setCourseName(student
						.getAdmAppln().getCourseBySelectedCourseId().getName());
				newSupplementaryImpApplicationForm.setSchemeNo(""
						+ student.getClassSchemewise().getClasses()
						.getTermNumber());
				newSupplementaryImpApplicationForm.setEmail(student
						.getAdmAppln().getPersonalData().getEmail());
				newSupplementaryImpApplicationForm.setMobileNo(student
						.getAdmAppln().getPersonalData().getMobileNo2());
				newSupplementaryImpApplicationForm.setCourseId(""
						+ student.getClassSchemewise().getClasses().getCourse()
						.getId());
				newSupplementaryImpApplicationForm
				.setCommunicationAddressZipCode(""
						+ student.getAdmAppln().getPersonalData()
						.getCurrentAddressZipCode());

				String address = "";
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine1().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine1() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressLine2().equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressLine2() + ",";
				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressDistrictId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressDistrictId()
					.getName();
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}
				if (student.getAdmAppln().getPersonalData()
						.getStateByCurrentAddressStateId() != null) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getStateByCurrentAddressStateId()
					.getName() + ",";
				} else if (student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers() != null
						&& !student.getAdmAppln().getPersonalData()
						.getCurrentAddressStateOthers()
						.equalsIgnoreCase("")) {
					address = address
					+ ""
					+ student.getAdmAppln().getPersonalData()
					.getCurrentAddressStateOthers() + ",";

				}

				newSupplementaryImpApplicationForm.setAddress(address);
			}

		} else {
			newSupplementaryImpApplicationForm.setRevAppAvailable(false);
		}
		return mapping
		.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT_SUPPLY);
	}

	public ActionForward showPrintChallanForRevaluationSupply(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;

		return mapping.findForward("printDetailsChallanRevaluationSupply");

	}

	public ActionForward addDetailsForRegular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		ActionMessages messages = new ActionMessages();

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;
		try {

			if (newSupplementaryImpApplicationForm.getClassId() != null
					&& !newSupplementaryImpApplicationForm.getClassId()
					.isEmpty()) {

				NewSupplementaryImpApplicationHandler.getInstance()
				.addAppliedStudent(newSupplementaryImpApplicationForm);
				messages.add(CMSConstants.MESSAGES, new ActionMessage(
				"knowledgepro.regular.added.success.online"));
				messages
				.add(CMSConstants.MESSAGES, new ActionMessage(
						"knowledgepro.admission.empty.err.message",
						"Please click on Pay Online button, to pay amount of Rs "
						+ newSupplementaryImpApplicationForm
						.getTotalFee()
						+ " through PayUMoney."));

				saveMessages(request, messages);

			}

			newSupplementaryImpApplicationForm.setChallanButton(true);
			newSupplementaryImpApplicationForm.setDisplayButton(false);
			newSupplementaryImpApplicationForm.setPrintSupplementary(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (newSupplementaryImpApplicationForm.getStudentObj().getAdmAppln()
				.getCourseBySelectedCourseId().getProgram().getProgramType()
				.getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		}

	}

	public ActionForward redirectToPGIExamRegular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("enter redirectToPGI...");
		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();

		validatePgi(admForm, errors);
		try {

			if (NewSupplementaryImpApplicationHandler.getInstance()
					.checkOnlinePaymentReg(admForm)) {
				errors
				.add(
						"knowledgepro.admission.boardDetails.duplicateEntry",
						new ActionError(
								"knowledgepro.admission.boardDetails.duplicateEntry",
						"Payment already has done."));
				saveErrors(request, errors);
				if (admForm.getStudentObj().getAdmAppln()
						.getCourseBySelectedCourseId().getProgram()
						.getProgramType().getId() == 1) {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
				} else {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
				}

			}

			if (errors != null && !errors.isEmpty()) {
				saveErrors(request, errors);
				if (admForm.getStudentObj().getAdmAppln()
						.getCourseBySelectedCourseId().getProgram()
						.getProgramType().getId() == 1) {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
				} else {
					return mapping
					.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
				}
				// return
				// mapping.findForward(CMSConstants.ADMISSIONFORM_ONLINE_APPLICATIONDETAIL_PAGE);
			}
			String hash = NewSupplementaryImpApplicationHandler.getInstance()
			.getParameterForPGIReg(admForm);
			request.setAttribute("hash", hash);
			request.setAttribute("txnid", admForm.getRefNo());
			request.setAttribute("productinfo", admForm.getProductinfo());
			request.setAttribute("amount", admForm.getTotalFee());
			request.setAttribute("email", admForm.getEmail());
			request.setAttribute("firstname", admForm.getNameOfStudent());
			request.setAttribute("phone", admForm.getMobileNo());
			request.setAttribute("test", admForm.getTest());
			request.setAttribute("surl",
					CMSConstants.PAYUMONEY_SUCCESSURL_EXAM_REGULAR);
			request.setAttribute("furl",
					CMSConstants.PAYUMONEY_FAILUREURL_EXAM_REGULAR);
			log.info("*************************** exam rev hash " + hash);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error in redirectToPGI...", e);

			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			if (admForm.getStudentObj().getAdmAppln()
					.getCourseBySelectedCourseId().getProgram()
					.getProgramType().getId() == 1) {
				return mapping
				.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
			} else {
				return mapping
				.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
			}
		}
		return mapping.findForward(CMSConstants.REDIRECT_TO_PGI_PAGE_EXAM_REG);

	}

	public ActionForward updatePGIResponseExamRegular(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("enter updatePGIResponse-AdmissionFormAction...");
		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			boolean isUpdated = NewSupplementaryImpApplicationHandler
			.getInstance().updateResponseReg(admForm);

			if (isUpdated && admForm.getIsTnxStatusSuccess()) {

				if (admForm.getIsTnxStatusSuccess()) {

					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message", admForm
							.getPgiStatus()));
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message",
							"Your transaction no: " + admForm.getTxnRefNo()));
					saveMessages(request, messages);
					admForm.setChallanButton(false);
					admForm.setPrintSupplementary(true);
				} else {
					admForm.setPgiStatus("Payment Failure");
					errors.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message", admForm
							.getPgiStatus()));
					saveErrors(request, errors);

				}

			} else {
				errors.add("error", new ActionError(
				"knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			e.printStackTrace();
			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);

		}

		log.info("exit updatePGIResponse-AdmissionFormAction...");
		// return
		// mapping.findForward(CMSConstants.ONLINE_APPLICATION_ONLINE_APPLICATIONDETAIL_PAGE);

		/*if (admForm.getStudentObj().getAdmAppln().getCourseBySelectedCourseId()
				.getProgram().getProgramType().getId() == 1) {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTUG);
		} else {
			return mapping
			.findForward(CMSConstants.INIT_REGULAR_APP_STUDENT_RESULTPG);
		}*/
		return mapping.findForward("paymentSuccessfullDetails");

	}

	public ActionForward redirectToPGIExamSuppl(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("enter redirectToPGI...");
		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();

		validatePgi(admForm, errors);
		try {

			if (NewSupplementaryImpApplicationHandler.getInstance()
					.checkOnlinePaymentSuppl(admForm)) {
				errors
				.add(
						"knowledgepro.admission.boardDetails.duplicateEntry",
						new ActionError(
								"knowledgepro.admission.boardDetails.duplicateEntry",
						"Payment already has done."));
				saveErrors(request, errors);
				return mapping
				.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);

			}

			if (errors != null && !errors.isEmpty()) {
				saveErrors(request, errors);
				return mapping
				.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
				// return
				// mapping.findForward(CMSConstants.ADMISSIONFORM_ONLINE_APPLICATIONDETAIL_PAGE);
			}
			String hash = NewSupplementaryImpApplicationHandler.getInstance()
			.getParameterForPGISuppl(admForm);
			// only for test
			// request.setAttribute("pgiMsg", msg);
			request.setAttribute("hash", hash);
			request.setAttribute("txnid", admForm.getRefNo());
			request.setAttribute("productinfo", admForm.getProductinfo());
			request.setAttribute("amount", String.valueOf(admForm
					.getTotalFees()));
			request.setAttribute("email", admForm.getEmail());
			request.setAttribute("firstname", admForm.getNameOfStudent());
			request.setAttribute("phone", admForm.getMobileNo());
			request.setAttribute("test", admForm.getTest());
			request.setAttribute("surl",
					CMSConstants.PAYUMONEY_SUCCESSURL_EXAM_SUPPLY);
			request.setAttribute("furl",
					CMSConstants.PAYUMONEY_FAILUREURL_EXAM_SUPPLY);
			log.info("*************************** exam rev hash " + hash);
		} catch (Exception e) {
			e.printStackTrace();
			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}
		return mapping
		.findForward(CMSConstants.REDIRECT_TO_PGI_PAGE_EXAM_SUPPL);

	}

	public ActionForward updatePGIResponseExamSupply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("enter updatePGIResponse-AdmissionFormAction...");
		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			boolean isUpdated = NewSupplementaryImpApplicationHandler
			.getInstance().updateResponseSupp(admForm);

			if (isUpdated && admForm.getIsTnxStatusSuccess()) {

				if (admForm.getIsTnxStatusSuccess()) {
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message", admForm
							.getPgiStatus()));
					messages.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message",
							"Your transaction no: " + admForm.getTxnRefNo()));
					saveMessages(request, messages);
					//setDataToForm(admForm, request);
					admForm.setPrintSupplementary(true);
					admForm.setChallanButton(false);
				} else {
					admForm.setPgiStatus("Payment Failure");
					errors.add(CMSConstants.MESSAGES, new ActionMessage(
							"knowledgepro.admission.empty.err.message", admForm
							.getPgiStatus()));
					saveErrors(request, errors);
					//setDataToForm(admForm, request);

				}
			} else {
				errors.add("error", new ActionError(
				"knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
				//setDataToForm(admForm, request);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			errors.add("error", new ActionError(
			"knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			e.printStackTrace();
			errors
			.add(
					"knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError(
							"knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);
		}
		// setDataToForm(admForm,request);
	 return mapping.findForward("paymentSuccessfullDetails");
	}

	public ActionForward calculateAmountForSupply(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("Entered NewExamMarksEntryAction - saveMarks");
		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			double totalAmount = 0;
			int theoryCount = 0;
			int practicalCount = 0;
			int projectCount = 0;
			double lateFine = 0;
			try {
				List<SupplementaryAppExamTo> examList = newSupplementaryImpApplicationForm
				.getMainList();
				for (SupplementaryAppExamTo suppTo : examList) {
					List<SupplementaryApplicationClassTo> classTos = suppTo
					.getExamList();
					for (SupplementaryApplicationClassTo cto : classTos) {
						List<ExamSupplementaryImpApplicationSubjectTO> listSubject = cto
						.getToList();
						for (ExamSupplementaryImpApplicationSubjectTO to : listSubject) {
							if (to.getSubjectType().equalsIgnoreCase("T")) {
								if (!to.isTempChecked()) {
									if (to.getIsAppearedTheory()
											&& theoryCount == 0) {
										theoryCount++;
										totalAmount = totalAmount
										+ newSupplementaryImpApplicationForm
										.getTheoryFees();
										
									} else if (to.getIsAppearedTheory()
											&& theoryCount >= 1) {
										totalAmount = totalAmount
										+ Double
										.valueOf(newSupplementaryImpApplicationForm
												.getApplicationFees());
										
									}
								}
							} else if (to.getSubjectType()
									.equalsIgnoreCase("P")) {
								if (!to.isTempPracticalChecked()) {
									if (to.getIsAppearedPractical()
											&& practicalCount == 0
											&& theoryCount == 0) {
										practicalCount++;
										totalAmount = totalAmount
										+ newSupplementaryImpApplicationForm
										.getPracticalFees()
										+ Double
										.valueOf(newSupplementaryImpApplicationForm
												.getCvCampFees());
										if(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees() != null){
											lateFine = Double.parseDouble(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees());
											totalAmount = totalAmount + lateFine;
										}
									} else if (to.getIsAppearedPractical()
											&& practicalCount >= 1
											|| theoryCount >= 1) {
										totalAmount = totalAmount
										+ Double
										.valueOf(newSupplementaryImpApplicationForm
												.getCvCampFees());
										if(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees() != null){
											lateFine = Double.parseDouble(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees());
											totalAmount = totalAmount + lateFine;
										}
									}
								}
							} else if (to.getSubjectType().equalsIgnoreCase(
							"Project")) {
								totalAmount = totalAmount
								+ Double
								.valueOf(newSupplementaryImpApplicationForm
										.getMarksListFees())
										+ Double
										.valueOf(newSupplementaryImpApplicationForm
												.getOnlineServiceChargeFees());

							}

							/*
							 * if(!to.isTempChecked())
							 * if(to.getIsAppearedTheory())
							 * amount=amount+newSupplementaryImpApplicationForm
							 * .getTheoryFees();
							 * if(!to.isTempPracticalChecked())
							 * if(to.getIsAppearedPractical())
							 * amount=amount+newSupplementaryImpApplicationForm
							 * .getPracticalFees();
							 */}
					}
				}

				if (totalAmount == 0) {
					newSupplementaryImpApplicationForm
					.setPrintSupplementary(false);
					errors.add(CMSConstants.ERROR, new ActionError(
							"knowledgepro.exam.supplementary.message",
					"Please Select atleast one subject"));
					saveErrors(request, errors);
					return mapping
					.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
				} else {

					if (newSupplementaryImpApplicationForm.isExtended()) {
						totalAmount = totalAmount
						+ newSupplementaryImpApplicationForm
						.getFineFees();
					}
				}
				newSupplementaryImpApplicationForm.setTotalFees(totalAmount);

				// for supplementary online payment temp commented as mic not
				// using online payment

				if (totalAmount == 0) {
					if (newSupplementaryImpApplicationForm
							.getIsAppliedAlready()) {
						newSupplementaryImpApplicationForm
						.setChallanButton(false);
						newSupplementaryImpApplicationForm
						.setPrintSupplementary(false);
						newSupplementaryImpApplicationForm
						.setDisplayButton(true);
						return mapping
						.findForward(CMSConstants.SUPPLEMENTARY_APP_STUDENT_PAYMENT);
					}
					/*
					 * else{
					 * newSupplementaryImpApplicationForm.setPrintSupplementary
					 * (false); errors.add(CMSConstants.ERROR,new
					 * ActionError("knowledgepro.exam.supplementary.message"
					 * ,"Please Select atleast one subject"));
					 * saveErrors(request, errors); return
					 * mapping.findForward(CMSConstants
					 * .INIT_SUPPL_IMP_APP_STUDENT_RESULT); }
					 */
				}

				/*
				 * else {
				 * if(!newSupplementaryImpApplicationForm.getIsFeesExempted()){
				 * newSupplementaryImpApplicationForm.setTotalFees(amount);
				 * double applicationFees =0; double markslistFees =0; double
				 * cvCampFees =0; double onlineServiceCharge =0; double
				 * grandTotalFees =0;
				 * if(newSupplementaryImpApplicationForm.getApplicationFees
				 * ()!=null) applicationFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getApplicationFees());
				 * if(newSupplementaryImpApplicationForm.getCvCampFees()!=null)
				 * cvCampFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getCvCampFees());
				 * if(newSupplementaryImpApplicationForm.getMarksListFees
				 * ()!=null) markslistFees =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getMarksListFees());
				 * if(newSupplementaryImpApplicationForm.getOnlineServiceChargeFees
				 * ()!=null) onlineServiceCharge =
				 * Double.parseDouble(newSupplementaryImpApplicationForm
				 * .getOnlineServiceChargeFees()); amount =
				 * amount+applicationFees
				 * +cvCampFees+markslistFees+onlineServiceCharge;
				 * 
				 * 
				 * 
				 * 
				 * //newSupplementaryImpApplicationForm.setPrintSupplementary(true
				 * ); if(newSupplementaryImpApplicationForm.isExtended()) {
				 * amount
				 * =amount+newSupplementaryImpApplicationForm.getFineFees(); }
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmount(String.valueOf(amount));
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmount1(String.valueOf(amount));
				 * newSupplementaryImpApplicationForm
				 * .setApplicationAmountWords(CommonUtil.numberToWord1((int)
				 * amount)); } else{
				 * newSupplementaryImpApplicationForm.setApplicationAmount
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setApplicationAmount1
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setApplicationAmountWords
				 * ("fees exempted");
				 * newSupplementaryImpApplicationForm.setPrintSupplementary
				 * (true);
				 * newSupplementaryImpApplicationForm.setChallanButton(true);
				 * returnmapping.findForward(CMSConstants.
				 * INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART_2); } }
				 */
				newSupplementaryImpApplicationForm.setPrintSupplementary(false);
			} catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewExamMarksEntryAction - saveMarks errors not empty ");
			return mapping
			.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_RESULT);
		}
		log.info("Entered NewExamMarksEntryAction - saveMarks");
		// raghu added from mounts
		// return
		// mapping.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART);

		return mapping
		.findForward(CMSConstants.INIT_SUPPL_IMP_APP_STUDENT_VEIRFY_SMART_2);

	}

	public ActionForward redirectPGIForRevaluationScrutiny(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {

				// checl atleast one subject select or not

				/*
				 * boolean duplicate =
				 * NewSupplementaryImpApplicationHandler.getInstance
				 * ().checkDuplicateRecord(newSupplementaryImpApplicationForm);
				 * if(duplicate){
				 * errors.add("knowledgepro.admission.boardDetails.duplicateEntry"
				 * , new
				 * ActionError("knowledgepro.admission.boardDetails.duplicateEntry"
				 * ,"Payment already has done.")); saveErrors(request, errors);
				 * returnmapping.findForward(CMSConstants.
				 * INIT_REVALUATION_APP_STUDENT_RESULT);
				 * 
				 * }
				 */

				validatePGIForRevaluation(newSupplementaryImpApplicationForm,errors);
				String hash = NewSupplementaryImpApplicationHandler.getInstance().getParameterForRevaluation(newSupplementaryImpApplicationForm);
				request.setAttribute("hash", hash);
				request.setAttribute("txnid",newSupplementaryImpApplicationForm.getRefNo());
				request.setAttribute("productinfo",newSupplementaryImpApplicationForm.getProductinfo());
				request.setAttribute("amount",newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees());
				request.setAttribute("email",newSupplementaryImpApplicationForm.getEmail());
				request.setAttribute("firstname",newSupplementaryImpApplicationForm.getNameOfStudent());
				request.setAttribute("phone",newSupplementaryImpApplicationForm.getMobileNo());
				request.setAttribute("test", newSupplementaryImpApplicationForm.getTest());
				request.setAttribute("surl",CMSConstants.PAYUMONEY_SUCCESSURL_REVALUATION);
				request.setAttribute("furl",CMSConstants.PAYUMONEY_FAILUREURL_REVALUATION);
				log.info("*************************** exam rev hash " + hash);

			} catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewSupplementaryImpApplicationAction - saveMarks errors not empty ");
			return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
		}

		return mapping.findForward(CMSConstants.REDIRECT_PGI_REVALUATION);
	}

	private void validatePGIForRevaluation(NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm,ActionErrors errors) {
		if (CMSConstants.PGI_MERCHANT_ID_REV == null || CMSConstants.PGI_MERCHANT_ID_REV.isEmpty() || CMSConstants.PGI_MERCHANT_ID_REV == "") {
			if (errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED) != null && !errors.get(CMSConstants.PGI_MERCHANT_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_MERCHANT_ID_REQUIRED,new ActionError(CMSConstants.PGI_MERCHANT_ID_REQUIRED));
			}
		}
		if (CMSConstants.PGI_SECURITY_ID_REV == null|| CMSConstants.PGI_SECURITY_ID_REV.isEmpty()|| CMSConstants.PGI_SECURITY_ID_REV == "") {
			if (errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED) != null && !errors.get(CMSConstants.PGI_SECURITY_ID_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_SECURITY_ID_REQUIRED,new ActionError(CMSConstants.PGI_SECURITY_ID_REQUIRED));
			}
		}
		if (CMSConstants.PGI_CHECKSUM_KEY_REV == null || CMSConstants.PGI_CHECKSUM_KEY_REV.isEmpty() || CMSConstants.PGI_CHECKSUM_KEY_REV == "") {
			if (errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED) != null && !errors.get(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED).hasNext()) {
				errors.add(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED,new ActionError(CMSConstants.PGI_CHECKSUM_KEY_REQUIRED));
			}
		}

	}

	public ActionForward updatePGIDetailsForRevaluationNew(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		RevaluationApplicationPGIDetails pgiDetails = null;
		boolean isUpdated = false;
		boolean updateExamRevaluationData = false;

		try {
			List<RevaluationApplicationPGIDetails> applicationPGIDetailsList = NewSupplementaryImpApplicationHandler.getInstance().getPendingDetails(admForm);
			int countRev1 = 0;
			int countScr1 = 0;
			if (applicationPGIDetailsList != null && !applicationPGIDetailsList.isEmpty()) {
				Iterator<RevaluationApplicationPGIDetails> iterator = applicationPGIDetailsList.iterator();
				while (iterator.hasNext()) {
					pgiDetails = iterator.next();
					admForm.setStudentId(pgiDetails.getStudent().getId());
					admForm.setExamId(String.valueOf(pgiDetails.getExam()
							.getId()));
					if (pgiDetails.getIsRevaluation() == true) {
						countRev1++;
					}
					if (pgiDetails.getIsScrutiny() == true) {
						countScr1++;
					}
				}
			}
			if (countRev1 > 0) {
				isUpdated = NewSupplementaryImpApplicationHandler.getInstance().updateResponseRev(admForm);
			}
			if (countScr1 > 0) {
				isUpdated = NewSupplementaryImpApplicationHandler.getInstance().updateResponseScr(admForm);
			}
			RevaluationApplicationPGIDetails details = NewSupplementaryImpApplicationHandler.getInstance().getSuccessDetails(admForm);
			if (details.getIsRevaluation() == true) {
				updateExamRevaluationData = NewSupplementaryImpApplicationHandler.getInstance().updateData(admForm, details);
			}
			if (details.getIsScrutiny() == true) {
				updateExamRevaluationData = NewSupplementaryImpApplicationHandler.getInstance().updateDataScr(admForm, details);
			}
			if (isUpdated && admForm.getIsTnxStatusSuccess() && updateExamRevaluationData) {

				if (admForm.getIsTnxStatusSuccess()) {

					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message", admForm.getPgiStatus()));
					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message","Your transaction no: " + admForm.getTxnRefNo()));
					saveMessages(request, messages);
					admForm.setChallanButton(false);
					admForm.setPrintSupplementary(true);
				} else {
					admForm.setPgiStatus("Payment Failure");
					errors.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message", admForm.getPgiStatus()));
					saveErrors(request, errors);

				}

			} else {
				errors.add("error", new ActionError("knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			errors.add("error", new ActionError("knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add("knowledgepro.admission.boardDetails.duplicateEntry",
					new ActionError("knowledgepro.admission.boardDetails.duplicateEntry",
					"Error was occured, please login and enter details again"));
			saveErrors(request, errors);

		}
		// addRevaluationApplicationForStudentLogin(mapping, admForm, request,
		// response);
		admForm.setPrintSupplementary(false);

		log.info("exit updatePGIResponse-AdmissionFormAction...");
		// return
		// mapping.findForward(CMSConstants.ONLINE_APPLICATION_ONLINE_APPLICATIONDETAIL_PAGE);

		return mapping.findForward("paymentSuccessfullDetails");

	}
	
	
	public ActionForward redirectPGIForRevaluationScrutinySupply(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm newSupplementaryImpApplicationForm = (NewSupplementaryImpApplicationForm) form;// Type
		// casting
		// the
		// Action
		// form
		// to
		// Required
		// Form
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = newSupplementaryImpApplicationForm.validate(
				mapping, request);
		setUserId(request, newSupplementaryImpApplicationForm);
		if (errors.isEmpty()) {
			try {

				// checl atleast one subject select or not

				/*
				 * boolean duplicate =
				 * NewSupplementaryImpApplicationHandler.getInstance
				 * ().checkDuplicateRecord(newSupplementaryImpApplicationForm);
				 * if(duplicate){
				 * errors.add("knowledgepro.admission.boardDetails.duplicateEntry"
				 * , new
				 * ActionError("knowledgepro.admission.boardDetails.duplicateEntry"
				 * ,"Payment already has done.")); saveErrors(request, errors);
				 * returnmapping.findForward(CMSConstants.
				 * INIT_REVALUATION_APP_STUDENT_RESULT);
				 * 
				 * }
				 */

				validatePGIForRevaluation(newSupplementaryImpApplicationForm,errors);
				String hash = NewSupplementaryImpApplicationHandler.getInstance().getParameterForRevaluationSupply(newSupplementaryImpApplicationForm);
				request.setAttribute("hash", hash);
				request.setAttribute("txnid",newSupplementaryImpApplicationForm.getRefNo());
				request.setAttribute("productinfo",newSupplementaryImpApplicationForm.getProductinfo());
				request.setAttribute("amount",newSupplementaryImpApplicationForm.getTotalRevaluationPaymentFees());
				request.setAttribute("email",newSupplementaryImpApplicationForm.getEmail());
				request.setAttribute("firstname",newSupplementaryImpApplicationForm.getNameOfStudent());
				request.setAttribute("phone",newSupplementaryImpApplicationForm.getMobileNo());
				request.setAttribute("test", newSupplementaryImpApplicationForm.getTest());
				request.setAttribute("surl",CMSConstants.PAYUMONEY_SUCCESSURL_REVALUATION_SUPPLY);
				request.setAttribute("furl",CMSConstants.PAYUMONEY_FAILUREURL_REVALUATION_SUPPLY);
				log.info("*************************** exam rev hash " + hash);

			} catch (Exception exception) {
				exception.printStackTrace();
				String msg = super.handleApplicationException(exception);
				newSupplementaryImpApplicationForm.setErrorMessage(msg);
				newSupplementaryImpApplicationForm.setErrorStack(exception
						.getMessage());
				return mapping.findForward(CMSConstants.STUDENT_ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			log
			.info("Exit NewSupplementaryImpApplicationAction - saveMarks errors not empty ");
			return mapping.findForward(CMSConstants.INIT_REVALUATION_APP_STUDENT_RESULT);
		}

		return mapping.findForward(CMSConstants.REDIRECT_PGI_REVALUATION);
	}
	
	public ActionForward updatePGIDetailsForRevaluationNewSupply(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		NewSupplementaryImpApplicationForm admForm = (NewSupplementaryImpApplicationForm) form;
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		RevaluationApplicationPGIDetails pgiDetails = null;
		boolean isUpdated = false;
		boolean updateExamRevaluationData = false;

		try {
			List<RevaluationApplicationPGIDetails> applicationPGIDetailsList = NewSupplementaryImpApplicationHandler.getInstance().getPendingDetailsForSupply(admForm);
			int countRev1 = 0;
			int countScr1 = 0;
			if (applicationPGIDetailsList != null && !applicationPGIDetailsList.isEmpty()) {
				Iterator<RevaluationApplicationPGIDetails> iterator = applicationPGIDetailsList.iterator();
				while (iterator.hasNext()) {
					pgiDetails = iterator.next();
					admForm.setStudentId(pgiDetails.getStudent().getId());
					admForm.setExamId(String.valueOf(pgiDetails.getExam()
							.getId()));
					if (pgiDetails.getIsRevaluation() == true) {
						countRev1++;
					}
					if (pgiDetails.getIsScrutiny() == true) {
						countScr1++;
					}
				}
			}
			if (countRev1 > 0) {
				isUpdated = NewSupplementaryImpApplicationHandler.getInstance().updateResponseRevSupply(admForm);
			}
			if (countScr1 > 0) {
				isUpdated = NewSupplementaryImpApplicationHandler.getInstance().updateResponseScrSupply(admForm);
			}
			RevaluationApplicationPGIDetails details = NewSupplementaryImpApplicationHandler.getInstance().getSuccessDetails(admForm);
			if (details.getIsRevaluation() == true) {
				updateExamRevaluationData = NewSupplementaryImpApplicationHandler.getInstance().updateDataSupply(admForm, details);
			}
			if (details.getIsScrutiny() == true) {
				updateExamRevaluationData = NewSupplementaryImpApplicationHandler.getInstance().updateDataScrSupply(admForm, details);
			}
			if (isUpdated && admForm.getIsTnxStatusSuccess()&& updateExamRevaluationData) {

				if (admForm.getIsTnxStatusSuccess()) {

					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message", admForm.getPgiStatus()));
					messages.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message","Your transaction no: " + admForm.getTxnRefNo()));
					saveMessages(request, messages);
					admForm.setChallanButton(false);
					admForm.setPrintSupplementary(true);
				} else {
					admForm.setPgiStatus("Payment Failure");
					errors.add(CMSConstants.MESSAGES, new ActionMessage("knowledgepro.admission.empty.err.message", admForm.getPgiStatus()));
					saveErrors(request, errors);

				}

			} else {
				errors.add("error", new ActionError(
				"knowledgepro.admission.pgi.update.failure"));
				saveErrors(request, errors);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			errors.add("error", new ActionError("knowledgepro.admission.pgi.rejected"));
			saveErrors(request, errors);
		} catch (Exception e) {
			e.printStackTrace();
			errors
			.add("knowledgepro.admission.boardDetails.duplicateEntry",new ActionError("knowledgepro.admission.boardDetails.duplicateEntry",
			"Error was occured, please login and enter details again"));
			saveErrors(request, errors);

		}
		// addRevaluationApplicationForStudentLogin(mapping, admForm, request,
		// response);
		admForm.setPrintSupplementary(false);

		log.info("exit updatePGIResponse-AdmissionFormAction...");
		// return
		// mapping.findForward(CMSConstants.ONLINE_APPLICATION_ONLINE_APPLICATIONDETAIL_PAGE);

		return mapping.findForward("paymentSuccessfullDetails");

	}
	
}
