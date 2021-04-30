package com.kp.cms.handlers.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.exam.ConsolidateMarksCard;
import com.kp.cms.forms.exam.CertificateMarksCardForm;
import com.kp.cms.helpers.exam.CertificateMarksCardHelper;
import com.kp.cms.to.exam.ConsolidateMarksCardTO;
import com.kp.cms.transactions.exam.INewExamMarksEntryTransaction;
import com.kp.cms.transactionsimpl.exam.NewExamMarksEntryTransactionImpl;

public class CertificateMarksCardHandler {
	/**
	 * Singleton object of CertificateMarksCardHandler
	 */
	INewExamMarksEntryTransaction transaction=NewExamMarksEntryTransactionImpl.getInstance();
	private static volatile CertificateMarksCardHandler certificateMarksCardHandler = null;
	private static final Log log = LogFactory.getLog(CertificateMarksCardHandler.class);
	private CertificateMarksCardHandler() {
		
	}
	/**
	 * return singleton object of CertificateMarksCardHandler.
	 * @return
	 */
	public static CertificateMarksCardHandler getInstance() {
		if (certificateMarksCardHandler == null) {
			certificateMarksCardHandler = new CertificateMarksCardHandler();
		}
		return certificateMarksCardHandler;
	}
	/**
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	public ConsolidateMarksCardTO getStudentCertificateMarksCard(int studentId) throws Exception {
		String query=CertificateMarksCardHelper.getInstance().getStudentCertificateMarksCardQuery(studentId);
		List<ConsolidateMarksCard> boList=transaction.getDataForQuery(query);
		return CertificateMarksCardHelper.getInstance().convertBotoTo(boList,studentId, false);
	}
	/**
	 * @param certificateMarksCardForm
	 * @return
	 */
	public List<ConsolidateMarksCardTO> getStudentForInput( CertificateMarksCardForm certificateMarksCardForm) throws Exception {
		String query=CertificateMarksCardHelper.getInstance().getStudentIdsQueryForInput(certificateMarksCardForm);
		List<Integer> studentIds=transaction.getDataForQuery(query) ;
		List<ConsolidateMarksCardTO> mainList=new ArrayList<ConsolidateMarksCardTO>();
		ConsolidateMarksCardTO to;
		if(studentIds!=null && !studentIds.isEmpty()){
			Iterator<Integer> itr=studentIds.iterator();
			while (itr.hasNext()) {
				Integer sid = (Integer) itr.next();
//				if(sid==5303){
//				System.out.println(sid);
				String query1=CertificateMarksCardHelper.getInstance().getStudentCertificateMarksCardQuery(sid);
				List<ConsolidateMarksCard> boList=transaction.getDataForQuery(query1);
				 to=CertificateMarksCardHelper.getInstance().convertBotoTo(boList,sid, true);
				 if(to!=null)
					 mainList.add(to);
//				}
			}
		}
		return mainList;
	}
}
