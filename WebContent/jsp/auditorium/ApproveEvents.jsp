<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-tld/struts-nested.tld" prefix="nested"%>
<%@taglib uri="/WEB-INF/struts-tld/struts-tiles.tld" prefix="tiles" %>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms" %>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<%@page import="com.kp.cms.utilities.CommonUtil"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@page import="java.util.Date"%><html>
<head>
    <script type="text/javascript" src="js/auditorium/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="js/auditorium/spinners.min.js"></script> 
    <script type="text/javascript" src="js/auditorium/tipped.js"></script>
    <link rel="stylesheet" type="text/css" href="css/auditorium/tipped.css" />
	<link rel='stylesheet' type='text/css' href=css/auditorium/reset.css"/>
	<link rel='stylesheet' type='text/css' href="css/auditorium/start/jquery-ui.css" />
	<link rel='stylesheet' type='text/css' href="css/auditorium/jquery.weekcalendar.css"/>
	<link rel='stylesheet' type='text/css' href="css/auditorium/auditoriumBooking.css"/>
	 <script type='text/javascript' src="js/auditorium/jquery.min.js"></script>
	<script type='text/javascript' src="js/auditorium/jquery-ui.min.js"></script>
	<script type='text/javascript' src="js/auditorium/jquery.weekcalendar.js"></script>
	<link rel="stylesheet" type="text/css" href="css/auditorium/jquery.confirmDialog.css" />
    <script src="jquery.confirm/jquery.confirm/jquery.confirm.js"></script>
    <script src="js/auditorium/bookedEventsApproval.js"></script>
</head>
<body>
<html:form action="/auditoriumBooking" method="POST">
<html:hidden property="formName" value="auditoriumBookingForm"/>
<input type="hidden" id="userName" name="userName" value="<bean:write name="auditoriumBookingForm" property="userName"/>">
	
	<table width="100%">
		<tr>
			<td><span class="Bredcrumbs"><bean:message
				key="knowledgepro.auditorium" /> <span class="Bredcrumbs">&gt;&gt;
			<bean:message key="knowledgepro.auditorium.auditoriumBooking.approving" />
			&gt;&gt;</span></span>
			</td>
	</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="30"><img src="images/Tright_03_01.gif"></td>
					<td width="100%" background="images/Tcenter.gif" class="body">
					<div align="left"><strong class="boxheader"><bean:message
						key="knowledgepro.auditorium.auditoriumBooking.approving" /></strong></div>
					</td>
					<td width="10"><img src="images/Tright_1_01.gif" width="9"
						height="29"></td>
				</tr>
				
				<tr>
					<td valign="top" background="images/Tright_03_03.gif"></td>
					<td valign="top" class="news">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr height="5"></tr>
						<tr>
						<td colspan="6" class="heading" align="left"><font size="2">Please select Block and Venue and click on Filter</font></td>
						</tr>
						<tr height="5"></tr>
						<tr>
								<td align="left" width="5%"><font size="2"><b>Block :</b></font></td>
								<td width="12%" align="left">
									<input type="hidden" name="tempBlock" id="tempBlock" value="<bean:write name="auditoriumBookingForm" property="blockId"/>">
									<html:select
								name="auditoriumBookingForm" onchange="getVenuesByBlock(this.value)" styleId="blockIDs" property="blockId" styleClass="select1">
								<logic:notEmpty name="auditoriumBookingForm" property="blockMap">
								<option value="" >Select Block</option>
								<logic:iterate id="map" name="auditoriumBookingForm" property="blockMap">
								<option value="<bean:write name="map" property="key"/>"><bean:write name="map" property="value"/></option>
								</logic:iterate>
								
									</logic:notEmpty>
							</html:select>
								</td>
								<td align="left" width="6%"><font size="2"><b>Venue :</b></font></td><td align="left" width="13%">
								<input type="hidden" name="tempVenue" id="tempVenue" value="<bean:write name="auditoriumBookingForm" property="venueId"/>">
					<html:select name="auditoriumBookingForm" styleId="venueIdForBlock" property="venueId" styleClass="select1">
								<option value="">Select venues</option>
								<logic:notEmpty name="blockOrVenueMap" scope="request">
								<logic:iterate id="venueMap" name="blockOrVenueMap">
            		    	 		<option value="<bean:write name="venueMap" property="key"/>"><bean:write name="venueMap" property="value"/></option>
            		    	 		</logic:iterate>
            		    	 		</logic:notEmpty>
							</html:select></td><td width="14%" align="left">
							<button type="button" id="searchFrom" class="CSSButtonToday">Filter</button>
	                       </td><td width="34%" align="right" >
	                       <button type="button" id="about_button" class="CSSButton">Approver Instructions</button>
	                       </td><td width="16%" align="right"><div align="right">
		<button type="button" id="agendaType" class="CSSButton" >Approved Events</button>
	</div></td></tr>
	<tr>
	                            <td align="left" width="5%" colspan="5"></td>   
								<td align="left" width="34%"><table width="100%"><tr><td width="30%" align="right"><font size="2"><b>Go to Date :</b></font></td><td width="40%" align="right">
								<input type="text" id="gotoDtae" class="DatePickerTextBox">
							    <script language="JavaScript">
										$(function(){
											$("#gotoDtae").datepicker({
												changeMonth: true,
												changeYear: true
											});
											});
                             </script>
									</td><td width="30%" align="left" colspan="4">
							<button type="button" id="displayCalendarByDate" class="CSSButtonToday">Go</button></td></tr></table></td>
							<td width="16%" align="right"><div align="right">
		<button type="button" id="pendingEvents" class="CSSButton">Pending for Approval</button>
	</div></td>
							</tr>
						
								<tr>
									<td height="22" valign="top" width="100%" colspan="7">
	<div id='calendar'></div>
	<div id="event_edit_container">
			<input type="hidden" />
			<ul>
				<li>
					<span>Date: </span><span class="date_holder"></span> 
				</li>
				<li>
					<label for="start">Start Time: </label><select name="start" style="width: 240px; padding: 3px;"><option value="">Select Start Time</option></select>
				</li>
				<li>
					<label for="end">End Time: </label><select name="end"  style="width: 240px; padding: 3px;"><option value="">Select End Time</option></select>
				</li>
					<li>
					<label for="blockId">Block: </label><select
								name="blockId" onchange="getVenues(this.value)" style="width: 240px; padding: 3px;">
								<logic:notEmpty name="auditoriumBookingForm" property="blockMap">
								<option value="" >Select Block</option>
								<logic:iterate id="map" name="auditoriumBookingForm" property="blockMap">
								<option value="<bean:write name="map" property="key"/>"><bean:write name="map" property="value"/></option>
								</logic:iterate>
								
									</logic:notEmpty>
							</select>
				</li>
				<li>
					<label for="venueId">Venue: </label>
					<select name="venueId" id="venueId" style="overflow: auto; width: 240px">
								<option value="">Select venues</option>
								<logic:notEmpty name="blockOrVenueMap" scope="request">
								<logic:iterate id="venueMap" name="blockOrVenueMap">
            		    	 		<option value="<bean:write name="venueMap" property="key"/>"><bean:write name="venueMap" property="value"/></option>
            		    	 		</logic:iterate>
            		    	 		</logic:notEmpty>
							</select>
				</li>
				<li>
				
					<label for="requirementId">Requirements: </label><div style="overflow: auto; width: 230px; height: 100px; border: 1px solid #336699; padding-left: 5px">
					           <logic:notEmpty name="auditoriumBookingForm" property="bookingRequirements">
								<logic:iterate id="map" name="auditoriumBookingForm" property="bookingRequirements" indexId="count">
								<input type="checkbox" value="<bean:write name="map" property="key"/>" id="<bean:write name="map" property="key"/>" class="requirementsCheckBox"/><bean:write name="map" property="value"/><br> 
								</logic:iterate>
									</logic:notEmpty>
									</div>
				</li>	
				<li>
					<label for="remarks">Remarks: </label><textarea name="remarks" style="width: 240px; padding: 3px;"></textarea>
				</li>
			</ul>
			</div>
			<div id="event_modify">
			<input type="hidden" />
			<ul>
				<li>
					<label for="adminRemarks">Remarks: </label><textarea name="adminRemarks" style="width: 240px; padding: 3px;"></textarea>
				</li>
			</ul>
			</div>
	<div id="about">
		<h2>Approver Instructions</h2>
		<ul class="formatted">
			<li>Approver will recieve a mail from user when he/she book for an event.</li>
			<li>Approver can view all the events booked by users.</li>
			<li>Approver can Modify/Approve/Reject an event by clicking on event.</li>
			<li>Approver can modify the event by dragging and resizing.</li>
			<li>Modified/Approved events will be visible in green color and Rejected events not visible.</li>
			<li>Any modification can be done by approver.</li>
			<li>SMS/Mail will send to user while modify/approve/reject an event by approver.</li>
		</ul>
	</div>
	<div id='window' style='display: none;width:500px;height:500px;' ></div>
	</td></tr> </table></td>
						<td valign="top" background="images/Tright_3_3.gif" class="news"></td>
						</tr>
						<tr>
					<td><img src="images/Tright_03_05.gif" width="9" height="29"></td>
					<td width="0" background="images/TcenterD.gif"></td>
					<td><img src="images/Tright_02.gif" width="9" height="29"></td>
				</tr>
						</table></td></tr></table>
		</html:form>
		</body>
<script type="text/javascript">
var tempBlock=$("#tempBlock").val();
var tempVenue=$("#tempVenue").val();
if(tempBlock!='' && tempVenue!=''){
	$("#blockIDs").val(tempBlock);
	$("#venueIdForBlock").val(tempVenue);
}
</script>
