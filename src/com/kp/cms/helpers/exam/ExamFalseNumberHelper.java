package com.kp.cms.helpers.exam;

import java.util.ArrayList;
//import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kp.cms.actions.ajax.CommonAjaxExamAction;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Course;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamDefinitionBO;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.forms.exam.NewExamMarksEntryForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.exam.NewExamMarksEntryHandler;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.exam.MarksCardTO;
import com.kp.cms.to.exam.StudentMarksTO;
import com.kp.cms.to.exam.StudentMarksTO1;
import com.kp.cms.transactions.ajax.ICommonExamAjax;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.transactionsimpl.ajax.CommonAjaxExamImpl;
import com.kp.cms.transactionsimpl.exam.ExamFalseNumberTransactionImpl;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;
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
		IExamFalseNumberTransaction transaction = new ExamFalseNumberTransactionImpl();
		
		String maxNoStr=transaction.getMaxFalseNo(newExamMarksEntryForm);
		List<ExamFalseNumberGen> falseBoList=new ArrayList();
		int maxNo=Integer.parseInt(maxNoStr);
		ExamFalseNumberGen bo=null;
		boolean isadded=false;
		
		Integer academicYear = CommonAjaxHandler.getInstance().getAcademicYearByExam(Integer.parseInt(newExamMarksEntryForm.getExamId()));
		if(academicYear ==null){
			academicYear = 0;
		}
		int schemeId=0;
		ICommonExamAjax ICommonExamAjax = CommonAjaxExamImpl.getInstance();
		Map<Integer, String> schemeMap= ICommonExamAjax
				.getSchemeByCourseId(Integer.parseInt(newExamMarksEntryForm.getCourseId()));
				for (int subId : schemeMap.keySet()){
					schemeId = subId;
				}
		Map<Integer, String> subMap= CommonAjaxHandler.getInstance()
				.getSubjectsCodeNameByCourseSchemeExamIdForFalse("sCode",Integer.parseInt(newExamMarksEntryForm.getCourseId()),schemeId,
				Integer.parseInt(newExamMarksEntryForm.getSchemeNo()), Integer.parseInt(newExamMarksEntryForm.getExamId()));;
		
				
				
		List<StudentMarksTO> boList =newExamMarksEntryForm.getStudentList();
	for (Integer subId : subMap.keySet()){
		newExamMarksEntryForm.setSubjectId(String.valueOf(subId));
		Set<StudentMarksTO> studentList=NewExamMarksEntryHandler.getInstance().getStudentForInput(newExamMarksEntryForm);
		List<StudentMarksTO> list = new ArrayList<StudentMarksTO>(studentList);
		Iterator<StudentMarksTO> itr = list.iterator();
		while (itr.hasNext()) {
		  StudentMarksTO student = (StudentMarksTO) itr.next();
		  String suborderquery="select s.subjectOrder from ExamSubDefinitionCourseWiseBO s where s.subjectId="+subId+" and s.academicYear="+academicYear+" and courseId="+newExamMarksEntryForm.getCourseId();
			if (transaction.getData(newExamMarksEntryForm, suborderquery)!=null) {
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
			Course course=new Course();
			course.setId(Integer.parseInt(newExamMarksEntryForm.getCourseId()));
			bo.setCourse(course);
			st.setRegisterNo(student.getRegisterNo());
			bo.setRegisterNo(String.valueOf(st.getRegisterNo()));
			bo.setLastModifiedDate(new java.util.Date());
			bo.setModifiedBy(newExamMarksEntryForm.getUserId());
			bo.setCreatedBy(newExamMarksEntryForm.getUserId());
			bo.setCreatedDate(new java.util.Date());
			Subject subject=new Subject();
			subject.setId(subId);
			bo.setSubject(subject);
			int appYear=(Integer) transaction.getData(newExamMarksEntryForm, "select s.admAppln.appliedYear from Student s where s.id="+student.getStudentId());
			int suborder=(Integer) transaction.getData(newExamMarksEntryForm, suborderquery);
			String courseCode=(String) transaction.getData(newExamMarksEntryForm,"select c.bankName from Course c where c.id="+newExamMarksEntryForm.getCourseId());
			int sem=Integer.parseInt(newExamMarksEntryForm.getSchemeNo());
			int ptype= (Integer) transaction.getData(newExamMarksEntryForm,"select c.program.programType.id from Course c where c.id="+newExamMarksEntryForm.getCourseId());
			String proggrameType="";
			if(ptype==1)
				proggrameType="U";
			else
				proggrameType="P";
			
			ExamDefinitionBO edef=NewExamMarksEntryTransactionImpl.getInstance().getDetailsByExamID(Integer.parseInt(newExamMarksEntryForm.getExamId()));
			StringBuilder falseNum=new StringBuilder("");
			falseNum.append(String.valueOf(appYear).substring(2));
			falseNum.append("0"+suborder);
			falseNum.append(courseCode);
			falseNum.append(sem);
			if (edef.getIsImprovement()) {
				falseNum.append(proggrameType+"02");
			}else if (edef.getIsReappearance()) {
				falseNum.append(proggrameType+"03");
			}else{
				falseNum.append(proggrameType+"01");
			}
			
			if(newExamMarksEntryForm.getGenerateRandomly()){
				String randPass=PasswordGenerator.getRandomNo();
				while(transaction.DuplicateFalseNo(newExamMarksEntryForm, randPass)){
					randPass=PasswordGenerator.getRandomNo();
				}
				
				bo.setFalseNo(falseNum.toString());
			}
			else{
				falseNum.append(maxNo++ +"");
				bo.setFalseNo(falseNum.toString());
				}
			
			falseBoList.add(bo);
			//isadded=transaction.savedata(bo);
		  //}
		}
		}
	}
		isadded=transaction.savedata(falseBoList);
		newExamMarksEntryForm.setTotalCount(maxNo);
		return isadded;
	}
	
	public boolean convertTotoBoForEdit(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception{
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
				bo.setId(student.getFalseNumberGenId());
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
				
				//isadded=transaction.savedata(bo);
				
	}

	
	
		//}return boList;
		return isadded;
	}
	
	public Map<String, ExamFalseNumberGen> getFlaseNumberMap(NewExamMarksEntryForm newExamMarksEntryForm) throws Exception {
		
		Map<String, ExamFalseNumberGen> falseNumberMap = new HashMap<String, ExamFalseNumberGen>();
		IExamFalseNumberTransaction tx = ExamFalseNumberTransactionImpl.getInstance();
		List<ExamFalseNumberGen> falseNumbers = tx.getfalsenos(newExamMarksEntryForm);
		Iterator<ExamFalseNumberGen> it = falseNumbers.iterator();
		while(it.hasNext()) {
			ExamFalseNumberGen falseNumber = it.next();
			String key = falseNumber.getExamId().getId() + "_" + falseNumber.getCourse().getId() + "_" + falseNumber.getStudentId().getId() + "_" + falseNumber.getSubject().getId();
			falseNumberMap.put(key, falseNumber);
		}
		return falseNumberMap;
	}

}
