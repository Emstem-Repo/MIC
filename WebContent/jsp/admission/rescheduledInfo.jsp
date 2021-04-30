<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<html:html>

<head>
<title><bean:message key="knowledgepro.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<link rel="stylesheet" href="css/calendar.css">
<link href="css/styles.css" rel="stylesheet" type="text/css">

<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<script type="text/javascript">
	function closeMe() {
		window.close();
	}
</script>
</head>

<body>
<html:form action="/interviewTimeChange" method="POST">
	<html:hidden property="method" value="" />
	<html:hidden property="formName" value="interviewTimeChangeForm" styleId="formName" />
	<html:hidden property="pageType" value="5" />

	<table width="99%" border="0">

		<tr>
			<td><span class="Bredcrumbs">&gt;&gt;Admission
			&gt;&gt;Student Interview</span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Reschedule Information</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						<td width="5" background="images/left.gif"></td>
						<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr height="25">
									<td height="25" class="row-odd" ><div align="center">Application No:</div></td>
									<td class="row-even" align="center"><bean:write property="applnNo" name="interviewTimeChangeForm"/>
										</td>
									<td class="row-odd" align="center">Candidate Name:
									</td>
									<td class="row-even" align="center"><bean:write property="applicantName" name="interviewTimeChangeForm"/>
									</td>
									</tr></table>
						</td>
							
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr height="25">
									<td height="25" class="row-odd" ><div align="center"><bean:message key="knowledgepro.slno"/></div></td>
									<td class="row-odd" align="center">Date
										</td>
										<td class="row-odd" align="center">Reporting Time
									</td>
									<td class="row-odd" align="center">E-Admit Card Generate On
									</td>
								</tr>
									<logic:notEmpty name="interviewTimeChangeForm" property="rescheduledHistory">
										<nested:iterate name="interviewTimeChangeForm" property="rescheduledHistory" id="tos" indexId="count">
											<c:choose>
												<c:when test="${count%2 == 0}">
													<tr class="row-even" height="25">
												</c:when>
												<c:otherwise>
													<tr class="row-even" height="25">
												</c:otherwise>
											</c:choose>
											<td width="5%" height="25" class="row-even" ><div align="center"><c:out value="${count + 1}"/></div></td>
											<td align="center"><nested:write property="interviewDate" /></td>
											<td align="center"><nested:write property="reportingTime" /></td>
											<td align="center"><nested:write property="eAdmitCardGenerateOn" /></td>
										</nested:iterate>
									</logic:notEmpty>
							</table>
							</td>
							<td width="5" background="images/right.gif"></td>
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
							<td width="33%" height="35" align="right">&nbsp;</td>
							<td width="9%" height="35" align="center">&nbsp;</td>
							<td width="58%" height="35" align="left"><input
								name="Submit2" type="button" class="formbutton" value="Cancel"
								onclick="closeMe()" /></td>
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
</body>

</html:html>