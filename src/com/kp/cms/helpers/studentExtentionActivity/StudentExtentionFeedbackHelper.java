package com.kp.cms.helpers.studentExtentionActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackSession;
import com.kp.cms.bo.exam.ExamSpecializationBO;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.utilities.CommonUtil;

public class StudentExtentionFeedbackHelper {
	private static volatile StudentExtentionFeedbackHelper obj;
	public static StudentExtentionFeedbackHelper getInstance(){
		if(obj == null){
			obj = new StudentExtentionFeedbackHelper();
		}
		return obj;
	}
	public String formatDate(Date date){
		DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
		String newDate=formatter.format(date);
		return newDate;
	}
	
	public List<StudentGroupTO> convertTo(List<StudentGroup> studentgroup) {
		List<StudentGroupTO> extentionTO = new ArrayList<StudentGroupTO>();
		Iterator<StudentGroup> iterator = studentgroup.iterator();
		while(iterator.hasNext())
		{
			StudentGroupTO obj_TO = new StudentGroupTO();
			StudentGroup group = iterator.next();
			obj_TO.setGroupName(group.getGroupName());
			obj_TO.setId(group.getId());
			extentionTO.add(obj_TO);
		}
		return extentionTO;
	}
	
	public List<StudentExtentionTO> convertBOTOTO(List<StudentExtention> studentextention){
		List<StudentExtentionTO> studentTO = new ArrayList<StudentExtentionTO>();
		Iterator<StudentExtention> iterator = studentextention.iterator();
		while(iterator.hasNext()){
			StudentExtentionTO obj = new StudentExtentionTO();
			StudentExtention extention = iterator.next();
			obj.setActivityName(extention.getActivityName());
			obj.setId(extention.getId());
			studentTO.add(obj);
			
		}
		return studentTO;
	}
   public List<StudentExtentionFeedbackTO> convertTO( List<StudentExtentionFeedback> feedbackConnection)throws Exception{
	   List<StudentExtentionFeedbackTO> list = new ArrayList<StudentExtentionFeedbackTO>();
	   if(feedbackConnection != null && !feedbackConnection.toString().isEmpty()){
		   Iterator<StudentExtentionFeedback> iterator =  feedbackConnection.iterator();
		   while(iterator.hasNext()){
			   StudentExtentionFeedbackTO tolist = new StudentExtentionFeedbackTO();
			   StudentExtentionFeedback bo = (StudentExtentionFeedback)iterator.next();
			   if(bo.getId() != 0){
				   tolist.setId(bo.getId());
			   }
			   if(bo.getClassId() != null && bo.getClassId().getId()!=0){
				   tolist.setClassName(bo.getClassId().getName());
			   }
			   if(bo.getStartDate() != null && !bo.getStartDate().toString().isEmpty()){
				   tolist.setStartDate(formatDate(bo.getStartDate()));
			   }
			   if(bo.getEndDate() != null && !bo.getEndDate().toString().isEmpty()){
				   tolist.setEndDate(formatDate(bo.getEndDate()));
			   }
			   if(bo.getGroupId()!=null && !bo.getGroupId().toString().isEmpty()){
				   tolist.setGroupName(bo.getGroupId().getGroupName());
			   }
			   if(bo.getClassId()!=null && bo.getClassId().getClassSchemewises()!=null){
					Set<ClassSchemewise> set = bo.getClassId().getClassSchemewises();
					Iterator<ClassSchemewise> iterator1 = set.iterator();
					while (iterator1.hasNext()) {
						ClassSchemewise classSchemewise = (ClassSchemewise) iterator1 .next();
						if(classSchemewise!=null && classSchemewise.getCurriculumSchemeDuration()!= null){
							if(classSchemewise.getCurriculumSchemeDuration().getAcademicYear()!=null){
								tolist.setYear(classSchemewise.getCurriculumSchemeDuration().getAcademicYear().toString());
							}
						}
					}
				}
			   list.add(tolist);
		   }
	   }
	   return list;
   }
   
   public Map<Integer,String> BoTOTo( List<EvaluationStudentFeedbackSession> sessionsList) throws Exception{
		Map<Integer,String> sessionToMap = new HashMap<Integer, String>();
		if(sessionsList!=null && !sessionsList.isEmpty()){
			Iterator<EvaluationStudentFeedbackSession> iterator = sessionsList.iterator();
			while (iterator.hasNext()) {
				EvaluationStudentFeedbackSession bo = (EvaluationStudentFeedbackSession) iterator .next();
				if(bo.getId()!=0){
					sessionToMap.put(bo.getId(), bo.getName());
				}
			}
		}
		return sessionToMap;
	}
   
   public List<StudentExtentionFeedback> copyFromFormToBO(StudentExtentionFeedbackForm feedbackform, Map<Integer,Integer> specializationIds) throws Exception{
	   List<StudentExtentionFeedback> feedbackList = new ArrayList<StudentExtentionFeedback>();
	   if(feedbackform.getClassesId()!=null && feedbackform.getClassesId().length !=0 ){
		   String[] classesId = feedbackform.getClassesId();
			int i =0;
			for(i=0;i<classesId.length;i++){
				StudentExtentionFeedback obj = new StudentExtentionFeedback();
				String classId = classesId[i];
				Classes classes = new Classes();
				classes.setId(Integer.parseInt(classId));
				obj.setClassId(classes);
				if(feedbackform.getStartDate()!= null && !feedbackform.getStartDate().toString().isEmpty()){
					obj.setStartDate(CommonUtil.ConvertStringToDate(feedbackform.getStartDate()));
				}
                if(feedbackform.getEndDate()!= null && !feedbackform.getEndDate().toString().isEmpty()){
					obj.setEndDate(CommonUtil.ConvertStringToDate(feedbackform.getEndDate()));
				}
                StudentGroup group = new StudentGroup();
                group.setId(Integer.parseInt(feedbackform.getStudentGroupId()));
                obj.setGroupId(group);
                obj.setCreatedBy(feedbackform.getUserId());
				obj.setCreatedDate(new Date());
				obj.setIsActive(true);
				feedbackList.add(obj);
			}
	   }
	   return feedbackList;
	    
   }
}
