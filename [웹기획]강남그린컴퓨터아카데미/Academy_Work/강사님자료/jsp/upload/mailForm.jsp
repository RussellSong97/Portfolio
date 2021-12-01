<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>메일 입력 폼</h3>
<form action="mailSend" method="post">
<table cellpadding="5">
<tr><th>보내는 사람 메일주소 : </th><td><input type="text" name="sender" /></td></tr>
<tr><th>받는 사람 메일주소 : </th><td><input type="text" name="receiver" /></td></tr>
<tr><th>메일 제목 : </th><td><input type="text" name="title" /></td></tr>
<tr>
<th>메일 내용 : </th>
<td><textarea name="content" cols="40" rows="10"></textarea></td>
</tr>
<tr><td colspan="2" align="center">
	<input type="submit" value="메일 보내기" />
</td></tr>
</table>
</form>
</body>
</html>
