package com.kp.cms.helpers.studentExtentionActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectStream;
import com.kp.cms.bo.studentExtentionActivity.StudentExtention;
import com.kp.cms.bo.studentExtentionActivity.StudentGroup;
import com.kp.cms.forms.exam.ConsolidatedSubjectStreamForm;
import com.kp.cms.forms.studentExtentionActivity.StudentExtentionForm;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionTO;
import com.kp.cms.to.studentExtentionActivity.StudentGroupTO;

public class StudentExtentionHelper {

private static volatile StudentExtentionHelper obj;
	
	public static StudentExtentionHelper getInstance()
	{
		if(obj == null)
		{
			obj = new StudentExtentionHelper();
		}
		return obj;
	}
	
	
	
	public List<StudentExtentionTO> convertBOToTO(List<StudentExtention> studentExtention) throws Exception
	{
		Iterator<StudentExtention> it = studentExtention.iterator();
		List<StudentExtentionTO> extentionTO = new ArrayList<StudentExtentionTO>();
		while(it.hasNext())
		{
			StudentExtentionTO objto = new StudentExtentionTO();
			StudentExtention objbo = it.next();
			objto.setId(objbo.getId());
			objto.setActivityName(objbo.getActivityName());
			objto.setDisplayOrder(objbo.getDisplayOrder());
			objto.setGroupName(objbo.getStudentGroup().getGroupName());
			extentionTO.add(objto);
		}
		return extentionTO;
	}
	
	public StudentExtention convertFormToBO(StudentExtentionForm masterForm) throws Exception
	{
		StudentGroup group = new StudentGroup();
		StudentExtention studentExtention = new StudentExtention();
		studentExtention.setId(masterForm.getId());
		studentExtention.setActivityName(masterForm.getActivityName());
		studentExtention.setDisplayOrder(masterForm.getDisplayOrder());
		studentExtention.setIsActive(true);
		group.setId(masterForm.getStudentGroupId());
		studentExtention.setStudentGroup(group);
		
		return studentExtention;
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
}
