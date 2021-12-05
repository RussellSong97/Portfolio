<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
String name = request.getParameter("name");
String gender = request.getParameter("gender");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<i>include 액션 방식 </i><br />
 	<b><%=name %></b><br />
 	<b><%=gender %></b>
</body>
</html>