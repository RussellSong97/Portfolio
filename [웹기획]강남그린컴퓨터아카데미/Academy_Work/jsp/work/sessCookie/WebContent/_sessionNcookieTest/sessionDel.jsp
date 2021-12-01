<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
session.removeAttribute("name");
// session객체 안에서 'name'이라는 속성을 삭제 - removeAttribute() : 지정한 속성망을 삭제하는 메소드
%>
<script>
	location.href = "sessionTest.jsp" ;
</script>
