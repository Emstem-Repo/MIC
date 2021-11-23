<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.to.admission.AdmissionStatusTO"%>
<%@page import="com.kp.cms.forms.admission.AdmissionStatusForm"%><script
	language="JavaScript" src="js/calendar_us.js"></script>

<link rel="stylesheet" href="css/calendar.css">
<link href="css/styles.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/sdmenu.css" />
<link rel="stylesheet" href="jquery/Zebra/css/default/zebra_dialog.css" />

<script language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/AC_RunActiveContent.js"></script>
<script type="text/javascript" src="js/sdmenu/sdmenu.js"></script>
<script type="text/javascript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
<script type="text/javascript" src="js/leftSwitchMenu.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
<script type='text/javascript'
	src="jquery/Zebra/javascript/zebra_dialog.js"></script>
<script src="jquery/js/jquery-1.9.0.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
<link type="text/css"
	href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css"
	rel="stylesheet" />
<script src="jquery/js/jquery-ui-1.10.0.custom.min.js"
	type="text/javascript"></script>

<script language="javaScript" type="text/javascript">
	function redirectControl() {
		document.location.href = "AdmissionStatus.do?method=initAdmissionStatus";
	}
	function downloadAdmitCard(applicationNo, courseId, interviewTypeId) {
		var url = "AdmissionStatus.do?method=downloadInterviewCard&applicationNo="+applicationNo+"&courseId="+courseId+"&interviewTypeId="+interviewTypeId;
		myRef = window.open(url,"EAdmitCard","left=20,top=20,width=600,height=500,toolbar=1,resizable=0,scrollbars=1");
	}
	function downloadAdmissionCard(applicationNo, courseId, interviewTypeId) {
		var appno=document.getElementById('appNo').value; 
		var year=document.getElementById('year').value;
		var url = "AdmissionStatus.do?method=downloadAdmissionCard&applicationNo="+applicationNo+"&courseId="+courseId+"&interviewTypeId="+interviewTypeId;
		myRef = window.open(url,"EAdmissionCard","left=20,top=20,width=600,height=500,toolbar=1,resizable=0,scrollbars=1");
	}	
	function downloadmemo() {
		var appno=document.getElementById('appNo').value; 
		var year="2021";
		var url = "AdmissionStatus.do?method=downloadApplication&applicationNo="+appno +"&appliedYear="+year+"&displaySemister="+true+"&memo="+true+"&chanceMemo="+false;
		myRef = window
		.open(url, "viewDescription",
				"left=200,top=200,width=3000,height=1500,toolbar=1,resizable=0,scrollbars=1");
	}
	function downloadChanceMemo() {
		var chanceCourseId=document.getElementById("courseId").value;
		var appno=document.getElementById('appNo').value; 
		var year="2021";
		var url = "AdmissionStatus.do?method=downloadApplication&applicationNo="+appno +"&appliedYear="+year+"&displaySemister="+true+"&memo="+false+"&chanceMemo="+true+"&chanceCourseId="+chanceCourseId;
		myRef = window
		.open(url, "viewDescription",
				"left=200,top=200,width=3000,height=1500,toolbar=1,resizable=0,scrollbars=1");
	}	
	function downloadAcknowledgement(applnNo, appliedYear) {
		var url = "AdmissionStatus.do?method=downloadAcknowledgement&applicationNo="+applnNo +"&appliedYear="+appliedYear;
		myRef = window
		.open(url, "viewDescription",
				'left=20,top=20,width=800,height=1000,toolbar=1,resizable=0,scrollbars=1');
	}	
	function resetMessages() {
		 document.getElementById("appNo").value="";
	     document.getElementById("datePick").value="";
	     resetErrMsgs();
	     document.getElementById("first").style.display="none";
	     document.getElementById("second").style.display="none";
	     document.getElementById("third").style.display="none";
	     document.getElementById("fourth").style.display="none";
	     document.getElementById("sixth").style.display="none";
	     document.getElementById("fifth").style.display="none";
	     
	}		
	function cancel(){
		document.getElementById("method").value="forwardDashBordPage";
		document.admissionStatusForm.submit();
	}
	function acceptEntry(id,count,type){
		document.getElementById("selectedCourseId").value=id;
		document.getElementById("selectedValue").value="accept";
		document.getElementById("chancOrAllotment").value=type;
		document.getElementById("method").value="getOnlineApplicationStatus";
		document.admissionStatusForm.submit();
	}
	function declineEntry(id,count,type) {
			  $.confirm({
					'message'	: '<b>Are you sure that you want to reject the admission in this quota?<b>',
					'buttons'	: {
						'Ok'	: {
							'class'	: 'blue',
							'action': function(){
								$.confirm.hide();
								document.getElementById("selectedCourseId").value=id;
								document.getElementById("selectedValue").value="decline";
								document.getElementById("chancOrAllotment").value=type;
								document.getElementById("method").value="getOnlineApplicationStatus";
								document.admissionStatusForm.submit();
							}
						},
	  	       'Cancel'	:  {
							'class'	: 'gray',
							'action': function(){
								$.confirm.hide();
							}
						}
					}
				});
			  
		
		
	}
	function uploadDoc() {
		var box=document.getElementById("uploadId").value;
		var cek=document.getElementById("isChecked").value
		if(cek=='false'){
			if(box=='MICADMN2020'){
				document.getElementById("uploadDetail").value="upload";
				document.getElementById("method").value="getOnlineApplicationStatus";
				document.admissionStatusForm.submit();
			}else{
				document.getElementById("displayingErrorMessage").innerHTML="Please Enter Authenticated code"; 
			}
		}else{
			document.getElementById("displayingErrorMessage").innerHTML="Please Click the declare button"; 
		}
	}
	function payment(){
		document.getElementById("method").value="redirectToPayment";
		document.admissionStatusForm.submit();
	}
	function printPage(p) {
		var appno=document.getElementById('appNo').value; 
		var year=document.getElementById('year').value; 
		var url="AdmissionStatus.do?method=printAdmissionFormOnline&applicationNo="+appno+"&appliedYear="+year+"&pageNum="+p;
		window.open(url,'PrintApplication','left=20,top=20,width=800,height=1000,toolbar=1,resizable=0,scrollbars=1');
	}
	function submitAddMorePreferences(method){
		document.getElementById("method").value=method;
		document.admissionStatusForm.submit();
	}
	function submitAddMorePreferences(method){
		document.getElementById("method").value=method;
		document.admissionStatusForm.submit();
	}
	function submitgetstatus(){
		document.getElementById("method").value="getOnlineApplicationStatus";
		document.admissionStatusForm.submit();
	}
	function addCourseId(id){
			document.getElementById("courseId").value=id;
	}
	function removePreferences(){
		document.getElementById("method").value="removePreferences";
		document.admissionStatusForm.submit();
	}
	function submitcourse(){
		document.getElementById("method").value="submitcourse";
		document.admissionStatusForm.submit();
	}
	
	submitcourse
</script>
<style>
.pref {
	padding: 5px;
	width: 300px;
}
a:hover {
  cursor: pointer;
}
</style>
<html:form action="/AdmissionStatus" method="post">
	<html:hidden property="method" styleId="method"
		value="getOnlineApplicationStatus" />
	<html:hidden property="formName" value="admissionStatusForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="serverDownMessage" styleId="serverDownMessage"
		name="admissionStatusForm" />
	<html:hidden property="selectedCourseId" styleId="selectedCourseId" />
	<html:hidden property="selectedValue" styleId="selectedValue" />
	<html:hidden property="uploadDetail" styleId="uploadDetail" />
	<html:hidden property="chancOrAllotment" styleId="chancOrAllotment" />
	<html:hidden property="courseId" styleId="courseId"
		name="admissionStatusForm" />
	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><bean:message
						key="knowledgepro.admission" /> <span class="Bredcrumbs">&gt;&gt;
						<bean:message key="knowledgepro.admission.applicationstatus" />
						&gt;&gt;
				</span></span></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="top">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="9"><img src="images/Tright_03_01.gif" width="9"
										height="29"></td>
									<td background="images/Tcenter.gif" class="body">
										<div align="left">
											<strong class="boxheader"><bean:message
													key="knowledgepro.admission.applicationstatus" /></strong>
										</div>
									</td>
									<td width="10"><img src="images/Tright_1_01.gif" width="9"
										height="29"></td>
								</tr>
								<tr>
									<td height="122" valign="top"
										background="images/Tright_03_03.gif"></td>
									<td valign="top" class="news">
										<table width="100%" cellspacing="1" cellpadding="2">
											<tr bgcolor="#FFFFFF">
												<td height="20" colspan="4">
													<div align="right">
														<span class='MandatoryMark'><bean:message
																key="knowledgepro.mandatoryfields" /></span>
													</div> <FONT size="2px" color="red"><div
															id="displayingErrorMessage"></div></FONT>
													<div id="errorMessage">
														&nbsp;<FONT color="red"><html:errors /></FONT> <FONT
															color="green"> <html:messages id="msg"
																property="messages" message="true">
																<c:out value="${msg}" escapeXml="false"></c:out>
																<br>
															</html:messages>
														</FONT>
													</div>
												</td>
											</tr>
											<%
												String dateofBirth = "datePick";
											%>
											<tr>
												<td width="27%" class="row-odd">
													<div align="right">
														<span class="Mandatory">*</span>&nbsp;
														<bean:message key="knowledgepro.admission.appNo" />
													</div>
												</td>
												<td width="20%" class="row-even"><html:text
														property="applicationNo" styleId="appNo" size="17"
														maxlength="30" styleClass="body"></html:text></td>
												<td class="row-odd">
													<div align="right">
														<span class="Mandatory">*</span> &nbsp;
														<bean:message key="knowledgepro.admission.dob" />
														<bean:message
															key="admissionForm.application.dateformat.label" />
													</div>
												</td>
												<td class="row-odd"><html:text property="dateOfBirth"
														readonly="false" size="10" maxlength="10"
														styleId='<%=dateofBirth%>'></html:text> <script
														language="JavaScript">
													new tcal ({
														// form name
														'formname': 'admissionStatusForm',
														// input name
														'controlname': '<%=dateofBirth%>
															'
																	});
														</script></td>
											</tr>
											<tr>
												<td height="45" colspan="4">
													<table width="100%" border="0" cellspacing="0"
														cellpadding="0">
														<tr>
															<td width="45%" height="35">
																<div align="right">
																	<html:submit onclick="submitgetstatus();"
																		styleClass="formbutton" value="Submit" />
																	&nbsp; &nbsp;
																</div>
															</td>
															<td width="2%"><html:button property=""
																	styleClass="formbutton" value="Reset"
																	onclick="resetMessages()" /></td>
															<td width="53%">&nbsp;&nbsp; <html:button
																	property="" styleClass="formbutton" value="Cancel"
																	onclick="cancel()" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td height="25" colspan="4">
													<table width="100%" height="53" border="0" cellpadding="0"
														cellspacing="1">
														<tr class="row-odd">
															<td height="25" class="bodytext">
																<div align="center">
																	<bean:message
																		key="knowledgepro.admission.applicationNo" />
																</div>
															</td>
															<td height="25" class="bodytext">
																<div align="center">
																	<bean:message key="knowledgepro.admission.dateOfBirth" />
																</div>
															</td>
															<td height="25" class="bodytext">
																<div align="center">
																	<bean:message key="admissionFormForm.emailId" />
																</div>
															</td>
															<td class="bodytext">
																<div align="center">
																	<bean:message key="knowledgepro.admission.status" />
																</div>
															</td>
															<logic:notEmpty property="statusTO"
																name="admissionStatusForm">
																<logic:equal name="admissionStatusForm"
																	property="statusTO.isInterviewSelected"
																	value="interview">
																	<td class="bodytext">
																		<div align="center">
																			<bean:message
																				key="knowledgepro.admission.applicationstatus.downloadicard" />
																		</div>
																	</td>
																</logic:equal>
																<logic:equal name="admissionStatusForm"
																	property="statusTO.isInterviewSelected"
																	value="admission">
																	<td class="bodytext">
																		<div align="center">
																			<bean:message
																				key="knowledgepro.admission.applicationstatus.downloadadmitcard" />
																		</div>
																	</td>
																</logic:equal>
															</logic:notEmpty>
														</tr>
														<logic:notEmpty property="statusTO"
															name="admissionStatusForm">
															<tr class="row-odd" id="first">
																<td align="center" height="25" class="row-even"><bean:write
																		name="admissionStatusForm"
																		property="statusTO.applicationNo" /></td>
																<td width="250" align="center" height="25"
																	class="row-even"><bean:write
																		name="admissionStatusForm"
																		property="statusTO.dateOfBirth" /></td>
																<td align="center" height="25" class="row-even"><bean:write
																		name="admissionStatusForm" property="statusTO.email" />
																</td>
																<logic:equal value="1" name="admissionStatusForm"
																	property="programTypeId">
																	<logic:equal value="false" name="admissionStatusForm"
																		property="specialCourse">
																		<c:choose>
																			<c:when test="${admissionStatusForm.maxallotment==2}">
																				<td align="center" height="25" class="row-even">
																					<font color="red">UG Allotment Published.</font>
																				</td>
																			</c:when>
																			<c:otherwise>
																				<td align="center" height="25" class="row-even">
																					<font color="red">UG Allotment Published.</font>
																				</td>
																			</c:otherwise>
																		</c:choose>
																	</logic:equal>
																</logic:equal>
																<logic:equal value="2" name="admissionStatusForm"
																	property="programTypeId">
																	<c:choose>
																		<c:when test="${admissionStatusForm.maxallotment==1}">
																			<td align="center" height="25" class="row-even">
																				<font color="red">PG Allotment Published.</font>
																			</td>
																		</c:when>
																		<c:otherwise>
																			<td align="center" height="25" class="row-even">
																				<font color="red">PG Allotment Published.</font>
																			</td>
																		</c:otherwise>
																	</c:choose>
																</logic:equal>
																<logic:equal value="true" name="admissionStatusForm"
																	property="specialCourse">
																	<c:choose>
																		<c:when test="${admissionStatusForm.maxallotment==2}">
																			<td align="center" height="25" class="row-even">
																				<font color="red">PG/UG Allotment
																					Published.(Only for New Aided Programmes)</font>
																			</td>
																		</c:when>
																		<c:otherwise>
																			<td align="center" height="25" class="row-even">
																				<font color="red">PG/UG Allotment
																					Published.(Only for New Aided Programmes)</font>
																			</td>
																		</c:otherwise>
																	</c:choose>
																</logic:equal>
																<logic:equal name="admissionStatusForm"
																	property="statusTO.isInterviewSelected"
																	value="interview">
																	<td align="center" class="row-even">
																		<div align="center">
																			<a href="javascript:void(0)" class="menuLink"
																				onclick="downloadAdmitCard('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.courseId" />', '<bean:write name="admissionStatusForm" property="statusTO.interviewProgramCourseId" />')">
																				<bean:message
																					key="knowledgepro.admission.applicationstatus.downloadicard" />
																			</a>
																		</div>
																	</td>
																</logic:equal>
																<logic:equal name="admissionStatusForm"
																	property="statusTO.isInterviewSelected"
																	value="admission">
																	<td align="center" class="row-even">
																		<div align="center">
																			<a href="javascript:void(0)" class="menuLink"
																				onclick="downloadAdmissionCard('<bean:write name="admissionStatusForm" property="statusTO.applicationNo" />', '<bean:write name="admissionStatusForm" property="statusTO.courseId" />', '<bean:write name="admissionStatusForm" property="statusTO.interviewProgramCourseId" />')">
																				<bean:message
																					key="knowledgepro.admission.applicationstatus.downloadadmitcard" />
																			</a>
																		</div>
																	</td>
																</logic:equal>
															</tr>
															<logic:equal value="1" name="admissionStatusForm"
																property="programTypeId">
																<logic:equal value="2" name="admissionStatusForm"
																	property="maxallotment">
																	<tr id="eight">
																		<td height="25" colspan="4">
																			<table width="100%" height="53" border="0"
																				cellpadding="0" cellspacing="1">
																				<tr class="row-even">
																					<td height="25" class="bodytext" width="60">
																						<div align="center">*Accept*</div>
																						<br> <font color="red">This
																							acceptance is not a claim for admission.</font> After
																						verification the Payment window will be opened for
																						those whose admission is confirmed. The remaining
																						candidates will be given a chance number according
																						to their rank from which their admission will be
																						confirmed as and when the vacancy arises.<br>
																					
																						</p>

																					</td>
																					<td height="25" class="bodytext" width="80">
																						<div align="center">*Decline*</div>
																						<br> If you decline or do not respond to the
																						allotment within the stipulated time you will not
																						be considered for further allotments in that
																						particular subject under that particular quota.
																					</td>
																				</tr>

<tr align = "center"><td colspan="2" align = "center">
<font color="blue" size="3px">
If a candidate receives more than one allotment
<br><br>1. He/She should opt for one allotment only, of his/her choice.
<br><br>2. After verification the payment window will be opened for those whose admission is confirmed (on 08-10-2021).
<br><br>3. The remaining candidates will be given a chance number according to the rank from which their admission will be confirmed as and when the vacancy arises.
</font><br>
<!-- <p align ="center"><font color="red"> The last date for Accepting/ Declining the allotment is 15th September 2021.</font></p> -->
</td></tr>
																			</table>
																		</td>
																	</tr>
																</logic:equal>
															</logic:equal>
															<logic:equal value="2" name="admissionStatusForm"
																property="programTypeId">
																<logic:notEmpty name="admissionStatusForm"
																	property="statusTOs">
																	<tr id="eight">
																		<td height="25" colspan="4">
																			<table width="100%" height="53" border="0"
																				cellpadding="0" cellspacing="1">
																				<tr class="row-even">
																					<td height="25" class="bodytext" width="60">
																						<div align="center">*Accept*</div>
																						<br> <font color="red">This
																							acceptance is not a claim for admission.</font> After
																						verification the Payment window will be opened for
																						those whose admission is confirmed. The remaining
																						candidates will be given a chance number according
																						to their rank from which their admission will be
																						confirmed as and when the vacancy arises.<br>
																					<br> If a candidate receives more than one
																						allotment he/she should opt for one allotment
																						only, of his choice.
																						</p>

																					</td>
																					<td height="25" class="bodytext" width="80">
																						<div align="center">*Decline*</div>
																						<br> If you decline or do not respond to the
																						allotment within the stipulated time you will not
																						be considered for further allotments in that
																						particular subject under that particular quota.
																					</td>
																				</tr>

<tr align = "center"><td colspan="2" align = "center">
<font color="blue" size="3px">
If a candidate receives more than one allotment
<br><br>1. He/She should opt for one allotment only, of his/her choice.
<br><br>2. After verification the payment window will be opened for those whose admission is confirmed (on 08-10-2021).
<br><br>3. The remaining candidates will be given a chance number according to the rank from which their admission will be confirmed as and when the vacancy arises.
</font><br>

																			</table>
																		</td>
																	</tr>
																</logic:notEmpty>
															</logic:equal>
															<logic:equal value="true" name="admissionStatusForm"
																property="specialCourse">
																<logic:notEmpty name="admissionStatusForm"
																	property="statusTOs">
																	<tr id="eight">
																		<td height="25" colspan="4">
																			<table width="100%" height="53" border="0"
																				cellpadding="0" cellspacing="1">
																				<tr class="row-even">
																					<td height="25" class="bodytext" width="60">
																						<div align="center">*Accept*</div>
																						<br> <font color="red">This
																							acceptance is not a claim for admission.</font> After
																						verification the Payment window will be opened for
																						those whose admission is confirmed. The remaining
																						candidates will be given a chance number according
																						to their rank from which their admission will be
																						confirmed as and when the vacancy arises.<br>
																					<br> If a candidate receives more than one
																						allotment he/she should opt for one allotment
																						only, of his choice.
																						</p>

																					</td>
																					<td height="25" class="bodytext" width="80">
																						<div align="center">*Decline*</div>
																						<br> If you decline or do not respond to the
																						allotment within the stipulated time you will not
																						be considered for further allotments in that
																						particular subject under that particular quota.
																					</td>
																				</tr>

																			</table>
																		
																		</td>
																	</tr>

																</logic:notEmpty>   
															</logic:equal> 

														</logic:notEmpty>


													</table>

          												</td>  
											</tr>
 

											<logic:notEmpty name="admissionStatusForm"
												property="statusTOs">
												<logic:iterate id="to" name="admissionStatusForm"
													property="statusTOs" indexId="count">
													<tr id="second">
														<td height="25" colspan="4">
															<table width="100%" height="53" border="0"
																cellpadding="0" cellspacing="1">
																<tr class="row-even">
																	<td height="25" class="bodytext" width="60">
																		<div align="center" style="font-size: 14px">
																			<b> <bean:write name="to" property="casteName" />&nbsp;
																				<bean:write name="to" property="courseName" /></b>
																		</div>
																	</td>
																	<td height="25" class="bodytext" width="80"><logic:equal
																			value="false" name="to" property="isAccept">
																			<logic:equal value="false" name="to"
																				property="isDecline">
																							<!--	<img src="images/accept.png" width="25" height="25" style="cursor:pointer" id="accept"
                   											onclick="acceptEntry('<bean:write name="to" property="courseId"/>',<%=count + 1%>,'<bean:write name="to" property="chanceAllotment"/>')"><b>Accept</b>
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<img src="images/cross.png" width="25" height="25" style="cursor:pointer" id="decline"
                   											onclick="declineEntry('<bean:write name="to" property="courseId"/>',<%=count + 1%>,'<bean:write name="to" property="chanceAllotment"/>')">	<b>Decline</b>   -->
																				<font color="red"><b> The last date for
																						 accepting the allotment was 07th October 2021.  </b></font>
																			</logic:equal>
																		</logic:equal> <logic:equal value="true" name="to"
																			property="isAccept">
																			<b>You have<font color="green">&nbsp;accepted</font>
																			<bean:write name="to" property="msgValue" /></b>
																			<font color="blue"> <br>After
																				verification the payment window will be opened for
																				those whose admission <br> is confirmed (on
																				08-10-2021).
																			</font>
																			<br>The remaining candidates will be given a chance number according to the rank from which their admission will be confirmed as and when the vacancy arises. 
														</logic:equal> <logic:equal value="true" name="to" property="isDecline">
																			<b><bean:write name="to" property="msgValue" /><font
																				color="red">declined</font></b>
																		</logic:equal></td>
																</tr>
															</table>
														</td>
													</tr>
												</logic:iterate>
											</logic:notEmpty>
											
											<logic:equal value="false" name="admissionStatusForm"
												property="specialCourse">
												<logic:equal value="1" name="admissionStatusForm"
													property="programTypeId">
													<logic:equal value="true" name="admissionStatusForm"
														property="isOnceAccept">
														<logic:equal value="false" name="admissionStatusForm"
															property="isUploadDocument">
															<logic:equal value="true" name="admissionStatusForm"
																property="payonline">
															<tr>
																<td align="center" class="bodytext" colspan="5"
																	height="25" class="row-even"><font color="blue"
																	size="3px">The College prefers that a student
																		should take three Diploma/Certificate Add-on courses during the
																		entire duration of the degree programme.<br> The
																		student should give three preferences from the list. After <b>submitting</b> the choices the payment window will be opened.
																</font></td>
															</tr>
															
															<tr class="row-odd">

																<td align="center" class="bodytext" colspan="5"
																	height="25" class="row-even">Choice of
																	Diploma/Certificate Add-on Courses</td>
															</tr>
															<nested:iterate id="admissionpreference"
																name="admissionStatusForm" property="prefList"
																indexId="count">
																<tr class="row-odd">
																	<td align="center" class="bodytext" colspan="5"
																		height="25" class="row-even">
																		<%-- <bean:write property="courseName" name="admissionpreference"/> --%>
																		<html:text property="courseName" styleClass="pref"
																			name="admissionpreference" disabled="true"></html:text>
																	</td>
																</tr>
															</nested:iterate>
															<%--  <%
											String dynaid="coursePreference"+count;;
										%>  --%>
															
															<c:if test="${admissionStatusForm.preCount<3}">
																<c:if
																	test="${admissionStatusForm.certificationCourseDone!=true}">
																	<tr class="row-odd">
																		<td height="25" class="row-odd" colspan="2"
																			align="right">preferences</td>
																		<td height="25" class="row-even" colspan="3">
																			<%-- <bean:define id="prefNo" name="admissionpreference" property="semType"></bean:define> --%>
																			<nested:select property="certicateId"
																				name="admissionStatusForm"
																				onchange="addCourseId(this.value)">
																				<html:option value="">
																					<bean:message key="knowledgepro.admin.select" />
																				</html:option>
																				<html:optionsCollection name="admissionStatusForm"
																					property="certicateCourses" label="value"
																					value="key" />
																			</nested:select>
																		</td>
																	</tr>
																</c:if>
															</c:if>

															

																<c:if
																	test="${admissionStatusForm.certificationCourseDone!=true}">
																	<c:if test="${admissionStatusForm.preCount<3}">
																		<tr class="row-odd">
																			<td align="center" class="bodytext" colspan="5"
																				height="25" class="row-even"><a href="url"
																				style="text-decoration: none; vertical-align: middle; color: #007700; font-weight: bold"
																				onclick="submitAddMorePreferences('submitAddMorePreferences'); return false;">
																					Click here &nbsp;<img
																					src="images/admission/images/12673199831854453770medical cross button.svg.med.png"
																					width="20px" ; height="20px"
																					; style="vertical-align: middle;">
																			</a> to Add More Choice</td>
																		</tr>
																	</c:if>
																	<c:if test="${admissionStatusForm.preCount>0}">
																		<tr class="row-odd">
																			<td align="center" class="bodytext" colspan="5"
																				height="25" class="row-even"><a href="url"
																				style="text-decoration: none; vertical-align: middle; color: #B91A1A; font-weight: bold"
																				onclick="removePreferences(); return false;">
																					Click here &nbsp;<img
																					src="images/admission/images/15107-illustration-of-a-red-close-button-pv.png"
																					width="20px" ; height="20px"
																					; style="vertical-align: middle;">
																			</a> to Remove Choice.&nbsp; &nbsp; &nbsp;</td>
																		</tr>
																	</c:if>
																	<c:if test="${admissionStatusForm.preCount==3}">
																		<tr class="row-odd">
																			<td align="center" class="bodytext" colspan="5"
																				height="25" class="row-even">
																				<button class="formbutton" onclick="submitcourse()">Submit
																					certificate Courses</button>
																			</td>
																		</tr>
																	</c:if>
																</c:if>
															</logic:equal>
															<%-- <nested:iterate id="admissionpreference" name="admissionStatusForm" property="prefcourses" indexId="count">
					      						  <%
													String dynaid="coursePreference"+count;;
													%>
										<tr id="eight">
											<td height="25" class="row-odd" colspan="5">
												<bean:write name="admissionStatusForm" property="prefName"></bean:write>:
											</td>
											<td class="row-even" width="70%">
											<bean:define id="prefNo" name="admissionpreference" property="prefNo"></bean:define>
												<nested:select  property="id" styleClass="dropdownmedium"  styleId="<%=dynaid %>" onchange='<%="addCourseId(this.value,"+prefNo+")"%>'>
													<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
													<html:optionsCollection name="admissionStatusForm" property="courseMap" label="value" value="key"/>	
												</nested:select>
											</td>
										</tr>
										</nested:iterate> --%>
															<%-- <c:if  test="${admissionStatusForm.isPaid==true}"> --%>

															<%-- </c:if>		 --%>

															<!--  <tr id="third">
											<td height="25" colspan="4">
												<table width="100%" height="53" border="0" cellpadding="0" cellspacing="1">
													<tr class="row-even">
														<td height="25" class="bodytext" width="60">
															<div align="center" style="font-size: 14px">
																	<a href="downloadForms/Declaration_General.pdf" download=" Declaration_General">Declaration I</a><br><br>
																	<a href="downloadForms/Declarion_Ceretificate Due 2020.pdf" download="Declarion_Ceretificate Due 2020">Declaration II</a><br><br>
																	<a href="downloadForms/Declaration_PTA.pdf" download="Declaration_PTA">Declaration III</a><br><br>
																	<a href="downloadForms/Anti ragging undertaking.pdf" download="Anti ragging undertaking">Declaration IV</a><br><br>	
																	<a href='<bean:write name="admissionStatusForm"  property="formlink" />' class="menulink" target="_blank">
																		 <b>Click here to Upload Certificate</b> 
																	</a>
															</div>
														</td>
														
													</tr>
												</table>  
											</td>
										</tr>   -->
															<!--	<tr id="fourth">
											<td height="25" colspan="4">
												<table width="100%" height="53" border="0" cellpadding="0" cellspacing="1">
													<tr class="row-even">
														<td height="25" class="bodytext" width="60">
															<div align="center" style="font-size: 14px">
															<html:radio property="isChecked" value="false" name="admissionStatusForm" styleId="isChecked">
															<i>I hereby declare that, I have uploaded the documents and the documents submitted are true and genuine.</i></html:radio>
															</div>
														</td>
													</tr>
												</table>
											</td>
										</tr>  -->
															<!--	<tr id="fifth">
											<td height="25" colspan="4">
												<table width="100%" height="53" border="0" cellpadding="0" cellspacing="1">
													<tr class="row-even">
														
														<td height="25" class="bodytext" width="60" style="font-size: 14px">
															<div align="center">Enter the verification code after uploading the documents:
															<html:text property="uploadId" name="admissionStatusForm" styleId="uploadId"></html:text>
															<html:button property="" styleId="verify" value="Verify" styleClass="formbutton" onclick="uploadDoc()"></html:button>
															</div>
														</td>
														
													</tr> 
												</table>
											</td>
										</tr> -->
															<!-- <tr><td colspan="4" align="center"> <font color="red"><b>You have not uploaded the documents within the stipulated time, last date was 12th September 2020.</b></font></td></tr>  -->
														</logic:equal>
														<tr class="row-odd">
																			<td align="center" class="bodytext" colspan="5"
																				height="25" class="row-even">
																					<div align="center">
																						<logic:equal value="false" name="admissionStatusForm" property="payonline">
																 <b> <font color="blue" size="3px" ><b>You are in the waiting list.</b><br>  
																To know your new chance number check the admission link of the college website (www.mic.ac.in).<br><b>As an when the vacancy arises you will be informed by SMS/Email/Mobile. </b></font>
															</logic:equal> 
															</div>
															</td>
															</tr>
														<c:if
															test="${admissionStatusForm.certificationCourseDone==true}">
															<logic:equal value="2" name="admissionStatusForm"
																property="maxallotment">

																<tr id="sixth">
																	<td height="25" colspan="4">
																		<table width="100%" height="53" border="0"
																			cellpadding="0" cellspacing="1">
																			<tr class="row-even" id="seven">
																				<td height="25" class="bodytext" width="60">
																					<div align="center">
																						<%-- <logic:equal value="false" name="admissionStatusForm" property="payonline">
                                                                                                                                <font color="blue"><b>You have successfully uploaded the documents.</b>    
																After verification the payment window will be opened for those whose admission is confirmed(on 14-09-2020).<br>The remaining candidates will be given a chance number according to the rank from which their admission will be confirmed as and when the vacancy arises.</font> 
																 <b> <font color="blue"><b>You are in the waiting list.</b><br>  
																To know your new chance number check the admission link of the college website (www.mic.ac.in).<br><b>As an when the vacancy arises you will be informed by SMS/Email/Mobile. </b></font> 
															</logic:equal>  --%>
																						<logic:equal value="true"
																							name="admissionStatusForm" property="payonline">
																							<div align="center" style="color:blue;padding: 10px">
																								<b>You are admitted to : </b> <bean:write property="courseName" name="admissionStatusForm" />
																							</div>
																							<c:choose>
																								<c:when
																									test="${admissionStatusForm.isPaid==true}">
																									<font color="green" size="3px">You have
																										successfully paid the College/ University/
																										Govt Fees for UG Admission 2021 and your
																										admission is provisionally confirmed. </font>
																									<font color="blue" size="3px"><p>The
																												Interview schedule is given below. For
																												detailed schedule see UG Admission portal of the
																												College website.</p></font>
																										<p></p>
																										<table class="minimalistBlack" border="1">
																											<thead>
																												<tr>
																													<th>Date</th>
																													<th>09:30am to 12:30pm</th>																							
																												</tr>
																											</thead>
																											<tbody>
																												<tr>
																													<td>22 September 2021</td>
																													<td>B.A. Economics<br />
																													B.A. English Language and
																														Literature<br /> B.A. Journalism and Mass
																														Communication<br />B.Sc. Mathematics<br />Botany &amp; Biotechnology<br />B.Sc. Statistics
																														(Self-Financing)<br />B.A. English &amp; Communicative English
																														(Self-Financing)

																													</td>
																												</tr>
																												
																											</tbody>
																										</table>
<table>
<tr><td align="center"><font color="blue" size="3px">You are asked to come for a physical interview at the college with your parent/ guardian at the date and time mentioned in the interview schedule. 
While coming for the physical interview you have to bring the printouts of the following forms.
The admission process will be conducted under strict COVID protocol restrictions.</font>
<td>
</tr>
</table>
																								</c:when>
																								<c:otherwise>
																									<div align="center">
																										<font color="red">*Payment Gateway
																											service charges*<br> &nbsp;&nbsp;<br>
																											Credit Card - 1.95%+gst <br>
																											&nbsp;&nbsp;<br> Debit Card - 1.1%+gst<br>
																											&nbsp;&nbsp;<br> Net Banking - Rs 25+gst<br>
																											&nbsp;&nbsp;<br> *Google Pay/UPI/Rupay
																											DC - NIL*<br> &nbsp;&nbsp;<br> CC
																											EMI - 1.95%+gst<br> &nbsp;&nbsp;<br>
																										</font> If the amount is debited from your account wait for 24 hrs. If you have any payment issues mail to<font color="blue"> <u>admission2021@mic.ac.in</u></font>
																									</div>
																									<br>			
																			&nbsp;&nbsp;<br>

																								<!--	<html:button property="" styleId="payFees"
																										styleClass="formbutton" value="Pay Fees"
																										onclick="payment()"></html:button>   --> 
                                                                                                                                                                                                              <!-- <div align="center"> <font color="red">The last date for paying the fees was 31st August 2021.</font></div>  -->
																								</c:otherwise>
																							</c:choose>
																						</logic:equal>
																					</div>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<c:if test="${admissionStatusForm.isPaid==true}">
																	<tr>
																		<td style="text-align: center" colspan="5"><br>
																		<br>
																		<br>
																		<br> <html:hidden property="applicationNo"
																				name="admissionStatusForm" styleId="appNo"></html:hidden>
																			<html:hidden property="appliedYear"
																				name="admissionStatusForm" styleId="year"></html:hidden>
																			<table width="100%">
																				<tr>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(4);">Application Form
																							 &nbsp; </a></td>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(3);">Bio Data Form
																							(Original & Duplicate) &nbsp; </a></td>
																					
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(1);">Anti-Ragging
																							&nbsp; </a></td>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(2);">
																							PTA Membership Form &nbsp; </a></td>
																					
																					

																					
																				</tr>
																				<tr height="30px"></tr>
																				<tr>
																					<td colspan="2"
																						align="center"><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="downloadmemo();">Download Memo</a>
																						</td>
																						<td colspan="2"
																						align="center"><a  href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="downloadChanceMemo();">Download Chance Memo</a></td>
																						
																				</tr>
																			</table></td>
																	</tr>
																</c:if>
															</logic:equal>
														</c:if>
													</logic:equal>
												</logic:equal>
											</logic:equal>
											<c:if
												test="${admissionStatusForm.programTypeId==2}">
												<logic:equal value="true" name="admissionStatusForm"
													property="isOnceAccept">

														<tr id="sixth">
											<td height="25" colspan="4">
												<table width="100%" height="53" border="0" cellpadding="0" cellspacing="1">
													<tr class="row-even" id="seven">
														<td height="25" class="bodytext" width="60">
															<div align="center">
															 <logic:equal value="false" name="admissionStatusForm" property="payonline">
                                                                                                                               <b> <font color="blue"><b>You are in the waiting list.</b><br>  
																To know your new chance number check the admission link of the college website (www.mic.ac.in).<br><b>As an when the vacancy arises you will be informed by SMS/Email/Mobile. </b></font>
															</logic:equal>  															<logic:equal value="true" name="admissionStatusForm" property="payonline">
															<c:choose>
																<c:when test="${admissionStatusForm.isPaid==true}">
																	You have successfully paid the fees.  <font color = "blue" size= "12px" >   <p> You are asked to report with the original certificates and any one parent at Mar Ivanios College at 09.30 AM on 07.12.2020. Those candidates who have already taken admission in other colleges should come to the college and confirm admission before taking their TC from the institution they have joined. The admission process will be conducted under strict COVID protocol restrictions.</p></font>
																
																</c:when>
																<c:otherwise>
																<div align="center">
																	<font color="red">*Payment Gateway service charges*<br>
																			&nbsp;&nbsp;<br>
																		Credit Card - 1.95%+gst <br>
																			&nbsp;&nbsp;<br>
																		Debit Card  - 1.1%+gst<br>
																			&nbsp;&nbsp;<br>
																		Net Banking - Rs 25+gst<br>
																			&nbsp;&nbsp;<br>
																		*Google Pay/UPI/Rupay DC - NIL*<br>
																			&nbsp;&nbsp;<br>
																		CC EMI - 1.95%+gst<br>
																			&nbsp;&nbsp;<br></font>

																		If you have any payment issue mail<font color="blue"> <u>it.admin@mic.ac.in</u></font></div>		<br>			
																			&nbsp;&nbsp;<br>

																	<html:button property="" styleId="payFees" styleClass="formbutton" value="Pay Fees" onclick="payment()"></html:button>
																</c:otherwise>
															</c:choose>
															</logic:equal> 
															</div>
														</td>
													</tr>  
												</table>
											</td>
										</tr>  
										<c:if test="${admissionStatusForm.isPaid==true}">
																	<tr>
																		<td style="text-align: center" colspan="5"><br>
																		<br>
																		<br>
																		<br> <html:hidden property="applicationNo"
																				name="admissionStatusForm" styleId="appNo"></html:hidden>
																			<html:hidden property="appliedYear"
																				name="admissionStatusForm" styleId="year"></html:hidden>
																			<table width="100%">
																				<tr>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(4);">Application Form
																							 &nbsp; </a></td>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(3);">Bio Data Form
																							(Original & Duplicate) &nbsp; </a></td>
																					
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(1);">Anti-Ragging
																							&nbsp; </a></td>
																					<td><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="printPage(2);">
																							PTA Membership Form &nbsp; </a></td>
																					
																					

																					
																				</tr>
																				<tr height="30px"></tr>
																				<tr>
																					<td colspan="2"
																						align="center"><a href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="downloadmemo();">Download Memo</a>
																						</td>
																						<td colspan="2"
																						align="center"><a  href="javascript:void(0)"
																						style="color: color:blue; font-size: 15px; text-decoration: none"
																						href="" onclick="downloadChanceMemo();">Download Chance Memo</a></td>
																						
																				</tr>
																			</table></td>
																	</tr>
																</c:if>
												</logic:equal>
											</c:if>
										</table>
										<div align="center">
											<table width="100%" height="27" border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td><div align="center"></div></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</table>
										</div>
									</td>
									<td width="10" valign="top" background="images/Tright_3_3.gif"
										class="news"></td>
								</tr>
								<tr>
									<td><img src="images/Tright_03_05.gif" width="9"
										height="29"></td>
									<td width="949" background="images/Tcenter.gif"></td>
									<td><img src="images/Tright_02.gif" width="9" height="29"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td valign="top">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
<SCRIPT language="JavaScript">
var browserName=navigator.appName; 
document.getElementById("chancOrAllotment").value="";
document.getElementById("selectedCourseId").value=null;
document.getElementById("selectedValue").value=null;
if (browserName=="Microsoft Internet Explorer")
{
	document.getElementById("message").innerHTML="<b style='color:red'>Note:</b> Use Mozilla Firefox for better Performance and view  or Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling.";
	alert(" If Your using InternetExplorer Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling  or Use Mozilla FireFox");
}
var serverDownMessage=$('#serverDownMessage').val();
if(serverDownMessage!=null && serverDownMessage!=""){
	 $(".ZebraDialog_Title a.ZebraDialog_Close").hide();
	$.Zebra_Dialog('<strong>'+serverDownMessage+'</strong>',{
		'title':    'Alert',
		'buttons':  ['Hide'],
		'keyboard':false,
		'overlay_close':false,
		'show_close_button':false
	});
}
</SCRIPT>