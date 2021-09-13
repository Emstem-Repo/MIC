<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>

<script type="text/javascript">


function editAttMaster(id) {
	document.location.href = "ExamAttendanceMarks.do?method=editAttendenceMaster&id="+id;
	document.getElementById("submit").value="Update";
	
	//resetErrMsgs();
}

function deleteAttMaster(id,name) {
	deleteConfirm =confirm("Are you sure to delete "+ name +" this entry?");
	if(deleteConfirm)
	{
	document.location.href = "ExamAttendanceMarks.do?method=deleteAttMaster&id="+id;
	}
}

function isValidNumber(field) {
	if (isNaN(field.value)) {
		field.value = "";
	}
}
function resetMessages() {
	
	document.location.href = "ExamAttendanceMarks.do?method=initAttMaster";
	
}
function update(){
	document.location.href = "ExamAttendanceMarks.do?method=updateAttMaster";
}
</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/ExamAttendanceMarks.do">
	<html:hidden property="formName" value="ExamAttendanceMarksForm" />
	<html:hidden property="pageType" value="1" />
	<c:choose>
		<c:when
			test="${examAttOperation != null && examAttOperation == 'edit'}">
			<html:hidden property="method" styleId="method"
				value="updateAttMaster" />
			<html:hidden property="id" styleId="id" />
			<html:hidden property="courseId" styleId="courseId"
				value="selectedCourse" />

			<html:hidden property="orgFromPercentage" styleId="orgFromPercentage" />
			<html:hidden property="orgToPercentage" styleId="orgToPercentage" />
			<html:hidden property="orgMarks" styleId="orgMarks" />
			<html:hidden property="orgTheoryPractical"
				styleId="orgTheoryPractical" />
			<html:hidden property="orgSelectedCourse" styleId="orgSelectedCourse" />


		</c:when>
		<c:otherwise>
			<html:hidden property="method" styleId="method" value="addAttMaster" />
		</c:otherwise>
	</c:choose>

	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><a href="main.html"
				class="Bredcrumbs"><bean:message key="knowledgepro.exam.exam" /></a>
			<span class="Bredcrumbs">&gt;&gt; <bean:message
				key="knowledgepro.exam.attendanceMarksRange" />&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message
						key="knowledgepro.exam.attendanceMarksRange" /></strong></td>
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
									<td width="10%" height="15" class="row-odd">
									<div align="right"><span class="Mandatory">*</span> <bean:message
										key="knowledgepro.exam.course" /> :</div>
									</td>


									<td width="17%" height="5" class="row-even"><nested:select
										property="selectedCourse" styleClass="body"
										multiple="multiple" size="8" styleId="id" style="width:500px">
										<nested:optionsCollection name="ExamAttendanceMarksForm"
											property="listExamCourseUtilTO" label="display" value="id" />
									</nested:select></td>
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
					<td valign="top" class="news">&nbsp;</td>
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


								<tr>
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.exam.fromPercentage" /> % :</div>
									</td>
									<td width="25%" height="25" class="row-even"><html:text
										property="fromPercentage" styleId="fromPercentage"
										maxlength="6" styleClass="TextBox" size="20"
										onkeypress="return isDecimalNumberKey(this.value,event)"
										onblur="isValidNumber(this)" /></td>
									<td width="28%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span></span><bean:message
										key="knowledgepro.exam.toPercentage" /> %:</div>
									</td>
									<td width="26%" class="row-even"><html:text
										property="toPercentage" styleId="toPercentage" maxlength="6"
										styleClass="TextBox" size="20" onkeypress="return isDecimalNumberKey(this.value,event)"
										onblur="isValidNumber(this)" /></td>
								</tr>
								<tr>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span></span><bean:message
										key="knowledgepro.exam.attendanceMarks" />:</div>
									</td>
									<td height="25" class="row-even"><html:text
										property="marks" styleId="marks" maxlength="20"
										styleClass="TextBox" size="9" onkeypress="return isDecimalNumberKey(this.value,event)"
										onblur="isValidNumber(this)" /></td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.exam.theoryProctical" /></div>
									</td>
									<td class="row-even"><html:select
										property="theoryPractical" styleId="theoryPractical">
										<html:option value="">Select</html:option>
										<html:option value="Theory">Theory</html:option>
										<html:option value="Practical">Practical</html:option>
										<html:option value="Theory and Practical">Theory and Practical</html:option>
									</html:select></td>
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

							<td width="49%" height="35" align="right"><c:choose>
								<c:when
									test="${examAttOperation != null && examAttOperation == 'edit'}">

									<input name="submit" type="submit" class="formbutton"
										value="Update" />
								</c:when>
								<c:otherwise>
									<input name="submit" type="submit" class="formbutton"
										value="Add Attendance" />

								</c:otherwise>
							</c:choose></td>
							<td width="2%" align="center">&nbsp;</td>
							<td width="49%" align="left">
								<c:choose>
									<c:when test="${examAttOperation == 'edit'}">
										<html:cancel styleClass="formbutton" onclick="update()">
											Reset
										</html:cancel>
									</c:when>
									<c:otherwise>
										<input type="button" class="formbutton" value="Reset"
										onclick="resetMessages()" />
									</c:otherwise>
								</c:choose>								
									
								</td>
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
							<td height="45" colspan="4">
							<table width="98%" border="0" align="center" cellpadding="0"
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
											<td height="25" class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.slno" /></div>
											</td>
											<td width="30%" height="25" class="row-odd" align="left"><bean:message
												key="knowledgepro.exam.course" /></td>
											<td width="20%" height="25" class="row-odd" align="left"><bean:message
												key="knowledgepro.exam.attendanceMarks" /></td>
											<td width="10%" height="25" class="row-odd" align="left"><bean:message
												key="knowledgepro.exam.fromPercentage" /> %</td>
											<td width="10%" height="25" class="row-odd" align="left"><bean:message
												key="knowledgepro.exam.toPercentage" /> %</td>
											<td width="20%" height="25" class="row-odd" align="left"><bean:message
												key="knowledgepro.exam.theoryProctical" /></td>
											<td class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.edit" /></div>
											</td>
											<td class="row-odd">
											<div align="center"><bean:message
												key="knowledgepro.delete" /></div>
											</td>
										</tr>



										<c:set var="temp" value="0" />

										<logic:iterate name="ExamAttendanceMarksForm"
											property="examAttList" id="examAttList"
											type="com.kp.cms.to.exam.ExamAttendanceMarksTO"
											indexId="count">
											<c:choose>
												<c:when test="${temp == 0}">
													<tr>
														<td height="25" class="row-even">
														<div align="center"><c:out value="${count+1}" /></div>
														</td>
														<td height="25" class="row-even" align="left"><bean:write
															name="examAttList" property="programTypeProgramCourse" /></td>
														<td height="25" class="row-even" align="left"><bean:write
															name="examAttList" property="marks" /></td>
														<td height="25" class="row-even" align="left"><bean:write
															name="examAttList" property="fromPercentage" /></td>
														<td height="25" class="row-even" align="left"><bean:write
															name="examAttList" property="toPercentage" /></td>
														<td height="25" class="row-even" align="left"><bean:write
															name="examAttList" property="isTheoryPractical" /></td>
														<td width="11%" height="25" class="row-even">
														<div align="center"><img src="images/edit_icon.gif"
															height="18" style="cursor: pointer"
															onclick="editAttMaster('<bean:write name="examAttList" property="id"/>')">
														</div>
														</td>
														<td height="25" class="row-even">
														<div align="center"><img
															src="images/delete_icon.gif" width="16" height="16"
															style="cursor: pointer"
															onclick="deleteAttMaster('<bean:write name="examAttList" property="id"/>','<bean:write name="examAttList" property="programTypeProgramCourse"/>')"></div>
														</td>
													</tr>
													<c:set var="temp" value="1" />
												</c:when>
												<c:otherwise>
													<tr>
														<td height="25" class="row-white">
														<div align="center"><c:out value="${count+1}" /></div>
														</td>
														<td height="25" class="row-white" align="left"><bean:write
															name="examAttList" property="programTypeProgramCourse" /></td>
														<td height="25" class="row-white" align="left"><bean:write
															name="examAttList" property="marks" /></td>
														<td height="25" class="row-white" align="left"><bean:write
															name="examAttList" property="fromPercentage" /></td>
														<td height="25" class="row-white" align="left"><bean:write
															name="examAttList" property="toPercentage" /></td>
														<td height="25" class="row-white" align="left"><bean:write
															name="examAttList" property="isTheoryPractical" /></td>
														<td height="25" class="row-white">
														<div align="center"><img src="images/edit_icon.gif"
															height="18" style="cursor: pointer"
															onclick="editAttMaster('<bean:write name="examAttList" property="id"/>')">
														</div>
														</td>
														<td height="25" class="row-white">
														<div align="center"><img
															src="images/delete_icon.gif" width="16" height="16"
															style="cursor: pointer"
															onclick="deleteAttMaster('<bean:write name="examAttList" property="id"/>','<bean:write name="examAttList" property="programTypeProgramCourse"/>')"></div>
														</td>
													</tr>
													<c:set var="temp" value="0" />
												</c:otherwise>
											</c:choose>
										</logic:iterate>


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
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
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