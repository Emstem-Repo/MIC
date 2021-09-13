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
<link rel="stylesheet" href="css/StudentLayout1Styles.css">

<style>
@media print {
 	@page{
 		margin: 6mm;
 	}
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

							<table width="100%" cellspacing="1" cellpadding="2" class="row-white"   style="background-repeat:no-repeat; background-position: center; padding-bottom: 100px; background-image: url('images/studentLogin/watermark.gif'); background-size: 500px 300px;">
							<tr>
								<td width="80%" align="center">
								<img src='<%=CMSConstants.LOGO_URL%>'  height="100" width="600" />
								</td>
								<!--<td width="20%" align="right">
								<img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" />
								</td>
							--></tr>
							
							<!--
							<tr height="25px">
								<td  align="center" width="80%" class="row-print"> <b> STATEMENT OF MARKS</b> </td>
								<td>
								<bean:define id="reg" name="loginform" property="marksCardTo.regNo"></bean:define>
								<img src='<%=CMSConstants.barbecueLink %><%=reg %>' height="21" width="118"   />
								</td>
							</tr>
							-->
							
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td align="right" colspan="2" class="row-print" width="100%"><b>Thiruvananthapuram<br>
							Date: <bean:write name="loginform" property="marksCardTo.date"/></b>
							</td>							
							</tr>							
							<tr>
								<td colspan="2">
									<table width="100%" border="1" rules="rows" class="watermark">
									
									<tr height="25px">
										<td class="row-print" width="15%" style="vertical-align: top;"><b>Name of the programme<br><br>Semester</b></td>
										<td class="row-print" width="1%" style="vertical-align: top;"><b>:<br><br><br>:</b></td>
										<td class="row-print" width="15%" style="vertical-align: top;"><b><bean:write name="loginform" property="marksCardTo.courseName"/>
										<br><br><bean:write name="loginform" property="marksCardTo.semNo"/></b></td>	
										<td class="row-print" style="border-left:solid thin" width="20%"> <b>Month & Year of Examination</b></td>
										<td class="row-print" width="1%"><b>:</b></td>
										<td class="row-print" width="15%"><b><bean:write name="loginform" property="marksCardTo.monthYear"/></b></td>									
										<td class="row-print" style="border-left:solid thin" width="15%"> <b>Register No</b></td>
										<td class="row-print" width="1%"><b>:</b></td>
										<td class="row-print" width="10%"><b><bean:write name="loginform" property="marksCardTo.regNo"/></b></td>
									</tr>
									<tr height="25px">
										<td class="row-print"><b>Name of Student</b></td>
										<td class="row-print"><b>:</b></td>
										<td class="row-print" style="border-right:solid thin" colspan="7"><b><bean:write name="loginform" property="marksCardTo.studentName"/></b></td>										
									</tr>									
									</table>
								</td>
							</tr>	
							<tr><td></td></tr>
							<tr>
								<td class="row-print" width="95" align="center" colspan="2"><font size="4"><b>Provisional Marks Statement</b></font></td>																			
							</tr>
							<tr><td></td></tr>						
							<tr>
								<td colspan="2">
									<table width="100%" style="border: 1px solid black; " rules="all">
									<tr height="25px"><!--

						                        <td rowspan="2" class="row-print" align="center" width="3%">
						                                    SI NO
						                        </td>
						                        -->
						                        <td rowspan="2" class="row-print" align="center" width="10%">
						                                    <b>Course Code</b>
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="45%">
						                                    <b>Course Title</b>
						                        </td><!--
						                        <td rowspan="2" class="row-print" align="center" width="10%">
						                                    TYPE
						                        </td>
						                        --><td align="center" class="row-print" width="45%" colspan="3">
						                                    <b>Marks Awarded</b>
						                        </td>
						                        <!--<td rowspan="2" class="row-print" align="center" width="5%">
						                                    CREDITS
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="5%">
						                                    GRADE
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="5%">
						                                    Status
						                        </td>
						            --></tr>
						            <tr height="25px"><!--
						                        <td class="row-print" align="center" >
						                                    MAX MARKS
						                        </td>
						                        --><td class="row-print" align="center" width="15%">
						                                    <b>CA<br><hr>Max. <bean:write name="loginform" property="marksCardTo.maxCIAMarks"/></b>
						                        </td><!--
						                        <td class="row-print" align="center" >
						                                    MAX MARKS
						                        </td>
						                        <td class="row-print" align="center">
						                                    MIN MARKS
						                        </td>
						                        --><td class="row-print" align="center" width="15%">
						                                    <b>ESA<br><hr>Max. <bean:write name="loginform" property="marksCardTo.maxESEMarks"/></b>
						                        </td><!--
						                        <td class="row-print" align="center">
						                                    MAX MARKS
						                        </td>
						
						                        --><td class="row-print" align="center" width="15%">
						                                    <b>Total<br>Max. <bean:write name="loginform" property="marksCardTo.maxTotalMarks"/></b>
						                        </td>               
						            </tr>
						            <c:set var="slnocount" value="0" />
										<logic:notEmpty name="loginform" property="marksCardTo.subMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.subMap">
											<!--<tr height="25px">
												<td></td>
												<td colspan="12" class="row-print"> <bean:write name="map" property="key"/> </td>
											</tr >
											--><logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<tr height="25px">
<!--												<td class="row-print" align="center"><c:out value="${slnocount}" /></td>-->
												<td style="font-size: 14px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 14px"> <bean:write name="to" property="name"/> </td>
<!--												<td class="row-print" align="center"> <bean:write name="to" property="type"/> </td>
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="ciaMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMaxMarks"/></logic:notEqual> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty>
												 </td>
												--><td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="ciaMaxMarks">
												<logic:notEqual value="-" name="to" property="ciaMaxMarks">
												<c:choose>
													<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
														<hr>Max. <bean:write name="to" property="ciaMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="eseMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"> <bean:write name="to" property="eseMinMarks"/> <logic:empty name="to" property="eseMinMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="eseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMaxMarks"/></logic:notEqual> <logic:empty name="to" property="totalMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="totalMaxMarks">
												<logic:notEqual value="-" name="to" property="totalMaxMarks">
												<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="totalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td><!--
												<td align="center" class="row-print"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												<td class="row-print" align="center"> <bean:write name="to" property="grade"/><logic:empty name="to" property="grade">-</logic:empty> </td>
												<%String check="row-print"; %>
												 <logic:notEmpty name="to" property="status">
												 	<%check="ColorGreen"; %>
												 </logic:notEmpty>
												<td class="<%=check %>">
												 <bean:write name="to" property="status"/> </td>
											--></tr>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										<c:set var="slnocount" value="0" />
										<logic:notEmpty name="loginform" property="marksCardTo.addOnCoursesubMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.addOnCoursesubMap">
											<!--<tr height="25px">
												<td></td>
												<td colspan="12" class="row-print"> <bean:write name="map" property="key"/> </td>
											</tr>
											--><logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<tr height="25px">
<!--												<td class="row-print" align="center"><c:out value="${slnocount}" /></td>-->
												<td style="font-size: 14px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 14px"> <bean:write name="to" property="name"/> </td>
<!--												<td class="row-print" align="center"> <bean:write name="to" property="type"/> </td>
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="ciaMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMaxMarks"/></logic:notEqual> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty>
												 </td>-->
												<td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="ciaMaxMarks">
												<logic:notEqual value="-" name="to" property="ciaMaxMarks">
												<c:choose>
													<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
														<hr>Max. <bean:write name="to" property="ciaMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="eseMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"> <bean:write name="to" property="eseMinMarks"/> <logic:empty name="to" property="eseMinMarks">- </logic:empty></td>
												-->
												<td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="eseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMaxMarks"/></logic:notEqual> <logic:empty name="to" property="totalMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 14px">
												
												<logic:notEmpty name="to" property="totalMaxMarks">
												<logic:notEqual value="-" name="to" property="totalMaxMarks">
												<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="totalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td><!--
												<td align="center" class="row-print"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												<td class="row-print" align="center"> <bean:write name="to" property="grade"/><logic:empty name="to" property="grade">-</logic:empty> </td>
												<%String check="row-print"; %>
												 <logic:notEmpty name="to" property="status">
												 	<%check="ColorGreen"; %>
												 </logic:notEmpty>
												<td class="<%=check %>">
												 <bean:write name="to" property="status"/> </td>
											--></tr>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										<logic:equal value="true" name="loginform" property="marksCardTo.hasPracticals">
										<tr height="25px" >
											<td align="center" style="font-size: 14px" colspan="2">
												Total of Practicals
											</td>
											<td align="center" style="font-size: 14px">
												<bean:write name="loginform" property="marksCardTo.practicalCIATotalMarks"/>
											</td>											
											<td align="center" style="font-size: 14px">
												<bean:write name="loginform" property="marksCardTo.practicalESATotalMarks"/>
											</td>
											
											<!--<td class="row-print" align="center" >
											<bean:write name="loginform" property="marksCardTo.totalMaxmarks"/>
											</td>
											--><td style="font-size: 14px" align="center">
											<bean:write name="loginform" property="marksCardTo.practicalTotalMarks"/>
											</td>
										</tr>
										</logic:equal>
										<tr height="25px">
											<td align="center" style="font-size: 14px" colspan="2">
												Group Total
											</td>
											<td align="center" style="font-size: 14px">
												<bean:write name="loginform" property="marksCardTo.grandTotalCIAMarks"/>
											</td>											
											<td align="center" style="font-size: 14px">
												<bean:write name="loginform" property="marksCardTo.grandTotalESAMarks"/>
											</td>
											
											<!--<td class="row-print" align="center" >
											<bean:write name="loginform" property="marksCardTo.totalMaxmarks"/>
											</td>
											--><td style="font-size: 14px" align="center">
											<bean:write name="loginform" property="marksCardTo.totalMarksAwarded"/>
											</td>
										</tr>
										<tr height="25px">
											<td colspan="2" align="center" class="row-print">
											 <b>Result: <bean:write name="loginform" property="marksCardTo.result"/></b>
											</td>
											<td colspan="3" align="center" class="row-print"><b>Grand Total: <bean:write name="loginform" property="marksCardTo.totalMarksAwarded"/></b></td>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr><td>&nbsp;</td></tr>
							<tr><td style="font-size: 8px"><b>
							&nbsp;ESA - End Semester Assessment, CA-Continuous  Assessment<br>
							</b></td></tr>
							
							<tr height="21px">
							<td colspan="2"  class="row-print-desc">
							<logic:notEmpty name="loginform" property="description1">
							<font size="6px">
								<c:out value="${loginform.description1}" escapeXml="false"></c:out>
							</font>
							</logic:notEmpty>
							</td>
							</tr>
							<tr><td>&nbsp;<br>&nbsp;</td></tr>							
							<tr>
								<td colspan="2" align="right">
								    <b style="font-size: 14px">For Controller of Examinations</b>
							    </td>
							</tr>
							</table>
							
</html:form>
<script type="text/javascript">window.print();</script>