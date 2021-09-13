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
	document.location.href = "StudentLoginAction.do?method=initMarksCard";
}
function resetErrorMsgs() {	
	resetFieldAndErrMsgs();
}
function printPass(){
	var url = "StudentLoginAction.do?method=printSupMarksCard";
	myRef = window
			.open(url, "MarksCard",
					"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");

}

</script>
<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printMarksCard" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="marksCardType" name="loginform" value="regPg"/>
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
								<div id="info">Revaluation /Re-totaling facility is available Online till:<bean:write name="loginform" property="revDate"/></div>
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
							<td align="right" colspan="2" class="row-print" width="100%">Thiruvananthapuram<br>
							Date: <bean:write name="loginform" property="marksCardTo.date"/>
							</td>							
							</tr>							
							<tr>
								<td colspan="2">	
									<table width="100%" border="1" rules="rows">									
									<tr height="25px">
										<td class="row-print">Name of the programme<br>Semester.<bean:write name="loginform" property="dontShowPracticals"/></td>
										<td class="row-print">:<br>:</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.courseName"/>
										<br><bean:write name="loginform" property="marksCardTo.semNo"/> &nbsp; Improvement </td>	
										<td class="row-print" style="border-left:solid thin"> Month & Year of Examination</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.monthYear"/></td>									
										<td class="row-print" style="border-left:solid thin"> Register No</td>
										<td class="row-print">:</td>
										<td class="row-print"><bean:write name="loginform" property="marksCardTo.regNo"/></td>
									</tr>
									<tr height="25px">
										<td class="row-print">Name of Student</td>
										<td class="row-print">:</td>
										<td class="row-print" style="border-right:solid thin" colspan="7"><bean:write name="loginform" property="marksCardTo.studentName"/></td>										
									</tr>									
									</table>
								</td>
							</tr>	
							<tr><td></td></tr>
							<tr>
								<td class="row-print" width="95" align="center" colspan="2"><font size="3">Provisional Marks Statement</font></td>																			
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
						                                    Course Code
						                        </td>
						                        <td rowspan="2" class="row-print" align="center" width="35%">
						                                    Course Title
						                        </td><!--
						                        <td rowspan="2" class="row-print" align="center" width="10%">
						                                    TYPE
						                        </td>
						                        --><td align="center" class="row-print" width="45%" colspan="4">
						                                    Marks Awarded
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
						                                    CA<br><hr>Max. <bean:write name="loginform" property="marksCardTo.maxCIAMarks"/>
						                        </td><!--
						                        <td class="row-print" align="center" >
						                                    MAX MARKS
						                        </td>
						                        <td class="row-print" align="center">
						                                    MIN MARKS
						                        </td>
						                        --><td class="row-print" align="center" width="15%">
						                                    ESA<br><hr>Max. <bean:write name="loginform" property="marksCardTo.maxESEMarks"/>
						                        </td><!--
						                        <td class="row-print" align="center">
						                                    MAX MARKS
						                        </td>
						
						                        -->
						                        <logic:equal value="false" name="loginform" property="dontShowPracticals">
						                        <td class="row-print" align="center" width="15%">
						                                    Practical<br><hr>Max. <bean:write name="loginform" property="marksCardTo.maxMarksPractical"/>
						                        </td>
						                        </logic:equal>
						                        <td class="row-print" align="center" width="15%">
						                                    Total<br>Max. <bean:write name="loginform" property="marksCardTo.maxTotalMarks"/>
						                        </td>               
						            </tr>
						            <c:set var="slnocount" value="0" />
										<logic:notEmpty name="loginform" property="marksCardTo.subMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.subMap">
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<tr height="25px">
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
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
												
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> 
												<logic:empty name="to" property="ciaMaxMarks">- </logic:empty> 
												<logic:notEqual value="-" name="to" property="ciaMaxMarks">
												<logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> 
												</logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td>
												<td align="center" style="font-size: 12px">
												
												<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>Max. <bean:write name="to" property="eseMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal>
												<logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  
												<logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<logic:equal value="false" name="loginform" property="dontShowPracticals">
												<td align="center" style="font-size: 12px">
													<logic:empty name="to" property="practicalEseMarksAwarded">-</logic:empty>
													<logic:notEmpty name="to" property="practicalEseMarksAwarded"><bean:write name="to" property="practicalEseMarksAwarded"/></logic:notEmpty>
												</td>	
												</logic:equal>											
												<td align="center" style="font-size: 12px">
												
												<%--<logic:notEmpty name="to" property="totalMaxMarks">
												<logic:notEqual value="-" name="to" property="totalMaxMarks">
												<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr><b>Max. <bean:write name="to" property="totalMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												--%>
													
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td>
												</tr>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										<c:set var="slnocount" value="0" />
										<logic:notEmpty name="loginform" property="marksCardTo.addOnCoursesubMap">
										<logic:iterate id="map" name="loginform" property="marksCardTo.addOnCoursesubMap">
											<logic:notEmpty name="map" property="value">
											<logic:iterate id="to" name="map" property="value">
											<c:set var="slnocount" value="${slnocount + 1 }" />
											<tr height="25px">
												<td style="font-size: 12px"> <bean:write name="to" property="code"/> </td>
												<td style="font-size: 12px"> <bean:write name="to" property="name"/> </td>
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
												
												<logic:equal value="-" name="to" property="ciaMaxMarks">-</logic:equal> <logic:empty name="to" property="ciaMaxMarks">- </logic:empty> <logic:notEqual value="-" name="to" property="ciaMaxMarks"><logic:notEmpty name="to" property="ciaMaxMarks"> <bean:write name="to" property="ciaMarksAwarded"/></logic:notEmpty> </logic:notEqual><logic:empty name="to" property="ciaMarksAwarded">- </logic:empty> </td>
												<td align="center" style="font-size: 12px">
												
												<logic:notEmpty name="to" property="eseMaxMarks">
												<logic:notEqual value="-" name="to" property="eseMaxMarks">
												<c:choose>
													<c:when test="${to.eseMaxMarks != loginform.marksCardTo.maxESEMarks}">
														<hr><b>Max. <bean:write name="to" property="eseMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="eseMaxMarks">-</logic:equal><logic:notEqual value="-" name="to" property="eseMaxMarks"><bean:write name="to" property="eseMarksAwarded"/></logic:notEqual>  <logic:empty name="to" property="eseMarksAwarded">- </logic:empty></td>
												<logic:equal value="false" name="loginform" property="dontShowPracticals">
												<td align="center" style="font-size: 12px">
													<logic:empty name="to" property="practicalTotalMarksAwarded">-</logic:empty>
													<logic:notEmpty name="to" property="practicalTotalMarksAwarded"><bean:write name="to" property="practicalTotalMarksAwarded"/></logic:notEmpty>
												</td>
												</logic:equal>
												<td align="center" style="font-size: 12px">
												
												<logic:notEmpty name="to" property="totalMaxMarks">
												<logic:notEqual value="-" name="to" property="totalMaxMarks">
												<c:choose>
													<c:when test="${to.totalMaxMarks != loginform.marksCardTo.maxTotalMarks}">
														<hr><b>Max. <bean:write name="to" property="totalMaxMarks"/></b><br><hr> 
													</c:when>
												</c:choose>	
												</logic:notEqual>
												</logic:notEmpty>
												
												<logic:equal value="-" name="to" property="totalMaxMarks">-</logic:equal> <logic:notEqual value="-" name="to" property="totalMaxMarks"><bean:write name="to" property="totalMarksAwarded"/></logic:notEqual> <logic:empty name="to" property="totalMarksAwarded">- </logic:empty></td></tr>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
										</logic:notEmpty>
										
										<tr><td><table>
										
												<logic:notEmpty name="loginform" property="marksCardTo.subMapForSuppl">
										<logic:iterate id="extraMap" name="loginform" property="marksCardTo.subMapForSuppl">
										<logic:notEmpty name="extraMap" property="value">
											<logic:iterate id="to" name="extraMap" property="value">
													<tr height="25px"><td>&nbsp;</td></tr>
											
											</logic:iterate></logic:notEmpty></logic:iterate></logic:notEmpty>
										
										
										</table></td>
										
										</tr>
										
										<tr height="25px">
											<td align="center" style="font-size: 12px" colspan="2">
												Group Total
											</td>
											<td align="center" style="font-size: 12px">
												<bean:write name="loginform" property="marksCardTo.grandTotalCIAMarks"/>
											</td>											
											<td align="center" style="font-size: 12px">
												<bean:write name="loginform" property="marksCardTo.grandTotalESAMarks"/>
											</td>											
											
											<logic:equal value="false" name="loginform" property="dontShowPracticals">
											<td align="center" style="font-size: 12px">
												<bean:write name="loginform" property="marksCardTo.practicalTotalMarks"/>
											</td>
											</logic:equal>
											<td style="font-size: 12px" align="center" align="center">
											<bean:write name="loginform" property="marksCardTo.totalMarksAwarded"/>
											</td>
										</tr>
										<tr height="25px">
											<td colspan="2" align="center" style="font-size: 12px">
											 <b>Result: <bean:write name="loginform" property="marksCardTo.result"/></b>
											</td>
											<td colspan="4" align="center" style="font-size: 12px"><b>Grand Total: <bean:write name="loginform" property="marksCardTo.grandTotalMarks"/></b></td>
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
							<html:button property="" styleClass="formbutton" onclick="printPass()">
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