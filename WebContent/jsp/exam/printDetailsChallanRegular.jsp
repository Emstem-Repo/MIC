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
<style type="text/css" media="print">
  @page { size: landscape; }
</style>
</head>
<body>
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="4" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />
	
	
	<table width="100%" >
	
	<tr>
	<td width="32%"></td>
	<td width="2%"></td>
	<td width="32%"></td>
	<td width="2%"></td>
	<td width="32%" style="font-size: 12px;" align="right"><bean:write name="newSupplementaryImpApplicationForm" property="date" /></td>
	</tr>
	
	<tr>
	
	
	<td width="32%">
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr><td colspan="2">
		<div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>Bank Copy</b></div></td>
		</tr>			
			
		<tr>
		<td align="center" colspan="2" style="text-align: center; font-size: 14px;"><b>Examination Fee Payment Voucher</b></td>
		</tr>
							
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="70" width="300" ></img>
		</td>
		
		</tr>
		
		
		<tr><td colspan="2" >
		Challan No: <bean:write name="newSupplementaryImpApplicationForm" property="journalNo"/>
		</td></tr>
		<tr>
		<td colspan="2" >
		Name of Examination: <bean:write name="newSupplementaryImpApplicationForm" property="examName" /> 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c  Name: MAR IVANIOS COLLEGE 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c No: <bean:message key="knowledgepro.admin.tieupbank.accountno"/> 
		</td>
		</tr>
		
		 <tr>
		 <td colspan="2">Name of the Candidate:
			<bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		 
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Register No:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="studentObj.registerNo" /></td>
		 
		 </tr>
		
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Course:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 
		 </tr>			
				
		<tr>
		<td width="50%">
		Examination Fee: 
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td width="50%">
		Fine [ if any]  
		</td>
		<td >
		  
		</td>
		</tr>
		
		<tr>
		
		<td align="right" width="50%">
		<b>Total</b>
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td colspan="2" >
		[Rs.In words]: <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmountWords"/> only
		<br></br>  
		</td>
		</tr>
		 
		<tr height="30px">
		<td colspan="2" style="font-size: 14px;">
		<b>Signature of Remitter:</b>  
		</td>
		</tr>
		 
		<tr>
		<td colspan="2" style="text-align: center; font-size: 14px;">
		For Office  Use  
		</td>
		</tr>
		
		<tr>
		<td colspan="2" style="font-size: 14px;">
		<b>Office Seal and Signature</b>  
		<br></br>  
		<br></br>  
		
		NB:Branch should mention DC Code on the student copy.
		</td>
		</tr>
		
		
		</table>
		
		
	</td>
	
	
	<td width="2%">
	
	
	</td>
	
	
	<td width="32%">
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr>
		<td colspan="2">
		<div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>Candidate Copy</b></div></td>
		</tr>			
			
		<tr>
		<td align="center" colspan="2" style="text-align: center; font-size: 14px;"><b>Examination Fee Payment Voucher</b></td>
		</tr>
							
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="70" width="300" ></img>
		</td>
		
		</tr>
		
		
		<tr><td colspan="2" >
		Challan No: <bean:write name="newSupplementaryImpApplicationForm" property="journalNo"/>
		</td></tr>
		<tr>
		<td colspan="2" >
		Name of Examination: <bean:write name="newSupplementaryImpApplicationForm" property="examName" /> 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c  Name: MAR IVANIOS COLLEGE 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c No: <bean:message key="knowledgepro.admin.tieupbank.accountno"/> 
		</td>
		</tr>
		
		 <tr>
		 <td colspan="2">Name of the Candidate:
			<bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		 
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Register No:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="studentObj.registerNo" /></td>
		 
		 </tr>
		
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Course:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 
		 </tr>			
				
		<tr>
		<td width="50%">
		Examination Fee:  
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td width="50%">
		Fine [ if any]  
		</td>
		<td >
		  
		</td>
		</tr>
		
		<tr>
		
		<td align="right" width="50%">
		<b>Total</b>
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td colspan="2" >
		[Rs.In words]: <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmountWords"/> only
		<br></br>  
		</td>
		</tr>
		 
		<tr height="30px">
		<td colspan="2" style="font-size: 14px;">
		<b>Signature of Remitter:</b>  
		</td>
		</tr>
		 
		<tr>
		<td colspan="2" style="text-align: center; font-size: 14px;">
		For Office  Use  
		</td>
		</tr>
		
		<tr>
		<td colspan="2" style="font-size: 14px;">
		<b>Office Seal and Signature</b>  
		<br></br>  
		<br></br>  
		
		NB:Branch should mention DC Code on the student copy.
		</td>
		</tr>
		
		
		</table>
		
		
	</td>
	
	
	<td width="2%">
	
	</td>
	
	
	<td width="32%">
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr>
		<td colspan="2">
		<div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>Office Copy</b></div></td>
		</tr>			
			
		<tr>
		<td align="center" colspan="2" style="text-align: center; font-size: 14px;"><b>Examination Fee Payment Voucher</b></td>
		</tr>
							
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="70" width="300" ></img>
		</td>
		
		</tr>
		
		
		<tr><td colspan="2" >
		Challan No: <bean:write name="newSupplementaryImpApplicationForm" property="journalNo"/>
		</td></tr>
		<tr>
		<td colspan="2" >
		Name of Examination: <bean:write name="newSupplementaryImpApplicationForm" property="examName" /> 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c  Name: MAR IVANIOS COLLEGE 
		</td>
		</tr>
		<tr>
		<td colspan="2" >
		A/c No: <bean:message key="knowledgepro.admin.tieupbank.accountno"/> 
		</td>
		</tr>
		
		 <tr>
		 <td colspan="2">Name of the Candidate:
			<bean:write name="newSupplementaryImpApplicationForm" property="nameOfStudent" /></td>
		 </tr>
		 
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Register No:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="studentObj.registerNo" /></td>
		 
		 </tr>
		
		 <tr>
		 <td style="font-size: 14px;" colspan="2" width="40%">Course:
			
		 <bean:write name="newSupplementaryImpApplicationForm" property="courseName" /></td>
		 
		 </tr>			
				
		<tr>
		<td width="50%">
		Examination Fee:  
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td width="50%">
		Fine [ if any]  
		</td>
		<td >
		  
		</td>
		</tr>
		
		<tr>
		
		<td align="right" width="50%">
		<b>Total</b>
		</td>
		<td >
		  <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td colspan="2" >
		[Rs.In words]: <bean:write name="newSupplementaryImpApplicationForm" property="applicationAmountWords"/> only
		<br></br>  
		</td>
		</tr>
		 
		<tr height="30px">
		<td colspan="2" style="font-size: 14px;">
		<b>Signature of Remitter:</b>  
		</td>
		</tr>
		 
		<tr>
		<td colspan="2" style="text-align: center; font-size: 14px;">
		For Office  Use  
		</td>
		</tr>
		
		<tr height="30px">
		<td colspan="2" style="font-size: 14px;">
		<b>Office Seal and Signature</b>  
		<br></br>  
		<br></br>  
		
		NB:Branch should mention DC Code on the student copy.
		</td>
		</tr>
		
		
		</table>
		
	
			
	</td>
	
	
	</tr>
	<tr>
	<td width="32%"></td>
	<td width="2%"></td>
	<td width="32%"></td>
	<td width="2%"></td>
	<td width="32%" style="font-size: 10px;">This copy to be attached along with the application form</td>
	</tr>
	</table>
		
		

	
	
</html:form>


</body>
</html>
<script type="text/javascript"> window.print();</script>
