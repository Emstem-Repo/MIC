package com.kp.cms.handlers.admin;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.PublishSpecialFees;
import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.admin.PublishSpecialFeesForm;
import com.kp.cms.helpers.admin.PublishSpecialFeesHelper;
import com.kp.cms.to.admin.PublishSpecialFeesTO;
import com.kp.cms.transactions.admin.IPublishSpecialFeesTransaction;
import com.kp.cms.transactionsimpl.admin.PublishSpecialFeesTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class PublishSpecialFeesHandler {
	IPublishSpecialFeesTransaction transaction = new PublishSpecialFeesTransactionImpl();
	PublishSpecialFeesHelper helper = new PublishSpecialFeesHelper();
	
	private static volatile PublishSpecialFeesHandler publishSpecialFeesHandler = null;
    public PublishSpecialFeesHandler(){
    	
    }
    
    public static PublishSpecialFeesHandler getInstance() {
		if (publishSpecialFeesHandler == null) {
			publishSpecialFeesHandler = new PublishSpecialFeesHandler();
		}
		return publishSpecialFeesHandler;
	}

	public List<PublishSpecialFeesTO> getPublishList(PublishSpecialFeesForm publishSpecialFeesForm) throws Exception {
		List<PublishSpecialFees>publishSpecialFees = transaction.getList(publishSpecialFeesForm);
		List<PublishSpecialFeesTO> specialFeesTOs = helper.convertBOToTO(publishSpecialFees);
		return specialFeesTOs;
	}

	public void addList(String[] stayClass, String toDate, String fromDate,String userId, PublishSpecialFeesForm objform) throws Exception {
		
int classID = 0;
		
		if (stayClass != null && stayClass.length > 0){
			
			
			for (String i : stayClass) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {
					
					classID = Integer.parseInt(tokens.nextElement().toString());
		
		
		StringBuffer classNames=new StringBuffer();
		String className=transaction.isDuplicate(0,Integer.parseInt(objform.getYear()), classID);
		
		if(!className.isEmpty()){
			if(classNames.length()>0){
			classNames.append(","+className);
			} else classNames.append(className);
		}
		
		if(classNames.length()>0){
			objform.setClassNames(classNames.toString());
			throw new DuplicateException(classNames.toString());
		}
		
				}
			}
		}
		
		
		
		
		if (stayClass != null && stayClass.length > 0){
			
			
			for (String i : stayClass) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {
					
					classID = Integer.parseInt(tokens.nextElement().toString());
					PublishSpecialFees publishSpecialFees = new PublishSpecialFees();;
					
					Classes classes=new Classes();
					classes.setId(classID);
					publishSpecialFees.setClasses(classes);
					publishSpecialFees.setFromDate(CommonUtil.ConvertStringToSQLDate(fromDate));
					publishSpecialFees.setToDate(CommonUtil.ConvertStringToSQLDate(toDate));
					publishSpecialFees.setIsActive(true);
					publishSpecialFees.setAcademicYear(objform.getYear());
					publishSpecialFees.setCreatedBy(userId);
					publishSpecialFees.setModifiedBy(userId);
					publishSpecialFees.setCreatedDate(new Date());
					publishSpecialFees.setLastModifiedDate(new Date());
				
					transaction.insert(publishSpecialFees);
					
		
				
				}
			}
			
			
			
		}
		
		
	}

	public void delete(int id) throws Exception {
		
		transaction.delete(id);
	}

	public void update(int id, String[] classId, String toDate,
			String fromDate, String userId, PublishSpecialFeesForm objform) throws Exception{
		int classID = 0;

		if (classId != null && classId.length > 0){


			for (String i : classId) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {

					classID = Integer.parseInt(tokens.nextElement().toString());


					StringBuffer classNames=new StringBuffer();
					String className=transaction.isDuplicateForUpdate(id,Integer.parseInt(objform.getYear()), classID);

					if(!className.isEmpty()){
						if(classNames.length()>0){
							classNames.append(","+className);
						} else classNames.append(className);
					}

					if(classNames.length()>0){
						objform.setClassNames(classNames.toString());
						throw new DuplicateException(classNames.toString());
					}

				}
			}
		}

		if (classId != null && classId.length > 0){


			for (String i : classId) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {

					classID = Integer.parseInt(tokens.nextElement().toString());
					PublishSpecialFees publishStudentSemesterFees = new PublishSpecialFees();

					Classes classes=new Classes();
					classes.setId(classID);
					publishStudentSemesterFees.setClasses(classes);
					publishStudentSemesterFees.setFromDate(CommonUtil.ConvertStringToSQLDate(fromDate));
					publishStudentSemesterFees.setToDate(CommonUtil.ConvertStringToSQLDate(toDate));
					publishStudentSemesterFees.setIsActive(true);
					publishStudentSemesterFees.setAcademicYear(objform.getYear());
					publishStudentSemesterFees.setCreatedBy(userId);
					publishStudentSemesterFees.setModifiedBy(userId);
					publishStudentSemesterFees.setCreatedDate(new Date());
					publishStudentSemesterFees.setLastModifiedDate(new Date());
					publishStudentSemesterFees.setId(Integer.parseInt(objform.getId()));
					transaction.insertForUpdate(publishStudentSemesterFees);
							

				}
			}



		}

		
	}

	public void editPublishSpecialFees(PublishSpecialFeesForm publishSpecialFeesForm)  throws Exception{
		if((publishSpecialFeesForm.getId())!= null && !(publishSpecialFeesForm.getId().equalsIgnoreCase("") )){
			PublishSpecialFees publishSpecialFees = transaction.getPublishSpecialFees(publishSpecialFeesForm.getId());
			helper.setPublishSpecialFees(publishSpecialFeesForm, publishSpecialFees);
		}
		
	}
}
