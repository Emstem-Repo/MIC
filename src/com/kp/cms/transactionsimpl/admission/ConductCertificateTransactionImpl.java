package com.kp.cms.transactionsimpl.admission;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.StudentTCDetails;
import com.kp.cms.exceptions.BusinessException;
import com.kp.cms.utilities.HibernateUtil;
import com.kp.cms.transactions.admission.IConductCertificateTransaction;
public class ConductCertificateTransactionImpl implements IConductCertificateTransaction{	
	public List<StudentTCDetails> getStudentList(String classId, String fromReg,
			String toReg, String studentId) throws Exception {
		
		
		Session session=null;
		try {
			session = HibernateUtil.getSession();
			StringBuffer sqlString = new StringBuffer(
					"from StudentTCDetails s where  "	);
			
			if(fromReg!= null && (fromReg == "R" || fromReg.equalsIgnoreCase("R"))){
				sqlString.append("( s.student.admAppln.isSelected=1 and s.student.admAppln.isApproved=1 and s.student.registerNo>='"+classId+"' OR ( s.student.admAppln.isCancelled = 1)) and s.student.registerNo='"+classId+"'");
			}
			else{
				sqlString.append("( s.student.admAppln.isSelected=1 and s.student.admAppln.isApproved=1 and s.student.registerNo>='"+classId+"' OR ( s.student.admAppln.isCancelled = 1)) and s.student.registerNo='"+classId+"'");
			}
			
			Query  query = session.createQuery(sqlString.toString());
			List<StudentTCDetails> studentList = query.list();
			session.flush();
			return studentList;
		} catch (Exception e) {
			throw  new BusinessException(e);
		}		
	}
	
	public List<StudentTCDetails> getStudentsByName(String name) throws Exception {
		
		Session session=null;
		List<StudentTCDetails> student=null;
		try {
			session = HibernateUtil.getSession();
			/* code changed by sudhir */
			/*StringBuffer sqlString = new StringBuffer(
					"select s from Student s " +
					"where s.registerNo='"+reg+"' " +
					" and s.tcNo is not null");*/
			StringBuffer sqlString = new StringBuffer(
			"from StudentTCDetails stc where" +
			" stc.student.id in (select s.id from Student s " +
			" where s.admAppln.personalData.firstName like '%"+name+"%' ) "	);		
			
			Query  query = session.createQuery(sqlString.toString());
			student = query.list();
			session.flush();
			return student;
		} catch (Exception e) {
			throw  new BusinessException(e);
		}	
	}
	public int getClassTermNumber(int classId) throws Exception {
		int termNumber = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			String query = "select c.classes.termNumber from ClassSchemewise c where c.id = " +classId;
			termNumber = (Integer) session.createQuery(query).uniqueResult();
		} catch (Exception e) {
			if (session != null){
				session.flush();
			}
		}
		return termNumber;
	}
}
