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
		document.location.href = "newCIAMarksEntry.do?method=initInternalMarksEntry";
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

	function printReport(){
		document.getElementById("method").value = "checkModified"; 
		var examId = document.getElementById("exam").value;
		var subject = document.getElementById("subject").value;
		var classId = document.getElementById("classId").value;
		document.location.href = "birtFeeReport.do?method=internalMarksReport&subjectId="+subject+"&classId="+classId+"&examId="+examId;
	}
	function checkModified(){
		document.getElementById("method").value = "printReportInternalMarks"; 
		}

</script>

<html:form action="/newCIAMarksEntry" method="POST" enctype="multipart/form-data">
	<html:hidden property="formName" value="newInternalMarksEntryForm"	styleId="formName" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method"	value="saveInternalExamMarks" />
	<html:hidden property="examId" styleId="exam"/>
	<html:hidden property="subjectId" styleId="subject"/>
	<html:hidden property="classId" styleId="classId"/>
	<html:hidden property="batchId" styleId="batchId"/>
	<html:hidden property="printConfirmation" styleId="printConfirmation"/>
	<table width="99%" border="0">
		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam	>> Exam Marks Entry&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Student
					Marks Entry </td>
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
									<td width="23%" height="25" class="row-odd">
									<div align="right">Exam Name :</div>
									</td>
									<td width="23%" height="25" class="row-even"><bean:write
										property="examName" name="newInternalMarksEntryForm"></bean:write></td>
										<logic:notEmpty name="newInternalMarksEntryForm" property="batchName">
									<td width="28%" class="row-odd">
									<div align="right">Batch Name :</div>
									</td>
									<td width="26%" class="row-even"><bean:write
										property="batchName" name="newInternalMarksEntryForm"></bean:write>&nbsp;(<bean:write
										property="classNames" name="newInternalMarksEntryForm"></bean:write>)</td>
									</logic:notEmpty>
									<logic:empty name="newInternalMarksEntryForm" property="batchName">
									<td width="28%" class="row-odd">
									<div align="right">Class Name :</div>
									</td>
									<td width="26%" class="row-even"><bean:write
										property="className" name="newInternalMarksEntryForm"></bean:write></td>
									</logic:empty>
								</tr>
								<tr>
									<td height="25" class="row-odd">
									<div align="right">Subject Name:</div>
									</td>
									<td height="25" class="row-even"><bean:write
										property="subjectName" name="newInternalMarksEntryForm"></bean:write></td>
									<td height="25" class="row-odd">
									<div align="right">Subject Code :</div>
									</td>
									<td height="25" class="row-even"><bean:write
										property="subjectCode" name="newInternalMarksEntryForm"></bean:write></td>
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
									<td class="row-odd" colspan="4" align="left">
										<logic:notEmpty name="newInternalMarksEntryForm" property="maxMarks">
											<font color="Red" style="font-size: medium">Please enter the Marks out of <bean:write name="newInternalMarksEntryForm" property="maxMarks"/></font>
										</logic:notEmpty>
										<logic:empty name="newInternalMarksEntryForm" property="maxMarks">
											<font color="Red" style="font-size: medium">Max marks is not defined. Please contact the Admin</font>
										</logic:empty>
									</td>
								</tr>
								<tr>

									<td height="35" class="row-odd">
									<div align="center">Sl.No</div>
									</td>
									<td height="35" class="row-odd">Student Name</td>
									<td height="35" class="row-odd">Register No.</td>
									<logic:equal value="1" property="subjectType" name="newInternalMarksEntryForm">
									<td height="35" class="row-odd">Theory Marks </td>
									</logic:equal>
									<logic:equal value="0" property="subjectType" name="newInternalMarksEntryForm">
										<td height="35" class="row-odd">Practical Marks  </td>
									</logic:equal>
									<logic:equal value="11" property="subjectType" name="newInternalMarksEntryForm">
										<td height="35" class="row-odd">Theory Marks   </td>
									</logic:equal>
									

								</tr>
								<logic:notEmpty  property="studentList" name="newInternalMarksEntryForm">
								<nested:iterate property="studentList" id="examMarksEntryStudentTO" name="newInternalMarksEntryForm" indexId="count">
									<tr>
										<td height="35" width="7%" class="row-even">
										<div align="center"><c:out value="${count+1}"></c:out></div>
										</td>
										<td height="35" width="20%" class="row-even"><nested:write
											property="name" /></td>
										<td height="35" width="18%" class="row-even"><nested:write
											property="registerNo" /></td>
											<%String id="test_"+count; %>
										<logic:equal value="1" property="subjectType" name="newInternalMarksEntryForm">
											<logic:equal value="true" property="isTheory" name="examMarksEntryStudentTO">
												<td height="35" width="18%" class="row-even">
												<logic:equal value="true" property="isTheorySecured" name="examMarksEntryStudentTO">	
													<nested:text property="theoryMarks" maxlength="6" size="6" styleId= '<%=id %>' onkeydown="movenext(this.name, event, this.id)" disabled="true"/>
												</logic:equal>
												<logic:equal value="false" property="isTheorySecured" name="examMarksEntryStudentTO">	
													<nested:text property="theoryMarks" maxlength="6" size="6" styleId= '<%=id %>' onkeydown="movenext(this.name, event, this.id)"/>
												</logic:equal>
												</td>
											</logic:equal>
										</logic:equal>
										<logic:equal value="0" property="subjectType" name="newInternalMarksEntryForm">
											<logic:equal value="true" property="isPractical" name="examMarksEntryStudentTO">
												<td height="35" width="18%" class="row-even">
													<logic:equal value="true" property="isPracticalSecured" name="examMarksEntryStudentTO">	
														<nested:text property="practicalMarks" maxlength="6" size="6" styleId= '<%=id %>' onkeydown="movenext(this.name, event, this.id)" disabled="true"/>
													</logic:equal>
													<logic:equal value="false" property="isPracticalSecured" name="examMarksEntryStudentTO">	
														<nested:text property="practicalMarks" maxlength="6" size="6" styleId= '<%=id %>' onkeydown="movenext(this.name, event, this.id)"/>
													</logic:equal>
												</td>
											</logic:equal>
										</logic:equal>
										
									</tr>
								</nested:iterate>
								</logic:notEmpty>
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
							<div align="right">
							<html:submit property="" styleClass="formbutton" value="Print Report" onclick="checkModified()"></html:submit>&nbsp;&nbsp;&nbsp;
							<input name="button2" type="submit"
								class="formbutton" value="Submit" />
							</div>
							</td>
							<td width="2%"></td>
							<td width="6%">
								<input type="button" class="formbutton" value="Close" onclick="goToFirstPage()" />
								</td>
							<td width="2%"></td>
							<td width="46%">&nbsp;</td>

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
 <script type="text/javascript">
 var printConfirm=document.getElementById("printConfirmation").value;
 if(printConfirm!='' && printConfirm=="Yes"){
	 var url = "newCIAMarksEntry.do?method=printReportInternalMarksAfterConfirm";
	 myRef = window.open(url, "Marks Entry",
									"left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
 }
</script>