<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("utf-8");
String data1 = request.getParameter("data1");
String data2 = request.getParameter("data2"); 
String[] arrHobby = request.getParameterValues("hobby");
// hobby라는 이름을 가진 컨트롤들의 데이터들을 문자열 배열로 받아 옴
String hobbys = "";
if (arrHobby == null) {	// hobby컨트롤에 갑싱 하나도 없으면
	hobbys = "없습니다.";
} else {
	for(int i = 0; i < arrHobby.length; i++) {
		hobbys += ", " + arrHobby[i]; 
	}
	hobbys = hobbys.substring(2) + "입니다.";	
}

// JSP에서 데이터를 받을 때는 get과 post 방식의 구분없이 데이터를 받음
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
out.println("data1 : " + data1 + "<br />");
out.println("data2 : " + data2 + "<br />");
// JSP에서는 PrintWriter 객체를 따로 생성하지 않아도 out이라는 인스턴스가 이미 생성되어 있어서 그냥 사용하면 됨
out.println("내 취미는 " + hobbys + " <br />");	

%>
</body>
</html>