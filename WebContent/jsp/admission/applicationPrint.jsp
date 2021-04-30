<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>

<style>
 .title{
 font-weight: bold;
 font-size: large;
 
 }
 .title2{
 font-weight: bold;
 font-size: medium;	
 }
 body{
 	color:black;
	
 }
 
.mytable{
	border-collapse: collapse;
	border:1px solid #0000;
	text-align:
	
}
table {
    font-size: 18px;
}

</style>
<SCRIPT type="text/javascript">
	function downloadFile(documentId) {
		document.location.href = "DocumentDownloadAction.do?documentId="
				+ documentId;
	}
	function getSemesterMarkDetails(qualId) {
		var url = "admissionFormSubmit.do?method=viewSemesterMarkPage&editcountID="
				+ qualId;
		myRef = window
				.open(url, "ViewSemesterMarkDetails",
						"left=20,top=20,width=800,height=300,toolbar=1,resizable=0,scrollbars=1");
	}
	function getDetailsMark(qualId) {
		var url = "admissionFormSubmit.do?method=viewDetailMarkPage&editcountID="
				+ qualId;
		myRef = window
				.open(url, "ViewDetailsMark",
						"left=20,top=20,width=800,height=600,toolbar=1,resizable=0,scrollbars=1");
	}
	function detailLateralSubmit() {
		var url = "admissionFormSubmit.do?method=viewLateralEntryPage";
		myRef = window
				.open(url, "ViewLateralDetails",
						"left=20,top=20,width=800,height=600,toolbar=1,resizable=0,scrollbars=1");
	}
	function detailTransferSubmit() {
		var url = "admissionFormSubmit.do?method=viewTransferEntryPage";
		myRef = window
				.open(url, "ViewTransferDetails",
						"left=20,top=20,width=800,height=600,toolbar=1,resizable=0,scrollbars=1");
	}
	function forwardQualifySeparate() {
		var url = "admissionFormSubmit.do?method=forwardQualifyExamPrint";
		myRef = window
				.open(url, "ViewTransferDetails",
						"left=20,top=20,width=800,height=600,toolbar=1,resizable=0,scrollbars=1");
	}
	function printMe() {
		window.print();
	}
	function closeMe() {
		window.close();
	}
</SCRIPT>



<script language="JavaScript" src="js/calendar_us.js"></script>
	<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<html:form action="/AdmissionStatus" method="POST">
	<html:hidden property="method" styleId="method" />
	<html:hidden property="formName" value="admissionFormForm" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionFormForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId" value='<bean:write	name="admissionFormForm" property="applicantDetails.course.id" />' />	
	
	
	<table width="90%" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">		
	 <tr>
		<td align="center">
		  <img width="600" height="100"  src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/LogoServlet") %>' alt="Logo not available">
		</td>
	</tr>
	<tr>
	  <td>
	    <table width="100%">
	      <tr>
	        <td width="60%">
	          <table >
	          <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	          <tr>
	            <td>Subject Applied For</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.preference.firstprefCourseName"/></td>
	           </tr>
	          </logic:equal>
	           <tr>
	            <td>Application No</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.applnNo"/></td>
	           </tr>
	           <tr>
	            <td><bean:write name="admissionFormForm" property="applicantDetails.mode"/> No.</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.journalNo"/></td>
	           </tr>
	           <tr>
	            <td>Date</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.challanDate"/></td>
	           </tr>
	           <tr>
	            <td>Quota</td><td>: </td><td><bean:write name="uniqueIdRegistrationForm" property="applicantDetails.quota"/></td>
	           </tr>
	          </table>
	        </td>
	        
	       
	        
	         <td width="40%" class="title"  >
	          <table width="100%" border="0" cellpadding="3" class="mytable">
	            <tr>
	            <td align="right">
	            
	            <img src='<%= request.getRequestURL().toString().replace(request.getRequestURI().substring(0), request.getContextPath()).concat("/PhotoServlet") %>'  height="150px" width="150px" />
	            </td>
	            
	            </tr>
	            </table>
	        </td>
	     
	      </tr>
	    </table>
	  </td>
	</tr>
	
	<logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	<tr>
	  <td style="font-size:medium; font-weight: bold" align="center" align="center">APPLICATION FOR ADMISSION TO FIRST DEGREE PROGRAMME - <bean:write name="admissionFormForm" property="applicantDetails.appliedYear"/></td>
	</tr>
	</logic:equal>
	
	<logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	<tr>
	  <td style="font-size:medium; font-weight: bold" align="center" align="center">APPLICATION FOR ADMISSION TO POSTGRADUATE PROGRAMME - <bean:write name="admissionFormForm" property="applicantDetails.appliedYear"/></td>
	</tr>
	</logic:equal>

	<tr><td><p></p></td></tr>
	
	<tr>
	 <td>
	 <table width="100%" border="0" cellpadding="3" >
	   <tr height="40%">
	     <td>1.</td><td >Name</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.firstName"></bean:write>&nbsp;
				                    </td><td></td><td></td><td></td>
		 </tr>
	   
	   <tr height="40%">
	     <td>2.</td><td>Gender</td><td>: </td><td><logic:equal name="admissionFormForm" property="applicantDetails.personalData.gender" value="MALE"><bean:message key="admissionForm.studentinfo.sex.male.text"/></logic:equal>
											<logic:equal name="admissionFormForm" property="applicantDetails.personalData.gender" value="FEMALE"><bean:message key="admissionForm.studentinfo.sex.female.text"/></logic:equal>
											<logic:equal name="admissionFormForm" property="applicantDetails.personalData.gender" value="TRANSGENDER"><bean:message key="admissionForm.studentinfo.sex.transgender.text"/></logic:equal></td><td></td><td></td><td></td>
	   </tr>
	   
	   <tr height="40%">
	     <td>3.</td><td>Date of Birth</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.dob"></bean:write></td><td></td><td></td><td></td>
	   </tr>
	   
	   <tr height="40%">
	     <td>4.</td><td>Nationality</td><td>: </td><td><bean:write	name="admissionFormForm" property="applicantDetails.personalData.citizenship" /></td><td></td><td></td><td></td>
	   </tr>
	   
	   <tr height="40%">
	     <td>5.</td><td>Domicile State</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.residentCategoryName" /></td><td></td><td></td><td></td>
	   </tr >
	   
	   <tr height="40%">
	     <td>6.</td><td>Religion</td><td>: </td><td><bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.religionName" /></td><td></td><td></td><td></td>
	   </tr>
	   
	    <tr height="40%">
	     <td>7.</td><td>Caste</td><td>: </td><td><bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.casteCategory" /></td><td></td><td></td><td></td>
	   </tr>
	   
	 	<logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.isCommunity">	   
	    <tr height="40%">
	     <td>8.</td><td>Malakara Syrian Catholics</td><td>: </td><td>YES,
			<logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.isCommunity">
				<bean:write name="admissionFormForm" property="applicantDetails.personalData.parishOthers" /> 
			</logic:equal> 
		</td><td></td><td></td><td></td>
	   </tr></logic:equal>
	   
	   <tr height="40%">
	     <td>9.</td><td>Category</td><td>: </td><td><bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.subregligionName" /></td>
	   </tr>
	   
	   <tr height="40%">
	     <td>10.</td><td>Name of Father/Guardian</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.titleOfFather"/>. 
											<bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherName"></bean:write></td><%--  <td colspan="3">Occupation : 
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation" /></logic:notEmpty>
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather" /></logic:notEmpty></td>--%>
	   </tr>
	   <%--
	   <tr height="40%">
	     <td>10.</td><td>Name of Mother</td><td>: </td><td><nested:write name="admissionFormForm" property="applicantDetails.titleOfMother"/>. 
											<bean:write name="admissionFormForm" property="applicantDetails.personalData.motherName"></bean:write></td><td colspan="3">Occupation : 
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.motherOccupation"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.motherOccupation" /></logic:notEmpty>
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationMother"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationMother" /></logic:notEmpty></td>
	   </tr>
	    
	   <tr height="40%">
	     <td>11.</td><td>Annual Income</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherIncome"></bean:write>,
										</td><td></td><td></td><td></td>
	   </tr>
	   --%>
	   <tr height="40%">
	     <td valign="top">11.</td><td valign="top">Permanent Address</td><td valign="top">: </td><td><nested:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine1"></nested:write>,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine2"></nested:write>,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.permanentCityName"></nested:write>
										</td><td></td><td></td><td></td>
	   </tr>
	   <tr height="40%">
	     <td></td><td></td><td></td><td>
										<bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.permanentStateName" />,
										<bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.permanentCountryName" />,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressZipCode"></nested:write></td><td></td><td></td><td></td>
	   </tr>
	   <%--
	   <tr height="40%">
	     <td valign="top">13.</td><td valign="top">Present Address</td><td valign="top">: </td><td><nested:write name="admissionFormForm"  property="applicantDetails.personalData.currentAddressLine1"></nested:write>,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressLine2"></nested:write>,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.currentCityName"></nested:write>
										</td><td></td><td></td><td></td>
	   </tr>
	    
	    <tr height="40%">
	     <td></td><td></td><td></td><td><bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.currentStateName" />,
										<bean:write
												name="admissionFormForm"
												property="applicantDetails.personalData.currentCountryName" />,
										<nested:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressZipCode"></nested:write></td><td></td><td></td><td></td>
	   </tr>
	   --%>
	   <tr height="40%">
	     <td>12.</td><td>Phone Land</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.landlineNo"/></td>
	     <td colspan="3">Mobile : <bean:write name="admissionFormForm" property="applicantDetails.personalData.mobileNo" /></td>
	   </tr>
	   
	   
	   <tr height="40%">
	     <td>13.</td><td>E - Mail</td><td>: </td><td><nested:write name="admissionFormForm" property="applicantDetails.personalData.email"></nested:write></td><td></td><td></td><td></td>
	   </tr>
	     
	    <tr height="40%">
	     <td>14.</td><td>Dependent of Service/ExService Man </td><td>: </td>	     
	     <td>
	     <logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.exservice">YES</logic:equal>
	     <logic:equal value="false" name="admissionFormForm" property="applicantDetails.personalData.exservice">NO</logic:equal>
	     </td>	     
	     <td></td><td></td><td></td>
	   </tr>
	   
	   <% int num = 14; %>
	    <tr>
	       <td><%=++num%></td><td>Differently abled</td><td>: </td><td>
	   		<logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.handicapped">YES, 
	       	<bean:write name="admissionFormForm" property="applicantDetails.personalData.hadnicappedDescription" />(<bean:write name="admissionFormForm" property="applicantDetails.personalData.handicapedPercentage" />%)
	    	</logic:equal>
	    	<logic:equal value="false" name="admissionFormForm" property="applicantDetails.personalData.handicapped">NO
	    	</logic:equal></td>
	    </tr>	    
	                                                      
	   <logic:notEmpty name="admissionFormForm" property="applicantDetails.personalData.sports" >
	   <tr>
	     <td><%=++num%>.</td><td>Participation in Sports</td><td>: </td><td>YES, <bean:write name="admissionFormForm" property="applicantDetails.personalData.sports"/> (<bean:write name="admissionFormForm" property="applicantDetails.personalData.sportsParticipate"/>), <bean:write name="admissionFormForm" property="applicantDetails.personalData.sportsId"/></td>
	  </tr>
	   </logic:notEmpty>
	   <logic:empty name="admissionFormForm" property="applicantDetails.personalData.sports" >
	   <tr>
	     <td><%=++num%>.</td><td>Participation in Sports</td><td>: </td><td>NO</td>
	   </tr>
	   </logic:empty>
	   <logic:notEmpty name="admissionFormForm" property="applicantDetails.personalData.arts" >
	   <tr>
	     <td><%=++num%>.</td><td>Participation in Cultural Activities</td><td>: </td><td>YES, <bean:write name="admissionFormForm" property="applicantDetails.personalData.arts"/> (<bean:write name="admissionFormForm" property="applicantDetails.personalData.artsParticipate"/>)</td>
	   </tr>
	   </logic:notEmpty>
	   <logic:empty name="admissionFormForm" property="applicantDetails.personalData.arts" >
	     <td><%=++num%>.</td><td>Participation in Cultural Activities</td><td>: </td><td>NO</td>
	   </logic:empty>
	    <tr>
	     <td><%=++num%></td><td>Certifiicates</td><td>: </td><td>
	         <logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.nsscertificate">NSS, </logic:equal>
	         <logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.ncccertificate">NCC (<bean:write name="admissionFormForm" property="applicantDetails.personalData.nccgrades"/> grade)</logic:equal> 
<%--	     <logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.exservice">Ex-Service </logic:equal>--%>
	     </td>
	   </tr>
	   <%--<logic:equal value="true" name="admissionFormForm" property="applicantDetails.personalData.isCommunity">
	   <tr>
	     <td><%=++num%></td><td>Malankara Syrian Catholic</td><td>: </td><td>YES</td>
	   </tr>
	   </logic:equal>
	     
	 --%></table>
	 
	 </td>
	</tr>
	</table>
		
	
<!--	<p style="page-break-after:always;"> </p>-->
	<table width="600" border="0"  cellpadding="3" cellspacing="1" >
	<tr>
	 <td>
	 
	 <table>
	 
	 <tr>
	  <td colspan="7">
	    <table border='1' width="800" height='100' cellpadding="3" style="border-collapse: collapse; border: 1px solid #0000;">
           <tr style="font-size:medium; font-weight: bold">
             <td align="center" rowspan='2' style="border:1px solid black; border-collapse: collapse ">Exam</td>
             <td align="center" rowspan='2' style="border:1px solid black; border-collapse: collapse ">Reg. No.</td>
             <td align="center" rowspan='2' style="border:1px solid black; border-collapse: collapse ">Year</td>
             <td align="center" rowspan='2' style="border:1px solid black; border-collapse: collapse ">Name of Board</td>
             <td align="center" rowspan='2' style="border:1px solid black; border-collapse: collapse ">Name of Institution last attended</td>
             <td align="center" colspan='3' style="border:1px solid black; border-collapse: collapse ">Marks</td>
           </tr>
           <tr style="font-size:medium; font-weight: bold">
             <td align="center" style="border:1px solid black; border-collapse: collapse ">%</td>
             <td align="center" style="border:1px solid black; border-collapse: collapse ">Secured</td>
             <td align="center" style="border:1px solid black; border-collapse: collapse ">Max</td>
          </tr>
          <%String noofattempt = ""; 
            String total = "";
            String obtained = "";
            String noofattemptpg = ""; 
            String totalpg = "";
            String obtainedpg = "";
          %>
          <nested:iterate name="admissionFormForm" property="applicantDetails.ednQualificationList" id="ednQualList" indexId="count">
          
          <tr>
             <td style="border:1px solid black; border-collapse: collapse ">
											    <logic:equal value="Class 12" name="ednQualList" property="docName">Plus Two
											    <bean:define id="noofAtt" name="ednQualList" property="noOfAttempts"></bean:define>
											    <bean:define id="totalm" name="ednQualList" property="totalMarks"></bean:define>
											    <bean:define id="obtainedm" name="ednQualList" property="marksObtained"></bean:define>
											    <%
											    if(noofAtt!=null)
											    {
											    noofattempt =noofAtt.toString(); 
											    }
											    if(totalm!=null)
											    {
											    	total =totalm.toString(); 
											    }
											    if(obtainedm!=null)
											    {
											    	obtained =obtainedm.toString(); 
											    }
											    %>
											    </logic:equal>
											    <logic:equal value="DEG" name="ednQualList" property="docName">Degree
											    <bean:define id="noofAttpg" name="ednQualList" property="noOfAttempts"></bean:define>
											    <bean:define id="totalmpg" name="ednQualList" property="totalMarks"></bean:define>
											    <bean:define id="obtainedmpg" name="ednQualList" property="marksObtained"></bean:define>
											    <%
											    if(noofAttpg!=null)
											    {
											    noofattemptpg =noofAttpg.toString(); 
											    }
											    if(totalmpg!=null)
											    {
											    	totalpg =totalmpg.toString(); 
											    }
											    if(obtainedmpg!=null)
											    {
											    	obtainedpg =obtainedmpg.toString(); 
											    }
											    %>
											    </logic:equal>
											    
												<logic:equal value="Class 10" name="ednQualList" property="docName">X</logic:equal>
												<logic:equal value="PG" name="ednQualList" property="docName">PG</logic:equal>
			</td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList" property="previousRegNo" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList" property="yearPassing" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList"property="universityName" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList"property="institutionName" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList" property="percentage" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList" property="marksObtained" /></td>
             <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="ednQualList" property="totalMarks" /></td>
             
         </tr>
         </nested:iterate>
         
        </table>
	  </td>
	</tr>
	
	 </table>
	 </td>
	</tr>
	
	<tr><td><p style="page-break-after: always"></p></td></tr>
	
	<logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	<tr >
	  <td align="center" align="center" style="font-size:medium; font-weight: bold">Statement of Plus Two Marks</td>
	</tr>
	</logic:equal>
	
	<logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	<tr >
	  <td align="center" align="center" style="font-size:medium; font-weight: bold">Statement of Degree Marks</td>
	</tr>
	</logic:equal>
	
	<tr>
	  <td>
	    <table cellpadding="3" align="center" width="100%" border="1" style="border:1px solid black; border-collapse: collapse ">
	      <tr style="font-size:medium; font-weight: bold">
	        <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	         <td align="center" style="border:1px solid black; border-collapse: collapse ">Subject Group</td>
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Name of Subject</td>
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Marks Secured/Grade Point</td>
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Maximum Marks/Grade Point</td>
	        </logic:equal>
	        <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Name of Subject</td>
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Marks Secured</td>
	        <td align="center" style="border:1px solid black; border-collapse: collapse ">Maximum Marks</td>
	        </logic:equal>
	       </tr>
	      
	      
	       <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	            <logic:iterate id="submrks" property="applicantDetails.pucsubjectmarkto" name="admissionFormForm">
	            <tr>
	            <td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="subjectName"/></td>
	            <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="obtainedmark" /></td>
	            <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="maxmark" /></td>
	            </tr>
	            </logic:iterate>
	          </logic:equal>
	          
	          <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	            <logic:iterate id="map" property="applicantDetails.degMap" name="admissionFormForm">
	              <logic:notEmpty property="value" name="map">
	                 <logic:iterate id="submrks" property="value" name="map" >
	                   <tr>
	                   <td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="groupname"/></td>
	                   <td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="subjectName"/></td>
	                   <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="obtainedmark" /></td>
	                   <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="maxmark" /></td>
	                   </tr>
	                 </logic:iterate>
	              </logic:notEmpty>
	          	</logic:iterate>           
	           
	           </logic:equal>
	      <tr>
	         <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	        <td style="font-size:medium; font-weight: bold" align="right" style="border:1px solid black; border-collapse: collapse ">Total Marks</td><td style="border:1px solid black; border-collapse: collapse "><%=obtained%></td><td style="border:1px solid black; border-collapse: collapse "><%=total%></td>
	         </logic:equal>
	          <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	        <td colspan="2" style="font-size:medium; font-weight: bold" align="right" style="border:1px solid black; border-collapse: collapse ">Total Marks/CGPA</td><td style="border:1px solid black; border-collapse: collapse "><%=obtainedpg%></td><td style="border:1px solid black; border-collapse: collapse "><%=totalpg%></td>
	         </logic:equal>
	         
	      </tr>
	    </table>
	  </td>
	</tr>
	
	
	<tr>
	 <td>
	   <table>
	     
	     <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	     <tr>
	       <td><%=++num%>.</td><td>No of time appeared for qualifying examination</td><td>: </td><td><%=noofattempt %></td>
	     </tr>
		 </logic:equal>
		 
	     <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType">
	     <tr>
	       <td><%=++num%>.</td><td>Number of time appeared for qualifying examination</td><td>: </td><td><%=noofattemptpg %></td>
	     </tr>
		 </logic:equal>

	     <tr>
	       <td><%=++num%>.</td><td>Preference Details</td>
	    </tr>
	   </table>
	    </td>
    </tr>
    <tr>
      <td>
         <table cellpadding="3" width="60%" align="center" border="1" style="border:1px solid black; border-collapse: collapse ">
           <tr style="font-size:medium; font-weight: bold">
             <td align="center" style="border:1px solid black; border-collapse: collapse ">Choice</td><td align="center" style="border:1px solid black; border-collapse: collapse ">Programme</td>
           </tr>
           <logic:iterate id="preflist" name="admissionFormForm" property="applicantDetails.preflist">
               <tr>
                 <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="preflist" property="prefNo"/></td>
                  <td style="border:1px solid black; border-collapse: collapse "><bean:write name="preflist" property="coursName"/></td>
               </tr>
           
           </logic:iterate>
        </table>
      </td>
    </tr>
    
    <tr>
	 <td>
	   <table>
	    <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	     <tr>
	       <td><%=++num%>.</td><td>Second language to be opted for degree</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.secondLanguage" /></td>
	     </tr>
	     </logic:equal>
	     <%--
	     <logic:equal value="1" name="admissionFormForm" property="applicantDetails.programType">
	     <tr>
	       <td><%=++num%></td><td> Add-On Course Opted for</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.addonCourse" /></td>
	    </tr>
	    </logic:equal>
	    --%>
	    <logic:equal value="2" name="admissionFormForm" property="applicantDetails.programType"> 
	     <tr>
	       <td><%=++num%>.</td><td> Course Studied for Degree</td><td>: </td><td><bean:write name="admissionFormForm" property="applicantDetails.personalData.ugcourse" /></td>
	    </tr>
	    </logic:equal>
	   </table>
	    </td>
    </tr>
    </table> 
    
    <table>
    
	   <tr>
	  <td align="center" style="font-size:medium; font-weight: bold"><font style="text-decoration: underline">DECLARATION</font></td>
	</tr>
	
	 <tr>
	  <td align="left">The above information is true to the best of my knowledge and I shall abide by the rules and regulations of the college currently prevailing/Which may be amended from time to time.</td>
	</tr>
	
	      <tr>
	        <td style="font-size:medium; font-weight: bold">
	         Place:
	       </td>
	     </tr>
	       <tr><td><p></p><p></p></td></tr>
	     <tr><td>
	     <table width="100%">
	     <tr style="font-size:medium; font-weight: bold">
	       <td align="left">Date: </td><td align="center">Signature of Parent/Guardian</td><td align="right">Signature of the Candidate</td>
	    </tr>
	    </table>
	    <hr style="size:1px;background-color: black" noshade="noshade"  align="center"></hr>
	    </td></tr>
	    
	 <tr>
	  <td align="center" align="center" style="font-size:medium; font-weight: bold"><font style="text-decoration: underline">FOR OFFICE USE ONLY</font></td>
	</tr>
	 <tr><td><p></p></td></tr>
	  <tr><td><p></p></td></tr>
	<tr>
	  <td>
	    <table width="100%">
	   
	        <tr><td width="30%">FDP Program to which admitted </td><td>: </td><td>..................................................................................................</td> </tr>
	        <tr><td>Language course selected</td><td>: </td><td>..................................................................................................</td></tr>
	        <tr><td>Admission number</td><td>: </td><td>..................................................................................................</td></tr> 
	        <tr><td>Date of admission</td><td>: </td><td>..................................................................................................</td></tr>  
	       </table>
	  </td>
	 </tr>
	<tr><td><p></p><p></p><p></p></td></tr>
	 <tr>
	    <td>
	      <table width="100%">
	        <tr style="font-size:medium; font-weight: bold">
	         <td align="left">Date: </td>
<%--	         <td align="center">Signature of the Officer </td>--%>
	         <td align="right">Principal</td>
	        </tr>
	      </table>
	    </td>
	  </tr>  
	
	</table>
</html:form>
<script type="text/javascript">
	window.print();
</script>
