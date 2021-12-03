<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
if (!isLogin) {
	out.println("<script>");
	out.println("alert('잘못된 경로로 들어오셨습니다.');");
	out.println("history.back();");
	out.println("</script>");
	out.close();
}

ArrayList<CartInfo> cartList = (ArrayList<CartInfo>)request.getAttribute("cartList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#cartTable th { border-bottom:3px black double; }
#cartTable td { border-bottom:1px black solid; }
</style>
<script src="jquery-3.6.0.js"></script>
<script>
function changeOpt(idx, opt, piid) {
	$.ajax({
		type : "POST", 
		url : "/mvcSite/cart_up_proc.ord", 
		data : {"kind" : "opt", "op" : opt, "idx" : idx, "piid" : piid}, 
		success : function(chkRst) {
			if (chkRst == 0) {	// 옵션 변경에 실패했을 경우
				alert("상품 옵션 변경에 실패했습니다.\n다시 시도해 주십시오.");
			} else {
				location.reload();
			}
		}
	});
}

function changeCnt(op, idx, cnt, max) {	// 장바구니의 수량을 변경시키는 함수
	if (op == "+" && (cnt + 1) > max) {
		alert("재고량(" + max + ")보다 많이 주문할 수 없습니다.");	return;
	} else if (op == '-' && (cnt - 1) < 1) {
		alert("최소 1개는 주문해야 합니다.");	return;
	}

	$.ajax({
		type : "POST", 
		url : "/mvcSite/cart_up_proc.ord", 
		data : {"kind" : "cnt", "op" : op, "idx" : idx, "piid" : ""}, 
		success : function(chkRst) {
			if (chkRst == 0) {	// 수량 변경에 실패했을 경우
				alert("상품 수량 변경에 실패했습니다.\n다시 시도해 주십시오.");
			} else {
				location.reload();
			}
		}
	});
}

function clearCart(kind) {
	var msg = "지정한 상품을 ";	var idx = kind;
	if (kind == -1) {
		msg = "선택한 상품들을 모두 ";
		idx = getSelectedChk();	// 선택된 체크박스의 value들을 받아 옴
		if (idx == "") {
			alert("삭제할 상품을 선택해야 합니다.");	return;
		}
	} else if (kind == 0)	msg = "장바구니안의 모든 상품을 ";

	if (confirm(msg + "삭제하시겠습니까?")) {
		$.ajax({
			type : "POST", 
			url : "/mvcSite/cart_del_proc.ord", 
			data : {"kind" : kind, "idx" : idx}, 
			success : function(chkRst) {
				if (chkRst == 0) {	// 삭제에 실패했을 경우
					alert("상품 삭제에 실패했습니다.\n다시 시도해 주십시오.");
				} else {
					location.reload();
				}
			}
		});
	}
}

function getSelectedChk() {	// 선택한 체크박스들의 value값을 리턴하는 함수
	var idx = "";	// 선택한 체크박스들의 value값을 쉼표로 구분하여 저장하는 변수
	var arrChk = document.frmCart.chk;
	for (var i = 0 ; i < arrChk.length ; i++) {
		if (arrChk[i].checked)	idx += "," + arrChk[i].value;
	}
	if (idx != "")	idx = idx.substring(1);

	return idx;
}

function chkAll(all) {
	var arrChk = document.frmCart.chk;
	for (var i = 0 ; i < arrChk.length ; i++) {
		arrChk[i].checked = all.checked;
	}
}

function choose(chk) {
	if (!chk.checked) {	// 현재 체크박스를 체크 해제했을 경우
		document.frmCart.all.checked = false;
	}
}

function goOrder(kind) {
	if (kind == "a") {	// 전체 상품 구매하기일 경우
		var arrChk = document.frmCart.chk;
		for (var i = 0 ; i < arrChk.length ; i++)	arrChk[i].checked = true;
		// 모든 체크박스를 체크된 상태로 변경
	}
	document.frmCart.submit();
}
</script>
</head>
<body>
<h2>장바구니 화면</h2>
<form name="frmCart" action="order_form.ord" method="post">
<input type="hidden" name="kind" value="cart" />
<table width="800" cellpadding="5" cellspacing="0" id="cartTable">
<tr>
<th width="5%"><input type="checkbox" name="all" checked="checked" onclick="chkAll(this);" /></th>
<th width="*">상 품</th><th width="10%">옵션</th><th width="15%">수량</th>
<th width="10%">단가</th><th width="10%">가격</th><th width="10%">삭제</th>
</tr>
<%
if (cartList != null && cartList.size() > 0) {	// 장바구니에 상품이 들어 있으면
	int total = 0;	// 총 구매가격을 누적할 변수
	for (int i = 0 ; i < cartList.size() ; i++) {
		CartInfo cart = cartList.get(i);
		int idx = cart.getOc_idx();	// 카트 인덱스번호
		String lnk = "<a href=\"pdt_view.pdt?id=" + cart.getPi_id() + "\">";
		int max = cart.getPi_stock();
		if (max == -1)	max = 1000;
%>
<tr align="center">
<td><input type="checkbox" name="chk" value="<%=idx%>" checked="checked" onclick="choose(this);" /></td>
<td align="left">
	<%=lnk%><img src="product/pdt_img/<%=cart.getPi_img1()%>" width="50" height="60" align="absmiddle" /></a>
	&nbsp;&nbsp;&nbsp;<%=cart.getB_name() %> - <%=lnk + cart.getPi_name() %></a>
</td>
<td>
	<select name="opt" onchange="changeOpt(<%=idx%>, this.value, '<%=cart.getPi_id()%>');">
<%
		String[] arrOpt = cart.getPi_option().split(";");
		for (int j = 0 ; j < arrOpt.length ; j++) {
			String slt = "";
			if (cart.getOc_option().equals(arrOpt[j]))	slt = " selected='selected'";
			out.println("<option value='" + arrOpt[j] + "'" + slt + ">" + arrOpt[j] + "</option>");
		}
%>
	</select>
</td>
<td>
	<input type="button" value="-" 
		onclick="changeCnt(this.value, <%=idx%>, <%=cart.getOc_cnt()%>, 0);" />
	<span id="cnt<%=idx%>"><%=cart.getOc_cnt()%></span>
	<input type="button" value="+" 
		onclick="changeCnt(this.value, <%=idx%>, <%=cart.getOc_cnt()%>, <%=max%>);" />
</td>
<td><%=cart.getPi_price()%></td>
<td><%=cart.getPi_price() * cart.getOc_cnt()%></td>
<td><input type="button" value="삭제" onclick="clearCart(<%=idx%>);" />
</tr>
<%
		total += cart.getPi_price() * cart.getOc_cnt();
		// 장바구니 내의 상품 총 가격
	}
%>
</table>
<p style="width:800px; text-align:right">총 결제 금액 : <%=total%>&nbsp;&nbsp;</p>
<p style="width:800px; text-align:right">
	<input type="button" value="장바구니 비우기" onclick="clearCart(0);" />&nbsp;&nbsp;
	<input type="button" value="선택한 상품 삭제" onclick="clearCart(-1);" />&nbsp;&nbsp;
	<input type="button" value="선택한 상품 구매" onclick="goOrder('s');" />&nbsp;&nbsp;
	<input type="button" value="전체 상품 구매" onclick="goOrder('a');" />&nbsp;&nbsp;
</p>
<%
} else {	// 장바구니가 비었으면
	out.println("<p style='width:800px; text-align:right'>장바구니가 비었습니다.</p>");
}
%>
<p style="width:800px; text-align:center; ">
	<input type="button" value="계속 쇼핑" onclick="location.href='pdt_list.pdt';" />
</p>
</form>
</body>
</html>
