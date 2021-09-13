<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
 .title{
 font-weight: bolder;
 font-size: large;
 
 
 }
 .title2{
 font-weight: bold;
 font-size: medium;	
 }
 body{
 	color:black;
 	font-family: "Times New Roman", Times, serif;
	
 }
 
.mytable{
	border-collapse: collapse;
	
}
.algn{
   height:25px;

}
.smallCheck {
	font-weight: bold;
 	font-size: small;
}
p{
	font-weight: bold;
 	font-size: small;
 	text-justify: inter-word;
 	 text-align: justify;
}
.office{
	font-size: medium;
    line-height: 35px;
    font-weight: bold;
}
table tr{
	vertical-align: top;
}
tr{
	height: 30px;
}
td {
	line-height: 25px;
}
</style>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<body>
<html:form action="/admissionFormsOnline" method="post">
	<html:hidden property="formName" value="admissionStatusForm" />
	<html:hidden property="method" styleId="method" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionStatusForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId" value='<bean:write	name="admissionStatusForm" property="applicantDetails.course.id" />' />
	<%int i=1; %>
	<table  width="100%" style="padding: 25px" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">
		<tr><td align="center" class="title"  colspan="2">ANNEXURE-VII</td></tr>
		<tr><td align="center" class="title2" colspan="2">Undertaking from the Students as per the provisions of anti-ragging
															verdict by the Hon’ble Supreme Court of India</td></tr>
		<tr height="10px"></tr>
		<tr><td>I, Mr./ Ms. &nbsp;<bean:write name="admissionStatusForm" property="applicantDetails.personalData.firstName"/>,&nbsp;&nbsp;&nbsp; Roll No..........  ,Program:&nbsp; ..................................................</td></tr>
		<tr><td> student of Mar Ivanios College (Autonomous) do
		hereby undertake on this day ....................... Month ............................ year  ............................, the following
		with respect to above subject and Office Order No: ..................................................
		</td></tr>
		<tr><td>
			<table>
				<tr>
					<td>1)</td>
					<td>That I have read and understood the directives of the Hon’ble Supreme Court of India on
						antiragging and the measures proposed to be taken in the above references.</td>
				</tr>
				<tr height="10px"></tr>
				<tr>
					<td>2)</td>
					<td>That I have read and understood the directives of the Hon’ble Supreme Court of India on
						antiragging and the measures proposed to be taken in the above references.</td>
				</tr>
				<tr height="10px"></tr>
				<tr>
					<td>3)</td>
					<td>That I have not been found or charged for my involvement in any kind of ragging in the
							past. However, I undertake to face disciplinary action/legal proceedings including
							expulsion from the Institute if the above statement is found to be untrue or the facts are
							concealed, at any stage in future.</td>
				</tr>
				<tr height="10px"></tr>
				<tr>
					<td>4)</td>
					<td>That I shall not resort to ragging in any form at any place and shall abide by the rules / laws
						prescribed by the Courts, Govt. of India and Institute authorities for the purpose from time
						to time.</td>
				</tr>
			</table>
		</td></tr>
		<tr height="30px"></tr>
		<tr><td>Signature of Student</td></tr>
		<tr><td>I hereby fully endorse the undertaking made by my child/ward.</td></tr>
		<tr height="30px"></tr>
		<tr><td>Signature of Mother/Father/Guardian</td></tr>
		<tr height="30px"></tr>
		<tr><td>Witness: ................................................</td></tr>
		<tr height="400px"></tr>
		<tr><td>Mar Ivanios College (Autonomous), UG/PG Admission - 2021-2022
		<hr>
		</td></tr>	
	</table>

</html:form>
</body>

