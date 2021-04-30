<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@page import="com.kp.cms.constants.KPPropertiesConfiguration"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
	<script type="text/javascript" src="jquery/collaps/jquery-1.10.2.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
	<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>

 	<script type="text/javascript">
		setStartDate();
		setInterval ( "checkSession()", 300000 );
	</script>

 	<style type="text/css">
		input[type="radio"]:focus, input[type="radio"]:active {
		    -webkit-box-shadow:inset 2px 1px 1px , 1px 1px 3px #008000;
		    -moz-box-shadow:inset 2px 1px 1px #008000, 1px 1px 3px #008000;
		    box-shadow:inset 2px 1px 1px #008000, 1px 1px 3px #008000;
		}
 	</style>
 
	<style type="text/css">
	
		.tooltip{
   			display: inline;
    		position: relative;
		}
		
		.tooltip:hover:after{
    		background: #333;
    		background: rgba(0,0,0,.8);
    		border-radius: 5px;
    		bottom: 26px;
    		color: #fff;
    		content: attr(title);
    		left: 20%;
    		padding: 5px 15px;
    		position: absolute;
    		z-index: 98;
    		width: 220px;
		}
		
		.tooltip:hover:before{
    		border: solid;
    		border-color: #333 transparent;
    		border-width: 6px 6px 0 6px;
    		bottom: 20px;
    		content: "";
    		left: 50%;
    		position: absolute;
    		z-index: 99;
		}
	
	</style>
	
 	<link rel="stylesheet" href="css/admission/css/stylesheet-pure-css.css"/>  
 
	<!-- for cache controling with html code-->
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
 
	<!-- for cache controling with jsp code-->
	<% 
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
	%>

	<script language="JavaScript" src="js/calendar_us.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
	<script language="JavaScript" src="js/admission/studentdetails.js"></script>
	<html:hidden property="focusValue" styleId="focusValue" name="onlineApplicationForm"/>
	<html:hidden property="onlineApply" styleId="onlineApply" name="onlineApplicationForm"/>
	<html:hidden property="formName" styleId="onlineApplicationForm" value="onlineApplicationForm" />
	<html:hidden property="tempUniversityId" styleId="tempUniversityId" value="onlineApplicationForm" />
	
 			
	<table width="80%" style="background-color: #F0F8FF" align="center">
	
		<tr><td height="5px"></td></tr>
	
		<tr>
    		<td>
				<table width="100%" align="center" border="0">
					<tr>
						<td align="center">
							<div id="nav-menu">
								<ul>
									<li class="acGreen">Terms &amp; Conditions</li>
									<li class="acGreen">Payment</li>
									<li class="acGreen">Preferences</li>
							     	<li class="acGreen">Personal Details</li>
							     	<li class="acBlue">Education Details</li>
								 	<li class="acGrey">Upload Photo</li>
  	 							</ul>
   							</div>
  	 					</td>
   					</tr>
    			</table>
    		</td>
  		</tr>
  	
	  	<tr><td height="10"></td></tr>
	   
	   	<!-- errors display -->
	  	<tr>
			<td align="center">
				<div id="errorMessage" align="center">
					<FONT color="red"><html:errors /></FONT>
					<FONT color="green"><html:messages id="msg"	property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						</html:messages>
					</FONT>
				</div>
				<div id="errorMessage1" style="font-size: 11px; color: red"></div>
			</td>
		</tr>
	  
	 	<tr><td height="30"></td></tr>
	    
	    <%String qualificationListSize= session.getAttribute("eduQualificationListSize").toString();  %> 
	    <%String dynaStyle=""; %>
	    <html:hidden property="ednQualificationListSize" styleId="ednQualificationListSize" name="onlineApplicationForm" value="<%=qualificationListSize %>"/>		
			
		<logic:notEmpty name="onlineApplicationForm" property="applicantDetails.ednQualificationList">							
	    	<nested:iterate  name="onlineApplicationForm" property="applicantDetails.ednQualificationList" indexId="count" id="qualDoc">
	 
	 	        <%
					String dynamicStyle="";
					String oppStyle="";
					String dynaid="UniversitySelect"+count;
					String dynaYearId="YOP"+count;
					String dynamonthId="Month"+count;
					String dynaExamId="Exam"+count;
					String dynaAttemptId="Attempt"+count;
					String dynaStateId="State"+count;
					String dynarow1="University"+count;
					String dynarow2="Institute"+count;
					String instituteId=count+"Institute";
					String labelOtherUniversity = "otherUniversityLabel";
					String courseSettingsJsMethod="getMarkEntryAvailable(this,'"+count+"');";
					String collegeJsMethod="getColleges('Map"+count+"',this,"+count+");";
					String instituteJsMethod="funcOtherInstituteShowHide('"+instituteId+"','"+dynarow2+"','"+count+"');";
					String showhide=collegeJsMethod+"funcOtherUniversityShowHide('"+dynarow1+"','"+dynaid+"','"+dynarow2+"','"+instituteId +"','" + labelOtherUniversity + "');";
					if(count%2!=0) {
						dynamicStyle="row-white";
						oppStyle="row-even";
					}else {
						dynamicStyle="row-even";
						oppStyle="row-white";
					}
					String dynaMap="Map"+count;
					
					String isExamConfigured="isExamConfigured_"+count;
					String blockedMarsks="blockedMarks_"+count;
					String showHideBlockMarsk="showHideExamPassYearMonth_"+count;
					
					String marksObtained="marksObtained_"+count;
					String maxMarks="maxMarks_"+count;
					String lastExam="lastExam_"+count;
					String consolidated="consolidated_"+count;
					String checkAlertMarksObtained="checkAlertMarksObtained("+count+")";
					String checkAlertMaxMarks="checkAlertMaxMarks("+count+")";
					String checkAlertMarksObtainedBySemisterWise="checkAlertMarksObtainedBySemisterWise("+count+")";
					String checkAlertMaxMarksSemisterWise="checkAlertMaxMarksSemisterWise("+count+")";
					String previousRegNo="previousRegNo"+count;
					String percentage="percentage_"+count;
					String dynarow3="Institute1"+count;
				%>
				
				 <!-- doc name -->
				 <tr>
		         	<td>
		         		<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;">
		         			<tr>
					         	<c:choose>
									<c:when test="${qualDoc.docTypeId==6}">
										<td height="23" align="center" class="subheading"><span style="color: white;">Degree</span></td>
									</c:when>
									<c:otherwise>
										<td height="23" align="center" class="subheading"><span style="color: white;"><nested:write property="docName" name="qualDoc"/></span></td>
									</c:otherwise>
								</c:choose>
		         			</tr>
		       	 		</table>
		       		</td>
		  		</tr>	
					
				<tr>
					<td>	
						<bean:define id="countIndex" name="qualDoc" property="countId"></bean:define>
						<input type="hidden" id="countID" name="countID" >
		    
		   				<table width="100%" align="center" cellpadding="4">
		         
			         		<!-- raghu exam name -->	
			         		<logic:equal value="true" name="qualDoc" property="examConfigured">
					 			<html:hidden property="isExamConfigured" styleId="<%=isExamConfigured %>" name="onlineApplicationForm" value="true"/>		
				   				<tr>
				   					<td class="row-odd" align="right" width="40%">Type of Examination<span class="Mandatory">*</span></td>
									<td class="row-even" width="50%">
										<c:set var="dexmid"><%=dynaExamId %></c:set>
										<nested:select property="selectedExamId" styleClass="dropdownmedium" styleId='<%=dynaExamId %>'>
											<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
											<logic:notEmpty name="qualDoc" property="examTos">
											<html:optionsCollection name="qualDoc" property="examTos" label="name" value="id"/>
											</logic:notEmpty>	
										</nested:select>
										<a href="#" title="Select exam" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
										<script type="text/javascript">
											var exmid= '<nested:write property="selectedExamId"/>';
											document.getElementById("<c:out value='${dexmid}'/>").value = exmid;
										</script>
									</td>
				   				</tr>
					  		</logic:equal>
					
				         	<logic:notEqual value="true" name="qualDoc" property="examConfigured">
								<html:hidden property="isExamConfigured" styleId="<%=isExamConfigured %>" name="onlineApplicationForm" value="false"/>
						 	</logic:notEqual>
					
							<!-- raghu university/board name -->	
			        
					    	<tr>
								<td class="row-odd" align="right"  width="40%">
									<div>University/Board<span class="Mandatory">*</span></div>
									<div><span id="<%=labelOtherUniversity%>"></span></div>
								</td>           
			            		<td class="row-even" width="50%"> 
			            			<div align="left">
			               				<c:set var="dunid"><%=dynaid %></c:set>
						               	<nested:select property="universityId" styleId="<%=dynaid %>" styleClass="dropdownmedium" onchange="<%=showhide %>">
											<option value="">Select</option>
											<logic:notEmpty name="qualDoc" property="universityList">
												<logic:iterate id="option"  name="qualDoc" property="universityList">
													<option value='<bean:write name="option" property="id"/>'><bean:write name="option" property="name"/> </option>
												</logic:iterate>
											</logic:notEmpty>
											<option value="Other">Other</option>
						               	</nested:select>
										<script type="text/javascript">
											var id= '<nested:write property="universityId"/>';
											document.getElementById("<c:out value='${dunid}'/>").value = id;
										</script>
										<a href="#" title="Select university from options,if not there please select other option and write university name in text box." class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
									</div>
									<logic:equal value="Other" name="qualDoc" property="universityId">
				                		<%dynaStyle="display: block;" ;%>
									</logic:equal>
									<logic:notEqual value="Other" name="qualDoc" property="universityId">
				                  		<%dynaStyle="display: none;" ;%>
									</logic:notEqual>
									<div align="left" >
										<nested:text property="universityOthers" styleClass="textboxmedium" size="10" maxlength="50" style='<%=dynaStyle %>' styleId='<%=dynarow1 %>' onkeypress="IsAlpha(event)" ></nested:text>
			                	 	</div>
			                  	</td>
							</tr>
			        
			        		<!-- raghu institution name -->	
				
			     			<tr style="display: none;">
					   			<td class="row-odd" align="right" width="40%">
									<bean:message key="knowledgepro.admission.instituteName" /><span class="Mandatory">*</span>
								</td>
			            		<td class="row-even">
			           				<div align="left">
										<c:set var="dinid"><%=instituteId %></c:set>
			                			<c:set var="temp"><nested:write property="universityId"/></c:set>
						                <nested:select property="institutionId" styleClass="dropdownmedium" styleId='<%=instituteId %>' onchange='<%=instituteJsMethod %>' >
											<option value="">-Select-</option>
											<c:set var="tempKey">Map<c:out value="${count}"/></c:set>	
											<c:if test="${temp != null && temp != '' && temp != 'Other'}">
					                             <c:set var="Map" value="${baseActionForm.collectionMap[tempKey]}"/>
												<c:if test="${Map!=null}">
												<html:optionsCollection name="Map" label="value" value="key"/>
												</c:if>
											</c:if>
											<html:option value="Other">Other</html:option>
										</nested:select>
										<script type="text/javascript">
											var inId= '<nested:write property="institutionId"/>';
											document.getElementById("<c:out value='${dinid}'/>").value = inId;
										</script>
										<a href="#" title="Select institute from options,if not there please select other option and write institute name in text box." class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
									</div>
					            	<logic:equal value="Other" name="qualDoc" property="institutionId">
				                  		<%dynaStyle="display: block;" ;%>
									</logic:equal>
										<logic:notEqual value="Other" name="qualDoc" property="institutionId">
				                  	<%dynaStyle="display: none;" ;%>
									</logic:notEqual>
									<div align="left">
										<nested:text property="institutionId" styleClass="textboxmedium" size="10" maxlength="50" style='<%=dynaStyle %>' styleId='<%=dynarow2 %>' onkeypress="IsAlpha(event)"></nested:text>
			                  		</div>
								</td>
							</tr>
							<tr>
					   			<td class="row-odd" align="right" width="40%">
									<bean:message key="knowledgepro.admission.instituteName" /><span class="Mandatory">*</span>
								</td>
			            		<td class="row-even">
						           	<div align="left">
										<nested:text property="otherInstitute" styleClass="textboxmedium" size="10" maxlength="50"  styleId='<%=dynarow3 %>' onkeypress="IsAlpha(event)"></nested:text>
							            <a href="#" title="Select institute from options,if not there please select other option and write institute name in text box." class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						            </div>
								</td>
							</tr>
			         
			         		<!-- raghu edn state name -->	
				
			    			<tr>
								<td class="row-odd" align="right" width="40%"><bean:message key="admissionForm.education.State.label"/><span class="Mandatory">*</span></td>
			        			<td class="row-even">
									<c:set var="dstateid"><%=dynaStateId %></c:set>
									<nested:select property="stateId" styleClass="dropdownmedium" styleId='<%=dynaStateId %>'>
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
											<logic:notEmpty name="onlineApplicationForm" property="ednStates">
												<nested:optionsCollection name="onlineApplicationForm" property="ednStates" label="name" value="id"/>
											</logic:notEmpty>
										<option value="OUTSIDEINDIA"><bean:message key="admissionForm.education.outsideindia.label"/></option>
									</nested:select>
									<script type="text/javascript">
										var stid= '<nested:write property="stateId"/>';
										document.getElementById("<c:out value='${dstateid}'/>").value = stid;
									</script>
									<a href="#" title="Select state where you studied" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				   				</td>
			    			</tr>
			            
							<!-- raghu passing year  details name -->	
					
			      			<tr>
			    				<td class="row-odd" align="right" width="40%">
			    					<bean:message key="knowledgepro.admission.passingYear"/><span class="Mandatory">*</span>
			    				</td>
			            		<td class="row-even">
									<c:set var="dyopid"><%=dynaYearId %></c:set>
									<nested:select property="yearPassing" styleId='<%=dynaYearId %>' styleClass="dropdownmedium">
										<html:option value="">Select</html:option>
						              	<cms:renderYear normalYear="true"></cms:renderYear>
									</nested:select>
									<script type="text/javascript">
										var yopid= '<nested:write property="yearPassing"/>';
										if(yopid!=0)
											document.getElementById("<c:out value='${dyopid}'/>").value = yopid;
									</script>
									<a href="#" title="Select year of passing exam" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			             		</td>
			       			</tr>
			       
			       			<!-- raghu passing  month details name -->	
			          
			        		<tr>
			      	    		<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.applicationform.passingmonth"/><span class="Mandatory">*</span></td>
			            		<td class="row-even">
									<c:set var="dmonid"><%=dynamonthId %></c:set>
									<nested:select property="monthPassing" styleId='<%=dynamonthId %>' styleClass="dropdownmedium">
										<html:option value="0">Select</html:option>
										<html:option value="1">JAN</html:option>
						              	<html:option value="2">FEB</html:option>
										<html:option value="3">MAR</html:option>
										<html:option value="4">APR</html:option>
										<html:option value="5">MAY</html:option>
										<html:option value="6">JUN</html:option>
										<html:option value="7">JUL</html:option>
										<html:option value="8">AUG</html:option>
										<html:option value="9">SEPT</html:option>
										<html:option value="10">OCT</html:option>
										<html:option value="11">NOV</html:option>
										<html:option value="12">DEC</html:option>
									</nested:select>
									<script type="text/javascript">
										var monid= '<nested:write property="monthPassing"/>';
										document.getElementById("<c:out value='${dmonid}'/>").value = monid;
									</script>
									<a href="#" title="Select month of passing exam" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			           			</td>
			       			</tr>
			          
			          		<!-- raghu exam attempts details name -->	
					
			       			<tr>
			      		 		<td class="row-odd" align="right" width="40%">
			      		 			<bean:message key="knowledgepro.admission.attempts"/><span class="Mandatory">*</span>
			      		 		</td>
			        			<td class="row-even">
									<c:set var="dAttemptid"><%=dynaAttemptId %></c:set>
			                		<nested:select property="noOfAttempts" styleId='<%=dynaAttemptId %>' styleClass="dropdownmedium">
								 		<option value="">Select</option>
								 		<option value="1">1</option>
								 		<option value="2">2</option>
								 		<option value="3">3</option>
								 		<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
			                		</nested:select>
									<script type="text/javascript">
										var dAttemid= '<nested:write property="noOfAttempts"/>';
										if(dAttemid!=0)
										document.getElementById("<c:out value='${dAttemptid}'/>").value = dAttemid;
									</script>
									<a href="#" title="Select number of attempts" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			           			</td>
							</tr>
					
							<!-- raghu exam pre register no details name -->	 
							<logic:equal value="true" name="qualDoc" property="lastExam">
					 			<tr>
				        			<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.applicationform.prevregno.label"/><span class="Mandatory">*</span></td>
				            		<td class="row-even"><nested:text property="previousRegNo" styleId='<%=previousRegNo %>' styleClass="textboxmedium" size="10" maxlength="15"/>
				            			<a href="#" title="Enter previous exam register number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				            		</td>
			         			</tr>
			         		</logic:equal>
			          
			          		<!-- raghu cosolidate marks details name -->	
							<nested:equal value="true" property="consolidated" name="qualDoc">
			            		<tr>
			            			<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.percentage"/><span class="Mandatory">*</span></td>
			          	 			<td class="row-even">
			              				<nested:text property="percentage" styleId='<%=percentage %>' styleClass="textboxmedium" size="5" maxlength="8" onkeypress='return isNumberKey(event)'></nested:text>
			              				<a href="#" title="Enter your percentage" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			           				</td>
			           			</tr>
			         		</nested:equal>
			         
			         		<tr><td colspan="2" height="20"></td></tr>
			         
			         		<nested:equal value="false"  property="consolidated" name="qualDoc">
			         
			         			<!-- raghu semster marks details name -->	
								<nested:equal value="true"  property="semesterWise" name="qualDoc">
									<tr>
			          		  	 		<td align="center" colspan="2"><span class="Mandatory">*</span><a href="#" style=" color:#FF0000;font-size:large;text-decoration:none; font-family:Arial, Helvetica, sans-serif" onclick="detailSemesterSubmit('<%=countIndex %>')"><bean:message key="knowledgepro.applicationform.semsestermark.link"/></a>
			          		  	 			<a href="#" title="Click here for entering semester marks" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			          		  	 		</td>
									</tr>
								</nested:equal>
							
								<!-- raghu doc marks details name -->	
					
								<nested:equal value="false"  property="semesterWise" name="qualDoc">
									<!-- raghu -->
									<c:choose>
										<c:when test="${qualDoc.docTypeId==6}">
											<tr>
												<td align="center" colspan="2">
													<div align="center">
														<span class="Mandatory">*</span> 
														<a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" >For Degree Marks Entry </a>
														<html:button property="" value=" Click Here " styleClass="btn1" onclick='<%="detailSubmitDegree("+countIndex+")"%>' />
														<a href="#" title="Click here for entering DEGREE marks" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
													</div>
												</td>
											</tr>
										</c:when>
										<c:when test="${qualDoc.docTypeId==9}">
											<tr>
												<td align="center" colspan="2">
													<div align="center">
														<span class="Mandatory">*</span> 
														<a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" >Click here for entering class 12 marks</a>
														<html:button property="" value=" Click Here " styleClass="btn1" onclick='<%="detailSubmitClass12("+countIndex+")"%>' />
														<a href="#" title="Click here for entering CLASS12 marks" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
													</div>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
							          		<tr>
							          		   	<td align="center" colspan="2">
							          		   		<div align="center">
							          		   			<span class="Mandatory">*</span>  
							          		    		<a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" >For <nested:write property="docName" name="qualDoc"/> Marks Entry</a>
							          		    		<html:button property="" value=" Click Here " styleClass="btn1" onclick='<%="detailSubmit("+countIndex+")"%>' />
							          		   			<a href="#" title="Click here for entering marks" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							          		   		</div>
							          		   	</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</nested:equal>
							</nested:equal>
			        	</table>
					</td>
				</tr>
		  
		    	<tr><td height="30"></td></tr>
	    
	    	</nested:iterate>
	   	</logic:notEmpty>
	 
	   	<tr>
	    	<td>
	    		<table width="100%"  align="center" cellpadding="4">
	    			<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
						<tr>
							<td width="50%" class="row-odd" align="right">
								Stream Under Class 12:<span class="Mandatory">*</span>
							</td>
					        <td width="50%" class="row-even">
								<nested:select property="applicantDetails.personalData.stream" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="stream">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<html:optionsCollection property="streamMap" name="onlineApplicationForm" label="value" value="key"/>
								</nested:select>
							 	<a href="#" title="Select stream in Class 12" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
					    </tr>
					</logic:equal>		                        
		
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="2">
						<tr>
							<td width="50%" class="row-odd" align="right">
								<span class="Mandatory">*</span>Qualifying Under Graduate Program:
							</td>
					        <td width="50%" class="row-even">
								<nested:select property="applicantDetails.personalData.ugcourse" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="ugcourse">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<html:optionsCollection property="ugcourseList" name="onlineApplicationForm" label="name" value="id"/>
								</nested:select>
								<a href="#" title="Select qualifying under graduate program" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
					    </tr>
	    			</logic:equal>
	    
	    		</table>
	    	</td>
	  	</tr>
	   
	   	<!-- raghu student  entrance details code start here -->	
		<logic:equal value="true" property="displayEntranceDetails" name="onlineApplicationForm">								
											
			<tr><td  height="20"></td></tr>
			<tr>
	    		<td width="100%">
	    			<table width="100%" align="center"  border="0" style="border-collapse:collapse">
	      				<tr>
	        				<td height="23" align="center" class="subheading"><bean:message key="admissionForm.education.entrancedetails.label"/></td>
	      				</tr>
	    			</table>
	    		</td>
	  		</tr>					
			<tr>
	        	<td>
	        		<table width="100%" align="center" cellpadding="4">
	     				<tr>
	            			<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="admissionForm.education.entrance.label"/></div></td>
							<td class="row-even" width="50%" height="25">
								<div align="left">
									<nested:select property="applicantDetails.entranceDetail.entranceId" styleId="entranceId" styleClass="dropdownmedium" name="onlineApplicationForm">
										<html:option value="">-Select-</html:option>
										<logic:notEmpty property="entranceList" name="onlineApplicationForm">
											<html:optionsCollection property="entranceList" name="onlineApplicationForm" label="name" value="id"/>
										</logic:notEmpty>
					   				</nested:select>
					   				<a href="#" title="Enter entrance exam name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				   				</div>
				   			</td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.admission.totalMarks"/>:</div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:text property="applicantDetails.entranceDetail.totalMarks" styleId="totalMarks" styleClass="textboxmedium"  name="onlineApplicationForm"  maxlength="8"></nested:text>
									<a href="#" title="Enter total mark" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="admissionForm.education.markObtained.label"/>:</div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:text property="applicantDetails.entranceDetail.marksObtained" styleId="marksObtained" styleClass="textboxmedium"  name="onlineApplicationForm"  maxlength="8"></nested:text>
									<a href="#" title="Enter obtain mark" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
	           			</tr>
						<tr>
	            			<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.applicationform.prereq.roll.label"/></div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:text property="applicantDetails.entranceDetail.entranceRollNo" styleClass="textboxmedium" name="onlineApplicationForm" styleId="entranceRollNo" maxlength="25"></nested:text>
									<a href="#" title="Enter entrance roll no" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.applicationform.passingmonth"/>:</div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:select property="applicantDetails.entranceDetail.monthPassing"  styleClass="dropdownmedium" styleId="monthPassing" name="onlineApplicationForm">
										<html:option value="0"><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:option value="1">JAN</html:option>
									    <html:option value="2">FEB</html:option>
										<html:option value="3">MAR</html:option>
										<html:option value="4">APR</html:option>
										<html:option value="5">MAY</html:option>
										<html:option value="6">JUN</html:option>
										<html:option value="7">JUL</html:option>
										<html:option value="8">AUG</html:option>
										<html:option value="9">SEPT</html:option>
										<html:option value="10">OCT</html:option>
										<html:option value="11">NOV</html:option>
										<html:option value="12">DEC</html:option>
								   	</nested:select>
							   		<a href="#" title="Select entrance month" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
						</tr>
												
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.admission.passingYear"/>:</div></td>
							<td class="row-even" width="50%" height="25">
								<div align="left">
									<nested:select property="applicantDetails.entranceDetail.yearPassing" styleId='entranceyear' styleClass="dropdownmedium"  name="onlineApplicationForm">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									    <cms:renderYear normalYear="true"></cms:renderYear>
									</nested:select>
									<script type="text/javascript">
										var entyopid= '<nested:write property="applicantDetails.entranceDetail.yearPassing"  name="onlineApplicationForm"/>';
										document.getElementById("entranceyear").value = entyopid;
									</script>
									<a href="#" title="Select entrance year" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
		            	</tr>
					</table>
				</td>
			</tr>							
		</logic:equal>						
					
		<tr><td  height="20"></td></tr>
	  
		<!-- raghu student  entrance details code over here -->	
	  	<tr>
	  		<td>
	  			<table width="100%" align="center" cellpadding="4"   >
	      			<tr>
	        			<td width="100%" align="center">
	        				<div>
	        					<html:button property="" styleClass="cntbtn" styleId="SubmitEducationDetailPage" value="Save and continue for signature and photo" > </html:button>
							</div>						
	         			</td>
	      			</tr>
	      			<tr>
	        			<td width="100%" align="center">
	        				<div>
	        					<br/>
	        					<html:button property="" value="Clear" styleClass="btn1" onclick="resetEducationalForm();" /> 
			 					&nbsp;
			 					<html:button property="" onclick="cancel()" styleClass="cancelbtn" value="Logout"></html:button>
							</div>						
	         			</td>
	      			</tr>
	  			</table>
	  		</td>
	  	</tr>
	  
	  	<tr><td height="30"></td></tr>
	
	</table>	
	<script language="JavaScript" src="js/admission/OnlineDetailsEducationInfo.js"></script>	