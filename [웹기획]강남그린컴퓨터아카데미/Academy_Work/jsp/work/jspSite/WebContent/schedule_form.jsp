<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%> 
<%@ page import="mem.*"%>
<%
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");
	if (login_info == null)  {   
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
	Statement stmt = null; 
	ResultSet rs = null;
	
	request.setCharacterEncoding("utf-8");
	String w = request.getParameter("w");
	String msg = null;
	Calendar today = Calendar.getInstance();
	int cYear = today.get(Calendar.YEAR);
	
	
	int y = 0, m = 0, d = 0, h = 0, n = 0;		// 연월일시분을 저장할 변수 
	int idx = 0;		// 수정시 사용할 일정번호를 저장
	String content = "";
	
	if (w.equals("in")) {			// 일정 등록일 경우
		msg = "등록";
		y = Integer.parseInt(request.getParameter("y"));		// 등록할 일정의 연도
		m = Integer.parseInt(request.getParameter("m")) + 1;	// 등록할 일정의 월
		d = Integer.parseInt(request.getParameter("d"));		// 등록할 일정의 일
		
	} else if (w.equals("up")) {	// 일정 수정일 경우
		msg = "수정";
		idx = Integer.parseInt(request.getParameter("idx"));	// 수정할 일정 번호
		 
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "1234");
			stmt = conn.createStatement();
			
			String sql = "select * from t_schedule_info where mi_id = '" + login_info.getMi_id() + " and si_idx = " + idx;
			System.out.println("sql : " + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				content = rs.getString("si_content");
				String si_date = rs.getString("si_stime");
				y =  Integer.parseInt(si_date.substring(0, 4));
				m =  Integer.parseInt(si_date.substring(5, 7));
				d =  Integer.parseInt(si_date.substring(8, 10));
				h =  Integer.parseInt(si_date.substring(11, 13));
				n =  Integer.parseInt(si_date.substring(14)); 
				
			} else {   
			   out.println("<script>");
			   out.println("alert('잘못된 경로로 들어 오셨습니다.');");
			   out.println("history.back();");
			   out.println("</script>");
			   out.close();
			} 
		
		} catch(Exception e) {
			out.println("일정관리 달력 작업 시 오류가 발생했습니다.");
			e.printStackTrace();
		} 
		
		
	} else {
	   out.println("<script>");
	   out.println("alert('로그인 후에 사용할 수 있습니다.');");
	   out.println("history.back();");
	   out.println("</script>");
	   out.close();
	}
 
	
	 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>일정 <%=msg %> 폼</h2>  
	<form name="frmSchedule" action="schedule_proc.jsp" method="post">
		<input type="hidden" name="w" value="<%=w %>" />
		<input type="hidden" name="idx" value="<%=idx %>" />
		<table width="700" cellpadding="5">
			<tr>
				<th width="15%">일시</th>
				<td>
					<select name="y">
					<% for (int i = 1988 ; i <= (cYear+1); i++) { %>
						<option value="<%=i %>" <%if (y == i) { %> selected = "selected" <% } %>><%=i %></option>
					<% } %>
					</select>년&nbsp;&nbsp;
					<select name="m">
					<% 
					for (int i = 1; i <= 12; i++) {
						String sn = i + "";
						if (i < 10) sn = "0" + sn;
					%>
						<option value="<%=sn %>" <%if (m == i) { %> selected = "selected" <% } %>><%=sn %></option>
					<% } %>
					</select>월&nbsp;&nbsp;
					<select name="d">
					<% 
					for (int i = 1; i <= 31; i++) {
						String sn = i + "";
						if (i < 10) sn = "0" + sn;
					%>
						<option value="<%=sn %>" <%if (d == i) { %> selected = "selected" <% } %>><%=sn %></option>
					<% } %>
					</select>일&nbsp;&nbsp;
					<select name="h">
					<% 
					for (int i = 0; i <= 23; i++) {
						String sn = i + "";
						if (i < 10) sn = "0" + sn;
					%>
						<option value="<%=sn %>" <%if (h == i) { %> selected = "selected" <% } %>><%=sn %></option>
					<% } %>
					</select>시&nbsp;&nbsp;
					<select name="n">
					<% 
					for (int i = 0; i <= 59; i++) {
						String sn = i + "";
						if (i < 10) sn = "0" + sn;
					%>
						<option value="<%=sn %>" <%if (n == i) { %> selected = "selected" <% } %>><%=sn %></option>
					<% } %>
					</select>분&nbsp;&nbsp;
				</td>
			</tr>
			<tr>
				<th>일정 내용</th>
				<td>
					<textarea name="content" rows="5" cols="60"><%=content %></textarea>
				</td>
			</tr>
			<tr> 
				<td colspan="2" align="center">
					<input type="submit" value="일정<%=msg %>" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="reset" value="다시 입력" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="목록으로" onclick="history.back();" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
