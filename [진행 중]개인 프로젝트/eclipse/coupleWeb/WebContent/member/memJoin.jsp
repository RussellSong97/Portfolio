<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%
request.setCharacterEncoding("utf-8");
Calendar today = Calendar.getInstance();
int year = today.get(Calendar.YEAR);	// 올해 연도
int month = today.get(Calendar.MONTH) + 1;	// 현재 월
int day = today.get(Calendar.DATE);		// 오늘 일

String[] arrDomain = new String[5];
arrDomain[0] = "naver.com";
arrDomain[1] = "nate.com";
arrDomain[2] = "gmail.com";
arrDomain[3] = "daum.net";
arrDomain[4] = "yahoo.com";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="https://kit.fontawesome.com/05383ba415.js" crossorigin="anonymous"></script>
<script src="./jquery-3.6.0.js"></script>
<style>
@import url('//cdn.jsdelivr.net/font-iropke-batang/1.2/font-iropke-batang.css');
body{background-color:black; color:#dbe3eb;  position:absolute;
    left:50%;transform: translateX(-50%); -ms-overflow-style: none;
    font-family: 'Iropke Batang', serif;
    }
::-webkit-scrollbar { display: none; }
input, select {background-color:#E6E4E4;}
select{position:relative; top:-1px; height:21.7px; }
input[type=submit],input[type=reset] {background-color:#525151; color:#dbe3eb; padding:5px 10px; border-radius:10px;}
</style>
<script>
function chkValue(frm) {
	var agree = frm.agree.value;
	if (agree == "n") {
		alert("약관에 동의해야 회원가입이 가능합니다.");
		frm.agree.focus();		return false;
	}

	return true;
}

function setDomain(domain) {
	var e3 = document.frmJoin.e3;
	if (domain == "direct") {
		e3.value = "";		e3.focus();
	} else {
		e3.value = domain;
	}
}
</script>
</head>
<body>

<h2>회원가입</h2>
<form name="frmJoin" action="proc.mem" method="post" >
<input type="hidden" name="wtype" value="in" />
<input type="hidden" name="idChk" id="idChk" value="n" />
<div id="agreement" style="width:700px; height:100px;overflow:auto;">
약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />
</div>
<div style="width:700px;display: flex;justify-content: center;">
<br /><input type="radio" name="agree" value="y" />&nbsp;동의 함&nbsp;&nbsp;&nbsp;&nbsp;
<input type="radio" name="agree" value="n" checked="checked" />&nbsp;동의 안함<br /><br />
</div>
<table border="1" cellpadding="5" width="700">
<tr>
<th width="20%">아이디</th>
<td width="*">
	<input type="text" name="mi_id" onkeyup="chkDupID(this.value);" />
	<span id="idMsg" style="font-size:80%">아이디는 4~20자의 영문 및 숫자 조합으로 입력하세요.</span>
</td>
</tr>
<tr><th>비밀번호</th><td><input type="password" name="mi_pwd" /></td></tr>
<tr><th>이름</th><td><input type="text" name="mi_name" /></td></tr>
<tr>
<th>생년월일</th>
<td>
	<select name="by">
<% for (int i = 1950 ; i <= year ; i++) { %>
		<option value="<%=i%>" <% if(i == year) { %>selected="selected"<% } %>><%=i%></option>
<% } %>
	</select> 년
	<select name="bm">
<% for (int i = 1 ; i <= 12 ; i++) { %>
		<option value="<%=i%>" <% if(i == month) { %>selected="selected"<% } %>><%=i%></option>
<% } %>
	</select> 월
	<select name="bd">
<% for (int i = 1 ; i <= 31 ; i++) { %>
		<option value="<%=i%>" <% if(i == day) { %>selected="selected"<% } %>><%=i%></option>
<% } %>
	</select> 일
</td>
</tr>
<tr>
<th>성별</th>
<td>
	<input type="radio" name="mi_gender" value="m" />남
	<input type="radio" name="mi_gender" value="f" checked="checked" />여
</td>
</tr>
<tr>
<th>전화번호</th>
<td>
	<select name="p1">
		<option value="010">010</option>
		<option value="011">011</option>
		<option value="016">016</option>
		<option value="019">019</option>
	</select> -
	<input type="text" name="p2" size="4" maxlength="4" /> -
	<input type="text" name="p3" size="4" maxlength="4" />
</td>
</tr>
<tr>
<th>이메일</th>
<td>
	<input type="text" name="e1" /> @
	<select name="e2" onchange="setDomain(this.value);">
		<option value="">도메인 선택</option>
<% for (int i = 0 ; i < arrDomain.length ; i++) { %>
		<option value="<%=arrDomain[i] %>"><%=arrDomain[i] %></option>
<% } %>
		<option value="direct">직접 입력</option>
	</select>
	<input type="text" name="e3" />
</td>
</tr>
<tr>
<th>SNS 수신</th>
<td>
	<input type="radio" name="mi_issns" value="y" checked="checked" />수신
	<input type="radio" name="mi_issns" value="n" />미수신
</td>
</tr>
</table>
<p style="width:700px; text-align:center;">
	<input type="submit" value="회원 가입" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</p>
</form>
</body>
</html>