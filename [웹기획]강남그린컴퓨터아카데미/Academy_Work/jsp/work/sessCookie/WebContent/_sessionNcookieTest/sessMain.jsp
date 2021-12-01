<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="test.*" %>
<%
	String name = null, uid = null;
	int point = 0;
	
	if (session.getAttribute("login_info") != null) {
		MemberInfo mi = (MemberInfo)session.getAttribute("login_info"); 
		// 세션의 속성으로 들어간 데이터는 Object이므로 사용시 해당 객체로 형 변환을 해야 사용할 수 있음
		uid = mi.getUid();
		name = mi.getName();
		point = mi.getPoint();
	} 
	

%>       
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% if (uid == null) { %>	<!-- 로그인이 안되어 있을 때   -->
		아직 로그인 전입니다.<br />
		<a href="sessLoginForm.jsp">로그인</a>
	<% } else { %> 				<!-- 로그인이 되어 있을 때  --> 
		<%=name %>(<%=uid %>) 님 환영합니다. 현재 보유중인 포인트는 <%=point %>포인트 입니다. <br />
		<a href="sessLogout.jsp">로그아웃</a> 
	
	<% } %>
</body>
</html>