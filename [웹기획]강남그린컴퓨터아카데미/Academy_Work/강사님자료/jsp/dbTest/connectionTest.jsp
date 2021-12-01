<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%
Connection conn = null;
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

boolean isConnect = false;

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	isConnect = true;
	conn.close();

} catch(Exception e) {
	isConnect = false;
	e.printStackTrace();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2><% if (isConnect) { %>DB 연결 성공!!<% } else { %>DB 연결 실패<% } %></h2>
</body>
</html>
