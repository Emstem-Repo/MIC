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
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
	<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
	<script language="JavaScript" src="js/admission/admissionform.js"></script>
	<link rel="stylesheet" href="css/admission/css/stylesheet-pure-css.css"/>  
 
	<script type="text/javascript">
		setStartDate();
		setInterval ( "checkSession()", 300000 );
	
		function enableParish() {
			document.getElementById("parish_description").style.display = "block";
		}
		function disableParish() {
			document.getElementById("parish_description").style.display = "none";
		}
		
		function setSpc(val) {
			if (val) {
				document.getElementById("spc").value=true;
			}else{
				document.getElementById("spc").value=false;
			}
		}
		function setScot(val) {
			if (val) {
				document.getElementById("scot").value=true;
			}else{
				document.getElementById("scot").value=false;
			}
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

	<html:hidden property="focusValue" styleId="focusValue" name="onlineApplicationForm"/>
	<html:hidden property="onlineApply" styleId="onlineApply" name="onlineApplicationForm"/>
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
 		
	<table width="80%" style="background-color: #F0F8FF" align="center" border="0" cellpadding="0" cellspacing="0">
	
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
							     	<li class="acBlue">Personal Details</li>
							     	<li class="acGrey">Education Details</li>
								 	<li class="acGrey">Upload Photo</li>
  	 							</ul>
   							</div>
  	 					</td>
   					</tr>
    			</table>
    		</td>
  		</tr>
  	
  		<tr><td height="20"></td></tr>
   
   		<!-- errors display -->
  		<tr>
			<td align="center">
				<div id="errorMessage" align="center">
					<FONT color="red"><html:errors /></FONT>
					<FONT color="green">
						<html:messages id="msg"	property="messages" message="true">
							<c:out value="${msg}" escapeXml="false"></c:out>
						</html:messages>
					</FONT>
				</div>
				<div id="errorMessage1" style="font-size: 11px; color: red"></div>
			</td>
		</tr>
		
		<tr><td height="5px"></td></tr>
	
		<tr>
    		<td width="100%">
    			<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;">
      				<tr>
        				<td height="30" align="center" class="subheading"><span style="color: white;">Personal Details</span></td>
      				</tr>
    			</table>
    		</td>
  		</tr>
	
 		<tr><td height="30"></td></tr>
	
      	<tr>
        	<td>
        		<table width="100%" border="0" cellpadding="4"  align="center" >
          			<tr>
			            <td class="row-odd" width="25%" align="right"><bean:message key="knowledgepro.applicationform.candidateName"/><span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%">
            				<nested:text readonly="true" property="applicantDetails.personalData.firstName" styleId="firstNameId" name="onlineApplicationForm"  maxlength="90" size="25%" style="border-radius: 6px;"></nested:text>						
            				<a href="#" title="Enter your name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
             			</td>
            			<td class="row-odd" width="25%" align="right"><bean:message key="admissionForm.studentinfo.bloodgroup.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%">
							<input type="hidden" id="BGType" name="BGType" value='<bean:write name="onlineApplicationForm" property="bloodGroup"/>'/>
                         	<nested:select property="applicantDetails.personalData.bloodGroup" name="onlineApplicationForm" styleClass="dropdown" styleId="bgType">
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
             				<nested:text readonly="true" name="onlineApplicationForm" property="applicantDetails.personalData.dob" styleId="dateOfBirth"  maxlength="11" size="25%" style="border-radius: 6px;"></nested:text>
           					<a href="#" title="Select your date of birth" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
             			<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.residentcatg.label2"/><span class="Mandatory">*</span></td>
			            <td class="row-even">
				            <div align="left">
					            <input type="hidden" id="tempResidentCategory" value="<nested:write property="applicantDetails.personalData.residentCategory" name="onlineApplicationForm" />">
								<nested:select property="applicantDetails.personalData.residentCategory" name="onlineApplicationForm" styleClass="dropdown" styleId="residentCategory" disabled="true">
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
			            	<fieldset style="border: 0px">
			             		<nested:radio disabled="true" property="applicantDetails.personalData.gender" styleId="MALE" name="onlineApplicationForm" value="MALE"></nested:radio>
			             		<label for="MALE"><span><span></span></span>M</label> 
						 		<nested:radio disabled="true" property="applicantDetails.personalData.gender" name="onlineApplicationForm" styleId="FEMALE" value="FEMALE"></nested:radio>
						 		<label for="FEMALE"><span><span></span></span>F</label>
						 		<nested:radio disabled="true" property="applicantDetails.personalData.gender" name="onlineApplicationForm" styleId="TRANSGENDER" value="TRANSGENDER"></nested:radio>
						 		<label for="TRANSGENDER"><span><span></span></span><bean:message key="admissionForm.studentinfo.sex.transgender.text"/></label>
						 		<a href="#" title="Select your gender" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a> 
			   				</fieldset>
						</td>
             			<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.nationality.label"/><span class="Mandatory">*</span></td>
			            <td class="row-even">
				            <div align="left">
					            <input type="hidden" id="nationalityhidden" name="nationality" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.nationality"/>"/>
					            <nested:select property="applicantDetails.personalData.nationality" styleClass="dropdown" styleId="nationality" name="onlineApplicationForm">
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
          				<td class="row-odd" align="right">Land <bean:message key="admissionForm.studentinfo.phone.label"/></td>
            			<td class="row-even">
            				<nested:text styleClass="textboxsmall" property="applicantDetails.personalData.phNo2" name="onlineApplicationForm" styleId="applicantphNo2" maxlength="7" size="7" onkeypress="return isNumberKey(event)" />
           					<nested:text styleClass="textboxmedium" property="applicantDetails.personalData.phNo3" name="onlineApplicationForm" styleId="applicantphNo3" maxlength="10" size="10" onkeypress="return isNumberKey(event)"/>
			 				<a href="#" title="Enter your phone code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            			</td>
          				<td class="row-odd" align="right"> 
            				<bean:message key="admissionForm.studentinfo.mobile.label"/><span class="Mandatory">*</span>
             			</td>
			            <td class="row-even">
			              <nested:text readonly="true" styleClass="textboxsmall"  property="applicantDetails.personalData.mobileNo1" name="onlineApplicationForm" styleId="applicantMobileCode" maxlength="4" size="4" onkeypress="return isNumberKey(event)" ></nested:text>
			              <nested:text readonly="true" styleClass="textboxmedium" property="applicantDetails.personalData.mobileNo2" name="onlineApplicationForm" styleId="applicantMobileNo" maxlength="10" size="10" onkeypress="return isNumberKey(event)"></nested:text>
			              <a href="#" title="Enter your code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			           	</td>
	         		</tr>
          			
          			<tr>
						<%String dynaStyle=""; %>
						<logic:equal value="Other" property="birthState" name="onlineApplicationForm">
							<%dynaStyle="display:block;"; %>
						</logic:equal>
						<logic:notEqual value="Other" property="birthState" name="onlineApplicationForm">
							<%dynaStyle="display:none;"; %>
						</logic:notEqual>
          			</tr>
          
          			<tr>
          				<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.email.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<div align="left">
            					<nested:text readonly="true" property="applicantDetails.personalData.email" styleId="applicantEmail" name="onlineApplicationForm" size="25%"  maxlength="50" style="border-radius: 6px;"></nested:text>
             					<a href="#" title="Enter your mail" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					<br/>(e.g. name@yahoo.com)
            				</div>
            			</td>
						<td class="row-odd" align="right"><bean:message key="admissionForm.studentinfo.aadhaarNumber"/>:</td>
						<td class="row-even">
							<nested:text property="applicantDetails.personalData.aadharCardNumber" styleId="applicantadhaarNo" name="onlineApplicationForm" size="25%"  maxlength="12" onkeypress="return isNumberKey(event)" style="border-radius: 6px;"></nested:text>
							<a href="#" title="Enter Your Aadhaar Card Number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right">Name with initials expanded</td>
            			<td class="row-even">
            				<div align="left">
            					<nested:text property="applicantDetails.personalData.nameWithInitial" styleId="applicantEmail" name="onlineApplicationForm" size="25%"  maxlength="50" style="border-radius: 6px;"></nested:text>
             					<a href="#" title="Enter your Name with initials expanded" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            				</div>
            			</td>
						<td class="row-odd" align="right">Mother Tongue<span class="Mandatory">*</span>:</td>
						<td class="row-even">
							<nested:text property="applicantDetails.personalData.motherTonge" styleId="applicantadhaarNo" name="onlineApplicationForm" size="25%"  maxlength="12" style="border-radius: 6px;"></nested:text>
							<a href="#" title="Enter Your Mother tongue" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right">Place of birth<span class="Mandatory">*</span>:</td>
            			<td class="row-even">
            				<div align="left">
            					<nested:text  property="applicantDetails.personalData.placeOfBirth" styleId="applicantEmail" name="onlineApplicationForm" size="25%"  maxlength="50" style="border-radius: 6px;"></nested:text>
             					<a href="#" title="Enter your place of birth" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            				</div>
            			</td>
						<td class="row-odd" align="right">thaluk<span class="Mandatory">*</span>:</td>
						<td class="row-even">
							<nested:text property="applicantDetails.personalData.thaluk" styleId="applicantadhaarNo" name="onlineApplicationForm" size="25%"  maxlength="12"  style="border-radius: 6px;"></nested:text>
							<a href="#" title="Enter Birth taluk" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
          			</tr>
          			<tr>
          				<td class="row-odd" align="right">District<span class="Mandatory">*</span>:</td>
            			<td class="row-even">
            				<div align="left">
            					<nested:text  property="applicantDetails.personalData.district" styleId="applicantEmail" name="onlineApplicationForm" size="25%"  maxlength="50" style="border-radius: 6px;"></nested:text>
             					<a href="#" title="Enter Birth District" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            				</div>
            			</td>
						<td class="row-odd" align="right"></td>
						<td class="row-even"></td>
          			</tr>
          		</table>
       		</td>
      	</tr>
       	
       	<tr><td height="30"></td></tr>

  		<tr>
    		<td width="100%">
    			<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;" >
      				<tr>
        				<td height="30" align="center" class="subheading"><span style="color: white;">Current Address</span></td>
      				</tr>
    			</table>
    		</td>
  		</tr>
 
      	<tr>
        	<td>
        		<table width="100%" align="center" cellpadding="4" >
          			<tr>
			            <td class="row-odd" align="right" width="25%">Address Line 1:<span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%">
			            	<nested:text property="applicantDetails.personalData.currentAddressLine1" styleId="currentAddressLine1" size="25%" name="onlineApplicationForm" maxlength="35" style="border-radius: 6px;"></nested:text>
							<a href="#" title="Enter your house name/house number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
			
			            <td class="row-odd" align="right" width="25%">Country:<span class="Mandatory">*</span></td>
			            <td class="row-even" width="25%">
            				<div align="left">
            					<input type="hidden" id="currentCountryNamehidden" name="nationality" name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.currentCountryId"/>"/>
								<nested:select property="applicantDetails.personalData.currentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="currentCountryName" onchange="getTempAddrStates(this.value,'tempAddrstate');" >
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
            			<td class="row-odd" align="right"> Address Line 2:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text property="applicantDetails.personalData.currentAddressLine2" styleId="currentAddressLine2" size="25%" name="onlineApplicationForm" maxlength="40" onkeypress="IsAlpha(event)" style="border-radius: 6px;"></nested:text>
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
								
								<nested:select property="applicantDetails.personalData.currentStateId" name="onlineApplicationForm"  styleClass="dropdown" styleId="tempAddrstate" onchange="funcOtherShowHide('tempAddrstate','otherTempAddrState');getTempAddrDistrict(this.value,'tempAddrdistrict');">
									<html:option value="">- Select -</html:option>
									<logic:notEmpty property="stateMap" name="onlineApplicationForm">
										<html:optionsCollection name="onlineApplicationForm" property="curAddrStateMap" label="value" value="key" />
									</logic:notEmpty>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Enter your current state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
	   						<div align="left"><nested:text property="applicantDetails.personalData.currentAddressStateOthers" styleClass="textbox" name="onlineApplicationForm" maxlength="30" styleId="otherTempAddrState" style="<%=dynaStyle %>" onkeypress="IsAlpha(event)"></nested:text></div>
     					</td>
     				</tr>
     
          			<tr>
            			<td class="row-odd" align="right"> Pin Code:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text size="25%" property="applicantDetails.personalData.currentAddressZipCode" styleId="currentAddressZipCode" name="onlineApplicationForm"  maxlength="10" onkeypress="return isNumberKey(event)" style="border-radius: 6px;"/>
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
								<nested:select property="applicantDetails.personalData.currentDistricId" name="onlineApplicationForm" styleClass="dropdown" styleId="tempAddrdistrict" onchange="funcOtherShowHide('tempAddrdistrict','otherTempAddrDistrict');">
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
							<div align="left"><nested:text property="applicantDetails.personalData.currentAddressDistrictOthers" name="onlineApplicationForm" styleClass="textbox" size="10" maxlength="30" styleId="otherTempAddrDistrict" style="<%=dynaStyle %>" onkeypress="IsAlpha(event)"></nested:text></div>
			            </td>
          			</tr>
          
          			<tr>
            			<td class="row-odd" align="right">City:<span class="Mandatory">*</span></td>
            			<td class="row-even">
            				<nested:text property="applicantDetails.personalData.currentCityName" styleId="currentCityName" size="25%" name="onlineApplicationForm" maxlength="30" onkeypress="IsAlpha(event)" style="border-radius: 6px;"></nested:text>
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
            				<fieldset style="border: 0px">
             					<html:radio property="sameTempAddr" styleId="sameAddr" value="true" onclick="disableTempAddress();"></html:radio>
					     		<label for="sameAddr"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
			 					<html:radio property="sameTempAddr" styleId="DiffAddr" value="false" onclick="enableTempAddress();"></html:radio>
								<label for="DiffAddr"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
            				</fieldset>
						</td>
     				</tr>
				</table>
    		</td>
  		</tr>

		<tr>
      		<td width="100%">
      			<div id="currLabel">
      				<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;" >
      					<tr>
        					<td height="30" align="center" class="subheading"><span style="color: white;">Permanent Address</span></td>
      					</tr>
    				</table>
    			</div>
    		</td>
    	</tr>
   
      	<tr>
        	<td>
        		<div id="currTable">
        			<table align="center" cellpadding="4" width="100%">
          				<tr>
				            <td class="row-odd" align="right" width="25%">Address Line 1:<span class="Mandatory">*</span></td>
				            <td class="row-even" width="25%">
				            	<nested:text property="applicantDetails.personalData.permanentAddressLine1" styleId="permanentAddressLine1" styleClass="textboxmedium" name="onlineApplicationForm"  maxlength="35"></nested:text>
								<a href="#" title="Enter your house number and house name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
				            <td class="row-odd" align="right" width="25%">Country:<span class="Mandatory">*</span></td>
				            <td class="row-even" width="25%">
				            	<div align="left">
				            		<input type="hidden" id="permanentCountryNamehidden" name="nationality" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.permanentCountryId"/>"/>
									<nested:select property="applicantDetails.personalData.permanentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="permanentCountryName" onchange="getPermAddrStates(this.value,'permAddrstate');">
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
            				<td class="row-odd" align="right"> Address Line 2:<span class="Mandatory">*</span></td>
            				<td class="row-even">
           						<nested:text property="applicantDetails.personalData.permanentAddressLine2" styleClass="textboxmedium"  styleId="permanentAddressLine2" name="onlineApplicationForm"  maxlength="40" onkeypress="IsAlpha(event)" ></nested:text>
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
              						<nested:select property="applicantDetails.personalData.permanentStateId" name="onlineApplicationForm" styleClass="dropdown" styleId="permAddrstate" onchange="funcOtherShowHide('permAddrstate','otherPermAddrState');getPermAddrDistrict(this.value,'permAddrdistrict');">
										<html:option value="">- Select -</html:option>
										<logic:notEmpty property="stateMap" name="onlineApplicationForm" >
											<html:optionsCollection name="onlineApplicationForm" property="perAddrStateMap" label="value" value="key" />
										</logic:notEmpty>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Enter your permanent state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>
								<div align="left"><nested:text property="applicantDetails.personalData.permanentAddressStateOthers" name="onlineApplicationForm" styleClass="textbox" maxlength="30" styleId="otherPermAddrState" style="<%=dynaStyle %>" onkeypress="IsAlpha(event)"></nested:text></div>
        					</td>
        				</tr>
     
          				<tr>
            				<td class="row-odd" align="right"> Pin Code:<span class="Mandatory">*</span></td>
            				<td class="row-even">
            					<nested:text styleClass="textboxmedium" property="applicantDetails.personalData.permanentAddressZipCode" styleId="permanentAddressZipCode" name="onlineApplicationForm"  maxlength="10" onkeypress="return isNumberKey(event)"/>
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
                     				<nested:select property="applicantDetails.personalData.permanentDistricId" name="onlineApplicationForm" styleClass="dropdown" styleId="permAddrdistrict" onchange="funcOtherShowHide('permAddrdistrict','otherPermAddrDistrict');">
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
				  				<div align="left"><nested:text property="applicantDetails.personalData.permanentAddressDistrictOthers" name="onlineApplicationForm" size="10" maxlength="30" styleClass="textbox" styleId="otherPermAddrDistrict" style="<%=dynaStyle %>" onkeypress="IsAlpha(event)"></nested:text></div>
            				</td>
          				</tr>
          
          				<tr>
            				<td class="row-odd" align="right">City:<span class="Mandatory">*</span></td>
				            <td class="row-even">
					            <nested:text property="applicantDetails.personalData.permanentCityName" styleId="permanentCityName" styleClass="textboxmedium" name="onlineApplicationForm"  maxlength="30" onkeypress="IsAlpha(event)"></nested:text>
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
    			<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;">
      				<tr>
        				<td height="30" align="center" class="subheading"><span style="color: white;">Parent Information</span></td>
      				</tr>
    			</table>
    		</td>
  		</tr>

      	<tr>
        	<td>
        		<table width="100%"  align="center" cellpadding="4" >
          			<tr>
            			<td class="row-odd" align="right" width="25%">Father's Name<span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%">
            				<div align="left">
	            				<nested:select property="applicantDetails.titleOfFather" styleId='titleOfFather' name="onlineApplicationForm" styleClass="dropdownsmall" onchange="fatherIncomeMandatory()">
									<html:option value="Mr">Mr.</html:option>
                                                                         <html:option value="Late">Late.</html:option>

								</nested:select>
								<nested:text property="applicantDetails.personalData.fatherName" styleId="fatherName" name="onlineApplicationForm" size="25px" maxlength="50" onkeypress="IsAlphaDot(event)" style="border-radius: 6px;"></nested:text>
								<a href="#" title="Enter name of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>								
						</td>
			            <td class="row-odd" align="right" width="25%">
							Mother's name<span class="Mandatory">*</span>
						</td>
						<td class="row-even" width="25%">
							<div align="left">
								<nested:select property="applicantDetails.titleOfMother" styleId='titleOfMother' name="onlineApplicationForm" styleClass="dropdownsmall" onchange="motherIncomeMandatory()">
									<html:option value="Mrs">Mrs.</html:option>
                                                                         <html:option value="Late">Late.</html:option>
								</nested:select>
								<nested:text property="applicantDetails.personalData.motherName" styleId="motherName"  name="onlineApplicationForm" size="25px" maxlength="50" onkeypress="IsAlphaDot(event)" style="border-radius: 6px;"></nested:text>
								<a href="#" title="Enter name of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
          			</tr>
          
					<tr>
						<td class="row-odd" width="25%" align="right">
							<bean:message key="knowledgepro.admin.occupation" />:<span class="Mandatory">*</span>
						</td>
						<td class="row-even" width="25%" >
							<div align="left">
								<input type="hidden" id="hiddenFatherOccupationId"  name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.fatherOccupationId"/>"/>
								<nested:select property="applicantDetails.personalData.fatherOccupationId" name="onlineApplicationForm" styleClass="dropdown" styleId="fatherOccupation" onchange="displayOtherForFather(this.value)">
									<html:option value="">- Select -</html:option>
									<html:optionsCollection name="onlineApplicationForm" property="occupations" label="occupationName" value="occupationId"/>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Select occupation of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
							<div align="left" id="displayFatherOccupation" >
								<nested:text property="applicantDetails.personalData.otherOccupationFather" name="onlineApplicationForm" styleClass="textbox" maxlength="50" styleId="otherOccupationFather"/>
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
			 					<nested:select property="applicantDetails.personalData.motherOccupationId" name="onlineApplicationForm" styleClass="dropdown" styleId="motherOccupation" onchange="displayOtherForMother(this.value)">
									<html:option value="">- Select -</html:option>
									<html:optionsCollection name="onlineApplicationForm" property="occupations" label="occupationName" value="occupationId"/>
				 					<html:option value="Other">Other</html:option>
								</nested:select>
								<a href="#" title="Select occupation of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
		   					</div>
							<div align="left" id="displayMotherOccupation" >
								<nested:text property="applicantDetails.personalData.otherOccupationMother" name="onlineApplicationForm" styleClass="textbox"   maxlength="50"  styleId="otherOccupationMother"/>
							</div>
            			</td>
          			</tr>
                    	
          			<tr>
						<td class="row-odd" align="right">Family Annual Income: <span class="Mandatory">*</span></td>
		            	<td class="row-even">
		            		<div align="left">
		            	   <!--  		<nested:select style="display:none" property="applicantDetails.personalData.fatherCurrencyId" name="onlineApplicationForm"  styleId="fatherCurrency">
		           		 			<%String selected=""; %>
									<logic:iterate id="option" property="currencyList" name="onlineApplicationForm">
										<logic:equal value='<%=String.valueOf(CMSConstants.KP_DEFAULT_CURRENCY_ID) %>' name="option" property="id"><%selected="selected"; %></logic:equal>
										<logic:notEqual value='<%=String.valueOf(CMSConstants.KP_DEFAULT_CURRENCY_ID) %>' name="option" property="id"><%selected=""; %></logic:notEqual>
										<option value='<bean:write name="option" property="id"/>' <%=selected %> ><bean:write name="option" property="name"/> </option>
									</logic:iterate>
								</nested:select>	
		            			<nested:select property="applicantDetails.personalData.fatherIncomeId" name="onlineApplicationForm" styleClass="dropdown" styleId="fatherIncomeRange">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<nested:optionsCollection name="onlineApplicationForm" property="incomeList" label="incomeRange" value="id"/>
								</nested:select> -->
								<nested:text styleId="fatherIncomeRange" property="applicantDetails.personalData.familyAnnualIncome" name="onlineApplicationForm" size="15" maxlength="15" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
					 			<a href="#" title="Select the annual income of your family" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
						<td class="row-odd">
							<div align="right"><bean:message key="admissionForm.studentinfo.mobile.label" /></div>
						</td>
						<td class="row-even" align="left">
							<nested:text styleId="motherMobile1" property="applicantDetails.personalData.motherMobile" name="onlineApplicationForm" size="15" maxlength="10" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
							<a href="#" title="Enter the country code (without + or 00) and mobile number of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
             			</td>	
          			</tr>
          
         			<tr>
						<td class="row-odd">
							<div align="right"><bean:message key="admissionForm.studentinfo.mobile.label" /><span class="Mandatory">*</span></div>
						</td>
						<td class="row-even" align="left">
					<!-- 		<nested:text readonly="false" styleId="fatherMobile" property="applicantDetails.personalData.parentMob1" name="onlineApplicationForm" size="4" maxlength="4" styleClass="textboxsmall" onkeypress="return isNumberKey(event)"></nested:text> -->
							<nested:text styleId="fatherMobile1" property="applicantDetails.personalData.fatherMobile" name="onlineApplicationForm" size="15" maxlength="10" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
							<a href="#" title="Enter the country code (without + or 00) and mobile number of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			            </td>
						<td class="row-odd" width="25%" align="right"><bean:message key="admissionForm.studentinfo.email.label" /></td>
			            <td class="row-even">
				            <div align="left">
				            	<nested:text property="applicantDetails.personalData.motherEmail" styleId="motherEmail" styleClass="textboxmedium" name="onlineApplicationForm"  maxlength="50"></nested:text>
				            	<a href="#" title="Enter email id of mother" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				            </div>
							<div align="left">(e.g. name@yahoo.com)</div>
			            </td>    
           			</tr>
						
		          	<tr>
		          		<td class="row-odd" width="25%" align="right">
							<bean:message key="admissionForm.studentinfo.email.label"/>
						</td>
						<td class="row-even">
							<div align="left">
								<nested:text property="applicantDetails.personalData.fatherEmail" name="onlineApplicationForm" styleId="fatherEmail" styleClass="textboxmedium" maxlength="50"></nested:text>
								<a href="#" title="Enter email id of father" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
		    				<div align="left">(e.g. name@yahoo.com)</div>
						</td>
						<td class="row-odd" align="right" width="25%"><bean:message key="knowledgepro.applicationform.guardianname.label"/></td>
						<td class="row-even">
							<nested:text property="applicantDetails.personalData.guardianName" styleId="guardianNameFatherSide" name="onlineApplicationForm" styleClass="textboxmedium" maxlength="50" onkeypress="IsAlphaDot(event)"></nested:text>
							<a href="#" title="Enter name of guardian" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
		          	</tr>
		          	
		          	<tr>
						<td class="row-odd" width="25%" align="right">If any of the parents of the applicant was a student of this college, give details, including present address</td>
						<td class="row-even" width="25%" align="right">
							<div align="left">
							<nested:textarea name="onlineApplicationForm" style="width:240px" rows="3" cols="8" property="applicantDetails.personalData.parentOldStudent" styleClass="textboxmedium" ></nested:textarea>
							<a href="#" title="Enter relationship " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						
						</td>
						<td class="row-odd" align="right" width="25%">Whether the applicant is related to any former student(s) of this College. If so give the details including present address</td>
						<td class="row-even">
							<nested:textarea name="onlineApplicationForm" style="width:240px" rows="3" cols="8" property="applicantDetails.personalData.relativeOldStudent" styleClass="textboxmedium" ></nested:textarea>
							<a href="#" title="Enter relationship " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>
          			</tr>
          
           			<tr>
						<td class="row-odd" width="25%" align="right"></td>
						<td class="row-even" width="25%" align="right"></td>
						<td class="row-odd" align="right" width="25%">Guardian's relationship</td>
						<td class="row-even">
							<nested:text property="applicantDetails.personalData.guardianRelationShip" styleId="guardianRelationFatherSide" name="onlineApplicationForm" styleClass="textboxmedium" maxlength="50" onkeypress="IsAlphaDot(event)"></nested:text>
							<a href="#" title="Enter relationship with guardian" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
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
            				<fieldset style="border: 0px">
				            	<html:radio property="sameParentAddr" styleId="sameParAddr" value="true" onclick="disablePermAddress()"></html:radio>
								<label for="sameParentAddr"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
							 	<html:radio property="sameParentAddr" styleId="DiffParAddr" value="false" onclick="enablePermAddress()"></html:radio>
								<label for="sameParentAddr"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
							 	<a href="#" title="Select parent address yes, if same as current address  " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				            </fieldset>
						</td>
     				</tr>
    			</table>
    		</td>
  		</tr>
					
		<tr>
 			<td width="100%">
 				<div id="permLabel">
 					<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;">
   						<tr><td height="30" align="center" class="subheading"><span style="color: white;">Parent Address</span></td></tr>
 					</table>
 				</div>
 			</td>
		</tr>
  					
		<tr>
        	<td>
            	<div id="permTable">
                	<table width="100%"  align="center" cellpadding="4" >
						<tr>
							<td class="row-odd" width="25%">
								<div align="right">Address Line 1:<span class="Mandatory">*</span></div>
							</td>
							<td class="row-even" width="25%" align="left">
								<nested:text styleId="parentAddressLine1" property="applicantDetails.personalData.parentAddressLine1" styleClass="textboxmedium" name="onlineApplicationForm" size="15" maxlength="100" ></nested:text>
								<a href="#" title="Enter your parent house name or number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
							<td class="row-odd" width="25%"  align="right"><bean:message key="admissionForm.studentinfo.mobile.label"/><span class="Mandatory">*</span></td>
            				<td class="row-even" width="25%" >
           						<div align="left">
           					 		<nested:text readonly="false" styleClass="textboxsmall" property="applicantDetails.personalData.parentMob1" styleId="parentMobile" name="onlineApplicationForm"  maxlength="4" size="4" onkeypress="return isNumberKey(event)"></nested:text>
				   			 		<nested:text styleClass="textboxmedium" property="applicantDetails.personalData.parentMob2"  styleId="parentMobile1" name="onlineApplicationForm"  maxlength="10" size="10" onkeypress="return isNumberKey(event)"></nested:text>
					 				<a href="#" title="Enter your parent mobile code and number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
					 			</div>
							</td>
						</tr>
										
						<tr>
							<td class="row-odd">
								<div align="right">Address Line 2:<span class="Mandatory">*</span></div>
							</td>
							<td class="row-even">
								<nested:text styleId="parentAddressLine2" property="applicantDetails.personalData.parentAddressLine2" styleClass="textboxmedium" name="onlineApplicationForm" size="15" maxlength="100" onkeypress="IsAlpha(event)"></nested:text>
								<a href="#" title="Enter your parent post office name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
							<td class="row-odd" align="right">
								<bean:message key="knowledgepro.admin.country" /><span class="Mandatory">*</span>								
							</td>
							<td class="row-even">
								<div align="left">
									<input type="hidden" id="hiddenParentCountryId" name="nationality" name="onlineApplicationForm" value="<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.parentCountryId"/>"/>
									<nested:select property="applicantDetails.personalData.parentCountryId" name="onlineApplicationForm" styleClass="dropdown" styleId="parentCountryName" onchange="getParentAddrStates(this.value,'parentState')">
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
							<td class="row-odd" align="right">
								<bean:message key="knowledgepro.admission.zipCode"/><span class="Mandatory">*</span>
							</td>
							<td class="row-even" align="left">
								<nested:text styleId="parentAddressZipCode" property="applicantDetails.personalData.parentAddressZipCode" styleClass="textboxmedium" name="onlineApplicationForm" size="10" maxlength="10" onkeypress="return isNumberKey(event)"></nested:text>
								<a href="#" title="Select your parent ZIP code" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
							<td class="row-odd">
								<div align="right"><bean:message key="knowledgepro.admin.state" /><span class="Mandatory">*</span></div>
							</td>
							<td class="row-even">
								<div align="left">
									<%String dynastyle=""; %>
									<logic:equal value="Other" property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm">
										<%dynastyle="display:block;"; %>
									</logic:equal>
									<logic:notEqual value="Other" property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm">
										<%dynastyle="display:none;"; %>
									</logic:notEqual>
				                  	<nested:select property="applicantDetails.personalData.parentStateId" name="onlineApplicationForm" styleClass="dropdown" styleId="parentState" onchange="funcOtherShowHide('parentState','otherParentAddrState');">
										<html:option value="">- Select -</html:option>
										<logic:notEmpty property="parentStateMap" name="onlineApplicationForm">
											<html:optionsCollection name="onlineApplicationForm" property="parentStateMap" label="value" value="key" />
										</logic:notEmpty>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Enter your parent state" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</div>				
								<div align="left">
									<nested:text property="applicantDetails.personalData.parentAddressStateOthers" styleClass="textbox" name="onlineApplicationForm" maxlength="30" styleId="otherParentAddrState" style="<%=dynastyle %>" onkeypress="IsAlpha(event)"></nested:text>
								</div>
							</td>
						</tr>
							
						<tr>
            				<td class="row-odd" width="25%" >
								<div align="right"><bean:message key="knowledgepro.admin.city" />:<span class="Mandatory">*</span></div>
							</td>
							<td class="row-even" width="25%" align="left">
								<nested:text styleId="parentCityName" property="applicantDetails.personalData.parentCityName" styleClass="textboxmedium" name="onlineApplicationForm" size="15" maxlength="30" onkeypress="IsAlpha(event)"></nested:text>
								<a href="#" title="Enter your parent city" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</td>
           				 	<td class="row-odd"></td>
            				<td class="row-even"></td>
       					</tr>					
					</table>
				</div>
			</td>
      	</tr>
      
       	<tr ><td height="30"></td></tr>
      
   		<tr>
    		<td width="100%">
    			<table align="center" width="100%" border="0" style="border-collapse:collapse;background-color: #808080;">
      				<tr>
        				<td height="30" align="center" class="subheading"><span style="color: white;">Reservation Details</span></td>
      				</tr>
    			</table>
    		</td>
  		</tr>

      	<tr>
        	<td>
        		<table align="center" cellpadding="4"   >
          			<tr>
            			<td class="row-odd" valign="top" align="right" width="25%"><bean:message key="admissionForm.studentinfo.religion.label"/><span class="Mandatory">*</span></td>
            			<td class="row-even" width="25%" valign="top">
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
									<nested:select property="applicantDetails.personalData.religionId" name="onlineApplicationForm" styleClass="dropdown" styleId="religions" onchange="getSubCaste(this.value,'castCatg');funcOtherShowHide('religions','otherReligion');funcReligionShowHide(this.value);">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<nested:optionsCollection name="onlineApplicationForm" property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Select your religion" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</logic:notEqual>
								<logic:equal value="Other" property="applicantDetails.personalData.religionId" name="onlineApplicationForm">
									<%dynaStyle="display:block;"; %>												
									<nested:select property="applicantDetails.personalData.religionId" name="onlineApplicationForm" styleClass="dropdown" styleId="religions" onchange="getSubCaste(this.value,'castCatg');funcOtherShowHide('religions','otherReligion');funcReligionShowHide(this.value);">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<nested:optionsCollection name="onlineApplicationForm" property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<a href="#" title="Select your religion" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			  					</logic:equal>
			  				</div>
							<div align="left"><nested:text property="applicantDetails.personalData.religionOthers" styleClass="textbox"  name="onlineApplicationForm"   maxlength="30" styleId="otherReligion" style="<%=dynaStyle %>"></nested:text></div>
			 				<logic:equal name="onlineApplicationForm" property="applicantDetails.personalData.religionId" value="3">
								<%dynaStyle="display:block;"; %>	
							</logic:equal>
            			</td>
            
            			<%String dynaStyle4="display:none;"; %>
						<logic:equal value="true" property="handicapped" name="onlineApplicationForm">
							<%dynaStyle4="display:block;"; %>
						</logic:equal>
         				<td class="row-odd" valign="top" align="right" width="21%"><bean:message key="knowledgepro.applicationform.physical.label"/></td>
         				<td class="row-even" width="29%" valign="top">
         					<div align="left">
                				<input type="hidden" id="hiddenHandicaped" name="hiddenHandicaped" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.handicapped"/>'/>
             					<nested:radio property="applicantDetails.personalData.handicapped" styleId="handicappedYes" name="onlineApplicationForm" value="true" onclick="showHandicappedDescription()"></nested:radio>
                           		<label for="handicappedYes"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
			 					<nested:radio property="applicantDetails.personalData.handicapped" styleId="handicappedNo" name="onlineApplicationForm" value="false" onclick="hideHandicappedDescription()"></nested:radio>
				           		<label for="handicappedNo"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
			 					<a href="#" title="Select if you are physically challenged person" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
			  				<div align="left" id="handicapped_description" style="display: none;" >
		 						PWD Category:<input type="hidden" id="pwdType" name="pwdType" value='<bean:write name="onlineApplicationForm" property="pwdType"/>'/>
                        		<nested:select property="applicantDetails.personalData.hadnicappedDescription" name="onlineApplicationForm" styleClass="dropdown" styleId="pwdType">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="Visually Handicapped">Visually Handicapped</html:option>
									<html:option value="Hearing Impaired">Hearing Impaired</html:option>
									<html:option value="Orthopeadic">Orthopeadic</html:option>											
									<html:option value="Cerebral Palsy">Cerebral Palsy</html:option>
								</nested:select>	
			 					<a href="#" title="Select the type of disability" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								Percentage of disability:<nested:text styleId="hadnicappedDescription" property="applicantDetails.personalData.handicapedPercentage" name="onlineApplicationForm" size="7" maxlength="3" styleClass="textboxmedium" onkeypress="return isNumberKey(event)"></nested:text>
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
                 			<nested:select property="applicantDetails.personalData.casteId" name="onlineApplicationForm" styleId="castCatg" styleClass="dropdown" onchange="funcOtherShowHide('castCatg','otherCastCatg')">
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
						<div align="left"><nested:text property="applicantDetails.personalData.casteOthers" name="onlineApplicationForm" size="10" maxlength="30" styleId="otherCastCatg" styleClass="textbox" style="<%=dynaStyle %>"></nested:text></div>
						</td>
	           
	             		<td class="row-odd" align="right" ><div align="right">Participation in Cultural Activities:</div></td>
						<td class="row-even" align="left">
							<div align="left">
			                	<input type="hidden" id="arts" name="ats" value='<bean:write name="onlineApplicationForm" property="arts"/>'/>
		                        <nested:select property="applicantDetails.personalData.arts" name="onlineApplicationForm" styleClass="dropdown" styleId="arts1" onchange="showArtsParticipate(this.value)">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="International Level">International Level</html:option>
									<html:option value="National Level">National Level</html:option>
									<html:option value="State Level">State Level</html:option>
									<html:option value="Participated">Inter college Level</html:option>
									<html:option value="District Level">District Level</html:option>
								</nested:select>
								<a href="#" title="Select your level of participation" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
	           		</tr>
			   
			   		<tr>
			   			<td class="row-odd" align="right">If you are a <span style="color: red">Malakara Syrian Catholic-Parish</span> Name:</td>
			   			<td class="row-even"><nested:text property="applicantDetails.personalData.parishOthers" styleClass="textboxmedium"  name="onlineApplicationForm" size="10" maxlength="30" styleId="otherParish" ></nested:text></td>
	            		<td class="row-odd" align="right" ><div align="right">If Yes Achievement In Cultural Activities:</div></td>
		                <td class="row-even" align="left">
		                	<div align="left">
		                		<input type="hidden" id="artsParticipate" name="artsParticipate" value='<bean:write name="onlineApplicationForm" property="artsParticipate"/>'/>
	                        	<nested:select disabled="true" property="applicantDetails.personalData.artsParticipate" name="onlineApplicationForm" styleClass="dropdown" styleId="artsParticipate1">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="1 Prize">1 Prize</html:option>
									<html:option value="2 Prize">2 Prize</html:option>
									<html:option value="3 Prize">3 Prize</html:option>
									<html:option value="Participated">Participated</html:option>
								</nested:select>
								<a href="#" title="Select your achievement" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
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
						<td class="row-odd" align="right" ><div align="right">Participation in Sports:</div></td>
		            	<td class="row-even" align="left">
		            		<div align="left">
			            		<input type="hidden" id="sports" name="sports" value='<bean:write name="onlineApplicationForm" property="sports"/>'/>
		                		<nested:select property="applicantDetails.personalData.sports" name="onlineApplicationForm" styleClass="dropdown" styleId="sports1" onchange="funcSportsShowHide(this.value);">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="International Level">International Level</html:option>
									<html:option value="National Level">National Level</html:option>
									<html:option value="State Level">State Level</html:option>
									<html:option value="Participated">Inter college Level</html:option>
									<html:option value="District Level">District Level</html:option>
								</nested:select>
				 	 			<a href="#" title="Select if you are participte in any sports" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				 			</div> 
				 		</td>	
					</tr>
							
					<tr>
						<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">		
							<td class="row-odd" width="25%" align="right" ><div align="right">Dependent of Service/Ex-Service Man:</div></td>
							<td class="row-even" width="25%">
								<div align="left">
			           				<fieldset style="border: 0px">
		             					<nested:radio styleId="exservice" property="applicantDetails.personalData.exservice" name="onlineApplicationForm" value="true" ></nested:radio>
								  		<label for="exservice"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
					 					<nested:radio styleId="exservice1" property="applicantDetails.personalData.exservice" name="onlineApplicationForm" value="false" ></nested:radio>
								   		<label for="exservice1"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
						 				<a href="#" title="Select who your family are dependent of service/ex-service man" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
		    	       				</fieldset>
								</div>					
							</td>	
						</logic:equal>
						<logic:notEqual value="1" name="onlineApplicationForm" property="programTypeId" >
							<td class="row-odd" width="25%" align="right" ></td>
	          				<td class="row-even" width="25%"></td>
						</logic:notEqual>					
	                        
						<td class="row-odd" align="right" ><div align="right">Sports Item:</div></td>
		            	<td class="row-even" align="left">
		            		<div align="left">
								<nested:select disabled="true" property="applicantDetails.personalData.sportsId" name="onlineApplicationForm" styleClass="dropdown" styleId="sportsItem" onchange="showSportsParticipate(this.value);">
									<option value=""><bean:message key="knowledgepro.admin.select"/></option>
									<nested:optionsCollection name="onlineApplicationForm" property="sportsList" label="name" value="id"/>
									<html:option value="Other">Other</html:option>
								</nested:select>
								<div id="displayOtherSportsItem" style="display: none;">
									&nbsp;<nested:text property="applicantDetails.personalData.otherSportsItem" name="onlineApplicationForm" size="20" maxlength="20" styleId="otherSportsItem"/>
								</div>
	           					<a href="#" title="Select your sport" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
				 			</div> 
				 		</td>	     
					</tr>
	                 
					<tr>
						<%String dynaStyle5="display:none;"; %>
						<logic:equal value="true" property="ncccertificate" name="onlineApplicationForm">
							<%dynaStyle5="display:block;"; %>
						</logic:equal>
						<td class="row-odd" align="right" >Holder of NCC certificate (<span style="color: red;">in just previous course</span>):</td>
						<td class="row-even">
	                        <div>
								<input type="hidden" id="hiddenncccertificate" name="hiddenncccertificate" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.ncccertificate"/>'/>
	           				 	<fieldset style="border: 0px">
	             					<nested:radio styleId="ncccertificate" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="true" onclick="showNcccertificate()"></nested:radio>
							   		<label for="ncccertificate"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
				 					<nested:radio styleId="ncccertificate1" property="applicantDetails.personalData.ncccertificate" name="onlineApplicationForm" value="false" onclick="hideNcccertificate()"></nested:radio>
									<label for="ncccertificate1"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
				 					<a href="#" title="Select yes if you are a holder of ncc certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
	           				 	</fieldset>
							</div>
							<div id="ncccertificate_description" style="<%=dynaStyle5 %>">
								Grade of Certificate:
								<input type="hidden" id="nccgrade" name="nccgrade" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.nccgrades"/>'/>
								<nested:select property="applicantDetails.personalData.nccgrades" styleClass="dropdownmedium" name="onlineApplicationForm" styleId="nccgrade1">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="Weightage Certificate">Weightage Certificate</html:option>
									<html:option value="B">B</html:option>
									<html:option value="C">C</html:option>
								</nested:select>
							</div>
						</td>			
							
						<td class="row-odd" align="right" ><div align="right">If Yes Achievement In Sports:</div></td>
		                <td class="row-even" align="left">
		                	<div align="left">
			                	<input type="hidden" id="sportsParticipate" name="sportsParticipate" value='<bean:write name="onlineApplicationForm" property="sportsParticipate"/>'/>
		                        <nested:select disabled="true" property="applicantDetails.personalData.sportsParticipate" name="onlineApplicationForm" styleClass="dropdown" styleId="sportsParticipate1" onchange="checkDisabilityOfParticipationYear(this.value)">
									<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
									<html:option value="1st Prize">1st Prize</html:option>
									<html:option value="2nd Prize">2nd Prize</html:option>
									<html:option value="3rd Prize">3rd Prize</html:option>
									<html:option value="Participated">Participated</html:option>
								</nested:select>
								<a href="#" title="Select you achieve any thing in sports" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
							</div>
						</td>
					</tr>
	                
	                <tr>
						<td class="row-odd" align="right" >Holder of NSS certificate (<span style="color: red;">in just previous course</span>):</td>
						<td class="row-even">
							<div align="left">
		           				<fieldset style="border: 0px">
		             				<nested:radio styleId="nsscertificate" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="true" ></nested:radio>
									<label for="nsscertificate"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
					 				<nested:radio styleId="nsscertificate1" property="applicantDetails.personalData.nsscertificate" name="onlineApplicationForm" value="false" ></nested:radio>
									<label for="nsscertificate1"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
					 				<a href="#" title="Select yes if you are a holder of nss certificate from your previous course" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
		           				</fieldset>
							</div>
						</td>
						<td class="row-odd" align="right" ><div align="right">Participated year :</div></td>
	            		<td class="row-even" width="25%">
	            			<nested:select styleId="sportsParticipationYear" name="onlineApplicationForm" property="applicantDetails.personalData.sportsParticipationYear" styleClass="dropdown">
	            				<html:option value="">-Select-</html:option>
	            				<html:option value="2013">2013</html:option>
	            				<html:option value="2014">2014</html:option>
	            				<html:option value="2015">2015</html:option>
	            				<html:option value="2016">2016</html:option>
	            				<html:option value="2017">2017</html:option>
                                                <html:option value="2018">2018</html:option>
	            				<html:option value="2019">2019</html:option>
                                                <html:option value="2020">2020</html:option>
	            				

                              
	            			</nested:select>
		            		<a href="#" title="Select the year in which you got the prize in sports participation"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
						</td>			           
		          	</tr>
		          	<logic:equal name="onlineApplicationForm" property="programTypeId" value="1">
		          	<tr>
            			<td class="row-odd" align="right">Holder of SPC certificate (Student Police cadet ):<span class="Mandatory">*</span></td>
            			<nested:hidden property="applicantDetails.personalData.spc" name="onlineApplicationForm" styleId="spc"></nested:hidden>
			            <td class="row-even">
			            	<fieldset style="border: 0px">
			             		<input type="radio" id="spcradYes" name="spc" value="false" onclick="setSpc(true)"/>
			             		<label for="no"><span><span></span></span>Yes</label> 
						 		<input type="radio" id="spcradNo" name="spc" value="true" onclick="setSpc(false)">
						 		<label for="yes"><span><span></span></span>No</label>
						 		<a href="#" title="Select If yes" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a> 
			   				</fieldset>
						</td>
             			<td class="row-odd" align="right">Holder of  Scouts and Guides:<span class="Mandatory">*</span></td>
			            <nested:hidden name="onlineApplicationForm" property="applicantDetails.personalData.scouts" styleId="scot"></nested:hidden>
			            <td class="row-even">
			            	<input type="radio" Id="scotradYes" name="scout" value="false" onclick="setScot(true)"/>
			             														<label for="no"><span><span></span></span>Yes</label> 
						 														<input type="radio" Id="scotradNo" name="scout" value="true" onclick="setScot(false)"/>
						 														<label for="yes"><span><span></span></span>No</label>
						 														<a href="#" title="Select If yes" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a> 
			            
			            </td>
					</tr>
			   </logic:equal>
			   		<!--  this one we are not using -->
			    	<tr style="display: none;">
						<%String dynaStyle3="display:none;"; %>
						<logic:equal value="true" property="sportsPerson" name="onlineApplicationForm">
							<%dynaStyle3="display:block;"; %>
						</logic:equal>
						<td align="right"><bean:message key="knowledgepro.applicationform.sports.label"/></td>
	                   	<td>
	                   		<div align="left">
	                    		<input type="hidden" id="hiddenSportsPerson" name="hiddenSportsPerson" value='<bean:write name="onlineApplicationForm" property="applicantDetails.personalData.sportsPerson"/>'/>
	             				<fieldset style="border: 0px">
	             					<nested:radio property="applicantDetails.personalData.sportsPerson" styleId="sportsPersonYes" name="onlineApplicationForm" value="true" onclick="showSportsDescription()"></nested:radio>
									<label for="sportsPersonYes"><span><span></span></span><bean:message key="knowledgepro.applicationform.yes.label"/></label> 
				 					<nested:radio property="applicantDetails.personalData.sportsPerson" styleId="sportsPersonNo" name="onlineApplicationForm" value="false" onclick="hideSportsDescription()"></nested:radio>
									<label for="sportsPersonNo"><span><span></span></span><bean:message key="knowledgepro.applicationform.No.label"/></label> 
				 					<a href="#" title="Select yes if you are a sports person and write description." class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
	             				</fieldset>
							</div>
							<div align="left" id="sports_description" style="<%=dynaStyle3 %>">
								<nested:text styleId="sportsDescription" property="applicantDetails.personalData.sportsDescription" name="onlineApplicationForm"  maxlength="50" styleClass="textbox" ></nested:text>
							</div>	
						</td>		
	           		</tr>
	        	</table>
	        </td>
		</tr>
	   
	    <tr ><td height="30"></td></tr>
	    <tr ><td height="20"></td></tr>
	   
	   	<tr>
	  		<td>
	  			<table width="100%">
	      			<tr>
	        			<td width="100%" align="center">
	        				<html:button property="" styleClass="cntbtn" styleId="SubmitPersonalDetailPage" value="Save & Continue to Education Details" > </html:button>
	         			</td>
	      			</tr>
	  			</table>
	  		</td>
	  	</tr>
	  
	   	<tr>
	  		<td>
		  		<table width="100%">
		      		<tr>
		        		<td width="100%" align="center">
		        			<br/>
		        			<html:button property="" value="Clear" styleClass="btn1" onclick="resetPersonalForm();" /> 
				 			&nbsp; <html:button property="" onclick="cancel()" styleClass="cancelbtn" value="Logout"></html:button>
		         		</td>
		      		</tr>
		  		</table>
	  		</td>
	  	</tr>
	  
	   	<tr><td height="30"></td></tr>
	   
	</table>
	
	<script language="JavaScript" src="js/admission/OnlineDetailsPersonalInfo.js"></script>
	<script type="text/javascript">
		onLoadAddrCheck();
	
		var relgId = document.getElementById("religionType").value;
		if(relgId != null && relgId.length != 0) {
			document.getElementById("religions").value = relgId;
		}
	
		var sameAddr= document.getElementById("sameAddr").checked;
		if(sameAddr==false){
			var permanentCountryNamehidden = document.getElementById("permanentCountryNamehidden").value;
			if(permanentCountryNamehidden != null && permanentCountryNamehidden.length != 0) {
				document.getElementById("permanentCountryName").value = permanentCountryNamehidden;
			}	
		}
	
		var sameParAddr= document.getElementById("sameParAddr").checked;
		if(sameParAddr==false){
			var hiddenParentCountryId = document.getElementById("hiddenParentCountryId").value;
			if(hiddenParentCountryId != null && hiddenParentCountryId.length != 0) {
				document.getElementById("parentCountryName").value = hiddenParentCountryId;
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
			if(otherTempAddrState!='') {
				document.getElementById("tempAddrstate").value='Other';
				$("#otherTempAddrState").show();
			}

			var otherPermAddrState =  document.getElementById("otherPermAddrState").value;
			if(otherPermAddrState!=''){
				document.getElementById("permAddrstate").value='Other';
				$("#otherPermAddrState").show();
			}

			var otherParentAddrState =  document.getElementById("otherParentAddrState").value;
			if(otherParentAddrState!=''){
				document.getElementById("parentState").value='Other';
				$("#otherParentAddrState").show();
			}

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
		
			var showNcc = document.getElementById("hiddenncccertificate").value;
			if(showNcc != null && showNcc.length != 0 && showNcc=='true') {
				showNcccertificate();	
			}else{
				hideNcccertificate();
			}

			var sportsPrize =  document.getElementById("sportsParticipate1").value;
			if(sportsPrize!=""){
				document.getElementById("sportsParticipationYear").disabled=false;
			}else{
				document.getElementById("sportsParticipationYear").disabled=true;
			}
		});

		function displayOtherForMother(occpation){
			if(document.getElementById("motherOccupation").value=="Other"){
				document.getElementById("displayMotherOccupation").style.display = "block";		}else{
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
		var spc = document.getElementById("spc").value;
		if(spc=="true") {
			document.getElementById("spcradYes").checked = true;
		}else{
			document.getElementById("spcradNo").checked = true;
		}
		
		var scot = document.getElementById("scot").value;
		if(scot=="true") {
			document.getElementById("scotradYes").checked = true;
		}else{
			document.getElementById("scotradNo").checked = true;
		}
	</script>