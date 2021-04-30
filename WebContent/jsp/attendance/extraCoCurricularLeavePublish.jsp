<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="calendar_us.js"></script>
<link rel="stylesheet" href="calendar.css">

<script type="text/javascript" language="javascript">
function getClassesByAcademicYear(year) {
	getClassesByYearForMuliSelect("classMap", year, "classListId", updateClasses);
}
function updateClasses(req) {
	updateOptionsFromMapForMultiSelect(req, "classListId");
}

function deleteDetails(id){
	deleteConfirm = confirm("Are you sure you want to delete this entry?")
	if (deleteConfirm == true) {
		document.location.href = "ExtraCocurricularLeavePublish.do?method=deleteCoCurricularLeaveDetails&id=" + id;
	}
}
function editDetails(id){
	document.location.href = "ExtraCocurricularLeavePublish.do?method=editCoCurricularLeaveDetails&id=" + id;
}

	function resetValues() {
		resetFieldAndErrMsgs();
		//document.location.href = "studentSemesterFeeDetails.do?method=initStudentSemesterFeeDetails";
	}
	function saveData(){
		document.getElementById("method1").value = "saveData";
		document.extraCoCurricularLeavePublishForm.submit();
	}

	function updateData(){
		document.getElementById("method1").value = "updateExtraCoCurricularPublishDetails";
		document.extraCoCurricularLeavePublishForm.submit();
	}
	function resetErrorMsgs() {
		document.getElementById("classListId").value = "";
		document.getElementById("year").value = "";
		document.getElementById("publishStartDate").value =  "";
		document.getElementById("publishEndDate").value = "";
		if(document.getElementById("method").value == "updateExtraCoCurricularPublishDetails"){
			document.getElementById("publishStartDate").value = document.getElementById("publishStartDate1").value;
			document.getElementById("publishEndDate1").value= document.getElementById("publishEndDate1").value;
		}
		resetErrMsgs();
	}


</script>
<html:form action="/ExtraCocurricularLeavePublish" method="post">
	<html:hidden property="formName" value="extraCoCurricularLeavePublishForm" styleId="formName" />
	<html:hidden property="pageType" value="1" styleId="pageType" />
	<html:hidden property="method" styleId="method1" value="saveData"/>
	
	
	
	<table width="100%" border="0">
		<tr>
			<td><span class="heading">Attendance<span class="Bredcrumbs">&gt;&gt;
			Extra Co Curricular Leave Publish &gt;&gt;</span></span></td>
		</tr>

		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Extra Co Curricular
					 Leave Publish</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>

				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div id="errorMessage"><FONT color="red" size="14px"><html:errors />
					</FONT>
					<FONT color="red" size="1px"><bean:write name="extraCoCurricularLeavePublishForm" property="errorMessage"/></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					<div align="right" class="mandatoryfield">*Mandatory fields</div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news">
					</td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">

						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="100%" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
							<tr>
					
									<td width="50%" class="row-odd" >
									<div align="right">Academic Year:</div>
									</td>

									<td width="50%" colspan="2" class="row-even" width="70%">
									<input
										type="hidden" id="tempyear" name="appliedYear"
										value="<bean:write name="extraCoCurricularLeavePublishForm" property="academicYear"/>" />
									<html:select
										property="academicYear" styleClass="body"
										styleId="academicYear"  onchange="getClassesByAcademicYear(this.value)">
										<cms:renderAcademicYear></cms:renderAcademicYear>

									</html:select></td>
								</tr>
								
								<tr>
									<td height="25" class="row-odd" width="50%">
									<div align="right">
									<DIV align="right"><span class="Mandatory">*</span>Class
									 :</DIV>
									</div>
									</td>
									<td height="25" class="row-even" width="500%">
									<html:select property="classListId"  name="extraCoCurricularLeavePublishForm"  styleId="classListId"  multiple="multiple" style="width:300px;height:90px ">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<logic:notEmpty name="extraCoCurricularLeavePublishForm" property="classMap">
											<html:optionsCollection property="classMap" name="extraCoCurricularLeavePublishForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
								</tr>
								
								<tr>

									<td class="row-odd" width="50%">
									<div align="right"><span class="mandatoryfield">*</span>publish for :</div>
									</td>
									<td class="row-even" width="50%"><html:select property="publishFor"
										styleId="publishFor" >
										<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<html:option value="Apply for extra co curricular leave">Apply for extra co curricular leave</html:option>
									</html:select></td>
								</tr>
								
						</table>
						<td width="5" background="images/right.gif"></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>publish start date:
									</div>
									</td>
									<td width="25%" height="25" class="row-even">
									<table width="25" border="0" cellspacing="0" cellpadding="0">
										<tr>
										<td class="row-even" width="25%"><input type="hidden" id="publishStartDate1" name="publishStartDate1" value='<bean:write name="extraCoCurricularLeavePublishForm" property="publishStartDate"/>'/>
											<td width="15%"><html:text
												name="extraCoCurricularLeavePublishForm"
												property="publishStartDate" styleId="publishStartDate"
												maxlength="10" styleClass="TextBox" size="10" /></td>
											<td width="15%"><script language="JavaScript">
	new tcal( {
		// form name
		'formname' :'extraCoCurricularLeavePublishForm',
		// input name
		'controlname' :'publishStartDate'
	});
</script></td>
										</tr>
									</table>
									</td>
									<td width="28%" class="row-odd">
									<div align="right">
									<div align="right"><span class="Mandatory">*</span>publish end date:</div>
									</div>
									</td>
									<td width="26%" class="row-even">
									<table width="25" border="0" cellspacing="0" cellpadding="0">
										<tr>
										<td class="row-even" width="25%"><input type="hidden" id="publishEndDate1" name="publishEndDate1" value='<bean:write name="extraCoCurricularLeavePublishForm" property="publishEndDate"/>'/>
											<td width="15%"><html:text
												name="extraCoCurricularLeavePublishForm" property="publishEndDate"
												styleId="publishEndDate" maxlength="10"
												styleClass="TextBox" size="10" /></td>
											<td width="15%"><script language="JavaScript">
	new tcal( {
		// form name
		'formname' :'extraCoCurricularLeavePublishForm',
		// input name
		'controlname' :'publishEndDate'
	});
</script></td>
										</tr>
									</table>
									</td>
									
								</tr>


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
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div align="right" class="mandatoryfield"></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					
					
						<tr>
						<td width="45%" height="35"><div align="right">
				<c:choose>
					<c:when test="${openConnection!=null && openConnection == 'edit'}">
						<html:button property="" styleClass="formbutton" value="Update"  onclick="updateData()"></html:button>
					</c:when>
					<c:otherwise>
						<html:button property="" styleClass="formbutton" value="Submit"  onclick="saveData()"></html:button>
					</c:otherwise>
				</c:choose></div>
				</td>
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
					<logic:notEmpty name="extraCoCurricularLeavePublishForm" property="toList">
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
                    
                    <td width="5" height="55%" class="row-odd" align="center">StartDate</td>
                    <td width="5" height="10%" class="row-odd" align="center">Enddate</td>
                    <td width="5" height="25" class="row-odd" align="center" ><bean:message key="knowledgepro.edit"/></td>
                    <td width="5" class="row-odd"><div align="center"><bean:message key="knowledgepro.delete"/></div></td>
                 </tr>
                   <c:set var="temp" value="0"/>
                <logic:iterate id="CME" name="extraCoCurricularLeavePublishForm" property="toList" indexId="count">
                <c:choose>
                   <c:when test="${temp == 0}">
                   	<tr>
              		<td  height="25" class="row-even" ><div align="center"><c:out value="${count + 1}"/></div></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="year"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="CME" property="className"/></td>
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
	<script>
	//var year = document.getElementById("tempyear").value;
	//if(year.length != 0) {
	 	//document.getElementById("year").value=year;
	//}
</script>

</html:form>