<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="mem.*"%>
<%
	MemberInfo login_info = (MemberInfo) session.getAttribute("login_info");
	if (login_info == null) {
		out.println("<script>");
		out.println("alert('로그인 후에 사용할 수 있습니다.');");
		out.println("location.href = 'loginForm.jsp';");
		out.println("</script>");
		out.close();
	}

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/testmall?useUnicode=true&characterEncoding=UTF8";
	url += "&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	request.setCharacterEncoding("utf-8");
	String schYear = request.getParameter("schYear");
	String schMonth = request.getParameter("schMonth");

	Calendar today = Calendar.getInstance();
	int year = today.get(Calendar.YEAR);
	int month = today.get(Calendar.MONTH);
	int day = today.get(Calendar.DATE);

	int sYear = year, sMonth = month;

	if (schYear != null && schMonth != null) {
		sYear = Integer.parseInt(schYear);
		sMonth = Integer.parseInt(schMonth);
	}

	int weekDay = 0;
	int endDay = 0;

	Calendar sdate = Calendar.getInstance();
	Calendar edate = Calendar.getInstance();

	sdate.set(sYear, sMonth, 1);
	edate.set(sYear, sMonth + 1, 1);
	edate.add(Calendar.DATE, -1);

	weekDay = sdate.get(Calendar.DAY_OF_WEEK);
	endDay = edate.get(Calendar.DATE);

	int nextYear = sYear, nextMonth = sMonth + 1;
	int prevYear = sYear, prevMonth = sMonth - 1;
	if (nextMonth == 12) {
		nextMonth = 0;
		nextYear++;
	}
	if (prevMonth == -1) {
		prevMonth = 11;
		prevYear--;
	}

	String pYear = "location.href='schedule.jsp?schYear=" + (sYear - 1) + "&schMonth=" + sMonth + "';";
	if (sYear == 1990)
		pYear = "alert('이전 연도가 없습니다.');";

	String nYear = "location.href='schedule.jsp?schYear=" + (sYear + 1) + "&schMonth=" + sMonth + "';";
	if (sYear == year)
		nYear = "alert('다음 연도가 없습니다.');";

	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "1234");
		stmt = conn.createStatement();

	} catch (Exception e) {
		out.println("일정관리 달력 작업 시 오류가 발생했습니다.");
		e.printStackTrace();
	}

	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
body {
	margin: 0;
}
 
.pdt, .pdt th, .pdt td {
	border: 1px black solid;
}

#col {
	border-collapse: collapse;
}

.pdt td {
	height: 70px;
}

.txtRed {
	color: red;
	font-weight: bold;
}

.txtBlue {
	color: blue;
	font-weight: bold;
}

#txtToday {
	background: #efefef;
}
.scheduleBox {
	width:400px; height:300px; background:#fbef84; padding:10px; overflow:auto;
	position:absolute; top:150px; left:150px; text-align:left; display:none;
}
</style>
<script>
function showSchedule(siidx){
	document.getElementById(siidx).style.display = "block";
}

function hideSchedule(siidx){
	document.getElementById(siidx).style.display = "none";
	
}

function isDel(idx){
	if (confirm("정말 삭제하시겠습니까?")) {
		location.href = "schedule_proc.jsp?w=del&idx=" + idx + "&y=<%=sYear%>&m=<%=sMonth + 1%>";	
	} 
}

</script>

</head>
<body>
	<div>
		<h2>일정 관리</h2>
		<form name="frm">
			<!-- action과 method는 기본값으로 현재 파일과 get을 가짐 -->
			<input type="button" value="이전 연도" onclick="<%=pYear%>" /> <input
				type="button" value="이전 달"
				onclick="location.href='schedule.jsp?schYear=<%=prevYear%>&schMonth=<%=prevMonth%>';" />
			<select name="schYear" onchange="this.form.submit();">
				<%
					String selected = "";
					for (int i = 1990; i <= year; i++) {
						selected = "";
						if (i == sYear)
							selected = " selected=\"selected\"";
				%>
				<option value="<%=i%>" <%=selected%>><%=i%></option>
				<%
					}
				%>
			</select>년 <select name="schMonth" onchange="this.form.submit();">
				<%
					for (int i = 0; i <= 11; i++) {
						selected = "";
						if (i == sMonth)
							selected = " selected=\"selected\"";
				%>
				<option value="<%=i%>" <%=selected%>><%=i + 1%></option>
				<%
					}
				%>
			</select>월 <input type="button" value="다음 달"
				onclick="location.href='schedule.jsp?schYear=<%=nextYear%>&schMonth=<%=nextMonth%>';" />
			<input type="button" value="다음 연도" onclick="<%=nYear%>" /> <input
				type="button" value="오늘" onclick="location.href='schedule.jsp';" />
		</form>
		<br />
		<table width="700" class="pdt" id="col">
			<tr height="30">
				<th width="100" class="txtRed">일</th>
				<th width="100">월</th>
				<th width="100">화</th>
				<th width="100">수</th>
				<th width="100">목</th>
				<th width="100">금</th>
				<th width="100" class="txtBlue">토</th>
			</tr>
			<%
				if (weekDay != 1) { // 1일이 일요일이 아니면(1일의 시작위치가 처음이 아니면)
					out.println("<tr>");
					for (int i = 1; i < weekDay; i++)
						out.println("<td></td>");
				}

				for (int i = 1, n = weekDay; i <= endDay; i++, n++) {
					// i : 날짜의 일(day)을 표현하기 위한 변수 / n : 일주일이 지날 때 마다 다음 줄로 내리기 위한 변수
					String txtClass = ""; 	// 요일별 색상 스타일을 적용하기 위한 클래스용 변수
					String txtID = ""; 		// 오늘 날짜인 경우 배경색 스타일을 적용하기 위한 아이디용 변수
					String schImg = "";		// 일정이 있을 경우 보여줄 이미지를 저장할 변수 

					if (n % 7 == 1) { // 요일번호가 1이면
						out.println("<tr>");
						txtClass = " class=\"txtRed\""; // 일요일이므로 날짜 색상 변경
					}
					if (n % 7 == 0)
						txtClass = " class=\"txtBlue\""; // 토요일이므로 날짜 색상 변경

					if (sYear == year && sMonth == month && i == day)
						txtID = " id=\"txtToday\"";
					// 현재 출력중인 날짜가 오늘 날짜인 경우 아이디를 지정해 스타일을 적용시킴
					
					String stime = sYear + "-" + ((sMonth < 9) ? "0" + (sMonth + 1) : sMonth +1)  + "-" + ((i < 10) ? "0" + i : i); 
					String sql = "select * from t_schedule_info where mi_id = '" + login_info.getMi_id() + "' and si_stime like '" + stime + "%' order by si_stime ; ";
					//System.out.println("sql : " + sql);
					rs = stmt.executeQuery(sql);
					if (rs.next()) {		// 해당일에 일정이 있으면 
						schImg  = "<a href=\"javascript:showSchedule('box" + i + "');\" >";
						schImg += "<img src='img/schedule_icon.png' width='20' /></a>";
						
	
				%>
						<div class="scheduleBox" id="box<%=i%>">
							<%=stime.substring(0, 10) %> 일정
							<img src="img/close.png" width="10" onclick="hideSchedule('box<%=i %>')" style="cursor:pointer" align="right" /><br/>
						<% do { 
						

							int idx = rs.getInt("si_idx");
						%>
						 
							일시 : <%=rs.getString("si_stime").substring(11) %>
							<input type="button" value="수정" onclick="location.href='schedule_form.jsp?w=up&idx=<%=idx %>';" />
							<input type="button" value="삭제" onclick="isDel(<%=idx %>)" /><br>
							<%=rs.getString("si_content").replace("\r\n", "<br />") %><hr />
				<% 
						} while (rs.next());					
						out.println("</div>");
			
					}
			
					String lnk = "<a href='schedule_form.jsp?y=" + sYear + "&m=" + sMonth + "&d=" + i + "&w=in'>";
					out.println("<td" + txtClass + txtID + " align='left' valign='top'>" + 
					      lnk + i + "</a>&nbsp;&nbsp;" + schImg +"</td>");


					if (n % 7 == 0) { // 요일번호가 7이면
						out.println("</tr>");
					} else if (i == endDay && n % 7 != 0) {
						// 말일까지 출력했으나 요일번호가 7이 아니어서 확실한 종료가 되지 않을 경우
						for (int j = n % 7; j < 7; j++) out.println("<td></td>");
						out.println("</tr>");
					}
				}
			%>
		</table>
	</div>
</body>
</html>
