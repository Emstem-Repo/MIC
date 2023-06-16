<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<html>
<head>

<style>
    @media print {
            tr.page-break  { display: block; page-break-before: always; }
        } 
     
 	
}  
 </style>
    
 <script language="javascript">
    document.onmousedown=disableclick;
    status="Right Click Disabled";
    function disableclick(event)
    {
      if(event.button==2)
       {
         alert(status);
         return false;    
       }
    }
    </script>

</head>

<html:form action="/falseMarkEntry" method="POST" enctype="multipart/form-data">
	<html:hidden property="formName" value="newExamMarksEntryForm"	styleId="formName" />
	<table width="99%" border="0">
		
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				
				
				<tr>
					
					<td class="heading">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						
						<tr>
							
							<td valign="top">
							<table width="100%" cellspacing="1" cellpadding="2">
								
								<tr>
									<td align="center" colspan="4">
									<img src='<%=CMSConstants.LOGO_URL%>'  height="120" width="550" />
									</td>

								</tr>
								<tr>
									<td align="center" colspan="4">
										TABULATION SHEET AFTER FIRST & SECOND VALUATION OF THE P.G. DEGREE EXAMINATIONS
									</td>

								</tr>
								
								<tr>
									<td  height="25" class="row-odd">
									<div align="left">Exam Name :</div>
									</td>
									<td  height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="examName"/>
									</td>
									
								</tr>
								
								<tr>
									<td  height="25" class="row-odd">
									<div align="left">Programme:</div>
									</td>
									<td  height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="programName"/>
									</td>
									
								</tr>
								<tr>
								
									<td  class="row-odd">
									<div align="left">Course Name :</div>
									</td>
									<td  align="left" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="courseName"/>
									</td>

								</tr>
								<tr>
									
									<td  height="25" class="row-odd">
									<div align="left">Course Code :</div>
									</td>
									<td height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="courseCode"/>
									</td>
								</tr>
								<tr>
									<td  height="25" class="row-odd">
									<div align="left">Q . P Code:</div>
									</td>
									<td  height="25" class="row-even">
										
									</td>
									<td width="20%" height="25" class="row-odd">
										Maximum Mark
									</td>
									<td height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="qpCode"/>
									</td>
								</tr>
								
								
							</table>
							</td>
							
						</tr>
						<tr>
						 <td>
							<table width="100%" cellspacing="1" cellpadding="2" border="1">
								<tr>
									<td rowspan="2" height="25" class="row-odd">
									 	Sl.No
									 </td>
									<td rowspan="2" height="25" class="row-odd">
									<div align="center">Box No.</div>
									</td>
									
									<td rowspan="2" height="25" class="row-odd">
									<div align="center">Barcode No.</div>
									</td>
									<td colspan="2" height="25" align="center" class="row-even">
										Mark
									</td>
								</tr>
								<tr>
									<td>In Figures</td>
									<td>In Words</td>
								</tr>
								<% int i=0; %>
								<logic:notEmpty name="newExamMarksEntryForm" property="examMarkEvaluationPrintToList">
								<logic:iterate id="to" name="newExamMarksEntryForm" property="examMarkEvaluationPrintToList">
								<%i++ ;%>
									<tr>
									<td align="center">
									 	<%=i %>
									 </td>
									 <td align="center">
									 	<bean:write name="to" property="boxNo"/>
									 </td>
									 <td>
									 	<bean:write name="to" property="falseNo"/>
									 </td>
									 <td>
									 	<bean:write name="to" property="mark"/>
									 </td>
									 <td>
									 	<bean:write name="to" property="markInWords"/>
									 </td>
									</tr>
									
								</logic:iterate>
								</logic:notEmpty>
							</table>
						</td>
					</tr>
					<tr>
						 <td>
							<table width="100%" cellspacing="1" cellpadding="2" >
							<tr height="50px"></tr>
					<tr>
									<% 
									  					DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
									    				Date date2 = new Date();
									    				String date11=dateFormat1.format(date2);
								    %>
										
									<td width="20%" height="25" class="row-odd">
									Date :<%=date11 %>
									</td>
									<td height="25" align="right" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="examMarkPrintTo.empName"/><br>
										<bean:write name="newExamMarksEntryForm" property="examMarkPrintTo.dept"/><br>
										<bean:write name="newExamMarksEntryForm" property="examMarkPrintTo.profession"/><br>
									</td>
								</tr>
								</table>
								</td>
								</tr>
					
					</table>
					</td>
					
				</tr>
				
			</table>
			</td>
		</tr>

	</table>
</html:form>
</html>
<script type="text/javascript"> window.print();</script>
