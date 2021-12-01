<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%! private String str = "JSP & Servlet"; // 멤버변수로 페이지내에서 자유롭게 접근 가능 %>
<%!
public String getString(String str) {
	if (str == null) {
		return "no";
	} else {
		return str;
	}
	// 멤버메소드로 페이지내에서 자유롭게 사용가능
}
%>
<hr />
<%
String ss = getString(str);
out.println(ss);
%>
</body>
</html>