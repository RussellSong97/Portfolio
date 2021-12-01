<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form name="frmLogin" action="login" method="post" onsubmit="return chkVal(this);">
		<table cellpadding="5">
			<tr>
				<th><label for="uid">아이디</label></th>
				<td><input type="text" name="uid" maxlength="20" /></td>
			</tr>
			<tr>
				<th><label for="pwd">비밀번호</label></th>
				<td><input type="password" name="pwd" maxlength="20" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="로그인" />&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="reset" value="다시 입력" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>