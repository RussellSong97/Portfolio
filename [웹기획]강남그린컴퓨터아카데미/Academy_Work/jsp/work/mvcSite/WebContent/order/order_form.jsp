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

String kind = request.getParameter("kind");	// 장바구니와 바로 구매의 구분자
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
</head>
<body>
<h2>결제 폼 화면</h2>
<h3>구매할 상품 정보</h3>
<table width="800" cellpadding="5" cellspacing="0" id="cartTable">
<%
int total = 0;	// 총 구매가격을 누적할 변수
if (kind.equals("cart")) {	// 장바구니를 통한 구매시
	ArrayList<CartInfo> cartList = (ArrayList<CartInfo>)request.getAttribute("cartList");
	for (int i = 0 ; i < cartList.size() ; i++) {
		CartInfo cart = cartList.get(i);
		String lnk = "<a href=\"pdt_view.pdt?id=" + cart.getPi_id() + "\">";
%>
<tr align="center">
<td align="left">
	<%=lnk%><img src="product/pdt_img/<%=cart.getPi_img1()%>" width="50" height="60" align="absmiddle" /></a>
	&nbsp;&nbsp;&nbsp;<%=cart.getB_name() %> - <%=lnk + cart.getPi_name() %></a>
</td>
<td><%=cart.getOc_option()%></td>
<td><%=cart.getOc_cnt()%>ea</td>
<td><%=cart.getPi_price()%></td>
<td><%=cart.getPi_price() * cart.getOc_cnt()%></td>
</tr>
<%
		total += cart.getPi_price() * cart.getOc_cnt();
	}

} else {	// 상품상세화면에서 '바로 구매' 클릭시
	ProductInfo pdtInfo = (ProductInfo)request.getAttribute("pdtInf");
	String occnt = (String)request.getAttribute("occnt");
	String ocoption = (String)request.getAttribute("ocoption");
	String lnk = "<a href=\"pdt_view.pdt?id=" + pdtInfo.getPi_id() + "\">";
%>
<tr align="center">
<td align="left">
<%=lnk%><img src="product/pdt_img/<%=pdtInfo.getPi_img1()%>" width="50" height="60" align="absmiddle" /></a>
&nbsp;&nbsp;&nbsp;<%=pdtInfo.getB_name() %> - <%=lnk + pdtInfo.getPi_name() %></a>
</td>
<td><%=ocoption%></td>
<td><%=occnt%>ea</td>
<td><%=pdtInfo.getPi_price()%></td>
<td><%=pdtInfo.getPi_price() * Integer.parseInt(occnt)%></td>
</tr>
<%
	total += pdtInfo.getPi_price() * Integer.parseInt(occnt);
}

String[] phones = loginMember.getMi_phone().split("-");
// 회원정보 중 전화번호는 필수요소이므로 무조건 배열로 생성
%>
<tr><td colspan="5" align="right">총 구매가격 : <%=total %>원</td></tr>
</table>
<br />
<form name="frmOrder" action="" method="post">
<h3>배송지 정보</h3>
<table width="800" cellpadding="5" cellspacing="0">
<tr>
<th width="25%">수취인 이름</th>
<td width="*"><input type="text" name="name" value="<%=loginMember.getMi_name()%>" /></td>
</tr>
<tr>
<th>전화번호</th>
<td>
	<select name="p1">
		<option value="010" <% if (phones[0].equals("010")) { %>selected="selected"<% } %>>010</option>
		<option value="011" <% if (phones[0].equals("011")) { %>selected="selected"<% } %>>011</option>
		<option value="016" <% if (phones[0].equals("016")) { %>selected="selected"<% } %>>016</option>
		<option value="019" <% if (phones[0].equals("019")) { %>selected="selected"<% } %>>019</option>
	</select> -
	<input type="text" name="p2" size="4" maxlength="4" value="<%=phones[1] %>" /> -
	<input type="text" name="p3" size="4" maxlength="4" value="<%=phones[2] %>" />
</td>
</tr>
<tr>
<th>배송지 우편번호</th>
<td><input type="text" name="zip" value="<%=loginMember.getMa_zip()%>" size="5" /></td>
</tr>
<tr>
<th>배송지 주소</th>
<td>
	<input type="text" name="addr1" value="<%=loginMember.getMa_addr1()%>" />
	<input type="text" name="addr2" value="<%=loginMember.getMa_addr2()%>" />
</td>
</tr>
</table>
<br />
<h3>결제 정보</h3>
<p style="width:800px; ">
	<input type="radio" name="payment" value="a" />카드결제
	<input type="radio" name="payment" value="b" />계좌이체
	<input type="radio" name="payment" value="c" />무통장 입금
	<input type="radio" name="payment" value="d" />휴대폰 결제
</p>
<p style="width:800px; text-align:center;">
	<input type="submit" value="상품 구매" />&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="구매 취소" onclick="history.back();" />
</p>
</form>
<br /><br /><br />
</body>
</html>
