<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.to.admission.AdmissionStatusTO"%>
<%@page import="com.kp.cms.forms.admission.GenerateProcessForm"%><script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/calendar.js"></script>
<link href="css/styles.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
    <script type="text/javascript" src="js/AC_RunActiveContent.js"></script>
    <script type="text/javascript" src="js/sdmenu/sdmenu.js"></script>
    <script type="text/javascript" src="js/ajax/Ajax.js"></script>
    <script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
    <script type="text/javascript" src="js/leftSwitchMenu.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script language="javaScript" type="text/javascript">
	function redirectControl() {
		document.location.href = "AdmissionStatus.do?method=initAdmissionStatus";
	}
	function downloadApplication(applnNo, appliedYear) {
		var url = "AdmissionStatus.do?method=downloadApplication&applicationNo="+applnNo +"&appliedYear="+appliedYear+"&displaySemister="+true;
		myRef = window
		.open(url, "viewDescription",
				"left=200,top=200,width=3000,height=1500,toolbar=1,resizable=0,scrollbars=1");
	}	
	function resetMessages() {
		resetErrMsgs();
	}		
	function download(applicationNo, courseId, interviewTypeId) {
		var url = "generateProcess.do?method=getData";
		myRef = window.open(url,"EAdmitCard","left=20,top=20,width=600,height=500,toolbar=1,resizable=0,scrollbars=1");
	}
</script>
<html:form action="/generateProcess" method="post">
	<html:hidden property="method" value="getApplicationStatus" />
	<html:hidden property="formName" value="generateProcessForm" />
	<html:hidden property="pageType" value="2" />
	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><bean:message
				key="knowledgepro.admission" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.admission.applicationstatus" />
			&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="9"><img src="images/Tright_03_01.gif" width="9"
								height="29"></td>
							<td background="images/Tcenter.gif" class="body">
							<div align="left"><strong class="boxheader">
							<bean:message key="knowledgepro.admission.applicationstatus" /></strong></div>
							</td>
							<td width="10"><img src="images/Tright_1_01.gif" width="9"
								height="29"></td>
						</tr>
						<tr>
							<td height="122" valign="top"
								background="images/Tright_03_03.gif"></td>
							<td valign="top" class="news">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr bgcolor="#FFFFFF">
									<td height="20" colspan="4" class="news">
									<div align="right"><span class='MandatoryMark'> <bean:message
									key="knowledgepro.mandatoryfields" /></span></div>
									<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
									<FONT color="green"><html:messages id="msg"
										property="messages" message="true">
										<c:out value="${msg}" escapeXml="false"></c:out>
										<br>
									</html:messages> </FONT></div>
									</td>
								</tr>
								<%
									String dateofBirth = "datePick";
								%>
								<tr>
									<td width="27%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message
										key="knowledgepro.admission.appNo" /></div>
									</td>
									<td width="20%" class="row-even"><html:text
										property="applicationNo" styleId="appNo" size="17"
										maxlength="30" styleClass="body"></html:text></td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message
										key="knowledgepro.admission.dob" /><bean:message key="admissionForm.application.dateformat.label"/></div>
									</td>
									<td class="row-odd"><html:text property="dateOfBirth"
										readonly="false" size="10" maxlength="10"
										styleId='<%=dateofBirth%>'></html:text> <script
										language="JavaScript">
									new tcal ({
									// form name
									'formname': 'generateProcessForm',
									// input name
									'controlname': '<%=dateofBirth%>'
									});</script></td>
								</tr>
								<tr>
									<td height="45" colspan="4">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="45%" height="35">
											<div align="right"><html:submit styleClass="formbutton"
												value="Submit" /></div>
											</td>
											<td width="2%"></td>
											<td width="53%"><html:button property=""
												styleClass="formbutton" value="Reset"
												onclick="resetMessages()" /></td>
										</tr>
									</table>
									</td>
								</tr>
						
								<tr>
									<td height="25" colspan="4">
									<table width="100%" height="53" border="0" cellpadding="0"
										cellspacing="1">
										<tr class="row-odd">
											<td width="250" height="25" class="bodytext">
											<div align="center"><bean:message
												key="knowledgepro.admission.applicationNo" /></div>
											</td>
											<td width="250" height="25" class="bodytext">
											<div align="center"><bean:message
												key="knowledgepro.admission.dateOfBirth" /></div>
											</td>
											<td width="250" height="25" class="bodytext">
											<div align="center"><bean:message
												key="admissionFormForm.emailId" /></div>
											</td>
											<td width="250" class="bodytext">
												<div align="center"><bean:message
													key="knowledgepro.admission.status" /></div>
												</td>
											<c:choose>
											<c:when test="${generateProcessForm.admStatus!= null && generateProcessForm.admStatus!= ''}">
												<td >
												&nbsp;	
												</td>
											</c:when>
											<c:otherwise>
											<logic:notEmpty property="admissionStatusTO"
												name="generateProcessForm">
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="interview">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/> </div>
													</td>
												</logic:equal>
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="admission">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/> </div>
													</td>
												</logic:equal>
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="viewapplication">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.viewapp"/> </div>
													</td>
												</logic:equal>
											</logic:notEmpty>
										</c:otherwise>
										</c:choose>
										
										</tr>
										<c:choose>
										<c:when test="${generateProcessForm.admStatus!= null && generateProcessForm.admStatus!= ''}">
											<logic:notEmpty property="admissionStatusTO"
												name="generateProcessForm">
												<tr class="row-odd">
													<td width="250" align="center" height="25" class="row-even"><bean:write
														name="generateProcessForm"
														property="admissionStatusTO.applicationNo" /></td>
													<td width="250" align="center" height="25" class="row-even"><bean:write
														name="generateProcessForm"
														property="admissionStatusTO.dateOfBirth" /></td>
													<td width="250" align="center" height="25" class="row-even"><bean:write
														name="generateProcessForm"
														property="admissionStatusTO.email" /></td>
													<td width="250" align="center" height="25" class="row-even"><bean:write
														name="generateProcessForm"
														property="admStatus" />
													<div align="center"></div>
													</td>
													<td class="row-even">
													&nbsp;
													</td>

												</tr>
												</logic:notEmpty>
										</c:when>
										<c:otherwise>

										<logic:notEmpty property="admissionStatusTO"
											name="generateProcessForm">
											<tr class="row-odd">
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="generateProcessForm"
													property="admissionStatusTO.applicationNo" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="generateProcessForm"
													property="admissionStatusTO.dateOfBirth" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="generateProcessForm"
													property="admissionStatusTO.email" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="generateProcessForm"
													property="admissionStatusTO.isSelected" />
												<div align="center"></div>
												</td>
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="interview">
													<td width="250" align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="download('<bean:write name="generateProcessForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="generateProcessForm" property="admissionStatusTO.courseId" />', '<bean:write name="generateProcessForm" property="admissionStatusTO.interviewProgramCourseId" />' )">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/></a></div>
													</td>
												</logic:equal>
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="admission">
													<td width="250" align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="download('<bean:write name="generateProcessForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="generateProcessForm" property="admissionStatusTO.courseId" />', '<bean:write name="generateProcessForm" property="admissionStatusTO.interviewProgramCourseId" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/></a></div>
													</td>
												</logic:equal>
												<logic:equal name="generateProcessForm"
													property="admissionStatusTO.isInterviewSelected"
													value="viewapplication">
													<td width="250" align="center" class="row-even">
													<%-- 
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadApplication('<bean:write name="generateProcessForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="generateProcessForm" property="admissionStatusTO.appliedYear" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.viewapp"/></a></div>
													--%>
													</td>
												</logic:equal>
											</tr>
										</logic:notEmpty>
										</c:otherwise>
										</c:choose>
									</table>
									</td>
								</tr>
							</table>
							<div align="center">
							<table width="100%" height="27" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td>
									<div align="center"></div>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
							</div>
							</td>
							<td width="10" valign="top" background="images/Tright_3_3.gif"
								class="news"></td>
						</tr>
						<tr>
							<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
							<td width="949" background="images/Tcenter.gif"></td>
							<td><img src="images/Tright_02.gif" width="9" height="29"></td>
						</tr>
						<tr>
							<td colspan="3">
								<div id=message>
								</div>
							</td>					
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

</html:form>
<SCRIPT language="JavaScript">
var browserName=navigator.appName; 
if (browserName=="Microsoft Internet Explorer")
{
	document.getElementById("message").innerHTML="<b style='color:red'>Note:</b> Use Mozilla Firefox for better Performance and view  or Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling.";
	alert(" If Your using InternetExplorer Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling  or Use Mozilla FireFox");	
}
</SCRIPT>