<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
    <script type="text/javascript" src="js/AC_RunActiveContent.js"></script>
    <script type="text/javascript" src="js/sdmenu/sdmenu.js"></script>
    <script type="text/javascript" src="js/ajax/Ajax.js"></script>
    <script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
    <script type="text/javascript" src="js/leftSwitchMenu.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<%@page import="com.kp.cms.constants.CMSConstants"%>
	<script>
	function cancelAction() {
		document.location.href = "StudentLoginAction.do?method=returnHomePage";
	}
	

	
	</script>
<html:form action="/StudentLoginAction" >

<html:hidden property="formName" value="loginform"/>
<html:hidden property="pageType" value="1"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader">Fee Payment Details</strong></td>

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
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td valign="top">
							<table width="100%"  height="80" cellspacing="1" cellpadding="1">
								<tr>
									<td width="35%"  class="studentrow-odd">
									<div align="center">Student Name :</div>
									</td>
									<td width="35%" class="studentrow-odd">
									<div align="center"><bean:write name="loginform" property="studentName"/></div>
									</td>
									</tr>
									<tr>
									<td width="40%" class="studentrow-odd"><div align="center">Register No:</div> </td>
									<td width="10%"  align="center"  class = "studentrow-odd">
									<div align="center"><bean:write name="loginform" property="regNo"/></div>
									</td>
									</tr>
							</table>
							
							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						
						<tr>
						<td width="5" background="images/st_left.gif"></td>
						<td valign="top">
							<table width="100%"  height="30" cellspacing="1" cellpadding="1">
							<tr>
							<td class="studentrow-odd"><div align="center"> Exam Registration/Fee Details</div>
							</td>
							</tr>
							</table>
							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						
						
						
						<tr>
					<td height="10" valign="top" background="images/st_left.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>
							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="0">
							
								<tr>
									<td height="25" class="studentrow-odd" align="center" ><div align="center">Sl no</div></td>
									<td class="studentrow-odd"  align="center"><div align="center">Exam Name</div></td>
									<td class="studentrow-odd" align="center"><div align="center">Transaction Date</div></td>
									<td class="studentrow-odd"  align="center"><div align="center">Amount</div></td>
									<td class="studentrow-odd" align="center"> <div align="center">Payment Status</div></td>
									
								</tr>
								<logic:notEmpty name="loginform" property="paymentDetailsList">
								<logic:iterate id="list" name="loginform" property="paymentDetailsList" indexId="count">
								
									<c:choose>
										<c:when test="${count%2 == 0}">
											<tr class="studentrow-odd">
										</c:when>
										<c:otherwise>
											<tr class="studentrow-white">
										</c:otherwise>
									</c:choose>
									<td width="7%" height="25" align="center"><c:out value="${count+1}"/></td>
									<td width="20%" align="center"><bean:write name="list" property="examName" ></bean:write></td>
									<td width="10%" align="center"><bean:write name="list" property="txnDate" /></td>
									<td width="10%" align="center"><bean:write name="list"  property="amount"  /></td>
									<td width="10%" align="center"><bean:write name="list" property="txnStatus"  /></td>
								</logic:iterate>
								</logic:notEmpty>
							</table>
							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/st_04.gif" width="5" height="5" /></td>

							<td background="images/st_05.gif"></td>
							<td><img src="images/st_06.gif" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/st_right.gif" class="news"></td>
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
