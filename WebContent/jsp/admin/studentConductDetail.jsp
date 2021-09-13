<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script type="text/javascript">
	function cancelAction() {
		document.location.href = "ConductAndCertificate.do?method=initConductDetails";
	}
	function printCertificate(){
		var year=document.getElementById("year").value;
		if(year==null || year==""){
			document.getElementById("displayingErrorMessage").innerHTML="Please Select Academic Year";
		}else{
			resetErrMsgs();
			document.getElementById("displayingErrorMessage").innerHTML="";
			var url = "ConductAndCertificate.do?method=printConductCertificate&academicYear="+year;
			var browserName=navigator.appName; 
				 myRef = window.open(url,"MarksCard","left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
		}
	}
</script>
<html:form action="/ConductAndCertificate" method="post">
	<html:hidden property="method" styleId="method" value="" />
  	<html:hidden property="formName" value="certificateForm" />
	<html:hidden property="pageType" value="2" />
	<table width="99%" border="0">
		<tr>
			<td>
				<span class="Bredcrumbs">
					<bean:message key="knowledgepro.admission" />&gt;&gt;<bean:message key="knowledgepro.admission.tc.details.label" />&gt;&gt;
				</span>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
						<td background="images/Tcenter.gif" class="heading_white">
							<bean:message key="knowledgepro.admission.tc.details.label"/>
						</td>
						<td width="10"><img src="images/Tright_1_01.gif" width="9" height="29"></td>
					</tr>
					<tr>
						<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
						<td class="news">
							<div align="right">
								<span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span>
							</div>
							<FONT size="2px" color="red"><div id="displayingErrorMessage"></div></FONT>
							<div id="errorMessage"><FONT color="red"><html:errors/></FONT>
								<FONT color="green">
									<html:messages id="msg" property="messages" message="true">
										<c:out value="${msg}" escapeXml="false"></c:out>
										<br>
									</html:messages>
								</FONT>
							</div>
						</td>
						<td valign="top" background="images/Tright_3_3.gif"></td>
					</tr>
					<tr>
						<td valign="top" background="images/Tright_03_03.gif"></td>
						<td valign="top">
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
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
												<td height="25" class="row-odd">
													<div align="right"><bean:message key="knowledgepro.fee.studentname" /></div>
												</td>
												<td class="row-even"><bean:write name="certificateForm" property="certificateTO.studentName"/></td>
												
												<td align="right" class="row-odd">
													<bean:message key="knowledgepro.hostel.reservation.registerNo" />
												</td>
												<td class="row-even"><bean:write name="certificateForm" property="certificateTO.registerNo"/></td>
											</tr>
											<tr>
												<td height="25" class="row-odd">
													<div align="right"><bean:message key="knowledgepro.admission.admissionno" /></div>
												</td>
												<td class="row-even"><bean:write name="certificateForm" property="certificateTO.admNo"></bean:write></td>
												
												<td align="right" height="25" class="row-odd"><bean:message key="knowledgepro.admin.dateofbirth"/></td>
												<td class="row-even"><bean:write name="certificateForm" property="certificateTO.dob"></bean:write></td>
											</tr>
											<tr>
												<td height="25" class="row-odd">
													<div align="right"><bean:message key="knowledgepro.applicationform.trainingprog.label" /></div>
												</td>
												<td class="row-even"><bean:write name="certificateForm" property="certificateTO.programme"/></td>
												
												<td align="right" class="row-odd">
													<span class="Mandatory">*</span><bean:message key="knowledgepro.petticash.academicYear" />
												</td>
												<td class="row-even" ><html:text styleClass="TextBox" size="16"
												maxlength="50" name="certificateForm" property="academicYear" styleId="year"></html:text></td>
											</tr>
											<tr>
												<td height="25" class="row-odd">
													<div align="right"><bean:message key="knowledgepro.exam.course" /></div>
												</td>
												<td class="row-even"><html:text styleClass="TextBox" size="50"
												maxlength="50" property="certificateTO.courseName" name="certificateForm"></html:text></td>
												<td align="right" class="row-odd">
													Subsidiaries
												</td>
												<td class="row-even"><html:text styleClass="TextBox" size="50"
												maxlength="50" name="certificateForm" property="certificateTO.subsidiaries"></html:text></td>
											</tr>
											
											
											
											
											
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
						<td valign="top" background="images/Tright_3_3.gif"></td>
					</tr>
					<tr>
						<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
						<td class="heading">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="35" align="center">
										<html:button property="" styleClass="formbutton" value="Print" onclick="printCertificate()"></html:button> &nbsp;&nbsp;
										&nbsp;
										<html:button property="" styleClass="formbutton" value="Cancel" onclick="cancelAction()"></html:button>
									</td>
								</tr>
							</table>
						</td>
						<td valign="top" background="images/Tright_3_3.gif"></td>
					</tr>
					<tr>
						<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
						<td width="0" background="images/TcenterD.gif"></td>
						<td><img src="images/Tright_02.gif" width="9" height="29"></td>
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