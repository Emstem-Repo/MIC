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
<script language="JavaScript" src="js/admission/OnlineDetailsAppCreation.js"></script>

<script language="JavaScript" src="js/admission/studentdetails.js"></script>
<html:hidden property="focusValue" styleId="focusValue" name="onlineApplicationForm"/>
<html:hidden property="onlineApply" styleId="onlineApply" name="onlineApplicationForm"/>
<html:hidden property="pageType" value="" />
 			
 			
	
	
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
 
    
   <tr ><td height="20px"></td></tr>   
  
  <tr ><td height="30"></td></tr>
   
   <!-- errors display -->
  <tr >
	<td  align="center">
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
      <tr>
        <td>
        <table width="100%" border="0" cellpadding="0"  align="center" class="normal inst w" >
          <tr ><td colspan="2" height="10"></td></tr>
          <!--<tr>
		  <td style="color:#330000; text-align:right" width="10%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
            <td width="100%" >Click the Final Submit Button If you don't have any editing to do to your Application.</td>
		  </tr>
		  --><tr>
		  <td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%">Please click on PREVIEW button to verify all information</td>
		 </tr>
		 <tr>
		 <td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" >To make corrections click on EDIT button.</td>
		</tr>
		<!--<tr>
		<td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" >Printing of Application can be done only after final submission Print Application link will be available from Downloads & Prints.</td>
		</tr>
		 --><tr>
		<td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" >When ready for final submission click on SUBMIT WITH NO MORE EDIT</td>
		</tr>
		<tr>
		<td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" >If you don't want to submit the application now click SAVE AND LOGOUT</td>
		</tr>
		 <tr>
		<td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" ><font color="red">LAST DATE FOR FINAL SUBMISSION <%=CMSConstants.CLOSE_DATE %>.</font></td>
		</tr>
		<!--<tr>
		<td style="color:#330000; text-align:right" width="6%"><img src="images/admission/images/bullet_triangle_red.png" width="20" height="20" />&nbsp;</td>
			<td width="80%" >No Excuses pertaining to not following the deadline will be entertained.</td>
		</tr>
		-->		 
        </table>
        </td>
      </tr>
   <tr><td>&nbsp;</td></tr>
  	<tr><td><table border="0" cellpadding="0"  align="center"  width="65%" style="color: #2d0000; font-size: 17px; font-style: oblique; font-weight: bold;"><tr>
		<td style="color:#330000; text-align:left" width="6%"><input type="checkbox" name="checkbox" value="check" id="agreeTermsandConditions" />
			The information given is true to the best of my knowledge and belief. I accept that incorrect or incomplete information may lead to disqualification of my admission.</td>
		</tr>
		</table>
		</td>
		</tr>
 <tr ><td height="20px"></td></tr>   
  
  
  <tr>
  <td  width="100%" align="center">
  <html:button property="" onclick="submitAdmissionForm('submitConfirmPageOnline')" styleClass="btn" value=" Save and Logout"></html:button>
  <html:button property="" onclick="submitAdmissionForm('submitPeviewApplication')" styleClass="btn" value=" Preview "></html:button>
  <html:button property="" onclick="submitAdmissionForm('submitEditApplication')" styleClass="btn" value=" Edit "></html:button>
  </td>
  </tr>
  
  <tr ><td height="20px"></td></tr>   
  
  <tr>
  <td  width="100%" align="center">
  <html:button property="" onclick="submitFinalAdmissionForm('submitCompleteApplication')" styleClass="cntbtn" value="Submit With No More Edit"></html:button>
  </td>
  </tr>
  
 <tr ><td height="40"></td></tr>
   
</table>

