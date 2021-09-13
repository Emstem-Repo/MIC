package com.kp.cms.handlers.exam;

import java.util.List;

import com.kp.cms.bo.exam.MarksCardSiNo;
import com.kp.cms.forms.exam.MarksCardSiNoForm;
import com.kp.cms.helpers.exam.MarksCardSiNoHelper;
import com.kp.cms.to.exam.MarksCardSiNoTO;
import com.kp.cms.transactions.exam.IMarksCardSiNoTransaction;
import com.kp.cms.transactionsimpl.exam.MarksCardSiNoTransactionImpl;

public class MarksCardSiNoHandler {
	private static MarksCardSiNoHandler marksCardSiNoHandler=null;
	public static MarksCardSiNoHandler getInstance(){
		if(marksCardSiNoHandler==null){
			marksCardSiNoHandler = new MarksCardSiNoHandler();
		}
		return marksCardSiNoHandler;
	}
	
	IMarksCardSiNoTransaction transaction = new MarksCardSiNoTransactionImpl();
	
	
	public boolean save(MarksCardSiNoForm cardSiNoForm) throws Exception{
		// TODO Auto-generated method stub
		MarksCardSiNo bo = MarksCardSiNoHelper.getInstance().convertFormToBo(cardSiNoForm);
		return transaction.save(bo);
	}


	public boolean getData(MarksCardSiNoForm cardSiNoForm)throws Exception {
		// TODO Auto-generated method stub
		return transaction.getDataAvailable(cardSiNoForm);
	}


	public List<MarksCardSiNoTO> getDataConvert()throws Exception {
		// TODO Auto-generated method stub
		List<MarksCardSiNo> bo = transaction.getData();
		return MarksCardSiNoHelper.getInstance().convertBotoTo(bo);
	}
}
