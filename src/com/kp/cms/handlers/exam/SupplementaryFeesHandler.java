package com.kp.cms.handlers.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Program;
import com.kp.cms.bo.exam.PublishSupplementaryImpApplication;
import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.SupplementaryFees;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.exam.SupplementaryFeesForm;
import com.kp.cms.helpers.exam.SupplementaryFeesHelper;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SupplementaryFeesTo;
import com.kp.cms.transactions.admin.ISingleFieldMasterTransaction;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.admin.SingleFieldMasterTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
import com.kp.cms.utilities.PropertyUtil;

public class SupplementaryFeesHandler {
	/**
	 * Singleton object of SupplementaryFeesHandler
	 */
	private static volatile SupplementaryFeesHandler supplementaryFeesHandler = null;
	private static final Log log = LogFactory.getLog(SupplementaryFeesHandler.class);
	private SupplementaryFeesHandler() {
		
	}
	/**
	 * return singleton object of SupplementaryFeesHandler.
	 * @return
	 */
	public static SupplementaryFeesHandler getInstance() {
		if (supplementaryFeesHandler == null) {
			supplementaryFeesHandler = new SupplementaryFeesHandler();
		}
		return supplementaryFeesHandler;
	}
	/**
	 * @param supplementaryFeesForm
	 * @return
	 */
	public boolean addOrUpdate(SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		log.info("Entered into addOrUpdate");
		String query="from SupplementaryFees s where s.isActive=1 and s.academicYear="+supplementaryFeesForm.getAcademicYear()+" and s.course.id in(";
		StringBuilder intType =new StringBuilder();
		String[] tempArray=supplementaryFeesForm.getSelectedCourse();
		for(int i=0;i<tempArray.length;i++){
			intType.append(tempArray[i]);
			 if(i<(tempArray.length-1)){
				 intType.append(",");
			 }
		}
		query=query+intType+")";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<SupplementaryFees> fees=transaction.getDataForQuery(query);
		if(fees!=null && !fees.isEmpty()){
			String programNames="";
			for (SupplementaryFees bo: fees) {
				if(!programNames.isEmpty())
					programNames=programNames+","+bo.getCourse().getName();
				else
					programNames=programNames+","+bo.getCourse().getName();
			}
			throw new DuplicateException(programNames);
		}
		List<SupplementaryFees> boList=new ArrayList<SupplementaryFees>();
		for (int i = 0; i < tempArray.length; i++) {
			SupplementaryFees bo=new SupplementaryFees();
			Course course=new Course();
			course.setId(Integer.parseInt(tempArray[i]));
			bo.setCourse(course);
			bo.setIsActive(true);
			bo.setCreatedBy(supplementaryFeesForm.getUserId());
			bo.setModifiedBy(supplementaryFeesForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setTheoryFees(new BigDecimal(supplementaryFeesForm.getTheoryFees()));
			bo.setPracticalFees(new BigDecimal(supplementaryFeesForm.getPracticalFees()));
			if(supplementaryFeesForm.getApplicationFees()!=null && !supplementaryFeesForm.getApplicationFees().equalsIgnoreCase(""))
			bo.setApplicationFees(new BigDecimal(supplementaryFeesForm.getApplicationFees()));
			if(supplementaryFeesForm.getCvCampFees()!=null&& !supplementaryFeesForm.getCvCampFees().equalsIgnoreCase(""))
			bo.setCvCampFees(new BigDecimal(supplementaryFeesForm.getCvCampFees()));
			if(supplementaryFeesForm.getMarksListFees()!=null && !supplementaryFeesForm.getMarksListFees().equalsIgnoreCase(""))
			bo.setMarksListFees(new BigDecimal(supplementaryFeesForm.getMarksListFees()));
			if(supplementaryFeesForm.getOnlineServiceChargeFees()!=null && !supplementaryFeesForm.getOnlineServiceChargeFees().equalsIgnoreCase(""))
			bo.setOnlineServiceChargeFees(new BigDecimal(supplementaryFeesForm.getOnlineServiceChargeFees()));
			bo.setAcademicYear(supplementaryFeesForm.getAcademicYear());
			bo.setEgrandFees(new BigDecimal(supplementaryFeesForm.getEgrandFees()));
			boList.add(bo);
		}
		log.info("Exit from addOrUpdate");
		return PropertyUtil.getInstance().saveList(boList);
	}
	/**
	 * @param id
	 * @param mode
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrReactivate(int id, String mode, String userId) throws Exception{
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		SupplementaryFees bo=(SupplementaryFees) transaction.getMasterEntryDataById(SupplementaryFees.class, id);
//		if(mode.equalsIgnoreCase("delete"))
//			bo.setIsActive(false);
//		else
//			bo.setIsActive(true);
//		bo.setModifiedBy(userId);
//		bo.setLastModifiedDate(new Date());
		return PropertyUtil.getInstance().delete(bo);
	}
	/**
	 * @return
	 * @throws Exception
	 */
	public List<SupplementaryFeesTo> getActiveList() throws Exception {
		String query="from SupplementaryFees p where p.isActive=1";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<SupplementaryFees> boList=transaction.getDataForQuery(query);
		return SupplementaryFeesHelper.getInstance().convertBotoToList(boList);
	}
	
	public List<RegularExamFeesTo> getActiveListRegular() throws Exception {
		String query="from RegularExamFees r where r.isActive=1";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<RegularExamFees> boList=transaction.getDataForQuery(query);
		return SupplementaryFeesHelper.getInstance().convertBotoToListRegular(boList);
	}

	public boolean addOrUpdateRegularFee(SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		log.info("Entered into addOrUpdate");
		String query="from RegularExamFees r where r.isActive=1 and r.academicYear="+supplementaryFeesForm.getAcademicYear()+" and r.classes.id in(";
		StringBuilder intType =new StringBuilder();
		String[] tempArray=supplementaryFeesForm.getSelectedClasses();
		for(int i=0;i<tempArray.length;i++){
			intType.append(tempArray[i]);
			 if(i<(tempArray.length-1)){
				 intType.append(",");
			 }
		}
		query=query+intType+")";
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<RegularExamFees> fees=transaction.getDataForQuery(query);
		if(fees!=null && !fees.isEmpty()){
			String classNames="";
			for (RegularExamFees bo: fees) {
				if(!classNames.isEmpty())
					classNames=classNames+","+bo.getClasses().getName();
				else
					classNames=bo.getClasses().getName();
			}
			throw new DuplicateException(classNames);
		}
		List<RegularExamFees> boList=new ArrayList<RegularExamFees>();
		for (int i = 0; i < tempArray.length; i++) {
			RegularExamFees bo=new RegularExamFees();
			Classes classes=new Classes();
			classes.setId(Integer.parseInt(tempArray[i]));
			bo.setClasses(classes);
			bo.setIsActive(true);
			bo.setCreatedBy(supplementaryFeesForm.getUserId());
			bo.setModifiedBy(supplementaryFeesForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setTheoryFees(new BigDecimal(supplementaryFeesForm.getTheoryFees()));
			if(!supplementaryFeesForm.getPracticalFees().isEmpty())
				bo.setPracticalFees(new BigDecimal(supplementaryFeesForm.getPracticalFees()));
			if(!supplementaryFeesForm.getApplicationFees().isEmpty())
				bo.setApplicationFees(new BigDecimal(supplementaryFeesForm.getApplicationFees()));
			if(!supplementaryFeesForm.getCvCampFees().isEmpty())
				bo.setCvCampFees(new BigDecimal(supplementaryFeesForm.getCvCampFees()));
			if(!supplementaryFeesForm.getMarksListFees().isEmpty())
				bo.setMarksListFees(new BigDecimal(supplementaryFeesForm.getMarksListFees()));
			if(!supplementaryFeesForm.getOnlineServiceChargeFees().isEmpty())
				bo.setOnlineServiceChargeFees(new BigDecimal(supplementaryFeesForm.getOnlineServiceChargeFees()));
			bo.setAcademicYear(supplementaryFeesForm.getAcademicYear());
			bo.setEgrandFees(new BigDecimal(supplementaryFeesForm.getEgrandFees()));
			boList.add(bo);
		}
		log.info("Exit from addOrUpdate");
		return PropertyUtil.getInstance().saveList(boList);
	}
	
	public boolean deleteOrReactivateRegular(int id, String mode, String userId) throws Exception{
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		RegularExamFees bo=(RegularExamFees) transaction.getMasterEntryDataById(RegularExamFees.class, id);

		return PropertyUtil.getInstance().delete(bo);
	}
	public void setEditData(int id,SupplementaryFeesForm form) throws Exception {
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		SupplementaryFees bo=(SupplementaryFees) transaction.getMasterEntryDataById(SupplementaryFees.class, id);
		form.setAcademicYear(bo.getAcademicYear());
		String[] ab={String.valueOf(bo.getCourse().getId())};
		form.setProgramTypeId(String.valueOf(bo.getCourse().getProgram().getId()));
		form.setSelectedCourse(ab);
		form.setTheoryFees(String.valueOf(bo.getTheoryFees().doubleValue()));
		form.setPracticalFees(String.valueOf(bo.getPracticalFees().doubleValue()));
		form.setApplicationFees(String.valueOf(bo.getApplicationFees().doubleValue()));
		form.setCvCampFees(String.valueOf(bo.getCvCampFees().doubleValue()));
		form.setMarksListFees(String.valueOf(bo.getMarksListFees().doubleValue()));
		form.setOnlineServiceChargeFees(String.valueOf(bo.getOnlineServiceChargeFees()));
		form.setEgrandFees(String.valueOf(bo.getEgrandFees().doubleValue()));
		form.setId(bo.getId());
		
		// TODO Auto-generated method stub
		
	}
	public boolean UpdateFees(SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		log.info("Entered into addOrUpdate");
		
		String[] tempArray=supplementaryFeesForm.getSelectedCourse();
		String  courseId=tempArray[0];
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		INewExamMarksEntryTransaction transactionbo=NewExamMarksEntryTransactionImpl.getInstance();
		/*String query="from SupplementaryFees s where s.isActive=1 and s.academicYear="+supplementaryFeesForm.getAcademicYear()+" and s.course.id="+courseId;
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<SupplementaryFees> fees=transaction.getDataForQuery(query);
		if(fees!=null && !fees.isEmpty()){
			String programNames="";
			for (SupplementaryFees bo: fees) {
				if(!programNames.isEmpty())
					programNames=programNames+","+bo.getCourse().getName();
				else
					programNames=programNames+","+bo.getCourse().getName();
			}
			throw new DuplicateException(programNames);
		}*/
		SupplementaryFees bo=(SupplementaryFees) transaction.getMasterEntryDataById(SupplementaryFees.class, supplementaryFeesForm.getId());
		
			Course course=new Course();
			course.setId(Integer.parseInt(courseId));
			bo.setCourse(course);
			bo.setIsActive(true);
			bo.setId(supplementaryFeesForm.getId());
			bo.setCreatedBy(supplementaryFeesForm.getUserId());
			bo.setModifiedBy(supplementaryFeesForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setLastModifiedDate(new Date());
			bo.setTheoryFees(new BigDecimal(supplementaryFeesForm.getTheoryFees()));
			bo.setPracticalFees(new BigDecimal(supplementaryFeesForm.getPracticalFees()));
			if(supplementaryFeesForm.getApplicationFees()!=null && !supplementaryFeesForm.getApplicationFees().equalsIgnoreCase(""))
			bo.setApplicationFees(new BigDecimal(supplementaryFeesForm.getApplicationFees()));
			if(supplementaryFeesForm.getCvCampFees()!=null&& !supplementaryFeesForm.getCvCampFees().equalsIgnoreCase(""))
			bo.setCvCampFees(new BigDecimal(supplementaryFeesForm.getCvCampFees()));
			if(supplementaryFeesForm.getMarksListFees()!=null && !supplementaryFeesForm.getMarksListFees().equalsIgnoreCase(""))
			bo.setMarksListFees(new BigDecimal(supplementaryFeesForm.getMarksListFees()));
			if(supplementaryFeesForm.getOnlineServiceChargeFees()!=null && !supplementaryFeesForm.getOnlineServiceChargeFees().equalsIgnoreCase(""))
			bo.setOnlineServiceChargeFees(new BigDecimal(supplementaryFeesForm.getOnlineServiceChargeFees()));
			bo.setAcademicYear(supplementaryFeesForm.getAcademicYear());
			bo.setEgrandFees(new BigDecimal(supplementaryFeesForm.getEgrandFees()));
			
		
		log.info("Exit from addOrUpdate");
		return transactionbo.saveBo(bo);
	}
	public boolean UpdateRegularFee(SupplementaryFeesForm supplementaryFeesForm) throws Exception {
		log.info("Entered into addOrUpdate");
		
		String[] tempArray=supplementaryFeesForm.getSelectedClasses();
		String  classId=tempArray[0];
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		INewExamMarksEntryTransaction transactionbo=NewExamMarksEntryTransactionImpl.getInstance();
		/*String query="from SupplementaryFees s where s.isActive=1 and s.academicYear="+supplementaryFeesForm.getAcademicYear()+" and s.course.id="+courseId;
		INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
		List<SupplementaryFees> fees=transaction.getDataForQuery(query);
		if(fees!=null && !fees.isEmpty()){
			String programNames="";
			for (SupplementaryFees bo: fees) {
				if(!programNames.isEmpty())
					programNames=programNames+","+bo.getCourse().getName();
				else
					programNames=programNames+","+bo.getCourse().getName();
			}
			throw new DuplicateException(programNames);
		}*/
		RegularExamFees bo=(RegularExamFees) transaction.getMasterEntryDataById(RegularExamFees.class, supplementaryFeesForm.getId());
		
		Classes classes=new Classes();
		classes.setId(Integer.parseInt(classId));
		bo.setClasses(classes);
		bo.setIsActive(true);
		bo.setCreatedBy(supplementaryFeesForm.getUserId());
		bo.setModifiedBy(supplementaryFeesForm.getUserId());
		bo.setCreatedDate(new Date());
		bo.setLastModifiedDate(new Date());
		bo.setTheoryFees(new BigDecimal(supplementaryFeesForm.getTheoryFees()));
		if(!supplementaryFeesForm.getPracticalFees().isEmpty())
			bo.setPracticalFees(new BigDecimal(supplementaryFeesForm.getPracticalFees()));
		if(!supplementaryFeesForm.getApplicationFees().isEmpty())
			bo.setApplicationFees(new BigDecimal(supplementaryFeesForm.getApplicationFees()));
		if(!supplementaryFeesForm.getCvCampFees().isEmpty())
			bo.setCvCampFees(new BigDecimal(supplementaryFeesForm.getCvCampFees()));
		if(!supplementaryFeesForm.getMarksListFees().isEmpty())
			bo.setMarksListFees(new BigDecimal(supplementaryFeesForm.getMarksListFees()));
		if(!supplementaryFeesForm.getOnlineServiceChargeFees().isEmpty())
			bo.setOnlineServiceChargeFees(new BigDecimal(supplementaryFeesForm.getOnlineServiceChargeFees()));
		bo.setAcademicYear(supplementaryFeesForm.getAcademicYear());
		bo.setEgrandFees(new BigDecimal(supplementaryFeesForm.getEgrandFees()));
			
		
		log.info("Exit from addOrUpdate");
		return transactionbo.saveRegularBo(bo);
	}
	public void setEditForRegularData(int id, SupplementaryFeesForm form) throws Exception {
		ISingleFieldMasterTransaction transaction=SingleFieldMasterTransactionImpl.getInstance();
		RegularExamFees bo=(RegularExamFees) transaction.getMasterEntryDataById(RegularExamFees.class, id);
		form.setAcademicYear(bo.getAcademicYear());
		String[] ab={String.valueOf(bo.getClasses().getId())};
		form.setSelectedClassArray(ab[0]);
		form.setSelectedClasses(ab);
		form.setProgramTypeId(String.valueOf(bo.getClasses().getCourse().getProgram().getId()));
		form.setTheoryFees(String.valueOf(bo.getTheoryFees().doubleValue()));
		form.setPracticalFees(String.valueOf(bo.getPracticalFees().doubleValue()));
		form.setApplicationFees(String.valueOf(bo.getApplicationFees().doubleValue()));
		form.setCvCampFees(String.valueOf(bo.getCvCampFees().doubleValue()));
		form.setMarksListFees(String.valueOf(bo.getMarksListFees().doubleValue()));
		form.setOnlineServiceChargeFees(String.valueOf(bo.getOnlineServiceChargeFees()));
		form.setEgrandFees(String.valueOf(bo.getEgrandFees().doubleValue()));
		form.setId(bo.getId());
		
		// TODO Auto-generated method stub
		
	}

}
