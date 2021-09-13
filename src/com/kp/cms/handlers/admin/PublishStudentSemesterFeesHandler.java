package com.kp.cms.handlers.admin;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.PublishOptionalCourseSubjectApplication;
import com.kp.cms.bo.admin.PublishStudentSemesterFees;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.admin.PublishStudentSemesterFeesForm;
import com.kp.cms.helpers.admin.PublishOptionalCourseSubjectApplicationHelper;
import com.kp.cms.helpers.admin.PublishStudentSemesterFeesHelper;
import com.kp.cms.to.admin.PublishStudentSemesterFeesTO;
import com.kp.cms.transactions.admin.IPublishOptionalCourseSubjectApplication;
import com.kp.cms.transactions.admin.IPublishStudentSemesterFeesTransaction;
import com.kp.cms.transactionsimpl.admin.PublishOptionalCourseSubjectApplicationTransactionImpl;
import com.kp.cms.transactionsimpl.admin.PublishStudentSemesterFeesTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class PublishStudentSemesterFeesHandler {
	private static final String String = null;
	IPublishStudentSemesterFeesTransaction impl = new PublishStudentSemesterFeesTransactionImpl();
	PublishStudentSemesterFeesHelper helper = new PublishStudentSemesterFeesHelper();
	
	private static volatile PublishStudentSemesterFeesHandler publishStudentSemesterFeesHandler = null;
    public PublishStudentSemesterFeesHandler(){
    	
    }
    
    public static PublishStudentSemesterFeesHandler getInstance() {
		if (publishStudentSemesterFeesHandler == null) {
			publishStudentSemesterFeesHandler = new PublishStudentSemesterFeesHandler();
		}
		return publishStudentSemesterFeesHandler;
	}

	public List<PublishStudentSemesterFeesTO> getPublishList(PublishStudentSemesterFeesForm studentSemesterFeesForm) throws Exception {
		List<PublishStudentSemesterFees> studentSemesterPublishList = impl.getList(studentSemesterFeesForm);
		List<PublishStudentSemesterFeesTO> publishTOList = PublishStudentSemesterFeesHelper.getInstance().convertBOToTO(studentSemesterPublishList);
		return publishTOList;
	}

	public void addList(String[] stayClass, String toDate, String fromDate,String userId, PublishStudentSemesterFeesForm objform) throws Exception {

		
		int classID = 0;
		
		if (stayClass != null && stayClass.length > 0){
			
			
			for (String i : stayClass) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {
					
					classID = Integer.parseInt(tokens.nextElement().toString());
		
		
		StringBuffer classNames=new StringBuffer();
		String className=impl.isDuplicate(0,Integer.parseInt(objform.getYear()), classID);
		
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
					PublishStudentSemesterFees publishStudentSemesterFees = new PublishStudentSemesterFees();;
					
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
				
					impl.insert(publishStudentSemesterFees);
					
		
				
				}
			}
			
			
			
		}
		
		
		
	
		
	}

	public void delete(int id) throws Exception {
		impl.delete(id);
		
	}

	public void update(int id, String[] classId, String toDate,String fromDate, String userId,PublishStudentSemesterFeesForm objform) throws Exception {


		int classID = 0;

		if (classId != null && classId.length > 0){


			for (String i : classId) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {

					classID = Integer.parseInt(tokens.nextElement().toString());


					StringBuffer classNames=new StringBuffer();
					String className=impl.isDuplicateForUpdate(id,Integer.parseInt(objform.getYear()), classID);

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
					PublishStudentSemesterFees publishStudentSemesterFees = new PublishStudentSemesterFees();

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
					impl.insertForUpdate(publishStudentSemesterFees);
							

				}
			}



		}


	
	}

	public void editPublishOptionalCourseSubjectApplication(PublishStudentSemesterFeesForm publishStudentSemesterFeesForm) throws Exception {
		if((publishStudentSemesterFeesForm.getId())!= null && !(publishStudentSemesterFeesForm.getId().equalsIgnoreCase("") )){
			PublishStudentSemesterFees publishStudentSemesterFees = impl.getPublishOptionalCourseSubjectApplication(publishStudentSemesterFeesForm.getId());
			helper.setPublishOptionalCourseSubjectApplication(publishStudentSemesterFeesForm, publishStudentSemesterFees);
		}
	}
}
