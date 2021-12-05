<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	request.setCharacterEncoding("utf-8");	
	MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
	if (login_info == null) {
		out.println("<script>");
		out.println("alert('로그인 후에 사용할 수 있습니다.');");
		out.println("location.href = 'loginForm.jsp';");
		out.println("</script>");
		out.close();
	}
	 
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	
	Connection conn = null;
	Statement stmt 	= null;  
	
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
	 
	
	} catch(Exception e) {
		out.println("오류가 발생했습니다.");
		e.printStackTrace();
	} finally {
		try {
			stmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>