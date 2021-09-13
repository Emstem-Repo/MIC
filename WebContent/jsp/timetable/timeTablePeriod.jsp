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
<link rel="stylesheet" type="text/css" href="jquery.confirm/jquery.confirm/jquery.confirm.css" />
    <script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
<script type="text/javascript">
var c=0;
function getActivityBatches(activity,count){
	c=count;
	var classes =  document.getElementById("classId").value;
	var selectedClasses = new Array();
	selectedClasses[0] = classes;
	getBatchesByActivity1("subjectMap",activity,selectedClasses,c,updateBatches);
}


function getBatches(subjectId,count) {
	c=count;
	var classes =  document.getElementById("classId").value;
	var selectedClasses = new Array();
	selectedClasses[0] = classes;
	getBatchesBySubject1("subjectMap",subjectId,selectedClasses,count,updateBatches);
}
function updateBatches(req) {
	updateOptionsFromMap(req,c,"- Select -");
}
function addMore(){
	document.getElementById("method").value="addMoreToPeriodDetail";
}
function deleteTimeTable(count){
	
	document.location.href="timeTableForClass.do?method=removeFromPeriodDetail&deleteCount="+count;
}
function cancelAction(){
	document.location.href="timeTableForClass.do?method=goToMainPage";
}
function getActivityByAttendanceType(value,count) {
	c=count;
	getMandatoryFieldsByAttendanceType("activityMap",value,updateMandatory);
}
function updateMandatory(req) {
	updateOptionsFromMap(req,"activity_"+c,"- Select -");	
}
var dep=0;
function searchDeptWise(deptId,count){
	dep=count;
	var destination = document.getElementById("teacher_"+dep);
	for (x1 = destination.options.length; x1 >=0; x1--) {
	destination.options[x1] = null;
	} 
	getTearchersByDepartmentWise(deptId,updateTeachersMapByDep);
}
function updateTeachersMapByDep(req){
	updateTeachersFromMap(req,"teacher_"+dep);
}

</script>
<html:form action="/timeTableForClass" method="post">
	<html:hidden property="method" styleId="method" value="addTimeTableForaPeriod" />
	<html:hidden property="formName" value="timeTableForClassForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden name="timeTableForClassForm" property="classId" styleId="classId"/>
	<html:hidden name="timeTableForClassForm" property="displayWarning" styleId="displayWarning"/>
	<html:hidden name="timeTableForClassForm" property="displayWarning1" styleId="displayWarning1"/>
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
	      <tr>
	
	        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
	        <td valign="top" class="news">
				<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
					<br>
				</html:messages> </FONT></div></td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	      </tr>
	      
	      
	      <tr>
	        <td valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading">
	        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	        	<tr height="25">
	        		<td class="row-odd" align="right">Class:</td>
	        		<td class="row-even"><bean:write name="timeTableForClassForm" property="className"/>  </td>
	        		<td class="row-odd" align="right">period:</td>
	        		<td class="row-even"><bean:write name="timeTableForClassForm" property="periodName"/>  </td>
	        		<td class="row-odd" align="right">Week:</td>
	        		<td class="row-even"><bean:write name="timeTableForClassForm" property="week"/>  </td>
	        	</tr>
	        </table>
	        </td>
	        <td valign="top" background="images/Tright_3_3.gif" ></td>
	        </tr>
	      <tr>
	        <td valign="top" background="images/Tright_03_03.gif"></td>
	        <td class="heading">
	        &nbsp;
	        </td>
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
	                	<tr class="row-odd">
		                    <td ><bean:message key="knowledgepro.hostel.hostelerdatabase.roomno"/> </td>
		                    <td ><bean:message key="knowledgepro.admin.selectedSubjects"/> </td>
		                    <td ><bean:message key="knowledgepro.attendance.type.colon"/> </td><!--
		                    <td ><bean:message key="knowledgepro.attendance.activityattendence.activitytype"/> </td>
		                    --><td ><bean:message key="knowledgepro.cancelattendance.batches"/> </td>
		                    <td ><bean:message key="knowledgepro.attn.teacherclass.teacher.name"/> </td>
		                    <c:if test="${timeTableForClassForm.deleteSubject}">
		                    <td ><bean:message key="knowledgepro.delete"/> </td>
		                    </c:if>
	                    </tr>
	                    <logic:notEmpty name="timeTableForClassForm" property="subjectList">
	                    	<nested:iterate id="to" name="timeTableForClassForm" property="subjectList" indexId="count">
	                    	<c:if test="${to.isActive}">
								<tr class="row-even">
				                    <td ><nested:text  property="roomNo"></nested:text> </td>
				                    <td>
				                    <%String batch="getBatches(this.value,"+count+")"; 
				                    String activitybatch="getActivityBatches(this.value,"+count+")"; 
				                    String activity="getActivityByAttendanceType(this.value,"+count+")";
				                    String dep="searchDeptWise(this.value,"+count+")";
				                    String activityId="activity_"+count; 
				                    String teacherId="teacher_"+count; 
				                    
				                    %>
				                    <nested:select  property="subjectId" styleClass="combo" onchange="<%=batch %>">
				                    	<html:option value="">select</html:option>
				                    	<html:optionsCollection name="timeTableForClassForm" property="subjectMap" label="value" value="key"/>
				                    </nested:select> </td>
				                    <td ><nested:select  property="attendanceTypeId" styleClass="combo" onchange="<%=activity %>">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="timeTableForClassForm" property="attTypeMap">
				                    	<html:optionsCollection name="timeTableForClassForm" property="attTypeMap" label="value" value="key"/>
				                    	</logic:notEmpty>
				                    </nested:select> </td>
				                    <!--<td ><nested:select  property="activityId" styleId="<%=activityId %>" styleClass="combo" onchange="<%=activitybatch %>">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="to" property="activity">
				                    	<html:optionsCollection name="to" property="activity" label="value" value="key"/>
				                    	</logic:notEmpty>
				                    </nested:select> </td>
				                    --><td ><nested:select  property="batchId" styleId="<%=String.valueOf(count) %>" styleClass="combo">
				                    	<html:option value="">select</html:option>
				                    	<logic:notEmpty name="to" property="batchs">
				                    	<html:optionsCollection name="to" property="batchs" label="value" value="key"/>
				                    	</logic:notEmpty>
				                    </nested:select> </td>
				                    <td >
				                    <table>
				                    <tr><td>
				                    <nested:select property="depId" styleId="depId" styleClass="combo" onchange="<%=dep %>">
										<html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
											<logic:notEmpty name="timeTableForClassForm" property="departmentMap">
												<html:optionsCollection name="timeTableForClassForm" property="departmentMap" label="value" value="key" />
											</logic:notEmpty>
									</nested:select>
				                    </td> </tr>
				                    <tr><td>
				                    	<nested:select  property="userId" style="width:280px;height:80px" styleId="<%=teacherId %>" multiple="multiple">
					                    	<logic:notEmpty name="timeTableForClassForm" property="teachersMap">
					                    	<html:optionsCollection name="to" property="teachersMap" label="value" value="key"/>
					                    	</logic:notEmpty>
				                    </nested:select>
				                    </td> </tr>
				                    </table>
				                     </td>
				                   <c:if test="${timeTableForClassForm.deleteSubject}"> 
				                    <td height="25">
											<div align="center" class="delete"><img src="images/delete_icon.gif"
													width="16" height="16"
													onclick="deleteTimeTable('<bean:write name="to" property="deleteCount" />')" /></div>
											</td>
											</c:if>
			                    </tr>
			                    </c:if>			                    	
	                    	</nested:iterate>
	                    </logic:notEmpty>
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
				<td width="55%"> <html:submit value="Add More" styleClass="formbutton" onclick="addMore()"></html:submit>
				&nbsp;&nbsp;
				<logic:equal value="false" name="timeTableForClassForm" property="deleteBackButton">
				<html:button property="" styleClass="formbutton" value="Back" onclick="cancelAction()"></html:button>
				</logic:equal>
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
var displayWarning=document.getElementById("displayWarning").value;
if(displayWarning!=""){
	$.confirm({
		'message'	: displayWarning+'<br> Are you sure you want to assign this Teacher. ?',
		'buttons'	: {
			'Ok'	: {
				'class'	: 'blue',
				'action': function(){
					$.confirm.hide();
					document.location.href = "timeTableForClass.do?method=addTimeTableForaPeriodAfterConfirmation";
				}
			},
    'Cancel'	:  {
				'class'	: 'gray',
				'action': function(){
					$.confirm.hide();
				}
			}
		}
	});
}
var displayWarning1=document.getElementById("displayWarning1").value;
if(displayWarning1!=""){
	$.confirm({
		'message'	: displayWarning1+'<br> Do you want to assign this Teacher. ?',
		'buttons'	: {
			'Ok'	: {
				'class'	: 'blue',
				'action': function(){
					$.confirm.hide();
					document.location.href = "timeTableForClass.do?method=addTimeTableForaPeriodAfterConfirmation";
				}
			},
    'Cancel'	:  {
				'class'	: 'gray',
				'action': function(){
					$.confirm.hide();
				}
			}
		}
	});
}
</script>