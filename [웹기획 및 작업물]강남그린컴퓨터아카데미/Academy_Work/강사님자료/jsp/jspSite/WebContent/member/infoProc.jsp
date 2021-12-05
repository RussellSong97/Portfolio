<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
if (login_info == null) {
%>
<script>
	alert("잘못된 경로로 들어오셨습니다.");
	history.back();
</script>
<%
	out.close();	// 파일 실행을 강제로 종료시킴
}

request.setCharacterEncoding("utf-8");
String mi_id = login_info.getMi_id();
String old_pwd = request.getParameter("old_pwd");
String mi_pwd = request.getParameter("mi_pwd");
String p1 = request.getParameter("p1");
String p2 = request.getParameter("p2");
String p3 = request.getParameter("p3");
String mi_phone = p1 + "-" + p2 + "-" + p3;
String e1 = request.getParameter("e1");
String e3 = request.getParameter("e3");
String mi_email = e1 + "@" + e3;
String mi_issns = request.getParameter("mi_issns");
String mi_ismail = request.getParameter("mi_ismail");

String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;
String sql = "update t_member_info set ";
if (mi_pwd != null && !(mi_pwd.equals(""))) {
	sql += "mi_pwd = '" + mi_pwd + "', ";
}
sql += "mi_phone = '" 	+ mi_phone	+ "', mi_email = '"  + mi_email	+ "', "; 
sql += "mi_issns = '" 	+ mi_issns	+ "', mi_ismail = '" + mi_ismail+ "' ";
sql += " where mi_id = '" + mi_id + "' and mi_pwd = '" + old_pwd + "' ";

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	int result = stmt.executeUpdate(sql);

	out.println("<script>");
	if (result > 0) {	// 수정된 레코드가 있으면(수정이 성공했으면)
		if (mi_pwd != null && !(mi_pwd.equals(""))) {
			login_info.setMi_pwd(mi_pwd);
		}
		login_info.setMi_phone(mi_phone);
		login_info.setMi_email(mi_email);
		login_info.setMi_issns(mi_issns);
		login_info.setMi_ismail(mi_ismail);

		out.println("alert('정상적으로 수정되었습니다.');");
		out.println("location.href = 'mypage.jsp';");
	} else {
		out.println("alert('정보수정에 실패했습니다.');");
		out.println("history.back();");
	}
	out.println("</script>");

} catch(Exception e) {
	out.println("회원 정보수정 시 오류가 발생했습니다.");
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
