<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String name = request.getParameter("name");
	String birth = request.getParameter("birth");
	String file1 = request.getParameter("file1");
	String file2 = request.getParameter("file2");
	String file3 = request.getParameter("file3");
	String file4 = request.getParameter("file4");
	String file1_old = request.getParameter("file1_old");
	String file2_old = request.getParameter("file2_old");
	String file3_old = request.getParameter("file3_old");
	String file4_old = request.getParameter("file4_old");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>파일 업로드 결과</h3>
	<table cellpadding="5">
		<tr>
			<th>업로더</th>
			<td><%=name %></td>
		</tr>
		<tr>
			<th>생년월일</th>
			<td><%=birth %></td>
		</tr>
		<tr>
			<th>파일1</th>
			<td><a href="fileDownload.jsp?file=<%=file1 %>"><%=file1_old %></a></td>
		</tr>
		<tr>
			<th>파일2</th>
			<td><a href="fileDownload.jsp?file=<%=file2 %>"><%=file2_old %></a></td>
		</tr> 
		<tr>
			<th>파일3</th>
			<td><a href="fileDownload.jsp?file=<%=file3 %>"><%=file3_old %></a></td>
		</tr> 
		<tr>
			<th>파일4</th>
			<td><a href="fileDownload.jsp?file=<%=file4 %>"><%=file4_old %></a></td>
		</tr> 
	</table>
</body>
</html>