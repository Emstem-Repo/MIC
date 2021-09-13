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
<%@page import="java.util.Date" %>

<%@page import="java.text.SimpleDateFormat"%><link rel="stylesheet" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
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
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="3" />
	<logic:notEmpty name="newSupplementaryImpApplicationForm" property="stuList">
		<logic:iterate id="student" name="newSupplementaryImpApplicationForm" property="stuList" indexId="count">

		<logic:equal value="on" name="student" property="isChecked">
		
		<c:if test="${count>0}">
		<p style="page-break-after:always;"> </p>
		</c:if>
		<table width="100%" border="0">

		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
	
	
				
				<tr>
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
				
		<table width="100%" cellspacing="1" cellpadding="2" style='font-family: "Times New Roman", Times, serif;font-size: 9pt;'>
							
		<tr>
		
		<td width="70%" align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="90" width="450" ></img>
		</td>
		<td>
		
		<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
		 
		 
		 <tr>
		 <td align="center" style="border:none">Details of Fee Paid
		 </td>
		 </tr>
		<tr>
		<% SimpleDateFormat df = new SimpleDateFormat();
			String curDate = df.format(new Date());
		%>
		 <td  style="border:none"> <div align="left">Date: <%=curDate %></div>
		 </td>
		 </tr>
		 
		 		<tr>

		 <td  style="border:none"> <div align="left">Fees: &nbsp; -</div>
		 </td>
		 </tr>
		</table>
		
		
		</td>
		</tr>
		<tr>
		<td style="text-align: center;" colspan="2"><table>
		<tr><td align="center">
		<b> EXAM REGISTRATION FORM <br><bean:write name="newSupplementaryImpApplicationForm" property="examName" />, <bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></b>
		</td></tr>
		<tr>
		 <td align="center" style="padding-left: 70px;" colspan="2" width="40%"><b>Permanent Register Number</b>
		 <bean:define id="reg" name="student" property="registerNo"></bean:define>
		 : <html:text property="registerNo" name="student" readonly="true" style="border-color:black;text-align: center;"></html:text>			
		 </td>
		 </tr>
		</table>
		</td>
				<td align="right" width="20%">
							<table>
							<tr>
							<td width="20%" align="right">
								<img src='images/StudentPhotos/<c:out value="${student.id}"/>.jpg?<%=System.currentTimeMillis() %>'  height="128" width="133" />
								</td>
							
							</tr>
							</table>
							</td>
		</tr>		
		<tr>
		<td colspan="3"></td>
		</tr>
		 <tr>
		 <td colspan="3">
		 
		 <table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;" rules="all">
		 <tr>
		 <td height="20%" align="center" width="2%">1</td>
		 <td align="left">Name of the Candidate																	
		 </td>
		 <td colspan="3" height="20%" style="text-transform: uppercase;" align="left"><bean:write name="student" property="studentName" /></td>
		 </tr>
		  <tr>
		 <td align="center" >2</td>
		 <td align="left">Date of Birth</td>
		 <td align="left"><bean:write name="student" property="dob" /></td>
		 <td align="left">Gender</td>
		 <td align="left"><bean:write name="student" property="gender" /></td></tr>
		<tr>
		 <td height="20%" align="center">3</td>
		 <td align="left">Religion and Community														
		 </td>
		 <td align="left" colspan="3"><bean:write name="student" property="religion" /></td>
		 </tr>
		 <tr>
		 <td height="20%" align="center">4</td>
		 <td align="left">Whether belonging to SC/ST/OBC/OEC/FM/KPCR</td>
		<!--<logic:notEmpty name="newSupplementaryImpApplicationForm" property="feeConcessionCategory"><td align="left" colspan="3">YES, <bean:write name="newSupplementaryImpApplicationForm" property="feeConcessionCategory" />
		 </td></logic:notEmpty>
		 <logic:empty name="newSupplementaryImpApplicationForm" property="feeConcessionCategory"><td align="left" colspan="3">
		 </td></logic:empty>
		 --></tr>
		 <tr>
		 <td height="20%" align="center" >5</td>
		 <td align="left">Name of Father/Mother/Guardian with relationship
		 </td>
		 <td align="left" colspan="3"><bean:write name="student" property="careTaker" /></td>
		 </tr>
		  <tr>	 <td height="25%" align="center" >6</td>	 
		 <td height="20%" width="50%" style="text-align: left;vertical-align: top">Address for Communication</td>
		 <td colspan="3"><bean:write name="student" property="communicationAddress" />
		 Pin Code: <bean:write name="student" property="communicationAddressZipCode" />
		 , Phone: <bean:write name="student" property="communicationMobileNo" />
		 </td>
		
		 </tr>		
</table>
		 
		
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
		 </td></tr>
		
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
		<tr><td colspan="3" align="center"><b><font size="2pt">OFFICE USE</font></b></td></tr>		 
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

		
		 </table>
		</td>
		</tr>
		
					
		</table>
	
	
	</nested:equal>
		
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
				<tr>
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
		
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td valign="top" class="news">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td width="0" background="images/st_TcenterD.gif"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</logic:equal>
	
	</logic:iterate>
	
	</logic:notEmpty>

</html:form>

