<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<head>
	<link href="css/styles.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.heading3
		{
			font-weight: bold;
			font-family: Arial,Helvetica,sans-serif;
			font-size: 9.5pt;
		}
		.body{
			 size: a5 landscape;
        	 margin: 0;
		}				
	</style>
</head>
<body>
	<html:form action="/ConductAndCertificate" method="post">
	<html:hidden property="method" styleId="method" value="" />
  	<html:hidden property="formName" value="certificateForm" />
	<html:hidden property="pageType" value="3" />
		
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
										  			<img alt="Logo not available" height="150" src="images/tc-cc-logo.jpg" vspace="20" width="550">
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
						 								<font face="verdana" size="1" style="text-transform : uppercase;"><b><bean:write name="certificateForm" property="certificateTO.studentName"/></b></font> is/was a student 
									 					of Mar Ivanios College, Thiruvananthapuram 
									 					doing the <font face="verdana" size="1" style="text-transform : uppercase;"><b><bean:write name="certificateForm" property="certificateTO.programme"/></b></font> course,
									 					during the academic
									 					years <font face="verdana" size="1" style="text-transform : uppercase;"><b><bean:write name="certificateForm" property="academicYear"/></b></font> his/ her subjects of study
									 					being <font face="verdana" size="1" style="text-transform : uppercase;"><b><bean:write name="certificateForm" property="certificateTO.courseName"/></b></font> Main and 
									 					<font face="verdana" size="1" style="text-transform : uppercase;"><b><bean:write name="certificateForm" property="certificateTO.subsidiaries"/></b></font>
									 					subsidiaries, his/her Optional Subjects
									 					being ...........................................................................................
									 					<br>The medium of instruction of course has been English.<br>
									 					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 					His/ her conduct and character have been .................................
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
			<script type="text/javascript">
				window.print();
			</script>
	</html:form>
</body>
