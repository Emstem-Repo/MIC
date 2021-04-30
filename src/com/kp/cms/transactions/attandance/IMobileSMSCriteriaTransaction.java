package com.kp.cms.transactions.attandance;

import java.util.List;

import com.kp.cms.bo.admin.MobileSMSCriteriaBO1;
import com.kp.cms.forms.attendance.MobileSmsCriteriaForm;

public interface IMobileSMSCriteriaTransaction {

	String[] getSmsClassMap(int currentYear, int smsTimehours,int smsMinitue)throws Exception;
	boolean isDuplicateClass(MobileSmsCriteriaForm mobileSmsCriteriaForm) throws Exception;
	boolean addSMSCriteria(MobileSMSCriteriaBO1 mobileSMSCriteriaBO) throws Exception;
	List<MobileSMSCriteriaBO1> getAllSMSCriteriaList()throws Exception;
	boolean deleteSMSCriteria(MobileSmsCriteriaForm mobileSmsCriteriaForm)throws Exception;
	MobileSMSCriteriaBO1 getSMScriteriaDetails(String smsCriteriaId) throws Exception;
	boolean updateSMSCriteria(MobileSMSCriteriaBO1 smsCriteBo)throws Exception;
	boolean deleteAllOldSMSCriteria(MobileSmsCriteriaForm mobileSmsCriteriaForm) throws Exception;
	boolean addSMSCriteriaForAllClass(List<MobileSMSCriteriaBO1> boList)throws Exception;

}
