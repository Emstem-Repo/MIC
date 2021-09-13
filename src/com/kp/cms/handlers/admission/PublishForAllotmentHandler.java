package com.kp.cms.handlers.admission;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.forms.admission.PublishForAllotmentForm;
import com.kp.cms.helpers.admission.PublishForAllotmentHelper;
import com.kp.cms.to.admission.PublishForAllotmentTO;
import com.kp.cms.transactions.admission.IPublishForAllotmentTransaction;
import com.kp.cms.transactionsimpl.admission.PublishForAllotmentTransactionImpl;
import com.kp.cms.utilities.CommonUtil;

public class PublishForAllotmentHandler {
	
	
	public static volatile PublishForAllotmentHandler handler=null;
	
	public static PublishForAllotmentHandler getInstance(){
		if(handler==null){
			handler = new PublishForAllotmentHandler();
			return handler;
		}
		return handler;
	}

	public boolean checkDuplicate(PublishForAllotmentForm publishForAllotmentForm)throws Exception {
		IPublishForAllotmentTransaction transaction = PublishForAllotmentTransactionImpl.getInstance();
		return transaction.checkDuplicate(publishForAllotmentForm);
	}

	public boolean addAllotmentDetails(PublishForAllotmentForm publishForAllotmentForm)throws Exception {
		List<PublishForAllotment> allotments=PublishForAllotmentHelper.getInstance().setBoList(publishForAllotmentForm);
		IPublishForAllotmentTransaction transaction = PublishForAllotmentTransactionImpl.getInstance();
		boolean isAdded=transaction.addDetails(allotments);
		return isAdded;
	}

	public List<PublishForAllotmentTO> getToList()throws Exception {
		IPublishForAllotmentTransaction transaction = PublishForAllotmentTransactionImpl.getInstance();
		List<PublishForAllotment> allotments =transaction.getBolist();
		List<PublishForAllotmentTO> allotmentTOs =PublishForAllotmentHelper.getInstance().getTolist(allotments);
		return allotmentTOs;
	}

	public boolean updateAllotmentDetails(PublishForAllotmentForm allotmentForm) throws Exception{
		IPublishForAllotmentTransaction transaction = PublishForAllotmentTransactionImpl.getInstance();
		PublishForAllotment allotment =transaction.getBo(allotmentForm);
		allotment.setFromDate(CommonUtil.ConvertStringToSQLDate(allotmentForm.getFromDate()));
		allotment.setEndDate(CommonUtil.ConvertStringToSQLDate(allotmentForm.getEndDate()));
		allotment.setModifiedBy(allotmentForm.getUserId());
		allotment.setLastModifiedDate(new Date());
		return transaction.updateBo(allotment);
	}

	
	
}
