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
									<td width="10%" height="25" class="row-odd">
									<div align="right">Exam Name :</div>
									</td>
									<td width="50%" height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="displatoList.examName"/>
									</td>
									<td width="10%" class="row-odd">
									<div align="right">Course Name :</div>
									</td>
									<td width="30%" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="displatoList.courseName"/>
									</td>

								</tr>
								<tr>
									<td width="20%" height="25" class="row-odd">
									<div align="right">Semester:</div>
									</td>
									<td width="50%" height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="displatoList.termNum"/>
									</td>
									<td width="20%" height="25" class="row-odd">
									<div align="right">Subject :</div>
									</td>
									<td height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="displatoList.subjectName"/>
									</td>
								</tr>
								<tr>
									<td width="20%" height="25" class="row-odd">
									<div align="right">Teacher:</div>
									</td>
									<td width="50%" height="25" class="row-even">
										<bean:write name="newExamMarksEntryForm" property="displatoList.teacher"/>
									</td>
									<td width="20%" height="25" class="row-odd">
									
									</td>
									<td height="25" class="row-even">
										
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
