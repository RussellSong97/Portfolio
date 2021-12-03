<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String sql = null;

	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");

	request.setCharacterEncoding("utf-8");
	int idx 		= Integer.parseInt(request.getParameter("idx"));	// 글번호
	int cpage 		= Integer.parseInt(request.getParameter("cpage")); 	// 페이지번호
	String schType 	= request.getParameter("schType");					// 검색구분
	String keyword 	= request.getParameter("keyword");					// 검색어
	String ord 		= request.getParameter("ord");						// 댓글 정렬 기준 
		
	String args 	= "?cpage=" + cpage, schargs = "";					// 페이징 파라미터
	if (schType != null && keyword != null) {							// 쿼리 스트링으로 URL에 넣을 값들을 args 변수에 담음
		URLEncoder.encode(keyword, "UTF-8");
		schargs = "&schType=" + schType + "&keyword=" + keyword;
		args += schargs;
	}

	String bf_ismem = "", bf_pwd = "", bf_writer = "";
	String bf_title = "", bf_content = "", bf_date = "";
	int bf_read = 0;
	
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();
		sql = "select * from t_brd_free where bf_idx = " + idx;
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			bf_ismem = rs.getString("bf_ismem");
			bf_writer = rs.getString("bf_writer");
			bf_title = rs.getString("bf_title");
			bf_content = rs.getString("bf_content");
			bf_date = rs.getString("bf_date");
			bf_read = rs.getInt("bf_read");
		} else {
			out.println("<script>");
			out.println("alert('잘못된 경로로 들어오셨습니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

	} catch (Exception e) {
		out.println("자유 게시판 조회수 증가 작업 시 오류가 발생했습니다.");
		e.printStackTrace();
	}
	 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	#brd th, #brd td {
		border-bottom: 1px black solid;
	}
	
	#reply td {
		border-bottom: 2px #efefef solid;
	}
</style>
<script>
	function replyUp(bfridx) {
		var cont = eval("document.frm.cont" + bfridx + ".value");
		var frm = document.frmReply;
		frm.rcontent.value = cont;
		frm.wtype.value = "up";
		frm.bfridx.value = bfridx;
	}
	
	function replyDel(bfridx) {
		if (confirm("정말로 삭제하시겠습니까?")) {
			var frm = document.frmReply; 
			frm.wtype.value = "del";
			frm.bfridx.value = bfridx;
			frm.submit();
		}
	}
	 

</script>
</head>
<body>
	<h2>자유 게시판 내용 보기</h2>
	
	<table width="700" cellpadding="5" cellspacing="0" id="brd">
		<tr>
			<th width="15%">이름</th>
			<td width="18%"><%=bf_writer%></td>
			<th width="15%">작성일</th>
			<td width="*"><%=bf_date%></td>
			<th width="15%">조회수</th>
			<td width="5%"><%=bf_read%></td>
		</tr>
		<tr>
			<th>제목</th>
			<td colspan="5"><%=bf_title%></td>
		</tr>
		<tr>
			<th>내용</th>
			<td colspan="5"><%=bf_content.replace("\r\n", "<br />")%></td>
		</tr>
	</table>
	
	<p style="width: 700px; text-align: center;">
		<%
			boolean isPermission = false;
			String lnk1 = "", lnk2 = "";
			if (bf_ismem.equals("n")) { // 비회원 글이면
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
		<%
			}
		%>
		<input type="button" value="수 정" onclick="<%=lnk1%>" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="삭 제" onclick="<%=lnk2%>" />&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			}
		%>
		<input type="button" value="목 록" onclick="location.href='freeList.jsp<%=args%>';" />
	</p>
	<!-- 댓글 관련 기능 -->
	<form name="frmReply" action="freeReplyProc.jsp<%=args%>" method="post">
		<input type="hidden" name="wtype" value="in" /> 
		<input type="hidden" name="idx" value="<%=idx%>" />
		<input type="hidden" name="bfridx" value="" />
		<input type="hidden" name="ord" value="<%=ord %>" />
		<div id="reply" style="width: 700px; height: 60px; border: 1px solid black; padding: 5px;">
			<textarea name="rcontent" style="width: 550px; height: 50px; float: left;" <%if (login_info == null) {%> onfocus="alert('로그인 후에 사용하실 수 있습니다.'); this.blur();" <%}%>><% if (login_info == null) { // 로그인을 하지 않은 경우 %>댓글 기능은 로그인 후에 사용할 수 있습니다.<% } %></textarea>
			<input type="submit" value="댓 글" style="width: 130px; height: 55px; margin-left: 10px;" />
		</div>
	</form>
	<hr align="left" style="width: 700px;" />
	<%
	ord = request.getParameter("ord");		// 댓글 정렬 기준
	if (ord == null) ord = "asc";
	
	out.println("댓글 정렬 : ");
	if (ord.equals("asc")) {
		out.println("▲");
		out.println("<a href='freeView.jsp"+args + "&idx=" + idx + "&ord=desc'>▽</a>");	
	} else { 
		out.println("<a href='freeView.jsp"+args + "&idx=" + idx + "&ord=asc'>△</a>");
		out.println("▼");	
	}
	%> 
	<br />
	<form name="frm" >
	<%
		try {
			
			sql = "select * from t_brd_free_reply where bf_idx = " + idx + " order by bfr_idx " + ord;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				boolean isPms = false;
				out.println("<table width='700' cellpadding='10' cellspacing='0' id='reply'>");
				do {
					isPms = false;
					if (login_info != null && rs.getString("mi_id").equals(login_info.getMi_id())) {
						// 댓글 쓴 사람과 로그인한 사람이 같은지 확인
						isPms = true;
					}
	%>
	<tr>
		<td width="15%"><%=rs.getString("mi_id")%></td>
		<td width="*"><%=rs.getString("bfr_content").replace("\r\n", "<br />")%></td>
		<td width="26%" align="right" valign="top">
			<%=rs.getString("bfr_date").substring(5)%> 
			<a href="javascript: <% if(isPms) { %> replyUp(<%=rs.getInt(1) %>); <%	} else { %> alert('권한이 없습니다.'); <% } %>">[u]</a> 
			<a href="javascript: <% if(isPms) { %> replyDel(<%=rs.getInt(1) %>); <%	} else { %> alert('삭제 권한이 없습니다.'); <% } %>">[x]</a>
			<!-- 작성자가 아니면 "권한이 없습니다." 작성자면 대기 -->
		</td>
	</tr>
	<input type="hidden" name="cont<%=rs.getInt(1) %>" value="<%=rs.getString("bfr_content")%>" />
	<%
		} while (rs.next());
				out.println("</table>");
			} else {
	%>
	<div id="reply"
		style="width: 700px; height: 60px; border: 1px solid black; text-align: center;">
		<br />등록된 댓글이 없습니다.
	</div>
	<%
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	%>
	</form>
	<br />
	<br />
</body>
</html>
