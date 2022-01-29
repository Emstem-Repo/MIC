<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<head>
<title>:: CMS ::</title>
<SCRIPT>
	function cancelAction() {
		document.location.href = "LoginAction.do?method=LoginAction";
	}

	function getExamName(examType) {
		getExamNameByExamType("examMap", examType, "examNameId", updateExamName);

	}
	function updateExamName(req) {
		updateOptionsFromMap(req, "examNameId", "- Select -");
		updateCurrentExam(req, "examNameId");
		getCourse(document.getElementById("examNameId").value);
	}
	function getCourse(examName) {
		getCourseByExamName("schemeMap", examName, "course", updateToCourse);

	}
	function updateToCourse(req) {
		updateOptionsFromMap(req, "course", "- Select -");

	}

	function getScheme(courseId) {
		examId = document.getElementById("examNameId").value;
		getSchemeNoByExamIdCourseId("schemeMap", examId, courseId, "scheme",
				updateToScheme);

	}
	function updateToScheme(req) {
		updateOptionsFromMap(req, "scheme", "- Select -");

	}
	function getSubject(schemeId) {
		var courseId = document.getElementById("course").value;
		getAnswerScript("", "", courseId, schemeId);
		getEvaluatorType("", "", courseId, schemeId);
		var examId = document.getElementById("examNameId").value;
		getSubjectsByCourseSchemeExamId("schemeMap", courseId, "subject",
				updateToSubject, schemeId,examId);
	}

	function updateToSubject(req) {
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
	}

	function getEvaluatorType(subjectId, subjectType, courseId, schemeId) {
		examName = document.getElementById("examNameId").value;
	
		var args = "method=getEvaluatorTypeMap&subjectId=" + subjectId
				+ "&subjectType=" + subjectType + "&courseId=" + courseId
				+ "&schemeId=" + schemeId+"&examName="+examName;
		var url = "AjaxRequest.do";

		requestOperation(url, args, updateEvaluator);

	}
	function getAnswerScript(subjectId, subjectType, courseId, schemeId) {
		examName = document.getElementById("examNameId").value;
		var args = "method=getAnswerScriptTypeMap&subjectId=" + subjectId
				+ "&subjectType=" + subjectType + "&courseId=" + courseId
				+ "&schemeId=" + schemeId+"&examName="+examName;
		var url = "AjaxRequest.do";
		requestOperation(url, args, updateAnswerScript);

	}
	function updateAnswerScript(req) {
		var examType=0;
		if(document.getElementById("int").checked){
			examType=1;
		}
		updateOptionsFromMap2(req, "answerScriptType", "- Select -","ast","answerScriptType",examType);
	}
	function updateEvaluator(req) {
		var examType=0;
		if(document.getElementById("int").checked){
			examType=1;
		}
		updateOptionsFromMap2(req, "evaluatorType", "- Select -","et","evaluatorType",examType);
		
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
	function getSectionByScheme(schemeId) {
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
	function displayEvaluAndAnswerScript(subjectType){
		var subjectId = document.getElementById("subject").value;
		var courseId = document.getElementById("course").value;
		var schemeId = document.getElementById("scheme").value;
		getAnswerScript(subjectId, subjectType, courseId, schemeId);
		getEvaluatorType(subjectId, subjectType, courseId, schemeId);
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

	function getSCodeName(sCName) {

		var courseId = document.getElementById("course").value;
		var examId = document.getElementById("examNameId").value;
		var schemeId = document.getElementById("scheme").value;
		getSubjectsCodeNameByCourseSchemeExamId("schemeMap", sCName, courseId, "subject", updateToSubject, schemeId,examId);
	}
	function updateToSubject(req) {
		var subco ="subco";
		updateOptionsFromMap(req, "subject", "- Select -");
	}
	function getTeacher() {

		var year=document.getElementById("year").value;
		var subjectId = document.getElementById("subject").value;
		getTeacherBysubId("teachersMap", year, subjectId, "teachers", updateToTeachers);
	}
	function updateToTeachers(req) {
		updateOptionsFromMap(req, "teachers", "- Select -");
	}
	function barcodeAlert() {
		  alert("CONECT YOUR BARCODE READER AND SCAN CODES");
		}
</SCRIPT>

</head>


<html:form action="/falsenumSINO" method="POST" enctype="multipart/form-data">
	<html:hidden property="method" styleId="method" value="initFalseBoxSecondPage" />
	<html:hidden property="formName" value="falsenumSINOForm" styleId="formName" />
	<html:hidden property="pageType" value="2" styleId="pageType" />
	<html:hidden property="displaySubType" styleId="sCodeName_2" value="sName" />
									
	<table width="99%" border="0">

		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam
			&gt;&gt; Exam paper boxing&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Exam papers Boxing</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9" height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					<div align="right" class="mandatoryfield">*Mandatory fields</div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
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
										type="hidden" id="tempyear" name="appliedYear"
										value="<bean:write name="falsenumSINOForm" property="year"/>" />
									<html:select 
										property="year" styleId="year"
										styleClass="combo" onchange="getExamsByExamTypeAndYear()">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:renderAcademicYear></cms:renderAcademicYear>
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
									<td width="26%" class="row-even"><html:select
										property="schemeNo" styleClass="body" styleId="scheme"
										onchange="getSectionByScheme(this.value)" > 
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
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Subject
									:</div>
									</td>
									<td height="25" class="row-even"><html:select
										property="subjectId" styleClass="body" styleId="subject"
										onchange="getSubjectType(this.value),getTeacher()">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="subjectCodeNameMap">
											<html:optionsCollection property="subjectCodeNameMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									 <td height="25" class="row-odd">
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
									</html:select></td>

								</tr>
							 	<tr >
									<td height="25" class="row-odd">
									<div align="right" ><span class="Mandatory">*</span>Teacher</div>
									</td>
									<td height="25" class="row-even">
									<html:select name="falsenumSINOForm" styleId="teachers" property="teachers"  >
									<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
                    						<html:optionsCollection name="falsenumSINOForm" property="teachersMap" label="value" value="key"/>
                  					</html:select></td>

									<td height="25" class="row-odd">
									<div align="right" ><span class="Mandatory">*</span>Box No</div>
									</td>

									<td height="25" class="row-even" colspan="1"><html:text property="boxNo" styleId="boxNo" name="falsenumSINOForm"></html:text></td>

								</tr>
								<%-- <tr style="display: none">
									<td class="row-odd">
									
									</td>
									<td class="row-even"></td>
									<td width="22%" class="row-odd">
									<div align="right">&nbsp;<bean:message
										key="admissionForm.studentinfo.castcatg.label" /></div>
									</td>
									<td width="34%" class="row-even"><html:select
										property="section" styleClass="combo" styleId="section">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm" property="sectionMap">
										<html:optionsCollection name="falsenumSINOForm" property="sectionMap" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td><tr> --%>
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
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="49%" height="35" align="right"><input
								name="Submit7" type="submit" class="formbutton"  onclick="barcodeAlert()" 	 value="Next" /></td>

							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left"><input
								type="button" class="formbutton" value="Cancel"
								onclick="cancelAction()" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				
				<logic:notEmpty name="falsenumSINOForm" property="falseBoxToList">
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
								<tr align="center" style="font-weight: bolder;">
									<td td height="25" class="row-odd">Sl.No</td>
									<td td height="25" class="row-even">Course</td>
									<td td height="25" class="row-odd">Subject</td>
									<td td height="25" class="row-even">Box.No</td>
									<td td height="25" class="row-even">sem</td>
									<td td height="25" class="row-odd">Academic Year</td>
									<td td height="25" class="row-odd">Exam Name</td>
									<td td height="25" class="row-even">Edit</td>
									<td td height="25" class="row-odd">Delete</td>
								</tr>
								<%int i=1; %>
								<logic:iterate id="box" name="falsenumSINOForm" property="falseBoxToList">
								 <c:choose>
       							 <c:when test="${i % 2 == 0}">
								<tr align="center" style="font-weight: bolder;">
									<td td height="25" class="row-odd"><%=i %></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="courseName"/></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="subjectName"/></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="boxNum"/></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="schemeNum"/></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="academicYear"/></td>
									<td td height="25" class="row-odd"><bean:write name="box" property="examName"/></td>
									<td td height="25" class="row-odd">edit</td>
									<td td height="25" class="row-odd">delete</td>
								</tr>
								</c:when>
       						 <c:otherwise>
       						 <tr align="center" style="font-weight: bolder;">
									<td td height="25" class="row-even"><%=i %></td>
									<td td height="25" class="row-even"><bean:write name="box" property="courseName"/></td>
									<td td height="25" class="row-even"><bean:write name="box" property="subjectName"/></td>
									<td td height="25" class="row-even"><bean:write name="box" property="boxNum"/></td>
									<td td height="25" class="row-even"><bean:write name="box" property="schemeNum"/></td>
									<td td height="25" class="row-even"><bean:write name="box" property="academicYear"/></td>
									<td td height="25" class="row-even"><bean:write name="box" property="examName"/></td>
									<td td height="25" class="row-even">edit</td>
									<td td height="25" class="row-odd">delete</td>
								</tr>
								</c:otherwise>
    						</c:choose>
    						<%i++; %>
								</logic:iterate>
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
				</logic:notEmpty>
				
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading"></td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
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
var showET = document.getElementById("displayET").value;
if(showET.length != 0 && showET == "yes") {
	document.getElementById("et").style.display = "block";
	document.getElementById("evaluatorType").style.display = "block";
}else{
	document.getElementById("et").style.display = "none";
	document.getElementById("evaluatorType").style.display = "none";
	
}
var showAT = document.getElementById("displayAST").value;
if(showAT.length != 0 && showAT == "yes") {
	document.getElementById("ast").style.display = "block";
	document.getElementById("answerScriptType").style.display = "block";
}else{
	document.getElementById("ast").style.display = "none";
	document.getElementById("answerScriptType").style.display = "none";
}
var year = document.getElementById("tempyear").value;
if(year.length != 0) {
 	document.getElementById("year").value=year;
}
</script>