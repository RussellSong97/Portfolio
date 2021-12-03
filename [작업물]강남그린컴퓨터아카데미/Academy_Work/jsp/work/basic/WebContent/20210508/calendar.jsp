<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %><!-- Calendar 클래스를 사용하기 위해 import -->
<%
request.setCharacterEncoding("utf-8");
// request로 받아오는 데이터들의 캐릭터 인코딩을 유니코드로 지정(영문을 제외한 타 언어가 깨지지 않게 하기 위해)
String schYear = request.getParameter("schYear");
String schMonth = request.getParameter("schMonth");
// 처음 열린 경우 받아오는 값이 없으므로 schYear와 schMonth에는 각각 null값이 들어감

Calendar today = Calendar.getInstance();	// 오늘 날짜 및 현재 시간으로 today 인스턴스 생성
int year = today.get(Calendar.YEAR);
int month = today.get(Calendar.MONTH);
int day = today.get(Calendar.DATE);
// today인스턴스 내의 년월일을 int형 데이터로 받아 옴

int sYear = year, sMonth = month;
// 실제 달력을 구현할 때 사용할 연도와 월로 기본값은 현재 연도와 월(값이 없을 경우 현재 연도와 월로 달력을 출력)

if (schYear != null && schMonth != null) {
// 검색할(보여줄) 연도와 월의 값을 받아 왔을 경우
	sYear = Integer.parseInt(schYear);
	sMonth = Integer.parseInt(schMonth);
	// schYear와 schMonth의 값이 String이므로 int형으로 형변환한 후 넣어 줌
	// 달력을 만들 날짜를 생성하기 위해 인수(set()메소드)로 사용할 연도와 월을 int형으로 만들어야 함
}

int weekDay = 0;	// 1일의 요일번호를 저장할 변수(1일의 출력 위치를 위한 변수)
int endDay = 0;		// 해당 월의 말일을 저장할 변수(루프의 조건을 위해 필요)

Calendar sdate = Calendar.getInstance();	// 달력의 시작일을 저장할 인스턴스
Calendar edate = Calendar.getInstance();	// 달력의 종료일을 저장할 인스턴스

sdate.set(sYear, sMonth, 1);		// 달력의 시작일 지정
edate.set(sYear, sMonth + 1, 1);	// 달력의 종료일을 추출하기 위해 다음달 1일로 지정
edate.add(Calendar.DATE, -1);		// 다음달 1일에서 1일을 뺀 날짜로 종료일을 지정

weekDay = sdate.get(Calendar.DAY_OF_WEEK);	// 시작일의 요일 번호로 달력의 시작점으로 사용됨
endDay = edate.get(Calendar.DATE);			// 말일로 루프의 종료점 즉 조건으로 사용됨

int nextYear = sYear, nextMonth = sMonth + 1;
int prevYear = sYear, prevMonth = sMonth - 1;
if (nextMonth == 12) {	// 현재 달이 12월이어서 다음 달의 값이 다음 연도로 넘어갈 경우
	nextMonth = 0;	nextYear++;
}
if (prevMonth == -1) {	// 현재 달이 1월이어서 이전 달의 값이 이전 연도로 넘어갈 경우
	prevMonth = 11;	prevYear--;
}
// 다음달과 이전달 버튼의 링크에서 사용할 연월을 생성

String pYear = "location.href='calendar.jsp?schYear=" + 
	(sYear - 1) + "&schMonth=" + sMonth + "';";
if (sYear == 1990)	pYear = "alert('이전 연도가 없습니다.');";

String nYear = "location.href='calendar.jsp?schYear=" + 
	(sYear + 1) + "&schMonth=" + sMonth + "';";
if (sYear == year)	nYear = "alert('다음 연도가 없습니다.');";
// 다음연도와 이전연도 버튼의 링크에서 사용할 js명령을 생성
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
body { margin:0; }
#wrapper { margin:0 auto; width:700px; text-align:center; }
.pdt, .pdt th, .pdt td { border:1px black solid; }
#col { border-collapse:collapse; }
.pdt td { height:70px; }
.txtRed { color:red; font-weight:bold; }
.txtBlue { color:blue; font-weight:bold; }
#txtToday { background:#efefef; }
</style>
</head>
<body>
<div id="wrapper">
	<h2>일정 관리 - <%=sYear %>년 <%=sMonth + 1 %>월</h2>
	<form name="frm"><!-- action과 method는 기본값으로 현재 파일과 get을 가짐 -->
	<input type="button" value="이전 연도" onclick="<%=pYear %>" />
	<input type="button" value="이전 달" onclick="location.href='calendar.jsp?schYear=<%=prevYear %>&schMonth=<%=prevMonth %>';" />
	<select name="schYear" onchange="this.form.submit();">
<%
String selected = "";
for (int i = 1990 ; i <= year ; i++) {
	selected = "";
	if (i == sYear)	selected = " selected=\"selected\"";
%>
	<option value="<%=i%>"<%=selected%>><%=i%></option>
<% } %>	</select>년
	<select name="schMonth" onchange="this.form.submit();">
<%
for (int i = 0 ; i <= 11 ; i++) {
	selected = "";
	if (i == sMonth)	selected = " selected=\"selected\"";
%>
	<option value="<%=i%>"<%=selected%>><%=i + 1%></option>
<% } %>
	</select>월
	<input type="button" value="오늘" onclick="location.href='calendar.jsp';" />
	<input type="button" value="다음 달" onclick="location.href='calendar.jsp?schYear=<%=nextYear %>&schMonth=<%=nextMonth %>';" />
	<input type="button" value="다음 연도" onclick="<%=nYear %>" />
	</form>
	<br />
	<table width="700" class="pdt" id="col">
	<tr height="30">
	<th width="100" class="txtRed">일</th><th width="100">월</th>
	<th width="100">화</th><th width="100">수</th><th width="100">목</th>
	<th width="100">금</th><th width="100" class="txtBlue">토</th>
	</tr>
<%
if (weekDay != 1) {	// 1일이 일요일이 아니면(1일의 시작위치가 처음이 아니면)
	out.println("<tr>");
	for (int i = 1 ; i < weekDay ; i++) out.println("<td></td>");
}

for (int i = 1, n = weekDay ; i <= endDay ; i++, n++) {
// i : 날짜의 일(day)을 표현하기 위한 변수 / n : 일주일이 지날 때 마다 다음 줄로 내리기 위한 변수
	String txtClass = "";	// 요일별 색상 스타일을 적용하기 위한 클래스용 변수
	String txtID = "";		// 오늘 날짜인 경우 배경색 스타일을 적용하기 위한 아이디용 변수

	if (n % 7 == 1) {	// 요일번호가 1이면
		out.println("<tr>");
		txtClass = " class=\"txtRed\""; // 일요일이므로 날짜 색상 변경
	}
	if (n % 7 == 0)	txtClass = " class=\"txtBlue\""; // 토요일이므로 날짜 색상 변경

	if (sYear == year && sMonth == month && i == day)	txtID = " id=\"txtToday\"";
	// 현재 출력중인 날짜가 오늘 날짜인 경우 아이디를 지정해 스타일을 적용시킴

	out.println("<td" + txtClass + txtID + ">" + i + "</td>");

	if (n % 7 == 0) {	// 요일번호가 7이면
		out.println("</tr>");
	} else if (i == endDay && n % 7 != 0) {
	// 말일까지 출력했으나 요일번호가 7이 아니어서 확실한 종료가 되지 않을 경우
		for (int j = n % 7 ; j < 7 ; j++)	out.println("<td></td>");
		out.println("</tr>");
	}
}
%>
	</table>
</div>
</body>
</html>
