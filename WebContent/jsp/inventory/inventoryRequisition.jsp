<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>


<html:html>
<head>
<title>:: CMS ::</title>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/calendar.css" type="text/css">
<script language="JavaScript" src="js/calendar_us.js"></script>
<script type="text/javascript">
function confirmation() {
	var answer = confirm("Do you really want to continue")
	if (answer){
		window.location.href = "Inventory_requisition2.html";
		submitRequistion();
	}
}
function searchSubject(searchValue){
	var sda = document.getElementById('itemId');
	var len = sda.length;
	var searchValueLen = searchValue.length;
	for(var m =0; m<len; m++){
		sda.options[m].selected = false;		
	}
	for(var j=0; j<len; j++)
	{
		for(var i=0; i<len; i++){
			if(((sda.options[i].text).substring(0, searchValueLen).toUpperCase())  == (searchValue.toUpperCase())){
				sda.options[i].selected = true;
				break;
			}
		}
	}
}
function prepareItemList(){
	document.getElementById("method").value = "prepareItemListForTransfer";	
	document.getElementById("pageType").value = "1";
	document.invRequisitionForm.submit();
}
function deleteItem(itemId){
	deleteConfirm = confirm("Are you sure want to delete this entry?")
	if (deleteConfirm) {
		document.getElementById("itemId").value = itemId;	
		document.getElementById("method").value = "deleteItem";	
		document.invRequisitionForm.submit();
	}
}
function submitRequistion(){
	document.getElementById("method").value = "submitInvRequisition";	
	document.getElementById("pageType").value = "2";
	document.invRequisitionForm.submit();
}
function imposeMaxLength(evt, Object) {
	var keynum = (evt.which) ? evt.which : event.keyCode;
	if (keynum == 8 || keynum == 37 || keynum == 39 || keynum == 9) {
		return true;
	}
	var MaxLen = 200;
	return (Object.value.length < MaxLen);
}

</script> 
<style type="text/css"> 
.hide {display: none} 
</style> 

<style type="text/css">
<!--
body {
	margin-right: 0px;
	margin-bottom: 0px;
	margin-left: 0px;
	margin-top: 5px;
}
.hide {display: none}
.hide1 {display: none}
.hide1 {display: none}
-->
</style> </head>

<body>
<html:form action="InvRequisitionAction" method="post">
<html:hidden property="method" styleId="method" value=""/>
<html:hidden property="formName" value="invRequisitionForm"/>
<html:hidden property="pageType" styleId="pageType" value=""/>



<table width="99%" border="0">
  
  <tr>
    <td><span class="heading"><span class="Bredcrumbs">Inventory &gt;&gt; Inventory Requisition &gt;&gt;</span></span></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="9"><img src="images/Tright_03_01.gif" width="9" height="29"></td>
        <td colspan="2" background="images/Tcenter.gif" class="heading_white" >Inventory Requisition</td>
        <td width="10" ><img src="images/Tright_1_01.gif" width="9" height="29"></td>
      </tr>
      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
		<td colspan="2" class="heading">
        <div id="errorMessage"><FONT color="red"><html:errors /></FONT>
		<FONT color="green"><html:messages id="msg"
		property="messages" message="true">
		<c:out value="${msg}" escapeXml="false"></c:out>
		<br>
		</html:messages> </FONT></div>

        <div align="right"> <span class='mandatoryfield'>* Mandatory Fields</span></div></td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
      <tr>
        <td valign="top" background="images/Tright_03_03.gif"></td>
        <td colspan="2" valign="top" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td ><img src="images/01.gif" width="5" height="5" /></td>
            <td width="914" background="images/02.gif"></td>
            <td><img src="images/03.gif" width="5" height="5" /></td>
          </tr>
          <tr>
            <td width="5" background="images/left.gif"></td>
            <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                <tr >
                  <td height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span> <bean:message key="knowledgepro.inventory.from.inventory.location.col"/> </div></td>
                  <td class="row-even" >
                  <html:select property="inventoryId" styleId="inventoryIds" styleClass="comboLarge">
                  <html:option value=""><bean:message key="knowledgepro.admin.select" /></html:option>
                    <logic:notEmpty name="invRequisitionForm" property="inventoryList">
					<html:optionsCollection property="inventoryList" name="invRequisitionForm" label="name" value="id" />
					</logic:notEmpty>
                  </html:select>
				 </td>
                  <td class="row-odd" ><div align="right"><span class="Mandatory">*</span>Required by Date: </div></td>
                  <td class="row-even" ><table width="82" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="60"><html:text name="invRequisitionForm" property="dateOfDelivery" styleId="dateOfDeliveryId" size="16" maxlength="16"/>
</td>
                      <td width="40"><script language="JavaScript">
	new tcal ({
		// form name
		'formname': 'invRequisitionForm',
		// input name
		'controlname': 'dateOfDelivery'
	});</script></td>
                    </tr>
                  </table></td>
                </tr>
                <tr >
                  <td width="26%" height="25" class="row-odd" ><div align="right"><span class="Mandatory">*</span> Description   :</div></td>
                  <td class="row-even" ><label>
                  <html:textarea property="description" cols="18" rows="2" styleId="description" onkeypress="return imposeMaxLength(event,this)"></html:textarea>
                  </label></td>
                  <td class="row-odd" >&nbsp;</td>
                  <td width="26%" class="row-even" >&nbsp;</td>
                </tr>
              </table>

			</td>
            <td width="5" height="30"  background="images/right.gif"></td>
          </tr>
          <tr>
            <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
            <td background="images/05.gif"></td>
            <td><img src="images/06.gif" /></td>
          </tr>
        </table></td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
  <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td colspan="2" class="heading">&nbsp;</td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>







      <tr>
        <td valign="top" background="images/Tright_03_03.gif"></td>
        <td valign="top" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td ><img src="images/01.gif" width="5" height="5" /></td>
            <td width="914" background="images/02.gif"></td>
            <td><img src="images/03.gif" width="5" height="5" /></td>
          </tr>
          <tr>
            <td width="5"  background="images/left.gif"></td>
            <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                <tr >
                  <td height="25" class="row-odd" ><div align="right"> <span class="Mandatory">*</span> <bean:message key="knowledgepro.inventory.item.col"/> </div></td>
                  <td class="row-even" >
                  <html:text property="searchItem" styleId="searchSubLeft" size="15" maxlength="15" onkeyup="searchSubject(this.value)"/>
                   </td>
                  <td class="row-odd" ></td>
                  <td class="row-even" ></td>
                </tr>               
                <tr align="center">
                <td height="25" class="row-odd" >&nbsp; </td>
                <td style="width:300px">
		           <nested:select property="itemId" styleId="itemId" styleClass="body" size="4" style="width:300px;">
					<logic:notEmpty name="invRequisitionForm" property="itemList">
					<nested:optionsCollection name="invRequisitionForm" property="itemList" label="nameWithCode" value="id" styleClass="comboBig"/>
					</logic:notEmpty>
				</nested:select>
				</td>
				<td class="row-odd" valign="top" ><div align="right"><span class="Mandatory">*</span>Quantity Requested </div></td>
                  <td class="row-even" valign="top" >
                  <div align="left"><html:text property="quantityIssued" size="15" maxlength="15"></html:text></div>
                  </td>
				</tr>               
            </table>
           </td>
            <td width="5" height="30"  background="images/right.gif"></td>
          </tr>
          <tr>
            <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
            <td background="images/05.gif"></td>
            <td><img src="images/06.gif" /></td>
          </tr>      
        </table></td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
 
      
         <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td class="heading"><table width="100%"  border="0" cellspacing="0" cellpadding="0">  
          <tr>
            <td width="45%" align="right">&nbsp;</td>
            <td width="10%" height="35" align="center">
           <html:button property="" value="Add" styleClass="formbutton" onclick="prepareItemList()"></html:button>
            </td>            
            <td width="45%" height="35" align="left">&nbsp;</td>
          </tr>
        </table></td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
   
      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td class="heading"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td ><img src="images/01.gif" width="5" height="5" /></td>
            <td width="914" background="images/02.gif"></td>
            <td><img src="images/03.gif" width="5" height="5" /></td>
          </tr>
          <tr>
            <td width="5"  background="images/left.gif"></td>
            <td valign="top"><table width="100%" cellspacing="1" cellpadding="2">
                <tr >
                  <td width="48" class="row-odd" ><div align="center"><bean:message key="knowledgepro.slno"/> </div></td>
                  <td class="row-odd" align="center"><bean:message key="knowledgepro.inventory.item"/> </td>
                  <td width="163" height="25" class="row-odd" align="center">UOM </td>
                  <td width="154" class="row-odd" align="center">Quantity Requested </td>
                  <td width="53" class="row-odd" align="center"><bean:message key="knowledgepro.delete"/></td>
                </tr>
                <c:set var="temp" value="0" />
                <logic:notEmpty property="transferItemList" name="invRequisitionForm">
                <nested:iterate id="stock" property="transferItemList" name="invRequisitionForm" indexId="count">
                <c:choose>
				<c:when test="${temp == 0}">
                <tr >
                  <td height="25" class="row-even" align="center"><div align="center"><c:out value="${count + 1}" /></div></td>
                  <td width="135" class="row-even"align="center" ><nested:write name="stock" property="nameWithCode"/> </td>
                  <td height="25" class="row-even" align="center"><nested:write name="stock" property="issueUomName"/></td>
                  <td class="row-even" align="center"><nested:write name="stock" property="quantityIssued"/></td>
                  <td class="row-even" ><div align="center"><img src="images/delete_icon.gif" alt="CMS" width="16" height="16" style="cursor: pointer"
                  onclick="deleteItem('<nested:write name="stock" property="id"/>')"></div></td>
                </tr>
                <c:set var="temp" value="1" />
				</c:when>
				<c:otherwise>	
                <tr >
                  <td height="25" class="row-white"><div align="center"><c:out value="${count + 1}" /></div></td>
                  <td class="row-white" align="center" ><nested:write name="stock" property="nameWithCode"/></td>
                  <td height="25" class="row-white" align="center"><nested:write name="stock" property="issueUomName"/></td>
                  <td class="row-white" align="center"><nested:write name="stock" property="quantityIssued"/></td>
                  <td class="row-white" ><div align="center"><img src="images/delete_icon.gif" alt="CMS" width="16" height="16"
                  onclick="deleteItem('<nested:write name="stock" property="id"/>')"></div></td>
                </tr>
                <c:set var="temp" value="0" />
				</c:otherwise>
				</c:choose>
                </nested:iterate>
                </logic:notEmpty>
              </table>
               </td>
            <td width="5" height="30"  background="images/right.gif"></td>
          </tr>        
          <tr>
            <td height="5"><img src="images/04.gif" width="5" height="5" /></td>
            <td background="images/05.gif"></td>
            <td><img src="images/06.gif" /></td>
          </tr>
        </table>      
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>











      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td colspan="2" class="heading"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="35" align="right">&nbsp;</td>
            <td width="3" height="35" align="center"><html:button property="" value="Submit" styleClass="formbutton" onclick="confirmation()"></html:button></td>
            <td height="35" align="left">&nbsp;</td>
          </tr>
        </table></td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
      <tr>
        <td height="19" valign="top" background="images/Tright_03_03.gif"></td>
        <td colspan="2" class="heading">&nbsp;</td>
        <td valign="top" background="images/Tright_3_3.gif" ></td>
      </tr>
      <tr>
        <td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
        <td width="0" colspan="2" background="images/TcenterD.gif"></td>
        <td><img src="images/Tright_02.gif" width="9" height="29"></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>
</body>
</html:html>
