<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;

MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");

request.setCharacterEncoding("utf-8");
int idx = Integer.parseInt(request.getParameter("idx"));		// 글번호
int cpage = Integer.parseInt(request.getParameter("cpage"));	// 페이지번호
String schType = request.getParameter("schType");
String keyword = request.getParameter("keyword");

String args = "?cpage=" + cpage, schargs = "";
if (schType != null && keyword != null) {
	URLEncoder.encode(keyword, "UTF-8");
	schargs = "&schType=" + schType + "&keyword=" + keyword;
	args += schargs;
} // 쿼리 스트링으로 URL에 넣을 값들을 args 변수에 담음

String bf_ismem = "", bf_pwd = "", bf_writer = "";
String bf_title = "", bf_content = "", bf_date = "";
int bf_read = 0;
try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();
	String sql = "select * from t_brd_free where bf_idx = " + idx;
	rs = stmt.executeQuery(sql);
	if (rs.next()) {
		bf_ismem 	= rs.getString("bf_ismem");	bf_writer	= rs.getString("bf_writer");
		bf_title	= rs.getString("bf_title");	bf_content	= rs.getString("bf_content");
		bf_date		= rs.getString("bf_date");	bf_read		= rs.getInt("bf_read");
	} else {
		out.println("<script>");
		out.println("alert('잘못된 경로로 들어오셨습니다.');");
		out.println("history.back();");
		out.println("</script>");
		out.close();
	}

} catch(Exception e) {
	out.println("자유 게시판 조회수 증가 작업 시 오류가 발생했습니다.");
	e.printStackTrace();
} finally {
	try {
		rs.close();		stmt.close();	conn.close();
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
<style>
#brd th, #brd td { border-bottom:1px black solid; }
</style>
</head>
<body>
<h2>자유 게시판 내용 보기</h2>
<table width="700" cellpadding="5" cellspacing="0" id="brd">
<tr>
<th width="15%">이름</th><td width="18%"><%=bf_writer %></td>
<th width="15%">작성일</th><td width="*"><%=bf_date %></td>
<th width="15%">조회수</th><td width="5%"><%=bf_read %></td>
</tr>
<tr><th>제목</th><td colspan="5"><%=bf_title %></td></tr>
<tr><th>내용</th><td colspan="5"><%=bf_content.replace("\r\n", "<br />") %></td></tr>
</table>
<p style="width:700px; text-align:center;">
<%
boolean isPermission = false;
String lnk1 = "", lnk2 = "";
if (bf_ismem.equals("n")) {	// 비회원 글이면
	isPermission = true;
	lnk1 = "location.href='freePwd.jsp" + args + "&wtype=up&idx=" + idx + "'";
	lnk2 = "location.href='freePwd.jsp" + args + "&wtype=del&idx=" + idx + "'";
} else if (login_info != null && login_info.getMi_id().equals(bf_writer)) {
// 현재 로그인한 회원이 작성자이면
	isPermission = true;
	lnk1 = "location.href='freeForm.jsp" + args + "&wtype=up&idx=" + idx + "'";
	lnk2 = "isDel();";
}

if (isPermission) {
	if (bf_ismem.equals("y")) {
%>
<script>
function isDel() {
	if (confirm("정말 삭제하시겠습니까?")) {
		location.href = "freeProc.jsp?wtype=del&idx=<%=idx%>";
	}
}
</script>
<%	} %>
	<input type="button" value="수 정" onclick="<%=lnk1 %>" />&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="삭 제" onclick="<%=lnk2 %>" />&nbsp;&nbsp;&nbsp;&nbsp;
<% } %>
	<input type="button" value="목 록" onclick="location.href='freeList.jsp<%=args %>';" />
</p>
</body>
</html>
