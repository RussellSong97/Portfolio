<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

Connection conn = null;
PreparedStatement pstmt = null;
String sql = "insert into t_admin_info (ai_id, ai_pwd, ai_name) values (?, ?, ?)";
// PreparedStatement 를 사용하는 쿼리는 값들이 들어갈 자리에 '?'로 채워놓고 후에 파라미터로 전달함

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	pstmt = conn.prepareStatement(sql);
	
	pstmt.setString(1, "admin3");
	pstmt.setString(2, "1111");
	pstmt.setString(3, "어드민삼");
	
	int result = pstmt.executeUpdate();
	if (result > 0) {
		out.println("정상적으로 등록되었습니다.");
	} else {
		out.println("등록에 실패했습니다.");
	}

} catch(Exception e) {
	out.println("관리자 등록시 오류가 발생했습니다.");
	e.printStackTrace();
} finally {
	try {
		pstmt.close();
		conn.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
}
%>
