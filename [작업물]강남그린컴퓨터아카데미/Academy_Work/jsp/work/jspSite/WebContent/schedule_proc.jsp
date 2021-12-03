<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	request.setCharacterEncoding("utf-8");	
	MemberInfo login_info = (MemberInfo)session.getAttribute("login_info");
	if (login_info == null) {
		out.println("<script>");
		out.println("alert('로그인 후에 사용할 수 있습니다.');");
		out.println("location.href = 'loginForm.jsp';");
		out.println("</script>");
		out.close();
	}
	 
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	
	Connection conn = null;
	Statement stmt 	= null;  
	String sql = null;
	

	request.setCharacterEncoding("utf-8");
	String w = request.getParameter("w");		// 일정 작업 종류(등록:in, 수정:up, 삭제:del)
	String y = null, m = null, d = null, h = null, n = null;		// 연월일시분
	String content = null, stime = null; 		// 일정내용과 시간을 저장할 변수
	
	y = request.getParameter("y");
	m = request.getParameter("m");
	String args = "?schYear=" + y + "&schMonth=" + (Integer.parseInt(m) - 1);
	if (w.equals("in") || w.equals("up")) {	// 일정 등록이나 수정일 경우
		d = request.getParameter("d");	// 일
		h = request.getParameter("h");	// 시
		n = request.getParameter("n");	// 분
		stime = y + "-" + m + "-" + d + " " + h + ":" + n;	// 일시		
		content = request.getParameter("content");			// 일정내용 
	}
	
	
	
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
	
		if (w.equals("in"))	 {		// 일정 등록일 경우
			sql = "insert into t_schedule_info (mi_id, si_stime, si_content) " + 
				" values ('" + login_info.getMi_id() + "', '" + stime + "', '" + content + "')";
			
 		} else {		// 일정 수정이나 삭제일 경우
			int idx = Integer.parseInt(request.getParameter("idx"));		// 일정 번호
			
			
			if (w.equals("up")) {
				sql = "update t_schedule_info set si_stime = '" + stime + "', si_content = '" + content + "' where mi_id = '" + login_info.getMi_id() + "' and si_idx = " + idx;
				
			} else if (w.equals("del")) {
				sql = "delete from t_schedule_info where mi_id = '" + login_info.getMi_id() + "' and si_idx = " + idx;
				
			} else {
			   out.println("<script>");
			   out.println("alert('잘못된 경로로 들어 오셨습니다.');");
			   out.println("history.back();");
			   out.println("</script>");
			   out.close();
			}
		}
		
		System.out.println("sql : " + sql);
		int result = stmt.executeUpdate(sql);
		out.println("<script>");
		if (result > 0) {	// 일정 추가 및 수정, 삭제 쿼리의 실행이 성공했으면
			out.println("location.href = 'schedule.jsp" + args + "';");
		} else {
			out.println("alert('오류가 발생했습니다.');");
			out.println("history.back();");
		}
		out.println("</script>");
	
	} catch(Exception e) {
		out.println("일정관리 달력 작업 시 오류가 발생했습니다.");
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