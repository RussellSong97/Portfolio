<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
/* 
3. 로그인 폼 파일(login_form.jsp) 작성
  - 아이디와 암호를 입력받아 처리 파일로 서브밋
  - 아이디와 암호를 자바 스크립트로 검사 후 서브밋
    (검사 내용 : 입력여부, 4자 이상 입력의 2가지만 검사)
*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script>
	function onSubmit() {
		var uid = document.frmLogin.uid.value;
		var pwd = document.frmLogin.pwd.value;

		if (uid == "" || uid.length < 4) {
			alert("아이디를 4자 이상 입력해주세요.");
			return false;
		}
		
		if (pwd == "" || pwd.length < 4) {
			alert("비밀번를 4자 이상 입력해주세요.");
			return false;
		}
		
		document.frmLogin.submit();
	}
</script>
</head>
<body>
	<h2>로그인 폼</h2>
	<form name="frmLogin" action="login_proc.jsp" method="post">
		아이디 : <input type="text" name="uid" placeholder="아이디 입력" maxlength="20"/><br />
		비밀번호 : <input type="password" name="pwd" placeholder="비밀번호 입력" maxlength="20"/><br />
		<input type="button" value="로그인" onclick="onSubmit()" />
	</form>
</body>
</html>