package com.kp.cms.transactions.exam;

import java.util.List;

import com.kp.cms.bo.exam.SpecialFeesBO;

public interface ISpecialFeesTransaction {

	List<SpecialFeesBO> getList(String query) throws Exception;

	Object getMasterEntryDataById(Class<SpecialFeesBO> class1, int id) throws Exception;

}
