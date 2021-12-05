<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
request.setCharacterEncoding("utf-8");

ArrayList<ProductInfo> pdtList = (ArrayList<ProductInfo>)request.getAttribute("pdtList");
ArrayList<CataBigInfo> cataBigList = 
	(ArrayList<CataBigInfo>)request.getAttribute("cataBigList");	// 대분류 목록
ArrayList<CataSmallInfo> cataSmallList = 
	(ArrayList<CataSmallInfo>)request.getAttribute("cataSmallList");// 소분류 목록
ArrayList<BrandInfo> brandList = 
	(ArrayList<BrandInfo>)request.getAttribute("brandList");		// 브랜드 목록
PdtPageInfo pageInfo = (PdtPageInfo)request.getAttribute("pageInfo");

String args = "", schargs = "";
// 검색관련 쿼리스트링 제작
if (pageInfo.getKeyword() == null)	schargs += "&keyword=";
else		schargs += "&keyword=" + pageInfo.getKeyword();

if (pageInfo.getBcata() == null)	schargs += "&bcata=";
else		schargs += "&bcata=" + pageInfo.getBcata();

if (pageInfo.getScata() == null)	schargs += "&scata=";
else		schargs += "&scata=" + pageInfo.getScata();

if (pageInfo.getBrand() == null)	schargs += "&brand=";
else		schargs += "&brand=" + pageInfo.getBrand();

if (pageInfo.getSprice() == null)	schargs += "&sprice=";
else		schargs += "&sprice=" + pageInfo.getSprice();

if (pageInfo.getEprice() == null)	schargs += "&eprice=";
else		schargs += "&eprice=" + pageInfo.getEprice();
args = "?cpage=" + pageInfo.getCpage() + schargs;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
<%
String cbid = "", arrName = "";
int j = 0;
for (int i = 0 ; i < cataSmallList.size() ; i++) {
	if (!cbid.equals(cataSmallList.get(i).getCb_id())) {
		cbid = cataSmallList.get(i).getCb_id();
		arrName = "arr" + cbid;
		out.println("var " + arrName + " = new Array();");
		out.println(arrName + "[0] = new Option(\"\", \"소분류 전체\");");
		j = 1;
	}
	out.println(arrName + "[" + j + "] = new Option(\"" + 
	cataSmallList.get(i).getCs_id() + "\", \"" + cataSmallList.get(i).getCs_name() + "\");");
	j++;
}
%>
function setCategory(x, target) {
// x : 선택한 대분류 ID, target : 선택한 대분류에 속한 소분류를 보여줄 컨트롤 객체
	for (var i = target.options.length - 1 ; i > 0 ; i--) {
		target.options[i] = null;
	}
	
	if (x != "") {
		var arr = eval("arr" + x);
		for (var i = 0 ; i < arr.length ; i++) {
			target.options[i] = new Option(arr[i].value, arr[i].text);
		}
		target.options[0].selected = true;
	}
}
</script>
</head>
<body>
<h2>상품 목록</h2>
<form name="frmSch" method="get">
<input type="hidden" name="ord" value="<%=pageInfo.getOrd() %>" />
<input type="hidden" name="psize" value="<%=pageInfo.getPsize() %>" />
<table width="800" cellpadding="5">
<tr>
<th width="10%">분류</th>
<td width="40%">
	<select name="bcata" onchange="setCategory(this.value, this.form.scata);">
		<option value="">대분류 전체</option>
<%
for (CataBigInfo cata : cataBigList) {
	String slt = "";
	if (pageInfo.getBcata() != null && pageInfo.getBcata().equals(cata.getCb_id()))
		slt = " selected='selected'";
%>
		<option value="<%=cata.getCb_id() %>"<%=slt %>><%=cata.getCb_name() %></option>
<% } %>
	</select>&nbsp;&nbsp;&nbsp;
	<select name="scata">
		<option value="">소분류 전체</option>
<%
if (pageInfo.getBcata() != null && !pageInfo.getBcata().equals("")) {
// 대분류 검색조건이 있을 경우
	for (CataSmallInfo cata : cataSmallList) {
		if (pageInfo.getBcata().equals(cata.getCb_id())) {
		// 검색조건의 대분류와 동일한 대분류를 가진 소분류일 경우
			String slt = "";
			if (pageInfo.getScata() != null && pageInfo.getScata().equals(cata.getCs_id()))
				slt = " selected='selected'";
%>
		<option value="<%=cata.getCs_id()%>"<%=slt%>><%=cata.getCs_name()%></option>
<%
		}
	}
}
%>
	</select>
</td>
<th width="10%">브랜드</th>
<td width="40%">
	<select name="brand">
		<option value="">브랜드 전체</option>
<%
for (BrandInfo br : brandList) {
	String slt = "";
	if (pageInfo.getBrand() != null && pageInfo.getBrand().equals(br.getB_id()))
		slt = " selected='selected'";
%>
		<option value="<%=br.getB_id() %>"><%=br.getB_name() %></option>
<%
}
%>
	</select>
</td>
</tr>
<tr>
<th>상품명</th>
<td><input type="text" name="keyword" value="<%=pageInfo.getKeyword() %>" /></td>
<th>가격대</th>
<td>
	<input type="text" name="sprice" size="5" value="<%=pageInfo.getSprice()%>" />원 ~
	<input type="text" name="eprice" size="5" value="<%=pageInfo.getEprice()%>" />원
</td>
</tr>
<tr><td colspan="4" align="center">
	<input type="submit" value="상품 검색" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</td></tr>
</table>
</form>
<p style="width:800px;" align="right">정렬조건 :
	<a href="pdt_list.pdt<%=args%>&ord=idd" style="font-weight:<%=(pageInfo.getOrd().equals("idd")) ? "bold" : "normal" %>">신상품순</a>&nbsp;
	<a href="pdt_list.pdt<%=args%>&ord=salecntd" style="font-weight:<%=(pageInfo.getOrd().equals("salecntd")) ? "bold" : "normal" %>">인기순</a>&nbsp;
	<a href="pdt_list.pdt<%=args%>&ord=namea" style="font-weight:<%=(pageInfo.getOrd().equals("namea")) ? "bold" : "normal" %>">상품명</a>&nbsp;
	<a href="pdt_list.pdt<%=args%>&ord=pricea" style="font-weight:<%=(pageInfo.getOrd().equals("pricea")) ? "bold" : "normal" %>">낮은 가격순</a>&nbsp;
	<a href="pdt_list.pdt<%=args%>&ord=priced" style="font-weight:<%=(pageInfo.getOrd().equals("priced")) ? "bold" : "normal" %>">높은 가격순</a>&nbsp;
	<a href="pdt_list.pdt<%=args%>&ord=reviewd" style="font-weight:<%=(pageInfo.getOrd().equals("reviewd")) ? "bold" : "normal" %>">리뷰순</a>&nbsp;&nbsp;

	<a href="pdt_list.pdt<%=args%>&ord=<%=pageInfo.getOrd()%>&psize=10">
	<img src="img/pdt_list_<%=(pageInfo.getPsize() == 10) ? "on" : "off" %>.png" width="15" align="absmiddle" /></a>
	<a href="pdt_list.pdt<%=args%>&ord=<%=pageInfo.getOrd()%>&psize=12">
	<img src="img/pdt_img_<%=(pageInfo.getPsize() == 12) ? "on" : "off" %>.png" width="15" align="absmiddle" /></a>&nbsp;&nbsp;
</p>
<table width="800" cellpadding="5">
<%
if (pdtList != null && pdtList.size() > 0) {
// 상품 검색결과가 있으면
	for (int i = 0 ; i < pdtList.size() ; i++) {
		ProductInfo pi = pdtList.get(i);
		String lnk = "<a href=\"pdt_view.pdt" + args + "&id=" + pi.getPi_id() + 
			"&ord=" + pageInfo.getOrd() + "&psize=" + pageInfo.getPsize() + "\">";
		if (pageInfo.getPsize() == 12) {	// 상품목록을 한 줄에 4개씩 보여주기
			if (i % 4 == 0)		out.println("<tr align=\"center\">");
%>
<td width="200">
	<%=lnk%><img src="product/pdt_img/<%=pi.getPi_img1()%>" width="180" height="200" /><br />
	<%=pi.getPi_name()%></a><br /><%=pi.getPi_price()%> 원
</td>
<%
			if (i % 4 == 3)		out.println("</tr>");
		} else {	// 상품목록을 한 줄에 하나씩 보여주기
%>
<tr>
<td width="150" align="center"><%=lnk%><img src="product/pdt_img/<%=pi.getPi_img1()%>" width="110" height="130" /></a></td>
<td width="*"><%=lnk + pi.getPi_name()%></a></td>
<td width="100"><strong><%=pi.getPi_price()%></strong> 원</td>
</tr>
<%
		}
	}
} else {
	out.println("<tr><th>검색결과가 없습니다.</th></tr>");
}
%>
</table>
<%
if (pdtList != null && pdtList.size() > 0) {
	args = "?ord=" + pageInfo.getOrd() + "&psize=" + pageInfo.getPsize() + schargs;

	out.println("<p style=\"width:800px;\" align=\"center\">");

	if (pageInfo.getCpage() == 1) {	// 현재 페이지 번호가 1이면
		out.println("[&lt;&lt;]&nbsp;&nbsp;[&lt;]&nbsp;&nbsp;");
	} else {
		out.print("<a href='pdt_list.pdt" + args + "&cpage=1" + "'>");
		out.println("[&lt;&lt;]</a>&nbsp;&nbsp;");
		out.print("<a href='pdt_list.pdt" + args + 
			"&cpage=" + (pageInfo.getCpage() - 1) + "'>");
		out.println("[&lt;]</a>&nbsp;&nbsp;");
	} // 첫 페이지와 이전 페이지 링크

	for (int i = 1, k = pageInfo.getSpage() ; i <= pageInfo.getBsize() && k <= pageInfo.getEpage() ; i++, k++) {
	// i : 루프돌릴 횟수를 검사하는 용도의 변수, k : 페이지 번호 출력용 변수
	// 조건 : bsize만큼 루프를 도는데 페이지가 마지막 페이지일 경우 bsize보다 작아도 멈춤
		if (pageInfo.getCpage() == k) {	// 현재 페이지 번호일 경우 링크없이 강조만 함
			out.print("&nbsp;<strong>" + k + "</strong>&nbsp;");
		} else {
			out.print("&nbsp;<a href='pdt_list.pdt" + args + "&cpage=" + k + "'>");
			out.print(k + "</a>&nbsp;");
		}
	}

	if (pageInfo.getCpage() == pageInfo.getPcnt()) {	// 현재 페이지번호가 마지막 페이지 번호이면
		out.println("&nbsp;&nbsp;[&gt;]&nbsp;&nbsp;[&gt;&gt;]");
	} else {
		out.println("&nbsp;&nbsp;<a href='pdt_list.pdt" + args + 
			(pageInfo.getCpage() + 1) + "'>[&gt;]</a>");
		out.print("&nbsp;&nbsp;<a href='pdt_list.pdt" + args + 
			"&cpage=" + pageInfo.getPcnt() + "'>");
		out.println("[&gt;&gt;]</a>");
	}

	out.println("</p>");
}
%>
</body>
</html>
