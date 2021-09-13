<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@page import="com.kp.cms.constants.KPPropertiesConfiguration"%>
<%@ page import="org.apache.struts.Globals"%> 
<%@ page import="org.apache.struts.taglib.html.Constants"%> 
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<meta name="viewport" content="width=device-width, initial-scale=1">
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
<html:hidden property="onlineApply" value="true" name="onlineApplicationForm"/>
<html:hidden property="serverDownMessage" styleId="serverDownMessage"  name="onlineApplicationForm"/>

<input type="hidden" name="<%= Constants.TOKEN_KEY %>" value="<%= session.getAttribute(Globals.TRANSACTION_TOKEN_KEY) %>" > 
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
								<li class="acBlue">Terms &amp; Conditions</li>
								<li class="acGrey">Payment</li>
								<li class="acGrey">Preferences</li>
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
  
   <tr ><td height="10px"></td></tr>
   
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
	
   <tr ><td height="10px"></td></tr>
   
      <tr>
        <td>
        <table width="100%" border="0" cellpadding="0"  align="center" class="normal inst" >
         <tr ><td height="20px"></td></tr>
          <tr>
             <td width="7%">&nbsp; 1.</td>
            <td>I have read the Prospectus and accept all the terms and conditions for admission, and accept all the rules and regulations mentioned in the Prospectus.</td>
		 </tr>
		 <tr><td>&nbsp;</td></tr>
		  <tr>
             <td width="7%">&nbsp; 2:</td>
            <td>I accept application fee terms and I have remitted the application fee through Challan/Online Payment.</td>
		 </tr>
		 <tr><td>&nbsp;</td></tr>		 
		  <tr>
             <td width="7%">&nbsp; 3:</td>
            <td>I understand and accept that I will be disqualified for admission if I enter any false or misleading information in this application form.</td>
		 </tr>
		  <tr ><td height="20px"></td></tr>
        </table>
        </td>
      </tr>
   
  	 <tr ><td height="20%"></td></tr>
  
   
      
 <tr style="font-size:18px; font-weight:bold;font-style:oblique">
   <td  align="center">
     I accept all the above terms and conditions.
   </td>
 </tr>
  
  <tr ><td height="20"></td></tr>
  
   <tr>
  <td  width="100%" align="center">
  
   <html:button property="" onclick="submitAdmissionForm('submitBasicInfo')" styleClass="cntbtn" value="Accept and Continue"></html:button>
    <%-- &nbsp; <html:button property="" onclick="cancel()" styleClass="btn btn2" value="Click To Decline"></html:button>
   --%> &nbsp; <html:button property="" onclick="cancel2()" styleClass="cancelbtn" value="Logout"></html:button>
              
   </td>
  </tr>
  
 
     <tr ><td height="40px"></td></tr>
     
</table>

	
	
	