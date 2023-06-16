<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="javax.servlet.*,java.text.*"%>
<style>
.bold {
	font-weight: bold;
}

@page {
	size: A4;
	margin: 0;
}
</style>

<html:form action="/StudentLoginAction">
	<html:hidden property="formName" value="loginform" />
	<div style="padding: 30px;">
	<table width="100%" cellspacing="1" cellpadding="2" class="row-white"
		style="font-family: Times New Roman">
		<tr>
			<td colspan="3" align="center"><img src='<%=CMSConstants.LOGO_URL%>' height="90" width="450" /></td>
		</tr>
		<tr><td align="left"><bean:write name="loginform" property="studentName"/></td>
		<td align="right"></td>
		<td align="right">Transaction No: <bean:write name="loginform" property="feePrintReciept.txNo"/></td>
		</tr>
		<tr><td align="left" colspan="2"><bean:write name="loginform" property="regNo"/></td>
		</tr>
		<tr><td align="left"><bean:write name="loginform" property="courseName"/></td>
		<td align="right"></td>
		<td align="right">Date:<bean:write name="loginform" property="feePrintReciept.txnDate"/></td>
		</tr>
		<tr height="35px"></tr>
		<tr><td align="center" colspan="3">Reciept</td>
		</tr>
		<tr>
			<td align="center" colspan="3">
			<table border="1" style="border-collapse: collapse;">
				<tr><td align="left">Sl.No</td>
					<td align="right">Particular</td>
					<td align="left">Amount</td>
				</tr>
				<tr><td align="left">1</td>
					<td align="right"><bean:write name="loginform" property="feePrintReciept.examName"/></td>
					<td align="left"><bean:write name="loginform" property="feePrintReciept.amount"/></td>
				</tr>
				<tr><td align="left"></td>
					<td align="right">Total</td>
					<td align="left"><bean:write name="loginform" property="feePrintReciept.amount"/></td>
				</tr>
				<tr>
					<td align="center" colspan="3">(<bean:write name="loginform" property="feePrintReciept.amountWords"/>)</td>
					
				</tr>
			</table>
			</td>
			
		</tr>
		<tr height="25px"></tr>
		<tr><td align="left">Thank you</td>
		<td align="right"></td>
		<td align="right">For Mar Ivanious College</td>
		</tr>

	</table>
</div>
</html:form>
<script type="text/javascript">window.print();</script>
