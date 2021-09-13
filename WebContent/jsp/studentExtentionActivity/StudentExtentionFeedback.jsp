<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>

<script type="text/javaScript">
	function getClasses(year) {
		getClassesByYearForMuliSelect("classMap", year, "classesId", updateClasses);
	}
	function updateClasses(req) {
		updateOptionsFromMapForMultiSelect(req, "classesId");
	}
	function getClassAndSession(year){
		document.location.href = "StudentExtentionFeedback.do?method=getClassAndSession&academicYear=" + year;	
	}
	function getDetailsBySessionId(sessionId){
		var year=document.getElementById("year").value;
		document.location.href = "StudentExtentionFeedback.do?method=getClassesAndSession&academicYear=" + year+"&sessionId="+sessionId;	
	}
	function getMultipleSubject(classId) {
		var classes =  document.getElementById("classesId");
		  var selectedArray = new Array();	  
		  var i;
		  var count = 0;
		  for (i=0; i<classes.options.length; i++) {
		    if (classes.options[i].selected) {
		      selectedArray[count] = classes.options[i].value;
		      count++;
		    }
		  }
		  getSpecializationByClass("specializationMap",selectedArray,"specializationId",updateSpecialization);
	}
	function editDetails(id){
		document.location.href = "StudentExtentionFeedback.do?method=editFeedbackDetails&id=" + id;
	}
	function deleteDetails(id){
		deleteConfirm = confirm("Are you sure you want to delete this entry?")
		if (deleteConfirm == true) {
			document.location.href = "StudentExtentionFeedback.do?method=deleteFeedbackDetails&id=" + id;
		}
	}

	function updateSpecialization(req) {
		updateOptionsFromMap(req,"specializationId","- Select -");
	}

	function resetErrorMsgs() {
		document.getElementById("classId").value = "";
		document.getElementById("year").value = "";
		document.getElementById("group").value = "";
		document.getElementById("startDate").value =  "";
		document.getElementById("endDate").value = "";
		if(document.getElementById("method").value == "updateFeedbackDetails"){
			document.getElementById("startDate").value = document.getElementById("startDate1").value;
			document.getElementById("endDate").value= document.getElementById("endDate1").value;
		}
		resetErrMsgs();
	}
	function cancel() {
		document.location.href = "LoginAction.do?method=loginAction";
	}
</script>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<html:form action="/StudentExtentionFeedback">

<html:hidden property="formName" value="studentExtentionFeedbackForm"/>
<html:hidden property="pageType" value="1"/>
<c:choose>
		<c:when test=""></c:when>
	</c:choose>
<c:choose>
		<c:when test="${openConnection!=null && openConnection == 'edit'}">
			<html:hidden property="method" styleId="method" value="updateFeedbackDetails" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method" value="submitFeedbackDetails" />
		</c:otherwise>
	</c:choose>

  <table width="100%" border="0">
		   <tr>
			 <td>
				<span class="Bredcrumbs">
					<bean:message key="knowledgepro.studentExtentionActivity"/>
					&gt;&gt;
					<bean:message key="knowlegepro.studentFeedbackDetails"/>
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
								<bean:message key="knowlegepro.studentFeedbackDetails"/>
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
							     <% boolean disable=false;%>
					               <logic:equal value="true" name="studentExtentionFeedbackForm" property="flag">
							       <% disable=true;%>
					             </logic:equal>
							    <tr>
							    <td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.admin.year" />:</div></td>
						        <td align="left" height="25" class="row-even"><input type="hidden" id="tempyear" name="appliedYear" value="<bean:write name="studentExtentionFeedbackForm" property="academicYear"/>" />
									  
									  <html:select property="academicYear"  styleId="year" onchange="getClassAndSession(this.value)" styleClass="combo" disabled='<%=disable%>'>
									  <html:option value=" "><bean:message key="knowledgepro.admin.select" /></html:option>
									  <cms:renderAcademicYear></cms:renderAcademicYear>
			                          </html:select></td>
				                 <td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.attendance.activityattendence.class" />:</div></td>       
								              <td align="left" width="30%" class="row-even"><html:select property="classesId" size="10" style="width:250px" multiple="multiple" styleId="classId" styleClass="body" disabled='<%=disable%>' onchange="getMultipleSubject(this.value)">
											<html:optionsCollection name="classMap" label="value" value="key" styleClass="comboBig" />
				                            </html:select></td>     
							    </tr>
							    <tr>
						             <td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.studentExtentionActivity.text" />:</div></td>
						              <td height="25" class="row-odd" colspan="100">
													<html:select property="studentGroupId" name="studentExtentionFeedbackForm" styleId="group">
														<html:option value=""><bean:message key="knowledgepro.select"/></html:option>
										   				<logic:notEmpty property="subGrouplist" name="studentExtentionFeedbackForm">
										   					<html:optionsCollection property="subGrouplist" label="groupName" value="id"/>
										   				</logic:notEmpty>
													</html:select>
									    </td>
                               </tr>
								<tr>
								<td height="25" width="20%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.employee.holidays.startDate" />:</div>
								</td>
									<td class="row-even" width="25%"><input type="hidden" id="startDate1" name="startDate1" value='<bean:write name="studentExtentionFeedbackForm" property="startDate"/>'/>
									<html:text name="studentExtentionFeedbackForm" property="startDate" styleId="startDate" size="10" maxlength="16"/>
									<script
									language="JavaScript">
									new tcal( {
										'formname' :'studentExtentionFeedbackForm',
										'controlname' :'startDate'
									});
									</script>
									</td>
									<td height="25" class="row-odd" width="20%">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.employee.holidays.endDate" />:</div>
									</td>
									<td height="25" class="row-even" width="25%">
									<input type="hidden" id="endDate1" name="endDate1" value='<bean:write name="studentExtentionFeedbackForm" property="endDate"/>'/>
									<html:text property="endDate" styleId="endDate" size="10" maxlength="16"  ></html:text>
									<script language="JavaScript">
										new tcal( {
											'formname' :'studentExtentionFeedbackForm',
											'controlname' :'endDate'
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
             <td height="122" valign="top" background="images/Tright_03_03.gif"></td>
            <td valign="top" class="news"><table width="100%" cellspacing="1" cellpadding="2">
           <tr>
			<td>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
	            <td width="45%" height="35"><div align="right">
				<c:choose>
					<c:when test="${openConnection!=null && openConnection == 'edit'}">
						<html:submit property="" styleClass="formbutton" value="Update" ></html:submit>
					</c:when>
					<c:otherwise>
						<html:submit property="" styleClass="formbutton" value="Submit"></html:submit>
					</c:otherwise>
				</c:choose></div>
				</td>
				<td width="2%"></td>
					<td width="53%"><c:choose>
						<c:when test="${openConnection!=null && openConnection == 'edit'}">
							<html:button property="" value="Reset" styleClass="formbutton" onclick="resetErrorMsgs()"></html:button>
						</c:when>
						<c:otherwise>
							<html:button property="" styleClass="formbutton" value="Reset"
								onclick="resetErrorMsgs()"></html:button>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
				</table>
			</td>
	          </tr>
          <logic:notEmpty name="studentExtentionFeedbackForm" property="feedbackConnectionTo">
          <tr>
            <td height="45" colspan="4" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td ><img src="images/01.gif" width="5" height="5" /></td>
                <td width="914" background="images/02.gif"></td>
                <td><img src="images/03.gif" width="5" height="5" /></td>
              </tr>
              <tr>
                <td width="5"  background="images/left.gif"></td>
                <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                  <tr >
                    <td width="5"  height="5%" class="row-odd" ><div align="center"><bean:message key="knowledgepro.slno"/></div></td>
                     <td width="15" height="30%" class="row-odd" align="center" >Academic Year</td>
                    <td width="20" height="30%" class="row-odd" align="center" >Class</td>
                    <td width="20" height="30%" class="row-odd" align="center" >GroupName</td>
                    <td width="5" height="55%" class="row-odd" align="center">StartDate</td>
                    <td width="5" height="10%" class="row-odd" align="center">Enddate</td>
                    <td width="5" height="25" class="row-odd" align="center" ><bean:message key="knowledgepro.edit"/></td>
                    <td width="5" class="row-odd"><div align="center"><bean:message key="knowledgepro.delete"/></div></td>
                 </tr>
                   <c:set var="temp" value="0"/>
                <logic:iterate id="CME" name="studentExtentionFeedbackForm" property="feedbackConnectionTo" indexId="count">
                <c:choose>
                   <c:when test="${temp == 0}">
                   	<tr>
              		<td  height="25" class="row-even" ><div align="center"><c:out value="${count + 1}"/></div></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="year"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="className"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="groupName"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="startDate"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="endDate"/></td>
                   		<td  height="25" class="row-even" align="center"> <div align="center"><img src="images/edit_icon.gif"
						 height="18" style="cursor:pointer" onclick="editDetails('<bean:write name="CME" property="id"/>')"> </div> </td>
                   		<td  height="25" class="row-even" ><div align="center">
                   			<img src="images/delete_icon.gif" width="16" height="16" style="cursor:pointer" onclick="deleteDetails('<bean:write name="CME" property="id"/>')"></div></td>
                   	</tr>
                    <c:set var="temp" value="1"/>
                   	</c:when>
                    <c:otherwise>
		            <tr>
               			<td  height="25" class="row-white"><div align="center"><c:out value="${count + 1}"/></div></td>
               			<td  height="25" class="row-white" align="center"><bean:write name="CME" property="year"/></td>
               			<td  height="25" class="row-white" align="center"><bean:write name="CME" property="className"/></td>
               			<td  height="25" class="row-even" align="center"><bean:write name="CME" property="groupName"/></td>
               			<td  height="25" class="row-white" align="center"><bean:write name="CME" property="startDate"/></td>
               			<td  height="25" class="row-white" align="center"><bean:write name="CME" property="endDate"/></td>
               			<td  height="25" class="row-white" align="center"> <div align="center"><img src="images/edit_icon.gif"
						 height="18" style="cursor:pointer" onclick="editDetails('<bean:write name="CME" property="id"/>')"> </div> </td>
                   		<td  height="25" class="row-white" ><div align="center">
                   			<img src="images/delete_icon.gif" width="16" height="16" style="cursor:pointer" onclick="deleteDetails('<bean:write name="CME" property="id"/>')"></div></td>
               		</tr>
                    <c:set var="temp" value="0"/>
				  	</c:otherwise>
                  </c:choose>
                </logic:iterate>
                
                </table></td>
                <td width="5" height="30"  background="images/right.gif"></td>
              </tr>
              <tr>
                <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
                <td background="images/05.gif"></td>
                <td><img src="images/06.gif" /></td>
              </tr>
            </table></td>
          </tr>
          </logic:notEmpty>
          
          
          </table></td>
        <td width="10" valign="top" background="images/Tright_3_3.gif" class="news"></td>
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
<script type="text/javascript">
	var yearId = document.getElementById("tempyear").value;
	if (yearId != null && yearId.length != 0) {
		document.getElementById("year").value = yearId;
	}
	var sessionId = document.getElementById("tempSession").value;
	if (sessionId != null && sessionId.length != 0) {
		document.getElementById("sessionId").value = sessionId;
	}
</script>