<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%> 
<%
	request.setCharacterEncoding("utf-8");

//1. 사용자 입력값 받아 오기
	String mi_id = request.getParameter("mi_id");
	String mi_pwd = request.getParameter("mi_pwd");
	String mi_name = request.getParameter("mi_name");
	String by = request.getParameter("by");
	String bm = (request.getParameter("bm").length() < 2) ? "0"+ request.getParameter("bm") : request.getParameter("bm");
	String bd = (request.getParameter("bd").length() < 2) ? "0"+ request.getParameter("bd") : request.getParameter("bd");
	String mi_birth = by + "-" + bm + "-" + bd;
	String mi_gender = request.getParameter("mi_gender");
	String p1 = request.getParameter("p1");
	String p2 = request.getParameter("p2");
	String p3 = request.getParameter("p3");
	String mi_phone = p1 + "-" + p2 + "-" + p3;
	String e1 = request.getParameter("e1");
	String e3 = request.getParameter("e3");
	String mi_email = e1 + "@" + e3;
	String mi_issns = request.getParameter("mi_issns");
	String mi_ismail = request.getParameter("mi_ismail");

	String ma_zip = request.getParameter("ma_zip");
	String ma_addr1 = request.getParameter("ma_addr1");
	String ma_addr2 = request.getParameter("ma_addr2");
	

// 2. db 연결 및 쿼리 작성과 실행
	String driver = "com.mysql.cj.jdbc.Driver";
	String 	url   = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
			url  += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt 	= null; 
	
	
	//String sql = " insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, mi_email, mi_issns, mi_ismail, mi_point) " + 
	//			 " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
		
	String sql		= " insert into t_member_info (mi_id, mi_pwd, mi_name, mi_birth, mi_gender, mi_phone, mi_email, mi_issns, mi_ismail, mi_point) ";
		   sql	   += " values ('" + mi_id + "', '" + mi_pwd + "', '" + mi_name + "', '" + mi_birth + "', '" + mi_gender + "', '" + mi_phone + "', '" + mi_email + "', '" + mi_issns + "', '" + mi_ismail + "', 1000 )";
		   out.println(sql);
	
	String sql2 = " insert into t_member_addr (mi_id, ma_zip, ma_addr1, ma_addr2) values ('"+mi_id+"', '"+ma_zip+"', '"+ma_addr1+"', '"+ma_addr2+"') ";
			
	
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234"); 
		
		stmt = conn.createStatement();  
		int result = stmt.executeUpdate(sql);
		result += stmt.executeUpdate(sql2);
		
		/*
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, mi_id);
		pstmt.setString(2, mi_pwd);
		pstmt.setString(3, mi_name);
		pstmt.setString(4, mi_birth);
		pstmt.setString(5, mi_gender);
		pstmt.setString(6, mi_phone);
		pstmt.setString(7, mi_email);
		pstmt.setString(8, mi_issns);
		pstmt.setString(9, mi_ismail);
		pstmt.setInt(10, 1000);
		
		int result = pstmt.executeUpdate(); 
		*/
				
		out.println("<script>");
		if (result > 0) {
			out.println("location.href = '../loginForm.jsp'");
		} else {
			out.println("alert('회원가입에 실패했습니다.')");
			out.println("history.back();");
		}
		out.println("</script>");

	} catch (Exception e) {
		out.println("회원가입 시 오류가 발생했습니다.");
		e.printStackTrace();
	} finally {
		try {
			pstmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

%>
