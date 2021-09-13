<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>

<script type="text/javascript">

function resetFormFields(){	
	document.getElementById("startNo").value=null;
	document.getElementById("programtype").selectedIndex = 0;
	resetOption("program");
	resetOption("course");
	resetOption("semister");
	
}


function getPrograms(programTypeId) {
	getProgramsByType("programMap",programTypeId,"program",updatePrograms);
}

function updatePrograms(req) {
	updateOptionsFromMap(req,"program","- Select -");
}

function getCourses(programId) {
	getCoursesByProgram("coursesMap",programId,"course",updateCourses);
	
}

function updateCourses(req) {
	updateOptionsFromMap(req,"course","- Select -");
}

function getSemisters(year) {
	year = document.getElementById("academicYear").value;
	var courseId = document.getElementById("course").value;
	getSemistersOnYearAndCourse("semistersMap",courseId,"semister",year,updateSemisters);
        
}

function updateSemisters(req){
	updateOptionsFromMap(req,"semister","- Select -");
}

function resetCoursesChilds() {
	resetAcademicYear("academicYear");
	resetOption("semister");

}

function getClassesbySemandCourse() {
	var courseId = document.getElementById("course").value;
	var termno = document.getElementById("semister").value;
	var year = document.getElementById("academicYear").value;
	
	getClassesBySemAndCourse("listClassName", courseId, year, termno, "classes", updateClasses);
}

function updateClasses(req) {
	updateOptionsFromMap(req,"classes","- Select -");
}

function generateMarksCard() {
	document.getElementById("method").value = "generateMarksCard";
	document.marksCardGenerateForm.submit();
}

</script>
<html:form action="/marksCardGenerate">	
	<html:hidden property="formName" value="marksCardGenerateForm" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="method" styleId="method" value="generateMarksCard"/>
	<table width="100%" border="0">
		<tr>
			<td><span class="Bredcrumbs"><bean:message key="knowledgepro.admin"/>
			<span class="Bredcrumbs">&gt;&gt;
			Marks Card No Entry
			 &gt;&gt;</span></span></td>
		</tr>
		<tr>
			<td valign="top">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/Tcenter.gif" class="body"><strong
						class="boxheader"> Marks Card No Entry</strong></td>

					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr>
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<div align="right"><FONT color="red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></FONT></div>
					<div id="errorMessage"><FONT color="red"><html:errors /></FONT>
					<FONT color="green"> <html:messages id="msg"
						property="messages" message="true">
						<c:out value="${msg}" escapeXml="false"></c:out>
						<br>
					</html:messages> </FONT></div>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="41" valign="top" background="images/Tright_03_03.gif"></td>

					<td valign="top" class="news">
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
							
							
							
							
				<tr >
                <td height="25" class="row-odd" ><div align="right"><bean:message key="knowledgepro.admission.programtype"/>:</div></td>
                <td height="25" class="row-even" >
                    <html:select property="programTypeId"  styleId="programtype" styleClass="combo" onchange="getPrograms(this.value)">
                 			<html:option value=""><bean:message key="knowledgepro.admin.select"/> </html:option>
				    			<html:optionsCollection name="marksCardGenerateForm" property="programTypeList" label="programTypeName" value="programTypeId"/>
	     			</html:select> 
                </td>
                <td height="25" class="row-odd" ><div align="right"><bean:message key="knowledgepro.admission.program"/>:</div></td>
                <td height="25" class="row-even" >
			      <html:select property="programId"  styleId="program" styleClass="combo" onchange="getCourses(this.value)">
          		   		<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
        			    	<c:if test="${marksCardGenerateForm.programTypeId != null && marksCardGenerateForm.programTypeId != ''}">
            					<c:set var="programMap" value="${baseActionForm.collectionMap['programMap']}"/>
            		    	 	<c:if test="${programMap != null}">
            		    	 		<html:optionsCollection name="programMap" label="value" value="key"/>
            		    	 	</c:if>	 
            		   		</c:if>
	           		</html:select>	   
				</td>
              </tr>
              <tr >
                <td height="25" class="row-odd" ><div align="right"><bean:message key="knowledgepro.admission.course"/>:</div></td>
                <td class="row-even" >
             <html:select property="courseId"  styleId="course" styleClass="combo" onchange="getSemisters(this.value)">
           				<html:option value=""><bean:message key="knowledgepro.admin.select"/></html:option>
         				   		<c:if test="${marksCardGenerateForm.programId != null && marksCardGenerateForm.programId != ''}">
             						<c:set var="courseMap" value="${baseActionForm.collectionMap['coursesMap']}"/>
             		    			<c:if test="${courseMap != null}">
             		    				<html:optionsCollection name="courseMap" label="value" value="key"/>
             		    			</c:if>	 
             		   			</c:if>
       			   </html:select>
				</td>
                
                <td height="25" class="row-odd" ><div align="right"><bean:message key="knowledgepro.admission.scheme.col" /></div></td>
                <td height="25" class="row-even" >
                 	<html:select property="semister" styleId="semister" styleClass="combo" onchange="getClassesbySemandCourse()">
         	   				 <html:option value="">- Select -</html:option>
           				    	<c:if test="${marksCardGenerateForm.academicYear != null && marksCardGenerateForm.academicYear != ''}">
              						<c:set var="semistersMap" value="${baseActionForm.collectionMap['semistersMap']}"/>
              		    			<c:if test="${semistersMap != null}">
              		    				<html:optionsCollection name="semistersMap" label="value" value="key"/>
              		    			</c:if>	 
              		   			</c:if>
         	 			 </html:select>
				</td>
              </tr>
              <tr >
                
				<td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="admissionForm.studentedit.admyear.label" /></div></td>
				<td height="25" class="row-even" >
					<input type="hidden" id="tempyear" name="tempyear" value="<bean:write name="marksCardGenerateForm" property="academicYear"/>"/>
                 <html:select property="year" styleId="academicYear" styleClass="combo" onchange="getSemisters(this.value)">
  	   				 <html:option value="">- Select -</html:option>
  	   				<cms:renderAcademicYear></cms:renderAcademicYear>
   			   </html:select>
				</td>
				
				<td class="row-odd" valign="top">
									<div align="right"><span class="Mandatory">*</span>Class :</div>
				</td>
							
							<td class="row-even" valign="top">
										<html:select property="classId" name="marksCardGenerateForm" styleClass="combo" styleId="classes" style="width:200px">
									<html:option value="">
											<bean:message key="knowledgepro.admin.select" />
										</html:option>
									 <logic:notEmpty name="marksCardGenerateForm"	property="listClassName">
											<html:optionsCollection property="listClassName" label="value" value="key" />
										</logic:notEmpty>  
									</html:select>
							</td>
				
				</tr>
                
					
				<tr>
								<td class="row-odd" valign="top">
									<div align="right"><span class="Mandatory">*</span>Exam Type:</div>
								</td>
								<td  class="row-even" valign="top" >
									<html:radio property="examType" value="Regular" styleId="examTypId" >Regular</html:radio><br>   
									<html:radio property="examType" value="Supplementary" styleId="suppexamTypId" >Supplementary</html:radio><br>
									<html:radio property="examType" value="Revaluation" styleId="Revaluation" >Revaluation</html:radio><br> 
									<html:radio property="examType" value="Improvement" styleId="consolidatedSuppexamTypId" >Improvement</html:radio><br> 
									 
								</td>
								<td class="row-odd" valign="top">
								</td>
								<td  class="row-even" valign="top" >
									 
								</td>
				</tr>			
							
							
							
							</table>
							</td>
							<td width="5" height="29" background="images/right.gif"></td>
						</tr>

						<tr>
							<td height="5"><img src="images/04.gif" width="5" height="5" /></td>
							<td background="images/05.gif"></td>
							<td><img src="images/06.gif" /></td>
						</tr>
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="35" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="45%" height="35">
							<div align="right">
								<html:submit property="" styleClass="formbutton" value="Submit"	styleId="submitbutton">
								</html:submit>
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
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>

					<td valign="top" class="news">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
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
