<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>포워드된 페이지(actForwardTest2.jsp)</h2>
<table border="1">
<tr><th>이름</th><td><%=request.getParameter("name") %></td></tr>
<tr><th>나이</th><td><%=request.getParameter("age") %></td></tr>
<tr><th>주소</th><td><%=request.getParameter("addr") %></td></tr>
<tr><th>전화</th><td><%=request.getParameter("tel") %></td></tr>
</table>
</body>
</html>
