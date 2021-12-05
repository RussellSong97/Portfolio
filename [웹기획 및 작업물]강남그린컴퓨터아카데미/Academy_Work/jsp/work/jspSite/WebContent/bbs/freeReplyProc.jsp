<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null, msg = null;

	request.setCharacterEncoding("utf-8");
	String wtype = request.getParameter("wtype");
	int idx = Integer.parseInt(request.getParameter("idx")); // 게시글 번호
	int cpage = Integer.parseInt(request.getParameter("cpage")); // 페이지번호
	String schType = request.getParameter("schType");
	String keyword = request.getParameter("keyword");
	String ord 		= request.getParameter("ord");						// 댓글 정렬 기준 

	String args = "?cpage=" + cpage + "&idx=" + idx;
	if (ord != null) args += "&ord=" + ord;
	if (schType != null && keyword != null) {
		URLEncoder.encode(keyword, "UTF-8");
		args += "&schType=" + schType + "&keyword=" + keyword;
	}

	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
		String sql2 = null;
		String rcontent = request.getParameter("rcontent");

		if (wtype.equals("in")) { // 댓글 등록이면
			sql  = " insert into t_brd_free_reply (bf_idx, mi_id, bfr_content) values (" + idx + ", '" + login_info.getMi_id() + "', '" + rcontent + "')";
			sql2 = " update t_brd_free set bf_reply = bf_reply + 1 where bf_idx = " + idx;
			// 게시판에서 해당 게시글의 댓글수를 1 증가시키는 쿼리

		} else if (wtype.equals("up")) { // 댓글 수정이면
			int bfridx = Integer.parseInt(request.getParameter("bfridx")); 
			sql = " update t_brd_free_reply set bfr_content = '" + rcontent +"' where bfr_idx = " + bfridx + " and mi_id = '" + login_info.getMi_id() + "'";
			
		} else if (wtype.equals("del")) { // 댓글 삭제이면
			int bfridx = Integer.parseInt(request.getParameter("bfridx"));  
			sql  = " delete from t_brd_free_reply where bfr_idx = " + bfridx + " and mi_id = '" + login_info.getMi_id() + "'";
			sql2 = " update t_brd_free set bf_reply = bf_reply - 1 where bf_idx = " + idx;	// 게시판에서 해당 게시글의 댓글수를 1 감소시키는 쿼리 
			
		} else {
			out.println("<script>");
			out.println("alert('잘못된 경로로 들어오셨습니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		System.out.println("sql : " + sql);
		int result = stmt.executeUpdate(sql);
		
		out.println("<script>");
		if (result > 0) {	// 댓글 추가 및 삭제 쿼리의 실행이 성공했으면
			System.out.println("sql2 : " + sql2);
			if (sql2 != null) stmt.executeUpdate(sql2); 	// 댓글 수 추가 및 감소 쿼리
			out.println("location.href = 'freeView.jsp" + args + "';");
			
		} else {
			out.println("alert('" + msg.substring(0, msg.length() - 1) + "에 실패했습니다.');");
			out.println("history.back();");
			
		}
		
		out.println("</script>");

	} catch (Exception e) {
		out.println("자유게시판 댓글 작업 시 오류가 발생했습니다.");
		e.printStackTrace();
	} finally {
		try {
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
%>

