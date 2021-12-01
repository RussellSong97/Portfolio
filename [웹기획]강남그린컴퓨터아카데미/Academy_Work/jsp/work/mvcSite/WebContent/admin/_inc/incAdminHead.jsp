<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*"%>
<%
	request.setCharacterEncoding("utf-8");
	AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
	if (adminInfo == null) {
		out.println("<script>");
		out.println("alert('로그인 후 사용하실 수 있습니다.');");
		out.println("location.href='login_form.jsp';");
		out.println("</script>");
		out.close();
	}
	
	
	// 권한(a:전체, b:고객센터-custom, c:주문-order, d:상품-product)

	String cURI = request.getRequestURI();
	int endIdx = cURI.lastIndexOf("/");
	int startIdx = cURI.lastIndexOf("/", endIdx - 1);
	String cFolder = cURI.substring(startIdx + 1, endIdx);	// 현재 파일의 기능명
	String admin_pms = adminInfo.getAi_pms(); // 로그인한 관리자의 권한
	
	boolean isPms = false;	// 권한여부를 저장할 변수
	if (admin_pms.equals("a")) { 
		isPms = true; 
	} else if (cFolder.equals("custom") && admin_pms.equals("b")) {
		isPms = true; 
	} else if (cFolder.equals("order") && admin_pms.equals("c")) {
		isPms = true; 
	} else if (cFolder.equals("product") && admin_pms.equals("d")) {
		isPms = true; 
	}
	
	if (!isPms) {
		out.println("<script>");
		out.println("alert('사용 권한이 없습니다.');");
		out.println("location.href='../login.jsp';");
		out.println("</script>");
		out.close();
	}
	
	
	
%>

<a href="../admin/" >index</a>