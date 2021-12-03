<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
test
<%
int i = 10, j = 20;
int result = i + j ;
out.println(result);

Calendar cal = Calendar.getInstance();
int hour = cal.get(Calendar.HOUR_OF_DAY);

%>
<h2>오늘 날짜는 <%=cal.get(Calendar.YEAR)%>년 <%=cal.get(Calendar.MONTH) + 1%>월 <%=cal.get(Calendar.DAY_OF_MONTH)%>이고, </h2>
<h3>현재 시간은 <%= cal.get(Calendar.HOUR_OF_DAY) %>시 <%= cal.get(Calendar.MINUTE) %>분 <%= cal.get(Calendar.SECOND) %>초 입니다. </h3>
현재는 <%= (hour < 12) ? "오전" : "오후"  %> 입니다.
</body>
</html>

 