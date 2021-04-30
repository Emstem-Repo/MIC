package com.kp.cms.transactions.exam;

import java.util.List;

import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.forms.exam.MarksCardSiNoForm;

public interface IMarksCardSiNoTransaction {

	boolean save(MarksCardSiNo bo)throws Exception;

	List<MarksCardSiNo> getData()throws Exception;

	boolean getDataAvailable(MarksCardSiNoForm cardSiNoForm)throws Exception;

}
