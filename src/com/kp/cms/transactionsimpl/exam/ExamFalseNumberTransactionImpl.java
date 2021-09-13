package com.kp.cms.transactionsimpl.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.EligibilityCriteria;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamFalseNumberGen;
import com.kp.cms.bo.exam.StudentFinalMarkDetails;
import com.kp.cms.exceptions.ApplicationException;
import com.kp.cms.forms.exam.ConsolidatedMarksCardForm;
import com.kp.cms.forms.exam.ExamFalseNumberForm;
//import com.kp.cms.transactions.exam.IMarksCardTransaction;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.exam.IExamFalseNumberTransaction;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.utilities.InitSessionFactory;

public class ExamFalseNumberTransactionImpl implements IExamFalseNumberTransaction{
	

	@Override
	public List<Student> getQueryForCurrentClass(ExamFalseNumberForm marksCardForm)
			throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query=("select s from Student s inner join s.classSchemewise csc join csc.curriculumSchemeDuration csd join csc.classes cls "+
                      " where s.admAppln.isCancelled=0  and (s.isHide=0 or s.isHide is null) "+
                      " and s.classSchemewise.curriculumSchemeDuration.academicYear=(select e.academicYear from ExamDefinitionBO e where e.id="+marksCardForm.getExamName()+")"+ 
                      " and s.admAppln.courseBySelectedCourseId.id=" +marksCardForm.getCourseId()+" and s.classSchemewise.curriculumSchemeDuration.semesterYearNo="+marksCardForm.getSchemeNo()+ 
                      " and s.classSchemewise.classes.id="+marksCardForm.getClassId()+" and s.id not in(select b.student.id from ExamStudentDetentionRejoinDetails b where b.discontinued=1 or b.detain=1)") ;
				
			Query query1=session.createQuery(query);
		List<Student> list=query1.list();
		return list;
	}



	@Override
	public void getcourseansScheme(ExamFalseNumberForm marksCardForm)
			throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query="select c.course.id,c.termNumber from Classes c where c.id="+marksCardForm.getClassId()+" and c.isActive=1";
		Query query1=session.createQuery(query);
		List list=query1.list();
		Iterator<Object> itr=list.iterator();
		while(itr.hasNext()){
			Object[] obj=(Object[]) itr.next();
			if(obj[0]!=null && obj[1]!=null){
				marksCardForm.setCourseId(obj[0].toString());
				marksCardForm.setSchemeNo(obj[1].toString());
			}
		}
	}

	@Override
	public boolean savedata(ExamFalseNumberGen bo) throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		try{
		
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		txn.begin();
		session.saveOrUpdate(bo);	
		txn.commit();
		return true;
		}
		catch (Exception exception) {
			if (txn != null) {
				txn.rollback();
				
			}
			return false;

		} finally {
			if (session != null) {
				session.flush();
				// session.close();
			}
		}
	}

	@Override
	public List<ExamFalseNumberGen> getfalsenos(
			ExamFalseNumberForm marksCardForm) throws Exception {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction txn=null;
		List<ExamFalseNumberGen> list=null;
		session=(Session)HibernateUtil.getSession();
		txn=session.beginTransaction();
		String query="from ExamFalseNumberGen e where e.classId.id="+marksCardForm.getClassId()+" and e.examId.id="+marksCardForm.getExamName();
		Query query1=session.createQuery(query);
		list=query1.list();
		
		return list;
	}


}
