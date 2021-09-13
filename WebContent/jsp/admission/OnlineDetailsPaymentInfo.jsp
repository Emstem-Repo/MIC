<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
		<script language="JavaScript" src="js/admission/admissionform.js"></script>
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
		<script type="text/javascript">
			var jq=$.noConflict();
			
			function payOnline(){
				document.onlineApplicationForm.method.value="redirectToPGI";
				document.onlineApplicationForm.submit();
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
			
			function submitPaymentForm() {
				document.getElementById("method").value="submitApplicationFormInfoOnline";
			    document.onlineApplicationForm.submit();	
			}	
			
			function resetPaymentform() {   
				document.getElementById("bankBranch").value = "";
				document.getElementById("applicationdate").value = "";
				document.getElementById("ddDrawnOn").value = "";
				document.getElementById("ddDate").value = "";
			}
			
		</script>
		<title>Online Application Form</title>
	</head>

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
				submitjsmethod="submitAdmissionForm('submitApplicationFormInfo')";
			%>
		</logic:empty>
		<%String dynaMandate=""; %>
		<logic:equal value="true" property="onlineApply" name="onlineApplicationForm">
			<%dynaMandate="<span class='Mandatory'>*</span>"; %>
		</logic:equal>		
	
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
										<li class="acBlue">Payment</li>
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
  
  			<tr>
        		<td>
        			<table width="100%" border="0" cellpadding="0" align="center" class="row-odd">
			   			<tr><td height="50px" align="center" colspan="3"><font size="3px">Fee Payment Options</font></td></tr>
			  			<tr>
			  				<td height="50px" width="25%" align="center" class="pay">
							    <fieldset style="border: 0px" style="display : none;">
								    <html:radio styleId="SBIRadio" property="selectedFeePayment" value="SBI"></html:radio>
								    <label for="SBIRadio"><span><span></span></span>Click if paid with SIB challan</label> 
							    </fieldset>
						    </td>
						    <td height="50px" width="30%" align="center" class="pay">
							    <fieldset style="border: 0px" style="display : none;">
								    <html:radio styleId="NEFTRadio" property="selectedFeePayment" value="NEFT"></html:radio>
								    <label for="NEFTRadio"><span><span></span></span>Click if paid by NEFT</label> 
							    </fieldset>
						    </td>
							<td width="45%" align="center" class="pay" colspan="3">
							    <fieldset style="border: 0px">
									<html:radio styleId="onlinePayRadio" property="selectedFeePayment" value="OnlinePayment"></html:radio>
								    <label for="onlinePayRadio"><span><span></span></span>Click to make online payment using Credit Card /Debit Card /Net Banking/UPI</label> 
								</fieldset>
						    </td>
			  			</tr>
        			</table>
        		</td>
      		</tr>
      
      		<tr><td height="30"></td></tr>
 
  	   		<tr class="SBI">
        		<td>
        			<table width="100%"  border="0" cellpadding="0"  align="center" class="profiletable"  >
			  			<tr>
                			<td class="row-odd" height="30" align="right" width="50%"><%=dynaMandate%><bean:message key="knowledgepro.admission.challanNo"/></td>
                			<td class="row-even" height="30" align="left" width="50%"><html:text readonly="true" property="journalNo" size="15" styleId="journalNo" maxlength="30" styleClass="textbox"></html:text>
                 				<a href="#" title="Enter Journal No/Challan No" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
              			</tr>
              			<tr>
                			<td class="row-odd" height="30"  width="50%"><div align="right"><%=dynaMandate%><b><bean:message key="admissionForm.application.amount.label"/></b></div></td>
                			<td class="row-even" height="30"  align="left" width="50%">
                				<html:text readonly="true" property="applicationAmount" styleId="applicationAmount" size="15" maxlength="8" onkeypress="return isNumberKey(event)" styleClass="textbox"> </html:text>
                 				<a href="#" title="Enter course fee amount" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
              			</tr>
						<tr>
                			<td class="row-odd" height="30" width="50%"><div align="right"><bean:message key="knowledgepro.hostel.reservation.bankBranchName"/></div></td>
                			<td class="row-even" height="30" align="left" width="50%">
                				<html:text property="bankBranch" styleId="bankBranch" size="15" maxlength="20" styleClass="textbox"></html:text>
                 				<a href="#" title="Enter bank branch name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
           				</tr>
						<tr>    
			                <td class="row-odd" height="30"  width="50%"><div align="right"><%=dynaMandate%><bean:message key="admissionForm.application.date.label"/><bean:message key="admissionForm.application.dateformat.label"/></div></td>
			                <td class="row-even" height="30"  align="left" width="50%">
               					<html:text readonly="true" property="applicationDate" styleId="applicationdate" size="12" maxlength="15" styleClass="textbox"></html:text>
               					<script language="JavaScript">
									new tcal( {
										// form name
										'formname' :'onlineApplicationForm',
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
        		<td>
        			<table width="100%" border="0" cellpadding="0"  align="center">
        				<tr>
		      				<td colspan="2" align="center"><html:button property="" onclick="submitPaymentForm()" styleClass="cntbtn" value="Submit Payment Details"></html:button></td>
		               	</tr>
        				<tr>
		        			<td colspan="2" align="center">
		        				<br/>
                   				<html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 
		           				&nbsp;<html:button property="" onclick="cancel2()" styleClass="cancelbtn" value="Logout"></html:button>
               				</td>
               			</tr>
       				</table>
        		</td>
       		</tr>
            	
          	<tr><td height="10"></td></tr>
 
  	   		<tr class="NEFT">
        		<td>
        			<table width="100%"  border="0" cellpadding="0"  align="center" class="profiletable">
			  			<tr>
                			<td class="row-odd" height="30"  align="right" width="50%"><%=dynaMandate%><bean:message key="knowledgepro.admission.challanNo"/></td>
                			<td class="row-even" height="30" align="left" width="50%">
                				<html:text readonly="true" property="ddNo" size="15" styleId="ddNo" maxlength="30" styleClass="textbox"></html:text>
                 				<a href="#" title="Enter NEFT journal no/challan no" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
              			</tr>
              			<tr>
                			<td class="row-odd" height="30"  width="50%"><div align="right"><%=dynaMandate%><b><bean:message key="admissionForm.application.amount.label"/></b></div></td>
                			<td class="row-even" height="30"  align="left" width="50%">
                				<html:text readonly="true" property="ddAmount" styleId="ddAmount" size="15" maxlength="8" onkeypress="return isNumberKey(event)" styleClass="textbox"> </html:text>
                 				<a href="#" title="Enter course fee amount" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
              			</tr>
						<tr>
                			<td class="row-odd" height="30" width="50%"><div align="right"><bean:message key="knowledgepro.hostel.reservation.bankBranchName"/></div></td>
                			<td class="row-even" height="30" align="left" width="50%">
                				<html:text property="ddDrawnOn" styleId="ddDrawnOn" size="15" maxlength="20" styleClass="textbox"></html:text>
                 				<a href="#" title="Enter NEFT bank branch name" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
                			</td>
           				</tr>
						<tr>    
			                <td class="row-odd" height="30" width="50%"><div align="right"><%=dynaMandate%><bean:message key="admissionForm.application.date.label"/><bean:message key="admissionForm.application.dateformat.label"/></div></td>
			                <td class="row-even" height="30" align="left" width="50%">
               					<html:text readonly="true" property="ddDate" styleId="ddDate" size="12" maxlength="15" styleClass="textbox"></html:text>
               					<script language="JavaScript">
									new tcal( {
										// form name
										'formname' :'onlineApplicationForm',
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
        		<td>
        			<table width="100%" border="0" cellpadding="0" align="center">
        				<tr>
		      				<td colspan="2" align="center"><html:button property="" onclick="submitPaymentForm()" styleClass="cntbtn" value="Submit Payment Details"></html:button></td>
               			</tr>
        				<tr>
		        			<td colspan="2" align="center">
		        				<br/>
                   				<html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 
		           				&nbsp; <html:button property="" onclick="cancel2()" styleClass="cancelbtn" value="Logout"></html:button>
               				</td>
               			</tr>
       				</table>
        		</td>
       		</tr>
              
			<tr><td height="10px"></td></tr>
              
            <tr class="OnlinePayment">
            	<td align="center">
            		<table width="100%" border="0" cellpadding="0"  align="center" class="profiletable"  >
            			<logic:equal value="false" name="onlineApplicationForm" property="paymentSuccess">
            				<tr>
                				<td class="row-odd" width="50%" height="30" ><div align="right"><%=dynaMandate%><bean:message key="admissionForm.application.amount.label"/></div></td>
                				<td class="row-even" width="50%"  height="30" >
                 					<html:text readonly="true" property="applicationAmount1" styleId="applicationAmount1" disabled="false" size="15" maxlength="8" onkeypress="return isNumberKey(event)"> </html:text>
                 					<a href="#" title="Enter course fee for pay online" class="tooltip"><span title="Help"><img alt="" src="images/admission/images/Tooltip_QIcon.png"/></span></a>
               					</td>
             				</tr>
            				<tr class="row-odd">
            					<td colspan="2" height="100" width="100%" class="heading" align="center" >
            						<p>
            							<b>
            								You have chosen to pay online. You will be redirected to an external site for payment. Please click on Pay Online to make payment.<br/>
             								NOTE: Your CREDIT CARD/DEBIT CARD/NET BANKING statement will show the transaction as coming from PAY U MONEY.<br/>
             								Please quote your Transaction ID for any queries relating to this request.
             							</b>
             						</p>
             					</td>
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
				<td align="center">
					<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1">
                    	<tr>
                    		<logic:equal value="false" name="onlineApplicationForm" property="paymentSuccess">
                      			<td width="48%" height="30">
                      				<div align="center">
                      					<html:button property="" onclick="payOnline()" styleClass="cntbtn" value="Pay Online"></html:button>
	                      				&nbsp;<html:button property="" value="Clear" styleClass="btn1" onclick="resetPaymentform();" /> 
			               				&nbsp;<html:button property="" onclick="cancel2()" styleClass="cancelbtn" value="Cancel"></html:button>
                      				</div>
                      			</td>
                      		</logic:equal>
                       		<logic:equal value="true" name="onlineApplicationForm" property="paymentSuccess">
                      			<td width="48%" height="30"><div align="center"><html:button property=""  styleClass="cntbtn" value="Continue to fill Application" onclick='<%=submitjsmethod %>'></html:button></div></td>
                      		</logic:equal>
                    	</tr>
                  	</table>
				</td>
			</tr>
                
 			<tr><td height="20px"></td></tr>
 
 			<tr id="hideCloseButton">
       			<td>
					<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1">
						<tr>
                      		<td colspan="2" height="21">
                       			<div align="center">
                       				<html:button property="" styleClass="cntbtn" value="Close" onclick="cancel2()"></html:button>
                      			</div>
                      		</td>
                    	</tr>
                  	</table>
				</td>
     		</tr>
	     	<tr>
				<td>
					<span style="color: red;">
						In case of any problem with the payment kindly forward the mail you received from 'Payumoney'/Scanned copy of the challan from 
						the bank to
					</span>
					<span style="color: green;">
						<b><i>it.admin@mic.ac.in</i></b>
					</span>
					<span style="color: red;">
						or
					</span>
					<span style="color: green;">
						<b><i>info@mic.ac.in</i></b>
					</span> 
				</td>
	     	</tr>
 
   			<tr><td height="40"></td></tr>
		</table>
	</body>
</html:html>