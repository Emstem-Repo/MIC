package com.kp.cms.helpers.exam;

import java.util.ArrayList;
//import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.forms.exam.ExamFalseNumberForm;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.StudentMarksTO1;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;
import com.kp.cms.utilities.PasswordGenerator;

public class ExamFalseNumberHelper {
	private static ExamFalseNumberHelper marksCardHelper=null;
	public static ExamFalseNumberHelper getInstance(){
		if(marksCardHelper==null){
			marksCardHelper = new ExamFalseNumberHelper();
		}
		return marksCardHelper;
	}
	
	

	public boolean convertTotoBo(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
		// TODO Auto-generated method stub
		
		IExamFalseNumberTransaction transaction = new ExamFalseNumberTransactionImpl();
		List<StudentMarksTO1> boList = new ArrayList<StudentMarksTO1>();
		boList=newExamMarksEntryForm.getStudentlist1();
		ExamFalseNumberGen bo=null;
		boolean isadded=false;
		//if(!list.isEmpty()){
			Iterator<StudentMarksTO1> itr = boList.iterator();
			while (itr.hasNext()) {
				StudentMarksTO1 student = (StudentMarksTO1) itr.next();
				bo = new ExamFalseNumberGen();
				Classes classes=new Classes();
				classes.setId(student.getClassId());
				bo.setClassId(classes);
				ExamDefinition examDef = new ExamDefinition();
				examDef.setId(Integer.parseInt(newExamMarksEntryForm.getExamId()));
				bo.setExamId(examDef);
				Student st=new Student();
				st.setId(student.getStudentId());
				bo.setStudentId(st);
				Subject subject=new Subject();
				subject.setId(Integer.parseInt(newExamMarksEntryForm.getSubjectId()));
				bo.setSubject(subject);
				Course course=new Course();
				course.setId(Integer.parseInt(newExamMarksEntryForm.getCourseId()));
				bo.setCourse(course);
				if(student.getFalseNoId()!=null)
				bo.setId(Integer.parseInt(student.getFalseNoId()));
				st.setRegisterNo(student.getRegisterNo());
				bo.setRegisterNo(String.valueOf(st.getRegisterNo()));
				bo.setFalseNo(student.getFalseNo());
				bo.setLastModifiedDate(new java.util.Date());
				bo.setModifiedBy(newExamMarksEntryForm.getUserId());
				bo.setCreatedBy(newExamMarksEntryForm.getUserId());
				bo.setCreatedDate(new java.util.Date());
				
				isadded=transaction.savedata(bo);
				
	}

	
	
		//}return boList;
		return isadded;
	}

	}
