<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<link rel="stylesheet" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
<script language="JavaScript" src="js/ajax/AjaxUtil.js"></script>
<script language="JavaScript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript">
function cancelAction(){
	document.location.href="StudentLoginAction.do?method=returnHomePage";
		}

function printApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintDetails";
	
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}

function printChallanApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintChallanForSupply";
	
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}
function onlinePay(){
	document.getElementById("method").value = "redirectToPGIExamSuppl";
	document.newSupplementaryImpApplicationForm.submit();
}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="newSupplementaryImpApplicationForm" value="totalFees" />
	<html:hidden property="method" styleId="method" value="calculateAmountForSupply" />

	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><bean:message
				key="knowledgepro.exam.exam" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.exam.supplementaryApplication" />
			&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message
						key="knowledgepro.exam.supplementaryApplication" /></strong></td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div align="right" class="mandatoryfield"><bean:message
						key="knowledgepro.mandatoryfields" /></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20">
					<table width="100%">
					<tr>
							<td align="left">
							<div id="errorMessages" class="ui-widget">
							<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
							<p>
							<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
							<strong>Alert:</strong>
							<span id="err"><html:errors/></span>
							</p>
							</div>
							</div>

							<div id="messages"><div class="display-info">
							<span id="msg"><html:messages id="message" property="messages" message="true"><c:out value="${message}" escapeXml="false"></c:out><br></html:messages></span>
							</div>
							</div>
							<script type="text/javascript">
								if(document.getElementById("msg")==null ||  document.getElementById("msg").innerHTML==''){
									document.getElementById("messages").style.display="none";
									}
								if(document.getElementById("err").innerHTML==''){
									document.getElementById("errorMessages").style.display="none";
									}
							</script>
							
							</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top" align="center">
							
							<table width="90%" cellspacing="1" cellpadding="2">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="feesNotConfigured">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="suppImpAppAvailable">
								<nested:notEmpty name="newSupplementaryImpApplicationForm" property="mainList">
																			<tr>
									<td width="33%" height="30" class="studentrow-odd">
									<div align="right"><bean:message
										key="knowledgepro.exam.revaluationApplication.regNo" /></div>
									</td>
									<td width="19%" height="30" class="studentrow-even"><nested:write name="newSupplementaryImpApplicationForm"
										 property="registerNo" /></td>
									<td height="26" class="studentrow-odd">
									<div align="right"><bean:message
										key="knowledgepro.exam.revaluationApplication.studentName" /></div>
									</td>
									<td class="studentrow-even"><nested:write name="newSupplementaryImpApplicationForm"
										 property="nameOfStudent" /></td>
								</tr>
								<tr>
									<td height="26" class="studentrow-odd">
									<div align="right"><bean:message
										key="knowledgepro.exam.revaluationApplication.course" /></div>
									</td>
									<td class="studentrow-even"><nested:write name="newSupplementaryImpApplicationForm"
										 property="courseName" /></td>
									<td height="26" class="studentrow-odd">
									
									</td>
									<td class="studentrow-even"></td>

								</tr>
								<%-- 
								<tr>
									<td height="26" class="studentrow-odd">
									<div align="right"><bean:message
										key="knowledgepro.supplementary.theory.fees" /></div>
									</td>
									<td class="studentrow-even">
										<bean:write name="newSupplementaryImpApplicationForm" property="theoryFees"/>
									</td>
									<td height="26" class="studentrow-odd">
									<div align="right"><bean:message
										key="knowledgepro.supplementary.practical.fees" /></div>
									</td>
									<td class="studentrow-even">
									<bean:write name="newSupplementaryImpApplicationForm" property="practicalFees"/>
									</td>
								</tr>
								--%>
								</nested:notEmpty></nested:equal></nested:equal>
							
							</table></td>
							<td width="5" height="30" background="images/right.gif"></td>
							</tr>
							
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="suppImpAppAvailable">
								Application is not available
							</nested:equal>
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="feesNotConfigured">
								Application is not available Please Contact: 
							</nested:equal>
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="feesNotConfigured">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="suppImpAppAvailable">
								<nested:notEmpty name="newSupplementaryImpApplicationForm" property="mainList">
									<nested:iterate id="eto" name="newSupplementaryImpApplicationForm" property="mainList" indexId="count2">
										<nested:notEmpty property="examList" >
											<nested:iterate id="cto"  property="examList" indexId="count1">
											<tr><td colspan="4"> &nbsp; </td> </tr>
											<tr ><td colspan="4" align="center"> <p>
											<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="supplementary">
												NOTE:The failed subjects are displayed below. Kindly tick the subject which you wish to apply for supplementary/improvement</nested:equal>
											<nested:equal value="true" name="cto" property="improvement">
											NOTE:Kindly tick [Apply for] for the subject which you wish to apply for improvement
											</nested:equal>
											</p>
											 </td> </tr>
								<%-- 			 
									<nested:equal value="false" property="extended" name="newSupplementaryImpApplicationForm">
											<tr class="heading"><td colspan="4" align="center"> <p>
											The Application will be available till: <bean:write name="eto" property="examDate"/> </p>
											 </td> </tr>
									</nested:equal>
									--%>
									<nested:equal value="true" property="extended" name="newSupplementaryImpApplicationForm">
											<tr class="heading"><td colspan="4" align="center"> <p>
											The Application is extended with fine till: <bean:write name="eto" property="extendedDate"/>&nbsp; </p>
											 </td> </tr>
									</nested:equal>
								<tr class="heading"><td width="25%" align="right">
								Class:
								</td>
								<td width="25%" ><nested:write property="className"/> </td>
								<td width="25%" align="right">Exam : </td>
								<td width="25%"> <bean:write name="eto" property="examName"/> </td>
								 </tr>
								<tr>
									<td colspan="4" align="center">
									<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
									<td height="25" rowspan="2" align="center" class="studentrow-odd">Sl
									No.</td>
									<td rowspan="2" class="studentrow-odd">Subject Code</td>
									<td rowspan="2" class="studentrow-odd">Subject Name</td>
									<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="supplementary"><td colspan="2" class="studentrow-odd">
									<div align="center">Failed</div>
									</td></nested:equal>
									<td colspan="2" class="studentrow-odd">
									<div align="center">Apply For</div>
									</td>
									<!--<td  class="studentrow-odd" rowspan="2">
									<div align="center">Is CIA</div>
									</td>
									-->
								</tr>
								<tr>
									<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="supplementary"><td class="studentrow-odd">Theory</td>
									<td class="studentrow-odd">Practical</td></nested:equal>
									<td class="studentrow-odd">Theory</td>
									<td class="studentrow-odd">Practical</td>
								</tr>


								<nested:iterate  property="toList" indexId="count">

									<%
										String dynamicStyle = "";
														if (count % 2 != 0) {
															dynamicStyle = "row-white";

														} else {
															dynamicStyle = "studentrow-even";

														}
									%>
									<tr>
										<td class='<%=dynamicStyle%>'>
										<div align="center"><c:out value="${count + 1}" /></div>
										</td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="subjectCode" /></td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="subjectName" /></td>
											
										<%-- raghu chnged down
										<nested:equal value="true" name="cto" property="supplementary">
										--%>
										
										<nested:equal value="true" property="isSupplementary">
										<td class='<%=dynamicStyle%>'>
										<%    String ft2 = "ft2_"+count2+"_"+count1+"_"+count; 
                      					 	  String ft1 = "ft1_"+count2+"_"+count1+"_"+count; %>
                      					 	  
		                     				 <nested:hidden styleId='<%=ft2%>' property="isFailedTheory" />
		                     				 <span id="sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											 <nested:checkbox styleId='<%=ft1%>' property="failedTheory" indexed="true" disabled="true" />
											 </span>
											 <script type="text/javascript">
												var vft1=document.getElementById("ft2_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
												if(vft1=="true"){
												document.getElementById("ft1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
												document.getElementById("sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
												}
       										</script>
										</td>
										<td class='<%=dynamicStyle%>'>
										<%    String fp2 = "hidden1_"+count2+"_"+count1+"_"+count; 
                      						  String fp1 = "check1_"+count2+"_"+count1+"_"+count; %>
		                     				 <nested:hidden styleId='<%=fp2%>' property="isFailedPractical" />
		                     				 <span id="pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											<nested:checkbox styleId='<%=fp1%>' property="failedPractical" indexed="true" disabled="true"/>
											</span>
										<script type="text/javascript">
										var vfp=document.getElementById("hidden1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
										if(vfp=="true"){
										document.getElementById("check1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
										document.getElementById("pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
										}
										</script>
										</td>
										
										<% String control="control1_"+count;
										String disable="disable_"+count;
										%>
										
												<td class='<%=dynamicStyle%>'>
												<nested:equal value="false" property="tempChecked">
																						<nested:equal value="true" property="isFailedTheory">
												<input
																	type="hidden"
																	name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempChecked"
																	id="hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>"
																	value="<nested:write property='tempChecked'/>" />
																	<input
																	type="checkbox"
																	name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isAppearedTheory"
																	id="<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>" />
																	<script type="text/javascript">
																		var studentId = document.getElementById("hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
																		if(studentId == "true") {
																			document.getElementById("<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked = true;
																		}		
																	</script>
																	</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="tempChecked">Applied</nested:equal>
															</td>
										<td class='<%=dynamicStyle%>'>
										<nested:equal value="false" property="tempPracticalChecked">
										<nested:equal value="true" property="isFailedPractical">

										<input
												type="hidden"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempPracticalChecked"
												id="prhidden_<c:out value='${count}'/>"
												value="<nested:write property='tempPracticalChecked'/>" />
												<input
												type="checkbox"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isAppearedPractical"
												id="pr_<c:out value='${count}'/>" />
												<script type="text/javascript">
													var pr = document.getElementById("prhidden_<c:out value='${count}'/>").value;
													if(pr == "true") {
														document.getElementById("pr_"+"<c:out value='${count}'/>").checked = true;
													}		
												</script>
												</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="tempPracticalChecked">Applied</nested:equal>
										</td>
										<nested:hidden styleId='<%=control%>'
											property="controlDisable" />
										</nested:equal>
										
										<%-- raghu chnged down
										<nested:equal value="true" name="cto" property="improvement">
										--%>
										
										<nested:equal value="true" property="isImprovement">
											<% String control="control1_"+count;
										String disable="disable_"+count;
										%>
										
										
												
												
										<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="supplementary">
												
												
												<td class='<%=dynamicStyle%>'>
										<%    String ft2 = "ft2_"+count2+"_"+count1+"_"+count; 
                      					 	  String ft1 = "ft1_"+count2+"_"+count1+"_"+count; %>
                      					 	  
		                     				 <nested:hidden styleId='<%=ft2%>' property="isFailedTheory" />
		                     				 <span id="sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											 <nested:checkbox styleId='<%=ft1%>' property="failedTheory" indexed="true" disabled="true" />
											 </span>
											 <script type="text/javascript">
												var vft1=document.getElementById("ft2_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
												if(vft1=="true"){
												document.getElementById("ft1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
												document.getElementById("sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
												}
       										</script>
										</td>
										<td class='<%=dynamicStyle%>'>
										<%    String fp2 = "hidden1_"+count2+"_"+count1+"_"+count; 
                      						  String fp1 = "check1_"+count2+"_"+count1+"_"+count; %>
		                     				 <nested:hidden styleId='<%=fp2%>' property="isFailedPractical" />
		                     				 <span id="pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											<nested:checkbox styleId='<%=fp1%>' property="failedPractical" indexed="true" disabled="true"/>
											</span>
										<script type="text/javascript">
										var vfp=document.getElementById("hidden1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
										if(vfp=="true"){
										document.getElementById("check1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
										document.getElementById("pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
										}
										</script>
										</td>
												
										</nested:equal>
												
												
												
												
												<td class='<%=dynamicStyle%>'>
												<nested:equal value="false" property="tempChecked">
																						<nested:equal value="true" property="theory">
												<input
																	type="hidden"
																	name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempChecked"
																	id="hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>"
																	value="<nested:write property='tempChecked'/>" />
																	<input
																	type="checkbox"
																	name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isAppearedTheory"
																	id="<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>" />
																	<script type="text/javascript">
																		var studentId = document.getElementById("hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
																		if(studentId == "true") {
																			document.getElementById("<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked = true;
																		}		
																	</script>
																	</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="tempChecked">Applied</nested:equal>
															</td>
										<td class='<%=dynamicStyle%>'>
										<nested:equal value="false" property="tempPracticalChecked">
										<nested:equal value="true" property="practical">

										<input
												type="hidden"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempPracticalChecked"
												id="prhidden_<c:out value='${count}'/>"
												value="<nested:write property='tempPracticalChecked'/>" />
												<input
												type="checkbox"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isAppearedPractical"
												id="pr_<c:out value='${count}'/>" />
												<script type="text/javascript">
													var pr = document.getElementById("prhidden_<c:out value='${count}'/>").value;
													if(pr == "true") {
														document.getElementById("pr_"+"<c:out value='${count}'/>").checked = true;
														
													}		
												</script>
												</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="tempPracticalChecked">Applied</nested:equal>
										</td>
										<nested:hidden styleId='<%=control%>'
											property="controlDisable" />
										</nested:equal>
										<!-- Start CIA Exam -->
										
										<%--
										<td  class='<%=dynamicStyle%>' align="center"><div align="center">
										
										<nested:equal value="false" property="isESE">
										<nested:equal value="false" property="tempCIAExamChecked">
										

										<input
												type="hidden"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempCIAExamChecked"
												id="CIAhidden_<c:out value='${count}'/>"
												value="<nested:write property='tempCIAExamChecked'/>" />
												<input
												type="checkbox"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].ciaExam"
												id="CIA_<c:out value='${count}'/>" />
												<script type="text/javascript">
													var cia = document.getElementById("CIAhidden_<c:out value='${count}'/>").value;
													if(cia == "true") {
														document.getElementById("CIA_"+"<c:out value='${count}'/>").checked = true;
														
													}		
												</script>
												
												</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="isESE">No</nested:equal>
												<nested:equal value="true" property="tempCIAExamChecked">Yes</nested:equal>
												
										
										</div>
										</td>
										
										--%>
										
										<!-- END CIA Exam -->
									</tr>
								</nested:iterate>
							</table>
							</td>
							<td width="5" height="30" background="images/right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
							<td background="images/05.gif"></td>
							<td><img src="images/06.gif" /></td>
						</tr>
					</table>
											</nested:iterate>
										</nested:notEmpty>
									</nested:iterate>
								</nested:notEmpty>
							</nested:equal></nested:equal>
							</table>
							</td>
							<td width="5" height="30" background="images/right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
							<td background="images/05.gif"></td>
							<td><img src="images/06.gif" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="47%" height="29">&nbsp;</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<nested:equal value="true" property="extended" name="newSupplementaryImpApplicationForm">
						<tr>
						<td width="47%" height="35" colspan="3">
						<div align="center" style="font-weight: bold;font-size: 12px;color: red"><B>Late submission fee: <bean:write name="newSupplementaryImpApplicationForm" property="fineFees"/></B>
						
						</div>
						</td>
						</tr>
					</nested:equal>
						<tr><td colspan="3"><hr></hr></td></tr>
					 <tr align="center" >
					 <td colspan="3" style="height: 20px; font-size: 15px">
					 	<b>Examination Fee : </b> <bean:write name="newSupplementaryImpApplicationForm" property="totalFees" /></td>
					 </tr>
					 <%-- <tr align="center" >
					 <td colspan="3" style="height: 20px; font-size: 15px">
					 	<b>Late Fine Fee : </b> <bean:write name="newSupplementaryImpApplicationForm" property="onlineServiceChargeFees" /></td>
					 </tr> --%>
		
						<tr>
							<td width="47%" height="35">
							<div align="right">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="feesNotConfigured">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="suppImpAppAvailable">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="displayButton">
							<nested:submit styleClass="btnbg" value="Submit"></nested:submit>
							</nested:equal>
							</nested:equal></nested:equal>
							</div>
							</td>
							<td width="1%"></td>
							<td width="46%">
							
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="challanButton">
							
								<html:button property=""
								styleClass="btnbg" value="Pay Online"
								onclick="onlinePay()"></html:button>
								
							</nested:equal>
								<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="printSupplementary">
							<html:button property=""
								styleClass="btnbg" value="Print"
								onclick="printApplication()"></html:button>
							</nested:equal>
						<html:button property=""
								styleClass="btnbg" value="Close"
								onclick="cancelAction()"></html:button>
							</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td><img src="images/st_Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/st_TcenterD.gif"></td>
					<td><img src="images/st_Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
<script type="text/javascript">
$(".display-info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
var print = "<c:out value='${newSupplementaryImpApplicationForm.challanButton}'/>";
if(print.length != 0 && print == "true") {
	var url ="newSupplementaryImpApp.do?method=showPrintDetails";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}
</script>