<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%> 
<%@ page import="mem.*"%> 
<%

	request.setCharacterEncoding("utf-8"); 
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");
	//�α��� ���ο� �α��ν� �α��� ������ �����ϱ� ���� MemberInfo�� �ν��Ͻ� login_info�� ������ �Ӽ��� ����
	
	if (login_info == null) {
	%>
	<script>
		alert("�߸��� ��η� �����̽��ϴ�.");
		history.back();
	</script>
	
	<%
		out.close(); // ���� ������ ������ �����Ŵ
	}
	
	String mi_id 	= login_info.getMi_id(); 
	String old_pwd = request.getParameter("old_pwd");
	String mi_pwd  = request.getParameter("mi_pwd");

	String p1 = request.getParameter("p1");
	String p2 = request.getParameter("p2");
	String p3 = request.getParameter("p3");
	String mi_phone = p1 + "-" + p2 + "-" + p3;
	
	String e1 = request.getParameter("e1");
	String e3 = request.getParameter("e3");
	String mi_email = e1 + "@" + e3;
	
	String mi_issns = request.getParameter("mi_issns");
	String mi_ismail = request.getParameter("mi_ismail");


// 2. db ���� �� ���� �ۼ��� ����
	String driver = "com.mysql.cj.jdbc.Driver";
	String 	url   = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
			url  += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";

	Connection conn = null; 
	Statement stmt 	= null;  	
	
	String sql = " update t_member_info set " ;

	if (mi_pwd != null && !(mi_pwd.equals(""))) {
		sql += "mi_pwd = '" + mi_pwd + "', ";
	}
	sql +=	" mi_phone = '" + mi_phone + "', " ;
	sql +=	" mi_email = '" + mi_email + "', " ; 
	sql +=	" mi_issns = '" + mi_issns + "', " ; 
	sql +=	" mi_ismail = '" + mi_ismail + "'" ; 
	sql +=	" where mi_id = '" + mi_id + "' and mi_pwd = '" + mi_pwd + "'"  ;
	System.out.println(sql);
		
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234"); 
		
		stmt = conn.createStatement();  
		int result = stmt.executeUpdate(sql); 
		 
		out.println("<script>");
		if (result > 0) { 	// ������ ���ڵ尡 ������ (������ ����������..)
			if (mi_pwd != null && !(mi_pwd.equals(""))) {
				login_info.setMi_pwd(mi_pwd);
			}
			login_info.setMi_phone(mi_phone);
			login_info.setMi_email(mi_email);
			login_info.setMi_issns(mi_issns);
			login_info.setMi_ismail(mi_ismail);
			
			out.println("alert('���� ������ �Ϸ�Ǿ����ϴ�.')");
			out.println("location.href = 'mypage.jsp'");
		} else { 
			out.println("alert('���� ������ �����߽��ϴ�.')");
			out.println("history.back();");
		}
		out.println("</script>");
 
	} catch (Exception e) {
		out.println("ȸ�� ���� ���� �� ������ �߻��߽��ϴ�.");
		e.printStackTrace();
	} finally {
		try {
			stmt.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

%>
