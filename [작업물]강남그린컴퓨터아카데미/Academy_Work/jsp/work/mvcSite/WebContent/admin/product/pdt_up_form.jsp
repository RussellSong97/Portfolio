<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incAdminHead.jsp" %>
<%
PdtPageInfo pageInfo = (PdtPageInfo)request.getAttribute("pageInfo"); // 검색조건
ProductInfo pdtInfo = (ProductInfo)request.getAttribute("pdtInfo");	// 수정할 상품 정보
ArrayList<CataBigInfo> cataBigList = 
	(ArrayList<CataBigInfo>)request.getAttribute("cataBigList");	// 대분류 목록
ArrayList<CataSmallInfo> cataSmallList = 
	(ArrayList<CataSmallInfo>)request.getAttribute("cataSmallList");// 소분류 목록
ArrayList<BrandInfo> brandList = 
	(ArrayList<BrandInfo>)request.getAttribute("brandList");		// 브랜드 목록
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.ipt, .cmb { width:200px; }
</style>
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
<h2>상품 수정 폼</h2>
<form name="frmPdt" action="pdt_proc.pdta" method="post" enctype="multipart/form-data">
<input type="hidden" name="wtype" value="up" />
<input type="hidden" name="id" value="<%=pdtInfo.getPi_id() %>" />
<input type="hidden" name="oldImg1" value="<%=pdtInfo.getPi_img1() %>" />
<input type="hidden" name="oldImg2" value="<%=pdtInfo.getPi_img2() %>" />
<input type="hidden" name="oldImg3" value="<%=pdtInfo.getPi_img3() %>" />
<input type="hidden" name="oldDesc" value="<%=pdtInfo.getPi_desc() %>" />
<input type="hidden" name="cpage" value="<%=pageInfo.getCpage() %>" />
<input type="hidden" name="schtype" value="<%=pageInfo.getSchtype()%>" />
<input type="hidden" name="keyword" value="<%=pageInfo.getKeyword() %>" />
<input type="hidden" name="bcata" value="<%=pageInfo.getBcata() %>" />
<input type="hidden" name="scata" value="<%=pageInfo.getScata() %>" />
<input type="hidden" name="brand" value="<%=pageInfo.getBrand() %>" />
<input type="hidden" name="sprice" value="<%=pageInfo.getSprice() %>" />
<input type="hidden" name="eprice" value="<%=pageInfo.getEprice() %>" />
<input type="hidden" name="isview" value="<%=pageInfo.getIsview() %>" />
<input type="hidden" name="sdate" value="<%=pageInfo.getSdate() %>" />
<input type="hidden" name="edate" value="<%=pageInfo.getEdate() %>" />
<input type="hidden" name="stock" value="<%=pageInfo.getStock() %>" />
<input type="hidden" name="ord" value="<%=pageInfo.getOrd() %>" />
<table width="800" cellpadding="5">
<tr>
<th width="150">대분류</th>
<td width="250">
	<select name="cbid" onchange="setCategory(this.value, this.form.csid);" class="cmb">
		<option value="">대분류 전체</option>
<%
for (CataBigInfo cata : cataBigList) {
	String slt = "";
	if (pdtInfo.getCb_id().equals(cata.getCb_id()))
		slt = " selected='selected'";
%>
		<option value="<%=cata.getCb_id() %>"<%=slt %>><%=cata.getCb_name() %></option>
<% } %>
	</select>
</td>
<th width="150">소분류</th>
<td width="250">
	<select name="csid" class="cmb">
		<option value="">소분류 전체</option>
<%
for (CataSmallInfo cata : cataSmallList) {
	if (pdtInfo.getCb_id().equals(cata.getCb_id())) {
	// 상품의 대분류와 동일한 대분류를 가진 소분류일 경우
		String slt = "";
		if (pdtInfo.getCs_id().equals(cata.getCs_id()))	slt = " selected='selected'";
%>
		<option value="<%=cata.getCs_id()%>"<%=slt%>><%=cata.getCs_name()%></option>
<%
	}
}
%>
	</select>
</td>
</tr>
<tr>
<th>브랜드</th>
<td>
	<select name="bid" class="cmb">
		<option value="">브랜드 전체</option>
<%
for (BrandInfo br : brandList) {
	String slt = "";
	if (br.getB_id().equals(pdtInfo.getB_id()))	slt = " selected='selected'";
%>
		<option value="<%=br.getB_id() %>"<%=slt%>><%=br.getB_name() %></option>
<% } %>
	</select>
</td>
<th>원산지</th>
<td><input type="text" name="origin" class="ipt" value="<%=pdtInfo.getPi_origin() %>" /></td>
</tr>
<tr>
<th>상품명</th>
<td><input type="text" name="name" class="ipt" value="<%=pdtInfo.getPi_name() %>" /></td>
<th>판매가</th>
<td><input type="text" name="price" class="ipt" value="<%=pdtInfo.getPi_price() %>" /></td>
</tr>
<tr>
<th>원가</th>
<td><input type="text" name="cost" class="ipt" value="<%=pdtInfo.getPi_cost() %>" /></td>
<th>할인율</th>
<td><input type="text" name="discount" class="ipt" value="<%=pdtInfo.getPi_discount() %>" />%</td>
</tr>
<tr>
<th>옵션</th>
<td colspan="3">
	<input type="text" name="option" class="ipt" value="<%=pdtInfo.getPi_option() %>" />
	해당 제품의 도수를 세미콜론(;)을 기준으로 구분하여 입력
</td>
</tr>
<tr>
<th>이미지1</th>
<td>
	<input type="file" name="img1" /><br />
<% if (pdtInfo.getPi_img1() == null || pdtInfo.getPi_img1().equals("")) { %>
	기존 이미지가 없습니다.
<% } else { %>
	<img src="../product/pdt_img/<%=pdtInfo.getPi_img1()%>" width="50" height="60" />
<% } %>
</td>
<th>이미지2</th>
<td>
	<input type="file" name="img2" /><br />
<% if (pdtInfo.getPi_img2() == null || pdtInfo.getPi_img2().equals("")) { %>
	기존 이미지가 없습니다.
<% } else { %>
	<img src="../product/pdt_img/<%=pdtInfo.getPi_img2()%>" width="50" height="60" />
<% } %>
</td>
</tr>
<tr>
<th>이미지3</th>
<td>
	<input type="file" name="img3" /><br />
<% if (pdtInfo.getPi_img3() == null || pdtInfo.getPi_img3().equals("")) { %>
	기존 이미지가 없습니다.
<% } else { %>
	<img src="../product/pdt_img/<%=pdtInfo.getPi_img3()%>" width="50" height="60" />
<% } %>
</td>
<th>설명이미지</th>
<td>
	<input type="file" name="desc" /><br />
<% if (pdtInfo.getPi_desc() == null || pdtInfo.getPi_desc().equals("")) { %>
	기존 이미지가 없습니다.
<% } else { %>
	<img src="../product/pdt_img/<%=pdtInfo.getPi_desc()%>" width="50" height="60" />
<% } %>
</td>
</tr>
<tr>
<th>재고량</th>
<td>
	<select name="pstock" class="cmb">
		<option value="-1" <% if (pdtInfo.getPi_stock() == -1) { %>selected="selected"<% } %>>무제한</option>
<% for (int i = 1 ; i <= 100 ; i++) { %>
		<option value="<%=i%>" <% if (pdtInfo.getPi_stock() == i) { %>selected="selected"<% } %>><%=i%></option>
<% } %>
	</select>
</td>
<th>게시여부</th>
<td>
	<input type="radio" name="pisview" value="y" <% if (pdtInfo.getPi_isview().equals("y")) { %>checked="checked"<% } %> />상품게시&nbsp;&nbsp;
	<input type="radio" name="pisview" value="n" <% if (pdtInfo.getPi_isview().equals("n")) { %>checked="checked"<% } %>  />상품미게시
</td>
</tr>
<tr><td colspan="4" align="center">
	<input type="submit" value="상품 수정" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</td></tr>
</table>
</form>
</body>
</html>
