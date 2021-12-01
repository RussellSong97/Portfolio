<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

Connection conn = null;
Statement stmt = null;
String sql = null;

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	conn.setAutoCommit(false);
	// conn의 autoCommit 값을 false로 지정하여 쿼리 실행 후 자동으로 적용되지 않게 설정 - 트랜잭션의 시작

	stmt = conn.createStatement();
	sql = "update t_admin_info set ai_name = 'aaa' where ai_id = 'abcd'";
	int result = stmt.executeUpdate(sql);
	if (result > 0) {
		conn.commit();		// 실행한 쿼리의 결과를 실제 테이블에 적용시킴
		out.println("쿼리를 commit() 시켰습니다.");
	} else {
		conn.rollback();	// 실행한 쿼리의 결과가 테이블에 적용되지 않게 취소 시킴
		out.println("쿼리를 rollback() 시켰습니다.");
	}
	conn.setAutoCommit(true);
	// conn의 autoCommit 값을 true로 지정하여 쿼리 실행 후 자동으로 적용되게 설정 - 트랜잭션의 종료

} catch(Exception e) {
	out.println("관리자 등록시 오류가 발생했습니다.");
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
