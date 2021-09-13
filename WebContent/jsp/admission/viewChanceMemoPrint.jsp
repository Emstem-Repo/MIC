<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><style>
.title {
	font-weight: bold;
	font-size: large;
}

.title2 {
	font-weight: bold;
	font-size: medium;
}

body {
	color: black;
}

.mytable {
	border-collapse: collapse;
	border: 1px solid #0000;
	text-align: 
}

.mytabledata {
	font-size: 14px;
}

.mytabledatafee {
	font-size: 14px;
	font-weight: bold;
}

div {
	background-color: white;
	width: 300px;
	border: 2px solid black;
	padding: 25px;
	margin: 25px;
}
</style>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<html:form action="/AdmissionStatus" method="POST">
	<html:hidden property="method" styleId="method" />
	<html:hidden property="formName" value="admissionStatusForm" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionStatusForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId"
		value='<bean:write	name="admissionStatusForm" property="applicantDetails.course.id" />' />
	<table width="100%" border="0" cellpadding="3" cellspacing="0"
		bordercolor="#E0DFDB">
		<tr>
			<td align="center"><img width="600" height="100"
				src='<%=request.getRequestURL().toString().replace(
						request.getRequestURI().substring(0),
						request.getContextPath()).concat("/LogoServlet")%>'
				alt="Logo not available"></td>
		</tr>
		<logic:notEqual value="GENERAL" name="admissionStatusForm"
			property="admissionStatusTO.chanceCategory">
			<logic:notEqual value="COMMUNITY" name="admissionStatusForm"
				property="admissionStatusTO.chanceCategory">
				<tr>
					<td class="title" align="center" style="text-transform: uppercase">
						CHANCE MEMO (RESERVATION - <bean:write name="admissionStatusForm"
						property="admissionStatusTO.chanceCategory" />)</td>
				</tr>
			</logic:notEqual>
		</logic:notEqual>
		<logic:equal value="GENERAL" name="admissionStatusForm"
			property="admissionStatusTO.chanceCategory">
				<tr>
					<td class="title" align="center" style="text-transform: uppercase">
					CHANCE MEMO (<bean:write name="admissionStatusForm"
						property="admissionStatusTO.chanceCategory" />)</td>
				</tr>
		</logic:equal>
		<logic:equal value="COMMUNITY" name="admissionStatusForm"
			property="admissionStatusTO.chanceCategory">
			<tr>
				<td class="title" align="center" style="text-transform: uppercase">
				CHANCE MEMO (<bean:write name="admissionStatusForm"
					property="admissionStatusTO.chanceCategory" />)</td>
			</tr>
		</logic:equal>
		<tr>
			<td align="center" style="font-size: 18px" class="mytabledata"><b>[Memo
			to be Produced at the time of Admission]</b><br></br>
			</td>
		</tr>
		<tr>
			<td width="100%">
			<table width="100%" class="fontsz">

				<tr>
					<td align="left" class="mytabledata">Application No : <bean:write
						name="admissionStatusForm" property="applicantDetails.applnNo" /></td>
					<td align="center" class="mytabledata"><bean:write
						name="admissionStatusForm" property="applicantDetails.mode" /> No
					: <bean:write name="admissionStatusForm"
						property="applicantDetails.journalNo" /></td>
					<td align="right" class="mytabledata">Date : 
						<% 
		  					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    				Date date = new Date();
		    				String currentDate = dateFormat.format(date);
	    				%>
	    				<%=currentDate%>
					</td>
				</tr>
			</table>
			</td>
			</tr>
				<%--azr --%>
				<tr>
					<td align="center" class="title2">ADMISSION DETAILS</td>
				</tr>
				<tr>
					<td>
					<table cellpadding="3" width="100%" align="center" border="1"
						class="mytable" style="border: 1px solid black;">
						<tr class="title2">
							<td align="center">Preference No.</td>
							<td align="center">Programme allotted</td>
							<td align="center">Category under which allotment is made</td>
							<td align="center">Index Mark</td>
							<td align="center">WaitingList Rank(Chance)</td>
							<%-- <td align="center">Bonus Mark</td><td align="center">Negative Mark</td>--%>
							<td align="center">Interview Date and Time</td>
						</tr>
						<logic:notEmpty name="admissionStatusForm"
							property="admissionStatusTO.chanceList">
							<%-- <logic:iterate id="chncList" name="admissionStatusForm"
								property="admissionStatusTO.chanceList"> --%>
								<%
									String chanceCourseId = request.getAttribute(
														"chanceCourseId").toString();
								%>
								<logic:equal value="<%=chanceCourseId%>" name="admissionStatusForm"
									property="admissionStatusTO.chanceCurrentcourseid">
									<logic:equal value="true" name="admissionStatusForm"
										property="admissionStatusTO.chanceAlmntgeneral">
										<tr>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chancePref" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceCurrentcourse" /></td>
											<td align="center" class="mytabledata">General</td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceIndexmark" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.genRank" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.dateTime" /></td>
										</tr>
									</logic:equal>
									<logic:equal value="true" name="admissionStatusForm"
										property="admissionStatusTO.chanceAlmntcaste">
										<tr>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chancePref" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceCurrentcourse" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceCategory" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceIndexmark" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.casteRank" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.communityDateTime" /></td>
										</tr>
									</logic:equal>
									<logic:equal value="true" name="admissionStatusForm"
										property="admissionStatusTO.chanceAlmntCommunity">
										<tr>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chancePref" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceCurrentcourse" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceCategory" />(Syro Malankara Catholic)</td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.chanceIndexmark" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.casteRank" /></td>
											<td align="center" class="mytabledata"><bean:write
												name="admissionStatusForm" property="admissionStatusTO.communityDateTime" /></td>
										</tr>
									</logic:equal>
								</logic:equal>
							<%-- </logic:iterate> --%>
						</logic:notEmpty>
					</table>
					</td>
				</tr>
				<tr><td>
				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					bordercolor="#E0DFDB">
					<tr>
					<td align="justify" class="mytabledata">The admission secured by
					you is based on the details furnished online. The original
					documents relating to the above must be submitted before the
					Principal at the time of admission. Kindly furnish the print copy
					of the Allotment Memo (Sure / Chance) along with the original documents. If any
					discrepancies are detected at the time of admission or course
					period, your admission will be cancelled. Please read the
					prospectus and admission instructions given on the website.<br></br>
					</td>
				</tr>
				<tr>
					<td>
					<table>
						<tr>
							<td class="mytabledatafee"><u>Fees to be paid at the
							time of admission:</u></td>
							<td class="mytabledatafee">Rs.<bean:write
								name="admissionStatusForm"
								property="admissionStatusTO.chanceGeneralFee" /> + PTA (for General
							Candidates)</td>
						</tr>
						<tr>
							<td></td>
							<td class="mytabledatafee">Rs.<bean:write
								name="admissionStatusForm" property="admissionStatusTO.chanceCasteFee" /> + PTA
							(for SC/ST/OEC Candidates)</td>
						</tr>
						
				</table>
				</td>
			</tr>
		</table>
		</td>
		</tr>
		<%--azr --%>
		<tr>
			<td>
			<table cellpadding="3" width="100%" border="0">
				<tr>
					<td class="mytabledata">Name</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><nested:write
						property="applicantDetails.personalData.firstName"></nested:write>&nbsp;
					<nested:write property="applicantDetails.personalData.middleName"></nested:write>&nbsp;
					<nested:write property="applicantDetails.personalData.lastName"></nested:write></td>
					<td rowspan="6" colspan="3" align="right"><img
						src='<%=request.getRequestURL().toString().replace(
						request.getRequestURI().substring(0),
						request.getContextPath()).concat("/PhotoServlet")%>'
						height="150px" width="150px" /></td>
				</tr>
				<tr>
					<td class="mytabledata">Gender</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><logic:equal
						name="admissionStatusForm"
						property="applicantDetails.personalData.gender" value="MALE">
						<bean:message key="admissionForm.studentinfo.sex.male.text" />
					</logic:equal> <logic:equal name="admissionStatusForm"
						property="applicantDetails.personalData.gender" value="FEMALE">
						<bean:message key="admissionForm.studentinfo.sex.female.text" />
					</logic:equal></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Date of Birth</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><nested:write
						property="applicantDetails.personalData.dob"></nested:write></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Nationality</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.personalData.citizenship" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Domicile State</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.personalData.residentCategoryName" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Religion</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.personalData.religionName" /></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Caste</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.personalData.casteCategory" /></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Category</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.personalData.subregligionName" /></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="mytabledata">Bonus Mark</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm" property="applicantDetails.personalData.bonusMarks"/>(<bean:write name="admissionStatusForm" property="applicantDetails.personalData.bonusType"/>)</td>
					<td class="mytabledata" align="right">Negative Mark</td>
					<td class="mytabledata">:</td>
					<td class="mytabledata"><bean:write name="admissionStatusForm"
						property="applicantDetails.handicapmark" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<%
			String noofattempt = "";
				String total = "";
				String obtained = "";
				String regno = "";
				String board = "";
				String year = "";
				String noofattemptpg = "";
				String totalpg = "";
				String obtainedpg = "";
				String regnopg = "";
				String boardpg = "";
				String yearpg = "";
		%>
		<nested:iterate property="applicantDetails.ednQualificationList"
			id="ednQualList" indexId="count">
			<logic:equal value="Class 12" name="ednQualList" property="docName">
				<bean:define id="noofAtt" name="ednQualList" property="noOfAttempts"></bean:define>
				<bean:define id="totalm" name="ednQualList" property="totalMarks"></bean:define>
				<bean:define id="obtainedm" name="ednQualList"
					property="marksObtained"></bean:define>
				<bean:define id="previousRegNom" name="ednQualList"
					property="previousRegNo"></bean:define>
				<bean:define id="universityNamem" name="ednQualList"
					property="universityName"></bean:define>
				<bean:define id="yearPassingm" name="ednQualList"
					property="yearPassing"></bean:define>
				<%
					if (noofAtt != null) {
									noofattempt = noofAtt.toString();
								}
								if (totalm != null) {
									total = totalm.toString();
								}
								if (obtainedm != null) {
									obtained = obtainedm.toString();
								}
								if (previousRegNom != null) {
									regno = previousRegNom.toString();
								}
								if (universityNamem != null) {
									board = universityNamem.toString();
								}
								if (yearPassingm != null) {
									year = yearPassingm.toString();
								}
				%>
			</logic:equal>
			<logic:equal value="DEG" name="ednQualList" property="docName">
				<bean:define id="noofAttpg" name="ednQualList"
					property="noOfAttempts"></bean:define>
				<bean:define id="totalmpg" name="ednQualList" property="totalMarks"></bean:define>
				<bean:define id="obtainedmpg" name="ednQualList"
					property="marksObtained"></bean:define>
				<bean:define id="previousRegNompg" name="ednQualList"
					property="previousRegNo"></bean:define>
				<bean:define id="universityNamempg" name="ednQualList"
					property="universityName"></bean:define>
				<bean:define id="yearPassingmpg" name="ednQualList"
					property="yearPassing"></bean:define>
				<%
					if (noofAttpg != null) {
									noofattemptpg = noofAttpg.toString();
								}
								if (totalmpg != null) {
									totalpg = totalmpg.toString();
								}
								if (obtainedmpg != null) {
									obtainedpg = obtainedmpg.toString();
								}
								if (previousRegNompg != null) {
									regnopg = previousRegNompg.toString();
								}
								if (universityNamempg != null) {
									boardpg = universityNamempg.toString();
								}
								if (yearPassingmpg != null) {
									yearpg = yearPassingmpg.toString();
								}
				%>
			</logic:equal>
		</nested:iterate>
		<tr>
			<td>
			<table>
				<logic:equal value="1" name="admissionStatusForm"
					property="applicantDetails.programType">
					<tr>
						<td class="mytabledata">University/Board of Qualifying
						Examination</td>
						<td class="mytabledata">:</td>
						<td class="mytabledata"><%=board%></td>
					</tr>
					<tr>
						<td class="mytabledata">Year of Passing and Reg. No</td>
						<td class="mytabledata">:</td>
						<td class="mytabledata"><%=year%> , <%=regno%></td>
					</tr>
				</logic:equal>
				<logic:equal value="2" name="admissionStatusForm"
					property="applicantDetails.programType">
					<tr>
						<td class="mytabledata">University/Board of Qualifying
						Examination</td>
						<td class="mytabledata">:</td>
						<td class="mytabledata"><%=boardpg%></td>
					</tr>
					<tr>
						<td class="mytabledata">Ug Course studied</td>
						<td class="mytabledata">:</td>
						<td><bean:write name="admissionStatusForm"
							property="applicantDetails.personalData.ugcourse" /></td>
					</tr>
					<tr>
						<td class="mytabledata">Year of Passing and Reg. No</td>
						<td class="mytabledata">:</td>
						<td class="mytabledata"><%=yearpg%> , <%=regnopg%></td>
					</tr>
				</logic:equal>
			</table>
			</td>
		</tr>
		<logic:equal value="1" name="admissionStatusForm"
			property="applicantDetails.programType">
			<tr>
				<td align="center" class="title2">
				Statement of Plus Two Marks</td>
			</tr>
		</logic:equal>

		<logic:equal value="2" name="admissionStatusForm"
			property="applicantDetails.programType">
			<tr>
				<td align="center" class="title2"><br></br>
				Statement of Degree Marks</td>
			</tr>
		</logic:equal>

		<tr>
			<td>
			<table cellpadding="3" align="center" width="100%" border="1"
				style="border: 1px solid black; border-collapse: collapse">
				<tr style="font-size: small; font-weight: bold">
					<logic:equal value="2" name="admissionStatusForm"
						property="applicantDetails.programType">
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Subject
						Group</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Name
						of Subject</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Marks
						Secured/Grade Point</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Maximum
						Marks/Grade Point</td>
						<logic:equal value="true" name="admissionStatusForm"
							property="applicantDetails.isCbscc">
							<td align="center"
								style="border: 1px solid black; border-collapse: collapse">Credits</td>
						</logic:equal>
					</logic:equal>
					<logic:equal value="1" name="admissionStatusForm"
						property="applicantDetails.programType">
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Name
						of Subject</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Marks
						Secured</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse">Maximum
						Marks</td>
					</logic:equal>
				</tr>
				<logic:equal value="1" name="admissionStatusForm"
					property="applicantDetails.programType">
					<logic:iterate id="submrks"
						property="applicantDetails.pucsubjectmarkto"
						name="admissionStatusForm">
						<tr>
							<td align="left"
								style="border: 1px solid black; border-collapse: collapse"><bean:write
								name="submrks" property="subjectName" /></td>
							<td align="center"
								style="border: 1px solid black; border-collapse: collapse"><bean:write
								name="submrks" property="obtainedmark" /></td>
							<td align="center"
								style="border: 1px solid black; border-collapse: collapse"><bean:write
								name="submrks" property="maxmark" /></td>
						</tr>
					</logic:iterate>
				</logic:equal>

				<logic:equal value="2" name="admissionStatusForm"
					property="applicantDetails.programType">
					<logic:iterate id="map" property="applicantDetails.degMap"
						name="admissionStatusForm">
						<logic:notEmpty property="value" name="map">
							<logic:iterate id="submrks" property="value" name="map">
								<tr>
									<td align="left"
										style="border: 1px solid black; border-collapse: collapse"><bean:write
										name="submrks" property="groupname" /></td>
									<td align="left"
										style="border: 1px solid black; border-collapse: collapse"><bean:write
										name="submrks" property="subjectName" /></td>
									<td align="center"
										style="border: 1px solid black; border-collapse: collapse"><bean:write
										name="submrks" property="obtainedmark" /></td>
									<td align="center"
										style="border: 1px solid black; border-collapse: collapse"><bean:write
										name="submrks" property="maxmark" /></td>
									<logic:equal value="true" name="admissionStatusForm"
										property="applicantDetails.isCbscc">
										<td align="center"
											style="border: 1px solid black; border-collapse: collapse"><bean:write
											name="submrks" property="credit" /></td>
									</logic:equal>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
					</logic:iterate>
				</logic:equal>
				<tr>
					<logic:equal value="1" name="admissionStatusForm"
						property="applicantDetails.programType">
						<td style="font-size: small; font-weight: bold" align="right"
							style="border:1px solid black; border-collapse: collapse ">Total
						Marks</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse"><%=obtained%></td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse"><%=total%></td>
					</logic:equal>
					<logic:equal value="2" name="admissionStatusForm"
						property="applicantDetails.programType">
						<td colspan="2" style="font-size: small; font-weight: bold"
							align="right"
							style="border:1px solid black; border-collapse: collapse ">Total
						Marks/CGPA(S)/CCPA(S)</td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse"><%=obtainedpg%></td>
						<td align="center"
							style="border: 1px solid black; border-collapse: collapse"><%=totalpg%></td>
						<logic:equal value="true" name="admissionStatusForm"
							property="applicantDetails.isCbscc">
							<td align="center"
								style="border: 1px solid black; border-collapse: collapse"><bean:write
								name="admissionStatusForm"
								property="applicantDetails.totalCredit" /></td>
						</logic:equal>
					</logic:equal>
				</tr>

			</table>
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td align="right"><small>(P.T.O)</small></td>
		</tr>
	</table>
	<p style="page-break-before: always"></p>
	<table width="100%">
		<tr>
			<td align="center" class="title2"><u>COMMUNICATION DETAILS</u></td>
		</tr>
	</table>
	<table cellpadding="3" width="100%" border="0">
		<tr>
			<td class="mytabledata">Name of Father</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.titleOfFather" />. <nested:write
				property="applicantDetails.personalData.fatherName"></nested:write></td>
			<td class="mytabledata">Occupation</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><bean:write name="admissionStatusForm"
				property="applicantDetails.personalData.fatherOccupation" /></td>
		</tr>

		<tr>
			<td class="mytabledata">Name of Mother</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.titleOfMother" />. <nested:write
				property="applicantDetails.personalData.motherName"></nested:write></td>
			<td class="mytabledata">Occupation</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><bean:write name="admissionStatusForm"
				property="applicantDetails.personalData.motherOccupation" /></td>
		</tr>
		<tr>
			<td class="mytabledata">Permanent Address</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.permanentAddressLine1"></nested:write>
			</td>
			<td class="mytabledata">Present Address</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.currentAddressLine1"></nested:write></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.permanentAddressLine2"></nested:write></td>
			<td></td>
			<td></td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.currentAddressLine2"></nested:write></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.permanentCityName"></nested:write>,<bean:write
				name="admissionStatusForm"
				property="applicantDetails.personalData.permanentStateName" />, <bean:write
				name="admissionStatusForm"
				property="applicantDetails.personalData.permanentCountryName" />, <nested:write
				property="applicantDetails.personalData.permanentAddressZipCode"></nested:write></td>
			<td></td>
			<td></td>
			<td class="mytabledata"><bean:write name="admissionStatusForm"
				property="applicantDetails.personalData.currentStateName" />, <bean:write
				name="admissionStatusForm"
				property="applicantDetails.personalData.currentCountryName" />, <nested:write
				property="applicantDetails.personalData.currentAddressZipCode"></nested:write></td>
		</tr>


		<tr>
			<td class="mytabledata">Phone-Land</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><bean:write name="admissionStatusForm"
				property="applicantDetails.personalData.landlineNo" /></td>
			<td class="mytabledata">Mobile</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><bean:write name="admissionStatusForm"
				property="applicantDetails.personalData.mobileNo" /></td>
		</tr>


		<tr>
			<td class="mytabledata">E - Mail</td>
			<td class="mytabledata">:</td>
			<td class="mytabledata"><nested:write
				property="applicantDetails.personalData.email"></nested:write></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="3" cellspacing="0"
		bordercolor="#E0DFDB">
		<tr>
			<td align="center" class="title2"><font
				style="text-decoration: underline">All admissions are subject
			to verification of Original documents</font></td>
		</tr>

		<tr>
			<td align="center" class="title2"><font
				style="text-decoration: underline">LIST OF DOCUMENTS TO BE
			PRODUCED AT THE TIME OF INTERVIEW</font></td>
		</tr>

		<logic:equal value="1" name="admissionStatusForm"
			property="applicantDetails.programType">
			<!--<tr>
	  <td align="center" class="title2"><font style="text-decoration: underline">UNDER GRADUATE ADMISSION 2015 -16</font></td>
	</tr>
	
	-->
			<tr>
				<td>
				<table width="100%">
					<tr>
						<td>(a)</td>
						<td class="mytabledata">A printed copy of the downloaded Admission Memo.</td>
					</tr>
					<tr>
						<td>(b)</td>
						<td class="mytabledata">Original T.C, Course &amp; Conduct
						Printed copy of the downloaded Application form, two Bio-
data forms (original and duplicate), Anti-ragging form and
PTA form.</td>
					</tr>
					<tr>
						<td>(c)</td>
						<td class="mytabledata">Original T.C, Course &amp; Conduct certificate from the
institution last attended.</td>
					</tr>
					<tr>
						<td>(d)</td>
						<td class="mytabledata">Printout of the completed online application generated by
the candidate.</td>
					</tr>
					<tr>
						<td>(e)</td>
						<td class="mytabledata">One self-attested copy of SSLC / Class X Certificate.</td>
					</tr>
					<tr>
						<td>(f)</td>
						<td class="mytabledata">Original Mark list of the qualifying examination along with
one self-attested copy of the same.</td>
					</tr>
					<tr>
						<td>(g)</td>
						<td class="mytabledata">Pass Certificate, if applicable.</td>
					</tr>
					<tr>
						<td>(h)</td>
						<td class="mytabledata"><b><u>ELIGIBILITY CERTIFICATE</u>
</b> <b>in the case of candidates other than
Kerala HSE/VHSE/CBSE/ISC.</b>.</td>
			</tr>
					<tr>
						<td>(i)</td>
						<td class="mytabledata">Migration Certificate, if applicable.</td>
					</tr>
					<tr>
						<td>(j)</td>
						<td class="mytabledata"> Community certificate from the Parish priest and
Catechism certificate for Malankara Catholics.</td>
					</tr>
					<tr>
						<td>(k)</td>
						<td class="mytabledata">Caste / Community / Income Certificates for Reservation /
Fee concession candidates, from the concerned authority.
Certificates more than a year old shall be considered
invalid.</td>
					</tr>
					<tr>
						<td>(l)</td>
						<td class="mytabledata">NCC / NSS / SPC / Scouts &amp; Guides certificate, if
applicable.</td>
					</tr>
					<tr>
						<td>(m)</td>
						<td class="mytabledata">SC / ST / O.E.C. candidates should produce recent (with
one year validity) Community / Caste certificate from
Tahsildar / Village Officer and O.B.C. candidates should
produce community and income certificates from the
Village Officer.</td>
					</tr>
					<tr>
						<td>(n)</td>
						<td class="mytabledata">Children of Ex-Service personnel / Jawan should produce
the concerned certificate</td>
					</tr>
					<tr>
						<td>(o)</td>
						<td class="mytabledata">PWD (Person with disability) candidates should produce
the medical certificate with 5 year validity</td>
					</tr>
					
	<tr>
				<td colspan="2" align="center">
					<div>
						<span style="color: red;">
							<b>
								The candidate should be accompanied by at least one of the parents at the time of the interview. 
								This is compulsory. In the case of any extraordinary situation where neither of the parents can be present, 
								it should be intimated to the principal beforehand either through email or in person.
							</b>							
						</span>
					</div>
				</td>
			</tr>
				</table>
				</td>
			</tr>
		</logic:equal>

		<logic:equal value="2" name="admissionStatusForm"
			property="applicantDetails.programType">
			<!--<tr>
	  <td align="center" class="title2"><font style="text-decoration: underline">POST GRADUATE ADMISSION 2015 -16</font></td>
	</tr>
	
	-->
			<tr>
				<td>
				<table width="100%">
					<tr>
						<td>(a)</td>
						<td class="mytabledata">Allotment Memo</td>
					</tr>
					<tr>
						<td>(b)</td>
						<td class="mytabledata">Original T.C, Course &amp; Conduct
						certificate from the institution last attended.</td>
					</tr>
					<tr>
						<td>(c)</td>
						<td class="mytabledata">Printout of the completed online
						application generated by the candidate.</td>
					</tr>
					<tr>
						<td>(d)</td>
						<td class="mytabledata">Fee receipt (College Copy) towards
						Registration fees.</td>
					</tr>
					<tr>
						<td>(e)</td>
						<td class="mytabledata">SSLC / Class X Certificate.</td>
					</tr>
					<tr>
						<td>(f)</td>
						<td class="mytabledata">Original Mark list of the qualifying
						examination (all semesters) and Original Consolidated mark list.</td>
					</tr>
					<tr>
						<td>(g)</td>
						<td class="mytabledata">Original Provisional / Degree
						certificates.</td>
					</tr>
					<tr>
						<td>(h)</td>
						<td class="mytabledata"><span style="color: red;"> <b><u>Eligibility
						Certificate</u></b> </span> <b>from University of Kerala in case of
						candidates who have passed the Qualifying Examination from other
						Universities.</b></td>
					</tr>
					<tr>
						<td>(i)</td>
						<td class="mytabledata">Migration Certificate, if applicable.</td>
					</tr>
					<tr>
						<td>(j)</td>
						<td class="mytabledata">3 recent passport size photographs.</td>
					</tr>
					<tr>
						<td>(k)</td>
						<td class="mytabledata">Community certificate from the Parish
						priest and Catechism certificate for Malankara Catholics.</td>
					</tr>
					<tr>
						<td>(l)</td>
						<td class="mytabledata">Caste / Community / Income
						Certificates for Reservation / Fee concession candidates.
						Certificates more than a year old shall be considered invalid.</td>
					</tr>
					<tr>
						<td>(m)</td>
						<td class="mytabledata">NCC / NSS certificate, if applicable.</td>
					</tr>
					<tr>
						<td>(n)</td>
						<td class="mytabledata">SC / ST CANDIDATES - Community /
						Caste and Income Certificate from Tahsildar / Village Officer.</td>
					</tr>
					<tr>
						<td>(o)</td>
						<td class="mytabledata">PWD (Person with disability)
						candidates should produce the medical certificate with 5 year
						validity.</td>
					</tr>
					<tr>
						<td><span style="color: red;">(p)</span></td>
						<td class="mytabledata"><span style="color: red;">
						Those candidates who have more than one memo (Sure / Chance)
						should produce all the memos (sure &amp; chance), if he/she wants
						the claim for the concerned subjects. </span></td>
					</tr>
					<tr>
						<td>*</td>
						<td>Merit list candidates should bring their original
						certificates compulsorily at the time of admission other wise
						their admission will be rejected</td>
					</tr>
					<tr>
						<td>*</td>
						<td>Candidates coming for interview should carefully read the
						Interview Schedule and instructions given in the website.</td>
					</tr>
						<tr>
				<td colspan="2" align="center">
					<div>
						<span style="color: red;">
							<b>
								The candidate should be accompanied by at least one of the parents at the time of the interview. 
								This is compulsory. In the case of any extraordinary situation where neither of the parents can be present, 
								it should be intimated to the principal beforehand either through email or in person.
							</b>							
						</span>
					</div>
				</td>
			</tr>
		</table>
	 </td>
	</tr>
				</table>
				</td>
			</tr>
		</logic:equal>
	</table>
	<table width="100%">
		
	</table>
</html:form>
<script type="text/javascript">
	window.print();
</script>
