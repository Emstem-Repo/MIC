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
</head>
<body>
<html:form action="/uniqueIdRegistration">
	<html:hidden property="formName" value="uniqueIdRegistrationForm" />
	<html:hidden property="pageType" value="" />
	<html:hidden property="method" styleId="method" value="forwardChallanTemplate" />
	
	
	<table width="100%" >
	
	<tr>
	<td height="10px"></td>
	</tr>
	
	<tr>
	<td>
	
	<table width="100%" border="0" style="border-collapse: collapse;border: 1px solid balck;">
			
							
		<tr>
		
		<td  align="center" colspan="2">
		<img src="images/header-logo.png" alt="Logo not available" height="120" width="440" ></img>
		</td>
		
		</tr>
		
		<tr>
		<td width="20%" height="30%" >
		<br></br>
		</td>
	
		</tr>
	
		
		
		<tr>
		<td colspan="2" height="30%" style="font-size: 14px;">
		Dear <bean:write name="uniqueIdRegistrationForm" property="applicantName"/> Please Make  Your NEFT Transaction  at any Bank(except SIB) by Using Following Details.  
		</td>
		</tr>
		
		
		<tr>
		<td width="20%" height="30%" >
		<br></br>
		</td>
	
		</tr>
	
		<tr>
		<td width="20%" height="30%">College Name
		</td>
	
		<td height="30%">: <%= CMSConstants.COLLEGE_NAME %> 
		</td>
		</tr>
		
		 <tr>
		 <td height="30%">Destination  Bank</td>
		 <td height="30%">: <%= CMSConstants.BANK_NAME %>
			</td>
		 </tr>
		
		
		<tr>
		 <td height="30%">Bank Branch</td>
		 <td height="30%">: <%= CMSConstants.BANK_BRANCH %>
			</td>
		 </tr>
		 
		
		<tr>
		 <td height="30%">IFSC Code</td>
		 <td height="30%">: <%= CMSConstants.IFSC_CODE %>
			</td>
		 </tr>
		
				
		<tr>
		 <td height="30%">Account Number</td>
		 <td height="30%">: <%= CMSConstants.OTHERBANK_ACNO %><bean:write name="uniqueIdRegistrationForm" property="challanRefNo"/>
			</td>
		 </tr>
				
		<tr>
		<td height="30%">
		Amount
		</td>
		<td height="30%">
		  : <bean:write name="uniqueIdRegistrationForm" property="applicationAmount"/>
		</td>
		</tr>
		
		<tr>
		<td colspan="2" height="30%">
		<br></br>
		</td>
		</tr>
		  
		<tr>
		<td colspan="2" height="30%">
		Note:
		</td>
		</tr>
		<tr>
		<td colspan="2" height="30%">
		1.Please Pay Your Online Registration Fee  via NEFT  Transaction to above Account Number and IFSC code.
		</td>
		</tr>
		<tr>
		<td colspan="2" height="30%">
		2.For Continuing the Online Registration Process after Payment, Please use last 9 alphanumeric Charaters(<bean:write name="uniqueIdRegistrationForm" property="challanRefNo"/>)  as Your Challan Number.
		
		</td>
		</tr>
		
		<!--<tr>
		<td colspan="2" height="30%">
		3.If You Wish to Make Payment through South Indian Bank(SIB) then download the Challan Provided at Admission Portal
		
		</td>
		</tr>
		
		
		
		--><tr>
		<td colspan="2" height="30%">
		<br></br>
		</td>
		</tr>
		
		
		<tr>
		<td colspan="2" >
		<table width="100%">
		<tr>
		<td width="50%">
		Thank You.
		</td>
		<td align="right" width="50%">
		Date:&nbsp; &nbsp; &nbsp; &nbsp;
		</td>
		</tr>
		</table>
		</td>
		</tr>
		
		
		</table>
		
		
	</td>
	

	</tr>
	
	
	
	<tr>
	<td height="10px"></td>
	</tr>
	
	
	</table>
		
		

	
	
</html:form>


</body>
</html>
<script type="text/javascript"> window.print();</script>