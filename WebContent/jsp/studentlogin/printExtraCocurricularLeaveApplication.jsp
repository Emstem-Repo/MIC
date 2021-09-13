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
	var url = "/ExtraCocurricularLeaveEntry.do?method=printCoCurricularLeaveApplication";
	myRef = window
			.open(url, 
					"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");

}
</script>
<html:form action="/ExtraCocurricularLeaveEntry" >
<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="extraCocurricularLeaveEntryForm"/>
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
											<td width="30%" class="styleimport"><div align="left" ><bean:write name="extraCocurricularLeaveEntryForm" property="studentName"/></div></td>
											<td class="row-print"  width="15%"><div align="right"><b>Class & Sem </b></div></td>
											<td class="row-print" width="1%"><b>:</b></td> 
											<td width="12%" class="styleimport"><div align="left" ><bean:write name="extraCocurricularLeaveEntryForm" property="className"/>&nbsp;&nbsp;&nbsp;<bean:write name="extraCocurricularLeaveEntryForm" property="termNo"/></div></td>
											<td width="15%" class="row-print" ><div align="right"><b>Register No</b></div></td>
											<td class="row-print" width="1%"><b>:</b></td> 
											<td width="15%" class="styleimport"><div align="left" ><bean:write name="extraCocurricularLeaveEntryForm" property="registerNo"/></div></td>
										</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table class="format" align="center" width="80%" >
										<tr align="center" >
										<td width="100%" align="center" class="row-print"  colspan="4" height="2px" style=
										"border-bottom: 1px solid black;padding: 6px;" ><b>Absence Details</b> up to &nbsp;<bean:write name="extraCocurricularLeaveEntryForm" property="date"/>
										</td>
										</tr>
										<tr>
										<td width="30%" class="styleimport" height="2px" style="padding: 5px;"><div align="center" class="th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Working Hours :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="workingHours"/></td>
										<td width="40%" class="styleimport" height="2px"><div align="center" >Total Co Curricular Leave Availed &nbsp;  :</div></td>
										<td width="15" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="approvedLeaveHrs"/>&nbsp;Hrs</td>
										</tr>
										<tr>
										<td width="20%" class="styleimport" height="2px" ><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Present Hours :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="presentHours"/></td>
										<td  class="styleimport" width="35%" height="2px"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Attendance Required for 75  :</div></td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="requiredHrs"/>Hrs</td>
										</tr>
										</table>
									</td>
								</tr> 
								<tr>
								<td>
								<table class="format"  align="center" width="80%" >
								<tr>	
										
										<td width="30%"  height="2px" class="row-print" style="padding: 5px;"><div align="left">Attendance Percentage :</div>
										</td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="percentage"/>%</td>
										
										<td  width="35%" class="tds" height="2px"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Shortage of Attendance :</div>
										</td>
										<td width="15%" height="2px" class="styleimport"><bean:write name="extraCocurricularLeaveEntryForm" property="shortageOfAttendance"/>Hrs</td>
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
								
								<tr height="10px"><td></td>
								</tr>
								
								<tr>
								<td>
								<table class="format" align="center" width="80%"  rules="cols">
								<tr>
								<th colspan="4" height="5px"><div align="center" class="row-print"  ><FONT size="3px">DETAILS OF ABSENCE </FONT></div>
								</th>
								</tr>
								<tr >
								<th width="5%" class="th" height="2px"><div align="center" class="row-print">Reason</div></th>
								<th class="row-print" width="5%" class="th" height="2px"><div align="center">Dates</div></th>
								<th class="row-print" width="7%" class="th" height="2px"><div align="center">Total Hours</div></th>
								<th class="row-print" width="9%" class="th" height="2px"><div align="center">Name & Signature of Leave Recommending Authority</div></th>
								</tr>
								<nested:notEmpty name="extraCocurricularLeaveEntryForm" property="leaveList">
								<nested:iterate name="extraCocurricularLeaveEntryForm" property="leaveList" id="leave">
								<tr height="5px">
								<th class="th" width="5%" height="5px"><div align="center"><nested:write property="activityName" name="leave"/></div></th>
										<th class="th" width="5%" height="5px"><div align="center">
										<logic:notEmpty name="extraCocurricularLeaveEntryForm" property="periodsMap">
										<logic:iterate name="extraCocurricularLeaveEntryForm" property="periodsMap" id="map"> 
										<c:set var="activity" value="${leave.activityName}"/>
 										<jsp:useBean id="activity" type="java.lang.String" />
  										<logic:equal value="<%=activity %>" property="key" name="map">
											<nested:write property="value" name="map"/>
										</logic:equal>
										</logic:iterate>
										</logic:notEmpty>
										
										</div></th>
										<th class="th" width="7%" height="5px"><div align="center"><nested:write property="totalHrs" name="leave"/>&nbsp;Hrs</div></th>
									<th class="th" width="9%" height="5px"></th>
									</tr>
								</nested:iterate>
								</nested:notEmpty>
								<tr>
								<th width="10%" class="th"  colspan="2" height="2px"><div align="center">Total</div></th>
								<th width="5%" class="th" height="2px"><div align="center"><bean:write name="extraCocurricularLeaveEntryForm" property="subTotalHrs"/>&nbsp;Hrs</div></th>
								<th width="5%" class="th" height="2px"><div align="center">Whether Supporting Document Given</div><br>
								<div align="center" height="2px"><span style='border:2px black solid;'>YES</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='border:2px black solid;'>NO</span>
								</div>
								</th>
								</tr>
								<tr>
								<th rowspan="2" colspan="2" width="15%" height="2px"><div align="center">Recommendation of Gaurdian/Warden</div></th>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Signature:</div>
								</th>
								</tr>
								<tr>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Mobile No:</div></th>
								</tr>
								<tr>
								<th colspan="2"><div align="center" class="th" height="2px">Signature of Applicant </div></th>
								<th colspan="2">
								</th></tr>
								<tr>
								<th colspan="4"><div align="center" class="row-print" height="2px"><font size="3.em"> FOR OFFICE USE ONLY </font></div>
								</th>
								</tr>
								<tr>
								<th rowspan="2" colspan="2" width="15%" height="2px"><div align="center">Recommendation of Faculty Advisor</div></th>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Name:</div>
								</th>
								</tr>
								<tr>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Signature:</div></th>
								</tr>
								<tr>
								<th rowspan="2" colspan="2" width="15%" height="2px"><div align="center">Recommendation of HoD</div></th>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Name:</div>
								</th>
								</tr>
								<tr>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Signature:</div></th>
								</tr>
								<tr>
								<th rowspan="2" colspan="2" width="15%" height="2px"><div align="center">Approval of the Principal</div></th>
								
								</tr>
								<tr>
								<th colspan="2" width="10%" class="th" height="2px"><div align="left">Signature:</div></th>
								</tr>
								</table>
								</td>
								</tr>
																
	</table>
	</html:form>
