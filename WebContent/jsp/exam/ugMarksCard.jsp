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
function submitMarksCard(){
	document.getElementById("method").value="submitRevaluation";
	document.loginform.submit();
}
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=initMarksCard";
}
function resetErrorMsgs() {	
	resetFieldAndErrMsgs();
}
function printMarksCard(){
	var url = "StudentLoginAction.do?method=printMarksCard";
	var browserName=navigator.appName; 
		 myRef = window.open(url,"MarksCard","left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
}
</script>

<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printMarksCard" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="marksCardType" name="loginform" value="regUg"/>

	
	<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin.studentlogin" />
			<span class="Bredcrumbs">&gt;&gt; <bean:message key="knowledgepro.exam.publishHM.MarksCard" /> &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/st_Tright_03_01.gif"></td>
					<td width="100%" background="images/st_Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader"><bean:message key="knowledgepro.exam.publishHM.MarksCard" /></strong></div>
					</td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>
							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
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
							</table></td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						<tr>
							<td height="19" valign="top" background="images/st_left.gif"></td>
							<td valign="top" class="news"><logic:equal value="true"
								name="loginform" property="checkRevaluation">
								<div id="info">Revaluation /Re-totaling facility is available Online till:<bean:write name="loginform" property="revDate"/> </div>
							</logic:equal></td>
							<td valign="top" background="images/st_right.gif"></td>
						</tr>

						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white" style="background-repeat:no-repeat; background-position: center; padding-bottom: 100px; background-image: url('images/studentLogin/watermark.gif'); background-size: 500px 300px;">
							<tr>
								<td width="80%" align="center">
								<img src='<%=CMSConstants.LOGO_URL%>'  height="100" width="600" />
								</td>
						</tr>
						
							<tr><td>&nbsp;</td></tr>
							<tr>
							<td align="right" colspan="2" class="row-print" width="100%">Thiruvananthapuram<br>
							Date: <bean:write name="loginform" property="marksCardTo.date"/>
							</td>							
							</tr>
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
										<td class="row-print" style="border-right:solid thin"><bean:write name="loginform" property="marksCardTo.semNo"/></td>		
										<td class="row-print" width="10%"> Register No</td>
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
								<td class="row-print" width="95" colspan="6" align="center"><font size="3">Provisional Marks cum Grade Statement</font></td>																			
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
						                        </td>
						                         <td rowspan="2" align="center" class="row-print" width="6%" style="border: solid thin">
						                                    Credits<br>(C)
						                        </td>
						                        <td colspan="3" align="center" class="row-print" width="15%" style="border: solid thin">
						                                    Marks Awarded
						                        </td><td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Grade Point<br>(GP)(m/10)
						                        </td>
						                        <td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Grade Awarded
						                        </td>
						                        <td rowspan="2" align="center" class="row-print" width="4%" style="border: solid thin">
						                                    Credit Point<br>(C*GP)
						                        </td>
						            </tr>
						            <tr height="21px">
						                        <td align="center" class="row-print" style="border: solid thin">
						                                    ESE<br>(Max. <bean:write name="loginform" property="marksCardTo.maxESEMarks"/>)
						                        </td>
						                        <td align="center" class="row-print" style="border: solid thin">
						                                    CE<br>(Max. <bean:write name="loginform" property="marksCardTo.maxCIAMarks"/>)
						                        </td><td align="center" class="row-print" style="border: solid thin">
						                                    TOTAL<br>(Max. <bean:write name="loginform" property="marksCardTo.maxTotalMarks"/>)(m)
						                        </td>               
						            </tr>
						            <c:set var="slnocount" value="0" />
									<logic:notEmpty name="loginform" property="marksCardTo.subMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.subMap" indexId="count">
												<c:choose>
														<c:when test="${to.ciaMaxMarks == loginform.marksCardTo.maxCIAMarks}">
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
														</c:when>
												</c:choose>
											
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<c:if test="${to.theory!=null && to.theory==true && to.displaySubject==true}">
											<tr height="21px">
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
										
												<td align="center" style="font-size: 12px">
												<!--  	<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>Max. <bean:write name="to" property="eseMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty> -->
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<td align="center" style="font-size: 12px">
							
												<logic:notEmpty name="to" property="ciaMaxMarks">
													<logic:notEqual value="-" name="to" property="ciaMaxMarks">
													<c:choose>
														<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
															<hr><b>Max. <bean:write name="to" property="ciaMaxMarks"/></b><br><hr> 
														</c:when>
													</c:choose>
													</logic:notEqual>
												</logic:notEmpty>
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> 
												</td>
												
												
								
												<td align="center" style="font-size: 12px">
																	
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
												</tr>
											</c:if>
											
											
									<c:if test="${to.practical!=null && to.practical==true && to.displaySubject==true}">
												
									<c:choose>
									<c:when test="${to.practicalTotalMaxMarks != loginform.marksCardTo.maxTotalMarks}">		
										<tr height="15px">
												
												
											<td style="font-size: 12px"> </td>
												<td style="font-size: 12px"></td>
												<td align="center" style="font-size: 12px"></td>
												
												
												
												<td align="center" style="font-size: 12px">
													<logic:notEmpty name="to" property="practicalEseMaxMarks">
												<logic:notEqual value="-" name="to" property="practicalEseMaxMarks">
												<c:choose>
													<c:when test="${to.practicalEseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>Max. <bean:write name="to" property="practicalEseMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												</td>
												
												<td align="center" style="font-size: 12px">
									
														<logic:notEmpty name="to" property="practicalCiaMaxMarks">
														<logic:notEqual value="-" name="to" property="practicalCiaMaxMarks">
														<c:choose>
															<c:when test="${to.practicalCiaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
																<hr><b>Max. <bean:write name="to" property="practicalCiaMaxMarks"/></b><br><hr> 
															</c:when>
														</c:choose>
														</logic:notEqual>
														</logic:notEmpty>
												 </td>
									
							
									
											<td align="center" style="font-size: 12px">
											
														<logic:notEmpty name="to" property="practicalTotalMaxMarks">
												<logic:notEqual value="-" name="to" property="practicalTotalMaxMarks">
												<c:choose>
													<c:when test="${to.practicalTotalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr><b>Max. <bean:write name="to" property="practicalTotalMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>	
												</logic:notEqual>
												</logic:notEmpty>
											</td>
												
												<td  style="font-size: 12px" align="center"> </td>
												<td  style="font-size: 12px" align="center"> </td>
												<td  style="font-size: 12px" align="center">  </td>
											</tr>
											</c:when>
												</c:choose>
											
											<tr height="21px">
												
												
											<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												
												
												
												<td align="center" style="font-size: 12px">
								
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
												
												<td align="center" style="font-size: 12px">
									
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td>
									
							
									
											<td align="center" style="font-size: 12px">
						
											 <logic:equal value="-" name="to" property="practicalTotalMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalTotalMaxMarks"> <bean:write name="to" property="practicalTotalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalTotalMarksAwarded">- </logic:empty> </td>
												
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGradePnt"/><logic:empty name="to" property="practicalGradePnt">- </logic:empty> </td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="practicalGrade"/><logic:empty name="to" property="practicalGrade">- </logic:empty> </td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/><logic:empty name="to" property="creditPoint">- </logic:empty> </td>
											</tr>
											
											
											</c:if>
											<c:if test="${to.theoryAndPractical!=null && to.theoryAndPractical==true && to.displaySubject==true}">
											<tr height="21px">
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
										
												<td align="center" style="font-size: 12px">
												<table rules="cols">
												<tr>
												<td>
													<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>&nbsp;&nbsp;T <br> Max. <bean:write name="to" property="eseMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												</td>
												<td>
												<logic:notEmpty name="to" property="esePracticalMaxMarks">
												<logic:notEqual value="-" name="to" property="esePracticalMaxMarks">
												<c:choose>
													<c:when test="${to.esePracticalMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>&nbsp;&nbsp;P <br>Max. <bean:write name="to" property="esePracticalMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												</td>
												</tr>
												<tr>
										     	<td><logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<td><logic:equal value="-" name="to" property="esePracticalMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="esePracticalMaxMarks"><bean:write name="to" property="esePracticalMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="esePracticalMarksAwarded">- </logic:empty></td>
										       </tr>
										       </table>
										        </td>
												
												<td align="center" style="font-size: 12px">
							
												<logic:notEmpty name="to" property="ciaMaxMarks">
													<logic:notEqual value="-" name="to" property="ciaMaxMarks">
													<c:choose>
														<c:when test="${to.ciaMaxMarks != loginform.marksCardTo.maxCIAMarks}">
															<hr><b>Max. <bean:write name="to" property="ciaMaxMarks"/></b><br><hr> 
														</c:when>
													</c:choose>
													</logic:notEqual>
												</logic:notEmpty>
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> 
												</td>
												
												
								
												<td align="center" style="font-size: 12px">
																	
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
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
										
												<td align="center" style="font-size: 12px">
									
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<td align="center" style="font-size: 12px">
									
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td>
										
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
										
												<td align="center" style="font-size: 12px">
											
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td  style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
									</tr>
											</c:if>
											<c:if test="${to.practical!=null && to.practical==true && to.displaySubject==true}" >
											<tr height="21px">
												
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
												
												<td align="center" class="row-print">
											
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
												
												<td align="center" style="font-size: 12px">
									
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td>
									
												<td align="center" style="font-size: 12px">
									
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
											
																								<td align="center" class="row-print">
										
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<td align="center" style="font-size: 12px">
										
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td>
											
												<td align="center" class="row-print"><logic:equal value="0" name="to" property="credits">-</logic:equal> <logic:notEqual value="00" name="to" property="credits"><bean:write name="to" property="credits"/></logic:notEqual> <logic:empty name="to" property="credits">- </logic:empty></td>
											
												<td align="center" style="font-size: 12px">
											
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="theoryGradePnt"/> <logic:empty name="to" property="theoryGradePnt">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="grade"/> <logic:empty name="to" property="grade">- </logic:empty></td>
												<td style="font-size: 12px" align="center"> <bean:write name="to" property="creditPoint"/> <logic:empty name="to" property="creditPoint">- </logic:empty></td>
												</tr>
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
									
												<logic:equal value="-" name="to" property="practicalEseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalEseMaxMarks"> <bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="practicalEseMarksAwarded">- </logic:empty></td>
											
												<td align="center" style="font-size: 12px">
								
												<logic:equal value="-" name="to" property="practicalCiaMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="practicalCiaMaxMarks"> <bean:write name="to" property="practicalCiaMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="practicalCiaMarksAwarded">- </logic:empty> </td>
											
												<td align="center" style="font-size: 12px"><logic:equal value="0" name="to" property="practicalCredits">-</logic:equal> <logic:notEqual value="00" name="to" property="practicalCredits"><bean:write name="to" property="practicalCredits"/></logic:notEqual> <logic:empty name="to" property="practicalCredits">- </logic:empty></td>
											
												<td align="center" style="font-size: 12px">
											
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
									<td align="center" colspan="2" style="border: solid thin;;font-size: 12px"><b>SCPA 
										<bean:write name="loginform" property="marksCardTo.sgpa"/></b>
										</td>
										<td align="center" colspan="4" style="border: solid thin;;font-size: 12px"><b>Grade Awarded: 
										<bean:write name="loginform" property="marksCardTo.totalGrade"/></b></td>
										</tr>
										
										
									</table>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td style="font-size: 8px"><b>
							&nbsp;ESE - End Semester Examination, CE-Continuous  Evaluation<br>
							&nbsp;SCPA - Semester Credit Point Average(Total Credit Point/Total Credit)
							</b></td></tr>
							
							<tr height="21px">
							<td colspan="2"  class="row-print">
							<logic:notEmpty name="loginform" property="description1">
								<c:out value="${loginform.description1}" escapeXml="false"></c:out>
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
							<div align="right">
							<logic:equal value="true" name="loginform" property="checkRevaluation">
							<html:button property="" styleClass="formbutton" onclick="submitMarksCard()">
								Revaluation/Re-totaling
							</html:button>
							</logic:equal>
							<html:button property="" styleClass="formbutton" onclick="printMarksCard()">
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
</html:form>
<script type="text/javascript">
if(document.getElementById("info")!=null){	
	$("#info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
}
</script> 