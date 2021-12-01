<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String id = "test1";
// http://localhost:8004/basic/actIncludeTest1.jsp?age=33&gender=f
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>include 액션 테스트</h2>
<jsp:include page="actIncludeTest2.jsp">
	<jsp:param name="name" value="hong-gildong" />
</jsp:include>
<hr />
<%@ include file="actIncludeTest3.jsp" %>
</body>
</html>
