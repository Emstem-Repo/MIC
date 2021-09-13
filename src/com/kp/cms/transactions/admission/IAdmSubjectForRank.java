package com.kp.cms.transactions.admission;

import java.util.List;

import com.kp.cms.bo.admin.AdmSubjectForRank;
import com.kp.cms.bo.admin.StudentRank;
import com.kp.cms.to.admin.AdmSubjectMarkForRankTO;

public interface IAdmSubjectForRank {

	public List<AdmSubjectForRank> getSubject();

	public AdmSubjectForRank isDuplicated(AdmSubjectForRank admsbjctbo) throws Exception;

	public boolean add(AdmSubjectForRank admsbjctbo);

	public boolean edit(AdmSubjectForRank admsbjctbo);

	public boolean delete(int id, String userId);

	public boolean reActivate(AdmSubjectForRank subj, String userId);

	public List<StudentRank> getRankDetails(int year,int courseid) throws Exception;
	
	

}
