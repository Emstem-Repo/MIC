<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>

<%@page import="com.kp.cms.utilities.CommonUtil"%>
<%@page import="com.kp.cms.forms.fee.FeePaymentForm"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/styles.css" rel="stylesheet" type="text/css">
<style type="text/css">
body
{
	font-family: sans-serif;
	font-size: 12;
	
}

</style>
<title><bean:message key="knowledgepro.fee.feerecipt"/></title>
</head>
<script type="text/javascript">
function printMe()
{
	window.print();
}
function closeMe()
{
	window.close();
}
</script>
<body>
<%
	FeePaymentForm feePaymentForm = (FeePaymentForm) request
			.getAttribute("feePaymentForm");
%>

<html:form action="/FeePayment">
<table width="100%">
<tr>
<td colspan="2" class="row-pri-odd">
<logic:notEmpty name="feePaymentForm" property="feePaymentDetails" > 
<% int totCount = 0;
	int totalAmount = 0;
	int amountPaid = 0;
	int balance = 0;
%>

<logic:iterate id="feeAccountTest" name="feePaymentForm" property="feePaymentDetails" indexId="counter">
<% totCount = totCount + 1;
%>
</logic:iterate>
<logic:iterate id="feeAccount" name="feePaymentForm" property="feePaymentDetails" indexId="counter" >
<table width="60%" height="250px" border="0" >
	
	
	<tr>
	<logic:iterate id="accname" name="feeAccount" property="value" indexId="c">
		<c:choose>
			<c:when test="${c == 0}">
				
			<tr>	
			<td align="center" width="100%" class="row-pri-odd"  style="border-top-style:dashed; border-bottom-style:dashed;  border-color: black; border-width: medium thick"><font style="font-family: monospace;"> <bean:write name="accname" property="printAccountName"/></font></td>	
			</tr>
			</c:when>
		</c:choose>
	</logic:iterate>
	</tr>		


	<tr>
	<td>
	<table width="100%">
	<tr>
	<td align="left" class="row-pri-odd" width="70%" style="font-size:11px; font-family:monospace;"> Receipt No:&nbsp; <bean:write name="feePaymentForm" property="billNo"/></td>
	<td align="left"class="row-pri-odd" width="30%" style="font-size:11px; font-family:monospace;">Date: &nbsp;<bean:write name="feePaymentForm" property="challanPrintDate"/></td>
	</tr>
	<tr>
	<td class="row-pri-even" align="left" width="70%" style="font-size:11px; font-family:monospace;" >Register Number & App.No:&nbsp; <bean:write name="feePaymentForm" property="applicationId"/></td>
	<td class="row-pri-even" align="left" width="30%" style="font-size:11px; font-family:monospace;" >Category:&nbsp; <bean:write name="feePaymentForm" property="admittedThroughCode"/></td>
	</tr>
	<tr>
	<td class="row-pri-even" align="left" width="70%" style="font-size:11px; font-family:monospace;"  >Student Name:&nbsp;<bean:write name="feePaymentForm" property="studentName"/></td>
	<td class="row-pri-even" align="left" width="30%" style="font-size:11px; font-family:monospace;"  >Caste:&nbsp;<bean:write name="feePaymentForm" property="casteName"/></td>
	</tr>
	<tr>
	<td class="row-pri-even" align="left" width="70%" style="font-size:11px; font-family:monospace;"  colspan="2">Class / Course:&nbsp;<bean:write name="feePaymentForm" property="className"/></td>
	</tr>
	<tr>
	<td class="row-pri-even" width="50%" align="left" style=" border-bottom-style:dashed;  border-color: black; font-weight: 1px"><font style="font-family: monospace; font-size: 11px"> Language:&nbsp;<bean:write name="feePaymentForm" property="secondLanguage"/></font></td>
	<td class="row-pri-even" align="left" width="100%" style="border-bottom-style:dashed;  border-color: black; font-weight: 1px">&nbsp;</td>
	</tr>	
	</table>
	</td>

	
 <tr>
	<td colspan="2" align="center" class="row-pri-odd" height="320"  valign="top">
	<%String height="375px"; %>
	<!--  <logic:equal value="3" name="feeAccount" property="key">
		<%height="330px"; %>
	</logic:equal> -->
	 
	<table width="100%" > 
	<%
		int total = 0;
	%>
	<c:set var="present" value="0"/>
	<logic:notEmpty name="feeAccount" property="value">
	
	<logic:iterate id="feeHeading" name="feeAccount" property="value" indexId="count">
	<bean:define id="count1" name="feeHeading" property="count"/>
								<bean:define id="amount" name="feeHeading" property="amountPaid" />
								<bean:define id="totamount" name="feeHeading" property="totalAmount" />
								<%
									total = total+ Integer.parseInt(amount.toString());
									amountPaid = amountPaid+ Integer.parseInt(amount.toString());
									totalAmount = totalAmount + Integer.parseInt(totamount.toString());
								%>
								<tr>
	<td align="left" width="50%" class="row-pri-odd"  style="font-size:11px; font-family:monospace;" >
		<bean:write name="feeHeading" property="feeGroup"/>&nbsp;&nbsp;
	</td>
	<td class="row-pri-even" width="50%" align="right" style="font-size:11px; font-family:monospace;" > <bean:write name="feeHeading" property="amountPaid"/>.00&nbsp;&nbsp;&nbsp;</td>
	</tr>
	</logic:iterate>
	</logic:notEmpty>
	<%balance = totalAmount - amountPaid; %>
	</table>
	</td>
	</tr>
	<tr>
	<td>
	<table width="100%">
	<tr>
		<td align="left" width="50%" class="row-pri-odd" style="border-top-style:dashed; border-bottom-style:dashed;  border-color: black; border-width: medium thick;" ><font style="font-family: monospace; font-size: 13; font-weight: 5px">T O T A L</font></td>	
		<td class="row-pri-even" width="50%"  align="right" style="border-top-style:dashed; border-bottom-style:dashed;  border-color: black; border-width: medium thick;"> <font style="font-family: monospace; font-size: 13; font-weight: 5px"> <%=total%>.00&nbsp;&nbsp;&nbsp;</font></td>
	</tr>
	</table>
	</td>		
	</tr>

	<!--  <tr>
		<td colspan="2" class="row-pri-odd">Amount in words:&nbsp; <%=CommonUtil.numberToWord(total) + " ONLY"%></td>
	</tr>-->
	<c:choose>
	<c:when test="${counter == 1}">
	<tr>
		<td class="row-pri-even" width="50%"  align="left" style="border-bottom-style:dashed;  border-color: black; border-width: medium thick;"><font style="font-size:13;font-weight: 5px; font-family: monospace;"> Grand Total: (Recpt 1 + 2):
		&nbsp;&nbsp;<bean:write name="feePaymentForm" property="totalPaidAmt"/>0
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Balance:&nbsp;&nbsp;<%=balance %>.00</font> </td>
	</tr>
	</c:when>
	</c:choose>
	
	<tr height="40">
		<td colspan="2" class="row-pri-odd" style="font-size:11px; font-family:monospace;">
			Accountant Signature
		</td>
	</tr>
	
	<tr height="10">
	<td>&nbsp;</td>
	</tr>

	
	
</table>
</logic:iterate>
</logic:notEmpty>
</td>
</tr>
</table>

</html:form>
<script type="text/javascript">
window.print();
</script>
