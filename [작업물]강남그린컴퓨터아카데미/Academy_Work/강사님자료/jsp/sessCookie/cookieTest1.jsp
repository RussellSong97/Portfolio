<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
Cookie cookie = new Cookie("name", "hong-gildong");
// 쿠키 생성(name이라는 이름을 가진 쿠키에 "hong-gildong"이라는 값을 지정)
cookie.setMaxAge(600);		// 생성한 쿠키의 만료기간을 600초(10분)로 지정
response.addCookie(cookie);	// 클라이언트에 생성한 쿠키를 보내어 클라이언트 PC에 저장시킴
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 쿠키의 이름과 값, 만료기간을 각각 출력 -->
<h2><%=cookie.getName() %></h2>
<h2><%=cookie.getValue() %></h2>
<h2><%=cookie.getMaxAge() %></h2>
<a href="cookieTest2.jsp">저장된 쿠키 값 불러오기</a>
</body>
</html>
