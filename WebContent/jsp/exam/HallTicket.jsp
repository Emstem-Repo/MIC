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
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<style>
.bold{
	font-weight: bold;
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
	var url = "StudentLoginAction.do?method=printHallTicket";
	myRef = window
			.open(url, "HallTicket",
					"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");

}
</script>
<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />

	<logic:empty name="loginform" property="hallticketBlock">
	<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin.studentlogin" />
			<span class="Bredcrumbs">&gt;&gt; <bean:message key="knowledgepro.exam.publishHM.hallTicket" /> &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/st_Tright_03_01.gif"></td>
					<td width="100%" background="images/st_Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader"><bean:message key="knowledgepro.exam.publishHM.hallTicket" /></strong></div>
					</td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="6" align="left">
							<div align="right"><span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields" /></span></div>
							<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
							<FONT color="green"><html:messages id="msg"
								property="messages" message="true">
								<c:out value="${msg}" escapeXml="false"></c:out>
								<br>
							</html:messages></FONT></div>
							</td>
						</tr>
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>
							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white" style="font-family: Times New Roman">
							<tr>
							<td align="center">
							<img src='<%=CMSConstants.LOGO_URL%>'  height="90" width="450"/></td>
							</tr>
							<tr>
							<td width="80%" align="center" class="bold"><font size="3">Hall Ticket</font></td>
							<!--<td>
								<bean:define id="reg" name="loginform" property="hallTicket.registerNo"></bean:define>
								<img src='<%=CMSConstants.barbecueLink %><%=reg %>' height="21" width="118"   />
								</td>
							--></tr><tr><td align="center" class="bold"><font size="3"><bean:write name="loginform" property="hallTicket.examName"/></font></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr> 
							<td colspan="2">
							<table width="100%">
							<tr>
							<td align="left" width="15%" class="bold"><font size="3">Register Number</font></td><td width="1%">:</td>
							<td class="bold"><font size="3"><bean:write name="loginform" property="hallTicket.registerNo"/></font> </td>
							<td align="right" rowspan="5">  <img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" /></td>
							</tr>
							<tr>
							<logic:equal value="UG" name="loginform" property="programTypeName">
							<td align="left" class="bold"><font size="2">Programme</font></td><td>:</td>
							</logic:equal>
							<logic:notEqual value="UG" name="loginform" property="programTypeName">
							<td align="left" class="bold"><font size="2">Stream</font></td><td>:</td>
							</logic:notEqual>
							<td class="bold"><font size="2"><bean:write name="loginform" property="hallTicket.courseName"/></font></td></tr>
							<tr>
							<td align="left" class="bold"><font size="2">Semester</font></td><td>:</td>
							<td><font size="2" class="bold"><bean:write name="loginform" property="hallTicket.semesterNo"/></font></td></tr>							
							<!--<tr>
							<td align="right">Class:</td>
							<td><bean:write name="loginform" property="hallTicket.className"/> </td></tr>
							--><tr><td align="left" class="bold"><font size="2">Name of the Candidate</font></td><td>:</td>
							<td><font size="2" class="bold"><bean:write name="loginform" property="hallTicket.studentName"/></font> </td></tr>
							
							<tr><td align="left" class="bold"><font size="2">Centre of Examination</font></td><td>:</td>
							<td><font size="2" class="bold">Mar Ivanios College, Thiruvananthapuram</font> </td></tr>
							
							<tr>
							<td align="left" class="bold"><font size="2">Date of Birth</font></td><td>:</td>
							<td class="bold"><font size="2"><bean:write name="loginform" property="hallTicket.dateofBirth"/></font></td></tr>	
							<%--<logic:notEmpty property="hallTicket.roomAlloted" name="loginform">
							<td align="right">Venue :</td>
							<td><bean:write name="loginform" property="hallTicket.blockNO"/> - Floor No:&nbsp;<bean:write name="loginform" property="hallTicket.floorNo"/> 
							- Room No:&nbsp;<bean:write name="loginform" property="hallTicket.roomAlloted"/>
							</td>
							</logic:notEmpty>
							--%>
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td colspan="8">
							<table  width="95%" style="border: 1px solid black; " rules="all">
							<tr>
							<td align="center" class="bold"><font size="2">Course Code</font></td>
							<td align="center" class="bold"><font size="2">Course Name</font></td>
							<logic:notEqual value="PG" name="loginform" property="programTypeName"><td align="center" class="bold"><FONT size="2">Course Type</FONT></td>							
						</logic:notEqual>
						<!--<logic:equal value="false" property="hallTicket.doNotDisplay" name="loginform">
							<td align="center" class="bold"><font size="2">Date</font></td>
							<td align="center" class="bold"><font size="2">Start Time</font></td>
							<td align="center" class="bold"><font size="2">End Time</font></td>							
						</logic:equal>
							<td align="center">Venue</td>
							<td align="center">Signature of the Invigilator</td>-->
							</tr>
							<logic:notEmpty name="loginform" property="hallTicket.subList">
							<logic:iterate id="sub" name="loginform" property="hallTicket.subList">
							<tr height="21px">
							<td style="padding-left: 5px"><font size="2"><bean:write name="sub" property="code" /></font></td>
							<td style="padding-left: 5px"><font size="2"><bean:write name="sub" property="name" /></font></td>
							<logic:notEqual value="PG" name="loginform" property="programTypeName"><td style="padding-left: 5px"><font size="2"><bean:write name="sub" property="subjectType" /></font></td>
						</logic:notEqual>
						<!--<logic:equal value="false" property="hallTicket.doNotDisplay" name="loginform">
							<td align="center"><font size="2"><bean:write name="sub" property="startDate" /></font></td>
							<td align="center"><font size="2"><bean:write name="sub" property="startTime" /></font></td>
							<td align="center"><font size="2"><bean:write name="sub" property="endTime" /></font></td>							
						</logic:equal>
							<td> 
							<logic:notEmpty name="sub" property="roomNo">
							<bean:write name="sub" property="blockNo"/>- Floor:<bean:write name="sub" property="floorNo"/>
							- Room:<bean:write name="sub" property="roomNo"/>
							</logic:notEmpty>
							</td>
							<td>&nbsp; </td>-->
							</tr>
							</logic:iterate>
							</logic:notEmpty>
							</table>
							</td>
							</tr>
							</table>
							</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td>&nbsp;</td></tr>
							
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td colspan="3" align="right" class="bold" style="border-top: solid thin">
							</td>
							</tr><tr><td>&nbsp;</td></tr>							
							<tr><td>&nbsp;</td></tr>							
									
							<tr height="10px"></tr>
							<tr></tr>
							<tr height="10px">
							<td colspan="2" class="row-print-desc">
							<logic:notEmpty name="loginform" property="description1">
								<c:out value="${loginform.description1}" escapeXml="false"></c:out>
							</logic:notEmpty>
							</td>
							</tr>
							
							<!--<tr><td colspan="2" align="right">
							<%
								   Date dNow = new Date( );
								   SimpleDateFormat ft = 
								   new SimpleDateFormat ("E dd/MM/yyyy 'at' hh:mm:ss a zzz");
								   out.print( "<h6 align=\"right\">" + 
							               ft.format(dNow) + 
							               "</h2>");
								%>
											</td></tr>-->
							</table>
							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/st_04.gif" width="5" height="5" /></td>
							<td background="images/st_05.gif"></td>
							<td><img src="images/st_06.gif" /></td>
						</tr>
					</table>
					<div align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="46%" height="35">
							<div align="right"><html:button property="" styleClass="formbutton" onclick="printPass()">
								Print
							</html:button></div>
							</td>
							<td width="2%"></td>
							<td width="52%" align="left"><html:button property=""  styleClass="formbutton" value="Close" onclick="goToHomePage()"></html:button> 
							</td>
						</tr>
					</table>
					</div>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif" height="29" width="9"></td>
					<td background="images/TcenterD.gif" width="100%"></td>
					<td><img src="images/Tright_02.gif" height="29" width="9"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</logic:empty>
	<logic:notEmpty name="loginform" property="hallticketBlock">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white" style="font-family: Times New Roman">
								<tr>
									<td width="80%" align="center" class="bold"><font size="3"><bean:write name="loginform" property="hallticketBlock"/></font></td>
								</tr>
								
							</table>
							</logic:notEmpty>
</html:form>
