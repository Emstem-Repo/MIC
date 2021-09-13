<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
td{
	font-size: 12px;
}
 .title{
 font-weight: bold;
 font-size: larger;
 
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
	<html:hidden property="formName" value="admissionFormForm" />
	<html:hidden property="method" styleId="method" />
	<input type="hidden" name="applicationId"
		value='<bean:write name="admissionFormForm" property="applicantDetails.applicationId" />' />
	<input type="hidden" name="courseId" value='<bean:write	name="admissionFormForm" property="applicantDetails.course.id" />' />
	<%int i=1; %>
	<% int num = 0; %>
	<table>
   	<tr><td>

	<table  width="100%" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">
		<tr>
			<td>
				<table width="100%" border="0"  cellpadding="3" cellspacing="0">
				<tr><td align="right" colspan="2" >ORIGINAL</td></tr>
					<tr><td align="center" class="title"  colspan="2">MAR IVANIOS COLLEGE (AUTONOMOUS)</td></tr>
					<tr><td align="center" class="title2"  colspan="2">Thiruvananthapuram - 15</td></tr>
					<tr><td align="center" class="title"  colspan="2">Bio-Data</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					<tr><td colspan="4" align="center">For Office Use only</td></tr>
					<tr>
						<td>Admission No. <br><bean:write name="admissionFormForm" property="applicantDetails.admissionNumber"/></td>
						<td>Year of Admission &nbsp;<%-- <bean:write name="admissionFormForm" property="applicantDetails.appliedYear" /> --%>
							<script>
								var year='<bean:write name="admissionFormForm" property="applicantDetails.appliedYear" />'
								var year1=parseInt(year)+1;
								document.writeln(year.toString()+"-"+year1.toString());
								console.log(year1);
							</script>
						</td>
						<td align="center">Programme<br><%-- <bean:write name="admissionFormForm" property="applicantDetails.course.name" /> --%></td>
						<td>Class No.<br></td>
					</tr>
				</table>
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					<tr>
						<td colspan="3">Name of the student: <bean:write name="admissionFormForm" property="applicantDetails.personalData.firstName"/></td>
						<td width="20%" rowspan="4" align="center" valign="middle"><div valign="middle">Affix recent photo of student (mandatory)</div></td>
					</tr>
					<tr>
						<td>Gender : <bean:write name="admissionFormForm" property="applicantDetails.personalData.gender"/></td>
						<td>Date of Birth : <bean:write name="admissionFormForm" property="applicantDetails.personalData.dob"/></td>
						<td>Age : 
						
						<html:hidden styleId="dob" name="admissionFormForm" property="applicantDetails.personalData.dob"/>
							<script>
						var date1 = new Date();
						var birthDate = document.getElementById("dob").value;
						var dob = new Date(birthDate);
						var years = (date1.getFullYear() - dob.getFullYear());
						console.log(years);
						document.writeln(years);
						</script>
						</td>
					</tr>
					<!-- <tr>
						<td colspan="3" align="center">Address of the Student</td>
					</tr> -->
					<tr><td colspan="3">
						<table width="100%" cellpadding="3" cellspacing="0">
							<tr align="center"><td style="border-right: 1px solid black;border-bottom: 1px solid black" width="50%">Permanent Address of the Student</td>
							<td style="border-bottom: 1px solid black" width="50%">Present / Residential Address of the Student</td></tr>
							<tr><td style="border-right: 1px solid black;border-bottom: 1px solid black">
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine1"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine2"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentCityName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentCountryName"/>,&nbsp;
								<br>
			 					Phone : &nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.mobileNo2"/>&nbsp;
							</td><td style="border-bottom: 1px solid black">
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressLine1"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressLine2"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentCityName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentStateName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentCountryName"/>,&nbsp;<br>
								Phone : &nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.mobileNo2"/>&nbsp;
							</td>
							</tr>
							<tr><td style="border-right: 1px solid black;">Religion & Denomination :<bean:write name="admissionFormForm" property="applicantDetails.personalData.religionName"/>,
							<bean:write name="admissionFormForm" property="applicantDetails.personalData.casteCategory"/></td>
							<td>Native place <bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentDistricName"/></td></tr>
						</table>
					</td></tr>
					<tr><td colspan="2">
						Name of Father/Guardian : <bean:write name="admissionFormForm" property="applicantDetails.titleOfFather"/>. 
											<bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherName"/>
					</td><td align="center" style="border-bottom: 1px solid black" rowspan="3">
						Annual Income <br> <br><bean:write name="admissionFormForm" property="applicantDetails.personalData.familyAnnualIncome"/>
					</td>
					</tr>
					<tr><td colspan="2">Occupation : 
						<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation" /></logic:notEmpty>
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather" /></logic:notEmpty>
					</td>
<td align="center" style="border-bottom: 1px solid black" rowspan="3">
						Relationship with the student <br><br> Father
					</td>
</tr>
					<tr><td colspan="2">Present Designation (Specify) : </td></tr>
				</table>
			</td>
		</tr>
		<tr><td >
			<table width="100%" border="1"  cellpadding="3" cellspacing="0">
				<!-- <tr align="center"><td colspan="3">Address of Father/Guardian (Mandatory)</td></tr> -->
				<tr>
					<td align="center" width="35%">Address of Father/Guardian - Residence</td>
					<td align="center" width="35%">Address of Father/Guardian - Office</td>
					<td width="30%">Contact details of Parent/Guardian(mandatory)</td>
				</tr>
				<tr>
					<td ><bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine1"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine2"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentCityName"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressZipCode"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.citizenship"/>,&nbsp;</td>
					<td ></td>
					<td>
						<table width="100%" border="0"  cellpadding="3" cellspacing="0">
							<tr><td>Mobile : <bean:write name="admissionFormForm" property="applicantDetails.personalData.parentMobile"/></td></tr>
							<tr><td>Res :</td></tr>
							<tr><td>Office :</td></tr>
							
							<tr><td>E-mail: <bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherEmail"/></td></tr>
						</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><table width="100%" border="1"  cellpadding="3" cellspacing="0">
				<tr>
				<td align="center">Quota under which admission is given</td>
				<td colspan="2" align="center">Name and address of person recommending for Management Quota</td>
				</tr>
				<tr>
					<td align="center" valign="middle"><%-- <bean:write name="admissionFormForm" property="applicantDetails.quota"/> --%>
						<table width="100%" border="1"  cellpadding="3" cellspacing="0">
							<tr><td style="width: 70%;">1. General Merit</td>
							<td align="center"><!-- <div style="border: 1px solid black;height: 10px;width: 40px"></div> --></td>
							</tr>
							<tr><td>2. Community (Malankara Syrian Catholic)</td>
							<td align="center"></div></td></tr>
							<tr><td>3. Reservation (SC/ST)</td>
							<td align="center"></td></tr>
							<tr><td>4. Management</td>
							<td align="center"></td></tr>
							<tr><td>5. Sports</td>
							<td align="center"></td></tr>
							<tr><td>6. PWD</td>
							<td align="center"></td></tr>
						</table>
					</td>
					<td width="30%" rowspan="6">
						
					</td>
					<td  width="30%" rowspan="6"> Mobile:<br><br>
						Res :<br><br>
						Office : <br>
						
					</td>
					
				</tr>
				
			</table></td>
		</tr>
		<tr>
			<td>
			 
			 <logic:notEmpty name="admissionFormForm" property="applicantDetails.ednQualificationList">
			 	<logic:iterate name="admissionFormForm" property="applicantDetails.ednQualificationList" id="acd">
			 		<table width="100%" border="1"  cellpadding="3" cellspacing="0">
				<tr>
					<td colspan="5" >Details of Academic Records</td>
					<td rowspan="5" width="20%">Extra Curricular Activities</td>
				</tr>
				<tr>
					<td width="15%">Name of Course</td>
					<td width="15%">Name of School / College & University</td>
					<td width="15%">Total Marks/Grade Secured</td>
					<td width="15%">Percentage of marks</td>
					<td width="15%">No. of Chances</td>
				</tr>
				
				<logic:equal value="Class 10" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>SSLC/CBSE/ICSE etc.</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/></td>
					 <td><bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
				<logic:equal value="Class 12" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>CLASS - XII</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/>/<bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="percentage"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
				<logic:equal value="DEG" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>DEGREE</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/></td>
					 <td><bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
			</table>
			 	</logic:iterate>
			 	
			 </logic:notEmpty>
			</td>
		</tr>
		<tr>
			<td><table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr height="40px"></tr>
				<tr>
					<td>Signature of Father/Guardian<br><br><br></td>
					<td align="right">Signature of Student<br><br><br></td>
				</tr>
				<tr>
					<td colspan="2" class="title2" align="center"><i>NB: Father / Guardian should provide his / her contact details including phone number correctly.</i><br><br><br><br></td>
				</tr>
				<tr>
					<td colspan="2" class="title3" align="center">
					<!-- <p style="page-break-after:always;">  --></p>CLASS RECORD</td>
				</tr>
				<tr>
					<td colspan="2" align="center">Remarks, observations and comments by the Principal, HOD/Faculty Advisor on
													the academic performance, general behaviour, attendance and discipline of the student.<hr></td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2">FIRST YEAR</td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2"><hr>SECOND YEAR</td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2"><hr>THIRD YEAR</td>
				</tr>
			</table>
			</td>
		</tr>
			
	</table>
	</td>
	</tr>
	<tr>
	<td>
		<table  width="100%" border="0"  cellpadding="3" cellspacing="0" bordercolor="#E0DFDB">
		<tr>
			<td>
				<table width="100%" border="0"  cellpadding="3" cellspacing="0">
				<tr><td align="right" colspan="2" >DUPLICATE</td></tr>
					<tr><td align="center" class="title"  colspan="2">MAR IVANIOS COLLEGE (AUTONOMOUS)</td></tr>
					<tr><td align="center" class="title2"  colspan="2">Thiruvananthapuram - 15</td></tr>
					<tr><td align="center" class="title"  colspan="2">Bio-Data</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					<tr><td colspan="4" align="center">For Office Use only</td></tr>
					<tr>
						<td>Admission No. <br><bean:write name="admissionFormForm" property="applicantDetails.admissionNumber"/></td>
						<td>Year of Admission &nbsp;<%-- <bean:write name="admissionFormForm" property="applicantDetails.appliedYear" /> --%>
							<script>
								var year='<bean:write name="admissionFormForm" property="applicantDetails.appliedYear" />'
								var year1=parseInt(year)+1;
								document.writeln(year.toString()+"-"+year1.toString());
								console.log(year1);
							</script>
						</td>
						<td align="center">Programme<br><%-- <bean:write name="admissionFormForm" property="applicantDetails.course.name" /> --%></td>
						<td>Class No.<br></td>
					</tr>
				</table>
				<hr>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="1"  cellpadding="3" cellspacing="0">
					<tr>
						<td colspan="3">Name of the student: <bean:write name="admissionFormForm" property="applicantDetails.personalData.firstName"/></td>
						<td width="20%" rowspan="4" align="center" valign="middle"><div valign="middle">Affix recent photo of student (mandatory)</div></td>
					</tr>
					<tr>
						<td>Gender : <bean:write name="admissionFormForm" property="applicantDetails.personalData.gender"/></td>
						<td>Date of Birth : <bean:write name="admissionFormForm" property="applicantDetails.personalData.dob"/></td>
						<td>Age : 
						
						<html:hidden styleId="dob" name="admissionFormForm" property="applicantDetails.personalData.dob"/>
							<script>
						var date1 = new Date();
						var birthDate = document.getElementById("dob").value;
						var dob = new Date(birthDate);
						var years = (date1.getFullYear() - dob.getFullYear());
						console.log(years);
						document.writeln(years);
						</script>
						</td>
					</tr>
					<!-- <tr>
						<td colspan="3" align="center">Address of the Student</td>
					</tr> -->
					<tr><td colspan="3">
						<table width="100%" cellpadding="3" cellspacing="0">
							<tr align="center"><td style="border-right: 1px solid black;border-bottom: 1px solid black" width="50%">Permanent Address of the Student</td>
							<td style="border-bottom: 1px solid black" width="50%">Present / Residential Address of the Student</td></tr>
							<tr><td style="border-right: 1px solid black;border-bottom: 1px solid black">
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine1"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressLine2"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentCityName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentCountryName"/>,&nbsp;
								<br>
			 					Phone : &nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.mobileNo2"/>&nbsp;
							</td><td style="border-bottom: 1px solid black">
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressLine1"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressLine2"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentCityName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentAddressZipCode"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentStateName"/>,&nbsp;
								<bean:write name="admissionFormForm" property="applicantDetails.personalData.currentCountryName"/>,&nbsp;<br>
								Phone : &nbsp;<bean:write name="admissionFormForm" property="applicantDetails.personalData.mobileNo2"/>&nbsp;
							</td>
							</tr>
							<tr><td style="border-right: 1px solid black;">Religion & Denomination :<bean:write name="admissionFormForm" property="applicantDetails.personalData.religionName"/>,
							<bean:write name="admissionFormForm" property="applicantDetails.personalData.casteCategory"/></td>
							<td>Native place <bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentDistricName"/></td></tr>
						</table>
					</td></tr>
					<tr><td colspan="2">
						Name of Father/Guardian : <bean:write name="admissionFormForm" property="applicantDetails.titleOfFather"/>. 
											<bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherName"/>
					</td><td align="center" style="border-bottom: 1px solid black" rowspan="3">
						Annual Income <br> <br><bean:write name="admissionFormForm" property="applicantDetails.personalData.familyAnnualIncome"/>
					</td><!-- <td align="center" style="border-bottom: 1px solid black" rowspan="3">
						Relationship with the student <br><br> Father -->
					</td>
					</tr>
					<tr><td colspan="2">Occupation : 
						<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.fatherOccupation" /></logic:notEmpty>
											<logic:notEmpty name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather"><bean:write   name="admissionFormForm"   property="applicantDetails.personalData.otherOccupationFather" /></logic:notEmpty>
					</td>
<td align="center" style="border-bottom: 1px solid black" rowspan="3">
						Relationship with the student <br><br> Father

</tr>
					<tr><td colspan="2">Present Designation (Specify) : </td></tr>
				</table>
			</td>
		</tr>
		<tr><td >
			<table width="100%" border="1"  cellpadding="3" cellspacing="0">
			<!-- 	<tr align="center"><td colspan="3">Address of Father/Guardian (Mandatory)</td></tr> -->
				<tr>
					<td align="center" width="35%">Address of Father/Guardian - Residence</td>
					<td align="center" width="35%">Address of Father/Guardian - Office</td>
					<td width="30%">Contact details of Parent/Guardian(mandatory)</td>
				</tr>
				<tr>
					<td ><bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine1"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressLine2"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentCityName"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.parentAddressZipCode"/>,&nbsp;<br>
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.permanentStateName"/>,&nbsp;
			<bean:write name="admissionFormForm" property="applicantDetails.personalData.citizenship"/>,&nbsp;</td>
					<td ></td>
					<td>
						<table width="100%" border="0"  cellpadding="3" cellspacing="0">
							<tr><td>Mobile : <%-- <bean:write name="admissionFormForm" property="applicantDetails.personalData.parentMobile"/> --%></td></tr>
							<tr><td>Res :</td></tr>
							<tr><td>Office :</td></tr>
							<!-- <tr><td>Mobile: <bean:write name="admissionFormForm" property="applicantDetails.personalData.parentMobile"/></td></tr> -->
							<tr><td>E-mail: <bean:write name="admissionFormForm" property="applicantDetails.personalData.fatherEmail"/></td></tr>
						</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><table width="100%" border="1"  cellpadding="3" cellspacing="0">
				<tr>
				<td align="center">Quota under which admission is given</td>
				<td colspan="2" align="center">Name and address of person recommending for Management Quota</td>
				</tr>
				<tr>
					<td align="center" valign="middle"><%-- <bean:write name="admissionFormForm" property="applicantDetails.quota"/> --%>
						<table width="100%" border="1"  cellpadding="3" cellspacing="0">
							<tr><td style="width: 70%;">1. General Merit</td>
							<td align="center"><!-- <div style="border: 1px solid black;height: 10px;width: 40px"></div> --></td>
							</tr>
							<tr><td>2. Community (Malankara Syrian Catholic)</td>
							<td align="center"></div></td></tr>
							<tr><td>3. Reservation (SC/ST)</td>
							<td align="center"></td></tr>
							<tr><td>4. Management</td>
							<td align="center"></td></tr>
							<tr><td>5. Sports</td>
							<td align="center"></td></tr>
							<tr><td>6. PWD</td>
							<td align="center"></td></tr>
						</table>
					</td>
					<td width="30%" rowspan="6">
						
					</td>
					<td  width="30%" rowspan="6">Mobile: <br><br>
						Res :<br><br>
						Office : <br>
						
					</td>
					
				</tr>
				
			</table></td>
		</tr>
		<tr>
			<td>
			 
			 <logic:notEmpty name="admissionFormForm" property="applicantDetails.ednQualificationList">
			 	<logic:iterate name="admissionFormForm" property="applicantDetails.ednQualificationList" id="acd">
			 		<table width="100%" border="1"  cellpadding="3" cellspacing="0">
				<tr>
					<td colspan="5" >Details of Academic Records</td>
					<td rowspan="5" width="20%">Extra Curricular Activities</td>
				</tr>
				<tr>
					<td width="15%">Name of Course</td>
					<td width="15%">Name of School / College & University</td>
					<td width="15%">Total Marks/Grade Secured</td>
					<td width="15%">Percentage of marks</td>
					<td width="15%">No. of Chances</td>
				</tr>
				
				<logic:equal value="Class 10" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>SSLC/CBSE/ICSE etc.</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/></td>
					 <td><bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
				<logic:equal value="Class 12" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>CLASS - XII</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/>/<bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="percentage"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
				<logic:equal value="DEG" name="acd" property="docName">
				<tr align="center" valign="middle">
					 <td>DEGREE</td>
					 <td><bean:write name="acd" property="institutionName"/></td>
					 <td><bean:write name="acd" property="marksObtained"/></td>
					 <td><bean:write name="acd" property="totalMarks"/></td>
					 <td><bean:write name="acd" property="noOfAttempts"/></td>
				</tr>
				</logic:equal>
			</table>
			 	</logic:iterate>
			 	
			 </logic:notEmpty>
			</td>
		</tr>
		<tr>
			<td><table width="100%" border="0"  cellpadding="3" cellspacing="0">
			<tr height="40px"></tr>
				<tr>
					<td>Signature of Father/Guardian<br><br><br></td>
					<td align="right">Signature of Student<br><br><br></td>
				</tr>
				<tr>
					<td colspan="2" class="title2" align="center"><i>NB: Father / Guardian should provide his / her contact details including phone number correctly.</i><br><br><br><br></td>
				</tr>
				<tr>
					<td colspan="2" class="title3" align="center">CLASS RECORD</td>
				</tr>
				<tr>
					<td colspan="2" align="center">Remarks, observations and comments by the Principal, HOD/Faculty Advisor on
													the academic performance, general behaviour, attendance and discipline of the student.<hr></td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2">FIRST YEAR</td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2"><hr>SECOND YEAR</td>
				</tr>
				<tr height="300px">
					<td align="center" colspan="2"><hr>THIRD YEAR</td>
				</tr>
			</table>
			</td>
		</tr>
			
	</table>
	</td>
	</tr>
	</table>

</html:form>

