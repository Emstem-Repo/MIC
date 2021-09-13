package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.SpecialFeesBO;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SpecialFeesTO;

public class SpecialFeesHelper {
	private static volatile SpecialFeesHelper specialFeesHelper = null;
	private static final Log log = LogFactory.getLog(SpecialFeesHelper.class);
	private SpecialFeesHelper() {
		
	}
	/**
	 * return singleton object of SupplementaryFeesHelper.
	 * @return
	 */
	public static SpecialFeesHelper getInstance() {
		if (specialFeesHelper == null) {
			specialFeesHelper = new SpecialFeesHelper();
		}
		return specialFeesHelper;
	}
	public List<SpecialFeesTO> convertBOToTO(List<SpecialFeesBO> boList) throws Exception {
		List<SpecialFeesTO> toList=new ArrayList<SpecialFeesTO>();
		SpecialFeesTO to=null;
		for (SpecialFeesBO bo : boList) {
			to=new SpecialFeesTO();
			to.setId(bo.getId());
			to.setClassName(bo.getClasses().getName());
			to.setTutionFees(bo.getTutionFees().doubleValue());
			if(bo.getSpecialFees()!=null)
				to.setSpecialFees(bo.getSpecialFees().doubleValue());			
			if(bo.getApplicationFees()!=null)
				to.setApplicationFees(bo.getApplicationFees().doubleValue());
			if(bo.getLateFineFees()!=null)
				to.setLateFineFees(bo.getLateFineFees().doubleValue());
			
			to.setAcademicYear(bo.getAcademicYear());
			toList.add(to);
		}
		return toList;
	}
}
