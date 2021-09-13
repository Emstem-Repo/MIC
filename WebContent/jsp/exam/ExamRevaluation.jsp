<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>


<head>
<title>:: CMS ::</title>
<SCRIPT>
	function fun() {
		
		document.location.href = "ExamRevaluation.do?method=initExamRevaluation";
	}

	function getExamName(examType) {
		getExamNameByExamType("examMap", examType, "examName", updateExamName);

	}
	function updateExamName(req) {
		updateOptionsFromMap(req, "examName", "- Select -");
	}
	function getCourse(examName) {
		getCourseByExamName("schemeMap", examName, "course", updateToCourse);

	}
	function updateToCourse(req) {
		updateOptionsFromMap(req, "course", "- Select -");

	}

	function getScheme(courseId) {
		examId = document.getElementById("examName").value;
		getSchemeNoByExamIdCourseId("schemeMap", examId, courseId, "scheme",
				updateToScheme);

	}
	function updateToScheme(req) {
		updateOptionsFromMap(req, "scheme", "- Select -");

	}
	function getSubject(schemeId) {
		var courseId = document.getElementById("course").value;
		getSubjectsByCourseScheme("schemeMap", courseId, "subject",
				updateToSubject, schemeId);
	}

	function updateToSubject(req) {
		updateOptionsFromMap(req, "subject", "- Select -");

	}
	function getSubjectType(subjectNo) {

		if (subjectNo != '') {
			var args = "method=getSubjectsTypeBySubjectId&subjectId="
					+ subjectNo;
			var url = "AjaxRequest.do";
			requestOperation(url, args, updateClass);
		}
	}
	function updateClass(req) {
		updateSubjectsTypeBySubjectIdForMarks(req, "subjectType");
	}
	function getExamsByExamTypeAndYear() {
		var examType=document.getElementById("examType").value;
		var year=document.getElementById("year").value;
		getExamNameByExamTypeAndYear("examMap", examType,year, "examName", updateExamName);
	}
	function updateExamName(req) {
		updateOptionsFromMap(req, "examName", "- Select -");
		updateCurrentExam(req, "examName");
	}
</SCRIPT>

</head>


<html:form action="/ExamRevaluation.do" method="POST"
	enctype="multipart/form-data">

	<html:hidden property="method" styleId="method"
		value="getStudentMarksEntry" />
	<html:hidden property="formName" value="ExamRevaluationForm"
		styleId="formName" />
	<html:hidden property="pageType" value="1" styleId="pageType" />

	<table width="99%" border="0">

		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam
			&gt;&gt; Re-valuation &gt;&gt;</span></span></td>

		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Re-valuation</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
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
									<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span> <bean:message
										key="knowledgepro.admin.year" /> :</div></td>
									<td width="16%" class="row-even" valign="top">
									<input
										type="hidden" id="tempyear" name="appliedYear"
										value="<bean:write name="ExamRevaluationForm" property="year"/>" />
									<html:select 
										property="year" styleId="year"
										styleClass="combo" onchange="getExamsByExamTypeAndYear()">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:renderAcademicYear></cms:renderAcademicYear>
									</html:select></td>
							<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span> <bean:message
										key="knowledgepro.exam.examDefinition.examType" /> :</div></td>
								<td height="25" class="row-even"><html:select
										property="examType" styleClass="combo"  styleId="examType" onchange="getExamsByExamTypeAndYear()">
										<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<logic:notEmpty property="examTypeList"
											name="ExamRevaluationForm">
											<html:optionsCollection property="examTypeList"
												name="ExamRevaluationForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									</tr>
								<tr>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span> Exam
									Name :</div>
									</td>

									<td class="row-even"><html:select property="examName"
										styleClass="combo" styleId="examName"
										name="ExamRevaluationForm" onchange="getCourse(this.value)"
										style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
										<logic:notEmpty name="ExamRevaluationForm"
											property="examNameMap">
											<html:optionsCollection property="examNameMap"
												name="ExamRevaluationForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Marks
									Entry For :</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="marksEntryFor" styleId="marksEntryFor"
										style="width:200px">
										<html:option value="">Select</html:option>
										<html:option value="Single Student">Single Student</html:option>
										<html:option value="All Students">All Students</html:option>
									</html:select></td>

								</tr>
								<tr>


									<td width="23%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Course:</div>
									</td>
									<td width="23%" height="25" class="row-even"><html:select
										property="course" styleClass="body" styleId="course"
										onchange="getScheme(this.value)" style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<c:if
											test="${ExamRevaluationForm.examName != null && ExamRevaluationForm.examName != ''}">
											<c:set var="schemeMap"
												value="${baseActionForm.collectionMap['schemeMap']}" />
											<c:if test="${schemeMap != null}">
												<html:optionsCollection name="schemeMap" label="value"
													value="key" />
											</c:if>
										</c:if>

										<c:if test="${retainValues=='yes'}">
											<logic:notEmpty name="ExamRevaluationForm"
												property="courseNameList">
												<html:optionsCollection property="courseNameList"
													name="ExamRevaluationForm" label="value" value="key" />
											</logic:notEmpty>
										</c:if>

									</html:select></td>

									<td width="28%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>
									Scheme:</div>
									</td>
									<td width="26%" class="row-even"><html:select
										property="scheme" styleClass="body" styleId="scheme"
										onchange="getSubject(this.value)" style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<c:if
											test="${ExamRevaluationForm.course != null && ExamRevaluationForm.course != ''}">
											<c:set var="schemeMap"
												value="${baseActionForm.collectionMap['schemeMap']}" />
											<c:if test="${schemeMap != null}">
												<html:optionsCollection name="schemeMap" label="value"
													value="key" />
											</c:if>
										</c:if>

										<c:if test="${retainValues=='yes'}">
											<logic:notEmpty name="ExamRevaluationForm"
												property="schemeNameList">
												<html:optionsCollection property="schemeNameList"
													name="ExamRevaluationForm" label="value" value="key" />
											</logic:notEmpty>
										</c:if>


									</html:select></td>
								</tr>
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Subject
									:</div>
									</td>
									<td height="25" class="row-even"><html:select
										property="subject" styleClass="body" styleId="subject"
										onchange="getSubjectType(this.value)" style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<c:if
											test="${ExamRevaluationForm.scheme != null && ExamRevaluationForm.scheme != ''}">
											<c:set var="schemeMap"
												value="${baseActionForm.collectionMap['schemeMap']}" />
											<c:if test="${schemeMap != null}">
												<html:optionsCollection name="schemeMap" label="value"
													value="key" />
											</c:if>
										</c:if>
										<c:if test="${retainValues=='yes'}">
											<logic:notEmpty name="ExamRevaluationForm"
												property="subjectNameList">
												<html:optionsCollection property="subjectNameList"
													label="value" value="key" />
											</logic:notEmpty>
										</c:if>

									</html:select></td>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Subject
									Type:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="subjectType" styleId="subjectType"
										style="width:200px">
										<html:option value="">Select</html:option>
										<html:option value="1">Theory</html:option>
										<html:option value="0">Practical</html:option>
										<html:option value="11">Theory and Practical</html:option>
									</html:select></td>

								</tr>

								<tr>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Revaluation
									Type :</div>
									</td>
									<td class="row-even" colspan="3"><html:select
										property="revaluationType" styleId="revaluationType"
										style="width:200px">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="ExamRevaluationForm"
											property="listRevaluationType">
											<html:optionsCollection property="listRevaluationType"
												name="ExamRevaluationForm" label="display" value="id" />
										</logic:notEmpty>
									</html:select></td>
								</tr>
								<tr>
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
								name="Submit7" type="submit" class="formbutton" value="Submit" /></td>

							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left"><input
								type="button" class="formbutton" value="Cancel" onclick="fun()" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
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
var year = document.getElementById("tempyear").value;
if(year.length != 0) {
 	document.getElementById("year").value=year;
}
</script>