<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="vo.*"%>
<%   
TestInfo testInfo = (TestInfo)request.getAttribute("testInfo");  
String wtype = (String)request.getAttribute("wtype");  
String btnVal = (wtype.equals("in")) ? "등록":"수정";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2><%=btnVal%></h2>
	<form action="test_proc.brd" method="post" name="frmForm"> 
		<input type="hidden" name="wtype" value="<%=wtype %>" /> 
		<input type="hidden" name="b_idx" value="<%=testInfo.getB_idx() %>" /> 
		<table width="800px" cellpadding="5" id="brd" cellspacing="0">
			<tr> 
				<th width="300px">작성자</th>
				<td width="*"><input type="text" name="b_writer" value="<%=testInfo.getB_writer()%>"></td> 
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" name="b_title" size="70" value="<%=testInfo.getB_title()%>"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea name="b_content" cols="80" rows="10"><%=testInfo.getB_content()%></textarea>
				</td>
			</tr>
		</table>
		<p style="width: 700px;text-align:center;" >
			<input type="submit" value="<%=btnVal%>"> 
			<input type="reset" value="다시 입력">
		</p>
	</form>
</body>
</html>