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
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/styles.css" rel="stylesheet" type="text/css">
<STYLE TYPE="text/css">
td{font-size: 10pt;}
</STYLE>
</head>
<body>
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="4" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />
	
	<nested:iterate property="mainList" indexId="count2"  name="newSupplementaryImpApplicationForm">
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
		 <td  style="border:none"> <div align="left">Rs: .......................</div>
		 </td>
		 </tr>
		<tr>
		 <td  style="border:none"> <div align="left">Challan No: ...............</div>
		 </td>
		 </tr>
		<tr>
		 <td  style="border:none"> <div align="left">Date: .....................</div>
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
		 : <html:text property="regNo" name="newSupplementaryImpApplicationForm" readonly="true" style="border-color:black;text-align: center;"></html:text>			
		 </td>
		 </tr>
		</table>
<!--		,<bean:write name="newSupplementaryImpApplicationForm" property="month" />-<bean:write name="newSupplementaryImpApplicationForm" property="year" /> -->
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
		 <td colspan="3" height="25%" style="text-transform: uppercase;font-size: 8pt;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		  <tr>
		 <td align="center" >2</td>
		 <td align="left">Date of Birth</td>
		 <td align="left"><bean:write name="newSupplementaryImpApplicationForm" property="originalDob" /></td>
		 <td align="left">Gender</td>
		 <td align="left" style="font-size: 8pt;"><bean:write name="newSupplementaryImpApplicationForm" property="gender" /></td></tr>
		<tr>
		 <td height="25%" align="center">3</td>
		 <td align="left">Religion and Community													
		 </td>
		 <td align="left" colspan="3" style="font-size: 8pt;"><bean:write name="newSupplementaryImpApplicationForm" property="religion" /></td>
		 </tr>
		 <tr>
		 <td height="25%" align="center">4</td>
		 <td align="left">Whether belonging to SC/ST/OBC/OEC/FM/KPCR</td>
		 <td align="left" colspan="3" style="font-size: 8pt;" >
		 	<logic:equal value="YES" name="newSupplementaryImpApplicationForm" property="feeConcession">Yes</logic:equal>		 	
		 	<logic:equal value="Yes" name="newSupplementaryImpApplicationForm" property="feeConcession">Yes</logic:equal>
		 	<logic:equal value="NO" name="newSupplementaryImpApplicationForm" property="feeConcession">No</logic:equal>
		 	<logic:equal value="No" name="newSupplementaryImpApplicationForm" property="feeConcession">No</logic:equal>
		 </td>
		 </tr>
		 <tr>
		 <td height="25%" align="center" >5</td>
		 <td align="left">Name of Father/Mother/Guardian with relationship
		 </td>
		 <td align="left" colspan="3" style="font-size: 8pt;"><bean:write name="newSupplementaryImpApplicationForm" property="careTaker" /></td>
		 </tr>
		  <tr>	 <td height="25%" align="center" >6</td>	 
		 <td height="25%" width="50%" style="text-align: left;vertical-align: top;font-size: 8pt;">Address for Communication</td>
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
		 <td width="10%" style="text-align: center;" colspan="4">Course Code & Title</td>		
		 </tr>	
		
		<nested:notEmpty property="examList">
		 <nested:iterate property="examList" indexId="count1">
		 <nested:notEmpty property="toList">
		 <nested:iterate property="toList" indexId="count">
		 
		<nested:equal value="true" property="tempChecked">
		 <tr>
		 
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;" width="2%"><nested:write  property="sectionName" /></td>
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;" width="1%"><nested:write  property="subjectCode" /></td>
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;"><nested:write  property="subjectName" /></td>
		 </tr>
		 </nested:equal>
		 <nested:equal value="true" property="tempPracticalChecked">
		 <tr>
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;" width="2%"><nested:write  property="sectionName" /></td>
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;" width="1%"><nested:write  property="subjectCode" /></td>
		 <td style="text-transform: uppercase; text-align: left;font-size: 8pt;"><nested:write  property="subjectName" /></td>
		 </tr>
		 </nested:equal>
		 
		 </nested:iterate>
		 </nested:notEmpty>
		 </nested:iterate>
		 </nested:notEmpty>
		 
		 
		 </table>
		 <!--  
		  * T-Theory , P-Practical , V-Viva Voce 
		 -->
		 </td>
		</tr>
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
		<tr><td style="height: 40px;text-align: left;"><b>Office Superintendent</b></td>
		<td></td>		 
		 <td style="height: 40px;text-align: right;"><b>Principal</b> </td> </tr>		 
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
		 </table>
		</td>
		</tr>
		</table>
		<table width="100%" >							
				<tr height="10px">
					<td colspan="3" class="row-print-desc">
					<logic:notEmpty name="newSupplementaryImpApplicationForm" property="description">
						<c:out value="${newSupplementaryImpApplicationForm.description}" escapeXml="false"></c:out>
					</logic:notEmpty>
					</td>
				</tr>
			</table>
</nested:iterate>
</html:form>
</body>
</html>
<script type="text/javascript"> window.print();</script>
