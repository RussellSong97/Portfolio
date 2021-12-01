<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*"%>
<%@ page import="java.util.*"%>
<%
	TestInfo brd  = (TestInfo)request.getAttribute("brd"); 
	if (brd == null) {
		out.println("<script>");
		out.println("alert('잘못된 경로로 들어오셨습니다.');");
		out.println("history.back();");
		out.println("</script>");
	}
	
	request.setCharacterEncoding("utf-8");
	int idx = Integer.parseInt(request.getParameter("idx"));
	int cpage = Integer.parseInt(request.getParameter("cpage"));
	String schtype = request.getParameter("schtype");
	String keyword = request.getParameter("keyword");
	String args = "?cpage=" + cpage;
	
	if (schtype != null && keyword != null && !keyword.equals("")) {
		args += "&schtype=" + schtype + "&keyword=" + keyword; 
	}	// 검색어가 있을 경우에만 쿼리스트링에 추가
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>보기</h2>
	<table width="700" cellpadding="5" cellspacing="0" id="brd">
		<tr>
			<th width="15%">제목</th>
			<td width="35%" colspan="3"><%=brd.getB_title() %></td>
			<th width="15%">작성자</th>
			<td width="35%" colspan="3"><%=brd.getB_writer() %></td>
		</tr>
		<tr>
			<th>작성일</th>
			<td colspan="3"><%=brd.getB_date() %></td>
			<th>조회수</th>
			<td colspan="3"><%=brd.getB_read() %></td>
		</tr>
		<tr>
			<th>내용</th>
			<td width="35%" colspan="8"><%=brd.getB_content().replace("\r\n", "<br />") %></td>
		</tr>
	</table>
	<table width="700" cellpadding="5" cellspacing="0" id="brd2">
		<tr>
			<td align="right">
				<input type="button" value="수정" onclick="location.href='test_form.brd<%=args %>&b_idx=<%=brd.getB_idx() %>'" />
				<input type="button" value="목록으로" onclick="location.href='test_list.brd<%=args %>'" />
			</td>
		</tr>
	</table>
</body>
</html>