<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = "test1" ;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>include 액션 테스트 </h2>
	<jsp:include page="actIncludeTest2.jsp">
		<jsp:param name="name" value="hong-gildong" />
	</jsp:include>
<hr />
<%@ include file="actIncludeTest3.jsp" %>
</body>
</html>