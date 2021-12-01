<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.*" %>
<%
	request.setCharacterEncoding("utf-8");

	String uploadPath = request.getRealPath("/upload"); 
	int maxSize = 5 * 1024 * 1024 ;	 
	String name = "", birth = "";
	String file1 = "", file2 = "";
	String file3 = "", file4 = "";
	String file1_old = "", file2_old = "";
	String file3_old = "", file4_old = "";
		
	try { 
		MultipartRequest multi = new MultipartRequest(
				request, 	
				uploadPath,	
				maxSize, 	
				"utf-8",	
				new DefaultFileRenamePolicy()
		);
		
		name = multi.getParameter("name");
		birth = multi.getParameter("birth");
		
		Enumeration files = multi.getFileNames();
 
		String f1 = (String)files.nextElement();
		file1 = multi.getFilesystemName(f1);	
		file1_old = multi.getOriginalFileName(f1);

		String f2 = (String)files.nextElement();
		file2 = multi.getFilesystemName(f2);
		file2_old = multi.getOriginalFileName(f2);

		String f3 = (String)files.nextElement();
		file3 = multi.getFilesystemName(f3);
		file3_old = multi.getOriginalFileName(f3);

		String f4 = (String)files.nextElement();
		file4 = multi.getFilesystemName(f4);
		file4_old = multi.getOriginalFileName(f4); 
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>	
	<form name="frmFile" action="my_info_result.jsp" method="post">
		<input type="hidden" name="name" value="<%=name %>" />
		<input type="hidden" name="birth" value="<%=birth %>" />
		<input type="hidden" name="file1" value="<%=file1 %>" />
		<input type="hidden" name="file2" value="<%=file2 %>" />
		<input type="hidden" name="file3" value="<%=file3 %>" />
		<input type="hidden" name="file4" value="<%=file4 %>" />
		<input type="hidden" name="file1_old" value="<%=file1_old %>" />
		<input type="hidden" name="file2_old" value="<%=file2_old %>" />
		<input type="hidden" name="file3_old" value="<%=file3_old %>" />
		<input type="hidden" name="file4_old" value="<%=file4_old %>" />
	</form>
	<a href="javascript:document.frmFile.submit();">업로드 확인 및 다운로드 페이지로 이동</a>
</body>
</html>