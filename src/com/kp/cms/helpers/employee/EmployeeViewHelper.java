package com.kp.cms.helpers.employee;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.util.Calendar;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Designation;
import com.kp.cms.bo.admin.EmpAcheivement;
import com.kp.cms.bo.admin.EmpDependents;
import com.kp.cms.bo.admin.EmpImmigration;
import com.kp.cms.bo.admin.EmpLeave;
import com.kp.cms.bo.admin.EmpLeaveType;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.employee.EmpEducationalDetails;
import com.kp.cms.bo.employee.EmpFeeConcession;
import com.kp.cms.bo.employee.EmpFinancial;
import com.kp.cms.bo.employee.EmpImages;
import com.kp.cms.bo.employee.EmpIncentives;
import com.kp.cms.bo.employee.EmpLoan;
import com.kp.cms.bo.employee.EmpPayAllowanceDetails;
import com.kp.cms.bo.employee.EmpPreviousExperience;
import com.kp.cms.bo.employee.EmpRemarks;
import com.kp.cms.forms.employee.EmployeeInfoViewForm;
import com.kp.cms.handlers.employee.EmployeeInfoEditHandler;
import com.kp.cms.handlers.employee.EmployeeViewHandler;
import com.kp.cms.to.admin.EmpAcheivementTO;
import com.kp.cms.to.admin.EmpAllowanceTO;
import com.kp.cms.to.admin.EmpDependentsTO;
import com.kp.cms.to.admin.EmpImmigrationTO;
import com.kp.cms.to.admin.EmployeeTO;
import com.kp.cms.to.employee.EmpFeeConcessionTO;
import com.kp.cms.to.employee.EmpFinancialTO;
import com.kp.cms.to.employee.EmpIncentivesTO;
import com.kp.cms.to.employee.EmpLeaveAllotTO;
import com.kp.cms.to.employee.EmpLoanTO;
import com.kp.cms.to.employee.EmpPreviousOrgTo;
import com.kp.cms.to.employee.EmpQualificationLevelTo;
import com.kp.cms.to.employee.EmpRemarksTO;
import com.kp.cms.transactions.employee.IEmployeeViewTransaction;
import com.kp.cms.transactionsimpl.employee.EmployeeViewTransactionImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.PayAllowance;

public class EmployeeViewHelper {
		
		private static volatile EmployeeViewHelper instance=null;
		
		/**
		 * 
		 */
		private EmployeeViewHelper(){
			
		}
		
		/**
		 * @return
		 */
		public static EmployeeViewHelper getInstance(){
			if(instance==null){
				instance=new EmployeeViewHelper();
			}
			return instance;
		}

		IEmployeeViewTransaction txn = new EmployeeViewTransactionImpl();

		
		/**
		 * @param EmployeeInfoViewForm
		 * @return
		 */
				
		public String getQueryByselectedEmpTypeId(String empTypeId) throws Exception {
			String query="from EmpType e where e.isActive=true and e.id= "+empTypeId; 
			return query;
		}
		
		public String getLeaveByEmpTypeId(String empTypeId) throws Exception {
			String query="from EmpLeaveAllotment r where r.isActive=true and r.empType.id="+empTypeId; 
			return query;
		}
		
		public String getQueryByselectedPayscaleId(String payScaleId) throws Exception {
			String query="select p.scale from PayScaleBO p where p.isActive=true and p.id="+payScaleId; 
			return query;
		}

		public void convertBoToForm(Employee empApplicantDetails,EmployeeInfoViewForm objform) throws Exception {
			if(empApplicantDetails!=null){
				if (empApplicantDetails.getEligibilityTest() != null && !empApplicantDetails.getEligibilityTest().isEmpty()) {
					if(empApplicantDetails.getEligibilityTestOther()!=null && !empApplicantDetails.getEligibilityTestOther().isEmpty())
					{
						String empEligibAddOther=empApplicantDetails.getEligibilityTest()+"  --Other description:-"+empApplicantDetails.getEligibilityTestOther();
						 objform.setEligibilityTestdisplay(empEligibAddOther);
					}
					else
						 objform.setEligibilityTestdisplay(empApplicantDetails.getEligibilityTest());
				}
				if(empApplicantDetails.getIndustryFunctionalArea()!=null && !empApplicantDetails.getIndustryFunctionalArea().trim().isEmpty()){
					objform.setIndustryFunctionalArea(empApplicantDetails.getIndustryFunctionalArea());
				}			
				if(empApplicantDetails.getReservationCategory() !=null && !empApplicantDetails.getReservationCategory().isEmpty()){
					if("GM".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						objform.setReservationCategory(empApplicantDetails.getReservationCategory());
					}
					if("SC".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						objform.setReservationCategory(empApplicantDetails.getReservationCategory());
					}
					if("ST".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						objform.setReservationCategory(empApplicantDetails.getReservationCategory());
					}
					if("OBC".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						objform.setReservationCategory(empApplicantDetails.getReservationCategory());
					}
					if("Minority".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						objform.setReservationCategory(empApplicantDetails.getReservationCategory());
					}
					if("Person With Disability".equalsIgnoreCase(empApplicantDetails.getReservationCategory())){
						
						String personWithDisability=empApplicantDetails.getReservationCategory()+"  --Handicap description:-"+empApplicantDetails.getHandicappedDescription();
							objform.setReservationCategory(personWithDisability);
						
						}
				}
				
				
				
				////...............................................................Photo.....................................................
			
				
				if(empApplicantDetails.getEmpImages()!=null && !empApplicantDetails.getEmpImages().isEmpty()){
					Iterator<EmpImages> itr=empApplicantDetails.getEmpImages().iterator();
					while (itr.hasNext()) {
						EmpImages bo = itr.next();
						if(bo.getEmpPhoto()!=null)
							objform.setPhotoBytes(bo.getEmpPhoto());
					}
				
				}
			////...............................................................Photo.....................................................
				if(empApplicantDetails.getId()>0){
					 objform.setEmployeeId(String.valueOf(empApplicantDetails.getId()));
				}
				if(StringUtils.isNotEmpty(String.valueOf(empApplicantDetails.getTeachingStaff()))){
					String Value= String.valueOf(empApplicantDetails.getTeachingStaff());
					if(Value.equals("true"))
						objform.setTeachingStaff("Teaching Staff");
					else
						objform.setTeachingStaff("Non-teaching Staff");
					 
				}
					
				
				if(StringUtils.isNotEmpty(String.valueOf(empApplicantDetails.getActive()))){
					String Value= String.valueOf(empApplicantDetails.getActive());
					if(Value.equals("true"))
						objform.setActive("Active");
					else
						objform.setActive("Not Active");
					 
				}
				if(StringUtils.isNotEmpty(String.valueOf(empApplicantDetails.getIsSameAddress()))){
					String Value= String.valueOf(empApplicantDetails.getIsSameAddress());
					if(Value.equals("true"))
						objform.setSameAddress("true");
					else
						objform.setSameAddress("false");
					 
				}
				if(StringUtils.isNotEmpty(String.valueOf(empApplicantDetails.getCurrentlyWorking()))){
					String Value= String.valueOf(empApplicantDetails.getCurrentlyWorking());
					if(Value.equals("true"))
						objform.setCurrentlyWorking("YES");
					else
						objform.setCurrentlyWorking("NO");
					 
				}
				
				if(empApplicantDetails.getPayScaleId()!=null && empApplicantDetails.getPayScaleId().getId()>0){
					  objform.setPayScaleId(String.valueOf(empApplicantDetails.getPayScaleId().getPayScale()));
				}
				
				if(StringUtils.isNotEmpty(empApplicantDetails.getFirstName()) && empApplicantDetails.getFirstName()!=null){
					  objform.setName(empApplicantDetails.getFirstName());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getSmartCardNo()) && empApplicantDetails.getSmartCardNo()!=null){
					  objform.setSmartCardNo(empApplicantDetails.getSmartCardNo());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getUid()) && empApplicantDetails.getUid()!=null){
					  objform.setuId(empApplicantDetails.getUid());
				}
				if(empApplicantDetails.getTitleId()!=null && empApplicantDetails.getTitleId().getId()>0){
					  objform.setTitleId(String.valueOf(empApplicantDetails.getTitleId().getTitle()));
				}
				if(empApplicantDetails.getDepartment()!=null && empApplicantDetails.getDepartment().getId()>0){
					  objform.setDepartmentId(String.valueOf(empApplicantDetails.getDepartment().getName()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCode())&& empApplicantDetails.getCode()!=null){
					  objform.setCode(empApplicantDetails.getCode());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getFingerPrintId()) && empApplicantDetails.getFingerPrintId()!=null){
					  objform.setFingerPrintId(empApplicantDetails.getFingerPrintId());
				}
				if(empApplicantDetails.getGender()!=null && !empApplicantDetails.getGender().isEmpty()){
					 objform.setGender(empApplicantDetails.getGender());
				}
				if(empApplicantDetails.getNationality()!=null && empApplicantDetails.getNationality().getId()>0){
					  objform.setNationalityId(String.valueOf(empApplicantDetails.getNationality().getName()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getMaritalStatus()) && empApplicantDetails.getMaritalStatus()!=null){
					  objform.setMaritalStatus(String.valueOf(empApplicantDetails.getMaritalStatus()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getBloodGroup()) && empApplicantDetails.getBloodGroup()!=null){
					  objform.setBloodGroup(String.valueOf(empApplicantDetails.getBloodGroup()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPanNo()) && empApplicantDetails.getPanNo()!=null){
					  objform.setPanno(String.valueOf(empApplicantDetails.getPanNo()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmail()) && empApplicantDetails.getEmail()!=null){
					  objform.setEmail(String.valueOf(empApplicantDetails.getEmail()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getWorkEmail()) && empApplicantDetails.getWorkEmail()!=null){
					  objform.setOfficialEmail(String.valueOf(empApplicantDetails.getWorkEmail()));
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCurrentAddressMobile1()) && empApplicantDetails.getCurrentAddressMobile1()!=null){
					  objform.setMobileNo1(empApplicantDetails.getCurrentAddressMobile1());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getBankAccNo()) && empApplicantDetails.getBankAccNo()!=null){
					  objform.setBankAccNo(empApplicantDetails.getBankAccNo());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPfNo()) && empApplicantDetails.getPfNo()!=null){
					  objform.setPfNo(empApplicantDetails.getPfNo());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getFourWheelerNo()) && empApplicantDetails.getFourWheelerNo()!=null){
					  objform.setFourWheelerNo(empApplicantDetails.getFourWheelerNo());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getTwoWheelerNo()) && empApplicantDetails.getTwoWheelerNo()!=null){
					  objform.setTwoWheelerNo(empApplicantDetails.getTwoWheelerNo());
				}
				
				if(empApplicantDetails.getReligionId()!=null && empApplicantDetails.getReligionId().getId()>0){
					  objform.setReligionId(String.valueOf(empApplicantDetails.getReligionId().getName()));
				}
				if(empApplicantDetails.getEmptype()!=null && empApplicantDetails.getEmptype().getId()>0){
					  objform.setEmptypeId(String.valueOf(empApplicantDetails.getEmptype().getName()));
				}
				if(empApplicantDetails.getEmpQualificationLevel()!=null && empApplicantDetails.getEmpQualificationLevel().getId()>0){
					  objform.setQualificationId(String.valueOf(empApplicantDetails.getEmpQualificationLevel().getName()));
				}
				//Modification    ........................................................
				
				if(StringUtils.isNotEmpty(empApplicantDetails.getPermanentAddressLine1()) && empApplicantDetails.getPermanentAddressLine1()!=null){
					  objform.setAddressLine1(empApplicantDetails.getPermanentAddressLine1());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPermanentAddressLine2()) && empApplicantDetails.getPermanentAddressLine2()!=null ){
					  objform.setAddressLine2(empApplicantDetails.getPermanentAddressLine2());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPermanentAddressCity()) && empApplicantDetails.getPermanentAddressCity()!=null){
					  objform.setCity(empApplicantDetails.getPermanentAddressCity());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPermanentAddressZip()) && empApplicantDetails.getPermanentAddressZip()!=null){
					  objform.setPermanentZipCode(empApplicantDetails.getPermanentAddressZip());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPermanentAddressStateOthers()) && empApplicantDetails.getPermanentAddressStateOthers()!=null){
					  objform.setOtherPermanentState(empApplicantDetails.getPermanentAddressStateOthers());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCommunicationAddressLine1()) && empApplicantDetails.getCommunicationAddressLine1()!=null){
					  objform.setCurrentAddressLine1(empApplicantDetails.getCommunicationAddressLine1());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCommunicationAddressLine2()) && empApplicantDetails.getCommunicationAddressLine2()!=null){
					  objform.setCurrentAddressLine2(empApplicantDetails.getCommunicationAddressLine2());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCommunicationAddressCity()) && empApplicantDetails.getCommunicationAddressCity()!=null){
					  objform.setCurrentCity(empApplicantDetails.getCommunicationAddressCity());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCommunicationAddressStateOthers()) && empApplicantDetails.getCommunicationAddressStateOthers()!=null){
					  objform.setOtherCurrentState(empApplicantDetails.getCommunicationAddressStateOthers());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getCommunicationAddressZip()) && empApplicantDetails.getCommunicationAddressZip()!=null){
					  objform.setCurrentZipCode(empApplicantDetails.getCommunicationAddressZip());
				}
				if(empApplicantDetails.getCountryByPermanentAddressCountryId()!=null && empApplicantDetails.getCountryByPermanentAddressCountryId().getId()>0){
					  objform.setCountryId(String.valueOf(empApplicantDetails.getCountryByPermanentAddressCountryId().getName()));
				}
				if(empApplicantDetails.getCountryByCommunicationAddressCountryId()!=null && empApplicantDetails.getCountryByCommunicationAddressCountryId().getId()>0){
					  objform.setCurrentCountryId(String.valueOf(empApplicantDetails.getCountryByCommunicationAddressCountryId().getName()));
				}
				if(empApplicantDetails.getCurrentAddressHomeTelephone1()!=null && !empApplicantDetails.getCurrentAddressHomeTelephone1().isEmpty()){
					objform.setHomePhone1(empApplicantDetails.getCurrentAddressHomeTelephone1());
				}
				
				if(empApplicantDetails.getCurrentAddressHomeTelephone2()!=null && !empApplicantDetails.getCurrentAddressHomeTelephone2().isEmpty()){
					objform.setHomePhone2(empApplicantDetails.getCurrentAddressHomeTelephone2());
				}
				
				if(empApplicantDetails.getCurrentAddressHomeTelephone3()!=null && !empApplicantDetails.getCurrentAddressHomeTelephone3().isEmpty()){
					objform.setHomePhone3(empApplicantDetails.getCurrentAddressHomeTelephone3());
				}
				if(empApplicantDetails.getCurrentAddressWorkTelephone1()!=null && !empApplicantDetails.getCurrentAddressWorkTelephone1().isEmpty()){
					objform.setWorkPhNo1(empApplicantDetails.getCurrentAddressWorkTelephone1());
				}
				
				if(empApplicantDetails.getCurrentAddressWorkTelephone2()!=null && !empApplicantDetails.getCurrentAddressWorkTelephone2().isEmpty()){
					objform.setWorkPhNo2(empApplicantDetails.getCurrentAddressWorkTelephone2());
				}
				
				if(empApplicantDetails.getCurrentAddressWorkTelephone3()!=null && !empApplicantDetails.getCurrentAddressWorkTelephone3().isEmpty()){
					objform.setWorkPhNo3(empApplicantDetails.getCurrentAddressWorkTelephone3());
				}

				if(StringUtils.isNotEmpty(empApplicantDetails.getDesignationName()) && empApplicantDetails.getDesignationName()!=null){
					  objform.setDesignation(empApplicantDetails.getDesignationName());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getOrganistionName()) && empApplicantDetails.getOrganistionName()!=null ){
					  objform.setOrgAddress(empApplicantDetails.getOrganistionName());
				}
				if(empApplicantDetails.getDesignation()!=null && empApplicantDetails.getDesignation().getId()>0){
					  objform.setDesignationPfId(String.valueOf(empApplicantDetails.getDesignation().getName()));
				}
				if(empApplicantDetails.getDepartment()!=null && empApplicantDetails.getDepartment().getId()>0){
					  objform.setDepartmentId(String.valueOf(empApplicantDetails.getDepartment().getName()));
				}
				if(empApplicantDetails.getEmployeeByReportToId()!=null && empApplicantDetails.getEmployeeByReportToId().getId()>0){
					  objform.setReportToId(String.valueOf(empApplicantDetails.getEmployeeByReportToId().getFirstName()));
				}
			/*	if(empApplicantDetails.getGrade()!=null && !empApplicantDetails.getGrade().isEmpty()){
					  objform.setGrade(String.valueOf(empApplicantDetails.getGrade()));
				}*/
				/*if(StringUtils.isNotEmpty(empApplicantDetails.getCurrentOrganization()) && empApplicantDetails.getCurrentOrganization()!=null){
					  objform.setOrgAddress(empApplicantDetails.getCurrentOrganization());
				}*/
				if(empApplicantDetails.getBooks()!=null && !empApplicantDetails.getBooks().isEmpty()){
					  objform.setBooks(String.valueOf(empApplicantDetails.getBooks()));
				}
				if(empApplicantDetails.getNoOfPublicationsNotRefered()!=null && !empApplicantDetails.getNoOfPublicationsNotRefered().isEmpty()){
					  objform.setNoOfPublicationsNotRefered(empApplicantDetails.getNoOfPublicationsNotRefered());
				}
				if(empApplicantDetails.getNoOfPublicationsRefered()!=null && !empApplicantDetails.getNoOfPublicationsRefered().isEmpty()){
					  objform.setNoOfPublicationsRefered(empApplicantDetails.getNoOfPublicationsRefered());
				}
								
				if(empApplicantDetails.getEmpQualificationLevel()!=null && empApplicantDetails.getEmpQualificationLevel().getId()>0){
					  objform.setQualificationId(String.valueOf(empApplicantDetails.getEmpQualificationLevel().getName()));
				}
				if(empApplicantDetails.getDesignation()!=null && empApplicantDetails.getDesignation().getId()>0){
					  objform.setDesignationPfId(String.valueOf(empApplicantDetails.getDesignation().getName()));
				}
				if(empApplicantDetails.getEmpSubjectArea()!=null && empApplicantDetails.getEmpSubjectArea().getId()>0){
					  objform.setEmpSubjectAreaId(String.valueOf(empApplicantDetails.getEmpSubjectArea().getName()));
				}
				if(empApplicantDetails.getDob()!=null && !empApplicantDetails.getDob().toString().isEmpty() ){
					objform.setDateOfBirth(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getDob().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getDateOfLeaving()!=null && !empApplicantDetails.getDateOfLeaving().toString().isEmpty()){
					objform.setDateOfLeaving(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getDateOfLeaving().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getDateOfResignation()!=null && !empApplicantDetails.getDateOfResignation().toString().isEmpty()){
					objform.setDateOfResignation(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getDateOfResignation().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getDoj()!=null && !empApplicantDetails.getDoj().toString().isEmpty()){
					objform.setDateOfJoining(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getDoj().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getRejoinDate()!=null && !empApplicantDetails.getRejoinDate().toString().isEmpty()){
					objform.setRejoinDate(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getRejoinDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getRetirementDate()!=null && !empApplicantDetails.getRetirementDate().toString().isEmpty()){
					objform.setRetirementDate(CommonUtil.ConvertStringToDateFormat(empApplicantDetails.getRetirementDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
				}
				if(empApplicantDetails.getTotalExpMonths()!=null && !empApplicantDetails.getTotalExpMonths().isEmpty()){
					  objform.setExpMonths(String.valueOf(empApplicantDetails.getTotalExpMonths()));
				}
				if(empApplicantDetails.getTotalExpYear()!=null && !empApplicantDetails.getTotalExpYear().isEmpty()){
					  objform.setExpYears(String.valueOf(empApplicantDetails.getTotalExpYear()));
				}
				/*if(empApplicantDetails.getActive()){
					  objform.setActive(String.valueOf(empApplicantDetails.getActive()));
				}*/
				
				
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmergencyContName()) && empApplicantDetails.getEmergencyContName()!=null){
					  objform.setEmContactName(empApplicantDetails.getEmergencyContName());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmContactAddress()) && empApplicantDetails.getEmContactAddress()!=null){
					  objform.setEmContactAddress(empApplicantDetails.getEmContactAddress());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmergencyHomeTelephone()) && empApplicantDetails.getEmergencyHomeTelephone()!=null){
					  objform.setEmContactHomeTel(empApplicantDetails.getEmergencyHomeTelephone());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmergencyMobile()) && empApplicantDetails.getEmergencyMobile()!=null){
					  objform.setEmContactMobile(empApplicantDetails.getEmergencyMobile());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getEmergencyWorkTelephone()) && empApplicantDetails.getEmergencyWorkTelephone()!=null){
					  objform.setEmContactWorkTel(empApplicantDetails.getEmergencyWorkTelephone());
				}
				
				if(StringUtils.isNotEmpty(empApplicantDetails.getGrossPay()) && empApplicantDetails.getGrossPay()!=null){
					  objform.setGrossPay(empApplicantDetails.getGrossPay());
				}
				/*if(StringUtils.isNotEmpty(empApplicantDetails.getHalfDayEndTime())){
					  objform.setHalfDayEndTime(empApplicantDetails.getHalfDayEndTime());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getHalfDayStartTime())){
					  objform.setHalfDayStartTime(empApplicantDetails.getHalfDayStartTime());
				}*/
				if(StringUtils.isNotEmpty(empApplicantDetails.getHighQualifForAlbum()) && empApplicantDetails.getHighQualifForAlbum()!=null){
					  objform.setHighQualifForAlbum(empApplicantDetails.getHighQualifForAlbum());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getOtherInfo()) && empApplicantDetails.getOtherInfo()!=null){
					  objform.setOtherInfo(empApplicantDetails.getOtherInfo());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getWorkEmail()) && empApplicantDetails.getWorkEmail()!=null){
					  objform.setOfficialEmail(empApplicantDetails.getWorkEmail());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getPanNo()) && empApplicantDetails.getPanNo()!=null){
					  objform.setPanno(empApplicantDetails.getPanNo());
				}
				
							
				
				if(StringUtils.isNotEmpty(empApplicantDetails.getReasonOfLeaving()) && empApplicantDetails.getReasonOfLeaving()!=null){
					  objform.setReasonOfLeaving(empApplicantDetails.getReasonOfLeaving());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getRelationship()) && empApplicantDetails.getRelationship()!=null){
					  objform.setEmContactRelationship(empApplicantDetails.getRelationship());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getRelevantExpMonths()) && empApplicantDetails.getRelevantExpMonths()!=null ){
					  objform.setRelevantExpMonths(empApplicantDetails.getRelevantExpMonths());
				}
				if(StringUtils.isNotEmpty(empApplicantDetails.getRelevantExpYears()) && empApplicantDetails.getRelevantExpYears()!=null ){
					  objform.setRelevantExpYears(empApplicantDetails.getRelevantExpYears());
				}
				/*if(StringUtils.isNotEmpty(empApplicantDetails.getSaturdayTimeOut())){
					  objform.setSaturdayTimeOut(empApplicantDetails.getSaturdayTimeOut());
				}*/
				if(StringUtils.isNotEmpty(empApplicantDetails.getScale()) && empApplicantDetails.getScale()!=null){
					  objform.setScale(empApplicantDetails.getScale());
				}
				if(empApplicantDetails.getStateByCommunicationAddressStateId()!=null && empApplicantDetails.getStateByCommunicationAddressStateId().getId()>0){
					  objform.setCurrentState(String.valueOf(empApplicantDetails.getStateByCommunicationAddressStateId().getName()));
				}
				if(empApplicantDetails.getStateByPermanentAddressStateId()!=null && empApplicantDetails.getStateByPermanentAddressStateId().getId()>0){
					  objform.setStateId(String.valueOf(empApplicantDetails.getStateByPermanentAddressStateId().getName()));
				}
				if(empApplicantDetails.getStreamId() != null && empApplicantDetails.getStreamId().getId()>0){
					objform.setStreamId(String.valueOf(empApplicantDetails.getStreamId().getName()));
					
				}
				
				if(empApplicantDetails.getTimeIn()!=null && !empApplicantDetails.getTimeIn().isEmpty()){
					objform.setTimeIn(empApplicantDetails.getTimeIn().substring(0,2));
					objform.setTimeInMin(empApplicantDetails.getTimeIn().substring(3,5));
				}
			
				if(empApplicantDetails.getTimeInEnds()!=null && !empApplicantDetails.getTimeInEnds().isEmpty()){
					objform.setTimeInEnds(empApplicantDetails.getTimeInEnds().substring(0,2));
					objform.setTimeInEndMIn(empApplicantDetails.getTimeInEnds().substring(3,5));
				}
				
				if(empApplicantDetails.getTimeOut()!=null && !empApplicantDetails.getTimeOut().isEmpty()){
					objform.setTimeOut(empApplicantDetails.getTimeOut().substring(0,2));
					objform.setTimeOutMin(empApplicantDetails.getTimeOut().substring(3,5));
				}
				
				if(empApplicantDetails.getSaturdayTimeOut()!=null && !empApplicantDetails.getSaturdayTimeOut().isEmpty()){
					objform.setSaturdayTimeOut(empApplicantDetails.getSaturdayTimeOut().substring(0,2));
					objform.setSaturdayTimeOutMin(empApplicantDetails.getSaturdayTimeOut().substring(3,5));
				}
				
				if(empApplicantDetails.getHalfDayStartTime()!=null && !empApplicantDetails.getHalfDayStartTime().isEmpty()){
					objform.setHalfDayStartTime(empApplicantDetails.getHalfDayStartTime().substring(0,2));
					objform.setHalfDayStartTimeMin(empApplicantDetails.getHalfDayStartTime().substring(3,5));
				}
				
				if(empApplicantDetails.getHalfDayEndTime()!=null && !empApplicantDetails.getHalfDayEndTime().isEmpty()){
					objform.setHalfDayEndTime(empApplicantDetails.getHalfDayEndTime().substring(0,2));
					objform.setHalfDayEndTimeMin(empApplicantDetails.getHalfDayEndTime().substring(3,5));
				}
			
				
				
				
				
				
				
				
				
				/*if(empApplicantDetails.getTimeIn()!=null && !empApplicantDetails.getTimeIn().isEmpty()){
					objform.setTimeIn(empApplicantDetails.getTimeIn().substring(0,2));
					objform.setTimeInMin(empApplicantDetails.getTimeIn().substring(3,5));
				}
			
				if(empApplicantDetails.getTimeInEnds()!=null && !empApplicantDetails.getTimeInEnds().isEmpty()){
					objform.setTimeInEnds(empApplicantDetails.getTimeInEnds().substring(0,2));
					objform.setTimeInEndMIn(empApplicantDetails.getTimeInEnds().substring(3,5));
				}
				
				if(empApplicantDetails.getTimeOut()!=null && !empApplicantDetails.getTimeOut().isEmpty()){
					objform.setTimeOut(empApplicantDetails.getTimeOut().substring(0,2));
					objform.setTimeOutMin(empApplicantDetails.getTimeOut().substring(3,5));
				}
				
				if(empApplicantDetails.getSaturdayTimeOut()!=null && !empApplicantDetails.getSaturdayTimeOut().isEmpty()){
					objform.setSaturdayTimeOut(empApplicantDetails.getSaturdayTimeOut().substring(0,2));
					objform.setSaturdayTimeOutMin(empApplicantDetails.getSaturdayTimeOut().substring(3,5));
				}
				
				if(empApplicantDetails.getHalfDayStartTime()!=null && !empApplicantDetails.getHalfDayStartTime().isEmpty()){
					objform.setHalfDayStartTime(empApplicantDetails.getHalfDayStartTime().substring(0,2));
					objform.setHalfDayStartTimeMin(empApplicantDetails.getHalfDayStartTime().substring(3,5));
				}
				
				if(empApplicantDetails.getHalfDayEndTime()!=null && !empApplicantDetails.getHalfDayEndTime().isEmpty()){
					objform.setHalfDayEndTime(empApplicantDetails.getHalfDayEndTime().substring(0,2));
					objform.setHalfDayEndTimeMin(empApplicantDetails.getHalfDayEndTime().substring(3,5));
				}*/
								
				if(StringUtils.isNotEmpty(empApplicantDetails.getWorkEmail()) && empApplicantDetails.getWorkEmail()!=null){
					  objform.setOfficialEmail(empApplicantDetails.getWorkEmail());
				}
				if(empApplicantDetails.getWorkLocationId()!=null && empApplicantDetails.getWorkLocationId().getId()>0){
					  objform.setWorkLocationId(String.valueOf(empApplicantDetails.getWorkLocationId().getName()));
				}
				if(empApplicantDetails.getEmptype()!=null && empApplicantDetails.getEmptype().getId()>0){
					  objform.setEmptypeId(String.valueOf(empApplicantDetails.getEmptype().getName()));
				}
				
				if(empApplicantDetails.getEducationalDetailsSet()!=null){
					List<EmpQualificationLevelTo> fixed=null;
					if(objform.getEmployeeInfoTONew()!=null){
						if(objform.getEmployeeInfoTONew().getEmpQualificationFixedTo()!=null){
							fixed=objform.getEmployeeInfoTONew().getEmpQualificationFixedTo();
						}
						List<EmpQualificationLevelTo> level=new ArrayList<EmpQualificationLevelTo>();
						Set<EmpEducationalDetails> empEducationalDetailsSet=empApplicantDetails.getEducationalDetailsSet();
						Iterator<EmpEducationalDetails> iterator=empEducationalDetailsSet.iterator();
						while(iterator.hasNext()){
							EmpEducationalDetails empEducationalDetails=iterator.next();
							if(empEducationalDetails!=null){
								boolean flag=false;
								if(empEducationalDetails.getEmpQualificationLevel()!=null 
										&& empEducationalDetails.getEmpQualificationLevel().isFixedDisplay()!=null){
									flag=empEducationalDetails.getEmpQualificationLevel().isFixedDisplay();
									if(flag && fixed!=null){
										Iterator<EmpQualificationLevelTo> iterator2=fixed.iterator();
										while(iterator2.hasNext()){
											EmpQualificationLevelTo empQualificationLevelTo=iterator2.next();
											if(empQualificationLevelTo!=null && StringUtils.isNotEmpty(empQualificationLevelTo.getEducationId())){
												if(empEducationalDetails.getEmpQualificationLevel().getId()>0)
													if(empQualificationLevelTo.getEducationId().equalsIgnoreCase(String.valueOf(empEducationalDetails.getEmpQualificationLevel().getId()))){
														
														if(empEducationalDetails.getId()>0){
															empQualificationLevelTo.setQualification(empEducationalDetails.getEmpQualificationLevel().getName());
															}
														
														if(StringUtils.isNotEmpty(empEducationalDetails.getCourse())){
															empQualificationLevelTo.setCourse(empEducationalDetails.getCourse());
														}
														
														
														if(StringUtils.isNotEmpty(empEducationalDetails.getSpecialization())){
															empQualificationLevelTo.setSpecialization(empEducationalDetails.getSpecialization());
														}
														
														if(StringUtils.isNotEmpty(empEducationalDetails.getGrade())){
															empQualificationLevelTo.setGrade(empEducationalDetails.getGrade());
														}
														
														if(StringUtils.isNotEmpty(empEducationalDetails.getInstitute())){
															empQualificationLevelTo.setInstitute(empEducationalDetails.getInstitute());
														}
														
														if(empEducationalDetails.getYearOfCompletion()>0){
															empQualificationLevelTo.setYearOfComp(String.valueOf(empEducationalDetails.getYearOfCompletion()));
														}
													}
											}
										}
									}else{
										EmpQualificationLevelTo empQualificationLevelTo=new EmpQualificationLevelTo();
										
										if(empEducationalDetails.getId()>0){
											empQualificationLevelTo.setQualification(empEducationalDetails.getEmpQualificationLevel().getName());
											}
										if(StringUtils.isNotEmpty(empEducationalDetails.getCourse())){
												empQualificationLevelTo.setCourse(empEducationalDetails.getCourse());
											}
											
											if(StringUtils.isNotEmpty(empEducationalDetails.getSpecialization())){
												empQualificationLevelTo.setSpecialization(empEducationalDetails.getSpecialization());
											}
											
											if(StringUtils.isNotEmpty(empEducationalDetails.getGrade())){
												empQualificationLevelTo.setGrade(empEducationalDetails.getGrade());
											}
											
											if(StringUtils.isNotEmpty(empEducationalDetails.getInstitute())){
												empQualificationLevelTo.setInstitute(empEducationalDetails.getInstitute());
											}
											
											if(empEducationalDetails.getYearOfCompletion()>0){
												empQualificationLevelTo.setYearOfComp(String.valueOf(empEducationalDetails.getYearOfCompletion()));
											}
											if(empEducationalDetails.getEmpQualificationLevel().getId()>0){
												empQualificationLevelTo.setEducationId(String.valueOf(empEducationalDetails.getEmpQualificationLevel().getId()));
											}
										level.add(empQualificationLevelTo);
									}
										
									}
								}
						}
						objform.getEmployeeInfoTONew().setEmpQualificationLevelTos(level);
					}
				}
			}
				
				
				
				
			if(empApplicantDetails.getEmpAcheivements()!=null)
			{
				Set<EmpAcheivement> empAcheivements=empApplicantDetails.getEmpAcheivements();
				if(empAcheivements != null && !empAcheivements.isEmpty())
				{
				Iterator<EmpAcheivement> iterator=empAcheivements.iterator();
				List<EmpAcheivementTO> empAcheivementTOs=new ArrayList<EmpAcheivementTO>();
				
				while(iterator.hasNext()){
					EmpAcheivement empAcheiv=iterator.next();
					if(empAcheiv!=null){
						EmpAcheivementTO empAcheivementTO=new EmpAcheivementTO();
					
						if(empAcheiv.getId()>0)
						{
							empAcheivementTO.setId(empAcheiv.getId());
						}
						if(StringUtils.isNotEmpty(empAcheiv.getAcheivementName())){
							empAcheivementTO.setAcheivementName(empAcheiv.getAcheivementName());
						}
						
						if(StringUtils.isNotEmpty(empAcheiv.getDetails())){
							empAcheivementTO.setDetails(empAcheiv.getDetails());
						}
						
						empAcheivementTOs.add(empAcheivementTO);
					
				
			}
					}
				objform.getEmployeeInfoTONew().setEmpAcheivements(empAcheivementTOs);
				}
			else
			{
				
				List<EmpAcheivementTO> flist=new ArrayList<EmpAcheivementTO>();
				EmpAcheivementTO empAcheivementTO=new EmpAcheivementTO();
				empAcheivementTO.setAcheivementName("");
				empAcheivementTO.setDetails("");
				objform.setAchievementListSize(String.valueOf(flist.size()));
				flist.add(empAcheivementTO);
				objform.getEmployeeInfoTONew().setEmpAcheivements(flist);
			}
			}
			
			if(empApplicantDetails.getEmpDependentses()!=null)
			{
				Set<EmpDependents> empDependents=empApplicantDetails.getEmpDependentses();
				if(empDependents != null && !empDependents.isEmpty())
				{
				Iterator<EmpDependents> iterator=empDependents.iterator();
				List<EmpDependentsTO> empDependentsTOs=new ArrayList<EmpDependentsTO>();
				
				while(iterator.hasNext()){
					EmpDependents empDepen=iterator.next();
					if(empDepen!=null){
						EmpDependentsTO empDepenTOs=new EmpDependentsTO();
					
						if(empDepen.getId()>0)
						{
							empDepenTOs.setId(String.valueOf(empDepen.getId()));
						}
						if(StringUtils.isNotEmpty(empDepen.getDependentName())){
							empDepenTOs.setDependantName(empDepen.getDependentName());
						}
						
						if(StringUtils.isNotEmpty(empDepen.getDependentRelationship())){
							empDepenTOs.setDependentRelationship(empDepen.getDependentRelationship());
						}
						
						if(empDepen.getDependantDOB()!=null){
							empDepenTOs.setDependantDOB(CommonUtil.ConvertStringToDateFormat(empDepen.getDependantDOB().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empDependentsTOs.add(empDepenTOs);
					
				
			}
			}
				objform.getEmployeeInfoTONew().setEmpDependentses(empDependentsTOs);
				}
			else
			{
				List<EmpDependentsTO> empDependentses=new ArrayList<EmpDependentsTO>();
				EmpDependentsTO empDependentsTO=new EmpDependentsTO();
				empDependentsTO.setDependantDOB("");
				empDependentsTO.setDependantName("");
				empDependentsTO.setDependentRelationship("");
				objform.setDependantsListSize(String.valueOf(empDependentses.size()));
				empDependentses.add(empDependentsTO);
				
				objform.getEmployeeInfoTONew().setEmpDependentses(empDependentses);
				
			}
			}
			
			if(empApplicantDetails.getEmpFeeConcession()!=null)
			{
				Set<EmpFeeConcession> empFeeConcession=empApplicantDetails.getEmpFeeConcession();
				if(empFeeConcession != null && !empFeeConcession.isEmpty())
				{
				Iterator<EmpFeeConcession> iterator=empFeeConcession.iterator();
				List<EmpFeeConcessionTO> empFeeConcessionTOs=new ArrayList<EmpFeeConcessionTO>();
				
				while(iterator.hasNext()){
					EmpFeeConcession empFeeConc=iterator.next();
					if(empFeeConc!=null){
						EmpFeeConcessionTO empFeeConcTO=new EmpFeeConcessionTO();
						if(empFeeConc.getId()>0)
						{
							empFeeConcTO.setId(empFeeConc.getId());
						}
						if(StringUtils.isNotEmpty(empFeeConc.getFeeConcessionDetails())){
							empFeeConcTO.setFeeConcessionDetails(empFeeConc.getFeeConcessionDetails());
						}
						
						if(StringUtils.isNotEmpty(empFeeConc.getFeeConcessionAmount())){
							empFeeConcTO.setFeeConcessionAmount(empFeeConc.getFeeConcessionAmount());
						}
						
						if(empFeeConc.getFeeConcessionDate()!=null){
							empFeeConcTO.setFeeConcessionDate(CommonUtil.ConvertStringToDateFormat(empFeeConc.getFeeConcessionDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empFeeConcessionTOs.add(empFeeConcTO);
					
				
			}
			}objform.getEmployeeInfoTONew().setEmpFeeConcession(empFeeConcessionTOs);
				}
			else
			{
				List<EmpFeeConcessionTO> list=new ArrayList<EmpFeeConcessionTO>();
				EmpFeeConcessionTO empFeeConcessionTO=new EmpFeeConcessionTO();
				empFeeConcessionTO.setFeeConcessionAmount("");
				empFeeConcessionTO.setFeeConcessionDate("");
				empFeeConcessionTO.setFeeConcessionDetails("");
				objform.setFeeListSize(String.valueOf(list.size()));
				list.add(empFeeConcessionTO);
				objform.getEmployeeInfoTONew().setEmpFeeConcession(list);
				
			}
			}
			
			if(empApplicantDetails.getEmpFinancial()!=null)
			{
				Set<EmpFinancial> empFinancial=empApplicantDetails.getEmpFinancial();
				if(empFinancial != null && !empFinancial.isEmpty())
				{
				Iterator<EmpFinancial> iterator=empFinancial.iterator();
				List<EmpFinancialTO> empFinancialTOs=new ArrayList<EmpFinancialTO>();
				
				while(iterator.hasNext()){
					EmpFinancial empFinancial2=iterator.next();
					if(empFinancial2!=null){
						EmpFinancialTO empFinancialTO=new EmpFinancialTO();
						if(empFinancial2.getId()>0)
						{
							empFinancialTO.setId(empFinancial2.getId());
						}
						if(StringUtils.isNotEmpty(empFinancial2.getFinancialAmount())){
							empFinancialTO.setFinancialAmount(empFinancial2.getFinancialAmount());
						}
						
						if(StringUtils.isNotEmpty(empFinancial2.getFinancialDetails())){
							empFinancialTO.setFinancialDetails(empFinancial2.getFinancialDetails());
						}
						
						if(empFinancial2.getFinancialDate()!=null){
							empFinancialTO.setFinancialDate(CommonUtil.ConvertStringToDateFormat(empFinancial2.getFinancialDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empFinancialTOs.add(empFinancialTO);
					
				
			}
			}objform.getEmployeeInfoTONew().setEmpFinancial(empFinancialTOs);
				}
				else
				{
					List<EmpFinancialTO> flist=new ArrayList<EmpFinancialTO>();
					EmpFinancialTO empFinancialTO=new EmpFinancialTO();
					empFinancialTO.setFinancialAmount("");
					empFinancialTO.setFinancialDate("");
					empFinancialTO.setFinancialDetails("");
					objform.setFinancialListSize(String.valueOf(flist.size()));
					flist.add(empFinancialTO);
					objform.getEmployeeInfoTONew().setEmpFinancial(flist);
					
				}
			}
			
			if(empApplicantDetails.getEmpIncentives()!=null)
			{
				Set<EmpIncentives> empIncentives=empApplicantDetails.getEmpIncentives();
				if(empIncentives != null && !empIncentives.isEmpty())
				{
				Iterator<EmpIncentives> iterator=empIncentives.iterator();
				List<EmpIncentivesTO> empIncentivesTOs=new ArrayList<EmpIncentivesTO>();
				
				while(iterator.hasNext()){
					EmpIncentives empIncen=iterator.next();
					if(empIncen!=null){
						EmpIncentivesTO empIncentivesTO=new EmpIncentivesTO();
					
						if(empIncen.getId()>0)
						{
							empIncentivesTO.setId(empIncen.getId());
						}
						if(StringUtils.isNotEmpty(empIncen.getIncentivesAmount())){
							empIncentivesTO.setIncentivesAmount(empIncen.getIncentivesAmount());
						}
						
						if(StringUtils.isNotEmpty(empIncen.getIncentivesDetails())){
							empIncentivesTO.setIncentivesDetails(empIncen.getIncentivesDetails());
						}
						
						if(empIncen.getIncentivesDate()!=null){
							empIncentivesTO.setIncentivesDate(CommonUtil.ConvertStringToDateFormat(empIncen.getIncentivesDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empIncentivesTOs.add(empIncentivesTO);
					
				
			}
			}
				objform.getEmployeeInfoTONew().setEmpIncentives(empIncentivesTOs);
				}
				else
				{
					List<EmpIncentivesTO> list=new ArrayList<EmpIncentivesTO>();
					EmpIncentivesTO empIncentivesTO=new EmpIncentivesTO();
					empIncentivesTO.setIncentivesAmount("");
					empIncentivesTO.setIncentivesDate("");
					empIncentivesTO.setIncentivesDetails("");
					objform.setIncentivesListSize(String.valueOf(list.size()));
					list.add(empIncentivesTO);
					objform.getEmployeeInfoTONew().setEmpIncentives(list);
				}
			}
			if(empApplicantDetails.getEmpLoan()!=null)
			{
				Set<EmpLoan> empLoan=empApplicantDetails.getEmpLoan();
				if(empLoan != null && !empLoan.isEmpty())
				{
				Iterator<EmpLoan> iterator=empLoan.iterator();
				List<EmpLoanTO> empLoanTOs=new ArrayList<EmpLoanTO>();
				
				while(iterator.hasNext()){
					EmpLoan eLoan=iterator.next();
					if(eLoan!=null){
						EmpLoanTO eLoanTO=new EmpLoanTO();
						if(eLoan.getId()>0)
						{
							eLoanTO.setId(eLoan.getId());
						}
						
						if(StringUtils.isNotEmpty(eLoan.getLoanAmount())){
							eLoanTO.setLoanAmount(eLoan.getLoanAmount());
						}
						
						if(StringUtils.isNotEmpty(eLoan.getLoanDetails())){
							eLoanTO.setLoanDetails(eLoan.getLoanDetails());
						}
						
						if(eLoan.getLoanDate()!=null){
							eLoanTO.setLoanDate(CommonUtil.ConvertStringToDateFormat(eLoan.getLoanDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empLoanTOs.add(eLoanTO);
					
				
			}
			}
				objform.getEmployeeInfoTONew().setEmpLoan(empLoanTOs);
				}
				else
				{
					List<EmpLoanTO> list=new ArrayList<EmpLoanTO>();
					EmpLoanTO emploanTo=new EmpLoanTO();
					emploanTo.setLoanAmount("");
					emploanTo.setLoanDate("");
					emploanTo.setLoanDetails("");
					objform.setLoanListSize(String.valueOf(list.size()));
					list.add(emploanTo);
					objform.getEmployeeInfoTONew().setEmpLoan(list);
				}
			}
			
			
			if(empApplicantDetails.getEmpRemarks()!=null)
			{
				Set<EmpRemarks> empRemarks=empApplicantDetails.getEmpRemarks();
				if(empRemarks != null && !empRemarks.isEmpty())
				{
				Iterator<EmpRemarks> iterator=empRemarks.iterator();
				List<EmpRemarksTO> empRemarkTOs=new ArrayList<EmpRemarksTO>();
				
				while(iterator.hasNext()){
					EmpRemarks eRemarks=iterator.next();
					if(eRemarks!=null){
						EmpRemarksTO eRemarksTO=new EmpRemarksTO();
						if(eRemarks.getId()>0)
						{
							eRemarksTO.setId(eRemarks.getId());
						}
						if(StringUtils.isNotEmpty(eRemarks.getRemarksDetails())){
							eRemarksTO.setRemarkDetails(eRemarks.getRemarksDetails());
						}
						
						if(StringUtils.isNotEmpty(eRemarks.getRemarksEnteredBy())){
							eRemarksTO.setEnteredBy(eRemarks.getRemarksEnteredBy());
						}
						
						if(eRemarks.getRemarksDate()!=null){
							eRemarksTO.setRemarkDate(CommonUtil.ConvertStringToDateFormat(eRemarks.getRemarksDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empRemarkTOs.add(eRemarksTO);
					
				
			}
			}
				objform.getEmployeeInfoTONew().setEmpRemarks(empRemarkTOs);
				}
				else
				{
				List<EmpRemarksTO> flist=new ArrayList<EmpRemarksTO>();
				EmpRemarksTO empRemarksTO=new EmpRemarksTO();
				empRemarksTO.setEnteredBy("");
				empRemarksTO.setRemarkDate("");
				empRemarksTO.setRemarkDetails("");
				objform.setRemarksListSize(String.valueOf(flist.size()));
				flist.add(empRemarksTO);
				
				
				objform.getEmployeeInfoTONew().setEmpRemarks(flist);
					
					}
				}


		/*	if(empApplicantDetails.getEmpLeaves()!=null)
			{
				Set<EmpLeave> empLeaves=empApplicantDetails.getEmpLeaves();
				if(empLeaves != null && !empLeaves.isEmpty())
				{
				Iterator<EmpLeave> iterator=empLeaves.iterator();
 				List<EmpLeaveAllotTO> empLeaveTOs=new ArrayList<EmpLeaveAllotTO>();
				
				while(iterator.hasNext()){
					EmpLeave eLeave=iterator.next();
					if(eLeave!=null){
						EmpLeaveAllotTO eLeaveTO=new EmpLeaveAllotTO();
						
						if(eLeave.getId()>0)
						{
							eLeaveTO.setEmpLeaveId(eLeave.getId());
						}
						
					if(eLeave.getEmpLeaveType().getId()>0){
						EmpLeaveType leavetype=new EmpLeaveType();
						leavetype.setId(eLeave.getEmpLeaveType().getId());
						eLeaveTO.setEmpLeaveType(leavetype);
					}										
						
						if(StringUtils.isNotEmpty(eLeave.getEmpLeaveType().getName())){
							eLeaveTO.setLeaveType(eLeave.getEmpLeaveType().getName());
						}
						
						if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesAllocated()))){
							eLeaveTO.setAllottedLeave(String.valueOf(eLeave.getLeavesAllocated()));
						}
						if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesSanctioned()))){
							eLeaveTO.setSanctionedLeave(String.valueOf(eLeave.getLeavesSanctioned()));
						}
												
						empLeaveTOs.add(eLeaveTO);
						}
			     }
				objform.getEmployeeInfoTONew().setEmpLeaveToList(empLeaveTOs);
				}
			else
			{
				String empTypeId=null;
				if(empApplicantDetails.getEmptype()!=null && empApplicantDetails.getEmptype().getId()>0)
				{
				empTypeId=String.valueOf(empApplicantDetails.getEmptype().getId());
				List<EmpLeaveAllotTO> empLeaveToList;
				try {
				empLeaveToList = EmployeeViewHandler.getInstance().getEmpLeaveList(empTypeId);
				objform.getEmployeeInfoTONew().setEmpLeaveToList(empLeaveToList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}

		}*/
			//Leaves............................................
			
		/*	if(empApplicantDetails.getEmpLeaves()!=null)
			{
				int month=txn.getInitializationMonth(empApplicantDetails.getId());
				int currentMonth=currentMonth();
				int year=Calendar.getInstance().get(Calendar.YEAR);
				int year1=0;
				Set<EmpLeave> empLeaves=empApplicantDetails.getEmpLeaves();
				List<EmpLeaveAllotTO> extEmpLeaveTos = new ArrayList<EmpLeaveAllotTO>();
				List<EmpLeaveAllotTO> empLeaveTOs=new ArrayList<EmpLeaveAllotTO>();
				if(empLeaves != null && !empLeaves.isEmpty())
				{
				Iterator<EmpLeave> iterator=empLeaves.iterator();
 				
				
				while(iterator.hasNext()){
					EmpLeave eLeave=iterator.next();
					EmpLeaveAllotTO eLeaveTO=new EmpLeaveAllotTO();
					if(eLeave!=null){
						if(month==6 && currentMonth < month && year > eLeave.getYear()){
						      year1=year-1;
					     }
						if(eLeave.getYear()==year1){
						   if(eLeave.getId()>0)
						   {
						 	  eLeaveTO.setEmpLeaveId(eLeave.getId());
						   }
						
					       if(eLeave.getEmpLeaveType().getId()>0){
						      EmpLeaveType leavetype=new EmpLeaveType();
						      leavetype.setId(eLeave.getEmpLeaveType().getId());
						      eLeaveTO.setEmpLeaveType(leavetype);
					       }										
						
						   if(StringUtils.isNotEmpty(eLeave.getEmpLeaveType().getName())){
							   eLeaveTO.setLeaveType(eLeave.getEmpLeaveType().getName());
						   }
						
						   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesAllocated()))){
							   eLeaveTO.setAllottedLeave(String.valueOf(eLeave.getLeavesAllocated()));
						   }
						   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesSanctioned()))){
							   eLeaveTO.setSanctionedLeave(String.valueOf(eLeave.getLeavesSanctioned()));
						   }
						   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesRemaining()))){
							   eLeaveTO.setRemainingLeave(String.valueOf(eLeave.getLeavesRemaining()));
						   }
							if(StringUtils.isNotEmpty(String.valueOf(eLeave.getYear()))){
								eLeaveTO.setYear(eLeave.getYear());
							}
							String monthString = new DateFormatSymbols().getMonths()[month-1];

							if(StringUtils.isNotEmpty(monthString)){
								eLeaveTO.setMonth(monthString);
							}
							empLeaveTOs.add(eLeaveTO);
						}else{
							if(eLeave.getId()>0)
							   {
							 	  eLeaveTO.setEmpLeaveId(eLeave.getId());
							   }
							
						       if(eLeave.getEmpLeaveType().getId()>0){
							      EmpLeaveType leavetype=new EmpLeaveType();
							      leavetype.setId(eLeave.getEmpLeaveType().getId());
							      eLeaveTO.setEmpLeaveType(leavetype);
						       }										
							
							   if(StringUtils.isNotEmpty(eLeave.getEmpLeaveType().getName())){
								   eLeaveTO.setLeaveType(eLeave.getEmpLeaveType().getName());
							   }
							
							   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesAllocated()))){
								   eLeaveTO.setAllottedLeave(String.valueOf(eLeave.getLeavesAllocated()));
							   }
							   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesSanctioned()))){
								   eLeaveTO.setSanctionedLeave(String.valueOf(eLeave.getLeavesSanctioned()));
							   }
							   if(StringUtils.isNotEmpty(String.valueOf(eLeave.getLeavesRemaining()))){
								   eLeaveTO.setRemainingLeave(String.valueOf(eLeave.getLeavesRemaining()));
							   }
								if(StringUtils.isNotEmpty(String.valueOf(eLeave.getYear()))){
									eLeaveTO.setYear(eLeave.getYear());
								}
								String monthString = new DateFormatSymbols().getMonths()[month-1];

								if(StringUtils.isNotEmpty(monthString)){
									eLeaveTO.setMonth(monthString);
								}
								extEmpLeaveTos.add(eLeaveTO);
						}
						
						}
			     }
				objform.getEmployeeInfoTONew().setEmpLeaveToList(empLeaveTOs);
				objform.setEmpLeaveAllotTo(extEmpLeaveTos);
				}
			else
			{
				String empTypeId=null;
				if(objform.getEmptypeId()!=null && !objform.getEmptypeId().isEmpty())
				{
				empTypeId=objform.getEmptypeId();
				List<EmpLeaveAllotTO> empLeaveToList;
				try {
				empLeaveToList = EmployeeViewHandler.getInstance().getEmpLeaveList(empTypeId,objform);
				objform.getEmployeeInfoTONew().setEmpLeaveToList(empLeaveToList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
			
			}*/


			
			
			
			
			if (empApplicantDetails.getEmpLeaves() != null) {
				int month = txn.getInitializationMonth(empApplicantDetails.getEmptype().getId());
				int currentMonth = currentMonth();
				int year = Calendar.getInstance().get(Calendar.YEAR);
				int year1 = 0;
				Set<EmpLeave> empLeaves = empApplicantDetails.getEmpLeaves();
				List<EmpLeaveAllotTO> extEmpLeaveTos = new ArrayList<EmpLeaveAllotTO>();
				if (empLeaves != null && !empLeaves.isEmpty()) {
					Iterator<EmpLeave> iterator = empLeaves.iterator();
					List<EmpLeaveAllotTO> empLeaveTOs = new ArrayList<EmpLeaveAllotTO>();

					while (iterator.hasNext()) {
						EmpLeave eLeave = iterator.next();
						EmpLeaveAllotTO eLeaveTO = new EmpLeaveAllotTO();
						if (eLeave != null) {
							if (month == 6 && currentMonth < month
									&& year > eLeave.getYear()) {
								year1 = year - 1;
							} else {
								year1 = year;
							}
							if (eLeave.getYear() == year1) {
								if (eLeave.getId() > 0) {
									eLeaveTO.setEmpLeaveId(eLeave.getId());
								}

								if (eLeave.getEmpLeaveType().getId() > 0) {
									EmpLeaveType leavetype = new EmpLeaveType();
									leavetype.setId(eLeave.getEmpLeaveType()
											.getId());
									eLeaveTO.setEmpLeaveType(leavetype);
								}

								if (StringUtils.isNotEmpty(eLeave.getEmpLeaveType()
										.getName())) {
									eLeaveTO.setLeaveType(eLeave.getEmpLeaveType()
											.getName());
								}

								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesAllocated()))) {
									eLeaveTO.setAllottedLeave(String.valueOf(eLeave
											.getLeavesAllocated()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesSanctioned()))) {
									eLeaveTO.setSanctionedLeave(String
											.valueOf(eLeave.getLeavesSanctioned()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesRemaining()))) {
									eLeaveTO.setRemainingLeave(String
											.valueOf(eLeave.getLeavesRemaining()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getYear()))) {
									eLeaveTO.setYear(eLeave.getYear());
								}
								String monthString = new DateFormatSymbols()
										.getMonths()[month - 1];

								if (StringUtils.isNotEmpty(monthString)) {
									eLeaveTO.setMonth(monthString);
								}
								empLeaveTOs.add(eLeaveTO);
							} else {
								if (eLeave.getId() > 0) {
									eLeaveTO.setEmpLeaveId(eLeave.getId());
								}

								if (eLeave.getEmpLeaveType().getId() > 0) {
									EmpLeaveType leavetype = new EmpLeaveType();
									leavetype.setId(eLeave.getEmpLeaveType()
											.getId());
									eLeaveTO.setEmpLeaveType(leavetype);
								}

								if (StringUtils.isNotEmpty(eLeave.getEmpLeaveType()
										.getName())) {
									eLeaveTO.setLeaveType(eLeave.getEmpLeaveType()
											.getName());
								}

								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesAllocated()))) {
									eLeaveTO.setAllottedLeave(String.valueOf(eLeave
											.getLeavesAllocated()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesSanctioned()))) {
									eLeaveTO.setSanctionedLeave(String
											.valueOf(eLeave.getLeavesSanctioned()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getLeavesRemaining()))) {
									eLeaveTO.setRemainingLeave(String
											.valueOf(eLeave.getLeavesRemaining()));
								}
								if (StringUtils.isNotEmpty(String.valueOf(eLeave
										.getYear()))) {
									eLeaveTO.setYear(eLeave.getYear());
								}
								String monthString = new DateFormatSymbols()
										.getMonths()[month - 1];

								if (StringUtils.isNotEmpty(monthString)) {
									eLeaveTO.setMonth(monthString);
								}
								extEmpLeaveTos.add(eLeaveTO);
							}

						}
					}
					Collections.sort(empLeaveTOs);
					objform.getEmployeeInfoTONew().setEmpLeaveToList(empLeaveTOs);
					objform.setEmpLeaveAllotTo(extEmpLeaveTos);
				} else {
					String empTypeId = null;
					if (objform.getEmptypeId() != null
							&& !objform.getEmptypeId().isEmpty()) {
						empTypeId = objform.getEmptypeId();
						List<EmpLeaveAllotTO> empLeaveToList;
						try {
							empLeaveToList = EmployeeViewHandler.getInstance().getEmpLeaveList(empTypeId, objform);
							Collections.sort(empLeaveToList);
							objform.getEmployeeInfoTONew().setEmpLeaveToList(
									empLeaveToList);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
			
			
			
			
			
			
			
			
			
			
			
			
			
//Leaves Code Ends.....................................
			if(empApplicantDetails.getEmpImmigrations()!=null)
			{
				Set<EmpImmigration> empImmigration=empApplicantDetails.getEmpImmigrations();
				Iterator<EmpImmigration> iterator=empImmigration.iterator();
				List<EmpImmigrationTO> empImmigrationTOs=new ArrayList<EmpImmigrationTO>();
				
				while(iterator.hasNext()){
					EmpImmigration eImmigration=iterator.next();
					if(eImmigration!=null){
						EmpImmigrationTO eImmigrationTO=new EmpImmigrationTO();
						
						
						if(eImmigration.getId()>0)
						{
							eImmigrationTO.setId(String.valueOf(eImmigration.getId()));
						}
						if(StringUtils.isNotEmpty(eImmigration.getPassportComments())){
							eImmigrationTO.setPassportComments(eImmigration.getPassportComments());
						}
						
						if(StringUtils.isNotEmpty(eImmigration.getPassportNo())){
							eImmigrationTO.setPassportNo(eImmigration.getPassportNo());
						}
						if(StringUtils.isNotEmpty(eImmigration.getPassportReviewStatus())){
							eImmigrationTO.setPassportReviewStatus(eImmigration.getPassportReviewStatus());
						}
						if(StringUtils.isNotEmpty(eImmigration.getPassportStatus())){
							eImmigrationTO.setPassportStatus(eImmigration.getPassportStatus());
						}
						if(eImmigration.getCountryByPassportCountryId()!=null && eImmigration.getCountryByPassportCountryId().getId()>0){
							
							eImmigrationTO.setPassportCountryId(String.valueOf(eImmigration.getCountryByPassportCountryId().getName()));
						}
						if(eImmigration.getPassportDateOfExpiry()!=null){
							eImmigrationTO.setPassportExpiryDate(CommonUtil.ConvertStringToDateFormat(eImmigration.getPassportDateOfExpiry().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						if(eImmigration.getPassportIssueDate()!=null){
							eImmigrationTO.setPassportIssueDate(CommonUtil.ConvertStringToDateFormat(eImmigration.getPassportIssueDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						if(StringUtils.isNotEmpty(eImmigration.getVisaComments())){
							eImmigrationTO.setVisaComments(eImmigration.getVisaComments());
						}
						if(StringUtils.isNotEmpty(eImmigration.getVisaNo())){
							eImmigrationTO.setVisaNo(eImmigration.getVisaNo());
						}
						if(StringUtils.isNotEmpty(eImmigration.getVisaReviewStatus())){
							eImmigrationTO.setVisaReviewStatus(eImmigration.getVisaReviewStatus());
						}
						if(StringUtils.isNotEmpty(eImmigration.getVisaStatus())){
							eImmigrationTO.setVisaStatus(eImmigration.getVisaStatus());
						}
						if(eImmigration.getCountryByVisaCountryId()!=null && eImmigration.getCountryByVisaCountryId().getId()>0){
						
							eImmigrationTO.setVisaCountryId(String.valueOf(eImmigration.getCountryByVisaCountryId().getName()));
						}
						if(eImmigration.getVisaDateOfExpiry()!=null){
							eImmigrationTO.setVisaExpiryDate(CommonUtil.ConvertStringToDateFormat(eImmigration.getVisaDateOfExpiry().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						if(eImmigration.getVisaIssueDate()!=null){
							eImmigrationTO.setVisaIssueDate(CommonUtil.ConvertStringToDateFormat(eImmigration.getVisaIssueDate().toString(), "yyyy-mm-dd", "dd/mm/yyyy"));
						}
						
						empImmigrationTOs.add(eImmigrationTO);
					
				
			}
					objform.getEmployeeInfoTONew().setEmpImmigration(empImmigrationTOs);	
				}
				}
			
			
			
			if(empApplicantDetails.getEmpPayAllowance()!=null)
			{
				Set<EmpPayAllowanceDetails> empPayAllowanceDetails=empApplicantDetails.getEmpPayAllowance();
				if(empPayAllowanceDetails != null && !empPayAllowanceDetails.isEmpty())
				{
				Iterator<EmpPayAllowanceDetails> iterator=empPayAllowanceDetails.iterator();
				//List<EmpPayAllowanceTO> empPayAllowanceTOs=new ArrayList<EmpPayAllowanceTO>();
				List<EmpAllowanceTO> empPayAllowanceTOs=new ArrayList<EmpAllowanceTO>();
				
				List<EmpAllowanceTO> fixed=null;
				if(objform.getEmployeeInfoTONew()!=null){
				if(objform.getEmployeeInfoTONew().getPayscaleFixedTo()!=null){
				fixed=objform.getEmployeeInfoTONew().getPayscaleFixedTo();
				}
				
				//
				while(iterator.hasNext()){
					//EmpPayAllowanceTO ePayAllotTO=new EmpPayAllowanceTO();
					EmpPayAllowanceDetails ePayAlloDet=iterator.next();
					EmpAllowanceTO eAllotTO=new EmpAllowanceTO();
					if(ePayAlloDet!=null){
						
						if(fixed!=null){
							Iterator<EmpAllowanceTO> iterator2=fixed.iterator();
							while(iterator2.hasNext()){
								EmpAllowanceTO empAllTO=iterator2.next();
							if(empAllTO!=null && (empAllTO.getId()>0)){
							if(ePayAlloDet.getEmpAllowance()!= null && ePayAlloDet.getEmpAllowance().getId()>0){
								if(empAllTO.getId()==(ePayAlloDet.getEmpAllowance().getId())){
									eAllotTO.setId(ePayAlloDet.getEmpAllowance().getId());	
									//check the above line......
									if(ePayAlloDet.getId()>0){
										//ePayAllotTO.setId(ePayAlloDet.getId());
										eAllotTO.setEmpPayAllowanceId(ePayAlloDet.getId());
									}
									
									if(StringUtils.isNotEmpty(ePayAlloDet.getAllowanceValue())){
										eAllotTO.setAllowanceName(ePayAlloDet.getAllowanceValue());
										//ePayAllotTO.setAllowanceValue(eAllotTO.getAllowanceName());
									}
									
									if(StringUtils.isNotEmpty(String.valueOf(empAllTO.getName()))){
										eAllotTO.setName(empAllTO.getName());
										
									}
									eAllotTO.setDisplayOrder(empAllTO.getDisplayOrder());
									empPayAllowanceTOs.add(eAllotTO);
									
								}
							}
			     }
							}
						}
					}
				}
				Collections.sort(empPayAllowanceTOs,new PayAllowance());
				objform.getEmployeeInfoTONew().setPayscaleFixedTo(empPayAllowanceTOs);
				}
		
				}
				else
				{
					 try {
						 List<EmpAllowanceTO> payscaleFixedTo=txn.getPayAllowanceFixedMap();
					
					 if(payscaleFixedTo!=null){
						 objform.getEmployeeInfoTONew().setPayscaleFixedTo(payscaleFixedTo);
					 }
					 } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
				}
				}
			
			
			
			
	
			if(empApplicantDetails.getPreviousExpSet()!=null){
				int teachingFlag=0;
				int industryFlag=0;
				Set<EmpPreviousExperience> empOnlinePreviousExperiencesSet=empApplicantDetails.getPreviousExpSet();
				if(empOnlinePreviousExperiencesSet != null && !empOnlinePreviousExperiencesSet.isEmpty()){
					
					Iterator<EmpPreviousExperience> iterator=empOnlinePreviousExperiencesSet.iterator();
					List<EmpPreviousOrgTo> industryExp=new ArrayList<EmpPreviousOrgTo>();
					List<EmpPreviousOrgTo> teachingExp=new ArrayList<EmpPreviousOrgTo>();
					while(iterator.hasNext()){
						EmpPreviousExperience empOnlinePreviousExperiences=iterator.next();
						if(empOnlinePreviousExperiences!=null){
							EmpPreviousOrgTo empOnliPreviousExperienceTO=new EmpPreviousOrgTo();
							
							if(empOnlinePreviousExperiences.isIndustryExperience())
							{
								if(empOnlinePreviousExperiences.getId()>0){
									empOnliPreviousExperienceTO.setId(empOnlinePreviousExperiences.getId());
								}
								if(StringUtils.isNotEmpty(empOnlinePreviousExperiences.getEmpDesignation())){
									empOnliPreviousExperienceTO.setCurrentDesignation(empOnlinePreviousExperiences.getEmpDesignation());
								}
								
								if(StringUtils.isNotEmpty(empOnlinePreviousExperiences.getEmpOrganization())){
									empOnliPreviousExperienceTO.setCurrentOrganisation(empOnlinePreviousExperiences.getEmpOrganization());
								}
								
								if( empOnlinePreviousExperiences.getExpMonths()>0 ){
									empOnliPreviousExperienceTO.setIndustryExpMonths(String.valueOf(empOnlinePreviousExperiences.getExpMonths()));
								}
								
								if(empOnlinePreviousExperiences.getExpYears()>0){
									empOnliPreviousExperienceTO.setIndustryExpYears(String.valueOf(empOnlinePreviousExperiences.getExpYears()));
								}
								/*code added by sudhir */
								if(empOnlinePreviousExperiences.getFromDate()!=null && !empOnlinePreviousExperiences.getFromDate().toString().isEmpty()){
									empOnliPreviousExperienceTO.setIndustryFromDate(CommonUtil.formatDates(empOnlinePreviousExperiences.getFromDate()));
								}
								if(empOnlinePreviousExperiences.getToDate()!=null && !empOnlinePreviousExperiences.getToDate().toString().isEmpty()){
									empOnliPreviousExperienceTO.setIndustryToDate(CommonUtil.formatDates(empOnlinePreviousExperiences.getToDate()));
								}
									/*code added by sudhir */
								industryFlag=1;
								industryExp.add(empOnliPreviousExperienceTO);
							}else if(empOnlinePreviousExperiences.isTeachingExperience())
							{
								if(empOnlinePreviousExperiences.getId()>0){
									empOnliPreviousExperienceTO.setId(empOnlinePreviousExperiences.getId());
								}
								if(StringUtils.isNotEmpty(empOnlinePreviousExperiences.getEmpDesignation())){
									empOnliPreviousExperienceTO.setCurrentTeachnigDesignation(empOnlinePreviousExperiences.getEmpDesignation());
								}
								
								if(StringUtils.isNotEmpty(empOnlinePreviousExperiences.getEmpOrganization())){
									empOnliPreviousExperienceTO.setCurrentTeachingOrganisation(empOnlinePreviousExperiences.getEmpOrganization());
								}
								
								if( empOnlinePreviousExperiences.getExpMonths()>0 ){
									empOnliPreviousExperienceTO.setTeachingExpMonths(String.valueOf(empOnlinePreviousExperiences.getExpMonths()));
								}
								
								if(empOnlinePreviousExperiences.getExpYears()>0){
									empOnliPreviousExperienceTO.setTeachingExpYears(String.valueOf(empOnlinePreviousExperiences.getExpYears()));
								}
								/*code added by sudhir */
								if(empOnlinePreviousExperiences.getFromDate()!=null && !empOnlinePreviousExperiences.getFromDate().toString().isEmpty()){
									empOnliPreviousExperienceTO.setTeachingFromDate(CommonUtil.formatDates(empOnlinePreviousExperiences.getFromDate()));
								}
								if(empOnlinePreviousExperiences.getToDate()!=null && !empOnlinePreviousExperiences.getToDate().toString().isEmpty()){
									empOnliPreviousExperienceTO.setTeachingToDate(CommonUtil.formatDates(empOnlinePreviousExperiences.getToDate()));
								}
									/*code added by sudhir */
								teachingFlag=1;
								teachingExp.add(empOnliPreviousExperienceTO);
																
							}
						}
							objform.getEmployeeInfoTONew().setExperiences(industryExp);
							objform.getEmployeeInfoTONew().setTeachingExperience(teachingExp);
					}
				}else {
					List<EmpPreviousOrgTo> list=new ArrayList<EmpPreviousOrgTo>();
					EmpPreviousOrgTo empPreviousOrgTo=new EmpPreviousOrgTo();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setIndustryExpYears("");
					empPreviousOrgTo.setIndustryExpMonths("");
					empPreviousOrgTo.setCurrentDesignation("");
					empPreviousOrgTo.setCurrentOrganisation("");
					/*code added by sudhir */
					empPreviousOrgTo.setIndustryFromDate("");
					empPreviousOrgTo.setIndustryToDate("");
					/*--------------------- */
					objform.setIndustryExpLength(String.valueOf(list.size()));
					list.add(empPreviousOrgTo);
					
					List<EmpPreviousOrgTo> teachingList=new ArrayList<EmpPreviousOrgTo>();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setTeachingExpYears("");
					empPreviousOrgTo.setTeachingExpMonths("");
					empPreviousOrgTo.setCurrentTeachingOrganisation("");
					empPreviousOrgTo.setCurrentTeachnigDesignation("");
					/*code added by sudhir */
					empPreviousOrgTo.setTeachingFromDate("");
					empPreviousOrgTo.setTeachingToDate("");
					/*--------------------- */
					objform.setTeachingExpLength(String.valueOf(teachingList.size()));
					teachingList.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setExperiences(list);
					objform.getEmployeeInfoTONew().setTeachingExperience(teachingList);
				}
				if(teachingFlag==1 && industryFlag==0)
				{
					List<EmpPreviousOrgTo> list=new ArrayList<EmpPreviousOrgTo>();
					EmpPreviousOrgTo empPreviousOrgTo=new EmpPreviousOrgTo();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setIndustryExpYears("");
					empPreviousOrgTo.setIndustryExpMonths("");
					empPreviousOrgTo.setCurrentDesignation("");
					empPreviousOrgTo.setCurrentOrganisation("");
					/*code added by sudhir */
					empPreviousOrgTo.setIndustryFromDate("");
					empPreviousOrgTo.setIndustryToDate("");
					/*--------------------- */
					objform.setIndustryExpLength(String.valueOf(list.size()));
					list.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setExperiences(list);
				}
				if(teachingFlag==0 && industryFlag==1)
				{
					List<EmpPreviousOrgTo> teachingList=new ArrayList<EmpPreviousOrgTo>();
					EmpPreviousOrgTo empPreviousOrgTo=new EmpPreviousOrgTo();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setTeachingExpYears("");
					empPreviousOrgTo.setTeachingExpMonths("");
					empPreviousOrgTo.setCurrentTeachingOrganisation("");
					empPreviousOrgTo.setCurrentTeachnigDesignation("");
					/*code added by sudhir */
					empPreviousOrgTo.setTeachingFromDate("");
					empPreviousOrgTo.setTeachingToDate("");
					/*--------------------- */
					objform.setTeachingExpLength(String.valueOf(teachingList.size()));
					teachingList.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setTeachingExperience(teachingList);
				}
				if(teachingFlag==1 && industryFlag==0)
				{
					List<EmpPreviousOrgTo> list=new ArrayList<EmpPreviousOrgTo>();
					EmpPreviousOrgTo empPreviousOrgTo=new EmpPreviousOrgTo();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setIndustryExpYears("");
					empPreviousOrgTo.setIndustryExpMonths("");
					empPreviousOrgTo.setCurrentDesignation("");
					empPreviousOrgTo.setCurrentOrganisation("");
					/*code added by sudhir */
					empPreviousOrgTo.setIndustryFromDate("");
					empPreviousOrgTo.setIndustryToDate("");
					/*--------------------- */
					objform.setIndustryExpLength(String.valueOf(list.size()));
					list.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setExperiences(list);
				}
				if(teachingFlag==0 && industryFlag==0)
				{
					List<EmpPreviousOrgTo> teachingList=new ArrayList<EmpPreviousOrgTo>();
					EmpPreviousOrgTo empPreviousOrgTo=new EmpPreviousOrgTo();
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setTeachingExpYears("");
					empPreviousOrgTo.setTeachingExpMonths("");
					empPreviousOrgTo.setCurrentTeachingOrganisation("");
					empPreviousOrgTo.setCurrentTeachnigDesignation("");
					/*code added by sudhir */
					empPreviousOrgTo.setTeachingFromDate("");
					empPreviousOrgTo.setTeachingToDate("");
					/*--------------------- */
					objform.setTeachingExpLength(String.valueOf(teachingList.size()));
					teachingList.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setTeachingExperience(teachingList);
					
					List<EmpPreviousOrgTo> list=new ArrayList<EmpPreviousOrgTo>();
					
					empPreviousOrgTo.setIfEmpty("1");
					empPreviousOrgTo.setIndustryExpYears("");
					empPreviousOrgTo.setIndustryExpMonths("");
					empPreviousOrgTo.setCurrentDesignation("");
					empPreviousOrgTo.setCurrentOrganisation("");
					/*code added by sudhir */
					empPreviousOrgTo.setIndustryFromDate("");
					empPreviousOrgTo.setIndustryToDate("");
					/*--------------------- */
					objform.setIndustryExpLength(String.valueOf(list.size()));
					list.add(empPreviousOrgTo);
					objform.getEmployeeInfoTONew().setExperiences(list);
				}
			}
			/*----------------------------------- code added by sudhir---------------------------------------*/
			/* calculating Experience in cjc (or) cu from joining Date to present day*/
			Date joiningDate = null;
			String fromDateDay = null;
			String fromDateMonth = null;
			if(objform.getDateOfJoining()!=null && !objform.getDateOfJoining().isEmpty()){
				 joiningDate = CommonUtil.ConvertStringToDate(objform.getDateOfJoining());
				 fromDateDay = objform.getDateOfJoining().substring(0, 2);
				 fromDateMonth = objform.getDateOfJoining().substring(3, 5);
			}
			String todaysDate = CommonUtil.getTodayDate();
			String toDateDay = todaysDate.substring(0, 2);
			String toDateMonth = todaysDate.substring(3, 5);
			Date toDate = CommonUtil.ConvertStringToDate(todaysDate);
			if(joiningDate!=null && toDate!=null){
				
			/*int years = CommonUtil.getYearsDiff(joiningDate,toDate);
			int months = CommonUtil.getMonthsBetweenTwoYears(joiningDate, toDate,fromDate,toDateNumber);*/
				double msPerGregorianYear = 365.25 * 86400 * 1000;
			 	double years1 =(toDate.getTime() - joiningDate.getTime()) / msPerGregorianYear;
			 	int yy = (int) years1;
		        int mm = (int) ((years1 - yy) * 12);
		        if(fromDateDay.equals(toDateDay)){
		        	if(fromDateMonth.equals(toDateMonth)){
		        		mm=0;
		        		yy = (int) Math.round(years1);
		        	}
		        }
		        if(fromDateDay.equals(toDateDay)){
		        	if(!fromDateMonth.equals(toDateMonth)){
		        		mm = (int) Math.round(((years1 - yy) * 12));
		        	}
		        }
			objform.setExperienceInYears(yy);
			objform.setExperienceInMonths(mm);
			}
			/* calculating TotalCurrent Experience Years and Months based on Recognised Experience and Experience in cjc (or) cu
			int totalYears = Integer.parseInt(objform.getRelevantExpYears())+objform.getExperienceInYears();
			int totalMonths1 = Integer.parseInt(objform.getRelevantExpMonths()) + objform.getExperienceInMonths() % 12;
			int totalMonths2 = Integer.parseInt(objform.getRelevantExpMonths()) + objform.getExperienceInMonths() / 12;
			totalYears = totalYears + totalMonths2;
			objform.setTotalCurrentExpYears(totalYears);
			objform.setTotalCurrentExpMonths(totalMonths1);*/
			
			/*-----------------------------------------------------------------------------------------------*/
			if(empApplicantDetails.getExtensionNumber()!=null && !empApplicantDetails.getExtensionNumber().isEmpty()){
				objform.setExtensionNumber(empApplicantDetails.getExtensionNumber());
			}
			}	
			
		
		/**
		 * @param employeelist
		 * @param departmentId
		 * @param designationId
		 * @return
		 * @throws Exception
		 */
		public List<EmployeeTO> convertEmployeeTOtoBO(List<Employee> employeelist, int departmentId ,int designationId) throws Exception 
		{
			
			List<Department> departmentList = txn.getEmployeeDepartment();
			List<Designation> designationList=txn.getEmployeeDesignation();
			
			List<EmployeeTO> employeeTos = new ArrayList<EmployeeTO>();
			String name = "";
			if (employeelist != null) {
				Iterator<Employee> stItr = employeelist.iterator();
				while (stItr.hasNext()) {
					name = "";
					Employee emp=(Employee)stItr.next();
					EmployeeTO empTo = new EmployeeTO();
					if (emp.getId()>0) {
						empTo.setId(emp.getId());
					}
					if (emp.getUid() != null) {
						empTo.setDummyUid(emp.getUid());
					}
					if (emp.getCode() != null) {
						
							empTo.setDummyCode(emp.getCode());
					}
					if (emp.getFingerPrintId() != null) {
						
						empTo.setDummyFingerPrintId(emp.getFingerPrintId());
					}
					if (emp.getFirstName() != null) {
						
						empTo.setDummyFirstName(emp.getFirstName());
					}
                    
					if(emp.getDesignation()!=null && emp.getDesignation().getId()>0){
						int DesigId=emp.getDesignation().getId();
						String DesigName= null;
						if (designationList != null) {
							Iterator<Designation> desItr = designationList.iterator();
							while (desItr.hasNext()) {
								Designation des=(Designation)desItr.next();
								int desigId =des.getId();
								if(desigId== DesigId)
								{
								DesigName=des.getName();
								}
							}
						}
						empTo.setDummyDesignationName(DesigName);
					}
						if(emp.getDepartment()!=null && emp.getDepartment().getId()>0){
							int DepId=emp.getDepartment().getId();
							String DepName= null;
							if (departmentList != null) {
								Iterator<Department> depItr = departmentList.iterator();
								while (depItr.hasNext()) {
									Department dep=(Department)depItr.next();
									int depId =dep.getId();
									if(depId== DepId)
									{
										DepName=dep.getName();
									}
								}
							}
							empTo.setDummyDepartmentName(DepName);
						}	
						
						if(emp.getEmpImages()!=null && !emp.getEmpImages().isEmpty())
						{
							Iterator<EmpImages> itr=emp.getEmpImages().iterator();
							while (itr.hasNext()) {
								EmpImages bo =itr.next();
								if(bo.getEmpPhoto()!=null && bo.getEmpPhoto().length >0){
									empTo.setIsPhoto(true);
									break;
								}
							}
							
						}
				employeeTos.add(empTo);
				}
				
			}
			
		
			return employeeTos;
		}
		public int currentMonth(){
			 Date date=new Date();
			 Calendar c=Calendar.getInstance();
			 c.setTime(date);
			 int month=c.get(Calendar.MONTH);
			 return month+1;
		}
		
		
		
		public List<EmployeeTO> convertEmployeeTOtoBO(List<Employee> employeelist) throws Exception 
		{
			List<Designation> designationList=txn.getEmployeeDesignation();
			List<EmployeeTO> employeeTos = new ArrayList<EmployeeTO>();
			String name = "";
			if (employeelist != null) {
				Iterator<Employee> stItr = employeelist.iterator();
				while (stItr.hasNext()) {
					name = "";
					Employee emp=(Employee)stItr.next();
					EmployeeTO empTo = new EmployeeTO();
					if (emp.getId()>0) {
						empTo.setId(emp.getId());
					}
					if (emp.getUid() != null) {
						empTo.setDummyUid(emp.getUid());
					}
					if (emp.getCode() != null) {
						
							empTo.setDummyCode(emp.getCode());
					}
					if (emp.getFingerPrintId() != null) {
						
						empTo.setDummyFingerPrintId(emp.getFingerPrintId());
					}
					if (emp.getFirstName() != null) {
						
						empTo.setDummyFirstName(emp.getFirstName());
					}
					if(emp.getDepartment().getName()!=null && !emp.getDepartment().getName().isEmpty()){
						empTo.setDeptName(emp.getDepartment().getName());
					}
					if(emp.getDesignation()!=null && emp.getDesignation().getId()>0){
						int DesigId=emp.getDesignation().getId();
						String DesigName= null;
						if (designationList != null) {
							Iterator<Designation> desItr = designationList.iterator();
							while (desItr.hasNext()) {
								Designation des=(Designation)desItr.next();
								int desigId =des.getId();
								if(desigId== DesigId)
								{
								DesigName=des.getName();
								}
							}
						}
						empTo.setDummyDesignationName(DesigName);
					}
					if(emp.getEmpImages()!=null && !emp.getEmpImages().isEmpty())
						{
							Iterator<EmpImages> itr=emp.getEmpImages().iterator();
							while (itr.hasNext()) {
								EmpImages bo =itr.next();
								if(bo.getEmpPhoto()!=null && bo.getEmpPhoto().length >0){
									empTo.setIsPhoto(true);
									break;
								}
							}
							
						}
				employeeTos.add(empTo);
				}
			}
			return employeeTos;
		}
		
	}



