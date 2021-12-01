<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*"%>
<%
request.setCharacterEncoding("utf-8");

ArrayList<ProductInfo> pdtList = (ArrayList<ProductInfo>)request.getAttribute("productList"); 
PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");


String args = "";
args = "?cpage=" + pageInfo.getCpage();

%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<style>
	body { margin:0; font-size:13px; }
	ol, ul { list-style:none; }
	a:link { color:#4f4f4f; text-decoration:none; }
	a:visited { color:#4f4f4f; text-decoration:none; }
	a:hover { color:#f00; text-decoration:underline; }
	#wrapper { width:1250px; margin:0 auto; height:500px; }
	#paging { clear:both; width: 1250px; text-align: center; margin: 0 auto; }
	#top {
		width:100%; height:50px; text-align:center; font-size:25px; color:#4f4f4f; 
		font-weight:bold; padding-top:30px;
	}
	.outerBox {
		border:5px #4f4f4f solid; width:270px; text-align:center;
		padding:5px; margin:10px; float:left;
	}
	.innerBox {
		border:1px #000 solid; width:260px; height:190px; padding:5px; 
		text-align:center; display:table-cell; vertical-align:middle;
	}
	.pdtName { font-size:15px; padding-top:7px; padding-bottom:3px; font-weight:bold; }
	.pdtPrice { color:red; font-weight:bold; }
	</style>
</head>
<body>
<div id="wrapper">
	<div id="top">PRODUCTS LIST</div>
<%
if (pdtList != null && pdtList.size() > 0) {
// 상품 검색결과가 있으면
	for (int i = 0 ; i < pdtList.size() ; i++) {
		ProductInfo pi = pdtList.get(i); 
%>
	<div class="outerBox">
		<div class="innerBox"><img src="./sample/<%=pi.getProduct_img() %>" width="256" height="200" /></div>
		<div class="pdtName"><%=pi.getProduct_name() %></div>￦ <span class="pdtPrice"><%=pi.getProduct_price() %></span>
	</div> 
<%
	
	}
} else {
	out.println("<tr><th>데이터가 없습니다.</th></tr>");
}
%>
</div>
<div id="paging"> 
<%
if (pdtList != null && pdtList.size() > 0) { 
	
	args = "";

	out.println("<p style=\"width:1250px;\" align=\"center\">");

	if (pageInfo.getCpage() == 1) {	// 현재 페이지 번호가 1이면
		out.println("[&lt;&lt;]&nbsp;&nbsp;[&lt;]&nbsp;&nbsp;");
	} else {
		out.print("<a href='pdt_list.pdt?cpage=1" + "'>");
		out.println("[&lt;&lt;]</a>&nbsp;&nbsp;");
		out.print("<a href='pdt_list.pdt?cpage=" + (pageInfo.getCpage() - 1) + "'>");
		out.println("[&lt;]</a>&nbsp;&nbsp;");
	} // 첫 페이지와 이전 페이지 링크

	for (int i = 1, k = pageInfo.getSpage() ; i <= pageInfo.getBsize() && k <= pageInfo.getEpage() ; i++, k++) {
		if (pageInfo.getCpage() == k) {	// 현재 페이지 번호일 경우 링크없이 강조만 함
			out.print("&nbsp;<strong>" + k + "</strong>&nbsp;");
		} else {
			out.print("&nbsp;<a href='pdt_list.pdt" + "?cpage=" + k + "'>");
			out.print(k + "</a>&nbsp;");
		}
	}

	if (pageInfo.getCpage() == pageInfo.getPcnt()) {	// 현재 페이지번호가 마지막 페이지 번호이면
		out.println("&nbsp;&nbsp;[&gt;]&nbsp;&nbsp;[&gt;&gt;]");
	} else {
		out.println("&nbsp;&nbsp;<a href='pdt_list.pdt" + 
			(pageInfo.getCpage() + 1) + "'>[&gt;]</a>");
		out.print("&nbsp;&nbsp;<a href='pdt_list.pdt" + 
			"?cpage=" + pageInfo.getPcnt() + "'>");
		out.println("[&gt;&gt;]</a>");
	}

	out.println("</p>");
}
%>
</div>
</body>
</html>
