<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	table { margin:auto; width:900px; color:gray; border:1px solid gray; }
</style>
</head>
<body>
	<table>
		<tr>
			<td height="50" colspan="2" align="left" >
				<jsp:include page="tempTop.jsp" />
			</td>
		</tr>
		<tr>
			<td width="15%" align="right" valign="top">
				<br />
				<jsp:include page="tempLeft.jsp" />
			</td>
			<td width="*" align="center">
				<h2>신상품 목록</h2>
			</td>
		</tr>
		<tr>
			<td height="50" colspan="2" align="left" >
				<jsp:include page="tempBottom.jsp" />
			</td>
		</tr>
	</table>
</body>
</html>