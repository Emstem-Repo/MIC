package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidateMarksCardSiNo;
import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.forms.exam.ConsolidateMarksCardSiNoForm;
import com.kp.cms.forms.exam.MarksCardSiNoForm;
import com.kp.cms.to.exam.ConsolidateMarksCardSiNoTO;
import com.kp.cms.to.exam.MarksCardSiNoTO;

public class ConsolidateMarksCardSiNoHelper {
	private static ConsolidateMarksCardSiNoHelper consolidateMarksCardSiNoHelper = null;
	public static ConsolidateMarksCardSiNoHelper getInstance(){
		if(consolidateMarksCardSiNoHelper==null){
			consolidateMarksCardSiNoHelper = new ConsolidateMarksCardSiNoHelper();
		}
		return consolidateMarksCardSiNoHelper;
	}
	public ConsolidateMarksCardSiNo convertFormToBo(ConsolidateMarksCardSiNoForm consolidateMarksCardSiNoForm)throws Exception {
		// TODO Auto-generated method stub
		ConsolidateMarksCardSiNo bo = new ConsolidateMarksCardSiNo();
		bo.setStartNo(consolidateMarksCardSiNoForm.getStartNo());
		bo.setIsActive(true);
		bo.setCurrentNo(consolidateMarksCardSiNoForm.getStartNo());
		bo.setCreatedDate(new Date());
		return bo;
	}
	
	//shashi
	public List<ConsolidateMarksCardSiNoTO> convertBotoTo(ConsolidateMarksCardSiNo bo)throws Exception {
		// TODO Auto-generated method stub
		List<ConsolidateMarksCardSiNoTO> toList = new ArrayList<ConsolidateMarksCardSiNoTO>();
		ConsolidateMarksCardSiNoTO to = new ConsolidateMarksCardSiNoTO();
		try{
			if(bo!=null){
				to = new ConsolidateMarksCardSiNoTO();
				to.setId(bo.getId());
				to.setStartNo(bo.getStartNo());
				to.setCurrentNo(bo.getCurrentNo());
				if(bo.getCreatedDate()!=null){
					to.setCreatedDate(bo.getCreatedDate());
				}
				toList.add(to);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return toList;
	}
}
