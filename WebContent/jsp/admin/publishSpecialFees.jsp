<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<%@page import="java.util.List"%>
<%@page import="com.sun.org.apache.bcel.internal.generic.GOTO"%><script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<!-- attendanceSlipDetails.jsp -->
<script type="text/javascript">


function deleteAttendancePercentageDayRange(id,department){
	deleteConfirm = confirm("Are you sure you want to delete this entry?");
	if(deleteConfirm){
		document.location.href = "PublishSpecialFees.do?method=deleteSpecialFees&id=" +id + "&name=" +department;
	}
}
function editAttendancePercentageDayRange(id) {
	document.location.href = "PublishSpecialFees.do?method=editSpecialFees&id=" +id;
	
	}
function resetFormFields(){	
	
	document.getElementById("classes").value = "";
	document.getElementById("fromDate").value = "";
	document.getElementById("toDate").value = "";
	resetErrMsgs();
	
}
function getClasses(year) {
	getClassesByYear("classMap", year, "classes", updateClasses);		
}
function updateClasses(req) {
	updateOptionsFromMap(req, "classes", "- Select -");
}



function getAttendanceClasses(classes){
	classes =  document.getElementById("classes");
	if(classes.selectedIndex != -1) {
		var selectedClasses = new Array();
		var count = 0;
		for (var i=0; i<classes.options.length; i++) {
		    if (classes.options[i].selected) {
		    	selectedClasses[count] = classes.options[i].value;
		      count++;
		    }
		 }	
		if(teachers.selectedIndex != -1) {
			getClassesByYearInMuliSelect("classMap", year, "classes", updateClasses);
		} 
	}
}

function moveinid() {
	//alert('hi....from movein');
	var mapFrom = document.getElementById('classes');
	var mapTo = document.getElementById('selsubMap');
	var len = mapTo.length;

	for ( var j = 0; j < len; j++) {
		if (mapTo[j].selected) {
			
			var tmp = mapTo.options[j].text;
			var tmp1 = mapTo.options[j].value;
			mapTo.remove(j);
			len--;
			j--;
			if (j < 0) {
				document.getElementById("moveIn").disabled = true;
				document.getElementById("moveOut").disabled = false;
			}
			if (mapTo.length != 0) {
				document.getElementById("moveOut").disabled = false;
				document.getElementById("moveIn").disabled = false;
			} else
				document.getElementById("moveOut").disabled = false;
			var y = document.createElement('option');
			y.setAttribute("class", "comboBig");
			y.text = tmp;
			y.value = tmp1;
			try {
				mapFrom.add(y, null);
			} catch (ex) {
				mapFrom.add(y);
			}
		}
	}

}

function moveoutid() {
	//alert('hi ....from moveout');
	var mapFrom = document.getElementById('classes');
	var len = mapFrom.length;
	var mapTo = document.getElementById('selsubMap');

	if (mapTo.length == 0) {
		document.getElementById("moveIn").disabled = false;
	}
	for ( var j = 0; j < len; j++) {
		if (mapFrom[j].selected) {

			//listClasses.push(mapFrom[j].value);
			var tmp = mapFrom.options[j].text;
			var tmp1 = mapFrom.options[j].value;
			mapFrom.remove(j);
			len--;
			j--;
			if (j < 0) {
				document.getElementById("moveOut").disabled = true;
				document.getElementById("moveIn").disabled = false;
			}
			if (mapFrom.length <= 0)
				document.getElementById("moveOut").disabled = true;
			else
				document.getElementById("moveOut").disabled = false;
			var y = document.createElement('option');

			y.text = tmp;
			y.value = tmp1;
			y.setAttribute("class", "comboBig");
			try {
				mapTo.add(y, null);
			} catch (ex) {
				mapTo.add(y);
			}
		}
	}
}
function getClassValues() {
	var listClasses = new Array();
	var mapTo1 = document.getElementById('selsubMap');
	var len1 = mapTo1.length;
	for ( var k = 0; k < len1; k++) {
		listClasses.push(mapTo1[k].value);
	}
	document.getElementById("stayClass").value = listClasses;
}

</script>

<html:form action="/PublishSpecialFees" onsubmit="getClassValues()">

<html:hidden property="formName" value="publishSpecialFeesForm" />
<html:hidden property="pageType" value="4" />
<html:hidden property="stayClass" styleId="stayClass" />
	
	<html:hidden property="id" styleId="id"/>
	<html:hidden property="dupId" styleId="dupId"/>
	
      <c:choose>
		<c:when test="${department == 'edit'}">
		<html:hidden property="method" styleId="method" value="updateSpecialFees" />
		</c:when>
		<c:otherwise>
		<html:hidden property="method" styleId="method" value="addSpecialFees" />
		</c:otherwise>
	 </c:choose>

	
<table width="98%" border="0">
  <tr>
    <td><span class="heading">Publish Special Fees</span></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader">Publish Special Fees</strong></div></td>
        <td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
      <tr>
        <td height="122" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news">
        
              <table width="100%" height="185"  border="0" cellpadding="0" cellspacing="0">
                 <tr>
               	    <td height="20" colspan="6" align="left">
               	    <div align="right" style="color:red"> <span class='MandatoryMark'>* Mandatory fields</span></div>
               	    <div id="err" style="color:red;font-family:arial;font-size:11px;"></div>
               	    <div id="errorMessage">
                       <FONT color="red"><html:errors/></FONT>
                       <FONT color="green">
							<html:messages id="msg" property="messages" message="true">
									<c:out value="${msg}" escapeXml="false"></c:out><br>
							</html:messages>
					  </FONT>
					  </div>
               	    </td>
                 </tr>
                 <tr>
                    <td height="35" colspan="6" class="body" >
			        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
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
			                       <td class="row-odd" width="25%"><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.fee.academicyear"/>:</div></td>
				        		  
				        			
				        			
				        			
				        		<td  class="row-even" align="left" width="25%">	
				        		<c:choose>
    								<c:when test="${department != null && department == 'edit'}">
     								 <input type="hidden"  id="tempyear"    name="tempyear" value="<bean:write name="publishSpecialFeesForm" property="year"/>"/>
      									 <html:select disabled="true"  property="year" styleId="academicYear" name="publishSpecialFeesForm" styleClass="combo"  onchange="getClasses(this.value)">
				                           
		                       	   				
		                       	   				<cms:renderAcademicYear></cms:renderAcademicYear>
		                       			   </html:select>
   								 </c:when>
   								 <c:otherwise>
     							 <input type="hidden" id="tempyear"    name="tempyear" value="<bean:write name="publishSpecialFeesForm" property="year"/>"/>
  										 <html:select property="year" styleId="academicYear" name="publishSpecialFeesForm" styleClass="combo"  onchange="getClasses(this.value)">
				                           
		                       	   				
		                       	   				<cms:renderAcademicYear></cms:renderAcademicYear>
		                       			   </html:select>
   								 </c:otherwise>
 								</c:choose>
				        			</td>
				        		
				                  <td class="row-odd" width="25%">
			                 		 <div id="classsdiv" align="right"><span class='MandatoryMark'>*</span>Classes:</div>
					               </td>
					              
											<%--multi select pavani --%>
					                <td class="row-even" width="100%">
					                  <html:select styleId="classes" property="classCodeIdsFrom"  multiple="multiple" size="8"  tabindex="1">
					                  
					                 	    <c:if test="${publishSpecialFeesForm.classMap != null}">
					       		    		<html:optionsCollection property="classMap"  name="publishSpecialFeesForm" label="value" value="key"/>
					       		    		</c:if>
					                  </html:select>
				                  </td>
				                  <td width="49"><c:choose>
												<c:when
													test="${department != null && department == 'edit'}">
												<table border="0">

														<tr>
															<td><input type="button" 
																id="moveOut" value="&gt;&gt;" disabled="disabled"></td>
														</tr>
														<tr>
															&nbsp;&nbsp;&nbsp;
															<td><input type="button" value="<<" id="moveIn"  disabled="disabled"></td>
														</tr>
													</table>
                                                       
                                                    <script type="text/javascript" language="javascript">
			
                                                  document.getElementById("examName").disabled=true;
                                                  document.getElementById("examType").disabled=true;
                                                  document.getElementById("classes").disabled=true;
                                                  
                                                  </script>

													<html:hidden property="examName"/>
												<html:hidden property="examType"/>
												
                                                     
												</c:when>
												<c:otherwise>
                                    		    <script type="text/javascript" language="javascript">
		
                                                  document.getElementById("examName").disabled=false;
                                                  document.getElementById("examType").disabled=false;
                                                  document.getElementById("classes").disabled=false;
                                                 
                                                  </script>
													<table border="0">

														<tr>
															<td><input type="button" onClick="moveoutid()"
																id="moveOut" value="&gt;&gt;"></td>
														</tr>
														<tr>
															&nbsp;&nbsp;&nbsp;
															<td><input type="button" value="<<" id="moveIn" onclick="moveinid()"></td>
														</tr>
													</table>
												</c:otherwise>
											</c:choose></td>
											<td width="120"><label> <nested:select
												property="classCodeIdsTo" styleId="selsubMap"
												styleClass="body" multiple="multiple" size="8"
												style="width:200px;">



												<logic:notEmpty name="publishSpecialFeesForm"
													property="mapSelectedClass">
													<nested:optionsCollection name="publishSpecialFeesForm"
														property="mapSelectedClass" label="value" value="key"
														styleClass="comboBig" />
												</logic:notEmpty>



											</nested:select> </label></td>
										
									
									
									
									
									
									
									
				                  
				                
				                  
				                </tr>
				                <tr>
				                	<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.attendance.leavemodify.fromdate" />:</div>
									</td>
									<td class="row-even">
									<html:text name="publishSpecialFeesForm" property="fromDate" styleId="fromDate" readonly="true" size="10" maxlength="16"/>
							<script
							language="JavaScript">
							new tcal( {
								// form name
								'formname' :'publishSpecialFeesForm',
								// input name
								'controlname' :'fromDate'
							});
						</script>
									</td>
									<td height="25" class="row-odd">
									<div align="right"><span class="Mandatory">*</span>&nbsp;<bean:message key="knowledgepro.attendance.leavemodify.todate" />:</div>
									</td>
									<td height="25" class="row-even">
									<html:text property="toDate" styleId="toDate"  readonly="true" size="11" maxlength="11"></html:text>
									<script language="JavaScript">
										new tcal( {
											// form name
											'formname' :'publishSpecialFeesForm',
											// input name
											'controlname' :'toDate'
										});
									</script>
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
                   </table>
                   </td>
                 </tr>
                 
                 <tr>
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="right">
							<c:choose>
							<c:when test="${department == 'edit'}">
									<html:submit property="" styleClass="formbutton" value="Update"
										styleId="submitbutton">
									</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit property="" styleClass="formbutton" value="Submit"
										styleId="submitbutton">
								</html:submit>
							</c:otherwise>
							</c:choose>
								</div>
							</td>
							<td width="2%"></td>
							<td width="53%">
							<html:button property=""
							styleClass="formbutton" value="Reset"
							onclick="resetFormFields()"></html:button>
							</td>
						</tr>
					</table>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>	
				
				<tr>
							<td height="25" colspan="6">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0">
								<tr>
									<td><img src="images/01.gif" width="5" height="5"></td>
									<td width="914" background="images/02.gif"></td>
									<td><img src="images/03.gif" width="5" height="5"></td>
								</tr>
								<tr>
									<td width="5" background="images/left.gif"></td>
									<td valign="top">
									<table width="100%" cellspacing="1" cellpadding="2">

										<tr>
											<td width="7%" height="25" class="row-odd">
											<div align="center"><bean:message key="knowledgepro.slno" /></div>
											</td>
											<td width="29%" class="row-odd">
											<div align="center">Academic Year</div>
											</td>
											<td width="29%" class="row-odd">
											<div align="center">Class Name</div>
											</td>
											
											
											
											
											
											<td width="29%" class="row-odd">
											<div align="center">From Date</div>
											</td>
											<td width="29%" class="row-odd">
											<div align="center">To Date</div>
											</td>
											
											<td width="9%" class="row-odd">
											<div align="center"><bean:message key="knowledgepro.edit" /></div>
											</td>
											<td width="8%" class="row-odd">
											<div align="center"><bean:message
										key="knowledgepro.delete" /></div>
											</td>
										</tr>
										<logic:iterate id="attList" property="attendanceList" name="publishSpecialFeesForm"
											 indexId="count">
											 <tr>
											<c:choose>
												<c:when test="${count%2 == 0}">
													<tr class="row-even">
												</c:when>
												<c:otherwise>
													<tr class="row-white">
												</c:otherwise>
											</c:choose>
											<td height="25">
											<div align="center"><c:out value="${count + 1}" /></div>
											</td>
											<td align="center"><bean:write name="attList"
												property="acadamicYear" /></td>
												 <td align="center"><bean:write name="attList"
												property="className" /></td>
												<td align="center"><bean:write name="attList"
												property="fromDate" /></td>
												<td align="center"><bean:write name="attList"
												property="toDate" /></td>
											
											 <td height="25" align="center">
											<div align="center"><img src="images/edit_icon.gif"
													width="16" height="18"
													onclick="editAttendancePercentageDayRange('<bean:write name="attList" property="id" />')" /></div>
											</td>
											<td height="25">
											<div align="center"><img src="images/delete_icon.gif"
													width="16" height="16"
													onclick="deleteAttendancePercentageDayRange('<bean:write name="attList" property="id" />','<bean:write name="attList" property="acadamicYear" />')" /></div>
											</td>
											</tr>
										</logic:iterate>
									</table>
									</td>
									<td width="5" height="30" background="images/right.gif"></td>
								</tr>
								<tr>
									<td height="5"><img src="images/04.gif" width="5"
										height="5"></td>
									<td background="images/05.gif"></td>
									<td><img src="images/06.gif"></td>
								</tr>
							</table>
							</td>
						</tr>		
                 
                
               </table> 
                
<!-- List of Slip Details -->
	          </td>
	          <td width="10" valign="top" background="images/Tright_3_3.gif" class="news"></td>
	         
  </tr>
 
   
  <tr>
					<td><img src="images/Tright_03_05.gif" height="29" width="9"></td>
					<td background="images/TcenterD.gif" width="100%"></td>
					<td><img src="images/Tright_02.gif" height="29" width="9"></td>
				</tr>
</table></td></tr>
</table>
</html:form>
<script type="text/javascript">
var year1 = document.getElementById("tempyear").value;
if(year1.length != 0) {
	document.getElementById("academicYear").value = year1;
}
</script>
