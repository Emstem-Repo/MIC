<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.kp.cms.constants.KPPropertiesConfiguration"%>
<head>
</head>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tbody>
  
  
   <tr valign="top"><td width="100%">
	   	    <table cellpadding="0" cellspacing="0" align="center" width="80%">
		  <tr>
            <td width="30%"  align="left" valign="top"><img src="images/header-logo.jpg" width="600" height="100" align="top"/></td>
			<td align="right" width="70%" valign="top"><img src="images/OnlineHeaderFinal.jpg" width="100%" height="100" align="top"/></td>
          </tr>
		  
		  <tr valign="bottom">
            <td colspan="2" height="10" width="100%"  align="left" bgcolor="#5D5D5D"></td>
		  </tr>
		
		</table>
	    </td>
	</tr>
  
  <%-- 
  
  
    <tr>
      <td height="87" colspan="3" valign="top">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="20%" height="25" rowspan="2" class="row-white" ><div align="left"><img src='<%=request.getContextPath()%>/TopBarServlet' alt="Logo not available" width="238" height="100"></div></td>
          <td width="80%"><div align="right"><img src="images/OnlineHeaderFinal.jpg" width="772" height="100"></div></td>
        </tr>
      </table>
      </td>
    </tr>
    <tr>
     
      <td width="100%" background="images/Orang_CurveBG.gif"><div align="left">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
			
				<tr><td width="15%" height="15" class="heading_white"></td></tr>
			
        </table>
      </div></td>
      
    </tr>
     <%if(session.getAttribute("serverDownMessage")!=null && !session.getAttribute("serverDownMessage").toString().isEmpty()){ %>
        <tr>
        <td width="100%"> <font color="#864b4f" size="3" FACE="BRITANNIC BOLD"><marquee scrollamount="3">
       <%=session.getAttribute("serverDownMessage") %>
        </marquee></font> </td>
        </tr>
        <%} %>
        
     --%>   
        
        
        
  </tbody>
</table>

<%-- 
<% if (KPPropertiesConfiguration.INSTITUTION .equalsIgnoreCase("GH")) {%>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-62455062-1', 'app.cimghaziabad.in');
  ga('send', 'pageview');
</script>
<%}%>
--%>
