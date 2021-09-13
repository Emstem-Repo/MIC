<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested" %>

<script type="text/javascript">

	function getPrograms(ProgramTypeId) {
		getProgramsByType("programMap", ProgramTypeId, "programId",
				updatePrograms);
		resetOption("courseId");
	}
	
	function updatePrograms(req) {
		updateOptionsFromMap(req, "programId", "- Select -");
	}
	
	function getCourse(programId) {
		getCoursesByProgram("courseMap", programId, "courseId", updateCourse);
		var destination = document.getElementById("selectedSubjects");
		for (x1=destination.options.length-1; x1>=0; x1--) {
			destination.options[x1]=null;
		}		
	}
	function updateCourse(req) {
		updateOptionsFromMap(req, "courseId", " Select ");
	}


	function resetCriteria() {
		document.getElementById("programTypeId").value = "";
		document.getElementById("programId").value = "";
		document.getElementById("courseId").value = "";
		resetOption("programId");
		resetOption("courseId");
		resetErrMsgs();
	}	

	function getClasses() {
		var year = document.getElementById("year").value;
		var courseId = document.getElementById("courseId").value;
		getclassesByYearAndCourse("classMap", courseId, year, "classId", updateClasses);
	}
	function updateClasses(req) {
		updateOptionsFromMap(req, "classId", "- Select -");
	}
	
	
</script>


<html:form action="/UpdatePassFailForCJC" method="post">
	<html:hidden property="method" styleId="method" value="updatePassFail" />
	<html:hidden property="formName" value="updatePassFailForCjc" />
	<html:hidden property="pageType" value="1" />
	<table width="98%" border="0">
		<tr>

			<td><span class="Bredcrumbs">Exams
			<span class="Bredcrumbs">&gt;&gt; Update Pass/Fail &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader">Update Pass/Fail </strong></div>
					</td>

					<td width="13"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></div>
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
					<td height="70" valign="top" background="images/Tright_03_03.gif"></td>

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
							<td width="21%" height="25" class="row-odd">
							<div align="right"><span class="Mandatory">*</span><bean:message
								key="knowledgepro.admin.program.type" /></div>
							</td>
							<td width="10%" height="25" class="row-even">
							<input type="hidden"
								name="prgTypeId" id="prgTypeId"
								value='<bean:write name="updatePassFailForCjc" property="programTypeId"/>' />

							<label><html:select
								property="programTypeId" styleClass="comboLarge"
								styleId="programTypeId" onchange="getPrograms(this.value)">
								<html:option value="">
									<bean:message key="knowledgepro.select" />
								</html:option>
								<cms:renderProgramTypes></cms:renderProgramTypes>
							</html:select></label><span class="star"></span></td>
							<td width="25%" class="row-odd">
							<div align="right"><span class="Mandatory">*</span><bean:message
								key="knowledgepro.admin.prog" /></div>
							</td>
							<td width="30%" class="row-even"><span class="star">
							
							<html:select name="updatePassFailForCjc" property="programId"
								styleId="programId" styleClass="comboLarge"
								onchange="getCourse(this.value)">
								<html:option value="">- Select -</html:option>
								<c:if
									test="${updatePassFailForCjc.programTypeId != null && updatePassFailForCjc.programTypeId != ''}">
									<c:set var="programMap"
										value="${baseActionForm.collectionMap['programMap']}" />
									<c:if test="${programMap != null}">
										<html:optionsCollection name="programMap" label="value"
											value="key" />
									</c:if>
								</c:if>
							</html:select> </span></td>
							<%--<td height="25" valign="top" class="row-even">
								<div align="right">&nbsp;</div>
							</td>--%>
							</tr>
						<tr>
							<td height="25" valign="top" class="row-odd">
							<div align="right"><span class="Mandatory">*</span><bean:message
								key="knowledgepro.admin.course.with.col" /></div>
							</td>
							<td height="25" valign="top" class="row-even"><html:select
								name="updatePassFailForCjc" property="courseId" styleId="courseId"
								styleClass="comboLarge" onchange="getClasses()">
								<html:option value="">- Select -</html:option>
								<c:if
									test="${updatePassFailForCjc.programId != null && updatePassFailForCjc.programId != ''}">
									<c:set var="courseMap"
										value="${baseActionForm.collectionMap['courseMap']}" />
									<c:if test="${courseMap != null}">
										<html:optionsCollection name="courseMap" label="value"
											value="key" />
									</c:if>
								</c:if>
							</html:select></td>
							<td class="row-odd" >
								<div align="right"><span class="Mandatory">*</span><bean:message key = "knowledgepro.fee.academicyear.col"/></div>
							</td>
							<td class="row-even"><input type="hidden" id="tempyear"
								name="tempyear"
								value="<bean:write name="updatePassFailForCjc" property="year"/>" />
								<html:select
								name="updatePassFailForCjc" property="year" styleId="year"
								styleClass="combo" onchange="getClasses()">
								<html:option value="">- Select -</html:option>
								<cms:renderYear></cms:renderYear>
							</html:select></td>
						</tr>
						<tr>
							<td height="25" class="row-odd"><div align="right"><span class="Mandatory">*</span>Classes</div></td>
			                <td class="row-even">
							<html:select property="classId" styleId="classId" styleClass="combo">
								<html:option value=" "><bean:message key="knowledgepro.admin.select" /></html:option>
								<c:if test="${updatePassFailForCjc.courseId != null && updatePassFailForCjc.courseId != ''}">
									<c:set var="classMap" value="${baseActionForm.collectionMap['classMap']}" />
									<c:if test="${classMap != null}">
										<html:optionsCollection name="classMap" label="value" value="key" />
									</c:if>
								</c:if>
							</html:select>
							</td>
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

				</tr>
				<tr>
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>

							<td width="45%" height="35">
							<div align="right">
								<html:submit property="" styleClass="formbutton" value="Submit"></html:submit>
							</div>
							</td>
							<td width="2%"></td>
							<td width="53%">
								<html:button property="" styleClass="formbutton" value="Reset"
									onclick="resetCriteria()"></html:button>
							</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">&nbsp;</td>
					<td width="13" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>

				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
					<td width="100%" background="images/TcenterD.gif"></td>
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
		document.getElementById("programTypeId").value = programType;
	}
	var year = document.getElementById("tempyear").value;
	if (year.length != 0) {
		document.getElementById("year").value = year;
	}
</script>