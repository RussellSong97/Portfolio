<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
/* 
3. �α��� �� ����(login_form.jsp) �ۼ�
  - ���̵�� ��ȣ�� �Է¹޾� ó�� ���Ϸ� �����
  - ���̵�� ��ȣ�� �ڹ� ��ũ��Ʈ�� �˻� �� �����
    (�˻� ���� : �Է¿���, 4�� �̻� �Է��� 2������ �˻�)
*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script>
	function onSubmit() {
		var uid = document.frmLogin.uid.value;
		var pwd = document.frmLogin.pwd.value;

		if (uid == "" || uid.length < 4) {
			alert("���̵� 4�� �̻� �Է����ּ���.");
			return false;
		}
		
		if (pwd == "" || pwd.length < 4) {
			alert("��й��� 4�� �̻� �Է����ּ���.");
			return false;
		}
		
		document.frmLogin.submit();
	}
</script>
</head>
<body>
	<h2>�α��� ��</h2>
	<form name="frmLogin" action="login_proc.jsp" method="post">
		���̵� : <input type="text" name="uid" placeholder="���̵� �Է�" maxlength="20"/><br />
		��й�ȣ : <input type="password" name="pwd" placeholder="��й�ȣ �Է�" maxlength="20"/><br />
		<input type="button" value="�α���" onclick="onSubmit()" />
	</form>
</body>
</html>