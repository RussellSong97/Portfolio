<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
session.invalidate();
// 로그인 정보 외에 살려야 하는 세션 정보가 있을 경우 invalidate()가 아닌 removeAttribute()를 사용해야 함
%>
<script>
location.href = "sessMain.jsp";
</script>
