<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<head>
	<link href="css/styles.css" rel="stylesheet" type="text/css">
	<title><bean:message key="knowledgepro.admission.transferCertificate"/></title>
	<style type="text/css">
		.heading1 {
			font-weight: bold;	
			font-family: Arial,Helvetica,sans-serif;
			font-family: 9.5pt;
		}
		.heading2 {
			font-family: Arial,Helvetica,sans-serif;
			font-size: 13px;		
			vertical-align: top;
		}
		.heading3 {
			font-weight: bold;
			font-family: Arial,Helvetica,sans-serif;
			font-size: 9.5pt;
		}
		.heading4 {
			font-weight: bold;
			font-family: Arial,Helvetica,sans-serif;
			font-size: 9pt;
		}
		.heading5 {
			font-weight: bold;
			font-family: Arial,Helvetica,sans-serif;
			font-size: 9pt;
		}
		.body2 {
			font-size: 8pt;
		}	
	</style>
	<style>
		@media print {
			@page {size: landscape; overflow: hidden; margin-left: 70px;}
			body { font-size: 10pt; }
		}
		@media screen {     /* Just for computer */
			body { font-size: 13px; }
		}
	</style>
</head>
<body>
	<%@ page import="java.util.*"%>
	<%@ page import="java.text.*"%>
	<html:form action="/transferCertificate">
		<html:hidden property="formName" value="transferCertificateForm" />
		<logic:notEmpty name="transferCertificateForm" property="studentList">
			<logic:iterate id="studentList" name="transferCertificateForm" property="studentList" indexId="count">
				<logic:equal value="false" name="transferCertificateForm" property="reprint">
					<table width="100%" border="0">
						<tr>
							<td width="46%" style="border-bottom: solid thin; border-left: solid thin; border-right: solid thin; border-top: solid thin">
								<table border="0" width="100%">
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr><td align="center" colspan="3"><img width="560" height="180"  src='images/tc-cc-logo.jpg' alt="Logo not available"></td></tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr>
													<td width="2%" class="heading2"></td>
													<td width="27%" class="heading5" align="left">
														<bean:message key="knowledgepro.admission.tcno"/>.: 
														<bean:write name="studentList" property="tcNo"/>
													</td>	
													<td class="heading5" width="27%" align="center"><bean:message key="knowledgepro.admission.admissionno"/>: <bean:write name="studentList" property="admissionnumber"/></td>   										
												</tr>
												<tr><td>&nbsp;</td></tr>				
												<tr>
													<td align="center" class="heading3" colspan="3">
														<h3><b><u><bean:message key="knowledgepro.admission.tcconductcertificate"/></u></b></h3>
													</td>
												</tr> 
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%">
											<table width="100%"> 				
		       									<tr>	
		       										<td width="1%" class="heading2">1. </td>	
		       										<td width="49%" class="heading2"><bean:message key="knowledgepro.admission.studentname"/></td>
		       										<td width="1%" class="heading2">: </td>	
		    										<td class="heading2" width="49%"><b><bean:write name="studentList" property="studentName"/></b></td>
		    									</tr>
		     									<tr>
		     										<td width="1%" class="heading2">2. </td>	
		    										<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.dob"/></td>
		    										<td width="1%" class="heading2">: </td>
		    										<td class="heading2" width="49%">
		    											<bean:write name="studentList" property="dobFigures"/>,
		    											<bean:write name="studentList" property="dobWords"/>
		    											<br></br>
		    										</td>
		    									</tr>
		    									<tr>
		    										<td width="1%" class="heading2">3. </td>
		    										<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.admissiondate"/></td>  
													<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="dateOfAdmission"/></td>
		    									</tr>
								    			<tr>
								    				<td width="1%" class="heading2">4.</td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.leavingdate"/></td>
								    				<td width="1%" class="heading2">: </td> 
													<td class="heading2" width="50%"><bean:write name="studentList" property="dateOfLeaving"/></td>
								    			</tr>    			
								     			<tr>
								     				<td width="1%" class="heading2">5. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.class&subjectwhileleaving"/></td>
								    				<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="classOfLeaving"/></td>
								    			</tr>   
		    									<tr>
		    										<td width="1%" class="heading2">6. </td>
		    										<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.subjectsstudied"/></td>
		    										<td width="1%" class="heading2">: </td>
													<td class="heading2">
														<logic:notEmpty name ="studentList" property="subjectPassedCore">
															<bean:write name="studentList"  property="subjectPassedCore" />
														</logic:notEmpty>
														<logic:notEmpty name ="studentList" property="subjectsPassedComplimentary">
															, <bean:write name="studentList" property="subjectsPassedComplimentary" />
														</logic:notEmpty>
														<logic:notEmpty name ="studentList" property="subjectPassedOptional">
															, <bean:write name="studentList"  property="subjectPassedOptional" />
														</logic:notEmpty>
													</td>	
		    									</tr> 				
		    									<tr>
		    										<td width="1%" class="heading2">7. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.higherclasspromotion"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="promotionToNextClass"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">8. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.university"/></td>
								    				<td width="1%" class="heading2">: </td>
								    				<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="publicExamName"/>
								    			</tr>   			
		    									<tr>
		    										<td width="1%" class="heading2">9. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.regno&year"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="regMonthYear"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">10. </td>
													<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.exam.appeared"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="isAppeared"/></td>
												</tr>
								    			<tr>
								    				<td width="1%" class="heading2">11. </td>
													<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.feedues"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="feePaid"/></td>
												</tr>
								    			<tr>
								    				<td width="1%" class="heading2">12. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.parts&divisionpass"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectPassed"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">13. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.parts&divisionfail"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectFailed"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">14. </td>
													<td width="50%" class="heading2"><bean:message key="knowledgepro.admission.exam.punished"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="isStudentPunished"/></td>
												</tr>
												<tr><td></td><td></td></tr>				
		 									</table>
		 								</td>
		 							</tr>
					 				<tr>
						 				<td>
							 				<table width="100%">
								 				<tr><td></td></tr>
								 				<tr><td></td></tr>  				
								 				<tr>
								 					<% 
									  					DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
									    				Date date2 = new Date();
									    				String date11=dateFormat1.format(date2);
								    				%>
		 											<td align="right" class="heading3" width="20%"><h3><bean:message key="knowledgepro.admission.date"/> <%=date11 %></h3></td> 	
								 				    <td width="20%"></td> 
								 				    <td width="20%"></td>
		    										<td align="left" class="heading3" width="20%"><h3><bean:message key="knowledgepro.admission.principal"/></h3></td>
		    									</tr>
		 									</table>
		 								</td>
		 							</tr>
								</table>
							</td>
							<td width="2%" align="right"></td>
							<td style="border-left: thin;border-left: dashed;" width="2%" align="right"></td>
							<td width="46%" style="border-bottom: solid thin; border-left: solid thin; border-right: solid thin; border-top: solid thin">
								<table border="0" width="100%">
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr>
													<td align="center" colspan="3"><img width="560" height="180"  src='images/tc-cc-logo.jpg' alt="Logo not available"></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr>
													<td width="2%" class="heading2"> </td>
													<td width="27%" class="heading5" align="left"><bean:message key="knowledgepro.admission.tcno"/>.:<bean:write name="studentList" property="tcNo"/></td>	
													<td class="heading5" width="27%" align="center"><bean:message key="knowledgepro.admission.admissionno"/>: <bean:write name="studentList" property="admissionnumber"/></td>   										
												</tr>
												<tr><td>&nbsp;</td></tr>				
												<tr>
													<td align="center" class="heading3" colspan="3"><h3><b><u><bean:message key="knowledgepro.admission.tcconductcertificate"/></u></b></h3></td>
												</tr> 
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%">
											<table width="100%"> 				
		       									<tr>
		       										<td width="1%" class="heading2">1. </td>	
		       										<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.studentname"/></td>
		       										<td width="1%" class="heading2">: </td>	
		    										<td align="left" class="heading2" width="49%"><b><bean:write name="studentList" property="studentName"/></b></td>
		    									</tr>
		     									<tr>
		     										<td width="1%" class="heading2">2. </td>	
		    										<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.dob"/></td>
		    										<td width="1%" class="heading2">: </td>	
		    										<td class="heading2" width="49%">
		    											<bean:write name="studentList" property="dobFigures"/> ,
		    											<bean:write name="studentList" property="dobWords"/>
		    											<br></br>
		    										</td>
		    									</tr>       									
								    			<tr>
								    				<td width="1%" class="heading2">3. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.admissiondate"/></td>  
													<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="dateOfAdmission"/></td>
								    			</tr>
								    			<tr>
								    				<td width="1%" class="heading2">4.</td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.leavingdate"/></td>
								    				<td width="1%" class="heading2">: </td> 
													<td class="heading2" width="50%"><bean:write name="studentList" property="dateOfLeaving"/></td>
								    			</tr>    			
								     			<tr>
								     				<td width="1%" class="heading2">5. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.class&subjectwhileleaving"/></td>
								    				<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="classOfLeaving"/></td>
								    			</tr>   
		    									<tr>
		    										<td width="1%" class="heading2">6. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.subjectsstudied"/></td>
								    				<td width="1%" class="heading2">: </td>
													<td class="heading2">
														<logic:notEmpty name ="studentList" property="subjectPassedCore">
															<bean:write name="studentList"  property="subjectPassedCore" />
														</logic:notEmpty>
														<logic:notEmpty name ="studentList" property="subjectsPassedComplimentary">
															, <bean:write name="studentList" property="subjectsPassedComplimentary" />
														</logic:notEmpty>
														<logic:notEmpty name ="studentList" property="subjectPassedOptional">
															, <bean:write name="studentList"  property="subjectPassedOptional" />
														</logic:notEmpty>
													</td>	
		    									</tr> 				
								    			<tr>
								    				<td width="1%" class="heading2">7. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.higherclasspromotion"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="promotionToNextClass"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">8. </td>
								    				<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.university"/></td>
								    				<td width="1%" class="heading2">: </td>
								    				<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="publicExamName"/>
								    			</tr>   			
								    			<tr>
								    				<td width="1%" class="heading2">9. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.regno&year"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="regMonthYear"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">10. </td>
													<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.exam.appeared"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="isAppeared"/></td>
												</tr>
								    			<tr>
								    				<td width="1%" class="heading2">11. </td>
													<td align="left" width="49%" class="heading2"><bean:message key="knowledgepro.admission.feedues"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="feePaid"/></td>
												</tr>
								    			<tr>
								    				<td width="1%" class="heading2">12. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.parts&divisionpass"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectPassed"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">13. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.parts&divisionfail"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectFailed"/></td>
												</tr>
												<tr>
													<td width="1%" class="heading2">14. </td>
													<td align="left" width="50%" class="heading2"><bean:message key="knowledgepro.admission.exam.punished"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="isStudentPunished"/></td>
												</tr>
												<tr><td></td><td></td></tr>				
		 									</table>
		 								</td>
		 							</tr>
		 							<tr>
						 				<td>
							 				<table width="100%">
								 				<tr><td></td></tr>
								 				<tr><td></td></tr>  				
								 				<tr>
								 					<% 
									  					DateFormat dateFormat11 = new SimpleDateFormat("dd/MM/yyyy");
									    				Date date22 = new Date();
									    				String date111=dateFormat1.format(date22);
								    				%>
									 				<td align="right" class="heading3" width="20%"><h3><bean:message key="knowledgepro.admission.date"/> <%=date111 %></h3></td> 	
								 				    <td width="20%"></td> 
								 				    <td width="20%"></td>
								    				<td align="left" class="heading3" width="20%"><h3><bean:message key="knowledgepro.admission.principal"/></h3></td>
		    									</tr>
		 									</table>
		 								</td>
		 							</tr>
								</table>
							</td>
						</tr>
					</table>
				</logic:equal>
				<logic:equal value="true" name="transferCertificateForm" property="reprint">
					<table width="100%" border="0">
						<tr>
							<td width="46%" style="border-bottom: solid thin; border-left: solid thin; border-right: solid thin; border-top: solid thin">
								<table border="0" width="100%" style="background-image: url('images/duplicateWatermark.png');background-repeat: no-repeat;background-position: center;background-size: 400px 400px;">
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr><td>&nbsp;</td></tr>
												<tr>
													<td align="center" colspan="3">
											  			<img width="480" height="100"  src='images/tc-cc-logo.jpg' alt="Logo not available">
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr>
													<td width="2%" class="heading2"></td>
													<td width="27%" class="heading5" align="left">
														<bean:message key="knowledgepro.admission.tcno"/>.: 
														<bean:write name="studentList" property="tcNo"/>
													</td>	
													<td class="heading5" width="27%" align="center"><bean:message key="knowledgepro.admission.admissionno"/>: <bean:write name="studentList" property="admissionnumber"/></td>   										
												</tr>
												<tr><td>&nbsp;</td></tr>				
												<tr>
													<td align="center" class="heading3" colspan="3">
														<h3><b><u><bean:message key="knowledgepro.admission.tcconductcertificate"/></u></b></h3>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%">
											<table width="100%"> 				
								       			<tr height="30px">	
								       				<td width="1%" class="heading2">1. </td>	
								       				<td align="left" width="49%" class="heading2">
								    					<bean:message key="knowledgepro.admission.studentname"/>
								    				</td><td width="1%" class="heading2">: </td>	
								    				<td align="left" class="heading2" width="49%"><b><bean:write name="studentList" property="studentName"/></b>
								    				</td>
								    			</tr>
								     			<tr height="30px"><td width="1%" class="heading2">2. </td>	
								    				<td align="left" width="49%" class="heading2">
								    					<bean:message key="knowledgepro.admission.dob"/>
								    				</td>
								    				<td width="1%" class="heading2">: </td>	
								    				<td class="heading2" width="49%"><bean:write name="studentList" property="dobFigures"/> ,
								    					<bean:write name="studentList" property="dobWords"/>
								    				</td>
								    			</tr>
	       										<tr height="30px">
	       											<td width="1%" class="heading2">3. </td>
								    				<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.admissiondate"/>
													</td>  
													<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="dateOfAdmission"/>
													</td>
								    			</tr>
								    			<tr height="30px"><td width="1%" class="heading2">4.</td>
								    				<td align="left" width="49%" class="heading2">
														 <bean:message key="knowledgepro.admission.leavingdate"/>
													</td> 
													<td width="1%" class="heading2">: </td> 
													<td class="heading2" width="50%"><bean:write name="studentList" property="dateOfLeaving"/></td>
								    			</tr>    			
								     			<tr height="30px">
								     				<td width="1%" class="heading2">5. </td>
								    				<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.class&subjectwhileleaving"/>
													</td>
													<td width="1%" class="heading2">: </td>
													<td class="heading2" width="49%"><bean:write name="studentList" property="classOfLeaving"/></td>
								    			</tr>    				
												<tr height="30px">
													<td width="1%" class="heading2">6. </td>
								    				<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.subjectsstudied"/>
													</td>
													<td width="1%" class="heading2">: </td>
													<td>
														<table> 
															<tr>
																<logic:notEmpty name ="studentList" property="subjectPassedCore">
																	<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectPassedCore" /></td>
																</logic:notEmpty>
															</tr>
															<tr>
																<logic:notEmpty name ="studentList" property="subjectsPassedComplimentary">
																	<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectsPassedComplimentary" /></td>
																</logic:notEmpty>
															</tr>
															<tr>
																<logic:notEmpty name ="studentList" property="subjectPassedOptional">
																	<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectPassedOptional" /></td>
																</logic:notEmpty>
															</tr>
														</table>
													</td>	
	    										</tr>
								    			<tr height="30px">
								    				<td width="1%" class="heading2">7. </td>
								    				<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.higherclasspromotion"/>
													</td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%">
														<bean:write name="studentList" property="promotionToNextClass"/>
													</td>
												</tr>
												<tr height="30px">
													<td width="1%" class="heading2">8. </td>
								    				<td align="left" width="49%" class="heading2">
								    					<bean:message key="knowledgepro.admission.university"/>
								    				</td>
								    				<td width="1%" class="heading2">: </td>
								    				<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="publicExamName"/>
								    			</tr>   			
								    			<tr height="30px">
								    				<td width="1%" class="heading2">9. </td>
													<td align="left" width="50%" class="heading2">
														<bean:message key="knowledgepro.admission.regno&year"/>
													</td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="regMonthYear"/></td>
												</tr>
												<tr height="30px"><td width="1%" class="heading2">10. </td>
													<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.exam.appeared"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="isAppeared"/></td>
												</tr>
								    			<tr height="30px">
								    				<td width="1%" class="heading2">11. </td>
													<td align="left" width="49%" class="heading2">
														<bean:message key="knowledgepro.admission.feedues"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="feePaid"/></td>
												</tr>
								    			<tr height="30px">
								    				<td width="1%" class="heading2">12. </td>
													<td align="left" width="50%" class="heading2">
														<bean:message key="knowledgepro.admission.parts&divisionpass"/></td>
													<td width="1%" class="heading2">:</td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectPassed"/></td>
												</tr>
												<tr height="30px">
													<td width="1%" class="heading2">13. </td>
													<td align="left" width="50%" class="heading2">
														<bean:message key="knowledgepro.admission.parts&divisionfail"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectFailed"/></td>
												</tr>
												<tr height="30px">
													<td width="1%" class="heading2">14. </td>
													<td align="left" width="50%" class="heading2">
														<bean:message key="knowledgepro.admission.exam.punished"/></td>
													<td width="1%" class="heading2">: </td>
													<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="isStudentPunished"/></td>
												</tr>
												<tr><td></td><td></td></tr>				
	 										</table>
	 									</td>
	 								</tr>
	 								<tr>
	 									<td>
	 										<table width="100%">
	 											<tr><td></td></tr>
	 											<tr><td></td></tr>  				
								 				<tr height="30px">
								 					<% 
									  					DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
									    				Date date2 = new Date();
									    				String date11=dateFormat1.format(date2);
								    				%>
								 					<td align="right" class="heading3" width="20%">
														<h3><bean:message key="knowledgepro.admission.date"/> <%=date11 %></h3>													
								    				</td> 	
								 				    <td width="20%"></td> 
								 				    <td width="20%"></td>
								    				<td align="left" class="heading3" width="20%">
														<h3><bean:message key="knowledgepro.admission.principal"/></h3>													
								    				</td>
								    			</tr>
	 										</table>
	 									</td>
	 								</tr>
								</table>
							</td>
							<td width="2%" align="right"></td>
							<td width="2%" style="border-left: thin;border-left: dashed;" align="right"></td>
							<td width="46%" style="border-bottom: solid thin; border-left: solid thin; border-right: solid thin; border-top: solid thin">
								<table border="0" width="100%" style="background-image: url('images/duplicateWatermark.png');background-repeat: no-repeat;background-position: center;background-size: 400px 400px;">
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr><td>&nbsp;</td></tr>
												<tr>
													<td align="center" colspan="3">
											  			<img width="450" height="100"  src='images/tc-cc-logo.jpg' alt="Logo not available">
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%" align="center">
											<table width="100%" height="20%" border="0">
												<tr>
													<td width="2%" class="heading2"></td>
													<td width="27%" class="heading5" align="left">
														<bean:message key="knowledgepro.admission.tcno"/>.: 
														<bean:write name="studentList" property="tcNo"/>
													</td>	
													<td class="heading5" width="27%" align="center"><bean:message key="knowledgepro.admission.admissionno"/>: <bean:write name="studentList" property="admissionnumber"/></td>   										
												</tr>
												<tr><td>&nbsp;</td></tr>				
												<tr>
													<td align="center" class="heading3" colspan="3">
														<h3><b><u><bean:message key="knowledgepro.admission.tcconductcertificate"/></u></b></h3>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%">
											<table width="100%"> 				
								       			<tr height="30px">	
								       				<td width="1%" class="heading2">1. </td>	
								       				<td align="left" width="49%" class="heading2">
								    					<bean:message key="knowledgepro.admission.studentname"/>
								    				</td>
								    				<td width="1%" class="heading2">: </td>	
								    				<td align="left" class="heading2" width="49%"><b><bean:write name="studentList" property="studentName"/></b></td>
								    			</tr>
						     			<tr height="30px"><td width="1%" class="heading2">2. </td>	
						    				<td align="left" width="49%" class="heading2">
						    					<bean:message key="knowledgepro.admission.dob"/>
						    				</td><td width="1%" class="heading2">: </td>	
						    							<td class="heading2" width="49%"><bean:write name="studentList" property="dobFigures"/>,
						    				<bean:write name="studentList" property="dobWords"/></td>
						    			</tr>
						       			<tr height="30px"><td width="1%" class="heading2">3. </td>
						    				<td align="left" width="49%" class="heading2">
												<bean:message key="knowledgepro.admission.admissiondate"/>
											</td>  
											<td width="1%" class="heading2">: </td>
											<td class="heading2" width="49%"><bean:write name="studentList" property="dateOfAdmission"/>
											</td>
						    			</tr>
	    			
	    			<tr height="30px"><td width="1%" class="heading2">4.</td>
	    				<td align="left" width="49%" class="heading2">
							 <bean:message key="knowledgepro.admission.leavingdate"/>
						</td> <td width="1%" class="heading2">: </td> 
						<td class="heading2" width="50%"><bean:write name="studentList" property="dateOfLeaving"/>
						</td>
	    			</tr>    			
	    			
	     			<tr height="30px"><td width="1%" class="heading2">5. </td>
	    				<td align="left" width="49%" class="heading2">
							<bean:message key="knowledgepro.admission.class&subjectwhileleaving"/>
						</td><td width="1%" class="heading2">: </td>
						<td class="heading2" width="49%">
							<bean:write name="studentList" property="classOfLeaving"/></td>
	    			</tr>    				
	    			
					<tr height="30px"><td width="1%" class="heading2">6. </td>
	    				<td align="left" width="49%" class="heading2">
							<bean:message key="knowledgepro.admission.subjectsstudied"/>
						</td><td width="1%" class="heading2">: </td>
						
						<td >
					<table > 
					<tr>
					<logic:notEmpty name ="studentList" property="subjectPassedCore">
				
						<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectPassedCore" /></td>
						</logic:notEmpty>
					</tr>
						<tr>
					<logic:notEmpty name ="studentList" property="subjectsPassedComplimentary">
					
						<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectsPassedComplimentary" /></td>
						</logic:notEmpty>
					</tr>
						<tr>
					<logic:notEmpty name ="studentList" property="subjectPassedOptional">
				
						<td class="heading2" width="49%"><bean:write name="studentList"  property="subjectPassedOptional" /></td>
						</logic:notEmpty>
					</tr>
					</table></td>	
	    			</tr> 	
	    			
	    			<tr height="30px"><td width="1%" class="heading2">7. </td>
	    				<td align="left" width="49%" class="heading2">
							<bean:message key="knowledgepro.admission.higherclasspromotion"/></td>
							<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="49%">
						<bean:write name="studentList" property="promotionToNextClass"/>
						</td>
					</tr>
	    		 
					
					<tr height="30px"><td width="1%" class="heading2">8. </td>
	    				<td align="left" width="49%" class="heading2">
	    					<bean:message key="knowledgepro.admission.university"/>
	    				</td><td width="1%" class="heading2">: </td>
	    				<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="publicExamName"/>
	    				
	    			</tr>   			
	    			
	    			<tr height="30px"><td width="1%" class="heading2">9. </td>
						<td align="left" width="50%" class="heading2">
							<bean:message key="knowledgepro.admission.regno&year"/></td>
							<td width="1%" class="heading2">: </td>
						<!--<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="regNo"/> / <bean:write name="studentList" property="exammonth"/> <bean:write name="studentList" property="examyear"/></td>
						
					-->
					<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="regMonthYear"/></td>
					</tr>
					
					<tr height="30px"><td width="1%" class="heading2">10. </td>
						<td align="left" width="49%" class="heading2">
							<bean:message key="knowledgepro.admission.exam.appeared"/></td>
						<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="isAppeared"/>
						</td>
					</tr>
							
	    			<tr height="30px"><td width="1%" class="heading2">11. </td>
						<td align="left" width="49%" class="heading2">
							<bean:message key="knowledgepro.admission.feedues"/></td>
						<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="49%"><bean:write name="studentList" property="feePaid"/>
						</td>
					</tr>
	    			
	    			<tr height="30px"><td width="1%" class="heading2">12. </td>
						<td align="left" width="50%" class="heading2">
							<bean:message key="knowledgepro.admission.parts&divisionpass"/></td>
							<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectPassed"/></td>
						
					</tr>
					
					<tr height="30px"><td width="1%" class="heading2">13. </td>
						<td align="left" width="50%" class="heading2">
							<bean:message key="knowledgepro.admission.parts&divisionfail"/></td>
							<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="subjectFailed"/></td>
						
					</tr>
					
						<tr height="30px"><td width="1%" class="heading2">14. </td>
						<td align="left" width="50%" class="heading2">
							<bean:message key="knowledgepro.admission.exam.punished"/></td>
							<td width="1%" class="heading2">: </td>
						<td rowspan="1" class="heading2" width="50%"><bean:write name="studentList" property="isStudentPunished"/></td>
						
					</tr>
			
					
					<tr><td></td><td></td></tr>				
	 				</table></td></tr>
	 				
	 				<tr>
	 				<td>
	 				<table width="100%">
	 				<tr><td></td></tr>
	 				<tr><td></td></tr>  				
	 				<tr height="30px">
	 					<% 
	  					DateFormat dateFormat123 = new SimpleDateFormat("dd/MM/yyyy");
	    				Date date23 = new Date();
	    				String date112=dateFormat123.format(date23);
	    				%>
	    				
	 				<td align="right" class="heading3" width="20%">
							<h3><bean:message key="knowledgepro.admission.date"/> <%=date112 %></h3>													
	    				</td> 	
	 				    <td width="20%"></td> 
	 				    <td width="20%"></td>
	 				    			     	
	    				<td align="left" class="heading3" width="20%">
							<h3><bean:message key="knowledgepro.admission.principal"/></h3>													
	    				</td>
	    			</tr>
	 				
	 				</table></td></tr>
			</table>
			</td>
			
		</tr>
		</table>
		</logic:equal>
		<p style="page-break-after: always;"></p>
		</logic:iterate>		
	<script type="text/javascript">
		window.print();
	</script>
	</logic:notEmpty>
	<logic:empty name="transferCertificateForm" property="studentList">
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
