package com.kp.cms.utilities;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ibm.icu.util.Calendar;
import com.ibm.wsdl.util.StringUtils;
import com.itextpdf.text.log.SysoLogger;
import com.kp.cms.forms.attendance.NewAttendanceSmsForm;
import com.kp.cms.handlers.ajax.CommonAjaxHandler;
import com.kp.cms.handlers.attendance.MobileSMSCriteriaHandler;
import com.kp.cms.handlers.attendance.NewAttendanceSmsHandler;
import com.kp.cms.helpers.admission.StudentEditHelper;

public class SendingNewSMSJob implements Job {
	private static final Log log = LogFactory.getLog(SendingSmsJob.class);	
	private static final String FROM_DATEFORMAT = "dd/MM/yyyy";
	private static final String SQL_DATEFORMAT = "dd-MMM-yyyy";	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Entered into SendingNewSMSJob"+Calendar.getInstance().getTimeInMillis());

		try {
			Date attToday=new Date();

			String todatDate=CommonUtil.ConvertStringToDateFormat(
					CommonUtil.getStringDate(attToday),
					SendingNewSMSJob.SQL_DATEFORMAT,
					SendingNewSMSJob.FROM_DATEFORMAT);

			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);

			int year=CurrentAcademicYear.getInstance().getAcademicyear();
			if(year!=0){
				currentYear=year;
			}
			int smsTimehours=calendar.getTime().getHours();
			int smsMinitue=calendar.getTime().getMinutes();
			String[] smsClass=MobileSMSCriteriaHandler.getInstance().getSmsClassList(currentYear,smsTimehours,smsMinitue);

			if(smsClass!=null && !smsClass.equals(null))
			{
				
					NewAttendanceSmsForm newAttendanceSmsForm = new NewAttendanceSmsForm();
					newAttendanceSmsForm.setAttendancedate(todatDate);
					String syear=""+year;
					newAttendanceSmsForm.setYear(syear);


					newAttendanceSmsForm.setClasses(smsClass);
					NewAttendanceSmsHandler.getInstance().getAbsentees(newAttendanceSmsForm);
					NewAttendanceSmsHandler.getInstance().getStudents(newAttendanceSmsForm);
					boolean isMsgSent=false;
					if(newAttendanceSmsForm.getAbsenteesList()!=null && newAttendanceSmsForm.getAbsenteesList().size()!=0 && smsClass!=null && smsClass.length!=0 )
					{
						isMsgSent=NewAttendanceSmsHandler.getInstance().sendSMS(newAttendanceSmsForm);
						if(isMsgSent)
						{
							System.out.println("Message sent sucess");
							log.info("Message sent sucess");
						}
						else
						{
							System.out.println("Message Sent Fail");
							log.error("Message Sent Fail");
						}
					}
					else
					{
						log.error("Class List is Null ");
					}
			}

		} catch (Exception e) {
			log.error("Error in SendingNewSMSJob"+Calendar.getInstance().getTimeInMillis());
			e.printStackTrace();
		}
		log.info("Exit from SendingNewSMSJob"+Calendar.getInstance().getTimeInMillis());
	}
}
