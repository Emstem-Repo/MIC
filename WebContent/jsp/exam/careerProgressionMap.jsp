<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>

<script type="text/javascript">

	function resetMessages() {
		document.getElementById("programType").selectedIndex = 0;
		document.getElementById("program").selectedIndex = 0;
		document.getElementById("course").selectedIndex = 0;
		document.getElementById("subjectName").value = "";
		resetErrMsgs();
	}
	function submitAddMorePreferences() {
		document.getElementById("method").value=method;
		document.admissionStatusForm.submit();
		resetErrMsgs();
	}
</script> 	
<html:form action="/careerProgression" method="post">
<html:hidden property="method" styleId="method"	value="saveCareerData" />

	
	
	
	<table width="100%" border="0">
	<tr>
    <td><span class="Bredcrumbs">Admin
    <span class="Bredcrumbs">&gt;&gt; career progression map</span></span></td>
  </tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> CAREER PROGRESSION MAP</strong></td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">&nbsp;
					<div align="right" style="color:red"> <span class='MandatoryMark'>* Mandatory fields</span></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"><html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
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
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
									<td width="20%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message
										key="knowledgepro.admin.program.Type" /></div>
									</td>
									<td width="27%" height="25" class="row-even">
									<html:select property="programType" styleClass="comboLarge" styleId="programType">
										<html:option value="">- Select -</html:option>
										<html:option value="ug">UG</html:option>
										<html:option value="pg">PG</html:option>
										
									</html:select> <span class="star"></span></td>
									<td width="29%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Batch</div>
									</td>
									<td width="24%" class="row-even">
									<html:select property="batch" styleClass="comboLarge"
										styleId="batch" onchange="getCourses(this.value)">
										<html:option value="">- Select -</html:option>
										<cms:renderYear></cms:renderYear>
									</html:select></td>
								</tr>
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Type</div>
									</td>
									<td height="25" class="row-even"><html:select
										property="course" styleClass="comboLarge" styleId="course">
										<html:option value="">- Select -</html:option>
										<html:option value="Higher Education">Higher Education</html:option>
										<html:option value="Placement">Placement</html:option>
									</html:select></td>
									<td class="row-even"></td>
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
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
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
							<tr><td width="20%" height="25" class="row-odd" colspan="4">Higher Education<td></tr>
							<logic:iterate id="to" name="careerProgressionForm"
													property="higherEducationList" indexId="count">
								<tr>
									<td width="20%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Higher Education Programme</div>
									</td>
									<td width="27%" height="25" class="row-even">
									<html:text property="higherEduProgram" styleId="higherEduProgram" name="to" maxlength="45"/>
									<td width="29%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;College/ Institute</div>
									</td>
									<td width="24%" class="row-even">
									<html:text property="college" styleId="college" name="to" maxlength="45"/>
									</td>
								</tr>
								
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Year of Admission</div>
									</td>
									<td height="25" class="row-even">
										<html:text property="yearOfAdmission" styleId="yearOfAdmission" name="to" maxlength="45"/>
									</td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Attachment</div>
									</td>
									<td class="row-even">
									<html:file property="document" styleId="document" name="to" maxlength="45"/> </td>
								</tr>
								</logic:iterate>
								<tr>
									<td align="center" class="bodytext" colspan="5" height="25"
													class="row-even"><a href="url"
													style="text-decoration: none; vertical-align: middle; color: #007700; font-weight: bold"
													onclick="submitAddMorePreferences('submitAddMorePreferences'); return false;">
														Click here &nbsp;<img
														src="images/admission/images/12673199831854453770medical cross button.svg.med.png"
														width="20px" ; height="20px"
														; style="vertical-align: middle;">
												</a> to Add More Choice</td>
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
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
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
							<tr><td width="20%" height="25" class="row-odd" colspan="4">Placement<td></tr>
							<logic:iterate id="to" name="careerProgressionForm"
													property="placementList" indexId="count">
								<tr>
									<td width="20%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Placement Firm</div>
									</td>
									<td width="27%" height="25" class="row-even">
										<html:text property="placementFirm" styleId="placementFirm" name="to" maxlength="45"/>
									</td>
									<td width="29%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Annual Salary</div>
									</td>
									<td width="24%" class="row-even">
										<html:text property="annualSalary" styleId="annualSalary" name="to" maxlength="45"/>
									</td>
								</tr>
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;Year of Placement </div>
									</td>
									<td height="25" class="row-even">
										<html:text property="yearOfPlacemment" styleId="yearOfPlacemment" name="to" maxlength="45"/>
									</td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Attachment</div>
									</td>
									<td class="row-even"><html:text property="document" styleId="document" name="to" maxlength="45"/> </td>
								</tr>
								</logic:iterate>
								<tr>
									<td align="center" class="bodytext" colspan="5" height="25"
													class="row-even"><a href="url"
													style="text-decoration: none; vertical-align: middle; color: #007700; font-weight: bold"
													onclick="submitAddMorePreferences('submitAddMorePreferences'); return false;">
														Click here &nbsp;<img
														src="images/admission/images/12673199831854453770medical cross button.svg.med.png"
														width="20px" ; height="20px"
														; style="vertical-align: middle;">
												</a> to Add More Choice</td>
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
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						<td width="45%" height="35">
							<div align="center"><input name="button2" type="submit"
								class="formbutton" value="Submit" />
								<html:button  property="" styleClass="formbutton" value="Reset"
										onclick="resetMessages();"></html:button>
										</div>
							</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				<tr>
					<td height="34" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news"></td>
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
	var programType = document.getElementById("prgTypeId").value;
	if (programType.length != 0) {
		document.getElementById("programType").value = programType;
	}
</script>
