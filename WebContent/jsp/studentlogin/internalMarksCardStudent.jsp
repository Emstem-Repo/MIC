<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<link rel="stylesheet"
	href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/sdmenu.css" />
<link rel="stylesheet" href="jquery/development-bundle/demos/demos.css">
<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
<script language="JavaScript" src="js/ajax/AjaxUtil.js"></script>
<script language="JavaScript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript">
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=returnHomePage";
}

function printMarkscard()
{
	var url = "StudentLoginAction.do?method=printCommonInternalMarksCard";
	var browserName=navigator.appName; 
		 myRef = window.open(url,"MarksCard","left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
}


</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/StudentLoginAction">
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="method" styleId="method" value="" />
	
	
	<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message
				key="knowledgepro.admin.studentlogin" /> <span class="Bredcrumbs">&gt;&gt;
			Internal Marks card &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/st_Tright_03_01.gif"></td>
					<td width="100%" background="images/st_Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader">
					Internal Marks card</strong></div>
					</td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
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
							<td width="100%" valign="top">
							<table width="100%" cellspacing="8" cellpadding="2"
								class="row-white">
							<tr>
							<td align="center" style="font-size: 25px">
								<b>St Berchmans College</b>
							</td>			
							</tr>
							
								<tr>
							
							<td  align="center" style="font-size: 15px">
								<b>INTERNAL MARKS</b>
							</td>
							</tr>
							
							</table>

							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						
						
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2"
								class="row-white">
								<tr>
									<td colspan="2">
									<table width="100%" cellpadding="10" cellspacing="1">
										<tr>
											<td height="20" 
												align="center" width="30%">Name of Programme : <bean:write name="loginform" property="programName"/></td>

										</tr>
										<tr>
										
										<td height="20" 
												align="center" width="30%">Semester :  <bean:write name="loginform" property="internalMarks.semester" /></td>
											<td height="20" 
												align="center">Register No :  <bean:write name="loginform" property="internalMarks.registerNo" /></td>

										</tr>
										
											<tr>
										
										<td height="20" 
											align="center" width="30%">Name of Student :  <bean:write name="loginform" property="internalMarks.studentName" /></td>

										</tr>

									</table>
									</td>
								</tr>
								<tr>
								</tr>
							</table>

							</td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>
						
						
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="0" style="border: 1px solid black; " rules="all">
								<tr>

									<td >
									<div align="center">Sl.No</div>
									</td>
									<td align="center">Subject Code</td>
									<td height="25" align="center">Subject Name</td>
									
									<logic:iterate  property="examList" name="loginform" id="examid">
									<td align="center">
									<bean:write name="examid" property="examName"/>
									</td>
									</logic:iterate>
									<td height="25" align="center">Total Theory</td>	
								</tr>
								
								<logic:notEmpty name="loginform" property="internalMarks.internalSubjectMap">
										<logic:iterate id="map" name="loginform" property="internalMarks.internalSubjectMap" indexId="count">
								
								<logic:notEmpty name="map" property="value">
									 		
								<c:choose>
								<c:when test="${count%2==0}">
								<tr>
								</c:when>
								<c:otherwise>
								<tr >
								</c:otherwise>
								</c:choose>
										<td height="35" width="5%">
										<div align="center"><c:out value="${count+1}"></c:out></div>
										</td>
										<td height="35" width="30%" align="center"><bean:write name="map" property="value.code" /></td>
										<td height="35" width="15%" align="center"><bean:write	name="map" property="value.subjectName" /></td>

										<logic:iterate property="value.studentMarksList" id="marksTO" name="map"	indexId="count1">
											
											<logic:equal value="true" name="marksTO" property="isTheory">
											<td height="35" width="30%" align="center"><bean:write name="marksTO" property="theoryMarks" /></td>
											</logic:equal>
											<logic:equal value="true" name="marksTO" property="isPractical">
											<td height="35" width="30%" align="center"><bean:write name="marksTO" property="practicalMarks" /></td>
											</logic:equal>
										
										</logic:iterate>
									<td height="35" width="15%" align="center"><bean:write	name="map" property="value.totalMarks" /></td>
									</logic:notEmpty>
									
									</logic:iterate>
									</logic:notEmpty>
							</table>
							</td>
							<td width="5" background="images/right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/st_04.gif" width="5"
								height="5" /></td>
							<td background="images/st_05.gif"></td>
							<td><img src="images/st_06.gif" /></td>
						</tr>
					</table>
					<div align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="46%" height="35">
							<div align="right"></div>
							</td>
							<td width="2%"></td>
							<td width="52%" align="left"><html:button property=""
								styleClass="btnbg" value="Print" onclick="printMarkscard()"></html:button>
							<html:button property="" styleClass="btnbg" value="Close"
								onclick="goToHomePage()"></html:button></td>
						</tr>
					</table>
					</div>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/st_Tright_03_05.gif" height="29"
						width="9"></td>
					<td background="images/st_TcenterD.gif" width="100%"></td>
					<td><img src="images/st_Tright_02.gif" height="29" width="9"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
