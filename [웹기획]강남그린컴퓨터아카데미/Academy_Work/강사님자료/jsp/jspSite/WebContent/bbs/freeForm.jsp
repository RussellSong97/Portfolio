<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
request.setCharacterEncoding("utf-8");
String wtype = request.getParameter("wtype");

String msg = null;
if (wtype.equals("in")) {	// 게시글 등록이면
	msg = "등록";
} else if (wtype.equals("up")) {	// 게시글 수정이면
	msg = "수정";
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
	
	} catch(Exception e) {
		out.println("자유 게시판 목록화면 작업 시 오류가 발생했습니다.");
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
<h2>자유 게시판 <%=msg %> 폼</h2>
<form name="frmForm" action="freeProc.jsp" method="post">
<input type="hidden" name="wtype" value="<%=wtype %>" />
<table width="700" cellpadding="5" cellspacing="0" id="brd">
<tr>
<% if (login_info == null) { %>
<th width="15%">이름</th><td width="35%"><input type="text" name="bfwriter" /></td>
<th width="15%">암호</th><td width="35%"><input type="password" name="bfpwd" /></td>
<% } else { %>
<input type="hidden" name="bfwriter" value="" />
<input type="hidden" name="bfpwd" value="" />
<th width="15%">이름</th><td width="*"><%=login_info.getMi_id() %></td>
<% } %>
</tr>
<tr>
<th>제목</th><td colspan="3"><input type="text" name="bftitle" /></td>
</tr>
<tr>
<th>내용</th><td colspan="3"><textarea name="bfcontent" cols="50" rows="10"></textarea></td>
</tr>
</table>
<p style="width:700px; text-align:center;">
	<input type="submit" value="게시글 <%=msg %>" />
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</p>
</form>
</body>
</html>
