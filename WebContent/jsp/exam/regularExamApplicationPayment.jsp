<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<html:html>
<head>
 <script src="jquery/development-bundle/jquery-1.7.1.js"></script>
 <script src="http://code.jquery.com/jquery-latest.js"></script>
 <title><bean:message key="knowledgepro.title"/> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript" src="js/calendar_us.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
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
	

</head>
<script type="text/javascript">
var jq=$.noConflict();

function payOnline(){
	document.newSupplementaryImpApplicationForm.method.value="redirectToRegularExamApplicationPGI";
	document.newSupplementaryImpApplicationForm.submit();
}

jq(document).ready(function(){

	
	var ctrlKeyDown = false;

	$(document).on("keydown", keydown);
    $(document).on("keyup", keyup);
   

    function keydown(e) { 
        if ((e.which || e.keyCode) == 116 || ((e.which || e.keyCode) == 82 && ctrlKeyDown)) {
            // Pressing F5 or Ctrl+R
            e.preventDefault();
        } else if ((e.which || e.keyCode) == 17) {
            // Pressing  only Ctrl
            ctrlKeyDown = true;
        }
    };

    function click(e){
    	e.preventDefault();
    };

    function keyup(e){
        // Key up Ctrl
        if ((e.which || e.keyCode) == 17) 
            ctrlKeyDown = false;
    };
    
	var options = {};
	if(jq('#SBIRadio').is(':checked')){
		 jq(".SBI").show();
		 jq(".NEFT").hide();
		 jq(".OnlinePayment").hide();
		 jq("#hideCloseButton").hide();
	}
	else if(jq('#NEFTRadio').is(':checked')){
		 jq(".NEFT").show();
		 jq(".SBI").hide();
		 jq(".OnlinePayment").hide();
		 jq("#hideCloseButton").hide();
	}
	else if(jq('#onlinePayRadio').is(':checked')){
		jq(".OnlinePayment").show();
		jq(".SBI").hide();
		jq(".NEFT").hide();
		jq("#hideCloseButton").hide();
	}
	else{
	jq(".SBI").hide();
	jq(".NEFT").hide();
	jq(".OnlinePayment").hide();
	jq("#hideCloseButton").show();
	}
	jq("#SBIRadio").click(function(){
		 jq(".SBI").show(2000);
		 jq(".NEFT").hide();
		 jq(".OnlinePayment").hide();
		 jq("#hideCloseButton").hide();
	  });

	jq("#NEFTRadio").click(function(){
		     jq(".SBI").hide();
			 jq(".NEFT").show(2000);
			 jq(".OnlinePayment").hide();
			 jq("#hideCloseButton").hide();
		  });

	jq("#onlinePayRadio").click(function(){
		 jq(".SBI").hide();
		 jq(".NEFT").hide();
		 jq(".OnlinePayment").show(2000);
		 jq("#hideCloseButton").hide();
		  });

	  
	});

function submitPaymentForm()
{
	document.getElementById("method").value="submitRegularExamApplicationChallan";
    document.newSupplementaryImpApplicationForm.submit();	
}	

function resetPaymentform(){   
	
	document.getElementById("bankBranch").value = "";
	document.getElementById("applicationdate").value = "";
	document.getElementById("ddDrawnOn").value = "";
	document.getElementById("ddDate").value = "";
}

function submitRegularExamForm(method){
	document.newSupplementaryImpApplicationForm.method.value=method;
	document.newSupplementaryImpApplicationForm.submit();
}
function printChallanApplication()
{
	var url ="newSupplementaryImpApp.do?method=showPrintChallanForRegular";
	myRef = window.open(url,"certificateCourse","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}
</script>


<title>Regular Exam Application Form</title>


<body>

<%
	String submitjsmethod=null;
%>
<logic:notEmpty name="transactionstatus" scope="request">
	<%
		submitjsmethod="#";
	%>
</logic:notEmpty>
<logic:empty name="transactionstatus" scope="request">
	<%
		submitjsmethod="submitRegularExamForm('submitRegularExamFormInfo')";
	%>
</logic:empty>
<%String dynaMandate=""; %>
 <logic:equal value="true" property="onlineApply" name="newSupplementaryImpApplicationForm">
	<%dynaMandate="<span class='Mandatory'>*</span>"; %>
</logic:equal>
	
<html:form action="/newSupplementaryImpApp">
	<html:hidden property="formName" value="newSupplementaryImpApplicationForm" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method" value="calculateAmount" />

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader">Exam Regular Application Payment</strong></td>

					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr height="10%">
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" height="10%">
					
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>

							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="regularAppAvailable">
						<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
						
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="5">								
									<table align="center" width="100%" border="0" style="border-collapse:collapse">
 										<tr><td>&nbsp;</td></tr>
 										<tr><td>
        									<table width="100%" border="0" cellpadding="0"  align="center" class="profiletable">
 												 <tr>
			    									<td height="50px" align="center" colspan="3"><font size="1px" style="font-weight: bold;">Fee Payment Options</font></td>
			      								 </tr>
			      								  <tr>
			    									<td>&nbsp;</td>
			      								 </tr>
			      								 <tr>
			    									<td height="50px" width="25%" align="center" class="pay"><b>Challan Payment</b></td>
			      								    <td height="50px" width="30%" align="center" class="pay" style="display: none;"></td>
													<td width="45%" align="center" class="pay"><b>Online Payment</b></td>		      								 </tr>
			      								 <tr>
			    									<td height="50px" width="25%" align="center" class="pay">
			    										<table>
			    										<tr><td>&nbsp;&nbsp;1. &nbsp;<html:button property="" styleClass="cntbtn" value="Download Challan" onclick="printChallanApplication()"></html:button></td></tr>
			    										<tr><td><fieldset style="border: 0px">
			    										2. <html:radio styleId="SBIRadio" property="selectedFeePayment" value="SBI"></html:radio>
			    										<label for="SBIRadio"><span><span></span></span><b>Click if paid with SIB challan</b></label> 
			    										</fieldset></td></tr>
			    										</table>
			    										
			    									</td>
			    
			    									<td height="50px" width="30%" align="center" class="pay" style="display: none;">
			    										<fieldset style="border: 0px">
			   	 										<html:radio styleId="NEFTRadio" property="selectedFeePayment" value="NEFT"></html:radio>
			    										<label for="NEFTRadio"><span><span></span></span><b>Click if paid by NEFT</b></label> 
			    										</fieldset>
			    									</td>
			    
			     									<td width="45%" align="center" class="pay">
			     										<br><fieldset style="border: 0px">
			     										<html:radio styleId="onlinePayRadio" property="selectedFeePayment" value="OnlinePayment"></html:radio>
			     										<label for="onlinePayRadio"><span><span></span></span><b>Click to make online payment using Credit Card /Debit Card /Net Banking</b></label> 
			     										</fieldset>
			     									</td>
			  									</tr>
			  									<tr><td height="30">&nbsp;</td></tr>
			  									<tr class="SBI">
        											<td colspan="3" align="center">
        												<table width="100%"  border="0" cellpadding="0"  align="center" class="profiletable">		      
			  											<tr>			    
                											<td height="30"  align="right" width="50%"><%=dynaMandate%><bean:message key="knowledgepro.admission.challanNo"/></td>
                											<td  height="30" align="left" width="50%"><html:text readonly="true" property="journalNo" size="15" styleId="journalNo" maxlength="30" styleClass="textbox"></html:text>
                 											<a href="#" title="Enter Journal No/Challan No" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                											</td>
              											</tr>             
			  
              											<tr>
                											<td height="30"  width="50%"><div align="right"><%=dynaMandate%><b><bean:message key="admissionForm.application.amount.label"/></b></div></td>
                											<td height="30"  align="left" width="50%">
                											<html:text readonly="true" property="applicationAmount" styleId="applicationAmount" size="15" maxlength="8" onkeypress="return isNumberKey(event)" styleClass="textbox"></html:text>
                 											<a href="#" title="Enter course fee amount" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
               												</td>
              											</tr>
              
														<tr>
                											<td height="30"  width="50%"><div align="right"><bean:message key="knowledgepro.hostel.reservation.bankBranchName"/></div></td>
                											<td height="30"  align="left" width="50%"><html:text property="bankBranch" styleId="bankBranch" size="15" maxlength="20" styleClass="textbox" value="South Indian Bank, Main Branch via Market Road"></html:text>
                 											<a href="#" title="Enter bank branch name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                											</td>
           												</tr>
           
														<tr> 
															<td height="30"  width="50%"><div align="right"><%=dynaMandate%><bean:message key="admissionForm.application.date.label"/><bean:message key="admissionForm.application.dateformat.label"/></div></td>
                											<td height="30"  align="left" width="50%">                 
               												<html:text readonly="true" property="applicationDate" styleId="applicationdate" size="12" maxlength="15" styleClass="textbox"></html:text><script language="JavaScript">
																new tcal( {
																// form name
																'formname' :'newSupplementaryImpApplicationForm',
																// input name
																'controlname' :'applicationdate'
																});
															</script>
				 											<a href="#" title="Enter challan date" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
															</td>
              												</tr>			  
        													</table>
        												</td>
      												</tr>                 
            
         											<tr><td height="10px"></td></tr>
            	
            										<tr class="SBI">
        												<td colspan="3" align="center">
        													<table width="100%" border="0" cellpadding="0"  align="center"   >
        													<tr>
        													<td width="48%" height="30"><div align="center">
                      										&nbsp; <html:button property="" onclick="submitPaymentForm()" styleClass="cntbtn" value="Submit Payment Details"></html:button> 
                      										</div></td>
               												</tr>
        
        													<tr>
		        												<td colspan="3" align="center"><br/>
                   												<html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 		 
               													</td>
               												</tr>
       														</table>
        												</td>
       												</tr>
            	
          											<tr><td height="10"></td></tr>
 
  	   												<tr class="NEFT">
        												<td colspan="3" align="center">
        												<table width="100%"  border="0" cellpadding="0"  align="center" class="profiletable"  >
			  											<tr>			    
                											<td height="30"  align="right" width="50%"><%=dynaMandate%><bean:message key="knowledgepro.admission.challanNo"/></td>
                											<td  height="30" align="left" width="50%"><html:text readonly="true" property="ddNo" size="15" styleId="ddNo" maxlength="30" styleClass="textbox"></html:text>
                 											<a href="#" title="Enter NEFT journal no/challan no" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                											</td>
              											</tr>
			  
              											<tr>
                											<td height="30"  width="50%"><div align="right"><%=dynaMandate%><b><bean:message key="admissionForm.application.amount.label"/></b></div></td>
                											<td height="30"  align="left" width="50%">
                											<html:text readonly="true" property="ddAmount" styleId="ddAmount" size="15" maxlength="8" onkeypress="return isNumberKey(event)" styleClass="textbox"> </html:text>
                 											<a href="#" title="Enter course fee amount" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                											</td>
              											</tr>
              
														<tr>
                											<td height="30"  width="50%"><div align="right"><bean:message key="knowledgepro.hostel.reservation.bankBranchName"/></div></td>
                											<td height="30"  align="left" width="50%"><html:text property="ddDrawnOn" styleId="ddDrawnOn" size="15" maxlength="20" styleClass="textbox"></html:text>
                 											<a href="#" title="Enter NEFT bank branch name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                											</td>
           												</tr>
           
														<tr>  
                											<td height="30"  width="50%"><div align="right"><%=dynaMandate%><bean:message key="admissionForm.application.date.label"/><bean:message key="admissionForm.application.dateformat.label"/></div></td>
                											<td height="30"  align="left" width="50%">                 
               												<html:text readonly="true" property="ddDate" styleId="ddDate" size="12" maxlength="15" styleClass="textbox"></html:text><script language="JavaScript">
															new tcal( {
															// form name
															'formname' :'newSupplementaryImpApplicationForm',
															// input name
															'controlname' :'ddDate'
															});
															</script>
				 											<a href="#" title="Enter NEFT challan date" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
        													</td>
              											</tr>			  
        												</table>
        												</td>
      												</tr>                 
            
         											<tr><td height="10px"></td></tr> 
            	
        											<tr class="NEFT">
        												<td colspan="3" align="center">
        												<table width="100%" border="0" cellpadding="0"  align="center">
        												<tr>
		      												<td colspan="2" align="center"><html:button property="" onclick="submitPaymentForm()" styleClass="cntbtn" value="Submit Payment Details"></html:button> 
               												</td>
               											</tr>
        
        												<tr>
		        											<td colspan="2" align="center"><br/>
                   											<html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 
               												</td>
               											</tr>
       													</table>
        												</td>
       												</tr>
              
              										<tr><td height="10px"></td></tr>
              
             										<tr class="OnlinePayment">
            											<td colspan="3" align="center">            
            											<table width="100%" border="0" cellpadding="0"  align="center" class="profiletable"  >
            											<logic:equal value="false" name="newSupplementaryImpApplicationForm" property="paymentSuccess">           
            											<tr>
                											<td width="50%" height="30" ><div align="right"><%=dynaMandate%><b><bean:message key="admissionForm.application.amount.label"/></b></div></td>
                											<td width="50%"  height="30" >                
                 											<html:text readonly="true" property="applicationAmount1" styleId="applicationAmount1" disabled="false" size="15" maxlength="8" onkeypress="return isNumberKey(event)"> </html:text>
                 											<a href="#" title="Enter course fee for pay online" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
               												</td>               
             											</tr>             
             
            											<tr class="row-odd">
            												<td colspan="2" height="100" width="100%" class="heading" align="center" >
            												<p><b>You have chosen to pay online. You will be redirected to an external site for payment. Please click on Pay Online to make payment.<br/>
             												NOTE: Your CREDIT CARD/DEBIT CEARD/NET BANKING statement will show the transaction as coming from PAY U MONEY.<br/>
             												Please quote your Transaction ID for any queries relating to this request. </b></p></td>
            											</tr>
            
             											<tr>
            												<td colspan="2" height="20" class="heading"></td>
            											</tr>
            											</logic:equal>
            											</table>
            											</td>            
          											</tr> 
                
  													<tr><td height="10px"></td></tr>
 
 													<tr class="OnlinePayment">                 
                  										<td colspan="3" align="center">
                  										<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1">
                    									<tr>
                    										<logic:equal value="false" name="newSupplementaryImpApplicationForm" property="paymentSuccess">
                      										<td width="48%" height="30"><div align="center"><html:button property="" onclick="payOnline()" styleClass="cntbtn" value="Pay Online"></html:button>
                      										&nbsp; <html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 
		               										&nbsp; <html:button property="" onclick="cancel2()" styleClass="cancelbtn" value="Cancel"></html:button>
                      										</div></td>
                      										</logic:equal>
                      										
                       										<logic:equal value="true" name="newSupplementaryImpApplicationForm" property="paymentSuccess">
                      										<td width="48%" height="30"><div align="center"><html:button property=""  styleClass="cntbtn" value="Continue to fill Application" onclick='<%=submitjsmethod %>'></html:button></div></td>
                      										</logic:equal>
                    									</tr>
                  										</table>                 
                 										</td>
            										</tr>
                
 													<tr><td height="20px"></td></tr>
 
 													<tr id="hideCloseButton">
       													<td align="center" colspan="3">
                  										<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1">            
                    									<tr>
                       										<td colspan="2" height="21">
                       										<div align="center"><html:button property="" styleClass="cntbtn" value="Close" onclick="cancel2()">
                      										</html:button>
                      										</div>
                      										</td>                      
                    									</tr>
                  										</table>                 
                 										</td>
     												</tr> 
 
   													<tr><td height="40"></td></tr>	
   															  									
 												</table></td>
 											</tr> 										
    										</table>								
										</td>
										<td width="5" height="30" background="images/st_right.gif"></td>
									</tr>
									</nested:equal>
									</nested:equal>
									<tr>
										<td valign="top">
											<table width="100%" cellspacing="1" cellpadding="2">
											<nested:equal value="false" name="newSupplementaryImpApplicationForm" property="regularAppAvailable">
													Application is not available
											</nested:equal>
											<nested:equal value="true" name="newSupplementaryImpApplicationForm" property="isAttendanceShortage">
													You are not eligible for writing the Examination. Your attendance is below 75%.
											</nested:equal>							
											</table>
										</td>
									</tr>
									<tr>
							<td height="5"><img src="images/st_04.gif" width="5" height="5" /></td>
							<td background="images/st_05.gif"></td>
							<td><img src="images/st_06.gif" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>

					<td valign="top" class="news">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td><img src="images/st_Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/st_TcenterD.gif"></td>
					<td><img src="images/st_Tright_02.gif" width="9" height="29"></td>
				</tr>			
			</table>		
</html:form>
</body>
</html:html>