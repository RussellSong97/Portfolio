<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String language = "korean", cook = request.getHeader("Cookie");
if (cook != null) {
	Cookie[] cookies = request.getCookies();
	for (int i = 0 ; i < cookies.length ; i++) {
		if (cookies[i].getName().equals("language")) {
			language = cookies[i].getValue();
		}
	}
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% if (language.equals("korean")) { %>
<h3>안녕하세요. 이것은 쿠키 예제입니다.</h3>
<% } else { %>
<h3>Hello. This is Cookie example.</h3>
<% } %>
<form action="cookieTest4.jsp" method="post">
<input type="radio" name="language" value="korean" id="ko" <% if (language.equals("korean")) { %>checked="checked"<% } %>/>
<label for="ko">한국어 페이지 보기</label>&nbsp;&nbsp;&nbsp;
<input type="radio" name="language" value="english" id="en" <% if (language.equals("english")) { %>checked="checked"<% } %>/>
<label for="en">영어 페이지 보기</label><br />
<input type="submit" value="언어 설정" />
</form>
</body>
</html>
