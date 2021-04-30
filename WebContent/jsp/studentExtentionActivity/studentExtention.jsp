<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<script type="text/javaScript">
	function editEntry(id, activityName) 
	{
		document.getElementById("id").value = id;
		document.getElementById("activityName").value = activityName;
		document.getElementById("origActivityName").value = activityName;
		document.getElementById("method").value = "updateStudentExtentionDetails";
		document.getElementById("submitbutton").value="Update";
	}
	function deleteEntry(id, activityName) 
	{
		deleteConfirm = confirm("Are you sure you want to delete this entry?");
		if (deleteConfirm) 
		{
			 document.location.href="studentExtention.do?method=deleteStudentExtentionDetails&id=" + id + "&activityName=" + activityName;
		}
	}
	function reActivate()
	{
		var dupId = document.getElementById("dupId").value;
		document.location.href = "studentExtention.do?method=reActivateStudentExtentionDetails&dupId=" + dupId;
	}
	function resetFormFields()
	{	
		document.getElementById("activityName").value = null;
		document.getElementById("order").value = null;
		document.getElementById("submitbutton").value="Submit";
		document.getElementById("method").value="submitStudentExtentionDetails";
		resetErrMsgs();
	}
    function getname(id){
    //    alert(id);
    }


</script>


<html:form action="/studentExtention" method="POST">
    <html:hidden property="formName" value="studentExtentionForm"/>
    <html:hidden property="pageType" value="1"/>
	<html:hidden property="id" styleId="id"/>
	<html:hidden property="origActivityName" styleId="origActivityName"/>
	<html:hidden property="dupId" styleId="dupId"/>
     
     
      <c:choose>
		<c:when test="${admOperation != null && admOperation == 'edit'}">
			<html:hidden property="method" styleId="method" value="updateStudentExtentionDetails" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method" value="submitStudentExtentionDetails" />
		</c:otherwise>
	</c:choose>
     
     <table width="100%" border="0">
		   <tr>
			<td>
				<span class="Bredcrumbs">
					<bean:message key="knowledgepro.studentExtentionActivity"/>
					&gt;&gt;
					<bean:message key="knowledgepro.studentExtentionActivity.module.entry"/>
					&gt;&gt;
				</span>
			</td>
		  </tr>
		
		<tr>
		  <td valign="top">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		          <tr>
						<td width="9">
							<img src="images/Tright_03_01.gif" width="9" height="29">
						</td>
						<td background="images/Tcenter.gif" class="body">
							<strong class="boxheader"> 
								<bean:message key="knowledgepro.studentExtentionActivity"/>
							</strong>
						</td>
						<td width="10">
							<img src="images/Tright_1_01.gif" width="9" height="29">
						</td>
					</tr>
		            <tr>
						<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
						<td valign="top" class="news">
							<div align="right">
								<FONT color="red">
									 <span class='MandatoryMark'>
									 	<bean:message key="knowledgepro.mandatoryfields"/>
									 </span>
								</FONT>
							</div>
							<div id="errorMessage">
								<FONT color="red">
									<html:errors/>
								</FONT>
								<FONT color="green">
									 <html:messages id="msg" property="messages" message="true">
										<c:out value="${msg}" escapeXml="false"></c:out>
										<br>
									</html:messages>
							    </FONT>
						    </div>
						</td>
						<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
					</tr>
		            <tr>
						<td height="41" valign="top" background="images/Tright_03_03.gif"></td>
						<td valign="top" class="news">
							<table width="100%"
								   border="0" 
								   align="center" 
								   cellpadding="0"
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
											      <td align="center" width="25%" height="25" class="row-odd"><bean:message key="knowledgepro.studentExtentionActivity.text"/></td>
											      <td height="25" class="row-odd" colspan="100">
													<html:select property="studentGroupId" name="studentExtentionForm" styleId="studentGroupId">
														<html:option value=""><bean:message key="knowledgepro.select"/></html:option>
										   				<logic:notEmpty property="list" name="studentExtentionForm">
										   					<html:optionsCollection property="list" label="groupName" value="id"/>
										   				</logic:notEmpty>
													</html:select>
									            </td>
										    </tr>
											<tr>
													<td align="right" width="25%" height="25" class="row-odd"><bean:message key="knowledgepro.studentExtentionActivity.fieldname"/></td>
													<td width="25%" height="25" class="row-even"><html:textarea name="studentExtentionForm" property="activityName" 
											                                                                     styleId="activityName" rows="4" cols="50">
											                                                     </html:textarea></td>
													<td align="right" width="25%" height="25" class="row-odd"><bean:message key="knowledgepro.studentExtentionActivity.order"/></td>
													<td width="25%" height="25" class="row-even"><html:text property="displayOrder" name="studentExtentionForm" styleId="order" onkeypress="return onlyDecimalNumber(this.value, event)"/></td>
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
									<td width="50%" height="35">
										<div align="right">
										<input type="submit" class="formbutton" value="Submit" id="submitbutton" onclick="submitdetails()"/>
										</div>
									</td>
									<td width="2%"></td>
									<td width="50%">
										<html:button property=""
													 styleClass="formbutton" 
													 value="Reset"
						 							 onclick="resetFormFields()">
										</html:button>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
	                 <td height="25" colspan="6">
							<table width="100%" 
								   border="0" 
								   align="center" 
								   cellpadding="0"
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
												<td width="10%" height="25" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.slno" /></u>
													</div>
												</td>
												<td width="15%" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.studentExtentionActivity.fieldname" /></u>
													</div>
												</td>
												<td width="20%" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.studentExtentionActivity.text" /></u>
													</div>
												</td>
												<td width="15%" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.studentExtentionActivity.order" /></u>
													</div>
												</td>
												<td width="20%" height="25" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.edit" /></u>
													</div>
												</td>
												<td width="20%" height="25" class="row-odd">
													<div align="center">
														<u><bean:message key="knowledgepro.delete" /></u>
													</div>
												</td>
											</tr>
											<logic:notEmpty name="studentExtentionForm" property="subjectActivity">
												<logic:iterate name="studentExtentionForm" 
															   property="subjectActivity" 
															   id="subjectActivity"
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
												<td width="10%" height="25">
													<div align="center">
														<c:out value="${count + 1}" />
													</div>
												</td>
												<td width="15%" align="center">
													<bean:write name="subjectActivity" property="activityName"/>
												</td>	
												<td width="20%" align="center">      
												       <bean:write name="subjectActivity" property="groupName"/> 
												</td>	
			                                    <td width="15%" align="center">
													<bean:write name="subjectActivity" property="displayOrder"/>
												</td>	
												<td width="20%" height="25" align="center">
													<div align="center">
														<img src="images/edit_icon.gif"
															 width="16" 
															 height="18"
        	        			 			 				 style="cursor: pointer;"
		 													 onclick="editEntry('<bean:write name="subjectActivity" property="id"/>',
    	           												                '<bean:write name="subjectActivity" property="activityName"/>')">
													</div>
												</td>
												<td width="20%" height="25">
													<div align="center">
														<img src="images/delete_icon.gif"
															 width="16" 
															 height="16"
                				 				 			 style="cursor: pointer;"
															 onclick="deleteEntry('<bean:write name="subjectActivity" property="id"/>',
    	           												                  '<bean:write name="subjectActivity" property="activityName"/>')">
													</div>
												</td>
											</tr>
												</logic:iterate>
											</logic:notEmpty>
										</table>
									</td>
									<td width="5" height="30" background="images/right.gif"></td>
								</tr>
								<tr>
									<td height="5">
										<img src="images/04.gif" width="5" height="5"></td>
									<td background="images/05.gif"></td>
									<td><img src="images/06.gif"></td>
								</tr>
							</table>
						</td>				
	
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