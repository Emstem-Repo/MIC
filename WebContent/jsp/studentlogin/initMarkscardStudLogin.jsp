<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<link rel="stylesheet" href="jquery/css/ui-lightness/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
<link rel="stylesheet" href="jquery/development-bundle/demos/demos.css">
<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
<script language="JavaScript" src="js/ajax/AjaxUtil.js"></script>
<script language="JavaScript" src="js/ajax/Ajax.js"></script>
<script type="text/javascript">
function goToHomePage() {
	document.location.href = "StudentLoginAction.do?method=returnHomePage";
}

function getHideSuppExam()
{
	document.getElementById("dataRegular").style.display="block";
	document.getElementById("dataRegularLabel").style.display="block";
	document.getElementById("dataSupp").style.display="none";
	document.getElementById("dataSuppLabel").style.display="none";
}
function getHideRegularExam()
{
	document.getElementById("dataRegular").style.display="none";
	document.getElementById("dataRegularLabel").style.display="none";
	 document.getElementById("dataSupp").style.display="block";
	 document.getElementById("dataSuppLabel").style.display="block";
}

function getMarkscard()
{
	var regular=null;
	var supp=null;
	regular=document.getElementById("regularExamId").value;
	supp=document.getElementById("suppExamId").value;
	if(supp !=null && supp!=""){
		document.getElementById("method").value="SupplementaryMarksCard";
		document.loginform.submit();
	}
	else if(regular !=null && regular!=""){
		document.getElementById("method").value="MarksCardDisplay";
		document.loginform.submit();
		
	}
}

</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:form action="/StudentLoginAction">
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method" value="" />
	<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin.studentlogin" />
			<span class="Bredcrumbs">&gt;&gt; Marks card &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/st_Tright_03_01.gif"></td>
					<td width="100%" background="images/st_Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader">Marks card</strong></div>
					</td>
					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						
						<tr>
							<td><img src="images/st_01.gif" width="5" height="5" /></td>
							<td width="914" background="images/st_02.gif"></td>
							<td><img src="images/st_03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
								<tr>
									<td align="left">
	               	    				<div id="err" style="color:red;font-family:arial;font-size:11px;"></div>
	               	   						<div id="errorMessage" style="color:red;font-family:arial;font-size:11px;">
	                       				<p>
										<span id="err"><html:errors/></span> 
										</p>
	                       					<FONT color="green">
											<html:messages id="msg" property="messages" message="true">
											<c:out value="${msg}" escapeXml="false"></c:out><br>
											</html:messages>
						 				 </FONT>
						  			</div>
						  			</td>
						  		</tr>
							</table></td>
							<td width="5" height="30" background="images/st_right.gif"></td>
						</tr>

						<tr>
							<td width="5" background="images/st_left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
							<tr>
								<td colspan="2">
									<table width="100%" cellpadding="2" cellspacing="1">
										
						            <tr height="21px">
									<td height="20" width="50%" class="studentrow-odd" align="left" ><span class="Mandatory">*</span>Exam Type</td>
									<td height="20" width="50%" class="studentrow-even" align="left" class="tdIMGRegular">
									<html:radio property="examType" styleId="examType" value="Regular" name="loginform" onclick="getHideSuppExam()">Regular</html:radio>
									<html:radio property="examType" styleId="examType"  value="Supplementary" name="loginform" onclick="getHideRegularExam()">Improvement</html:radio>
								</td>

								</tr>
								<tr class="data">
									<td height="20" class="studentrow-odd" width="50%"  align="left"><div id="dataRegularLabel">
										<span class="Mandatory">*</span>Regular Exam List	</div></td>
									<td height="25" class="studentrow-even" width="50%" ><div id="dataRegular">
										<html:select property="regularExamId" styleId="regularExamId">
											<html:option value=""><bean:message key="knowledgepro.select"/></html:option>
												<logic:notEmpty property="regularExamList" name="loginform">
											  		<html:optionsCollection property="regularExamList" label="data" value="newExamId" name="loginform"/>
											   </logic:notEmpty>
										 </html:select> </div>
												
							 	    </td>
							 	  
								 </tr>
								 <tr class="data">
									<td height="20" class="studentrow-odd" width="50%" ><div id="dataSuppLabel" align="left">
										<span class="Mandatory">*</span>Improvement Exam List</div></td>
											<td height="25" class="studentrow-even" width="50%" ><div id="dataSupp" align="left">
										<html:select property="suppExamId" styleId="suppExamId">
											<html:option value=""><bean:message key="knowledgepro.select"/></html:option>
												<logic:notEmpty property="suppExamList" name="loginform" >
											  		<html:optionsCollection property="suppExamList" label="data" value="newExamId" name="loginform"/>
											   </logic:notEmpty>
										 </html:select> </div>
												
							 	    </td>
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
							<td height="5"><img src="images/st_04.gif" width="5" height="5" /></td>
							<td background="images/st_05.gif"></td>
							<td><img src="images/st_06.gif" /></td>
						</tr>
					</table>
					<div align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="46%" height="35">
							<div align="right">
							</div>
							</td>
							<td width="2%"></td>
							<td width="52%" align="left">
							<html:button property="" styleClass="btnbg" value="Submit" onclick="getMarkscard()"></html:button>
							<html:button property=""  styleClass="btnbg" value="Close" onclick="goToHomePage()"></html:button>
							
							</td>
						</tr>
					</table>
					</div>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/st_Tright_03_05.gif" height="29" width="9"></td>
					<td background="images/st_TcenterD.gif" width="100%"></td>
					<td><img src="images/st_Tright_02.gif" height="29" width="9"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
<script>
var SelectedRadio=document.getElementById("examType").value;

if(SelectedRadio=="Regular")
{
document.getElementById("dataRegular").style.display="block";
document.getElementById("dataRegularLabel").style.display="block";
document.getElementById("dataSupp").style.display="none";
document.getElementById("dataSuppLabel").style.display="none";
}else
{
document.getElementById("dataRegular").style.display="none";
document.getElementById("dataRegularLabel").style.display="none";
 document.getElementById("dataSupp").style.display="block";
 document.getElementById("dataSuppLabel").style.display="block";
}
</script>