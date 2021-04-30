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
<style>
.block{
	display: inline-block;
  	width: 150px;
  	height: 50px;
  	padding: 10px;
  	border-collapse: collapse;
  	border: 1px solid black; 
}
.blocksec{
	display: inline-block;
  	width: 200px;
  	height: 100px;
  	padding: 10px;
  	border-collapse: collapse;
  	border: 1px solid black; 
}
</style>
<script type="text/javascript">
function cancelAction(){
	document.location.href="StudentLoginAction.do?method=returnHomePage";
		}
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="4" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />
	
	<nested:iterate property="mainList" indexId="count2"  name="newSupplementaryImpApplicationForm">
		
		<table width="100%" >
					
							
		<tr>
		
		<td width="100%" align="center" >
		<img src="images/header-logo.png" alt="Logo not available" height="90" width="800" ></img>
		</td>
		
		</tr>
		</table>
		
		<tr>
		<td>
		<table style="padding-bottom: 30px;">
		<tr>
			<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<table class="block" width="30%"  border="1" style="border-collapse: collapse;border: 1px solid balck;font-size: 12px;" rules="none">
				<tr><td>
					<b>Please attach copies of mark list of courses for which revaluation is required. </b></td></tr>
					
				</table>
			</td>
			
			<td width="60%"></td>
			<td align="right">
				<table class="blocksec" width="80%" align="right"  border="1" style="border-collapse: collapse;border: 1px solid balck;font-size: 13px;" rules="none">
				 <tr>
					 <td  style="text-align: center;"><b>Details of Fee Remitted</b></td>
				 </tr>
				<tr>
				 <td> <div align="left">Amount Rs :.........</div></td>
				</tr>
				<tr>
				 <td> <div align="left">Mode of Remittance :..........</div></td>
				</tr>
				<tr>
				  <td> <div align="left">Fee Receipt No :..........</div></td>
				  </tr>
				  <tr>
				  <td> <div align="left">Date of Payment :..........</div></td>
				  </tr>
				</table>
			</td>
			
		</tr>
		</table>
		</td>
		</tr>
		
		
		<tr height="20px"><td>Sl no.</td></tr>
								
		
		
		
		
		
		<tr>
		<td style="text-align: center;" colspan="2"><table align="center">
				<tr><td align="right" style="text-align: center;">
				 Application For  Scrutiny / Revaluation of Answer Books of <b> <bean:write name="newSupplementaryImpApplicationForm" property="examName" /></b>
				</td></tr>
				
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
		 <td height="25%" align="center" width="2%">1</td>
		 <td align="left">Name of the Candidate																	
		 </td>
		 <td colspan="3" height="25%" style="text-transform: uppercase;font-size: 10pt;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		 <tr>
		 <td height="25%" align="center" width="2%">2</td>
		 <td align="left">Name of the Examination																
		 </td>
		 <td colspan="3" height="25%" style="font-size: 10pt;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="examName" /></td>
		 </tr>
		 <tr>
		 <td height="25%" align="center" width="2%">3</td>
		 <td align="left">Register	No															
		 </td>
		 <td colspan="3" height="25%" style="font-size: 10pt;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="registerNo" /></td>
		 </tr>
		  <tr>
		 <td height="25%" align="center" width="2%">4</td>
		 <td align="left">Course															
		 </td>
		 <td colspan="3" height="25%" style="font-size: 10pt;" align="left"><bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 </tr>
		  
		  <tr>	 <td height="25%" align="center" >6</td>	 
		 <td height="25%" width="50%" style="text-align: left;vertical-align: top">Address for Communication</td>
		 <td colspan="3" style="font-size: 10pt;" ><bean:write name="newSupplementaryImpApplicationForm" property="communicationAddress" />
		 Pin Code: <bean:write name="newSupplementaryImpApplicationForm" property="communicationAddressZipCode" />
		 , Phone: <bean:write name="newSupplementaryImpApplicationForm" property="communicationMobileNo" />
<!--		 , Email: <bean:write name="newSupplementaryImpApplicationForm" property="communicationEmail" />-->
		 </td>
		
		 </tr>
		 <tr>
		 <td align="center" >7</td>
		 <td align="left">Date of Birth</td>
		 <td align="left"><bean:write name="newSupplementaryImpApplicationForm" property="originalDob" /></td>
		 </tr>	
		 <tr>
		 <td align="center" >8</td>
		 <td >Purpose of the application</td>
		 <td colspan="3" style="text-transform: uppercase;padding-left: 10px;">
		 <c:if test="${newSupplementaryImpApplicationForm.isRevaluation=='on'}" >
		 Revaluation
		 </c:if>
		  <c:if test="${newSupplementaryImpApplicationForm.isScrutiny=='on'}" >
		 Scrutiny
		 </c:if>

		 
		 </td>
		 
		 </tr>	
		 </table>
		 
		
		 </td>
		 
		 
		 
		 </tr>

		 
		 
		 
		 <tr>
		 <td  colspan="3"><br></br>
		 <p style="font-size: 12px">8. Write down the details of the papers for <c:if test="${newSupplementaryImpApplicationForm.isRevaluation=='revaluation'}" >
		 Revaluation
		 </c:if>
		  <c:if test="${newSupplementaryImpApplicationForm.isRevaluation=='scrutiny'}" >
		 Scrutiny
		 </c:if>
</p>
		 <table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;font-size: 12px;">
		 <tr style="border-collapse: collapse;border: 1px solid balck;">
		 <td width="5%" style="text-align: center;">S.I.NO</td>
		 <td width="20%" style="text-align: center;">Course Code</td>
		 <td width="15%" style="text-align: center;">Course Title</td>
		 <td width="15%" style="text-align: center;">Marks Obtained</td>
		 <td width="15%" style="text-align: center;">Max. Marks</td>
		 <%-- 
		 <td width="10%" style="text-align: center;">Marks Scored</td>
		 --%>
		 </tr>
		 <%
		 int total=0;
		 int subTotal=0;
		 int theotyTotal=0;
		 int i=1;
		 %>
		 <nested:notEmpty property="examList">
		 <nested:iterate property="examList" indexId="count1">
		 <nested:notEmpty property="toList">
		 <nested:iterate property="toList" indexId="count">
		<nested:equal value="true" property="tempChecked">
		
		 <tr>
		 <td style="text-transform: uppercase; text-align: center;"><%= i++ %></td>
		 <td style="text-transform: uppercase; text-align: center;"><nested:write  property="subjectCode" /></td>
		 <td style="text-align: center;"><nested:write  property="subjectName" /></td>
		 <td style="text-align: center;"><nested:write  property="marks" /></td>
		 <td style="text-align: center;"><nested:write  property="maxMarks" /></td>
		 <%-- 
		 <td ><nested:write  property="marks" /></td>
		 --%>
		 </tr>
		  <%
		
		 	subTotal++;
		 
		 %>
		 </nested:equal>
		 </nested:iterate>
		 </nested:notEmpty>
		 </nested:iterate>
		 </nested:notEmpty>
		
		 
		 </table>
		 
		  
		 </td>
		 
		 
		 
		 </tr>
		 <tr><td colspan="3">
		 <table width="100%" >
		 <tr><td colspan="3" align="center"><b><font size="3pt">DECLARATION OF CANDIDATE</font></b></td></tr>	
		 <tr><td colspan="3" align="center">I have gone through the instructions to the candidate printed overleaf. The details furnished are true to the best of my knowledge. </td></tr>
		  <tr>		 
		 <td  width="35%" style="height: 40px;"><b> Date:	</b>				
		</td>
		</tr>
		<tr>
		<td width="30%" style="height: 40px;"><b>Place:  </b></td>
		<td width="60%" style="height: 40px;text-align: right;"><b>Signature of Candidate</b></td>
		 </tr>
		 	 
		

		 </table>
		</td>
		</tr>
			
		
		
		
		

</nested:iterate>	
	
</html:form>


</body>
</html>
<script type="text/javascript"> window.print();</script>
