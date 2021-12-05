<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String tmpID = "", chkIsSave="", cook = request.getHeader("Cookie");
	
	if (cook != null) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) { 
			if (cookies[i].getName().equals("saveID")) { 
				tmpID = cookies[i].getValue(); 
			}
		}
	}   
%>          
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>로그인 폼</h2>
	<form name="frmLogin" action="sessLoginProc.jsp" method="post">
		아이디 : <input type="text" name="uid" value="<%=tmpID %>" /><br />
		비밀번호 : <input type="password" name="pwd" /><br />
		<input type="submit" value="로그인" />
		<input type="checkbox" name="isSave" value="y" <%=(!tmpID.equals(""))?"checked='checked'":"" %> />
	</form>
</body>
</html>