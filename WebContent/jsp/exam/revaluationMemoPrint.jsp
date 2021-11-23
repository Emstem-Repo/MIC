<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib prefix="pd4ml" uri="http://pd4ml.com/tlds/pd4ml/2.6"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
 <%@ page import="java.util.*" %>
 <%@page import="java.text.SimpleDateFormat"%>

<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<link rel="stylesheet" href="css/StudentLayout1Styles.css">

<style>
@media print {
*{-webkit-print-color-adjust: exact;}
 	@page{
 		margin: 0.5mm;
 	}
}
.row-print {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: normal;
	color: #000000;
	background-color: #FFFFFF;
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
	var url = "StudentLoginAction.do?method=printHallTicket";
	myRef = window
			.open(url, "Hall Ticket",
					"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");

}

</script>
<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />
<div style="margin-right:30px; margin-left:30px">
<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
							<tr>
								<td width="80%" align="center">
		  <img width="600" height="100"  src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/LogoServlet") %>' alt="Logo not available">
								</td>
								<!--<td width="20%" align="right">
								<img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" />
								</td>
							--></tr>
							
							<tr><td>&nbsp;</td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td class="row-print" width="95" colspan="6" align="center"><font size="3"><u>REVALUATION RESULT MEMO</u></font></td>																			
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td align="right" colspan="2" class="row-print" width="100%">Thiruvananthapuram<br>
							Date: <%
									Date td = new Date();
							String b = new String("");
							SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY	");
							b = format.format(td);
							out.println(b);    %>
							</td>							
							</tr>
							<tr><td>&nbsp;</td></tr>							
							<tr>
								<td colspan="2">
									<table border="1" width="100%" rules="rows">
									<tr>
										<td class="row-print" width="20%">Name of Programme</td>
										<td class="row-print" width="1%">:</td>
										<td class="row-print"  width="29%"><bean:write name="loginform" property="programName"/></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td class="row-print" width="95">Name of Student</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="studentName"/></td>										
										<td></td>									
										<td></td>									
										<td></td>									
									</tr>
									
									
									<tr>
										<td class="row-print" width="95">Register No</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="regNo"/></td>										
										<td></td>									
										<td></td>									
										<td></td>									
									</tr>
									<tr>
										<td class="row-print" width="95">Semester</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="schemeNo"/>&nbsp;(<bean:write name="loginform" property="examType"/>)</td>										
										<td></td>									
										<td></td>									
										<td></td>									
									</tr>
									<tr>
										<td class="row-print" width="95"> Month & Year of Exam</td>
										<td class="row-print"> :</td>
										<td class="row-print"><bean:write name="loginform" property="month"/></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									
									</table>
									
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td class="row-print" width="95" colspan="6" align="center"><font size="3">REVALUATION RESULT</font></td>																			
							</tr>
							<tr><td>&nbsp;</td></tr>							
							<tr>
								<td colspan="2">
									<table width="100%" style="border: 1px solid black; " rules="cols">
										<tr height="21px">

						                        <td rowspan="2" class="row-print" align="center" width="15%" style="border: solid thin">
						                                    Course Code
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="25%" style="border: solid thin">
						                                   Course Name
						                        </td>
						                         <td rowspan="2" align="center" class="row-print" width="15%" style="border: solid thin">
						                                    Result
						                        </td>
						                        <td colspan="2" align="center" class="row-print" width="45%" style="border: solid thin">
						                                 Marks Awarded in Revaluation</td>
						            </tr>
						            <tr>
						            
						            	<td align="center">ESE</td>
						            	<td align="center">In Words</td>
						            </tr>
						            <logic:iterate id="memo" property="revaluationMemoList" name="loginform">
						            <tr height="21px">

						                        <td class="row-print" align="center" width="15%" style="border: solid thin">
						                                   <bean:write name="memo" property="subjectCode"/>
						                        </td>
						                        <td class="row-print" align="center" width="25%" style="border: solid thin">
						                                  <bean:write name="memo" property="className"/>
						                        </td>
						                         <td align="center" class="row-print" width="15%" style="border: solid thin">
						                                  <bean:write name="memo" property="comment"/>
						                        </td>
						                        <td align="center" class="row-print" width="15%" style="border: solid thin">
						                        <logic:equal value="CHANGE" name="memo" property="comment">
						                                 <bean:write name="memo" property="newMarks"/>
						                        </logic:equal>         
						                                 </td>
						                         <td align="center" class="row-print" width="45%" style="border: solid thin">
						                          <logic:equal value="CHANGE" name="memo" property="comment">
						                                 <bean:write name="memo" property="newMark1"/>
						                           </logic:equal>             
						                                 </td>
						            </tr>
						            </logic:iterate>
									
									</table>
								</td>
							</tr>
							<!-- <tr height="20px"><td>&nbsp;</td></tr>	
							<tr>
							<td align="center" colspan="2" class="row-print" width="100%">
								The changed mark will be updated in the College Website.Revised marklist will be issued as and when the
								original marklist is surrendered at the office of the Controller of Examinations.
							</td>							
							</tr>
							<tr height="20px"><td>&nbsp;</td></tr>	
							<tr>
							<td align="right" colspan="2" class="row-print" width="100%">
								Controller of Examination
							</td>							
							</tr> -->
							
							</table>
</div>						
</html:form>
<script type="text/javascript">window.print();</script>
