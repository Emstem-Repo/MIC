<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/calendar_us.js"></script>
<script type="text/javascript">
function editEmployeeInitialize(id){
	document.location.href = "empInitializeLeaves.do?method=editEmployeeInitialize&id="+ id;
}
function deleteEmployeeInitialize(id){
	deleteConfirm = confirm("Are you sure you want to delete this entry?")
	if (deleteConfirm) {
		document.location.href = "empInitializeLeaves.do?method=deleteEmployeeInitialize&id="+ id;
	}
}
function reActivate(){
	document.location.href = "empInitializeLeaves.do?method=reActivateEmployeeInitialize";
}
function resetFormFields(){	
	resetFieldAndErrMsgs();
	
}
</script>
<html:form action="/empInitializeLeaves">	
	<c:choose>
		<c:when test="${operation == 'edit'}">
		<html:hidden property="method" styleId="method" value="updateEmployeeInitialize" />
		</c:when>
		<c:otherwise>
	<html:hidden property="method" styleId="method" value="addEmployeeInitialize" />
	</c:otherwise>
	</c:choose>
	
	<html:hidden property="formName" value="employeeInitializeLeavesForm" />
	<html:hidden property="pageType" value="1" />
	<table width="100%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin.sec.EmployeeCategory"/>
			<span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.employee.leaves.initialize.label"/>
			 &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message key="knowledgepro.employee.leaves.initialize.label"/></strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
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
					<td height="41" valign="top" background="images/Tright_03_03.gif"></td>

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
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span> <bean:message key="employee.info.job.emptype"/> </div>
									</td>
									<td  class="row-even">
									<div align="left">
									<html:select name="employeeInitializeLeavesForm" property="empTypeId" styleClass="TextBox">
					                <html:option value=""> <bean:message key="knowledgepro.pettycash.Select"/> </html:option>
					                <logic:notEmpty name="employeeInitializeLeavesForm" property="listEmployeeType">
           				                <html:optionsCollection name="employeeInitializeLeavesForm" property="listEmployeeType" label="name" value="id"/>
           				                </logic:notEmpty>
					                </html:select>
									</div>
									</td>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span> <bean:message key="knowledgepro.employee.leaves.initialize.alloted.leaves"/> </div>
									</td>
									<td class="row-even">
									<html:text property="allotedLeaves" name="employeeInitializeLeavesForm" maxlength="4" styleId="allotedLeaves" onkeypress="return isNumberKey(event)"></html:text>
									</td>
								</tr>
								<tr>
								<td  class="row-odd" ><div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.attendance.activityattendence.leavetype"/>:</div></td>
				                <td class="row-even">
				                <html:select name="employeeInitializeLeavesForm" property="leaveTypeId" styleId="leaveId" styleClass="combo">
									<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
									<html:optionsCollection name="employeeInitializeLeavesForm" property="leaveList" label="name" value="id" />
								</html:select> 
				                
				                </td>
				                <td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span> <bean:message key="knowledgepro.employee.leaves.initialize.isinitialize.required"/> </div>
									</td>
				                <td class="row-even">
					                <input type="radio" name="isInitializeRequired" id="feeExemption_1" value="true"/> <bean:message key="knowledgepro.yes"/>
		                   			 <input type="radio" name="isInitializeRequired" id="feeExemption_2" value="false"/> <bean:message key="knowledgepro.no"/>
									<script type="text/javascript">
										var isInitializeRequired = "<bean:write name='employeeInitializeLeavesForm' property='isInitializeRequired'/>";
										if(isInitializeRequired == "true") {
						                        document.getElementById("feeExemption_1").checked = true;
										}	
										if(isInitializeRequired == "false") {
					                        document.getElementById("feeExemption_2").checked = true;
									}
									</script>
					                </td>  
								</tr>
								<tr>
		               				<td class="row-odd"><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.employee.leaves.initialize.alloted.Date" /></div></td>
		              				 <td class="row-even" colspan="3">
										<html:text name="employeeInitializeLeavesForm" property="allotedDate" styleId="allotedDate" size="16" maxlength="10"/>
									<script	language="JavaScript">
										new tcal( {
											// form name
											'formname' :'employeeInitializeLeavesForm',
											// input name
											'controlname' :'allotedDate'
											});
									</script>
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
				<tr>
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
									<c:choose>
							<c:when test="${operation == 'edit'}">
							<td width="45%" height="35">
							<div align="right">
									<html:submit property="" styleClass="formbutton" value="Update"
										styleId="submitbutton">
									</html:submit>
								</div>
							</td>
							<td width="2%"></td>
							<td width="53%"><html:cancel value="Reset" styleClass="formbutton" ></html:cancel></td>
							</c:when>
							<c:otherwise>
								<td width="45%" height="35">
							<div align="right">
									<html:submit property="" styleClass="formbutton" value="submit"
										styleId="submitbutton">
									</html:submit>
								</div>
							</td>
							<td width="2%"></td>
							<td width="53%"><html:button property=""
							styleClass="formbutton" value="Reset"
							onclick="resetFormFields()"></html:button></td>
							</c:otherwise>
							</c:choose>
						</tr>
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>	
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
						<td valign="top" class="news" >
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0">
								<tr>
									<td><img src="images/01.gif" width="5" height="5"></td>
									<td width="914" background="images/02.gif"></td>
									<td><img src="images/03.gif" width="5" height="5"></td>
								</tr>
								<tr>
									<td width="5" background="images/left.gif"></td>
									<td valign="top">
									<table width="100%" cellspacing="1" cellpadding="2">

										<tr>
											<td height="25" class="row-odd">
											<div align="center"><bean:message key="knowledgepro.slno" /></div>
											</td>
											<td class="row-odd">
											<div align="center"><bean:message
											key="employee.info.job.emptype" /></div>
											</td>
											<td class="row-odd">
											<div align="center"><bean:message
											key="knowledgepro.employee.leaves.initialize.alloted.leaves" /></div>
											</td>
											<td class="row-odd">
											<div align="center"><bean:message
											key="knowledgepro.attendance.activityattendence.leavetype" /></div>
											</td>
											<td  class="row-odd">
											<div align="center"><bean:message
											key="knowledgepro.employee.leaves.initialize.isinitialize.required" /></div>
											</td>
											<td   class="row-odd">
											<div align="center"><bean:message key="knowledgepro.edit" /></div>
											</td>
											<td  class="row-odd">
											<div align="center"><bean:message
										key="knowledgepro.delete" /></div>
											</td>
										</tr>
										<logic:notEmpty name="employeeInitializeLeavesForm" property="list">
										<logic:iterate id="dList" name="employeeInitializeLeavesForm" property="list"
											indexId="count">
										<tr>
											<c:choose>
												<c:when test="${count%2 == 0}">
													<tr class="row-even">
												</c:when>
												<c:otherwise>
													<tr class="row-white">
												</c:otherwise>
											</c:choose>
											<td height="25">
											<div align="center"><c:out value="${count + 1}" /></div>
											</td>
											<td align="center"><bean:write name="dList"
												property="empTypeName" /></td>
												<td align="center"><bean:write name="dList"
												property="allotLeaves" /></td>
												<td align="center"><bean:write name="dList"
												property="leaveTypeName" /></td>
											<td align="center"><bean:write name="dList"
												property="isInitializeRequired" /></td>
											<td height="25" align="center">
											<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18"
													onclick="editEmployeeInitialize('<bean:write name="dList" property="id" />')" /></div>
											</td>
											<td height="25">
											<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16"
													onclick="deleteEmployeeInitialize('<bean:write name="dList" property="id" />')" /></div>
											</td>
											</tr>	
										</logic:iterate>
										</logic:notEmpty>
									</table>
									</td>
									<td width="5" height="30" background="images/right.gif"></td>
								</tr>
								<tr>
									<td height="5"><img src="images/04.gif" width="5"
										height="5"></td>
									<td background="images/05.gif"></td>
									<td><img src="images/06.gif"></td>
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
					<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>

		</tr>
	</table>
</html:form>
