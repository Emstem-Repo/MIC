<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>

<html>
<head>
<style type="text/css">
body
{
	background-color:#FFF;
}

div.sdmenu {
	width: 185px;
	background-color:#FFF;
	font-family: Arial, sans-serif;
	font-size: 10px;
	padding-bottom: 10px;
	background: url(bottom.gif) no-repeat  right bottom;
	color: #000;
}
div.sdmenu div {
	/*background: url(title.gif) repeat-x;*/
	overflow: hidden;
	 border: 1px solid #0B7ED1;
   background: -webkit-gradient(linear, left top, left bottom, from(#D5EAFA), to(#6EA7D1));
   background: -webkit-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -moz-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -ms-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -o-linear-gradient(top, #D5EAFA, #6EA7D1);
   background-color: #6EA7D1;
  
   -webkit-border-radius: 2px;
   -moz-border-radius: 2px;
   border-radius: 2px;
   -webkit-border-radius: 3px;
   -moz-border-radius: 3px;
   border-radius: 3px;
   text-decoration: none;
  -webkit-transition: .1s;
   -moz-transition: .1s;
   -o-transition: .1s;
   cursor: pointer;
   margin-bottom:5px;
}

div.sdmenu div:first-child {
	/*background: url(toptitle.gif) no-repeat;*/
	 border: 1px solid #666363;
    border: 1px solid #0B7ED1;
   background: -webkit-gradient(linear, left top, left bottom, from(#D5EAFA), to(#6EA7D1));
   background: -webkit-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -moz-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -ms-linear-gradient(top, #D5EAFA, #6EA7D1);
   background: -o-linear-gradient(top, #D5EAFA, #6EA7D1);
   background-color: #6EA7D1;
  
   -webkit-border-radius: 2px;
   -moz-border-radius: 2px;
   border-radius: 2px;
   -webkit-border-radius: 3px;
   -moz-border-radius: 3px;
   border-radius: 3px;
   text-decoration: none;
   margin-bottom:2px;
}
div.sdmenu div.collapsed {
	height: 25px;
}
div.sdmenu div span {
	display: block;
	padding: 5px 25px;
	font-size: 13px;
	font-weight: bold;
	color: #000;
	background: url(images/expanded.gif) no-repeat 10px center;
	cursor: default;
	
	border-bottom: 1px solid #ddd;
}
div.sdmenu div.collapsed span {
	background-image: url(images/collapsed.gif);
}
div.sdmenu div a {
	font-size: 12px;
	padding: 5px 10px;
	background: #eee;
	display: block;
	border-bottom: 1px solid #666;
	color: #000;
	text-decoration: none;
}
div.sdmenu div a.current {
	background : #ccc;
	font-size: 12px;
}
div.sdmenu div a:hover {
	background : url(images/linkarrow.gif) no-repeat right center ;
	color: #020ffe;
	
	border: 1px solid #C7C7C7;
 	 border: 1px solid #77BFF2;
   background: -webkit-gradient(linear, left top, left bottom, from(#F2F7FC), to(#C9E1F7));
   background: -webkit-linear-gradient(top, #F2F7FC, #C9E1F7);
   background: -moz-linear-gradient(top, #F2F7FC, #C9E1F7);
   background: -ms-linear-gradient(top, #F2F7FC, #C9E1F7);
   background: -o-linear-gradient(top, #F2F7FC, #C9E1F7);
   background-color: #C9E1F7;
  
 
   -webkit-border-radius: 2px;
   -moz-border-radius: 2px;
   border-radius: 2px;
   
  
   
   -webkit-transition: 0.5s;
   -moz-transition: 0.5s;
   -o-transition: 0.5s;
  	 cursor: pointer;
	text-decoration: none;
}
div.selected {
	background-color: #74aad3;
	font-size: 12px;
}

</style>
<script type="text/javascript">


	var myMenu;
	window.onload = function() {
	myMenu = new SDMenu("my_menu");
	myMenu.init();
	myMenu.expandAll();
	};
	
	
	var myMenu = new SDMenu("my_menu"); // ID of the menu element
	//Default values...
	myMenu.speed = 3;                     // Menu sliding speed (1 - 5 recomended)
	myMenu.remember = true;               // Store menu states (expanded or collapsed) in cookie and restore later
	myMenu.oneSmOnly = false;             // One expanded submenu at a time
	myMenu.markCurrent = true;            // Mark current link / page (link.href == location.href)
	
	//myMenu.init();
	
	//Additional methods...
	var firstSubmenu = myMenu.submenus[0];
	myMenu.expandMenu(firstSubmenu);      // Expand a submenu
	myMenu.collapseMenu(firstSubmenu);    // Collapse a menu
	myMenu.toggleMenu(firstSubmenu);      // Expand if collapsed and collapse if expanded
	
	myMenu.expandAll();                   // Expand all submenus
	myMenu.collapseAll();  	           // Collapse all submenus

	myMenu.expandAll = function() {
		var oldOneSmOnly = this.oneSmOnly;
		this.oneSmOnly = false;
		for (var i = 0; i < this.submenus.length; i++)
			if (this.submenus[i].className == "collapsed")
				this.expandMenu(this.submenus[i]);
		this.oneSmOnly = oldOneSmOnly;
	};
</script>
<style type="text/css">
	@keyframes blink {
    0% {
        opacity: 1;
    }
    50% {
        opacity: 0;
    }
    100% {
        opacity: 1;
    }
}
.blinkImage {
    animation: blink 1s;
    animation-iteration-count: infinite;
}
</style>
</head>
<body>
	<table width="250" border="0" cellspacing="0" cellpadding="0">
   <tr>
    <td >&nbsp;</td>
   </tr>
    
        <tr>
             <td class="">
             
             <table border="0" cellspacing="0" cellpadding="0">
             <tr>
             <td>
              <div style="float: left" id="my_menu" class="sdmenu">
             	<div class="collapsed">
             	<span>Attendance</span>
		         <c:if test="${showAttendanceRep}">
	                 <c:choose>
		         <c:when test="${linkForCjc}">
	                  <a href="studentWiseAttendanceSummary.do?method=getIndividualStudentWiseAttendanceSummaryParent" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance"/> </a>
	              </c:when>
	              <c:otherwise>
	              
	              <%-- <a href="studentWiseAttendanceSummary.do?method=getIndividualSessionWiseSubjectAndActivityAttendanceSummary" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" >SessionWise Attendance</a> --%>
	            
	              
	              <a href="studentWiseAttendanceSummary.do?method=getIndividualStudentWiseSubjectAndActivityAttendanceSummaryParent" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance"/></a> 
	              </c:otherwise>
	                </c:choose>
	             </c:if>
	              
                  <c:if test="${linkForCjc}">
                 	<a href="studentWiseAttendanceSummary.do?method=getIndividualStudentWiseActivityAttendanceSummary" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance.activity.attendance"/></a>
                  </c:if>
                  <c:if test="${showLinks}">
 					 <a href="studentWiseAttendanceSummary.do?method=getStudentAbscentWithCocularLeaveParentNew" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" >Absence Details </a>
	                </c:if>
	              <c:if test="${previousAttendance}">
	                <c:choose>
		           <c:when test="${linkForCjc}">
<!--	                  <a href="studentWiseAttendanceSummary.do?method=initPreviousStudentAttendanceSummeryCjc" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance.previous"/> </a>-->
	               </c:when>
	               <c:otherwise>
<!--	                   <a href="studentWiseAttendanceSummary.do?method=initPreviousStudentAttendanceSummeryChrist" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance.previous"/></a> -->
	               </c:otherwise>
	                </c:choose>
	             </c:if>
	             
	             <%--
	             <a href="ExtraCocurricularLeaveEntry.do?method=initExtraCocurricularLeaveEntry" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.attendance.studentLogin.extra.curricular.leave.entry"/></a> 
	          	  --%>
	            
	            </div>
	            <div class="collapsed">
             	<span>Downloads</span>
	             	<a href="studentWiseAttendanceSummary.do?method=getInternalMarkDetailsParent" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" ><bean:message key="knowledgepro.studentlogin.sem.result"/></a> 	

                   <!-- Mary:- Alternative code for MarksCard -->
                    <c:if test="${linkForCjc==false}">
		                   <a href="ParentLoginAction.do?method=initMarksCard" class="menuLink"><img src="images/smallArrow.gif" width="13" height="11" border="0" align="absmiddle" >ESE Marks Card</a>
		            </c:if>
		            </div>
		            </div>
               </td>
               </tr>
             </table>
            
             </td>
           
           </tr>
     
   </table>
</body>
</html>