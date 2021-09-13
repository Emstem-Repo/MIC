package com.kp.cms.forms.fee;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.CurrencyTO;
import com.kp.cms.to.fee.FeeAccountAssignmentTO;
import com.kp.cms.to.fee.FeeTO;

/**
 * 
 * Date 20/jan/2009
 * This is a Form for Fee Assignments.
 */
public class FeeAssignmentForm extends BaseActionForm {

	private static final long serialVersionUID = 1L;
	private String id;
	private String academicYear;
	private String semister;
	private Map<Integer,String> feeGroupMap;
	private Map<Integer,String> feeDivisionMap;
	private Map<Integer,String> feeAccountsMap;
	private Map<String,Map<Integer,String>> feeHeadingsMap;
	private Map<Integer,String> admitedThroughMap;
	private FeeTO feeTO;
	private List<FeeAccountAssignmentTO> feeAssignmentList;
	private List<FeeTO> feeList;
	private List<CurrencyTO> currencies;
	private String aidedUnAided;
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the feeAccountsMap
	 */
	public Map<Integer, String> getFeeAccountsMap() {
		return feeAccountsMap;
	}

	/**
	 * @param feeAccountsMap the feeAccountsMap to set
	 */
	public void setFeeAccountsMap(Map<Integer, String> feeAccountsMap) {
		this.feeAccountsMap = feeAccountsMap;
	}



	/**
	 * @return
	 */
	public Map<String, Map<Integer, String>> getFeeHeadingsMap() {
		return feeHeadingsMap;
	}

	public void setFeeHeadingsMap(Map<String, Map<Integer, String>> feeHeadingsMap) {
		this.feeHeadingsMap = feeHeadingsMap;
	}

	/**
	 * @return the admitedThroughMap
	 */
	public Map<Integer, String> getAdmitedThroughMap() {
		return admitedThroughMap;
	}

	/**
	 * @param admitedThroughMap the admitedThroughMap to set
	 */
	public void setAdmitedThroughMap(Map<Integer, String> admitedThroughMap) {
		this.admitedThroughMap = admitedThroughMap;
	}

	/**
	 * @return the feeAssignmentList
	 */
	public List<FeeAccountAssignmentTO> getFeeAssignmentList() {
		return feeAssignmentList;
	}
	/**
	 * @param feeAssignmentList the feeAssignmentList to set
	 */
	public void setFeeAssignmentList(List<FeeAccountAssignmentTO> feeAssignmentList) {
		this.feeAssignmentList = feeAssignmentList;
	}
	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	/**
	 * @return the academicYear
	 */
	public String getAcademicYear() {
		return academicYear;
	}
	/**
	 * @param academicYear the academicYear to set
	 */
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	/**
	 * @return the feeList
	 */
	public List<FeeTO> getFeeList() {
		return feeList;
	}
	/**
	 * @param feeList the feeList to set
	 */
	public void setFeeList(List<FeeTO> feeList) {
		this.feeList = feeList;
	}
	/**
	 * @return the semister
	 */
	public String getSemister() {
		return semister;
	}

	/**
	 * @param semister the semister to set
	 */
	public void setSemister(String semister) {
		this.semister = semister;
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 */
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		this.semister = null;
		this.academicYear = null;
		this.aidedUnAided=null;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}

	/**
	 * @return the feeGroupMap
	 */
	public Map<Integer,String> getFeeGroupMap() {
		return feeGroupMap;
	}

	/**
	 * @param feeGroupMap the feeGroupMap to set
	 */
	public void setFeeGroupMap(Map<Integer,String> feeGroupMap) {
		this.feeGroupMap = feeGroupMap;
	}

	/**
	 * @return the feeDivisionMap
	 */
	public Map<Integer,String> getFeeDivisionMap() {
		return feeDivisionMap;
	}

	/**
	 * @param feeDivisionMap the feeDivisionMap to set
	 */
	public void setFeeDivisionMap(Map<Integer,String> feeDivisionMap) {
		this.feeDivisionMap = feeDivisionMap;
	}

	/**
	 * @return the feeTO
	 */
	public FeeTO getFeeTO() {
		return feeTO;
	}

	/**
	 * @param feeTO the feeTO to set
	 */
	public void setFeeTO(FeeTO feeTO) {
		this.feeTO = feeTO;
	}

	public List<CurrencyTO> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<CurrencyTO> currencies) {
		this.currencies = currencies;
	}

	public String getAidedUnAided() {
		return aidedUnAided;
	}

	public void setAidedUnAided(String aidedUnAided) {
		this.aidedUnAided = aidedUnAided;
	}
}
