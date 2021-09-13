package com.kp.cms.handlers.exam;

import java.util.Date;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectSection;
import com.kp.cms.exceptions.DuplicateException;
import com.kp.cms.exceptions.ReActivateException;
import com.kp.cms.forms.exam.ConsolidatedSubjectSectionForm;
import com.kp.cms.helpers.exam.ConsolidatedSubjectSectionHelper;
import com.kp.cms.to.exam.ConsolidatedSubjectSectionTO;
import com.kp.cms.transactions.exam.IConsolidatedSubjectSectionTransaction;
import com.kp.cms.transactionsimpl.exam.ConsolidatedSubjectSectionTransactionImpl;

public class ConsolidatedSubjectSectionHandler 
{
	private static volatile ConsolidatedSubjectSectionHandler obj;
	
	public static ConsolidatedSubjectSectionHandler getInstance()
	{
		if(obj == null)
		{
			obj = new ConsolidatedSubjectSectionHandler();			
		}
		return obj;
	}
	
	public List<ConsolidatedSubjectSectionTO> getSubjectSections() throws Exception {
		IConsolidatedSubjectSectionTransaction tx = ConsolidatedSubjectSectionTransactionImpl.getInstance();		 
		List<ConsolidatedSubjectSection> consolidatedSubjectSections = tx.getConsolidatedSubjectSections();
		List<ConsolidatedSubjectSectionTO> consolidatedSubjectSectionsTO = ConsolidatedSubjectSectionHelper.getInstance().convertBOToTO(consolidatedSubjectSections);
		return consolidatedSubjectSectionsTO;
	}
	
	public boolean addSubjectSection(ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm, String mode) throws Exception {
		IConsolidatedSubjectSectionTransaction tx = ConsolidatedSubjectSectionTransactionImpl.getInstance();
		boolean isAdded = false;
		boolean originalNotChanged = false;
		String streamName = "";
		String origStreamName = "";
		
		if(consolidatedSubjectSectionForm.getSectionName() != null && !consolidatedSubjectSectionForm.getSectionName().equals("")) {
			streamName = consolidatedSubjectSectionForm.getSectionName();
		}
		if(consolidatedSubjectSectionForm.getOrigSectionName() != null && !consolidatedSubjectSectionForm.getOrigSectionName().equals(""))	{
			origStreamName = consolidatedSubjectSectionForm.getOrigSectionName();
		}
		
		if(origStreamName.equalsIgnoreCase(streamName)) {
			originalNotChanged = true;
		}
		if(mode.equalsIgnoreCase("Add")) {
			originalNotChanged = false;
		}
		
		if(!originalNotChanged){
			ConsolidatedSubjectSection dupConsolidatedSubjectSection = ConsolidatedSubjectSectionHelper.getInstance().convertFormToBO(consolidatedSubjectSectionForm);
			dupConsolidatedSubjectSection = tx.isDuplicate(dupConsolidatedSubjectSection);
			
			if(dupConsolidatedSubjectSection != null && dupConsolidatedSubjectSection.getIsActive()) {
				throw new DuplicateException();
			}
			else if(dupConsolidatedSubjectSection != null && !dupConsolidatedSubjectSection.getIsActive()) {
				consolidatedSubjectSectionForm.setDupId(dupConsolidatedSubjectSection.getId());
				throw new ReActivateException();
			}
		}
		
		ConsolidatedSubjectSection consolidatedSubjectSection = ConsolidatedSubjectSectionHelper.getInstance().convertFormToBO(consolidatedSubjectSectionForm);
		if(mode.equalsIgnoreCase("Add")) {
			consolidatedSubjectSection.setCreatedBy(consolidatedSubjectSectionForm.getUserId());
			consolidatedSubjectSection.setCreatedDate(new Date());
			consolidatedSubjectSection.setModifiedBy(consolidatedSubjectSectionForm.getUserId());
			consolidatedSubjectSection.setLastModifiedDate(new Date());
		}
		else {
			consolidatedSubjectSection.setModifiedBy(consolidatedSubjectSectionForm.getUserId());
			consolidatedSubjectSection.setLastModifiedDate(new Date());
		}
		
		isAdded = tx.addConsolidatedSubjectSection(consolidatedSubjectSection, mode);
		return isAdded;
	}
	
	public boolean deleteSubjectSection(int dupId, boolean activate, ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm) throws Exception {
		IConsolidatedSubjectSectionTransaction tx = ConsolidatedSubjectSectionTransactionImpl.getInstance();
		boolean deleted = tx.deleteConsolidatedSubjectSection(dupId, activate, consolidatedSubjectSectionForm);
		return deleted;
	}
}
