package com.kp.cms.helpers.attendance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.ClassSchemewise;
import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.ExtraCoCurricularLeavePublishBO;
import com.kp.cms.bo.studentExtentionActivity.StudentExtentionFeedback;
import com.kp.cms.forms.attendance.ExtraCoCurricularLeavePublishForm;
import com.kp.cms.to.attendance.ExtraCoCurricularLeavePublishTO;
import com.kp.cms.to.studentExtentionActivity.StudentExtentionFeedbackTO;
import com.kp.cms.utilities.CommonUtil;

public class ExtraCoCurricularLeavePublishHelper {
	private static volatile ExtraCoCurricularLeavePublishHelper extraCoCurricularLeavePublishHelper = null;
	private static final Log log = LogFactory.getLog(ExtraCoCurricularLeavePublishHelper.class);
	public static ExtraCoCurricularLeavePublishHelper getInstance()
	{
		if(extraCoCurricularLeavePublishHelper == null)
		{
			extraCoCurricularLeavePublishHelper = new ExtraCoCurricularLeavePublishHelper();
		}
		return extraCoCurricularLeavePublishHelper;
	}
	public String formatDate(Date date){
		DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
		String newDate=formatter.format(date);
		return newDate;
	}
	
	public List<ExtraCoCurricularLeavePublishBO> convertToBO(ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		List<ExtraCoCurricularLeavePublishBO> list = new ArrayList<ExtraCoCurricularLeavePublishBO>();
		if(extraCoCurricularLeavePublishForm.getClassListId() != null && extraCoCurricularLeavePublishForm.getClassListId().length != 0){
			String[] classesId = extraCoCurricularLeavePublishForm.getClassListId();
					int i = 0;
			for ( i = 0; i < classesId.length; i++) {
				ExtraCoCurricularLeavePublishBO bo = new ExtraCoCurricularLeavePublishBO();
				String classId = classesId[i];
				Classes classes = new Classes();
				classes.setId(Integer.parseInt(classId));
				bo.setClasses(classes);
				bo.setPublishFor(extraCoCurricularLeavePublishForm.getPublishFor());
				String startDate = extraCoCurricularLeavePublishForm.getPublishStartDate();
				java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = df.parse(startDate);
				bo.setPublishStartDate(date1);
				String endDate = extraCoCurricularLeavePublishForm.getPublishEndDate();
				java.text.DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
				Date date2 = df1.parse(endDate);
				bo.setPublishEndDate(date2);
				bo.setCreatedBy(extraCoCurricularLeavePublishForm.getUserId());
				bo.setModifiedBy(extraCoCurricularLeavePublishForm.getUserId());
				bo.setCreatedDate(new Date());
				bo.setModifyDate(new Date());
				bo.setIsActive(true);
				list.add(bo);
			}
		}
		return list;
	}
	public ExtraCoCurricularLeavePublishBO convertToUpdateBo(ExtraCoCurricularLeavePublishBO exPublishBO, 
			ExtraCoCurricularLeavePublishForm extraCoCurricularLeavePublishForm) throws Exception {
		ExtraCoCurricularLeavePublishBO bo = new ExtraCoCurricularLeavePublishBO();
		if(exPublishBO != null && !exPublishBO.toString().isEmpty()){
			bo.setId(exPublishBO.getId());
			bo.setPublishFor(extraCoCurricularLeavePublishForm.getPublishFor());
			bo.setPublishStartDate(CommonUtil.ConvertStringToDate(extraCoCurricularLeavePublishForm.getPublishStartDate()));
			bo.setPublishEndDate(CommonUtil.ConvertStringToDate(extraCoCurricularLeavePublishForm.getPublishEndDate()));
			Classes classes = new Classes();
			classes.setId(Integer.parseInt(extraCoCurricularLeavePublishForm.getClassId()));
			bo.setClasses(classes);
			bo.setCreatedBy(extraCoCurricularLeavePublishForm.getUserId());
			bo.setCreatedDate(new Date());
			bo.setModifiedBy(extraCoCurricularLeavePublishForm.getUserId());
			bo.setModifyDate(new Date()); 
			
		}
		return bo;
	}
	public List<ExtraCoCurricularLeavePublishTO> convertBOToTO(List<ExtraCoCurricularLeavePublishBO> list) throws Exception {
		 List<ExtraCoCurricularLeavePublishTO> list1 = new ArrayList<ExtraCoCurricularLeavePublishTO>();
		   if(list != null && !list.toString().isEmpty()){
			   Iterator<ExtraCoCurricularLeavePublishBO> iterator =  list.iterator();
			   while(iterator.hasNext()){
				   ExtraCoCurricularLeavePublishTO tolist = new ExtraCoCurricularLeavePublishTO();
				   ExtraCoCurricularLeavePublishBO bo = (ExtraCoCurricularLeavePublishBO)iterator.next();
				   if(bo.getId() != 0){
					   tolist.setId(bo.getId());
				   }
				   if(bo.getClasses() != null && bo.getClasses().getId()!=0){
					   tolist.setClassName(bo.getClasses().getName());
				   }
				   if(bo.getPublishStartDate() != null && !bo.getPublishStartDate().toString().isEmpty()){
					   tolist.setStartDate(formatDate(bo.getPublishStartDate()));
				   }
				   if(bo.getPublishEndDate() != null && !bo.getPublishEndDate().toString().isEmpty()){
					   tolist.setEndDate(formatDate(bo.getPublishEndDate()));
				   }
				   
				   if(bo.getClasses()!=null && bo.getClasses().getClassSchemewises()!=null){
						Set<ClassSchemewise> set = bo.getClasses().getClassSchemewises();
						Iterator<ClassSchemewise> iterator1 = set.iterator();
						while (iterator1.hasNext()) {
							ClassSchemewise classSchemewise = (ClassSchemewise) iterator1 .next();
							if(classSchemewise!=null && classSchemewise.getCurriculumSchemeDuration()!= null){
								if(classSchemewise.getCurriculumSchemeDuration().getAcademicYear()!=null){
									tolist.setYear(classSchemewise.getCurriculumSchemeDuration().getAcademicYear().toString());
								}
							}
						}
					}
				   list1.add(tolist);
			   }
		   }
		   return list1;
	}
	
	
}
