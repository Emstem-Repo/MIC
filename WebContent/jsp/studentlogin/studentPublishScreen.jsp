<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<script type="text/javaScript" language="javascript">
	function getActivities() {
		document.getElementById("method").value = "initPublish";
		document.loginform.submit();
	}

	function hide(){
		//alert("hii");
    document.getElementById("show").style.display ='none';
    document.getElementById("showview").style.display ='none';
	}
	
	function selectAll(obj) {
	    var value = obj.checked;
	    var inputs = document.getElementsByTagName("input");
	    var inputObj;
	    var checkBoxselectedCount = 0;
	    for(var count1 = 0;count1<inputs.length;count1++) {
			inputObj = inputs[count1];
			var type = inputObj.getAttribute("type");
			if (type == 'checkbox') {
				inputObj.checked = value;
			}
	    }
	}
	function unCheckSelectAll() {
	    var inputs = document.getElementsByTagName("input");
	    var inputObj;
	    var checkBoxOthersSelectedCount = 0;
	    var checkBoxOthersCount = 0;
	    for(var count1 = 0;count1<inputs.length;count1++) {
			inputObj = inputs[count1];
			var type = inputObj.getAttribute("type");
			if (type == 'checkbox' && inputObj.id != "checkAll") {
				checkBoxOthersCount++;
				if(inputObj.checked) {
					checkBoxOthersSelectedCount++;
				}     
			}
	    }
	    if(checkBoxOthersCount != checkBoxOthersSelectedCount) {
	      document.getElementById("checkAll").checked = false;
	    } else {
	      document.getElementById("checkAll").checked = true;
	    }        
	}
</script>
   <html:form action="/StudentLoginAction">
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="pageType" value="3" />
	<html:hidden property="method" styleId="method" value="saveStudentExtensionActivities" />
	<table width="98%" border="0">
		<tr>
			<td style="font-size:12px; font-weight:bold"><span class="Bredcrumbs"><bean:message key="knowledgepro.admin.studentlogin" />
			<span class="Bredcrumbs">&gt;&gt; Extension Activity &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="10"><img src="images/st_Tright_03_01.gif"></td>
						<td width="100%" background="images/st_Tcenter.gif" class="body">
						<div align="left"><strong class="boxheader">Extension Activity</strong></div>
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
							
							<nested:notEmpty property="exList">
								<tr>
									<td width="5" background="images/st_left.gif"></td>
									<td width="100%" valign="top">
										<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
											<tr>
												<td colspan="2">
													<table width="100%" cellpadding="2" cellspacing="1">
														<tr class="row-odd">
															<td height="20" class="studentrow-odd" width="5%" style="font-size:12px; font-weight:bold" >Sl No.</td>
															<td height="20" class="studentrow-odd" width="60%"  style="font-size:12px; font-weight:bold">Student Extension Activity</td>
															<td height="20" class="studentrow-odd" width="15%" align="center" style="font-size:12px; font-weight:bold">
																Select
															</td>
												           	<td height="20" class="studentrow-odd" width="20%"  align="center" style="font-size:12px; font-weight:bold">Preference</td>
											 			</tr>
											 			<nested:iterate property="exList" indexId="count">
															<c:choose>
																<c:when test="${count%2 == 0}">
																	<tr class="studentrow-even">
																</c:when>
																<c:otherwise>
																	<tr class="studentrow-white">
																</c:otherwise>
															</c:choose>
															<%
																String checkBxId="check_"+count;
																String preferenceId = "preference_" + count;
															%>
															<td align="center"><c:out value="${count + 1}" /></td>
														    <td style="font-size:12px; font-weight:bold"><nested:write property="activityName"/></td>
														    <td align="center">
														    	<nested:checkbox property="checked" styleId="<%=checkBxId%>" onclick="unCheckSelectAll()"/>
														    </td>
														    <td align="center">
														    	<nested:select property="preference" styleId="<%=preferenceId %>">
														    		<html:option value="">-Select-</html:option>
														    		<html:option value="1">1</html:option>
														    		<html:option value="2">2</html:option>
														    		<html:option value="3">3</html:option>
														    	</nested:select>
														    </td>
														</nested:iterate>
													</table>
												</td>
											</tr>
										</table>
									</td>
									<td width="5" height="30" background="images/st_right.gif"></td>
								</tr>
								<tr>
									<td width="5" background="images/st_left.gif"></td>
									<td width="100%" valign="top">
										<table width="100%" cellspacing="1" cellpadding="2" class="row-white">
											<tr>							
												<td width="46%" height="35">
													<div align="right"></div>
												</td>
												<td width="2%"></td>
												<td width="52%" align="left">
													<html:submit styleClass="btnbg" styleId="show" onclick="hide()"></html:submit>
													<html:button property=""  styleClass="btnbg" value="Close" styleId="showview" ></html:button>
												</td>	
											</tr>
										</table>
									</td>
								</tr>
							</nested:notEmpty>
						</td>
						<td width="10" valign="top" background="images/Tright_3_3.gif" class="news"></td>
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

