<%@ taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tld/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c"%>
<head>
<title>Knowledge Pro | Login</title>
<!--starts Css links -->
<link rel="stylesheet" href="css/Kprostyle.css"  />
<!--End Css links -->
</head>
<html:form action="/ParentLoginAction">
	<html:hidden property="method" styleId="method" value="parentLoginAction" />
	<html:hidden property="formName" value="loginform" />
	<html:hidden property="pageType" value="2" />
	<body onload="resetFieldAndErrMsgs()">
	<!--Start Body  -->
	<div id="container"  style="background-color: #FFE4B5">
	<div id="wrapper" style="background-color: #FFE4B5">
	<div id="LoginFormDiv" style="background-color: #FFE4B5">
	<!--Start Login form -->
	<div id="st_login" style="background-color: #FFE4B5">Parent Login
	</div>
	<div id="errorMessage" align="center">
             <FONT color="red"><html:errors/></FONT>
             <FONT color="green">
					<html:messages id="msg" property="messages" message="true">
							<c:out value="${msg}" escapeXml="false"></c:out>
					</html:messages>
			 </FONT>
	</div>
	<center>
	<form>
	<p align="center" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: #636161;">User Name:<html:text property="parentUserName" styleId="parentusername" size="30" maxlength="50" styleClass="text"></html:text></p>
	<p align="center" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: #636161;">Password:&nbsp;&nbsp;<html:password property="parentPassword" styleId="parentpassword" size="30" styleClass="password" maxlength="200"></html:password></p>
	<html:button property="" styleClass="buttonsubmit" value="Login" onclick="submitLogin()"/>
    <html:button property="" styleClass="buttonreset" value="Reset" onclick="resetFieldAndErrMsgs()"/>
	<!-- <input type="submit" value="Login"  /> <input type="reset" value="Reset" />-->
	<br />
	</form>
	<br />
	</center>
	<p align="right" >
	<%-- 
	 <% String path= request.getContextPath(); %>
	<a href='<%=path %>/ForgotPasswordServlet' style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: blue;"> Forgot Password</a></p>
	<!--End login form -->
	--%>
	<%--<div id="News">
	
	<iframe src="http://christuniversity.in/ticker.php"  width=220 height=150 marginwidth=0 
		marginheight=0 frameborder=0 scrolling=no style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 12px;font-weight: bold; color: green;"></iframe>
	
	
	Here define iframe to scroll the news contents 
	width less than 220px and height less than height:160px 
	
		</div>
	
		--%>
		
		</div>
	</div>
	
	</div>
	
	
	
	
	
	
	
	<!--End of body -->
	
	</body>
<script type="text/javascript">
var browserName=navigator.appName; 
if (browserName=="Microsoft Internet Explorer")
{
	 alert(" If Your using InternetExplorer Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling  or Use Mozilla FireFox");
}

/*var unameId=document.getElementById("username");
unameId.onfocus=function()
{
	if(unameId.value=="User Name")
	{
		unameId.value="";
	}
}
unameId.onblur=function()
{
	if(unameId.value.length==0 )
	{
		unameId.value="User Name";
	}
}
var passId=document.getElementById("password");
passId.onfocus=function()
{
	if(passId.value=="password")
	{
		passId.value="";
	}
}
passId.onblur=function()
{
	if(passId.value.length==0)
	{
		passId.value="password";
	}
}*/
function submitLogin()
{
	document.getElementById("method").value="parentLoginAction";
	document.loginform.submit();
}
function resetFieldAndErrMsgs()
{
	
	document.getElementById("parentusername").value="";
	document.getElementById("parentpassword").value="";
	document.getElementById("msg").value="";
}

</script>
</html:form>
