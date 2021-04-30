<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<script type="text/javascript">
	function addAcademicYearDetails(){		
		document.getElementById("method").value="addAcademicYearEntry";
		document.getElementById("academicYearId").value=document.getElementById("academicYearSelId").options[document.getElementById("academicYearSelId").selectedIndex].text;
	}
	
	function editAcademicYearDetails(id) {
		document.location.href = "academicYear.do?method=editAcademicYearDetails&id="+id;
	}
	function updateAcademicYearDetails() {
		document.getElementById("academicYearId").value=document.getElementById("academicYearSelId").value;
		document.getElementById("method").value = "updateAcademicYearDetails";
	}
	function reActivate() {
		var academicYear = document.getElementById("academicYearSelId").value;
		document.location.href = "academicYear.do?method=reActivateAcademicYear&academicYear=" + academicYear;
	}
	function deleteAcademicYearDetails(id) {
		deleteConfirm = confirm("Are you sure want to delete this entry?")
		if (deleteConfirm) {
			document.location.href = "academicYear.do?method=deleteAcademicYearDetails&id="+ id;
		}
	}
	function resetMessages() {	
		document.getElementById("academicYearSelId").value = resetYear();
		document.getElementById("isCurrentId").checked=false;
		document.getElementById("isCurrentId1").checked=false;
		document.getElementById("isCurrentForAdmissionId").checked=false;
		document.getElementById("isCurrentForAdmissionId1").checked=false;
		resetErrMsgs();
	}	
</script>
<html:form action="/academicYear" method="post">
	<html:hidden property="method" styleId="method" />
	<html:hidden property="formName" value="academicYearForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="academicYear" styleId="academicYearId"
		value="" />
	<html:hidden property="id" />

	<table width="99%" border="0">
		<tr>
			<td><span class="heading"><bean:message
				key="knowledgepro.admin" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.admin.academicyear.academicYear" /> &gt;&gt;</span> </span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white"><bean:message
						key="knowledgepro.admin.academicyear.academicYear" /></td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"><span class='MandatoryMark'> <bean:message
						key="knowledgepro.mandatoryfields" /></span></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="44" valign="top" background="images/Tright_03_03.gif"></td>
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
									<td width="20%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.admin.academicyear.academicYear" /> :</div>
									</td>
									<td width="30%" class="row-even"><input type="hidden"
										id="yr" name="yr"
										value='<bean:write name="academicYearForm" property="academicYearSel"/>' />
									<html:select property="academicYearSel"
										styleId="academicYearSelId" styleClass="combo">
										<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<cms:renderYear></cms:renderYear>
									</html:select></td>
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message
										key="knowledgepro.admin.academicyear.iscurrent" /></div>
									</td>
									<td width="25%" class="row-even">
									<table width="90%" border="0" cellspacing="0" cellpadding="0"
										class="row-even">
										<tr>
											<td><html:radio property="isCurrent"
												styleId="isCurrentId" value="true">
												<bean:message key="knowledgepro.yes" />
											</html:radio> <html:radio property="isCurrent" styleId="isCurrentId1"
												value="false">
												<bean:message key="knowledgepro.no" />
											</html:radio></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
								<td width="20%" height="25" class="row-odd">
								<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.admin.academicyear.iscurrentforadmission" /></div>
									</td>
									<td class="row-even" colspan="3"><html:radio property="isCurrentForAdmission"
												styleId="isCurrentForAdmissionId" value="true">
												<bean:message key="knowledgepro.yes" />
											</html:radio> <html:radio property="isCurrentForAdmission" styleId="isCurrentForAdmissionId1"
												value="false">
												<bean:message key="knowledgepro.no" />
											</html:radio></td>
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
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="122" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" cellspacing="1" cellpadding="2">
						<tr bgcolor="#FFFFFF">
							<td width="100%" height="20" colspan="4">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="45%" height="35">
									<div align="right"><c:choose>
										<c:when test="${operation == 'edit'}">
											<html:submit property="" styleClass="formbutton"
												onclick="updateAcademicYearDetails()">
												<bean:message key="knowledgepro.update" />
											</html:submit>
										</c:when>
										<c:otherwise>
											<html:submit property="" styleClass="formbutton"
												onclick="addAcademicYearDetails()">
												<bean:message key="knowledgepro.submit" />
											</html:submit>
										</c:otherwise>
									</c:choose></div>
									</td>
									<td width="2%"></td>
									<td width="53%"><c:choose>
										<c:when test="${operation == 'edit'}">
											<html:cancel styleClass="formbutton">
												<bean:message key="knowledgepro.admin.reset" />
											</html:cancel>
										</c:when>
										<c:otherwise>
											<html:button property="" styleClass="formbutton"
												value="Reset" onclick="resetMessages()">
												<bean:message key="knowledgepro.cancel" />
											</html:button>
										</c:otherwise>
									</c:choose></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td height="45" colspan="4">
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
											<td height="25" class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.slno" /></div>
											</td>
											<td width="25%" height="25" class="row-odd" align="center"><bean:message
												key="knowledgepro.admin.academicyear.academicYear" /></td>
											<td width="25%" height="25" class="row-odd" align="center"><bean:message
												key="knowledgepro.admin.academicyear.iscurrent" /></td>
											<td width="25%" height="25" class="row-odd" align="center"><bean:message
												key="knowledgepro.admin.academicyear.iscurrentforadmission" /></td>
											<td class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.edit" /></div>
											</td>
											<td class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.delete" /></div>
											</td>
										</tr>
										<c:set var="temp" value="0" />
										<logic:notEmpty name="academicYearForm"
											property="academicYearList">
											<logic:iterate id="academicYr" name="academicYearForm"
												property="academicYearList" indexId="count">

												<c:choose>
													<c:when test="${temp == 0}">
														<tr>
															<td width="6%" height="25" class="row-even">
															<div align="center"><c:out value="${count + 1}" /></div>
															</td>
															<td width="25%" class="row-even" align="center"><bean:write
																name="academicYr" property="year" /></td>
															<td width="25%" height="25" class="row-even"
																align="center"><bean:write name="academicYr"
																property="isCurrent" /></td>
															<td width="25%" height="25" class="row-even"
																align="center"><bean:write name="academicYr"
																property="isCurrentForAdmission" /></td>
															<td width="6%" height="25" class="row-even">
															<div align="center"><img src="images/edit_icon.gif"
																width="16" height="18" style="cursor: pointer"
																onclick="editAcademicYearDetails('<bean:write name="academicYr" property="id" />')" />
																
																
															</div>
															</td>
															<td width="6%" height="25" class="row-even">
															<div align="center"><img
																src="images/delete_icon.gif" width="16" height="16"
																style="cursor: pointer"
																onclick="deleteAcademicYearDetails('<bean:write name="academicYr" property="id"/>')" />
															</div>
															</td>
														</tr>
														<c:set var="temp" value="1" />
													</c:when>
													<c:otherwise>
														<tr>
															<td width="6%" height="25" class="row-white">
															<div align="center"><c:out value="${count + 1}" /></div>
															</td>
															<td width="25%" class="row-white" align="center"><bean:write
																name="academicYr" property="year" /></td>
															<td width="25%" height="25" class="row-white"
																align="center"><bean:write name="academicYr"
																property="isCurrent" /></td>
															<td width="25%" height="25" class="row-white"
																align="center"><bean:write name="academicYr"
																property="isCurrentForAdmission" /></td>
															<td width="6%" height="25" class="row-white">
															<div align="center"><img src="images/edit_icon.gif"
																width="16" height="18" style="cursor: pointer"
																onclick="editAcademicYearDetails('<bean:write name="academicYr" property="id" />')" />
																
															</div>
															</td>
															<td width="6%" height="25" class="row-white">
															<div align="center"><img
																src="images/delete_icon.gif" width="16" height="16"
																style="cursor: pointer"
																onclick="deleteAcademicYearDetails('<bean:write name="academicYr" property="id"/>')" />
															</div>
															</td>
														</tr>
														<c:set var="temp" value="0" />
													</c:otherwise>
												</c:choose>

											</logic:iterate>
										</logic:notEmpty>
									</table>
									</td>
									<td width="5" height="30" background="images/right.gif"></td>
								</tr>
								<tr>
									<td height="5"><img src="images/04.gif" width="5"
										height="5" /></td>
									<td background="images/05.gif"></td>
									<td><img src="images/06.gif" /></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td height="26" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news"></td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
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
	var yearId = document.getElementById("yr").value;
	if (yearId != null && yearId.length != 0) {
		document.getElementById("academicYearSelId").value = yearId;
	}
</script>