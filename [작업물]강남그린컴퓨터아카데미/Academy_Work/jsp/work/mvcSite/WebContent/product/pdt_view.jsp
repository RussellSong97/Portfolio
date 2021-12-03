<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
ProductInfo pdtInfo = (ProductInfo)request.getAttribute("pdtInfo");
if (pdtInfo == null) {
   out.println("<script>");
   out.println("alert('잘못된 경로로 들어오셨습니다.');");
   out.println("history.back();");
   out.println("</script>");
   out.close();
}
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
var cnt = 1;
var total = <%=pdtInfo.getPi_price() %>;
function changeCnt(op){
   var obj1 = document.getElementById("occnt");
   var obj2 = document.getElementById("total");
   if (op == "+"){
      cnt = cnt + 1;
   } else {
      if ( cnt > 1)      cnt = cnt - 1;
   }
   obj1.innerHTML = cnt;               // 보여줄 cnt(수량변경)
   obj2.innerHTML = (total * cnt);         // 총 구매가 변경
   document.frmPdt.occnt.value = cnt;      // cnt 가져가기
}

function cartBuy(chk){
   // 장바구니나 즉시 구매로 이동시키는 함수로 비로그인 시 로그인 폼으로 이동시켜야 함
   // 장바구니 : cart_in_proc.ord / 즉시구매 : order_form.ord
   var lnk = "";
   if (chk == "c")  lnk = "cart_in_proc.ord";
   else          	lnk = "order_form.ord";
<% if (!isLogin) { %>
   location.href = "login_form.jsp?url=" + lnk;
<% } else { %>
   var frm = document.frmPdt;
   frm.action = lnk;
   frm.submit();
<% } %>
}
</script>
</head>
<body>
<table width="800" cellpadding="0">
<tr align="center"><td>
   <!-- 상품 이미지 및 상품 정보 보기 영역 시작 -->
   <table width="100%" cellpadding="5">
   <tr align="center">
   <td width="55%">
      <table width="100%">
      <tr><td align="center" valign="middle">
         <img src="product/pdt_img/<%=pdtInfo.getPi_img1()%>" width="300" height="300" />
      </td></tr>
      <tr><td align="center" valign="middle">
         <img src="product/pdt_img/<%=pdtInfo.getPi_img1()%>" width="50" height="50" />
<% if (pdtInfo.getPi_img2() != null && !pdtInfo.getPi_img2().equals("")) { %>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <img src="product/pdt_img/<%=pdtInfo.getPi_img2()%>" width="50" height="50" />
<% } %>
<% if (pdtInfo.getPi_img3() != null && !pdtInfo.getPi_img3().equals("")) { %>
         &nbsp;&nbsp;&nbsp;&nbsp;
         <img src="product/pdt_img/<%=pdtInfo.getPi_img3()%>" width="50" height="50" />
<% } %>
      </td></tr>
      </table>
   </td>
   <td width="*">
      <table width="100%" cellpadding="3">
      <tr>
      <th width="25%">분류</th>
      <td width="*"><%=pdtInfo.getCb_name() + " -> " + pdtInfo.getCs_name() %></td>
      </tr>
      <tr><th>브랜드</th><td><%=pdtInfo.getB_name() %></td></tr>
      <tr><th>상품명</th><td><%=pdtInfo.getPi_name() %></td></tr>
      <tr><th>판매가</th><td><%=pdtInfo.getPi_price() %></td></tr>
      <tr><th>할인율</th><td><%=pdtInfo.getPi_discount() %>%</td></tr>
      <tr><th>별점평균</th><td><%=pdtInfo.getPi_star() == 0.0 ? "별점 없음" : pdtInfo.getPi_star() + "점" %></td></tr>
      <form name="frmPdt" action="" method="post">
      <input type="hidden" name="piid" value="<%=pdtInfo.getPi_id()%>">
      <input type="hidden" name="occnt" value="1">
      <tr>
      <th>수량</th>
      <td>
         <input type="button" value="-" onclick="changeCnt(this.value);">
         <span id="cnt">1</span>
         <input type="button" value="+" onclick="changeCnt(this.value);">
      </td>
      </tr>
<% if (pdtInfo.getPi_option() != null && !pdtInfo.getPi_option().equals("")) { 
   String[] arrOpt = pdtInfo.getPi_option().split(";");
%>
      <tr>
      <th>옵션</th>
      <td>
         <select name="ocoption">
            <option value="">도수 선택</option>
<% for (int i = 0; i<arrOpt.length; i++) { %>
            <option value="<%=arrOpt[i] %>"><%=arrOpt[i] %></option>            
<%   } %>
         </select>
      </td>
      </tr>
<% } %>
      </form>
      <tr><td colspan="2" align="right">
         총 구매가격 : <span id="total"><%=pdtInfo.getPi_price() %></span> 원
      </td></tr>
      <tr><td colspan="2" align="center">
         <input type="button" value="장바구니 담기" onclick="cartBuy('c');">
         <input type="button" value="바로 구매하기" onclick="cartBuy('d');">
      </td></tr>
      </table>
   </td>
   </tr>
   </table>
   <!-- 상품 이미지 및 상품 정보 보기 영역 종료 -->
</td></tr>
</table>
</body>
</html>