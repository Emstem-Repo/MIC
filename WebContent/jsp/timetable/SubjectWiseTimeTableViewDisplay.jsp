<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<script type="text/javascript">
function printTimeTable(){
	var url ="subjectWiseTimeTableView.do?method=printSubjectWiseTimeTable";
	myRef = window.open(url,"collectionLedger","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
}

</script>

<html:form action="/subjectWiseTimeTableView" method="post">
	<html:hidden property="pageType" styleId="pageType" value="2" />
	<html:hidden property="formName" value="SubjectWiseTimeTableViewForm" />
	<html:hidden property="method" styleId="method" value="initSubjectWiseTimeTableView"/>
	<table width="98%" border="0">

		<tr>
			<td>
				<span class="heading">
					<bean:message key="knowledgepro.viewTimeTable"/>
					<span class="Bredcrumbs">
						&gt;&gt;<bean:message key="knowledgepro.viewTimeTable.subjectWise"/>
				 		&gt;&gt;
				 	</span>
				 </span>
			 </td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
        			<td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
       				<td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader"><bean:message key="knowledgepro.viewTimeTable.subjectWise"/></strong></div></td>
        			<td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      			</tr>

				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
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
									<td height="25" colspan="6" class="heading">Subject
									: <bean:write name="SubjectWiseTimeTableViewForm"
										property="subject" /></td>

								</tr>
								<tr>
									<td width="71" height="25" class="row-odd">Week Day</td>
									<nested:iterate id="val" name="SubjectWiseTimeTableViewForm" property="periodList">
										<td class="row-odd"><nested:write name="val" /></td>
									</nested:iterate>
								</tr>
								<nested:iterate id="object" property="subTimeList"
									indexId="count">
									<tr>
									<c:choose>
										<c:when test="${count%2==0}">
											<tr class="row-even">
										</c:when>
										<c:otherwise>
											<tr class="row-white">
										</c:otherwise>
									</c:choose>
									<td height="25"><nested:write name="object"
										property="dayName" /></td>
									<nested:iterate name="object" id="object1"
										property="subMapList">
										<td width="256"><nested:write name="object1" /></td>
									</nested:iterate>

									</tr>
								</nested:iterate>
								
							</table>

							</td>
							<td width="5" height="30" background="images/right.gif"></td>
						</tr>
						<tr>
							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
							<td background="images/05.gif"></td>
							<td><img src="images/06.gif" /></td>
						</tr>

					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="49%" align="right"><input name="Button"
								type="button" class="formbutton" value="Print"
								onClick="printTimeTable();" /></td>

							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left">
							<html:submit  styleClass="formbutton" value="Cancel"/></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading"></td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td class="heading">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif"></td>
				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>

					<td width="0" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>