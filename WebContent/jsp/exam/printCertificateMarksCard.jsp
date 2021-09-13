<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<style>
	.heading1{
		font-family: Verdana, Arial, Helvetica, sans-serif;
	    font-size: 15px;
	    font-weight: normal;
	    color: #000000;
	    background-color: #FFFFFF;
	    font-weight: bold;
	}
	
	.heading2{
		font-family: Verdana, Arial, Helvetica, sans-serif;
	    font-size: 12px;
	    font-weight: normal;
	    color: #000000;
	    background-color: #FFFFFF;
	    font-weight: bold;
	}
	.streamFont {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	    font-size: 11px;
	    font-weight: normal;
	    color: #000000;
	    background-color: #FFFFFF;
	}
	.sectionFont {
		font-family: Verdana, Arial, Helvetica, sans-serif;
	    font-size: 14px;
	    font-weight: normal;
	    color: #000000;
	    background-color: #FFFFFF;
	    font-weight: bold;
	}
	.labelFont {
		font-family: sans-serif;
		font-size: 14px;
	    font-weight: normal;
	    color: #000000;
	    background-color: #FFFFFF;
	}
</style>
<script language="JavaScript" >
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=returnHomePage";
}
function resetErrorMsgs() {	
	resetFieldAndErrMsgs();
}
function printMarksCard(){
	var url = "StudentLoginAction.do?method=printMarksCard";
	var browserName=navigator.appName; 
		 myRef = window.open(url,"MarksCard","left=20,top=20,width=900,height=900,toolbar=1,resizable=0,scrollbars=1");
}
</script>
<html:form action="/StudentLoginAction" >
	<html:hidden property="method" styleId="method" value="printMarksCard" />	
	<html:hidden property="pageType" value="1" />
	<html:hidden property="formName" value="loginform" />

	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td></td>
			<td align="center"><img src='<%=CMSConstants.LOGO_URL%>' height="100" width="600" /></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="center" class="heading1">(Affiliated to University of Kerala)</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">
			<table>
				<tr>
					<td class="labelFont">Thiruvananthapuram</td>
				</tr>
				<tr>
					<% 
	  					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    				Date date = new Date();
	    				String date1= dateFormat.format(date);
    				%>
					<td class="labelFont">Date : <%= date1%>	</td>
				</tr>
			</table>
			</td>
			<td width="5" background="images/st_right.gif"></td>
		</tr>
		<tr>
			<td></td>
			<td align="center" class="heading1">MEMORANDUM</td>
			<td></td>
		</tr>
		<tr>
			<td width="5" background="images/st_left.gif"></td>
			<td align="center" class="heading1">								
				<c:choose>
					<c:when test="${loginform.consolidateMarksCardTO.courseId == 18 ||
							  		loginform.consolidateMarksCardTO.courseId == 19}">
						First Degree Programme under CSS
					</c:when>
					<c:otherwise>
						First Degree Programme under CBCS System
					</c:otherwise>
				</c:choose>
			</td>
			<td width="5" background="images/st_right.gif"></td>
		</tr>
		<tr height="10px"></tr>
		<tr>
			<td width="5" background="images/st_left.gif"></td>
			<td align="center">
			<table width="100%" border="1" rules="rows"
				style="border-collapse: collapse;">
				<tr height="25px">
					<td class="row-print" width="20%">Name of the programme</td>
					<td class="row-print">:</td>
					<td class="labelFont" width="29%"><bean:write name="loginform" property="consolidateMarksCardTO.courseName" /></td>
					<td class="row-print" style="border-left: solid thin" width="20%">Month & Year of Examination</td>
					<td class="row-print">:</td>
					<td class="labelFont" width="29%">
						<logic:notEqual value="0" name="loginform" property="consolidateMarksCardTO.examMonth">
							<logic:notEqual value="0" name="loginform" property="consolidateMarksCardTO.examYear">
								<bean:write name="loginform" property="consolidateMarksCardTO.examMonth"/>
								<bean:write name="loginform" property="consolidateMarksCardTO.examYear"/>
							</logic:notEqual>
						</logic:notEqual>
					</td>
				</tr>
				<tr height="25px">
					<td class="row-print">Name of Student</td>
					<td class="row-print">:</td>
					<td class="labelFont"><bean:write name="loginform" property="studentName" /></td>
					<td class="row-print" style="border-left: solid thin">Candidate Code</td>
					<td class="row-print">:</td>
					<td class="labelFont"><bean:write name="loginform" property="regNo" /></td>
				</tr>
			</table>
			</td>
			<td width="5" background="images/st_right.gif"></td>
		</tr>
		<tr height="10px"></tr>
		<tr>
			<td width="5" background="images/st_left.gif"></td>
			<td class="heading1" align="center">Provisional Consolidated Statement of Grades</td>
			<td width="5" height="30" background="images/st_right.gif"></td>
		</tr>
		<tr>
			<td></td>
			<td width="100%" valign="top">
				<c:choose>
					<c:when test="${loginform.consolidateMarksCardTO.courseId != '18' && loginform.consolidateMarksCardTO.courseId != '19'}">
						<table width="100%" border="1" rules="cols"
							   style="border-collapse: collapse; border-color: black; background-repeat: no-repeat; background-position: center; padding-bottom: 100px; background-image: url('images/studentLogin/draftConsolidated.png'); background-size: 250px 150px;">
					</c:when>
					<c:otherwise>
						<table width="100%" border="1" rules="cols"
							   style="border-collapse: collapse; border-color: black; background-repeat: no-repeat; background-position: top; padding-bottom: 100px; background-image: url('images/studentLogin/draftConsolidated.png'); background-size: 250px 150px;">
					</c:otherwise>
				</c:choose>
					<tr>
						<td class="labelFont" align="center" width="55%" style="border: solid thin;background-color: transparent;">Courses</td>
						<td class="labelFont" align="center" width="25%" style="border: solid thin;background-color: transparent;">CGPA</td>
						<td class="labelFont" align="center" width="20%" style="border: solid thin;background-color: transparent;">Credit</td>
					</tr>
					<nested:notEmpty name="loginform" property="consolidateMarksCardTO.programmePartMap">
						<nested:iterate id="programmePartSection" name="loginform" property="consolidateMarksCardTO.programmePartMap" indexId="count">											
							<nested:notEmpty name="programmePartSection" property="value">												
								<nested:iterate id="to" name="programmePartSection" property="value" indexId="countInner">
									<nested:equal value="false" property="showStreamForSection">
										<tr>
											<td class="sectionFont" style="background-color: transparent;"><nested:write name="programmePartSection" property="key"/></td>
											<td class="labelFont" align="center" style="background-color: transparent;"><nested:write property="ccpa"/></td>
											<td class="labelFont" align="center" style="background-color: transparent;"><nested:write property="credit"/></td>
										</tr>
										<tr height="7px">
											<td class="sectionFont" style="background-color: transparent;"></td>
											<td class="labelFont" align="center" style="background-color: transparent;"></td>
											<td class="labelFont" align="center" style="background-color: transparent;"></td>
										</tr>														
									</nested:equal>
									<nested:equal value="true" property="showStreamForSection">
										<c:if test="${countInner == 0}">															
											<tr>
												<td class="sectionFont" style="background-color: transparent;"><nested:write name="programmePartSection" property="key"/></td>
												<td class="labelFont" align="center" style="background-color: transparent;"></td>
												<td class="labelFont" align="center" style="background-color: transparent;"></td>
											</tr>
										</c:if>														
										<tr>
											<td class="streamFont" style="background-color: transparent;">&nbsp;<nested:write property="consolidatedSubjectStreamName"/></td>
											<td class="labelFont" align="center" style="background-color: transparent;"><nested:write property="ccpa"/></td>
											<td class="labelFont" align="center" style="background-color: transparent;"><nested:write property="credit"/></td>
										</tr>
										<tr height="7px">
											<td class="sectionFont" style="background-color: transparent;"></td>
											<td class="labelFont" align="center" style="background-color: transparent;"></td>
											<td class="labelFont" align="center" style="background-color: transparent;"></td>
										</tr>														
									</nested:equal>
								</nested:iterate>
							</nested:notEmpty>											
						</nested:iterate>
					</nested:notEmpty>
					<tr height="35px">
						<td style="border-bottom: none;"></td>
						<td class="labelFont" align="center" colspan="2" style="border-bottom: solid thin; border-top: solid thin">
							Semester wise SCPA
						</td>
					</tr>
					<tr>
						<td class="streamFont" valign="top" style="border-bottom: solid thin;">
							For the successful
							completion of each semester, the candidate has to score a minimum
							SCPA of 4
						</td>
						<td colspan="2" style="border-bottom: solid thin;">
							<table rules="cols" width="100%" style="border-collapse: collapse; border-bottom: none;">
								<tr>
									<td class="labelFont" align="center" width="25%" style="border: solid thin;">Semester</td>
									<td class="labelFont" align="center" width="35%" style="border: solid thin;">SCPA</td>
									<td class="labelFont" align="center" width="45%" style="border: solid thin;">Credit</td>
								</tr>
								<nested:notEmpty name="loginform" property="consolidateMarksCardTO.sgpaTO">
									<nested:iterate id="sgpa" name="loginform" property="consolidateMarksCardTO.sgpaTO">
										<tr>
											<td class="labelFont" align="center"><nested:write name="sgpa" property="schemeNo" /></td>
											<td class="labelFont" align="center"><nested:write name="sgpa" property="sgpa" /></td>
											<td class="labelFont" align="center"><nested:write name="sgpa" property="credit" /></td>
										</tr>
									</nested:iterate>
								</nested:notEmpty>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td class="labelFont" align="center">Final Result</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td>
				<table width="100%" border="1" style="border-collapse: collapse;">
					<c:choose>
						<c:when test="${loginform.consolidateMarksCardTO.courseId != '18' && loginform.consolidateMarksCardTO.courseId != '19'}">
							<tr>
								<td class="labelFont" align="center" width="60%">CCPA(S)</td>
								<td class="labelFont" align="center" width="15%">Credit</td>
								<td class="labelFont" align="center" width="25%">Grade</td>
							</tr>
							<tr>
								<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpas"/></td>
								<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpasCredit"/></td>
								<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpasGrade"/></td>
							</tr>
						</c:when>												
					</c:choose>						
					<tr>
						<td class="labelFont" align="center">CCPA</td>
						<td class="labelFont" align="center">Credit</td>
						<td class="labelFont" align="center">Grade</td>
					</tr>
					<tr>
						<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpa" /></td>
						<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpaCredit" /></td>
						<td class="labelFont" align="center"><nested:write name="loginform" property="consolidateMarksCardTO.ccpaGrade" /></td>
					</tr>
				</table>
			</td>
			<td></td>
		</tr>
		<tr height="60px">
			<td width="5" background="images/st_left.gif"></td>
			<td></td>
			<td width="5" background="images/st_right.gif"></td>
		</tr>
		<tr>
			<td width="5" background="images/st_left.gif"></td>
			<td>
				<table width="100%">
					<tr>
						<td width="50%">Controller of Examination</td>
						<td align="right" width="50%">Principal</td>
					</tr>
				</table>
			</td>
			<td width="5" background="images/st_right.gif"></td>
		</tr>		
	</table>

</html:form>
<script>
	window.print();
</script>