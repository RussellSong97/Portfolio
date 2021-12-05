<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="vo.*"%>
<%
	AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
	if (adminInfo == null) {
		out.println("<script>");
		out.println("alert('로그인 후 사용하실 수 있습니다.');");
		out.println("location.href='login_form.jsp';");
		out.println("</script>");
		out.close();
	}

	String pms = adminInfo.getAi_pms(); // 관리자 권한
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%=adminInfo.getAi_id()%>님, 환영합니다.<br /><br />
	
	<% if (pms.equals("a") || pms.equals("b")) { %>
	<a href="member_list.mema">회원 관리</a> <hr />
	<% } %>
	
	<% if (pms.equals("a") || pms.equals("b")) { %>
	<a href="notice_list.ntca">공지사항 관리</a><hr />
	<% } %>
	
	<% if (pms.equals("a") || pms.equals("c")) { %>
	<a href="pdt_list.pdta">상품 관리</a> <hr />
	<% } %>
	
	<% if (pms.equals("a") || pms.equals("d")) { %>
	<a href="order_list.orda">주문 관리</a> <hr />
	<% } %>
	
	<a href="../logout.jsp">로그 아웃</a> <hr />
</body>
</html>
