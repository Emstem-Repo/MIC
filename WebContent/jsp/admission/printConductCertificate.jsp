<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<head>
	<link href="css/styles.css" rel="stylesheet" type="text/css">
	<title><bean:message key="knowledgepro.admission.ccCertificate"/></title>
	<style type="text/css">
		.heading3
		{
			font-weight: bold;
			font-family: Arial,Helvetica,sans-serif;
			font-size: 9.5pt;
		}				
	</style>
</head>
<body>
	<html:form action="/conductCertificate">
	
		<html:hidden property="formName" value="conductCertificateForm" />
		<html:hidden property="method" styleId="method" value="initConductCertificate"/>
		<html:hidden property="pageType" value="3"/>
		
		<nested:notEmpty name="conductCertificateForm" property="studentList">
			<nested:iterate name="conductCertificateForm" property="studentList" indexId="count">		
				<table class="print" width="100%">
					<tr>
						<td align="center">
							<table width="100%" border="0">
								<tr>
									<td width="20%"></td>
									<td width="40%">
										<table width="100%" border="0">
											<tr>
												<td align="center" colspan="3">
										  			<img alt="Logo not available" height="150" src="images/tc-cc-logo.jpg" vspace="30" width="600">
												</td>
											</tr>																												
											<tr>
												<td align="center" class="heading3" colspan="3">
													<font size="3">CERTIFICATE</font><br>
													<font size="3">OF</font><br>
													<font size="3">COURSE AND CONDUCT</font>
												</td>									
											</tr>
											<tr height="10px"></tr>										
										</table>
									</td>
									<td width="20%"></td>
								</tr>
								<tr>
									<td width="20%"></td>
									<td width="40%">
										<table width="100%">
						 					<tr>
						 						<td>
						 							<p style="font-style: normal; text-align: justify; font-family: Lucida Calligraphy; font-size: 11;line-height: 220%;">
						 								<span style="padding-left:5em">Certified that </span>
						 								<font face="verdana" size="1"><b><bean:write name="studentList" property="studentName"/></b></font> is/was a student 
									 					of Mar Ivanios College, Thiruvananthapuram 
									 					doing the <font face="verdana" size="1"><b><bean:write name="studentList" property="course"/></b></font> course,
									 					during the academic
									 					years <font face="verdana" size="1"><b><bean:write name="studentList" property="leavingAcademicYear"/></b></font> his/ her subjects of study
									 					being <font face="verdana" size="1"><b><bean:write name="studentList" property="subjectPassedCore"/></b></font> Main and 
									 					<font face="verdana" size="1"><b><bean:write name="studentList" property="subjectsPassedComplimentary"/></b></font>
									 					subsidiaries, his/her Optional Subjects
									 					being <font face="verdana" size="1"><b><bean:write name="studentList" property="subjectPassedOptional"/></b></font>.
									 					The medium of instruction of course has been English.<br>
									 					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 					His/ her conduct have been .................................
									 				</p>
						 						</td>
						 						<td></td>
						 					</tr>
						 				</table>
									</td>
									<td width="20%"></td>
								</tr>
								<tr>
									<td width="20%"></td>
									<td width="40%">
										<table>
											<tr>
												<td width="10%">
													<font size="1">
														Date
													</font>
												</td>
												<td width="85%"></td>
												<td width="15%"></td>
											</tr>
											<tr>
												<td width="10%"><font size="1">Thiruvananthapuram</font></td>
												<td width="85%"></td>
												<td width="15%"><font size="1">PRINCIPAL</font></td>
											</tr>
										</table>
									</td>
									<td width="20%"></td>
								</tr>		 				
							</table>			
						</td>
					</tr>	
				</table>
				<p style="page-break-after: always;"></p>
			</nested:iterate>		
			<script type="text/javascript">
				window.print();
			</script>
		</nested:notEmpty>
		<logic:empty name="conductCertificateForm" property="studentList">
			<table width="100%" height="435px">
				<tr>
					<td align="center" valign="middle">
						No Records Found
					</td>
				</tr>
			</table>
		</logic:empty>
	</html:form>
</body>
