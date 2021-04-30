package com.kp.cms.actions.admission;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.bo.admin.TCNumber;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.admission.TransferCertificateForm;
import com.kp.cms.handlers.admission.TransferCertificateHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.to.admission.PrintTcDetailsTo;
import com.kp.cms.transactions.admission.ITransferCertificateTransaction;
import com.kp.cms.transactionsimpl.admission.TransferCertificateTransactionImpl;
import com.kp.cms.utilities.CurrentAcademicYear;

@SuppressWarnings("deprecation")
public class TransferCertificateAction extends BaseDispatchAction
{
	private static final Log log = LogFactory.getLog(TransferCertificateAction.class);
	public ActionForward initTransferCertificate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.resetFields();
			certificateForm.setToCollege("Cjc");
			certificateForm.setReprint(false);
			initsetDataToForm(certificateForm,request);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE);
	}

	private void setDate(TransferCertificateForm certificateForm) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		certificateForm.setTcDate(dateFormat.format(date));
	}

	private void initsetDataToForm(TransferCertificateForm certificateForm,HttpServletRequest request) throws Exception{
		Map<Integer,String> classMap = setupClassMapToRequest(certificateForm,request);
		certificateForm.setClassMap(classMap);
	}
	
	
	/**
	 * Sets all the classes for the current year in request scope
	 */
	public Map<Integer,String> setupClassMapToRequest(TransferCertificateForm certificateForm,HttpServletRequest request) throws Exception{
		log.info("Entering into setpClassMapToRequest of TransferCertificateAction");
		Map<Integer,String> classMap = new HashMap<Integer, String>();
		try {
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int year=CurrentAcademicYear.getInstance().getAcademicyear();
			if(year!=0){
				currentYear=year;
			}
			if(certificateForm.getYear()!=null && !certificateForm.getYear().isEmpty()){
				currentYear=Integer.parseInt(certificateForm.getYear());
			}
			classMap = CommonAjaxHandler.getInstance().getClassesByYear(currentYear);
			request.setAttribute("classMap", classMap);
			return classMap;
		} catch (Exception e) {
			log.debug(e.getMessage());
			log.error("Error occured in setpClassMapToRequest of TransferCertificateAction");
		}
		log.info("Leaving into setpClassMapToRequest of TransferCertificateAction");
		return classMap;
	}
	
	public ActionForward printTC(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		certificateForm.setFlag(false);
		try
		{
			if(certificateForm.getPrintOnlyTc() == null){
				certificateForm.setPrintOnlyTc("false");
			}
			 ActionErrors errors = certificateForm.validate(mapping, request);
			if (errors.isEmpty())
			{
				List<PrintTcDetailsTo>studentList= null;
				if(certificateForm.getPrintOnlyTc().equalsIgnoreCase("true")){
					studentList=TransferCertificateHandler.getInstance().getStudentsByClassOnlyTC(request,certificateForm);
				}
				else{
					studentList=TransferCertificateHandler.getInstance().getStudentsByClass(request,certificateForm);
				}
				certificateForm.setStudentList(studentList);
				certificateForm.resetFields();
			}
			else
			{
				addErrors(request, errors);
			}
		}
		catch (Exception e) {
			if(e instanceof DataNotFoundException) {
				ActionErrors errors = new ActionErrors();
				errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.norecords"));
				saveErrors(request, errors);
				return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
			}
			e.printStackTrace();
			String msg = super.handleApplicationException(e);
			certificateForm.setErrorMessage(msg);
			certificateForm.setErrorStack(e.getMessage());
	        return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		if(certificateForm.getToCollege().equalsIgnoreCase("Cjc")){
			setDate(certificateForm);
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		}else if
		(certificateForm.getPrintOnlyTc().equalsIgnoreCase("true")){
			setDate(certificateForm);
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);	
		}else {
			setDate(certificateForm);
 			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		}
	}
	
	public ActionForward initTCMC(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.resetFields();
			certificateForm.setToCollege("MIC");
			initsetDataToForm(certificateForm,request);
			setDate(certificateForm);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE);
	}
	
	public ActionForward initReprintTC(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.resetFields();
			certificateForm.setToCollege("Cjc");
			initsetDataToForm(certificateForm,request);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE_REPRINT);
	}
	
	public ActionForward initReprintTCMC(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.resetFields();
			certificateForm.setToCollege("MIC");
			initsetDataToForm(certificateForm,request);
			setDate(certificateForm);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE_REPRINT);
	}
	
	public ActionForward rePrintTC(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		certificateForm.setFlag(false);
		try
		{
			if(certificateForm.getRePrintOnlyTc() == null){
				certificateForm.setRePrintOnlyTc("false");
			}
			certificateForm.setDuplicate("no");
			certificateForm.setTcType(null);
			certificateForm.setReprint(true);
			 ActionErrors errors = certificateForm.validate(mapping, request);
			if (errors.isEmpty())
			{
				List<PrintTcDetailsTo>studentList = null;
				 if(certificateForm.getRePrintOnlyTc().equalsIgnoreCase("true")){
					 studentList=TransferCertificateHandler.getInstance().getStudentsByRegNoForReprintOnlyTc(certificateForm.getFromUsn(),request,certificateForm);
				 }
				 else{
					studentList=TransferCertificateHandler.getInstance().getStudentsByRegNo(certificateForm.getFromUsn(),request,certificateForm);
				 }
				certificateForm.setStudentList(studentList);
				certificateForm.resetFields();
			}
			else
			{
				addErrors(request, errors);
			}
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		if(certificateForm.getToCollege().equalsIgnoreCase("Cjc")){
			setDate(certificateForm);
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		}
		else if(certificateForm.getRePrintOnlyTc().equalsIgnoreCase("true")){
			setDate(certificateForm);
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		}else{
			setDate(certificateForm);
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		}
	}
	
	public ActionForward initReprintTCByClass(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.resetFields();
			certificateForm.setToCollege("Cjc");
			certificateForm.setReprint(true);
			initsetDataToForm(certificateForm,request);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE);
	}
	
	public ActionForward rePrintTCByClass(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			 ActionErrors errors = certificateForm.validate(mapping, request);
			if (errors.isEmpty())
			{
				List<PrintTcDetailsTo>studentList=TransferCertificateHandler.getInstance().getStudentsByClassForReprint(request,certificateForm);
				certificateForm.setStudentList(studentList);
				certificateForm.resetFields();
			}
			else
			{
				addErrors(request, errors);
			}
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		if(certificateForm.getToCollege().equalsIgnoreCase("Cjc"))
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		else
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
	}
	
	public ActionForward printTCForCjc(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			ActionErrors errors = new ActionErrors();
			/* code modified by sudhir*/
			certificateForm.setReprint(false);
			//errors = certificateForm.validate(mapping, request);
			if (errors.isEmpty())
			{
				List<PrintTcDetailsTo>studentList=TransferCertificateHandler.getInstance().getStudentsByClassForCjc(certificateForm,request);
				certificateForm.setStudentList(studentList);
				certificateForm.resetFields();
			}
			else
			{
				addErrors(request, errors);
			}
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		if(certificateForm.getToCollege().equalsIgnoreCase("Cjc"))
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
		else
			return mapping.findForward(CMSConstants.PRINT_TC_CHRIST);
	}
	
	/**
	 * This is For Christ only Tc (MC in not included)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward initChristTCFormat(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		log.info("Entered initChristTCFormat in TransferCertificate Action");
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			//clearing the Fields Before Displaying the jsp
			certificateForm.resetFields();
			certificateForm.setReprint(false);
			initsetDataToForm(certificateForm,request);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		log.info("Exit initChristTCFormat in TransferCertificate Action");
		return mapping.findForward(CMSConstants.INIT_TRANSFER_CERTIFICATE);
	}
	
	/**
	 * Method to select the candidates for score sheet result entry based on the input selected
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printChristTransferCertificate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("Entered TransferCertificate Action- printChristTransferCertificate");
		
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		 ActionErrors errors = certificateForm.validate(mapping, request);
		if (errors.isEmpty()) {
		
			try {
				ITransferCertificateTransaction certificateTransaction=TransferCertificateTransactionImpl.getInstance();
				// Check TC Master Is Configured are not
				TCNumber number=certificateTransaction.getOnlyTCNumber(Integer.parseInt(certificateForm.getYear()),"MIC",true);
				// TC Master has Not configured then show message
				if (number==null) {
					errors.add(CMSConstants.ERROR, new ActionError("knowledgepro.admission.tcMaster.configure"));
					saveErrors(request, errors);
					initsetDataToForm(certificateForm,request);
					log.info("Exit TransferCertificate Action- printChristTransferCertificate size 0");
					return mapping.findForward(CMSConstants.INIT_TRANSFER_CERTIFICATE);
				}
				//List<PrintTcDetailsTo> studentList=TransferCertificateHandler.getInstance().getTCForStudentsByClass(request,certificateForm,number);
				
			}  catch (Exception exception) {
				String msg = super.handleApplicationException(exception);
				certificateForm.setErrorMessage(msg);
				certificateForm.setErrorStack(exception.getMessage());
				return mapping.findForward(CMSConstants.ERROR_PAGE);
			}
		} else {
			addErrors(request, errors);
			initsetDataToForm(certificateForm,request);	
			log.info("Exit TransferCertificate Action - printChristTransferCertificate errors not empty ");
			return mapping.findForward(CMSConstants.INIT_TRANSFER_CERTIFICATE);
		}
		log.info("Entered TransferCertificate Action - printChristTransferCertificate");
		return mapping.findForward(CMSConstants.INIT_TRANSFER_CERTIFICATE);
	}
	
	public ActionForward initReprint(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		TransferCertificateForm certificateForm=(TransferCertificateForm)form;
		try
		{
			certificateForm.setRePrintOnlyTc("true");
			certificateForm.resetFields();
			certificateForm.setToCollege("MIC");
			initsetDataToForm(certificateForm,request);
		}
		catch (Exception e) {
			 String msg = super.handleApplicationException(e);
			 certificateForm.setErrorMessage(msg);
			 certificateForm.setErrorStack(e.getMessage());
	         return mapping.findForward(CMSConstants.ERROR_PAGE);
		}
		return mapping.findForward(CMSConstants.TRANSFER_CERTIFICATE_REPRINT);
	}
	
}
