<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
String name = request.getParameter("name");
String id = request.getParameter("id");
if (name != null && id != null) {
	application.setAttribute("name", name);
	application.setAttribute("id", id);
	// application객체의 속성으로 name과 id를 지정
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>영역과 속성 테스트 2</h2>
<h3><%=name %>님 반갑습니다.<br /><%=name %>님의 아이디는 <%=id %>입니다.</h3>
<form name="frm" action="attrTest2.jsp" method="post">
<table border="1">
<tr><th colspan="2">Session 영역에 저장할 내용들</th></tr>
<tr><th>이메일</th><td><input type="text" name="mail" /></td></tr>
<tr><th>집주소</th><td><input type="text" name="addr" /></td></tr>
<tr><th>전화번호</th><td><input type="text" name="phone" /></td></tr>
<tr><td colspan="2"><input type="submit" value="전송" /></td></tr>
</table>
</form>
</body>
</html>
