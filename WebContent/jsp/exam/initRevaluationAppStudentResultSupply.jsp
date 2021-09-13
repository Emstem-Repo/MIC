<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<link rel="stylesheet" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
<script language="JavaScript" src="js/ajax/AjaxUtil.js"></script>
<script language="JavaScript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript">

function cancelAction(){
	document.location.href="StudentLoginAction.do?method=returnHomePage";
}

function printApplication() {
	var printApp = "<c:out value='${newSupplementaryImpApplicationForm.printApplication}'/>";
	if( printApp!= false) {
		//document.getElementById("method").value = "showPrintDetailsForRevalution";
		//document.newSupplementaryImpApplicationForm.submit();
		var url ="newSupplementaryImpApp.do?method=showPrintChallanForRevaluationSupply";
		myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
	}else{
		document.getElementById("errorMessages").style.display="block";
		document.getElementById("err").innerHTML = "Please select atlest one exam apply type";
	}
}

function printChallanApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintChallanForRevaluationSupply";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}

function forRevaluation() {
	var rev = document.getElementById("isRevaluation");
	if(!rev.checked) {
		rev.value = "";
		document.getElementById("isScrutiny").disabled = false;
		
	}
	else {		
		document.getElementById("isScrutiny").value = "";
		document.getElementById("method").value = "getDataToFormForRevaluationChooseSupply";
		document.newSupplementaryImpApplicationForm.submit();
	}
}

function forScrutiny(){
	var scr = document.getElementById("isScrutiny");	
	if(!scr.checked) {
		scr.value = "";
		document.getElementById("isRevaluation").disabled = false;
	}
	else {
		var reval = "<c:out value='${newSupplementaryImpApplicationForm.isRevaluation}'/>";
		if(reval == "on"){
		document.getElementById("isRevaluation").value = false;
		document.getElementById("method").value = "getDataToFormForRevaluationChooseSupply";
		document.newSupplementaryImpApplicationForm.submit();
		}else {
			document.getElementById("method").value = "getDataToFormForRevaluationChooseSupply";
			document.newSupplementaryImpApplicationForm.submit();
		}
	}	
}

function viewTotalPoint(){
	  // var checked = document.getElementById("checked").checked;
	  	var rev = document.getElementById("isRevaluation");
		var scr = document.getElementById("isScrutiny");
	  	var checkedCount =  $(".checkId:checked").length;
	  if(checkedCount != 0){
		  document.getElementById("amountTab").style.display="table-row";
	  }
	  if(rev.checked){
	  if(checkedCount >= 1){
		 var amount = (500* checkedCount) + 30;
	  }else {
		  amount = 0;
	  }
	 }else if(scr.checked){
		 if(checkedCount >= 1){
			 var amount = (100* checkedCount) + 30;
		  }else {
			  amount = 0;
		  }
	 }
	  document.getElementById("totalAmount").innerHTML = amount;
	  document.getElementById("totalRevaluationPaymentFees").value = amount;
}



</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method" value="addRevaluationApplicationForStudentLoginSupply" />
	<html:hidden property="totalRevaluationPaymentFees" styleId="totalRevaluationPaymentFees"/>

	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><bean:message
				key="knowledgepro.exam.exam" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.exam.supplementaryApplication" />
			&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader"> <bean:message
						key="knowledgepro.exam.supplementaryApplication" /></strong></td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div align="right" class="mandatoryfield"><bean:message
						key="knowledgepro.mandatoryfields" /></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20">
					<table width="100%">
					<tr>
							<td align="left">
							<div id="errorMessages" class="ui-widget">
							<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
							<p>
							<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
							<strong>Alert:</strong>
							<span id="err"><html:errors/></span>
							
							</p>
							
							</div>
							</div>

							<div id="messages"><div class="display-info">
							<span id="msg"><html:messages id="message" property="messages" message="true"><c:out value="${message}" escapeXml="false"></c:out><br></html:messages></span>
							</div>
							</div>
							<script type="text/javascript">
								if(document.getElementById("msg")==null ||  document.getElementById("msg").innerHTML==''){
									document.getElementById("messages").style.display="none";
									}
								if(document.getElementById("err").innerHTML==''){
									document.getElementById("errorMessages").style.display="none";
									}
							</script>
							
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
						<td><img src="images/01.gif" width="5" height="5" /></td>
						<td width="914" background="images/02.gif"></td>
						<td><img src="images/03.gif" width="5" height="5" /></td>
					</tr>
					<tr>
						<td width="5" background="images/left.gif"></td>
						<td valign="top" align="center">
							
						<table width="90%" cellspacing="1" cellpadding="2">
						<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="revAppAvailable">
						<nested:notEmpty name="newSupplementaryImpApplicationForm" property="mainList">
						<tr>
						<td colspan="5" style="text-align: center; font-size: 13px;" class="studentrow-even">
						<b> Application for the Revaluation/Scrutiny of <bean:write name="newSupplementaryImpApplicationForm" property="examName" /> </b>
						</td>
						</tr>
								
						<tr>
		 			<td colspan="5">
		 
					 <table width="100%" border="0" style="font-size: 12px;">
		 
		
		  			<tr>-
					 <td height="25%" align="center" class="studentrow-even"><div align="center">1</div></td>
		 			<td class="studentrow-even">Name of the Candidate
																	
					</td >
					 <td height="25%" colspan="2" style="text-transform: uppercase;padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
					 </tr>
					 <tr>
					 <td align="center" class="studentrow-even"><div align="center">2</div></td>
		 			<td class="studentrow-even">Name of the Examination</td>
		 			<td colspan="2" style=" padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="examName" /></td>
					 </tr>
					 <tr>
					 <td align="center" width="5%" class="studentrow-even"><div align="center">3</div></td>
					 <td width="35%" class="studentrow-even">Register No</td>
					 <td colspan="2" style=" padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="registerNo" /></td>
					 </tr>
					 <tr>
					 <td align="center" width="5%" class="studentrow-even"><div align="center">4</div></td>
		 			<td width="35%" class="studentrow-even">Course</td>
					 <td colspan="2" style=" padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
					 </tr>
		 			<tr>
		 			<td height="25%" align="center" class="studentrow-even"><div align="center">5</div></td>
		 			<td height="25%" class="studentrow-even">Address for Communication with Telephone Number & E-mail id</td>
					 <td height="25%" colspan="2" style=" padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="address" />
					 <bean:write name="newSupplementaryImpApplicationForm" property="mobileNo" /> & 
					 <bean:write name="newSupplementaryImpApplicationForm" property="email" />
					 </td>
					 </tr>
		 			<tr>
		 			<td align="center" class="studentrow-even"><div align="center">6</div></td>
		 			<td class="studentrow-even">Date of Birth</td>
					 <td colspan="2" style=" padding-left: 10px;" class="studentrow-even"><bean:write name="newSupplementaryImpApplicationForm" property="originalDob" /></td>
					 </tr>
					<tr>
									<td align="center" class="studentrow-even">
									<div align="center">7</div>
									</td>
									<td  class="studentrow-even">
									Purpose of the application
									 <br> (Mark tick on whatever is applicable)
									</td>
									<logic:equal value="1" property="programTypeId" name="newSupplementaryImpApplicationForm">
										<td width="20%" class="studentrow-even">
											Revaluation 
											<br>&nbsp; &nbsp; &nbsp; <html:checkbox styleId="isRevaluation" name="newSupplementaryImpApplicationForm" property="isRevaluation" onclick="forRevaluation();"/>
										</td>
										</logic:equal>
									
									<td height="26" width="20%" class="studentrow-even">Scrutiny 
									<br> &nbsp; &nbsp; &nbsp; <html:checkbox styleId="isScrutiny" name="newSupplementaryImpApplicationForm" property="isScrutiny" onclick="forScrutiny();"/>
									
									</td>
									
									
									
								</tr>
		 
				</table>
		 
		
						 </td>
		 
		 
		 
						 </tr>
		 
								
								
								
								
								</nested:notEmpty>
								</nested:equal>
								
							
							</table></td>
							<td width="5" height="30" background="images/right.gif"></td>
							</tr>
							
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="revAppAvailable">
								Application is not available
							</nested:equal>
							
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="revAppAvailable">
								<nested:notEmpty name="newSupplementaryImpApplicationForm" property="mainList">
									<nested:iterate id="eto" name="newSupplementaryImpApplicationForm" property="mainList" indexId="count2">
										<nested:notEmpty property="examList" >
											<nested:iterate id="cto"  property="examList" indexId="count1">
											
											<tr><td colspan="4"> &nbsp; </td> </tr>
											<%-- 
											<tr ><td colspan="4" align="center"> <p>
											
											NOTE:Kindly tick [Apply for] for the subject which you wish to apply for Revaluation / Scrutiny / Copy of Answer Scripts
											
											</p>
											 </td> </tr>
									
											<tr class="heading"><td colspan="4" align="center"> <p>
											The Application will be available till: <bean:write name="eto" property="examDate"/> </p>
											 </td> </tr>
									--%>
									
								
					<tr>
						<td colspan="4" align="center">
						<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
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
									<td colspan="6" height="25"  align="center" class="studentrow-even">8. Write down the details of the papers for Scrutiny/Photo Copy</td>
									
									
									
								</tr>
								
								<tr>
									<td height="25"  align="center" class="studentrow-odd">SlNo.</td>
									<td  align="center" class="studentrow-odd">Course Code</td>
									<td  align="center"class="studentrow-odd">Course Title</td>
									<td  align="center" class="studentrow-odd">Marks Scored	</td>
									<td  align="center" class="studentrow-odd">Max.	Marks</td>
									<td  align="center" class="studentrow-odd">Apply For</td>
									
								</tr>
								


								<nested:iterate  property="toList" indexId="count">

									<%
										String dynamicStyle = "";
														if (count % 2 != 0) {
															dynamicStyle = "row-white";

														} else {
															dynamicStyle = "studentrow-even";

														}
									%>
									<tr>
										<td class='<%=dynamicStyle%>'>
										<div align="center"><c:out value="${count + 1}" /></div>
										</td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="subjectCode" /></td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="subjectName" /></td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="marks" /></td>
										<td class='<%=dynamicStyle%>'><nested:write
											property="maxMarks" /></td>
											
										
										<%-- 
										<td class='<%=dynamicStyle%>'>
										<%    String ft2 = "ft2_"+count2+"_"+count1+"_"+count; 
                      					 	  String ft1 = "ft1_"+count2+"_"+count1+"_"+count; %>
                      					 	  
		                     				 <nested:hidden styleId='<%=ft2%>' property="isAppearedTheory" />
		                     				 <span id="sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											 <nested:checkbox styleId='<%=ft1%>' property="failedTheory" indexed="true" disabled="true" />
											 </span>
											 <script type="text/javascript">
												var vft1=document.getElementById("ft2_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
												if(vft1=="true"){
												document.getElementById("ft1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
												document.getElementById("sp_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
												}
       										</script>
										</td>
										
										<td class='<%=dynamicStyle%>'>
										<%    String fp2 = "hidden1_"+count2+"_"+count1+"_"+count; 
                      						  String fp1 = "check1_"+count2+"_"+count1+"_"+count; %>
		                     				 <nested:hidden styleId='<%=fp2%>' property="isFailedPractical" />
		                     				 <span id="pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>">
											<nested:checkbox styleId='<%=fp1%>' property="failedPractical" indexed="true" disabled="true"/>
											</span>
										<script type="text/javascript">
										var vfp=document.getElementById("hidden1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
										if(vfp=="true"){
										document.getElementById("check1_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked =true ;
										document.getElementById("pr_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").style.background="red";
										}
										</script>
										</td>
										--%>
										<% String control="control1_"+count;
										String disable="disable_"+count;
										%>
										
												<td class='<%=dynamicStyle%>'>
												<nested:equal value="false" property="tempChecked">
												
												<input	type="hidden"  name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempChecked"
														id="hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>"	 value="<nested:write property='tempChecked'/>" />
												
												<input	id="checked" type="checkbox" class="checkId" name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isApplied"
														id="<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>" onclick="viewTotalPoint()" />
																	
																	<script type="text/javascript">
																		var studentId = document.getElementById("hidden_<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").value;
																		if(studentId == "true") {
																			document.getElementById("<c:out value='${count2}'/>_<c:out value='${count1}'/>_<c:out value='${count}'/>").checked = true;
																		}		
																	</script>
												
												</nested:equal>
												
												<nested:equal value="true" property="tempChecked">Applied</nested:equal>
											</td>
										<%-- 	
										<td class='<%=dynamicStyle%>'>
										<nested:equal value="false" property="tempPracticalChecked">
										<nested:equal value="true" property="isFailedPractical">

										<input
												type="hidden"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].tempPracticalChecked"
												id="prhidden_<c:out value='${count}'/>"
												value="<nested:write property='tempPracticalChecked'/>" />
												<input
												type="checkbox"
												name="mainList[<c:out value='${count2}'/>].examList[<c:out value='${count1}'/>].toList[<c:out value='${count}'/>].isAppearedPractical"
												id="pr_<c:out value='${count}'/>" />
												<script type="text/javascript">
													var pr = document.getElementById("prhidden_<c:out value='${count}'/>").value;
													if(pr == "true") {
														document.getElementById("pr_"+"<c:out value='${count}'/>").checked = true;
													}		
												</script>
												</nested:equal>
												</nested:equal>
												<nested:equal value="true" property="tempPracticalChecked">Applied</nested:equal>
										</td>
										--%>
										<nested:hidden styleId='<%=control%>' property="controlDisable" />
										
										
										
									
									
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
					</tr>
											</nested:iterate>
										</nested:notEmpty>
									</nested:iterate>
								</nested:notEmpty>
							</nested:equal>
							</table>
							</td>
							<td width="5" height="30" background="images/right.gif"></td>
						</tr>
						<tr id="amountTab" style="display: none;">
					<td width="5" background="images/left.gif"></td>
					<td height="20">
					<table width="70%" align="center">
					<tr>
							<td align="center">
							<div class="ui-widget">
							<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
							<p>
							<font style="size: 30px;"><strong>Total Amount:<span id="totalAmount"></span></strong></font>
							</p>
							</div>
							</div>
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
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="47%" height="29">&nbsp;</td>
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
							<td align="center" colspan="3" height="35">
							
						<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="revAppAvailable">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="displayButton">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="paymentDone">
							<nested:submit styleClass="btnbg" value="Pay Online"></nested:submit>
							</nested:equal>
							</nested:equal>
							</nested:equal>
							
							
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="printApplication">
							<html:button property="" 
								styleClass="btnbg" value="Print Application"
								onclick="printApplication()">
								</html:button>
								</nested:equal>
							<html:button property=""
								styleClass="btnbg" value="Close"
								onclick="cancelAction()"></html:button>
							</td>
						</tr>
					<tr>
					<td colspan="3">
					<br></br>
					<br></br>
					<br></br>
					</td>
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
					<td><img src="images/st_Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/st_TcenterD.gif"></td>
					<td><img src="images/st_Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
<script type="text/javascript">
$(".display-info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
//var print = "<c:out value='${newSupplementaryImpApplicationForm.printSupplementary}'/>";
//if(print.length != 0 && print == "true") {
	//var url ="newSupplementaryImpApp.do?method=showPrintDetailsForRevalution";
	//myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
//}
//var printApp = "<c:out value='${newSupplementaryImpApplicationForm.printApplication}'/>";


//if(printApp != 0 && printApp == "true"){
	//var url = "newSupplementaryImpApp.do?method=showPrintApplication";
	//myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
//}
	var rev = document.getElementById("isRevaluation");
	var scr = document.getElementById("isScrutiny");
	
	if(rev.checked) {
		document.getElementById("isScrutiny").disabled = false;
	}
	else if(scr.checked) {
		document.getElementById("isRevaluation").disabled = false;
	}
</script>