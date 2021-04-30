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
<LINK REL=StyleSheet HREF= "css/admission/AdmissionResponsiveTabs.css" TYPE="text/css">

 <script type="text/javascript">
		setStartDate();
		setInterval ( "checkSession()", 300000 );
		//raghu
		function downloadFile(documentId) {
			document.location.href = "DocumentDownloadAction.do?documentId="
				+ documentId;
		}
		
		function resetDocumentform(){   
			document.getElementById("editDocument").value = "";
			
		}
		
</script>

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
							 	<li class="acBlue">Upload Photo</li>
 							</ul>
						</div>
 					</td>
				</tr>
   			</table>
   		</td>
 	</tr>
  
  <tr ><td height="10"></td></tr>
   
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
	
 
   
  
      
   
  
 		<tr ><td height="10"></td></tr>
  
	
      <tr>
        <td>
        <table width="100%" border="0" cellpadding="4"  align="center" class="normal w"  >
        
        						<nested:iterate name="onlineApplicationForm" property="applicantDetails.editDocuments" indexId="count" id="docList" type="com.kp.cms.to.admin.ApplnDocTO" >
								
								
								<nested:equal value="true" property="photo" name="docList">
			
          						<tr height="80">
								<td height="25" width="25%" class="row-even" align="center"><nested:write name="docList" property="printName" /></td>
														
								<td  class="row-even" width="60%">
								<nested:equal value="true" property="photo" name="docList">
								<nested:file property="editDocument" styleId="editDocument"></nested:file>
								 <a href="#" title="Upload passport size photo" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
        		
								</nested:equal>
							   <nested:equal value="true" property="documentPresent" name="docList">
								<a	href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.view.image" /></a>
								</nested:equal>
								</td>
								
							    <nested:equal value="true" property="documentPresent" name="docList">
								
								<td width="30%">
								<img src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/PhotoServlet") %>'  height="150px" width="150px" />
          
								</td>
		 						</nested:equal>	
		 							
		 						<nested:notEqual value="true" property="documentPresent" name="docList">
											
								<td class="row-even" width="30%"><img src="images/admission/images/passport.png" width="60" height="60"/></td>
								</nested:notEqual>
		 						</tr>
		 
		 						</nested:equal>	
		 						
		 						<!-- signature -->
		 						<nested:equal value="true" property="signature" name="docList">
			
          						<tr height="80">
								<td height="25" width="25%" class="row-even" align="center"><nested:write name="docList" property="printName" /></td>
														
								<td  class="row-even" width="60%">
								<nested:equal value="true" property="signature" name="docList">
								<nested:file property="editDocument" styleId="editDocument"></nested:file>
								 <a href="#" title="Upload your signature" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
        		
								</nested:equal>
							   <nested:equal value="true" property="documentPresent" name="docList">
								<a	href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.view.image" /></a>
								</nested:equal>
								</td>
								
							    <nested:equal value="true" property="documentPresent" name="docList">
								
								<td class="row-even" width="30%">
								<img src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/PhotoServlet1") %>'  height="150px" width="150px" />
          
								</td>
		 						</nested:equal>	
		 							
		 						<nested:notEqual value="true" property="documentPresent" name="docList">
											
								<td class="row-even" width="30%"><img src="images/admission/images/signature.jpg" width="60" height="60"/></td>
								</nested:notEqual>
		 						</tr>
		 
		 						</nested:equal>	
		 						
		 						<!-- Consolidate Marks Card -->
		 					    	<nested:equal value="true" property="consolidateMarksCard" name="docList">
			
          						<tr height="80">
								<td height="25" width="25%" class="row-even" align="center"><nested:write name="docList" property="printName" /></td>
														
								<td  class="row-even" width="60%">
								<nested:equal value="true" property="consolidateMarksCard" name="docList">
								<nested:file property="editDocument" styleId="editDocument"></nested:file>
								 <a href="#" title="Upload Consolidate Marks Card" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
								</nested:equal>
								<nested:equal value="true" property="documentPresent" name="docList">
								<a	href="javascript:downloadFile('<nested:write name="docList" property="id"/>')"><bean:message key="knowledgepro.admission.download.consolidatemarkscard" /></a>
								</nested:equal>
							   
								</td>
		 						<td class="row-even">
		 						
		 						</td>
		 						</tr>
		 
		 						</nested:equal>	
		 						
		 
		 						
								</nested:iterate>
		 
		 
		
		 
          <tr class="row-even">
            <td colspan="3" width="100%" align="center"><font color="#FF0000">Photograph and Signature size should be less than 100kb and .jpg extension And Degree Marks Card size should be less than 350kb and .pdf extension</font></td>
		 </tr>
		 
		 
		
		 
         
        </table>
        </td>
      </tr>
   
  
 <tr ><td height="20"></td></tr>
  
   
  
  <tr>
  <td  width="100%" align="center">
  
  	 <html:button property="" onclick="submitAdmissionFormAttachment('submitAttachMentPageOnline')" styleClass="cntbtn" value="Save & Continue to Final Submission"></html:button>
   	
  
  </td>
  </tr>
  
  <tr>
  <td  width="100%" align="center">
  <br/>
  	<html:button property="" value="Clear" styleClass="btn1" onclick="resetDocumentform();" /> 
		 
	&nbsp; <html:button property="" onclick="cancel()" styleClass="cancelbtn" value="Logout"></html:button>
  	
  
  </td>
  </tr>
  
 <tr ><td height="40"></td></tr>
   
</table>	
	
	
<script language="JavaScript" src="js/admission/OnlineDetailsAppCreation.js"></script>
	