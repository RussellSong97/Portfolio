<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="test.*" %>
<%
request.setCharacterEncoding("utf-8");
String uid = request.getParameter("uid");
String pwd = request.getParameter("pwd");
String name = request.getParameter("name");
String phone = request.getParameter("phone");
String point = request.getParameter("point");
int pnt = 0;
if (point != null)	pnt = Integer.parseInt(point);
// point가 비어있지 않으면 int형으로 형변환 후 pnt에 저장

MemberInfo mi = new MemberInfo();
mi.setUid(uid);		mi.setPwd(pwd);		mi.setName(name);
mi.setPhone(phone);	mi.setPoint(pnt);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>회원 정보 보기</h2>
<table>
<tr><th>아이디</th><td><%=mi.getUid() %></td></tr>
<tr><th>암호</th><td><%=mi.getPwd() %></td></tr>
<tr><th>이름</th><td><%=mi.getName() %></td></tr>
<tr><th>전화</th><td><%=mi.getPhone() %></td></tr>
<tr><th>포인트</th><td><%=mi.getPoint() %></td></tr>
</table>
</body>
</html>
