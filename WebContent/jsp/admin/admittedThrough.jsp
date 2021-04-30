<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<script type="text/javascript">
	function editAdmittedThrough(admThroughId,admThrough,admThroughCode) {
		document.getElementById("method").value = "updateAdmittedThrough";
		document.getElementById("admittedThroughId").value = admThroughId;
		document.getElementById("admittedThrough").value = admThrough;
		document.getElementById("origAdmittedThrough").value = admThrough;
		document.getElementById("admittedThroughCode").value = admThroughCode;
		document.getElementById("origAdmittedThroughCode").value = admThroughCode;
		document.getElementById("submitbutton").value = "Update";
		resetErrMsgs();
	}
	function deleteAdmittedThrough(admittedThroughId, admThrough,admThroughCode) {
		deleteConfirm = confirm("Are you sure you want to delete this entry?")
		if (deleteConfirm) {
			document.location.href = "AdmittedThrough.do?method=deleteAdmittedThrough&admittedThroughId="
					+ admittedThroughId + "&admittedThrough=" + admThrough + "&admittedThroughCode=" +admThroughCode;
		}
	}
	function reActivate() {
		var id = document.getElementById("admittedThroughId").value;
		document.location.href = "AdmittedThrough.do?method=activateAdmittedThrough&admittedThroughId="
				+ id;
	}
	function resetValues() {
		document.getElementById("admittedThrough").value = "";
		document.getElementById("admittedThroughCode").value = "";
		if (document.getElementById("method").value == "updateAdmittedThrough") {
			document.getElementById("admittedThrough").value = document
					.getElementById("origAdmittedThrough").value;
			document.getElementById("admittedThroughCode").value = document
					.getElementById("origAdmittedThroughCode").value;
		}
		resetErrMsgs();
	}
</script>

<html:form action="/AdmittedThrough" focus="admittedThrough">
	<c:choose>
		<c:when test=""></c:when>
	</c:choose>
	<c:choose>
		<c:when test="${admOperation != null && admOperation == 'edit'}">
			<html:hidden property="method" styleId="method"
				value="updateAdmittedThrough" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method"
				value="addAdmittedThrough" />
		</c:otherwise>
	</c:choose>

	<html:hidden property="admittedThroughId" styleId="admittedThroughId" />
	<html:hidden property="origAdmittedThrough"
		styleId="origAdmittedThrough" />
		<html:hidden property="origAdmittedThroughCode"
		styleId="origAdmittedThroughCode" />
	<html:hidden property="formName" value="admittedThroughForm" />
	<html:hidden property="pageType" value="1" />

	<table width="100%" border="0">
		<tr>

			<td><span class="Bredcrumbs">
				<bean:message key="knowledgepro.admin" />
			<span class="Bredcrumbs">&gt;&gt; <bean:message
				key="knowledgepro.admin.admittedthroughentry" /> &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message
						key="knowledgepro.admin.admittedthroughentry" /></strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="122" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" cellspacing="1" cellpadding="2">
						<tr bgcolor="#FFFFFF">
							<td height="20" colspan="2">
							<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
							<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
							<FONT color="green"> <html:messages id="msg"
								property="messages" message="true">
								<c:out value="${msg}" escapeXml="false"></c:out>
								<br>
							</html:messages> </FONT></div>
							</td>
						</tr>


						<tr>
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
									<td valign="top">
									<table width="100%" cellspacing="1" cellpadding="2">
			
			
								<tr>
			
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.admin.admitted.through" /></div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="admittedThrough"
										styleClass="TextBox" styleId="admittedThrough" size="20"
										maxlength="30" /> </span></td>
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.admin.admitted.through.code" /></div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="admittedThroughCode"
										styleClass="TextBox1" styleId="admittedThroughCode" size="20"
										maxlength="35" /> </span></td>	
								</tr>
			
			
									</table>
									</td>
									<td width="5" height="29" background="images/right.gif"></td>
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
					<td height="25" colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="right"><c:choose>
								<c:when
									test="${admOperation != null && admOperation == 'edit'}">
									<html:submit property="" styleClass="formbutton"
										value="Update" styleId="submitbutton">
									</html:submit>
								</c:when>
								<c:otherwise>
									<html:submit property="" styleClass="formbutton"
										value="Submit" styleId="submitbutton">
									</html:submit>
								</c:otherwise>
							</c:choose></div>
							</td>
							<td width="2%"></td>
							<td width="53%"><html:button property=""
								styleClass="formbutton" value="Reset" onclick="resetValues()"></html:button></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
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
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">


						<tr>

							<td height="25" colspan="2">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
									<td height="25" class="row-odd" align="center">
									<div align="center"><bean:message key="knowledgepro.slno" /></div>
									</td>
									<td height="25" class="row-odd" align="center"><bean:message
										key="knowledgepro.admin.admittedthrough" /></td>
									<td height="25" class="row-odd" align="center"><bean:message
										key="knowledgepro.admin.admittedthroughcode" /></td>
									<td class="row-odd" align="center">
									<div align="center"><bean:message key="knowledgepro.edit" /></div>
									</td>
									<td class="row-odd" align="center">
									<div align="center"><bean:message
										key="knowledgepro.delete" /></div>
									</td>
								</tr>
								<c:set var="temp" value="0" />
								<logic:iterate name="admittedThroughList" id="admittedThrough"
									type="com.kp.cms.to.admin.AdmittedThroughTO" indexId="count">
									<c:choose>
										<c:when test="${temp == 0}">
											<tr>
												<td width="9%" height="25" class="row-even" align="center">
												<div align="center"><c:out value="${count+1}" /></div>
												</td>
												<td width="36%" height="25" class="row-even" align="center"><bean:write
													name="admittedThrough" property="name"  /></td>
												<td width="36%" height="25" class="row-even" align="center"><bean:write
													name="admittedThrough" property="admittedThroughCode"  /></td>
												<td width="10%" height="25" class="row-even" align="center">
												<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18" style="cursor:pointer" 
													onclick="editAdmittedThrough('<bean:write name="admittedThrough" property="id"/>','<bean:write name="admittedThrough" property="name"/>','<bean:write name="admittedThrough" property="admittedThroughCode"/>')">
												</div>
												</td>
												<td width="9%" height="25" class="row-even" align="center">
												<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16" style="cursor:pointer" 
													onclick="deleteAdmittedThrough('<bean:write name="admittedThrough" property="id"/>','<bean:write name="admittedThrough" property="name"/>','<bean:write name="admittedThrough" property="admittedThroughCode"/>')">
												</div>

												</td>
											</tr>
											<c:set var="temp" value="1" />
										</c:when>
										<c:otherwise>
											<tr>
												<td height="25" class="row-white" align="center">
												<div align="center"><c:out value="${count+1}" /></div>
												</td>

												<td height="25" class="row-white" align="center"><bean:write
													name="admittedThrough" property="name" /></td>
												<td width="36%" height="25" class="row-white" align="center"><bean:write
													name="admittedThrough" property="admittedThroughCode"  /></td>
												<td height="25" class="row-white" align="center">
												<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18" style="cursor:pointer"
													onclick="editAdmittedThrough('<bean:write name="admittedThrough" property="id"/>','<bean:write name="admittedThrough" property="name"/>','<bean:write name="admittedThrough" property="admittedThroughCode"/>')">
												</div>
												</td>
												<td height="25" class="row-white" align="center">
												<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16" style="cursor:pointer"
													onclick="deleteAdmittedThrough('<bean:write name="admittedThrough" property="id"/>','<bean:write name="admittedThrough" property="name"/>','<bean:write name="admittedThrough" property="admittedThroughCode"/>')"></div>
												</td>
											</tr>
											<c:set var="temp" value="0" />
										</c:otherwise>
									</c:choose>
								</logic:iterate>
							</table>
							</td>
						</tr>
							</table>
							</td>
							<td width="5" height="29" background="images/right.gif"></td>
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


					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>

				</tr>

				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" valign="top"></td>
					<td width="13" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif" width="9" height="29" /></td>
					<td width="100%" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29" /></td>
				</tr>

			</table>
			</td>
		</tr>
	</table>
</html:form>
