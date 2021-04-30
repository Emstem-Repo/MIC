<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
    <script type="text/javascript" src="js/AC_RunActiveContent.js"></script>
    <script type="text/javascript" src="js/sdmenu/sdmenu.js"></script>
    <script type="text/javascript" src="js/ajax/Ajax.js"></script>
    <script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
    <script type="text/javascript" src="js/leftSwitchMenu.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script>
	function cancelAction() {
		document.location.href = "ParentLoginAction.do?method=returnHomePage";
	}
	</script>
<html:form action="/studentWiseAttendanceSummary" >
<html:hidden property="formName" value="studentWiseAttendanceSummaryForm"/>
<html:hidden property="pageType" value="1"/>

<table width="100%" border="0">
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader">Absence Details</strong></td>

					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr height="10%">
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" height="10%">
					
					<FONT color="black" size="2px">
					 <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>

							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="1" border="0" >
									 <logic:empty name="studentWiseAttendanceSummaryForm" property="attList">
									 	No Abscent Records
									 </logic:empty>
									 <logic:notEmpty name="studentWiseAttendanceSummaryForm" property="attList">
										 <tr>
											<td class="studentrow-odd">Date </td>
											<td  class="studentrow-odd">
											<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Day </div>
											</td>
											<logic:iterate id="periodName" name="studentWiseAttendanceSummaryForm" property="periodNameList">
												<td  class="studentrow-odd">
												<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<bean:write name="periodName"/>
												</div>
												</td>
											</logic:iterate>
											<td class="studentrow-odd">Total</td>
											</tr>
										<logic:notEmpty name="studentWiseAttendanceSummaryForm" property="attList">
											<logic:iterate id="to" name="studentWiseAttendanceSummaryForm" property="attList">
												<tr class="studentrow-even">
												<td><bean:write name="to" property="date"/> </td>
											<td><bean:write name="to" property="day"/> </td>
											<logic:iterate id="pto" name="to" property="periodList">
												<td class="studentrow-even">
												<div align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<logic:equal value="true" name="pto" property="coLeave">
													<font color="green"><bean:write name="pto" property="periodName"/></font>
												</logic:equal>
												<logic:equal value="false" name="pto" property="coLeave">
													<bean:write name="pto" property="periodName"/>
												</logic:equal>
												</div>
												</td>
											</logic:iterate>
											<td><bean:write name="to" property="hoursHeldByDay"/> </td>
												</tr>
											</logic:iterate>
										</logic:notEmpty>
										</logic:notEmpty>
									</table>
										
											<table>
											<tr>
											<td colspan="6">Periods Marked In Green Are Cocurricular Leave</td>
											</tr>
											<tr>
											<td width="20%">NO OF PERIODS ABSENT </td>
											<td width="10%"><bean:write name="studentWiseAttendanceSummaryForm" property="total"/> </td>
											<td width="20%">TOTAL COCURRICULAR</td>
											<td width="10%"><bean:write name="studentWiseAttendanceSummaryForm" property="totalCoLeave"/> </td>
											<td width="20%">TOTAL PERIODS ABSENT </td>
											<td width="10%"><bean:write name="studentWiseAttendanceSummaryForm" property="abscent"/> </td>
											</tr>
											</table>
											
											<table>
												<logic:iterate id="sub" name="studentWiseAttendanceSummaryForm" property="subMap">
													<tr> 
														<td><bean:write name="sub" property="key"/> </td>
														<td><bean:write name="sub" property="value"/> </td>
													</tr>
												</logic:iterate>
											</table>
										
									
							<table>
								<tr class="row-white">
								<td width="75%"></td><td width="25"></td>
                   						<td colspan="2"><div align="center">
										<html:button property="" value="Cancel" styleClass="btnbg" onclick="cancelAction()"></html:button>
										</div></td>
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
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>

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