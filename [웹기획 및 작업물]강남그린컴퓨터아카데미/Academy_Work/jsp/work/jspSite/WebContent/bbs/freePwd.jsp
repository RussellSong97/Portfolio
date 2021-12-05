<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.*"%>
<%
	request.setCharacterEncoding("utf-8");
	int idx = Integer.parseInt(request.getParameter("idx")); // 글번호
	int cpage = Integer.parseInt(request.getParameter("cpage")); // 페이지번호
	String wtype = request.getParameter("wtype");
	String schType = request.getParameter("schType");
	String keyword = request.getParameter("keyword");

	String args = "?cpage=" + cpage, schargs = "";
	if (schType != null && keyword != null) {
		URLEncoder.encode(keyword, "UTF-8");
		schargs = "&schType=" + schType + "&keyword=" + keyword;
		args += schargs;
	} // 쿼리 스트링으로 URL에 넣을 값들을 args 변수에 담음

	String lnk = "";
	if (wtype.equals("up")) {
		lnk = "freeForm.jsp" + args;
	} else if (wtype.equals("del")) {
		lnk = "freeProc.jsp";
	} else {
		out.println("<script>");
		out.println("alert('잘못된 경로로 들어오셨습니다.');");
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
<script>
function isDel() {
	if (confirm("정말 삭제하시겠습니까?")) {
		return true; // 삭제할 경우에만 폼을 서브밋
	}
	return false;
}
</script>
</head>
<body> 
	<h3>비밀번호 입력 폼</h3>
	<form name="frmPwd" action="<%=lnk%>" method="post">
		<input type="hidden" name="wtype" value="<%=wtype%>" /> <input
			type="hidden" name="idx" value="<%=idx%>" /> <input type="password"
			name="pwd" /> <input type="submit" value="입력" />
	</form> 
</body>
</html>
