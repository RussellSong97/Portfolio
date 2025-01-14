<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="mem.*" %>
<%
MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
String uid = null;	// 회원 아이디를 저장할 변수로 비회원일 시 null값으로 작업
if (login_info != null)	uid = login_info.getMi_id();

String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
String sql = null, sql2 = null, msg = null;

request.setCharacterEncoding("utf-8");
String wtype = request.getParameter("wtype");
String bfpwd = request.getParameter("bfpwd").trim();
String bfwriter = request.getParameter("bfwriter").trim();
String bftitle = request.getParameter("bftitle").trim();
String bfcontent = request.getParameter("bfcontent").trim();

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();

	int idx = 1;	// 게시글 번호를 저장할 변수
	String lnk = "freeList.jsp";	// 작업 후 이동할 경로
	if (wtype.equals("in")) {	// 게시글 등록일 경우
		msg = "등록이";
		// 1. 글번호 생성
		sql = "select max(bf_idx) + 1 from t_brd_free";
		rs = stmt.executeQuery(sql);
		if (rs.next())	idx = rs.getInt(1);
		if (uid != null)	bfwriter = uid;
		sql = "insert into t_brd_free (bf_idx, bf_ismem, bf_pwd, bf_writer, bf_title, " + 
			"bf_content) values (" + idx + ", '" + (uid == null ? 'n' : 'y') + "', '" + 
			bfpwd + "', '" + bfwriter + "', '" + bftitle + "', '" + bfcontent + "')";
		lnk = "freeList.jsp";

	} else {
		String tmpIdx = request.getParameter("idx");
		idx = Integer.parseInt(tmpIdx);

		if (wtype.equals("up")) {	// 주소 수정일 경우
			msg = "수정이";

		} else if (wtype.equals("del")) {	// 주소 삭제일 경우
			msg = "삭제가";

		}
	}

	int result = stmt.executeUpdate(sql);
	out.println("<script>");
	if (result > 0) {
		out.println("alert('정상적으로 " + msg + " 실행되었습니다.');");
		out.println("location.href = '" + lnk + "';");
	} else {
		out.println("alert('" + msg.substring(0, msg.length() - 1) + "에 실패했습니다.');");
		out.println("history.back();");
	}
	out.println("</script>");

} catch(Exception e) {
	out.println("자유게시판 작업 시 오류가 발생했습니다.");
	e.printStackTrace();
} finally {
	try {
		stmt.close();	conn.close();
	} catch(Exception e) {
		e.printStackTrace();
	}
}
%>
