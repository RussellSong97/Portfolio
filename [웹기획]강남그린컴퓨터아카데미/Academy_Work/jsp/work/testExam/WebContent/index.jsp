<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
/*
	2. �ε��� ȭ�� �ۼ�
	 - �α��ΰ� ȸ������ ��ũ �����ֱ�
	 - �α��� �� 'ȸ������' ��ũ�� ������(�׳� a�±׸� ����)
	 - �α��� ������ '�α���' ��ũ�� ������
*/

String id = null;
if ((String)session.getAttribute("login_id") != null) {
	id = (String)session.getAttribute("login_id") ;
}  
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

<% if (id == null) { %>
	<a href="login_form.jsp" >�α���</a>
<% } else { %> 		
	<a href="#">ȸ������</a>
<% } %>
	<br /><hr />
	<a href="test_list.brd">�۸��</a>
	<br /><hr />
	<a href="pdt_list.pdt">��ǰ��� ȭ��</a>
	<br /><hr />
	<br />
</body>
</html>