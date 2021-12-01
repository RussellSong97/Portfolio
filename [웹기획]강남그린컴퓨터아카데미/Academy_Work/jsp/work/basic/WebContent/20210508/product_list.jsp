<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	.pdt, .pdt th, .pdt td { border:1px blue solid; }
	#col { border-collapse:collapse; }
</style>
</head>
<body>
<h2>상품 목록</h2>
	<table width="520" class="pdt" id="col">  
<% 
		for (int i = 1; i <= 12; i++) {
			if (i % 4 == 1) out.println("<tr>");
			out.println("<td width=\"130\">상품" + i + "</td>");
			if (i % 4 == 0) out.println("</tr>");
		}
%>
				
	</table>
</body>
</html>