<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script>
	function chkVal(frm) {
		var uid = frm.uid.value;
		var pwd = frm.pwd.value;

		if (uid == "") {
			alert("아이디를 입력하세요");
			frm.uid.focus(); 	return false;
		} else if (uid.length < 4) {
			alert("아이디는 4자 이상 ");
			frm.uid.focus(); 	return false;
		}

		if (pwd == "") {
			alert("비밀번호를 입력하세요");
			frm.pwd.focus(); 	return false;
		} else if (uid.length < 4) {
			alert("비밀번호는 4자 이상 ");
			frm.pwd.focus(); 	return false;
		}
		
		return true;
	}
</script>
</head>
<body>
	<h3>로그인 폼</h3>
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