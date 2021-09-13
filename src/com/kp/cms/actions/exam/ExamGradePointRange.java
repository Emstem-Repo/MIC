package com.kp.cms.actions.exam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.actions.BaseDispatchAction;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.exam.GradePointRangeForm;

public class ExamGradePointRange extends BaseDispatchAction {

	public ActionForward initGradePointRange(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		GradePointRangeForm gradePointRangeForm=(GradePointRangeForm) form;
		
		return mapping.findForward(CMSConstants.EXAM_GRADEPOINTRANGE);
	}
	
}
