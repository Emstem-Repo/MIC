package com.kp.cms.helpers.exam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.forms.exam.AdminHallTicketForm;

public class AdminHallTicketHelper {
	/**
	 * Singleton object of AdminHallTicketHelper
	 */
	private static volatile AdminHallTicketHelper adminHallTicketHelper = null;
	private static final Log log = LogFactory.getLog(AdminHallTicketHelper.class);
	private AdminHallTicketHelper() {
		
	}
	/**
	 * return singleton object of AdminHallTicketHelper.
	 * @return
	 */
	public static AdminHallTicketHelper getInstance() {
		if (adminHallTicketHelper == null) {
			adminHallTicketHelper = new AdminHallTicketHelper();
		}
		return adminHallTicketHelper;
	}
	/**
	 * @param adminHallTicketForm
	 * @return
	 * @throws Exception
	 */
	
	
	
	public String getCurrStudentIdListByRegisterNo( AdminHallTicketForm adminHallTicketForm) throws Exception {
		String query="select s.id,s.registerNo from Student s where (s.isHide = 0 or s.isHide is null) and s.admAppln.isCancelled=0"; 
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and s.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and s.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		//vibin for sbc
		else{
		 
	     //query= "select s.student.id,s.student.registerNo from StudentPreviousClassHistory s where (s.student.isHide = 0 or s.student.isHide is null) and s.student.admAppln.isCancelled=0 and s.classes = "+adminHallTicketForm.getClassId();
			query= "select s.studentId,s.studentUtilBO.registerNo  from ExamSupplementaryImprovementApplicationBO s where (s.isAppearedTheory=1 or s.isAppearedPractical=1) and s.classes = "+adminHallTicketForm.getClassId()+" group by s.studentId";    
		}
	
		return query;
	}
	public String getPrevStudentIdListByRegisterNo( AdminHallTicketForm adminHallTicketForm) throws Exception {
		String query="select s.student.id,s.student.registerNo from StudentPreviousClassHistory s where (s.student.isHide = 0 or s.student.isHide is null) and s.student.admAppln.isCancelled=0" ;
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and s.student.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and s.student.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		return query;
	}
	
	public String getCurrentClassStudentsQuery( AdminHallTicketForm adminHallTicketForm) throws Exception {
		String query="select s.id from Student s where (s.isHide = 0 or s.isHide is null) " +
				"and s.admAppln.isCancelled=0 ";
		if(Integer.parseInt(adminHallTicketForm.getExamId()) >= 20) {	//	only exams id greater than 19 are present in regular app
			query += " and s.id in ( select e.student.id from ExamRegularApplication e where e.classes.id="+Integer.parseInt(adminHallTicketForm.getClassId())+
			" and e.exam.id="+adminHallTicketForm.getExamId()+" and e.challanVerified=1)";
		}
		query += " and s.classSchemewise.classes.id="+adminHallTicketForm.getClassId();
				//" and s.id in(select e.student.id from ExamRegularApplication e where e.exam.id="+adminHallTicketForm.getExamId()+" and e.classes.id ="+adminHallTicketForm.getClassId();
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and e.student.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and e.student.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		//query=query+")"; 
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and s.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and s.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		return query;
	}
	
	
	
	/**
	 * @param adminHallTicketForm
	 * @return
	 * @throws Exception
	 */
	public String getPreviousClassStudentsQuery( AdminHallTicketForm adminHallTicketForm) throws Exception {
		String query="select s.student.id from StudentPreviousClassHistory s where " +
				"(s.student.isHide = 0 or s.student.isHide is null) and s.student.admAppln.isCancelled=0";
		if(Integer.parseInt(adminHallTicketForm.getExamId()) >= 20) {	//	only exams id greater than 19 are present in regular app
			query += " and s.id in ( select e.student.id from ExamRegularApplication e where e.classes.id="+Integer.parseInt(adminHallTicketForm.getClassId())+
			" and e.exam.id="+adminHallTicketForm.getExamId()+" and e.challanVerified=1)";
		}
				
		query += " and s.classes.id="+adminHallTicketForm.getClassId() ;
		//" and s.id in(select e.student.id from ExamRegularApplication e where e.exam.id="+adminHallTicketForm.getExamId()+" and e.classes.id ="+adminHallTicketForm.getClassId();
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and e.student.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and e.student.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		//query=query+")";
		if(adminHallTicketForm.getRegNoFrom()!=null && !adminHallTicketForm.getRegNoFrom().isEmpty()){
			query=query+" and s.student.registerNo>='"+adminHallTicketForm.getRegNoFrom()+"'";
		}
		if(adminHallTicketForm.getRegNoTo()!=null && !adminHallTicketForm.getRegNoTo().isEmpty()){
			query=query+" and s.student.registerNo<='"+adminHallTicketForm.getRegNoTo()+"'";
		}
		return query;
	}
	
	public String getClassListByStudId( int studId) throws Exception {
		String query="select c.id from Classes c where c.id in(select exm.classes.id from ExamSupplementaryImprovementApplicationBO exm "+
				"where exm.studentId="+studId+" and (exm.isAppearedTheory=1 or exm.isAppearedPractical=1)) group by c.id";
		return query;
	}
}
