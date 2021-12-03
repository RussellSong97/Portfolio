<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>영역과 속성 테스트 3</h2>
<table border="1">
<tr><th colspan="2">Application 영역에 저장할 내용들</th></tr>
<tr><th>이름</th><td><%=application.getAttribute("name") %></td></tr>
<tr><th>아이디</th><td><%=application.getAttribute("id") %></td></tr>
</table>
<hr />
<table border="1">
<tr><th colspan="2">Session 영역에 저장할 내용들</th></tr>
<%
Enumeration e = session.getAttributeNames();
while(e.hasMoreElements()) {
	String attName = (String)e.nextElement();
	String attValue = (String)session.getAttribute(attName);
	out.println("<tr>");
	out.println("<th>" + attName + "</th>");
	out.println("<td>" + attValue + "</td>");
	out.println("</tr>");
}
%>
</table>
</body>
</html>
