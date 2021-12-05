<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
session.setMaxInactiveInterval(10);
// 세션 유지시간을 10초로 셋팅(기본값은 1800초)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
isNew() : <%=session.isNew() %><br />
생성시간 : <%=session.getCreationTime() %><br />
최종 접속시간 : <%=session.getLastAccessedTime() %><br />
세션ID : <%=session.getId() %><br />
세션유지시간 : <%=session.getMaxInactiveInterval() %>
</body>
</html>
