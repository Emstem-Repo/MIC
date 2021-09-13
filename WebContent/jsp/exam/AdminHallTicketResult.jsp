<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="javax.servlet.*,java.text.*" %>
<style>
.bold{
	font-weight: bold;
}
</style>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<link rel="stylesheet" href="css/calendar.css">
<html:form action="/adminHallTicket" >
	<html:hidden property="method" styleId="method" value="printMarksCard" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="adminHallTicketForm" />
	<logic:notEmpty name="adminHallTicketForm" property="studentList">
		<logic:iterate id="to" name="adminHallTicketForm" property="studentList" indexId="count">
		<c:if test="${count>0}">
		<p style="page-break-after:always;"> </p>
		</c:if>
			<table width="100%" cellspacing="1" cellpadding="2" class="row-white" style="font-family: Times New Roman">
							
							<!--<tr>
							<td align="left">
							<img src='<%=request.getContextPath()+"/LogoServlet?count=1"%>'  height="100" width="210" /></td>
							<td align="right">  <bean:define id="reg" name="to" property="registerNo"></bean:define>
								<img src='<bean:write name="to" property="studentPhoto" />'  height="128" width="133" /></td>
							</tr>
							<tr>
							<td width="80%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXAMINATION ADMISSION TICKET</td>
							<td>
								
							<%--<img src='<%=CMSConstants.barbecueLink %><%=reg %>' height="21" width="118"   />
							--%>
							
							</td>
							</tr>
							
							
							
							-->
							
							<tr>
							
							<td colspan="3" width="100%" align="center">
							<img src='<%=request.getContextPath()+"/LogoServlet?count=1"%>'  height="100" width="600" /></td>
							<!--<td width="60%"> <FONT size="2">&nbsp;&nbsp;&nbsp;&nbsp;HALL TICKET OF <bean:write name="to" property="semesterExt"/> SEMESTER &nbsp; DEGREE EXAMINATION <bean:write name="to" property="month"/> <bean:write name="to" property="year"/></font></td>
							
							-->
							  
							
							</tr>
							
							<tr>							
							<td colspan="3" width="100%" align="center" class="bold">
							 <FONT size="3">Hall Ticket</font>
							</td>							
							</tr>
							<tr>							
							<td colspan="3" width="100%" align="center" class="bold">
							 <FONT size="3"><bean:write name="to" property="examName"/></font>
							</td>							
							</tr>
							<tr><td>&nbsp;</td></tr>							
							<tr> 
							<td colspan="3">
							<table width="100%">
							
							
							<tr>
							
							<td align="left" width="80%">
							<table>
							<tr>
							<td class="bold"><FONT size="3">Register Number</FONT></td>
							<td class="bold"><FONT size="3">:&nbsp;<bean:write name="to" property="registerNo"/></FONT> </td>
							</tr>
							<tr>
							<logic:equal value="UG" name="adminHallTicketForm" property="programTypeName">
								<td class="bold"><FONT size="2">Programme</FONT></td>
							</logic:equal>
							<logic:notEqual value="UG" name="adminHallTicketForm" property="programTypeName">
								<td class="bold"><FONT size="2">Stream</FONT></td>
							</logic:notEqual>						
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="to" property="courseName"/> </FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Semester</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="to" property="semesterNo"/> </FONT></td>
							</tr>
							
							<tr>
							<td class="bold"><FONT size="2">Name of the Candidate</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="to" property="studentName"/> </FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Centre of Examination</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;Mar Ivanios College, Thiruvananthapuram</FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Date of Birth</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="to" property="dateofBirth"/></FONT> </td>
							</tr>
							
							<!--<logic:notEmpty property="roomAlloted" name="to">
							<td align="right">Venue :</td>
							<td><bean:write name="to" property="blockNO"/> - Floor No:&nbsp;<bean:write name="to" property="floorNo"/> 
							- Room No:&nbsp;<bean:write name="to" property="roomAlloted"/>
							</td>
							</logic:notEmpty>
							-->
							
							
							
							</table>
							</td>
							
							
							
							<td align="right" width="20%">
							<table>
							<tr>
							<td width="" align="right" rowspan="2"><img src='<bean:write name="to" property="studentPhoto" />'  height="128" width="133" />
							</td>
							
							</tr>
							</table>
							</td>
							
							
							
							</tr>
							
							
							
							
							
							
							
							</table>
							</td>
							</tr>
							
							<tr> 
							<td colspan="3">
							<table width="100%">
							
							<tr>
							<td colspan="8">
							<table  width="95%" style="border: 1px solid black; " rules="all">
							<tr>
							<td align="center" class="bold"><FONT size="2">Course Code</FONT></td>
							<td align="center" class="bold"><FONT size="2">Course Name</FONT></td>
							<logic:notEqual value="PG" name="adminHallTicketForm" property="programTypeName"><td align="center" class="bold"><FONT size="2">Course Type</FONT></td></logic:notEqual>
						<!--<logic:equal value="false" property="doNotDisplay" name="to">
							<td align="center" class="bold"><FONT size="2">Date</FONT></td>
							<td align="center" class="bold"><FONT size="2">Start Time</FONT></td>
							<td align="center" class="bold"><FONT size="2">End Time</FONT></td>
						</logic:equal>
							<td align="center">Venue</td>
							
							<td align="center">Signature of the Invigilator</td>
							
							--></tr>
							<logic:notEmpty name="to" property="subList">
							<logic:iterate id="sub" name="to" property="subList">
							<tr height="21px">
							<td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="code" /></FONT></td>
							<td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="name" /></FONT></td>
							<logic:notEqual value="PG" name="adminHallTicketForm" property="programTypeName"><td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="subjectType" /></FONT></td>
							</logic:notEqual>
						<!--<logic:equal value="false" property="doNotDisplay" name="to">
							<td align="center"><FONT size="2"><bean:write name="sub" property="startDate" /></FONT></td>
							<td align="center"><FONT size="2"><bean:write name="sub" property="startTime" /></FONT></td>
							<td align="center"><FONT size="2"><bean:write name="sub" property="endTime" /></FONT></td>
							
						</logic:equal>
							<td>
							<logic:notEmpty name="sub" property="roomNo">
							 <bean:write name="sub" property="blockNo"/>- Floor:<bean:write name="sub" property="floorNo"/>
							- Room:<bean:write name="sub" property="roomNo"/>
							</logic:notEmpty>
							</td>
							
							<td>&nbsp; </td>
							
							--></tr>
							</logic:iterate>
							</logic:notEmpty>
							</table>
							</td>
							</tr>
							</table>
							</td>
							</tr>
							
							<!--<tr height="10px">
							<td colspan="3"></td>
							</tr>
							
							
							-->
							<tr><td>&nbsp;</td></tr>
							<tr><td>&nbsp;</td></tr>
							
							<tr><td colspan="3">
							<table width="100%">
							<tr>							
							<td  align="left" width="40%" class="bold"><br></br>
							<FONT size="2" style="font-family: serif">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Signature of the Candidate</FONT><br>
							<FONT  style="font-family: serif" size="1">(To be signed in presence of the issuing officer)</FONT></td>
							<td align="center"><FONT size="2" style="font-family: serif"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Office Seal)</FONT></td>
							<td colspan="2" align="right" class="bold">
								<FONT size="2" style="font-family: serif">&nbsp;<br>Controller of Examinations</FONT>
							</td>
							<!--<td  align="center" width="30%"></td>
							<td align="right"  width="30%"><img src="images/Principal.jpg" width="130px" height="50px" /></td>
							
							
							
							--></tr><tr><td>&nbsp;</td></tr>
							<tr>
							<td colspan="3" align="right" class="bold" style="border-top: solid thin">
							</td>
							</tr><tr><td>&nbsp;</td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td colspan="3" align="left" class="bold"><FONT size="3" style="font-family: serif">Issued By:
							</FONT></td>
							</tr>							
							</table>
							</td></tr>
							
							<tr><td colspan="3">
							<table width="100%"><!--
							
							<tr>
							<td  align="left" width="50%"><FONT size="1" style="font-family: serif"><br></br><br></br>
								Seal and Signature of the Head of the Department </FONT>
							</td>
							<td  align="center" width="20%"><br></br><br></br><FONT size="1" style="font-family: serif">(Seal)</font></td>
							<td  align="right" width="30%"><img src="images/COEFinal.jpg" width="130px" height="50px" /></td>
							</tr>
							--></table></td></tr>
							
							
							<!--<tr>
							<td colspan="3">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Signature of The Candidate:
							</td>
							</tr>
							
							-->
							
							

							
							<tr height="10px">
							<td colspan="3" class="row-print-desc">
							<logic:notEmpty name="adminHallTicketForm" property="description">
								<c:out value="${adminHallTicketForm.description}" escapeXml="false"></c:out>
							</logic:notEmpty>
							</td>
							</tr>
							
							
							
							<%--
							<tr>
							<td colspan="2" align="right">
								<img src="images/COEFinal.jpg" width="157px" height="72px" />
							</td>
							</tr>
							
							<tr><td colspan="2" align="right">
							<%
								   Date dNow = new Date( );
								   SimpleDateFormat ft = 
								   new SimpleDateFormat ("E dd/MM/yyyy 'at' hh:mm:ss a zzz");
								   out.print( "<h6 align=\"right\">" + 
							               ft.format(dNow) + 
							               "</h2>");
								%>
							</td></tr>
							
							--%>
							
							</table>
		</logic:iterate>
	</logic:notEmpty>
</html:form>