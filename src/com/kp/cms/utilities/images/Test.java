package com.kp.cms.utilities.images;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test 
{
	public static void main(String args[]) 
	{
		Test test = new Test();
		List<Integer> preferences = new ArrayList<Integer>();
		for(int i = 1; i <= 5; i++)
		{
			preferences.add(i);
		}
		Map<Integer, StudentRank> studentMap = new HashMap<Integer, StudentRank>();
		Map<Integer, Map<Integer, StudentRank>> preferenceMap = new HashMap<Integer, Map<Integer, StudentRank>>();
		
		//Map<Integer, Integer> rankMap = new HashMap<Integer, Integer>();
		List<StudentRank> tmpList = null;
		List<StudentRank> studentList = new ArrayList<StudentRank>();
		Integer maxRank = null;
		for(Integer preference : preferences)
		{
//			studentList = select StudentRank objects using preference and rank (asc order);
//			create tempList<String> and addAll the StudentRank objects to that.
			tmpList = new ArrayList<StudentRank>();
			tmpList.addAll(studentList);
			for(StudentRank studentRank : tmpList)
			{
				studentMap = null;
				if(studentRank.isOpenCategory())
				{
					if(preferenceMap.containsKey(preference))
					{
						studentMap = preferenceMap.get(preference);
//						maxRank = rankMap.get(preference);
					}
					else
					{
						studentMap = new HashMap<Integer, StudentRank>();
						preferenceMap.put(preference, studentMap);
//						rankMap.put(preference, studentRank.getRank());
					}
					
					studentMap.put(studentRank.getStudentId(), studentRank);
					studentList.remove(studentRank);
				}
			}
			
			for(StudentRank studentRank : studentList)
			{
				if(studentRank.isCasteCategory())// if Caste category..
				{
					if(preferenceMap.containsKey(preference))
					{
						studentMap = preferenceMap.get(preference);
					}
					else
					{
						studentMap = new HashMap<Integer, StudentRank>();
					}
					
					studentMap.put(studentRank.getStudentId(), studentRank);
					preferenceMap.put(preference, studentMap);
				}
			}
		}
		
		StudentRank sRank = null;
		for(Integer key : preferenceMap.keySet())
		{
			studentMap = preferenceMap.get(key);
			for(StudentRank studentRank : studentMap.values())
			{
				sRank = test.validatePreference(key, maxRank, studentRank, preferenceMap);
				if(sRank != null)
				{
					//need to add to the separate list.. which need to be processed for allotment as per the rank on course.. 
				}
			}
		}
	}
	
	private StudentRank validatePreference(Integer preference, Integer maxRank, StudentRank studentRank, Map<Integer, Map<Integer, StudentRank>> preferenceMap)
	{
		Map<Integer, StudentRank> studentMap = null;

		for(Integer key : preferenceMap.keySet())
		{
			if(preference == key)
			{
				continue;
			}
			
			studentMap = preferenceMap.get(key);
			for (StudentRank sRank : studentMap.values())
			{
				if(sRank.getIndexMarks().getPreferenceNo() == key && sRank.getCourseId() == studentRank.getCourseId() 
						&& sRank.getRank() < maxRank && sRank.getRank() < studentRank.getRank())
				{
					if(!isAlloted(sRank, preferenceMap))
					{
						return sRank;
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean isAlloted(StudentRank studentRank, Map<Integer, Map<Integer, StudentRank>> preferenceMap)
	{
		Map<Integer, StudentRank> studentMap = null;
		
		for(int i = studentRank.getIndexMarks().getPreferenceNo(); i > 0; i--)
		{
			studentMap = preferenceMap.get(i);
			for(StudentRank sRank : studentMap.values())
			{
				if(sRank.getStudentId() == studentRank.getStudentId())
				{
					//isAlloted;
					return true;
				}
			}
		}
		return false;
	}
}

class StudentRank
{
	private Integer studentId;
	private Integer courseId;
	private Integer rank;
	private IndexMarks indexMarks;
	private Boolean isCasteCategory;
	private Boolean isOpenCategory;
	private Integer preferenceNo;
	
	
	public Integer getPreferenceNo() {
		return preferenceNo;
	}

	public void setPreferenceNo(Integer preferenceNo) {
		this.preferenceNo = preferenceNo;
	}

	public Integer getStudentId() 
	{
		return studentId;
	}
	
	public void setStudentId(Integer studentId) 
	{
		this.studentId = studentId;
	}
	
	public Integer getCourseId() 
	{
		return courseId;
	}
	
	public void setCourseId(Integer courseId) 
	{
		this.courseId = courseId;
	}
	
	public IndexMarks getIndexMarks() 
	{
		return indexMarks;
	}
	
	public void setIndexMarks(IndexMarks indexMarks) 
	{
		this.indexMarks = indexMarks;
	}

	public Integer getRank() 
	{
		return rank;
	}
	
	public void setRank(Integer rank) 
	{
		this.rank = rank;
	}

	public Boolean isCasteCategory() {
		return isCasteCategory;
	}

	public void setIsCasteCategory(Boolean isCasteCategory) {
		this.isCasteCategory = isCasteCategory;
	}

	public Boolean isOpenCategory() {
		return isOpenCategory;
	}

	public void setIsOpenCategory(Boolean isOpenCategory) {
		this.isOpenCategory = isOpenCategory;
	}
	
}

class IndexMarks
{
	private Integer studentId;
	private Integer indexMarks;
	private Integer courseId;
	private Integer preferenceNo;
	
	public Integer getStudentId() 
	{
		return studentId;
	}
	
	public void setStudentId(Integer studentId) 
	{
		this.studentId = studentId;
	}
	
	public Integer getIndexMarks() 
	{
		return indexMarks;
	}
	
	public void setIndexMarks(Integer indexMarks) 
	{
		this.indexMarks = indexMarks;
	}
	
	public Integer getCourseId() 
	{
		return courseId;
	}
	
	public void setCourseId(Integer courseId) 
	{
		this.courseId = courseId;
	}
	
	public Integer getPreferenceNo() 
	{
		return preferenceNo;
	}
	
	public void setPreferenceNo(Integer preferenceNo) 
	{
		this.preferenceNo = preferenceNo;
	}
}
