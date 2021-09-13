package com.kp.cms.bo.exam;
// Generated Feb 8, 2011 10:52:35 AM by Hibernate Tools 3.2.6.CR1

/**
 * ExamSubjectRuleSettingsMulEvaluator generated by hbm2java
 */
public class SubjectRuleSettingsMulEvaluator implements
		java.io.Serializable {

	private Integer id;
	private SubjectRuleSettings examSubjectRuleSettings;
	private Integer evaluatorId;
	private Integer noOfEvaluations;
	private String typeOfEvaluation;
	private Character isTheoryPractical;
	private Boolean isActive;

	public SubjectRuleSettingsMulEvaluator() {
	}

	public  SubjectRuleSettingsMulEvaluator(
			SubjectRuleSettings examSubjectRuleSettings,
			Integer evaluatorId, Integer noOfEvaluations,
			String typeOfEvaluation, Character isTheoryPractical,
			Boolean isActive) {
		this.examSubjectRuleSettings = examSubjectRuleSettings;
		this.evaluatorId = evaluatorId;
		this.noOfEvaluations = noOfEvaluations;
		this.typeOfEvaluation = typeOfEvaluation;
		this.isTheoryPractical = isTheoryPractical;
		this.isActive = isActive;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SubjectRuleSettings getExamSubjectRuleSettings() {
		return this.examSubjectRuleSettings;
	}

	public void setExamSubjectRuleSettings(
			SubjectRuleSettings examSubjectRuleSettings) {
		this.examSubjectRuleSettings = examSubjectRuleSettings;
	}

	public Integer getEvaluatorId() {
		return this.evaluatorId;
	}

	public void setEvaluatorId(Integer evaluatorId) {
		this.evaluatorId = evaluatorId;
	}

	public Integer getNoOfEvaluations() {
		return this.noOfEvaluations;
	}

	public void setNoOfEvaluations(Integer noOfEvaluations) {
		this.noOfEvaluations = noOfEvaluations;
	}

	public String getTypeOfEvaluation() {
		return this.typeOfEvaluation;
	}

	public void setTypeOfEvaluation(String typeOfEvaluation) {
		this.typeOfEvaluation = typeOfEvaluation;
	}

	public Character getIsTheoryPractical() {
		return this.isTheoryPractical;
	}

	public void setIsTheoryPractical(Character isTheoryPractical) {
		this.isTheoryPractical = isTheoryPractical;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
