<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="login.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%
	/*
		4. 로그인 처리 파일(login_proc.jsp)
		 - 이전 화면에서 받아 온 아이디와 암호를 이용하여 t_member테이블에서 검사 후 로그인 처리
		 - 로그인 성공 시 : 아이디를 'login_id' 라는 이름의 속성으로 세션에 저장 후 index로 이
		 - 로그인 실패(아이디가 없을 경우) : '아이디가 없습니다.' 메시지 출력 후 로그인 화면으로 이동
		 - 로그인 실패(암호가 없을 경우) : '암호가 틀립니다' 메시지 출력 후 로그인 화면으로 이동 
	*/
	

	// ===== DB Connector & 변수 선언 
	String driver 	= "com.mysql.cj.jdbc.Driver";
	String url 		= "jdbc:mysql://localhost:3306/testExam?useUnicode=true&characterEncoding=UTF-8&";
	url			   += "verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

	Connection conn = null;	 
	Statement  stmt = null;
	ResultSet  rs 	= null;
	
	
	// ===== Form Parameter (id, pwd)
	request.setCharacterEncoding("utf-8"); 
	String uid = request.getParameter("uid");
	String pwd = request.getParameter("pwd"); 

		
	// ===== 조회 쿼리 실행
	String sql = "select * from t_member where m_id = '" + uid + "' ; ";
	//System.out.println(sql);	// 디버깅

	try {
		Class.forName(driver);
		conn 	= DriverManager.getConnection(url, "root", "1234");
		stmt 	= conn.createStatement();
		rs 		= stmt.executeQuery(sql);
		
		
		// ===== 로그인 처리 및 MemberInfo 클래스에 저장
		boolean isLogin = false;	// 로그인 가능여부
		String 	msg 	= "";		// 오류메시지 
		String	m_id	= "";
		String	m_pwd	= "";
		
		
		if (rs.next()) {	// rs에 데이터 가져오기 (id 기준) - 아이디는 있는 상태 
			m_id  = rs.getString("m_id");
			m_pwd = rs.getString("m_pwd");
			
			if (uid.equals(m_id) && pwd.equals(m_pwd)) {

				MemberInfo mi = new MemberInfo(); 				// 회원 정보를 저장할 인스턴스 생성
				mi.setM_idx(rs.getInt("m_idx"));
				mi.setM_id(uid);         
				mi.setM_pwd(pwd);        

				session.setAttribute("member_info", mi);		// 로그인 정보를 세션의 속성으로 저장   
				session.setAttribute("login_id", uid);			// 로그인 아이디를 세션의 속성으로 저장   
				msg = "정상적으로 로그인 되었습니다.";
				isLogin = true;	
				
			} else {
				msg = "암호가 틀립니다.";
			}
		} else {
			msg = "아이디가 없습니다.";
		}
		
		
		// ===== Script 
		out.println("<script>");	
		out.println("alert('"+msg+"');");
		
		if (isLogin) {		// isLogin가 true 이면 (로그인 성공) 
			out.println("location.href = 'index.jsp' ;"); 
		} else {			// isLogin가 false 이면 (로그인 실패)
			out.println("history.back();");
		}
		out.println("</script>");
				 
	} catch (Exception e) {
		out.println("로그인 처리시 오류가 발생했습니다.");
		e.printStackTrace();
	}
	
%>
