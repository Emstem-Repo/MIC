<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.constants.CMSConstants"%>

<html:html>
<head>
<title><bean:message key="knowledgepro.title"/> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script language="JavaScript" src="js/admission/admissionform.js"></script>

 <script src="jquery/development-bundle/jquery-1.7.1.js"></script>
 <script src="http://code.jquery.com/jquery-latest.js"></script>

<link href="css/styles.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

var jq=$.noConflict();

function IsNumeric(sText)
{
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
   return IsNumber;
   
   }




function validate(evt) {
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  key = String.fromCharCode( key );
	  var regex = /[0-9]|\./;
	  if( !regex.test(key) ) {
		  if (theEvent.keyCode!=8){
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
		  }
	  }
	}


jq(document).ready(function(){
	var options = {};
	if(jq('#CBCSSRadio').is(':checked')){
		 jq(".CBCSS").show();
		 jq(".OTHER").hide();
		
	}
	else if(jq('#OTHERRadio').is(':checked')){
		 jq(".OTHER").show();
		 jq(".CBCSS").hide();
		 
	}
	
	else{
	jq(".CBCSS").hide();
	jq(".OTHER").hide();
	
	}
	jq("#CBCSSRadio").click(function(){
		 jq(".CBCSS").show(2000);
		 jq(".OTHER").hide();
		 
	  });

	jq("#OTHERRadio").click(function(){
		     jq(".CBCSS").hide();
			 jq(".OTHER").show(2000);
			
		  });

	

	  
	});



function resetPG1(){
	//alert('hi');
	
	if (document.getElementById("pgtotalobtainedmarkother").value!=null)
	{
		document.getElementById("pgtotalobtainedmarkother").value="";
	}
	if (document.getElementById("pgtotalmaxmarkother").value!=null)
	{
		document.getElementById("pgtotalmaxmarkother").value="";
	}
	if (document.getElementById("pgtotalcredit").value!=null)
	{
		document.getElementById("pgtotalcredit").value="";
	}
	if (document.getElementById("pgtotalobtainedmark").value!=null)
	{
		document.getElementById("pgtotalobtainedmark").value="";
	}
	if (document.getElementById("pgtotalmaxmark").value!=null)
	{
		document.getElementById("pgtotalmaxmark").value="";
	}
	
}


</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 5px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>

<body>
<html:form action="/applicationEdit">
<html:hidden property="method" value=""/>
<html:hidden property="pageType" value="4"/>
<html:hidden property="formName" value="applicationEditForm"/>

<table width="98%" border="0">
  <tr>
    <td><span class="Bredcrumbs"><bean:message key="knowledgepro.admission"/> <span class="Bredcrumbs">&gt;&gt; <bean:message key="admissionForm.edit.detailmarkedit.label"/>&gt;&gt;</span></span></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
     
      <tr>
        <td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader"><bean:message key="admissionForm.detailmark.main.label"/></strong></div></td>
        <td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
		
		
		<tr><td colspan="3" align="left"><div id="errorMessage"><html:errors/></div></td></tr>
    
    
    
    
      <tr>
        <td height="122" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><div align="center">
              <table width="100%" height="347"  border="0" cellpadding="0" cellspacing="0">
                
              <tr>
                   <td height="35" colspan="6" class="body" ><table width="70%" cellspacing="1" cellpadding="2" align="center">
                     <tr bgcolor="#FFFFFF">
                       <td height="10" colspan="22"><h3><font color="red">Enter Your Qualifying Examination Marks</font></h3></td>
                       
                     </tr>
                     
                      <tr> <td height="5" colspan="4" align="center"><h3><font color="blue">Choose the pattern which you studied for PostGraduate program</font></h3></td>
                      </tr>
                      <tr>
                        <td width="50%" class="heading" colspan="2" id="CBCSS" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:radio styleId="CBCSSRadio" property="patternofStudyPG" value="CBCSS" >Grade system</html:radio></td>
                         <td width="50%" class="heading" id="OTHER" colspan="2" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:radio styleId="OTHERRadio" property="patternofStudyPG" value="OTHER" >Mark system</html:radio></td>
         
                     </tr>
                     <tr><td colspan="4"><p></p></td></tr>
                     <tr><td colspan="4"><p></p></td></tr>
                     		
					<tr class="CBCSS">
					<td height="25" class="row-odd" width="22%" ><div align="center">Obtained Grade Score</div></td>
 						
					<td width="25%" height="25" class="row-even"  >
                     <div align="center">CGPA: <nested:text property="pgtotalobtainedmark" styleId="pgtotalobtainedmark" onkeypress='validate(event)' size="6" maxlength="6"></nested:text></div></td>
						 	
					<td width="25%" height="25" class="row-even"   align="left">
                     <div align="center">MAX CGPA: <nested:text property="pgtotalmaxmark" styleId="pgtotalmaxmark" onkeypress='validate(event)' size="6" maxlength="6"></nested:text></div></td>
					<td width="25%" height="25" class="row-even"   align="left">
                     <div align="center">TOTAL CREDIT: <nested:text property="pgtotalcredit" styleId="pgtotalcredit" onkeypress='validate(event)' size="6" maxlength="6"></nested:text></div></td>
						 
					</tr >
						
									
					<tr class="OTHER">
					<td height="25" class="row-odd" width="22%" ><div align="center">Final Marks </div></td>
 						
					<td width="25%" height="25" class="row-even">
                    <div align="center"> GRAND TOTAL: <nested:text property="pgtotalobtainedmarkother" styleId="pgtotalobtainedmarkother" size="6" maxlength="6" onkeypress='validate(event)'></nested:text></div></td>
						 	
					<td width="25%" height="25" class="row-even">
                     <div align="center">MAX MARK: <nested:text property="pgtotalmaxmarkother" styleId="pgtotalmaxmarkother" size="6" maxlength="6" onkeypress='validate(event)'></nested:text></div></td>
					<td width="25%" height="25" class="row-even"></td>	 
					</tr>
					
					
					
                   </table>
                   </td>
             </tr>
                 
                 
                 
                 
             <tr>
                   <td height="35" colspan="6" class="body" >
			
	
                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="45%" height="35"><div align="right">
                        <html:button property="" onclick="submitApplicationEditForm('submitDetailMarkConfirmforpgEdit')" styleClass="formbutton" value="Submit"></html:button>
                    </div></td>
                    <td width="2%"><html:button property=""  styleClass="formbutton" value="Reset" onclick='resetPG1()'></html:button></td>
                   <td width="53%"><html:button property=""  styleClass="formbutton" value="Cancel" onclick='submitEditCancelButton()'></html:button></td>
                  </tr>
                </table>
                </td>
                 
              </tr>
                       
               
            <tr>
                   <td height="10" colspan="6" class="body" ></td>
          </tr>
                 
                 
              </table>
            </div>
            </td>
            
        <td width="13" valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      
     
      
      
      
      
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="100%" background="images/Tcenter.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
      
      
      
      
    </table>
    </td>
  </tr>
  
</table>

</html:form>

</body>
</html:html>