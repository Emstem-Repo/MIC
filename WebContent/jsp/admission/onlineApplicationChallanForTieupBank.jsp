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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/styles.css" rel="stylesheet" type="text/css">
<style type="text/css" media="print">
  @page { size: landscape; }
 
</style>
<style>
 .label{
  font-size: 14px;
  font-weight: bold;
  }
</style>
</head>
<body>
<html:form action="/uniqueIdRegistration">
	<html:hidden property="formName" value="uniqueIdRegistrationForm" />
	<html:hidden property="pageType" value="" />
	<html:hidden property="method" styleId="method" value="forwardChallanTemplate" />
	
	
	<table width="100%" >
	
	<tr>
	<td width="30%"></td>
	<td width="2%"></td>
	<td width="30%"></td>
	<td width="2%"></td>
	<td width="36%" style="font-size: 12px;" align="right"></td>
	</tr>
	
	<tr>
	
	
	<td width="30%" align="center">
	<b>SIB FEE MODEL 1</b>
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr>
	     <td colspan="3">
	     <div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>College Copy</b></div>
	     
	     </td>
		</tr>	
		<tr>
		<td colspan="3"> <div style="float: left;">Branch...........................</div> <div style="float: right;">    Date...........................</div> </td>
		
		</tr>		
			
		<tr>
		<td colspan="2" class="label">
		A/c No: <%= CMSConstants.TIEUPBANK_ACNO %> 
		</td>
		</tr>
		<tr>
		<td colspan="2" class="label">
		A/c  Name: MAR IVANIOS COLLEGE 
		</td>
		</tr>					
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="50" width="350" ></img>
		</td>
		
		</tr>
		
		<tr>
		<td colspan="2" class="label">
		SIB Fee Institution Code : <b>452</b>
		</td>
		</tr>		
				
		<tr>
		<logic:equal value="false" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="4" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="5" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		
		<tr><td>Category Fee :&nbsp;Rs. <bean:write name="uniqueIdRegistrationForm" property="categoryAmount"/></td></tr>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<tr><td>Management Fee :&nbsp;Rs. 500</td></tr>
		</logic:equal>
		<tr><td>Total : Rs.&nbsp;<bean:write name="uniqueIdRegistrationForm" property="applicationAmount"/></td></tr>
		<tr><td>In words... Rupees<bean:write name="uniqueIdRegistrationForm" property="applicationAmountWords"/> only</tr>
		  
		</tr>
		
		<tr>
		<td class="label">Reference No.</td>
		<td><bean:write name="uniqueIdRegistrationForm" property="challanRefNo"/></td>
		</tr>
		
		 <tr>
		 <td class="label">Name of the Student:</td>
		 <td><bean:write name="uniqueIdRegistrationForm" property="applicantName"/></td>
		 </tr>
		 
		<tr>
		<td height="50" class="label">
		<b>Signature of Remitter:</b>  
		</td>
		<td></td>
		</tr>
		<tr>
		<td class="label">
		<b>Mob. Phone No </b>  
		</td>
		<td></td>
		</tr>
		 
		<tr>
		<td height="50" class="label"><b>Entered By </b>  </td>
		<td>Authorised Signature</td>
		</tr>
				
		</table>
		
		
	</td>
	
	
	<td width="2%">
	
	
	</td>
	
	
	
	
	
	
	<td width="30%" align="center">
	<b>SIB FEE MODEL 1</b>
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr>
	     <td colspan="3">
	     <div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>Student's Copy</b></div>
	     
	     </td>
		</tr>	
		<tr>
		<td colspan="3"> <div style="float: left;">Branch...........................</div> <div style="float: right;">    Date...........................</div> </td>
		
		</tr>		
			
		<tr>
		<td colspan="2" class="label">
		A/c No: <%= CMSConstants.TIEUPBANK_ACNO %> 
		</td>
		
		</tr>
		<tr>
		<td colspan="2" class="label">
		A/c  Name:  MAR IVANIOS COLLEGE 
		</td>
		</tr>					
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="50" width="300" ></img>
		</td>
		
		</tr>
		
		<tr>
		<td colspan="2" class="label">
		SIB Fee Institution Code : <b>452</b>
		</td>
		</tr>		
		 		
		<tr>
		<logic:equal value="false" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="4" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="5" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		
		<tr><td>Category Fee :&nbsp;Rs. <bean:write name="uniqueIdRegistrationForm" property="categoryAmount"/></td></tr>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<tr><td>Management Fee :&nbsp;Rs. 500</td></tr>
		</logic:equal>
		<tr><td>Total : Rs.&nbsp;<bean:write name="uniqueIdRegistrationForm" property="applicationAmount"/></td></tr>
		<tr><td>In words... Rupees<bean:write name="uniqueIdRegistrationForm" property="applicationAmountWords"/> only</tr>
		  
		</tr>
		
		<tr>
		<td class="label">Reference No.</td>
		<td><bean:write name="uniqueIdRegistrationForm" property="challanRefNo"/></td>
		</tr>
		
		 <tr>
		 <td class="label">Name of the Student:</td>
		 <td><bean:write name="uniqueIdRegistrationForm" property="applicantName"/></td>
		 </tr>
		 
		<tr>
		<td height="50" class="label">
		<b>Signature of Remitter:</b>  
		</td>
		<td></td>
		</tr>
		<tr>
		<td class="label">
		<b>Mob. Phone No </b>  
		</td>
		<td></td>
		</tr>
		 
		<tr>
		<td height="50" class="label"><b>Entered By </b>  </td>
		<td>Authorised Signature</td>
		</tr>
				
		</table>
		
		
	</td>
	
	
	<td width="2%">
	
	</td>
	
	
	
	
	<td width="36%" height="100%" align="center">
	
	<b>SIB FEE MODEL 1</b>
	
	<table width="100%" border="1" style="border-collapse: collapse;border: 1px solid balck;">
			
		<tr>
	     <td colspan="3">
	     <div style="float: left; width: 70%;">
		<img src="images/admission/images/sib.jpg" alt="Logo not available" height="75" width="200" ></img>
		</div>
		<div style="text-align: left; font-size: 14px; float: right;width: 30%"><b>Bank Copy</b></div>
	     
	     </td>
		</tr>	
		<tr>
		<td colspan="3"> <div style="float: left;">Branch...........................</div> <div style="float: right;">    Date...........................</div> </td>
		
		</tr>		
			
		<tr>
		<td colspan="2" class="label">
		A/c No: <%= CMSConstants.TIEUPBANK_ACNO %> 
		</td>
		
		<%-- Denomination table --%>
		<td height="100%" width="8%" rowspan="12">
		  
		    <table border="1" height="100%" width="100%" style="border-collapse: collapse;border: 1px solid balck;">
		       <tr><td>Demo</td><td>Places</td><td>Rs.&nbsp;&nbsp;&nbsp;</td><td>Ps</td></tr>
		       <tr><td>2000X</td><td></td><td></td><td></td></tr>
		       <tr><td>500X</td><td></td><td></td><td></td></tr>
		       <tr><td>100X</td><td></td><td></td><td></td></tr>
		       <tr><td>50X</td><td></td><td></td><td></td></tr>
		       <tr><td>20X</td><td></td><td></td><td></td></tr>
		       <tr><td>10X</td><td></td><td></td><td></td></tr>
		       <tr><td>5X</td><td></td><td></td><td></td></tr>
		       <tr><td>Coins</td><td></td><td></td><td></td></tr>
		       <tr><td>Total</td><td></td><td></td><td></td></tr>
		      
		    </table>
		
		
		
		
		
		
		
		</td>
		
		
		</tr>
		<tr>
		<td colspan="2" class="label">
		A/c  Name: MAR IVANIOS COLLEGE 
		</td>
		</tr>					
		<tr>
		
		<td  align="center" colspan="2" class="label">
		<img src="images/header-logo.png" alt="Logo not available" height="50" width="300" ></img>
		</td>
		
		</tr>
		
		<tr>
		<td colspan="2" class="label">
		SIB Fee Institution Code : <b>452</b>
		</td>
		</tr>		
		 		
				<tr>
		<logic:equal value="false" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="4" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<td rowspan="5" width="28%"  align="right" class="label">
		Amount:   
		</td>
		</logic:equal>
		
		<tr><td>Category Fee :&nbsp;Rs. <bean:write name="uniqueIdRegistrationForm" property="categoryAmount"/></td></tr>
		<logic:equal value="true" name="uniqueIdRegistrationForm" property="mngQuota">
		<tr><td>Management Fee :&nbsp;Rs. 500</td></tr>
		</logic:equal>
		<tr><td>Total : Rs.&nbsp;<bean:write name="uniqueIdRegistrationForm" property="applicationAmount"/></td></tr>
		<tr><td>In words... Rupees<bean:write name="uniqueIdRegistrationForm" property="applicationAmountWords"/> only</tr>
		  
		</tr>
		
		<tr>
		<td class="label">Reference No.</td>
		<td><bean:write name="uniqueIdRegistrationForm" property="challanRefNo"/></td>
		</tr>
		
		 <tr>
		 <td class="label">Name of the Student:</td>
		 <td><bean:write name="uniqueIdRegistrationForm" property="applicantName"/></td>
		 </tr>
		 
		<tr>
		<td height="50" class="label">
		<b>Signature of Remitter:</b>  
		</td>
		<td></td>
		</tr>
		<tr>
		<td class="label">
		<b>Mob. Phone No </b>  
		</td>
		<td></td>
		</tr>
		 
		<tr>
		<td height="50" class="label"><b>Entered By </b>  </td>
		<td>Authorised Signature</td>
		</tr>
				
		</table>
		
		
	</td>
	
	
	</tr>
	<tr>
	<td width="30%"></td>
	<td width="2%"></td>
	<td width="30%"></td>
	<td width="2%"></td>
	<td width="36%" style="font-size: 10px;"></td>
	</tr>
	</table>
		
		

	
	
</html:form>


</body>
</html>
<script type="text/javascript"> window.print();</script>