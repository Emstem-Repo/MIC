<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
	
	<script type="text/javascript">
		function getDetails(){
			document.location.href="cancelPromotion.do?method=searchCancelPromotion";	
		}
		function cancelPromotion(){
			document.location.href="cancelPromotion.do?method=cancelPromotion";	
		}
		function closeWindow(){
			document.location.href="cancelPromotion.do?method=initCancelPromotion";	
		}
		
		function resetValues(){
			resetErrMsgs();
		}
	</script>
	<html:form action="/cancelPromotion" method="post">
	<html:hidden property="formName" value="cancelPromotionForm"/> 
	<html:hidden property="pageType" value="1"/>
	<html:hidden property="method" styleId="method" value="searchCancelPromotion" />
	<table width="100%" border="0">
		<tr>

			<td><span class="Bredcrumbs">
				<bean:message key="knowledgepro.admission" />
			<span class="Bredcrumbs">&gt;&gt;Cancel Promotion &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> Cancel Promotion</strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="122" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" cellspacing="1" cellpadding="2">
						<tr bgcolor="#FFFFFF">
							<td height="20" colspan="2">
							<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
							<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
							<FONT color="green"> <html:messages id="msg"
								property="messages" message="true">
								<c:out value="${msg}" escapeXml="false"></c:out>
								<br>
							</html:messages> </FONT></div>
							</td>
						</tr>


						<tr>
							<td valign="top" class="news">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0">
								<tr>
									<td><img src="images/01.gif" width="5" height="5" /></td>
									<td width="914" background="images/02.gif"></td>
									<td><img src="images/03.gif" width="5" height="5" /></td>
								</tr>
								<tr>
									<td width="5" background="images/left.gif"></td>
									<td valign="top">
									<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
			
									<td width="25%" height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>Register No:</div>
									</td>
									<td width="25%" height="25" class="row-even"><span
										class="star"> <html:text property="registerNo" 
										styleClass="TextBox" styleId="registerNo" 
										maxlength="30"/> </span></td>
								</tr>
			
			
									</table>
									</td>
									<td width="5" height="29" background="images/right.gif"></td>
								</tr>
			
								<tr>
									<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
									<td background="images/05.gif"></td>
									<td><img src="images/06.gif" /></td>
								</tr>
							</table>
							</td>
							<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
						</tr>

				<tr>
					<td height="25" colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="center">
							<input name="submit" type="submit" class="formbutton" value="Search" onclick="getDetails()" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:reset property="" value="Reset" styleClass="formbutton" onclick="resetValues()"></html:reset>
							</div>
							</td>
							
						</tr>
					</table>
					</td>
				</tr>
				
				<tr>
				<logic:notEmpty name="cancelPromotionForm" property="cancelPromotionTo">
				<logic:iterate id="cancelPromotionId" name="cancelPromotionForm" property="cancelPromotionTo">
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
						<tr>
							<td width="25%" height="25" class="row-odd">
									<div align="right">Student Name:</div>
									</td>
									<td width="25%" height="25" class="row-even">
										 <bean:write name="cancelPromotionId" property="studentName"/> </td>
						</tr>
						<tr>
							<td width="25%" height="25" class="row-odd">
									<div align="right">Class:</div>
									</td>
									<td width="25%" height="25" class="row-even">
										 <bean:write name="cancelPromotionId" property="classes"/> </td>
						</tr>
						<tr>
							<td width="25%" height="25" class="row-odd">
									<div align="right">Subject Group:</div>
									</td>
									<td width="25%" height="25" class="row-even">
									<logic:iterate id="subjectGroupId" name="cancelPromotionId" property="subjectGroupTo">
										 <bean:write name="subjectGroupId" property="subjectGroupName"/><br/><br/>
										 </logic:iterate> </td>
						</tr>
						
							</table>
							</td>
							<td width="5" height="29" background="images/right.gif"></td>
						</tr>

						<tr>
							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
							<td background="images/05.gif"></td>
							<td><img src="images/06.gif" /></td>
						</tr>
						<tr>
					<td height="25" colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="center">
							<html:button property="" styleClass="formbutton" value="Cancel Promotion" onclick="cancelPromotion()" ></html:button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:button property="" styleClass="formbutton" value="Close" onclick="closeWindow()" ></html:button>
							</div>
							</td>
							
						</tr>
					</table>
					</td>
				</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
					</logic:iterate>			
						</logic:notEmpty>
				</tr>
				

					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif" class="news"></td>

				</tr>

				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td height="20" valign="top"></td>
					<td width="13" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif" width="9" height="29" /></td>
					<td width="100%" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29" /></td>
				</tr>

			</table>
			</td>
		</tr>
	</table>
</html:form>