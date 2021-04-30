<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<head>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<title><bean:message key="knowledgepro.admission.transferCertificate"/></title>
<style type="text/css">
	.heading1
	{
		font-weight: bold;	
		font-family: Arial,Helvetica,sans-serif;
		font-family: 9.5pt;
	}
	.heading2
	{
		font-family: Arial,Helvetica,sans-serif;
		font-size: 13px;		
		vertical-align: top;
	}
	
	.heading3
	{
		font-weight: bold;
		font-family: Arial,Helvetica,sans-serif;
		font-size: 9.5pt;
	}
	.heading4
	{
		font-weight: bold;
		font-family: Arial,Helvetica,sans-serif;
		font-size: 9pt;
	}
	.heading5
	{
		font-weight: bold;
		font-family: Arial,Helvetica,sans-serif;
		font-size: 9pt;
	}
	.body2
	{
		font-size: 8pt
	}
	
	.border
	{
		border:1px solid black;
	}	
</style>
</head>
<body>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<html:form action="/transferCertificate">

<html:hidden property="formName" value="employeeApplyLeaveForm" />
	<table width="100%" border="0">
	<tr>
		
		<td width="48%" style="border-bottom: solid thin; border-left: solid thin; border-right: solid thin; border-top: solid thin">
			<table border="0" width="100%">
					<tr>
				<td width="100%" align="center">
				<table width="100%" height="20%" border="0">
		
				<tr><td>&nbsp;</td></tr>
								<tr>
				<td align="center" >
		  			<img width="450" height="100"  src='images/leave_logo.jpg' alt="Logo not available">
				</td>
				</tr>
											
				</table></td></tr>
		
				<tr><td width="100%">
				<table width="100%"> 				
       	    			<tr><td align="center" >
<table width="100%"  style="border:1px solid black;border-collapse:collapse;">

<tr><td style="border:1px solid black;">1.</td>
<td class="border">NAME OF APPLICANT:</td><td class="border">
<bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.employeeName" ></bean:write></td>
</tr>
<tr><td class="border">2.</td>
<td class="border">DESIGNATION</td><td class="border">
<bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.employee.designation.name" ></bean:write></td>
</tr>

<tr><td class="border" rowspan="2">3.</td>
<td class="border">a. NO. OF DAYS OF CASUAL/ DUTY LEAVE REQUESTED</td>
<td class="border">
<bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.noOfDays" ></bean:write></td></tr>
<tr><td class="border">b. DATE(S) ON WHICH CASUAL/ DUTY LEAVE IS REQUESTED</td><td class="border">
<bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.requestedLeaveDate" ></bean:write></td></tr>



<tr><td class="border">4.</td>
<td class="border">REASON FOR AVAILING CASUAL/ DUTY LEAVE</td><td class="border">
<bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.onlineLeave.reason" /></td>
</tr>

<tr><td class="border">5.</td>
<td class="border">NO. OF DAYS OF CASUAL/ DUTY LEAVE AVAILED OF DURING THE CALENDAR YEAR</td><td class="border">
<bean:write name="employeeApplyLeaveForm" property="empLeaveTo.leavesSanctioned" /></td>
</tr>

<tr><td class="border">6.</td>
<td class="border">SIGNATURE OF THE APPLICANT</td><td class="border"></td>
</tr>

<tr><td class="border">7.</td>
<td class="border">DATE OF APPLICATION</td><td class="border"><bean:write name="employeeApplyLeaveForm" property="empApplyLeaveTO.appliedOn" /></td>
</tr>

<tr><td class="border" rowspan="3">8.</td>
<td class="border">a. WHETHER LEAVE INFORMED TO CONCERNED FACULTY ADVISOR AND HoD BEFORE AVAILING LEAVE</td>
<td class="border"></td></tr>
<tr><td class="border">b.	TEACHING HOURS ON DAY(S) OF LEAVE</td><td class="border">
</td></tr>
<tr><td class="border">c.	WORK ARRANGEMENT DONE BY THE HoD</td><td class="border"></td></tr>

<tr><td class="border">9.</td>
<td class="border">RECOMMENDATION OF THE HoD</td><td class="border"></td>
</tr>

<tr><td class="border">10.</td>
<td class="border">ORDER OF THE PRINCIPAL</td><td class="border"></td>
</tr>

</table></td></tr>

    			

		
    
				
 				</table></td></tr>
 				
 				<tr>
 				<td>
 				<table width="100%">
 				<tr><td></td></tr>
 				<tr><td></td></tr>  				
 	
 				
 				</table></td></tr>
		</table>
		</td>
		

		
		
	</tr>
	</table>
		
	<p style="page-break-after: always;"></p>
<script type="text/javascript">
	window.print();
</script>

</html:form>
</body>
