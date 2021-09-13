<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 
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

		<link rel="stylesheet" href="css/admission/css/stylesheet-pure-css.css"/>  
		<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
		<script src="js/AC_RunActiveContent.js" type="text/javascript"></script>
		<link type="text/css" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
		<script src="jquery/js/jquery-ui-1.10.0.custom.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/ajax/Ajax.js"></script>
		<script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
		<script type='text/javascript' src="jquery/Zebra/javascript/zebra_dialog.js"></script>
		<link rel="stylesheet" href="jquery/Zebra/css/default/zebra_dialog.css"  />
		<link rel="stylesheet" type="text/css" href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
		<script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>

		<style type="text/css">
			.ui-datepicker {
		        font-family:Garamond;
		        font-size: 14px;
		        margin-left:10px
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
	
		<style type="text/css">
			input[type="radio"] {
			    -webkit-appearance: checkbox;
			    -moz-appearance: checkbox;
			    -ms-appearance: checkbox;     /* not currently supported */
			    -o-appearance: checkbox;      /* not currently supported */
			     transform:scale(1.3, 1.3);
			     color: white;
			     background: white; 
			     background-color: white;
			}
		</style>

		<script type="text/javascript">
		
			function getMobileNo(){
				var residentId = document.getElementById("residentCategory").value;
				var nativeCountry = document.getElementById("nativeCountry").value;
				var  nativeCountrys = nativeCountry.split(",");
				var indian=false;
				for ( var i = 0; i < nativeCountrys.length; i++) {
					if(nativeCountrys[i]==residentId){
						indian = true;
					}
				}
				if(indian){
					document.getElementById("mobilecode").readOnly = true;
					document.getElementById("mobilecode").value="91";
				}else{
					document.getElementById("mobilecode").value="";
					document.getElementById("mobilecode").readOnly = false;
				}			
			}
			
			function IsAlpha(e) {
				var inputValue = e.which;
				if(!(inputValue >= 65 && inputValue <= 120) && (inputValue != 32 && inputValue != 0) && (inputValue != 8 && inputValue != 0) && (inputValue != 127 && inputValue != 0)) {
					e.preventDefault(); 
				}
			}
		 function defparishname(val){
			if (val=='true') {
				document.getElementById("pname").style.display="block";
			}
			else if(val=='false'){
				document.getElementById("pname").style.display="none";
			}
		 }	 
		</script>
		<title>Online Application Form</title>
	</head>

	<html:form action="/uniqueIdRegistration" method="post" styleId="register">
		<html:hidden property="method" styleId="method" value="" />
		<html:hidden property="formName" value="uniqueIdRegistrationForm" />
		<html:hidden property="mode" name="uniqueIdRegistrationForm" styleId="mode" />
		<html:hidden property="pageType" name="uniqueIdRegistrationForm" styleId="pageType" value="1" />
		<html:hidden property="offlinePage" styleId="offlinePage" name="uniqueIdRegistrationForm"/>
		<html:hidden property="serverDownMessage" styleId="serverDownMessage" name="uniqueIdRegistrationForm" />
		<html:hidden property="nativeCountry" styleId="nativeCountry" name="uniqueIdRegistrationForm"/>
	
		<body>	
			<table width="80%" style="background-color: #808080" align="center">
				<tr>
	 				<td height="20px">
						<!-- errors display -->
					    <div align="center" class="subheading">
							<div id="errorMessage" align="center">
								<FONT color="red"><html:errors /></FONT>
							</div>
							<div id="errorMessage1" style="font-size: 11px; color: red"></div>
						</div>
					</td>
				</tr>
	
   				<tr><td align="center" class="subheading"><span style="color: white;">REGISTRATION DETAILS</span></td></tr>
  			</table>
  			<table width="80%" style="background-color: #F0F8FF" align="center">
      			<tr>
        			<td>
        				<table width="100%" border="0" cellspacing="1" cellpadding="2" height="50%" align="center">
          					<tr>
            					<td align="right" class="row-odd" width="55%"  height="30%"><span class="Mandatory">*</span><font size="3px">Candidate&rsquo;s Name</font><font size="1" color="red">(as in Class 10 record)</font></td>
            					<td class="row-even">
             						<html:text property="applicantName" styleClass="textboxmedium" styleId="applicantName" size="30" maxlength="90" name="uniqueIdRegistrationForm" errorStyleClass="error" onkeypress="IsAlphaDot(event)" style="text-transform:uppercase"></html:text>
          							<a href="#" title="Enter name based on 10th class records" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
          					</tr>
           					<tr>
					            <td align="right" class="row-odd"  height="50%">
					            	<span class="Mandatory">*</span>
					            	<font size="3px">
					            		Date of Birth</font>
										<font size="1" color="red"><bean:message key="admissionForm.application.dateformat.label"/>
						                <bean:message key="knowledgepro.applicationform.dob.format"/></font>
					            	
					          	</td>
            					<td class="row-even" height="50%">
             						<html:text property="registerDateOfBirth" 
             								   styleClass="textboxmedium" 
             								   name="uniqueIdRegistrationForm" 
             								   styleId="registerDateOfBirth" 
		 									   onkeypress="return checkNumeric(event)" 
		 									   size="10" 
		 									   maxlength="10" />
						 			<script language="JavaScript">
										$(function(){
											var d = new Date();
											var year = d.getFullYear() - 15 ;
											$("#registerDateOfBirth").datepicker({dateFormat:"dd/mm/yy",
												changeMonth: true,
												changeYear: true,
												yearRange: '1940:' + year, 
												defaultDate: new Date(year, 0, 1),
												reverseYearRange: true
											});
										});
									</script>
            						<a href="#" title="Select date of birth based on 10th class records" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
          					</tr>
            				<tr>
            					<td align="right" class="row-odd"  height="50%"><span class="Mandatory">*</span><font size="3px">Gender</font></td>
            					<td class="row-even">
					            	<fieldset style="border: 0px">
					             		<html:radio  property="gender" styleId="MALE"  value="MALE"></html:radio>
					             		<label for="MALE"><span><span></span></span><font size="3px">MALE</font></label> 
								
								 		<html:radio property="gender"  styleId="FEMALE" value="FEMALE"></html:radio>
								 		<label for="FEMALE"><span><span></span></span><font size="3px">FEMALE</font></label> 			 			
									</fieldset>
									<fieldset style="border: 0px">
										<html:radio property="gender"  styleId="TRANSGENDER" value="TRANSGENDER"></html:radio>
								 		<label for="TRANSGENDER"><span><span></span></span><font size="3px">TRANSGENDER</font></label> 
								 		<a href="#" title="Select gender" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
									</fieldset>	
            					</td>
          					</tr>
		  					<tr>
					            <td align="right" class="row-odd"  height="30%">
					            	<span class="Mandatory">*</span>
									<font size="3px">Domicile Status</font>
					            </td>
            					<td class="row-even">
             						<html:select property="residentCategoryId" name="uniqueIdRegistrationForm" styleClass="dropdownsmall" styleId="residentCategory"  errorStyleClass="error" onchange="getMobileNo();showOtherOption(this.value);">
										<option value=""><bean:message key="knowledgepro.admin.select"/></option>
										<html:optionsCollection name="uniqueIdRegistrationForm" property="residentTypes" label="name" value="id"/>
									</html:select>
            						<a href="#" title="Select residential category" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            						<span id="residentCategoryOtherSpan" style="display: none;">
			 							<html:text property="categoryOther" styleId="categoryOther" name="uniqueIdRegistrationForm" styleClass="textboxmedium"></html:text>
			 							<a href="#" title="Enter option for Community Quota manually" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			 						</span>
            					</td>
          					</tr>
           					<tr>
            					<td align="right" class="row-odd"  width="50%" height="30%"><span class="Mandatory">*</span><font size="3px"><bean:message key="knowledgepro.admission.programtype"/></font></td>
            					<td class="row-even" width="50%">
             						<html:select styleId="programTypeId"  property="programTypeId" name="uniqueIdRegistrationForm" styleClass="dropdownsmall"   errorStyleClass="error"  >
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:programTypesOnlineOpen></cms:programTypesOnlineOpen>
									</html:select>
           							<a href="#" title="For BCom,BSc,BA,B Voc programs select UG program type and For MCom,MSc,MA,MTTM programs select PG program type" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
		 					</tr>
		  					<tr>
            					<td align="right" class="row-odd"  height="50%"><span class="Mandatory">*</span><font size="3px">Category</font></td>
            					<td class="row-even">
							        <html:select property="subReligionId" name="uniqueIdRegistrationForm" styleClass="dropdownsmall" styleId="subreligion" >
										<html:option value="">- Select -</html:option>
		    							<html:optionsCollection property="subReligionMap" name="uniqueIdRegistrationForm" label="value" value="key"/>
									</html:select>
			 						<a href="#" title="Select category" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
            				</tr>
		   					<tr>
            					<td align="right" class="row-odd"  height="50%">
            						<span class="Mandatory">*</span>
									<font size="3px">E-mail</font>
									<br/><font size="1px" color="red">(e.g. name@yahoo.com)</font>
        	 					</td>
            					<td class="row-even">
             						<html:text property="emailId" name="uniqueIdRegistrationForm" styleClass="textboxmedium" styleId="email" size="30" maxlength="30" onblur="checkMail(this.value)" onchange="checkMail(this.value)"/>
            						<a href="#" title="Enter mail id" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
          					</tr>
          					<tr>
            					<td align="right" class="row-odd"  height="50%">
            						<span class="Mandatory">*</span>
									<font size="3px">Re-confirm E-mail</font>
									<br/><font size="1px" color="red">(e.g. name@yahoo.com)</font>
        	 					</td>
            					<td class="row-even">
             						<html:text property="confirmEmailId" name="uniqueIdRegistrationForm" styleClass="textboxmedium" styleId="confirmEmailId" size="30" maxlength="30" onblur="checkMail(this.value)" onchange="checkMail(this.value)"  onmousedown="noCopyMouse(event)" onkeydown= "noCopyKey(event)" onkeyup="noCopyKey(event)"/>
           							<a href="#" title="Email id and confirm email id should same" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
          					</tr>
<!--          					<tr>-->
<!--            					<td align="right" class="row-odd"  height="30%"><span class="Mandatory">*</span><font size="3px">Country Mobile Code</font><br/><font size="3px">(Only India Number Possible)</font></td>-->
<!--            					<td class="row-even">-->
<!--             						<html:text readonly="false" property="mobileCode" styleClass="textboxmedium" styleId="mobilecode" size="30" name="uniqueIdRegistrationForm" onkeypress="return isNumberKey(event)" maxlength="5"/>-->
<!--             						<a href="#" title="Enter mobile code, e.g. India 91 " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>-->
<!--            					</td>-->
<!--          					</tr>-->
		  					<tr>
            					<td align="right" class="row-odd"  height="30%"><span class="Mandatory">*</span><font size="3px">Mobile No </font></td>
            					<td class="row-even">
            					    <html:text readonly="false" property="mobileCode"  styleId="mobilecode" size="5" name="uniqueIdRegistrationForm" onkeypress="return isNumberKey(event)" maxlength="5" style="border-radius: 6px;" />
            						<a href="#" title="Enter mobile code, e.g. India 91 " class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
             						<html:text property="mobileNo" styleClass="textboxmedium" styleId="mobile" size="30" name="uniqueIdRegistrationForm" onkeypress="return isNumberKey(event)" maxlength="10"/>
             						<a href="#" title="Enter mobile number" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
            					</td>
          					</tr>          
        				</table>
        			</td>
      			</tr>
      			<tr height="20px"><td></td></tr>
      			<tr>
        			<td>
        				<table width="100%" border="0" cellspacing="1" cellpadding="2" height="50%" align="center">
        					<tr>		   				
								<td class="row-odd"  height="30%" width="55%"><span class="Mandatory">*</span><font size="3px" color="red">Apply for Management Quota</font>
								 	<br/>
								 	<div align="right">
								 	<font size="2px">(There is no separate application form for Management Quota. If you click <b>'YES'</b> here your application will be considered both for General and Management Quota. 
								 	If you click <b>'NO'</b> you will  be consider only for General Quota)</font></div>
							 	</td>
                           		<td class="row-even">
             						<nested:radio  property="mngQuota" name="uniqueIdRegistrationForm" value="true" ></nested:radio>
							 		<label for="mngQuotatrue"><span><span></span></span><font size="3px"><bean:message key="knowledgepro.applicationform.yes.label"/></font></label> 
			 						<nested:radio  property="mngQuota" name="uniqueIdRegistrationForm" value="false" ></nested:radio>
									<label for="mngQuotafalse"><span><span></span></span><font size="3px"><bean:message key="knowledgepro.applicationform.No.label"/></font></label> 
			 						<a href="#" title="Enter option for management quota" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
			 						<a href="downloadForms/Managament Regulation.pdf" download>Download managament regulation form</a>
								</td>
							</tr>
							<tr>		   				
								<td class="row-odd"  height="30%"><span class="Mandatory">*</span><font size="3px" color="red">Apply For Community Quota</font><br></br><div align="right"><font size="2px">(If you belong to Malakara Syrian Catholic click 'YES' for considering you under community Quota)</font></div></td>
                           		<td class="row-even">
             						<nested:radio  property="malankara" name="uniqueIdRegistrationForm" value="true" onclick="defparishname('true');" styleId="comQuota"></nested:radio>
							 		<label for="malankaratrue"><span><span></span></span><font size="3px"><bean:message key="knowledgepro.applicationform.yes.label"/></font></label> 
			 						<nested:radio  property="malankara" name="uniqueIdRegistrationForm" value="false" onclick="defparishname('false');"></nested:radio>
									<label for="malankarafalse"><span><span></span></span><font size="3px"><bean:message key="knowledgepro.applicationform.No.label"/></font></label> 
			 						<a href="#" title="Enter option for Community Quota" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</td>
							</tr>
							<tr>
							<td colspan="2">
							<div id="pname">
								<table width="100%">
							<tr>		   				
								<td class="row-odd"  height="30%" width="55%"><span class="Mandatory">*</span><font size="3px" color="red">Malakara Syrian Catholic Parish Name</font></td>
                           		<td class="row-even">
			 						<html:text property="parishName" styleClass="textboxmedium" styleId="parishname" size="30" name="uniqueIdRegistrationForm" maxlength="10"/>
								</td>
							</tr>
							</table>
							</div>
							</td>
							</tr>
							
        				</table>
        			</td>
        		</tr>
  				<tr height="20px"><td></td></tr>
  				<tr>
  					<td width="100%" align="center">
   						<div align="center" id="buttons" style="display: block;">
  							<html:submit value="Register" styleClass="formbutton" styleId="register_validate" /> 
  							&nbsp; <html:button property="" value="Clear" styleClass="formbutton" onclick="resetForm('register');" /> 
  							&nbsp;<html:button value="Cancel" property="" styleClass="formbutton" onclick="loginPage()" />	
  						</div>
  					</td>
  				</tr>
   				<tr height="40px"><td></td></tr>
			</table>
			<!--End login form -->
			<!--End of body -->
		</body>
		<script src="js/admission/UniqueIdRegistrationPage.js" type="text/javascript"></script>	
		<script type="text/javascript">
			document.getElementById("buttons").style.display="block";
			var val=document.getElementById("comQuota");
			if (val.checked == true) {
				document.getElementById("pname").style.display="block";
			}else{
				document.getElementById("pname").style.display="none";
			}
			
		</script>
	</html:form>
</html>