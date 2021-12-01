<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%

request.setCharacterEncoding("utf-8"); 
MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");

String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

Connection conn = null;
Statement stmt = null;

String sql = "update t_member_info set mi_isactive = 'n' where mi_id = '" + login_info.getMi_id() + "'";

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	int result = stmt.executeUpdate(sql);
	
	out.println("<script>");
	if (result > 0) {	// 수정된 레코드가 있으면 (탈퇴가 성공했으면)
		out.println("alert('탈퇴처리 되었습니다.')");
		out.println("location.href = '../logout.jsp'");
	} else {
		out.println("alert('회원 탈퇴 실패했습니다.')");
		out.println("history.back();");
	}
	out.println("</script>");

} catch (Exception e) {
	out.println("회원 탈퇴 시 오류가 발생했습니다.");
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