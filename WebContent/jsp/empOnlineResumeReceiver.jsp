<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@taglib uri="/WEB-INF/struts-tld/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-tld/c.tld" prefix="c" %>
<html:html>
<head>
<title>:: CMS ::</title>
<body>
<html:form action="empResumeSubmission" method="post">
<%
String reqPath=request.getContextPath();
pageContext.setAttribute("reqPath",reqPath);
%>
<html:hidden property="method" value=""/>
<html:hidden property="formName" value="empResumeSubmissionForm"/>
</html:form>
</body>
<script type="text/javascript">
	document.location.href="<c:out value='${reqPath}'/>/empResumeSubmission.do?method=initEmpResume";
</script>
</html:html>