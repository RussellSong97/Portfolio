<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="mem.*"%>
<%
	request.setCharacterEncoding("utf-8"); 
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");
	//로그인 여부와 로그인시 로그인 정보를 추출하기 위한 MemberInfo형 인스턴스 login_info에 세션의 속성을 저장
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Index</h2>
<%
	if (login_info == null) {
%>
	<a href="loginForm.jsp">로그인</a>&nbsp;&nbsp;
	<a href="member/joinForm.jsp">회원가입</a>
<%
	} else {
%>
	<%=login_info.getMi_name()%>(<%=login_info.getMi_id()%>) 님 환영합니다. 
	현재 회원님이 보유하신 포인트는 <%=login_info.getMi_point()%>point입니다.
	<br />
	<a href="logout.jsp">로그아웃</a>&nbsp;&nbsp;
	<a href="member/mypage.jsp">마이페이지</a>
<%
	}
%>
	<br />
	<hr />
	<br />
	<a href="bbs/freeList.jsp">자유 게시판(비회원 가능)</a>
	<a href="schedule.jsp">일정 관리</a>
</body>
</html>
