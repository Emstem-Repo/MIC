package com.kp.cms.transactions.exam;

import java.util.List;

import com.kp.cms.bo.exam.FalseNumSiNo;
import com.kp.cms.bo.exam.FalseNumberBox;
import com.kp.cms.bo.exam.FalseNumberBoxDetails;
import com.kp.cms.forms.exam.FalseNumSiNoForm;

public interface IFalseNumSiNoTransaction {

	boolean save(FalseNumSiNo bo)throws Exception;

	List<FalseNumSiNo> getData()throws Exception;

	boolean getDataAvailable(FalseNumSiNoForm cardSiNoForm)throws Exception;

	FalseNumSiNo getFalseNoBoObject(FalseNumSiNoForm cardSiNoForm)throws Exception;

	boolean updateFalseNo(FalseNumSiNo numSiNo)throws Exception;

	boolean updateFalseNoBox(List<FalseNumberBoxDetails> falseNumberBox);

	boolean getDuplicate(FalseNumSiNoForm cardSiNoForm);

	List<FalseNumberBox> getFalseBox(FalseNumSiNoForm cardSiNoForm);

}
