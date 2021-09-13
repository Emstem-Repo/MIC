package com.kp.cms.helpers.usermanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.admin.Subject;
import com.kp.cms.bo.usermanagement.OptionalCourseApplication;
import com.kp.cms.forms.usermanagement.OptionalCourseApplicationForm;
import com.kp.cms.handlers.usermanagement.OptionalCourseApplicationHandler;
import com.kp.cms.to.usermanagement.OptionalCourseApplicationTO;

public class OptionalCourseApplicationHelper {
	private static volatile OptionalCourseApplicationHelper helper = null;
 
	public static OptionalCourseApplicationHelper getInstance() {
		if (helper == null) {
			helper = new OptionalCourseApplicationHelper();
		}
		return helper;
	}
	public List<OptionalCourseApplicationTO> convertBoToTo(List<Subject> boList, OptionalCourseApplicationForm form){
		List<OptionalCourseApplicationTO> toList = new ArrayList<OptionalCourseApplicationTO>();
		OptionalCourseApplicationTO to =null;
		Iterator itr = boList.iterator();
		Map<Integer,Integer> optionMap = new HashMap<Integer,Integer>();
		int count = 0;
		while(itr.hasNext()){
			Object[] bo =  (Object[]) itr.next();
			to = new OptionalCourseApplicationTO();
			System.out.println(bo[2]);
			if(form.getCourseId().equalsIgnoreCase("23") || form.getCourseId().equalsIgnoreCase("26") ){
				if(bo[2].toString().equalsIgnoreCase("14") || bo[2].toString().equalsIgnoreCase("1") || bo[2].toString().equalsIgnoreCase("20")
						|| bo[2].toString().equalsIgnoreCase("7") || bo[2].toString().equalsIgnoreCase("19") || bo[2].toString().equalsIgnoreCase("31")){
			to.setCourseName(bo[1].toString());
			to.setDepartment(bo[0].toString());
			to.setDeptId(Integer.parseInt(bo[2].toString()));
			to.setSubjectId(Integer.parseInt(bo[3].toString()));
			toList.add(to);
			count++;
			optionMap.put(count, count);
				}
			}else if(form.getCourseId().equalsIgnoreCase("22") || form.getCourseId().equalsIgnoreCase("29")){
				if(bo[2].toString().equalsIgnoreCase("14") || bo[2].toString().equalsIgnoreCase("17")|| bo[2].toString().equalsIgnoreCase("20")
						|| bo[2].toString().equalsIgnoreCase("7") || bo[2].toString().equalsIgnoreCase("19") || bo[2].toString().equalsIgnoreCase("31")){
				to.setCourseName(bo[1].toString());
				to.setDepartment(bo[0].toString());
				to.setDeptId(Integer.parseInt(bo[2].toString()));
				to.setSubjectId(Integer.parseInt(bo[3].toString()));
				toList.add(to);
				count++;
				optionMap.put(count, count);
				}
			}else if(form.getCourseId().equalsIgnoreCase("28") ){
				System.out.println(bo[2].toString());
				if(bo[2].toString().equalsIgnoreCase("14") || bo[2].toString().equalsIgnoreCase("17")|| bo[2].toString().equalsIgnoreCase("20")
						|| bo[2].toString().equalsIgnoreCase("7") || bo[2].toString().equalsIgnoreCase("19") || bo[2].toString().equalsIgnoreCase("1") ){
				to.setCourseName(bo[1].toString());
				to.setDepartment(bo[0].toString());
				to.setDeptId(Integer.parseInt(bo[2].toString()));
				to.setSubjectId(Integer.parseInt(bo[3].toString()));
				toList.add(to);
				count++;
				optionMap.put(count, count);
				}
			}else {
				if(bo[2].toString().equalsIgnoreCase("31")){
					continue;
				}
				to.setCourseName(bo[1].toString());
				to.setDepartment(bo[0].toString());
				to.setDeptId(Integer.parseInt(bo[2].toString()));
				to.setSubjectId(Integer.parseInt(bo[3].toString()));
				toList.add(to);
				count++;
				optionMap.put(count, count);
			}
		}
		form.setOptionMap(optionMap);
		return toList;
	}
	public List<OptionalCourseApplication> convertFormToBo(OptionalCourseApplicationForm form){
		
		List<OptionalCourseApplicationTO> toList = form.getOptionalCourseApplicationTO();
		List<OptionalCourseApplication> boList = new ArrayList<OptionalCourseApplication>();
		Iterator itr = toList.iterator();
		while(itr.hasNext()){
			OptionalCourseApplicationTO to= (OptionalCourseApplicationTO)itr.next();
			if(to.getOption()!=null && !to.getOption().equalsIgnoreCase("")){
				
			
			OptionalCourseApplication bo = new OptionalCourseApplication();
			Department department = new Department();
			Subject subject = new Subject();
			department.setId(to.getDeptId());
			subject.setId(to.getSubjectId());
			bo.setDepartment(department);
			bo.setSubject(subject);
			bo.setCourseOption(Integer.parseInt(to.getOption()));
			bo.setModifiedBy(form.getUserId());
			bo.setLastModifiedDate(new Date());
			bo.setCreatedBy(form.getUserId());
			bo.setCreatedDate(new Date());
			Student student=new Student();
			student.setId(Integer.parseInt(form.getStudentId()));
			bo.setStudent(student);
			
			Classes classes=new Classes();
			classes.setId(Integer.parseInt(form.getClassId()));
			bo.setClasses(classes);
			
			boList.add(bo);
			}
		}
		
		return boList;
	}
	
	public List<OptionalCourseApplicationTO> convertBoToTo1(List<OptionalCourseApplication> boList, OptionalCourseApplicationForm form){
		List<OptionalCourseApplicationTO> toList = new LinkedList<OptionalCourseApplicationTO>();
		OptionalCourseApplicationTO to =null;
		Iterator itr = boList.iterator();
		while(itr.hasNext()){
			OptionalCourseApplication bo =  (OptionalCourseApplication) itr.next();
			to = new OptionalCourseApplicationTO();
			to.setCourseName(bo.getSubject().getName());
			to.setDepartment(bo.getDepartment().getName());
			to.setOption(String.valueOf(bo.getCourseOption()));
			toList.add(to);
		}
		return toList;
	}
	
	
	
}
