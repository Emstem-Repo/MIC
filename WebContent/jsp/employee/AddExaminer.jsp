<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.utilities.CommonUtil"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Guest Faculty Information Edit Submission</title>
<LINK REL=StyleSheet HREF="css/styles.css" TYPE="text/css">
<script language="JavaScript" src="js/admissionform.js"></script>
<script language="JavaScript" src="js/calendar_us.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<link rel="stylesheet" href="css/calendar.css">

<script type="text/javascript">
<!--
	function setFocus() {
		var Focus = document.getElementById("focusValue").value;
		var txtBox = document.getElementById("Focus").value;
		document.all('txtBox').focus();
		return false;

	}

	function maxlength(field, size) {
		if (field.value.length > size) {
			field.value = field.value.substring(0, size);
		}
	}
	function Print() {
		document.location.href = "GuestFaculty.do?method=getResumeDetails&guestId="
				+ id;

	}

	var destId;
	function closeWindow() {
		document.getElementById("method").value = "getSearchedGuest";
		document.GuestFacultyInfoForm.submit();
		//document.location.href = "LoginAction.do?method=loginAction";
	}

	function resetEmpInfo() {
		document.getElementById("method").value = "initEmployeeInfo";
		document.GuestFacultyInfoForm.submit();
	}

	function saveEmpDetails() {
		document.getElementById("method").value = "addEmployeeInfoAdd";
		document.GuestFacultyInfoForm.submit();
	}
	function submitEmployeeInfoAdd(method, mode) {
		document.getElementById("method").value = method;
		document.getElementById("mode").value = mode;
		document.GuestFacultyInfoForm.submit();
	}

	function getStateByCountry(country, stateId) {
		destId = stateId;
		getStatesByCountry("stateMap", country, stateId, updateState);
	}

	function getCurrentStateByCountry(country, stateId) {
		destId = stateId;
		getStatesByCountry("currentStateMap", country, stateId, updateState);
	}
	function updateState(req) {
		updateOptionsFromMapWithOther(req, destId, "-Select-");
	}
	var totalExp;
	function getYears(field) {
		//checkForEmpty(field);
		if (isNaN(field.value)) {
			document.getElementById("err").innerHTML = "please enter valid number";
			error = true;
			return;
		}
		getTotalYears();
		getTotalMonths();
		if (totalMonth >= 12) {
			totalExp = totalExp + (totalMonth / 12);
			totalMonth = parseInt(totalMonth % 12);
			document.getElementById("expYears").value = parseInt(totalExp);
		}
		document.getElementById("expMonths").value = totalMonth;
	}

	function getTotalYears() {
		totalExp = 0;
		size = document.getElementById("teachingExpLength").value;
		for (i = 0; i <= size; i++) {
			if (document.getElementById("teach_" + i).value != '')
				totalExp = totalExp
						+ parseInt(document.getElementById("teach_" + i).value);
		}
		isize = document.getElementById("industryExpLength").value;
		for (i = 0; i <= isize; i++) {
			if (document.getElementById("industry_" + i).value != '')
				totalExp = totalExp
						+ parseInt(document.getElementById("industry_" + i).value);
		}
		document.getElementById("expYears").value = totalExp;
	}
	var totalMonth;
	function getMonths(field) {
		//	checkForEmpty(field);
		if (isNaN(field.value)) {
			document.getElementById("err").innerHTML = "please enter valid month";
			error = true;
			return;
		}
		getTotalMonths();
		if (totalMonth >= 12) {
			getTotalYears();
			totalExp = totalExp + (totalMonth / 12);
			totalMonth = parseInt(totalMonth % 12);
			document.getElementById("expYears").value = parseInt(totalExp);
		}
		document.getElementById("expMonths").value = totalMonth;
	}

	function getTotalMonths() {
		totalMonth = 0;
		size = document.getElementById("teachingExpLength").value;
		for (i = 0; i <= size; i++) {
			if (document.getElementById("teach_month_" + i).value != '')
				totalMonth = totalMonth
						+ parseInt(document.getElementById("teach_month_" + i).value);
		}
		isize = document.getElementById("industryExpLength").value;
		for (i = 0; i <= isize; i++) {
			if (document.getElementById("industry_month_" + i).value != '')
				totalMonth = totalMonth
						+ parseInt(document.getElementById("industry_month_"
								+ i).value);
		}
	}

	function checkForEmpty(field) {
		if (field.value.length == 0)
			field.value = "0";
		if (field.value == 0)
			field.value = "0";

	}

	function disableAddress() {
		document.getElementById("currLabel").style.display = "none";
		document.getElementById("currTable").style.display = "none";
	}
	function enableAddress() {
		document.getElementById("currLabel").style.display = "block";
		document.getElementById("currTable").style.display = "block";
	}
	function imposeMaxLength(evt, desc) {
		var keynum = (evt.which) ? evt.which : event.keyCode;
		if (keynum == 8 || keynum == 37 || keynum == 39 || keynum == 9) {
			return true;
		}
		var MaxLen = 10;
		return (desc.length < MaxLen);
	}

	function CalAge() {

		var now = new Date();

		var Dob = document.getElementById("dateOfBirth").value;

		bD = Dob.split('/');
		if (bD.length == 3) {
			born = new Date(bD[2], bD[1] * 1 - 1, bD[0]);
			years = Math.floor((now.getTime() - born.getTime())
					/ (365.25 * 24 * 60 * 60 * 1000));
			document.getElementById("age").value = years;
			alert(years);
		}
	}

	function searchStreamWise(streamId) {
		getDepartmentByStreamWise(streamId, updateDepartmentMap);
	}
	function updateDepartmentMap(req) {
		updateOptionsFromMap(req, "departmentId", "-Select-");
	}

	function removeTextField(countValue, testValue) {

		if (document.getElementById(countValue).checked == true
				&& document.getElementById(countValue).value == "OTHER") {
			document.getElementById("otherEligibilityTest").style.display = "block";
		} else if (document.getElementById(countValue).checked == false
				&& document.getElementById(countValue).value == "OTHER") {
			document.getElementById("otherEligibilityTestValue").value = "";
			document.getElementById("otherEligibilityTest").style.display = "none";
		}
	}

	function getOtherEligibilityTest() {
		if (document.getElementById("eligibilityTestOther").checked == true) {
			document.getElementById("otherEligibilityTest").style.display = "block";
		} else if (document.getElementById("eligibilityTestOther").checked == false) {
			document.getElementById("otherEligibilityTestValue").value = "";
			document.getElementById("otherEligibilityTest").style.display = "none";
		}
	}

	function disableRemainingEligibilityTest() {
		if (document.getElementById("eligibilityTestNone").checked == true) {
			document.getElementById("eligibilityTestNET").checked = false;
			document.getElementById("eligibilityTestSLET").checked = false;
			document.getElementById("eligibilityTestSET").checked = false;
			document.getElementById("eligibilityTestOther").checked = false;
			document.getElementById("eligibilityTestNET").disabled = true;
			document.getElementById("eligibilityTestSLET").disabled = true;
			document.getElementById("eligibilityTestSET").disabled = true;
			document.getElementById("eligibilityTestOther").disabled = true;
			document.getElementById("otherEligibilityTestValue").value = "";
		} else {
			document.getElementById("eligibilityTestNET").disabled = false;
			document.getElementById("eligibilityTestSLET").disabled = false;
			document.getElementById("eligibilityTestSET").disabled = false;
			document.getElementById("eligibilityTestOther").disabled = false;
		}
	}
	function disableEligibilityTestNone(property) {
		if (document.getElementById(property).checked == true) {
			document.getElementById("eligibilityTestNone").checked = false;
			document.getElementById("eligibilityTestNone").disabled = true;
		} else {
			document.getElementById("eligibilityTestNone").disabled = false;
		}
	}
	function getPersonDisability() {
		if (document.getElementById("personWithDisability").checked == true) {

			document.getElementById("handicappedDescription").style.display = "block";
		} else {
			document.getElementById("handicappedDescription").style.display = "none";
		}
	}
	function showHandicappedDescription() {
		document.getElementById("handicappedDescription").style.display = "block";
	}

	function hideHandicappedDescription() {
		document.getElementById("handicappedDescription").style.display = "none";
		document.getElementById("handicappedDescription").value = "";
	}

	function imposeMaxLength12(Object, MaxLen) {
		return (Object.value.length < (MaxLen));
	}

	function imposeMaxLength1(field, size) {
		if (field.value.length > size) {
			field.value = field.value.substring(0, size);
		}
	}

	// to display the text areas length 
	function len_display(Object, MaxLen, element) {
		var len_remain = MaxLen + Object.value.length;
		if (len_remain <= 500) {
			document.getElementById(element).value = len_remain;
		}
	}

	var print = "<c:out value='${GuestFacultyInfoForm.printPage}'/>";
	if (print.length != 0 && print == "true") {
		var url = "GuestFaculty.do?method=printResume";
		myRef = window
				.open(url, "ViewResume",
						"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
	}
	function duplicateCheck(staffId) {
		checkDupilcateOfStaffId(staffId, updateStaffId);
	}
	function updateStaffId(req) {
		var responseObj = req.responseXML.documentElement;
		var value = responseObj.getElementsByTagName("value");
		if (value != null) {
			for (var I = 0; I < value.length; I++) {
				if (value[I].firstChild != null) {
					var temp = value[I].firstChild.nodeValue;
					document.getElementById("invalidStaffId").innerHTML = temp;
				}
			}
		}
	}
</script>
</head>

<script type="text/javascript">
	function setFocus() {
		var Focus = document.getElementById("focusValue").value;
		var txtBox = document.getElementById("Focus");
		document.all('txtBox').focus();

	}

	function putFocusOnField() {
		var focusField = document.getElementById("focusValue").value;
		if (focusField != null) {
			if (document.getElementById(focusField).type != 'hidden') {
				document.getElementById(focusField).focus();
			}
		}

	}

	function departmentByStream(count) {
		var strmId = document.getElementById("strmId_" + count).value;
		document.getElementById("count").value = count;
		getDepartmentByStreamWise(strmId, updateDepartmentMapByStream);

	}
	function updateDepartmentMapByStream(req) {
		var count = document.getElementById("count").value;
		var deptId = "deptId_" + count;
		updateOptionsFromMap(req, deptId, "-Select-");
	}
	function caps(element) {
		element.value = element.value.toUpperCase();
	}
	function openURL() {
		var url = "http://www.ifsc4bank.com";
		myRef = window.open(url);
	}
</script>
<body>
	<table width="100%" border="0">

		<html:form action="/ExaminerInfo" enctype="multipart/form-data">
			<html:hidden property="pageType" value="7" />
			<html:hidden property="method" value="addEmployeeInfoAdd" styleId="method" />
			<html:hidden property="formName" value="GuestFacultyInfoForm" />
			<html:hidden property="mode" styleId="mode" value="" />
			<html:hidden property="focusValue" styleId="focusValue" />
			<html:hidden property="listSize" styleId="listSize" />
			<input type="hidden" id="count" />
			<tr>
				<td><span class="Bredcrumbs"><bean:message
							key="knowledgepro.admin.sec.EmployeeCategory" /> <span
						class="Bredcrumbs">&gt;&gt; <bean:message
								key="knowledgepro.guest.faculty" /> &gt;&gt;
					</span></span></td>
			</tr>

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="10"><img src="images/Tright_03_01.gif" width="9"
								height="29" /></td>
							<td width="1271" background="images/Tcenter.gif" class="body">
								<div align="left">
									<strong class="boxheader"><bean:message
											key="knowledgepro.guest.faculty" /></strong>
								</div>
							</td>
							<td width="15"><img src="images/Tright_1_01.gif" width="9"
								height="29" /></td>
						</tr>
						<tr>
							<td height="122" valign="top"
								background="images/Tright_03_03.gif"></td>
							<td valign="top" class="news">
								<table width="100%" height="50" border="0" cellpadding="0"
									cellspacing="0">
									<tr>
										<td height="20" colspan="6" align="left">
											<div align="right" style="color: red">
												<span class='MandatoryMark'>* Mandatory fields</span>
											</div>
											<div id="err"
												style="color: red; font-family: arial; font-size: 11px;"></div>
											<div id="errorMessage">
												<FONT color="red"><html:errors /></FONT> <FONT color="green">
													<html:messages id="msg" property="messages" message="true">
														<c:out value="${msg}" escapeXml="false"></c:out>
														<br>
													</html:messages>
												</FONT>
											</div>
										</td>
									</tr>



									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>
									</tr>


									<tr>
										<td colspan="2" class="heading" align="left"><bean:message
												key="knowledgepro.employee.personal.details" /></td>
									</tr>

									<tr>
										<td valign="top" class="news" colspan="3">
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
																<td class="row-odd" width="75">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.admin.name" />
																	</div>
																</td>
																<td class="row-even"><html:text property="name"
																		styleId="name" size="35" maxlength="100"
																		style="text-transform:uppercase;"></html:text></td>
																<td class="row-odd" width="75">
																	<div align="left">
																		<bean:message key="knowledgepro.employee.mobile" />
																	</div>
																</td>
																<td class="row-even" width="36%"><html:text
																		property="mobileNo1"
																		onkeypress="return isNumberKey(event)" maxlength="10"></html:text>
																</td>
															</tr>

															
															<tr>
																<td class="row-odd">
																	<div align="left">
																		<bean:message key="admissionFormForm.emailId" />
																	</div>
																</td>
																<td class="row-even"><html:text property="email"
																		size="20" maxlength="50"></html:text></td>
																		
																<td class="row-odd">
																	<%-- <div align="left">
																		<bean:message
																			key="knowledgepro.employee.officialEmailId" />
																	</div> --%>
																</td>
																<td class="row-even"><%-- <html:text
																		property="officialEmail" size="20" maxlength="50">
																	</html:text> --%></td>
																		
															</tr>






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


									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>
									</tr>


									<tr>
										<td colspan="2" class="heading" align="left"><bean:message
												key="admissionForm.studentinfo.currAddr.label" /></td>
									</tr>



									<tr>
										<td valign="top" class="news" colspan="3">
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
																	<td class="row-odd" width="13%">
																		<div align="left">
																			<span class="Mandatory">*</span>
																			<bean:message
																				key="admissionForm.studentinfo.addrs1.label" />
																		</div>
																	</td>
																	<td class="row-even" width="37%"><html:text
																			property="currentAddressLine1"
																			styleId="currentAddressLine1" size="35"
																			maxlength="100"></html:text></td>

																	<td class="row-odd" width="14%">
																		<div align="left">
																			<bean:message
																				key="admissionForm.studentinfo.addrs2.label" />
																		</div>
																	</td>
																	<td class="row-even" width="36%"><html:text
																			property="currentAddressLine2"
																			styleId="currentAddressLine2" size="40"
																			maxlength="100"></html:text></td>
																</tr>
														
															<tr>

																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="employee.info.contact.City" />
																	</div>
																</td>
																<td class="row-even"><html:text
																		property="currentCity" styleId="currentCity" size="40"
																		maxlength="40"></html:text></td>
																<td class="row-odd" align="left"><bean:message
																		key="knowledgepro.usermanagement.userinfo.pincode" />
																</td>
																<td class="row-even"><html:text
																		property="currentZipCode" styleId="currentZipCode"
																		onkeypress="return isNumberKey(event)" size="20"
																		maxlength="10"></html:text></td>
															</tr>

															<tr>
																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.admin.country.report" />
																	</div>
																</td>

																<td class="row-even"><html:select
																		property="currentCountryId" styleId="currentCountryId"
																		onchange="getCurrentStateByCountry(this.value,'currentState')">
																		<html:option value="">
																			<bean:message key="knowledgepro.select" />
																		</html:option>
																		<logic:notEmpty property="currentCountryMap"
																			name="GuestFacultyInfoForm">
																			<html:optionsCollection property="currentCountryMap"
																				label="value" value="key" />
																		</logic:notEmpty>
																	</html:select></td>

																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.admin.state.report" />
																	</div>
																</td>
																<td class="row-even"><html:select
																		property="currentState" styleId="currentState"
																		onchange="getOtherCurrentState()">
																		<html:option value="">
																			<bean:message key="knowledgepro.select" />
																		</html:option>
																		<logic:notEmpty property="currentStateMap"
																			name="GuestFacultyInfoForm">
																			<html:optionsCollection property="currentStateMap"
																				label="value" value="key" />
																		</logic:notEmpty>
																	</html:select> <input type="hidden" id="tempState" name="tempState"
																	value='<bean:write name="GuestFacultyInfoForm" property="currentState"/>' />

																	<div id="otehrState">
																		<html:text property="otherCurrentState"
																			name="GuestFacultyInfoForm" size="20" maxlength="50"></html:text>
																	</div></td>
															</tr>


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


									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>
									</tr>

									<tr>
										<td colspan="2" class="heading">&nbsp;&nbsp;<bean:message
												key="knowledgepro.applicationform.sameaddr.label" /> <html:radio
												property="sameAddress" styleId="sameAddr" value="true"
												onclick="disableAddress()">
												<bean:message key="knowledgepro.applicationform.yes.label" />
											</html:radio> <html:radio property="sameAddress" styleId="DiffAddr"
												value="false" onclick="enableAddress()">
												<bean:message key="knowledgepro.applicationform.No.label" />
											</html:radio></td>
									</tr>
									<tr>
										<td colspan="2" class="heading" align="left">&nbsp;
											<div id="currLabel">
												<bean:message key="admissionForm.studentinfo.permAddr.label" />
											</div>
										</td>
									</tr>


									<tr>


										<td valign="top" class="news" colspan="3">
											<table width="100%" border="0" align="center" cellpadding="0"
												cellspacing="0" id="currTable">
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
																	<td class="row-odd" width="13%">
																		<div align="left">
																			<span class="Mandatory">*</span>
																			<bean:message
																				key="admissionForm.studentinfo.addrs1.label" />
																		</div>
																	</td>
																	<td class="row-even" width="37%"><html:text
																			property="addressLine1" styleId="addressLine1"
																			size="35" maxlength="100"></html:text></td>
																	<td class="row-odd" width="14%">
																		<div align="left">
																			<bean:message
																				key="admissionForm.studentinfo.addrs2.label" />
																		</div>
																	</td>
																	<td class="row-even" width="36%"><html:text
																			property="addressLine2" styleId="addressLine2"
																			size="40" maxlength="100"></html:text></td>
																</tr>
														
															<tr>
																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="employee.info.contact.City" />
																	</div>
																</td>
																<td class="row-even"><html:text property="city"
																		styleId="city" size="40" maxlength="40"></html:text></td>
																<td class="row-odd" align="left"><bean:message
																		key="knowledgepro.usermanagement.userinfo.pincode" />
																</td>
																<td class="row-even"><html:text
																		property="permanentZipCode" styleId="permanentZipCode"
																		onkeypress="return isNumberKey(event)" size="20"
																		maxlength="10"></html:text></td>
															</tr>

															<tr>
																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.admin.country.report" />
																	</div>
																</td>
																<td class="row-even"><html:select
																		property="countryId" styleId="countryId"
																		onchange="getStateByCountry(this.value,'stateId')">
																		<html:option value="">
																			<bean:message key="knowledgepro.select" />
																		</html:option>
																		<logic:notEmpty property="countryMap"
																			name="GuestFacultyInfoForm">
																			<html:optionsCollection property="countryMap"
																				label="value" value="key" />
																		</logic:notEmpty>
																	</html:select></td>

																<td class="row-odd">
																	<div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.admin.state.report" />
																	</div>
																</td>
																<td class="row-even"><html:select
																		property="stateId" styleId="stateId"
																		onchange="getOtherPermanentState()">
																		<html:option value="">
																			<bean:message key="knowledgepro.select" />
																		</html:option>
																		<logic:notEmpty property="stateMap"
																			name="GuestFacultyInfoForm">
																			<html:optionsCollection property="stateMap"
																				label="value" value="key" />
																		</logic:notEmpty>
																	</html:select> <input type="hidden" id="tempPermanentState"
																	name="tempPermanentState"
																	value='<bean:write name="GuestFacultyInfoForm" property="stateId"/>' />

																	<div id="otehrPermState">
																		<html:text property="otherPermanentState"
																			name="GuestFacultyInfoForm" size="20" maxlength="50"></html:text>
																	</div></td>
															</tr>
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





									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>
									</tr>

									<tr>
										<td colspan="2" class="heading" align="left"><bean:message
												key="knowledgepro.employee.Job" /></td>
									</tr>

									<tr>
										<td valign="top" class="news" colspan="3">
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
																<td height="25" class="row-odd"><div align="left">Retired
																	</div></td>
																<td class="row-even"><span class="star"> <html:radio
																			property="retired" value="1" />Yes&nbsp; <html:radio
																			property="retired" value="0" />No
																</span></td>
																<td width="13%" class="row-odd"><div align="left">
																		<span class="Mandatory">*</span>
																		<bean:message key="knowledgepro.employee.Department" />
																	</div></td>
																<td width="37%" class="row-even"><html:select
																		property="departmentId" styleId="departmentId"
																		styleClass="comboExtraLarge">
																		<html:option value="">
																			<bean:message key="knowledgepro.select" />
																		</html:option>
																		<logic:notEmpty property="departmentMap"
																			name="GuestFacultyInfoForm">
																			<html:optionsCollection property="departmentMap"
																				label="value" value="key" />
																		</logic:notEmpty>
																	</html:select></td>
															</tr>
															
															
															<tr>

																<td class="row-odd"><div align="left">
																		<span class="Mandatory">*</span>
																		College Name
																	</div></td>
																<td height="25" class="row-even"><span
																	class="star"> <html:text
																			property="collegeName"
																			styleId="collegeName" size="20"
																			maxlength="100"></html:text>
																</span></td>
																<td class="row-odd"><div align="left">Experience(in years)
																	</div></td>
																<td height="25" class="row-even"><span
																	class="star"> <html:text property="expYears"
																			styleId="expYears" size="20" maxlength="100"></html:text>
																</span></td>

															</tr>
															
															

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

									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>

									</tr>
									<tr>
										<td align="center" colspan="6"><html:button property=""
												styleClass="formbutton" value="Submit"
												onclick="saveEmpDetails()"></html:button>&nbsp;&nbsp; <html:button
												property="" styleClass="formbutton" value="Close"
												onclick="closeWindow()"></html:button></td>
									</tr>
									<tr>
										<td height="19" valign="top" background="images/separater.gif"></td>
										<td valign="top" class="news">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">

											</table>
										</td>
										<td height="19" valign="top" background="images/separater.gif"></td>
									</tr>


									<tr>
										<td><img src="images/Tright_03_05.gif" width="9"
											height="29" /></td>
										<td width="100%" background="images/TcenterD.gif"></td>
										<td><img src="images/Tright_02.gif" width="9" height="29" /></td>
									</tr>


								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</html:form>
	</table>

</body>
<script type="text/javascript">

			//size=document.getElementById("listSize").value;
			//var size1=size-1;
			var focusField=document.getElementById("focusValue").value;
		    if(focusField != null){  
			    if(document.getElementById(focusField)!=null)      
		            document.getElementById(focusField).focus();
			}



			
			var sameAddr= document.getElementById("sameAddr").checked;

			if(sameAddr==true){
				disableAddress();
			}
			if(sameAddr==false){
				enableAddress();
			}
			countryId=document.getElementById("currentCountryId").value;
			if(countryId!=''){
				setTimeout("getCurrentStateByCountry(countryId,'currentState')",1000); 
				setTimeout("setData1()",1800); 
			}
			
			cId=document.getElementById("countryId").value;
			if(cId!=''){
				setTimeout("getStateByCountry(cId,'stateId')",3000); 
				setTimeout("setData2()",3500); 
			}
			function setData1(){
				stateId=document.getElementById("tempState").value;
				document.getElementById('currentState').value=stateId;
			}
			function setData2(){ 
				var stId=document.getElementById("tempPermanentState").value;
				document.getElementById('stateId').value=stId;
			}

			function getOtherCurrentState(){
				other=document.getElementById("currentState").value;
				if(other=="Other"){
					document.getElementById("otehrState").style.display="block";
				}else{
					document.getElementById("otehrState").style.display="none";
				}
			}

			var tempOther=document.getElementById("tempState").value;
			if(tempOther=="Other"){
				document.getElementById("otehrState").style.display="block";
			}else{
				document.getElementById("otehrState").style.display="none";
			}

			function getOtherPermanentState(){
				other=document.getElementById("stateId").value;
				if(other=="Other"){
					document.getElementById("otehrPermState").style.display="block";
				}else{
					document.getElementById("otehrPermState").style.display="none";
				}
			}

			var tempPermOther=document.getElementById("tempPermanentState").value;
			if(tempPermOther=="Other"){
				document.getElementById("otehrPermState").style.display="block";
			}else{
				document.getElementById("otehrPermState").style.display="none";
			}
			function maxlength(field, size) {
			    if (field.value.length > size) {
			        field.value = field.value.substring(0, size);
			    }
			}
		</script>
</html>






