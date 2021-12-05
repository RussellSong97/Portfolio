<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

Connection conn = null;	// db와의 연결을 위한 Connection 객체 선언
Statement stmt = null;	// db에 쿼리를 보내기 위한 Statement 객체 선언
String sql = null;		// db에 보내기 위한 쿼리를 저장할 문자열 변수

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");	// db서버에 연결
	stmt = conn.createStatement();	// 쿼리를 db서버에 보낼 Statement 생성
	
	sql = "insert into t_admin_info (ai_id, ai_pwd, ai_name) ";
	sql += "values ('admin', '1111', '어드민')";
	
	int result = stmt.executeUpdate(sql);
	// 생성시킨 Statement 객체를 이용하여 insert문을 실행시킨 후 insert되 레코드의 개수를 리턴
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
		stmt.close();
		conn.close();
		// db관련 객체들 중 나중에 생성한 순으로 닫아줌
	} catch(Exception e) {
		e.printStackTrace();
	} // close() 메소드도 throws SQLException으로 선언되어 있으므로 예외처리를 하면서 실행해야 함
}
%>
