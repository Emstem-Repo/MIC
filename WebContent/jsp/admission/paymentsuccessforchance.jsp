<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.to.admission.AdmissionStatusTO"%>
<%@page import="com.kp.cms.forms.admission.AdmissionStatusForm"%><script language="JavaScript" src="js/calendar_us.js"></script>

<link rel="stylesheet" href="css/calendar.css">
<link href="css/styles.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
<link rel="stylesheet" href="jquery/Zebra/css/default/zebra_dialog.css"  />



<script language="javaScript" type="text/javascript">

function cancel(){
	document.getElementById("appNo").value=null;
	document.getElementById("dateOfBirth").value=null;
	document.getElementById("method").value="initOutsideAccessAdmissionStatusOfStudent";
	document.admissionStatusForm.submit();
}

</script>

<html:form action="/AdmissionStatus" method="post">
	<html:hidden property="method" styleId="method" value="" />
	<html:hidden property="formName" value="admissionStatusForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="serverDownMessage" styleId="serverDownMessage" name="admissionStatusForm" />
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
								<td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
								<td background="images/Tcenter.gif" class="body">
									<div align="left">
										<strong class="boxheader"><bean:message key="knowledgepro.admission.applicationstatus" /></strong>
									</div>
								</td>
								<td width="10"><img src="images/Tright_1_01.gif" width="9" height="29"></td>
							</tr>
							<tr>
								<td height="122" valign="top" background="images/Tright_03_03.gif"></td>
								<td valign="top" class="news">
									<table width="100%" cellspacing="1" cellpadding="2">
										<tr>
											<td height="45" colspan="4">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														
														
														<td width="53%" align="center">
															<logic:equal value="true" name="admissionStatusForm" property="isPaid">
																<font color="green" size="14">Your payment is Successful.</font>
															</logic:equal>
															<logic:equal value="false" name="admissionStatusForm" property="isPaid">
																<font color="red" size="5">Your payment is failed.</font>
															</logic:equal>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td height="45" colspan="4" >
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														
														
														<td width="53%" align="center">
															<html:button property=""
																		 styleClass="formbutton" 
																		 value="Back"
																		 onclick="cancel()" />
														</td>
													</tr>
												</table>
											</td>
										</tr>															
																				
																			</table>
									<div align="center">
										<table width="100%" height="27" border="0" cellpadding="0"
											cellspacing="0">
											<tr><td><div align="center"></div></td></tr>
											<tr><td>&nbsp;</td></tr>
										</table>
									</div>
								</td>
								<td width="10" valign="top" background="images/Tright_3_3.gif" class="news"></td>
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
