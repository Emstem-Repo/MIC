<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<script language="JavaScript" src="js/admission/studentdetails.js"></script>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script>
	function showHideOther(id,destid){
		var selectedVal=document.getElementById(id).value;
		if(selectedVal=="Other"){
			showOther(id,destid);
		}else{
			hideOther(id,destid);
		}
	}
	function showOther(srcid,destid) {
		if(document.getElementById(destid)!=null){
	 		document.getElementById(destid).style.display = "block";
		}
	}
	function hideOther(id,destid) {
		if(document.getElementById(destid)!=null){
			document.getElementById(destid).style.display = "none";
		}
	}
	function setCheckBoxValue(count) {
		var checkBoxClicked = document.getElementById("eGrandStudent" + count);
		if(checkBoxClicked.checked) {
			document.getElementById("dupIsEgrandStudent" + count).value = "true";
		}
		else {
			document.getElementById("dupIsEgrandStudent" + count).value = "false";
		}
	}
</script>

<html:form action="/studentEdit">
	<html:hidden property="method" value="updateBulkApplicationEGrand" />
	<html:hidden property="selectedAppNo" value="" />
	<html:hidden property="selectedYear" value="" />
	<html:hidden property="detailsView" value="false" />
	<html:hidden property="studentId" value="" />
	<html:hidden property="formName" value="studentEditForm" />
	<html:hidden property="pageType" value="2" />
<table width="98%" border="0">
  <tr>
    <td><span class="Bredcrumbs"><bean:message key="knowledgepro.admission" /><span class="Bredcrumbs">&gt;&gt; <bean:message key="admissionForm.studentedit.mainedit.label"/>&gt;&gt;</span></span></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader"><bean:message key="admissionForm.studentedit.mainedit.label"/></strong></div></td>
        <td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
	
      <tr>
        <td height="122" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><div align="center">
            <table width="100%" height="90"  border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="35" valign="top" class="body" ><tr bgcolor="#FFFFFF">
		<td height="20" colspan="2">
		<div align="right"><FONT color="red"> </FONT></div>
		<div id="errorMessage" align="left"><FONT color="red"><html:errors /></FONT>
		<FONT color="green"> <html:messages id="msg"
			property="messages" message="true">
			<c:out value="${msg}" escapeXml="false"></c:out>
			<br>
		</html:messages> </FONT></div>
		</td>
	</tr></td>
              </tr>
              <tr>
                <td height="35" valign="top" class="body" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                      <td ><img src="images/01.gif" width="5" height="5" /></td>
                      <td width="914" background="images/02.gif"></td>
                      <td><img src="images/03.gif" width="5" height="5" /></td>
                    </tr>
                    <tr>
                      <td width="5"  background="images/left.gif"></td>
                      <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
						<tr>
                            <td align="center" class="row-odd" width="4%"><bean:message key="knowledgepro.slno" /></td>
                            <td align="center" class="row-odd" width="25%"><bean:message key="knowledgepro.admin.name"/></td>
                            <td align="center" class="row-odd" width="6%">Class Number</td>
                            <td align="center" class="row-odd" width="10%">Register Number</td>
                            <td align="center" class="row-odd" width="10%">Admission Number</td>
                            <td align="center" class="row-odd" width="10%">Religion</td>
                            <td align="center" class="row-odd" width="10%">Section</td>
                            <td align="center" class="row-odd" width="10%">Caste</td>
                            <td align="center" class="row-odd" width="5%">Egrand student</td>
                           
						</tr>
                          <c:set var="temp" value="0" />
							<nested:iterate id="stdList" name="studentEditForm" property="studentTOList" indexId="count">								
						
						<tr>
                         	 
                          	<% 
								String religionId = "religionId" + count;
                          		String otherReligionId = "otherReligionId" + count;                          		
                          		String casteId = "casteId" + count;
                          		String otherCasteId = "otherCasteId" + count;
                          		String religionOtherSpanId = "religionOtherSpanId" + count;
                          		String casteOtherSpanId = "casteOtherSpanId" + count;
                          		String religionChangeFn = "getSubCaste(this.value, '" + casteId + "'); showHideOther('" + religionId + "', '" + religionOtherSpanId + "');";
                          		String showOthersCaste = "showHideOther('" + casteId + "', '" + casteOtherSpanId + "');";
                          		String eGrandStudent = "eGrandStudent" + count;
                          		String setCheckBoxValue = "setCheckBoxValue(" + count + ")";
                          		String dupIsEgrandStudent = "dupIsEgrandStudent" + count;
							%>
                          
                            <td align="center" width="5%" class="row-even"><c:out value="${count+1}" /></td>                          
                            <td class="row-even">&nbsp;<nested:write property="studentName"/></td>
                            <td align="center" class="row-even"><nested:text property="rollNo"  styleClass="textboxMediam" style="text-align: center;" disabled="true"></nested:text></td>
                            <td align="center" class="row-even"><nested:text property="registerNo"  styleClass="textboxMediam" style="text-align: center;" disabled="true"></nested:text></td>
                            <td align="center" class="row-even"><nested:text property="admissionNumber"  styleClass="textboxMediam" style="text-align: center;" disabled="true"></nested:text></td>                            
							<td align="center" class="row-even">								
								<nested:notEqual value="Other" 
												 property="religion">
									<nested:select property="religion"
											   	   styleClass="combo" 
											   	   styleId="<%=religionId %>"
											       onchange="<%=religionChangeFn %>">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>
										<html:optionsCollection property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<span id="<%=religionOtherSpanId %>" style="display : none;">
										<nested:text property="religionOther"
													 styleClass="textboxMediam" 
													 maxlength="30"													 
													 styleId="<%=otherReligionId %>"></nested:text>
									</span>
								</nested:notEqual>
								
								<nested:equal value="Other" 
											  property="religion">									
									<nested:select property="religion"
												   styleClass="combo" 
												   styleId="<%=religionId %>"
												   onchange="<%=religionChangeFn %>">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>
										<html:optionsCollection property="religions" label="religionName" value="religionId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<span id="<%=religionOtherSpanId %>" style="display : block;">
										<nested:text property="religionOther"
													 styleClass="textboxMediam" 
													 maxlength="30" 													 
													 styleId="<%=otherReligionId %>"></nested:text>
									</span>									
								</nested:equal>								
							</td>
							<td align="center" class="row-even">
								<nested:notEqual value="Other" 
												 property="religionSection">
									<nested:select property="religionSection"
											   	   styleClass="combo">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>
										<html:optionsCollection property="subReligions" label="name" value="id" />
									</nested:select>
								</nested:notEqual>
								
								<nested:equal value="Other" 
											  property="religionSection">
									<nested:select property="religionSection"
											   	   styleClass="combo">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>
										<html:optionsCollection property="subReligions" label="name" value="id" />
									</nested:select>
									<br></br>
									<nested:text property="religionSectionOther"
												 styleClass="textboxMediam" 
												 maxlength="30"></nested:text>
								</nested:equal>
							</td>
							<td align="center" class="row-even">
								<nested:notEqual value="Other" 
												 property="caste">
									<nested:select property="caste"
											   	   styleClass="combo"
											   	   styleId="<%=casteId %>"
											   	   onchange="<%=showOthersCaste %>">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>										
										<html:optionsCollection property="casteList" label="casteName" value="casteId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<span id="<%=casteOtherSpanId %>" style="display : none;">
										<nested:text property="casteOther"
													 styleClass="textboxMediam" 
													 maxlength="30"													 
													 styleId="<%=otherCasteId %>"></nested:text>
									</span>
								</nested:notEqual>
								<nested:equal value="Other" 
											  property="caste">
									<nested:select property="caste"
											   	   styleClass="combo"
											   	   styleId="<%=casteId %>"
											   	   onchange="<%=showOthersCaste %>">
										<option value=""><bean:message key="knowledgepro.admin.select" /></option>										
										<html:optionsCollection property="casteList" label="casteName" value="casteId"/>
										<html:option value="Other">Other</html:option>
									</nested:select>
									<span id="<%=casteOtherSpanId %>" style="display : block;">
										<nested:text property="casteOther"
													 styleClass="textboxMediam" 
													 maxlength="30" 													 
													 styleId="<%=otherCasteId %>"></nested:text>
									</span>
								</nested:equal>
							</td>
							<td align="center" class="row-even">
								<nested:equal value="true" property="dupIsEgrandStudent">
                               		<input type="checkbox" name="studentTOList[<c:out value='${count}'/>].isEgrandStudent" value="true" id="<%= eGrandStudent%>" onclick="<%= setCheckBoxValue%>" checked="checked"/>
                               	</nested:equal>
                               	<nested:empty property="dupIsEgrandStudent">
                               		<input type="checkbox" name="studentTOList[<c:out value='${count}'/>].isEgrandStudent" value="false" id="<%= eGrandStudent%>" onclick="<%= setCheckBoxValue%>"/>
                               	</nested:empty>
                               	<nested:equal value="false" property="dupIsEgrandStudent">
                               		<input type="checkbox" name="studentTOList[<c:out value='${count}'/>].isEgrandStudent" value="false" id="<%= eGrandStudent%>" onclick="<%= setCheckBoxValue%>"/>
                               	</nested:equal>	
								<!--<nested:equal value="true" property="isEgrandStudent">
									<nested:checkbox property="isEgrandStudent" styleId="<%= eGrandStudent%>" onclick="<%= setCheckBoxValue%>"></nested:checkbox>
								</nested:equal>-->
								
								<nested:hidden property="dupIsEgrandStudent" styleId="<%= dupIsEgrandStudent%>"></nested:hidden>
							</td>
                          </tr>
                         
                          <c:set var="temp" value="0" />
						
						</nested:iterate>
                      </table></td>
                      <td width="5" height="30"  background="images/right.gif"></td>
                    </tr>
                    <tr>
                      <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
                      <td background="images/05.gif"></td>
                      <td><img src="images/06.gif" /></td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td height="10" class="body" ><table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    
                      <td width="1%" height="35"><div align="center">
                      <html:submit styleClass="formbutton" property=""  >Submit</html:submit>
                      <html:submit styleClass="formbutton" property=""  onclick="cancelMe('initBulkStudentEditEGrand')" >Cancel</html:submit>
                      
                      </div>
					  </td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td height="10" class="body" ></td>
              </tr>
            </table>
        </div></td>
        <td width="13" valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="100%" background="images/TcenterD.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>