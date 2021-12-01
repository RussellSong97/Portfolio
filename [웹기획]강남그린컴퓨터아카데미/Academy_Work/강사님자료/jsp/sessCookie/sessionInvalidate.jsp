<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
session.invalidate();
// session객체에서 모든 데이터를 삭제 시킴(로그아웃 등에서 주로 사용됨)
%>
<script>
location.href = "sessionTest.jsp";
</script>
