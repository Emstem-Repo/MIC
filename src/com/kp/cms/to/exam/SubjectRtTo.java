/**
 *Jan 1, 2010
 * developed by 9Elements Team
 */
package com.kp.cms.to.exam;

public class SubjectRtTo {
	int exaamId;
	double marks;
	double retestMarks;
	double intEntMaxMarks;
	double intMaxMarks;
	double theoryIntEntryMaxMarksTotal;
	double theoryIntMaxMarksTotal;
	double percentage;
	boolean gracing;
	boolean retest;
	int intExamType;
	
	
	public int getExaamId() {
		return exaamId;
	}
	public void setExaamId(int exaamId) {
		this.exaamId = exaamId;
	}
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public double getIntEntMaxMarks() {
		return intEntMaxMarks;
	}
	public void setIntEntMaxMarks(double intEntMaxMarks) {
		this.intEntMaxMarks = intEntMaxMarks;
	}
	public double getIntMaxMarks() {
		return intMaxMarks;
	}
	public void setIntMaxMarks(double intMaxMarks) {
		this.intMaxMarks = intMaxMarks;
	}
	public double getTheoryIntEntryMaxMarksTotal() {
		return theoryIntEntryMaxMarksTotal;
	}
	public void setTheoryIntEntryMaxMarksTotal(double theoryIntEntryMaxMarksTotal) {
		this.theoryIntEntryMaxMarksTotal = theoryIntEntryMaxMarksTotal;
	}
	public double getTheoryIntMaxMarksTotal() {
		return theoryIntMaxMarksTotal;
	}
	public void setTheoryIntMaxMarksTotal(double theoryIntMaxMarksTotal) {
		this.theoryIntMaxMarksTotal = theoryIntMaxMarksTotal;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	public boolean isGracing() {
		return gracing;
	}
	public void setGracing(boolean gracing) {
		this.gracing = gracing;
	}
	public boolean isRetest() {
		return retest;
	}
	public void setRetest(boolean retest) {
		this.retest = retest;
	}
	public int getIntExamType() {
		return intExamType;
	}
	public void setIntExamType(int intExamType) {
		this.intExamType = intExamType;
	}
	public double getRetestMarks() {
		return retestMarks;
	}
	public void setRetestMarks(double retestMarks) {
		this.retestMarks = retestMarks;
	}
	
	
}
