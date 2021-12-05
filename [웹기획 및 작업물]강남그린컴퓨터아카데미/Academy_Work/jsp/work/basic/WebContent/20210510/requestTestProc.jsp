<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Request 객체 예제 결과</h2>
<%
request.setCharacterEncoding("utf-8");
Enumeration e = request.getParameterNames();
// 클라이언트의 요청중 컨트롤들의 이름들만 Enumeration형 인스턴스로 받아 옴
while(e.hasMoreElements()) {	// e에 사용할 요소가 남아 있으면
	String param = (String)e.nextElement();
	// e안의 데이터는 Object로 저장되어 있기 때문에 String으로 형변환 후 param에 저장
	out.println(param + " : " + request.getParameter(param) + "<br />");
	// param에 해당하는 데이터를 받아 출력
}
%>
</body>
</html>
