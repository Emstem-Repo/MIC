package com.kp.cms.handlers.exam;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectStream;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.exam.ConsolidatedSubjectStreamForm;
import com.kp.cms.helpers.exam.ConsolidatedSubjectStreamHelper;
import com.kp.cms.to.exam.ConsolidatedSubjectStreamTO;
import com.kp.cms.transactions.exam.IConsolidatedSubjectStreamTransaction;
import com.kp.cms.transactionsimpl.exam.ConsolidatedSubjectStreamTransactionImpl;

public class ConsolidatedSubjectStreamHandler 
{
	private static volatile ConsolidatedSubjectStreamHandler obj;
	
	public static ConsolidatedSubjectStreamHandler getInstance()
	{
		if(obj == null)
		{
			obj = new ConsolidatedSubjectStreamHandler();
		}
		return obj;
	}
	
	public List<ConsolidatedSubjectStreamTO> getSubjectStreams() throws Exception {
		IConsolidatedSubjectStreamTransaction tx = ConsolidatedSubjectStreamTransactionImpl.getInstance();		 
		List<ConsolidatedSubjectStream> consolidatedSubjectStreams = tx.getConsolidatedSubjectStreams();
		List<ConsolidatedSubjectStreamTO> consolidatedSubjectStreamsTO = ConsolidatedSubjectStreamHelper.getInstance().convertBOToTO(consolidatedSubjectStreams);
		return consolidatedSubjectStreamsTO;
	}
	
	public boolean addSubjectStream(ConsolidatedSubjectStreamForm consolidatedSubjectStreamForm, String mode) throws Exception {
		IConsolidatedSubjectStreamTransaction tx = ConsolidatedSubjectStreamTransactionImpl.getInstance();
		boolean isAdded = false;
		boolean originalNotChanged = false;
		String streamName = "";
		String origStreamName = "";
		
		if(consolidatedSubjectStreamForm.getStreamName() != null && !consolidatedSubjectStreamForm.getStreamName().equals("")) {
			streamName = consolidatedSubjectStreamForm.getStreamName();
		}
		if(consolidatedSubjectStreamForm.getOrigStreamName() != null && !consolidatedSubjectStreamForm.getOrigStreamName().equals(""))	{
			origStreamName = consolidatedSubjectStreamForm.getOrigStreamName();
		}
		
		if(origStreamName.equalsIgnoreCase(streamName)) {
			originalNotChanged = true;
		}
		if(mode.equalsIgnoreCase("Add")) {
			originalNotChanged = false;
		}
		
		if(!originalNotChanged){
			ConsolidatedSubjectStream dupConsolidatedSubjectStream = ConsolidatedSubjectStreamHelper.getInstance().convertFormToBO(consolidatedSubjectStreamForm);
			dupConsolidatedSubjectStream = tx.isDuplicate(dupConsolidatedSubjectStream);
			
			if(dupConsolidatedSubjectStream != null && dupConsolidatedSubjectStream.getIsActive()) {
				throw new DuplicateException();
			}
			else if(dupConsolidatedSubjectStream != null && !dupConsolidatedSubjectStream.getIsActive()) {
				consolidatedSubjectStreamForm.setDupId(dupConsolidatedSubjectStream.getId());
				throw new ReActivateException();
			}
		}
		
		ConsolidatedSubjectStream consolidatedSubjectStream = ConsolidatedSubjectStreamHelper.getInstance().convertFormToBO(consolidatedSubjectStreamForm);
		if(mode.equalsIgnoreCase("Add")) {
			consolidatedSubjectStream.setCreatedBy(consolidatedSubjectStreamForm.getUserId());
			consolidatedSubjectStream.setCreatedDate(new Date());
			consolidatedSubjectStream.setModifiedBy(consolidatedSubjectStreamForm.getUserId());
			consolidatedSubjectStream.setLastModifiedDate(new Date());
		}
		else {
			consolidatedSubjectStream.setModifiedBy(consolidatedSubjectStreamForm.getUserId());
			consolidatedSubjectStream.setLastModifiedDate(new Date());
		}
		
		isAdded = tx.addConsolidatedSubjectStream(consolidatedSubjectStream, mode);
		return isAdded;
	}
	
	public boolean deleteSubjectStream(int dupId, boolean activate, ConsolidatedSubjectStreamForm consolidatedSubjectStreamForm) throws Exception {
		IConsolidatedSubjectStreamTransaction tx = ConsolidatedSubjectStreamTransactionImpl.getInstance();
		boolean deleted = tx.deleteConsolidatedSubjectStream(dupId, activate, consolidatedSubjectStreamForm);
		return deleted;
	}
}
