<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

Connection conn = null;
CallableStatement cs = null;
String sql = "{call sp_ad_admin_insert(?, ?, ?, ?)}";

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	cs = conn.prepareCall(sql);
	
	cs.setString(1, "admin4");
	cs.setString(2, "1111");
	cs.setString(3, "어드민사");
	cs.setString(4, "y");
	
	cs.execute();
	out.println("정상적으로 등록되었습니다.");

} catch(Exception e) {
	out.println("관리자 등록시 오류가 발생했습니다.");
	e.printStackTrace();
} finally {
	try {
		cs.close();
		conn.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
}
%>
