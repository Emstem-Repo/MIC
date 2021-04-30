package com.kp.cms.to.exam;

import java.io.Serializable;

/**
 * Dec 25, 2009 Created By 9Elements
 */
public class ExamSettingsTO implements Serializable,Comparable<ExamSettingsTO>{

	private int id = 0;
	private String organisationName = "";
	private String absentCodeMarkEntry = "";
	private String notProcedCodeMarkEntry = "";
	private String securedMarkEntryBy = "";
	private String malpracticeCodeMarkEntry = "";
	private String cancelCodeMarkEntry = "";
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getAbsentCodeMarkEntry() {
		return absentCodeMarkEntry;
	}

	public void setAbsentCodeMarkEntry(String absentCodeMarkEntry) {
		this.absentCodeMarkEntry = absentCodeMarkEntry;
	}

	public String getNotProcedCodeMarkEntry() {
		return notProcedCodeMarkEntry;
	}

	public void setNotProcedCodeMarkEntry(String notProcedCodeMarkEntry) {
		this.notProcedCodeMarkEntry = notProcedCodeMarkEntry;
	}

	public String getSecuredMarkEntryBy() {
		return securedMarkEntryBy;
	}

	public void setSecuredMarkEntryBy(String securedMarkEntryBy) {
		this.securedMarkEntryBy = securedMarkEntryBy;
	}

	@Override
	public int compareTo(ExamSettingsTO arg0) {
		if(arg0!=null && this!=null && arg0.getOrganisationName()!=null
				 && this.getOrganisationName()!=null){
			return this.getOrganisationName().compareTo(arg0.getOrganisationName());
		}else
		return 0;
	}

	public String getMalpracticeCodeMarkEntry() {
		return malpracticeCodeMarkEntry;
	}

	public void setMalpracticeCodeMarkEntry(String malpracticeCodeMarkEntry) {
		this.malpracticeCodeMarkEntry = malpracticeCodeMarkEntry;
	}
	public String getCancelCodeMarkEntry() {
		return cancelCodeMarkEntry;
	}

	public void setCancelCodeMarkEntry(String cancelCodeMarkEntry) {
		this.cancelCodeMarkEntry = cancelCodeMarkEntry;
	}
}
