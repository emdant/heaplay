<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String error = (String) request.getAttribute("errorMessage");
	String email = (String) request.getAttribute("email");
	String username = (String) request.getAttribute("username");
	if(email == null)
		email = "";
	if(username == null)
		username = "";
	/*String previousUrl = request.getRequestURI();
	if(!previousUrl.startsWith("/login"))
		session.setAttribute("afterLoginRedirect", previousUrl);
	*/
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<%@ include file="WEB-INF/views/links.jsp"%>
<script src="${pageContext.servletContext.contextPath}/js/validate.js"></script>
</head>
<body>
<jsp:include page="WEB-INF/views/header.jsp" />
<form action="register" onsubmit="return validateRegister(this)" method="post">
	<label for="username">Username: </label>
	<input type="text" name="username" value="<%=username%>" placeholder="username" required><br/>
	<label for="email">E-Mail: </label>
	<input type="text" name="email" value="<%=email%>" required><br/>
	<label for="password">Password: </label>
	<input type="password" name="password" value="" required><br/>
	
	<input type="submit" value="Register">
</form>
<%	if( error != null) {%>
		<%=error%>
<%	} %>	
</body>
</html>