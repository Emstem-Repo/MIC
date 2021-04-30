package com.kp.cms.handlers.studentExtentionActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.EvaStudentFeedbackOpenConnection;
import com.kp.cms.bo.admin.EvaluationStudentFeedbackSession;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.admin.EvaStudentFeedbackOpenConnectionForm;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionFeedbackForm;
import com.kp.cms.helpers.admin.EvaStudentFeedbackOpenConnectionHelper;
import com.kp.cms.helpers.studentExtentionActivity.StudentExtentionFeedbackHelper;
import com.kp.cms.helpers.studentExtentionActivity.StudentExtentionHelper;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionFeedbackTransaction;
import com.kp.cms.transactions.studentExtentionActivity.IStudentExtentionTransaction;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentExtentionFeedbackTransactionImpl;
import com.kp.cms.transactionsimpl.studentExtentionActivity.StudentExtentionTransactionImpl;
import com.kp.cms.utilities.CurrentAcademicYear;

public class StudentExtentionFeedbackHandler {
	private static volatile StudentExtentionFeedbackHandler obj;
	public static StudentExtentionFeedbackHandler getInstance()
	{
		if(obj == null)
		{
			obj = new StudentExtentionFeedbackHandler();
		}
		return obj;
	}
	IStudentExtentionFeedbackTransaction tx = StudentExtentionFeedbackTransactionImpl.getInstance();
	
	public String formatDate(Date date){
		DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
		String newDate=formatter.format(date);
		return newDate;
	}
	
	public List<StudentGroupTO> getStudentGroup()throws Exception {
		List<StudentGroup> studentgroup = tx.getStudentGroupDetails();
		List<StudentGroupTO> studentGroupTO = StudentExtentionFeedbackHelper.getInstance().convertTo(studentgroup);
		return studentGroupTO;
	}
	public List<StudentExtentionTO> getStudentExtention() throws Exception{
		List<StudentExtention> studentextention = tx.getStudentExtentionDetails();
		List<StudentExtentionTO> studentExtentionTO = StudentExtentionFeedbackHelper.getInstance().convertBOTOTO(studentextention);
	     return studentExtentionTO;
	}
   public List<StudentExtentionFeedbackTO> getStudentDetails(int year) throws Exception{
	   List<StudentExtentionFeedback> feedbackConnection = tx.getFeedbackList(year);
	   List<StudentExtentionFeedbackTO> feedbackDetails = StudentExtentionFeedbackHelper.getInstance().convertTO(feedbackConnection);
	   return feedbackDetails;
   }
   
   public List<StudentExtentionFeedbackTO> getDetailsBySessionId(String sessionId) throws Exception{
	   List<StudentExtentionFeedback> feedbackConnection = tx.getConnectionListBySessionId();
	   List<StudentExtentionFeedbackTO> feedbackDetails = StudentExtentionFeedbackHelper.getInstance().convertTO(feedbackConnection);
	   return feedbackDetails;
   }
   public List<StudentExtentionFeedbackTO> getDetailsBySessionIdNew(HttpServletRequest request) throws Exception{
	   List<StudentExtentionFeedback> feedbackConnection = tx.getConnectionListBySessionIdNew();
	   List<StudentExtentionFeedbackTO> feedbackDetails = StudentExtentionFeedbackHelper.getInstance().convertTO(feedbackConnection);
	   return feedbackDetails;
   }
   
   public boolean duplicateCheck( StudentExtentionFeedbackForm feedbackform) throws Exception{
		boolean isCheckDuplicate = tx.checkDuplicate(feedbackform);
		return isCheckDuplicate;
	}
   
   public boolean submitOpenConnectionDetails( StudentExtentionFeedbackForm feedbackform) throws Exception{
		boolean isAdded = false;
		Map<Integer,Integer> specializationIds = null;
		/*if(feedbackform.getSpecializationId()!=null && !feedbackform.getSpecializationId().isEmpty()){
			String specializationName = tx.getSpecializationName(feedbackform.getSpecializationId());
			 specializationIds = tx.getSpecializationIds(feedbackform.getClassesId(),specializationName);
		}*/
			List<StudentExtentionFeedback> boList = StudentExtentionFeedbackHelper.getInstance().copyFromFormToBO(feedbackform,specializationIds);
			isAdded = tx.submitOpenConnectionDetails(boList);
		return isAdded;
	}
   public boolean deleteOpenConnection( StudentExtentionFeedbackForm feedbackform) throws Exception{
		boolean isDeleted = tx.deleteOpenConnection(feedbackform);
		return isDeleted;
	}
   
  public boolean updateOpenConnectionDetails( StudentExtentionFeedbackForm feedbackform)throws Exception {
	  String[] clsId = feedbackform.getClassesId();
	  String classId = clsId[0];
	  int recordId = tx.getRecordId(classId);
	  if(recordId != 0){
	  feedbackform.setId(recordId);
	  }
		boolean isUpdated = tx.updateOpenConnection(feedbackform);
		return isUpdated;
	}
   public Map<Integer,String> getFeedBackSessionList(StudentExtentionFeedbackForm feedbackform) throws Exception {
		Map<Integer,String> sessionMap ;
		if(feedbackform.getAcademicYear()==null){
			Integer year = CurrentAcademicYear.getInstance().getAcademicyear();
			
			List<EvaluationStudentFeedbackSession> sessionsList = tx.getSessionDetails(year);
			 sessionMap = StudentExtentionFeedbackHelper.getInstance().BoTOTo(sessionsList);
		}else{
			List<EvaluationStudentFeedbackSession> sessionsList = tx.getSessionDetails(Integer.parseInt(feedbackform.getAcademicYear()));
			sessionMap = StudentExtentionFeedbackHelper.getInstance().BoTOTo(sessionsList);
		}
		
		return sessionMap;
	}
   
   public void  getEditOpenConnectionDetails(StudentExtentionFeedbackForm feedbackform) throws Exception{
	   StudentExtentionFeedback bo = tx.getFeedbackDetails(feedbackform.getId());
	   if(bo!=null && !bo.toString().isEmpty()){
			if(bo.getId()!=0 ){
				feedbackform.setId(bo.getId());
			}
			if(bo.getClassId()!=null && !bo.getClassId().toString().isEmpty()){
				String classesId[] =  new String[1];
				 classesId[0] = Integer.toString(bo.getClassId().getId());
				 feedbackform.setClassesId(classesId);
			}
			if(bo.getStartDate()!=null && !bo.getStartDate().toString().isEmpty()){
				feedbackform.setStartDate(formatDate(bo.getStartDate()));
			}
			if(bo.getEndDate()!=null && !bo.getEndDate().toString().isEmpty()){
				feedbackform.setEndDate(formatDate(bo.getEndDate()));
			}
			if(bo.getClassId()!=null && bo.getClassId().getClassSchemewises()!=null){
				Set<ClassSchemewise> set = bo.getClassId().getClassSchemewises();
				Iterator<ClassSchemewise> iterator = set.iterator();
				while (iterator.hasNext()) {
					ClassSchemewise classSchemewise = (ClassSchemewise) iterator .next();
					if(classSchemewise!=null && classSchemewise.getCurriculumSchemeDuration()!= null){
						if(classSchemewise.getCurriculumSchemeDuration().getAcademicYear()!=null){
							feedbackform.setAcademicYear(classSchemewise.getCurriculumSchemeDuration().getAcademicYear().toString());
						}
					}
				}
			}
		}
   }


}


