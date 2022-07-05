
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
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
	var url = "StudentLoginAction.do?method=printSuppHallTicket";
	myRef = window
			.open(url, "Hall Ticket",
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
							
							<%--<tr>
							<td align="left">
							<img src='<%=request.getContextPath()+"/LogoServlet?count=1"%>'  height="100" width="210" /></td>
							<td align="right">  <bean:define id="reg" name="to" property="registerNo"></bean:define>
								<img src='<bean:write name="to" property="studentPhoto" />'  height="128" width="133" /></td>
							</tr>
							<tr>
							<td width="80%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXAMINATION ADMISSION TICKET</td>
							<td>
								
							<img src='<%=CMSConstants.barbecueLink %><%=reg %>' height="21" width="118"   />
							
							
							</td>
							</tr>
							
							
							
							--%>
							
							<tr>
							
							<td colspan="3" width="100%" align="center">
							<img src='<%=request.getContextPath()+"/LogoServlet?count=1"%>'  height="120" width="600" /></td>
							<%--<td width="60%"> <FONT size="2">&nbsp;&nbsp;&nbsp;&nbsp;HALL TICKET OF <bean:write name="to" property="semesterExt"/> SEMESTER &nbsp; DEGREE EXAMINATION <bean:write name="to" property="month"/> <bean:write name="to" property="year"/></font></td>
							
							--%>
							  
							
							</tr>
							
							<tr>							
							<td colspan="3" width="100%" align="center" class="bold">
							 <FONT size="3">Hall Ticket</font>
							</td>							
							</tr>
							<tr>							
							<td colspan="3" width="100%" align="center" class="bold">
							 <FONT size="3"><bean:write name="loginform" property="hallTicket.examName"/></font>
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
							<td class="bold"><FONT size="3">:&nbsp;<bean:write name="loginform" property="hallTicket.registerNo"/></FONT> </td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Stream</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="loginform" property="hallTicket.courseName"/> </FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Semester</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="loginform" property="hallTicket.semesterNo"/> </FONT></td>
							</tr>
							
							<tr>
							<td class="bold"><FONT size="2">Name of the Candidate</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="loginform" property="hallTicket.studentName"/> </FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Centre of Examination</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;Mar Ivanios College, Thiruvananthapuram</FONT></td>
							</tr>
							<tr>
							<td class="bold"><FONT size="2">Date of Birth</FONT></td>
							<td class="bold"><FONT size="2">:&nbsp;<bean:write name="loginform" property="hallTicket.dateofBirth"/></FONT> </td>
							</tr>
							
							<%--<logic:notEmpty property="roomAlloted" name="to">
							<td align="right">Venue :</td>
							<td><bean:write name="to" property="blockNO"/> - Floor No:&nbsp;<bean:write name="to" property="floorNo"/> 
							- Room No:&nbsp;<bean:write name="to" property="roomAlloted"/>
							</td>
							</logic:notEmpty>
							--%>
							
							
							
							</table>
							</td>
							
							
							
							<td align="right" width="20%">
							<table>
							<tr>
							<td align="right">  <img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" /></td>
							
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
							<table  width="95%" style="border: 1px solid black;" rules="all">
							<tr>
							<td align="center" class="bold"><FONT size="2">Course Code</FONT></td>
							<td align="center" class="bold"><FONT size="2">Course Name</FONT></td>
							<logic:notEqual value="PG" name="loginform" property="programTypeName"><td align="center" class="bold"><FONT size="2">Course Type</FONT></td>
							</logic:notEqual>
							<!--<td align="center" class="bold"><FONT size="2">Date</FONT></td>
							<td align="center" class="bold"><FONT size="2">Start Time</FONT></td>
							<td align="center" class="bold"><FONT size="2">End Time</FONT></td>
							--><%--<td align="center">Venue</td>
							
							<td align="center">Signature of the Invigilator</td>
							
							--%></tr>
							<logic:notEmpty name="loginform" property="hallTicket.subList">
							<logic:iterate id="sub" name="loginform" property="hallTicket.subList">
							<tr height="21px">
							<td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="code" /></FONT></td>
							<td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="name" /></FONT></td>
							<logic:notEqual value="PG" name="loginform" property="programTypeName"><td style="padding-left: 5px"><FONT size="2"><bean:write name="sub" property="subjectType" /></FONT></td>
							</logic:notEqual>
							<!--<td align="center"><FONT size="2"><bean:write name="sub" property="startDate" /></FONT></td>
							<td align="center"><FONT size="2"><bean:write name="sub" property="startTime" /></FONT></td>
							<td align="center"><FONT size="2"><bean:write name="sub" property="endTime" /></FONT></td>
							
							--><%--<td>
							<logic:notEmpty name="sub" property="roomNo">
							 <bean:write name="sub" property="blockNo"/>- Floor:<bean:write name="sub" property="floorNo"/>
							- Room:<bean:write name="sub" property="roomNo"/>
							</logic:notEmpty>
							</td>
							
							<td>&nbsp; </td>
							
							--%></tr>
							</logic:iterate>
							</logic:notEmpty>
							</table>
							</td>
							</tr>
							</table>
							</td>
							</tr>
							
							<%--<tr height="10px">
							<td colspan="3"></td>
							</tr>
							
							
							--%>
							<tr><td>&nbsp;</td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td colspan="3">
							<table width="100%">
							<tr>
							
							<td  align="left" width="40%" class="bold"><br></br>
							<FONT size="2" style="font-family: serif">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Signature of the Candidate</FONT><br>
							
							</td>
							<%--<td  align="center" width="30%"></td>
							<td align="right"  width="30%"><img src="images/Principal.jpg" width="130px" height="50px" /></td>
							
							
							
							--%></tr>
							<tr><td>&nbsp;</td></tr>
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
							<table width="100%">
							<%--<tr>
							<td  align="left" width="50%"><FONT size="1" style="font-family: serif"><br></br><br></br>
								Seal and Signature of the Head of the Department </FONT>
							</td>
							<td  align="center" width="20%"><br></br><br></br><FONT size="1" style="font-family: serif">(Seal)</font></td>
							<td  align="right" width="30%"><img src="images/COEFinal.jpg" width="130px" height="50px" /></td>
							</tr>
							--%></table></td></tr>
							
							
							<%--<tr>
							<td colspan="3">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Signature of The Candidate:
							</td>
							</tr>
							
							
							
							

							
							<tr height="10px">
							<td colspan="3" class="row-print-desc">
							<logic:notEmpty name="loginform" property="description">
								<c:out value="${loginform.description}" escapeXml="false"></c:out>
							</logic:notEmpty>
							</td>
							</tr>
							
							
							
							fv<%--
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
