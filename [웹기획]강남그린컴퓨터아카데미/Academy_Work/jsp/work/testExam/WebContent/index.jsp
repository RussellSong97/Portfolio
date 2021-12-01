<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
/*
	2. 인덱스 화면 작성
	 - 로그인과 회원가입 링크 보여주기
	 - 로그인 시 '회원가입' 링크를 보여줌(그냥 a태그만 연결)
	 - 로그인 전에는 '로그인' 링크만 보여줌
*/

String id = null;
if ((String)session.getAttribute("login_id") != null) {
	id = (String)session.getAttribute("login_id") ;
}  
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

<% if (id == null) { %>
	<a href="login_form.jsp" >로그인</a>
<% } else { %> 		
	<a href="#">회원가입</a>
<% } %>
	<br /><hr />
	<a href="test_list.brd">글목록</a>
	<br /><hr />
	<a href="pdt_list.pdt">상품목록 화면</a>
	<br /><hr />
	<br />
</body>
</html>