<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>

<script type="text/javascript"><!--

function resetFormFields(){
	resetErrMsgs();
	document.getElementById("submitbutton").value="Submit";
	document.getElementById("year").value="";
	document.getElementById("examNameId").value="";
	document.getElementById("course").value="";
	document.getElementById("scheme").value="";
	document.getElementById("startNo").value=null;
	document.getElementById("currentFalseNo").value=null;
	document.getElementById("examId").checked=true;
	document.getElementById("method").value="save";
}

function getExamsByExamTypeAndYear() {
	if(document.getElementById("examId").checked==true)
	var examType=document.getElementById("examId").value;
	else if(document.getElementById("sup").checked==true)
		var examType=document.getElementById("sup").value;
	else if(document.getElementById("int").checked==true)
		var examType=document.getElementById("int").value;
	var year=document.getElementById("year").value;
	getExamNameByExamTypeAndYear("examMap", examType,year, "examNameId", updateExamName);
}
function updateExamName(req) {
	updateOptionsFromMap(req, "examNameId", "- Select -");
	updateCurrentExam(req, "examNameId");
	getCourse(document.getElementById("examNameId").value);
}
function populateOptionForSup(isSup){
	var destination = document.getElementById("subjectType");
	for (x1 = destination.options.length - 1; x1 > 0; x1--) {
		destination.options[x1] = null;
	}
	if(isSup){
		destination.options[0] = new Option("Select", "");
		destination.options[1] = new Option("Theory", 1);
		destination.options[2] = new Option("Practical", 0);
	}
	else{
		destination.options[0] = new Option("Select", "");
		destination.options[1] = new Option("Theory", 1);
		destination.options[2] = new Option("Practical", 0);
	}
}
function getCourse(examName) {
	getCourseByExamName("schemeMap", examName, "course", updateToCourse);

}
function updateToCourse(req) {
	updateOptionsFromMap(req, "course", "- Select -");

}
<%--
function getPrograms(programTypeId) {
	getProgramsByType("programMap",programTypeId,"program",updatePrograms);
}--%>

function updatePrograms(req) {
	updateOptionsFromMap(req,"program","- Select -");
}

function getCourses(programId) {
	getCoursesByProgram("coursesMap",programId,"course",updateCourses);
	
}

function updateCourses(req) {
	updateOptionsFromMap(req,"course","- Select -");
}

function getSemisters(year) {
	year = document.getElementById("academicYear").value;

	var courseId = document.getElementById("course").value;
	getSemistersOnYearAndCourse("semistersMap",courseId,"semister",year,updateSemisters);
        
}

function updateSemisters(req){
	updateOptionsFromMap(req,"semister","- Select -");
}

function resetCoursesChilds() {
	resetAcademicYear("academicYear");
	resetOption("semister");

}
function getScheme(courseId) {
	examId = document.getElementById("examNameId").value;
	getSchemeNoByExamIdCourseId("schemeMap", examId, courseId, "scheme",
			updateToScheme);

}
function updateToScheme(req) {
	updateOptionsFromMap(req, "scheme", "- Select -");

}
function getSectionByScheme(schemeId) {
	//document.getElementById("sCodeName_1").checked = true;
	var ScodeName="sCode";
	getSCodeName(ScodeName);
	var courseId = document.getElementById("course").value;
	var examId =  document.getElementById("examNameId").value;
	getSectionByExamIdCourseIdSchemeId("sectionMap", courseId, schemeId,examId,
			"section", updateSection);
}
function updateSection(req) {
	updateOptionsFromMap(req, "section", "--Select--");
}
function getSCodeName(sCName) {

	var courseId = document.getElementById("course").value;
	//getAnswerScript("", "", courseId, schemeId);
	//getEvaluatorType("", "", courseId, schemeId);
	var examId = document.getElementById("examNameId").value;
	var schemeId = document.getElementById("scheme").value;
	getSubjectsCodeNameByCourseSchemeExamId("schemeMap", sCName, courseId, "subject",
			updateToSubject, schemeId,examId);
}
function updateToSubject(req) {
	var subco ="subco";
	updateOptionsFromMap(req, "subject", "- Select -");
}
function getSubjectType(subjectNo) {
	if (subjectNo != "") {
		var args = "method=getSubjectsTypeBySubjectId&subjectId="
				+ subjectNo;
		var url = "AjaxRequest.do";
		requestOperation(url, args, updateClass);
	}
}
function updateClass(req) {
	updateSubjectsTypeBySubjectIdForMarks(req, "subjectType");
	var subjectId = document.getElementById("subject").value;
	var subjectType = document.getElementById("subjectType").value;
    var courseId = document.getElementById("course").value;
	var schemeId = document.getElementById("scheme").value;
	getAnswerScript(subjectId, subjectType, courseId, schemeId);
	getEvaluatorType(subjectId, subjectType, courseId, schemeId);
}
function editEntry(id,year){
	document.location.href = "falsenumSINO.do?method=editDetails&falseNoId=" + id + "&year=" +year;
}
</script>
<html:form action="/falsenumSINO">	
	<html:hidden property="formName" value="falsenumSINOForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="falseNoId" styleId="falseNoId"/>
	<c:choose>
		<c:when test="${editOption != null && editOption == 'edit'}">
			<html:hidden property="method" styleId="method" value="updateDetails" />
		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method" value="save"/>
		</c:otherwise>
	</c:choose>
	<table width="100%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin"/>
			<span class="Bredcrumbs">&gt;&gt;
			False No Entry
			 &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> False No Entry</strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="41" valign="top" background="images/Tright_03_03.gif"></td>

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

									<td height="25" colspan="8" class="row-even">
									<div align="Center">
									<%  boolean disableInternal=false; 
										boolean disableRegular=false;
									%>
									<c:if test="${falsenumSINOForm.regular==false && falsenumSINOForm.internal==true}">
										<%   disableInternal=false; 
											 disableRegular=true;
										%>
									</c:if>
									<c:if test="${falsenumSINOForm.regular==true && falsenumSINOForm.internal==false}">
										<%   disableInternal=true; 
											 disableRegular=false;
										%>
									</c:if>
									<html:radio property="examType"
										styleId="examId" value="Regular"
										onclick="getExamsByExamTypeAndYear(), populateOptionForSup(false)" disabled="<%=disableRegular %>"></html:radio>
									Regular&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<html:radio property="examType" value="Supplementary"
										styleId="sup"
										onclick="getExamsByExamTypeAndYear(), populateOptionForSup(true)" disabled="<%=disableRegular %>"></html:radio>
									Supplementary&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<html:radio property="examType" value="Internal" styleId="int"
										onclick="getExamsByExamTypeAndYear(), populateOptionForSup(false)" disabled="<%=disableInternal %>"></html:radio>
									Internal
									
									</div>
									</td>

								</tr>
								<tr>
								     <td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span> <bean:message
										key="knowledgepro.admin.year" /> :</div></td>
									<td width="16%" class="row-even" valign="top">
									<input
										type="hidden" id="tempyear" name="academicYear"
										value="<bean:write name="falsenumSINOForm" property="year"/>" />
									<html:select 
										property="year" styleId="year"
										styleClass="combo" onchange="getExamsByExamTypeAndYear()">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<logic:notEmpty property="batchMap" name="falsenumSINOForm">
		   								<html:optionsCollection property="batchMap" label="value" value="key"/>
		   								</logic:notEmpty>
									</html:select></td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span> Exam
									Name :</div>
									</td>

									<td class="row-even"><html:select property="examId"
										styleClass="combo" styleId="examNameId"
										name="falsenumSINOForm" onchange="getCourse(this.value)"
										style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="examNameList">
											<html:optionsCollection property="examNameList"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
								</tr>
								<tr>


									<td width="23%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Course:</div>
									</td>
									<td width="23%" height="25" class="row-even"><html:select
										property="courseId" styleClass="body" styleId="course"
										onchange="getScheme(this.value)">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="courseMap">
											<html:optionsCollection property="courseMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>

									<td width="28%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>
									Scheme:</div>
									</td>
								<!--  	<td width="26%" class="row-even"><html:select
										property="schemeNo" styleClass="body" styleId="scheme"
										onchange="getSubject(this.value), getSectionByScheme(this.value)">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="schemeMap">
											<html:optionsCollection property="schemeMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td> -->
									<td width="26%" class="row-even"><html:select
										property="semister" styleClass="body" styleId="scheme"> 
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="schemeMap">
											<html:optionsCollection property="schemeMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
								</tr>
								<%-- <tr>
								<td height="25" colspan="2" class="row-even">
									<div align="center">
									<html:radio property="displaySubType" styleId="sCodeName_1" value="sCode" onchange="getSCodeName(this.value)">Subject Code</html:radio>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 <html:radio property="displaySubType" styleId="sCodeName_2" value="sName" onchange="getSCodeName(this.value)">Subject Name</html:radio>
									</div>
									</td>
								</tr>--%>
								<tr>
												<td  height="25" class="row-odd">
							<div align="right"><span class="Mandatory">*</span>False No</div>
							</td>
							<td  height="25" class="row-even">
							<html:text
								property="startNo" styleId="startNo" styleClass="TextBox"
								size="20" maxlength="30" /><span class="star"></span></td>
								<td height="25" class="row-odd">
									<div align="right">Prefix:</div>
									</td>

									<td height="25" class="row-even">
									<html:text
									property="prefix" styleId="prefix" styleClass="TextBox"
									size="20" maxlength="30" />
									
									</td>
						
								
								
									<%-- <td height="25" class="row-odd">
									<div align="right">Subject Type:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="subjectType" styleId="subjectType" onchange="displayEvaluAndAnswerScript(this.value)">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="subjectTypeMap">
											<html:optionsCollection property="subjectTypeMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>--%>

								</tr>
								<tr>
								<td  height="25" class="row-odd">
								<c:if test="${editOption != null && editOption == 'edit'}">
								<div align="right">Current False No</div>
								</c:if>
								</td>
								<td  height="25" class="row-even">
								<c:if test="${editOption != null && editOption == 'edit'}">
								<html:text
								property="currentFalseNo" styleId="currentFalseNo" styleClass="TextBox"
								size="20" maxlength="30" /></c:if>
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
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="right">
								<c:choose>
										<c:when test="${editOption == 'edit'}">
											<input type="submit" class="formbutton" value="Update" id="submitbutton"/>
										</c:when>
										<c:otherwise>
											<input type="submit" class="formbutton" value="Submit" id="submitbutton"/>
										</c:otherwise>
								</c:choose>
								</div>
							</td>
							<td width="2%"></td>
							<td width="53%">
							<html:button property=""
							styleClass="formbutton" value="Reset"
							onclick="resetFormFields()"></html:button>
							</td>
						</tr>
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>	
				<tr>
							<td height="25" colspan="6">
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
											<td width="10%" height="25" class="row-odd">
											<div align="center"><bean:message key="knowledgepro.slno" /></div>
											</td>
											<td width="10%" height="25" class="row-odd">
											<div align="center">Year</div>
											</td>
											<td width="10%" height="25" class="row-odd">
											<div align="center">Exam Name</div>
											</td>
											<td width="30%" height="25" class="row-odd">
											<div align="center">Course</div>
											</td>
											<td width="10%" height="25" class="row-odd">
											<div align="center">Semester</div>
											</td>
											<td width="10%" height="25" class="row-odd">
											<div align="center">Prefix</div>
											</td>
											<td width="10%" class="row-odd">
											<div align="center">Start No</div>
											</td>
											<td width="20%" class="row-odd">
											<div align="center">Current No</div>
											</td>
											<td width="15%" height="25" class="row-odd">
													<div align="center">
														<bean:message key="knowledgepro.edit" />
													</div>
											</td>
										</tr>
										
										
										
										
										
										
										
										
										
											<logic:notEmpty name="falsenumSINOForm" property="toList">
												<logic:iterate id="list" name="falsenumSINOForm" property="toList" indexId="count">
														<tr class="row-even">
														<td height="25"><div align="center"><c:out value="${count+1}" /></div></td>
														<td align="center"><bean:write name="list"	property="academicYear" /></td>	
														<td align="center"><bean:write name="list"	property="examName" /></td>	
														<td align="center"><bean:write name="list"	property="courseName" /></td>
														<td align="center"><bean:write name="list"	property="semister" /></td>
														<td align="center"><bean:write name="list"	property="prefix" /></td>
														<td align="center"><bean:write name="list"	property="startNo" /></td>
														<td align="center"><bean:write name="list"	property="currentNo" /></td>
														<td height="25" align="center">
															<div align="center">
																<img src="images/edit_icon.gif"
																	 width="16" 
																	 height="18"
		        	        			 			 				 style="cursor: pointer;"
				 													 onclick="editEntry('<bean:write name="list" property="id"/>',
		    	           												                '<bean:write name="list" property="academicYear"/>')">
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
									<td height="5"><img src="images/04.gif" width="5"
										height="5"></td>
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
