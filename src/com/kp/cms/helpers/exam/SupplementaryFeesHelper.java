package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.RegularExamFees;
import com.kp.cms.bo.exam.SupplementaryFees;
import com.kp.cms.to.exam.RegularExamFeesTo;
import com.kp.cms.to.exam.SupplementaryFeesTo;

public class SupplementaryFeesHelper {
	/**
	 * Singleton object of SupplementaryFeesHelper
	 */
	private static volatile SupplementaryFeesHelper supplementaryFeesHelper = null;
	private static final Log log = LogFactory.getLog(SupplementaryFeesHelper.class);
	private SupplementaryFeesHelper() {
		
	}
	/**
	 * return singleton object of SupplementaryFeesHelper.
	 * @return
	 */
	public static SupplementaryFeesHelper getInstance() {
		if (supplementaryFeesHelper == null) {
			supplementaryFeesHelper = new SupplementaryFeesHelper();
		}
		return supplementaryFeesHelper;
	}
	/**
	 * @param boList
	 * @return
	 * @throws Exception
	 */
	public List<SupplementaryFeesTo> convertBotoToList( List<SupplementaryFees> boList) throws Exception {
		List<SupplementaryFeesTo> toList=new ArrayList<SupplementaryFeesTo>();
		SupplementaryFeesTo to=null;
		for (SupplementaryFees bo : boList) {
			to=new SupplementaryFeesTo();
			to.setId(bo.getId());
			to.setCourseName(bo.getCourse().getName());
			to.setTheoryFees(bo.getTheoryFees().doubleValue());
			to.setPracticalFees(bo.getPracticalFees().doubleValue());
			if(bo.getImprovementPracticalFees()!=null)
			to.setImprovementPracticalFees(bo.getImprovementPracticalFees().doubleValue());
			if(bo.getImprovementTheoryFees()!=null)
			to.setImprovementTheoryFees(bo.getImprovementTheoryFees().doubleValue());
			if(bo.getApplicationFees()!=null)
			to.setApplicationFees(bo.getApplicationFees().doubleValue());
			if(bo.getCvCampFees()!=null)
			to.setCvCampFees(bo.getCvCampFees().doubleValue());
			if(bo.getMarksListFees()!=null)
			to.setMarksListFees(bo.getMarksListFees().doubleValue());
			if(bo.getOnlineServiceChargeFees()!=null)
			to.setOnlineServiceChargeFees(bo.getOnlineServiceChargeFees().doubleValue());
			to.setAcademicYear(bo.getAcademicYear());
			to.setEgrandFees(bo.getEgrandFees().doubleValue());
			toList.add(to);
		}
		return toList;
	}

	public List<RegularExamFeesTo> convertBotoToListRegular( List<RegularExamFees> boList) throws Exception {
		List<RegularExamFeesTo> toList=new ArrayList<RegularExamFeesTo>();
		RegularExamFeesTo to=null;
		for (RegularExamFees bo : boList) {
			to=new RegularExamFeesTo();
			to.setId(bo.getId());
			to.setClassName(bo.getClasses().getName());
			to.setTheoryFees(bo.getTheoryFees().doubleValue());
			if(bo.getPracticalFees()!=null)
				to.setPracticalFees(bo.getPracticalFees().doubleValue());			
			if(bo.getApplicationFees()!=null)
				to.setApplicationFees(bo.getApplicationFees().doubleValue());
			if(bo.getCvCampFees()!=null)
				to.setCvCampFees(bo.getCvCampFees().doubleValue());
			if(bo.getMarksListFees()!=null)
				to.setMarksListFees(bo.getMarksListFees().doubleValue());
			if(bo.getOnlineServiceChargeFees()!=null)
				to.setOnlineServiceChargeFees(bo.getOnlineServiceChargeFees().doubleValue());
			to.setAcademicYear(bo.getAcademicYear());
			to.setEgrandFees(bo.getEgrandFees().doubleValue());
			toList.add(to);
		}
		return toList;
	}
}
