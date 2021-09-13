package com.kp.cms.helpers.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Employee;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.helpers.attendance.NewAttendanceSmsHelper;
import com.kp.cms.to.admin.DepartmentEntryTO;
import com.kp.cms.to.admin.EmployeeTO;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.transactions.attandance.IAttendanceEntryTransaction;
import com.kp.cms.transactionsimpl.attendance.AttendanceEntryTransactionImpl;
import com.kp.cms.transactionsimpl.attendance.NewAttendanceSmsTransImpl;

public class SendBulkSmsToStudentParentsNewHelper 
{
	private static volatile SendBulkSmsToStudentParentsNewHelper sendBulkSmsToStudentParentsNewHelper = null;
	private static final Log log = LogFactory.getLog(SendBulkSmsToStudentParentsNewHelper.class);
	
	public static SendBulkSmsToStudentParentsNewHelper getInstance() 
	{
		if (sendBulkSmsToStudentParentsNewHelper == null) 
		{
			sendBulkSmsToStudentParentsNewHelper = new SendBulkSmsToStudentParentsNewHelper();
			return sendBulkSmsToStudentParentsNewHelper;
		}
		return sendBulkSmsToStudentParentsNewHelper;
	}
	public List<StudentTO> copyStudentBoToTO(List<Student> studentList,List<Integer> listOfDetainedStudents) throws Exception {
		log.info("Handler : Inside copyStudentBoToTO");
		List<StudentTO> list = new ArrayList<StudentTO>();
		IAttendanceEntryTransaction transaction=new AttendanceEntryTransactionImpl();
		Iterator<Student> itr = studentList.iterator();
		StudentTO studentTo;
		Student student;
		StringBuffer studentName=new StringBuffer();
		int subject=0;		
		while(itr.hasNext()) {
			
			
			student = itr.next();
			if(!listOfDetainedStudents.contains(student.getId()))
			{
						studentTo = new StudentTO();
						studentTo.setId(student.getId());
						studentTo.setRegisterNo(student.getRegisterNo());
						studentTo.setRollNo(student.getRollNo());
						studentTo.setChecked(false);
						studentTo.setTempChecked(false);
						
						if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getFirstName() != null) {
							studentName.append(student.getAdmAppln().getPersonalData().getFirstName()).append(" ");
						} 
						if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getMiddleName() != null) {
							studentName.append(student.getAdmAppln().getPersonalData().getMiddleName()).append(" ");
						}
						if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getLastName() != null) {
							studentName.append(student.getAdmAppln().getPersonalData().getLastName()).append(" ");
						}
						if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getParentMob1() != null && student.getAdmAppln().getPersonalData().getParentMob2() != null) 
						{
							if(student.getAdmAppln().getPersonalData().getParentMob1()!=null && !student.getAdmAppln().getPersonalData().getParentMob1().isEmpty())
							{
								studentTo.setParentMobileNo1(student.getAdmAppln().getPersonalData().getParentMob1());
							}
							else
							{
								studentTo.setParentMobileNo1("91");
							}
							studentTo.setParentMobileNo2(student.getAdmAppln().getPersonalData().getParentMob2());
						}
						if(student.getAdmAppln().getPersonalData()!= null && student.getAdmAppln().getPersonalData().getMobileNo1() != null && student.getAdmAppln().getPersonalData().getMobileNo2() != null) 
						{
							if(student.getAdmAppln().getPersonalData().getMobileNo1()!=null && !student.getAdmAppln().getPersonalData().getMobileNo1().isEmpty())
							{
								studentTo.setMobileNo1(student.getAdmAppln().getPersonalData().getMobileNo1());
							}
							else
							{
								studentTo.setMobileNo1("91");
							}
							studentTo.setMobileNo2(student.getAdmAppln().getPersonalData().getMobileNo2());
						}
						studentTo.setStudentName(studentName.toString());
						if(student.getClassSchemewise()!=null && student.getClassSchemewise().getClasses()!=null)
						{
							studentTo.setClassName(student.getClassSchemewise().getClasses().getName());
						}
						
						list.add(studentTo);
						studentName = new StringBuffer();
				
					
				
		}
		}
		log.info("Handler : Leaving copyStudentBoToTO");
	    return list;	
	}
	
	public List<EmployeeTO> copyTeacherBoToTO(List<Employee> employeeList) throws Exception {
		log.info("Handler : Inside copyStudentBoToTO");
		List<EmployeeTO> list = new ArrayList<EmployeeTO>();
		IAttendanceEntryTransaction transaction=new AttendanceEntryTransactionImpl();
		Iterator<Employee> itr = employeeList.iterator();
		EmployeeTO employeeTo;
		Employee employee;
		StringBuffer employeeName=new StringBuffer();
		int subject=0;		
		while(itr.hasNext()) {
			
			DepartmentEntryTO departmentTo=new DepartmentEntryTO();
			employee = itr.next();
			employeeTo = new EmployeeTO();
			employeeTo.setId(employee.getId());
			if(employee.getDepartment()!=null){
			departmentTo.setName( employee.getDepartment().getName());
			departmentTo.setId(employee.getDepartment().getId());
			}
						
						if(employee.getFirstName() != null) {
							employeeName.append(employee.getFirstName()).append(" ");
						} 
						if(employee.getMiddleName() != null) {
							employeeName.append(employee.getMiddleName()).append(" ");
						}
						if(employee.getLastName() != null) {
							employeeName.append(employee.getLastName()).append(" ");
						}
						if(employee.getCurrentAddressMobile1()!= null) 
						{
							employeeTo.setCurrentAddressMobile1(employee.getCurrentAddressMobile1());
						}
						employeeTo.setName(employeeName.toString());
						employeeTo.setDepartmentTo(departmentTo);
						employeeTo.setChecked(false);
						employeeTo.setTempChecked(false);
						
						list.add(employeeTo);
						employeeName = new StringBuffer();
				
					
				
		
		}
		log.info("Handler : Leaving copyTeacherBoToTO");
	    return list;	
	}
	
	public List<EmployeeTO> copyNonTeacherBoToTO(List<Employee> employeeList) throws Exception {
		log.info("Handler : Inside copyStudentBoToTO");
		List<EmployeeTO> list = new ArrayList<EmployeeTO>();
		IAttendanceEntryTransaction transaction=new AttendanceEntryTransactionImpl();
		Iterator<Employee> itr = employeeList.iterator();
		EmployeeTO employeeTo;
		Employee employee;
		StringBuffer employeeName=new StringBuffer();
		int subject=0;		
		while(itr.hasNext()) {
			
			DepartmentEntryTO departmentTo=new DepartmentEntryTO();
			employee = itr.next();
			employeeTo = new EmployeeTO();
			employeeTo.setId(employee.getId());
			departmentTo.setName(employee.getDepartment().getName());
			departmentTo.setId(employee.getDepartment().getId());
						
						if(employee.getFirstName() != null) {
							employeeName.append(employee.getFirstName()).append(" ");
						} 
						if(employee.getMiddleName() != null) {
							employeeName.append(employee.getMiddleName()).append(" ");
						}
						if(employee.getLastName() != null) {
							employeeName.append(employee.getLastName()).append(" ");
						}
						if(employee.getCurrentAddressMobile1()!= null) 
						{
							employeeTo.setCurrentAddressMobile1(employee.getCurrentAddressMobile1());
						}
						employeeTo.setName(employeeName.toString());
						employeeTo.setDepartmentTo(departmentTo);
						employeeTo.setChecked(false);
						employeeTo.setTempChecked(false);
						
						list.add(employeeTo);
						employeeName = new StringBuffer();
				
					
				
		
		}
		log.info("Handler : Leaving copyNonTeacherBoToTO");
	    return list;	
	}

}
