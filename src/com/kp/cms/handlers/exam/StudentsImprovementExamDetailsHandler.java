package com.kp.cms.handlers.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kp.cms.bo.admin.Classes;
import com.kp.cms.bo.admin.Student;
import com.kp.cms.bo.exam.ExamDefinition;
import com.kp.cms.bo.exam.ExamStudentFinalMarkDetailsBO;
import com.kp.cms.bo.exam.StudentSupplementaryImprovementApplication;
import com.kp.cms.bo.exam.StudentsImprovementExamDetailsBO;
import com.kp.cms.forms.exam.StudentsImprovementExamDetailsForm;
import com.kp.cms.transactions.exam.IStudentsImprovementExamDetailsTransaction;
import com.kp.cms.transactionsimpl.exam.StudentsImprovementExamDetailsTransactionsImpl;

public class StudentsImprovementExamDetailsHandler {
	
	private static volatile StudentsImprovementExamDetailsHandler handler = null;
	private static final Log log = LogFactory.getLog(StudentsImprovementExamDetailsHandler.class);
	private StudentsImprovementExamDetailsHandler() {
		
	}
	public static StudentsImprovementExamDetailsHandler getInstance() {
		if (handler == null) {
			handler = new StudentsImprovementExamDetailsHandler();
		}
		return handler;
	}
	
	public Map<Integer, String> loadClassByExamNameAndYear(StudentsImprovementExamDetailsForm actionForm) throws Exception
	{
		IStudentsImprovementExamDetailsTransaction applicationTxn=StudentsImprovementExamDetailsTransactionsImpl.getInstance();
		List applicationBOList =applicationTxn.loadClassByExamNameAndYear(actionForm);
		Map<Integer, String> classMap=new HashMap<Integer, String>();
		if(applicationBOList!=null )
		{
		Iterator iterator=applicationBOList.iterator();
		while(iterator.hasNext())
		{
		 	Object[] objects=(Object[]) iterator.next();
		 	if(objects[0]!=null && objects[1]!=null && objects[2]!=null )
		 	{
			classMap.put(Integer.parseInt(objects[0].toString()), objects[1].toString()+"("+objects[2]+")");
		 	}
		}
		}
		return classMap;
		
	}
	public Map<String,List<ExamStudentFinalMarkDetailsBO>> getStudentsImprovementExamDetails(StudentsImprovementExamDetailsForm form) throws Exception{
	    IStudentsImprovementExamDetailsTransaction Txn = StudentsImprovementExamDetailsTransactionsImpl.getInstance();
	    List<ExamStudentFinalMarkDetailsBO> studentFinalMarkDetailsBOList = Txn.getStudentsImpExamDetails(form);
	    List<Integer> subjectList = new ArrayList();
	    Map<String, List<ExamStudentFinalMarkDetailsBO>> stuSuppMarksMap = new LinkedHashMap();
	    Map<String, ExamStudentFinalMarkDetailsBO> marksMap = new LinkedHashMap();
	    Map<String, List<ExamStudentFinalMarkDetailsBO>> stuImpFinalExamMarksMap = new LinkedHashMap();
	    
	    ExamStudentFinalMarkDetailsBO boFromMap = null;
	    Iterator itr = studentFinalMarkDetailsBOList.iterator();
	    if (studentFinalMarkDetailsBOList.size() > 0) {
	      try {
	        while (itr.hasNext()) {
	          ExamStudentFinalMarkDetailsBO bo = (ExamStudentFinalMarkDetailsBO)itr.next();
	          if (stuSuppMarksMap.containsKey(bo.getStudentId() + "-" + bo.getSubjectId())) {
	            boFromMap = null;
	            double theoryMarks = 0.0D;
	            double practMarks = 0.0D;
	            int stuId = bo.getStudentId();
	            if ((bo.getStudentTheoryMarks() != null) && 
	              (bo.getStudentPracticalMarks() == null)) {
	              if ((bo.getStudentTheoryMarks() != null) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("C"))) {
	                List<ExamStudentFinalMarkDetailsBO> marksList = (List)stuSuppMarksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	                boFromMap = (ExamStudentFinalMarkDetailsBO)marksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	                if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentPracticalMarks() == null)) {
	                  if (Double.parseDouble(bo.getStudentTheoryMarks()) > Double.parseDouble(boFromMap.getStudentTheoryMarks())) {
	                    bo.setImpFlag(true);
	                    marksList.add(bo);
	                    stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	                    marksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), bo);
	                  }
	                  else {
	                    marksList.remove(boFromMap);
	                    boFromMap.setImpFlag(true);
	                    marksList.add(boFromMap);
	                    marksList.add(bo);
	                    
	                    stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                  
	                }
	                else
	                {
	                  bo.setImpFlag(true);
	                  marksList.add(bo);
	                  
	                  stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                  marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                }
	                
	              }
	              else
	              {
	                boFromMap = (ExamStudentFinalMarkDetailsBO)marksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	                List<ExamStudentFinalMarkDetailsBO> marksList = (List)stuSuppMarksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	                if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentPracticalMarks() == null))
	                {
	                  marksList.remove(bo);
	                  boFromMap.setImpFlag(true);
	                  marksList.add(bo);
	                  
	                  stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                  marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);

	                }
	                else
	                {
	                  marksList.remove(boFromMap);
	                  boFromMap.setImpFlag(false);
	                  marksList.add(bo);
	                  stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                  marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                }
	                
	              }
	              

	            }
	            else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() == null)) {
	              List<ExamStudentFinalMarkDetailsBO> marksList = (List)stuSuppMarksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	              boFromMap = (ExamStudentFinalMarkDetailsBO)marksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	              marksList.add(bo);
	              if ((!bo.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                (!bo.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("C")))
	              {

	                if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP"))  && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentPracticalMarks() == null)) {
	                  if (Double.parseDouble(bo.getStudentPracticalMarks()) > Double.parseDouble(boFromMap.getStudentPracticalMarks())) {
	                    bo.setImpFlag(true);
	                    marksList.add(bo);
	                    stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                  else {
	                    marksList.remove(boFromMap);
	                    boFromMap.setImpFlag(true);
	                    marksList.add(boFromMap);
	                    marksList.add(bo);
	                    
	                    stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                }
	                else
	                {
	                  marksList.remove(boFromMap);
	                  boFromMap.setImpFlag(false);
	                  marksList.add(bo);
	                  stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                  marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);

	                }
	                

	              }
	              

	            }
	            else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() != null)) {
	              List<ExamStudentFinalMarkDetailsBO> marksList = (List)stuSuppMarksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	              if ((bo.getStudentTheoryMarks() != null) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("C")) )
	              {
	                boFromMap = (ExamStudentFinalMarkDetailsBO)marksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	                if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) )
	                {
	                  if (Double.parseDouble(bo.getStudentTheoryMarks()) > Double.parseDouble(boFromMap.getStudentTheoryMarks())) {
	                    bo.setImpFlag(true);
	                    marksList.add(bo);
	                    stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	                    marksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), bo);
	                  }
	                  else {
	                    marksList.remove(boFromMap);
	                    boFromMap.setImpFlag(true);
	                    marksList.add(boFromMap);
	                    marksList.add(bo);
	                    
	                    stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                  
	                }
	              }
	              else if ((bo.getStudentPracticalMarks() != null) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                (!bo.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("C"))  )
	              {

	                if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentPracticalMarks() == null)) {
	                  if (Double.parseDouble(bo.getStudentPracticalMarks()) > Double.parseDouble(boFromMap.getStudentPracticalMarks())) {
	                    bo.setImpFlag(true);
	                    marksList.add(bo);
	                    stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                  else {
	                    marksList.remove(boFromMap);
	                    boFromMap.setImpFlag(true);
	                    marksList.add(boFromMap);
	                    marksList.add(bo);
	                    
	                    stuSuppMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), marksList);
	                    marksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), boFromMap);
	                  }
	                  
	                }
	              }
	            }
	          }
	          else
	          {
	            if ((bo.getStudentId() == 737) && (bo.getSubjectId() == 290)) {
	              System.out.println("");
	            }
	            List<ExamStudentFinalMarkDetailsBO> marksList = new ArrayList();
	            if ((bo.getStudentTheoryMarks() != null) && (bo.getStudentPracticalMarks() == null)) {
	              marksList.add(bo);
	              marksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), bo);
	              stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	            }
	            else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() == null)) {
	              marksList.add(bo);
	              marksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), bo);
	              stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);



	            }
	            else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() != null)) {
	              marksList.add(bo);
	              marksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), bo);
	              stuSuppMarksMap.put(bo.getStudentId() + "-" + bo.getSubjectId(), marksList);
	            }
	          }
	        }
	        




	        List<ExamStudentFinalMarkDetailsBO> studentRegularExamMarkDetailsBOList = Txn.getStudentsRegularExamMarks(form);
	        itr = studentRegularExamMarkDetailsBOList.iterator();
	        
	        while (itr.hasNext()) {
	          ExamStudentFinalMarkDetailsBO bo = (ExamStudentFinalMarkDetailsBO)itr.next();
	          if (stuSuppMarksMap.containsKey(bo.getStudentId() + "-" + bo.getSubjectId())) {
	            List<ExamStudentFinalMarkDetailsBO> supplExamList = new ArrayList();
	            List<ExamStudentFinalMarkDetailsBO> examStudentFinalMarkDetailsBOList = (List)stuSuppMarksMap.get(bo.getStudentId() + "-" + bo.getSubjectId());
	            
	            Iterator supplMarksListItr = examStudentFinalMarkDetailsBOList.iterator();
	            while (supplMarksListItr.hasNext()) {
	              boFromMap = (ExamStudentFinalMarkDetailsBO)supplMarksListItr.next();
	              if ((bo.getStudentTheoryMarks() != null) && (bo.getStudentPracticalMarks() == null)) {
	                if ((bo.getStudentTheoryMarks() != null) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                  (bo.getStudentPracticalMarks() == null))
	                {
	                  if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                    (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                    (boFromMap.getStudentPracticalMarks() == null)) {
	                    if ((bo.getStudentTheoryMarks() != null) && (Double.parseDouble(boFromMap.getStudentTheoryMarks()) > Double.parseDouble(bo.getStudentTheoryMarks()))) {
	                      boFromMap.setImpFlag(true);
	                      supplExamList.add(boFromMap);
	                      stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                    }
	                    else
	                    {
	                      boFromMap.setImpFlag(false);
	                      supplExamList.add(boFromMap);
	                      stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                    }
	                  }
	                  else {
	                    boFromMap.setImpFlag(false);
	                    supplExamList.add(boFromMap);
	                    stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);

	                  }
	                  

	                }
	                else if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentPracticalMarks() == null)) {
	                  boFromMap.setImpFlag(true);
	                  supplExamList.add(boFromMap);
	                  stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                }
	                else
	                {
	                  boFromMap.setImpFlag(false);
	                  supplExamList.add(boFromMap);
	                  stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                }
	                

	              }
	              else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() == null)) {
	                if ((bo.getStudentPracticalMarks() != null) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                  (bo.getStudentTheoryMarks() == null))
	                {
	                  if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                    (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                    (boFromMap.getStudentPracticalMarks() == null))
	                  {
	                    if ((bo.getStudentPracticalMarks() != null) && (Double.parseDouble(boFromMap.getStudentPracticalMarks()) > Double.parseDouble(bo.getStudentPracticalMarks()))) {
	                      boFromMap.setImpFlag(true);
	                      supplExamList.add(boFromMap);
	                      stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                    }
	                    else
	                    {
	                      boFromMap.setImpFlag(false);
	                      supplExamList.add(boFromMap);
	                      stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                    }
	                  }
	                  else {
	                    boFromMap.setImpFlag(false);
	                    supplExamList.add(boFromMap);
	                    stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                  }
	                  
	                }
	                else if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                  (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                  (boFromMap.getStudentTheoryMarks() == null)) {
	                  boFromMap.setImpFlag(true);
	                  supplExamList.add(boFromMap);
	                  stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                }
	                else
	                {
	                  boFromMap.setImpFlag(false);
	                  supplExamList.add(boFromMap);
	                  stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);

	                }
	                

	              }
	              else if ((bo.getStudentPracticalMarks() != null) && (bo.getStudentTheoryMarks() != null)) {
	                if (bo.getStudentTheoryMarks() != null) {
	                  if ((bo.getStudentTheoryMarks() != null) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                    (!bo.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!bo.getStudentTheoryMarks().equalsIgnoreCase("C")) )
	                  {
	                    if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && 
	                      (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                      (boFromMap.getStudentPracticalMarks() == null)) {
	                      if ((bo.getStudentTheoryMarks() != null) && (Double.parseDouble(boFromMap.getStudentTheoryMarks()) > Double.parseDouble(bo.getStudentTheoryMarks()))) {
	                        boFromMap.setImpFlag(true);
	                        supplExamList.add(boFromMap);
	                        stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                      }
	                      else
	                      {
	                        boFromMap.setImpFlag(false);
	                        supplExamList.add(boFromMap);
	                        stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                      }
	                    }
	                    else {
	                      boFromMap.setImpFlag(false);
	                      supplExamList.add(boFromMap);
	                      stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	                    }
	                    
	                  }
	                  else if ((boFromMap.getStudentTheoryMarks() != null) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("Ab")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("C")) && 
	                    (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentTheoryMarks().equalsIgnoreCase("MP")) && 
	                    (boFromMap.getStudentPracticalMarks() == null)) {
	                    boFromMap.setImpFlag(true);
	                    supplExamList.add(boFromMap);
	                  }
	                  else
	                  {
	                    boFromMap.setImpFlag(false);
	                    supplExamList.add(boFromMap);
	                  }
	                }
	                
	                if (bo.getStudentPracticalMarks() != null) {
	                  if ((bo.getStudentPracticalMarks() != null) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!bo.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                    (!bo.getStudentPracticalMarks().equalsIgnoreCase("MP")))
	                  {
	                    if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                      (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                      (boFromMap.getStudentPracticalMarks() == null))
	                    {
	                      if (Double.parseDouble(boFromMap.getStudentPracticalMarks()) > Double.parseDouble(bo.getStudentPracticalMarks())) {
	                        boFromMap.setImpFlag(true);
	                        supplExamList.add(boFromMap);
	                      }
	                    }
	                    else {
	                      boFromMap.setImpFlag(false);
	                      supplExamList.add(boFromMap);
	                    }
	                    

	                  }
	                  else if ((boFromMap.getStudentPracticalMarks() != null) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("Ab")) && 
	                    (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("nr")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("MP")) && (!boFromMap.getStudentPracticalMarks().equalsIgnoreCase("C")) && 
	                    (boFromMap.getStudentPracticalMarks() == null)) {
	                    boFromMap.setImpFlag(true);
	                    supplExamList.add(boFromMap);
	                  }
	                  else
	                  {
	                    boFromMap.setImpFlag(false);
	                    supplExamList.add(boFromMap);
	                  }
	                }
	                
	                stuImpFinalExamMarksMap.put(boFromMap.getStudentId() + "-" + boFromMap.getSubjectId(), supplExamList);
	              }
	              
	            }
	            
	          }
	        }
	      }
	      catch (Exception e)
	      {
	        e.printStackTrace();
	      }
	    }
	    


	    return stuImpFinalExamMarksMap;
	  }
	
	public List<StudentsImprovementExamDetailsBO>  saveStudentsImprovementExamMarksFlag(StudentsImprovementExamDetailsForm form,Map<String,List<ExamStudentFinalMarkDetailsBO>> stuImpExamMarksMap) throws Exception{

		Iterator entries = stuImpExamMarksMap.entrySet().iterator();
		List<StudentsImprovementExamDetailsBO> boList = new ArrayList<StudentsImprovementExamDetailsBO>();
		StudentsImprovementExamDetailsBO studentsImprovementExamDetailsBO =null;
		Classes classes = new Classes();
		
		List<StudentsImprovementExamDetailsBO> innerBoList = new ArrayList<StudentsImprovementExamDetailsBO>();
		classes.setId(Integer.parseInt(form.getClassCodeIdsFrom()));
		ExamDefinition def = new ExamDefinition();
		ExamStudentFinalMarkDetailsBO bo=null;
		
		while (entries.hasNext()) {
			Entry thisEntry = (Entry) entries.next();
			Object key = thisEntry.getKey();
			//bo = new ExamStudentFinalMarkDetailsBO();
			
			innerBoList  = (List<StudentsImprovementExamDetailsBO>) thisEntry.getValue();
			Iterator listIter = innerBoList.iterator();
			while(listIter.hasNext()){
				studentsImprovementExamDetailsBO = new StudentsImprovementExamDetailsBO();
				bo=(ExamStudentFinalMarkDetailsBO) listIter.next();
				studentsImprovementExamDetailsBO.setClasses(classes);
				Student student = new Student();
				student.setId(bo.getStudentId());
				def = new ExamDefinition();
				studentsImprovementExamDetailsBO.setStudent(student);
				def.setId(bo.getExamId());
				studentsImprovementExamDetailsBO.setExamDef(def);
				studentsImprovementExamDetailsBO.setCreatedBy(form.getUserId());
				studentsImprovementExamDetailsBO.setCreatedDate(new Date());
				studentsImprovementExamDetailsBO.setImprovementFlag(bo.getImpFlag());
				studentsImprovementExamDetailsBO.setSubjectId(String.valueOf(bo.getSubjectId()));
				boList.add(studentsImprovementExamDetailsBO);
			}
		}
		return boList;
	}
	

}
