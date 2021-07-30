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
	
		function submitAddMorePreferences(method){
			document.getElementById("method").value=method;
			document.onlineApplicationForm.submit();
		}
	
		function addCourseId(id,preNo){
			if(preNo==1){
				document.getElementById("courseId").value=id;
			}
		}
	
		function resetCourcePreferenceForm() {
			//Educational info
			var preferenceSize=  $('#preferenceSize').val();
			if( parseInt(preferenceSize) > 0){
				for(var i=0;i<parseInt(preferenceSize);i++){
					$('#coursePreference'+i).val("");
				}
			}//errors check over
		}
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
	
	<script language="JavaScript" src="js/calendar_us.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
	<script language="JavaScript" src="js/admission/studentdetails.js"></script>
	
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
 			
	<html:hidden property="courseId" styleId="courseId" name="onlineApplicationForm" />
	<html:hidden property="preferenceSize" styleId="preferenceSize" name="onlineApplicationForm" />
	
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
									<li class="acBlue">Preferences</li>
							     	<li class="acGrey">Personal Details</li>
							     	<li class="acGrey">Education Details</li>
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
  
 		<tr><td height="10"></td></tr>
  
      	<tr>
        	<td>
        		<table width="100%" border="0" cellpadding="7"  align="center">
        			<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">	
        				<nested:iterate id="admissionpreference" name="onlineApplicationForm" property="prefcourses" indexId="count">
					        <%
								String dynaid="coursePreference"+count;;
							%>
          					<tr>
					            <td class="row-odd" width="50%">
					            	<div align="right">
					            		<span class="Mandatory">*</span>
					           			<bean:write name="admissionpreference" property="prefName"></bean:write>:
					           		</div>
					            </td>
					            <td class="row-even" width="70%">
					            	<bean:define id="prefNo" name="admissionpreference" property="prefNo"></bean:define>
					                <nested:select  property="id" styleClass="dropdownmedium"  styleId="<%=dynaid %>" onchange='<%="addCourseId(this.value,"+prefNo+")"%>'>
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:optionsCollection name="onlineApplicationForm" property="courseMap" label="value" value="key"/>	
									</nested:select>
								 	<a href="#" title="Select cources preferences wise" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
					            </td>
							 </tr>
						</nested:iterate> 
					</logic:equal>
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="2">	
						<nested:iterate id="admissionpreference" name="onlineApplicationForm" property="prefcourses" indexId="count">
					        <%
								String dynaid="coursePreference"+count;;
							%>
         	 				<tr>
            					<td class="row-odd" width="30%"><div align="right"><span class="Mandatory">*</span>Subject Selected:</div></td>
					            <td class="row-even" width="70%">
					            	<bean:define id="prefNo" name="admissionpreference" property="prefNo"></bean:define>
									<nested:select  property="id" styleClass="dropdown"  styleId="<%=dynaid %>" onchange='<%="addCourseId(this.value,"+prefNo+")"%>'>
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:optionsCollection name="onlineApplicationForm" property="courseMap" label="value" value="key"/>	
									</nested:select>
								 	<a href="#" title="Select cources preferences wise" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
					            </td>
		 					</tr>
						</nested:iterate> 
					</logic:equal>
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="3">	
						<nested:iterate id="admissionpreference" name="onlineApplicationForm" property="prefcourses" indexId="count">
					        <%
								String dynaid="coursePreference"+count;;
							%>
         	 				<tr>
            					<td class="row-odd" width="30%"><div align="right"><span class="Mandatory">*</span>Subject Selected:</div></td>
					            <td class="row-even" width="70%">
					            	<bean:define id="prefNo" name="admissionpreference" property="prefNo"></bean:define>
									<nested:select  property="id" styleClass="dropdown"  styleId="<%=dynaid %>" onchange='<%="addCourseId(this.value,"+prefNo+")"%>'>
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:optionsCollection name="onlineApplicationForm" property="courseMap" label="value" value="key"/>	
									</nested:select>
								 	<a href="#" title="Select cources preferences wise" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
					            </td>
		 					</tr>
						</nested:iterate> 
					</logic:equal>
		
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">	
		 				<tr>
            				<td colspan="2" width="100%" align="center" style="text-align:center">
					            <a href="url" style="text-decoration:none; vertical-align:middle; color:#007700; font-weight:bold" onclick="submitAddMorePreferences('addPrefereneces'); return false;"> 
								Click here &nbsp;<img src="images/admission/images/12673199831854453770medical cross button.svg.med.png" width="20px"; height="20px"; style="vertical-align:middle;">
								</a> to Add More Choice
							</td>
		 				</tr>
		 			</logic:equal>
		 
		  			<tr>
            			<td colspan="2" width="100%" align="center" style="text-align:center">
				 			<c:if test="${onlineApplicationForm.preferenceSize>1}">      
								<a href="url" style="text-decoration:none; vertical-align:middle; color:#B91A1A; font-weight:bold" onclick="submitAddMorePreferences('removePreferences'); return false;">
									Click here &nbsp;<img src="images/admission/images/15107-illustration-of-a-red-close-button-pv.png" width="20px"; height="20px"; style="vertical-align:middle;">
								</a> to Remove Choice.&nbsp;  &nbsp; &nbsp;
				 			</c:if>
						</td>
		 			</tr>
        		</table>
        	</td>
      	</tr>
      
        <logic:equal value="1" name="onlineApplicationForm" property="programTypeId">
     		<tr><td align="center">Maximum of only <b><%=CMSConstants.MAX_CANDIDATE_PREFERENCES %></b> Preferences allowed.</td></tr>
     	</logic:equal>
     
     	<logic:equal value="2" name="onlineApplicationForm" property="programTypeId">
     		<tr><td align="center">Only <b><%=CMSConstants.MAX_CANDIDATE_PREFERENCES_PG %></b> preferences is allowed.</td></tr>
     	</logic:equal>
      
       	<tr><td align="center"></td></tr>      
 		<tr><td height="20"></td></tr>
  
  		<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
			<tr>
				<td align="center" class="row-odd">
					<table>
						<tr>
							<td align="center" width="50%"><font size="2px"><bean:message key="knowledgepro.applicationform.secLang.label"/></font><span class="Mandatory">*</span> </td>
            				<td width="50%">
					            <html:select property="applicantDetails.personalData.secondLanguage" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="secondLanguage">
									<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
									<html:optionsCollection property="secondLanguageList" label="value" value="value" name="onlineApplicationForm"/>
								</html:select>
			 					<a href="#" title="Choose second language for UG course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>            
							</td>	
						</tr>
					</table>
				</td>           		
          	</tr>
		</logic:equal>
		
		<tr><td height="20px"></td></tr>
		
   		<tr class="row-odd">
   			<td align="center" colspan="2" width="100%">
	   			<table width="100%">
					<tr>
						<td align="center" width="50%"><b><font size="2px">FIRST DEGREE PROGRAMMES (AIDED PROGRAMMES)</font></b></td>
						<td align="center" width="50%"><b><font size="2px">FIRST DEGREE PROGRAMMES (UNAIDED PROGRAMMES)</font></b></td>
					</tr>
					<tr>
						<td>
							<table border="1" style=" border-collapse: collapse;">
								<tr>
									<th width="5%">No</th>
									<th width="10%">DEGREE</th>
									<th width="30%">PROGRAMME</th>
									<th width="55%">COMPLEMENTARY COURSE</th>
								</tr>
								<tr>
									<td rowspan="2">1</td>
									<td rowspan="2">B.A</td>
									<td rowspan="2">Economics</td>
									<td>1. Political Science</td>
								</tr>
								<tr>
									<td>2. History of Modern India/Mathematics</td>
								</tr>
								<tr>
									<td rowspan="2">2</td>
									<td rowspan="2">B.A</td>
									<td rowspan="2">English Language and Literature</td>
									<td>1. History of English Language & Literature</td>
								</tr>
								<tr>
									<td>2. History of the Modern World</td>
								</tr>
								<tr>
									<td rowspan="2">3</td>
									<td rowspan="2">B.A</td>
									<td rowspan="2">Journalism, Mass Communication &amp; Video Production</td>
									<td>1. Creative Writing: Malayalam</td>
								</tr>
								<tr>
									<td>2. Creative Writing : English</td>
								</tr>
								
								<tr>
									<td rowspan="2">4</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Mathematics</td>
									<td>1. Physics</td>
								</tr>
								<tr>
									<td>2. Statistics</td>
								</tr>
								<tr>
									<td rowspan="2">5</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Physics</td>
									<td>1. Mathematics</td>
								</tr>
								<tr>
									<td>2. Chemistry</td>
								</tr>
                                                                 <tr>
									<td rowspan="2">6</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Physics with Machine Learning</td>
									<td>1. Mathematics</td>
								</tr>
								<tr>
									<td>2. Machine Learning</td>
								</tr>

								<tr>
									<td rowspan="2">7</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Chemistry</td>
									<td>1. Mathematics</td>
								</tr>
								<tr>
									<td>2. Physics</td>
								</tr>
								<tr>
									<td rowspan="2">8</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Botany</td>
									<td>1. Chemistry</td>
								</tr>
								<tr>
									<td>2. Zoology</td>
								</tr>
								<tr>
									<td rowspan="2">9</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Zoology</td>
									<td>1. Chemistry</td>
								</tr>
								<tr>
									<td>2. Botany</td>
								</tr>
								<tr>
									<td>10</td>
									<td>B.Sc</td>
									<td>Botany &amp; Biotechnology</td>
									<td>1. Biochemistry</td>
								</tr>
								<tr>
									<td>11</td>
									<td>B.Com</td>
									<td>Commerce</td>
									<td>1. Finance/Computer Applications/Co-operation (Electives)</td>
								</tr>
							</table>
						</td>
						<td valign="top">
							<table border="1" style=" border-collapse: collapse;">
								<tr>
									<th width="5%">No</th>
									<th width="10%">DEGREE</th>
									<th width="30%">PROGRAMME</th>
									<th width="55%">COMPLEMENTARY COURSE</th>
								</tr>
                                                                <tr>
									<td  rowspan="2">1</td>
									<td  rowspan="2">B.A</td>
									<td  rowspan="2">Analytical Economics </td>
									<td>1. Political Science</td>
								</tr>
                                                                <tr>
									<td>2. History / Mathematics</td>
								</tr>


								<tr>
									<td rowspan="2">2</td>
									<td rowspan="2">B.A</td>
									<td rowspan="2">English Language and Literature </td>
									<td>1. History of English Language &amp; Literature</td>
								</tr>
								<tr>
									<td>2. History of the Modern World</td>
								</tr>


                                                                <tr>
									<td>3</td>
									<td>B.A</td>
									<td>English & Communicative English  </td>
									<td>1. History of English Literature</td>
								</tr>
                                                                <tr>
									<td>4</td>
									<td>B.Sc</td>
									<td>Computer Science  </td>
									<td>1. Mathematics </td>
								</tr>

                                                                 <tr>
									<td rowspan="2">5</td>
									<td rowspan="2">B.Sc</td>
									<td rowspan="2">Statistics  </td>
									<td>1. Mathematics </td>
								</tr>
                                                                 <tr>
									<td>2. Computer Science</td>
								</tr>


								<tr>
									<td>6</td>
									<td>B.Com</td>
									<td>Commerce </td>
									<td>1. Finance </td>
								</tr>
								<tr>
									<td>7</td>
									<td>B.Com</td>
									<td>B.Com Accounts &amp; Audit  </td>
									<td>1. Accounts &amp; Audit </td>
								</tr>
                                                                
                                                                

							</table>
						</td>
					</tr>			
				</table>
   			</td>
   		</tr>
  		<tr>
  			<td width="100%" align="center">
    			<html:button property="" onclick="submitAdmissionForm('submitPriferencePageOnline')" styleClass="cntbtn" value="Save & Continue to Personal Details">  </html:button>
  			</td>
  		</tr>
	  	<tr>
	  		<td width="100%" align="center">
	  			<br/>
	   			<html:button property="" value="Clear" styleClass="btn1" onclick="resetCourcePreferenceForm();" /> 
	   			&nbsp; <html:button property="" onclick="cancel()" styleClass="cancelbtn" value="Logout"></html:button>
	  		</td>
	  	</tr>
   		<tr><td height="40px"></td></tr>
</table>	