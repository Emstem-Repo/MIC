package com.kp.cms.transactions.exam;

import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectSection;
import com.kp.cms.forms.exam.ConsolidatedSubjectSectionForm;

public interface IConsolidatedSubjectSectionTransaction 
{
	public List<ConsolidatedSubjectSection> getConsolidatedSubjectSections() throws Exception;
	public boolean addConsolidatedSubjectSection(ConsolidatedSubjectSection consolidatedSubjectSection, String mode) throws Exception;
	public boolean deleteConsolidatedSubjectSection(int dupId, boolean activate, ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm) throws Exception;
	public ConsolidatedSubjectSection isDuplicate(ConsolidatedSubjectSection oldConsolidatedSubjectSection) throws Exception;
}
