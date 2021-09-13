<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<link rel="stylesheet" href="css/StudentLayout1Styles.css">
<link rel="stylesheet" type="text/css" href="css/styles.css"/>


<style>
@media print {
 	@page{
 		margin: 7mm;
 	}
}
.table1,th{
	font-family: Times New Roman, Times, serif;
	font-size: 14px;
	font-weight: normal;
	color: #000000;
	background-color: #FFFFFF;
	font-weight: normal;
    position: relative;
	border-bottom: 1px solid black;
	background: transparent; 
	padding: 17px;
	 
}
.tds{
	 border-left: 2px solid black;
	 font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: normal;
	color: #000000;
	background-color: #FFFFFF;
	font-weight: bold;
    position: relative;
    background: transparent; 
}
.format{
	border: 1px solid black;
	
}

.row-print {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: normal;
	color: #000000;
	background-color: #FFFFFF;
	font-weight: bold;
    position: relative;
    background: transparent; 
}
.styleimport {
	font-family: Times New Roman, Times, serif;
	font-size: 14px;
	font-weight: bold;
	color: #000000;
	background-color: #FFFFFF;
	font-weight: bold;
    position: relative;
    background: transparent; 
}
</style>
<script language="JavaScript" >
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=returnHomePage";
}
function resetErrorMsgs() {	
	resetFieldAndErrMsgs();
}
function printPass(){
	var url = "/studentWiseAttendanceSummary.do?method=printStudentAbsenceDetails";
	myRef = window.open(url, "left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");

}
</script>
<html:form action="/disciplinaryDetails" >
<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="DisciplinaryDetailsForm"/>
	<table width="100%" cellspacing="0" cellpadding="0" class="row-white"   style="background-repeat:no-repeat; background-position: center; padding-bottom: 30px;">
								
								<tr>
									<td width="80%" align="center">
									<img src='<%=CMSConstants.LOGO_URL%>'  height="100" width="600" />
									</td>
								</tr>
								<tr>
									<td>
										<table  align="center" width="100%" cellspacing="1" cellpadding="0" style="padding-bottom: 20px;">
										<tr align="center">
											<td  width="12%" class="row-print"><div align="right"><b>Name</b></div></td>
											<td class="row-print" width="1%"><b>:</b></td> 
											<td width="30%" class="styleimport"><div align="left" ><bean:write name="DisciplinaryDetailsForm" property="studentName"/></div></td>
											<td class="row-print"  width="15%"><div align="right"><b>Class & Sem </b></div></td>
											<td class="row-print" width="1%"><b>:</b></td> 
											<td width="12%" class="styleimport"><div align="left" ><bean:write name="DisciplinaryDetailsForm" property="className"/>&nbsp;&nbsp;&nbsp;<bean:write name="DisciplinaryDetailsForm" property="termNo"/></div></td>
											<td width="15%" class="row-print" ><div align="right"><b>Register No</b></div></td>
											<td class="row-print" width="1%"><b>:</b></td> 
											<td width="15%" class="styleimport"><div align="left" ><bean:write name="DisciplinaryDetailsForm" property="registerNo"/></div></td>
										</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table class="format" align="center" width="80%" >
										<tr align="center" >
										<td width="100%" align="center" class="row-print"  colspan="4" height="2px" style=
										"border-bottom: 1px solid black;padding: 6px;" ><b>Absence Details</b>
										</td>
										</tr>
										<tr>
										<td width="30%" class="styleimport" height="2px" style="padding: 5px;"><div align="center" class="th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Working Hours :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="workingHours"/></td>
										<td width="40%" class="styleimport" height="2px"><div align="center" >Total Co Curricular Leave Availed &nbsp;  :</div></td>
										<td width="15" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="approvedLeaveHrs"/>&nbsp;Hrs</td>
										</tr>
										<tr>
										<td width="20%" class="styleimport" height="2px" ><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Present Hours :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="presentHours"/></td>
										<td  class="styleimport" width="35%" height="2px"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Attendance Required for 75  :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="requiredHrs"/>Hrs</td>
										</tr>
										</table>
									</td>
								</tr> 
								<tr>
								<td>
								<table class="format"  align="center" width="80%" >
								<tr>	
										
										<td width="30%"  height="2px" class="row-print" style="padding: 5px;"><div align="center">Attendance Percentage :</div>
										</td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="percentage"/>%</td>
										
										<td  width="35%" class="tds" height="2px"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Shortage of Attendance :</div>
										</td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="DisciplinaryDetailsForm" property="shortageOfAttendance"/>Hrs</td>
										</tr>
								</table>
								</td>
								</tr>
								
								<tr height="10px"><td></td>
								</tr>
								
								<tr>
								<td >
								<hr width="80%" style="border-style: 2px solid black"></hr>
								</td>
								</tr>
								<tr>
								<td class="row-print"><div align="center">ABSENCE DETAILS</div>
								</td>
								</tr>
								
								<tr height="10px"><td></td>
								</tr>
								
								
								<tr>
								<td>
								<table class="format" align="center" width="90%" rules="all" >
									 <logic:empty name="DisciplinaryDetailsForm" property="attList">
									 	No Abscent Records
									 </logic:empty>
									 <logic:notEmpty name="DisciplinaryDetailsForm" property="attList">
										 <tr>
											<td class="row-print">Date </td>
											<td  class="row-print">
											<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Day </div>
											</td>
											<logic:iterate id="periodName" name="DisciplinaryDetailsForm" property="periodNameList">
												<td  class="row-print">
												<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<bean:write name="periodName"/>
												</div>
												</td>
											</logic:iterate>
											<td class="row-print">Total</td>
											</tr>
										<logic:notEmpty name="DisciplinaryDetailsForm" property="attList">
											<logic:iterate id="to" name="DisciplinaryDetailsForm" property="attList">
												<tr class="row-print">
												<td><bean:write name="to" property="date"/> </td>
											<td><bean:write name="to" property="day"/> </td>
											<logic:iterate id="pto" name="to" property="periodList">
												<td class="row-print">
												<logic:equal value="true" name="pto" property="coLeave">
												<div align="center" style="background-color: #808080">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													
												<logic:equal value="true" name="pto" property="coLeave">
												<bean:write name="pto" property="periodName"/>
												</logic:equal>
												</div>
												</logic:equal>
												<logic:equal value="false" name="pto" property="coLeave">
												<div align="center" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<logic:equal value="false" name="pto" property="coLeave">
												<bean:write name="pto" property="periodName"/>
												</logic:equal>
												</div>
												</logic:equal>
												</td>
											</logic:iterate>
											<td><bean:write name="to" property="hoursHeldByDay"/> </td>
												</tr>
											</logic:iterate>
										</logic:notEmpty>
										</logic:notEmpty>
										
									</table>
								</td>
								</tr>
								
								
																
	</table>
	</html:form>
	<script type="text/javascript">window.print();</script>