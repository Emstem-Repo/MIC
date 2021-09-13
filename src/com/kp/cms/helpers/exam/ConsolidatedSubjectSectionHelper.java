package com.kp.cms.helpers.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kp.cms.bo.exam.ConsolidatedSubjectSection;
import com.kp.cms.forms.exam.ConsolidatedSubjectSectionForm;
import com.kp.cms.to.exam.ConsolidatedSubjectSectionTO;

public class ConsolidatedSubjectSectionHelper 
{
	private static volatile ConsolidatedSubjectSectionHelper obj;
	
	public static ConsolidatedSubjectSectionHelper getInstance()
	{
		if(obj == null)
		{
			obj = new ConsolidatedSubjectSectionHelper();
		}
		return obj;
	}
	
	public List<ConsolidatedSubjectSectionTO> convertBOToTO(List<ConsolidatedSubjectSection> consolidatedSubjectSections) throws Exception
	{
		Iterator<ConsolidatedSubjectSection> it = consolidatedSubjectSections.iterator();
		List<ConsolidatedSubjectSectionTO> sectionNamesTO = new ArrayList<ConsolidatedSubjectSectionTO>();
		
		while(it.hasNext())
		{
			ConsolidatedSubjectSectionTO obj_TO = new ConsolidatedSubjectSectionTO();
			ConsolidatedSubjectSection obj_BO = it.next();
			obj_TO.setId(obj_BO.getId());
			obj_TO.setSectionName(obj_BO.getSectionName());
			obj_TO.setSectionOrder(String.valueOf(obj_BO.getSectionOrder()));
			obj_TO.setShowRespectiveStreams(obj_BO.getShowRespectiveStreams());
			sectionNamesTO.add(obj_TO);
		}
		return sectionNamesTO;
	}
	
	public ConsolidatedSubjectSection convertFormToBO(ConsolidatedSubjectSectionForm consolidatedSubjectSectionForm) throws Exception
	{
		ConsolidatedSubjectSection consolidatedSubjectSection = new ConsolidatedSubjectSection();
		consolidatedSubjectSection.setId(consolidatedSubjectSectionForm.getId());
		consolidatedSubjectSection.setSectionName(consolidatedSubjectSectionForm.getSectionName());
		consolidatedSubjectSection.setSectionOrder(Integer.parseInt(consolidatedSubjectSectionForm.getSectionOrder()));
		consolidatedSubjectSection.setIsActive(true);
		consolidatedSubjectSection.setShowRespectiveStreams(consolidatedSubjectSectionForm.isShowRespectiveStreams());
		
		return consolidatedSubjectSection;
	}
}
