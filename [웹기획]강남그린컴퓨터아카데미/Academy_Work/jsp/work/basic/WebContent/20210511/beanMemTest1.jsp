<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>회원 정보 등록 폼</h2>
	<form action="beanMemTest2.jsp" method="post">
	<table>
		<tr><th>아이디</th><td><input type="text" name="uid" /></td></tr>
		<tr><th>암호</th><td><input type="text" name="pwd" /></td></tr>
		<tr><th>이름</th><td><input type="text" name="name" /></td></tr>
		<tr><th>전화</th><td><input type="text" name="phone" /></td></tr>
		<tr><th>포인트</th><td><input type="text" name="point" /></td></tr>
		<tr><td colspan="2"><input type="submit" value="전송" /></td></tr>
	</table>
	</form>
	<hr />
	
	<h2>회원 정보 등록 폼2</h2>
	<form action="beanMemTest3.jsp" method="post">
	<table>
		<tr><th>아이디</th><td><input type="text" name="uid" /></td></tr>
		<tr><th>암호</th><td><input type="text" name="pwd" /></td></tr>
		<tr><th>이름</th><td><input type="text" name="name" /></td></tr>
		<tr><th>전화</th><td><input type="text" name="phone" /></td></tr>
		<tr><th>포인트</th><td><input type="text" name="point" /></td></tr>
		<tr><td colspan="2"><input type="submit" value="전송" /></td></tr>
	</table>
	</form>
</body>
</html>
