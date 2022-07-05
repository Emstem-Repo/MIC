<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<head>
<title>:: CMS ::</title>
<SCRIPT>
var lastScannedBarCode = "";
var listOfScannedBarCodes = [];

// Global event handler
document.onkeypress = onGlobalKeyPressed;

function onGlobalKeyPressed(e) {
    var charCode = (typeof e.which == "number") ? e.which : e.keyCode;

    if (charCode != 13) { // ascii 13 is return key
        lastScannedBarCode += String.fromCharCode(charCode);
    console.log(lastScannedBarCode);
    } else { // barcode reader indicate code finished with "enter"
        var lastCode = lastScannedBarCode;

        

        lastScannedBarCode = ""; // zero out last code (so we do not keep adding)
        document.getElementById("brcode").value=lastCode;
        console.log(lastCode);
        console.log(document.getElementById("brcode").value);
        document.getElementById("method").value="updateBarCodeList";
        document.falsenumSINOForm.submit();
    }    
}


function deleteSub(courseId) {
	deleteConfirm = confirm("Are you sure you want to delete this entry?"+courseId)
	if (deleteConfirm) {
		/* document.location.href = "falsenumSINO.do?method=updateBarCodeList&deleId="
				+ courseId ; */
		document.getElementById("method").value="updateBarCodeList";
		document.getElementById("deleId").value=courseId;
	    document.falsenumSINOForm.submit();
	}
	document.getElementById("falseBoxId").value=id;
}

function cancelAction() {
	document.location.href = "falsenumSINO.do?method=initFalseBox";
}
</SCRIPT>

</head>


<html:form action="/falsenumSINO" method="POST" enctype="multipart/form-data">
	<html:hidden property="method" styleId="method" value="updateteExaminer" />
	<html:hidden property="formName" value="falsenumSINOForm"/>
	<html:hidden property="pageType" value="2" styleId="pageType" />
	<html:hidden property="barcode" styleId="brcode" />
	<html:hidden property="subjectId" />
	<html:hidden property="examId" />
	<html:hidden property="deleId" styleId="deleId" />									
	<table width="99%" border="0">

		<tr>
			<td><span class="heading"><span class="Bredcrumbs">Exam papers Boxing</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
					<td background="images/Tcenter.gif" class="heading_white">Exam papers Boxing</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9" height="29"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="	images/Tright_03_03.gif"></td>
					<td height="20" class="news">
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					<div align="right" class="mandatoryfield">*Mandatory fields</div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
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
						<%int i=1; %>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								
								<tr>
									<td width="5%" class="row-even" align="center" valign="top">Sl.No</td>
								     <td height="25" width="80%" align="center" class="row-odd">False Number</td>
									<td width="15%" class="row-even" align="center" valign="top">Delete</td>
								
								</tr>
								<logic:notEmpty property="barcodeList" name="falsenumSINOForm">
								<logic:iterate id="val" property="barcodeList" name="falsenumSINOForm">
								<logic:equal value="true" name="val" property="boxDetIsActive">
								<tr>
									<td width="5%" class="row-even" align="center" valign="top"><%=i %></td>
								     <td height="25" align="center" width="80%" class="row-odd"><bean:write name="val"  property="falseNum"/></td>
									<td width="15%" align="center" class="row-even" valign="top">
									<div align="center"><img src="images/delete_icon.gif"
										width="16" height="16" style="cursor:pointer" 
										onclick="deleteSub('<bean:write name="val" property="falseNum" />')">
									</div></td>
								
								</tr>
								<%i++; %>
								</logic:equal>
								</logic:iterate>
								</logic:notEmpty>
								
								
							</table>
							</td>


							<td width="5" background="images/right.gif"></td>
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
							<td width="49%" height="35" align="right"><input
								name="Submit7" type="submit" class="formbutton" value="Next" /></td>

							<td width="2%" height="35" align="center">&nbsp;</td>
							<td width="49%" height="35" align="left"><input
								type="button" class="formbutton" value="Cancel"
								onclick="cancelAction()" /></td>
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
					<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
<script type="text/javascript">

/* var year = document.getElementById("tempyear").value;
if(year.length != 0) {
 	document.getElementById("year").value=year;
} */
</script>