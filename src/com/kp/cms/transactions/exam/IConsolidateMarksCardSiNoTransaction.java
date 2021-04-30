package com.kp.cms.transactions.exam;

import com.kp.cms.bo.exam.ConsolidateMarksCardSiNo;


public interface IConsolidateMarksCardSiNoTransaction {

	boolean save(ConsolidateMarksCardSiNo bo)throws Exception;

	ConsolidateMarksCardSiNo getData()throws Exception;

	boolean getDataAvailable()throws Exception;

}
