<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<script>
	function goToFirstPage(method) {
		document.location.href = "marksEntry.do?method="+method;
	}
	function movenext(val, e, count) {
		var keynum;
		var keychar;
		var numcheck;

		if (window.event) // IE
		{
			keynum = e.keyCode;
		} else if (e.which) // Netscape/Firefox/Opera
		{
			keynum = e.which;
		}
		if (keynum == 40 ) {
		var abc=count;
		var ghi=abc.substring(5);
		var jkl=parseInt(ghi)+1;
		var mno="test_"+jkl;
		//please check whether the control is found ....then move to next
		eval(document.getElementById(mno)).focus();
		//pqr.focus();
		return true;
		}
	}

	
	function GoToFalseNumber() {
		document.location.href = "marksEntry.do?method=initFalseNoEntry";
	}
	
</script>

<html:form action="/marksEntry" method="POST" enctype="multipart/form-data">
	<html:hidden property="formName" value="newExamMarksEntryForm"	styleId="formName" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method"	value="saveFalseNumber" />
	<table width="99%" border="0">
		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam	>> Exam False Number Entry&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Student
					False Number Entry - All Students Single Subject</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>

					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					<div align="right" class="mandatoryfield">*Mandatory fields</div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
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
									<td height="25" class="row-odd">
									<div align="right">Exam Name :</div>
									</td>

									<td height="25" class="row-even" colspan="3"><bean:write
										property="examName" name="newExamMarksEntryForm"></bean:write></td>
								
									<td width="23%" height="25" class="row-odd">
									<div align="right">Course :</div>
									</td>
									<td width="23%" height="25" class="row-even"><bean:write
										property="courseName" name="newExamMarksEntryForm"></bean:write></td>
										</tr>
								<tr>
									<td width="28%" class="row-odd">
									<div align="right">Scheme :</div>
									</td>
									<td width="26%" class="row-even"><bean:write
										property="schemeNo" name="newExamMarksEntryForm"></bean:write></td>

									<td height="25" class="row-odd">
									<div align="right">Subject Name:</div>
									</td>
									<td height="25" class="row-even"><bean:write
										property="subjectName" name="newExamMarksEntryForm"></bean:write></td>
									
								</tr>
								
							</table>
							</td>
							<td width="5" background="images/right.gif"></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
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
							<table width="100%" cellspacing="1" cellpadding="0">
								<tr>

									<td class="row-odd">
									<div align="center">Sl.No</div>
									</td>
									<td class="row-odd">Student Name</td>
									<td height="25" class="row-odd">Register No.</td>
									<td height="25" class="row-odd">False No.</td>
									</tr>
									<c:set var="temp" value="0" />
								<nested:iterate property="studentlist1" id="examMarksEntryStudentTO"	indexId="count">
								<c:choose>
										<c:when test="${temp == 0}">
									<tr>
										<td width="7%" class="row-even">
										<div align="center"><c:out value="${count+1}"></c:out></div>
										</td>
										<td width="20%" class="row-even"><nested:write
											property="name" /></td>
										<td width="18%" class="row-even"><nested:write
											property="registerNo" /></td>
											
										<td width="20%" class="row-even"><nested:text property="falseNo"></nested:text></td>
									</tr>
									<c:set var="temp" value="1" />
										</c:when>
										<c:otherwise>
										<tr>
										<td width="7%" class="row-odd">
										<div align="center"><c:out value="${count+1}"></c:out></div>
										</td>
										<td width="20%" class="row-odd"><nested:write
											property="name" /></td>
										<td width="18%" class="row-odd"><nested:write
											property="registerNo" /></td>
											
										<td width="20%" class="row-odd"><nested:text property="falseNo"></nested:text></td>
									</tr>
										<c:set var="temp" value="0" />
										</c:otherwise>
									</c:choose>
								</nested:iterate>
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
							<td width="45%" height="35">
							<div align="center"><input name="button2" type="submit"
								class="formbutton" value="Submit" />
							
							<input name="button" type="button"
								class="formbutton" value="Cancel" onclick="GoToFalseNumber()"/></div>
							</td>
							
						</tr>
					</table>
					</td>
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
