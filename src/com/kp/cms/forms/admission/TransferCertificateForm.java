package com.kp.cms.forms.admission;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.kp.cms.forms.BaseActionForm;
import com.kp.cms.to.admin.StudentTO;
import com.kp.cms.to.admission.AdmApplnTO;
import com.kp.cms.to.admission.PrintTcDetailsTo;

public class TransferCertificateForm extends BaseActionForm
{
	private String year;
	private String method;
	private String classes;
	private String fromUsn;
	private String toUsn;
	private String toCollege;
	private String duplicate="No";
	private boolean reprint;
	private String printOnlyTc;
	
	Map<Integer,String> classMap;
	List<PrintTcDetailsTo>studentList;
	private String includeFail="No";
	private String tcType;
	private String tcFor;
	private String rePrintOnlyTc;
	private String tcDate;
	private String tcReDate;
	private boolean flag;
	private String searchBy;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public Map<Integer, String> getClassMap() {
		return classMap;
	}
	public void setClassMap(Map<Integer, String> classMap) {
		this.classMap = classMap;
	}
	public String getFromUsn() {
		return fromUsn;
	}
	public void setFromUsn(String fromUsn) {
		this.fromUsn = fromUsn;
	}
	public String getToUsn() {
		return toUsn;
	}
	public void setToUsn(String toUsn) {
		this.toUsn = toUsn;
	}
	public List<PrintTcDetailsTo> getStudentList() {
		return studentList;
	}
	public void setStudentList(List<PrintTcDetailsTo> studentList) {
		this.studentList = studentList;
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		String formName = request.getParameter("formName");
		ActionErrors actionErrors = super.validate(mapping, request, formName);
		
		return actionErrors;
	}
	
	public void resetFields()
	{
		this.fromUsn=null;
		this.toUsn=null;
		this.classes=null;
	}
	public String getToCollege() {
		return toCollege;
	}
	public void setToCollege(String toCollege) {
		this.toCollege = toCollege;
	}
	public String getDuplicate() {
		return duplicate;
	}
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}
	public boolean getReprint() {
		return reprint;
	}
	public void setReprint(boolean reprint) {
		this.reprint = reprint;
	}
	public String getIncludeFail() {
		return includeFail;
	}
	public void setIncludeFail(String includeFail) {
		this.includeFail = includeFail;
	}
	public String getTcType() {
		return tcType;
	}
	public void setTcType(String tcType) {
		this.tcType = tcType;
	}
	public String getTcFor() {
		return tcFor;
	}
	public void setTcFor(String tcFor) {
		this.tcFor = tcFor;
	}
	public String getPrintOnlyTc() {
		return printOnlyTc;
	}
	public void setPrintOnlyTc(String printOnlyTc) {
		this.printOnlyTc = printOnlyTc;
	}
	public String getRePrintOnlyTc() {
		return rePrintOnlyTc;
	}
	public void setRePrintOnlyTc(String rePrintOnlyTc) {
		this.rePrintOnlyTc = rePrintOnlyTc;
	}
	public String getTcDate() {
		return tcDate;
	}
	public void setTcDate(String tcDate) {
		this.tcDate = tcDate;
	}
	public String getTcReDate() {
		return tcReDate;
	}
	public void setTcReDate(String tcReDate) {
		this.tcReDate = tcReDate;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
}
