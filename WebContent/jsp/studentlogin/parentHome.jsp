<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<script type="text/javascript" src="js/clockh.js"></script>
<script type="text/javascript" src="js/clockp.js"></script>

<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<script src="js/chart/amcharts.js" type="text/javascript"></script>
<script src="js/chart/serial.js" type="text/javascript"></script>



<script type="text/javascript">
var chart;

var chartData =<%=session.getAttribute("jsonChart")%>

AmCharts.ready(function () {
    chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = "monthName";
    chart.startDuration = 1;
    chart.angle = 25;
    chart.depth3D = 10;    
    var categoryAxis = chart.categoryAxis;
    categoryAxis.labelRotation = 90;
    categoryAxis.autoGridCount  = false;
    categoryAxis.gridCount = chartData.length;                    
    categoryAxis.gridPosition = "start";
    var graph = new AmCharts.AmGraph();
    graph.valueField = "totalConduct";
    graph.balloonText = "[Conducted Periods]: <b>[[value]]</b>";
    graph.type = "column";
    graph.fillAlphas = 0.8;
    chart.addGraph(graph);

    var graph1 = new AmCharts.AmGraph();
    graph1.valueField = "totalPresent";
    graph1.balloonText = "[Attended Periods]: <b>[[value]]</b>";
    graph1.type = "column";
    graph1.fillAlphas = 0.8;
    chart.addGraph(graph1);

    var graph2 = new AmCharts.AmGraph();
    graph2.valueField = "totalAbsent";
    graph2.balloonText = "[Absented Periods]: <b>[[value]]</b>";
    graph2.type = "column";
    graph2.fillAlphas = 0.8;
    chart.addGraph(graph2);

    var graph3 = new AmCharts.AmGraph();
    graph3.valueField = "percentage";
    graph3.balloonText = "Percentage(%): <b>[[value]]</b>";
    graph3.type = "column";
    graph3.fillAlphas = 0.8;
    chart.addGraph(graph3);
    
    chart.write("chartdiv");
});
</script>
<script>
AmCharts.ready(function() {
	// chart code will go here
	var graph = new AmCharts.AmGraph();
graph.valueField = "visits";
graph.type = "column";
chart.addGraph(graph);
});
chart.write('chartdiv');
</script>


	<script type="text/javascript">
	// 1.
	$(document).ready(function(){
		 
        $("#mobileNo").each(function() 
    	     {
            $(this).keyup(function(){
                calculate();
            });
        });
    });
 
    function calculate() {
        $("#mobileNo").each(function() {
            if(!isNaN(this.value) && this.value.length!=0) {
            	if(this.value.length > 10 || this.value.length < 10){
            		document.getElementById('button').style.visibility='hidden';
                	}else{
                		document.getElementById('button').style.visibility='visible';
                	}
            }
        });
    }
	</script>
<!-- <SCRIPT LANGUAGE='JAVASCRIPT' TYPE='TEXT/JAVASCRIPT'>
var popupWindow=null;
function popup(mypage,myname,w,h,pos,infocus){

if (pos == 'random')
{LeftPosition=(screen.width)?Math.floor(Math.random()*(screen.width-w)):100;TopPosition=(screen.height)?Math.floor(Math.random()*((screen.height-h)-75)):100;}
else
{LeftPosition=(screen.width)?(screen.width-w)/2:100;TopPosition=(screen.height)?(screen.height-h)/2:100;}
settings='width='+ w + ',height='+ h + ',top=' + TopPosition + ',left=' + LeftPosition + ',scrollbars=no,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=no';popupWindow=window.open('',myname,settings);
if(infocus=='front'){popupWindow.focus();popupWindow.location=mypage;}
if(infocus=='back'){popupWindow.blur();popupWindow.location=mypage;popupWindow.blur();}

}
</script>-->
<script language="JavaScript">
	
 	 function submit(){
			var mobileNo=document.getElementById("mobileNo").value;
			document.location.href = "StudentLoginAction.do?method=submitMobileNo&mobileNo=" +mobileNo;
 	 	 }
 	function openHtml() {
 		var url="StudentLoginAction.do?method=help";
 		win2 = window.open(url, "Help", "left=1350,top=550,width=200,height=100,toolbar=0,resizable=0,scrollbars=0,addressbar=0"); 
 	}

	function opendetailsStudent() {
		var url="StudentLoginAction.do?method=StudentDetails";
		win2 = window.open(url, "Student Details", " left=0,top=0,width=500,height=450,toolbar=0,resizable=1,scrollbars=1,addressbar=0"); 
	}
 	</script>
<body style="background-color:#E7F5FE">
<table width="930" border="0" align="center" cellpadding="7" cellspacing="0">
  <tr>
    <td >&nbsp;</td>
    <td width="7">&nbsp;</td>
    <td>&nbsp;</td>
    <td width="7">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <!-- 
    <td valign="top">
    <table width="201" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table width="180" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td colspan="2" class="nav"><bean:message key="knowledgepro.studentlogin.navigation"/></td>
                    <td width="13" class="tr">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="13" class="le">&nbsp;</td>
                    <td class=""><table border="0" cellspacing="0" cellpadding="0">
                       
                        <tr>
                         <td width="35" height="21" align="center"><img src="images/bullet_img.gif" width="14" height="9" /></td>
                          <td width="137" height="21" class="navmenu"><a href="studentWiseAttendanceSummary.do?method=getIndividualStudentWiseAttendanceSummary" class="navmenu"><bean:message key="knowledgepro.attendance"/></a></td>
                          </tr>
                          <tr>
                          <td width="35" height="21" align="center"><img src="images/bullet_img.gif" width="14" height="9" /></td>
                           <td width="137" height="21" class="navmenu"><a href="studentWiseAttendanceSummary.do?method=getIndividualStudentWiseActivityAttendanceSummary" class="navmenu"><bean:message key="knowledgepro.attendance.activity.attendance"/> </a></td>
                        </tr>
                        <%-- 
                         <tr>
                          <td width="35" height="21" align="center"><img src="images/bullet_img.gif" width="14" height="9" /></td>
                          <td height="21" class="navmenu"><a href="studentreqstatus.do?method=initViewStudentDetails" class="navmenu"><bean:message key="knowledgepro.hostel.studentreqstatus"/></a></td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center"><img src="images/bullet_img.gif" width="14" height="9" /></td>
                          <td height="21" class="navmenu"><a href="HostelApplication.do?method=initHostelApplicationStudent" class="navmenu"><bean:message key="knowledgepro.hostel.app.student"/></a></td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                         <tr>
                          <td width="35" height="21" align="center">&nbsp;</td>
                          <td height="21" class="navmenu">&nbsp;</td>
                        </tr>
                        --%>
                    </table></td>
                    <td class="ri">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="bl">&nbsp;</td>
                    <td class="bm">&nbsp;</td>
                    <td class="br">&nbsp;</td>
                  </tr>
              </table></td>

        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height="30" valign="bottom"><p>&nbsp;</p>
          <p>&nbsp;</p>
          <p><img src="images/bullet_imge.gif" width="170" height="8" /></p></td>
        </tr>
      </table></td>
    <td width="7"><p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p></td>
    </td>
    -->

	<!--
	
	<td valign="top">
	
	<table width="248" >

         <tr>
          <td height="23" align="center"><img src="images/dot_img.gif" width="247" height="1" /></td>
        </tr>
        <tr>
          <td><table width="270" id="outertable">
            <tr>
              <td colspan="2" class="nav"><bean:message key="knowledgepro.studentlogin.notifications"/></td>
                <td colspan="2"><img src="images/studentLogin/home/notifications.jpg" width="290" height="35"></td>
            </tr>
            <tr>
              <td><table height="460" width="100%">
                   <tr>
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                    <c:if test="${!linkForHolisticExam || linkForCjc==true}">
                     <td class="navmenu1"><bean:message key="knowledgepro.nonotifications"/> </td>
                     </c:if>
                    <td><c:if test="${linkForCjc==false}">
                    <c:if test="${linkForHolisticExam}">
						   <a href="onlineExamSuppApplication.do?method=initOnlineSuppExam" class="navmenu" style="text-decoration: blink;color: red">Holistic/Indian Constitution Repeat Exam Application</a>
				    </c:if>
				    </c:if>
				    </td>
                  </tr>
                    <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><blink><B><a href="http://www.christuniversity.in/uploadimages/examination%20gide%20revised.pdf" class="menuLink" target="_blank" style="text-decoration: blink;color: red">Examination Guide</a></B></blink></td>
                      </c:if>
                  </tr>

                  <tr>
                  		<c:if test="${linkForCjc==false}">
                  		<logic:equal value="FAIL" name="loginform" property="status">
                  		 <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
								<td class="navmenu1"><blink><B><a href="examRegistrationDetails.do?method=initExamRegistrationDetails&propertyName=supplementry" class="navmenu" style="text-decoration: blink;color: red">SAP Supplementary Registration</a></B></blink></td>
                  		</logic:equal>
                  		<logic:equal value="ALLOW" name="loginform" property="status">
                  			<td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
							<td class="navmenu1"><blink><B><a href="examRegistrationDetails.do?method=initExamRegistrationDetails&propertyName=regular" class="navmenu" style="text-decoration: blink;color: red">SAP Exam Registration </a></B></blink></td>
						</logic:equal>
						</c:if>
                  </tr>
                  <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><blink><B><a href="StudentLoginAction.do?method=initSapRegistration" class="navmenu" style="text-decoration: blink;color: red">SAP Course Registration</a></B></blink></td>
                      </c:if>
                  </tr>
                  <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                  <td class="navmenu1"><blink><B>
						   <a target="_blank" href="http://www.christuniversity.in/display_article.php?fid=1196&arid=104&f=5" class="navmenu" style="text-decoration: blink;color: red">Mid Sem Repeat Examinations - August 2013</a></B></blink>
				  	</td> 
				  	
				    </c:if> 
                   </tr>
                  <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                  <td class="navmenu1">
						   <a href="studentCarPass.do?method=initStudentCarPass">Car Pass</a>
				  	</td> 
				  	
				    </c:if> 
                   </tr>
                   <tr>
                   <c:if test="${linkForCjc==false}">
                     <logic:equal value="true" name="loginform" property="studentPhotoUpload">
                       <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                         <td class="navmenu1"><blink><B>
						   <a href="UploadPhotos.do?method=initUploadFinalYearStudentPhoto" class="navmenu" style="text-decoration: blink;color: red">Upload your recent photograph</a>
				  	     </B></blink></td>
				  	  </logic:equal>
				   </c:if>
                  </tr>
                   <tr>
                   <c:if test="${linkForCjc==false}">
                     <logic:equal value="true" name="loginform" property="studentPhotoUpload">
                       <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                         <td class="navmenu1"><blink><B>
						   <a target="_blank" href="http://www.christuniversity.in/christalumni/index.php" class="navmenu" style="text-decoration: blink;color: red">Register for Alumni</a>
				  	     </B></blink></td>
				  	  </logic:equal>
				   </c:if>
                  </tr>
                  <tr>
                  <c:if test="${linkForCjc==false}">
                    <c:if test="${finalYearStudent==true}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                    <td>
						   <a href="onlineExamSuppApplication.do?method=initConvocationRegistration" class="navmenu" style="text-decoration: blink;color: red">Convocation Registration</a>
				  	</td>
				    </c:if>
				    </c:if>
				    
                  </tr>
                  
                   <tr>
				    <c:if test="${linkForCjc==false}">
                    	<td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                    	<td class="navmenu1"><a target="_blank" href="http://www.christuniversity.in//uploadimages/1%284%29.pdf" style="color: blue">Student Regulations & Sample Seating Chart</a></td>
                    </c:if>
				    
                  </tr>
                  <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><a target="_blank" href="./downloadForms/ExamRegulations.pdf"  style="color: blue">Exam Regulations</a></td>
                      </c:if>
                  </tr>
                  
                  <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                      <td class="navmenu1"><a target="_blank" href="http://www.christuniversity.in/uploadimages/Returning Documents.pdf"  style="color: blue">Schedule for returning of documents(Class XII) for UG classes</a></td> 
                      </c:if>
                  </tr>
		<tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" ><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><blink><B><a href="http://www.christuniversity.in/uploadimages/examination%20gide%20revised.pdf" class="menuLink" target="_blank" style="text-decoration: blink;color: red">Examination Guide</a></B></blink></td>
                      </c:if>
                  </tr>
                   <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><blink><B>

						<a style="font-size:12px;color:#ffffff;font-family:verdana;background-color:blue;" href="javascript:void;" onmouseover="popup('http://christuniversity.in/html/room.php?fRegisterNo=<c:out value="${username}"/>','pagename','460','80','center','front');">Allocated Room/Seat No. For Mid Sem Exam</a>
						</B></blink></td>
                      </c:if>
                  </tr>
                    <tr>
                   <c:if test="${linkForCjc==false}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                     <td class="navmenu1"><a target="_blank" href="./downloadForms/examination gide revised.pdf"  style="color: blue">Examination Guide Revised</a></td>
                      </c:if>
                  </tr>
                  <tr>
                  <c:if test="${linkForCjc==false}">
                    <c:if test="${finalYearStudent==true}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                    <td>
						   <a href="onlineExamSuppApplication.do?method=initConvocationRegistration" class="navmenu" style="text-decoration: blink;color: red">Convocation Registration</a>
				  	</td>
				    </c:if>
				    </c:if>
				    
                  </tr>
                  
                  <tr>
                    <c:if test="${studentAchievement!=null}">
                    <td width="10" align="center"><img src="images/bullet_imgs.gif" width="8" height="6" /></td>
                    <td>
                    <font style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: green;text-decoration: blink;">
						   <bean:write name="studentAchievement" scope="session"/></font>
				  	</td>
				    </c:if>
				    
                  </tr>
                  
              </table></td>
            </tr>
          </table></td>
        </tr>
      </table>
      
      
      
      </td>
      
      -->
      
      
      
   <!--
    
    <td align="left" valign="top" ><table width="280">
       
        <tr>
          <td valign="top"><table width="280" id="outertable">
            <tr>
              <td colspan="2" class="nav"><bean:message key="knowledgepro.studentlogin.profile"/></td>
               <td colspan="2"><img src="images/studentLogin/home/profile.jpg" width="280" height="35"></td>
            </tr>
            
            <tr>
              <td valign="top"><table class="gradienttable"  height="110" width="100%">
                <tr>
                  <td colspan="2" width="151" class="studentrow-even" align="center" valign="top"><div align="center" style="height: 180px; width: 148px;">
	                <center>  <img src='<%=session.getAttribute("STUDENT_IMAGE")%>' width="148" height="180"/></center></div>
                  </td>
                  </tr>
                  <tr >
                  <td style="padding: 3px;">
	                  <table class="gradienttable"  width="100%">
	                	<tr>
	                   		<td height="20" class="studentrow-odd" align="left" width="35%"><bean:message key="knowledgepro.studentlogin.name"/>:</td>
	                   		<td height="20" class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="studentName"/></td>
	                 </tr>
	                 <tr>
	                   		<td height="20" class="studentrow-odd" align="left" width="35%"><bean:message key="admissionFormForm.emailId"/>:</td>
	                   		<td height="20" class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="univEmailId"/></td>
	                 </tr>
	                  <tr>
		                	<td class="studentrow-odd" align="left"  width="35%"><bean:message key="knowledgepro.studentlogin.mobile.no"/>:</td>
		                	<td class="studentrow-even" align="left" width="65%"><html:text property="mobileNo" name="loginform" styleClass="studentrow-even" size="12" maxlength="10" styleId="mobileNo" /> 
							<img src="images/questionMark.jpg" style="cursor:pointer" width="20" height="20"
										 onclick="openHtml()" title="Help"><div id="messageBox">
			                <div id="contents"></div></div>
			                <html:button property="" styleClass="btnbg" styleId="button" value="Update" onclick="submit()"></html:button></td>
		                 
		                </tr>
		                <tr>
		                <td colspan="2" height="8">
	                   		<a href="#" class="menuLink" onclick="opendetailsStudent()"  class="navmenu" style="text-decoration: color: red">More...</a></td>
		                </tr>
	                  </table>
                 </td>
                 </tr>
              </table></td>
              
           </tr>
           </table>
           </td>
           </tr>
         <tr><td height="3" align="center">&nbsp;</td>
          </tr>
          <tr>
          <td>
          
          
          
          <Div id="LoginFormDiv">
          <table width="278">
            <tr>
             <td colspan="2" class="nav" width="278"><bean:message key="knowledgepro.studentlogin.newandevents" /></td>
              <td colspan="2"><img src="images/studentLogin/home/News.jpg" width="290" height="35"></td>
            </tr>
            <tr>
              <td><table width="246" border="0" cellspacing="0" height="150">
                  <tr><td class="navmenu1"><div id="News">
					<c:out value='${loginform.description}' escapeXml='false' />
					
					</div></td></tr>
              </table></td>
            </tr>
            
            
          </table>
          </Div>
          
          
          
          
          </td>
        </tr>
            
          </table>
          </td>
        -->
        
          <td valign="top" align="left"><table width="500" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2" class="nav"><bean:message key="knowledgepro.studentlogin.profile"/></td>
              <td width="13" class="tr">&nbsp;</td>
            </tr>
            <tr>
              <td width="13" class="le" valign="top">&nbsp;</td>
              <td valign="top"><table cellspacing="1" cellpadding="2" border="1" bordercolor="ffffff" height="410" width="100%">
                <tr>
                  <td width="151" class="studentrow-even" align="center" valign="top"><div style="border-style:solid; border-width :1px;border-color:#3292BA;height: 129px; width: 133px">
	                  <img src='<%=session.getAttribute("STUDENT_IMAGE")%>' width="133" height="129" align="top"/></div>
                  </td>
                </tr>
                <tr>
	                   		<td height="20" class="studentrow-odd" align="left" width="35%"><bean:message key="knowledgepro.studentlogin.name"/>:</td>
	                   		<td height="20" class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="studentName"/></td>
	                 </tr>
	                 <tr>
	                   		<td height="20" class="studentrow-odd" align="left" width="35%"><bean:message key="admissionFormForm.emailId"/>:</td>
	                   		<td height="20" class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="univEmailId"/></td>
	                 </tr>
	                  <tr>
		                	<td class="studentrow-odd" align="left"  width="35%"><bean:message key="knowledgepro.studentlogin.mobile.no"/>:</td>
		                	<td class="studentrow-even" align="left" width="65%"><html:text property="mobileNo" name="loginform" styleClass="studentrow-even" size="12" maxlength="10" styleId="mobileNo" /> 
							<!--<img src="images/questionMark.jpg" style="cursor:pointer" width="20" height="20"
										 onclick="openHtml()" title="Help"><div id="messageBox">
			                <div id="contents"></div></div>
			                <html:button property="" styleClass="btnbg" styleId="button" value="Update" onclick="submit()"></html:button></td>
		                 
		                --></tr>
                <tr >
                    <td class="studentrow-odd" align="left" width="35%"><bean:message key="knowledgepro.studentlogin.name"/>:</td>
                    <td class="studentrow-even" align="left" width="65%"> <bean:write name="loginform" property="studentName"/></td>
                </tr>
                <tr>
                    <td class="studentrow-odd" align="left" width="35%"><bean:message key="knowledgepro.attendance.activityattendence.class"/>:</td>
                    <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="className"/></td>
                 </tr>
                 <tr>
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionFormForm.fatherName"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"> <bean:write name="loginform" property="fatherName"/></td>
                 </tr>
                 <tr>  
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionFormForm.motherName"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="motherName"/></td>
                 </tr>
				 <tr>
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionForm.studentinfo.nationality.label"/></td>
                   <td class="studentrow-even" align="left" width="65%"> <bean:write name="loginform" property="nationality"/></td>
                </tr>
                    <tr>   
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionForm.studentinfo.bloodgroup.label"/></td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="bloodGroup"/></td>
                 </tr>
                 <tr>
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionFormForm.emailId"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="contactMail"/></td>
                  </tr>
                    <tr>
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="knowledgepro.studentLogin.bankAcNo"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="bankAccNo"/></td>
                 </tr>     
				 <tr>
                   <td class="studentrow-odd" align="left" width="35%"><bean:message key="admissionForm.phone.main.label"/></td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="phNo1"/></td>
                   </tr>
                 <tr>
                   <td class="studentrow-odd" align="left"  width="35%"><bean:message key="admissionForm.studentinfo.currAddr.label"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="currentAddress1"/>,
                   													   <bean:write name="loginform" property="currentAddress2"/>,
                   													   <bean:write name="loginform" property="currentCity"/>,
                   													   <bean:write name="loginform" property="currentState"/>, 
                   													   <bean:write name="loginform" property="currentPincode"/>
				   </td>
				  </tr>
                  <tr>
                   <td class="studentrow-odd" align="left"  width="35%"><bean:message key="admissionForm.studentinfo.permAddr.label"/>:</td>
                   <td class="studentrow-even" align="left" width="65%"><bean:write name="loginform" property="permanentAddress1"/>,
                    												   <bean:write name="loginform" property="permanentAddress2"/>,
                    												   <bean:write name="loginform" property="permanentCity"/>, 
                   													   <bean:write name="loginform" property="permanentState"/>,
                                                                       <bean:write name="loginform" property="permanentPincode"/>
				   </td>
                 </tr> 
                
                 </table>
                 </td>
                  <td class="ri">&nbsp;</td>
           </tr><!--
           
           <tr>
           			<td colspan="3" align="center">
           			<html:button property="" styleClass="formbutton" value="Close" onclick="javascript:self.close();"></html:button>
           			</td>
           </tr>
            --><tr>
              <td class="bl">&nbsp;</td>
              <td class="bm">&nbsp;</td>
              <td class="br">&nbsp;</td>
            </tr>
           </table>
         </td>
      
    <!--<c:choose>
 	<c:when test="${jsonChart != null }">
 	<div style="width: 100%;height: 450px; margin-top: 50px; display: block;">
			<input type="hidden" id="json" value="${jsonChart}"></input>
			<div align="center" style="font-weight: bold; font-size: 16px;"><font style="text-transform: uppercase;"></font>Student Monthly Attendance Summary</div>
			<div id="chartdiv" style='width:100%; height: 350px;'></div>
	</div>
	</c:when>
	
	</c:choose>
	
    -->
    <td height="200" width="300" valign="top" align="left" colspan="4" style="padding-top: 10px ;">
    
     <Div id="LoginFormDiv">
          <table width="278">
            <tr>
             <td colspan="2" class="nav" width="278"><bean:message key="knowledgepro.studentlogin.newandevents" /></td>
             <!-- <td colspan="2"><img src="images/studentLogin/home/News.jpg" width="290" height="35"></td>-->
            </tr>
            <tr>
              <td><table width="246" border="0" cellspacing="0" height="150">
                  <tr><td class="navmenu1"><div id="News">
                  <marquee behavior="scroll" direction="up"
					scrollamount="2" width=220 height=150
		    		style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: black"
					onmouseover="this.setAttribute('scrollamount', 0, 0);"
					onmouseout="this.setAttribute('scrollamount', 1, 0);">
					<c:out	value='${loginform.description}' escapeXml='false'  />													
				  </marquee>
					
					
					</div></td></tr>
              </table></td>
            </tr>
            
            
          </table>
     </Div>
    
    
    </td>
        
  <tr>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="right" valign="top" >&nbsp;</td>
  </tr>
</table>
</body>
<script language="JavaScript">
document.getElementById('button').style.visibility='hidden';
</script>
