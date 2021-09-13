<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>

<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
td{
	font-size: small;
}
 .title{
 font-weight: bold;
 font-size: 25px;
 
 }
 .title2{
 font-weight: bold;
 font-size: medium;	
 }
 .title3{
 	font-size: 35px;
 	font-weight: bold;
 }
 body{
 	color:black;
 	font-family: "Times New Roman", Times, serif;
 	font-size: 10px;
	
 }
 
.mytable{
	border-collapse: collapse;
	
}
.algn{
   height:25px;

}
.smallCheck {
	font-weight: normal;
 	font-size: small;
}
p{
	font-weight: normal;
 	font-size: small;
 	text-justify: inter-word;
 	 text-align: justify;
}
.office{
	font-size: medium;
    line-height: 35px;
    font-weight: bold;
}
table tr{
	vertical-align: top;
}
</style>
<script language="JavaScript" src="js/calendar_us.js"></script>
<link rel="stylesheet" href="css/calendar.css">
<script language="JavaScript" src="js/admission/admissionform.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>

<html:form action="/admissionFormsOnline" method="post">
	<html:hidden property="formName" value="admissionStatusForm" />
	<html:hidden property="method" styleId="method" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionStatusForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId" value='<bean:write	name="admissionStatusForm" property="applicantDetails.course.id" />' />
	<%int i=1; %>
	<% int num=0; %>
	<table  width="100%" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">
		<tr><td align="right" style="padding-right: 85px">Application No.<%-- <bean:write name="admissionStatusForm" property="applicantDetails.applnNo" /> --%></td></tr>
		<tr>
			<td>
				<table width="100%" border="0"  cellpadding="3" cellspacing="0">
					<tr><td rowspan="6" width="15%" align="right"><img height="120px" alt="none" src="images\formsonlogo.png"></td><td align="left" class="title"  colspan="2">MAR IVANIOS COLLEGE (AUTONOMOUS)</td></tr>
					<tr><td align="left" colspan="2">Mar Ivanios Vidya Nagar, Nalanchira, Thiruvananthapuram - 695 015</td></tr>
					<tr><td align="left" colspan="2">Re-accredited (Fourth Cycle) with 'A+' Grade by NAAC| DST-FIST College | NIRF 2020 - 48th Rank</td>
					<tr><td align="left" colspan="2">CPE (College with Potential for Excellence) Status conferred by UGC| UGC PARAMARSH Mentor College</td>
					<tr><td align="left" colspan="2">(Affiliated to the University of Kerala)<hr></td>
					<tr><td align="center" class="title2" colspan="2">APPLICATION FOR ADMISSION TO THE B.A./B.Sc./B.Com.<br> FIRST DEGREE PROGRAMMES 2021-2022<hr></td>
					<!-- <tr><td align="center" colspan="2">(Make all entries in block letters; tick () appropriate boxes)<hr></td> -->
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					<tr><td width="5%" rowspan="3"><%=++num%>.</td><td width="35%"> Name</td><td><bean:write name="admissionStatusForm" property="applicantDetails.personalData.firstName"/></td></tr>
					<tr><td width="35%">Gender</td><td><bean:write name="admissionStatusForm" property="applicantDetails.personalData.gender"/></td></tr>
					<tr><td width="35%"> Address of Applicant</td><td>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine1"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine2"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCityName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCountryName"/>,&nbsp;
								<br>
			 					Phone : &nbsp;<bean:write name="admissionStatusForm" property="applicantDetails.personalData.mobileNo2"/>&nbsp;
					</td></tr>
					<tr><td><%=++num%>.</td><td>Category to which applicant belongs</td><td>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.subregligionName"/>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Whether child of Ex-service Personnel or Jawan </td><td>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.exservice">
							YES
						</logic:equal>
						<logic:equal value="false" name="admissionStatusForm" property="applicantDetails.personalData.exservice">
							No
						</logic:equal>
					</td></tr>
					<tr><td><%=++num%>.</td><td>a) If Member of N.C.C., details of Certificate holding<br>
												b) Whether Certificate holder of N.S.S<br>
												c)Whether certificate holder of SPC<br>
												d)Whether certificate holder of Scouts & Guides
					</td><td>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.ncccertificate">NCC (<bean:write name="admissionStatusForm" property="applicantDetails.personalData.nccgrades"/> grade)</logic:equal>
						<logic:notEqual value="true" name="admissionStatusForm" property="applicantDetails.personalData.ncccertificate">NA</logic:notEqual>
						<br>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.nsscertificate">Yes </logic:equal>
						<logic:notEqual value="true" name="admissionStatusForm" property="applicantDetails.personalData.nsscertificate">NA</logic:notEqual>
						<br>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.spc">Yes</logic:equal>
						<logic:notEqual value="true" name="admissionStatusForm" property="applicantDetails.personalData.spc">NA</logic:notEqual>
						<br>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.scouts">Yes</logic:equal>
						<logic:notEqual value="true" name="admissionStatusForm" property="applicantDetails.personalData.scouts">No</logic:notEqual>
					</td></tr>
					
					<tr><td><%=++num%>.</td><td>If sports person, of which level</td><td>
						<logic:notEmpty name="admissionStatusForm" property="applicantDetails.personalData.sports" >
						 <bean:write name="admissionStatusForm" property="applicantDetails.personalData.sports"/>
						</logic:notEmpty>
						<logic:empty name="admissionStatusForm" property="applicantDetails.personalData.sports" >
						 NA
						</logic:empty>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Whether differently abled</td><td>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.handicapped">YES, 
	       					<bean:write name="admissionStatusForm" property="applicantDetails.personalData.hadnicappedDescription" />(<bean:write name="admissionStatusForm" property="applicantDetails.personalData.handicapedPercentage" />%)
	    				</logic:equal>
	    				<logic:notEqual value="true" name="admissionStatusForm" property="applicantDetails.personalData.handicapped">
	    					No
	    				</logic:notEqual>
					</td></tr>
					<nested:iterate name="admissionStatusForm" property="applicantDetails.ednQualificationList" id="ednQualList" indexId="count">
					<tr><td><%=++num%>.</td><td>Name of qualifying Examination passed</td><td>
					
						<logic:equal value="Class 12" name="ednQualList" property="docName">Plus Two</logic:equal>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Group selected for Plus Two Course/CBSE/ISC</td><td>
						 <bean:write name="admissionStatusForm" property="applicantDetails.personalData.stream" />
						
					</td></tr>
					<tr><td><%=++num%>.</td><td>Number of times appeared for qualifying examination</td><td>
						<bean:write  name="ednQualList" property="noOfAttempts"></bean:write>
					</td></tr>
					</nested:iterate>
					<tr><td><%=++num%>.</td><td>Religion & Caste <br>If Catholic, state whether Malankara Catholic <!-- If Catholic, state whether --> </td><td>
						<%-- <bean:write name="admissionStatusForm" property="applicantDetails.personalData.casteCategory"/> --%>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.religionName" />,
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.casteCategory" />	
							<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.personalData.isCommunity">	
								<br>Yes
								<br>(Attach copy of Community Certificate from Parish Priest)
							</logic:equal>
							<br>
							
										</td></tr>
					<tr><td><%=++num%>.</td><td>Choice of Subject for Degree</td><td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<b>Core course (Main Subject)</b><br>
					<logic:iterate id="preflist" name="admissionStatusForm" property="applicantDetails.preflist" >
						<bean:write name="preflist" property="prefNo"/>.&nbsp;&nbsp;&nbsp; <bean:write name="preflist" property="coursName"/><br>
						</logic:iterate>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Choice of Diploma/Certificate Courses</td><td>
					<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
					<!-- <b>Core course (Main Subject)</b><br> -->
					<logic:iterate id="preflist" name="admissionStatusForm" property="certicateCoursesPrint" indexId="count">
						<c:out value="${count + 1}"/>&nbsp;&nbsp;.<bean:write name="preflist" property="courseName"/>.&nbsp;&nbsp;&nbsp;<br>
						</logic:iterate>
					</td></tr>
					</table>
					</td></tr>
					
					
					 <%String noofattempt = ""; 
            String total = "";
            String obtained = "";
            String noofattemptpg = ""; 
            String totalpg = "";
            String obtainedpg = "";
          %>
          
          <nested:iterate name="admissionStatusForm" property="applicantDetails.ednQualificationList" id="ednQualList" indexId="count">
          
           <tr>
											    <logic:equal value="Class 12" name="ednQualList" property="docName">
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
         </nested:iterate>
					
					<tr><td colspan="3">
							
							<table width="100%">
	 	<logic:equal value="1" name="admissionStatusForm" property="applicantDetails.programType">
			<tr >
			  <td align="left" style="font-size:small;">
			 
			  <%=++num%>. &nbsp;Summary of marks in the Qualifying Examination</td>
			</tr>
			</logic:equal>
			
			<logic:equal value="2" name="admissionStatusForm" property="applicantDetails.programType">
			<tr >
			  <td align="center" align="center" style="font-size:small; font-weight: bold">Summary of marks in the Qualifying Examination</td>
			</tr>
			</logic:equal>
			
			<tr>
			  <td width="75%">
			    <table cellpadding="3" align="center" width="100%" border="1" style="border:1px solid black; border-collapse: collapse ">
			      <tr style="font-size:small; font-weight: bold">
					<logic:equal value="2" name="admissionStatusForm" property="applicantDetails.programType">
						<td align="center" style="border:1px solid black; border-collapse: collapse ">Subject Group</td>
						<td align="center" style="border:1px solid black; border-collapse: collapse ">Name of Subject</td>
						<td align="center" style="border:1px solid black; border-collapse: collapse ">Marks Secured/Grade Point</td>
						<td align="center" style="border:1px solid black; border-collapse: collapse ">Maximum Marks/Grade Point</td>
						<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.isCbscc">
							<td align="center" style="border:1px solid black; border-collapse: collapse ">Credits</td>
						</logic:equal>
					</logic:equal>
			        <logic:equal value="1" name="admissionStatusForm" property="applicantDetails.programType">
				        <td align="center" style="border:1px solid black; border-collapse: collapse ">Name of Subject</td>
				        <td align="center" style="border:1px solid black; border-collapse: collapse ">Marks Secured</td>
				        <td align="center" style="border:1px solid black; border-collapse: collapse ">Maximum Marks</td>		        
			        </logic:equal>
			       </tr>
			      
			      
			       <logic:equal value="1" name="admissionStatusForm" property="applicantDetails.programType">
			            <logic:iterate id="submrks" property="applicantDetails.pucsubjectmarkto" name="admissionStatusForm">
			            <tr>
			            <td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="subjectName"/></td>
			            <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="obtainedmark" /></td>
			            <td  align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="maxmark" /></td>
			            </tr>
			            </logic:iterate>
			          </logic:equal>
			          
			          <logic:equal value="2" name="admissionStatusForm" property="applicantDetails.programType">
			            <logic:iterate id="map" property="applicantDetails.degMap" name="admissionStatusForm">
			              <logic:notEmpty property="value" name="map">
			                 <logic:iterate id="submrks" property="value" name="map" >
			                   <tr>
								<td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="groupname"/></td>
								<td align="left" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="subjectName"/></td>
								<td align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="obtainedmark" /></td>
								<td align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="maxmark" /></td>
								<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.isCbscc">
									<td align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="submrks" property="credit" /></td>
								</logic:equal>
			                   </tr>
			                 </logic:iterate>
			              </logic:notEmpty>
			          	</logic:iterate>           
			           
			           </logic:equal>
					<tr>
						<logic:equal value="1" name="admissionStatusForm" property="applicantDetails.programType">
							<td style="font-size:small; font-weight: bold" align="right" style="border:1px solid black; border-collapse: collapse ">Total Marks</td>
							<td align="center" style="border:1px solid black; border-collapse: collapse "><%=obtained%></td>
							<td align="center" style="border:1px solid black; border-collapse: collapse "><%=total%></td>
			         	</logic:equal>
			          	<logic:equal value="2" name="admissionStatusForm" property="applicantDetails.programType">
			        		<td colspan="2" style="font-size:small; font-weight: bold" align="right" style="border:1px solid black; border-collapse: collapse ">Total Marks/CGPA(S)/CCPA(S)</td>
			        		<td align="center" style="border:1px solid black; border-collapse: collapse "><%=obtainedpg%></td>
			        		<td align="center" style="border:1px solid black; border-collapse: collapse "><%=totalpg%></td>
			        		<logic:equal value="true" name="admissionStatusForm" property="applicantDetails.isCbscc">
					        	<td align="center" style="border:1px solid black; border-collapse: collapse "><bean:write name="admissionStatusForm" property="applicantDetails.totalCredit"/></td>
					        </logic:equal>
			         </logic:equal>
			         
			      </tr>
			    </table>
			  </td>
			  <td align="center"><div style="border: 1px solid black;height: 150px;width: 125px;" ><br><br><br>Affix Photograph of the candidate(mandatory)</div></td>
			</tr>
	 </table>
					</td></tr>
					<tr>
					<td>
					<p style="page-break-after:always;"> </p>
					<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					
					<tr><td width="5%">
					 
					<%=++num%>.</td><td width="35%">Additional Language selected (for degree)</td><td>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.secondLanguage"/>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Name of Institution last attended</td><td>
						<bean:write name="ednQualList" property="institutionName" /></td>
					</tr>
					<tr><td><%=++num%>.</td><td>Register No. and Year</td><td> 
						<bean:write name="ednQualList" property="previousRegNo" />
					</td></tr>
					<tr><td><%=++num%>.</td><td>Name of Board / University</td><td>
						<bean:write name="ednQualList" property="universityName" />
					</td></tr>
					<tr><td><%=++num%>.</td><td>Percentage of Marks</td><td>
						<bean:write name="ednQualList" property="percentage" />
					</td></tr>
					<tr><td><%=++num%>.</td><td>Name with initials expanded</td><td>
						
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.nameWithInitial"/>
						<logic:empty  name="admissionStatusForm" property="applicantDetails.personalData.nameWithInitial">
							NA
						</logic:empty>
					</td></tr>
					<tr><td><%=++num%>.</td><td>(a) Date of birth<br>(b) Nationality and Mother Tongue, 
					<br>(c) Age.&nbsp;&nbsp;
					<br>(d) Place of birth, Taluk and District. &nbsp;&nbsp;
					</td><td><bean:write name="admissionStatusForm" property="applicantDetails.personalData.dob"/><br>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.citizenship"/>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.motherTonge"/>
						<br><html:hidden name="admissionStatusForm" styleId="dob" property="applicantDetails.personalData.dob"/>
						<script>
						var date1 = new Date();
						var birthDate = document.getElementById("dob").value;
						var dob = new Date(birthDate);
						var years = (date1.getFullYear() - dob.getFullYear());
						console.log(years);
						document.writeln(years);
						</script>
						<br><bean:write name="admissionStatusForm" property="applicantDetails.personalData.placeOfBirth"/>,
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.thaluk"/>,
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.district"/>
					
					
					</td></tr>
					<tr><td><%=++num%>.</td><td>Proficiency in extra-curricular activities with details</td><td>
						NA
					</td></tr>
					<tr><td><%=++num%>.</td><td>a) Name, address, occupation and  annual income of guardian Telephone No.(Land & Mobile)</td><td>
						<table width="100%"><tr><td>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.fatherName"/>
							<logic:notEmpty name="admissionStatusForm"   property="applicantDetails.personalData.fatherOccupation"><bean:write   name="admissionStatusForm"   property="applicantDetails.personalData.fatherOccupation" /></logic:notEmpty>
											<logic:notEmpty name="admissionStatusForm"   property="applicantDetails.personalData.otherOccupationFather"><bean:write   name="admissionStatusForm"   property="applicantDetails.personalData.otherOccupationFather" /></logic:notEmpty></td>
						<td>
						(b)Relationship of the applicant to guardian:Father
						</td></tr>
							
						</table>
					</td></tr>
					<tr><td><%=++num%>.</td><td>Reason for break of study, if any</td><td>
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.reasonFrBreakStudy"/>
						<logic:empty name="admissionStatusForm" property="applicantDetails.personalData.reasonFrBreakStudy">
							NA
						</logic:empty>
					</td></tr>
					<tr><td width="5%"><%=++num%>.</td><td width="35%">a) If any of the parents of the applicant was a student of this College, give details, including present address.</td><td>
						
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.parentOldStudent"/>
						<logic:empty name="admissionStatusForm" property="applicantDetails.personalData.parentOldStudent">
							NA
						</logic:empty>
					</td></tr>
					
					<tr><td>b)</td><td>b)Whether the applicant is related to any former student (s) of this College. If so give details including present address.</td><td>
						
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.relativeOldStudent"/>
						<logic:empty name="admissionStatusForm" property="applicantDetails.personalData.relativeOldStudent">
							NA
						</logic:empty>
					</td></tr>
					<tr><td width="5%"><%=++num%>.</td><td width="35%">Does the applicant receive any military or other concession or scholarship - Specify</td><td>
						
						<bean:write name="admissionStatusForm" property="applicantDetails.personalData.scholarship"/>
						<logic:empty name="admissionStatusForm" property="applicantDetails.personalData.scholarship">
							NA
						</logic:empty>
					</td></tr>
					
					
				</table>
			</td>
		</tr>
		<!-- <tr>
			<td><p style="page-break-after:always;"> </p>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					
					</table>
					</td>
		</tr> -->
		<tr>	
		<td>
			<table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr>
			<td>Place:</td>
			<td></td>
			</tr>
			<tr height="15px"></tr>
			<tr>
			<td>Date:</td>
			<td align="right">Signature of Applicant</td>
			</tr>
			</table>
		</td>
		</tr>
		<tr><td> <div>
			<img alt="none" src="images\formsonpr.jpg" width="100%">
			</div>
		</td></tr>
		<tr>
		<td>
			<table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr><td colspan="2" style="font-weight: bolder;" align="center">
			<p style="page-break-after:always;"> </p>
			<br><br>
				Undertaking to be signed by the student as per the UGC Regulation No. F-1-16/2007 (CPP-II) 13/4/2009<br>
				UNDERTAKING BY THE CANDIDATE/STUDENT
			</td></tr>
			<tr><td colspan="2" align="right">ANNEXURE 1, Part 1</td></tr>
			<tr><td width="10%">1.</td><td>I <bean:write name="admissionStatusForm" property="applicantDetails.personalData.firstName"/>  S/o. D/o. of <bean:write name="admissionStatusForm" property="applicantDetails.titleOfFather"/>. 
											<bean:write name="admissionStatusForm" property="applicantDetails.personalData.fatherName"/> have carefully read and fully understood the law prohibiting ragging and the directions of the Supreme Court and 
											the Central/State Government in this regard.
			</td></tr>
			<tr><td width="10%">2.</td><td>
			I have received a copy of the UGC Regulations on Curbing the Menace of Ragging in Higher Educational Institutions, 2009, and have carefully gone through it.
			</td></tr>
			<tr><td width="10%">3.</td><td>
					I hereby undertake that<br>
						* I will not indulge in any behaviour or act that may come under the definition of ragging.<br>
						* I will not participate in or abet or propagate ragging in any form,<br>
						* I will not hurt anyone physically or psychologically or cause any other harm.
			</td></tr>
			<tr><td width="10%">4.</td><td>
				I hereby agree that if found guilty of any aspect of ragging, I may be punished as per the provisions of the UGC
				Regulations mentioned above and/or per the law in force.
			</td></tr>
			<tr><td width="10%">5.</td><td>
				I hereby affirm that I have not been expelled or debarred from admission by any institution.
			</td></tr>
			<tr height="35px"></tr>
			<tr><td colspan="2">Signed this ...................................... day of ...............................month of ...............................................year</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Signature</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Name : <bean:write name="admissionStatusForm" property="applicantDetails.personalData.firstName"/></td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Address : <bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine1"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine2"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCityName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCountryName"/>,&nbsp;</td></tr>
			</table>
			
		</td>
		
		</tr>
		<tr height="65px"></tr>
		<tr><td><hr> <div>
			<img alt="none" src="images\formsonan.jpg" width="100%">
			</div>
		</td></tr>
		<tr>
		<td>
			<table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr><td style="font-weight: bold;">
			
			</td></tr>
			<tr><td></td></tr>
			</table>
			
		</td>
		</tr>
		<tr>
		<td>
			<table width="100%" border="0"  cellpadding="3" cellspacing="0">
			
			<tr><td colspan="2" style="font-weight: bolder;text-align: center;">
			<p style="page-break-after:always;"> </p>
			<br><br>
			Undertaking to be signed by the Parent / Guardian as per the UGC Regulation No. F-1-16/2007 (CPP-II) 13 April 2009<br>
			UNDERTAKING BY PARENT/GUARDIAN
			</td></tr>
			<tr><td>1.</td>
			<td>I <bean:write name="admissionStatusForm" property="applicantDetails.titleOfFather"/>. 
				<bean:write name="admissionStatusForm" property="applicantDetails.personalData.fatherName"/>
				F/o. M/o. G/o.<bean:write name="admissionStatusForm" property="applicantDetails.personalData.firstName"/>
				have carefully read and fully understood the law prohibiting ragging and the directions of the Supreme Court
			and the Central/State Government in this regard as well as the UGC Regulations on Curbing the Menace of
			Ragging in Higher Education Institutions, 2009.							
			</td></tr>
			<tr><td>2.</td>
			<td>
					I assure you that my son/daughter/ward will not indulge in any act of ragging.			
			</td></tr>
			<tr><td>3.</td>
			<td>
					I hereby agree that if he/she is found guilty of any aspect of ragging, he/she may be punished as per the
				provisions of the UGC Regulations mentioned above and / or as per the law in force.		
			</td></tr>
			<tr height="35px"></tr>
			<tr><td colspan="2">Signed this ...................................... day of ...............................month of ...............................................year</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Signature</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Name : <bean:write name="admissionStatusForm" property="applicantDetails.personalData.fatherName"/></td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Address :<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine1"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressLine2"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCityName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
								<bean:write name="admissionStatusForm" property="applicantDetails.personalData.permanentCountryName"/>,&nbsp;</td></tr></td></tr>
			</table>
			
		</td>
		</tr>
		<tr height="45px"></tr>
		<tr><td> 
		<hr>
		<div>
			<img alt="none" src="images\formsonan2.jpg" width="100%">
			</div>
		</td></tr>
		<tr>
		<tr>
		<td>
			<table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr><td colspan="2" style="font-weight: bolder;text-align: center;">
				<hr>
				For Office Use Only
			</td></tr>
			<tr><td colspan="2">FDP Programme to which admitted : .............................................................................................</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Language Course Selected : ............................................................................................................</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Diploma certificate Course Selected : ............................................................................................</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Admission No : ...............................................................................................................................</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2">Date of Admission : ........................................................................................................................</td></tr>
			<tr height="15px"></tr>
			<tr><td colspan="2" align="right" style="padding-right: 65px">PRINCIPAL</td></tr>
			
			</table>
			
		</td>
		</tr>
	</table>

</html:form>

