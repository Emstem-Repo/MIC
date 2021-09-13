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
<script language="JavaScript" >
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=returnHomePage";
}
function resetErrorMsgs() {	
	resetFieldAndErrMsgs();
}
function printMarksCard(){
	var url = "StudentLoginAction.do?method=printSupMarksCard";
	var browserName=navigator.appName; 
		 myRef = window.open(url,"MarksCard","left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
}

</script>

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
	font-weight: bold;
    position: relative;
    background: transparent; 
}
</style>

<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printHallTicket" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />
<div style="margin-right:30px; margin-left:30px">
<table width="100%" height="100%" cellspacing="1" cellpadding="2" class="row-white" style="background-repeat:no-repeat; background-position: center; padding-bottom: 100px; background-image: url('images/studentLogin/watermark.gif'); background-size: 500px 300px;">
							<tr>
								<td colspan="2" align="center">
		  <img width="600" height="100"  src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/LogoServlet") %>' alt="Logo not available">
								</td>
								<!--<td width="20%" align="right">
								<img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" />
								</td>
							--></tr>
							<!--<tr height="21px">
								<td  align="center" width="80%" class="row-print"> <b> STATEMENT OF MARKS</b> </td>
								<td>
								<bean:define id="reg" name="loginform" property="marksCardTo.regNo"></bean:define>
								<img src='<%=CMSConstants.barbecueLink %><%=reg %>' height="21" width="118"   />
								</td>
							</tr>
							-->
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td align="right" colspan="2" class="row-print" width="100%">Thiruvananthapuram<br>
							Date: <bean:write name="loginform" property="marksCardTo.date"/>
							</td>							
							</tr>
							<tr><td>&nbsp;</td></tr>							
							<tr>
								<td colspan="2">
									<table width="100%" border="1" rules="rows">
									<tr>
										<td class="row-print" width="20%">Name of Programme</td>
										<td class="row-print" width="1%">:</td>
										<td class="row-print"  width="29%"><bean:write name="loginform" property="marksCardTo.courseName"/></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.semType"/></td>
										<td class="row-print">:</td>
								       <td class="row-print" style="border-right:solid thin"><bean:write name="loginform" property="marksCardTo.semNo"/>&nbsp; Improvement</td>		
										<td class="row-print" width="15%"> Register No</td>
										<td class="row-print" width="1%">:</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.regNo"/></td>								
									</tr>
									<tr>
										<td class="row-print" width="95"> Month & Year of Exam</td>
										<td class="row-print"> :</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.monthYear"/></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr>
										<td class="row-print" width="95">Name of Student</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.studentName"/></td>										
										<td></td>									
										<td></td>									
										<td></td>									
									</tr>
									</table>
									
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td class="row-print" width="95" colspan="6" align="center"><font size="3" style="font-weight: bold">Provisional Marks cum Grade Statement</font></td>																			
							</tr>
							<tr><td>&nbsp;</td></tr>							
							<tr>
								<td colspan="2">
									<table width="100%" style="border: 1px solid black; " rules="cols">
										<tr height="21px">

						                        <td rowspan="2" class="row-print" align="center" width="3%" style="border: solid thin">
						                                    Course Code
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="25%" style="border: solid thin">
						                                    Course Title
						                        </td><!--
						                        <td rowspan="2" class="row-print" align="center" width="5%">
						                                    TYPE
						                        </td>
						                        -->
						                         <td rowspan="2" align="center" class="row-print" width="6%" style="border: solid thin">
						                                    Credits<br>(C)
						                        </td>
						                        <td colspan="3" align="center" class="row-print" width="15%" style="border: solid thin">
						                                    Marks Awarded
						                        </td><!--
						                        <td colspan="2" align="center" class="row-print" width="15%">
						                                    ATT
						                        </td>
						                       <td colspan="1" align="center" class="row-print" width="17%">
						                                    ESE
						                        </td>
						                        <td  colspan="1" align="center" class="row-print" width="15%">
						                                    TOTAL
						                        </td>
						                       
						                         --><td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Grade Point<br>(GP)(m/10)
						                        </td>
						                        <td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Grade Awarded
						                        </td>
						                        <td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Credit Point<br>(C*GP)
						                        </td>
						            </tr>
						            <tr height="21px"><!--
						                        <td align="center" class="row-print">
						                                    MAX MARKS
						                        </td>
						                        -->
						                        <td align="center" class="row-print" style="border: solid thin">
						                                    ESE<br>(Max. <bean:write name="loginform" property="marksCardTo.maxESEMarks"/>)
						                        </td>
						                        <td align="center" class="row-print" style="border: solid thin">
						                                    CE<br>(Max. <bean:write name="loginform" property="marksCardTo.maxCIAMarks"/>)
						                        </td><!--
						                         <td align="center" class="row-print">
						                                    MAX MARKS
						                        </td>
						                        <td align="center" class="row-print">
						                                    MARKS AWARDED
						                        </td>
						                        <td align="center" class="row-print">
						                                    MAX MARKS
						                        </td>
						                        <td align="center" class="row-print">
						                                    MIN MARKS
						                        </td>-->
						                        
						                        <!--<td align="center" class="row-print">
						                                    MAX MARKS
						                        </td>
						                        --><td align="center" class="row-print" style="border: solid thin">
						                                    TOTAL<br>(Max. <bean:write name="loginform" property="marksCardTo.maxTotalMarks"/>)(m)
						                        </td>               
						            </tr>
						            <c:set var="slnocount" value="0" />
									<logic:notEmpty name="loginform" property="marksCardTo.subMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.subMap" indexId="count">
											<tr height="21px">
												<td></td>
												<td style="font-size: 12px"></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<c:if test="${to.theory!=null && to.theory==true && to.displaySubject==true}">
											<tr height="21px">
<%--												<td align="center" class="row-print"> <c:out value="${slnocount}" /> </td>--%>
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
<!--												<td align="center" class="row-print"> <bean:write name="to" property="type"/> </td>-->
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												<!--<td align="center" class="row-print"><logic:equal value="00" name="to" property="ciaMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMaxMarks"/></logic:notEqual> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty>
												 </td>
												-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="eseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose> -->
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMaxMarks"/></logic:notEqual> <logic:empty name="to" property="totalMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="ciaMaxMarks">
														<hr>Max. <bean:write name="to" property="ciaMaxMarks"/><br><hr> 
													</logic:notEqual>														
													</c:when>
												</c:choose>  -->
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> 
												</td>
												
												<!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="attMaxMarks"><bean:write name="to" property="attMaxMarks"/></logic:notEqual> <logic:empty name="to" property="attMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="attMaxMarks"><bean:write name="to" property="attMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="attMarksAwarded">- </logic:empty></td>
												
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="eseMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"> <bean:write name="to" property="eseMinMarks"/> <logic:empty name="to" property="eseMinMarks">- </logic:empty></td>
												-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="totalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>		-->										
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
												<!--<%String check="row-print"; %>
												 <logic:notEmpty name="to" property="status">
												 	<%check="ColorGreen"; %>
												 </logic:notEmpty>
												<td class="<%=check %>">
												 <bean:write name="to" property="status"/> </td>
											--></tr>
											</c:if>
											<c:if test="${to.practical!=null && to.practical==true && to.displaySubject==true}">
											<tr height="21px"><!--
												<td align="center" style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												<c:out value="${slnocount}" />
												</c:if>
												</td>
												<td style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												<bean:write name="to" property="name"/>
												</c:if>
												</td>  	-->
												
												
											<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												
												
												
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.practicalEseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="practicalEseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalTotalMaxMarks">- </logic:empty></td>-->
												
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalCiaMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.practicalCiaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="practicalCiaMaxMarks">
														<hr>Max. <bean:write name="to" property="practicalCiaMaxMarks"/><br><hr> 
													</logic:notEqual>														
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalAttMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalAttMarksAwarded">- </logic:empty> </td>
												-->
												<!--<td align="center" class="row-print"><logic:equal value="0" name="to" property="practicalCredits">-</logic:equal> <logic:notEqual value="00" name="to" property="practicalCredits"><bean:write name="to" property="practicalCredits"/></logic:notEqual> <logic:empty name="to" property="practicalCredits">- </logic:empty></td>
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalEseMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><bean:write name="to" property="practicalEseMinMarks"/> <logic:empty name="to" property="practicalEseMinMarks">- </logic:empty> </td>
												-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.practicalTotalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="practicalTotalMaxMarks"/><br><hr> 
													</c:when> 
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalTotalMarksAwarded">- </logic:empty> </td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGradePnt"/><logic:empty name="to" property="practicalGradePnt">- </logic:empty> </td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGrade"/><logic:empty name="to" property="practicalGrade">- </logic:empty> </td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/><logic:empty name="to" property="creditPoint">- </logic:empty> </td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
											
										
										
										<logic:notEmpty name="loginform" property="marksCardTo.nonElectivesubMap">
										<c:set var="slnocount" value="${slnocount + 1 }" />
										<logic:iterate id="map" name="loginform" property="marksCardTo.nonElectivesubMap" indexId="count">
										
											<tr height="21px">
												<td style="font-size: 12px" align="center"><c:out value="${slnocount}" /></td>
												<td colspan="14" style="font-size: 12px"> <bean:write name="map" property="key"/>/s </td>
											</tr>
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											
											<c:if test="${to.theory!=null && to.theory==true && to.displaySubject==true}" >
											<tr height="21px">
												<td align="center" style="font-size: 12px"> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
<!--												<td align="center" class="row-print"> <bean:write name="to" property="type"/> </td>-->
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												<!--<td align="center" class="row-print"><logic:equal value="00" name="to" property="ciaMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMaxMarks"/></logic:notEqual> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty>
												 </td>
												-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.eseMaxMarks}">
														<hr>Max. <bean:write name="to" property="eseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMaxMarks"/></logic:notEqual> <logic:empty name="to" property="totalMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="ciaMaxMarks">
														<hr>Max. <bean:write name="to" property="ciaMaxMarks"/><br><hr> 
													</logic:notEqual>														
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="attMaxMarks"><bean:write name="to" property="attMaxMarks"/></logic:notEqual> <logic:empty name="to" property="attMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="attMaxMarks"><bean:write name="to" property="attMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="attMarksAwarded">- </logic:empty></td>
												-->
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="eseMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"> <bean:write name="to" property="eseMinMarks"/> <logic:empty name="to" property="eseMinMarks">- </logic:empty></td>
												-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="totalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
												<!--<%String check="row-print"; %>
												 <logic:notEmpty name="to" property="status">
												 	<%check="ColorGreen"; %>
												 </logic:notEmpty>
												<td class="<%=check %>">
												 <bean:write name="to" property="status"/> </td>
											--></tr>
											</c:if>
											<c:if test="${to.practical!=null && to.practical==true && to.displaySubject==true}" >
											<tr height="21px"><!--
												<td align="center" style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												
												</c:if>
												</td>
												<td style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												<bean:write name="to" property="name"/>
												</c:if>
												</td>  
												<td align="center" style="font-size: 12px"> <bean:write name="to" property="subjectType"/> </td>-->
												
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
<!--												<td align="center" class="row-print"> <bean:write name="to" property="type"/> </td>-->
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												
												<td align="center" class="row-print">
												<!--<c:choose>
													<c:when test="${to.practicalEseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="practicalEseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalTotalMaxMarks">- </logic:empty></td>-->
												
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalCiaMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!--<c:choose>
													<c:when test="${to.practicalCiaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="practicalCiaMaxMarks">
														<hr>Max. <bean:write name="to" property="practicalCiaMaxMarks"/><br><hr>
													</logic:notEqual>														 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalAttMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalAttMarksAwarded">- </logic:empty> </td>
												-->
												<!--<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="practicalCredits">-</logic:equal> <logic:notEqual value="00" name="to" property="practicalCredits"><bean:write name="to" property="practicalCredits"/></logic:notEqual> <logic:empty name="to" property="practicalCredits">- </logic:empty></td>
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalEseMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><bean:write name="to" property="practicalEseMinMarks"/> <logic:empty name="to" property="practicalEseMinMarks">- </logic:empty> </td>
												-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.practicalTotalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="practicalTotalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	 -->
												<logic:equal value="-" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalTotalMarksAwarded">- </logic:empty> </td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGradePnt"/> <logic:empty name="to" property="practicalGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGrade"/> <logic:empty name="to" property="practicalGrade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										
										
										
										
										<logic:notEmpty name="loginform" property="marksCardTo.addOnCoursesubMap">
										<c:set var="slnocount" value="${slnocount + 1 }" />
										<logic:iterate id="map" name="loginform" property="marksCardTo.addOnCoursesubMap" indexId="count">
										
											<tr height="21px">
												<td style="font-size: 12px" align="center"> <c:out value="${slnocount}" /></td>
												<td colspan="14" style="font-size: 12px"> <bean:write name="map" property="key"/>/s </td>
											</tr>
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											
											<c:if test="${to.theory!=null && to.theory==true && to.displaySubject==true}" >
											<tr height="21px">
												<td align="center" style="font-size: 12px"> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"> <bean:write name="to" property="type"/> </td>
												<!--<td align="center" class="row-print"><logic:equal value="00" name="to" property="ciaMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMaxMarks"/></logic:notEqual> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty>
												 </td>
												-->
																								<td align="center" class="row-print">
												<!-- <c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="eseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMaxMarks"/></logic:notEqual> <logic:empty name="to" property="totalMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="ciaMaxMarks">
														<hr>Max. <bean:write name="to" property="ciaMaxMarks"/><br><hr> 
													</logic:notEqual>														
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="00" name="to" property="attMaxMarks"><bean:write name="to" property="attMaxMarks"/></logic:notEqual> <logic:empty name="to" property="attMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="attMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="attMaxMarks"><bean:write name="to" property="attMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="attMarksAwarded">- </logic:empty></td>
												-->
												<td align="center" class="row-print"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="eseMaxMarks">- </logic:empty></td>
												<td align="center" class="row-print"> <bean:write name="to" property="eseMinMarks"/> <logic:empty name="to" property="eseMinMarks">- </logic:empty></td>
												-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="totalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
												<!--<%String check="row-print"; %>
												 <logic:notEmpty name="to" property="status">
												 	<%check="ColorGreen"; %>
												 </logic:notEmpty>
												<td class="<%=check %>">
												 <bean:write name="to" property="status"/> </td>
											--></tr>
											</c:if>
											<c:if test="${to.practical!=null && to.practical==true && to.displaySubject==true}" >
											<tr height="21px">
												<td align="center" style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												
												</c:if>
												</td>
												<td style="font-size: 12px">
												<c:if test="${to.theory!=null && to.theory==false}">
												<bean:write name="to" property="name"/>
												</c:if>
												</td>  
												<td align="center" style="font-size: 12px"> <bean:write name="to" property="subjectType"/> </td>
																								<td align="center" class="row-print">
												<!-- <c:choose>
													<c:when test="${to.practicalEseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr>Max. <bean:write name="to" property="practicalEseMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalTotalMaxMarks">- </logic:empty></td>-->
												
<!--												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMaxMarks"/></logic:notEqual>  <logic:empty name="to" property="practicalCiaMaxMarks">- </logic:empty></td>-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.practicalCiaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
													<logic:notEqual value="-" name="to" property="practicalCiaMaxMarks">
														<hr>Max. <bean:write name="to" property="practicalCiaMaxMarks"/><br><hr>
													</logic:notEqual>														 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalAttMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><logic:equal value="-" name="to" property="practicalAttMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalAttMaxMarks"> <bean:write name="to" property="practicalAttMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalAttMarksAwarded">- </logic:empty> </td>
												-->
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="practicalCredits">-</logic:equal> <logic:notEqual value="00" name="to" property="practicalCredits"><bean:write name="to" property="practicalCredits"/></logic:notEqual> <logic:empty name="to" property="practicalCredits">- </logic:empty></td><!--
												<td align="center" class="row-print"><logic:equal value="00" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="00" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMaxMarks"/></logic:notEqual> <logic:empty name="to" property="practicalEseMaxMarks">- </logic:empty> </td>
												<td align="center" class="row-print"><bean:write name="to" property="practicalEseMinMarks"/> <logic:empty name="to" property="practicalEseMinMarks">- </logic:empty> </td>
												-->
												<td align="center" style="font-size: 12px">
												<!-- <c:choose>
													<c:when test="${to.practicalTotalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr>Max. <bean:write name="to" property="practicalTotalMaxMarks"/><br><hr> 
													</c:when>
												</c:choose>	-->
												<logic:equal value="-" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalTotalMarksAwarded">- </logic:empty> </td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGradePnt"/> <logic:empty name="to" property="practicalGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGrade"/> <logic:empty name="to" property="practicalGrade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
											</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										<!--<tr height="21px">
											<td colspan="10" align="center"  class="row-print">
												Total Marks (In Words): <bean:write name="loginform" property="marksCardTo.totalMarksInWord"/>
											</td>
											<td align="center" class="row-print">
											<bean:write name="loginform" property="marksCardTo.totalMaxmarks"/>
											</td>
											<td align="center" class="row-print">
											<bean:write name="loginform" property="marksCardTo.totalMarksAwarded"/>
											</td>
											<td colspan="2"></td>
										</tr>
										-->
										<tr height="21px">
										<td align="center"  colspan="2" style="border: solid thin;font-size: 12px">
											 Total
											</td>
											<td align="center" style="border: solid thin;font-size: 12px">
											 <bean:write name="loginform" property="marksCardTo.totalCredits"/>
											</td>
											<td style="border: solid thin;font-size: 12px"></td>
											<td style="border: solid thin;font-size: 12px"></td>
											<td style="border: solid thin;font-size: 12px"></td>
											<td style="border: solid thin;font-size: 12px"></td>
											<td align="center" style="border: solid thin;font-size: 12px"></td>
											<td align="center" style="border: solid thin;font-size: 12px"><bean:write name="loginform" property="marksCardTo.totalGradePoints"/></td>
										</tr>
										<tr height="21px">
											<td align="center"  colspan="3" style="border: solid thin;;font-size: 12px">
											 <b>Result: <bean:write name="loginform" property="marksCardTo.resultClass"/></b>
											</td>
											<!--<td colspan="3"  class="row-print">
											 Total Credits Awarded:<bean:write name="loginform" property="marksCardTo.totalCredits"/>
											</td>
											<td colspan="4" align="center" class="row-print">
												Grade Points Average :<bean:write name="loginform" property="marksCardTo.gradePoints"/>
											</td>
										--><td align="center" colspan="2" style="border: solid thin;;font-size: 12px"><b>SCPA 
										<bean:write name="loginform" property="marksCardTo.sgpa"/></b>
										</td>
										<td align="center" colspan="4" style="border: solid thin;;font-size: 12px"><b>Grade Awarded: 
										<bean:write name="loginform" property="marksCardTo.totalGrade"/></b></td>
										</tr>
										
										
									</table>
								</td>
							</tr>
							
							<tr><td style="font-size: 8px" width="50%"><b>
							&nbsp;ESE - End Semester Examination, CE-Continuous  Evaluation<br>
							&nbsp;SCPA - Semester Credit Point Average(Total Credit Point/Total Credit)
							</b></td>
							<td align="right" width="50%">
								    <b style="font-size: 14px">For Controller of Examinations</b>
							</td></tr>
							
							<%-- <tr height="21px">
							<td colspan="2"  class="row-print">
							<logic:notEmpty name="loginform" property="description1">
								<c:out value="${loginform.description1}" escapeXml="false"></c:out>
							</logic:notEmpty>
							</td>
							</tr>--%>
							
							</table>
</div>						
</html:form>
<script type="text/javascript">window.print();</script>