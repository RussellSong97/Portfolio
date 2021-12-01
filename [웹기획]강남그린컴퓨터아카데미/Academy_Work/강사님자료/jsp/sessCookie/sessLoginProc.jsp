<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="test.*" %>
<%
request.setCharacterEncoding("utf-8");
String uid = request.getParameter("uid");
String pwd = request.getParameter("pwd");
String isSave = request.getParameter("isSave");

if (isSave != null) {	// 아이디를 저장할 경우
	Cookie cookie = new Cookie("saveID", uid);
	cookie.setMaxAge(60 * 60 * 24 * 365);
	response.addCookie(cookie);
}

out.println("<script>");
if (uid.equals("test1") && pwd.equals("1111")) {
	MemberInfo mi = new MemberInfo();	// 회원정보들을 저장할 MemberInfo 인스턴스 생성
	mi.setUid(uid);
	mi.setPwd(pwd);
	mi.setName("홍길동");
	mi.setPhone("010-1234-5678");
	mi.setPoint(3000);

	session.setAttribute("login_info", mi);
	// 로그인 한 회원의 정보를 가지고 있는 인스턴스 mi를 세션의 속성으로 저장
	out.println("location.href = 'sessMain.jsp';");
} else {
	out.println("alert('로그인 정보가 잘못되었습니다.');");
	out.println("location.href = 'sessLoginForm.jsp';");
}
out.println("</script>");
%>
