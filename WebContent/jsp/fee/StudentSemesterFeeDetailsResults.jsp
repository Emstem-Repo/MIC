<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>

<script type="text/javascript" language="javascript">
	function resetValues() {
		document.location.href = "studentSemesterFeeDetails.do?method=initStudentSemesterFeeDetails";
	}

	function save(){
		document.getElementById("method").value = "save";
		document.studentSemesterFeeDetailsForm.submit();
	}

	function reset(){
		resetFieldAndErrMsgs();
	}
	

	
</script>
<html:html>
<head>
<title><bean:message key="knowledgepro.title"/> </title>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<style type="text/css">

</style>
</head>
<html:form action="/studentSemesterFeeDetails">

	<html:hidden property="formName" value="studentSemesterFeeDetailsForm" styleId="formName" />
	<html:hidden property="pageType" value="2" styleId="pageType" />
	<html:hidden property="method" styleId="method" value="save" />
	
	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><a href="main.html"
				class="Bredcrumbs">Fee</a> <span class="Bredcrumbs">&gt;&gt;
			Student Semester Fee Details&gt;&gt;</span></span></td>
		</tr>

		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Student
					Semester Fee Details</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				
				<tr>

					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div align="right" class="mandatoryfield">*Mandatory fields</div>
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

									<td class="row-odd" height="28">
									<div align="right">Academic Year:</div>
									</td>
									<td class="row-even" colspan="3"><bean:write
										property="academicYear" name="studentSemesterFeeDetailsForm" /></td>

								</tr>
								<tr>
									<td width="26%" class="row-odd">
									<div align="right">Semester :</div>
									</td>

									<td width="27%" class="row-even" ><bean:write
										property="semester" name="studentSemesterFeeDetailsForm" /></td>


									<td width="19%" height="25" class="row-odd">
									<div align="right">
									<DIV align="right">Class Name :</DIV>
									</div>
									</td>
									<td width="28%" height="25" class="row-even"><bean:write
										property="className" name="studentSemesterFeeDetailsForm" /></td>
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
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">&nbsp;</td>

					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
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
							<table width="100%" cellspacing="1" cellpadding="0">
								<tr>
									<td height="25" align="center" class="row-odd">Sl no
									<td class="row-odd" align="center">Register No</td>
									<td class="row-odd" align="center">Student Name</td>
									<td class="row-odd" align="center">Admission Fee</td>
									<td class="row-odd" align="center">Special Fee</td>
									<td class="row-odd" align="center">Other Fee</td>
									<td class="row-odd" align="center">Semester Fee</td>
									<logic:equal value="true" name="studentSemesterFeeDetailsForm" property="isCourse">
									<td class="row-odd" align="center">CA Training Fee</td>
									</logic:equal>
									<td class="row-odd" align="center">Date</td>
									<td class="row-odd" align="center">Fee Approval</td>

									<td class="row-odd" align="center">Remarks</td>
								</tr>
								<nested:iterate id="list" property="studentList"  name="studentSemesterFeeDetailsForm" indexId="count">
								<%
								String date = "date" + count;
								String approve = "approve" + count;
								String universityFee = "universityFee" + count;
								String specialFee = "specialFee" + count;
								String otherFee = "otherFee" + count;
								String CATrainingFee = "CATrainingFee" + count;
								String remarks = "remarks" + count;
								String semesterFee = "semesterFee" + count;
								
								%>
									<c:choose>
										<c:when test="${count%2 == 0}">
											<tr class="row-even">
										</c:when>
										<c:otherwise>
											<tr class="row-white">
										</c:otherwise>
									</c:choose>
									<td width="7%" height="25" align="center"><c:out value="${count+1}"/></td>
									<td width="10%" align="center"><nested:write property="registerNo" /></td>
									<td width="15%" align="center"><nested:write property="studentName" /></td>
									<td width="10%" align="center"><nested:text size="5"  property="universityFee"  styleId="<%=universityFee %>"/></td>
									<td width="10%" align="center"><nested:text size="5"  property="specialFee"  styleId="<%=specialFee %>"/></td>
									<td width="10%" align="center"><nested:text size="5"  property="otherFee"  styleId="<%=otherFee %>"/></td>
									<td width="10%" align="center"><nested:text size="5"  property="semesterFee"  styleId="<%=semesterFee %>"/></td>
									<logic:equal value="true" name="studentSemesterFeeDetailsForm" property="isCourse">
									<td width="10%" align="center"><nested:text size="5"  property="CATrainingFee" styleId="<%=CATrainingFee %>"/></td>
									</logic:equal>
									<td width="15%" align="center">
									
									<nested:text  property="date" styleId='<%=date%>'   size="10" maxlength="16"/>
									
									
									<script language="JavaScript">
												new tcal({
													// form name
													'formname' :'studentSemesterFeeDetailsForm',
													// input name
													'controlname' :'<%=date%>'
												});
											</script>
									
									</td>
									
									
									<td width="9%" align="center">
									<nested:checkbox property="feeApprove" styleId="<%=approve%>"  value="1">yes</nested:checkbox>
									
									</td>
									
									<td width="19%"><nested:textarea  property="remarks" styleId="<%=remarks%>"></nested:textarea>
									</tr>
								</nested:iterate>
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
					<td height="10" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="94%" border="0" align="center" cellpadding="0"
						cellspacing="0">

						<tr>
							<td width="20%" height="35" align="center">&nbsp;</td>
							<td width="20%" height="35" align="center">&nbsp;</td>
							<td width="5%" align="center">
							<html:button property="" styleClass="formbutton" value="Submit" onclick="save()"></html:button></td>
							<td width="5%" align="center">
							<html:button property="" styleClass="formbutton" value="Clear" onclick="reset()"></html:button></td>
							<td width="5%" align="center">
							<input name="Submit2"
								type="button" class="formbutton" value="Cancel"
								onclick="resetValues()" />
							</td>
							<td width="20%" align="center">&nbsp;</td>
							<td width="20%" align="center">&nbsp;</td>
						</tr>
					</table>
					</td>
					<td valign="top" background="../images/Tright_3_3.gif" class="news"></td>

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
</html:html>
<script language="JavaScript" >
	var print = document.getElementById("validateProcess").value;
	var msg=document.getElementById("validateMsg").value;
	if(print.length != 0 && print == "true"){
		var deleteConfirm = confirm(msg);
		if (deleteConfirm) {
			document.newUpdateProccessForm.submit();
		}else{
			document.getElementById("validateProcess").value=false;
		}
		
		
	}
	
</script>