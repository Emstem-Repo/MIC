package com.kp.cms.bo.exam;

/**
 * Dec 25, 2009 Created By 9Elements Team
 */
import java.math.BigDecimal;
import java.util.Date;

public class ExamSettingsBO extends ExamGenBO {

	private String absentCodeMarkEntry;
	private String notProcedCodeMarkEntry;
	private String securedMarkEntryBy;
	private BigDecimal maxAllwdDiffPrcntgMultiEvaluator;
	private BigDecimal gradePointForFail;
	private String gradeForFail;
	private String malpracticeCodeMarkEntry;
	private String cancelCodeMarkEntry;
	
	

	public ExamSettingsBO() {
		super();
	}

	public ExamSettingsBO(String absentCodeMarkEntry,
			String notProcedCodeMarkEntry, String securedMarkEntryBy,
			BigDecimal maxAllwdDiffPrcntgMultiEvaluator,
			BigDecimal gradePointForFail, String gradeForFail, String userId, String malpracticeCodeMarkEntry,String cancelCodeMarkEntry ) {
		super();
		this.absentCodeMarkEntry = absentCodeMarkEntry;
		this.gradeForFail = gradeForFail;
		this.gradePointForFail = gradePointForFail;
		this.maxAllwdDiffPrcntgMultiEvaluator = maxAllwdDiffPrcntgMultiEvaluator;
		this.notProcedCodeMarkEntry = notProcedCodeMarkEntry;
		this.securedMarkEntryBy = securedMarkEntryBy;
		this.createdBy = userId;
		this.createdDate = new Date();
		this.modifiedBy = userId;
		this.lastModifiedDate = new Date();
		this.isActive = true;
		this.malpracticeCodeMarkEntry=malpracticeCodeMarkEntry;
		this.cancelCodeMarkEntry=cancelCodeMarkEntry;
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

	public BigDecimal getMaxAllwdDiffPrcntgMultiEvaluator() {
		return maxAllwdDiffPrcntgMultiEvaluator;
	}

	public void setMaxAllwdDiffPrcntgMultiEvaluator(
			BigDecimal maxAllwdDiffPrcntgMultiEvaluator) {
		this.maxAllwdDiffPrcntgMultiEvaluator = maxAllwdDiffPrcntgMultiEvaluator;
	}

	public BigDecimal getGradePointForFail() {
		return gradePointForFail;
	}

	public void setGradePointForFail(BigDecimal gradePointForFail) {
		this.gradePointForFail = gradePointForFail;
	}

	public String getGradeForFail() {
		return gradeForFail;
	}

	public void setGradeForFail(String gradeForFail) {
		this.gradeForFail = gradeForFail;
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
