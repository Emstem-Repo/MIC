<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">

<script type="text/javascript">
function printTc()
{
	
	var fromUsn=document.getElementById("fromUsn").value;
	var tcReDate=document.getElementById("tcReDate").value;
	var toReg=document.getElementById("toUsn").value;
	var url = "transferCertificate.do?method=rePrintTC&fromUsn="+fromUsn+"&tcReDate="+tcReDate+"&toUsn="+toReg;
	myRef = window.open(url,"TC","left=20,top=20,width=600,height=500,toolbar=2,resizable=0,scrollbars=1");
}
</script>

<html:form action="/transferCertificate" method="post">
	<html:hidden property="formName" value="transferCertificateForm" />
	<html:hidden property="method" styleId="method" value="initReprint"/>
	<html:hidden property="pageType" value="1"/>
	<table width="98%" border="0">
  		<tr>
    		<td><span class="heading"><bean:message key="knowledgepro.admission"/><span class="Bredcrumbs">&gt;&gt;<bean:message key="knowledgepro.admission.transferCertificate"/> &gt;&gt;</span></span></td>
  		</tr>
  		<tr>
    		<td>
    			<table width="100%" border="0" cellpadding="0" cellspacing="0">
      				<tr>
        				<td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        				<td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader"><bean:message key="knowledgepro.admission.transferCertificate"/></strong></div></td>
        				<td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      				</tr>
      				<tr>
        				<td height="122" valign="top" background="images/Tright_03_03.gif"></td>
        				<td valign="top" class="news">
        					<div align="center">
	              				<table width="100%" height="185"  border="0" cellpadding="0" cellspacing="0">
	                 				<tr>
	               	    				<td height="20" colspan="6" align="left">
	               	    					<div align="right" style="color:red"> <span class='MandatoryMark'>* Mandatory fields</span></div>
	               	    					<div id="err" style="color:red;font-family:arial;font-size:11px;"></div>
	               	    					<div id="errorMessage">
	                       						<FONT color="red"><html:errors/></FONT>
	                       						<FONT color="green">
													<html:messages id="msg" property="messages" message="true">
														<c:out value="${msg}" escapeXml="false"></c:out><br>
													</html:messages>
						  						</FONT>
						  					</div>
	               	    				</td>
	                 				</tr>
	                 				<tr>
	                    				<td height="35" colspan="6" class="body" >
				        					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		                     					<tr>
		                       						<td ><img src="images/01.gif" width="5" height="5" /></td>
		                       						<td width="914" background="images/02.gif"></td>
		                       						<td><img src="images/03.gif" width="5" height="5" /></td>
		                     					</tr>
		                     					<tr>
		                       						<td width="5"  background="images/left.gif"></td>
		                       						<td valign="top">
		                       							<table width="100%" cellspacing="1" cellpadding="2">
						        							<tr>
																<td class="row-odd"><div align="right"><bean:message key="knowledgepro.attendance.regno.from"/>:</div></td>
																<td class="row-even" align="left"><html:text property="fromUsn" styleId="fromUsn"></html:text> </td>
																<td class="row-odd"><div align="right"><bean:message key="knowledgepro.attendance.regno.to"/>:</div></td>
																<td class="row-even" align="left"><html:text property="toUsn" styleId="toUsn"></html:text> </td>
																<td class="row-odd" align="center"><div align="right"><bean:message key="admissionForm.education.TCDate.label"/>:</div></td>
		                                                        <td class="row-even"><html:text
												                 name="transferCertificateForm" property="tcReDate"
												                 styleId="tcReDate" size="16" maxlength="16" /> <script
												                 language="JavaScript">
												                 new tcal( {
												            	// form name
													          'formname' :'transferCertificateForm',
													          // input name
													          'controlname' :'tcReDate'
												              });
										             	     </script></td>
					                						</tr>
		                       							</table>
		                       						</td>
		                       						<td width="5" height="30"  background="images/right.gif"></td>
		                     					</tr>
	                     						<tr>
	                       							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
	                       							<td background="images/05.gif"></td>
	                       							<td><img src="images/06.gif" /></td>
	                     						</tr>
	                   						</table>
	                   					</td>	
	                 				</tr>
	                 				<tr>
	                   					<td height="35" colspan="6" class="body" >
	                   						<table width="100%" height="27"  border="0" cellpadding="0" cellspacing="0">
	                     						<tr>
	                       							<td width="45%">
	                       								<div align="right">	
	                       									<html:button styleClass="formbutton"  property="" onclick="printTc()"><bean:message key="knowledgepro.admission.transferCertificate.print"/></html:button>
	                       								</div>
	                       							</td>
	                       							<td width="2%"></td>
	                       							<td width="53%" height="45" align="left">
	                   	 								<html:button property="" styleClass="formbutton" value="Reset" onclick="resetFieldAndErrMsgs()"></html:button>
	                       							</td>
	                     						</tr>
	                   						</table>
	                   					</td>
	                				</tr>
	              				</table>
            				</div>
            			</td>
        				<td width="13" valign="top" background="images/Tright_3_3.gif" class="news"></td>
      				</tr>
      				<tr>
        				<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        				<td width="100%" background="images/TcenterD.gif"></td>
        				<td><img src="images/Tright_02.gif" width="9" height="29"></td>
      				</tr>
    			</table>
    		</td>
  		</tr>
	</table>
</html:form>