<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;

request.setCharacterEncoding("utf-8");
int idx = Integer.parseInt(request.getParameter("idx"));		// 글번호
int cpage = Integer.parseInt(request.getParameter("cpage"));	// 페이지번호
String schType = request.getParameter("schType");
String keyword = request.getParameter("keyword");
String schargs = "";
if (schType != null && keyword != null) {
	URLEncoder.encode(keyword, "UTF-8");
	schargs = "&schType=" + schType + "&keyword=" + keyword;
}

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	String sql = "update t_brd_free set bf_read = bf_read + 1 where bf_idx = " + idx;
	int result = stmt.executeUpdate(sql);

} catch(Exception e) {
	out.println("자유 게시판 조회수 증가 작업 시 오류가 발생했습니다.");
	e.printStackTrace();
} finally {
	try {
		stmt.close();	conn.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
}
%>
<script>
	location.href = "freeView.jsp?idx=<%=idx%>&cpage=<%=cpage + schargs%>";
</script>
