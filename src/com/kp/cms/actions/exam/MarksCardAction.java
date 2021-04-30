package com.kp.cms.actions.exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.MarksCardSiNoGen;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.exceptions.DataNotFoundException;
import com.kp.cms.forms.exam.MarksCardForm;
import com.kp.cms.handlers.admin.ProgramTypeHandler;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.MarksCardHandler;
import com.kp.cms.to.admin.ProgramTypeTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.exam.IMarksCardTransaction;
import com.kp.cms.transactionsimpl.exam.MarksCardTransactionImpl;
import com.kp.cms.utilities.StudentRollNoComparator;

public class MarksCardAction extends BaseDispatchAction {
private static final Log log = LogFactory.getLog(MarksCardSiNoAction.class);
	
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward initMarksCard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		log.debug("entered into marks card init screen");
		
		MarksCardForm marksCardForm = (MarksCardForm)form;
		marksCardForm.setYear(null);
		marksCardForm.reset();
		setUserId(request, marksCardForm);
		List<ProgramTypeTO> programTypeList;
		try {
			programTypeList = ProgramTypeHandler.getInstance().getProgramType();
			marksCardForm.setProgramTypeList(programTypeList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("out of init screen");
		return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
	}

	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward generateMarksCard(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		log.debug("Enter into MArks Card Generate Screen");
		MarksCardForm marksCardForm = (MarksCardForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		errors = marksCardForm.validate(mapping, request);
		IMarksCardTransaction transaction = new MarksCardTransactionImpl();
		//raghu
		marksCardForm.setRadioId("");
		try{
			
			//if duplicate check valid reg no or not
			if(marksCardForm.getIsDuplicate().equalsIgnoreCase("yes")){
				if(marksCardForm.getRegNo()!=null && !marksCardForm.getRegNo().equalsIgnoreCase("")){
					validateRegNo(marksCardForm,request,errors);
				}else{
					errors.add("errors", new ActionError("knowledgepro.hostel.leave.registerNo.required"));
					saveErrors(request, errors);
				}
			}
			
			if(errors.isEmpty()){
				
				//this is for duplications
				if(marksCardForm.getIsDuplicate().equalsIgnoreCase("yes")){
					List<MarksCardSiNoGen> cardSiNoGens=new ArrayList<MarksCardSiNoGen>();
					List<MarksCardSiNoGen> cardSiNoGens1=new ArrayList<MarksCardSiNoGen>();
					//checking original record by setting dupplicate no
					marksCardForm.setIsDuplicate("");
					cardSiNoGens= transaction.getDataAvailable(marksCardForm);
					
					if(cardSiNoGens.size()==0){
						marksCardForm.setIsDuplicate("yes");
						errors.add("errors", new ActionError("knowledgepro.admin.markscard.no.original"));
						saveErrors(request, errors);
						setDataToForm(marksCardForm);
						return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
					}
					
					else{
						marksCardForm.setIsDuplicate("yes");
						cardSiNoGens1= transaction.getDataAvailable(marksCardForm);
						int size=cardSiNoGens1.size();
						
						if(size>5){
							errors.add("errors", new ActionError("knowledgepro.admin.markscard.no.generate.exceeded"));
							saveErrors(request, errors);
							setDataToForm(marksCardForm);
							return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
						}
						else{
							

							boolean isUpdate = MarksCardHandler.getInstance().updateSingleStudentDuplicate(marksCardForm, cardSiNoGens, cardSiNoGens1);
							
							if(isUpdate){
								ActionMessage message = new ActionMessage("knowledgepro.admin.markscard.no.generate.success");
								messages.add(CMSConstants.MESSAGES, message);
								saveMessages(request, messages);
								marksCardForm.reset();
								//marksCardForm.setExamType("Regular");
								setDataToForm(marksCardForm);
								return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
							}
							
							else{
								
								errors.add("errors", new ActionError("knowledgepro.admin.markscard.no.generate.failure"));
								saveErrors(request, errors);
								setDataToForm(marksCardForm);
								return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
								
							}
							 
						}
					}
					
					
					
					
				}
				
				//this is for not duplications
				else{
					
				
							//check for generate no for class
							int currentNO = MarksCardHandler.getInstance().getCurrentNO(marksCardForm);
							
							if(currentNO != -1){
								
								
								
								if(!marksCardForm.getExamType().equalsIgnoreCase("Regular")){
									
									if(!validateGenerateNoForExamType(marksCardForm,request,errors)){
										
										return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
									}
									
									//get data from supply or revaluation application
									List<StudentTO> studentIdForExamType = MarksCardHandler.getInstance().getStudentIdForExamType(marksCardForm);
									
									//StudentRollNoComparator sRollNo = new StudentRollNoComparator();
									//sRollNo.setRegNoCheck(false);
									//Collections.sort(studentIdForExamType, sRollNo);

									int halfLength = 0;
									int totLength = studentIdForExamType.size();
									if(totLength==0){
										errors.add("errors", new ActionError(CMSConstants.KNOWLEDGEPRO_NORECORDS));
										saveErrors(request, errors);
										setDataToForm(marksCardForm);
										return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
									}
									if (totLength % 2 == 0) {
										halfLength = totLength / 2;
									} else {
										halfLength = (totLength / 2) + 1;
									}
									
									marksCardForm.setHalfLength(halfLength);
									marksCardForm.setStudentList(studentIdForExamType);
									//get data from regular exam
									Map<Integer, MarksCardSiNoGen> studentMapForExamType = transaction.getStudentList1(marksCardForm);
									marksCardForm.setStudentMap(studentMapForExamType);
									if(studentMapForExamType.size()==0){
										errors.add("errors", new ActionError("knowledgepro.admin.markscard.no.original"));
										saveErrors(request, errors);
										setDataToForm(marksCardForm);
										return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
										
									}
									Map<Integer,String> classMap=CommonAjaxHandler.getInstance().getClassNameByExamNameForSupplementary(Integer.parseInt(marksCardForm.getExamName()));
									Map<Integer,String> examMap=CommonAjaxHandler.getInstance().getExamNameByAcademicYearAndExamType(marksCardForm.getYear(), marksCardForm.getExamType());
									
									marksCardForm.setClassName(classMap.get(Integer.parseInt(marksCardForm.getClassId())));
									marksCardForm.setExamNameId(examMap.get(Integer.parseInt(marksCardForm.getExamName())));
									
									return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE1);
								}
								//check not regular close
								
								
								
								List<Integer> studentId = transaction.getStudentId(marksCardForm);
								//getting already generate records
								Map<Integer, String> studentMaksCardNO = transaction.getStudentList(marksCardForm);
								//List<Integer> newStudentId = new ArrayList<Integer>();
								
								
								if(!studentId.isEmpty())
								{
									
								boolean isNotNew=false;
								Iterator<Integer> itr = studentId.iterator();
								//check for duplicate
								while (itr.hasNext()) {
									Integer integer = (Integer) itr.next();
									if(studentMaksCardNO.containsKey(integer)){
										isNotNew=true;
										}
//									else{
//											newStudentId.add(integer);
//										}
								}
								
								
								
								if(isNotNew){
									if(!marksCardForm.getRadioId().equalsIgnoreCase("1"))
									{
									  if(marksCardForm.getIsDuplicate().equalsIgnoreCase("yes")){
										//if(!studentId.isEmpty()){
											//boolean isDuplicate=MarksCardHandler.getInstance().checkDuplicate(marksCardForm,studentId);
											//boolean isStudentBelongsToClass=MarksCardHandler.getInstance().checkStudentClass(marksCardForm,studentId);
										//if(isDuplicate){
											/*boolean isUpdate = MarksCardHandler.getInstance().updateBalanceStudent(studentId,marksCardForm,currentNO);
											if(isUpdate){
												String totalCount = Integer.toString(currentNO+1);
												boolean isUpdated = MarksCardHandler.getInstance().update(totalCount,marksCardForm);
												ActionMessage message = new ActionMessage("knowledgepro.admin.markscard.no.generate.success");
												messages.add(CMSConstants.MESSAGES, message);
												saveMessages(request, messages);
												marksCardForm.reset();
												marksCardForm.setExamType("Regular");
												return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
											}*/
//											}else{
//												errors.add("errors", new ActionError("knowledgepro.admin.markscard.duplicate.regNo"));
//												saveErrors(request, errors);
//												marksCardForm.reset();
//												marksCardForm.setExamType("Regular");
//												return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
//											}
										//}else{
										//	errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_GENERATED_ALREADY_CLASS_WRONG));
										//	saveErrors(request, errors);
										//	marksCardForm.reset();
										//	marksCardForm.setExamType("Regular");
										//	setDataToForm(marksCardForm);
										//	return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
										//}
										
//									}else{
//										if(!newStudentId.isEmpty()){
//											boolean isUpdate = MarksCardHandler.getInstance().updateBalanceStudent(newStudentId,marksCardForm,currentNO);
//											if(isUpdate){
//												String totalCount = Integer.toString(currentNO+Integer.parseInt(marksCardForm.getTotalCount()));
//												boolean isCountUpdate = MarksCardHandler.getInstance().update(totalCount);
//												ActionMessage message = new ActionMessage("knowledgepro.admin.markscard.no.generate.success");
//												messages.add(CMSConstants.MESSAGES, message);
//												saveMessages(request, messages);
//												marksCardForm.reset();
//												marksCardForm.setExamType("Regular");
//												setDataToForm(marksCardForm);
//												return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
//											}
										}else{
											errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_GENERATED_ALREADY));
											saveErrors(request, errors);
											//marksCardForm.reset();
											//marksCardForm.setExamType("Regular");
											setDataToForm(marksCardForm);
											return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
										}
									  }
									  else{
											errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_GENERATED_ALREADY));
											saveErrors(request, errors);
											//marksCardForm.reset();
											//marksCardForm.setExamType("Regular");
											setDataToForm(marksCardForm);
											return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
										}
									
								}
								//duplication check over
								
//								else {
//									if(marksCardForm.getIsDuplicate().equalsIgnoreCase("yes")){
//										errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_GENERATED_YOU_CONT_WRONG));
//										saveErrors(request, errors);
//										marksCardForm.reset();
//										marksCardForm.setExamType("Regular");
//										setDataToForm(marksCardForm);
//										return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
//									}
								
								
								
									else{
										boolean isStudentUpdate = MarksCardHandler.getInstance().updateStudent(marksCardForm,currentNO);
										//boolean isDuplicate=MarksCardHandler.getInstance().checkDuplicate(marksCardForm,studentId);
										
										if(isStudentUpdate){
											String totalCount = Integer.toString(currentNO+Integer.parseInt(marksCardForm.getTotalCount()));
											boolean isUpdate = MarksCardHandler.getInstance().update(totalCount,marksCardForm);
											ActionMessage message = new ActionMessage("knowledgepro.admin.markscard.no.generate.success");
											messages.add(CMSConstants.MESSAGES, message);
											saveMessages(request, messages);
											marksCardForm.reset();
											//marksCardForm.setExamType("Regular");
											setDataToForm(marksCardForm);
											return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
									}
										
										
									//}
								}
								}
								//student empty if close
								
								
								else{
									errors.add("errors", new ActionError(CMSConstants.MARKSCARD_STUDENT_NOT_PRESENT));
									saveErrors(request, errors);
									//marksCardForm.reset();
									//marksCardForm.setExamType("Regular");
									setDataToForm(marksCardForm);
									return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
								}
								
								
							}
							//currentno if close
								else{
								errors.add("errors", new ActionError(CMSConstants.MARKSCARD_SI_NOT_AVAILABLE));
								saveErrors(request, errors);
								//marksCardForm.reset();
								//marksCardForm.setExamType("Regular");
								setDataToForm(marksCardForm);
								return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
							}
							
							
							
							
			}	
			//not duplicate check over				
							
							
							
							
							
			}
			//error if close
			
			else {
				saveErrors(request, errors);
				//marksCardForm.reset();
				//marksCardForm.setExamType("Regular");
				setDataToForm(marksCardForm);
				return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
				}
			
		}
		catch (DataNotFoundException e1) {
			errors.add("errors", new ActionError(CMSConstants.MARKSCARD_SI_NOT_AVAILABLE));
			saveErrors(request, errors);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
	}


	private void setDataToForm(MarksCardForm marksCardForm)throws Exception {
		// TODO Auto-generated method stub
		if(marksCardForm.getExamType()!=null && !StringUtils.isEmpty(marksCardForm.getExamType())){
			Map<Integer, String> listExamName = CommonAjaxHandler.getInstance().getExamNameByAcademicYearAndExamType(marksCardForm.getYear(),marksCardForm.getExamType());
			marksCardForm.setListExamName(listExamName);
		}
		if(marksCardForm.getExamName()!=null && !StringUtils.isEmpty(marksCardForm.getExamName())){
			Map<Integer, String> listClassName = CommonAjaxHandler.getInstance().getClassNameByExamName(Integer.parseInt(marksCardForm.getExamName()));
			marksCardForm.setListClassName(listClassName);
		}
		
	}


	private void validateRegNo(MarksCardForm marksCardForm,HttpServletRequest request,ActionErrors errors)throws Exception {
		// TODO Auto-generated method stub
		boolean isAvailable = MarksCardHandler.getInstance().checkRegNo(marksCardForm);
		if(!isAvailable){
			errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_GENERATED_ALREADY_REG));
			saveErrors(request, errors);
		}
	}
	
	private boolean validateGenerateNoForExamType(MarksCardForm marksCardForm,HttpServletRequest request,ActionErrors errors)throws Exception {
		// TODO Auto-generated method stub
		boolean isAvailable = MarksCardHandler.getInstance().validateGenerateNoForExamType(marksCardForm);
		if(!isAvailable){
			errors.add("errors", new ActionError(CMSConstants.MARKS_CARD_NOTGENERATED_REG));
			saveErrors(request, errors);
			
		}
		return isAvailable;
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward generateMarksCardForExamType(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		log.debug("Enter into MArks Card Generate Screen");
		MarksCardForm marksCardForm = (MarksCardForm)form;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		errors = marksCardForm.validate(mapping, request);
		IMarksCardTransaction transaction = new MarksCardTransactionImpl();
		try{
				if(errors.isEmpty()){
							
							boolean isStudentUpdate = transaction.updateStudentForExamType(marksCardForm);
										
							if(isStudentUpdate){
											ActionMessage message = new ActionMessage("knowledgepro.admin.markscard.no.generate.success");
											messages.add(CMSConstants.MESSAGES, message);
											saveMessages(request, messages);
											marksCardForm.reset();
											//marksCardForm.setExamType("Regular");
											setDataToForm(marksCardForm);
											return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
							}else {
											saveErrors(request, errors);
											//marksCardForm.reset();
											//marksCardForm.setExamType("Regular");
											setDataToForm(marksCardForm);
											return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
									}	
								
				}
				else {
				saveErrors(request, errors);
				//marksCardForm.reset();
				//marksCardForm.setExamType("Regular");
				setDataToForm(marksCardForm);
				return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
				}
			
		}
		catch (DataNotFoundException e1) {
			errors.add("errors", new ActionError(CMSConstants.MARKSCARD_SI_NOT_AVAILABLE));
			saveErrors(request, errors);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapping.findForward(CMSConstants.INIT_MARKS_CARD_GENERATE);
	}
}
