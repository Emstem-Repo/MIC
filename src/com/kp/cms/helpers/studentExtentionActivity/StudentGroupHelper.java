package com.kp.cms.helpers.studentExtentionActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.studentExtentionActivity.StudentGroupForm;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;

public class StudentGroupHelper {

	 private static volatile StudentGroupHelper obj;
	 public static StudentGroupHelper getInstance(){
		  if(obj == null){
			  obj = new StudentGroupHelper();
		  }
		  return obj;
	 }
	 
	 public List<StudentGroupTO> convertBOToTO(List<StudentGroup> studentGroup) throws Exception
		{
			Iterator<StudentGroup> it = studentGroup.iterator();
			List<StudentGroupTO> groupTO = new ArrayList<StudentGroupTO>();
			
			while(it.hasNext())
			{
				StudentGroupTO objTO = new StudentGroupTO();
				StudentGroup obj_BO = it.next();
				objTO.setId(obj_BO.getId());
				objTO.setGroupName(obj_BO.getGroupName());
				groupTO.add(objTO);
			}
			return groupTO;
		}
	 
	 public StudentGroup convertFormToBO(StudentGroupForm masterForm) throws Exception
		{
		    StudentGroup masterForms = new StudentGroup();
			masterForms.setId(masterForm.getId());
			masterForms.setGroupName(masterForm.getGroupName());
			masterForms.setIsActive(true);
			return masterForms;
		}
}
