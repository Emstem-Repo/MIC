<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>

<link type="text/css"
	href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
<script src="jquery/js/jquery-ui-1.10.0.custom.min.js" type="text/javascript"></script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>

<script type="text/javascript"><!--

function getExamName() {
	var examType=document.getElementById("examType").value;
	var year=document.getElementById("year").value;
	getExamNameByExamTypeAndYear("examMap", examType,year, "examId", updateExamName);

}
function updateExamName(req) {
	updateOptionsFromMap(req, "examId", "- Select -");
	updateCurrentExam(req, "examId");
}

function loadClassByExamName(examId){
	var year=document.getElementById("year").value;
	document.location.href = "newSupplementaryImpApp.do?method=getClassNameByExamName&examId="+examId+"&year="+year;
}
function submit(){
	document.newSupplementaryImpApplicationForm.method.value="checkRegularApplicationForStudentLoginTemp";
	document.newSupplementaryImpApplicationForm.submit();
}



</script>
<html:form action="/newSupplementaryImpApp" >	

	<html:hidden property="method" styleId="method" value="printRegularApplication" />
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="examType" name="newSupplementaryImpApplicationForm" styleId="examType"/>
	<table width="100%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.exam"/>
			<span class="Bredcrumbs">&gt;&gt;
			Manual regular application
			 &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader">Manual regular application</strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"><FONT color="red"> <span class='MandatoryMark'></span></FONT></div>
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
							<td width="100%" background="images/02.gif"></td>
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
										value="<bean:write name="newSupplementaryImpApplicationForm" property="year"/>" />
									<html:select 
										property="year" styleId="year" onchange="getExamName()"
										styleClass="combo" >
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:renderAcademicYear></cms:renderAcademicYear>
									</html:select></td>
									<td class="row-odd">
									<div align="right"><span class="Mandatory">*</span> Exam
									Name :</div>
									</td>

										<td width="21%"  class="row-even">
									<html:select property="examId" styleClass="combo" styleId="examId" name="newSupplementaryImpApplicationForm" style="width:200px" onchange="loadClassByExamName(this.value);">
									<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
										<logic:notEmpty name="newSupplementaryImpApplicationForm" property="examNameMap">
											<html:optionsCollection property="examNameMap" name="newSupplementaryImpApplicationForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
							</tr>
							<tr>
								<td width="22%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span> <bean:message
										key="knowledgepro.exam.studentEligibilityEntry.classCode" />
									:</div>
									</td>
							<td height="25" class="row-even" colspan="3">

									<table border="0">
										<tr>
											<td width="112"><label>
											
											 <nested:select
												property="classCodeIdsFrom" styleClass="body" styleId="mapClass"
												style="width:200px">
												<logic:notEmpty name="newSupplementaryImpApplicationForm" property="mapClass" >
														<nested:optionsCollection name="newSupplementaryImpApplicationForm"
															property="mapClass" label="value" value="key"
															styleClass="comboBig" />
													</logic:notEmpty>
											</nested:select> </label></td>
								
										</tr>
									</table>

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
				
				<tr height="15px"></tr>
				
				
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
											<td width="7%" height="25" class="row-odd">
											<div align="center"><bean:message key="knowledgepro.slno" /></div>
											</td>
											<td width="29%" class="row-odd">
											<div align="center">Student Name</div>
											</td>
											<td width="29%" class="row-odd">
											<div align="center">Register No</div>
											</td>
											<td width="18%" class="row-odd">
											<div align="center">Print</div>
											</td>
									
										</tr>
										<nested:iterate id="student" property="stuList" name="newSupplementaryImpApplicationForm"
											 indexId="count">
											<tr>
											<c:choose>
												<c:when test="${count%2 == 0}">
													<tr class="row-even">
												</c:when>
												<c:otherwise>
													<tr class="row-white">
												</c:otherwise>
											</c:choose>
											<%String s1="check_"+count; %>
											<td height="25">
											<div align="center"><c:out value="${count + 1}" /></div>
											</td>
											<td align="center"><bean:write name="student"
												property="studentName" /></td>
											
											<td align="center"><bean:write name="student"
												property="registerNo" /></td>	
											<td align="center"><nested:checkbox property="isChecked" styleId="<%=s1%>" ></nested:checkbox>	</td>	
									
											</tr>
										</nested:iterate>
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
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						
								<td width="45%" height="35">
							<div align="right">
									<html:submit property="" styleClass="formbutton" value="Print" 
										styleId="submitbutton">
									</html:submit>
								</div>
							</td>
							<td width="2%"></td>
							<td width="53%"><html:button property=""
							styleClass="formbutton" value="Reset"
							onclick="resetFormFields()"></html:button></td>
						</tr>
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
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
<script type="text/javascript">
var year = document.getElementById("tempyear").value;
if(year.length != 0) {
 	document.getElementById("year").value=year;
}

$(".display-info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
var print = "<c:out value='${newSupplementaryImpApplicationForm.printSupplementary}'/>";
if(print.length != 0 && print == "true") {
	var url ="newSupplementaryImpApp.do?method=printRegularApplicationApp";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}

</script>