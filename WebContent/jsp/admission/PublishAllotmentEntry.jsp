<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<head>
<script type="text/javascript">
	function editEntry(id,fromDate,endDate,appliedYear,courseId,allotmentNo,chanceNo){
		document.getElementById("fromDate").value=fromDate;
		document.getElementById("endDate").value=endDate;
		document.getElementById("courseId").value=courseId;
		document.getElementById("allotmentNo").value=allotmentNo;
		document.getElementById("chanceNo").value=chanceNo;
		document.getElementById("id").value=id;
		document.getElementById("year").disabled=true;
		document.getElementById("courseId").disabled=true;
		document.getElementById("allotmentNo").disabled=true;
		document.getElementById("chanceNo").disabled=true;
		document.getElementById("method").value = "updateAllotmentDetails";
		document.getElementById("submit").value="Update";
	}
	function addDetails(id){
		document.getElementById("method").value="addAllotmentDetails";
	}
	function resetErrorMsgs() {
		resetErrMsgs();
		document.getElementById("fromDate").value=null;
		document.getElementById("endDate").value=null;
		document.getElementById("courseId").value=null;
		document.getElementById("allotmentNo").value="";
		document.getElementById("chanceNo").disabled="";
		document.getElementById("year").disabled=false;
		document.getElementById("courseId").disabled=false;
		document.getElementById("allotmentNo").disabled=false;
		document.getElementById("chanceNo").disabled=false;
		document.getElementById("method").value = "addAllotmentDetails";
		document.getElementById("submit").value="Submit";
	}
</script>
</head>
<html:form action="/PublishForAllotment" method="POST">
<html:hidden property="formName" value="publishForAllotmentForm" />
<html:hidden property="pageType" value="1" />
<html:hidden property="id" styleId="id"/>
<html:hidden property="method" styleId="method" value="addAllotmentDetails" />

<table width="99%" border="0">
  
  <tr>
    <td><span class="Bredcrumbs">Admission <span class="Bredcrumbs">&gt;&gt; Allotment Details</span> </span></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="heading_white" >Publish Allotment Coursewise  </td>
        <td width="10" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><div align="right"><span class='MandatoryMark'><bean:message
					key="knowledgepro.mandatoryfields" /></span></div>
			<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
				<FONT color="green"><html:messages id="msg" property="messages" message="true">
					<c:out value="${msg}" escapeXml="false"></c:out><br>
					</html:messages> </FONT></div></td>
        <td valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td height="44" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td ><img src="images/01.gif" width="5" height="5" /></td>
            <td width="914" background="images/02.gif"></td>
            <td><img src="images/03.gif" width="5" height="5" /></td>
          </tr>
          <tr>
            <td width="5"  background="images/left.gif"></td>
            <td valign="top">
            <table width="100%" cellspacing="1" cellpadding="2">
	            <tr>
	            	<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;From Date:</div></td>
	              	<td align="left" height="25" class="row-even">
	              		<html:text property="fromDate" styleId="fromDate" size="11" maxlength="11"></html:text>
										<script language="JavaScript">
											new tcal( {
												// form name
												'formname' :'publishForAllotmentForm',
												// input name
												'controlname' :'fromDate'
											});
										</script>
	              	</td>
	              	<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;End Date:</div></td>
	              	<td align="left" height="25" class="row-even">
	              		<html:text property="endDate" styleId="endDate" size="11" maxlength="11"></html:text>
										<script language="JavaScript">
											new tcal( {
												// form name
												'formname' :'publishForAllotmentForm',
												// input name
												'controlname' :'endDate'
											});
										</script>
	              	</td>
	            </tr>
	            
            	 <tr>
            	 	<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>
										<bean:message key="knowledgepro.fee.appliedyear"/>:</div>
									</td>
									<td class="row-even">
									<input type="hidden" id="tempyear" name="tempyear" value="<bean:write name="publishForAllotmentForm" property="year"/>"/>
									<html:select property="year" styleId="year" styleClass="combo">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:renderFutureYear normalYear="true"></cms:renderFutureYear>
									</html:select></td>
	            	<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>&nbsp;Courses:</div></td>
	              	<td align="left" height="25" class="row-even">
	              		<html:select property="courseIds"  styleId="courseId" styleClass="body" multiple="multiple" size="10" style="width:350px" >
						  <html:optionsCollection name="publishForAllotmentForm" property="courseMap" label="value" value="key" />
					  </html:select>
	              	</td>
	              	
	            </tr>
	            <tr>
            	 	<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>
										Allotment No:</div>
									</td>
									<td class="row-even">
									<html:select property="allotmentNo" styleId="allotmentNo" styleClass="combo">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:option value="1">1</html:option>
										<html:option value="2">2</html:option>
										<html:option value="3">3</html:option>
										<html:option value="4">4</html:option>
									</html:select></td>
	            	<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>
	            					Chance No:</div></td>
	              	<td align="left" height="25" class="row-even">
	              	<html:select property="chanceNo" styleId="chanceNo" styleClass="combo">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:option value="1">1</html:option>
										<html:option value="2">2</html:option>
										<html:option value="3">3</html:option>
										<html:option value="4">4</html:option>
										<html:option value="5">5</html:option>
										<html:option value="6">6</html:option>
					</html:select>
	              	</td>
	              	
	            </tr>
             </table></td>
             <td width="5" height="30"  background="images/right.gif"></td>
             </tr>
             <tr>
            <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
            <td background="images/05.gif"></td>
            <td><img src="images/06.gif" /></td>
            </tr>
            </table></td>
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
	            <html:submit property="" styleClass="formbutton" value="Submit" styleId="submit"></html:submit>
				</div>
				</td>
				<td width="2%"></td>
					<td width="53%">
					<html:button property="" styleClass="formbutton" value="Reset"
								onclick="resetErrorMsgs()"></html:button>
				</td>
			</tr>
				</table>
			</td>
	          </tr>
          <logic:notEmpty name="publishForAllotmentForm" property="publishForAllotmentTOs">
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
                     <td width="15" height="30%" class="row-odd" align="center" >FromDate</td>
                    <td width="20" height="30%" class="row-odd" align="center" >EndDate</td>
                    <td width="15" height="30%" class="row-odd" align="center" >Applied Year</td>
                    <td width="20" height="30%" class="row-odd" align="center" >Cousrse Name</td>
                    <td width="20" height="30%" class="row-odd" align="center" >Allotment No</td>
                    <td width="20" height="30%" class="row-odd" align="center" >Chance  No</td>
                    <td width="5" class="row-odd"><div align="center"><bean:message key="knowledgepro.edit"/></div></td>
                 </tr>
                <c:set var="temp" value="0"/>
                <logic:iterate id="to" name="publishForAllotmentForm" property="publishForAllotmentTOs" indexId="count">
                		<c:choose>
							<c:when test="${count%2 == 0}">
									<tr class="row-even">
							</c:when>
							<c:otherwise>
									<tr class="row-white">
							</c:otherwise>
						</c:choose>
                		<td  height="25" class="row-even" ><div align="center"><c:out value="${count + 1}"/></div></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="fromDate"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="toDate"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="appliedYear"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="courseName"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="allotmentNo"/></td>
                   		<td  height="25" class="row-even" align="center"><bean:write name="to" property="chanceNo"/></td>
                   		<td  height="25" class="row-even" ><div align="center">
                   			<img src="images/edit_icon.gif" width="16" height="16" style="cursor:pointer"
                   			onclick="editEntry('<bean:write name="to" property="id"/>',
                   								'<bean:write name="to" property="fromDate"/>',
                   								'<bean:write name="to" property="toDate"/>',
                   								'<bean:write name="to" property="appliedYear"/>',
                   								'<bean:write name="to" property="courseId"/>',
                   								'<bean:write name="to" property="allotmentNo"/>',
                   								'<bean:write name="to" property="chanceNo"/>')"></div></td>
                   
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
        <td height="26" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"></td>
        <td valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="0" background="images/TcenterD.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
    </table></td>
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