<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
	
	<script type="text/javascript">
	
	function editDepartment(id) {
		document.getElementById("id").value=id;
		document.departmentEntryForm.method.value="editDepartmentEntry";
		document.departmentEntryForm.submit();
		}
		function deleteDepartment(id,department){
			deleteConfirm = confirm("Are you sure you want to delete this entry?");
			if(deleteConfirm){
				document.location.href = "departmentEntry.do?method=deleteDepartmentEntry&id=" +id + "&name=" +department;
			}
		}
		function reActivate(){
				var id=document.getElementById("id").value;
				document.location.href = "departmentEntry.do?method=activateDepartmentEntry&id=" +id;
			}
		function resetValues(){
			document.getElementById("department").value= "";
			document.getElementById("empStreamId").value = "";
			document.getElementById("code").value="";
			document.getElementById("email").value="";
			if(document.getElementById("method").value == "updateDepartmentEntry"){
				document.getElementById("department").value = document.getElementById("orgName").value;
				document.getElementById("empStreamId").value = document.getElementById("orgempStreamId").value;
				document.getElementById("code").value= document.getElementById("orgCode").value;

			}
			resetErrMsgs();
		}
		
	</script>
	<html:form action="/departmentEntry" method="post" focus="department">
	<html:hidden property="formName" value="departmentEntryForm"/> 
	<html:hidden property="pageType" value="1"/>
	<html:hidden property="id" styleId="id"/>
	<html:hidden property="dupId" styleId="dupId"/>
	<html:hidden property="orgName" styleId="orgName"/>
	<html:hidden property="orgempStreamId" styleId="orgempStreamId"/>
	<html:hidden property="orgCode" styleId="orgCode"/>

	<c:choose>
		<c:when test=""></c:when>
	</c:choose>
	<c:choose>
		<c:when test="${department != null && department == 'edit'}">
			<html:hidden property="method" styleId="method" value="updateDepartmentEntry" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method" value="addDepartmentEntry" />
		</c:otherwise>
	</c:choose>
	<table width="100%" border="0">
		<tr>

			<td><span class="Bredcrumbs">
				<bean:message key="knowledgepro.admin" />
			<span class="Bredcrumbs">&gt;&gt; <bean:message
				key="knowledgepro.admin.Department" /> &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message
						key="knowledgepro.admin.Department" /></strong></td>

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
									<div align="right"><span class="Mandatory">*</span>Department Name</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="name"
										styleClass="TextBox" styleId="department" size="30"
										maxlength="75" /> </span></td>
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Department Code(for Smart Card)</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="code"
										styleClass="TextBox" styleId="code" size="20"
										maxlength="30" /> </span></td>
								</tr>
								<tr>
								<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Stream</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> 
										<html:select property="empStreamId" styleClass="checkBox" styleId="empStreamId" >
										<html:option value="">
												<bean:message key="knowledgepro.select" />-</html:option>
															<html:optionsCollection name="empStreamMap" 
																label="value" value="key" />
																</html:select>
										</span></td>
								<td width="24%" height="25" class="row-odd"><div align="right" ><span class="Mandatory">*</span> Is Academic:</div></td>
	                          <td width="26%" class="row-even" colspan="3">
					           <html:radio property="isAcademic" name="departmentEntryForm" value="true" styleId="fixed">Yes</html:radio>
					           <html:radio property="isAcademic"  name="departmentEntryForm" value="false" styleId="fixed">No</html:radio>
					           
					           </td>
							 </tr>
			                  	<tr>
			
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Web-Id</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="webId"
										styleClass="TextBox" styleId="webId" size="30"
										 maxlength="9" onkeypress="return isNumberKey(event)"/> </span></td>
									<td width="25%" height="25" class="row-odd">
									<div align="right">H.O.D's E-Mail</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="email"
										styleClass="TextBox" styleId="email" size="20"
										maxlength="50" /> </span></td>
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
									test="${department != null && department == 'edit'}">
									<html:submit property="" styleClass="formbutton"
										onclick="updateDepartment()">
										<bean:message key="knowledgepro.update" />
									</html:submit>
								</c:when>
								<c:otherwise>
									<html:submit property="" styleClass="formbutton"
										onclick="addDepartment()">
										<bean:message key="knowledgepro.submit" />
									</html:submit>
								</c:otherwise>
							</c:choose></div>
							</td>
							<td width="2%"></td>
							<td width="53%"><c:choose>
								<c:when test="${department != null && department == 'edit'}">
									<html:reset property="" styleClass="formbutton">
										<bean:message key="knowledgepro.admin.reset" />
									</html:reset>
								</c:when>
								<c:otherwise>
									<html:button property="" styleClass="formbutton" value="Reset"
										onclick="resetFieldAndErrMsgs()">
										<bean:message key="knowledgepro.cancel" />
									</html:button>
								</c:otherwise>
							</c:choose></td>
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
									<td height="25" class="row-odd" align="center"><bean:message key="knowledgepro.admin.sec.Department"/></td>
									<td height="25" class="row-odd" align="center"><bean:message key="knowledgepro.department.code"/></td>
									<td height="25" class="row-odd" align="center"><bean:message key="employee.info.job.streamName"/></td> 	
									<td height="25" class="row-odd" align="center"><bean:message key="knowledgepro.studentFeedBack.IsAcademic"/></td>
									<td height="25" class="row-odd" align="center"><bean:message key="knowledgepro.department.webId"/></td>
									<td height="25" class="row-odd" align="center"><bean:message key="knowledgepro.department.hodsmail"/></td>
									<td class="row-odd" align="center">
									<div align="center"><bean:message key="knowledgepro.edit" /></div>
									</td>
									<td class="row-odd" align="center">
									<div align="center"><bean:message
										key="knowledgepro.delete" /></div>
									</td>
								</tr>
								<c:set var="temp" value="0" />
								<logic:notEmpty name="departmentEntryForm" property="departmentList">
								<logic:iterate name="departmentEntryForm" id="departmentEntry" property="departmentList" indexId="count">
									<c:choose>
										<c:when test="${temp == 0}">
											<tr>
												<td width="5%" height="25" class="row-even" align="center">
												<div align="center"><c:out value="${count+1}" /></div>
												</td>
												<td width="20%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="name"  /></td>
												<td width="10%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="code"  /></td>
												<td width="25%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="empStreamName"  /></td>
												<td width="10%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="isAcademic"  /></td>
												<td width="10%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="webId"  /></td>
												<td width="10%" height="25" class="row-even" align="center"><bean:write
													name="departmentEntry" property="email"  /></td>
												<td width="5%" height="25" class="row-even" align="center">
												<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18" style="cursor:pointer" 
													onclick="editDepartment('<bean:write name="departmentEntry" property="id"/>')">
												</div>
												</td>
												<td width="5%" height="25" class="row-even" align="center">
												<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16" style="cursor:pointer" 
													onclick="deleteDepartment('<bean:write name="departmentEntry" property="id"/>','<bean:write name="departmentEntry" property="name"/>')">
												</div>

												</td>
											</tr>
											<c:set var="temp" value="1" />
										</c:when>
										<c:otherwise>
											<tr>
												<td width="5%" height="25" class="row-white" align="center">
												<div align="center"><c:out value="${count+1}" /></div>
												</td>

												<td width="20%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="name" /></td>
												<td width="10%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="code"  /></td>
												<td width="25%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="empStreamName"  /></td>
												<td width="10%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="isAcademic"  /></td>
												<td width="10%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="webId" /></td>
												<td width="10%" height="25" class="row-white" align="center"><bean:write
													name="departmentEntry" property="email"  /></td>
												<td width="5%" height="25" class="row-white" align="center">
												<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18" style="cursor:pointer"
													onclick="editDepartment('<bean:write name="departmentEntry" property="id"/>')">
												</div>
												</td>
												<td width="5%" height="25" class="row-white" align="center">
												<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16" style="cursor:pointer"
													onclick="deleteDepartment('<bean:write name="departmentEntry" property="id"/>','<bean:write name="departmentEntry" property="name"/>')"></div>
												</td>
											</tr>
											<c:set var="temp" value="0" />
										</c:otherwise>
									</c:choose>
								</logic:iterate>
								</logic:notEmpty>
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