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
<link rel="stylesheet" href="css/styles.css">
<style>
.bold{
	font-weight: bold;
}
@page {
  size: A4;
  margin: 0;
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
							<td align="left" width="20%" class="bold"><font size="3">Register Number</font></td><td width="1%">:</td>
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
<!--							<tr><td>&nbsp;</td></tr>-->
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
							<td  align="left" width="40%" class="bold"><br></br>
							<FONT size="2" style="font-family: serif">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Signature of the Candidate</FONT><br>
						</td>
<!--							
							</tr>
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
							<td colspan="3" align="right" class="bold" style="border-top: solid thin">
							</td>
							</tr>
<!--							<tr><td>&nbsp;</td></tr>-->
<!--							<tr><td>&nbsp;</td></tr>-->
							<tr>
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
							
</html:form>
<script type="text/javascript">window.print();</script>
