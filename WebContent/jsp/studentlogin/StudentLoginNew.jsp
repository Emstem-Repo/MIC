<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html lang="en">

   <head>
   <%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<%@page import="com.kp.cms.constants.CMSConstants"%>
<%@taglib uri="/WEB-INF/cmsTags.tld" prefix="cms"%>
<title>Knowledge Pro | Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- CSS -->
        <link rel="stylesheet" href="js/loginPage Design/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="js/loginPage Design/css/form-elements.css">
        <link rel="stylesheet" href="js/loginPage Design/css/style.css">
        <link href="css/style.css" rel='stylesheet' type='text/css' />
        <link rel='stylesheet'  href="css/auditorium/start/jquery-ui.css" />
        <link rel="stylesheet" href="jquery/Zebra/css/default/zebra_dialog.css"  />
        <link rel="stylesheet" href="css/webticker.css"  />
        
    	<link rel="stylesheet" href="assets/online/css/style.css">
		<script src="jquery/development-bundle/jquery-1.7.1.js"></script>
		<script type='text/javascript' src="js/auditorium/jquery-ui.min.js"></script>
		<script type='text/javascript' src="jquery/Zebra/javascript/zebra_dialog.js"></script>
		<script type="text/javascript" src="js/jquery.webticker.js"></script>	
<style type="text/css">
.btn {
    width: 25%;
}
.form-box{
     background: rgba(0, 0, 0, 0.35) none repeat scroll 0 0;
    border-radius: 10px;
    box-shadow: 0 0 2px #aaa;
    border-image: none;
    align:right
  
}
body{
 	background-repeat: no-repeat;
 	background-size: cover;	
 	margin: auto;
 	overflow: hidden;
}
@media only screen and (max-width: 320px) {

   .btn {
    width: 15%;
}

}
.header {
 
    background: rgba(0, 0, 0, 0) linear-gradient(45deg, rgba(153, 116, 244, 1) 20%, rgba(255, 255, 255, 1) 61%, rgba(255, 255, 255, 1) 71%, rgba(255, 255, 255, 1) 95%) repeat scroll 0 0;
    width: 100%;
    height: 100px;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 100;
 opacity: 0.6;
  max-width: 100%;
	margin-bottom: 15px;
	border-color: white;
	border: 0px solid rgba(0, 0, 0, 0.5);
	display: table-row;
}
</style>
<style>
	.fa-3 {
      font-size: 2em;
    }
    .fa-3:hover {
      font-size: 2em;
      color: #FF892A!important;
    }
	</style> 
    </head>
    	<body onload="resetFieldAndErrMsgs()" id="wrapper">
    	<div class="header" align="left">
    	&nbsp;&nbsp;&nbsp;&nbsp;
<img src='images/StudentLoginLogos/header-logo.png' alt="Logo not available" width="350" height="100" align="right" ></div>
<html:form action="/StudentLoginAction">
	<html:hidden property="method" styleId="method" value="" />
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="pageType" value="1" />
	<html:hidden property="serverDownMessage" styleId="serverDownMessage" name="loginform" />

	
        <!-- Top content -->
        <div class="top-content">
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        	<p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            
                           
                        	 <div class="col-sm-6 col-sm-offset-8 form-box">
                        	<div >
                        			<H3 style="color:white;">Welcome Student/Parent !</H3>
							                            </div>
								<div style="height:24px;" align="right">
									<table >
									<tr> <td  >
									<div id="errorMessage" align="center"
										style="color: red; font-size: 17px;"><FONT color="red" ><html:errors /></FONT>
									<FONT color="green"> <html:messages id="msg" property="messages" message="true">
										<c:out value="${msg}" escapeXml="false"></c:out>
									</html:messages> </FONT></div>
									</td></tr>
									</table>
									</div>
								<div>
			                    <div class="login-form" style="line-height: 70px;">
			                    	<div class="form-group" >
			                    		<label class="sr-only" for="form-username">Username</label>
			                        	<html:text property="userName" styleId="username" size="20" maxlength="50" styleClass="form-username form-control" style="background: #eee ;"></html:text>
			                        </div>
			                        <div class="form-group" >
			                        	<label class="sr-only" for="form-password">Password</label>
			                        	<html:password property="password" styleId="password" size="20" styleClass="form-password form-control" maxlength="200" style="background: #eee ;"></html:password>
												                        </div>
										<div>
										<table width="100%">
											<tr>
												<td align="right">
												<button type="submit" class="btn" style="background: #ff6600;text-align: center;" id="Login"><b>Login</b></button>
												</td>
											</tr>
										</table>
										</div>
										</div>
		                    </div>
                        </div>
                    </div>
                </div>
                <br></br>
	<div class="col-sm-12 text" align="left"></div>
            </div>
        </div>
        </html:form>
        </div>
    </body>
<script src="js/studentlogin/studentLoginPageNew.js"></script>
</html>