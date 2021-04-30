<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested" %>
<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
<script type="text/javascript">
	function disableDown(){
		if(document.getElementById("display")!=null){
			document.getElementById("display").style.display="none";
		}
	}
	function getClasses(year) {
		getClassesByYear("classMap", year, "class", updateClasses);
	}
	function updateClasses(req) {
		updateOptionsFromMap(req, "class", "- Select -");
	}
	function updateTimeTable(finalApprove) {
		if (finalApprove != "" && finalApprove=='on') {
			var args = "method=updateFlagForTimeTable&finalApprove=" + finalApprove;
			var url = "timeTableForClass.do";
			requestOperation(url, args, timeTableUpdate);
			document.getElementById("finalApprove_2").disabled=true;
		}
	}
	function timeTableUpdate(req) {
		var responseObj = req.responseXML.documentElement;
		var value = responseObj.getElementsByTagName("value");
		if(value!=null){
			for ( var I = 0; I < value.length; I++) {
				if(value[I].firstChild!=null){
				var temp = value[I].firstChild.nodeValue;
				document.getElementById("errorMessage").innerHTML =temp;
				}
			}
		}
	}
	function getPeriodDetails(position,count) {
		document.location.href="timeTableForClass.do?method=getPeriodDetail&position="+position+"&count="+count;
	}
	function getTimeTableHistory(){
		document.location.href="timeTableForClass.do?method=getTimeTableHistory";
	}
	function copyPeriods(){
		document.getElementById("method").value="copyPeriodDetails";
		document.timeTableForClassForm.submit();
	}
	function copy(){
		document.location.href="timeTableForClass.do?method=copyPeriods";
	}
</script>
<html:form action="/timeTableForClass" method="post">
	<html:hidden property="method" styleId="method" value="getPeriods" />
	<html:hidden property="formName" value="timeTableForClassForm" />
	<html:hidden property="pageType" value="1" />
	<table width="100%" border="0">
	  <tr>
	   <td><span class="Bredcrumbs"><bean:message key="knowledgepro.timetable.module" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.timetable.for.class"/> &gt;&gt;</span></span></td>
	  </tr>
	
	  <tr>
	    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
	      <tr>
	        <td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
	        <td background="images/Tcenter.gif" class="heading_white" ><bean:message key = "knowledgepro.timetable.for.class"/></td>
	        <td width="10" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
	      </tr>
	      <logic:equal value="true" name="blink" scope="request">
	      <tr>
	
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td valign="top" class="news">
					<div id="info">
						Please Approve The Time Table After Correction
					</div> 
					</td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	      </logic:equal>
	      <tr>
	
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td valign="top" class="news">
				<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT><Font color="red"><bean:write name="timeTableForClassForm" property="errormsg"></bean:write></Font>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
					<br>
				</html:messages> </FONT></div></td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	      <tr>
	        <td valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td align="center"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	
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
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.interview.Year" /></div>
									</td>
									<td  class="row-even"><input type="hidden"
										id="yr" name="yr"
										value='<bean:write name="timeTableForClassForm" property="academicYear"/>' />
									<html:select property="year" styleClass="combo" styleId="academicYear"
										onchange="getClasses(this.value)">
										<html:option value="">
											<bean:message key="knowledgepro.select" />-</html:option>
										<cms:renderAcademicYear></cms:renderAcademicYear>
									</html:select></td>
									<td  height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.attendance.class.col" /></div>
									</td>
									<td class="row-even">
									<nested:select property="classId" styleClass="comboLarge" styleId="class" onchange="disableDown()">
										<html:option value="">
										<bean:message key="knowledgepro.select" />-</html:option>
										<logic:notEmpty  name="timeTableForClassForm" property="classMap">
										<html:optionsCollection name="timeTableForClassForm" property="classMap" label="value" value="key" />
										</logic:notEmpty>
									</nested:select>
									</td>
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
	            </table></td>
	          </tr>
	          <tr>
			<td>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
	            <td width="45%" height="35"><div align="right">
					<html:submit value="Submit" styleClass="formbutton"></html:submit>
				</div>
				</td>
				<td width="2%"></td>
					<td width="53%">
							<html:button property="" styleClass="formbutton" value="Reset"
								onclick="resetFieldsAndErrors()"></html:button>
				</td>
			</tr>
				</table>
			</td>
	          </tr>

	        </table></td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	      <tr>
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading"></td>      
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	<logic:notEmpty name="timeTableForClassForm" property="classTos">
	      <tr>
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading">
	        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="display">
	          <tr>
	            <td ><img src="images/01.gif" width="5" height="5"></td>
	            <td width="914" background="images/02.gif"></td>
	            <td><img src="images/03.gif" width="5" height="5"></td>
	          </tr>
	          <c:if test="${timeTableForClassForm.ttClassId>0}">
	          <tr>
	
	            <td width="5"  background="images/left.gif"></td>
	            <td height="54" valign="top">
	            <table width="100%" cellspacing="1" cellpadding="2">
					           

<tr>
						<tr>
						<span style="color: red;font-size: 120%;">Kindly check the Subject Code and Name. If there is any change in the Subject Code and Name, Email the new Subject Name and Code to</span><span style="color: blue;font-size: 140%"> it.admin@mic.ac.in </span>
						
					</tr> 
					</tr>





	            </table>
	            
	            </td>  
	             <td  background="images/right.gif" width="5" height="54"></td>
	        <!--  <td valign="top" background="images/Tright_3_3.gif" ></td>-->
	      </tr></c:if>
	          <tr>
	
	            <td width="5"  background="images/left.gif"></td>
	            <td height="54" valign="top">
	            <table width="100%" cellspacing="1" cellpadding="2">
	            <tr class="row-odd">
	            	<td>Day </td>
	            	<logic:notEmpty name="timeTableForClassForm" property="periodList">
	            		<logic:iterate id="bo" name="timeTableForClassForm" property="periodList">
	            		<td><bean:write name="bo" property="periodName"/>(<bean:write name="bo" property="startTime"/> - <bean:write name="bo" property="endTime"/>) </td>
	            		</logic:iterate>
	            	</logic:notEmpty>
	            </tr>
	            <logic:iterate id="to" name="timeTableForClassForm" property="classTos">
	            <tr class="row-even" height="50px">
					<td><bean:write name="to" property="week"/> </td>
					<logic:notEmpty name="to" property="timeTablePeriodTos">
	            		<logic:iterate id="pto" name="to" property="timeTablePeriodTos">
	            		<td>
	            		<a href="#" onclick="getPeriodDetails('<bean:write name="to" property="position" />','<bean:write name="pto" property="count" />')">
	            		<bean:write name="pto" property="periodName"/></a> </td>
	            		</logic:iterate>
	            	</logic:notEmpty>						            
	            </tr>
	            </logic:iterate>	
	            
								
	            </table>
	            </td>
	            
	           <td  background="images/right.gif" width="5" height="54"></td>
	          </tr>
	          <tr>
	          <td width="5"  background="images/left.gif"></td>
	          <td height="54" valign="top">
	            
	           
	            <table width="100%"  cellspacing="1" cellpadding="3">
	                    <tr>
								<td>
								<table width="100%"  cellspacing="0" cellpadding="0">
								<tr>
								<td height="35" align="center">
									<span c style="color: red;font-size: 170%;">
									<input type="hidden" id="fa" name="fa"
										value='<bean:write name="timeTableForClassForm" property="finalApprove"/>' />
									Final Approve:
									</span>
									<span style="color: green;font-size: 170%;">
									<nested:radio property="finalApprove" styleId="finalApprove_1" value="on" onclick="updateTimeTable(this.value)"> Yes</nested:radio>
									</span>
									<span c style="color: red;font-size: 170%;">
									<nested:radio property="finalApprove" styleId="finalApprove_2" value="off" onclick="updateTimeTable(this.value)"> No</nested:radio>
									</span>
								
						
								</td>
								</tr>
								<tr>
									<td height="35" align="center">
										<logic:equal value="true" name="timeTableForClassForm" property="ttClassHistoryExists">
						<div align="right"><a href="#" onclick="getTimeTableHistory()">View History</a></div>
						</logic:equal>
									</td>
								</tr>
								</table>
								</td>
								</tr>
								<tr>
								<td>
								<table width="100%"  cellspacing="1" cellpadding="3">
								<logic:equal value="true" name="timeTableForClassForm" property="flag">
								<tr>
								
								<td width="12%"class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.from.day" /></div>
									</td>
									<td width="12%" class="row-even">
									<nested:select  property="fromDay" styleId="day" styleClass="combo">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="timeTableForClassForm" property="classTos">
				                    	<html:optionsCollection name="timeTableForClassForm" property="classTos" label="week" value="position"/>
				                    	</logic:notEmpty>
				                    </nested:select> </td>
				                    <td height="25" width="12%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.from.period" /></div>
									</td>
									<td width="12%" class="row-even">
									<nested:select  property="fromPeriodId" styleId="period" styleClass="combo">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="timeTableForClassForm" property="periodList">
				                    	<html:optionsCollection name="timeTableForClassForm" property="periodList" label="periodName" value="id"/>
				                    	</logic:notEmpty>
				                    </nested:select>
									</td>
									<td height="25" width="12%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.to.day" /></div>
									</td>
									<td width="12%" class="row-even">
									<nested:select  property="toDay" styleId="day" styleClass="combo">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="timeTableForClassForm" property="classTos">
				                    	<html:optionsCollection name="timeTableForClassForm" property="classTos" label="week" value="position"/>
				                    	</logic:notEmpty>
				                    </nested:select> </td>
				                    <td height="25" width="12%" class="row-odd">
									<div align="right"><span class="Mandatory">*</span><bean:message
										key="knowledgepro.to.period" /></div>
									</td>
									<td width="12%" class="row-even">
									<nested:select  property="toPeriodId" styleId="period" styleClass="combo" >
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="timeTableForClassForm" property="periodList">
				                    	<html:optionsCollection name="timeTableForClassForm" property="periodList" label="periodName" value="id"/>
				                    	</logic:notEmpty>
				                    </nested:select>
									</td>
									</tr>
									</logic:equal>
									</table>
									</td>
									</tr>
									<logic:equal value="true" name="timeTableForClassForm" property="flag">
									<tr>
									<td>
									 <table width="100%"  border="0"  cellspacing="0" cellpadding="0">
									 <tr>
									<td  width="70%" height="35"><div align="center">
					                 <html:button property="" value="Submit" styleClass="formbutton" onclick="copyPeriods()" styleId="copyPeriod_1"></html:button>
				                    </div>
				                   </td>
				                   </tr>
				                   </table>	
				                   </td>
				                   </tr>
									</logic:equal>
								</table>	
								
	            
	            </td>
	             <td  background="images/right.gif" width="5" height="54"></td>
	            </tr>
	          
	          
	          <tr>
	            <td height="5"><img src="images/04.gif" width="5" height="5"></td>
	            <td background="images/05.gif"></td>
	            <td><img src="images/06.gif" ></td>
	          </tr>
	
	        </table>   </td>   
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	      </logic:notEmpty>
	      <tr>
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
	        </table></td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	
	      <tr>
	        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
	        <td width="0" background="images/TcenterD.gif"></td>
	        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
	      </tr>
	    </table></td>
	  </tr>
	</table>
</html:form>
<script type="text/javascript">
	var yearId = document.getElementById("yr").value;
	if (yearId != null && yearId.length != 0) {
		document.getElementById("academicYear").value = yearId;
	}
	if(document.getElementById("fa")!=null){
		var fa = document.getElementById("fa").value;
		if (fa!= null && fa.length != 0 && fa=='on') {
			document.getElementById("finalApprove_2").disabled=true;
		}
	}
if(document.getElementById("info")!=null){	
	$("#finalApprove_1").click(function(){
	$("#info").animate({left:"+=10px"}).animate({left:"-5000px"});
	});
	function blink(){
	$("#info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
	}
	blink();
}
if(document.getElementById("info")!=null){	
	$("#copyPeriod_1").click(function(){
	$("#info").animate({left:"+=10px"}).animate({left:"-5000px"});
	});
	function blink(){
	$("#info").fadeOut(800).fadeIn(800).fadeOut(400).fadeIn(400).fadeOut(400).fadeIn(400);
	}
	blink();
}
</script> 	
