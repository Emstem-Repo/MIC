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
</style>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<body>
<html:form action="/admissionFormsOnline" method="post">
	<html:hidden property="formName" value="admissionFormForm" />
	<html:hidden property="method" styleId="method" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionFormForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId" value='<bean:write	name="admissionFormForm" property="applicantDetails.course.id" />' />
	<%int i=1; %>
	<table  width="100%" style="padding: 100px" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">
		<tr><td align="center" class="title"  colspan="2">PARENT - TEACHER ASSOCIATION</td></tr>
		<tr><td align="center" class="title2" colspan="2">MAR IVANIOS COLLEGE (AUTONOMOUS)</td></tr>
		<tr><td align="center" class="title2" colspan="2">THIRUVANANTHAPURAM</td></tr>
		<tr height="10px"></tr>
		<tr><td align="center" class="title2" colspan="2">APPLICATION FOR MEMBERSHIP</td></tr>
		
			<tr><td style="width: 35%">No.</td><td></td></tr>
			<tr><td>Name of the parent&nbsp;&nbsp;&nbsp;</td><td>:&nbsp;&nbsp;&nbsp;<bean:write name="admissionFormForm" property="applicantDetails.titleOfFather"/> <bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherName"/></td></tr>
			<tr><td>Name of the student&nbsp;&nbsp;&nbsp;</td><td>:&nbsp;&nbsp;&nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.firstName"></bean:write></td></tr>
			<tr><td>Course&nbsp;&nbsp;&nbsp;</td><td>:&nbsp;&nbsp;&nbsp;<%-- <bean:write name="admissionFormForm" property="applicantDetails.course.name" /> --%></td></tr>
			<tr><td>Year of study&nbsp;&nbsp;&nbsp;</td><td>:&nbsp;&nbsp;&nbsp;
			<script>
								var year='<bean:write name="admissionFormForm" property="applicantDetails.appliedYear" />'
								var year1=parseInt(year)+3;
								document.writeln(year.toString()+"-"+year1.toString());
								console.log(year1);
							</script>
			<%-- <bean:write name="admissionFormForm" property="applicantDetails.appliedYear" /> --%></td></tr>
			<tr><td>Address of the parent with Phone No.&nbsp;&nbsp;&nbsp;</td><td>:&nbsp;&nbsp;&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine1"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine2"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentCityName"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressZipCode"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.citizenship"/>,&nbsp;
			<br>
			 Phone : &nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentMob2"/>,&nbsp;
			</td></tr>
			<tr height="10px"></tr>
			<tr><td colspan="2">Please enroll me as a member of the Parent-Teacher Association of the
								Mar Ivanios College (Autonomous), Thiruvananthapuram. I will abide by the
								rules and regulations of the Association.</td></tr>
			<tr height="10px"></tr>
			<tr><td>Place:</td><td></td></tr>
			<tr height="10px"></tr>
			<tr><td>Date :</td><td align="center">Signature</td></tr>
			
	</table>

</html:form>
</body>