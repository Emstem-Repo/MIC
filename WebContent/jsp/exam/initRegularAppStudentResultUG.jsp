<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<link rel="stylesheet" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
<script language="JavaScript" src="js/ajax/AjaxUtil.js"></script>
<script language="JavaScript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript">
function cancelAction(){
	document.location.href="StudentLoginAction.do?method=returnHomePage";
}

function printApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintDetailsForRegular";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}

function printChallanApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintChallanForRegular";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}
function previousExamLink(){
	document.location.href="newSupplementaryImpApp.do?method=checkRegularApplicationForPreviousExam";
}


function submitApplication(){


	/**bootbox.confirm({
	    title: "Message :",
	    className: 'my-modal',
		 size: "small",
		 message: "Please Confirm the subjects selected for Regular exams before submission.No modifications is Possible after submission of the Application Form."
		,
		 buttons: {
	        confirm: {
	            label: 'Submit',
	            className: 'btn-success'
	        },
	        cancel: {
	            label: 'Cancel',
	            className: 'btn-danger'
	        }
	    },
		 callback: function(result) {
			
    if (result) {*/
    	 document.getElementById("method").value="addDetailsForRegular";
		 document.newSupplementaryImpApplicationForm.submit();
 /**   } else {
        console.log("User declined dialog");
    }
}
});*/


}

function onlinePay(){
	document.getElementById("method").value = "redirectToPGIExamRegular";
	document.newSupplementaryImpApplicationForm.submit();
		}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />

	<table width="100%" border="0">
		<tr>
			<td><span class="heading"><bean:message
				key="knowledgepro.exam.exam" /> <span class="Bredcrumbs">&gt;&gt;
			Regular Application 
			&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader"> Regular Application</strong></td>
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
					
		<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="regularAppAvailable">
		<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
				
		<table width="100%" cellspacing="1" cellpadding="2" style='font-family: "Times New Roman", Times, serif;font-size: 10pt;'>
							
		<tr>
		
		<td width="100%" align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="90" width="450" ></img>
		</td>

		</tr>
		<tr>
		<td style="text-align: center;" colspan="2"><table>
		<tr><td align="center">
		<b> EXAM REGISTRATION FORM <br><bean:write name="newSupplementaryImpApplicationForm" property="examName" />, <bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></b>
		</td></tr>
		<tr>
		 <td align="center" style="padding-left: 70px;" colspan="2" width="40%"><b>Permanent Register Number</b>
		 : <html:text property="regNo" name="newSupplementaryImpApplicationForm" readonly="true" style="border-color:black;text-align: center;"></html:text>			
		 </td>
		 </tr>
		</table>
		</td>
		<td align="right"><table><tr>								
								<td width="20%" align="right">
								<img src='<%=session.getAttribute("STUDENT_IMAGE")%>'  height="128" width="133" />
								</td>
							</tr></table></td>
		</tr>		
		<tr>
		<td colspan="3"></td>
		</tr>
		 <tr>
		 <td colspan="3">
		 
		 <table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;" rules="all">
		 <tr>
		 <td height="25%" align="center" width="2%">1</td>
		 <td align="left">Name of the Candidate																	
		 </td>
		 <td colspan="3" height="25%" style="text-transform: uppercase;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		  <tr>
		 <td align="center" >2</td>
		 <td align="left">Date of Birth</td>
		 <td align="left"><bean:write name="newSupplementaryImpApplicationForm" property="originalDob" /></td>
		 <td align="left">Gender</td>
		 <td align="left"><bean:write name="newSupplementaryImpApplicationForm" property="gender" /></td></tr>
		<tr>
		 <td height="25%" align="center">3</td>
		 <td align="left">Religion and Community														
		 </td>
		 <td align="left" colspan="3"><bean:write name="newSupplementaryImpApplicationForm" property="religion" /></td>
		 </tr>
		 <tr>
		 <td height="25%" align="center">4</td>
		 <td align="left">Whether belonging to SC/ST/OBC/OEC/FM/KPCR</td>
		<!--<logic:notEmpty name="newSupplementaryImpApplicationForm" property="feeConcessionCategory"><td align="left" colspan="3">YES, <bean:write name="newSupplementaryImpApplicationForm" property="feeConcessionCategory" />
		 </td></logic:notEmpty>
		 <logic:empty name="newSupplementaryImpApplicationForm" property="feeConcessionCategory"><td align="left" colspan="3">
		 </td></logic:empty>
		 --></tr>
		 <tr>
		 <td height="25%" align="center" >5</td>
		 <td align="left">Name of Father/Mother/Guardian with relationship
		 </td>
		 <td align="left" colspan="3"><bean:write name="newSupplementaryImpApplicationForm" property="careTaker" /></td>
		 </tr>
		  <tr>	 <td height="25%" align="center" >6</td>	 
		 <td height="25%" width="50%" style="text-align: left;vertical-align: top">Address for Communication</td>
		 <td colspan="3"><bean:write name="newSupplementaryImpApplicationForm" property="communicationAddress" />
		 Pin Code: <bean:write name="newSupplementaryImpApplicationForm" property="communicationAddressZipCode" />
		 , Phone: <bean:write name="newSupplementaryImpApplicationForm" property="communicationMobileNo" />
<!--		 , Email: <bean:write name="newSupplementaryImpApplicationForm" property="communicationEmail" />-->
		 </td>
		
		 </tr>		
		 <!--
		  <tr>
		 <td align="center" >1</td>
		 <td >Name of the Department</td>
		 <td colspan="2" style=" padding-left: 10px;"><bean:write name="newSupplementaryImpApplicationForm" property="courseDep" /></td>
		 </tr>
		 <tr>
		 <td align="center" >2</td>
		 <td >Name of the Programme</td>
		 <td colspan="2" style=" padding-left: 10px;"><bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 </tr>
		 <tr>
		 <td align="center" width="5%" >3</td>
		 <td width="35%" >Examination appearing for  (Regular/ Supplementary/ Improvement)</td>
		 <td colspan="2" style=" padding-left: 10px;"><bean:write name="newSupplementaryImpApplicationForm" property="examType" /></td>
		 </tr>
		
		 
		 
		 <tr>
		 <td height="25%" align="center" >5</td>
		 <td height="25%" >Address for Communication with Telephone Number</td>
		 <td height="25%" colspan="2" style=" padding-left: 10px;"><bean:write name="newSupplementaryImpApplicationForm" property="address" />
		 <bean:write name="newSupplementaryImpApplicationForm" property="mobileNo" />
		  &  <bean:write name="newSupplementaryImpApplicationForm" property="email" />
		 </td>
		 </tr>
		
		 <tr>
		 <td align="center" >7</td>
		 <td >Whether eligible for fee concession, If eligible, state the category</td>
		 <td width="15%" style=" padding-left: 10px;"><bean:write name="newSupplementaryImpApplicationForm" property="feeConcession" /></td>
		 <logic:equal value="yes" name="newSupplementaryImpApplicationForm" property="feeConcession" >
		 <td  style=" padding-left: 10px;">Category: <bean:write name="newSupplementaryImpApplicationForm" property="feeConcessionCategory" /></td>
		 </logic:equal>
		  <logic:notEqual value="yes" name="newSupplementaryImpApplicationForm" property="feeConcession" >
		 <td  style=" padding-left: 10px;">Category:....................................</td>
		 </logic:notEqual>
		 </tr>
		 
		  
		 <tr>
		 <td align="center" >8</td>
		 <td >Whether the attendance of the Candidate is satisfactory at the time of submitting the application/ shortage will be condoned.</td>
		  <td width="15%" style=" padding-left: 10px;">YES</td>
		 <td  style=" padding-left: 10px;">Applied for condonation<br>
		 (Specify):....................................<br></td>
		 </tr>
		 
		 
		 
		 --></table>
		 
		
		 </td>
		 
		 
		 
		 </tr>
		 
		 <tr>
		 <td  colspan="3">
		 <p align="center"><b>Courses for which the candidate is appearing</b></p>
		 <table width="100%" style="border-collapse: collapse;border: 1px solid balck;" border="1">
		 <tr style="border-collapse: collapse;border: 1px solid balck;">
		 <td width="15%" style="text-align: center;" colspan="3">Course Code & Title</td>		
		 </tr>
		 
		 <nested:notEmpty property="supSubjectList" name="newSupplementaryImpApplicationForm">
		 <nested:iterate property="supSubjectList" indexId="count" name="newSupplementaryImpApplicationForm">
		 
		 <tr>
		 <td style="font-size: 10pt;" width="2%"><nested:write  property="sectionName" /></td>
		 <td style="font-size: 10pt;" width="1%"><nested:write  property="subjectCode" /></td>		 
		 <td style="font-size: 10pt;"><nested:write  property="subjectName" /></td>
		</tr>
		 </nested:iterate>
		 </nested:notEmpty>
		 
		 
		 </table>
		 <!--  
		  * T-Theory , P-Practical , V-Viva Voce 
		 -->
		 </td>
		
		 <tr><td colspan="3">
		 <table width="100%" >
		 <tr><td colspan="3" align="left">I hereby declare that the entries made above are true to the best of my knowledge</td></tr>
		  <tr>		 
		 <td  width="35%" style="height: 40px;"><b>Date:	</b>				
		</td>
		<td width="30%" style="height: 40px;text-align: center;"><b>Signature of Student</b></td>
		<td width="30%" style="height: 40px;text-align: right;"><b>Signature of Faculty Advisor</b></td>
		 </tr>

		
		 
		 <tr><td colspan="3"><hr></hr></td></tr>
		<tr><td colspan="3" align="center"><b><font size="3pt">OFFICE USE</font></b></td></tr>		 
		<tr style="border-collapse: collapse;border:none;">
		 <td style="height: 40px;text-align: left;"><b>Office Superintendent</b></td>	
		 <td></td>	 
		 <td style="height: 40px;text-align: right;"><b>Principal</b> </td>
		 </tr>	 
		 <tr><td colspan="3" align="center"><b>(for SC/ST/OEC/OBC/FM/KPCR candidate only)</b></td></tr>	
		
		<tr><td colspan="3" align="left"><p>This is to certify that the candidate belongs to SC/ST/OBC/OEC/FM/KPCR and is appearing for the examination for the first/second consecutive chance and the examination fee will be reimbursed from the department concerned</td></tr>
		  <tr>		 
		 <td  width="35%" style="height: 40px;"><b>Date:</b>	
		</td>
		<td width="30%" style="height: 40px;text-align: left;" colspan="2"><b>(Office Seal)</b></td>
		 </tr>		 
		 <tr style="border-collapse: collapse;border:none;">
		 <td style="height: 40px;text-align: left;"><b>Exam Clerk</b></td>
		 <td style="height: 40px;text-align: left;"><b>Office Superintendent</b></td>		 
		 <td style="height: 40px;text-align: right;"><b>Principal</b> </td>
		 </tr>
		 <tr><td colspan="3"><hr></hr></td></tr>
		 <tr>
		 <td colspan="3"><b>Note: </b>First year students should enclose a copy of their  qualifying mark list</td>
		 </tr>
		 		
		 
		 
		 <!--  
		 <tr>
		 <td width="35%" style="font-size: 12px;">	<b>Signature of Class Teacher/Mentor</b> </td>
		 <td width="30%">	 </td>
		 <td width="35%" style="text-align: right;font-size: 12px;"><b>Seal & Signature of the H.O.D</b>	 </td>
		 
		 
		 </tr>
		 -->
		 
		
		
		 </table>
		</td>
		</tr>
		
					
		</table>
		
		<table width="100%" >
							
							<tr height="10px">
							<td colspan="3" style='font-family: "Times New Roman", Times, serif;font-size: 10pt;'>
							<logic:notEmpty name="newSupplementaryImpApplicationForm" property="description">
								<font size="3px"><c:out value="${newSupplementaryImpApplicationForm.description}" escapeXml="false"></c:out></font>
							</logic:notEmpty>
							</td>
							</tr>
	</table>	
	
	</nested:equal>
	</nested:equal>					
		
		</td>
		
		
							<td width="5" height="30" background="images/right.gif"></td>
							</tr>
							
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="regularAppAvailable">
								Application is not available.<br/>
							</nested:equal>
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
								You are not eligible for writing the Examination. Your attendance is below 75%.<br/>
							</nested:equal>
							<nested:equal value="yes" name="newSupplementaryImpApplicationForm" property="isPreviousExam">
								You are not eligible for writing the Examination. You did not register for previous exam application.
								<a href="javascript:previousExamLink();" > Click here to apply for previous exam.</a>
							</nested:equal>
							
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
				
				  <tr><td colspan="3"><hr></hr></td></tr>
		<nested:notEqual value="true" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
		 <tr align="center" >
		 <td colspan="3" style="height: 20px">
		 	<b>Examination Fee : </b> <bean:write name="newSupplementaryImpApplicationForm" property="theoryFee" /></td>
		 </tr>
		 <tr align="center"><td colspan="3" style="height: 20px">
		 	<b>University Fee :</b> <bean:write name="newSupplementaryImpApplicationForm" property="universityFee" /></td>
		 </tr>
		 <logic:equal value="false" property="eGrandStudent" name="newSupplementaryImpApplicationForm">
		 <tr align="center"><td colspan="3" style="height: 20px">
		 	<b>Late Fine Fee :</b> <bean:write name="newSupplementaryImpApplicationForm" property="onlineServiceChargeFees" /></td>
		 </tr></logic:equal>
		 <tr align="center"><td colspan="3" style="height: 20px">
		 	<b>Total Fee : </b><bean:write name="newSupplementaryImpApplicationForm" property="totalFee" /></td>
		 </tr>
	</nested:notEqual>		 
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
					<nested:equal value="true" property="extended" name="newSupplementaryImpApplicationForm">
						<tr>
						<td width="47%" height="35" colspan="3">
						<div align="center" style="font-weight: bold;font-size: 12px;color: red"><B>Late submission fee: <bean:write name="newSupplementaryImpApplicationForm" property="fineFees"/></B>
						
						</div>
						</td>
						</tr>
					</nested:equal>
						
						<tr>
							<td width="47%" height="35">
							<div align="right">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="regularAppAvailable">
							<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="displayButton">
							<html:button property=""
								styleClass="btnbg" value="Continue"
								onclick="submitApplication()"></html:button>
							<!--<html:button property=""
								styleClass="btnbg" value="Print Challan" 
								onclick="printChallanApplication()"></html:button>
								&nbsp; &nbsp; 
							
							
							-->
							</nested:equal>
							</nested:equal>
							</nested:equal>
							
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="challanButton">
							<nested:notEqual value="true" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
							
								<html:button property=""
								styleClass="btnbg" value="Pay Online"
								onclick="onlinePay()"></html:button>
								
								</nested:notEqual>
							</nested:equal>
							
							
							<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="printSupplementary">
								<html:button property=""
								styleClass="btnbg" value="Print Application"
								onclick="printApplication()"></html:button>
								
							</nested:equal>
							
							
							</div>
							</td>
							
							
							<td width="1%"></td>
							<td width="46%">
							
							<html:button property=""
								styleClass="btnbg" value="Close"
								onclick="cancelAction()"></html:button>
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
var print = "<c:out value='${newSupplementaryImpApplicationForm.printSupplementary}'/>";
if(print.length != 0 && print == "true") {
	var url ="newSupplementaryImpApp.do?method=showPrintDetails";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}
</script>