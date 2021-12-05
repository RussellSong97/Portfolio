<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="beantest" class="test.BeanTest" scope="page" />
<!-- test패키지에 있는 BeanTest클래스의 인스턴스 beantest를 생성하여 page영역에서 사용하겠다는 의미 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>자바빈 사용예제</h2>
<h3><%=beantest.getName() %></h3>
<!-- beantest 인스턴스 안의 getName() 메소드를 실행시킴 -->
<%
beantest.setName("홍길동");
// beantest 인스턴스의 name멤버변수의 setter인 setName()메소드로 변수값 변경

out.println(beantest.getName());
%>
<hr />
<jsp:setProperty name="beantest" property="name" value="전우치" />
<!-- setProperty 액션 태그를 이용하여 멤버변수 name의 값을 변경 -->
<h3><%=beantest.getName() %></h3>
</body>
</html>
