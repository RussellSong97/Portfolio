<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%
request.setCharacterEncoding("utf-8");

MemberInfo loginMember = (MemberInfo)session.getAttribute("loginMember");
boolean isLogin = false;
if (loginMember != null)	isLogin = true;
%>
<a href="/mvcSite/">인덱스</a>
