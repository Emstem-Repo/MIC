<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<%@page import="com.kp.cms.to.admission.AdmissionStatusTO"%>
<%@page import="com.kp.cms.forms.admission.AdmissionStatusForm"%><script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/calendar.js"></script>
<link href="css/styles.css" rel="stylesheet" type="text/css">

<script language="javaScript" type="text/javascript"><!--
	function redirectControl() {
		document.location.href = "AdmissionStatus.do?method=initAdmissionStatus";
	}
	<%--function downloadAdmitCard(applicationNo, courseId, interviewTypeId) {
		var url = "AdmissionStatus.do?method=downloadInterviewCard&applicationNo="+applicationNo+"&courseId="+courseId+"&interviewTypeId="+interviewTypeId;
		myRef = window.open(url,"EAdmitCard","left=20,top=20,width=600,height=500,toolbar=1,resizable=0,scrollbars=1");
	}
	function downloadAdmissionCard(applicationNo, courseId, interviewTypeId) {
		var url = "AdmissionStatus.do?method=downloadAdmissionCard&applicationNo="+applicationNo+"&courseId="+courseId+"&interviewTypeId="+interviewTypeId;
		myRef = window.open(url,"EAdmissionCard","left=20,top=20,width=600,height=500,toolbar=1,resizable=0,scrollbars=1");
	}	--%>
	function downloadApplication(applnNo, appliedYear) {
		var url = "MemoPrint.do?method=downloadApplication&applicationNo="+applnNo +"&appliedYear="+appliedYear+"&displaySemister="+true+"&memo="+false;
		window.open(url,'PrintApplication','left=20,top=20,width=800,height=1000,toolbar=1,resizable=0,scrollbars=1');
	}
	function downloadmemo(applnNo, appliedYear) {
		var url = "MemoPrint.do?method=downloadApplication&applicationNo="+applnNo +"&appliedYear="+appliedYear+"&displaySemister="+true+"&memo="+true;
		window.open(url,'PrintApplication','left=20,top=20,width=800,height=1000,toolbar=1,resizable=0,scrollbars=1');
	}	
</script>
<html:form action="/MemoPrint" method="post">
	<html:hidden property="method" value="getAdmissionStatus" />
	<html:hidden property="formName" value="admissionStatusForm" />
	<html:hidden property="pageType" value="1" />
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
									<td height="20" colspan="4">
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
									<div align="right"><span class="Mandatory">*</span>&nbsp; Department</div>
									</td>
									<td class="row-odd">
										<html:select property="deptId" name="admissionStatusForm">
											<html:optionsCollection name="admissionStatusForm" property="deptMap" label="value" value="key"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td width="27%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Applied Year</div>
									</td>
									<td width="20%" class="row-even"><html:select
										property="year" styleId="year" styleClass="combo">
										<html:option value=" ">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<cms:renderYear></cms:renderYear>
									</html:select></td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp; Category</div>
									</td>
									<td class="row-odd">
										<html:select property="categoryId" name="admissionStatusForm">
											<html:optionsCollection name="admissionStatusForm" property="categoryMap" label="value" value="key"/>
										</html:select>
									</td>
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
												onclick="redirectControl()" /></td>
										</tr>
									</table>
									</td>
								</tr>
													
								<tr>
									<td height="25" colspan="4">
									<table width="100%" height="53" border="0" cellpadding="0"
										cellspacing="1">
										<tr class="row-odd">
											<td  height="25" class="bodytext">
											<div align="center"><bean:message
												key="knowledgepro.admission.applicationNo" /></div>
											</td>
											<td  height="25" class="bodytext">
											<div align="center"><bean:message
												key="knowledgepro.admission.dateOfBirth" /></div>
											</td>
											<td  height="25" class="bodytext">
											<div align="center"><bean:message
												key="admissionFormForm.emailId" /></div>
											</td>
											<td  class="bodytext">
											<div align="center"><bean:message
												key="knowledgepro.admission.status" /></div>
											</td>
											<%--<logic:notEmpty property="admissionStatusTO"
												name="admissionStatusForm">
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="interview">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/> </div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="admission">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/> </div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="viewapplication">
													<td width="250" class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.viewapp"/> </div>
													</td>
												</logic:equal>
											</logic:notEmpty>
											--%><logic:notEmpty property="statusTO"
												name="admissionStatusForm">
												<logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected" value="interview">
													<td  class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/></div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected" value="admission">
													<td  class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/></div>
													</td>
												</logic:equal>
											<%--<logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected"
													value="viewapplication">
													<td  class="bodytext">
													<div align="center"><bean:message key="knowledgepro.admission.applicationstatus.viewapp"/> </div>
													</td>
												</logic:equal>--%>
										 </logic:notEmpty>
										</tr>
										<%--<logic:notEmpty property="admissionStatusTO"
											name="admissionStatusForm">
											<tr class="row-odd">
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm"
													property="admissionStatusTO.applicationNo" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm"
													property="admissionStatusTO.dateOfBirth" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm"
													property="admissionStatusTO.email" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm"
													property="admissionStatusTO.isSelected" />
												<div align="center"></div>
												</td>
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="interview">
													<td width="250" align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadAdmitCard('<bean:write name="admissionStatusForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="admissionStatusTO.courseId" />', '<bean:write name="admissionStatusForm" property="admissionStatusTO.interviewProgramCourseId" />' )">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/></a></div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="admission">
													<td width="250" align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadAdmissionCard('<bean:write name="admissionStatusForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="admissionStatusTO.courseId" />', '<bean:write name="admissionStatusForm" property="admissionStatusTO.interviewProgramCourseId" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/></a></div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="admissionStatusTO.isInterviewSelected"
													value="viewapplication">
													<td width="250" align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadApplication('<bean:write name="admissionStatusForm" property="admissionStatusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="admissionStatusTO.appliedYear" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.viewapp"/></a></div>
													</td>
												</logic:equal>
											</tr>
										</logic:notEmpty>--%>
										<logic:notEmpty property="statusTO" name="admissionStatusForm">
											<tr class="row-odd">
												<td  align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm"
													property="statusTO.applicationNo" /></td>
												<td width="250" align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm" property="statusTO.dateOfBirth" /></td>
												<td  align="center" height="25" class="row-even"><bean:write
													name="admissionStatusForm" property="statusTO.email" /></td>
													
													<c:choose>
													      <c:when test="${admissionStatusForm.maxallotment>0}">
													         <td  align="center" height="25" class="row-even">Allotment <bean:write name="admissionStatusForm" property="maxallotment"/> <bean:message key="knowledgepro.admission.applicationstatus.published"/>
													         <logic:equal value="true" name="admissionStatusForm" property="admissionStatusTO.allotmentflag">
													         <br></br><bean:message key="knowledgepro.admission.applicationstatus.gotallotment"/>
													         <br></br><bean:message key="knowledgepro.admission.applicationstatus.memo"/>
												             </logic:equal>
												             <logic:equal value="false" name="admissionStatusForm" property="admissionStatusTO.allotmentflag">
													         <br></br><bean:message key="knowledgepro.admission.applicationstatus.notallotment"/>
												             </logic:equal>
													         
													         </td>
													      
													      </c:when>
													      <c:otherwise>
													      <logic:equal value="false" name="admissionStatusForm" property="admissionStatusTO.allotmentflag">
												          <td  align="center" height="25" class="row-even"><bean:message key="knowledgepro.admission.applicationstatus.new"/></td>
											              </logic:equal>
													      
													      </c:otherwise>
													
													
													</c:choose>
													
													
													
													
													
													
													
												
												
												
												
												
												<logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected" value="interview">
													<td  align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadAdmitCard('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.courseId" />', '<bean:write name="admissionStatusForm" property="statusTO.interviewProgramCourseId" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadicard"/></a></div>
													</td>
												</logic:equal>
												<logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected" value="admission">
													<td  align="center" class="row-even">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadAdmissionCard('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.courseId" />', '<bean:write name="admissionStatusForm" property="statusTO.interviewProgramCourseId" />')">
													<bean:message key="knowledgepro.admission.applicationstatus.downloadadmitcard"/></a></div>
													</td>
												</logic:equal>
												
											</tr>
											<tr><td><logic:equal name="admissionStatusForm"
													property="statusTO.isInterviewSelected"
													value="viewapplication">
													<td  align="center">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadApplication('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.appliedYear" />')">
													<br></br><bean:message key="knowledgepro.admission.applicationstatus.viewapp"/></a></div>
													</td>
													
													
													
												</logic:equal></td>
												<logic:equal value="true" name="admissionStatusForm"
													property="admissionStatusTO.allotmentflag">
													<td  align="center">
													<div align="center"><a href="javascript:void(0)"
														class="menuLink"
														onclick="downloadmemo('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.appliedYear" />')">
													<br></br><bean:message key="knowledgepro.admission.applicationstatus.memo"/></a></div>
													</td>
													</logic:equal>
												
												</tr>
											
										</logic:notEmpty>
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