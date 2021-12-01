<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incAdminHead.jsp" %>
<%
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
if (pageInfo.getIsview() == null)	schargs += "&isview=";
else		schargs += "&isview=" + pageInfo.getIsview();
if (pageInfo.getSchtype() == null)	schargs += "&schtype=";
else		schargs += "&schtype=" + pageInfo.getSchtype();
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
if (pageInfo.getSdate() == null)	schargs += "&sdate=";
else		schargs += "&sdate=" + pageInfo.getSdate();
if (pageInfo.getEdate() == null)	schargs += "&edate=";
else		schargs += "&edate=" + pageInfo.getEdate();
if (pageInfo.getStock() == null)	schargs += "&stock=";
else		schargs += "&stock=" + pageInfo.getStock();
args = "?cpage=" + pageInfo.getCpage() + schargs;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script> 
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script> 
<script>
$(function() {
	$.datepicker.regional['ko'] = {
		closeText: '닫기',
		prevText: '이전달',
		nextText: '다음달',
		currentText: '오늘',
		monthNames: ['1월','2월','3월','4월','5월','6월',
		'7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월',
		'7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		buttonImageOnly: true,
		weekHeader: 'Wk',
		dateFormat: 'yy-mm-dd',
		firstDay: 0,
		isRTL: false,
		duration:200,
		showAnim:'show',
		showMonthAfterYear: false
	};
	$.datepicker.setDefaults($.datepicker.regional['ko']);

	$( "#sdate" ).datepicker({changeMonth: true, dateFormat:"yy-mm-dd",
		onClose: function( selectedDate ) {
		}
	});
	$( "#edate" ).datepicker({ changeMonth: true, dateFormat:"yy-mm-dd",
		onClose: function( selectedDate ) {
		}
	});
});

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
<h2>상품 관리</h2>
<form name="frmSch" action="" method="get">
<input type="hidden" name="ord" value="<%=pageInfo.getOrd() %>" />
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
<th>검색어</th>
<td>
	<select name="schtype">
		<option value="name"<% if (pageInfo.getSchtype().equals("name")) { %> selected="selected"<% } %>>상품명</option>
		<option value="id"<% if (pageInfo.getSchtype().equals("id")) { %> selected="selected"<% } %>>상품아이디</option>
	</select>
	<input type="text" name="keyword" size="10" value="<%=pageInfo.getKeyword() %>" />
</td>
<th>가격대</th>
<td>
	<input type="text" name="sprice" size="5" value="<%=pageInfo.getSprice()%>" />원 ~
	<input type="text" name="eprice" size="5" value="<%=pageInfo.getEprice()%>" />원
</td>
</tr>
<tr>
<th>재고량</th>
<td><input type="text" name="stock" size="5" value="<%=pageInfo.getStock() %>" />개 이상</td>
<th>등록기간</th>
<td>
	<input type="text" name="sdate" id="sdate" value="<%=pageInfo.getSdate() %>" size="10" class="ipt" /> ~
	<input type="text" name="edate" id="edate" value="<%=pageInfo.getEdate() %>" size="10" class="ipt" />
</td>
</tr>
<tr>
<th>게시여부</th>
<td colspan="3">
	<input type="radio" name="isview" value="" <% if (pageInfo.getIsview().equals("")) { %>checked="checked"<% } %> />전체 검색
	<input type="radio" name="isview" value="y" <% if (pageInfo.getIsview().equals("y")) { %>checked="checked"<% } %> />게시 상품
	<input type="radio" name="isview" value="n" <% if (pageInfo.getIsview().equals("n")) { %>checked="checked"<% } %> />미게시 상품
</td>
</tr>
<tr><td colspan="4" align="center">
	<input type="submit" value="상품 검색" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="전체 검색" onclick="location.href='pdt_list.pdta';" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="상품 등록" onclick="location.href='pdt_in_form.pdta';" />
</td></tr>
</table>
</form>
<p style="width:800px;" align="right">정렬조건 :
	<a href="pdt_list.pdta<%=args%>&ord=idd" style="font-weight:<%=(pageInfo.getOrd().equals("idd")) ? "bold" : "normal" %>">신상품순</a>&nbsp;
	<a href="pdt_list.pdta<%=args%>&ord=salecntd" style="font-weight:<%=(pageInfo.getOrd().equals("salecntd")) ? "bold" : "normal" %>">인기순</a>&nbsp;
	<a href="pdt_list.pdta<%=args%>&ord=namea" style="font-weight:<%=(pageInfo.getOrd().equals("namea")) ? "bold" : "normal" %>">상품명</a>&nbsp;
	<a href="pdt_list.pdta<%=args%>&ord=pricea" style="font-weight:<%=(pageInfo.getOrd().equals("pricea")) ? "bold" : "normal" %>">낮은 가격순</a>&nbsp;
	<a href="pdt_list.pdta<%=args%>&ord=priced" style="font-weight:<%=(pageInfo.getOrd().equals("priced")) ? "bold" : "normal" %>">높은 가격순</a>&nbsp;
	<a href="pdt_list.pdta<%=args%>&ord=reviewd" style="font-weight:<%=(pageInfo.getOrd().equals("reviewd")) ? "bold" : "normal" %>">리뷰순</a>&nbsp;&nbsp;
</p>
<table width="800" cellpadding="5">
<%
if (pdtList != null && pdtList.size() > 0) {
// 상품 검색결과가 있으면
	for (int i = 0 ; i < pdtList.size() ; i++) {
		ProductInfo pi = pdtList.get(i);
		String lnk = "<a href=\"pdt_up_form.pdta" + args + "&id=" + 
			pi.getPi_id() + "&ord=" + pageInfo.getOrd() + "\">";
		String st = pi.getPi_stock() + "";
		if (pi.getPi_stock() == -1)	st = "무제한";
		String view = "게시중";
		if (pi.getPi_isview().equals("n"))	view = "미게시";
%>
<tr>
<td width="150" align="center"><%=lnk%><img src="../product/pdt_img/<%=pi.getPi_img1()%>" width="50" height="60" /></a></td>
<td width="*">
	<%=lnk + pi.getPi_name()%></a>
	<%=pi.getCb_name()%> -> <%=pi.getCs_name()%><br />
	<%=pi.getB_name()%> / 재고량 : <%=st %> / <%=view %>
</td>
<td width="100"><strong><%=pi.getPi_price()%></strong> 원</td>
</tr>
<%
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
		out.print("<a href='pdt_list.pdta" + args + "&cpage=1" + "'>");
		out.println("[&lt;&lt;]</a>&nbsp;&nbsp;");
		out.print("<a href='pdt_list.pdta" + args + 
			"&cpage=" + (pageInfo.getCpage() - 1) + "'>");
		out.println("[&lt;]</a>&nbsp;&nbsp;");
	} // 첫 페이지와 이전 페이지 링크

	for (int i = 1, k = pageInfo.getSpage() ; i <= pageInfo.getBsize() && k <= pageInfo.getEpage() ; i++, k++) {
	// i : 루프돌릴 횟수를 검사하는 용도의 변수, k : 페이지 번호 출력용 변수
	// 조건 : bsize만큼 루프를 도는데 페이지가 마지막 페이지일 경우 bsize보다 작아도 멈춤
		if (pageInfo.getCpage() == k) {	// 현재 페이지 번호일 경우 링크없이 강조만 함
			out.print("&nbsp;<strong>" + k + "</strong>&nbsp;");
		} else {
			out.print("&nbsp;<a href='pdt_list.pdta" + args + "&cpage=" + k + "'>");
			out.print(k + "</a>&nbsp;");
		}
	}

	if (pageInfo.getCpage() == pageInfo.getPcnt()) {	// 현재 페이지번호가 마지막 페이지 번호이면
		out.println("&nbsp;&nbsp;[&gt;]&nbsp;&nbsp;[&gt;&gt;]");
	} else {
		out.println("&nbsp;&nbsp;<a href='pdt_list.pdta" + args + 
			(pageInfo.getCpage() + 1) + "'>[&gt;]</a>");
		out.print("&nbsp;&nbsp;<a href='pdt_list.pdta" + args + 
			"&cpage=" + pageInfo.getPcnt() + "'>");
		out.println("[&gt;&gt;]</a>");
	}

	out.println("</p>");
}
%>
<br /><br />
</body>
</html>
