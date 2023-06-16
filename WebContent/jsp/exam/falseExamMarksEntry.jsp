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
	
	function searchDetails(){
		document.getElementById("method").value="setvaluesBybarcode";
        document.newExamMarksEntryForm.submit();
		
	}
	function  print() {
		/* document.getElementById("method").value="printList";
        document.newExamMarksEntryForm.submit(); */
        var url = "falseMarkEntry.do?method=printList";
    	myRef = window .open(url, "Marks Card", "left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
	}
	function  submitmarks() {
		document.getElementById("method").value="saveEvaluatorsMarks";
        document.newExamMarksEntryForm.submit();
	}
	
	
</SCRIPT>

</head>


<html:form action="/falseMarkEntry" method="POST" enctype="multipart/form-data">
	<html:hidden property="method" styleId="method" value="saveEvaluatorsMarks" />
	<html:hidden property="formName" value="newExamMarksEntryForm" styleId="formName" />
	<html:hidden property="pageType" value="9" styleId="pageType" />
	<%-- <html:hidden property="validationAST" styleId="validationAST" />
	<html:hidden property="validationET" styleId="validationET"/>
	<html:hidden property="displayAST" styleId="displayAST"/>
	<html:hidden property="displayET" styleId="displayET"/> --%>
	<html:hidden property="falseBased" value="true"/>
	<table width="99%" border="0">

		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam
			&gt;&gt; Exam Marks Entry&gt;&gt;</span></span></td>

		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Student
					Marks Entry</td>
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
				
				<logic:notEmpty property="displatoList.courseName" name="newExamMarksEntryForm">
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
								<td height="25" width="22%" class="row-odd"><div align="left">Exam Name</div></td>
								<td height="25" width="23%" class="row-even"><div align="left">
									<bean:write name="newExamMarksEntryForm" property="displatoList.examName"/>
								</div></td>
								<td width="25%" class="row-odd">Course</td>   
								<td width="30%" class="row-even">
									<bean:write name="newExamMarksEntryForm" property="displatoList.courseName"/>
								</td>
							</tr>
							<tr>
								<td height="25" width="22%" class="row-odd"><div align="left">Subject</div></td>
								<td height="25" width="23%" class="row-even"><div align="left">
									<bean:write name="newExamMarksEntryForm" property="displatoList.subjectName"/>
								</div></td>
								<td width="25%" class="row-odd" >Scheme</td>   
								<td width="30%" class="row-even">
									<bean:write name="newExamMarksEntryForm" property="displatoList.termNum"/>
								</td>
							</tr>
							<tr>
								<td height="25" width="22%" class="row-odd"><div align="left">Exam Type</div></td>
								<td height="25" width="23%" class="row-even"><div align="left">
									<bean:write name="newExamMarksEntryForm" property="displatoList.examType"/>
								</div></td>
								<td width="25%" class="row-odd" >Subject Type</td>   
								<td width="30%" class="row-even">
									<bean:write name="newExamMarksEntryForm" property="displatoList.subjectType"/>
								</td>
							</tr>
							<%-- <tr>
								<td height="25" width="22%" class="row-odd"><div align="left">Evaluator Type</div></td>
								<td height="25" width="23%" class="row-even"><div align="left">
									<html:select property="evalNo" name="newExamMarksEntryForm" disabled="true">
										<html:option value="">Select</html:option>
										<html:option value="1">First Evaluator</html:option>
										<html:option value="2">Second Evaluator</html:option>
										<html:option value="3">Third Evaluator</html:option>
										<html:option value="4">Final Evaluator</html:option>
									</html:select>
								</div></td>
								<td width="25%" class="row-odd" ></td>   
								<td width="30%" class="row-even">
									<bean:write name="newExamMarksEntryForm" property="displatoList.subjectType"/>
								</td>
							</tr> --%>
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
				</logic:notEmpty>
				<tr height="25px"></tr>
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
									<td height="25" colspan="8" class="row-even">Scan barcode here!!!!!!!!</td>
								</tr>
								<tr height="30px">
									<td width="50%" align="center" colspan="2" class="row-odd" >False Number</td>   
									<td width="50%" align="center" colspan="2" class="row-odd" >Marks</td>
									
								</tr>
								
								
								<tr>
								  
									
									<td width="50%" align="center" colspan="2" class="row-even">
									
										<html:text property="falseNo" maxlength="16" onkeypress="keyListen(event)" styleId="falseId"  name="newExamMarksEntryForm"/>
									</td>
									<td width="50%" align="center" colspan="2" class="row-even" >
									<logic:notEmpty property="displatoList.examName" name="newExamMarksEntryForm">
									<logic:notEmpty property="studentMarksTo" name="newExamMarksEntryForm">
									<logic:notEmpty property="studentMarksTo" name="newExamMarksEntryForm">
									<logic:notEmpty property="falseNo" name="newExamMarksEntryForm">
										<logic:equal value="1" property="evalNo" name="newExamMarksEntryForm">
											<logic:empty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.firstEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" property="studentMarksTo.examEvalTo.firstEvaluation"></html:text>
											</logic:empty>
											<logic:notEmpty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.firstEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" disabled="true" property="studentMarksTo.examEvalTo.firstEvaluation"></html:text>
												<script>	
													/* document.getElementById("method").value="AddEvaluatorsMarks";
            										document.newExamMarksEntryForm.submit(); */
            									</script>	
											</logic:notEmpty>
										</logic:equal>
										<logic:equal  value="2" property="evalNo"  name="newExamMarksEntryForm">
											<logic:empty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.secondEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" property="studentMarksTo.examEvalTo.secondEvaluation"></html:text>
											</logic:empty>
											<logic:notEmpty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.secondEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" disabled="true" property="studentMarksTo.examEvalTo.secondEvaluation"></html:text>
												<script>	
													document.getElementById("method").value="AddEvaluatorsMarks";
            										document.newExamMarksEntryForm.submit();
            									</script>	
											</logic:notEmpty>
										
										</logic:equal>
										<logic:equal value="3" property="evalNo" name="newExamMarksEntryForm">
											<logic:empty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.thirdEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" property="studentMarksTo.examEvalTo.thirdEvaluation"></html:text>
											</logic:empty>
											<logic:notEmpty name="newExamMarksEntryForm" property="studentMarksTo.examEvalTo.thirdEvaluator">
												<html:text styleId="markId" onkeypress="keyListenAdd(event)" disabled="true" property="studentMarksTo.examEvalTo.thirdEvaluation"></html:text>
												<script>	
													document.getElementById("method").value="AddEvaluatorsMarks";
            										document.newExamMarksEntryForm.submit();
            									</script>	
											</logic:notEmpty>
										</logic:equal>
									</logic:notEmpty>
									</logic:notEmpty >
									</logic:notEmpty>
									</logic:notEmpty>
									</td>   
									
								</tr>
								
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
				
				<tr height="25px"></tr>
				
				
				
				
				<logic:notEmpty property="examEvalToList" name="newExamMarksEntryForm">
				
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
									<td height="25" colspan="8" class="row-even">Entered Marks</td>
								</tr>
								<tr height="30px">
								<td width="10%" align="center" colspan="2" class="row-even">serial No.</td>
									<td width="45%" align="center" colspan="2" class="row-even" >False Number</td>   
									<td width="45%" align="center" colspan="2" class="row-even" >Marks</td>
									
								</tr>
								<% int i=0; %>
								<logic:iterate property="examEvalToList" id="evTo" name="newExamMarksEntryForm"	indexId="count">
								<%i++ ;%>
								<tr height="30px">
								<td width="10%" align="center" colspan="2" class="row-even"><%=i %></td>
									<td width="45%" align="center" colspan="2" class="row-odd" >
										<bean:write name="evTo" property="falseNo"/>
									</td>   
									<td width="45%" align="center" colspan="2" class="row-odd" >
										<logic:equal value="1" property="evalNo" name="newExamMarksEntryForm">
											<logic:empty name="evTo" property="firstEvaluator">
												<html:text styleId="markId"  disabled="true" name="evTo" onkeypress="keyListenAdd(event)" property="firstEvaluation"></html:text>
											</logic:empty>
											<logic:notEmpty name="evTo" property="firstEvaluator">
												<html:text styleId="markId"  name="evTo" disabled="true" onkeypress="keyListenAdd(event)" property="firstEvaluation"></html:text>
											</logic:notEmpty>
										</logic:equal>
										<logic:equal  value="2" property="evalNo"  name="newExamMarksEntryForm">
											<html:text styleId="markId" name="evTo"  disabled="true" onkeypress="keyListenAdd(event)" property="secondEvaluation"></html:text>
										</logic:equal>
										<logic:equal value="3" property="evalNo" name="newExamMarksEntryForm">
											<html:text styleId="markId" name="evTo"  disabled="true" onkeypress="keyListenAdd(event)" property="thirdEvaluation"></html:text>
										</logic:equal>
									</td>
									
								</tr>
								</logic:iterate>
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
				</logic:notEmpty>
				
				
				
				
				
							
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="49%" height="35" align="right">
							
							<logic:empty name="newExamMarksEntryForm" property="examMarkEvaluationPrintToList">
							<input
								name="Submit7" type="submit" class="formbutton" value="Final Submit" />
								<!-- <button class="formbutton" onclick="submitmarks()">Final Submit</button> -->
								</logic:empty>
								</td>
							
							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left">
								<%-- <html:reset styleClass="formbutton"></html:reset> --%>
								<logic:notEmpty name="newExamMarksEntryForm" property="examMarkEvaluationPrintToList">
								<button class="formbutton" onclick="print()">Print</button>
								</logic:notEmpty>
							</td>
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
function keyListen(event) {
	var chCode = ('charCode' in event) ? event.charCode : event.keyCode;
    if (chCode=="13") {
    	document.getElementById("method").value="setvaluesBybarcode";
        document.newExamMarksEntryForm.submit();
	}
}
function keyListenAdd(event) {
	var chCode = ('charCode' in event) ? event.charCode : event.keyCode;
	 if (chCode=="13") {
        	document.getElementById("method").value="AddEvaluatorsMarks";
            document.newExamMarksEntryForm.submit();
		}
}

/* var lastScannedBarCode = "";
var listOfScannedBarCodes = [];
document.onkeypress = onGlobalKeyPressed;

function onGlobalKeyPressed(e) {
    var charCode = (typeof e.which == "number") ? e.which : e.keyCode;

    if (charCode != 13) { // ascii 13 is return key
        lastScannedBarCode += String.fromCharCode(charCode);
    } else { // barcode reader indicate code finished with "enter"
        var lastCode = lastScannedBarCode;

        

        lastScannedBarCode = ""; // zero out last code (so we do not keep adding)
        document.getElementById("falseId").value=lastCode;
        console.log(lastCode);
        console.log(document.getElementById("falseId").value);
        document.getElementById("method").value="setvaluesBybarcode";
        document.newExamMarksEntryForm.submit();
    }    
}
 */

function submitmarks(event){
	document.getElementById("method").value="saveEvaluatorsMarks";
    document.newExamMarksEntryForm.submit();
	
}
 
 var falseNumm=document.getElementById("falseId").value;
 if (falseNumm==null || falseNumm=="") {
	 document.getElementById("falseId").autofocus;
	 document.getElementById("falseId").select();
}else {
	document.getElementById("markId").autofocus;
	 document.getElementById("markId").select();
}

</script>