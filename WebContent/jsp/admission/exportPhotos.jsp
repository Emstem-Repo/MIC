<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<head>
<script type="text/javascript">
function getPrograms(year) {
	//getCoursesByAcademicYear("coursesMap", academicYear, "classes", updateCourses);
	getProgramsByAdmitedYear("programMap",year,"programId",updateProgram);
	
}

function updateProgram(req) {
	updateOptionsFromMap(req,"programId","-Select-"); 
}


function exportImages(){
	var admittedYear=document.getElementById("admittedYear").value;
	var programId=document.getElementById("programId").value;
	if(document.getElementById("all").checked){
		document.location.href = "exportImages.do?method=getStreamInfo&admittedYear="+admittedYear+"&programId="+programId+"&admitOrAll=all";
		}else{
			document.location.href = "exportImages.do?method=getStreamInfo&admittedYear="+admittedYear+"&programId="+programId+"&admitOrAll=admitted";
			}
}
</script>	
</head>
<html:form action="/exportPhotos" >
	<html:hidden property="pageType" value="1" />
	<table width="98%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admission" />
			<span class="Bredcrumbs">&gt;&gt; Export Photos&gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="10"><img src="images/Tright_03_01.gif"></td>
					<td width="100%" background="images/Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader">Export Photos</strong></div>
					</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr bgcolor="#FFFFFF">
							<td height="20" colspan="6">
							<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
							<div align="right"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields" /></span></div>
							
							</td>
						</tr>
						<tr>
							<td><img src="images/01.gif" width="5" height="5" /></td>
							<td width="914" background="images/02.gif"></td>
							<td><img src="images/03.gif" width="5" height="5" /></td>
						</tr>
						<tr>
							<td width="5" background="images/left.gif"></td>
							<td width="100%" valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								<tr>
					                <td width="25%" class="row-odd"><div align="right"><span class="Mandatory">*</span>Admitted Year:</div></td>
					                <td width="25%" class="row-even">
					                <html:select property="admittedYear" styleId="admittedYear" styleClass="combo">
											<html:option value=" "><bean:message key="knowledgepro.admin.select"/></html:option>
										<cms:renderAcademicYearForAdmission></cms:renderAcademicYearForAdmission>
										</html:select>
					                </td>
					                <td width="25%" class="row-odd"><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.admission.program"/>:</div></td>
					                <td width="25%" class="row-even">
					                <html:select property="programId" styleClass="combo" styleId="programId"  >
										<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
										<c:if test="${ExportPhotosForm.listProgram != null && ExportPhotosForm.listProgram != ''}">
										<html:optionsCollection name="ExportPhotosForm" property="listProgram" label="value" value="key"/>
										
										</c:if>
										<c:if test="${ExportPhotosForm.listProgram != null && ExportPhotosForm.listProgram != ''}">
                           					<c:set var="listProgram" value="${baseActionForm.collectionMap['programMap']}"/>
	                           		    	 	<c:if test="${listProgram != null}">
	                           		    	 		<html:optionsCollection name="listProgram" label="value" value="key"/>
	                           		    	 	</c:if> 
				                        </c:if>
										</html:select>
									</td>
					                
					            </tr>
								<tr>
					                <td width="25%" class="row-odd"><div align="right"><span class="Mandatory">*</span>Do you want:</div></td>
					                <td width="25%" class="row-even">
					               		<input type="radio" name="admitOrAll" id="all"></input>All students<input type="radio" name="admitOrAll" id="admitted" checked="checked"></input>Admitted students 
					                </td>
					                <td width="25%" class="row-odd">
					                </td>
					                <td width="25%" class="row-even">
									</td>
					                
					            </tr>
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
					<div align="center">
					<table width="100%" height="106" border="0" cellpadding="1"
						cellspacing="2">
						<tr>
							<td width="100%" height="46" class="heading">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="45%" height="35">
									<div align="center"><html:button  property="submit" styleClass="formbutton"  onclick="exportImages()">
										<bean:message key="knowledgepro.submit"/>
									</html:button>&nbsp;&nbsp;<html:button property="" styleClass="formbutton"
										onclick="resetFields()">
										<bean:message key="knowledgepro.admin.reset" />
									</html:button></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td height="10"></td>
						</tr>
					</table>
					</div>
					</td>
					<td width="10" valign="top" background="images/Tright_3_3.gif"
						class="news"></td>
				</tr>
				<tr>
					<td><img src="images/Tright_03_05.gif"></td>
					<td background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
