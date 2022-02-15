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
		document.location.href = "falsenumSINO.do?method=initFalseBox";
	}

	function getExamName(examType) {
		getExamNameByExamType("examMap", examType, "examNameId", updateExamName);

	}
	function updateExamName(req) {
		updateOptionsFromMap(req, "examNameId", "- Select -");
		updateCurrentExam(req, "examNameId");
		getCourse(document.getElementById("examNameId").value);
	}
	
	function getTeacher() {

		var year=document.getElementById("year").value;
		var subjectId = document.getElementById("subject").value;
		getTeacherBysubId("teachersMap", year, subjectId, "teachers", updateToTeachers);
	}
	function updateToTeachers(req) {
		updateOptionsFromMap(req, "teachers", "- Select -");
	}
	
	
	function editFalseBox(id){
		document.getElementById("falseBoxId").value=id;
		document.getElementById("method").value="editBarcodeBox";
        document.falsenumSINOForm.submit();
	}
	function searchDeptWise(deptId,par){
		
		if (par=='ex') {
			var destination = document.getElementById("teachers");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId("",deptId,updateTeachersMapByDepEx);
		}
		else if (par=='ch') {
			var destination = document.getElementById("chiefExaminer");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId("",deptId,updateTeachersMapByDepCh);
		}
		else if (par=='ad') {
			var destination = document.getElementById("additionalExaminer");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId("",deptId,updateTeachersMapByDepAd);
		}
		else if (par=='cr') {
			var destination = document.getElementById("correctionValidator");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId("",deptId,updateTeachersMapByDepCr);
		}
		
	}
	

	
	function searchOutSider(val,par){
		
		if (par=='ex') {
			var dept=document.getElementById("departmentIdEx").value;
			var destination = document.getElementById("teachers");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId(val,dept, updateTeachersMapByDepEx);
		}
		else if (par=='ch') {
			var dept=document.getElementById("departmentIdCH").value;
			var destination = document.getElementById("chiefExaminer");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId(val,dept, updateTeachersMapByDepCh);
		}
		else if (par=='ad') {
			var dept=document.getElementById("departmentIdADD").value;
			var destination = document.getElementById("additionalExaminer");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId(val,dept, updateTeachersMapByDepAd);
		}
		else if (par=='cr') {
			var dept=document.getElementById("departmentIdCORR").value;
			var destination = document.getElementById("correctionValidator");
			for (x1 = destination.options.length; x1 >=0; x1--) {
			destination.options[x1] = null;
			} 
			getTeacherBysubId(val,dept, updateTeachersMapByDepCr);
		}
		
	
	}
	function updateTeachersMapByDepEx(req){
		updateOptionsFromMap(req, "teachers", "--Select--");
		
	}
	function updateTeachersMapByDepCh(req){
		updateOptionsFromMap(req, "chiefExaminer", "--Select--");
		
	}
	function updateTeachersMapByDepAd(req){
		updateOptionsFromMap(req, "additionalExaminer", "--Select--");
		
	}
	function updateTeachersMapByDepCr(req){
		updateOptionsFromMap(req, "correctionValidator", "--Select--");
		
	}
	
</SCRIPT>

</head>


<html:form action="/falsenumSINO" method="POST" enctype="multipart/form-data">
	<html:hidden property="method" styleId="method" value="saveBarCodeList" />
	<html:hidden property="formName" value="falsenumSINOForm" styleId="formName" />
	<html:hidden property="pageType" value="2" styleId="pageType" />
	<html:hidden property="displaySubType" styleId="sCodeName_2" value="sName" />
	<html:hidden property="falseBoxId" styleId="falseBoxId" />
	<html:hidden property="subjectId" />
	<html:hidden property="examId" />
	
									
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
									
									 <td height="25" class="row-odd">
									<div align="right">Department:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="departmentId" styleId="departmentIdEx" onchange="searchDeptWise(this.value,'ex')">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="departmentMap">
											<html:optionsCollection property="departmentMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									
									<td height="25" class="row-even"><div align="left">Outside facualty
																	</div></td>
																<td class="row-odd"><span class="star"> <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ex')" property="outside" value="1" />Yes&nbsp; <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ex')" property="outside" value="0" />No
																</span></td>
									
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Teacher
									:</div>
									</td>
									<td height="25" class="row-even">
									<html:select name="falsenumSINOForm" styleId="teachers" property="teachers"  >
									<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
                    						<html:optionsCollection name="falsenumSINOForm" property="teachersMap" label="value" value="key"/>
                  					</html:select></td>

								</tr>
								
								<tr>
									
									 <td height="25" class="row-odd">
									<div align="right">Department:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="departmentId" styleId="departmentIdCH" onchange="searchDeptWise(this.value,'ch')">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="departmentMap">
											<html:optionsCollection property="departmentMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									
									<td height="25" class="row-even"><div align="left">Outside facualty
																	</div></td>
																<td class="row-odd"><span class="star"> <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ch')" property="outside" value="1" />Yes&nbsp; <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ch')" property="outside" value="0" />No
																</span></td>
									
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Chief Examiner
									:</div>
									</td>
									<td height="25" class="row-even">
									<html:select name="falsenumSINOForm" styleId="chiefExaminer" property="chiefExaminer"  >
									<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
                    						<html:optionsCollection name="falsenumSINOForm" property="teachersMap" label="value" value="key"/>
                  					</html:select></td>

								</tr>
								
								
								<tr>
									
									 <td height="25" class="row-odd">
									<div align="right">Department:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="departmentId" styleId="departmentIdADD" onchange="searchDeptWise(this.value,'ad')">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="departmentMap">
											<html:optionsCollection property="departmentMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									
									<td height="25" class="row-even"><div align="left">Outside facualty
																	</div></td>
																<td class="row-odd"><span class="star"> <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ad')" property="outside" value="1" />Yes&nbsp; <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'ad')" property="outside" value="0" />No
																</span></td>
									
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Additional Examiner
									:</div>
									</td>
									<td height="25" class="row-even">
									<html:select name="falsenumSINOForm" styleId="additionalExaminer" property="additionalExaminer"  >
									<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
                    						<html:optionsCollection name="falsenumSINOForm" property="teachersMap" label="value" value="key"/>
                  					</html:select></td>

								</tr>
								
								<tr>
									
									 <td height="25" class="row-odd">
									<div align="right">Department:</div>
									</td>

									<td height="25" class="row-even"><html:select
										property="departmentId" styleId="departmentIdCORR" onchange="searchDeptWise(this.value,'cr')">
										<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
										<logic:notEmpty name="falsenumSINOForm"
											property="departmentMap">
											<html:optionsCollection property="departmentMap"
												name="falsenumSINOForm" label="value" value="key" />
										</logic:notEmpty>
									</html:select></td>
									
									<td height="25" class="row-even"><div align="left">Outside facualty
																	</div></td>
																<td class="row-odd"><span class="star"> <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'cr')" property="outside" value="1" />Yes&nbsp; <html:radio
																			name="falsenumSINOForm" onchange="searchOutSider(this.value,'cr')" property="outside" value="0" />No
																</span></td>
									
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory"></span>Correction Validator
									:</div>
									</td>
									<td height="25" class="row-even">
									<html:select name="falsenumSINOForm" styleId="correctionValidator" property="correctionValidator"  >
									<html:option value="">
											<bean:message key="knowledgepro.select" />
										</html:option>
                    						<html:optionsCollection name="falsenumSINOForm" property="teachersMap" label="value" value="key"/>
                  					</html:select></td>

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
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="49%" height="35" align="right"><input
								name="Submit7" type="submit" class="formbutton" value="Save" /></td>

							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left"><input
								type="button" class="formbutton" value="Cancel"
								onclick="cancelAction()" /></td>
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
