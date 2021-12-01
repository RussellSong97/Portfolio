<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String uri = request.getRequestURI();
int pos = uri.lastIndexOf('/');
String file = uri.substring(pos + 1);
String mLnk = "", nLnk = "", bLnk = "";
if (file.equals("template.jsp"))			mLnk = " class='lnk'";
else if (file.equals("tempNewItem.jsp"))	nLnk = " class='lnk'";
else if (file.equals("tempBestItem.jsp"))	bLnk = " class='lnk'";
%>
<style>
.lnk { font-weight:bold; }
</style>
<a href="template.jsp"<%=mLnk %>>메인화면</a><br /><br />
<a href="tempNewItem.jsp"<%=nLnk %>>신상품 목록</a><br /><br />
<a href="tempBestItem.jsp"<%=bLnk %>>인기상품 목록</a>
