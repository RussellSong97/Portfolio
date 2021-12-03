<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%
AdminInfo adminInfo = (AdminInfo)session.getAttribute("adminInfo");
if (adminInfo != null) {
	out.println("<script>");
	out.println("alert('잘못된 경로로 들어오셨습니다.');");
	out.println("history.back();");
	out.println("</script>");
	out.close();
}

request.setCharacterEncoding("utf-8");
String url = request.getParameter("url");	// 로그인 후 이동할 페이지 주소(없는 경우도 있음)
if (url == null)	url = "";	// url값이 없으면 빈 문자열로 변환하여 사용
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
function chkVal(frm) {
	var uid = frm.uid.value;	var pwd = frm.pwd.value;
	if (uid == "") {
		alert("아이디를 입력하세요.");	frm.uid.focus();	return false;
	}
	if (pwd == "") {
		alert("비밀번호를 입력하세요.");
		frm.pwd.focus();	return false;
	} else if (pwd.length < 4) {
		alert("비밀번호는 4자 이상 입력해야 합니다.");
		frm.pwd.select();	return false;
	}

	return true;
}
</script>
</head>
<body>
<h3>로그인 폼</h3>
<form name="frmLogin" action="../adminLogin" method="post" onsubmit="return chkVal(this);">
<input type="hidden" name="url" value="<%=url %>" />
<table cellpadding="5">
<tr>
<th><label for="uid">아이디</label></th>
<td><input type="text" name="uid" id="uid" maxlength="20" value="sa" /></td>
</tr>
<tr>
<th><label for="pwd">비밀번호</label></th>
<td><input type="password" name="pwd" id="pwd" maxlength="20" value="1111" /></td>
</tr>
<tr><td colspan="2" align="center">
	<input type="submit" value="로그인" />&nbsp;&nbsp;
	&nbsp;&nbsp;<input type="reset" value="다시 입력" />
</td></tr>
</table>
</form>
</body>
</html>
