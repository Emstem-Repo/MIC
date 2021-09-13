package com.kp.cms.helpers.studentExtentionActivity;

import org.apache.log4j.Logger;

public class StudentInstructionHelper {
	
	private static final Logger log = Logger.getLogger(StudentInstructionHelper.class);
	public static volatile StudentInstructionHelper feedbackInstructionsHelper = null;
	public static StudentInstructionHelper getInstance(){
		if(feedbackInstructionsHelper ==null){
			feedbackInstructionsHelper = new StudentInstructionHelper();
			return feedbackInstructionsHelper;
		}
		return feedbackInstructionsHelper;
	}
	

}
