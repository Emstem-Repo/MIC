package com.kp.cms.handlers.employee;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;

import com.kp.cms.bo.admin.Country;
import com.kp.cms.bo.admin.Department;
import com.kp.cms.bo.admin.Designation;
import com.kp.cms.bo.admin.EmpOnlineResume;
import com.kp.cms.bo.admin.EmpQualificationLevel;
import com.kp.cms.bo.admin.GroupTemplate;
import com.kp.cms.bo.admin.Nationality;
import com.kp.cms.bo.admin.State;
import com.kp.cms.bo.admin.Users;
import com.kp.cms.bo.employee.GuestEducationalDetails;
import com.kp.cms.bo.employee.GuestFaculty;
import com.kp.cms.bo.employee.GuestPreviousExperience;
import com.kp.cms.constants.CMSConstants;
import com.kp.cms.forms.employee.GuestFacultyInfoForm;
import com.kp.cms.handlers.admin.TemplateHandler;
import com.kp.cms.helpers.attendance.AttendanceEntryHelper;
import com.kp.cms.helpers.employee.EmpEventVacationHelper;
import com.kp.cms.helpers.employee.GuestFacultyInfoHelper;
import com.kp.cms.to.employee.EmpQualificationLevelTo;
import com.kp.cms.to.employee.GuestEducationalDetailsTO;
import com.kp.cms.to.employee.GuestFacultyInfoTo;
import com.kp.cms.to.employee.GuestFacultyTO;
import com.kp.cms.to.employee.GuestPreviousExperienceTO;
import com.kp.cms.transactions.employee.IGuestFacultyInfoTransaction;
import com.kp.cms.transactions.exam.IDownloadEmployeeResumeTransaction;
import com.kp.cms.transactionsimpl.employee.DownloadEmployeeResumeTransactionImpl;
import com.kp.cms.transactionsimpl.employee.EmpEventVacationImpl;
import com.kp.cms.transactionsimpl.employee.GuestFacultyInfoImpl;
import com.kp.cms.utilities.CommonUtil;
import com.kp.cms.utilities.RepeatMidSemReminderSmsMail;
import com.kp.cms.utilities.jms.MailTO;

public class AddExaminerHandler {
	
	private static final Log log = LogFactory.getLog(AddExaminerHandler.class);
	IGuestFacultyInfoTransaction empTransaction=GuestFacultyInfoImpl.getInstance();
	
	private static volatile AddExaminerHandler instance=null;
	
	/**
	 * 
	 */
	private AddExaminerHandler(){
		
	}
	
	/**
	 * @return
	 */
	public static AddExaminerHandler getInstance(){
		log.info("Start getInstance of GuestFacultyInfoHandler");
		if(instance==null){
			instance=new AddExaminerHandler();
		}
		log.info("End getInstance of GuestFacultyInfoHandler");
		return instance;
	}

	public boolean saveExaminers(GuestFacultyInfoForm objform) throws Exception {
		GuestFaculty bo=convertTobo(objform);
		
		return GuestFacultyInfoImpl.getInstance().SaveEditEmpDetails(bo);
	}

	private GuestFaculty convertTobo(GuestFacultyInfoForm objform) {
		GuestFaculty guestFaculty = new GuestFaculty();
		if (objform.getName()!=null && !objform.getName().isEmpty()) {
			guestFaculty.setFirstName(objform.getName());
		}
		if (objform.getMobileNo1()!=null || !objform.getMobileNo1().isEmpty()) {
			guestFaculty.setCurrentAddressMobile1(objform.getMobileNo1());
		}
		if (objform.getEmail()!=null || !objform.getEmail().isEmpty()) {
			guestFaculty.setEmail(objform.getEmail());
		}
		if (objform.getRetired()!=null || !objform.getRetired().isEmpty()) {
			guestFaculty.setRetired(objform.getRetired());
		}
		
		if (objform.getDepartmentId()!=null || !objform.getDepartmentId().isEmpty()) {
			Department dp=new Department();
			dp.setId(Integer.parseInt(objform.getDepartmentId()));
			guestFaculty.setDepartment(dp);
		}
		if (objform.getCollegeName()!=null || !objform.getCollegeName().isEmpty()) {
			guestFaculty.setCollegeName(objform.getCollegeName());
		}
		if (objform.getExpYears()!=null || !objform.getExpYears().isEmpty()) {
			guestFaculty.setTotalExpYear(objform.getExpYears());
		}
		
		
		if (objform.getCurrentAddressLine1() != null
				&& !objform.getCurrentAddressLine1()
						.isEmpty()) {
			guestFaculty.setCommunicationAddressLine1(objform
					.getCurrentAddressLine1());
		}

		if (objform.getCurrentAddressLine2() != null
				&& !objform.getCurrentAddressLine2()
						.isEmpty()) {
			guestFaculty.setCommunicationAddressLine2(objform
					.getCurrentAddressLine2());
		}

		if (objform.getCurrentZipCode() != null
				&& !objform.getCurrentZipCode().isEmpty()) {
			guestFaculty.setCommunicationAddressZip(objform
					.getCurrentZipCode());
		}

		if (objform.getCurrentCountryId() != null
				&& !objform.getCurrentCountryId()
						.isEmpty()) {
			Country currentCountry = new Country();
			currentCountry.setId(Integer.parseInt(objform
					.getCurrentCountryId()));
			guestFaculty
					.setCountryByCommunicationAddressCountryId(currentCountry);
		}

		if (objform.getCurrentState() != null
				&& !objform.getCurrentState().isEmpty()) {
			if (objform.getCurrentState()
					.equalsIgnoreCase("other")) {
				if (objform.getOtherCurrentState() != null
						&& !objform.getOtherCurrentState()
								.isEmpty()) {
					guestFaculty
							.setCommunicationAddressStateOthers(objform
									.getOtherCurrentState());
				}
			} else {
				State currentState = new State();
				currentState.setId(Integer
						.parseInt(objform
								.getCurrentState()));
				guestFaculty
						.setStateByCommunicationAddressStateId(currentState);
			}
		}

		if (objform.getCurrentCity() != null
				&& !objform.getCurrentCity().isEmpty()) {
			guestFaculty.setCommunicationAddressCity(objform
					.getCurrentCity());
		}

		if (objform.getSameAddress().equalsIgnoreCase(
				"true")) {
			if (objform.getCurrentAddressLine1() != null
					&& !objform.getCurrentAddressLine1()
							.isEmpty()) {
				guestFaculty.setPermanentAddressLine1(objform
						.getCurrentAddressLine1());
			}

			if (objform.getCurrentAddressLine2() != null
					&& !objform.getCurrentAddressLine2()
							.isEmpty()) {
				guestFaculty.setPermanentAddressLine2(objform
						.getCurrentAddressLine2());
			}

			if (objform.getCurrentZipCode() != null
					&& !objform.getCurrentZipCode()
							.isEmpty()) {
				guestFaculty.setPermanentAddressZip(objform
						.getCurrentZipCode());
			}

			if (objform.getCurrentCountryId() != null
					&& !objform.getCurrentCountryId()
							.isEmpty()) {
				Country currentCountry = new Country();
				currentCountry.setId(Integer
						.parseInt(objform
								.getCurrentCountryId()));
				guestFaculty
						.setCountryByPermanentAddressCountryId(currentCountry);
			}

			if (objform.getCurrentState() != null
					&& !objform.getCurrentState()
							.isEmpty()) {
				if (objform.getCurrentState()
						.equalsIgnoreCase("other")) {
					if (objform.getOtherCurrentState() != null
							&& !objform
									.getOtherCurrentState().isEmpty()) {
						guestFaculty
								.setPermanentAddressStateOthers(objform
										.getOtherCurrentState());
					}
				} else {
					State currentState = new State();
					currentState.setId(Integer
							.parseInt(objform
									.getCurrentState()));
					guestFaculty
							.setStateByPermanentAddressStateId(currentState);
				}
			}

			if (objform.getCurrentCity() != null
					&& !objform.getCurrentCity().isEmpty()) {
				guestFaculty.setPermanentAddressCity(objform
						.getCurrentCity());
			}

		} else {
			if (objform.getAddressLine1() != null
					&& !objform.getAddressLine1()
							.isEmpty()) {
				guestFaculty.setPermanentAddressLine1(objform
						.getAddressLine1());
			}

			if (objform.getAddressLine2() != null
					&& !objform.getAddressLine2()
							.isEmpty()) {
				guestFaculty.setPermanentAddressLine2(objform
						.getAddressLine2());
			}

			if (objform.getPermanentZipCode() != null
					&& !objform.getPermanentZipCode()
							.isEmpty()) {
				guestFaculty.setPermanentAddressZip(objform
						.getPermanentZipCode());
			}

			if (objform.getCountryId() != null
					&& !objform.getCountryId().isEmpty()) {
				Country country = new Country();
				country.setId(Integer.parseInt(objform
						.getCountryId()));
				guestFaculty.setCountryByPermanentAddressCountryId(country);
			}

			if (objform.getStateId() != null
					&& !objform.getStateId().isEmpty()) {
				if (objform.getStateId().equalsIgnoreCase(
						"other")) {
					if (objform.getOtherPermanentState() != null
							&& !objform
									.getOtherPermanentState().isEmpty()) {
						guestFaculty
								.setPermanentAddressStateOthers(objform
										.getOtherPermanentState());
					}
				} else {
					State state = new State();
					state.setId(Integer.parseInt(objform
							.getStateId()));
					guestFaculty.setStateByPermanentAddressStateId(state);
				}
			}

			if (objform.getCity() != null
					&& !objform.getCity().isEmpty()) {
				guestFaculty.setPermanentAddressCity(objform
						.getCity());
			}
		}
		if (objform.getNationalityId() != null
				&& !objform.getNationalityId().isEmpty()) {
			Nationality nationality = new Nationality();
			nationality.setId(Integer.parseInt(objform
					.getNationalityId()));
			guestFaculty.setNationality(nationality);
		}
		guestFaculty.setOutside(true);
		guestFaculty.setIsActive(true);
		guestFaculty.setActive(true);
		
		return guestFaculty;
	}
	
}



