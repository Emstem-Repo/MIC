f<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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
<link rel="stylesheet" href="css/styles.css">
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

</script>
<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />
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
<!--							<td align="center"><FONT size="2" style="font-family: serif"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Office Seal)</FONT></td>-->
<!--							<td colspan="2" align="right" class="bold">-->
<!--								<FONT size="2" style="font-family: serif">&nbsp;<br>Controller of Examinations</FONT>-->
<!--							</td>-->
							</tr>
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
							<td colspan="3" align="right" class="bold" style="border-top: solid thin">
							</td>
							</tr>
<!--							<tr><td>&nbsp;</td></tr>-->
<!--							<tr><td>&nbsp;</td></tr>-->
							
							
							<td colspan="3" align="center" class="bold"><FONT size="3" style="font-family: serif">GENERAL INSTRUCTIONS TO CANDIDATES:
							</FONT></td>
							</tr>
							<tr>
						      <td><FONT size="2">1.	Candidates should take their places in the examination hall atleast five minutes before the commencement of examination. Candidates presenting themselves more than half an hour after the appointed time will not be admitted to the Examination Hall. Candidates who are undoubtedly suffering from infectious diseases of any kind will not be admitted for the examination. Candidates should bring with them, to the Examination hall, on each day of examination, their hall tickets for inspection by the Superintendent/Invigilator on duty. </FONT>
							</td></tr>
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
						      <td><FONT size="2">2. Candidates are prohibited from writing upon their hall tickets/question papers. They are also prohibited from writing their names on any part of the answer books. They are also prohibited from writing the Register No. on any part of the answer book other than in the space provided in the facing sheet.  </FONT>
							</td></tr>
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
						      <td><FONT size="2">3.	Serial Numbers of all additional sheets used by the candidate should be noted on the second page of the main answer book in the space provided. All the pages of the main answer book and the additional sheets used by the candidate should be serially numbered and total number of the pages should be noted in the front page of the main answer book in the space provided.   </FONT>
							</td></tr>
<!--								<tr><td>&nbsp;</td></tr>	-->
								<tr>
						      <td><FONT size="2">4.	Calculators are permitted to be used in certain examinations. Details are available with the Chief Superintendent of examination.   </FONT>
							</td></tr>
<!--							<tr><td>&nbsp;</td></tr>	-->
								<tr>
						      <td><FONT size="2">5.	No candidate will be allowed to leave the examination hall before the expiry of atleast half an hour after question paper has been given and no candidate who leaves the room during the period allotted for a paper will be allowed to return within that period.    </FONT>
							</td></tr>
<!--							<tr><td>&nbsp;</td></tr>	-->
								<tr>
						      <td><FONT size="2">6. Candidates are prohibited from bringing into the examination hall any book or portion of book, slate, manuscript or paper of any description and from communicating with any person inside/outside the examination hall. <b>Possession and use of mobile phones inside the examination hall is strictly prohibited. Mobile phones kept in bags/pouches and kept outside the examination hall shall be switched off.</b> Any candidate found violating any of the rules in the conduct of examinations will be sent out of the hall forthwith and will not be permitted to appear for the remaining papers of the examination. Violation of rules in this regard may involve cancellation of the examination taken by the candidate and his rustication period will be decided by the College. CANDIDATES CAUGHT FOR MALPRACTICE AT EXAMINATION WILL HAVE TO REMIT ENQUIRY FEE as required by the College.   </FONT>
							</td></tr>
							
<!--							<tr><td>&nbsp;</td></tr>	-->
								<tr>
						      <td><FONT size="2">7.	When a candidate has finished writing answers, answer book shall be collected by the Superintendent on duty. The candidate shall stand up and remain standing until the Superintendent has received answer books. They should not leave the hall leaving the answer books in their seat.    </FONT>
							</td></tr>
<!--							<tr><td>&nbsp;</td></tr>	-->
								<tr>
						      <td><FONT size="2">8.	<b>Duplicate Hall Ticket</b>:Will be issued and when a request is received with prescribed fee.    </FONT>
							</td></tr>

<tr>
							<td align="right"><Font size="2" class="bold">  Sd/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </Font></td>
							</tr>
							<tr>
							<td align="right"><Font size="2" class="bold">Controller of Examination</Font></td>
							</tr>











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
</html:form>
<script type="text/javascript">window.print();</script>
