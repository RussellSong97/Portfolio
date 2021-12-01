<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>파일 업로드 폼</h3>
	<form action="cosUploadProc.jsp" method="post" enctype="multipart/form-data" >
		<table cellpadding="5">
			<tr>
				<th><label for="name">업로더</label></th>
				<td><input type="text" name="name" id="name" /></td>
			</tr>
			<tr>
				<th><label for="title">제목</label></th>
				<td><input type="text" name="title" id="title" /></td>
			</tr>
			<tr>
				<th><label for="title">파일1</label></th>
				<td><input type="file" name="file1" id="file1" /></td>
			</tr>
			<tr>
				<th><label for="title">파일2</label></th>
				<td><input type="file" name="file2" id="file2" /></td>
			</tr>
			<tr> 
				<td colspan="2" align="center">
					<input type="submit" value="전 송" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>