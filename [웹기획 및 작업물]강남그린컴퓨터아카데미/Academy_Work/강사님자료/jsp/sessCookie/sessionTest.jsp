<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String name;
// session객체에 'name'이라는 속성값이 존재하면 그 값을 name에 저장하고, 없으면 '세션값 없음'을 저장
if (session.getAttribute("name") != null) {
	name = (String)session.getAttribute("name");
} else {
	name = "세션값 없음";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>세션 테스트</h2>
<p>세션 ID : <%=session.getId() %></p>
<input type="button" value="세션 값 저장" onclick="location.href='sessionSet.jsp';" />
<input type="button" value="세션 값 삭제" onclick="location.href='sessionDel.jsp';" />
<input type="button" value="세션 초기화" onclick="location.href='sessionInvalidate.jsp';" />
<h3><%=name %></h3>
</body>
</html>
