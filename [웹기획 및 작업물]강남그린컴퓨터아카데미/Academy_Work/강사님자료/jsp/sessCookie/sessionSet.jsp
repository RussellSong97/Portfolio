<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
session.setAttribute("name", "Session Test!!");
// session에 'name'이라는 이름의 속성(attribute)을 저장
%>
<script>
location.href = "sessionTest.jsp";
</script>
