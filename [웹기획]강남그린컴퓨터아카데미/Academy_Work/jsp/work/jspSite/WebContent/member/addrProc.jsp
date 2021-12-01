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

String driver = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
Connection conn = null;
Statement stmt = null;
String sql = null, sql2 = null, msg = null;

request.setCharacterEncoding("utf-8");
String wtype = request.getParameter("wtype");
String zip = request.getParameter("zip");
String addr1 = request.getParameter("addr1");
String addr2 = request.getParameter("addr2");

if (wtype.equals("in") || wtype.equals("up")) {	// 주소 등록이나 수정일 경우 사용자가 입력한 값 검사
	if (zip == null || zip.equals("") || addr1 == null || 
		addr1.equals("") || addr2 == null || addr2.equals("")) {
		out.println("<script>");
		out.println("alert('잘못된 경로로 들어오셨습니다.');");
		out.println("history.back();");
		out.println("/<script>");
		out.close();
	}
}

if (wtype.equals("in")) {			// 주소 등록일 경우
	msg = "등록이";
	sql = "insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2, ma_basic) " + 
		"values('" + login_info.getMi_id() + "', '" + zip + "', '" + addr1 + "', '" + addr2 + "', 'n')";

} else {
	String tmpIdx = request.getParameter("idx");
	int idx = Integer.parseInt(tmpIdx);

	if (wtype.equals("up")) {	// 주소 수정일 경우
		msg = "수정이";
		sql = " update t_member_addr set " +
			 	  " ma_zip = '"   + zip + "', " +
			 	  " ma_addr1 = '" + addr1 + "', " +
			 	  " ma_addr2 = '" + addr2 + "' " +
			  " where mi_id = '" + login_info.getMi_id() + "' and ma_idx = " + idx;

	} else if (wtype.equals("del")) {	// 주소 삭제일 경우
		msg = "삭제가";
		sql = "delete from t_member_addr where ma_basic = 'n' and mi_id = '" + login_info.getMi_id() + "' and ma_idx = " + idx;

	} else if (wtype.equals("basic")) {	// 기본 주소 설정일 경우
		msg = "기본주소 설정이";
		sql = "update t_member_addr set ma_basic = 'n' where mi_id = '" + login_info.getMi_id() + "' ";
		sql2 = "update t_member_addr set ma_basic = 'y' where mi_id = '" + login_info.getMi_id() + "' and ma_idx = " + idx;

	} else {	// wtype에 존재할 수 없는 값일 경우(url로 장난쳤을 경우)
		out.println("<script>");
		out.println("alert('잘못된 경로로 들어오셨습니다.');");
		out.println("history.back();");
		out.println("/<script>");
		out.close();
	}
}

try {
	Class.forName(driver);
	conn = DriverManager.getConnection(url, "root", "1234");
	stmt = conn.createStatement();

	int result = stmt.executeUpdate(sql);
	System.out.println("sql  : " + sql) ;
	
	if (wtype.equals("basic"))	result = stmt.executeUpdate(sql2);
	System.out.println("sql2 : " + sql2) ;
	// 기본주소 설정일 경우 실행해야 할 쿼리가 하나 더 있음

	out.println("<script>");
	if (result > 0) {
		out.println("alert('정상적으로 " + msg + " 실행되었습니다.');");
		out.println("location.href = 'addrForm.jsp';");
	} else {
		out.println("alert('" + msg.substring(0, msg.length() - 1) + "에 실패했습니다.');");
		out.println("history.back();");
	}
	out.println("</script>");

} catch(Exception e) {
	out.println("주소록 목록 호출 시 오류가 발생했습니다.");
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


