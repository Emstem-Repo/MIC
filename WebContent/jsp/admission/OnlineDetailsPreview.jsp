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
		
		function enableParish()
		{
			document.getElementById("parish_description").style.display = "block";
		}
		function disableParish()
		{
			document.getElementById("parish_description").style.display = "none";
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
	<html:hidden property="programId" styleId="programId" name="onlineApplicationForm"/>
	<html:hidden property="programYear" styleId="programYear" name="onlineApplicationForm"/>
	<html:hidden property="courseId" styleId="courseId" name="onlineApplicationForm"/>
	<html:hidden property="focusValue" styleId="focusValue" name="onlineApplicationForm"/>
	<html:hidden property="onlineApply" styleId="onlineApply" name="onlineApplicationForm"/>
	<html:hidden property="pageType" value="18" />
	<html:hidden property="indianCandidate" styleId="indianCandidate" name="onlineApplicationForm"/>
	<html:hidden property="singlePageAppln" value="true"/>
 	<html:hidden property="selectedAppNo" value="" />
	<html:hidden property="selectedYear" value="" />
	<html:hidden property="detailsView" value="false" />
	<html:hidden property="pageType" value="18" />
	<html:hidden property="reviewed" styleId="reviewed" name="onlineApplicationForm"/>
	<input type="hidden" name="courseId" id="courseId" value='<bean:write	name="onlineApplicationForm" property="applicantDetails.course.id" />' />
	<html:hidden property="checkReligionId" styleId="checkReligionId" />
 	<html:hidden property="secondLanguage" styleId="secondLanguage" />
		<html:hidden property="tempUniversityId" styleId="tempUniversityId" name="onlineApplicationForm" />
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
							     	<li class="acGreen">Education Details</li>
								 	<li class="acGreen">Upload Photo</li>
  	 							</ul>
   							</div>
  	 					</td>
   					</tr>
    			</table>
    		</td>
  		</tr>
  
  		<tr><td height="30"></td></tr>
	
	 	<tr>
    		<td width="100%">
    			<table align="center" width="40%" border="0" style="border-collapse:collapse">
      				<tr>
        				<td height="23" align="center" class="subheading">Programme Preferences</td>
      				</tr>
    			</table>
    		</td>
  		</tr>
  
	 	<tr>
        	<td>
        		<div align="center">
        			<table width="100%" border="0" cellpadding="7"  align="center"  >
        				<nested:iterate id="admissionpreference" name="onlineApplicationForm" property="prefcourses" indexId="count">
          					<tr>
            					<td class="row-odd" width="40%" align="right">
            						<bean:write name="admissionpreference" property="prefName"></bean:write>:<span class="Mandatory">*</span>
            					</td>
             					<td class="row-even" width="60%">
                    				<nested:select  disabled="true" property="id" styleClass="dropdown"  styleId="coursePreference1" >
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:optionsCollection name="onlineApplicationForm" property="courseMap" label="value" value="key"/>	
									</nested:select>
									<a href="#" title="Select cources preferences wise" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
		   						</td>
		 					</tr>
						</nested:iterate> 
						<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
							<tr>
								<td class="row-odd" align="right" width="50%"><bean:message key="knowledgepro.applicationform.secLang.label"/><span class="Mandatory">*</span> </td>
					            <td class="row-even" width="50%">
						            <html:select property="applicantDetails.personalData.secondLanguage" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="secondLanguage">
										<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
										<html:optionsCollection property="secondLanguageList" label="value" value="value" name="onlineApplicationForm"/>
									</html:select>
									 <a href="#" title="Choose second language for UG course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>            
								</td>	
							</tr>
						</logic:equal>
        			</table>
        		</div>	
        	</td>
      	</tr>
      
      	<tr><td height="30"></td></tr>
      
   		<tr>
    		<td width="100%">
    			<table align="center" width="100%" border="0" style="border-collapse:collapse">
      				<tr>
        				<td height="30" align="center" class="subheading">Personal Details</td>
      				</tr>
    			</table>
    		</td>
  		</tr>

      	<tr>
        	<td>
        		<table width="100%" border="0" cellpadding="4"  align="center">
          			<tr>
			            <td class="row-odd" width="25%" align="right"><bean:message key="knowledgepro.applicationform.candidateName"/><span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%" >
            				<nested:text readonly="true" property="applicantDetails.personalData.firstName" styleId="firstNameId" name="onlineApplicationForm"  maxlength="90" styleClass="textbox"></nested:text>						
            				<a href="#" title="Enter your name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
             			</td>
            			<td class="row-odd" width="25%" align="right"><bean:message key="admissionForm.studentinfo.bloodgroup.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%">
							<input type="hidden" id="BGType" name="BGType" value='<bean:write name="onlineApplicationForm" property="bloodGroup"/>'/>
                         	<nested:select disabled="true" property="applicantDetails.personalData.bloodGroup" name="onlineApplicationForm" styleClass="dropdown" styleId="bgType">
								<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
								<html:option value="O+VE"><bean:message key="knowledgepro.admission.report.opositive"/></html:option>
								<html:option value="A+VE"><bean:message key="knowledgepro.admission.report.apositive"/></html:option>
								<html:option value="B+VE"><bean:message key="knowledgepro.admission.report.bpositive"/></html:option>
								<html:option value="AB+VE"><bean:message key="knowledgepro.admission.report.abpositive"/></html:option>
								<html:option value="O-VE"><bean:message key="knowledgepro.admission.report.onegitive"/></html:option>
								<html:option value="A-VE"><bean:message key="knowledgepro.admission.report.anegitive"/></html:option>
								<html:option value="B-VE"><bean:message key="knowledgepro.admission.report.bnegitive"/></html:option>
								<html:option value="AB-VE"><bean:message key="knowledgepro.admission.report.abnegitive"/></html:option>
								<html:option value="NOT KNOWN"><bean:message key="knowledgepro.admission.report.unknown"/></html:option>
							</nested:select>	
			 				<a href="#" title="Select your blood group" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
          			</tr>
          			<tr>
			            <td class="row-odd" align="right">
				            <bean:message key="admissionForm.studentinfo.dob.label"/>
				            <bean:message key="admissionForm.application.dateformat.label"/><span class="Mandatory">*</span>
			            </td>
            			<td class="row-even">
             				<nested:text readonly="true" name="onlineApplicationForm" property="applicantDetails.personalData.dob" styleId="dateOfBirth"  maxlength="11" styleClass="textbox"></nested:text>
              				<a href="#" title="Select your date of birth" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
             			<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.residentcatg.label2"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<div align="left">
            					<input type="hidden" id="tempResidentCategory" value="<nested:write property="applicantDetails.personalData.residentCategory" name="onlineApplicationForm" />">
								<nested:select disabled="true" property="applicantDetails.personalData.residentCategory" name="onlineApplicationForm" styleClass="dropdown" styleId="residentCategory" >
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<nested:optionsCollection name="onlineApplicationForm" property="residentTypes" label="name" value="id"/>
								</nested:select>
								<a href="#" title="Select your resident category" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
            			</td>
          			</tr>
          			<tr>
            			<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.sex.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
			                <nested:radio disabled="true" property="applicantDetails.personalData.gender" styleId="MALE" name="onlineApplicationForm" value="MALE"></nested:radio><bean:message key="admissionForm.studentinfo.sex.male.text"/>
							<nested:radio disabled="true" property="applicantDetails.personalData.gender" name="onlineApplicationForm" styleId="FEMALE" value="FEMALE"><bean:message key="admissionForm.studentinfo.sex.female.text"/></nested:radio>
							<nested:radio disabled="true" property="applicantDetails.personalData.gender" name="onlineApplicationForm" styleId="TRANSGENDER" value="TRANSGENDER"><bean:message key="admissionForm.studentinfo.sex.transgender.text"/></nested:radio>
							<a href="#" title="Select your gender" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
		             	<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.nationality.label"/><span class="Mandatory">*</span></td>
			            <td class="row-even">
				            <div align="left">
				            	<input type="hidden" id="nationalityhidden" name="nationality" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.nationality"/>"/>
					            <nested:select disabled="true" property="applicantDetails.personalData.nationality" styleClass="dropdown" styleId="nationality" name="onlineApplicationForm">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<%String selected=""; %>
									<logic:iterate id="option" property="nationalities" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_NATIONALITY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_NATIONALITY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%=selected %> ><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>
							 	<a href="#" title="Select your nationality" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
			            </td>
					</tr>
          			<tr>
          				<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.phone.label"/> </td>
           				<td class="row-even">
            				<nested:text readonly="true" styleClass="textboxsmall" property="applicantDetails.personalData.phNo2" name="onlineApplicationForm"  maxlength="7" size="7" onkeypress="return isNumberKey(event)" />
           					<nested:text readonly="true" styleClass="textboxmedium" property="applicantDetails.personalData.phNo3" name="onlineApplicationForm" styleId="applicantphNo3" maxlength="10" size="10" onkeypress="return isNumberKey(event)"/>
			 				<a href="#" title="Enter your phone code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
           				<td class="row-odd" align="right" style="display: none;">Birth <bean:message key="admissionForm.studentinfo.birthdetails.country.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" style="display: none;">
							<div align="left">
				 				<input type="hidden" id="birthCountryhidden" name="birthCountry" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.birthCountry"/>"/>
								<nested:select disabled="true" property="applicantDetails.personalData.birthCountry" name="onlineApplicationForm" styleClass="dropdown" styleId="birthCountry" onchange="getStates(this.value,'birthState');">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<%String selected=""; %>
									<logic:iterate id="option" property="countries" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%= selected %>><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>
								<a href="#" title="Select bitrh country" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.mobile.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
		              		<nested:text readonly="true" styleClass="textboxsmall"  property="applicantDetails.personalData.mobileNo1" name="onlineApplicationForm" styleId="applicantMobileCode" maxlength="4" size="4" onkeypress="return isNumberKey(event)" ></nested:text>
		              		<nested:text readonly="true" styleClass="textboxmedium" property="applicantDetails.personalData.mobileNo2" name="onlineApplicationForm" styleId="applicantMobileNo" maxlength="10" size="10" onkeypress="return isNumberKey(event)"></nested:text>
		              		<a href="#" title="Enter your mobile code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
           				</td>
						<%String dynaStyle=""; %>
						<logic:equal value="Other" property="birthState" name="onlineApplicationForm">
							<%dynaStyle="display:block;"; %>
						</logic:equal>
						<logic:notEqual value="Other" property="birthState" name="onlineApplicationForm">
							<%dynaStyle="display:none;"; %>
						</logic:notEqual>
            			<td class="row-odd" align="right" style="display: none;">Birth <bean:message key="admissionForm.studentinfo.birthdetails.state.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" style="display: none;">
            				<div align="left">
	            				<input type="hidden" id="birthState1" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.birthState"/>">
	            				<nested:select disabled="true" property="applicantDetails.personalData.birthState" name="onlineApplicationForm"	styleId="birthState" styleClass="dropdown" onchange="funcOtherShowHide('birthState','otherBirthState')">
									<html:option value="">- Select -</html:option>
									<logic:notEmpty property="stateMap" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="stateMap" label="value" value="key" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
				 				<a href="#" title="Select your birth state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
           					<div align="left">
            					<nested:text readonly="true" property="applicantDetails.personalData.stateOthers"  name="onlineApplicationForm"	maxlength="30" styleId="otherBirthState" style="<%=dynaStyle %>" styleClass="textbox"></nested:text>
            				</div>
            			</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.email.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<div align="left">
            					<nested:text readonly="true" property="applicantDetails.personalData.email" styleId="applicantEmail" name="onlineApplicationForm" styleClass="textbox"  maxlength="50"></nested:text>
             					<a href="#" title="Enter your mail" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					<br/>(e.g. name@yahoo.com)
            				</div>
            			</td>
            			<td class="row-odd" align="right" style="display: none;">Birth <bean:message key="admissionForm.studentinfo.birthdetails.place.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" style="display: none;">
            				<nested:text readonly="true" property="applicantDetails.personalData.birthPlace"  styleId="birthPlace" name="onlineApplicationForm"  maxlength="50" styleClass="textbox"></nested:text>
             				<a href="#" title="Enter your birth place" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
          			</tr>
          		</table>
       		</td>
      	</tr>
      
       	<tr><td height="30"></td></tr>

  		<tr>
    		<td width="100%">
    			<table align="center" width="40%" border="0" style="border-collapse:collapse" >
      				<tr>
        				<td height="30" align="center" class="subheading">Current Address </td>
      				</tr>
    			</table>
    		</td>
  		</tr>
 
      	<tr>
        	<td>
        		<table align="center" cellpadding="4">
          			<tr>
			            <td class="row-odd" align="right" width="25%">House Name/House Number:<span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%">
			            	<nested:text readonly="true" property="applicantDetails.personalData.currentAddressLine1" styleId="currentAddressLine1" styleClass="textbox" name="onlineApplicationForm" maxlength="35"></nested:text>
							<a href="#" title="Enter your house name/house number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
            			<td class="row-odd" align="right" width="25%">Country:<span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%">
			            	<div align="left">
			            		<input type="hidden" id="currentCountryNamehidden" name="nationality" name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.currentCountryId"/>"/>
								<nested:select disabled="true" property="applicantDetails.personalData.currentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="currentCountryName" onchange="getTempAddrStates(this.value,'tempAddrstate');">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<%String selected=""; %>
									<logic:iterate id="option" property="countries" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%= selected %>><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>
								<a href="#" title="Enter your country" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            				</div>
            			</td>
          			</tr>
          			<tr>
            			<td class="row-odd" align="right"> Post Office Name:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text readonly="true" property="applicantDetails.personalData.currentAddressLine2" styleId="currentAddressLine2" styleClass="textbox" name="onlineApplicationForm" maxlength="40"></nested:text>
							<a href="#" title="Enter your post office name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
            			<td class="row-odd" align="right">State:<span class="Mandatory">*</span></td>
        				<td class="row-even">
             				<div align="left">
        						<input type="hidden" id="currentStateId1" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.currentStateId"/>">
								<logic:equal value="Other" property="applicantDetails.personalData.currentStateId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>
								</logic:equal>
								<logic:notEqual value="Other" property="applicantDetails.personalData.currentStateId" name="onlineApplicationForm">
									<%dynaStyle="display:none;"; %>
								</logic:notEqual>
		 						<nested:select disabled="true" property="applicantDetails.personalData.currentStateId" name="onlineApplicationForm"  styleClass="dropdown" styleId="tempAddrstate" onchange="funcOtherShowHide('tempAddrstate','otherTempAddrState');getTempAddrDistrict(this.value,'tempAddrdistrict');">
									<html:option value="">- Select -</html:option>
									<logic:notEmpty property="stateMap" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="curAddrStateMap" label="value" value="key" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Enter your current state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
	   						<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.currentAddressStateOthers" styleClass="textbox" name="onlineApplicationForm" maxlength="30" styleId="otherTempAddrState" style="<%=dynaStyle %>"></nested:text></div>
     					</td>
    	 			</tr>
          			<tr>
            			<td class="row-odd" align="right"> Pin Code:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text readonly="true" styleClass="textbox" property="applicantDetails.personalData.currentAddressZipCode" styleId="currentAddressZipCode" name="onlineApplicationForm"  maxlength="10"/>
            				<a href="#" title="Enter your current pincode" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
            			<td class="row-odd" align="right">District:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<logic:equal value="Other" property="applicantDetails.personalData.currentDistricId" name="onlineApplicationForm">
								<%dynaStyle="display:block;"; %>
							</logic:equal>
							<logic:notEqual value="Other" property="applicantDetails.personalData.currentDistricId" name="onlineApplicationForm">
								<%dynaStyle="display:none;"; %>
							</logic:notEqual>
							<div align="left">
								<nested:select disabled="true" property="applicantDetails.personalData.currentDistricId" name="onlineApplicationForm" styleClass="dropdown" styleId="tempAddrdistrict" onchange="funcOtherShowHide('tempAddrdistrict','otherTempAddrDistrict');">
									<html:option value="">- Select -</html:option>
									<c:if test="${onlineApplicationForm.applicantDetails.personalData.currentStateId != null && onlineApplicationForm.applicantDetails.personalData.currentStateId!= ''}">
                       					<c:set var="tempAddrDistrictMap" value="${baseActionForm.collectionMap['tempAddrDistrictMap']}"/>
                       		    	 	<c:if test="${tempAddrDistrictMap != null}">
                       		    	 		<html:optionsCollection name="tempAddrDistrictMap" label="value" value="key"/>
                       		    	 	</c:if> 
			                        </c:if>
			                        <logic:notEmpty property="editCurrentDistrict" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="editCurrentDistrict" label="name" value="id" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Enter your current district" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
							<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.currentAddressDistrictOthers" name="onlineApplicationForm" styleClass="textbox" size="10" maxlength="30" styleId="otherTempAddrDistrict" style="<%=dynaStyle %>"></nested:text></div>
            			</td>
          			</tr>
          			<tr>
            			<td class="row-odd" align="right">City:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text readonly="true" property="applicantDetails.personalData.currentCityName" styleId="currentCityName" styleClass="textbox" name="onlineApplicationForm" maxlength="30"></nested:text>
							<a href="#" title="Enter your city name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
			            <td class="row-odd">&nbsp;</td>
            			<td class="row-even">&nbsp;</td>
          			</tr>
        		</table>
        	</td>
      	</tr>
 		
 		<tr><td height="30"></td></tr>
 		 
		<tr>
			<td>
    			<table align="center" width="100%" border="0" style="border-collapse:collapse">
      				<tr>
       					<td height="30" align="center" class="subheading">
       				 		<bean:message key="knowledgepro.applicationform.sameaddr.label"/> &nbsp; 
       				 	 	<html:radio disabled="true" property="sameTempAddr" styleId="sameAddr" value="true" onclick="disableTempAddress();"><bean:message key="knowledgepro.applicationform.yes.label"/></html:radio>
					     	<html:radio disabled="true" property="sameTempAddr" styleId="DiffAddr" value="false" onclick="enableTempAddress();"><bean:message key="knowledgepro.applicationform.No.label"/></html:radio>
							<a href="#" title="Select parent address if same as current address " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
     				</tr>
    			</table>
    		</td>
  		</tr>
          
      	<tr>
      		<td width="100%">
      			<div id="currLabel">
      				<table align="center" width="100%" border="0" style="border-collapse:collapse" >
      					<tr>
        					<td height="30" align="center" class="subheading">Permanent Address </td>
      					</tr>
    				</table>
    			</div>
    		</td>
    	</tr>
 
      	<tr>
        	<td>
        		<div id="currTable">
        			<table  align="center" cellpadding="4">
          				<tr>
            				<td class="row-odd" align="right" width="25%">House Name/House Number:<span class="Mandatory">*</span></td>
            				<td class="row-even" width="25%">
            					<nested:text readonly="true" property="applicantDetails.personalData.permanentAddressLine1" styleId="permanentAddressLine1" styleClass="textbox" name="onlineApplicationForm"  maxlength="35"></nested:text>
								<a href="#" title="Enter your house number and house name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
            				<td class="row-odd" align="right" width="25%">Country:<span class="Mandatory">*</span></td>
            				<td class="row-even" width="25%">
            					<div align="left">
            						<input type="hidden" id="permanentCountryNamehidden" name="nationality" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.permanentCountryId"/>"/>
									<nested:select disabled="true" property="applicantDetails.personalData.permanentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="permanentCountryName" onchange="getPermAddrStates(this.value,'permAddrstate');">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<%String selected=""; %>
										<logic:iterate id="option" property="countries" name="onlineApplicationForm">
											<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
											<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
											<option value='<bean:write name="option" property="id"/>' <%= selected %>><bean:write name="option" property="name"/> </option>
										</logic:iterate>
									</nested:select>
									<a href="#" title="Selcet your permanent country" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</div>
            				</td>
          				</tr>
          
			          	<tr>
            				<td class="row-odd" align="right"> Post Office Name:<span class="Mandatory">*</span></td>
            				<td class="row-even">
           						<nested:text readonly="true" property="applicantDetails.personalData.permanentAddressLine2" styleClass="textbox"  name="onlineApplicationForm"  maxlength="40"></nested:text>
								<a href="#" title="Enter your permanent post office name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
            				<td class="row-odd" align="right">State:<span class="Mandatory">*</span></td>
        					<td class="row-even">
       							<input type="hidden" id="permanentState" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.permanentStateId"/>">
								<logic:equal value="Other" property="applicantDetails.personalData.permanentStateId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>
								</logic:equal>
								<logic:notEqual value="Other" property="applicantDetails.personalData.permanentStateId" name="onlineApplicationForm">
									<%dynaStyle="display:none;"; %>
								</logic:notEqual>
								<div align="left">
              						<nested:select disabled="true" property="applicantDetails.personalData.permanentStateId" name="onlineApplicationForm" styleClass="dropdown" styleId="permAddrstate" onchange="funcOtherShowHide('permAddrstate','otherPermAddrState');getPermAddrDistrict(this.value,'permAddrdistrict');">
										<html:option value="">- Select -</html:option>
										<logic:notEmpty property="stateMap" name="onlineApplicationForm" >
											<html:optionsCollection name="onlineApplicationForm" property="perAddrStateMap" label="value" value="key" />
										</logic:notEmpty>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Enter your permanenet state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
								<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.permanentAddressStateOthers" name="onlineApplicationForm" styleClass="textbox" maxlength="30" styleId="otherPermAddrState" style="<%=dynaStyle %>"></nested:text></div>
        					</td>
        				</tr>
     
          				<tr>
            				<td class="row-odd" align="right"> Pin Code:<span class="Mandatory">*</span></td>
            				<td class="row-even">
            					<nested:text readonly="true" styleClass="textbox" property="applicantDetails.personalData.currentAddressZipCode" styleId="permanentAddressZipCode" name="onlineApplicationForm"  maxlength="10"/>
            					<a href="#" title="Enter your permanent pincode" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            				</td>
            				<td class="row-odd" align="right">District:<span class="Mandatory">*</span></td>
            				<td class="row-even">
           						<logic:equal value="Other" property="applicantDetails.personalData.permanentDistricId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>
								</logic:equal>
								<logic:notEqual value="Other" property="applicantDetails.personalData.permanentDistricId" name="onlineApplicationForm">
									<%dynaStyle="display:none;"; %>
								</logic:notEqual>
								<div align="left">
                     				<nested:select disabled="true" property="applicantDetails.personalData.permanentDistricId" name="onlineApplicationForm" styleClass="dropdown" styleId="permAddrdistrict" onchange="funcOtherShowHide('permAddrdistrict','otherPermAddrDistrict');">
										<html:option value="">- Select -</html:option>
										<c:if test="${onlineApplicationForm.applicantDetails.personalData.permanentStateId != null && onlineApplicationForm.applicantDetails.personalData.permanentStateId!= ''}">
                           					<c:set var="permAddrDistrictMap" value="${baseActionForm.collectionMap['permAddrDistrictMap']}"/>
                           		    	 	<c:if test="${permAddrDistrictMap != null}">
                           		    	 		<html:optionsCollection name="permAddrDistrictMap" label="value" value="key"/>
                           		    	 	</c:if> 
			                        	</c:if>
			                         	<logic:notEmpty property="editPermanentDistrict" name="onlineApplicationForm">
											<html:optionsCollection name="onlineApplicationForm" property="editPermanentDistrict" label="name" value="id" />
										</logic:notEmpty>
										<html:option value="Other">Other</html:option>
				   					</nested:select>
				   					<a href="#" title="Enter your permanent district" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				   				</div>
				  				<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.permanentAddressDistrictOthers" name="onlineApplicationForm" size="10" maxlength="30" styleClass="textbox" styleId="otherPermAddrDistrict" style="<%=dynaStyle %>"></nested:text></div>
            				</td>
          				</tr>
          
          				<tr>
            				<td class="row-odd" align="right">City:<span class="Mandatory">*</span></td>
				            <td class="row-even">
					            <nested:text readonly="true" property="applicantDetails.personalData.permanentCityName" styleId="permanentCityName" styleClass="textbox" name="onlineApplicationForm"  maxlength="30"></nested:text>
								<a href="#" title="Enter your permanent city" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
				            <td class="row-odd">&nbsp;</td>
				            <td class="row-even">&nbsp;</td>
          				</tr>
		  
        			</table>
        		</div>
        	</td>
      	</tr>
   				           	  
       	<tr><td height="30"></td></tr>
       
   		<tr>
    		<td width="100%">
    			<table align="center" width="40%" border="0" style="border-collapse:collapse">
      				<tr>
        				<td height="30" align="center" class="subheading">Parent Information</td>
      				</tr>
    			</table>
    		</td>
  		</tr>
  
      	<tr>
        	<td>
        		<table width="100%"  align="center" cellpadding="4"  >
          			<tr>
            			<td class="row-odd" align="right" width="25%"><bean:message key="knowledgepro.admission.fatherName" /><span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%">
            				<div align="left">
	            				<nested:select disabled="true" property="applicantDetails.titleOfFather" styleId='titleOfFather' name="onlineApplicationForm" styleClass="dropdownsmall" onchange="fatherIncomeMandatory()">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="Mr">Mr.</html:option>
									<html:option value="Late">Late.</html:option>
								</nested:select>
								<nested:text readonly="true" property="applicantDetails.personalData.fatherName" styleId="fatherName" name="onlineApplicationForm" styleClass="textboxmedium" maxlength="50"></nested:text>
								<a href="#" title="Enter name of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>								
						</td>
			            <td class="row-odd" align="right" width="25%">
							<bean:message key="knowledgepro.admission.motherName" /><span class="Mandatory">*</span>
						</td>
						<td class="row-even" width="25%">
							<div align="left">
								<nested:select disabled="true" property="applicantDetails.titleOfMother" styleId='titleOfMother' name="onlineApplicationForm" styleClass="dropdownsmall" onchange="motherIncomeMandatory()">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="Mrs">Mrs.</html:option>
									<html:option value="Late">Late.</html:option>
								</nested:select>
								<nested:text readonly="true" property="applicantDetails.personalData.motherName" styleId="motherName"  name="onlineApplicationForm" styleClass="textboxmedium" maxlength="50"></nested:text>
								<a href="#" title="Enter name of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
          			</tr>
          			<tr>
			            <td class="row-odd" width="25%" align="right">
							<bean:message key="knowledgepro.admin.occupation" />:
						</td>
						<td class="row-even" width="25%" >
							<div align="left">
								<input type="hidden" id="hiddenFatherOccupationId"  name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.fatherOccupationId"/>"/>
								<nested:select disabled="true" property="applicantDetails.personalData.fatherOccupationId" name="onlineApplicationForm" styleClass="dropdown" styleId="fatherOccupation" onchange="displayOtherForFather(this.value)">
									<html:option value="">- Select -</html:option>
									<html:optionsCollection name="onlineApplicationForm" property="occupations" label="occupationName" value="occupationId"/>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Select occupation of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
							<div align="left" id="displayFatherOccupation" >
								<nested:text readonly="true" property="applicantDetails.personalData.otherOccupationFather" name="onlineApplicationForm" styleClass="textbox" maxlength="50" styleId="otherOccupationFather"/>
							</div>
						</td>
			 			<logic:equal value="Other" property="applicantDetails.personalData.motherOccupationId" name="onlineApplicationForm">
							<%dynaStyle="display:block;"; %>
						</logic:equal>
						<logic:notEqual value="Other" property="applicantDetails.personalData.motherOccupationId" name="onlineApplicationForm">
							<%dynaStyle="display:none;"; %>
						</logic:notEqual>
            			<td class="row-odd" width="25%" align="right"><bean:message	key="knowledgepro.admin.occupation" />:</td>
            			<td class="row-even" width="25%">
							<div align="left">
			 					<nested:select disabled="true" property="applicantDetails.personalData.motherOccupationId" name="onlineApplicationForm" styleClass="dropdown" styleId="motherOccupation" onchange="displayOtherForMother(this.value)">
									<html:option value="">- Select -</html:option>
									<html:optionsCollection name="onlineApplicationForm" property="occupations" label="occupationName" value="occupationId"/>
 									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Select occupation of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
		   					</div>
							<div align="left" id="displayMotherOccupation" >
								<nested:text readonly="true" property="applicantDetails.personalData.otherOccupationMother" name="onlineApplicationForm" styleClass="textbox"   maxlength="50"  styleId="otherOccupationMother"/>
							</div>
            			</td>
          			</tr>
          			<tr>
			            <td class="row-odd"><div align="right"><bean:message key="admissionForm.studentinfo.mobile.label" /><span class="Mandatory">*</span></div></td>
						<td class="row-even" align="left">
							<nested:text readonly="true" styleId="fatherMobile" property="applicantDetails.personalData.parentMob1" name="onlineApplicationForm" size="4" maxlength="4" styleClass="textboxsmall" ></nested:text> <nested:text readonly="true" styleId="fatherMobile1" property="applicantDetails.personalData.fatherMobile" name="onlineApplicationForm" size="15" maxlength="10" styleClass="textboxmedium"></nested:text>
							<a href="#" title="Enter father mobile code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
            			<td class="row-odd">
							<div align="right"><bean:message key="admissionForm.studentinfo.mobile.label" /></div>
						</td>
						<td class="row-even" align="left">
							<nested:text readonly="true" styleId="motherMobile" property="applicantDetails.personalData.parentMob1" name="onlineApplicationForm" size="4" maxlength="4" styleClass="textboxsmall"></nested:text> <nested:text readonly="true" styleId="motherMobile1" property="applicantDetails.personalData.motherMobile" name="onlineApplicationForm" size="15" maxlength="10" styleClass="textboxmedium"></nested:text>
							<a href="#" title="Enter mother mobile code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
             			</td>
          			</tr>
           			<tr>
            			<td class="row-odd" width="25%" align="right"><bean:message key="admissionForm.studentinfo.email.label"/></td>
						<td class="row-even">
							<div align="left">
								<nested:text readonly="true" property="applicantDetails.personalData.fatherEmail" name="onlineApplicationForm" styleClass="textbox" maxlength="50"></nested:text>
								<a href="#" title="Enter email id of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
		    				<div align="left">(e.g. name@yahoo.com)</div>
						</td>
           				<td class="row-odd" width="25%" align="right"><bean:message key="admissionForm.studentinfo.email.label" /></td>
           				<td class="row-even">
           					<div align="left">
           						<nested:text readonly="true" property="applicantDetails.personalData.motherEmail" styleId="motherEmail" styleClass="textbox" name="onlineApplicationForm"  maxlength="50"></nested:text>
           						<a href="#" title="Enter email id of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
           					</div>
							<div align="left">(e.g. name@yahoo.com)</div>
           				</td>
       				</tr>
          		</table>
          	</td>
      	</tr>
      
       	<tr><td height="30"></td></tr>
      
		<tr>
			<td>
    			<table align="center" width="100%" border="0" style="border-collapse:collapse">
      				<tr>
       					<td height="30" align="center" class="subheading">
       				    	<bean:message key="knowledgepro.applicationform.sameaddrparent.label"/> &nbsp;
       				 		<html:radio disabled="true" property="sameParentAddr" styleId="sameParAddr" value="true" onclick="setParentAddress();"><bean:message key="knowledgepro.applicationform.yes.label"/></html:radio>
							<html:radio disabled="true" property="sameParentAddr" styleId="DiffParAddr" value="false" onclick="disParentAddress();"><bean:message key="knowledgepro.applicationform.No.label"/></html:radio>
							<a href="#" title="Select parent address yes, if same as current address  " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
     				</tr>
    			</table>
    		</td>
  		</tr>
					
		<tr>
    		<td width="100%">
    			<table align="center" width="40%" border="0" style="border-collapse:collapse">
      				<tr>
       					<td height="30" align="center" class="subheading">Parent Address</td>
     				</tr>
    			</table>
    		</td>
  		</tr>
  					
		<tr>
            <td>
            	<table width="100%"  align="center" cellpadding="4">
					<tr>
						<td class="row-odd" width="25%">
							<div align="right">House Name/House Number:<span class="Mandatory">*</span></div>
						</td>
						<td class="row-even" width="25%" align="left">
							<nested:text readonly="true" styleId="parentAddressLine1" property="applicantDetails.personalData.parentAddressLine1" styleClass="textbox" name="onlineApplicationForm" size="15" maxlength="100" ></nested:text>
							<a href="#" title="Enter your parent house name or number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
						<td class="row-odd" width="25%"  align="right"><bean:message key="admissionForm.studentinfo.mobile.label"/><span class="Mandatory">*</span></td>
           				<td class="row-even" width="25%" >
							<div align="left">
	           					<nested:text readonly="true" styleClass="textboxsmall" property="applicantDetails.personalData.parentMob1" styleId="parentMobile" name="onlineApplicationForm"  maxlength="4" size="4" onkeypress="return isNumberKey(event)" onkeydown="hideGuardenPhoneNumber()"  ></nested:text>
					   			<nested:text readonly="true" styleClass="textboxmedium" property="applicantDetails.personalData.parentMob2"  styleId="parentMobile1" name="onlineApplicationForm"  maxlength="10" size="10" onkeypress="return isNumberKey(event)" onkeydown="hideGuardenPhoneNumber()"  ></nested:text>
						 		<a href="#" title="Enter your parent mobile code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
					 		</div>
						</td>
					</tr>
					<tr>
						<td class="row-odd"><div align="right">Post Office Name:<span class="Mandatory">*</span></div></td>
						<td class="row-even">
							<nested:text readonly="true" styleId="parentAddressLine2" property="applicantDetails.personalData.parentAddressLine2" styleClass="textbox" name="onlineApplicationForm" size="15" maxlength="100"></nested:text>
							<a href="#" title="Enter your parent post office name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
						<td class="row-odd" align="right"><bean:message key="knowledgepro.admin.country" /><span class="Mandatory">*</span></td>
						<td class="row-even">
							<div align="left">
								<input type="hidden" id="hiddenParentCountryId" name="nationality" name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.parentCountryId"/>"/>
								<nested:select disabled="true" property="applicantDetails.personalData.parentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="parentCountryName" onchange="getParentAddrStates(this.value,'parentState')">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<%String selected=""; %>
									<logic:iterate id="option" property="countries" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_COUNTRY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%= selected %>><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>
								<a href="#" title="Enter your parent country" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
					</tr>
					<tr>
						<td class="row-odd" align="right"><bean:message	key="knowledgepro.admission.zipCode" /><span class="Mandatory">*</span></td>
						<td class="row-even" align="left">
							<nested:text readonly="true" styleId="parentAddressZipCode" property="applicantDetails.personalData.parentAddressZipCode" styleClass="textbox" name="onlineApplicationForm" size="10" maxlength="10"></nested:text>
							<a href="#" title="Select your parent ZIP code" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
						<td class="row-odd"><div align="right"><bean:message key="knowledgepro.admin.state" /><span class="Mandatory">*</span></div></td>
						<td class="row-even">
							<div align="left">
								<%String dynastyle=""; %>
								<logic:equal value="Other" property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm">
									<%dynastyle="display:block;"; %>
								</logic:equal>
								<logic:notEqual value="Other" property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm">
									<%dynastyle="display:none;"; %>
								</logic:notEqual>
					            <nested:select disabled="true" property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm" styleClass="dropdown" styleId="parentState" onchange="funcOtherShowHide('parentState','otherParentAddrState');">
									<html:option value="">- Select -</html:option>
									<logic:notEmpty property="parentStateMap" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="parentStateMap" label="value" value="key" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Enter your parent state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>				
							<div align="left">
								<nested:text readonly="true" property="applicantDetails.personalData.parentAddressStateOthers" styleClass="textbox" name="onlineApplicationForm" maxlength="30" styleId="otherParentAddrState" style="<%=dynastyle %>"></nested:text>
							</div>
						</td>
					</tr>
					<tr>
            			<td class="row-odd" width="25%" >
							<div align="right"><bean:message key="knowledgepro.admin.city" />:<span class="Mandatory">*</span></div>
						</td>
						<td class="row-even" width="25%" align="left">
							<nested:text readonly="true" styleId="parentCityName" property="applicantDetails.personalData.parentCityName" styleClass="textbox" name="onlineApplicationForm" size="15" maxlength="30"></nested:text>
							<a href="#" title="Enter your parent city" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
           				<td class="row-odd"></td>
            			<td class="row-even"></td>
         			</tr>					
				</table>
			</td>
      	</tr>
		
		<tr><td height="30"></td></tr>
	    
	    <tr>
			<td width="100%">
				<table align="center" width="40%" border="0" style="border-collapse:collapse">
					<tr>
						<td height="30" align="center" class="subheading">Reservation Details</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>
			<td>
				<table align="center" cellpadding="4"  >
 					<tr>
   						<td class="row-odd" valign="top" align="right" width="25%"><bean:message key="admissionForm.studentinfo.religion.label"/><span class="Mandatory">*</span></td>
   						<td class="row-even" width="25%">
     						<logic:equal value="Other" property="religionId" name="onlineApplicationForm">
								<%dynaStyle="display:block;"; %>
							</logic:equal>
							<logic:notEqual value="Other" property="religionId" name="onlineApplicationForm">
								<%dynaStyle="display:none;"; %>
							</logic:notEqual>
       						<div align="left">
								<input type="hidden" id="religionType" name="religionType" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.religionId"/>'/>
								<logic:notEqual value="Other" property="applicantDetails.personalData.religionId" name="onlineApplicationForm">
									<%dynaStyle="display:none;"; %>
									<nested:select disabled="true" property="applicantDetails.personalData.religionId" name="onlineApplicationForm" styleClass="dropdown" styleId="religions" onchange="getSubCaste(this.value,'castCatg');funcOtherShowHide('religions','otherReligion');funcReligionShowHide(this.value);">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<nested:optionsCollection name="onlineApplicationForm" property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Select your religion" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</logic:notEqual>
								<logic:equal value="Other" property="applicantDetails.personalData.religionId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>												
									<nested:select disabled="true" property="applicantDetails.personalData.religionId" name="onlineApplicationForm" styleClass="dropdown" styleId="religions" onchange="getSubCaste(this.value,'castCatg');funcOtherShowHide('religions','otherReligion');funcReligionShowHide(this.value);">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<nested:optionsCollection name="onlineApplicationForm" property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Select your religion" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							  	</logic:equal>
							</div>
							<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.religionOthers" styleClass="textbox"  name="onlineApplicationForm"   maxlength="30" styleId="otherReligion" style="<%=dynaStyle %>"></nested:text></div>
							<logic:equal name="onlineApplicationForm" property="applicantDetails.personalData.religionId" value="3">
								<%dynaStyle="display:block;"; %>	
							</logic:equal>
						</td>
            			<%String dynaStyle4="display:none;"; %>
						<logic:equal value="true" property="handicapped" name="onlineApplicationForm">
							<%dynaStyle4="display:block;"; %>
						</logic:equal>
         				<td class="row-odd" align="right" width="25%"><bean:message key="knowledgepro.applicationform.physical.label"/></td>
       					<td class="row-even" width="29%" valign="top">
         					<div align="left">
                				<input type="hidden" id="hiddenHandicaped" name="hiddenHandicaped" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.handicapped"/>'/>
             					<nested:radio disabled="true" property="applicantDetails.personalData.handicapped" styleId="handicappedYes" name="onlineApplicationForm" value="true" onclick="showHandicappedDescription()"></nested:radio>
                           		<label for="handicappedYes"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
		 						<nested:radio disabled="true" property="applicantDetails.personalData.handicapped" styleId="handicappedNo" name="onlineApplicationForm" value="false" onclick="hideHandicappedDescription()"></nested:radio>
				           		<label for="handicappedNo"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
		 						<a href="#" title="Select if you are physically challenged person" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
			  				<div align="left" id="handicapped_description" style="display: none;" >
	 							PWD Category:<input type="hidden" id="pwdType" name="pwdType" value='<bean:write name="onlineApplicationForm" property="pwdType"/>'/>
                        		<nested:select disabled="true" property="applicantDetails.personalData.hadnicappedDescription" name="onlineApplicationForm" styleClass="dropdown" styleId="pwdType">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="Visually Handicapped">Visually Handicapped</html:option>
									<html:option value="Hearing Impaired">Hearing Impaired</html:option>
									<html:option value="Orthopeadic">Orthopeadic</html:option>											
									<html:option value="Cerebral Palsy">Cerebral Palsy</html:option>
								</nested:select>	
			 					<a href="#" title="Select the type of disability" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								Percentage of disability:<nested:text disabled="true" styleId="hadnicappedDescription" property="applicantDetails.personalData.handicapedPercentage" name="onlineApplicationForm" size="7" maxlength="30" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
				 				<a href="#" title="Percentage of disability " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
		 				</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right" width="25%"><bean:message key="admissionForm.studentinfo.subreligion.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%">
           					<div align="left">
								<input type="hidden" id="casteType" name="casteType" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.casteId"/>'/>
								<logic:equal value="Other" property="applicantDetails.personalData.casteId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>
								</logic:equal>
								<logic:notEqual value="Other" property="applicantDetails.personalData.casteId" name="onlineApplicationForm">
									<%dynaStyle="display:none;"; %>
								</logic:notEqual>
                 				<nested:select disabled="true" property="applicantDetails.personalData.casteId" name="onlineApplicationForm" styleId="castCatg" styleClass="dropdown" onchange="funcOtherShowHide('castCatg','otherCastCatg')">
				  					<option value="">-Select-</option>
		  							<c:if test="${onlineApplicationForm.applicantDetails.personalData.religionId != null && onlineApplicationForm.applicantDetails.personalData.religionId != ''}">
										<c:set var="subCasteMap" value="${baseActionForm.collectionMap['subCasteMap']}"/>
			              				<c:if test="${subCasteMap != null}">
			               					<html:optionsCollection name="subCasteMap" label="value" value="key"/>
			             				</c:if> 
				  					</c:if>
									<logic:notEmpty property="subCasteMap" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="subCasteMap" label="value" value="key" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Select your caste" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
							<div align="left"><nested:text readonly="true" property="applicantDetails.personalData.casteOthers" name="onlineApplicationForm" size="10" maxlength="30" styleId="otherCastCatg" styleClass="textbox" style="<%=dynaStyle %>"></nested:text></div>
            			</td>
             			<td class="row-odd" align="right"><div align="right">Participation in Cultural Activities:</div></td>
						<td class="row-even" align="left">
							<div align="left">
	                      		<input type="hidden" id="arts" name="ats" value='<bean:write name="onlineApplicationForm" property="arts"/>'/>
                         		<nested:select disabled="true" property="applicantDetails.personalData.arts" name="onlineApplicationForm" styleClass="dropdown" styleId="arts1">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="International Level">International Level</html:option>
									<html:option value="National Level">National Level</html:option>
									<html:option value="State Level">State Level</html:option>
									<html:option value="Participated">Inter college Level</html:option>
									<html:option value="District Level">District Level</html:option>
									<html:option value="None">None</html:option>
								</nested:select>
								<a href="#" title="Select your level of participation" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
           			</tr>
		   			<tr>
 		    			<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.castcatg.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<div align="left">
	        					<nested:select disabled="true" property="applicantDetails.personalData.subReligionId" name="onlineApplicationForm" styleClass="dropdown" styleId="subreligion" onchange="funcOtherShowHide('subreligion','otherSubReligion')">
									<html:option value="">- Select -</html:option>
		    						<html:optionsCollection property="subReligionMap" name="onlineApplicationForm" label="value" value="key"/>
								</nested:select>
								<a href="#" title="Select your caste category" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
            			</td>
           				<td class="row-odd" align="right" ><div align="right">If Yes Achievement In Cultural Activities:</div></td>
						<td class="row-even" align="left">
							<div align="left">
								<input type="hidden" id="artsParticipate" name="artsParticipate" value='<bean:write name="onlineApplicationForm" property="artsParticipate"/>'/>
                         		<nested:select disabled="true" property="applicantDetails.personalData.artsParticipate" name="onlineApplicationForm" styleClass="dropdown" styleId="artsParticipate">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="1 Prize">1 Prize</html:option>
									<html:option value="2 Prize">2 Prize</html:option>
									<html:option value="3 Prize">3 Prize</html:option>
									<html:option value="Participated">Participated</html:option>
									<html:option value="None">None</html:option>
								</nested:select>
								<a href="#" title="Select your achievement" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						 	</div>
						</td>
                 	</tr>
					<tr>
						<td class="row-odd" align="right">Family Annual Income: <span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<div align="left">
            				<%--	<nested:select style="display:none" property="applicantDetails.personalData.fatherCurrencyId" name="onlineApplicationForm"  styleId="fatherCurrency">
           		 					<%String selected=""; %>
									<logic:iterate id="option" property="currencyList" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_CURRENCY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_CURRENCY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%=selected %> ><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>	
					            <nested:select disabled="true" property="applicantDetails.personalData.fatherIncomeId" name="onlineApplicationForm" styleClass="dropdown" styleId="fatherIncomeRange">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<nested:optionsCollection name="onlineApplicationForm" property="incomeList" label="incomeRange" value="id"/>
								</nested:select>--%>
								<nested:text styleId="fatherIncomeRange" property="applicantDetails.personalData.familyAnnualIncome" name="onlineApplicationForm" size="15" maxlength="15" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
			 					<a href="#" title="Select the annual income of your family" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
						<td class="row-odd" align="right" ><div align="right">Participation in Sports:</div></td>
	            		<td class="row-even" align="left">
	            			<div align="left">
		            			<input type="hidden" id="sports" name="sports" value='<bean:write name="onlineApplicationForm" property="sports"/>'/>
	                			<nested:select disabled="true" property="applicantDetails.personalData.sports" name="onlineApplicationForm" styleClass="dropdown" styleId="sports1">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="International Level">International Level</html:option>
									<html:option value="National Level">National Level</html:option>
									<html:option value="State Level">State Level</html:option>
									<html:option value="Participated">Inter college Level</html:option>
									<html:option value="District Level">District Level</html:option>
									<html:option value="None">None</html:option>
			 	 				</nested:select>
			 	  				<a href="#" title="Select if you are participte in any sports" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			 				</div> 
			 			</td>
			 		</tr>
					<tr>
						<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">		
							<td class="row-odd" width="25%" align="right" ><div align="right">Whether Dependent of Service/Ex-Service Man:</div></td>
            			 	<td class="row-even" width="25%">
            			 		<div align="left">
            			 			<nested:radio disabled="true" property="applicantDetails.personalData.exservice" name="onlineApplicationForm" value="true" ><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
									<nested:radio disabled="true" property="applicantDetails.personalData.exservice" name="onlineApplicationForm" value="false" ><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
									<a href="#" title="Select whether your family are dependent of service/ex-service man" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>					
							</td>
						</logic:equal>
						<td class="row-odd" align="right" ><div align="right">If Yes Achievement In Sports:</div></td>
	                    <td class="row-even" align="left">
	                    	<div align="left">
	                      		<input type="hidden" id="sportsParticipate" name="sportsParticipate" value='<bean:write name="onlineApplicationForm" property="sportsParticipate"/>'/>
                         		<nested:select disabled="true" property="applicantDetails.personalData.sportsParticipate" name="onlineApplicationForm" styleClass="dropdown" styleId="sportsParticipate">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="1st Prize">1st Prize</html:option>
									<html:option value="2nd Prize">2nd Prize</html:option>
									<html:option value="3rd Prize">3rd Prize</html:option>
									<html:option value="Participated">Participated</html:option>
									<html:option value="None">None</html:option>
								</nested:select>
								<a href="#" title="Select you achieve any thing in sports" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						 	</div>
						</td>
					</tr>
                 
	                 <logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
						<tr>									
							<%String dynaStyle5="display:none;"; %>
							<logic:equal value="true" property="ncccertificate" name="onlineApplicationForm">
								<%dynaStyle5="display:block;"; %>
							</logic:equal>
						    <td class="row-odd" align="right" >Holder of NCC certificate in just Previous course:</td>
							<td class="row-even">
								<div>
	                           		<input type="hidden" id="hiddenncccertificate" name="hiddenncccertificate" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.ncccertificate"/>'/>
	                           		<nested:radio disabled="true" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="true" onclick="showNcccertificate()"><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
									<nested:radio disabled="true" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="false" onclick="hideNcccertificate()"><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
									<a href="#" title="Select yes if you are a holder of ncc certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
								<div id="ncccertificate_description" style="<%=dynaStyle5 %>">
									Grade of Certificate:
		                       		<input type="hidden" id="nccgrade" name="nccgrade" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.nccgrades"/>'/>
	                          		<nested:select disabled="true" property="applicantDetails.personalData.nccgrades" styleClass="dropdownmedium" name="onlineApplicationForm" styleId="nccgrade">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:option value="Weightage Certificate">Weightage Certificate</html:option>
										<html:option value="B">B</html:option>
										<html:option value="C">C</html:option>
									</nested:select>
								</div>
							</td>
							<td class="row-odd" align="right" >Holder of NSS certificate in just Previous course:</td>
	                        <td class="row-even">
	                        	<div align="left">
	                        		<nested:radio disabled="true" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="true" ><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
									<nested:radio disabled="true" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="false" ><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
									<a href="#" title="Select yes if you are a holder of nss certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
							</td>
						</tr>
					</logic:equal>
							
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="2">
						<tr>									
							<%String dynaStyle6="display:none;"; %>
							<logic:equal value="true" property="ncccertificate" name="onlineApplicationForm">
								<%dynaStyle6="display:block;"; %>
							</logic:equal>
							<td class="row-odd" align="right" >Holder of NCC certificate in just Previous course:</td>
							<td class="row-even">
								<div align="left">
	                           		<input type="hidden" id="hiddenncccertificate" name="hiddenncccertificate" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.ncccertificate"/>'/>
	                           		<nested:radio disabled="true" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="true" onclick="showNcccertificate()"><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
							    	<nested:radio disabled="true" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="false" onclick="hideNcccertificate()"><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
									<a href="#" title="Select yes if you are a holder of ncc certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
								<div id="ncccertificate_description" style="<%=dynaStyle6 %>">
									Grade of Certificate:
		                      		<input type="hidden" id="nccgrade" name="nccgrade" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.nccgrades"/>'/>
	                          		<nested:select disabled="true" property="applicantDetails.personalData.nccgrades" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="nccgrade">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<html:option value="Weightage Certificate">Weightage Certificate</html:option>
										<html:option value="B">B</html:option>
										<html:option value="C">C</html:option>
							 		</nested:select>
							 	</div>
							</td>
							<td class="row-odd" align="right" >Holder of NSS certificate in just Previous course:</td>
							<td class="row-even">
								<div align="left">
									<nested:radio disabled="true" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="true" ><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
									<nested:radio disabled="true" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="false" ><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
									<a href="#" title="Select yes if you are a holder of nss certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>				
							</td>
						</tr>
					</logic:equal>
					
					<tr>
						<td class="row-odd" align="right" ><div align="right">Sports Item:</div></td>
		            	<td class="row-even" align="left">
		            		<div align="left">
								<nested:select disabled="true" property="applicantDetails.personalData.sportsId" name="onlineApplicationForm" styleClass="dropdown" styleId="sportsItem" onchange="funcSportsShowHide(this.value);">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<nested:optionsCollection name="onlineApplicationForm" property="sportsList" label="name" value="id"/>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<div id="displayOtherSportsItem" style="display: none;">
									&nbsp;<nested:text readonly="true" property="applicantDetails.personalData.otherSportsItem" name="onlineApplicationForm" size="20" maxlength="50" styleId="otherSportsItem"/>
								</div>
		 			 			<a href="#" title="Select your sport" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				 			</div> 
				 		</td>
				 		<td class="row-odd" align="right"></td>
				 		<td class="row-even" align="left"></td>
					</tr>			
	                        	
			   		<!--  this one we are not using -->
			    	<tr style="display: none;">
						<%String dynaStyle3="display:none;"; %>
						<logic:equal value="true" property="sportsPerson" name="onlineApplicationForm">
							<%dynaStyle3="display:block;"; %>
						</logic:equal>
						<td class="row-odd" align="right"><bean:message key="knowledgepro.applicationform.sports.label"/> </td>
	                   	<td class="row-even">
	                   		<div align="left">
			                    <input type="hidden" id="hiddenSportsPerson" name="hiddenSportsPerson" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.sportsPerson"/>'/>
			                    <nested:radio disabled="true" property="applicantDetails.personalData.sportsPerson" styleId="sportsPersonYes" name="onlineApplicationForm" value="true" onclick="showSportsDescription()"><bean:message key="knowledgepro.applicationform.yes.label"/></nested:radio>
								<nested:radio disabled="true" property="applicantDetails.personalData.sportsPerson" styleId="sportsPersonNo" name="onlineApplicationForm" value="false" onclick="hideSportsDescription()"><bean:message key="knowledgepro.applicationform.No.label"/></nested:radio>
								<a href="#" title="Select yes if you are a sports person" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
							<div align="left" id="sports_description" style="<%=dynaStyle3 %>">
								<nested:text readonly="true" styleId="sportsDescription" property="applicantDetails.personalData.sportsDescription" name="onlineApplicationForm"  maxlength="80" styleClass="textbox" ></nested:text>
							</div>	
						</td>		
	           		</tr>
	       	 	</table>
	        </td>
		</tr>
	
	    <tr><td height="30"></td></tr>
	    
	  	<tr>
	    	<td width="100%">
	    		<table align="center" width="100%" border="0" style="border-collapse:collapse">
	      			<tr><td height="23" align="center" class="subheading">Academic Profile</td></tr>
	    		</table>
	    	</td>
	  	</tr>
	  
	 	<tr><td height="30"></td></tr>
    
   		<% String qualificationListSize= session.getAttribute("eduQualificationListSize").toString();  %> 
    	<%dynaStyle=""; %>
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
					String courseSettingsJsMethod="getMarkEntryAvailable(this,'"+count+"');";
					String collegeJsMethod="getColleges('Map"+count+"',this,"+count+");";
					String instituteJsMethod="funcOtherInstituteShowHide('"+instituteId+"','"+dynarow2+"','"+count+"');";
					String showhide=collegeJsMethod+"funcOtherUniversityShowHide('"+dynarow1+"','"+dynaid+"','"+dynarow2+"','"+instituteId +"');";
					if(count%2!=0){
						dynamicStyle="row-white";
						oppStyle="row-even";
					}else{
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
				%>
			
				<!-- doc name -->
		 		<tr>
         			<td>
         				<table align="center" width="100%" border="0" style="border-collapse:collapse">
	         				<tr>
	         					<c:choose>
									<c:when test="${qualDoc.docTypeId==6}">
										<td height="23" align="center" class="subheading">Degree</td>
									</c:when>
									<c:otherwise>
										<td height="23" align="center" class="subheading"><nested:write property="docName" name="qualDoc"/></td>
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
	   					<table align="center" cellpadding="4"  >
	         
		         			<!-- raghu exam name -->	
	         				<logic:equal value="true" name="qualDoc" property="examConfigured">
			 					<html:hidden property="isExamConfigured" styleId="<%=isExamConfigured %>" name="onlineApplicationForm" value="true"/>		
	           					<tr>
	           						<td class="row-odd" align="right" width="40%">Type of Examination<span class="Mandatory">*</span></td>
									<td class="row-even" width="50%">
										<c:set var="dexmid"><%=dynaExamId %></c:set>
										<nested:select disabled="true" property="selectedExamId" styleClass="dropdown" styleId='<%=dynaExamId %>'>
											<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
											<logic:notEmpty name="qualDoc" property="examTos">
											<html:optionsCollection name="qualDoc" property="examTos" label="name" value="id"/>
											</logic:notEmpty>	
										</nested:select>
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
								<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.universityBoard" /><span class="Mandatory">*</span></td>
	            				<td class="row-even" width="50%"> 
	            					<div align="left">
										<table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
	                						<tr>
												<td align="left">
													<div align="left">
														<c:set var="dunid"><%=dynaid %></c:set>
	               										<nested:select disabled="true" property="universityId" styleId="<%=dynaid %>" styleClass="dropdown" onchange="<%=showhide %>">
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
													</div>
	            								</td>
											</tr>
											<tr>
	                  							<td align="left">
													<logic:equal value="Other" name="qualDoc" property="universityId">
								                		<%dynaStyle="display: block;" ;%>
													</logic:equal>
													<logic:notEqual value="Other" name="qualDoc" property="universityId">
								                  		<%dynaStyle="display: none;" ;%>
													</logic:notEqual>
													<div align="left" >
	  													<nested:text readonly="true" styleClass="textbox" property="universityOthers" size="10" maxlength="50" style='<%=dynaStyle %>' styleId='<%=dynarow1 %>'></nested:text>
	                	 							</div>
	                  							</td>
	                						</tr>
										</table>
									</div>
								</td>
							</tr>
				
	        				<!-- raghu institution name -->	
			
	   						<tr>
	   							<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.instituteName" /><span class="Mandatory">*</span></td>
	            				<td class="row-even">
	            					<div align="left">
	            						<table align="left" width="100%" border="0" cellpadding="0" cellspacing="0">
	                					<%-- 	<tr>
												<td align="left" >
													<div align="left">
														<c:set var="dinid"><%=instituteId %></c:set>
	                									<c:set var="temp"><nested:write property="universityId"/></c:set>
		                								<nested:select disabled="true" property="institutionId" styleClass="dropdown" styleId='<%=instituteId %>' onchange='<%=instituteJsMethod %>' >
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
													</div>
		             							</td>
											</tr> --%>
					 						<tr>
	                  							<td align="left">
													<logic:equal value="Other" name="qualDoc" property="institutionId">
								                  		<%dynaStyle="display: block;" ;%>
													</logic:equal>
													<logic:notEqual value="Other" name="qualDoc" property="institutionId">
								                  		<%dynaStyle="display: none;" ;%>
													</logic:notEqual>
													<div align="left">
														<nested:text readonly="true" styleClass="textbox" property="otherInstitute" size="10" maxlength="50" style='<%=dynaStyle %>' styleId='<%=dynarow2 %>'></nested:text>
								                  	</div>
	                  							</td>
	                						</tr>
	            						</table>
	           		 				</div>
								</td>
							</tr>
	
	         				<!-- raghu edn state name -->	
		
	    					<tr>
								<td class="row-odd" align="right" width="40%"><bean:message key="admissionForm.education.State.label"/><span class="Mandatory">*</span></td>
	        					<td class="row-even">
									<c:set var="dstateid"><%=dynaStateId %></c:set>
									<nested:select disabled="true" property="stateId" styleClass="dropdown" styleId='<%=dynaStateId %>'>
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
		   						</td>
	    					</tr>
	            
							<!-- raghu passing year  details name -->	
			
	      					<tr>
	    						<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.passingYear"/><span class="Mandatory">*</span></td>
	            				<td class="row-even">
									<c:set var="dyopid"><%=dynaYearId %></c:set>
									<nested:select disabled="true" property="yearPassing" styleId='<%=dynaYearId %>' styleClass="dropdown">
										<html:option value="">Select</html:option>
						              	<cms:renderYear normalYear="true"></cms:renderYear>
									</nested:select>
									<script type="text/javascript">
										var yopid= '<nested:write property="yearPassing"/>';
										if(yopid!=0)
											document.getElementById("<c:out value='${dyopid}'/>").value = yopid;
									</script>
	             				</td>
	       					</tr>
	       
	       					<!-- raghu passing  month details name -->	
	          
	        				<tr>
	      	    				<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.applicationform.passingmonth"/><span class="Mandatory">*</span></td>
	           					<td class="row-even">
									<c:set var="dmonid"><%=dynamonthId %></c:set>
									<nested:select disabled="true" property="monthPassing" styleId='<%=dynamonthId %>' styleClass="dropdown">
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
	           					</td>
	       					</tr>
	          
	          				<!-- raghu exam attempts details name -->	
			
	   						<tr>
	      		 				<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.attempts"/><span class="Mandatory">*</span></td>
	        					<td class="row-even">
									<c:set var="dAttemptid"><%=dynaAttemptId %></c:set>
					                <nested:select disabled="true" property="noOfAttempts" styleId='<%=dynaAttemptId %>' styleClass="dropdown">
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
					           </td>
							</tr>
			
							<!-- raghu exam pre register no details name -->	 
			
							<logic:equal value="true" name="qualDoc" property="lastExam">
								<tr>
		        					<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.applicationform.prevregno.label"/><span class="Mandatory">*</span></td>
		            				<td class="row-even"><nested:text readonly="true" styleClass="textbox" property="previousRegNo" size="10" maxlength="15"/></td>
	         					</tr>
	         				</logic:equal>
			 
	          				<!-- raghu cosolidate marks details name -->	
			
							<nested:equal value="true" property="consolidated" name="qualDoc">
	           					<tr>
	           						<td class="row-odd" align="right" width="40%"><bean:message key="knowledgepro.admission.percentage"/><span class="Mandatory">*</span></td>
	          	 					<td class="row-even">
						              	<nested:text readonly="true" property="percentage" styleId='<%=maxMarks %>' styleClass="textbox" size="5" maxlength="8" ></nested:text>
						              	<a href="#" title="Enter your percentage" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
	           						</td>
	           					</tr>
	         				</nested:equal>
	         
	         				<tr><td colspan="2" height="20"></td></tr>
	         
	         				<nested:equal value="false"  property="consolidated" name="qualDoc">
	         
	         					<!-- raghu semster marks details name -->	
								<nested:equal value="true"  property="semesterWise" name="qualDoc">
									<tr>
	          		  	 				<td align="center" colspan="2"><span class="Mandatory">*</span><a href="#" style=" color:#FF0000;font-size:large;text-decoration:none; font-family:Arial, Helvetica, sans-serif" onclick="detailSemesterSubmit('<%=countIndex %>')"><bean:message key="knowledgepro.applicationform.semsestermark.link"/></a></td>
									</tr>
								</nested:equal>
					
								<!-- raghu doc marks details name -->	
								<nested:equal value="false"  property="semesterWise" name="qualDoc">
									<!-- raghu -->
									<c:choose>
										<c:when  test="${qualDoc.docTypeId==6}">
											<tr>
												<td align="center" colspan="2"><div align="center"><span class="Mandatory">*</span><a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" onclick="detailSubmitDegreeView('<%=countIndex %>')" >Click <bean:message key="knowledgepro.applicationform.detailmark.link"/> for DEGREE</a></div></td>
											</tr>
										</c:when>
										<c:when  test="${qualDoc.docTypeId==9}">
											<tr>
												<td align="center" colspan="2"><div align="center"><span class="Mandatory">*</span><a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" onclick="detailSubmitClass12View('<%=countIndex %>')" >Click <bean:message key="knowledgepro.applicationform.detailmark.link"/> for CLASS 12</a></div></td>
											</tr>
										</c:when>
										<c:otherwise>
							          		<tr>
							          		   <td  align="center" colspan="2"><div align="center"><span class="Mandatory">*</span><a href="#" style=" color:#FF0000;font-size:medium;text-decoration:none; font-family:Arial, Helvetica, sans-serif" onclick="detailSubmitView('<%=countIndex %>')" >Click <bean:message key="knowledgepro.applicationform.detailmark.link"/></a></div></td>
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
    			<table align="center" cellpadding="4" width="72%">
  	 				<tr style="display: none;">
						<td class="row-odd" align="right" width="50%">Backlogs in previous semesters/years to be cleared:</td>
						<td class="row-even" width="50%" >
							<html:radio disabled="true" property="backLogs" name="onlineApplicationForm" styleId="backLogs" value="true"><bean:message key="knowledgepro.applicationform.yes.label"/></html:radio>
							<html:radio disabled="true" property="backLogs" name="onlineApplicationForm" styleId="backLogs" value="false"><bean:message key="knowledgepro.applicationform.No.label"/></html:radio>
						</td>
	  				</tr>
					<tr style="display: none;">
						<td class="row-odd" align="right" width="50%">Whether SAY(Save A Year) Pass Out in same academic year or not:</td>
						<td class="row-even" width="50%">
							<html:radio disabled="true" property="isSaypass" name="onlineApplicationForm" styleId="isSaypass" value="true"><bean:message key="knowledgepro.applicationform.yes.label"/></html:radio>
							<html:radio disabled="true" property="isSaypass" name="onlineApplicationForm" styleId="isSaypass" value="false"><bean:message key="knowledgepro.applicationform.No.label"/></html:radio>
						</td>
					</tr>
					
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="1"></logic:equal>
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="2">
						<tr>
	         				<td class="row-odd" align="right" width="50%">
								<span class="Mandatory">*</span>Qualifying Under Graduate Program:
							</td>
	        				<td class="row-even" width="50%">
								<nested:select disabled="true" property="applicantDetails.personalData.ugcourse" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="ugcourse">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<html:optionsCollection property="ugcourseList" name="onlineApplicationForm" label="name" value="id"/>
								</nested:select>
							</td>
	    				</tr>
	    			</logic:equal>
	    
	    			<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
						<tr>
							<td class="row-odd" align="right" width="50%">Stream Under Class 12:<span class="Mandatory">*</span></td>
					        <td class="row-even" width="50%">
								<nested:select disabled="true" property="applicantDetails.personalData.stream" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="stream">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<html:optionsCollection property="streamMap" name="onlineApplicationForm" label="value" value="key"/>
								</nested:select>
							 	<a href="#" title="Select stream in Class 12" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
					    </tr>
					</logic:equal>
					
					<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
						<tr>
	            			<td class="row-odd" align="right" width="50%"><bean:message key="knowledgepro.applicationform.secLang.label"/><span class="Mandatory">*</span> </td>
	            			<td class="row-even" width="50%">
	            				<div align="left">
						            <html:select disabled="true" property="applicantDetails.personalData.secondLanguage" name="onlineApplicationForm" styleClass="dropdownmedium" styleId="secondLanguage">
										<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
										<html:optionsCollection property="secondLanguageList" label="value" value="value" name="onlineApplicationForm"/>
									</html:select>
				 					<a href="#" title="Select your language" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
	            				</div>
							</td>
	          			</tr>
					</logic:equal>
				</table>
	    	</td>
	  	</tr>
   
   		<!-- raghu student  entrance details code start here -->	
								
		<logic:equal value="true" property="displayEntranceDetails" name="onlineApplicationForm">								
			<tr>
    			<td width="100%">
    				<table align="center" width="40%" border="0" style="border-collapse:collapse">
      					<tr>
        					<td height="23" align="center" class="subheading"><bean:message key="admissionForm.education.entrancedetails.label"/></td>
      					</tr>
    				</table>
    			</td>
  			</tr>					
			<tr>
        		<td>
        			<table align="center" cellpadding="4"  >
     					<tr>
			            	<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="admissionForm.education.entrance.label"/></div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:select disabled="true" property="applicantDetails.entranceDetail.entranceId" styleClass="dropdown" name="onlineApplicationForm">
										<html:option value="">-Select-</html:option>
										<logic:notEmpty property="entranceList" name="onlineApplicationForm">
											<html:optionsCollection property="entranceList" name="onlineApplicationForm" label="name" value="id"/>
										</logic:notEmpty>
			   						</nested:select>
			  	 				</div>
			  	 			</td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.admission.totalMarks"/>:</div></td>
							<td class="row-even" width="50%" height="25" ><div align="left"><nested:text readonly="true" property="applicantDetails.entranceDetail.totalMarks" styleClass="textbox"  name="onlineApplicationForm"  maxlength="8"></nested:text></div></td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="admissionForm.education.markObtained.label"/>:</div></td>
							<td class="row-even" width="50%" height="25" ><div align="left"><nested:text readonly="true" property="applicantDetails.entranceDetail.marksObtained" styleClass="textbox"  name="onlineApplicationForm"  maxlength="8"></nested:text></div></td>
		           		</tr>
						<tr>
			            	<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.applicationform.prereq.roll.label"/></div></td>
							<td class="row-even" width="50%" height="25" ><div align="left"><nested:text readonly="true" property="applicantDetails.entranceDetail.entranceRollNo" styleClass="textbox" name="onlineApplicationForm"  maxlength="25"></nested:text></div></td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.applicationform.passingmonth"/>:</div></td>
							<td class="row-even" width="50%" height="25" >
								<div align="left">
									<nested:select disabled="true" property="applicantDetails.entranceDetail.monthPassing"  styleClass="dropdown"  name="onlineApplicationForm">
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
								</div>
							</td>
						</tr>
						<tr>
							<td class="row-odd" align="right" width="40%" height="25" ><div align="right"><bean:message key="knowledgepro.admission.passingYear"/>:</div></td>
							<td class="row-even" width="50%" height="25">
								<div align="left">
									<nested:select disabled="true" property="applicantDetails.entranceDetail.yearPassing" styleId='entranceyear' styleClass="dropdown"  name="onlineApplicationForm">
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									    <cms:renderYear normalYear="true"></cms:renderYear>
									</nested:select>
									<script type="text/javascript">
										var entyopid= '<nested:write property="applicantDetails.entranceDetail.yearPassing"  name="onlineApplicationForm"/>';
										document.getElementById("entranceyear").value = entyopid;
									</script>
								</div>
							</td>
		            	</tr>
					</table>
				</td>
			</tr>							
		</logic:equal>						
				
		<!-- raghu student  entrance details code over here -->	
   
   		<tr><td height="30"></td></tr>
   
   		<tr><td align="center" class="subheading">Photo View</td></tr>
   
      	<tr>
        	<td>
        		<table border="0" cellpadding="4"  align="center" class="subtable w"  >
        			<nested:iterate name="onlineApplicationForm" property="applicantDetails.editDocuments" indexId="count" id="docList" type="com.kp.cms.to.admin.ApplnDocTO" >
						<nested:equal value="true" property="photo" name="docList">
							<tr height="80">
								<td class="row-odd" height="25" width="25%"  align="center"><nested:write name="docList" property="printName" /></td>
								<td class="row-even">
							   		<nested:equal value="true" property="documentPresent" name="docList">
										<a href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.view.image" /></a>
									</nested:equal>
								</td>
								<td>
								 	<img src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/PhotoServlet") %>'  height="150px" width="150px" />
								</td>
	 						</tr>
		 				</nested:equal>	
		 						
		 				<!-- signature -->
		 				<nested:equal value="true" property="signature" name="docList">
          					<tr height="80">
								<td class="row-odd" height="25" width="25%"  align="center"><nested:write name="docList" property="printName" /></td>
								<td class="row-even">
							   		<nested:equal value="true" property="documentPresent" name="docList">
										<a href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.view.image" /></a>
									</nested:equal>
								</td>
								<td>
								 	<img src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/PhotoServlet1") %>'  height="150px" width="150px" />
								</td>
	 						</tr>
 						</nested:equal>	
 						<!-- Consolidate Marks Card -->
 						<nested:equal value="true" property="consolidateMarksCard" name="docList">
          					<tr height="80">
								<td class="row-odd" height="25" width="25%"  align="center"><nested:write name="docList" property="printName" /></td>
								<td class="row-even">
							   		<nested:equal value="true" property="documentPresent" name="docList">
										<a href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.admission.download.consolidatemarkscard" /></a>
									</nested:equal>
								</td>
								<td class="row-odd">
								</td>
	 						</tr>
 						</nested:equal>	
					</nested:iterate>
        		</table>
        	</td>
      	</tr>
  
  		<tr><td height="20"></td></tr>
  
   		<tr>
  			<td width="100%" align="center">
  	 			<html:button property="" onclick="submitAdmissionForm('backToConfirmPage')" styleClass="cntbtn" value="Verified for Submission"></html:button>
  			</td>
  		</tr>
  
  		<tr><td height="30"></td></tr>
	</table>
	
	<script language="JavaScript" src="js/admission/OnlineDetailsPersonalInfo.js"></script>
	<script language="JavaScript" src="js/admission/OnlineDetailsEducationInfo.js"></script>	
	<script type="text/javascript">
		onLoadAddrCheck();
		
		var relgId = document.getElementById("religionType").value;
		if(relgId != null && relgId.length != 0) {
			document.getElementById("religions").value = relgId;
		}

		//this for parish and diosis
		if(relgId != null && relgId=='3') {
			document.getElementById("dioces_description").style.display = "block";
			document.getElementById("parish_description").style.display = "block";
			
		}
	
		var sameAddr= document.getElementById("sameAddr").checked;
		if(sameAddr==false){
			var permanentCountryNamehidden = document.getElementById("permanentCountryNamehidden").value;
			if(permanentCountryNamehidden != null && permanentCountryNamehidden.length != 0) {
				document.getElementById("permanentCountryName").value = permanentCountryNamehidden;
			}	
		}
		var currentCountryNamehidden = document.getElementById("currentCountryNamehidden").value;
		if(currentCountryNamehidden != null && currentCountryNamehidden.length != 0) {
			document.getElementById("currentCountryName").value = currentCountryNamehidden;
		}
	
		var nationality = document.getElementById("nationalityhidden").value;
		if(nationality != null && nationality.length != 0) {
			document.getElementById("nationality").value = nationality;
		}
	
		var showHandi = document.getElementById("hiddenHandicaped").value;
		if(showHandi != null && showHandi.length != 0 && showHandi=='true') {
			showHandicappedDescription();	
		}else{
			hideHandicappedDescription();
		}
		var showSport = document.getElementById("hiddenSportsPerson").value;
		if(showSport != null && showSport.length != 0 && showSport=='true') {
			showSportsDescription();	
		}else{
			hideSportsDescription();
		}
		if(document.getElementById("casteType")!=null){
			var casteId = document.getElementById("casteType").value;
			if(casteId != null && casteId.length != 0) {
				document.getElementById("castCatg").value = casteId;
			}
		}
		if(document.getElementById("permanentCountryName").value==""){
			setTimeout("getPermAddrStates('<%=CMSConstants.KP_DEFAULT_COUNTRY_ID%>','permAddrstate')",4000);
		}
		if(document.getElementById("currentCountryName").value==""){
			setTimeout("getTempAddrStates('<%=CMSConstants.KP_DEFAULT_COUNTRY_ID%>','tempAddrstate')",6000);
		}
		if(document.getElementById("parentCountryName").value==""){
			setTimeout("getParentAddrStates('<%=CMSConstants.KP_DEFAULT_COUNTRY_ID%>','parentState')",8000);
		}
	
		var tempCategoryId = document.getElementById("tempResidentCategory").value;
		if(tempCategoryId!=null && tempCategoryId.length!=0){
			document.getElementById("residentCategory").value=tempCategoryId;
		}
	
		$(document).ready(function() {	
	
			var otherTempAddrState =  document.getElementById("otherTempAddrState").value;
			
			if(otherTempAddrState!=''){
				document.getElementById("tempAddrstate").value='Other';
				$("#otherTempAddrState").show();
				
			}

			var otherPermAddrState =  document.getElementById("otherPermAddrState").value;
			
			if(otherPermAddrState!=''){
				document.getElementById("permAddrstate").value='Other';
				$("#otherPermAddrState").show();
				
			}
	
			var hiddenParentCountryId = document.getElementById("hiddenParentCountryId").value;
			if(hiddenParentCountryId != null && hiddenParentCountryId.length != 0) {
				document.getElementById("parentCountryName").value = hiddenParentCountryId;
			}
	
			var otherParentAddrState =  document.getElementById("otherParentAddrState").value;
			if(otherParentAddrState!=''){
				document.getElementById("parentState").value='Other';
				$("#otherParentAddrState").show();
				
			}

			var showNcc = document.getElementById("hiddenncccertificate").value;
			if(showNcc != null && showNcc.length != 0 && showNcc=='true') {
				showNcccertificate();	
			}else{
				hideNcccertificate();
			}
	
			var arts =  document.getElementById("arts1").value;
			if(arts!=""){
				document.getElementById("artsParticipate1").disabled=false;
			}else{
				document.getElementById("artsParticipate1").disabled=true;
			}
		
			var sports1 =  document.getElementById("sports1").value;
			if(sports1!=""){
				document.getElementById("sportsItem").disabled=false;
			}else{
				document.getElementById("sportsItem").disabled=true;
				document.getElementById("sportsParticipate1").disabled=true;				
			}
	
	   		var sports =  document.getElementById("sportsItem").value;
			if(sports!=""){
				document.getElementById("sportsParticipate1").disabled=false;
			}else{
				document.getElementById("sportsParticipate1").disabled=true;
			}
		});

		if(document.getElementById("motherOccupation").value=='' || document.getElementById("motherOccupation").value== null){
			document.getElementById("displayMotherOccupation").style.display = "none";
		}else{
			if(document.getElementById("motherOccupation").value=='Other'){
				document.getElementById("displayMotherOccupation").style.display = "block";
			}else{
				document.getElementById("displayMotherOccupation").style.display = "none";
			}
		}
	
		if(document.getElementById("hiddenFatherOccupationId").value==null  || document.getElementById("hiddenFatherOccupationId").value==''){
			document.getElementById("displayFatherOccupation").style.display = "none";
		}else{
			if(document.getElementById("hiddenFatherOccupationId").value=='Other'){
				document.getElementById("displayFatherOccupation").style.display = "block";
			}else{
				document.getElementById("displayFatherOccupation").style.display = "none";
			}
		}
	
		function displayOtherForMother(occpation){
			if(document.getElementById("motherOccupation").value=="Other"){
				document.getElementById("displayMotherOccupation").style.display = "block";
			}else{
				document.getElementById("displayMotherOccupation").style.display = "none";
				document.getElementById("otherOccupationMother").value = "";
			}
		}
	
		
		function displayOtherForFather(occpation){
			if(document.getElementById("fatherOccupation").value=="Other"){
				document.getElementById("displayFatherOccupation").style.display = "block";
			}else{
				document.getElementById("displayFatherOccupation").style.display = "none";
				document.getElementById("otherOccupationFather").value = "";
			}
		}
</script>