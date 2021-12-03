<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
mem이라는 MemberInfo클래스의 인스턴스를 생성하고 beanMemTest1.jsp에서 보낸 데이터들을 멤버변수에 저장
저장 후 화면상에 출력
 -->
<%
request.setCharacterEncoding("utf-8");
%>
<jsp:useBean id="mem" class="test.MemberInfo" />
<jsp:setProperty name="mem" property="*" />
<!-- 사용자가 전송한 컨트롤이나 파라미터의 값들을 자동으로 같은 이름의 빈 객체 속성에 할당 -->
<!-- 
<jsp:setProperty name="mem" property="uid" param="uid" />
<jsp:setProperty name="mem" property="pwd" param="pwd" />
<jsp:setProperty name="mem" property="name" param="name" />
<jsp:setProperty name="mem" property="phone" param="phone" />
<jsp:setProperty name="mem" property="point" param="point" />
 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>회원 정보 보기</h2>
<table>
<tr><th>아이디</th><td><%=mem.getUid() %> (<jsp:getProperty name="mem" property="uid" />)</td></tr>
<tr><th>암호</th><td><%=mem.getPwd() %> (<jsp:getProperty name="mem" property="pwd" />)</td></tr>
<tr><th>이름</th><td><%=mem.getName() %> (<jsp:getProperty name="mem" property="name" />)</td></tr>
<tr><th>전화</th><td><%=mem.getPhone() %> (<jsp:getProperty name="mem" property="phone" />)</td></tr>
<tr><th>포인트</th><td><%=mem.getPoint() %> (<jsp:getProperty name="mem" property="point" />)</td></tr>
</table>
</body>
</html>
