<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="vo.*"%>
<%
	MemberInfo loginMember = (MemberInfo) session.getAttribute("loginMember");
	//session에 들어있는 속성(attribute)은 기본적으로 Object이므로 받아오려면 해당 클래스형으로 형변환을 해야함
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%	if (loginMember == null) { %>
			<a href="login_form.jsp">로그인</a>
	<%	} else { %>
			<%=loginMember.getMi_id()%>(<%=loginMember.getMi_name()%>) 님 환영합니다.
			<br />
			<a href="logout.jsp">로그아웃</a>
	<% 	} %>
	<hr />
	<a href="member/join_form.jsp">회원가입</a>	
	<!—  'brd_list.ntc'로 매핑된 서블릿으로 이동 —>
	<hr />
	<a href="brd_list.ntc">공지사항</a>	
	<!—  'brd_list.ntc'로 매핑된 서블릿으로 이동 —>
	<hr /> 
	<a href="pdt_list.pdt">상품리스트</a>
	<hr />
	<a href="cart_list.ord">장바구니</a>
	<hr />
	<a href=""></a>
	<hr />
</body>
</html>