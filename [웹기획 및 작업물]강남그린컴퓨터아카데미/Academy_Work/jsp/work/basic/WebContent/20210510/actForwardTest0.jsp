<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>포워드 액션 테스트</h2>
<form action="actForwardTest1.jsp" method="post">
<input type="hidden" name="forwardPage" value="actForwardTest2.jsp" />
<table border="1">
<tr><th>이름</th><td><input type="text" name="name" /></td></tr>
<tr><th>나이</th><td><input type="text" name="age" /></td></tr>
<tr><th>주소</th><td><input type="text" name="addr" /></td></tr>
<tr><th colspan="2"><input type="submit" value="전송" /></th></tr>
</table>
</form>
</body>
</html>
