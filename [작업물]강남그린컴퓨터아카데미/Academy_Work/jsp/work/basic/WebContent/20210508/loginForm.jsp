<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>로그인 폼 (GET방식)</h2>
	<form name="frmGet" action="loginProc" method="GET">
		아이디 : <input type="text" name="idGet" /> <br />
		비밀번호 : <input type="password" name="pwGet" /> <br />
		<input type="submit" value="로그인" />
	</form>
	<hr />
	
	<h2>아이디/비밀번호 찾기 폼(POST방식)</h2>
	<h3>아이디찾기</h3>
	<form name="frmFindID" action="loginProc" method="POST">
		<input type="hidden" nmame="idpw" value="id" />
		이름 : <input type="text" name="uname" /><br />
		이메일 : <input type="text" name="e1" /> @ 
		<select name="e2">
			<option value="naver.com">네이버</option>
			<option value="nate.com">네이트</option>
			<option value="gmail.com">지메일</option>
		</select><br />
		<input type="submit" value="아이디 찾기" />
	</form>
	
	<h3>비밀번호찾기</h3>
	<form name="frmFindPW" action="loginProc" method="POST">
		<input type="hidden" nmame="idpw" value="pw" />
		이름 : <input type="text" name="uname" /><br />
		아이디 : <input type="text" name="uid" /><br />
		이메일 : <input type="text" name="e1" /> @ 
		<select name="e2">
			<option value="naver.com">네이버</option>
			<option value="nate.com">네이트</option>
			<option value="gmail.com">지메일</option>
		</select><br />
		<input type="submit" value="비밀번호 찾기" />
	</form>
	<hr />
	
	<h2>JSP로 데이터 받기</h2>
	<form name="frm" action="loginProc.jsp" method="post" >
		데이터1 : <input type="text" name="data1" /><br />
		데이터2 : <input type="text" name="data2" /><br />
		<input type="submit" value="데이터 전송" />
	</form>
	<hr />
	
	<h2>한 이름의 여러 데이터 받기</h2>
	<form name="frmData" action="loginProc.jsp" method="post" >
		본인의 취미는? <br />
		<input type="checkbox" name="hobby" value="영화" />영화&nbsp;&nbsp;
		<input type="checkbox" name="hobby" value="음악" />음악&nbsp;&nbsp;
		<input type="checkbox" name="hobby" value="운동" />운동&nbsp;&nbsp;
		<input type="checkbox" name="hobby" value="등산" />등산&nbsp;&nbsp;
		<input type="checkbox" name="hobby" value="낚시" />낚시&nbsp;&nbsp;
		<input type="submit" value="데이터 전송" />
	</form>
</body>
</html>