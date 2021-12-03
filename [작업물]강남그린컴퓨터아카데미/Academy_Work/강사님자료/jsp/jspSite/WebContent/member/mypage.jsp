<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

String[] arrPhone = login_info.getMi_phone().split("-");
int atPos = login_info.getMi_email().indexOf('@');
String e1 = login_info.getMi_email().substring(0, atPos);	// @ 전까지
String e2 = login_info.getMi_email().substring(atPos + 1);	// @ 후부터

String[] arrDomain = new String[5];
arrDomain[0] = "naver.com";
arrDomain[1] = "nate.com";
arrDomain[2] = "gmail.com";
arrDomain[3] = "daum.net";
arrDomain[4] = "yahoo.com";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>회원 정보 수정 폼</h2>
<form name="frmInfo" action="infoProc.jsp" method="post" onsubmit="return chkValue(this);">
<table border="1" cellpadding="5" width="700">
<tr>
<th width="20%">아이디</th>
<td width="*"><%=login_info.getMi_id() %></td>
</tr>
<tr><th>기존 비밀번호</th><td><input type="password" name="old_pwd" /></td></tr>
<tr><th>새 비밀번호</th><td><input type="password" name="mi_pwd" /></td></tr>
<tr><th>새 비밀번호 확인</th><td><input type="password" name="mi_pwd2" /></td></tr>
<tr><th>이름</th><td><%=login_info.getMi_name() %></td></tr>
<tr>
<th>생년월일</th>
<td><%=login_info.getMi_birth() %></td>
</tr>
<tr>
<th>성별</th>
<td><%=login_info.getMi_gender().equals("m") ? "남자" : "여자" %></td>
</tr>
<tr>
<th>전화번호</th>
<td>
	<select name="p1">
		<option value="010" <% if (arrPhone[0].equals("010")) { %>selected="selected"<% } %>>010</option>
		<option value="011" <% if (arrPhone[0].equals("011")) { %>selected="selected"<% } %>>011</option>
		<option value="016" <% if (arrPhone[0].equals("016")) { %>selected="selected"<% } %>>016</option>
		<option value="019" <% if (arrPhone[0].equals("019")) { %>selected="selected"<% } %>>019</option>
	</select> -
	<input type="text" name="p2" size="4" maxlength="4" value="<%=arrPhone[1] %>" /> -
	<input type="text" name="p3" size="4" maxlength="4" value="<%=arrPhone[2] %>" />
</td>
</tr>
<tr>
<th>이메일</th>
<td>
	<input type="text" name="e1" value="<%=e1 %>" /> @
	<select name="e2" onchange="setDomain(this.value);">
		<option value="">도메인 선택</option>
<% for (int i = 0 ; i < arrDomain.length ; i++) { %>
		<option value="<%=arrDomain[i] %>" <% if (e2.equals(arrDomain[i])) { %>selected="selected"<% } %>><%=arrDomain[i] %></option>
<% } %>
		<option value="direct">직접 입력</option>
	</select>
	<input type="text" name="e3" value="<%=e2 %>" />
</td>
</tr>
<tr>
<th>SNS 수신</th>
<td>
	<input type="radio" name="mi_issns" value="y" <% if (login_info.getMi_issns().equals("y")) { %>checked="checked"<% } %> />수신
	<input type="radio" name="mi_issns" value="n" <% if (login_info.getMi_issns().equals("n")) { %>checked="checked"<% } %> />미수신
</td>
</tr>
<tr>
<th>메일 수신</th>
<td>
	<input type="radio" name="mi_ismail" value="y" <% if (login_info.getMi_ismail().equals("y")) { %>checked="checked"<% } %> />수신
	<input type="radio" name="mi_ismail" value="n" <% if (login_info.getMi_ismail().equals("n")) { %>checked="checked"<% } %> />미수신
</td>
</tr>
</table>
<br />
<p style="width:700px; text-align:center;">
	<input type="submit" value="정보 수정" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</p>
</form>
</body>
</html>
