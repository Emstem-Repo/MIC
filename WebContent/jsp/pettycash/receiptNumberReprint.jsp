<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<script type="text/javascript">
function validNumber(field) {
	if(isNaN(field.value)) {
		field.value="";
	}
}
function resetData() {
	document.getElementById("recNoToPrintId").value = "";
	document.getElementById("year").selectedIndex = 0;
	resetErrMsgs();
}

</script>

<html:form action="/CashCollection">
<html:hidden property="method" styleId="method" value="getReprintReceiptData"/>
<html:hidden property="formName" value="cashCollectionForm"/>
<html:hidden property="pageType" value="2"/>
<table width="98%" border="0">
  <tr>
    <td><span class="heading">
				<bean:message key="knowledgepro.pettycash" />
			<span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.pettycash.reprint"/>
			&gt;&gt;</span></span></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="10"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td background="images/Tcenter.gif" class="body" ><div align="left"><strong class="boxheader">
	        <bean:message key="knowledgepro.pettycash.reprint"/>
		</strong></div></td>
        <td width="13" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
      <tr>
        <td height="122" valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" class="news"><div align="center">
              <table width="100%" height="50"  border="0" cellpadding="0" cellspacing="0">
                 <tr>
               	    <td height="20" colspan="6" align="left">
               	    <div align="right" style="color:red"> <span class='MandatoryMark'><bean:message key="knowledgepro.mandatoryfields"/></span></div>
               	    <div id="errorMessage"> &nbsp;
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
                 <tr>
                   <td height="49" colspan="6" class="body" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                     <tr>
                       <td ><img src="images/01.gif" width="5" height="5" /></td>
                       <td width="914" background="images/02.gif"></td>
                       <td><img src="images/03.gif" width="5" height="5" /></td>
                     </tr>
                     <tr>
                       <td width="5"  background="images/left.gif"></td>
                       <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                           <tr >
                             <td width="25%" height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.petticash.recieptNo"/>: </div></td>
                             <td width="25%" height="25" class="row-even" align="left">
                             <span class="star">
                               <html:text property="number" styleId="recNoToPrintId" name="cashCollectionForm" maxlength="9" size="9" onkeypress="return isNumberKey(event)" onblur="validNumber(this)"/>
                             </span></td>
							
						<td class="row-odd" width="25%">
							<div align="right"><span class="Mandatory">*</span><bean:message key="knowledgepro.petticash.financialYear"/></div>
							</td>
							<td class="row-even" width="25%" align="left"><span class="star"> <input
								type="hidden" id="tempyear" name="tempyear"
								value="<bean:write name="cashCollectionForm" property="financialYearId"/>" />
							<html:select name="cashCollectionForm" property="finYearId"
								styleId="year" styleClass="combo">
								<html:option value="">- Select -</html:option>
								<logic:notEmpty name="cashCollectionForm" property="financilYearList">
						<html:optionsCollection name="cashCollectionForm" property="financilYearList"  label="financialYear" value="id"/>
						</logic:notEmpty>
							</html:select> </span></td>
							
                           </tr>
                       </table></td>
                       <td width="5" height="30"  background="images/right.gif"></td>
                     </tr>
                     <tr>
                       <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
                       <td background="images/05.gif"></td>
                       <td><img src="images/06.gif" /></td>
                     </tr>
                   </table></td>
                 </tr>
                 <tr>
                   <td height="20" colspan="6" valign="top" class="body" >
                   <table width="100%" cellspacing="1" cellpadding="2"><tr ></tr>
                     <tr>
                       <td height="20" colspan="2" >   
		                <table width="100%"  border="0" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td height="20"><div align="center">
		                      <html:submit styleClass="formbutton" value="Print" ></html:submit> &nbsp;&nbsp;&nbsp;
		                      <html:button property="" styleClass="formbutton" value="Reset" onclick="resetData()"></html:button>
		                    </div></td>
		                    </tr>
		                </table>
                        </td>
                     </tr>
                   </table></td>
                </tr>
                 <tr>
                   <td height="10" colspan="6" class="body" ></td>
                 </tr>
              </table>
            </div></td>
        <td width="13" valign="top" background="images/Tright_3_3.gif" class="news"></td>
      </tr>
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="100%" background="images/TcenterD.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>
<script type="text/javascript">
var print = "<c:out value='${cashCollectionForm.printReceipt}'/>";
if(print.length != 0 && print == "true") {
	var url ="CashCollection.do?method=dispalyReceipt";
	myRef = window.open(url,"challan_details","left=20,top=20,width=800,height=500,toolbar=1,resizable=0,scrollbars=1");
	
}
</script>
