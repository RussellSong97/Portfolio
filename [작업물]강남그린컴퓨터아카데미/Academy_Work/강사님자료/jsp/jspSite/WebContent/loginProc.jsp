<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

request.setCharacterEncoding("utf-8");
String uid = request.getParameter("uid");
String pwd = request.getParameter("pwd");

Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
String sql = "select * from t_member_info " + 
"where mi_id = '" + uid + "' and mi_pwd = '" + pwd + "' and mi_isactive = 'y'";

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	rs = stmt.executeQuery(sql);
	if (rs.next()) {	// rs에 데이터가 있으면(로그인 성공)
		MemberInfo mi = new MemberInfo();	// 회원 정보를 저장할 인스턴스 생성
		mi.setMi_id(uid);
		mi.setMi_pwd(pwd);
		mi.setMi_name(rs.getString("mi_name"));
		mi.setMi_birth(rs.getString("mi_birth"));
		mi.setMi_gender(rs.getString("mi_gender"));
		mi.setMi_phone(rs.getString("mi_phone"));
		mi.setMi_email(rs.getString("mi_email"));
		mi.setMi_issns(rs.getString("mi_issns"));
		mi.setMi_ismail(rs.getString("mi_ismail"));
		mi.setMi_rebank(rs.getString("mi_rebank"));
		mi.setMi_account(rs.getString("mi_account"));
		mi.setMi_recommend(rs.getString("mi_recommend"));
		mi.setMi_date(rs.getString("mi_date"));
		mi.setMi_isactive(rs.getString("mi_isactive"));
		mi.setMi_lastlogin(rs.getString("mi_lastlogin"));
		mi.setMi_point(rs.getInt("mi_point"));
		// 로그인에 성공한 회원정보를 담은 인스턴스 생성

		session.setAttribute("login_info", mi);	// 로그인 정보를 세션의 속성으로 저장
		out.println("<script>");
		out.println("location.href = 'index.jsp';");
		out.println("</script>");
	} else {	// rs에 데이터가 없으면(로그인 실패)
		out.println("<script>");
		out.println("alert('아이디와 암호를 확인하세요.');");
		out.println("history.back();");
		out.println("</script>");
	}

} catch(Exception e) {
	out.println("로그인 처리시 오류가 발생했습니다.");
	e.printStackTrace();
}
%>
