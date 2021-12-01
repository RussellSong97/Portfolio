<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;

request.setCharacterEncoding("utf-8");
int cpage = 1, psize = 2, bsize = 5, rcnt = 0, pcnt = 0;
// 현재 페이지번호, 페이지크기, 블록크기, 게시글 개수, 페이지개수를 저장한 변수

String tmpPage = request.getParameter("cpage");
if (tmpPage != null)	cpage = Integer.parseInt(tmpPage);
// 받아온 페이지 번호가 있으면 무조건 정수이므로 int형으로 형변환

String schType = request.getParameter("schType");	// 검색조건
String keyword = "";
if (schType != null)	keyword = request.getParameter("keyword").trim();
// 검색조건 중 schType의 값이 있을 경우에만 검색어(keyword)를 받아옴
else	schType = "";

String schargs = ""; // 검색관련 쿼리스트링을 저장할 변수
String where = "";	// 검색조건이 있을 경우 where절을 저장할 변수
if (!schType.equals("") && !keyword.equals("")) {	// 검색조건과 검색어가 있을 경우
	URLEncoder.encode(keyword, "UTF-8");
	// 쿼리스트리으로 주고 받는 검색어가 한글일 경우 IE에서 간혹 문제가 발생하므로 유니코드로 전환시켜 사용

	if (schType.equals("tc")) {	// 검색조건이 '제목+내용'일 경우
		where = " where bf_title like '%" + keyword + 
			"%' or bf_content like '%" + keyword + "%' ";
	} else {
		where = " where bf_" + schType + " like '%" + keyword + "%' ";
	}
	schargs += "&schType=" + schType + "&keyword=" + keyword;
}

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();

	// 전체 게시글의 개수를 받아오는 쿼리
	String sql = "select count(*) from t_brd_free " + where;
	rs = stmt.executeQuery(sql);
	rs.next();	rcnt = rs.getInt(1);
	// count()함수를 사용하므로 데이터의 유무는 검사할 필요가 없으나 데이터를 사용하려면 next()를 해야 함

	pcnt = rcnt / psize;
	if (rcnt % psize > 0)	pcnt++;	// 전체 페이지 개수

	int snum = (cpage - 1) * psize;	// limit 절의 시작 인덱스 번호
	sql = "select bf_idx, bf_writer, bf_title, bf_reply, bf_read, bf_date " + 
	" from t_brd_free " + where + " order by bf_idx desc limit " + snum + ", " + psize;
	rs = stmt.executeQuery(sql);

} catch(Exception e) {
	out.println("자유 게시판 목록화면 작업 시 오류가 발생했습니다.");
	e.printStackTrace();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#brd th { border-bottom:3px black double; }
#brd td { border-bottom:1px black solid; }
</style>
<script>
function goPage(page) {
	location.href = "freeList.jsp?cpage=" + page + "<%=schargs%>";
}
</script>
</head>
<body>
<h2>자유 게시판 목록 화면</h2>
<table width="700" cellpadding="5" cellspacing="0" id="brd">
<tr>
<th width="8%">번호</th><th width="*">제목</th>
<th width="15%">작성자</th><th width="10%">조회수</th><th width="15%">작성일</th>
</tr>
<%
boolean isBrd = false;
int num = 0;
if (rs.next()) {
	isBrd = true;
	num = rcnt - (psize * (cpage - 1));
	do {
		String title = rs.getString("bf_title");
		String reply = "";
		if (title.length() > 24)	title = title.substring(0, 24) + "...";
		if (rs.getInt("bf_reply") > 0) {
			reply = " [" + rs.getInt("bf_reply") + "]";
			if (title.length() > 22)	title = title.substring(0, 22) + "...";
		}
		String lnk = "<a href='freePreView.jsp?idx=" + rs.getInt("bf_idx") + 
			"&cpage=" + cpage + schargs + "'>";
%>
<tr align="center">
<td><%=num %></td>
<td align="left"><%=lnk + title + "</a>" + reply %></td>
<td><%=rs.getString("bf_writer") %></td>
<td><%=rs.getInt("bf_read") %></td>
<td><%=rs.getString("bf_date").substring(2, 11).replace('-', '.') %></td>
</tr>
<%
		num--;
	} while (rs.next());
} else {
	out.println("<tr><td colspan='5' align='center'>검색결과가 없습니다.</td></tr>");
}
%>
</table><br />
<table width="700" cellpadding="5" cellspacing="0">
<tr>
<td width="*" align="center">
<%
if (isBrd) {	// 게시글이 있으면 페이징 영역을 보여줌 
	if (cpage == 1) {
		out.println("<<&nbsp;&nbsp;<&nbsp;&nbsp;");
	} else {
		out.println("<a href='javascript:goPage(1);'>&lt;&lt;</a>&nbsp;&nbsp;");
		out.println("<a href='javascript:goPage(" + (cpage - 1) + ");'>&lt;</a>&nbsp;&nbsp;");
	}

	int spage = (cpage - 1) / bsize * bsize + 1;
	for (int i = 1, j = spage ; i <= bsize && j <= pcnt ; i++, j++) {
	// i : 블록에서 보여줄 페이지의 개수만큼 루프를 돌릴 조건으로 사용되는 변수
	// j : 실제 출력할 페이지 번호로 전체 페이지 개수(마지막 페이지 번호)를 넘지 않게 사용해야 함
		if (cpage == j) {
			out.println("&nbsp;<strong>" + j + "</strong>&nbsp;");
		} else {
			out.println("&nbsp;<a href='javascript:goPage(" + j + ");'>" + j + "</a>&nbsp;");
		}
	}

	if (cpage == pcnt) {
		out.println("&nbsp;&nbsp;>&nbsp;&nbsp;>>");
	} else {
		out.println("<a href='javascript:goPage(" + (cpage + 1) + ");'>&gt;</a>&nbsp;&nbsp;");
		out.println("<a href='javascript:goPage(" + pcnt + ");'>&gt;&gt;</a>");
	}
}
%>
</td>
<td width="10%" align="right">
	<input type="button" value="글쓰기" onclick="location.href='freeForm.jsp?wtype=in';" />
</td>
</tr>
<form name="frmSch" method="get">
<tr align="right">
<td colspan="2">
	<select name="schType">
		<option value="title" <% if (schType.equals("title")) { %>selected="selected"<% } %>>제목</option>
		<option value="content" <% if (schType.equals("content")) { %>selected="selected"<% } %>>내용</option>
		<option value="tc" <% if (schType.equals("tc")) { %>selected="selected"<% } %>>제목+내용</option>
		<option value="writer" <% if (schType.equals("writer")) { %>selected="selected"<% } %>>작성자</option>
	</select>
	<input type="text" name="keyword" value="<%=keyword %>" />
	<input type="submit" value="검 색" />
</td>
</tr>
</form>
</table>
</body>
</html>
