<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
pageContext.setAttribute("pageScope", "pageValue");
request.setAttribute("requestScope", "requestValue");

pageContext.forward("attrTest6.jsp");
// dispatch방식으로 이동하므로 attrTest6.jsp에서도 동일한 request객체를 사용할 수 있음
// page영역의 속성은 사라지지만 request영역의 속성은 attrTest6.jsp에서도 사용가능
%>
</body>
</html>
