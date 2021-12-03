<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Request 객체 예제 폼</h2>
<form name="frm" action="requestTestProc.jsp" method="post">
이름 : <input type="text" name="name" /><br />
성별 : <input type="radio" name="gender" value="m" />남 
<input type="radio" name="gender" value="f" />여<br />
전화 : <input type="text" size="5" name="p1" /> -
<input type="text" size="5" name="p2" /> - <input type="text" size="5" name="p3" /><br />
<input type="submit" value="전송" />
</form>
</body>
</html>
