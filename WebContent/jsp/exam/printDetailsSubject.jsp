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
</head>
<body>
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName"
		value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="4" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />
	
		
		<table width="80%" border="0" style="font-size: 13px; font-family: serif;">
		<tr>
		
		<td align="center" colspan="3">
		<img src="images/header-logo.png" alt="Logo not available" height="100" width="300" ></img>
		</td>
		</tr>
		<tr>
		<td colspan="3" style="text-align: center; font-size: 16px;">
		Supplementary Examination Application Form 
		</td>
		</tr>
		<tr>
		<td></td>
		<td style="text-align: right;font-size: 12px;">Application No:</td>
		<td style="text-align: left;font-size: 12px;"><nested:write name="newSupplementaryImpApplicationForm"  property="applicationNumber"></nested:write></td>
		</tr>
		<tr>
		<td></td>
		<td style="text-align: right;font-size: 12px;">Date:</td>
		<td style="text-align: left;font-size: 12px;"><nested:write name="newSupplementaryImpApplicationForm"  property="dateOfApplication"></nested:write></td>
		</tr>
		<nested:iterate property="mainList" indexId="count2"  name="newSupplementaryImpApplicationForm">
		  <tr>
		 <td width="200">Student Name :</td>
		 <td><nested:write name="newSupplementaryImpApplicationForm"
										 property="nameOfStudent" /></td>
		 <td width="60"> </td>
		 
		 
		 </tr>
		 <tr>
		 <td>Register No/Exam Number:</td>
		 <td><nested:write name="newSupplementaryImpApplicationForm"
										 property="registerNo" /></td>
		 
		 </tr>
		
		
		
		 <tr>
		 <td width="100">Course :</td>
		 <td><nested:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 <td width="60"> </td>
		 
		 
		 </tr>
		 <tr>
		 <td width="100">Class :</td>
		 <td> <nested:write name="newSupplementaryImpApplicationForm" property="className"/></td>
		 <td width="60"> </td>
		 
		 
		 </tr>
		  <tr>
		 <td width="100">Exam Name :</td>
		 <td><nested:write property="examName"></nested:write></td>
		 <td width="60"> </td>
		 
		 
		 </tr>
		 <tr>
		 <td colspan="3">
		 <p>Indicates the paper being taken for the Examination</p>
		 <table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;font-size: 12px;">
		 <tr style="border-collapse: collapse;border: 1px solid balck;">
		 <td style="text-align: center;">SEMISTER</td>
		 <td style="text-align: center;">NAME OF THE SUBJECT</td>
		 <td style="text-align: center;">PAPER TYPE</td>
		  <td style="text-align: center;">EXAM TYPE</td>
		 </tr>
		 <nested:notEmpty property="examList">
		 <nested:iterate property="examList" indexId="count1">
		  <nested:notEmpty property="toList">
		 <nested:iterate property="toList" indexId="count">
		 <nested:equal value="true" property="tempChecked">
		 <tr>
		 <td style="text-transform: uppercase; text-align: center;"><nested:write  property="schemeNo" /></td>
		 <td style="text-transform: uppercase;padding-left: 10px;"><nested:write  property="subjectName" /></td>
		 <td style="text-align: center;">
		 <nested:equal value="true" property="tempChecked"> Theory</nested:equal>
		  
		   <td style="text-align: center;">
		 <nested:equal value="true" property="isESE">ESE</nested:equal>
		  <nested:equal value="true" property="ciaExam"> CIA</nested:equal></td>
		 
		 </tr>
		 
		 </nested:equal>
		  <nested:equal value="true" property="tempPracticalChecked">
		 <tr>
		 <td style="text-transform: uppercase; text-align: center;"><nested:write  property="schemeNo" /></td>
		 <td style="text-transform: uppercase;padding-left: 10px;"><nested:write  property="subjectName" /></td>
		 <td style="text-align: center;">
		  <nested:equal value="true" property="tempPracticalChecked"> Practical</nested:equal></td>
		  
		   <td style="text-align: center;">
		 <nested:equal value="true" property="isESE">ESE</nested:equal>
		  <nested:equal value="true" property="ciaExam"> CIA</nested:equal></td>
		 
		 </tr>
		 
		 </nested:equal>
		 
		 
		 
		 </nested:iterate>
		 </nested:notEmpty>
		 
		 </nested:iterate>
		 
		 </nested:notEmpty>
		 
		 </table>
		 
		 
		 </td>
		 
		 
		 
		 </tr>
		 <tr>
		 <td>	 </td>
		 <td style="height: 100px;text-align: right;font-size: 12px;">Signature of Student	 </td>
		 <td>	 </td>
		 
		 
		 </tr>
		</nested:iterate>
		
		
		</table>
		
		

	
	
</html:form>


</body>
</html>
<script type="text/javascript"> window.print();</script>