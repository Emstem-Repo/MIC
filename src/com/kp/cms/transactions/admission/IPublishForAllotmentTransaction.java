package com.kp.cms.transactions.admission;

import java.util.List;

import com.kp.cms.bo.admission.PublishForAllotment;
import com.kp.cms.forms.admission.PublishForAllotmentForm;

public interface IPublishForAllotmentTransaction {
	
	boolean checkDuplicate(PublishForAllotmentForm allotmentForm)throws Exception;

	boolean addDetails(List<PublishForAllotment> allotments)throws Exception;

	List<PublishForAllotment> getBolist()throws Exception;

	PublishForAllotment getBo(PublishForAllotmentForm allotmentForm) throws Exception;

	boolean updateBo(PublishForAllotment allotment)throws Exception;

}
