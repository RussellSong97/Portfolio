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
ResultSet rs = null;
ResultSetMetaData rsmd = null;
String sql = "select mi_id, mi_name, if(mi_gender = 'm', '남자', '여자') gender, " + 
	"ifnull(mi_recommend, '없음') rec, if(mi_isactive = 'y', '사용중', '탈퇴') act " +
	" from t_member_info";

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	rs = stmt.executeQuery(sql);
	rsmd = rs.getMetaData();
	// rsmd에 rs의 메타데이터를 저장

} catch(Exception e) {
	out.println("관리자 등록시 오류가 발생했습니다.");
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
<h3>받아온 컬럼 수 : <%=rsmd.getColumnCount() %></h3>
<%
for (int i = 1 ; i <= rsmd.getColumnCount() ; i++) {
%>
<%=i %>번째 컬럼의 이름 : <%=rsmd.getColumnName(i) %> / 
레이블 : <%=rsmd.getColumnLabel(i) %> / 
데이터 타입 : <%=rsmd.getColumnTypeName(i) %><br />
<%
}
%>
</body>
</html>
