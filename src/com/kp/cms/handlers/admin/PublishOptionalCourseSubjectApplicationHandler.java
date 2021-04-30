package com.kp.cms.handlers.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.PublishOptionalCourseSubjectApplication;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.forms.admin.PublishOptionalCourseSubjectApplicationForm;
import com.kp.cms.helpers.admin.PublishOptionalCourseSubjectApplicationHelper;
import com.kp.cms.to.admin.PublishOptionalCourseSubjectApplicationTO;
import com.kp.cms.transactions.admin.IPublishOptionalCourseSubjectApplication;
import com.kp.cms.transactionsimpl.admin.PublishOptionalCourseSubjectApplicationTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class PublishOptionalCourseSubjectApplicationHandler 
{
	private static final String String = null;
	IPublishOptionalCourseSubjectApplication impl = new PublishOptionalCourseSubjectApplicationTransactionImpl();
	PublishOptionalCourseSubjectApplicationHelper helper = new PublishOptionalCourseSubjectApplicationHelper();

	private static volatile PublishOptionalCourseSubjectApplicationHandler publishOptionalCourseSubjectApplicationHandler = null;
    public PublishOptionalCourseSubjectApplicationHandler(){
    	
    }
	public static PublishOptionalCourseSubjectApplicationHandler getInstance() {
		if (publishOptionalCourseSubjectApplicationHandler == null) {
			publishOptionalCourseSubjectApplicationHandler = new PublishOptionalCourseSubjectApplicationHandler();
		}
		return publishOptionalCourseSubjectApplicationHandler;
	}

	public List<PublishOptionalCourseSubjectApplicationTO> getPublishOptionalCourseSubjectApplication(PublishOptionalCourseSubjectApplicationForm publishOptionalCourseSubjectApplicationHandlerForm) throws Exception{
		IPublishOptionalCourseSubjectApplication transaction = new PublishOptionalCourseSubjectApplicationTransactionImpl();
		List<PublishOptionalCourseSubjectApplication> publishOptionalCourseSubjectApplicationList = transaction.getPublishOptionalCourseSubjects();
		List<PublishOptionalCourseSubjectApplicationTO> attendanceToList = PublishOptionalCourseSubjectApplicationHelper.convertBOsToTos(publishOptionalCourseSubjectApplicationList);
		
		return attendanceToList;
	}
	
	
	
	
	
	// On - PUBLISH
	public void addList( String[] classId,String toDate, String fromDate, String userId,PublishOptionalCourseSubjectApplicationForm objform)
			throws Exception {
		
		int classID = 0;
		
		if (classId != null && classId.length > 0){
			
			
			for (String i : classId) {
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
		
		
		
		
		if (classId != null && classId.length > 0){
			
			
			for (String i : classId) {
				StringTokenizer tokens = new StringTokenizer(i, ",");
				while (tokens.hasMoreElements()) {
					
					classID = Integer.parseInt(tokens.nextElement().toString());
					PublishOptionalCourseSubjectApplication publishOptionalCourseSubject = new PublishOptionalCourseSubjectApplication();;
					
					Classes classes=new Classes();
					classes.setId(classID);
					publishOptionalCourseSubject.setClasses(classes);
					publishOptionalCourseSubject.setFromDate(CommonUtil.ConvertStringToSQLDate(fromDate));
					publishOptionalCourseSubject.setToDate(CommonUtil.ConvertStringToSQLDate(toDate));
					publishOptionalCourseSubject.setIsActive(true);
					publishOptionalCourseSubject.setAcademicYear(objform.getYear());
					publishOptionalCourseSubject.setCreatedBy(userId);
					publishOptionalCourseSubject.setModifiedBy(userId);
					publishOptionalCourseSubject.setCreatedDate(new Date());
					publishOptionalCourseSubject.setLastModifiedDate(new Date());
				
					impl.insert(publishOptionalCourseSubject);
					
		
				
				}
			}
			
			
			
		}
		
		
		
	}
	
	
	// On - DELETE
	public void delete(int id) throws Exception {
		impl.delete(id);
	}
	
	
	// On - EDIT
	public void update(int id, String[] classId,
			String toDate, String fromDate, String userId,PublishOptionalCourseSubjectApplicationForm objform)
	throws Exception {

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
					PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication = new PublishOptionalCourseSubjectApplication();;

					Classes classes=new Classes();
					classes.setId(classID);
					publishOptionalCourseSubjectApplication.setClasses(classes);
					publishOptionalCourseSubjectApplication.setFromDate(CommonUtil.ConvertStringToSQLDate(fromDate));
					publishOptionalCourseSubjectApplication.setToDate(CommonUtil.ConvertStringToSQLDate(toDate));
					publishOptionalCourseSubjectApplication.setIsActive(true);
					publishOptionalCourseSubjectApplication.setAcademicYear(objform.getYear());
					publishOptionalCourseSubjectApplication.setCreatedBy(userId);
					publishOptionalCourseSubjectApplication.setModifiedBy(userId);
					publishOptionalCourseSubjectApplication.setCreatedDate(new Date());
					publishOptionalCourseSubjectApplication.setLastModifiedDate(new Date());
					publishOptionalCourseSubjectApplication.setId(Integer.parseInt(objform.getId()));
					impl.insertForUpdate(publishOptionalCourseSubjectApplication);
							

				}
			}



		}


	}
	
	
	public void mapSelectedClassByStyleClass(
			PublishOptionalCourseSubjectApplicationForm objform) throws Exception{
		Map<Integer,String> MapSelectedClass=new HashMap<Integer, String>();
		String[] styleClass=objform.getStayClass();
		Map<Integer,String> map=objform.getMapClass();
		for (String string : styleClass) {
			StringTokenizer st=new StringTokenizer(string, ",");
			while(st.hasMoreElements()){
				try{
					int key=Integer.parseInt(st.nextElement().toString());
					if(map.containsKey(key)){
						MapSelectedClass.put(key, map.get(key));
						map.remove(key);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		objform.setMapSelectedClass(CommonUtil.sortMapByValue(MapSelectedClass));
		objform.setMapClass(map);
	}
	

public void editPublishOptionalCourseSubjectApplication(PublishOptionalCourseSubjectApplicationForm publishOptionalCourseSubjectApplicationForm) throws Exception{
	IPublishOptionalCourseSubjectApplication transaction = new PublishOptionalCourseSubjectApplicationTransactionImpl();
	if((publishOptionalCourseSubjectApplicationForm.getId())!= null && !(publishOptionalCourseSubjectApplicationForm.getId().equalsIgnoreCase("") )){
		PublishOptionalCourseSubjectApplication publishOptionalCourseSubjectApplication = transaction.getPublishOptionalCourseSubjectApplication(publishOptionalCourseSubjectApplicationForm.getId());
		PublishOptionalCourseSubjectApplicationHelper.getInstance().setPublishOptionalCourseSubjectApplication(publishOptionalCourseSubjectApplicationForm, publishOptionalCourseSubjectApplication);
		
	
	
	}
}

	

}
