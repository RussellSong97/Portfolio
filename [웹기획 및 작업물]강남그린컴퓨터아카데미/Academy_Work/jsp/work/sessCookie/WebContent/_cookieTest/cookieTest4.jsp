<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Cookie cookie = new Cookie("language", request.getParameter("language"));
	// 쿠키 생성(name이라는 이름을 가진 쿠키에 "hong-gildong"이라는 값을 지정)
	cookie.setMaxAge(60 * 60 * 24);		// 생성한 쿠키의 만료기간을 600초(10분)로 지정
	response.addCookie(cookie);	// 클라이언트에 생성한 쿠키를 보내어 클라이언트 PC에 저장시킴
%>   
<script>
	location.href = "cookieTest3.jsp" ;
</script>
