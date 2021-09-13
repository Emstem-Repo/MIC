<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="css/sdmenu.css"/>
    <script type="text/javascript" src="js/AC_RunActiveContent.js"></script>
    <script type="text/javascript" src="js/sdmenu/sdmenu.js"></script>
    <script type="text/javascript" src="js/ajax/Ajax.js"></script>
    <script type="text/javascript" src="js/ajax/AjaxUtil.js"></script>
    <script type="text/javascript" src="js/leftSwitchMenu.js"></script>
	<script>
	
	function cancelAction() {
		document.location.href = "EvaluationStudentFeedback.do?method=initEvaluationStudentFeedback";
	}
	function cancelAction1() {
		closeConfirm = confirm("Are you sure you want to close the Faculty Evaluation?\n Already Submitted feedback will not be saved");
		if (closeConfirm == true) {
		document.location.href = "StudentLoginAction.do?method=returnHomePage";
	}
	}
	</script>
<html:form action="/EvaluationStudentFeedback" >
<html:hidden property="method" styleId="method" value="getTeachersAndSubjectDetails" />
<html:hidden property="formName" value="evaluationStudentFeedbackForm"/>
<html:hidden property="pageType" value="8"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="9"><img src="images/st_Tright_03_01.gif" width="9"
						height="29"></td>
					<td background="images/st_Tcenter.gif" class="body"><strong
						class="boxheader">Faculty Evaluation</strong></td>

					<td width="10"><img src="images/st_Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				<tr height="10%">
					<td height="20" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" height="10%">
					
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				
				
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
						 
            <td valign="top" class="news">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td ><img src="images/st_01.gif" width="5" height="5" /></td>
               
                <td><img src="images/st_03.gif" width="5" height="5" /></td>
              </tr>
              
              <tr>
               
                <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                
                  <tr >
                   <td width="50%" height="25" class="row-even">
							<div align="right"><span class="Mandatory">*</span>Class:</div>
							</td>
							<td width="50%" height="25" class="row-even" align="left"  style="size: 70px;"><label>
							<html:select property="classId"   styleClass="combo"
								styleId="classId">
								<html:option value="">
									<bean:message key="knowledgepro.admin.select" />
								</html:option>
								<html:optionsCollection property="evaStudentFeedbackOpenConnectionToList" name="evaluationStudentFeedbackForm" label="className"
									value="classesid" />
							</html:select> </label> <span class="star"></span></td>
							
				</tr>
             
               
                     <tr>
							<td width="50%" height="35" align="right">
							<div align="right">
							
									<html:submit property="" styleClass="classname" value="Continue"
										styleId="submitbutton">
									</html:submit>
									
							
								</div>
							</td>
							<td width="50%" height="35" align="left">
							<html:button property="" value="Back" styleClass="classname" onclick="cancelAction()"></html:button>
							</td>
							
						</tr> 
               
               
               
                </table></td>
                
              </tr>
             
              
              <tr>
                <td height="5"><img src="images/st_04.gif" width="5" height="5" /></td>
               
              </tr>
            
            
            </table></td>
        	
        
          </tr>
          
					</table>
					</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td height="19" valign="top" background="images/Tright_03_03.gif"></td>

					<td valign="top" class="news">&nbsp;</td>
					<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
				</tr>
				<tr>
					<td><img src="images/st_Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/st_TcenterD.gif"></td>
					<td><img src="images/st_Tright_02.gif" width="9" height="29"></td>
				</tr>
			</table>

</html:form>
