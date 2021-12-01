<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="test.*" %>
<%
	// id는 'test1', 암호는 '1111'일 경우 로그인 성공
	request.setCharacterEncoding("utf-8"); 

	String uid = request.getParameter("uid");
	String pwd = request.getParameter("pwd");
	String isSave = request.getParameter("isSave");
	
	// saveID, 1년
	if (isSave != null) {
		Cookie cookie = new Cookie("saveID", uid); 
		cookie.setMaxAge(60 * 60 * 24 * 365);		// 60초 * 60분 => 1시간 * 24시간 => 하루 * 365 => 1년
		response.addCookie(cookie);	// 클라이언트에 생성한 쿠키를 보내어 클라이언트 PC에 저장시킴
	}
	
	out.println("<script>");
	if (uid.equals("test1") && pwd.equals("1111")) {
		// 이름은 '홍길동', 전화는 '010-1234-5678', 포인트는 3000으로 지정하여 MemberInfo클래스의 인스턴스를 저장
		MemberInfo mi = new MemberInfo();	// 회원정보들을 저장할 MemberInfo 인스턴스 생성
		mi.setUid(uid);
		mi.setPwd(pwd);
		mi.setName("홍길동");
		mi.setPhone("010-1234-5678");
		mi.setPoint(3000);
		
		session.setAttribute("login_info", mi); 
		// 로그인 한 회원의 정보를 가지고 있는 인스턴스 mi를 세션의 속성으로 저장
		out.println("location.href = 'sessMain.jsp' ;");
	} else {
		out.println("alert('로그인 정보가 잘못되었습니다. ');");
		out.println("location.href = 'sessLoginForm.jsp' ;");	// 히스토리.백 도 됨.
	}
	out.println("</script>");
	// 이름은 홍길동, 전화는 '010-1234-5678', 포인트는 3000으로 지정하여 MemberInfo클래스의 인스턴스를 저장
%>  