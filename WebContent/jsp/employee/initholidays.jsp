<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">

function reActivate(){
	document.location.href = "holidayDetails.do?method=reactivateHolidays";
}
 function cancelAction(){
	 document.location.href = "holidayDetails.do?method=initHolidays";
 }
 function resetHolidays(){
	 document.getElementById("startDate").value="";
	 document.getElementById("endDate").value="";
	 document.getElementById("descript").value="";
	 if(document.getElementById("method").value == "updateHolidays"){
		 document.getElementById("startDate").value=document.getElementById("origStartDate").value;
		 document.getElementById("endDate").value=document.getElementById("origEndDate").value;
		 document.getElementById("descript").value=document.getElementById("origDescription").value;
	 }
	 resetFieldAndErrMsgs();
 }
 function editHolidays(id){
	 document.location.href = "holidayDetails.do?method=editHolidays&id="+id;
 }
 function deleteHolidays(id){
	 document.location.href = "holidayDetails.do?method=deleteHolidays&id="+id;
 }
</script>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
</head>
<body>
<html:form action="/holidayDetails">
<html:hidden property="formName" value="holidayDetailsForm" />
	<html:hidden property="pageType" value="1" />
	<c:choose>
		<c:when test="${holidaysOperation == 'edit'}">
			<html:hidden property="method" styleId="method"
				value="updateHolidays" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method"
				value="addHolidays" />
		</c:otherwise>
	</c:choose>
	<html:hidden property="origStartDate"	styleId="origStartDate" name="holidayDetailsForm"/>
	<html:hidden property="origEndDate" styleId="origEndDate" name="holidayDetailsForm"/>
	<html:hidden property="origDescription"	styleId="origDescription" name="holidayDetailsForm"/>
<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs">Employee 
			<span class="Bredcrumbs">&gt;&gt;Holidays &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/Tright_03_01.gif"></td>
					<td width="100%" background="images/Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader">Holidays</strong></div>
					</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="6" align="left">
							<div align="right"><span class='MandatoryMark'>mandatoryfields</span></div>
							<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
							<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
					<br>
				</html:messages> </FONT></div>
							</td>
						</tr>
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
							
           
            <tr>
            	<td   class="row-odd" ><div align="right"><span class="Mandatory">*</span>&nbsp;Start Date:</div></td>
                <td  height="25" class="row-even">
                	<table width="89" border="0" cellspacing="0" cellpadding="0">
                    	<tr>
                      		<td width="60">
                      			<html:text name="holidayDetailsForm" property="startDate" styleId="startDate" size="12" maxlength="16" styleClass="TextBox"/> 
                      		</td>
                      		    <td width="40">
                      			<script language="JavaScript">
									new tcal ({
										// form name
										'formname': 'holidayDetailsForm',
										// input name
										'controlname': 'startDate'
									});
								</script>
							</td>
                    	</tr>
                	</table>
                </td>
			    <td  class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;End Date:</div></td>
                <td class="row-even">
                	<table width="88" border="0" cellspacing="0" cellpadding="0">
                  		<tr>
                    		<td width="60">
                    		<html:text name="holidayDetailsForm" property="endDate" styleId="endDate" size="12" maxlength="16" styleClass="TextBox"/>
                    		  </td>
                    		<td width="40">
                      			<script language="JavaScript">
									new tcal ({
										// form name
										'formname': 'holidayDetailsForm',
										// input name
										'controlname': 'endDate'
									});
								</script>
							</td>
                  		</tr>
                	</table>
                </td>
			</tr>
            <tr>
                <td class="row-odd" ><div align="right"><span class="Mandatory">*</span>&nbsp;Description:</div></td>
                <td class="row-even" colspan="3">
                	<span class="row-white">
                		<html:text property="description" name="holidayDetailsForm" size="66" styleId="descript"></html:text>
                	</span>
                </td>
                <td>&nbsp;</td>
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
					<div align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
            <td width="49%" height="35" align="right">
            <c:choose>
					<c:when test="${holidaysOperation == 'edit'}">
						<html:submit  value="Update" styleClass="formbutton" ></html:submit>
					</c:when>
					<c:otherwise>
						<html:submit  value="Submit" styleClass="formbutton" ></html:submit></c:otherwise>
				</c:choose></td>
             <td width="49%" height="35" align="left">
            <c:choose>
						<c:when test="${holidaysOperation == 'edit'}">
							<html:cancel value="Reset" styleClass="formbutton"></html:cancel>
						</c:when>
						<c:otherwise>
							<html:button property="" styleClass="formbutton" value="Reset"
								onclick="resetHolidays()"></html:button>
						</c:otherwise>
					</c:choose>
            &nbsp;<input name="Submit" type="reset" class="formbutton" value="Cancel" onclick="cancelAction()" />
            </td>
          </tr>
					</table>
					</div>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
							
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
											<td width="9%" height="25" class="row-odd">
											<div align="center">slno</div>
											</td>
											
											
											<td width="18%" class="row-odd">
											<div align="center">Start Date</div>
											</td>
											<td width="18%" class="row-odd">
											<div align="center">End Date</div>
											</td>
											<td width="25%" class="row-odd">
											<div align="center">Description</div>
											</td>
											<td width="15%" class="row-odd">
											<div align="center">Edit</div>
											</td>
											<td width="15%" class="row-odd">
											<div align="center">Delete</div>
											</td>
										</tr>
										<logic:notEmpty name="holidayDetailsForm" property="holidaysTO">
									<logic:iterate id="holiday" name="holidayDetailsForm"
										property="holidaysTO" indexId="count">
										<tr>
										<td width="9%" class="row-even"><div align="center"><c:out value="${count + 1}" /></div></td>
										<td width="18%" class="row-even"><div align="center"><bean:write name="holiday" property="startDate" /></div> </td>
										<td width="18%" class="row-even"><div align="center"><bean:write name="holiday" property="endDate" /></div> </td>
										<td width="25%" class="row-even"><div align="center"><bean:write name="holiday" property="description" /></div> </td>
										<td width="15%" height="25" class="row-even">
										<div align="center"><img src="images/edit_icon.gif" width="16" height="18" style="cursor: pointer" onclick="editHolidays('<nested:write name="holiday" property="id" />')" /> </div>

										</td>
										<td width="15%" height="25" class="row-even"> 
										<div align="center"><img src="images/delete_icon.gif" width="16" height="16" style="cursor: pointer" onclick="deleteHolidays('<nested:write name="holiday" property="id" />')" /></div> 
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
					<td height="26" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news"></td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>	
				<tr>
					<td><img src="images/Tright_03_05.gif" height="29" width="9"></td>
					<td background="images/TcenterD.gif" width="100%"></td>
					<td><img src="images/Tright_02.gif" height="29" width="9"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</html:form>
</body>
</html>