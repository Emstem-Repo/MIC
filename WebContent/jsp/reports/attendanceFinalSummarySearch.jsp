<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>

<script language="JavaScript" src="js/calendar_us.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
<script type="text/javascript">

function getActivity(attendenceTypeId) {
	  var selectedArray = new Array();	  
	  var i;
	  var count = 0;
	  for (i=0; i<attendenceTypeId.options.length; i++) {
	    if (attendenceTypeId.options[i].selected) {
	      selectedArray[count] = attendenceTypeId.options[i].value;
	      count++;
	    }
	  }
		
	getActivityByType("activityMap",selectedArray,"activity",updateActivity);
}

function updateActivity(req) {
	updateOptionsFromMap(req,"activity","- Select -");
}

function resetAttReport()	{
	
	document.getElementById("classesName").value = ""; 	
	document.getElementById("attendanceTypeId").selectedIndex = -1;
	resetOption("activity");	
	document.getElementById("startDateid").value = "";
	document.getElementById("endDateid").value = "";	
	document.getElementById("withLeave").checked = "";
	document.getElementById("withoutLeave").checked = "";
	resetErrMsgs();
}

function getNames(){
	document.getElementById("method").value = "getAttendanceFinalSummary";
	//document.getElementById("courseNameId").value = document.getElementById("course").options[document.getElementById("course").selectedIndex].text;	
}
function getClasses(year) {
	getClassesByYear("classMap", year, "classesName", updateClasses);
}
function updateClasses(req) {
	var responseObj = req.responseXML.documentElement;
	var destination = document.getElementById("classesName");
	for (x1=destination.options.length-1; x1>=0; x1--) {
		destination.options[x1]=null;
	}
	var items1 = responseObj.getElementsByTagName("option");
	for (var j = 0 ; j < items1.length ; j++) {
        label = items1[j].getElementsByTagName("optionlabel")[0].firstChild.nodeValue;
	     value = items1[j].getElementsByTagName("optionvalue")[0].firstChild.nodeValue;
	     destination.options[j] = new Option(label,value);
	 }
}
</script>

<body>
<html:form action="/attendanceFinalSummary" method="post">
<html:hidden property="method" styleId="method" value="getAttendanceFinalSummary"/>
<html:hidden property="pageType" value="1"/>
<html:hidden property="courseName" styleId="courseNameId" value=""/>
	<html:hidden property="formName" value="attendanceFinalSummaryForm" />
<table width="98%" border="0" cellpadding="2" cellspacing="1">
  <tr>
    <td class="heading"><bean:message key="knowledgepro.reports"/><span class="Bredcrumbs">&gt;&gt;
    <span class="Bredcrumbs"><bean:message key="knowledgepro.attendance.finalsummaryreport" />&gt;&gt;</span></span></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="5"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="body" ><strong class="boxheader"> <bean:message key="knowledgepro.attendance.finalsummaryreport" /></strong></td>
        <td width="10" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td height="20" class="news"><div align="right" class="mandatoryfield"></div></td>
        <td valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td height="45" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	          <tr bgcolor="#FFFFFF">
				<div align="right"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields" /></span></div>	
	            <td colspan="6" class="body" align="left">
	             <div id="errorMessage">
	            <FONT color="red"><html:errors/></FONT>
	             <FONT color="green">
						<html:messages id="msg" property="messages" message="true">
								<c:out value="${msg}" escapeXml="false"></c:out><br>
						</html:messages>
	            </FONT>
	            </div>
	            </td>
	          </tr>

            <tr>
              <td ><img src="images/01.gif" width="5" height="5" /></td>
              <td width="914" background="images/02.gif"></td>
              <td><img src="images/03.gif" width="5" height="5" /></td>
            </tr>
          <tr>
            <td width="5"  background="images/left.gif"></td>
            <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
              <tr >
                <td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.fee.academicyear.col" /></div></td>
                <td height="25" colspan="1" class="row-even" >
				<input type="hidden" id="tempyear" name="tempyear" value="<bean:write name="attendanceFinalSummaryForm" property="academicYear"/>"/>
                 <html:select property="academicYear" styleId="academicYear" styleClass="combo" onchange="getClasses(this.value)">
  	   				 <html:option value="">- Select -</html:option>
  	   						<cms:renderAcademicYear></cms:renderAcademicYear>
   			   </html:select>
                </td>
              <td width="25%" height="25" class="row-odd">
					<div align="right"><span class="Mandatory">*</span>
						<bean:message key="knowledgepro.attendanceentry.class" />
					</div>
				</td>
				<td width="25%" height="25" class="row-even">
				 	<html:select name="attendanceFinalSummaryForm" styleId="classesName" property="classesName" size="5" style="width: 200px;" multiple="multiple">
						<html:optionsCollection name="attendanceFinalSummaryForm" property="classMap" label="value" value="key" />
					</html:select> 
                </td>

              </tr>
              <tr >
                <td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.attn.activity.att.type" /></div></td>
				<td height="25" class="row-even" >
					<html:select property="attendanceType" styleId="attendanceTypeId" onchange="getActivity(this)" style="width: 120px;" multiple="multiple" size="2">						
						<html:optionsCollection name="attendanceTypeList" label="attendanceTypeName" value="id" />
					</html:select>
				</td>
				<td height="25" class="row-odd">&nbsp;</td>
				<td height="25" class="row-even">&nbsp;</td>
                </tr>
			<tr>
                <td height="25" class="row-odd" ><div align="right"><bean:message key="knowledgepro.attendanceentry.activitytype" /></div></td>
                <td width="26%" height="25" class="row-even" >
			      <html:select property="activityId" styleId="activity" styleClass="combo">
          		   		<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
        			    	<c:if test="${attendanceFinalSummaryForm.attendanceType != null && attendanceFinalSummaryForm.attendanceType != ''}">
            					<c:set var="activityMap" value="${baseActionForm.collectionMap['activityMap']}"/>
            		    	 	<c:if test="${activityMap != null}">
            		    	 		<html:optionsCollection name="activityMap" label="value" value="key"/>
            		    	 	</c:if>	 
            		   		</c:if>
	           		</html:select>	   
				</td>
				<td height="25" class="row-odd">
						<div align="right"><bean:message key="knowledgepro.attendanceentry.leavestatus" /></div>
				</td>
				<td width="26%" height="25" class="row-even" ><html:radio styleId="withLeave"
										property="leaveType" value="false"><bean:message key="knowledgepro.attendanceentry.leavewithout" /></html:radio> <html:radio
										property="leaveType" value="true" styleId="withoutLeave"><bean:message key="knowledgepro.attendanceentry.leavewith" /></html:radio>
				</td>

			</tr>
			<tr>
               <td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.feepays.startdate" /></div></td>

									<td width="26%" height="25" class="row-even"><html:text
										property="startDate" styleId="startDateid" size="16"
										maxlength="16" /> <script language="JavaScript">
	new tcal( {
		// form name
		'formname' :'attendanceFinalSummaryForm',
		// input name
		'controlname' :'startDate'
	});
</script></td>


									<td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.feepays.enddate" />:</div></td>
              
			     <td width="26%" height="25" class="row-even" >
					<html:text  property="endDate" styleId="endDateid" size="16" maxlength="16"/>
							<script
							language="JavaScript">
							new tcal( {
								// form name
								'formname' :'attendanceFinalSummaryForm',
								// input name
								'controlname' :'endDate'
							});
						</script>

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
        <td height="36" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news">
		
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="right"><html:submit styleClass="formbutton" onclick="getNames()">
								<bean:message key="knowledgepro.admin.search" />
							</html:submit></div>
							</td>
							<td width="2%"></td>
							<td width="53"><html:button property="" styleClass="formbutton" value="Reset" onclick="resetAttReport()"></html:button></td>
						</tr>
					</table>
		</td>
        <td valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td height="26" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news">&nbsp;</td>
        <td valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="931" background="images/TcenterD.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>