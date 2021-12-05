<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
response.sendRedirect("responseTest2.jsp");
// 페이지를 로딩하다가 sendRecirect() 메소드를 만나면 지정한 곳으로 바로 이동함
%>
</body>
</html>
