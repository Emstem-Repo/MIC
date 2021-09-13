<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%><html:html>
	<head>
		<title><bean:message key="knowledgepro.title"/> </title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<script language="JavaScript" src="js/calendar_us.js"></script>
		<link rel="stylesheet" href="css/calendar.css">

		<script type="text/javascript" src="jquery/collaps/jquery-1.10.2.js"></script>
		<link rel="stylesheet" type="text/css" href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
		<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
		<script type="text/javascript">
			var hook = true;
			var jq=$.noConflict();

			function resetDetailMarkClass12(count) {
		 		document.getElementById("totalMark").value="";
		 		document.getElementById("ObtainedMark").value="";
		 		if( document.getElementById("totallangMark")!=null)
		 			document.getElementById("totallangMark").value="";
		 		if( document.getElementById("ObtaintedlangMark")!=null)
		 			document.getElementById("ObtaintedlangMark").value="";
				var i;
				for(i=1;i<=count;i++) {
					if(i!=1) {
						if(document.getElementById("detailMark.subjectid"+i)!=null)
							document.getElementById("detailMark.subjectid"+i).value="";
					}
					if(document.getElementById("detailMark.subject"+i+"TotalMarks")!=null)
						document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
					if(document.getElementById("detailMark.subject"+i+"ObtainedMarks")!=null)
						document.getElementById("detailMark.subject"+i+"ObtainedMarks").value="";
				}
	 			resetErrMsgs();
			}

			function updatetotalMarkClass12(count) {
				var totalmark=document.getElementById("totalMark").value;
				totalmark=0;
				var i;
				for(i=1;i<=count;i++) {
					var subjectmark=document.getElementById("detailMark.subject"+i+"TotalMarks").value;
					if(subjectmark.length==0) {
						subjectmark=0;
					}
					if(IsNumeric(totalmark)) {
						totalmark=parseFloat(subjectmark)+parseFloat(totalmark);
						document.getElementById("totalMark").value=totalmark;
					}
				}
			}

			function updateObtainMarkClass12(count) {
				var universityId = document.getElementById("universityId").value;		
				var obtainmark=document.getElementById("ObtainedMark").value;
				obtainmark=0;
				var i;
				for(i=1;i<=count;i++) {
					var subjectmark=document.getElementById("detailMark.subject"+i+"ObtainedMarks").value;
					if(subjectmark.length==0){
						subjectmark=0;
					}
					if(IsNumeric(obtainmark)) {
						obtainmark=parseFloat(subjectmark)+parseFloat(obtainmark);
						document.getElementById("ObtainedMark").value=obtainmark;
						if(universityId == '26' && subjectmark != 0) {
							document.getElementById("detailMark.subject"+i+"TotalMarks").value = '200';
							updatetotalMarkClass12(count);
						}
					}
				}
			}

			function checkObtainedMarks(count) {
				var i;
				for(i=1;i<=count;i++) {
					var subjecobtaintmark=document.getElementById("detailMark.subject"+i+"ObtainedMarks").value;
					var subjecttotalmark=document.getElementById("detailMark.subject"+i+"TotalMarks").value;
					if(subjecobtaintmark.length!=0 && subjecttotalmark.length!=0) {
						if(IsNumeric(subjecobtaintmark) && IsNumeric(subjecttotalmark)) {
							if(parseFloat(subjecobtaintmark)>parseFloat(subjecttotalmark)) {
								document.getElementById("detailMark.subject"+i+"TotalMarks").value="";
								jq.confirm({
									'message'	: 'WARNING:  Entered marks are greater than Maximum marks ',
									'buttons'	: {
											'Ok'	: {
											'class'	: 'blue',
											'action': function() {
												jq.confirm.hide();
												
											}
										}
									}
								});
							}
						}
					}
				}
			}

			function submitAdmissionFormCalc(val) {
			    var outOf=false;
				var count=document.getElementById("subjectCount").value;
				var i;
				for(i=1;i<=count;i++) {
					if(document.getElementById("detailMark.subjectid"+i)!=null && document.getElementById("detailMark.subject"+i+"TotalMarks")!=null && document.getElementById("detailMark.subject"+i+"ObtainedMarks")!=null){
						if(document.getElementById("detailMark.subjectid"+i).value!="" && document.getElementById("detailMark.subject"+i+"TotalMarks").value!="" && document.getElementById("detailMark.subject"+i+"ObtainedMarks").value!=""){
							var subject=document.getElementById("detailMark.subjectid"+i).value;
							var subjecttotal=parseFloat(document.getElementById("detailMark.subject"+i+"TotalMarks").value);
							var subjectobtained=parseFloat(document.getElementById("detailMark.subject"+i+"ObtainedMarks").value);
							if(subjecttotal==subjectobtained){
								outOf=true;
							}
						}
					}
				}
				if(outOf) {
					jq.confirm({
						
						'message'	: 'WARNING: You have same marks in obtained and max marks,Please re check once.',
						'buttons'	: {
							'Ok'	: {
								'class'	: 'blue',
								'action': function(){
											jq.confirm.hide();
										 	document.getElementById("method").value=val;
									 		document.onlineApplicationForm.submit();
								}
							},
					        'Cancel'	:  {
									'class'	: 'gray',
									'action': function(){
										jq.confirm.hide();
									}
								}
							}
					});
				} else{
					document.getElementById("method").value=val;
		 			document.onlineApplicationForm.submit();
				}
			}

			function appendMethodOnBrowserClose() {
				hook = false;
			}
			$(function() {
				$("a,.formbutton").click(function() {
					hook =false;
				});
			});

			window.onbeforeunload = confirmExit;
		  	function confirmExit() {
				if(hook){
				  hook =false;
				}else{
					hook =true;
				}
		  	}

			$(document).ready(function() {	
	
				var ctrlKeyDown = false;
				$(document).on("keydown", keydown);
			    $(document).on("keyup", keyup);
   
			    function keydown(e) { 
			        if ((e.which || e.keyCode) == 116 || ((e.which || e.keyCode) == 82 && ctrlKeyDown)) {
			            // Pressing F5 or Ctrl+R
			            e.preventDefault();
			        } else if ((e.which || e.keyCode) == 17) {
			            // Pressing  only Ctrl
			            ctrlKeyDown = true;
			        }
			    };

			    function click(e) {
			    	e.preventDefault();
			    };

			    function keyup(e) {
			        // Key up Ctrl
			        if ((e.which || e.keyCode) == 17) 
			            ctrlKeyDown = false;
			    };
    
    		});
		</script>
		<style type="text/css">
			<!--
			body {
				margin-left: 0px;
				margin-top: 5px;
				margin-right: 0px;
				margin-bottom: 0px;
			}
			-->
		</style>

		<!-- for cache controling with html code-->
		<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		 
		 <!-- for cache controling with jsp code-->
		<% 
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
			response.setHeader("Expires", "0"); // Proxies.
		%>
	</head>
	<body>
		<input type="hidden" id="subjectCount" value="<%=CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS %>" />
		<table width="80%" style="background-color: #F0F8FF" align="center">
			<html:hidden property="universityIdPUC" styleId="universityId"/>
  			<tr><td></td></tr>
  			<tr>
  				<td align="left">
  					<div id="errorMessage"><html:errors/></div>
  				</td>
  			</tr>
  			<tr>
    			<td>
    				<table width="100%" height="347"  border="0" cellpadding="0" cellspacing="0">
              			<tr>
                   			<td height="35" colspan="6" class="body" >
                   				<table width="100%" align="center" cellspacing="1" cellpadding="2">
                     				<tr>
										<td class="row-odd" width="15%" align="center"><bean:message key="admissionForm.detailmark.slno.label"/></td>
										<td class="row-odd" width="20%"><div align="center">Subject Name</div></td>
				 						<logic:equal value="1" name="onlineApplicationForm" property="programTypeId">
					 						<td height="25" class="row-odd" width="15%"><div align="center"><bean:message key="admissionForm.semmark.obtain.label"/></div></td>
											<td height="25" class="row-odd" width="15%"><div align="center"><bean:message key="admissionForm.semmark.total.label"/></div></td>
				 						</logic:equal>
				 						<logic:notEqual value="1" name="onlineApplicationForm" property="programTypeId">
					 						<td height="25" class="row-odd" width="15%"><div align="center"><bean:message key="admissionForm.detailmark.obtain.label"/></div></td>
											<td height="25" class="row-odd" width="15%"><div align="center"><bean:message key="knowledgepro.admission.maxMark"/></div></td>
				 						</logic:notEqual>
									</tr>
									<%
										int optionalCount=1;
										for(int i=1;i<=CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS;i++) {
											String propertyName="detailMark.subjectid"+i;
											String dynaMandatory="detailMark.subject"+i+"Mandatory";
											String propertyDetailedName="detailMark.detailedSubjects"+i+".id";
											String propertyDetailedId="detailMark.detailedSubjects"+i;
											String totalMarkprop="detailMark.subject"+i+"TotalMarks";
											String dynajs="updatetotalMarkClass12("+CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS+");"+"checkObtainedMarks("+CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS+");";
											String obtainMarkprop="detailMark.subject"+i+"ObtainedMarks";
											String dynajs2="updateObtainMarkClass12("+CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS+");"+"checkObtainedMarks("+CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS+");";
											String method = "putSubjectName('"+i+"',this.value)";
									%>
	                     				<tr>
	                     					<%if(i==1){%>
	                        					<td class="row-even" > &nbsp;&nbsp;<%=i %>&nbsp;&nbsp;&nbsp;<span class="Mandatory">*</span>&nbsp;<b>English</b></td>
												<td class="row-even" align="center">
													<nested:select disabled="true" property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' styleClass="dropdownmedium">
						                    			<html:option value="">--Select--</html:option>
						                    			<html:optionsCollection property="admSubjectLangMap" name="onlineApplicationForm" label="value" value="key"/>						   				
													</nested:select>
												</td>
												<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
												<td class="row-even" align="center">
													<logic:equal value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text disabled="true" property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:equal>
													<logic:notEqual value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:notEqual>
												</td>
											<%}
												else if(i==2) {
											%>
												<td class="row-even"> &nbsp;&nbsp;<%=i %>&nbsp;&nbsp;&nbsp;<b>Language Course</b></td>
												<td class="row-even" align="center">
													<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' styleClass="dropdownmedium">
							                    		<html:option value="">--Select--</html:option>
							                    		<html:optionsCollection property="admSubjectLangMap" name="onlineApplicationForm" label="value" value="key"/>
													</nested:select>
												</td>
												<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
												<td class="row-even" align="center">
													<logic:equal value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text disabled="true" property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:equal>
													<logic:notEqual value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:notEqual>
												</td>
											<%}
												else if(i>2) {
											%>
												<td class="row-even" > &nbsp;&nbsp;<%=i %>&nbsp;&nbsp;&nbsp;<b>Optional Subject <%=optionalCount%></b> </td>
												<td class="row-even" align="center">
													<nested:select property="<%=propertyName%>"  name="onlineApplicationForm" styleId='<%=propertyName%>' styleClass="dropdownmedium">
							                    		<html:option value="">--Select--</html:option>
							                    		<html:optionsCollection property="admSubjectMap" name="onlineApplicationForm" label="value" value="key"/>
													</nested:select>
												</td>
												<td class="row-even" align="center"><html:text property="<%=obtainMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs2 %>' styleId='<%=obtainMarkprop %>'></html:text></td>
												<td class="row-even" align="center">
													<logic:equal value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text disabled="true" property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:equal>
													<logic:notEqual value="26" property="universityIdPUC" name="onlineApplicationForm">
														<html:text property="<%=totalMarkprop %>" size="6" maxlength="3" onblur='<%=dynajs %>' styleId='<%=totalMarkprop %>'></html:text>
													</logic:notEqual>
												</td>
											<%
												optionalCount++;
											}%>
										</tr>
									<%} %>
									<tr>
					   					<logic:equal value="1" name="onlineApplicationForm" property="programTypeId">
											<td class="row-odd" colspan="2" ><div align="right">TOTAL MARKS:</div></td>
										</logic:equal>
										<logic:notEqual value="1" name="onlineApplicationForm" property="programTypeId">
											<td class="row-odd" colspan="2" ><div align="right">TOTAL MARKS / TOTAL GRADE POINTS:</div></td>
										</logic:notEqual>			
										<td class="row-even" align="center"><html:text property="detailMark.totalObtainedMarks" size="7" maxlength="7"  styleId="ObtainedMark" readonly="true"></html:text></td>
										<td class="row-even" align="center"><html:text property="detailMark.totalMarks" styleId="totalMark" size="7" maxlength="7" readonly="true"></html:text></td>
									</tr>
                   				</table>
                   			</td>
                 		</tr>
                 		<tr>
                   			<td height="35" colspan="6" class="body">
								<%
									String resetmethod="resetDetailMarkClass12("+CMSConstants.MAX_CANDIDATE_CLASS12_SUBJECTS+")";
								%>
                				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
                  					<tr>
                    					<td width="45%" height="35" colspan="2">
                    						<div align="right">
                         						<html:button property="" onclick="submitAdmissionFormCalc('submitDetailMarkConfirmClass12')" styleClass="btn" value="Submit"></html:button>
                    						</div>
                    					</td>
                    					<td width="53%">
                    						&nbsp;<html:button property="" value="Clear" styleClass="btn1" onclick='<%=resetmethod %>' /> 
                   	 						&nbsp;<html:button property=""  styleClass="btn1" value="Cancel" onclick='submitConfirmCancelButton()'></html:button>
                    					</td>
                  					</tr>
                				</table>
                            </td>
                 		</tr>
                 		<tr>
                   			<td height="10" colspan="6" class="body" ></td>
                 		</tr>
              		</table>
         		</td>
  			</tr>
		</table>
	</body>
</html:html>
